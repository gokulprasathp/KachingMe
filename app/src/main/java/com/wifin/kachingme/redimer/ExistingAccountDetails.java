package com.wifin.kachingme.redimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.pojo.RestClaimDto;
import com.wifin.kachingme.registration_and_login.FreebieActivity;
import com.wifin.kachingme.util.CircleProgressBar;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.Validation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class ExistingAccountDetails extends HeaderActivity
        implements OnClickListener {

    public static HashMap<TextView, CountDownTimer> counters;
    Button $Submit;
    LinearLayout mProfileLayout, mFreebieLayout;
    TextView mMerchantNameText, mMerchantWebsiteTxt, mFreebieText,
            $RedeemerIdLable, $RedeemerLable, $RedeemerAddressLable,
            $EmailIdLable, $PhoneNumberLable, $RedeemerIdEdit;
    EditText $RedeemerEdit, $RedeemerAddressEdit,
            $EmailIdEdit, $PhoneNumberEdit;
    ImageView mProfileImg, mFreebieImg;
    Dialog dialog;
    SharedPreferences sp;
    View mFreebieView;
    int height = 0, width = 0;
    CommonMethods commonMethods;
    private ImageLoadingListener animateFirstListener = new FreebieActivity.AnimateFirstDisplayListener();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.existing_account_details, vg);
//        head.setText("Redeemer Details");
//        logo.setVisibility(ImageView.GONE);
//        back.setVisibility(ImageView.VISIBLE);
//        sideMenufoot.setVisibility(LinearLayout.GONE);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mFooterLayout.setVisibility(View.GONE);
        mFooterView.setVisibility(View.GONE);
        commonMethods = new CommonMethods(ExistingAccountDetails.this);
        mHeading.setText("Redeem");
        mHeaderImg.setVisibility(View.GONE);
        mNextBtn.setVisibility(View.GONE);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExistingAccountDetails.this, ExistingAccount.class);
                startActivity(i);
                finish();
            }
        });

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        existingAccountDetailsInitialization();
        existingAccountDetailsScreenArrange();

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_empty).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();

        mMerchantWebsiteTxt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String url = "http://" + mMerchantWebsiteTxt.getText().toString();
                try {
                    Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    startActivity(intent);
                } catch (Exception e) {

                }
            }
        });
        setData();
    }

    private void existingAccountDetailsInitialization() {
        // TODO Auto-generated method stub

        mFreebieView = (View) findViewById(R.id.freebie_view);

        mProfileLayout = (LinearLayout) findViewById(R.id.existingAccDetails_profile_layout);
        mProfileImg = (ImageView) findViewById(R.id.existingAccDetails_prof_pic);
        mMerchantNameText = (TextView) findViewById(R.id.existingAccDetails_merchant_name);
        mMerchantWebsiteTxt = (TextView) findViewById(R.id.existingAccDetails_merchant_website);
        mFreebieLayout = (LinearLayout) findViewById(R.id.existingAccDetails_freebie_layout);
        mFreebieImg = (ImageView) findViewById(R.id.existingAccDetails_freebie_img);
        mFreebieText = (TextView) findViewById(R.id.existingAccDetails_freebie_value);

        $RedeemerIdLable = (TextView) findViewById(R.id.existingAccDetails_shop_id_text);
        $RedeemerLable = (TextView) findViewById(R.id.existingAccDetails_redeemer_name_text);
        $RedeemerAddressLable = (TextView) findViewById(R.id.existingAccDetails_address_line1_text);
        $EmailIdLable = (TextView) findViewById(R.id.existingAccDetails_email_id_text);
        $PhoneNumberLable = (TextView) findViewById(R.id.existingAccDetails_phone_number_text);

        $RedeemerIdEdit = (TextView) findViewById(R.id.existingAccDetails_shop_id_edit);
        $RedeemerEdit = (EditText) findViewById(R.id.existingAccDetails_redeemer_name_edit);
        $RedeemerAddressEdit = (EditText) findViewById(R.id.existingAccDetails_address_line1_edit);
        $EmailIdEdit = (EditText) findViewById(R.id.existingAccDetails_email_id_edit);
        $PhoneNumberEdit = (EditText) findViewById(R.id.existingAccDetails_phone_number_edit);

        $Submit = (Button) findViewById(R.id.existingAccDetails_submit);
        $Submit.setOnClickListener(this);

        Constant.typeFace(this, mMerchantNameText);
        Constant.typeFace(this, mMerchantWebsiteTxt);
        Constant.typeFace(this, mFreebieText);
        Constant.typeFace(this, $RedeemerIdLable);
        Constant.typeFace(this, $RedeemerLable);
        Constant.typeFace(this, $RedeemerAddressLable);
        Constant.typeFace(this, $EmailIdLable);
        Constant.typeFace(this, $PhoneNumberLable);
        Constant.typeFace(this, $RedeemerIdEdit);
        Constant.typeFace(this, $RedeemerEdit);
        Constant.typeFace(this, $RedeemerAddressEdit);
        Constant.typeFace(this, $EmailIdEdit);
        Constant.typeFace(this, $PhoneNumberEdit);
        Constant.typeFace(this, $Submit);
    }

    public void setData() {
        mMerchantNameText.setText(Constant.mFreebieMerchantName);
        //mMerchantWebsiteTxt.setText(Constant.mFreebieShopWebsite);
        mFreebieText.setText(Constant.mFreebieName);
        ImageLoader.getInstance().displayImage(
                String.valueOf(Constant.mFreeBieMerchantImgUrl.replaceAll(" ",
                        "%20")), mProfileImg, options, animateFirstListener);

        ImageLoader.getInstance().displayImage(
                String.valueOf(Constant.mFreeBieImgUrl.replaceAll(" ", "%20")),
                mFreebieImg, options, animateFirstListener);

        Spanned spannedResult = Html.fromHtml("<font color=#232323><a href='"
                + Constant.mFreebieShopWebsite + "'>" + Constant.mFreebieShopWebsite + "</a></font>");
        mMerchantWebsiteTxt.setText(spannedResult);
        mMerchantWebsiteTxt.setClickable(true);
        mMerchantWebsiteTxt.setMovementMethod(LinkMovementMethod.getInstance());

        $RedeemerIdEdit.setText(Constant.mFreebieShopId);
        $RedeemerEdit.setText(Constant.mFreebieShopName);
        $RedeemerAddressEdit.setText(Constant.mFreebieShopAddress);
        $EmailIdEdit.setText(Constant.mFreebieShopEmailid);
        $PhoneNumberEdit.setText(Constant.mFreebieShopPhoneNumber);
        Constant.printMsg("phone number checkkkkkkkkkkkkkkkk...." + Constant.mFreebieShopPhoneNumber);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.existingAccDetails_submit:
                submissionProcess();
                break;
            default:
                break;
        }
    }

    private void submissionProcess() {
        if ($RedeemerEdit.getText().toString().length() > 0) {
            if ($EmailIdEdit.getText().toString().length() > 0) {
                if (Validation.isEmailAddress($EmailIdEdit, true)) {
                    if ($PhoneNumberEdit.getText().toString().length() > 0) {
                        if ($PhoneNumberEdit.getText().toString().trim().matches("[0-9]+")) {
                            if ($RedeemerAddressEdit.getText().toString().length() > 0) {
                            /*only enter here when all the validation true*/
                                if (Connectivity.isConnected(this)) {
                                    loadRegisterData();
                                } else {
                                    Toast.makeText(this, "Please Check Your Network Connection.!!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                commonMethods.showAlertDialog(this, "Redeemer Address Cannot Be Empty", true);
                            }
                        } else {
                            commonMethods.showAlertDialog(this, "Invalid Phone Number", true);
                        }
                    } else {
                        commonMethods.showAlertDialog(this, "Phone Number Cannot Be Empty", true);
                    }
                } else {
                    commonMethods.showAlertDialog(this, "Enter Valid Email", true);
                }
            } else {
                commonMethods.showAlertDialog(this, "EmailId Cannot Be Empty", true);
            }
        } else {
            commonMethods.showAlertDialog(this, "Redeemer Name Cannot Be Empty", true);
        }
    }

    public void loadRegisterData() {
        RestClaimDto re = new RestClaimDto();
        Long point = sp.getLong("buxvalue", 0);
        Constant.printMsg("bux    " + point);
        re.setBux(point);
        // String ctNum;
        // if (Constant.mPhoneNum.contains("IN")) {
        // ctNum = Constant.mPhoneNum.replace("IN", "");
        // } else {
        // ctNum = Constant.mPhoneNum;
        // }
        re.setPhoneNumber(KachingMeApplication.getjid().split("@")[0]);
        re.setOfferId(Integer.valueOf(Constant.mFreebieId));
        re.setShopId(Integer.valueOf($RedeemerIdEdit.getText().toString()
                .trim()));
        if (Constant.mCartType != null && !Constant.mCartType.isEmpty()) {
            re.setType(Integer.valueOf(Constant.mCartType));
        } else {
            re.setType(0);
        }
        re.setCountryName(Constant.mFreebieCurrentCountry);
        re.setName($RedeemerEdit.getText().toString().trim());
        re.setEmail($EmailIdEdit.getText().toString().trim());
        // re.setLat(mLatitude);
        // re.setLon(mLongitude);
        // String mFinalAdds = null;
        re.setAddress($RedeemerAddressEdit.getText().toString().trim());
        String mRegisterData = new Gson().toJson(re);
        new RegisterShopData().execute(mRegisterData);
    }

    private void countDownPopup() {
        // TODO Auto-generated method stub
        counters = new HashMap<TextView, CountDownTimer>();
        dialog = new Dialog(ExistingAccountDetails.this);
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
        startActivity(new Intent(ExistingAccountDetails.this,
                ExistingAccount.class));
        finish();
    }

    private void existingAccountDetailsScreenArrange() {
        // TODO Auto-generated method stub
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;
        Constant.screenWidth = width;
        Constant.screenHeight = height;

        LinearLayout.LayoutParams profilelayparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        profilelayparams.width = width * 34 / 100;
        profilelayparams.height = width * 34 / 100;
        // profilelayparams.leftMargin = width * 30 / 100;
        // profilelayparams.topMargin = width * 2 / 100;
        profilelayparams.gravity = Gravity.CENTER;
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

        LinearLayout.LayoutParams merTextparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // merTextparams.width = width * 40 / 100;
        merTextparams.gravity = Gravity.CENTER;
        // merTextparams.topMargin = width * 1 / 100;
        mMerchantNameText.setGravity(Gravity.CENTER);
        mMerchantNameText.setLayoutParams(merTextparams);

        LinearLayout.LayoutParams freelayparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//		freelayparams.width = width * 94 / 100;
//		freelayparams.height = width * 26 / 100;
//		freelayparams.topMargin = width * 3 / 100;
//		freelayparams.leftMargin = width * 3 / 100;
        freelayparams.width = width * 96 / 100;
        freelayparams.height = width * 27 / 100;
        freelayparams.topMargin = width * 2 / 100;
        freelayparams.rightMargin = width * 2 / 100;
        freelayparams.leftMargin = width * 2 / 100;
        mFreebieLayout.setLayoutParams(freelayparams);

        LinearLayout.LayoutParams profileimgparams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        profileimgparams1.width = width * 27 / 100;
        profileimgparams1.height = width * 25 / 100;
        profileimgparams1.leftMargin = width * 5 / 100;
        // mProfileImg.setGravity(Gravity.CENTER);
        profileimgparams1.gravity = Gravity.CENTER;
        mFreebieImg.setLayoutParams(profileimgparams1);

        LinearLayout.LayoutParams freebieViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        freebieViewParams.gravity = Gravity.CENTER;
        freebieViewParams.width = (int) (width * 0.5 / 100);
        freebieViewParams.height = width * 23 / 100;
        freebieViewParams.topMargin = width * 2 / 100;
        freebieViewParams.bottomMargin = width * 2 / 100;
        mFreebieView.setLayoutParams(freebieViewParams);

        LinearLayout.LayoutParams freebietextparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        freebietextparams.gravity = Gravity.CENTER;
        // freebietextparams.height = height * 10 / 100;
        freebietextparams.leftMargin = width * 5 / 100;
        // mFreebieText.setGravity(Gravity.CENTER);
        mFreebieText.setLayoutParams(freebietextparams);

        LinearLayout.LayoutParams lableParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lableParama.width = width * 90 / 100;
        // lableParama.height = height * 7.5 / 100;
        lableParama.leftMargin = width * 5 / 100;
        lableParama.rightMargin = width * 5 / 100;
        lableParama.topMargin = (int) (height * 2.5 / 100);
        $RedeemerIdLable.setLayoutParams(lableParama);
        $RedeemerLable.setLayoutParams(lableParama);
        $RedeemerAddressLable.setLayoutParams(lableParama);
        $EmailIdLable.setLayoutParams(lableParama);
        $PhoneNumberLable.setLayoutParams(lableParama);

        $RedeemerIdLable.setGravity(Gravity.CENTER | Gravity.LEFT);
        $RedeemerLable.setGravity(Gravity.CENTER | Gravity.LEFT);
        $RedeemerAddressLable.setGravity(Gravity.CENTER | Gravity.LEFT);
        $EmailIdLable.setGravity(Gravity.CENTER | Gravity.LEFT);
        $PhoneNumberLable.setGravity(Gravity.CENTER | Gravity.LEFT);

        $RedeemerIdLable.setPadding(width * 1 / 100, 0, 0, 0);
        $RedeemerLable.setPadding(width * 1 / 100, 0, 0, 0);
        $RedeemerAddressLable.setPadding(width * 1 / 100, 0, 0, 0);
        $EmailIdLable.setPadding(width * 1 / 100, 0, 0, 0);
        $PhoneNumberLable.setPadding(width * 1 / 100, 0, 0, 0);

        LinearLayout.LayoutParams addressEditParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        addressEditParama.width = width * 90 / 100;
        // addressEditParama.height = (int) (height * 7 / 100);
        addressEditParama.leftMargin = width * 5 / 100;
        addressEditParama.rightMargin = width * 5 / 100;
        addressEditParama.topMargin = height * 1 / 100;
        $RedeemerAddressEdit.setLayoutParams(addressEditParama);
        $RedeemerAddressEdit.setGravity(Gravity.CENTER | Gravity.LEFT);
        $RedeemerAddressEdit.setPadding(width * 2 / 100,
                (int) (height * 1.5 / 100), 0, (int) (height * 1.5 / 100));

        LinearLayout.LayoutParams editParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editParama.width = width * 90 / 100;
        editParama.height = (int) (height * 7 / 100);
        editParama.leftMargin = width * 5 / 100;
        editParama.rightMargin = width * 5 / 100;
        editParama.topMargin = height * 1 / 100;
        $RedeemerIdEdit.setLayoutParams(editParama);
        $RedeemerEdit.setLayoutParams(editParama);
        $EmailIdEdit.setLayoutParams(editParama);
        $PhoneNumberEdit.setLayoutParams(editParama);

        $RedeemerIdEdit.setGravity(Gravity.CENTER | Gravity.LEFT);
        $RedeemerEdit.setGravity(Gravity.CENTER | Gravity.LEFT);
        $EmailIdEdit.setGravity(Gravity.CENTER | Gravity.LEFT);
        $PhoneNumberEdit.setGravity(Gravity.CENTER | Gravity.LEFT);

        $RedeemerIdEdit.setPadding(width * 2 / 100, 0, 0, 0);
        $RedeemerEdit.setPadding(width * 2 / 100, 0, 0, 0);
        $EmailIdEdit.setPadding(width * 2 / 100, 0, 0, 0);
        $PhoneNumberEdit.setPadding(width * 2 / 100, 0, 0, 0);

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
        // ScaleDrawable sd = new ScaleDrawable(icon, 0, 0, 0);
//		$Submit.setCompoundDrawables(icon, null, null, null);

        LinearLayout.LayoutParams layoutdatat = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutdatat.height = (int) (height * 83 / 100);
        layoutdatat.gravity = Gravity.CENTER;
        layoutdatat.weight = 1;
//        datalay.setLayoutParams(layoutdatat);

        if (width >= 600) {
            $PhoneNumberLable.setTextSize(17);
            $RedeemerIdLable.setTextSize(17);
            $RedeemerLable.setTextSize(17);
            $RedeemerAddressLable.setTextSize(17);
            $EmailIdLable.setTextSize(17);
            $RedeemerIdEdit.setTextSize(17);
            $RedeemerEdit.setTextSize(17);
            $RedeemerAddressEdit.setTextSize(17);
            $EmailIdEdit.setTextSize(17);
            mMerchantNameText.setTextSize(17);
            mMerchantWebsiteTxt.setTextSize(17);
            mFreebieText.setTextSize(17);
            $Submit.setTextSize(17);
        } else if (width > 501 && width < 600) {
            $PhoneNumberLable.setTextSize(16);

            $RedeemerIdLable.setTextSize(16);
            $RedeemerLable.setTextSize(16);
            $RedeemerAddressLable.setTextSize(16);
            $EmailIdLable.setTextSize(16);
            $RedeemerIdEdit.setTextSize(16);
            $RedeemerEdit.setTextSize(16);
            $RedeemerAddressEdit.setTextSize(16);
            $EmailIdEdit.setTextSize(16);
            mMerchantNameText.setTextSize(16);
            mMerchantWebsiteTxt.setTextSize(16);
            mFreebieText.setTextSize(16);
            $Submit.setTextSize(16);
        } else if (width > 260 && width < 500) {
            $PhoneNumberLable.setTextSize(15);

            $RedeemerIdLable.setTextSize(15);
            $RedeemerLable.setTextSize(15);
            $RedeemerAddressLable.setTextSize(15);
            $EmailIdLable.setTextSize(15);
            $RedeemerIdEdit.setTextSize(15);
            $RedeemerEdit.setTextSize(15);
            $RedeemerAddressEdit.setTextSize(15);
            $EmailIdEdit.setTextSize(15);
            mMerchantNameText.setTextSize(15);
            mMerchantWebsiteTxt.setTextSize(15);
            mFreebieText.setTextSize(15);
            $Submit.setTextSize(15);
        } else if (width <= 260) {
            $PhoneNumberLable.setTextSize(14);

            $RedeemerIdLable.setTextSize(14);
            $RedeemerLable.setTextSize(14);
            $RedeemerAddressLable.setTextSize(14);
            $EmailIdLable.setTextSize(14);
            $RedeemerIdEdit.setTextSize(14);
            $RedeemerEdit.setTextSize(14);
            $RedeemerAddressEdit.setTextSize(14);
            $EmailIdEdit.setTextSize(14);
            mMerchantNameText.setTextSize(14);
            mMerchantWebsiteTxt.setTextSize(14);
            mFreebieText.setTextSize(14);
            $Submit.setTextSize(14);
        }
    }

    private class RegisterShopData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ExistingAccountDetails.this,
                    AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setProgressDrawable(new ColorDrawable(
                    android.graphics.Color.BLUE));
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            // progressDialog.show();

            countDownPopup();

        }

        @Override
        protected String doInBackground(String... params) {
            String result;
            HttpConfig ht = new HttpConfig();

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
            progressDialog.dismiss();
            cancelAllTimers();
            dialog.dismiss();

            if (result != null && result.length() > 0) {
                if (result.equalsIgnoreCase("Error on creating shop")) {

                    showToast("Failure in Redeemer registration.Please try again");
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
                } else if (result.equalsIgnoreCase("Redeemed Successfully")) {
                    Intent i = new Intent(getApplicationContext(),
                            RedeemedSuccessActivity.class);
                    i.putExtra("is_success", "Success");
                    i.putExtra("failure_msg",
                            "Redeemed your Freebie.");
                    startActivity(i);

                } else if (result.equalsIgnoreCase("Not a valid Shop-Id")) {
                    Intent i = new Intent(getApplicationContext(),
                            RedeemedSuccessActivity.class);
                    i.putExtra("is_success", "Failure");
                    i.putExtra("failure_msg", "your shop id is not valid");
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
                } else if (result.contains(":")) {

                    String[] str_msg_data_array = result.trim().split(":");

                    Intent i = new Intent(getApplicationContext(),
                            RedeemedSuccessActivity.class);
                    i.putExtra("is_success", "Success");
                    i.putExtra(
                            "failure_msg",
                            "registered as a KaChing.me Redeemer Your Redeemer ID Is "
                                    + str_msg_data_array[1]
                                    + " Please make a note of your Redeemer ID# Please check your email for further instructions Thank you.");
                    startActivity(i);

                    // Congratulation! You have successfully registered as a
                    // KaChing.me Redeemer Your Redeemer ID Is A31269 Please
                    // make a note of your Redeemer ID# Please check your email
                    // for further instructions Thank you.
                }
            } else {
                showToast("Failure in Redeemer registration.Please try again");

            }

        }
    }
}
