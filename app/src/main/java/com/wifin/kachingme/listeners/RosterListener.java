package com.wifin.kachingme.listeners;

import java.util.Collection;

import org.jivesoftware.smack.packet.Presence;
import org.jxmpp.jid.Jid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.util.Constant;

public class RosterListener implements
        org.jivesoftware.smack.roster.RosterListener {

    DatabaseHelper dbAdapter;
    Context context;
    SharedPreferences sp;
    Editor editor;

    public RosterListener(Context con) {
        context = con;
        dbAdapter = KachingMeApplication.getDatabaseAdapter();

        sp = context.getSharedPreferences(
                KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public void entriesAdded(Collection<Jid> addresses) {

    }

    @Override
    public void entriesUpdated(Collection<Jid> addresses) {

    }

    @Override
    public void entriesDeleted(Collection<Jid> addresses) {

    }

    @Override
    public void presenceChanged(Presence presence) {
        // Presence p=new Presence(Type.);

        Constant.printMsg("FFFFFFFFFF" + presence);

        // Toast.makeText(
        // context,
        // "Presence is cahnged::" + presence.getFrom() + " : type::"
        // + presence.getType() + " Status::"
        // + presence.getStatus(), Toast.LENGTH_LONG).show();
        String fromUser = presence.getFrom().toString().split("/")[0];

		/*
         * Presence response = new Presence(Presence.Type.unsubscribed);
		 * response.setTo(presence.getFrom()); try {
		 * TempConnectionService.connection.sendStanza(response); } catch
		 * (NotConnectedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

        Intent login_broadcast = new Intent("lastseen_broadcast");
        login_broadcast.putExtra("from", fromUser);
      /*  if (presence.getStatus() != null) {
            String status;
            String typing_msg = null;

            try {

                if(presence.getStatus().toString().contains("@"))
                {
                    status = presence.getStatus().split("@")[0];
                     typing_msg = presence.getStatus().split("@")[1];
                }
                else
                {
                     status = presence.getStatus();
                }

                Constant.printMsg("FFFFFFF split " + status +" " + typing_msg);



            if (status.equalsIgnoreCase(Constant.TYPING_STRING)) {

                login_broadcast.putExtra("type_msg", typing_msg);
                login_broadcast.putExtra("type", Constant.TYPING_STRING);

            } else if (status.equalsIgnoreCase(Constant.TYPING_STATUS_RECORDING)) {
                login_broadcast.putExtra("type", Constant.TYPING_STATUS_RECORDING);
            } else  {
                login_broadcast.putExtra("type", "jid_status_from_presence");
            }

            }
            catch (Exception e)
            {
                Constant.printMsg("FFFFFFF split eee" + e.toString());
            }
        } else {
            login_broadcast.putExtra("type", "jid_status_from_presence");
        }
        context.sendBroadcast(login_broadcast);*/
		/*
		 * if(presence.getProperty("is_lastseen")!=null) {
		 * editor.putBoolean(fromUser
		 * +"_is_lastseen",(Boolean)presence.getProperty("is_lastseen"));
		 * editor.commit(); }
		 */
    }

}
