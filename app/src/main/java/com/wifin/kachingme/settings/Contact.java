package com.wifin.kachingme.settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.ContactAdapter;
import com.wifin.kachingme.pojo.Contact_Us_Dto;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.cropimage.CropImage;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

public class Contact extends Activity
{
    AppBarLayout appbarContactUs;
    Toolbar toolbarContactUs;
    FrameLayout frameContactUs;
    ImageView imgBackContact, imgSendContact, imgAddScreenShot;
    TextView tvTitleContact, tvAddScreenContact;
    LinearLayout linearContactContent, linearAddScreenShot;
    EditText etContactQuery;
    Context contextContact;
    String TAG = Contact.class.getSimpleName();
    int height, width;
    GridView grid_photo;
    ContactAdapter mAdapter = null;
    public static final int REQUEST_CODE_CROP_IMAGE = 12, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 99;
    private Uri fileUri;
    byte img_byte[] = null;
    public static Context context;
    String data;
    SharedPreferences sp1;
    String number;
    ArrayList<String> image_file = new ArrayList<>();
    public ArrayList<String> map = new ArrayList<>();
    MultipartEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contact);

        contextContact = this;

        initContact();
        screenContact();

        Constant.printMsg("Contact Screen From App Info ::: ");
        sp1 = PreferenceManager.getDefaultSharedPreferences(this);
        String no = sp1.getString("number", "");
        String country_code = sp1.getString("countrycode", "");
        number = country_code + no;

        mAdapter = new ContactAdapter(Contact.this, Constant.mList);
        grid_photo.setAdapter(mAdapter);

        imgBackContact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        tvAddScreenContact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Constant.mList.size() < 2)
                {
                    addScreenShot();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Can't able to attach More", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgAddScreenShot.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Constant.mList.size() < 2)
                {
                    addScreenShot();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Can't able to attach More", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgSendContact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Constant.printMsg("Contact Us OnClicked For Image Upload :: ");

                if (etContactQuery.getText().toString().trim().length() > 0)
                {
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Contact.this.runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    try
                                    {
                                        for (int i = 0; i < Constant.mList.size(); i++)
                                        {
                                            new ImageUploadTask().execute(i + "", number, "Screen_" + i + ".jpg", "0.0", "0.0", etContactQuery.getText().toString());
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();

                                        Constant.printMsg("Contact Us Multiple Image 1 ::: " + e.toString());
                                    }
                                }
                            });
                        }
                    }).start();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please describe your problem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addScreenShot()
    {
        final CharSequence[] options = { "Gallery" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Contact.this);
        builder.setTitle("Select Picture");
        builder.setItems(options, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                if (options[item].equals("Gallery"))
                {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

                    photoPickerIntent.setType("image/*");

                    try
                    {
                        startActivityForResult(photoPickerIntent, 1);
                    }
                    catch (ActivityNotFoundException e)
                    {

                    }
                }
            }
        });
        builder.show();
    }

    public void initContact()
    {
        appbarContactUs = (AppBarLayout) findViewById(R.id.appbarContactUs);
        toolbarContactUs = (Toolbar) findViewById(R.id.toolbarContactUs);
        frameContactUs = (FrameLayout) findViewById(R.id.frameContactUs);
        imgBackContact = (ImageView) findViewById(R.id.imgBackContact);
        imgSendContact = (ImageView) findViewById(R.id.imgSendContact);
        tvTitleContact = (TextView) findViewById(R.id.tvTitleContact);
        tvAddScreenContact = (TextView) findViewById(R.id.tvAddScreenContact);
        linearContactContent = (LinearLayout) findViewById(R.id.linearContactContent);
        etContactQuery = (EditText) findViewById(R.id.etContactQuery);
        grid_photo = (GridView) findViewById(R.id.grid_photo);
        imgAddScreenShot = (ImageView) findViewById(R.id.imgAddScreenShot);
        linearAddScreenShot = (LinearLayout) findViewById(R.id.linearAddScreenShot);

        Constant.typeFace(this,tvTitleContact);
        Constant.typeFace(this,tvAddScreenContact);
        Constant.typeFace(this,etContactQuery);
    }

    public void screenContact()
    {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        Log.e(TAG, "Height : " + height + "\n" + "Width : " + width);

        Constant.height = height;
        Constant.width = width;

        int margin = width * 2 / 100;

        AppBarLayout.LayoutParams toolBarPrivacy = new AppBarLayout.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        toolBarPrivacy.width = (int) width;
        toolBarPrivacy.height = (int) height * 10 / 100;
        toolBarPrivacy.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        toolbarContactUs.setLayoutParams(toolBarPrivacy);

        FrameLayout.LayoutParams buttonPrivacyBack = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonPrivacyBack.width = (int) width * 6 / 100;
        buttonPrivacyBack.height = (int) width * 6 / 100;
        buttonPrivacyBack.leftMargin = (int) width * 4 / 100;
        buttonPrivacyBack.gravity = Gravity.START | Gravity.CENTER;
        imgBackContact.setLayoutParams(buttonPrivacyBack);

        FrameLayout.LayoutParams buttonSend = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonSend.width = (int) width * 10 / 100;
        buttonSend.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        buttonSend.rightMargin = (int) width * 3 / 100;
        buttonSend.gravity = Gravity.END | Gravity.CENTER;
        imgSendContact.setLayoutParams(buttonSend);

        LinearLayout.LayoutParams linearEditQuery = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearEditQuery.width = LinearLayout.LayoutParams.MATCH_PARENT;
        linearEditQuery.height = width * 30 / 100;
        linearEditQuery.setMargins(margin, margin, margin, margin);
        etContactQuery.setLayoutParams(linearEditQuery);

        LinearLayout.LayoutParams linearAddScreen = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearAddScreen.width = LinearLayout.LayoutParams.MATCH_PARENT;
        linearAddScreen.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearAddScreen.setMargins(margin, 0, margin, margin);
        linearAddScreenShot.setLayoutParams(linearAddScreen);

        LinearLayout.LayoutParams linearImageAdd = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearImageAdd.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearImageAdd.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        imgAddScreenShot.setLayoutParams(linearImageAdd);
        tvAddScreenContact.setLayoutParams(linearImageAdd);

        if (width >= 600)
        {
            tvTitleContact.setTextSize(19);
            etContactQuery.setTextSize(18);
            tvAddScreenContact.setTextSize(18);
        }
        else if (width > 501 && width < 600)
        {
            tvTitleContact.setTextSize(18);
            etContactQuery.setTextSize(17);
            tvAddScreenContact.setTextSize(17);
        }
        else if (width > 260 && width < 500)
        {
            tvTitleContact.setTextSize(17);
            etContactQuery.setTextSize(16);
            tvAddScreenContact.setTextSize(16);
        }
        else if (width <= 260)
        {
            tvTitleContact.setTextSize(16);
            etContactQuery.setTextSize(15);
            tvAddScreenContact.setTextSize(15);
        }
    }

    protected String jSonFrom()
    {
        String d = null;
        Contact_Us_Dto c = new Contact_Us_Dto();
        c.setDescription(etContactQuery.getText().toString().trim());
        c.setImageStrings(image_file);
        c.setPhoneNumber(number);
        d = new Gson().toJson(c);
        Constant.printMsg("json " + d);
        return d;
    }

    public class postContactusDetails extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {
            super.onPreExecute();

            if (map.size() < 0)
            {
                Constant.printMsg("Contact Page Map :: No Image Selected");
            }
            else
            {
                Constant.printMsg("Contact Page Map :: " + map.size() + " ::: " + map.toString());
            }
        }

        @Override
        protected String doInBackground(String... params)
        {
            // TODO Auto-generated method stub

            String result = null;

            HttpConfig ht = new HttpConfig();

            result = ht.doPostMobizee(data, KachingMeConfig.Contact_Us_URL);

            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            Constant.printMsg("contactus result res:;" + result);

            if (result != null && result.length() > 0)
            {
                if (result.toString().trim().equalsIgnoreCase("Reported"))
                {
                    Constant.printMsg("contactus result res:1;" + result);

                    Toast.makeText(getApplicationContext(), "Your problem has beed reported.We will reslove this soon", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Contact.this, AppInfo.class));
                    finish();
                }
                if (result.toString().trim().equalsIgnoreCase("Primary Number Not Exists"))
                {
                    Constant.printMsg("contactus result res:123;" + result);

                    Toast.makeText(getApplicationContext(), "Primary Number Not Exists", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Contact.this, AppInfo.class));
                    finish();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Network problem", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        switch (requestCode)
        {
            case 1:
                if (resultCode == RESULT_OK)
                {
                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    map.add(filePath);

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

                if (resultCode == RESULT_OK)
                {
                    if (CropImage.croppedImage != null)
                    {
                        try
                        {
                            Bitmap bitmap = CropImage.croppedImage;

                            ByteArrayOutputStream out = new ByteArrayOutputStream();

                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                            ByteArrayOutputStream outstream_thumb = new ByteArrayOutputStream();

                            try
                            {
                                int quality = 100;
                                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outstream_thumb);

                                while ((outstream_thumb.size() / 1024) > 180) {
                                    outstream_thumb = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality,
                                            outstream_thumb);
                                    quality = quality - 5;
                                }
                            }
                            catch (Exception e)
                            {
                                Constant.printMsg("Error saving image file: " + e.getMessage());
                            }

                            img_byte = outstream_thumb.toByteArray();

                            String str1 = null;

                            try
                            {
                                str1 = new String(img_byte, "UTF-8");
                            }
                            catch (UnsupportedEncodingException e)
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            Constant.printMsg("Contact Us >> " + str1); // encoding

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] b = baos.toByteArray();
                            String temp = Base64.encodeToString(b, Base64.DEFAULT);
                            Constant.printMsg("Contact Us >>  dfgfg " + temp); // encoding
                            // File file = new File(temp);
                            Constant.mList.add(temp);
                            image_file.add(temp);
                            Constant.printMsg("Contact Us Image File ::: " + image_file + " Map List ::: " + map.toString());

                            CropImage.croppedImage = null;
                        }
                        catch (Exception e)
                        {
                            Constant.printMsg("Contact Us ::: " + e.toString());
                        }
                    }
                }
                break;

            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:

                Constant.printMsg("calledd shot::::::::>>>>>>>>>");

                if (resultCode == RESULT_OK)
                {
                    Constant.printMsg("calledd shot::::::::>>>>>>>>>111");

                    File file = new File(fileUri.getPath());

                    if (file.length() > 26214400)
                    {
                        new AlertManager().showAlertDialog(this, getResources().getString(R.string.imagesize_must_be_smaller), true);
                    }
                    else
                    {
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
                    }
                }
            break;
        }

        Collections.reverse(Constant.mList);
        mAdapter = new ContactAdapter(Contact.this, Constant.mList);
        grid_photo.setAdapter(mAdapter);
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

    private class ImageUploadTask extends AsyncTask<String, Void, String>
    {
        String sResponse = null, resultImage = null;
        ProgressDialog dialogImageUpload;

        protected void onPreExecute()
        {
            super.onPreExecute();

            dialogImageUpload = new ProgressDialog(Contact.this);
            dialogImageUpload.setMessage("Sending Error Report");
            dialogImageUpload.setCancelable(false);
            dialogImageUpload.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                String url = KachingMeConfig.FEEDBACK_USER;
                int mapval = Integer.parseInt(params[0]);
                Bitmap bitmap = decodeFile(map.get(mapval));
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(url);
                entity = new MultipartEntity();

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] data = bos.toByteArray();

                entity.addPart("primaryNo", new StringBody(params[1]));
                entity.addPart("files", new ByteArrayBody(data, "image/jpeg", params[2]));
                entity.addPart("latitude", new StringBody(params[3]));
                entity.addPart("longitude", new StringBody(params[4]));
                entity.addPart("comment", new StringBody(params[5]));

                httpPost.setEntity(entity);

                Constant.printMsg("Contact Us Parameters ::: " + entity.toString());
                Constant.printMsg("Contact Us Parameters ::: " + params[1] + " :: " + params[2] + " :: " + params[3] + " :: " + params[4] + " :: " + params[5]);

                HttpResponse response = httpClient.execute(httpPost, localContext);
                sResponse = EntityUtils.getContentCharSet(response.getEntity());
                System.out.println("Contact Us Response :: " + sResponse);
                resultImage = EntityUtils.toString(response.getEntity());
                Constant.printMsg("Contact Us Entity ::: " + resultImage + " ::: " + response.getEntity());
            }
            catch (Exception e)
            {
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
            return sResponse;
        }

        @Override
        protected void onPostExecute(String sResponse)
        {
            try
            {
                if (dialogImageUpload != null)
                {
                    dialogImageUpload.dismiss();
                }

                if (resultImage.equalsIgnoreCase("SUCCESS"))
                {
                    if (Constant.mList.size() != 0)
                    {
                        Constant.mList.clear();
                    }
                    Toast.makeText(getApplicationContext()," Submitted successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Contact.this, AppInfo.class));
                    finish();
                }
                else
                {
                    if (Constant.mList.size() != 0)
                    {
                        Constant.mList.clear();
                    }
                    Toast.makeText(getApplicationContext()," Network Issue Try after Sometimes.!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Contact.this, AppInfo.class));
                    finish();
                }
            }
            catch (Exception e)
            {
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }

    public Bitmap decodeFile(String filePath)
    {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        final int REQUIRED_SIZE = 1024;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true)
        {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        return bitmap;
    }

    public void onBackPressed()
    {
        super.onBackPressed();

        if (Constant.mList.size() != 0)
        {
            Constant.mList.clear();
        }
        else
        {
            Constant.printMsg("List Empty");
        }
        startActivity(new Intent(Contact.this, AppInfo.class));

        finish();
    }
}
