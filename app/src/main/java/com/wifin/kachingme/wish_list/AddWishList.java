package com.wifin.kachingme.wish_list;

import java.util.Calendar;

import a_vcard.android.util.Log;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.wifin.kachingme.chat_home.MainActivity;
import com.wifin.kachingme.deals.WishActivity;
import com.wifin.kachingme.deals.WishListActivity;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Validation;
import com.wifin.kachingme.services.AlarmReceiver;
import com.wifin.kaching.me.ui.R;

public class AddWishList extends MainActivity {

	AlarmManager alarmManager;
	private PendingIntent pendingIntent;
	EditText rec;
	TextView occ, date, rem, datet;
	Spinner wisp;
	ToggleButton on;
	LinearLayout btnlay;
	Button can, sv, sel, selt;
	int widht, height;
	static final int DATE_DIALOG_FROMID = 1, TIME_DIALOG_FROMID = 2;
	Calendar cal;
	private int year, month, day, hour, min, sec;
	int cur = 0, cur1 = 0;
	private static AddWishList inst;
	Calendar calendar;
	Dbhelper db;
	SharedPreferences sp;
	int Count = 1;

	public static AddWishList instance() {
		return inst;
	}

	@Override
	public void onStart() {
		super.onStart();
		inst = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
		View.inflate(this, R.layout.activity_addwish, vg);
		logo.setVisibility(ImageView.GONE);
		back.setVisibility(ImageView.VISIBLE);
		cart.setVisibility(ImageView.INVISIBLE);
		head.setText("Wish List");
		Init();
		Screen();
		db = new Dbhelper(getApplicationContext());
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		can.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(AddWishList.this, WishActivity.class);
				startActivity(i);
				finish();
			}
		});

		sv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (CheckValidation()) {

					ContentValues cv = new ContentValues();

					cv.put("name", Constant.wishlistname);
					cv.put("recipientnane", rec.getText().toString().trim());
					cv.put("datetext", date.getText().toString().trim());
					cv.put("occatext", wisp.getSelectedItem().toString());

					insertDB(cv);

					int point = sp.getInt("wishpoint", 0);

					Constant.totalwish = point;

					Constant.totalwish = Count + Constant.totalwish;

					Editor e1 = sp.edit();
					e1.putInt("wishpoint", Constant.totalwish);
					e1.commit();

					Constant.bux = sp.getLong("buxvalue", 0);

					Long buxval = Constant.bux + Constant.wishpoints;
					Constant.bux = buxval;

					Editor e = sp.edit();
					e.putLong("buxvalue", buxval);
					e.commit();

					Intent i = new Intent(AddWishList.this,
							WishListActivity.class);
					startActivity(i);
					finish();

				} else {

					Toast.makeText(getApplicationContext(),
							"Please enter the recipient", Toast.LENGTH_SHORT)
							.show();

				}
			}
		});

		sel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				showDialog(DATE_DIALOG_FROMID);

			}
		});

		selt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				showDialog(TIME_DIALOG_FROMID);

			}
		});

		on.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String tx = sel.getText().toString().trim();
				if (tx.equalsIgnoreCase("Select a Date")) {

					Toast.makeText(getApplicationContext(),
							"Please select the reminder date",
							Toast.LENGTH_SHORT).show();

				} else {
					if (on.isChecked()) {

						Log.d("MyActivity", "Alarm On");
						calendar = Calendar.getInstance();

						calendar.set(Calendar.DAY_OF_MONTH, day);

						calendar.set(Calendar.MONTH, month);

						calendar.set(Calendar.YEAR, year);

						Intent myIntent = new Intent(AddWishList.this,
								AlarmReceiver.class);
						pendingIntent = PendingIntent.getBroadcast(
								AddWishList.this, 0, myIntent, 0);
						alarmManager.set(AlarmManager.RTC,
								calendar.getTimeInMillis(), pendingIntent);

					} else {

						alarmManager.cancel(pendingIntent);
						sel.setText("Select a Date");
						Log.d("MyActivity", "Alarm Off");

					}
				}
			}

		});

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(AddWishList.this, WishActivity.class);
				startActivity(i);
				finish();

			}
		});

	}

	public void insertDB(ContentValues v) {

		try {
			int a = (int) db.open().getDatabaseObj()
					.insert(Dbhelper.TABLE_WISH, null, v);

			Constant.printMsg("No of inserted rows in shop details :::::::::"
					+ a);
		} catch (SQLException e) {
			Constant.printMsg("Sql exception in new shop details ::::::"
					+ e.toString());
		} finally {
			db.close();
		}

	}

	private boolean CheckValidation() {
		// TODO Auto-generated method stub
		boolean valueReturn = true;

		if (!Validation.hasText(rec))
			valueReturn = false;

		return valueReturn;
	}

	private void Screen() {
		// TODO Auto-generated method stub

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		height = metrics.heightPixels;
		width = metrics.widthPixels;

		LinearLayout.LayoutParams headertext1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		headertext1.height = height * 8 / 100;
		headertext1.width = width * 95 / 100;
		headertext1.gravity = Gravity.CENTER;
		headertext1.setMargins(width * 1 / 100, height * 1 / 100,
				width * 1 / 100, height * 1 / 100);
		rec.setLayoutParams(headertext1);
		wisp.setLayoutParams(headertext1);
		btnlay.setLayoutParams(headertext1);
		btnlay.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams lay = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lay.height = height * 12 / 100;
		lay.width = width * 95 / 100;
		lay.gravity = Gravity.CENTER;
		lay.setMargins(width * 1 / 100, height * 1 / 100, width * 1 / 100,
				height * 1 / 100);

		btnlay.setLayoutParams(lay);
		btnlay.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams headerocc = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		headerocc.height = height * 7 / 100;
		headerocc.width = width * 95 / 100;
		headerocc.gravity = Gravity.CENTER;
		headerocc.setMargins(width * 3 / 100, height * 2 / 100,
				width * 1 / 100, height * 1 / 100);
		occ.setLayoutParams(headerocc);
		date.setLayoutParams(headerocc);
		rem.setLayoutParams(headerocc);
		datet.setLayoutParams(headerocc);

		LinearLayout.LayoutParams sel = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		sel.height = height * 6 / 100;
		sel.width = width * 70 / 100;
		sel.setMargins(width * 6 / 100, 0, width * 6 / 100, height * 1 / 100);
		this.sel.setLayoutParams(sel);
		selt.setLayoutParams(sel);

		LinearLayout.LayoutParams seltog = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		seltog.height = height * 8 / 100;
		seltog.width = width * 40 / 100;
		seltog.setMargins(width * 6 / 100, 0, width * 1 / 100, height * 1 / 100);
		on.setLayoutParams(seltog);

		LinearLayout.LayoutParams canbt = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		canbt.height = height * 8 / 100;
		canbt.width = width * 40 / 100;
		canbt.setMargins(width * 4 / 100, height * 2 / 100, width * 4 / 100, 0);
		can.setLayoutParams(canbt);
		sv.setLayoutParams(canbt);

		if (width >= 600) {

			rec.setTextSize(17);
			occ.setTextSize(17);
			date.setTextSize(17);
			rem.setTextSize(17);
			can.setTextSize(17);
			sv.setTextSize(17);

		} else if (width > 501 && width < 600) {

			rec.setTextSize(16);
			occ.setTextSize(16);
			date.setTextSize(16);
			rem.setTextSize(16);
			sv.setTextSize(16);
			can.setTextSize(16);

		} else if (width > 260 && width < 500) {

			rec.setTextSize(15);
			occ.setTextSize(15);
			date.setTextSize(15);
			rem.setTextSize(15);
			can.setTextSize(15);
			sv.setTextSize(15);

		} else if (width <= 260) {

			rec.setTextSize(14);
			occ.setTextSize(14);
			date.setTextSize(14);
			rem.setTextSize(14);
			can.setTextSize(14);
			sv.setTextSize(14);

		}

	}

	private void Init() {
		// TODO Auto-generated method stub

		rec = (EditText) findViewById(R.id.recipientedit);
		occ = (TextView) findViewById(R.id.occationtext);
		date = (TextView) findViewById(R.id.datetext);
		rem = (TextView) findViewById(R.id.remindertext);
		wisp = (Spinner) findViewById(R.id.wishspinner);
		on = (ToggleButton) findViewById(R.id.toggleButton1);
		btnlay = (LinearLayout) findViewById(R.id.wishbuttonlay);
		can = (Button) findViewById(R.id.wishcancel);
		sv = (Button) findViewById(R.id.wishsave);
		sel = (Button) findViewById(R.id.selectdate);
		selt = (Button) findViewById(R.id.selecttime);
		datet = (TextView) findViewById(R.id.timetext);
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Constant.typeFace(this,rec);
        Constant.typeFace(this,occ);
        Constant.typeFace(this,date);
        Constant.typeFace(this,rem);
        Constant.typeFace(this,can);
        Constant.typeFace(this,sv);
        Constant.typeFace(this,sel);
        Constant.typeFace(this,selt);
        Constant.typeFace(this,datet);
    }

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {

		case DATE_DIALOG_FROMID:
			Constant.printMsg("onCreateDialog  : " + id);
			cur = DATE_DIALOG_FROMID;
			/** set date picker as current date */
			DatePickerDialog dialog = new DatePickerDialog(this,
					datePickerListener, year, month, day);

			// dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

			dialog.getDatePicker()
					.setMinDate(System.currentTimeMillis() - 1000);
			//
			// dialog.getDatePicker().setMaxDate(
			// System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 2));

			return dialog;

		case TIME_DIALOG_FROMID:
			Constant.printMsg("onCreateDialog  : " + id);
			cur1 = TIME_DIALOG_FROMID;
			/** set date picker as current date */

			TimePickerDialog dialogtime = new TimePickerDialog(this,
					timePickerListener, hour, min, true);

			// dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

			// dialog.getDatePicker().setMaxDate(
			// System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 2));

			return dialogtime;

		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			if (cur == DATE_DIALOG_FROMID) {

				sel.setText("  "
						+ new StringBuilder().append(month + 1).append("-")
								.append(day).append("-").append(year)
								.append(" "));

			}

		}
	};

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			hour = hourOfDay;
			min = minute;

			if (cur1 == TIME_DIALOG_FROMID) {

				selt.setText("  "
						+ new StringBuilder().append(hour).append(":")
								.append(min).append(" "));

			}

		}

	};

}
