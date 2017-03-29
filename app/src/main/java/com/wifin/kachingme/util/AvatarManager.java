/**
 * Copyright (c) 2013, Redsolution LTD. All rights reserved.
 * 
 * This file is part of Xabber project; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License, Version 3.
 * 
 * Xabber is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * along with this program. If not, see http://www.gnu.org/licenses/.
 */
package com.wifin.kachingme.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Provides information about avatars (hashes and values). Store and retrieve
 * hashes from database and binary values from file system. Caches user's hashes
 * and avatar's values in memory. Handles changes in user's hashes. Requests
 * information from server when avatar for given hash don't exists locally.
 * 
 * <p>
 * This class is thread safe. All operation modification made from synchronized
 * blocks.
 * 
 * <p>
 * All requests to database / file system made in background thread or on
 * application load.
 * 
 * @author alexander.ivanov
 */
public class AvatarManager {

	/**
	 * Maximum image width / height to be loaded.
	 */
	public static final int MAX_SIZE = 265;

	public static final String EMPTY_HASH = "";
	public static final Bitmap EMPTY_BITMAP = Bitmap.createBitmap(1, 1,
			Bitmap.Config.ALPHA_8);

	/**
	 * Make {@link Bitmap} from array of bytes.
	 * 
	 * @param value
	 * @return Bitmap. <code>null</code> can be returned if value is invalid or
	 *         is <code>null</code>.
	 */
	public Bitmap makeBitemap(byte[] value) {
		if (value == null)
			return null;

		// Load only size values
		BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
		sizeOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(value, 0, value.length, sizeOptions);

		// Calculate factor to down scale image
		/*
		 * int scale = 1; int width_tmp = sizeOptions.outWidth; int height_tmp =
		 * sizeOptions.outHeight; while (width_tmp / 2 >= MAX_SIZE && height_tmp
		 * / 2 >= MAX_SIZE) { scale *= 2; width_tmp /= 2; height_tmp /= 2; }
		 * 
		 * // Load image BitmapFactory.Options resultOptions = new
		 * BitmapFactory.Options(); resultOptions.inSampleSize = scale;
		 */
		return BitmapFactory.decodeByteArray(value, 0, value.length,
				sizeOptions);

	}

	public byte[] saveBitemap(byte[] value) {
		if (value == null)
			return null;

		// Load only size values
		BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
		sizeOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(value, 0, value.length, sizeOptions);

		// Log.d("Avatar Manager","Height::"+
		// sizeOptions.outHeight+"Width::"+sizeOptions.outWidth);
		// Calculate factor to down scale image
		int scale = 1;
		int width_tmp = sizeOptions.outWidth;
		int height_tmp = sizeOptions.outHeight;
		while (width_tmp / 2 >= MAX_SIZE && height_tmp / 2 >= MAX_SIZE) {
			scale *= 2;
			width_tmp /= 2;
			height_tmp /= 2;
		}

		// Load image
		BitmapFactory.Options resultOptions = new BitmapFactory.Options();
		resultOptions.inSampleSize = scale;

		Log.d("Avatar Manager", "Scale Size::" + scale);
		// Log.d("Avatar Manager","::"+scale);
		Bitmap bitmap = BitmapFactory.decodeByteArray(value, 0, value.length,
				resultOptions);

		return convertBitmapToByteArray(bitmap);
	}

	private static byte[] convertBitmapToByteArray(Bitmap bitmap) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream(
				bitmap.getWidth() * bitmap.getHeight());
		bitmap.compress(CompressFormat.PNG, 100, buffer);
		return buffer.toByteArray();
	}

	public Bitmap roundCornerImage(Bitmap src, float round) {
		// Source image size
		int width = src.getWidth();
		int height = src.getHeight();

        System.gc();


        int ScaleSize = 250;//max Height or width to Scale

        float excessSizeRatio =  width > height ? (float)( (float)width / (float)ScaleSize) : (float)((float)height / (float)ScaleSize);

        //mBitmap.recycle(); if you are not using mBitmap Obj


        Bitmap result = Bitmap.createBitmap((int) (width/excessSizeRatio),(int) (height/excessSizeRatio), Config.RGB_565);


		// set canvas for painting
		Canvas canvas = new Canvas(result);
		canvas.drawARGB(0, 0, 0, 0);

		// configure paint
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);

		// configure rectangle for embedding
		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);

		// draw Round rectangle to canvas
		canvas.drawRoundRect(rectF, round, round, paint);

		// create Xfer mode
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// draw source image to canvas
		canvas.drawBitmap(src, rect, rect, paint);

		// return final image
		return result;
	}

	public byte[] saveThumb(String value) {
		if (value == null)
			return null;

		// Load only size values
		BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
		sizeOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(value, sizeOptions);

		// Log.d("Avatar Manager","Height::"+
		// sizeOptions.outHeight+"Width::"+sizeOptions.outWidth);
		// Calculate factor to down scale image
		int scale = 1;
		int width_tmp = sizeOptions.outWidth;
		int height_tmp = sizeOptions.outHeight;
		while (width_tmp / 2 >= 96 && height_tmp / 2 >= 96) {
			scale *= 2;
			width_tmp /= 2;
			height_tmp /= 2;
		}

		// Load image
		BitmapFactory.Options resultOptions = new BitmapFactory.Options();
		resultOptions.inSampleSize = scale;

		Log.d("Avatar Manager", "Scale Size::" + scale);
		// Log.d("Avatar Manager","::"+scale);
		Bitmap bitmap = BitmapFactory.decodeFile(value, resultOptions);

		return convertBitmapToByteArray(bitmap);
	}

	public byte[] compress_image(byte[] image_date) {

		byte[] image_byte = image_date;
		int m = 100;
		while ((image_byte.length / 1024) >= 500) {
			Bitmap bmp = BitmapFactory.decodeByteArray(image_byte, 0,
					image_byte.length);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.PNG, m, stream);
			m--;
			image_byte = stream.toByteArray();
		}

		return image_byte;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(String path,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

}
