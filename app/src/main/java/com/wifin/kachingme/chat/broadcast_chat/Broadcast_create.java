/*
* @author Gokul & dilip
*
* @usage -  This class is used to create broadcast group
*
*
* */

package com.wifin.kachingme.chat.broadcast_chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.SherlockBaseActivity;
import com.wifin.kachingme.chat.muc_chat.NewGroup_MemberList;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.MultiselectMembers;
import com.wifin.kachingme.util.ProfileRoundImg;

import java.util.ArrayList;
import java.util.Date;

import a_vcard.android.util.Log;

public class Broadcast_create extends SherlockBaseActivity {

    public static ArrayList<ContactsGetSet> selected_users;
    private static String TAG = Broadcast_create.class.getSimpleName();
    private AutoCompleteTextView txt_auto;
    private ListView list;
    private DatabaseHelper dbAdapter;
    private ArrayList<ContactsGetSet> users;
    private UserListAdapter select_adapter;
    private ImageButton btn_multi_select;
    private EditText txt_broadcastlist_name;
    private String jid;
    private SharedPreferences sp;
    private Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.broadcast_create);

        NewGroup_MemberList.selected_users = new ArrayList<ContactsGetSet>();

        txt_auto = (AutoCompleteTextView) findViewById(R.id.txt_autocomplete);
        list = (ListView) findViewById(R.id.list_experience);
        btn_multi_select = (ImageButton) findViewById(R.id.btn_multi_select);
        txt_broadcastlist_name = (EditText) findViewById(R.id.txt_broadcastlist_name);

        try {
            Constant.typeFace(this, txt_auto);
            Constant.typeFace(this, txt_broadcastlist_name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dbAdapter = KachingMeApplication.getDatabaseAdapter();
        users = new ArrayList<ContactsGetSet>();
        selected_users = new ArrayList<ContactsGetSet>();
        users = dbAdapter.getContacts();
        AutoAdapter adapter = new AutoAdapter(this, R.layout.contactlistitem,
                users);
        txt_auto.setAdapter(adapter);

        select_adapter = new UserListAdapter(this, R.layout.add_grm_mem_item,
                selected_users);
        list.setAdapter(select_adapter);
        View view = new View(this);
        list.addFooterView(view);

        sp = getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        editor = sp.edit();

        btn_multi_select.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in = new Intent(Broadcast_create.this,
                        MultiselectMembers.class);
                startActivityForResult(in, 12);
            }
        });

        txt_auto.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                boolean is_selected = false;
                for (int j = 0; j < selected_users.size(); j++) {

                    if (selected_users.get(j).getJid()
                            .equals(users.get(position).getJid())) {
                        is_selected = true;

                    }
                }

                if (is_selected == false) {
                    for (int j = 0; j < users.size(); j++) {

                        if (users.get(j).getJid().equals(users.get(position))) {
                            selected_users.add(users.get(j));
                            select_adapter.notifyDataSetChanged();
                        }
                    }
                }

                selected_users.add(users.get(position));
                select_adapter.notifyDataSetChanged();
                txt_auto.setText("");
                try {
                    users = dbAdapter.getContacts();
                    try {
                        for (int i = 0; i < Broadcast_create.selected_users.size(); i++) {

                            for (int j = 0; j < users.size(); j++) {

                                if (users.get(j).getJid().equalsIgnoreCase(Broadcast_create.selected_users.get(i).getJid())) {
                                    users.remove(j);
                                }

                            }

                        }
                    } catch (Exception e) {
                    }
                    AutoAdapter adapter = new AutoAdapter(Broadcast_create.this, R.layout.contactlistitem,
                            users);
                    txt_auto.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        jid = "" + new Date().getTime() + "" + KachingMeApplication.getHost();

    }

    @Override
    protected void onResume() {

        try {
            users = dbAdapter.getContacts();
            try {
                for (int i = 0; i < Broadcast_create.selected_users.size(); i++) {

                    for (int j = 0; j < users.size(); j++) {

                        if (users.get(j).getJid().equalsIgnoreCase(Broadcast_create.selected_users.get(i).getJid())) {
                            users.remove(j);
                        }

                    }

                }
            } catch (Exception e) {
                Constant.printMsg("dfsmfsa111" + e.toString());
            }
            AutoAdapter adapter = new AutoAdapter(this, R.layout.contactlistitem,
                    users);
            txt_auto.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        menu.add(getResources().getString(R.string.create)).setShowAsAction(
                MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (txt_broadcastlist_name.getText().toString().length() > 0) {
            String members = null;
            ContactsGetSet contact = new ContactsGetSet();
            contact.setDisplay_name(txt_broadcastlist_name.getText().toString());
            contact.setIs_niftychat_user(3);
            contact.setJid(jid);
            contact.setUnseen_msg_count(0);
            contact.setRaw_contact_id("" + new Date().getTime());
            dbAdapter.insertContacts(contact);

            for (int j = 0; j < selected_users.size(); j++) {

                String name = dbAdapter.getDisplay_name(selected_users.get(j)
                        .getJid());

                MessageGetSet msg = new MessageGetSet();
                msg.setData(name + " "
                        + getResources().getString(R.string.added_to_the_list));
                msg.setKey_from_me(0);
                msg.setKey_id("" + new Date().getTime());
                msg.setKey_remote_jid(jid);
                msg.setNeeds_push(0);
                msg.setStatus(0);
                msg.setTimestamp(new Date().getTime());
                msg.setRemote_resource(selected_users.get(j).getJid());
                msg.setMedia_wa_type("" + 9);
                msg.setIs_sec_chat(1);
                dbAdapter.setInsertMessages(msg);

                if (j == 0) {
                    members = selected_users.get(j).getJid();
                } else {
                    members = members + "," + selected_users.get(j).getJid();
                }
            }
            try {
                users = dbAdapter.getContacts();
                try {
                    for (int i = 0; i < Broadcast_create.selected_users.size(); i++) {

                        for (int j = 0; j < users.size(); j++) {

                            if (users.get(j).getJid().equalsIgnoreCase(Broadcast_create.selected_users.get(i).getJid())) {
                                users.remove(j);
                            }

                        }

                    }
                } catch (Exception e) {
                }
                AutoAdapter adapter = new AutoAdapter(Broadcast_create.this, R.layout.contactlistitem,
                        users);
                txt_auto.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Constant.printMsg("Broadcast Members List::" + members);

            editor.putString(jid, members);
            editor.commit();

            int unseen_msg = dbAdapter.getunseen_msg(jid, 1);
            int msg_id = dbAdapter.getLastMsgid_chat(jid, 1);
            dbAdapter.setUpdateContact_unseen_msg_chat(jid, unseen_msg, 1);

            if (dbAdapter.isExistinChatList_chat(jid, 1)) {
                dbAdapter.setUpdateChat_lits_chat(jid, msg_id, 1);
            } else {
                dbAdapter.setInsertChat_list_chat(jid, msg_id, 1);
            }

            if (members == null) {
                Toast.makeText(getApplicationContext(), "No member selected", Toast.LENGTH_LONG).show();
            }

            Intent intent = new Intent(Broadcast_create.this, SliderTesting.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Please enter broadcast name", Toast.LENGTH_LONG).show();

        }
        return super.onOptionsItemSelected(item);
    }

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
                                            Broadcast_create.this,
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
                                        Broadcast_create.this,
                                        R.layout.add_grm_mem_item,
                                        selected_users);
                                list.setAdapter(select_adapter);
                                select_adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                try {
                    users = dbAdapter.getContacts();
                    try {
                        for (int i = 0; i < Broadcast_create.selected_users.size(); i++) {

                            for (int j = 0; j < users.size(); j++) {

                                if (users.get(j).getJid().equalsIgnoreCase(Broadcast_create.selected_users.get(i).getJid())) {
                                    users.remove(j);
                                }

                            }

                        }
                    } catch (Exception e) {
                        Constant.printMsg("dfsmfsa111" + e.toString());
                    }
                    AutoAdapter adapter = new AutoAdapter(Broadcast_create.this, R.layout.contactlistitem,
                            users);
                    txt_auto.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private class UserListAdapter extends ArrayAdapter<ContactsGetSet> {
        ArrayList<ContactsGetSet> list;
        Context context;
        private int resource;

        public UserListAdapter(Context context, int textViewResourceId,
                               ArrayList<ContactsGetSet> objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            this.resource = textViewResourceId;

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
            name.setText(user.getDisplay_name());

            Bitmap bmp = null;

            System.gc();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;
            options.inSampleSize = 2;
            try {
                bmp = BitmapFactory.decodeByteArray(user.getPhoto_ts(),
                        0, user.getPhoto_ts().length, options);
                ProfileRoundImg mSenderImage = new ProfileRoundImg(bmp);
                img.setImageDrawable(mSenderImage);

            } catch (OutOfMemoryError e) {
                android.util.Log.e("Map", "Tile Loader (241) Out Of Memory Error " + e.getLocalizedMessage());
                System.gc();
            } catch (Exception e) {

            }


            img_remove.setTag(position);
            img_remove.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    selected_users.remove(position);
                    select_adapter.notifyDataSetChanged();
                    try {
                        users = dbAdapter.getContacts();
                        try {
                            for (int i = 0; i < Broadcast_create.selected_users.size(); i++) {

                                for (int j = 0; j < users.size(); j++) {

                                    if (users.get(j).getJid().equalsIgnoreCase(Broadcast_create.selected_users.get(i).getJid())) {
                                        users.remove(j);
                                    }

                                }

                            }
                        } catch (Exception e) {
                            Constant.printMsg("dfsmfsa111" + e.toString());
                        }
                        AutoAdapter adapter = new AutoAdapter(Broadcast_create.this, R.layout.contactlistitem,
                                users);
                        txt_auto.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


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
                if (productLabel != null) {
                    productLabel.setText(product.getDisplay_name());
                }
                int width = Constant.screenWidth;
                int height = Constant.screenHeight;
                try {
                    ImageView img = (ImageView) v.findViewById(R.id.avtarimg);
                    LinearLayout.LayoutParams circularImage = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    circularImage.width = width * 15 / 100;
                    circularImage.height = height * 8 / 100;
                    circularImage.setMargins(5, 5, 0, 5);
                    img.setLayoutParams(circularImage);


                    Bitmap bmp = null;
                    System.gc();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inDither = true;
                    options.inSampleSize = 2;
                    try {
                        bmp = BitmapFactory.decodeByteArray(product.getPhoto_ts(), 0,
                                product.getPhoto_ts().length, options);
                        ProfileRoundImg roundImgMember = new ProfileRoundImg(bmp);
                        img.setImageDrawable(roundImgMember);

                    } catch (OutOfMemoryError e) {
                        android.util.Log.e("Map", "Tile Loader (241) Out Of Memory Error " + e.getLocalizedMessage());
                        System.gc();
                    } catch (Exception e) {

                    }


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
}
