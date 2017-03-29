package com.wifin.kachingme.kaching_feature.nynms;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.l4digital.fastscroll.FastScrollRecyclerView;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat.muc_chat.MUCTest;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.util.ChatDictionary;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import a_vcard.android.provider.Contacts;

public class NynmActivity extends HeaderActivity implements OnItemClickListenerInterface, AdapterView.OnItemClickListener {
    public final static String header = "";
    public static NynmActivity mActivity;
    public static Context context;
    public ArrayList<String> mNymText;
    public ArrayList<Integer> idNymText;
    FastScrollRecyclerView nynmFastRecyclerView;
    List<String> nynmListValues = new ArrayList();
    NynmAdapter nynmAdapter = new NynmAdapter();
    int height, width;
    float heightinDp, widthinDp;
    String TAG = NynmActivity.class.getSimpleName();
    Dbhelper db;
    String meaning_val;
    int spinpos;
    TextView n_lym_edit_img, n_lym_del_img, n_lym_atach_img;
    TextView dichead, n_lym_mess_name, n_lym_abbre_desc;
    ImageView n_lym_close_img;
    int mNymnMeaningCount = 0;
    private PopupWindow pwindo;

    public static Context getContext() {
        // TODO Auto-generated method stub
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_nynm);
        getSupportActionBar().hide();
        mActivity = this;
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.activity_nynm, vg);
        this.db = new Dbhelper(getApplicationContext());
        mHeading.setText("NynM");
        mHeaderImg.setImageResource(R.drawable.nymn);
        mNextBtn.setImageResource(R.drawable.add);
        mFooterLayout.setVisibility(View.GONE);
        mFooterView.setVisibility(View.GONE);
        Constant.mNynmMeaningList.clear();

        this.mNymText = new ArrayList();
        this.idNymText = new ArrayList();
        init();
        nynmScreen();
        fetchNymFrom();

       /* if (this.mNymText.size() <= 5) {
            this.sideSelector.setVisibility(View.INVISIBLE);
        }*/
        this.db = new Dbhelper(context);
        context = this;

        ChatDictionary.mDictionaryList.clear();
        ChatDictionary.mDictionaryMeaningList.clear();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseContext());
        nynmFastRecyclerView.setLayoutManager(linearLayoutManager);
        nynmFastRecyclerView.setHasFixedSize(true);

//        imgNynmBack.setOnClickListener(this);
//        imgNynmAdd.setOnClickListener(this);

        nynmListValues = new ArrayList();

        for (int nym = 0; nym < 10; nym++) {
            nynmListValues.add("Message : " + nym);
        }
        if (Constant.addedNyms.size() > 0) {

            for (int i = 0; i < Constant.addedNyms.size(); i++) {

                LinkedList<String> hs = new LinkedList();
                hs.addAll(ChatDictionary.mDictionaryList);
                ChatDictionary.mDictionaryList.clear();
                ChatDictionary.mDictionaryList.addAll(hs);

                LinkedList<String> hs1 = new LinkedList();
                hs1.addAll(ChatDictionary.mDictionaryMeaningList);
                ChatDictionary.mDictionaryMeaningList.clear();
                ChatDictionary.mDictionaryMeaningList.addAll(hs1);

                Constant.printMsg("nyms::::>>>" + Constant.addedNyms.size());
                Constant.printMsg("nyms constant::>>>>>"
                        + ChatDictionary.mDictionaryList + "  "
                        + ChatDictionary.mDictionaryMeaningList);

                String ny = Constant.addedNyms.get(i).getText().toString();
                String nymm = Constant.addedNyms.get(i).getMeaning().toString();

                if (ChatDictionary.mDictionaryList.size() > 0) {
                    Constant.printMsg("List Size"
                            + ChatDictionary.mDictionaryList.size());
                    ChatDictionary.mDictionaryList.add(ny);
                    ChatDictionary.mDictionaryMeaningList.add(nymm);
                } else {
                    ChatDictionary.mDictionaryList.add(ny);
                    ChatDictionary.mDictionaryMeaningList.add(nymm);
                }
            }

        }

        mNextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent i = new Intent(NynmActivity.this, NynmAddActivity.class);
                i.putExtra(header, "add");
                startActivity(i);
                finish();

            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

//                Intent i = new Intent(NynmActivity.this,
//                        SliderTesting.class);
//                startActivity(i);
                finish();

            }
        });
        HashSet set = new HashSet();
        set.addAll(mNymText);
        mNymText.clear();
        mNymText.addAll(set);
        Collections.sort(mNymText);
        if (mNymText != null && mNymText.size() > 0) {
            nynmFastRecyclerView.setHideScrollbar(false);
        } else {
            nynmFastRecyclerView.setHideScrollbar(true);
        }
        nynmAdapter = new NynmAdapter(NynmActivity.this, this.mNymText);
        nynmFastRecyclerView.setAdapter(nynmAdapter);
    }

    public void init() {
        nynmFastRecyclerView = (FastScrollRecyclerView) findViewById(R.id.nynm_fastScrollerRecyclerView);
    }

    private void nynmScreen() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        LinearLayout.LayoutParams mAddBtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mAddBtnParams.width = (int) width * 8 / 100;
        mAddBtnParams.height = (int) width * 8 / 100;
        mAddBtnParams.rightMargin = width * 2 / 100;
        mAddBtnParams.topMargin = width * 1 / 100;
        mHeaderImg.setLayoutParams(mAddBtnParams);
    }

    public void fetchNymFrom() {
        SQLException e;
        Throwable th;
        Dbhelper dbhelper = new Dbhelper(getApplicationContext());
        Cursor c = null;
        this.mNymText.clear();
        this.idNymText.clear();
        try {
            String query = "SELECT * FROM nym ORDER BY name ";
            Dbhelper db = new Dbhelper(getApplicationContext());
            try {
                c = db.open().getDatabaseObj().rawQuery(query, null);
                int idIndex = c.getColumnIndex("id");
                int txnm = c.getColumnIndex(Contacts.PeopleColumns.NAME);
                int mnnm = c.getColumnIndex("meaning");
                Constant.printMsg("The pending cart list in db ::::"
                        + c.getCount());
                if (c.getCount() > 0) {
                    while (c.moveToNext()) {
                        String tx = c.getString(txnm);
                        String mn = c.getString(mnnm);
                        Integer idnm = Integer.valueOf(c.getInt(idIndex));
                        System.out
                                .println("dbadd:nym:" + tx + "  " + mn + "  ");
                        this.mNymText.add(tx);
                        this.idNymText.add(idnm);
                    }
                } else {
                    Constant.printMsg("there is nothing in db::");
                }
                c.close();
                db.close();
                dbhelper = db;
            } catch (SQLException e2) {
                e = e2;
                dbhelper = db;
            } catch (Throwable th2) {
                th = th2;
                dbhelper = db;
            }
        } catch (SQLException e3) {
            e = e3;
            try {
                Constant.printMsg("Sql exception in pending shop details ::::"
                        + e.toString());
                c.close();
                dbhelper.close();
            } catch (Throwable th3) {
                th = th3;
                c.close();
                dbhelper.close();

            }
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

//        Intent i = new Intent(NynmActivity.this, SliderTesting.class);
//        startActivity(i);
        finish();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //you can get the clicked item from the adapter using its position
        String value = parent.getItemAtPosition(position).toString();
        int idNym = ((Integer) NynmActivity.this.idNymText
                .get(position)).intValue();
        Constant.NymPosition = idNym;

        NynmActivity.this
                .queryPosition("SELECT * FROM nym WHERE id = " + idNym);
        NynmActivity.this.initiatePopupWindow(value, idNym);
    }

    private void queryPosition(String value) {
        // TODO Auto-generated method stub

        Constant.mNymnQuirePosition = value;

        Cursor c = null;
        db = new Dbhelper(getApplicationContext());
        Constant.printMsg("queryiiiiiiis:" + value);
        try {
            c = db.open().getDatabaseObj().rawQuery(value, null);
            // c =db
            Constant.printMsg("The queryiiiiiiis count is ::::::::"
                    + c.getCount());

            mNymnMeaningCount = c.getCount();

            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    int meaning = c.getColumnIndex("meaning");
                    meaning_val = c.getString(meaning);

                    Constant.mNynmMeaningList.add(meaning_val);

                    Constant.printMsg("value::" + value + "meaning_val::"
                            + meaning_val);
                }
            }
        } catch (SQLException e) {

        } finally {
            c.close();
            db.close();
        }
    }

    public void DbDeleteNym(String pos) {
        // TODO Auto-generated method stub

        // String dl = Constant.addedNyms.get(pos).getText().toString();
        Cursor c = null;
        String q = "DELETE FROM " + Dbhelper.TABLE_NYM + " WHERE name='" + pos
                + "'";
        try {
            c = db.open().getDatabaseObj().rawQuery(q, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
        } catch (SQLException e) {

        } finally {
            c.close();
            db.close();
        }

        Constant.addedNyms.remove(pos);

        ChatDictionary.mDictionaryList.remove(pos);
        ChatDictionary.mDictionaryMeaningList.remove(pos);

    }

    private void initiatePopupWindow(final String value, int pos) {
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

            checkHideButtons();


            n_lym_mess_name.setText(displayNym(value), TextView.BufferType.SPANNABLE);
            spinpos = pos;
            Constant.printMsg("spinpos::" + spinpos + "pos::" + pos);
            ArrayList<String> replacePosition = new ArrayList<String>();

            for (int i = 0; i < meaning_val.toString().length(); i++) {
                char tempCharecter = meaning_val.toString().charAt(i);
                if (tempCharecter == Constant.mNynmsSpecialCharacter) {
                    replacePosition.add(String.valueOf(i));

                    Constant.printMsg("spinpos::" + spinpos + "pos::");

                }

            }

            SpannableString redSpannable = new SpannableString(meaning_val);

            for (int i = 0; i < replacePosition.size(); i++) {
                redSpannable.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#00123654")),
                        Integer.valueOf(replacePosition.get(i).toString()),
                        Integer.valueOf(replacePosition.get(i).toString()) + 1,
                        33);

            }
            Constant.printMsg("spinpos::" + spinpos + "pos::" + meaning_val);
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(redSpannable);

            n_lym_abbre_desc.setText(builder, TextView.BufferType.SPANNABLE);

            n_lym_edit_img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    Constant.printMsg("n_lym_edit_img;::");
                    // Constant.message = Constant.addedNyms.get(spinpos)
                    // .getText();
                    // Constant.abbreviation = Constant.addedNyms.get(spinpos)
                    // .getMeaning();
                    Constant.message = value;
                    //
                    Constant.abbreviation = meaning_val;

                    Constant.NymPosition = spinpos;


                    Intent intent = new Intent(NynmActivity.this, NynmAddActivity.class);
//                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(header, "edit");
                    context.startActivity(intent);
                    finish();

                }
            });
            n_lym_del_img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Constant.printMsg("n_lym_del_img;::");
                    Constant.printMsg("positionnn::::>>>>>>>>" + spinpos);
                    DbDeleteNym(value);
                    dialog.cancel();
                    nynmAdapter.notifyDataSetChanged();
//                    dic_list.invalidateViews();
//                    dic_list.refreshDrawableState();
                    Intent in = new Intent(NynmActivity.this,
                            NynmActivity.class);
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
                    dialog.cancel();

                    if (Constant.mNynmFromSlider) {
                        Constant.attachNym = true;
                        Constant.attchnymstring = " <n>"
                                + value + "1"
                                + "</n>" + "<m>"
                                + meaning_val + "1"
                                + "</m>";
                        Intent intent = new Intent(NynmActivity.this,
                                SliderTesting.class);
                        startActivity(intent);
                        finish();
                    } else {


                        if (Constant.mNynmFromGroup) {
                            Constant.attachNym = true;
                            Constant.mNynmFromSlider = false;

                            Constant.attchnymstring = " <n>"
                                    + value + "1"
                                    + "</n>" + "<m>"
                                    + meaning_val + "1"
                                    + "</m>";
                            Intent intent = new Intent(NynmActivity.this,
                                    MUCTest.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Constant.attachNym = true;

                            Constant.attchnymstring = " <n>"
                                    + value + "1"
                                    + "</n>" + "<m>"
                                    + meaning_val + "1"
                                    + "</m>";
                            Intent intent = new Intent(NynmActivity.this,
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


    @Override
    public void onClick(View view, int position) {
//        String value = parent.getItemAtPosition(position).toString();
        Constant.mNynmMeaningList.clear();

        String value = this.mNymText.get(position);
        int idNym = ((Integer) NynmActivity.this.idNymText
                .get(position)).intValue();
        Constant.printMsg("kadalaaaaaaaaa " + idNym + "  " + value.trim());

        Constant.NymPosition = idNym;
//        NynmActivity.this
//                .queryPosition("SELECT * FROM nym WHERE id = " + idNym);
        NynmActivity.this
                .queryPosition("SELECT * FROM nym WHERE name = '" + value.trim() + "'");
        Constant.printMsg("kadalaaaaaaaaa1111 " + mNymnMeaningCount);

//        if (mNymnMeaningCount > 1) {

        Constant.mNymTextValue = value.trim();

        Intent in = new Intent(NynmActivity.this,
                MultipleNynmActivity.class);
        startActivity(in);
//        } else {
//            NynmActivity.this.initiatePopupWindow(value, idNym);
//
//        }
    }

    @Override
    public void onLongClick(View view, int position) {

    }


    public SpannableStringBuilder displayNym(String value) {
        ArrayList<String> replacePosition = new ArrayList<String>();

        for (int i = 0; i < value.toString().length(); i++) {
            char tempCharecter = value.toString().charAt(i);
            if (tempCharecter == Constant.mNynmsSpecialCharacter) {
                replacePosition.add(String.valueOf(i));


            }

        }

        SpannableString redSpannable = new SpannableString(value);

        for (int i = 0; i < replacePosition.size(); i++) {
            redSpannable.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#00123654")),
                    Integer.valueOf(replacePosition.get(i).toString()),
                    Integer.valueOf(replacePosition.get(i).toString()) + 1,
                    33);

        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(redSpannable);


        return builder;
    }


    public void checkHideButtons() {

        n_lym_atach_img.setVisibility(View.VISIBLE);
        n_lym_edit_img.setVisibility(View.VISIBLE);
        n_lym_del_img.setVisibility(View.VISIBLE);

       /* if (Constant.mNynmFromSlider== true) {
//            Constant.mNynmFromSlider = false;
            n_lym_edit_img.setVisibility(View.VISIBLE);
            n_lym_atach_img.setVisibility(View.GONE);

        } else if (Constant.mNynmFromGroup == true) {
            Constant.mNynmFromGroup = false;
            Constant.isMUC_Paused = true;
            n_lym_atach_img.setVisibility(View.VISIBLE);
            n_lym_edit_img.setVisibility(View.GONE);
            n_lym_del_img.setVisibility(View.GONE);

        } else {
            n_lym_atach_img.setVisibility(View.VISIBLE);
            n_lym_edit_img.setVisibility(View.GONE);
            n_lym_del_img.setVisibility(View.GONE);
        }*/
    }


}
