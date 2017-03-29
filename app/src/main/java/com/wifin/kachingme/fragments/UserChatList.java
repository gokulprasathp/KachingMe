package com.wifin.kachingme.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.async_tasks.ConcurrentAsyncTaskExecutor;
import com.wifin.kachingme.chat.chat_adaptors.UserChatListAdapter;
import com.wifin.kachingme.chat.muc_chat.MUCTest;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.Chat_list_home_GetSet;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.settings.UserProfile;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;

import org.jivesoftware.smack.chat.ChatManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UserChatList extends Fragment {
    public static String TAG = UserChatList.class.getSimpleName();
    public static RecyclerView recyclerViewChat;
    public static UserChatListAdapter adapter;
    public static String isChatTypingJid = null;
    public static String isChatTypingStatus = null;
    public static ArrayList<Bitmap> mProfileImagesList = new ArrayList<Bitmap>();
    public static ArrayList<Bitmap> mTempProfileImagesList = new ArrayList<Bitmap>();
    public static int mPosition;
    public static ArrayList<Chat_list_home_GetSet> tempchatlist = new ArrayList<Chat_list_home_GetSet>();
    public ChatManager chatManager;
    public org.jivesoftware.smack.chat.Chat chat;
    Cursor cursor;
    DatabaseHelper dbAdapter;
    SharedPreferences sp;
    Editor ed;
    Resources res;
    String Normallist = "";
    Dbhelper db;
    String status_lock = "check";
    SpannableStringBuilder ssb;
    int idx1;
    int idx2;
    boolean mRunTempFetchChat = true;
    BroadcastReceiver lastseen_event = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Constant.printMsg("siva called  Broadcast  ChatList");
            Log.d("ChatList", "Chat List Broadcast called...."
                    + intent.getAction().toString());
            if (intent.getAction().equals("update_profile")
                    ) {
                Constant.printMsg("asdhashdasd");
                String[] val = {null};
                ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);

            } else if (intent.getAction().equals("group_list")
                    ) {
                Constant.printMsg("asdhashdasd");
                String[] val = {null};
                ConcurrentAsyncTaskExecutor.executeConcurrently(new TempFetchChat(), val);

            } else if (intent.getAction().equals("chat")
                    || intent.getAction().equals(Constant.PROFILE_UPDATE)
                    || intent.getAction().equals("chat_1")) {

//                String[] val = {null};
//                ConcurrentAsyncTaskExecutor.executeConcurrently(new TempFetchChat(), val);

            } else if (intent.getAction().equals("lastseen_broadcast")) {

                Constant.printMsg("Chat List typing status :");


                String from = intent.getStringExtra("from");
                String type = intent.getStringExtra("type");

                try {

                    for (int i = 0; i < Constant.chatlist.size(); i++) {

                        Constant.printMsg("siva called  Broadcast  ChatListgdgsdg 111 " + from + "  " + Constant.chatlist.get(i).getJidId());

                        if (Constant.chatlist.get(i).getJidId().equalsIgnoreCase(from)) {
                            Constant.chatlist.get(i).setData(type);

                            Constant.printMsg("siva called  Broadcast  ChatListgdgsdg");

                            UserChatList.this.adapter.notifyDataSetChanged();
                            break;
                        }

                    }
                    pauseTypingThread();
                } catch (Exception e) {

                }


//                if (from != null) {
//                    if (type != null) {
//                        if (type.equalsIgnoreCase(Constant.TYPING_STRING)) {
//
//                            Constant.printMsg("Chat List typing status 1:" + from);
//
//                            isChatTypingJid = from;
//                            isChatTypingStatus = "typing...";
//
//                            String[] val = {null};
//                            ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);
//
//                            pauseTypingThread();
//
//
//                        } else if (type
//                                .equalsIgnoreCase(Constant.TYPING_STATUS_RECORDING)) {
//
//                            isChatTypingJid = from;
//                            isChatTypingStatus = "recording...";
//
//                            String[] val = {null};
//                            ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);
//
//                            pauseTypingThread();
//
//                        } else if (type
//                                .equalsIgnoreCase("jid_status_from_presence")) {
//                        }
//
//                    }
//
//                }
//            } else if (intent.getAction().equals("start_typing")) {
//
//
//                String jid2 = intent.getStringExtra("jid");
//                String jid_grp = intent.getStringExtra("jid_grp");
//                Constant.printMsg("Chat List typing status 3:" + jid_grp);
//                if (jid2 != null) {
//
//                    if (jid2.toString().split("@")[0] != null) {
//                        // typing or recording
//                        String type = null;
//                        if (intent.getStringExtra("type").equalsIgnoreCase(
//                                Constant.TYPING_STATUS_GROUP)) {
//                            type = " typing...";
//                        } else {
//                            type = " recording...";
//                        }
//
//                        ContactsGetSet contact = dbAdapter
//                                .getContact(jid2);
//
//                        if (contact.getDisplay_name() != null) {
//                            isChatTypingJid = jid_grp;
//                            isChatTypingStatus = contact.getDisplay_name() + " is " + type;
//
//
//                            String[] val = {null};
//                            ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);
//
//                            pauseTypingThread();
//
//                        } else if (contact.getNumber() != null) {
//                            isChatTypingJid = jid_grp;
//                            isChatTypingStatus = contact.getNumber() + " is " + type;
//
//
//                            String[] val = {null};
//                            ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);
//                            pauseTypingThread();
//                        } else {
//                            isChatTypingJid = jid_grp;
//                            isChatTypingStatus = jid2.toString().split("@")[0] + " is " + type;
//
//                            String[] val = {null};
//                            ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);
//
//                            pauseTypingThread();
//
//                        }
//
//                    }
//                }
//            }
            }
        }

    };

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contactlist, container, false);
        setHasOptionsMenu(true);
        db = new Dbhelper(getActivity());
        Constant.printMsg("siva called onCreateView Chat.............................");
        Constant.ref = false;
        Constant.mBuxDonated = false;
        dbAdapter = KachingMeApplication.getDatabaseAdapter();
        recyclerViewChat = (RecyclerView) v.findViewById(R.id.chatList_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerViewChat.setLayoutManager(linearLayoutManager);
        recyclerViewChat.setHasFixedSize(true);
        res = getResources();
        sp = getActivity().getSharedPreferences(KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
        ed = sp.edit();
        Constant.printMsg("siva called onCreateView Chat.........list size...................." + Constant.chatlist.size());
//        if (Constant.chatlist.size() == 0) {
//        String[] val = {null};
//        mProfileImagesList = new ArrayList<Bitmap>();
//        ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);
//        } else {
//            mProfileImagesList = new ArrayList<Bitmap>();
//            adapter = new UserChatListAdapter(getActivity(), Constant.chatlist, false, "chat");
//            recyclerViewChat.setAdapter(adapter);
//        }
        UserChatList.mProfileImagesList = UserChatList.mTempProfileImagesList;

        SliderTesting.mHeaderEditText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                SliderTesting.mHeaderEditText.requestFocus();
                SliderTesting.mHeaderEditText.setFocusable(true);
                SliderTesting.mHeaderEditText.setFocusableInTouchMode(true);
                return false;
            }
        });

        SliderTesting.mHeaderEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                Constant.printMsg("Siva testttttttttttttttttttttttt.." + cs);
                if (cs.length() > 0) {
                    SliderTesting.mHeaderClose.setVisibility(View.VISIBLE);
                    getSearchText(cs.toString().trim());
                } else {
                    SliderTesting.mHeaderClose.setVisibility(View.GONE);
                    getSearchText(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        return v;
    }

    public void getSearchText(String ch) {
        //new FetchChat().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, ch);
        ConcurrentAsyncTaskExecutor.executeConcurrently(new SearchChat(), ch);
    }

    public void pauseTypingThread() {

        if (mRunTempFetchChat == true) {
            mRunTempFetchChat = false;
            Thread typingStatusHold = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(1000);
                        isChatTypingJid = null;
                        isChatTypingStatus = null;
                        String[] val = {null};
                        ConcurrentAsyncTaskExecutor.executeConcurrently(new TempFetchChat(), val);
//                    new FetchChat().execute(val);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            typingStatusHold.start();
        }

    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        // TODO Auto-generated method stub
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        int menuItemIndex = item.getItemId();
        Constant.printMsg("userchatListitem::::>>>>>>>>>   " + menuItemIndex);
//        cursor.moveToPosition(info.position);
        /* Log.d("Context Menu",cursor.getString(8)); */


        if (Constant.mSlectedActivity.equalsIgnoreCase("chat")) {
            if (menuItemIndex == 10) {
                Constant.printMsg("userchatListjid::::>>>>>>>>>   "
                        + Constant.mJid);
                Intent intent = new Intent(getActivity(), UserProfile.class);
                intent.putExtra("jid", Constant.mJid);

                startActivity(intent);
            } else if (menuItemIndex == 11) {
                Constant.printMsg("callleddddd");
                if (Constant.mChatListint11 == 1) {
                    Constant.printMsg("callleddddd222");
                    Constant.printMsg("userchatListnondelete::::>>>>>>>>>   "
                            + Constant.mJid);
                    dbAdapter
                            .setDeleteMessages_Non_secret_chat(Constant.mJid);


                } else {
                    Constant.printMsg("callledddddfdg");

                    Log.d("UserChat list", "Is Sec chat");
                    if (cursor.getInt(12) == 0) {
                        Constant.printMsg("callledddddcbvcb");
                        Constant.printMsg("userchatListdelete::::>>>>>>>>>   "
                                + Constant.mJid);
                        Log.d("UserChat list", "Is Admin::" + cursor.getInt(12));
                        dbAdapter.setDelete_Sec_chat(Constant.mJid);
//                        sendMessage(Constant.mJid);

                    } else {
                        Constant.printMsg("callledddddvbfdhtu");

                        ed.remove(Constant.mJid + "_self_desc_time");
                        ed.commit();
                        Log.d("UserChat list", "non Admin::" + cursor.getInt(12));
                        // dbAdapter.setDelete_Sec_chat_messages(cursor.getString(8));
                        dbAdapter.setDelete_Sec_chat(Constant.mJid);
                    }

                }
                ed.remove(Constant.mJid + "_sec_admin");
//            Constant.printMsg("userchatListremove::::>>>>>>>>>   "
//                    + chatl);
                ed.commit();
                String[] val = {null};
                new FetchChat().execute(val);
            } else if (menuItemIndex == 13) {
                Constant.printMsg("userchatListelsif::::>>>>>>>>>   "
                        + Constant.mJid);
                String query = "select status from " + Dbhelper.TABLE_LOCK
                        + " where jid = '" + Constant.mJid + "'";
                Constant.printMsg("bhaththam " + query);

                lock_status(query);
                // if (sp.contains(cursor.getString(8) + "_lock")) {
                if (status_lock.equalsIgnoreCase("lock")) {

                    lock_input("Unlock", Constant.mJid, false);
                } else {
                    lock_input("Lock", Constant.mJid, true);
                }
            }
            // added archive
            else if (menuItemIndex == 12) {
                Constant.printMsg("userchatListelsif::::archive chattttttt>>>>>>>>>   "
                        + Constant.mJid);
                dbAdapter.setUpdateArchive(Constant.mJid);
                // new FetchChat().execute();

                String[] val = {null};
                ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        String[] val = {null};
//        mProfileImagesList = new ArrayList<Bitmap>();
        ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);
//        try {
//            Constant.printMsg("siva called onResume Chat.............................");
//            Bundle extras = getActivity().getIntent().getExtras();
//            Constant.printMsg("siva called onResume Chat............................." + extras);
//            if (extras != null) {
//                String strdata = extras.getString("SliderIntent");
//                if (strdata.equals("SliderTesting")) {
//                    String[] val = {null};
//                    ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);
//                }
//            }
//        } catch (Exception e) {
//
//        }
        super.onResume();
    }

    @Override
    public void onPause() {


        super.onPause();
    }

    @Override
    public void onStart() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("chat");
        filter.addAction("chat_1");
        filter.addAction("start_typing");
        filter.addAction("lastseen_broadcast");
        filter.addAction("update_profile");
        filter.addAction("group_list");
        filter.addAction(Constant.PROFILE_UPDATE);
        getActivity().registerReceiver(lastseen_event, filter);
        super.onStart();
    }

    @Override
    public void onStop() {

        getActivity().unregisterReceiver(lastseen_event);

        super.onStop();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == 1) {

                    Boolean is_sec_chat_exist = false;

                    String jid = data.getExtras().getString("jid");
                    ContactsGetSet contact = dbAdapter.getContact(jid);

                    if (sp.contains(jid + "_sec_admin")) {
                        if (sp.getInt(jid + "_sec_admin", 0) == 0
                                || sp.getInt(jid + "_sec_admin", 0) == 1) {
                            is_sec_chat_exist = true;
                        } else {
                            is_sec_chat_exist = false;
                        }
                    }

                    if (!is_sec_chat_exist) {

                        ed.putInt(jid + "_sec_admin", 0);
                        ed.commit();
                        if (cursor.getString(8).contains("@conference.localhost")) {
                            final Intent in = new Intent(
                                    getActivity(), MUCTest.class);
                            in.putExtra("jid", jid);
                            in.putExtra("name", cursor.getString(1));
                            in.putExtra("is_owner", ""
                                    + cursor.getInt(12));
                            in.putExtra("avatar", cursor.getBlob(2));
                            startActivity(in);

                        } else {
                            Intent intent = new Intent(getActivity(), ChatTest.class);
                            intent.putExtra("jid", contact.getJid());
                            intent.putExtra("name", contact.getDisplay_name());
                            intent.putExtra("avatar", contact.getPhoto_ts());
                            intent.putExtra("IS_SECRET_CHAT", true);
                            intent.putExtra("is_owner", "0");
                            intent.putExtra("is_new_sec", true);
                            startActivity(intent);
                        }
                    } else {
                        new AlertManager().showAlertDialog(
                                getActivity(),
                                getResources().getString(
                                        R.string.secretchat_already_created), true);
                    }
                }
                break;
        }
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
            // c.close();
            // db.close();
        }
        return status_lock;

    }

    public void lock_input(final String name, final String jid,
                           final Boolean is_lock) {
        Log.d(TAG, "lock_input called..");
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        String positive_label;
        if (is_lock) {
            alert.setTitle(res.getString(R.string.lock_chat));
            alert.setMessage(res.getString(R.string.lock) + " " + name);
            positive_label = res.getString(R.string.lock);
        } else {
            alert.setTitle(res.getString(R.string.unlock_chat));
            alert.setMessage(res.getString(R.string.unlock) + " " + name);
            positive_label = res.getString(R.string.unlock);
        }

        final EditText input = new EditText(getActivity());

        input.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_CLASS_NUMBER);
        input.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        int maxLength = 4;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        input.setFilters(FilterArray);
        input.setHint(getActivity().getResources().getString(R.string.password));

        alert.setView(input);
        alert.setPositiveButton(positive_label,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        // if (value.equals(sp.getString("pin", ""))) {
                        if (is_lock) {
                            // ed.putBoolean(jid + "_lock", true);
                            //
                            ContentValues cv = new ContentValues();
                            cv.put("jid", jid);
                            cv.put("password", value);
                            cv.put("jid_name", name);
                            cv.put("status", "lock");
                            insertToLock(cv);
                            Constant.printMsg("inserted lock details ::::::   "
                                    + jid + "  " + value);

                            Intent i = new Intent(getActivity(),
                                    SliderTesting.class);
                            startActivity(i);
                        } else {
                            String query = "select password from "
                                    + Dbhelper.TABLE_LOCK + " where jid = '"
                                    + jid + "'";
                            Constant.printMsg("bhaththam value" + value
                                    + "   " + status_lock);
                            lock_status(query);
                            if (value.equals(status_lock)) {
                                Constant.printMsg("delete :::::");
                                String delete_query1 = "delete from "
                                        + Dbhelper.TABLE_LOCK
                                        + " where jid = '" + jid + "'";
                                delete_query(delete_query1);
                                Constant.printMsg("inserted lock details ::::::   "
                                        + jid + "  " + value);

                                Intent i = new Intent(getActivity(),
                                        SliderTesting.class);
                                startActivity(i);
                            } else {
                                new AlertManager().showAlertDialog(
                                        getActivity(),
                                        res.getString(R.string.you_are_entered_incorrect_pin),
                                        true);
                            }

                            // ed.remove(jid + "_lock");
                        }
                        // ed.commit();

                        // } else {
                        // new AlertManager().showAlertDialog(
                        // getActivity(),
                        // res.getString(R.string.you_are_entered_incorrect_pin),
                        // true);
                        // }
                    }
                })

                .setNegativeButton(res.getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // Canceled.
                            }
                        }).show();
    }

    protected void insertToLock(ContentValues cv) {
        // TODO Auto-generated method stub
        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_LOCK, null, cv);
            Constant.printMsg("No of inserted rows in lock :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in ecpl details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }
    }

    private void delete_query(String query) {
        // TODO Auto-generated method stub
        Cursor c = null;

        try {
            c = db.open().getDatabaseObj().rawQuery(query, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
        } catch (SQLException e) {

        } finally {
            if (c != null) {
                c.close();
            }

            db.close();
        }
    }

    public class FetchChat extends AsyncTask<String, String, Cursor> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(String... params) {

            cursor = dbAdapter.getChat_list_searchview(params[0]);
            Constant.chatlist.clear();
            try {

                if (cursor.moveToFirst()) {
                    do {
                        Chat_list_home_GetSet chatData = new Chat_list_home_GetSet();
                        chatData.set_id(cursor.getInt(0));
                        chatData.setDisplay_name(cursor.getString(1));
                        chatData.setPhoto_ts(cursor.getBlob(2));
                        chatData.setData(cursor.getString(3));
                        chatData.setTimestamp(cursor.getLong(4));
                        chatData.setStatus(cursor.getInt(5));
                        chatData.setNeeds_push(cursor.getInt(6));
                        chatData.setUnseen_msg_count(cursor.getInt(7));
                        chatData.setJidId(cursor.getString(8));
                        chatData.setKeyId(cursor.getInt(9));
                        chatData.setStatus10(cursor.getString(10));
                        chatData.setInt11(cursor.getInt(11));
                        chatData.setInt12(cursor.getInt(12));
                        chatData.setInt13(cursor.getInt(13));
                        if (cursor.getInt(14) == 0) {
                            Constant.chatlist.add(chatData);
                        }
                    } while (cursor.moveToNext());
                }

//                        Set<Chat_list_home_GetSet> s = new HashSet<Chat_list_home_GetSet>();
//                        s.addAll(Constant.chatlist);
//                        Constant.chatlist = new ArrayList<Chat_list_home_GetSet>();
//                        Constant.chatlist.addAll(s);
               /* if(cursor!=null)
                {
                    if(cursor.getCount()>0)
                    {
                        dbAdapter.setUpdateFlagForHomeScreen_ListItems();
                    }
                }*/
            } catch (Exception e) {

            }

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor result) {
            super.onPostExecute(result);
            if (result != null) {
                try {


                    if (Constant.chatlist.size() > 0) {



                        Constant.printMsg("siva called adpter called " + Constant.chatlist.size());

//                        mProfileImagesList = new ArrayList<Bitmap>();
                        adapter = new UserChatListAdapter(getActivity(), Constant.chatlist, false, "chat");
                        Constant.printMsg("siva called adpter called");
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
                        recyclerViewChat.setLayoutManager(linearLayoutManager);
                        recyclerViewChat.setHasFixedSize(true);
                        recyclerViewChat.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        UserChatList.mProfileImagesList = UserChatList.mTempProfileImagesList;
                    } else {
//                        mProfileImagesList = new ArrayList<Bitmap>();
                        adapter = new UserChatListAdapter();
                        Constant.printMsg("siva called adpter called111");
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
                        recyclerViewChat.setLayoutManager(linearLayoutManager);
                        recyclerViewChat.setHasFixedSize(true);
                        recyclerViewChat.setAdapter(adapter);
                        UserChatList.mProfileImagesList = UserChatList.mTempProfileImagesList;
                    }
                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                    // TODO: handle exception
                } finally {
                    if (result != null) {
                        result.close();
                    }
                }

            }


        }

    }

    public class TempFetchChat extends AsyncTask<String, String, Cursor> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(String... params) {

            try {
                cursor = dbAdapter.getChat_list_searchview(params[0]);
                tempchatlist = new ArrayList<Chat_list_home_GetSet>();

                if (cursor.moveToFirst()) {
                    do {
                        Chat_list_home_GetSet chatData = new Chat_list_home_GetSet();
                        chatData.set_id(cursor.getInt(0));
                        chatData.setDisplay_name(cursor.getString(1));
                        chatData.setPhoto_ts(cursor.getBlob(2));
                        chatData.setData(cursor.getString(3));
                        chatData.setTimestamp(cursor.getLong(4));
                        chatData.setStatus(cursor.getInt(5));
                        chatData.setNeeds_push(cursor.getInt(6));
                        chatData.setUnseen_msg_count(cursor.getInt(7));
                        chatData.setJidId(cursor.getString(8));
                        chatData.setKeyId(cursor.getInt(9));
                        chatData.setStatus10(cursor.getString(10));
                        chatData.setInt11(cursor.getInt(11));
                        chatData.setInt12(cursor.getInt(12));
                        chatData.setInt13(cursor.getInt(13));
                        tempchatlist.add(chatData);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {

            }

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor result) {
            super.onPostExecute(result);

            Constant.printMsg("GGGGGGGGGGGGOP");

            if (result != null) {
                try {
                    if (tempchatlist.size() > 0) {

                        Constant.chatlist.clear();
                        Constant.chatlist.addAll(tempchatlist);
                        Constant.printMsg("siva called adpter called " + Constant.chatlist.size());
//                        Set<Chat_list_home_GetSet> s = new HashSet<Chat_list_home_GetSet>();
//                        s.addAll(Constant.chatlist);
//                        Constant.chatlist = new ArrayList<Chat_list_home_GetSet>();
//                        Constant.chatlist.addAll(s);

                        if (adapter != null) {
                            try {
                                UserChatList.this.adapter.notifyDataSetChanged();
                            } catch (Exception e) {

                            }
                        } else {

//                            mProfileImagesList = new ArrayList<Bitmap>();
                            adapter = new UserChatListAdapter(getActivity(), Constant.chatlist, false, "chat");
                            Constant.printMsg("siva called adpter called");
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
                            recyclerViewChat.setLayoutManager(linearLayoutManager);
                            recyclerViewChat.setHasFixedSize(true);
                            recyclerViewChat.setAdapter(adapter);
                            UserChatList.mProfileImagesList = UserChatList.mTempProfileImagesList;
                        }

                    }
                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                    // TODO: handle exception
                } finally {
                    mRunTempFetchChat = true;
                    if (result != null) {
                        result.close();
                    }
                }

            }


        }

    }

    public class SearchChat extends AsyncTask<String, String, Cursor> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(String... params) {

            cursor = dbAdapter.getChat_list_searchview(params[0]);
            Constant.chatlist.clear();
            Constant.printMsg("size of userchat. int count.........." + cursor.getCount());
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Chat_list_home_GetSet chatData = new Chat_list_home_GetSet();
                        chatData.set_id(cursor.getInt(0));
                        chatData.setDisplay_name(cursor.getString(1));
                        chatData.setPhoto_ts(cursor.getBlob(2));
                        chatData.setData(cursor.getString(3));
                        chatData.setTimestamp(cursor.getLong(4));
                        chatData.setStatus(cursor.getInt(5));
                        chatData.setNeeds_push(cursor.getInt(6));
                        chatData.setUnseen_msg_count(cursor.getInt(7));
                        chatData.setJidId(cursor.getString(8));
                        chatData.setKeyId(cursor.getInt(9));
                        chatData.setStatus10(cursor.getString(10));
                        chatData.setInt11(cursor.getInt(11));
                        chatData.setInt12(cursor.getInt(12));
                        chatData.setInt13(cursor.getInt(13));
//                        Constant.printMsg("size of userchat. int.........."+cursor.getInt(14));
//                        if (cursor.getInt(14) == 0) {
                        Constant.chatlist.add(chatData);
//                        }
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Constant.printMsg("size of userchat. int. exception........." + e);
            }

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor result) {
            super.onPostExecute(result);
            Constant.printMsg("size of userchat..........." + result.getCount() + "....list size...." + Constant.chatlist.size());
            if (result != null) {
                try {
//                    if (Constant.chatlist.size() > 0) {
//                    mProfileImagesList = new ArrayList<Bitmap>();
                    adapter = new UserChatListAdapter(getActivity(), Constant.chatlist, false, "chat");
                    Constant.printMsg("siva called adpter called");
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
                    recyclerViewChat.setLayoutManager(linearLayoutManager);
                    recyclerViewChat.setHasFixedSize(true);
                    recyclerViewChat.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    UserChatList.mProfileImagesList = UserChatList.mTempProfileImagesList;
//                    }else{
//
//                    }
                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                    // TODO: handle exception
                } finally {
                    if (result != null) {
                        result.close();
                    }
                }

            }


        }

    }

}