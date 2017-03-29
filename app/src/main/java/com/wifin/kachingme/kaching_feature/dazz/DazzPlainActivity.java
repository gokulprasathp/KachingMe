package com.wifin.kachingme.kaching_feature.dazz;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.CustomAdapter;
import com.wifin.kachingme.chat.muc_chat.MUCTest;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.util.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class DazzPlainActivity extends Activity implements AdapterView.OnItemSelectedListener {
    public static final String msg = "msg";
    public static DazzPlainActivity mActivity;
    LinearLayout dazzAppBarPlain;
    Toolbar dazzToolBarPlain;
    FrameLayout dazzFramePlain;
    ImageView imgDazzBackPlain, imgDazzCopyPlain, imgDazzPlainEmoji;
    int height, width;
    LinearLayout linearDazzPlainHead, linearDazzTypePlain, linearTextSizePlain, linearDazzBackPlain,
            linearDazzColorPlain, linearDazzSpeedPlain, linearDazzButtonPlain, linearDazzShapePlain;
    TextView tvDazzTypePlain, tvDazzTextSizePlain, tvDazzBackPlain, tvDazzColorPlain, tvDazzSpeedPlain, tvDazzShapePlain, mHeader;
    EditText tvDazzPlain;
    Button btDazzPreviewPlain, btDazzSendPlain;
    Spinner spDazzTypePlain, spTextSizePlain, spDazzBackPlain, spDazzColorPlain, spDazzSpeedPlain, spDazzShapePlain;
    String[] colorNames = {"Black", "Green", "Pink", "Blue", "White"};
    int flags[] = {R.color.black, R.color.kons_green, R.color.kons_pink, R.color.kons_blue, R.color.white};
    ArrayList<String> mFontSizeList = new ArrayList<String>();
    ArrayList<String> mSpeedList = new ArrayList<String>();
    ArrayList<String> mShapeList = new ArrayList<String>();
    ArrayList<String> mTypeList = new ArrayList<String>();
    ArrayList<String> mZzleList = new ArrayList<String>();
    Dbhelper db;
    String slctTextsize, slctBackgd, slctTextcolor, slctSpeed, slctType,
            query_dazz, mCount;
    SharedPreferences sharedPrefs;
    boolean msend = false;
    CustomAdapter mCustomAdapter;
    String mTextSizeText, shapetext;
    String mTextBackgroundText, mTextcolorText;
    ArrayAdapter<String> fontAdapter;
    ArrayAdapter<String> speedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dazz_plain);
        db = new Dbhelper(getApplicationContext());
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mActivity = this;
        init();
        dazzScreen();

        spDazzBackPlain.setOnItemSelectedListener(this);
        spDazzColorPlain.setOnItemSelectedListener(this);
        spDazzTypePlain.setOnItemSelectedListener(this);
        spTextSizePlain.setOnItemSelectedListener(this);
        spDazzSpeedPlain.setOnItemSelectedListener(this);

        mCustomAdapter = new CustomAdapter(getApplicationContext(), flags, colorNames);
        spDazzBackPlain.setAdapter(mCustomAdapter);

        spDazzColorPlain.setAdapter(mCustomAdapter);


        Constant.printMsg("dazzplainActivity :::::" + Constant.mFromDazzLib +
                Constant.mDazzLib + "   " + Constant.mChatText + Constant.mPreviewSpeed + "   " + Constant.mPreviewTextsize);
        if (Constant.mChatText != null) {


            tvDazzPlain.setText(Constant.mChatText);
        }

        mFontSizeList.add("Small");
        mFontSizeList.add("Medium");
        mFontSizeList.add("Large");

        mSpeedList.add("Slow");
        mSpeedList.add("Medium");
        mSpeedList.add("Fast");

        mShapeList.add("Circle");
        mShapeList.add("Star");
        mShapeList.add("Heart");

        mTypeList.add("Plain");
        mTypeList.add("LED");


        String query = "SELECT * FROM " + Dbhelper.TABLE_CART;
        getCount(query);
//        setcount_txt.setText(mCount);

        query = "select msg from " + Dbhelper.TABLE_ZZLE;
        collectData(query);

        fontAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mFontSizeList);
        fontAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTextSizePlain.setAdapter(fontAdapter);


        speedAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mSpeedList);
        speedAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDazzSpeedPlain.setAdapter(speedAdapter);

        ArrayAdapter<String> shapeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mShapeList);
        shapeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDazzShapePlain.setAdapter(shapeAdapter);


        ArrayAdapter<String> textctypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mTypeList);


        textctypeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDazzTypePlain.setAdapter(textctypeAdapter);

        Constant.mZzle = true;


        if (Constant.mFromDazzLed == true) {
            Constant.printMsg("TEST DazZ ::::::33333");

            Constant.mFromDazzLed = false;
            spDazzTypePlain.setSelection(1);

        } else {
            Constant.printMsg("TEST DazZ :::" +
                    ":::44444");

            spDazzTypePlain.setSelection(0);

        }
        Constant.printMsg("TEST DazZ :::" +
                Constant.mDazzLib + "   " + mZzleList);


        Constant.printMsg("nbjhvdfhjgvfgvb type " + Constant.mDazZType);
        if (Constant.mDazZType != null) {
            if (Constant.mDazZType == "plain") {
                if (Constant.mPreviewBackground.equalsIgnoreCase("black")) {
                    spDazzBackPlain.setSelection(0);

                }
                if (Constant.mPreviewBackground.equalsIgnoreCase("white")) {
                    spDazzBackPlain.setSelection(4);

                }
                if (Constant.mPreviewBackground.equalsIgnoreCase("pink")) {
                    spDazzBackPlain.setSelection(2);

                }
                if (Constant.mPreviewBackground.equalsIgnoreCase("green")) {
                    spDazzBackPlain.setSelection(1);

                }
                if (Constant.mPreviewBackground.equalsIgnoreCase("blue")) {
                    spDazzBackPlain.setSelection(3);

                }

                if (Constant.mPreviewTextColor.equalsIgnoreCase("white")) {
                    spDazzColorPlain.setSelection(4);

                }
                if (Constant.mPreviewTextColor.equalsIgnoreCase("black")) {
                    spDazzColorPlain.setSelection(0);

                }
                if (Constant.mPreviewTextColor.equalsIgnoreCase("blue")) {
                    spDazzColorPlain.setSelection(3);

                }
                if (Constant.mPreviewTextColor.equalsIgnoreCase("pink")) {
                    spDazzColorPlain.setSelection(2);

                }
                if (Constant.mPreviewTextColor.equalsIgnoreCase("green")) {
                    spDazzColorPlain.setSelection(1);

                }

                if (Constant.mPreviewSpeed.equalsIgnoreCase("slow")) {
                    spDazzSpeedPlain.setSelection(0);

                }
                if (Constant.mPreviewSpeed.equalsIgnoreCase("medium")) {
                    spDazzSpeedPlain.setSelection(1);

                }
                if (Constant.mPreviewSpeed.equalsIgnoreCase("fast")) {
                    spDazzSpeedPlain.setSelection(2);

                }


                if (Constant.mPreviewTextsize.equalsIgnoreCase("small")) {
                    spTextSizePlain.setSelection(0);


                }
                if (Constant.mPreviewTextsize.equalsIgnoreCase("medium")) {

                    spTextSizePlain.setSelection(1);

                }
                if (Constant.mPreviewTextsize.equalsIgnoreCase("large")) {
                    spTextSizePlain.setSelection(2);

                }
            } else {
                try {


                    Constant.printMsg("nbjhvdfhjgvfgvb type led " + Constant.mDazZType + Constant.mPreviewSpeed + Constant.mPreviewTextsize);

                    if (Constant.mPreviewSpeed.equalsIgnoreCase("Slow")) {
                        spDazzSpeedPlain.setSelection(0);
                        Constant.printMsg("nbjhvdfhjgvfgvb type led111 " + Constant.mDazZType + Constant.mPreviewSpeed + Constant.mPreviewTextsize);
                    }

                    if (Constant.mPreviewSpeed.equalsIgnoreCase("Medium")) {
                        spDazzSpeedPlain.setSelection(1);

                        Constant.printMsg("nbjhvdfhjgvfgvb type led111222 ");
                    }
                    if (Constant.mPreviewSpeed.equalsIgnoreCase("Fast")) {
                        spDazzSpeedPlain.setSelection(2);
                        Constant.printMsg("nbjhvdfhjgvfgvb type led111222333 ");

                    }


                    if (Constant.mPreviewTextsize.equalsIgnoreCase("Small")) {
                        Constant.printMsg("nbjhvdfhjgvfgvb type led2222 " + Constant.mDazZType + Constant.mPreviewSpeed + Constant.mPreviewTextsize);

                        spTextSizePlain.setSelection(0);

                    }
                    if (Constant.mPreviewTextsize.equalsIgnoreCase("Medium")) {
                        spTextSizePlain.setSelection(1);
                        Constant.printMsg("nbjhvdfhjgvfgvb type led111222444 ");


                    }
                    if (Constant.mPreviewTextsize.equalsIgnoreCase("Large")) {
                        spTextSizePlain.setSelection(2);
                        Constant.printMsg("nbjhvdfhjgvfgvb type led111222555 ");

                    }
                    if (Constant.shapeselected.equalsIgnoreCase("Circle")) {
                        spDazzShapePlain.setSelection(0);
                        Constant.printMsg("nbjhvdfhjgvfgvb type led111222666 ");

                    }
                    if (Constant.shapeselected.equalsIgnoreCase("Star")) {
                        spDazzShapePlain.setSelection(1);

                    }
                    if (Constant.shapeselected.equalsIgnoreCase("Heart")) {
                        spDazzShapePlain.setSelection(2);
                        Constant.printMsg("nbjhvdfhjgvfgvb type led111222777 ");

                    }
                } catch (Exception e) {
                    Constant.printMsg("sjkasfd" + e.toString());
                }

            }


        } else {

            Constant.printMsg("nbjhvdfhjgvfgvb type led1112228888 ");
            Constant.printMsg("TEST DazZ ::::::1111");

            spTextSizePlain.setSelection(2);
            spDazzBackPlain.setSelection(0);
            spDazzColorPlain.setSelection(4);
            spDazzSpeedPlain.setSelection(1);
            spDazzTypePlain.setSelection(0);
        }

        imgDazzCopyPlain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(DazzPlainActivity.this, DazzTabActivity.class);
                startActivity(in);
            }
        });

        imgDazzBackPlain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.mZzle = false;
                Constant.mBazzle = false;
                if (Constant.mDazZFromSlider == true) {

                    Constant.mDazZFromSlider = false;
                    Intent i = new Intent(DazzPlainActivity.this,
                            SliderTesting.class);
                    startActivity(i);
                    finish();
                } else if (Constant.zzleFromgroup == true) {
                    Constant.isMUC_Paused = true;
                    Constant.zzleFromgroup = false;
                    Intent i = new Intent(DazzPlainActivity.this, MUCTest.class);
                    startActivity(i);
                    finish();
                } else {
                    Constant.printMsg("chatttt34" + Constant.mZzleText);

                    Intent i = new Intent(DazzPlainActivity.this, ChatTest.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        btDazzPreviewPlain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String types = spDazzTypePlain.getSelectedItem().toString();
                Constant.printMsg("TEST DazZ ::::::2222");

                if (tvDazzPlain.getText().toString().trim().length() != 0) {
                    if (types.equalsIgnoreCase("Plain")) {
                        if (Constant.mZzle == true) {
                            Constant.mDazZType = "Plain";
                            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.putBoolean("dazzStatus", true);
                            editor.commit();
//                      Constant.mPreviewBackground = spDazzBackPlain
//                              .getSelectedItem().toString();
                            Constant.mPreviewText = tvDazzPlain.getText()
                                    .toString();
                            Constant.mChatText = tvDazzPlain.getText()
                                    .toString();
                            Constant.mPreviewSpeed = spDazzSpeedPlain
                                    .getSelectedItem().toString();
//                      Constant.mPreviewTextColor = spDazzColorPlain
//                              .getSelectedItem().toString();
                            Constant.mPreviewTextsize = spTextSizePlain
                                    .getSelectedItem().toString();

                            Constant.mZzleText = tvDazzPlain.getText()
                                    .toString().trim();
//                      String bg = spDazzBackPlain.getSelectedItem()
//                              .toString();
//                      Constant.printMsg("bg::::::>>>>>>>>>" + bg);
                            String font = spTextSizePlain.getSelectedItem()
                                    .toString();
                            Constant.printMsg("font:::::>>>>>>>>" + font);
                            String speed = spDazzSpeedPlain.getSelectedItem()
                                    .toString();
                            Constant.printMsg("speed:::>>>" + speed);
//                      String fontcolor = spDazzColorPlain.getSelectedItem()
//                              .toString();
//                      Constant.printMsg("textcolor:::>>>"
//                              + fontcolor);
                            ContentValues cv = new ContentValues();

                            String mDazzText = tvDazzPlain.getText()
                                    .toString().trim()
                                    .replaceAll("\\s++", " ");

                            Constant.mZzleText = mDazzText;
                            cv.put("msg", mDazzText);
                            cv.put("backgrount", Constant.mPreviewBackground);
                            cv.put("font", font);
                            cv.put("speed", speed);
                            cv.put("fontcolor", Constant.mPreviewTextColor);

                            if (Constant.mFromDazzLib == true) {
                                // Constant.mFromDazzLib = false;
                                System.out
                                        .println("checkkkkkk   inside update"
                                                + tvDazzPlain.getText()
                                                .toString()
                                                + "  "
                                                + Constant.mChatText);


                                try {
                                    int a = (int) db
                                            .open()
                                            .getDatabaseObj()
                                            .update(Dbhelper.TABLE_ZZLE,
                                                    cv,
                                                    msg
                                                            + "='"
                                                            + Constant.mDazZLibText
                                                            + "'", null);
                                    System.out
                                            .println("No of updated rows in zzle :::::::::"
                                                    + a + cv + Constant.mDazZLibText);
                                    // Constant.mDazzLib = false;
                                } catch (SQLException e) {
                                    System.out
                                            .println("Sql exception in ecpl details ::::::"
                                                    + e.toString());
                                } finally {
                                    db.close();
                                }

                            } else {

                                // Constant.mZzle = true;
                                Constant.printMsg("called DazZ if:::>>>   "
                                        + Constant.mDazzLib);
                                if (!Constant.mDazzLib) {
                                    if (!mZzleList
                                            .contains(Constant.mZzleText)) {

                                        Constant.printMsg("checkkkkkk12 inserting text ::::: "
                                                + Constant.mZzleText
                                                + "    "
                                                + tvDazzPlain
                                                .getText()
                                                .toString()
                                                .trim());
                                        Constant.printMsg("insert 3");

                                        insertToDB(cv);
                                        // Constant.mDazzLib = true;
                                        Constant.printMsg("called DazZ if:::>>>");
                                    } else {
                                        Constant.printMsg("checkkkkkk123 called DazZ if:::>>>111" + cv);
                                        Constant.printMsg("updating text ::::: "
                                                + Constant.mChatText
                                                + "    "
                                                + tvDazzPlain
                                                .getText()
                                                .toString()
                                                .trim());
                                        try {
                                            int a = (int) db
                                                    .open()
                                                    .getDatabaseObj()
                                                    .update(Dbhelper.TABLE_ZZLE,
                                                            cv,
                                                            msg
                                                                    + "='"
                                                                    + Constant.mChatText
                                                                    + "'",
                                                            null);
                                            System.out
                                                    .println("No of updated rows in zzle1111 :::::::::"
                                                            + a);
                                            // Constant.mDazzLib = false;
                                        } catch (SQLException e) {
                                            System.out
                                                    .println("Sql exception in ecpl details ::::::"
                                                            + e.toString());
                                        } finally {
                                            db.close();
                                        }
                                    }

                                } else {
                                    Constant.printMsg("checkkkkkk55 called DazZ if:::>>>222");
                                    Constant.printMsg("updating text :::::1 "
                                            + Constant.mZzleText
                                            + "    "
                                            + tvDazzPlain.getText()
                                            .toString()
                                            .trim());
                                    try {
                                        int a = (int) db
                                                .open()
                                                .getDatabaseObj()
                                                .update(Dbhelper.TABLE_ZZLE,
                                                        cv,
                                                        msg
                                                                + "='"
                                                                + Constant.mChatText
                                                                + "'", null);
                                        System.out
                                                .println("No of updated rows in zzle :::::::::"
                                                        + a);
                                        // Constant.mDazzLib = false;
                                    } catch (SQLException e) {
                                        System.out
                                                .println("Sql exception in ecpl details ::::::"
                                                        + e.toString());
                                    } finally {
                                        db.close();
                                    }

                                }

                            }

//                      Constant.mPreviewBackground = spDazzBackPlain
//                              .getSelectedItem().toString();
                            Constant.mPreviewText = tvDazzPlain.getText()
                                    .toString();
                            Constant.mPreviewSpeed = spDazzSpeedPlain
                                    .getSelectedItem().toString();
//                      Constant.mPreviewTextColor = spDazzColorPlain
//                              .getSelectedItem().toString();
                            Constant.mPreviewTextsize = spTextSizePlain
                                    .getSelectedItem().toString();

                            SharedPreferences.Editor e1 = sharedPrefs.edit();
                            e1.putString("lastdazzl", Constant.mPreviewText);
                            e1.commit();
                            BannerPreview.mZzleText = tvDazzPlain.getText()
                                    .toString();
                            Intent in = new Intent(DazzPlainActivity.this,
                                    BannerPreview.class);
                            startActivity(in);
                            // finish();


                        }
                    } else if (types.equalsIgnoreCase("LED")) {
//                  mEmoji.setVisibility(View.GONE);
                        Constant.mDazZType = "LED";
                        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.putBoolean("dazzStatus", false);
                        editor.commit();
                        shapetext = spDazzShapePlain.getSelectedItem().toString();

                        Constant.shapeselected = spDazzShapePlain.getSelectedItem()
                                .toString();
                        Constant.mPreviewSpeed = spDazzSpeedPlain.getSelectedItem()
                                .toString();

                        Constant.mPreviewTextsize = spTextSizePlain.getSelectedItem()
                                .toString();
                        String mDazzText = tvDazzPlain.getText().toString()
                                .trim().replaceAll("\\s++", " ");

                        Constant.mZzleText = mDazzText;
                        Constant.mChatText = mDazzText;

                        ContentValues cv1 = new ContentValues();
                        cv1.put("msg", Constant.mZzleText);
                        cv1.put("shape", spTextSizePlain.getSelectedItem().toString());
                        cv1.put("speed", spDazzSpeedPlain.getSelectedItem().toString());
                        cv1.put("background", spDazzShapePlain.getSelectedItem()
                                .toString());

                        Constant.printMsg("inserted data ::>>11 "
                                + Constant.mZzleText + "   "
                                + spTextSizePlain.getSelectedItem().toString() + "  "
                                + spTextSizePlain.getSelectedItem().toString());
                        insertToLEDDB(cv1);

                        SharedPreferences.Editor e1 = sharedPrefs.edit();
                        e1.putString("lastdazzl", Constant.mZzleText);
                        e1.commit();

                        Constant.mFromDazzLed = true;
                        BannerActivity.mZzleText = tvDazzPlain.getText()
                                .toString();
                        Intent intent = new Intent(DazzPlainActivity.this,
                                BannerActivity.class);
                        startActivity(intent);
//                  finish();

                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please Enter DazZ or select from DazZLib",
                            Toast.LENGTH_SHORT).show();


                }
            }
        });
        btDazzSendPlain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvDazzPlain.getText().toString().trim().length() != 0) {
                    sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("dazzStatus", true);
                    editor.commit();
                    msend = true;
                    if (Constant.mZzle == true) {


                        btDazzSendPlain.setEnabled(false);
                        Constant.mZzleText = tvDazzPlain.getText().toString()
                                .trim();
//                    String bg = mTextBackground.getSelectedItem()
//                            .toString();
//                    Constant.printMsg("bg::::::>>>>>>>>>" + bg);
                        String font = spTextSizePlain.getSelectedItem()
                                .toString();
                        Constant.printMsg("font:::::>>>>>>>>" + font);
                        String speed = spDazzSpeedPlain.getSelectedItem().toString();
                        Constant.printMsg("speed:::>>>" + speed);
//                    String fontcolor = mTextColor.getSelectedItem()
//                            .toString();
//                    Constant.printMsg("textcolor:::>>>" + fontcolor);
                        ContentValues cv = new ContentValues();
                        String mDazzText = tvDazzPlain.getText().toString()
                                .trim().replaceAll("\\s++", " ");

                        Constant.mZzleText = mDazzText;
                        cv.put("msg", mDazzText);
                        cv.put("backgrount", Constant.mPreviewBackground);
                        cv.put("font", font);
                        cv.put("speed", speed);
                        cv.put("fontcolor", Constant.mPreviewTextColor);
                        if (Constant.mFromDazzLib == true) {
                            Constant.mFromDazzLib = false;
                            Constant.printMsg("inside update"
                                    + tvDazzPlain.getText().toString() + "  "
                                    + Constant.mChatText);


                            try {
                                int a = (int) db
                                        .open()
                                        .getDatabaseObj()
                                        .update(Dbhelper.TABLE_ZZLE,
                                                cv,
                                                msg
                                                        + "='"
                                                        + Constant.mChatText
                                                        + "'", null);
                                System.out
                                        .println("No of updated rows in zzle :::::::::"
                                                + a);
                                // Constant.mDazzLib = false;
                            } catch (SQLException e) {
                                System.out
                                        .println("Sql exception in ecpl details ::::::"
                                                + e.toString());
                            } finally {
                                db.close();
                            }

                        } else {

                            // Constant.mZzle = true;

                            if (!Constant.mDazzLib) {
                                if (!mZzleList.contains(Constant.mZzleText)) {

                                    Constant.printMsg("inserting text ::::: "
                                            + Constant.mZzleText
                                            + "    "
                                            + tvDazzPlain.getText()
                                            .toString()
                                            .trim());
                                    Constant.printMsg("insert 3");

                                    insertToDB(cv);
                                    // Constant.mDazzLib = true;
                                    Constant.printMsg("called DazZ if:::>>>");
                                } else {
                                    Constant.printMsg("called DazZ if:::>>>111");
                                    Constant.printMsg("updating text ::::: "
                                            + Constant.mZzleText
                                            + "    "
                                            + tvDazzPlain.getText()
                                            .toString()
                                            .trim());
                                    try {
                                        int a = (int) db
                                                .open()
                                                .getDatabaseObj()
                                                .update(Dbhelper.TABLE_ZZLE,
                                                        cv,
                                                        msg
                                                                + "='"
                                                                + Constant.mChatText
                                                                + "'", null);
                                        System.out
                                                .println("No of updated rows in zzle :::::::::"
                                                        + a);
                                        // Constant.mDazzLib = false;
                                    } catch (SQLException e) {
                                        System.out
                                                .println("Sql exception in ecpl details ::::::"
                                                        + e.toString());
                                    } finally {
                                        db.close();
                                    }
                                }

                            } else {
                                Constant.printMsg("called DazZ if:::>>>222");
                                Constant.printMsg("updating text :::::1 "
                                        + Constant.mZzleText
                                        + "    "
                                        + tvDazzPlain.getText()
                                        .toString().trim());
                                try {
                                    int a = (int) db
                                            .open()
                                            .getDatabaseObj()
                                            .update(Dbhelper.TABLE_ZZLE,
                                                    cv,
                                                    msg
                                                            + "='"
                                                            + Constant.mChatText
                                                            + "'", null);
                                    System.out
                                            .println("No of updated rows in zzle :::::::::"
                                                    + a);
                                    Constant.mDazzLib = false;
                                } catch (SQLException e) {
                                    System.out
                                            .println("Sql exception in ecpl details ::::::"
                                                    + e.toString());
                                } finally {
                                    db.close();
                                }

                            }

                        }

//                    Constant.mPreviewBackground = mTextBackground
//                            .getSelectedItem().toString();
                        Constant.mPreviewText = tvDazzPlain.getText()
                                .toString();
                        Constant.mZzleText = tvDazzPlain.getText()
                                .toString();
                        Constant.mPreviewSpeed = spDazzSpeedPlain.getSelectedItem()
                                .toString();
//                    Constant.mPreviewTextColor = mTextColor
//                            .getSelectedItem().toString();
                        Constant.mPreviewTextsize = spTextSizePlain
                                .getSelectedItem().toString();
                        Constant.mChatText = tvDazzPlain.getText().toString();
                        SharedPreferences.Editor e1 = sharedPrefs.edit();
                        e1.putString("lastdazzl", Constant.mPreviewText);
                        e1.commit();

                        if (Constant.mDazZFromSlider == true) {
                            Constant.mZzleGroup = true;
                            Constant.mDazZFromSlider = false;
                            cv.put("msg", mDazzText);
                            cv.put("backgrount", Constant.mPreviewBackground);
                            cv.put("font", font);
                            cv.put("speed", speed);
                            cv.put("fontcolor", Constant.mPreviewTextColor);
                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.DAY_OF_MONTH, +5);
                            Constant.printMsg("dfreebie date ::::::"
                                    + c.getTime());

                            SimpleDateFormat df1 = new SimpleDateFormat(
                                    "yyyy/MM/dd HH:mm:ss");
                            System.out
                                    .println("last date of freebie::::::::>>>>>>>"
                                            + Constant.zzle);
                            String last = df1.format(c.getTime());
                            Constant.mTimeZzle = last;
                            Constant.printMsg("Date ::>> "
                                    + Constant.mTimeZzle);
                            ContentValues cv1 = new ContentValues();
                            // cv1.put("seen", last);

                            insertToDB(cv);
                            Constant.mDazzLib = false;
                            Intent in = new Intent(DazzPlainActivity.this,
                                    SliderTesting.class);
                            startActivity(in);
                            finish();

                        } else {

                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.DAY_OF_MONTH, +5);
                            Constant.printMsg("dfreebie date ::::::"
                                    + c.getTime());
                            SimpleDateFormat df1 = new SimpleDateFormat(
                                    "yyyy/MM/dd HH:mm:ss");
                            System.out
                                    .println("last date of freebie::::::::>>>>>>>"
                                            + df1.format(c.getTime()));
                            String last = df1.format(c.getTime());
                            Constant.mTimeZzle = last;
                            Constant.printMsg("Date ::>> "
                                    + Constant.mTimeZzle);

                            ContentValues cv1 = new ContentValues();
                            cv1.put("seen", last);

                            insertToDB(cv1);

                            if (Constant.zzleFromgroup) {
                                Constant.mZzleGroup = true;
                                Constant.mZzle = false;
                                Constant.mDazzLib = false;
                                Constant.printMsg("group chatttt");
//                                Intent in = new Intent(DazzPlainActivity.this,
//                                        MUCTest.class);
//                                startActivity(in);
                                finish();
                            } else {
                                Constant.printMsg("chatttt" + Constant.mZzleText);
                                Constant.mDazzLib = false;
//                                Intent in = new Intent(DazzPlainActivity.this,
//                                        ChatTest.class);
//                                startActivity(in);
                                finish();
                            }

                        }
                    } else {
                        Constant.printMsg("called LED :::>>> ");
                        Constant.mZzle = false;

                        shapetext = spDazzShapePlain.getSelectedItem().toString();

                        Constant.shapeselected = spDazzShapePlain.getSelectedItem()
                                .toString();

                        String mDazzText = tvDazzPlain.getText().toString()
                                .trim().replaceAll("\\s++", " ");
                        Constant.mZzleText = mDazzText;
                        ContentValues cv1 = new ContentValues();
                        cv1.put("msg", Constant.mZzleText);
                        cv1.put("shape", spTextSizePlain.getSelectedItem().toString());
                        cv1.put("speed", spDazzSpeedPlain.getSelectedItem().toString());
                        cv1.put("background", spDazzShapePlain.getSelectedItem()
                                .toString());


                        if (Constant.mDazzLib == true) {
                            Constant.mDazzLib = false;
                            Constant.printMsg("inside update"
                                    + tvDazzPlain.getText().toString() + "  "
                                    + Constant.mChatText);


                            try {
                                int a = (int) db
                                        .open()
                                        .getDatabaseObj()
                                        .update(Dbhelper.TABLE_LED,
                                                cv1,
                                                msg
                                                        + "='"
                                                        + Constant.mChatText
                                                        + "'", null);
                                System.out
                                        .println("No of updated rows in zzle :::::::::"
                                                + a);
                                // Constant.mDazzLib = false;
                            } catch (SQLException e) {
                                System.out
                                        .println("Sql exception in ecpl details ::::::"
                                                + e.toString());
                            } finally {
                                db.close();
                            }

                        } else {

                            Constant.mPreviewSpeed = spDazzSpeedPlain.getSelectedItem()
                                    .toString();
                            Constant.mPreviewTextsize = spTextSizePlain.getSelectedItem()
                                    .toString();
//                Constant.printMsg("inserted data ::>> "
//                        + Constant.mZzleText + "   "
//                        + mTextSize.getSelectedItem().toString() + "  "
//                        + mSpeed.getSelectedItem().toString());
                            insertToLEDDB(cv1);

                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.DAY_OF_MONTH, +5);
                            Constant.printMsg("dfreebie date ::::::" + c.getTime());
                            SimpleDateFormat df1 = new SimpleDateFormat(
                                    "yyyy/MM/dd HH:mm:ss");
                            System.out
                                    .println("last date of freebie::::::::>>>>>>>"
                                            + df1.format(c.getTime()));
                            String last = df1.format(c.getTime());
                            Constant.mTimeZzle = last;
                            Constant.printMsg("Date ::>> " + Constant.mTimeZzle);

                            ContentValues cv = new ContentValues();
                            cv.put("seen", last);

                            insertToDB(cv);

                            SharedPreferences.Editor e1 = sharedPrefs.edit();
                            e1.putString("lastdazzl", Constant.mZzleText);
                            e1.commit();

                        }


                        if (Constant.mDazZFromSlider == true) {
                            Constant.mDazZFromSlider = false;
                            Intent in = new Intent(DazzPlainActivity.this,
                                    SliderTesting.class);

                            startActivity(in);
                            finish();
                        } else {

                            if (Constant.zzleFromgroup) {
                                Constant.mBazzleGroup = true;
                                Constant.mBazzle = false;
                                Constant.printMsg("group chatttt");
//                                Intent in = new Intent(DazzPlainActivity.this,
//                                        MUCTest.class);
//                                startActivity(in);
                                finish();
                            } else {
                                Constant.printMsg("chatttt12" + Constant.mZzleText);

//                                Intent in = new Intent(DazzPlainActivity.this,
//                                        ChatTest.class);
//                                startActivity(in);
                                finish();
                            }
                        }
                    }


                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please Enter DazZ or select from DazZLib",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        spDazzBackPlain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String bubblecolor = colorNames[position];
                if (!bubblecolor.equalsIgnoreCase("")) {
                    switch (bubblecolor) {
                        case "Black":
                            tvDazzPlain.setBackgroundResource(R.color.black);
                            Constant.mPreviewBackground = "Black";
                            break;
                        case "Green":
                            tvDazzPlain.setBackgroundResource(R.color.kons_green);
                            Constant.mPreviewBackground = "Green";
                            break;
                        case "Pink":
                            tvDazzPlain.setBackgroundResource(R.color.kons_pink);
                            Constant.mPreviewBackground = "Pink";
                            break;
                        case "Blue":
                            tvDazzPlain.setBackgroundResource(R.color.kons_blue);
                            Constant.mPreviewBackground = "Blue";
                            break;
                        case "White":
                            tvDazzPlain.setBackgroundResource(R.color.white);
                            Constant.mPreviewBackground = "White";
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDazzColorPlain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String bubblecolor = colorNames[position];
                if (!bubblecolor.equalsIgnoreCase("")) {
                    switch (bubblecolor) {
                        case "Black":
                            tvDazzPlain.setTextColor(getResources().getColor(R.color.black));
                            Constant.mPreviewTextColor = "Black";

                            break;
                        case "Green":
                            tvDazzPlain.setTextColor(getResources().getColor(R.color.kons_green));
                            Constant.mPreviewTextColor = "Green";

                            break;
                        case "Pink":
                            tvDazzPlain.setTextColor(getResources().getColor(R.color.kons_pink));
                            Constant.mPreviewTextColor = "Pink";

                            break;
                        case "Blue":
                            tvDazzPlain.setTextColor(getResources().getColor(R.color.kons_blue));
                            Constant.mPreviewTextColor = "Blue";

                            break;
                        case "White":
                            tvDazzPlain.setTextColor(getResources().getColor(R.color.white));
                            Constant.mPreviewTextColor = "White";

                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spDazzTypePlain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String bubblecolor = parent.getSelectedItem().toString();

                if (!bubblecolor.equalsIgnoreCase("")) {
                    switch (bubblecolor) {


                        case "Plain":
//            mSpinnerLayout.setVisibility(View.VISIBLE);
                            System.out.println("tesssssssstttttttt  >>>>>>>>>> " + "Plain");

                            linearDazzShapePlain.setVisibility(View.GONE);
//            mtextshape.setVisibility(View.GONE);

                            if (Constant.mDazzLib) {

                            } else {
                                if (Constant.mDazZType != null) {
                                    if (Constant.mPreviewBackground.equalsIgnoreCase("black")) {
                                        spDazzBackPlain.setSelection(0);

                                    }
                                    if (Constant.mPreviewBackground.equalsIgnoreCase("white")) {
                                        spDazzBackPlain.setSelection(4);

                                    }
                                    if (Constant.mPreviewBackground.equalsIgnoreCase("pink")) {
                                        spDazzBackPlain.setSelection(2);

                                    }
                                    if (Constant.mPreviewBackground.equalsIgnoreCase("green")) {
                                        spDazzBackPlain.setSelection(1);

                                    }
                                    if (Constant.mPreviewBackground.equalsIgnoreCase("blue")) {
                                        spDazzBackPlain.setSelection(3);

                                    }

                                    if (Constant.mPreviewTextColor.equalsIgnoreCase("white")) {
                                        spDazzColorPlain.setSelection(4);

                                    }
                                    if (Constant.mPreviewTextColor.equalsIgnoreCase("black")) {
                                        spDazzColorPlain.setSelection(0);

                                    }
                                    if (Constant.mPreviewTextColor.equalsIgnoreCase("blue")) {
                                        spDazzColorPlain.setSelection(3);

                                    }
                                    if (Constant.mPreviewTextColor.equalsIgnoreCase("pink")) {
                                        spDazzColorPlain.setSelection(2);

                                    }
                                    if (Constant.mPreviewTextColor.equalsIgnoreCase("green")) {
                                        spDazzColorPlain.setSelection(1);

                                    }

                                    if (Constant.mPreviewSpeed.equalsIgnoreCase("slow")) {
                                        spDazzSpeedPlain.setSelection(0);

                                    }
                                    if (Constant.mPreviewSpeed.equalsIgnoreCase("medium")) {
                                        spDazzSpeedPlain.setSelection(1);

                                    }
                                    if (Constant.mPreviewSpeed.equalsIgnoreCase("fast")) {
                                        spDazzSpeedPlain.setSelection(2);

                                    }


                                    if (Constant.mPreviewTextsize.equalsIgnoreCase("small")) {
                                        spTextSizePlain.setSelection(0);


                                    }
                                    if (Constant.mPreviewTextsize.equalsIgnoreCase("medium")) {

                                        spTextSizePlain.setSelection(1);

                                    }
                                    if (Constant.mPreviewTextsize.equalsIgnoreCase("large")) {
                                        spTextSizePlain.setSelection(2);

                                    }
                                } else {
                                    spDazzSpeedPlain.setSelection(1);
                                    spDazzColorPlain.setSelection(4);
                                    spDazzBackPlain.setSelection(0);
                                }
                            }


                            tvDazzPlain.setBackgroundResource(R.color.black);
                            tvDazzPlain.setTextColor(getResources().getColor(R.color.white));

                            spDazzBackPlain.setVisibility(View.VISIBLE);
                            tvDazzBackPlain.setVisibility(View.VISIBLE);
                            spDazzColorPlain.setVisibility(View.VISIBLE);
                            tvDazzColorPlain.setVisibility(View.VISIBLE);
                            spDazzSpeedPlain.setVisibility(View.VISIBLE);
                            tvDazzSpeedPlain.setVisibility(View.VISIBLE);
                            spTextSizePlain.setVisibility(View.VISIBLE);
                            tvDazzTextSizePlain.setVisibility(View.VISIBLE);
                            linearDazzBackPlain.setVisibility(View.VISIBLE);
                            linearDazzColorPlain.setVisibility(View.VISIBLE);
                            Constant.mZzle = true;
                            Constant.mBazzle = false;
//            mEmoji.setVisibility(View.VISIBLE);
                            break;
                        case "LED":
                            System.out.println("tesssssssstttttttt  >>>>>>>>>> " + "LED" + Constant.mPreviewSpeed
                                    + Constant.mPreviewTextsize + Constant.mPreviewTextsize + Constant.shapeselected);

                            spTextSizePlain.setSelection(2);
                            spDazzSpeedPlain.setSelection(1);
                            spDazzColorPlain.setSelection(0);
                            spDazzBackPlain.setSelection(4);
                            tvDazzPlain.setBackgroundResource(R.color.white);
                            tvDazzPlain.setTextColor(getResources().getColor(R.color.black));

                            Constant.printMsg("nbjhvdfhjgvfgvb type led11122299999 ");
                            if (Constant.mPreviewSpeed != null) {
                                if (Constant.mPreviewSpeed.equalsIgnoreCase("Slow")) {
                                    spDazzSpeedPlain.setSelection(0);
                                    Constant.printMsg("nbjhvdfhjgvfgvb type led111 " + Constant.mDazZType);
                                }

                                if (Constant.mPreviewSpeed.equalsIgnoreCase("Medium")) {
                                    spDazzSpeedPlain.setSelection(1);

                                    Constant.printMsg("nbjhvdfhjgvfgvb type led111222 ");
                                }
                                if (Constant.mPreviewSpeed.equalsIgnoreCase("Fast")) {
                                    spDazzSpeedPlain.setSelection(2);
                                    Constant.printMsg("nbjhvdfhjgvfgvb type led111222333 ");

                                }


                                if (Constant.mPreviewTextsize.equalsIgnoreCase("Small")) {
                                    Constant.printMsg("nbjhvdfhjgvfgvb type led2222 " + Constant.mDazZType + Constant.mPreviewSpeed + Constant.mPreviewTextsize);

                                    spTextSizePlain.setSelection(0);

                                }
                                if (Constant.mPreviewTextsize.equalsIgnoreCase("Medium")) {
                                    spTextSizePlain.setSelection(1);
                                    Constant.printMsg("nbjhvdfhjgvfgvb type led111222444 ");


                                }
                                if (Constant.mPreviewTextsize.equalsIgnoreCase("Large")) {
                                    spTextSizePlain.setSelection(2);
                                    Constant.printMsg("nbjhvdfhjgvfgvb type led111222555 ");

                                }
                                if (Constant.shapeselected != null) {
                                    if (Constant.shapeselected.equalsIgnoreCase("Circle")) {
                                        spDazzShapePlain.setSelection(0);
                                        Constant.printMsg("nbjhvdfhjgvfgvb type led111222666 ");

                                    }
                                    if (Constant.shapeselected.equalsIgnoreCase("Star")) {
                                        spDazzShapePlain.setSelection(1);

                                    }
                                    if (Constant.shapeselected.equalsIgnoreCase("Heart")) {
                                        spDazzShapePlain.setSelection(2);
                                        Constant.printMsg("nbjhvdfhjgvfgvb type led111222777 ");

                                    }
                                } else {
                                    spDazzShapePlain.setSelection(0);
                                }
                            }
//            mEmoji.setVisibility(View.GONE);
//            mSpinnerLayout.setVisibility(View.VISIBLE);
                            spDazzBackPlain.setVisibility(View.GONE);
                            spDazzSpeedPlain.setVisibility(View.VISIBLE);
                            tvDazzColorPlain.setVisibility(View.GONE);
                            tvDazzTextSizePlain.setVisibility(View.VISIBLE);
                            tvDazzBackPlain.setVisibility(View.GONE);
                            spDazzColorPlain.setVisibility(View.GONE);
                            linearDazzBackPlain.setVisibility(View.GONE);
                            linearDazzColorPlain.setVisibility(View.GONE);
                            linearDazzShapePlain.setVisibility(View.VISIBLE);

                            tvDazzSpeedPlain.setVisibility(View.VISIBLE);
//            mShape.setVisibility(View.VISIBLE);
//            mtextshape.setVisibility(View.VISIBLE);
                            Constant.mBazzle = true;
                            Constant.mZzle = false;
                            break;

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spTextSizePlain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String bubblecolor = parent.getSelectedItem().toString();
                if (!bubblecolor.equalsIgnoreCase("")) {
                    switch (bubblecolor) {
                        case "Small":
                            tvDazzPlain.setTextSize(14.0f);
                            break;
                        case "Medium":
                            tvDazzPlain.setTextSize(18.0f);
                            break;
                        case "Large":
                            tvDazzPlain.setTextSize(22.0f);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spDazzSpeedPlain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void init() {
        dazzToolBarPlain = (Toolbar) findViewById(R.id.dazzToolBarPlain);
        dazzFramePlain = (FrameLayout) findViewById(R.id.dazzFramePlain);
        imgDazzBackPlain = (ImageView) findViewById(R.id.imgDazzBackPlain);
        imgDazzCopyPlain = (ImageView) findViewById(R.id.imgDazzCopyPlain);
        imgDazzPlainEmoji = (ImageView) findViewById(R.id.imgDazzPlainEmoji);

        dazzAppBarPlain = (LinearLayout) findViewById(R.id.dazzAppBarPlain);
        linearDazzPlainHead = (LinearLayout) findViewById(R.id.linearDazzPlainHead);
        linearDazzTypePlain = (LinearLayout) findViewById(R.id.linearDazzTypePlain);
        linearTextSizePlain = (LinearLayout) findViewById(R.id.linearTextSizePlain);
        linearDazzBackPlain = (LinearLayout) findViewById(R.id.linearDazzBackPlain);
        linearDazzColorPlain = (LinearLayout) findViewById(R.id.linearDazzColorPlain);
        linearDazzSpeedPlain = (LinearLayout) findViewById(R.id.linearDazzSpeedPlain);
        linearDazzButtonPlain = (LinearLayout) findViewById(R.id.linearDazzButtonPlain);
        linearDazzShapePlain = (LinearLayout) findViewById(R.id.linearDazzShapePlain);

        tvDazzPlain = (EditText) findViewById(R.id.tvDazzPlain);
        tvDazzTypePlain = (TextView) findViewById(R.id.tvDazzTypePlain);
        tvDazzTextSizePlain = (TextView) findViewById(R.id.tvDazzTextSizePlain);
        tvDazzBackPlain = (TextView) findViewById(R.id.tvDazzBackPlain);
        tvDazzColorPlain = (TextView) findViewById(R.id.tvDazzColorPlain);
        tvDazzSpeedPlain = (TextView) findViewById(R.id.tvDazzSpeedPlain);
        tvDazzShapePlain = (TextView) findViewById(R.id.tvDazzShapePlain);
        mHeader = (TextView) findViewById(R.id.header);

        spDazzTypePlain = (Spinner) findViewById(R.id.spDazzTypePlain);
        spTextSizePlain = (Spinner) findViewById(R.id.spTextSizePlain);
        spDazzBackPlain = (Spinner) findViewById(R.id.spDazzBackPlain);
        spDazzColorPlain = (Spinner) findViewById(R.id.spDazzColorPlain);
        spDazzSpeedPlain = (Spinner) findViewById(R.id.spDazzSpeedPlain);
        spDazzShapePlain = (Spinner) findViewById(R.id.spDazzShapePlain);

        btDazzPreviewPlain = (Button) findViewById(R.id.btDazzPreviewPlain);
        btDazzSendPlain = (Button) findViewById(R.id.btDazzSendPlain);

        try {
            Constant.typeFace(this, tvDazzPlain);
            Constant.typeFace(this, tvDazzTypePlain);
            Constant.typeFace(this, tvDazzTextSizePlain);
            Constant.typeFace(this, tvDazzBackPlain);
            Constant.typeFace(this, tvDazzColorPlain);
            Constant.typeFace(this, tvDazzSpeedPlain);
            Constant.typeFace(this, tvDazzShapePlain);
            Constant.typeFace(this, mHeader);
            Constant.typeFace(this, btDazzPreviewPlain);
            Constant.typeFace(this, btDazzSendPlain);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dazzScreen() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        Constant.screenWidth = width;
        Constant.screenHeight = height;

        AppBarLayout.LayoutParams mToolBarLayoutParams = new AppBarLayout.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        mToolBarLayoutParams.width = (int) width;
        mToolBarLayoutParams.height = (int) height * 10 / 100;
        mToolBarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        dazzToolBarPlain.setLayoutParams(mToolBarLayoutParams);

        FrameLayout.LayoutParams mBackBtnParams = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBackBtnParams.width = width * 6 / 100;
        mBackBtnParams.height = width * 6 / 100;
        mBackBtnParams.leftMargin = (int) width * 4 / 100;
        mBackBtnParams.gravity = Gravity.START | Gravity.CENTER;
        imgDazzBackPlain.setLayoutParams(mBackBtnParams);

        FrameLayout.LayoutParams mAddBtnParams = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mAddBtnParams.width = (int) width * 7 / 100;
        mAddBtnParams.height = (int) width * 7 / 100;
        mAddBtnParams.rightMargin = (int) width * 5 / 100;
        mAddBtnParams.gravity = Gravity.END | Gravity.CENTER;
        imgDazzCopyPlain.setLayoutParams(mAddBtnParams);

        LinearLayout.LayoutParams mSpinnnerBtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mSpinnnerBtnParams.width = width * 55 / 100;
        mSpinnnerBtnParams.height = height * 6 / 100;
        mSpinnnerBtnParams.gravity = Gravity.CENTER;
        mSpinnnerBtnParams.topMargin = width * 1 / 100;
        mSpinnnerBtnParams.bottomMargin = width * 1 / 100;
        mSpinnnerBtnParams.leftMargin = width * 1 / 100;
        mSpinnnerBtnParams.rightMargin = width * 1 / 100;
        spDazzTypePlain.setLayoutParams(mSpinnnerBtnParams);
        spTextSizePlain.setLayoutParams(mSpinnnerBtnParams);
        spDazzBackPlain.setLayoutParams(mSpinnnerBtnParams);
        spDazzColorPlain.setLayoutParams(mSpinnnerBtnParams);
        spDazzSpeedPlain.setLayoutParams(mSpinnnerBtnParams);
        spDazzShapePlain.setLayoutParams(mSpinnnerBtnParams);
        spDazzTypePlain.setGravity(Gravity.CENTER);
        spTextSizePlain.setGravity(Gravity.CENTER);
        spDazzBackPlain.setGravity(Gravity.CENTER);
        spDazzColorPlain.setGravity(Gravity.CENTER);
        spDazzSpeedPlain.setGravity(Gravity.CENTER);
        spDazzShapePlain.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams mSpinnnerBtnParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mSpinnnerBtnParams1.width = width * 30 / 100;
        mSpinnnerBtnParams1.height = height * 6 / 100;
        mSpinnnerBtnParams1.leftMargin = width * 5 / 100;
        mSpinnnerBtnParams1.gravity = Gravity.CENTER_VERTICAL;
        tvDazzTypePlain.setLayoutParams(mSpinnnerBtnParams1);
        tvDazzTextSizePlain.setLayoutParams(mSpinnnerBtnParams1);
        tvDazzBackPlain.setLayoutParams(mSpinnnerBtnParams1);
        tvDazzColorPlain.setLayoutParams(mSpinnnerBtnParams1);
        tvDazzSpeedPlain.setLayoutParams(mSpinnnerBtnParams1);
        tvDazzShapePlain.setLayoutParams(mSpinnnerBtnParams1);

        tvDazzTextSizePlain.setGravity(Gravity.CENTER | Gravity.LEFT);
        tvDazzBackPlain.setGravity(Gravity.CENTER | Gravity.LEFT);
        tvDazzColorPlain.setGravity(Gravity.CENTER | Gravity.LEFT);
        tvDazzTypePlain.setGravity(Gravity.CENTER | Gravity.LEFT);
        tvDazzSpeedPlain.setGravity(Gravity.CENTER | Gravity.LEFT);
        tvDazzShapePlain.setGravity(Gravity.CENTER | Gravity.LEFT);

        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.width = width;
        mLayoutParams.topMargin = width * 20 / 100;
        mLayoutParams.gravity = Gravity.CENTER;
//        mBtnParams.topMargin = width * 10 / 100;
        linearDazzButtonPlain.setLayoutParams(mLayoutParams);


        LinearLayout.LayoutParams mBtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBtnParams.width = width * 36 / 100;
        mBtnParams.height = height * 6 / 100;
        mBtnParams.gravity = Gravity.CENTER;
        mBtnParams.leftMargin = width * 2 / 100;
        mBtnParams.rightMargin = width * 2 / 100;
//        mBtnParams.topMargin = width * 10 / 100;
        btDazzPreviewPlain.setLayoutParams(mBtnParams);
        btDazzSendPlain.setLayoutParams(mBtnParams);

        LinearLayout.LayoutParams mTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mTextParams.width = width * 96 / 100;
        mTextParams.height = height * 8 / 100;
        mTextParams.gravity = Gravity.CENTER;
        mTextParams.leftMargin = width * 2 / 100;
        mTextParams.rightMargin = width * 2 / 100;
        tvDazzPlain.setLayoutParams(mTextParams);
        tvDazzPlain.setGravity(Gravity.CENTER);

        FrameLayout.LayoutParams mHeaderTextParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        mHeaderTextParams.width = width * 30 / 100;
        mHeaderTextParams.height = width * 6 / 100;
        mHeaderTextParams.gravity = Gravity.CENTER;
        mHeader.setLayoutParams(mHeaderTextParams);

        if (width >= 600) {

            tvDazzTypePlain.setTextSize(17);
            tvDazzTextSizePlain.setTextSize(17);
            tvDazzBackPlain.setTextSize(17);
            tvDazzColorPlain.setTextSize(17);
            tvDazzSpeedPlain.setTextSize(17);
            btDazzPreviewPlain.setTextSize(17);
            btDazzSendPlain.setTextSize(17);
            mHeader.setTextSize(18);

        } else if (width < 600 && width >= 480) {
            tvDazzTypePlain.setTextSize(16);
            tvDazzTextSizePlain.setTextSize(16);
            tvDazzBackPlain.setTextSize(16);
            tvDazzColorPlain.setTextSize(16);
            tvDazzSpeedPlain.setTextSize(16);
            btDazzPreviewPlain.setTextSize(16);
            btDazzSendPlain.setTextSize(16);
            mHeader.setTextSize(17);
        } else if (width < 480 && width >= 320) {
            tvDazzTypePlain.setTextSize(14);
            tvDazzTextSizePlain.setTextSize(14);
            tvDazzBackPlain.setTextSize(14);

            tvDazzColorPlain.setTextSize(14);
            tvDazzSpeedPlain.setTextSize(14);
            btDazzPreviewPlain.setTextSize(14);
            btDazzSendPlain.setTextSize(14);
            mHeader.setTextSize(16);
        } else if (width < 320) {
            tvDazzTypePlain.setTextSize(12);
            tvDazzTextSizePlain.setTextSize(12);
            tvDazzBackPlain.setTextSize(12);
            tvDazzColorPlain.setTextSize(12);
            tvDazzSpeedPlain.setTextSize(12);
            btDazzPreviewPlain.setTextSize(12);
            btDazzSendPlain.setTextSize(12);
            mHeader.setTextSize(15);
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Constant.mZzle = false;
        Constant.mBazzle = false;
//        Intent i = new Intent(DazzPlainActivity.this, SliderTesting.class);
//        startActivity(i);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    protected void insertToDB(ContentValues cv) {
        // TODO Auto-generated method stub
        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_ZZLE, null, cv);
            Constant.printMsg("No of inserted rows in zzle :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in ecpl details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }
    }

    protected void insertToLEDDB(ContentValues cv) {
        // TODO Auto-generated method stub
        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_LED, null, cv);
            Constant.printMsg("No of inserted rows in zzle :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in led details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }
    }

    private void getCount(String query) {
        // TODO Auto-generated method stub
        Cursor c = null;

        try {
            c = db.open().getDatabaseObj().rawQuery(query, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());

            mCount = String.valueOf(c.getCount());

        } catch (SQLException e) {

        } finally {
            c.close();
            db.close();
        }

    }

    private ArrayList<String> collectData(String query) {
        // TODO Auto-generated method stub
        Cursor c = null;

        try {
            c = db.open().getDatabaseObj().rawQuery(query, null);

            Constant.printMsg("The count is::::::>>>> " + c.getCount());

            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    mZzleList.add(String.valueOf(c.getString(0)));

                }

            }

        } catch (Exception e) {
            // TODO: handle exception
        } finally {

            db.close();

        }
        return mZzleList;
    }

    @Override
    protected void onResume() {
        if (Constant.mDazzLib
                || (mZzleList.contains(tvDazzPlain.getText().toString()))) {

            if (Constant.mChatText != null) {
                tvDazzPlain.setText(Constant.mChatText);
            }

            Constant.printMsg("text from library:::::::::: " + Constant.mChatText);

            Constant.mPlainDazZ = sharedPrefs.getBoolean("dazzStatus", false);//


            Constant.printMsg("LIBRARY PLAIN111" + Constant.mPlainDazZ);
            if (Constant.mPlainDazZ) {
                Constant.printMsg("LIBRARY PLAIN");


                if (Constant.mDazzLib) {
                    query_dazz = "select * from " + Dbhelper.TABLE_ZZLE
                            + " where msg" + "='" + Constant.mChatText + "'";
                } else if (tvDazzPlain.getText().toString() != "") {
                    String sChatText = tvDazzPlain.getText().toString();
                    query_dazz = "select * from " + Dbhelper.TABLE_ZZLE
                            + " where msg" + "='" + sChatText + "'";
                }
                Cursor c = null;

                try {
                    c = db.open().getDatabaseObj().rawQuery(query_dazz, null);

                    Constant.printMsg("The count is::::::>>>> " + c.getCount());
                    if (c.getCount() > 0) {

                        if (c.moveToFirst()) {

                            String Textmsg = c.getString(c.getColumnIndex("msg"));

                            slctBackgd = c.getString(c.getColumnIndex("backgrount"));
                            slctTextsize = c.getString(c.getColumnIndex("font"));
                            slctTextcolor = c.getString(c.getColumnIndex("fontcolor"));
                            slctSpeed = c.getString(c.getColumnIndex("speed"));
                            slctType = c.getString(c.getColumnIndex("seen"));

                            Constant.printMsg("Speed" + slctSpeed);
                            Constant.printMsg("Font " + slctTextsize);
                        }
                    }
                    c.close();

                } catch (Exception e) {
                    Constant.printMsg("Sql exception in ecpl details ::::::"
                            + e.toString());
                } finally {

                    db.close();

                }
                Constant.mBackDazzColor = slctBackgd;

                Constant.printMsg("propertiessss::::::: ::::: >>>>> " + slctBackgd + " " + slctTextsize + " " + slctTextcolor + " " + slctSpeed + "  " + slctType);
                spTextSizePlain.setSelection(fontAdapter.getPosition(slctTextsize));
                if (slctBackgd != null) {
                    if (slctBackgd.equalsIgnoreCase("Black")) {
                        spDazzBackPlain.setSelection(0);

                    }
                    if (slctBackgd.equalsIgnoreCase("Green")) {
                        spDazzBackPlain.setSelection(1);
                        Constant.mPreviewBackground = "Green";


                    }
                    if (slctBackgd.equalsIgnoreCase("Pink")) {
                        spDazzBackPlain.setSelection(2);
                        Constant.mPreviewBackground = "Pink";

                    }
                    if (slctBackgd.equalsIgnoreCase("Blue")) {
                        spDazzBackPlain.setSelection(3);
                        Constant.mPreviewBackground = "Blue";

                    }
                    if (slctBackgd.equalsIgnoreCase("White")) {
                        spDazzBackPlain.setSelection(4);
                        Constant.mPreviewBackground = "White";

                    }


                    if (slctTextcolor.equalsIgnoreCase("Black")) {
                        spDazzColorPlain.setSelection(0);
                        Constant.mPreviewTextColor = "Black";

                    }
                    if (slctTextcolor.equalsIgnoreCase("Green")) {
                        spDazzColorPlain.setSelection(1);
                        Constant.mPreviewTextColor = "Green";

                    }
                    if (slctTextcolor.equalsIgnoreCase("Pink")) {
                        spDazzColorPlain.setSelection(2);

                    }
                    if (slctTextcolor.equalsIgnoreCase("Blue")) {
                        spDazzColorPlain.setSelection(3);
                        Constant.mPreviewTextColor = "Blue";

                    }
                    if (slctTextcolor.equalsIgnoreCase("White")) {
                        spDazzColorPlain.setSelection(4);
                        Constant.mPreviewTextColor = "White";

                    }
                }
            } else {
                Constant.printMsg("LIBRARY LED");
                Constant.printMsg("TEST DazZ ::::::");
                spDazzBackPlain.setVisibility(View.GONE);
                tvDazzColorPlain.setVisibility(View.GONE);
                tvDazzBackPlain.setVisibility(View.GONE);
                spDazzColorPlain.setVisibility(View.GONE);
                linearDazzBackPlain.setVisibility(View.GONE);
                linearDazzColorPlain.setVisibility(View.GONE);
                spDazzSpeedPlain.setVisibility(View.VISIBLE);
                tvDazzTextSizePlain.setVisibility(View.VISIBLE);
                linearDazzShapePlain.setVisibility(View.VISIBLE);
                tvDazzSpeedPlain.setVisibility(View.VISIBLE);
                spDazzTypePlain.setSelection(1);


                if (Constant.mDazzLib) {
                    query_dazz = "select * from " + Dbhelper.TABLE_LED
                            + " where msg" + "='" + Constant.mChatText + "'";
                } else if (tvDazzPlain.getText().toString() != "") {
                    String sChatText = tvDazzPlain.getText().toString();
                    query_dazz = "select * from " + Dbhelper.TABLE_LED
                            + " where msg" + "='" + sChatText + "'";
                }
                Cursor c = null;
                Constant.printMsg("LED   Speed query" + query_dazz);

                try {
                    c = db.open().getDatabaseObj().rawQuery(query_dazz, null);

                    Constant.printMsg("The count is::::::>>>> " + c.getCount());
                    if (c.getCount() > 0) {

                        if (c.moveToFirst()) {

                            String Textmsg = c.getString(c.getColumnIndex("msg"));

                            slctTextsize = c.getString(c.getColumnIndex("background"));
                            slctBackgd = c.getString(c.getColumnIndex("shape"));
//                            slctTextcolor = c.getString(c.getColumnIndex("fontcolor"));
                            slctSpeed = c.getString(c.getColumnIndex("speed"));
//                            slctType = c.getString(c.getColumnIndex("seen"));
                            Constant.mPreviewSpeed = slctSpeed;
                            Constant.mPreviewTextsize = slctBackgd;
                            Constant.shapeselected = slctTextsize;

                            Constant.printMsg("LED   Speed" + slctSpeed + "   " + slctTextsize + "   " + slctBackgd);
                            Constant.printMsg("Font " + slctTextsize);
                        }
                    }
                    c.close();

                } catch (Exception e) {
                    Constant.printMsg("Sql exception in ecpl details ::::::"
                            + e.toString());
                } finally {

                    db.close();

                }

                if (Constant.mPreviewTextsize != null) {

                    Constant.printMsg("LED   Speed textled111222555 " + Constant.mPreviewTextsize);

                    if (Constant.mPreviewTextsize.equalsIgnoreCase("Small")) {
                        Constant.printMsg("LED   Speed led2222 " + Constant.mDazZType + Constant.mPreviewSpeed + Constant.mPreviewTextsize);

                        spTextSizePlain.setSelection(0);

                    }
                    if (Constant.mPreviewTextsize.equalsIgnoreCase("Medium")) {
                        spTextSizePlain.setSelection(1);
                        Constant.printMsg("LED   Speed led111222444 ");


                    }
                    if (Constant.mPreviewTextsize.equalsIgnoreCase("Large")) {
                        spTextSizePlain.setSelection(2);
                        Constant.printMsg("LED   Speed led111222555 ");

                    }
                }

                if (Constant.mPreviewSpeed != null) {
                    if (Constant.mPreviewSpeed.equalsIgnoreCase("Slow")) {
                        spDazzSpeedPlain.setSelection(0);
                        Constant.printMsg("LED   Speed led111 " + Constant.mDazZType);
                    }

                    if (Constant.mPreviewSpeed.equalsIgnoreCase("Medium")) {
                        spDazzSpeedPlain.setSelection(1);

                        Constant.printMsg("LED   Speed led111222 ");
                    }
                    if (Constant.mPreviewSpeed.equalsIgnoreCase("Fast")) {
                        spDazzSpeedPlain.setSelection(2);
                        Constant.printMsg("LED   Speed led111222333 ");

                    }
                }

                if (Constant.shapeselected != null) {
                    Constant.printMsg("LED   Speed textshapeled111222555 " + Constant.shapeselected);

                    if (Constant.shapeselected.equalsIgnoreCase("Circle")) {
                        spDazzShapePlain.setSelection(0);
                        Constant.printMsg("LED   Speed led111222666 ");

                    }
                    if (Constant.shapeselected.equalsIgnoreCase("Star")) {
                        spDazzShapePlain.setSelection(1);

                    }
                    if (Constant.shapeselected.equalsIgnoreCase("Heart")) {
                        spDazzShapePlain.setSelection(2);
                        Constant.printMsg("LED   Speed led111222777 ");

                    }
                } else {
                    spDazzShapePlain.setSelection(0);
                }


            }

//            spDazzBackPlain.setSelection(customAdapter.getPosition(slctBackgd));
//            spDazzColorPlain
//                    .setSelection(customAdapter.getPosition(slctTextcolor));
            Constant.printMsg("nbjhvdfhjgvfgvb type led111222101010 ");

            spDazzSpeedPlain.setSelection(speedAdapter.getPosition(slctSpeed));

            // mType.setSelection(textctypeAdapter.getPosition(slctType));

            // mType.setSelection(1);

        }
        super.onResume();
    }
}
