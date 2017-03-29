package com.wifin.kachingme.listeners;

import android.content.Context;
import android.content.Intent;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;

import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;

public class RecieptRecievedListenersImpl implements ReceiptReceivedListener {

    TempConnectionService service;
    DatabaseHelper dbAdapter;
    Context context;

    public RecieptRecievedListenersImpl(TempConnectionService ser) {
        service = ser;
        context = (Context) ser;
        dbAdapter = KachingMeApplication.getDatabaseAdapter();
    }

    @Override
    public void onReceiptReceived(String from, String to, String recieptid,
                                  Stanza receipt) {

        try {

            Log.d("Message Delivery Reciept ID", "From ::" + from + " To::" + to
                    + " ID::" + recieptid);
            Constant.printMsg("notification1::::>>>>.   " + "From ::" + from
                    + " To::" + to + " ID::" + recieptid);
            String fromUser = from.toString().split("/")[0];

            MessageGetSet messagegetset = dbAdapter
                    .getMessages_by_key_id(recieptid);
            if (messagegetset.getIs_sec_chat() == 0
                    && !messagegetset.getMedia_wa_type().equals("7")
                    && messagegetset.getSelf_des_time() != 0) {
//			new Self_Destruct_Messages(context).setDestruct(
//					"" + messagegetset.get_id(),
//					messagegetset.getSelf_des_time(),
//					messagegetset.getKey_remote_jid());
            }
            Constant.printMsg("notification Message Delivery Reciept ID From ::"
                    + from + " To::" + to + " ID::" + recieptid);

            try {
                dbAdapter.setUpdateMessage_status(fromUser, recieptid, 0);
            } catch (Exception e) {

            }
            Intent login_broadcast = new Intent("chat");
            login_broadcast.putExtra("jid", "" + fromUser);

            service.getApplicationContext().sendBroadcast(login_broadcast);

        } catch (Exception e) {

        }


        // TODO Auto-generated method stub

    }

}
