package com.wifin.kachingme.listeners;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.AvatarManager;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.NetworkSharedPreference;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.Date;
import java.util.HashMap;

public class StatusListenerMethods {

    public static final String TAG = "StatusListenerMethods";
    public int ser_count = 0;
    Context context;
    DatabaseHelper dbAdapter;
    String status_lock = "check";
    SharedPreferences pref;
    Dbhelper db;

    public StatusListenerMethods(Context context) {
        this.context = context;
        dbAdapter = KachingMeApplication.getDatabaseAdapter();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        db = new Dbhelper(context);
    }

    public static void sendReceipt(String toJID, String id, String status) {
        if (status.equalsIgnoreCase(com.wifin.kachingme.util.Constant.STATUS_DELIVERED)) {
            final Message ack = new Message(toJID, Message.Type.chat);
            ack.setSubject(Constant.STATUS_DELIVERED);
            ack.addExtension(new DeliveryReceipt(id));
            try {
                TempConnectionService.connection.sendStanza(ack);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        } else {
            final Message ack = new Message(toJID, Message.Type.chat);
            ack.setSubject(Constant.STATUS_DISPLAYED);
            ack.addExtension(new DeliveryReceipt(id));
            try {
                TempConnectionService.connection.sendStanza(ack);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }

    }

    public String getRow_id(String phoneNumber) {
        String row_id = "";
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));

        String[] projection = new String[]{PhoneLookup.DISPLAY_NAME,
                PhoneLookup._ID};

        Cursor cursor = null;
        try {

            cursor = contentResolver
                    .query(uri, projection, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String contactName = cursor.getString(cursor
                            .getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME));
                    String contactId = cursor.getString(cursor
                            .getColumnIndexOrThrow(PhoneLookup._ID));
                    row_id = contactId;
                    Log.d("Service", "contactMatch name: " + contactName);
                    Log.d("Service", "contactMatch id: " + contactId);
                }
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return row_id;
    }

    public void Add_New_Contact(String jid) {
        Log.d("AsyncAdd_contact", "Add contact called::" + jid);
        new AsyncAdd_Contact().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, jid);

    }

    public void issueNotification(String from, String key_id) {
        Constant.printMsg(":print num    " + from);
        Constant.printMsg(":DDDDDDD  " + from);
        try {

            SharedPreferences preference = PreferenceManager
                    .getDefaultSharedPreferences(context);
            String strRingtonePreference = preference.getString(
                    "notify_ringtone", "DEFAULT_SOUND");
            Boolean conversation_sound = preference.getBoolean(
                    "conversation_sound", true);
            int notify_vibrate_length = Integer.parseInt(preference.getString(
                    "notify_vibrate_length", "1"));
            String notify_light_color = preference.getString(
                    "notify_light_color", "0");

            dbAdapter = KachingMeApplication.getDatabaseAdapter();

            MessageGetSet msggetset = dbAdapter.getMessages_by_key_id(key_id);
            ContactsGetSet contact = dbAdapter.getContact(from);

            String notification_label = null;
            String text = null;
            Constant.printMsg("number check  lock " + contact.getJid()
                    + contact.getDisplay_name());

            String query = "select status from " + Dbhelper.TABLE_LOCK
                    + " where jid = '" + contact.getJid() + "'";
            Constant.printMsg("bhaththam " + query);

            lock_status(query);

            if (status_lock.equalsIgnoreCase("lock")) {
                notification_label = " ";
                text = " ";
            } else {

                if (msggetset.getMedia_wa_type().equals("0")) {
                    text = msggetset.getData();
                    byte[] b = text.getBytes("UTF-8");

                    // Msg Network receive size
                    long txtSize = (long) b.length;
                    Constant.printMsg("session rec" + text);
                    updateNetwork_Message_Receive(txtSize);

                    if (text.length() > 3) {
//                        contact_msg = "you have received message from";
                        notification_label = text.toString();
                        char s = text.charAt(0);
                        char s1 = text.charAt(1);
                        char s2 = text.charAt(2);
                        if (s == '<') {

                            Constant.printMsg("ldfhadf11");

                            if (s1 == '-') {

                                notification_label = "Nymn";

//                                Constant.printMsg("ldfhadf222");
//                                String[] parts = text.toString().split("-");
//                                Constant.printMsg("ldfhadf333a"+parts.length);
//                                String part1 = parts[0];
//                                Constant.printMsg("ldfhadf333s"+part1);
//                                String part2 = parts[1];
//                                Constant.printMsg("ldfhadf333f"+part2);
//                                String part3 = parts[2];
//                                Constant.printMsg("ldfhadf333ff"+part3);
//                                String part4 = parts[3];
//                                Constant.printMsg("ldfhadf333sew"+part4);
//                                notification_label = part3;
//                                Constant.printMsg("ldfhadf333q  ");
//                                Constant.printMsg("called8 nym:::"
//                                        + notification_label);

                            } else if (s1 == 'b' && s2 == '>') {
                                Constant.printMsg("called8:1:::::::"
                                        + text.toString());
                                notification_label = text.toString().substring(
                                        3);
                                Constant.printMsg("called8:1:::::::"
                                        + notification_label);
                            } else if (s1 == 'z' && s2 == '>') {
                                Constant.printMsg("called8::::::4::"
                                        + text.toString());
                                notification_label = text.toString().substring(
                                        3);
                                Constant.printMsg("called8:1:::::::"
                                        + notification_label);
                            } else if (s1 == 'l' && s2 == '>') {
                                Constant.printMsg("called8:::5:::::"
                                        + text.toString());
                                notification_label = text.toString().substring(
                                        3);
                                Constant.printMsg("called8:1:::::::"
                                        + notification_label);
                            } else if (s1 == 'x' && s2 == '>') {
                                Constant.printMsg("called8:::11:::::"
                                        + text.toString());
                                String dazzle = text.toString().substring(3);
                                String[] parts = dazzle.split("-");
                                String part1 = parts[0];
                                String part2 = parts[1];
                                String part3 = parts[2];
                                String part4 = parts[3];
                                String part5 = parts[4];
                                notification_label = part5;
                                Constant.printMsg("called8:1:::::::"
                                        + notification_label);
                            } else if (s1 == 'k' && s2 == '>') {
                                Constant.printMsg("called8:::11:::::"
                                        + text.toString());
                                notification_label = "KonS";
                                Constant.printMsg("called8:1:::::::"
                                        + notification_label);
                            } else if (s1 == 's' && s2 == '>') {
                                String[] parts = text.split("-");
                                final String part1 = parts[0];
                                String part2 = parts[1];
                                notification_label = part2;
                                Constant.printMsg("called8:1:::::::"
                                        + notification_label);
                            } else if (s1 == 'd' && s2 == '>') {
                                notification_label = contact.getDisplay_name()
                                        + " donates " + text.substring(3)
                                        + " BuxS for you";
                            } else {
                                Constant.printMsg("called8:56:::::::"
                                        + text.toString());
                                notification_label = msggetset.getData();
                                Constant.printMsg("called8:1111:::::::"
                                        + notification_label);
                            }
                        }else {
                            Constant.printMsg("called8:56:::::::"
                                    + text.toString());
                            notification_label = msggetset.getData();
                            Constant.printMsg("called8:1111:::::::"
                                    + notification_label);
                        }
                    }else {
                        Constant.printMsg("called8:56:::::::"
                                + text.toString());
                        notification_label = msggetset.getData();
                        Constant.printMsg("called8:1111:::::::"
                                + notification_label);
                    }
                } else if (msggetset.getMedia_wa_type().equals("1")) {
                    notification_label = "Image";
                } else if (msggetset.getMedia_wa_type().equals("2")) {
                    notification_label = "Video";
                } else if (msggetset.getMedia_wa_type().equals("3")) {
                    notification_label = "Audio";
                } else if (msggetset.getMedia_wa_type().equals("4")) {
                    notification_label = "Location";
                } else if (msggetset.getMedia_wa_type().equals("5")) {
                    notification_label = "Contact";
                } else if (msggetset.getMedia_wa_type().equals("6")) {
                    notification_label = "File";
                } else if (msggetset.getMedia_wa_type().equals("11")) {
                    notification_label = "Image";
                } else if (msggetset.getMedia_wa_type().equals("12")) {
                    notification_label = "Image";
                }
            }

            Constant.printMsg("ldfhadf444");
            Constant.FROM_CHAT_SCREEN = "notification";
            Intent intent = new Intent(context, ChatTest.class);
            intent.putExtra("jid", from);
            intent.putExtra("name", contact.getDisplay_name());
            intent.putExtra("avatar", contact.getPhoto_ts());
            if (msggetset.getIs_sec_chat() == 0) {
                intent.putExtra("IS_SECRET_CHAT", true);
                intent.putExtra("is_new_sec", true);

            } else {
                intent.putExtra("IS_SECRET_CHAT", false);
                intent.putExtra("is_new_sec", false);
            }
            intent.putExtra("is_owner", "" + msggetset.getIs_owner());
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack
            stackBuilder.addParentStack(SliderTesting.class);
            // Adds the Intent to the top of the stack
            stackBuilder.addNextIntent(intent);

            PendingIntent pIntent = stackBuilder.getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Constant.printMsg("jid::" + contact.getJid());

            Bitmap avtar_bitmap = null;

            try {

                byte[] avatar = contact.getPhoto_ts();
                if (avatar == null) {
                    avtar_bitmap = BitmapFactory.decodeResource(
                            context.getResources(), R.drawable.ic_launcher);
                    Constant.mReciverAvathor = avtar_bitmap;

                } else {
                    avtar_bitmap = BitmapFactory.decodeByteArray(avatar, 0,
                            avatar.length);
                    Constant.mReciverAvathor = avtar_bitmap;
                }


            } catch (Exception e) {

            }

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(android.content.Context.NOTIFICATION_SERVICE);
            String userName = "";
            Constant.printMsg("siva getting notificatiomn...." + contact.getDisplay_name() + "..." + contact.getJid() + "..." + contact.getNumber());
            if (contact.getDisplay_name() != null && !contact.getDisplay_name().isEmpty()) {
                userName = contact.getDisplay_name();
            } else if (contact.getNumber() != null) {
                userName = contact.getNumber();
            } else {
                userName = from.split("@")[0];
            }


            try {

                Notification notification = new NotificationCompat.Builder(context)
                        .setContentTitle(userName)
                        .setContentText(notification_label)
                        .setTicker(notification_label)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setLargeIcon(avtar_bitmap).setAutoCancel(true)
                        .setContentIntent(pIntent).build();
                int received_msg_count = pref.getInt("received_msg_count", 0);
                Constant.printMsg("received msg chat::" + received_msg_count);
                received_msg_count = received_msg_count + 1;
                Constant.printMsg("received msg chat::" + received_msg_count);
                update_received_count(received_msg_count);
                if (conversation_sound) {
                    if (strRingtonePreference.equals("DEFAULT_SOUND")) {
                        notification.defaults |= Notification.DEFAULT_SOUND;
                    } else {
                        notification.sound = Uri.parse(strRingtonePreference);
                    }
                }

                if (notify_vibrate_length == 1) {
                    notification.defaults |= Notification.DEFAULT_VIBRATE;
                } else if (notify_vibrate_length == 2) {
                    long[] l = {1000, 1000};
                    notification.vibrate = l;
                } else if (notify_vibrate_length == 3) {
                    long[] l = {2000, 2000};
                    notification.vibrate = l;
                }

                if (notify_light_color.equals("1")) {
                    notification.ledARGB = Color.WHITE;
                    Log.d(TAG, "Notification Light Color::" + notify_light_color);
                } else if (notify_light_color.equals("2")) {
                    // notification.ledARGB=Color.RED;
                    notification.ledARGB = 0xffff0000;
                    Log.d(TAG, "Notification Light Color::" + notify_light_color);
                } else if (notify_light_color.equals("3")) {
                    notification.ledARGB = Color.YELLOW;
                    Log.d(TAG, "Notification Light Color::" + notify_light_color);
                } else if (notify_light_color.equals("4")) {
                    notification.ledARGB = Color.GREEN;
                    Log.d(TAG, "Notification Light Color::" + notify_light_color);
                } else if (notify_light_color.equals("5")) {
                    notification.ledARGB = Color.CYAN;
                    Log.d(TAG, "Notification Light Color::" + notify_light_color);
                } else if (notify_light_color.equals("6")) {
                    notification.ledARGB = Color.BLUE;
                    Log.d(TAG, "Notification Light Color::" + notify_light_color);
                } else if (notify_light_color.equals("7")) {
                    notification.ledARGB = Color.MAGENTA;
                    Log.d(TAG, "Notification Light Color::" + notify_light_color);
                } else if (notify_light_color.equals("11")) {
                    notification.ledARGB = Color.WHITE;
                    Log.d(TAG, "Notification Light Color::" + notify_light_color);
                }

                notification.ledOnMS = 100;
                notification.ledOffMS = 100;

                notification.flags |= Notification.FLAG_SHOW_LIGHTS
                        | Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(contact.getId(), notification);
                KachingMeApplication.setIS_FROM_NOTIFICATION(true);
                Constant.printMsg("Noti::Finished..");

            } catch (Exception e) {

            }


        } catch (Exception e) {
            // TODO: handle exception
            // ACRA.getErrorReporter().handleException(e);
            e.printStackTrace();
        }
        // h.sendMessage(msg);

    }

    private String lock_status(String query) {
        // TODO Auto-generated method stub
        Cursor c = null;

        try {

            Constant.printMsg("query  " + query);

            c = db.open().getDatabaseObj().rawQuery(query, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
            c.moveToFirst();
            if (c.getCount() > 0) {
                Constant.printMsg("lock_status " + c.getString(0));
                status_lock = c.getString(0);
                Constant.printMsg("status_lock   " + status_lock);
            } else {
                status_lock = "not_lock";
            }

        } catch (SQLException e) {

        } finally {
            if (c != null)
                c.close();
            if (db != null)
                db.close();
        }
        return status_lock;

    }

    public void update_received_count(int code) {

        ser_count++;

        Constant.printMsg("received msg update_received_count added is Nifty::"
                + code);
        Editor e = pref.edit();
        e.putInt("received_msg_count", code);
        Constant.printMsg("received_msg_count in nifty service:::" + code);
        e.commit();

    }

    // Updating the Shared Preference with Messaging Sent Network Usage....
    public void updateNetwork_Message_Receive(long bite) {
        NetworkSharedPreference mNewtSharPref = new NetworkSharedPreference(
                context);
        long val = 0;
        // get user data from session
        HashMap<String, String> user = mNewtSharPref
                .getMessage_ReceiveDetails();

        // name
        String value = user.get(NetworkSharedPreference.KEY_MESSAGE_GET_RX);
        if (value != null) {

            val = Long.parseLong(value);
        }
        long update = bite + val;

        String data = String.valueOf(update);

        mNewtSharPref.setMessageData_Receive(data);
    }

    public void ChangeVcard(String jid) {
        new AsyncVCard().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, jid);
    }

    public class AsyncAdd_Contact extends AsyncTask<String, String, String> {
        VCard vc = new VCard();
        String jid;
        ContactsGetSet contact = new ContactsGetSet();
        Boolean is_vcard_found = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            jid = params[0];
            try {

                Log.d(TAG, "Load vcard for::" + jid);
                vc = VCardManager.getInstanceFor(TempConnectionService.connection).loadVCard(jid);
                contact.setStatus(vc.getField("SORT-STRING"));
                contact.setNifty_email(vc.getEmailWork());
                contact.setNifty_name(vc.getFirstName());
                contact.setJid(vc.getJabberId());
                // contact.setDisplay_name(vc.getFirstName());
                Constant.printMsg("print ::::22222 " + vc.getFirstName());

                contact.setDisplay_name(jid.split("@")[0]);

                contact.setIs_niftychat_user(1);
                contact.setJid(jid);
                contact.setNumber(jid.split("@")[0]);
                contact.setIsInContactList(0);
                if (getRow_id(jid.split("@")[0]).equals("")) {
                    contact.setRaw_contact_id("" + new Date().getTime());
                } else {
                    String s = getRow_id(jid.split("@")[0]);
                    contact.setRaw_contact_id(s);
                }

                contact.setUnseen_msg_count(0);

                AvatarManager av = new AvatarManager();
                contact.setPhoto_ts(new AvatarManager().saveBitemap(vc
                        .getAvatar()));
                is_vcard_found = true;

                Log.i(TAG, "VCARD::" + vc.toString());
                Log.d(TAG, "VC_EMail::" + vc.getEmailWork());
                Log.d(TAG, "VC_FIRST_NAME::" + vc.getFirstName());
                Log.d(TAG, "VC_JID::" + vc.getJabberId());
                // Log.d(TAG, "VC_IS_UPDATED::"+l);

            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();

            }

            Log.d("Vcard", "VCard Loaded For::" + vc.toXML());
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Constant.printMsg("siva check enter into insert...1....." + vc.getJabberId() + "...." + contact.getJid() +
                    "...." + KachingMeApplication.getUserID()
                    + KachingMeApplication.getHost());

            if (vc.getJabberId() != null
                    && !contact.getJid().equals(
                    KachingMeApplication.getUserID()
                            + KachingMeApplication.getHost()))

            {
                Constant.printMsg("siva check enter into insert........");
                DatabaseHelper dbAdapter = KachingMeApplication
                        .getDatabaseAdapter();
                if (!dbAdapter.isjidExist(vc.getJabberId())) {
                    Constant.printMsg("siva check enter into insert....2....");
                    Constant.printMsg(":DDDDDDD  hgh ");
                    long l = dbAdapter.insertContacts(contact);
                    if (l > 0) {
                        if (TempConnectionService.connection.isAuthenticated()) {
                            Presence presencePacket = new Presence(Presence.Type.subscribe);
                            presencePacket.setTo(jid);
                            try {
                                TempConnectionService.connection.sendStanza(presencePacket);
                            } catch (SmackException.NotConnectedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                } else {
                    Constant.printMsg("siva check enter into insert....3....");
                    dbAdapter.setUpdateVcard(contact);
                }
            }

            if (ChatStatusListener.chat_msg_id != null)
                issueNotification(jid, ChatStatusListener.chat_msg_id);

            Intent intent = new Intent("Add_New_Contact");
            intent.putExtra("jid", jid);
            context.sendBroadcast(intent);
            super.onPostExecute(result);
        }
    }

    public class AsyncVCard extends AsyncTask<String, String, String> {
        VCard vc_new = new VCard();
        String jid;
        ContactsGetSet contact = new ContactsGetSet();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            jid = params[0];
            try {
                DatabaseHelper dbAdapter = KachingMeApplication
                        .getDatabaseAdapter();

                vc_new = VCardManager.getInstanceFor(TempConnectionService.connection).loadVCard(jid);

                contact.setStatus(vc_new.getField("SORT-STRING"));
                contact.setNifty_email(vc_new.getEmailWork());
                contact.setNifty_name(vc_new.getFirstName());
                contact.setJid(vc_new.getJabberId());

                AvatarManager av = new AvatarManager();
                contact.setPhoto_ts(new AvatarManager().saveBitemap(vc_new
                        .getAvatar()));

                long l = dbAdapter.setUpdateVcard(contact);

                Log.i(TAG, "VCARD::" + vc_new.toString());
                Log.d(TAG, "VC_EMail::" + vc_new.getEmailWork());
                Log.d(TAG, "VC_FIRST_NAME::" + vc_new.getFirstName());
                Log.d(TAG, "VC_JID::" + vc_new.getJabberId());
                Log.d(TAG, "VC_IS_UPDATED::" + l);

            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                // TODO: handle exception
            }

            Log.d("Vcard", "VCard Loaded For::" + vc_new.getField("status"));
            Log.d("Vcard",
                    "VCard Loaded For::"
                            + vc_new.toString().substring(6000,
                            (vc_new.toString().length() - 3)));
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(Constant.PROFILE_UPDATE);
            intent.putExtra("jid", jid);
            context.sendBroadcast(intent);
            super.onPostExecute(result);
        }
    }


}
