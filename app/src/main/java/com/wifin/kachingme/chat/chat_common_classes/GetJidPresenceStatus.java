package com.wifin.kachingme.chat.chat_common_classes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smackx.iqlast.LastActivityManager;
import org.jivesoftware.smackx.iqlast.packet.LastActivity;
import org.jxmpp.jid.impl.JidCreate;

import android.app.Activity;
import android.view.View;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.Utils;


public class GetJidPresenceStatus {

    public static void getPresenceStatus(final Activity context,
                                         final String jid) {

        Constant.printMsg("GetJidPresenceStatus called : " + jid);

        final boolean isChatTestIsFront = Utils.isActivityIsFront(context, ChatTest.class.getCanonicalName().toString());

        try {
            Roster roster = Roster
                    .getInstanceFor(TempConnectionService.connection);
            Presence presence = roster.getPresence(JidCreate.bareFrom(jid));


            if (presence.isAvailable()) {

                Constant.USER_ONLINE_CHEKING_FOR_SEND_PRESENCE = true;

                // online
                context.runOnUiThread(new Runnable() {
                    public void run() {

                        if (isChatTestIsFront) {
                            ChatTest.txt_sub_title.setText(context.getResources()
                                    .getString(R.string.online));
                            ChatTest.isJidOnline = true;
                        }


                    }
                });
            } else {
                Constant.USER_ONLINE_CHEKING_FOR_SEND_PRESENCE = false;
                if (!presence.isAvailable()) {
                    context.runOnUiThread(new Runnable() {
                        public void run() {

                            if (isChatTestIsFront) {
                                ChatTest.txt_sub_title.setText(lastSeenStatus(
                                        context, jid));
                                ChatTest.isJidOnline = false;
                            }


                        }
                    });

                } else {
                    context.runOnUiThread(new Runnable() {
                        public void run() {

                            if (isChatTestIsFront) {
                                ChatTest.txt_sub_title.setText(context
                                        .getString(R.string.tap_here_for_info));

                                ChatTest.isJidOnline = false;
                            }

                        }
                    });
                }
            }

        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    static String lastseen = null;

    public static String lastSeenStatus(Activity context, String jid) {
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

        } catch (XMPPErrorException e1) {
            // TODO: handle exception
            // is_lastseen_available = false;
            return context.getResources().getString(R.string.tap_here_for_info);
        } catch (Exception e) {

            return context.getResources().getString(R.string.tap_here_for_info);
        }
    }

}
