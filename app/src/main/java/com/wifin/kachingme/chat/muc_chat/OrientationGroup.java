package com.wifin.kachingme.chat.muc_chat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.util.Constant;

public class OrientationGroup extends Activity implements AnimationListener {
	ImageView web;
	Handler mHandler = new Handler();
	public static String mZzleTextor = "";
	public static String Value = "";
	Animation animFadein;
	TextView marqueeText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orientation);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		Value = mZzleTextor;

		Constant.printMsg("colorr::class" + Value);
		web = (ImageView) findViewById(R.id.web);
		marqueeText = (TextView) findViewById(R.id.scrolltext);
        Constant.typeFace(this,marqueeText);

		Bundle extras = getIntent().getExtras();

		Constant.printMsg("extra::" + extras);
		if (extras != null) {

			byte[] byteArray = extras.getByteArray("pic");

			Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0,
					byteArray.length);

			web.setImageBitmap(bmp);
		}

		marqueeText.setText(Value);

		animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.move);

		// set animation listener
		animFadein.setAnimationListener(this);
		web.startAnimation(animFadein);
		marqueeText.startAnimation(animFadein);

		animFadein.setFillAfter(true);

		screenArrangement();

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
			Textparams.width = width * 95 / 100;
			Textparams.height = height;
			Textparams.gravity = Gravity.CENTER;
			web.setLayoutParams(Textparams);
			//
			// LinearLayout.LayoutParams Textparams1 = new
			// LinearLayout.LayoutParams(
			// LinearLayout.LayoutParams.WRAP_CONTENT,
			// LinearLayout.LayoutParams.WRAP_CONTENT);
			// Textparams1.width = width * 40 / 100;
			// Textparams1.height = height * 70 / 100;
			// Textparams1.topMargin = (int) (height * 0.8 / 100);
			// Textparams1.gravity = Gravity.CENTER;
			// marqueeText.setLayoutParams(Textparams1);
			// marqueeText.setGravity(Gravity.CENTER);
			//
			// if (widthinDP >= 600) {
			//
			// marqueeText.setTextSize(60);
			//
			// } else if (widthinDP > 501 && widthinDP < 600) {
			//
			// marqueeText.setTextSize(55);
			//
			// } else if (widthinDP > 331 && widthinDP < 500) {
			//
			// marqueeText.setTextSize(50);
			//
			// } else if (widthinDP > 260 && widthinDP < 330) {
			//
			// marqueeText.setTextSize(45);
			//
			// } else if (widthinDP <= 260) {
			//
			// marqueeText.setTextSize(40);
			//
			// }

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

	public void onBackPressed() {

		Intent intent = new Intent(OrientationGroup.this, MUCTest.class);
		startActivity(intent);
		finish();

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
}
