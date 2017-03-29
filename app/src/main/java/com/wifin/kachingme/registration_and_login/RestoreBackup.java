package com.wifin.kachingme.registration_and_login;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.DatabaseHelper_Backup;
import com.wifin.kachingme.pojo.Chat_list_GetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.util.Constant;
import com.wifin.kaching.me.ui.R;

public class RestoreBackup extends AppCompatActivity implements OnClickListener{

	Button btn_no, btn_yes;
	TextView mText1,mText2,mProcessText;
	DatabaseHelper_Backup dbadapter_backup;
	DatabaseHelper dbadapter;
	LinearLayout rl_restore_process,rl_restore;
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
		mProcessText= (TextView) findViewById(R.id.restore_processText);
		rl_restore = (LinearLayout) findViewById(R.id.restore_backup);
		rl_restore_process = (LinearLayout) findViewById(R.id.restore_backup_process);
		progressBar= (ProgressBar) findViewById(R.id.restore_progressBar);

		Constant.typeFace(this,btn_yes);
		Constant.typeFace(this,btn_no);
		Constant.typeFace(this,mText1);
		Constant.typeFace(this,mText2);
		Constant.typeFace(this,mProcessText);

		btn_no.setOnClickListener(this);
		btn_yes.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_yes:
				new Restore_Async().execute();
				break;

			case R.id.btn_no:
				startActivity(new Intent(RestoreBackup.this, SliderTesting.class));
				finish();
				break;
		}
	}

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
//			File dir = new File(Constant.local_database_dir);
//			File[] files = dir.listFiles();
//			File file = null;
//			Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
//			for (int i = 0; i < files.length; i++) {
//				file = files[i];
//				System.out.printf("File %s - %2$tm %2$te,%2$tY%n= ", file.getName(), file.lastModified());
//				SimpleDateFormat date_format = new SimpleDateFormat("hh:mma ,dd/MM/yyyy");
//				Date dt = new Date(file.lastModified());
//				String date = date_format.format(dt);
//			}
//			if (file != null) {
//				new encry_decry(RestoreBackup.this).decodeFile(file);
//			}
//			dbadapter_backup = DatabaseHelper_Backup
//					.getDBAdapterInstance(RestoreBackup.this);
//			dbadapter_backup.openDataBase();
//			msg_list = dbadapter_backup.getAllMessages();
//			chat_list = dbadapter_backup.getAllChat_list();
//			contact_list = dbadapter_backup.getAllContacts();
//
//			for (int i = 0; i < msg_list.size(); i++) {
//				dbadapter.setInsertMessages(msg_list.get(i));
//			}
//			for (int j = 0; j < chat_list.size(); j++) {
//				dbadapter.setInsertChat_list_chat_all(chat_list.get(j)
//						.getKey_remote_jid(), chat_list.get(j)
//						.getMessage_table_id(), chat_list.get(j)
//						.getIs_sec_chat(), chat_list.get(j)
//						.getUnseen_msg_count());
//
//			}
//			for (int k = 0; k < contact_list.size(); k++) {
//				dbadapter.insertContacts(contact_list.get(k));
//
//			}//siva
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
//			dbadapter_backup.close();//siva
			startActivity(new Intent(RestoreBackup.this, SliderTesting.class));
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
		buttonLayoutParama.width = width*98/100;
		buttonLayoutParama.height=height*7/100;
		buttonLayoutParama.setMargins(width*1/100,width*1/100,width*1/100,width*1/100);
		rl_restore.setLayoutParams(buttonLayoutParama);

		LinearLayout.LayoutParams buttonNoParama = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		buttonNoParama.width = width*35/100;
		buttonNoParama.height = height * 7 / 100;
		buttonNoParama.gravity=Gravity.LEFT;
		btn_no.setLayoutParams(buttonNoParama);
		btn_no.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams buttonRestoreParama = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		buttonRestoreParama.width = width*35/100;
		buttonRestoreParama.height = height * 7 / 100;
		buttonRestoreParama.leftMargin=width*28/100;
		buttonRestoreParama.gravity=Gravity.RIGHT;
		btn_yes.setLayoutParams(buttonRestoreParama);
		btn_yes.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams buttonLayoutProcessParama = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		buttonLayoutProcessParama.height=height*7/100;
		rl_restore_process.setLayoutParams(buttonLayoutProcessParama);
		rl_restore_process.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams restoreTextParama = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		restoreTextParama.width = width*84/100;
		restoreTextParama.height = height * 7 / 100;
		restoreTextParama.leftMargin=width*2/100;
		restoreTextParama.rightMargin=width*2/100;
		mProcessText.setLayoutParams(restoreTextParama);
		mProcessText.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams restoreProcesserParama = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		restoreProcesserParama.width = width*10/100;
		restoreProcesserParama.height = height * 6 / 100;
		restoreProcesserParama.rightMargin=width*2/100;
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
