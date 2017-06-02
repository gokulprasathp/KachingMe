package com.wifin.kachingme.util;

import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;

public class GIFView extends View {
	public Movie mMovie;

	public long movieStart;

	// Constant.gifimage =
	// "/storage/sdcard0/Kaching.me/ProfilePictures/butter.gif";

	public GIFView(Context context) {

		super(context);
		Constant.printMsg("type of wallpaper gifView  " + Constant.wallType);

		if (Constant.wallType != null) {
			if (Constant.wallType.equalsIgnoreCase("file")) {
				initializeViewFile();

			} else {
				initializeView();

			}
		} else {
			initializeView();
		}
	}

	public GIFView(Context context, AttributeSet attrs) {

		super(context, attrs);

		if (Constant.wallType != null) {

			if (Constant.wallType.equalsIgnoreCase("file")) {
				initializeViewFile();

			} else {
				initializeView();

			}
		} else {
			initializeView();

		}
	}

	public GIFView(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);
		if (Constant.wallType != null) {

			if (Constant.wallType.equalsIgnoreCase("file")) {
				initializeViewFile();

			} else {
				initializeView();

			}
		} else {
			initializeView();

		}
	}

	private void initializeView() {

		// R.drawable.loader - our animated GIF
		InputStream is = null;
		String wall = KachingMeApplication.getsharedpreferences().getString(
				"wallpaper", "");
		Constant.printMsg("not file ::::::: ");

		if (wall.equalsIgnoreCase("blue")) {
			is = getContext().getResources().openRawResource(+
					R.drawable.ic_gif_one);
			mMovie = Movie.decodeStream(is);

		}
		if (wall.equalsIgnoreCase("birds")) {
			is = getContext().getResources().openRawResource(+ R.drawable.ic_gif_two);
			mMovie = Movie.decodeStream(is);

		}
		if (wall.equalsIgnoreCase("nature")) {
			is = getContext().getResources().openRawResource(+ R.drawable.ic_gif_three);
			mMovie = Movie.decodeStream(is);
		}
		if (wall.equalsIgnoreCase("sunset")) {
			is = getContext().getResources().openRawResource(+ R.drawable.ic_gif_four);
			mMovie = Movie.decodeStream(is);

		}
		if (wall.equalsIgnoreCase("earth_rotation")) {
			is = getContext().getResources().openRawResource(+
					R.drawable.ic_gif_five);

		}/*
		if (wall.equalsIgnoreCase("smiley")) {
			is = getContext().getResources().openRawResource(+ R.drawable.smiley);
			mMovie = Movie.decodeStream(is);

		}
		if (wall.equalsIgnoreCase("love")) {
			is = getContext().getResources().openRawResource(+ R.drawable.love);
			mMovie = Movie.decodeStream(is);

		}
		if (wall.equalsIgnoreCase("elephant")) {
			is = getContext().getResources().openRawResource(+
					R.drawable.elephant);
			mMovie = Movie.decodeStream(is);

		}
		if (wall.equalsIgnoreCase("cat")) {
			is = getContext().getResources().openRawResource(+ R.drawable.cat);
			mMovie = Movie.decodeStream(is);

		}*/

	}

	private void initializeViewFile() {
		// TODO Auto-generated method stub
		if (KachingMeApplication.getsharedpreferences().contains("wallpaper")) {
			File f = new File(KachingMeApplication.getsharedpreferences()
					.getString("wallpaper", null));
			Constant.printMsg("paththththth ::::: >>>>>>> "
					+ f.getAbsolutePath());
			// 05-02 10:50:10.298: I/System.out(5863): Kaching
			// me::::::::::::paththththth ::::: >>>>>>>
			// /storage/sdcard0/Kaching.me/ProfilePictures/butter.gif
			String pathName = f.getAbsolutePath();
			Constant.gifimage = pathName;
			// R.drawable.loader - our animated GIF

			// File file = new File(Constant.gifimage);
			// Constant.printMsg("file   " + file);
			// InputStream is = null;
			// String pathName = file.getAbsolutePath();
			// / {
			// is = new FileInputStream(file);
			Constant.printMsg("file into try  " + pathName + "   ");
			// mMovie.decodeStream(is);
			mMovie = Movie.decodeFile(pathName);

			// } catch (FileNotFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		} else {
			// InputStream is = getContext().getResources().openRawResource(
			// R.drawable.rotate_earth);
			//
			// mMovie = Movie.decodeStream(is);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			// Do something for lollipop and above versions
		} else {
			// do something for phones running an SDK before lollipop
		}

		Constant.printMsg("marsh mallow  version " + currentapiVersion);

		canvas.drawColor(Color.TRANSPARENT);

		super.onDraw(canvas);

		long now = android.os.SystemClock.uptimeMillis();

		if (movieStart == 0) {

			movieStart = now;
		}

		if (mMovie != null) {
			Constant.printMsg("called this movie" + mMovie + " " + getWidth()
					+ "   " + getHeight());
			int relTime = (int) ((now - movieStart) % mMovie.duration());

			if (currentapiVersion >= 23) {
//				canvas.scale(5.0f, 6.0f);

			} else {
//				canvas.scale(3.0f, 4.0f);

			}
			canvas.scale(2.0f, 2.0f);
			mMovie.setTime(relTime);

			mMovie.draw(canvas, 0, 0);
			// mMovie.setTime(relTime);
			// mMovie.draw(canvas, 100, 100);
			this.invalidate();

		}

	}

	private int gifId;

	public void setGIFResource(int resId) {

		this.gifId = resId;
		initializeViewFile();
		// initializeView();

	}

	public int getGIFResource() {

		return this.gifId;

	}
}
