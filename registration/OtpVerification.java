package com.wifin.kachingme.registration_and_login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wifin.kachingme.R;
import com.wifin.kachingme.applications.NiftyApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.CartDetailsDto;
import com.wifin.kachingme.pojo.ContactPost;
import com.wifin.kachingme.pojo.ContactResponseDto;
import com.wifin.kachingme.pojo.FreeBieRequest;
import com.wifin.kachingme.pojo.FreebieDto;
import com.wifin.kachingme.pojo.FreebieMainDto;
import com.wifin.kachingme.pojo.LoginResponseDto;
import com.wifin.kachingme.pojo.RestUserDetailsDto;
import com.wifin.kachingme.pojo.SecondaryUserResponse;
import com.wifin.kachingme.services.ContactLastSync;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.AvatarManager;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.GPSTrackerUtils;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.WebConfig;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//import cz.msebera.android.httpclient.Header;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;

public class OtpVerification extends Slideshow implements View.OnClickListener{

	TextView mMsgTop,mMsgBottom,mReSend,mNextButton;
	EditText mVerificationCode;
	LinearLayout mNextLayout;
	String mobileno;
	String TAG = OtpVerification.class.getSimpleName();
	String randome_no = null;
	String pin, data, data1;
	String country_code, full_mobile_no;
	String SMS_GATEWAY_URL = "";
	GPSTrackerUtils gpsTracker;
	Geocoder geocoder;
	double latitude, longitude;
	String stringLatitude, stringLongitude;
	StatusLine resultst;
	Dbhelper dbHelper;
	SharedPreferences preference;
	DatabaseHelper dbAdapter;
	Editor ed;
	CommonMethods commonMethods=new CommonMethods(OtpVerification.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		StrictMode.setThreadPolicy(policy);
		ViewGroup vg = (ViewGroup) findViewById(R.id.register_mainLayout);
		View.inflate(this, R.layout.otp_verification, vg);
		initialization();
		screenArrangeOtpVerification();
		sIndicator.setVisibility(View.GONE);

		preference = PreferenceManager.getDefaultSharedPreferences(this);
		dbHelper = new Dbhelper(getApplicationContext());
		dbAdapter = NiftyApplication.getDatabaseAdapter();
		ed = preference.edit();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mobileno = bundle.getString("mobileno");
			country_code = bundle.getString("country_code");
			full_mobile_no = mobileno;
			Log.d(TAG, "Mobile NO::" + full_mobile_no);
			Constant.printMsg("country:::sms" + full_mobile_no);
		}
		gpsTracker = new GPSTrackerUtils(OtpVerification.this);
		// check if GPS enabled
		if (gpsTracker.canGetLocation()) {
			latitude = gpsTracker.getLatitude();
			longitude = gpsTracker.getLongitude();
		} else {
			gpsTracker.showSettingsAlert();
		}
		mMsgTop.setText(Html.fromHtml(String.format(
				getResources().getString(R.string.we_sent_sms),
				String.valueOf(Constant.mVerifiedNum))));
		randome_no = Constant.Otp;
		mVerificationCode.setText(randome_no);
		Log.d(TAG, "Randome No::" + randome_no);
		RequestParams request_params = new RequestParams();
		AsyncHttpClient client = new AsyncHttpClient();
		Constant.printMsg("mobjkjkfd::" + mobileno);
		// http://198.24.149.4/API/pushsms.aspx?loginID=velkesh&password=123456&mobile=9943933949&text=hai&senderid=wifint&route_id=2&Unicode=0"
//		String mOTPMessege = "http://198.24.149.4/API/pushsms.aspx?loginID=velkesh&password=123456&mobile="
//				+ URLEncoder.encode(Constant.otpnumner)
//				+ "&text="
//				+ URLEncoder.encode("Kaching.me OTP is" + randome_no)
//				+ "&senderid=wifint&route_id=2&Unicode=0";
		// ServiceClass.getServerCall(mOTPMessege);

		client.post(SMS_GATEWAY_URL, request_params,
				new AsyncHttpResponseHandler(Looper.getMainLooper()) {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.d(TAG, "SMS Service Success ");
					}
				});

	}

	private void initialization() {
		mMsgTop = (TextView) findViewById(R.id.otp_topMessage);
		mVerificationCode = (EditText) findViewById(R.id.otp_verification_code);
		mMsgBottom = (TextView) findViewById(R.id.otp_bottomText);
		mReSend= (TextView) findViewById(R.id.otp_resend);
		mNextButton= (TextView) findViewById(R.id.otp_next);
		mNextLayout= (LinearLayout) findViewById(R.id.otp_nextLayout);

		Constant.typeFace(this, mMsgTop);
		Constant.typeFace(this, mVerificationCode);
		Constant.typeFace(this, mMsgBottom);
		Constant.typeFace(this, mReSend);
		Constant.typeFace(this, mNextButton);

		mReSend.setOnClickListener(this);
		mNextButton.setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.otp_resend:
				re_send();
				break;
			case R.id.otp_next:
				nextSubmitProcess();
				break;
		}
	}

	public String gen5DigitNumber() {
		Random rng = new Random();
		int val = rng.nextInt(100000);
		return String.format("%05d", val);
	}

	public void re_send() {
		RequestParams request_params = new RequestParams();
		AsyncHttpClient client = new AsyncHttpClient();
		String mOTPMessege = "http://198.24.149.4/API/pushsms.aspx?loginID=velkesh&password=123456&mobile="
				+ URLEncoder.encode(mobileno)
				+ "&text="
				+ URLEncoder.encode("Kaching.me verification code "
				+ randome_no) + "&senderid=wifint&route_id=2&Unicode=0";
		client.post(SMS_GATEWAY_URL, request_params,
				new AsyncHttpResponseHandler(Looper.getMainLooper()) {
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						Log.d(TAG, "SMS Service Failure");
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.d(TAG, "SMS Service Success");
					}

				});
	}

	private void nextSubmitProcess() {
		if (mVerificationCode.getText().toString().trim()
				.equals(randome_no)) {
			stringLatitude = String.valueOf(latitude);
			stringLongitude = String.valueOf(longitude);
			if (Constant.addverification == false) {
				if (Connectivity.isConnected(OtpVerification.this)) {
					data = jsonForm();
					new postOtp().execute();
					Editor e1 = preference.edit();
					e1.putInt("regpoints", 1);
					e1.commit();
				} else {
					Toast.makeText(OtpVerification.this,
							"Please check your network connection",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Constant.addverification = false;
				data = responseJson();
				new postSecondaryRes().execute();
				int count1 = preference.getInt("added_num", 0);
				int count = count1 + 1;
			}
		} else {
			commonMethods.showAlertDialog(this, getResources()
							.getString(R.string.sorry_you_have_entered_wrong_pin), true);
		}
	}

	public String jsonForm() {
		String d = null;
		FreeBieRequest l = new FreeBieRequest();
		l.setCountry(Constant.country);
		l.setLattitude(stringLatitude);
		// l.setContactListDtos(Constant.contactlist);
		l.setLongitude(stringLongitude);
		l.setOtp(Constant.Otp);
		l.setPhoneNumber(country_code + Constant.phone);
		l.setResponseCode(Constant.responsecode);

		Editor e1 = preference.edit();
		e1.putString("countrycode", country_code);
		e1.putString("number", Constant.phone);

		e1.commit();

		d = new Gson().toJson(l);

		Constant.printMsg("order product name ::::" + d.toString());
		return d;
	}

	public String responseJson() {
		String d = null;

		SecondaryUserResponse l = new SecondaryUserResponse();

		l.setOtp(Constant.Otp);
		l.setSecondaryNumber(full_mobile_no);

		d = new Gson().toJson(l);

		Constant.printMsg("respiknse jso:" + d.toString());
		return d;
	}

	public String jsonFormContact() {
		String d = null;

		ContactPost l = new ContactPost();

		l.setPhoneNumber(country_code + Constant.phone);
		l.setUserContactDtos(Constant.contactlist);

		d = new Gson().toJson(l);

		Constant.printMsg("order product name ::::" + d.toString());
		return d;
	}

	public class postSecondaryRes extends AsyncTask<String, String, String> {
		ProgressDialog progressDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub

			super.onPreExecute();
			progressDialog = new ProgressDialog(OtpVerification.this,
					AlertDialog.THEME_HOLO_LIGHT);
			progressDialog.setMessage("Please Wait...");
			progressDialog.setProgressDrawable(new ColorDrawable(
					android.graphics.Color.BLUE));
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = null;
			HttpConfig ht = new HttpConfig();

			result = ht.httpget(WebConfig.SECONDARY_URL_RES + full_mobile_no
					+ "&otp=" + Constant.Otp);

			Constant.printMsg("result dis verification" + result + "    "
					+ data);

			return result;
		}

		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			progressDialog.dismiss();

			if (result != null && result.length() > 0) {

				if (result.equalsIgnoreCase("Number Not Exists")) {

					Toast.makeText(getApplicationContext(),
							"Invalid User...Mobile Number  And Otp Wrong",
							Toast.LENGTH_SHORT).show();

				} else {
					// Intent i = new Intent(OtpVerification.this,
					// VerificationActivity.class);
					// startActivity(i);
					// finish();
					int count = preference.getInt("added_num", 0);

					ed = preference.edit();
					ed.putInt("added_num", count + 1);
					ed.commit();
					Intent intent = new Intent(OtpVerification.this,
							VerificationActivity.class);
					Constant.fullmob = full_mobile_no;
					Constant.countrycode = country_code;
					intent.putExtra("mobileno", preference.getString("countrycode", "")
							+ preference.getString("number", ""));
					intent.putExtra("country_code", country_code);
					startActivity(intent);
					finish();
				}

			} else {
				Toast.makeText(getApplicationContext(),
						"Network Error!Please try again later!",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	public class postOtp extends AsyncTask<String, String, String> {
		ProgressDialog progressDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub

			super.onPreExecute();
			progressDialog = new ProgressDialog(OtpVerification.this,
					AlertDialog.THEME_HOLO_LIGHT);
			progressDialog.setMessage("Please Wait...");
			progressDialog.setProgressDrawable(new ColorDrawable(
					android.graphics.Color.BLUE));
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = null;
			HttpConfig ht = new HttpConfig();

			result = ht.doPostMobizee(data, WebConfig.OTP_URL);

			Constant.printMsg("result dis344" + data);

			Constant.printMsg("result dis" + result);

			return result;
		}

		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Constant.printMsg("Priya Test ::>>>> " + result.toString());
			progressDialog.dismiss();

			if (result != null && result.length() > 0) {

				data1 = jsonFormContact();
				Constant.printMsg("siva check contact data:::::::" + data1);
				// new postcontact().execute();

				Constant.freelistmain.clear();

				List<FreebieMainDto> freelistlocalmain = new ArrayList<FreebieMainDto>();
				List<LoginResponseDto> localmail = new ArrayList<LoginResponseDto>();

				try {
					JSONObject jobj = new JSONObject(result);
					JSONArray jarray = jobj
							.getJSONArray("stzzleCampgnDetailsRestDtos");
					for (int i = 0; i < jarray.length(); i++) {
						JSONObject jobj1 = jarray.getJSONObject(i);

						FreebieMainDto ns = new FreebieMainDto();
						List<FreebieDto> freelistlocal = new ArrayList<FreebieDto>();

						ns.setAdvertiserId(jobj1.getString("advertiserId"));
						ns.setCompanyLogoPath(jobj1
								.getString("companyLogoPath"));
						ns.setFirstName(jobj1.getString("firstName"));
						ns.setLastName(jobj1.getString("lastName"));
						ns.setWebsite(jobj1.getString("website"));
						ns.setCompanyName(jobj1.getString("companyName"));

						JSONArray jar = jobj1
								.getJSONArray("stZzleCampgnDetailsDtos");
						for (int j = 0; j < jar.length(); j++) {
							JSONObject jobj2 = jar.getJSONObject(j);

							FreebieDto m = new FreebieDto();

							m.setId(jobj2.getString("id"));
							m.setAdvertiser(jobj2.getString("advertiser"));
							m.setLanguage(jobj2.getString("language"));
							m.setName(jobj2.getString("name"));
							m.setNoOfDaysDisplayed(jobj2
									.getString("noOfDaysDisplayed"));
							m.setNoOfFreebies(jobj2.getString("noOfFreebies"));
							m.setPhotoPath(jobj2.getString("photoPath"));
							m.setQrCode(jobj2.getString("qrCode"));
							m.setQrCodePath(jobj2.getString("qrCodePath"));
							m.setTagLine(jobj2.getString("tagLine"));
							m.setTimePeriod(jobj2.getString("timePeriod"));
							m.setUserId(jobj2.getString("userId"));
							m.setValid_until(jobj2.getString("valid_until"));
							m.setValid_untilSrt(jobj2
									.getString("valid_untilSrt"));

							freelistlocal.add(m);

						}

						ns.setFreebilist(freelistlocal);

						freelistlocalmain.add(ns);

						ContentValues cv = new ContentValues();
						cv.put("countryc", country_code);
						cv.put("phonenumber", full_mobile_no);
						cv.put("advertiserId", jobj1.getString("firstName"));
						cv.put("companyLogoPath",
								jobj1.getString("companyLogoPath"));
						cv.put("firstName", jobj1.getString("firstName"));
						cv.put("lastName", jobj1.getString("lastName"));
						cv.put("website", jobj1.getString("website"));
						Constant.printMsg("mesaagess   ;:::::::>>>>>>> "
								+ freelistlocal);
						cv.put("freebielist", new Gson().toJson(freelistlocal));

						insertDBFreeBie(cv);

					}

					Constant.freelistmain.addAll(freelistlocalmain);

					JSONObject jar = jobj.getJSONObject("restUserDetailsDto");

					RestUserDetailsDto cartlocalmail = new RestUserDetailsDto();

					for (int i = 0; i < jar.length(); i++) {

						List<CartDetailsDto> cartlocal = new ArrayList<CartDetailsDto>();

						cartlocalmail.setBux(jar.getLong("bux"));
						Long bx = jar.getLong("bux");
						Constant.bux = bx;
						Constant.printMsg("buxxx:::" + jar.getLong("bux"));
						cartlocalmail.setUserId(jar.getLong("userId"));
						Long usid = jar.getLong("userId");
						Constant.userId = usid;

						JSONArray jar1 = jar.getJSONArray("cartDetailsDtos");

						for (int j = 0; j < jar1.length(); j++) {
							JSONObject jObject4 = jar1.getJSONObject(j);

							CartDetailsDto m = new CartDetailsDto();

							m.setUserId(jObject4.getLong("userId"));
							m.setCartDetailsId(jObject4
									.getLong("cartDetailsId"));
							m.setPhoneNumber(jObject4.getString("phoneNumber"));
							m.setOfferId(jObject4.getLong("offerId"));
							m.setType(jObject4.getInt("type"));
							m.setDescription(jObject4.getString("description"));
							m.setPhotoPath(jObject4.getString("photoPath"));
							m.setBux(jObject4.getLong("bux"));
							m.setQrCodePath(jObject4.getString("qrCodePath"));
							m.setIsDelete(jObject4.getInt("isDelete"));

							System.out
									.println("list Dtyo::" + m.getPhotoPath());
							cartlocal.add(m);

							ContentValues cv = new ContentValues();

							cv.put("phonenumber",
									jObject4.getString("phoneNumber"));
							cv.put("deelid", jObject4.getLong("offerId"));
							cv.put("type", jObject4.getInt("type"));
							cv.put("desc", jObject4.getString("description"));
							cv.put("photopath", jObject4.getString("photoPath"));
							cv.put("bux", jObject4.getLong("bux"));
							cv.put("qrpath", jObject4.getString("qrCodePath"));

							insertDB(cv);

						}

						cartlocalmail.setCartDetailsDtos(cartlocal);

					}

					Constant.restlistmain.add(cartlocalmail);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (Constant.bux != null) {

					ContentValues cv = new ContentValues();

					cv.put("bux", Constant.bux);
					cv.put("phonenumber", Constant.userId);

					insertDBBux(cv);

					Editor e = preference.edit();
					e.putLong("buxvalue", Constant.bux);
					e.putLong("uservalue", Constant.userId);
					e.commit();

					Constant.point = Long.parseLong("0");

					Editor e1 = preference.edit();
					e1.putLong("donationpoint", Constant.point);
					e1.commit();
				}
				// LoginResponseDto l = new LoginResponseDto();
				// l.setRestUserDetailsDto(cartlocalmail);
				// l.setStzzleCampgnDetailsRestDtos(freelistlocalmain);
				// localmail.add(l);
				// Constant.listmainres.add(l);

				Constant.printMsg("sysout::" + Constant.freelistmain.size()
						+ "cary size:" + Constant.restlistmain.size());
				Constant.printMsg("Priya Test ::>>>>111 "
						+ Constant.freelistmain.size() + "  "
						+ Constant.restlistmain.size());
				if (Constant.freelistmain.size() > 0) {
					// mToast.cancel();
					Intent intent = new Intent(OtpVerification.this,
							VerificationActivity.class);
					Constant.fullmob = full_mobile_no;
					Constant.countrycode = country_code;
					intent.putExtra("mobileno", preference.getString("countrycode", "")
							+ preference.getString("number", ""));
					intent.putExtra("country_code", country_code);
					startActivity(intent);
					finish();

				} else {

					// if (Constant.restlistmain.size() > 0) {

					byte[] av = null;
					String status = "";
					String fname = null, email = null;
					try {
						VCard vc = VCardManager.getInstanceFor(
								TempConnectionService.connection).loadVCard();
						// vc.load(connection);
						// Constant.printMsg("Status::"+vc.getField("SORT-STRING"));
						status = vc.getField("SORT-STRING");
						fname = vc.getFirstName();
						email = vc.getEmailWork();
						av = new AvatarManager().saveBitemap(vc.getAvatar());
					} catch (Exception e) {
						// ACRA.getErrorReporter().handleException(e);
						e.printStackTrace();
					}

					try {
						Presence presence = new Presence(
								Presence.Type.available);
						presence.setStatus(status);
						// Send the packet (assume we have a Connection
						// instance called "con").
						TempConnectionService.connection.sendStanza(presence);

					} catch (Exception e) {
						// ACRA.getErrorReporter().handleException(e);
						e.printStackTrace();
					}
					if (Constant.ifSecondary) {

					} else {
						Toast.makeText(getApplicationContext(), full_mobile_no,
								Toast.LENGTH_SHORT).show();
						Constant.printMsg("test:::::>>>>>>>>>>>"
								+ full_mobile_no);
						dbAdapter.insertLogin(
								full_mobile_no,
								Constant.pass,
								getResources().getString(
										R.string.hey_im_usning_niftycha),
								Constant.first, Constant.manualmail,
								Constant.byteimage);
					}

					// Intent intent1 = new Intent(OtpVerification.this,
					// Contact_server.class);
					// startService(intent1);
					Intent intent1 = new Intent(OtpVerification.this,
							ContactLastSync.class);
					startService(intent1);
					// mToast.cancel();
					// ed.putString("pin", "0");
					// ed.commit();
					ed = preference.edit();
					ed.putInt("nofreebie", 1);
					ed.commit();
					Constant.printMsg("nofreebie :::>>>>   "
							+ preference.getInt("nofreebie", 0));
//					Intent intent = new Intent(OtpVerification.this,
//							SliderTesting.class);
//					intent.putExtra("mobileno", sp.getString("countrycode", "")
//							+ sp.getString("number", ""));
//					intent.putExtra("country_code", country_code);
//					startActivity(intent);
//					finish();
//siva
					// } else {
					//
					// Toast.makeText(getApplicationContext(),
					// "Network Error!Please try again later!",
					// Toast.LENGTH_SHORT).show();
					//
				}

			} else {
				Toast.makeText(getApplicationContext(),
						"Network Error!Please try again later!",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	public class postontact extends AsyncTask<String, String, String> {

		protected void onPreExecute() {
			// TODO Auto-generated method stub

			super.onPreExecute();

		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = null;
			HttpConfig ht = new HttpConfig();

			Constant.printMsg("siva result dis comtactbefore"
					+ WebConfig.Contact_URL + "..." + data1);

			result = ht.doPostMobizee(data1, WebConfig.Contact_URL);

			Constant.printMsg("siva result dis comtact" + result);

			return result;
		}

		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result != null && result.length() > 0) {
				if (commonMethods.isJSONValid(result.trim())) {
					Gson g = new Gson();
					ContactResponseDto contactReponse = g.fromJson(
							result.trim(), ContactResponseDto.class);
					if (contactReponse.getContactListDtos() != null
							&& contactReponse.getContactListDtos().size() > 0) {
						// handleSyncContactResponse(contactReponse
						// .getContactListDtos());

					}
				}
				Constant.printMsg("siva contact result res:;" + result);
			} else {
				// Toast.makeText(getApplicationContext(),
				// "Network Error!Please try again later!",
				// Toast.LENGTH_SHORT).show();
			}

		}
	}

	// public void handleSyncContactResponse(List<ContactSyncDto>
	// contactReponse) {
	// // TODO Auto-generated method stub
	// for (int i = 0; i < contactReponse.size(); i++) {
	// Constant.printMsg("siva check contact from name,.,.,.,.,.,"
	// + contactReponse.get(i).getPrimaryContactNumber());
	// if (contactReponse.get(i).getPrimaryContactNumber() != null
	// && !contactReponse.get(i).getPrimaryContactNumber()
	// .equalsIgnoreCase("null")) {
	// String alphaAndDigitsPrimary = contactReponse.get(i)
	// .getPrimaryContactNumber();
	// String alphaAndDigitsSecondary = contactReponse.get(i)
	// .getSecondaryContactNumbers();
	// String contactName = contactReponse.get(i).getName();
	// int row_contact_id = Integer.valueOf(contactReponse.get(i)
	// .getuId());
	// VCard vc = new VCard();
	// if (!dbAdapter.isjidExist(alphaAndDigitsPrimary
	// + NiftyApplication.getHost())) {
	// Log.d(TAG, "Contact not exist");
	// try {
	//
	// try {
	//
	// vc = VCardManager.getInstanceFor(connection)
	// .loadVCard(
	// alphaAndDigitsPrimary
	// + NiftyApplication
	// .getHost());
	// } catch (Exception e) {
	// // TODO: handle exception
	// vc = new VCard();
	// }
	//
	// if (vc.getJabberId() != null) {
	// // is_UserEXist = false;
	// Log.d("Vcard", "Vcard Called..." + vc.getJabberId()
	// + " Name::" + vc.getFirstName());
	//
	// contactsGetSet contects = new contactsGetSet();
	// contects.setDisplay_name(contactName);
	// contects.setIs_niftychat_user(1);
	// contects.setJid(vc.getJabberId());
	// contects.setNumber(alphaAndDigitsSecondary);
	// contects.setPhone_label("");
	// contects.setRaw_contact_id("" + row_contact_id);
	// contects.setPhone_type("");
	// contects.setUnseen_msg_count(0);
	// contects.setNifty_email(vc.getEmailWork());
	// // Roster roster1=Roster.getInstanceFor(connection);
	//
	// try {
	//
	// contects.setStatus(vc.getField("SORT-STRING"));
	// } catch (Exception e) {
	// // ACRA.getErrorReporter().handleException(e);
	// e.printStackTrace();
	// }
	//
	// AvatarManager av = new AvatarManager();
	// contects.setPhoto_ts(new AvatarManager()
	// .saveBitemap(vc.getAvatar()));
	// if (!contects.getJid().equals(
	// NiftyApplication.getUserID()
	// + NiftyApplication.getHost())) {
	// if (Constant.ref == true) {
	// dbAdapter.insertContactsFavorites(contects);
	// } else {
	// dbAdapter.insertContacts(contects);
	// }
	// }
	//
	// // if (!user_rosters.contains(vc.getJabberId())) {
	// // Log.d(TAG, "Subscribed user::" +
	// // vc.getJabberId());
	// // Presence subscribed = new Presence(
	// // Presence.Type.subscribe);
	// // subscribed.setTo(vc.getJabberId());
	// // connection.sendPacket(subscribed);
	// // Roster roster =
	// // Roster.getInstanceFor(connection);
	// // roster.setSubscriptionMode(SubscriptionMode.accept_all);
	// // roster.createEntry(vc.getJabberId(),
	// // vc.getJabberId(), null);
	// //
	// // RosterExchangeManager rem = new
	// // RosterExchangeManager(
	// // connection);
	// // rem.send(roster, vc.getJabberId());
	// //
	// // }
	//
	// } else {
	// contactsGetSet contects = new contactsGetSet();
	// contects.setDisplay_name(contactName);
	// contects.setIs_niftychat_user(0);
	// contects.setJid(alphaAndDigitsSecondary
	// + NiftyApplication.getHost());
	// contects.setNumber(alphaAndDigitsSecondary);
	// contects.setPhone_label("");
	// contects.setRaw_contact_id("" + row_contact_id);
	// contects.setPhone_type("");
	// contects.setUnseen_msg_count(0);
	//
	// if (!contects.getJid().equals(
	// NiftyApplication.getUserID()
	// + NiftyApplication.getHost())) {
	// if (Constant.ref == true) {
	// dbAdapter.insertContactsFavorites(contects);
	// } else {
	// dbAdapter.insertContacts(contects);
	// }
	// }
	// Constant.printMsg("User Not Exist::"
	// + alphaAndDigitsSecondary);
	// }
	// } catch (Exception e) {
	// // ACRA.getErrorReporter().handleException(e);
	// e.printStackTrace();
	// // TODO: handle exception
	// }
	//
	// } else {
	//
	// try {
	// dbAdapter.setUpdateContact_onob(alphaAndDigitsSecondary
	// + NiftyApplication.getHost(), contactName,
	// alphaAndDigitsSecondary);
	// } catch (Exception e) {
	// // ACRA.getErrorReporter().handleException(e);
	// // TODO: handle exception
	// }
	// }
	// }
	//
	// }
	// }

	public void insertDB(ContentValues v) {

		try {
			int a = (int) dbHelper.open().getDatabaseObj()
					.insert(Dbhelper.TABLE_CART, null, v);

			Constant
					.prtMsg("No of inserted rows in shop details :::::::::" + a);
		} catch (SQLException e) {
			Constant.printMsg("Sql exception in new shop details ::::::"
					+ e.toString());
		} finally {
			dbHelper.close();
		}

	}

	public void insertDBBux(ContentValues v) {

		try {
			int a = (int) dbHelper.open().getDatabaseObj()
					.insert(Dbhelper.TABLE_BUX, null, v);

			Constant
					.prtMsg("No of inserted rows in shop details :::::::::" + a);
		} catch (SQLException e) {
			Constant.printMsg("Sql exception in new shop details ::::::"
					+ e.toString());
		} finally {
			dbHelper.close();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		unregisterReceiver(sms_broadcast);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();
		filter.addAction("sms");
		registerReceiver(sms_broadcast, filter);
		super.onResume();
	}

	BroadcastReceiver sms_broadcast = new BroadcastReceiver() {

		private int NOTIFICATION_ID = 0;
		public byte[] IMAGE;
		DatabaseHelper dbAdapter;

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals("sms")) {

				pin = intent.getExtras().getString("pin");
				mVerificationCode.setText(pin);
			}
		}
	};

	public void insertDBFreeBie(ContentValues v) {

		try {
			int a = (int) dbHelper.open().getDatabaseObj()
					.insert(Dbhelper.TABLE_FREEBIE, null, v);

			Constant
					.prtMsg("No of inserted rows in shop details :::::::::" + a);
		} catch (SQLException e) {
			Constant.printMsg("Sql exception in new shop details ::::::"
					+ e.toString());
		} finally {
			dbHelper.close();
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent ii = new Intent(OtpVerification.this, Signin.class);
		startActivity(ii);
		finish();
	}
	private void screenArrangeOtpVerification() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;

		LinearLayout.LayoutParams msgTopParama = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		msgTopParama.width = (int) (width * 85 / 100);
		msgTopParama.gravity = Gravity.CENTER;
		msgTopParama.topMargin=height*13/100;
		mMsgTop.setLayoutParams(msgTopParama);
		mMsgTop.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams verificationParama = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		verificationParama.height = (int) (height * 7 / 100);
		verificationParama.width = (int) (width * 85 / 100);
		verificationParama.topMargin=height*3/100;
		verificationParama.gravity = Gravity.CENTER;
		mVerificationCode.setLayoutParams(verificationParama);
		mVerificationCode.setGravity(Gravity.CENTER|Gravity.LEFT);
		mVerificationCode.setPadding(width*2/100,0,0,0);

		LinearLayout.LayoutParams sendLayoutParama = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		sendLayoutParama.height = (int) (height * 7 / 100);
		sendLayoutParama.width = (int) (width * 85 / 100);
		sendLayoutParama.topMargin=height*5/100;
		sendLayoutParama.gravity = Gravity.CENTER;
		mNextLayout.setLayoutParams(sendLayoutParama);
		mNextLayout.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams resendParama = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		resendParama.width = (int) (width * 30 / 100);
		resendParama.height = (int) (height * 7 / 100);
		resendParama.gravity = Gravity.CENTER;
		mReSend.setLayoutParams(resendParama);
		mReSend.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams nextParama = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		nextParama.height = (int) (height * 7 / 100);
		nextParama.width = (int) (width * 30 / 100);
		nextParama.gravity = Gravity.CENTER;
		nextParama.leftMargin=width*3/100;
		mNextButton.setLayoutParams(nextParama);
		mNextButton.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams msgBottomParama = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		msgBottomParama.width = (int) (width * 85 / 100);
		msgBottomParama.gravity = Gravity.CENTER|Gravity.BOTTOM;
		msgBottomParama.topMargin=height*5/100;
		mMsgBottom.setLayoutParams(msgBottomParama);
		mMsgBottom.setGravity(Gravity.CENTER);

		if (width >= 600) {
			mMsgTop.setTextSize(16);
			mReSend.setTextSize(16);
			mVerificationCode.setTextSize(16);
			mNextButton.setTextSize(16);
			mMsgBottom.setTextSize(16);
		} else if (width > 501 && width < 600) {
			mMsgTop.setTextSize(15);
			mReSend.setTextSize(15);
			mVerificationCode.setTextSize(15);
			mNextButton.setTextSize(15);
			mMsgBottom.setTextSize(15);
		} else if (width > 260 && width < 500) {
			mMsgTop.setTextSize(14);
			mReSend.setTextSize(14);
			mVerificationCode.setTextSize(14);
			mNextButton.setTextSize(14);
			mMsgBottom.setTextSize(14);
		} else if (width <= 260) {
			mMsgTop.setTextSize(13);
			mReSend.setTextSize(13);
			mVerificationCode.setTextSize(13);
			mNextButton.setTextSize(13);
			mMsgBottom.setTextSize(13);
		}
	}

}
