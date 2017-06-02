package com.wifin.kachingme.listeners;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat.muc_chat.MUCTest;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.ADV_group_GetSet;
import com.wifin.kachingme.pojo.GroupParticipantGetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.ChatDictionary;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.Self_Destruct_Messages;
import com.wifin.kachingme.util.Utils;
import com.wifin.kachingme.util.KachingMeConfig;

import org.apache.http.Header;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.stringencoder.Base64;
import org.jivesoftware.smackx.bookmarks.BookmarkManager;
import org.jivesoftware.smackx.chatstates.packet.ChatStateExtension;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Part;
import org.jxmpp.jid.parts.Resourcepart;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MUC_MessageListener implements MessageListener {

    public static String TAG = MUC_MessageListener.class.getSimpleName();
    Context service;
    DatabaseHelper dbadapter;
    AbstractXMPPConnection connection;
    Intent chat_broadcast;
    Intent group_chat;
    Context context;
    SharedPreferences sp;
    Editor editor;
    int message_id = 0;
    String remote_jid = null;

    String remote_mem = null;

    public MUC_MessageListener(Context con, Context service) {
        Constant.printMsg("called muc_MsgListener:::::::::::");
        this.service = service;
        context = con;
        connection = TempConnectionService.connection;
        dbadapter = KachingMeApplication.getDatabaseAdapter();
        sp = KachingMeApplication.getsharedpreferences();
        editor = KachingMeApplication.getsharedpreferences_Editor();

    }

    @Override
    public void processMessage(Message message) {

        try {
            Constant.printMsg("HHHHHH called muc_MsgListener:::::::::::1111"
                    + message.toXML() + " Composing ::" + message.hasExtension(ChatStateExtension.NAMESPACE));

            if (message.getExtension("urn:xmpp:delay") != null && message.getBody() == null) {
                Constant.printMsg("Delay typing status ");
                return;
            }

            if(message.hasExtension(DeliveryReceipt.NAMESPACE))
            {
                if(message.getBody().equalsIgnoreCase("delivered"))
                {
                    deliveredNotification(message.getFrom().toString(), message.getStanzaId());
                    return;
                }
            }

            if (Message.Type.groupchat == message.getType()
                    && !dbadapter.isMessageExist(message.getStanzaId()) && message.hasExtension(ChatStateExtension.NAMESPACE) == false) {
                Log.d("HHHHHH called muc_MsgListener",
                        "Message::" + message.getBody() + " from::"
                                + message.getFrom() + " to::" + message.getTo()
                                + " Packate ID::" + message.hasExtension("composing"));
                // Constant.printMsg("Received message: !!! "+(message != null
                // ? message.getBody() : "NULL"));
                remote_jid = "";
                remote_mem = "";


                if (message.getFrom().toString().contains("/")) {

                    Constant.printMsg("muc Message "
                            + message.getFrom().toString().split("/")[0]);

                    remote_jid = message.getFrom().toString().split("/")[0];
                    remote_mem = message.getFrom().toString().split("/")[1];
                }

                if (!remote_mem.contains("@")) {
                    remote_mem = remote_mem + KachingMeApplication.getHost();
                }
                Calendar c = Calendar
                        .getInstance(/* TimeZone.getTimeZone("UTC") */);
                Log.d(TAG, "UTC::" + c.getTimeInMillis());

                String Bookmarked_time = Utils.getBookmarkTime();
                Editor editor1 = sp.edit();
                editor1.putString(Constant.LAST_REFRESH_TIME + "_"
                        + remote_jid, Bookmarked_time);
                editor1.commit();

                BookmarkManager bm1;
                try {
                    bm1 = BookmarkManager
                            .getBookmarkManager(TempConnectionService.connection);

                    bm1.addBookmarkedConference(remote_jid, JidCreate.entityBareFrom(remote_jid), true,
                           Resourcepart.from(Bookmarked_time), "");


                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    // ACRA.getErrorReporter().handleException(e1);
                    e1.printStackTrace();
                }

                if (JivePropertiesManager.getProperties(message).get("ID") != null) {
                    message_id = Integer.parseInt(""
                            + JivePropertiesManager.getProperties(message).get(
                            "ID"));
                }

                //Sending Delivery notification....
                MUC_ListenerMethods.sendReceiptDelivered(remote_jid, message.getStanzaId());

                Constant.printMsg("IIIIIIIIIIIIID" + message.getFrom());

                if (message_id == 2) {


                    try {
                        editor.putString(remote_jid, message.getBody());
                        editor.commit();
                        Log.d("Removed Member", JivePropertiesManager
                                .getProperties(message).get("Removed_member")
                                .toString());


                        MessageGetSet msg = new MessageGetSet();
                        msg.setData(JivePropertiesManager.getProperties(message)
                                .get("Removed_member").toString());
                        msg.setKey_from_me(0);
                        msg.setKey_id(message.getStanzaId());
                        msg.setKey_remote_jid(remote_jid);
                        msg.setNeeds_push(0);
                        msg.setStatus(0);
                        msg.setTimestamp(new Date().getTime());
                        msg.setRemote_resource(remote_mem);
                        msg.setMedia_wa_type("8");
                        long l = 0;
                        if (remote_mem.equals(dbadapter.getLogin().get(0).getUserName()+ KachingMeApplication.getHost())) {
                            if (!JivePropertiesManager.getProperties(message)
                                    .get("Removed_member").toString()
                                    .equals(dbadapter.getLogin().get(0).getUserName()+ KachingMeApplication.getHost())) {
                                l = dbadapter.setInsertMessages(msg);
                            }
                        } else {
                            l = dbadapter.setInsertMessages(msg);
                        }
                        String group_admin = (dbadapter.getGroupAdmin(remote_jid))
                                .getJid();

                        dbadapter.deleteGroupMembers(
                                remote_jid,
                                JivePropertiesManager.getProperties(message)
                                        .get("Removed_member").toString());
//                        int unseen_msg = dbadapter.getunseen_msg(remote_jid);
                        int msg_id = dbadapter.getLastMsgid(remote_jid);

                        Log.d(TAG, "Last Message ID::" + msg_id
                                + " Inserted row id::" + l);

//                        dbadapter.setUpdateContact_unseen_msg(remote_jid,
//                                unseen_msg);
                        if (dbadapter.isExistinChatList(remote_jid)) {
                            long l1 = dbadapter.setUpdateChat_lits(remote_jid,
                                    msg_id);
                            Log.d(TAG, "Last Message ID Updated::" + l1);

                        } else {
                            dbadapter.setInsertChat_list(remote_jid, msg_id);
                        }

                        if (JivePropertiesManager
                                .getProperties(message)
                                .get("Removed_member")
                                .toString()
                                .equals(dbadapter.getLogin().get(0).getUserName()
                                        + KachingMeApplication.getHost())) {
                            editor.remove(remote_jid);
                            editor.remove(remote_jid + "_admin");
                            editor.commit();
                            try {
                                BookmarkManager bm = BookmarkManager
                                        .getBookmarkManager(connection);
                                bm.removeBookmarkedConference(JidCreate.entityBareFrom(remote_jid));
                            } catch (Exception e) {
                                // ACRA.getErrorReporter().handleException(e);
                                e.printStackTrace();
                                // TODO: handle exception
                            }
                        }
                        Log.d(TAG,
                                "Group Admin::"
                                        + group_admin
                                        + " Removed Member::"
                                        + JivePropertiesManager
                                        .getProperties(message)
                                        .get("Removed_member").toString());
                        Log.d(TAG,
                                "IS ADMIN::"
                                        + group_admin.equals(JivePropertiesManager
                                        .getProperties(message)
                                        .get("Removed_member").toString()));
                        Log.d(TAG,
                                "IS ADMIN 2::"
                                        + (group_admin == JivePropertiesManager
                                        .getProperties(message)
                                        .get("Removed_member").toString()));

                        if (group_admin.equals(JivePropertiesManager
                                .getProperties(message).get("Removed_member")
                                .toString())) {
                            Log.d(TAG, "ADMIN CHANGED");

                            try {
                                new MUC_ListenerMethods(context)
                                        .changg_group_admin(remote_jid);




                            } catch (Exception e) {

                            }
                        }

                        if (!dbadapter.isjidExist(remote_mem)) {
                            try {
                                new StatusListenerMethods(service)
                                        .Add_New_Contact(remote_mem);
                            } catch (Exception e) {

                            }

                        }
                    } catch (Exception e) {

                    }

                    // For calling Broadcast in all screens...
                    callBroadcasts(context, remote_jid);

                } else if (message_id == 1) {

                    editor.putString(remote_jid, message.getBody());
                    editor.commit();
                    Log.d("Added Member",
                            JivePropertiesManager.getProperties(message)
                                    .get("Added_member").toString());



                    if (!JivePropertiesManager
                            .getProperties(message)
                            .get("Added_member")
                            .toString()
                            .equals(dbadapter.getLogin().get(0).getUserName()+ KachingMeApplication.getHost())) {
                        try {
                            Log.e(TAG,
                                    "Added Member::"
                                            + JivePropertiesManager
                                            .getProperties(message)
                                            .get("Added_member").toString()
                                            + " Niftychat user::"
                                            + dbadapter.getLogin().get(0).getUserName()
                                            + KachingMeApplication.getHost());
                            MessageGetSet msg = new MessageGetSet();
                            msg.setData(JivePropertiesManager
                                    .getProperties(message).get("Added_member")
                                    .toString());
                            msg.setKey_from_me(0);
                            msg.setKey_id(message.getStanzaId());
                            msg.setKey_remote_jid(remote_jid);
                            msg.setNeeds_push(0);
                            msg.setStatus(0);
                            msg.setTimestamp(new Date().getTime());
                            msg.setRemote_resource(remote_mem);
                            msg.setMedia_wa_type("7");
                            dbadapter.setInsertMessages(msg);

                            GroupParticipantGetSet group_getset = new GroupParticipantGetSet();
                            group_getset.setAdmin(0);
                            group_getset.setGjid(remote_jid);
                            group_getset.setJid(JivePropertiesManager
                                    .getProperties(message).get("Added_member")
                                     .toString());

                            if (!remote_mem.equals(dbadapter.getLogin().get(0).getUserName()+ KachingMeApplication.getHost()))
                                dbadapter.addGroupMembers(group_getset);

                            int unseen_msg = dbadapter.getunseen_msg(remote_jid);
                            int msg_id = dbadapter.getLastMsgid(remote_jid);
                            try {
                                if (remote_jid != null) {
                                    if (Utils.isActivityIsFront(context, MUCTest.class.getCanonicalName().toString()) && remote_jid.equalsIgnoreCase(MUCTest.jid)) {

                                    } else {
                                        dbadapter.setUpdateContact_unseen_msg(remote_jid,
                                                unseen_msg);
                                    }
                                }
                            } catch (Exception e) {

                            }
                            if (dbadapter.isExistinChatList(remote_jid)) {
                                dbadapter.setUpdateChat_lits(remote_jid, msg_id);
                            } else {
                                dbadapter.setInsertChat_list(remote_jid, msg_id);
                            }


                            Constant.printMsg("MUC Broadcast ..chat 44..");
                        } catch (Exception e) {

                        }

                        // For calling Broadcast in all screens...
                        callBroadcasts(context,remote_jid);
                    }
                    if (!dbadapter.isjidExist(remote_mem)) {

                        try {
                            new StatusListenerMethods(service)
                                    .Add_New_Contact(remote_mem);
                        } catch (Exception e) {

                        }

                    }

                } else if (message_id == 3) {

                    editor.putString(remote_jid, message.getBody());
                    editor.putString(
                            remote_jid + "_admin",
                            JivePropertiesManager.getProperties(message)
                                    .get("New_admin").toString());
                    editor.commit();

                    Intent login_broadcast = new Intent("update_list");
                    context.sendBroadcast(login_broadcast);

                    MessageGetSet msg = new MessageGetSet();
                    msg.setData(JivePropertiesManager.getProperties(message)
                            .get("Left_Member").toString());
                    msg.setKey_from_me(0);
                    msg.setKey_id(message.getStanzaId());
                    msg.setKey_remote_jid(remote_jid);
                    // msg.setMedia_size(2);
                    msg.setNeeds_push(0);
                    msg.setStatus(0);
                    msg.setTimestamp(new Date().getTime());
                    msg.setRemote_resource(remote_mem);
                    msg.setMedia_wa_type("8");
                    dbadapter.setInsertMessages(msg);

                    dbadapter.deleteGroupMembers(
                            remote_jid,
                            JivePropertiesManager.getProperties(message)
                                    .get("Left_Member").toString());
                    dbadapter.updateGroupMembers(
                            remote_jid,
                            JivePropertiesManager.getProperties(message)
                                    .get("New_admin").toString(), 1);
                    int msg_id = dbadapter.getLastMsgid(remote_jid);
                    if (dbadapter.isExistinChatList(remote_jid)) {
                        dbadapter.setUpdateChat_lits(remote_jid, msg_id);
                    } else {
                        dbadapter.setInsertChat_list(remote_jid, msg_id);
                    }

                    Constant.printMsg("MUC Broadcast ..chat 33..");



                    if (!dbadapter.isjidExist(remote_mem)) {
                        try {
                            new StatusListenerMethods(service)
                                    .Add_New_Contact(remote_mem);
                        } catch (Exception e) {

                        }
                    }

                    callBroadcasts(context,remote_jid);


                } else if (message_id == 4) {

                    Constant.printMsg("ADMIN ADDED ");

                    try {

                        if(JivePropertiesManager.getProperties(message)
                                .get("Add_Admin").toString()!=null) {
                            String room_admin = JivePropertiesManager.getProperties(message)
                                    .get("Add_Admin").toString();

                            long l = dbadapter.updateGroupMembers(remote_jid, room_admin, 1);
                            android.util.Log.d(TAG, "Room Admin updated::" + l);


                            callBroadcasts(context, remote_jid);
                        }
                    } catch (Exception e) {

                    }

                } else if (message_id == 6) {
                    ADV_group_GetSet advgroup = new ADV_group_GetSet();
                    advgroup.setJid(remote_jid);
                    advgroup.setMember_id(remote_mem);
                    advgroup.setAnswer(message.getBody());
                    advgroup.setGroup_type(Integer
                            .parseInt(JivePropertiesManager
                                    .getProperties(message).get("group_type")
                                    .toString()));

                    if (dbadapter.getADV_Group_is_exist(remote_jid, remote_mem) > 0) {
                        dbadapter.setUpdateAdv_group_and(advgroup);
                    } else {
                        dbadapter.insertADV_group(advgroup);
                    }

                    Constant.printMsg("MUC Broadcast ..chat 22..");
                    chat_broadcast = new Intent("chat");
                    chat_broadcast.putExtra("jid", "" + remote_jid);
                    context.sendBroadcast(chat_broadcast);
                } else if (message_id == 7) {
                    Log.d(TAG, "Topic changed recieved!!!!" + message.getBody());
                    editor.putString(remote_jid + "_group_question",
                            message.getBody());
                    editor.commit();
                } else if (message_id == 8) {

                    String url = message.getBody();

                    MessageGetSet msg = new MessageGetSet();
                    msg.setData("");
                    msg.setKey_from_me(0);
                    msg.setKey_id(message.getStanzaId());
                    msg.setKey_remote_jid(remote_jid);
                    msg.setNeeds_push(0);
                    msg.setStatus(0);
                    msg.setTimestamp(new Date().getTime());
                    msg.setRemote_resource(remote_mem);
                    msg.setMedia_wa_type("11");
                    dbadapter.setInsertMessages(msg);

                    int msg_id = dbadapter.getLastMsgid(remote_jid);
                    if (dbadapter.isExistinChatList(remote_jid)) {
                        dbadapter.setUpdateChat_lits(remote_jid, msg_id);
                    } else {
                        dbadapter.setInsertChat_list(remote_jid, msg_id);
                    }

                    boolean success = (new File(
                            KachingMeApplication.PROFILE_PIC_DIR)).mkdirs();
                    final String jid = remote_jid;
                    final String jid1 = remote_jid;
                    AsyncHttpClient client = new AsyncHttpClient();
                    String[] allowedContentTypes = new String[]{"image/png",
                            "image/jpeg"};
                    client.get(url,
                            new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                                @Override
                                public void onFinish() {
                                    // TODO Auto-generated method stub
                                    chat_broadcast = new Intent(
                                            Constant.BROADCAST_UPDATE_GROUP_ICON);
                                    chat_broadcast.putExtra("jid", "" + jid);
                                    context.sendBroadcast(chat_broadcast);
                                    super.onFinish();

                                }

                                @Override
                                public void onFailure(int statusCode,
                                                      Header[] headers, byte[] binaryData,
                                                      Throwable error) {

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
                                        myBitmap.compress(
                                                Bitmap.CompressFormat.PNG, 85,
                                                outstream);
                                        byte[] byteArray = outstream
                                                .toByteArray();

                                        stream.write(byteArray);
                                        stream.close();

                                    } catch (Exception e) {
                                        // ACRA.getErrorReporter().handleException(e);
                                        // TODO: handle exception
                                    }
                                }

								/*
                                 * @Override public void onSuccess(byte[]
								 * fileData) {
								 * 
								 * try { Bitmap myBitmap =
								 * BitmapFactory.decodeByteArray( fileData, 0,
								 * fileData.length); FileOutputStream stream =
								 * new FileOutputStream(new
								 * File(KachingMeApplication
								 * .PROFILE_PIC_DIR+jid1.split("@")[0]+".png"));
								 * 
								 * ByteArrayOutputStream outstream = new
								 * ByteArrayOutputStream();
								 * myBitmap.compress(Bitmap.CompressFormat.PNG,
								 * 85, outstream); byte[] byteArray =
								 * outstream.toByteArray();
								 * 
								 * stream.write(byteArray); stream.close();
								 * 
								 * 
								 * } catch (Exception e) { // TODO: handle
								 * exception }
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
                                        myBitmap.compress(
                                                Bitmap.CompressFormat.PNG, 85,
                                                outstream);
                                        byte[] byteArray = outstream
                                                .toByteArray();

                                        stream.write(byteArray);
                                        stream.close();

                                    } catch (Exception e) {
                                        // ACRA.getErrorReporter().handleException(e);
                                        // TODO: handle exception
                                    }

                                }
                            });

                    if (!dbadapter.isjidExist(remote_mem)) {

                        try {
                            new StatusListenerMethods(service)
                                    .Add_New_Contact(remote_mem);
                        } catch (Exception e) {

                        }

                    }

                    Constant.printMsg("MUC Broadcast ..chat 11..");
                    chat_broadcast = new Intent("chat");
                    chat_broadcast.putExtra("jid", "" + remote_jid);
                    context.sendBroadcast(chat_broadcast);

                }

				/*
                 * else if(message_id==4) { MessageGetSet msg=new
				 * MessageGetSet(); msg.setData(""); msg.setKey_from_me(0);
				 * msg.setKey_id(message.getPacketID());
				 * msg.setKey_remote_jid(remote_jid);
				 * msg.setMedia_wa_type("10"); msg.setNeeds_push(0);
				 * msg.setStatus(0); msg.setTimestamp(new Date().getTime());
				 * msg.setRemote_resource(remote_mem);
				 * dbadapter.setInsertMessages(msg);
				 * dbadapter.setUpdateSubject(remote_jid,message.getBody());
				 * 
				 * int msg_id=dbadapter.getLastMsgid(remote_jid);
				 * if(dbadapter.isExistinChatList(remote_jid)) {
				 * dbadapter.setUpdateChat_lits(remote_jid, msg_id); } else {
				 * dbadapter.setInsertChat_list(remote_jid, msg_id); }
				 * 
				 * Intent login_broadcast=new Intent("group_list");
				 * login_broadcast.putExtra("jid",""+remote_jid);
				 * context.getApplicationContext
				 * ().sendBroadcast(login_broadcast);
				 * 
				 * chat_broadcast=new Intent("chat");
				 * chat_broadcast.putExtra("jid",""+remote_jid);
				 * context.getApplicationContext
				 * ().sendBroadcast(chat_broadcast); }
				 */
                else {
                    if (message.getStanzaId() != null
                            && message.getBody() != null
                            && !remote_mem.equals(dbadapter.getLogin().get(0).getUserName()
                            + KachingMeApplication.getHost())
                            && !dbadapter.isMessageExist(message.getStanzaId())) {
                        // Constant.printMsg("true");

                        long l = -1;

                        try {

                            MessageGetSet msg = new MessageGetSet();
                            try {
                                // msg.setData(message.getBody().toString());
                                msg.setData(Utils.DecryptMessage(message
                                        .getBody().toString()));
                            } catch (Exception e) {
                                // ACRA.getErrorReporter().handleException(e);
                                // TODO: handle exception
                            }
                            msg.setKey_from_me(1);
                            msg.setKey_id(message.getStanzaId());
                            msg.setKey_remote_jid(remote_jid);
                            msg.setNeeds_push(1);
                            msg.setReceived_timestamp(new Date().getTime());
                            msg.setStatus(1);
                            msg.setTimestamp(new Date().getTime());
                            // msg.setMedia_size(5);
                            msg.setRemote_resource(remote_mem);

                            if (JivePropertiesManager.getProperties(message)
                                    .get("media_type") != null) {
                                msg.setMedia_wa_type(JivePropertiesManager
                                        .getProperties(message)
                                        .get("media_type").toString());
                            }

                            if (JivePropertiesManager.getProperties(message)
                                    .get("mime_type") != null) {
                                msg.setMedia_mime_type(JivePropertiesManager
                                        .getProperties(message)
                                        .get("mime_type").toString());
                            }

                            if (JivePropertiesManager.getProperties(message)
                                    .get("path") != null) {
                                msg.setMedia_url(JivePropertiesManager
                                        .getProperties(message).get("path")
                                        .toString());
                            }

                            if (JivePropertiesManager.getProperties(message)
                                    .get("media_duration") != null) {
                                msg.setMedia_duration(Integer
                                        .parseInt(JivePropertiesManager
                                                .getProperties(message)
                                                .get("media_duration")
                                                .toString()));
                            }

                            if (JivePropertiesManager.getProperties(message)
                                    .get("media_size") != null) {
                                msg.setMedia_size(Integer
                                        .parseInt(JivePropertiesManager
                                                .getProperties(message)
                                                .get("media_size").toString()) * 1024);
                            }

                            if (JivePropertiesManager.getProperties(message)
                                    .get("thumb_image") != null) {
                                // msg.setRow_data((byte[])JivePropertiesManager.getProperties(message).get("thumb_image"));
                                msg.setRow_data(Base64
                                        .decode(JivePropertiesManager
                                                .getProperties(message)
                                                .get("thumb_image").toString()));
                            }

                            if (JivePropertiesManager.getProperties(message)
                                    .get("lat") != null) {
                                msg.setLatitude(Double
                                        .parseDouble(JivePropertiesManager
                                                .getProperties(message)
                                                .get("lat").toString()));
                            }
                            if (JivePropertiesManager.getProperties(message)
                                    .get("lon") != null) {
                                msg.setLongitude(Double
                                        .parseDouble(JivePropertiesManager
                                                .getProperties(message)
                                                .get("lon").toString()));
                            }

                            if (JivePropertiesManager.getProperties(message)
                                    .get("media_name") != null) {
                                msg.setMedia_name(JivePropertiesManager
                                        .getProperties(message)
                                        .get("media_name").toString());
                                msg.setOrigin(1);
                            }

                            if (msg.getMedia_wa_type().equals("5")) {
                               /* VCardParser parser = new VCardParser();
                                VDataBuilder builder = new VDataBuilder();
                                try {
                                    boolean parsed = parser.parse(
                                            msg.getData(), "UTF-8", builder);

                                    // get all parsed contacts
                                    List<VNode> pimContacts = builder.vNodeList;

                                    // do something for all the contacts
                                    for (VNode contact : pimContacts) {
                                        ArrayList<PropertyNode> props = contact.propList;

                                        // contact name - FN property
                                        String name = null;
                                        for (PropertyNode prop : props) {

                                            Log.d("vcard", prop.propName + "::"
                                                    + prop.propValue);
                                            if ("FN".equals(prop.propName)) {
                                                name = prop.propValue;
                                                msg.setMedia_name(name);
                                                // we have the name now
                                                // break;
                                            }

                                        }

                                    }
                                } catch (VCardException e) {
                                    // ACRA.getErrorReporter().handleException(e);
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // ACRA.getErrorReporter().handleException(e);
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }*/
                            }

                            try {
                                l = dbadapter.setInsertMessages(msg);
                                checkNynmInsetion(msg.getData());

                                Gson gson = new Gson();
                                Constant.printMsg("Receive msg MUC " + gson.toJson(msg));

                            } catch (Exception e) {

                            }


                            boolean isMUCFront = Utils.isActivityIsFront(context, MUCTest.class.getCanonicalName().toString());

                            if (isMUCFront && remote_jid.equalsIgnoreCase(MUCTest.jid)) {
                                MUCTest.msg_list.add(msg);
                                MUCTest.mPositionKey_muc.add(msg.getKey_id());


                                Constant.printMsg("HHHHHH called muc_MsgListener:::::::::::11112222"
                                        );

                                Intent login_broadcast = new Intent("update_left");
                                login_broadcast.putExtra("position", "" + (MUCTest.msg_list.size() - 1));
                                context.getApplicationContext().sendBroadcast(login_broadcast);

                            }else
                            {
                                Intent login_broadcast = new Intent("lastseen_broadcast");
                                login_broadcast.putExtra("from", remote_jid);
                                login_broadcast.putExtra("type", msg.getData());
                                Context context = (Context) service;
                                context.getApplicationContext().sendBroadcast(login_broadcast);
                            }


                           /* try{


                                if (dbadapter.isExistinChatList_chat(remote_jid,
                                        msg.getIs_sec_chat())) {
                                    Constant.printMsg("called::>>> secret8");

                                    dbadapter.setUpdateChat_lits_chat(remote_jid,  msg.get_id(),
                                            msg.getIs_sec_chat());
                                } else {
                                    Constant.printMsg("called::>>> secret7");

                                    dbadapter.setInsertChat_list_chat(remote_jid,  msg.get_id(),
                                            msg.getIs_sec_chat());
                                }

                            }catch (Exception e)
                            {

                            }*/

                            Constant.printMsg("Chat::" + isMUCFront + " Chat List::" + Utils.isActivityIsFront(context, SliderTesting.class.getCanonicalName().toString()));
                            if (!isMUCFront
                                    && !Utils.isActivityIsFront(context, SliderTesting.class.getCanonicalName().toString())) {
                                if (msg.getMedia_wa_type().equals("0")) {
                                    new MUC_ListenerMethods(service)
                                            .issueNotification_Room(message
                                                    .getStanzaId());
                                } else if (msg.getMedia_wa_type().equals("1")) {
                                    new MUC_ListenerMethods(service)
                                            .issueNotification_Room(message
                                                    .getStanzaId());
                                } else if (msg.getMedia_wa_type().equals("2")) {
                                    new MUC_ListenerMethods(service)
                                            .issueNotification_Room(message
                                                    .getStanzaId());
                                } else if (msg.getMedia_wa_type().equals("3")) {
                                    new MUC_ListenerMethods(service)
                                            .issueNotification_Room(message
                                                    .getStanzaId());
                                } else if (msg.getMedia_wa_type().equals("4")) {
                                    new MUC_ListenerMethods(service)
                                            .issueNotification_Room(message
                                                    .getStanzaId());
                                } else if (msg.getMedia_wa_type().equals("5")) {
                                    new MUC_ListenerMethods(service)
                                            .issueNotification_Room(message
                                                    .getStanzaId());
                                } else if (msg.getMedia_wa_type().equals("6")) {
                                    new MUC_ListenerMethods(service)
                                            .issueNotification_Room(message
                                                    .getStanzaId());
                                } else if (msg.getMedia_wa_type().equals("12")) {
                                    new MUC_ListenerMethods(service)
                                            .issueNotification_Room(message
                                                    .getStanzaId());
                                }

                            } else if (remote_jid != null) {
                                if (isMUCFront && (!remote_jid.equalsIgnoreCase(MUCTest.jid))) {
                                    new MUC_ListenerMethods(service)
                                            .issueNotification_Room(message
                                                    .getStanzaId());
                                }
                            }

                        } catch (Exception e) {
                            // ACRA.getErrorReporter().handleException(e);
                            e.printStackTrace();
                            // TODO: handle exception
                        }

                        int unseen_msg = dbadapter.getunseen_msg(remote_jid);
                        int msg_id = dbadapter.getLastMsgid(remote_jid);
                        dbadapter.setUpdateContact_unseen_msg(remote_jid,
                                unseen_msg);
                        if (dbadapter.isExistinChatList(remote_jid)) {
                            dbadapter.setUpdateChat_lits(remote_jid, msg_id);
                        } else {
                            dbadapter.setInsertChat_list(remote_jid, msg_id);
                        }

                        Log.d("Message", "Message inserted::" + l + "--"
                                + remote_jid);


//                        Intent login_broadcast = new Intent("chat");
//                        login_broadcast.putExtra("jid", "" + remote_jid);
//                        context.sendBroadcast(login_broadcast);

                        group_chat = new Intent("group_list");
                        context.getApplicationContext().sendBroadcast(
                                group_chat);

                        if (!dbadapter.isjidExist(remote_mem)) {
                            new StatusListenerMethods(service)
                                    .Add_New_Contact(remote_mem);
                        }

                    }
                }

            } else if (message.getBody() != null) {// Typing and recording....


                if (message.hasExtension(DelayInformation.NAMESPACE))
                    return;



//                if (!message.getBody().equalsIgnoreCase(Constant.TYPING_STATUS_GROUP) && !message.getBody().equalsIgnoreCase(Constant.TYPING_STATUS_RECORDING))
//                    return;

                String jid = message.getFrom().toString().split("/")[1];
                String jid_grp = message.getFrom().toString().split("/")[0];

                Constant.printMsg("HHHHHHH composing muc " + jid_grp + jid + "  " + dbadapter.getLogin().get(0).getUserName() + KachingMeApplication.getHost());

                if(!jid.contains("@"))
                {
                    jid = jid + KachingMeApplication.getHost();
                }

                if (jid.equalsIgnoreCase(dbadapter.getLogin().get(0).getUserName()+ KachingMeApplication.getHost()))
                    return;


                String typingString = message.getBody().split(",")[0];
                String typingJid = message.getBody().split(",")[1];

                // Checking slider testing in front
                if (Utils.isActivityIsFront(context, SliderTesting.class.getCanonicalName().toString()) && jid_grp != null) {

                    if (typingString.equalsIgnoreCase(Constant.TYPING_STATUS_GROUP)) {


                        Intent login_broadcast = new Intent("lastseen_broadcast");
                        login_broadcast.putExtra("from", "" + jid_grp);
                        login_broadcast.putExtra("type", "typing");
                        context.sendBroadcast(login_broadcast);

                    } else if (typingString.equalsIgnoreCase(Constant.TYPING_STATUS_RECORDING)) {

                        Intent login_broadcast = new Intent("lastseen_broadcast");
                        login_broadcast.putExtra("from", "" + jid_grp);
                        login_broadcast.putExtra("type", "recording" );
                        context.sendBroadcast(login_broadcast);

                    }
                } else if (Utils.isActivityIsFront(context, MUCTest.class.getCanonicalName().toString()) && typingJid != null) {//Checking muc group match
                    if (jid_grp.equalsIgnoreCase(MUCTest.jid)) {


                        if (typingString.equalsIgnoreCase(Constant.TYPING_STATUS_GROUP)) {


                            Intent login_broadcast = new Intent("start_typing");
                            login_broadcast.putExtra("jid", "" + typingJid);
                            login_broadcast.putExtra("jid_grp", "" + jid_grp);
                            login_broadcast.putExtra("type", "" + Constant.TYPING_STATUS_GROUP);
                            context.sendBroadcast(login_broadcast);

                        } else if (typingString.equalsIgnoreCase(Constant.TYPING_STATUS_RECORDING)) {

                            Intent login_broadcast = new Intent("start_typing");
                            login_broadcast.putExtra("jid", "" + typingJid);
                            login_broadcast.putExtra("jid_grp", "" + jid_grp);
                            login_broadcast.putExtra("type", "" + Constant.TYPING_STATUS_RECORDING);
                            context.sendBroadcast(login_broadcast);

                        }
                    }

                }


            }
        } catch (Exception e) {
            // ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
        }
    }

	/*
     * @Override public void processPacket(Stanza arg0) throws
	 * NotConnectedException { // TODO Auto-generated method stub
	 * 
	 * }
	 */

    public static void callBroadcasts(Context context, String remote_jid)
    {
        //MucInfo
         Intent  chat_broadcast = new Intent("update_list");
         context.sendBroadcast(chat_broadcast);

        //Both MUCTest and UserChatList same
        Intent chat_broadcast1 = new Intent("chat");
        chat_broadcast1.putExtra("jid", "" + remote_jid);
        context.sendBroadcast(chat_broadcast1);

        //GroupchatList
        Intent  group_chat = new Intent("group_list");
        context.getApplicationContext().sendBroadcast(
                group_chat);
    }

    /**
     * Deliverd return message to update tick
     */
    public void deliveredNotification(String from, String packetID) {
        // TODO Auto-generated method stub

        Constant.printMsg("HHHHHH  msg event listener Deliverd");
        try {

            String fromUser = from.toString().split("/")[0];
            // Constant.printMsg("Display Notification Received from::"+fromUser);
            MessageGetSet messagegetset = dbadapter
                    .getMessages_by_key_id(packetID);
            if (messagegetset.getIs_sec_chat() == 0
                    && !messagegetset.getMedia_wa_type().equals("7")
                    && messagegetset.getSelf_des_time() != 0) {
                new Self_Destruct_Messages(context).setDestruct(""
                                + messagegetset.get_id(),
                        messagegetset.getSelf_des_time(),
                        messagegetset.getKey_remote_jid());
            }

            dbadapter.setUpdateMessage_status(fromUser, packetID, 0);


            try {
                ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
                ActivityManager.RunningTaskInfo ar = RunningTask.get(0);

                if (ar.topActivity.getClassName().toString().equalsIgnoreCase("com.wifin.kachingme.chat.single_chat.ChatTest")) {

                } else if (ar.topActivity.getClassName().toString().equalsIgnoreCase("com.wifin.kachingme.chat.muc_chat.MUCTest")) {

                    for (int i = 0; i < MUCTest.msg_list.size(); i++) {

                        if (MUCTest.msg_list.get(i).getKey_id().equalsIgnoreCase(packetID)) {

                            MessageGetSet msg = new MessageGetSet();
                            msg.set_id(MUCTest.msg_list.get(i).get_id());
                            msg.setData(MUCTest.msg_list.get(i).getData());
                            msg.setKey_from_me(MUCTest.msg_list.get(i).getKey_from_me());
                            msg.setKey_id(MUCTest.msg_list.get(i).getKey_id());
                            msg.setKey_remote_jid(MUCTest.msg_list.get(i).getKey_remote_jid());
                            msg.setLatitude(MUCTest.msg_list.get(i).getLatitude());
                            msg.setLongitude(MUCTest.msg_list.get(i).getLongitude());
                            msg.setMedia_duration(MUCTest.msg_list.get(i).getMedia_duration());
                            msg.setMedia_hash(MUCTest.msg_list.get(i).getMedia_hash());
                            msg.setMedia_mime_type(MUCTest.msg_list.get(i).getMedia_mime_type());
                            msg.setMedia_name(MUCTest.msg_list.get(i).getMedia_name());
                            msg.setMedia_size(MUCTest.msg_list.get(i).getMedia_size());
                            msg.setMedia_url(MUCTest.msg_list.get(i).getMedia_url());
                            msg.setMedia_wa_type(MUCTest.msg_list.get(i).getMedia_wa_type());
                            msg.setNeeds_push(MUCTest.msg_list.get(i).getNeeds_push());
                            msg.setOrigin(MUCTest.msg_list.get(i).getOrigin());
                            msg.setReceipt_device_timestamp(MUCTest.msg_list.get(i).getReceipt_device_timestamp());
                            msg.setReceipt_server_timestamp(MUCTest.msg_list.get(i).getReceipt_server_timestamp());
                            msg.setReceived_timestamp(MUCTest.msg_list.get(i).getReceived_timestamp());
                            msg.setRemote_resource(MUCTest.msg_list.get(i).getRemote_resource());
                            msg.setRow_data(MUCTest.msg_list.get(i).getRow_data());
                            msg.setSend_timestamp(MUCTest.msg_list.get(i).getSend_timestamp());
                            msg.setStatus(0);
                            msg.setThumb_image(MUCTest.msg_list.get(i).getThumb_image());
                            msg.setTimestamp(MUCTest.msg_list.get(i).getTimestamp());
                            MUCTest.msg_list.set(i, msg);


                            if (Utils.isActivityIsFront(context, MUCTest.class.getCanonicalName())) {
                                Constant.printMsg("MUCCCCCCCC");
                                Intent login_broadcast = new Intent("update_tick");
                                login_broadcast.putExtra("position", "" + i);
                                login_broadcast.putExtra("status", "delivered");
                                context.getApplicationContext().sendBroadcast(login_broadcast);
                            }

                            else if (Utils.isActivityIsFront(context, SliderTesting.class.getCanonicalName())) {
                                Constant.printMsg("MUCCCCCCCC slider");
                                Intent login_broadcast = new Intent("lastseen_broadcast");
                                login_broadcast.putExtra("from",msg.getKey_remote_jid());
                                login_broadcast.putExtra("type", msg.getData());
                                context.getApplicationContext().sendBroadcast(login_broadcast);
                            }

                        }

                    }


                }
            } catch (Exception e) {

            }


        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    public void checkNynmInsetion(String text)
    {



        if (text.length() > 2) {

            char q = text.charAt(0);
            char p = text.charAt(1);

            if (q == '<') {

                if (p == '-') {
                    getNynmStrings(text.substring(2));
                }
            }
        }

    }

    void getNynmStrings(String value) {

        try {
            String[] arr = value.split(" ");
            for (String ss : arr) {
                if (!ss.isEmpty()) {
                    if (ss.contains("<n>") && ss.contains("</n>")
                            && ss.contains("<m>") && ss.contains("</m>")) {
                        String[] subSplits = ss.split("</n>");

                        if (subSplits.length > 0) {
                            String subSplitsMeaning = ss.substring(subSplits[0]
                                    .length() + 4);


                            Constant.printMsg("Dilip MUC Test :" + subSplits[0].substring(3,
                                    subSplits[0].length()) + " " + subSplitsMeaning.substring(3,
                                    subSplitsMeaning.length() - 4));

                            String nynmTitle = subSplits[0].substring(3,
                                    subSplits[0].length());

                            String nynmMeaning = subSplitsMeaning.substring(3,
                                    subSplitsMeaning.length() - 4);

                            if(nynmTitle!=null && nynmMeaning!=null)
                            {

                                Constant.printMsg("Dilip MUC Test 00 "
                                        +  nynmTitle);
                                nynmTitle = subLastString(nynmTitle);
                                nynmMeaning = subLastString(nynmMeaning);

                                Constant.printMsg("Dilip MUC Test 1:" + nynmTitle +"  " + nynmMeaning + " " + CheckNynmTitleCount(nynmTitle) + " " + CheckNynmMessageCount(nynmTitle, nynmMeaning));

                                if(CheckNynmTitleCount(nynmTitle))
                                {
                                    if(CheckNynmMessageCount(nynmTitle, nynmMeaning))
                                    {
                                        ContentValues cv = new ContentValues();
                                        cv.put("name", nynmTitle);
                                        cv.put("meaning", nynmMeaning);
                                        insertDB(cv);

                                        Constant.bux = Long.valueOf(sp.getLong(
                                                "buxvalue", 0));
                                        Long buxval = Long.valueOf(Constant.bux.longValue()
                                                + ((long) Constant.nympoints));
                                        Constant.bux = buxval;
                                        Editor e = sp.edit();
                                        e.putLong("buxvalue", buxval.longValue());
                                        e.commit();


                                        ChatDictionary.mDictionaryList.add(nynmTitle);
                                        ChatDictionary.mDictionaryMeaningList.add(nynmMeaning);
                                    }
                                }

                            }


                        }
                    } else {
                        Constant.printMsg("Dilip MUC Test else "
                                + ss);

                    }
                }
            }

        } catch (Exception e) {
            Constant.printMsg("Dilip MUC Test else "
                    + e.toString());
        }

    }


    public void insertDB(ContentValues v) {
        Dbhelper db = new Dbhelper(service);
        try {

            Constant.printMsg("No of inserted rows in shop details :::::::::"
                    + ((int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_NYM, null, v)));
        } catch (Exception e) {
            Constant.printMsg("Sql exception in new shop details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }

    }

    public boolean CheckNynmTitleCount(String txt) {


        Dbhelper db = new Dbhelper(service);
        Cursor c = null;
        boolean isCheck = false;
        try {
            c = db.open()
                    .getDatabaseObj()
                    .query(Dbhelper.TABLE_NYM,  new String[] {"id"
                            }, "name like ?",  new String[] {txt}, null, null,
                            null);
            if(c!=null)
            {

                Constant.printMsg("Nynm Check count : "  + c.getCount());
                if(c.getCount()<3)
                {
                    isCheck =  true;
                }

            }

        }catch (Exception e)
        {

        }

        return isCheck;
      /*  sd
        for (int i = 0; i < Constant.addedNyms.size(); i++) {
            if (((NymsPojo) Constant.addedNyms.get(i)).getText().toString()
                    .trim().equals(txt)) {
            }
        }
        return true;*/
    }


    public  boolean CheckNynmMessageCount(String title, String txt) {


        Dbhelper db = new Dbhelper(service);
        Cursor c = null;
        boolean isCheck = true;
        try {
            c = db.open()
                    .getDatabaseObj()
                    .query(Dbhelper.TABLE_NYM,  new String[] {"id"
                            }, "meaning like ? and name like ?",  new String[] {txt, title}, null, null,
                            null);
            if(c!=null)
            {

                Constant.printMsg("Nynm Check Msg count : "  + c.getCount());
                if(c.getCount()>0)
                {
                    isCheck =  false;
                }

            }

        }catch (Exception e)
        {

        }

        return isCheck;

    }

    public String subLastString(String str) {
        if (str != null ) {

            if(str.length() > 0)
                str = str.substring(0, str.length()-1);
        }
        return str;
    }


}
