package com.wifin.kachingme.settings;

import java.util.ArrayList;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.wifin.kachingme.applications.SherlockBaseActivity;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.util.Constant;

public class StatusUpdate extends SherlockBaseActivity {

	ProgressDialog progressdialog;

	TextView txt_status;
	ImageButton btn_edit;

	String status;
	DatabaseHelper dbadapter;
	ListView listview;
	VCard vc = new VCard();
	// private ChatManager chatManager;
	private Chat chat;
	String[] values = new String[] {};
	Context context;
	public static String TAG = Status.class.getSimpleName();
	String roster[];
	ArrayList<String> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.status_update);

		// txt_status = (TextView) findViewById(R.id.status_txt_status);
		// btn_edit = (ImageButton) findViewById(R.id.status_btn_edit);

		Intent intent = new Intent(StatusUpdate.this, Status.class);
		// Log.i("Forword", "Message IDS " + msg_ids);
		// intent.putExtra("status_update", "vino");
		Constant.printMsg("cam eto status update::");
		startActivity(intent);
	}
}
