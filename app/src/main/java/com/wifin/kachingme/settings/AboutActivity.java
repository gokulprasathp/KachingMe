package com.wifin.kachingme.settings;

import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.util.Constant;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by user on 10/5/2016.
 */
public class AboutActivity extends HeaderActivity {
	LinearLayout mLogoLayout, mCopyRightTextLayout;
	ImageView mLogoImg;
	TextView mKchingMeText, mVersionText, mHelpText, mContactUsText,
			mCopyRightText, mRightsText;
	int height = 0;
	int width = 0;
	String version;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_about);
		ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
		View.inflate(this, R.layout.activity_about, vg);
		mHeading.setText("About");
		mHeaderImg.setVisibility(View.GONE);
		mNextBtn.setVisibility(View.GONE);
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = pInfo.versionName;
			// int verCode = pInfo.versionCode;

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initializeVariable();
		screenArrangement();
		mBackBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AboutActivity.this, Settings.class);
				startActivity(intent);
				finish();
			}
		});
		mHelpText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Constant.printMsg("about settings::help_text");
				Intent in = new Intent(AboutActivity.this, FAQ_Activity.class);
				startActivity(in);

			}
		});
		mContactUsText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Constant.mList.clear();
				Constant.printMsg("about settings::contact_text");
				Intent in = new Intent(AboutActivity.this,
						ContactUsActivity.class);
				startActivity(in);
			}
		});
		
		
		mVersionText.setText(version);
	}

	private void screenArrangement() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		height = metrics.heightPixels;
		width = metrics.widthPixels;

		LinearLayout.LayoutParams logoLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		logoLayoutParams.width = width * 40 / 100;
		logoLayoutParams.height = width * 55 / 100;
		logoLayoutParams.gravity = Gravity.CENTER;
		logoLayoutParams.topMargin = width * 5 / 100;
		logoLayoutParams.bottomMargin = width * 10 / 100;

		mLogoLayout.setLayoutParams(logoLayoutParams);

		LinearLayout.LayoutParams logoImgParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		logoImgParams.width = width * 30 / 100;
		logoImgParams.height = width * 30 / 100;
		logoImgParams.gravity = Gravity.CENTER;
		logoImgParams.topMargin = width * 3 / 100;
		mLogoImg.setLayoutParams(logoImgParams);

		LinearLayout.LayoutParams kachingTextParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		kachingTextParams.width = width * 40 / 100;
		kachingTextParams.height = width * 10 / 100;
		kachingTextParams.topMargin = width * 5 / 100;
		kachingTextParams.gravity = Gravity.CENTER;
		mKchingMeText.setLayoutParams(kachingTextParams);

		LinearLayout.LayoutParams versionTextParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		versionTextParams.width = width * 10 / 100;
		versionTextParams.height = width * 6 / 100;
		versionTextParams.leftMargin = width * 5 / 100;
		versionTextParams.gravity = Gravity.RIGHT;
		mVersionText.setGravity(Gravity.RIGHT);
		mVersionText.setLayoutParams(versionTextParams);

		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		textParams.width = width;
		textParams.height = width * 13 / 100;
		textParams.leftMargin = width * 5 / 100;
		textParams.topMargin = width * 3 / 100;
		textParams.gravity = Gravity.CENTER;
		mContactUsText.setLayoutParams(textParams);
		mHelpText.setLayoutParams(textParams);

		LinearLayout.LayoutParams copyrighttextlayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		copyrighttextlayoutParams.width = width;
		copyrighttextlayoutParams.height = width * 12 / 100;
		copyrighttextlayoutParams.topMargin = width * 10 / 100;
		copyrighttextlayoutParams.gravity = Gravity.CENTER;
		mCopyRightTextLayout.setLayoutParams(copyrighttextlayoutParams);

		LinearLayout.LayoutParams copyrighttextParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		copyrighttextParams.width = width;
		copyrighttextParams.height = width * 6 / 100;
		copyrighttextParams.gravity = Gravity.CENTER;
		mCopyRightText.setGravity(Gravity.CENTER);
		mRightsText.setGravity(Gravity.CENTER);
		mCopyRightText.setLayoutParams(copyrighttextParams);
		mRightsText.setLayoutParams(copyrighttextParams);
		if (width >= 600) {

			mHelpText.setTextSize(20);
			mContactUsText.setTextSize(20);
			mKchingMeText.setTextSize(20);
			mVersionText.setTextSize(16);
			mCopyRightText.setTextSize(16);
			mRightsText.setTextSize(16);

		} else if (width < 600 && width >= 480) {
			mHelpText.setTextSize(15);
			mContactUsText.setTextSize(15);
			mKchingMeText.setTextSize(15);
			mVersionText.setTextSize(15);
			mCopyRightText.setTextSize(15);
			mRightsText.setTextSize(15);
		} else if (width < 480 && width >= 320) {
			mHelpText.setTextSize(12);
			mContactUsText.setTextSize(12);
			mKchingMeText.setTextSize(12);
			mVersionText.setTextSize(12);
			mCopyRightText.setTextSize(12);
			mRightsText.setTextSize(12);
		} else if (width < 320) {
			mHelpText.setTextSize(10);
			mContactUsText.setTextSize(10);
			mKchingMeText.setTextSize(10);
			mVersionText.setTextSize(10);
			mCopyRightText.setTextSize(10);
			mRightsText.setTextSize(10);
		}

	}

	private void initializeVariable() {
		mLogoLayout = (LinearLayout) findViewById(R.id.logo_layout);
		mCopyRightTextLayout = (LinearLayout) findViewById(R.id.copy_right_text_layout);
		mLogoImg = (ImageView) findViewById(R.id.logo_img);
		mVersionText = (TextView) findViewById(R.id.version_text);
		mKchingMeText = (TextView) findViewById(R.id.kachingme_text);
		mHelpText = (TextView) findViewById(R.id.help_text);
		mContactUsText = (TextView) findViewById(R.id.contact_us_text);
		mCopyRightText = (TextView) findViewById(R.id.copy_right_text);
		mRightsText = (TextView) findViewById(R.id.rights_text);

        Constant.typeFace(this,mVersionText);
        Constant.typeFace(this,mKchingMeText);
        Constant.typeFace(this,mHelpText);
        Constant.typeFace(this,mContactUsText);
        Constant.typeFace(this,mCopyRightText);
        Constant.typeFace(this,mRightsText);
    }
}
