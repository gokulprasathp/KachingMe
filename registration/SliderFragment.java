package com.wifin.kachingme.registration_and_login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.wifin.kachingme.R;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.Validation;
import com.wifin.kachingme.util.WebConfig;

import org.json.JSONObject;

import de.measite.minidns.record.A;

/**
 * Created by siva(wifin) on 21/09/2016
 */
public class SliderFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    EditText sEmailEdit;
    TextView sSubmit,sInfoOne,sInfoTwo,sInfoThree,sInfoFour,sInfoFive,sOrText;
    ImageView sInfoImageOne,sInfoImageTwo,sInfoImageThree,sInfoImageFour,sInfoImageFive;
    LinearLayout sSocialMediaLayout,sViewLayout,sInfoLayoutOne,sInfoLayoutTwo,sInfoLayoutThree,sInfoLayoutFour,sInfoLayoutFive;
    Dbhelper db;
    SignInButton sGPlusLogin;
    LoginButton sFbLogin;
    ImageView sFbImage,sGPlusImage;
    View sOrView1,sOrView2;

    public static SliderFragment newInstance(int page, String title) {
        SliderFragment fragmentFirst = new SliderFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=null;
        switch (page) {
            case 0:
                view = inflater.inflate(R.layout.slide_1, container, false);
                loginProcess(view);
                break;
            case 1:
                view = inflater.inflate(R.layout.slide_3, container, false);
                break;
            case 2:
                view = inflater.inflate(R.layout.slide_4, container, false);
                break;
            case 3:
                view = inflater.inflate(R.layout.slide_5, container, false);
                break;
            case 4:
                view = inflater.inflate(R.layout.slide_6, container, false);
                break;
            case 5:
                view = inflater.inflate(R.layout.slide_7, container, false);
                break;
        }
        return view;
    }

    private void loginProcess(View view) {
        initialization(view);
        screenArrange();
        sEmailEdit.addTextChangedListener(new TextWatcher() {
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
                if (s.length() > 0) {
                    sSubmit.setVisibility(View.VISIBLE);
                    sSocialMediaLayout.setVisibility(View.GONE);
                    sViewLayout.setVisibility(View.GONE);
                } else {
                    sSubmit.setVisibility(View.GONE);
                    sSocialMediaLayout.setVisibility(View.VISIBLE);
                    sViewLayout.setVisibility(View.VISIBLE);
                }}
        });

        sSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new OtpSharedPreference(getActivity())
                        .clearRegistrationDetails();
                String mail = sEmailEdit.getText().toString();
                if (mail.trim().length() > 0) {
                    if (Validation.isEmailAddress(sEmailEdit, true)) {
                        DbDelete();
                        Constant.manualmail = sEmailEdit.getText().toString();
                        if (Connectivity.isConnected(getActivity())) {
                            new mailValidation().execute();
//                            checkMailUsingVolley();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Please check your internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                  //  fetchFrom();
                  //  startActivity(new Intent(getActivity(), RegisterActivity.class));
                  //  getActivity().finish();
                }
            }
        });
    }
    private void initialization(View view) {
        sEmailEdit = (EditText)view.findViewById(R.id.slide_mailEdit);
        sSubmit = (TextView) view.findViewById(R.id.slide_submit);
        sInfoOne = (TextView) view.findViewById(R.id.slide_text1);
        sInfoTwo = (TextView) view.findViewById(R.id.slide_text2);
        sInfoThree = (TextView) view.findViewById(R.id.slide_text3);
        sInfoFour = (TextView) view.findViewById(R.id.slide_text4);
        sInfoFive = (TextView) view.findViewById(R.id.slide_text5);
        sOrText = (TextView) view.findViewById(R.id.slide_txtOr);
        sInfoImageOne = (ImageView) view.findViewById(R.id.slide_tick1);
        sInfoImageTwo = (ImageView) view.findViewById(R.id.slide_tick2);
        sInfoImageThree = (ImageView) view.findViewById(R.id.slide_tick3);
        sInfoImageFour = (ImageView) view.findViewById(R.id.slide_tick4);
        sInfoImageFive = (ImageView) view.findViewById(R.id.slide_tick5);
        sInfoLayoutOne = (LinearLayout) view.findViewById(R.id.slide_infoLayout1);
        sInfoLayoutTwo = (LinearLayout) view.findViewById(R.id.slide_infoLayout2);
        sInfoLayoutThree = (LinearLayout) view.findViewById(R.id.slide_infoLayout3);
        sInfoLayoutFour = (LinearLayout) view.findViewById(R.id.slide_infoLayout4);
        sInfoLayoutFive = (LinearLayout) view.findViewById(R.id.slide_infoLayout5);
        sViewLayout = (LinearLayout) view.findViewById(R.id.slide_viewLayout);
        sSocialMediaLayout= (LinearLayout) view.findViewById(R.id.slide_socialMediaLayout);
        sGPlusLogin = (SignInButton)view. findViewById(R.id.signin);
        sFbLogin = (LoginButton)view. findViewById(R.id.authButton);
        sOrView1= (View)view. findViewById(R.id.slide_view1);
        sOrView2= (View)view. findViewById(R.id.slide_view2);
        sFbImage = (ImageView)view. findViewById(R.id.facebook_login);
        sGPlusImage = (ImageView)view. findViewById(R.id.google_login);

        Constant.typeFace(getActivity(),sEmailEdit);
        Constant.typeFace(getActivity(),sSubmit);
        Constant.typeFace(getActivity(),sInfoOne);
        Constant.typeFace(getActivity(),sInfoTwo);
        Constant.typeFace(getActivity(),sInfoThree);
        Constant.typeFace(getActivity(),sInfoFour);
        Constant.typeFace(getActivity(),sInfoFive);
        Constant.typeFace(getActivity(),sOrText);
    }

//    private void checkMailUsingVolley() {
////        String tag_json_obj = "json_obj_req";
//
//        String url = WebConfig.Email_Verification +"emailid="+ Constant.manualmail;
//
//        final ProgressDialog pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Loading...");
//        pDialog.show();
//
//        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
//                url, null,
//                new Response.Listener<StringRequest>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.hide();
//                        Constant.printMsg("response..volley......"+response);
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // hide the progress dialog
//                pDialog.hide();
//                Constant.printMsg("response..volley....error.." + error);
//            }
//        });
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        jsonObjReq.setShouldCache(false);
//        queue.getCache().remove(url);
//        queue.add(jsonObjReq);
//    }

    public class mailValidation extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();

            result = ht.httpget(WebConfig.Email_Verification + "emailid="
                    + Constant.manualmail);
            Constant.printMsg("post link mail verificatio....."+WebConfig.Email_Verification + "emailid="
                    + Constant.manualmail);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
            Constant.printMsg("result ::::::: >>>>>> " + result);
            // Toast.makeText(getApplicationContext(), "Email " + result,
            // Toast.LENGTH_LONG).show();
            if (result != null && result.length() > 0) {

                if (result.equalsIgnoreCase("true")) {
                    startActivity(new Intent(getActivity(), RegisterActivity.class));
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(),
                            "Email already exist", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(),
                        "Server Down Try Again Later", Toast.LENGTH_LONG)
                        .show();
            }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setProgressDrawable(new ColorDrawable(
                    android.graphics.Color.BLUE));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }
    public void DbDelete() {
        // TODO Auto-generated method stub
        db = new Dbhelper(getActivity());
        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_SOCIAL, null, null);
            System.out
                    .println("No of deleted rows from bux data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void fetchFrom() {

        String namet = null, mailt = null, phtt = null;

        Dbhelper db = new Dbhelper(getActivity());
        Cursor c = null;
        try {
            c = db.open()
                    .getDatabaseObj()
                    .query(Dbhelper.TABLE_SOCIAL, null, null, null, null, null,
                            null);
            int nm = c.getColumnIndex("name");
            int ml = c.getColumnIndex("mail");
            int ph = c.getColumnIndex("photo");

            Constant.printMsg("The pending cart list in db ::::"
                    + c.getCount());
            while (c.moveToNext()) {
                namet = c.getString(nm);
                mailt = c.getString(ml);
                phtt = c.getString(ph);

            }
        } catch (SQLException e) {

            Constant.printMsg("Sql exception in pending shop details ::::"
                    + e.toString());
        } finally {
            c.close();
            db.close();
        }
        Constant.profilename = namet;
        Constant.printMsg("Google Plus Test222");
    }


    private void screenArrange() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        LinearLayout.LayoutParams layoutOneParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutOneParama.width = width * 80 / 100;
        layoutOneParama.height = height * 3 / 100;
        layoutOneParama.setMargins(width * 1 / 100, height * 14 / 100, 0, width * 1 / 100);
        layoutOneParama.gravity = Gravity.CENTER;
        sInfoLayoutOne.setLayoutParams(layoutOneParama);

        LinearLayout.LayoutParams layoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParama.width = width * 80 / 100;
        layoutParama.height = height * 3 / 100;
        layoutParama.gravity = Gravity.CENTER;
        layoutParama.setMargins(width * 1 / 100, width * 1 / 100, 0, width * 1 / 100);
        sInfoLayoutTwo.setLayoutParams(layoutParama);
        sInfoLayoutThree.setLayoutParams(layoutParama);
        sInfoLayoutFour.setLayoutParams(layoutParama);
        sInfoLayoutFive.setLayoutParams(layoutParama);

        LinearLayout.LayoutParams tickImageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tickImageParama.width = width * 5 / 100;
        tickImageParama.height =(int) (height * 2 / 100);
        tickImageParama.gravity = Gravity.LEFT|Gravity.CENTER;
//        tickImageParama.setMargins(width * 1 / 100,0,0,0);
        sInfoImageOne.setLayoutParams(tickImageParama);
        sInfoImageTwo.setLayoutParams(tickImageParama);
        sInfoImageThree.setLayoutParams(tickImageParama);
        sInfoImageFour.setLayoutParams(tickImageParama);
        sInfoImageFive.setLayoutParams(tickImageParama);

        LinearLayout.LayoutParams textParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
       // textParama.width = width * 95 / 100;
//        textParama.height = height * 3 / 100;
        textParama.gravity = Gravity.CENTER|Gravity.LEFT;
        textParama.setMargins(width * 1 / 100,0, 0, 0);
        sInfoOne.setLayoutParams(textParama);
        sInfoTwo.setLayoutParams(textParama);
        sInfoThree.setLayoutParams(textParama);
        sInfoFour.setLayoutParams(textParama);
        sInfoFive.setLayoutParams(textParama);
        sInfoOne.setGravity(Gravity.CENTER | Gravity.LEFT);
        sInfoTwo.setGravity(Gravity.CENTER | Gravity.LEFT);
        sInfoThree.setGravity(Gravity.CENTER | Gravity.LEFT);
        sInfoFour.setGravity(Gravity.CENTER | Gravity.LEFT);
        sInfoFive.setGravity(Gravity.CENTER | Gravity.LEFT);

        LinearLayout.LayoutParams emailParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        emailParama.width = width * 80 / 100;
        emailParama.height = (int)( height * 7 / 100);
        emailParama.gravity = Gravity.CENTER;
        emailParama.topMargin=height*3/100;
        sEmailEdit.setLayoutParams(emailParama);
        sEmailEdit.setGravity(Gravity.LEFT | Gravity.CENTER);
        sEmailEdit.setPadding(width*3/100,0,0,0);

        LinearLayout.LayoutParams submitParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        submitParama.width = width * 80 / 100;
        submitParama.height = (int)(height * 7 / 100);
        submitParama.topMargin = height * 2 / 100;
        submitParama.gravity = Gravity.CENTER;
        sSubmit.setLayoutParams(submitParama);
        sSubmit.setGravity(Gravity.CENTER);


        LinearLayout.LayoutParams viewLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        viewLayoutParama.width = width * 80 / 100;
        //viewLayoutParama.height = (int)( height * 0.3/ 100);
        viewLayoutParama.gravity = Gravity.CENTER;
        viewLayoutParama.topMargin=height*2/100;
        sViewLayout.setLayoutParams(viewLayoutParama);

        LinearLayout.LayoutParams viewParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        viewParama.width = width * 36 / 100;
        viewParama.height = (int)( height * 0.3/ 100);
        viewParama.gravity = Gravity.CENTER;
        sOrView1.setLayoutParams(viewParama);
        sOrView2.setLayoutParams(viewParama);

        LinearLayout.LayoutParams orTextParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        orTextParama.width = width * 8 / 100;
        orTextParama.height = (int)( width * 8/ 100);
        orTextParama.gravity = Gravity.CENTER;
        sOrText.setLayoutParams(orTextParama);
        sOrText.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams socialLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        socialLayoutParama.width = width * 100 / 100;
        //socialLayoutParama.height = (int)( height * 7/ 100);
        socialLayoutParama.gravity = Gravity.CENTER;
        socialLayoutParama.topMargin=height*2/100;
        sSocialMediaLayout.setLayoutParams(socialLayoutParama);
        sSocialMediaLayout.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams fbParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        fbParama.width = (int) (width * 38 / 100);
        fbParama.height = (int) (height * 8 / 100);
//        fbParama.leftMargin=width*1/2/100;
//        fbParama.rightMargin=width*1/2/100;//reduce for left and right
        //fbParama.topMargin=height*1/100;
        fbParama.gravity = Gravity.CENTER;
        sFbLogin.setLayoutParams(fbParama);
        sFbLogin.setPadding(0, width * 7/2/ 100, 0, width * 7/2 / 100);//reduce for top and bottom
        sFbLogin.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams gPlusParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        gPlusParama.width = (int) (width * 42 / 100);
        gPlusParama.height = (int) (height * 8 / 100);
        gPlusParama.gravity = Gravity.CENTER;
        sGPlusLogin.setLayoutParams(gPlusParama);

        LinearLayout.LayoutParams fbImageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        fbImageParama.width = (int) (width * 79.5/2 / 100);
        fbImageParama.height = (int) (height * 6 / 100);
        fbImageParama.gravity = Gravity.CENTER;
        sFbImage.setLayoutParams(fbImageParama);

        LinearLayout.LayoutParams gPlusImageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        gPlusImageParama.width = (int) (width * 79.5/2/ 100);
        gPlusImageParama.height = (int) (height * 6 / 100);
        gPlusImageParama.gravity = Gravity.CENTER;
        gPlusImageParama.leftMargin=width*1/2/100;
        sGPlusImage.setLayoutParams(gPlusImageParama);

        if (width >= 600) {
            sInfoOne.setTextSize(13);
            sInfoTwo.setTextSize(13);
            sInfoThree.setTextSize(13);
            sInfoFour.setTextSize(13);
            sInfoFive.setTextSize(13);
            sEmailEdit.setTextSize(16);
            sSubmit.setTextSize(16);
            //sFbLogin.setTextSize(13);
            sOrText.setTextSize(13);
        } else if (width > 501 && width < 600) {
            sInfoOne.setTextSize(12);
            sInfoTwo.setTextSize(12);
            sInfoThree.setTextSize(12);
            sInfoFour.setTextSize(12);
            sInfoFive.setTextSize(12);
            sEmailEdit.setTextSize(15);
            sSubmit.setTextSize(15);
            //sFbLogin.setTextSize(15);
            sOrText.setTextSize(12);
        } else if (width > 260 && width < 500) {
            sInfoOne.setTextSize(11);
            sInfoTwo.setTextSize(11);
            sInfoThree.setTextSize(11);
            sInfoFour.setTextSize(11);
            sInfoFive.setTextSize(11);
            sEmailEdit.setTextSize(14);
            sSubmit.setTextSize(14);
            //sFbLogin.setTextSize(14);
            sOrText.setTextSize(11);
        } else if (width <= 260) {
            sInfoOne.setTextSize(10);
            sInfoTwo.setTextSize(10);
            sInfoThree.setTextSize(10);
            sInfoFour.setTextSize(10);
            sInfoFive.setTextSize(10);
            sEmailEdit.setTextSize(13);
            sSubmit.setTextSize(13);
            //sFbLogin.setTextSize(13);
            sOrText.setTextSize(10);
        }
    }
}