package com.wifin.kachingme.registration_and_login;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.viewpagerindicator.CirclePageIndicator;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.SlideShowAdapter;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.NymsPojo;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.AlertUtils;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashSet;

/**
 * Created by Wifintech on 12-Sep-16.
 */
public class Slideshow extends AppCompatActivity implements OnClickListener {

    public static final String REG_ID = "regId";
    //	Button signout, next;
//	EditText mail;
//	View mView;
    private static final String TAG = "RegistrationMainActivity";
    private static final int PROFILE_PIC_SIZE = 400;
    private static final String APP_VERSION = "appVersion";
    public static ImageView sLogoImage, sLoginImage, sLogoTitleImage;
    boolean fbcheck = false;
    SharedPreferences sharedPreferences;
    Editor editor;
    //    private static final int RC_SIGN_IN = 9001;
    String user_id, username, provider, useremail, image;
    String regId;
    //	Context context;
    Dbhelper dbhelper;
    SharedPreferences pref;
    String db_data;
    int received_msg_count, sent_msg_count;
    ViewPager sPager;
    CirclePageIndicator sIndicator;
    TextView sLogoTitle;
    View sBottomView;
    LinearLayout sMainLayout, sPagerMainLayout;
    private boolean mSignInClicked;
    private boolean mIntentInProgress;

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            Log.d("RegisterActivity",
                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
        //siva
//		ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
//		View.inflate(this, R.layout.slideshow, vg);
        setContentView(R.layout.slideshow);
        // After delete account this condition is used..
        intializeSlideShow();
        screenArrangeSlideShow();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.wifin.kaching.me.ui", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Constant.printMsg("KeyHash:" +
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        if (Build.VERSION.SDK_INT >= 21) {
//				getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.cardview_dark_background));
            getWindow().setStatusBarColor(getResources().getColor(R.color.red_status_bar));
        }
        String text = "<font color=#57584b>Ka</font><font color=#ff0000>C</font><font color=#57584b>hing.me</font>";
        sLogoTitle.setText(Html.fromHtml(text));
        getSupportActionBar().hide();
//		actionBar = getSupportActionBar();
//		this.getActionBar().hide();
        if (Constant.isNiftyApplicationRunning) {
            if (Constant.isModeDebug)
                Constant.printMsg("VVVVVVVVVVV Slide show inside: "
                        + Constant.isNiftyApplicationRunning);
            KachingMeApplication app = (KachingMeApplication) getApplication();
            app.reCreate();
            Constant.isNiftyApplicationRunning = false;
        }
        sharedPreferences = getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
//		context = getApplicationContext();

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        db_data = pref.getString("db_data", "");
        received_msg_count = pref.getInt("received_msg_count", 0);
        sent_msg_count = pref.getInt("sent_msg_count", 0);
        Constant.printMsg("received_msg_count in slideshow get::"
                + received_msg_count + "sent_msg_count" + sent_msg_count);
        Editor editor = pref.edit();
        editor.putInt("received_msg_count", 0);
        editor.putInt("sent_msg_count", 0);
        editor.commit();
        Constant.printMsg("received_msg_count after put:"
                + received_msg_count + "sent_msg_count" + sent_msg_count);
        Constant.printMsg("db_data :" + db_data);
        if (db_data.contains("null") || db_data.equalsIgnoreCase(null)
                || db_data.isEmpty()) {
            Constant.printMsg("db_data entered::::::::::::::::::: value is 0000");
            insertDBDatas();
        } else {
            Constant.printMsg("db_data entered::::::::::::::::::: value is 1111111111");
        }

        dbhelper = new Dbhelper(getApplicationContext());

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.wifin.kaching.me.ui", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Constant.printMsg("KeyHash:"
                        + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//		uiHelper = new UiLifecycleHelper(this, callback);
//		uiHelper.onCreate(savedInstanceState);

//		mail.setVisibility(View.VISIBLE);

        Constant.printMsg("ssss " + Constant.isRegisteredBackPress + "--"
                + Constant.profilemail + " --"
                + new OtpSharedPreference(this).isRegistered() + "---"
                + Constant.manualmail);

        if (Constant.isRegisteredBackPress) {
            new OtpSharedPreference(this).clearRegistrationDetails();
            sharedPreferences.edit().remove("facebookProfilePicture").commit();
            Constant.byteimage = null;
            Constant.bitmapImage=null;
            Constant.isRegisteredBackPress = false;
//			if (Constant.profilemail != null)
//				mail.setText(Constant.profilemail);
        } else {
            if (new OtpSharedPreference(this).isRegistered()
                    && Constant.profilemail != null) {
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
                finish();
            } else {
                new OtpSharedPreference(this).clearRegistrationDetails();
            }
        }

        // Button click listeners
        //	btnSignIn.setOnClickListener(this);
//		btnFbLogin.setOnClickListener(this);

        // Initializing google plus api client
//		mGoogleApiClient = new GoogleApiClient.Builder(this)
//				.addConnectionCallbacks(this)
//				.addOnConnectionFailedListener(this)
//				.addApi(Plus.API, Plus.PlusOptions.builder().build())
//				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

        SlideShowAdapter mAdapter = new SlideShowAdapter(
                getSupportFragmentManager());
        sPager.setAdapter(mAdapter);
        Constant.printMsg("[pager:" + sPager.getCurrentItem());

        sIndicator.setViewPager(sPager);
        startService(new Intent(Slideshow.this, TempConnectionService.class));
        // mPager.setOnPageChangeListener(new OnPageChangeListener() {
        // public void onPageScrollStateChanged(int state) {
        //
        //
        // public void onPageScrolled(int position, float positionOffset,
        // int positionOffsetPixels) {
        // Constant.printMsg("[pager:onPageScrolled");
        // }
        //
        // public void onPageSelected(int position) {
        // Constant.printMsg("[pager:onPageSelected");
        // // Check if this is the page you want.
        // }
        // });

//        if (Connectivity.isConnected(Slideshow.this)) {
//            if (TextUtils.isEmpty(regId)) {
//                regId = registerGCM();
//                Constant.device_id = regId;
//                Constant.printMsg("RegisterActivit GCM RegId: " + regId + " res"
//                        + Constant.device_id);
//            } else {
//                Constant.printMsg("Already Registered with GCM Server!");
//            }
//        } else {
//            Toast.makeText(Slideshow.this,
//                    "Please check your network connection", Toast.LENGTH_SHORT)
//                    .show();
//        }
//		sIndicator.setOnPageChangeListener(new OnPageChangeListener() {
//
//			@Override
//			public void onPageSelected(int arg0) {
//				// TODO Auto-generated method stub
//				if (arg0 == 0) {
////					img.setImageDrawable(getResources().getDrawable(
////							R.drawable.ic_launcher));
//				} else if (arg0 == 1) {
////					img.setImageDrawable(getResources().getDrawable(
////							R.drawable.ic_launcher));
//				} else if (arg0 == 2) {
////					img.setImageDrawable(getResources().getDrawable(
////							R.drawable.ic_launcher));
//				} else if (arg0 == 3) {
////					img.setImageDrawable(getResources().getDrawable(
////							R.drawable.ic_launcher));
//				} else if (arg0 == 4) {
////					img.setImageDrawable(getResources().getDrawable(
////							R.drawable.ic_launcher));
//				} else if (arg0 == 5) {
////					img.setImageDrawable(getResources().getDrawable(
////							R.drawable.ic_launcher));
//				}
//				Constant.printMsg("[pager:onpage scroll state cange"
//						 +sPager.getCurrentItem());
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});


    }

    private void intializeSlideShow() {
        sLogoImage = (ImageView) findViewById(R.id.slide_logoimage);
//		signout = (Button) findViewById(R.id.signout);
//		next = (Button) findViewById(R.id.btn_start_messanging);
        sIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        sBottomView = (View) findViewById(R.id.slide_bottomView);
        sPager = (ViewPager) findViewById(R.id.pager);
        sLogoTitle = (TextView) findViewById(R.id.slide_logTitle);
        sLoginImage = (ImageView) findViewById(R.id.slide_loginImage);
        sMainLayout = (LinearLayout) findViewById(R.id.register_mainLayout);
        sPagerMainLayout = (LinearLayout) findViewById(R.id.register_mainPagerLayout);
        sLogoTitleImage = (ImageView) findViewById(R.id.slide_logTitleImage);
//		signout.setOnClickListener(this);
//		cartno.setOnClickListener(this);
        sLoginImage.setOnClickListener(this);
        Constant.typeFaceBold(this, sLogoTitle);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.slide_loginImage:
                if (Connectivity.isConnected(this)){
                    if (SliderFragment.gplusCheck) {
                        Constant.printMsg("Logged slide show gplus");
                        SliderFragment.signOut();
                    } else {
                        if (SliderFragment.fbGpluscheck) {
                            SliderFragment.logoutFromFacebook();
                            Constant.printMsg("Logged slide show fb");
                        } else {
                            Constant.printMsg("Logged slide show nothing");
                        }
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (!TempConnectionService.connection.isConnected()) {
                                try {
                                    TempConnectionService.connection.connect();
                                    Constant.printMsg("login initial  show. TempConnectionService....");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Constant.printMsg("login initial  show. TempConnectionService Exception...." + e);
                                }
                            }
                        }
                    }).start();
                    startActivity(new Intent(Slideshow.this, Signin.class));
                    finish();
                    Constant.login = true;
                    Constant.printMsg("succeeeeeeeeeeeeee slide show" + Constant.login);
                }else {
                    Toast.makeText(this,"Please Check Your Network Connection.!!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_cart:
//				Constant.login = true;
//				startActivity(new Intent(Slideshow.this, Signin.class));
//				finish();
                break;
//            case R.id.btn_start_messanging:
//
//				new OtpSharedPreference(Slideshow.this)
//						.clearRegistrationDetails();
//				int vis = mail.getVisibility();
//				Constant.printMsg("visiv::" + vis);
//				if (mail.getText().toString().length() > 0) {
//
//					if (CheckValidation()) {
//						DbDelete();
//						Constant.manualmail = mail.getText().toString();
//						new mailValidation().execute();
//					}
//				} else {
//					fetchFrom();
//					startActivity(new Intent(Slideshow.this, RegisterActivity.class));
//					finish();
//
//				}
//				siva
//                break;
//		case R.id.signin:
//			// Signin button clicked
//			if (KachingMeApplication.getIsNetAvailable()) {
////			signInWithGplus();
//			} else {
//				new CommonMethods().Toast_call(
//						this,
//						getResources().getString(
//								R.string.no_internet_connection));
//			}
//			break;
//
//		case R.id.signout:
//			// Signout button clicked
//
//			String out = signout.getText().toString();
//			String tx = "Logout From Google";
//
//			if (out.equals(tx)) {
//
////				signOutFromGplus();
//			}
//			break;
//siva
            case R.id.authButton:
                // Signout button clicked
                if (Connectivity.isConnected(this)){
                    if (fbcheck == false) {
//					SocialActivity.openActiveSession(this, true, callback);
                    }
                } else {
                    new AlertUtils().Toast_call(
                            this,
                            getResources().getString(
                                    R.string.no_internet_connection));
                }
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Constant.printMsg("Logged onActivityResult..." + resultCode + "...." + requestCode);
        if (SliderFragment.gplusCheck) {
            Constant.printMsg("Logged onActivityResult...gplus");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            SliderFragment.handleSignInResult(result);
        }

        if (SliderFragment.mCallbackManager.onActivityResult(requestCode, resultCode, data)) {
            Constant.printMsg("Logged onActivityResult...fb");
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        /*
         * menu.add("Skip")
		 *
		 * .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		 */
        // MenuCompat.setShowAsAction(menu.add("Skip"),
        // MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    public void DbDelete() {
        // TODO Auto-generated method stub

        try {
            int a = dbhelper.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_SOCIAL, null, null);
            System.out
                    .println("No of deleted rows from bux data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            dbhelper.close();
        }
    }

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//
//		editor.putBoolean("is_slideshow", true);
//		editor.commit();
//		Constant.printMsg("option selected");
//		Intent intent = new Intent(Slideshow.this, MainActivity.class);
//		startActivity(intent);
//		finish();
//		return super.onOptionsItemSelected(item);
//	}

    public void fetchFrom() {

        String namet = null, mailt = null, phtt = null;

        Dbhelper db = new Dbhelper(getApplicationContext());
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

        // Constant.profileimg = phtt;

        Constant.profilename = namet;
        Constant.printMsg("Google Plus Test222");

        // Constant.profilemail = mailt;
    }

    protected void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
//		if (mGoogleApiClient.isConnected()) {
//			mGoogleApiClient.disconnect();
//		}
    }

    /**
     * Fetching user's information name, email, profile pic
     */
//	private void getProfileInformation() {
//		try {
//			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
//				Person currentPerson = Plus.PeopleApi
//						.getCurrentPerson(mGoogleApiClient);
//				String personName = currentPerson.getDisplayName();
//				String personPhotoUrl = currentPerson.getImage().getUrl();
//				String personGooglePlusProfile = currentPerson.getUrl();
//				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
//				Log.e(TAG, "Name: " + personName + ", plusProfile: "
//						+ personGooglePlusProfile + ", email: " + email
//						+ ", Image: " + personPhotoUrl);
//
//				Constant.profilemail = personName;
//				Constant.profileimg = personGooglePlusProfile;
//				Constant.manualmail = email;
//
//				// txtName.setText(personName);
//				// txtEmail.setText(email);
//
//				// by default the profile url gives 50x50 px image only
//				// we can replace the value with whatever dimension we want by
//				// replacing sz=X
//				personPhotoUrl = personPhotoUrl.substring(0,
//						personPhotoUrl.length() - 2)
//						+ PROFILE_PIC_SIZE;
//
//				Constant.profileimg = personPhotoUrl;
//
//				Constant.profilename = personName;
//				Constant.printMsg("Google Plus Test111");
//				Constant.printMsg("Google Plus Test " + personName + "  "
//						+ Constant.profilemail + "  " + Constant.manualmail
//						+ "   " + Constant.profileimg);
//
//				// Constant.profilemail = email;
//
//				ContentValues cv = new ContentValues();
//
//				cv.put("name", personName);
//				cv.put("mail", email);
//				cv.put("photo", personPhotoUrl);
//
//				insertDB(cv);
//
//			} else {
//				Toast.makeText(getApplicationContext(),
//						"Person information is null", Toast.LENGTH_LONG).show();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
    public void insertDB(ContentValues v) {

        try {
            int a = (int) dbhelper.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_SOCIAL, null, v);

            Constant.printMsg("No of inserted rows in details :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in new shop details ::::::"
                    + e.toString());
        } finally {
            dbhelper.close();
        }

    }

    /*
     * To Register with google cloud added on 10-09-2014 by prabhakaran
     */
//    public String registerGCM() {
//        // preloaderStart();
//        // progressDialog.show();
////		gcm = GoogleCloudMessaging.getInstance(this);
//        regId = getRegistrationId();
//        if (TextUtils.isEmpty(regId)) {
//            registerInBackground();
//            Log.d("RegisterActivity",
//                    "registerGCM - successfully registered with GCM server - regId: "
//                            + regId);
//
//        } else {
//            // preloaderStop();
//
//        }
//        return regId;
//    }

//    private String getRegistrationId() {
//        final SharedPreferences prefs = getSharedPreferences(
//                Slideshow.class.getSimpleName(), Context.MODE_PRIVATE);
//        String registrationId = prefs.getString(REG_ID, "");
//        if (registrationId.isEmpty()) {
//            Log.i("ECPL", "Registration not found.");
//            return "";
//        }
//        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
//        int currentVersion = getAppVersion(Slideshow.this);
//        if (registeredVersion != currentVersion) {
//            Log.i("ECPL", "App version changed.");
//            return "";
//        }
//        return registrationId;
//    }

//    private void registerInBackground() {
//        new AsyncTask<Void, Void, String>() {
//
//            @Override
//            protected String doInBackground(Void... params) {
//                String msg = "";
//                try {
////					if (gcm == null) {
////						gcm = GoogleCloudMessaging.getInstance(context);
////					}
////					regId = gcm.register(Constant.GOOGLE_PROJECT_ID);
//                    Log.d("RegisterActivity", "registerInBackground - regId: "
//                            + regId);
//                    msg = "Device registered, register_activity ID = " + regId;
//
//                    Constant.printMsg("inside posting" + msg);
//
//                    Constant.device_id = regId;
//                    // stored the register_activity ID in shared preferences
//                    storeRegistrationId(Slideshow.this, regId);
//                } catch (Exception ex) {
//                    msg = "Error :" + ex.getMessage();
//                    Log.d("RegisterActivity", "Error: " + msg);
//                }
//                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
//
//                return msg;
//            }
//
//            @Override
//            protected void onPostExecute(String msg) {
//
//                google_reg(msg);
//
//                // progressDialog.dismiss();
//
//            }
//
//            @Override
//            protected void onPreExecute() {
//                // TODO Auto-generated method stub
//                super.onPreExecute();
//
//            }
//
//        }.execute(null, null, null);
//    }

//    public void google_reg(String msg) {
//        Constant.printMsg("GCM Register id is ::::::::::" + msg);
//
//        if (msg.contains("SERVICE_NOT_AVAILABLE")) {
//            Constant.device_id = "";
//            registerGCM();
//        } else {
//            Constant.printMsg("GCM Registration Success ::::::::");
//        }
//    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(
                Slideshow.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Log.i("ECPL", "Saving regId on app version " + appVersion);
        Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();

    }

    // private void insertDBDatas() {
    // // TODO Auto-generated method stub
    //
    // // default 25 data set for Kons and Dazz
    //
    // String[] nym_shortform = { "hru", "wru", "wt", "pls", "omg", "2mrw",
    // "4", "asap", "ayt", "bbs", "bf", "cu", "cuz", "fb", "gb",
    // "gr8", "hbd", "gl", "ic", "lol", "otw", "sup", "thnx", "tyt",
    // "u2" };
    // String[] nym_meaning = { "how.are.you", "where.are.you", "what",
    // "please", "oh.my.god", "tomorrow", "for",
    // "as.soon.as.possible", "are.you.there", "be.back.soon",
    // "boy.friend", "see.you", "because", "facebook", "good.bye",
    // "great", "happy.birthday", "good.luck", "i.see",
    // "laugh.out.loud", "on.the.way", "Whats.up", "thanks",
    // "take.your.time", "you.too" };
    //
    // String[] kons_words = { "love", "hai", "lol", "thnx", "asap", "hRu",
    // "OMG", "gr8", "2mrw", "g00d", "frnd", "NiCe", "try", "tAlk",
    // "Sit", "h0t", "co0l", "r0om", "weLL", "cU", "thR", "lUck",
    // "rOFl", "pLz", "WaIt" };
    //
    // String[] zzle_msg = { "I WILL ALWAYS LOVE YOU", "missing you",
    // "PLEASE FORGIVE ME", "are you okay?", "Thank you",
    // "Hey Dude!!", "TOO GOOD FOR YOU", "EMERGENCY, CALL ME",
    // "big a smile", "ALL THE BEST", "boss is listening",
    // "BE RIGHT BACK", "SEE YOU LATER", "free to talk",
    // "HUG AND KISS", "keep in touch", "PUT ON A HAPPY FACE",
    // "TAKE CARE OF YOURSELF", "thinking of you", "TA TA FOR NOW",
    // "YOU ARE A CUTIE", "Welcome back", "wait 4 me", "Want 2 talk?",
    // "happy wEEkEnd" };
    //
    // String statusvalues[] = { "Available", "Busy", "At School",
    // "At the movies", "At Work", "Battery about to die",
    // "Can\'t talk, KaChing.me only", "In a meeting", "At the gym",
    // "Sleeping", "Urgent calls only" };
    //
    // int status_length = statusvalues.length;
    //
    // Constant.printMsg("status_length:::" + status_length
    // + "status_length::" + status_length);
    // for (int k = 0; k < status_length; k++) {
    // ContentValues v = new ContentValues();
    // // v.put("name", k);
    // v.put("status", statusvalues[k]);
    // Constant.printMsg("check value::statusvalues" + statusvalues[k]
    // + "k" + k);
    // insertStatusDB(v);
    // }
    // int nym_length = nym_shortform.length;
    // int nym_meaning_length = nym_meaning.length;
    // Constant.printMsg("nym_length:::" + nym_length
    // + "nym_meaning_length::" + nym_meaning_length);
    // for (int k = 0; k < nym_length; k++) {
    // ContentValues v = new ContentValues();
    // v.put("name", nym_shortform[k]);
    // v.put("meaning", nym_meaning[k]);
    // Constant.printMsg("check value::" + nym_shortform[k]
    // + "  nym_meaning.get(k)" + nym_meaning[k] + "k" + k);
    // insertNymDB(v);
    // }
    //
    // int kons_length = kons_words.length;
    // Constant.printMsg("kons_length::" + kons_length);
    // for (int kons = 0; kons < kons_length; kons++) {
    // Constant.printMsg("check value:kons:" + kons_words[kons]
    // + "kons" + kons);
    // ContentValues v = new ContentValues();
    // v.put("msg", kons_words[kons]);
    // insertKons(v);
    //
    // }
    //
    // int zzle_length = zzle_msg.length;
    // Constant.printMsg("zzle_length::" + zzle_length);
    // for (int z = 0; z < zzle_length; z++) {
    // Constant.printMsg("check value:zzle_msg:" + zzle_msg[z] + "zzle"
    // + z);
    // ContentValues v = new ContentValues();
    // v.put("msg", zzle_msg[z]);
    // v.put("backgrount", "black");
    // v.put("fontcolor", "white");
    // v.put("speed", "slow");
    // v.put("font", "large");
    //
    // if (z == 1 || z == 7 || z == 10 || z == 13 || z == 16) {
    // v.put("backgrount", "white");
    // v.put("fontcolor", "black");
    // v.put("speed", "medium");
    // v.put("font", "small");
    // }
    // if (z == 2 || z == 5 || z == 8 || z == 11) {
    // v.put("backgrount", "blue");
    // v.put("fontcolor", "black");
    // v.put("speed", "fast");
    // v.put("font", "medium");
    // }
    // if (z == 3 || z == 6 || z == 9 || z == 15 || z == 18) {
    // v.put("backgrount", "pink");
    // v.put("fontcolor", "black");
    // v.put("speed", "medium");
    // v.put("font", "small");
    // }
    // if (z == 19 || z == 4 || z == 12 || z == 21 || z == 23) {
    // v.put("backgrount", "green");
    // v.put("fontcolor", "black");
    // v.put("speed", "fast");
    // v.put("font", "medium");
    // }
    //
    // insertZzle(v);
    // Constant.printMsg("insertZzle:::zz:" + z + "     " + v);
    // }
    // // first time only it should run , so default value is 0 , now the value
    // // is changed as 1.
    // db_data = "1";
    // updateDBvalue(db_data);
    // }

    private void insertDBDatas() {
        // TODO Auto-generated method stub

        // default 25 data set for Kons and Dazz
        Dbhelper db = new Dbhelper(getApplicationContext());
        Dbhelper.Mydatabase my = db.mydata.getInstance(getApplicationContext());
        // my.handleNcaMdaResponse(result);
        SQLiteDatabase s = my.getWritableDatabase();

        // SQLiteDatabase s = d.mydata.getWritableDatabase();
        s.beginTransaction();
        String[] nym_shortform = {"hru", "wru", "wt", "pls", "omg", "2mrw",
                "4", "asap", "ayt", "bbs", "bf", "cu", "cuz", "fb", "gb",
                "gr8", "hbd", "gl", "ic", "lol", "otw", "sup", "thnx", "tyt",
                "u2"};
        String[] nym_meaning = {"how.are.you", "where.are.you", "what",
                "please", "oh.my.god", "tomorrow", "for",
                "as.soon.as.possible", "are.you.there", "be.back.soon",
                "boy.friend", "see.you", "because", "facebook", "good.bye",
                "great", "happy.birthday", "good.luck", "i.see",
                "laugh.out.loud", "on.the.way", "Whats.up", "thanks",
                "take.your.time", "you.too"};

        String[] kons_words = {"love", "hai", "lol", "thnx", "asap", "hRu",
                "OMG", "gr8", "2mrw", "g00d", "frnd", "NiCe", "try", "tAlk",
                "Sit", "h0t", "co0l", "r0om", "weLL", "cU", "thR", "lUck",
                "rOFl", "pLz", "WaIt"};

        String[] zzle_msg = {"I WILL ALWAYS LOVE YOU", "missing you",
                "PLEASE FORGIVE ME", "are you okay?", "Thank you",
                "Hey Dude!!", "TOO GOOD FOR YOU", "EMERGENCY, CALL ME",
                "big a smile", "ALL THE BEST", "boss is listening",
                "BE RIGHT BACK", "SEE YOU LATER", "free to talk",
                "HUG AND KISS", "keep in touch", "PUT ON A HAPPY FACE",
                "TAKE CARE OF YOURSELF", "thinking of you", "TA TA FOR NOW",
                "YOU ARE A CUTIE", "Welcome back", "wait 4 me", "Want 2 talk?",
                "happy wEEkEnd"};

        String statusvalues[] = {"Available", "Busy", "At School",
                "At the movies", "At Work", "Battery about to die",
                "Can\'t talk, KaChing.me only", "In a meeting", "At the gym",
                "Sleeping", "Urgent calls only"};

        int status_length = statusvalues.length;

        Constant.printMsg("status_length:::" + status_length
                + "status_length::" + status_length);
        for (int k = 0; k < status_length; k++) {
            ContentValues v = new ContentValues();
            // v.put("name", k);
            v.put("status", statusvalues[k]);
            Constant.printMsg("check value::statusvalues" + statusvalues[k]
                    + "k" + k);
            // insertStatusDB(v);
            s.insert(db.TABLE_UPDATE_STATUS, null, v);
            Constant.printMsg("ppp status" + v);
        }
        int nym_length = nym_shortform.length;
        int nym_meaning_length = nym_meaning.length;
        Constant.printMsg("nym_length:::" + nym_length
                + "nym_meaning_length::" + nym_meaning_length);
        for (int k = 0; k < nym_length; k++) {
            ContentValues v = new ContentValues();
            v.put("name", nym_shortform[k]);
            v.put("meaning", nym_meaning[k]);
            Constant.printMsg("check value::" + nym_shortform[k]
                    + "  nym_meaning.get(k)" + nym_meaning[k] + "k" + k);
            // insertNymDB(v);
            s.insert(db.TABLE_NYM, null, v);
            Constant.printMsg("ppp nnnymc" + v);
        }

        int kons_length = kons_words.length;
        Constant.printMsg("kons_length::" + kons_length);
        for (int kons = 0; kons < kons_length; kons++) {
            Constant.printMsg("check value:kons:" + kons_words[kons]
                    + "kons" + kons);
            ContentValues v = new ContentValues();
            v.put("msg", kons_words[kons]);
            // insertKons(v);
//            s.insert(db.TABLE_KONS, null, v);
            Constant.printMsg("ppp kons" + v);

        }

        int zzle_length = zzle_msg.length;
        Constant.printMsg("zzle_length::" + zzle_length);
        for (int z = 0; z < zzle_length; z++) {
            Constant.printMsg("check value:zzle_msg:" + zzle_msg[z] + "zzle"
                    + z);
            ContentValues v = new ContentValues();
            v.put("msg", zzle_msg[z]);
            v.put("backgrount", "black");
            v.put("fontcolor", "white");
            v.put("speed", "slow");
            v.put("font", "large");

            if (z == 1 || z == 7 || z == 10 || z == 13 || z == 16) {
                v.put("backgrount", "white");
                v.put("fontcolor", "black");
                v.put("speed", "medium");
                v.put("font", "small");
            }
            if (z == 2 || z == 5 || z == 8 || z == 11) {
                v.put("backgrount", "blue");
                v.put("fontcolor", "black");
                v.put("speed", "fast");
                v.put("font", "medium");
            }
            if (z == 3 || z == 6 || z == 9 || z == 15 || z == 18) {
                v.put("backgrount", "pink");
                v.put("fontcolor", "black");
                v.put("speed", "medium");
                v.put("font", "small");
            }
            if (z == 19 || z == 4 || z == 12 || z == 21 || z == 23) {
                v.put("backgrount", "green");
                v.put("fontcolor", "black");
                v.put("speed", "fast");
                v.put("font", "medium");
            }

            // insertZzle(v);
            s.insert(db.TABLE_ZZLE, null, v);
            Constant.printMsg("ppp zzle:" + v);
            Constant.printMsg("insertZzle:::zz:" + z + "     " + v);
        }
        // first time only it should run , so default value is 0 , now the value
        // is changed as 1.
        db_data = "1";
        updateDBvalue(db_data);
        s.setTransactionSuccessful();
        s.endTransaction();

        s.close();
    }

    public void updateDBvalue(String code) {
        Editor e = pref.edit();
        e.putString("db_data", code);
        Constant.printMsg("updateDBvalue:" + code);
        e.commit();
    }

    public void fetchNymFrom() {

        Constant.addedNyms.clear();

        String tx, mn;

        Dbhelper db = new Dbhelper(getApplicationContext());
        Cursor c = null;
        try {
            c = db.open()
                    .getDatabaseObj()
                    .query(Dbhelper.TABLE_NYM, null, null, null, null, null,
                            null);
            int txnm = c.getColumnIndex("name");
            int mnnm = c.getColumnIndex("meaning");

            Constant.printMsg("The pending cart list in db ::::"
                    + c.getCount());

            if (c.getCount() > 0) {

                while (c.moveToNext()) {

                    tx = c.getString(txnm);
                    mn = c.getString(mnnm);

                    Constant.printMsg("dbadd:nym:" + tx + "  " + mn + "  ");

                    NymsPojo p = new NymsPojo();
                    p.setMeaning(mn);
                    p.setText(tx);

                    Constant.addedNyms.add(p);
                }
            }
        } catch (SQLException e) {

            Constant.printMsg("Sql exception in pending shop details ::::"
                    + e.toString());
        } finally {
            c.close();
            db.close();
        }

        SetList();
    }

    public void SetList() {
        // TODO Auto-generated method stub

        if (Constant.addedNyms.size() > 0) {

            for (int i = 0; i < Constant.addedNyms.size(); i++) {

                LinkedHashSet hs = new LinkedHashSet();

                hs.addAll(Constant.mDictionaryList);
                Constant.mDictionaryList.clear();
                Constant.mDictionaryList.addAll(hs);
                LinkedHashSet hs1 = new LinkedHashSet();
                hs1.addAll(Constant.mDictionaryMeaningList);
                Constant.mDictionaryMeaningList.clear();
                Constant.mDictionaryMeaningList.addAll(hs1);

                Constant.printMsg("nyms::::>>>" + Constant.addedNyms.size());
                Constant.printMsg("nyms constant::>>>>>"
                        + Constant.mDictionaryList + "  "
                        + Constant.mDictionaryMeaningList);

                String ny = Constant.addedNyms.get(i).getText().toString();
                String nymm = Constant.addedNyms.get(i).getMeaning().toString();

                if (Constant.mDictionaryList.size() > 0) {

                    // for (int j = 0; j <
                    // Constant.mDictionaryList.size(); j++) {
                    //
                    // String chk = Constant.mDictionaryList.get(j)
                    // .toString();
                    //
                    // if (ny.equals(chk)) {
                    //
                    // } else {
                    //
                    // Constant.mDictionaryList.add(ny);
                    // Constant.mDictionaryMeaningList.add(nymm);
                    // }
                    //
                    // }

                    if (Constant.mDictionaryList.size() > 0) {

                        Constant.printMsg("List Size"
                                + Constant.mDictionaryList.size());

                        if (!Constant.mDictionaryList.contains(ny)) {
                            Constant.mDictionaryList.add(ny);
                            Constant.mDictionaryMeaningList.add(nymm);

                        }
                    }

                } else {

                    Constant.mDictionaryList.add(ny);
                    Constant.mDictionaryMeaningList.add(nymm);

                }

            }
        }

    }

    // public void insertStatusDB(ContentValues v) {
    // Dbhelper db = new Dbhelper(getApplicationContext());
    // try {
    // int a = (int) db.open().getDatabaseObj()
    // .insert(Dbhelper.TABLE_UPDATE_STATUS, null, v);
    // Constant
    // .prtMsg("No of rows inserted into TABLE_UPDATE_STATUS  is ::::"
    // + a);
    // } catch (SQLException e) {
    //
    // } finally {
    // db.close();
    // }
    // }

    public void screenArrangeSlideShow() {
        // TODO Auto-generated method stub
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Constant.screenHeight = height;
        Constant.screenWidth = width;

        LinearLayout.LayoutParams logoImageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        logoImageParama.width = (int) (width * 34 / 100);
        logoImageParama.height = (int) (width * 30 / 100);
        logoImageParama.gravity = Gravity.CENTER | Gravity.TOP;
        logoImageParama.topMargin = height * 10 / 100;
        sLogoImage.setLayoutParams(logoImageParama);

        LinearLayout.LayoutParams logTitleParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //logTitleParama.height = (int) (height * 83 / 100);
        logTitleParama.gravity = Gravity.CENTER;
        logTitleParama.topMargin = height * 1 / 100;
        sLogoTitle.setLayoutParams(logTitleParama);

        LinearLayout.LayoutParams logoTitleImageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        logoTitleImageParama.height = (int) (height * 3 / 100);
        logoTitleImageParama.width = (int) (width * 30 / 100);
        logoTitleImageParama.topMargin = (int) (height * 1.5 / 100);
        logoTitleImageParama.gravity = Gravity.CENTER;
        sLogoTitleImage.setLayoutParams(logoTitleImageParama);

        FrameLayout.LayoutParams loginImageParama = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        loginImageParama.height = (int) (width * 7 / 100);
        loginImageParama.width = (int) (width * 7 / 100);
        loginImageParama.topMargin = (int) (height * 18 / 100);
        loginImageParama.gravity = Gravity.RIGHT;
        loginImageParama.rightMargin = width * 9 / 100;
        sLoginImage.setLayoutParams(loginImageParama);

        FrameLayout.LayoutParams pagerMainLayoutParama = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        pagerMainLayoutParama.height = (int) (height * 65 / 100);
        pagerMainLayoutParama.width = (int) (width * 97.5 / 100);
        pagerMainLayoutParama.gravity = Gravity.CENTER;
        pagerMainLayoutParama.leftMargin = width * 5 / 2 / 100;
        sPagerMainLayout.setLayoutParams(pagerMainLayoutParama);
        sPagerMainLayout.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams pagerParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        pagerParama.height = (int) (height * 60 / 100);
        pagerParama.width = (int) (width * 85 / 100);
        pagerParama.gravity = Gravity.CENTER;
        pagerParama.rightMargin = width * 5 / 2 / 100;
        sPager.setLayoutParams(pagerParama);

        FrameLayout.LayoutParams mainLayoutParama = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mainLayoutParama.height = (int) (height * 60 / 100);
        mainLayoutParama.width = (int) (width * 90 / 100);
        mainLayoutParama.gravity = Gravity.CENTER;
        sMainLayout.setLayoutParams(mainLayoutParama);
        sMainLayout.setGravity(Gravity.CENTER);


        FrameLayout.LayoutParams pagerIndicatorParama = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //pagerIndicatorParama.height = (int) (height * 10 / 100);
        pagerIndicatorParama.gravity = Gravity.CENTER | Gravity.BOTTOM;
        pagerIndicatorParama.bottomMargin = height * 14 / 100;
        sIndicator.setLayoutParams(pagerIndicatorParama);

        FrameLayout.LayoutParams bottomViewParama = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomViewParama.height = (int) (height * 0.6 / 100);
        bottomViewParama.gravity = Gravity.CENTER | Gravity.BOTTOM;
        sBottomView.setLayoutParams(bottomViewParama);

        if (width >= 600) {
//			cartno.setTextSize(16);
            sLogoTitle.setTextSize(19);
        } else if (width > 501 && width < 600) {
//			cartno.setTextSize(15);
            sLogoTitle.setTextSize(18);
        } else if (width > 260 && width < 500) {
//			cartno.setTextSize(14);
            sLogoTitle.setTextSize(17);
        } else if (width <= 260) {
//			cartno.setTextSize(13);
            sLogoTitle.setTextSize(16);
        }
    }
}
