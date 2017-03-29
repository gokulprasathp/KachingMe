package com.wifin.kachingme.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat_home.MainActivity;
import com.wifin.kachingme.util.Constant;

public class FAQ_Answer_Activity extends MainActivity {

	TextView qus_text, description_text;
	int height = 0, width = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
		View.inflate(this, R.layout.faq_answer_activity, vg);
		// setContentView(R.layout.faq_answer_activity);
		head.setText("Kaching.Me");
		footer.setVisibility(View.GONE);
		logo.setVisibility(ImageView.INVISIBLE);
		back.setVisibility(ImageView.VISIBLE);
		cart.setVisibility(ImageView.INVISIBLE);
		cartno.setVisibility(ImageView.INVISIBLE);
		sideMenufoot.setVisibility(LinearLayout.VISIBLE);
		head.setTextColor(Color.parseColor("#FFFFFF"));
		Ka_newlogo.setVisibility(ImageView.INVISIBLE);
		back.setBackgroundResource(R.drawable.arrow);
		headlay.setBackgroundColor(Color.parseColor("#FE0000"));
		cart.setBackgroundResource(0);
		cart.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.add_btn));
		init();
		screenArrange();
		qus_text.setText(getIntent().getExtras().getString("qus"));
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(FAQ_Answer_Activity.this,
						FAQ_Activity.class);
				startActivity(i);
				finish();

			}
		});
	}

	private void screenArrange() {
		// TODO Auto-generated method stub
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		height = displayMetrics.heightPixels;
		width = displayMetrics.widthPixels;

		LinearLayout.LayoutParams layoutMenuq = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutMenuq.height = width * 15 / 100;
		layoutMenuq.width = width;
		layoutMenuq.leftMargin = width * 5 / 100;
		layoutMenuq.gravity = Gravity.CENTER;
		layoutMenuq.topMargin = width * 5 / 100;
		qus_text.setLayoutParams(layoutMenuq);

		LinearLayout.LayoutParams layoutMenuq1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		// layoutMenuq1.height = width * 15 / 100;
		layoutMenuq1.width = width;
		layoutMenuq1.gravity = Gravity.CENTER;
		layoutMenuq1.leftMargin = width * 5 / 100;
		description_text.setLayoutParams(layoutMenuq1);

	}

	private void init() {
		// TODO Auto-generated method stub
		qus_text = (TextView) findViewById(R.id.qus_text);
		description_text = (TextView) findViewById(R.id.description_text);
        Constant.typeFace(this, qus_text);
        Constant.typeFace(this,description_text);
	}
}
