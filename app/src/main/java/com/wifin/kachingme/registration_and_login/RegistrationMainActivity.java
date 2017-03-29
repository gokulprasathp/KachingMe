package com.wifin.kachingme.registration_and_login;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.util.Constant;
import com.wifin.kaching.me.ui.R;

/**
 * @author siva
 * Created by Wifintech on 12-Sep-16.
 */
public class RegistrationMainActivity extends FragmentActivity implements View.OnClickListener {

	public static LinearLayout headlay, datalay, ctlay, bglay, cotlay, stlay,setcount_lay, sideMenufoot;
	public static ImageView logo, back, chatimg, bugsimg, contimg, settimg;
	public static LinearLayout cart;
	public static TextView head, right_head, next;
	public static int width, height;
	public static TextView cartno, cttext, bgtext, cottext, stext,setcount_txt;
	public static ImageView footer, Ka_newlogo;
	private KachingMeApplication globalApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		globalApplication = (KachingMeApplication) getApplication();
		setContentView(R.layout.registration_main_activity);
		Initialize();
		ScreenArrange();
		boolean tabletSize = false;
		tabletSize = getResources().getBoolean(R.bool.isTablet);
		Constant.printMsg("test vino tabletSize4:in slidertesting:"
                + tabletSize);
		String first = "Ka";
		String next = "<font color='#FE0000'>C</font>";
		String second = "hing.me";
		head.setText(Html.fromHtml(first + next + second));
		head.setVisibility(View.GONE);
	}

	private void Initialize() {
		// TODO Auto-generated method stub
		headlay = (LinearLayout) findViewById(R.id.Ka_headlayout);
		datalay = (LinearLayout) findViewById(R.id.Ka_datalayout);
		logo = (ImageView) findViewById(R.id.Ka_logo);
		cart = (LinearLayout) findViewById(R.id.Ka_cart);
		head = (TextView) findViewById(R.id.Ka_headtext);
		cartno = (TextView) findViewById(R.id.button_cart);
		back = (ImageView) findViewById(R.id.back_logo);
		footer = (ImageView) findViewById(R.id.foot);
		right_head = (TextView) findViewById(R.id.right_headtext);
		next = (TextView) findViewById(R.id.Ka_nexttext);

		ctlay = (LinearLayout) findViewById(R.id.m_chatlay);
		bglay = (LinearLayout) findViewById(R.id.m_bugslay);
		cotlay = (LinearLayout) findViewById(R.id.m_contlay);
		stlay = (LinearLayout) findViewById(R.id.m_settlay);
		chatimg = (ImageView) findViewById(R.id.m_chat);
		bugsimg = (ImageView) findViewById(R.id.m_bugs);
		contimg = (ImageView) findViewById(R.id.m_contact);
		setcount_lay = (LinearLayout) findViewById(R.id.m_setcount_lay);
		cttext = (TextView) findViewById(R.id.m_chattxt);
		bgtext = (TextView) findViewById(R.id.m_bugstxt);
		cottext = (TextView) findViewById(R.id.m_conttxt);
		stext = (TextView) findViewById(R.id.m_setttxt);
		setcount_txt = (TextView) findViewById(R.id.m_setcount_txt);
		sideMenufoot = (LinearLayout) findViewById(R.id.m_slider_footer);
		Ka_newlogo = (ImageView) findViewById(R.id.Ka_newlogo);
		cotlay.setOnClickListener(this);
		stlay.setOnClickListener(this);
		bglay.setOnClickListener(this);
		ctlay.setOnClickListener(this);

        Constant.typeFace(this,head);
        Constant.typeFace(this,right_head);
        Constant.typeFace(this,next);
        Constant.typeFace(this,cartno);
        Constant.typeFace(this,cttext);
        Constant.typeFace(this,bgtext);
        Constant.typeFace(this,cottext);
        Constant.typeFace(this,stext);
        Constant.typeFace(this,setcount_txt);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.m_contlay:
//				startActivity(new Intent(RegistrationMainActivity.this,DealsActivity.class));
				finish();
				break;
			case R.id.m_settlay:
//				startActivity(new Intent(RegistrationMainActivity.this,CartActivity.class));
				finish();
				break;
			case R.id.m_bugslay:
//				startActivity(new Intent(RegistrationMainActivity.this, NewBuxActivity.class));
				finish();
				break;
			case R.id.m_chatlay:
//				startActivity(new Intent(RegistrationMainActivity.this, SliderTesting.class));
				finish();
				break;
		}
	}
	@Override
	protected void onResume() {
		globalApplication.onActivityResumed(this);
		Constant.printMsg("IS Resume base::"
				+ globalApplication.isApplicationBroughtToBackground());

		super.onResume();
	}

//	private void screenArrge() {
//		// TODO Auto-generated method stub
//		DisplayMetrics displayMetrics = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//		height = displayMetrics.heightPixels;
//		width = displayMetrics.widthPixels;
//
//		LinearLayout.LayoutParams layoutMenuq = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		layoutMenuq.height = height * 7 / 100;
//		// layoutMenuq.gravity = Gravity.CENTER;
//		headlay.setLayoutParams(layoutMenuq);
//
//		LinearLayout.LayoutParams layoutdatat = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		layoutdatat.height = (int) (height * 78.5 / 100);
//		layoutdatat.gravity = Gravity.CENTER;
//		datalay.setLayoutParams(layoutdatat);
//
//		LinearLayout.LayoutParams ft = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		ft.height = height * 6 / 100;
//		ft.gravity = Gravity.CENTER;
//		footer.setLayoutParams(ft);
//
//		LinearLayout.LayoutParams logolay = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		logolay.width = width * 10 / 100;
//		logolay.height = width * 10 / 100;
//		logolay.setMargins(width * 1 / 100, height * 1 / 100, width * 1 / 100,
//				height * 1 / 100);
//		logo.setLayoutParams(logolay);
//
//		LinearLayout.LayoutParams backlay = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		backlay.width = width * 5 / 100;
//		backlay.height = width * 5 / 100;
//		back.setLayoutParams(backlay);
//
//		LinearLayout.LayoutParams cartlay = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		cartlay.width = width * 14 / 100;
//		cartlay.height = height * 6 / 100;
//		cart.setLayoutParams(cartlay);
//
//		LinearLayout.LayoutParams textlay = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		textlay.width = width * 60 / 100;
//		textlay.gravity = Gravity.CENTER;
//		textlay.leftMargin = width * 3 / 100;
//		head.setLayoutParams(textlay);
//		head.setGravity(Gravity.CENTER);
//
//		LinearLayout.LayoutParams textlay2 = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		textlay2.width = width * 9 / 100;
//		textlay2.gravity = Gravity.RIGHT;
//		textlay2.leftMargin = width * 10 / 100;
//		next.setLayoutParams(textlay2);
//
//		LinearLayout.LayoutParams textlay1 = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		// textlay.width = width * 74 / 100;
//		textlay1.gravity = Gravity.RIGHT;
//		right_head.setLayoutParams(textlay1);
//		right_head.setGravity(Gravity.CENTER);
//
//		LinearLayout.LayoutParams cartnu = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		cartnu.width = width * 6 / 100;
//		cartnu.height = height * 4 / 100;
//		cartnu.gravity = Gravity.CENTER;
//		cartno.setLayoutParams(cartnu);
//		cartno.setGravity(Gravity.CENTER);
//		LinearLayout.LayoutParams logolay1 = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		logolay1.width = (int) (width * 30.5 / 100);
//		logolay1.setMargins(width * 1 / 100, (int) (height * 1.5 / 100),
//				width * 1 / 100, height * 1 / 100);
//		logolay1.gravity = Gravity.CENTER;
//		ctlay.setLayoutParams(logolay1);
//		bglay.setLayoutParams(logolay1);
//		// cotlay.setLayoutParams(logolay);
//		stlay.setLayoutParams(logolay1);
//
//		LinearLayout.LayoutParams img = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		img.height = height * 4 / 100;
//		img.gravity = Gravity.CENTER;
//		chatimg.setLayoutParams(img);
//		bugsimg.setLayoutParams(img);
//		contimg.setLayoutParams(img);
//		// setcount_lay.s;etLayoutParams(img);
//
//		LinearLayout.LayoutParams img1 = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		img1.width = (int) (width * 10.5 / 100);
//		img1.height = height * 4 / 100;
//		img1.gravity = Gravity.CENTER;
//
//		setcount_lay.setLayoutParams(img1);
//
//		LinearLayout.LayoutParams img2 = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		img2.width = (int) (width * 5.5 / 100);
//		img2.height = height * 3 / 100;
//		img2.bottomMargin = height * 2 / 100;
//		img2.leftMargin = width * 1 / 100;
//		img2.gravity = Gravity.CENTER | Gravity.RIGHT;
//
//		setcount_txt.setLayoutParams(img2);
//
//		LinearLayout.LayoutParams imgtext = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		imgtext.height = height * 4 / 100;
//		imgtext.gravity = Gravity.CENTER;
//		cttext.setLayoutParams(imgtext);
//		bgtext.setLayoutParams(imgtext);
//		cottext.setLayoutParams(imgtext);
//		stext.setLayoutParams(imgtext);
//		// LinearLayout.LayoutParams layoutMenuqfoot = new
//		// LinearLayout.LayoutParams(
//		// LinearLayout.LayoutParams.MATCH_PARENT,
//		// LinearLayout.LayoutParams.WRAP_CONTENT);
//		// layoutMenuqfoot.height = height * 15 / 100;
//		// // layoutMenuqfoot.height = height * 15 / 100;
//		// layoutMenuqfoot.gravity = Gravity.CENTER;
//		// // layoutMenuqfoot.topMargin = height * 2 / 100;
//		// sideMenufoot.setLayoutParams(layoutMenuqfoot);
//
//		if (width >= 600) {
//
//			head.setTextSize(22);
//			next.setTextSize(14);
//
//		} else if (width > 501 && width < 600) {
//
//			head.setTextSize(21);
//			next.setTextSize(12);
//
//		} else if (width > 260 && width < 500) {
//
//			head.setTextSize(20);
//			next.setTextSize(10);
//
//		} else if (width <= 260) {
//
//			head.setTextSize(19);
//			next.setTextSize(9);
//
//		}
//
//	}

	private void ScreenArrange() {
		// TODO Auto-generated method stub
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		height = displayMetrics.heightPixels;
		width = displayMetrics.widthPixels;

		LinearLayout.LayoutParams layoutMenuq = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutMenuq.height = height * 7 / 100;
		// layoutMenuq.gravity = Gravity.CENTER;
		headlay.setLayoutParams(layoutMenuq);

		LinearLayout.LayoutParams layoutdatat = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		// layoutdatat.height = (int) (height * 83.5 / 100);
		layoutdatat.height = height * 80 / 100;
		// layoutdatat.bottomMargin = height * 2 / 100;
		layoutdatat.gravity = Gravity.CENTER;
		datalay.setLayoutParams(layoutdatat);

		LinearLayout.LayoutParams ft = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		ft.height = height * 6 / 100;
		ft.gravity = Gravity.CENTER | Gravity.BOTTOM;
		footer.setLayoutParams(ft);

		LinearLayout.LayoutParams logolay = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		logolay.width = width * 10 / 100;
		logolay.height = width * 10 / 100;
		logolay.setMargins(width * 1 / 100, height * 1 / 100, width * 1 / 100,
				height * 1 / 100);
		logo.setLayoutParams(logolay);

		LinearLayout.LayoutParams backlay = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		backlay.width = width * 5 / 100;
		backlay.height = width * 5 / 100;
		back.setLayoutParams(backlay);

		LinearLayout.LayoutParams cartlay = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		cartlay.width = width * 14 / 100;
		cartlay.height = height * 6 / 100;
		cart.setLayoutParams(cartlay);

		LinearLayout.LayoutParams imglay1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		imglay1.width = width * 35 / 100;
		imglay1.gravity = Gravity.RIGHT | Gravity.CENTER;
		imglay1.leftMargin = width * 20 / 100;
		imglay1.height = width * 9 / 100;
		imglay1.rightMargin = width * 15 / 100;
		Ka_newlogo.setLayoutParams(imglay1);

		LinearLayout.LayoutParams textlay = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		textlay.width = width * 38 / 100;
		// textlay.gravity = Gravity.CENTER | Gravity.LEFT;
		// textlay.leftMargin = width * 3 / 100;
		textlay.gravity = Gravity.CENTER;
		textlay.rightMargin = width * 14 / 100;
		head.setLayoutParams(textlay);
		head.setGravity(Gravity.CENTER | Gravity.LEFT);

		LinearLayout.LayoutParams textlay2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		textlay2.width = width * 9 / 100;
		textlay2.gravity = Gravity.RIGHT;
		textlay2.leftMargin = width * 30 / 100;
		next.setLayoutParams(textlay2);

		LinearLayout.LayoutParams textlay1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		// textlay.width = width * 74 / 100;
		textlay1.gravity = Gravity.RIGHT;
		right_head.setLayoutParams(textlay1);
		right_head.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams cartnu = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		cartnu.width = width * 6 / 100;
		cartnu.height = height * 4 / 100;
		cartnu.leftMargin = width * 10 / 100;
		cartnu.gravity = Gravity.CENTER;
		cartno.setLayoutParams(cartnu);
		cartno.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams logolay1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		logolay1.width = (int) (width * 30.5 / 100);
		logolay1.height = height * 11 / 100;
		logolay1.setMargins(width * 1 / 100, (int) (height * 1.5 / 100),
				width * 1 / 100, 0);
		logolay1.gravity = Gravity.CENTER;
		logolay1.topMargin = height * 3 / 100;
		ctlay.setLayoutParams(logolay1);
		bglay.setLayoutParams(logolay1);
		stlay.setLayoutParams(logolay1);

		LinearLayout.LayoutParams img = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		img.height = height * 4 / 100;
		img.gravity = Gravity.CENTER;
		chatimg.setLayoutParams(img);
		bugsimg.setLayoutParams(img);
		contimg.setLayoutParams(img);

		LinearLayout.LayoutParams img1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		img1.width = (int) (width * 10.5 / 100);
		img1.height = height * 4 / 100;
		img1.gravity = Gravity.CENTER;

		setcount_lay.setLayoutParams(img1);

		LinearLayout.LayoutParams img2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		img2.width = (int) (width * 5.5 / 100);
		img2.height = height * 3 / 100;
		img2.bottomMargin = height * 2 / 100;
		img2.leftMargin = width * 1 / 100;
		img2.gravity = Gravity.CENTER | Gravity.RIGHT;

		setcount_txt.setLayoutParams(img2);

		LinearLayout.LayoutParams imgtext = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		imgtext.height = height * 4 / 100;
		imgtext.gravity = Gravity.CENTER;
		cttext.setLayoutParams(imgtext);
		bgtext.setLayoutParams(imgtext);
		cottext.setLayoutParams(imgtext);
		stext.setLayoutParams(imgtext);
		// LinearLayout.LayoutParams layoutMenuqfoot = new
		// LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.MATCH_PARENT,
		// LinearLayout.LayoutParams.WRAP_CONTENT);
		// layoutMenuqfoot.height = height * 15 / 100;
		// layoutMenuqfoot.gravity = Gravity.CENTER;
		// // layoutMenuqfoot.topMargin = height * 2 / 100;
		// sideMenufoot.setLayoutParams(layoutMenuqfoot);

		if (width >= 600) {
			// LinearLayout.LayoutParams textlayy = new
			// LinearLayout.LayoutParams(
			// LinearLayout.LayoutParams.MATCH_PARENT,
			// LinearLayout.LayoutParams.MATCH_PARENT);
			// textlayy.width = width * 25 / 100;
			// textlayy.gravity = Gravity.CENTER;
			// // textlay.leftMargin = width * 3 / 100;
			// textlayy.rightMargin = width * 14 / 100;
			// head.setLayoutParams(textlayy);
			// head.setGravity(Gravity.CENTER);
			// head.setTextSize(22);
			// next.setTextSize(14);

			// LinearLayout.LayoutParams textlayy = new
			// LinearLayout.LayoutParams(
			// LinearLayout.LayoutParams.MATCH_PARENT,
			// LinearLayout.LayoutParams.MATCH_PARENT);
			// textlayy.width = width * 42 / 100;
			// textlayy.gravity = Gravity.CENTER;
			// // textlay.leftMargin = width * 5 / 100;
			// // textlay.rightMargin = width * 4 / 100;
			// head.setLayoutParams(textlayy);
			// head.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams textlayy = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			textlayy.width = width * 38 / 100;
			textlayy.gravity = Gravity.CENTER | Gravity.LEFT;
			textlay.rightMargin = width * 20 / 100;
			// textlay.leftMargin = width * 5 / 100;
			// textlay.rightMargin = width * 4 / 100;
			head.setLayoutParams(textlayy);
			head.setGravity(Gravity.CENTER | Gravity.LEFT);
			Constant.printMsg("width>600");
			LinearLayout.LayoutParams imglay11 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			imglay11.width = width * 35 / 100;
			imglay11.gravity = Gravity.RIGHT | Gravity.CENTER;
			imglay11.leftMargin = width * 20 / 100;
			imglay11.height = width * 9 / 100;
			imglay11.rightMargin = width * 15 / 100;
			Ka_newlogo.setLayoutParams(imglay11);
			Constant.printMsg("width>600");
		} else if (width > 501 && width < 600) {
			head.setTextSize(21);
			next.setTextSize(12);
			Constant.printMsg("width>500");
		} else if (width > 260 && width < 500) {
			head.setTextSize(20);
			next.setTextSize(10);
			Constant.printMsg("width>200");
		} else if (width <= 260) {
			Constant.printMsg("width<200");
			head.setTextSize(19);
			next.setTextSize(9);
		}
	}
}
