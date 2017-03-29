package com.wifin.kachingme.settings;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.Chat_list_home_GetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.AlertUtils;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.LastModifiedFileComparator;
import com.wifin.kachingme.util.Utils;
import com.wifin.kachingme.util.encry_decry;

import org.jivesoftware.smackx.bookmarks.BookmarkManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

@SuppressWarnings("deprecation")
public class Chat_Settings extends PreferenceFragment {
    CheckBoxPreference chk_enter_is_send, default_scroll;
    Preference email_conversation, delete_all_conversations, backup_conversation, email_backup_conversation, cloud_backup_conversation, wallpaper, lock_list, archive_list;
    DatabaseHelper dbAdapter;
    Context context;
    Dbhelper db;
    SharedPreferences sp;
    Editor ed;
    int mCountOfLockedList = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_chats);

        context = getActivity();
        db = new Dbhelper(getActivity());
        dbAdapter = KachingMeApplication.getDatabaseAdapter();

        chk_enter_is_send = (CheckBoxPreference) getPreferenceManager().findPreference("enter_is_send");
        default_scroll = (CheckBoxPreference) getPreferenceManager().findPreference("defualt_scroll");
        wallpaper = (Preference) getPreferenceManager().findPreference("wallpaper");
        delete_all_conversations = (Preference) getPreferenceManager().findPreference("delete_all_conversations");
        backup_conversation = (Preference) getPreferenceManager().findPreference("backup_conversation");
        email_backup_conversation = (Preference) getPreferenceManager().findPreference("email_backup_conversation");
        lock_list = (Preference) getPreferenceManager().findPreference("lock_list");
        archive_list = (Preference) getPreferenceManager().findPreference("archive_list");
        sp = PreferenceManager.getDefaultSharedPreferences(context);

        getLastBackupDate();

        lock_list.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // TODO Auto-generated method stub

                String query = "select jid_name,jid from " + Dbhelper.TABLE_LOCK;
                lock_status(query);

                if (mCountOfLockedList > 0) {

                    Intent i = new Intent(context, LockScreen.class);
                    startActivity(i);
                } else {

                    callAlertIfListIsEmpty("There is no locked user");

                }


                return false;
            }
        });

        archive_list.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // TODO Auto-generated method stub
                //

                ArrayList<String> blocked = KachingMeApplication.getBlocked_user();


                Constant.printMsg("size of list :::::: " + blocked.size());
                if (blocked.size() > 0) {
                    Intent i = new Intent(context, blocked_users.class);
                    startActivity(i);

                } else {
                    callAlertIfListIsEmpty("There is no blocked user");

                }


                return false;
            }
        });

        final CharSequence[] options = {
                getResources().getString(R.string.set_wallpaper),
                getResources().getString(R.string.no_wallpaper),
                getResources().getString(R.string.default_wallpaper)};

        wallpaper.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(getResources().getString(R.string.wallpaper));
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            String data = "file";
                            ed = sp.edit();
                            ed.putString("wallpaper_type", data);
                            ed.commit();

                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            photoPickerIntent.setType("image/*");
                            startActivityForResult(photoPickerIntent, 0);
                        } else if (item == 1) {
                            if (KachingMeApplication.getsharedpreferences().contains("wallpaper"))
                                KachingMeApplication.getsharedpreferences_Editor().remove("wallpaper").commit();
                        } else if (item == 2) {
                            String data = "image";
                            ed = sp.edit();
                            ed.putString("wallpaper_type", data);
                            ed.commit();

                            Intent i = new Intent(context, Wallpaper_Activity.class);
                            startActivity(i);
                        }
                    }
                });

                builder.show();

                return false;
            }
        });

        default_scroll.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.d("MyApp", "Pref " + preference.getKey() + " changed to " + newValue.toString());

                if (newValue.toString().equalsIgnoreCase("true")) {
                    com.wifin.kachingme.util.Constant.mDefaultScroll = true;
                } else {
                    com.wifin.kachingme.util.Constant.mDefaultScroll = false;
                }

                ContentValues cv = new ContentValues();
                cv.put("status", com.wifin.kachingme.util.Constant.mDefaultScroll);
                Constant.printMsg("Constant.mDefaultScroll" + com.wifin.kachingme.util.Constant.mDefaultScroll);
                insertToDB(cv);
                return true;
            }
        });

		/*email_conversation.setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(Chat_Settings.this, forward_list.class);
				intent.putExtra("email", "email");
				startActivity(intent);
				return false;
			}
		});*/

        delete_all_conversations.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle(getResources().getString(R.string.delete_all_conversastion));
                alert.setMessage(getResources().getString(R.string.are_sure_delete_coversastion));

                alert.setPositiveButton(getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dbAdapter.setDelete_All_Messages();

                        ArrayList<MessageGetSet> list = dbAdapter.getMessages_group_by_jid();

                        for (int i = 0; i < list.size(); i++) {
                            dbAdapter.setUpdateChat_lits(list.get(i).getKey_remote_jid(), list.get(i).get_id());
                        }
                        Cursor cursor =null;
                        try {
                            String[] val = {null};
                             cursor = dbAdapter.getMUCChat_list_Filter(val[0]);
                            if (cursor.moveToFirst()) {
                                do {
                                    Chat_list_home_GetSet chatData = new Chat_list_home_GetSet();
//                            if (cursor.getInt(12) == 0) {
                                    chatData.setJidId(cursor.getString(8));
                                    String Bookmarked_time = Utils.getBookmarkTime();
                                    Editor editor1 = sp.edit();
                                    editor1.putString(Constant.LAST_REFRESH_TIME + "_"
                                            + cursor.getString(8), Bookmarked_time);
                                    editor1.commit();

                                    BookmarkManager bm1;
                                    try {
                                        bm1 = BookmarkManager
                                                .getBookmarkManager(TempConnectionService.connection);

                                        bm1.addBookmarkedConference(cursor.getString(8), cursor.getString(8), true,
                                                Bookmarked_time, "");


                                    } catch (Exception e1) {
                                        // TODO Auto-generated catch block
                                        // ACRA.getErrorReporter().handleException(e1);
                                        e1.printStackTrace();
                                    }

//                            }
                                } while (cursor.moveToNext());
                            }
                        } catch (Exception e) {

                        }finally {
                            if(cursor!=null)
                                cursor.close();
                        }
                    }
                });

                alert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();

                return false;
            }
        });

        backup_conversation.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new encry_decry(context).saveFile();
                getLastBackupDate();
                Toast.makeText(context, getResources().getString(R.string.backup_data_successfully), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        email_backup_conversation.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (new File(getLastBackupDate()).exists()) {
                    try {
                        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Conversation backup");
                        emailIntent.setType("message/rfc822");
                        emailIntent.setPackage("com.google.android.gm");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(getLastBackupDate())));
                        startActivity(Intent.createChooser(emailIntent, ""));
                    } catch (ActivityNotFoundException e) {
                        AlertDialog.Builder b = new AlertDialog.Builder(context);
                        b.setMessage(getResources().getString(R.string.it_seems_like_no_email)).setCancelable(false);
                        b.setNegativeButton(getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = b.create();
                        alert.show();
                    }
                } else {
                    new AlertUtils().Toast_call(context, getResources().getString(R.string.no_backup_found_in_sdcard));
                }
                return false;
            }
        });
    }

    protected void insertToDB(ContentValues cv) {
        try {
            int a = (int) db.open().getDatabaseObj().insert(Dbhelper.TABLE_DEFAULT_STATUS, null, cv);
            Constant.printMsg("No of inserted rows in zzle seen:::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in ecpl details seen ::::::"
                    + e.toString());
        } finally {
            db.close();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 0)
        {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String filePath = cursor.getString(columnIndex);

            File f = new File(Constant.local_profile_picture_dir + new File(filePath).getName());
            FileChannel inChannel = null, outChannel = null;

            try
            {
                inChannel = new FileInputStream(filePath).getChannel();
                outChannel = new FileOutputStream(f.getAbsolutePath()).getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);

                if (inChannel != null)
                    inChannel.close();
                if (outChannel != null)
                    outChannel.close();

                KachingMeApplication.getsharedpreferences_Editor().putString("wallpaper", Constant.local_profile_picture_dir + new File(filePath).getName()).commit();

                Toast.makeText(context, "Wallpaper Applied",
                        Toast.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {
                // ACRA.getErrorReporter().handleException(e);
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference.getKey().equals("media_auto_download")) {
            startActivity(new Intent(context, NotificationSettings.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .putExtra("TAG", "Media"));
            return true;
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public String getLastBackupDate() {
        String path = "";

        try {
            File dir = new File(Constant.local_database_dir);
            File[] files = dir.listFiles();

            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                System.out.printf("File %s - %2$tm %2$te,%2$tY%n= ",
                        file.getName(), file.lastModified());
                SimpleDateFormat date_format = new SimpleDateFormat("hh:mma ,dd/MM/yyyy");
                Date dt = new Date(file.lastModified());
                String date = date_format.format(dt);
                backup_conversation.setSummary(getResources().getString(R.string.last_backup) + " " + date);
                path = file.getAbsolutePath();
                long si = file.length();
                Log.e("Chat", "Size : " + si + "\n" + "Path : " + path.toString());
            }
        } catch (Exception e) {
            // ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
        }
        return path;
    }

    private void lock_status(String query) {
        // TODO Auto-generated method stub
        Cursor c = null;

        try {

            Constant.printMsg("query  " + query);

            c = db.open().getDatabaseObj().rawQuery(query, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
            mCountOfLockedList = c.getCount();
            // c.moveToFirst();
            if (c.getCount() > 0) {

                while (c.moveToNext()) {


                    Constant.printMsg("lock_status " + c.getString(0));
                }

            }

        } catch (SQLException e) {

        } finally {
            c.close();
            db.close();
        }

    }

    private void callAlertIfListIsEmpty(String msg) {

        AlertDialog.Builder b;
        b = new AlertDialog.Builder(getActivity());

        b.setCancelable(false);
        b.setMessage(msg)
                .setCancelable(false);


        b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        b.setCancelable(true);

        AlertDialog alert = b.create();
        alert.show();

    }
}