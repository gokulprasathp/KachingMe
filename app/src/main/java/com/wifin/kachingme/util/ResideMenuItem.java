package com.wifin.kachingme.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;

import static android.content.Context.WINDOW_SERVICE;

public class ResideMenuItem extends LinearLayout {

    String sideIdentification;
    /**
     * menu item  icon
     */
    private ImageView iv_icon;
    /**
     * menu item  title
     */
    private TextView tv_title;
    /* Linear Layout */
    private LinearLayout linearResideMenuItem;

//    public ResideMenuItemLeft(Context context) {
//        super(context);
//        initViews(context);
//    }

//    public ResideMenuItemLeft(Context context, int icon, int title) {
//        super(context);
//        initViews(context);
//        iv_icon.setImageResource(icon);
//        tv_title.setText(title);
//    }

    public ResideMenuItem(Context context, int icon, String title, String side) {
        super(context);
        sideIdentification = side;
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (sideIdentification.equalsIgnoreCase("left")) {
            inflater.inflate(R.layout.residemenu_itemleft, this);
        } else {
            inflater.inflate(R.layout.residemenu_itemright, this);
        }
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
        try {
            Constant.typeFace(context,tv_title);
        } catch (Exception e) {
            e.printStackTrace();
        }
        linearResideMenuItem = (LinearLayout) findViewById(R.id.linearResideMenuItem);
         screenArrangement();

    }

    private void screenArrangement() {
        WindowManager wm = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        if (sideIdentification.equalsIgnoreCase("left")) {
            LayoutParams menuItemReside = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            menuItemReside.height = height*15/100;
            linearResideMenuItem.setLayoutParams(menuItemReside);
            linearResideMenuItem.setGravity(Gravity.LEFT);

            LayoutParams img = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            img.height = 5 * height / 100;
            img.width = 5 * height / 100;
            img.gravity = Gravity.CENTER;
            img.leftMargin = width * 4 / 100;
            img.rightMargin = width * 6 / 100;
            iv_icon.setLayoutParams(img);

            LayoutParams text = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            text.height = 10 * height / 100;
            text.gravity = Gravity.CENTER|Gravity.LEFT;
            tv_title.setLayoutParams(text);
            tv_title.setGravity(Gravity.CENTER|Gravity.LEFT);
        }else{
            LayoutParams menuItemReside = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            menuItemReside.height = height*15/100;
            linearResideMenuItem.setLayoutParams(menuItemReside);
            linearResideMenuItem.setGravity(Gravity.RIGHT);

            LayoutParams text = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            text.height = 10 * height / 100;
            text.gravity = Gravity.CENTER|Gravity.RIGHT;
            tv_title.setLayoutParams(text);
            tv_title.setGravity(Gravity.CENTER|Gravity.RIGHT);

            LayoutParams img = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            img.height = 5 * height / 100;
            img.width = 5 * height / 100;
            img.gravity = Gravity.CENTER;
            img.leftMargin = width * 8 / 100;
//            img.rightMargin = width * 1 / 100;
            iv_icon.setLayoutParams(img);
        }

        if (width >= 600) {
            tv_title.setTextSize(20);
        } else if (width > 501 && width < 600) {
            tv_title.setTextSize(19);
        } else if (width > 260 && width < 500) {
            tv_title.setTextSize(18);
        } else if (width <= 260) {
            tv_title.setTextSize(17);
        }
    }

    /**
     * set the icon color;
     *
     * @param icon
     */
    public void setIcon(int icon) {
        iv_icon.setImageResource(icon);
    }

    /**
     * set the title with resource
     * ;
     *
     * @param title
     */
    public void setTitle(int title) {
        tv_title.setText(title);
    }

    /**
     * set the title with string;
     *
     * @param title
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }
}
