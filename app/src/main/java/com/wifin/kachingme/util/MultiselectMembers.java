package com.wifin.kachingme.util;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuCompat;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.SherlockBaseActivity;
import com.wifin.kachingme.chat.broadcast_chat.Broadcast_create;
import com.wifin.kachingme.chat.muc_chat.NewGroup_MemberList;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.ContactsGetSet;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class MultiselectMembers extends SherlockBaseActivity {

    public static ProgressDialog myPd_ring = null;
    public boolean isSearched = false;
    ActionMode mMode;
    DatabaseHelper dbAdapter;
    ArrayList<ContactsGetSet> users;
    ListView lstview;
    MyCustomAdapter adapter;
    Handler h;
    Thread refresh;
    String selectedid = null;
    String jid = null;
    EditText searchview;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiselect_memebers);

        RecyclerView contactList_recyclerView = (RecyclerView) findViewById(R.id.contactList_recyclerView);
        contactList_recyclerView.setVisibility(View.GONE);

        lstview = (ListView) findViewById(R.id.list_experience);
        lstview.setVisibility(View.VISIBLE);
        searchview = (EditText) findViewById(R.id.serchEdit);

        myPd_ring = new ProgressDialog(this);
        myPd_ring.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        myPd_ring.setMessage("Updating...");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            jid = bundle.getString("jid");
        }

        dbAdapter = KachingMeApplication.getDatabaseAdapter();
        users = new ArrayList<ContactsGetSet>();

        if (jid != null) {

            users = dbAdapter.get_Select_Group_Contacts(jid);
            Constant.printMsg("GGGGSSS" + users);

        } else {
            users = dbAdapter.getContacts();
            Constant.printMsg("GGGGSSS1111" + users);


        }

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


        adapter = new MyCustomAdapter(this, R.layout.multi_select_member_item,
                users);

        lstview.setAdapter(adapter);

        View view = new View(this);
        lstview.addFooterView(view);
        searchview.setFocusable(false);

        searchview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                searchview.requestFocus();
                searchview.setFocusable(true);
                searchview.setFocusableInTouchMode(true);
                return false;
            }
        });

        searchview.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                // When user changed the Text
                // DailySearch.this.arrayAutoListAdapter.getFilter().filter(cs);
                if (cs.length() > 0) {
                    getSearchText(String.valueOf(cs));
                } else {
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

        lstview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        // menu.add("Done").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        Constant.printMsg("dfsmfsa12312312");
        getMenuInflater().inflate(R.menu.add_search_menu, menu);

        SearchView searchView = new SearchView(getSupportActionBar()
                .getThemedContext());
        searchView.setQueryHint("Search for name");
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // Constant.printMsg("Text:" + s);
                int textlength = s.length();
                getSearchText(s);
                return false;
            }
        });
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo info = searchManager
                .getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(info);
        MenuItem item_search = menu.findItem(R.id.menu_search);
        item_search.setActionView(searchView);

        MenuCompat.setShowAsAction(menu.add("Done"),
                MenuItem.SHOW_AS_ACTION_IF_ROOM);
        Constant.printMsg("dfsmfsa123123123333");
        return true;
    }

    protected void getSearchText(String s) {
        // TODO Auto-generated method stub

        ArrayList<ContactsGetSet> list = new ArrayList<ContactsGetSet>();
        // list = null;
        if (s != null && s.length() > 0) {
            // Constant.printMsg("HIIIII00000");
            Constant.printMsg("search ::::::::: " + s);
            for (int i = 0; i < users.size(); i++) {
                ContactsGetSet dc = new ContactsGetSet();
                // if
                // (mListDailySearch.get(i).getDoctorChemistName().contains(ch))
                // {

                if (Pattern.compile(Pattern.quote(s), Pattern.CASE_INSENSITIVE)
                        .matcher(users.get(i).getDisplay_name()).find()) {
                    // Constant.printMsg("Hiiiiiii");
                    dc = users.get(i);
                    list.add(dc);
                }

            }

        } else {
            //users = dbAdapter.getContacts();
            list = users;

        }

        // users=new ArrayList<contactsGetSet>();
        adapter = new MyCustomAdapter(this, R.layout.multi_select_member_item,
                list);

        lstview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (isSearched) {
            sendMemberList();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //  Constant.printMsg("Multi select : " + mMemberCount+"  "+searchView.isIconified() );

        //Done button clicked...


        if (Connectivity.isOnline(MultiselectMembers.this)) {
            isSearched = true;

            getSearchText(null);
        } else {
            new AlertUtils().Toast_call(MultiselectMembers.this, getResources()
                    .getString(R.string.no_internet_connection));
        }

        return super.onOptionsItemSelected(item);
    }


    public void sendMemberList() {
        StringBuffer responseText = new StringBuffer();

        ArrayList<ContactsGetSet> stateList = adapter.stateList;

        for (int i = 0; i < stateList.size(); i++) {
            ContactsGetSet con = stateList.get(i);

            if (i == 0) {
                if (con.isIs_checked()) {
                    responseText.append(stateList.get(i).getJid());
                }
            } else {
                if (con.isIs_checked()) {
                    responseText.append("," + stateList.get(i).getJid());
                }
            }
        }

        Constant.printMsg("Multi select" + responseText);

        // myPd_ring.show();

        Intent intent = new Intent();
        intent.putExtra("jid", "" + responseText);

        setResult(1, intent);
        finish();
    }

    private class MyCustomAdapter extends ArrayAdapter<ContactsGetSet> {

        private ArrayList<ContactsGetSet> stateList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<ContactsGetSet> stateList) {
            super(context, textViewResourceId, stateList);
            this.stateList = new ArrayList<ContactsGetSet>();
            this.stateList.addAll(stateList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

			/*
             * if (convertView == null) {
			 */
            Constant.printMsg("dfsmfsa12312312qqqq");
            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(R.layout.multi_select_member_item, null);

            holder = new ViewHolder();
            holder.txt_name = (TextView) convertView.findViewById(R.id.name);
            holder.txt_status = (TextView) convertView
                    .findViewById(R.id.status);
            holder.avatarimage = (ImageView) convertView
                    .findViewById(R.id.avtarimg);
            holder.name = (CheckBox) convertView.findViewById(R.id.is_check);

            holder.name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    ContactsGetSet _state = (ContactsGetSet) cb.getTag();

                    _state.setIs_checked(cb.isChecked());
                }
            });
            Constant.printMsg("dfsmfsa12312312212");
            convertView.setTag(holder.name);
            Constant.printMsg("dfsmfsa12312312sasds");
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v.getTag();
                    if (cb.isChecked()) {
                        cb.setChecked(false);
                    } else {
                        cb.setChecked(true);

                    }

                    // CheckBox cb = (CheckBox) v;
                    ContactsGetSet _state = (ContactsGetSet) cb.getTag();

                    _state.setIs_checked(cb.isChecked());
                }
            });

			/*
             * } else { holder = (ViewHolder) convertView.getTag(); }
			 */
            ContactsGetSet state = stateList.get(position);

            // holder.code.setText(" (" + state.getCode() + ")");
            // holder.name.setText(state.getName());
            holder.txt_name.setText(state.getDisplay_name());
            holder.txt_status.setText(state.getStatus());
            holder.name.setChecked(state.isIs_checked());
            Constant.printMsg("dfsmfsa12312312afd");
            holder.name.setTag(state);

            Bitmap bmp = null;

            System.gc();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;
            options.inSampleSize = 2;
            try {
                bmp =BitmapFactory.decodeByteArray(state.getPhoto_ts(),
                        0, state.getPhoto_ts().length,options);
                ProfileRoundImg roundImgMember = new ProfileRoundImg(bmp);
                holder.avatarimage.setImageDrawable(roundImgMember);

            } catch (OutOfMemoryError e) {
                android.util.Log.e("Map", "Tile Loader (241) Out Of Memory Error " + e.getLocalizedMessage());
                System.gc();
            }catch (Exception e){

            }
            return convertView;
        }

        private class ViewHolder {
            TextView txt_name, txt_status;
            CheckBox name;
            ImageView avatarimage;
        }

    }

}

