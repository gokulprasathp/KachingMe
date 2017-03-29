package com.wifin.kachingme.settings;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;

import com.google.gson.Gson;
 import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.adaptors.ContactusAdapter;
import com.wifin.kachingme.pojo.Contact_Us_Dto;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.cropimage.CropImage;

import a_vcard.android.util.Log;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by user on 10/5/2016.
 */
public class ContactUsActivity extends HeaderActivity {

	LinearLayout mAddImgLayout;
	ImageView mAddImg;
	TextView mOptionalText;
	EditText mProblemEdit;
	int height = 0;
	int width = 0;
	ContactusAdapter mAdapter = null;
	GridView grid_view;
	public static final int REQUEST_CODE_CROP_IMAGE = 12,
			CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 99;
	private Uri fileUri;
	byte img_byte[] = null;
	public static Context context;
	String data;
	SharedPreferences sp1;
	String number;
	ArrayList<String> image_file = new ArrayList<String>();
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_contactus);
		ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
		View.inflate(this, R.layout.activity_contactus, vg);
		mHeading.setText("Contact Us");
		mHeaderImg.setVisibility(View.GONE);

		sp1 = PreferenceManager.getDefaultSharedPreferences(this);
		String no = sp1.getString("number", "");
		String country_code = sp1.getString("countrycode", "");
		number = country_code + no;
		context = ContactUsActivity.this;

		initializeVariables();
		screenArrangeMent();
		mAdapter = new ContactusAdapter(ContactUsActivity.this, Constant.mList);

		grid_view.setAdapter(mAdapter);

		mNextBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mProblemEdit.getText().toString().trim().length() > 0) {
					data = jSonFrom();
					new postContactusDetails().execute();
				} else {
					Toast.makeText(getApplicationContext(),
							"Please describe your problem", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});
		mBackBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(ContactUsActivity.this,
						AboutActivity.class);
				startActivity(intent);
				finish();
			}
		});

		mAddImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final CharSequence[] options = { "Gallery" };

				AlertDialog.Builder builder = new AlertDialog.Builder(
						ContactUsActivity.this);
				builder.setTitle("Select Picture");
				builder.setItems(options,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int item) {
								if (options[item].equals("Gallery")) {

									Intent photoPickerIntent = new Intent(
											Intent.ACTION_PICK);
									photoPickerIntent.setType("image/*");
									try {
										startActivityForResult(
												photoPickerIntent, 1);
									} catch (ActivityNotFoundException e) {

									}

								}

							}
						});
				builder.show();

			}
		});

	}

	protected String jSonFrom() {
		// TODO Auto-generated method stub
		String d = null;
		Contact_Us_Dto c = new Contact_Us_Dto();
		c.setDescription(mProblemEdit.getText().toString().trim());
		// for (int i = 0; i < image_file.size(); i++) {
		c.setImageStrings(image_file);
		c.setPhoneNumber(number);

		d = new Gson().toJson(c);

		// }
		Constant.printMsg("json " + d);
		return d;

	}

	public class postContactusDetails extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = null;
			HttpConfig ht = new HttpConfig();

			result = ht.doPostMobizee(data, KachingMeConfig.Contact_Us_URL);

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Constant.printMsg("contactus result res:;" + result);

			if (result != null && result.length() > 0) {
				if (result.toString().trim().equalsIgnoreCase("Reported")) {
					Constant.printMsg("contactus result res:1;" + result);

					Toast.makeText(
							getApplicationContext(),
							"Your problem has beed reported.We will reslove this soon",
							Toast.LENGTH_SHORT).show();
					startActivity(new Intent(ContactUsActivity.this,
							AboutActivity.class));
					finish();

				}
				if (result.toString().trim()
						.equalsIgnoreCase("Primary Number Not Exists")) {
					Constant.printMsg("contactus result res:123;" + result);

					Toast.makeText(getApplicationContext(),
							"Primary Number Not Exists", Toast.LENGTH_SHORT)
							.show();
					startActivity(new Intent(ContactUsActivity.this,
							AboutActivity.class));
					finish();

				}

			} else {
				Toast.makeText(getApplicationContext(), "Network problem",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	public static Context getContext() {
		// TODO Auto-generated method stub
		return context;
	}

	private void screenArrangeMent() {

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		height = metrics.heightPixels;
		width = metrics.widthPixels;

		LinearLayout.LayoutParams addImgLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		addImgLayoutParams.width = width * 90 / 100;
		addImgLayoutParams.height = width * 15 / 100;
		addImgLayoutParams.gravity = Gravity.CENTER;
		mAddImgLayout.setLayoutParams(addImgLayoutParams);

		LinearLayout.LayoutParams problemEditParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		problemEditParams.width = width * 90 / 100;
		problemEditParams.height = width * 30 / 100;
		problemEditParams.topMargin = width * 5 / 100;
		problemEditParams.gravity = Gravity.CENTER;
		mProblemEdit.setPadding(10, 10, 0, 0);
		mProblemEdit.setGravity(Gravity.LEFT | Gravity.TOP);
		mProblemEdit.setLayoutParams(problemEditParams);

		LinearLayout.LayoutParams addImgParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		addImgParams.width = width * 9 / 100;
		addImgParams.height = width * 9 / 100;
		addImgParams.gravity = Gravity.CENTER;
		mAddImg.setLayoutParams(addImgParams);

		LinearLayout.LayoutParams optionalTextParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		optionalTextParams.height = width * 15 / 100;
		optionalTextParams.leftMargin = width * 3 / 100;
		mOptionalText.setGravity(Gravity.CENTER);
		mOptionalText.setLayoutParams(optionalTextParams);

		if (width >= 600) {
			mOptionalText.setTextSize(18);
			mProblemEdit.setTextSize(18);

		} else if (width < 600 && width >= 480) {
			mOptionalText.setTextSize(15);
			mProblemEdit.setTextSize(15);

		} else if (width < 480 && width >= 320) {
			mOptionalText.setTextSize(12);
			mProblemEdit.setTextSize(12);

		} else if (width < 320) {
			mOptionalText.setTextSize(10);
			mProblemEdit.setTextSize(10);

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {

		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
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
			if (resultCode == RESULT_OK) {

				final Bundle extras = imageReturnedIntent.getExtras();

				if (CropImage.croppedImage != null) {
                    try {
                        Bitmap bitmap = CropImage.croppedImage;

                        ByteArrayOutputStream out = new ByteArrayOutputStream();

                        bitmap.compress(CompressFormat.PNG, 100, out);

                        ByteArrayOutputStream outstream_thumb = new ByteArrayOutputStream();
                        try {

                            int quality = 100;
                            bitmap.compress(CompressFormat.JPEG, quality,
                                    outstream_thumb);

                            while ((outstream_thumb.size() / 1024) > 180) {
                                outstream_thumb = new ByteArrayOutputStream();
                                bitmap.compress(CompressFormat.JPEG, quality,
                                        outstream_thumb);
                                quality = quality - 5;
                                // Log.d(TAG,
                                // "Map Image Size::"
                                // + (outstream_thumb.size()) / 1024);
                            }

                        } catch (Exception e) {
                            // ACRA.getErrorReporter().handleException(e);
                            Log.w("TAG",
                                    "Error saving image file: " + e.getMessage());

                        }

                        // Log.d(TAG, "Map Image Size::" + (outstream_thumb.size())
                        // / 1024);

                        img_byte = outstream_thumb.toByteArray();
                        // String str = new String(bytes, "UTF-8"); // for UTF-8
                        String str1 = null;
                        try {
                            str1 = new String(img_byte, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Constant.printMsg("str1 >> " + str1); // encoding

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
                        String temp = Base64.encodeToString(b, Base64.DEFAULT);
                        Constant.printMsg("str1 >>  dfgfg " + temp); // encoding
                        // File file = new File(temp);
                        Constant.mList.add(temp);
                        image_file.add(temp);
                        Constant.printMsg("resuly   " + image_file);

                        CropImage.croppedImage = null;
                    } catch (Exception e) {

                    }

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
		Collections.reverse(Constant.mList);

		mAdapter = new ContactusAdapter(ContactUsActivity.this, Constant.mList);

		grid_view.setAdapter(mAdapter);
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
	}

	private void initializeVariables() {
		mAddImg = (ImageView) findViewById(R.id.add_img);
		mAddImgLayout = (LinearLayout) findViewById(R.id.add_img_layout);
		mProblemEdit = (EditText) findViewById(R.id.problem_text);
		mOptionalText = (TextView) findViewById(R.id.optional_text);
		grid_view = (GridView) findViewById(R.id.grid_photo);

        Constant.typeFace(this,mProblemEdit);
        Constant.typeFace(this,mOptionalText);
    }
}
