package com.wifin.kachingme.registration_and_login;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.SessionState;
//import com.facebook.UiLifecycleHelper;
//import com.facebook.android.Facebook;
import com.facebook.appevents.AppEventsLogger;
//import com.facebook.model.GraphUser;
//import com.facebook.LoginButton;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.UserContactDto;
import com.wifin.kachingme.util.AlertUtils;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.Validation;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kaching.me.ui.R;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by siva(wifin) on 21/09/2016
 */
public class SliderFragment extends Fragment implements View.OnClickListener, OnConnectionFailedListener {
    // Store instance variables
    private String title;
    private int page;
    EditText sEmailEdit;
    TextView sSubmit, sInfoOne, sInfoTwo, sInfoThree, sInfoFour, sInfoFive, sOrText;
    ImageView sInfoImageOne, sInfoImageTwo, sInfoImageThree, sInfoImageFour, sInfoImageFive;
    LinearLayout sSocialMediaLayout, sViewLayout, sInfoLayoutOne, sInfoLayoutTwo, sInfoLayoutThree, sInfoLayoutFour, sInfoLayoutFive;
    static Dbhelper db;
    //    SignInButton sGPlusLogin;
    //    LoginButton sFbLogin;
    //siva
    ImageView sFbImage;
    ImageView sGPlusImage;
    View sOrView1, sOrView2;
    public static boolean fbCheck, gplusCheck, fbGpluscheck;
    String user_id, username, provider, useremail, image;
    public static final int RC_SIGN_IN = 1992;
    private boolean mSignInClicked;
    private boolean mIntentInProgress;
    public static GoogleApiClient mGoogleApiClient;
    //    private UiLifecycleHelper uiHelper;//siva
    public static final int WEBVIEW_OFFLINE_CODE = 200;
    public static final int WEBVIEW_REQUEST_CODE = 100;
    public static CallbackManager mCallbackManager;
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    static ArrayList<UserContactDto> conlist = new ArrayList<UserContactDto>();
    OtpSharedPreference prefOtp;
    static String strFirstName, strLastName, strGender, strDob, strProfilePicture, strEmail;
    GoogleSignInOptions gso;
    static Activity activity;
    static ProgressDialog progressDialog;

    /* slider variables */
    TextView mKonHead, mKonKonsText, mKonKonsDivider, mKonKonsRes, mKonBuxsText, mKonBuxsDivider, mKonBuxsRes,
            mKonDestText, mKonDestDivider, mKonDestRes, mKonLockText, mKonLockDivider, mKonLockRes, mKonKrokText, mKonKrokDivider, mKonKrokRes, mNymnText1, mNymnText2, mDazzText1, mDazzText2;
    ImageView mKonzsImageView, mNynmImageView, mDazzImageView;


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
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        AppEventsLogger.activateApp(getActivity());
        mCallbackManager = CallbackManager.Factory.create();
        prefOtp = new OtpSharedPreference(getActivity());
        sharedPreferences = getActivity().getSharedPreferences(
                KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
        Constant.printMsg("Logged before oncreate object");
        activity = getActivity();
        try {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestScopes(new Scope(Scopes.PROFILE))
                    .requestProfile()
                    .requestScopes(new Scope(Scopes.PLUS_LOGIN))
//                    .requestScopes(new Scope("https://www.googleapis.com/auth/user.birthday.read"))
//                    .requestScopes (new Scope("https://www.googleapis.com/auth/userinfo.profile"))
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                    .enableAutoManage(getActivity(), this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addApi(Plus.API)
                    .addScope(new Scope(Scopes.PROFILE))
//                    .addScope(new Scope("https://www.googleapis.com/auth/user.birthday.read"))
//                    .addScope(new Scope("https://www.googleapis.com/auth/userinfo.profile"))
                    .build();

            Constant.printMsg("Logged google client present.......");

        } catch (Exception e) {
            Constant.printMsg("Logged google client exception......." + e);
        }
        Constant.printMsg("Logged after oncreate object");

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        Constant.printMsg("Logged Success..." + loginResult);
                        final AccessToken accessToken = loginResult.getAccessToken();
                        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject JSONUser, GraphResponse graphResponse) {
                                        if (graphResponse.getError() != null) {
                                            Constant.printMsg("Logged Success. error.." + graphResponse.getError());
                                        } else {
                                            Constant.printMsg("Logged Success.. result..." + JSONUser);
                                            Constant.printMsg("Logged Success.. GraphResponse..." + graphResponse);
                                            try {
                                                Constant.printMsg("Logged Success.. profile picture..." + "http://graph.facebook.com/" + JSONUser.getString("id") + "/picture?type=large");
                                                editor = sharedPreferences.edit();
                                                editor.putString("facebookId", JSONUser.getString("id"));
//                                              editor.putString("facebookPicture","http://graph.facebook.com/" + JSONUser.getString("id")+"/picture?type=large");
                                                editor.commit();
//                                               profilePicture="http://graph.facebook.com/"+JSONUser.getString("id")+"/picture?type=large";

                                                JSONObject jsonPicture = new JSONObject(JSONUser.getJSONObject("picture").getJSONObject("data").toString());
                                                Constant.printMsg("Logged json puicture........." + jsonPicture.getString("url"));
//                                              strProfilePicture=jsonPicture.getString("url");
                                                strFirstName = JSONUser.getString("first_name");
                                                strLastName = JSONUser.getString("last_name");
                                                strGender = JSONUser.getString("gender");
                                                strEmail = JSONUser.getString("email");
                                                //strDob=JSONUser.getString("");
                                                progressDialog = new ProgressDialog(activity, android.R.style.Theme_Material_Light_Dialog_Alert);
                                                progressDialog.setMessage("Please Wait...");
                                                progressDialog.setProgressDrawable(new ColorDrawable(android.graphics.Color.BLUE));
                                                progressDialog.setCancelable(false);
                                                progressDialog.show();
                                                WebView webView = new WebView(getActivity());
                                                webView.getSettings().setJavaScriptEnabled(true);
                                                webView.setWebViewClient(new WebViewClient() {
                                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                        view.loadUrl(url);
                                                        Constant.printMsg("Logged final url...load......" + url);
                                                        return true;
                                                    }

                                                    public void onPageFinished(WebView view, String url) {
                                                        Constant.printMsg("Logged final url........." + url);
                                                        strProfilePicture = url;
                                                        new getBitmapFromURL().execute();
                                                    }

                                                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                                        Constant.printMsg("Logged final url.........onReceivedError");
                                                    }
                                                });
                                                webView.loadUrl("http://graph.facebook.com/" + JSONUser.getString("id") + "/picture?type=large");

                                            } catch (JSONException e) {
                                                Constant.printMsg("Logged Success.. profile picture..exception." + e);
                                            }
                                        }
                                    }

                                }
                        );
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name,email,gender,picture,name,age_range,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getActivity(), "Login Cancel", Toast.LENGTH_LONG).show();
                        Constant.printMsg("Logged Cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        Constant.printMsg("Logged FacebookException");
                    }
                });
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
//        activity = getActivity();
        switch (page) {
            case 0:
                view = inflater.inflate(R.layout.slide_login, container, false);
                Constant.printMsg("Logged google client onCreateView.......");
                loginProcess(view);
                break;
            case 1:
                view = inflater.inflate(R.layout.slide_feature, container, false);
                konsInitialization(view);
                konsScreenArrange();
                break;
            case 2:
                view = inflater.inflate(R.layout.slide_nynm, container, false);
                nynmScreenArrange(view);
                break;
            case 3:
                view = inflater.inflate(R.layout.slide_dazz, container, false);
                dazzScreenArrange(view);
                break;
//            case 4:
//                view = inflater.inflate(R.layout.slide_6, container, false);
//                break;
//            case 5:
//                view = inflater.inflate(R.layout.slide_7, container, false);
//                break;
        }
        return view;
    }

    private void initialization(View view) {
        sEmailEdit = (EditText) view.findViewById(R.id.slide_mailEdit);
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
        sSocialMediaLayout = (LinearLayout) view.findViewById(R.id.slide_socialMediaLayout);
//        sGPlusLogin = (SignInButton)view. findViewById(R.id.signin);
//        sFbLogin = (LoginButton)view. findViewById(R.id.authButton);
        //siva
        sOrView1 = (View) view.findViewById(R.id.slide_view1);
        sOrView2 = (View) view.findViewById(R.id.slide_view2);
        sFbImage = (ImageView) view.findViewById(R.id.facebook_login);
        sGPlusImage = (ImageView) view.findViewById(R.id.google_login);

        Constant.typeFace(getActivity(), sEmailEdit);
        Constant.typeFace(getActivity(), sSubmit);
        Constant.typeFace(getActivity(), sInfoOne);
        Constant.typeFace(getActivity(), sInfoTwo);
        Constant.typeFace(getActivity(), sInfoThree);
        Constant.typeFace(getActivity(), sInfoFour);
        Constant.typeFace(getActivity(), sInfoFive);
        Constant.typeFace(getActivity(), sOrText);

        sSubmit.setOnClickListener(this);
        sFbImage.setOnClickListener(this);
        sGPlusImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slide_submit:
                new OtpSharedPreference(getActivity()).clearRegistrationDetails();
                String mail = sEmailEdit.getText().toString();
                if (mail.trim().length() > 0) {
                    if (Validation.isEmailAddress(sEmailEdit, true)) {
                        DbDelete();
                        Constant.manualmail = sEmailEdit.getText().toString().trim();
                        if (Connectivity.isConnected(getActivity())) {
                            fbGpluscheck = false;
                            new mailValidation().execute();
                            //checkMailUsingVolley();
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
                break;

            case R.id.facebook_login:
                Constant.printMsg("Logged fb clicked...");
                fbCheck = true;
                new OtpSharedPreference(getActivity()).clearRegistrationDetails();
                if (Connectivity.isConnected(getContext())) {
                    LoginManager.getInstance().logInWithReadPermissions(getActivity(),
                            Arrays.asList("public_profile", "email", "user_birthday"));
                } else {
                    new AlertUtils().Toast_call(getContext(), getResources().getString(
                            R.string.no_internet_connection));
                }
                break;

            case R.id.google_login:
                gplusCheck = true;
                new OtpSharedPreference(getActivity()).clearRegistrationDetails();
                Constant.printMsg("Logged gplus  clicked...");
                new OtpSharedPreference(getActivity()).clearRegistrationDetails();
                if (Connectivity.isConnected(getContext())) {
                    signIn();
                } else {
                    new AlertUtils().Toast_call(getContext(), getResources().getString(
                            R.string.no_internet_connection));
                }
                break;
        }
    }

    private void konsInitialization(View view) {
        mKonzsImageView = (ImageView) view.findViewById(R.id.kons_topImage);
        mKonHead = (TextView) view.findViewById(R.id.kons_textTitle);
        mKonKonsText = (TextView) view.findViewById(R.id.kons_textKons);
        mKonKonsDivider = (TextView) view.findViewById(R.id.kons_textKonsDivider);
        mKonKonsRes = (TextView) view.findViewById(R.id.kons_textKonsRes);
        mKonBuxsText = (TextView) view.findViewById(R.id.kons_textBuxs);
        mKonBuxsDivider = (TextView) view.findViewById(R.id.kons_textBuxsDivider);
        mKonBuxsRes = (TextView) view.findViewById(R.id.kons_textBuxsRes);
        mKonDestText = (TextView) view.findViewById(R.id.kons_textDest);
        mKonDestDivider = (TextView) view.findViewById(R.id.kons_textDestDivider);
        mKonDestRes = (TextView) view.findViewById(R.id.kons_textDestRes);
        mKonLockText = (TextView) view.findViewById(R.id.kons_textLock);
        mKonLockDivider = (TextView) view.findViewById(R.id.kons_textLockDivider);
        mKonLockRes = (TextView) view.findViewById(R.id.kons_textLockRes);
        mKonKrokText = (TextView) view.findViewById(R.id.kons_textKrok);
        mKonKrokDivider = (TextView) view.findViewById(R.id.kons_textKrokDivider);
        mKonKrokRes = (TextView) view.findViewById(R.id.kons_textKrokRes);

        Constant.typeFace(getActivity(), mKonHead);
        Constant.typeFace(getActivity(), mKonKonsText);
        Constant.typeFace(getActivity(), mKonKonsDivider);
        Constant.typeFace(getActivity(), mKonKonsRes);
        Constant.typeFace(getActivity(), mKonBuxsText);
        Constant.typeFace(getActivity(), mKonBuxsDivider);
        Constant.typeFace(getActivity(), mKonBuxsRes);
        Constant.typeFace(getActivity(), mKonDestText);
        Constant.typeFace(getActivity(), mKonDestDivider);
        Constant.typeFace(getActivity(), mKonDestRes);
        Constant.typeFace(getActivity(), mKonLockText);
        Constant.typeFace(getActivity(), mKonLockDivider);
        Constant.typeFace(getActivity(), mKonLockRes);
        Constant.typeFace(getActivity(), mKonKrokText);
        Constant.typeFace(getActivity(), mKonKrokDivider);
        Constant.typeFace(getActivity(), mKonKrokRes);
    }

    private static void finalSubmissionProcess() {
        if (strFirstName != null && !strFirstName.isEmpty()) {
            Constant.mFirstName = strFirstName;
            Constant.mFirstNameText = strFirstName;
        }
        if (strLastName != null && !strLastName.isEmpty()) {
            Constant.mLastName = strLastName;
            Constant.mSecondNameText = strLastName;
        }
        if (strGender != null && !strGender.isEmpty()) {
            Constant.gender = strGender;
            Constant.mGender = strGender;
        }
        if (strDob != null && !strDob.isEmpty()) {
            Constant.dob = strDob;
            Constant.mDateOfBirth = strDob;
        }
        Constant.first = Constant.mFirstName + "  " + Constant.mLastName;
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor people = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int count = 0;
        people.moveToFirst();
        if (indexName > 0) {
            do {
                count++;
                String name = people.getString(indexName).trim();
                String number = people.getString(indexNumber).trim();
                if (number.length() > 9) {
                    UserContactDto cd = new UserContactDto();
                    cd.setContactNumbe(number);
                    cd.setContactName(name);
                    cd.setuId(String.valueOf(count));
                    conlist.add(cd);
                    Constant.printMsg("Logged conatct list:::" + name + number);
                }
                // Do work...
            } while (people.moveToNext());
        }
        Constant.contactlist.addAll(conlist);
        Constant.isFbGplus = fbGpluscheck;
        Constant.printMsg("Logged Gplu and fb intent");
        if (gplusCheck) {
            signOut();
        } else {
            if (fbGpluscheck) {
                logoutFromFacebook();
            }
        }
        activity.startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        activity.finish();
    }

    public static void logoutFromFacebook() {
        try {
            if (AccessToken.getCurrentAccessToken() == null) {
                return; // already logged out
            }
//            long fb_id=sharedPreferences.getFacebookId(); //get fb id from sharedprefrences
            long fb_id = Long.parseLong(sharedPreferences.getString("facebookId", "")); //get fb id from sharedprefrences
            GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "/ " + fb_id + "/permissions/", null,
                    HttpMethod.DELETE, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    LoginManager.getInstance().logOut();
                }
            });

            graphRequest.executeAsync();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void signOut() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Constant.printMsg("Logged gplus status......" + status);
                        }
                    });
        } else {
            Constant.printMsg("Logged signout no connected...");
//            mGoogleApiClient.connect();
//            mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);

        }
    }

    public static String bitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /*google plus integt=ration*/
    private void signIn() {
        mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public static void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Constant.printMsg("Logged gplus acct......" + result + "...." + acct);
            String personName = acct.getDisplayName();

            strFirstName = personName.substring(0, personName.indexOf(' '));
            strLastName = personName.substring(personName.indexOf(' ') + 1);
            strEmail = acct.getEmail();
            if (acct.getPhotoUrl()!=null){
                strProfilePicture = acct.getPhotoUrl().toString();
            }
            progressDialog = new ProgressDialog(activity, android.R.style.Theme_Material_Light_Dialog_Alert);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setProgressDrawable(new ColorDrawable(android.graphics.Color.BLUE));
            progressDialog.setCancelable(false);
            progressDialog.show();
            new getBitmapFromURL().execute();
            Constant.printMsg("Logged gplus details......" + personName + "...." + strFirstName + "....." + strLastName);

            if (mGoogleApiClient.isConnected()) {
                Constant.printMsg("Logged gplus details. finasl.....enter");
                Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                Constant.printMsg("Logged gplus details. finasl.....next" + person);

                if (person != null) {
                    if (person.getGender() == 1) {
                        strGender = "female";
                    } else {
                        strGender = "male";
                    }
                    Constant.printMsg("Logged gplus details. finasl....." + person.getBirthday() + "...." + person.getGender() + "....." + person.getAboutMe());
                }
            } else {
                Constant.printMsg("Logged gplus details. elseeeeeeeeeee....");
//                Plus.PeopleApi.load(mGoogleApiClient, acct.getId()).setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
//                    @Override
//                    public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {
//                        Person person = loadPeopleResult.getPersonBuffer().get(0);
//                        Constant.printMsg("Logged gplus details. else finasl....." + person.getBirthday() + "...." + person.getGender() + "....." + person.getAboutMe());
//                    }
//                });

            }

        } else {
            Constant.printMsg("Logged gplus acct..else...." + result);
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Constant.printMsg("Logged fragment onConnectionFailed called..." + connectionResult);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        Constant.printMsg("Logged startActivityForResult fragment");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Constant.printMsg("Logged gplus fragemrnt onActivityResult");

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }

    @Override
    public void onResume() {
        super.onResume();
//        uiHelper.onResume();
        Constant.printMsg("Logged fragment onResume called...");
    }

    @Override
    public void onPause() {
        super.onPause();
//        uiHelper.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            Constant.printMsg("Logged fragment onStart Got cached sign-in...");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    Constant.printMsg("Logged fragment onStart Got googleSignInResult sign-in..."+googleSignInResult);
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Constant.printMsg("Logged fragment onstop called...");
//        mGoogleApiClient.stopAutoManage(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        uiHelper.onDestroy();
        Constant.printMsg("Logged fragment onDestroy ...");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        uiHelper.onSaveInstanceState(outState);
    }

    private void loginProcess(View view) {
        initialization(view);
        loginScreenArrange();
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
                    Slideshow.sLoginImage.setVisibility(View.GONE);
                } else {
                    sSubmit.setVisibility(View.GONE);
                    sSocialMediaLayout.setVisibility(View.VISIBLE);
                    sViewLayout.setVisibility(View.VISIBLE);
                    Slideshow.sLoginImage.setVisibility(View.VISIBLE);

                }
            }
        });
    }


//    private void checkMailUsingVolley() {
////        String tag_json_obj = "json_obj_req";
//
//        String url = KachingMeConfig.Email_Verification +"emailid="+ Constant.manualmail;
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

    private static class getBitmapFromURL extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            URL url = null;
            Bitmap myBitmap = null;
            try {
                if (strProfilePicture != null) {
                    url = new URL(strProfilePicture);
                    myBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    Constant.printMsg("Logged bitmap no Exception....." + myBitmap);
                }
            } catch (Exception e) {
                Constant.printMsg("Logged bitmap Exception....." + e);
            }
            return myBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            Bitmap mp=getBitmapFromURL("https://scontent.xx.fbcdn.net/v/t1.0-1/p200x200/14224858_1116376125108707_913375397210780581_n.jpg?oh=3bc962570889f92b104168ff26a33a12&oe=58D42E5C");
            Constant.printMsg("Logged bnitmap images..........." + strProfilePicture + "............" + bitmap);
            if (bitmap != null) {
//                sF.setImageBitmap(bitmap);
                editor = sharedPreferences.edit();
                editor.putString("facebookProfilePicture", bitMapToString(bitmap));
                editor.commit();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                byte[] byteArray = stream.toByteArray();
                Constant.byteimage = byteArray;
            } else {
                Constant.printMsg("Logged bnitmap images. else.........." + bitmap);
            }
            if (strEmail != null && strEmail.trim().length() > 0) {
                DbDelete();
                Constant.manualmail = strEmail;
                if (Connectivity.isConnected(getApplicationContext())) {
                    fbGpluscheck = true;
                    new mailValidation().execute();
                    //checkMailUsingVolley();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    public static class mailValidation extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();
            result = ht.httpget(KachingMeConfig.Email_Verification + "emailid="
                    + Constant.manualmail);
            Constant.printMsg("Logged post link mail verificatio....." + KachingMeConfig.Email_Verification + "emailid="
                    + Constant.manualmail);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
            Constant.printMsg("Logged result ::::::: >>>>>> " + result);
            // Toast.makeText(getApplicationContext(), "Email " + result,
            // Toast.LENGTH_LONG).show();
            if (result != null && result.length() > 0) {

                if (result.equalsIgnoreCase("true")) {
                    if (fbGpluscheck) {
                        finalSubmissionProcess();
                    } else {
                        Constant.printMsg("Logged Gplu and fb intent");
                        activity.startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                        activity.finish();
                    }
                } else {
                    if (gplusCheck) {
                        Constant.printMsg("logged signout called");
                        signOut();
                    } else {
                        if (fbCheck) {
                            Constant.printMsg("logged signout fb called");
                            logoutFromFacebook();
                        } else {
                            Constant.printMsg("logged signout nothing called");

                        }
                    }
                    sharedPreferences.edit().remove("facebookProfilePicture").commit();
                    Constant.byteimage = null;
                    Constant.bitmapImage=null;
                    Toast.makeText(activity,
                            "Email already exist", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(activity,
                        "Server Down Try Again Later", Toast.LENGTH_LONG)
                        .show();
            }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            Constant.printMsg("Logged mail  ::::::: >>>>>> pre");
            if (!fbGpluscheck) {
                progressDialog = new ProgressDialog(activity, android.R.style.Theme_Material_Light_Dialog_Alert);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setProgressDrawable(new ColorDrawable(android.graphics.Color.BLUE));
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

    }

    public static void DbDelete() {
        // TODO Auto-generated method stub
        db = new Dbhelper(getApplicationContext());
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

    private void nynmScreenArrange(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        mNynmImageView = (ImageView) view.findViewById(R.id.nynm_topImage);
        mNymnText1 = (TextView) view.findViewById(R.id.nynm_text1);
        mNymnText2 = (TextView) view.findViewById(R.id.nynm_text2);
        Constant.typeFace(getActivity(), mNymnText1);
        Constant.typeFace(getActivity(), mNymnText2);

        LinearLayout.LayoutParams imageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        imageParama.width = width * 90 / 100;
        imageParama.height = height * 30 / 100;
        imageParama.gravity = Gravity.CENTER;
        mNynmImageView.setLayoutParams(imageParama);

        LinearLayout.LayoutParams tetxParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tetxParama.topMargin=height*1/100;
        tetxParama.gravity = Gravity.CENTER;
        mNymnText1.setLayoutParams(tetxParama);
        mNymnText2.setLayoutParams(tetxParama);

        if (width >= 600) {
            mNymnText1.setTextSize(14);
            mNymnText2.setTextSize(14);
        } else if (width > 501 && width < 600) {
            mNymnText1.setTextSize(13);
            mNymnText2.setTextSize(13);
        } else if (width > 260 && width < 500) {
            mNymnText1.setTextSize(12);
            mNymnText2.setTextSize(12);
        } else if (width <= 260) {
            mNymnText1.setTextSize(11);
            mNymnText2.setTextSize(11);
        }
    }

    private void dazzScreenArrange(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        mDazzImageView = (ImageView) view.findViewById(R.id.dazz_topImage);
        mDazzText1 = (TextView) view.findViewById(R.id.dazz_text1);
        mDazzText2 = (TextView) view.findViewById(R.id.dazz_text2);
        Constant.typeFace(getActivity(), mDazzText1);
        Constant.typeFace(getActivity(), mDazzText2);

        LinearLayout.LayoutParams imageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        imageParama.width = width * 70 / 100;
        imageParama.height = height * 23 / 100;
        imageParama.gravity = Gravity.CENTER;
        mDazzImageView.setLayoutParams(imageParama);

        LinearLayout.LayoutParams tetx1Parama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tetx1Parama.topMargin=height*3/100;
        tetx1Parama.gravity = Gravity.CENTER;
        mDazzText1.setLayoutParams(tetx1Parama);

        LinearLayout.LayoutParams tetx2Parama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tetx2Parama.topMargin=height*1/100;
        tetx2Parama.gravity = Gravity.CENTER;
        mDazzText2.setLayoutParams(tetx2Parama);

        if (width >= 600) {
            mDazzText1.setTextSize(13);
            mDazzText2.setTextSize(13);
        } else if (width > 501 && width < 600) {
            mDazzText1.setTextSize(13);
            mDazzText2.setTextSize(13);
        } else if (width > 260 && width < 500) {
            mDazzText1.setTextSize(12);
            mDazzText2.setTextSize(12);
        } else if (width <= 260) {
            mDazzText1.setTextSize(11);
            mDazzText2.setTextSize(11);
        }
    }

    private void konsScreenArrange() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        LinearLayout.LayoutParams imageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        imageParama.width = width * 70 / 100;
        imageParama.height = height * 18 / 100;
        imageParama.gravity = Gravity.CENTER;
        imageParama.topMargin=height*1/100;
        mKonzsImageView.setLayoutParams(imageParama);

        LinearLayout.LayoutParams textTopParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textTopParama.leftMargin=width*5/100;
        textTopParama.bottomMargin=height*1/100;
        textTopParama.gravity = Gravity.LEFT;
        mKonHead.setLayoutParams(textTopParama);

        LinearLayout.LayoutParams textParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textParama.width = width * 11 / 100;
        textParama.gravity = Gravity.LEFT;
        textParama.leftMargin=width*5/100;
        textParama.bottomMargin=height*1/100;
        mKonKonsText.setLayoutParams(textParama);
        mKonBuxsText.setLayoutParams(textParama);
        mKonDestText.setLayoutParams(textParama);
        mKonLockText.setLayoutParams(textParama);
        mKonKrokText.setLayoutParams(textParama);

        LinearLayout.LayoutParams textDividerParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//        textDividerParama.height = height * 3 / 100;
        textDividerParama.bottomMargin=height*1/100;
        textDividerParama.gravity = Gravity.LEFT|Gravity.CENTER;
        mKonKonsDivider.setLayoutParams(textDividerParama);
        mKonBuxsDivider.setLayoutParams(textDividerParama);
        mKonDestDivider.setLayoutParams(textDividerParama);
        mKonLockDivider.setLayoutParams(textDividerParama);
        mKonKrokDivider.setLayoutParams(textDividerParama);

        LinearLayout.LayoutParams textResParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textResParama.leftMargin=width*2/100;
        textResParama.bottomMargin=height*1/100;
        textResParama.gravity = Gravity.LEFT;
        mKonKonsRes.setLayoutParams(textResParama);
        mKonBuxsRes.setLayoutParams(textResParama);
        mKonDestRes.setLayoutParams(textResParama);
        mKonLockRes.setLayoutParams(textResParama);
        mKonKrokRes.setLayoutParams(textResParama);

        if (width >= 600) {
            mKonHead.setTextSize(14);
            mKonKonsText.setTextSize(13);
            mKonKonsDivider.setTextSize(13);
            mKonKonsRes.setTextSize(13);
            mKonBuxsText.setTextSize(13);
            mKonBuxsDivider.setTextSize(13);
            mKonBuxsRes.setTextSize(13);
            mKonDestText.setTextSize(13);
            mKonDestDivider.setTextSize(13);
            mKonDestRes.setTextSize(13);
            mKonLockText.setTextSize(13);
            mKonLockDivider.setTextSize(13);
            mKonLockRes.setTextSize(13);
            mKonKrokText.setTextSize(13);
            mKonKrokDivider.setTextSize(13);
            mKonKrokRes.setTextSize(13);
        } else if (width > 501 && width < 600) {
            mKonHead.setTextSize(13);
            mKonKonsText.setTextSize(12);
            mKonKonsDivider.setTextSize(12);
            mKonKonsRes.setTextSize(12);
            mKonBuxsText.setTextSize(12);
            mKonBuxsDivider.setTextSize(12);
            mKonBuxsRes.setTextSize(12);
            mKonDestText.setTextSize(12);
            mKonDestDivider.setTextSize(12);
            mKonDestRes.setTextSize(12);
            mKonLockText.setTextSize(12);
            mKonLockDivider.setTextSize(12);
            mKonLockRes.setTextSize(12);
            mKonKrokText.setTextSize(12);
            mKonKrokDivider.setTextSize(12);
            mKonKrokRes.setTextSize(12);
        } else if (width > 260 && width < 500) {
            mKonHead.setTextSize(12);
            mKonKonsText.setTextSize(11);
            mKonKonsDivider.setTextSize(11);
            mKonKonsRes.setTextSize(11);
            mKonBuxsText.setTextSize(11);
            mKonBuxsDivider.setTextSize(11);
            mKonBuxsRes.setTextSize(11);
            mKonDestText.setTextSize(11);
            mKonDestDivider.setTextSize(11);
            mKonDestRes.setTextSize(11);
            mKonLockText.setTextSize(11);
            mKonLockDivider.setTextSize(11);
            mKonLockRes.setTextSize(11);
            mKonKrokText.setTextSize(11);
            mKonKrokDivider.setTextSize(11);
            mKonKrokRes.setTextSize(11);
        } else if (width <= 260) {
            mKonHead.setTextSize(11);
            mKonKonsText.setTextSize(10);
            mKonKonsDivider.setTextSize(10);
            mKonKonsRes.setTextSize(10);
            mKonBuxsText.setTextSize(10);
            mKonBuxsDivider.setTextSize(10);
            mKonBuxsRes.setTextSize(10);
            mKonDestText.setTextSize(10);
            mKonDestDivider.setTextSize(10);
            mKonDestRes.setTextSize(10);
            mKonLockText.setTextSize(10);
            mKonLockDivider.setTextSize(10);
            mKonLockRes.setTextSize(10);
            mKonKrokText.setTextSize(10);
            mKonKrokDivider.setTextSize(10);
            mKonKrokRes.setTextSize(10);
        }
    }


    private void loginScreenArrange() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        LinearLayout.LayoutParams layoutOneParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutOneParama.width = width * 80 / 100;
        layoutOneParama.height = height * 3 / 100;
        layoutOneParama.setMargins(width * 1 / 100, height * 12 / 100, 0, width * 1 / 100);
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
        tickImageParama.height = (int) (height * 2 / 100);
        tickImageParama.gravity = Gravity.LEFT | Gravity.CENTER;
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
        textParama.gravity = Gravity.CENTER | Gravity.LEFT;
        textParama.setMargins(width * 1 / 100, 0, 0, 0);
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
        emailParama.height = (int) (height * 7 / 100);
        emailParama.gravity = Gravity.CENTER;
        emailParama.topMargin = height * 3 / 100;
        sEmailEdit.setLayoutParams(emailParama);
        sEmailEdit.setGravity(Gravity.LEFT | Gravity.CENTER);
        sEmailEdit.setPadding(width * 3 / 100, 0, 0, 0);

        LinearLayout.LayoutParams submitParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        submitParama.width = width * 80 / 100;
        submitParama.height = (int) (height * 7 / 100);
        submitParama.topMargin = height * 2 / 100;
        submitParama.gravity = Gravity.CENTER;
        sSubmit.setLayoutParams(submitParama);
        sSubmit.setGravity(Gravity.CENTER);


        LinearLayout.LayoutParams viewLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        viewLayoutParama.width = width * 80 / 100;
        viewLayoutParama.height = (int) (width * 8 / 100);
        viewLayoutParama.gravity = Gravity.CENTER;
        viewLayoutParama.topMargin = height * 2 / 100;
        sViewLayout.setLayoutParams(viewLayoutParama);

        LinearLayout.LayoutParams viewParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        viewParama.width = width * 36 / 100;
        viewParama.height = (int) (height * 0.3 / 100);
        viewParama.gravity = Gravity.CENTER;
        sOrView1.setLayoutParams(viewParama);
        sOrView2.setLayoutParams(viewParama);

        LinearLayout.LayoutParams orTextParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        orTextParama.width = width * 8 / 100;
        orTextParama.height = (int) (width * 8 / 100);
        orTextParama.gravity = Gravity.CENTER;
        sOrText.setLayoutParams(orTextParama);
        sOrText.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams socialLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        socialLayoutParama.width = width * 100 / 100;
        //socialLayoutParama.height = (int)( height * 7/ 100);
        socialLayoutParama.gravity = Gravity.CENTER;
        socialLayoutParama.topMargin = height * 2 / 100;
        sSocialMediaLayout.setLayoutParams(socialLayoutParama);
        sSocialMediaLayout.setGravity(Gravity.CENTER);
//
//        LinearLayout.LayoutParams fbParama = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        fbParama.width = (int) (width * 38 / 100);
//        fbParama.height = (int) (height * 8 / 100);
////        fbParama.leftMargin=width*1/2/100;
////        fbParama.rightMargin=width*1/2/100;//reduce for left and right
//        //fbParama.topMargin=height*1/100;
//        fbParama.gravity = Gravity.CENTER;
//        sFbLogin.setLayoutParams(fbParama);
//        sFbLogin.setPadding(0, width * 7/2/ 100, 0, width * 7/2 / 100);//reduce for top and bottom
//        sFbLogin.setGravity(Gravity.CENTER);
//
//        LinearLayout.LayoutParams gPlusParama = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        gPlusParama.width = (int) (width * 42 / 100);
//        gPlusParama.height = (int) (height * 8 / 100);
//        gPlusParama.gravity = Gravity.CENTER;
//        sGPlusLogin.setLayoutParams(gPlusParama);
//siva
        LinearLayout.LayoutParams fbImageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        fbImageParama.width = (int) (width * 79.5 / 2 / 100);
        fbImageParama.height = (int) (height * 6 / 100);
        fbImageParama.gravity = Gravity.CENTER;
        sFbImage.setLayoutParams(fbImageParama);

        LinearLayout.LayoutParams gPlusImageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        gPlusImageParama.width = (int) (width * 79.5 / 2 / 100);
        gPlusImageParama.height = (int) (height * 6 / 100);
        gPlusImageParama.gravity = Gravity.CENTER;
        gPlusImageParama.leftMargin = width * 1 / 2 / 100;
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