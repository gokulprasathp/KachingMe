package com.wifin.kachingme.settings;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.SherlockBaseActivity;
import com.wifin.kachingme.async_tasks.ConcurrentAsyncTaskExecutor;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.ProfileRoundImg;

import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class UserProfile extends SherlockBaseActivity implements OnClickListener {

    //    Bitmap bmp;
    public static String TAG = UserProfile.class.getSimpleName();
    ImageView img_avatar;
    TextView txt_id, txt_name, txt_status, txt_phone, mStatusLabel, mNumberLabel, tvNameProfile, tvEmailProfile, nameLabelProfile, mailLabelProfile;
    ProgressDialog progressdialog;
    VCard vc = new VCard();
    byte img_byte[] = null;
    String jid;
    DatabaseHelper dbAdapter;
    String email, fb, twitter, in;
    TextView btn_email/* ,btn_fb,btn_twitter,btn_in */, btn_call;
    ContactsGetSet contact;
    FrameLayout mBlurImgLayout;
    LinearLayout mStatusLayout, mNumberLayout, mNameLayout, mEmailLayout;
    View mStatusSeperation, mNumberSeperation, mNameSeparator, mEmailSeparator;
    ImageView img_profile, img_profile_bg;
    BroadcastReceiver lastseen_event = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.PROFILE_UPDATE)) {
                ConcurrentAsyncTaskExecutor.executeConcurrently(new MyAsync());
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_2);
        initialization();
        screenArrangement();
        dbAdapter = KachingMeApplication.getDatabaseAdapter();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(
                getResources().getString(R.string.contact_info));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jid = bundle.getString("jid");

        }
        txt_phone.setText("+" + jid.toString().split("@")[0]);
        new File(Constant.local_profile_picture_dir).mkdirs();

        ConcurrentAsyncTaskExecutor.executeConcurrently(new MyAsync());
//        new MyAsync().execute();
        /* doBindService(); */
    }

    private void initialization() {
        img_avatar = (ImageView) findViewById(R.id.profile_pic);
        /* txt_id=(TextView)findViewById(R.id.txt_id); */
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_status = (TextView) findViewById(R.id.txt_status);
        txt_phone = (TextView) findViewById(R.id.txt_phone);
        btn_email = (TextView) findViewById(R.id.btn_email);
        btn_call = (TextView) findViewById(R.id.btn_call);
        mBlurImgLayout = (FrameLayout) findViewById(R.id.blur_img_layout);
        mStatusLayout = (LinearLayout) findViewById(R.id.status_layout);
        mNumberLayout = (LinearLayout) findViewById(R.id.number_layout);
        mStatusLabel = (TextView) findViewById(R.id.status_label);
        mNumberLabel = (TextView) findViewById(R.id.num_label);
        mStatusSeperation = (View) findViewById(R.id.status_seperation);
        mNumberSeperation = (View) findViewById(R.id.num_seperation);
        img_profile_bg = (ImageView) findViewById(R.id.profile_picBaground);
        mNameLayout = (LinearLayout) findViewById(R.id.name_layout_profile);
        mEmailLayout = (LinearLayout) findViewById(R.id.email_layout_profile);
        mNameSeparator = (View) findViewById(R.id.name_separator);
        mEmailSeparator = (View) findViewById(R.id.email_separator);
        tvNameProfile = (TextView) findViewById(R.id.tvNameProfile);
        tvEmailProfile = (TextView) findViewById(R.id.tvEmailProfile);
        nameLabelProfile = (TextView) findViewById(R.id.name_label_profile);
        mailLabelProfile = (TextView) findViewById(R.id.email_label_profile);

        btn_email.setOnClickListener(this);
        img_avatar.setOnClickListener(this);
        btn_call.setOnClickListener(this);

        Constant.typeFace(this, txt_name);
        Constant.typeFace(this, txt_status);
        Constant.typeFace(this, txt_phone);
        Constant.typeFace(this, btn_email);
        Constant.typeFace(this, btn_call);
        Constant.typeFace(this, mStatusLabel);
        Constant.typeFace(this, mNumberLabel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_email:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{email});
                i.putExtra(Intent.EXTRA_SUBJECT, "NiftyChat Email");
            /* i.putExtra(android.content.Intent.EXTRA_TEXT, text); */
                startActivity(Intent.createChooser(i, "Send email"));
                break;
            case R.id.profile_pic:
                Log.d(TAG, "Clicked on profile picture....");
                File f = new File(Constant.local_profile_picture_dir
                        + jid.split("@")[0] + ".jpg");

                // you can create a new file name "test.BMP" in sdcard folder.
                try {
                    if (contact.getPhoto_bitmap() != null) {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        contact.getPhoto_bitmap().compress(Bitmap.CompressFormat.PNG, 100, bytes);

                        f.createNewFile();
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    }
                } catch (Exception e) {
                    // ACRA.getErrorReporter().handleException(e);
                    // TODO: handle exception
                    e.printStackTrace();
                }
                if (f.exists()) {
                    try {
                        Intent intent = new Intent();
                        // intent.putExtra("path",f);
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(f), "image/jpg");
                        startActivity(intent);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
                break;
            case R.id.btn_call:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri
                        .parse("tel:+" + jid.toString().split("@")[0]));
                startActivity(callIntent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        registerReceiver(lastseen_event, new IntentFilter(
                Constant.PROFILE_UPDATE));
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        unregisterReceiver(lastseen_event);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public String searchname(String no) {
        // Constant.printMsg("Search no::"+no);
        String name = "";
        name = getContactDisplayNameByNumber(no);
        if (name.equals("?")) {
            name = getContactDisplayNameByNumber(no.substring(2, 11));
        }

        return name;

    }

    public String getContactDisplayNameByNumber(String number) {
        Uri uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String name = "?";

        ContentResolver contentResolver = getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, new String[]{
                        BaseColumns._ID, ContactsContract.PhoneLookup.DISPLAY_NAME},
                null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                name = contactLookup.getString(contactLookup
                        .getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                // String contactId =
                // contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
            }

        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }

        return name;
    }

    private Bitmap blurRenderScript(Bitmap smallBitmap, int radius) {

        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(this);

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;
    }

    private Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();

                break;
        }
        return true;
    }

    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void screenArrangement() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        FrameLayout.LayoutParams mAddBtnParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        mAddBtnParams.width = (int) width;
        mAddBtnParams.height = (int) height * 35 / 100;
//        mAddBtnParams.rightMargin = width * 1 / 100;
        mBlurImgLayout.setLayoutParams(mAddBtnParams);

        FrameLayout.LayoutParams mProfileImgParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        mProfileImgParams.width = (int) width * 40 / 100;
        mProfileImgParams.height = (int) width * 40 / 100;
        mProfileImgParams.leftMargin = (int) (width * 1 / 100);
        mProfileImgParams.topMargin = (int) (width * 0.9 / 100);
        img_avatar.setLayoutParams(mProfileImgParams);

        FrameLayout.LayoutParams mProfileImgBgParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        mProfileImgBgParams.width = (int) (width * 41.5 / 100);
        mProfileImgBgParams.height = (int) (width * 41.5 / 100);
//        mProfileImgBgParams.rightMargin = width * 1 / 100;
        img_profile_bg.setLayoutParams(mProfileImgBgParams);

//        FrameLayout.LayoutParams mAddBtnParams = new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.WRAP_CONTENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
//        mAddBtnParams.width = (int) width;
//        mAddBtnParams.height = (int) height * 35 / 100;
//        mAddBtnParams.rightMargin = width * 1 / 100;
//        mBlurImgLayout.setLayoutParams(mAddBtnParams);

//        FrameLayout.LayoutParams mProfileImgParams = new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.WRAP_CONTENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
//        mProfileImgParams.width = (int) width * 40 /100;
//        mProfileImgParams.height = (int) width * 40 / 100;
//        mProfileImgParams.rightMargin = width * 1 / 100;
//        img_avatar.setLayoutParams(mProfileImgParams);

        LinearLayout.LayoutParams mStatusLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mStatusLayoutParams.width = (int) width;
//        mStatusLayoutParams.height = (int) width * 25 / 100;
        mStatusLayoutParams.topMargin = width * 5 / 100;
        mStatusLayout.setLayoutParams(mStatusLayoutParams);
        mNumberLayout.setLayoutParams(mStatusLayoutParams);
        mNameLayout.setLayoutParams(mStatusLayoutParams);
        mEmailLayout.setLayoutParams(mStatusLayoutParams);

        LinearLayout.LayoutParams mEmailBtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mEmailBtnParams.width = (int) width * 10 / 100;
        mEmailBtnParams.height = (int) width * 10 / 100;
        mEmailBtnParams.topMargin = width * 5 / 100;
        mEmailBtnParams.leftMargin = width * 5 / 100;
        btn_email.setLayoutParams(mEmailBtnParams);

        LinearLayout.LayoutParams mStatusLabelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mStatusLabelParams.width = (int) width;
        mStatusLabelParams.height = (int) width * 12 / 100;
        mStatusLabel.setLayoutParams(mStatusLabelParams);
        mNumberLabel.setLayoutParams(mStatusLabelParams);
        mailLabelProfile.setLayoutParams(mStatusLabelParams);
        nameLabelProfile.setLayoutParams(mStatusLabelParams);

        mStatusLabel.setPadding(width * 5 / 100, width * 3 / 100, width * 5 / 100, width * 3 / 100);
        mNumberLabel.setPadding(width * 5 / 100, width * 3 / 100, width * 5 / 100, width * 3 / 100);
        nameLabelProfile.setPadding(width * 5 / 100, width * 3 / 100, width * 5 / 100, width * 3 / 100);
        mailLabelProfile.setPadding(width * 5 / 100, width * 3 / 100, width * 5 / 100, width * 3 / 100);

        LinearLayout.LayoutParams mNumberTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mNumberTextParams.width = width * 80 / 100;
        mNumberTextParams.leftMargin = (int) width * 5 / 100;
        mNumberTextParams.height = (int) width * 12 / 100;
        txt_phone.setLayoutParams(mNumberTextParams);
        txt_phone.setGravity(Gravity.CENTER | Gravity.LEFT);
        tvNameProfile.setLayoutParams(mNumberTextParams);
        tvNameProfile.setGravity(Gravity.CENTER | Gravity.LEFT);
        tvEmailProfile.setLayoutParams(mNumberTextParams);
        tvEmailProfile.setGravity(Gravity.CENTER | Gravity.LEFT);
        txt_status.setLayoutParams(mNumberTextParams);
        txt_status.setGravity(Gravity.CENTER | Gravity.LEFT);

//        txt_status.setPadding(width* 3 /100,width* 3 /100,width* 3 /100,width* 3 /100);
//        txt_phone.setPadding(width* 3 /100,width* 3 /100,width* 3 /100,width* 3 /100);

//        LinearLayout.LayoutParams mStatusTextParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        mStatusTextParams.width = width * 90 / 100;
//        mStatusTextParams.leftMargin = (int) width * 5 / 100;
////        mStatusTextParams.height = (int) width * 12 / 100;
//        txt_status.setLayoutParams(mStatusTextParams);
//        txt_status.setGravity(Gravity.CENTER|Gravity.LEFT);
//        txt_status.setPadding(width* 5 /100,width* 3 /100,width* 5 /100,width* 3 /100);

        LinearLayout.LayoutParams mStatusViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mStatusViewParams.width = (int) width * 90 / 100;
        mStatusViewParams.height = (int) (width * 0.3 / 100);
        mStatusViewParams.gravity = Gravity.CENTER;
        mStatusSeperation.setLayoutParams(mStatusViewParams);
        mNumberSeperation.setLayoutParams(mStatusViewParams);
        mNameSeparator.setLayoutParams(mStatusViewParams);
        mEmailSeparator.setLayoutParams(mStatusViewParams);

        LinearLayout.LayoutParams mCallImgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mCallImgParams.width = (int) width * 6 / 100;
        mCallImgParams.height = (int) (width * 6.0 / 100);
        mCallImgParams.gravity = Gravity.CENTER;
        mCallImgParams.leftMargin = width * 3 / 100;
        btn_call.setLayoutParams(mCallImgParams);

        if (width >= 600) {
            mStatusLabel.setTextSize(16);
            mNumberLabel.setTextSize(16);
            txt_status.setTextSize(16);
            txt_phone.setTextSize(16);
            tvNameProfile.setTextSize(16);
            tvEmailProfile.setTextSize(16);
        } else if (width < 600 && width >= 480) {
            mStatusLabel.setTextSize(15);
            mNumberLabel.setTextSize(15);
            txt_status.setTextSize(15);
            txt_phone.setTextSize(15);
            tvNameProfile.setTextSize(15);
            tvEmailProfile.setTextSize(15);
        } else if (width < 480 && width >= 320) {
            mStatusLabel.setTextSize(12);
            mNumberLabel.setTextSize(12);
            txt_status.setTextSize(12);
            txt_phone.setTextSize(12);
            tvNameProfile.setTextSize(12);
            tvEmailProfile.setTextSize(12);
        } else if (width < 320) {
            mStatusLabel.setTextSize(10);
            mNumberLabel.setTextSize(10);
            txt_status.setTextSize(10);
            txt_phone.setTextSize(10);
            tvNameProfile.setTextSize(10);
            tvEmailProfile.setTextSize(10);
        }
    }

    private class MyAsync extends AsyncTask<String, String, ContactsGetSet> {

        String email, kachingName, userName, status;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressdialog = new ProgressDialog(UserProfile.this);
            progressdialog.setMessage(getResources().getString(R.string.loading));
            progressdialog.show();
        }

        @Override
        protected ContactsGetSet doInBackground(String... params) {
            try {
                contact = new ContactsGetSet();
                contact = dbAdapter.getContact(jid);
            } catch (Exception e) {
                // //ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
            }
            return contact;
        }

        @Override
        protected void onPostExecute(ContactsGetSet result) {
            progressdialog.cancel();
            super.onPostExecute(result);
            try {
                if (contact.getPhoto_bitmap() != null) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            System.gc();
//                             ProfileRoundImg roundImageProfile = new ProfileRoundImg(contact.getPhoto_bitmap());
//                    img_avatar.setImageDrawable(roundImageProfile);


                    Bitmap blurred = blurRenderScript(contact.getPhoto_bitmap(), 10);
                    final BitmapDrawable background = new BitmapDrawable(blurred);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    img_avatar.setImageBitmap(new AvatarManager()
//                                            .roundCornerImage(contact.getPhoto_bitmap(), 180));
                                    ProfileRoundImg roundImgProfile = new ProfileRoundImg(contact.getPhoto_bitmap());
                                    img_avatar.setImageDrawable(roundImgProfile);
                                    mBlurImgLayout.setBackgroundDrawable(background);
                                }
                            });
                        }
                    }).start();
                } else {
                    img_avatar.setImageDrawable(getApplicationContext()
                            .getResources().getDrawable(R.drawable.avtar));
                }
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
                e.printStackTrace();
            }
            try {
                email = contact.getNifty_email();
                status = contact.getStatus();
                userName = contact.getDisplay_name();
                kachingName = contact.getNifty_name();
                /* txt_id.setText(contact.getNifty_email()); */
                Constant.printMsg("profile friends  ::>>> "
                        + contact.getNifty_name() + "   "
                        + contact.getDisplay_name() + "   " + contact.getJid() + "...status...." + contact.getStatus());
                tvNameProfile.setText("" + contact.getDisplay_name());
                if (userName != null && !userName.equalsIgnoreCase("null")&& !userName.isEmpty()) {
                    txt_name.setText(userName);
                } else {
                    if (kachingName != null && !kachingName.equalsIgnoreCase("null")&& !kachingName.isEmpty()) {
                        txt_name.setText(kachingName);
                    } else {
                        txt_name.setText("Kaching User");
                    }
                }
                if (email != null && !email.equalsIgnoreCase("null") && !email.isEmpty()) {
                    tvEmailProfile.setText(email);
                } else {
                    tvEmailProfile.setText("No Email Id Found");
                }
                if (status != null && !status.equalsIgnoreCase("null") && !status.isEmpty()) {
                    txt_status.setText("" + status);
                } else {
                    txt_status.setText("" + R.string.hey_im_usning_niftycha);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
