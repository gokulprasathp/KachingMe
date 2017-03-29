package com.wifin.kachingme.listeners;

import android.content.Context;
import android.content.Intent;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat.muc_chat.MUCTest;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Self_Destruct_Messages;
import com.wifin.kachingme.util.Utils;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;

public class RecieptRecievedListener implements ReceiptReceivedListener {

    DatabaseHelper dbAdapter;
    Context context;

    public RecieptRecievedListener(Context ser) {
        context = (Context) ser;
        dbAdapter = KachingMeApplication.getDatabaseAdapter();

        Constant.printMsg("EEEEEEEE Context added... notification1::::>>>>");
    }

    @Override
    public void onReceiptReceived(String from, String to, String recieptid,
                                  Stanza receipt) {
        Constant.printMsg("EEEEEEEE notification1::::>>>>.   " + "From ::"  +recieptid +"    "+from+"    "+to+"   "+receipt.toXML());

        int msg_status = 0;
        Message m = (Message) receipt;

        try {


            if (m.getSubject().equals(Constant.STATUS_DISPLAYED)) {
                msg_status = -1;
            } else if (m.getSubject().equals(Constant.STATUS_DELIVERED)) {
                msg_status = 2;
            }

        } catch (Exception e) {

        }

        MessageGetSet messagegetset=null;
        try {
            messagegetset = dbAdapter
                    .getMessages_by_key_id(recieptid);

        } catch (Exception e) {

        }

        try {
            String fromUser = from.toString().split("/")[0];

            // Receiver seen message status..
            if (msg_status == -1) {


                if (messagegetset.getIs_sec_chat() == 0
                        && !messagegetset.getMedia_wa_type().equals("7")
                        && messagegetset.getSelf_des_time() != 0) {
                    new Self_Destruct_Messages(context).setDestruct(
                            "" + messagegetset.get_id(),
                            messagegetset.getSelf_des_time(),
                            messagegetset.getKey_remote_jid());
                }

                try {
                    dbAdapter.setUpdateMessage_status(fromUser, recieptid, -1);

                } catch (Exception e) {

                }

                if (Utils.isActivityIsFront(context, ChatTest.class.getCanonicalName().toString())) {

                    for (int i = 0; i < ChatTest.msg_list.size(); i++) {

                        if (ChatTest.msg_list.get(i).getKey_id().toString().equalsIgnoreCase(recieptid)) {

                            Intent login_broadcast = new Intent("update_tick");
                            login_broadcast.putExtra("position", "" + i);
                            login_broadcast.putExtra("status", "displayed");
                            context.getApplicationContext().sendBroadcast(login_broadcast);
                        }
                        else if (Utils.isActivityIsFront(context, SliderTesting.class.getCanonicalName())) {
                            Constant.printMsg("MUCCCCCCCC slider");
                            Intent login_broadcast = new Intent("lastseen_broadcast");
                            login_broadcast.putExtra("from",fromUser);
                            login_broadcast.putExtra("type", ChatTest.msg_list.get(i).getData());
                            context.getApplicationContext().sendBroadcast(login_broadcast);
                        }

                    }

                } else if (Utils.isActivityIsFront(context, MUCTest.class.getCanonicalName().toString())) {

                    for (int i = 0; i < ChatTest.msg_list.size(); i++) {

                        if (MUCTest.msg_list.get(i).getKey_id().toString().equalsIgnoreCase(recieptid)) {

                            Intent login_broadcast = new Intent("update_tick");
                            login_broadcast.putExtra("position", "" + i);
                            login_broadcast.putExtra("status", "displayed");
                            context.getApplicationContext().sendBroadcast(login_broadcast);
                        }

                    }
                }

            } else if (msg_status == 2) {  //  Receiver getting delivered  message status..

                int statuss = dbAdapter.getMessageStatus(fromUser, recieptid);
                Constant.printMsg("" +
                        "RRRRRR status delivery : " + statuss);
                try {


                    if (statuss != 0) {

                        if (messagegetset.getIs_sec_chat() == 0
                                && !messagegetset.getMedia_wa_type().equals("7")
                                && messagegetset.getSelf_des_time() != 0) {
//                        new Self_Destruct_Messages(context).setDestruct(""
//                                        + messagegetset.get_id(),
//                                messagegetset.getSelf_des_time(),
//                                messagegetset.getKey_remote_jid());
                        }

                        dbAdapter.setUpdateMessage_status(fromUser, recieptid, 0);

                        if (Utils.isActivityIsFront(context, ChatTest.class.getCanonicalName().toString())) {

                            for (int i = 0; i < ChatTest.msg_list.size(); i++) {

                                if (ChatTest.msg_list.get(i).getKey_id().toString().equalsIgnoreCase(recieptid)) {

                                    Intent login_broadcast = new Intent("update_tick");
                                    login_broadcast.putExtra("position", "" + i);
                                    login_broadcast.putExtra("status", "delivered");
                                    context.getApplicationContext().sendBroadcast(login_broadcast);
                                }

                            }

                        } else if (Utils.isActivityIsFront(context, MUCTest.class.getCanonicalName().toString())) {

                            for (int i = 0; i < MUCTest.msg_list.size(); i++) {

                                if (MUCTest.msg_list.get(i).getKey_id().toString().equalsIgnoreCase(recieptid)) {

                                    Intent login_broadcast = new Intent("update_tick");
                                    login_broadcast.putExtra("position", "" + i);
                                    login_broadcast.putExtra("status", "delivered");
                                    context.getApplicationContext().sendBroadcast(login_broadcast);
                                }

                            }
                        }

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

        }catch (Exception e1)

        {

        }


    }


}
