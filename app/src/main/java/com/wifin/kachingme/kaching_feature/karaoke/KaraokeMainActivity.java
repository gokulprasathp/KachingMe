package com.wifin.kachingme.kaching_feature.karaoke;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.wifin.kachingme.chat_home.MainActivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kaching.me.ui.R;

public class KaraokeMainActivity extends MainActivity implements
		OnItemClickListener {

	ListView mkaraokeList;
	List<String> mSongList = new ArrayList<String>();
	List<String> mImgList = new ArrayList<String>();
    public static KaraokeMainActivity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        mActivity=this;
		// setContentView(R.layout.karoake_main_activity);
		ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
		View.inflate(this, R.layout.karoake_main_activity, vg);
		logo.setVisibility(ImageView.GONE);
		back.setVisibility(ImageView.VISIBLE);
		cart.setVisibility(ImageView.INVISIBLE);
		head.setText("Karaoke");
		initialize();
		screenArrange();
		mSongList.add("FIFA 2010");
		// mImgList.add(R.drawable.fifaimg);
		mSongList.add("SmackThat");

		mkaraokeList.setAdapter(new KaraokeAdapter(this, mSongList));
		mkaraokeList.setOnItemClickListener(this);
	}

	private void screenArrange() {
		// TODO Auto-generated method stub
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int height = metrics.heightPixels;
		int width = metrics.widthPixels;
		Constant.screenWidth = width;
		Constant.screenHeight = height;
		LinearLayout.LayoutParams img_params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		img_params.width = width;
		img_params.height = height * 75 / 100;
		img_params.gravity = Gravity.CENTER;
		mkaraokeList.setLayoutParams(img_params);

	}

	private void initialize() {
		// TODO Auto-generated method stub
		mkaraokeList = (ListView) findViewById(R.id.karaoke_list);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		// TODO Auto-generated method stub
		if (mSongList.get(position).toString().equalsIgnoreCase("FIFA 2010")) {
			Intent i = new Intent(KaraokeMainActivity.this,
					KaraokeActivity.class);
			startActivity(i);
			// finish();
		}
		if (mSongList.get(position).toString().equalsIgnoreCase("SmackThat")) {
			Intent i = new Intent(KaraokeMainActivity.this,
					SmackThatActivity.class);
			startActivity(i);
			// finish();

		}
	}

}
