package com.wifin.kachingme.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Utils;

/**
 * Created by Wifin on 06-03-2017.
 */
public class DestuctServiceIntent extends IntentService {

    private final static int NUM_PARTICLES = 25;
    private final static int FRAME_RATE = 30;
    private final static int LIFETIME = 300;
    static Handler handler = new Handler();
    static View view;
    Thread dest_thread;
    DatabaseHelper dbAdapter;
    Thread mAnimThread;

    public DestuctServiceIntent()
    {
        super("DestuctServiceIntent");
    }

    @Override
    protected void onHandleIntent(Intent intent) {{


//        TimerTask timerTask = new TimerTask() {
//
//            @Override
//            public void run() {
//                System.out.println("TimerTask executing counter is: " + counter);
//                counter++;//increments the counter
//            }
//        };
//
//        Timer timer = new Timer("MyTimer");//create a new Timer
//
//        if(timer!=null){
//            timer.cancel();
//            timer.scheduleAtFixedRate(timerTask, 0, 1000);
//
//        }


        dbAdapter = KachingMeApplication.getDatabaseAdapter();


        dest_thread = new Thread(new Runnable() {
            @Override
            public void run() {


                for (int k = 0; k < ChatTest.dest_list_msgids.size(); k++) {

                    try {
                        String mDaestMessege = dbAdapter.getMessageDataById(ChatTest.dest_list_msgids.get(k).toString());

                        if (mDaestMessege != null || !mDaestMessege.equalsIgnoreCase("null")) {
                            Constant.printMsg("MMMMMMMMMMMMMMMDESTasdasdadas" + mDaestMessege + "    " + k + "      " + ChatTest.dest_list_msgids.size() + "     " + ChatTest.msg_list.size());


                            if (mDaestMessege.length() > 3) {

                                char s = mDaestMessege.charAt(0);
                                char s1 = mDaestMessege.charAt(1);
                                char s2 = mDaestMessege.charAt(2);

                                if (s == '<' && s1 == 's' && s2 == '>') {


                                    String self_destruct = mDaestMessege.substring(3)
                                            .toString();
                                    String[] parts = self_destruct.split("-");
                                    String part1 = parts[0];
                                    String part2 = parts[1];
                                    String part3 = parts[2];
                                    Constant.printMsg("MMMMMMMMMMMMMMMDEST11111111" + part1 + "     " + part2);
                                    int temp_time = Integer.valueOf(part1);
                                    if (temp_time > 0) {
                                        temp_time = temp_time - 1;


                                        String changedDestValue = "<s>" + temp_time + "-" + part2 + "-" + part3;
                                        dbAdapter.setUpdateDestMsgData(ChatTest.dest_list_msgids.get(k).toString(), changedDestValue);

                                        try {


                                            if (Utils.isActivityIsFront(DestuctServiceIntent.this, ChatTest.class.getCanonicalName().toString())) {
                                                MessageGetSet msg = new MessageGetSet();
                                                msg.set_id(ChatTest.msg_list.get(k).get_id());
                                                msg.setData(changedDestValue);
                                                msg.setKey_from_me(ChatTest.msg_list.get(k).getKey_from_me());
                                                msg.setKey_id(ChatTest.msg_list.get(k).getKey_id());
                                                msg.setKey_remote_jid(ChatTest.msg_list.get(k).getKey_remote_jid());
                                                msg.setLatitude(ChatTest.msg_list.get(k).getLatitude());
                                                msg.setLongitude(ChatTest.msg_list.get(k).getLongitude());
                                                msg.setMedia_duration(ChatTest.msg_list.get(k).getMedia_duration());
                                                msg.setMedia_hash(ChatTest.msg_list.get(k).getMedia_hash());
                                                msg.setMedia_mime_type(ChatTest.msg_list.get(k).getMedia_mime_type());
                                                msg.setMedia_name(ChatTest.msg_list.get(k).getMedia_name());
                                                msg.setMedia_size(ChatTest.msg_list.get(k).getMedia_size());
                                                msg.setMedia_url(ChatTest.msg_list.get(k).getMedia_url());
                                                msg.setMedia_wa_type(ChatTest.msg_list.get(k).getMedia_wa_type());
                                                msg.setNeeds_push(ChatTest.msg_list.get(k).getNeeds_push());
                                                msg.setOrigin(ChatTest.msg_list.get(k).getOrigin());
                                                msg.setReceipt_device_timestamp(ChatTest.msg_list.get(k).getReceipt_device_timestamp());
                                                msg.setReceipt_server_timestamp(ChatTest.msg_list.get(k).getReceipt_server_timestamp());
                                                msg.setReceived_timestamp(ChatTest.msg_list.get(k).getReceived_timestamp());
                                                msg.setRemote_resource(ChatTest.msg_list.get(k).getRemote_resource());
                                                msg.setRow_data(ChatTest.msg_list.get(k).getRow_data());
                                                msg.setSend_timestamp(ChatTest.msg_list.get(k).getSend_timestamp());
                                                msg.setStatus(ChatTest.msg_list.get(k).getStatus());
                                                msg.setThumb_image(ChatTest.msg_list.get(k).getThumb_image());
                                                msg.setTimestamp(ChatTest.msg_list.get(k).getTimestamp());
                                                msg.setPostion(ChatTest.msg_list.get(k).getPosition());
                                                ChatTest.msg_list.set(k, msg);

                                                Constant.printMsg("MMMMMMMMMMMMMMM CCCCCCCC" + String.valueOf(ChatTest.msg_list.get(k).getPosition()) + "     " + String.valueOf(temp_time));

                                                Intent login_broadcast;
                                                login_broadcast = new Intent("destruct_time");
                                                login_broadcast.putExtra("position", String.valueOf(ChatTest.msg_list.get(k).getPosition()));
                                                login_broadcast.putExtra("time", String.valueOf(temp_time));
                                                sendBroadcast(login_broadcast);
                                            }
                                        } catch (Exception e) {

                                        }

                                        if (temp_time == 1) {

                                            // mAnimation(k);

                                        }

                                        if (temp_time == 0) {

//                                            if (Utils.isActivityIsFront(DestructService.this, ChatTest.class.getCanonicalName().toString())) {
//                                                Intent login_broadcast;
//                                                login_broadcast = new Intent("destruct_time");
//                                                login_broadcast.putExtra("position", String.valueOf(k));
//                                                login_broadcast.putExtra("time", String.valueOf(temp_time));
//                                                sendBroadcast(login_broadcast);
//                                            }

                                            dbAdapter.setDeleteMessages_by_msgid(ChatTest.dest_list_msgids.get(k).toString());
                                            int msg_id = dbAdapter.getLastMsgid_chat(part3, 1);
                                            Constant.printMsg("id::::::::::::" + msg_id);
                                            if (dbAdapter.isExistinChatList_chat(part3, 1)) {
                                                dbAdapter.setUpdateChat_lits_chat(part3, msg_id, 1);
                                            } else {
                                                dbAdapter.setInsertChat_list_chat(part3, msg_id, 1);
                                            }

//                                            try {
//                                                ChatTest.msg_list.remove(k);
//
//                                            } catch (Exception e) {
//
//                                            }

//                                            ChatTest.dest_list_msgids.remove(k);
//                                            ChatTest.dest_list_bombids.remove(k);
                                            ChatTest.dest_list.set(k, 0);
//                                            ChatTest.dest_list_anim.remove(k);

                                            boolean destAvailable = true;
                                            for (int j = 0; j < ChatTest.dest_list.size(); j++) {
                                                if (Integer.valueOf(ChatTest.dest_list.get(j).toString()) != 0) {
                                                    destAvailable = false;
                                                }

                                            }

                                            if (destAvailable == true) {
                                                Constant.printMsg("Call dest service stop" + ChatTest.dest_list_msgids);
                                                handler.removeCallbacks(dest_thread);

                                                Intent login_broadcast;
                                                login_broadcast = new Intent("destruct_service");
                                                sendBroadcast(login_broadcast);

                                            }

                                        }
                                    }


                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


//                    ChatTest.msg_list.remove(ChatTest.msg_list.size()-1);
//                    ChatTest.adaptor.notifyDataSetChanged();


//                    if(ChatTest.isNotifyDataChanged)
//                        ChatTest.isNotifyDataChanged = false;
//                    else
//                        ChatTest.isNotifyDataChanged = true;

//                if (ChatTest.adaptor != null) {
//                    ChatTest.adaptor.notifyDataSetChanged();
//                } else {
//                    Constant.printMsg("ELSE DEST");
//                }

                try {
                    handler.postDelayed(dest_thread, 1000);

                } catch (Exception e) {

                }













/*

                    for (int i = 0; i < ChatTest.dest_list.size(); i++) {
                        int temp_time = Integer.valueOf(ChatTest.dest_list.get(i).toString());
                        if (temp_time > 0) {
                            temp_time = temp_time - 1;
                            ChatTest.dest_list.set(i, temp_time);

                            if (temp_time == 1) {

                                mAnimation(i);

                            }

                            if (temp_time == 0) {

                                long del_id = dbAdapter.setDeleteMessages_by_msgid(ChatTest.dest_list_msgids.get(i).toString());
                                Constant.printMsg("Call dest service 222" + i + ChatTest.dest_list_msgids + ChatTest.dest_list);
                                int msg_id = dbAdapter.getLastMsgid_chat(Constant.jid, 1);
                                Constant.printMsg("id::::::::::::" + msg_id);
                                if (dbAdapter.isExistinChatList_chat(Constant.jid, 1)) {
                                    dbAdapter.setUpdateChat_lits_chat(Constant.jid, msg_id, 1);
                                } else {
                                    dbAdapter.setInsertChat_list_chat(Constant.jid, msg_id, 1);
                                }
                                ChatTest.dest_list_msgids.remove(i);
                                ChatTest.dest_list_bombids.remove(i);
                                ChatTest.dest_list.remove(i);
                                ChatTest.dest_list_anim.remove(i);

                                Constant.printMsg("Call dest service 333" + i + ChatTest.dest_list_msgids + ChatTest.dest_list);

                                Intent login_broadcast;
                                login_broadcast = new Intent("destruct_service");
                                sendBroadcast(login_broadcast);


                                boolean destAvailable = true;
                                for (int j = 0; j < ChatTest.dest_list.size(); j++) {
                                    if (Integer.valueOf(ChatTest.dest_list.get(j).toString()) != 0) {
                                        destAvailable = false;
                                    }

                                }

                                if (destAvailable == true) {
                                    Constant.printMsg("Call dest service stop" + ChatTest.dest_list_msgids);
                                    ChatTest.handler.removeCallbacks(dest_thread);
                                    stopSelf();
                                }

//                            if(ChatTest.adaptor!=null)
//                                ChatTest.adaptor.notifyDataSetChanged();


                            }


                        }

                    }*/


            }
        });

        if (ChatTest.isThreadRun == false) {
            ChatTest.isThreadRun = true;
            Constant.printMsg("Call dest service first time " + ChatTest.isThreadRun);
            handler.post(dest_thread);

        }



    }

    }
}
