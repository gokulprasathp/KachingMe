package com.wifin.kachingme.chat.muc_chat;

import java.io.ByteArrayOutputStream;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.SherlockBaseActivity;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.AvatarManager;
import com.wifin.kachingme.util.Constant;

public class NewGroup_Name extends SherlockBaseActivity {

	ImageView img_avatar;
	TextView txt_subject;
	byte[] avatar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newgroup_name);

		img_avatar = (ImageView) findViewById(R.id.img_avatar);
		txt_subject = (TextView) findViewById(R.id.txt_grp_name);
        Constant.typeFace(this,txt_subject);
		/*
		 * int[] res = {R.drawable.grp_1,
		 * R.drawable.grp_2,R.drawable.grp_3,R.drawable.grp_4,R.drawable.grp_5};
		 * Random rand = new Random(); int rndInt = rand.nextInt(res.length);
		 * 
		 * Log.d("Randome","Randome Image ID::"+rndInt); Bitmap bitmap =
		 * BitmapFactory.decodeResource(context.getResources(), res[rndInt]);
		 */

		img_avatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				try {
					startActivityForResult(photoPickerIntent, 1);
				} catch (ActivityNotFoundException e) {
					AlertDialog.Builder b = new AlertDialog.Builder(
							NewGroup_Name.this);
					b.setMessage(
							"It seems like you don't have image gallery application to select the image!")
							.setCancelable(false);
					b.setNegativeButton("ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
					AlertDialog alert = b.create();
					alert.show();
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add("Next")

		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (txt_subject.getText().toString().equals("")) {
			new AlertManager().showAlertDialog(this,
					"Please enter room subject", true);
		} else {
			Intent intent = new Intent(this, NewGroup.class);
			intent.putExtra("subject", txt_subject.getText().toString());
			intent.putExtra("avatar", avatar);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

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

				Bitmap b = decodeSampledBitmapFromResource(filePath, 150, 150);
				Bitmap thumb = ThumbnailUtils.extractThumbnail(b, 150, 150);
				int orientation = getOrientation(this, selectedImage);
				Matrix mat = new Matrix();
				mat.postRotate(orientation);
				thumb = Bitmap.createBitmap(thumb, 0, 0, thumb.getWidth(),
						thumb.getHeight(), mat, true);

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				thumb.compress(Bitmap.CompressFormat.PNG, 100, out);

				// Constant.printMsg("Before::"+out.toByteArray().length);
				avatar = new AvatarManager().saveBitemap(out.toByteArray());
				// Constant.printMsg("Before::"+out.toByteArray().length);

				img_avatar.setImageBitmap(thumb);

			}
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
				new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
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

}
