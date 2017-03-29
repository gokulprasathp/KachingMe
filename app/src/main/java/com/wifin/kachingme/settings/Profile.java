package com.wifin.kachingme.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.SherlockBaseActivity;
import com.wifin.kachingme.async_tasks.ConcurrentAsyncTaskExecutor;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.LoginGetSet;
import com.wifin.kachingme.pojo.ProfileUpdateDto;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.ProfileRoundImg;
import com.wifin.kachingme.util.SendWeb;
import com.wifin.kachingme.util.Utils;
import com.wifin.kachingme.util.cropimage.CropImage;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;

import a_vcard.android.util.Log;

public class Profile extends SherlockBaseActivity implements OnClickListener {

    public static final int REQUEST_CODE_CROP_IMAGE = 12,
            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 99;
    private static final float BLUR_RADIUS = 25f;
    public static String TAG = Profile.class.getSimpleName();
    ProgressDialog progressdialog;
    TextView txt_email, txt_name, txt_phone, txt_status, mStatusLabel, mNumberLabel, mEmailLabel, mNameLabel;
    ImageView img_profile, img_profile_bg;
    VCard vc = new VCard();
    byte img_byte[] = null;
    DatabaseHelper dbAdapter;
    TextView btn_edit_name, btn_edit_picture, btn_edit_email, btn_edit_status;
    String roster[];
    Bitmap bmp;
    boolean tabletSize = false;
    TextView mGalleryText, mPhotoText, mSelectText;
    View popupView;
    int height, width;
    SharedPreferences sp1, preferences;
    String number, country_code, statusSave, emailSave, nameSave;
    FrameLayout mBlurImgLayout;
    LinearLayout mNameLayout, mStatusLayout, mNumberLayout, mEmailLayout;
    View.OnTouchListener customPopUpTouchListenr = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            Log.d("POPUP", "Touch false");
            return false;
        }

    };
    private ChatManager chatManager;
    private Chat chat;
    // newly added
    private Uri fileUri;
    private PopupWindow pwindo;
    Bitmap updateImage;
    ProfileRoundImg roundImgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        dbAdapter = KachingMeApplication.getDatabaseAdapter();
        initialization();
        screenArrangement();
        getSupportActionBar().setTitle(
                getResources().getString(R.string.contact_info));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabletSize = getResources().getBoolean(R.bool.isTablet);
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup_gallery, null);
        pwindo = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        sp1 = PreferenceManager.getDefaultSharedPreferences(this);
        preferences = getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        number = sp1.getString("number", "");
        country_code = sp1.getString("countrycode", "");
        ConcurrentAsyncTaskExecutor.executeConcurrently(new MyAsync());
    }

    private void initialization() {
        img_profile = (ImageView) findViewById(R.id.profile_pic);
        img_profile_bg = (ImageView) findViewById(R.id.profile_picBaground);
        txt_email = (TextView) findViewById(R.id.txt_email_id);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_phone = (TextView) findViewById(R.id.txt_phone);
        txt_status = (TextView) findViewById(R.id.txt_status_id);
        mBlurImgLayout = (FrameLayout) findViewById(R.id.blur_img_layout);
        mStatusLayout = (LinearLayout) findViewById(R.id.status_layout);
        mNumberLayout = (LinearLayout) findViewById(R.id.number_layout);
        mEmailLayout = (LinearLayout) findViewById(R.id.email_layout);
        mNameLayout = (LinearLayout) findViewById(R.id.name_layout);

        mStatusLabel = (TextView) findViewById(R.id.status_label);
        mNumberLabel = (TextView) findViewById(R.id.number_label);
        mEmailLabel = (TextView) findViewById(R.id.email_label);
        mNameLabel = (TextView) findViewById(R.id.name_label);

        // img_profile=(ImageView)findViewById(R.id.profile_img_p_pic);

        btn_edit_email = (TextView) findViewById(R.id.btn_edit_email);
        btn_edit_picture = (TextView) findViewById(R.id.btn_edit_picture);
        btn_edit_name = (TextView) findViewById(R.id.btn_edit_name);
        btn_edit_status = (TextView) findViewById(R.id.btn_edit_status);
        btn_edit_picture.setOnClickListener(this);
        btn_edit_name.setOnClickListener(this);
        btn_edit_status.setOnClickListener(this);
        btn_edit_email.setOnClickListener(this);
        img_profile.setOnClickListener(this);

        Constant.typeFace(this, txt_email);
        Constant.typeFace(this, txt_name);
        Constant.typeFace(this, txt_phone);
        Constant.typeFace(this, txt_status);
        Constant.typeFace(this, mStatusLabel);
        Constant.typeFace(this, mNumberLabel);
        Constant.typeFace(this, mEmailLabel);
        Constant.typeFace(this, mNameLabel);
        Constant.typeFace(this, btn_edit_email);
        Constant.typeFace(this, btn_edit_picture);
        Constant.typeFace(this, btn_edit_name);
        Constant.typeFace(this, btn_edit_status);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit_picture:
                if (Connectivity.isConnected(this)) {
                    selectImage();
                } else {
                    Toast.makeText(this, "Please Check Your Network Connection.!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_edit_name:
                if (Connectivity.isConnected(this)) {
                    setEdit(txt_name, "Name");
                } else {
                    Toast.makeText(this, "Please Check Your Network Connection.!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_edit_status:
                if (Connectivity.isConnected(this)) {
                    setEdit(txt_status, "Status");
                } else {
                    Toast.makeText(this, "Please Check Your Network Connection.!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_edit_email:
                if (Connectivity.isConnected(this)) {
                    setEdit(txt_email, "Email");
                } else {
                    Toast.makeText(this, "Please Check Your Network Connection.!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.profile_pic:
                if (Connectivity.isConnected(this)) {
                    Constant.printMsg("Clicked on profile picture....");
                    File f = new File(Constant.local_profile_picture_dir
                            + KachingMeApplication.getjid().split("@")[0] + ".jpg");
                    // you can create a new file name "test.BMP" in sdcard folder.
                    try {
                        if (bmp != null) {
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                         //   bmp.compress(CompressFormat.PNG, 100, bytes);

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
                        Intent intent = new Intent();
                        // intent.putExtra("path",f);
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(f), "image/jpg");
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please Check Your Network Connection.!!", Toast.LENGTH_SHORT).show();
                }
                break;
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

	/*
     * private ServiceConnection mConnection = new ServiceConnection() { public
	 * void onServiceConnected(ComponentName className, IBinder service) {
	 *
	 * mBoundService = ((KaChingMeService.LocalBinder) service) .getService();
	 * connection = mBoundService.getConnection(); new MyAsync().execute(); }
	 *
	 * public void onServiceDisconnected(ComponentName className) {
	 *
	 * mBoundService = null; } };
	 *
	 * void doBindService() {
	 *
	 * bindService(new Intent(profile.this, KaChingMeService.class),
	 * mConnection, Context.BIND_AUTO_CREATE); isBound = true; }
	 *
	 * void doUnbindService() { if (isBound) {
	 *
	 * unbindService(mConnection); isBound = false; } }
	 */
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//	}

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


//    public Bitmap blur(Bitmap image) {
//        if (null == image) return null;
//
//        Bitmap outputBitmap = Bitmap.createBitmap(image);
//        final RenderScript renderScript = RenderScript.create(this);
//        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
//        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
//
//        //Intrinsic Gausian blur filter
//        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
//        theIntrinsic.setRadius(BLUR_RADIUS);
//        theIntrinsic.setInput(tmpIn);
//        theIntrinsic.forEach(tmpOut);
//        tmpOut.copyTo(outputBitmap);
//        return outputBitmap;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* getSupportMenuInflater().inflate(R.menu.profile_menu, menu); */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                Intent intent = new Intent(Profile.this, SliderTesting.class);
                startActivity(intent);
                finish();
                // NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {

        switch (requestCode) {
            case 1:
                Constant.printMsg("siva check....RESULT_OK...." + RESULT_OK);
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

                    intent.putExtra("outputX", 126);
                    intent.putExtra("outputY", 126);
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("scale", true);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);

                }
                break;

            case REQUEST_CODE_CROP_IMAGE:
                Constant.printMsg("siva check....REQUEST_CODE_CROP_IMAGE...." + REQUEST_CODE_CROP_IMAGE);
                if (resultCode == RESULT_OK) {
                    final Bundle extras = imageReturnedIntent.getExtras();
                    if (CropImage.croppedImage  != null) {
                        try {
                            Bitmap bitmap = CropImage.croppedImage ;
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            bitmap.compress(CompressFormat.PNG, 100, out);
                            ByteArrayOutputStream outstream_thumb = new ByteArrayOutputStream();
                            try {

                                int quality = 100;
                                bitmap.compress(CompressFormat.JPEG, quality,
                                        outstream_thumb);

                                while ((outstream_thumb.size() / 1024) > 800) {
                                    outstream_thumb = new ByteArrayOutputStream();
                                    bitmap.compress(CompressFormat.JPEG, quality,
                                            outstream_thumb);
                                    quality = quality - 5;
                                    Log.d(TAG,
                                            "Map Image Size::"
                                                    + (outstream_thumb.size()) / 1024);
                                }
                            } catch (Exception e) {
                                // ACRA.getErrorReporter().handleException(e);
                                Log.w("TAG",
                                        "Error saving image file: " + e.getMessage());

                            }

                            Log.d(TAG, "Map Image Size::" + (outstream_thumb.size())
                                    / 1024);
                            img_byte = outstream_thumb.toByteArray();
                            // img_profile.setImageBitmap(bitmap);
                            roundImgProfile = new ProfileRoundImg(bitmap);
                            img_profile.setImageDrawable(roundImgProfile);
                            //img_profile.setImageBitmap(new AvatarManager().roundCornerImage(bitmap, 180));
                            Bitmap blurred = blurRenderScript(bitmap, 10);
                            BitmapDrawable background = new BitmapDrawable(blurred);
                            mBlurImgLayout.setBackgroundDrawable(background);
                            updateImage = bitmap;

                            CropImage.croppedImage = null;


                            ConcurrentAsyncTaskExecutor.executeConcurrently(new MyAsyncSave(),null, null, null);
                        } catch (Exception e) {

                        }
                    }

                }
                break;

            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                Constant.printMsg("siva check....CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE...." + CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                if (resultCode == RESULT_OK) {
                    // Image captured and saved to fileUri specified in the Intent
                /*
                 * Toast.makeText(this, "Image saved to:\n" + fileUri.getPath(),
				 * Toast.LENGTH_LONG).show();
				 */

                    Constant.printMsg("calledd shot::::::::>>>>>>>>>111");
                    File file = new File(fileUri.getPath());
                    if (file.length() > 26214400) {
                        new AlertManager().showAlertDialog(this, getResources()
                                        .getString(R.string.imagesize_must_be_smaller),
                                true);
                    } else {
                        Intent intent = new Intent(this, CropImage.class);
                        intent.setType("image/*");
                        intent.putExtra(CropImage.IMAGE_PATH, fileUri.getPath());

                        intent.putExtra("outputX", 126);
                        intent.putExtra("outputY", 126);
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
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

    public void setEdit(final TextView text, final String status) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(status);
        // alert.setMessage("Message");
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setText(text.getText());
        input.setSelectAllOnFocus(true);
        alert.setView(input);
        alert.setPositiveButton(getResources().getString(R.string.Ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (status.equalsIgnoreCase("name")) {
                            String value = input.getText().toString();
                            String temp = text.getText().toString();
                            text.setText(value);
                            if (!value.isEmpty()) {
                                if (!temp.equals(value)) {
                                    img_byte = null;
                                    ConcurrentAsyncTaskExecutor.executeConcurrently(new MyAsyncSave(),value, txt_email.getText().toString(), null);
//                                    new MyAsyncSave().execute(value, txt_email.getText().toString(), null);
                                } else {
//                                    Toast.makeText(getApplicationContext(), "Please Changes Something.!!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Name Fields Cannot be Empty", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (status.equalsIgnoreCase("email")) {
                                String value = input.getText().toString();
                                String temp = text.getText().toString();
                                text.setText(value);
                                if (!value.isEmpty()) {
                                    if (!temp.equals(value)) {
                                        img_byte = null;
                                        ConcurrentAsyncTaskExecutor.executeConcurrently(new MyAsyncSave(),value, txt_name.getText().toString(), value, null);
//                                        new MyAsyncSave().execute(txt_name.getText().toString(), value, null);
                                    } else {
//                                        Toast.makeText(getApplicationContext(), "Please Changes Something.!!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Email Fields Cannot be Empty", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });

        alert.setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
        alert.show();
    }

//    public void uploadFile(String strURL, boolean is_image) {
//        Constant.printMsg("calledd shot::::::::>>>>>>>>>222");
//
//        // Constant.bux = sharedPrefs.getLong("buxvalue", 0);
//        //
//        // Long buxval = Constant.bux + Constant.imagepoints;
//        // Constant.bux = buxval;
//        //
//        // int point = sharedPrefs.getInt("imgpoint", 0);
//        //
//        // Constant.totalimg = point;
//        //
//        // Constant.totalimg = Count + Constant.totalimg;
//        //
//        // Editor e2 = sharedPrefs.edit();
//        // e2.putInt("imgpoint", Constant.totalimg);
//        // e2.commit();
//        //
//        // Editor e1 = sharedPrefs.edit();
//        // e1.putLong("buxvalue", buxval);
//        // e1.commit();
//
//        String time = "" + System.currentTimeMillis();
//        String File_name = time + ".jpg";
//        String file_path = "";
//        int media_duration = 0;
//        int size = 0;
//        byte[] thumb = null;
//
//        String strMyImagePath = null;
//        Bitmap scaledBitmap = null;
//
//        if (is_image) {
//            Constant.printMsg("calledd shot::::::::>>>>>>>>>333");
//
//            try {
//
//                byte[] byteArray = null;
//                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
//                // ByteArrayOutputStream outstream_thumb = new
//                // ByteArrayOutputStream();
//                // Part 1: Decode image
//                // Bitmap unscaledBitmap = ScalingUtilities.decodeFile(strURL,
//                // 480, 800);
//
//                // if (!(unscaledBitmap.getWidth() <= 480 && unscaledBitmap
//                // .getHeight() <= 800)) {
//                // // Part 2: Scale image
//                // scaledBitmap = ScalingUtilities.createScaledBitmap(
//                // unscaledBitmap, 480, 800);
//                // scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75,
//                // outstream);
//                // scaledBitmap.recycle();
//                // } else {
//                //
//                // unscaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75,
//                // outstream);
//                // unscaledBitmap.recycle();
//                // }
//
//                Bitmap thumb_bitmap = ScalingUtilities.decodeFile(strURL, 100,
//                        100);
//                // ExifInterface exif = new ExifInterface(strURL);
//                Constant.printMsg("test vino image::::::shot3333333333::");
//                // Log.d("EXIF value", exif
//                // .getAttribute(ExifInterface.TAG_ORIENTATION));
//                // if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
//                // .equalsIgnoreCase("6")) {
//                // thumb_bitmap = rotate(thumb_bitmap, 90);
//                // } else if (exif.getAttribute(
//                // ExifInterface.TAG_ORIENTATION)
//                // .equalsIgnoreCase("8")) {
//                // thumb_bitmap = rotate(thumb_bitmap, 270);
//                // } else if (exif.getAttribute(
//                // ExifInterface.TAG_ORIENTATION)
//                // .equalsIgnoreCase("3")) {
//                // thumb_bitmap = rotate(thumb_bitmap, 180);
//                // } else if (exif.getAttribute(
//                // ExifInterface.TAG_ORIENTATION)
//                // .equalsIgnoreCase("0")) {
//                // thumb_bitmap = rotate(thumb_bitmap, 90);
//                // }
//
//                thumb_bitmap.compress(CompressFormat.JPEG, 100,
//                        outstream);
//                // thumb = outstream_thumb.toByteArray();
//                //
//                // boolean success = (new File(Constants.local_image_dir))
//                // .mkdirs();
//                // if (!success) {
//                // Log.w("directory not created", "directory not created");
//                // }
//                //
//                // file_path = Constants.local_image_dir + File_name;
//                //
//                // FileOutputStream stream = new FileOutputStream(file_path);
//                //
//                // byteArray = outstream.toByteArray();
//                //
//                // stream.write(byteArray);
//                // stream.close();
//                ByteArrayOutputStream outstream_thumb1 = new ByteArrayOutputStream();
//                try {
//
//                    int quality = 100;
//
//                    tabletSize = getResources().getBoolean(R.bool.isTablet);
//                    Constant.printMsg("test vino tabletSize4::" + tabletSize);
//                    if (tabletSize) {
//                        ExifInterface exife = new ExifInterface(strURL);
//
//                        System.out
//                                .println("test vino image::::::shot444444444444::"
//                                        + exife.getAttribute(ExifInterface.TAG_ORIENTATION));
//                        Log.d("EXIF value", exife
//                                .getAttribute(ExifInterface.TAG_ORIENTATION));
//                        if (exife.getAttribute(ExifInterface.TAG_ORIENTATION)
//                                .equalsIgnoreCase("6")) {
//                            thumb_bitmap = rotate(thumb_bitmap, 90);
//                        } else if (exife.getAttribute(
//                                ExifInterface.TAG_ORIENTATION)
//                                .equalsIgnoreCase("8")) {
//                            thumb_bitmap = rotate(thumb_bitmap, 270);
//                        } else if (exife.getAttribute(
//                                ExifInterface.TAG_ORIENTATION)
//                                .equalsIgnoreCase("3")) {
//                            thumb_bitmap = rotate(thumb_bitmap, 180);
//                        } else if (exife.getAttribute(
//                                ExifInterface.TAG_ORIENTATION)
//                                .equalsIgnoreCase("0")) {
//                            thumb_bitmap = rotate(thumb_bitmap, 90);
//                        }
//                    }
//                    thumb_bitmap.compress(CompressFormat.JPEG, quality,
//                            outstream_thumb1);
//
//                    while ((outstream_thumb1.size() / 1024) > 180) {
//                        outstream_thumb1 = new ByteArrayOutputStream();
//                        thumb_bitmap.compress(CompressFormat.JPEG, quality,
//                                outstream_thumb1);
//                        quality = quality - 5;
//                        Log.d(TAG,
//                                "Map Image Size::" + (outstream_thumb1.size())
//                                        / 1024);
//                    }
//
//                } catch (Exception e) {
//                    // ACRA.getErrorReporter().handleException(e);
//                    Log.w("TAG", "Error saving image file: " + e.getMessage());
//
//                }
//
//                Log.d(TAG, "Map Image Size::" + (outstream_thumb1.size())
//                        / 1024);
//
//                img_byte = outstream_thumb1.toByteArray();
//                roundImgProfile = new ProfileRoundImg(thumb_bitmap);
//                img_profile.setImageDrawable(roundImgProfile);
//                //img_profile.setImageBitmap(thumb_bitmap);
//                //img_profile.setImageBitmap(new AvatarManager().roundCornerImage(thumb_bitmap, 180));
//                // img_profile.setImageBitmap(thumb_bitmap);
//                Constant.printMsg("calledd shot::::::::>>>>>>>>>444"
//                        + thumb_bitmap);
//                ConcurrentAsyncTaskExecutor.executeConcurrently(new MyAsyncSave(),null, null, null);
////                new MyAsyncSave().execute(null, null, null);
//                // Toast.makeText(context, "Downloading Completed",
//                // Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//                e.printStackTrace();
//            }
//
//        } else {
//            file_path = strURL;
//
//            Bitmap video_thumb = ThumbnailUtils.createVideoThumbnail(file_path,
//                    MediaStore.Images.Thumbnails.MINI_KIND);
//
//            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
//            video_thumb.compress(CompressFormat.JPEG, 50, outstream);
//
//            // thumb = outstream.toByteArray();
//
//            ByteArrayOutputStream outstream_thumb = new ByteArrayOutputStream();
//            try {
//
//                int quality = 100;
//                video_thumb.compress(CompressFormat.JPEG, quality,
//                        outstream_thumb);
//
//                while ((outstream_thumb.size() / 1024) > 180) {
//                    outstream_thumb = new ByteArrayOutputStream();
//                    video_thumb.compress(CompressFormat.JPEG, quality,
//                            outstream_thumb);
//                    quality = quality - 5;
//                    Log.d(TAG, "Map Image Size::" + (outstream_thumb.size())
//                            / 1024);
//                }
//
//            } catch (Exception e) {
//                // ACRA.getErrorReporter().handleException(e);
//                Log.w("TAG", "Error saving image file: " + e.getMessage());
//
//            }
//
//            Log.d(TAG, "Map Image Size::" + (outstream_thumb.size()) / 1024);
//
//            img_byte = outstream_thumb.toByteArray();
//            // img_profile.setImageBitmap(bitmap);
//            roundImgProfile = new ProfileRoundImg(video_thumb);
//            img_profile.setImageDrawable(roundImgProfile);
//            //img_profile.setImageBitmap(new AvatarManager().roundCornerImage(video_thumb, 180));
//            ConcurrentAsyncTaskExecutor.executeConcurrently(new MyAsyncSave(),null, null, null);
//
//            // File f = new File(strURL);
//            // File_name = f.getName();
//            // size = (int) f.length();
//            // try {
//            // MediaPlayer mp = new MediaPlayer();
//            // mp.setDataSource(file_path);
//            // mp.prepare();
//            //
//            // media_duration = mp.getDuration();
//            //
//            // Log.d("Media", "Duration::" + media_duration);
//            // } catch (Exception e) {//
//            // ACRA.getErrorReporter().handleException(e);
//            // e.printStackTrace();
//            // }
//            // Log.d("Video Thumb size", "Video Thumbsize::" + (size / 1024)
//            // / 1024 + " MB");
//            //
//            // try {
//            // if (!f.getPath().equals(Constants.local_video_dir + File_name)) {
//            // Utils.Copyfile(f, new File(Constants.local_video_dir
//            // + File_name));
//            // }
//            // } catch (IOException e) {
//            // // TODO Auto-generated catch block
//            // e.printStackTrace();
//            // }
//            // }
//
//            // MessageGetSet msggetset = new MessageGetSet();
//            // String packet_id = "" + new Date().getTime();
//            //
//            // msggetset.setData("");
//            // msggetset.setKey_from_me(0);
//            // msggetset.setKey_id("" + packet_id);
//            // msggetset.setKey_remote_jid(jid);
//            // msggetset.setNeeds_push(1);
//            // msggetset.setSend_timestamp(new Date().getTime());
//            // msggetset.setStatus(3);
//            // msggetset.setTimestamp(new Date().getTime());
//            // msggetset.setThumb_image(thumb);
//            //
//            // msggetset.setMedia_name(File_name);
//            // if (file_path.contains(".")) {
//            // msggetset.setMedia_mime_type(new Utils().getMimeType(file_path));
//            // if (is_image) {
//            // msggetset.setMedia_wa_type("" + 1);
//            // } else {
//            // msggetset.setMedia_wa_type("" + 2);
//            // }
//            // msggetset.setMedia_url(null);
//            // msggetset.setRow_data(thumb);
//            // msggetset.setMedia_duration(media_duration);
//            // msggetset.setMedia_size(size);
//            // msggetset.setIs_sec_chat(sec);
//            // msggetset.setSelf_des_time(selected_self_desc_time);
//            // dbAdapter.setInsertMessages(msggetset);
//            // edt_msg.setText("");
//
//            // new FetchChat().execute();
//            // } else {
//            // Toast.makeText(getApplicationContext(),
//            // "Please Select Valid File",
//            // Toast.LENGTH_SHORT).show();
//            // }
//            // int msg_id = dbAdapter.getLastMsgid_chat(jid, sec);
//            //
//            // if (dbAdapter.isExistinChatList_chat(jid, sec)) {
//            // dbAdapter.setUpdateChat_lits_chat(jid, msg_id, sec);
//            // } else {
//            // dbAdapter.setInsertChat_list_chat(jid, msg_id, sec);
//            // }
//            //
//            // Intent login_broadcast = new Intent("chat");
//            // login_broadcast.putExtra("jid", "" + jid);
//            // this.sendBroadcast(login_broadcast);
//
//            // new FetchChat().execute();
//        }
//    }

    private void popup() {
        // TODO Auto-generated method stub
        try {
            // We need to get the instance of the LayoutInflater
            // LayoutInflater inflater = (LayoutInflater) OtpActivity.this
            // .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // View layout = inflater.inflate(R.layout.popup_gallery,
            // (ViewGroup) findViewById(R.id.gallery_layout));
            // layout.setPadding(4, 4, 4, 4);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            height = displayMetrics.heightPixels;
            width = displayMetrics.widthPixels;
            Constant.screenHeight = height;
            Constant.screenWidth = width;
            int x = Constant.screenWidth;
            int y = Constant.screenHeight;
            Constant.printMsg("x :::::::::::" + x);
            Constant.printMsg("y::::::::::::::" + y);

            // pwindo.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            //
            mGalleryText = (TextView) popupView.findViewById(R.id.gallery_txt);
            mPhotoText = (TextView) popupView.findViewById(R.id.photo_txt);
            mSelectText = (TextView) popupView
                    .findViewById(R.id.select_textView1);
            LinearLayout.LayoutParams lay1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lay1.width = x * 30 / 100;
            // text_lay.setMargins(10, 0, 10, 0);
            lay1.height = y * 20 / 100;
            lay1.gravity = Gravity.CENTER;
            popupView.setLayoutParams(lay1);

            LinearLayout.LayoutParams list_lay = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            list_lay.width = x * 30 / 100;
            list_lay.setMargins(10, 0, 10, 0);
            list_lay.height = y * 6 / 100;
            mGalleryText.setLayoutParams(list_lay);
            // mGalleryText.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams text_lay = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            text_lay.width = x * 30 / 100;
            // text_lay.setMargins(10, 0, 10, 0);
            text_lay.height = y * 6 / 100;
            text_lay.gravity = Gravity.CENTER;
            mPhotoText.setLayoutParams(text_lay);
            // mPhotoText.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams text_lay1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            text_lay1.width = x * 30 / 100;
            // text_lay.setMargins(10, 0, 10, 0);
            text_lay1.leftMargin = x * 1 / 100;
            text_lay1.height = y * 5 / 100;
            text_lay1.gravity = Gravity.LEFT;
            mSelectText.setLayoutParams(text_lay1);

            // popupView.setLayoutParams(text_lay1);

            if (width >= 600) {

                mGalleryText.setTextSize(19);
                mPhotoText.setTextSize(19);
                mSelectText.setTextSize(19);

            } else if (width > 501 && width < 600) {

                mGalleryText.setTextSize(18);
                mPhotoText.setTextSize(18);
                mSelectText.setTextSize(18);
            } else if (width > 260 && width < 500) {

                mGalleryText.setTextSize(17);
                mPhotoText.setTextSize(17);
                mSelectText.setTextSize(17);
            } else if (width <= 260) {

                mGalleryText.setTextSize(16);
                mPhotoText.setTextSize(16);
                mSelectText.setTextSize(16);
            }

            mGalleryText.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    pwindo.dismiss();
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    try {
                        startActivityForResult(photoPickerIntent, 1);
                    } catch (ActivityNotFoundException e) {

                    }
                }
            });

            mPhotoText.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    pwindo.dismiss();
                    Intent intent;
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = Utils.getOutputMediaFileUri(1);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set

                    startActivityForResult(intent,
                            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

                }
            });

            pwindo.setOutsideTouchable(true);
            pwindo.setFocusable(true);

            pwindo.setBackgroundDrawable(new BitmapDrawable());
            pwindo.getBackground().setAlpha(0);
            // pwindo.setModal(true);
            // pwindo.setOnDismissListener(onDismissListener);
            pwindo.setTouchInterceptor(customPopUpTouchListenr);
            pwindo.showAsDropDown(btn_edit_picture, -2, 55);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selectImage() {
        final CharSequence[] options = {"Photo", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("Select Picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Photo")) {

                    Intent intent;
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = Utils.getOutputMediaFileUri(1);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set

                    startActivityForResult(intent,
                            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

                } else if (options[item].equals("Gallery")) {

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    try {
                        startActivityForResult(photoPickerIntent, 1);
                    } catch (ActivityNotFoundException e) {

                    }

                }

            }
        });
        builder.show();
    }

    public void insertStatusDB(ContentValues v) {
        Dbhelper db = new Dbhelper(getApplicationContext());
        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_UPDATE_STATUS, null, v);
            Constant.printMsg("No of rows inserted into TABLE_UPDATE_STATUS in status classsssss is ::::"
                    + a);
        } catch (SQLException e) {

        } finally {
            db.close();
        }

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

    private class MyAsync extends AsyncTask<String, String, String> {
        // Bitmap bmp;
        String email;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressdialog = new ProgressDialog(Profile.this);
            progressdialog.setMessage(getResources()
                    .getString(R.string.loading));
            progressdialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            Constant.printMsg("profile get inside........");
            try {
                img_byte = KachingMeApplication.getAvatar();
                bmp = BitmapFactory.decodeByteArray(
                        KachingMeApplication.getAvatar(), 0,
                        KachingMeApplication.getAvatar().length);
            } catch (Exception e) {
                // //ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
            }
            Constant.printMsg("profile get inside after get bitmap........");
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressdialog.cancel();
            super.onPostExecute(result);
            try {
                if (bmp != null) {
//                    img_profile.setImageBitmap(new AvatarManager()
//                            .roundCornerImage(bmp, 180));
                    roundImgProfile = new ProfileRoundImg(bmp);
                    img_profile.setImageDrawable(roundImgProfile);
                    //img_profile.setImageBitmap(bmp);
//                    img_profile.setImageResource(R.drawable.avatar_image);
                    Bitmap blurred = blurRenderScript(bmp, 10);
                    BitmapDrawable background = new BitmapDrawable(blurred);
                    mBlurImgLayout.setBackgroundDrawable(background);

//                    Bitmap icon = BitmapFactory.decodeResource(getResources(),
//                            R.drawable.reg_bg);
//                    img_profile_bg.setImageBitmap(new AvatarManager()
//                            .roundCornerImage(bmp, 180));
                    img_profile_bg.setImageResource(R.drawable.profile_square_bg);

//                    BitmapDrawable ob = new BitmapDrawable(getResources(), new AvatarManager()
//                            .roundCornerImage(icon, 190));
//                    img_profile.setBackgroundDrawable(ob);
//                    img_profile.setBackgroundResource(R.drawable.reg_bg);
                } else {
                    img_profile.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.avtar));
                    mBlurImgLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_profilepic_bg));
                }

            } catch (Exception e) {
                // //ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
            }

            txt_email.setText(KachingMeApplication.Nifty_email);
            Constant.printMsg("profile name ::>>>> "
                    + KachingMeApplication.getNifty_name());
            txt_name.setText(KachingMeApplication.getNifty_name());
            // txt_phone.setText("+" + country_code+number);
            String phoneNumber = preferences.getString("ChatUserNumber", "");
            Constant.printMsg("profile number ::>>>> "
                    + phoneNumber+"....."+ KachingMeApplication.getjid().split("@")[0]);
            if (phoneNumber != null && !phoneNumber.equalsIgnoreCase("null") &&
                    !phoneNumber.isEmpty()) {
                txt_phone.setText("+" + phoneNumber);
            } else {
                txt_phone.setText("+" + KachingMeApplication.getjid().split("@")[0]);
            }
            txt_status.setText(KachingMeApplication.Status);
        }
    }

    private class MyAsyncSave extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressdialog = new ProgressDialog(Profile.this);
            progressdialog.setMessage(getResources()
                    .getString(R.string.loading));
            progressdialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            nameSave = params[0];
            emailSave = params[1];
            statusSave = params[2];
            Constant.printMsg("profile Update values status.." + statusSave + "....name...." + nameSave + "....email...." + emailSave);
            Constant.printMsg("profile Update values picture.." +img_byte);
            try {
                try {
                    vc = VCardManager.getInstanceFor(
                            TempConnectionService.connection).loadVCard();
                } catch (Exception e) {
                    Constant.printMsg("profile Update vcard Exception" + e);
                }
                if (nameSave != null) {
                    vc.setFirstName(nameSave);
                }
                if (emailSave != null) {
                    vc.setEmailWork(emailSave);
                }
                if (statusSave != null) {
                    vc.setField("SORT-STRING", statusSave);
                }
                if (img_byte != null && img_byte.length>0) {
                    vc.setAvatar(img_byte);
                }
                // vc.setField("SORT-STRING", KachingMeApplication.getStatus());
                vc.setJabberId(KachingMeApplication.getjid());
                VCardManager.getInstanceFor(TempConnectionService.connection).saveVCard(vc);
                Constant.printMsg("profile Update Before send Packate");
                chatManager = ChatManager.getInstanceFor(TempConnectionService.connection);

                Roster roster_c = Roster.getInstanceFor(TempConnectionService.connection);
                Collection<RosterEntry> entries = roster_c.getEntries();
                Profile.this.roster = new String[entries.size()];
                int i = 0;
                for (RosterEntry rosterEntry : entries) {
                    roster[i] = rosterEntry.getUser();
                    i++;
                }
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
               // e.printStackTrace();
                Constant.printMsg("profile Update vcard final  Exception" + e);
            }
            if (img_byte != null && img_byte.length>0) {
                KachingMeApplication.setAvatar(img_byte);
            }
            if (nameSave != null && emailSave != null) {
                KachingMeApplication.setNifty_name(nameSave);
                KachingMeApplication.setNifty_email(emailSave);
                String encodeName, encodeEmail;
                try {
                    encodeName = URLEncoder.encode(nameSave, "utf-8");
                    encodeEmail = URLEncoder.encode(emailSave, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    encodeName = nameSave;
                    encodeEmail =emailSave;
                }
                HttpConfig ht = new HttpConfig();
                result = ht.httpget(KachingMeConfig.UPDATE_EMAIL_NAME + "phoneNumber=" + preferences.getString("MyPrimaryNumber", "")
                        + "&&emailId=" + encodeEmail + "&&username=" + encodeName);
                Constant.printMsg("Profile update....email.and name..url...." + KachingMeConfig.UPDATE_EMAIL_NAME + "phoneNumber=" + preferences.getString("MyPrimaryNumber", "")
                        + "&&emailId=" + encodeEmail + "&&username=" + encodeName);
                Constant.printMsg("Profile update....email.and name..result.." + result);
            }

            if (statusSave != null) {
                dbAdapter.setUpdateUserStatus(KachingMeApplication.getUserID(), statusSave);
                ContentValues v = new ContentValues();
                // v.put("name", k);
                v.put("status", statusSave);
                insertStatusDB(v);
                KachingMeApplication.setStatus(statusSave);
            }
            if (img_byte != null && img_byte.length>0) {
                try {
                    Constant.printMsg("Profile update.picture.............");
                    HttpConfig ht = new HttpConfig();
                    ProfileUpdateDto profileUpdate = new ProfileUpdateDto();
                    if (number != null && !number.isEmpty()) {
                        profileUpdate.setPhoneNumber(country_code + number);
                    } else {
                        profileUpdate.setPhoneNumber(preferences.getString("MyPrimaryNumber", ""));
                    }
                    profileUpdate.setProfilePhoto(encodeToBase64(updateImage, CompressFormat.JPEG, 100));
                    Gson gson = new Gson();
                    String strData = gson.toJson(profileUpdate).toString();
                    Constant.printMsg("Profile update........post data........." + strData);
                    result = ht.doPostMobizee(strData, KachingMeConfig.UPDATE_PROFILE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Constant.printMsg("Profile update....result err....." + e);
                }
            }
            LoginGetSet login = new LoginGetSet();
            login.setAvatar(img_byte);
            login.setNifty_email(emailSave);
            login.setNifty_name(nameSave);
            login.setUserName(KachingMeApplication.getUserID());
            login.setStatus(statusSave);
            dbAdapter.setUpdateLogin(login);
            Constant.printMsg("Profile update....final result....." + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && result.length() > 0) {
                if (result.contains("Profile photo updated successfully")){
                    try {
                        for (int i = 0; i < roster.length; i++) {
                            Message msg = new Message();
                            msg.setStanzaId("vcardedit");
                            msg.setType(Message.Type.chat);
                            msg.setTo(roster[i]);
                            TempConnectionService.connection.sendStanza(msg);
                        }
                    } catch (Exception e) {
                        // ACRA.getErrorReporter().handleException(e);fv
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    SendWeb.Update_Profile_web_async(Profile.this,
                            KachingMeApplication.getjid(), txt_name.getText().toString(),
                            txt_email.getText().toString(), img_byte);
                    Toast.makeText(getApplicationContext(), "Profile Picture Updated", Toast.LENGTH_SHORT).show();
                }else{
                    /**need to do failure state*/
                    //Toast.makeText(getApplicationContext(), "Failure to Update", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Network Error.!!", Toast.LENGTH_SHORT).show();
            }
            progressdialog.cancel();
        }
    }

    public static String encodeToBase64(Bitmap image, CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Profile.this, SliderTesting.class);
        startActivity(intent);
        finish();
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
        img_profile.setLayoutParams(mProfileImgParams);

        FrameLayout.LayoutParams mProfileImgBgParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        mProfileImgBgParams.width = (int) (width * 41.5 / 100);
        mProfileImgBgParams.height = (int) (width * 41.5 / 100);
//        mProfileImgBgParams.rightMargin = width * 1 / 100;
        img_profile_bg.setLayoutParams(mProfileImgBgParams);

        LinearLayout.LayoutParams mStatusLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mStatusLayoutParams.width = (int) width;
//        mStatusLayoutParams.height = (int) width * 25 / 100;
        mStatusLayoutParams.topMargin = width * 5 / 100;
        mStatusLayout.setLayoutParams(mStatusLayoutParams);
        mNumberLayout.setLayoutParams(mStatusLayoutParams);
        mEmailLayout.setLayoutParams(mStatusLayoutParams);
        mNameLayout.setLayoutParams(mStatusLayoutParams);

        LinearLayout.LayoutParams mStatusLabelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mStatusLabelParams.width = (int) width;
        mStatusLabelParams.height = (int) width * 12 / 100;
        mStatusLabel.setLayoutParams(mStatusLabelParams);
        mNumberLabel.setLayoutParams(mStatusLabelParams);
        mEmailLabel.setLayoutParams(mStatusLabelParams);
        mNameLabel.setLayoutParams(mStatusLabelParams);

        mStatusLabel.setPadding(width * 5 / 100, width * 3 / 100, width * 5 / 100, width * 3 / 100);
        mNumberLabel.setPadding(width * 5 / 100, width * 3 / 100, width * 5 / 100, width * 3 / 100);
        mEmailLabel.setPadding(width * 5 / 100, width * 3 / 100, width * 5 / 100, width * 3 / 100);
        mNameLabel.setPadding(width * 5 / 100, width * 3 / 100, width * 5 / 100, width * 3 / 100);

        LinearLayout.LayoutParams mCallImgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mCallImgParams.width = (int) width * 6 / 100;
        mCallImgParams.height = (int) (width * 6.0 / 100);
        mCallImgParams.gravity = Gravity.CENTER;
        mCallImgParams.leftMargin = width * 3 / 100;
        mCallImgParams.rightMargin = width * 3 / 100;
        btn_edit_email.setLayoutParams(mCallImgParams);
        btn_edit_name.setLayoutParams(mCallImgParams);
//        btn_edit_picture.setLayoutParams(mCallImgParams);
        btn_edit_status.setLayoutParams(mCallImgParams);

        btn_edit_status.setGravity(Gravity.CENTER);
        btn_edit_name.setGravity(Gravity.CENTER);
        btn_edit_email.setGravity(Gravity.CENTER);


        FrameLayout.LayoutParams mProfileImgEditParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        mProfileImgEditParams.width = (int) width * 11 / 100;
        mProfileImgEditParams.height = (int) (width * 11 / 100);
        mProfileImgEditParams.gravity = Gravity.RIGHT;
        mProfileImgEditParams.topMargin = width * 30 / 100;
//        mProfileImgEditParams.leftMargin = width * 3 / 100;
        btn_edit_picture.setLayoutParams(mProfileImgEditParams);
        btn_edit_picture.setGravity(Gravity.BOTTOM | Gravity.RIGHT);

        LinearLayout.LayoutParams mNumberTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mNumberTextParams.width = width * 80 / 100;
        mNumberTextParams.leftMargin = (int) width * 5 / 100;
        mNumberTextParams.height = (int) width * 12 / 100;
        txt_phone.setLayoutParams(mNumberTextParams);
        txt_phone.setGravity(Gravity.CENTER | Gravity.LEFT);
        txt_email.setLayoutParams(mNumberTextParams);
        txt_email.setGravity(Gravity.CENTER | Gravity.LEFT);

        LinearLayout.LayoutParams mStatusTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mStatusTextParams.width = width * 90 / 100;
//        mStatusTextParams.leftMargin = (int) width * 5 / 100;
//        mStatusTextParams.height = (int) width * 12 / 100;
        txt_status.setLayoutParams(mStatusTextParams);
        txt_status.setGravity(Gravity.CENTER | Gravity.LEFT);
        txt_name.setGravity(Gravity.CENTER | Gravity.LEFT);
        txt_name.setPadding(width * 5 / 100, width * 3 / 100, width * 5 / 100, width * 3 / 100);

        txt_status.setPadding(width * 5 / 100, width * 3 / 100, width * 5 / 100, width * 3 / 100);


        if (width >= 600) {
            mStatusLabel.setTextSize(16);
            mNumberLabel.setTextSize(16);
            mEmailLabel.setTextSize(16);
            mNameLabel.setTextSize(16);
            txt_status.setTextSize(16);
            txt_phone.setTextSize(16);
            txt_name.setTextSize(16);
            txt_email.setTextSize(16);
        } else if (width < 600 && width >= 480) {
            mStatusLabel.setTextSize(15);
            mNumberLabel.setTextSize(15);
            mEmailLabel.setTextSize(15);
            mNameLabel.setTextSize(15);
            txt_status.setTextSize(15);
            txt_phone.setTextSize(15);
            txt_name.setTextSize(15);
            txt_email.setTextSize(15);
        } else if (width < 480 && width >= 320) {
            mStatusLabel.setTextSize(12);
            mNumberLabel.setTextSize(12);
            mEmailLabel.setTextSize(12);
            mNameLabel.setTextSize(12);
            txt_status.setTextSize(12);
            txt_phone.setTextSize(12);
            txt_name.setTextSize(12);
            txt_email.setTextSize(12);
        } else if (width < 320) {
            mStatusLabel.setTextSize(10);
            mNumberLabel.setTextSize(10);
            mEmailLabel.setTextSize(10);
            mNameLabel.setTextSize(10);
            txt_status.setTextSize(10);
            txt_phone.setTextSize(10);
            txt_name.setTextSize(10);
            txt_email.setTextSize(10);
        }
    }
}
