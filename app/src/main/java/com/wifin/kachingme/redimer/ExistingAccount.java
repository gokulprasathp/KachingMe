package com.wifin.kachingme.redimer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.deals.VerifyShop;
import com.wifin.kachingme.adaptors.ExistingAccountAdapter;
import com.wifin.kachingme.pojo.ShopDetailsDto;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.GPSTracking;
import com.wifin.kaching.me.ui.R;

public class ExistingAccount extends HeaderActivity {
LinearLayout mSearchLayout;
	ListView $ExistingList;
	EditText $SearchEdit;
	TextView mRedeemerAlert;
	SearchView $SearchView;
	// List<CartAdapDto> listCart = new ArrayList<CartAdapDto>();
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	protected LocationManager mLocationManager;
	double mLatitude = 0, mLongitude = 0;
	List<ShopDetailsDto> shopResponse = new ArrayList<ShopDetailsDto>();
	List<ShopDetailsDto> shopListAdapter = new ArrayList<ShopDetailsDto>();
	String country;
    int height,width;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
		ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
		View.inflate(this, R.layout.existing_account, vg);
		mHeading.setText("Redeemer List");
		mNextBtn.setVisibility(View.INVISIBLE);
		mHeaderImg.setVisibility(View.GONE);
		mFooterLayout.setVisibility(View.GONE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mLocationManager = (LocationManager) getApplicationContext()
				.getSystemService(LOCATION_SERVICE);
		isGPSEnabled = mLocationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		isGPSEnabled = mLocationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		existingInitialize();
		existingScreenArrange();
		mBackBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ExistingAccount.this, VerifyShop.class));
				finish();
			}
		});
		if (isGPSEnabled || isNetworkEnabled) {

		} else {
			turnGPSOn();
		}

        GPSTracking mGPSService = new GPSTracking(ExistingAccount.this);

		if (isGPSEnabled || isNetworkEnabled) {
			// Constant.printMsg("HIIIIIIIIIIII");

			mGPSService.getLocation();

			if (mGPSService.getLatitude() != 0
					&& mGPSService.getLongitude() != 0) {

				mLatitude = mGPSService.getLatitude();
				mLongitude = mGPSService.getLongitude();

			}

			mGPSService.stopUsingGPS();

		} else {
			Toast.makeText(getApplicationContext(),
					"Location Service is not active.", Toast.LENGTH_SHORT)
					.show();
			// mGPSService.showSettingsAlert();
		}

		if (Connectivity.isConnected(ExistingAccount.this)) {
			new getShopData().execute();
		} else {
			Toast.makeText(getApplicationContext(),
					"Please check your network connection", Toast.LENGTH_SHORT)
					.show();
		}

		$ExistingList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Constant.mFreebieShopName = shopResponse.get(position)
						.getName();
				Constant.mFreebieShopId = String.valueOf(shopResponse.get(
						position).getShopId());
				Constant.mFreebieShopAddress = shopResponse.get(position)
						.getAddress();
				Constant.mFreebieShopEmailid = shopResponse.get(position)
						.getEmail();
				Constant.mFreebieShopPhoneNumber = shopResponse.get(position)
						.getShopPhoneNumber();

				startActivity(new Intent(ExistingAccount.this,
						ExistingAccountDetails.class));
				finish();
			}
		});

		/**
		 * Enabling Search Filter
		 * */
		$SearchEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				// DailySearch.this.arrayAutoListAdapter.getFilter().filter(cs);
				if (cs.length() > 0) {
					getSearchText(String.valueOf(cs).trim());
				} else {
					getSearchText(null);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void existingInitialize() {
		// TODO Auto-generated method stub
		$ExistingList = (ListView) findViewById(R.id.existingAccount_list);
		$SearchEdit = (EditText) findViewById(R.id.existingAccount_serchEdit);
		$SearchEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
		$SearchView = (SearchView) findViewById(R.id.existingAccount_serchView);
		mSearchLayout = (LinearLayout) findViewById(R.id.search_layout);
		mRedeemerAlert = (TextView) findViewById(R.id.existingAccount_noRedeemerText);
		Constant.typeFace(this,$SearchEdit);
		Constant.typeFace(this,mRedeemerAlert);
	}

	public void getSearchText(String ch) {
		Constant.printMsg("List size is ::::::" + shopResponse.size()
				+ " and the character is :::::" + ch);

		List<ShopDetailsDto> list = new ArrayList<ShopDetailsDto>();
		// list = null;
		if (ch != null && ch.length() > 0) {
			// Constant.printMsg("HIIIII00000");
			for (int i = 0; i < shopResponse.size(); i++) {
				ShopDetailsDto dc = new ShopDetailsDto();
				// if
				// (mListDailySearch.get(i).getDoctorChemistName().contains(ch))
				// {
				if (Pattern
						.compile(Pattern.quote(ch), Pattern.CASE_INSENSITIVE)
						.matcher(shopResponse.get(i).getName()).find()) {
					// Constant.printMsg("Hiiiiiii");
					dc = shopResponse.get(i);
					list.add(dc);
				}

			}

		} else {
			list = shopResponse;
		}

		shopListAdapter = list;

		Constant.printMsg("List size after filtered is :::::"
				+ shopListAdapter.size());
		if (shopListAdapter.size()>0){
			mRedeemerAlert.setVisibility(View.GONE);
			$ExistingList.setVisibility(View.VISIBLE);
			ExistingAccountAdapter dailyAdapter = new ExistingAccountAdapter(
					ExistingAccount.this, shopListAdapter);
			$ExistingList.setAdapter(dailyAdapter);
			dailyAdapter.notifyDataSetChanged();
			// $SearchListView.setAdapter(new DailySearchAdapter(DailySearch.this,
			// mFilteredListDailySearch));
		}else{
			$ExistingList.setVisibility(View.GONE);
			mRedeemerAlert.setVisibility(View.VISIBLE);
		}
	}

	private class getShopData extends AsyncTask<String, String, String> {

		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(ExistingAccount.this,
					AlertDialog.THEME_HOLO_LIGHT);
			progressDialog.setMessage("Please Wait...");
			progressDialog.setProgressDrawable(new ColorDrawable(
					Color.BLUE));
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... params) {
			String result;
			HttpConfig ht = new HttpConfig();
			result = ht.httpget(KachingMeConfig.REDEEM_SHOP_DATA + "&lattitude="
					+ mLatitude + "&longitude=" + mLongitude);
			// ht.httpget("");
			return result;

		}

		protected void onProgressUpdate(String... progress) {
		}

		protected void onPostExecute(String result) {
			// sourceedit.setText(result);

			Constant.printMsg("Check shop data result :::" + result);

			progressDialog.dismiss();

			if (result != null && result.length() > 0) {

				Gson g = new Gson();
				InputStream is = new ByteArrayInputStream(result.getBytes());
				Reader reader = new InputStreamReader(is);
				Type fooType = new TypeToken<List<ShopDetailsDto>>() {
				}.getType();
				shopResponse = g.fromJson(reader, fooType);

				// $ExistingList.setAdapter(new ExistingAccountAdapter(
				// ExistingAccount.this, shopResponse));

				getSearchText(null);

			} else {
				Toast.makeText(getApplicationContext(), "No shop found",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	private class GetAddressTask extends AsyncTask<Double, String, String> {
		// TextView currenta_adddress_view;
		// double getlat, getlng;
		// double address_type;

		// public GetAddressTask(TextView txtview) {
		// currenta_adddress_view = txtview;
		// }
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(ExistingAccount.this,
					AlertDialog.THEME_HOLO_LIGHT);
			progressDialog.setMessage("Please Wait...");
			progressDialog.setProgressDrawable(new ColorDrawable(
					Color.BLUE));
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

		}

		/**
		 * Get a Geocoder instance, get the latitude and longitude look up the
		 * address, and return it
		 *
		 * @return A string containing the address of the current location, or
		 *         an empty string if no address can be found, or an error
		 *         message
		 * @params params One or more Location objects
		 */
		@Override
		protected String doInBackground(Double... params) {
			// getlat = params[0];
			// getlng = params[1];
			// address_type = params[2];
			Geocoder geocoder = new Geocoder(getApplicationContext(),
					Locale.getDefault());
			List<Address> addresses = null;
			try {
				addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);

				country = addresses.get(0).getCountryName();

			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (IllegalArgumentException e2) {
				// Error message to post in the log
				String errorString = "Illegal arguments "
						+ Double.toString(params[0]) + " , "
						+ Double.toString(params[1])
						+ " passed to address service";
				e2.printStackTrace();
				return errorString;
			} catch (NullPointerException np) {
				// TODO Auto-generated catch block
				np.printStackTrace();
			}
			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {
				// Get the first address
				Address address = addresses.get(0);
				/*
				 * Format the first line of address (if available), city, and
				 * country name.
				 */
				String addressText = null;
				StringBuffer addr = new StringBuffer();
				for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
					addressText = address.getAddressLine(i);
					if (i == address.getMaxAddressLineIndex()) {
						addr.append(addressText);
					} else {
						addr.append(addressText + ",");
					}

				}
				// Return the text
				return addr.toString();
			} else {
				return "No address found";
			}
		}

		protected void onProgressUpdate(String... progress) {
		}

		protected void onPostExecute(String result) {
			// sourceedit.setText(result);
			progressDialog.dismiss();
			if (result != null && !result.isEmpty()) {
				if (!result.equals("IO Exception trying to get address")
						|| !result.equalsIgnoreCase("No address found")) {
					Constant.mFreebieCurrentCountry = country;
				}
			} else {
				Toast empty_fav = Toast.makeText(getApplicationContext(),
						"Please Try Again", Toast.LENGTH_LONG);
				empty_fav.show();
			}
		}
	}

	private void turnGPSOn() {
		// Get Location Manager and check for GPS & Network location services
		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// Build the alert dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Location Services Not Active");
			builder.setMessage("Please enable Location Services and GPS");
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialogInterface,
								int i) {
							// Show location settings when the user acknowledges
							// the alert dialog
							// updateTempLogout(true);
							Intent intent = new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS);

							startActivity(intent);
							// finish();
						}
					});
			Dialog alertDialog = builder.create();
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.setCancelable(false);
			alertDialog.show();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		startActivity(new Intent(ExistingAccount.this, VerifyShop.class));
		finish();
	}

	private void existingScreenArrange() {
		// TODO Auto-generated method stub
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		height = metrics.heightPixels;
		width = metrics.widthPixels;
		Constant.screenWidth = width;
		Constant.screenHeight = height;

		 LinearLayout.LayoutParams contentLayoutParams = new LinearLayout.LayoutParams(
	                LinearLayout.LayoutParams.WRAP_CONTENT,
	                LinearLayout.LayoutParams.WRAP_CONTENT);
	        contentLayoutParams.width = width ;
	        contentLayoutParams.height = height * 87 / 100;
	        contentLayoutParams.gravity = Gravity.CENTER;
//	        mContentLayout.setLayoutParams(contentLayoutParams);

		LinearLayout.LayoutParams listViewParama = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		// listViewParama.height=height*8/100;
		// listViewParama.width=width;
		// listViewParama.topMargin=height*1/100;;
//		$ExistingList.setLayoutParams(listViewParama);
		LinearLayout.LayoutParams searchlayoutParama = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		searchlayoutParama.width = width * 94 / 100;
		searchlayoutParama.height = height  * 8 / 100;
		searchlayoutParama.leftMargin = width * 3 / 100;
		searchlayoutParama.rightMargin = width * 3 / 100;
		searchlayoutParama.topMargin = width * 5 / 100;
		searchlayoutParama.bottomMargin = width * 5 / 100;
		mSearchLayout.setLayoutParams(searchlayoutParama);

		LinearLayout.LayoutParams searchParama = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		searchParama.height = height * 7 / 100;
		// searchParama.width=width;
		searchParama.gravity = Gravity.CENTER;
		$SearchEdit.setLayoutParams(searchParama);
		$SearchEdit.setGravity(Gravity.CENTER | Gravity.LEFT);
		$SearchEdit.setPadding(width * 2 / 100, 0, width * 2 / 100, 0);
		Drawable icon = getResources().getDrawable(R.drawable.search);
		icon.setBounds(0, 0, (int) (icon.getIntrinsicWidth() * 0.5),
				(int) (icon.getIntrinsicHeight() * 0.5));
		// ScaleDrawable sd = new ScaleDrawable(icon, 0, 0, 0);
		$SearchEdit.setCompoundDrawables(null, null, icon, null);
		$SearchEdit.setBackground(null);

		$SearchView.setLayoutParams(searchParama);
		$SearchView.setGravity(Gravity.CENTER | Gravity.LEFT);
		$SearchView.setPadding(width * 2 / 100, 0, 0, 0);

		LinearLayout.LayoutParams redeemerAlert= new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		//redeemerAlert.height = (int) (height * 83 / 100);
		redeemerAlert.gravity = Gravity.CENTER;
		mRedeemerAlert.setLayoutParams(redeemerAlert);
		mRedeemerAlert.setGravity(Gravity.CENTER);

		if (width >= 600) {
			$SearchEdit.setTextSize(18);
			mRedeemerAlert.setTextSize(19);
		} else if (width > 501 && width < 600) {
			$SearchEdit.setTextSize(17);
			mRedeemerAlert.setTextSize(18);
		} else if (width > 260 && width < 500) {
			$SearchEdit.setTextSize(16);
			mRedeemerAlert.setTextSize(17);
		} else if (width <= 260) {
			$SearchEdit.setTextSize(15);
			mRedeemerAlert.setTextSize(16);
		}

	}

}
