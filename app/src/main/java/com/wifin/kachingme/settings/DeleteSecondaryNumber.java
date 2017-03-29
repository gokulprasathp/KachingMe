package com.wifin.kachingme.settings;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kaching.me.ui.R;

public class DeleteSecondaryNumber extends HeaderActivity implements View.OnClickListener
{
    TextView primary_number, sec_number1, sec_number2, sec_number3, sec_number4;
    Button submit_btn1, submit_btn2, submit_btn3, submit_btn4;
    SharedPreferences sp1,preference;
    int width = 0, height = 0;
    Dbhelper db;
    int mNumberCount = 0;
    List<String> sec_number_list = new ArrayList<>();
    String sec_number;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.sec_number_delete, vg);
        preference = getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        mHeaderImg.setVisibility(View.INVISIBLE);
        mNextBtn.setVisibility(View.INVISIBLE);
        mHeaderLayout.setVisibility(View.VISIBLE);
        mFooterLayout.setVisibility(View.GONE);
        mFooterView.setVisibility(View.GONE);
        mHeading.setText("Delete Number");

        sp1 = PreferenceManager.getDefaultSharedPreferences(this);
        db = new Dbhelper(getApplicationContext());
        String no = sp1.getString("number", "");
        String country_code = sp1.getString("countrycode", "");

        Constant.printMsg("no ::::::>>>>>>>>> " + no);

        initialization();
        screenArrange();
        primary_number.setVisibility(View.GONE);

        String query = "select secondarynum from " + Dbhelper.TABLE_NUMBERS;
        calsecondary(query);
        Constant.printMsg("list count   " + sec_number_list.size());

        if (sec_number_list.size() == 0)
        {
            alertSecNum();
            sec_number1.setVisibility(View.GONE);
            submit_btn1.setVisibility(View.GONE);
            sec_number2.setVisibility(View.GONE);
            sec_number3.setVisibility(View.GONE);
            sec_number4.setVisibility(View.GONE);
            submit_btn2.setVisibility(View.GONE);
            submit_btn3.setVisibility(View.GONE);
            submit_btn4.setVisibility(View.GONE);
        }
        if (sec_number_list.size() == 1)
        {
            sec_number1.setVisibility(View.VISIBLE);
            sec_number2.setVisibility(View.GONE);
            sec_number3.setVisibility(View.GONE);
            sec_number4.setVisibility(View.GONE);
            submit_btn1.setVisibility(View.VISIBLE);
            submit_btn2.setVisibility(View.GONE);
            submit_btn3.setVisibility(View.GONE);
            submit_btn4.setVisibility(View.GONE);
            sec_number1.setText(sec_number_list.get(0));
        }
        if (sec_number_list.size() == 2)
        {
            sec_number1.setVisibility(View.VISIBLE);
            sec_number2.setVisibility(View.VISIBLE);
            sec_number3.setVisibility(View.GONE);
            sec_number4.setVisibility(View.GONE);

            submit_btn1.setVisibility(View.VISIBLE);
            submit_btn2.setVisibility(View.VISIBLE);
            submit_btn3.setVisibility(View.GONE);
            submit_btn4.setVisibility(View.GONE);

            sec_number1.setText(sec_number_list.get(0));
            sec_number2.setText(sec_number_list.get(1));
        }
        if (sec_number_list.size() == 3)
        {
            sec_number1.setVisibility(View.VISIBLE);
            sec_number2.setVisibility(View.VISIBLE);
            sec_number3.setVisibility(View.VISIBLE);
            sec_number4.setVisibility(View.GONE);

            submit_btn1.setVisibility(View.VISIBLE);
            submit_btn2.setVisibility(View.VISIBLE);
            submit_btn3.setVisibility(View.VISIBLE);
            submit_btn4.setVisibility(View.GONE);

            sec_number1.setText(sec_number_list.get(0));
            sec_number2.setText(sec_number_list.get(1));
            sec_number3.setText(sec_number_list.get(2));
        }
        if (sec_number_list.size() == 4)
        {
            sec_number1.setVisibility(View.VISIBLE);
            submit_btn1.setVisibility(View.VISIBLE);
            sec_number2.setVisibility(View.VISIBLE);
            sec_number2.setVisibility(View.VISIBLE);
            sec_number2.setVisibility(View.VISIBLE);
            submit_btn2.setVisibility(View.VISIBLE);
            submit_btn3.setVisibility(View.VISIBLE);
            submit_btn4.setVisibility(View.VISIBLE);

            sec_number1.setText(sec_number_list.get(0));
            sec_number2.setText(sec_number_list.get(1));
            sec_number3.setText(sec_number_list.get(2));
            sec_number4.setText(sec_number_list.get(3));
        }

        primary_number.setText(country_code + no);

        mBackBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(DeleteSecondaryNumber.this, UsageActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
    }

    private void initialization()
    {
        // TODO Auto-generated method stub
        primary_number = (TextView) findViewById(R.id.primary_number);

        sec_number1 = (TextView) findViewById(R.id.sec_number1);
        submit_btn1 = (Button) findViewById(R.id.sub_btn1);

        sec_number2 = (TextView) findViewById(R.id.sec_number2);
        submit_btn2 = (Button) findViewById(R.id.sub_btn2);

        sec_number3 = (TextView) findViewById(R.id.sec_number3);
        submit_btn3 = (Button) findViewById(R.id.sub_btn3);

        sec_number4 = (TextView) findViewById(R.id.sec_number4);
        submit_btn4 = (Button) findViewById(R.id.sub_btn4);

        Constant.typeFace(this,primary_number);
        Constant.typeFace(this,sec_number1);
        Constant.typeFace(this,submit_btn1);
        Constant.typeFace(this,sec_number2);
        Constant.typeFace(this,submit_btn2);
        Constant.typeFace(this,sec_number3);
        Constant.typeFace(this,submit_btn3);
        Constant.typeFace(this,sec_number4);
        Constant.typeFace(this,submit_btn4);

        submit_btn1.setOnClickListener(this);
        submit_btn2.setOnClickListener(this);
        submit_btn3.setOnClickListener(this);
        submit_btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.sub_btn1:

                if (sec_number1.getText().toString().length() > 0)
                {
                    sec_number = sec_number1.getText().toString();
                    delete_alert();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Enter secondary number", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.sub_btn2:

                if (sec_number2.getText().toString().length() > 0)
                {
                    sec_number = sec_number2.getText().toString();
                    delete_alert();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Enter secondary number", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.sub_btn3:

                if (sec_number3.getText().toString().length() > 0)
                {
                    sec_number = sec_number3.getText().toString();
                    delete_alert();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Enter secondary number", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.sub_btn4:

                if (sec_number4.getText().toString().length() > 0)
                {
                    sec_number = sec_number4.getText().toString();
                    delete_alert();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Enter secondary number", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    protected void alertSecNum()
    {
        AlertDialog.Builder alertSec = new AlertDialog.Builder(this);
        alertSec.setTitle("Delete Number");
        alertSec.setMessage("Didn't have Secondary Number to Delete");
        alertSec.setNegativeButton("Dismiss", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                Intent in = new Intent(DeleteSecondaryNumber.this, UsageActivity.class);
                startActivity(in);
                finish();
            }
        });
        alertSec.setCancelable(false);
        alertSec.show();
    }

    protected void delete_alert()
    {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setCancelable(false);
        b.setMessage("Are you sure you want to delete this number ?").setCancelable(false);
        b.setNegativeButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                new deleteSecondaryNumber().execute();
            }
        });

        b.setPositiveButton("CANCEL", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.dismiss();
            }
        });

        b.setCancelable(true);

        AlertDialog alert = b.create();
        alert.show();
    }

    private void deleteSecondary(String secNumber)
    {
        // TODO Auto-generated method stub
        String wheres = " secondarynum=?";
        String[] whereArgs = {secNumber.toString()};
        Constant.printMsg("No of deleted rows from bux data is called::::::::::::");

        try
        {
            int a = db.open().getDatabaseObj().delete(Dbhelper.TABLE_NUMBERS, wheres, whereArgs);
            Constant.printMsg("No of deleted rows from bux data is ::::::::::::" + a);
        }
        catch (SQLException e)
        {
            Constant.printMsg("Sql exception while deleting particular record for shop:::::" + e.toString());
        }
        finally
        {
            db.close();
        }
    }

    private void calsecondary(String query)
    {
        // TODO Auto-generated method stub
        Cursor c = null;
        try {
            c = db.open().getDatabaseObj().rawQuery(query, null);

            Constant.printMsg("The selected elist activity count is ::::::"
                    + c.getCount());
            mNumberCount = c.getCount();
            // int check_index = c.getColumnIndex("check");
            if (c.getCount() > 0) {
                Constant.printMsg("Caling sysout:::::::::::::::::::::::::");
                while (c.moveToNext()) {
                    if (c.getString(0) != null) {
                        sec_number_list.add(c.getString(0));
                    }
                    // mKonsList.add(String.valueOf(c.getString(1)));
                }
            }
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        finally
        {
            db.close();
        }
    }

    public class deleteSecondaryNumber extends AsyncTask<String, String, String>
    {
        ProgressDialog progressDialog;
        String primaryNum ;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DeleteSecondaryNumber.this, AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setProgressDrawable(new ColorDrawable(Color.BLUE));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();
            primaryNum=preference.getString("MyPrimaryNumber", "");
            result = ht.httpget(KachingMeConfig.Delete_Secondary_Number + "primaryNumber=" + primaryNum + "&secondaryNumber=" + sec_number);
            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
            Constant.printMsg("result ::::::: >>>>>> " + result);
            Toast.makeText(getApplicationContext(), "result " + result + KachingMeApplication.getjid().split("@")[0], Toast.LENGTH_LONG).show();

            if (result != null && result.length() > 0)
            {
                if (result.equalsIgnoreCase("Deleted"))
                {
                    startActivity(new Intent(DeleteSecondaryNumber.this, UsageActivity.class));
                    deleteSecondary(sec_number);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Number not exist", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void screenArrange()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mainLayoutParams.width = width;
        mainLayoutParams.height = height * 10 / 100;
        mainLayoutParams.gravity = Gravity.CENTER;
        mHeaderLayout.setLayoutParams(mainLayoutParams);

        LinearLayout.LayoutParams layoutMenuq5 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutMenuq5.height = width * 15 / 100;
        layoutMenuq5.width = width * 55 / 100;
        layoutMenuq5.gravity = Gravity.CENTER;
        layoutMenuq5.leftMargin = width * 5 / 100;
        layoutMenuq5.topMargin = width * 10 / 100;
        sec_number1.setLayoutParams(layoutMenuq5);

        LinearLayout.LayoutParams layoutMenuq51 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutMenuq51.height = width * 15 / 100;
        layoutMenuq51.width = width * 30 / 100;
        layoutMenuq51.gravity = Gravity.CENTER;
        layoutMenuq51.leftMargin = width * 5 / 100;
        layoutMenuq51.topMargin = width * 5 / 100;
        submit_btn1.setLayoutParams(layoutMenuq51);

        LinearLayout.LayoutParams layoutMenuq = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutMenuq.height = width * 15 / 100;
        layoutMenuq.width = width * 55 / 100;
        layoutMenuq.gravity = Gravity.CENTER;
        layoutMenuq.leftMargin = width * 5 / 100;
        layoutMenuq.topMargin = width * 5 / 100;
        sec_number2.setLayoutParams(layoutMenuq);
        sec_number3.setLayoutParams(layoutMenuq);
        sec_number4.setLayoutParams(layoutMenuq);

        LinearLayout.LayoutParams layoutMenuq2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutMenuq2.height = width * 15 / 100;
        layoutMenuq2.width = width * 60 / 100;
        layoutMenuq2.gravity = Gravity.CENTER;
        layoutMenuq2.topMargin = width * 10 / 100;
        layoutMenuq2.leftMargin = width * 5 / 100;
        primary_number.setLayoutParams(layoutMenuq2);

        LinearLayout.LayoutParams layoutMenuq1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutMenuq1.height = width * 15 / 100;
        layoutMenuq1.width = width * 30 / 100;
        layoutMenuq1.topMargin = width * 5 / 100;
        layoutMenuq1.leftMargin = width * 5 / 100;
        submit_btn2.setLayoutParams(layoutMenuq1);
        submit_btn3.setLayoutParams(layoutMenuq1);
        submit_btn4.setLayoutParams(layoutMenuq1);

        android.support.v4.widget.DrawerLayout.LayoutParams linearContentLayout = new android.support.v4.widget.DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearContentLayout.width = width;
        linearContentLayout.height = height;
        mContentLayout.setLayoutParams(linearContentLayout);

        LinearLayout.LayoutParams ft = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        ft.height = height * 8 / 100;
        ft.gravity = Gravity.CENTER | Gravity.BOTTOM;
        mFooterLayout.setLayoutParams(ft);

        LinearLayout.LayoutParams logolay1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        logolay1.width = (int) (width * 30.5 / 100);
        logolay1.height = height * 11 / 100;
        logolay1.setMargins(width * 1 / 100, (int) (height * 1.5 / 100), width * 1 / 100, 0);
        logolay1.gravity = Gravity.CENTER;
        logolay1.topMargin = height * 2 / 100;
        mChatLayout.setLayoutParams(logolay1);
        mBuxLayout.setLayoutParams(logolay1);
        mCartLayout.setLayoutParams(logolay1);

        LinearLayout.LayoutParams img = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        img.height = height * 4 / 100;
        img.gravity = Gravity.CENTER;
        mChatImg.setLayoutParams(img);
        mBuxSImg.setLayoutParams(img);
        mCartImg.setLayoutParams(img);

        LinearLayout.LayoutParams imgtext = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        imgtext.height = height * 4 / 100;
        imgtext.gravity = Gravity.CENTER;
        mChatText.setLayoutParams(imgtext);
        mBuxText.setLayoutParams(imgtext);
        mCartText.setLayoutParams(imgtext);

        if (width >= 600)
        {
            primary_number.setTextSize(16);
            sec_number1.setTextSize(16);
            submit_btn1.setTextSize(16);
            sec_number2.setTextSize(16);
            submit_btn2.setTextSize(16);
            sec_number3.setTextSize(16);
            submit_btn3.setTextSize(16);
            sec_number4.setTextSize(16);
            submit_btn4.setTextSize(16);
        }
        else if (width > 501 && width < 600)
        {
            primary_number.setTextSize(15);
            sec_number1.setTextSize(15);
            submit_btn1.setTextSize(15);
            sec_number2.setTextSize(15);
            submit_btn2.setTextSize(15);
            sec_number3.setTextSize(15);
            submit_btn3.setTextSize(15);
            sec_number4.setTextSize(15);
            submit_btn4.setTextSize(15);
        }
        else if (width > 260 && width < 500)
        {
            primary_number.setTextSize(14);
            sec_number1.setTextSize(14);
            submit_btn1.setTextSize(14);
            sec_number2.setTextSize(14);
            submit_btn2.setTextSize(14);
            sec_number3.setTextSize(14);
            submit_btn3.setTextSize(14);
            sec_number4.setTextSize(14);
            submit_btn4.setTextSize(14);
        }
        else if (width <= 260)
        {
            primary_number.setTextSize(13);
            sec_number1.setTextSize(13);
            submit_btn1.setTextSize(13);
            sec_number2.setTextSize(13);
            submit_btn2.setTextSize(13);
            sec_number3.setTextSize(13);
            submit_btn3.setTextSize(13);
            sec_number4.setTextSize(13);
            submit_btn4.setTextSize(13);
        }
    }
}