/*
* @author Sivanesan
*
* @usage -  This class for Freebie Dazz
*
* */
package com.wifin.kachingme.registration_and_login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.util.Constant;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StartUpDazz extends Activity implements AnimationListener {
    ImageView web, web1, web2, web3, web4, web5, web6, web7, web8, web9;
    TextView marqueeText, wtext,  wtext1,  wtext2,
             wtext3, wtext4;
    Animation animFadein;
    private ImageLoadingListener animateFirstListener = new FreebieActivity.AnimateFirstDisplayListener();
    private DisplayImageOptions options;
    LinearLayout ly;
    int count = 1000;
    Dbhelper db;
    SharedPreferences sharedpreferences, preferences;
    Editor editor;
    int listposition, spinnerposition;
    String mCompany;
    Handler handler = new Handler();
    Runnable run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        db = new Dbhelper(getApplicationContext());

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.stub).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();

        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(this));
        initialization();
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences = getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        setActivitystage("WelcomeActivity");

        listposition = sharedpreferences.getInt("listpos", 0); // 2
        spinnerposition = sharedpreferences.getInt("spinnerpos", 0); // 2
        mCompany = sharedpreferences.getString("company", "");
        Constant.printMsg("posss::::::::>>>>>>" + listposition
                + spinnerposition + "   "
                + Constant.freelistmain.get(listposition).getCompanyName());
        ImageLoader.getInstance().displayImage(
                String.valueOf(
                        Constant.freelistmain.get(listposition)
                                .getCompanyLogoPath()).replaceAll(" ", "%20"),
                web, options, animateFirstListener);
        ImageLoader.getInstance().displayImage(
                String.valueOf(
                        Constant.freelistmain.get(listposition)
                                .getCompanyLogoPath()).replaceAll(" ", "%20"),
                web2, options, animateFirstListener);
        ImageLoader.getInstance().displayImage(
                String.valueOf(
                        Constant.freelistmain.get(listposition)
                                .getCompanyLogoPath()).replaceAll(" ", "%20"),
                web5, options, animateFirstListener);
        ImageLoader.getInstance().displayImage(
                String.valueOf(
                        Constant.freelistmain.get(listposition)
                                .getCompanyLogoPath()).replaceAll(" ", "%20"),
                web6, options, animateFirstListener);
        ImageLoader.getInstance().displayImage(
                String.valueOf(
                        Constant.freelistmain.get(listposition)
                                .getCompanyLogoPath()).replaceAll(" ", "%20"),
                web8, options, animateFirstListener);

        marqueeText.setVisibility(View.GONE);
        marqueeText.setText(Constant.freelistmain.get(listposition)
                .getFreebilist().get(spinnerposition).getTagLine());

        wtext.setText(Constant.freelistmain.get(listposition).getFreebilist()
                .get(spinnerposition).getTagLine());
        wtext1.setText(Constant.freelistmain.get(listposition).getFreebilist()
                .get(spinnerposition).getTagLine());
        wtext2.setText(Constant.freelistmain.get(listposition).getFreebilist()
                .get(spinnerposition).getTagLine());
        wtext3.setText(Constant.freelistmain.get(listposition).getFreebilist()
                .get(spinnerposition).getTagLine());
        wtext4.setText(Constant.freelistmain.get(listposition).getFreebilist()
                .get(spinnerposition).getTagLine());
        Constant.typeFace(StartUpDazz.this, wtext);
        Constant.typeFace(StartUpDazz.this, wtext1);
        Constant.typeFace(StartUpDazz.this, wtext2);
        Constant.typeFace(StartUpDazz.this, wtext3);
        Constant.typeFace(StartUpDazz.this, wtext4);
        ImageLoader.getInstance().displayImage(
                String.valueOf(
                        Constant.freelistmain.get(listposition).getFreebilist()
                                .get(spinnerposition).getPhotoPath())
                        .replaceAll(" ", "%20"), web1, options,
                animateFirstListener);
        ImageLoader.getInstance().displayImage(
                String.valueOf(
                        Constant.freelistmain.get(listposition).getFreebilist()
                                .get(spinnerposition).getPhotoPath())
                        .replaceAll(" ", "%20"), web3, options,
                animateFirstListener);
        ImageLoader.getInstance().displayImage(
                String.valueOf(
                        Constant.freelistmain.get(listposition).getFreebilist()
                                .get(spinnerposition).getPhotoPath())
                        .replaceAll(" ", "%20"), web4, options,
                animateFirstListener);
        ImageLoader.getInstance().displayImage(
                String.valueOf(
                        Constant.freelistmain.get(listposition).getFreebilist()
                                .get(spinnerposition).getPhotoPath())
                        .replaceAll(" ", "%20"), web7, options,
                animateFirstListener);
        ImageLoader.getInstance().displayImage(
                String.valueOf(
                        Constant.freelistmain.get(listposition).getFreebilist()
                                .get(spinnerposition).getPhotoPath())
                        .replaceAll(" ", "%20"), web9, options,
                animateFirstListener);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.movestartup);

        // set animation listener
        animFadein.setAnimationListener(this);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(calendar.getTime());
        Editor editor = preferences.edit();
        editor.putInt("scrollValue", preferences.getInt("scrollValue", 0) + 1);
        editor.putString("scrollDate", currentDate);
        editor.commit();

        ly.startAnimation(animFadein);
        animFadein.setFillAfter(true);
        handler.postDelayed(run = new Runnable() {
            public void run() {
                ly.clearAnimation();
                // animFadein.setFillAfter(true);
                if (Constant.mFrebieScroll == true) {
                    Constant.printMsg("Freeeee redirect Slider....");
                    Intent intent = new Intent(StartUpDazz.this,
                            SliderTesting.class);
                    startActivity(intent);
                    finish();
                } else {
                    Constant.printMsg("Freeeee redirect WelcomeActivity....");
                    Intent intent = new Intent(StartUpDazz.this,
                            WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, count * 20);

    }

    private void initialization() {
        web = (ImageView) findViewById(R.id.web_st);
        web1 = (ImageView) findViewById(R.id.web_st1);
        web2 = (ImageView) findViewById(R.id.web_st2);
        web3 = (ImageView) findViewById(R.id.web_st11);
        web5 = (ImageView) findViewById(R.id.web_st3);
        web4 = (ImageView) findViewById(R.id.web_st22);
        web6 = (ImageView) findViewById(R.id.web_st4);
        web7 = (ImageView) findViewById(R.id.web_st33);
        web8 = (ImageView) findViewById(R.id.web_st5);
        web9 = (ImageView) findViewById(R.id.web_st44);
        marqueeText = (TextView) findViewById(R.id.scrolltext_st);
        wtext = (TextView) findViewById(R.id.webtext_st);
        wtext1 = (TextView) findViewById(R.id.webtext_st1);
        wtext2 = (TextView) findViewById(R.id.webtext_st2);
        wtext3 = (TextView) findViewById(R.id.webtext_st3);
        wtext4 = (TextView) findViewById(R.id.webtext_st4);
        ly = (LinearLayout) findViewById(R.id.startlay);

        Constant.typeFace(this, marqueeText);
        Constant.typeFace(this, wtext);
        Constant.typeFace(this, wtext1);
        Constant.typeFace(this, wtext2);
        Constant.typeFace(this, wtext3);
        Constant.typeFace(this, wtext4);
    }

    private void setActivitystage(String activity) {
        // TODO Auto-generated method stub
        editor = sharedpreferences.edit();
        editor.putString("activity_name", activity);

        editor.commit();
    }

    private void screenArrangement() {
        // TODO Auto-generated method stub
        if (getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int height = metrics.heightPixels;
            int width = metrics.widthPixels;

            float density = getResources().getDisplayMetrics().density;
            float heightinDP = metrics.heightPixels / density;
            float widthinDP = metrics.widthPixels / density;

            LinearLayout.LayoutParams Textparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            Textparams.width = width * 23 / 100;
            Textparams.height = height * 80 / 100;
            Textparams.gravity = Gravity.CENTER;
            web.setLayoutParams(Textparams);
            web1.setLayoutParams(Textparams);

            LinearLayout.LayoutParams Textparams1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            // Textparams1.width = width * 26 / 100;
            // Textparams1.height = height * 80 / 100;
            // Textparams1.topMargin = (int) (height * 0.8 / 100);
            Textparams1.gravity = Gravity.CENTER;
            marqueeText.setLayoutParams(Textparams1);
            marqueeText.setGravity(Gravity.CENTER);
            wtext.setLayoutParams(Textparams1);
            wtext.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams layp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            // layp.width = width;
            // layp.height = height;
            ly.setLayoutParams(layp);

            if (widthinDP >= 600) {

                marqueeText.setTextSize(60);
                wtext.setTextSize(60);

            } else if (widthinDP > 501 && widthinDP < 600) {

                marqueeText.setTextSize(55);
                wtext.setTextSize(55);

            } else if (widthinDP > 331 && widthinDP < 500) {

                marqueeText.setTextSize(50);
                wtext.setTextSize(50);

            } else if (widthinDP > 260 && widthinDP < 330) {

                marqueeText.setTextSize(45);
                wtext.setTextSize(45);

            } else if (widthinDP <= 260) {

                marqueeText.setTextSize(40);
                wtext.setTextSize(40);

            }

        } else {

        }

    }

    public int getScreenOrientation() {
        Display getOrient = getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if (getOrient.getWidth() == getOrient.getHeight()) {
            orientation = Configuration.ORIENTATION_SQUARE;
        } else {
            if (getOrient.getWidth() < getOrient.getHeight()) {
                orientation = Configuration.ORIENTATION_PORTRAIT;
            } else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        // Thread.abort = true;
        Thread.interrupted();
        super.onDestroy();
    }

    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(run);
    }
}
