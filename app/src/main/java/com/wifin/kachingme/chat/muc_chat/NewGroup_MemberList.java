package com.wifin.kachingme.chat.muc_chat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.bradcast_recivers.GlobalBroadcast;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.listeners.MUC_SubjectChangeListener;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.GroupParticipantGetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.services.DestructService;
import com.wifin.kachingme.services.Group_CreateIntentService;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.AlertUtils;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.MultiselectMembers;
import com.wifin.kachingme.util.ProfileRoundImg;
import com.wifin.kachingme.util.SendWeb_Group;
import com.wifin.kachingme.util.Utils;

import org.apache.http.Header;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smackx.bookmarks.BookmarkManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewGroup_MemberList extends Fragment {

    public static ArrayList<ContactsGetSet> selected_users;
    static String TAG = NewGroup_MemberList.class.getSimpleName();
    AutoCompleteTextView txt_auto;
    ListView list;
    TextView txt_members;
    DatabaseHelper dbAdapter;
    ArrayList<ContactsGetSet> users;
    UserListAdapter select_adapter;
    ImageButton btn_multi_select;
    /* MUC */
    // KaChingMeService mBoundService;
    Boolean isBound;
    // public static AbstractXMPPConnection connection;
    //MultiUserChat muc;
    String room_subject, grp_id;
    ProgressDialog progressdialog;

    GroupParticipantGetSet group_partcipant_getset;

    AutoAdapter adapter = null;
    BroadcastReceiver grp_Create_Broadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("create_group")
                    ) {
                try {
                    Intent login_broadcast = new Intent("chat");
                    login_broadcast.putExtra("jid", "" + grp_id);
                    getActivity().sendBroadcast(login_broadcast);
                    try {
                        progressdialog.cancel();
                    } catch (Exception e) {

                    }
                    Constant.mFromMemberList = true;
                    Intent i = new Intent(getActivity(), SliderTesting.class);
                    startActivity(i);
                    getActivity().finish();
                } catch (Exception e) {

                }

            }
        }
    };

    @Override
    public void onStart() {

        IntentFilter filter = new IntentFilter();
        filter.addAction("create_group");

        getActivity().registerReceiver(grp_Create_Broadcast, filter);
        super.onStart();
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(grp_Create_Broadcast);
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.newgroup_memberlist, container,
                false);

        //new ServerConnectionAsync(getActivity()).execute();

        txt_auto = (AutoCompleteTextView) v.findViewById(R.id.txt_autocomplete);
        txt_members = (TextView) v.findViewById(R.id.txt_members);
        list = (ListView) v.findViewById(R.id.list_options);
        btn_multi_select = (ImageButton) v.findViewById(R.id.btn_multi_select);

        try {
            Constant.typeFace(getActivity(), txt_auto);
            Constant.typeFace(getActivity(), txt_members);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dbAdapter = KachingMeApplication.getDatabaseAdapter();
        selected_users = new ArrayList<ContactsGetSet>();

        users = new ArrayList<ContactsGetSet>();
        users = dbAdapter.getContacts();

        try {
            for (int i = 0; i < NewGroup_MemberList.selected_users.size(); i++) {

                for (int j = 0; j < users.size(); j++) {

                    if (users.get(j).getJid().equalsIgnoreCase(NewGroup_MemberList.selected_users.get(i).getJid())) {
                        users.remove(j);
                    }

                }

            }
        } catch (Exception e) {

            Constant.printMsg("dfsmfsa" + e.toString());

        }
        adapter = new AutoAdapter(getActivity(),
                R.layout.contactlistitem, users);
        txt_auto.setAdapter(adapter);

        select_adapter = new UserListAdapter(getActivity(),
                R.layout.add_grm_mem_item, selected_users);
        list.setAdapter(select_adapter);

        View view = new View(getActivity());
        list.addFooterView(view);

        progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage(getResources().getString(R.string.loading));

        btn_multi_select.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in = new Intent(getActivity(), MultiselectMembers.class);
                startActivityForResult(in, 12);
            }
        });

        txt_auto.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (selected_users.size() < 200) {
                    boolean is_selected = false;
                    for (int j = 0; j < selected_users.size(); j++) {

                        if (selected_users.get(j).getJid()
                                .equals(users.get(position).getJid())) {
                            is_selected = true;

                        }
                    }

                    if (is_selected == false) {
                        for (int j = 0; j < users.size(); j++) {

                            if (users.get(j).getJid()
                                    .equals(users.get(position))) {
                                selected_users.add(users.get(j));
                                select_adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    selected_users.add(users.get(position));
                    select_adapter.notifyDataSetChanged();
                    txt_auto.setText("");
                    txt_members.setText(selected_users.size()
                            + " "
                            + getResources().getString(
                            R.string.out_of_200_members));
                    users = new ArrayList<ContactsGetSet>();
                    users = dbAdapter.getContacts();

                    try {
                        for (int i = 0; i < NewGroup_MemberList.selected_users.size(); i++) {

                            for (int j = 0; j < users.size(); j++) {

                                if (users.get(j).getJid().equalsIgnoreCase(NewGroup_MemberList.selected_users.get(i).getJid())) {
                                    users.remove(j);
                                }

                            }

                        }
                    } catch (Exception e) {

                        Constant.printMsg("dfsmfsa" + e.toString());

                    }
                    adapter = new AutoAdapter(getActivity(),
                            R.layout.contactlistitem, users);
                    txt_auto.setAdapter(adapter);
                } else {
                    new AlertManager().showAlertDialog(
                            getActivity(),
                            getResources().getString(
                                    R.string.only_200_members_are_allowed),
                            true);
                }
            }
        });

        txt_members.setText("0 "
                + getResources().getString(R.string.out_of_200_members));

        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuitem = menu.add(0, 2, 0, getActivity().getResources()
                .getString(R.string.create));

        MenuCompat.setShowAsAction(menuitem, MenuItem.SHOW_AS_ACTION_ALWAYS);
        // .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		/* inflater. */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        try {
            if (item.getItemId() == 2) {

                if (Connectivity.isOnline(getActivity())) {

                    if(Connectivity.isTempConnection()) {

                        Constant.printMsg("Group Subject::"
                                + NewGroup_Info.txt_subject.getText().toString());
                        Constant.printMsg("Group Type::" + NewGroup_Info.group_type);
                        Constant.printMsg("Group Question::"
                                + NewGroup_Info.txt_questions.getText().toString());

                        for (String string : NewGroup_Info.ar_list_option) {

                            Log.d(TAG, "Group Question Options::" + string);
                        }

                        if (NewGroup_Info.txt_subject.getText().toString().trim().length() == 0) {
                            new AlertManager().showAlertDialog(
                                    getActivity(),
                                    getResources().getString(
                                            R.string.please_enter_room_subject), true);
                        } else if (NewGroup_Info.group_type == -1) {
                            new AlertManager().showAlertDialog(
                                    getActivity(),
                                    getResources().getString(
                                            R.string.please_select_group_type), true);
                        } else if (NewGroup_Info.group_type > 0
                                && NewGroup_Info.txt_questions.getText().toString().trim()
                                .length() == 0) {
                            new AlertManager().showAlertDialog(
                                    getActivity(),
                                    getResources().getString(
                                            R.string.please_enter_group_question), true);
                        } else if (NewGroup_Info.group_type >= 4
                                && (NewGroup_Info.ar_list_option.size() == 0 || NewGroup_Info.ar_list_option
                                .size() == 1)) {
                            new AlertManager().showAlertDialog(
                                    getActivity(),
                                    getResources().getString(
                                            R.string.please_give_more_than_two_options),
                                    true);
                        } else {

                            // new CreateRoom().execute();\

                            try {
                                if (selected_users.size() > 0) {
                                    try {
                                        progressdialog.show();
                                    } catch (Exception e) {
                                    }
                                    room_subject = NewGroup_Info.txt_subject.getText().toString()
                                            .trim();

                                    if (room_subject != null) {
                                        Intent i = new Intent(getActivity(), Group_CreateIntentService.class);
                                        i.putExtra("room_subject", room_subject);
                                        getActivity().startService(i);
                                    }
                                    Constant.printMsg("group createddd ::::::::::");
                                } else {
                                    Constant.printMsg("group nottt createddd ::::::::::");
                                    Toast.makeText(getActivity(),
                                            "At least 1 contact must be selected",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                            }

                        }
                    }else
                    {
                        try {
                            getActivity().stopService(new Intent(getActivity(), TempConnectionService.class));
                            GlobalBroadcast.stopService(getActivity());
                            getActivity().startService(new Intent(getActivity(), TempConnectionService.class));
                        } catch (Exception e) {

                        }
                        new AlertUtils().Toast_call(getActivity(),"Something went wrong. Try again later.");
                    }

                } else {
                    new AlertUtils().Toast_call(getActivity(), getResources()
                            .getString(R.string.no_internet_connection));
                }
            }
        } catch (Resources.NotFoundException e) {

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub

        super.onPause();
    }

    @Override
    public void onResume() {
        // doBindService();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        // doUnbindService();
        super.onDestroy();
    }


    // private ServiceConnection mConnection = new ServiceConnection() {
    // public void onServiceConnected(ComponentName className, IBinder service)
    // {
    //
    // mBoundService = ((KaChingMeService.LocalBinder) service)
    // .getService();
    // connection = mBoundService.getConnection();
    //
    // }
    //
    // public void onServiceDisconnected(ComponentName className) {
    //
    // mBoundService = null;
    // }
    // };
    //
    // void doBindService() {
    //
    // getActivity().bindService(
    // new Intent(getActivity(), KaChingMeService.class), mConnection,
    // Context.BIND_AUTO_CREATE);
    // isBound = true;
    // }
    //
    // void doUnbindService() {
    // if (isBound) {
    //
    // getActivity().unbindService(mConnection);
    // isBound = false;
    // }
    // }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12) {

            if (data != null) {
                users.clear();
                users = dbAdapter.getContacts();
                String jid = data.getStringExtra("jid");

                Log.d("NewwGroup", "Selected user::" + jid
                        + " selected user size::" + selected_users.size());

                String selected[] = jid.split(",");

                for (int i = 0; i < selected.length; i++) {
                    boolean is_selected = false;
                    if (selected_users.size() > 0) {
                        for (int j = 0; j < selected_users.size(); j++) {

                            if (selected_users.get(j).getJid()
                                    .equals(selected[i])) {
                                is_selected = true;
                            }
                        }
                        Log.d("NewGrop", "Is selectd::" + is_selected);
                        if (is_selected == false) {
                            for (int k = 0; k < users.size(); k++) {

                                Log.d("NewGroup_List", users.get(k).getJid()
                                        + "::" + selected[i]);

                                if (users.get(k).getJid().equals(selected[i])) {
                                    Log.d("NewGroup", "User inserted !!!!");
                                    selected_users.add(users.get(k));
                                    select_adapter = new UserListAdapter(
                                            getActivity(),
                                            R.layout.add_grm_mem_item,
                                            selected_users);
                                    list.setAdapter(select_adapter);
                                    select_adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    } else {
                        for (int j = 0; j < users.size(); j++) {

                            if (users.get(j).getJid().equals(selected[i])) {
                                selected_users.add(users.get(j));
                                select_adapter.notifyDataSetChanged();
                                select_adapter = new UserListAdapter(
                                        getActivity(),
                                        R.layout.add_grm_mem_item,
                                        selected_users);
                                list.setAdapter(select_adapter);
                                select_adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                txt_members
                        .setText(selected_users.size()
                                + " "
                                + getResources().getString(
                                R.string.out_of_200_members));
                users = new ArrayList<ContactsGetSet>();
                users = dbAdapter.getContacts();

                try {
                    for (int i = 0; i < NewGroup_MemberList.selected_users.size(); i++) {

                        for (int j = 0; j < users.size(); j++) {

                            if (users.get(j).getJid().equalsIgnoreCase(NewGroup_MemberList.selected_users.get(i).getJid())) {
                                users.remove(j);
                            }

                        }

                    }
                } catch (Exception e) {

                    Constant.printMsg("dfsmfsa" + e.toString());

                }
                adapter = new AutoAdapter(getActivity(),
                        R.layout.contactlistitem, users);
                txt_auto.setAdapter(adapter);
            }

        }
    }

    private class UserListAdapter extends ArrayAdapter<ContactsGetSet> {
        ArrayList<ContactsGetSet> list;
        Context context;
        private int resource;
        private LayoutInflater layoutInflater;

        public UserListAdapter(Context context, int textViewResourceId,
                               ArrayList<ContactsGetSet> objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            this.resource = textViewResourceId;
            layoutInflater = LayoutInflater.from(context);
            list = objects;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            LinearLayout container = (LinearLayout) (inflater.inflate(resource,
                    parent, false));
            ContactsGetSet user = list.get(position);
            TextView name = (TextView) container.findViewById(R.id.name);
            ImageView img = (ImageView) container.findViewById(R.id.avtarimg);
            ImageView img_remove = (ImageView) container.findViewById(R.id.img_remove);
            LinearLayout grmLayout = (LinearLayout) container.findViewById(R.id.add_grmLayout);

            int width = Constant.screenWidth;
            int height = Constant.screenHeight;

            LinearLayout.LayoutParams addGrmLayout = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            addGrmLayout.width = width;
            addGrmLayout.height = (int) (height * 9 / 100);
            addGrmLayout.gravity = Gravity.CENTER;
            grmLayout.setLayoutParams(addGrmLayout);
            grmLayout.setGravity(Gravity.CENTER | Gravity.LEFT);

            FrameLayout.LayoutParams circularImage = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            circularImage.width = width * 15 / 100;
            circularImage.height = height * 8 / 100;
            circularImage.setMargins(width * 5 / 100, height * 1 / 2 * 100, width * 1 / 100, height * 1 / 2 * 100);
            circularImage.gravity = Gravity.CENTER | Gravity.LEFT;
            img.setLayoutParams(circularImage);

            LinearLayout.LayoutParams chatMsgParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            chatMsgParama.width = width * 63 / 100;
            chatMsgParama.height = (int) (height * 9 / 100);
            name.setLayoutParams(chatMsgParama);
            name.setGravity(Gravity.CENTER | Gravity.LEFT);
            name.setPadding(width * 3 / 100, 0, 0, 0);
            name.setGravity(Gravity.CENTER | Gravity.LEFT);

            LinearLayout.LayoutParams removeParama = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            removeParama.width = width * 15 / 100;
            removeParama.height = height * 7 / 100;
            removeParama.setMargins(width * 1 / 100, height * 1 / 100, width * 1 / 100, height * 1 / 100);
            removeParama.gravity = Gravity.CENTER | Gravity.LEFT;
            img_remove.setLayoutParams(removeParama);

            if (width >= 600) {
                name.setTextSize(19);
            } else if (width > 501 && width < 600) {
                name.setTextSize(17);
            } else if (width > 260 && width < 500) {
                name.setTextSize(15);
            } else if (width <= 260) {
                name.setTextSize(13);
            }
            // set name
            name.setText(user.getDisplay_name());

            try {
                Bitmap bmp = null;
                System.gc();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inDither = true;
                options.inSampleSize = 2;


                try {
                    if (user.getPhoto_ts() != null) {
                        bmp = BitmapFactory.decodeByteArray(
                                user.getPhoto_ts(), 0, user.getPhoto_ts().length, options);
//                        img.setImageBitmap(new AvatarManager().roundCornerImage(
//                                bmp, 180));
                        ProfileRoundImg roundImageProfile = new ProfileRoundImg(bmp);
                        img.setImageDrawable(roundImageProfile);
                    } else {
                        img.setImageDrawable(getResources().getDrawable(
                                R.drawable.avtar));
                    }
//                     bmp = BitmapFactory.decodeByteArray(user.getPhoto_ts(),
//                            0, user.getPhoto_ts().length, options);
                } catch (OutOfMemoryError e) {
                    Log.e("Map", "Tile Loader (241) Out Of Memory Error " + e.getLocalizedMessage());
                    System.gc();
                }
                //img.setImageBitmap(new AvatarManager().roundCornerImage(bmp,
                //        180));
                //bmp.recycle();
            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
            }
            img_remove.setTag(position);
            img_remove.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    selected_users.remove(position);
                    select_adapter.notifyDataSetChanged();
                    txt_members.setText(selected_users.size()
                            + " "
                            + getResources().getString(
                            R.string.out_of_200_members));
                }
            });
            return container;
        }

    }

    private class AutoAdapter extends ArrayAdapter<ContactsGetSet> {
        Filter nameFilter = new Filter() {
            public String convertResultToString(Object resultValue) {
                String str = ((ContactsGetSet) (resultValue)).getDisplay_name();
                return str;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    for (ContactsGetSet product : itemsAll) {
                        if (product
                                .getDisplay_name()
                                .toLowerCase()
                                .startsWith(constraint.toString().toLowerCase())) {
                            suggestions.add(product);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                @SuppressWarnings("unchecked")
                ArrayList<ContactsGetSet> filteredList = (ArrayList<ContactsGetSet>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (ContactsGetSet c : filteredList) {
                        add(c);
                    }
                    notifyDataSetChanged();
                }
            }
        };
        private ArrayList<ContactsGetSet> items;
        private ArrayList<ContactsGetSet> itemsAll;
        private ArrayList<ContactsGetSet> suggestions;
        private int viewResourceId;

        @SuppressWarnings("unchecked")
        public AutoAdapter(Context context, int viewResourceId,
                           ArrayList<ContactsGetSet> items) {
            super(context, viewResourceId, items);
            this.items = items;
            this.itemsAll = (ArrayList<ContactsGetSet>) items.clone();
            this.suggestions = new ArrayList<ContactsGetSet>();
            this.viewResourceId = viewResourceId;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(viewResourceId, null);
            }
            ContactsGetSet product = items.get(position);
            if (product != null) {
                TextView productLabel = (TextView) v.findViewById(R.id.name);
                ImageView img = (ImageView) v.findViewById(R.id.avtarimg);
                LinearLayout groupLayout = (LinearLayout) v.findViewById(R.id.newgroup_member_layout);
                View mView = (View) v.findViewById(R.id.newgroup_member_view);

                if (productLabel != null) {
                    productLabel.setText(product.getDisplay_name());
                }
                int height = Constant.screenHeight;
                int width = Constant.screenWidth;

                LinearLayout.LayoutParams addGrmLayout = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                addGrmLayout.width = width;
                addGrmLayout.height = (int) (height * 9 / 100);
                addGrmLayout.gravity = Gravity.CENTER;
                groupLayout.setLayoutParams(addGrmLayout);
                groupLayout.setGravity(Gravity.CENTER | Gravity.LEFT);

                LinearLayout.LayoutParams circularImage = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                circularImage.width = width * 17 / 100;
                circularImage.height = height * 7 / 100;
                circularImage.setMargins(width * 5 / 100, height * 1 / 2 * 100, width * 1 / 100, height * 1 / 2 * 100);
                circularImage.gravity = Gravity.CENTER | Gravity.LEFT;
                img.setLayoutParams(circularImage);

                LinearLayout.LayoutParams chatMsgParama = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                chatMsgParama.width = width * 68 / 100;
                chatMsgParama.height = (int) (height * 9 / 100);
                productLabel.setLayoutParams(chatMsgParama);
                productLabel.setGravity(Gravity.CENTER | Gravity.LEFT);
                productLabel.setPadding(width * 3 / 100, 0, 0, 0);

                LinearLayout.LayoutParams viewParama = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                viewParama.width = width;
                viewParama.height = (int) (height * 0.3 / 100);
                mView.setLayoutParams(viewParama);

                if (width >= 600) {
                    productLabel.setTextSize(16);
                } else if (width < 600 && width >= 480) {
                    productLabel.setTextSize(15);
                } else if (width < 480 && width >= 320) {
                    productLabel.setTextSize(12);
                } else if (width < 320) {
                    productLabel.setTextSize(10);
                }
                try {
                    System.gc();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inDither = true;
                    Bitmap bmp = BitmapFactory.decodeByteArray(
                            product.getPhoto_ts(), 0,
                            product.getPhoto_ts().length, options);
//                    img.setImageBitmap(new AvatarManager().roundCornerImage(
//                            bmp, 180));
                    ProfileRoundImg roundImageProfile = new ProfileRoundImg(bmp);
                    img.setImageDrawable(roundImageProfile);
                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                    // TODO: handle exception
                }
                // set status
                TextView status = (TextView) v.findViewById(R.id.status);

                try {
                    status.setText(product.getStatus());
                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                    // TODO: handle exceptionas
                    e.printStackTrace();
                }
            }
            return v;
        }

        @Override
        public Filter getFilter() {
            return nameFilter;
        }

    }

    private class CreateRoom extends AsyncTask<String, String, String> {

        String mem_list;
        JSONObject main_job = new JSONObject();
        JSONObject json_tags = new JSONObject();
        MessageGetSet msg = new MessageGetSet();
        String icon_url = "";

        @Override
        protected void onPreExecute() {

            progressdialog.show();

            room_subject = NewGroup_Info.txt_subject.getText().toString()
                    .trim();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            Date timestamp = new Date();
            int k = 0;
            String question_options = null;
            for (String string : NewGroup_Info.ar_list_option) {
                if (k == 0) {
                    question_options = string;
                } else {
                    question_options = question_options + "," + string;
                }
                k++;
            }

            try {

                json_tags.put(
                        Constant.GROUP_ADMIN,
                        KachingMeApplication.getUserID()
                                + KachingMeApplication.getHost());
                json_tags.put(Constant.GROUP_TYPE, ""
                        + NewGroup_Info.group_type);
                json_tags.put(Constant.GROUP_TOPIC,
                        NewGroup_Info.txt_questions.getText());
                json_tags.put(Constant.TOPIC_OPTION, question_options);
                json_tags.put(Constant.TIMESTAMP,
                        "" + System.currentTimeMillis() / 1000L);
                main_job.put("data", json_tags);
                Log.d(TAG, "JSON String::" + main_job.toString());
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
            Constant.printMsg("create room ::::: @conference."
                    + KachingMeApplication.getHost_12());
            grp_id = KachingMeApplication.getUserID() + "-" + timestamp.getTime()
                    + "@conference." + KachingMeApplication.getHost_12();

            // muc = TempConnectionEstablishment.MUC_MANAGER
            // .getMultiUserChat(grp_id);

            MultiUserChatManager multiUserChatManager = MultiUserChatManager
                    .getInstanceFor(TempConnectionService.connection);

            /* = multiUserChatManager
                    .getMultiUserChat(grp_id);*/

            MultiUserChat multiUserChat = TempConnectionService.MUC_MANAGER.getMultiUserChat(grp_id);

            // Constant.printMsg("BM Group ID::" + grp_id + " Subject::" +
            // room_subject);

            try {

                Constant.printMsg("FFFFFFFFFFFFF1");

                multiUserChat.create(room_subject);
                Constant.printMsg("FFFFFFFFFFFFF2");

                Form form = multiUserChat.getConfigurationForm();
                Form submitForm = form.createAnswerForm();
                submitForm.setAnswer("muc#roomconfig_roomname", room_subject);
                submitForm.setAnswer("muc#roomconfig_persistentroom", true);
                submitForm.setAnswer("muc#roomconfig_membersonly", true);
                submitForm.setAnswer("muc#roomconfig_allowinvites", true);
                submitForm.setAnswer("muc#roomconfig_moderatedroom", true);
                submitForm.setAnswer("muc#roomconfig_changesubject", true);
                submitForm.setAnswer("allow_query_users", true);
                submitForm.setAnswer("public_list", true);
                List<String> admin = new ArrayList<String>();
                admin.add(KachingMeApplication.getjid());
                submitForm.setAnswer("muc#roomconfig_roomdesc",
                        main_job.toString());
                multiUserChat.sendConfigurationForm(submitForm);
                multiUserChat.join(KachingMeApplication.getjid());

                SharedPreferences sp_1 = getActivity().getSharedPreferences(
                        KachingMeApplication.getPereference_label(),
                        Activity.MODE_PRIVATE);
                String Bookmarked_time = Utils.getBookmarkTime();
                Editor editor = sp_1.edit();
                editor.putString(Constant.LAST_REFRESH_TIME + "_" + grp_id,
                        Bookmarked_time);
                editor.commit();

                BookmarkManager bm = BookmarkManager
                        .getBookmarkManager(TempConnectionService.connection);

                bm.addBookmarkedConference(room_subject, grp_id, true,
                        Utils.getBookmarkTime(), "");

            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();

                Constant.printMsg("FFFFFFFFFFFFF3" + e.toString());
            }

            try {
                if (multiUserChat.isJoined()) {

                    Constant.printMsg("FFFFFFFFFFFFF4" + multiUserChat.isJoined());
                    Log.d("Room",
                            "Selected Member size::" + selected_users.size());

                    group_partcipant_getset = new GroupParticipantGetSet();
                    group_partcipant_getset.setAdmin(1);
                    group_partcipant_getset.setGjid(grp_id);
                    group_partcipant_getset.setJid(KachingMeApplication.getjid());
                    dbAdapter.addGroupMembers(group_partcipant_getset);

                    for (int j = 0; j < selected_users.size(); j++) {

                        if (j == 0) {
                            mem_list = selected_users.get(j).getJid();
                        } else {
                            mem_list = mem_list + ","
                                    + selected_users.get(j).getJid();
                        }

                        group_partcipant_getset = new GroupParticipantGetSet();
                        group_partcipant_getset.setAdmin(0);
                        group_partcipant_getset.setGjid(grp_id);
                        group_partcipant_getset.setJid(selected_users.get(j)
                                .getJid());
                        dbAdapter.addGroupMembers(group_partcipant_getset);

                        multiUserChat.grantOwnership(selected_users.get(j)
                                .getJid().toString());
                    }

                    for (int i = 0; i < selected_users.size(); i++) {

                        try {

                            multiUserChat.invite(
                                    selected_users.get(i).getJid(), "");
                            Log.d("MUC Create", "Invited Memebrs::"
                                    + selected_users.get(i).getJid());
                        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                            e.printStackTrace();
                            // TODO: handle exception
                        }

                    }

                }

            } catch (SmackException e) {
                e.printStackTrace();
                // TODO: handle exception
            } catch (XMPPErrorException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            msg.setData(room_subject);
            msg.setKey_from_me(0);
            msg.setKey_id("" + new Date().getTime());
            msg.setKey_remote_jid(grp_id);
            msg.setMedia_wa_type("9");
            msg.setNeeds_push(0);
            msg.setStatus(0);
            msg.setTimestamp(new Date().getTime());
            msg.setRemote_resource(KachingMeApplication.getUserID()
                    + KachingMeApplication.getHost());
            dbAdapter.setInsertMessages(msg);

            try {
                SharedPreferences sp = getActivity().getSharedPreferences(
                        KachingMeApplication.getPereference_label(),
                        Activity.MODE_PRIVATE);
                Editor editor = sp.edit();
                editor.putString(grp_id,
                        mem_list + "," + KachingMeApplication.getUserID()
                                + KachingMeApplication.getHost());
                editor.putString(
                        grp_id + "_admin",
                        KachingMeApplication.getUserID()
                                + KachingMeApplication.getHost());
                editor.putString(grp_id + "_group_type",
                        json_tags.getString(Constant.GROUP_TYPE));
                editor.putString(grp_id + "_group_question",
                        json_tags.getString(Constant.GROUP_TOPIC));

                if (json_tags.getString(Constant.GROUP_TYPE).equals("4")
                        || json_tags.getString(Constant.GROUP_TYPE)
                        .equals("5")) {
                    editor.putString(grp_id + "_group_question_options",
                            json_tags.getString(Constant.TOPIC_OPTION));
                }
                editor.commit();
            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
            }
            // mBoundService.setMUC_Listeners(muc);
            multiUserChat
                    .addMessageListener(TempConnectionService.muc_messageListener);
            multiUserChat
                    .addSubjectUpdatedListener(new MUC_SubjectChangeListener(
                            getActivity()));

            boolean success = (new File(KachingMeApplication.PROFILE_PIC_DIR))
                    .mkdirs();
            RequestParams request_params = new RequestParams();
            FileOutputStream fos;
            try {
                if (NewGroup_Info.img_byte == null) {
                    NewGroup_Info.img_byte = new Utils()
                            .getGroupRandomeIcon(getActivity());

                }

                fos = new FileOutputStream(new File(
                        KachingMeApplication.PROFILE_PIC_DIR + grp_id.split("@")[0]
                                + ".png"));
                fos.write(NewGroup_Info.img_byte);
                fos.close();
                request_params.put("uploaded_file", new File(
                        KachingMeApplication.PROFILE_PIC_DIR + grp_id.split("@")[0]
                                + ".png"));
                request_params.put("filename", grp_id.split("@")[0]);


                icon_url = KachingMeConfig.UPLOAD_GROUP_ICON_FOLDER_PNG_PHP + grp_id.split("@")[0] + ".png";

				/*
                 * byte[] randome=new
				 * Utils().getGroupRandomeIcon(getActivity()); Bitmap myBitmap =
				 * BitmapFactory.decodeByteArray( randome, 0, randome.length);
				 * FileOutputStream stream = new FileOutputStream(new
				 * File(KachingMeApplication
				 * .PROFILE_PIC_DIR+grp_id.split("@")[0]+".png"));
				 *
				 * ByteArrayOutputStream outstream = new
				 * ByteArrayOutputStream();
				 * myBitmap.compress(Bitmap.CompressFormat.PNG, 85, outstream);
				 * byte[] byteArray = outstream.toByteArray();
				 *
				 * stream.write(byteArray); stream.close();
				 */

            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(60000);
            client.post(KachingMeConfig.UPLOAD_GROUP_ICON_PHP,
                    request_params,
                    new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                        @Override
                        public void onFailure(int arg0, Header[] arg1,
                                              byte[] arg2, Throwable arg3) {
                            // TODO Auto-generated method stub
//                            Log.d(TAG, "Group icon onFailure::"
//                                    + new String(arg2));

                        }

                        @Override
                        public void onSuccess(int arg0, Header[] arg1,
                                              byte[] arg2) {
                            // TODO Auto-generated method stub
                            Log.d(TAG, "Group icon onSuccess::"
                                    + new String(arg2));
                        }

                    });

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            NewGroup_Info.img_byte = null;

            DatabaseHelper dbadapter = KachingMeApplication.getDatabaseAdapter();
            int msg_id = dbadapter.getLastMsgid_chat_grp(grp_id);

            try {

                if (!dbadapter.isjidExist(grp_id)) {

                    ContactsGetSet contact = new ContactsGetSet();
                    contact.setIs_niftychat_user(1);
                    contact.setJid(grp_id);
                    contact.setPhone_label(room_subject);
                    contact.setDisplay_name(room_subject);
                    contact.setPhoto_ts(new Utils()
                            .getGroupRandomeIcon(getActivity()));
                    dbadapter.insertContacts(contact);
                    dbadapter.setInsertChat_list(grp_id, msg_id,
                            json_tags.getString(Constant.TIMESTAMP));
                }
            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
            }

            SendWeb_Group.Add_Group_on_web(getActivity(), grp_id, room_subject,
                    KachingMeApplication.getjid(),
                    mem_list + "," + KachingMeApplication.getUserID()
                            + KachingMeApplication.getHost(),
                    "" + msg.getTimestamp() / 1000, icon_url);

            Intent login_broadcast = new Intent("chat");
            login_broadcast.putExtra("jid", "" + grp_id);
            getActivity().sendBroadcast(login_broadcast);
            try {
                progressdialog.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Constant.mFromMemberList = true;
            Intent i = new Intent(getActivity(), SliderTesting.class);
            startActivity(i);
            getActivity().finish();

        }

    }


}
