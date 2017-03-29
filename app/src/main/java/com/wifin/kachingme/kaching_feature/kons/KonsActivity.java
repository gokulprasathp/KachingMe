package com.wifin.kachingme.kaching_feature.kons;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.CustomAdapter;
import com.wifin.kachingme.adaptors.HLVAdapter;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.emojicons.emojicon.EmojiconFragmentGroup;
import com.wifin.kachingme.emojicons.emojicon.EmojiconGridFragment;
import com.wifin.kachingme.emojicons.emojicon.LogoEmojiconsFragment;
import com.wifin.kachingme.emojicons.emojicon.emoji.Emojicon;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Constant;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class KonsActivity extends HeaderActivity implements AdapterView.OnItemSelectedListener, EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconFragmentGroup.OnEmojiconBackspaceClickedListener {

    LinearLayout mMainLayout, mHeaderTextLayout, mSpinnerLayout, mEditTextLayout,mShapeLayout,mBtnLayout;
    ImageView mBackBtn, Bubble_img;
    CheckBox mSelectAllBtn;
    TextView mSpinnerHeader, mSelectShapeText;
    int height = 0;
    int width = 0;
    public static KonsActivity mActivity;
    FrameLayout FrameImageWithText_layout;
    TextView Cancel_Btn, Add_Btn;

    String[] colorNames = {"Blue", "Green", "Pink"};
    int flags[] = {R.color.kons_blue, R.color.kons_green, R.color.kons_pink};
    Spinner spin;
    EditText Bubble_edit_text;
    TextView Bubble_text;
    int ans;

    private HorizontalListView hlv;
    private HLVAdapter hlvAdapter;
    ArrayList<String> alName;
    ArrayList<Integer> alImage;
    String mShape;
    String mColor;

    Dbhelper db;
    LinearLayout emoji_frag, emoticonsCover;
    GridView gridview_emo;
    ImageButton mEmoji;
    int count1 = 0;
    int konsLength = 0;
    String mFinalText = "";
    String mText = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.kons_activity);
        getSupportActionBar().hide();
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.kons_activity, vg);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mActivity=this;

        db = new Dbhelper(getApplicationContext());
        mHeading.setText("KonS");
        mNextBtn.setVisibility(View.GONE);
        mHeaderImg.setImageResource(R.drawable.kons_img);
        initialisation();
        screenArrangement();
        mShape = "oval_left";
        Bubble_edit_text.setBackground(null);
        Bubble_edit_text.clearFocus();
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in = new Intent(KonsActivity.this, KonsHomeScreen.class);
//                startActivity(in);
                finish();
            }
        });
        mEmoji.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (emoji_frag.getVisibility() == View.VISIBLE) {
                    emoji_frag.setVisibility(View.GONE);
                    mEmoji.setImageResource(R.drawable.emoji_btn_normal);
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .toggleSoftInput(InputMethodManager.SHOW_FORCED,
                                    InputMethodManager.HIDE_IMPLICIT_ONLY);
                    mSpinnerLayout.setVisibility(View.VISIBLE);
                    mShapeLayout.setVisibility(View.VISIBLE);
                    mBtnLayout.setVisibility(View.VISIBLE);
                    mFooterView.setVisibility(View.VISIBLE);
                    mFooterLayout.setVisibility(View.VISIBLE);




                } else {
                    mEmoji.setImageResource(R.drawable.ic_action_hardware_keyboard);

                    emoji_frag.setVisibility(View.VISIBLE);
                    mSpinnerLayout.setVisibility(View.GONE);
                    mShapeLayout.setVisibility(View.VISIBLE);
                    mBtnLayout.setVisibility(View.GONE);
                    mFooterView.setVisibility(View.GONE);
                    mFooterLayout.setVisibility(View.GONE);



//
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(Bubble_edit_text.getWindowToken(),
                                    0);


                }
            }
        });
        gridview_emo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

Constant.printMsg("gridview touch test");
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;

//                int action = event.getAction();
//            switch (action) {
//                case MotionEvent.ACTION_DOWN:
//                    // Disallow ScrollView to intercept touch events.
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                    break;
//
//                case MotionEvent.ACTION_UP:
//                    // Allow ScrollView to intercept touch events.
//                    v.getParent().requestDisallowInterceptTouchEvent(false);
//                    break;
//            }
//
//            // Handle ListView touch events.
//            v.onTouchEvent(event);
//
//
//                return true;



            }
        });
        Bubble_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                int total_count = Bubble_edit_text.getText().toString().length();


                int length = s.length();
                int value = length - count1;

                if (total_count == 0){
                    konsLength = 0;
                }

                if (konsLength < 0) {
                    konsLength = 0;
                }
                if (value == 2) {
                    konsLength = konsLength + 1;
                } else if (value == 1) {
                    konsLength = konsLength + 1;
                } else if (count <= 0) {
                    konsLength = konsLength + 1;
                }

                count1 = s.length();


                Constant.printMsg("count::::::>>>" + count1 + "  " + length
                        + "  " + count + "   " + konsLength + "   " + Bubble_edit_text.getText().length());


                if (konsLength >= 4 && Bubble_edit_text.getText().length() >= 3) {
                    Constant.printMsg("count::::::>>> if" + count1 + "  " + length
                            + "  " + count + "   " + konsLength + "   " + Bubble_edit_text.getText().length());
                    InputFilter[] lengthFilter = new InputFilter[1];
                    lengthFilter[0] = new InputFilter.LengthFilter(s.length());
                    Constant.printMsg("count::::::>>> iffilter"+lengthFilter);
                            Bubble_edit_text.setFilters(lengthFilter);
                    Constant.typeFaceKons(getApplicationContext(),Bubble_text);

//                  final  InputFilter filter = new InputFilter() {
//                        public CharSequence filter(CharSequence source, int start, int end,
//                                                   Spanned dest, int dstart, int dend) {
//                            for (int i = start; i < end; i++) {
//                                if (!Character.isLetterOrDigit(source.charAt(i))) {
//                                    return "";
//                                }
//                            }
//                            return null;
//                        }
//                    };
//                    Bubble_edit_text.setFilters(filter);


                    Bubble_text.setText(Bubble_edit_text.getText().toString());
//                    Bubble_text.setTypeface(Typeface.createFromAsset(
//                            getAssets(), "billo.TTF"));
                    Bubble_text.setTextColor(Color.WHITE);
                    Toast.makeText(getApplicationContext(),
                            "You can't type more than four character",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Constant.printMsg("count::::::>>> else" + count1 + "  " + length
                            + "  " + count + "   " + konsLength + "   " + Bubble_edit_text.getText().length());
                    InputFilter[] lengthFilter = new InputFilter[1];
                    lengthFilter[0] = new InputFilter.LengthFilter(8);
                    Bubble_edit_text.setFilters(lengthFilter);
                    mText = Bubble_edit_text.getText().toString().trim();
                    Bubble_text.setText(Bubble_edit_text.getText().toString());
//                    Bubble_text.setTypeface(Typeface.createFromAsset(
//                            getAssets(), "billo.TTF"));
                    Constant.typeFaceKons(getApplicationContext(),Bubble_text);

                    Bubble_text.setTextColor(Color.WHITE);
                    mFinalText.concat("" + s);

                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        Add_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Bubble_edit_text.getText().toString().trim().length() > 0) {
                    Bubble_img.setBackgroundResource(R.color.white);
                    Bitmap b = ((BitmapDrawable) Bubble_img.getDrawable()).getBitmap();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    Bubble_img.setDrawingCacheEnabled(true);
                    Bubble_img.layout(0, 0, Bubble_img.getWidth(), Bubble_img.getHeight());
                    Bubble_img.buildDrawingCache(true);
                    b = Bitmap.createBitmap(Bubble_img.getDrawingCache());
                    b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    Bubble_img.setDrawingCacheEnabled(false);
                    byte[] img = bos.toByteArray();


                    String text = Bubble_edit_text.getText().toString().trim();
                    ContentValues cv = new ContentValues();
                    cv.put("msg", text);

                    cv.put("image", img);
                    cv.put("background", mShape);
                    cv.put("color", mColor);

                    insertToDB(cv);
//                    Intent in = new Intent(KonsActivity.this,
//                            KonsHomeScreen.class);
//                    startActivity(in);
//                    finish();


//                    Intent i = new Intent(KonsActivity.this, KonsHomeScreen.class);

                    Bubble_img.buildDrawingCache();
                    Bitmap image = Bubble_img.getDrawingCache();

                    Bundle extras = new Bundle();
                    extras.putParcelable("imagebitmap", image);
                    extras.putString("text", Bubble_edit_text.getText().toString());

//                    i.putExtras(extras);
//                    startActivity(i);
                    finish();
                }
            }
        });
        Cancel_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        alName = new ArrayList<>(Arrays.asList("Seven", "Six", "Five", "Four", "Three", "Two", "One"));
        alImage = new ArrayList<>(Arrays.asList(R.drawable.kons_format_seven, R.drawable.kons_format_six, R.drawable.kons_format_five, R.drawable.kons_format_four, R.drawable.kons_format_three, R.drawable.kons_format_two, R.drawable.kons_format_one));
        hlvAdapter = new HLVAdapter(KonsActivity.this, alName, alImage);
        hlv.setAdapter(hlvAdapter);

        hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String bubblecolor = alName.get(position);
                if (!bubblecolor.equalsIgnoreCase("")) {
                    switch (bubblecolor) {
                        case "Seven":
                            mShape = "cloud_left";
                            mColor = "Pink";

                            Bubble_img.setImageResource(R.drawable.kons_format_seven);

                            break;
                        case "Six":
                            mShape = "cross_rect_left";
                            mColor = "Green";

                            Bubble_img.setImageResource(R.drawable.kons_format_six);


                            break;
                        case "Five":
                            mShape = "rect_right";
                            mColor = "Pink";

                            Bubble_img.setImageResource(R.drawable.kons_format_five);

                            break;
                        case "Four":
                            mShape = "rect_left";
                            mColor = "Green";

                            Bubble_img.setImageResource(R.drawable.kons_format_four);

                            break;
                        case "Three":
                            mShape = "oval_right";
                            mColor = "Pink";

                            Bubble_img.setImageResource(R.drawable.kons_format_three);

                            break;
                        case "Two":
                            mShape = "cross_rect_right";
                            mColor = "Green";

                            Bubble_img.setImageResource(R.drawable.kons_format_two);


                            break;
                        case "One":
                            mShape = "oval_left";
                            mColor = "Blue";

                            Bubble_img.setImageResource(R.drawable.kons_format_one);

                            break;
                    }
                }
            }
        });

        spin.setOnItemSelectedListener(this);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), flags, colorNames);
        spin.setAdapter(customAdapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String bubblecolor = colorNames[position];
                mColor = bubblecolor;
                if (!mShape.equalsIgnoreCase("oval")) {
                    if (!bubblecolor.equalsIgnoreCase("")) {
                        switch (bubblecolor) {
                            case "Blue":
                                if (mShape.equalsIgnoreCase("oval_left")) {
                                    Bubble_img.setImageResource(R.drawable.blue_one);
                                }
                                if (mShape.equalsIgnoreCase("cross_rect_right")) {
                                    Bubble_img.setImageResource(R.drawable.blue_two);
                                }
                                if (mShape.equalsIgnoreCase("oval_right")) {
                                    Bubble_img.setImageResource(R.drawable.blue_three);
                                }
                                if (mShape.equalsIgnoreCase("rect_left")) {
                                    Bubble_img.setImageResource(R.drawable.blue_four);
                                }
                                if (mShape.equalsIgnoreCase("rect_right")) {
                                    Bubble_img.setImageResource(R.drawable.blue_five);
                                }
                                if (mShape.equalsIgnoreCase("cross_rect_left")) {
                                    Bubble_img.setImageResource(R.drawable.blue_six);
                                }
                                if (mShape.equalsIgnoreCase("cloud_left")) {
                                    Bubble_img.setImageResource(R.drawable.blue_seven);
                                }
                                break;
                            case "Green":
                                if (mShape.equalsIgnoreCase("oval_left")) {
                                    Bubble_img.setImageResource(R.drawable.green_one);
                                }
                                if (mShape.equalsIgnoreCase("cross_rect_right")) {
                                    Bubble_img.setImageResource(R.drawable.green_two);
                                }
                                if (mShape.equalsIgnoreCase("oval_right")) {
                                    Bubble_img.setImageResource(R.drawable.green_three);
                                }
                                if (mShape.equalsIgnoreCase("rect_left")) {
                                    Bubble_img.setImageResource(R.drawable.green_four);
                                }
                                if (mShape.equalsIgnoreCase("rect_right")) {
                                    Bubble_img.setImageResource(R.drawable.green_five);
                                }
                                if (mShape.equalsIgnoreCase("cross_rect_left")) {
                                    Bubble_img.setImageResource(R.drawable.green_six);
                                }
                                if (mShape.equalsIgnoreCase("cloud_left")) {
                                    Bubble_img.setImageResource(R.drawable.green_seven);
                                }
                                break;
                            case "Pink":
                                if (mShape.equalsIgnoreCase("oval_left")) {
                                    Bubble_img.setImageResource(R.drawable.pink_one);
                                }
                                if (mShape.equalsIgnoreCase("cross_rect_right")) {
                                    Bubble_img.setImageResource(R.drawable.pink_two);
                                }
                                if (mShape.equalsIgnoreCase("oval_right")) {
                                    Bubble_img.setImageResource(R.drawable.pink_three);
                                }
                                if (mShape.equalsIgnoreCase("rect_left")) {
                                    Bubble_img.setImageResource(R.drawable.pink_four);
                                }
                                if (mShape.equalsIgnoreCase("rect_right")) {
                                    Bubble_img.setImageResource(R.drawable.pink_five);
                                }
                                if (mShape.equalsIgnoreCase("cross_rect_left")) {
                                    Bubble_img.setImageResource(R.drawable.pink_six);
                                }
                                if (mShape.equalsIgnoreCase("cloud_left")) {
                                    Bubble_img.setImageResource(R.drawable.pink_seven);
                                }
                                break;
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please select the Shape", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


        Bubble_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String bubbletxt = Bubble_edit_text.getText().toString().trim();
                Bubble_text.setText(bubbletxt);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String bubbletxt = Bubble_edit_text.getText().toString().trim();
                Bubble_text.setText(bubbletxt);
            }
        });
        Bubble_edit_text.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Constant.printMsg("oooooooooooooo1111111111" + konsLength);

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (konsLength < 0) {
                        konsLength = 0;
                    } else {
                        konsLength = konsLength - 1;
                        Constant.printMsg("oooooooooooooo" + konsLength);
                    }


                } else {
                    Constant.printMsg("oooooooooooooovvvvvvvv" + konsLength);

                }
                return false;
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initialisation() {

        mMainLayout = (LinearLayout) findViewById(R.id.main_layout);
        mContentLayout = (LinearLayout) findViewById(R.id.content_layout);
        mHeaderTextLayout = (LinearLayout) findViewById(R.id.header_text_layout);
        mSpinnerLayout = (LinearLayout) findViewById(R.id.select_color_layout);
        mEditTextLayout = (LinearLayout) findViewById(R.id.text_layout);
        mShapeLayout  = (LinearLayout) findViewById(R.id.select_shapes_layout);
        mBtnLayout = (LinearLayout) findViewById(R.id.Cancel_Add_layout);
        mBackBtn = (ImageView) findViewById(R.id.back_btn);
        hlv = (HorizontalListView) findViewById(R.id.hlv);
        FrameImageWithText_layout = (FrameLayout) findViewById(R.id.FrameImageWithText_layout);
        Cancel_Btn = (TextView) findViewById(R.id.Cancel_Btn);
        Add_Btn = (TextView) findViewById(R.id.Add_Btn);
        Bubble_img = (ImageView) findViewById(R.id.Bubble_img);

//        mSelectAllBtn = (CheckBox) findViewById(R.id.select_all);
        spin = (Spinner) findViewById(R.id.spinner_color);
        Bubble_edit_text = (EditText) findViewById(R.id.Bubble_edit_text);
        Bubble_text = (TextView) findViewById(R.id.Bubble_text);
        mSpinnerHeader = (TextView) findViewById(R.id.select_color_text);
        mSelectShapeText = (TextView) findViewById(R.id.select_shape_text);
        mEmoji = (ImageButton) findViewById(R.id.btn_emo);
        emoji_frag = (LinearLayout) findViewById(R.id.emoji_frag);
        gridview_emo = (GridView) findViewById(R.id.gridview_emo);
        emoticonsCover = (LinearLayout) findViewById(R.id.footer_for_emoticons);

        Constant.typeFace(this,Cancel_Btn);
        Constant.typeFace(this,Add_Btn);
        Constant.typeFace(this,Bubble_edit_text);
        Constant.typeFace(this,Bubble_text);
        Constant.typeFace(this,mSpinnerHeader);
        Constant.typeFace(this,mSelectShapeText);
    }

    public void screenArrangement() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        Constant.screenWidth = width;
        Constant.screenHeight = height;

        LinearLayout.LayoutParams mAddBtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mAddBtnParams.width = (int) width * 8 / 100;
        mAddBtnParams.height = (int) width * 5 / 100;
        mAddBtnParams.rightMargin = width * 2 / 100;

        mHeaderImg.setLayoutParams(mAddBtnParams);


        LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mainLayoutParams.width = width * 96 / 100;
        mainLayoutParams.height = width * 20 / 100;
        mainLayoutParams.gravity = Gravity.CENTER;
        mainLayoutParams.leftMargin = width * 2 / 100;
        mainLayoutParams.topMargin = width * 1 / 100;
        mainLayoutParams.bottomMargin = width * 1 / 100;
        hlv.setLayoutParams(mainLayoutParams);

        LinearLayout.LayoutParams spinnertextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        spinnertextParams.width = width;
        spinnertextParams.height = width * 6 / 100;
        spinnertextParams.gravity = Gravity.CENTER;
        spinnertextParams.topMargin = width * 4 / 100;
        spinnertextParams.leftMargin = width * 2 / 100;
        spinnertextParams.bottomMargin = width * 1 / 100;
        mSpinnerHeader.setLayoutParams(spinnertextParams);
        mSelectShapeText.setLayoutParams(spinnertextParams);

        LinearLayout.LayoutParams spinnerLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        spinnerLayoutParams.width = width * 96 / 100;
        spinnerLayoutParams.height = height * 18 / 100;
        spinnerLayoutParams.gravity = Gravity.CENTER;
        spinnerLayoutParams.leftMargin = width * 2 / 100;
        spinnerLayoutParams.rightMargin = width * 2 / 100;
        spinnerLayoutParams.topMargin = width * 1 / 100;

        mSpinnerLayout.setLayoutParams(spinnerLayoutParams);


        LinearLayout.LayoutParams cancelbtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cancelbtnParams.width = width * 40 / 100;
        cancelbtnParams.height = height * 8 / 100;
        cancelbtnParams.leftMargin = width * 6 / 100;
        cancelbtnParams.bottomMargin = width * 10 / 100;
        Cancel_Btn.setLayoutParams(cancelbtnParams);
        Cancel_Btn.setGravity(Gravity.CENTER);


        LinearLayout.LayoutParams okaytextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        okaytextParams.width = width * 40 / 100;
        okaytextParams.height = height * 8 / 100;
        okaytextParams.leftMargin = width * 8 / 100;
        okaytextParams.bottomMargin = width * 10 / 100;
        Add_Btn.setLayoutParams(okaytextParams);
        Add_Btn.setGravity(Gravity.CENTER);


        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.width = width * 80 / 100;
        editTextParams.height = height * 6 / 100;
        editTextParams.leftMargin = width * 1 / 100;
        editTextParams.rightMargin = width * 1 / 100;
        editTextParams.gravity = Gravity.CENTER;
        Bubble_edit_text.setPadding(10, 10, 10, 10);
        Bubble_edit_text.setLayoutParams(editTextParams);


        LinearLayout.LayoutParams editTextlayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextlayoutParams.width = width * 96 / 100;
        editTextlayoutParams.height = height * 6 / 100;
        editTextlayoutParams.leftMargin = width * 2 / 100;
        editTextlayoutParams.rightMargin = width * 2 / 100;
        editTextlayoutParams.gravity = Gravity.CENTER;

        mEditTextLayout.setLayoutParams(editTextlayoutParams);

        FrameLayout.LayoutParams bubbleImgParams = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        bubbleImgParams.width = width * 50 / 100;
        bubbleImgParams.height = height * 15 / 100;
        bubbleImgParams.leftMargin = width * 25 / 100;
        Bubble_img.setLayoutParams(bubbleImgParams);




        LinearLayout.LayoutParams Textparams3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Textparams3.topMargin = width * 1 / 100;
        Textparams3.height = height * 55 / 100;
        emoticonsCover.setLayoutParams(Textparams3);
        emoji_frag.setLayoutParams(Textparams3);

        LinearLayout.LayoutParams spinnerlayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        spinnerlayoutParams.width = width * 94 / 100;
        spinnerlayoutParams.height = height * 6 / 100;
        spinnerlayoutParams.leftMargin = width * 1 / 100;
        spinnerlayoutParams.rightMargin = width * 1 / 100;
        spinnerlayoutParams.topMargin = width * 1 / 100;
        spinnerlayoutParams.bottomMargin = width * 1 / 100;
        spinnerlayoutParams.gravity = Gravity.CENTER;

        spin.setLayoutParams(spinnerlayoutParams);

        LinearLayout.LayoutParams emojiParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        emojiParams.width = width * 9 / 100;
        emojiParams.height = width * 9 / 100;
        emojiParams.leftMargin = width * 3 / 100;
        emojiParams.gravity = Gravity.CENTER;
        mEmoji.setLayoutParams(emojiParams);

        if (width >= 600) {

            Add_Btn.setTextSize(17);
            Bubble_edit_text.setTextSize(17);
            Cancel_Btn.setTextSize(17);
            mSelectShapeText.setTextSize(17);
            mSpinnerHeader.setTextSize(17);


        } else if (width < 600 && width >= 480) {
            Add_Btn.setTextSize(16);
            Bubble_edit_text.setTextSize(16);
            Cancel_Btn.setTextSize(16);
            mSelectShapeText.setTextSize(16);
            mSpinnerHeader.setTextSize(16);


        } else if (width < 480 && width >= 320) {
            Add_Btn.setTextSize(14);
            Bubble_edit_text.setTextSize(14);
            Cancel_Btn.setTextSize(14);

            mSelectShapeText.setTextSize(14);
            mSpinnerHeader.setTextSize(14);

        } else if (width < 320) {
            Add_Btn.setTextSize(12);
            Bubble_edit_text.setTextSize(12);
            Cancel_Btn.setTextSize(12);
            mSelectShapeText.setTextSize(12);
            mSpinnerHeader.setTextSize(12);


        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getApplicationContext(), colorNames[position], Toast.LENGTH_LONG).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    protected void insertToDB(ContentValues cv) {
        // TODO Auto-generated method stub
        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_KONS, null, cv);
            Constant.printMsg("No of inserted rows in kons :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in kons details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }
    }

    @Override
    public void onBackPressed()
    {
        // TODO Auto-generated method stub

        if (emoji_frag.getVisibility() == View.VISIBLE)
        {
            emoji_frag.setVisibility(View.GONE);
            mEmoji.setImageResource(R.drawable.emoji_btn_normal);
            mSpinnerLayout.setVisibility(View.VISIBLE);
            mShapeLayout.setVisibility(View.VISIBLE);
            mBtnLayout.setVisibility(View.VISIBLE);
            mFooterView.setVisibility(View.VISIBLE);
            mFooterLayout.setVisibility(View.VISIBLE);
        }
        else
        {
//            Intent i = new Intent(KonsActivity.this, KonsHomeScreen.class);
//            startActivity(i);
            finish();

            super.onBackPressed();
        }
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        LogoEmojiconsFragment.backspace(Bubble_edit_text);

    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        LogoEmojiconsFragment.input(Bubble_edit_text, emojicon);

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Kons Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.kaching.me.login.kons/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Kons Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.kaching.me.login.kons/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }
//    gri.setOnTouchListener(new ListView.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            int action = event.getAction();
//            switch (action) {
//                case MotionEvent.ACTION_DOWN:
//                    // Disallow ScrollView to intercept touch events.
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                    break;
//
//                case MotionEvent.ACTION_UP:
//                    // Allow ScrollView to intercept touch events.
//                    v.getParent().requestDisallowInterceptTouchEvent(false);
//                    break;
//            }
//
//            // Handle ListView touch events.
//            v.onTouchEvent(event);
//            return true;
//        }
//    });

}
