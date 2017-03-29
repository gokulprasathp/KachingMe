package com.wifin.kachingme.registration_and_login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wifin.kachingme.R;
import com.wifin.kachingme.adaptors.CountryAdapter;
import com.wifin.kachingme.applications.NiftyApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.CartDetailsDto;
import com.wifin.kachingme.pojo.CountryCodeGetSet;
import com.wifin.kachingme.pojo.RegistrationResponse;
import com.wifin.kachingme.pojo.RestUserDetailsDto;
import com.wifin.kachingme.pojo.SecondaryUserDto;
import com.wifin.kachingme.services.ContactLastSync;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.AvatarManager;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.LastModifiedFileComparator;
import com.wifin.kachingme.util.WebConfig;
import com.wifin.kachingme.util.XMLParser;

import org.apache.http.Header;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.sasl.packet.SaslStreamElements;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Signin extends Slideshow implements OnClickListener {

    EditText mMobileno, mPassword;
    TextView mMsgText, mCountryCode, mSeperator, mForgotPassword, mNextButton;
    AutoCompleteTextView mCountry;
    LinearLayout mMobileLayout;

    boolean isUserExist = false, isLogintry = false;
    public static String COUNTRY, COUNTRYCODE, COUNTRYCODE_CHAR;
    String data, strMobileNumber = null, TAG = Signin.class.getSimpleName(), country, mTextAuto,
            country_code, mobileno, full_mobile_no, primary_number, secondary_number;

    ArrayList<CountryCodeGetSet> country_list;
    CountryAdapter adapter;
    ArrayAdapter adapterc;
    ArrayList<String> items = new ArrayList<String>();

    ProgressDialog progressDialog;
    SharedPreferences preferences, sp1;
    Editor editor;
    PhoneNumber NumberProto;
    PhoneNumberUtil phoneUtil;
    AsYouTypeFormatter formatter;

    //    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 226;
    CommonMethods commonMethods = new CommonMethods(Signin.this);
    DatabaseHelper dbAdapter;
    Dbhelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ViewGroup vg = (ViewGroup) findViewById(R.id.register_mainLayout);
        View.inflate(this, R.layout.signin, vg);
        initialization();
        screenArrangeSignin();
        sIndicator.setVisibility(View.GONE);
        Constant.printMsg("succeeeeeeeeeeeeee" + Constant.login);
        if (Constant.login) {
            sLoginImage.setVisibility(View.GONE);
        }
        Constant.mFromVerfication = false;
        mMobileno.setEnabled(false);
        mMobileno.setImeOptions(EditorInfo.IME_ACTION_DONE);

        sp1 = PreferenceManager.getDefaultSharedPreferences(this);
        preferences = getSharedPreferences(NiftyApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        editor = preferences.edit();
        country_list = new ArrayList<CountryCodeGetSet>();
        country_list = getCountry();
        items = getCountryName();
        db = new Dbhelper(getApplicationContext());

        if (Constant.addverification == true) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                mobileno = bundle.getString("mobileno");
                country_code = bundle.getString("country_code");
                full_mobile_no = mobileno;

                Log.d(TAG, "Mobile NO::" + mobileno);
                Constant.printMsg("country:::signin" + mobileno);
            }
        }

        Log.d(TAG, "Country List Size::" + country_list.size());
        adapterc = new ArrayAdapter(this, R.layout.dropdown, items);
        mCountry.setAdapter(adapterc);
        mCountry.requestFocus();

        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        strMobileNumber = telephonyManager.getLine1Number();
        phoneUtil = PhoneNumberUtil.getInstance();
        formatter = phoneUtil.getAsYouTypeFormatter(Locale.getDefault()
                .getCountry());

        mCountry.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                if (mCountry.getText().toString().length() == 0) {
                    commonMethods.Toast_call(Signin.this, getResources()
                            .getString(R.string.please_select_country));
                    mCountryCode.setText("");
                    mMobileno.setText("");
                    mMobileno.setEnabled(false);
                    mCountry.showDropDown();
                } else {
                    String couname = mCountry.getText().toString().trim();
                    if (Arrays.asList(items).contains(couname)) {

                        mMobileno.setEnabled(true);
                    }
                }
                mTextAuto = mCountry.getText().toString().trim();
            }
        });


        mCountry.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {
                // Constant.printMsg("Selected Position::"+position);
                mMobileno.setEnabled(true);
                mMobileno.requestFocus();
                String selecttx = mCountry.getText().toString().trim();

                for (int i = 0; i < country_list.size(); i++) {

                    String name = country_list.get(i).getName().toString();
                    if (name.equals(selecttx)) {
                        try {
                            mCountryCode.setText(country_list.get(i).getCode());
                            COUNTRY = country_list.get(i).getName();
                            COUNTRYCODE = country_list.get(i).getCode();
                            COUNTRYCODE_CHAR = country_list.get(i)
                                    .getCountry_Code().toUpperCase();
                            formatter = phoneUtil
                                    .getAsYouTypeFormatter(COUNTRYCODE_CHAR);
                        } catch (Exception e) {
                            // ACRA.getErrorReporter().handleException(e);
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        dbAdapter = NiftyApplication.getDatabaseAdapter();
        mMobileno.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                // this is for backspace
                if (keyCode == KeyEvent.ACTION_DOWN) {
                    // this is for backspace
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
                return false;
            }
        });
    }

    private void initialization() {
        mMsgText = (TextView) findViewById(R.id.sigin_textView);
        mCountry = (AutoCompleteTextView) findViewById(R.id.sigin_country);
        mCountryCode = (TextView) findViewById(R.id.sigin_country_code);
        mSeperator = (TextView) findViewById(R.id.sigin_seperator);
        mMobileno = (EditText) findViewById(R.id.signin_mobileno);
        mPassword = (EditText) findViewById(R.id.sigin_password);
        mMobileLayout = (LinearLayout) findViewById(R.id.signin_mobileLayout);

        mNextButton = (TextView) findViewById(R.id.signin_next);
        mForgotPassword = (TextView) findViewById(R.id.sigin_forgot_password);

        Constant.typeFace(this, mMobileno);
        Constant.typeFace(this, mPassword);
        Constant.typeFace(this, mCountry);
        Constant.typeFace(this, mCountryCode);
        Constant.typeFace(this, mSeperator);
        Constant.typeFace(this, mForgotPassword);
        Constant.typeFace(this, mNextButton);
        Constant.typeFace(this, mMsgText);

        mNextButton.setOnClickListener(this);
        mForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sigin_forgot_password:
                forgetPasswordRedirect();
                break;

            case R.id.signin_next:
                submitRegistration();
                break;
        }
    }

    private void submitRegistration() {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Log.d(TAG, "Country::" + COUNTRYCODE_CHAR);
            NumberProto = phoneUtil.parse(
                    mMobileno.getText().toString(), COUNTRYCODE_CHAR);
        } catch (NumberParseException e) {
            // ACRA.getErrorReporter().handleException(e);
            System.err.println("NumberParseException was thrown: "
                    + e.toString());
        }
        // String couname = null;
        // couname = mCountry.getText().toString().trim();
        if (mCountry.getText().toString().length() == 0) {
            commonMethods.showAlertDialog(this, getResources()
                    .getString(R.string.please_select_country), true);
        } else if (mMobileno.getText().toString().length() == 0) {
            commonMethods.showAlertDialog(this, getResources()
                    .getString(R.string.please_enter_phone_number), true);
        } else if (!phoneUtil.isValidNumber(NumberProto)) {
            commonMethods.showAlertDialog(this,
                    "Invalid phone number", true);
        } else {
            /** after the country and phone validation */
            if (Constant.addverification == true) {
                data = jsonFormAddNumber();
                Constant.printMsg("siva .postSecondaryNumber.....");
                String secno = mMobileno.getText().toString();
                new postSecondaryNumber().execute(secno);
            } else {
                if (Connectivity.isConnected(Signin.this)) {
                    // new MySync().execute();
                    country = COUNTRYCODE_CHAR;
                    Constant.country = country;
                    Constant.countrycode = COUNTRYCODE;
                    if (isUserExist) {
                        /*login session*/
                        String password = mPassword.getText().toString();
                        Constant.printMsg("siva .MySync.....");
                        new MySync().execute(password);
                    } else {
                        /** first time of user to call this */
                        // IS_USER_REGISTER();
                        // vollyCheckSecNumber();
                        String code = mCountryCode.getText().toString().trim();
                        String mobileNo = mMobileno.getText().toString().trim();
//						new getSecondaryNumber().execute(code, mobileNo);
                        getSecondaryNumberByVolley(code, mobileNo);
                        Constant.mPrimarynum = COUNTRYCODE;
                        Constant.printMsg("siva primary:>>>>>"
                                + Constant.mPrimarynum);
                    }
                } else {
                    commonMethods.Toast_call(this, getResources()
                            .getString(R.string.no_internet_connection));
                }
            }
        }
    }

    private void forgetPasswordRedirect() {
        long current_time = (System.currentTimeMillis() / 1000L);
        long last_send_time = preferences.getLong("forgot_password",
                current_time);
        long diff_time = current_time - last_send_time;
        Log.d(TAG, "Diff Time::" + diff_time);
        if (diff_time > 0 && diff_time < 300) {
            commonMethods.Toast_call(Signin.this,
                    getResources().getString(R.string.password_already_send_on_your_mobile_number));
            Constant.printMsg("called if");
        } else {
            editor.putLong("forgot_password", current_time);
            editor.commit();
            Constant.printMsg("called");
            if (Connectivity.isConnected(Signin.this)) {
                RequestParams request_params = new RequestParams();
                request_params.put("jid",
                        preferences.getString("MyPrimaryNumber", "")
                                + NiftyApplication.getHost());
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(
                        getString(R.string.webservice_host)
                                + getString(R.string.forgot_password_webservice),
                        request_params,
                        new AsyncHttpResponseHandler(Looper
                                .getMainLooper()) {

                            @Override
                            public void onFailure(int arg0,
                                                  Header[] arg1, byte[] arg2,
                                                  Throwable arg3) {
                                // TODO Auto-generated method stub
                                Constant.printMsg("failure");
                            }

                            @Override
                            public void onSuccess(int arg0,
                                                  Header[] arg1, byte[] arg2) {
                                // TODO Auto-generated method stub
                                // TODO Auto-generated method stub
                                // Log.d(TAG,"SMS Service Success Response::"+content);
                                Constant.printMsg("success ");
                                commonMethods
                                        .showAlertDialog(
                                                Signin.this,
                                                getResources()
                                                        .getString(
                                                                R.string.you_will_recieve_sms),
                                                true);

                            }

                        });
            } else {
                commonMethods.Toast_call(
                        Signin.this,
                        getResources().getString(
                                R.string.no_internet_connection));
            }
        }
    }

    /**
     * Call a service using volly library
     */
    public void getSecondaryNumberByVolley(String code, String mobile) {
        // String tag_json_arry = "json_array_req";

        String url = WebConfig.GET_PRIMARY_NUMBER;
//		JSONObject jsonObj = null;
//		try {
//			jsonObj = new JSONObject(data)
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			Constant.printMsg("error  >>>>>>> " + e);
//			e.printStackTrace();
//		}
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("phoneNumber", code + mobile);
        postParam.put("responseCode", "1");
        JSONObject postData = new JSONObject(postParam);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Constant.printMsg("post response........" + postData);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url,
                postData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Constant.printMsg("reponse of secondary number........" + response);
                handleSceondaryNumberCheckResponse(response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Constant.printMsg("reponse of secondary number error........" + error);
            }

        });
        /**for remove cache data and timeout declaration */
        RequestQueue queue = Volley.newRequestQueue(this);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjReq.setShouldCache(false);
        queue.getCache().remove(url);
        queue.add(jsonObjReq);

    }

    public class getSecondaryNumber extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            String result;
            Map<String, String> postParam = new HashMap<String, String>();
            postParam.put("phoneNumber", args[0] + args[1]);
            postParam.put("responseCode", "1");
            JSONObject postData = new JSONObject(postParam);
            Constant.printMsg("siva post datya.........."
                    + postData.toString());
            HttpConfig ht = new HttpConfig();
            result = ht.doPostMobizee(postData.toString(),
                    WebConfig.GET_PRIMARY_NUMBER);

            Constant.printMsg("siva ......dpostatya......"
                    + WebConfig.GET_PRIMARY_NUMBER + "............" + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(Signin.this,
                    AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setProgressDrawable(new ColorDrawable(
                    android.graphics.Color.BLUE));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }

    private void handleSceondaryNumberCheckResponse(String result) {
        // TODO Auto-generated method stub
        if (result != null && result.length() > 0) {
            if (commonMethods.isJSONValid(result.trim())) {
                JSONObject json, jsonData;
                try {
                    json = new JSONObject(result);
                    jsonData = new JSONObject(json
                            .get("restUserDetailsDto").toString());
                    Constant.printMsg("siva secondray no"
                            + jsonData.get("secondaryContactNo")
                            + "....primaryy..."
                            + jsonData.get("primaryContactNo"));
                    if (!jsonData.get("primaryContactNo").toString().trim()
                            .equalsIgnoreCase("null")
                            && jsonData.get("primaryContactNo").toString()
                            .length() > 0) {
                        String Prime = jsonData.get("primaryContactNo")
                                .toString().trim();
                        String second = jsonData.get("secondaryContactNo")
                                .toString().trim();
                        IS_USER_REGISTER(Prime + NiftyApplication.getHost());
                        Editor editor = preferences.edit();
                        editor.putString("MySecondaryNumber", second);
                        editor.putString("MyPrimaryNumber", Prime);
                        editor.commit();
                        Constant
                                .prtMsg("siva Using number as sac to prim..."
                                        + Prime
                                        + NiftyApplication.getHost());
                    } else {
                        IS_USER_REGISTER(mCountryCode.getText().toString().trim()
                                + mMobileno.getText().toString().trim()
                                + NiftyApplication.getHost());
                        Constant.printMsg("siva Using number as direct prim..."
                                + mCountryCode.getText().toString().trim()
                                + mMobileno.getText().toString().trim()
                                + NiftyApplication.getHost());
                        Editor editor = preferences.edit();
                        editor.putString("MyPrimaryNumber", mCountryCode
                                .getText().toString().trim()
                                + mMobileno.getText().toString().trim());
                        editor.commit();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            } else {
                progressDialog.dismiss();
                Toast.makeText(Signin.this, "Time Out! Try again later",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            progressDialog.dismiss();
            Toast.makeText(Signin.this, "Network Error! Try again later",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public class postSecondaryNumber extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(Signin.this,
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

            String no = sp1.getString("number", "");
            String code = sp1.getString("countrycode", "");
            result = ht.httpget(WebConfig.SECONDARY_URL + code + no
                    + "&secondaryNumber=" + COUNTRYCODE + params[0]
                    + "&countryCode=" + COUNTRYCODE);

            Constant.printMsg("siva ......secondary result....."
                    + WebConfig.SECONDARY_URL + code + no + "&secondaryNumber="
                    + COUNTRYCODE + params[0] + "&countryCode=" + COUNTRYCODE
                    + "............" + result);

            return result;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            progressDialog.dismiss();
            if (result != null && result.length() > 0) {
                if (result.equalsIgnoreCase("Primary Number Not Exists")) {
                    Toast.makeText(getApplicationContext(),
                            "Invalid User...Mobile Number Not Exists",
                            Toast.LENGTH_SHORT).show();
                } else if (result
                        .equalsIgnoreCase("Secondary Number already exists")) {
                    Toast.makeText(getApplicationContext(),
                            "Mobile Number already Exists", Toast.LENGTH_SHORT)
                            .show();
                } else if (result.equalsIgnoreCase("Secondary Limit Reached")) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry Limit Exceeded", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put("primarynum", primary_number);
                    cv.put("secondarynum", secondary_number);
                    insertNumber(cv);
                    Constant.Otp = result;
                    Constant.otpnumner = mMobileno.getText().toString();
                    Constant.printMsg("verif otp:" + Constant.Otp);

                    Intent intent = new Intent(Signin.this,
                            OtpVerification.class);
                    Constant.mVerifiedNum = "+" + COUNTRYCODE + " - "
                            + mMobileno.getText().toString();
                    intent.putExtra("mobileno", COUNTRYCODE
                            + mMobileno.getText().toString());
                    // intent.putExtra("mobileno", sp1.getString("countrycode",
                    // "")
                    // +sp1.getString("number", ""));

                    Constant.printMsg("siva mobile......1,....."
                            + COUNTRYCODE + mMobileno.getText().toString());
                    Constant.printMsg("siva mobile......1,prinmary....."
                            + sp1.getString("countrycode", "")
                            + sp1.getString("number", ""));
                    intent.putExtra("country_code", COUNTRYCODE);
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

    public String jsonFormAddNumber() {
        String d = null;

        String no = sp1.getString("number", "");
        String code = sp1.getString("countrycode", "");
        String secno = mMobileno.getText().toString();

        SecondaryUserDto l = new SecondaryUserDto();

        l.setCountryCode(COUNTRYCODE);
        l.setPrimaryNumber(COUNTRYCODE + no);
        l.setSecondaryNumber(COUNTRYCODE + secno);

        d = new Gson().toJson(l);

        Constant.printMsg("siva orderverification" + d.toString());
        return d;
    }

    public void insertNumber(ContentValues cv) {
        // TODO Auto-generated method stub
        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_NUMBERS, null, cv);
            Constant.printMsg("No of inserted rows in kons :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in kons details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }
    }

    public void IS_USER_REGISTER(String strPhone) {

        RequestParams request_params = new RequestParams();
        request_params.put("jid", strPhone);

        Log.d(TAG, "Jid::" + strPhone);

        AsyncHttpClient client = new AsyncHttpClient();
        Constant.printMsg("siva parama async......" + request_params);
        client.post(getString(R.string.webservice_host)
                        + getString(R.string.is_user_register_webservice),
                request_params,
                new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                    @Override
                    public void onFinish() {
                        Constant.printMsg("siva onFinish......");
                        super.onFinish();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onStart() {
                        Constant.printMsg("siva onStart......");
                        super.onStart();
                        progressDialog = ProgressDialog.show(Signin.this,
                                getResources().getString(R.string.please_wait),
                                getResources().getString(R.string.loading),
                                false);

                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        // TODO Auto-generated method stub
                        String error = null;

                        if (arg3 != null)
                            error = stackTraceToString(arg3);

                        String resp = null;
                        if (arg2 != null)
                            resp = new String(arg2);

                        if (arg3.getCause() instanceof IOException) {
                            Constant.printMsg("failureF Connection timeout !");
                        }
                        Constant.printMsg("failureF register :::::>>>>>>@@@" + error + " -- ");

                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub
                        // TODO Auto-generated method stub
                        Log.d(TAG, "IS_USER_REGISTER:::::" + new String(arg2));
                        Constant.printMsg("success register :::::>>>>>>" + Constant.login);
                        Constant.printMsg("success register :::::>>>>>> arg..." + new String(arg2).equals("1"));

                        if (Constant.login) {
                            Constant.printMsg("siva.........................login true");

                            if (new String(arg2).equals("1")) {
                                Constant.printMsg("siva..............1...........postRegister");

                                isUserExist = true;
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        mMobileno.setEnabled(false);
                                        mCountry.setEnabled(false);
                                        mPassword
                                                .setVisibility(View.VISIBLE);
                                        mForgotPassword
                                                .setVisibility(View.VISIBLE);
                                        mNextButton.setText("Login");
                                    }
                                });
                            } else {
                                /** is return inavlid user case */
                                commonMethods.showAlertDialog(
                                        Signin.this,
                                        getResources().getString(
                                                R.string.invalid_user), true);
                            }

                        } else {
                            if (new String(arg2).equals("1")) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        showAlerDialog();
                                    }
                                });
                            } else {
                                String nu = mMobileno.getText().toString();
                                editor.putString(Constant.COUNTRY_CODE_LABEL,
                                        mCountryCode.getText().toString());
                                editor.commit();
                                Constant.phone = mMobileno.getText()
                                        .toString();
                                Constant
                                        .prtMsg("siva.........................postRegister");
                                new postRegister().execute();
                            }
                        }

                    }
                });
    }

    public class postRegister extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(Signin.this,
                    AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Please Wait...Sending Otp");
            progressDialog.setProgressDrawable(new ColorDrawable(
                    android.graphics.Color.BLUE));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();

            result = ht.httpget(WebConfig.REGISTER_URL + "emailId="
                    + Constant.manualmail + "&dateofBirth=" + Constant.dob
                    + "&phoneNumber=" + COUNTRYCODE + Constant.phone
                    + "&password=" + Constant.pass + "&deviceId="
                    + Constant.device_id + "&imeNumber=" + Constant.Imei_no);

            Constant.printMsg("siva post registration...."
                    + WebConfig.REGISTER_URL + "emailId=" + Constant.manualmail
                    + "&dateofBirth=" + Constant.dob + "&phoneNumber="
                    + COUNTRYCODE + Constant.phone + "&password="
                    + Constant.pass + "&deviceId=" + Constant.device_id
                    + "&imeNumber=" + Constant.Imei_no + "........." + result);
            return result;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Constant.printMsg("result:::::::>>>>>>>>> " + result);
            progressDialog.dismiss();
            if (result != null && result.length() > 0) {
                RegistrationResponse login_response;
                Gson g = new Gson();
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(result);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String actualData = new String(jObj.toString().trim());
                g = new Gson();
                login_response = g.fromJson(actualData,RegistrationResponse.class);
                if (login_response.getOtp() != null && login_response.getOtp().length() > 0) {
                    String s = login_response.getOtp().toString().trim();
                    Constant.Otp = s;
                }
                if (login_response.getResponseCode()!= null && login_response.getResponseCode().length() > 0) {
                    String s1 = login_response.getResponseCode().toString().trim();
                    Constant.responsecode = s1;
                    Constant.printMsg("response code ::::::>>>>>>>> " + Constant.responsecode + "   " + s1);
                }

                if (login_response.getErrorMsg() != null) {
                    if (login_response.getErrorMsg().equalsIgnoreCase(
                            "Already Exists as Secondary Number")) {
                        Toast.makeText(getApplicationContext(),
                                "Already Exists as Secondary Number",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                Constant.otpnumner = mMobileno.getText().toString();
                Constant.printMsg("country:::" + COUNTRYCODE + mMobileno.getText().toString());
                Intent intent = new Intent(Signin.this, OtpVerification.class);
                Constant.mVerifiedNum = "+" + COUNTRYCODE + " - " + mMobileno.getText().toString();
                intent.putExtra("mobileno", COUNTRYCODE + mMobileno.getText().toString());
                Constant.printMsg("siva mobile......2,....." + COUNTRYCODE + mMobileno.getText().toString());
                intent.putExtra("country_code", COUNTRYCODE);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Network Error!Please try again later!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public String stackTraceToString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public void showAlerDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Signin.this);
        alertDialog.setTitle(R.string.Alert);
        alertDialog.setMessage("Number Already Exist.!Do you want to login?");
        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isUserExist = true;
                        mMobileno.setEnabled(false);
                        mCountry.setEnabled(false);
                        mPassword.setVisibility(View.VISIBLE);
                        mForgotPassword.setVisibility(View.VISIBLE);
                        mNextButton.setText("Login");
                        sLoginImage.setVisibility(View.GONE);
                    }
                });
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public ArrayList<CountryCodeGetSet> getCountry() {
        ArrayList<CountryCodeGetSet> list = new ArrayList<CountryCodeGetSet>();
        CountryCodeGetSet country = new CountryCodeGetSet();
        country.setCode("");
        country.setName(getResources().getString(R.string.select_country));
        country.setCountry_Code("in");
        list.add(country);

        StringBuilder x = null;
        try {
            InputStream in_s = getApplicationContext().getAssets().open(
                    "countrylist.xml");

            byte[] bytes = new byte[1000];

            x = new StringBuilder();

            int numRead = 0;
            while ((numRead = in_s.read(bytes)) >= 0) {
                x.append(new String(bytes, 0, numRead));
            }

        } catch (Exception e) {
            // ACRA.getErrorReporter().handleException(e);
            e.printStackTrace();
        }
        ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();

        XMLParser parser = new XMLParser();
        // String xml = parser.getXmlFromUrl(URL); // getting XML
        Document doc = parser.getDomElement(x.toString()); // getting DOM
        // element

        NodeList nl = doc.getElementsByTagName("country");

        for (int i = 0; i < nl.getLength(); i++) {
            country = new CountryCodeGetSet();
            Element e = (Element) nl.item(i);
            // Constant.printMsg("Name::"+parser.getValue(e, "name"));
            country.setCode(parser.getValue(e, "phoneCode"));
            Constant.printMsg("country code:::::::>>>>"
                    + parser.getValue(e, "phoneCode"));
            country.setName(parser.getValue(e, "name"));
            country.setCountry_Code(parser.getValue(e, "code"));
            list.add(country);
        }

        return list;
    }

    public ArrayList<String> getCountryName() {

        ArrayList<String> listn = new ArrayList<String>();
        CountryCodeGetSet country = new CountryCodeGetSet();
        country.setCode("");
        country.setName(getResources().getString(R.string.select_country));
        country.setCountry_Code("in");

        listn.add(getResources().getString(R.string.select_country));

        StringBuilder x = null;
        try {
            InputStream in_s = getApplicationContext().getAssets().open(
                    "countrylist.xml");

            byte[] bytes = new byte[1000];

            x = new StringBuilder();

            int numRead = 0;
            while ((numRead = in_s.read(bytes)) >= 0) {
                x.append(new String(bytes, 0, numRead));
            }

        } catch (Exception e) {
            // ACRA.getErrorReporter().handleException(e);
            e.printStackTrace();
        }
        ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();

        XMLParser parser = new XMLParser();
        // String xml = parser.getXmlFromUrl(URL); // getting XML
        Document doc = parser.getDomElement(x.toString()); // getting DOM
        // element

        NodeList nl = doc.getElementsByTagName("country");

        for (int i = 0; i < nl.getLength(); i++) {
            country = new CountryCodeGetSet();
            Element e = (Element) nl.item(i);
            // Constant.printMsg("Name::"+parser.getValue(e, "name"));
            country.setCode(parser.getValue(e, "phoneCode"));
            country.setName(parser.getValue(e, "name"));
            country.setCountry_Code(parser.getValue(e, "code"));
            listn.add(parser.getValue(e, "name"));
        }

        return listn;
    }


    Handler.Callback callback = new Handler.Callback() {

        @Override
        public boolean handleMessage(android.os.Message msg) {
            // TODO Auto-generated method stub

            // Toast.makeText(NiftyService.this, ((String)msg.obj),
            // Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    @Override
    protected void onDestroy() {
        // unbindService(mConnection);

        super.onDestroy();
    }

    /*Kaching User Login through Ejabeed*/
    public class MySync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            // super.onPreExecute();
            progressDialog = ProgressDialog.show(Signin.this, getResources()
                            .getString(R.string.please_wait),
                    getResources().getString(R.string.loading), false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                try {
                    // MOBILENO=txt_code.getText().toString()+mMobileno.getText().toString();
                    if (!TempConnectionService.connection.isConnected()) {
                        TempConnectionService.connection.connect();
                    }
                    Log.d("signin", "is connected?"
                            + TempConnectionService.connection.isConnected());

                    Log.d("signin", "is userexit false");

                } catch (Exception e) {
                    // ACRA.getErrorReporter().handleException(e);
                    e.printStackTrace();
                    Log.d("sign", "Pre-Execute::" + e.toString());
                }
                isLogintry = true;
                // connection.login(COUNTRYCODE + mMobileno.getText(),
                // txt_password.getText().toString(), "Messnger");
                Constant.printMsg("siva test login send data"
                        + preferences.getString("MyPrimaryNumber", "") + "..."
                        + params[0]);
                TempConnectionService.connection.login(preferences.getString(
                        "MyPrimaryNumber", ""), params[0]
                        .toString(), "Messnger");
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                // connection.disconnect();
                e.printStackTrace();
                // stopService(new Intent(this, signin.class));
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (isUserExist) {
                if (isLogintry) {
                    Constant.printMsg("test........" + TempConnectionService.connection);
                    if (TempConnectionService.connection != null) {
                        if (TempConnectionService.connection.isAuthenticated()) {
                            // String status="Available";
                            Constant.printMsg("passss  "
                                    + mPassword.getText().toString().trim());
                            editor.putString("pin", mPassword.getText()
                                    .toString().trim());
                            editor.putString(Constant.COUNTRY_CODE_LABEL, mCountryCode
                                    .getText().toString());
                            editor.commit();
                            byte[] av = null;
                            String status = "";
                            String fname = null, email = null;
                            try {
                                VCard vc = VCardManager.getInstanceFor(
                                        TempConnectionService.connection)
                                        .loadVCard();
                                // vc.load(connection);
                                // Constant.printMsg("Status::"+vc.getField("SORT-STRING"));
                                status = vc.getField("SORT-STRING");
                                fname = vc.getFirstName();
                                email = vc.getEmailWork();
                                av = new AvatarManager()
                                        .saveBitemap(vc.getAvatar());
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
                                TempConnectionService.connection
                                        .sendStanza(presence);

                            } catch (Exception e) {
                                // ACRA.getErrorReporter().handleException(e);
                                e.printStackTrace();
                            }

                            NiftyApplication.setUserID(COUNTRYCODE
                                    + mMobileno.getText());
                            NiftyApplication.setNifty_name(fname);
                            NiftyApplication.setNifty_email(email);
                            NiftyApplication.setAvatar(av);
                            NiftyApplication.setAcra();
                            Toast.makeText(getApplicationContext(),
                                    mMobileno.getText(), Toast.LENGTH_SHORT)
                                    .show();
                            Constant.printMsg("test:::::>>>>>>>>>>>"
                                    + mMobileno.getText().toString());
                            dbAdapter.insertLogin(
                                    COUNTRYCODE + mMobileno.getText(),
                                    mPassword.getText().toString(), status,
                                    fname, email, av);
                            // dbAdapter.insertLogin(sp.getString("MyPrimaryNumber",
                            // ""),
                            // txt_password.getText().toString(), status,
                            // fname, email, av);
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
                            String mobile = mMobileno.getText().toString();
                            new getCartDetails().execute(mobile);

                            // if (Constant.freebieflag == true) {

                            if (file != null) {
                                // new
                                // encry_decry(signin.this).decodeFile(file);
//							Intent intent = new Intent(Signin.this,
//									Restore_Backup.class);
//							startActivity(intent);
//							finish();
//siva
                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        Signin.this);
                                builder.setTitle(
                                        getResources().getString(
                                                R.string.restore_conversation_from_cloud))
                                        .setMessage(
                                                getResources().getString(
                                                        R.string.are_you_want_to_restore_conversation))
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(
                                                getResources().getString(
                                                        R.string.yes),
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
                                                        // TODO Auto-generated
                                                        // method stub
//													Intent intent = new Intent(
//															Signin.this,
//															Restore_Backup_Cloud.class);
//													startActivity(intent);
//													finish();
//siva
                                                    }

                                                })
                                        .setNegativeButton(
                                                getResources().getString(
                                                        R.string.no),
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
                                                        // TODO Auto-generated
                                                        // method stub
                                                    /*
                                                     * Intent intent=new
													 * Intent(signin
													 * .this,ContactSync
													 * .class); startActivity
													 * (intent); finish();
													 */

                                                        // Intent intent1 = new
                                                        // Intent(
                                                        // Signin.this,
                                                        // Contact_server.class);
                                                        // startService(intent1);
                                                        Intent intent1 = new Intent(
                                                                Signin.this,
                                                                ContactLastSync.class);
                                                        startService(intent1);
                                                        // Intent intent=new
                                                        // Intent(signin.this,TabsExample.class);
//													Intent intent = new Intent(
//															Signin.this,
//															SliderTesting.class);
//													startActivity(intent);
//													finish();
//siva
                                                    }

                                                }).show();

							/*
                             * Intent intent=new
							 * Intent(signin.this,ContactSync.class);
							 * startActivity(intent); finish();
							 */
                            }

                            // } else {
                            //
                            //
                            //
                            // }
                        } else {
                            TempConnectionService.connection.disconnect();
                            commonMethods.showAlertDialog(
                                    Signin.this,
                                    getResources()
                                            .getString(
                                                    R.string.phoneno_or_password_not_matched),
                                    true);
                            // your alert dialog here..
                        }
                    } else {
                        /*getting connection as null*/
                        commonMethods.showAlertDialog(
                                Signin.this,
                                getResources().getString(
                                        R.string.phoneno_or_password_not_matched),
                                true);
                    }
                } else {
                   /* false condition of login*/
                }
            }
            progressDialog.cancel();
        }
    }

    public class getCartDetails extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();
            String mob = COUNTRYCODE + params[0];
            result = ht.httpget(WebConfig.GET_CARTDETAILS + mob);
            Constant.printMsg("result dis get cart"
                    + WebConfig.GET_CARTDETAILS + mob + "    " + result);
            return result;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            // progressDialog.dismiss();

            if (result != null && result.length() > 0) {

                try {
                    JSONObject jar = new JSONObject(result);

                    RestUserDetailsDto cartlocalmail = new RestUserDetailsDto();
                    List<CartDetailsDto> cartlocal = new ArrayList<CartDetailsDto>();

                    cartlocalmail.setBux(jar.getLong("bux"));
                    Long bx = jar.getLong("bux");
                    Constant.bux = bx;
                    Constant.printMsg("buxxx:::" + jar.getLong("bux"));
                    cartlocalmail.setUserId(jar.getLong("userId"));
                    Long usid = jar.getLong("userId");
                    Constant.userId = usid;
                    cartlocalmail.setFreeBeeFlag(jar.getBoolean("freeBeeFlag"));
                    boolean flag = jar.getBoolean("freeBeeFlag");
                    Constant.freebieflag = flag;

                    JSONArray jar1 = jar.getJSONArray("cartDetailsDtos");

                    Constant.printMsg("jar:;" + jar + "   " + jar1.length()
                            + "  " + jar.length());

                    for (int j = 0; j < jar1.length(); j++) {
                        JSONObject jObject4 = jar1.getJSONObject(j);

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
                        m.setMerchantName(jObject4.getString("merchantName"));
                        m.setDiscountPrice(jObject4.getString("discountPrice"));
                        m.setOfferPrice(jObject4.getString("offerPrice"));

                        Constant.printMsg("list Dtyo::" + m.getPhotoPath());
                        cartlocal.add(m);

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
                        cv.put("merchantname",
                                jObject4.getString("merchantName"));
                        cv.put("discount", jObject4.getString("discountPrice"));
                        cv.put("offer", jObject4.getString("offerPrice"));

                        insertDB(cv);

                    }

                    cartlocalmail.setCartDetailsDtos(cartlocal);

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

                    Editor e = preferences.edit();
                    e.putLong("buxvalue", Constant.bux);
                    e.putLong("uservalue", Constant.userId);
                    e.commit();

                    Constant.point = Long.parseLong("0");

                    Editor e1 = preferences.edit();
                    e1.putLong("donationpoint", Constant.point);
                    e1.commit();
                }

            } else {
                Toast.makeText(getApplicationContext(),
                        "Network Error!Please try again later!",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void insertDB(ContentValues v) {

        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_CART, null, v);

            Constant
                    .prtMsg("No of inserted rows in shop details :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in new shop details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }

    }

    public void insertDBBux(ContentValues v) {

        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_BUX, null, v);

            Constant
                    .prtMsg("No of inserted rows in shop details :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in new shop details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Constant.printMsg("Siva......back pressed of fragmentb called");
        if (Constant.login) {
            Intent ii = new Intent(Signin.this, Slideshow.class);
            startActivity(ii);
            finish();
        } else {
            Constant.printMsg("siva mobile " + full_mobile_no);
            Intent ii = new Intent(Signin.this, VerificationActivity.class);
            Constant.fullmob = full_mobile_no;
            Constant.countrycode = country_code;
            ii.putExtra("mobileno", full_mobile_no);
            ii.putExtra("country_code", country_code);
            startActivity(ii);
            finish();
        }
    }

    private void screenArrangeSignin() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        LinearLayout.LayoutParams msgTextParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        msgTextParama.width = (int) (width * 85 / 100);
        msgTextParama.gravity = Gravity.CENTER;
        msgTextParama.topMargin = height * 9 / 100;
        mMsgText.setLayoutParams(msgTextParama);
        mMsgText.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams countryParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        countryParama.height = (int) (height * 7 / 100);
        countryParama.width = (int) (width * 85 / 100);
        countryParama.gravity = Gravity.CENTER;
        countryParama.topMargin = height * 2 / 100;
        mCountry.setLayoutParams(countryParama);
        mCountry.setGravity(Gravity.LEFT | Gravity.CENTER);
        mCountry.setPadding(width * 2 / 100, 0, 0, 0);

        LinearLayout.LayoutParams mobileLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mobileLayoutParama.height = (int) (height * 7 / 100);
        mobileLayoutParama.width = (int) (width * 85 / 100);
        mobileLayoutParama.gravity = Gravity.CENTER;
        mobileLayoutParama.topMargin = height * 1 / 100;
        mMobileLayout.setLayoutParams(mobileLayoutParama);

        LinearLayout.LayoutParams countryCodeParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        countryCodeParama.height = (int) (height * 7 / 100);
        countryCodeParama.width = (int) (width * 14 / 100);
        countryCodeParama.gravity = Gravity.CENTER;
        mCountryCode.setLayoutParams(countryCodeParama);
        mCountryCode.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams separatorParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        separatorParama.height = (int) (height * 7 / 100);
        separatorParama.width = (int) (width * 6 / 100);
        separatorParama.gravity = Gravity.CENTER;
        //separatorParama.setMargins(width*1/100,0,width*1/100,0);
        mSeperator.setLayoutParams(separatorParama);
        mSeperator.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams mobileNoParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mobileNoParama.height = (int) (height * 7 / 100);
        mobileNoParama.width = (int) (width * 65 / 100);
        mMobileno.setLayoutParams(mobileNoParama);
        mMobileno.setGravity(Gravity.LEFT | Gravity.CENTER);
        mMobileno.setPadding(width * 2 / 100, 0, 0, 0);

        LinearLayout.LayoutParams passwordParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        passwordParama.height = (int) (height * 7 / 100);
        passwordParama.width = (int) (width * 85 / 100);
        passwordParama.gravity = Gravity.CENTER;
        passwordParama.topMargin = height * 1 / 100;
        mPassword.setLayoutParams(passwordParama);
        mPassword.setGravity(Gravity.CENTER | Gravity.LEFT);
        mPassword.setPadding(width * 2 / 100, 0, 0, 0);

        LinearLayout.LayoutParams forgetParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        forgetParama.width = (int) (width * 85 / 100);
        forgetParama.gravity = Gravity.RIGHT;
        forgetParama.topMargin = (int) (height * 0.5 / 100);
        mForgotPassword.setLayoutParams(forgetParama);
        mForgotPassword.setGravity(Gravity.RIGHT);
        mForgotPassword.setPadding(0, 0, width * 1 / 100, 0);

        LinearLayout.LayoutParams nextParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nextParama.width = width * 30 / 100;
        nextParama.height = height * 6 / 100;
        nextParama.gravity = Gravity.CENTER;
        nextParama.topMargin = height * 4 / 100;
        mNextButton.setLayoutParams(nextParama);
        mNextButton.setGravity(Gravity.CENTER);

        if (width >= 600) {
            mMobileno.setTextSize(16);
            mPassword.setTextSize(16);
            mMsgText.setTextSize(16);
            mCountryCode.setTextSize(16);
            mSeperator.setTextSize(16);
            mForgotPassword.setTextSize(16);
            mNextButton.setTextSize(16);
            mCountry.setTextSize(16);
        } else if (width > 501 && width < 600) {
            mMobileno.setTextSize(15);
            mPassword.setTextSize(15);
            mMsgText.setTextSize(15);
            mCountryCode.setTextSize(15);
            mSeperator.setTextSize(15);
            mForgotPassword.setTextSize(15);
            mNextButton.setTextSize(15);
            mCountry.setTextSize(15);
        } else if (width > 260 && width < 500) {
            mMobileno.setTextSize(14);
            mPassword.setTextSize(14);
            mMsgText.setTextSize(14);
            mCountryCode.setTextSize(14);
            mSeperator.setTextSize(14);
            mForgotPassword.setTextSize(14);
            mNextButton.setTextSize(14);
            mCountry.setTextSize(14);
        } else if (width <= 260) {
            mMobileno.setTextSize(13);
            mPassword.setTextSize(13);
            mMsgText.setTextSize(13);
            mCountryCode.setTextSize(13);
            mSeperator.setTextSize(13);
            mForgotPassword.setTextSize(13);
            mNextButton.setTextSize(13);
            mCountry.setTextSize(13);
        }
    }
}
