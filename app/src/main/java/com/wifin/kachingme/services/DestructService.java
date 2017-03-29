package com.wifin.kachingme.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.util.Constant;

import java.util.Date;

/**
 * Created by comp on 10/25/2016.
 */
public class DestructService extends Service {

    private final static int NUM_PARTICLES = 25;
    private final static int FRAME_RATE = 30;
    private final static int LIFETIME = 300;
    static Handler handler = new Handler();
    static View view;
    Thread dest_thread;
    DatabaseHelper dbAdapter;
    Thread mAnimThread;
    int mAnimPos = 0;
    int mBroadPos = 0;

    boolean isThreadRun = false;
    int counter = 0;
  /*  public static Runnable mRunner = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(mRunner);
            view.invalidate();
        }
    };*/

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ChatTest.isThreadRun = false;

        Constant.printMsg("Call dest service on create ");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(dest_thread);
        handler.removeCallbacks(null);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


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
                                            if (ChatTest.IS_Front) {

                                                for (int i = 0; i < ChatTest.msg_list.size(); i++) {

                                                    String mTempMsgCheck = ChatTest.msg_list.get(i).getData();

                                                    if (mTempMsgCheck != null || !mTempMsgCheck.equalsIgnoreCase("null")) {

                                                        if (mTempMsgCheck.length() > 3) {

                                                            char sa = mTempMsgCheck.charAt(0);
                                                            char s1a = mTempMsgCheck.charAt(1);
                                                            char s2a = mTempMsgCheck.charAt(2);

                                                            if (sa == '<' && s1a == 's' && s2a == '>') {

                                                                String self_destructa = mTempMsgCheck.substring(3)
                                                                        .toString();
                                                                String[] partsa = self_destructa.split("-");
                                                                String part1a = partsa[0];
                                                                String part2a = partsa[1];
                                                                String part3a = partsa[2];

                                                                if (part2.equalsIgnoreCase(part2a)) {
                                                                    MessageGetSet msg = new MessageGetSet();
                                                                    msg.set_id(ChatTest.msg_list.get(i).get_id());
                                                                    msg.setData(changedDestValue);
                                                                    msg.setKey_from_me(ChatTest.msg_list.get(i).getKey_from_me());
                                                                    msg.setKey_id(ChatTest.msg_list.get(i).getKey_id());
                                                                    msg.setKey_remote_jid(ChatTest.msg_list.get(i).getKey_remote_jid());
                                                                    msg.setLatitude(ChatTest.msg_list.get(i).getLatitude());
                                                                    msg.setLongitude(ChatTest.msg_list.get(i).getLongitude());
                                                                    msg.setMedia_duration(ChatTest.msg_list.get(i).getMedia_duration());
                                                                    msg.setMedia_hash(ChatTest.msg_list.get(i).getMedia_hash());
                                                                    msg.setMedia_mime_type(ChatTest.msg_list.get(i).getMedia_mime_type());
                                                                    msg.setMedia_name(ChatTest.msg_list.get(i).getMedia_name());
                                                                    msg.setMedia_size(ChatTest.msg_list.get(i).getMedia_size());
                                                                    msg.setMedia_url(ChatTest.msg_list.get(i).getMedia_url());
                                                                    msg.setMedia_wa_type(ChatTest.msg_list.get(i).getMedia_wa_type());
                                                                    msg.setNeeds_push(ChatTest.msg_list.get(i).getNeeds_push());
                                                                    msg.setOrigin(ChatTest.msg_list.get(i).getOrigin());
                                                                    msg.setReceipt_device_timestamp(ChatTest.msg_list.get(i).getReceipt_device_timestamp());
                                                                    msg.setReceipt_server_timestamp(ChatTest.msg_list.get(i).getReceipt_server_timestamp());
                                                                    msg.setReceived_timestamp(ChatTest.msg_list.get(i).getReceived_timestamp());
                                                                    msg.setRemote_resource(ChatTest.msg_list.get(i).getRemote_resource());
                                                                    msg.setRow_data(ChatTest.msg_list.get(i).getRow_data());
                                                                    msg.setSend_timestamp(ChatTest.msg_list.get(i).getSend_timestamp());
                                                                    msg.setStatus(ChatTest.msg_list.get(i).getStatus());
                                                                    msg.setThumb_image(ChatTest.msg_list.get(i).getThumb_image());
                                                                    msg.setTimestamp(ChatTest.msg_list.get(i).getTimestamp());
                                                                    msg.setPostion(ChatTest.msg_list.get(i).getPosition());
                                                                    ChatTest.msg_list.set(i, msg);
                                                                    mBroadPos = i;
                                                                    Constant.printMsg("sdjihildfjdi" + String.valueOf(ChatTest.msg_list.get(i).getPosition()));

                                                                    break;
                                                                }
                                                            }

                                                        }


                                                    }


                                                }

                                            }


                                        } catch (Exception e) {

                                        }


                                        Constant.printMsg("DEEEEESSSSSTTTTT3 " + mBroadPos);
                                        Intent login_broadcast;
                                        login_broadcast = new Intent("destruct_time");
                                        login_broadcast.putExtra("position", String.valueOf(mBroadPos));
                                        login_broadcast.putExtra("time", String.valueOf(temp_time));
                                        login_broadcast.putExtra("jid", String.valueOf(ChatTest.msg_list.get(mBroadPos).getKey_remote_jid()));
                                        sendBroadcast(login_broadcast);
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



                                            if (dbAdapter.getMessegeCount(part3, 1) < 1) {

                                                updateForHomeScreenList(part3);
                                            }

                                            int msg_id = dbAdapter.getLastMsgid_chat(part3, 1);



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


        return super.onStartCommand(intent, flags, startId);

    }

    public void mAnimation(final int pos) {


        mAnimThread = new Thread(new Runnable() {
            @Override
            public void run() {

                Intent login_broadcast;

                try {

                    switch ((Integer) ChatTest.dest_list_anim.get(pos)) {


                        case 0:

                            ChatTest.dest_list_bombids.set(pos, R.drawable.b1);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 1:
                            ChatTest.dest_list_bombids.set(pos, R.drawable.b2);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 2:
                            ChatTest.dest_list_bombids.set(pos, R.drawable.b3);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 3:
                            ChatTest.dest_list_bombids.set(pos, R.drawable.b4);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 4:
                            ChatTest.dest_list_bombids.set(pos, R.drawable.b5);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 5:
                            ChatTest.dest_list_bombids.set(pos, R.drawable.b6);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 6:
                            ChatTest.dest_list_bombids.set(pos, R.drawable.b7);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 7:
                            ChatTest.dest_list_bombids.set(pos, R.drawable.b8);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 8:
                            ChatTest.dest_list_bombids.set(pos, R.drawable.b9);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 9:
                            ChatTest.dest_list_bombids.set(pos, R.drawable.b10);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 10:
                            ChatTest.dest_list_bombids.set(pos, R.drawable.b11);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 11:
                            ChatTest.dest_list_bombids.set(pos, R.drawable.b12);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        default:
                            break;

                    }

                    int var = (Integer) ChatTest.dest_list_anim.get(pos);
                    var++;

                    ChatTest.dest_list_anim.set(pos, var);
                    if (var > 11) {

                        handler.removeCallbacks(mAnimThread);

                    } else {
                        handler.postDelayed(mAnimThread, 82);

                    }
                } catch (IndexOutOfBoundsException e1) {

                } catch (Exception
                        e) {

                }

            }
        });

        handler.postDelayed(mAnimThread, 0);

    }


    public void updateForHomeScreenList(String jid) {


        MessageGetSet msggetset1 = new MessageGetSet();


        msggetset1.setData("");
        msggetset1.setKey_from_me(0);
        msggetset1.setKey_id(""+ new Date().getTime());
        msggetset1.setKey_remote_jid(jid);
        msggetset1.setNeeds_push(1);
        msggetset1.setSend_timestamp(new Date().getTime());
        msggetset1.setStatus(5);
        msggetset1.setTimestamp(new Date().getTime());
        msggetset1.setMedia_wa_type("40");
        msggetset1.setIs_sec_chat(1);
        msggetset1.setSelf_des_time(0);
        msggetset1.setIs_owner(0);


        long l = dbAdapter.setInsertMessages(msggetset1);


    }

}
