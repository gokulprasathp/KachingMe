package com.wifin.kachingme.settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.Chat_list_GetSet;
import com.wifin.kachingme.registration_and_login.Slideshow;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.Select_MUC_Admin;

import org.apache.http.Header;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.bookmarks.BookmarkManager;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DeleteAccount extends Activity
{
    LinearLayout linearDeleteButton, linearDeleteContent, linearAccountDelete;
    AppBarLayout deleteAppBar;
    Toolbar deleteToolBar;
    FrameLayout deleteFrameLayout;
    TextView tvDeleteTitle, tvAccDelete, tvAccContent;
    Button btDeleteYes, btDeleteNo;
    int width, height;
    String TAG = DeleteAccount.class.getSimpleName();

    DatabaseHelper dbAdapter;
    SharedPreferences sp, sp1;
    SharedPreferences.Editor editor;
    MultiUserChat muc;
    Cursor cursor;
    ArrayList<String> ar_jid = new ArrayList<String>();
    ArrayList<String> mem_jid = new ArrayList<String>();
    ArrayAdapter<String> ar_name;
    int selected_position;
    ProgressDialog progressdialog;
    String db_data;
    DatabaseHelper dbadapter = KachingMeApplication.getDatabaseAdapter();
    Dbhelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_delete_account);

        initDelete();
        arrangeScreenDelete();

        btDeleteYes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    get_Group_List();
                }
                catch (Exception e)
                {
                    Constant.printMsg("GGGGGGGGGGGGG" + e.toString());
                }
            }
        });

        btDeleteNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        dbadapter = KachingMeApplication.getDatabaseAdapter();
        db = new Dbhelper(getApplicationContext());
        sp1 = PreferenceManager.getDefaultSharedPreferences(this);
        sp = getSharedPreferences(KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
        editor = sp.edit();
        dbAdapter = KachingMeApplication.getDatabaseAdapter();
        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage(getResources().getString(R.string.please_wait));
    }

    public void initDelete(){
        linearDeleteButton = (LinearLayout) findViewById(R.id.linearDeleteButton);
        linearDeleteContent = (LinearLayout) findViewById(R.id.linearDeleteContent);
        linearAccountDelete = (LinearLayout) findViewById(R.id.linearAccountDelete);
        deleteAppBar = (AppBarLayout) findViewById(R.id.deleteAppBar);
        deleteToolBar = (Toolbar) findViewById(R.id.deleteToolBar);
        deleteFrameLayout = (FrameLayout) findViewById(R.id.deleteFrameLayout);
        tvDeleteTitle = (TextView) findViewById(R.id.tvDeleteTitle);
        tvAccDelete = (TextView) findViewById(R.id.tvAccDelete);
        tvAccContent = (TextView) findViewById(R.id.tvAccContent);
        btDeleteYes = (Button) findViewById(R.id.btDeleteYes);
        btDeleteNo = (Button) findViewById(R.id.btDeleteNo);

        Constant.typeFace(this,tvDeleteTitle);
        Constant.typeFace(this,tvAccDelete);
        Constant.typeFace(this,tvAccContent);
        Constant.typeFace(this,btDeleteYes);
        Constant.typeFace(this,btDeleteNo);

        btDeleteYes.setSelected(true);
        btDeleteNo.setSelected(true);
    }

    public void arrangeScreenDelete()
    {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        Log.e(TAG, "Height : " + height + "\n" + "Width : " + width);

        Constant.height = height;
        Constant.width = width;

        AppBarLayout.LayoutParams toolBarDelete = new AppBarLayout.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        toolBarDelete.width = (int) width;
        toolBarDelete.height = (int) height * 10 / 100;
        toolBarDelete.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        deleteToolBar.setLayoutParams(toolBarDelete);

        Log.e(TAG, "" + toolBarDelete.width + " " + toolBarDelete.height);

        LinearLayout.LayoutParams deleteContentTitle = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        deleteContentTitle.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        deleteContentTitle.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        deleteContentTitle.setMargins(width * 2 / 100, width * 4 / 100, width * 1 / 100, width * 4 / 100);
        deleteContentTitle.gravity = Gravity.CENTER_VERTICAL;
        linearDeleteContent.setLayoutParams(deleteContentTitle);

        Log.e(TAG, deleteContentTitle.width + " " + deleteContentTitle.height + " " + width * 2 / 100);

        LinearLayout.LayoutParams deleteTextTitleContent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        deleteTextTitleContent.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        deleteTextTitleContent.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        deleteTextTitleContent.setMargins(width * 2 / 100, width * 3 / 100, width * 1 / 100, width * 3 / 100);
        deleteTextTitleContent.gravity = Gravity.CENTER_VERTICAL;
        tvAccContent.setLayoutParams(deleteTextTitleContent);
        tvAccDelete.setLayoutParams(deleteTextTitleContent);

        LinearLayout.LayoutParams deleteButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        deleteButton.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        deleteButton.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        deleteButton.setMargins(width * 2 / 100, width * 4 / 100, width * 2 / 100, 0);
        deleteButton.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER;
        linearDeleteButton.setLayoutParams(deleteButton);

        LinearLayout.LayoutParams deleteButtonContent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        deleteButtonContent.width = 25 * width / 100;
        deleteButtonContent.height = 10 * width / 100;
        deleteButtonContent.setMargins(width * 2 / 100, width * 2 / 100, width * 2 / 100, width * 2 / 100);
        deleteButtonContent.gravity = Gravity.CENTER;
        btDeleteYes.setLayoutParams(deleteButtonContent);
        btDeleteNo.setLayoutParams(deleteButtonContent);

        Log.e(TAG, deleteButtonContent.width + " " + deleteContentTitle.height + " " + width * 2 / 100);

        if (width >= 600)
        {
            tvDeleteTitle.setTextSize(19);
            tvAccDelete.setTextSize(17);
            tvAccContent.setTextSize(17);
            btDeleteYes.setTextSize(17);
            btDeleteNo.setTextSize(17);
        }
        else if (width > 501 && width < 600)
        {
            tvDeleteTitle.setTextSize(18);
            tvAccDelete.setTextSize(16);
            tvAccContent.setTextSize(16);
            btDeleteYes.setTextSize(16);
            btDeleteNo.setTextSize(16);
        }
        else if (width > 260 && width < 500)
        {
            tvDeleteTitle.setTextSize(17);
            tvAccDelete.setTextSize(15);
            tvAccContent.setTextSize(15);
            btDeleteYes.setTextSize(15);
            btDeleteNo.setTextSize(15);
        }
        else if (width <= 260)
        {
            tvDeleteTitle.setTextSize(16);
            tvAccDelete.setTextSize(14);
            tvAccContent.setTextSize(14);
            btDeleteYes.setTextSize(14);
            btDeleteNo.setTextSize(14);
        }
    }

    public void get_Group_List()
    {
        ar_jid.clear();
        ar_name = new ArrayAdapter<>(DeleteAccount.this, android.R.layout.select_dialog_singlechoice);
        cursor = dbAdapter.getMUCChat_list();
        if (cursor.moveToFirst())
        {
            do
            {
                com.wifin.kachingme.util.Log.d(TAG, "Rooms::" + cursor.getString(8));

                String admin = sp.getString(cursor.getString(8) + "_admin", "");

                if (admin.equals(KachingMeApplication.getUserID() + KachingMeApplication.getHost()) && (sp.getString(cursor.getString(8), "").toString().split(",").length > 1))
                {
                    com.wifin.kachingme.util.Log.d(TAG, "Room admin::" + cursor.getString(8));
                    ar_jid.add(cursor.getString(8));
                    ar_name.add(cursor.getString(1));
                }
                else
                {
                    mem_jid.add(cursor.getString(8));
                }
            }
            while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }

        new left_group().execute();
    }

    public void Asign_Group_Admin()
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(DeleteAccount.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle(getResources().getString(R.string.select_admin));

        builderSingle.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(ar_name, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String strName = ar_name.getItem(which);
                selected_position = which;
                String[] mm = sp.getString(ar_jid.get(which), "").toString().split(",");
                String mm_list = "";
                int i = 0;
                for (String string : mm)
                {
                    if (i == 0)
                    {
                        mm_list = "'" + string + "'";
                    }
                    else
                    {
                        mm_list = mm_list + ",'" + string + "'";
                    }
                    i++;
                }

                Intent intente = new Intent(DeleteAccount.this, Select_MUC_Admin.class);
                intente.putExtra("jids", ar_jid.get(which));
                startActivityForResult(intente, 2);
            }
        });
        builderSingle.show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    private class left_group extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressdialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            MultiUserChatManager muc_manager = TempConnectionService.MUC_MANAGER;

            for (int j = 0; j < mem_jid.size(); j++) {
                muc = muc_manager.getMultiUserChat(mem_jid.get(j));
                try {
                    muc.join(mem_jid.get(j));
                } catch (Exception e) {
                    // ACRA.getErrorReporter().handleException(e);
                    e.printStackTrace();
                    // TODO: handle exception
                }

                try {

                    muc.revokeOwnership(KachingMeApplication.getjid());
                    String mem_list = null;
                    Collection<Affiliate> owner = muc.getOwners();

                    int i = 0;
                    for (Affiliate affiliate : owner) {
                        if (i == 0) {
                            mem_list = affiliate.getJid();
                        } else {
                            mem_list = mem_list + "," + affiliate.getJid();
                        }
                        i++;
                        com.wifin.kachingme.util.Log.d("MUC_info", "Owner::" + affiliate.getJid());
                    }

                    Message msg = new Message(mem_jid.get(j), Message.Type.groupchat);

                    // msg.setSubject("Remove");
                    msg.setBody(mem_list);

                    JivePropertiesManager.addProperty(msg, "ID", 2);
                    JivePropertiesManager.addProperty(msg, "Removed_member",
                            KachingMeApplication.getjid());
                    msg.setPacketID(Constant.MEMBERREMOVEMESSAGE
                            + new Date().getTime());
                    JivePropertiesManager
                            .addProperty(msg, "media_wa_type", "0");
                    muc.sendMessage(msg);

                    // Delete_Local();

                } catch (Exception e) {
                    // ACRA.getErrorReporter().handleException(e);
                    e.printStackTrace();
                    // TODO: handle exception
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            // TODO Auto-generated method stub

            new delete_Async().execute();

        }
    }

    private class delete_Async extends AsyncTask<String, String, String> {
        boolean deleted = false;
        boolean deleted1 = false;

        @Override
        protected String doInBackground(String... params) {
            try {

                com.wifin.kachingme.util.Log.d(TAG, "Before account delete");
                dbAdapter.close();
                AccountManager account = AccountManager
                        .getInstance(TempConnectionService.connection);
                account.deleteAccount();

                File file = new File("/data/data/"
                        + getApplicationContext().getPackageName()
                        + "/databases/kachingme.db");
                deleted = file.delete();

                File file_1 = new File("/data/data/"
                        + getApplicationContext().getPackageName()
                        + "/shared_prefs/niftychat_pereferences.xml");
                deleted1 = file_1.delete();

                com.wifin.kachingme.util.Log.d(TAG, "IS DELETE DB::" + deleted + " IS DELETE SP::"
                        + deleted1);

            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (deleted && deleted1) {
                com.wifin.kachingme.util.Log.d(TAG, "Before account delete .... after");

                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams request_params = new RequestParams();
                request_params.add("jid", KachingMeApplication.getjid());
                client.post(KachingMeConfig.DELETE_USER_PHP,
                        request_params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onFinish() {
                                // TODO Auto-generated method stub
                                com.wifin.kachingme.util.Log.d("Remove_user", "User_Delete_Finish");
                                editor.remove("pin");
                                editor.commit();
                                finish();
                                super.onFinish();
                            }

                            @Override
                            public void onStart() {
                                // TODO Auto-generated method stub
                                com.wifin.kachingme.util.Log.d("Remove_user", "User_Delete_Sart");
                                super.onStart();
                            }

                            @Override
                            public void onFailure(int arg0, Header[] arg1,
                                                  byte[] arg2, Throwable arg3) {
                                // TODO Auto-generated method stub
                                com.wifin.kachingme.util.Log.d("Remove_user", "User_Delete_Failure::"
                                        + new String(arg2));
                            }

                            @Override
                            public void onSuccess(int arg0, Header[] arg1,
                                                  byte[] arg2) {
                                // TODO Auto-generated method stub
                                com.wifin.kachingme.util.Log.d("Remove_user", "User_Delete::"
                                        + new String(arg2));
                                editor.remove("pin");
                                editor.commit();
                                finish();
                            }

                        });

            }
            // System.exit(0);
            new deleteAccount().execute();

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

    }

    public void Admin_exit(String new_admin, String jid) {
        try {
            MultiUserChatManager muc_manager = TempConnectionService.MUC_MANAGER;
            muc = muc_manager.getMultiUserChat(jid);
            try {
                muc.join(jid);
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                // TODO: handle exception
            }

            dbAdapter = KachingMeApplication.getDatabaseAdapter();

            Chat_list_GetSet chat_list = dbAdapter.getChat_List(muc.getRoom());
            Form f1 = muc.getConfigurationForm();
            List<String> admin = new ArrayList<String>();
            admin.add(new_admin);
            Form submitForm = f1.createAnswerForm();
            JSONObject main_job = new JSONObject();

            for (Iterator fields = f1.getFields().iterator(); fields.hasNext();) {
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

            com.wifin.kachingme.util.Log.d(TAG, "New Room Configuration::" + main_job.toString());
            submitForm
                    .setAnswer("muc#roomconfig_roomdesc", main_job.toString());
            muc.sendConfigurationForm(submitForm);

            try {

                muc.revokeOwnership(KachingMeApplication.getUserID()
                        + KachingMeApplication.getHost());

				/* muc.sendConfigurationForm(f1); */
                String mem_list = null;
                Collection<Affiliate> owner = muc.getOwners();

                int i = 0;
                for (Affiliate affiliate : owner) {
                    if (i == 0) {
                        mem_list = affiliate.getJid();
                    } else {
                        mem_list = mem_list + "," + affiliate.getJid();
                    }
                    i++;
                    com.wifin.kachingme.util.Log.d("MUC_info", "Owner::" + affiliate.getJid());
                }

                Message msg = new Message(muc.getRoom(), Message.Type.groupchat);
                // msg.setSubject(muc.getSubject());

                msg.setBody(mem_list);

                JivePropertiesManager.addProperty(msg, "ID", 2);
                JivePropertiesManager.addProperty(msg, "Removed_member",
                        KachingMeApplication.getjid());
                msg.setPacketID(Constant.MEMBERREMOVEMESSAGE
                        + new Date().getTime());
                JivePropertiesManager.addProperty(msg, "media_wa_type", "0");
                muc.sendMessage(msg);

                editor.remove(jid + "_admin");
                editor.remove(jid);
                editor.commit();

                Delete_Local(jid);

                get_Group_List();

                if (ar_jid.size() > 0) {
                    Asign_Group_Admin();
                }

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

    public void Delete_Local(String jid) {
        editor.remove(jid);
        editor.remove(jid + "_admin");

        editor.commit();

        dbAdapter.setDeleteContact(jid);
        dbAdapter.setDeleteMessages(jid);
        dbAdapter.setDeleteChatList(jid);
        try {
            BookmarkManager bm = BookmarkManager
                    .getBookmarkManager(TempConnectionService.connection);
            bm.removeBookmarkedConference(muc.getRoom());
        } catch (Exception e) {
            // ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == 2) {
                if (data != null) {
                    String jid = data.getStringExtra("jid");
                    com.wifin.kachingme.util.Log.d("Activity Result", "Selected Admin jid::" + jid);
                    if (jid != null && jid != "") {
                        Admin_exit(jid, ar_jid.get(selected_position));
                    }
                    // Add_Meber(jid);
                }
            }
        }

    }

    public class deleteAccount extends AsyncTask<String, String, String> {
        // ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();

            result = ht.httpget(KachingMeConfig.Delete_Account + "primaryNumber="
                    + KachingMeApplication.getjid().split("@")[0]);
            // Toast.makeText(getApplicationContext(), "result " + result,
            // Toast.LENGTH_LONG).show();
            Constant.printMsg("result " + result
                    + KachingMeApplication.getjid().split("@")[0]);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // progressDialog.dismiss();
            Constant.printMsg("result ::::::: >>>>>> " + result);
            Toast.makeText(
                    getApplicationContext(),
                    "result " + result
                            + KachingMeApplication.getjid().split("@")[0],
                    Toast.LENGTH_LONG).show();
            if (result != null && result.length() > 0) {

                if (result.equalsIgnoreCase("Account Deleted")) {

                    Constant.freelistmain.clear();
                    Constant.printMsg("deleted db::beforeeeeee");
                    // deletemethod();

                    insertValueAgain();
                    editor.remove("pin");
                    editor.commit();

                    stopService(new Intent(getApplicationContext(),
                            TempConnectionService.class));

                    Constant.isNiftyApplicationRunning = true;
                    Intent intent = new Intent(DeleteAccount.this,
                            Slideshow.class);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                    // finish();
                } else {
                    Toast.makeText(getApplicationContext(), "not exist",
                            Toast.LENGTH_LONG).show();
                }

            }
            progressdialog.cancel();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

    }

    public void deletemethod() {
        // TODO Auto-generated method stub

        try {
            DbDelete();
            DbDeleteDeel();
            DbDeleteNym();
            DbDeleteBux();
            DbDeleteDonation();
            DbDeleteZZle();
            DbDeleteWish();
            DbDeleteFreeBie();
            DbDeleteMer();
            DbDeletekons();
            DbdeletePrimary();
            dbadapter.setDeleteLogin("");
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void DbDelete() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_BUX, null, null);
            System.out
                    .println("No of deleted rows from bux data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeleteDeel() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_CART, null, null);
            System.out
                    .println("No of deleted rows from deel data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeleteNym() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_NYM, null, null);
            System.out
                    .println("No of deleted rows from deel data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeleteBux() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_BUX, null, null);
            System.out
                    .println("No of deleted rows from deel data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeleteZZle() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_ZZLE, null, null);
            System.out
                    .println("No of deleted rows from deel data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeleteWish() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_WISH, null, null);
            System.out
                    .println("No of deleted rows from deel data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeleteFreeBie() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_FREEBIE, null, null);
            System.out
                    .println("No of deleted rows from freebie data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeleteMer() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_RET, null, null);
            System.out
                    .println("No of deleted rows from retmetr data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbdeletePrimary() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_PRIMENUMBER, null, null);
            System.out
                    .println("No of deleted rows from primary data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular primary for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeleteDonation() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_DONATE, null, null);
            System.out
                    .println("No of deleted rows from deel data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeletekons() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_KONS, null, null);
            System.out
                    .println("No of deleted rows from retmetr data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    private void insertValueAgain() {
        // TODO Auto-generated method stub
        db_data = "null";
        updateDBvalue(db_data);
    }

    public void updateDBvalue(String code) {
        SharedPreferences.Editor e = sp.edit();
        e.putString("db_data", code);
        Constant.printMsg("updateDBvalue:while logout" + code);
        e.commit();
    }

    public void onBackPressed()
    {
        super.onBackPressed();

        startActivity(new Intent(DeleteAccount.this, UsageActivity.class));

        finish();
    }
}
