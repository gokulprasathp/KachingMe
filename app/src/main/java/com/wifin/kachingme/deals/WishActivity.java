/*
* @author Gokul
*
*
*
*
* */

package com.wifin.kachingme.deals;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat_home.MainActivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.wish_list.AddWishList;

public class WishActivity extends MainActivity {

    TextView select;
    LinearLayout addlay;
    EditText wishname;
    Button add;
    int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_pons);
        ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
        View.inflate(this, R.layout.activity_pons, vg);
        logo.setVisibility(ImageView.GONE);
        back.setVisibility(ImageView.VISIBLE);
        cart.setVisibility(ImageView.INVISIBLE);
        cartno.setVisibility(ImageView.INVISIBLE);

        head.setText("Wish List");

        Init();
        SArrange();

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (wishname.getText().toString().length() > 0) {

                    Constant.wishlistname = wishname.getText().toString()
                            .trim();

                    Intent i = new Intent(WishActivity.this, AddWishList.class);
                    startActivity(i);
                    finish();

                } else {

                    Toast.makeText(getApplicationContext(),
                            "Please enter the wish list name",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent i = new Intent(WishActivity.this, WishListActivity.class);
                startActivity(i);
                finish();

            }
        });

    }

    private void SArrange() {
        // TODO Auto-generated method stub
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        LinearLayout.LayoutParams layoutMenuq = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutMenuq.height = height * 17 / 100;
        layoutMenuq.gravity = Gravity.CENTER;
        layoutMenuq.setMargins(width * 1 / 100, height * 3 / 100,
                width * 1 / 100, height * 1 / 100);
        select.setLayoutParams(layoutMenuq);
        select.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layouttot = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layouttot.height = height * 20 / 100;
        layouttot.gravity = Gravity.CENTER;
        layouttot.setMargins(width * 1 / 100, height * 3 / 100,
                width * 1 / 100, height * 1 / 100);
        addlay.setLayoutParams(layouttot);
        addlay.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutedit = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutedit.width = width * 75 / 100;
        layoutedit.height = height * 10 / 100;
        layoutedit.gravity = Gravity.CENTER;
        layoutedit.setMargins(width * 1 / 100, height * 1 / 100,
                width * 1 / 100, height * 1 / 100);
        wishname.setLayoutParams(layoutedit);
        wishname.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutbtn = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutbtn.width = width * 10 / 100;
        layoutbtn.height = height * 4 / 100;
        layoutbtn.gravity = Gravity.CENTER;
        layoutbtn.setMargins(width * 1 / 100, height * 1 / 100,
                width * 1 / 100, height * 1 / 100);
        add.setLayoutParams(layoutbtn);
        add.setGravity(Gravity.CENTER);

        if (width >= 600) {

            select.setTextSize(17);
            wishname.setTextSize(17);

        } else if (width > 501 && width < 600) {

            select.setTextSize(16);
            wishname.setTextSize(16);

        } else if (width > 260 && width < 500) {

            select.setTextSize(15);
            wishname.setTextSize(15);

        } else if (width <= 260) {

            select.setTextSize(14);
            wishname.setTextSize(14);

        }
    }

    private void Init() {
        // TODO Auto-generated method stub

        select = (TextView) findViewById(R.id.wish_start_text);
        addlay = (LinearLayout) findViewById(R.id.wishlay);
        wishname = (EditText) findViewById(R.id.wishname);
        add = (Button) findViewById(R.id.wishbtn);

        Constant.typeFace(this, select);
        Constant.typeFace(this, wishname);
        Constant.typeFace(this, add);
    }
}
