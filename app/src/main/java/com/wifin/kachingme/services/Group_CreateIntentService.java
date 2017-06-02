package com.wifin.kachingme.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat.muc_chat.NewGroup_Info;
import com.wifin.kachingme.chat.muc_chat.NewGroup_MemberList;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.listeners.MUC_SubjectChangeListener;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.GroupParticipantGetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.Utils;

import org.apache.http.Header;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.bookmarks.BookmarkManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.json.JSONException;
import org.json.JSONObject;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Wifin on 29-03-2017.
 */
public class Group_CreateIntentService extends IntentService {


    public static ArrayList<ContactsGetSet> selected_users;
    String mem_list;
    JSONObject main_job = new JSONObject();
    JSONObject json_tags = new JSONObject();
    MessageGetSet msg = new MessageGetSet();
  //  String icon_url = "";
    String room_subject, grp_id;
    GroupParticipantGetSet group_partcipant_getset;
    DatabaseHelper dbAdapter;
    MultiUserChat multiUserChat = null;

    public Group_CreateIntentService() {
        super("Group_CreateIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        room_subject = intent.getStringExtra("room_subject");
        selected_users = NewGroup_MemberList.selected_users;

        dbAdapter = KachingMeApplication.getDatabaseAdapter();

        Date timestamp = new Date();
        int k = 0;
        String question_options = null;
        for (String string : NewGroup_Info.ar_list_option) {
            if (k == 0) {
                question_options = string;
            } else {
                question_options = question_options + "," + string;
            }
            k++;
        }

        try {

            json_tags.put(
                    Constant.GROUP_ADMIN,
                    KachingMeApplication.getUserID()
                            + KachingMeApplication.getHost());
            json_tags.put(Constant.GROUP_TYPE, ""
                    + NewGroup_Info.group_type);
            json_tags.put(Constant.GROUP_TOPIC,
                    NewGroup_Info.txt_questions.getText());
            json_tags.put(Constant.TOPIC_OPTION, question_options);
            json_tags.put(Constant.TIMESTAMP,
                    "" + System.currentTimeMillis() / 1000L);
            main_job.put("data", json_tags);
//            Log.d(TAG, "JSON String::" + main_job.toString());
        } catch (JSONException e1) {

            e1.printStackTrace();
        }
        Constant.printMsg("create room ::::: @conference."
                + KachingMeApplication.getHost_12());
        grp_id = KachingMeApplication.getUserID() + "-" + timestamp.getTime()
                + "@conference." + KachingMeApplication.getHost_12();

        // muc = TempConnectionEstablishment.MUC_MANAGER
        // .getMultiUserChat(grp_id);

        MultiUserChatManager multiUserChatManager = MultiUserChatManager
                .getInstanceFor(TempConnectionService.connection);

            /* = multiUserChatManager
                    .getMultiUserChat(grp_id);*/


        // Constant.printMsg("BM Group ID::" + grp_id + " Subject::" +
        // room_subject);

        try {
            multiUserChat = MultiUserChatManager.getInstanceFor(TempConnectionService.connection).getMultiUserChat(JidCreate.entityBareFrom(grp_id));

            Constant.printMsg("FFFFFFFFFFFFF1");

            multiUserChat.create(Resourcepart.from(room_subject));
            Constant.printMsg("FFFFFFFFFFFFF2");

            Form form = multiUserChat.getConfigurationForm();
            Form submitForm = form.createAnswerForm();
            submitForm.setAnswer("muc#roomconfig_roomname", room_subject);
            submitForm.setAnswer("muc#roomconfig_persistentroom", true);
            submitForm.setAnswer("muc#roomconfig_membersonly", true);
            submitForm.setAnswer("muc#roomconfig_allowinvites", true);
            submitForm.setAnswer("muc#roomconfig_moderatedroom", true);
            submitForm.setAnswer("muc#roomconfig_changesubject", true);
            submitForm.setAnswer("allow_query_users", true);
            submitForm.setAnswer("public_list", true);
            List<String> admin = new ArrayList<String>();
            admin.add(KachingMeApplication.getjid());
            submitForm.setAnswer("muc#roomconfig_roomdesc",
                    main_job.toString());
            multiUserChat.sendConfigurationForm(submitForm);
            multiUserChat.join(Resourcepart.from(KachingMeApplication.getjid()));
            Constant.printMsg("FFFFFFFFFFFFF7");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences sp_1 = Group_CreateIntentService.this.getSharedPreferences(
                            KachingMeApplication.getPereference_label(),
                            Activity.MODE_PRIVATE);
                    String Bookmarked_time = Utils.getBookmarkTime();
                    SharedPreferences.Editor editor = sp_1.edit();
                    editor.putString(Constant.LAST_REFRESH_TIME + "_" + grp_id,
                            Bookmarked_time);
                    editor.commit();

                    try {
                        BookmarkManager bm = BookmarkManager
                                .getBookmarkManager(TempConnectionService.connection);

                        bm.addBookmarkedConference(room_subject, JidCreate.entityBareFrom(grp_id), true,
                                Resourcepart.from(Utils.getBookmarkTime()), "");
                        Constant.printMsg("FFFFFFFFFFFFF9412");
                    } catch (Exception e) {

                    }
                }
            }).start();


            Constant.printMsg("FFFFFFFFFFFFF5");

        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            e.printStackTrace();

            Constant.printMsg("FFFFFFFFFFFFF3" + e.toString());
        }

        try {
            if (multiUserChat.isJoined()) {

                Constant.printMsg("FFFFFFFFFFFFF4" + multiUserChat.isJoined());
                Log.d("Room",
                        "Selected Member size::" + selected_users.size());

                group_partcipant_getset = new GroupParticipantGetSet();
                group_partcipant_getset.setAdmin(1);
                group_partcipant_getset.setGjid(grp_id);
                group_partcipant_getset.setJid(KachingMeApplication.getjid());
                dbAdapter.addGroupMembers(group_partcipant_getset);

                for (int j = 0; j < selected_users.size(); j++) {

                    if (j == 0) {
                        mem_list = selected_users.get(j).getJid();
                    } else {
                        mem_list = mem_list + ","
                                + selected_users.get(j).getJid();
                    }

                    group_partcipant_getset = new GroupParticipantGetSet();
                    group_partcipant_getset.setAdmin(0);
                    group_partcipant_getset.setGjid(grp_id);
                    group_partcipant_getset.setJid(selected_users.get(j)
                            .getJid());
                    dbAdapter.addGroupMembers(group_partcipant_getset);

                    multiUserChat.grantOwnership(JidCreate.from(selected_users.get(j)
                            .getJid().toString()));
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < selected_users.size(); i++) {

                            try {

                                multiUserChat.invite(
                                        JidCreate.entityBareFrom(selected_users.get(i).getJid()), "");
                                Log.d("MUC Create", "Invited Memebrs::"
                                        + selected_users.get(i).getJid());
                            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                                e.printStackTrace();
                                // TODO: handle exception
                            }

                        }
                    }
                }).start();


            }

        } catch (SmackException e) {
            e.printStackTrace();
            // TODO: handle exception
        } catch (XMPPException.XMPPErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

        msg.setData(room_subject);
        msg.setKey_from_me(0);
        msg.setKey_id("" + new Date().getTime());
        msg.setKey_remote_jid(grp_id);
        msg.setMedia_wa_type("9");
        msg.setNeeds_push(0);
        msg.setStatus(0);
        msg.setTimestamp(new Date().getTime());
        msg.setRemote_resource(KachingMeApplication.getUserID()
                + KachingMeApplication.getHost());
        dbAdapter.setInsertMessages(msg);



            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        SharedPreferences sp = Group_CreateIntentService.this.getSharedPreferences(
                                KachingMeApplication.getPereference_label(),
                                Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(grp_id,
                                mem_list + "," + KachingMeApplication.getUserID()
                                        + KachingMeApplication.getHost());
                        editor.putString(
                                grp_id + "_admin",
                                KachingMeApplication.getUserID()
                                        + KachingMeApplication.getHost());
                        editor.putString(grp_id + "_group_type",
                                json_tags.getString(Constant.GROUP_TYPE));
                        editor.putString(grp_id + "_group_question",
                                json_tags.getString(Constant.GROUP_TOPIC));

                        if (json_tags.getString(Constant.GROUP_TYPE).equals("4")
                                || json_tags.getString(Constant.GROUP_TYPE)
                                .equals("5")) {
                            editor.putString(grp_id + "_group_question_options",
                                    json_tags.getString(Constant.TOPIC_OPTION));
                        }
                        editor.commit();
                    } catch (JSONException e) {

                    }
                }
            }).start();


        // mBoundService.setMUC_Listeners(muc);
        multiUserChat
                .addMessageListener(TempConnectionService.muc_messageListener);
        multiUserChat
                .addSubjectUpdatedListener(new MUC_SubjectChangeListener(
                        this));

        boolean success = (new File(KachingMeApplication.PROFILE_PIC_DIR))
                .mkdirs();
        RequestParams request_params = new RequestParams();
        FileOutputStream fos;
        try {
            if (NewGroup_Info.img_byte == null) {
                NewGroup_Info.img_byte = new Utils()
                        .getGroupRandomeIcon(this);

            }

            fos = new FileOutputStream(new File(
                    KachingMeApplication.PROFILE_PIC_DIR + grp_id.split("@")[0]
                            + ".png"));
            fos.write(NewGroup_Info.img_byte);
            fos.close();
            request_params.setUseJsonStreamer(false);


            request_params.put("primaryNo", "" + KachingMeApplication.getUserID().split("@")[0]);
            request_params.put("fileType",  "5");
            request_params.put("fileName",  "");
            request_params.put("latitude",  "0");
            request_params.put("longitude", "0");
            request_params.put("reciverId", "1");
            request_params.put("groupId", grp_id.split("@")[0]);
            request_params.put("msgId", "");
            request_params.put("file",new File(
                    KachingMeApplication.PROFILE_PIC_DIR + grp_id.split("@")[0]
                            + ".png"));



           //icon_url = KachingMeConfig.UPLOAD_GROUP_ICON_FOLDER_PNG_PHP + grp_id.split("@")[0] + ".png";

				/*
                 * byte[] randome=new
				 * Utils().getGroupRandomeIcon(getActivity()); Bitmap myBitmap =
				 * BitmapFactory.decodeByteArray( randome, 0, randome.length);
				 * FileOutputStream stream = new FileOutputStream(new
				 * File(KachingMeApplication
				 * .PROFILE_PIC_DIR+grp_id.split("@")[0]+".png"));
				 *
				 * ByteArrayOutputStream outstream = new
				 * ByteArrayOutputStream();
				 * myBitmap.compress(Bitmap.CompressFormat.PNG, 85, outstream);
				 * byte[] byteArray = outstream.toByteArray();
				 *
				 * stream.write(byteArray); stream.close();
				 */

        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.post(this,
                KachingMeConfig.UPLOAD_MEDIA,
                null,
                request_params,"multipart/form-data",
                new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                    @Override
                    public void onFailure(int arg0, Header[] arg1,
                                          byte[] arg2, Throwable arg3) {
                        // TODO Auto-generated method stub
                        Constant.printMsg("GGrrrr ee" + new String(arg2) + " " + arg3.getMessage());

                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1,
                                          byte[] arg2) {
                        // TODO Auto-generated method stub

                        try {
                            Constant.printMsg("GGrrrr " + new String(arg2));

                            String content = new String(arg2);

                            JSONObject jsonObject_Image = new JSONObject(content);
//
                            final String  url = jsonObject_Image.getString("url");


                        } catch (Exception e) {

                        }
                    }

                });



        NewGroup_Info.img_byte = null;

        int msg_id = dbAdapter.getLastMsgid_chat_grp(grp_id);

        try {

            if (!dbAdapter.isjidExist(grp_id)) {

                ContactsGetSet contact = new ContactsGetSet();
                contact.setIs_niftychat_user(1);
                contact.setJid(grp_id);
                contact.setPhone_label(room_subject);
                contact.setDisplay_name(room_subject);
                contact.setPhoto_ts(new Utils()
                        .getGroupRandomeIcon(this));
                dbAdapter.insertContacts(contact);
                dbAdapter.setInsertChat_list(grp_id, msg_id,
                        json_tags.getString(Constant.TIMESTAMP));
            }
        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
        }

        Constant.mFromMemberList = true;
        Intent login_broadcast = new Intent("create_group");
        sendBroadcast(login_broadcast);

        Intent updateList_broadcast = new Intent("update_profile");
        sendBroadcast(updateList_broadcast);


        updateList_broadcast = new Intent("group_list");
        sendBroadcast(updateList_broadcast);

       /* SendWeb_Group.Add_Group_on_web(this, grp_id, room_subject,
                KachingMeApplication.getjid(),
                mem_list + "," + KachingMeApplication.getUserID()
                        + KachingMeApplication.getHost(),
                "" + msg.getTimestamp() / 1000, icon_url);*/


    }
}
