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

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.ArrayList;
import java.util.Calendar;

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


    public MUC_InvitationListener(Context context, Context service1) {
        this.context = context;
        service = service1;
        connection = TempConnectionService.connection;
        dbadapter = KachingMeApplication.getDatabaseAdapter();
        cr = context.getContentResolver();

        sp = this.context.getSharedPreferences(
                KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public void invitationReceived(XMPPConnection arg0, MultiUserChat arg1,
                                   String inviter, String arg3, String arg4, Message arg5) {
        // TODO Auto-generated method stub
        Constant.printMsg("Invitation received is inviter temp:" + inviter
                + " room:" + arg1.getSubject() + " isAuth::"
                + arg0.isAuthenticated());

        try {
            Calendar c = Calendar.getInstance(/* TimeZone.getTimeZone("UTC") */);
            editor.putLong("last_refresh_time",/* new Date().getTime() */
                    c.getTimeInMillis());
            editor.commit();


            if (arg1.getSubject()!=null) {
                new MUC_ListenerMethods(context).setJoinRoom(arg1.getRoom(), inviter);
            }


        } catch (Exception e) {
            // ACRA.getErrorReporter().handleException(e);
            e.printStackTrace();
            Log.d("Room::Bookmarking:", e.toString());
        }

    }

}
