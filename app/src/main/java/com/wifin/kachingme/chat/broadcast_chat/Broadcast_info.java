/*
* @author Gokul & dilip
*
* @usage -  This class is used to display broadcast information
*
*
* */

package com.wifin.kachingme.chat.broadcast_chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.SherlockBaseActivity;
import com.wifin.kachingme.async_tasks.ConcurrentAsyncTaskExecutor;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.ProfileRoundImg;
import com.wifin.kachingme.util.SelectContactList;

import java.util.ArrayList;
import java.util.Date;

public class Broadcast_info extends SherlockBaseActivity implements
        OnClickListener {

    public static ArrayList<ContactsGetSet> contact_list;
    ImageView img_avatar;
    TextView txt_subject, txt_member;
    TextView btn_edit_sunject;
    ListView list;
    Button btn_delete;
    String jid;
    DatabaseHelper dbAdapter;
    UserListAdapter adapter;
    String mem_list, admin;
    String[] mem_ar;
    Boolean Is_Admin = false;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    Cursor cursor;
    int height = 0;
    int width = 0;
    BroadcastReceiver lastseen_event = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("update_list")) {
                ConcurrentAsyncTaskExecutor.executeConcurrently(new MyAsync());
            } else if (intent.getAction().equals("network_status")) {
                if (KachingMeApplication.getIsNetAvailable()) {

                    ConcurrentAsyncTaskExecutor.executeConcurrently(new MyAsync());
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broadcast_info);

        img_avatar = (ImageView) findViewById(R.id.img_avatar);
        txt_subject = (TextView) findViewById(R.id.txt_subject);
        txt_member = (TextView) findViewById(R.id.txt_members);
        btn_edit_sunject = (TextView) findViewById(R.id.btn_edit_subjects);
        btn_edit_sunject.setOnClickListener(this);

        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);
        list = (ListView) findViewById(R.id.list_experience);
        dbAdapter = KachingMeApplication.getDatabaseAdapter();

        View view = new View(this);
        list.addFooterView(view);
        screenArrangement();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jid = bundle.getString("jid");
            txt_subject.setText(bundle.getString("name"));
            getSupportActionBar().setTitle(
                    getResources().getString(R.string.broadcast_list_info));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setIcon(R.drawable.ic_broadcast);
        }

        sp = getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        editor = sp.edit();

        Is_Admin = true;

        registerForContextMenu(list);

        cursor = dbAdapter.getChat_list();

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                ContactsGetSet contact = (ContactsGetSet) arg0.getAdapter()
                        .getItem(arg2);

                boolean is_chat_exist = false;

                if (cursor.moveToFirst()) {
                    do {
                        if (contact.getJid().equals(cursor.getString(8))) {
                            Log.e("Muc_Chat Info",
                                    "Chat exist::" + cursor.getString(8));

                            Intent intent = new Intent(Broadcast_info.this,
                                    ChatTest.class);
                            intent.putExtra("jid", cursor.getString(8));
                            intent.putExtra("name", cursor.getString(1));
                            intent.putExtra("is_owner", "" + cursor.getInt(12));
                            intent.putExtra("avatar", cursor.getBlob(2));
                            if (cursor.getInt(11) == 1) {
                                is_chat_exist = true;
                                intent.putExtra("IS_SECRET_CHAT", false);

                                startActivity(intent);
                            }

                        }

                    } while (cursor.moveToNext());
                }

                if (!is_chat_exist) {

                    Intent intent = new Intent(Broadcast_info.this, ChatTest.class);
                    intent.putExtra("jid", contact.getJid());
                    intent.putExtra("name", contact.getDisplay_name());
                    intent.putExtra("is_owner", "" + 1);
                    intent.putExtra("avatar", contact.getPhoto_ts());
                    intent.putExtra("IS_SECRET_CHAT", false);
                    startActivity(intent);
                }

                Log.e("Muc_Info", "JID::" + contact.getJid());
            }
        });

        ConcurrentAsyncTaskExecutor.executeConcurrently(new MyAsync());

    }

    private void screenArrangement() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        Constant.screenWidth = width;
        Constant.screenHeight = height;

        LinearLayout.LayoutParams mTopLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mTopLayoutParams.width = (int) width;
        mTopLayoutParams.height = (int) height * 15 / 100;

        LinearLayout.LayoutParams mSeperatorParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mSeperatorParams.width = (int) (width * 0.5 / 100);
        mSeperatorParams.height = (int) height * 8 / 100;
        mSeperatorParams.gravity = Gravity.CENTER;

        LinearLayout.LayoutParams mGroupImgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mGroupImgParams.width = (int) width * 23 / 100;
        mGroupImgParams.height = (int) width * 23 / 100;
        mGroupImgParams.leftMargin = width * 2 / 100;
        mGroupImgParams.gravity = Gravity.CENTER;

        LinearLayout.LayoutParams mGroupEditParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mGroupEditParams.width = (int) width * 8 / 100;
        mGroupEditParams.height = (int) width * 8 / 100;
        mGroupEditParams.gravity = Gravity.CENTER;
        mGroupEditParams.leftMargin = width * 3 / 100;
        btn_edit_sunject.setLayoutParams(mGroupEditParams);


        LinearLayout.LayoutParams mGroupMemCountParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mGroupMemCountParams.width = (int) width;
        mGroupMemCountParams.height = (int) height * 8 / 100;
        mGroupMemCountParams.leftMargin = width * 3 / 100;
        mGroupMemCountParams.gravity = Gravity.CENTER;
        txt_member.setLayoutParams(mGroupMemCountParams);
        txt_member.setGravity(Gravity.CENTER | Gravity.LEFT);

        LinearLayout.LayoutParams mGroupLabelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mGroupLabelParams.width = (int) width * 76 / 100;
        mGroupLabelParams.height = (int) height * 4 / 100;
        mGroupLabelParams.leftMargin = width * 2 / 100;
        mGroupLabelParams.topMargin = height * 1 / 100;
        mGroupLabelParams.gravity = Gravity.CENTER;
        txt_subject.setLayoutParams(mGroupLabelParams);
        txt_subject.setGravity(Gravity.LEFT | Gravity.CENTER);

        LinearLayout.LayoutParams meditLabelParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        meditLabelParams1.width = (int) width * 10 / 100;
        meditLabelParams1.height = (int) width * 10 / 100;
        meditLabelParams1.leftMargin = width * 2 / 100;
        meditLabelParams1.gravity = Gravity.CENTER;

        LinearLayout.LayoutParams mGroupDeleteBtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mGroupDeleteBtnParams.width = (int) width * 50 / 100;
        mGroupDeleteBtnParams.height = (int) height * 7 / 100;
        mGroupDeleteBtnParams.bottomMargin = height * 5 / 100;
        mGroupDeleteBtnParams.topMargin = width * 5 / 100;
        mGroupDeleteBtnParams.gravity = Gravity.CENTER;
        btn_delete.setLayoutParams(mGroupDeleteBtnParams);
        btn_delete.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams mheaderLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mheaderLayoutParams.height = (int) height * 90 / 100;
        mheaderLayoutParams.gravity = Gravity.CENTER;

        if (width >= 600) {

            txt_subject.setTextSize(17);
            txt_member.setTextSize(17);
            btn_delete.setTextSize(17);

        } else if (width < 600 && width >= 480) {

            txt_subject.setTextSize(16);
            txt_member.setTextSize(16);
            btn_delete.setTextSize(16);

        } else if (width < 480 && width >= 320) {

            txt_subject.setTextSize(14);
            txt_member.setTextSize(14);
            btn_delete.setTextSize(14);

        } else if (width < 320) {

            txt_subject.setTextSize(12);
            txt_member.setTextSize(12);
            btn_delete.setTextSize(12);

        }

    }

    @Override
    protected void onResume() {

        IntentFilter filter = new IntentFilter();
        filter.addAction("update_list");
        filter.addAction("network_status");

        registerReceiver(lastseen_event, filter);
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        // merlin.unbind();
    }

    @Override
    protected void onDestroy() {

        unregisterReceiver(lastseen_event);

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_delete:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(
                        getResources().getString(R.string.delete_broadcast_list))
                        .setMessage(
                                getResources().getString(
                                        R.string.are_you_sure_delete_list))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(getResources().getString(R.string.yes),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        // Remove_Meber(KachingMeApplication.getUserID()+KachingMeApplication.getHost());
                                        Delete_Local();
                                        Intent intent = new Intent(
                                                Broadcast_info.this, SliderTesting.class);
                                        intent.putExtra("is_group_tab", "false");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        startActivity(intent);
                                        finish();
                                    }
                                })
                        .setNegativeButton(getResources().getString(R.string.No),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub

                                    }
                                }).show();

                break;
            case R.id.btn_edit_subjects:

                setStatus();

                break;
            default:
                break;
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        Adapter adapter = list.getAdapter();
        Object item = adapter.getItem(info.position);
        ContactsGetSet contact = contact_list.get(info.position);

        menu.setHeaderTitle("Choose");
        if (Is_Admin) {
            if (contact.getDisplay_name() != null
                    || contact.getDisplay_name().equals("")) {
                menu.add(
                        0,
                        info.position,
                        1,
                        String.format(
                                getResources().getString(
                                        R.string.remove_from_list),
                                contact.getDisplay_name()));
            } else {
                menu.add(0, info.position, 1, String.format(getResources()
                        .getString(R.string.remove_from_list), contact.getJid()
                        .split("@")[0]));
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Is_Admin) {
            getMenuInflater().inflate(R.menu.group_info, menu);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.muc_info_add:
                Intent intent = new Intent(this, SelectContactList.class);
                startActivityForResult(intent, 0);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {

        if (item.getOrder() == 1) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.remove_member))
                    .setMessage(
                            String.format(
                                    getResources()
                                            .getString(
                                                    R.string.are_you_sure_you_want_to_remove),
                                    item.getTitle().toString().toLowerCase()))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // Yes button clicked, do something

                                    Remove_Meber(item.getItemId());

                                }
                            })

                    .setNegativeButton(getResources().getString(R.string.No),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                }
                            }) // Do nothing on no
                    .show();

            //

        }

        return true;

    }

    public void Remove_Meber(int positon) {

        try {

            int i = 0;
            for (int j = 0; j < contact_list.size(); j++) {
                if (positon != j) {

                    if (i == 0) {
                        mem_list = contact_list.get(j).getJid();
                    } else {
                        mem_list = mem_list + ","
                                + contact_list.get(j).getJid();
                    }
                    i++;
                    Log.d("Broadcast_info", "Owner::"
                            + contact_list.get(j).getJid());
                }

            }
            String packate_id = "" + new Date().getTime();

            MessageGetSet msg1 = new MessageGetSet();
            msg1.setData(contact_list.get(positon).getDisplay_name() + " "
                    + getResources().getString(R.string.removed_from_list));
            msg1.setKey_from_me(0);
            msg1.setKey_id(packate_id);
            msg1.setKey_remote_jid(jid);
            msg1.setMedia_size(2);
            msg1.setNeeds_push(0);
            msg1.setStatus(0);
            msg1.setTimestamp(new Date().getTime());
            msg1.setMedia_wa_type("10");
            msg1.setRemote_resource(contact_list.get(positon).getJid());
            msg1.setIs_sec_chat(1);
            dbAdapter.setInsertMessages(msg1);
            editor.putString(jid, mem_list);
            editor.commit();

        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            e.printStackTrace();
            // TODO: handle exception
        }

        ConcurrentAsyncTaskExecutor.executeConcurrently(new MyAsync());
    }

    public void Add_Meber(String mem_jid) {

        try {

            int i = 0;
            for (int j = 0; j < contact_list.size(); j++) {
                if (i == 0) {
                    mem_list = contact_list.get(j).getJid();
                } else {
                    mem_list = mem_list + "," + contact_list.get(j).getJid();
                }
                i++;

            }

            mem_list = mem_list + "," + mem_jid;

            ContactsGetSet con = dbAdapter.getContact(mem_jid);
            MessageGetSet msg = new MessageGetSet();
            msg.setData(con.getDisplay_name() + " "
                    + getResources().getString(R.string.added_to_the_list));
            msg.setKey_from_me(0);
            msg.setKey_id("" + new Date().getTime());
            msg.setKey_remote_jid(jid);
            msg.setNeeds_push(0);
            msg.setStatus(0);
            msg.setTimestamp(new Date().getTime());
            msg.setRemote_resource(mem_jid);
            msg.setMedia_wa_type("" + 9);
            msg.setIs_sec_chat(1);
            dbAdapter.setInsertMessages(msg);

            editor.putString(jid, mem_list);
            editor.commit();

        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            e.printStackTrace();
            // TODO: handle exception
        }

        ConcurrentAsyncTaskExecutor.executeConcurrently(new MyAsync());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == 1) {
                if (data != null) {
                    String jid = data.getStringExtra("jid");
                    Log.d("Activity Result", "Selected jid::" + jid);
                    Add_Meber(jid);
                }
            }
        }

    }

    public void Delete_Local() {
        editor.remove(jid);
        editor.remove(jid + "_admin");

        editor.commit();

        dbAdapter.setDeleteContact(jid);
        dbAdapter.setDeleteMessages(jid);
        dbAdapter.setDeleteChatList(jid);

    }

    public void setStatus() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(getResources().getString(R.string.list_name));

        final EditText input = new EditText(this);

        input.setText(txt_subject.getText().toString());
        input.setSelectAllOnFocus(true);
        alert.setView(input);

        alert.setPositiveButton(getResources().getString(R.string.Ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        try {

                            txt_subject.setText(value);

                            dbAdapter.setUpdateSubject(jid, value);
                        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                            e.printStackTrace();
                            // TODO: handle exception
                        }
                    }
                });

        alert.setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

        alert.show();
    }

    private class MyAsync extends AsyncTask<String, String, String> {
        Boolean count_admin = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            count_admin = false;
            contact_list = new ArrayList<ContactsGetSet>();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            contact_list = new ArrayList<ContactsGetSet>();

            mem_list = sp.getString(jid, "");

            Log.d("MUC info", "Admin::" + admin);
            if (!mem_list.equals("")) {
                mem_ar = mem_list.split(",");
            }

            for (String string : mem_ar) {
                Log.d("MUC_info", "Member ID:" + string);
                ContactsGetSet contact = dbAdapter.getContact(string);

                contact_list.add(contact);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("MUC_info", "List Size:" + contact_list.size());
            adapter = new UserListAdapter(Broadcast_info.this,
                    R.layout.muc_mem_list_items, contact_list);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            super.onPostExecute(result);

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

            // set name

            TextView name = (TextView) container.findViewById(R.id.name);
            TextView txt_aff = (TextView) container.findViewById(R.id.profile);
            ImageView img = (ImageView) container
                    .findViewById(R.id.avtarimg);
            name.setText(user.getDisplay_name());
            txt_aff.setText(user.getStatus());
            if (user.getJid().equals(admin)) {
                txt_aff.setVisibility(View.VISIBLE);
            }

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
                ProfileRoundImg roundImgMember = new ProfileRoundImg(bmp);
                img.setImageDrawable(roundImgMember);

            } catch (OutOfMemoryError e) {
                android.util.Log.e("Map", "Tile Loader (241) Out Of Memory Error " + e.getLocalizedMessage());
                System.gc();
            } catch (Exception e) {

            }

            LinearLayout.LayoutParams mGroupTextItemParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mGroupTextItemParams.width = (int) width * 45 / 100;
            mGroupTextItemParams.gravity = Gravity.CENTER;
            name.setLayoutParams(mGroupTextItemParams);

            LinearLayout.LayoutParams mGroupProfileTextItemParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mGroupProfileTextItemParams.height = (int) height * 5 / 100;
            mGroupProfileTextItemParams.gravity = Gravity.CENTER;
            txt_aff.setLayoutParams(mGroupProfileTextItemParams);
            txt_aff.setGravity(Gravity.CENTER | Gravity.RIGHT);

            LinearLayout.LayoutParams mGroupImgItemParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mGroupImgItemParams.width = (int) width * 25 / 100;
            mGroupImgItemParams.height = (int) width * 25 / 100;
            mGroupImgItemParams.gravity = Gravity.CENTER;
            img.setLayoutParams(mGroupImgItemParams);
            img.setPadding(width * 3 / 100, width * 3 / 100, width * 3 / 100, width * 3 / 100);

            if (width >= 600) {

                name.setTextSize(17);
                txt_aff.setTextSize(17);

            } else if (width < 600 && width >= 480) {
                name.setTextSize(16);
                txt_aff.setTextSize(16);

            } else if (width < 480 && width >= 320) {
                name.setTextSize(14);
                txt_aff.setTextSize(14);


            } else if (width < 320) {
                name.setTextSize(12);
                txt_aff.setTextSize(12);

            }

            return container;
        }

    }
}
