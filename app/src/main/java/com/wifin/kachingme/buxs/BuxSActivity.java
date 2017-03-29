/*
* @author Sivaprasath
*
* @usage -  This class is used to display the buxs screen
*
*
* */

package com.wifin.kachingme.buxs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.BuxSActivityAdapter;
import com.wifin.kachingme.adaptors.BuxsPointAdapter;
import com.wifin.kachingme.adaptors.DonateAdapter;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.cart.CartActivity;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.BuxMasterDto;
import com.wifin.kachingme.pojo.BuxsEarnedDto;
import com.wifin.kachingme.pojo.BuxsRedeemedDto;
import com.wifin.kachingme.pojo.DonationDto;
import com.wifin.kachingme.pojo.RedeemDto;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.ProfileRoundImg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BuxSActivity extends HeaderActivity implements
        OnItemSelectedListener {

    LinearLayout mBuXSDetailLayout, mBuxLayout, mRemainingBuxSLayout,
            mDonatedBuxSLayout, mBuXSButtonLayout, mDonationLayout,
            mNumOfBuXSLayout, mDonateContactlayout, mActivityCounterLayout,
            mBuxSActivityLayout, mDonationBtnLayout, mSumBuxSLAyout;
    ImageView mProfileImg;
    TextView mBuXSText, mBuXSCountText, mDonateBuXSText, mDonateBuXSCountText,
            mNumOfBuXSText, mDonateToText, mContactText, mActivityCounterText,
            mDonationCounterText, mEarnedText, mActivityText,
            mBuXSActivityText, mEarnedBuxSActivity, mSumCreditText,
            mSumDebitText, mMyBuXSBtn, mDonateBtn, donate_no_record;
    EditText mDonatedBuXSEdit;
    ListView mBuxSActivityListview, mBuxSDonationList;
    View mListBottomView;
    Spinner mBuxSFilterSpinner;
    View mDonationView;
    int height = 0;
    int width = 0;
    Dbhelper db;
    ArrayList<String> mList = new ArrayList<String>();
    SharedPreferences sp;
    ArrayList<BuxsEarnedDto> mEarned = new ArrayList<BuxsEarnedDto>();
    ArrayList<BuxsRedeemedDto> mRedeemed = new ArrayList<BuxsRedeemedDto>();
    ArrayList<BuxMasterDto> buxMasterList = new ArrayList<BuxMasterDto>();

    Bitmap bmp;
    int debit_value = 0;
    int credit_value = 0;
    ProgressDialog progressDialog, progressDialogAsyn;
    int count = 1000;
    Handler handler = new Handler();
    Runnable run;
    SharedPreferences preference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.activity_buxs, vg);

        mHeading.setText("BuxS");
        mHeaderImg.setVisibility(View.VISIBLE);
        mHeaderImg.setImageResource(R.drawable.doller);
        mNextBtn.setImageResource(R.drawable.reset);
        db = new Dbhelper(getApplicationContext());
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        preference = getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        initializeVariable();
        screenArrangement();
        FetchRedeem();
        mList.add("All");
        mList.add("Credit");
        mList.add("Debit");
        new MyAsync().execute();

        ArrayAdapter<String> list_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mList);
        list_adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBuxSFilterSpinner.setAdapter(list_adapter);
        mBuxSFilterSpinner.setOnItemSelectedListener(this);
        Long point = sp.getLong("buxvalue", 0);
        Long donpoint = sp.getLong("donationpoint", 0);
        Constant.printMsg("bux:::" + point + donpoint + Constant.userId);
        mBuXSCountText.setText(String.valueOf(point));
        mDonateBuXSCountText.setText(String.valueOf(donpoint));
        mMyBuXSBtn.setBackgroundResource(R.drawable.red_border_background);
        mMyBuXSBtn.setTextColor(Color.RED);
        mDonateBtn.setBackgroundResource(R.drawable.gray_border_background);
        mDonateBtn.setTextColor(Color.GRAY);
        mActivityCounterLayout.setVisibility(View.VISIBLE);
        mDonationLayout.setVisibility(View.GONE);
        mDonationBtnLayout.setVisibility(View.GONE);
        mBuxSActivityListview.setVisibility(View.VISIBLE);
        if (Connectivity.isConnected(getApplicationContext())) {
            new getEarned().execute();
            new getRedeemed().execute();
//            new getBuxsMaster().execute();
        } else {
            getdataFromDB();
            Toast.makeText(getApplicationContext(), "Check", Toast.LENGTH_SHORT)
                    .show();
            mBuxSActivityListview.setAdapter(new BuxsPointAdapter(
                    getApplicationContext(), mEarned));
        }
        mNextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAlert();
            }
        });
        mBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BuxSActivity.this,
                        SliderTesting.class);
                startActivity(i);
                finish();
            }
        });
        mChatLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(BuxSActivity.this,
                        SliderTesting.class));
            }
        });
        mBuxLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //startActivity(new Intent(HeaderActivity.this,
                //		BuxSActivity.class));
            }
        });
        mCartLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(BuxSActivity.this,
                        CartActivity.class));
            }
        });
        mContactText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mDonatedBuXSEdit.getText().toString().length() > 0) {
                    Constant.donatepoint = Long.parseLong(mDonatedBuXSEdit.getText()
                            .toString());
                    Constant.mFromDonateContact = true;
                    Intent i = new Intent(BuxSActivity.this,
                            SliderTesting.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Enter the point to be donated", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        mMyBuXSBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mMyBuXSBtn
                        .setBackgroundResource(R.drawable.red_border_background);
                mMyBuXSBtn.setTextColor(Color.RED);
                mDonateBtn
                        .setBackgroundResource(R.drawable.gray_border_background);
                mDonateBtn.setTextColor(Color.GRAY);
                mActivityCounterLayout.setVisibility(View.VISIBLE);
                mDonationLayout.setVisibility(View.GONE);
                mDonationBtnLayout.setVisibility(View.GONE);
                mBuxSActivityListview.setVisibility(View.VISIBLE);
                if (Connectivity.isConnected(getApplicationContext())) {
                    new getEarned().execute();
                    new getRedeemed().execute();
                } else {
                    getdataFromDB();
                    Toast.makeText(getApplicationContext(), "Check",
                            Toast.LENGTH_SHORT).show();
                    mBuxSActivityListview.setAdapter(new BuxsPointAdapter(
                            getApplicationContext(), mEarned));
                }

            }
        });
        mDonateBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mMyBuXSBtn
                        .setBackgroundResource(R.drawable.gray_border_background);
                mMyBuXSBtn.setTextColor(Color.GRAY);
                mDonateBtn
                        .setBackgroundResource(R.drawable.red_border_background);
                mDonateBtn.setTextColor(Color.RED);
                mActivityCounterLayout.setVisibility(View.GONE);
                mDonationLayout.setVisibility(View.VISIBLE);
                mBuxSActivityListview.setVisibility(View.GONE);
                mDonationBtnLayout.setVisibility(View.VISIBLE);
                if (Constant.donatelust.size() > 0) {
                    mBuxSFilterSpinner.setVisibility(View.VISIBLE);
                    mSumCreditText.setVisibility(View.VISIBLE);
                    mSumDebitText.setVisibility(View.VISIBLE);
                    donate_no_record.setVisibility(View.GONE);
                    mBuxSDonationList.setAdapter(new DonateAdapter(
                            getApplicationContext(), Constant.donatelust));
                } else {
                    mBuxSFilterSpinner.setVisibility(View.GONE);
                    mSumCreditText.setVisibility(View.GONE);
                    mSumDebitText.setVisibility(View.GONE);
                    donate_no_record.setVisibility(View.VISIBLE);
                }
            }
        });

        mDonatedBuXSEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (mDonatedBuXSEdit.getText().toString().matches("0")) {
                    mDonatedBuXSEdit.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                if (mDonatedBuXSEdit.getText().toString().trim().length() > 0) {
                    if (mDonatedBuXSEdit.getText()
                            .toString().contains(".")) {
                        Toast.makeText(getApplicationContext(),
                                "Please Enter the correct format", Toast.LENGTH_LONG).show();
                        mDonatedBuXSEdit.getText().clear();
                    } else {
                        int point = Integer.valueOf(mBuXSCountText.getText().toString()
                                .trim());
                        int points_entered = Integer.valueOf(mDonatedBuXSEdit.getText()
                                .toString().trim());

                        if (points_entered > point) {
                            Toast.makeText(getApplicationContext(),
                                    "exceeding buxs", Toast.LENGTH_LONG).show();
                            mDonatedBuXSEdit.getText().clear();
                        }
                    }
                }
            }
        });
    }


    private void initializeVariable() {
        mBuXSDetailLayout = (LinearLayout) findViewById(R.id.buxs_detail_layout);
        mBuxLayout = (LinearLayout) findViewById(R.id.buxs_layout);
        mRemainingBuxSLayout = (LinearLayout) findViewById(R.id.remaining_buxs_layout);
        mDonatedBuxSLayout = (LinearLayout) findViewById(R.id.donated_buxs_layout);
        mBuXSButtonLayout = (LinearLayout) findViewById(R.id.buxs_button_layout);
        mDonationLayout = (LinearLayout) findViewById(R.id.donation_layout);
        mNumOfBuXSLayout = (LinearLayout) findViewById(R.id.num_of_buxs_layout);
        mDonateContactlayout = (LinearLayout) findViewById(R.id.donate_contact_layout);
        mActivityCounterLayout = (LinearLayout) findViewById(R.id.activity_counter_layout);
        mBuxSActivityLayout = (LinearLayout) findViewById(R.id.activity_buxs_layout);
        mDonationBtnLayout = (LinearLayout) findViewById(R.id.donation_btn_layout);
        mSumBuxSLAyout = (LinearLayout) findViewById(R.id.sum_buxs_layout);
        mDonationView = (View) findViewById(R.id.donate_view);
        mProfileImg = (ImageView) findViewById(R.id.profile_img);
        mBuXSText = (TextView) findViewById(R.id.bux_text);
        mBuXSCountText = (TextView) findViewById(R.id.bux_count_text);
        mDonateBuXSText = (TextView) findViewById(R.id.donated_buxs_text);
        mDonateBuXSCountText = (TextView) findViewById(R.id.donated_buxs_count);
        mNumOfBuXSText = (TextView) findViewById(R.id.num_of_buxs_text);
        mDonateToText = (TextView) findViewById(R.id.donate_text);
        mContactText = (TextView) findViewById(R.id.contact_text);
        mDonationCounterText = (TextView) findViewById(R.id.donation_counter_text);
        mActivityCounterText = (TextView) findViewById(R.id.activity_counter_text);
        mEarnedText = (TextView) findViewById(R.id.earned_text);
        mActivityText = (TextView) findViewById(R.id.activity_text);
        mBuXSActivityText = (TextView) findViewById(R.id.buxs_activity_text);
        mEarnedBuxSActivity = (TextView) findViewById(R.id.earned_buxs_text);
        mMyBuXSBtn = (TextView) findViewById(R.id.my_buxs_btn);
        mSumCreditText = (TextView) findViewById(R.id.sum_of_credit);
        mSumDebitText = (TextView) findViewById(R.id.sum_of_debit);
        donate_no_record = (TextView) findViewById(R.id.donate_no_record);
        mDonateBtn = (TextView) findViewById(R.id.donate_btn);
        mDonatedBuXSEdit = (EditText) findViewById(R.id.enter_bux);
        mBuxSActivityListview = (ListView) findViewById(R.id.bux_activity_listview);
        mBuxSDonationList = (ListView) findViewById(R.id.donation_list);
        mBuxSFilterSpinner = (Spinner) findViewById(R.id.date_pick_text);
        mListBottomView= (View) findViewById(R.id.activity_buxs_listView);

        Constant.typeFace(this, mBuXSText);
        Constant.typeFace(this, mBuXSCountText);
        Constant.typeFace(this, mDonateBuXSText);
        Constant.typeFace(this, mDonateBuXSCountText);
        Constant.typeFace(this, mNumOfBuXSText);
        Constant.typeFace(this, mDonateToText);
        Constant.typeFace(this, mContactText);
        Constant.typeFace(this, mActivityCounterText);
        Constant.typeFace(this, mDonationCounterText);
        Constant.typeFace(this, mEarnedText);
        Constant.typeFace(this, mActivityText);
        Constant.typeFace(this, mBuXSActivityText);
        Constant.typeFace(this, mEarnedBuxSActivity);
        Constant.typeFace(this, mSumCreditText);
        Constant.typeFace(this, mSumDebitText);
        Constant.typeFace(this, mMyBuXSBtn);
        Constant.typeFace(this, mDonateBtn);
        Constant.typeFace(this, mDonatedBuXSEdit);
        Constant.typeFace(this, donate_no_record);
    }

    public void FetchRedeem() {

        Constant.redeemlist.clear();

        String tx, mn;

        Dbhelper db = new Dbhelper(getApplicationContext());
        Cursor c = null;
        try {
            c = db.open()
                    .getDatabaseObj()
                    .query(Dbhelper.TABLE_RET, null, null, null, null, null,
                            null);
            int txnm = c.getColumnIndex("name");
            int mnnm = c.getColumnIndex("bux");

            Constant.printMsg("The pending cart list in db ::::"
                    + c.getCount());

            if (c.getCount() > 0) {

                while (c.moveToNext()) {

                    tx = c.getString(txnm);
                    mn = c.getString(mnnm);

                    Constant.printMsg("dbadd:nym:" + tx + "  " + mn + "  ");

                    RedeemDto p = new RedeemDto();
                    p.setName(tx);
                    p.setBux(mn);

                    Constant.redeemlist.add(p);
                }
            }
        } catch (SQLException e) {

            Constant.printMsg("Sql exception in pending shop details ::::"
                    + e.toString());
        } finally {
            c.close();
            db.close();
        }
    }

    protected void insertbuxDB(ContentValues cv) {
        // TODO Auto-generated method stub
        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_BUX_COUNTER, null, cv);
            Constant.printMsg("No of inserted rows in zzle :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in led details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }
    }

    private void delete(String query) {
        // TODO Auto-generated method stub
        Cursor c = null;

        try {
            c = db.open().getDatabaseObj().rawQuery(query, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
        } catch (SQLException e) {

        } finally {
            if (c != null) {
                c.close();
            }

            db.close();
        }
    }

    private void getdataFromDB() {

        mEarned.clear();

        String tx, dt, bux_status;
        Long pt;

        Dbhelper db = new Dbhelper(getApplicationContext());
        Cursor c = null;
        try {

            c = db.open()
                    .getDatabaseObj()
                    .query(Dbhelper.TABLE_BUX_COUNTER, null, null, null, null,
                            null, null);
            int txnm = c.getColumnIndex("activity_bux");
            int mnnm = c.getColumnIndex("activity");
            int poi = c.getColumnIndex("earned_bux");
            Constant.printMsg("The pending donate list in db ::::"
                    + c.getCount());

            if (c.getCount() > 0) {

                while (c.moveToNext()) {

                    tx = c.getString(txnm);
                    dt = c.getString(mnnm);
                    pt = c.getLong(poi);
                    Constant.printMsg("donate:ddfsd" + tx + "  " + dt + "  "
                            + pt);
                    if (pt != 0) {
                        BuxsEarnedDto p = new BuxsEarnedDto();
                        p.setAcheive(tx);
                        p.setName(dt);
                        p.setBuxs(String.valueOf(pt));
                        mEarned.add(p);
                    }

                }
            }
        } catch (SQLException e) {

            Constant.printMsg("Sql exception in pending shop details ::::"
                    + e.toString());
        } finally {
            c.close();
            db.close();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // TODO Auto-generated method stub
        debit_value = 0;
        credit_value = 0;

        if (mBuxSFilterSpinner.getSelectedItem().toString()
                .equalsIgnoreCase("credit")) {
            String query = "select * from " + Dbhelper.TABLE_DONATE
                    + " where status = 'credit'";
            Constant.printMsg("priyaaaa " + query);
            FetchBuxsData(query);
            Constant.printMsg("priyaaaa11 " + Constant.donatelust);

            mBuxSDonationList.setAdapter(new DonateAdapter(
                    getApplicationContext(), Constant.donatelust));

            for (int i = 0; i < Constant.donatelust.size(); i++) {
                credit_value += Double.valueOf(Constant.donatelust.get(i)
                        .getPoint());
            }
            mSumCreditText.setVisibility(View.VISIBLE);
            mSumDebitText.setVisibility(View.GONE);
            mSumCreditText.setText(String.valueOf("Total Credit : "
                    + credit_value));

        }
        if (mBuxSFilterSpinner.getSelectedItem().toString()
                .equalsIgnoreCase("debit")) {
            String query = "select * from " + Dbhelper.TABLE_DONATE
                    + " where status = 'debit'";
            Constant.printMsg("priyaaaa " + query);

            FetchBuxsData(query);
            Constant.printMsg("priyaaaa11 " + Constant.donatelust);
            for (int i = 0; i < Constant.donatelust.size(); i++) {
                debit_value += Double.valueOf(Constant.donatelust.get(i)
                        .getPoint());
            }
            mSumCreditText.setVisibility(View.GONE);
            mSumDebitText.setVisibility(View.VISIBLE);
            mSumDebitText.setText(String
                    .valueOf("Total Debit : " + debit_value));

            mBuxSDonationList.setAdapter(new DonateAdapter(
                    getApplicationContext(), Constant.donatelust));
        }
        if (mBuxSFilterSpinner.getSelectedItem().toString()
                .equalsIgnoreCase("all")) {
            debit_value = 0;
            credit_value = 0;
            fetchData();

            mBuxSDonationList.setAdapter(new DonateAdapter(
                    getApplicationContext(), Constant.donatelust));

            for (int i = 0; i < Constant.donatelust.size(); i++) {

                if (Constant.donatelust.get(i).getStatus()
                        .equalsIgnoreCase("credit")) {
                    credit_value += Double.valueOf(Constant.donatelust.get(i)
                            .getPoint());
                }
                if (Constant.donatelust.get(i).getStatus()
                        .equalsIgnoreCase("debit")) {
                    debit_value += Double.valueOf(Constant.donatelust.get(i)
                            .getPoint());
                }
            }
            mSumCreditText.setVisibility(View.VISIBLE);
            mSumDebitText.setVisibility(View.VISIBLE);
            mSumDebitText.setText(String
                    .valueOf("Total Debit : " + debit_value));
            mSumCreditText.setText(String.valueOf("Total Credit : "
                    + credit_value));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    private void FetchBuxsData(String query) {
        // TODO Auto-generated method stub

        Constant.donatelust.clear();

        Cursor c = null;

        try {
            c = db.open().getDatabaseObj().rawQuery(query, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
            if (c.getCount() > 0) {

                while (c.moveToNext()) {
                    DonationDto p = new DonationDto();
                    p.setDate(c.getString(1));
                    p.setName(c.getString(3));
                    p.setPoint(c.getString(2));
                    p.setStatus(c.getString(5));

                    Constant.donatelust.add(p);
                }
            }
        } catch (SQLException e) {

        } finally {
            if (c != null) {
                c.close();
            }

            db.close();
        }

    }

    private void fetchData() {

        Constant.donatelust.clear();

        String tx, dt, bux_status;
        Long pt;

        Dbhelper db = new Dbhelper(getApplicationContext());
        Cursor c = null;
        try {

            c = db.open()
                    .getDatabaseObj()
                    .query(Dbhelper.TABLE_DONATE, null, null, null, null, null,
                            null);
            int txnm = c.getColumnIndex("name");
            int mnnm = c.getColumnIndex("date");
            int poi = c.getColumnIndex("points");
            int status = c.getColumnIndex("status");
            Constant.printMsg("The pending donate list in db ::::"
                    + c.getCount());

            if (c.getCount() > 0) {

                while (c.moveToNext()) {

                    tx = c.getString(txnm);
                    dt = c.getString(mnnm);
                    pt = c.getLong(poi);
                    bux_status = c.getString(status);
                    Constant.printMsg("donate:" + tx + "  " + dt + "  " + pt);
                    if (pt != 0) {
                        DonationDto p = new DonationDto();
                        p.setDate(dt);
                        p.setName(tx);
                        p.setPoint(String.valueOf(pt));
                        p.setStatus(bux_status);

                        Constant.donatelust.add(p);
                    }

                }
            }
        } catch (SQLException e) {

            Constant.printMsg("Sql exception in pending shop details ::::"
                    + e.toString());
        } finally {
            c.close();
            db.close();
        }
    }

    private void resetAlert() {

        AlertDialog.Builder b;
        b = new AlertDialog.Builder(this);

        b.setCancelable(false);
        b.setMessage("Are you sure you want to reset the BuxS").setCancelable(
                false);

        b.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                progressDialog = new ProgressDialog(BuxSActivity.this,
                        AlertDialog.THEME_HOLO_LIGHT);
                progressDialog.setMessage("Resetting Points Please Wait...");
                progressDialog.setProgressDrawable(new ColorDrawable(
                        Color.BLUE));
                progressDialog.setCancelable(false);
                progressDialog.show();
                String query1 = "DELETE FROM " + Dbhelper.TABLE_DONATE;
                delete(query1);

                sp.edit().remove("buxvalue").commit();

                sp.edit().remove("intropoint").commit();

                sp.edit().remove("donationpoint").commit();

                sp.edit().remove("chatpoint").commit();

                sp.edit().remove("zzlepoint").commit();

                sp.edit().remove("nympoint").commit();

                sp.edit().remove("locpoint").commit();

                sp.edit().remove("imgpoint").commit();

                sp.edit().remove("deelpoint").commit();

                sp.edit().remove("wishpoint").commit();

                sp.edit().remove("konpoint").commit();

                sp.edit().remove("regpoints").commit();

                sp.edit().remove("destpoint").commit();

                handler.postDelayed(run = new Runnable() {
                    public void run() {

                        progressDialog.dismiss();

                        Intent i = new Intent(BuxSActivity.this,
                                BuxSActivity.class);
                        startActivity(i);
                        finish();

                    }
                }, count * 3);


            }
        });
        b.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        b.setCancelable(true);

        AlertDialog alert = b.create();
        alert.show();


    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        try {
            if (progressDialogAsyn.isShowing()) {
                progressDialogAsyn.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent i = new Intent(BuxSActivity.this, SliderTesting.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    private void screenArrangement() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        LinearLayout.LayoutParams mAddBtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mAddBtnParams.width = (int) width * 6 / 100;
        mAddBtnParams.height = (int) width * 6 / 100;
        mAddBtnParams.rightMargin = width * 1 / 100;
        mHeaderImg.setLayoutParams(mAddBtnParams);

        LinearLayout.LayoutParams listBottomParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        listBottomParama.height = (int) (width * 0.5 / 100);
        mListBottomView.setLayoutParams(listBottomParama);

        LinearLayout.LayoutParams dateparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dateparams.width = width * 30 / 100;
        dateparams.height = (int) (height * 5 / 100);
        dateparams.bottomMargin = (int) (width * 0.5 / 100);
        mBuxSFilterSpinner.setLayoutParams(dateparams);
        LinearLayout.LayoutParams totalparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        totalparams.width = width * 50 / 100;
        totalparams.height = (int) (height * 5 / 100);
        totalparams.gravity = Gravity.CENTER;
        mSumCreditText.setLayoutParams(totalparams);
        mSumDebitText.setLayoutParams(totalparams);

        LinearLayout.LayoutParams buXSDetailLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        buXSDetailLayoutParams.width = width;
        buXSDetailLayoutParams.height = width * 35 / 100;
        buXSDetailLayoutParams.gravity = Gravity.CENTER;
        mBuXSDetailLayout.setLayoutParams(buXSDetailLayoutParams);

        LinearLayout.LayoutParams profileImgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        profileImgParams.width = width * 20 / 100;
        profileImgParams.height = width * 20 / 100;
        profileImgParams.leftMargin = width * 3 / 100;
        profileImgParams.gravity = Gravity.CENTER;
        mProfileImg.setLayoutParams(profileImgParams);

        LinearLayout.LayoutParams mBuxlayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        mBuxlayoutParams.leftMargin = width * 5 / 100;
        mBuxlayoutParams.gravity = Gravity.CENTER;
        mBuxlayoutParams.topMargin = width * 3 / 100;
        mBuxLayout.setLayoutParams(mBuxlayoutParams);

        LinearLayout.LayoutParams buXScountParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buXScountParams.width = width * 30 / 100;
        buXScountParams.height = width * 10 / 100;
        mBuXSCountText.setGravity(Gravity.CENTER);
        mBuXSCountText.setLayoutParams(buXScountParams);
        mMyBuXSBtn.setGravity(Gravity.CENTER);
        mMyBuXSBtn.setLayoutParams(buXScountParams);

        LinearLayout.LayoutParams donatedTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        donatedTextParams.leftMargin = width * 5 / 100;
        mDonateBuXSText.setLayoutParams(donatedTextParams);

        LinearLayout.LayoutParams DonatebuXScountParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        DonatebuXScountParams.width = width * 30 / 100;
        DonatebuXScountParams.height = width * 10 / 100;
        DonatebuXScountParams.leftMargin = width * 5 / 100;
        mDonateBuXSCountText.setGravity(Gravity.CENTER);
        mDonateBuXSCountText.setLayoutParams(DonatebuXScountParams);
        mDonateBtn.setGravity(Gravity.CENTER);
        mDonateBtn.setLayoutParams(DonatebuXScountParams);

        LinearLayout.LayoutParams buxbtnlayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        buxbtnlayoutParams.width = width;
        buxbtnlayoutParams.height = width * 20 / 100;
        mBuXSButtonLayout.setGravity(Gravity.CENTER);
        mBuXSButtonLayout.setLayoutParams(buxbtnlayoutParams);

        LinearLayout.LayoutParams donateBuXSLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        donateBuXSLayoutParams.width = width;
        donateBuXSLayoutParams.height = width * 40 / 100;
        donateBuXSLayoutParams.gravity = Gravity.CENTER;
        mDonationLayout.setLayoutParams(donateBuXSLayoutParams);

        LinearLayout.LayoutParams numOfBuxSParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        numOfBuxSParams.width = width * 30 / 100;
        numOfBuxSParams.height = width * 10 / 100;
        numOfBuxSParams.leftMargin = width * 5 / 100;
        numOfBuxSParams.topMargin = width * 5 / 100;

        mNumOfBuXSText.setLayoutParams(numOfBuxSParams);
        mDonateToText.setLayoutParams(numOfBuxSParams);

        LinearLayout.LayoutParams numOfBuxSCountParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        numOfBuxSCountParams.width = width * 40 / 100;
        numOfBuxSCountParams.height = width * 10 / 100;
        numOfBuxSCountParams.leftMargin = width * 5 / 100;
        numOfBuxSCountParams.topMargin = width * 5 / 100;

        mDonatedBuXSEdit.setLayoutParams(numOfBuxSCountParams);
        mDonatedBuXSEdit.setPadding(width * 1 / 100, width * 1 / 100, width * 1 / 100,
                width * 1 / 100);
        mContactText.setLayoutParams(numOfBuxSCountParams);

        LinearLayout.LayoutParams activityCounterTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        activityCounterTextParams.width = width;
        activityCounterTextParams.height = width * 8 / 100;
        activityCounterTextParams.leftMargin = width * 5 / 100;
        activityCounterTextParams.topMargin = width * 3 / 100;

        mActivityCounterText.setLayoutParams(activityCounterTextParams);
        mDonationCounterText.setLayoutParams(activityCounterTextParams);

        LinearLayout.LayoutParams earnedTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        earnedTextParams.width = width;

        earnedTextParams.height = width * 10 / 100;
        earnedTextParams.topMargin = width * 1 / 100;
        mEarnedText.setGravity(Gravity.CENTER);
        mEarnedText.setLayoutParams(earnedTextParams);

        LinearLayout.LayoutParams activityBuxSParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        activityBuxSParams.width = width;
        activityBuxSParams.height = width * 10 / 100;
        mBuxSActivityLayout.setLayoutParams(activityBuxSParams);

        LinearLayout.LayoutParams activityBuxStextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        activityBuxStextParams.width = width * 32 / 100;
        activityBuxStextParams.height = width * 10 / 100;
        mBuXSActivityText.setGravity(Gravity.CENTER);
        mActivityText.setGravity(Gravity.CENTER);
        mEarnedBuxSActivity.setGravity(Gravity.CENTER);

        mActivityText.setLayoutParams(activityBuxStextParams);
        mBuXSActivityText.setLayoutParams(activityBuxStextParams);
        mEarnedBuxSActivity.setLayoutParams(activityBuxStextParams);

        if (width >= 600) {
            mBuXSCountText.setTextSize(16);
            mMyBuXSBtn.setTextSize(16);
            mDonateBuXSText.setTextSize(16);
            mBuXSText.setTextSize(16);
            mDonateBtn.setTextSize(16);
            mNumOfBuXSText.setTextSize(16);
            mDonateToText.setTextSize(16);
            mDonatedBuXSEdit.setTextSize(16);
            mContactText.setTextSize(16);
            mActivityCounterText.setTextSize(16);
            mDonationCounterText.setTextSize(16);
            mEarnedText.setTextSize(16);
            donate_no_record.setTextSize(16);
            mActivityText.setTextSize(15);
            mBuXSActivityText.setTextSize(15);
            mEarnedBuxSActivity.setTextSize(15);
        } else if (width < 600 && width >= 480) {
            mBuXSCountText.setTextSize(15);
            mMyBuXSBtn.setTextSize(15);
            mDonateBuXSText.setTextSize(15);
            mBuXSText.setTextSize(15);
            mDonateBtn.setTextSize(15);
            mNumOfBuXSText.setTextSize(15);
            mDonateToText.setTextSize(15);
            mDonatedBuXSEdit.setTextSize(15);
            mContactText.setTextSize(15);
            mActivityCounterText.setTextSize(15);
            mDonationCounterText.setTextSize(15);
            mEarnedText.setTextSize(15);
            donate_no_record.setTextSize(15);
            mActivityText.setTextSize(14);
            mBuXSActivityText.setTextSize(14);
            mEarnedBuxSActivity.setTextSize(14);
        } else if (width < 480 && width >= 320) {
            mBuXSCountText.setTextSize(12);
            mMyBuXSBtn.setTextSize(12);
            mBuXSText.setTextSize(12);
            mDonateBuXSText.setTextSize(12);
            mDonateBtn.setTextSize(12);
            mNumOfBuXSText.setTextSize(12);
            mDonateToText.setTextSize(12);
            mDonatedBuXSEdit.setTextSize(12);
            mContactText.setTextSize(12);
            mActivityCounterText.setTextSize(12);
            mDonationCounterText.setTextSize(12);
            mEarnedText.setTextSize(12);
            donate_no_record.setTextSize(12);
            mActivityText.setTextSize(11);
            mBuXSActivityText.setTextSize(11);
            mEarnedBuxSActivity.setTextSize(11);
        } else if (width < 320) {
            mBuXSCountText.setTextSize(10);
            mMyBuXSBtn.setTextSize(10);
            mDonateBuXSText.setTextSize(10);
            mDonateBtn.setTextSize(10);
            mNumOfBuXSText.setTextSize(10);
            mDonateToText.setTextSize(10);
            mDonatedBuXSEdit.setTextSize(10);
            mContactText.setTextSize(10);
            mActivityCounterText.setTextSize(10);
            mDonationCounterText.setTextSize(10);
            mBuXSText.setTextSize(10);
            donate_no_record.setTextSize(10);
            mEarnedText.setTextSize(10);
            mActivityText.setTextSize(9);
            mBuXSActivityText.setTextSize(9);
            mEarnedBuxSActivity.setTextSize(9);
        }
    }

    private class MyAsync extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialogAsyn = new ProgressDialog(BuxSActivity.this);
            progressDialogAsyn.setMessage(getResources()
                    .getString(R.string.loading));
            progressDialogAsyn.show();
            Constant.printMsg("called");
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                System.gc();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inDither = true;
                bmp = BitmapFactory.decodeByteArray(
                        KachingMeApplication.getAvatar(), 0,
                        KachingMeApplication.getAvatar().length, options);
            } catch (Exception e) {
                // //ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
            }
            return bmp;
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            progressDialogAsyn.cancel();
            super.onPostExecute(result);
            try {
                if (bmp != null) {
                    ProfileRoundImg roundImageProfile = new ProfileRoundImg(bmp);
                    mProfileImg.setImageDrawable(roundImageProfile);
                } else {
                    Bitmap mTempIcon = null;
                    mTempIcon = BitmapFactory.decodeResource(getApplicationContext()
                            .getResources(), R.drawable.contact_profile);
                    mProfileImg.setImageBitmap(mTempIcon);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class getBuxsMaster extends AsyncTask<String, String, String> {
        ProgressDialog progressdialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressdialog = new ProgressDialog(BuxSActivity.this);
            progressdialog.setMessage(getResources()
                    .getString(R.string.loading));
            progressdialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();

            result = ht.httpget(KachingMeConfig.GET_BUXS);
            Constant.printMsg("PRODUCT URL>>>>>>" + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressdialog.dismiss();
            Constant.printMsg("PRODUCT RESULT>>>>>>" + result);
            if (result != null && result.length() > 0) {
                CommonMethods commonMethods = new CommonMethods(BuxSActivity.this);
                if (commonMethods.isJSONValid(result)) {
                    JSONArray jsonArray = null;
                    JSONObject jObject = null;
                    try {
                        jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jObject = new JSONObject(jsonArray.get(i).toString());
                            BuxMasterDto buxMasterDto = new BuxMasterDto();
                            buxMasterDto.setActivity(jObject.getString("activity"));
                            buxMasterDto.setBuxMasterId(Integer.parseInt(jObject.getString("buxMasterId")));
                            buxMasterDto.setBuxsForActivity(Integer.parseInt(jObject.getString("buxsForActivity")));
                            buxMasterList.add(buxMasterDto);
                        }
                    } catch (JSONException e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    if (buxMasterList.size() > 0) {
                        mBuxSActivityListview.setAdapter(new BuxSActivityAdapter(
                                getApplicationContext(), buxMasterList));
                    }
                } else {
                    Toast.makeText(BuxSActivity.this, "Timeout Error..Try Again Later", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(BuxSActivity.this, "Network Error..Try Again Later", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class getEarned extends AsyncTask<String, String, String> {

        ProgressDialog progressdialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressdialog = new ProgressDialog(BuxSActivity.this);
            progressdialog.setMessage(getResources()
                    .getString(R.string.loading));
            progressdialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();
            result = ht.httpget(KachingMeConfig.Buxs);
            Constant.printMsg("PRODUCT URL>>>>>>" + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            String query1 = "DELETE FROM " + Dbhelper.TABLE_BUX_COUNTER;
            Constant.printMsg("delete query1 ::>>>> " + query1);
            delete(query1);
            progressdialog.dismiss();
            Constant.printMsg("PRODUCT RESULT>>>>>>" + result);
            if (result != null && result.length() > 0) {
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(result);

                } catch (JSONException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                JSONArray jsonArray = null;
                try {
                    jsonArray = jObject.getJSONArray("buxsEarnedDtos");
                    mEarned = new ArrayList<BuxsEarnedDto>();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        jObject = jsonArray.getJSONObject(i);
                        String name = jObject.getString("name");
                        String acheive = jObject.getString("acheive");
                        String buxs = jObject.getString("buxs");

                        BuxsEarnedDto e = new BuxsEarnedDto();
                        e.setName(name);
                        e.setAcheive(acheive);
                        e.setBuxs(buxs);

                        mEarned.add(e);

                        ContentValues cv = new ContentValues();
                        cv.put("activity", name);
                        cv.put("activity_bux", buxs);
                        cv.put("earned_bux", acheive);
                        Constant.printMsg("activity data   " + name + "   "
                                + buxs + "    " + acheive);
                        insertbuxDB(cv);

                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

                if (mEarned.size() > 0) {
                    mBuxSActivityListview.setAdapter(new BuxsPointAdapter(
                            getApplicationContext(), mEarned));
                    // new MyAsync().execute();
                }
            } else {

                Toast.makeText(BuxSActivity.this,
                        "Network Error..Try Again Later", Toast.LENGTH_SHORT)
                        .show();
            }

        }

    }

    private class getRedeemed extends AsyncTask<String, String, String> {
        ProgressDialog progressdialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressdialog = new ProgressDialog(BuxSActivity.this);
            progressdialog.setMessage(getResources()
                    .getString(R.string.loading));
            progressdialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();

            result = ht.httpget(KachingMeConfig.Buxs);
            Constant.printMsg("PRODUCT URL>>>>>>" + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressdialog.dismiss();
            Constant.printMsg("PRODUCT RESULT>>>>>>" + result);
            if (result != null && result.length() > 0) {
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(result);

                } catch (JSONException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                JSONArray jsonArray = null;
                try {
                    jsonArray = jObject.getJSONArray("buxsRedeemedDtos");
                    mRedeemed = new ArrayList<BuxsRedeemedDto>();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        jObject = jsonArray.getJSONObject(i);
                        String name = jObject.getString("retMerName");
                        String buxs = jObject.getString("redeemedBux");

                        BuxsRedeemedDto r = new BuxsRedeemedDto();
                        r.setRetmerName(name);
                        r.setRedeemedBuxs(buxs);

                        mRedeemed.add(r);

                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                if (mRedeemed.size() > 0) {
                    // reddemed_list.setAdapter(new RedeemedAdapter(
                    // getApplicationContext(), mRedeemed));
                    //new MyAsync().execute();
                }
            } else {

                Toast.makeText(BuxSActivity.this,
                        "Network Error..Try Again Later", Toast.LENGTH_SHORT)
                        .show();
            }

        }

    }
}
