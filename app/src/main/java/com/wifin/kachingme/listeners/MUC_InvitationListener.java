package com.wifin.kachingme.listeners;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.SerialExecutor;


import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.packet.MUCUser;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jxmpp.jid.EntityJid;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Queue;
import java.util.concurrent.Executor;

public class MUC_InvitationListener implements InvitationListener {

    AbstractXMPPConnection connection;
    Context context;
    DatabaseHelper dbadapter;
    VCard vc = new VCard();
    ContentResolver cr;
    ArrayList<String> non_contactlist = new ArrayList<String>();
    Context service;
    MultiUserChat muc;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    SerialExecutor serialExecutor;


    public MUC_InvitationListener(Context context, Context service1) {
        this.context = context;
        service = service1;
        connection = TempConnectionService.connection;
        dbadapter = KachingMeApplication.getDatabaseAdapter();
        cr = context.getContentResolver();

        sp = this.context.getSharedPreferences(
                KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
        editor = sp.edit();
        serialExecutor = new SerialExecutor(new Executor() {
            @Override
            public void execute(Runnable runnable) {

                runnable.run();
            }
        });
    }



    @Override
    public void invitationReceived(XMPPConnection conn, MultiUserChat room, EntityJid inviter, String reason, String password, Message message, MUCUser.Invite invitation) {
        {
            // TODO Auto-generated method stub
            Constant.printMsg("Invitation received is inviter temp:" + inviter
                    + " room:" + reason + " isAuth::"
                    + password);


            final MultiUserChat argument = room;
            final String invite = inviter.toString();

            try {
                Calendar c = Calendar.getInstance(/* TimeZone.getTimeZone("UTC") */);
                editor.putLong("last_refresh_time",/* new Date().getTime() */
                        c.getTimeInMillis());
                editor.commit();



                Runnable runnable1 =  new Runnable() {
                    @Override
                    public void run() {
                        try
                        {

                            Constant.printMsg("MUC Invitation before sleep....");
                            Thread.sleep(3000);
                            Constant.printMsg("MUC Invitation after sleep....");



                            if(argument.getRoom()!=null && !dbadapter.isjidExist(argument.getRoom().toString()))
                                new MUC_ListenerMethods(context).setJoinRoom(argument.getRoom().toString(), invite);
                        }catch (Exception e)
                        {

                        }
                    }
                };

                serialExecutor.execute(runnable1);

            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                Log.d("Room::Bookmarking:", e.toString());
            }

        }
    }
}
