package com.wifin.kachingme.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.util.Constant;

public class FAQ_Activity extends HeaderActivity {

	LinearLayout first_qus_layout, sec_qus_layout, third_qus_layout,
			fourth_qus_layout, fifth_qus_layout, sixth_qus_layout,
			seventh_qus_layout, eight_qus_layout;
	TextView first_qus_text, sec_qus_text, third_qus_text, fourth_qus_text,
			fifth_qus_text, sixth_qus_text, seventh_qus_text, eight_qus_text;
	int height = 0, width = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
		View.inflate(this, R.layout.faq_activity, vg);
        mHeading.setText("FAQ");
        mHeaderImg.setVisibility(View.GONE);
        mNextBtn.setVisibility(View.GONE);

		
		// setContentView(R.layout.faq_activity);
//		head.setText("FAQ");
//		head.setVisibility(View.VISIBLE);
//
//		footer.setVisibility(View.GONE);
//		logo.setVisibility(ImageView.GONE);
//		back.setVisibility(ImageView.VISIBLE);
//		cart.setVisibility(ImageView.INVISIBLE);
//		cartno.setVisibility(ImageView.INVISIBLE);
//		sideMenufoot.setVisibility(LinearLayout.VISIBLE);
//		head.setTextColor(Color.parseColor("#FFFFFF"));
//		Ka_newlogo.setVisibility(ImageView.GONE);
//		back.setBackgroundResource(R.drawable.arrow);
//		headlay.setBackgroundColor(Color.parseColor("#FE0000"));
//		cart.setBackgroundResource(0);
//		cart.setBackgroundDrawable(getResources().getDrawable(
//				R.drawable.add_btn));
		init();
		screenArrange();
		mBackBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(FAQ_Activity.this, AboutActivity.class);
				startActivity(i);
				finish();

			}
		});
		first_qus_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(FAQ_Activity.this,
						FAQ_Answer_Activity.class);
				i.putExtra("qus", "How do I verify my number in KaChing.me?");
				startActivity(i);
			}
		});
		sec_qus_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(FAQ_Activity.this,
						FAQ_Answer_Activity.class);
				i.putExtra("qus", "How do I restore my messages?	");

				startActivity(i);
			}
		});
		third_qus_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(FAQ_Activity.this,
						FAQ_Answer_Activity.class);
				i.putExtra("qus", "How do I use emoticons and Licensed Logos?");

				startActivity(i);
			}
		});
		fourth_qus_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(FAQ_Activity.this,
						FAQ_Answer_Activity.class);
				i.putExtra("qus", "How do I add contacts to KaChing.me?");

				startActivity(i);
			}
		});
		fifth_qus_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(FAQ_Activity.this,
						FAQ_Answer_Activity.class);
				i.putExtra("qus",
						"How do I receive KaChing.me push notifications?");

				startActivity(i);
			}
		});
		sixth_qus_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(FAQ_Activity.this,
						FAQ_Answer_Activity.class);
				i.putExtra("qus", "Which Apple devices are supported?");

				startActivity(i);
			}
		});
		eight_qus_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(FAQ_Activity.this,
						FAQ_Answer_Activity.class);
				i.putExtra("qus", "How do I send media, contacts or location?");

				startActivity(i);
			}
		});
		seventh_qus_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(FAQ_Activity.this,
						FAQ_Answer_Activity.class);
				i.putExtra("qus", "How do I block or unblock a contact?");

				startActivity(i);
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
		layoutMenuq.gravity = Gravity.CENTER;
		first_qus_layout.setLayoutParams(layoutMenuq);
		sec_qus_layout.setLayoutParams(layoutMenuq);
		third_qus_layout.setLayoutParams(layoutMenuq);
		fourth_qus_layout.setLayoutParams(layoutMenuq);
		fifth_qus_layout.setLayoutParams(layoutMenuq);
		sixth_qus_layout.setLayoutParams(layoutMenuq);
		seventh_qus_layout.setLayoutParams(layoutMenuq);
		eight_qus_layout.setLayoutParams(layoutMenuq);

//		LinearLayout.LayoutParams abbtx1 = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		abbtx1.width = width * 60 / 100;
//		abbtx1.gravity = Gravity.CENTER;
//		abbtx1.leftMargin = width * 5 / 100;
//		head.setLayoutParams(abbtx1);
//		head.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams layoutMenuq1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		// layoutMenuq1.height = width * 10 / 100;
		layoutMenuq1.width = width;
		layoutMenuq1.leftMargin = width * 5 / 100;
		layoutMenuq1.topMargin = width * 5 / 100;
		first_qus_text.setLayoutParams(layoutMenuq1);
		sec_qus_text.setLayoutParams(layoutMenuq1);
		third_qus_text.setLayoutParams(layoutMenuq1);
		fourth_qus_text.setLayoutParams(layoutMenuq1);
		fifth_qus_text.setLayoutParams(layoutMenuq1);
		sixth_qus_text.setLayoutParams(layoutMenuq1);
		seventh_qus_text.setLayoutParams(layoutMenuq1);
		eight_qus_text.setLayoutParams(layoutMenuq1);

	}

	private void init() {
		// TODO Auto-generated method stub
		first_qus_layout = (LinearLayout) findViewById(R.id.question1_layout);
		sec_qus_layout = (LinearLayout) findViewById(R.id.question2_layout);
		third_qus_layout = (LinearLayout) findViewById(R.id.question3_layout);
		fourth_qus_layout = (LinearLayout) findViewById(R.id.question4_layout);
		fifth_qus_layout = (LinearLayout) findViewById(R.id.question5_layout);
		sixth_qus_layout = (LinearLayout) findViewById(R.id.question6_layout);
		seventh_qus_layout = (LinearLayout) findViewById(R.id.question7_layout);
		eight_qus_layout = (LinearLayout) findViewById(R.id.question8_layout);

		first_qus_text = (TextView) findViewById(R.id.question2_text);
		sec_qus_text = (TextView) findViewById(R.id.question3_text);
		third_qus_text = (TextView) findViewById(R.id.question4_text);
		fourth_qus_text = (TextView) findViewById(R.id.question5_text);
		fifth_qus_text = (TextView) findViewById(R.id.question6_text);
		sixth_qus_text = (TextView) findViewById(R.id.question1_text);
		seventh_qus_text = (TextView) findViewById(R.id.question7_text);
		eight_qus_text = (TextView) findViewById(R.id.question8_text);

        Constant.typeFace(this, first_qus_text);
        Constant.typeFace(this,sec_qus_text);
        Constant.typeFace(this,third_qus_text);
        Constant.typeFace(this,fourth_qus_text);
        Constant.typeFace(this,fifth_qus_text);
        Constant.typeFace(this,sixth_qus_text);
        Constant.typeFace(this,seventh_qus_text);
        Constant.typeFace(this,eight_qus_text);
    }
}
