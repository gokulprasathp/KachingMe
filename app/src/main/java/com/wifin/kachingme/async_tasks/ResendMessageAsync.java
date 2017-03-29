/*
* @author Gokul
*
* @usage -  This class is used to send the offline messages
*
*
* */

package com.wifin.kachingme.async_tasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat.muc_chat.MUCTest;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.Utils;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.util.stringencoder.Base64;
import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.xevent.MessageEventManager;

import java.io.File;
import java.util.ArrayList;

public class ResendMessageAsync extends AsyncTask<String, String, String> {
    ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();

    DatabaseHelper dbAdapter;
    boolean is_send = false;
    Context context;

    public ResendMessageAsync(TempConnectionService service) {

        this.context = service;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        dbAdapter = KachingMeApplication.getDatabaseAdapter();
        list = dbAdapter.getUnsendMessages();
    }

    @Override
    protected String doInBackground(String... params) {

        for (int i = 0; i < list.size(); i++) {

            Constant.printMsg("EEEEEEEEEEE Resend Msg Inside chat : "
                    + list.get(i).getData());

            MessageGetSet msg = list.get(i);

            ContactsGetSet contact = dbAdapter.getContact(msg
                    .getKey_remote_jid());

            String duplicate = "num" + contact.getNumber();
            duplicate = duplicate.trim();

            if (duplicate.equalsIgnoreCase("num")) {
                contact.setNumber(null);
            }

            Constant.printMsg("EEEEEEEEEEE Resend Msg  cond: "
                    + contact.getNumber() + "--"
                    + contact.getIs_niftychat_user());

            Log.d("NiftyService",
                    "Message ID::" + msg.get_id() + " ::"
                            + msg.getKey_remote_jid() + " Media type::"
                            + msg.getMedia_wa_type());
            if (contact.getIs_niftychat_user() == 1
                    && (contact.getNumber() != null)) {

                Constant.printMsg("EEEEEEEEEEE ENTER SINGLE CHAT: "
                        + contact.getNumber() + "--"
                        + contact.getIs_niftychat_user());

                Message message = new Message();
                if (msg.getIs_sec_chat() == 0) {
                    JivePropertiesManager.addProperty(message, "is_owner",
                            msg.getIs_owner());
                    JivePropertiesManager.addProperty(message, "is_sec_chat",
                            msg.getIs_sec_chat());
                    JivePropertiesManager.addProperty(message,
                            "self_desc_time", msg.getSelf_des_time());
                }

                if (msg.getMedia_wa_type().equals("0")) {
                    // message.setBody(msg.getData());
                    message.setBody(Utils.EncryptMessage(msg.getData()));

                    message.setStanzaId("" + msg.getKey_id());
                    JivePropertiesManager.addProperty(message, "media_type", 0);

                } else if (msg.getMedia_wa_type().equals("1")) {
                    if (msg.getMedia_url() != null) {

                        message.setStanzaId("" + msg.getKey_id());
                        message.setBody("");
                        JivePropertiesManager.addProperty(message, "path",
                                msg.getMedia_url());
                        JivePropertiesManager.addProperty(message,
                                "media_type", msg.getMedia_wa_type());
                        JivePropertiesManager.addProperty(message, "mime_type",
                                msg.getMedia_mime_type());
                        JivePropertiesManager.addProperty(message,
                                "thumb_image",
                                Base64.encodeToString(msg.getRow_data()));
                        JivePropertiesManager.addProperty(message,
                                "media_duration", msg.getMedia_duration());
                        JivePropertiesManager.addProperty(message,
                                "media_size", msg.getMedia_size());

                    }
                } else if (msg.getMedia_wa_type().equals("2")) {

                    if (msg.getMedia_url() != null) {

                        message.setStanzaId("" + msg.getKey_id());
                        message.setBody("");
                        JivePropertiesManager.addProperty(message, "path",
                                msg.getMedia_url());
                        JivePropertiesManager.addProperty(message,
                                "media_type", msg.getMedia_wa_type());
                        JivePropertiesManager.addProperty(message, "mime_type",
                                msg.getMedia_mime_type());
                        JivePropertiesManager.addProperty(message,
                                "thumb_image",
                                Base64.encodeToString(msg.getRow_data()));
                        JivePropertiesManager.addProperty(message,
                                "media_duration", msg.getMedia_duration());
                        JivePropertiesManager.addProperty(message,
                                "media_size", msg.getMedia_size());

                    }

                } else if (msg.getMedia_wa_type().equals("3")) {
                    if (msg.getMedia_url() != null) {

                        message.setStanzaId("" + msg.getKey_id());
                        message.setBody("");
                        JivePropertiesManager.addProperty(message, "path",
                                msg.getMedia_url());
                        JivePropertiesManager.addProperty(message,
                                "media_type", msg.getMedia_wa_type());
                        JivePropertiesManager.addProperty(message, "mime_type",
                                msg.getMedia_mime_type());
                        JivePropertiesManager.addProperty(message,
                                "media_duration", msg.getMedia_duration());
                        JivePropertiesManager.addProperty(message,
                                "media_size", msg.getMedia_size());

                    }

                } else if (msg.getMedia_wa_type().equals("4")) {

                    message.setStanzaId("" + msg.getKey_id());
                    // message.setBody(msg.getData());
                    message.setBody(Utils.EncryptMessage(msg.getData()));
                    JivePropertiesManager.addProperty(message, "media_type",
                            msg.getMedia_wa_type());

                    JivePropertiesManager.addProperty(message, "lat",
                            msg.getLatitude());
                    JivePropertiesManager.addProperty(message, "lon",
                            msg.getLongitude());

                } else if (msg.getMedia_wa_type().equals("5")) {
                    message.setStanzaId("" + msg.getKey_id());
                    // message.setBody(msg.getData());
                    message.setBody(Utils.EncryptMessage(msg.getData()));
                    JivePropertiesManager.addProperty(message, "media_type",
                            msg.getMedia_wa_type());
                    if (msg.getRow_data() != null) {
                        JivePropertiesManager.addProperty(message,
                                "thumb_image",
                                Base64.encodeToString(msg.getRow_data()));
                    }
                    JivePropertiesManager.addProperty(message, "lat",
                            msg.getLatitude());
                    JivePropertiesManager.addProperty(message, "lon",
                            msg.getLongitude());

                } else if (msg.getMedia_wa_type().equals("6")) {
                    /* message.setProperty("msg_type", 4); */
                    message.setStanzaId("" + msg.getKey_id());
                    // message.setBody(msg.getData());
                    message.setBody(Utils.EncryptMessage(msg.getData()));
                    JivePropertiesManager.addProperty(message, "media_type", 5);
                    if (msg.getRow_data() != null) {
                        JivePropertiesManager.addProperty(message,
                                "thumb_image",
                                Base64.encodeToString(msg.getRow_data()));
                    }
                    JivePropertiesManager.addProperty(message, "media_name",
                            msg.getMedia_name());

                } else if (msg.getMedia_wa_type().equals("11")) {
                    if (msg.getMedia_url() != null) {

                        message.setStanzaId("" + msg.getKey_id());
                        message.setBody("");
                        JivePropertiesManager.addProperty(message, "path",
                                msg.getMedia_url());
                        JivePropertiesManager.addProperty(message,
                                "media_type", msg.getMedia_wa_type());
                        JivePropertiesManager.addProperty(message, "mime_type",
                                msg.getMedia_mime_type());
                        JivePropertiesManager.addProperty(message,
                                "thumb_image",
                                Base64.encodeToString(msg.getRow_data()));
                        JivePropertiesManager.addProperty(message,
                                "media_duration", msg.getMedia_duration());
                        JivePropertiesManager.addProperty(message,
                                "media_size", msg.getMedia_size());

                    }
                } else if (msg.getMedia_wa_type().equals("12")) {
                    if (msg.getMedia_url() != null) {

                        message.setStanzaId("" + msg.getKey_id());
                        message.setBody("");
                        JivePropertiesManager.addProperty(message, "path",
                                msg.getMedia_url());
                        JivePropertiesManager.addProperty(message,
                                "media_type", msg.getMedia_wa_type());
                        JivePropertiesManager.addProperty(message, "mime_type",
                                msg.getMedia_mime_type());
                        JivePropertiesManager.addProperty(message,
                                "thumb_image",
                                Base64.encodeToString(msg.getRow_data()));
                        JivePropertiesManager.addProperty(message,
                                "media_duration", msg.getMedia_duration());
                        JivePropertiesManager.addProperty(message,
                                "media_size", msg.getMedia_size());

                    }
                }

                org.jivesoftware.smack.chat.Chat chat = null;
                ChatManager chatManager;
                try {

                    if (TempConnectionService.connection != null) {
                        chatManager = ChatManager
                                .getInstanceFor(TempConnectionService.connection);

                        chat = chatManager
                                .createChat(
                                        msg.getKey_remote_jid(),
                                        TempConnectionService.mChatCreatedListener
                                                .getMessageListener());
                    }
                } catch (Exception e) {
                    // ACRA.getErrorReporter().handleException(e);
                    // TODO: handle exception
                    e.printStackTrace();
                }

                MessageEventManager.addNotificationsRequests(message, true,
                        true, true, true);

                try {

                    DeliveryReceiptRequest.addTo(message);
                    chat.sendMessage(message);

                    Thread.sleep(1000);
                } catch (Exception e) {
                    // ACRA.getErrorReporter().handleException(e);
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Log.e("NiftyService", "Unsend Message send........");

            } else {

                Log.d("NiftyService",
                        "RRRRRR Unsend group message::"
                                + msg.getKey_remote_jid() + msg.getData() + "0");
                Message muc_message = new Message(msg.getKey_remote_jid(),
                        Type.groupchat);
                MultiUserChatManager multiUserChatManager = MultiUserChatManager
                        .getInstanceFor(TempConnectionService.connection);

                MultiUserChat multiUserChat = multiUserChatManager
                        .getMultiUserChat(msg.getKey_remote_jid());
                // Group messages ...
                if (msg.getMedia_wa_type().equals("0")) {

                    muc_message.setStanzaId("" + msg.getKey_id());
                    // muc_message.setBody(msg.getData());
                    muc_message.setBody(Utils.EncryptMessage(msg.getData()));
                    JivePropertiesManager.addProperty(muc_message, "ID", 5);
                    JivePropertiesManager.addProperty(muc_message,
                            "media_type", "0");

                    is_send = true;
                } else if (msg.getMedia_wa_type().equals("1")) {
                    if (msg.getMedia_url() != null) {
                        muc_message.setStanzaId("" + msg.getKey_id());
                        muc_message.setBody("");
                        JivePropertiesManager.addProperty(muc_message, "path",
                                msg.getMedia_url());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_type", msg.getMedia_wa_type());
                        JivePropertiesManager.addProperty(muc_message,
                                "mime_type", msg.getMedia_mime_type());
                        JivePropertiesManager.addProperty(muc_message,
                                "thumb_image",
                                Base64.encodeToString(msg.getRow_data()));
                        JivePropertiesManager.addProperty(muc_message,
                                "media_duration", msg.getMedia_duration());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_size", msg.getMedia_size());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_wa_type", "1");
                        JivePropertiesManager.addProperty(muc_message, "ID", 5);
                        is_send = true;
                    }
                } else if (msg.getMedia_wa_type().equals("2")) {

                    if (msg.getMedia_url() != null) {
                        JivePropertiesManager.addProperty(muc_message,
                                "msg_type", 2);
                        muc_message.setStanzaId("" + msg.getKey_id());
                        muc_message.setBody("");
                        JivePropertiesManager.addProperty(muc_message, "path",
                                msg.getMedia_url());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_type", msg.getMedia_wa_type());
                        JivePropertiesManager.addProperty(muc_message,
                                "mime_type", msg.getMedia_mime_type());
                        JivePropertiesManager.addProperty(muc_message,
                                "thumb_image",
                                Base64.encodeToString(msg.getRow_data()));
                        JivePropertiesManager.addProperty(muc_message,
                                "media_duration", msg.getMedia_duration());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_size", msg.getMedia_size());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_wa_type", "2");
                        JivePropertiesManager.addProperty(muc_message, "ID", 5);
                        is_send = true;
                    }

                } else if (msg.getMedia_wa_type().equals("3")) {
                    if (msg.getMedia_url() != null) {
                        JivePropertiesManager.addProperty(muc_message,
                                "msg_type", 3);
                        muc_message.setStanzaId("" + msg.getKey_id());
                        muc_message.setBody("");
                        JivePropertiesManager.addProperty(muc_message, "path",
                                msg.getMedia_url());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_type", msg.getMedia_wa_type());
                        JivePropertiesManager.addProperty(muc_message,
                                "mime_type", msg.getMedia_mime_type());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_duration", msg.getMedia_duration());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_size", msg.getMedia_size());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_wa_type", "3");
                        JivePropertiesManager.addProperty(muc_message, "ID", 5);
                        is_send = true;
                    }

                } else if (msg.getMedia_wa_type().equals("4")) {
                    JivePropertiesManager.addProperty(muc_message, "msg_type",
                            4);
                    muc_message.setStanzaId("" + msg.getKey_id());
                    muc_message.setBody("");
                    JivePropertiesManager.addProperty(muc_message,
                            "media_type", msg.getMedia_wa_type());
                    JivePropertiesManager.addProperty(muc_message,
                            "thumb_image",
                            Base64.encodeToString(msg.getRow_data()));
                    JivePropertiesManager.addProperty(muc_message, "lat",
                            msg.getLatitude());
                    JivePropertiesManager.addProperty(muc_message, "lon",
                            msg.getLongitude());
                    JivePropertiesManager.addProperty(muc_message,
                            "media_wa_type", "4");
                    JivePropertiesManager.addProperty(muc_message, "ID", 5);
                    is_send = true;
                } else if (msg.getMedia_wa_type().equals("5")) {

                    JivePropertiesManager.addProperty(muc_message, "msg_type",
                            4);
                    muc_message.setStanzaId("" + msg.getKey_id());
                    // muc_message.setBody(msg.getData());
                    muc_message.setBody(Utils.EncryptMessage(msg.getData()));
                    JivePropertiesManager.addProperty(muc_message,
                            "media_type", 5);
                    JivePropertiesManager.addProperty(muc_message,
                            "media_wa_type", "5");
                    JivePropertiesManager.addProperty(muc_message, "ID", 5);

                    if (msg.getRow_data() != null) {
                        JivePropertiesManager.addProperty(muc_message,
                                "thumb_image", msg.getRow_data());
                    }

                    JivePropertiesManager.addProperty(muc_message,
                            "media_name", msg.getMedia_name());
                    is_send = true;

                } else if (msg.getMedia_wa_type().equals("6")) {
                    if (msg.getMedia_url() != null) {
                        JivePropertiesManager.addProperty(muc_message,
                                "msg_type", 3);
                        muc_message.setStanzaId("" + msg.getKey_id());
                        muc_message.setBody("");
                        JivePropertiesManager.addProperty(muc_message, "path",
                                msg.getMedia_url());
                        JivePropertiesManager.addProperty(muc_message, "ID",
                                "5");
                        JivePropertiesManager.addProperty(muc_message,
                                "media_wa_type", 6);
                        try {
                            JivePropertiesManager.addProperty(muc_message,
                                    "mime_type", msg.getMedia_mime_type());
                        } catch (Exception e) {
                            // ACRA.getErrorReporter().handleException(e);
                            // TODO: handle exception
                        }
                        JivePropertiesManager.addProperty(muc_message,
                                "media_size", msg.getMedia_size());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_name",
                                new File(msg.getMedia_name()).getName());

                        is_send = true;
                    }
                } else if (msg.getMedia_wa_type().equals("11")) {
                    if (msg.getMedia_url() != null) {
                        muc_message.setStanzaId("" + msg.getKey_id());
                        muc_message.setBody("");
                        JivePropertiesManager.addProperty(muc_message, "path",
                                msg.getMedia_url());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_type", msg.getMedia_wa_type());
                        JivePropertiesManager.addProperty(muc_message,
                                "mime_type", msg.getMedia_mime_type());
                        JivePropertiesManager.addProperty(muc_message,
                                "thumb_image",
                                Base64.encodeToString(msg.getRow_data()));
                        JivePropertiesManager.addProperty(muc_message,
                                "media_duration", msg.getMedia_duration());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_size", msg.getMedia_size());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_wa_type", "1");
                        JivePropertiesManager.addProperty(muc_message, "ID", 5);
                        is_send = true;
                    }
                } else if (msg.getMedia_wa_type().equals("12")) {
                    if (msg.getMedia_url() != null) {
                        muc_message.setStanzaId("" + msg.getKey_id());
                        muc_message.setBody("");
                        JivePropertiesManager.addProperty(muc_message, "path",
                                msg.getMedia_url());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_type", msg.getMedia_wa_type());
                        JivePropertiesManager.addProperty(muc_message,
                                "mime_type", msg.getMedia_mime_type());
                        JivePropertiesManager.addProperty(muc_message,
                                "thumb_image",
                                Base64.encodeToString(msg.getRow_data()));
                        JivePropertiesManager.addProperty(muc_message,
                                "media_duration", msg.getMedia_duration());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_size", msg.getMedia_size());
                        JivePropertiesManager.addProperty(muc_message,
                                "media_wa_type", "1");
                        JivePropertiesManager.addProperty(muc_message, "ID", 5);
                        is_send = true;
                    }
                }

                if (is_send) {

                    MessageEventManager.addNotificationsRequests(muc_message,
                            true, true, true, true);
                    DeliveryReceiptRequest.addTo(muc_message);

                    Constant.printMsg("RRRR " + muc_message.toString());

                    Constant.printMsg("RRRR " + multiUserChat.isJoined());

                    try {
                        multiUserChat.sendMessage(muc_message);

                        if (TempConnectionService.connection.isConnected() == true && TempConnectionService.connection.isAuthenticated() == true) {
                            dbAdapter.setUpdateMessage_status(
                                    msg.getKey_id(), 2);

                        } else {
                            dbAdapter.setUpdateMessage_status(
                                    msg.getKey_id(), 3);

                        }

                        if (Utils.isActivityIsFront(context, MUCTest.class.getCanonicalName().toString()) && msg.getKey_remote_jid().equalsIgnoreCase(MUCTest.jid)) {
                            Intent login_broadcast = new Intent("chat");
                            context.sendBroadcast(login_broadcast);
                        }
                    } catch (NotConnectedException e) {
                        // TODO Auto-generated catch block
                        Constant.printMsg("RRRR " + e.toString());
                    }

                    // } catch (NotConnectedException e) {
                    // // TODO Auto-generated catch block
                    // e.printStackTrace();
                    // }
                }

            }

            if (i + 1 < list.size()) {

                MessageGetSet mTempmsg = list.get(i + 1);
                if (!msg.getKey_remote_jid().equalsIgnoreCase(mTempmsg.getKey_remote_jid())) {
                    Intent login_broadcast = new Intent("lastseen_broadcast");
                    login_broadcast.putExtra("from", msg.getKey_remote_jid());
                    login_broadcast.putExtra("type", msg.getData());
                    context.getApplicationContext().sendBroadcast(login_broadcast);
                }

            } else if (i + 1 == list.size()) {
                Intent login_broadcast = new Intent("lastseen_broadcast");
                login_broadcast.putExtra("from", msg.getKey_remote_jid());
                login_broadcast.putExtra("type", msg.getData());
                context.getApplicationContext().sendBroadcast(login_broadcast);
            }


        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

}