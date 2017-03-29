package com.wifin.kachingme.listeners;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat.muc_chat.MUCTest;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.settings.SharedPrefPrivacy;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.Self_Destruct_Messages;
import com.wifin.kachingme.util.Utils;

import org.jivesoftware.smackx.xevent.MessageEventNotificationListener;

import java.util.HashMap;
import java.util.List;

public class MessageEventNooficationList implements
        MessageEventNotificationListener {

    DatabaseHelper dbAdapter;
    Context context;

    public MessageEventNooficationList(Context cont) {

        Constant.printMsg("HHHHHH  msg event listener cancel");

        context = cont;
        dbAdapter = KachingMeApplication.getDatabaseAdapter();

    }

    @Override
    public void cancelledNotification(String from, String packetID) {
        Constant.printMsg("HHHHHH  msg event listener cancel");
    }

    @Override
    public void composingNotification(String from, String packetID) {


        String fromUser = from.toString().split("/")[0];

        Constant.printMsg("HHHHHH  msg event listener" + packetID + "--"
                + fromUser);
        chatTypingStatus(fromUser, packetID);


		/*if (packetID.equalsIgnoreCase(Constant.TYPING_STATUS_GROUP)) {
            Intent login_broadcast = new Intent("typing_group");
			login_broadcast.putExtra("status", "typing...");
			login_broadcast.putExtra("jid", "" + fromUser);
			context.getApplicationContext().sendBroadcast(login_broadcast);
		} else {

			if (packetID.equalsIgnoreCase(Constant.TYPING_STATUS)) {
				if (!fromUser.equals(KachingMeApplication.getUserID()
						+ KachingMeApplication.getHost())) {
					Intent login_broadcast = new Intent("typing");
					login_broadcast.putExtra("status", "typing...");
					login_broadcast.putExtra("jid", "" + fromUser);
					context.getApplicationContext().sendBroadcast(
							login_broadcast);

					Constant.printMsg("called::>>> secret");

				}
			} else if (packetID.equalsIgnoreCase(Constant.ONLINE_STATUS)) {
				Constant.isOnline = true;
				Constant.printMsg("HHHHHH  msg event listener");
				Intent login_broadcast = new Intent("typing");
				login_broadcast.putExtra("status", "online");
				login_broadcast.putExtra("jid", "" + fromUser);
				context.getApplicationContext().sendBroadcast(login_broadcast);

			} else if (packetID.equalsIgnoreCase(Constant.TAP_HERE)) {
				Constant.printMsg("HHHHHH  msg event listener");
				Intent login_broadcast = new Intent("typing");
				login_broadcast.putExtra("status", "tap here for info");
				login_broadcast.putExtra("jid", "" + fromUser);
				context.getApplicationContext().sendBroadcast(login_broadcast);
			} else {
				String lastSeen = getLastSeenStatus(context);
				Intent login_broadcast = new Intent("typing");

				if (!lastSeen.equalsIgnoreCase(SharedPrefPrivacy.EVERY_ONE)) {
					if (lastSeen.equalsIgnoreCase(SharedPrefPrivacy.MY_CONTACT)
							&& dbAdapter.isjidExist(fromUser) == true) {
						login_broadcast.putExtra("status", packetID);
					} else {
						login_broadcast.putExtra("status", "tap here for info");
					}
				} else
					login_broadcast.putExtra("status", packetID);

				login_broadcast.putExtra("jid", "" + fromUser);
				context.getApplicationContext().sendBroadcast(login_broadcast);
			}
		}*/
    }

    public String getLastSeenStatus(Context context) {
        HashMap<String, String> map = new SharedPrefPrivacy(context)
                .getLastSeen();
        String last_seen = map.get(SharedPrefPrivacy.KEY_LAST_SEEN_GET);

        return last_seen;
    }

    @Override
    public void deliveredNotification(String from, String packetID) {
        // TODO Auto-generated method stub

        Constant.printMsg("HHHHHH  msg event listener Deliverd");
        try {

            String fromUser = from.toString().split("/")[0];
            // Constant.printMsg("Display Notification Received from::"+fromUser);
            MessageGetSet messagegetset = dbAdapter
                    .getMessages_by_key_id(packetID);
            if (messagegetset.getIs_sec_chat() == 0
                    && !messagegetset.getMedia_wa_type().equals("7")
                    && messagegetset.getSelf_des_time() != 0) {
                new Self_Destruct_Messages(context).setDestruct(""
                                + messagegetset.get_id(),
                        messagegetset.getSelf_des_time(),
                        messagegetset.getKey_remote_jid());
            }

            dbAdapter.setUpdateMessage_status(fromUser, packetID, 0);


            try {
                ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
                ActivityManager.RunningTaskInfo ar = RunningTask.get(0);

                if (ar.topActivity.getClassName().toString().equalsIgnoreCase("com.wifin.kachingme.chat.single_chat.ChatTest")) {

                } else if (ar.topActivity.getClassName().toString().equalsIgnoreCase("com.wifin.kachingme.chat.muc_chat.MUCTest")) {

                    for (int i = 0; i < MUCTest.msg_list.size(); i++) {

                        if (MUCTest.msg_list.get(i).getKey_id().equalsIgnoreCase(packetID)) {

                            MessageGetSet msg = new MessageGetSet();
                            msg.set_id(MUCTest.msg_list.get(i).get_id());
                            msg.setData(MUCTest.msg_list.get(i).getData());
                            msg.setKey_from_me(MUCTest.msg_list.get(i).getKey_from_me());
                            msg.setKey_id(MUCTest.msg_list.get(i).getKey_id());
                            msg.setKey_remote_jid(MUCTest.msg_list.get(i).getKey_remote_jid());
                            msg.setLatitude(MUCTest.msg_list.get(i).getLatitude());
                            msg.setLongitude(MUCTest.msg_list.get(i).getLongitude());
                            msg.setMedia_duration(MUCTest.msg_list.get(i).getMedia_duration());
                            msg.setMedia_hash(MUCTest.msg_list.get(i).getMedia_hash());
                            msg.setMedia_mime_type(MUCTest.msg_list.get(i).getMedia_mime_type());
                            msg.setMedia_name(MUCTest.msg_list.get(i).getMedia_name());
                            msg.setMedia_size(MUCTest.msg_list.get(i).getMedia_size());
                            msg.setMedia_url(MUCTest.msg_list.get(i).getMedia_url());
                            msg.setMedia_wa_type(MUCTest.msg_list.get(i).getMedia_wa_type());
                            msg.setNeeds_push(MUCTest.msg_list.get(i).getNeeds_push());
                            msg.setOrigin(MUCTest.msg_list.get(i).getOrigin());
                            msg.setReceipt_device_timestamp(MUCTest.msg_list.get(i).getReceipt_device_timestamp());
                            msg.setReceipt_server_timestamp(MUCTest.msg_list.get(i).getReceipt_server_timestamp());
                            msg.setReceived_timestamp(MUCTest.msg_list.get(i).getReceived_timestamp());
                            msg.setRemote_resource(MUCTest.msg_list.get(i).getRemote_resource());
                            msg.setRow_data(MUCTest.msg_list.get(i).getRow_data());
                            msg.setSend_timestamp(MUCTest.msg_list.get(i).getSend_timestamp());
                            msg.setStatus(0);
                            msg.setThumb_image(MUCTest.msg_list.get(i).getThumb_image());
                            msg.setTimestamp(MUCTest.msg_list.get(i).getTimestamp());
                            MUCTest.msg_list.set(i, msg);


                            if (Utils.isActivityIsFront(context, MUCTest.class.getCanonicalName())) {
                                Constant.printMsg("MUCCCCCCCC");
                                Intent login_broadcast = new Intent("update_tick");
                                login_broadcast.putExtra("position", "" + i);
                                login_broadcast.putExtra("status", "delivered");
                                context.getApplicationContext().sendBroadcast(login_broadcast);
                            }

                           else if (Utils.isActivityIsFront(context, SliderTesting.class.getCanonicalName())) {
                                Constant.printMsg("MUCCCCCCCC slider");
                                Intent login_broadcast = new Intent("lastseen_broadcast");
                                login_broadcast.putExtra("from",msg.getKey_remote_jid());
                                login_broadcast.putExtra("type", msg.getData());
                                context.getApplicationContext().sendBroadcast(login_broadcast);
                            }

                        }

                    }


                }
            } catch (Exception e) {

            }


        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void displayedNotification(String from, String packetID) {

        Constant.printMsg("HHHHHH  msg event listener seen");
        // TODO Auto-generated method stub
        String fromUser = from.toString().split("/")[0];
        // Constant.printMsg("Delivered Notification Received from::"+fromUser);
        Log.d("displayedNotification", "Message Display id::" + packetID);
        MessageGetSet messagegetset = dbAdapter.getMessages_by_key_id(packetID);
        if (messagegetset.getIs_sec_chat() == 0
                && !messagegetset.getMedia_wa_type().equals("7")
                && messagegetset.getSelf_des_time() != 0) {
            new Self_Destruct_Messages(context).setDestruct(
                    "" + messagegetset.get_id(),
                    messagegetset.getSelf_des_time(),
                    messagegetset.getKey_remote_jid());
        }

        dbAdapter.setUpdateMessage_status(fromUser, packetID, -1);
        Intent login_broadcast = new Intent("chat");
        login_broadcast.putExtra("jid", "" + fromUser);

        Constant.printMsg("notification called::>>> secret1 " + fromUser
                + "   " + packetID);

        context.getApplicationContext().sendBroadcast(login_broadcast);
    }

    @Override
    public void offlineNotification(String from, String packetID) {

        Constant.printMsg("HHHHHH  msg event listener ofline");
        // TODO Auto-generated method stub
        String fromUser = from.toString().split("/")[0];
        Log.d("Offline notification", fromUser + " PackateID::" + packetID);

        MessageGetSet msg = dbAdapter.getMessages_by_key_id(packetID);
        if (msg.getStatus() != 0) {
            dbAdapter.setUpdateMessage_status(from.split("/")[0], packetID, 2);
        }
        Intent login_broadcast = new Intent("chat");
        login_broadcast.putExtra("jid", "" + from.split("/")[0]);
        context.getApplicationContext().sendBroadcast(login_broadcast);

        Constant.printMsg("notification called111::>>> secret" + fromUser
                + "   " + packetID);

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

                } else if (status.equalsIgnoreCase(Constant.TYPING_STATUS_RECORDING)) {
                    login_broadcast.putExtra("type", Constant.TYPING_STATUS_RECORDING);
                } else {
                    login_broadcast.putExtra("type", "jid_status_from_presence");
                }

            } catch (Exception e) {
                Constant.printMsg("FFFFFFF split eee" + e.toString());
            }
        } else {
            login_broadcast.putExtra("type", "jid_status_from_presence");
        }
        context.sendBroadcast(login_broadcast);
    }

}
