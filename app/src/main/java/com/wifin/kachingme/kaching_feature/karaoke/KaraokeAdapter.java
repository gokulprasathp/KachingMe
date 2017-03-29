package com.wifin.kachingme.kaching_feature.karaoke;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kachingme.util.Constant;
import com.wifin.kaching.me.ui.R;

public class KaraokeAdapter extends BaseAdapter {

	Activity myacActivity;
	private static LayoutInflater inflater = null;
	int width;
	int height;
	List<String> mList = new ArrayList<String>();

	public KaraokeAdapter(Activity context, List<String> mSongList) {
		// TODO Auto-generated constructor stub
		mList = mSongList;
		inflater = LayoutInflater.from(context);
		myacActivity = context;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return mList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		width = Constant.screenWidth;
		height = Constant.screenHeight;
		View vi = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.karaoke_adapter_list, null);
			holder = new ViewHolder();
			holder.mKaraokeText = (TextView) vi
					.findViewById(R.id.karaoke_song_text);
			holder.mKaraokeImg = (ImageView) vi
					.findViewById(R.id.karaoke_adapter_img);
            Constant.typeFace(myacActivity,holder.mKaraokeText);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}

		LinearLayout.LayoutParams img_params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		img_params.width = width * 15 / 100;
		img_params.height = height * 10 / 100;
		img_params.leftMargin = width * 2 / 100;
		img_params.topMargin = width * 2 / 100;
		img_params.bottomMargin = width * 2 / 100;

		holder.mKaraokeImg.setLayoutParams(img_params);

		LinearLayout.LayoutParams text_params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		// text_params.width = width * 20 / 100;
		// text_params.height = width * 20 / 100;
		text_params.leftMargin = width * 8 / 100;
		text_params.gravity = Gravity.CENTER;
		holder.mKaraokeText.setLayoutParams(text_params);

		if (width >= 600) {
			holder.mKaraokeText.setTextSize(20);

		} else if (width < 600 && width >= 480) {
			holder.mKaraokeText.setTextSize(16);

		} else if (width < 480 && width >= 320) {
			holder.mKaraokeText.setTextSize(14);

		} else if (width < 320) {
			holder.mKaraokeText.setTextSize(12);

		}

		holder.mKaraokeText.setText(mList.get(position).toString().trim());

		if (mList.get(position).toString().trim().equalsIgnoreCase("FIFA 2010")) {
			holder.mKaraokeImg.setImageResource(R.drawable.fifaimg);
		}
		if (mList.get(position).toString().trim().equalsIgnoreCase("SmackThat")) {
			holder.mKaraokeImg.setImageResource(R.drawable.smackthat);
		}
		return vi;
	}

	public class ViewHolder {

		ImageView mKaraokeImg;
		TextView mKaraokeText;

	}
}
