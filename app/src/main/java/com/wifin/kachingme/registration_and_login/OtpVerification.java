package com.wifin.kachingme.registration_and_login;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
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
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.async_tasks.ConcurrentAsyncTaskExecutor;
import com.wifin.kachingme.bradcast_recivers.GlobalBroadcast;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.CartDetailsDto;
import com.wifin.kachingme.pojo.ContactPost;
import com.wifin.kachingme.pojo.ContactResponseDto;
import com.wifin.kachingme.pojo.FreeBieRequest;
import com.wifin.kachingme.pojo.FreebieDto;
import com.wifin.kachingme.pojo.FreebieMainDto;
import com.wifin.kachingme.pojo.LoginGetSet;
import com.wifin.kachingme.pojo.LoginPostPojo;
import com.wifin.kachingme.pojo.LoginResponseDto;
import com.wifin.kachingme.pojo.ProfileUpdateDto;
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
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.LastModifiedFileComparator;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

//import cz.msebera.android.httpclient.Header;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;

public class OtpVerification extends Slideshow implements View.OnClickListener {

    public static Handler mHandelSininMsg;
    TextView mMsgTop, mMsgBottom, mReSend, mNextButton;
    EditText mVerificationCode;
    LinearLayout mNextLayout;
    String mobileno;
    String TAG = OtpVerification.class.getSimpleName();
    String randome_no = null, newOtp = null;
    String pin, data, data1, loginPostData;
    BroadcastReceiver sms_broadcast = new BroadcastReceiver() {

        public byte[] IMAGE;
        DatabaseHelper dbAdapter;
        private int NOTIFICATION_ID = 0;

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("sms")) {

                pin = intent.getExtras().getString("pin");
                mVerificationCode.setText(pin);
            }
        }
    };
    String country_code, full_mobile_no;
    String SMS_GATEWAY_URL = "";
    GPSTrackerUtils gpsTracker;
    Geocoder geocoder;
    double latitude, longitude;
    String stringLatitude, stringLongitude;
    StatusLine resultst;
    Dbhelper dbHelper;
    SharedPreferences preferenceses, preference;
    DatabaseHelper dbAdapter;
    Editor ed;
    CommonMethods commonMethods;
    boolean isLogintry = false;
    ProgressDialog progressRegistrationDialog, progressLoginDialog, progressSecondaryDialog;
    public static OtpVerification myActivity;
    Handler callLogHandler = new Handler();
    Runnable callRunnable;

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

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
        myActivity = OtpVerification.this;
        initialization();
        screenArrangeOtpVerification();
        commonMethods = new CommonMethods(OtpVerification.this);
        sIndicator.setVisibility(View.GONE);
        sLoginImage.setVisibility(View.GONE);
        sPager.setVisibility(View.GONE);
        preferenceses = PreferenceManager.getDefaultSharedPreferences(this);
        preference = getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        dbHelper = new Dbhelper(getApplicationContext());
        dbAdapter = KachingMeApplication.getDatabaseAdapter();
        ed = preferenceses.edit();
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
        if (Constant.mVerifiedNum != null && !Constant.mVerifiedNum.equalsIgnoreCase("null")
                && !Constant.mVerifiedNum.isEmpty()) {
            mMsgTop.setText(Html.fromHtml(String.format(
                    getResources().getString(R.string.we_sent_sms),
                    String.valueOf(Constant.mVerifiedNum))));
        } else {
            mMsgTop.setText(Html.fromHtml(String.format(
                    getResources().getString(R.string.we_sent_sms),
                    String.valueOf(Constant.mVerifiedNum))));
        }
        mVerificationCode.setText("");
        Constant.printMsg("siva test otp....login.."+Constant.loginOtp);
        Constant.printMsg("siva test otp....Registarion.."+Constant.Otp);

        if (Constant.loginOtp != null && !Constant.loginOtp.equalsIgnoreCase("null")
                && !Constant.loginOtp.isEmpty()) {
            randome_no = Constant.loginOtp;
        } else {
            if (Constant.Otp != null && !Constant.Otp.equalsIgnoreCase("null")
                    && !Constant.Otp.isEmpty()) {
                randome_no = Constant.Otp;
            }
        }
        callRunnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(OtpVerification.this,
                        "Enter First Five Digit Number Of Missed Call", Toast.LENGTH_SHORT).show();
                //commonMethods.showAlertDialog(OtpVerification.this,
                 //       "Enter First Five Digit Number Of Missed Call", true);
            }
        };
        callLogHandler.postDelayed(callRunnable, 15000);

        mHandelSininMsg = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Constant.printMsg("siva check msg 1............." + msg.arg1);
                Constant.printMsg("siva check msg 2............." + msg.arg2);
                int sentInt = msg.getData().getInt("what");
                Constant.printMsg("siva check msg 3............." + sentInt);
                createAndLoginProcess(sentInt);
            }
        };
//        Log.d(TAG, "Randome No::" + randome_no);
//        RequestParams request_params = new RequestParams();
//        AsyncHttpClient client = new AsyncHttpClient();
//        Constant.printMsg("mobjkjkfd::" + mobileno);
        // http://198.24.149.4/API/pushsms.aspx?loginID=velkesh&password=123456&mobile=9943933949&text=hai&senderid=wifint&route_id=2&Unicode=0"
//		String mOTPMessege = "http://198.24.149.4/API/pushsms.aspx?loginID=velkesh&password=123456&mobile="
//				+ URLEncoder.encode(Constant.otpnumner)
//				+ "&text="
//				+ URLEncoder.encode("Kaching.me OTP is" + randome_no)
//				+ "&senderid=wifint&route_id=2&Unicode=0";
        // ServiceClass.getServerCall(mOTPMessege);
//
//        client.post(SMS_GATEWAY_URL, request_params,
//                new AsyncHttpResponseHandler(Looper.getMainLooper()) {
//
//                    @Override
//                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//                                          Throwable arg3) {
//                        // TODO Auto-generated method stub
//                    }
//
//                    @Override
//                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//                        // TODO Auto-generated method stub
//                        Log.d(TAG, "SMS Service Success ");
//                    }
//                });
    }

    private void initialization() {
        mMsgTop = (TextView) findViewById(R.id.otp_topMessage);
        mVerificationCode = (EditText) findViewById(R.id.otp_verification_code);
        mMsgBottom = (TextView) findViewById(R.id.otp_bottomText);
        mReSend = (TextView) findViewById(R.id.otp_resend);
        mNextButton = (TextView) findViewById(R.id.otp_next);
        mNextLayout = (LinearLayout) findViewById(R.id.otp_nextLayout);

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

    public static void otpConfirmation(String newOtp) {
        if (Constant.loginOtp != null && !Constant.loginOtp.equalsIgnoreCase("null")
                && !Constant.loginOtp.isEmpty()) {
            if (newOtp != null && !newOtp.isEmpty()) {
                Constant.isOtpVerification=false;
                Constant.printMsg("Otpverification check data........" + newOtp + ".." + Constant.loginOtp);
                if (myActivity.callLogHandler != null && myActivity.callRunnable != null)
                    myActivity.callLogHandler.removeCallbacks(myActivity.callRunnable);
                myActivity.mVerificationCode.setText(Constant.loginOtp);
                myActivity.nextSubmitProcess();
            }
        } else {
            if (Constant.Otp != null && !Constant.Otp.equalsIgnoreCase("null")
                    && !Constant.Otp.isEmpty()) {
                if (newOtp != null && !newOtp.isEmpty()) {
                    Constant.isOtpVerification=false;
                    Constant.printMsg("Otpverification check data........" + newOtp + ".." + Constant.Otp);
                    if (myActivity.callLogHandler != null && myActivity.callRunnable != null)
                        myActivity.callLogHandler.removeCallbacks(myActivity.callRunnable);
                    myActivity.mVerificationCode.setText(Constant.Otp);
                    myActivity.nextSubmitProcess();
                }
            }
        }
    }

    private String getMissedCallLog(String otp) {
//        Cursor managedCursor = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
//                null, null, null);
        String data = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "TODO";
        }
        Cursor managedCursor = getContentResolver().query(
                CallLog.Calls.CONTENT_URI, null, null,
                null, null);

        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int i = 0;
        while (managedCursor.moveToNext()) {
            i++;
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
//            switch (dircode) {
//                case CallLog.Calls.OUTGOING_TYPE:
//                    dir = "OUTGOING";
//                    break;
//
//                case CallLog.Calls.INCOMING_TYPE:
//                    dir = "INCOMING";
//                    break;
//
//                case CallLog.Calls.MISSED_TYPE:
//                    dir = "MISSED";
//                    break;
//            }
//            sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
//                    + dir + " \nCall Date:--- " + callDayTime
//                    + " \nCall duration in sec :--- " + callDuration);
//            sb.append("\n----------------------------------");
            Constant.printMsg("return before as,,.,.,...." + phNumber + "...." + phNumber.replaceAll("[-+.^:,]", ""));
            String lastPhoneNumber = phNumber.replaceAll("[-+.^:,]", "").substring(0, 5);
            Constant.printMsg("return data as,,.,.,...." + lastPhoneNumber + "..." + otp);
            if (lastPhoneNumber.equals(otp)) {
                //data=lastPhoneNumber;
                return lastPhoneNumber;
            }
            if (i == 5)
                break;
        }
        managedCursor.close();
        return data;
    }

    public String gen5DigitNumber() {
        Random rng = new Random();
        int val = rng.nextInt(100000);
        return String.format("%05d", val);
    }

    public void re_send() {
        Toast.makeText(OtpVerification.this,
                "Please try again later!!", Toast.LENGTH_SHORT).show();
//        RequestParams request_params = new RequestParams();
//        AsyncHttpClient client = new AsyncHttpClient();
//        String mOTPMessege = "http://198.24.149.4/API/pushsms.aspx?loginID=velkesh&password=123456&mobile="
//                + URLEncoder.encode(mobileno)
//                + "&text="
//                + URLEncoder.encode("Kaching.me verification code "
//                + randome_no) + "&senderid=wifint&route_id=2&Unicode=0";
//        client.post(SMS_GATEWAY_URL, request_params,
//                new AsyncHttpResponseHandler(Looper.getMainLooper()) {
//                    @Override
//                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//                                          Throwable arg3) {
//                        // TODO Auto-generated method stub
//                        Log.d(TAG, "SMS Service Failure");
//                    }
//
//                    @Override
//                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//                        // TODO Auto-generated method stub
//                        Log.d(TAG, "SMS Service Success");
//                    }
//
//                });
    }

    private void nextSubmitProcess() {
        if (mVerificationCode.getText().toString().isEmpty()) {
            Toast.makeText(OtpVerification.this,
                    "Field Cannot be Empty", Toast.LENGTH_SHORT).show();
        } else {
            if (mVerificationCode.getText().toString().trim()
                    .equals(randome_no)) {
                if (Connectivity.isConnected(OtpVerification.this)) {
                    stringLatitude = String.valueOf(latitude);
                    stringLongitude = String.valueOf(longitude);
                    if (Constant.loginOtp != null && !Constant.loginOtp.equalsIgnoreCase("null")
                            && !Constant.loginOtp.isEmpty()) {
                        loginPostData = jsonFormLoginOtp();
                        progressLoginDialog = new ProgressDialog(OtpVerification.this,
                                AlertDialog.THEME_HOLO_LIGHT);
                        progressLoginDialog.setMessage("Please Wait...");
                        progressLoginDialog.setProgressDrawable(new ColorDrawable(
                                android.graphics.Color.BLUE));
                        progressLoginDialog.setCancelable(false);
                        progressLoginDialog.show();
                        //new postLoginOtp().execute();
                        ConcurrentAsyncTaskExecutor.executeConcurrently(new postLoginOtp());
                        commonMethods.stopAsyncTask(new postLoginOtp(), progressLoginDialog);
                    } else {
                        if (!Constant.addverification) {
                            data = jsonForm();
                            progressRegistrationDialog = ProgressDialog.show(OtpVerification.this,
                                    getResources().getString(R.string.please_wait),
                                    getResources().getString(R.string.loading), true);
                            progressRegistrationDialog.show();
                            editor.putString("loginSucess", "Success");
                            editor.commit();
                            //new postPrimaryOtp().execute();
                            ConcurrentAsyncTaskExecutor.executeConcurrently(new postPrimaryOtp());
                            commonMethods.stopAsyncTask(new postPrimaryOtp(), progressRegistrationDialog);
                            Editor e1 = preferenceses.edit();
                            e1.putInt("regpoints", 1);
                            e1.commit();

                        } else {
                            Constant.addverification = false;
                            data = responseJson();
                            progressSecondaryDialog = new ProgressDialog(OtpVerification.this,
                                    AlertDialog.THEME_HOLO_LIGHT);
                            progressSecondaryDialog.setMessage("Please Wait...");
                            progressSecondaryDialog.setProgressDrawable(new ColorDrawable(
                                    android.graphics.Color.BLUE));
                            progressSecondaryDialog.setCancelable(false);
                            progressSecondaryDialog.show();
                            //new postSecondaryOtp().execute();
                            ConcurrentAsyncTaskExecutor.executeConcurrently(new postSecondaryOtp());
                            commonMethods.stopAsyncTask(new postSecondaryOtp(), progressSecondaryDialog);
                            int count1 = preferenceses.getInt("added_num", 0);
                            int count = count1 + 1;
                        }
                    }
                } else {
                    Constant.isOtpVerification=true;
                    Toast.makeText(OtpVerification.this,
                            "Please check your network connection",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                commonMethods.showAlertDialog(this, getResources()
                        .getString(R.string.sorry_you_have_entered_wrong_pin), true);
            }
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
        Editor e1 = preferenceses.edit();
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

    public String jsonFormLoginOtp() {
        String data;
        String regId = preference.getString("regId", null);
        LoginPostPojo loginPostPojo = new LoginPostPojo();
        if (Constant.Imei_no != null) {
            loginPostPojo.setImei(Constant.Imei_no);
        } else {
            loginPostPojo.setImei("");
        }
        loginPostPojo.setLatitude(stringLatitude);
        loginPostPojo.setLongitude(stringLongitude);
        loginPostPojo.setOtp(Constant.loginOtp);
        if (Constant.loginMobileNumber != null && !Constant.loginMobileNumber.equalsIgnoreCase("null")
                && !Constant.loginMobileNumber.isEmpty()) {
            loginPostPojo.setUserName(Constant.loginMobileNumber);
        } else {
            loginPostPojo.setUserName(preference.getString("MyPrimaryNumber", ""));
        }
        if (regId != null && !regId.isEmpty()) {
            loginPostPojo.setNotificationToken(regId);
        } else {
            loginPostPojo.setNotificationToken("");
        }
        data = new Gson().toJson(loginPostPojo);
        Constant.printMsg("otp login data  ::::" + data);
        return data;
    }

    private void createAndLoginProcess(int sentInt) {
        if (sentInt == 1) {
            new createUserVcard().execute();
        } else {
            new MySync().execute();
        }
    }

    public void insertNumber(ContentValues cv) {
        // TODO Auto-generated method stub
        Constant.printMsg("login secondary ....insert :::::::::" + cv);
        try {
            int a = (int) dbHelper.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_NUMBERS, null, cv);
            Constant.printMsg("login primary....No of inserted rows in kons :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("login primary....Sql exception in kons details ::::::"
                    + e.toString());
        } finally {
            dbHelper.close();
        }
    }

    public void insertDB(ContentValues v) {

        try {
            int a = (int) dbHelper.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_CART, null, v);

            Constant.printMsg("No of inserted rows in shop details :::::::::" + a);
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

            Constant.printMsg("No of inserted rows in shop details :::::::::" + a);
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

    public void insertDBFreeBie(ContentValues v) {

        try {
            int a = (int) dbHelper.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_FREEBIE, null, v);

            Constant.printMsg("No of inserted rows in shop details :::::::::" + a);
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
        msgTopParama.width = (int) (width * 78 / 100);
        msgTopParama.gravity = Gravity.CENTER;
        msgTopParama.topMargin = height * 7 / 100;
        mMsgTop.setLayoutParams(msgTopParama);
        mMsgTop.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams verificationParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        verificationParama.height = (int) (height * 7 / 100);
        verificationParama.width = (int) (width * 80 / 100);
        verificationParama.topMargin = height * 3 / 100;
        verificationParama.gravity = Gravity.CENTER;
        mVerificationCode.setLayoutParams(verificationParama);
        mVerificationCode.setGravity(Gravity.CENTER | Gravity.LEFT);
        mVerificationCode.setPadding(width * 2 / 100, 0, 0, 0);

        LinearLayout.LayoutParams sendLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        sendLayoutParama.height = (int) (height * 6 / 100);
        sendLayoutParama.width = (int) (width * 80 / 100);
        sendLayoutParama.topMargin = height * 4 / 100;
        sendLayoutParama.gravity = Gravity.CENTER;
        mNextLayout.setLayoutParams(sendLayoutParama);
        mNextLayout.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams resendParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        resendParama.width = (int) (width * 30 / 100);
        resendParama.height = (int) (height * 6 / 100);
        resendParama.gravity = Gravity.CENTER;
        mReSend.setLayoutParams(resendParama);
        mReSend.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams nextParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nextParama.height = (int) (height * 6 / 100);
        nextParama.width = (int) (width * 30 / 100);
        nextParama.gravity = Gravity.CENTER;
        nextParama.leftMargin = width * 3 / 100;
        mNextButton.setLayoutParams(nextParama);
        mNextButton.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams msgBottomParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        msgBottomParama.width = (int) (width * 80 / 100);
        msgBottomParama.gravity = Gravity.CENTER | Gravity.BOTTOM;
        msgBottomParama.topMargin = height * 4 / 100;
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
            mMsgTop.setTextSize(13);
            mReSend.setTextSize(13);
            mVerificationCode.setTextSize(13);
            mNextButton.setTextSize(13);
            mMsgBottom.setTextSize(13);
        } else if (width <= 260) {
            mMsgTop.setTextSize(11);
            mReSend.setTextSize(11);
            mVerificationCode.setTextSize(11);
            mNextButton.setTextSize(11);
            mMsgBottom.setTextSize(11);
        }
    }

    public class postLoginOtp extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();
            result = ht.doPostMobizee(loginPostData, KachingMeConfig.POST_LOGIN_OTP);
            Constant.printMsg("Siva Test ::>>>> data..." + loginPostData);
            Constant.printMsg("Siva Test ::>>>> result...." + result);
            return result;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Constant.printMsg("Siva Test ::>>>> " + result.toString());
            if (result != null && result.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("OTP_EXPIRED")) {
                        progressLoginDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Otp Expired.!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS")) {
                            dbAdapter.insertLogin(preference.getString("MyPrimaryNumber", ""),
                                    preference.getString("MyPassword", ""), "",
                                    "", "", null);
                            //Constant.printMsg("otp login GFSKJHLKJDSLN" + dbAdapter.getLogin().size() + "..." +
                            //       preference.getString("MyPrimaryNumber", "") + "...." + preference.getString("MyPassword", ""));
                            Constant.mIsLogin = true;
                            editor.putString("loginSucess", "failure");
                            editor.commit();
                            //new MySyncLast().execute(preference.getString("MyPrimaryNumber", ""), preference.getString("MyPassword", ""), result);
                            ConcurrentAsyncTaskExecutor.executeConcurrently(new MySyncLast(), preference.getString("MyPrimaryNumber", ""),
                                    preference.getString("MyPassword", ""), result);
                            commonMethods.stopAsyncTask(new MySyncLast(), progressLoginDialog);
                        } else {
                            progressLoginDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Login Failed.!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressLoginDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Network Error.!Please try again later!",
                            Toast.LENGTH_SHORT).show();

                }
            } else {
                progressLoginDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error.!Please try again later!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class MySync extends AsyncTask<String, String, String> {

        String password = Constant.pass;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            Constant.printMsg("ggggggggggggggggggggggggggggggggggggg" + password);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // temp.run();
            try {
                if (TempConnectionService.connection.isConnected()) {
                    Constant.printMsg("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJPPPPP inside Connection............::MySync");
                    AccountManager account = AccountManager
                            .getInstance(TempConnectionService.connection);
                    account.sensitiveOperationOverInsecureConnectionDefault(true);
                    account.sensitiveOperationOverInsecureConnection(true);
                    Log.d("Signup_3",
                            "is Create::" + account.supportsAccountCreation() + "::" + full_mobile_no);
                    Map<String, String> regAttr = new HashMap<String, String>();
                    Constant.printMsg("siva mobile number........" + full_mobile_no);

                    regAttr.put("user", full_mobile_no);
                    regAttr.put("password", password);
                    Constant.printMsg("ssss::::Signup_3" + full_mobile_no
                            + "password" + password);

                    Constant.printMsg("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJPPPPP"
                            + full_mobile_no + "   " + Constant.first + "        "
                            + Constant.manualmail + "      " + Constant.byteimage);

                    account.createAccount(full_mobile_no, password, regAttr);
                    dbAdapter.insertLogin(full_mobile_no, password, getResources()
                                    .getString(R.string.hey_im_usning_niftycha),
                            Constant.first, Constant.manualmail,
                            Constant.byteimage);

                    Constant.registrationLogin = true;
                    Constant.printMsg("ssss::::db adapter in mysync");
                    GlobalBroadcast.stopService(OtpVerification.this);
//                    stopService(new Intent(VerificationActivity.this,TempConnectionService.class));
                    startService(new Intent(OtpVerification.this, TempConnectionService.class));

                } else {
                    Constant.printMsg("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJPPPPP inside No Connection............::MySync");
                    try {
                        Constant.registrationLoginNoConnection = true;
                        GlobalBroadcast.stopService(OtpVerification.this);
//                    stopService(new Intent(VerificationActivity.this,TempConnectionService.class));
                        startService(new Intent(OtpVerification.this, TempConnectionService.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
//            try {
//                if (!TempConnectionService.connection.isConnected()) {
//                    TempConnectionService.connection.connect();
//                }
//            } catch (Exception e) {
//                // ACRA.getErrorReporter().handleException(e);
//                e.printStackTrace();
//                Log.d("Signup_3",
//                        "Error while connnecting!!!!!!" + e.toString());
//            }
//            try {
//                Log.d("Signup_3", "is Authenticate::"
//                        + TempConnectionService.connection.isAuthenticated());
//                Constant.printMsg("ssss::::do in bg::MySync");
//                AccountManager account = AccountManager
//                        .getInstance(TempConnectionService.connection);
//                account.sensitiveOperationOverInsecureConnectionDefault(true);
//                account.sensitiveOperationOverInsecureConnection(true);
//                Log.d("Signup_3",
//                        "is Create::" + account.supportsAccountCreation()
//                                + "::" + full_mobile_no);
//                Map<String, String> regAttr = new HashMap<String, String>();
//
//                Constant.printMsg("siva mobile number........"
//                        + full_mobile_no);
//
//                regAttr.put("user", full_mobile_no);
//                regAttr.put("password", password);
//                Constant.printMsg("ssss::::Signup_3" + full_mobile_no
//                        + "password" + password);
//
//                Constant.printMsg("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJPPPPP"
//                        + full_mobile_no + "   " + Constant.first + "        "
//                        + Constant.manualmail + "      " + Constant.byteimage);
//                account.createAccount(full_mobile_no, password, regAttr);
//                dbAdapter.insertLogin(full_mobile_no, password, getResources()
//                                .getString(R.string.hey_im_usning_niftycha),
//                        Constant.first, Constant.manualmail,
//                        Constant.byteimage);
//                Constant.printMsg("ssss::::db adapter in mysync");
//                // connection.disconnect();
//
//                // SASLAuthentication.supportSASLMechanism("PLAIN", 0);

//                TempConnectionService.connection.login(full_mobile_no,
//                        password, "Messnger");
//                Constant.printMsg("ssss::::loginnnn");
            /*
             * catch (XMPPException e) { e.printStackTrace(); new
			 * AlertManager().
			 * getToast(signup_3.this,"Some issues in signup please try again!!"
			 * ); connection.disconnect(); Log.d("signup_3",e.toString());
			 *
			 * }
			 */
            } catch (SmackException.NoResponseException e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO Auto-generated catch block
                e.printStackTrace();
                Constant.printMsg("ssss::::NoResponseException"
                        + e.toString());
            } catch (SmackException.NotConnectedException e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO Auto-generated catch block
                e.printStackTrace();
                Constant.printMsg("ssss::::NotConnectedException"
                        + e.toString());
            } catch (SmackException e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO Auto-generated catch block
                e.printStackTrace();
                Constant.printMsg("ssss::::SmackException" + e.toString());
//            } catch (IOException e) {
//               // ACRA.getErrorReporter().handleException(e);
//               // TODO Auto-generated catch block
//                e.printStackTrace();
//                Constant.printMsg("ssss::::IOException" + e.toString());
            } catch (XMPPException.XMPPErrorException e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO Auto-generated catch block
                e.printStackTrace();
                Constant.printMsg("ssss::::XMPPErrorException"
                        + e.toString());
            } catch (XMPPException e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO Auto-generated catch block
                e.printStackTrace();
                Constant.printMsg("ssss::::XMPPException" + e.toString());
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO Auto-generated catch block
                e.printStackTrace();
                Constant.printMsg("ssss::::XMPPException" + e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Constant.printMsg("ssss::result::" + result);
            // if (result == null || result.toString().length() == 0) {
            // progressdialog.dismiss();
            // }
//            Log.d(TAG, "Registration Post Execute");
//            if (TempConnectionService.connection.isAuthenticated()) {
//                Constant.printMsg("ssss::::post1111");
//                KachingMeApplication.setAvatar(Constant.byteimage);
//                KachingMeApplication.setNifty_name(Constant.first);
//                KachingMeApplication.setUserID(full_mobile_no);
//                KachingMeApplication.setAcra();
//                Presence presence = new Presence(Presence.Type.available);
//                presence.setStatus(getResources().getString(
//                        R.string.hey_im_usning_niftycha));
//                try {
//                    TempConnectionService.connection.sendStanza(presence);
//                } catch (NotConnectedException e) {
//                    // TODO Auto-generated catch block
//                    Constant.printMsg("");
//                    e.printStackTrace();
//                }
//                Constant.printMsg("ssss::::post2222");
//                RequestParams request_params = new RequestParams();
//                request_params.put("jid",
//                        full_mobile_no + KachingMeApplication.getHost());
//                request_params.put("email", Constant.manualmail);
//                request_params.put("password", Constant.pass);
//                AsyncHttpClient client = new AsyncHttpClient();
//                client.post(getString(R.string.webservice_host)
//                                +getString(R.string.user_regisrtation), request_params,
//                        new AsyncHttpResponseHandler(Looper.getMainLooper()) {
//
//                            @Override
//                            public void onFailure(int arg0, Header[] arg1,
//                                                  byte[] arg2, Throwable arg3) {
//                                // TODO Auto-generated method stub
//                                Constant.printMsg("ssss::::failure");
//                            }
//
//                            @Override
//                            public void onSuccess(int arg0, Header[] arg1,
//                                                  byte[] arg2) {
//                                // TODO Auto-generated method stub
//                                Constant.printMsg("ssss:::: success");
//                            }
//
//                        });
//
//				/*
//                 * Intent intent=new Intent(signup_3.this,ContactSync.class);
//				 * startActivity(intent); finish();
//				 */
//                editor.putString("pin", Constant.pass);
//                editor.commit();
//
//                Constant.printMsg("ssss::::endddddd");
//                // Intent intent1 = new Intent(VerificationActivity.this,
//                // Contact_server.class);
//                // startService(intent1);
//                Intent syncContact = new Intent(VerificationActivity.this,
//                        ContactLastSync.class);
//                VerificationActivity.this.startService(syncContact);
//                Constant.printMsg("decline::endddddd" + decline);
//                if (!decline) {
//                    Intent intent = new Intent(VerificationActivity.this,
//                            NewStartUpActivity.class);
////                    intent.putExtra("mobileno", Constant.fullmob);
////                    intent.putExtra("country_code", Constant.countrycode);
////                    startActivity(intent);
////                    finish();
//
//                    Constant.fullmob = full_mobile_no;
//                    Constant.countrycode = country_code;
//                    intent.putExtra("mobileno", full_mobile_no);
//                    intent.putExtra("country_code", country_code);
//                    startActivity(intent);
//                    finish();
//                    updatefreebieaccept();
//                } else {
//                    Intent intent = new Intent(VerificationActivity.this,
//                            SliderTesting.class);
//                    startActivity(intent);
//                    updatefreebiedecline();
//                }
//                progressdialog.dismiss();
//            }
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
    // + KachingMeApplication.getHost())) {
    // Log.d(TAG, "Contact not exist");
    // try {
    //
    // try {
    //
    // vc = VCardManager.getInstanceFor(connection)
    // .loadVCard(
    // alphaAndDigitsPrimary
    // + KachingMeApplication
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
    // KachingMeApplication.getUserID()
    // + KachingMeApplication.getHost())) {
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
    // + KachingMeApplication.getHost());
    // contects.setNumber(alphaAndDigitsSecondary);
    // contects.setPhone_label("");
    // contects.setRaw_contact_id("" + row_contact_id);
    // contects.setPhone_type("");
    // contects.setUnseen_msg_count(0);
    //
    // if (!contects.getJid().equals(
    // KachingMeApplication.getUserID()
    // + KachingMeApplication.getHost())) {
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
    // + KachingMeApplication.getHost(), contactName,
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

    public class createUserVcard extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Constant.printMsg("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJPPPPP handler......");
                VCard vc = new VCard();
                Constant.printMsg("ssss::::vc entered");
                vc.setJabberId(full_mobile_no + KachingMeApplication.getHost());
                vc.setNickName(full_mobile_no);
                vc.setField(
                        "SORT-STRING",
                        getResources().getString(R.string.hey_im_usning_niftycha));
                vc.setFirstName(Constant.first);
                if (!Constant.manualmail.equals("")) {
                    vc.setEmailWork(Constant.manualmail);
                    KachingMeApplication.setNifty_email(Constant.manualmail);
                }
                vc.setAvatar(Constant.byteimage);
                Constant.printMsg("ssss:::: img byte");
                vc.save(TempConnectionService.connection);
                Constant.printMsg("ssss::::connection");
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                Constant.printMsg("ssss:::: exce" + e);
                Log.d("signup_3", e.toString());
            }

            if (TempConnectionService.connection.isAuthenticated()) {
                Constant.printMsg("ssss::::post1111");
                KachingMeApplication.setAvatar(Constant.byteimage);
                KachingMeApplication.setNifty_name(Constant.first);
                KachingMeApplication.setUserID(full_mobile_no);
                KachingMeApplication.setAcra();
                Presence presence = new Presence(Presence.Type.available);
                presence.setStatus(getResources().getString(
                        R.string.hey_im_usning_niftycha));
                try {
                    TempConnectionService.connection.sendStanza(presence);
                } catch (SmackException.NotConnectedException e) {
                    // TODO Auto-generated catch block
                    Constant.printMsg("");
                    e.printStackTrace();
                }
                Constant.printMsg("ssss::::post2222");
                RequestParams request_params = new RequestParams();
                request_params.put("jid",
                        full_mobile_no + KachingMeApplication.getHost());
                request_params.put("email", Constant.manualmail);
                request_params.put("password", Constant.pass);
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(KachingMeConfig.REGISTRATION_PHP, request_params,
                        new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                            @Override
                            public void onFailure(int arg0, Header[] arg1,
                                                  byte[] arg2, Throwable arg3) {
                                // TODO Auto-generated method stub
                                Constant.printMsg("ssss::::failure");
                            }

                            @Override
                            public void onSuccess(int arg0, Header[] arg1,
                                                  byte[] arg2) {
                                // TODO Auto-generated method stub
                                Constant.printMsg("ssss:::: success");
                            }
                        });

                editor.putString("pin", Constant.pass);
                editor.commit();
                Constant.printMsg("ssss::::endddddd");
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Intent syncContact = new Intent(OtpVerification.this,
                    ContactLastSync.class);
            OtpVerification.this.startService(syncContact);
            progressRegistrationDialog.dismiss();
            Intent intent = new Intent(OtpVerification.this,
                    VerificationActivity.class);
            Constant.fullmob = full_mobile_no;
            Constant.countrycode = country_code;
            intent.putExtra("mobileno", preferenceses.getString("countrycode", "")
                    + preferenceses.getString("number", ""));
            intent.putExtra("country_code", country_code);
            startActivity(intent);
            ed = preferenceses.edit();
            finish();
            mHandelSininMsg.removeCallbacks(null);
        }
    }

    public class MySync_Check extends AsyncTask<String, String, String> {
        public Boolean isEmailExist = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            // super.onPreExecute();
            // connection.disconnect();
            /*
             * progressdialog = ProgressDialog.show(signup_3.this,
			 * "Please wait ...", "Loading....", true); progressdialog.show();
			 */
            Constant.printMsg("ssssssssssssssss");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                try {
                    if (!TempConnectionService.connection.isConnected()) {
                        TempConnectionService.connection.connect();
                    }
                    Constant.printMsg("sssssssssssssss"
                            + TempConnectionService.connection.isConnected() + "         "
                            + Constant.manualmail);
                } catch (Exception e) {
                    // ACRA.getErrorReporter().handleException(e);
                    e.printStackTrace();
                    Constant.printMsg("sssssss Check doIN exp"
                            + e.toString());
                    Log.d("sign", "Pre-Execute::" + e.toString());
                }
                /*
                 * if(!connection.isAuthenticated()) {
				 * connection.login(admin_username,admin_password,"Messnger"); }
				 */
                UserSearchManager search = new UserSearchManager(TempConnectionService.connection);
                Form searchForm = search.getSearchForm("vjud."
                        + TempConnectionService.connection.getServiceName());
                Form answerForm = searchForm.createAnswerForm();
                answerForm.setAnswer("email", Constant.manualmail);
                // answerForm.setAnswer("search", true);
                ReportedData data = search.getSearchResults(answerForm, "vjud."
                        + TempConnectionService.connection.getServiceName());
                Log.d("sign", "Search ending for.." + Constant.manualmail);
                if (data.getRows() != null) {
                    Log.d("sign", "Search get Result");
                    Iterator<ReportedData.Row> it = data.getRows().iterator();
                    while (it.hasNext()) {

                        ReportedData.Row row = it.next();
                        Iterator iterator = row.getValues("jid").iterator();
                        if (iterator.hasNext()) {
                            String value = iterator.next().toString();

                            // Constant.printMsg("Iteartor values......"+value);
                            if (!value.toString().equals(
                                    "admin" + KachingMeApplication.getHost())) {
                                isEmailExist = true;
                            }
                            // Log.i("Iteartor values......"," "+value);
                        }
                    }
                }
                TempConnectionService.connection.disconnect();
            } catch (Exception e) {
                // TODO: handle exception
                // ACRA.getErrorReporter().handleException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Constant.printMsg("ssssssssss ::::::>>>>>>>> " + result);
            // progressdialog.dismiss();
            if (!isEmailExist) {
                new MySync().execute();
            } else {
                commonMethods.showAlertDialog(OtpVerification.this,
                        getResources().getString(R.string.email_already_exist), true);
                progressRegistrationDialog.cancel();
            }
            super.onPostExecute(result);
        }
    }

    public class MySyncLast extends AsyncTask<String, String, String> {
        //ProgressDialog progressdialog;
        String primaryNumber, password, result;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            // progressdialog = ProgressDialog.show(OtpVerification.this, getResources()
            //               .getString(R.string.please_wait),
            //     getResources().getString(R.string.loading), false);
            //progressdialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Constant.printMsg("login inside doInBackground ");
            primaryNumber = params[0];
            password = params[1];
            result = params[2];
            try {
                if (TempConnectionService.connection.isConnected()) {
                    Constant.printMsg("login inside connected ");
                    Constant.printMsg("siva login send data"
                            + primaryNumber + "..." + password);
                    TempConnectionService.connection.login(primaryNumber,
                            password, "Messnger");
                    isLogintry = true;
                } else {
                    Constant.printMsg("login inside not connected ");
                    TempConnectionService.connection.connect();
                }
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                Constant.printMsg("login inside Exception... " + e);
            }
            try {
                if (!isLogintry) {
                    if (TempConnectionService.connection.isConnected()) {
                        Constant.printMsg("login inside second time login try... ");
                        TempConnectionService.connection.login(primaryNumber,
                                password, "Messnger");
                        isLogintry = true;
                    }
                }
            } catch (XMPPException e) {
                Constant.printMsg("login inside second login XMPPException... " + e);
                e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
                Constant.printMsg("login inside second login SmackException... " + e);
            } catch (IOException e) {
                e.printStackTrace();
                Constant.printMsg("login inside second login IOException... " + e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Constant.printMsg("Login final response............." + result);
            editor.putString("pin", password);
            editor.putString(Constant.COUNTRY_CODE_LABEL, Constant.loginCountryCode);
            editor.commit();
            if (Signin.isUserExist) {
                if (isLogintry) {
                    if (TempConnectionService.connection.isAuthenticated()) {
                        editor.putString("loginSucess", "Success");
                        editor.commit();
                        Constant.loginOtp = null;
                        Constant.printMsg("login inside final  login process... ");
                        isLogintry = false;
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            RestUserDetailsDto cartlocalmail = new RestUserDetailsDto();
                            List<CartDetailsDto> cartlocal = new ArrayList<CartDetailsDto>();
                            Long bx = jsonObject.getLong("bux");
                            Long usid = jsonObject.getLong("userId");
                            boolean flag = jsonObject.getBoolean("freeBeeFlag");
                            String userName = jsonObject.getString("userName");
                            String email = jsonObject.getString("userEmail");
                            String imageUrl = jsonObject.getString("userImageUrl");
                            String status = jsonObject.getString("userStatus");
                            byte[] imageByte = null;

                            Constant.bux = bx;
                            Constant.userId = usid;
                            Constant.freebieflag = flag;
                            cartlocalmail.setBux(jsonObject.getLong("bux"));
                            cartlocalmail.setUserId(jsonObject.getLong("userId"));
                            cartlocalmail.setFreeBeeFlag(jsonObject.getBoolean("freeBeeFlag"));

                            Constant.printMsg("login email::" + email);
                            Constant.printMsg("login FirstName()::" + userName);
                            Constant.printMsg("login Status::" + status);
                            Constant.printMsg("login image::" + imageUrl);
                            CommonMethods commonMethods = new CommonMethods(OtpVerification.this);
                            imageByte = new AvatarManager().saveBitemap(commonMethods.getImagefromUrl(imageUrl));
                            try {
                                Presence presence = new Presence(
                                        Presence.Type.available);
                                presence.setStatus(status);
                                // Send the packet (assume we have a Connection instance called "con").
                                TempConnectionService.connection.sendStanza(presence);
                            } catch (Exception e) {
                                // ACRA.getErrorReporter().handleException(e);
                                e.printStackTrace();
                            }

                            KachingMeApplication.setUserID(preference.getString("MyPrimaryNumber", ""));
                            KachingMeApplication.setNifty_name(userName);
                            KachingMeApplication.setNifty_email(email);
                            KachingMeApplication.setAvatar(imageByte);
                            KachingMeApplication.setAcra();
                            KachingMeApplication.setStatus(status);
                            LoginGetSet loginDataUpdate = new LoginGetSet();
                            loginDataUpdate.setNifty_email(email);
                            loginDataUpdate.setNifty_name(userName);
                            loginDataUpdate.setAvatar(imageByte);
                            loginDataUpdate.setStatus(status);
                            loginDataUpdate.setUserName(preference.getString("MyPrimaryNumber", ""));
                            dbAdapter.setUpdateLogin(loginDataUpdate);

                            JSONArray jsonCart = jsonObject.getJSONArray("cartDetailsDtos");
                            Constant.printMsg("jar:;" + jsonObject + "   " + jsonCart.length()
                                    + "  " + jsonObject.length());
                            for (int j = 0; j < jsonCart.length(); j++) {
                                JSONObject jObject4 = jsonCart.getJSONObject(j);
                                CartDetailsDto m = new CartDetailsDto();
                                // m.setUserId(jObject4.getLong("userId"));
                                m.setCartDetailsId(jObject4.getLong("cartDetailsId"));
                                // m.setPhoneNumber(jObject4.getString("phoneNumber"));
                                m.setOfferId(jObject4.getLong("offerId"));
                                m.setType(jObject4.getInt("type"));
                                m.setDescription(jObject4.getString("description"));
                                m.setPhotoPath(jObject4.getString("photoPath"));
                                m.setBux(jObject4.getLong("bux"));
                                m.setQrCodePath(jObject4.getString("qrCodePath"));
                                // m.setIsDelete(jObject4.getInt("isDelete"));
                                m.setMerchantId(jObject4.getString("merchantId"));
                                m.setCompanyLogoPath(jObject4
                                        .getString("companyLogoPath"));
                                m.setMerchantName(jObject4.getString("websiteName"));
                                m.setDiscountPrice(jObject4.getString("discountPrice"));
                                m.setOfferPrice(jObject4.getString("offerPrice"));
                                m.setValidity(jObject4.getString("valid_until"));
                                Constant.printMsg("list Dtyo::" + m.getPhotoPath());
                                cartlocal.add(m);
                                Editor e = preference.edit();
                                e.putString("ExpiryDate", jObject4.getString("valid_until"));
                                e.commit();
                                ContentValues cv = new ContentValues();
                                cv.put("phonenumber", jObject4.getString("phoneNumber"));
                                cv.put("deelid", jObject4.getLong("offerId"));
                                cv.put("type", jObject4.getInt("type"));
                                cv.put("desc", jObject4.getString("description"));
                                cv.put("photopath", jObject4.getString("photoPath"));
                                cv.put("bux", jObject4.getLong("bux"));
                                cv.put("qrpath", jObject4.getString("qrCodePath"));
                                cv.put("merchantid", jObject4.getString("merchantId"));
                                cv.put("merchantimagepath",
                                        jObject4.getString("companyLogoPath"));
                                //cv.put("merchantname",
                                //       jObject4.getString("merchantName"));
                                cv.put("discount", jObject4.getString("discountPrice"));
                                cv.put("offer", jObject4.getString("offerPrice"));
                                cv.put("merchantname",
                                        jObject4.getString("websiteName"));
                                cv.put("companyname", jObject4.getString("companyName"));
                                cv.put("item", jObject4.getString("description"));
                                cv.put("prodname", jObject4.getString("freebeeName"));
                                cv.put("validity", jObject4.getString("valid_until"));
                                insertDB(cv);
                            }
                            cartlocalmail.setCartDetailsDtos(cartlocal);
                            Constant.restlistmain.add(cartlocalmail);

                            JSONArray jsonSecondaryNumber = new JSONArray(jsonObject.get("secondaryContactNoList").toString());
                            Constant.printMsg("login inside final  jsonSecondaryNumber process... " + jsonSecondaryNumber.toString());
                            if (jsonSecondaryNumber != null && jsonSecondaryNumber.length() > 0) {
                                for (int i = 0; i < jsonSecondaryNumber.length(); i++) {
                                    if (!preference.getString("MyPrimaryNumber", "").equalsIgnoreCase(jsonSecondaryNumber.get(i).toString())) {
                                        Constant.printMsg("get secondary list........." + jsonSecondaryNumber.get(i));
                                        ContentValues cv = new ContentValues();
                                        cv.put("primarynum", preference.getString("MyPrimaryNumber", ""));
                                        cv.put("secondarynum", jsonSecondaryNumber.get(i).toString());
                                        insertNumber(cv);
                                    }
                                }
                            }
                            if (Constant.bux != null) {
                                ContentValues cv = new ContentValues();
                                cv.put("bux", Constant.bux);
                                cv.put("phonenumber", Constant.userId);
                                insertDBBux(cv);
                                Constant.point = Long.parseLong("0");

                                Editor e = preferenceses.edit();
                                e.putLong("buxvalue", Constant.bux);
                                e.putLong("uservalue", Constant.userId);
                                e.putLong("donationpoint", Constant.point);
                                e.commit();

                                Editor e1 = preference.edit();
                                e1.putLong("buxvalue", Constant.bux);
                                e1.putLong("uservalue", Constant.userId);
                                e1.putLong("donationpoint", Constant.point);
                                e1.commit();
                            }

                            /**final Redirection to Chat*/
                            File dir = new File(Constant.local_database_dir);
                            File[] files = dir.listFiles();
                            File file = null;
                            try {
                                Arrays.sort(
                                        files,
                                        LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
                                for (int i = 0; i < files.length; i++) {
                                    file = files[i];
                                    System.out.printf(
                                            "File %s - %2$tm %2$te,%2$tY%n= ",
                                            file.getName(), file.lastModified());
                                    SimpleDateFormat date_format = new SimpleDateFormat(
                                            "hh:mma ,dd/MM/yyyy");
                                    Date dt = new Date(file.lastModified());
                                    String date = date_format.format(dt);

                                }
                            } catch (Exception e) {
                                // //ACRA.getErrorReporter().handleException(e);
                                // TODO: handle exception
                            }
                            if (file != null) {
                                startService(new Intent(OtpVerification.this, ContactLastSync.class));
                                startActivity(new Intent(OtpVerification.this, SliderTesting.class));
                                finish();
                            } else {
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(OtpVerification.this);
                                Editor editor = sp.edit();
                                editor.putString("activity_name", "SliderTesting");
                                editor.putBoolean("decline", false);
                                editor.commit();
                                SharedPreferences sps = OtpVerification.this.getSharedPreferences(KachingMeApplication.getPereference_label(),
                                        Activity.MODE_PRIVATE);
                                Editor editor1 = sps.edit();
                                editor1.putString("activity_name", "SliderTesting");
                                editor1.putBoolean("decline", false);
                                editor1.commit();
                                Signin.isUserExist = false;
                                startService(new Intent(OtpVerification.this, ContactLastSync.class));
                                startActivity(new Intent(OtpVerification.this, SliderTesting.class));
                                finish();
                            }
                            progressLoginDialog.dismiss();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            progressLoginDialog.dismiss();
                            Constant.printMsg("login final Exception..........." + e);
                            Toast.makeText(getApplicationContext(),
                                    "Login Failed.!Please try again later!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressLoginDialog.dismiss();
                        Toast.makeText(OtpVerification.this, "Time Out..! Try Again Later !!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressLoginDialog.dismiss();
                    Toast.makeText(OtpVerification.this, "Time Out..! Try Again Later !!",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                progressLoginDialog.dismiss();
            }
        }
    }

    public class postSecondaryOtp extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();
            result = ht.httpget(KachingMeConfig.SECONDARY_URL_RES + full_mobile_no
                    + "&otp=" + Constant.Otp);
            Constant.printMsg("result dis verification" + result + "    "
                    + data);
            return result;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressSecondaryDialog.dismiss();
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
                    int count = preferenceses.getInt("added_num", 0);

                    ed = preferenceses.edit();
                    ed.putInt("added_num", count + 1);
                    ed.commit();
                    Constant.emptyFreebie = true;
                    Intent intent = new Intent(OtpVerification.this,
                            VerificationActivity.class);
                    Constant.fullmob = full_mobile_no;
                    Constant.countrycode = country_code;
                    intent.putExtra("mobileno", preferenceses.getString("countrycode", "")
                            + preferenceses.getString("number", ""));
                    intent.putExtra("country_code", country_code);
                    startActivity(intent);
                    finish();
                    Constant.printMsg("primarty.....1...." + Constant.countrycode + Constant.fullmob);
                    Constant.printMsg("primarty.....2...." + preferenceses.getString("countrycode", "")
                            + preferenceses.getString("number", ""));
                    ContentValues cv = new ContentValues();
                    cv.put("primarynum", preferenceses.getString("number", ""));
                    cv.put("secondarynum", Constant.fullmob);
                    insertNumber(cv);
                    int countMsg = preference.getInt("sec_count", 0);
                    if (countMsg == 1) {
                        Toast.makeText(OtpVerification.this, "Added Second Number",
                                Toast.LENGTH_SHORT).show();
                    } else if (countMsg == 2) {
                        Toast.makeText(OtpVerification.this, "Added Third Number",
                                Toast.LENGTH_SHORT).show();
                    } else if (countMsg == 3) {
                        Toast.makeText(OtpVerification.this, "Added Fourth Number",
                                Toast.LENGTH_SHORT).show();
                    } else if (countMsg == 4) {
                        Toast.makeText(OtpVerification.this, "Added Fifth Number",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Network Error!Please try again later!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class postPrimaryOtp extends AsyncTask<String, String, String> {
        //ProgressDialog progressDialog;

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            // progressDialog = new ProgressDialog(OtpVerification.this,
            //        AlertDialog.THEME_HOLO_LIGHT);
            //progressDialog.setMessage("Please Wait...");
            //progressDialog.setProgressDrawable(new ColorDrawable(
            //        android.graphics.Color.BLUE));
            //progressDialog.setCancelable(false);
            //progressDialog.show();

        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();
            result = ht.doPostMobizee(data, KachingMeConfig.OTP_URL);
            Constant.printMsg("Siva Test ::>>>> data" + data);
            Constant.printMsg("Siva Test ::>>>> result.." + result);
            return result;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Constant.printMsg("Siva Test ::>>>> " + result.toString());
            //progressDialog.dismiss();
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
                        cv.put("companyLogoPath", jobj1.getString("companyLogoPath"));
                        cv.put("firstName", jobj1.getString("firstName"));
                        cv.put("lastName", jobj1.getString("lastName"));
                        cv.put("website", jobj1.getString("website"));
                        cv.put("companyName", jobj1.getString("companyName"));
                        Constant.printMsg("mesaagess   ;:::::::>>>>>>> " + freelistlocal);
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
                    Constant.point = Long.parseLong("0");
                    Editor e = preferenceses.edit();
                    e.putLong("buxvalue", Constant.bux);
                    e.putLong("uservalue", Constant.userId);
                    e.putLong("donationpoint", Constant.point);
                    e.commit();
                    Editor e1 = preference.edit();
                    e1.putLong("buxvalue", Constant.bux);
                    e1.putLong("uservalue", Constant.userId);
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
                Constant.emptyFreebie = true;
//                if (Constant.byteimage != null) {
//                    new postProfile().execute();
//                }
//                new postStatus().execute();
                if (Constant.manualmail.equals("")) {
                    //new MySync().execute();
                    ConcurrentAsyncTaskExecutor.executeConcurrently(new MySync());
                    commonMethods.stopAsyncTask(new MySync(), progressRegistrationDialog);
                } else {
                    //new MySync_Check().execute();
                    ConcurrentAsyncTaskExecutor.executeConcurrently(new MySync_Check());
                    commonMethods.stopAsyncTask(new MySync_Check(), progressRegistrationDialog);
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Network Error!Please try again later!",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class postProfile extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            try {
                Constant.printMsg("Profile update..............");
                HttpConfig ht = new HttpConfig();
                ProfileUpdateDto profileUpdate = new ProfileUpdateDto();
                profileUpdate.setPhoneNumber(country_code + preferenceses.getString("number", ""));
                // profileUpdate.setProfilePhoto(encodeToBase64(Constant.bitmapImage, Bitmap.CompressFormat.JPEG,100));
                profileUpdate.setProfilePhoto(Base64.encodeToString(Constant.byteimage,
                        Base64.NO_WRAP));
                Gson gson = new Gson();
                String strData = gson.toJson(profileUpdate).toString();
                Constant.printMsg("Profile update........post data........." + strData);
                result = ht.doPostMobizee(strData, KachingMeConfig.UPDATE_PROFILE);
            } catch (Exception e) {
                e.printStackTrace();
                Constant.printMsg("Profile update....result err....." + e);
            }
            Constant.printMsg("Profile update....result....." + result);
            return result;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Constant.printMsg("siva contact result res:;" + result);
        }
    }

    public class postStatus extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            try {
                String encodeStatus = URLEncoder.encode(getResources().getString(R.string.hey_im_usning_niftycha), "utf-8");
                HttpConfig ht = new HttpConfig();
                result = ht.httpget(KachingMeConfig.UPDATE_STATUS + "phoneNumber=" + country_code + preferenceses.getString("number", "")
                        + "&status=" + encodeStatus);
                Constant.printMsg("Profile update....service....." + KachingMeConfig.UPDATE_STATUS + "phoneNumber=" + country_code + preferenceses.getString("number", "")
                        + "&status=" + encodeStatus);
                Constant.printMsg("Profile update....result fi....." + result);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Constant.printMsg("Profile update....result....." + result);
            return result;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Constant.printMsg("siva contact result res:;" + result);
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
                    + KachingMeConfig.Contact_URL + "..." + data1);

            result = ht.doPostMobizee(data1, KachingMeConfig.Contact_URL);

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

}
