package com.wifin.kachingme.registration_and_login;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveApi.MetadataBufferResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.DatabaseHelper_Backup;
import com.wifin.kachingme.pojo.Chat_list_GetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.DriveBaseActivity;
import com.wifin.kaching.me.ui.R;

public class RestoreBackupCloud extends DriveBaseActivity implements OnClickListener {

    Button btn_no, btn_yes;
    TextView mText1, mText2, mProcessText;
    DatabaseHelper_Backup dbadapter_backup;
    DatabaseHelper dbadapter;
    LinearLayout rl_restore, rl_restore_process;
    DriveFile file;
    Boolean is_connected = false;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restore_backup);
        initialization();
        screenArrange();
        rl_restore.setVisibility(View.VISIBLE);
        rl_restore_process.setVisibility(View.GONE);
        dbadapter = KachingMeApplication.getDatabaseAdapter();
    }

    private void initialization() {
        btn_no = (Button) findViewById(R.id.btn_no);
        btn_yes = (Button) findViewById(R.id.btn_yes);
        mText1 = (TextView) findViewById(R.id.restore_text1);
        mText2 = (TextView) findViewById(R.id.restore_text2);
        mProcessText = (TextView) findViewById(R.id.restore_processText);
        rl_restore = (LinearLayout) findViewById(R.id.restore_backup);
        rl_restore_process = (LinearLayout) findViewById(R.id.restore_backup_process);
        progressBar = (ProgressBar) findViewById(R.id.restore_progressBar);

        Constant.typeFace(this, btn_yes);
        Constant.typeFace(this, btn_no);
        Constant.typeFace(this, mText1);
        Constant.typeFace(this, mText2);
        Constant.typeFace(this, mProcessText);

        btn_no.setOnClickListener(this);
        btn_yes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                if (is_connected) {
                    rl_restore.setVisibility(View.GONE);
                    rl_restore_process.setVisibility(View.VISIBLE);
                    Query query = new Query.Builder().addFilter(
                            Filters.eq(SearchableField.TITLE, "niftychat.db")).build();
                    Drive.DriveApi.query(getGoogleApiClient(), query)
                            .setResultCallback(metadataCallback);
                } else {
                    Toast.makeText(RestoreBackupCloud.this,getResources().getString(
                            R.string.wait_while_connect_to_google_account),Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_no:

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(RestoreBackupCloud.this);
                SharedPreferences.Editor editor = sp.edit();

                editor.putString("activity_name", "SliderTesting");
                editor.putBoolean("decline", false);
                editor.commit();

                SharedPreferences sps = RestoreBackupCloud.this.getSharedPreferences(KachingMeApplication.getPereference_label(),
                        Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sps.edit();

                editor1.putString("activity_name", "SliderTesting");
                editor1.putBoolean("decline", false);
                editor1.commit();

                stopService(new Intent(RestoreBackupCloud.this, TempConnectionService.class));
                startService(new Intent(RestoreBackupCloud.this, TempConnectionService.class));
                startActivity(new Intent(RestoreBackupCloud.this, SliderTesting.class));
                finish();
                break;
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        is_connected = true;

    }

    final private ResultCallback<MetadataBufferResult> metadataCallback = new ResultCallback<MetadataBufferResult>() {
        @Override
        public void onResult(MetadataBufferResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Problem while retrieving results");
                return;
            }
            // showMessage(result.getMetadataBuffer().getCount()+" Files found!!!!!");

            if (result.getMetadataBuffer().getCount() == 0) {
                // showMessage("No database backup fond on cloud!!");
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        RestoreBackupCloud.this);
                builder.setTitle(
                        getResources().getString(R.string.restore_backup))
                        .setMessage(
                                getResources().getString(
                                        R.string.no_database_found_on_cloud))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(
                                getResources().getString(R.string.Ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // Yes button clicked, do something

                                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(RestoreBackupCloud.this);
                                        SharedPreferences.Editor editor = sp.edit();

                                        editor.putString("activity_name", "SliderTesting");
                                        editor.putBoolean("decline", false);
                                        editor.commit();

                                        SharedPreferences sps = RestoreBackupCloud.this.getSharedPreferences(KachingMeApplication.getPereference_label(),
                                                Activity.MODE_PRIVATE);
                                        SharedPreferences.Editor editor1 = sps.edit();

                                        editor1.putString("activity_name", "SliderTesting");
                                        editor1.putBoolean("decline", false);
                                        editor1.commit();

                                        stopService(new Intent(RestoreBackupCloud.this, TempConnectionService.class));
                                        startService(new Intent(RestoreBackupCloud.this, TempConnectionService.class));
                                        startActivity(new Intent(RestoreBackupCloud.this, SliderTesting.class));
                                        finish();

                                    }
                                }).show();
            } else {

                file = Drive.DriveApi.getFile(getGoogleApiClient(), result
                        .getMetadataBuffer().get(0).getDriveId());
                file.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null)
                        .setResultCallback(contentsOpenedCallback);

            }

        }
    };

    ResultCallback<DriveContentsResult> contentsOpenedCallback = new ResultCallback<DriveContentsResult>() {
        @Override
        public void onResult(DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                // display an error saying file can't be opened
                return;
            }
            // DriveContents object contains pointers
            // to the actual byte stream
            String saveFilePath = null;
            DriveContents contents = result.getDriveContents();
            try {

                final int BUFFER_SIZE = 4096;
                InputStream inputStream = contents.getInputStream();
                saveFilePath = new File("/data/data/"
                        + getApplicationContext().getPackageName()
                        + "/databases/", "niftychat_backup.db")
                        .getAbsolutePath();

                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(
                        saveFilePath);

                int bytesRead = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                new Restore_Async().execute();
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                // TODO: handle exception
            }
        }
    };

    public class Restore_Async extends AsyncTask<String, String, String> {

        ArrayList<Chat_list_GetSet> chat_list = new ArrayList<Chat_list_GetSet>();
        ArrayList<MessageGetSet> msg_list = new ArrayList<MessageGetSet>();
        ArrayList<ContactsGetSet> contact_list = new ArrayList<ContactsGetSet>();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            rl_restore.setVisibility(View.GONE);
            rl_restore_process.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            dbadapter_backup = DatabaseHelper_Backup
                    .getDBAdapterInstance(RestoreBackupCloud.this);
            dbadapter_backup.openDataBase();

            msg_list = dbadapter_backup.getAllMessages();
            chat_list = dbadapter_backup.getAllChat_list();

            for (int i = 0; i < msg_list.size(); i++) {
                dbadapter.setInsertMessages(msg_list.get(i));
            }

            for (int j = 0; j < chat_list.size(); j++) {
                dbadapter.setInsertChat_list_chat_all(chat_list.get(j)
                        .getKey_remote_jid(), chat_list.get(j)
                        .getMessage_table_id(), chat_list.get(j)
                        .getIs_sec_chat(), chat_list.get(j)
                        .getUnseen_msg_count());
            }

            for (int k = 0; k < contact_list.size(); k++) {
                dbadapter.insertContacts(contact_list.get(k));

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dbadapter_backup.close();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(RestoreBackupCloud.this);
            SharedPreferences.Editor editor = sp.edit();

            editor.putString("activity_name", "SliderTesting");
            editor.putBoolean("decline", false);
            editor.commit();

            SharedPreferences sps = RestoreBackupCloud.this.getSharedPreferences(KachingMeApplication.getPereference_label(),
                    Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sps.edit();

            editor1.putString("activity_name", "SliderTesting");
            editor1.putBoolean("decline", false);
            editor1.commit();

            stopService(new Intent(RestoreBackupCloud.this, TempConnectionService.class));
            startService(new Intent(RestoreBackupCloud.this, TempConnectionService.class));
            startActivity(new Intent(RestoreBackupCloud.this, SliderTesting.class));
            finish();
        }
    }

    private void screenArrange() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Constant.screenHeight = height;
        Constant.screenWidth = width;

        LinearLayout.LayoutParams buttonLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParama.width = width * 98 / 100;
        buttonLayoutParama.height = height * 7 / 100;
        buttonLayoutParama.setMargins(width * 1 / 100, width * 1 / 100, width * 1 / 100, width * 1 / 100);
        rl_restore.setLayoutParams(buttonLayoutParama);

        LinearLayout.LayoutParams buttonNoParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonNoParama.width = width * 35 / 100;
        buttonNoParama.height = height * 7 / 100;
        buttonNoParama.gravity = Gravity.LEFT;
        btn_no.setLayoutParams(buttonNoParama);
        btn_no.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams buttonRestoreParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonRestoreParama.width = width * 35 / 100;
        buttonRestoreParama.height = height * 7 / 100;
        buttonRestoreParama.leftMargin = width * 28 / 100;
        buttonRestoreParama.gravity = Gravity.RIGHT;
        btn_yes.setLayoutParams(buttonRestoreParama);
        btn_yes.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams buttonLayoutProcessParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutProcessParama.height = height * 7 / 100;
        rl_restore_process.setLayoutParams(buttonLayoutProcessParama);
        rl_restore_process.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams restoreTextParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        restoreTextParama.width = width * 84 / 100;
        restoreTextParama.height = height * 7 / 100;
        restoreTextParama.leftMargin = width * 2 / 100;
        restoreTextParama.rightMargin = width * 2 / 100;
        mProcessText.setLayoutParams(restoreTextParama);
        mProcessText.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams restoreProcesserParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        restoreProcesserParama.width = width * 10 / 100;
        restoreProcesserParama.height = height * 6 / 100;
        restoreProcesserParama.rightMargin = width * 2 / 100;
        progressBar.setLayoutParams(restoreProcesserParama);

        if (width >= 600) {
            btn_no.setTextSize(17);
            btn_yes.setTextSize(17);
            mProcessText.setTextSize(17);
            mText1.setTextSize(20);
            mText2.setTextSize(18);
        } else if (width > 501 && width < 600) {
            btn_no.setTextSize(16);
            btn_yes.setTextSize(16);
            mProcessText.setTextSize(16);
            mText1.setTextSize(19);
            mText2.setTextSize(17);
        } else if (width > 260 && width < 500) {
            btn_no.setTextSize(15);
            btn_yes.setTextSize(15);
            mProcessText.setTextSize(15);
            mText1.setTextSize(18);
            mText2.setTextSize(16);
        } else if (width <= 260) {
            btn_no.setTextSize(13);
            btn_yes.setTextSize(13);
            mProcessText.setTextSize(13);
            mText1.setTextSize(16);
            mText2.setTextSize(14);
        }
    }
}