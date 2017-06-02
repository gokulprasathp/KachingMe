package com.wifin.kachingme.deals;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.cart.CartActivity;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.pojo.RestClaimDto;
import com.wifin.kachingme.pojo.ShopDetailsDto;
import com.wifin.kachingme.redimer.ExistingAccount;
import com.wifin.kachingme.redimer.NewAccount;
import com.wifin.kachingme.registration_and_login.FreebieActivity;
import com.wifin.kachingme.util.CircleProgressBar;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.GPSTracking;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.Validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

public class VerifyShop extends HeaderActivity implements View.OnClickListener {
    public static HashMap<TextView, CountDownTimer> counters;
    TextView mShopIDText, mShopAddressText, mShopNameText, mShopNameEdit,
            mHeader, mEmailIDText, mMerchantNameText, mFreebieText,
            mUnderLineText, mAddsTxt1, mAddsTxt2, mAddsTxtCity, mAddsTxtState,
            mAddsTxtCountry, mAddsTxtPincode, mRedeemerTxtName,
            mMerchantWebsiteTxt, mExistingAccount, mNewAccount;
    EditText mShopIDEdit, mShopAddressEdit, mEmailIDEdit, mAddsEdt1, mAddsEdt2,
            mAddsEdtCity, mAddsEdtState, mAddsEdtCountry, mAddsEdtPincode,
            mRedeemerEdtName;
    Button mClearBtn, mSendBtn, mExistingAccountButton, mNewAccountButton;
    Spinner mRedeemerNameSpinner, mRedeemerIdSpinner;
    View mFreebieDivider;
    RadioGroup mRadioBtn;
    ImageView mProfileImg, mFreebieImg;
    RadioButton mNewRadioBtn, mExistingRadioBtn;
    LinearLayout mNewRegisterLayout, mNewAccountLayout, mExistingAccountLayout;
    int height, width = 0;
    // if GPS is enabled
    boolean isGPSEnabled = false;
    // if Network is enabled
    boolean isNetworkEnabled = false;
    double mLatitude = 0, mLongitude = 0;
    LinearLayout mFreebieLayout, mProfileLayout;
    String underline_string;
    String city, state, country, postalCode, subLocality, address;
    List<String> mRedeemerIdList = new ArrayList<String>();
    List<String> mRedeemerNameList = new ArrayList<String>();
    List<String> mRedeemerEmailList = new ArrayList<String>();
    List<String> mRedeemerAddressList = new ArrayList<String>();
    Dialog dialog;
    private LocationManager mLocationManager;
    private ImageLoadingListener animateFirstListener = new FreebieActivity.AnimateFirstDisplayListener();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.activity_shop_verify, vg);
        // getWindow().setSoftInputMode(
        // WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mFooterLayout.setVisibility(View.GONE);
        mFooterView.setVisibility(View.GONE);
        initialize();
        underline_string = "New Account";
        screenArrange();

        mHeading.setText("Redeem");
        mHeaderImg.setVisibility(View.GONE);
        mNextBtn.setVisibility(View.GONE);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VerifyShop.this, CartActivity.class);
                startActivity(i);
                finish();
            }
        });

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.stub).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();
        ImageLoader.getInstance()
                .init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
        mUnderLineText.setText(Html.fromHtml(settext("New Account")),
                TextView.BufferType.SPANNABLE);
        setData();
//        head.setVisibility(View.VISIBLE);
//        head.setText("Redeem");
//        logo.setVisibility(ImageView.INVISIBLE);
//        sideMenufoot.setVisibility(LinearLayout.GONE);
//        head.setTextColor(Color.parseColor("#FFFFFF"));
//        Ka_newlogo.setVisibility(ImageView.INVISIBLE);
//        back.setBackgroundResource(R.drawable.arrow);
//        headlay.setBackgroundColor(Color.parseColor("#FE0000"));
//        back.setVisibility(ImageView.VISIBLE);
//        footer.setVisibility(ImageView.GONE);
        mLocationManager = (LocationManager) getApplicationContext()
                .getSystemService(LOCATION_SERVICE);
        isGPSEnabled = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        GPSTracking mGPSService = new GPSTracking(VerifyShop.this);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;

//        LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        mainLayoutParams.width = width * 6 / 100;
//        mainLayoutParams.rightMargin = width * 10 / 100;
//        back.setLayoutParams(mainLayoutParams);
//
//        LinearLayout.LayoutParams mainLayoutParams1 = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        mainLayoutParams1.width = width * 30 / 100;
//        mainLayoutParams1.leftMargin = -width * 130 / 100;
//        head.setLayoutParams(mainLayoutParams1);

//        if (isGPSEnabled || isNetworkEnabled) {
//            // Constant.printMsg("HIIIIIIIIIIII");
//            mGPSService.getLocation();
//            if (mGPSService.getLatitude() != 0
//                    && mGPSService.getLongitude() != 0) {
//                mLatitude = mGPSService.getLatitude();
//                mLongitude = mGPSService.getLongitude();
//            }
//            new GetAddressTask().execute();
//            mGPSService.stopUsingGPS();
//        } else {
//            //Toast.makeText(getApplicationContext(),
//            //        "Location Service is not active.", Toast.LENGTH_SHORT)
//             //       .show();
//            mGPSService.showSettingsAlert();
//        }

        if (Constant.checkPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION))
        {
            AlertToCustomer();
        }
        else
        {
            Constant.permissionRequest(this, Manifest.permission.ACCESS_FINE_LOCATION, Constant.PERMISSION_CODE_STORAGE);
        }

        if (Connectivity.isConnected(VerifyShop.this)) {
            ShopDetailsDto sd = new ShopDetailsDto();
            sd.setName(Constant.mFreebieShopName);
            sd.setLat(mLatitude);
            sd.setLon(mLongitude);
            // new getShopData().execute(new Gson().toJson(sd));
        }
        // countDownPopup();
        mRadioBtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = (RadioButton) findViewById(checkedId);
                if (rb.getText().toString().trim()
                        .equalsIgnoreCase("New Account")) {
                    mEmailIDText.setVisibility(View.GONE);
                    mEmailIDEdit.setVisibility(View.GONE);
                    // mShopIDEdit.setVisibility(visibility)
                    mShopIDEdit.setVisibility(View.GONE);
                    mShopIDText.setVisibility(View.GONE);
                    mSendBtn.setText("Register");
                } else {
                    mEmailIDText.setVisibility(View.GONE);
                    mEmailIDEdit.setVisibility(View.GONE);
                    mShopIDEdit.setVisibility(View.GONE);
                    mShopIDText.setVisibility(View.GONE);
                    mSendBtn.setText("Redeem");
                }
            }
        });


    }

    private void initialize() {
        // TODO Auto-generated method stub
        mShopIDText = (TextView) findViewById(R.id.shop_id_text);
        mShopAddressText = (TextView) findViewById(R.id.address_text);
        mShopIDEdit = (EditText) findViewById(R.id.shop_id_edit);
        mShopNameEdit = (TextView) findViewById(R.id.shop_name_edit);
        mShopNameText = (TextView) findViewById(R.id.shop_name_text);
        mShopAddressEdit = (EditText) findViewById(R.id.address_edit);
        mClearBtn = (Button) findViewById(R.id.clean_btn);
        mSendBtn = (Button) findViewById(R.id.send_btn);
        mHeader = (TextView) findViewById(R.id.header_text);
        mEmailIDText = (TextView) findViewById(R.id.email_id_text);
        mEmailIDEdit = (EditText) findViewById(R.id.email_id_edit);
        mMerchantNameText = (TextView) findViewById(R.id.merchant_name);
        mFreebieText = (TextView) findViewById(R.id.freebie_value);
        mFreebieImg = (ImageView) findViewById(R.id.freebie_img);
        mFreebieDivider = (View) findViewById(R.id.freebie_valueView);
        mProfileImg = (ImageView) findViewById(R.id.prof_pic);
        mFreebieLayout = (LinearLayout) findViewById(R.id.freebie_layout);
        mProfileLayout = (LinearLayout) findViewById(R.id.profile_layout);

        mShopIDEdit.setVisibility(View.GONE);
        mShopIDText.setVisibility(View.GONE);

        mRadioBtn = (RadioGroup) findViewById(R.id.radio_shop);
        mNewRadioBtn = (RadioButton) findViewById(R.id.new_radio_btn);
        mUnderLineText = (TextView) findViewById(R.id.underline_text);
        mExistingRadioBtn = (RadioButton) findViewById(R.id.existing_radio_btn);

        mAddsEdt1 = (EditText) findViewById(R.id.address_line1_edit);
        mAddsEdt2 = (EditText) findViewById(R.id.address_line2_edit);
        mAddsEdtCity = (EditText) findViewById(R.id.address_city_edit);
        mAddsEdtState = (EditText) findViewById(R.id.address_state_edit);
        mAddsEdtCountry = (EditText) findViewById(R.id.address_country_edit);
        mAddsEdtPincode = (EditText) findViewById(R.id.address_pincode_edit);
        mRedeemerEdtName = (EditText) findViewById(R.id.redeemer_name_edit);

        mAddsTxt1 = (TextView) findViewById(R.id.address_line1_text);
        mAddsTxt2 = (TextView) findViewById(R.id.address_line2_text);
        mAddsTxtCity = (TextView) findViewById(R.id.address_city_text);
        mAddsTxtState = (TextView) findViewById(R.id.address_state_text);
        mAddsTxtCountry = (TextView) findViewById(R.id.address_country_text);
        mAddsTxtPincode = (TextView) findViewById(R.id.address_pincode_text);
        mRedeemerTxtName = (TextView) findViewById(R.id.redeemer_name_text);

        mNewRegisterLayout = (LinearLayout) findViewById(R.id.new_account_layout);
        mRedeemerIdSpinner = (Spinner) findViewById(R.id.shop_id_drop_down);
        mRedeemerNameSpinner = (Spinner) findViewById(R.id.shop_name_drop_down);
        mMerchantWebsiteTxt = (TextView) findViewById(R.id.merchant_website);
        mNewAccountLayout = (LinearLayout) findViewById(R.id.verifyShop_newAccouutLayout);
        mExistingAccountLayout = (LinearLayout) findViewById(R.id.verifyShop_existingAccouutLayout);

        mExistingAccount = (TextView) findViewById(R.id.verifyShop_existingAccouut);
        mNewAccount = (TextView) findViewById(R.id.verifyShop_newAccouut);
        mExistingAccountButton = (Button) findViewById(R.id.verifyShop_buttonExistingAccount);
        mNewAccountButton = (Button) findViewById(R.id.verifyShop_buttonNewAccount);

        mUnderLineText.setOnClickListener(this);
        mClearBtn.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);
//        back.setOnClickListener(this);
        mExistingAccountButton.setOnClickListener(this);
        mNewAccountButton.setOnClickListener(this);
        mMerchantWebsiteTxt.setOnClickListener(this);

        Constant.typeFace(this, mShopIDText);
        Constant.typeFace(this, mShopAddressText);
        Constant.typeFace(this, mShopNameText);
        Constant.typeFace(this, mShopNameEdit);
        Constant.typeFace(this, mHeader);
        Constant.typeFace(this, mEmailIDText);
        Constant.typeFace(this, mMerchantNameText);
        Constant.typeFace(this, mFreebieText);
        Constant.typeFace(this, mUnderLineText);
        Constant.typeFace(this, mAddsTxt1);
        Constant.typeFace(this, mAddsTxt2);
        Constant.typeFace(this, mAddsTxtCity);
        Constant.typeFace(this, mAddsTxtState);
        Constant.typeFace(this, mAddsTxtCountry);
        Constant.typeFace(this, mAddsTxtPincode);
        Constant.typeFace(this, mRedeemerTxtName);
        Constant.typeFace(this, mMerchantWebsiteTxt);
        Constant.typeFace(this, mExistingAccount);
        Constant.typeFace(this, mNewAccount);
        Constant.typeFace(this, mShopIDEdit);
        Constant.typeFace(this, mShopAddressEdit);
        Constant.typeFace(this, mEmailIDEdit);
        Constant.typeFace(this, mAddsEdt1);
        Constant.typeFace(this, mAddsEdt2);
        Constant.typeFace(this, mAddsEdtCity);
        Constant.typeFace(this, mAddsEdtState);
        Constant.typeFace(this, mAddsEdtCountry);
        Constant.typeFace(this, mAddsEdtPincode);
        Constant.typeFace(this, mRedeemerEdtName);
        Constant.typeFace(this, mClearBtn);
        Constant.typeFace(this, mSendBtn);
        Constant.typeFace(this, mExistingAccountButton);
        Constant.typeFace(this, mNewAccountButton);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_logo:
                startActivity(new Intent(VerifyShop.this, CartActivity.class));
                finish();
                break;
            case R.id.verifyShop_buttonExistingAccount:
                if (Connectivity.isConnected(VerifyShop.this)) {
                    startActivity(new Intent(VerifyShop.this, ExistingAccount.class));
                    finish();
                } else {
                    Toast.makeText(VerifyShop.this, "Please Check Your Network Connection!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.verifyShop_buttonNewAccount:
                if (Connectivity.isConnected(VerifyShop.this)) {
                    startActivity(new Intent(VerifyShop.this, NewAccount.class));
                    finish();
                } else {
                    Toast.makeText(VerifyShop.this, "Please Check Your Network Connection!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.send_btn:
                if (mSendBtn.getText().toString().trim()
                        .equalsIgnoreCase("Register")) {
                    if (validateRegister()) {
                        loadRegisterData();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please fill all the details",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (mSendBtn.getText().toString().trim().equalsIgnoreCase("Verify Shop")) {

                } else if (mSendBtn.getText().toString().trim().equalsIgnoreCase("Redeem")) {
                    loadRedeemData();
                }
                break;
            case R.id.clean_btn:
                mEmailIDEdit.setText("");
                mShopAddressEdit.setText("");
                mShopIDEdit.setText("");
                break;
            case R.id.underline_text:
                if (mUnderLineText.getText().toString().trim()
                        .equalsIgnoreCase("New Account")) {
                    mUnderLineText.setText(
                            Html.fromHtml(settext("Already have account")),
                            TextView.BufferType.SPANNABLE);
                    screenArrange();
                    mEmailIDText.setVisibility(View.GONE);
                    mEmailIDEdit.setVisibility(View.GONE);
                    // mShopIDEdit.setVisibility(visibility)
                    mShopIDEdit.setVisibility(View.GONE);
                    mShopIDText.setVisibility(View.GONE);
                    mNewRegisterLayout.setVisibility(View.VISIBLE);
                    getAddressData();
                    mSendBtn.setText("Register");
                } else {
                    mUnderLineText.setText(
                            Html.fromHtml(settext("New Account")),
                            TextView.BufferType.SPANNABLE);
                    screenArrange();
                    mEmailIDText.setVisibility(View.GONE);
                    mEmailIDEdit.setVisibility(View.GONE);
                    mShopIDEdit.setVisibility(View.GONE);
                    mShopIDText.setVisibility(View.GONE);
                    mNewRegisterLayout.setVisibility(View.GONE);
                    mSendBtn.setText("Redeem");
                }
                break;
            case R.id.merchant_website:
                String url = "http://" + mMerchantWebsiteTxt.getText().toString();

//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
                try {
                    Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    startActivity(intent);
                } catch (Exception e) {

                }


                break;
        }
    }

    public void AlertToCustomer() {
        // Build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Please give your phone to the POS Checkout Personnel to Redeem your Freebie or DeeL");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                // Show location settings when the user acknowledges
                // the alert dialog
                dialogInterface.cancel();
                // finish();
            }
        });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Constant.printMsg("verify shop canceled........");
                        startActivity(new Intent(VerifyShop.this,
                                CartActivity.class));
                        finish();
                    }
                });

        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private String settext(String string) {
        // TODO Auto-generated method stub
        String styledText = "<u><font color='blue'>" + string + "</font></u>";
        return styledText;
    }

    public void setData() {
        mUnderLineText.setText("New Account");
        mMerchantNameText.setText(Constant.mFreebieMerchantName);
        mFreebieText.setText(Constant.mFreebieName);
        mShopNameEdit.setText(Constant.mFreebieShopName);
        Constant.printMsg("Merchant website........." + Constant.mFreebieShopWebsite);
        Spanned spannedResult = Html.fromHtml("<font color=#ff0000><a href='"
                + Constant.mFreebieShopWebsite + "'>" + Constant.mFreebieShopWebsite + "</a></font>");
        mMerchantWebsiteTxt.setText(spannedResult);
        mMerchantWebsiteTxt.setClickable(true);
        mMerchantWebsiteTxt.setMovementMethod(LinkMovementMethod.getInstance());
        mExistingRadioBtn.setChecked(true);
        mNewRadioBtn.setChecked(false);
        mEmailIDText.setVisibility(View.GONE);
        mEmailIDEdit.setVisibility(View.GONE);
        mShopIDEdit.setVisibility(View.GONE);
        mShopIDText.setVisibility(View.GONE);
        mSendBtn.setText("Redeem");
        mUnderLineText.setText(Html.fromHtml(settext("New Account")),
                TextView.BufferType.SPANNABLE);
        try {
            ImageLoader.getInstance().displayImage(
                    String.valueOf(Constant.mFreeBieMerchantImgUrl.replaceAll(" ",
                            "%20")), mProfileImg, options, animateFirstListener);

            ImageLoader.getInstance().displayImage(
                    String.valueOf(Constant.mFreeBieImgUrl.replaceAll(" ", "%20")),
                    mFreebieImg, options, animateFirstListener);
        } catch (Exception e) {

        }
    }

    public void getAddressData() {
        GPSTracking mGPSService = new GPSTracking(VerifyShop.this);
        if (isGPSEnabled || isNetworkEnabled) {
            // Constant.printMsg("HIIIIIIIIIIII");
            mGPSService.getLocation();
            if (mGPSService.getLatitude() != 0
                    && mGPSService.getLongitude() != 0) {
                mLatitude = mGPSService.getLatitude();
                mLongitude = mGPSService.getLongitude();
                // mTxtContent.setText("The Gps location is :::" + mLatitude
                // + "Longi ::::;" + mLongitude);

            }
            new GetAddressTask().execute();
            mGPSService.stopUsingGPS();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Location Service is not active.", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public boolean validateRegister() {
        boolean isValid = true;
        if (!Validation.hasText(mShopAddressEdit))
            isValid = false;
        if (!Validation.isEmailAddress(mEmailIDEdit, true))
            isValid = false;
        return isValid;
    }

    public boolean validateRedeem() {
        boolean isValid = false;
        if (!Validation.hasText(mShopAddressEdit))
            isValid = false;
        if (!Validation.hasText(mShopIDEdit))
            isValid = false;
        return isValid;
    }

    public void loadRegisterData() {
        ShopDetailsDto sh = new ShopDetailsDto();
        sh.setAddress(mShopAddressEdit.getText().toString().trim());
        sh.setEmail(mEmailIDEdit.getText().toString().trim());
        sh.setLat(mLatitude);
        sh.setLon(mLongitude);
        sh.setName(mMerchantNameText.getText().toString().trim());
        String mRegisterData = new Gson().toJson(sh);
        new RegisterShopData().execute(mRegisterData);
    }

    public void loadRedeemData() {
        RestClaimDto re = new RestClaimDto();
        re.setBux(0L);
        String ctNum;
        if (Constant.mPhoneNum.contains("IN")) {
            ctNum = Constant.mPhoneNum.replace("IN", "");
        } else {
            ctNum = Constant.mPhoneNum;
        }
        re.setPhoneNumber(ctNum);
        re.setOfferId(Integer.valueOf(Constant.mFreebieId));
        re.setShopId(Integer.valueOf(mShopIDEdit.getText().toString().trim()));
        re.setType(Integer.valueOf(Constant.mCartType));
        String redeemData = new Gson().toJson(re);
        new VerifyRedeemData().execute(redeemData);
    }

    public void setShopNameDropDownData() {

        if (!(mRedeemerNameList.size() > 0)) {
            mRedeemerNameList.add("Select");
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                VerifyShop.this, android.R.layout.simple_spinner_item,
                mRedeemerNameList) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                // TextView v = (TextView) super.getView(position,
                // convertView,parent);
                // Constant.face(VerifyShop.this, (TextView) v);
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                // ((TextView)
                // v).setBackgroundColor(Color.parseColor("#BBfef3da"));
                // TextView v = (TextView) super.getView(position,
                // convertView,parent);
                // Constant.face(VerifyShop.this, (TextView) v);
                return v;
            }
        };
        spinnerArrayAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRedeemerNameSpinner.setAdapter(spinnerArrayAdapter);

    }

    public void setShopIdSpinnerDropDownData() {

        if (!(mRedeemerIdList.size() > 0)) {
            mRedeemerIdList.add("Select");
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                VerifyShop.this, android.R.layout.simple_spinner_item,
                mRedeemerIdList) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                // TextView v = (TextView) super.getView(position,
                // convertView,parent);
                // Constant.face(VerifyShop.this, (TextView) v);
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                // ((TextView)
                // v).setBackgroundColor(Color.parseColor("#BBfef3da"));
                // TextView v = (TextView) super.getView(position,
                // convertView,parent);
                // Constant.face(VerifyShop.this, (TextView) v);
                return v;
            }
        };
        spinnerArrayAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRedeemerIdSpinner.setAdapter(spinnerArrayAdapter);
    }

    private void countDownPopup() {
        // TODO Auto-generated method stub
        counters = new HashMap<TextView, CountDownTimer>();
        dialog = new Dialog(VerifyShop.this);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.count_down_timer_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final TextView tvCounter = (TextView) dialog
                .findViewById(R.id.popup_tvCounter);
        final CircleProgressBar cp = (CircleProgressBar) dialog
                .findViewById(R.id.popup_custom_progressBar_adapter);

        // final TextView tv = tvCounter;
        // final CircleProgressBar cp = circleProgressBar;

        // CountDownTimer cdt = VerifyShop.counters.get(tvCounter);
        // if (cdt != null) {
        // cdt.cancel();
        // cdt = null;
        // }
        //
        // final long difference = Long.valueOf(30);
        //
        // // Constant.printMsg("the difference is :::::" +
        // values.get(position)
        // // + " posi is :::" + position);
        //
        // cdt = new CountDownTimer(difference, 1000) {
        // @Override
        // public void onTick(long millisUntilFinished) {
        //
        // float diff1 = Float.valueOf(difference);
        // float diff2 = Float.valueOf(millisUntilFinished);
        // if ((difference - millisUntilFinished) / 1000 != 0) {
        // tvCounter
        // .setText(String
        // .valueOf((difference - millisUntilFinished) / 1000));
        //
        // cp.setProgress((((diff1 - diff2) / 1000) / (diff1 / 1000)) * 100);
        //
        // } else {
        // tvCounter.setText(String.valueOf((difference) / 1000));
        // cp.setProgress(((difference / 1000)) * 100);
        // }
        // }
        //
        // @Override
        // public void onFinish() {
        // tvCounter.setText(String.valueOf((difference) / 1000));
        // cp.setProgress(((difference / 1000)) * 100);
        // }
        // };

        // counters.put(tvCounter, cdt);
        // // cpList.add(cp);
        // cdt.start();
        final long difference = Long.valueOf(30000);
        new CountDownTimer(difference, 1000) {

            public void onTick(long millisUntilFinished) {
                // tvCounter.setText("seconds remaining: " + millisUntilFinished
                // / 1000);

                // here you can have your logic to set text to edittext

                float diff1 = Float.valueOf(difference);
                float diff2 = Float.valueOf(millisUntilFinished);

                if ((difference - millisUntilFinished) / 1000 != 0) {
                    tvCounter.setText(String
                            .valueOf((millisUntilFinished + 1000) / 1000));

                    cp.setProgress((((diff1 - diff2) / 1000) / (diff1 / 1000)) * 100);

                } else {
                    tvCounter.setText(String.valueOf((difference) / 1000));
                    cp.setProgress(((difference / 1000)) * 100);
                }

            }

            public void onFinish() {

                // Constant.printMsg("Verify shop countdown timer called ::::");

                // tvCounter.setText(String.valueOf(30));

                tvCounter.setText(String.valueOf((difference) / 1000));
                cp.setProgress(((difference / 1000)) * 100);

            }

        }.start();

    }

    public void cancelAllTimers() {
        Set<Entry<TextView, CountDownTimer>> s = counters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            try {
                Entry pairs = (Entry) it.next();
                CountDownTimer cdt = (CountDownTimer) pairs.getValue();

                cdt.cancel();
                cdt = null;
            } catch (Exception e) {
            }
        }

        it = null;
        s = null;
        counters.clear();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent i = new Intent(VerifyShop.this, CartActivity.class);
        startActivity(i);
        finish();
    }

    private void screenArrange() {
        // TODO Auto-generated method stub
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;
        Constant.screenWidth = width;
        Constant.screenHeight = height;

        LinearLayout.LayoutParams Textparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Textparams.width = width * 40 / 100;
        Textparams.height = height * 5 / 100;
        Textparams.topMargin = width * 2 / 100;
        Textparams.leftMargin = width * 5 / 100;
        mShopIDText.setLayoutParams(Textparams);
        mEmailIDText.setLayoutParams(Textparams);
        mShopNameText.setLayoutParams(Textparams);
        LinearLayout.LayoutParams Textparams2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Textparams2.width = width * 50 / 100;
        Textparams2.height = height * 5 / 100;
        Textparams2.leftMargin = width * 5 / 100;
        Textparams2.topMargin = width * 2 / 100;

        mShopAddressText.setLayoutParams(Textparams2);


        LinearLayout.LayoutParams merTextparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // merTextparams.width = width * 40 / 100;
        merTextparams.gravity = Gravity.CENTER;
        // merTextparams.topMargin = width * 1 / 100;
        mMerchantNameText.setGravity(Gravity.CENTER);
        mMerchantNameText.setLayoutParams(merTextparams);

        LinearLayout.LayoutParams underTextparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // underTextparams.width = width * 40 / 100;
        // underTextparams.gravity = Gravity.RIGHT;
        if (mUnderLineText.getText().toString().equalsIgnoreCase("New Account")) {
            underTextparams.leftMargin = width * 65 / 100;

        } else {
            underTextparams.leftMargin = width * 50 / 100;

        }
        underTextparams.topMargin = width * 3 / 100;
        mUnderLineText.setLayoutParams(underTextparams);

        LinearLayout.LayoutParams profilelayparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        profilelayparams.width = width * 34 / 100;
        profilelayparams.height = width * 34 / 100;
        profilelayparams.leftMargin = width * 30 / 100;
        // profilelayparams.topMargin = width * 2 / 100;
        // mProfileImg.setGravity(Gravity.CENTER);
        mProfileLayout.setLayoutParams(profilelayparams);

        LinearLayout.LayoutParams profileimgparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        profileimgparams.width = width * 20 / 100;
        profileimgparams.height = width * 20 / 100;
        profileimgparams.leftMargin = width * 7 / 100;
        profileimgparams.topMargin = width * 7 / 100;
        // mProfileImg.setGravity(Gravity.CENTER);
        mProfileImg.setLayoutParams(profileimgparams);

        LinearLayout.LayoutParams freelayparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        freelayparams.width = width * 96 / 100;
        freelayparams.height = width * 27 / 100;
        freelayparams.topMargin = width * 2 / 100;
        freelayparams.rightMargin = width * 2 / 100;
        freelayparams.leftMargin = width * 2 / 100;
        // mFreebieLayout.setGravity(Gravity.CENTER);
        mFreebieLayout.setLayoutParams(freelayparams);

        LinearLayout.LayoutParams profileimgparams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        profileimgparams1.width = width * 27 / 100;
        profileimgparams1.height = width * 25 / 100;
        profileimgparams1.leftMargin = width * 5 / 100;
        profileimgparams1.gravity = Gravity.CENTER;
        mFreebieImg.setLayoutParams(profileimgparams1);

        LinearLayout.LayoutParams profileDividerparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        profileDividerparams.width = (int) (width * 0.4 / 100);
        profileDividerparams.height = width * 23 / 100;
        profileDividerparams.leftMargin = width * 2 / 100;
        profileDividerparams.rightMargin = width * 2 / 100;
        profileDividerparams.topMargin = width * 2 / 100;
        // mProfileImg.setGravity(Gravity.CENTER);
        mFreebieDivider.setLayoutParams(profileDividerparams);

        LinearLayout.LayoutParams freebietextparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        freebietextparams.gravity = Gravity.CENTER;
        // freebietextparams.height = height * 10 / 100;
        freebietextparams.leftMargin = width * 5 / 100;
        // mFreebieText.setGravity(Gravity.CENTER);
        mFreebieText.setLayoutParams(freebietextparams);

        LinearLayout.LayoutParams Textparams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Textparams1.height = height * 7 / 100;
        Textparams1.bottomMargin = width * 3 / 100;
        Textparams1.topMargin = width * 5 / 100;
        mHeader.setLayoutParams(Textparams1);

        LinearLayout.LayoutParams editparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editparams.width = width * 90 / 100;
        editparams.height = height * 7 / 100;
        editparams.leftMargin = width * 5 / 100;
        mShopIDEdit.setPadding(5, 0, 0, 0);
        mShopNameEdit.setPadding(5, 15, 0, 0);
        mShopIDEdit.setLayoutParams(editparams);
        // mShopNameEdit.setGravity(Gravity.CENTER);
        mShopNameEdit.setLayoutParams(editparams);

        LinearLayout.LayoutParams editparams2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editparams2.width = width * 90 / 100;
        editparams2.height = height * 5 / 100;
        editparams2.leftMargin = width * 5 / 100;
        mShopIDEdit.setPadding(5, 0, 0, 0);

        mShopIDEdit.setLayoutParams(editparams2);
        mEmailIDEdit.setLayoutParams(editparams2);
        mEmailIDEdit.setPadding(5, 0, 0, 0);

        LinearLayout.LayoutParams editparams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editparams1.width = width * 90 / 100;
        // editparams1.height = height * 25 / 100;
        editparams1.leftMargin = width * 5 / 100;
        mShopAddressEdit.setPadding(5, 10, 5, 10);
        mShopAddressEdit.setLayoutParams(editparams1);

        LinearLayout.LayoutParams btnparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btnparams.width = width * 30 / 100;
        btnparams.height = height * 7 / 100;
        btnparams.leftMargin = width * 12 / 100;
        btnparams.topMargin = width * 2 / 100;
        mClearBtn.setLayoutParams(btnparams);
        mSendBtn.setLayoutParams(btnparams);

        LinearLayout.LayoutParams radioparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        radioparams.width = width;
        radioparams.height = height * 8 / 100;
        mRadioBtn.setLayoutParams(radioparams);

        LinearLayout.LayoutParams radioparams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        radioparams1.width = width * 40 / 100;
        radioparams1.height = height * 8 / 100;
        radioparams1.leftMargin = width * 5 / 100;
        mNewRadioBtn.setLayoutParams(radioparams1);

        LinearLayout.LayoutParams radioparams2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        radioparams2.width = width * 50 / 100;
        radioparams2.height = height * 8 / 100;
        radioparams2.leftMargin = width * 3 / 100;
        mExistingRadioBtn.setLayoutParams(radioparams2);

        LinearLayout.LayoutParams accountLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        accountLayoutParama.height = height * 6 / 100;
        accountLayoutParama.topMargin = height * 4 / 100;
        mNewAccountLayout.setLayoutParams(accountLayoutParama);
        mExistingAccountLayout.setLayoutParams(accountLayoutParama);
        mNewAccountLayout.setGravity(Gravity.CENTER);
        mExistingAccountLayout.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams accountTxtParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        accountTxtParama.width = width * 45 / 100;
        accountTxtParama.height = height * 6 / 100;
        mExistingAccount.setLayoutParams(accountTxtParama);
        mNewAccount.setLayoutParams(accountTxtParama);
        mExistingAccount.setGravity(Gravity.CENTER | Gravity.LEFT);
        mNewAccount.setGravity(Gravity.CENTER | Gravity.LEFT);

        LinearLayout.LayoutParams accountButtonParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        accountButtonParama.width = width * 28 / 100;
        accountButtonParama.height = height * 6 / 100;
        accountButtonParama.leftMargin = width * 3 / 100;
        mExistingAccountButton.setLayoutParams(accountButtonParama);
        mNewAccountButton.setLayoutParams(accountButtonParama);
        mExistingAccountButton.setGravity(Gravity.CENTER);
        mNewAccountButton.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutdatat = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//		layoutdatat.height = (int) (height * 83 / 100);
        layoutdatat.height = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutdatat.gravity = Gravity.CENTER;
        layoutdatat.weight = 2;
//        datalay.setLayoutParams(layoutdatat);

        if (width >= 600) {
            mClearBtn.setTextSize(18);
            mSendBtn.setTextSize(18);
            mShopIDEdit.setTextSize(18);
            mShopNameEdit.setTextSize(18);
            mShopNameText.setTextSize(18);
            mHeader.setTextSize(18);
            mExistingRadioBtn.setTextSize(18);
            mNewRadioBtn.setTextSize(18);
            mShopAddressEdit.setTextSize(18);
            mShopIDText.setTextSize(18);
            mShopAddressText.setTextSize(18);
            mEmailIDEdit.setTextSize(18);
            mEmailIDText.setTextSize(18);
            mMerchantNameText.setTextSize(20);
            mFreebieText.setTextSize(18);
            mUnderLineText.setTextSize(18);
            mExistingAccount.setTextSize(18);
            mNewAccount.setTextSize(18);
            mExistingAccountButton.setTextSize(18);
            mNewAccountButton.setTextSize(18);
            mMerchantWebsiteTxt.setTextSize(15);

        } else if (width > 501 && width < 600) {
            mShopNameEdit.setTextSize(17);
            mShopNameText.setTextSize(17);
            mHeader.setTextSize(17);
            mClearBtn.setTextSize(17);
            mSendBtn.setTextSize(17);
            mShopIDEdit.setTextSize(17);
            mShopAddressEdit.setTextSize(17);
            mShopIDText.setTextSize(17);
            mShopAddressText.setTextSize(17);
            mEmailIDEdit.setTextSize(17);
            mEmailIDText.setTextSize(17);
            mExistingRadioBtn.setTextSize(17);
            mNewRadioBtn.setTextSize(17);
            mMerchantNameText.setTextSize(19);
            mFreebieText.setTextSize(17);
            mUnderLineText.setTextSize(17);
            mExistingAccount.setTextSize(17);
            mNewAccount.setTextSize(17);
            mExistingAccountButton.setTextSize(17);
            mNewAccountButton.setTextSize(17);
            mMerchantWebsiteTxt.setTextSize(14);

        } else if (width > 260 && width < 500) {
            mShopNameEdit.setTextSize(16);
            mShopNameText.setTextSize(16);
            mHeader.setTextSize(16);
            mClearBtn.setTextSize(16);
            mSendBtn.setTextSize(16);
            mShopIDEdit.setTextSize(16);
            mShopAddressEdit.setTextSize(16);
            mShopIDText.setTextSize(16);
            mShopAddressText.setTextSize(16);
            mEmailIDEdit.setTextSize(16);
            mEmailIDText.setTextSize(16);
            mExistingRadioBtn.setTextSize(16);
            mNewRadioBtn.setTextSize(16);
            mMerchantNameText.setTextSize(18);
            mFreebieText.setTextSize(16);
            mUnderLineText.setTextSize(16);
            mExistingAccount.setTextSize(16);
            mNewAccount.setTextSize(16);
            mExistingAccountButton.setTextSize(16);
            mNewAccountButton.setTextSize(16);
            mMerchantWebsiteTxt.setTextSize(13);
        } else if (width <= 260) {
            mShopNameEdit.setTextSize(15);
            mShopNameText.setTextSize(15);
            mHeader.setTextSize(15);
            mClearBtn.setTextSize(15);
            mSendBtn.setTextSize(15);
            mShopIDEdit.setTextSize(15);
            mShopAddressEdit.setTextSize(15);
            mShopIDText.setTextSize(15);
            mShopAddressText.setTextSize(15);
            mEmailIDEdit.setTextSize(15);
            mEmailIDText.setTextSize(15);
            mExistingRadioBtn.setTextSize(15);
            mNewRadioBtn.setTextSize(15);
            mMerchantNameText.setTextSize(17);
            mFreebieText.setTextSize(15);
            mUnderLineText.setTextSize(15);
            mExistingAccount.setTextSize(15);
            mNewAccount.setTextSize(15);
            mExistingAccountButton.setTextSize(15);
            mNewAccountButton.setTextSize(15);
            mMerchantWebsiteTxt.setTextSize(12);
        }
    }

    private class GetAddressTask extends AsyncTask<Double, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(VerifyShop.this,
                    android.R.style.Theme_Holo_Light_Dialog);
            progressDialog.setMessage("Please Wait...");
            //progressDialog.setProgressDrawable(new ColorDrawable(
            //        Color.BLUE));
            progressDialog.setCancelable(true);
            //progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

        }

        /**
         * Get a Geocoder instance, get the latitude and longitude look up the
         * address, and return it
         *
         * @return A string containing the address of the current location, or
         * an empty string if no address can be found, or an error
         * message
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
            Constant.printMsg("verify shop asyn...1......");

            try {
                Constant.printMsg("verify shop asyn...2......");

                addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);

                Constant.printMsg("verify shop asyn...3......" + mSendBtn.getText().toString());
                if (mSendBtn.getText().toString().trim()
                        .equalsIgnoreCase("Register")) {
                    address = addresses.get(0).getAddressLine(0); // If
                    // any
                    // additional
                    // address
                    // line
                    // present
                    // than
                    // only,
                    // check
                    // with
                    // max
                    // available
                    // address
                    // lines
                    // by
                    // getMaxAddressLineIndex()
                    // Constant.printMsg("Prabha address " + address);
                    city = addresses.get(0).getLocality();
                    // Constant.printMsg("Prabha city " + city);
                    state = addresses.get(0).getAdminArea();
                    // Constant.printMsg("Prabha state " + state);
                    country = addresses.get(0).getCountryName();
                    // Constant.printMsg("Prabha country " + country);
                    postalCode = addresses.get(0).getPostalCode();
                    // Constant.printMsg("Prabha postalCode " + postalCode);
                    // String knownName = addresses.get(0).getFeatureName();
                    //
                    // Constant.printMsg("Prabha knownName " + knownName);

                    // Bundle extras = addresses.get(0).getExtras();

                    // Constant.printMsg("Prabha extras " + extras);
                    // String featureName = addresses.get(0).getFeatureName();
                    //
                    // Constant.printMsg("Prabha featureName " + featureName);
                    // String subAdminArea = addresses.get(0).getSubAdminArea();
                    //
                    // Constant.printMsg("Prabha subAdminArea " +
                    // subAdminArea);
                    subLocality = addresses.get(0).getSubLocality();

                    // Constant.printMsg("Prabha subLocality " + subLocality);

                }

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

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Constant.printMsg("verify shop asyn...result......" + result);
            // sourceedit.setText(result);
            try {
                progressDialog.dismiss();
                if (result != null && !result.isEmpty()) {
                    if (!result.equals("IO Exception trying to get address")
                            || !result.equalsIgnoreCase("No address found")) {
                        mShopAddressEdit.setText((result.equals("null") ? "No Data"
                                : result));
                        if (mSendBtn.getText().toString().trim()
                                .equalsIgnoreCase("Register")) {
                            mAddsEdt1.setText(address);
                            mAddsEdt2.setText(subLocality);
                            mAddsEdtCity.setText(city);
                            mAddsEdtState.setText(state);
                            mAddsEdtCountry.setText(country);
                            mAddsEdtPincode.setText(postalCode);

                            Constant.printMsg("demo address::" + city + "  "
                                    + state + "  " + country + "  " + postalCode);
                        }
                    }
                } else {
                    Toast empty_fav = Toast.makeText(getApplicationContext(),
                            "Please Try Again", Toast.LENGTH_LONG);
                    empty_fav.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class RegisterShopData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(VerifyShop.this,
                    AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setProgressDrawable(new ColorDrawable(
                    Color.BLUE));
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            countDownPopup();

        }

        @Override
        protected String doInBackground(String... params) {
            String result;
            HttpConfig ht = new HttpConfig();

            Constant.printMsg("input data is :::::" + params[0]
                    + ": and url is ::::" + KachingMeConfig.REDEEM_SHOP_REGISTER);
            String d = params[0];
            result = ht
                    .doPostMobizee(params[0], KachingMeConfig.REDEEM_SHOP_REGISTER);
            // ht.httpget("");
            return result.trim();

        }

        protected void onProgressUpdate(String... progress) {
        }

        protected void onPostExecute(String result) {
            // sourceedit.setText(result);
            Constant.printMsg("Check shop data result :::" + result);
            progressDialog.dismiss();
            // cancelAllTimers();
            // dialog.dismiss();

            if (result != null && result.length() > 0) {
                if (result.equalsIgnoreCase("Error on creating shop")) {
                    Toast.makeText(getApplicationContext(),
                            "Failure in Redeemer registration",
                            Toast.LENGTH_SHORT).show();
                } else {
                    mSendBtn.setText("Redeem");
                    mShopIDEdit.setText(result.trim());
                    mShopIDEdit.setVisibility(View.GONE);
                    mShopIDText.setVisibility(View.GONE);
                    mEmailIDEdit.setVisibility(View.GONE);
                    mEmailIDText.setVisibility(View.GONE);

                    mUnderLineText.setText(
                            Html.fromHtml(settext("New Account")),
                            TextView.BufferType.SPANNABLE);
                    screenArrange();
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Failure in Redeemer registration", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }

    private class VerifyRedeemData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(VerifyShop.this,
                    AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setProgressDrawable(new ColorDrawable(
                    Color.BLUE));
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            countDownPopup();

        }

        @Override
        protected String doInBackground(String... params) {
            String result;
            HttpConfig ht = new HttpConfig();
            // ht.httpget("");

            Constant.printMsg("Redeem pos data ::::" + params[0]
                    + " url is ::::" + KachingMeConfig.REDEEM_POST_URL);

            result = ht.doPostMobizee(params[0], KachingMeConfig.REDEEM_POST_URL);
            return result.trim();

        }

        protected void onProgressUpdate(String... progress) {
        }

        protected void onPostExecute(String result) {
            // sourceedit.setText(result);
            Constant.printMsg("Check shop data result :::" + result);
            progressDialog.dismiss();

            // cancelAllTimers();
            // dialog.dismiss();

            if (result != null && result.length() > 0) {
                if (result.equalsIgnoreCase("Repository Error")) {
                    Intent i = new Intent(getApplicationContext(),
                            Verified_Activity.class);
                    i.putExtra("is_success", "Failure");
                    i.putExtra("failure_msg",
                            "Oops! Failure.Try again after some time");
                    startActivity(i);
                } else if (result.equalsIgnoreCase("Not a valid FreeBee")) {
                    Intent i = new Intent(getApplicationContext(),
                            Verified_Activity.class);
                    i.putExtra("is_success", "Failure");
                    i.putExtra("failure_msg", "Oops! your FreeBee is not valid");
                    startActivity(i);
                } else if (result.equalsIgnoreCase("FreeBee Expired")) {
                    Intent i = new Intent(getApplicationContext(),
                            Verified_Activity.class);
                    i.putExtra("is_success", "Failure");
                    i.putExtra("failure_msg",
                            "Oops! your FreeBee has been expired");
                    startActivity(i);
                } else if (result.equalsIgnoreCase("Items not in Cart")) {
                    Intent i = new Intent(getApplicationContext(),
                            Verified_Activity.class);
                    i.putExtra("is_success", "Failure");
                    i.putExtra("failure_msg",
                            "Oops! already your FreeBee has been claimed");
                    startActivity(i);
                } else if (result.equalsIgnoreCase("Redeemed Successfully")) {
                    Intent i = new Intent(getApplicationContext(),
                            Verified_Activity.class);
                    i.putExtra("is_success", "Success");
                    i.putExtra("failure_msg",
                            "Your FreeBee has been successfully claimed.");
                    startActivity(i);
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Oops! Something went wrong.Please try again",
                        Toast.LENGTH_SHORT).show();
            }

            // startActivity(new Intent(VerifyShop.this,
            // Verified_Activity.class));
            // finish();
            // Toast.makeText(getApplicationContext(), "Redeem Failure",
            // Toast.LENGTH_SHORT).show();
            // Intent i = new Intent(getApplicationContext(),
            // Verified_Activity.class);
            // i.putExtra("is_success", "Success");
            // i.putExtra("failure_msg", "Successfully Redeemed");
            // startActivity(i);

            // mSendBtn.setText("Register");
            //
            // mShopIDEdit.setVisibility(View.GONE);
            // mShopIDText.setVisibility(View.VISIBLE);
            // if (result != null && !result.isEmpty()) {
            //
            // } else {
            // Toast empty_fav = Toast.makeText(getApplicationContext(),
            // "Please Try Again", Toast.LENGTH_LONG);
            // empty_fav.show();
            // }
        }
    }

    private class getShopData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(VerifyShop.this,
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
                ShopDetailsDto shopResponse;
                Gson g = new Gson();

                shopResponse = g.fromJson(result.trim(), ShopDetailsDto.class);

                if (shopResponse.getShopId() != null) {
                    mShopIDEdit
                            .setText(String.valueOf(shopResponse.getShopId()));
                    mEmailIDEdit.setVisibility(View.GONE);
                    mEmailIDText.setVisibility(View.GONE);
                    mShopIDEdit.setVisibility(View.GONE);
                    mShopIDText.setVisibility(View.GONE);
                    // mUnderLineText.setText("New account");
                    mSendBtn.setText("Redeem");

                    mUnderLineText.setText(
                            Html.fromHtml(settext("New Account")),
                            TextView.BufferType.SPANNABLE);
                    screenArrange();
                } else {
                    mEmailIDEdit.setVisibility(View.GONE);
                    mEmailIDText.setVisibility(View.GONE);
                    mShopIDEdit.setVisibility(View.GONE);
                    mShopIDText.setVisibility(View.GONE);
                    mUnderLineText.setText("Already have account");
                    mSendBtn.setText("Register");

                    mUnderLineText.setText(
                            Html.fromHtml(settext("Already have account")),
                            TextView.BufferType.SPANNABLE);
                    screenArrange();
                }

                if (shopResponse.getAddress() != null
                        && shopResponse.getAddress().length() > 0) {
                    mShopAddressEdit.setText(shopResponse.getAddress().trim());
                }
            } else {
                mEmailIDEdit.setVisibility(View.GONE);
                mEmailIDText.setVisibility(View.GONE);
                mShopIDEdit.setVisibility(View.GONE);
                mShopIDText.setVisibility(View.GONE);
                // mUnderLineText.setText("Already have account");
                mSendBtn.setText("Register");
                String styledText = "<u><font color='blue'>"
                        + "Already have account" + "</font></u>";

                mUnderLineText.setText(Html.fromHtml(styledText),
                        TextView.BufferType.SPANNABLE);
                screenArrange();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 1004:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Constant.printMsg("Permission Granted");
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Allow Permission to Access", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
