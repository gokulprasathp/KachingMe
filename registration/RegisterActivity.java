package com.wifin.kachingme.registration_and_login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.wifin.kachingme.R;
import com.wifin.kachingme.applications.NiftyApplication;
import com.wifin.kachingme.pojo.CountryCodeGetSet;
import com.wifin.kachingme.pojo.UserContactDto;
import com.wifin.kachingme.util.AvatarManager;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.RounderImageView;
import com.wifin.kachingme.util.Utils;
import com.wifin.kachingme.util.Validation;
import com.wifin.kachingme.util.XMLParser;
import com.wifin.kachingme.util.cropimage.CropImage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * @author siva
 *         Created by Wifintech on 12-Sep-16.
 */
public class RegisterActivity extends Slideshow {

    LinearLayout mEmailLayout, mProfileImageMainLayout, mPersonDetailMainLayout, mPassswordMainLayout,
            mcheckLayout1, mcheckLayout2, mGenderLayout;
    FrameLayout mProfileImageLayout;
    TextView mWelcomeText, mEmailIdText, mNextToUserName, mDateOfBirth, mNextToPasswod, mNextLast,
            mCheckCondition1, mCheckCondition2, mMaleText, mFemaleText;
    RounderImageView mProfileImage;
    ImageView mCameraImage, mCheckImage1, mCheckImage2, mMaleImage, mFemaleImage;
    EditText mFirstName, mLastName, mPassword, mReEnterPassword;
    RadioGroup mGenderRadioGroup;
    RadioButton mMale, mFemale, mRadioSexButton;

    CommonMethods commonMethods;
    OtpSharedPreference prefOtp;

    int width, height;
    boolean isAgreeSync, isAgreeCondition;
    int seXvalue;
    byte img_byte[];
    private Uri fileUri;
    boolean tabletSize = false;
    public static final int REQUEST_CODE_CROP_IMAGE = 12;
    String TAG = RegisterActivity.class.getSimpleName();
    GoogleCloudMessaging gcm;
    SharedPreferences pref, preference;
    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";
    Calendar cal;
    private int year, month, day;
    static final int DATE_DIALOG_FROMID = 1,
            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 99;
    ArrayList<CountryCodeGetSet> country_list;
    ArrayList<UserContactDto> conlist = new ArrayList<UserContactDto>();
    Spanned spannedResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // saving the data when activity in onPause state
        ViewGroup vg = (ViewGroup) findViewById(R.id.register_mainLayout);
        View.inflate(this, R.layout.register_activity, vg);
        intializeRegistration();
        screenArrangeRegistration();

        prefOtp = new OtpSharedPreference(this);
        commonMethods = new CommonMethods(RegisterActivity.this);
        sIndicator.setVisibility(View.GONE);
        sPager.setVisibility(View.GONE);
        mPersonDetailMainLayout.setVisibility(View.GONE);
        mPassswordMainLayout.setVisibility(View.GONE);

        setBackReturnData();
    }

    private void intializeRegistration() {
        // TODO Auto-generated method stub
        mProfileImageMainLayout = (LinearLayout) findViewById(R.id.register_profileimageLayout);
        mPersonDetailMainLayout = (LinearLayout) findViewById(R.id.register_personalDetailsLayout);
        mPassswordMainLayout = (LinearLayout) findViewById(R.id.register_passwordLayout);
        mEmailLayout = (LinearLayout) findViewById(R.id.email_layout);
        mcheckLayout1 = (LinearLayout) findViewById(R.id.register_checkLayout1);
        mcheckLayout2 = (LinearLayout) findViewById(R.id.register_checkLayout2);
        mProfileImageLayout = (FrameLayout) findViewById(R.id.profile_image_layout);
        mGenderLayout = (LinearLayout) findViewById(R.id.register_genderLayout);
        mWelcomeText = (TextView) findViewById(R.id.welcome_text);
        mEmailIdText = (TextView) findViewById(R.id.email_text);
        mProfileImage = (RounderImageView) findViewById(R.id.profile_image);
        mCameraImage = (ImageView) findViewById(R.id.camera_image);
        mNextToUserName = (TextView) findViewById(R.id.register_nextToPersonDetails);
        mFirstName = (EditText) findViewById(R.id.register_firstName);
        mLastName = (EditText) findViewById(R.id.register_LastName);
        mPassword = (EditText) findViewById(R.id.register_password);
        mReEnterPassword = (EditText) findViewById(R.id.register_passwordReEnter);
        mGenderRadioGroup = (RadioGroup) findViewById(R.id.register_radioGroup);
        mMale = (RadioButton) findViewById(R.id.register_radioButtonMale);
        mFemale = (RadioButton) findViewById(R.id.register_radioButtonFemale);
        mCheckCondition1 = (TextView) findViewById(R.id.register_check1);
        mCheckCondition2 = (TextView) findViewById(R.id.register_check2);
        mCheckImage1 = (ImageView) findViewById(R.id.register_checkImage1);
        mCheckImage2 = (ImageView) findViewById(R.id.register_checkImage2);
        mDateOfBirth = (TextView) findViewById(R.id.register_dateOfBirth);
        mNextToPasswod = (TextView) findViewById(R.id.register_nextToPassword);
        mNextLast = (TextView) findViewById(R.id.register_next);
        mMaleText = (TextView) findViewById(R.id.register_radioMaleText);
        mFemaleText = (TextView) findViewById(R.id.register_radioFeMaleText);
        mMaleImage = (ImageView) findViewById(R.id.register_radioMaleImage);
        mFemaleImage = (ImageView) findViewById(R.id.register_radioFeMaleImage);

        Constant.typeFace(this, mWelcomeText);
        Constant.typeFace(this, mEmailIdText);
        Constant.typeFace(this, mNextToUserName);
        Constant.typeFace(this, mFirstName);
        Constant.typeFace(this, mLastName);
        Constant.typeFace(this, mPassword);
        Constant.typeFace(this, mReEnterPassword);
        Constant.typeFace(this, mMale);
        Constant.typeFace(this, mFemale);
        Constant.typeFace(this, mCheckCondition1);
        Constant.typeFace(this, mCheckCondition2);
        Constant.typeFace(this, mDateOfBirth);
        Constant.typeFace(this, mNextToPasswod);
        Constant.typeFace(this, mNextLast);
        Constant.typeFace(this, mMaleText);
        Constant.typeFace(this, mFemaleText);

        mNextToUserName.setOnClickListener(this);
        mNextLast.setOnClickListener(this);
        mNextToPasswod.setOnClickListener(this);
        mDateOfBirth.setOnClickListener(this);
        mCheckImage1.setOnClickListener(this);
        mCheckImage2.setOnClickListener(this);
        mMaleImage.setOnClickListener(this);
        mFemaleImage.setOnClickListener(this);
        mCameraImage.setOnClickListener(this);
        sLoginImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.slide_loginImage:
                Constant.login = true;
                startActivity(new Intent(RegisterActivity.this, Signin.class));
                finish();
                break;
            case R.id.camera_image:
                //popupChooseImage();
                chooseImage();
                break;
            case R.id.register_nextToPersonDetails:
                mProfileImageMainLayout.setVisibility(View.GONE);
                mPersonDetailMainLayout.setVisibility(View.VISIBLE);
                mPassswordMainLayout.setVisibility(View.GONE);
                break;
            case R.id.register_dateOfBirth:
                showDialog(DATE_DIALOG_FROMID);
                break;
            case R.id.register_radioMaleImage:
                mMale.setChecked(true);
                mMaleImage.setImageResource(R.drawable.tick_red);
                mMaleImage.setBackgroundResource(R.drawable.border_style_red);
                mFemaleImage.setImageDrawable(null);
                mFemaleImage.setBackgroundResource(R.drawable.border_style_gray);
                break;
            case R.id.register_radioFeMaleImage:
                mFemale.setChecked(true);
                mFemaleImage.setImageResource(R.drawable.tick_red);
                mFemaleImage.setBackgroundResource(R.drawable.border_style_red);
                mMaleImage.setImageDrawable(null);
                mMaleImage.setBackgroundResource(R.drawable.border_style_gray);
                break;
            case R.id.register_nextToPassword:
                nextToPersonalDetails();
                break;
            case R.id.register_checkImage1:
                isAgreeCondition = true;
                mCheckImage1.setImageResource(R.drawable.tick_red);
                mCheckImage1.setBackgroundResource(R.drawable.border_style_red);
                break;
            case R.id.register_checkImage2:
                isAgreeSync = true;
                mCheckImage2.setImageResource(R.drawable.tick_red);
                mCheckImage2.setBackgroundResource(R.drawable.border_style_red);
                break;
            case R.id.register_next:
                nextSubmitProcess();
                break;
            default:
                break;
        }
    }

    private void setBackReturnData() {
        mMale.setChecked(true);
        mMaleImage.setImageResource(R.drawable.tick_red);
        mMaleImage.setBackgroundResource(R.drawable.border_style_red);
        mFemaleImage.setImageDrawable(null);
        mFemaleImage.setBackgroundResource(R.drawable.border_style_gray);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            seXvalue = extras.getInt("sexId", 0);
            Constant.printMsg("seXvalue is otpactivitry::" + seXvalue);
            if (seXvalue == R.id.register_radioButtonMale) {
                mMale.setChecked(true);
                mMaleImage.setImageResource(R.drawable.tick_red);
                mMaleImage.setBackgroundResource(R.drawable.border_style_red);
                mFemaleImage.setImageDrawable(null);
                mFemaleImage.setBackgroundResource(R.drawable.border_style_gray);
                Constant.printMsg("fff seXvalue is otpactivitry::eeeale"
                        + seXvalue);
            } else if (seXvalue == R.id.register_radioButtonFemale) {
                mFemale.setChecked(true);
                mFemaleImage.setImageResource(R.drawable.tick_red);
                mFemaleImage.setBackgroundResource(R.drawable.border_style_red);
                mMaleImage.setImageDrawable(null);
                mMaleImage.setBackgroundResource(R.drawable.border_style_gray);
                Constant.printMsg("fff seXvalue is otpactivitry::eeeefem"
                        + seXvalue);
            } else {
                Constant.printMsg("eeee seXvalue is otpactivitry::eeeefem"
                        + seXvalue);
            }
        }
       /*set sharedPref data in respective fields when app minimized and maximised*/
        if (prefOtp.isRegistered())
            getRegistrationData();
        preference = getSharedPreferences(
                NiftyApplication.getPereference_label(), Activity.MODE_PRIVATE);
//		options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ic_empty)
//				.showImageForEmptyUri(R.drawable.ic_empty)
//				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
//				.cacheOnDisk(true).considerExifParams(true).build();
        ImageLoader.getInstance()
                .init(ImageLoaderConfiguration
                        .createDefault(getApplicationContext()));
        Constant.printMsg("profile img     " + Constant.profileimg);
        if (Constant.profileimg != null) {
//			 ImageLoader.getInstance().displayImage(
//					 String.valueOf(Constant.profileimg).replaceAll(" ", "%20"),
//					 mProfileImage, options, animateFirstListener);
            new LoadProfileImage(mProfileImage).execute(Constant.profileimg);

        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spannedResult = Html.fromHtml("I agree to the "
                    + "<font color=#ff0000><a href='http://www.google.com'>Terms of Service and Privacy Policy</a></font>",Html.FROM_HTML_MODE_LEGACY);
        } else {
            spannedResult = Html.fromHtml("I agree to the "
                    + "<font color=#ff0000><a href='http://www.google.com'>Terms of Service and Privacy Policy</a></font>");
        }
        tabletSize = getResources().getBoolean(R.bool.isTablet);
        mCheckCondition1.setText(spannedResult);
        mCheckCondition1.setClickable(true);
        mCheckCondition1.setMovementMethod(LinkMovementMethod.getInstance());

        mCheckCondition2.setText(Html.fromHtml("Sync Contacts To Server"));
        mCheckCondition2.setClickable(true);
        mCheckCondition2.setMovementMethod(LinkMovementMethod.getInstance());

        country_list = new ArrayList<CountryCodeGetSet>();
        country_list = getCountry();
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        // maxYear = year - 1000;
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        regId = getIntent().getStringExtra("regId");
        Constant.Imei_no = getIMEINo();

        if (Constant.mFromVerfication == true) {
            Constant.mFromVerfication = false;
            mFirstName.setText(Constant.mFirstNameText);
            mLastName.setText(Constant.mSecondNameText);
            mDateOfBirth.setText(Constant.mDateOfBirth);
            mPassword.setText(Constant.mPassword);
            mReEnterPassword.setText(Constant.mRepassword);
            isAgreeCondition = true;
            mCheckImage1.setImageResource(R.drawable.tick_red);
            mCheckImage1.setBackgroundResource(R.drawable.border_style_red);
            isAgreeSync = true;
            mCheckImage2.setImageResource(R.drawable.tick_red);
            mCheckImage2.setBackgroundResource(R.drawable.border_style_red);
            if (Constant.mProfileImage != null) {
                mProfileImage.setImageBitmap(Constant.mProfileImage);
            }
        }
        if (Connectivity.isConnected(RegisterActivity.this)) {
            if (TextUtils.isEmpty(regId)) {
                regId = registerGCM();
                Constant.device_id = regId;
                Log.d("RegisterActivity", "GCM RegId: " + regId + " res"
                        + Constant.device_id);
            } else {
                Constant.printMsg("Already Registered with GCM Server!");
            }
        } else {
            Toast.makeText(RegisterActivity.this,
                    "Please check your network connection", Toast.LENGTH_SHORT)
                    .show();
        }
        if (Constant.manualmail == null) {
            Constant.printMsg("name ::::::>>>>>>>=== " + Constant.profilemail);
            if (Constant.profilemail!=null){
                mEmailIdText.setText(Constant.profilemail);
            }
        } else {
            mEmailIdText.setText(Constant.manualmail);
        }

//		TelephonyManager telephonyManager = (TelephonyManager) this
//				.getSystemService(Context.TELEPHONY_SERVICE);
//		strMobileNumber = telephonyManager.getLine1Number();
//
//		phoneUtil = PhoneNumberUtil.getInstance();
//		formatter = phoneUtil.getAsYouTypeFormatter(Locale.getDefault()
//				.getCountry());
//
    }

    private void nextToPersonalDetails() {
        if (mFirstName.getText().toString().length() > 0) {
            if (mLastName.getText().toString().length() > 0) {
                if (mDateOfBirth.getText().toString().length() > 0) {
                    mProfileImageMainLayout.setVisibility(View.GONE);
                    mPersonDetailMainLayout.setVisibility(View.GONE);
                    mPassswordMainLayout.setVisibility(View.VISIBLE);
                } else {
                    commonMethods.showAlertDialog(this, "Please Choose Your Date of birth", true);
                }
            } else {
                commonMethods.showAlertDialog(this, "Please Enter Last Name", true);
            }
        } else {
            commonMethods.showAlertDialog(this, "Please Enter First Name", true);
        }
    }

    private void nextSubmitProcess() {
        if (mPassword.getText().toString().length() > 0) {
            if (mReEnterPassword.getText().toString().length() > 0) {
                if (mPassword.getText().toString().length() >= 6) {
                    Editor ed = preference.edit();
                    ed.remove("sec_count");
                    ed.commit();
                    if (Validation.isPassword(mPassword, true)) {
                        if (mPassword.getText().toString().equals(
                                mReEnterPassword.getText().toString())) {
                            if (isAgreeCondition) {
                                if (isAgreeSync) {
                            /*only enter here when all the validation true*/
                                    finalSubmissionProcess();
                                } else {
                                    commonMethods.showAlertDialog(this, "Please agree to sync your contacts", true);
                                }
                            } else {
                                commonMethods.showAlertDialog(this, "Please agree the terms and conditions", true);
                            }
                        } else {
                            commonMethods.showAlertDialog(this, "Password Mismatch", true);
                        }
                    } else {
                        commonMethods.showAlertDialog(this, "Password should be alphanumeric", true);
                    }
                } else {
                    commonMethods.showAlertDialog(this, "Password should have atleast 6 character", true);
                }
            } else {
                commonMethods.showAlertDialog(this, "Please Enter your Repassword", true);
            }
        } else {
            commonMethods.showAlertDialog(this, "Please Enter your password", true);
        }
    }

    private void finalSubmissionProcess() {
        Constant.mFirstName = mFirstName.getText().toString().trim();
        int selectedId = mGenderRadioGroup.getCheckedRadioButtonId();
        mRadioSexButton = (RadioButton) findViewById(selectedId);
        Constant.printMsg("date::" + mDateOfBirth.getText().toString() + "  " + mRadioSexButton.getText().toString());
        Constant.pass = mPassword.getText().toString().trim();
        Constant.dob = mDateOfBirth.getText().toString().trim();
        Constant.first = Constant.mFirstName + "  " + mLastName.getText().toString().trim();
        Constant.gender = mRadioSexButton.getText().toString();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor people = getContentResolver().query(uri, projection, null, null, null);
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
                    Constant.printMsg("conatct list:::" + name + number);
                }
                // Do work...
            } while (people.moveToNext());
        }
        Constant.contactlist.addAll(conlist);
        Constant.mFirstNameText = mFirstName.getText()
                .toString().trim();
        Constant.mSecondNameText = mLastName
                .getText().toString().trim();
        Constant.mPassword = mPassword.getText()
                .toString().trim();
        Constant.mRepassword = mReEnterPassword.getText()
                .toString().trim();
        Constant.mDateOfBirth = mDateOfBirth.getText()
                .toString().trim();
        Constant.mGender = mRadioSexButton
                .getText().toString();
        Constant.printMsg("gendwer:::::    "
                + Constant.mGender);
        // Clearing saved register_activity details
        // in shared preference
        prefOtp.clearRegistrationDetails();
        Intent intent = new Intent(RegisterActivity.this,
                VerificationActivity.class);
        intent.putExtra("sexId", mGenderRadioGroup.getCheckedRadioButtonId());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        prefOtp.setRegistered(true);
        setRegistrationData();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        prefOtp.setRegistered(false);
        super.onResume();
    }

    /**
     * Saving the register_activity data in SharedPreference
     */
    public void setRegistrationData() {
        String frstName = null, lstName = null, dateOfBirth = null;
        boolean isTerm = false, isContact = false, isGender = false;
        if (Validation.hasText(mFirstName)) {
            frstName = mFirstName.getText().toString();
        }
        if (!Validation.hasText(mLastName)) {
            lstName = mLastName.getText().toString();
        }
        if (Validation.hasText1(mDateOfBirth)) {
            dateOfBirth = mDateOfBirth.getText().toString();
        }
        if (isAgreeCondition) {
            isTerm = true;
        }
        if (isAgreeSync) {
            isContact = true;
        }
        if (mFemale.isChecked()) {
            isGender = true;
        }
        prefOtp.setRegistrationData(frstName, lstName, dateOfBirth, isTerm,
                isContact, isGender);
    }

    /**
     * Get Registration values from shared preference
     */
    public void getRegistrationData() {
        HashMap<String, String> map = prefOtp.getAll_Details();
        mFirstName.setText(map.get(OtpSharedPreference.KEY_FIRST_NAME_GET));
        mLastName.setText(map.get(OtpSharedPreference.KEY_LAST_NAME_GET));
        mDateOfBirth.setText(map.get(OtpSharedPreference.KEY_DATE_GET));
        if (map.get(OtpSharedPreference.KEY_TERMS_GET).equalsIgnoreCase("true")) {
            isAgreeCondition = true;
            mCheckImage1.setImageResource(R.drawable.tick_red);
            mCheckImage1.setBackgroundResource(R.drawable.border_style_red);
        }
        if (map.get(OtpSharedPreference.KEY_CONTACT_GET).equalsIgnoreCase(
                "true")) {
            isAgreeSync = true;
            mCheckImage2.setImageResource(R.drawable.tick_red);
            mCheckImage2.setBackgroundResource(R.drawable.border_style_red);
        }
        if (map.get(OtpSharedPreference.KEY_GENDER_SET)
                .equalsIgnoreCase("true")) {
            mFemale.setChecked(true);
            mFemaleImage.setImageResource(R.drawable.tick_red);
            mFemaleImage.setBackgroundResource(R.drawable.border_style_red);
            mMaleImage.setImageDrawable(null);
            mMaleImage.setBackgroundResource(R.drawable.border_style_gray);
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
            country.setName(parser.getValue(e, "name"));
            country.setCountry_Code(parser.getValue(e, "code"));
            list.add(country);

        }

        return list;
    }

    private void chooseImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
//					Intent cameraIntent = new Intent(
//							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//					startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    dialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = Utils.getOutputMediaFileUri(1);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set
                    startActivityForResult(intent,
                            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                } else if (options[item].equals("Choose from Gallery")) {
//					Intent pickPhoto = new Intent(
//							Intent.ACTION_PICK,
//							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//					startActivityForResult(pickPhoto, GALLERY_REQUEST);
                    dialog.dismiss();
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    try {
                        startActivityForResult(photoPickerIntent, 1);
                    } catch (ActivityNotFoundException e) {
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    View.OnTouchListener customPopUpTouchListenr = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            Log.d("POPUP", "Touch false");
            return false;
        }

    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_FROMID:
                Constant.printMsg("onCreateDialog  : " + id);
                /** set date picker as current date */
                DatePickerDialog dialog = new DatePickerDialog(this,
                        datePickerListener, year, month, day);
                cal.set(year - 12, month, day);
                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                // dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                Constant.printMsg("yetr>>>>>" + year);
                return dialog;

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            mDateOfBirth.setText("  "
                    + new StringBuilder().append(month + 1).append("-")
                    .append(day).append("-").append(year)
                    .append(" "));


        }
    };

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
            bmImage.setBackgroundResource(0);
            bmImage.setImageBitmap(result);

            bmImage.setDrawingCacheEnabled(true);
            Bitmap scaledBitmap = bmImage.getDrawingCache();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
            byte[] byteArray = stream.toByteArray();

            Constant.byteimage = byteArray;
        }
    }

    private String getIMEINo() {
        String tempString = "";
        try {
            TelephonyManager telemgr = (TelephonyManager) getApplicationContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            tempString = telemgr.getDeviceId();
            // .trim() == null ? "" : telemgr.getDeviceId().trim();
        } catch (Exception e) {
            // tempString = "EX2";
        }
        Constant.printMsg("Imei number is :::::::" + tempString);
        Constant.Imei_no = tempString;
        return tempString;

    }

    /*
     * To Register with google cloud added on 10-09-2014 by prabhakaran
     */
    public String registerGCM() {
        // preloaderStart();
        // progressDialog.show();
        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId();
        if (TextUtils.isEmpty(regId)) {
            registerInBackground();
            Log.d("RegisterActivity",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + regId);

        } else {
            // preloaderStop();

        }
        return regId;
    }

    private String getRegistrationId() {
        final SharedPreferences prefs = getSharedPreferences(
                RegisterActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("ECPL", "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(this);
        if (registeredVersion != currentVersion) {
            Log.i("ECPL", "App version changed.");
            return "";
        }
        return registrationId;
    }

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

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regId = gcm.register(Constant.GOOGLE_PROJECT_ID);
                    Log.d("RegisterActivity", "registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, register_activity ID = " + regId;

                    Constant.printMsg("inside posting" + msg);

                    Constant.device_id = regId;
                    // stored the register_activity ID in shared preferences
                    storeRegistrationId(getApplicationContext(), regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {

                google_reg(msg);
                // progressDialog.dismiss();
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

        }.execute(null, null, null);
    }

    public void google_reg(String msg) {
        Constant.printMsg("GCM Register id is ::::::::::" + msg);

        if (msg.contains("SERVICE_NOT_AVAILABLE")) {
            Constant.device_id = "";
            registerGCM();
        } else {
            Constant.printMsg("GCM Registration Success ::::::::");
        }
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(
                RegisterActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Log.i("ECPL", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    // Constant.printMsg("filepath is : "+filePath);
                    cursor.close();

                    Intent intent = new Intent(this, CropImage.class);
                    intent.setType("image/*");
                    intent.putExtra(CropImage.IMAGE_PATH, filePath);
                    intent.putExtra("outputX", 256);
                    intent.putExtra("outputY", 256);
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("scale", true);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);

                }
                break;
            case REQUEST_CODE_CROP_IMAGE:
                if (resultCode == RESULT_OK) {

                    final Bundle extras = imageReturnedIntent.getExtras();

                    if (extras != null) {
                        Bitmap unscaledBitmap = extras.getParcelable("data");

                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        unscaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                                out);

                        Log.d(TAG, "Unscalled Bitmap::" + (out.size() / 1024));
                    /*
					 * if (!(unscaledBitmap.getWidth() <= 480 &&
					 * unscaledBitmap.getHeight() <= 800)) { // Part 2: Scale
					 * image Bitmap scaledBitmap =
					 * ScalingUtilities.createScaledBitmap(unscaledBitmap, 480,
					 * 800); scaledBitmap.compress(Bitmap.CompressFormat.JPEG,
					 * 50,out); scaledBitmap.recycle(); }
					 */
                        int quality = 100;
                        while ((out.size() / 1024) > 25) {
                            out = new ByteArrayOutputStream();
                            unscaledBitmap.compress(CompressFormat.JPEG, quality,
                                    out);
                            quality = quality - 5;
                            Log.d(TAG, "Profile picture Image Size::"
                                    + (out.size()) / 1024);
                        }

                        Log.d(TAG, "Scalled Bitmap::" + (out.size() / 1024));
                        img_byte = out.toByteArray();
                        Constant.byteimage = img_byte;
                        mProfileImage.setImageBitmap(unscaledBitmap);
                        Constant.mProfileImage = unscaledBitmap;
                        // new MyAsync_save().execute();
                    }
                }
                break;

            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                Constant.printMsg("calledd shot::::::::>>>>>>>>>");
                if (resultCode == RESULT_OK) {
                    // Image captured and saved to fileUri specified in the Intent
				/*
				 * Toast.makeText(this, "Image saved to:\n" + fileUri.getPath(),
				 * Toast.LENGTH_LONG).show();
				 */
                    Constant.printMsg("calledd shot::::::::>>>>>>>>>111");
                    File file = new File(fileUri.getPath());
                    if (file.length() > 26214400) {
                        commonMethods.showAlertDialog(this, getResources()
                                        .getString(R.string.imagesize_must_be_smaller),
                                true);
                    } else {
                        Intent intent = new Intent(this, CropImage.class);
                        intent.setType("image/*");
                        intent.putExtra(CropImage.IMAGE_PATH, fileUri.getPath());
                        intent.putExtra("outputX", 256);
                        intent.putExtra("outputY", 256);
                        intent.putExtra("aspectX", 1);
                        intent.putExtra("aspectY", 1);
                        intent.putExtra("scale", true);
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
                        uploadFile(fileUri.getPath(), true);
                    }
                }
                break;
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(String filepath,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filepath, options);
    }

    public static int calculateInSampleSize(

            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
                null, null, null);
        try {
            if (cursor.moveToFirst()) {

                return cursor.getInt(0);
            } else {
                return -1;
            }
        } finally {
            cursor.close();
        }
    }

    public void uploadFile(String strURL, boolean is_image) {
        Constant.printMsg("calledd shot::::::::>>>>>>>>>222");
        String time = "" + System.currentTimeMillis();
        String File_name = time + ".jpg";
        String file_path = "";
        int media_duration = 0;
        int size = 0;
        byte[] thumb = null;

        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        if (is_image) {
            Constant.printMsg("calledd shot::::::::>>>>>>>>>333");
            try {
                byte[] byteArray = null;
                ByteArrayOutputStream outstream = new ByteArrayOutputStream();

                Bitmap thumb_bitmap = CommonMethods.decodeFile(strURL, 100,
                        100);
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                        outstream);

                ByteArrayOutputStream outstream_thumb1 = new ByteArrayOutputStream();
                try {

                    int quality = 100;

                    // set for landscape photo

                    tabletSize = getResources().getBoolean(R.bool.isTablet);
                    Constant.printMsg("test vino tabletSize4: otp activity:"
                            + tabletSize);
                    if (tabletSize) {
                        ExifInterface exife = new ExifInterface(strURL);

                        Constant
                                .prtMsg("test vino image:::otp activity:::shot444444444444::"
                                        + exife.getAttribute(ExifInterface.TAG_ORIENTATION));
                        Log.d("EXIF value", exife
                                .getAttribute(ExifInterface.TAG_ORIENTATION));
                        if (exife.getAttribute(ExifInterface.TAG_ORIENTATION)
                                .equalsIgnoreCase("6")) {
                            thumb_bitmap = rotate(thumb_bitmap, 90);
                        } else if (exife.getAttribute(
                                ExifInterface.TAG_ORIENTATION)
                                .equalsIgnoreCase("8")) {
                            thumb_bitmap = rotate(thumb_bitmap, 270);
                        } else if (exife.getAttribute(
                                ExifInterface.TAG_ORIENTATION)
                                .equalsIgnoreCase("3")) {
                            thumb_bitmap = rotate(thumb_bitmap, 180);
                        } else if (exife.getAttribute(
                                ExifInterface.TAG_ORIENTATION)
                                .equalsIgnoreCase("0")) {
                            thumb_bitmap = rotate(thumb_bitmap, 90);
                        }
                    }

                    thumb_bitmap.compress(CompressFormat.JPEG, quality,
                            outstream_thumb1);

                    while ((outstream_thumb1.size() / 1024) > 180) {
                        outstream_thumb1 = new ByteArrayOutputStream();
                        thumb_bitmap.compress(CompressFormat.JPEG, quality,
                                outstream_thumb1);
                        quality = quality - 5;
                        Log.d(TAG,
                                "Map Image Size::" + (outstream_thumb1.size())
                                        / 1024);
                    }

                } catch (Exception e) {
                    // ACRA.getErrorReporter().handleException(e);
                    Log.w("TAG", "Error saving image file: " + e.getMessage());

                }

                Log.d(TAG, "Map Image Size::" + (outstream_thumb1.size())
                        / 1024);

                img_byte = outstream_thumb1.toByteArray();
                Constant.byteimage = img_byte;
                mProfileImage.setImageBitmap(thumb_bitmap);

                mProfileImage.setImageBitmap(new AvatarManager()
                        .roundCornerImage(thumb_bitmap, 180));
                // img_profile.setImageBitmap(thumb_bitmap);
                // img_profile.setImageBitmap(new AvatarManager()
                // .roundCornerImage(thumb_bitmap, 180));
                Constant.printMsg("calledd shot::::::::>>>>>>>>>444"
                        + thumb_bitmap);

                // new MyAsync_save().execute();
                // Toast.makeText(context, "Downloading Completed",
                // Toast.LENGTH_SHORT).show();
            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
            }

        } else {
            file_path = strURL;

            Bitmap video_thumb = ThumbnailUtils.createVideoThumbnail(file_path,
                    MediaStore.Images.Thumbnails.MINI_KIND);

            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
            video_thumb.compress(Bitmap.CompressFormat.JPEG, 50, outstream);

            // thumb = outstream.toByteArray();

            ByteArrayOutputStream outstream_thumb = new ByteArrayOutputStream();
            try {

                int quality = 100;
                video_thumb.compress(CompressFormat.JPEG, quality,
                        outstream_thumb);

                while ((outstream_thumb.size() / 1024) > 180) {
                    outstream_thumb = new ByteArrayOutputStream();
                    video_thumb.compress(CompressFormat.JPEG, quality,
                            outstream_thumb);
                    quality = quality - 5;
                    Log.d(TAG, "Map Image Size::" + (outstream_thumb.size())
                            / 1024);
                }

            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                Log.w("TAG", "Error saving image file: " + e.getMessage());

            }

            Log.d(TAG, "Map Image Size::" + (outstream_thumb.size()) / 1024);

            img_byte = outstream_thumb.toByteArray();
            // img_profile.setImageBitmap(bitmap);
            Constant.byteimage = img_byte;
            mProfileImage.setImageBitmap(video_thumb);
            // new MyAsync_save().execute();
        }
    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        // mtx.postRotate(degree);
        mtx.setRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    private void hideSoftKeyBoardOnTabClicked(View v) {
        if (v != null && getApplicationContext() != null) {
            InputMethodManager imm = (InputMethodManager) getApplicationContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPassswordMainLayout.getVisibility() == View.VISIBLE) {
            mPassswordMainLayout.setVisibility(View.GONE);
            mPersonDetailMainLayout.setVisibility(View.VISIBLE);
            mProfileImageMainLayout.setVisibility(View.GONE);
        } else {
            if (mPersonDetailMainLayout.getVisibility() == View.VISIBLE) {
                mPassswordMainLayout.setVisibility(View.GONE);
                mPersonDetailMainLayout.setVisibility(View.GONE);
                mProfileImageMainLayout.setVisibility(View.VISIBLE);
            } else {
                Constant.isRegisteredBackPress = true;
                Intent ii = new Intent(RegisterActivity.this, Slideshow.class);
                startActivity(ii);
                finish();
            }
        }
    }

    public void screenArrangeRegistration() {
        // TODO Auto-generated method stub
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        LinearLayout.LayoutParams profileImgLayoutParams = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        profileImgLayoutParams.gravity = Gravity.CENTER;
        profileImgLayoutParams.topMargin = (int) (height * 7 / 100);
        mProfileImageLayout.setLayoutParams(profileImgLayoutParams);

        FrameLayout.LayoutParams profileImgParams = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        profileImgParams.width = width * 35 / 100;
        profileImgParams.height = width * 35 / 100;
        profileImgParams.gravity = Gravity.CENTER;
        mProfileImage.setLayoutParams(profileImgParams);

        FrameLayout.LayoutParams cameraImgLayoutParams = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cameraImgLayoutParams.width = width * 12 / 100;
        cameraImgLayoutParams.height = width * 12 / 100;
//		cameraImgLayoutParams.leftMargin=width*11/100;
        cameraImgLayoutParams.topMargin = height * 15 / 100;
        cameraImgLayoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        mCameraImage.setLayoutParams(cameraImgLayoutParams);

        LinearLayout.LayoutParams emailLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        emailLayoutParams.width = width * 89 / 100;
        emailLayoutParams.topMargin = height * 2 / 100;
        emailLayoutParams.gravity = Gravity.CENTER;
        mEmailLayout.setLayoutParams(emailLayoutParams);

        LinearLayout.LayoutParams welcomeTextLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        welcomeTextLayoutParams.gravity = Gravity.CENTER;
        welcomeTextLayoutParams.topMargin = height * 1 / 100;
        mWelcomeText.setLayoutParams(welcomeTextLayoutParams);
        mWelcomeText.setGravity(Gravity.CENTER);
//		mWelcomeText.setPadding(0,width*1/100,0,0);

        LinearLayout.LayoutParams emailTextLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //emailTextLayoutParams.width = width * 70 / 100;
        emailTextLayoutParams.gravity = Gravity.CENTER;
//		emailTextLayoutParams.topMargin=(int)(height*0.5/100);
        emailTextLayoutParams.bottomMargin = height * 1 / 100;
        mEmailIdText.setLayoutParams(emailTextLayoutParams);
        mEmailIdText.setGravity(Gravity.CENTER);
//		mEmailIdText.setPadding(0,0,0, width * 1 / 100);

        LinearLayout.LayoutParams firstNameParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        firstNameParama.width = width * 85 / 100;
        firstNameParama.height = height * 7 / 100;
        firstNameParama.topMargin = height * 7 / 100;
        firstNameParama.gravity = Gravity.CENTER;
        mFirstName.setLayoutParams(firstNameParama);
        mFirstName.setGravity(Gravity.LEFT | Gravity.CENTER);
        mFirstName.setPadding(width * 2 / 100, 0, 0, 0);

        LinearLayout.LayoutParams lastNameParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lastNameParama.width = width * 85 / 100;
        lastNameParama.height = height * 7 / 100;
        lastNameParama.topMargin = height * 1 / 100;
        lastNameParama.gravity = Gravity.CENTER;
        mLastName.setLayoutParams(lastNameParama);
        mLastName.setGravity(Gravity.LEFT | Gravity.CENTER);
        mLastName.setPadding(width * 2 / 100, 0, 0, 0);

        LinearLayout.LayoutParams radioGroupParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        radioGroupParama.width = width * 85 / 100;
        radioGroupParama.height = height * 7 / 100;
        radioGroupParama.topMargin = height * 1 / 100;
        radioGroupParama.gravity = Gravity.CENTER;
        mGenderRadioGroup.setLayoutParams(radioGroupParama);
        mGenderRadioGroup.setGravity(Gravity.LEFT);
        mGenderLayout.setLayoutParams(radioGroupParama);

        LinearLayout.LayoutParams maleImageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        maleImageParama.width = height * 11 / 2 / 100;
        maleImageParama.height = height * 11 / 2 / 100;
        maleImageParama.gravity = Gravity.LEFT | Gravity.CENTER;
        mMaleImage.setLayoutParams(maleImageParama);
        mFemaleImage.setLayoutParams(maleImageParama);
        mMaleImage.setPadding(width*5/2/100,width*5/2/100,width*5/2/100,width*5/2/100);
        mFemaleImage.setPadding(width*5/2/100,width*5/2/100,width*5/2/100,width*5/2/100);

//		LinearLayout.LayoutParams femaleImageParama = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		femaleImageParama.width = height * 11/2 / 100;
//		femaleImageParama.height = height * 11/2 / 100;
//		femaleImageParama.gravity = Gravity.LEFT|Gravity.CENTER;
//		femaleImageParama.leftMargin=width*1/100;

        LinearLayout.LayoutParams maleTextParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //maleTextParama.width = width * 89/2 / 100;
        maleTextParama.height = height * 7 / 100;
        maleTextParama.gravity = Gravity.LEFT;
        maleTextParama.rightMargin = width * 4 / 100;
        maleTextParama.leftMargin = width * 4 / 100;
        mMaleText.setLayoutParams(maleTextParama);
        mFemaleText.setLayoutParams(maleTextParama);
        mMaleText.setGravity(Gravity.LEFT | Gravity.CENTER);
        mFemaleText.setGravity(Gravity.LEFT | Gravity.CENTER);

//		LinearLayout.LayoutParams maleRadioButtonarama = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		maleRadioButtonarama.width = width * 89/2 / 100;
//		maleRadioButtonarama.height = height * 7 / 100;
//		maleRadioButtonarama.gravity = Gravity.LEFT;
//		mMale.setLayoutParams(maleRadioButtonarama);
//		mMale.setGravity(Gravity.LEFT | Gravity.CENTER);
//		mMale.setScaleX((int)(0.5));
//		mMale.setScaleY((int)(0.5));
//
//		LinearLayout.LayoutParams femaleRadioButtonarama = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		femaleRadioButtonarama.width = width * 89/2 / 100;
//		femaleRadioButtonarama.height = height * 7 / 100;
//		femaleRadioButtonarama.gravity = Gravity.LEFT;
//		femaleRadioButtonarama.leftMargin=width*8/100;
//		mFemale.setLayoutParams(femaleRadioButtonarama);
//		mFemale.setGravity(Gravity.LEFT | Gravity.CENTER);
//		mFemale.setPadding(width * 2 / 100, 0, 0, 0);
//

        LinearLayout.LayoutParams dobParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dobParama.width = width * 85 / 100;
        dobParama.height = height * 7 / 100;
        dobParama.topMargin = height * 3 / 2 / 100;
        dobParama.gravity = Gravity.CENTER;
        mDateOfBirth.setLayoutParams(dobParama);
        mDateOfBirth.setGravity(Gravity.LEFT | Gravity.CENTER);
        mDateOfBirth.setPadding(width * 2 / 100, 0, width * 3 / 100, 0);
        Drawable icon = getResources().getDrawable(R.drawable.calender);
        icon.setBounds(0, 0, (int) (icon.getIntrinsicWidth() * 0.7),
                (int) (icon.getIntrinsicHeight() * 0.7));
        // ScaleDrawable sd = new ScaleDrawable(icon, 0, 0, 0);
        mDateOfBirth.setCompoundDrawables(null, null, icon, null);

        LinearLayout.LayoutParams passwordParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        passwordParama.width = width * 85 / 100;
        passwordParama.height = height * 7 / 100;
        passwordParama.topMargin = height * 7 / 100;
        passwordParama.gravity = Gravity.CENTER;
        mPassword.setLayoutParams(passwordParama);
        mPassword.setGravity(Gravity.LEFT | Gravity.CENTER);
        mPassword.setPadding(width * 2 / 100, 0, 0, 0);

        LinearLayout.LayoutParams reEnterPasswordParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        reEnterPasswordParama.width = width * 85 / 100;
        reEnterPasswordParama.height = height * 7 / 100;
        reEnterPasswordParama.topMargin = height * 1 / 100;
        reEnterPasswordParama.gravity = Gravity.CENTER;
        mReEnterPassword.setLayoutParams(reEnterPasswordParama);
        mReEnterPassword.setGravity(Gravity.LEFT | Gravity.CENTER);
        mReEnterPassword.setPadding(width * 2 / 100, 0, 0, 0);

        LinearLayout.LayoutParams chekboxLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        chekboxLayoutParama.width = width * 85 / 100;
        chekboxLayoutParama.height = height * 7 / 100;
        chekboxLayoutParama.topMargin = height * 1 / 100;
        chekboxLayoutParama.gravity = Gravity.CENTER;
        mcheckLayout1.setLayoutParams(chekboxLayoutParama);
        mcheckLayout2.setLayoutParams(chekboxLayoutParama);

        LinearLayout.LayoutParams chekboxImageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        chekboxImageParama.width = height * 11 / 2 / 100;
        chekboxImageParama.height = height * 11 / 2 / 100;
        chekboxImageParama.gravity = Gravity.CENTER | Gravity.LEFT;
        mCheckImage1.setLayoutParams(chekboxImageParama);
        mCheckImage2.setLayoutParams(chekboxImageParama);
        mCheckImage1.setPadding(width*5/2/100,width*5/2/100,width*5/2/100,width*5/2/100);
        mCheckImage2.setPadding(width*5/2/100,width*5/2/100,width*5/2/100,width*5/2/100);

        LinearLayout.LayoutParams chekboxParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //chekboxParama.width = width * 89 / 100;
        chekboxParama.height = height * 7 / 100;
        chekboxParama.leftMargin = width * 1 / 100;
        chekboxParama.gravity = Gravity.CENTER | Gravity.LEFT;
        mCheckCondition1.setLayoutParams(chekboxParama);
        mCheckCondition2.setLayoutParams(chekboxParama);
        mCheckCondition1.setGravity(Gravity.CENTER | Gravity.LEFT);
        mCheckCondition2.setGravity(Gravity.CENTER | Gravity.LEFT);

        LinearLayout.LayoutParams submitParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        submitParama.width = width * 30 / 100;
        submitParama.height = height * 6 / 100;
        submitParama.gravity = Gravity.CENTER;
        submitParama.topMargin = height * 4 / 100;
        mNextLast.setLayoutParams(submitParama);
        mNextLast.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams nextBtnLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nextBtnLayoutParams.width = width * 30 / 100;
        nextBtnLayoutParams.height = height * 6 / 100;
        nextBtnLayoutParams.gravity = Gravity.CENTER;
        nextBtnLayoutParams.topMargin = height * 4 / 100;
        mNextToUserName.setLayoutParams(nextBtnLayoutParams);
        mNextToUserName.setGravity(Gravity.CENTER);


        LinearLayout.LayoutParams nextPassBtnLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nextPassBtnLayoutParams.width = width * 30 / 100;
        nextPassBtnLayoutParams.height = height * 6 / 100;
        nextPassBtnLayoutParams.gravity = Gravity.CENTER;
        nextPassBtnLayoutParams.topMargin = height * 4 / 100;
        mNextToPasswod.setLayoutParams(nextPassBtnLayoutParams);
        mNextToPasswod.setGravity(Gravity.CENTER);

        if (width >= 600) {
            mWelcomeText.setTextSize(18);
            mEmailIdText.setTextSize(18);
            mNextToUserName.setTextSize(18);
            mLastName.setTextSize(18);
            mFirstName.setTextSize(18);
            mPassword.setTextSize(18);
            mReEnterPassword.setTextSize(18);
            mMaleText.setTextSize(18);
            mFemaleText.setTextSize(18);
            mCheckCondition1.setTextSize(13);
            mCheckCondition2.setTextSize(13);
            mDateOfBirth.setTextSize(18);
        } else if (width > 501 && width < 600) {
            mWelcomeText.setTextSize(17);
            mEmailIdText.setTextSize(17);
            mNextToUserName.setTextSize(17);
            mLastName.setTextSize(17);
            mFirstName.setTextSize(17);
            mPassword.setTextSize(17);
            mReEnterPassword.setTextSize(17);
            mMaleText.setTextSize(17);
            mFemaleText.setTextSize(17);
            mCheckCondition1.setTextSize(12);
            mCheckCondition2.setTextSize(12);
            mDateOfBirth.setTextSize(17);
        } else if (width > 260 && width < 500) {
            mWelcomeText.setTextSize(16);
            mEmailIdText.setTextSize(16);
            mNextToUserName.setTextSize(16);
            mLastName.setTextSize(16);
            mFirstName.setTextSize(16);
            mPassword.setTextSize(16);
            mReEnterPassword.setTextSize(16);
            mMaleText.setTextSize(16);
            mFemaleText.setTextSize(16);
            mCheckCondition1.setTextSize(11);
            mCheckCondition2.setTextSize(11);
            mDateOfBirth.setTextSize(16);
        } else if (width <= 260) {
            mWelcomeText.setTextSize(15);
            mEmailIdText.setTextSize(15);
            mNextToUserName.setTextSize(15);
            mLastName.setTextSize(15);
            mFirstName.setTextSize(15);
            mPassword.setTextSize(15);
            mReEnterPassword.setTextSize(15);
            mMaleText.setTextSize(15);
            mFemaleText.setTextSize(15);
            mCheckCondition1.setTextSize(10);
            mCheckCondition2.setTextSize(10);
            mDateOfBirth.setTextSize(15);
        }
    }
}
