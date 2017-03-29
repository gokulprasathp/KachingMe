package com.wifin.kachingme.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public final class TestFragment extends Fragment {
	private static final String KEY_CONTENT = "TestFragment:Content";

	public static TestFragment newInstance(int content) {
		TestFragment fragment = new TestFragment();

		/*
		 * StringBuilder builder = new StringBuilder(); for (int i = 0; i < 20;
		 * i++) { builder.append(content).append(" "); }
		 * builder.deleteCharAt(builder.length() - 1);
		 */
		fragment.mContent = content;

		return fragment;
	}

	private int mContent = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getInt(KEY_CONTENT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/*
		 * TextView text = new TextView(getActivity());
		 * text.setGravity(Gravity.CENTER); text.setText(mContent);
		 * text.setTextSize(20 * getResources().getDisplayMetrics().density);
		 * text.setPadding(20, 20, 20, 20);
		 * 
		 * LinearLayout layout = new LinearLayout(getActivity());
		 * layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		 * LayoutParams.FILL_PARENT)); layout.setGravity(Gravity.CENTER);
		 * layout.addView(text);
		 */

		View v = inflater.inflate(mContent, container, false);
		/*
		 * Button btn_start_messaging = null;
		 * btn_start_messaging=(Button)v.findViewById
		 * (R.id.btn_start_messanging);
		 * btn_start_messaging.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Intent intent=new Intent(getActivity(),MainActivity.class);
		 * startActivity(intent); getActivity().finish(); } });
		 */
		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_CONTENT, mContent);
	}
}
