package com.wifin.kachingme.registration_and_login;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wifin.kachingme.adaptors.OfferAdapter;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.adaptors.SpinnerValue;
import com.wifin.kachingme.pojo.FreebieMainDto;
import com.wifin.kachingme.pojo.NewStartUp;
import com.wifin.kachingme.pojo.StartUpDto;
import com.wifin.kachingme.util.Constant;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wifin.kaching.me.ui.R;

public class FreebieActivity extends AppCompatActivity {

	ListView listdata;
	View mBottomView;
	public static TextView freebie_textView1, f_head, f_next;
	List<StartUpDto> jlist = new ArrayList<StartUpDto>();
	static int backpress = 0;
	public static Context context;
	ArrayList<NewStartUp> freebie_list;
	String country_code, full_mobile_no;
	String mobileno;
	String TAG = FreebieActivity.class.getSimpleName();
	Activity myacActivity;
	public static String mcompany = "";
	static List<FreebieMainDto> newStartup_list = new ArrayList<FreebieMainDto>();
	SharedPreferences sharedpreferences;
	Editor editor;
	Dbhelper db;
	private PopupWindow pwindo;
	ListView freeebie_list;
	Button btn_cancel_popup, btn_submit_popup;
	ArrayList<String> myValuearray = new ArrayList<String>();
	String finalSpinnervalue, finalspinvalue;
	String finalimg;
	int finalspin;
//	LinearLayout f_headlay;
	int height,width;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
		setContentView(R.layout.freebie_activity);
		// ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
		// View.inflate(FreebieActivity.this, R.layout.activity_start_up,vg);
		inialize();
		screenArrange();
//		sideMenufoot.setVisibility(LinearLayout.GONE);
		myValuearray.clear();
		myacActivity = this;
		newStartup_list = Constant.freelistmain;
		db = new Dbhelper(myacActivity);
		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(myacActivity);
		context = FreebieActivity.this;
		setActivitystage("FreebieActivity");

		// head.setText("Welcome");
		// next.setVisibility(View.VISIBLE);
		// cart.setVisibility(ImageView.GONE);
		// logo.setVisibility(View.GONE);
		// Ka_newlogo.setVisibility(View.GONE);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mobileno = bundle.getString("mobileno");
			country_code = bundle.getString("country_code");
			// full_mobile_no = country_code + mobileno;
			Constant.printMsg("country:::newst" + mobileno);

		}

		if (Constant.freelistmain.size() > 0) {
            Constant.printMsg("size ::: >>>>" + Constant.freelistmain.size());
			Constant.fullmob = mobileno;

			Constant.countrycode = country_code;
			HashMap<Integer, Integer> myMap = new HashMap<Integer, Integer>(); // loop
																				// list<Student>
																				// &
																				// fill
																				// mapValue

			myMap.put(0, 0);
			Constant.printMsg("size of list ::>>>>>>> "
					+ Constant.freelistmain.size());
			listdata.setAdapter(new OfferAdapter(FreebieActivity.this,
					Constant.freelistmain, getResources(), myMap));

		} else {

			AlertDialog.Builder builder1 = new AlertDialog.Builder(
					FreebieActivity.this);
			builder1.setMessage("Sorry No FreeBie Available");
			builder1.setCancelable(true);
			builder1.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							final Dialog nagDialog = new Dialog(
									FreebieActivity.this);
							nagDialog
									.requestWindowFeature(Window.FEATURE_NO_TITLE);
							nagDialog
									.getWindow()
									.setBackgroundDrawable(
											new ColorDrawable(
													android.graphics.Color.TRANSPARENT));

							nagDialog.setCancelable(true);

							// dialog.cancel();

							android.os.Process.killProcess(android.os.Process
									.myPid());
							System.exit(0);
							finish();

						}
					});

			AlertDialog alert11 = builder1.create();
			alert11.show();

		}
		f_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				myValuearray.clear();
				Constant.printMsg("dddddddddddddddddddd 12333333" );
				if (Constant.spinnerList.size() > 0) {
					Set<Entry<String, SpinnerValue>> setMap = Constant.hm.entrySet();
					Iterator<Entry<String, SpinnerValue>> iteratorMap = setMap.iterator();
					while (iteratorMap.hasNext()) {
						Entry<String, SpinnerValue> entry = (Entry<String, SpinnerValue>) iteratorMap
								.next();
						String Value = entry.getKey();

						SpinnerValue Key = entry.getValue();

						if (!Key.getvalue().equalsIgnoreCase("Select Offer")) {
							finalspin = Key.getspinpos();
							finalimg = entry.getKey();
							finalspinvalue = Key.getvalue();
							myValuearray.add(Key.getvalue());
							Constant.printMsg("nwe start up::"
									+ myValuearray.size() + myValuearray);
						} else {

						}
					}
					Constant.printMsg("dddddddddddddddddddd");
					if (myValuearray.size() == 1) {
						Intent intent = new Intent(FreebieActivity.this,
								AddToCart.class);
						intent.putExtra("mobileno", Constant.fullmob);
						intent.putExtra("country_code", Constant.countrycode);
						startActivity(intent);
						finish();

						Constant.printMsg("vino selected spinn::"
								+ "image selected position::" + finalimg
								+ "spinner selected post::" + finalspin);
						setshared(Integer.parseInt(finalimg), finalspin - 1,
								finalspinvalue);
						Constant.newstlistpos = Integer.parseInt(finalimg);
						Constant.spinnerpos = finalspin - 1;
						Calendar c = Calendar.getInstance();
						c.add(Calendar.DAY_OF_MONTH, +5);
						Constant.printMsg("dfreebie date ::::::" + c.getTime());
						SimpleDateFormat df1 = new SimpleDateFormat(
								"dd-MM-yyyy");

						String last = df1.format(c.getTime());
						ContentValues cv = new ContentValues();
						cv.put("date", last);

						insertToDB(cv);
					} else {
						Toast.makeText(myacActivity,
								"Please select one freebie", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Toast.makeText(myacActivity, "Please select the freebie",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void setActivitystage(String freebieActivity) {
		// TODO Auto-generated method stub
		editor = sharedpreferences.edit();
		editor.putString("activity_name", freebieActivity);
		editor.commit();
	}

	private void inialize() {
		// TODO Auto-generated method stub
		listdata = (ListView) findViewById(R.id.Ka_start_list);
//		f_headlay = (FrameLayout) findViewById(R.id.freebie_headlayout);
		f_next = (TextView) findViewById(R.id.freebie_nexttext);
		f_head = (TextView) findViewById(R.id.freebie_headtext);
		mBottomView= (View) findViewById(R.id.startUp_bottomView);

        Constant.typeFace(this,f_next);
        Constant.typeFace(this,f_head);

    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		AnimateFirstDisplayListener.displayedImages.clear();
		AnimateFirstDisplayListener1.displayedImages.clear();
	}

	public static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	public static class AnimateFirstDisplayListener1 extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		backpress = backpress + 1;
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				backpress = 0;
			}
		}, 3000);
		if (backpress == 1) {
			Toast.makeText(this, "Click again to close Application ",
					Toast.LENGTH_SHORT).show();
		}
		if (backpress == 2) {
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
			finish();
		}
	}

	public static Context getContext() {
		// TODO Auto-generated method stub
		return context;
	}

	protected void setshared(int newstlistpos, int spinnerpos2, String mcompany2) {
		// TODO Auto-generated method stub
		Constant.printMsg("position:::::>>>>>" + newstlistpos + "   "
				+ spinnerpos2);
		editor = sharedpreferences.edit();
		editor.putInt("listpos", newstlistpos);
		editor.putInt("spinnerpos", spinnerpos2);
		editor.putString("company", mcompany2);
		editor.commit();
	}

	protected void insertToDB(ContentValues cv) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		try {
			int a = (int) db.open().getDatabaseObj()
					.insert(Dbhelper.TABLE_FREEBIE, null, cv);
			Constant.printMsg("No of inserted rows in freebie :::::::::" + a);
		} catch (SQLException e) {
			Constant.printMsg("Sql exception in kaching details ::::::"
					+ e.toString());
		} finally {
			db.close();
		}

	}

	private void initiatePopupWindow() {
		try {
			// We need to get the instance of the LayoutInflater
			LayoutInflater inflater = (LayoutInflater) FreebieActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.freebie_screen_popup,
					(ViewGroup) findViewById(R.id.popup_element));
			layout.setPadding(4, 4, 4, 4);
			int x = Constant.screenWidth;
			int y = Constant.screenHeight;
			Constant.printMsg("x :::::::::::" + x);
			Constant.printMsg("y::::::::::::::" + y);
			pwindo = new PopupWindow(layout,
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT, true);
			pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

			btn_cancel_popup = (Button) layout
					.findViewById(R.id.btn_cancel_popup);
			btn_submit_popup = (Button) layout
					.findViewById(R.id.btn_submit_popup);
			freeebie_list = (ListView) layout.findViewById(R.id.freeebie_list);
			freebie_textView1 = (TextView) layout
					.findViewById(R.id.freebie_textView1);
			LinearLayout.LayoutParams list_lay = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			list_lay.width = x * 70 / 100;
			list_lay.setMargins(10, 0, 10, 0);
			list_lay.height = LinearLayout.LayoutParams.WRAP_CONTENT;
			freeebie_list.setLayoutParams(list_lay);

			LinearLayout.LayoutParams text_lay = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			text_lay.width = x * 80 / 100;
			// text_lay.setMargins(10, 0, 10, 0);
			text_lay.height = LinearLayout.LayoutParams.WRAP_CONTENT;
			text_lay.gravity = Gravity.CENTER;
			freebie_textView1.setLayoutParams(text_lay);

			if (width >= 600) {

				freebie_textView1.setTextSize(17);

			} else if (width > 501 && width < 600) {

				freebie_textView1.setTextSize(16);

			} else if (width > 260 && width < 500) {

				freebie_textView1.setTextSize(15);

			} else if (width <= 260) {

				freebie_textView1.setTextSize(14);

			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_single_choice,
					android.R.id.text1, myValuearray);

			// Assign adapter to ListView
			freeebie_list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			pwindo.setOutsideTouchable(true);
			pwindo.setFocusable(true);
			// Removes default background.
			// pwindo.setBackgroundDrawable(new
			// ColorDrawable(Color.TRANSPARENT));
			freeebie_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					// ListView Clicked item index
					int itemPosition = position;

					// ListView Clicked item value
					String itemValue = (String) freeebie_list
							.getItemAtPosition(position);

					// Show Alert
					// Toast.makeText(
					// getApplicationContext(),
					// "Position :" + itemPosition + "  ListItem : "
					// + itemValue, Toast.LENGTH_LONG).show();
					finalSpinnervalue = itemValue;

				}
			});

			btn_cancel_popup.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					pwindo.dismiss();
					myValuearray.clear();
					Constant.spinnerList.clear();
					Intent intent = new Intent(myacActivity,
							FreebieActivity.class);
					startActivity(intent);
				}
			});
			btn_submit_popup.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
                    Constant.printMsg("finalSpinnervalue=btn_submit_popup;::"
									+ finalSpinnervalue);
					int spin = 0;
					if (finalSpinnervalue != null) {

						pwindo.dismiss();

						// Set<Entry<String, List<Integer>>> setMap =
						// Constant.hm
						// .entrySet();
						// Iterator<Entry<String, List<Integer>>> iteratorMap =
						// setMap
						// .iterator();
						// while (iteratorMap.hasNext()) {
						// Map.Entry<String, List<Integer>> entry =
						// (Map.Entry<String, List<Integer>>) iteratorMap
						// .next();
						// String Value = entry.getKey();
						// List<Integer> Key = entry.getValue();
						// Constant.printMsg("key size is:" + Key.size());
						// for (int i = 0; i < Key.size(); i++) {
						//
						// int spinn = Key.get(i);
						// Constant.printMsg("vino spinn::" + spinn
						// + "image position::" + Key.get(0)
						// + "spinner post::" + Key.get(1));
						//
						// }
						// Constant.printMsg("vino Value:::" + Value);
						// if (Value.equalsIgnoreCase(finalSpinnervalue)) {
						// Constant.printMsg("vino selected spinn::"
						// + "image selected position::"
						// + Key.get(0)
						// + "spinner selected post::"
						// + Key.get(1));
						// setshared(Key.get(0), Key.get(1),
						// finalSpinnervalue);
						// Constant.newstlistpos = Key.get(0);
						// } else {
						// Constant.printMsg("vino not match");
						// }
						//
						// }

						Set<Entry<String, SpinnerValue>> setMap = Constant.hm.entrySet();
						Iterator<Entry<String, SpinnerValue>> iteratorMap = setMap
								.iterator();
						while (iteratorMap.hasNext()) {
							Entry<String, SpinnerValue> entry = (Entry<String, SpinnerValue>) iteratorMap
									.next();
							String Value = entry.getKey();

							SpinnerValue Key = entry.getValue();
							Constant.printMsg("key size is:"
									+ Key.getspinpos() + "values::"
									+ Key.getvalue());
							Constant.printMsg("vino Value:::key" + Value);

							Constant.printMsg("vino Value:::spinner"
									+ Key.getvalue());
							if (Key.getvalue().equalsIgnoreCase(
									finalSpinnervalue)) {
								Constant.printMsg("vino selected spinn::"
										+ "image selected position::"
										+ entry.getKey()
										+ "spinner selected post::"
										+ Key.getspinpos());
								setshared(Integer.parseInt(entry.getKey()),
										Key.getspinpos() - 1, Key.getvalue());
								spin = Key.getspinpos();
								Constant.newstlistpos = Integer.parseInt(entry
										.getKey());
							} else {
								Constant.printMsg("vino not match");
							}
						}

						Calendar c = Calendar.getInstance();
						c.add(Calendar.DAY_OF_MONTH, +5);
						Constant.printMsg("dfreebie date ::::::" + c.getTime());
						SimpleDateFormat df1 = new SimpleDateFormat(
								"dd-MM-yyyy");
						System.out
								.println("last date of freebie::::::::>>>>>>>"
										+ df1.format(c.getTime()));
						String last = df1.format(c.getTime());
						ContentValues cv = new ContentValues();
						cv.put("date", last);

						insertToDB(cv);
						Intent intent = new Intent(myacActivity,
								AddToCart.class);
						intent.putExtra("mobileno", Constant.fullmob);
						intent.putExtra("country_code", Constant.countrycode);
						intent.putExtra("spin_pos", spin);
						Constant.spinnerpos = spin - 1;
						myacActivity.startActivity(intent);
						myacActivity.finish();
						myValuearray.clear();
						Constant.printMsg("went vino selected spinn::in else"
										+ Constant.spinnerpos
										+ "finalspinvalue" + finalspinvalue
										+ "spinner selected post::" + finalspin);
					} else {
						Constant.printMsg("vino select one");
						Toast.makeText(FreebieActivity.this,
								"Please select one", Toast.LENGTH_SHORT).show();

					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void screenArrange() {
		// TODO Auto-generated method stub
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		height = displayMetrics.heightPixels;
		width = displayMetrics.widthPixels;
		Constant.screenHeight = height;
		Constant.screenWidth = width;

		LinearLayout.LayoutParams layoutdatat = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutdatat.height = (int) (height * 83 / 100);
		layoutdatat.gravity = Gravity.CENTER;
//		datalay.setLayoutParams(layoutdatat);

		FrameLayout.LayoutParams textlay = new FrameLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		//textlay.width = width * 28 / 100;
		textlay.height = height * 7 / 100;
		textlay.gravity = Gravity.CENTER;
		f_head.setLayoutParams(textlay);
		f_head.setGravity(Gravity.CENTER);

		FrameLayout.LayoutParams cartnu = new FrameLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		cartnu.height = height * 7 / 100;
		cartnu.rightMargin=width*3/100;
		cartnu.gravity = Gravity.CENTER|Gravity.RIGHT;
		f_next.setLayoutParams(cartnu);
		f_next.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams bottomViewParama = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		bottomViewParama.height = (int) (height * 0.6 / 100);
		bottomViewParama.gravity = Gravity.CENTER | Gravity.BOTTOM;
		mBottomView.setLayoutParams(bottomViewParama);

		if (width >= 600) {
			f_head.setTextSize(17);
			f_next.setTextSize(17);
		} else if (width > 501 && width < 600) {
			f_head.setTextSize(16);
			f_next.setTextSize(16);
		} else if (width > 260 && width < 500) {
			f_head.setTextSize(15);
			f_next.setTextSize(15);
		} else if (width <= 260) {
			f_head.setTextSize(14);
			f_next.setTextSize(14);
		}
	}
}
