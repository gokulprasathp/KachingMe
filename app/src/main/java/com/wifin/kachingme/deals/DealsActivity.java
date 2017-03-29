/*
* @author Gokul
*
* @usage -  This class is used to display the available deals
*
*
* */

package com.wifin.kachingme.deals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.DealMain_Adap;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;

public class DealsActivity extends SliderTesting {

    public static Context context;
    MultiAutoCompleteTextView searchedit;
    ListView searchlist;
    LinearLayout outer;
    ProgressDialog progressDialog;
    int width, height;

    public static Context getContext() {
        // TODO Auto-generated method stub
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       ViewGroup vg = (ViewGroup) findViewById(R.id.main_layout_inner);
        View.inflate(this, R.layout.activity_deals, vg);
        context = DealsActivity.this;
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Init();
        ScreenArrange();
        Drawable icon = getResources().getDrawable(R.drawable.search19);
        icon.setBounds(0, 0, (int) (icon.getIntrinsicWidth() * 0.4),
                (int) (icon.getIntrinsicHeight() * 0.4));
        searchedit.setCompoundDrawables(null, null, icon, null);

        JsonForm();

        searchedit.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        /**
         * Enabling Search Filter
         * */
        searchedit.addTextChangedListener(new TextWatcher() {

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

    public void getSearchText(String ch) {

        ArrayList deelct = new ArrayList();

        if (ch != null && ch.length() > 0) {

            for (int i = 0; i < Constant.chatdeel.size(); i++) {

                if (Pattern
                        .compile(Pattern.quote(ch), Pattern.CASE_INSENSITIVE)
                        .matcher(Constant.chatdeel.get(i).toString()).find()) {

                    deelct.add(Constant.chatdeel.get(i).toString());
                }

            }

        } else {

            deelct = Constant.chatdeel;

        }

        DealMain_Adap dailyAdapter = new DealMain_Adap(this, deelct);
        searchlist.setAdapter(dailyAdapter);
        dailyAdapter.notifyDataSetChanged();

    }

    private void Init() {
        // TODO Auto-generated method stub

        searchlist = (ListView) findViewById(R.id.deals_list);
        searchedit = (MultiAutoCompleteTextView) findViewById(R.id.search_deals);
        outer = (LinearLayout) findViewById(R.id.deels_outer_lay);

        try {
            Constant.typeFace(this, searchedit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ScreenArrange() {
        // TODO Auto-generated method stub
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        LinearLayout.LayoutParams layoutenter = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutenter.width = width * 85 / 100;
        layoutenter.height = height * 8 / 100;
        layoutenter.gravity = Gravity.CENTER;
        layoutenter.setMargins(width * 2 / 100, height * 3 / 100,
                width * 4 / 100, height * 2 / 100);
        searchedit.setLayoutParams(layoutenter);
        searchedit.setPadding(width * 2 / 100, 0, 0, 0);

        LinearLayout.LayoutParams layoutout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutout.width = width;
        layoutout.height = height * 10 / 100;
        layoutout.gravity = Gravity.CENTER;

        outer.setLayoutParams(layoutout);
    }

    private void JsonForm() {
        // TODO Auto-generated method stub

        if (Connectivity.isConnected(DealsActivity.this)) {

            new getKeyword().execute();

        }

    }

    public void SetAdpter() {
        // TODO Auto-generated method stub

        if (Constant.blinkoffer.size() > 0) {

            Constant.printMsg("Duplicates List " + Constant.blinkoffer
                    + Constant.chatdeel);

            Object[] st = Constant.blinkoffer.toArray();
            for (Object s : st) {
                if (Constant.blinkoffer.indexOf(s) != Constant.blinkoffer
                        .lastIndexOf(s)) {
                    Constant.blinkoffer.remove(Constant.blinkoffer
                            .lastIndexOf(s));
                }
            }

            Constant.printMsg("Distinct List " + Constant.blinkoffer
                    + Constant.chatdeel);

            for (int k = 0; k < Constant.blinkoffer.size(); k++) {

                String ch = Constant.blinkoffer.get(k).toString().toLowerCase();

                for (int l = 0; l < Constant.chatdeel.size(); l++) {

                    String dl = Constant.chatdeel.get(l).toString()
                            .toLowerCase();

                    if (ch.equals(dl)) {

                        Constant.chatdeel.remove(l);

                        Constant.chatdeel.add(0, ch);

                    }
                }
            }

            Constant.printMsg("Distinct List 1" + Constant.blinkoffer
                    + Constant.chatdeel);

            for (int k = 0; k < Constant.chatdeel.size(); k++) {

                String ch = Constant.chatdeel.get(k).toString().toLowerCase();

                for (int l = 0; l < Constant.blinkoffer.size(); l++) {

                    String dl = Constant.blinkoffer.get(l).toString()
                            .toLowerCase();

                    if (ch.equals(dl)) {

                        Constant.blinkPos.add(k);

                    }
                }
            }

        }

        if (Constant.chatdeel.size() > 0) {

            searchlist.setAdapter(new DealMain_Adap(this, Constant.chatdeel));

        } else {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(
                    DealsActivity.this);
            builder1.setMessage("Sorry No Deals Near By Your Location");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            final Dialog nagDialog = new Dialog(
                                    DealsActivity.this);
                            nagDialog
                                    .requestWindowFeature(Window.FEATURE_NO_TITLE);
                            nagDialog
                                    .getWindow()
                                    .setBackgroundDrawable(
                                            new ColorDrawable(
                                                    android.graphics.Color.TRANSPARENT));

                            nagDialog.setCancelable(true);

                            Intent i = new Intent(DealsActivity.this,
                                    SliderTesting.class);
                            startActivity(i);
                            finish();

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }

    }

    public class getKeyword extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            // TODO Auto-generated method stub

            super.onPreExecute();

            progressDialog = new ProgressDialog(DealsActivity.this,
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

            result = ht.httpget(KachingMeConfig.KEYWORD_URL);

            Constant.printMsg("the key values::::" + result);

            Constant.printMsg("result dis" + result);
            return result;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result != null && result.length() > 0) {

                Constant.chatdeel.clear();

                JSONArray jarray;
                try {
                    jarray = new JSONArray(result);
                    for (int i = 0; i < jarray.length(); i++) {

                        Constant.chatdeel
                                .add(jarray.getString(i).toLowerCase());

                        Constant.printMsg("ouyput:::" + Constant.chatdeel);

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                HashSet hs = new HashSet();
                hs.addAll(Constant.chatdeel);
                Constant.chatdeel.clear();
                Constant.chatdeel.addAll(hs);

                Constant.printMsg("ouyput:::after" + Constant.chatdeel);

                SetAdpter();

                getSearchText(null);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Network Error!Please try again later!",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

}
