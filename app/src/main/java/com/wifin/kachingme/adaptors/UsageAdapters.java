package com.wifin.kachingme.adaptors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.CountryCodeGetSet;
import com.wifin.kachingme.settings.DataUsage;
import com.wifin.kachingme.settings.DeleteAccount;
import com.wifin.kachingme.settings.DeleteSecondaryNumber;
import com.wifin.kachingme.settings.Network_Usage;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.XMLParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

public class UsageAdapters extends RecyclerView.Adapter<UsageAdapters.UsageViewHolder> {
    List<String> usageItem;
    Context contextUsage;
    String TAG = UsageAdapters.class.getSimpleName(), primary_number, secondary_number, fullMobileNo;
    public static final String local_image_dir = Environment.getExternalStorageDirectory() + "/Kaching.me/";
    ArrayAdapter adapterc;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<CountryCodeGetSet> country_list;
    AsYouTypeFormatter formatter;
    PhoneNumberUtil phoneUtil;
    Phonenumber.PhoneNumber NumberProto;
    String mTextAuto;
    public static String COUNTRY, COUNTRYCODE, COUNTRYCODE_CHAR;
    EditText  etMobileNumbSec;
    TextView tvSignSecProceed, tvSignSecDismiss,etCountryCodeSec,mAlertTextMsg,mAlertCountryCodeSeparator;
    LinearLayout mAlertMobileLayout,mAlertControlLayout;
    AutoCompleteTextView atvCountryName;
    CommonMethods commonMethods;
    AlertDialog dialogSecondary;
    Dbhelper db;
    SharedPreferences  preferenceses, preference;
    SharedPreferences.Editor ed;

    public UsageAdapters() {
        Log.e(TAG, "Usage Adapter");
    }

    public UsageAdapters(Context contextUsage, List usageItem) {
        this.contextUsage = contextUsage;
        this.usageItem = usageItem;
    }

    @Override
    public UsageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View usageValues = LayoutInflater.from(parent.getContext()).inflate(R.layout.usage_card_jtem, parent, false);
        UsageViewHolder usageViewHolder = new UsageViewHolder(usageValues);
        return usageViewHolder;
    }

    @Override
    public void onBindViewHolder(UsageViewHolder holder, final int position) {
        CardView.LayoutParams usageLinear = new CardView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        usageLinear.width = (int) Constant.width;
        usageLinear.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        holder.linearUsageItem.setLayoutParams(usageLinear);

        Log.e(TAG, usageLinear.width + " " + usageLinear.height);

        LinearLayout.LayoutParams usageText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        usageText.width = LinearLayout.LayoutParams.MATCH_PARENT;
        usageText.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        usageText.setMargins(Constant.width * 2 / 100, Constant.width * 2 / 100, Constant.width * 2 / 100, Constant.width * 2 / 100);
        usageText.gravity = Gravity.CENTER_VERTICAL;
        holder.tvUsageItem.setLayoutParams(usageText);

        Log.e(TAG, usageText.width + " " + usageText.height + " " + Constant.width * 2 / 100);

        if (Constant.width >= 600) {
            holder.tvUsageItem.setTextSize(19);
        } else if (Constant.width > 501 && Constant.width < 600) {
            holder.tvUsageItem.setTextSize(18);
        } else if (Constant.width > 260 && Constant.width < 500) {
            holder.tvUsageItem.setTextSize(17);
        } else if (Constant.width <= 260) {
            holder.tvUsageItem.setTextSize(16);
        }

        holder.tvUsageItem.setText(usageItem.get(position));

        try {
            if (position == 4) {
                holder.tvUsageItem.setText("Storage Usage - " + Network_Usage.convertBytesToSuitableUnit(String.valueOf(dirSize(new File(local_image_dir)))));
            }
        } catch (Exception e) {
            Constant.printMsg("storage :" + e.toString());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usageItem == null ? 0 : usageItem.size();
    }

    class UsageViewHolder extends RecyclerView.ViewHolder {
        CardView cardUsageItem;
        LinearLayout linearUsageItem;
        TextView tvUsageItem;

        public UsageViewHolder(View itemView) {
            super(itemView);

            cardUsageItem = (CardView) itemView.findViewById(R.id.cardUsageItem);
            linearUsageItem = (LinearLayout) itemView.findViewById(R.id.linearUsageItem);
            tvUsageItem = (TextView) itemView.findViewById(R.id.tvUsageItem);
            Constant.typeFace(contextUsage, tvUsageItem);

        }
    }

    public void onItemSelected(int position) {
        if (position == 0) {
            // Delete Number
            Constant.printMsg(" settings::secondary_add");
            alertDialogSecondary();
        } else if (position == 1) {
            // Delete Number
            Constant.printMsg(" settings::account_deleteno");
            contextUsage.startActivity(new Intent(contextUsage, DeleteSecondaryNumber.class));
        } else if (position == 2) {
            // Delete Account
            Constant.printMsg(" settings::account_deleteaccount");
            contextUsage.startActivity(new Intent(contextUsage, DeleteAccount.class));
        } else if (position == 3) {
            // Newtork Usage
            Constant.printMsg(" settings::account_networkusage");
            contextUsage.startActivity(new Intent(contextUsage, DataUsage.class));
        }
    }

    public void alertDialogSecondary() {
        LayoutInflater inflaterSecondary = (LayoutInflater) contextUsage.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder alertSecondary = new AlertDialog.Builder(contextUsage);
        View inflateSecondaryAlert = inflaterSecondary.inflate(R.layout.alert_secondary_numb, null);
        alertSecondary.setView(inflateSecondaryAlert);
        dialogSecondary = alertSecondary.create();
        alertSecondaryScreenArrange(inflateSecondaryAlert);
        items = getCountryName();
        country_list = new ArrayList<>();
        country_list = getCountry();
        adapterc = new ArrayAdapter(contextUsage, R.layout.dropdown, items);
        phoneUtil = PhoneNumberUtil.getInstance();
        formatter = phoneUtil.getAsYouTypeFormatter(Locale.getDefault().getCountry());
        atvCountryName.setThreshold(1);
        atvCountryName.setAdapter(adapterc);
        atvCountryName.requestFocus();
        db = new Dbhelper(contextUsage.getApplicationContext());
        commonMethods = new CommonMethods(contextUsage);
        atvCountryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.length() < count) {
                    atvCountryName.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                if (atvCountryName.getText().toString().length() == 0) {
                    commonMethods.Toast_call(contextUsage, contextUsage.getResources()
                            .getString(R.string.please_select_country));
                    etCountryCodeSec.setText("");
                    etMobileNumbSec.setText("");
                    etMobileNumbSec.setEnabled(false);
                    atvCountryName.showDropDown();
                } else {
                    String countryName = atvCountryName.getText().toString().trim();

                    if (Arrays.asList(items).contains(countryName)) {
                        etMobileNumbSec.setEnabled(true);
                    }
                }
                mTextAuto = atvCountryName.getText().toString().trim();
            }
        });
        atvCountryName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                InputMethodManager in = (InputMethodManager) contextUsage.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getWindowToken(), 0);
                etMobileNumbSec.setEnabled(true);
                etMobileNumbSec.requestFocus();
                String selecttx = atvCountryName.getText().toString().trim();

                for (int i = 0; i < country_list.size(); i++) {
                    String name = country_list.get(i).getName().toString();

                    if (name.equals(selecttx)) {
                        try {
                            etCountryCodeSec.setText(country_list.get(i).getCode());
                            COUNTRY = country_list.get(i).getName();
                            COUNTRYCODE = country_list.get(i).getCode();
                            COUNTRYCODE_CHAR = country_list.get(i).getCountry_Code().toUpperCase();
                            formatter = phoneUtil.getAsYouTypeFormatter(COUNTRYCODE_CHAR);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        etMobileNumbSec.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                // this is for backspace
                if (keyCode == KeyEvent.ACTION_DOWN) {
                    // this is for backspace
                    ((Activity) contextUsage).getWindow().
                            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
                return false;
            }
        });
        tvSignSecProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSecondary.setCancelable(false);
                submitRegistration();
            }
        });
        tvSignSecDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSecondary.dismiss();
            }
        });
        dialogSecondary.setCancelable(true);
        dialogSecondary.show();
    }

    private void alertSecondaryScreenArrange(View view) {
        mAlertTextMsg = (TextView) view.findViewById(R.id.tvInfoSecondary);
        atvCountryName = (AutoCompleteTextView) view.findViewById(R.id.atvCountryName);
        etCountryCodeSec = (TextView) view.findViewById(R.id.etCountryCodeSec);
        etMobileNumbSec = (EditText) view.findViewById(R.id.etMobileNumbSec);
        mAlertCountryCodeSeparator = (TextView) view.findViewById(R.id.tvSeparatorSec);
        mAlertMobileLayout = (LinearLayout) view.findViewById(R.id.linearMobileNumber);
        mAlertControlLayout = (LinearLayout) view.findViewById(R.id.linearConfirmSec);
        tvSignSecProceed = (TextView) view.findViewById(R.id.tvSignSecProceed);
        tvSignSecDismiss = (TextView) view.findViewById(R.id.tvSignSecDismiss);

        int height = Constant.screenHeight;
        int width = Constant.screenWidth;

        LinearLayout.LayoutParams msgTextParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        msgTextParama.width = (int) (width * 85 / 100);
        msgTextParama.gravity = Gravity.CENTER;
        msgTextParama.topMargin = height * 4 / 100;
        mAlertTextMsg.setLayoutParams(msgTextParama);
        mAlertTextMsg.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams countryParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        countryParama.height = (int) (height * 7 / 100);
        countryParama.width = (int) (width * 80 / 100);
        countryParama.gravity = Gravity.CENTER;
        countryParama.topMargin = height * 2 / 100;
        atvCountryName.setLayoutParams(countryParama);
        atvCountryName.setGravity(Gravity.LEFT | Gravity.CENTER);
        atvCountryName.setPadding(width * 2 / 100, 0, 0, 0);

        LinearLayout.LayoutParams mobileLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mobileLayoutParama.height = (int) (height * 7 / 100);
        mobileLayoutParama.width = (int) (width * 80 / 100);
        mobileLayoutParama.gravity = Gravity.CENTER;
        mobileLayoutParama.topMargin = height * 1 / 100;
        mAlertMobileLayout.setLayoutParams(mobileLayoutParama);

        LinearLayout.LayoutParams countryCodeParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        countryCodeParama.height = (int) (height * 7 / 100);
        countryCodeParama.width = (int) (width * 13 / 100);
        countryCodeParama.gravity = Gravity.CENTER;
        etCountryCodeSec.setLayoutParams(countryCodeParama);
        etCountryCodeSec.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams separatorParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        separatorParama.height = (int) (height * 7 / 100);
        separatorParama.width = (int) (width * 6 / 100);
        separatorParama.gravity = Gravity.CENTER;
        //separatorParama.setMargins(width*1/100,0,width*1/100,0);
        mAlertCountryCodeSeparator.setLayoutParams(separatorParama);
        mAlertCountryCodeSeparator.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams mobileNoParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mobileNoParama.height = (int) (height * 7 / 100);
        mobileNoParama.width = (int) (width * 61 / 100);
        etMobileNumbSec.setLayoutParams(mobileNoParama);
        etMobileNumbSec.setGravity(Gravity.LEFT | Gravity.CENTER);
        etMobileNumbSec.setPadding(width * 2 / 100, 0, 0, 0);

        LinearLayout.LayoutParams sendLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        sendLayoutParama.height = (int) (height * 6 / 100);
        sendLayoutParama.width = (int) (width * 80 / 100);
        sendLayoutParama.topMargin = height * 4 / 100;
        sendLayoutParama.bottomMargin = height * 4 / 100;
        sendLayoutParama.gravity = Gravity.CENTER;
        mAlertControlLayout.setLayoutParams(sendLayoutParama);
        mAlertControlLayout.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams resendParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        resendParama.width = (int) (width * 30 / 100);
        resendParama.height = (int) (height * 6 / 100);
        resendParama.gravity = Gravity.CENTER;
        tvSignSecDismiss.setLayoutParams(resendParama);
        tvSignSecDismiss.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams nextParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nextParama.height = (int) (height * 6 / 100);
        nextParama.width = (int) (width * 30 / 100);
        nextParama.gravity = Gravity.CENTER;
        nextParama.leftMargin = width * 5 / 100;
        tvSignSecProceed.setLayoutParams(nextParama);
        tvSignSecProceed.setGravity(Gravity.CENTER);

        if (width >= 600) {
            mAlertTextMsg.setTextSize(16);
            atvCountryName.setTextSize(16);
            etCountryCodeSec.setTextSize(16);
            etMobileNumbSec.setTextSize(16);
            mAlertCountryCodeSeparator.setTextSize(16);
            tvSignSecProceed.setTextSize(16);
            tvSignSecDismiss.setTextSize(16);
        } else if (width > 501 && width < 600) {
            mAlertTextMsg.setTextSize(15);
            atvCountryName.setTextSize(15);
            etCountryCodeSec.setTextSize(15);
            etMobileNumbSec.setTextSize(15);
            mAlertCountryCodeSeparator.setTextSize(15);
            tvSignSecProceed.setTextSize(15);
            tvSignSecDismiss.setTextSize(15);
        } else if (width > 260 && width < 500) {
            mAlertTextMsg.setTextSize(14);
            atvCountryName.setTextSize(14);
            etCountryCodeSec.setTextSize(14);
            etMobileNumbSec.setTextSize(14);
            mAlertCountryCodeSeparator.setTextSize(14);
            tvSignSecProceed.setTextSize(14);
            tvSignSecDismiss.setTextSize(14);
        } else if (width <= 260) {
            mAlertTextMsg.setTextSize(13);
            atvCountryName.setTextSize(13);
            etCountryCodeSec.setTextSize(13);
            etMobileNumbSec.setTextSize(13);
            mAlertCountryCodeSeparator.setTextSize(13);
            tvSignSecProceed.setTextSize(13);
            tvSignSecDismiss.setTextSize(13);
        }
    }

    public ArrayList<String> getCountryName() {
        ArrayList<String> listn = new ArrayList<>();
        CountryCodeGetSet country = new CountryCodeGetSet();
        country.setCode("");
        country.setName(contextUsage.getResources().getString(R.string.select_country));
        country.setCountry_Code("in");
        listn.add(contextUsage.getResources().getString(R.string.select_country));
        StringBuilder x = null;

        try {
            InputStream in_s = contextUsage.getApplicationContext().getAssets().open("countrylist.xml");
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
            country.setCode(parser.getValue(e, "phoneCode"));
            country.setName(parser.getValue(e, "name"));
            country.setCountry_Code(parser.getValue(e, "code"));
            listn.add(parser.getValue(e, "name"));
        }
        return listn;
    }

    public ArrayList<CountryCodeGetSet> getCountry() {
        ArrayList<CountryCodeGetSet> list = new ArrayList<>();
        CountryCodeGetSet country = new CountryCodeGetSet();
        country.setCode("");
        country.setName(contextUsage.getResources().getString(R.string.select_country));
        country.setCountry_Code("in");
        list.add(country);
        StringBuilder x = null;

        try {
            InputStream in_s = contextUsage.getApplicationContext().getAssets().open("countrylist.xml");

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
            Constant.printMsg("country code:::::::>>>>" + parser.getValue(e, "phoneCode"));
            country.setName(parser.getValue(e, "name"));
            country.setCountry_Code(parser.getValue(e, "code"));
            list.add(country);
        }
        return list;
    }

    private void submitRegistration() {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        commonMethods = new CommonMethods(contextUsage);
        try {
            Log.d(TAG, "Country::" + COUNTRYCODE_CHAR);

            NumberProto = phoneUtil.parse(etMobileNumbSec.getText().toString().trim(), COUNTRYCODE_CHAR);
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }

        /**Siva Validating the phone number and countries*/

        if (atvCountryName.getText().toString().length() == 0) {
            commonMethods.showAlertDialog(contextUsage, contextUsage.getResources().getString(R.string.please_select_country), true);
        } else if (etMobileNumbSec.getText().toString().length() == 0) {
            commonMethods.showAlertDialog(contextUsage, contextUsage.getResources().getString(R.string.please_enter_phone_number), true);
        } else if (!phoneUtil.isValidNumber(NumberProto)) {
            commonMethods.showAlertDialog(contextUsage, "Invalid phone number", true);
        } else {
            String secno = etMobileNumbSec.getText().toString();
            new postSecondaryNumber().execute(secno);
            dialogSecondary.dismiss();
        }
    }

    public class postSecondaryNumber extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(contextUsage, AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setProgressDrawable(new ColorDrawable(android.graphics.Color.BLUE));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();
            preference = contextUsage.getSharedPreferences(KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
            //sp1 = PreferenceManager.getDefaultSharedPreferences(contextUsage);
            //String no = sp1.getString("number", "");
            //String code = sp1.getString("countrycode", "");
            result = ht.httpget(KachingMeConfig.SECONDARY_URL +preference.getString("MyPrimaryNumber", "")+
                    "&secondaryNumber=" + COUNTRYCODE + params[0] + "&countryCode=" + COUNTRYCODE);
            Constant.printMsg("siva secondary result..." + KachingMeConfig.SECONDARY_URL +preference.getString("MyPrimaryNumber", "")
                    + "&secondaryNumber=" + COUNTRYCODE + params[0] + "&countryCode=" + COUNTRYCODE + "............" + result);
            return result;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            super.onPostExecute(result);

            progressDialog.dismiss();

            if (result != null && result.length() > 0) {
                JSONObject jsonObject;
                String response = "", otp = "";
                try {
                    jsonObject = new JSONObject(result);
                    response = jsonObject.getString("status");
                    otp = jsonObject.getString("otp");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response.equalsIgnoreCase("Primary Number Not Exists")) {
                    Toast.makeText(contextUsage.getApplicationContext(), "Invalid User...Mobile Number Not Exists", Toast.LENGTH_SHORT).show();
                } else if (response.equalsIgnoreCase("Secondary Number already exists")) {
                    Toast.makeText(contextUsage.getApplicationContext(), "Mobile Number already Exists", Toast.LENGTH_SHORT).show();
                } else if (response.equalsIgnoreCase("Secondary Limit Reached")) {
                    Toast.makeText(contextUsage.getApplicationContext(), "Sorry Limit Exceeded", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put("primarynum", primary_number);
                    cv.put("secondarynum", secondary_number);
                    insertNumber(cv);
                    Constant.Otp = otp;
                    Constant.otpnumner = etMobileNumbSec.getText().toString();
                    fullMobileNo = COUNTRYCODE + etMobileNumbSec.getText().toString();
                    Constant.printMsg("verif otp:" + Constant.Otp);

                    AlertDialog.Builder dialogOtp = new AlertDialog.Builder(contextUsage);
                    dialogOtp.setTitle("OTP For Secondary Number");
                    dialogOtp.setMessage(Constant.Otp);
                    dialogOtp.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new postSecondaryOtp().execute();
                            dialog.dismiss();
                        }
                    });
                    dialogOtp.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialogOtp.setCancelable(false);
                    dialogOtp.show();
                }
            } else {
                Toast.makeText(contextUsage.getApplicationContext(), "Network Error!Please try again later!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void insertNumber(ContentValues cv) {
        // TODO Auto-generated method stub

        try {
            int a = (int) db.open().getDatabaseObj().insert(Dbhelper.TABLE_NUMBERS, null, cv);

            Constant.printMsg("No of inserted rows in kons :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in kons details ::::::" + e.toString());
        } finally {
            db.close();
        }
    }

    public class postSecondaryOtp extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        protected void onPreExecute() {
            // TODO Auto-generated method stub

            super.onPreExecute();
            progressDialog = new ProgressDialog(contextUsage, AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setProgressDrawable(new ColorDrawable(android.graphics.Color.BLUE));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();
            result = ht.httpget(KachingMeConfig.SECONDARY_URL_RES + fullMobileNo + "&otp=" + Constant.Otp);
            Constant.printMsg("result dis verification" + result);
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (result != null && result.length() > 0) {
                if (result.equalsIgnoreCase("Number Not Exists")) {
                    Toast.makeText(contextUsage.getApplicationContext(), "Invalid User...Mobile Number And Otp Wrong", Toast.LENGTH_SHORT).show();
                } else {
                    preferenceses = PreferenceManager.getDefaultSharedPreferences(contextUsage);
                    int count = preferenceses.getInt("added_num", 0);
                    ed = preferenceses.edit();
                    ed.putInt("added_num", count + 1);
                    ed.commit();
                    Constant.emptyFreebie = true;
                    Constant.fullmob = fullMobileNo;
                    Constant.printMsg("primarty.....1...." + Constant.countrycode + Constant.fullmob);
                    Constant.printMsg("primarty.....2...." + preferenceses.getString("countrycode", "") + preferenceses.getString("number", ""));
                    ContentValues cv = new ContentValues();
                    cv.put("primarynum", preferenceses.getString("number", ""));
                    cv.put("secondarynum", Constant.fullmob);
                    insertNumber(cv);
                    preference = contextUsage.getSharedPreferences(KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
                    int countMsg = preference.getInt("sec_count", 0);

                    if (countMsg == 1) {
                        Toast.makeText(contextUsage, "Number Added", Toast.LENGTH_SHORT).show();
                    } else if (countMsg == 2) {
                        Toast.makeText(contextUsage, "Number Added", Toast.LENGTH_SHORT).show();
                    } else if (countMsg == 3) {
                        Toast.makeText(contextUsage, "Number Added", Toast.LENGTH_SHORT).show();
                    } else if (countMsg == 4) {
                        Toast.makeText(contextUsage, "Number Added", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(contextUsage.getApplicationContext(), "Network Error!Please try again later!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Return the size of a directory in bytes
     **/

    private static long dirSize(File dir) {
        try {
            long result = 0;

            Stack<File> dirlist = new Stack<File>();
            dirlist.clear();

            dirlist.push(dir);

            while (!dirlist.isEmpty()) {
                File dirCurrent = dirlist.pop();

                File[] fileList = dirCurrent.listFiles();
                for (File f : fileList) {
                    if (f.isDirectory())
                        dirlist.push(f);
                    else
                        result += f.length();
                }
            }

            return result;
        } catch (Exception e) {
            // Constant.printMsg("storage meth:" + e.toString());

            return 0;
        }
    }
}
