package com.wifin.kachingme.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.AlertUtils;
import com.wifin.kachingme.util.AvatarManager;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.KachingMeConfig;

import org.apache.http.Header;

import java.util.ArrayList;

//import cz.msebera.android.httpclient.Header;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;

public class LockScreen extends HeaderActivity {
    String status_lock;
    ListView list_lock;
    ArrayList<String> lock_list = new ArrayList<String>();
    ArrayList<String> jid_list = new ArrayList<String>();

    Dbhelper db;
    SharedPreferences sp;
    Editor ed;
    DatabaseHelper dbAdapter;
    ArrayList<ContactsGetSet> users;

    Handler h;
    Thread refresh;
    Button unlock_all;
    Resources res;
    String clicked_jid;
    UserListAdapter adapter;
    Editor editor;
    SharedPreferences sp1;
    EditText search_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_lock);
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.activity_lock, vg);

        dbAdapter = KachingMeApplication.getDatabaseAdapter();
        users = new ArrayList<ContactsGetSet>();

        db = new Dbhelper(getApplicationContext());
        res = getResources();
        sp1 = PreferenceManager.getDefaultSharedPreferences(this);

        sp = getApplicationContext().getSharedPreferences(
                KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
        editor = sp.edit();

        ed = sp.edit();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        mHeading.setText("Locked Users");
        mHeaderImg.setVisibility(View.GONE);
        mNextBtn.setImageResource(R.drawable.unlock);
        initialize();
//		unlock_all.setVisibility(View.GONE);
//		unlock_all.setText("UnLock");
        String query = "select jid_name,jid from " + Dbhelper.TABLE_LOCK;
        lock_status(query);
        Constant.printMsg("jid_list " + jid_list);
        for (int i = 0; i < jid_list.size(); i++) {
            ContactsGetSet contact = dbAdapter.getContact(jid_list.get(i));
            users.add(contact);
        }
        adapter = new UserListAdapter(this, R.layout.lock_list_item, users);
        list_lock.setAdapter(adapter);
        // if (lock_list.size() > 0) {
        // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
        // android.R.layout.simple_list_item_1, lock_list);
        // list_lock.setAdapter(arrayAdapter);
        //
        // }

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        list_lock.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                clicked_jid = users.get(position).getJid();
                alert_one();

            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                password_alert();

            }
        });
    }

    protected void alert() {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        // AlertDialog.Builder b;
        // b = new AlertDialog.Builder(this);
        //
        // b.setCancelable(false);
        // b.setMessage("Are you sure you want to unlock all")
        // .setCancelable(false);
        //
        // b.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
        // public void onClick(DialogInterface dialog, int id) {

        password_alert();
        // }
        // });
        // b.setPositiveButton("NO", new DialogInterface.OnClickListener() {
        // public void onClick(DialogInterface dialog, int id) {
        //
        // }
        // });
        // b.setCancelable(true);
        //
        // AlertDialog alert = b.create();
        // alert.show();

    }

    protected void password_alert() {

        AlertDialog.Builder alert = new AlertDialog.Builder(LockScreen.this);

        alert.setTitle(res.getString(R.string.open_chat));
        alert.setMessage("Enter password to unlock all chat's");

        final EditText input = new EditText(getApplicationContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        input.setTextColor(Color.BLACK);

        alert.setView(input);

        alert.setPositiveButton(res.getString(R.string.open),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String value = input.getText().toString();

                        if (value.equals(sp.getString("pin", ""))) {
                            String delete = "delete from "
                                    + Dbhelper.TABLE_LOCK;
                            delete_query(delete);
                            Constant.printMsg("deleted   " + jid_list);
                            for (int i = 0; i < jid_list.size(); i++) {
                                ed.remove(jid_list.get(i) + "_lock");
                                ed.commit();
                            }
                            users.clear();
                            adapter.notifyDataSetChanged();
                            // Intent intent = new Intent(LockScreen.this,
                            // LockScreen.class);
                            // startActivity(intent);
                            // finish();
                        } else {
                            new AlertManager().showAlertDialog(
                                    LockScreen.this,
                                    res.getString(R.string.you_are_entered_incorrect_pin),
                                    true);

                        }
                    }
                });

        alert.setNegativeButton(res.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.

                    }
                });
        alert.setNeutralButton("Forget", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                forget();
            }
        });
        alert.show();

    }

    protected void forget() {
        // TODO Auto-generated method stub
        long current_time = (System.currentTimeMillis() / 1000L);
        long last_send_time = sp.getLong("forgot_password", current_time);
        long diff_time = current_time - last_send_time;
        // Log.d(TAG, "Diff Time::" + diff_time);
        if (diff_time > 0 && diff_time < 300) {
            new AlertUtils()
                    .Toast_call(
                            LockScreen.this,
                            getResources()
                                    .getString(
                                            R.string.password_already_send_on_your_mobile_number));
        } else {
            if (KachingMeApplication.getIsNetAvailable()) {
                editor.putLong("forgot_password", current_time);
                editor.commit();
                new AlertManager().showAlertDialog(LockScreen.this, getResources().getString(R.string.you_will_recieve_sms), true);
                editor.putLong("forgot_password", current_time);
                editor.commit();

//                String no = sp1.getString("number", "");
//                String country_code = sp1.getString("countrycode", "");
//                RequestParams request_params = new RequestParams();
//                request_params.put("jid",
//                        country_code + no + KachingMeApplication.getHost());
//                AsyncHttpClient client = new AsyncHttpClient();
//                client.post(KachingMeConfig.FORGET_PASSWORD_PHP,
//                        request_params,
//                        new AsyncHttpResponseHandler(Looper.getMainLooper()) {
//
//                            @Override
//                            public void onFailure(int arg0, Header[] arg1,
//                                                  byte[] arg2, Throwable arg3) {
//                                // TODO Auto-generated method stub
//                                Constant.printMsg("failure");
//                            }
//
//                            @Override
//                            public void onSuccess(int arg0, Header[] arg1,
//                                                  byte[] arg2) {
//                                // TODO Auto-generated method stub
//                                Constant.printMsg("success");
//                                new AlertManager().showAlertDialog(LockScreen.this, getResources().getString(R.string.you_will_recieve_sms), true);
//                            }
//                        });
            } else {
                new AlertUtils().Toast_call(LockScreen.this, getResources()
                        .getString(R.string.no_internet_connection));
            }
        }
    }

    protected void alert_one() {
        AlertDialog.Builder alert = new AlertDialog.Builder(LockScreen.this);

        alert.setTitle(res.getString(R.string.open_chat));
        alert.setMessage("Enter password to unlock this chat");

        final EditText input = new EditText(getApplicationContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_CLASS_NUMBER);
        input.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        input.setTextColor(Color.BLACK);

        alert.setView(input);

        alert.setPositiveButton(res.getString(R.string.open),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String query = "select password from "
                                + Dbhelper.TABLE_LOCK + " where jid = '"
                                + clicked_jid + "'";

                        lock_status1(query);

                        String value = input.getText().toString();

                        if (value.equals(status_lock)) {
                            // if (value.equals(sp.getString("pin", ""))) {
                            String query1 = "delete from "
                                    + Dbhelper.TABLE_LOCK + " where jid = '"
                                    + clicked_jid + "'";
                            delete_query(query1);
                            Constant.printMsg("deleted   " + jid_list);
                            // for (int i = 0; i < jid_list.size(); i++) {
                            // ed.remove(jid_list.get(i) + "_lock");
                            // ed.commit();
                            // }
                            ed.remove(clicked_jid + "_lock");

                            ed.commit();

                            // users.clear();
                            // adapter.notifyDataSetChanged();
                            Intent intent = new Intent(LockScreen.this,
                                    LockScreen.class);
                            startActivity(intent);
                            finish();
                            // String select_query = "select jid_name,jid from "
                            // + Dbhelper.TABLE_LOCK;
                            // lock_status(select_query);
                            // Constant.printMsg("jid_list " + jid_list);
                            // for (int i = 0; i < jid_list.size(); i++) {
                            // contactsGetSet contact =
                            // dbAdapter.getContact(jid_list.get(i));
                            // users.add(contact);
                            // }
                            // UserListAdapter adapter = new
                            // UserListAdapter(this, R.layout.contactlistitem,
                            // users);
                            // list_lock.setAdapter(adapter);
                            // adapter.notifyDataSetChanged();
                        } else {
                            new AlertManager().showAlertDialog(
                                    LockScreen.this,
                                    res.getString(R.string.you_are_entered_incorrect_pin),
                                    true);

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

    private void initialize() {
        // TODO Auto-generated method stub
        list_lock = (ListView) findViewById(R.id.list_experience);
        unlock_all = (Button) findViewById(R.id.unblock_btn);
        Constant.typeFace(this, unlock_all);

//		search_edit = (EditText) findViewById(R.id.serchEdit);
//		search_edit.setVisibility(View.GONE);
    }

    private void lock_status(String query) {
        // TODO Auto-generated method stub
        Cursor c = null;

        try {

            Constant.printMsg("query  " + query);

            c = db.open().getDatabaseObj().rawQuery(query, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
            // c.moveToFirst();
            if (c.getCount() > 0) {

                while (c.moveToNext()) {

                    lock_list.add(String.valueOf(c.getString(0)));
                    jid_list.add(String.valueOf(c.getString(1)));
                    Constant.printMsg("lock_status " + c.getString(0));
                    Constant.printMsg("list lock" + lock_list);
                    // status_lock = c.getString(0);
                }

            }

        } catch (SQLException e) {

        } finally {
            c.close();
            db.close();
        }

    }

    private String lock_status1(String query) {
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
            c.close();
            db.close();
        }
        return status_lock;

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

    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(LockScreen.this, NotificationSettings.class)
                .putExtra("TAG", "Chat")
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
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

            name.setText(user.getDisplay_name());

            try {
                ImageView img = (ImageView) container
                        .findViewById(R.id.avtarimg);
                Bitmap bmp = BitmapFactory.decodeByteArray(user.getPhoto_ts(),
                        0, user.getPhoto_ts().length);
                img.setImageBitmap(new AvatarManager().roundCornerImage(bmp,
                        180));
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
            }
            // set status
            TextView status = (TextView) container.findViewById(R.id.status);

            try {
                status.setText(user.getStatus());
            } catch (Exception e) {
                // TODO: handle exceptionas
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
            }

            return container;
        }

    }
}
