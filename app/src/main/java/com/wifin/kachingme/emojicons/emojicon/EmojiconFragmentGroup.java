package com.wifin.kachingme.emojicons.emojicon;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.wifin.kachingme.emojicons.emojicon.emoji.Nature;
import com.wifin.kachingme.emojicons.emojicon.emoji.Objects;
import com.wifin.kachingme.emojicons.emojicon.emoji.People;
import com.wifin.kachingme.emojicons.emojicon.emoji.Places;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.chat.chat_common_classes.LogoGroup;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.wifin.kachingme.emojicons.emojicon.emoji.Emojicon;
import com.wifin.kachingme.emojicons.emojicon.emoji.Symbols;
import com.wifin.kaching.me.ui.R;

//import EmojiconsFragment.getLogo;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com).
 */
public class EmojiconFragmentGroup extends Fragment implements
		ViewPager.OnPageChangeListener {
	private OnEmojiconBackspaceClickedListener mOnEmojiconBackspaceClickedListener;
	private int mEmojiTabLastSelectedIndex = -1;
	private View[] mEmojiTabs;
	ImageButton mCustomEmojicons, mBuyCustomEmojicons;
	LinearLayout mCustomEmojiconsLayout, mDynamicLogozzleLayout,
			mDynamicInnerLogozzleLayout;
	LinearLayout mBuyCustomEmojiconsLayout, mBuyDynamicLogozzleLayout,
			mBuyDynamicInnerLogozzleLayout;
	View mInflateView;
	ImageView mBannerImage = null;
	Context context;
	ArrayList<String> mImgList = new ArrayList<String>();
	private DisplayImageOptions options;
	int mTotalPage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.emojicons, container, false);
		mInflateView = view;

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).build();

		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(getActivity()));

		final ViewPager emojisPager = (ViewPager) view
				.findViewById(R.id.emojis_pager);
		emojisPager.setOnPageChangeListener(this);
		EmojisPagerAdapter emojisAdapter = new EmojisPagerAdapter(
				getFragmentManager(), Arrays.asList(
						EmojiconGridFragment.newInstance(People.DATA),
						EmojiconGridFragment.newInstance(Nature.DATA),
						EmojiconGridFragment.newInstance(Objects.DATA),
						EmojiconGridFragment.newInstance(Places.DATA),
						EmojiconGridFragment.newInstance(Symbols.DATA)));

		emojisPager.setAdapter(emojisAdapter);

		intVariable();
		if (Connectivity.isConnected(getActivity())) {

			new getLogo().execute();

		}

		mEmojiTabs = new View[5];
		mEmojiTabs[0] = view.findViewById(R.id.emojis_tab_0_people);
		mEmojiTabs[1] = view.findViewById(R.id.emojis_tab_1_nature);
		mEmojiTabs[2] = view.findViewById(R.id.emojis_tab_2_objects);
		mEmojiTabs[3] = view.findViewById(R.id.emojis_tab_3_cars);
		mEmojiTabs[4] = view.findViewById(R.id.emojis_tab_4_punctuation);
		for (int i = 0; i < mEmojiTabs.length; i++) {
			final int position = i;
			mEmojiTabs[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					mCustomEmojicons.setBackgroundColor(Color
							.parseColor("#d3d3d3"));
					mCustomEmojiconsLayout.setVisibility(View.GONE);
					mBuyCustomEmojiconsLayout.setVisibility(View.GONE);
					v.setPressed(true);
					v.setActivated(true);
					v.setSelected(true);

					emojisPager.setCurrentItem(position);
				}
			});
		}
		view.findViewById(R.id.emojis_backspace).setOnTouchListener(
				new RepeatListener(1000, 50, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mOnEmojiconBackspaceClickedListener != null) {
							mOnEmojiconBackspaceClickedListener
									.onEmojiconBackspaceClicked(v);
						}
					}
				}));

		mCustomEmojicons.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				for (int i = 0; i < mEmojiTabs.length; i++) {
					final int position = i;
					mEmojiTabs[i].setActivated(false);
					mEmojiTabs[i].setPressed(false);
					mEmojiTabs[i].setSelected(false);
					mEmojiTabs[i]
							.setBackgroundResource(R.drawable.orca_emoji_tab_background);
				}

				mBuyCustomEmojicons
						.setBackgroundResource(R.drawable.orca_emoji_tab_background);
				mCustomEmojicons.setBackgroundColor(Color.parseColor("#00a0ff"));
				onPageSelected(7);

				mCustomEmojiconsLayout.setVisibility(View.INVISIBLE);
				mBuyCustomEmojiconsLayout.setVisibility(View.VISIBLE);

			}
		});

		mBuyCustomEmojicons.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				for (int i = 0; i < mEmojiTabs.length; i++) {
					final int position = i;
					mEmojiTabs[i].setActivated(false);
					mEmojiTabs[i].setPressed(false);
					mEmojiTabs[i].setSelected(false);
					mEmojiTabs[i]
							.setBackgroundResource(R.drawable.orca_emoji_tab_background);
				}

				mCustomEmojicons
						.setBackgroundResource(R.drawable.orca_emoji_tab_background);
				mBuyCustomEmojicons.setBackgroundColor(Color
						.parseColor("#00a0ff"));
				onPageSelected(7);

				mCustomEmojiconsLayout.setVisibility(View.VISIBLE);
				mBuyCustomEmojiconsLayout.setVisibility(View.INVISIBLE);

			}
		});

		onPageSelected(0);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (getActivity() instanceof OnEmojiconBackspaceClickedListener) {
			mOnEmojiconBackspaceClickedListener = (OnEmojiconBackspaceClickedListener) getActivity();
		} else if (getParentFragment() instanceof OnEmojiconBackspaceClickedListener) {
			mOnEmojiconBackspaceClickedListener = (OnEmojiconBackspaceClickedListener) getParentFragment();
		} else {
			throw new IllegalArgumentException(activity
					+ " must implement interface "
					+ OnEmojiconBackspaceClickedListener.class.getSimpleName());
		}
	}

	@Override
	public void onDetach() {
		mOnEmojiconBackspaceClickedListener = null;
		super.onDetach();
	}

	public static void input(EditText editText, Emojicon emojicon) {
		if (editText == null || emojicon == null) {
			return;
		}

		int start = editText.getSelectionStart();
		int end = editText.getSelectionEnd();
		if (start < 0) {
			editText.append(emojicon.getEmoji());
		} else {
			editText.getText().replace(Math.min(start, end),
					Math.max(start, end), emojicon.getEmoji(), 0,
					emojicon.getEmoji().length());
		}
	}

	public static void backspace(EditText editText) {
		KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0,
				0, KeyEvent.KEYCODE_ENDCALL);
		editText.dispatchKeyEvent(event);
	}

	@Override
	public void onPageScrolled(int i, float v, int i2) {
	}

	@Override
	public void onPageSelected(int i) {
		if (mEmojiTabLastSelectedIndex == i) {
			return;
		}
		switch (i) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
			if (mEmojiTabLastSelectedIndex >= 0
					&& mEmojiTabLastSelectedIndex < mEmojiTabs.length) {
				mEmojiTabs[mEmojiTabLastSelectedIndex].setSelected(false);
			}
			mEmojiTabs[i].setSelected(true);
			mEmojiTabLastSelectedIndex = i;
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int i) {
	}

	private static class EmojisPagerAdapter extends FragmentStatePagerAdapter {
		private List<EmojiconGridFragment> fragments;

		public EmojisPagerAdapter(FragmentManager fm,
				List<EmojiconGridFragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int i) {
			return fragments.get(i);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
	}

	/**
	 * A class, that can be used as a TouchListener on any view (e.g. a Button).
	 * It cyclically runs a clickListener, emulating keyboard-like behaviour.
	 * First click is fired immediately, next before initialInterval, and
	 * subsequent before normalInterval.
	 * <p/>
	 * <p>
	 * Interval is scheduled before the onClick completes, so it has to run
	 * fast. If it runs slow, it does not generate skipped onClicks.
	 */
	public static class RepeatListener implements View.OnTouchListener {

		private Handler handler = new Handler();

		private int initialInterval;
		private final int normalInterval;
		private final View.OnClickListener clickListener;

		private Runnable handlerRunnable = new Runnable() {
			@Override
			public void run() {
				if (downView == null) {
					return;
				}
				handler.removeCallbacksAndMessages(downView);
				handler.postAtTime(this, downView, SystemClock.uptimeMillis()
						+ normalInterval);
				clickListener.onClick(downView);
			}
		};

		private View downView;

		/**
		 * @param initialInterval
		 *            The interval before first click event
		 * @param normalInterval
		 *            The interval before second and subsequent click events
		 * @param clickListener
		 *            The OnClickListener, that will be called periodically
		 */
		public RepeatListener(int initialInterval, int normalInterval,
				View.OnClickListener clickListener) {
			if (clickListener == null)
				throw new IllegalArgumentException("null runnable");
			if (initialInterval < 0 || normalInterval < 0)
				throw new IllegalArgumentException("negative interval");

			this.initialInterval = initialInterval;
			this.normalInterval = normalInterval;
			this.clickListener = clickListener;
		}

		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downView = view;
				handler.removeCallbacks(handlerRunnable);
				handler.postAtTime(handlerRunnable, downView,
						SystemClock.uptimeMillis() + initialInterval);
				clickListener.onClick(view);
				return true;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_OUTSIDE:
				handler.removeCallbacksAndMessages(downView);
				downView = null;
				return true;
			}
			return false;
		}
	}

	public interface OnEmojiconBackspaceClickedListener {
		void onEmojiconBackspaceClicked(View v);
	}

	public void intVariable() {

		mCustomEmojicons = (ImageButton) mInflateView
				.findViewById(R.id.custom_emojicons);
		mBuyCustomEmojicons = (ImageButton) mInflateView
				.findViewById(R.id.buy_custom_emojicons);
		mCustomEmojiconsLayout = (LinearLayout) mInflateView
				.findViewById(R.id.custom_emojicon_layout);
		mDynamicLogozzleLayout = (LinearLayout) mInflateView
				.findViewById(R.id.dynamic_logozzle_layout);
		mBuyCustomEmojiconsLayout = (LinearLayout) mInflateView
				.findViewById(R.id.buy_custom_emojicon_layout);
		mBuyDynamicLogozzleLayout = (LinearLayout) mInflateView
				.findViewById(R.id.buy_dynamic_logozzle_layout);

	}

	public void screenArrangement() {

		// Logic for adding Logo ZZle Start

		mDynamicInnerLogozzleLayout = new LinearLayout(getActivity());

		// Toast.makeText(getActivity(), "Click" + KachingUtill.width,
		// Toast.LENGTH_LONG).show();

		for (int i = 0; i < mImgList.size(); i++) {

			mBannerImage = new ImageView(getActivity());
			mBannerImage.setId(i);
			mBannerImage.setScaleType(ScaleType.FIT_XY);

			mBannerImage.setDrawingCacheEnabled(true);
			ImageLoader.getInstance().displayImage(
					String.valueOf(mImgList.get(i).toString()).replaceAll(" ",
							"%20"), mBannerImage, options);
			// if (i == 0) {
			// mBannerImage.setImageResource(R.drawable.csk);
			// } else if (i == 1) {
			// mBannerImage.setImageResource(R.drawable.rj);
			// } else if (i == 2) {
			// mBannerImage.setImageResource(R.drawable.kkr);
			// } else if (i == 3) {
			// mBannerImage.setImageResource(R.drawable.kp);
			// } else {
			// mBannerImage.setImageResource(R.drawable.kkr);
			// }

			LinearLayout.LayoutParams listparams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			listparams.width = Constant.width * 20 / 100;
			listparams.height = Constant.height * 10 / 100;
			listparams.topMargin = Constant.width * 2 / 100;
			listparams.bottomMargin = Constant.width * 2 / 100;
			listparams.leftMargin = Constant.width * 4 / 100;
			listparams.gravity = Gravity.CENTER;
			mBannerImage.setLayoutParams(listparams);

			if (i % 4 == 0 && i != 0) {

				mDynamicLogozzleLayout.addView(mDynamicInnerLogozzleLayout);
				mDynamicInnerLogozzleLayout = new LinearLayout(getActivity());

			}
			mDynamicInnerLogozzleLayout.addView(mBannerImage);

			mBannerImage.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Toast.makeText(getActivity(), "Click" + v.getId(),
					// Toast.LENGTH_LONG).show();

					ImageView view = (ImageView) v;

					view.buildDrawingCache();

					Drawable myDrawable = view.getDrawable();

					Bitmap bitmap = ((BitmapDrawable) myDrawable).getBitmap();

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
					byte[] byteArray = stream.toByteArray();

					Intent i = new Intent(getActivity(), LogoGroup.class);
					i.putExtra("Bitmap", byteArray);
					startActivity(i);
					getActivity().finish();

					// Constant.logogroup = true;
					// Constant.logobitgroup = byteArray;
					// Intent in = new Intent(getActivity(), MUC_Chat.class);
					// startActivity(in);
					// getActivity().finish();

				}
			});

		}
		mDynamicLogozzleLayout.addView(mDynamicInnerLogozzleLayout);

		// Logic for adding Logo ZZle Stop

		// Logic for adding Buy Logo ZZle Start

		ImageView mBuyBannerImage = null;
		mBuyDynamicInnerLogozzleLayout = new LinearLayout(getActivity());

		// Toast.makeText(getActivity(), "Click" + KachingUtill.width,
		// Toast.LENGTH_LONG).show();

		for (int i = 0; i < 7; i++) {

			mBuyBannerImage = new ImageView(getActivity());
			mBuyBannerImage.setId(i);
			mBuyBannerImage.setScaleType(ScaleType.FIT_XY);

			if (i == 0) {
				mBuyBannerImage.setImageResource(R.drawable.csk);
			} else if (i == 1) {
				mBuyBannerImage.setImageResource(R.drawable.rj);
			} else if (i == 2) {
				mBuyBannerImage.setImageResource(R.drawable.kkr);
			} else if (i == 3) {
				mBuyBannerImage.setImageResource(R.drawable.kp);
			} else {
				mBuyBannerImage.setImageResource(R.drawable.kkr);
			}

			LinearLayout.LayoutParams listparams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			listparams.width = Constant.width * 20 / 100;
			listparams.height = Constant.height * 10 / 100;
			listparams.topMargin = Constant.width * 2 / 100;
			listparams.bottomMargin = Constant.width * 2 / 100;
			listparams.leftMargin = Constant.width * 4 / 100;
			listparams.gravity = Gravity.CENTER;
			mBuyBannerImage.setLayoutParams(listparams);

			if (i % 4 == 0 && i != 0) {

				mBuyDynamicLogozzleLayout
						.addView(mBuyDynamicInnerLogozzleLayout);
				mBuyDynamicInnerLogozzleLayout = new LinearLayout(getActivity());

			}
			mBuyDynamicInnerLogozzleLayout.addView(mBuyBannerImage);

			mBuyBannerImage.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Toast.makeText(getActivity(), "Click" + v.getId(),
					// Toast.LENGTH_LONG).show();
				}
			});

		}
		mBuyDynamicLogozzleLayout.addView(mBuyDynamicInnerLogozzleLayout);

		// Logic for adding Logo ZZle Stop

	}

	private class getLogo extends AsyncTask<String, String, String> {

		ProgressDialog progressdialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = null;
			HttpConfig ht = new HttpConfig();

			result = ht
					.httpget("http://49.207.180.36:8080/kachingme/rest/getrandomlogozzle.json?pageNumber=" + 1);
			Constant.printMsg("PRODUCT URL>>>>>>" + result);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Constant.printMsg("RESULT::>>>>>>>>>>>>>>" + result);
			// progressdialog.dismiss();
			mImgList.clear();

			if (result != null && result.length() > 0) {

				JSONArray jarray;
				try {
					jarray = new JSONArray(result);
					for (int i = 0; i < jarray.length(); i++) {
						JSONObject jObject = jarray.getJSONObject(i);

						mImgList.add(jObject.getString("photoPath"));
						mTotalPage = Integer.valueOf(jObject
								.getString("totalPage"));

					}
					Constant.printMsg("total page::>>>>>>" + mTotalPage);
					Constant.printMsg("mImgList::>>>>>>" + mImgList.size());

					// screenArrangement();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}
}
