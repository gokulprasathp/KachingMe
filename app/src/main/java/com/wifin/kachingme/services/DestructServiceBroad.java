package com.wifin.kachingme.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat.broadcast_chat.BroadCastTest;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.util.Constant;

/**
 * Created by comp on 10/25/2016.
 */
public class DestructServiceBroad extends Service {

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

        BroadCastTest.isThreadRun = false;

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


                for (int k = 0; k < BroadCastTest.dest_list_msgids.size(); k++) {

                    try {
                        String mDaestMessege = dbAdapter.getMessageDataById(BroadCastTest.dest_list_msgids.get(k).toString());

                        if (mDaestMessege != null || !mDaestMessege.equalsIgnoreCase("null")) {
                            Constant.printMsg("MMMMMMMMMMMMMMMDESTasdasdadas" + mDaestMessege + "    " + k + "      " + BroadCastTest.dest_list_msgids.size() + "     " + BroadCastTest.msg_list.size());


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
                                        dbAdapter.setUpdateDestMsgData(BroadCastTest.dest_list_msgids.get(k).toString(), changedDestValue);

                                        try {
                                            if (BroadCastTest.IS_Front) {

                                                for (int i = 0; i < BroadCastTest.msg_list.size(); i++) {

                                                    String mTempMsgCheck = BroadCastTest.msg_list.get(i).getData();

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
                                                                    msg.set_id(BroadCastTest.msg_list.get(i).get_id());
                                                                    msg.setData(changedDestValue);
                                                                    msg.setKey_from_me(BroadCastTest.msg_list.get(i).getKey_from_me());
                                                                    msg.setKey_id(BroadCastTest.msg_list.get(i).getKey_id());
                                                                    msg.setKey_remote_jid(BroadCastTest.msg_list.get(i).getKey_remote_jid());
                                                                    msg.setLatitude(BroadCastTest.msg_list.get(i).getLatitude());
                                                                    msg.setLongitude(BroadCastTest.msg_list.get(i).getLongitude());
                                                                    msg.setMedia_duration(BroadCastTest.msg_list.get(i).getMedia_duration());
                                                                    msg.setMedia_hash(BroadCastTest.msg_list.get(i).getMedia_hash());
                                                                    msg.setMedia_mime_type(BroadCastTest.msg_list.get(i).getMedia_mime_type());
                                                                    msg.setMedia_name(BroadCastTest.msg_list.get(i).getMedia_name());
                                                                    msg.setMedia_size(BroadCastTest.msg_list.get(i).getMedia_size());
                                                                    msg.setMedia_url(BroadCastTest.msg_list.get(i).getMedia_url());
                                                                    msg.setMedia_wa_type(BroadCastTest.msg_list.get(i).getMedia_wa_type());
                                                                    msg.setNeeds_push(BroadCastTest.msg_list.get(i).getNeeds_push());
                                                                    msg.setOrigin(BroadCastTest.msg_list.get(i).getOrigin());
                                                                    msg.setReceipt_device_timestamp(BroadCastTest.msg_list.get(i).getReceipt_device_timestamp());
                                                                    msg.setReceipt_server_timestamp(BroadCastTest.msg_list.get(i).getReceipt_server_timestamp());
                                                                    msg.setReceived_timestamp(BroadCastTest.msg_list.get(i).getReceived_timestamp());
                                                                    msg.setRemote_resource(BroadCastTest.msg_list.get(i).getRemote_resource());
                                                                    msg.setRow_data(BroadCastTest.msg_list.get(i).getRow_data());
                                                                    msg.setSend_timestamp(BroadCastTest.msg_list.get(i).getSend_timestamp());
                                                                    msg.setStatus(BroadCastTest.msg_list.get(i).getStatus());
                                                                    msg.setThumb_image(BroadCastTest.msg_list.get(i).getThumb_image());
                                                                    msg.setTimestamp(BroadCastTest.msg_list.get(i).getTimestamp());
                                                                    msg.setPostion(BroadCastTest.msg_list.get(i).getPosition());
                                                                    BroadCastTest.msg_list.set(i, msg);
                                                                    mBroadPos = i;
                                                                    Constant.printMsg("sdjihildfjdi" + String.valueOf(BroadCastTest.msg_list.get(i).getPosition()));

                                                                    break;
                                                                }
                                                            }

                                                        }


                                                    }


                                                }

                                            }


                                        } catch (Exception e) {

                                        }


                                        Constant.printMsg("DEEEEESSSSSTTTTT3 " + String.valueOf(BroadCastTest.msg_list.get(mBroadPos).getPosition()));
                                        Intent login_broadcast;
                                        login_broadcast = new Intent("destruct_time");
                                        login_broadcast.putExtra("position", String.valueOf(mBroadPos));
                                        login_broadcast.putExtra("time", String.valueOf(temp_time));
                                        login_broadcast.putExtra("jid", String.valueOf(BroadCastTest.msg_list.get(mBroadPos).getKey_remote_jid()));
                                        sendBroadcast(login_broadcast);
                                        if (temp_time == 1) {

                                            // mAnimation(k);

                                        }

                                        if (temp_time == 0) {

//                                            if (Utils.isActivityIsFront(DestructService.this, BroadCastTest.class.getCanonicalName().toString())) {
//                                                Intent login_broadcast;
//                                                login_broadcast = new Intent("destruct_time");
//                                                login_broadcast.putExtra("position", String.valueOf(k));
//                                                login_broadcast.putExtra("time", String.valueOf(temp_time));
//                                                sendBroadcast(login_broadcast);
//                                            }

                                            dbAdapter.setDeleteMessages_by_msgid(BroadCastTest.dest_list_msgids.get(k).toString());
                                            int msg_id = dbAdapter.getLastMsgid_chat(part3, 1);
                                            Constant.printMsg("id::::::::::::" + msg_id);
                                            if (dbAdapter.isExistinChatList_chat(part3, 1)) {
                                                dbAdapter.setUpdateChat_lits_chat(part3, msg_id, 1);
                                            } else {
                                                dbAdapter.setInsertChat_list_chat(part3, msg_id, 1);
                                            }

//                                            try {
//                                                BroadCastTest.msg_list.remove(k);
//
//                                            } catch (Exception e) {
//
//                                            }

//                                            BroadCastTest.dest_list_msgids.remove(k);
//                                            BroadCastTest.dest_list_bombids.remove(k);
                                            BroadCastTest.dest_list.set(k, 0);
//                                            BroadCastTest.dest_list_anim.remove(k);

                                            boolean destAvailable = true;
                                            for (int j = 0; j < BroadCastTest.dest_list.size(); j++) {
                                                if (Integer.valueOf(BroadCastTest.dest_list.get(j).toString()) != 0) {
                                                    destAvailable = false;
                                                }

                                            }

                                            if (destAvailable == true) {
                                                Constant.printMsg("Call dest service stop" + BroadCastTest.dest_list_msgids);
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


//                    BroadCastTest.msg_list.remove(BroadCastTest.msg_list.size()-1);
//                    BroadCastTest.adaptor.notifyDataSetChanged();


//                    if(BroadCastTest.isNotifyDataChanged)
//                        BroadCastTest.isNotifyDataChanged = false;
//                    else
//                        BroadCastTest.isNotifyDataChanged = true;

//                if (BroadCastTest.adaptor != null) {
//                    BroadCastTest.adaptor.notifyDataSetChanged();
//                } else {
//                    Constant.printMsg("ELSE DEST");
//                }

                try {
                    handler.postDelayed(dest_thread, 1000);

                } catch (Exception e) {

                }













/*

                    for (int i = 0; i < BroadCastTest.dest_list.size(); i++) {
                        int temp_time = Integer.valueOf(BroadCastTest.dest_list.get(i).toString());
                        if (temp_time > 0) {
                            temp_time = temp_time - 1;
                            BroadCastTest.dest_list.set(i, temp_time);

                            if (temp_time == 1) {

                                mAnimation(i);

                            }

                            if (temp_time == 0) {

                                long del_id = dbAdapter.setDeleteMessages_by_msgid(BroadCastTest.dest_list_msgids.get(i).toString());
                                Constant.printMsg("Call dest service 222" + i + BroadCastTest.dest_list_msgids + BroadCastTest.dest_list);
                                int msg_id = dbAdapter.getLastMsgid_chat(Constant.jid, 1);
                                Constant.printMsg("id::::::::::::" + msg_id);
                                if (dbAdapter.isExistinChatList_chat(Constant.jid, 1)) {
                                    dbAdapter.setUpdateChat_lits_chat(Constant.jid, msg_id, 1);
                                } else {
                                    dbAdapter.setInsertChat_list_chat(Constant.jid, msg_id, 1);
                                }
                                BroadCastTest.dest_list_msgids.remove(i);
                                BroadCastTest.dest_list_bombids.remove(i);
                                BroadCastTest.dest_list.remove(i);
                                BroadCastTest.dest_list_anim.remove(i);

                                Constant.printMsg("Call dest service 333" + i + BroadCastTest.dest_list_msgids + BroadCastTest.dest_list);

                                Intent login_broadcast;
                                login_broadcast = new Intent("destruct_service");
                                sendBroadcast(login_broadcast);


                                boolean destAvailable = true;
                                for (int j = 0; j < BroadCastTest.dest_list.size(); j++) {
                                    if (Integer.valueOf(BroadCastTest.dest_list.get(j).toString()) != 0) {
                                        destAvailable = false;
                                    }

                                }

                                if (destAvailable == true) {
                                    Constant.printMsg("Call dest service stop" + BroadCastTest.dest_list_msgids);
                                    BroadCastTest.handler.removeCallbacks(dest_thread);
                                    stopSelf();
                                }

//                            if(BroadCastTest.adaptor!=null)
//                                BroadCastTest.adaptor.notifyDataSetChanged();


                            }


                        }

                    }*/


            }
        });

        if (BroadCastTest.isThreadRun == false) {
            BroadCastTest.isThreadRun = true;
            Constant.printMsg("Call dest service first time " + BroadCastTest.isThreadRun);
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

                    switch ((Integer) BroadCastTest.dest_list_anim.get(pos)) {


                        case 0:

                            BroadCastTest.dest_list_bombids.set(pos, R.drawable.b1);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 1:
                            BroadCastTest.dest_list_bombids.set(pos, R.drawable.b2);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 2:
                            BroadCastTest.dest_list_bombids.set(pos, R.drawable.b3);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 3:
                            BroadCastTest.dest_list_bombids.set(pos, R.drawable.b4);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 4:
                            BroadCastTest.dest_list_bombids.set(pos, R.drawable.b5);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 5:
                            BroadCastTest.dest_list_bombids.set(pos, R.drawable.b6);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 6:
                            BroadCastTest.dest_list_bombids.set(pos, R.drawable.b7);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 7:
                            BroadCastTest.dest_list_bombids.set(pos, R.drawable.b8);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 8:
                            BroadCastTest.dest_list_bombids.set(pos, R.drawable.b9);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 9:
                            BroadCastTest.dest_list_bombids.set(pos, R.drawable.b10);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 10:
                            BroadCastTest.dest_list_bombids.set(pos, R.drawable.b11);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        case 11:
                            BroadCastTest.dest_list_bombids.set(pos, R.drawable.b12);
                            login_broadcast = new Intent("chat");
                            sendBroadcast(login_broadcast);

                            break;
                        default:
                            break;

                    }

                    int var = (Integer) BroadCastTest.dest_list_anim.get(pos);
                    var++;

                    BroadCastTest.dest_list_anim.set(pos, var);
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


}
