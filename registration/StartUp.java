package com.wifin.kachingme.registration_and_login;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wifin.kachingme.R;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.util.Constant;

public class StartUp extends Activity implements AnimationListener {
	ImageView web, web1, web2, web3, web4, web5, web6, web7;
	TextView marqueeText, wtext, marqueeText1, wtext1, marqueeText2, wtext2,
			marqueeText3, wtext3;
	Handler mHandler = new Handler();
	Animation animFadein;
	private ImageLoadingListener animateFirstListener = new NewStartUpActivity.AnimateFirstDisplayListener();
	private DisplayImageOptions options;
	LinearLayout ly;
	int count = 1000;
	Dbhelper db;
	String mFreebieLastDay;
	Date mFreebieLastDayDate, mTodayDate;
	SharedPreferences sharedpreferences;
	Editor editor;
	int listposition, spinnerposition;
	String mCompany;
	Handler handler = new Handler();
	Runnable run;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		db = new Dbhelper(getApplicationContext());

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.stub).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).build();

		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this));

		web = (ImageView) findViewById(R.id.web_st);
		web1 = (ImageView) findViewById(R.id.web_st1);
		web2 = (ImageView) findViewById(R.id.web_st2);
		web3 = (ImageView) findViewById(R.id.web_st11);
		web5 = (ImageView) findViewById(R.id.web_st3);
		web4 = (ImageView) findViewById(R.id.web_st22);
		web6 = (ImageView) findViewById(R.id.web_st4);
		web7 = (ImageView) findViewById(R.id.web_st33);
		marqueeText = (TextView) findViewById(R.id.scrolltext_st);

		wtext = (TextView) findViewById(R.id.webtext_st);
		wtext1 = (TextView) findViewById(R.id.webtext_st1);
		wtext2 = (TextView) findViewById(R.id.webtext_st2);
		wtext3 = (TextView) findViewById(R.id.webtext_st3);

		ly = (LinearLayout) findViewById(R.id.startlay);
		sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		setActivitystage("StartUp");
		
		listposition = sharedpreferences.getInt("listpos", 0); // 2
		spinnerposition = sharedpreferences.getInt("spinnerpos", 0); // 2
		mCompany = sharedpreferences.getString("company", "");
		Constant.printMsg("posss::::::::>>>>>>" + listposition
				+ spinnerposition + "   "
				+ Constant.freelistmain.get(listposition).getCompanyName());
		ImageLoader.getInstance().displayImage(
				String.valueOf(
						Constant.freelistmain.get(listposition)
								.getCompanyLogoPath()).replaceAll(" ", "%20"),
				web, options, animateFirstListener);
		ImageLoader.getInstance().displayImage(
				String.valueOf(
						Constant.freelistmain.get(listposition)
								.getCompanyLogoPath()).replaceAll(" ", "%20"),
				web2, options, animateFirstListener);
		ImageLoader.getInstance().displayImage(
				String.valueOf(
						Constant.freelistmain.get(listposition)
								.getCompanyLogoPath()).replaceAll(" ", "%20"),
				web5, options, animateFirstListener);
		ImageLoader.getInstance().displayImage(
				String.valueOf(
						Constant.freelistmain.get(listposition)
								.getCompanyLogoPath()).replaceAll(" ", "%20"),
				web6, options, animateFirstListener);
		marqueeText.setVisibility(View.GONE);
		marqueeText.setText(Constant.freelistmain.get(listposition)
				.getFreebilist().get(spinnerposition).getTagLine());

		// wtext.setText(mCompany);
		wtext.setText(Constant.freelistmain.get(listposition).getFreebilist()
				.get(spinnerposition).getTagLine());
		wtext1.setText(Constant.freelistmain.get(listposition).getFreebilist()
				.get(spinnerposition).getTagLine());
		wtext2.setText(Constant.freelistmain.get(listposition).getFreebilist()
				.get(spinnerposition).getTagLine());
		wtext3.setText(Constant.freelistmain.get(listposition).getFreebilist()
				.get(spinnerposition).getTagLine());
		ImageLoader.getInstance().displayImage(
				String.valueOf(
						Constant.freelistmain.get(listposition).getFreebilist()
								.get(spinnerposition).getPhotoPath())
						.replaceAll(" ", "%20"), web1, options,
				animateFirstListener);
		ImageLoader.getInstance().displayImage(
				String.valueOf(
						Constant.freelistmain.get(listposition).getFreebilist()
								.get(spinnerposition).getPhotoPath())
						.replaceAll(" ", "%20"), web3, options,
				animateFirstListener);
		ImageLoader.getInstance().displayImage(
				String.valueOf(
						Constant.freelistmain.get(listposition).getFreebilist()
								.get(spinnerposition).getPhotoPath())
						.replaceAll(" ", "%20"), web4, options,
				animateFirstListener);
		ImageLoader.getInstance().displayImage(
				String.valueOf(
						Constant.freelistmain.get(listposition).getFreebilist()
								.get(spinnerposition).getPhotoPath())
						.replaceAll(" ", "%20"), web7, options,
				animateFirstListener);
		animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.movestartup);

		// set animation listener
		animFadein.setAnimationListener(this);

		// web.startAnimation(animFadein);
		// marqueeText.startAnimation(animFadein);
		Calendar c = Calendar.getInstance();
		Constant.printMsg("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df.format(c.getTime());

		String query = "select date  from " + Dbhelper.TABLE_FREEBIE;
		callFreebieDB(query);

		mTodayDate = new Date();
		try {
			try {
				mTodayDate = df.parse(formattedDate);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mFreebieLastDayDate = new Date();
		try {
			mFreebieLastDayDate = df.parse(mFreebieLastDay);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Constant.printMsg("last::::" + mFreebieLastDayDate + "   "
				+ mTodayDate);
		if (mTodayDate.after(mFreebieLastDayDate)) {
			Intent intent = new Intent(StartUp.this, WelcomeActivity.class);
			startActivity(intent);
			finish();
		} else {
			ly.startAnimation(animFadein);
			animFadein.setFillAfter(true);
			handler.postDelayed(run = new Runnable() {
				public void run() {
					ly.clearAnimation();
					// animFadein.setFillAfter(true);
//					if (Constant.mFrebieScroll == true) {
//						Intent intent = new Intent(StartUp.this,
//								SliderTesting.class);
//						startActivity(intent);
//						finish();
//						siva
//					} else {

						Intent intent = new Intent(StartUp.this,
								WelcomeActivity.class);
						startActivity(intent);
						finish();

//					}
				}
			}, count * 15);
		}

		// call(count);
		// screenArrangement();
		// ly.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// onStop();
		// count = 0;
		// ly.clearAnimation();
		// if (Constant.mFrebieScroll == true) {
		// Intent intent = new Intent(StartUp.this,
		// SliderTesting.class);
		// startActivity(intent);
		// finish();
		// Thread.interrupted();
		// } else {
		//
		// Intent intent = new Intent(StartUp.this,
		// WelcomeActivity.class);
		// startActivity(intent);
		// finish();
		// Thread.interrupted();
		//
		// }
		//
		// }
		// });

	}

	private void setActivitystage(String activity) {
		// TODO Auto-generated method stub
		editor = sharedpreferences.edit();
		editor.putString("activity_name", activity);

		editor.commit();
	}



	private void callFreebieDB(String query) {
		// TODO Auto-generated method stub
		Cursor c = null;

		try {
			c = db.open().getDatabaseObj().rawQuery(query, null);
			Constant.printMsg("The selected elist activity count is ::::::"
					+ c.getCount());
			if (c.getCount() > 0) {
				Constant.printMsg("Caling sysout:::::::::::::::::::::::::");
				while (c.moveToNext()) {
					mFreebieLastDay = c.getString(0);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void screenArrangement() {
		// TODO Auto-generated method stub
		if (getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int height = metrics.heightPixels;
			int width = metrics.widthPixels;

			float density = getResources().getDisplayMetrics().density;
			float heightinDP = metrics.heightPixels / density;
			float widthinDP = metrics.widthPixels / density;

			LinearLayout.LayoutParams Textparams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			Textparams.width = width * 23 / 100;
			Textparams.height = height * 80 / 100;
			Textparams.gravity = Gravity.CENTER;
			web.setLayoutParams(Textparams);
			web1.setLayoutParams(Textparams);

			LinearLayout.LayoutParams Textparams1 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			// Textparams1.width = width * 26 / 100;
			// Textparams1.height = height * 80 / 100;
			// Textparams1.topMargin = (int) (height * 0.8 / 100);
			Textparams1.gravity = Gravity.CENTER;
			marqueeText.setLayoutParams(Textparams1);
			marqueeText.setGravity(Gravity.CENTER);
			wtext.setLayoutParams(Textparams1);
			wtext.setGravity(Gravity.CENTER);

			LinearLayout.LayoutParams layp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			// layp.width = width;
			// layp.height = height;
			ly.setLayoutParams(layp);

			if (widthinDP >= 600) {

				marqueeText.setTextSize(60);
				wtext.setTextSize(60);

			} else if (widthinDP > 501 && widthinDP < 600) {

				marqueeText.setTextSize(55);
				wtext.setTextSize(55);

			} else if (widthinDP > 331 && widthinDP < 500) {

				marqueeText.setTextSize(50);
				wtext.setTextSize(50);

			} else if (widthinDP > 260 && widthinDP < 330) {

				marqueeText.setTextSize(45);
				wtext.setTextSize(45);

			} else if (widthinDP <= 260) {

				marqueeText.setTextSize(40);
				wtext.setTextSize(40);

			}

		} else {

		}

	}

	public int getScreenOrientation() {
		Display getOrient = getWindowManager().getDefaultDisplay();
		int orientation = Configuration.ORIENTATION_UNDEFINED;
		if (getOrient.getWidth() == getOrient.getHeight()) {
			orientation = Configuration.ORIENTATION_SQUARE;
		} else {
			if (getOrient.getWidth() < getOrient.getHeight()) {
				orientation = Configuration.ORIENTATION_PORTRAIT;
			} else {
				orientation = Configuration.ORIENTATION_LANDSCAPE;
			}
		}
		return orientation;
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// Thread.abort = true;
		Thread.interrupted();
		super.onDestroy();
	}

	protected void onStop() {
		super.onStop();
		handler.removeCallbacks(run);
	}
}
