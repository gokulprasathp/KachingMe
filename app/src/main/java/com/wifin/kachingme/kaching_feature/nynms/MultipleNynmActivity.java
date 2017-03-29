package com.wifin.kachingme.kaching_feature.nynms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat.muc_chat.MUCTest;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.util.ChatDictionary;
import com.wifin.kachingme.util.Constant;

/**
 * Created by user on 10/18/2016.
 */
public class MultipleNynmActivity extends HeaderActivity {
    public final static String header = "";
    public static Context context;
    public static MultipleNynmActivity mActivity;
    LinearLayout mMeaningLayoutOne, mMeaningLayoutTwo, mMeaningLayoutThree, mMeaningLayout, mMeaningHedderLayout;
    TextView mNymnLabel, mNymnValue, mActualMsgLabel, mMeaningDeleteOne, mMeaningDeleteTwo, mMeaningDeleteThree, mMeaningEditOne, mMeaningEditTwo, mMeaningEditThree, mMeaningOne, mMeaningTwo, mMeaningThree, mMeaningAddOne;
    ListView mNymnMeaningList;
    TextView mNymnAttachOne, mNymnAttachTwo, mNymnAttachThree;
    int height = 0;
    int width = 0;
    TextView n_lym_edit_img, n_lym_del_img, n_lym_atach_img, n_lym_mess_name, n_lym_abbre_desc;
    ImageView n_lym_close_img;
    Dbhelper db;
    private PopupWindow pwindo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        mActivity = this;
//        setContentView(R.layout.activity_multiple_meaning_nynm);
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.activity_multiple_meaning_nynm, vg);
        mHeading.setText("NynM Meaning");
        mHeaderImg.setVisibility(View.GONE);
        mNextBtn.setVisibility(View.GONE);
        mFooterLayout.setVisibility(View.GONE);
        mFooterView.setVisibility(View.GONE);
        initializeVariable();
        ScreenArrangement();
        this.db = new Dbhelper(getApplicationContext());
        context = this;

        mNymnValue.setText(new NynmActivity().displayNym(Constant.mNymTextValue));

        Constant.printMsg("list size   " + Constant.mNynmMeaningList);
//        ArrayAdapter<String> fontAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, Constant.mNynmMeaningList);
//        fontAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mNymnMeaningList.setAdapter(fontAdapter);


        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MultipleNynmActivity.this, NynmActivity.class);
                startActivity(in);
                finish();
            }
        });

        mNymnMeaningList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String mNymMeaning = Constant.mNynmMeaningList.get(position).toString().trim();
                String mNym = mActualMsgLabel.getText().toString().trim();

                Constant.printMsg("Nynm multi list clicked " + mNym + "  " + mNymMeaning);

                showPopup(mNymMeaning, mNym);

            }
        });
        mMeaningAddOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.mNynmMeaningList.size() == 3) {
                    Toast.makeText(MultipleNynmActivity.this.getApplicationContext(),
                            "NynM is Already Existed 3 Times", Toast.LENGTH_SHORT).show();
                } else {
                    Constant.message = mNymnValue.getText().toString().trim();
                    Constant.abbreviation = "";

                    Intent intent = new Intent(MultipleNynmActivity.this, NynmAddActivity.class);
                    intent.putExtra(header, "add");
                    intent.putExtra("nynmValue", mNymnValue.getText().toString());
                    context.startActivity(intent);
                }
            }
        });

        mMeaningEditOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.message = mNymnValue.getText().toString().trim();
                Constant.abbreviation = mMeaningOne.getText().toString().trim();

                Intent intent = new Intent(MultipleNynmActivity.this, NynmAddActivity.class);
                intent.putExtra(header, "edit");
                context.startActivity(intent);
//                finish();
            }
        });
        mMeaningEditTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.message = mNymnValue.getText().toString().trim();
                Constant.abbreviation = mMeaningTwo.getText().toString().trim();

                Intent intent = new Intent(MultipleNynmActivity.this, NynmAddActivity.class);
                intent.putExtra(header, "edit");
                context.startActivity(intent);
//                finish();
            }
        });
        mMeaningEditThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.message = mNymnValue.getText().toString().trim();
                Constant.abbreviation = mMeaningThree.getText().toString().trim();

                Intent intent = new Intent(MultipleNynmActivity.this, NynmAddActivity.class);
                intent.putExtra(header, "edit");
                context.startActivity(intent);
//                finish();
            }
        });
        mMeaningDeleteOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*String mNym = mNymnValue.getText().toString().trim();
                String mNymMeaning = mMeaningOne.getText().toString().trim();

                for (int i = 0; i < Constant.mNynmMeaningList.size(); i++) {
                    if (Constant.mNynmMeaningList.get(i).equalsIgnoreCase(mNymMeaning)) {
                        Constant.mNynmMeaningList.remove(i);
                    }
                }

                DbDeleteNym(mNym, mNymMeaning);

                if (Constant.mNynmMeaningList.size() > 0) {
                    Intent in = new Intent(MultipleNynmActivity.this,
                            MultipleNynmActivity.class);
                    startActivity(in);
                    finish();
                } else {
                    Intent in = new Intent(MultipleNynmActivity.this,
                            NynmActivity.class);
                    startActivity(in);
                    finish();
                }*/
                dialogDeleteNynm("one");
            }
        });

        mMeaningDeleteTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String mNym = mNymnValue.getText().toString().trim();
                String mNymMeaning = mMeaningTwo.getText().toString().trim();

                for (int i = 0; i < Constant.mNynmMeaningList.size(); i++) {
                    if (Constant.mNynmMeaningList.get(i).equalsIgnoreCase(mNymMeaning)) {
                        Constant.mNynmMeaningList.remove(i);
                    }
                }

                DbDeleteNym(mNym, mNymMeaning);

                Intent in = new Intent(MultipleNynmActivity.this,
                        MultipleNynmActivity.class);
                startActivity(in);
                finish();*/
                dialogDeleteNynm("two");
            }
        });
        mMeaningDeleteThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String mNym = mNymnValue.getText().toString().trim();
                String mNymMeaning = mMeaningThree.getText().toString().trim();

                for (int i = 0; i < Constant.mNynmMeaningList.size(); i++) {
                    if (Constant.mNynmMeaningList.get(i).equalsIgnoreCase(mNymMeaning)) {
                        Constant.mNynmMeaningList.remove(i);
                    }
                }

                DbDeleteNym(mNym, mNymMeaning);

                Intent in = new Intent(MultipleNynmActivity.this,
                        MultipleNynmActivity.class);
                startActivity(in);
                finish();*/
                dialogDeleteNynm("three");
            }
        });
    }

    private void showPopup(final String mNymMeaning, final String mNym) {
        try {
            // We need to get the instance of the LayoutInflater
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_nym);
            dialog.setCancelable(true);
            int x = Constant.screenWidth;
            int y = Constant.screenHeight;
            Constant.printMsg("x :::::::::::" + x);
            Constant.printMsg("y::::::::::::::" + y);

            n_lym_edit_img = (TextView) dialog.findViewById(R.id.n_lym_edit_img);
            n_lym_del_img = (TextView) dialog.findViewById(R.id.n_lym_del_img);
            n_lym_mess_name = (TextView) dialog
                    .findViewById(R.id.n_lym_mess_name);
            n_lym_abbre_desc = (TextView) dialog
                    .findViewById(R.id.n_lym_abbre_desc);
            n_lym_close_img = (ImageView) dialog
                    .findViewById(R.id.n_lym_close_img);
            n_lym_atach_img = (TextView) dialog
                    .findViewById(R.id.n_lym_atach_img);

            Constant.typeFace(this, n_lym_edit_img);
            Constant.typeFace(this, n_lym_del_img);
            Constant.typeFace(this, n_lym_mess_name);
            Constant.typeFace(this, n_lym_abbre_desc);
            Constant.typeFace(this, n_lym_atach_img);

            LinearLayout.LayoutParams close = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            close.width = width * 7 / 100;
            close.height = width * 7 / 100;
            close.gravity = Gravity.CENTER | Gravity.TOP;
            // buttonParamsimg.setMargins(width * 1 / 100, height * 1 / 100,
            // width * 1 / 100, height * 1 / 100);
            n_lym_close_img.setLayoutParams(close);
            // holder.dl.setLayoutParams(buttonParamsimg);

            LinearLayout.LayoutParams buttonParamsimg = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParamsimg.width = width * 15 / 100;
            buttonParamsimg.height = width * 10 / 100;
            buttonParamsimg.setMargins(width * 3 / 100, height * 1 / 100,
                    width * 1 / 100, height * 1 / 100);
            n_lym_edit_img.setLayoutParams(buttonParamsimg);
            n_lym_atach_img.setLayoutParams(buttonParamsimg);
            // holder.dl.setLayoutParams(buttonParamsimg);

            LinearLayout.LayoutParams buttonParamsimg1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParamsimg1.width = width * 15 / 100;
            buttonParamsimg1.height = width * 10 / 100;
            buttonParamsimg1.setMargins(width * 3 / 100, height * 1 / 100,
                    width * 1 / 100, height * 1 / 100);
            // holder.img.setLayoutParams(buttonParamsimg);
            n_lym_del_img.setLayoutParams(buttonParamsimg1);

            LinearLayout.LayoutParams text_lay = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            text_lay.width = x * 60 / 100;
            text_lay.height = width * 10 / 100;
            // text_lay.setMargins(10, 0, 10, 0);
            // text_lay.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            text_lay.gravity = Gravity.CENTER;
            n_lym_mess_name.setLayoutParams(text_lay);

            LinearLayout.LayoutParams text_lay1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            text_lay1.width = x * 60 / 100;
            text_lay1.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            text_lay1.gravity = Gravity.CENTER;

            n_lym_abbre_desc.setLayoutParams(text_lay1);

            if (width >= 600) {

                n_lym_abbre_desc.setTextSize(17);
                n_lym_atach_img.setTextSize(15);
                n_lym_edit_img.setTextSize(15);
                n_lym_del_img.setTextSize(15);

            } else if (width > 501 && width < 600) {

                n_lym_abbre_desc.setTextSize(16);
                n_lym_atach_img.setTextSize(14);
                n_lym_edit_img.setTextSize(14);
                n_lym_del_img.setTextSize(14);

            } else if (width > 260 && width < 500) {

                n_lym_abbre_desc.setTextSize(15);
                n_lym_atach_img.setTextSize(13);
                n_lym_edit_img.setTextSize(13);
                n_lym_del_img.setTextSize(13);

            } else if (width <= 260) {

                n_lym_abbre_desc.setTextSize(14);
                n_lym_atach_img.setTextSize(12);
                n_lym_edit_img.setTextSize(12);
                n_lym_del_img.setTextSize(12);

            }

//            pwindo.setOutsideTouchable(true);
//            pwindo.setFocusable(true);
            n_lym_atach_img.setVisibility(View.VISIBLE);
            n_lym_mess_name.setText(mNym);
            n_lym_edit_img.setVisibility(View.GONE);
            n_lym_del_img.setVisibility(View.GONE);
//            spinpos = pos;
//            Constant.printMsg("spinpos::" + spinpos + "pos::" + pos);
//            ArrayList<String> replacePosition = new ArrayList<String>();
//
//            for (int i = 0; i < meaning_val.toString().length(); i++) {
//                char tempCharecter = meaning_val.toString().charAt(i);
//                if (tempCharecter == Constant.mNynmsSpecialCharacter) {
//                    replacePosition.add(String.valueOf(i));
//
//                    Constant.printMsg("spinpos::" + spinpos + "pos::");
//
//                }
//
//            }
//
//            SpannableString redSpannable = new SpannableString(meaning_val);
//
//            for (int i = 0; i < replacePosition.size(); i++) {
//                redSpannable.setSpan(
//                        new ForegroundColorSpan(Color.parseColor("#00123654")),
//                        Integer.valueOf(replacePosition.get(i).toString()),
//                        Integer.valueOf(replacePosition.get(i).toString()) + 1,
//                        33);
//
//            }
//            Constant.printMsg("spinpos::" + spinpos + "pos::" + meaning_val);
//            SpannableStringBuilder builder = new SpannableStringBuilder();
//            builder.append(redSpannable);
//
            n_lym_abbre_desc.setText(new NynmActivity().displayNym(mNymMeaning));

            n_lym_edit_img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    Constant.printMsg("n_lym_edit_img;::");
                    Constant.message = mNym;
                    Constant.abbreviation = mNymMeaning;
//                    Constant.message = value;
//                    //
//                    Constant.abbreviation = meaning_val;
//
//                    Constant.NymPosition = spinpos;
//
//
//
                    Intent intent = new Intent(MultipleNynmActivity.this, NynmAddActivity.class);
                    intent.putExtra(header, "edit");
                    context.startActivity(intent);
                    finish();

                }
            });
            n_lym_del_img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    Constant.printMsg("n_lym_del_img;::");
//                    Constant.printMsg("positionnn::::>>>>>>>>" + spinpos);
                    for (int i = 0; i < Constant.mNynmMeaningList.size(); i++) {
                        if (Constant.mNynmMeaningList.get(i).equalsIgnoreCase(mNymMeaning)) {
                            Constant.mNynmMeaningList.remove(i);
                        }
                    }


                    DbDeleteNym(mNym, mNymMeaning);
                    pwindo.dismiss();

                    Intent in = new Intent(MultipleNynmActivity.this,
                            MultipleNynmActivity.class);
                    startActivity(in);
                }
            });

            n_lym_close_img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Constant.printMsg("n_lym_close_img;::");
                    dialog.cancel();

                }
            });

            n_lym_atach_img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    if (Constant.mNynmFromSlider) {
                        System.out.println("test123 ::::::: slider");

                        Constant.attachNym = true;

                        Constant.attchnymstring = " <n>"
                                + mNym + "1"
                                + "</n>" + "<m>"
                                + mNymMeaning + "1"
                                + "</m>";
                        Intent intent = new Intent(MultipleNynmActivity.this,
                                SliderTesting.class);
                        startActivity(intent);
                        finish();
                    } else {
                        if (Constant.mNynmFromGroup) {
                            Constant.attachNym = true;


                            Constant.message = mNym;
                            Constant.abbreviation = mNymMeaning;
                            Constant.attchnymstring = " <n>"
                                    + mNym + "1"
                                    + "</n>" + "<m>"
                                    + mNymMeaning + "1"
                                    + "</m>";
                            Intent intent = new Intent(MultipleNynmActivity.this,
                                    MUCTest.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Constant.attachNym = true;

                            Constant.attchnymstring = " <n>"
                                    + mNym + "1"
                                    + "</n>" + "<m>"
                                    + mNymMeaning + "1"
                                    + "</m>";
                            Intent intent = new Intent(MultipleNynmActivity.this,
                                    ChatTest.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });
            dialog.show();
            // pwindo.showAsDropDown(dic_list, 55, 30);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void ScreenArrangement() {

        DisplayMetrics metrix = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrix);

        width = metrix.widthPixels;
        height = metrix.heightPixels;

        LinearLayout.LayoutParams nymLabelText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nymLabelText.width = (int) width * 25 / 100;
        nymLabelText.height = (int) height * 5 / 100;
        nymLabelText.topMargin = height * 10 / 100;
        nymLabelText.leftMargin = width * 5 / 100;

        mNymnLabel.setLayoutParams(nymLabelText);

        LinearLayout.LayoutParams mActualMsgLabelText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mActualMsgLabelText.width = (int) width * 96 / 100;
        mActualMsgLabelText.height = (int) height * 5 / 100;
        mActualMsgLabelText.topMargin = height * 10 / 100;
        mActualMsgLabelText.leftMargin = width * 2 / 100;
        mActualMsgLabelText.rightMargin = width * 2 / 100;
        mMeaningHedderLayout.setLayoutParams(mActualMsgLabelText);
        mMeaningHedderLayout.setPadding(10, 5, 5, 5);

        LinearLayout.LayoutParams nymLabelTextvalueParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nymLabelTextvalueParams.width = (int) width * 60 / 100;
        nymLabelTextvalueParams.height = (int) height * 5 / 100;
        nymLabelTextvalueParams.topMargin = height * 10 / 100;
        nymLabelTextvalueParams.leftMargin = width * 5 / 100;
        mNymnValue.setLayoutParams(nymLabelTextvalueParams);
        mNymnValue.setPadding(10, 10, 10, 10);

        LinearLayout.LayoutParams nymvalueListParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nymvalueListParams.width = (int) width * 96 / 100;
//        nymvalueListParams.topMargin = height * 1 / 100;
        nymvalueListParams.leftMargin = width * 2 / 100;
        nymvalueListParams.rightMargin = width * 2 / 100;

//        mNymnMeaningList.setLayoutParams(nymvalueListParams);
        mMeaningLayout.setLayoutParams(nymvalueListParams);

        LinearLayout.LayoutParams nymvalueList1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nymvalueList1Params.width = (int) width * 94 / 100;
        nymvalueList1Params.height = (int) height * 64 / 100;
        nymvalueList1Params.topMargin = height * 1 / 100;
        nymvalueList1Params.leftMargin = width * 1 / 100;
        nymvalueList1Params.rightMargin = width * 1 / 100;

        mNymnMeaningList.setLayoutParams(nymvalueList1Params);
//        mMeaningLayout.setLayoutParams(nymvalueList1Params);

        LinearLayout.LayoutParams mAddBtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mAddBtnParams.width = (int) width * 8 / 100;
        mAddBtnParams.height = (int) width * 8 / 100;
        mAddBtnParams.rightMargin = width * 2 / 100;
        mAddBtnParams.topMargin = width * 1 / 100;

        mHeaderImg.setLayoutParams(mAddBtnParams);


        LinearLayout.LayoutParams deleteImgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        deleteImgParams.width = (int) width * 7 / 100;
        deleteImgParams.height = (int) width * 7 / 100;
        deleteImgParams.gravity = Gravity.CENTER;
        deleteImgParams.leftMargin = width * 2 / 100;
        deleteImgParams.rightMargin = width * 2 / 100;
        mMeaningDeleteOne.setLayoutParams(deleteImgParams);
        mMeaningDeleteTwo.setLayoutParams(deleteImgParams);
        mMeaningDeleteThree.setLayoutParams(deleteImgParams);
        mMeaningDeleteOne.setGravity(Gravity.CENTER);
        mMeaningDeleteTwo.setGravity(Gravity.CENTER);
        mMeaningDeleteThree.setGravity(Gravity.CENTER);
        mMeaningEditOne.setLayoutParams(deleteImgParams);
        mMeaningEditTwo.setLayoutParams(deleteImgParams);
        mMeaningEditThree.setLayoutParams(deleteImgParams);
        mMeaningEditOne.setGravity(Gravity.CENTER);
        mMeaningEditTwo.setGravity(Gravity.CENTER);
        mMeaningEditThree.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams editImgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editImgParams.width = (int) width * 7 / 100;
        editImgParams.height = (int) width * 7 / 100;
        editImgParams.gravity = Gravity.CENTER;
//        editImgParams.leftMargin = width * 2 / 100;
        editImgParams.rightMargin = width * 2 / 100;
        mNymnAttachOne.setLayoutParams(editImgParams);
        mNymnAttachTwo.setLayoutParams(editImgParams);
        mNymnAttachThree.setLayoutParams(editImgParams);

        LinearLayout.LayoutParams editImgParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editImgParams1.width = (int) width * 7 / 100;
        editImgParams1.height = (int) width * 7 / 100;
        editImgParams1.gravity = Gravity.CENTER_VERTICAL;
//        editImgParams.leftMargin = width * 2 / 100;
        editImgParams1.leftMargin = (int) width * 65 / 100;
        mMeaningAddOne.setLayoutParams(editImgParams1);
//        mMeaningAddOne.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.width = (int) width * 96 / 100;
        mMeaningLayoutOne.setLayoutParams(layoutParams);
        mMeaningLayoutTwo.setLayoutParams(layoutParams);
        mMeaningLayoutThree.setLayoutParams(layoutParams);
        mMeaningLayoutThree.setPadding(width * 2 / 100, width * 3 / 100, width * 2 / 100, width * 3 / 100);
        mMeaningLayoutTwo.setPadding(width * 2 / 100, width * 3 / 100, width * 2 / 100, width * 3 / 100);
        mMeaningLayoutOne.setPadding(width * 2 / 100, width * 3 / 100, width * 2 / 100, width * 3 / 100);


        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.width = (int) width * 60 / 100;
//        editTextParams.height = (int) width * 70 / 100;
//        nymvalueListParams.topMargin = height * 1 / 100;
//        editImgParams.leftMargin = width * 2 / 100;
        mMeaningOne.setLayoutParams(editTextParams);
        mMeaningTwo.setLayoutParams(editTextParams);
        mMeaningThree.setLayoutParams(editTextParams);
        mMeaningLayoutThree.setPadding(width * 2 / 100, width * 2 / 100, width * 2 / 100, width * 2 / 100);
        mMeaningLayoutTwo.setPadding(width * 2 / 100, width * 2 / 100, width * 2 / 100, width * 2 / 100);
        mMeaningLayoutOne.setPadding(width * 2 / 100, width * 2 / 100, width * 2 / 100, width * 2 / 100);

        if (width >= 600) {

            mNymnValue.setTextSize(17);
            mActualMsgLabel.setTextSize(17);
            mNymnLabel.setTextSize(17);


        } else if (width < 600 && width >= 480) {
            mNymnValue.setTextSize(16);
            mActualMsgLabel.setTextSize(16);
            mNymnLabel.setTextSize(16);

        } else if (width < 480 && width >= 320) {
            mNymnValue.setTextSize(14);
            mActualMsgLabel.setTextSize(14);
            mNymnLabel.setTextSize(14);

        } else if (width < 320) {
            mNymnValue.setTextSize(12);
            mActualMsgLabel.setTextSize(12);
            mNymnLabel.setTextSize(12);

        }

        if (Constant.mNynmMeaningList.size() == 1) {
            mMeaningLayoutThree.setVisibility(View.GONE);
            mMeaningLayoutTwo.setVisibility(View.GONE);
            mMeaningAddOne.setVisibility(View.VISIBLE);
            mMeaningOne.setText(new NynmActivity().displayNym(Constant.mNynmMeaningList.get(0).toString()));
        }

        if (Constant.mNynmMeaningList.size() == 2) {
            mMeaningLayoutThree.setVisibility(View.GONE);
            mMeaningOne.setText(new NynmActivity().displayNym(Constant.mNynmMeaningList.get(0).toString()));
            mMeaningTwo.setText(new NynmActivity().displayNym(Constant.mNynmMeaningList.get(1).toString()));
        }
        if (Constant.mNynmMeaningList.size() == 3) {
            mMeaningOne.setText(new NynmActivity().displayNym(Constant.mNynmMeaningList.get(0).toString()));
            mMeaningTwo.setText(new NynmActivity().displayNym(Constant.mNynmMeaningList.get(1).toString()));
            mMeaningThree.setText(new NynmActivity().displayNym(Constant.mNynmMeaningList.get(2).toString()));
        }

        mNymnAttachOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mNymMeaning = Constant.mNynmMeaningList.get(0).toString().trim();
                String mNym = mNymnValue.getText().toString().trim();

                Constant.printMsg("Nynm multi list clicked 1" + mNym + "  " + mNymMeaning);

//                showPopup(mNymMeaning, mNym);

                if (Constant.mNynmFromSlider) {
                    System.out.println("test123 ::::::: slider");

                    Constant.attachNym = true;

                    Constant.attchnymstring = " <n>"
                            + mNym + "1"
                            + "</n>" + "<m>"
                            + mNymMeaning + "1"
                            + "</m>";
                    Intent intent = new Intent(MultipleNynmActivity.this,
                            SliderTesting.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (Constant.mNynmFromGroup) {
                        Constant.attachNym = true;


                        Constant.message = mNym;
                        Constant.abbreviation = mNymMeaning;
                        Constant.attchnymstring = " <n>"
                                + mNym + "1"
                                + "</n>" + "<m>"
                                + mNymMeaning + "1"
                                + "</m>";

                        if (NynmActivity.mActivity != null)
                            NynmActivity.mActivity.finish();
                        if (NynmAddActivity.mActivity != null)
                            NynmAddActivity.mActivity.finish();

//                        Intent intent = new Intent(MultipleNynmActivity.this,
//                                MUCTest.class);
//                        startActivity(intent);
                        finish();
                    } else {
                        Constant.attachNym = true;
                        if (NynmActivity.mActivity != null)
                            NynmActivity.mActivity.finish();
                        if (NynmAddActivity.mActivity != null)
                            NynmAddActivity.mActivity.finish();
                        Constant.attchnymstring = " <n>"
                                + mNym + "1"
                                + "</n>" + "<m>"
                                + mNymMeaning + "1"
                                + "</m>";
//                        Intent intent = new Intent(MultipleNynmActivity.this,
//                                ChatTest.class);
//                        startActivity(intent);
                        finish();
                    }
                }

            }
        });

        mNymnAttachTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mNymMeaning = Constant.mNynmMeaningList.get(1).toString().trim();
                String mNym = mNymnValue.getText().toString().trim();

                Constant.printMsg("Nynm multi list clicked 1" + mNym + "  " + mNymMeaning);

//                showPopup(mNymMeaning, mNym);
                if (Constant.mNynmFromSlider) {
                    System.out.println("test123 ::::::: slider");

                    Constant.attachNym = true;

                    Constant.attchnymstring = " <n>"
                            + mNym + "1"
                            + "</n>" + "<m>"
                            + mNymMeaning + "1"
                            + "</m>";
                    Intent intent = new Intent(MultipleNynmActivity.this,
                            SliderTesting.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (Constant.mNynmFromGroup) {
                        Constant.attachNym = true;


                        Constant.message = mNym;
                        Constant.abbreviation = mNymMeaning;
                        Constant.attchnymstring = " <n>"
                                + mNym + "1"
                                + "</n>" + "<m>"
                                + mNymMeaning + "1"
                                + "</m>";
                        Intent intent = new Intent(MultipleNynmActivity.this,
                                MUCTest.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Constant.attachNym = true;

                        Constant.attchnymstring = " <n>"
                                + mNym + "1"
                                + "</n>" + "<m>"
                                + mNymMeaning + "1"
                                + "</m>";
                        Intent intent = new Intent(MultipleNynmActivity.this,
                                ChatTest.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        mNymnAttachThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mNymMeaning = Constant.mNynmMeaningList.get(2).toString().trim();
                String mNym = mNymnValue.getText().toString().trim();

                Constant.printMsg("Nynm multi list clicked 1" + mNym + "  " + mNymMeaning);

//                showPopup(mNymMeaning, mNym);

                if (Constant.mNynmFromSlider) {
                    System.out.println("test123 ::::::: slider");

                    Constant.attachNym = true;

                    Constant.attchnymstring = " <n>"
                            + mNym + "1"
                            + "</n>" + "<m>"
                            + mNymMeaning + "1"
                            + "</m>";
                    Intent intent = new Intent(MultipleNynmActivity.this,
                            SliderTesting.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (Constant.mNynmFromGroup) {
                        Constant.attachNym = true;


                        Constant.message = mNym;
                        Constant.abbreviation = mNymMeaning;
                        Constant.attchnymstring = " <n>"
                                + mNym + "1"
                                + "</n>" + "<m>"
                                + mNymMeaning + "1"
                                + "</m>";
                        Intent intent = new Intent(MultipleNynmActivity.this,
                                MUCTest.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Constant.attachNym = true;

                        Constant.attchnymstring = " <n>"
                                + mNym + "1"
                                + "</n>" + "<m>"
                                + mNymMeaning + "1"
                                + "</m>";
                        Intent intent = new Intent(MultipleNynmActivity.this,
                                ChatTest.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    private void initializeVariable() {

        mMeaningDeleteOne = (TextView) findViewById(R.id.nymn_delete_one);
        mMeaningDeleteTwo = (TextView) findViewById(R.id.nymn_delete_two);
        mMeaningDeleteThree = (TextView) findViewById(R.id.nymn_delete_three);
        mMeaningEditOne = (TextView) findViewById(R.id.nymn_edit_one);
        mMeaningEditTwo = (TextView) findViewById(R.id.nymn_edit_two);
        mMeaningEditThree = (TextView) findViewById(R.id.nymn_edit_three);
        mMeaningOne = (TextView) findViewById(R.id.nymn_meaning_one);
        mMeaningTwo = (TextView) findViewById(R.id.nymn_meaning_two);
        mMeaningThree = (TextView) findViewById(R.id.nymn_meaning_three);
        mNymnLabel = (TextView) findViewById(R.id.nymn_label);
        mNymnValue = (TextView) findViewById(R.id.nymn_value);
        mMeaningAddOne = (TextView) findViewById(R.id.nymn_add_one);
        mNymnAttachOne = (TextView) findViewById(R.id.nymn_attach_one);
        mNymnAttachTwo = (TextView) findViewById(R.id.nymn_attach_two);
        mNymnAttachThree = (TextView) findViewById(R.id.nymn_attach_three);

        mActualMsgLabel = (TextView) findViewById(R.id.nymn_meaning_label);
        mMeaningLayout = (LinearLayout) findViewById(R.id.meaning_layout);
        mMeaningLayoutOne = (LinearLayout) findViewById(R.id.layout_one);
        mMeaningLayoutTwo = (LinearLayout) findViewById(R.id.layout_two);
        mMeaningLayoutThree = (LinearLayout) findViewById(R.id.layout_three);
        mMeaningHedderLayout = (LinearLayout) findViewById(R.id.nymn_meaning_layout);
        mNymnMeaningList = (ListView) findViewById(R.id.meaning_list);

        Constant.typeFace(this, mMeaningDeleteOne);
        Constant.typeFace(this, mMeaningDeleteTwo);
        Constant.typeFace(this, mMeaningDeleteThree);
        Constant.typeFace(this, mMeaningEditOne);
        Constant.typeFace(this, mMeaningEditTwo);
        Constant.typeFace(this, mMeaningEditThree);
        Constant.typeFace(this, mMeaningOne);
        Constant.typeFace(this, mMeaningTwo);
        Constant.typeFace(this, mMeaningThree);
        Constant.typeFace(this, mNymnLabel);
        Constant.typeFace(this, mNymnValue);
        Constant.typeFace(this, mActualMsgLabel);
    }

    public void dialogDeleteNynm(final String positionDelete) {
        AlertDialog.Builder alertDeleteNynm = new AlertDialog.Builder(this);
        alertDeleteNynm.setMessage("Are You Sure To Delete.?");
        alertDeleteNynm.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mNym = mNymnValue.getText().toString().trim();
                String mNymMeaning = "";

                if (positionDelete == "one") {
                    mNymMeaning = mMeaningOne.getText().toString().trim();
                } else if (positionDelete == "two") {
                    mNymMeaning = mMeaningTwo.getText().toString().trim();
                } else if (positionDelete == "three") {
                    mNymMeaning = mMeaningThree.getText().toString().trim();
                }

                for (int i = 0; i < Constant.mNynmMeaningList.size(); i++) {
                    if (Constant.mNynmMeaningList.get(i).equalsIgnoreCase(mNymMeaning)) {
                        Constant.mNynmMeaningList.remove(i);
                    }
                }

                DbDeleteNym(mNym, mNymMeaning);

                dialog.dismiss();

                if (Constant.mNynmMeaningList.size() > 0) {
                    Intent in = new Intent(MultipleNynmActivity.this, MultipleNynmActivity.class);
                    startActivity(in);
                    finish();
                } else {
                    Intent in = new Intent(MultipleNynmActivity.this, NynmActivity.class);
                    startActivity(in);
                    finish();
                }
            }
        });
        alertDeleteNynm.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDeleteNynm.setCancelable(false);
        alertDeleteNynm.show();
    }

    public void DbDeleteNym(String pos, String mNymMeaning) {
        // TODO Auto-generated method stub

        // String dl = Constant.addedNyms.get(pos).getText().toString();

        Cursor c = null;
        String query = "DELETE FROM " + Dbhelper.TABLE_NYM + " WHERE name='" + pos
                + "'" + "AND meaning='" + mNymMeaning
                + "'";
        Constant.printMsg("deletee QuErYYYYY:::::::: " + query);

        try {
            c = db.open().getDatabaseObj().rawQuery(query, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
            ChatDictionary.mDictionaryList.remove(pos);
            ChatDictionary.mDictionaryMeaningList.remove(mNymMeaning);

        } catch (SQLException e) {

        } finally {
            c.close();
            db.close();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        queryPosition(Constant.mNymnQuirePosition);


    }

    private void queryPosition(String value) {
        // TODO Auto-generated method stub

        Constant.mNymnQuirePosition = value;

        Constant.mNynmMeaningList.clear();

        Cursor c = null;
        db = new Dbhelper(getApplicationContext());
        Constant.printMsg("queryiiiiiiis:" + value);
        try {
            c = db.open().getDatabaseObj().rawQuery(value, null);
            // c =db
            Constant.printMsg("The queryiiiiiiis count is ::::::::"
                    + c.getCount());

            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    int meaning = c.getColumnIndex("meaning");
                    String meaning_val = c.getString(meaning);

                    Constant.mNynmMeaningList.add(meaning_val);

                    Constant.printMsg("value::" + value + "meaning_val::"
                            + meaning_val);
                }
            }
        } catch (SQLException e) {

        } finally {

            Constant.printMsg("fjasfalkf" + Constant.mNynmMeaningList.size());

            if (Constant.mNynmMeaningList.size() == 1) {
                mMeaningLayoutThree.setVisibility(View.GONE);
                mMeaningLayoutTwo.setVisibility(View.GONE);
                mMeaningAddOne.setVisibility(View.VISIBLE);
                mMeaningOne.setText(new NynmActivity().displayNym(Constant.mNynmMeaningList.get(0).toString()));

            }

            if (Constant.mNynmMeaningList.size() == 2) {
                mMeaningLayoutThree.setVisibility(View.GONE);
                mMeaningLayoutTwo.setVisibility(View.VISIBLE);
                mMeaningOne.setText(new NynmActivity().displayNym(Constant.mNynmMeaningList.get(0).toString()));
                mMeaningTwo.setText(new NynmActivity().displayNym(Constant.mNynmMeaningList.get(1).toString()));
//            mMeaningAddOne.setVisibility(View.INVISIBLE);
            }
            if (Constant.mNynmMeaningList.size() == 3) {
                mMeaningLayoutThree.setVisibility(View.VISIBLE);
                mMeaningOne.setText(new NynmActivity().displayNym(Constant.mNynmMeaningList.get(0).toString()));
                mMeaningTwo.setText(new NynmActivity().displayNym(Constant.mNynmMeaningList.get(1).toString()));
                mMeaningThree.setText(new NynmActivity().displayNym(Constant.mNynmMeaningList.get(2).toString()));
//            mMeaningAddOne.setVisibility(View.INVISIBLE);
            }
            c.close();
            db.close();
        }
    }
}
