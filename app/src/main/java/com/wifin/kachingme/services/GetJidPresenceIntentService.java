package com.wifin.kachingme.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Utils;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smackx.iqlast.LastActivityManager;
import org.jivesoftware.smackx.iqlast.packet.LastActivity;
import org.jxmpp.jid.impl.JidCreate;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by comp on 3/2/2017.
 */
public class GetJidPresenceIntentService extends IntentService {

    public static final String REQUEST_STRING = "jid";
    static String lastseen = null;
    Context context;

    public GetJidPresenceIntentService() {
        super("GetJidPresenceIntentService");
    }

    public static String lastSeenStatus(Context context, String jid) {
        try {
            Calendar cal_today = Calendar.getInstance();
            SimpleDateFormat formatter_date = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat formatter_last = new SimpleDateFormat(
                    "dd-MM-yyyy hh:ssa");
            SimpleDateFormat formatter_today = new SimpleDateFormat("hh:mma");
            /*
			 * Log.d("Last Activity", "Called" +
			 * LastActivityManager.isLastActivitySupported( connection, jid));
			 */
            LastActivity la = LastActivityManager.getInstanceFor(
                    TempConnectionService.connection).getLastActivity(JidCreate.from(jid));

            Calendar cal = Calendar.getInstance();
            Long l = (cal.getTimeInMillis() - (la.getIdleTime() * 1000));
            cal.setTimeInMillis(l);

            String today = formatter_date.format(cal_today.getTime());
            String last_seen_date = formatter_date.format(cal.getTime());
            if (today.toString().equals(last_seen_date)) {

                lastseen = context.getResources().getString(R.string.today_at)
                        + " " + formatter_today.format(cal.getTime());
            } else {
                // Constant.printMsg("last seen todat at " +
                // formatter_today.format(cal.getTime()));
                lastseen = "" + formatter_last.format(cal.getTime());
            }

            return lastseen;

        } catch (XMPPException.XMPPErrorException e1) {
            // TODO: handle exception
            // is_lastseen_available = false;
            return context.getResources().getString(R.string.tap_here_for_info);
        } catch (Exception e) {

            return context.getResources().getString(R.string.tap_here_for_info);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        context = this;
        String jid = intent.getStringExtra(REQUEST_STRING);

        Constant.printMsg("GetJidPresenceStatus called : " + jid);

        final boolean isChatTestIsFront = Utils.isActivityIsFront(context, ChatTest.class.getCanonicalName().toString());

        try {
            if (TempConnectionService.connection != null) {
                Roster roster = Roster
                        .getInstanceFor(TempConnectionService.connection);
                Presence presence = roster.getPresence(JidCreate.bareFrom(jid));

                if (presence.isAvailable()) {

                    Constant.USER_ONLINE_CHEKING_FOR_SEND_PRESENCE = true;


                    sendBroadcast(context.getResources()
                            .getString(R.string.online), true, context);

                } else {
                    Constant.USER_ONLINE_CHEKING_FOR_SEND_PRESENCE = false;
                    if (!presence.isAvailable()) {

                        sendBroadcast(lastSeenStatus(
                                context, jid), false, context);


                    } else {

                        sendBroadcast(context
                                .getString(R.string.tap_here_for_info), false, context);

                    }
                }
            }else {
                sendBroadcast(context
                        .getString(R.string.tap_here_for_info), false, context);
            }

        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public void sendBroadcast(String msg, boolean status, Context context) {
        Intent login_broadcast = new Intent("getjid_online");
        login_broadcast.putExtra("title_msg", msg);
        login_broadcast.putExtra("status", status);
        context.sendBroadcast(login_broadcast);
    }

}
