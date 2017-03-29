package com.wifin.kachingme.kaching_feature.dazz;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.SimpleGestureFilter;
import com.wifin.kachingme.util.SimpleGestureFilter.SimpleGestureListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BannerActivityLED extends Activity implements OnTouchListener,
        OnTouchModeChangeListener, SimpleGestureListener {

    public static String Value = "";
    public static String mZzleText = "";
    public static String mZzleTexTime = "";
    public static String mZzleTextBackground = "";
    public static String mZzleTextSpeed = "";
    public static String mZzleTextSize = "";
    public MyGameView mGameView;
    Dialog dialog;
    // Spinner $SizeAnalyz;
    ArrayList<String> sizeList = new ArrayList<String>();
    Handler handler = new Handler();
    Runnable run;
    int count = 1000;
    Dbhelper db;
    // private EditText $input;
    private int lengthPM;
    private int moveX;
    private int moveYFixed;
    private Paint paint;
    private int previewHeight;
    // Button $Submit, $Cancel, $Pause, $Resume;
    private int previewWidth;
    // TextWatcher printMessageWatcher;
    private int screen7over8;
    private int screenHeight;
    private int screenWidth;
    private SeekBar speedbar;
    /**
     * for an display ananlysis
     */
    private String printMessage;
    private String backroundColor;
    private String inputTextColor;
    private int movementType;
    private boolean isBlinker;
    private int speedText;
    private int movementDeltaX;
    private float textSizeRatio;
    private float zeroPointValue;
    private SimpleGestureFilter detector;
    public static BannerActivityLED mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        LinearLayout.LayoutParams paramlinear;
        super.onCreate(savedInstanceState);
        this.mGameView = new MyGameView(this);
        mActivity=this;
        Window win = getWindow();
        requestWindowFeature(1);
        win.setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT,
                AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        win.setContentView(this.mGameView);
        LinearLayout linear = (LinearLayout) ((LayoutInflater) getSystemService("layout_inflater"))
                .inflate(R.layout.activity_banner, null);
        db = new Dbhelper(getApplicationContext());

        detector = new SimpleGestureFilter(this, this);

        if (VERSION.SDK_INT < 8) {
            paramlinear = new LinearLayout.LayoutParams(-1, -1);
        } else {
            paramlinear = new LinearLayout.LayoutParams(-1, -1);
        }
        win.addContentView(linear, paramlinear);
        win.addFlags(AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.screenWidth = metrics.widthPixels;
        this.screenHeight = metrics.heightPixels;
        this.previewWidth = this.screenWidth;
        this.previewHeight = this.screenHeight;
        // (int) (((double) this.screenWidth) *
        // (((double) this.screenWidth) / ((double) this.screenHeight)));
        this.screen7over8 = this.screenWidth;
        // (int) (((double) this.screenHeight) * 0.875d);
        intilization();

//		String query = "select * from " + Dbhelper.TABLE_LED + " where msg = '"
//				+ mZzleText + "'";
//		callZzleDB(query);
        Constant.mPreviewSpeed = mZzleTextSpeed;
        Constant.mPreviewTextsize = mZzleTextSize;
        Constant.shapeselected = mZzleTextBackground;
//		Constant.printMsg("query   " + query);
        Constant.printMsg("preview data " + Constant.mPreviewSpeed + "   "
                + Constant.mPreviewTextsize + "   " + mZzleText);

        sizeList.add("Select");
        sizeList.add("2x2");
        sizeList.add("4x4");
        sizeList.add("6x6");
        sizeList.add("8x8");

        Value = mZzleText;

        // handler.postDelayed(run = new Runnable() {
        // public void run() {

        Constant.printMsg("width of screen" + screenWidth);
        SharedPreferences pref = getSharedPreferences("SaveSate", 0);

        printMessage = Value;
        for (int i = 0; i < 50; i++) {
            printMessage = Value + "  " + printMessage;
        }

        inputTextColor = pref.getString("inputtextcolor", "#00FF00");
        backroundColor = pref.getString("backroundcolor", "#292929");
        movementType = pref.getInt("movementtype", 2);
        isBlinker = pref.getBoolean("isblinker", false);
        if (Constant.mPreviewSpeed.length() > 0
                && Constant.mPreviewTextsize.length() > 0) {

            if (Constant.mPreviewSpeed.equalsIgnoreCase("slow")) {
                speedText = pref.getInt("speedtext", 100);
                Constant.printMsg("slow");
            }
            if (Constant.mPreviewSpeed.equalsIgnoreCase("medium")) {

                speedText = pref.getInt("speedtext", 70);
                Constant.printMsg("medium");

            }
            if (Constant.mPreviewSpeed.equalsIgnoreCase("fast")) {
                speedText = pref.getInt("speedtext", 50);
                Constant.printMsg("fast");

            }

            if (Constant.mPreviewTextsize.equalsIgnoreCase("small")) {
                textSizeRatio = pref.getFloat("textsizeratio", 0.7f);
                zeroPointValue = textSizeRatio * 2.3f;
                Constant.printMsg("slow1");

            }
            if (Constant.mPreviewTextsize.equalsIgnoreCase("medium")) {

                textSizeRatio = pref.getFloat("textsizeratio", 0.7f);
                zeroPointValue = textSizeRatio * 3.2f;
                Constant.printMsg("medium1");

            }
            if (Constant.mPreviewTextsize.equalsIgnoreCase("large")) {
                textSizeRatio = pref.getFloat("textsizeratio", 0.7f);
                zeroPointValue = textSizeRatio * 3.8f;
                Constant.printMsg("fast1");

            }
            if (Constant.shapeselected.equalsIgnoreCase("Circle")) {

            }
            if (Constant.shapeselected.equalsIgnoreCase("Star")) {

            }
            if (Constant.shapeselected.equalsIgnoreCase("Heart")) {

            }
            movementDeltaX = (int) (((float) previewWidth) / (((float) speedText) / (((float) screenHeight) / ((float) previewWidth))));

            mGameView.resume();
        }
    }

    private void intilization() {
        // TODO Auto-generated method stub
        // $Submit = (Button) findViewById(R.id.button_submit);
        // $input = (EditText) findViewById(R.id.input);
        // $Cancel = (Button) findViewById(R.id.button_cancel);
        // $Pause = (Button) findViewById(R.id.button_pause);
        // $Resume = (Button) findViewById(R.id.button_resume);
        // $SizeAnalyz = (Spinner) findViewById(R.id.spinner_size);
    }

    public void onPause() {
        super.onPause();
        this.mGameView.pause();
    }

    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
//		startActivity(new Intent(BannerActivityLED.this, ChatTest.class));
        finish();
        return true;
    }

    @Override
    public void onTouchModeChanged(boolean isInTouchMode) {
        // TODO Auto-generated method stub
        // startActivity(new Intent(BannerActivityLED.this, Chat.class));
        finish();
    }

    @Override
    public void onSwipe(int direction) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDoubleTap() {
        // TODO Auto-generated method stub
        // startActivity(new Intent(BannerActivityLED.this, Chat.class));
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        this.detector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        // startActivity(new Intent(BannerActivityLED.this, Chat.class));
        finish();
    }

    private void callZzleDB(String query) {
        Cursor c = null;

        try {
            c = db.open().getDatabaseObj().rawQuery(query, null);
            Constant.printMsg("The selected elist activity count is ::::::"
                    + c.getCount());
            c.moveToFirst();
            if (c.getCount() > 0) {
                Constant.printMsg("Caling sysout:::::::::::::::::::::::::");

                Constant.mPreviewSpeed = String.valueOf(c.getString(4));
                Constant.mPreviewTextsize = String.valueOf(c.getString(2));
                Constant.shapeselected = String.valueOf(c.getString(3));
                Constant.printMsg("testing  " + c.getString(0) + "  "
                        + c.getString(1) + "  " + c.getString(2) + " "
                        + c.getString(3) + " " + c.getString(4));

            }

        } catch (Exception e) {
            // TODO: handle exception
        } finally {

            db.close();

        }

    }

    class MyGameView extends SurfaceView implements Runnable {
        static final int DELAY = 10;
        Context mContext;
        SurfaceHolder mHolder;
        Thread mThread;
        volatile boolean running;
        private Bitmap back2;
        private int textSize;

        public MyGameView(Context context) {
            super(context);
            this.running = false;
            this.mHolder = getHolder();
            this.mContext = context;
        }

        public void resume() {
            this.running = true;
            this.mThread = new Thread(this);
            this.mThread.start();
        }

        public void stop() {
            this.running = false;
            this.mThread.destroy();

        }

        public void pause() {
            this.running = false;
            while (true) {
                try {
                    this.mThread.join();
                    break;
                } catch (Exception e) {
                }
            }
        }

        public void run() {
            int previewX = 0;
            int previewY = (int) ((((double) BannerActivityLED.this.screen7over8) / 4.0d) - (((double) BannerActivityLED.this.previewHeight) / 2.0d));
            if (BannerActivityLED.this.previewHeight > ((int) (((double) BannerActivityLED.this.screen7over8) / 2.0d))) {
                previewX = 0;
                previewY = 0;
            }
            Options options = new Options();
            options.inPreferredConfig = Config.RGB_565;
            options.inSampleSize = 1;
            options.inDither = true;
            options.inPurgeable = false;
            InputStream inputStream2 = null;
            try {
                if (Constant.shapeselected.equalsIgnoreCase("Circle")) {
                    inputStream2 = BannerActivityLED.this
                            .getApplicationContext().getAssets()
                            .open("back22.png");
                }
                if (Constant.shapeselected.equalsIgnoreCase("Star")) {
                    inputStream2 = BannerActivityLED.this
                            .getApplicationContext().getAssets()
                            .open("star.png");
                }
                if (Constant.shapeselected.equalsIgnoreCase("Heart")) {
                    inputStream2 = BannerActivityLED.this
                            .getApplicationContext().getAssets()
                            .open("heart.png");
                }

            } catch (IOException e) {
            }
            Bitmap temp2 = BitmapFactory.decodeStream(inputStream2, null,
                    options);
            try {
                inputStream2.close();
            } catch (IOException e2) {
            }
            this.back2 = Bitmap.createScaledBitmap(temp2,
                    BannerActivityLED.this.previewWidth,
                    BannerActivityLED.this.previewHeight, false);
            if (temp2 != this.back2) {
                temp2.recycle();
            }

            BannerActivityLED.this.paint = new Paint();
            BannerActivityLED.this.paint.setDither(true);
            BannerActivityLED.this.paint.setFilterBitmap(true);
            BannerActivityLED.this.paint.setTypeface(Typeface.create(
                    Typeface.SANS_SERIF, 1));
            this.textSize = (int) ((BannerActivityLED.this.zeroPointValue / 3.0f) * ((float) BannerActivityLED.this.previewHeight));
            BannerActivityLED.this.paint.setTextSize((float) this.textSize);
            BannerActivityLED.this.lengthPM = (int) BannerActivityLED.this.paint
                    .measureText(BannerActivityLED.this.printMessage);
            /** this for starting of lead on middle */
            BannerActivityLED.this.moveX = 0;
            BannerActivityLED.this.moveYFixed = ((int) (((float) (BannerActivityLED.this.previewHeight / 2)) - ((BannerActivityLED.this.paint
                    .descent() + BannerActivityLED.this.paint.ascent()) / 2.0f)))
                    + previewY;
            boolean isBlinkerSwitcher = true;
            int blinkerCounter = 0;
            /** this for starting of lead */
            if (BannerActivityLED.this.movementType == 0) {
                if (BannerActivityLED.this.lengthPM >= BannerActivityLED.this.screenWidth) {
                    BannerActivityLED.this.moveX = 0;
                } else {
                    BannerActivityLED.this.moveX = (BannerActivityLED.this.screenWidth / 2)
                            - (BannerActivityLED.this.lengthPM / 2);
                }
            } else if (BannerActivityLED.this.movementType == 1) {
                if (BannerActivityLED.this.lengthPM >= BannerActivityLED.this.screenWidth) {
                    BannerActivityLED.this.moveX = BannerActivityLED.this.lengthPM;
                } else {
                    BannerActivityLED.this.moveX = (BannerActivityLED.this.screenWidth / 2)
                            - (BannerActivityLED.this.lengthPM / 2);
                }
            } else if (BannerActivityLED.this.movementType == 2) {
                if (BannerActivityLED.this.lengthPM >= BannerActivityLED.this.screenWidth) {
                    BannerActivityLED.this.moveX = BannerActivityLED.this.screenWidth;
                } else {
                    BannerActivityLED.this.moveX = (BannerActivityLED.this.screenWidth / 2)
                            - (BannerActivityLED.this.lengthPM / 2);
                }
            }
            while (this.running) {
                Canvas canvas = this.mHolder.lockCanvas();
                if (canvas != null) {
                    try {
                        synchronized (this.mHolder) {
                            BannerActivityLED.this.paint.setColor(-16777216);
                            canvas.drawRect(
                                    0.0f,
                                    0.0f,
                                    (float) BannerActivityLED.this.screenWidth,
                                    (float) BannerActivityLED.this.screenHeight,
                                    BannerActivityLED.this.paint);
                            this.textSize = (int) ((BannerActivityLED.this.zeroPointValue / 3.0f) * ((float) BannerActivityLED.this.previewHeight));
                            BannerActivityLED.this.paint
                                    .setTextSize((float) this.textSize);
                            BannerActivityLED.this.moveYFixed = ((int) (((float) (BannerActivityLED.this.previewHeight / 2)) - ((BannerActivityLED.this.paint
                                    .descent() + BannerActivityLED.this.paint
                                    .ascent()) / 2.0f)))
                                    + previewY;
                            BannerActivityLED.this.lengthPM = (int) BannerActivityLED.this.paint
                                    .measureText(BannerActivityLED.this.printMessage);
                            BannerActivityLED.this.paint
                                    .setColor(Color
                                            .parseColor(BannerActivityLED.this.backroundColor));
                            canvas.drawRect(
                                    (float) previewX,
                                    (float) previewY,
                                    (float) (BannerActivityLED.this.previewWidth + 0),
                                    (float) (BannerActivityLED.this.previewHeight + previewY),
                                    BannerActivityLED.this.paint);
                            BannerActivityLED.this.paint
                                    .setColor(Color
                                            .parseColor(BannerActivityLED.this.inputTextColor));
                            if (BannerActivityLED.this.isBlinker) {
                                blinkerCounter += DELAY;
                                if (isBlinkerSwitcher) {
                                    if (blinkerCounter == 150) {
                                        blinkerCounter = 0;
                                        isBlinkerSwitcher = false;
                                    }
                                    canvas.drawText(
                                            BannerActivityLED.this.printMessage,
                                            (float) BannerActivityLED.this.moveX,
                                            (float) BannerActivityLED.this.moveYFixed,
                                            BannerActivityLED.this.paint);
                                } else {
                                    if (blinkerCounter == 100) {
                                        blinkerCounter = 0;
                                        isBlinkerSwitcher = true;
                                    }
                                    canvas.drawText(
                                            "",
                                            (float) BannerActivityLED.this.moveX,
                                            (float) BannerActivityLED.this.moveYFixed,
                                            BannerActivityLED.this.paint);
                                }
                            } else {
                                canvas.drawText(
                                        BannerActivityLED.this.printMessage,
                                        (float) BannerActivityLED.this.moveX,
                                        (float) BannerActivityLED.this.moveYFixed,
                                        BannerActivityLED.this.paint);
                            }
                            canvas.drawBitmap(this.back2, (float) previewX,
                                    (float) previewY,
                                    BannerActivityLED.this.paint);
                            if (BannerActivityLED.this.movementType != 0) {
                                BannerActivityLED BannerActivityLED;
                                if (BannerActivityLED.this.movementType == 1) {
                                    if (BannerActivityLED.this.screenWidth >= BannerActivityLED.this.moveX) {
                                        BannerActivityLED = BannerActivityLED.this;
                                        BannerActivityLED.moveX = BannerActivityLED.moveX
                                                + BannerActivityLED.this.movementDeltaX;
                                    } else {
                                        BannerActivityLED.this.moveX = -BannerActivityLED.this.lengthPM;
                                    }
                                } else if (BannerActivityLED.this.movementType == 2) {
                                    if (BannerActivityLED.this.moveX >= (-BannerActivityLED.this.lengthPM)) {
                                        BannerActivityLED = BannerActivityLED.this;
                                        BannerActivityLED.moveX = BannerActivityLED.moveX
                                                - BannerActivityLED.this.movementDeltaX;
                                    } else {
                                        BannerActivityLED.this.moveX = BannerActivityLED.this.screenWidth;
                                    }
                                }
                            }
                        }
                        this.mHolder.unlockCanvasAndPost(canvas);
                    } catch (Throwable th) {
                        this.mHolder.unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (Exception e3) {
                }
            }
            if (this.back2 != null && !this.back2.isRecycled()) {
                this.back2.recycle();
                this.back2 = null;
            }
        }
    }
}
