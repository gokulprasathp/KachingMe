package com.wifin.kachingme.settings;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.SherlockBaseActivity;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.AlertUtils;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;

public class Status extends SherlockBaseActivity {
    ProgressDialog progressdialog;
    TextView txt_status, tvStatusYourTitle, tvStatusNewSelect;
    ImageButton btn_edit;
    String status;
    DatabaseHelper dbadapter;
    ListView listview;
    VCard vc = new VCard();
    private Chat chat;
    String[] values = new String[]{};
    Context context;
    public static String TAG = Status.class.getSimpleName();
    String roster[];
    ArrayList<String> list;
    String status_update;
    String value;
    StableArrayAdapter adapter;
    ArrayList<String> stringArrayList = new ArrayList<String>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    SharedPreferences preference,sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);
        initialization();
        fetchDBFrom();
        Constant.printMsg("statusstatus:oncreate values::" + values
                + "size::" + values.length);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // values = getResources().getStringArray(R.array.status_array);
        progressdialog = new ProgressDialog(Status.this);
        progressdialog.setMessage(getResources().getString(R.string.loading));
        context = Status.this;
        dbadapter = KachingMeApplication.getDatabaseAdapter();
        list = new ArrayList<String>();
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        Constant.printMsg("list size before in status::" + list.size()
                + "values::" + list);
        adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);
        // listview.invalidateViews();
        txt_status.setText(dbadapter.getLogin().get(0).getStatus());
        // Bundle bundle = getIntent().getExtras();
        // if (bundle != null) {
        // // status_update = bundle.getString("status_update");
        // // // setStatus(txt_status.getText().toString());
        // // setStatus(status_update);
        // }
        btn_edit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Connectivity.isConnected(Status.this)){
                    setStatus(txt_status.getText().toString());
                }else {
                    Toast.makeText(Status.this,"Please Check Your Network Connection.!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                setStatus(item);
            }

        });
        // doBindService();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initialization() {
        tvStatusYourTitle = (TextView) findViewById(R.id.tvStatusYourTitle);
        tvStatusNewSelect = (TextView) findViewById(R.id.tvStatusNewSelect);
        txt_status = (TextView) findViewById(R.id.status_txt_status);
        btn_edit = (ImageButton) findViewById(R.id.status_btn_edit);
        listview = (ListView) findViewById(R.id.list_experience);
        Constant.typeFace(this, txt_status);
        Constant.typeFace(this, tvStatusYourTitle);
        Constant.typeFace(this, tvStatusNewSelect);
    }

    @Override
    protected void onDestroy() {
        // doUnbindService();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Status.this, SliderTesting.class);
                startActivity(intent);
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		/* getSupportMenuInflater().inflate(R.menu.profile_menu, menu); */
        return true;
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Status Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    public void setStatus(final String status1) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getResources().getString(R.string.status));
        // alert.setMessage("Message");
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setText(status1);
        input.setSelectAllOnFocus(true);
        alert.setView(input);
        alert.setPositiveButton(getResources().getString(R.string.Ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        value = input.getText().toString().trim();
                        status = value;
                        Constant.printMsg("status....values....."+txt_status.getText().toString()+"...."+value);
                        if (!txt_status.getText().toString().equals(value)){
                            new myStatusAsyncSave().execute();
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

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);

            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            TextView txt_name = (TextView) view
                    .findViewById(android.R.id.text1);
            // updateItemAtPosition(position);
            if (txt_status.getText().toString()
                    .equals(txt_name.getText().toString())) {
                txt_name.setTextColor(getResources().getColor(
                        R.color.app_color_blue));
            } else {
                txt_name.setTextColor(getResources().getColor(R.color.black));
            }
            txt_name.setText(getItem(position));
            Constant.typeFace(getApplicationContext(), txt_name);
            return view;
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);

            Constant.printMsg("item updated::" + item + "position::"
                    + position);
            // return mIdMap.get(item);
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    public void fetchDBFrom() {
        String tx, mn;
        Dbhelper db = new Dbhelper(getApplicationContext());
        Dbhelper.Mydatabase my = db.mydata.getInstance(getApplicationContext());
        // my.handleNcaMdaResponse(result);
        SQLiteDatabase s = my.getWritableDatabase();
        // SQLiteDatabase s = d.mydata.getWritableDatabase();
        s.beginTransaction();
        Cursor c = null;
        String qry = "SELECT * FROM " + Dbhelper.TABLE_UPDATE_STATUS;
        try {
            c = s.rawQuery(qry, null);
            int date_index = c.getColumnIndex("status");
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    mn = c.getString(date_index);
                    Constant.printMsg("statusstatus:" + mn);
                    // for (int i = 0; i < mn.length(); i++) {
                    stringArrayList.add(mn);
                    Constant.printMsg("statusstatus:added" + mn
                            + "stringArrayList:" + stringArrayList + "si:"
                            + stringArrayList.size());// add to arraylist
                    // }
                }
            }
            values = stringArrayList
                    .toArray(new String[stringArrayList.size()]);
            Constant.printMsg("statusstatus:" + "values::" + values
                    + "size::" + values.length);

        } catch (SQLException e) {

        } finally {
            if (c != null) {
                c.close();
            }
        }

        s.setTransactionSuccessful();
        s.endTransaction();
        s.close();
    }

    private class myStatusAsyncSave extends AsyncTask<String, String, String> {
        Boolean isEmailExist = false;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressdialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            dbadapter.setUpdateUserStatus(KachingMeApplication.getUserID(), status);
            String result=null;
            try {
                String encodeStatus = URLEncoder.encode(status, "utf-8");
                HttpConfig ht=new HttpConfig();
                result= ht.httpget(KachingMeConfig.UPDATE_STATUS+"phoneNumber="+sharedPreferences.getString("MyPrimaryNumber", "")
                        +"&status="+encodeStatus);
                Constant.printMsg("Profile update....service....."+ KachingMeConfig.UPDATE_STATUS+"phoneNumber="+sharedPreferences.getString("MyPrimaryNumber", "")
                        +"&status="+encodeStatus);
                Constant.printMsg("Profile update....result fi....."+result);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Constant.printMsg("Profile update....result....."+result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result != null && !result.isEmpty() &&
                        result.contains("Profile status updated successfully")) {
                    txt_status.setText(status);
                    Toast.makeText(getApplicationContext(), "Status Updated",
                            Toast.LENGTH_SHORT).show();
                    Constant.printMsg("list values status::" + values.length
                            + "values::" + values.toString() + "status::"
                            + status);
                    String newstatus = null;
                    boolean check = false;
                    for (int i = 0; i < values.length && check == false; i++) {

                        if (Arrays.asList(values).contains(status)) {
                            newstatus = null;
                            Constant.printMsg("c 1111" + i + list.size()
                                    + "values::" + list + "values[i]::"
                                    + Arrays.asList(values) + "status:"
                                    + status + "newstatus::" + newstatus);
                        } else {
                            newstatus = status;
                            Constant.printMsg("c 2222iii::" + i
                                    + list.size() + "values[i]::"
                                    + Arrays.asList(values) + "newstatus::"
                                    + newstatus);
                            check = true;
                        }
                        Constant.printMsg("check value::" + check + "iiii::"
                                + i + "vaaa" + Arrays.asList(values));
                    }

                    Constant.printMsg("c 3333 newstatus" + newstatus
                            + "value:" + value + "check::" + check);
                    if (newstatus != null && newstatus.length() > 0) {
                        list.add(newstatus);
                        ContentValues v = new ContentValues();
                        // v.put("name", k);
                        v.put("status", newstatus);
                        insertStatusDB(v);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                // listview.setAdapter(adapter);
                                Constant.printMsg("c 4444" + adapter
                                        + "value:" + value);
                                Constant.printMsg("c 5555:" + list.size()
                                        + "values::" + list);
                            }
                        });
                    } else {
                        Constant.printMsg("c 6666" + list.size()
                                + "newstatus" + newstatus + "list" + list);
                        adapter.notifyDataSetChanged();
                    }

                    Constant.printMsg("c 7777::" + list.size() + "values::"
                            + list);
                    DatabaseHelper dbAdapter = KachingMeApplication
                            .getDatabaseAdapter();

                    dbAdapter.setUpdateLogin_status(KachingMeApplication
                            .getUserID(), txt_status.getText().toString());
                    KachingMeApplication.setStatus(txt_status.getText().toString());
                    CommonMethods commonMethods= new CommonMethods(com.wifin.kachingme.settings.Status.this);
//                    new updateStatusPhp().execute();
//                    commonMethods.stopAsyncTask(new updateStatusPhp(), null,Constant.asynTaskSixtySeconds);
                    new updateStatusVcard().execute();
                    commonMethods.stopAsyncTask(new updateStatusVcard(), null,Constant.asynTaskSixtySeconds);
                } else {
                    new AlertUtils().Toast_call(context, getResources()
                            .getString(R.string.please_try_again));
                }
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
                e.printStackTrace();
            }
            progressdialog.cancel();
            super.onPostExecute(result);
        }
    }

    public class updateStatusVcard extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            try {
                vc = VCardManager.getInstanceFor(
                        TempConnectionService.connection).loadVCard();
                vc.setField("SORT-STRING", status);
                VCardManager.getInstanceFor(TempConnectionService.connection)
                        .saveVCard(vc);
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
            }

            try {
                Roster roster_c = Roster
                        .getInstanceFor(TempConnectionService.connection);
                Collection<RosterEntry> entries = roster_c.getEntries();

                // msg.setType(org.jivesoftware.smack.packet.Message.Type.);
                roster = new String[entries.size()];
                int i = 0;
                for (RosterEntry rosterEntry : entries) {
                    roster[i] = rosterEntry.getUser();
                    i++;
                }
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
            }
            Constant.printMsg("siva Final Result status post......");
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                for (int i = 0; i < roster.length; i++) {
                    Message msg = new Message();
                    msg.setStanzaId("vcardedit");
                    msg.setType(Message.Type.chat);
                    msg.setTo(roster[i]);
                    TempConnectionService.connection.sendStanza(msg);
                }
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

    }

//    public class updateStatusPhp extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... args) {
//            // TODO Auto-generated method stub
//            String result = null;
//            String url = KachingMeConfig.USER_UPDATE_PHP;
//            String jidValuue = KachingMeApplication.getUserID() + KachingMeApplication.getHost();
//            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("Waction", "update"));
//            nameValuePairs.add(new BasicNameValuePair("Wtable", "tbl_user"));
//            nameValuePairs.add(new BasicNameValuePair("Wcolumn", "jid"));
//            nameValuePairs.add(new BasicNameValuePair("Wvalue", jidValuue));
//            nameValuePairs.add(new BasicNameValuePair("status", status));
//
//            HttpConfig ht = new HttpConfig();
//			result = ht.doPostNameValue(url,nameValuePairs);
//			Constant.printMsg("siva Final Result status post......"+url+"............" +result);
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            Constant.printMsg("siva Final Result..........." + result);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//        }
//
//    }

    public void insertStatusDB(ContentValues v) {
        Dbhelper db = new Dbhelper(getApplicationContext());
        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_UPDATE_STATUS, null, v);
            Constant.printMsg("No of rows inserted into TABLE_UPDATE_STATUS in status classsssss is ::::"
                    + a);
        } catch (SQLException e) {

        } finally {
            db.close();
        }
    }

    private void updateItemAtPosition(int position) {
        int visiblePosition = listview.getFirstVisiblePosition();
        View view = listview.getChildAt(position - visiblePosition);
        listview.getAdapter().getView(position, view, listview);
        Constant.printMsg("ssss updateItemAtPosition::");
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent i = new Intent(Status.this, SliderTesting.class);
        startActivity(i);
        finish();
    }

}
