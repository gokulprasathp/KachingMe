/*
* @author Gokul
*
*
*
*
* */

package com.wifin.kachingme.deals;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wifin.kachingme.adaptors.WishListAdapter;
import com.wifin.kachingme.chat_home.MainActivity;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.WishListDto;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.util.Constant;
import com.wifin.kaching.me.ui.R;

public class WishListActivity extends MainActivity {

	ListView dic_list;
	TextView dichead;
	ImageView dicadd;
	LinearLayout headmain;

	public static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
		View.inflate(this, R.layout.activity_nyms_list, vg);
		logo.setVisibility(ImageView.GONE);
		back.setVisibility(ImageView.VISIBLE);
		cart.setVisibility(ImageView.VISIBLE);
		cartno.setVisibility(ImageView.INVISIBLE);
		cart.setBackgroundResource(0);
		cart.setBackgroundDrawable(getResources()
				.getDrawable(R.drawable.add182));

		head.setText("Wish List");
		Init();
		ScreenArrange();
		context = WishListActivity.this;

		FetchwishList();

		cart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(WishListActivity.this, WishActivity.class);
				startActivity(i);
				finish();

			}
		});

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(WishListActivity.this,
						SliderTesting.class);
				startActivity(i);
				finish();

			}
		});
	}

	private void Init() {
		// TODO Auto-generated method stub

		dic_list = (ListView) findViewById(R.id.dic_list);
		dichead = (TextView) findViewById(R.id.dic_text);
		dicadd = (ImageView) findViewById(R.id.dic_add);
		headmain = (LinearLayout) findViewById(R.id.dic_lay);
        Constant.typeFace(this,dichead);
	}

	private void ScreenArrange() {
		// TODO Auto-generated method stub
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		height = displayMetrics.heightPixels;
		width = displayMetrics.widthPixels;

		LinearLayout.LayoutParams messtx = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		messtx.height = height * 9 / 100;
		messtx.width = width * 66 / 100;
		dichead.setLayoutParams(messtx);
		dichead.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams abbtx = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		abbtx.height = height * 7 / 100;
		abbtx.width = width * 15 / 100;
		abbtx.leftMargin = width * 5 / 100;
		dicadd.setLayoutParams(abbtx);

		LinearLayout.LayoutParams layoutenter = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutenter.width = width;
		layoutenter.height = height * 9 / 100;
		layoutenter.setMargins(width * 2 / 100, height * 4 / 100,
				width * 2 / 100, height * 2 / 100);
		layoutenter.gravity = Gravity.CENTER;
		headmain.setLayoutParams(layoutenter);

		LinearLayout.LayoutParams list = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		list.width = width;
		list.height = height;
		list.setMargins(width * 2 / 100, height * 4 / 100, width * 2 / 100,
				height * 2 / 100);
		list.gravity = Gravity.CENTER;
		dic_list.setLayoutParams(list);

		if (width >= 600) {

			dichead.setTextSize(17);

		} else if (width > 501 && width < 600) {

			dichead.setTextSize(16);

		} else if (width > 260 && width < 500) {

			dichead.setTextSize(15);

		} else if (width <= 260) {

			dichead.setTextSize(14);

		}

	}

	public void FetchwishList() {

		Constant.addedwish.clear();

		String tx, dt, rectx, octx;
		Long pt;

		Dbhelper db = new Dbhelper(getApplicationContext());
		Cursor c = null;
		try {

			c = db.open()
					.getDatabaseObj()
					.query(Dbhelper.TABLE_WISH, null, null, null, null, null,
							null);
			int nm = c.getColumnIndex("name");
			int rec = c.getColumnIndex("recipientnane");
			int dttx = c.getColumnIndex("datetext");
			int oc = c.getColumnIndex("occatext");

			Constant.printMsg("The pending donate list in db ::::"
					+ c.getCount());

			if (c.getCount() > 0) {

				while (c.moveToNext()) {

					tx = c.getString(nm);
					dt = c.getString(dttx);
					rectx = c.getString(rec);
					octx = c.getString(oc);

					Constant.printMsg("donate:" + tx + "  " + dt + "  "
							+ rectx);

					WishListDto p = new WishListDto();
					p.setDate(dt);
					p.setWishlistname(tx);
					p.setRecipient(rectx);
					p.setOccation(octx);

					Constant.addedwish.add(p);
				}
			}
		} catch (SQLException e) {

			Constant.printMsg("Sql exception in pending shop details ::::"
					+ e.toString());
		} finally {
			c.close();
			db.close();
		}

		if (Constant.addedwish.size() > 0) {

			dic_list.setAdapter(new WishListAdapter(this, Constant.addedwish));

		} else {

			AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
			builder1.setMessage("Click Yes To Add Your Wish List");
			builder1.setCancelable(false);
			builder1.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Intent i = new Intent(WishListActivity.this,
									WishActivity.class);
							startActivity(i);
							finish();

							// dialog.cancel();
						}
					});
			builder1.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Intent i = new Intent(WishListActivity.this,
									SliderTesting.class);
							startActivity(i);
							finish();
							// dialog.cancel();
						}
					});

			AlertDialog alert11 = builder1.create();
			alert11.show();

		}

	}

	public static Context getContext() {
		// TODO Auto-generated method stub
		return context;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		Intent i = new Intent(WishListActivity.this, SliderTesting.class);
		startActivity(i);
		finish();
	}

}
