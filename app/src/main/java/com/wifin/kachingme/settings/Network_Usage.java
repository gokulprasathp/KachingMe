package com.wifin.kachingme.settings;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kachingme.chat_home.MainActivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.NetworkSharedPreference;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.util.CommonMethods;

public class Network_Usage extends MainActivity {

	TextView message_sent, message_sent_value, message_received,
			message_received_value, media_byte_sent, media_byte_sentvalue,
			media_byte_receive, media_byte_receivevalue, message_byte_sent,
			message_byte_sentvalue, message_byte_receive,
			message_byte_receivevalue, total_bytes_sent, total_bytes_sentvalue,
			total_bytes_receive, total_bytes_receivevalue, last_reset,
			last_resetvalue;
	Button reset_statistics;
	SharedPreferences pref;
	double totalData = 0.0;
	Editor editor;

	// New Code
	private double mStartRX = 0;
	private double mStartTX = 0;
	private NetworkSharedPreference mNewtSharPref;
	private static DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
	private static final double BYTE = 1024, KB = BYTE, MB = KB * BYTE, GB = MB
			* BYTE;
	int uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
		View.inflate(this, R.layout.network_usage, vg);
		sideMenufoot.setVisibility(View.VISIBLE);
		logo.setVisibility(View.INVISIBLE);
		footer.setVisibility(View.GONE);
		head.setText("Network Usage");
		back.setVisibility(View.VISIBLE);
		head.setTextColor(Color.parseColor("#FFFFFF"));
		back.setBackgroundResource(R.drawable.arrow);
		headlay.setBackgroundColor(Color.parseColor("#FE0000"));
		Ka_newlogo.setVisibility(View.INVISIBLE);

		mNewtSharPref = new NetworkSharedPreference(this);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(Network_Usage.this,
						Settings_account.class);
				startActivity(intent);
				finish();
			}
		});
		init();
		pref = PreferenceManager.getDefaultSharedPreferences(this);

		Constant.printMsg("last_reset   " + pref.getString("last_reset", ""));
		String var = pref.getString("last_reset", "");
		if (!var.equalsIgnoreCase("") || var != null) {
			last_resetvalue.setText(var);

		}

		int received_msg_count = pref.getInt("received_msg_count", 0);
		int sent_msg_count = pref.getInt("sent_msg_count", 0);
		Constant.printMsg("received msg count in network usage is::"
                + received_msg_count + "sent_msg_count" + sent_msg_count);
		message_received_value.setText(String.valueOf(received_msg_count));
		message_sent_value.setText(String.valueOf(sent_msg_count));
		screenArrange();
		// Showing the newtwork usage data
		getNewDataUsage();
		reset_statistics.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				showConfirmationDialog();

			}
		});

	}

	public void setLastTotalData() {
		mNewtSharPref.setTotalData_Sent(String.valueOf(TrafficStats
				.getUidTxBytes(uid)));
		mNewtSharPref.setTotalData_Receive(String.valueOf(TrafficStats
				.getUidRxBytes(uid)));
	}

	public void getNewDataUsage() {
		/*
		 * int uid = android.os.Process.myUid(); long mStartRX= 0; long mStartTX
		 * = 0;
		 * 
		 * Constant.printMsg("Total :" + TrafficStats.getUidRxBytes(uid) +"-"
		 * +getTotal_Cur_Receive_sp()+"--" + getTotalReceive_sp() +"-- " +
		 * TrafficStats.getUidTxBytes(uid) +"--" + getTotalSent_sp());
		 * 
		 * //mStartTX = TrafficStats.getUidTxBytes(uid) - getTotalSent_sp();
		 * //mStartRX = TrafficStats.getUidRxBytes(uid) - getTotalReceive_sp();
		 * 
		 * 
		 * if( getTotal_Cur_Receive_sp() > TrafficStats.getUidRxBytes(uid) ) {
		 * 
		 * mStartRX = (getTotal_Cur_Receive_sp() +
		 * TrafficStats.getUidRxBytes(uid)) - getTotalReceive_sp();
		 * mNewtSharPref
		 * .setTotalData_Ar_Recev(String.valueOf(getTotal_Cur_Receive_sp() +
		 * TrafficStats.getUidRxBytes(uid)));
		 * 
		 * 
		 * }else { if(TrafficStats.getUidRxBytes(uid) > getTotalReceive_sp()) {
		 * mStartRX = TrafficStats.getUidRxBytes(uid) - getTotalReceive_sp();
		 * }else { mStartRX = TrafficStats.getUidRxBytes(uid); }
		 * mNewtSharPref.setTotalData_Ar_Recev
		 * (String.valueOf(TrafficStats.getUidRxBytes(uid))); }
		 * 
		 * 
		 * 
		 * if(getTotal_Cur_Sent_sp() > TrafficStats.getUidTxBytes(uid)) {
		 * mStartTX = (getTotal_Cur_Sent_sp() + TrafficStats.getUidTxBytes(uid))
		 * - getTotalSent_sp();
		 * mNewtSharPref.setTotalData_Ar_Sent(String.valueOf
		 * (getTotal_Cur_Sent_sp() + TrafficStats.getUidTxBytes(uid))); }else {
		 * if(TrafficStats.getUidTxBytes(uid) > getTotalSent_sp()) { mStartTX =
		 * TrafficStats.getUidTxBytes(uid) - getTotalSent_sp(); }else { mStartTX
		 * = TrafficStats.getUidTxBytes(uid); }
		 * mNewtSharPref.setTotalData_Ar_Sent
		 * (String.valueOf(TrafficStats.getUidTxBytes(uid))); }
		 * 
		 * total_bytes_receivevalue.setText(convertBytesToSuitableUnit(String.
		 * valueOf(mStartRX)));
		 * total_bytes_sentvalue.setText(convertBytesToSuitableUnit
		 * (String.valueOf(mStartTX)));
		 */

		getNetworkUsageData();
	}

	public long getTotalReceive_sp() {
		HashMap<String, String> user = mNewtSharPref.getTotal_ReceiveData();

		String recev = user.get(NetworkSharedPreference.KEY_TOTAL_GET_RX);

		return Long.parseLong(recev);
	}

	public long getTotal_Cur_Receive_sp() {
		HashMap<String, String> user = mNewtSharPref.getTotal_Cur_ReceiveData();

		String recev = user.get(NetworkSharedPreference.KEY_TOTAL_AR_GET_RX);

		return Long.parseLong(recev);
	}

	public long getTotalSent_sp() {
		HashMap<String, String> user = mNewtSharPref.getTotal_SentData();
		String sent = user.get(NetworkSharedPreference.KEY_TOTAL_GET_SX);

		return Long.parseLong(sent);

	}

	public long getTotal_Cur_Sent_sp() {
		HashMap<String, String> user = mNewtSharPref.getTotal_Cur_SentData();

		String recev = user.get(NetworkSharedPreference.KEY_TOTAL_AR_GET_SX);

		return Long.parseLong(recev);
	}

	public void getDataUsageDetails() {
		final PackageManager pm = getPackageManager();
		// get a list of installed apps.
		List<ApplicationInfo> packages = pm.getInstalledApplications(0);

		// loop through the list of installed packages and see if the selected
		// app is in the list
		for (ApplicationInfo packageInfo : packages) {
			// get the UID for the selected app
			int UID = packageInfo.uid;
			Constant.printMsg("uid::" + UID);
			String package_name = packageInfo.packageName;
			if (package_name.equalsIgnoreCase("com.wifin.kaching.me.ui")) {
				ApplicationInfo app = null;
				try {
					app = pm.getApplicationInfo(package_name, 0);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String name = (String) pm.getApplicationLabel(app);
				Drawable icon = pm.getApplicationIcon(app);
				// internet usage for particular app(sent and received)
				double received = (double) TrafficStats.getUidRxBytes(UID)

				/ (1024 * 1024);
				double send = (double) TrafficStats.getUidTxBytes(UID)
						/ (1024 * 1024);
				Constant.printMsg("received::" + received + "send::" + send);
				total_bytes_receivevalue.setText(String
						.format("%.2f", received));
				total_bytes_sentvalue.setText(String.format("%.2f", send));
				double total = received + send;

				if (total > 0) {
					Constant.printMsg("name is::" + name + "package_name"
							+ package_name + "  " + "uid::" + UID + "  "
							+ String.format("%.2f", total) + " MB");
					// PackageInformationTotal pi = new
					// PackageInformationTotal();
					// pi.name = name;
					// pi.packageName = package_name;
					// pi.icon = icon;
					// pi.totalMB = String.format("%.2f", total) + " MB";
					// pi.individual_mb = String.format("%.2f", total);
					totalData += Double.parseDouble(String
							.format("%.2f", total));
					// dataHash.add(pi);
					// Log.e(name, String.format("%.2f", total) + " MB");
				}
                Constant.printMsg("Total" + String.format("%.2f", totalData));
			}

		}
	}

	private void screenArrange() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		height = displayMetrics.heightPixels;
		width = displayMetrics.widthPixels;
		LinearLayout.LayoutParams textlay = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		textlay.width = width * 48 / 100;
		// textlay.gravity = Gravity.CENTER | Gravity.LEFT;
		// textlay.leftMargin = width * 3 / 100;
		textlay.gravity = Gravity.CENTER;
		textlay.rightMargin = width * 14 / 100;
		head.setLayoutParams(textlay);
		head.setGravity(Gravity.CENTER | Gravity.LEFT);
		LinearLayout.LayoutParams layoutMenuq = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutMenuq.width = width * 50 / 100;
		layoutMenuq.height = height * 4 / 100;
		layoutMenuq.setMargins(width * 1 / 100, height * 1 / 100,
				width * 1 / 100, height * 1 / 100);
		// layoutMenuq.gravity = Gravity.CENTER;
		message_sent.setLayoutParams(layoutMenuq);
		message_received.setLayoutParams(layoutMenuq);
		media_byte_sent.setLayoutParams(layoutMenuq);
		media_byte_receive.setLayoutParams(layoutMenuq);
		message_byte_sent.setLayoutParams(layoutMenuq);
		message_byte_receive.setLayoutParams(layoutMenuq);
		total_bytes_sent.setLayoutParams(layoutMenuq);

		total_bytes_receive.setLayoutParams(layoutMenuq);

		LinearLayout.LayoutParams layoutMenuq1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutMenuq1.width = width * 50 / 100;
		// layoutMenuq1.height = height * 4 / 100;
		layoutMenuq1.setMargins(width * 1 / 100, height * 1 / 100,
				width * 1 / 100, height * 1 / 100);
		reset_statistics.setLayoutParams(layoutMenuq1);
		last_resetvalue.setLayoutParams(layoutMenuq1);

		LinearLayout.LayoutParams rightvalue = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		rightvalue.width = width * 35 / 100;
		rightvalue.height = height * 4 / 100;
		rightvalue.setMargins(width * 1 / 100, height * 1 / 100,
				width * 1 / 100, height * 1 / 100);
		message_sent_value.setLayoutParams(rightvalue);
		message_received_value.setLayoutParams(rightvalue);
		media_byte_sentvalue.setLayoutParams(rightvalue);
		media_byte_receivevalue.setLayoutParams(rightvalue);
		message_byte_sentvalue.setLayoutParams(rightvalue);
		message_byte_receivevalue.setLayoutParams(rightvalue);
		total_bytes_sentvalue.setLayoutParams(rightvalue);
		last_reset.setLayoutParams(rightvalue);

		total_bytes_receivevalue.setLayoutParams(rightvalue);
	}

	private void init() {
		// TODO Auto-generated method stub
		message_sent = (TextView) findViewById(R.id.message_sent);
		message_sent_value = (TextView) findViewById(R.id.message_sent_value);
		message_received = (TextView) findViewById(R.id.message_received);
		message_received_value = (TextView) findViewById(R.id.message_received_value);
		media_byte_sent = (TextView) findViewById(R.id.media_byte_sent);
		media_byte_sentvalue = (TextView) findViewById(R.id.media_byte_sentvalue);
		media_byte_receive = (TextView) findViewById(R.id.media_byte_receive);
		media_byte_receivevalue = (TextView) findViewById(R.id.media_byte_receivevalue);
		message_byte_sent = (TextView) findViewById(R.id.message_byte_sent);
		message_byte_sentvalue = (TextView) findViewById(R.id.message_byte_sentvalue);

		message_byte_receive = (TextView) findViewById(R.id.message_byte_receive);
		message_byte_receivevalue = (TextView) findViewById(R.id.message_byte_receivevalue);
		total_bytes_sent = (TextView) findViewById(R.id.total_bytes_sent);
		total_bytes_sentvalue = (TextView) findViewById(R.id.total_bytes_sentvalue);
		total_bytes_receive = (TextView) findViewById(R.id.total_bytes_receive);
		total_bytes_receivevalue = (TextView) findViewById(R.id.total_bytes_receivevalue);

		reset_statistics = (Button) findViewById(R.id.reset_statistics);
		last_reset = (TextView) findViewById(R.id.last_reset);
		last_resetvalue = (TextView) findViewById(R.id.last_resetvalue);

        Constant.typeFace(this, message_sent);
        Constant.typeFace(this, message_sent_value);
        Constant.typeFace(this, message_received);
        Constant.typeFace(this, message_received_value);
        Constant.typeFace(this, media_byte_sent);
        Constant.typeFace(this, media_byte_sentvalue);
        Constant.typeFace(this, media_byte_receive);
        Constant.typeFace(this, media_byte_receivevalue);
        Constant.typeFace(this, message_byte_sent);
        Constant.typeFace(this, message_byte_sentvalue);
        Constant.typeFace(this, message_byte_receive);
        Constant.typeFace(this, message_byte_receivevalue);
        Constant.typeFace(this, total_bytes_sent);
        Constant.typeFace(this, total_bytes_sentvalue);
        Constant.typeFace(this, total_bytes_receive);
        Constant.typeFace(this, total_bytes_receivevalue);
        Constant.typeFace(this, reset_statistics);
        Constant.typeFace(this, last_reset);
        Constant.typeFace(this, last_resetvalue);

    }

	public void showConfirmationDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Do you want to reset ?");
		// alert.setMessage("Message");
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				// Resetting Network Shared Preference
				mNewtSharPref.clearDataNewtork();
				setLastTotalData();

				Calendar c = Calendar.getInstance();
				Constant.printMsg("Current time => " + c.getTime());
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy/MM/dd_HH:mm:ss");
				String formattedDate = df.format(c.getTime());
				editor = pref.edit();
				// editor.putBoolean("freebie_status", true);
				editor.putString("last_reset", formattedDate);
				editor.commit();

				last_resetvalue.setText(formattedDate);
				// reset_statistics.setText("Reset Statistics " + "         "
				// + formattedDate);
				message_received_value.setText("0");
				message_sent_value.setText("0");

				editor = pref.edit();
				// editor.putBoolean("freebie_status", true);
				editor.putInt("received_msg_count", 0);
				editor.putInt("sent_msg_count", 0);

				editor.commit();

				getNewDataUsage();

			}
		});

		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();
	}

	public void getNetworkUsageData() {
		long total_sent = 0;
		long total_recev = 0;
		// get user data from session
		HashMap<String, String> user = mNewtSharPref.getAll_Details();

		// name
		String media_sent = user.get(NetworkSharedPreference.KEY_MEDIA_GET_SX);
		String media_recev = user.get(NetworkSharedPreference.KEY_MEDIA_GET_RX);

		String msg_sent = user.get(NetworkSharedPreference.KEY_MESSAGE_GET_SX);
		String msg_recev = user.get(NetworkSharedPreference.KEY_MESSAGE_GET_RX);

		media_byte_sentvalue.setText(convertBytesToSuitableUnit(media_sent));
		media_byte_receivevalue
				.setText(convertBytesToSuitableUnit(media_recev));

		message_byte_receivevalue
				.setText(convertBytesToSuitableUnit(msg_recev));
		message_byte_sentvalue.setText(convertBytesToSuitableUnit(msg_sent));

		try {

			if (media_sent != null) {
				total_sent = total_sent + Long.parseLong(media_sent);
			}

			if (media_recev != null) {
				total_recev = total_recev + Long.parseLong(media_recev);
			}

			if (msg_sent != null) {
				total_sent = total_sent + Long.parseLong(msg_sent);
			}

			if (msg_recev != null) {
				total_recev = total_recev + Long.parseLong(msg_recev);
			}
		} catch (Exception e) {

		}

		total_bytes_receivevalue.setText(convertBytesToSuitableUnit(String
				.valueOf(total_recev)));
		total_bytes_sentvalue.setText(convertBytesToSuitableUnit(String
				.valueOf(total_sent)));

	}

	// Converting the bytes into MB, GB, KB
	public static String convertBytesToSuitableUnit(String bytess) {
		try {

			if (bytess != null) {
				bytess = trimTrailingZeros(bytess);

				long bytes = Long.parseLong(bytess);
				String bytesToSuitableUnit = bytes + " B";

				if (bytes >= GB) {
					double tempBytes = bytes / GB;
					bytesToSuitableUnit = twoDecimalForm.format(tempBytes)
							+ " GB";
					return bytesToSuitableUnit;
				}

				if (bytes >= MB) {
					double tempBytes = bytes / MB;
					bytesToSuitableUnit = twoDecimalForm.format(tempBytes)
							+ " MB";
					return bytesToSuitableUnit;
				}

				if (bytes >= KB) {
					double tempBytes = bytes / KB;
					bytesToSuitableUnit = twoDecimalForm.format(tempBytes)
							+ " KB";
					return bytesToSuitableUnit;
				}

				return bytesToSuitableUnit;
			} else {
				return "0";
			}

		} catch (Exception e) {
			return "0";
		}
	}

	private static String trimTrailingZeros(String number) {
		if (!number.contains(".")) {
			return number;
		}

		return number.replaceAll("\\.?0*$", "");
	}
}
