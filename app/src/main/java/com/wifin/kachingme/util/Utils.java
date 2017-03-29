package com.wifin.kachingme.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kaching.me.ui.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	public static String TAG = Utils.class.getSimpleName();

    public static boolean autoResponse = false;


    public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {// ACRA.getErrorReporter().handleException(ex);
		}
	}

    public static long  getExternalFreeSpace
            (Context context)
    {
        /*long totalFreeSpace = 0;
        File[] externalStorageFiles= ContextCompat.getExternalFilesDirs(context, null);
        for(int i = 0; i<externalStorageFiles.length; i++)
        {
            totalFreeSpace = totalFreeSpace +  externalStorageFiles[i].getFreeSpace();
            //String formattedResult=android.text.format.Formatter.formatShortFileSize(context,availableSizeInBytes);
        }

        if(totalFreeSpace>0)
        {
            totalFreeSpace = totalFreeSpace/(1024*1024);
        }

        return totalFreeSpace;*/

        return new File(Environment.getExternalStorageDirectory().toString()).getFreeSpace()/(1024*1024);
    }

	public String getMimeType(String url) {

		String extension = url.substring(url.lastIndexOf("."));
		Log.d("Mimetype", "Extention::" + extension);
		String mimeTypeMap = MimeTypeMap.getFileExtensionFromUrl(extension);
		Log.d("Mimetype", "Map::" + mimeTypeMap);
		String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
				mimeTypeMap);
		return mimeType;
	}

	public static Boolean check_prefix(String mobileno, String prefix,
			int PrefixSize) {
		Boolean is_prefix = false;
		if (mobileno.length() > 0) {
			String phone_prefix = mobileno.substring(0, PrefixSize);

			if (phone_prefix.equals(prefix))
				is_prefix = true;
		}
		return is_prefix;
	}

    public static boolean isActivityIsFront(Context context, String conicalClassName)
    {


        try {
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
            ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
            Constant.printMsg("isActivityIsFront  : " + ar.topActivity.getClassName().toString()+"  "+  conicalClassName);
            if(ar.topActivity.getClassName().toString().equalsIgnoreCase(conicalClassName))
            {
                return true;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return false;
    }


	public static void Copyfile(File src, File dst) throws IOException {
		FileInputStream inStream = new FileInputStream(src);
		FileOutputStream outStream = new FileOutputStream(dst);
		FileChannel inChannel = inStream.getChannel();
		FileChannel outChannel = outStream.getChannel();
		inChannel.transferTo(0, inChannel.size(), outChannel);
		inStream.close();
		outStream.close();
	}

	public static String getDisplayName(DatabaseHelper dbAdapter, String jid) {
		String name = dbAdapter.getDisplay_name(jid);

		if (name == null) {
			name = jid.split("@")[0];
		}
		return name;
	}

	public byte[] getGroupRandomeIcon(Context context) {
		int[] res = { R.drawable.grp_1, R.drawable.grp_2, R.drawable.grp_3,
				R.drawable.grp_4, R.drawable.grp_5 };
		Random rand = new Random();
		int rndInt = rand.nextInt(res.length);
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				res[rndInt]);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();

		return byteArray;
	}

	public boolean emailValidator(String email) {
		Pattern pattern;
		Matcher matcher;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static String getBookmarkTime() {
		// (1) get today's date

		// Date today = Calendar.getInstance().getTime();
		Calendar c = Calendar.getInstance(/* TimeZone.getTimeZone("UTC") */);
		Log.d("", "UTC::" + c.getTimeInMillis());
		Date today = new Date();
		// (2) create our date "formatter" (the date format we want)
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
		date.setTimeZone(TimeZone.getTimeZone("GMT"));
		time.setTimeZone(TimeZone.getTimeZone("GMT"));

		// (3) create a new String using the date format we want
		String str_date = date.format(c.getTime());
		String str_time = time.format(c.getTime());

		// (4) this prints "Folder Name = 2009-09-06-08.23.23"
		Constant.printMsg("Folder Name = " + str_date + "T" + str_time + "Z");
		return str_date + "T" + str_time + "Z";
	}

	public static Date getBookmarkDate(String dateString) {

		Date date1 = null;
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		date.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			String str_date = dateString.split("T")[0];
			String str_time = dateString.split("T")[1].substring(0, 7);
			date1 = date.parse(str_date + "-" + str_time);
		} catch (ParseException e) {

		}
		return date1;
	}

	public static String DecryptMessage(String encrypted) {
//		JNCryptor cryptor = new AES256JNCryptor();
//		String str1 = null;
//		try {
//			byte[] decypted_msg = cryptor.decryptData(
//					Base64.decode(encrypted, Base64.NO_WRAP),
//					Constant.ENCRYPTION_KEY.toCharArray());
//			str1 = new String(decypted_msg, "UTF-8");
//			return str1;
//		} catch (InvalidHMACException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (CryptorException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return str1;
		
		return encrypted;
	}

	public static String EncryptMessage(String message) {
//		JNCryptor cryptor = new AES256JNCryptor();
//		byte[] plaintext = message.getBytes();
//		byte[] ciphertext = null;
//
//		try {
//			ciphertext = cryptor.encryptData(plaintext,
//					Constants.ENCRYPTION_KEY.toCharArray());
//		} catch (CryptorException e) {
//			// Something went wrong
//			e.printStackTrace();
//		}
//
//		// new String(Base64.encode(ciphertext, Base64.NO_WRAP));
//		return Base64.encodeToString(ciphertext, Base64.NO_WRAP);
		
		return message;
	}

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	/** Create a file Uri for saving an image or video */
	public static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type) {

		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(Constant.local_image_dir);
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	public static Bitmap loadBitmapFromView(Context context, View v) {
		/*
		 * DisplayMetrics dm = context.getResources().getDisplayMetrics();
		 * v.measure(MeasureSpec.makeMeasureSpec(dm.widthPixels,
		 * MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(dm.heightPixels,
		 * MeasureSpec.EXACTLY)); v.layout(0, 0, v.getMeasuredWidth(),
		 * v.getMeasuredHeight()); Bitmap returnedBitmap =
		 * Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(),
		 * Bitmap.Config.ARGB_8888); Canvas c = new Canvas(returnedBitmap);
		 * v.draw(c);
		 */

		/*
		 * DisplayMetrics dm = context.getResources().getDisplayMetrics();
		 * v.measure(MeasureSpec.makeMeasureSpec(dm.widthPixels,
		 * MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(dm.heightPixels,
		 * MeasureSpec.EXACTLY)); v.layout(0, 0, v.getMeasuredWidth(),
		 * v.getMeasuredHeight());
		 */
		Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
				v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(returnedBitmap);
		v.draw(c);

		return returnedBitmap;
	}

	public static void takeScreen(View view, Context context) {
		Bitmap bitmap = loadBitmapFromView(context, view); // get Bitmap from
															// the view
		String mPath = Environment.getExternalStorageDirectory()
				+ File.separator + "screen_" + System.currentTimeMillis()
				+ ".jpeg";
		File imageFile = new File(mPath);
		OutputStream fout = null;
		try {
			fout = new FileOutputStream(imageFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
			fout.flush();
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setHasEmbeddedTabs(Object inActionBar,
			final boolean inHasEmbeddedTabs) {
		// get the ActionBar class
		Class<?> actionBarClass = inActionBar.getClass();

		// if it is a Jelly Bean implementation (ActionBarImplJB), get the super
		// class (ActionBarImplICS)
		if ("android.support.v7.app.ActionBarImplJB".equals(actionBarClass
				.getName())) {
			actionBarClass = actionBarClass.getSuperclass();
		}

		// if Android 4.3 >
		if ("android.support.v7.app.ActionBarImplJBMR2".equals(actionBarClass
				.getName())) {
			actionBarClass = actionBarClass.getSuperclass().getSuperclass();
		}

		try {
			// try to get the mActionBar field, because the current ActionBar is
			// probably just a wrapper Class
			// if this fails, no worries, this will be an instance of the native
			// ActionBar class or from the ActionBarImplBase class
			final Field actionBarField = actionBarClass
					.getDeclaredField("mActionBar");
			actionBarField.setAccessible(true);
			inActionBar = actionBarField.get(inActionBar);
			actionBarClass = inActionBar.getClass();
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (NoSuchFieldException e) {
		}

		try {
			// now call the method setHasEmbeddedTabs, this will put the tabs
			// inside the ActionBar
			// if this fails, you're on you own <img class="wp-smiley" alt=";-)"
			// src="http://www.blogc.at/wp-includes/images/smilies/icon_wink.gif">
			final Method method = actionBarClass.getDeclaredMethod(
					"setHasEmbeddedTabs", new Class[] { Boolean.TYPE });
			method.setAccessible(true);
			method.invoke(inActionBar, new Object[] { inHasEmbeddedTabs });
		} catch (NoSuchMethodException e) {
		} catch (InvocationTargetException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		}
	}
}