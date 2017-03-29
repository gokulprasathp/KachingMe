package com.wifin.kachingme.settings;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.adaptors.WallpaperAdapter;
import com.wifin.kachingme.util.Constant;
import com.wifin.kaching.me.ui.R;

public class Wallpaper_Activity extends Activity implements OnItemClickListener {

	GridView grid_view;
	ArrayList<Integer> list = new ArrayList<Integer>();
	WallpaperAdapter mAdapter;
	public static Context context;
	int height, width;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallpaper);
		context = Wallpaper_Activity.this;

		initialize();
		screenArrange();
		list.add(R.drawable.flower_falling);
		list.add(R.drawable.birds);
		list.add(R.drawable.nature);
		list.add(R.drawable.sunset);
		list.add(R.drawable.rotate_earth);
		list.add(R.drawable.smiley);
		list.add(R.drawable.love);
		list.add(R.drawable.elephant);
		list.add(R.drawable.cat);

		mAdapter = new WallpaperAdapter(Wallpaper_Activity.this, list);

		grid_view.setAdapter(mAdapter);
		grid_view.setOnItemClickListener(this);

	}

	private void screenArrange() {
		// TODO Auto-generated method stub
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		height = metrics.heightPixels;
		width = metrics.widthPixels;
		Constant.screenWidth = width;
		Constant.screenHeight = height;
	}

	private void initialize() {
		// TODO Auto-generated method stub
		grid_view = (GridView) findViewById(R.id.img_view);
	}

	public static Context getContext() {
		// TODO Auto-generated method stub
		return context;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		Constant.printMsg("prt msG :::::>>>>>>>>>> " + position);
		if (position == 0) {
			KachingMeApplication.getsharedpreferences_Editor()
					.putString("wallpaper", "blue").commit();
		}
		if (position == 1) {
			KachingMeApplication.getsharedpreferences_Editor()
					.putString("wallpaper", "birds").commit();
		}
		if (position == 2) {
			KachingMeApplication.getsharedpreferences_Editor()
					.putString("wallpaper", "nature").commit();
		}
		if (position == 3) {
			KachingMeApplication.getsharedpreferences_Editor()
					.putString("wallpaper", "sunset").commit();
		}
		if (position == 4) {
			KachingMeApplication.getsharedpreferences_Editor()
					.putString("wallpaper", "earth_rotation").commit();
		}
		if (position == 5) {
			KachingMeApplication.getsharedpreferences_Editor()
					.putString("wallpaper", "smiley").commit();
		}
		if (position == 6) {
			KachingMeApplication.getsharedpreferences_Editor()
					.putString("wallpaper", "love").commit();
		}
		if (position == 7) {
			KachingMeApplication.getsharedpreferences_Editor()
					.putString("wallpaper", "elephant").commit();
		}
		if (position == 8) {
			KachingMeApplication.getsharedpreferences_Editor()
					.putString("wallpaper", "cat").commit();
		}
		Toast.makeText(getApplicationContext(), "Wallpaper Applied",
				Toast.LENGTH_SHORT).show();

		Intent intent = new Intent(Wallpaper_Activity.this, NotificationSettings.class)
                .putExtra("TAG", "Chat")
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

}
