package com.wifin.kachingme.redimer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.deals.VerifyShop;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.pojo.CountryCodeGetSet;
import com.wifin.kachingme.pojo.RestClaimDto;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.Validation;
import com.wifin.kachingme.util.CircleProgressBar;
import com.wifin.kachingme.util.GPSTracking;
import com.wifin.kachingme.util.XMLParser;
import com.wifin.kaching.me.ui.R;

public class NewAccount extends HeaderActivity implements OnClickListener {

    TextView $ShopName, $Adress1, $Address2, $Address3, $City, $State, $Country,
            $PostalCode, $EmailId, $HeadLable, $PhoneNumber,$CountryCode,$CountrySeparator;
    EditText $ShopNameEdit, $Adress1Edit, $Address2Edit, $Address3Edit, $CityEdit, $StateEdit,
            $CountryEdit, $PostalCodEEdit, $EmailIdEdit, $PhoneNumberEdit;
    Button $Submit;
    String city, state, country, postalCode, subLocality, address,
            countryCode;

    private LocationManager mLocationManager;
    // if GPS is enabled
    boolean isGPSEnabled = false;
    // if Network is enabled
    boolean isNetworkEnabled = false;
    double mLatitude = 0, mLongitude = 0;
    Dialog dialog;
    public static HashMap<TextView, CountDownTimer> counters;
    SharedPreferences sp;
    ArrayList<CountryCodeGetSet> country_list;

    int height, width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.new_account_redeemer, vg);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mHeading.setText("Register Redeemer");
        mNextBtn.setVisibility(View.INVISIBLE);
        mHeaderImg.setVisibility(View.GONE);
        mFooterLayout.setVisibility(View.GONE);

        // getWindow().setSoftInputMode(
        // WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

//        TelephonyManager tm = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
        newAccountInitialize();
        newAccountScreenArrange();
        InputMethodManager ipmm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ipmm.hideSoftInputFromWindow($PostalCodEEdit.getWindowToken(), 0);
//		head.setText("Register Redeemer");
//		logo.setVisibility(ImageView.INVISIBLE);
//		back.setVisibility(ImageView.VISIBLE);
//		sideMenufoot.setVisibility(LinearLayout.GONE);
//		head.setTextColor(Color.parseColor("#FFFFFF"));
//		Ka_newlogo.setVisibility(ImageView.INVISIBLE);
//		back.setBackgroundResource(R.drawable.arrow);
//		headlay.setBackgroundColor(Color.parseColor("#FE0000"));


        mLocationManager = (LocationManager) getApplicationContext()
                .getSystemService(LOCATION_SERVICE);
        isGPSEnabled = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        GPSTracking mGPSService = new GPSTracking(NewAccount.this);
        mBackBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(NewAccount.this, VerifyShop.class));
                finish();
            }
        });
//        Constant.printMsg("Gps............"+isGPSEnabled+"....Network........."+isNetworkEnabled);
//        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if(location==null){
//            Constant.printMsg("Gps Locatopn...1........."+location);
//            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            Constant.printMsg("Gps Locatopn....2........"+location);
//        }else{
//            Constant.printMsg("Gps Locatopn.....3......."+location);
//            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            Constant.printMsg("Gps Locatopn.....4......."+location);
//        }

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
            // mLatitude = 0.0;
            // mLongitude = 0.0;
            new GetAddressTask().execute();
            mGPSService.stopUsingGPS();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Location Service is not active.", Toast.LENGTH_SHORT)
                    .show();
            // mGPSService.showSettingsAlert();
        }

    }

    private void newAccountInitialize() {
        // TODO Auto-generated method stub
        $ShopName = (TextView) findViewById(R.id.newAccount_shopName);
        $Adress1 = (TextView) findViewById(R.id.newAccount_addressLine1);
        $Address2 = (TextView) findViewById(R.id.newAccount_addressLine2);
        $Address3 = (TextView) findViewById(R.id.newAccount_addressLine3);
        $City = (TextView) findViewById(R.id.newAccount_city);
        $State = (TextView) findViewById(R.id.newAccount_state);
        $Country = (TextView) findViewById(R.id.newAccount_country);
        $PostalCode = (TextView) findViewById(R.id.newAccount_postalCode);
        $EmailId = (TextView) findViewById(R.id.newAccount_emailId);
        $HeadLable = (TextView) findViewById(R.id.newAccount_heading);
        $PhoneNumber = (TextView) findViewById(R.id.newAccount_phonenumber);
        $CountryCode = (TextView) findViewById(R.id.newAccount_country_code);
        $CountrySeparator = (TextView) findViewById(R.id.newAccount_seperator);

        $ShopNameEdit = (EditText) findViewById(R.id.newAccount_shopNameEdit);
        $Adress1Edit = (EditText) findViewById(R.id.newAccount_addressLine1Edit);
        $Address2Edit = (EditText) findViewById(R.id.newAccount_addressLine2Edit);
        $Address3Edit = (EditText) findViewById(R.id.newAccount_addressLine3Edit);
        $CityEdit = (EditText) findViewById(R.id.newAccount_cityEdit);
        $StateEdit = (EditText) findViewById(R.id.newAccount_stateEdit);
        $CountryEdit = (EditText) findViewById(R.id.newAccount_countryEdit);
        $PhoneNumberEdit = (EditText) findViewById(R.id.newAccount_phonenumberEdit);
        $PostalCodEEdit = (EditText) findViewById(R.id.newAccount_postalCodeEdit);
        $EmailIdEdit = (EditText) findViewById(R.id.newAccount_emailIdEdit);
        $Submit = (Button) findViewById(R.id.newAccount_submitButton);

        $CountryEdit.setClickable(false);
        $CountryEdit.setFocusable(false);
        $StateEdit.setClickable(false);
        $StateEdit.setFocusable(false);
        $CityEdit.setClickable(false);
        $CityEdit.setFocusable(false);

        Constant.typeFace(this, $ShopName);
        Constant.typeFace(this, $Adress1);
        Constant.typeFace(this, $Address2);
        Constant.typeFace(this, $City);
        Constant.typeFace(this, $State);
        Constant.typeFace(this, $Country);
        Constant.typeFace(this, $PostalCode);
        Constant.typeFace(this, $EmailId);
        Constant.typeFace(this, $HeadLable);
        Constant.typeFace(this, $PhoneNumber);
        Constant.typeFace(this, $ShopNameEdit);
        Constant.typeFace(this, $Adress1Edit);
        Constant.typeFace(this, $Address2Edit);
        Constant.typeFace(this, $Address3Edit);
        Constant.typeFace(this, $CityEdit);
        Constant.typeFace(this, $StateEdit);
        Constant.typeFace(this, $CountryEdit);
        Constant.typeFace(this, $PostalCodEEdit);
        Constant.typeFace(this, $EmailIdEdit);
        Constant.typeFace(this, $PhoneNumberEdit);
        Constant.typeFace(this, $Submit);
        Constant.typeFace(this, $Address3);
        Constant.typeFace(this, $CountryCode);
        Constant.typeFace(this, $CountrySeparator);

        $Submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.newAccount_submitButton:
                if (validateRegister()) {
                    loadRegisterData();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please fill all the details", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    public boolean validateRegister() {
        boolean isValid = true;
        if (!Validation.hasText($ShopNameEdit))
            isValid = false;
        if (!Validation.hasText($Adress1Edit))
            isValid = false;
        if (!Validation.hasText($PhoneNumberEdit))
            isValid = false;
        if (!Validation.hasText($CityEdit))
            isValid = false;
        if (!Validation.hasText($StateEdit))
            isValid = false;
        if (!Validation.hasText($CountryEdit))
            isValid = false;
        if (!Validation.hasText_1($PostalCodEEdit))
            isValid = false;
        if (!Validation.isEmailAddress($EmailIdEdit, true))
            isValid = false;
        return isValid;
    }

    public void loadRegisterData() {

        RestClaimDto re = new RestClaimDto();
        Long point = sp.getLong("buxvalue", 0);

        Constant.printMsg("bux    " + point);
        re.setBux(point); // String ctNum;
        // if (Constant.mPhoneNum.contains("IN")) {
        // ctNum = Constant.mPhoneNum.replace("IN", "");
        // } else {
        // ctNum = Constant.mPhoneNum;
        // }

        re.setPhoneNumber(KachingMeApplication.getjid().split("@")[0]);

        // re.setPhoneNumber("912345677892");
        if (Constant.mFreebieId != null && !Constant.mFreebieId.isEmpty()) {
            re.setOfferId(Integer.valueOf(Constant.mFreebieId));
        }
        // re.setShopId(Integer.valueOf(mShopIDEdit.getText().toString().trim()));
        if (Constant.mCartType != null && !Constant.mCartType.isEmpty()) {
            re.setType(Integer.valueOf(Constant.mCartType));
        }
        re.setCountryName($CountryEdit.getText().toString().trim());
        re.setName($ShopNameEdit.getText().toString().trim());
        re.setEmail($EmailIdEdit.getText().toString().trim());
        re.setLat(mLatitude);
        re.setLon(mLongitude);
        re.setShopPhoneNumber(countryCode + $PhoneNumberEdit.getText().toString().trim());
        String mFinalAdds = null;

        if ($Address2Edit.getText().toString().trim().length() > 0
                && $Address3Edit.getText().toString().trim().length() > 0) {
            Constant.printMsg("check address......both");
            mFinalAdds = $Adress1Edit.getText().toString().trim() + ","
                    + $Address2Edit.getText().toString().trim() + ","
                    + $Address3Edit.getText().toString().trim() + ","
                    + $CityEdit.getText().toString().trim() + ","
                    + $StateEdit.getText().toString().trim() + ","
                    + $CountryEdit.getText().toString().trim() + ","
                    + $PostalCodEEdit.getText().toString().trim();
        } else {
            if ($Address2Edit.getText().toString().trim().length() > 0) {
                mFinalAdds = $Adress1Edit.getText().toString().trim() + ","
                        + $Address2Edit.getText().toString().trim() + ","
                        + $CityEdit.getText().toString().trim() + ","
                        + $StateEdit.getText().toString().trim() + ","
                        + $CountryEdit.getText().toString().trim() + ","
                        + $PostalCodEEdit.getText().toString().trim();
            } else {
                if ($Address3Edit.getText().toString().trim().length() > 0) {
                    mFinalAdds = $Adress1Edit.getText().toString().trim() + ","
                            + $Address3Edit.getText().toString().trim() + ","
                            + $CityEdit.getText().toString().trim() + ","
                            + $StateEdit.getText().toString().trim() + ","
                            + $CountryEdit.getText().toString().trim() + ","
                            + $PostalCodEEdit.getText().toString().trim();
                } else {
                    mFinalAdds = $Adress1Edit.getText().toString().trim() + ","
                            + $CityEdit.getText().toString().trim() + ","
                            + $StateEdit.getText().toString().trim() + ","
                            + $CountryEdit.getText().toString().trim() + ","
                            + $PostalCodEEdit.getText().toString().trim();
                }
            }
        }

        re.setAddress(mFinalAdds);
        String mRegisterData = new Gson().toJson(re);
        Constant.printMsg("new redeemer dataaaa   " + mRegisterData);
        new RegisterShopData().execute(mRegisterData);

    }
    /*Added to get Country code by siva on 15/11/2016*/
    public String getCountryCode(String countryName) {
        String[] isoCountryCodes = Locale.getISOCountries();
        Map<String, String> countryMap = new HashMap<>();
        for (String code : isoCountryCodes) {
            Locale locale = new Locale("", code);
            String name = locale.getDisplayCountry();
            countryMap.put(name, code);
        }
        String countryCode = countryMap.get(countryName);
        return countryCode;
    }

    private class GetAddressTask extends AsyncTask<Double, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NewAccount.this,
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
         * an empty string if no address can be found, or an error
         * message
         * @params params One or more Location objects
         */
        @Override
        protected String doInBackground(Double... params) {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);
                Constant.printMsg("res........... " + addresses.size());
                Constant.printMsg("res.....add....... " + addresses);
                if (addresses.size() > 0) {
                    Constant.printMsg("res.....test 1....... " + addresses.get(0));
                    Constant.printMsg("res.....test 2....... " + addresses.get(0).getAddressLine(0));
                    Constant.printMsg("res.....test 3....... " + addresses.get(0).getLocality() );
                    Constant.printMsg("res.....test 4....... " + addresses.get(0).getAdminArea());
                    Constant.printMsg("res.....test 5....... " + addresses.get(0).getCountryName());
                    Constant.printMsg("res.....test 6....... " + addresses.get(0).getPostalCode());
                    Constant.printMsg("res.....test 7....... " + addresses.get(0).getSubLocality());
                    Constant.printMsg("res.....test 8....... " + addresses.get(0).getAddressLine(1));
//                    Constant.printMsg("res.....test 9....... " + addresses.get(0));
//                    Constant.printMsg("res.....test 10....... " + addresses.get(0));
//                    Constant.printMsg("res.....test 11....... " + addresses.get(0));
//                    Constant.printMsg("res.....test 12....... " + addresses.get(0));

                    if (addresses.get(0).getAddressLine(0)!=null) {
                        address = addresses.get(0).getAddressLine(0);
                    }
                    if (addresses.get(0).getLocality() != null) {
                        city = addresses.get(0).getLocality();
                    }

                    if (addresses.get(0).getAdminArea()!=null) {
                        state = addresses.get(0).getAdminArea();
                    }

                    if (addresses.get(0).getCountryName()!=null) {
                        country = addresses.get(0).getCountryName();
                    }

                    if (addresses.get(0).getPostalCode()!=null) {
                        postalCode = addresses.get(0).getPostalCode();
                    }

                    if (addresses.get(0).getAddressLine(1)!=null) {
                        subLocality = addresses.get(0).getAddressLine(1);
                    }else{
                        if (addresses.get(0).getSubLocality()!=null) {
                            subLocality = addresses.get(0).getSubLocality();
                        }
                    }
                }
                Constant.printMsg("res.... " + address + "   " + subLocality
                        + "  " + city + "  " + country + "   " + postalCode);

            } catch (IOException e1) {
                e1.printStackTrace();
                Constant.printMsg("res.....e1....... " + e1);
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments "
                        + Double.toString(params[0]) + " , "
                        + Double.toString(params[1])
                        + " passed to address service";
                e2.printStackTrace();
                Constant.printMsg("res.....e2....... " + e2);
                return errorString;
            } catch (NullPointerException np) {
                // TODO Auto-generated catch block
                np.printStackTrace();
                Constant.printMsg("res.....np....... " + np);
            }
            Constant.printMsg("res.....reverse geocode....... " + addresses);
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);
                /*
				 * Format the first line of address (if available), city, and
				 * country name.
				 */
                Constant.printMsg("res.....for aadd....... " + address);
                String addressText = null;
                StringBuffer addr = new StringBuffer();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {

                    addressText = address.getAddressLine(i);
                    Constant.printMsg("res..... addressText....... " + addressText);
                    if (i == address.getMaxAddressLineIndex()) {
                        addr.append(addressText);
                    } else {
                        addr.append(addressText + ",");
                    }

                }
                // Return the text
                return addr.toString();
            } else {
                Constant.printMsg("res..... No address found....... ");
                return "No address found";
            }
        }

        protected void onProgressUpdate(String... progress) {
        }

        protected void onPostExecute(String result) {
            // sourceedit.setText(result);
            progressDialog.dismiss();

            Constant.printMsg("the result for adds is :::" + result);

            if (result != null && !result.isEmpty()) {
                if (!result.equals("IO Exception trying to get address")
                        || !result.equalsIgnoreCase("No address found")) {
                    if (address != null) {
                        $Adress1Edit.setText(address);
                    }
                    if (subLocality != null) {
                        $Address2Edit.setText(subLocality);
                    }
                    if (city != null) {
                        $CityEdit.setText(city);
                        Constant.printMsg("HIIIIII 11111 up");
                    } else {
                        $CityEdit.setClickable(true);
                        $CityEdit.setFocusable(true);
                        $CityEdit.setFocusableInTouchMode(true);
                        Constant.printMsg("HIIIIII 11111 down");

                    }
                    if (state != null) {
                        $StateEdit.setText(state);
                    } else {
                        $StateEdit.setClickable(true);
                        $StateEdit.setFocusable(true);
                        $StateEdit.setFocusableInTouchMode(true);
                    }
                    if (country != null) {
                        $CountryEdit.setText(country);
                    } else {
                        $CountryEdit.setClickable(true);
                        $CountryEdit.setFocusable(true);
                        $CountryEdit.setFocusableInTouchMode(true);
                    }
                    if (postalCode != null) {
                        $PostalCodEEdit.setText(postalCode);
                    }
                    // $CountryEdit.setFocusable(true);
                    // $CountryEdit.setFocusableInTouchMode(true);
                    // $CountryEdit.setClickable(true);
                    Constant.printMsg("HIIIIII 11111");
                } else {

                    $CityEdit.setFocusable(true);
                    $CityEdit.setFocusableInTouchMode(true);
                    $CityEdit.setClickable(true);

                    $StateEdit.setFocusable(true);
                    $StateEdit.setFocusableInTouchMode(true);
                    $StateEdit.setClickable(true);

                    $CountryEdit.setFocusable(true);
                    $CountryEdit.setFocusableInTouchMode(true);
                    $CountryEdit.setClickable(true);

                    Constant.printMsg("HIIIIII 22222");
                }
                if (country != null) {
                    country_list = new ArrayList<CountryCodeGetSet>();
                    country_list = getCountry();
                    String locale = getCountryCode(country);
                    Constant.printMsg("country test....."+getCountryCode(country));
                    for (int i = 0; i < country_list.size(); i++) {

                        String name = country_list.get(i).getCountry_Code().toString();
                        Constant.printMsg("country  " + name + "  " + locale);
                        if (name.equalsIgnoreCase(locale)) {
                            Constant.printMsg("country123  "
                                    + country_list.get(i).getCode().toString());
                            countryCode = country_list.get(i).getCode().toString();
                        }
                    }
                    Constant.printMsg("code set on country code....."+countryCode);
                    if (countryCode!=null && !countryCode.isEmpty()){
                        $CountryCode.setText(countryCode);
                    }
                }
            } else {
                // $CityEdit.setFocusable(true);
                // $CityEdit.setFocusableInTouchMode(true);
                // $CityEdit.setClickable(true);
                //
                // $StateEdit.setFocusable(true);
                // $StateEdit.setFocusableInTouchMode(true);
                // $StateEdit.setClickable(true);
                //
                // $CountryEdit.setFocusable(true);
                // $CountryEdit.setFocusableInTouchMode(true);
                // $CountryEdit.setClickable(true);
                Constant.printMsg("HIIIIII 33333");
                Toast.makeText(getApplicationContext(),
                        "Please Try Again", Toast.LENGTH_LONG).show();
            }
            // Constant.printMsg("city  " + city);
            // if (city == "null" && city.length() == 0) {
            // // $CityEdit.setFocusable(true);
            // $CityEdit.setFocusable(true);
            // $CityEdit.setEnabled(true);
            // $CityEdit.setFocusableInTouchMode(true);
            // // $CityEdit.setClickable(true);
            // }
            //
            // if (state == null || state.length() == 0) {
            // // $StateEdit.setFocusable(true);
            // $StateEdit.setFocusable(true);
            // $StateEdit.setEnabled(true);
            // $StateEdit.setFocusableInTouchMode(true);
            // // $StateEdit.setClickable(true);
            // }
            // if (country == null || country.length() == 0) {
            // Constant.printMsg("HIIIIII 44444");
            // $CountryEdit.setFocusable(true);
            // $CountryEdit.setEnabled(true);
            // $CountryEdit.setFocusableInTouchMode(true);
            // // $CountryEdit.reques
            //
            // }

        }
    }

    private class RegisterShopData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NewAccount.this,
                    AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setProgressDrawable(new ColorDrawable(
                    Color.BLUE));
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            // progressDialog.show();

            countDownPopup();

        }

        @Override
        protected String doInBackground(String... params) {
            String result;
            HttpConfig ht = new HttpConfig();
            Constant.printMsg("" + params[0]);
            Constant.printMsg("input data is :::::" + params[0]
                    + ": and url is ::::" + KachingMeConfig.REDEEM_POST_URL);
            String d = params[0];
            result = ht.doPostMobizee(params[0], KachingMeConfig.REDEEM_POST_URL);
            // ht.httpget("");
            return result.trim();

        }

        protected void onProgressUpdate(String... progress) {
        }

        protected void onPostExecute(String result) {
            // sourceedit.setText(result);
            Constant.printMsg("Check shop data result :::" + result);
            // progressDialog.dismiss();
            // dialog.dismiss();
            cancelAllTimers();
            dialog.dismiss();

            if (result != null && result.length() > 0) {
                result.trim();
                if (result.equalsIgnoreCase("Error on creating shop")) {

                    showToast("Failure in Redeemer registration.Please try again .");
                } else if (result.equalsIgnoreCase("Repository Error")) {
                    showToast("Failure in Redeemer registration.Please try again");

                } else if (result.equalsIgnoreCase("Not a valid FreeBee")) {
                    Intent i = new Intent(getApplicationContext(),
                            RedeemedSuccessActivity.class);
                    i.putExtra("is_success", "Failure");
                    i.putExtra("failure_msg",
                            "your FreeBee has been expired");
                    startActivity(i);
                } else if (result.equalsIgnoreCase("FreeBee Expired")) {
                    Intent i = new Intent(getApplicationContext(),
                            RedeemedSuccessActivity.class);
                    i.putExtra("is_success", "Failure");
                    i.putExtra("failure_msg",
                            "your FreeBee has been expired");
                    startActivity(i);
                } else if (result.equalsIgnoreCase("Items not in Cart")) {
                    Intent i = new Intent(getApplicationContext(),
                            RedeemedSuccessActivity.class);
                    i.putExtra("is_success", "Failure");
                    i.putExtra("failure_msg",
                            "your FreeBee has been expired");
                    startActivity(i);
                } else if (result.equalsIgnoreCase("Not a valid Shop-Id")) {
                    Intent i = new Intent(getApplicationContext(),
                            RedeemedSuccessActivity.class);
                    i.putExtra("is_success", "Failure");
                    i.putExtra("failure_msg", "Oops! your shop id is not valid");
                    startActivity(i);
                } else if (result.equalsIgnoreCase("Redeemed Successfully")) {
                    Intent i = new Intent(getApplicationContext(),
                            RedeemedSuccessActivity.class);
                    i.putExtra("is_success", "Success");
                    i.putExtra("failure_msg",
                            "You have successfully Redeemed your Freebie.");
                    startActivity(i);
                } else if (result
                        .equalsIgnoreCase("Total FreeBee Count Reached")) {
                    showToast("Sorry! Total Freebie count has reached already");
                } else if (result.equalsIgnoreCase("FreeBee Day Count Reached")) {
                    showToast("Sorry! Daily Freebie count has reached already");
                } else if (result
                        .equalsIgnoreCase("FreeBee Week Count Reached")) {
                    showToast("Sorry! Weekly Freebie count has reached already");
                } else if (result
                        .equalsIgnoreCase("FreeBee Month Count Reached")) {
                    showToast("Sorry! Monthly Freebie count has reached already");
                } else if (result
                        .equalsIgnoreCase("Not applicable for the shop location")) {
                    showToast("your Freebie is not applicable for this location");
                } else if (result
                        .equalsIgnoreCase("Email Already Exists.")) {
                    showToast("Email Already Exists");
                } else if (result.contains(":")) {

                    String[] str_msg_data_array = result.trim().split(":");

                    Intent i = new Intent(getApplicationContext(),
                            RedeemedSuccessActivity.class);
                    i.putExtra("is_success", "Success");
                    i.putExtra("failure_msg",
                            "registered as a KaChing.me Redeemer Your Redeemer ID Is " + str_msg_data_array[1]
                                    + " Please make a note of your Redeemer ID# Please check your email for further instructions Thank you.");
                    startActivity(i);
                    Constant.mFreebieShopId = str_msg_data_array[1];
                }
            } else {
                showToast("Failure in Redeemer registration.Please try again");

            }
        }
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

    private void countDownPopup() {
        // TODO Auto-generated method stub
        counters = new HashMap<TextView, CountDownTimer>();
        dialog = new Dialog(NewAccount.this);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.count_down_timer_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final TextView tvCounter = (TextView) dialog
                .findViewById(R.id.popup_tvCounter);
        final CircleProgressBar cp = (CircleProgressBar) dialog
                .findViewById(R.id.popup_custom_progressBar_adapter);

        final long difference = Long.valueOf(30000);
        new CountDownTimer(difference, 1000) {

            public void onTick(long millisUntilFinished) {
                // tvCounter.setText("seconds remaining: " + millisUntilFinished
                // / 1000);

                // here you can have your logic to set text to edittext

                float diff1 = Float.valueOf(difference);
                float diff2 = Float.valueOf(millisUntilFinished);

                if ((difference - millisUntilFinished) / 1000 != 0) {
                    tvCounter
                            .setText(String
                                    .valueOf((difference - millisUntilFinished) / 1000));

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

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();
        startActivity(new Intent(NewAccount.this, VerifyShop.class));
        finish();
    }

    private void newAccountScreenArrange() {
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
        contentLayoutParams.width = width;
        contentLayoutParams.height = height * 87 / 100;
        contentLayoutParams.gravity = Gravity.CENTER;
//        mContentLayout.setLayoutParams(contentLayoutParams);

        LinearLayout.LayoutParams headLableParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        headLableParama.width = width * 90 / 100;
        // headLableParama.height = height * 7 / 100;
        headLableParama.leftMargin = width * 5 / 100;
        headLableParama.topMargin = height * 2 / 100;
        $HeadLable.setLayoutParams(headLableParama);
        $HeadLable.setGravity(Gravity.CENTER | Gravity.LEFT);
        $HeadLable.setPadding(width * 1 / 100, 0, 0, 0);

        LinearLayout.LayoutParams shopLableParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        shopLableParama.width = width * 90 / 100;
        // shopLableParama.height = height * 7 / 100;
        shopLableParama.leftMargin = width * 5 / 100;
        shopLableParama.rightMargin = width * 5 / 100;
        shopLableParama.topMargin = (int) (height * 2.5 / 100);
        $ShopName.setLayoutParams(shopLableParama);
        $ShopName.setGravity(Gravity.CENTER | Gravity.LEFT);
//		$ShopName.setPadding(width * 1 / 100, 0, 0, 0);

        LinearLayout.LayoutParams countryCodeParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        countryCodeParama.height = (int) (height * 6 / 100);
        countryCodeParama.width = (int) (width * 14 / 100);
        countryCodeParama.leftMargin=width*5/100;
        countryCodeParama.topMargin = height * 1 / 100;
        $CountryCode.setLayoutParams(countryCodeParama);
        $CountryCode.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams separatorParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        separatorParama.height = (int) (height * 6 / 100);
        separatorParama.width = (int) (width * 7 / 100);
        separatorParama.topMargin = height * 1 / 100;
        //separatorParama.setMargins(width*1/100,0,width*1/100,0);
        $CountrySeparator.setLayoutParams(separatorParama);
        $CountrySeparator.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams mobileNoParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mobileNoParama.height = (int) (height * 6 / 100);
        mobileNoParama.width = (int) (width * 69 / 100);
        mobileNoParama.rightMargin=width*5/100;
        mobileNoParama.topMargin = height * 1 / 100;
        $PhoneNumberEdit.setLayoutParams(mobileNoParama);
        $PhoneNumberEdit.setGravity(Gravity.CENTER | Gravity.LEFT);
        $PhoneNumberEdit.setPadding(width * 2 / 100, 0, 0, 0);

        LinearLayout.LayoutParams lableParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lableParama.width = width * 90 / 100;
        // lableParama.height = height * 8 / 100;
        lableParama.leftMargin = width * 5 / 100;
        lableParama.rightMargin = width * 5 / 100;
        lableParama.topMargin = (int) (height * 2.5 / 100);
        $Adress1.setLayoutParams(lableParama);
        $Address2.setLayoutParams(lableParama);
        $Address3.setLayoutParams(lableParama);
        $City.setLayoutParams(lableParama);
        $State.setLayoutParams(lableParama);
        $Country.setLayoutParams(lableParama);
        $PostalCode.setLayoutParams(lableParama);
        $EmailId.setLayoutParams(lableParama);
        $PhoneNumber.setLayoutParams(lableParama);

        $Adress1.setGravity(Gravity.CENTER | Gravity.LEFT);
        $Address2.setGravity(Gravity.CENTER | Gravity.LEFT);
        $Address3.setGravity(Gravity.CENTER | Gravity.LEFT);
        $City.setGravity(Gravity.CENTER | Gravity.LEFT);
        $State.setGravity(Gravity.CENTER | Gravity.LEFT);
        $Country.setGravity(Gravity.CENTER | Gravity.LEFT);
        $PostalCode.setGravity(Gravity.CENTER | Gravity.LEFT);
        $EmailId.setGravity(Gravity.CENTER | Gravity.LEFT);
        $PhoneNumber.setGravity(Gravity.CENTER | Gravity.LEFT);

//		$Adress1.setPadding(width * 1 / 100, 0, 0, 0);
//		$Address2.setPadding(width * 1 / 100, 0, 0, 0);
//		$City.setPadding(width * 1 / 100, 0, 0, 0);
//		$State.setPadding(width * 1 / 100, 0, 0, 0);
//		$Country.setPadding(width * 1 / 100, 0, 0, 0);
//		$PostalCode.setPadding(width * 1 / 100, 0, 0, 0);
//		$EmailId.setPadding(width * 1 / 100, 0, 0, 0);
//		$PhoneNumber.setPadding(width * 1 / 100, 0, 0, 0);

        LinearLayout.LayoutParams addEditParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        addEditParama.width = width * 90 / 100;
        // addEditParama.height = (int) (height * 7 / 100);
        addEditParama.leftMargin = width * 5 / 100;
        addEditParama.rightMargin = width * 5 / 100;
        addEditParama.topMargin = height * 1 / 100;
        $Adress1Edit.setLayoutParams(addEditParama);
        $Address2Edit.setLayoutParams(addEditParama);
        $Address3Edit.setLayoutParams(addEditParama);

        $Adress1Edit.setGravity(Gravity.CENTER | Gravity.LEFT);
        $Address2Edit.setGravity(Gravity.CENTER | Gravity.LEFT);
        $Address3Edit.setGravity(Gravity.CENTER | Gravity.LEFT);

        $Adress1Edit.setPadding(width * 2 / 100, height * 1 / 100,
                width * 1 / 100, height * 1 / 100);
        $Address2Edit.setPadding(width * 2 / 100, height * 1 / 100,
                width * 1 / 100, height * 1 / 100);
        $Address3Edit.setPadding(width * 2 / 100, height * 1 / 100,
                width * 1 / 100, height * 1 / 100);

        LinearLayout.LayoutParams editParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editParama.width = width * 90 / 100;
        editParama.height = (int) (height * 7 / 100);
        editParama.leftMargin = width * 5 / 100;
        editParama.rightMargin = width * 5 / 100;
        editParama.topMargin = height * 1 / 100;
        $ShopNameEdit.setLayoutParams(editParama);
        $CountryEdit.setLayoutParams(editParama);
        $CityEdit.setLayoutParams(editParama);
        $StateEdit.setLayoutParams(editParama);
        $EmailIdEdit.setLayoutParams(editParama);

        $ShopNameEdit.setGravity(Gravity.CENTER | Gravity.LEFT);
        $CountryEdit.setGravity(Gravity.CENTER | Gravity.LEFT);
        $CityEdit.setGravity(Gravity.CENTER | Gravity.LEFT);
        $StateEdit.setGravity(Gravity.CENTER | Gravity.LEFT);
        $EmailIdEdit.setGravity(Gravity.CENTER | Gravity.LEFT);

        $ShopNameEdit.setPadding(width * 2 / 100, 0, width * 1 / 100, 0);
        $CountryEdit.setPadding(width * 2 / 100, 0, width * 1 / 100, 0);
        $CityEdit.setPadding(width * 2 / 100, 0, width * 1 / 100, 0);
        $StateEdit.setPadding(width * 2 / 100, 0, width * 1 / 100, 0);
        $EmailIdEdit.setPadding(width * 2 / 100, 0, width * 1 / 100, 0);

        LinearLayout.LayoutParams editParama1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editParama1.width = width * 90 / 100;
        editParama1.height = (int) (height * 7 / 100);
        editParama1.leftMargin = width * 5 / 100;
        editParama1.rightMargin = width * 5 / 100;
        editParama1.topMargin = height * 1 / 100;
        editParama1.bottomMargin = height * 1 / 100;
        $PostalCodEEdit.setLayoutParams(editParama1);
        $PostalCodEEdit.setGravity(Gravity.CENTER | Gravity.LEFT);
        $PostalCodEEdit.setPadding(width * 2 / 100, 0, width * 1 / 100, 0);

        LinearLayout.LayoutParams submitParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        submitParama.width = width * 50 / 100;
        submitParama.height = height * 7 / 100;
        submitParama.gravity = Gravity.CENTER;
        submitParama.topMargin = height * 7 / 100;
        submitParama.bottomMargin = height * 3 / 100;
        $Submit.setLayoutParams(submitParama);
        $Submit.setGravity(Gravity.CENTER);
//		Drawable icon = getResources().getDrawable(R.drawable.clock);
//		icon.setBounds(0, 0, (int) (icon.getIntrinsicWidth() * 0.2),
//				(int) (icon.getIntrinsicHeight() * 0.2));
//		// ScaleDrawable sd = new ScaleDrawable(icon, 0, 0, 0);
//		$Submit.setCompoundDrawables(icon, null, null, null);

        LinearLayout.LayoutParams layoutdatat = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutdatat.height = (int) (height * 83 / 100);
        layoutdatat.gravity = Gravity.CENTER;
//		datalay.setLayoutParams(layoutdatat);
        LinearLayout.LayoutParams textlay = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        textlay.width = width * 68 / 100;
        // textlay.gravity = Gravity.CENTER | Gravity.LEFT;
        // textlay.leftMargin = width * 3 / 100;
        // textlay.gravity = Gravity.CENTER;
        // textlay.rightMargin = width * 14 / 100;
//		head.setLayoutParams(textlay);
        // head.setGravity(Gravity.CENTER | Gravity.LEFT);
        if (width >= 600) {
            $ShopName.setTextSize(17);
            $Adress1.setTextSize(17);
            $Address2.setTextSize(17);
            $City.setTextSize(17);
            $State.setTextSize(17);
            $Country.setTextSize(17);
            $PostalCode.setTextSize(17);
            $EmailId.setTextSize(17);
            $ShopNameEdit.setTextSize(17);
            $Adress1Edit.setTextSize(17);
            $Address2Edit.setTextSize(17);
            $Address3Edit.setTextSize(17);
            $Address3.setTextSize(17);
            $CityEdit.setTextSize(17);
            $StateEdit.setTextSize(17);
            $CountryEdit.setTextSize(17);
            $PostalCodEEdit.setTextSize(17);
            $EmailIdEdit.setTextSize(17);
            $Submit.setTextSize(17);
            $HeadLable.setTextSize(17);
            $PhoneNumberEdit.setTextSize(17);
            $PhoneNumber.setTextSize(17);
            $CountryCode.setTextSize(17);
            $CountrySeparator.setTextSize(17);
        } else if (width > 501 && width < 600) {
            $ShopName.setTextSize(16);
            $Adress1.setTextSize(16);
            $Address2.setTextSize(16);
            $City.setTextSize(16);
            $State.setTextSize(16);
            $Country.setTextSize(16);
            $PostalCode.setTextSize(16);
            $EmailId.setTextSize(16);
            $ShopNameEdit.setTextSize(16);
            $Adress1Edit.setTextSize(16);
            $Address2Edit.setTextSize(16);
            $Address3Edit.setTextSize(16);
            $Address3.setTextSize(16);
            $CityEdit.setTextSize(16);
            $StateEdit.setTextSize(16);
            $CountryEdit.setTextSize(16);
            $PostalCodEEdit.setTextSize(16);
            $EmailIdEdit.setTextSize(16);
            $Submit.setTextSize(16);
            $HeadLable.setTextSize(16);
            $PhoneNumberEdit.setTextSize(16);
            $PhoneNumber.setTextSize(16);
            $CountryCode.setTextSize(16);
            $CountrySeparator.setTextSize(16);
        } else if (width > 260 && width < 500) {
            $ShopName.setTextSize(15);
            $Adress1.setTextSize(15);
            $Address2.setTextSize(15);
            $City.setTextSize(15);
            $State.setTextSize(15);
            $Country.setTextSize(15);
            $PostalCode.setTextSize(15);
            $EmailId.setTextSize(15);
            $ShopNameEdit.setTextSize(15);
            $Adress1Edit.setTextSize(15);
            $Address2Edit.setTextSize(15);
            $Address3Edit.setTextSize(15);
            $Address3.setTextSize(15);
            $CityEdit.setTextSize(15);
            $StateEdit.setTextSize(15);
            $CountryEdit.setTextSize(15);
            $PostalCodEEdit.setTextSize(15);
            $EmailIdEdit.setTextSize(15);
            $Submit.setTextSize(15);
            $HeadLable.setTextSize(15);
            $PhoneNumberEdit.setTextSize(15);
            $PhoneNumber.setTextSize(15);
            $CountryCode.setTextSize(15);
            $CountrySeparator.setTextSize(15);
        } else if (width <= 260) {
            $ShopName.setTextSize(14);
            $Adress1.setTextSize(14);
            $Address2.setTextSize(14);
            $City.setTextSize(14);
            $State.setTextSize(14);
            $Country.setTextSize(14);
            $PostalCode.setTextSize(14);
            $EmailId.setTextSize(14);
            $ShopNameEdit.setTextSize(14);
            $Adress1Edit.setTextSize(14);
            $Address2Edit.setTextSize(14);
            $Address3Edit.setTextSize(14);
            $Address3.setTextSize(14);
            $CityEdit.setTextSize(14);
            $StateEdit.setTextSize(14);
            $CountryEdit.setTextSize(14);
            $PostalCodEEdit.setTextSize(14);
            $EmailIdEdit.setTextSize(14);
            $Submit.setTextSize(14);
            $HeadLable.setTextSize(14);
            $PhoneNumberEdit.setTextSize(14);
            $PhoneNumber.setTextSize(14);
            $CountryCode.setTextSize(14);
            $CountrySeparator.setTextSize(14);
        }
    }
}
