package com.wifin.kachingme.listeners;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat.muc_chat.MUCTest;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.GroupParticipantGetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.NetworkSharedPreference;
import com.wifin.kachingme.util.Utils;
import com.wifin.kachingme.util.KachingMeConfig;

import org.apache.http.Header;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.bookmarks.BookmarkManager;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

//import cz.msebera.android.httpclient.Header;
//import com.loopj.android.http.AsyncHttpResponseHandler;

public class MUC_ListenerMethods {

    public static GroupParticipantGetSet group_partcipant_getset;
    public static DatabaseHelper dbadapter;
    public static SharedPreferences sp;
    public static MultiUserChat muc;
    public static boolean isRunning = true;
    public int ser_count = 0;
    String TAG = MUC_ListenerMethods.class.getSimpleName();
    Context context;
    Long timeout = 6000000L;
    Editor editor;
    String status_lock = "check";
    Dbhelper db;
    String contact_msg = "Received message from";
    SharedPreferences pref;

    public MUC_ListenerMethods(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(
                KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sp.edit();
        db = new Dbhelper(context);
    }

    public void setJoinRoom(final String jid, String inviter) {

        Constant.printMsg("print ....>> " + jid);

        try {

            Constant.printMsg("CCCCCCCCCC1");

            String room_subject = "";
            dbadapter = KachingMeApplication.getDatabaseAdapter();
            dbadapter.deleteGroup(jid);

            BookmarkManager bm = BookmarkManager
                    .getBookmarkManager(TempConnectionService.connection);
            bm.addBookmarkedConference(jid, jid, true, Utils.getBookmarkTime(),
                    "");
            String room_admin = null, createtion_time = null;
            String list = null;

			/* RoomInfo info = MultiUserChat.getRoomInfo(connection, jid); */

            SharedPreferences sp_1 = context.getSharedPreferences(
                    KachingMeApplication.getPereference_label(),
                    Activity.MODE_PRIVATE);
            String Bookmarked_time = Utils.getBookmarkTime();
            Editor editor1 = sp_1.edit();
            editor1.putString(Constant.LAST_REFRESH_TIME + "_" + jid,
                    Bookmarked_time);
            editor1.commit();
            Constant.printMsg("CCCCCCCCCC2");
            muc = TempConnectionService.MUC_MANAGER.getMultiUserChat(jid);
            Constant.printMsg("CCCCCCCCCC211");
            muc.addMessageListener(TempConnectionService.muc_messageListener);
            Constant.printMsg("CCCCCCCCCC2222");
            muc.addSubjectUpdatedListener(new MUC_SubjectChangeListener(
                    context));
            Constant.printMsg("CCCCCCCCCC2333");
            DiscussionHistory history = new DiscussionHistory();
            history.setSeconds(0);
            Constant.printMsg("CCCCCCCCCC2444");
            muc.join(KachingMeApplication.getUserID() + KachingMeApplication.getHost(),
                    null, history, timeout);
            Constant.printMsg("CCCCCCCCCC33333");

//------------------------------------
            Constant.printMsg("CCCCCCCCCC5");
            Collection<Affiliate> owner = muc.getOwners();

            int i = 0;

            for (Affiliate affiliate : owner) {
                if (i == 0) {
                    list = affiliate.getJid();
                } else {
                    list = list + "," + affiliate.getJid();
                }
                group_partcipant_getset = new GroupParticipantGetSet();

                group_partcipant_getset.setAdmin(0);

                group_partcipant_getset.setGjid(jid);
                group_partcipant_getset.setJid(affiliate.getJid());
                dbadapter.addGroupMembers(group_partcipant_getset);

				/*
                 * if (!affiliate.getJid().equals( KachingMeApplication.getUserID()
				 * + KachingMeApplication.getHost())) { MessageGetSet msg = new
				 * MessageGetSet(); msg.setData(""); msg.setKey_from_me(0);
				 * msg.setKey_id("" + new Date().getTime());
				 * msg.setKey_remote_jid(jid);
				 *
				 * msg.setNeeds_push(0); msg.setStatus(0); msg.setTimestamp(new
				 * Date().getTime());
				 * msg.setRemote_resource(affiliate.getJid().toString());
				 * msg.setMedia_wa_type("7"); dbadapter.setInsertMessages(msg);
				 * }
				 */
                i++;
                Log.d("MUC_info", "Owner::" + affiliate.getJid());
            }
//----------------------
            Form f1 = muc.getConfigurationForm();
            for (Iterator fields = f1.getFields().iterator(); fields.hasNext(); ) {
                FormField field = (FormField) fields.next();

                String s = "";

                Iterator<String> am = field.getValues().iterator();

                while (am.hasNext()) {
                    s = am.next();

                }
                if (field.getVariable().equals("muc#roomconfig_roomname")) {
                    room_subject = s;
                    Constant.printMsg("CCCCCCCCCC subjec " + room_subject);
                    long l = dbadapter.setUpdateSubject(jid, room_subject);
                }
                if (field.getVariable().equals("muc#roomconfig_roomdesc")) {

                    try {


                        room_admin = s;
                        JSONObject json = new JSONObject(s);
                        JSONObject j_obj = json.getJSONObject("data");

                        if(room_subject!=null) {
                            if (room_subject.equalsIgnoreCase("")) {
                                room_subject = j_obj.getString("group_name");
                                Constant.printMsg("CCCCCCCCCC subjec 11" + room_subject);
                                long l = dbadapter.setUpdateSubject(jid, room_subject);
                            }

                        }
                        else
                        {
                            room_subject = j_obj.getString("group_name");
                            Constant.printMsg("CCCCCCCCCC subjec 11" + room_subject);
                            long l = dbadapter.setUpdateSubject(jid, room_subject);
                        }
                        Log.d(TAG,
                                "Group Admin::"
                                        + j_obj.getString(Constant.GROUP_ADMIN));
                        try {
                            Log.d(TAG,
                                    "Group Subject::"
                                            + j_obj.getString("group_name"));
                        } catch (JSONException e) {
                            Constant.printMsg("CCCCCCCCCC subjec exp " + e.toString());
                        }
                        Log.d(TAG,
                                "Group Type::"
                                        + j_obj.getString(Constant.GROUP_TYPE));
                        if (j_obj.getString(Constant.GROUP_TYPE).equals("4")
                                || j_obj.getString(Constant.GROUP_TYPE)
                                .equals("5")) {
                        }

                        room_admin = j_obj.getString(Constant.GROUP_ADMIN);


                        editor.putString(jid + "_group_type",
                                j_obj.getString(Constant.GROUP_TYPE));
                        if (j_obj.getString(Constant.GROUP_TYPE).equals("4")
                                || j_obj.getString(Constant.GROUP_TYPE)
                                .equals("5")) {
                        }
                        createtion_time = j_obj.getString(Constant.TIMESTAMP);
                        editor.commit();

                        if (room_admin.contains(",")) {
                            String multi_admin[] = room_admin.split(",");
                            for (String admin : multi_admin) {
                                long l = dbadapter.updateGroupMembers(jid, admin, 1);
                                Log.d(TAG, "JOIN Room Admin updated ,,, ::" + l);
                            }
                        } else {
                            long l = dbadapter.updateGroupMembers(jid, room_admin, 1);
                            Log.d(TAG, "JOIN Room Admin updated::" + l);
                        }


                        Log.d("Muc_invitation", "Room Admin::" + room_admin);
                    } catch (Exception e) {
                        Constant.printMsg(e.toString());
                    }
                }
                // Constant.printMsg("Form Field::"+field.getLabel()+"::"+field.getVariable()+"::"+s);
            }
            Constant.printMsg("CCCCCCCCCC4");
            if (!dbadapter.isjidExist(jid)) {
                VCard vc_new = new VCard();

                vc_new = VCardManager.getInstanceFor(
                        TempConnectionService.connection).loadVCard(jid);

                ContactsGetSet contact = new ContactsGetSet();
                contact.setIs_niftychat_user(1);
                contact.setJid(jid);
                contact.setNumber("");
                contact.setDisplay_name(room_subject);
                contact.setNifty_name(vc_new.getFirstName());
                Constant.printMsg("print ::::1111 " + vc_new.getFirstName());

                contact.setPhoto_ts(new Utils().getGroupRandomeIcon(context));
                long l = dbadapter.insertContacts(contact);

                MessageGetSet msg = new MessageGetSet();
                msg.setData(room_subject);
                msg.setKey_from_me(0);
                msg.setKey_id("" + new Date().getTime());
                msg.setKey_remote_jid(jid);
                msg.setMedia_wa_type("9");
                msg.setNeeds_push(0);
                msg.setStatus(0);

                msg.setTimestamp(new Date().getTime());
                msg.setRemote_resource(room_admin);

                dbadapter.setInsertMessages(msg);

            }

            Constant.printMsg("CCCCCCCCCC6" + KachingMeApplication.getjid() + "   " + context.getSharedPreferences(KachingMeApplication.getPereference_label(),
                    Activity.MODE_PRIVATE).getString(
                    "MyPrimaryNumber", ""));

            String sp_jid = context.getSharedPreferences(KachingMeApplication.getPereference_label(),
                    Activity.MODE_PRIVATE).getString(
                    "MyPrimaryNumber", "");
            MessageGetSet msg = new MessageGetSet();
            msg.setData(sp_jid + KachingMeApplication.getHost());
            msg.setKey_from_me(0);
            msg.setKey_id("" + new Date().getTime());
            msg.setKey_remote_jid(jid);
            msg.setNeeds_push(0);
            msg.setStatus(0);
            msg.setTimestamp(new Date().getTime());
            msg.setRemote_resource(room_admin);
            msg.setMedia_wa_type("7");
            dbadapter.setInsertMessages(msg);

            SharedPreferences sp = context.getSharedPreferences(
                    KachingMeApplication.getPereference_label(),
                    Activity.MODE_PRIVATE);
            Editor editor = sp.edit();
            editor.putString(jid, list);
            editor.putString(jid + "_admin", room_admin);
            editor.commit();

            Log.d("NiftyService", "Is Room exist::" + dbadapter.isjidExist(jid));

            if (!dbadapter.isExistinChatList(jid)) {
                dbadapter.setInsertChat_list(jid, 0, createtion_time);
            }
            Constant.printMsg("CCCCCCCCCC7");
            boolean success = (new File(KachingMeApplication.PROFILE_PIC_DIR))
                    .mkdirs();

            final String jid1 = jid;
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(60000);
            String[] allowedContentTypes = new String[]{"image/png",
                    "image/jpeg"};
            client.get(KachingMeConfig.UPLOAD_GROUP_ICON_FOLDER_JPEG_PHP + jid.split("@")[0] + ".jpeg",
                    new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] binaryData, Throwable error) {

                            try {
                                byte[] randome = new Utils()
                                        .getGroupRandomeIcon(context);
                                Bitmap myBitmap = BitmapFactory
                                        .decodeByteArray(randome, 0,
                                                randome.length);
                                FileOutputStream stream = new FileOutputStream(
                                        new File(
                                                KachingMeApplication.PROFILE_PIC_DIR
                                                        + jid1.split("@")[0]
                                                        + ".png"));

                                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                                myBitmap.compress(Bitmap.CompressFormat.PNG,
                                        85, outstream);
                                byte[] byteArray = outstream.toByteArray();

                                stream.write(byteArray);
                                stream.close();

                            } catch (Exception e) {
                                // ACRA.getErrorReporter().handleException(e);
                                // TODO: handle exception
                            }
                        }

                        @Override
                        public void onFinish() {
                            // TODO Auto-generated method stub
                            Intent login_broadcast = new Intent(
                                    Constant.BROADCAST_UPDATE_GROUP_ICON);
                            login_broadcast.putExtra("jid", "" + jid);
                            context.sendBroadcast(login_broadcast);
                            super.onFinish();
                        }

						/*
                         * @Override public void onSuccess(byte[] fileData) {
						 *
						 * try { Bitmap myBitmap =
						 * BitmapFactory.decodeByteArray( fileData, 0,
						 * fileData.length); FileOutputStream stream = new
						 * FileOutputStream(new
						 * File(KachingMeApplication.PROFILE_PIC_DIR
						 * +jid1.split("@")[0]+".png"));
						 *
						 * ByteArrayOutputStream outstream = new
						 * ByteArrayOutputStream();
						 * myBitmap.compress(Bitmap.CompressFormat.PNG, 85,
						 * outstream); byte[] byteArray =
						 * outstream.toByteArray();
						 *
						 * stream.write(byteArray); stream.close();
						 *
						 *
						 * } catch (Exception e) { // TODO: handle exception }
						 *
						 * }
						 */

                        @Override
                        public void onSuccess(int arg0, Header[] arg1,
                                              byte[] fileData) {
                            try {
                                Bitmap myBitmap = BitmapFactory
                                        .decodeByteArray(fileData, 0,
                                                fileData.length);
                                FileOutputStream stream = new FileOutputStream(
                                        new File(
                                                KachingMeApplication.PROFILE_PIC_DIR
                                                        + jid1.split("@")[0]
                                                        + ".png"));

                                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                                myBitmap.compress(Bitmap.CompressFormat.PNG,
                                        85, outstream);
                                byte[] byteArray = outstream.toByteArray();

                                stream.write(byteArray);
                                stream.close();

                            } catch (Exception e) {
                                // ACRA.getErrorReporter().handleException(e);
                                // TODO: handle exception
                            }

                        }
                    });
            Constant.printMsg("CCCCCCCCCC8");
            int msg_id = dbadapter.getLastMsgid(jid);



            if (dbadapter.isExistinChatList(jid)) {
                dbadapter.setUpdateChat_lits(jid, msg_id);
            }

            Intent login_broadcast = new Intent("group_list");
            login_broadcast.putExtra("jid", "" + jid);
            context.sendBroadcast(login_broadcast);

            Intent login_broadcast_Chat = new Intent("lastseen_broadcast");
            login_broadcast_Chat.putExtra("from", jid);
            login_broadcast_Chat.putExtra("type", msg.getData());
            context.sendBroadcast(login_broadcast_Chat);

            Constant.printMsg("Room::BookMarked succesfully::" + jid);

            Log.d("MUC_invitation", "Subject::" + muc.getSubject());
            Constant.printMsg("CCCCCCCCCC9");

            if (inviter != null && !Utils.isActivityIsFront(context, SliderTesting.class.getCanonicalName().toString())) {

                    issue_notification_invitation(inviter, room_subject, jid);

            }

            // add_Listeners()
            //
            // ;

        } catch (Exception e) {
            // ACRA.getErrorReporter().handleException(e);
            e.printStackTrace();
            Constant.printMsg("CCCCCCCCCC10" + e.toString());
            // TODO: handle exception
        }
    }


    public void changg_group_admin(String gjid) {
        new ChangeGroupAdmin().execute(gjid);
    }

    public void issueNotification_Room(String key_id) {

        try {

            SharedPreferences preference = PreferenceManager
                    .getDefaultSharedPreferences(context);
            String strRingtonePreference = preference.getString(
                    "notify_ringtone", "DEFAULT_SOUND");
            Boolean conversation_sound = preference.getBoolean(
                    "conversation_sound", true);
            int notify_vibrate_length = Integer.parseInt(preference.getString(
                    "notify_vibrate_length", "1"));
            String notify_light_color = preference.getString(
                    "notify_light_color", "FFFFFF");

            dbadapter = KachingMeApplication.getDatabaseAdapter();

            MessageGetSet msggetset = dbadapter.getMessages_by_key_id(key_id);
            ContactsGetSet contact = dbadapter.getContact(msggetset
                    .getKey_remote_jid());
            ContactsGetSet contact_sender = dbadapter.getContact(msggetset
                    .getRemote_resource());
            String query = "select status from " + Dbhelper.TABLE_LOCK
                    + " where jid = '" + contact.getJid() + "'";
            Constant.printMsg("bhaththam " + query);

            lock_status(query);
            String notification_label = null, sender_label = null, msg_label = null;

            if (contact_sender != null
                    && contact_sender.getDisplay_name() != null) {
                sender_label = contact_sender.getDisplay_name();
            } else {
                sender_label = msggetset.getRemote_resource().toString()
                        .split("@")[0];
            }
            Constant.printMsg("locked group " + status_lock);

            if (status_lock.equalsIgnoreCase("lock")) {

                Constant.printMsg("locked group ");

                notification_label = " ";
                msg_label = " ";
                sender_label = " ";
            } else {
                if (msggetset.getMedia_wa_type().equals("0")) {
                    notification_label = msggetset.getData();
                    // Msg Network receive size
                    byte[] b = notification_label.getBytes("UTF-8");
                    long txtSize = (long) b.length;
                    Constant.printMsg("session rec" + txtSize);
                    updateNetwork_Message_Receive(txtSize);
                    if (isRunning) {
                        contact_msg = "you have received message from";
                        notification_label = notification_label.toString();
                        char s = notification_label.charAt(0);
                        char s1 = notification_label.charAt(1);
                        char s2 = notification_label.charAt(2);
                        if (s == '<') {
                            if (s1 == '-') {
//                                String[] parts = notification_label.toString()
//                                        .split("-");
//                                String part1 = parts[0];
//                                String part2 = parts[1];
//                                String part3 = parts[2];
//                                String part4 = parts[3];
//                                notification_label = part3;
//                                Constant.printMsg("called8 nym:::"
//                                        + notification_label);
                                notification_label = "Nymn";
                                contact_msg = "Received nymn message from";
                            } else if (s1 == 'b' && s2 == '>') {
                                Constant.printMsg("called8:1:::::::group"
                                        + notification_label.toString());
                                contact_msg = "Received DazZ message from";
                                // notification_label =
                                // text.toString().substring(3);
                                notification_label = " ";
                                Constant.printMsg("called8:1:::::::groupgroup"
                                        + notification_label);
                            } else if (s1 == 'z' && s2 == '>') {
                                Constant.printMsg("called8::::::4::"
                                        + notification_label.toString());
                                contact_msg = "Received message from";
                                // notification_label =
                                // text.toString().substring(3);
                                notification_label = "DazZ";

                                System.out
                                        .println("called8:1:::::::groupgroupgroup"
                                                + notification_label);
                            } else if (s1 == 'l' && s2 == '>') {
                                Constant.printMsg("called8:::5:::::"
                                        + notification_label.toString());
                                notification_label = notification_label
                                        .toString().substring(3);
                                System.out
                                        .println("called8:1:::::::groupgroupgroupgroup"
                                                + notification_label);
                                contact_msg = "Received message from";
                            } else if (s1 == 'x' && s2 == '>') {
                                Constant.printMsg("called8:::11:::::"
                                        + notification_label.toString());
                                String dazzle = notification_label.toString()
                                        .substring(3);
                                String[] parts = dazzle.split("-");
                                String part1 = parts[0];
                                String part2 = parts[1];
                                String part3 = parts[2];
                                String part4 = parts[3];
                                String part5 = parts[4];
                                // notification_label = part5;
                                notification_label = "DazZ";

                                contact_msg = "Received message from";
                                Constant.printMsg("called8:1:::::::"
                                        + notification_label);
                            } else if (s1 == 'k' && s2 == '>') {
                                System.out
                                        .println("called8:::11:::::groupgroupgroupgroupgroup"
                                                + notification_label.toString());
                                contact_msg = "Received message from";
                                // notification_label =
                                // text.toString().substring(3);
                                notification_label = "KonS";

                                // holder.txt_status.setTypeface(Typeface
                                // .createFromAsset(context.getAssets(),
                                // "billo.ttf"));
                                Constant.printMsg("called8:1:::::::"
                                        + notification_label);
                            } else if (s1 == 's' && s2 == '>') {
                                String[] parts = notification_label.split("-");
                                final String part1 = parts[0];
                                String part2 = parts[1];
                                notification_label = part2;
                                // holder.txt_status.setTypeface(Typeface
                                // .createFromAsset(context.getAssets(),
                                // "billo.ttf"));
                                System.out
                                        .println("called8:1:::::::groupgroupgroupgroupgroupgroup"
                                                + notification_label);
                                contact_msg = "you have received message from";
                            } else if (s1 == 'd' && s2 == '>') {
                                notification_label = contact.getDisplay_name()
                                        + " donates "
                                        + notification_label.substring(3)
                                        + " BuxS for you";
                                contact_msg = "you have received message from";

                            } else {
                                System.out
                                        .println("called8:56:::::::groupgroupgroupgroupgroupgroupgroupgroup"
                                                + notification_label.toString());

                                notification_label = msggetset.getData();
                                Constant.printMsg("called8:1111:::::::"
                                        + notification_label);
                                contact_msg = "you have received message from";
                            }
                        }
                    }
                } else if (msggetset.getMedia_wa_type().equals("1")) {

                    notification_label = "Image";
                } else if (msggetset.getMedia_wa_type().equals("2")) {
                    notification_label = "Video";
                } else if (msggetset.getMedia_wa_type().equals("3")) {
                    notification_label = "Audio";
                } else if (msggetset.getMedia_wa_type().equals("4")) {
                    notification_label = "Location";
                } else if (msggetset.getMedia_wa_type().equals("5")) {
                    notification_label = "Contact";
                } else if (msggetset.getMedia_wa_type().equals("6")) {
                    notification_label = "File";
                } else if (msggetset.getMedia_wa_type().equals("11")) {

                    notification_label = "Image";
                } else if (msggetset.getMedia_wa_type().equals("12")) {

                    notification_label = "Image";
                }
            }
            msg_label = sender_label + ": " + notification_label;
            Intent intent = new Intent(context, MUCTest.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("jid", msggetset.getKey_remote_jid());
            intent.putExtra("name", contact.getDisplay_name());
            /* intent.putExtra("avatar", contact.getPhoto_ts()); */
            Log.d(TAG,
                    "Notification Room jid::" + msggetset.getKey_remote_jid()
                            + " Name::" + contact.getDisplay_name());
            PendingIntent pIntent = PendingIntent.getActivity(context,
                    msggetset.get_id(), intent, 0);
            // Constant.printMsg("jid::" + msggetset.getKey_remote_jid());
            byte[] avatar = contact.getPhoto_ts();
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle(contact.getDisplay_name())
                    .setContentText(msg_label).setTicker(msg_label)
                    .setSmallIcon(R.drawable.ic_launcher)
                    /*
                     * .setLargeIcon( BitmapFactory.decodeByteArray(avatar,
					 * 0,avatar.length)).setAutoCancel(true)
					 */
                    .setContentIntent(pIntent).build();

            int received_msg_count = pref.getInt("received_msg_count", 0);
            Constant.printMsg("received msg grp::" + received_msg_count);
            received_msg_count = received_msg_count + 1;
            Constant.printMsg("received msg grp::" + received_msg_count);
            update_received_count(received_msg_count);

            if (conversation_sound) {
                if (strRingtonePreference.equals("DEFAULT_SOUND")) {
                    notification.defaults |= Notification.DEFAULT_SOUND;
                } else {
                    notification.sound = Uri.parse(strRingtonePreference);
                }
            }

            if (notify_vibrate_length == 1) {
                notification.defaults |= Notification.DEFAULT_VIBRATE;
            } else if (notify_vibrate_length == 2) {
                long[] l = {1000, 1000};
                notification.vibrate = l;
            } else if (notify_vibrate_length == 3) {
                long[] l = {2000, 2000};
                notification.vibrate = l;
            }

            if (notify_light_color.equals("1")) {
                notification.ledARGB = Color.WHITE;
                Log.d(TAG, "Notification Light Color::" + notify_light_color);
            } else if (notify_light_color.equals("2")) {
                // notification.ledARGB=Color.RED;
                notification.ledARGB = 0xffff0000;
                Log.d(TAG, "Notification Light Color::" + notify_light_color);
            } else if (notify_light_color.equals("3")) {
                notification.ledARGB = Color.YELLOW;
                Log.d(TAG, "Notification Light Color::" + notify_light_color);
            } else if (notify_light_color.equals("4")) {
                notification.ledARGB = Color.GREEN;
                Log.d(TAG, "Notification Light Color::" + notify_light_color);
            } else if (notify_light_color.equals("5")) {
                notification.ledARGB = Color.CYAN;
                Log.d(TAG, "Notification Light Color::" + notify_light_color);
            } else if (notify_light_color.equals("6")) {
                notification.ledARGB = Color.BLUE;
                Log.d(TAG, "Notification Light Color::" + notify_light_color);
            } else if (notify_light_color.equals("7")) {
                notification.ledARGB = Color.MAGENTA;
                Log.d(TAG, "Notification Light Color::" + notify_light_color);
            } else if (notify_light_color.equals("11")) {
                notification.ledARGB = Color.WHITE;
                Log.d(TAG, "Notification Light Color::" + notify_light_color);
            }

            notification.ledOnMS = 100;
            notification.ledOffMS = 100;

            notification.flags |= Notification.FLAG_SHOW_LIGHTS
                    | Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(contact.getId(), notification);
            KachingMeApplication.setIS_FROM_NOTIFICATION(true);
            Constant.printMsg("Noti::Finished..");
        } catch (Exception e) {
            // TODO: handle exception
            // ACRA.getErrorReporter().handleException(e);
            e.printStackTrace();
        }
        // h.sendMessage(msg);

    }

    private String lock_status(String query) {
        // TODO Auto-generated method stub
        Cursor c = null;

        try {

            Constant.printMsg("query  " + query);

            c = db.open().getDatabaseObj().rawQuery(query, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
            c.moveToFirst();
            if (c.getCount() > 0) {
                Constant.printMsg("lock_status " + c.getString(0));
                status_lock = c.getString(0);
                Constant.printMsg("status_lock   " + status_lock);
            } else {
                status_lock = "not_lock";
            }

        } catch (SQLException e) {

        } finally {
            c.close();
            db.close();
        }
        return status_lock;

    }

    // Updating the Shared Preference with Messaging Sent Network Usage....
    public void updateNetwork_Message_Receive(long bite) {
        NetworkSharedPreference mNewtSharPref = new NetworkSharedPreference(
                context);
        long val = 0;
        // get user data from session
        HashMap<String, String> user = mNewtSharPref
                .getMessage_ReceiveDetails();

        // name
        String value = user.get(NetworkSharedPreference.KEY_MESSAGE_GET_RX);
        if (value != null) {

            val = Long.parseLong(value);
        }
        long update = bite + val;

        String data = String.valueOf(update);

        mNewtSharPref.setMessageData_Receive(data);
    }

    public void update_received_count(int code) {

        ser_count++;

        Constant.printMsg("received msg update_received_count added is Nifty::"
                + code);
        Editor e = pref.edit();
        e.putInt("received_msg_count", code);
        Constant.printMsg("received_msg_count in nifty service:::" + code);
        e.commit();

    }

    public void issue_notification_invitation(String inviter, String groupName, String grpJid) {

        String sender_label = "";


        SharedPreferences preference = PreferenceManager
                .getDefaultSharedPreferences(context);
        String strRingtonePreference = preference.getString(
                "notify_ringtone", "DEFAULT_SOUND");
        Boolean conversation_sound = preference.getBoolean(
                "conversation_sound", true);
        int notify_vibrate_length = Integer.parseInt(preference.getString(
                "notify_vibrate_length", "1"));
        String notify_light_color = preference.getString(
                "notify_light_color", "FFFFFF");

        dbadapter = KachingMeApplication.getDatabaseAdapter();

        ContactsGetSet contact = new ContactsGetSet();
        try {
            contact = dbadapter.getContact(inviter.split("/")[0]);
            Constant.printMsg("grp username : " + contact);
            if (contact == null) {

                sender_label = inviter.split("@")[0];
            } else {
                if (contact.getDisplay_name() != null)
                    sender_label = contact.getDisplay_name();
                else
                    sender_label = inviter.split("@")[0];
            }

        } catch (Exception e) {
            // ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
        }


        String msg_label = sender_label + " added you in the group " + groupName;
        Intent intent = new Intent(context, MUCTest.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("jid", grpJid);
        intent.putExtra("name", groupName);
            /* intent.putExtra("avatar", contact.getPhoto_ts()); */
        Log.d(TAG,
                "Notification Room jid::" + grpJid
                        + " Name::" + contact.getDisplay_name());
        PendingIntent pIntent = PendingIntent.getActivity(context,
                12345, intent, 0);
        // Constant.printMsg("jid::" + msggetset.getKey_remote_jid());
        byte[] avatar = contact.getPhoto_ts();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle("Group Invitation")
                .setContentText(msg_label).setTicker(msg_label)
                .setSmallIcon(R.drawable.ic_launcher)
                    /*
					 * .setLargeIcon( BitmapFactory.decodeByteArray(avatar,
					 * 0,avatar.length)).setAutoCancel(true)
					 */
                .setContentIntent(pIntent).build();


        if (conversation_sound) {
            if (strRingtonePreference.equals("DEFAULT_SOUND")) {
                notification.defaults |= Notification.DEFAULT_SOUND;
            } else {
                notification.sound = Uri.parse(strRingtonePreference);
            }
        }

        if (notify_vibrate_length == 1) {
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        } else if (notify_vibrate_length == 2) {
            long[] l = {1000, 1000};
            notification.vibrate = l;
        } else if (notify_vibrate_length == 3) {
            long[] l = {2000, 2000};
            notification.vibrate = l;
        }

        if (notify_light_color.equals("1")) {
            notification.ledARGB = Color.WHITE;
            Log.d(TAG, "Notification Light Color::" + notify_light_color);
        } else if (notify_light_color.equals("2")) {
            // notification.ledARGB=Color.RED;
            notification.ledARGB = 0xffff0000;
            Log.d(TAG, "Notification Light Color::" + notify_light_color);
        } else if (notify_light_color.equals("3")) {
            notification.ledARGB = Color.YELLOW;
            Log.d(TAG, "Notification Light Color::" + notify_light_color);
        } else if (notify_light_color.equals("4")) {
            notification.ledARGB = Color.GREEN;
            Log.d(TAG, "Notification Light Color::" + notify_light_color);
        } else if (notify_light_color.equals("5")) {
            notification.ledARGB = Color.CYAN;
            Log.d(TAG, "Notification Light Color::" + notify_light_color);
        } else if (notify_light_color.equals("6")) {
            notification.ledARGB = Color.BLUE;
            Log.d(TAG, "Notification Light Color::" + notify_light_color);
        } else if (notify_light_color.equals("7")) {
            notification.ledARGB = Color.MAGENTA;
            Log.d(TAG, "Notification Light Color::" + notify_light_color);
        } else if (notify_light_color.equals("11")) {
            notification.ledARGB = Color.WHITE;
            Log.d(TAG, "Notification Light Color::" + notify_light_color);
        }

        notification.ledOnMS = 100;
        notification.ledOffMS = 100;

        notification.flags |= Notification.FLAG_SHOW_LIGHTS
                | Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(12345, notification);
        KachingMeApplication.setIS_FROM_NOTIFICATION(true);
    }

    class ChangeGroupAdmin extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            Log.d(TAG, "Change Admin Async called..");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String jid = params[0];
            String room_admin = null, createtion_time;
            // TODO Auto-generated method stub
            try {
                muc = TempConnectionService.MUC_MANAGER
                        .getMultiUserChat(jid);
                muc.addMessageListener(TempConnectionService.muc_messageListener);
                muc.addSubjectUpdatedListener(new MUC_SubjectChangeListener(
                        context));
                DiscussionHistory history = new DiscussionHistory();
                history.setSeconds(0);

                muc.join(
                        KachingMeApplication.getUserID()
                                + KachingMeApplication.getHost(), null, history,
                        timeout);

                Form f1 = muc.getConfigurationForm();

                for (Iterator fields = f1.getFields().iterator(); fields
                        .hasNext(); ) {
                    FormField field = (FormField) fields.next();

                    String s = "";

                    Iterator<String> am = field.getValues().iterator();

                    int count_admin = 0;
                    while (am.hasNext()) {
                        s = am.next();
                        count_admin++;
                        Constant.printMsg("LLLLLL group admin loop :" + count_admin);
                    }

                    if (field.getVariable().equals("muc#roomconfig_roomdesc")) {
                        room_admin = s;
                        JSONObject json = new JSONObject(s);
                        JSONObject j_obj = json.getJSONObject("data");

                        Log.d(TAG,
                                "Group Admin::"
                                        + j_obj.getString(Constant.GROUP_ADMIN));
                        Log.d(TAG,
                                "Group Type::"
                                        + j_obj.getString(Constant.GROUP_TYPE));
                        Log.d(TAG,
                                "Group Question::"
                                        + j_obj.getString(Constant.GROUP_TOPIC));
                        if (j_obj.getString(Constant.GROUP_TYPE).equals("4")
                                || j_obj.getString(Constant.GROUP_TYPE)
                                .equals("5")) {
                            Log.d(TAG,
                                    "Question options::"
                                            + j_obj.getString(Constant.GROUP_TOPIC));
                        }

                        room_admin = j_obj.getString(Constant.GROUP_ADMIN);
                        editor.putString(jid + "_group_type",
                                j_obj.getString(Constant.GROUP_TYPE));
                        editor.putString(jid + "_group_question",
                                j_obj.getString(Constant.GROUP_TOPIC));
                        if (j_obj.getString(Constant.GROUP_TYPE).equals("4")
                                || j_obj.getString(Constant.GROUP_TYPE)
                                .equals("5")) {
                            editor.putString(jid + "_group_question_options",
                                    j_obj.getString(Constant.TOPIC_OPTION));
                        }
                        createtion_time = j_obj.getString(Constant.TIMESTAMP);
                        editor.commit();

                        Log.d("Muc_invitation", "Room Admin::" + room_admin);
                        long l = dbadapter.updateGroupMembers(jid, room_admin, 1);
                        Log.d(TAG, "Room Admin updated::" + l);
                    }

                }

            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
                e.printStackTrace();
            }
            return null;
        }

    }

    public static void   sendReceiptDelivered(String toJID, String id) {
        final Message ack = new Message(toJID, Message.Type.groupchat);
        ack.setBody(Constant.STATUS_DELIVERED);
        ack.setStanzaId(id);
        ack.addExtension(new DeliveryReceipt(id));
        try {
            TempConnectionService.connection.sendStanza(ack);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

    }

}
