/*
* @author Sivaprasath
*
* @usage -  This class is used to display the list of deals available.
*
*
* */


package com.wifin.kachingme.adaptors;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kachingme.deals.DealsActivity;
import com.wifin.kachingme.deals.StoreActivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kaching.me.ui.R;

public class DealMain_Adap extends BaseAdapter {

	Activity myacActivity;
	Context mContext = DealsActivity.getContext();
	ArrayList dList = Constant.chatdeel;
	private static LayoutInflater inflater = null;
	int width = Constant.screenWidth;
	int height = Constant.screenHeight;

	public DealMain_Adap(Activity act, ArrayList list) {
		// TODO Auto-generated constructor stub
		myacActivity = act;
		dList = list;

		inflater = (LayoutInflater) myacActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint({ "ResourceAsColor", "DefaultLocale" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		final ViewHolder holder;
		width = Constant.screenWidth;
		height = Constant.screenHeight;

		vi = inflater.inflate(R.layout.deal_adap, null);
		holder = new ViewHolder();
		holder.text = (TextView) vi.findViewById(R.id.deal_adap_name);
		holder.imgdl = (ImageView) vi.findViewById(R.id.deal_img);
        Constant.typeFace(mContext,holder.text);

		LinearLayout.LayoutParams buttonParamstx = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		buttonParamstx.width = width * 80 / 100;
		buttonParamstx.height = height * 11 / 100;
		buttonParamstx.gravity = Gravity.CENTER;
		holder.text.setLayoutParams(buttonParamstx);
		holder.text.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams buttonParamsif = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		buttonParamsif.width = width * 10 / 100;
		buttonParamsif.height = height * 6 / 100;
		buttonParamsif.gravity = Gravity.CENTER;
		holder.imgdl.setLayoutParams(buttonParamsif);

		if (width >= 600) {

			holder.text.setTextSize(27);

		} else if (width > 501 && width < 600) {

			holder.text.setTextSize(26);

		} else if (width > 260 && width < 500) {

			holder.text.setTextSize(25);

		} else if (width <= 260) {

			holder.text.setTextSize(24);

		}

		vi.setTag(holder);

		holder.text.setText(dList.get(position).toString());

		for (int i = 0; i < Constant.blinkPos.size(); i++) {

			String pos = Constant.blinkPos.get(i).toString();

			String poscount = String.valueOf(position);

			if (pos.equals(poscount)) {

				holder.imgdl.setVisibility(ImageView.VISIBLE);

				holder.text.setTextColor(Color.RED);

			}
		}
		vi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				TextView mText = (TextView) v.findViewById(R.id.deal_adap_name);

				String value = mText.getText().toString().toLowerCase().trim();

				Constant.menuId = value;

				Intent intent = new Intent(mContext, StoreActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				myacActivity.startActivity(intent);
				myacActivity.finish();

			}
		});

		return vi;
	}

	public static class ViewHolder {

		public TextView text;
		public ImageView imgdl;

	}
}