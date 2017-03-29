package com.wifin.kachingme.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat_home.MainActivity;
import com.wifin.kachingme.util.Constant;

import java.io.File;
import java.util.Stack;

public class Settings_account extends MainActivity {
    public static final String local_image_dir = Environment
            .getExternalStorageDirectory() + "/Kaching.me/";
    TextView account_privacy, account_deleteno, account_deleteaccount,
            account_networkusage, account_storageusage;

    /**
     * Return the size of a directory in bytes
     */

    private static long dirSize(File dir) {
        try {
            long result = 0;

            Stack<File> dirlist = new Stack<File>();
            dirlist.clear();

            dirlist.push(dir);

            while (!dirlist.isEmpty()) {
                File dirCurrent = dirlist.pop();

                File[] fileList = dirCurrent.listFiles();
                for (File f : fileList) {
                    if (f.isDirectory())
                        dirlist.push(f);
                    else
                        result += f.length();
                }
            }

            return result;
        } catch (Exception e) {
            // Constant.printMsg("storage meth:" + e.toString());

            return 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
        View.inflate(this, R.layout.settings_account, vg);
        sideMenufoot.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);
        footer.setVisibility(View.GONE);
        head.setText("Account");
        back.setVisibility(View.VISIBLE);
        head.setTextColor(Color.parseColor("#FFFFFF"));
        back.setBackgroundResource(R.drawable.arrow);
        headlay.setBackgroundColor(Color.parseColor("#FE0000"));
        Ka_newlogo.setVisibility(View.INVISIBLE);
        init();
        screenArrange();

        // showing the storage data
        try {
            // Constant.printMsg("storage data :"+Network_Usage.convertBytesToSuitableUnit(String.valueOf(dirSize(new
            // File(local_image_dir)))));
            // Constant.printMsg("storage data :"+String.valueOf(dirSize(new
            // File(local_image_dir))));
            // Constant.printMsg("storage data :"+ local_image_dir);
            account_storageusage.setText("Storage Usage - "
                    + Network_Usage.convertBytesToSuitableUnit(String
                    .valueOf(dirSize(new File(local_image_dir)))));
        } catch (Exception e) {
            // Constant.printMsg("storage :" + e.toString());
        }

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(Settings_account.this,
                        Settings.class);
                startActivity(intent);
                finish();
            }
        });

        account_networkusage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.printMsg(" settings::account_networkusage");
                Intent intent = new Intent(Settings_account.this,
                        Network_Usage.class);
                startActivity(intent);
                finish();

            }
        });

        account_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.printMsg(" settings::account_privacy");
                Intent intent = new Intent(Settings_account.this,
                        SettingsPrivacyInfo.class);
                startActivity(intent);
                finish();

            }
        });

        account_deleteno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.printMsg(" settings::account_deleteno");
                Intent i = new Intent(Settings_account.this,
                        DeleteSecondaryNumber.class);
                startActivity(i);

            }
        });
        account_deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.printMsg(" settings::account_deleteaccount");
                Intent i = new Intent(Settings_account.this,
                        Delete_Account_Confirm.class);
                startActivity(i);
            }
        });
        /*
		 * account_storageusage.setOnClickListener(new View.OnClickListener() {
		 *
		 * @Override public void onClick(View v) {
		 * Constant.printMsg(" settings::account_storageusage");
		 *
		 * } });
		 */

    }

    private void init() {
        // TODO Auto-generated method stub
        account_privacy = (TextView) findViewById(R.id.account_privacy);
        account_deleteno = (TextView) findViewById(R.id.account_deleteno);
        account_deleteaccount = (TextView) findViewById(R.id.account_deleteaccount);
        account_networkusage = (TextView) findViewById(R.id.account_networkusage);
        account_storageusage = (TextView) findViewById(R.id.account_storageusage);

        Constant.typeFace(this, account_privacy);
        Constant.typeFace(this, account_deleteno);
        Constant.typeFace(this, account_deleteaccount);
        Constant.typeFace(this, account_networkusage);
        Constant.typeFace(this, account_storageusage);
    }

    private void screenArrange() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        LinearLayout.LayoutParams textlay = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        textlay.width = width * 80 / 100;
        textlay.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        // textlay.gravity = Gravity.CENTER | Gravity.LEFT;
        textlay.topMargin = height * 3 / 100;
        textlay.bottomMargin = height * 3 / 100;
        textlay.gravity = Gravity.CENTER;
        textlay.leftMargin = width * 5 / 100;
        textlay.rightMargin = width * 10 / 100;
        account_privacy.setLayoutParams(textlay);
        account_privacy.setGravity(Gravity.CENTER | Gravity.LEFT);
        account_deleteno.setLayoutParams(textlay);
        account_deleteno.setGravity(Gravity.CENTER | Gravity.LEFT);
        account_deleteaccount.setLayoutParams(textlay);
        account_deleteaccount.setGravity(Gravity.CENTER | Gravity.LEFT);
        account_networkusage.setLayoutParams(textlay);
        account_networkusage.setGravity(Gravity.CENTER | Gravity.LEFT);
        account_storageusage.setLayoutParams(textlay);
        account_storageusage.setGravity(Gravity.CENTER | Gravity.LEFT);
        LinearLayout.LayoutParams layoutMenuq = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutMenuq.width = width * 50 / 100;
        layoutMenuq.height = height * 4 / 100;

        if (width >= 600) {

            account_privacy.setTextSize(20);
            account_deleteaccount.setTextSize(20);
            account_deleteno.setTextSize(20);
            account_networkusage.setTextSize(20);
            account_storageusage.setTextSize(20);

        } else if (width > 501 && width < 600) {

            account_privacy.setTextSize(19);
            account_deleteaccount.setTextSize(19);
            account_networkusage.setTextSize(19);
            account_deleteno.setTextSize(19);
            account_storageusage.setTextSize(19);

        } else if (width > 260 && width < 500) {
            account_privacy.setTextSize(18);
            account_deleteaccount.setTextSize(18);
            account_networkusage.setTextSize(18);
            account_storageusage.setTextSize(18);
            account_deleteno.setTextSize(18);
        } else if (width <= 260) {
            account_privacy.setTextSize(17);
            account_deleteaccount.setTextSize(17);
            account_networkusage.setTextSize(17);
            account_storageusage.setTextSize(17);
            account_deleteno.setTextSize(17);

        }

    }

}
