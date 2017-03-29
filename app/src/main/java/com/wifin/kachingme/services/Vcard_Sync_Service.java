package com.wifin.kachingme.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.listeners.MUC_SubjectChangeListener;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.GroupParticipantGetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.util.AvatarManager;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Utils;
import com.wifin.kachingme.util.KachingMeConfig;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.Roster.SubscriptionMode;
import org.jivesoftware.smackx.bookmarks.BookmarkManager;
import org.jivesoftware.smackx.bookmarks.BookmarkedConference;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xroster.RosterExchangeManager;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import a_vcard.android.util.Log;

public class Vcard_Sync_Service extends IntentService {

    public static String MOBILENO;
    public static String TAG = Vcard_Sync_Service.class.getSimpleName();
    Handler h;
    Thread refresh;
    MultiUserChat muc;
    DatabaseHelper dbAdapter;
    VCard vc = new VCard();
    ArrayList<String> Roster_list = new ArrayList<String>();
    int no = 0;
    SharedPreferences sp;
    Editor editor;
    String country_code;
    GroupParticipantGetSet group_partcipant_getset;
    private int result = Activity.RESULT_CANCELED;

    public Vcard_Sync_Service() {
        super(Vcard_Sync_Service.class.getSimpleName());

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub

        dbAdapter = KachingMeApplication.getDatabaseAdapter();
        sp = getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        editor = sp.edit();
        country_code = sp.getString(Constant.COUNTRY_CODE_LABEL, "");

        ArrayList<ContactsGetSet> users = dbAdapter.getContacts();
        Log.d(TAG, "NifrtChat Users::" + users.size());
        for (int i = 0; i < users.size(); i++) {

            ContactsGetSet user = users.get(i);
            Log.d(TAG, "Mobile No After::" + user.getJid());

            try {

                try {
                    vc = VCardManager.getInstanceFor(
                            TempConnectionService.connection).loadVCard(
                            user.getJid());
                } catch (Exception e) {

                    vc = new VCard();
                }

                if (vc.getJabberId() != null) {
                    dbAdapter.setDeleteContact(user.getJid());
                    Log.d("Vcard",
                            "Vcard Called..." + vc.getJabberId() + " Name::"
                                    + vc.getFirstName() + "Dname"
                                    + user.getDisplay_name());

                    ContactsGetSet contects = new ContactsGetSet();
                    contects.setDisplay_name(user.getDisplay_name());
                    contects.setIs_niftychat_user(1);
                    contects.setJid(vc.getJabberId());
                    contects.setNumber(user.getJid());
                    contects.setPhone_label("");
                    contects.setRaw_contact_id("" + user.getRaw_contact_id());
                    contects.setPhone_type(" ");
                    contects.setUnseen_msg_count(0);
                    contects.setNifty_name(vc.getFirstName());

                    contects.setNifty_email(vc.getEmailWork());

                    try {

                        contects.setStatus(vc.getField("SORT-STRING"));

                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                    contects.setPhoto_ts(new AvatarManager().saveBitemap(vc
                            .getAvatar()));

                    if (!contects.getJid().equals(
                            KachingMeApplication.getUserID()
                                    + KachingMeApplication.getHost())) {
                        dbAdapter.insertContacts(contects);
                    }

                    if (!contects.getJid().equals(
                            KachingMeApplication.getUserID()
                                    + KachingMeApplication.getHost())) {
                        Log.d(TAG, "Subscribed user::" + vc.getJabberId());
                        Presence subscribed = new Presence(
                                Presence.Type.subscribe);
                        subscribed.setTo(vc.getJabberId());
                        TempConnectionService.connection.sendStanza(subscribed);

                        Roster roster = Roster
                                .getInstanceFor(TempConnectionService.connection);
                        roster.setSubscriptionMode(SubscriptionMode.accept_all);
                        roster.createEntry(vc.getJabberId(), vc.getJabberId(),
                                null);

                        RosterExchangeManager rem = new RosterExchangeManager(
                                TempConnectionService.connection);
                        rem.send(roster, vc.getJabberId());

                    }

                    Intent intn = new Intent("Add_New_Contact");
                    sendBroadcast(intn);

                }

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        editor.putBoolean(Constant.CONTACT_GROUP_SYNC_STATUS, true);
        editor.commit();

        Intent intn = new Intent("Profile_Update");
        sendBroadcast(intn);

        String createtion_time = null;
        try {
            // MultiUserChatManager muc_manager =
            // TempConnectionService.MUC_MANAGER;

            BookmarkManager bm = BookmarkManager
                    .getBookmarkManager(TempConnectionService.connection);
            BookmarkManager bm_1 = BookmarkManager
                    .getBookmarkManager(TempConnectionService.connection);
            ArrayList<String> rm_bm = new ArrayList<String>();

            Collection<BookmarkedConference> l = bm.getBookmarkedConferences();

            Log.d(TAG, "Bookmarks::" + l.size());

            for (BookmarkedConference bookmarkedConference : l) {
                RoomInfo room_info = null;
                try {
                    room_info = TempConnectionService.MUC_MANAGER
                            .getRoomInfo(bookmarkedConference.getJid());

                } catch (Exception e) {

                }

                try {
                    if (room_info != null) {

                        String room_subject = null;
                        // Constant.printMsg("Room Name::"+room_info.getSubject()+" Room::"+room_info.getRoom());
                        muc = TempConnectionService.MUC_MANAGER
                                .getMultiUserChat(bookmarkedConference.getJid());
                        muc.addMessageListener(TempConnectionService.muc_messageListener);
                        muc.addSubjectUpdatedListener(new MUC_SubjectChangeListener(
                                this));

                        DiscussionHistory history = new DiscussionHistory();
                        history.setMaxStanzas(0);
                        muc.join(KachingMeApplication.getUserID()
                                        + KachingMeApplication.getHost(), null, history,
                                30000L);
                        // muc.join(KachingMeApplication.getUserID()+KachingMeApplication.getHost());

                        Form f1 = muc.getConfigurationForm();

                        String room_admin = null;

                        for (Iterator fields = f1.getFields().iterator(); fields
                                .hasNext(); ) {
                            FormField field = (FormField) fields.next();

                            String s = "";

                            Iterator<String> am = field.getValues().iterator();

                            while (am.hasNext()) {
                                s = am.next();

                            }
                            if (field.getVariable().equals(
                                    "muc#roomconfig_roomname")) {
                                room_subject = s;
                            }

                            if (field.getVariable().equals(
                                    "muc#roomconfig_roomdesc")) {
                                /* room_admin = s; */
                                try {
                                    JSONObject json = new JSONObject(s);
                                    JSONObject j_obj = json
                                            .getJSONObject("data");

                                    Log.d(TAG,
                                            "Group Admin::"
                                                    + j_obj.getString(Constant.GROUP_ADMIN));
                                    Log.d(TAG,
                                            "Group Type::"
                                                    + j_obj.getString(Constant.GROUP_TYPE));
                                    Log.d(TAG,
                                            "Group Question::"
                                                    + j_obj.getString(Constant.GROUP_TOPIC));
                                    if (j_obj.getString(Constant.GROUP_TYPE)
                                            .equals("4")
                                            || j_obj.getString(
                                            Constant.GROUP_TYPE)
                                            .equals("5")) {
                                        Log.d(TAG,
                                                "Question options::"
                                                        + j_obj.getString(Constant.TOPIC_OPTION));
                                    }

                                    room_admin = j_obj
                                            .getString(Constant.GROUP_ADMIN);
                                    editor.putString(muc.getRoom()
                                            + "_group_type", j_obj
                                            .getString(Constant.GROUP_TYPE));
                                    editor.putString(muc.getRoom()
                                            + "_group_question", j_obj
                                            .getString(Constant.GROUP_TOPIC));
                                    if (j_obj.getString(Constant.GROUP_TYPE)
                                            .equals("4")
                                            || j_obj.getString(
                                            Constant.GROUP_TYPE)
                                            .equals("5")) {
                                        editor.putString(
                                                muc.getRoom()
                                                        + "_group_question_options",
                                                j_obj.getString(Constant.TOPIC_OPTION));
                                    }
                                    createtion_time = j_obj
                                            .getString(Constant.TIMESTAMP);
                                    editor.putString(muc.getRoom() + "_admin",
                                            room_admin);
                                    editor.commit();
                                } catch (Exception e) {
                                    // ACRA.getErrorReporter().handleException(e);
                                    // TODO: handle exception
                                }
                                Log.d("Muc_invitation", "Room Admin::"
                                        + room_admin);
                            }
                            // //Constant.printMsg("Form Field::"+field.getLabel()+"::"+field.getVariable()+"::"+s);
                        }

                        String list = null;
                        Collection<Affiliate> owner = muc.getOwners();
                        int i = 0;
                        for (Affiliate affiliate : owner) {
                            if (i == 0) {
                                list = affiliate.getJid();
                            } else {
                                list = list + "," + affiliate.getJid();
                            }

                            group_partcipant_getset = new GroupParticipantGetSet();
                            if (room_admin.equals(affiliate.getJid()))
                                group_partcipant_getset.setAdmin(1);
                            else
                                group_partcipant_getset.setAdmin(0);
                            group_partcipant_getset.setGjid(muc.getRoom());
                            group_partcipant_getset.setJid(affiliate.getJid());
                            dbAdapter.addGroupMembers(group_partcipant_getset);

                            i++;
                            Log.d("MUC_info", "Owner::" + affiliate.getJid());
                        }

                        // Log.d("MUC Invitation","Member List::"+list+" Admin::"+room_admin.replaceAll(",",""));
                        SharedPreferences sp = getSharedPreferences(
                                KachingMeApplication.getPereference_label(),
                                Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(muc.getRoom(), list);

                        editor.commit();

                        if (!dbAdapter
                                .isjidExist(bookmarkedConference.getJid())) {
                            ContactsGetSet contact = new ContactsGetSet();
                            contact.setIs_niftychat_user(1);
                            contact.setJid(bookmarkedConference.getJid());
                            contact.setDisplay_name(room_subject);
                            contact.setRaw_contact_id("" + 0);
                            contact.setPhoto_ts(new Utils()
                                    .getGroupRandomeIcon(this));
                            dbAdapter.insertContacts(contact);

                            int msg_id = dbAdapter
                                    .getLastMsgid_chat_grp(bookmarkedConference
                                            .getJid());
                            if (!dbAdapter
                                    .isExistinChatList(bookmarkedConference
                                            .getJid())) {
                                dbAdapter.setInsertChat_list(
                                        bookmarkedConference.getJid(), msg_id,
                                        createtion_time);
                            }

                            MessageGetSet msg = new MessageGetSet();
                            msg.setData(room_subject);
                            msg.setKey_from_me(0);
                            msg.setKey_id("" + new Date().getTime());
                            msg.setKey_remote_jid(muc.getRoom());
                            msg.setMedia_wa_type("9");
                            msg.setNeeds_push(0);
                            msg.setStatus(0);
                            msg.setTimestamp(new Date().getTime());
                            msg.setRemote_resource(room_admin);

                            dbAdapter.setInsertMessages(msg);

                        }

                        if (!room_admin.equals(KachingMeApplication.getjid())) {
                            MessageGetSet msg = new MessageGetSet();
                            msg.setData(KachingMeApplication.getjid());
                            msg.setKey_from_me(0);
                            msg.setKey_id("" + new Date().getTime());
                            msg.setKey_remote_jid(muc.getRoom());
                            msg.setNeeds_push(0);
                            msg.setStatus(0);
                            msg.setTimestamp(new Date().getTime());
                            msg.setRemote_resource(room_admin);
                            msg.setMedia_wa_type("7");
                            dbAdapter.setInsertMessages(msg);
                        }

                        int unseen_msg = dbAdapter.getunseen_msg(muc.getRoom());
                        int msg_id = dbAdapter.getLastMsgid(muc.getRoom());
                        dbAdapter.setUpdateContact_unseen_msg(muc.getRoom(),
                                unseen_msg);
                        if (dbAdapter.isExistinChatList(muc.getRoom())) {
                            dbAdapter.setUpdateChat_lits(muc.getRoom(), msg_id);
                        } else {
                            dbAdapter.setInsertChat_list(muc.getRoom(), msg_id);
                        }

                        dbAdapter.setUpdateChat_lits_timestamp(muc.getRoom(),
                                createtion_time);

                        (new File(KachingMeApplication.PROFILE_PIC_DIR)).mkdirs();

                        final String jid1 = muc.getRoom();

                        result = Activity.RESULT_CANCELED;
                        String urlPath = KachingMeConfig.UPLOAD_GROUP_ICON_FOLDER_JPEG_PHP
                                + "uploads//groupicon//"
                                + muc.getRoom().split("@")[0] + ".jpeg";
                        String fileName = KachingMeApplication.PROFILE_PIC_DIR
                                + jid1.split("@")[0] + ".png";
                        File output = new File(fileName);
                        if (output.exists()) {
                            output.delete();
                        }

                        InputStream stream = null;
                        FileOutputStream fos = null;
                        try {

                            URL url = new URL(urlPath);
                            stream = url.openConnection().getInputStream();

                            Bitmap myBitmap = BitmapFactory
                                    .decodeStream(stream);

                            FileOutputStream fo = new FileOutputStream(
                                    new File(KachingMeApplication.PROFILE_PIC_DIR
                                            + jid1.split("@")[0] + ".png"));

                            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                            myBitmap.compress(Bitmap.CompressFormat.PNG, 85,
                                    outstream);
                            byte[] byteArray = outstream.toByteArray();

                            fo.write(byteArray);
                            fo.close();

                            // Successful finished
                            Log.d(TAG, "Donwloaded Image::" + urlPath);
                            result = Activity.RESULT_OK;

                        } catch (Exception e) {
                            // ACRA.getErrorReporter().handleException(e);
                            e.printStackTrace();
                        } finally {
                            if (stream != null) {
                                try {
                                    stream.close();
                                 } catch (IOException e) {
                                    // ACRA.getErrorReporter().handleException(e);
                                    e.printStackTrace();
                                }
                            }
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    // ACRA.getErrorReporter().handleException(e);
                                    e.printStackTrace();
                                }
                            }
                        }

                        if (result == Activity.RESULT_CANCELED) {
                            try {
                                byte[] randome = new Utils()
                                        .getGroupRandomeIcon(Vcard_Sync_Service.this);
                                Bitmap myBitmap = BitmapFactory
                                        .decodeByteArray(randome, 0,
                                                randome.length);
                                FileOutputStream stream1 = new FileOutputStream(
                                        new File(
                                                KachingMeApplication.PROFILE_PIC_DIR
                                                        + jid1.split("@")[0]
                                                        + ".png"));

                                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                                myBitmap.compress(Bitmap.CompressFormat.PNG,
                                        85, outstream);
                                byte[] byteArray = outstream.toByteArray();

                                stream1.write(byteArray);
                                stream1.close();

                            } catch (Exception e) {
                                // ACRA.getErrorReporter().handleException(e);
                                // TODO: handle exception
                            }
                        }

                        Intent inten = new Intent(
                                Constant.BROADCAST_UPDATE_GROUP_ICON);
                        sendBroadcast(inten);
                    } else {
                        rm_bm.add(bookmarkedConference.getJid());
                    }
                } catch (Exception e) {
                    // ACRA.getErrorReporter().handleException(e);
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            for (String string : rm_bm) {
                Log.d("Room Removes", string);
                bm_1.removeBookmarkedConference(string);
            }
        } catch (Exception e) {
            // ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
            e.printStackTrace();
        }

        Log.d("ContactSync", "App Sync Finished....1");

    }

}
