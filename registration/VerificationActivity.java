package com.wifin.kachingme.registration_and_login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wifin.kachingme.R;
import com.wifin.kachingme.applications.NiftyApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.services.ContactLastSync;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.AvatarManager;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.RounderImageView;

import org.apache.http.Header;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//import cz.msebera.android.httpclient.Header;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;

public class VerificationActivity extends Slideshow implements View.OnClickListener {

    TextView mHeadText1, mHeadText2, mHeadText3, mPerimaryVerification, mAddSecondary, mNextButton;
    RounderImageView mProfilePicture;
    int width, height;

    //	static int backpress = 0;
    Bitmap bmp = null;
    String country_code, full_mobile_no, mobileno;
    String TAG = VerificationActivity.class.getSimpleName();
    //	private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    int count = 1, seXvalue;
    Editor editor;
    DatabaseHelper dbAdapter;
    SharedPreferences preference;
    byte[] img_byte;
    ProgressDialog progressdialog;
    boolean decline = false;
    CommonMethods commonMethods = new CommonMethods(VerificationActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.verification_activity);
        ViewGroup vg = (ViewGroup) findViewById(R.id.register_mainLayout);
        View.inflate(this, R.layout.verification_activity, vg);
        intializeVerification();
        screenArrangeVerification();
        sIndicator.setVisibility(View.GONE);

        dbAdapter = NiftyApplication.getDatabaseAdapter();
        preference = getSharedPreferences(NiftyApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        editor = preference.edit();


//		if (Constant.mFromsettingVerfication) {
////			logo.setVisibility(ImageView.GONE);
////			back.setVisibility(ImageView.VISIBLE);
//			if (mAddSecondary.getVisibility() == View.VISIBLE) {
//
//			} else {
//
//			}
//		}


        img_byte = Constant.byteimage;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            seXvalue = extras.getInt("sexId", 0);
            Constant.printMsg("seXvalue is::" + seXvalue);
        }
        count = preference.getInt("sec_count", 0);

        Constant.printMsg("count add:" + count);
        if (count == 1) {
            mAddSecondary.setText("Add Third Number");
            Toast.makeText(VerificationActivity.this, "Added Second Number",
                    Toast.LENGTH_SHORT).show();
        } else if (count == 2) {
            mAddSecondary.setText("Add Fourth Number");
            Toast.makeText(VerificationActivity.this, "Added Third Number",
                    Toast.LENGTH_SHORT).show();
        } else if (count == 3) {
            mAddSecondary.setText("Add Fifth Number");
            Toast.makeText(VerificationActivity.this, "Added Fourth Number",
                    Toast.LENGTH_SHORT).show();
        } else if (count == 4) {
            Toast.makeText(VerificationActivity.this, "Added Fifth Number",
                    Toast.LENGTH_SHORT).show();
        } else {

        }

        Constant.printMsg("countttttttt:::::>>>>>>" + count);
        if (count >= 4) {
            mAddSecondary.setVisibility(View.GONE);
            showAlertDialog1(this, getResources().getString(R.string.phone_number_exceed),
                    true);
        } else {
            mAddSecondary.setVisibility(View.VISIBLE);
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mobileno = bundle.getString("mobileno");
            country_code = bundle.getString("country_code");
            full_mobile_no = mobileno;
            Log.d(TAG, "Mobile NO::" + mobileno);
            Constant.printMsg("country:::ver" + mobileno);
        }

        if (Constant.settings == false) {
            if (Constant.freelistmain.size() > 0) {
                // veradd.setVisibility(Button.VISIBLE);
                mPerimaryVerification.setVisibility(Button.GONE);
                mNextButton.setVisibility(Button.VISIBLE);
                mHeadText1.setText("First Text Messaging App that Rewards You!");
                mHeadText2.setText("Text more, Earn more.");
                mHeadText3.setText("Add Multiple Phone Numbers to One Account");
            } else {
                mAddSecondary.setVisibility(Button.GONE);
                mPerimaryVerification.setVisibility(Button.VISIBLE);
                mNextButton.setVisibility(Button.GONE);

            }
        } else {
            // veradd.setVisibility(Button.VISIBLE);
            mPerimaryVerification.setVisibility(Button.GONE);
            mNextButton.setVisibility(Button.GONE);
            mHeadText1.setText("First Text Messaging App that Rewards You!");
            mHeadText2.setText("Text more, Earn more.");
            mHeadText3.setText("Add Multiple Phone Numbers to One Account");
        }
        if (Constant.byteimage != null) {
            bmp = BitmapFactory.decodeByteArray(Constant.byteimage, 0,
                    Constant.byteimage.length);
        }
        if (bmp != null) {
            mProfilePicture.setImageBitmap(new AvatarManager().roundCornerImage(bmp, 180));
        }

        if (Constant.manualmail == null) {
            Constant.manualmail = Constant.profilemail;
            new LoadProfileImage(mProfilePicture).execute(Constant.profileimg);
        } else {
            //head.setText("Welcome  " + Constant.mFirstName + "!!");
        }
    }

    private void intializeVerification() {
        mHeadText1 = (TextView) findViewById(R.id.ver_text1);
        mHeadText2 = (TextView) findViewById(R.id.ver_text2);
        mHeadText3 = (TextView) findViewById(R.id.ver_text3);
        mPerimaryVerification = (TextView) findViewById(R.id.ver_primaryBtn);
        mAddSecondary = (TextView) findViewById(R.id.ver_addSecondaryBtn);
        mNextButton = (TextView) findViewById(R.id.ver_NextButton);
        mProfilePicture = (RounderImageView) findViewById(R.id.ver_login_ban);

        Constant.typeFaceBold(this, mHeadText1);
        Constant.typeFace(this, mHeadText2);
        Constant.typeFace(this, mHeadText3);
        Constant.typeFace(this, mPerimaryVerification);
        Constant.typeFace(this, mAddSecondary);
        Constant.typeFace(this, mNextButton);

        mPerimaryVerification.setOnClickListener(this);
        mAddSecondary.setOnClickListener(this);
        mNextButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.ver_primaryBtn:
                Constant.ifSecondary = false;
                Constant.mPrimarynumBtn = true;
                startActivity(new Intent(VerificationActivity.this, Signin.class));
                finish();
                break;
            case R.id.ver_addSecondaryBtn:
                addSeondaryNumber();
                break;
            case R.id.ver_NextButton:
                freeBeDialgBox();
                break;
        }
    }

    private void addSeondaryNumber() {
        Constant.ifSecondary = true;
        Constant.mBackInvisible = true;
        Constant.addverification = true;
        Constant.printMsg("conut ::::: >>>" + count + 1);
        setshared(count + 1);
        Intent ii = new Intent(VerificationActivity.this, Signin.class);
        Constant.fullmob = full_mobile_no;
        Constant.countrycode = country_code;
        ii.putExtra("mobileno", full_mobile_no);
        ii.putExtra("country_code", country_code);
        startActivity(ii);
        finish();
    }

    private void freeBeDialgBox() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.freebie_dialogbox);
        dialog.setCancelable(true);
        TextView dialogText1 = (TextView) dialog.findViewById(R.id.freebieDialog_Text1);
        TextView dialogText2 = (TextView) dialog.findViewById(R.id.freebieDialog_Text2);
        TextView dialogText3 = (TextView) dialog.findViewById(R.id.freebieDialog_Text3);
        TextView dialogAgree = (TextView) dialog.findViewById(R.id.freebieDialog_agree);
        TextView dialogDecline = (TextView) dialog.findViewById(R.id.freebieDialog_decline);
        LinearLayout dialogAgreeLayout = (LinearLayout) dialog.findViewById(R.id.freebieDialog_agreeLayout);

        dialogText1.setText("Disclaimer:");
        dialogText2.setText("Select Your Freebiew Now!");
        dialogText3.setText("By Choosing your Freebie (Gift) from our advertiser,you are giving permission to scroll a DazZ - " +
                "an Advertising Billboard in your smartphone screen for 15 seconds for next 5 days, when you download and " +
                "open KaChing.me App");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        LinearLayout.LayoutParams text1Parama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // text1Parama.width = (int) (width * 25 / 100);
        //text1Parama.height = (int) (width * 25 / 100);
        text1Parama.gravity = Gravity.CENTER | Gravity.TOP;
        text1Parama.topMargin = height * 3 / 100;
        dialogText1.setLayoutParams(text1Parama);

        LinearLayout.LayoutParams text2Parama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //text2Parama.width = (int) (width * 25 / 100);
        //text2Parama.height = (int) (width * 25 / 100);
        text2Parama.gravity = Gravity.CENTER | Gravity.TOP;
        text2Parama.topMargin = height * 4 / 100;
        dialogText2.setLayoutParams(text2Parama);

        LinearLayout.LayoutParams text3Parama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //text3Parama.width = (int) (width * 25 / 100);
        // text3Parama.height = (int) (width * 25 / 100);
        text3Parama.gravity = Gravity.CENTER | Gravity.TOP;
        text3Parama.topMargin = height * 2 / 100;
        dialogText3.setLayoutParams(text3Parama);
        dialogText3.setPadding(width * 5 / 100, 0, width * 5 / 100, 0);
        dialogText3.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams agreeLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        agreeLayoutParama.width = (int) (width * 70 / 100);
        agreeLayoutParama.height = (int) (height * 6 / 100);
        agreeLayoutParama.gravity = Gravity.CENTER;
        agreeLayoutParama.topMargin = height * 4 / 100;
        agreeLayoutParama.bottomMargin = height * 5 / 100;
        dialogAgreeLayout.setLayoutParams(agreeLayoutParama);

        LinearLayout.LayoutParams textDeclineParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textDeclineParama.width = (int) (width * 65 / 2 / 100);
        textDeclineParama.height = (int) (height * 11 / 2 / 100);
        dialogDecline.setLayoutParams(textDeclineParama);
        dialogDecline.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams textAgreeParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textAgreeParama.width = (int) (width * 65 / 2 / 100);
        textAgreeParama.height = (int) (height * 11 / 2 / 100);
        textAgreeParama.leftMargin = width * 5 / 100;
        dialogAgree.setLayoutParams(textAgreeParama);
        dialogAgree.setGravity(Gravity.CENTER);

        if (width >= 600) {
            dialogText1.setTextSize(16);
            dialogText2.setTextSize(16);
            dialogText3.setTextSize(16);
            dialogDecline.setTextSize(16);
            dialogAgree.setTextSize(16);
        } else if (width > 501 && width < 600) {
            dialogText1.setTextSize(15);
            dialogText2.setTextSize(15);
            dialogText3.setTextSize(15);
            dialogDecline.setTextSize(15);
            dialogAgree.setTextSize(15);
        } else if (width > 260 && width < 500) {
            dialogText1.setTextSize(13);
            dialogText2.setTextSize(13);
            dialogText3.setTextSize(13);
            dialogDecline.setTextSize(13);
            dialogAgree.setTextSize(13);
        } else if (width <= 260) {
            dialogText1.setTextSize(12);
            dialogText2.setTextSize(12);
            dialogText3.setTextSize(12);
            dialogDecline.setTextSize(12);
            dialogAgree.setTextSize(12);
        }

        dialogAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                decline = false;
                progressdialog = ProgressDialog.show(VerificationActivity.this,
                        getResources().getString(R.string.please_wait),
                        getResources().getString(R.string.loading), true);
                progressdialog.show();
                if (Constant.manualmail.equals("")) {
                    Constant.printMsg("sssssssss  if condition agree " + Constant.manualmail);
                    new MySync().execute();
                } else {
                    Constant.printMsg("sssssssss  else  condition agree " + Constant.manualmail);
                    new MySync_Check().execute();
                }
            }
        });
        dialogDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                decline = true;
                progressdialog = ProgressDialog.show(VerificationActivity.this,
                        getResources().getString(R.string.please_wait),
                        getResources().getString(R.string.loading), true);
                progressdialog.show();
                if (Constant.manualmail.equals("")) {
                    new MySync().execute();
                } else {
                    new MySync_Check().execute();
                }
            }
        });
        dialog.show();
    }

    protected void setshared(int newstlistpos) {
        // TODO Auto-generated method stub
        editor = preference.edit();
        editor.putInt("sec_count", newstlistpos);

        editor.commit();
    }


    /**
     * Background Async task to load user profile picture from url
     */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public class MySync extends AsyncTask<String, String, String> {

        String password = Constant.pass;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            Constant.printMsg("ggggggggggggggggggggggggggggggggggggg"
                    + password);

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            // temp.run();
            try {
                if (!TempConnectionService.connection.isConnected()) {

                    TempConnectionService.connection.connect();
                }

            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                Log.d("Signup_3",
                        "Error while connnecting!!!!!!" + e.toString());
            }

            try {
                Log.d("Signup_3", "is Authenticate::"
                        + TempConnectionService.connection.isAuthenticated());
                Constant.printMsg("ssss::::do in bg::MySync");
                AccountManager account = AccountManager
                        .getInstance(TempConnectionService.connection);
                account.sensitiveOperationOverInsecureConnectionDefault(true);
                account.sensitiveOperationOverInsecureConnection(true);

                Log.d("Signup_3",
                        "is Create::" + account.supportsAccountCreation()
                                + "::" + full_mobile_no);
                Map<String, String> regAttr = new HashMap<String, String>();

                Constant.printMsg("siva mobile number........"
                        + full_mobile_no);

                regAttr.put("user", full_mobile_no);
                regAttr.put("password", password);
                Constant.printMsg("ssss::::Signup_3" + full_mobile_no
                        + "password" + password);

                Constant.printMsg("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJPPPPP"
                        + full_mobile_no + "   " + Constant.first + "        "
                        + Constant.manualmail + "      " + Constant.byteimage);
                account.createAccount(full_mobile_no, password, regAttr);
                dbAdapter
                        .insertLogin(full_mobile_no, password, getResources()
                                        .getString(R.string.hey_im_usning_niftycha),
                                Constant.first, Constant.manualmail,
                                Constant.byteimage);
                Constant.printMsg("ssss::::db adapter in mysync");
                // connection.disconnect();

                // SASLAuthentication.supportSASLMechanism("PLAIN", 0);

                TempConnectionService.connection.login(full_mobile_no,
                        password, "Messnger");
                Constant.printMsg("ssss::::loginnnn");
                try {

                    Constant.printMsg("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJPPPPP");

                    VCard vc = new VCard();
                    // vc.load(connection);
                    // //Constant.printMsg("Saved user::"+connection.getUser().toString());
                    Constant.printMsg("ssss::::vc entered");
                    vc.setJabberId(full_mobile_no + NiftyApplication.getHost());
                    vc.setNickName(full_mobile_no);
                    vc.setField(
                            "SORT-STRING",
                            getResources().getString(
                                    R.string.hey_im_usning_niftycha));
                    vc.setFirstName(Constant.first);
                    if (!Constant.manualmail.equals("")) {
                        vc.setEmailWork(Constant.manualmail);
                        NiftyApplication.setNifty_email(Constant.manualmail);
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

            } /*
             * catch (XMPPException e) { e.printStackTrace(); new
			 * AlertManager().
			 * getToast(signup_3.this,"Some issues in signup please try again!!"
			 * ); connection.disconnect(); Log.d("signup_3",e.toString());
			 * 
			 * }
			 */ catch (NoResponseException e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO Auto-generated catch block
                e.printStackTrace();
                Constant.printMsg("ssss::::NoResponseException"
                        + e.toString());
            } catch (NotConnectedException e) {
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
            } catch (IOException e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO Auto-generated catch block
                e.printStackTrace();
                Constant.printMsg("ssss::::IOException" + e.toString());
            } catch (XMPPErrorException e) {
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

            Log.d(TAG, "Registration Post Execute");
            if (TempConnectionService.connection.isAuthenticated()) {
                Constant.printMsg("ssss::::post1111");
                NiftyApplication.setAvatar(Constant.byteimage);
                NiftyApplication.setNifty_name(Constant.first);
                NiftyApplication.setUserID(full_mobile_no);
                NiftyApplication.setAcra();
                Presence presence = new Presence(Presence.Type.available);
                presence.setStatus(getResources().getString(
                        R.string.hey_im_usning_niftycha));
                try {
                    TempConnectionService.connection.sendStanza(presence);
                } catch (NotConnectedException e) {
                    // TODO Auto-generated catch block
                    Constant.printMsg("");
                    e.printStackTrace();
                }
                Constant.printMsg("ssss::::post2222");
                RequestParams request_params = new RequestParams();
                request_params.put("jid",
                        full_mobile_no + NiftyApplication.getHost());
                request_params.put("email", Constant.manualmail);
                request_params.put("password", Constant.pass);
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(getString(R.string.webservice_host)
                                + "user_regisrtation.php", request_params,
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

				/*
				 * Intent intent=new Intent(signup_3.this,ContactSync.class);
				 * startActivity(intent); finish();
				 */
                editor.putString("pin", Constant.pass);
                editor.commit();

                progressdialog.dismiss();
                Constant.printMsg("ssss::::endddddd");
                // Intent intent1 = new Intent(VerificationActivity.this,
                // Contact_server.class);
                // startService(intent1);
                Intent syncContact = new Intent(VerificationActivity.this,
                        ContactLastSync.class);
                VerificationActivity.this.startService(syncContact);
                Constant.printMsg("decline::endddddd" + decline);
                if (!decline) {
                    Intent intent = new Intent(VerificationActivity.this,
                            NewStartUpActivity.class);
                    intent.putExtra("mobileno", Constant.fullmob);
                    intent.putExtra("country_code", Constant.countrycode);
                    startActivity(intent);
                    finish();

                    Constant.fullmob = full_mobile_no;
                    Constant.countrycode = country_code;
                    intent.putExtra("mobileno", full_mobile_no);
                    intent.putExtra("country_code", country_code);
                    startActivity(intent);
                    finish();
                    updatefreebieaccept();
                } else {

//					Intent intent = new Intent(VerificationActivity.this,
//							SliderTesting.class);
//					startActivity(intent);
					updatefreebiedecline();
//siva
                }
            }
        }
    }

    private void updatefreebieaccept() {
        // TODO Auto-generated method stub
        editor = preference.edit();
        editor.putBoolean("decline", false);

        editor.commit();
    }

    private void updatefreebiedecline() {
        // TODO Auto-generated method stub
        editor = preference.edit();
        editor.putBoolean("decline", true);
        editor.commit();
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
                    Iterator<Row> it = data.getRows().iterator();
                    while (it.hasNext()) {

                        Row row = it.next();
                        Iterator iterator = row.getValues("jid").iterator();
                        if (iterator.hasNext()) {
                            String value = iterator.next().toString();

                            // Constant.printMsg("Iteartor values......"+value);
                            if (!value.toString().equals(
                                    "admin" + NiftyApplication.getHost())) {
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
                commonMethods.showAlertDialog(VerificationActivity.this,
                        getResources().getString(R.string.email_already_exist),
                        true);
                progressdialog.cancel();
            }
            super.onPostExecute(result);

        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

	/*private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {

			mBoundService = ((KaChingMeService.LocalBinder) service)
					.getService();
			connection = mBoundService.getConnection();

			Constant.printMsg("ggggggggggggggggggggggggfffffffffffffffffffff"
					+ connection.isConnected());
		}

		public void onServiceDisconnected(ComponentName className) {

			mBoundService = null;
		}
	};

	void doBindService() {

		bindService(new Intent(VerificationActivity.this,
				KaChingMeService.class), mConnection, Context.BIND_AUTO_CREATE);
		isBound = true;
	}

	void doUnbindService() {
		if (isBound) {

			unbindService(mConnection);
			isBound = false;
		}
	}*/

    @Override
    protected void onDestroy() {
        //	doUnbindService();

        super.onDestroy();
    }

    public void showAlertDialog1(Context context, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle("Kaching.Me");

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if (status != null)
            // Setting alert dialog icon
            // alertDialog.setIcon((status) ? R.drawable.success :
            // R.drawable.fail);

            // Setting OK Button
            alertDialog.setButton(
                    context.getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (Constant.mFromsettingVerfication == true) {

                                finish();

                            } else {

                                progressdialog = ProgressDialog.show(
                                        VerificationActivity.this,
                                        getResources().getString(
                                                R.string.please_wait),
                                        getResources().getString(
                                                R.string.loading), true);
                                progressdialog.show();

                                if (Constant.manualmail.equals("")) {

                                    new MySync().execute();

                                } else {
                                    new MySync_Check().execute();
                                }

                            }
                        }
                    });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (mAddSecondary.getVisibility() == View.VISIBLE) {
            if (Constant.mFromsettingVerfication == true) {
                finish();
            }
        } else {
            if (Constant.mBackInvisible == true) {

            } else if (Constant.mFromsettingVerfication == true) {
                finish();
            } else {
                Constant.mFromVerfication = true;
                Intent ii = new Intent(VerificationActivity.this,
                        RegisterActivity.class);
                ii.putExtra("sexId", seXvalue);
                startActivity(ii);
                finish();
            }
        }
    }

    private void screenArrangeVerification() {
        // TODO Auto-generated method stub
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        LinearLayout.LayoutParams profileImgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        profileImgParams.width = width * 25 / 100;
        profileImgParams.height = width * 25 / 100;
        profileImgParams.gravity = Gravity.CENTER;
        profileImgParams.topMargin = height * 13 / 100;
        mProfilePicture.setLayoutParams(profileImgParams);

        LinearLayout.LayoutParams headText = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //headText.height = height * 8 / 100;
        headText.width = width * 85 / 100;
        headText.topMargin = height * 1 / 100;
        headText.gravity = Gravity.CENTER;
        mHeadText1.setLayoutParams(headText);
        mHeadText2.setLayoutParams(headText);
        mHeadText3.setLayoutParams(headText);
        mHeadText1.setGravity(Gravity.CENTER);
        mHeadText2.setGravity(Gravity.CENTER);
        mHeadText3.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams buttonParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParama.height = height * 7 / 100;
        buttonParama.width = width * 50 / 100;
        buttonParama.topMargin = height * 5 / 100;
        buttonParama.gravity = Gravity.CENTER;
        mPerimaryVerification.setLayoutParams(buttonParama);
        mPerimaryVerification.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams buttonNextParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonNextParama.height = (int) (height * 6.5 / 100);
        buttonNextParama.width = width * 50 / 100;
        buttonNextParama.topMargin = height * 3 / 100;
        buttonNextParama.gravity = Gravity.CENTER;
        mAddSecondary.setLayoutParams(buttonNextParama);
        mNextButton.setLayoutParams(buttonNextParama);
        mAddSecondary.setGravity(Gravity.CENTER);
        mNextButton.setGravity(Gravity.CENTER);

        if (width >= 600) {
            mHeadText1.setTextSize(13);
            mHeadText2.setTextSize(13);
            mHeadText3.setTextSize(13);
            mPerimaryVerification.setTextSize(16);
            mAddSecondary.setTextSize(16);
            mNextButton.setTextSize(16);
        } else if (width > 501 && width < 600) {
            mHeadText1.setTextSize(12);
            mHeadText2.setTextSize(12);
            mHeadText3.setTextSize(12);
            mPerimaryVerification.setTextSize(15);
            mAddSecondary.setTextSize(15);
            mNextButton.setTextSize(15);
        } else if (width > 260 && width < 500) {
            mHeadText1.setTextSize(11);
            mHeadText2.setTextSize(11);
            mHeadText3.setTextSize(11);
            mPerimaryVerification.setTextSize(14);
            mAddSecondary.setTextSize(14);
            mNextButton.setTextSize(14);
        } else if (width <= 260) {
            mHeadText1.setTextSize(10);
            mHeadText2.setTextSize(10);
            mHeadText3.setTextSize(10);
            mPerimaryVerification.setTextSize(13);
            mAddSecondary.setTextSize(13);
            mNextButton.setTextSize(13);
        }
    }
}
