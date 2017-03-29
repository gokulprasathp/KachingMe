package com.wifin.kachingme.kaching_feature.nynms;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.NymsPojo;
import com.wifin.kachingme.util.Constant;

import a_vcard.android.provider.Contacts.PeopleColumns;

public class NynmAddActivity extends HeaderActivity implements View.OnClickListener {
    public final static String header = "";
    public static NynmAddActivity mActivity;
    //    AppBarLayout nynmAddAppBarLayout;
//    Toolbar nynmAddAppToolBar;
//    FrameLayout frameAddAppMenu;
//    ImageView imgAddNynmBack;
    LinearLayout linearNynmTitle, linearNynmMessage, linearNynmButton, linearNynmAddScreen;
    EditText etNynmTitle, etNynmMessage;
    TextView mNymnLabel, mActualMsgLabel;
    TextView btNynmCancel, btNynmAdd;
    int height, width;
    SharedPreferences sp;
    Dbhelper db;
    String head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_nynm_add);
        getSupportActionBar().hide();
        mActivity = this;
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.activity_nynm_add, vg);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        db = new Dbhelper(getApplicationContext());
        mHeading.setText("NynM");
        mNextBtn.setVisibility(View.INVISIBLE);
        mHeaderImg.setVisibility(View.VISIBLE);
        mHeaderImg.setImageResource(R.drawable.nymn);

        mFooterLayout.setVisibility(View.VISIBLE);
        init();
        nynmScreen();

        Intent intent = getIntent();
        head = intent.getStringExtra(header);
        try {
            String nynmValue = intent.getStringExtra("nynmValue");
            if (nynmValue != null) {
                etNynmTitle.setText(nynmValue);
                etNynmTitle.setEnabled(false);
                etNynmTitle.setFocusable(false);
                etNynmTitle.setFocusableInTouchMode(false);
            }

        } catch (Exception e) {

        }
        Constant.printMsg("testtstststststs  " + head);
        if (head.equalsIgnoreCase("edit")) {
            btNynmAdd.setText("Update");
            etNynmTitle.setText(Constant.message);
            etNynmMessage.setText(new NynmActivity().displayNym(Constant.abbreviation));
            etNynmTitle.setEnabled(false);
            etNynmTitle.setFocusableInTouchMode(false);
            etNynmTitle.setFocusable(false);
        } else {
            if (Constant.message != null) {
                etNynmTitle.setHint(getApplicationContext().getResources().getString(R.string.nynm_title));
            }
        }

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mNextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Intent i = new Intent(NynmAddActivity.this, NynmActivity.class);
//                startActivity(i);
                finish();
            }
        });
        btNynmCancel.setOnClickListener(this);
        btNynmAdd.setOnClickListener(this);

        etNynmMessage.setHint(getApplicationContext().getResources().getString(R.string.nynm_message));
        etNynmTitle.setHint(getApplicationContext().getResources().getString(R.string.nynm_title));
    }

    public void init() {
        linearNynmTitle = (LinearLayout) findViewById(R.id.linearNynmTitle);
        linearNynmMessage = (LinearLayout) findViewById(R.id.linearNynmMessage);
        linearNynmButton = (LinearLayout) findViewById(R.id.linearNynmButton);
        linearNynmAddScreen = (LinearLayout) findViewById(R.id.linearNynmAddScreen);
        etNynmTitle = (EditText) findViewById(R.id.etNynmTitle);
        etNynmMessage = (EditText) findViewById(R.id.etNynmMessage);
        btNynmCancel = (TextView) findViewById(R.id.btNynmCancel);
        btNynmAdd = (TextView) findViewById(R.id.btNynmAdd);
        mNymnLabel = (TextView) findViewById(R.id.tvNynmTitle);
        mActualMsgLabel = (TextView) findViewById(R.id.tvNynmMessage);

        Constant.typeFace(this, etNynmTitle);
        Constant.typeFace(this, etNynmMessage);
        Constant.typeFace(this, btNynmCancel);
        Constant.typeFace(this, btNynmAdd);
        Constant.typeFace(this, mNymnLabel);
        Constant.typeFace(this, mActualMsgLabel);
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

        LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        titleLayoutParams.width = (int) width * 96 / 100;
        titleLayoutParams.topMargin = height * 5 / 100;
        linearNynmTitle.setLayoutParams(titleLayoutParams);

        LinearLayout.LayoutParams msgLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        msgLayoutParams.width = (int) width * 96 / 100;
        msgLayoutParams.topMargin = height * 2 / 100;
        linearNynmMessage.setLayoutParams(msgLayoutParams);

        LinearLayout.LayoutParams labeltextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        labeltextParams.width = (int) width * 38 / 100;
        labeltextParams.leftMargin = height * 3 / 100;
        mNymnLabel.setLayoutParams(labeltextParams);
        mActualMsgLabel.setLayoutParams(labeltextParams);


        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.width = (int) width * 47 / 100;
        editTextParams.leftMargin = height * 3 / 100;
        etNynmTitle.setLayoutParams(editTextParams);
        etNynmMessage.setLayoutParams(editTextParams);
        etNynmTitle.setPadding(10, 15, 10, 15);
        etNynmMessage.setPadding(10, 15, 10, 15);


        LinearLayout.LayoutParams btnLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btnLayoutParams.width = (int) width;
        btnLayoutParams.height = (int) height * 10 / 100;
        btnLayoutParams.topMargin = height * 5 / 100;
        linearNynmButton.setLayoutParams(btnLayoutParams);

        LinearLayout.LayoutParams cancelbtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cancelbtnParams.width = (int) width * 35 / 100;
        cancelbtnParams.height = (int) height * 7 / 100;
//		cancelbtnParams.leftMargin =(int)( width * 5/ 100);
        btNynmCancel.setLayoutParams(cancelbtnParams);
        btNynmCancel.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams addbtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        addbtnParams.width = (int) width * 35 / 100;
        addbtnParams.height = (int) height * 7 / 100;
        addbtnParams.leftMargin = width * 5 / 100;

        btNynmAdd.setLayoutParams(addbtnParams);
        btNynmAdd.setGravity(Gravity.CENTER);


        if (width >= 600) {
            mNymnLabel.setTextSize(18);
            mActualMsgLabel.setTextSize(18);
            btNynmAdd.setTextSize(18);
            btNynmCancel.setTextSize(18);
            etNynmTitle.setTextSize(18);
            etNynmMessage.setTextSize(18);
        } else if (width < 600 && width >= 480) {
            mNymnLabel.setTextSize(17);
            mActualMsgLabel.setTextSize(17);
            btNynmAdd.setTextSize(17);
            btNynmCancel.setTextSize(17);
            etNynmTitle.setTextSize(17);
            etNynmMessage.setTextSize(17);
        } else if (width < 480 && width >= 320) {
            mNymnLabel.setTextSize(13);
            mActualMsgLabel.setTextSize(13);
            btNynmAdd.setTextSize(13);
            btNynmCancel.setTextSize(13);
            etNynmTitle.setTextSize(13);
            etNynmMessage.setTextSize(13);
        } else if (width < 320) {
            mNymnLabel.setTextSize(11);
            mActualMsgLabel.setTextSize(11);
            btNynmAdd.setTextSize(11);
            btNynmCancel.setTextSize(11);
            etNynmTitle.setTextSize(11);
            etNynmMessage.setTextSize(11);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.btNynmCancel:


//                Intent i = new Intent(NynmAddActivity.this, NynmActivity.class);
//                startActivity(i);
                finish();
                break;

            case R.id.btNynmAdd:

                if (head.equalsIgnoreCase("add")) {


                    if (NynmAddActivity.this.etNynmTitle.getText().toString()
                            .length() <= 0
                            || NynmAddActivity.this.etNynmMessage.getText().toString()
                            .length() <= 0) {
                        Toast.makeText(NynmAddActivity.this.getApplicationContext(),
                                "Please fill the NynM", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (NynmAddActivity.this.etNynmTitle.getText().toString()
                            .length() > 4) {
                        Toast.makeText(NynmAddActivity.this.getApplicationContext(),
                                "Nynm message should not exceed 4 characters", Toast.LENGTH_SHORT).show();
                        NynmAddActivity.this.etNynmTitle.setText("");
                        return;
                    }
                    String txt = NynmAddActivity.this.etNynmTitle.getText()
                            .toString().toLowerCase().trim();
                    String abbS = NynmAddActivity.this.etNynmMessage.getText().toString()
                            .toLowerCase().trim();
                    if (NynmAddActivity.this.CheckNynmTitleCount(txt.trim())) {

                        if (CheckNynmMessageCount(txt.trim(), abbS.trim())) {
                            String abbs;
                            String txt1;
                            String[] words = abbS.split("\\s+");
                            String[] words1 = txt.split("\\s+");
                            Constant.printMsg("lengj::" + words.length);
                            if (words.length > 1) {
                                abbs = String.format(abbS, new Object[0]).replace(' ',
                                        Constant.mNynmsSpecialCharacter);
                            } else {
                                abbs = abbS;
                            }
                            if (words1.length > 1) {
                                txt1 = String.format(txt, new Object[0]).replace(' ',
                                        Constant.mNynmsSpecialCharacter);
                            } else {
                                txt1 = txt;
                            }
                            Constant.printMsg("abbs::" + abbS + abbs);
                            NymsPojo np = new NymsPojo();
                            np.setText(txt1);
                            np.setMeaning(abbs);
                            Constant.addedNyms.add(0, np);

                            Constant.printMsg("Dilip Nynm vales ::" + txt1 + abbs);
                            ContentValues cv = new ContentValues();
                            cv.put(PeopleColumns.NAME, txt1);
                            cv.put("meaning", abbs);
                            NynmAddActivity.this.insertDB(cv);
                            Constant.NewNyms.add(0, np);
                            Constant.bux = Long.valueOf(NynmAddActivity.this.sp.getLong(
                                    "buxvalue", 0));
                            Long buxval = Long.valueOf(Constant.bux.longValue()
                                    + ((long) Constant.nympoints));
                            Constant.bux = buxval;
                            Editor e = NynmAddActivity.this.sp.edit();
                            e.putLong("buxvalue", buxval.longValue());
                            e.commit();
//                            NynmAddActivity.this.startActivity(new Intent(
//                                    NynmAddActivity.this, NynmActivity.class));
                            NynmAddActivity.this.finish();
                            return;
                        } else
                            Toast.makeText(NynmAddActivity.this.getApplicationContext(),
                                    "NynM  Message is already existed.", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(NynmAddActivity.this.getApplicationContext(),
                                "NynM is Already Existed 3 Times", Toast.LENGTH_SHORT).show();
                } else {
                    if (etNynmTitle.getText().toString().length() > 0
                            && etNynmMessage.getText().toString().length() > 0) {

                        String abbs, txt1;

                        String txt = etNynmTitle.getText().toString();
                        String abbS = etNynmMessage.getText().toString();
                        Constant.NymPosition = 0;
//						DbDeleteNym(Constant.NymPosition);

                        // if (Checkmethod(txt.trim())) {

                        String[] words = abbS.split("\\s+");
                        String[] words1 = txt.split("\\s+");

                        Constant.printMsg("lengj::" + words.length);

                        if (words.length > 1) {

                            abbs = String.format(abbS).replace(' ', '.');

                        } else {

                            abbs = abbS;
                        }

                        if (words1.length > 1) {

                            txt1 = String.format(txt).replace(' ', '.');

                        } else {

                            txt1 = txt;
                        }

                        NymsPojo np = new NymsPojo();
                        np.setText(txt1);
                        np.setMeaning(abbs);
                        Constant.addedNyms.add(Constant.NymPosition, np);

                        // ContentValues cv = new ContentValues();
                        //
                        // cv.put("name", txt1);
                        // cv.put("meaning", abbs);
                        //
                        // insertDB(cv);
                        // String qry = "UPDATE " + Dbhelper.TABLE_NYM +
                        // " SET name='"
                        // + txt1 + "' WHERE meaning='" + abbs + "'";

                        String qry = "UPDATE " + Dbhelper.TABLE_NYM + " SET name="
                                + txt1 + ", meaning=" + abbs + " WHERE name = '"
                                + Constant.message + "' AND meaning='" + Constant.abbreviation
                                + "'";

                        Constant.printMsg("edit qryyy:" + qry);

                        Constant.printMsg("query::" + qry);
                        // updateMsg(qry);

                        updateMsg(txt1, abbs);

//                        Intent i1 = new Intent(NynmAddActivity.this, NynmActivity.class);
//                        startActivity(i1);
                        finish();

                        // } else {
                        //
                        // Toast.makeText(getApplicationContext(),
                        // "Text Given is Already Existing",
                        // Toast.LENGTH_SHORT).show();
                        //
                        // }
                    } else {

                        Toast.makeText(getApplicationContext(),
                                "Please fill the NynM", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public void DbDeleteNym(int pos) {
        // TODO Auto-generated method stub

//		String dl = Constant.addedNyms.get(Constant.NymPosition).getText();
        Cursor c = null;
        String q = "DELETE FROM " + Dbhelper.TABLE_NYM + " WHERE name='" +
                Constant.message + "' AND meaning='" + Constant.abbreviation
                + "'";

        try {
            c = db.open().getDatabaseObj().rawQuery(q, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
        } catch (SQLException e) {

        } finally {
            c.close();
            db.close();
        }

        Constant.addedNyms.remove(Constant.NymPosition);

    }

    public void updateMsg(String txt, String mng) {

        try {
            String qry = "UPDATE " + Dbhelper.TABLE_NYM + " SET name='" + txt
                    + "' , meaning='" + mng + "' WHERE name = '"
                    + Constant.message + "' AND meaning='" + Constant.abbreviation
                    + "'";

            Constant.printMsg("The update qry for new shop is :::::" + txt
                    + "  " + mng + "   " + qry);

            Cursor c = db.open().getDatabaseObj().rawQuery(qry, null);
            c.moveToFirst();

            Constant.printMsg("updatedmssssg table ::::" + c.getCount());

        } catch (SQLException e) {

        } finally {
            db.close();

        }
    }

    public void insertDB(ContentValues v) {

        try {
            Constant.printMsg("No of inserted rows in shop details :::::::::"
                    + ((int) this.db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_NYM, null, v)));
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in new shop details ::::::"
                    + e.toString());
        } finally {
            this.db.close();
        }

    }

    public boolean CheckNynmTitleCount(String txt) {


        Dbhelper db = new Dbhelper(getApplicationContext());
        Cursor c = null;
        boolean isCheck = false;
        try {
            c = db.open()
                    .getDatabaseObj()
                    .query(Dbhelper.TABLE_NYM, new String[]{"id"
                            }, "name like ?", new String[]{txt}, null, null,
                            null);
            if (c != null) {

                Constant.printMsg("Nynm Check count : " + c.getCount());
                if (c.getCount() < 3) {
                    isCheck = true;
                }

            }

        } catch (Exception e) {

        }

        return isCheck;
      /*  sd
        for (int i = 0; i < Constant.addedNyms.size(); i++) {
            if (((NymsPojo) Constant.addedNyms.get(i)).getText().toString()
                    .trim().equals(txt)) {
            }
        }
        return true;*/
    }


    public boolean CheckNynmMessageCount(String title, String txt) {


        Dbhelper db = new Dbhelper(getApplicationContext());
        Cursor c = null;
        boolean isCheck = true;
        try {
            c = db.open()
                    .getDatabaseObj()
                    .query(Dbhelper.TABLE_NYM, new String[]{"id"
                            }, "meaning like ? and name like ?", new String[]{txt, title}, null, null,
                            null);
            if (c != null) {

                Constant.printMsg("Nynm Check Msg count : " + c.getCount());
                if (c.getCount() > 0) {
                    isCheck = false;
                }

            }

        } catch (Exception e) {

        }

        return isCheck;

    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub


        finish();
    }
}
