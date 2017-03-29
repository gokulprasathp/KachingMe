/*
* @author Gokul
*
*
*
*
* */

package com.wifin.kachingme.deals;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.OfferSummaryAdaptor;
import com.wifin.kachingme.cart.CartActivity;
import com.wifin.kachingme.chat_home.MainActivity;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.CartAdapDto;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OfferSummaryActivity extends MainActivity {

    public static Context context;
    ListView summary;
    Dbhelper db;
    SharedPreferences sp;

    public static Context getContext() {
        // TODO Auto-generated method stub
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
        View.inflate(this, R.layout.activity_offer_summary, vg);
        summary = (ListView) findViewById(R.id.offer_summary_list);
        logo.setVisibility(ImageView.GONE);
        back.setVisibility(ImageView.VISIBLE);
        cart.setVisibility(ImageView.VISIBLE);
        head.setText("Order Summary");
        context = OfferSummaryActivity.this;
        db = new Dbhelper(getApplicationContext());
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        Constant.bux = sp.getLong("buxvalue", 0);
        Constant.userId = sp.getLong("uservalue", 0);

        Constant.printMsg("constan::" + Constant.bux + Constant.userId);
        JsonForm();

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent i = new Intent(OfferSummaryActivity.this,
                        StoreActivity.class);
                startActivity(i);
                finish();

            }
        });

        cart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent i = new Intent(OfferSummaryActivity.this,
                        CartActivity.class);
                startActivity(i);
                finish();

            }
        });

    }

    private void JsonForm() {
        // TODO Auto-generated method stub

        if (Connectivity.isConnected(OfferSummaryActivity.this)) {

            new postID().execute();

        }
    }

    public void onBackPressed() {
        // TODO Auto-generated method stub

        Intent i = new Intent(OfferSummaryActivity.this, StoreActivity.class);
        startActivity(i);
        finish();
    }

    public class postID extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        protected void onPreExecute() {
            // TODO Auto-generated method stub

            super.onPreExecute();
            progressDialog = new ProgressDialog(OfferSummaryActivity.this,
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

            Constant.printMsg("result dis" + KachingMeConfig.MERID_URL
                    + Constant.merchantid);

            result = ht.httpget(KachingMeConfig.MERID_URL + Constant.merchantid);

            Constant.printMsg("result dis" + KachingMeConfig.MERID_URL
                    + Constant.merchantid + "response:::" + result);

            Constant.printMsg("the dic values::::" + result);

            Constant.printMsg("result dis" + result);
            return result;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result != null && result.length() > 0) {
                Constant.cartadap.clear();

                JSONArray jarray;
                try {
                    jarray = new JSONArray(result);
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jObject = jarray.getJSONObject(i);

                        CartAdapDto m = new CartAdapDto();
                        m.setMerchantId(jObject.getString("merchantId"));
                        m.setMerchantName(jObject.getString("merchantName"));
                        m.setDeelOfferDiscountId(jObject
                                .getString("deelOfferDiscountId"));
                        m.setProductName(jObject.getString("productName"));
                        m.setItem(jObject.getString("item"));
                        m.setOffer(jObject.getString("offer"));
                        m.setDiscount(jObject.getString("discount"));
                        m.setBuxs(jObject.getString("buxs"));
                        m.setPhotoPath(jObject.getString("photoPath"));
                        m.setQrCodePath(jObject.getString("qrCodePath"));

                        Constant.printMsg("list Dtyo::" + m.getProductName());

                        Constant.cartadap.add(m);

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                summary.setAdapter(new OfferSummaryAdaptor(
                        OfferSummaryActivity.this, Constant.cartadap));
            } else {

                Toast.makeText(getApplicationContext(),
                        "Network Error!Please try again later!",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

}
