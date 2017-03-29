package com.wifin.kachingme.kaching_feature.dazz;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.SimpleGestureFilter;
import com.wifin.kachingme.util.SimpleGestureFilter.SimpleGestureListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BannerPreview extends Activity implements OnTouchListener,
        OnTouchModeChangeListener, SimpleGestureListener {

    public static String Value = "";
    public static String mZzleText = "";
    public static String mZzleTexTime = "";
    public static BannerPreview mActivity;
    public MyGameView mGameView;
    Dialog dialog;
    // Spinner $SizeAnalyz;
    ArrayList<String> sizeList = new ArrayList<String>();
    Handler handler = new Handler();
    Runnable run;
    int count = 1000;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        LinearLayout.LayoutParams paramlinear;
        super.onCreate(savedInstanceState);
        this.mGameView = new MyGameView(this);
        mActivity = this;
        Window win = getWindow();
        requestWindowFeature(1);
        win.setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT,
                AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        win.setContentView(this.mGameView);
        LinearLayout linear = (LinearLayout) ((LayoutInflater) getSystemService("layout_inflater"))
                .inflate(R.layout.activity_banner, null);

        Constant.printMsg("preview data " + Constant.mPreviewSpeed + "   "
                + Constant.mPreviewTextsize);
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
            printMessage = Value + "   " + printMessage;
        }
        // inputTextColor = pref.getString("inputtextcolor", "#00FF00");
        // backroundColor = pref.getString("backroundcolor", "#292929");
        // movementType = pref.getInt("movementtype", 2);
        // isBlinker = pref.getBoolean("isblinker", false);
        // speedText = pref.getInt("speedtext", 80);
        // if (screenWidth >= 600) {
        // speedText = pref.getInt("speedtext", 50);
        //
        // } else if (screenWidth > 501 && screenWidth < 600) {
        // speedText = pref.getInt("speedtext", 60);
        //
        // } else if (screenWidth > 260 && screenWidth < 500) {
        // speedText = pref.getInt("speedtext", 70);
        //
        // } else if (screenWidth <= 260) {
        // speedText = pref.getInt("speedtext", 80);
        // }

        // movementDeltaX = (int) (((float) previewWidth) / (((float) speedText)
        // / (((float) screenHeight) / ((float) previewWidth))));
        // textSizeRatio = pref.getFloat("textsizeratio", 0.7f);
        // zeroPointValue = textSizeRatio * 3.0f;
        // mGameView.resume();

        if (Constant.mPreviewBackground.equalsIgnoreCase("black")) {
            backroundColor = pref.getString("backroundcolor", "#292929");

        }
        if (Constant.mPreviewBackground.equalsIgnoreCase("white")) {
            backroundColor = pref.getString("backroundcolor", "#FFFFFF");

        }
        if (Constant.mPreviewBackground.equalsIgnoreCase("pink")) {
            backroundColor = pref.getString("backroundcolor", "#FFDDF5");

        }
        if (Constant.mPreviewBackground.equalsIgnoreCase("green")) {
            backroundColor = pref.getString("backroundcolor", "#008000");

        }
        if (Constant.mPreviewBackground.equalsIgnoreCase("blue")) {
            backroundColor = pref.getString("backroundcolor", "#62C8E7");

        }

        if (Constant.mPreviewTextColor.equalsIgnoreCase("white")) {
            inputTextColor = pref.getString("inputtextcolor", "#FFFFFF");

        }
        if (Constant.mPreviewTextColor.equalsIgnoreCase("black")) {
            inputTextColor = pref.getString("inputtextcolor", "#292929");

        }
        if (Constant.mPreviewTextColor.equalsIgnoreCase("blue")) {
            inputTextColor = pref.getString("inputtextcolor", "#62C8E7");

        }
        if (Constant.mPreviewTextColor.equalsIgnoreCase("pink")) {
            inputTextColor = pref.getString("inputtextcolor", "#FFDDF5");

        }
        if (Constant.mPreviewTextColor.equalsIgnoreCase("green")) {
            inputTextColor = pref.getString("inputtextcolor", "#00FF00");

        }
        movementType = pref.getInt("movementtype", 2);
        isBlinker = pref.getBoolean("isblinker", false);

        if (Constant.mPreviewSpeed.equalsIgnoreCase("slow")) {
            speedText = pref.getInt("speedtext", 100);
        } else {
            if (Constant.mPreviewSpeed.equalsIgnoreCase("medium")) {

                speedText = pref.getInt("speedtext", 70);
            } else {
                if (Constant.mPreviewSpeed.equalsIgnoreCase("fast")) {
                    speedText = pref.getInt("speedtext", 50);
                }
            }
        }
        if (Constant.mPreviewTextsize.equalsIgnoreCase("small")) {
            textSizeRatio = pref.getFloat("textsizeratio", 0.7f);
            zeroPointValue = textSizeRatio * 2.3f;

        } else {
            if (Constant.mPreviewTextsize.equalsIgnoreCase("medium")) {

                textSizeRatio = pref.getFloat("textsizeratio", 0.7f);
                zeroPointValue = textSizeRatio * 3.2f;
            } else {
                if (Constant.mPreviewTextsize.equalsIgnoreCase("large")) {
                    textSizeRatio = pref.getFloat("textsizeratio", 0.7f);
                    zeroPointValue = textSizeRatio * 3.8f;
                }
            }
        }
        movementDeltaX = (int) (((float) previewWidth) / (((float) speedText) / (((float) screenHeight) / ((float) previewWidth))));

        mGameView.resume();
        // }
        // }, count * 2);

        // / this.input.addTextChangedListener(this.printMessageWatcher);
        // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
        // android.R.layout.simple_spinner_item, sizeList);
        // dataAdapter
        // .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // $SizeAnalyz.setAdapter(dataAdapter);
        // $SizeAnalyz.setOnItemSelectedListener(new OnItemSelectedListener() {
        //
        // @Override
        // public void onItemSelected(AdapterView<?> arg0, View arg1,
        // int arg2, long arg3) {
        // // TODO Auto-generated method stub
        // String getItem = $SizeAnalyz.getSelectedItem().toString();
        // if (Constant.mPreviewTextsize.equalsIgnoreCase("2x2")) {
        // SharedPreferences pref = getSharedPreferences("SaveSate", 0);
        // printMessage = $input.getText().toString();
        // // for (int i = 0; i < 50; i++) {
        // // printMessage = text+"     "+printMessage;
        // // }
        // inputTextColor = pref
        // .getString("inputtextcolor", "#00FF00");
        // backroundColor = pref
        // .getString("backroundcolor", "#292929");
        // movementType = pref.getInt("movementtype", 2);
        // isBlinker = pref.getBoolean("isblinker", false);
        // speedText = pref.getInt("speedtext", 80);
        // movementDeltaX = (int) (((float) previewWidth) / (((float) speedText)
        // / (((float) screenHeight) / ((float) previewWidth))));
        // textSizeRatio = pref.getFloat("textsizeratio", 0.7f);
        // zeroPointValue = textSizeRatio * 1.0f;
        // mGameView.resume();
        // } else {
        // if (Constant.mPreviewTextsize.equalsIgnoreCase("small")) {
        // SharedPreferences pref = getSharedPreferences(
        // "SaveSate", 0);
        // printMessage = $input.getText().toString();
        // // for (int i = 0; i < 50; i++) {
        // // printMessage =
        // // text+"                    "+printMessage;
        // // }
        // inputTextColor = pref.getString("inputtextcolor",
        // "#00FF00");
        // backroundColor = pref.getString("backroundcolor",
        // "#292929");
        // movementType = pref.getInt("movementtype", 2);
        // isBlinker = pref.getBoolean("isblinker", false);
        // speedText = pref.getInt("speedtext", 80);
        // movementDeltaX = (int) (((float) previewWidth) / (((float) speedText)
        // / (((float) screenHeight) / ((float) previewWidth))));
        // textSizeRatio = pref.getFloat("textsizeratio", 0.7f);
        // zeroPointValue = textSizeRatio * 2.0f;
        // mGameView.resume();
        // } else {
        // if (Constant.mPreviewTextsize
        // .equalsIgnoreCase("medium")) {
        // SharedPreferences pref = getSharedPreferences(
        // "SaveSate", 0);
        // printMessage = $input.getText().toString();
        // // for (int i = 0; i < 50; i++) {
        // // printMessage =
        // // text+"                        "+printMessage;
        // // }
        // inputTextColor = pref.getString("inputtextcolor",
        // "#00FF00");
        // backroundColor = pref.getString("backroundcolor",
        // "#292929");
        // movementType = pref.getInt("movementtype", 2);
        // isBlinker = pref.getBoolean("isblinker", false);
        // speedText = pref.getInt("speedtext", 80);
        // movementDeltaX = (int) (((float) previewWidth) / (((float) speedText)
        // / (((float) screenHeight) / ((float) previewWidth))));
        // textSizeRatio = pref
        // .getFloat("textsizeratio", 0.7f);
        // zeroPointValue = textSizeRatio * 3.0f;
        // mGameView.resume();
        // } else {
        // if (Constant.mPreviewTextsize
        // .equalsIgnoreCase("large")) {
        // printMessage = $input.getText().toString();
        // // for (int i = 0; i < 50; i++) {
        // // printMessage =
        // // text+"                         "+printMessage;
        // // }
        // SharedPreferences pref = getSharedPreferences(
        // "SaveSate", 0);
        // printMessage = $input.getText().toString();
        // inputTextColor = pref.getString(
        // "inputtextcolor", "#00FF00");
        // backroundColor = pref.getString(
        // "backroundcolor", "#292929");
        // movementType = pref.getInt("movementtype", 2);
        // isBlinker = pref.getBoolean("isblinker", false);
        // speedText = pref.getInt("speedtext", 80);
        // movementDeltaX = (int) (((float) previewWidth) / (((float) speedText)
        // / (((float) screenHeight) / ((float) previewWidth))));
        // textSizeRatio = pref.getFloat("textsizeratio",
        // 0.7f);
        // zeroPointValue = textSizeRatio * 4.0f;
        // mGameView.resume();
        // }
        // }
        // }
        // }
        //
        // }
        //
        // @Override
        // public void onNothingSelected(AdapterView<?> arg0) {
        // // TODO Auto-generated method stub
        //
        // }
        // });

        // $Submit.setOnClickListener(new View.OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // Constant.printMsg("width of screen" + screenWidth);
        // SharedPreferences pref = getSharedPreferences("SaveSate", 0);
        // printMessage = $input.getText().toString();
        // // for (int i = 0; i < 50; i++) {
        // // printMessage = text+"             "+printMessage;
        // // }
        // inputTextColor = pref.getString("inputtextcolor", "#00FF00");
        // backroundColor = pref.getString("backroundcolor", "#292929");
        // movementType = pref.getInt("movementtype", 2);
        // isBlinker = pref.getBoolean("isblinker", false);
        // speedText = pref.getInt("speedtext", 80);
        // movementDeltaX = (int) (((float) previewWidth) / (((float) speedText)
        // / (((float) screenHeight) / ((float) previewWidth))));
        // textSizeRatio = pref.getFloat("textsizeratio", 0.7f);
        // zeroPointValue = textSizeRatio * 3.0f;
        // mGameView.resume();
        // }
        // });
        // $Cancel.setOnClickListener(new View.OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // mGameView.stop();
        // }
        // });
        // $Pause.setOnClickListener(new View.OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // mGameView.pause();
        // }
        // });
        // $Resume.setOnClickListener(new View.OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // mGameView.resume();
        // }
        // });

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
//		startActivity(new Intent(BannerPreview.this, DazzPlainActivity.class));
        finish();
        return true;
    }

    @Override
    public void onTouchModeChanged(boolean isInTouchMode) {
        // TODO Auto-generated method stub
//		startActivity(new Intent(BannerPreview.this, DazzPlainActivity.class));
        finish();
    }

    @Override
    public void onSwipe(int direction) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDoubleTap() {
        // TODO Auto-generated method stub
//		startActivity(new Intent(BannerPreview.this, DazzPlainActivity.class));
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
//		startActivity(new Intent(BannerPreview.this, DazzPlainActivity.class));
        finish();
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
            int previewY = (int) ((((double) BannerPreview.this.screen7over8) / 4.0d) - (((double) BannerPreview.this.previewHeight) / 2.0d));
            if (BannerPreview.this.previewHeight > ((int) (((double) BannerPreview.this.screen7over8) / 2.0d))) {
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
                inputStream2 = BannerPreview.this.getApplicationContext()
                        .getAssets().open("blank.png");
            } catch (IOException e) {
            }
            Bitmap temp2 = BitmapFactory.decodeStream(inputStream2, null,
                    options);
            try {
                inputStream2.close();
            } catch (IOException e2) {
            }
            this.back2 = Bitmap.createScaledBitmap(temp2,
                    BannerPreview.this.previewWidth,
                    BannerPreview.this.previewHeight, false);
            if (temp2 != this.back2) {
                temp2.recycle();
            }

            BannerPreview.this.paint = new Paint();
            BannerPreview.this.paint.setDither(true);
            BannerPreview.this.paint.setFilterBitmap(true);
            BannerPreview.this.paint.setTypeface(Typeface.create(
                    Typeface.SANS_SERIF, 1));
            this.textSize = (int) ((BannerPreview.this.zeroPointValue / 3.0f) * ((float) BannerPreview.this.previewHeight));
            BannerPreview.this.paint.setTextSize((float) this.textSize);
            BannerPreview.this.lengthPM = (int) BannerPreview.this.paint
                    .measureText(BannerPreview.this.printMessage);
            /** this for starting of lead on middle */
            BannerPreview.this.moveX = 0;
            BannerPreview.this.moveYFixed = ((int) (((float) (BannerPreview.this.previewHeight / 2)) - ((BannerPreview.this.paint
                    .descent() + BannerPreview.this.paint.ascent()) / 2.0f)))
                    + previewY;
            boolean isBlinkerSwitcher = true;
            int blinkerCounter = 0;
            /** this for starting of lead */
            if (BannerPreview.this.movementType == 0) {
                if (BannerPreview.this.lengthPM >= BannerPreview.this.screenWidth) {
                    BannerPreview.this.moveX = 0;
                } else {
                    BannerPreview.this.moveX = (BannerPreview.this.screenWidth / 2)
                            - (BannerPreview.this.lengthPM / 2);
                }
            } else if (BannerPreview.this.movementType == 1) {
                if (BannerPreview.this.lengthPM >= BannerPreview.this.screenWidth) {
                    BannerPreview.this.moveX = BannerPreview.this.lengthPM;
                } else {
                    BannerPreview.this.moveX = (BannerPreview.this.screenWidth / 2)
                            - (BannerPreview.this.lengthPM / 2);
                }
            } else if (BannerPreview.this.movementType == 2) {
                if (BannerPreview.this.lengthPM >= BannerPreview.this.screenWidth) {
                    BannerPreview.this.moveX = BannerPreview.this.screenWidth;
                } else {
                    BannerPreview.this.moveX = (BannerPreview.this.screenWidth / 2)
                            - (BannerPreview.this.lengthPM / 2);
                }
            }
            while (this.running) {
                Canvas canvas = this.mHolder.lockCanvas();
                if (canvas != null) {
                    try {
                        synchronized (this.mHolder) {
                            BannerPreview.this.paint.setColor(-16777216);
                            canvas.drawRect(0.0f, 0.0f,
                                    (float) BannerPreview.this.screenWidth,
                                    (float) BannerPreview.this.screenHeight,
                                    BannerPreview.this.paint);
                            this.textSize = (int) ((BannerPreview.this.zeroPointValue / 3.0f) * ((float) BannerPreview.this.previewHeight));
                            BannerPreview.this.paint
                                    .setTextSize((float) this.textSize);
                            BannerPreview.this.moveYFixed = ((int) (((float) (BannerPreview.this.previewHeight / 2)) - ((BannerPreview.this.paint
                                    .descent() + BannerPreview.this.paint
                                    .ascent()) / 2.0f)))
                                    + previewY;
                            BannerPreview.this.lengthPM = (int) BannerPreview.this.paint
                                    .measureText(BannerPreview.this.printMessage);
                            BannerPreview.this.paint
                                    .setColor(Color
                                            .parseColor(BannerPreview.this.backroundColor));
                            canvas.drawRect(
                                    (float) previewX,
                                    (float) previewY,
                                    (float) (BannerPreview.this.previewWidth + 0),
                                    (float) (BannerPreview.this.previewHeight + previewY),
                                    BannerPreview.this.paint);
                            BannerPreview.this.paint
                                    .setColor(Color
                                            .parseColor(BannerPreview.this.inputTextColor));
                            if (BannerPreview.this.isBlinker) {
                                blinkerCounter += DELAY;
                                if (isBlinkerSwitcher) {
                                    if (blinkerCounter == 150) {
                                        blinkerCounter = 0;
                                        isBlinkerSwitcher = false;
                                    }
                                    canvas.drawText(
                                            BannerPreview.this.printMessage,
                                            (float) BannerPreview.this.moveX,
                                            (float) BannerPreview.this.moveYFixed,
                                            BannerPreview.this.paint);
                                } else {
                                    if (blinkerCounter == 100) {
                                        blinkerCounter = 0;
                                        isBlinkerSwitcher = true;
                                    }
                                    canvas.drawText(
                                            "",
                                            (float) BannerPreview.this.moveX,
                                            (float) BannerPreview.this.moveYFixed,
                                            BannerPreview.this.paint);
                                }
                            } else {
                                canvas.drawText(
                                        BannerPreview.this.printMessage,
                                        (float) BannerPreview.this.moveX,
                                        (float) BannerPreview.this.moveYFixed,
                                        BannerPreview.this.paint);
                            }
                            canvas.drawBitmap(this.back2, (float) previewX,
                                    (float) previewY, BannerPreview.this.paint);
                            if (BannerPreview.this.movementType != 0) {
                                BannerPreview BannerPreview;
                                if (BannerPreview.this.movementType == 1) {
                                    if (BannerPreview.this.screenWidth >= BannerPreview.this.moveX) {
                                        BannerPreview = BannerPreview.this;
                                        BannerPreview.moveX = BannerPreview.moveX
                                                + BannerPreview.this.movementDeltaX;
                                    } else {
                                        BannerPreview.this.moveX = -BannerPreview.this.lengthPM;
                                    }
                                } else if (BannerPreview.this.movementType == 2) {
                                    if (BannerPreview.this.moveX >= (-BannerPreview.this.lengthPM)) {
                                        BannerPreview = BannerPreview.this;
                                        BannerPreview.moveX = BannerPreview.moveX
                                                - BannerPreview.this.movementDeltaX;
                                    } else {
                                        BannerPreview.this.moveX = BannerPreview.this.screenWidth;
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
