package com.wifin.kachingme.social_media;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wifin.kachingme.chat_home.MainActivity;
import com.wifin.kachingme.settings.Settings;
import com.wifin.kachingme.util.Constant;
import com.wifin.kaching.me.ui.R;

public class TellFriend extends MainActivity {

	Button mailbtn, msgbtn, canbtn, fbbtn, twtbtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
		View.inflate(this, R.layout.activity_tellfriend, vg);
		sideMenufoot.setVisibility(View.VISIBLE);
		logo.setVisibility(View.INVISIBLE);
		footer.setVisibility(View.GONE);
		head.setText("Tell a Friend");
		back.setVisibility(View.VISIBLE);
		head.setTextColor(Color.parseColor("#FFFFFF"));
		back.setBackgroundResource(R.drawable.arrow);
		headlay.setBackgroundColor(Color.parseColor("#FE0000"));
		Ka_newlogo.setVisibility(View.INVISIBLE);
		Init();
		Screen();

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(TellFriend.this, Settings.class);
				startActivity(intent);
				finish();
			}
		});

		canbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(TellFriend.this, Settings.class);
				startActivity(intent);
				finish();
			}
		});

		fbbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Constant.fb = true;

				Intent intent = new Intent(TellFriend.this,
						WebViewActivity.class);
				startActivity(intent);
				finish();
			}
		});

		twtbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Constant.fb = false;

				Intent intent = new Intent(TellFriend.this,
						WebViewActivity.class);
				startActivity(intent);
				finish();
			}
		});

		mailbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent mailer = new Intent(Intent.ACTION_SENDTO);
				mailer.setType("text/plain");
				// mailer.setType("message/rfc822");
				mailer.setData(Uri.parse("mailto:"));
				mailer.putExtra(
						"sms_body",
						"Check out Kaching.Me Messanger for your smartphone.\n Download it from Google play store.");

				mailer.putExtra(Intent.EXTRA_SUBJECT, "KaChing.me");
				mailer.putExtra(
						Intent.EXTRA_TEXT,
						"Hey,\n\n"
								+ "I just downloaded Kaching.Me Messanger on my Android.\n\n"
								+ "It is a smartphone messanger which replaces SMS. This app event lets me send pictures,video and other multimedia.\n\n"
								+ "Kaching.Me is available for Android and there is no PIN or username to remember - it work just like SMS and uses your internet data plan.\n\n"
								+ "Get it now from Google play and say good-bye to SMS!");

				startActivity(Intent.createChooser(mailer, "Send email..."));
			}
		});
		msgbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent mailer = new Intent(Intent.ACTION_VIEW);
				// mailer.setType("text/plain");
				mailer.setType("vnd.android-dir/mms-sms");
				mailer.putExtra(
						"sms_body",
						"Hey,\n\n"
								+ "I just downloaded Kaching.Me Messanger on my Android.\n\n"
								+ "It is a smartphone messanger which replaces SMS. This app event lets me send pictures,video and other multimedia.\n\n"
								+ "Kaching.Me is available for Android and there is no PIN or username to remember - it work just like SMS and uses your internet data plan.\n\n"
								+ "Get it now from Google play and say good-bye to SMS!");

				mailer.putExtra(Intent.EXTRA_SUBJECT, "KaChing.me");

				startActivity(Intent.createChooser(mailer, "Send Message..."));
			}
		});

	}

	private void Screen() {
		// TODO Auto-generated method stub

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		height = metrics.heightPixels;
		width = metrics.widthPixels;
		Constant.screenHeight = height;
		Constant.screenWidth = width;

		LinearLayout.LayoutParams head_lab = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		head_lab.width = 50 * width / 100;
		head_lab.height = 8 * height / 100;
		head_lab.gravity = Gravity.CENTER;
		head_lab.setMargins(width * 2 / 100, height * 2 / 100, width * 2 / 100,
				height * 2 / 100);
		mailbtn.setLayoutParams(head_lab);
		msgbtn.setLayoutParams(head_lab);
		fbbtn.setLayoutParams(head_lab);
		twtbtn.setLayoutParams(head_lab);
		canbtn.setLayoutParams(head_lab);

		if (width >= 600) {

			msgbtn.setTextSize(17);
			mailbtn.setTextSize(18);
			fbbtn.setTextSize(17);
			twtbtn.setTextSize(17);
			canbtn.setTextSize(18);

		} else if (width > 501 && width < 600) {

			mailbtn.setTextSize(16);
			msgbtn.setTextSize(17);
			fbbtn.setTextSize(17);
			twtbtn.setTextSize(17);
			canbtn.setTextSize(17);

		} else if (width > 260 && width < 500) {

			mailbtn.setTextSize(15);
			msgbtn.setTextSize(16);
			fbbtn.setTextSize(16);
			twtbtn.setTextSize(16);
			canbtn.setTextSize(16);

		} else if (width <= 260) {

			canbtn.setTextSize(14);
			twtbtn.setTextSize(15);
			fbbtn.setTextSize(15);
			msgbtn.setTextSize(15);
			mailbtn.setTextSize(15);

		}

	}

	private void Init() {
		// TODO Auto-generated method stub
		mailbtn = (Button) findViewById(R.id.mail);
		msgbtn = (Button) findViewById(R.id.msg);
		fbbtn = (Button) findViewById(R.id.fb);
		twtbtn = (Button) findViewById(R.id.twitter);
		canbtn = (Button) findViewById(R.id.cancel);
        Constant.typeFace(this, mailbtn);
        Constant.typeFace(this, msgbtn);
        Constant.typeFace(this, fbbtn);
        Constant.typeFace(this, twtbtn);
        Constant.typeFace(this, canbtn);

	}
}
