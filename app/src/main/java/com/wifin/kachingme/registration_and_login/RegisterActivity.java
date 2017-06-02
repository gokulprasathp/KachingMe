package com.wifin.kachingme.registration_and_login;

import android.Manifest;
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
import android.content.pm.PackageManager;
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
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
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
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.pojo.CountryCodeGetSet;
import com.wifin.kachingme.pojo.UserContactDto;
import com.wifin.kachingme.util.AvatarManager;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.KachingMeConfig;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author siva
 *  Created by Wifintech on 12-Sep-16.
 */
public class RegisterActivity extends Slideshow {

    public static final int REQUEST_CODE_CROP_IMAGE = 12;
    static final int DATE_DIALOG_FROMID = 1,
            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 99;
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
    boolean tabletSize = false;
    String TAG = RegisterActivity.class.getSimpleName();
    GoogleCloudMessaging gcm;
    SharedPreferences pref, preference;
    Calendar cal;
    ArrayList<CountryCodeGetSet> country_list;
    ArrayList<UserContactDto> conlist = new ArrayList<UserContactDto>();
    Spanned spannedResult;
    View.OnTouchListener customPopUpTouchListenr = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            Log.d("POPUP", "Touch false");
            return false;
        }

    };
    String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS
            , Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private Uri fileUri;
    private int year, month, day;
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

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        // mtx.postRotate(degree);
        mtx.setRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // saving the data when activity in onPause state
        ViewGroup vg = (ViewGroup) findViewById(R.id.register_mainLayout);
        View.inflate(this, R.layout.register_activity, vg);
        intializeRegistration();
        screenArrangeRegistration();
        sLoginImage.setVisibility(View.GONE);
        prefOtp = new OtpSharedPreference(this);
        commonMethods = new CommonMethods(RegisterActivity.this);
        sIndicator.setVisibility(View.GONE);
        sPager.setVisibility(View.GONE);
        mPersonDetailMainLayout.setVisibility(View.GONE);
        mPassswordMainLayout.setVisibility(View.GONE);
        setBackReturnData();
        if (Constant.isFbGplus) {
            setFbGplusData();
        }

        mFirstName.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z ]+")) {
                            return src;
                        }
                        return src;
                    }
                }
        });

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
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
                if (hasPermissions(this, PERMISSIONS)) {
                    chooseImage();
                } else {
                    showDialogOK("Access Media Permission required for this app",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            if (!hasPermissions(RegisterActivity.this, PERMISSIONS)) {
                                                ActivityCompat.requestPermissions(RegisterActivity.this, PERMISSIONS, 1);
                                            }
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            // proceed with logic by disabling the related features or quit the app.
                                            break;
                                    }
                                }
                            });
                }
                break;
            case R.id.register_nextToPersonDetails:
                if (hasPermissions(this, PERMISSIONS)) {
                    mProfileImageMainLayout.setVisibility(View.GONE);
                    mPersonDetailMainLayout.setVisibility(View.VISIBLE);
                    mPassswordMainLayout.setVisibility(View.GONE);
                } else {
                    showDialogOK("Contatcs Permission required for this app",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            if (!hasPermissions(RegisterActivity.this, PERMISSIONS)) {
                                                ActivityCompat.requestPermissions(RegisterActivity.this, PERMISSIONS, 1);
                                            }
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;
                                    }
                                }
                            });
                }
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
                if (!isAgreeCondition) {
                    isAgreeCondition = true;
                    mCheckImage1.setImageResource(R.drawable.tick_red);
                    mCheckImage1.setBackgroundResource(R.drawable.border_style_red);
                } else {
                    isAgreeCondition = false;
                    mCheckImage1.setImageDrawable(null);
                    mCheckImage1.setBackgroundResource(R.drawable.border_style_gray);
                }
                break;
            case R.id.register_checkImage2:
                if (!isAgreeSync) {
                    isAgreeSync = true;
                    mCheckImage2.setImageResource(R.drawable.tick_red);
                    mCheckImage2.setBackgroundResource(R.drawable.border_style_red);
                } else {
                    isAgreeSync = false;
                    mCheckImage2.setImageDrawable(null);
                    mCheckImage2.setBackgroundResource(R.drawable.border_style_gray);
                }
                break;
            case R.id.register_next:
                if (hasPermissions(this, PERMISSIONS))
                    nextSubmitProcess();
                else
                    Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Constant.printMsg("Registration Activity Permission status......" + requestCode + "...." + permissions + "....." + grantResults);
        switch (requestCode) {
            case 1: {
                Constant.printMsg("Registration Activity Permission status...switch case 1...");
                Map<String, Integer> perms = new HashMap<>();
//                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
//                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    Constant.printMsg("Registration Activity Check for both permission status....."
                            + perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) + "....." + PackageManager.PERMISSION_GRANTED
                            + perms.get(Manifest.permission.READ_CONTACTS) + "....." + perms.get(Manifest.permission.PROCESS_OUTGOING_CALLS));
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Constant.printMsg("Registration Activity Permission status permission granted");
                    } else {
                        Constant.printMsg("Registration Activity Some permissions are not granted ask again ");
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.PROCESS_OUTGOING_CALLS)||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ) {
                            showDialogOK("Contatcs Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    if (!hasPermissions(RegisterActivity.this, PERMISSIONS)) {
                                                        ActivityCompat.requestPermissions(RegisterActivity.this, PERMISSIONS, 1);
                                                    }
                                                    Constant.printMsg("Registration Activity Permission status.....BUTTON_POSITIVE......");
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    Constant.printMsg("Registration Activity Permission status.....BUTTON_NEGATIVE......");
                                                    break;
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Constant.printMsg("Registration Activity code empty......");
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void setFbGplusData() {
        String test = sharedPreferences.getString("facebookProfilePicture", null);
        if (test != null) {
            Bitmap bitmp = stringToBitMap(test);
            if (bitmp != null) {
                mProfileImage.setImageBitmap(bitmp);
                Constant.mProfileImage=bitmp;
            }
        }
        mFirstName.setText(Constant.mFirstName);
        mLastName.setText(Constant.mLastName);
        if (Constant.dob != null && !Constant.dob.isEmpty()) {
            //mDateOfBirth.setText("");
        }

        if (Constant.gender != null && !Constant.gender.isEmpty()) {
            if (Constant.gender.equalsIgnoreCase("male")) {
                mMale.setChecked(true);
                mMaleImage.setImageResource(R.drawable.tick_red);
                mMaleImage.setBackgroundResource(R.drawable.border_style_red);
                mFemaleImage.setImageDrawable(null);
                mFemaleImage.setBackgroundResource(R.drawable.border_style_gray);
            } else {
                if (Constant.gender.equalsIgnoreCase("female")) {
                    mFemale.setChecked(true);
                    mFemaleImage.setImageResource(R.drawable.tick_red);
                    mFemaleImage.setBackgroundResource(R.drawable.border_style_red);
                    mMaleImage.setImageDrawable(null);
                    mMaleImage.setBackgroundResource(R.drawable.border_style_gray);
                }
            }
        }
    }

    public Bitmap stringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
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
                KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
        ImageLoader.getInstance()
                .init(ImageLoaderConfiguration
                        .createDefault(getApplicationContext()));
        Constant.printMsg("profile img     " + Constant.profileimg);
        if (Constant.profileimg != null) {
            new LoadProfileImage(mProfileImage).execute(Constant.profileimg);

        }
        spannedResult = Html.fromHtml("I agree to the "
                + "<font color=#ff0000><a href='"+ KachingMeConfig.TERMS_OF_SERVICE+"'>Terms of Service</a></font>" + " and "
                + "<font color=#ff0000><a href='"+ KachingMeConfig.PRIVACY_POLICY+"'>Privacy Policy</a></font>");

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

        if (Constant.manualmail == null) {
            Constant.printMsg("name ::::::>>>>>>>=== " + Constant.profilemail);
            if (Constant.profilemail != null) {
                mEmailIdText.setText(Constant.profilemail);
            }
        } else {
            mEmailIdText.setText(Constant.manualmail);
        }
    }

    private void nextToPersonalDetails() {
        if (mFirstName.getText().toString().trim().length() > 0) {
            if (mLastName.getText().toString().trim().length() > 0) {
                if (mDateOfBirth.getText().toString().length() > 0) {
                    mProfileImageMainLayout.setVisibility(View.GONE);
                    mPersonDetailMainLayout.setVisibility(View.GONE);
                    mPassswordMainLayout.setVisibility(View.VISIBLE);
                } else {
                    commonMethods.showAlertDialog(this, "Date of birth cannot be empty", true);
                }
            } else {
                commonMethods.showAlertDialog(this, "Lastname cannot be empty", true);
            }
        } else {
            commonMethods.showAlertDialog(this, "Firstname cannot be empty", true);
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
                                    if (Connectivity.isConnected(this)) {
                                        finalSubmissionProcess();
                                    } else {
                                        Toast.makeText(this, "Please Check Your Network Connection.!!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    commonMethods.showAlertDialog(this, "Please agree to sync your contacts", true);
                                }
                            } else {
                                commonMethods.showAlertDialog(this, "Please agree the terms and conditions", true);
                            }
                        } else {
                            commonMethods.showAlertDialog(this, "Password mismatch", true);
                        }
                    } else {
                        commonMethods.showAlertDialog(this, "Password should be alphanumeric", true);
                    }
                } else {
                    commonMethods.showAlertDialog(this, "Password should have atleast 6 character", true);
                }
            } else {
                commonMethods.showAlertDialog(this, "Retype password cannot be empty", true);
            }
        } else {
            commonMethods.showAlertDialog(this, "Create password cannot be empty", true);
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
        Constant.addverification=false;
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
                    if (CropImage.croppedImage != null) {
                        try {
                            Bitmap unscaledBitmap = CropImage.croppedImage;
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            unscaledBitmap.compress(CompressFormat.JPEG, 100, out);
                            int quality = 100;
                            while ((out.size() / 1024) > 25) {
                                out = new ByteArrayOutputStream();
                                unscaledBitmap.compress(CompressFormat.JPEG, quality,out);
                                quality = quality - 5;
                                Log.d(TAG, "Profile picture Image Size::" + (out.size()) / 1024);
                            }
                            Log.d(TAG, "Scalled Bitmap::" + (out.size() / 1024));
                            img_byte = out.toByteArray();
                            Constant.byteimage = img_byte;
                            mProfileImage.setImageBitmap(unscaledBitmap);
                            Constant.mProfileImage = unscaledBitmap;
                            CropImage.croppedImage = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                Constant.printMsg("calledd shot::::::::>>>>>>>>>");
                if (resultCode == RESULT_OK) {
                    // Image captured and saved to fileUri specified in the Intent
                    Constant.printMsg("calledd shot::::::::>>>>>>>>>111");
                    File file = new File(fileUri.getPath());
                    if (file.length() > 26214400) {
                        commonMethods.showAlertDialog(this, getResources()
                                        .getString(R.string.imagesize_must_be_smaller), true);
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
                        // uploadFile(fileUri.getPath(), true);
                    }
                }
                break;
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
                thumb_bitmap.compress(CompressFormat.JPEG, 100,
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

                        Constant.printMsg("test vino image:::otp activity:::shot444444444444::"
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
            video_thumb.compress(CompressFormat.JPEG, 50, outstream);

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
            scaledBitmap.compress(CompressFormat.JPEG, 75, stream);
            byte[] byteArray = stream.toByteArray();

            Constant.byteimage = byteArray;
            Constant.bitmapImage = result;
        }
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
        profileImgLayoutParams.topMargin = (int) (height * 3 / 100);
        mProfileImageLayout.setLayoutParams(profileImgLayoutParams);

        FrameLayout.LayoutParams profileImgParams = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        profileImgParams.width = width * 33 / 100;
        profileImgParams.height = width * 33 / 100;
        profileImgParams.gravity = Gravity.CENTER;
        mProfileImage.setLayoutParams(profileImgParams);

        FrameLayout.LayoutParams cameraImgLayoutParams = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cameraImgLayoutParams.width = width * 11 / 100;
        cameraImgLayoutParams.height = width * 11 / 100;
//		cameraImgLayoutParams.leftMargin=width*11/100;
        cameraImgLayoutParams.topMargin = height * 12 / 100;
        cameraImgLayoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        mCameraImage.setLayoutParams(cameraImgLayoutParams);

        LinearLayout.LayoutParams emailLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        emailLayoutParams.width = width * 85 / 100;
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
        firstNameParama.width = width * 80 / 100;
        firstNameParama.height = (int) (height * 6.5 / 100);
        firstNameParama.topMargin = height * 7 / 100;
        firstNameParama.gravity = Gravity.CENTER;
        mFirstName.setLayoutParams(firstNameParama);
        mFirstName.setGravity(Gravity.LEFT | Gravity.CENTER);
        mFirstName.setPadding(width * 2 / 100, 0, 0, 0);

        LinearLayout.LayoutParams lastNameParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lastNameParama.width = width * 80 / 100;
        lastNameParama.height = (int) (height * 6.5 / 100);
        lastNameParama.topMargin = height * 1 / 100;
        lastNameParama.gravity = Gravity.CENTER;
        mLastName.setLayoutParams(lastNameParama);
        mLastName.setGravity(Gravity.LEFT | Gravity.CENTER);
        mLastName.setPadding(width * 2 / 100, 0, 0, 0);

        LinearLayout.LayoutParams radioGroupParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        radioGroupParama.width = width * 80 / 100;
        radioGroupParama.height = (int) (height * 6.5 / 100);
        radioGroupParama.topMargin = height * 1 / 100;
        radioGroupParama.gravity = Gravity.CENTER;
        mGenderRadioGroup.setLayoutParams(radioGroupParama);
        mGenderRadioGroup.setGravity(Gravity.LEFT);
        mGenderLayout.setLayoutParams(radioGroupParama);

        LinearLayout.LayoutParams maleImageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        maleImageParama.width = height * 5 / 100;
        maleImageParama.height = height * 5 / 100;
        maleImageParama.gravity = Gravity.LEFT | Gravity.CENTER;
        mMaleImage.setLayoutParams(maleImageParama);
        mFemaleImage.setLayoutParams(maleImageParama);
        mMaleImage.setPadding(width * 5 / 2 / 100, width * 5 / 2 / 100, width * 5 / 2 / 100, width * 5 / 2 / 100);
        mFemaleImage.setPadding(width * 5 / 2 / 100, width * 5 / 2 / 100, width * 5 / 2 / 100, width * 5 / 2 / 100);

        LinearLayout.LayoutParams maleTextParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //maleTextParama.width = width * 89/2 / 100;
        maleTextParama.height = (int) (height * 6.5 / 100);
        maleTextParama.gravity = Gravity.LEFT;
        maleTextParama.rightMargin = width * 4 / 100;
        maleTextParama.leftMargin = width * 4 / 100;
        mMaleText.setLayoutParams(maleTextParama);
        mFemaleText.setLayoutParams(maleTextParama);
        mMaleText.setGravity(Gravity.LEFT | Gravity.CENTER);
        mFemaleText.setGravity(Gravity.LEFT | Gravity.CENTER);

        LinearLayout.LayoutParams dobParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dobParama.width = width * 80 / 100;
        dobParama.height = (int) (height * 6.5 / 100);
        dobParama.topMargin = height * 3 / 2 / 100;
        dobParama.gravity = Gravity.CENTER;
        mDateOfBirth.setLayoutParams(dobParama);
        mDateOfBirth.setGravity(Gravity.LEFT | Gravity.CENTER);
        mDateOfBirth.setPadding(width * 2 / 100, 0, width * 3 / 100, 0);
        Drawable icon = getResources().getDrawable(R.drawable.calender);
        icon.setBounds(0, 0, (int) (icon.getIntrinsicWidth() * 0.25),
                (int) (icon.getIntrinsicHeight() * 0.25));
        // ScaleDrawable sd = new ScaleDrawable(icon, 0, 0, 0);
        mDateOfBirth.setCompoundDrawables(null, null, icon, null);

        LinearLayout.LayoutParams passwordParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        passwordParama.width = width * 80 / 100;
        passwordParama.height = (int) (height * 6.5 / 100);
        passwordParama.topMargin = height * 7 / 100;
        passwordParama.gravity = Gravity.CENTER;
        mPassword.setLayoutParams(passwordParama);
        mPassword.setGravity(Gravity.LEFT | Gravity.CENTER);
        mPassword.setPadding(width * 2 / 100, 0, 0, 0);
        mPassword.setLongClickable(false);

        LinearLayout.LayoutParams reEnterPasswordParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        reEnterPasswordParama.width = width * 80 / 100;
        reEnterPasswordParama.height = (int) (height * 6.5 / 100);
        reEnterPasswordParama.topMargin = height * 1 / 100;
        reEnterPasswordParama.gravity = Gravity.CENTER;
        mReEnterPassword.setLayoutParams(reEnterPasswordParama);
        mReEnterPassword.setGravity(Gravity.LEFT | Gravity.CENTER);
        mReEnterPassword.setPadding(width * 2 / 100, 0, 0, 0);
        mReEnterPassword.setLongClickable(false);

        LinearLayout.LayoutParams chekboxLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        chekboxLayoutParama.width = width * 80 / 100;
        chekboxLayoutParama.height = (int) (height * 6.5 / 100);
        chekboxLayoutParama.topMargin = height * 1 / 100;
        chekboxLayoutParama.gravity = Gravity.CENTER;
        mcheckLayout1.setLayoutParams(chekboxLayoutParama);
        mcheckLayout2.setLayoutParams(chekboxLayoutParama);

        LinearLayout.LayoutParams chekboxImageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        chekboxImageParama.width = height * 5 / 100;
        chekboxImageParama.height = height * 5 / 100;
        chekboxImageParama.gravity = Gravity.CENTER | Gravity.LEFT;
        mCheckImage1.setLayoutParams(chekboxImageParama);
        mCheckImage2.setLayoutParams(chekboxImageParama);
        mCheckImage1.setPadding(width * 5 / 2 / 100, width * 5 / 2 / 100, width * 5 / 2 / 100, width * 5 / 2 / 100);
        mCheckImage2.setPadding(width * 5 / 2 / 100, width * 5 / 2 / 100, width * 5 / 2 / 100, width * 5 / 2 / 100);

        LinearLayout.LayoutParams chekboxParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //chekboxParama.width = width * 89 / 100;
        chekboxParama.height = (int) (height * 6.5 / 100);
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
        submitParama.height = (int) (height * 5.8 / 100);
        submitParama.gravity = Gravity.CENTER;
        submitParama.topMargin = (int) (height * 3.5 / 100);
        mNextLast.setLayoutParams(submitParama);
        mNextLast.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams nextBtnLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nextBtnLayoutParams.width = width * 30 / 100;
        nextBtnLayoutParams.height = height * 6 / 100;
        nextBtnLayoutParams.gravity = Gravity.CENTER;
        nextBtnLayoutParams.topMargin = height * 3 / 100;
        mNextToUserName.setLayoutParams(submitParama);
        mNextToUserName.setGravity(Gravity.CENTER);


        LinearLayout.LayoutParams nextPassBtnLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nextPassBtnLayoutParams.width = width * 30 / 100;
        nextPassBtnLayoutParams.height = height * 6 / 100;
        nextPassBtnLayoutParams.gravity = Gravity.CENTER;
        nextPassBtnLayoutParams.topMargin = height * 3 / 100;
        mNextToPasswod.setLayoutParams(submitParama);
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
