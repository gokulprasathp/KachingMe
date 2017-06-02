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
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.async_tasks.ConcurrentAsyncTaskExecutor;
import com.wifin.kachingme.chat.broadcast_chat.Broadcast_create;
import com.wifin.kachingme.chat.chat_adaptors.UserChatListAdapter;
import com.wifin.kachingme.chat.muc_chat.NewGroup_FragmentActivity;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.listeners.MUC_SubjectChangeListener;
import com.wifin.kachingme.pojo.Chat_list_GetSet;
import com.wifin.kachingme.pojo.Chat_list_home_GetSet;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.settings.Profile;
import com.wifin.kachingme.settings.Settings;
import com.wifin.kachingme.settings.Status;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.AlertUtils;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.SelectContactList;
import com.wifin.kachingme.util.Select_MUC_Admin;
import com.wifin.kachingme.util.Utils;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.bookmarks.BookmarkManager;
import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MucEnterConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.json.JSONObject;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GroupChatList extends Fragment {
    public static ArrayList<Bitmap> mProfileImagesList = new ArrayList<Bitmap>();
    public static ArrayList<Bitmap> mTempProfileImagesList = new ArrayList<Bitmap>();
    public static int mPosition;
    public static ArrayList<Chat_list_home_GetSet> tempchatlist = new ArrayList<Chat_list_home_GetSet>();
    public static GroupChatList grpChatListObj;
    static String TAG = GroupChatList.class.getSimpleName();
    //    int width, height;
    RecyclerView recyclerViewChat;
    Cursor cursor;
    DatabaseHelper dbAdapter;
    int chatsCount = 0;
    int totalcount = 1;
    boolean mRunTempFetchChat = true;
    MultiUserChat muc;
    String[] val = {null};
    BroadcastReceiver lastseen_event = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("group_list")
                    || intent.getAction().equals(
                    Constant.BROADCAST_UPDATE_GROUP_ICON)) {
                ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);
            }
//            } else if (intent.getAction().equals("lastseen_broadcast")) {
//
//                Constant.printMsg("Chat List typing status GGG:");
//
//
//                String from = intent.getStringExtra("from");
//                String type = intent.getStringExtra("type");
//
//                try {
//
//                    for (int i = 0; i < chatlist.size(); i++) {
//
//                        if (chatlist.get(i).getJidId().equalsIgnoreCase(from)) {
//                            chatlist.get(i).setData(type);
//
//                            Constant.printMsg("siva called  Broadcast  ChatListgdgsdg GGG");
//
//                            GroupChatList.this.adapter.notifyDataSetChanged();
//                            break;
//                        }
//
//                    }
//                    pauseTypingThread();
//                } catch (Exception e) {
//
//                }
//            }

        }

    };
    UserChatListAdapter adapter;
    Resources res;
    SharedPreferences sp;
    Editor editor;
    //    EditText searchview;
    Boolean Is_Admin = false;
    String status_lock;
    Context context;
    Dbhelper db;
    ArrayList<String> chat_count = new ArrayList<String>();
    ArrayList<Chat_list_home_GetSet> chatlist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contactlist, container, false);
        setHasOptionsMenu(true);
        context = getActivity();
        db = new Dbhelper(getActivity());
        Constant.ref = false;
        Constant.printMsg("siva called onCreateView Group.............................");
        dbAdapter = KachingMeApplication.getDatabaseAdapter();
        recyclerViewChat = (RecyclerView) v.findViewById(R.id.chatList_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerViewChat.setLayoutManager(linearLayoutManager);
        recyclerViewChat.setHasFixedSize(true);
//        mProfileImagesList = new ArrayList<Bitmap>();
//        ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);

        grpChatListObj = this;

        GroupChatList.mProfileImagesList = GroupChatList.mTempProfileImagesList;

        res = getResources();
        sp = getActivity().getSharedPreferences(
                KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
        editor = sp.edit();

        SliderTesting.mHeaderEditText.setOnTouchListener(new OnTouchListener() {

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


//        SliderTesting.search_edit.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence cs, int arg1, int arg2,
//                                      int arg3) {
//                if (cs.length() > 0) {
//                    getSearchText(String.valueOf(cs).trim());
//                } else {
//                    getSearchText(null);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence arg0, int arg1,
//                                          int arg2, int arg3) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void afterTextChanged(Editable arg0) {
//                // TODO Auto-generated method stub
//            }
//
//        });
//
//        SliderTesting.mHeaderEditText.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence cs, int arg1, int arg2,
//                                      int arg3) {
//                Constant.printMsg("Siva testttttttttttttttttttttttt.." + cs);
//                if (cs.length() > 0) {
//                    SliderTesting.mHeaderClose.setVisibility(View.VISIBLE);
//                    Constant.printMsg("data checkkkkkkkkkkkkkkkkkkkkkk" + cs);
//                    getSearchText(cs.toString().trim());
//                } else {
//                    SliderTesting.mHeaderClose.setVisibility(View.GONE);
//                    getSearchText(null);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence arg0, int arg1,
//                                          int arg2, int arg3) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void afterTextChanged(Editable arg0) {
//                // TODO Auto-generated method stub
//            }
//        });
//                searchview = (EditText) v.findViewById(R.id.serchEdit);
//        try {
//            Constant.typeFace(getActivity(), searchview);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        searchview.setFocusable(false);
//
//        searchview.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//
//                searchview.setFocusable(true);
//                searchview.setFocusableInTouchMode(true);
//                searchview.requestFocus();
//                return false;
//            }
//        });
//
//        searchview.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence cs, int arg1, int arg2,
//                                      int arg3) {
//                if (cs.length() > 0) {
//                    getSearchText(String.valueOf(cs).trim());
//                } else {
//                    getSearchText(null);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence arg0, int arg1,
//                                          int arg2, int arg3) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void afterTextChanged(Editable arg0) {
//                // TODO Auto-generated method stub
//            }
//        });
//        height = com.wifin.kachingme.util.Constant.screenHeight;
//        width = com.wifin.kachingme.util.Constant.screenWidth;
//
//        LinearLayout.LayoutParams layoutenter = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        layoutenter.width = width;
//        layoutenter.height = height * 90 / 100;
//        layoutenter.gravity = Gravity.CENTER;
//        layoutenter.setMargins(width * 1 / 100, height * 1 / 100,
//                width * 1 / 100, height * 1 / 100);
//
//        LinearLayout.LayoutParams img = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        img.width = (int) (width * 8 / 100);
//        img.height = height * 4 / 100;
//        img.gravity = Gravity.CENTER;
    }

    @Override
    public void onResume() {

        chatsCount = 0;
        Constant.printMsg("siva called onResume Group.............................");
        super.onResume();
//        mProfileImagesList = new ArrayList<Bitmap>();
        ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public void onStart() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BROADCAST_UPDATE_GROUP_ICON);
        filter.addAction("group_list");
        filter.addAction("lastseen_broadcast");
        getActivity().registerReceiver(lastseen_event, filter);
        super.onStart();
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(lastseen_event);
        super.onStop();
    }

    public void pauseTypingThread() {

        if (mRunTempFetchChat == true) {
            mRunTempFetchChat = false;
            Thread typingStatusHold = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(1000);
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
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int menuItemIndex = item.getItemId();
        if (Constant.mSlectedActivity.equalsIgnoreCase("group")) {

            if (menuItemIndex == 10) {

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            dbAdapter.setGroupDeleteMessages(Constant.mJid);
                            int msg_id = dbAdapter.getLastMsgid(Constant.mJid);
                            dbAdapter.setUpdateChat_lits(Constant.mJid,
                                    msg_id);
                            Constant.printMsg("testtt group");

                            ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);
                        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    }
                };

                thread.start();

            } else if (menuItemIndex == 11) {

                Constant.printMsg("POPOPOPO111");

                // dbAdapter.setDeleteMessages(cursor.getString(8));

                try {
                    muc = TempConnectionService.MUC_MANAGER
                            .getMultiUserChat(JidCreate.entityBareFrom(Constant.mJid));
//                    DiscussionHistory history = new DiscussionHistory();
//                    history.setSince(Utils.getBookmarkDate(sp.getString(
//                            Constant.LAST_REFRESH_TIME + "_"
//                                    + Constant.mJid,
//                            Utils.getBookmarkTime())));
                    MucEnterConfiguration.Builder build = muc.getEnterConfigurationBuilder(Resourcepart.from(KachingMeApplication.getUserID()
                            + KachingMeApplication.getHost()));

                    build.requestHistorySince(Utils.getBookmarkDate(sp.getString(
                            Constant.LAST_REFRESH_TIME + "_"
                                    + Constant.mJid,
                            Utils.getBookmarkTime())));
//                    build.requestMaxStanzasHistory(0);
//                    build.requestMaxCharsHistory(0);
                    build.timeoutAfter(6000000L);

                    MucEnterConfiguration musOb = build.build();


//                if (!muc.isJoined()) {
                    muc.join(musOb);
//                    muc.join(
//                            KachingMeApplication.getUserID()
//                                    + KachingMeApplication.getHost(), null,
//                            history, 30000L);
                    muc.addMessageListener(TempConnectionService.muc_messageListener);
                    muc.addSubjectUpdatedListener(new MUC_SubjectChangeListener(
                            getActivity()));

                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                    // TODO: handle exception
                }

                String admin = sp.getString(Constant.mJid + "_admin", "");
                String uid = KachingMeApplication.getUserID()
                        + KachingMeApplication.getHost();
                // Constant.printMsg("UID::"+uid+" admin::"+admin+" condition::"+admin.equals(uid));
                if (admin.equals(uid)) {

                    Is_Admin = true;

					/*
                     * muc=new MultiUserChat(connection, cursor.getString(8));
					 * muc.join(KachingMeApplication.getjid());
					 */

                    String[] mm = sp.getString(Constant.mJid, "")
                            .toString().split(",");
                    String mm_list = "";
                    final String list;
                    int i = 0;
                    for (String string : mm) {
                        if (i == 0) {
                            mm_list = "'" + string + "'";
                        } else {
                            mm_list = mm_list + ",'" + string + "'";
                        }

                        i++;
                    }

                    list = mm_list;
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            getActivity());
                    builder.setTitle(
                            getResources().getString(
                                    R.string.delete_n_exit_group))
                            .setMessage(
                                    getResources()
                                            .getString(
                                                    R.string.you_must_assign_group_admin))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(
                                    getResources().getString(R.string.Ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // Yes button clicked, do something
                                            // Log.d("Muc_info","Is internet available::"+KachingMeApplication.getIsNetAvailable());
                                            if (KachingMeApplication
                                                    .getIsNetAvailable()) {
                                                Intent intente = new Intent(
                                                        getActivity(),
                                                        Select_MUC_Admin.class);
                                                intente.putExtra("jids",
                                                        Constant.mJid);
                                                startActivityForResult(intente,
                                                        2);

                                            } else {
                                                new AlertUtils()
                                                        .Toast_call(
                                                                getActivity(),
                                                                getResources()
                                                                        .getString(
                                                                                R.string.no_internet_connection));
                                            }

                                        }
                                    })

                            // Do nothing on no
                            .show();
                } else {
                    Remove_Meber(Constant.mJid);
                }
                ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);
            }
            // archive chat -22
            else if (menuItemIndex == 13) {
                String query = "select status from " + Dbhelper.TABLE_LOCK
                        + " where jid = '" + Constant.mJid + "'";
                Constant.printMsg("bhaththam " + query);

                lock_status(query);

                // if (sp.contains(cursor.getString(8) + "_lock")) {
                if (status_lock.equalsIgnoreCase("lock")) {

                    lock_input("this group", Constant.mJid, false);
                } else {
                    lock_input("this group", Constant.mJid, true);
                }
            } else if (menuItemIndex == 12) {

                Constant.printMsg("cursor ::::>>>>>> " + Constant.mJid);
                dbAdapter.setUpdateGroupArchive(Constant.mJid);
                // new FetchChat().execute();

                String[] val = {null};
                ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);
            }
        }

        return super.onContextItemSelected(item);
    }

    public void Delete_Local(String jid) {
        SharedPreferences sp = getActivity().getSharedPreferences(
                KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
        Editor editor = sp.edit();

        editor.remove(jid);
        editor.remove(jid + "_admin");

        editor.commit();

        dbAdapter.setDeleteContact(jid);
        dbAdapter.setDeleteMessages(jid);
        dbAdapter.setDeleteChatList(jid);
        dbAdapter.deleteGroup(jid);
        try {
            BookmarkManager bm = BookmarkManager
                    .getBookmarkManager(TempConnectionService.connection);
            bm.removeBookmarkedConference(JidCreate.entityBareFrom(jid));
        } catch (Exception e) {
            // ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
        }
    }

    public void Remove_Meber(String jid_mem) {

        try {

            muc.revokeOwnership(JidCreate.from(KachingMeApplication.getjid()));
            String mem_list = null;
            Collection<Affiliate> owner = muc.getOwners();

            int i = 0;
            for (Affiliate affiliate : owner) {
                if (i == 0) {
                    mem_list = affiliate.getJid().toString();
                } else {
                    mem_list = mem_list + "," + affiliate.getJid();
                }
                i++;
                Log.d("MUC_info", "Owner::" + affiliate.getJid());
            }

            dbAdapter.deleteGroupMembers(muc.getRoom().toString(), jid_mem);

            Message msg = new Message(JidCreate.from(jid_mem), Type.groupchat);
            // msg.setSubject("Remove");
            msg.setBody(mem_list);

            JivePropertiesManager.addProperty(msg, "ID", 2);
            JivePropertiesManager.addProperty(msg, "Removed_member",
                    KachingMeApplication.getjid());
            msg.setStanzaId(Constant.MEMBERREMOVEMESSAGE
                    + new Date().getTime());
            JivePropertiesManager.addProperty(msg, "media_wa_type", "0");
            muc.sendMessage(msg);
            Delete_Local(jid_mem);
            muc.leave();

            getActivity().startActivity(new Intent(getContext(), SliderTesting.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            getActivity().finish();

        } catch (Exception e) {
            // ACRA.getErrorReporter().handleException(e);
            e.printStackTrace();
            // TODO: handle exception
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        KachingMeApplication.SELECTED_TAB = 1;
        Intent intent;
        switch (item.getItemId()) {

            case R.id.menu_contacts:
                intent = new Intent(getActivity(), NewGroup_FragmentActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_profile:
                intent = new Intent(getActivity(), Profile.class);
                startActivity(intent);
                break;
            case R.id.menu_group:
                intent = new Intent(getActivity(), NewGroup_FragmentActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_settings:
                intent = new Intent(getActivity(), Settings.class);
                startActivity(intent);
                break;
            case R.id.menu_sec_chat:
                intent = new Intent(getActivity(), SelectContactList.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.menu_status:
                intent = new Intent(getActivity(), Status.class);
                startActivity(intent);
                break;
            case R.id.menu_new_broadcast:
                intent = new Intent(getActivity(), Broadcast_create.class);
                startActivity(intent);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == 1) {
                    Cursor c = null;
                    try {

                        c = cursor;
                        Boolean is_sec_chat_exist = false;

                        String jid = data.getExtras().getString("jid");
                        ContactsGetSet contact = dbAdapter.getContact(jid);
                        if (c.moveToFirst()) {
                            do {
                                Log.d("UserChatList", "JID::" + contact.getJid()
                                        + " is sec chat::" + c.getInt(11));
                                if (c.getString(8).equals(contact.getJid())
                                        && c.getInt(11) == 0) {
                                    is_sec_chat_exist = true;
                                }

                            } while (cursor.moveToNext());
                        }
                        if (is_sec_chat_exist == false) {
                            Intent intent = new Intent(getActivity(), ChatTest.class);
                            intent.putExtra("jid", contact.getJid());
                            intent.putExtra("name", contact.getDisplay_name());
                            intent.putExtra("avatar", contact.getPhoto_ts());
                            intent.putExtra("IS_SECRET_CHAT", true);
                            intent.putExtra("is_owner", "0");
                            intent.putExtra("is_new_sec", true);
                            startActivity(intent);
                        } else {
                            new AlertManager().showAlertDialog(
                                    getActivity(),
                                    getResources().getString(
                                            R.string.secretchat_already_created), true);
                        }

                    } catch (Exception e) {

                    } finally {
                        if (c != null) {
                            c.close();
                        }
                    }


                }
                break;
            case 2:
                if (resultCode == 2) {
                    if (data != null) {
                        try {
                            String jid = data.getStringExtra("jid");
                            Log.d("Activity Result", "Selected Admin jid::" + jid);
                            if (jid != null && jid != "") {
                                Admin_exit(jid);
                            }

                            String[] val = {null};
                            ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat(), val);
                        } catch (Exception e) {

                        }
                        // Add_Meber(jid);df
                    }
                }
                break;
        }
    }

    public void Admin_exit(String new_admin) {
        try {
            dbAdapter = KachingMeApplication.getDatabaseAdapter();

            Chat_list_GetSet chat_list = dbAdapter.getChat_List(muc.getRoom().toString());
            Form f1 = muc.getConfigurationForm();
            List<String> admin = new ArrayList<String>();
            admin.add(new_admin);
            Form submitForm = f1.createAnswerForm();
            JSONObject main_job = new JSONObject();

            for (Iterator fields = f1.getFields().iterator(); fields.hasNext(); ) {
                FormField field = (FormField) fields.next();

                String s = "";

                Iterator<String> am = field.getValues().iterator();

                while (am.hasNext()) {
                    s = am.next();

                }

                if (field.getVariable().equals("muc#roomconfig_roomdesc")) {

                    JSONObject json = new JSONObject(s);
                    JSONObject j_obj = json.getJSONObject("data");
                    j_obj.put(Constant.GROUP_ADMIN, new_admin);
                    j_obj.put(Constant.GROUP_TYPE,
                            sp.getString(muc.getRoom() + "_group_type", "none"));
                    j_obj.put(Constant.GROUP_TOPIC, sp.getString(muc.getRoom()
                            + "_group_question", "none"));
                    j_obj.put(
                            Constant.TOPIC_OPTION,
                            sp.getString(muc.getRoom()
                                    + "_group_question_options", "none"));
                    j_obj.put(Constant.TIMESTAMP,
                            "" + chat_list.getTimestamp());
                    main_job.put("data", j_obj);
                    // Log.d("Muc_invitation", "Room Admin::" + room_admin);
                }
                // //Constant.printMsg("Form Field::"+field.getLabel()+"::"+field.getVariable()+"::"+s);
            }

            Log.d(TAG, "New Room Configuration::" + main_job.toString());
            submitForm
                    .setAnswer("muc#roomconfig_roomdesc", main_job.toString());
            muc.sendConfigurationForm(submitForm);

            try {

                muc.revokeOwnership(JidCreate.from(KachingMeApplication.getUserID()
                        + KachingMeApplication.getHost()));

				/* muc.sendConfigurationForm(f1); */
                String mem_list = null;
                Collection<Affiliate> owner = muc.getOwners();

                int i = 0;
                for (Affiliate affiliate : owner) {
                    if (i == 0) {
                        mem_list = affiliate.getJid().toString();
                    } else {
                        mem_list = mem_list + "," + affiliate.getJid();
                    }
                    i++;
                    Log.d("MUC_info", "Owner::" + affiliate.getJid());
                }

                dbAdapter.updateGroupMembers(muc.getRoom().toString(), new_admin, 1);
                dbAdapter.deleteGroupMembers(muc.getRoom().toString(),
                        KachingMeApplication.getjid());

                Message msg = new Message(muc.getRoom(), Type.groupchat);
                // msg.setSubject(muc.getSubject());

                msg.setBody(mem_list);
                /*
                 * msg.setProperty("ID",2);
				 * msg.setProperty("New_admin",new_admin);
				 * msg.setProperty("Left_Member"
				 * ,KachingMeApplication.getUserID()+KachingMeApplication.getHost());
				 * msg.setPacketID(""+new Date().getTime());
				 */
                JivePropertiesManager.addProperty(msg, "ID", 2);
                JivePropertiesManager.addProperty(msg, "Removed_member",
                        KachingMeApplication.getjid());
                msg.setStanzaId(Constant.MEMBERREMOVEMESSAGE
                        + new Date().getTime());
                JivePropertiesManager.addProperty(msg, "media_wa_type", "0");
                muc.sendMessage(msg);

                Delete_Local();
                muc.leave();


            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                // TODO: handle exception
            }

        } catch (Exception e) {
            // ACRA.getErrorReporter().handleException(e);
            e.printStackTrace();
        }
    }

	/*
     * private void updateCursor() { cursor=dbAdapter.getGroupChat_list();
	 * 
	 * }
	 */

    public void Delete_Local() {
        editor.remove(muc.getRoom().toString());
        editor.remove(muc.getRoom() + "_admin");

        editor.commit();

        dbAdapter.setDeleteContact(muc.getRoom().toString());
        dbAdapter.setDeleteMessages(muc.getRoom().toString());
        dbAdapter.setDeleteChatList(muc.getRoom().toString());
        dbAdapter.deleteGroup(muc.getRoom().toString());
        try {
            BookmarkManager bm = BookmarkManager.getBookmarkManager(TempConnectionService.connection);
            bm.removeBookmarkedConference(muc.getRoom());
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void lock_input(final String name, final String jid,
                           final Boolean is_lock) {
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
        input.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        input.setHint(getActivity().getResources().getString(R.string.password));
        alert.setView(input);
        alert.setPositiveButton(positive_label,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        if (input == null) {
                            Toast.makeText(context, "Please enter valid data", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (input.length() == 0) {
                            Toast.makeText(context, "Please enter valid data", Toast.LENGTH_SHORT).show();
                            return;

                        }

                        if (is_lock) {
                            // editor.putBoolean(jid + "_lock", true);
                            ContentValues cv = new ContentValues();
                            cv.put("jid", jid);
                            cv.put("password", value);
                            cv.put("jid_name", name);
                            cv.put("status", "lock");
                            insertToLock(cv);
                            Constant.printMsg("inserted lock details ::::::   "
                                    + jid + "  " + value);

                            Constant.group_lock = true;
                            Intent i = new Intent(getActivity(),
                                    SliderTesting.class);
                            startActivity(i);
                        } else {
                            // editor.remove(jid + "_lock");
                            String query = "select password from "
                                    + Dbhelper.TABLE_LOCK + " where jid = '"
                                    + jid + "'";
                            Constant.printMsg("bhaththam value" + value
                                    + "   " + status_lock);
                            lock_status(query);
                            if (value.equals(status_lock)) {
                                String delete_query1 = "delete from "
                                        + Dbhelper.TABLE_LOCK
                                        + " where jid = '" + jid + "'";
                                delete_query(delete_query1);
                                Constant.printMsg("inserted lock details ::::::   "
                                        + jid + "  " + value);
                                Constant.group_lock = true;
                                Intent i = new Intent(getActivity(),
                                        SliderTesting.class);
                                startActivity(i);
                            } else {
                                new AlertManager().showAlertDialog(
                                        getActivity(),
                                        res.getString(R.string.you_are_entered_incorrect_pin),
                                        true);
                            }
                            // editor.commit();
                        }
                    }
                });

        alert.setNegativeButton(res.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

        alert.show();
    }

    public void getSearchText(String ch) {
        ConcurrentAsyncTaskExecutor.executeConcurrently(new SearchChat(), ch);
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
            if (db != null) {
                db.close();
            }
        }
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
            if (db != null) {
                db.close();
            }
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
            if (c != null) {
                c.close();

            }
            if (db != null) {
                db.close();
            }
        }
        return status_lock;

    }

    class FetchChat extends AsyncTask<String, String, Cursor> {

        @Override
        protected void onPreExecute() {

            Constant.printMsg("GroupChatList GG");
            chatsCount = 0;
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(String... params) {

            try {
                cursor = dbAdapter.getMUCChat_list_Filter(params[0]);
                chatlist.clear();
                if (cursor.moveToFirst()) {
                    do {
                        Chat_list_home_GetSet chatData = new Chat_list_home_GetSet();
//                            if (cursor.getInt(12) == 0) {
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

                        if (cursor.getInt(7) > 0)
                            chatsCount++;

                        chatlist.add(chatData);
//                            }
                    } while (cursor.moveToNext());
                }

            } catch (Exception e) {

            }

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor result) {
            if (result != null) {
                try {
                    if (chatlist.size() > 0) {
//                        Set<Chat_list_home_GetSet> s = new HashSet<Chat_list_home_GetSet>();
//                        s.addAll(chatlist);
//                        chatlist = new ArrayList<Chat_list_home_GetSet>();
//                        chatlist.addAll(s);

//                        mProfileImagesList = new ArrayList<Bitmap>();

                        //  Constant.mChatCounts.set(1,String.valueOf(chatsCount));
                        //  SliderTesting.updateHedder();


                        adapter = new UserChatListAdapter(getActivity(), chatlist, false, "group");
                        recyclerViewChat.setAdapter(adapter);
                        GroupChatList.this.adapter.notifyDataSetChanged();
                        GroupChatList.mProfileImagesList = GroupChatList.mTempProfileImagesList;
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                } finally {
                    if (result != null) {
                        result.close();
                    }
                }

            }
            super.onPostExecute(result);
        }

    }

    class TempFetchChat extends AsyncTask<String, String, Cursor> {

        @Override
        protected void onPreExecute() {
            chatsCount = 0;
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(String... params) {

            try {
                cursor = dbAdapter.getMUCChat_list_Filter(params[0]);
                tempchatlist = new ArrayList<Chat_list_home_GetSet>();

                if (cursor.moveToFirst()) {
                    do {
                        Chat_list_home_GetSet chatData = new Chat_list_home_GetSet();
//                            if (cursor.getInt(12) == 0) {
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

                        if (cursor.getInt(7) > 0)
                            chatsCount++;


                        tempchatlist.add(chatData);
//                            }
                    } while (cursor.moveToNext());
                }

            } catch (Exception e) {

            }

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor result) {

            if (result != null) {
                try {
                    if (tempchatlist.size() > 0) {

//                        Constant.mChatCounts.set(1,String.valueOf(chatsCount));
//                        SliderTesting.updateHedder();

                        chatlist.clear();
                        chatlist.addAll(tempchatlist);
//                        Set<Chat_list_home_GetSet> s = new HashSet<Chat_list_home_GetSet>();
//                        s.addAll(chatlist);
//                        chatlist = new ArrayList<Chat_list_home_GetSet>();
//                        chatlist.addAll(s);

                        if (adapter != null) {
                            GroupChatList.this.adapter.notifyDataSetChanged();
                        } else {
//                            mProfileImagesList = new ArrayList<Bitmap>();
                            adapter = new UserChatListAdapter(getActivity(), chatlist, false, "group");
                            recyclerViewChat.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            GroupChatList.mProfileImagesList = GroupChatList.mTempProfileImagesList;
                        }


                    }
                } catch (Exception e) {
                    // TODO: handle exception
                } finally {
                    mRunTempFetchChat = true;
                    if (result != null) {
                        result.close();
                    }
                }

            }
            super.onPostExecute(result);
        }

    }

    class SearchChat extends AsyncTask<String, String, Cursor> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(String... params) {

            try {
                cursor = dbAdapter.getMUCChat_list_Filter(params[0]);
                chatlist.clear();
                if (cursor.moveToFirst()) {
                    do {
                        Chat_list_home_GetSet chatData = new Chat_list_home_GetSet();
//                            if (cursor.getInt(12) == 0) {
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


                        chatlist.add(chatData);
//                            }
                    } while (cursor.moveToNext());
                }

            } catch (Exception e) {

            }

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor result) {
            Constant.printMsg("size of group filter. before.........." + result);

            if (result != null) {
                Constant.printMsg("size of group filter..........." + result.getCount());
                try {
//                    if (chatlist.size() > 0) {
//                        mProfileImagesList = new ArrayList<Bitmap>();


                    adapter = new UserChatListAdapter(getActivity(), chatlist, false, "group");
                    recyclerViewChat.setAdapter(adapter);
                    GroupChatList.this.adapter.notifyDataSetChanged();
                    GroupChatList.mProfileImagesList = GroupChatList.mTempProfileImagesList;
//                    }else{
//                        mProfileImagesList = new ArrayList<Bitmap>();
//                        adapter = new UserChatListAdapter(getActivity(), chatlist, false, "group");
//                        recyclerViewChat.setAdapter(adapter);
//                        GroupChatList.this.adapter.notifyDataSetChanged();
//                    }
                } catch (Exception e) {
                    // TODO: handle exception
                } finally {
                    if (result != null) {
                        result.close();
                    }
                }

            }
            super.onPostExecute(result);
        }

    }

}