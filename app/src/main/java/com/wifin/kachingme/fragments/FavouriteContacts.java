/*
* @author Sivanesan
*
* @usage -  This class is used to display all the contacts
*
* */

package com.wifin.kachingme.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.l4digital.fastscroll.FastScrollRecyclerView;
import com.viethoa.RecyclerViewFastScroller;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.async_tasks.ConcurrentAsyncTaskExecutor;
import com.wifin.kachingme.chat.chat_adaptors.FavouriteContactsAdapter;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.util.Constant;
import java.util.ArrayList;

/**
 * Created by Wifintech.
 */
public class FavouriteContacts extends Fragment {

    public static FastScrollRecyclerView contactFastScrollRecyclerView;
    public static ArrayList<Bitmap> mProfileImagesList = new ArrayList<Bitmap>();
    public static int mPosition;
    static DatabaseHelper dbAdapter;
    static ArrayList<ContactsGetSet> users, usersnot;
    static FavouriteContactsAdapter adapter;
    static Context mContext;
    ContentResolver cr;
    SharedPreferences sp;
    Editor ed;
    Resources res;
    EditText searchview;
    Dbhelper db;
    boolean isOnCreateCalled = false;
    BroadcastReceiver contact_update = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("contact_update")) {
                SliderTesting.progressBarRefresh.setVisibility(View.GONE);
                SliderTesting.mFloatingActionBtnRefresh.setVisibility(View.VISIBLE);
                SliderTesting.mRightSliderMenu_Add.setVisibility(View.VISIBLE);
                SliderTesting.mRightSliderMenu_Search.setVisibility(View.VISIBLE);
                ConcurrentAsyncTaskExecutor.executeConcurrently(new loadContactFromDb());
            }
        }
    };
    BroadcastReceiver contact_firebase_update = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("contact_firebase_update")) {
//                if (users!=null && users.size()>0){
//                    contactFastScrollRecyclerView.setHideScrollbar(false);
//                    adapter = new FavouriteContactsAdapter(mContext, R.layout.contactlistitem, users);
//                    contactFastScrollRecyclerView.setAdapter(adapter);
//                    //contactRecyclerView.smoothScrollToPosition(1);
//                    adapter.notifyDataSetChanged();
//                }else{
                    ConcurrentAsyncTaskExecutor.executeConcurrently(new loadContactFromDb());
//                }
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favouritecontacts, container, false);
        setHasOptionsMenu(true);
        hideKeyboard(getActivity());
        db = new Dbhelper(getActivity());
        com.wifin.kachingme.util.Constant.ref = true;
        initialization(v);
        SliderTesting.search_edit.setVisibility(View.GONE);
        mContext = v.getContext();
        isOnCreateCalled = true;
        ConcurrentAsyncTaskExecutor.executeConcurrently(new loadContactFromDb());
        cr = getActivity().getContentResolver();
        sp = getActivity().getSharedPreferences(
                KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
        ed = sp.edit();
        res = getResources();
        Constant.printMsg("siva called onCreateView Contact.............................");

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
                Constant.printMsg("Siva Search testttttttttttttttttttttttt.." + cs + "...length..." + cs.length());
                if (cs.length() > 0) {
                    SliderTesting.mHeaderClose.setVisibility(View.VISIBLE);
                    searchContactFunction(cs.toString().trim());
                    //ConcurrentAsyncTaskExecutor.executeConcurrently(new searchContactFromDb(),cs.toString().trim());
                } else {
                    SliderTesting.mHeaderClose.setVisibility(View.GONE);
                    ConcurrentAsyncTaskExecutor.executeConcurrently(new loadContactFromDb());
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

    private void initialization(View v) {
        contactFastScrollRecyclerView = (FastScrollRecyclerView) v.findViewById(R.id.contactList_fastScrollerRecyclerView);
        searchview = (EditText) v.findViewById(R.id.serchEdit);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        contactFastScrollRecyclerView.setLayoutManager(linearLayoutManager);
        contactFastScrollRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction("contact_update");
            getActivity().registerReceiver(contact_update, filter);
            IntentFilter filterFirebase = new IntentFilter();
            filterFirebase.addAction("contact_firebase_update");
            getActivity().registerReceiver(contact_firebase_update, filterFirebase);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Constant.printMsg("siva test contact resume");
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Constant.printMsg("iva test csontact onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Constant.printMsg("siva test contact onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(contact_update);
        getActivity().unregisterReceiver(contact_firebase_update);

        Constant.printMsg("siva test contact onDestroy");
    }

    public class loadContactFromDb extends AsyncTask<String, String, ArrayList<ContactsGetSet>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<ContactsGetSet> doInBackground(String... params) {
            dbAdapter = KachingMeApplication.getDatabaseAdapter();
            users = new ArrayList<ContactsGetSet>();
            usersnot = new ArrayList<ContactsGetSet>();
            users = dbAdapter.getKachingUserContacts();
            usersnot = dbAdapter.getContactsnotuser();
            ArrayList<ContactsGetSet> temp_user = new ArrayList<ContactsGetSet>();
            temp_user.addAll(users);
            temp_user.addAll(usersnot);
            for (int i = 0; i < temp_user.size(); i++) {
                Constant.printMsg("removed 12  ::::::; "
                        + temp_user.get(i).getJid().toString());
                if (KachingMeApplication.getBlocked_user().contains(
                        temp_user.get(i).getJid().toString())) {
                    Constant.printMsg("removed   ::::::; "
                            + temp_user.get(i).toString());
                    temp_user.remove(i);
                }
            }
            Constant.printMsg("Siva..............2.........");
            users.clear();
            users.addAll(temp_user);
            return users;
        }

        @Override
        protected void onPostExecute(ArrayList<ContactsGetSet> result) {
            super.onPostExecute(result);
            if (result != null && result.size() > 0) {
                contactFastScrollRecyclerView.setHideScrollbar(false);
                adapter = new FavouriteContactsAdapter(mContext, R.layout.contactlistitem, result);
                contactFastScrollRecyclerView.setAdapter(adapter);
                //contactRecyclerView.smoothScrollToPosition(1);
                adapter.notifyDataSetChanged();
            }else{
                contactFastScrollRecyclerView.setHideScrollbar(true);
            }
        }
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void searchContactFunction(String search) {
        users.clear();
        usersnot.clear();
        if (search.length() == 0) {
            users = dbAdapter.getKachingUserContacts();
        } else {
            users = dbAdapter.getContactsFilterNumber(search);
        }
        if (search.length() == 0) {
            usersnot = dbAdapter.getContactsnotuser();
        } else {
            usersnot = dbAdapter.getContactsFilternotNumber(search);
        }

        ArrayList<ContactsGetSet> temp_user = new ArrayList<ContactsGetSet>();
        temp_user.addAll(users);
        temp_user.addAll(usersnot);

        for (int i = 0; i < temp_user.size(); i++) {
            Constant.printMsg("removed 12  ::::::; "
                    + temp_user.get(i).getJid().toString());
            if (KachingMeApplication.getBlocked_user()
                    .contains(
                            temp_user.get(i).getJid()
                                    .toString())) {
                Constant.printMsg("removed   ::::::; "
                        + temp_user.get(i).toString());
                temp_user.remove(i);
            }
        }
        users.clear();
        users.addAll(temp_user);
        if (users !=null && users.size()>0){
            contactFastScrollRecyclerView.setHideScrollbar(false);
            adapter = new FavouriteContactsAdapter(mContext,
                    R.layout.contactlistitem, users);
            contactFastScrollRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else{
            contactFastScrollRecyclerView.setHideScrollbar(true);
        }
//        ArrayList<AlphabetItem> mAlphabetItems = new ArrayList<>();
//        List<String> strAlphabets = new ArrayList<>();
//        for (int i = 0; i < users.size(); i++) {
//            String name = users.get(i).getDisplay_name();
//            if (name == null || name.trim().isEmpty())
//                continue;
//            String word = name.substring(0, 1);
//            if (!strAlphabets.contains(word)) {
//                strAlphabets.add(word);
//                mAlphabetItems.add(new AlphabetItem(i, word, false));
//            }
//        }
//        if (users != null && users.size() > 0) {
//            fastScroller.setVisibility(View.VISIBLE);
//            fastScroller.setRecyclerView(contactRecyclerView);
//            fastScroller.setUpAlphabet(mAlphabetItems);
//        } else {
//            fastScroller.setVisibility(View.GONE);
//        }
    }
}
