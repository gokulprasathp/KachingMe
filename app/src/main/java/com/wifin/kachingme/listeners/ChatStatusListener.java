package com.wifin.kachingme.listeners;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.preference.PreferenceManager;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.bradcast_recivers.GlobalBroadcast;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.services.DestructService;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.ChatDictionary;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.NetworkSharedPreference;
import com.wifin.kachingme.util.Utils;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateListener;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jxmpp.jid.impl.JidCreate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class ChatStatusListener implements ChatStateListener {

    public static String chat_msg_id;
    Context service;
    String uid;
    DatabaseHelper dbadapter;
    boolean isNewContact = false;
    SharedPreferences sp, pref;
    Editor editor;

    boolean isChatTestFront;
    int status_msg = 3;
    int IS_OWNER = 0;
    NetworkSharedPreference mNewtSharPref;

    SharedPreferences prefResponse;
    String preferenceResp = "auto_response", responseMsg = "";
    boolean response_state = false, dest_status = false;

    public ChatStatusListener(Context service, String uid) {

        this.service = service;
        this.uid = uid;
        dbadapter = KachingMeApplication.getDatabaseAdapter();
        sp = service.getSharedPreferences(
                KachingMeApplication.getPereference_label(), MODE_PRIVATE);
        editor = sp.edit();
        Context context = (Context) service;
        pref = PreferenceManager.getDefaultSharedPreferences(context
                .getApplicationContext());
        mNewtSharPref = new NetworkSharedPreference(service);
        prefResponse = context.getSharedPreferences(preferenceResp, MODE_PRIVATE);
    }

    @Override
    public void processMessage(Chat arg0, Message message) {

        try {


                chat_msg_id = null;

                boolean isChatTestFront = Utils.isActivityIsFront(service, ChatTest.class.getCanonicalName().toString());

                Constant.printMsg("HHHHHHHHHHHHHHHHHHHHHH" + message.toXML()
                        + "              " + message.getType() + "                 "
                        + Message.Type.chat);

                if (message.getStanzaId() != null) {
                    if (( message.getStanzaId().equalsIgnoreCase(Constant.TYPING_STRING) || message.getStanzaId().equalsIgnoreCase(Constant.STATUS_RECORDING)) && (message.hasExtension(DelayInformation.NAMESPACE))) {
                        Constant.printMsg("Typing delay....");
                        return;
                    }
                }

                if (!KachingMeApplication.getBlocked_user().contains(arg0.getParticipant().toString().split("/")[0])) {

                    try {


                        if ((message.getStanzaId().equalsIgnoreCase(Constant.TYPING_STRING) || message.getStanzaId().equalsIgnoreCase(Constant.STATUS_RECORDING)) && (!message.hasExtension(DelayInformation.NAMESPACE))) {
                            chatTypingStatus(arg0.getParticipant().toString().split("/")[0], message.getBody());
                            return;
                        }


                        ArrayList<String> blocked_users = new ArrayList<String>();
                        String bu[] = null;
                        try {
                            if (sp.contains(Constant.BLOCKED_USERS)) {
                                bu = sp.getString(Constant.BLOCKED_USERS, null).toString()
                                        .split(",");
                                Constant.printMsg("called::>>> secret");


                                if (bu != null) {
                                    for (String string : bu) {
                                        blocked_users.add(string);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            // ACRA.getErrorReporter().handleException(e);
                            // TODO: handle exception
                        }
                        Constant.printMsg("called::>>> secret");

                        if (message.getType() == Message.Type.chat && message.getBody() != null) {
                            Constant.printMsg("notification called::>>> secret" + arg0.getParticipant().toString().split("/")[0] + "--" + Constant.CURRENT_OPEN_JID + "--" + isChatTestFront + "--" + isChatTestFront);


                            try {

                                if ((isChatTestFront == true)
                                        && (arg0.getParticipant().toString().split("/")[0]
                                        .equalsIgnoreCase(Constant.CURRENT_OPEN_JID))) {

                                    Constant.printMsg("TTTTTTTT");


                                    StatusListenerMethods.sendReceipt(arg0.getParticipant()
                                                    .toString().split("/")[0], message.getStanzaId(),
                                            Constant.STATUS_DISPLAYED);


                                } else {
                                    Constant.printMsg("TTTTTTTT111");
                                    StatusListenerMethods.sendReceipt(arg0.getParticipant()
                                                    .toString().split("/")[0], message.getStanzaId(),
                                            Constant.STATUS_DELIVERED);


                                }

                            } catch (Exception e) {
                                // ACRA.getErrorReporter().handleException(e);
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                            Constant.printMsg("called::>>> secret");

                            try {
                                String fromUser = arg0.getParticipant().toString().split("/")[0];
                                if (message.getBody() != null
                                        && !message.getStanzaId().equals("vcardedit")
                                        && !blocked_users.contains(fromUser)
                                        && !dbadapter.isMessageExist(message.getStanzaId())) {
                                    Constant.printMsg("called::>>> secret");

                                    MessageGetSet msg = new MessageGetSet();
                                    try {
                                        if (message.getBody().toString().length() > 0)
                                            // msg.setData(message.getBody().toString());
                                            msg.setData(Utils.DecryptMessage(message.getBody()
                                                    .toString()));
                                    } catch (Exception e) {
                                        // ACRA.getErrorReporter().handleException(e);
                                        // TODO: handle exception
                                    }
                                    msg.setKey_from_me(1);
                                    msg.setKey_id(message.getStanzaId());
                                    msg.setKey_remote_jid(fromUser);
                                    msg.setNeeds_push(1);
                                    msg.setReceived_timestamp(System.currentTimeMillis());
                                    msg.setStatus(1);
                                    msg.setTimestamp(new Date().getTime());
                                    try {
                                        // Constant.printMsg("Media type::"+JivePropertiesManager.getProperties(message).get("media_type"));
                                    } catch (Exception e) {
                                        // ACRA.getErrorReporter().handleException(e);
                                        // TODO: handle exception
                                    }

                                    if (JivePropertiesManager.getProperties(message).get(
                                            "media_type") != null) {
                                        msg.setMedia_wa_type(JivePropertiesManager
                                                .getProperties(message).get("media_type")
                                                .toString());
                                    }
                    /*
                     * if(JivePropertiesManager.getProperties(message).get("is_owner"
                     * )!=null) {
                     * msg.setIs_owner(Integer.parseInt(JivePropertiesManager
                     * .getProperties(message).get("is_owner").toString()));
                     *
                     *
                     * }
                     */

                    /*
                     * MessageEventManager
                     * messageeventmanager=service.getMessageEventManager();
                     * messageeventmanager.sendDeliveredNotification(fromUser,
                     * message.getStanzaId());
                     */

                                    if (JivePropertiesManager.getProperties(message).get(
                                            "mime_type") != null) {
                                        msg.setMedia_mime_type(JivePropertiesManager
                                                .getProperties(message).get("mime_type").toString());
                                    }

                                    if (JivePropertiesManager.getProperties(message).get("path") != null) {
                                        msg.setMedia_url(JivePropertiesManager
                                                .getProperties(message).get("path").toString());

                                        Constant.printMsg("test path receiver::::::::::::: " + JivePropertiesManager
                                                .getProperties(message).get("path").toString());


                                    }

                                    if (JivePropertiesManager.getProperties(message).get(
                                            "media_duration") != null) {
                                        msg.setMedia_duration(Integer
                                                .parseInt(JivePropertiesManager
                                                        .getProperties(message)
                                                        .get("media_duration").toString()));
                                    }

                                    if (JivePropertiesManager.getProperties(message).get(
                                            "media_size") != null) {
                                        msg.setMedia_size(Integer.parseInt(JivePropertiesManager
                                                .getProperties(message).get("media_size")
                                                .toString()) * 1024);
                                    }

                                    if (JivePropertiesManager.getProperties(message).get(
                                            "thumb_image") != null) {
                                        msg.setRow_data(org.jivesoftware.smack.util.stringencoder.Base64
                                                .decode(JivePropertiesManager
                                                        .getProperties(message).get("thumb_image")
                                                        .toString()));
                                    }

                                    if (JivePropertiesManager.getProperties(message).get("lat") != null) {
                                        msg.setLatitude(Double.parseDouble(JivePropertiesManager
                                                .getProperties(message).get("lat").toString()));
                                    }
                                    if (JivePropertiesManager.getProperties(message).get("lon") != null) {
                                        msg.setLongitude(Double.parseDouble(JivePropertiesManager
                                                .getProperties(message).get("lon").toString()));
                                    }

                                    if (JivePropertiesManager.getProperties(message).get(
                                            "media_name") != null) {
                                        msg.setMedia_name(JivePropertiesManager
                                                .getProperties(message).get("media_name")
                                                .toString());
                                        msg.setOrigin(1);
                                    }

                                    if (JivePropertiesManager.getProperties(message).get(
                                            "is_sec_chat") != null) {
                                        Constant.printMsg("called::>>> secret");
                                        msg.setIs_sec_chat(Integer.parseInt(JivePropertiesManager
                                                .getProperties(message).get("is_sec_chat")
                                                .toString()));

                                        if (sp.contains(fromUser + "_sec_admin")) {
                                            msg.setIs_owner(sp.getInt(fromUser + "_sec_admin", 1));
                                        }
                                    } else {
                                        Constant.printMsg("called::>>> secret1");

                                        msg.setIs_sec_chat(1);
                                    }

                                    if (JivePropertiesManager.getProperties(message).get(
                                            "self_desc_time") != null) {
                                        Constant.printMsg("called::>>> secret2");

                                        msg.setSelf_des_time(Integer.parseInt(JivePropertiesManager
                                                .getProperties(message).get("self_desc_time")
                                                .toString()));

                                        Log.d("Chat view",
                                                "Self Destruct time::"
                                                        + JivePropertiesManager
                                                        .getProperties(message)
                                                        .get("self_desc_time").toString());

                        /*
                         * SharedPreferences
                         * sp=service.getSharedPreferences(KachingMeApplication
                         * .getPereference_label(), Activity.MODE_PRIVATE);
                         */
                        /* SharedPreferences.Editor editor = sp.edit(); */
                                        editor.putInt(
                                                fromUser + "_self_desc_time",
                                                Integer.parseInt(JivePropertiesManager
                                                        .getProperties(message)
                                                        .get("self_desc_time").toString()));
                                        editor.commit();

                                    } else {
                                        Constant.printMsg("called::>>> secret3");

                                        msg.setSelf_des_time(0);
                                    }

                    /*
                     * if(JivePropertiesManager.getProperties(message).get(
                     * "self_desc_time_index")!=null) {
                     *
                     *
                     * editor.putInt(fromUser+"_self_desc_time",
                     * Integer.parseInt(JivePropertiesManager
                     * .getProperties(message).
                     * get("self_desc_time_index").toString())); editor.commit();
                     *
                     * }
                     */
                                    if (msg.getMedia_wa_type().equals("7")) {
                        /*
                         * SharedPreferences
                         * sp=service.getSharedPreferences(KachingMeApplication
                         * .getPereference_label(), Activity.MODE_PRIVATE);
                         * SharedPreferences.Editor editor = sp.edit();
                         */
                                        Constant.printMsg("called::>>> secret4" + fromUser);

                                        editor.putInt(fromUser + "_sec_admin", 1);
                                        editor.commit();
                                    }

                                    if (msg.getMedia_wa_type().equals("5")) {
                                  /*  VCardParser parser = new VCardParser();
                                    VDataBuilder builder = new VDataBuilder();
                                    try {
                                        boolean parsed = parser.parse(msg.getData(), "UTF-8",
                                                builder);

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

                                    long l = -1;
                                    if (msg.getMedia_wa_type().equals("8")) {
                                        SharedPreferences sp = service.getSharedPreferences(
                                                KachingMeApplication.getPereference_label(),
                                                MODE_PRIVATE);
                                        Editor editor = sp.edit();
                                        editor.remove(fromUser + "_self_desc_time");
                                        editor.commit();
                                        Constant.printMsg("called::>>> secret5");

                                        dbadapter.setDelete_Sec_chat(fromUser);
                                    }
                                    else {
                                        l = dbadapter.setInsertMessages(msg);

                                        String mTempParse = message.getBody().toString();

                                        boolean autoResp = false;

                                        Constant.printMsg("called::>>> secret5" + mTempParse);

                                        if (mTempParse.length() > 3)
                                        {
                                            char s = mTempParse.charAt(0);
                                            char s1 = mTempParse.charAt(1);
                                            char s2 = mTempParse.charAt(2);

                                            if (s == '<' && s1 == 'o' && s2 == '>')
                                            {
                                                autoResp = true;
                                            }
                                        }

                                        //Nynm insertion..
                                        checkNynmInsetion(message.getBody().toString());

                                        if (isChatTestFront == true && ChatTest.jid.equalsIgnoreCase(fromUser)) {

                                            if (mTempParse.length() > 3) {

                                                char s = mTempParse.charAt(0);
                                                char s1 = mTempParse.charAt(1);
                                                char s2 = mTempParse.charAt(2);

                                                if (s == '<' && s1 == 's' && s2 == '>') {

                                                    if (!GlobalBroadcast.isServiceRunning(DestructService.class.getCanonicalName(), service))
                                                        service.startService(new Intent(service, DestructService.class));

                                                    dest_status = true;
                                                    String self_destruct = mTempParse.substring(3)
                                                            .toString();
                                                    String[] parts = self_destruct.split("-");
                                                    String part1 = parts[0];
                                                    String part2 = parts[1];

                                                    ChatTest.dest_list.add(part1.toString());
                                                    ChatTest.dest_list_msgids.add(l);
                                                    ChatTest.dest_list_bombids.add(R.drawable.black_bomb);
                                                    ChatTest.dest_list_anim.add(0);

                                                } else {
                                                    ChatTest.dest_list.add(0);
                                                    ChatTest.dest_list_msgids.add(l);
                                                    ChatTest.dest_list_bombids.add(R.drawable.black_bomb);
                                                    ChatTest.dest_list_anim.add(0);
                                                }
                                            } else {
                                                ChatTest.dest_list.add(0);
                                                ChatTest.dest_list_msgids.add(l);
                                                ChatTest.dest_list_bombids.add(R.drawable.black_bomb);
                                                ChatTest.dest_list_anim.add(0);
                                            }
                                            Constant.printMsg("Auto Response Broad 1000  " + ChatTest.msg_list.size() );
                                            msg.setPostion(ChatTest.msg_list.size());
                                            ChatTest.msg_list.add(msg);
                                            ChatTest.mPositionKey.add(msg.getKey_id());

                                            Constant.printMsg("Auto Response Broad 111 " + ChatTest.msg_list.size() );

                                            Context context = (Context) service;
                                            Intent login_broadcast = new Intent("update_left");
                                            login_broadcast.putExtra("position", "" + (ChatTest.msg_list.size() - 1));
                                            context.getApplicationContext().sendBroadcast(login_broadcast);

                                            login_broadcast = new Intent("invisible");
                                            context.getApplicationContext().sendBroadcast(login_broadcast);
                                        } else {
                                            Intent login_broadcast = new Intent("lastseen_broadcast");
                                            login_broadcast.putExtra("from", arg0.getParticipant().toString().split("/")[0]);
                                            login_broadcast.putExtra("type", msg.getData());
                                            Context context = (Context) service;
                                            context.getApplicationContext().sendBroadcast(login_broadcast);
                                        }
                                        response_state = prefResponse.getBoolean("status_auto", false);
                                        responseMsg = prefResponse.getString("status_msg", "");

                                        Constant.printMsg("Preference Status :: " + response_state + " Resp :: " + prefResponse.getBoolean("status_auto", false) + " Msg :: " + responseMsg);

                                        if (response_state && !autoResp)
                                        {
                                            autoResp("<o>" + "-" + responseMsg, fromUser);
                                        }

                                        autoResp = false;
                                    }
                                    int unseen_msg = dbadapter.getunseen_msg(fromUser,
                                            msg.getIs_sec_chat());
                                    int msg_id = dbadapter.getLastMsgid_chat(fromUser,
                                            msg.getIs_sec_chat());
                                    Constant.printMsg("called::>>> secret6");

                                    if (isChatTestFront == true && ChatTest.jid.equalsIgnoreCase(fromUser))
                                    {

                                    }else {
                                        dbadapter.setUpdateContact_unseen_msg_chat(fromUser,
                                                unseen_msg, msg.getIs_sec_chat());
                                    }

                                    Log.d("Chat",
                                            "Is Sec Chat::" + msg.getIs_sec_chat()
                                                    + "Last Message id::" + msg_id
                                                    + " msg status::" + msg.getStatus()
                                                    + " unseen message::" + unseen_msg);
                                    if (dbadapter.isExistinChatList_chat(fromUser,
                                            msg.getIs_sec_chat())) {
                                        Constant.printMsg("called::>>> secret8");

                                        dbadapter.setUpdateChat_lits_chat(fromUser, msg_id,
                                                msg.getIs_sec_chat());
                                    } else {
                                        Constant.printMsg("called::>>> secret7");

                                        dbadapter.setInsertChat_list_chat(fromUser, msg_id,
                                                msg.getIs_sec_chat());
                                    }


                                    if (!dbadapter.isjidExist(fromUser)) {
                                        Log.d("Message", "Contact does not exist::" + fromUser);
                                        new StatusListenerMethods(service)
                                                .Add_New_Contact(fromUser);
                                        chat_msg_id = msg.getKey_id();
                                        isNewContact = true;
                                    }else{
                                        Log.d("Message", "Contact exist::" + fromUser);
//                                        new StatusListenerMethods(service)
//                                                .Update_Exisiting_Contact(fromUser);
//                                        chat_msg_id = msg.getKey_id();
                                    }

                                    if (!isChatTestFront
                                            && !Utils.isActivityIsFront(service, SliderTesting.class.getCanonicalName().toString())) {
                                        Constant.printMsg("notification ::::>>>>>>>." + fromUser
                                                + "   " + msg.getKey_id());
                                        new StatusListenerMethods(service).issueNotification(
                                                fromUser, msg.getKey_id());

                                    } else if ((isChatTestFront) && (!fromUser.equalsIgnoreCase(ChatTest.jid))) {
                                        new StatusListenerMethods(service).issueNotification(
                                                fromUser, msg.getKey_id());
                                    }

                                } else {
                                    if (message.getStanzaId().equals("vcardedit")) {
                                        new StatusListenerMethods(service)
                                                .ChangeVcard(fromUser);

                                        Constant.printMsg("notification ::::>>>>>>>1." + fromUser
                                                + "   " + message.getStanzaId());
                                    }

                                }
                            } catch (Exception e) {

                            }


                        }
                    } catch (Exception e) {

                    }
                }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    @Override
//    public void stateChanged(Chat arg0, ChatState arg1) {
//        // Constant.printMsg("Chat State Recived!!!!!!!"+arg1.name());
//
//        if (ChatState.composing.equals(arg1)) {
//            Log.d("Chat State", arg0.getParticipant() + " is typing..");
//        } else if (ChatState.gone.equals(arg1)) {
//            Log.d("Chat State", arg0.getParticipant()
//                    + " has left the conversation.");
//        } else {
//            Log.d("Chat State", arg0.getParticipant() + ": " + arg1.name());
//        }
//
//    }

    public void update_received_count(int code) {
        Constant.printMsg("received msg update_received_count added is::"
                + code);
        Editor e = pref.edit();
        e.putInt("received_msg_count", code);
        Constant.printMsg("received_msg_count in chat state listener :::"
                + code);
        e.commit();
    }


    public void chatTypingStatus(String jid, String packetID) {

        Intent login_broadcast = new Intent("lastseen_broadcast");
        login_broadcast.putExtra("from", jid);

        if (packetID != null) {
            String status;
            String typing_msg = null;

            try {

                if (packetID.toString().contains("@")) {
                    status = packetID.split("@")[0];
                    typing_msg = packetID.split("@")[1];
                } else {
                    status = packetID;
                }

                Constant.printMsg("FFFFFFF split " + status + " " + typing_msg);

                if (status.equalsIgnoreCase(Constant.TYPING_STRING)) {

                    login_broadcast.putExtra("type_msg", typing_msg);
                    login_broadcast.putExtra("type", Constant.TYPING_STRING);

                } else if (status.equalsIgnoreCase(Constant.STATUS_RECORDING)) {
                    login_broadcast.putExtra("type", Constant.STATUS_RECORDING);
                } else {
                    login_broadcast.putExtra("type", "jid_status_from_presence");
                }

            } catch (Exception e) {
                Constant.printMsg("FFFFFFF split eee" + e.toString());
            }
        } else {
            login_broadcast.putExtra("type", "jid_status_from_presence");
        }
        service.sendBroadcast(login_broadcast);
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

    public void autoResp(final String stringmsg, final String jid)
    {
        final int sec = 1;
        final String msg_id_resp = "" + new Date().getTime();
        Constant.printMsg("Donate buxs msg_id start   " + msg_id_resp);
        final MessageGetSet msggetset = new MessageGetSet();

        Constant.printMsg("Auto Response Broad 1 :: " + ChatTest.k + dest_status);

        isChatTestFront = Utils.isActivityIsFront(service, ChatTest.class.getCanonicalName().toString());

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    if (dest_status)
                    {
                        dest_status = false;
                        Constant.printMsg("Auto Response Broad Threaddd :: " );
                        Thread.sleep(500);
                        Constant.printMsg("Auto Response Broad Threaddd222 :: ");
                    }
                    Message msg = new Message(JidCreate.from(jid), Message.Type.chat);
                    msg.setStanzaId(msg_id_resp);
                    msg.setBody(Utils.EncryptMessage(stringmsg));
                    msg.addExtension(new DeliveryReceiptRequest());

                    JivePropertiesManager.addProperty(msg, "media_type", 0);

                    if (sec == 0)
                    {
                        JivePropertiesManager.addProperty(msg, "is_sec_chat", sec);
                        JivePropertiesManager.addProperty(msg, "self_desc_time", sp.getInt(jid + "_self_desc_time", 0));
                    }

                    /*if(TempConnectionService.connection != null)
                        Constant.printMsg("FFFFFFFFFFFFFFF" + TempConnectionService.connection + "        " + TempConnectionService.connection.isConnected());
                    else
                    {
                        Constant.printMsg("FFFFFFFFFFFFFFF connection null ");
                        service.startService(new Intent(service, TempConnectionService.class));
                    }
*/
                    DeliveryReceiptRequest.addTo(msg);

                    if (Connectivity.isOnline(service) == true && Connectivity.isTempConnection())
                    {
                        status_msg = 2;
                        TempConnectionService.connection.sendStanza(msg);
                        Constant.mselected_self_destruct_time = 0;
                        Constant.printMsg("dataaa ::::::: " + status_msg + "     " + msg.toString());
                    }

                    Constant.printMsg("dataaa ::::::: " + stringmsg + "  " + "" + msg_id_resp + "    " + jid);

                    try
                    {
                        if (Connectivity.isOnline(service) && Connectivity.isTempConnection())
                        {
                            status_msg = 2;
                        }
                        msggetset.setData(stringmsg);
                        msggetset.setKey_from_me(0);
                        msggetset.setKey_id("" + msg_id_resp);
                        msggetset.setKey_remote_jid(jid);
                        msggetset.setNeeds_push(1);
                        msggetset.setSend_timestamp(new Date().getTime());
                        msggetset.setStatus(status_msg);
                        msggetset.setTimestamp(new Date().getTime());
                        msggetset.setMedia_wa_type("0");
                        msggetset.setIs_sec_chat(sec);
                        msggetset.setSelf_des_time(sp.getInt(jid + "_self_desc_time", 0));
                        msggetset.setIs_owner(IS_OWNER);

                        long id_insert = dbadapter.setInsertMessages(msggetset);
                        msggetset.set_id((int)id_insert);

                        Constant.printMsg("Auto Response Broad 4 :: " + isChatTestFront + " JID :: " + Constant.CURRENT_OPEN_JID + " JID V" + jid + "      " + ChatTest.k + " Size " + ChatTest.msg_list.size());

                        if(isChatTestFront && Constant.CURRENT_OPEN_JID.equalsIgnoreCase(jid))
                        {
                            ChatTest.msg_list.add(msggetset);
                            ChatTest.mPositionKey.add(msggetset.getKey_id());
                            ChatTest.dest_list_msgids.add(id_insert);
                            ChatTest.dest_list_bombids.add(R.drawable.black_bomb);
                            ChatTest.dest_list_anim.add(0);

                            Intent login_broadcast = new Intent("update_auto_response");
                            service.getApplicationContext().sendBroadcast(login_broadcast);
                            Constant.printMsg("Auto Response Broad 2 :: " + ChatTest.k + " Size " + ChatTest.msg_list.size());
                        }

                        int l = dbadapter.getLastMsgid_chat(jid, sec);
                        Constant.printMsg("looooollll   " + l);
                        Constant.printMsg("Donate buxs msg_id insert   " + l);

                        if (dbadapter.isExistinChatList_chat(jid, sec))
                        {
                            dbadapter.setUpdateChat_lits_chat(jid, l, sec);
                        }
                        else
                        {
                            dbadapter.setInsertChat_list_chat(jid, l, sec);
                        }
                    }
                    catch (Exception e)
                    {

                    }
                }
                catch (Exception e)
                {
                    Constant.printMsg("EEEEEEEEEEE Chat msg send excp ::::  " + e.toString());
                }
            }
        };

        thread.start();



        try
        {
            Constant.printMsg("Double msg:" + stringmsg);
            long lng = stringmsg.getBytes().length;
            updateNetwork_Message_Sent(lng);
        }
        catch (Exception e)
        {

        }
    }

    public void updateNetwork_Message_Sent(long bite)
    {
        try
        {
            long val = 0;
            HashMap<String, String> user = mNewtSharPref.getMessage_SentDetails();
            String value = user.get(NetworkSharedPreference.KEY_MESSAGE_GET_SX);

            if (value != null)
            {
                val = Long.parseLong(value);
            }

            Long update = bite + val;
            String data = String.valueOf(update);
            mNewtSharPref.setMessageData_Sent(data);
        }
        catch (NumberFormatException e)
        {

        }
    }

    @Override
    public void stateChanged(Chat chat, ChatState state, Message message) {

    }
}