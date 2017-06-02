/*
* @author Gokul, Dilip & Sivanesan
*
* @usage -  This class is the home screen of the project
*
*
* */

package com.wifin.kachingme.chat_home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.bradcast_recivers.GlobalBroadcast;
import com.wifin.kachingme.buxs.BuxSActivity;
import com.wifin.kachingme.cart.CartActivity;
import com.wifin.kachingme.chat.broadcast_chat.Broadcast_create;
import com.wifin.kachingme.chat.muc_chat.MUC_Info;
import com.wifin.kachingme.chat.muc_chat.NewGroup_FragmentActivity;
import com.wifin.kachingme.chat.muc_chat.NewGroup_MemberList;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.kaching_feature.auto_response.ResponseActivity;
import com.wifin.kachingme.kaching_feature.dazz.BannerActivity;
import com.wifin.kachingme.kaching_feature.dazz.BannerActivityChat;
import com.wifin.kachingme.kaching_feature.dazz.BannerActivityDazzAdapter;
import com.wifin.kachingme.kaching_feature.dazz.BannerActivityLED;
import com.wifin.kachingme.kaching_feature.dazz.BannerActivityZzleAdapter;
import com.wifin.kachingme.kaching_feature.dazz.BannerPreview;
import com.wifin.kachingme.kaching_feature.dazz.DazzPlainActivity;
import com.wifin.kachingme.kaching_feature.dazz.DazzTabActivity;
import com.wifin.kachingme.kaching_feature.karaoke.KaraokeActivity;
import com.wifin.kachingme.kaching_feature.karaoke.KaraokeActivityGroup;
import com.wifin.kachingme.kaching_feature.karaoke.KaraokeListActivity;
import com.wifin.kachingme.kaching_feature.karaoke.KaraokeMainActivity;
import com.wifin.kachingme.kaching_feature.karaoke.KaraokeMainGroup;
import com.wifin.kachingme.kaching_feature.karaoke.KaraokeTab;
import com.wifin.kachingme.kaching_feature.karaoke.KaraokeYoutubeActivity;
import com.wifin.kachingme.kaching_feature.karaoke.SmackThatActivity;
import com.wifin.kachingme.kaching_feature.kons.KonsActivity;
import com.wifin.kachingme.kaching_feature.kons.KonsHomeScreen;
import com.wifin.kachingme.kaching_feature.nynms.MultipleNynmActivity;
import com.wifin.kachingme.kaching_feature.nynms.NynmActivity;
import com.wifin.kachingme.kaching_feature.nynms.NynmAddActivity;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.LoginPostPojo;
import com.wifin.kachingme.services.ContactLastSync;
import com.wifin.kachingme.services.ContactService;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.settings.NotificationSettings;
import com.wifin.kachingme.settings.Profile;
import com.wifin.kachingme.settings.SettingsActivity;
import com.wifin.kachingme.settings.Status;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.GPSTrackerUtils;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.NotificationSharedPreference;
import com.wifin.kachingme.util.ResideMenu;
import com.wifin.kachingme.util.ResideMenuItem;
import com.wifin.kachingme.util.SimpleGestureFilter;
import com.wifin.kachingme.util.SimpleGestureFilter.SimpleGestureListener;

import org.jivesoftware.smack.packet.Presence;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SliderTesting extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener,
        SimpleGestureListener, OnTouchListener {
    public static String jid, home_title;
    public static Context context;
    public static EditText search_edit;
    public static TextView chatc, groupc, contactc;
    public static String msg_ids = null;
    public static ImageView mBottomChatImage, mBottomBuxsImage, mBottomCartImage, mForwardBackImage,
            mHeaderImage, mHeaderBack, mHeaderClose;
    public static EditText mHeaderEditText;
    public static TabLayout tabLayout;
    public static SliderTesting mActivity;
    static int backpress = 0;
    int height, width;
    FrameLayout content;
    View mViewBottom;
    DatabaseHelper dbadapter = KachingMeApplication.getDatabaseAdapter();
    SharedPreferences sp;
    Editor ed;
    int[] self_desc_time = {0, 5, 10, 15};
    Dbhelper db;
    DatabaseHelper dbbase;
    String mCount;
    String db_data, action, type;
    ImageView mLeftSliderMenu_Img, mLeftSliderLogo_Img, mSliderLogo_Img;
    public static ImageView mRightSliderMenu_Add,mRightSliderMenu_Search;
    public static ProgressBar progressBarRefresh;
    AppBarLayout mHomeAppBarLayout;
    CoordinatorLayout mCoorddinatorLayout;
    Toolbar mToolBar;
    LinearLayout mToolbarIconLayout, mSliderLogoLayout;
    DrawerLayout mDrawerLayout;
    FloatingActionButton mFloatingActionBtn, mFloatingActionBtnGroupAdd;
    public static FloatingActionButton mFloatingActionBtnRefresh;
    public static HomeTabSwipe adapter;
    boolean isForward;
    LinearLayout mBottomLayout, mBottomChatLayout, mBottomCartLayout, mBottomBuxsLayout, mForwardLayout;
    TextView mBottomChatText, mBottomBuxsText, mBottomCartText, mForwardText;
    LinearLayout mSliderRight, mSliderLeft, mHeaderLayout;
    String stringLatitude, stringLongitude;
    double latitude, longitude;
    SharedPreferences preference;
    /* bottom Layout VarIABLE*/
    public static ViewPager viewPager;
    private ResideMenu resideMenu;
    private ResideMenuItem menuLeftChat, menuLeftNymn, menuLeftDazz, menuLeftKons, menuLeftDest, menuLeftKrok,
            menuLeftResp, menuLeftBuxs, menuLeftCart;
    private ResideMenuItem menuRightSets, menuRightStas, menuRightProf, menuRightShsr, menuRightBrod,
            menuRightShwt, menuRightWher, menuRightLogout;

    CommonMethods commonMethods;

    SharedPreferences prefResponse;
    String preferenceResp = "auto_response", responseMsg = "";
    boolean response_state = false;

    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

    public static Context getContext() {
        // TODO Auto-generated method stub
        return context;
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public static void logoutTheUserFirebase() {
        KachingMeApplication.getsharedpreferences_Editor().remove("wallpaper").commit();
        Constant.freelistmain.clear();
        Constant.printMsg("deleted db::beforeeeeee");
        mActivity.ed.remove("pin");
        mActivity.ed.remove("sec_count");
        mActivity.ed.clear();
        mActivity.ed.commit();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mActivity);
        Editor editorClear = sp.edit();
        editorClear.clear();
        editorClear.commit();
        Editor editor = sp.edit();
        editor.putString("activity_name", "");
        editor.putBoolean("decline", false);
        editor.putInt("nofreebie", 0);
        editor.commit();
        SharedPreferences sps = mActivity.getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        Editor editor1 = sps.edit();
        editor1.putString("activity_name", "");
        editor1.putBoolean("decline", false);
        editor1.putInt("nofreebie", 0);
        editor1.commit();
        mActivity.dbadapter.dumpDatabase();
        mActivity.db.dumpDatabase();
        Constant.login = false;
        Constant.emptyFreebie = false;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Presence pr = new Presence(Presence.Type.unavailable);
                    TempConnectionService.connection.sendStanza(pr);
                    TempConnectionService.connection.disconnect();
                } catch (Exception e) {

                }
            }
        });
        thread.start();
        mActivity.deleteInternalAndExternalStorage();
        //mActivity.resideMenu.closeMenu();
        Intent intent = new Intent(mActivity, com.wifin.kachingme.registration_and_login.MainActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        initVariable();
        screenArrangement();
        setupActionBar();
        mActivity = this;
        preference = getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        commonMethods = new CommonMethods(this);
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        try {
            db = new Dbhelper(getApplicationContext());
            dbbase = KachingMeApplication.getDatabaseAdapter();
            sp = PreferenceManager.getDefaultSharedPreferences(this);
        } catch (Exception e) {

        }

        Constant.touchTrue = "null";
        setActivitystage("SliderTesting");
        mForwardLayout.setVisibility(View.GONE);
        setUpMenu();
        resideMenu.addIgnoredView(viewPager);
        resideMenu.removeIgnoredView(mSliderLeft);
        resideMenu.removeIgnoredView(mSliderRight);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.red_status_bar));
        }

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new HomeTabSwipe(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);

        context = SliderTesting.this;

        prefResponse = context.getSharedPreferences(preferenceResp, MODE_PRIVATE);
        response_state = prefResponse.getBoolean("status_auto", false);
        responseMsg = prefResponse.getString("status_msg", null);
        Constant.printMsg("Preference Status :: " + response_state + " Msg :: " + responseMsg);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        action = intent.getAction();
        type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                // Handle text being sent
            } else if (type.startsWith("image/")) {
                Constant.singleImagUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM); // Handle single image being sent
                Constant.printMsg("Image Share :::: " + Constant.singleImagUri.toString());
            } else if (type.startsWith("video/")) {
                Constant.singleVideoUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                Constant.printMsg("Video Share ::: " + Constant.singleVideoUri.toString());
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                // Handle multiple images being sent
                Constant.multipleImageUri = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                Constant.printMsg("Image Share Multiple :::: " + Constant.multipleImageUri.toString());
            }
            if (type.startsWith("video/")) {
                // Handle multiple images being sent
                Toast.makeText(getApplicationContext(), "KachingMe Not Allow To Send Multiple Videos", Toast.LENGTH_SHORT).show();
            }
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mFloatingButtonArrangment(position);
            }

            public void onPageSelected(int position) {
                mFloatingButtonArrangment(position);

            }
        });


        if (!GlobalBroadcast.isServiceRunning(ContactService.class.getCanonicalName(), this)) {
            startService(new Intent(this, ContactService.class));
        }
        sp = this.getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        ed = sp.edit();

        try {
            dbadapter = KachingMeApplication.getDatabaseAdapter();
            if (dbadapter != null && dbadapter.getLogin().size() > 0) {
                KachingMeApplication.setUserID(dbadapter.getLogin().get(0).getUserName());
                KachingMeApplication.setNifty_name(dbadapter.getLogin().get(0)
                        .getNifty_name());
                KachingMeApplication.setNifty_email(dbadapter.getLogin().get(0)
                        .getNifty_email());
                KachingMeApplication.setAvatar(dbadapter.getLogin().get(0).getAvatar());
                KachingMeApplication.setStatus(dbadapter.getLogin().get(0).getStatus());
                KachingMeApplication.setAcra();
            }

        } catch (Exception e) {

        }

        msg_ids = null;
        Bundle bundle = getIntent().getExtras();
        String query = "SELECT * FROM " + Dbhelper.TABLE_CART;
        getCount(query);
        if (bundle != null) {
            jid = bundle.getString("jid");
            home_title = bundle.getString("Profile");
            if (bundle.getString("msg_ids") != null) {
                msg_ids = bundle.getString("msg_ids");
                isForward = true;
                mToolBar.setVisibility(View.VISIBLE);
                mToolbarIconLayout.setVisibility(View.GONE);
                mBottomLayout.setVisibility(View.GONE);
                mViewBottom.setVisibility(View.GONE);
                mFloatingActionBtn.hide();
                mFloatingActionBtnGroupAdd.hide();
                mFloatingActionBtnRefresh.hide();
                mToolBar.setBackgroundColor(getResources().getColor(R.color.kaching_color));
                mForwardLayout.setVisibility(View.VISIBLE);
            } else {
                isForward = false;
            }
        }

        try {
            new NotificationSharedPreference(this).clearDataNewtork();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        } catch (Exception e) {

        }
    }

    public void initVariable() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mSliderRight = (LinearLayout) findViewById(R.id.slider_shadowRight);
        mSliderLeft = (LinearLayout) findViewById(R.id.slider_shadowLeft);

        mLeftSliderMenu_Img = (ImageView) findViewById(R.id.left_menu);
        mLeftSliderLogo_Img = (ImageView) findViewById(R.id.slider_logo);
        mSliderLogo_Img = (ImageView) findViewById(R.id.slider_logo_image);
        mRightSliderMenu_Search = (ImageView) findViewById(R.id.right_menu_search);
        mRightSliderMenu_Add = (ImageView) findViewById(R.id.right_menu_add);
        progressBarRefresh = (ProgressBar) findViewById(R.id.right_menu_progressBar);
        mFloatingActionBtn = (FloatingActionButton) findViewById(R.id.floatingbtn);
        mFloatingActionBtnGroupAdd = (FloatingActionButton) findViewById(R.id.floatingbtnGroup);
        mFloatingActionBtnRefresh = (FloatingActionButton) findViewById(R.id.floatingbtnRefresh);
        mToolbarIconLayout = (LinearLayout) findViewById(R.id.slider_toolbarIconLayout);
        mCoorddinatorLayout = (CoordinatorLayout) findViewById(R.id.home_corordinator_layout);
        mHomeAppBarLayout = (AppBarLayout) findViewById(R.id.home_appbar_layout);

        tabLayout = (TabLayout) findViewById(R.id.home_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.home_viewpager);
        mToolBar = (Toolbar) findViewById(R.id.home_toolbar_layout);

        mBottomLayout = (LinearLayout) findViewById(R.id.sliderTesting_footer);
        mBottomChatLayout = (LinearLayout) findViewById(R.id.sliderTesting_footerChatLayout);
        mBottomCartLayout = (LinearLayout) findViewById(R.id.sliderTesting_footerBuxsLayout);
        mBottomBuxsLayout = (LinearLayout) findViewById(R.id.sliderTesting_footerCartLayout);
        mForwardLayout = (LinearLayout) findViewById(R.id.slider_toolbarForwardLayout);
        mSliderLogoLayout = (LinearLayout) findViewById(R.id.slider_logoLayout);
        mBottomChatImage = (ImageView) findViewById(R.id.sliderTesting_footerChatImage);
        mBottomBuxsImage = (ImageView) findViewById(R.id.sliderTesting_footerBuxsImage);
        mBottomCartImage = (ImageView) findViewById(R.id.sliderTesting_footerCartImage);
        mForwardBackImage = (ImageView) findViewById(R.id.slider_forwardBackImage);
        mBottomChatText = (TextView) findViewById(R.id.sliderTesting_footerChatText);
        mBottomBuxsText = (TextView) findViewById(R.id.sliderTesting_footerBuxsText);
        mForwardText = (TextView) findViewById(R.id.slider_forwardTextView);
        mBottomCartText = (TextView) findViewById(R.id.sliderTesting_footerCartText);
        mViewBottom = (View) findViewById(R.id.slider_bottomView);
        content = (FrameLayout) findViewById(R.id.main_layout_inner);
        search_edit = (EditText) findViewById(R.id.slider_serchEdit);
        chatc = (TextView) findViewById(R.id.chatcount);
        groupc = (TextView) findViewById(R.id.groupcount);
        contactc = (TextView) findViewById(R.id.contactcount);

        Constant.typeFace(this, contactc);
        Constant.typeFace(this, groupc);
        Constant.typeFace(this, chatc);
        Constant.typeFace(this, mBottomChatText);
        Constant.typeFace(this, mBottomBuxsText);
        Constant.typeFace(this, mBottomCartText);
        Constant.typeFace(this, mForwardText);

        mBottomChatLayout.setOnClickListener(this);
        mBottomBuxsLayout.setOnClickListener(this);
        mBottomCartLayout.setOnClickListener(this);
        mLeftSliderMenu_Img.setOnClickListener(this);
        mRightSliderMenu_Search.setOnClickListener(this);
        mRightSliderMenu_Add.setOnClickListener(this);
        mFloatingActionBtn.setOnClickListener(this);
        mFloatingActionBtnRefresh.setOnClickListener(this);
        mFloatingActionBtnGroupAdd.setOnClickListener(this);
        mSliderLeft.setOnTouchListener(this);
        mSliderRight.setOnTouchListener(this);
        mForwardBackImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sliderTesting_footerChatLayout:
                Constant.mDazZFromSlider = false;
                Constant.mZzle = false;
                if (tabLayout.getSelectedTabPosition() == 0) {
                    Constant.printMsg("siva chat tab::" + tabLayout.getSelectedTabPosition());
                } else {
                    KachingMeApplication.setUserID(dbadapter.getLogin().get(0).getUserName());
                    KachingMeApplication.setNifty_name(dbadapter.getLogin().get(0).getNifty_name());
                    KachingMeApplication.setNifty_email(dbadapter.getLogin().get(0).getNifty_email());
                    KachingMeApplication.setAvatar(dbadapter.getLogin().get(0).getAvatar());
                    KachingMeApplication.setStatus(dbadapter.getLogin().get(0).getStatus());
                    KachingMeApplication.setAcra();
                    startActivity(new Intent(SliderTesting.this, SliderTesting.class));
                    finish();
                }
                break;
            case R.id.sliderTesting_footerBuxsLayout:
                Constant.mDazZFromSlider = false;
                Constant.mZzle = false;
                startActivity(new Intent(SliderTesting.this, BuxSActivity.class));
                finish();
                break;
            case R.id.sliderTesting_footerCartLayout:
                //Constant.mDazZFromSlider = false;
                // Constant.mZzle = false;
                //startActivity(new Intent(SliderTesting.this, CartActivity.class));
                //finish();
                checkDefaultTime();
                // cartValidationProcess();
                break;
            case R.id.left_menu:
//                resideMenu.closeMenu();
//                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                break;
            case R.id.right_menu_search:
                //resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                tabLayout.setVisibility(View.GONE);
                mRightSliderMenu_Search.setVisibility(View.GONE);
                mRightSliderMenu_Add.setVisibility(View.GONE);
                mSliderLogoLayout.setVisibility(View.GONE);
                mHeaderClose.setVisibility(View.GONE);
                progressBarRefresh.setVisibility(View.GONE);
                mHeaderLayout.setVisibility(View.VISIBLE);
                showSoftKeyboard(mHeaderEditText);
                break;
            case R.id.right_menu_add:
                if (Constant.checkPermission(getApplicationContext(), Manifest.permission.WRITE_CONTACTS)) {
                    try {
                        Intent addContactIntent = new Intent(Intent.ACTION_INSERT);
                        addContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                        addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, "");
                        startActivity(addContactIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    resideMenu.closeMenu();
                } else {
                    Constant.permissionRequest(this, Manifest.permission.WRITE_CONTACTS, Constant.PERMISSION_CODE_CONTACTS);
                }

                break;
            case R.id.floatingbtn:
                viewPager.setCurrentItem(2);
                break;
            case R.id.floatingbtnGroup:
                startActivity(new Intent(SliderTesting.this, NewGroup_FragmentActivity.class));
                finish();
                break;
            case R.id.slider_forwardBackImage:

                if (msg_ids != null)
                    msg_ids = null;

                finish();
                Constant.printMsg("Slider Forward back pressed...");
                // finish();
                /*need code to back press of chat*/
                break;




            case R.id.floatingbtnRefresh:
                if (Connectivity.isConnected(this)) {
                    mFloatingActionBtnRefresh.setVisibility(View.GONE);
                    //Animation startRotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_view);
                    //mFloatingActionBtnRefresh.startAnimation(startRotateAnimation);
                    mRightSliderMenu_Add.setVisibility(View.GONE);
                    mRightSliderMenu_Search.setVisibility(View.INVISIBLE);
                    progressBarRefresh.setVisibility(View.VISIBLE);
                    if (!GlobalBroadcast.isServiceRunning(ContactLastSync.class.getCanonicalName(), this)) {
                        startService(new Intent(SliderTesting.this, ContactLastSync.class));
                    }
                } else {
                    Toast.makeText(this, "Check Your Network Connection.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.slider_backArrow:
                getSupportActionBar().hide();
                mHeaderLayout.setVisibility(View.GONE);
                mSliderLogoLayout.setVisibility(View.VISIBLE);
                mRightSliderMenu_Search.setVisibility(View.VISIBLE);
                mRightSliderMenu_Add.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
//                mHeaderEditText.setFocusable(false);
                mHeaderEditText.setText("");
                hideSoftKeyboard();


                if (tabLayout.getSelectedTabPosition() == 2) {
                    //FavouriteContacts.loadContactList();
                    Intent loadContactBroadcast = new Intent("contact_update");
                    getApplicationContext().sendBroadcast(loadContactBroadcast);
                }
                break;
            case R.id.slider_close:
                mHeaderEditText.setText("");
                if (tabLayout.getSelectedTabPosition() == 2) {
                    //FavouriteContacts.loadContactList();
                    Intent loadContactBroadcast = new Intent("contact_update");
                    getApplicationContext().sendBroadcast(loadContactBroadcast);
                }
                break;

            default:
                resideMenu.closeMenu();
                break;
        }
        if (v == menuLeftChat) {
            resideMenu.closeMenu();
        } else if (v == menuLeftNymn) {
            Constant.mNynmFromSlider = true;
            startActivity(new Intent(SliderTesting.this, NynmActivity.class));
//            finish();
            resideMenu.closeMenu();
        } else if (v == menuLeftDazz) {
            Constant.mDazZFromSlider = true;
            Constant.mChatText = "";
            startActivity(new Intent(SliderTesting.this, DazzPlainActivity.class));
//            finish();
            resideMenu.closeMenu();
        } else if (v == menuLeftKons) {
            Constant.mKonsFromSlider = true;
            startActivity(new Intent(SliderTesting.this, KonsHomeScreen.class));
//            finish();
            resideMenu.closeMenu();
        } else if (v == menuLeftDest) {
            Show_Self_desc_time(0);
            resideMenu.closeMenu();
        } else if (v == menuLeftKrok) {
            Constant.mKroKFromSlider = true;
            startActivity(new Intent(SliderTesting.this, KaraokeListActivity.class));
            //finish();
            resideMenu.closeMenu();
        } else if (v == menuLeftResp) {
            startActivity(new Intent(SliderTesting.this, ResponseActivity.class));
            finish();
            resideMenu.closeMenu();
        } else if (v == menuLeftBuxs) {
            resideMenu.closeMenu();
        } else if (v == menuLeftCart) {
            resideMenu.closeMenu();
        } else if (v == menuRightSets) {
            startActivity(new Intent(SliderTesting.this, SettingsActivity.class));
            finish();
            resideMenu.closeMenu();
        } else if (v == menuRightStas) {
            startActivity(new Intent(SliderTesting.this, Status.class));
            finish();
            resideMenu.closeMenu();
        } else if (v == menuRightProf) {
            startActivity(new Intent(SliderTesting.this, Profile.class));
            finish();
            resideMenu.closeMenu();
        } else if (v == menuRightShsr) {
            Constant.cartboolean = true;
            startActivity(new Intent(SliderTesting.this, NotificationSettings.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .putExtra("TAG", "Share"));
            finish();
            resideMenu.closeMenu();
        } else if (v == menuRightBrod) {

            if (Connectivity.isOnline(context)) {
                startActivity(new Intent(SliderTesting.this, Broadcast_create.class));
            } else {
                Toast.makeText(getContext(), "No Network Connection.", Toast.LENGTH_SHORT).show();
            }
            resideMenu.closeMenu();
        } else if (v == menuRightShwt) {
            resideMenu.closeMenu();
        } else if (v == menuRightWher) {
            resideMenu.closeMenu();
        } else if (v == menuRightLogout) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper
                    (context, android.R.style.Theme_Holo_Light_Dialog));
            alertDialogBuilder
                    //.setTitle(context.getResources().getString(R.string.app_name))
                    .setMessage(Html.fromHtml("<font color=#232323>" + getResources().getString(R.string.are_you_sure)
                            + " you want to logout." + "</font>"))
                    .setCancelable(false)
                    .setPositiveButton(Html.fromHtml("<font color=#232323>" + "Yes" + "</font>"),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    if (Connectivity.isConnected(SliderTesting.this)) {
                                        if (Connectivity.isTempConnection()) {
                                            GPSTrackerUtils gpsTracker = new GPSTrackerUtils(SliderTesting.this);
                                            // check if GPS enabled
                                            if (gpsTracker.canGetLocation()) {
                                                latitude = gpsTracker.getLatitude();
                                                longitude = gpsTracker.getLongitude();
                                                new logoutTheUser().execute();
                                            } else {
                                                gpsTracker.showSettingsAlert();
                                            }
                                        } else {
                                            Toast.makeText(SliderTesting.this, "Something Went Wrong.! Please Try Again.!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(SliderTesting.this, "Please Check Your Network Connection.!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                    .setNegativeButton(Html.fromHtml("<font color=#232323>" + "No" + "</font>"),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private String postLogoutData() {
        stringLatitude = String.valueOf(latitude);
        stringLongitude = String.valueOf(longitude);
        LoginPostPojo loginPostPojo = new LoginPostPojo();
        loginPostPojo.setUserName(preference.getString("MyPrimaryNumber", ""));
        loginPostPojo.setOtp("");
        loginPostPojo.setLongitude(stringLatitude);
        loginPostPojo.setLatitude(stringLongitude);
        loginPostPojo.setActivityNo(preference.getString("ChatUserNumber", ""));
        if (preference.getString("ImeiNo", "") != null) {
            loginPostPojo.setImei(preference.getString("ImeiNo", ""));
        } else {
            loginPostPojo.setImei("");
        }
        String data = new Gson().toJson(loginPostPojo);
        return data;
    }

    private void checkDefaultTime() {
        // TODO Auto-generated method stub
        int currentApiVersion = Build.VERSION.SDK_INT;
        try {
            if (currentApiVersion >= 17) {
                if (Settings.Global.getInt(getContentResolver(),
                        Settings.Global.AUTO_TIME) == 1) {
                    cartValidationProcess();
                } else {
                    commonMethods.autoTimeEnable();
                }
            } else {
                if (Settings.System.getInt(getContentResolver(),
                        Settings.System.AUTO_TIME) == 1) {
                    cartValidationProcess();
                } else {
                    commonMethods.autoTimeEnable();
                }
            }
        } catch (Settings.SettingNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void cartValidationProcess() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormate = new SimpleDateFormat("dd-MM-yyyy");
        String cureentDate = simpleDateFormate.format(cal.getTime());
        String expiryDate = sp.getString("ExpiryDate", "");
        try {
            Constant.printMsg("Date Analyzing current and Expired......" + new Date() + "....cur...." + cureentDate + "...exp...." + expiryDate);
            if (expiryDate != null && !expiryDate.isEmpty()) {
                if (cureentDate.equals(expiryDate)) {
                    Constant.printMsg("Date Analyzing Not Expired..equal....");
                    Constant.mDazZFromSlider = false;
                    Constant.mZzle = false;
                    startActivity(new Intent(SliderTesting.this, CartActivity.class));
                    finish();
                } else {
                    if (new Date().after(new SimpleDateFormat("dd-MM-yyyy").parse(expiryDate))) {
                        Constant.printMsg("Date Analyzing Expired..last...." + new Date());
                        Toast.makeText(this, "Freebie Expired !!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Constant.printMsg("Date Analyzing Not Expired......");
                        Constant.mDazZFromSlider = false;
                        Constant.mZzle = false;
                        startActivity(new Intent(SliderTesting.this, CartActivity.class));
                        finish();
                    }
                }
            } else {
                Toast.makeText(this, "Your Cart is Empty !!!", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Network Error !!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (msg_ids != null)
            msg_ids = null;
        super.onDestroy();
        Constant.printMsg("onDestroy Called............");
        // hideSoftKeyboard();
    }

    @Override
    protected void onStop() {

        super.onStop();
        Constant.printMsg("onStop Called..............");
        // hideSoftKeyboard();
    }

    //siva
    @Override
    protected void onResume() {
        super.onResume();
        try {
            Constant.printMsg("onResume Called............");
            Broadcast_create.selected_users = null;
            MUC_Info.selected_users = null;
            NewGroup_MemberList.selected_users = null;

            if (resideMenu.isOpened()) {
                resideMenu.closeMenu();
            }

            tabLayout = (TabLayout) findViewById(R.id.home_tab_layout);
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            tabLayout.setupWithViewPager(viewPager);

            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                tab.setCustomView(adapter.getTabView(i));
            }

            getSupportActionBar().hide();
            mHeaderLayout.setVisibility(View.GONE);
            mSliderLogoLayout.setVisibility(View.VISIBLE);
            mRightSliderMenu_Search.setVisibility(View.VISIBLE);
            mRightSliderMenu_Add.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
//                mHeaderEditText.setFocusable(false);
            if (!mHeaderEditText.getText().toString().isEmpty())
                mHeaderEditText.setText("");
            hideSoftKeyboard();
            mFinishBackgroundAcrtivity();
            setCurrentTab();

//            if(viewPager.getAdapter()!=null){
//                viewPager.getAdapter().notifyDataSetChanged();
//            }

        } catch (Exception e) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constant.printMsg("onPause siva onPause" + "slider_shadowLeft");
        hideSoftKeyboard();
    }

    private void deleteInternalAndExternalStorage() {
        File backUpDirectory;
        File directory;
        if (Environment.getExternalStorageState() == null) {
            /*phone does not have sd card*/
            directory = new File(Environment.getDataDirectory()
                    + "/Kaching.me/");
            backUpDirectory = new File(Environment.getDataDirectory()
                    + "/Kaching.me/Kaching.me Backup");
            if (backUpDirectory.exists()) {
                Constant.printMsg("photo exists........sd............");
                File[] dirFiles = backUpDirectory.listFiles();
                if (dirFiles.length != 0) {
                    for (int ii = 0; ii <= dirFiles.length; ii++) {
                        dirFiles[ii].delete();
                    }
                }
            } else {
                Constant.printMsg("photo not exists........sd............");
            }
            // if no directory exists, create new directory
            if (!directory.exists()) {
                directory.mkdir();
            }

        } else if (Environment.getExternalStorageState() != null) {
            /*phone does have sd card but sometime it takes even not sd card*/
            // search for directory on SD card
            directory = new File(Environment.getExternalStorageDirectory()
                    + "/Kaching.me/");
            backUpDirectory = new File(
                    Environment.getExternalStorageDirectory()
                            + "/Kaching.me/Kaching.me Backup");
            if (backUpDirectory.exists()) {
                Constant.printMsg("photo exists....................");
                File[] dirFiles = backUpDirectory.listFiles();
                Constant.printMsg("photo exists...................." + dirFiles.length);
                if (dirFiles.length > 0) {
                    for (int ii = 0; ii < dirFiles.length; ii++) {
                        dirFiles[ii].delete();
                    }
                    dirFiles = null;
                }
            } else {
                Constant.printMsg("photo not exists....................");
            }
            // if no directory exists, create new directory to store test
            // results
            if (!directory.exists()) {
                directory.mkdir();
            }
        }
    }

    private void mFloatingButtonArrangment(int position) {
        Constant.printMsg("siva get position......." + position);
        switch (position) {
            case 0:
                if (isForward) {
                    mFloatingActionBtnRefresh.hide();
                    mFloatingActionBtnGroupAdd.hide();
                    mFloatingActionBtn.hide();
                } else {
                    mFloatingActionBtnRefresh.hide();
                    mFloatingActionBtnGroupAdd.hide();
                    mFloatingActionBtn.show();
                }
                break;
            case 1:
                if (isForward) {
                    mFloatingActionBtnRefresh.hide();
                    mFloatingActionBtnGroupAdd.hide();
                    mFloatingActionBtn.hide();
                } else {
                    mFloatingActionBtnRefresh.hide();
                    mFloatingActionBtn.hide();
                    mFloatingActionBtnGroupAdd.show();
                }
                break;
            case 2:
                if (isForward) {
                    mFloatingActionBtnRefresh.hide();
                    mFloatingActionBtnGroupAdd.hide();
                    mFloatingActionBtn.hide();
                } else {
                    mFloatingActionBtn.hide();
                    mFloatingActionBtnGroupAdd.hide();
                    mFloatingActionBtnRefresh.show();
                }
                break;
            default:
                break;
        }
    }

    public void showSoftKeyboard(View view) {
        view.requestFocus();
        view.setFocusable(true);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        // inputMethodManager.showSoftInput(view, 0);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolBar.getLayoutParams();
        params.setScrollFlags(0);
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolBar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
    }

    private void setUpMenu() {
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.bg3);
        resideMenu.attachToActivity(this);
//        resideMenu.setMenuListener(menuListener);
//        resideMenu.setScaleValue(0.6f);

//        menuLeftChat = new ResideMenuItem(this, R.drawable.selectorfornewchat, "ChaT","left");
        menuLeftNymn = new ResideMenuItem(this, R.drawable.selectorfornewnymz, "NynM", "left");
        menuLeftDazz = new ResideMenuItem(this, R.drawable.selectorfornewdazz, "DazZ", "left");
        menuLeftKons = new ResideMenuItem(this, R.drawable.selectorfornewkons, "KonS", "left");
        menuLeftDest = new ResideMenuItem(this, R.drawable.selectorfornewdes, "DesT", "left");
        menuLeftKrok = new ResideMenuItem(this, R.drawable.selectorfornewkrok, "KroK", "left");
        menuLeftResp = new ResideMenuItem(this, R.drawable.selectorforresp, "ResP", "left");
//        menuLeftAddContact = new ResideMenuItem(this, R.drawable.selector_for_profile, "Add ContacT", "left");
//        menuLeftBuxs = new ResideMenuItem(this, R.drawable.lefts_buxs, "BuxS");
//        menuLeftCart = new ResideMenuItem(this, R.drawable.lefts_cart, "CarT");
//        menuLeftChat.setOnClickListener(this);
        menuLeftNymn.setOnClickListener(this);
        menuLeftDazz.setOnClickListener(this);
        menuLeftKons.setOnClickListener(this);
        menuLeftDest.setOnClickListener(this);
        menuLeftKrok.setOnClickListener(this);
        menuLeftResp.setOnClickListener(this);
//        menuLeftAddContact.setOnClickListener(this);
//        menuLeftCart.setOnClickListener(this);
//        resideMenu.addMenuItem(menuLeftChat, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(menuLeftNymn, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(menuLeftDazz, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(menuLeftKons, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(menuLeftDest, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(menuLeftKrok, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(menuLeftResp, ResideMenu.DIRECTION_LEFT);
//        resideMenu.addMenuItem(menuLeftAddContact, ResideMenu.DIRECTION_LEFT);
//        resideMenu.addMenuItem(menuLeftCart, ResideMenu.DIRECTION_LEFT);
//        c[0] = menuLeftNymn.getId();
//        c[1] = menuLeftNymn.getId();
//        idView=menuLeftNymn.getId();
        menuRightSets = new ResideMenuItem(this, R.drawable.selector_for_settings, "SetS", "right");
        menuRightStas = new ResideMenuItem(this, R.drawable.selector_for_stas, "StaS", "right");
        menuRightProf = new ResideMenuItem(this, R.drawable.selector_for_profile, "ProF", "right");
        menuRightShsr = new ResideMenuItem(this, R.drawable.selector_for_share, "ShsR", "right");
        menuRightBrod = new ResideMenuItem(this, R.drawable.selector_for_brod, "BroD", "right");
//        menuRightShwt = new ResideMenuItem(this, R.drawable.symbols, "ShwT");
//        menuRightWher = new ResideMenuItem(this, R.drawable.icon_home, "WheR");
        menuRightLogout = new ResideMenuItem(this, R.drawable.selector_for_logout, "Logout", "right");

        menuRightSets.setOnClickListener(this);
        menuRightStas.setOnClickListener(this);
        menuRightProf.setOnClickListener(this);
        menuRightShsr.setOnClickListener(this);
        menuRightBrod.setOnClickListener(this);
//        menuRightShwt.setOnClickListener(this);
//        menuRightWher.setOnClickListener(this);
        menuRightLogout.setOnClickListener(this);

        resideMenu.addMenuItem(menuRightSets, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(menuRightStas, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(menuRightProf, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(menuRightShsr, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(menuRightBrod, ResideMenu.DIRECTION_RIGHT);
//        resideMenu.addMenuItem(menuRightShwt, ResideMenu.DIRECTION_RIGHT);
//        resideMenu.addMenuItem(menuRightWher, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(menuRightLogout, ResideMenu.DIRECTION_RIGHT);
    }

    private void setupActionBar() {
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setShowHideAnimationEnabled(false);
//        mToolBar.setCollapsible(false);

//        getSupportActionBar().setBa
//        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_launcher);
//        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        View customNav = LayoutInflater.from(this).inflate(R.layout.slider_actionbar, null);

        getSupportActionBar().setCustomView(customNav, lp1);
        mHeaderLayout = (LinearLayout) findViewById(R.id.slider_actionbarLayout);
        mHeaderImage = (ImageView) findViewById(R.id.slider_Headerlogo);
        mHeaderBack = (ImageView) findViewById(R.id.slider_backArrow);
        mHeaderClose = (ImageView) findViewById(R.id.slider_close);
        mHeaderEditText = (EditText) findViewById(R.id.slider_editText);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        mHeaderLayout.setGravity(Gravity.CENTER | Gravity.LEFT);

        LinearLayout.LayoutParams mLeftLogoParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mLeftLogoParams.width = (int) width * 40 / 100;
        mLeftLogoParams.height = (int) height * 6 / 100;
        mLeftLogoParams.leftMargin = width * 25 / 100;
        mLeftLogoParams.gravity = Gravity.CENTER | Gravity.LEFT;
        mHeaderImage.setLayoutParams(mLeftLogoParams);

        LinearLayout.LayoutParams backArroeParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        backArroeParama.width = (int) width * 10 / 100;
        backArroeParama.height = (int) height * 4 / 100;
        backArroeParama.leftMargin = width * 2 / 100;
        backArroeParama.rightMargin = width * 2 / 100;
        backArroeParama.gravity = Gravity.CENTER | Gravity.LEFT;
        mHeaderBack.setLayoutParams(backArroeParama);

        LinearLayout.LayoutParams editTextParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParama.width = (int) width * 70 / 100;
        editTextParama.height = (int) height * 6 / 100;
        editTextParama.gravity = Gravity.CENTER | Gravity.LEFT;
        mHeaderEditText.setLayoutParams(editTextParama);
        mHeaderEditText.setGravity(Gravity.CENTER | Gravity.LEFT);
        mHeaderEditText.setPadding(width * 2 / 100, 0, 0, 0);

        LinearLayout.LayoutParams closeArroeParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        closeArroeParama.width = (int) (width * 8 / 100);
        closeArroeParama.height = (int) (height * 2.5 / 100);
        closeArroeParama.gravity = Gravity.CENTER | Gravity.LEFT;
        closeArroeParama.leftMargin = width * 2 / 100;
        closeArroeParama.rightMargin = width * 2 / 100;
        mHeaderClose.setLayoutParams(closeArroeParama);

        Constant.typeFace(this, mHeaderEditText);
        mHeaderBack.setOnClickListener(this);
        mHeaderClose.setOnClickListener(this);
        mHeaderEditText.setOnClickListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        mFloatingButtonArrangment(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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

    private void setActivitystage(String sliderTesting) {
        // TODO Auto-generated method stub
        ed = sp.edit();
        ed.putString("activity_name", sliderTesting);
        ed.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == 1) {
                    Boolean is_sec_chat_exist = false;
                    String jid = data.getExtras().getString("jid");
                    ContactsGetSet contact = dbadapter.getContact(jid);

                    if (sp.contains(jid + "_sec_admin")) {
                        if (sp.getInt(jid + "_sec_admin", 0) == 0
                                || sp.getInt(jid + "_sec_admin", 0) == 1) {
                            is_sec_chat_exist = true;
                        } else {
                            is_sec_chat_exist = false;
                        }
                    }

                    if (!is_sec_chat_exist) {
                        ed.putInt(jid + "_sec_admin", 0);
                        ed.commit();
                        Intent intent = new Intent(SliderTesting.this, ChatTest.class);
                        intent.putExtra("jid", contact.getJid());
                        intent.putExtra("name", contact.getDisplay_name());
                        intent.putExtra("avatar", contact.getPhoto_ts());
                        intent.putExtra("IS_SECRET_CHAT", true);
                        intent.putExtra("is_owner", "0");
                        intent.putExtra("is_new_sec", true);
                        startActivity(intent);

                    } else {
                        new AlertManager().showAlertDialog(
                                SliderTesting.this,
                                getResources().getString(
                                        R.string.secretchat_already_created), true);
                    }
                }
                break;
        }
    }

    public void Show_Self_desc_time(int selected) {
        Constant.mTimeFromSlider = 0;
        final CharSequence[] items = getResources().getStringArray(
                R.array.self_des_time);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.self_des_time));
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Constant.mTimeFromSlider == 0) {
                    Constant.mDesTFromSlider = false;

                } else {
                    Constant.mDesTFromSlider = true;

                }
                Constant.printMsg("slider trueeeeeeeeeeeeeeeee");
//                Intent i = new Intent(SliderTesting.this, SliderTesting.class);
//                startActivity(i);
//                finish();
//                if(builder!=null){
//                    builder.setCancelable()
//                }
//                if (Constant.mTimeFromSlider == 0) {
//                    viewPager.setCurrentItem(0);
//                    Constant.mDesTFromSlider = false;
//                } else {
//                    viewPager.setCurrentItem(0);
//                }

            }

        });
        builder.setSingleChoiceItems(items, selected,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Constant.mTimeFromSlider = self_desc_time[which];
                        Constant.printMsg("dhfgdalf" + Constant.mTimeFromSlider);
                    }
                });
        builder.show();

    }

    public void updateDBvalue(String code) {
        Editor e = sp.edit();
        e.putString("db_data", code);
        Constant.printMsg("updateDBvalue:while logout" + code);
        e.commit();
    }

    @Override
    public void onSwipe(int direction) {
        // TODO Auto-generated method stub
        switch (direction) {
            // Swipe Right side
            case SimpleGestureFilter.SWIPE_RIGHT:
                Display mdisp = getWindowManager().getDefaultDisplay();
                Point mdispSize = new Point();
                mdisp.getSize(mdispSize);
                int maxX = mdispSize.x;
                int maxY = mdispSize.y;
                Constant.printMsg("testttttttttttttttt.." + direction + "....X::" + maxX + "...Y:::" + maxY);
                break;

            // Swipe Left side
            case SimpleGestureFilter.SWIPE_LEFT:
                break;
        }

    }

    @Override
    public void onDoubleTap() {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.slider_shadowLeft:
//                isLeftOpen = true;
                Constant.printMsg("siva testtttttttttttttt" + "slider_shadowLeft");
                int downX = 0;
                int upX;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    downX = (int) event.getX();
                    Log.i("event.getX()", " downX " + downX);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    upX = (int) event.getX();
                    if (upX - downX > 100) {
                        Constant.printMsg("siva swipe left open status..." + resideMenu.isOpened());
                        if (!resideMenu.isOpened()) {
                            resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                            // swipe right
                            Constant.printMsg("siva swipe left..." + event.getX() + "...yyyyyy..." + event.getY());
                        }
                    } else if (downX - upX > -100) {
                        // swipe left
//                        Constant.printMsg("siva swipe Left..." + event.getX() + "...yyyyyy..." + event.getY());
                    }
                    return true;
                }
                break;
            case R.id.slider_shadowRight:
//                isRightOpen = true;
                Constant.printMsg("siva testtttttttttttttt" + "slider_shadowRight");
                int rightdownX = 0;
                int rightupX;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    rightdownX = (int) event.getX();
                    Log.i("event.getX()", " downX " + rightdownX);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    rightupX = (int) event.getX();
                    if (rightupX - rightdownX > 100) {
                        // swipe right
                    } else if (rightdownX - rightupX > -100) {
                        // swipe left
                        Constant.printMsg("siva swipe right open status..." + resideMenu.isOpened());
                        if (!resideMenu.isOpened()) {
                            resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                            Constant.printMsg("siva swipe right..." + event.getX() + "...yyyyyy..." + event.getY());
                        }
                    }
                    return true;
                }
                break;
        }
        return false;
    }

    public void setCurrentTab() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        if (Constant.mFromGroupInfo == true) {
            Constant.mFromGroupInfo = false;
            viewPager.setCurrentItem(1);
        }

        if (Constant.mFromMemberList == true) {
            Constant.printMsg("slider    ");
            Constant.mFromMemberList = false;
            viewPager.setCurrentItem(1);
        }
        if (Constant.mFromContactScreen == true) {
            Constant.printMsg("slider    ");
            Constant.mFromContactScreen = false;
            viewPager.setCurrentItem(2);
        }

        if (Constant.mKonsFromSlider == true) {
            Constant.mKonsFromSlider =false;
            viewPager.setCurrentItem(2);
        }
        if (Constant.mDazZFromSlider == true) {
            Constant.mDazZFromSlider = false;
            viewPager.setCurrentItem(2);
        }
        if (Constant.mDesTFromSlider == true) {
            Constant.mDesTFromSlider =false;
            viewPager.setCurrentItem(0);
        }
        if (Constant.mKroKFromSlider) {
            Constant.mKroKFromSlider = false;
            viewPager.setCurrentItem(2);
        }
        if (Constant.fromChat == true) {
            Constant.fromChat = false;
            viewPager.setCurrentItem(0);
        }
        if (Constant.group_lock == true) {
            Constant.group_lock = false;
            viewPager.setCurrentItem(1);
        }
        if (Constant.mFromDonateContact == true) {
            Constant.mFromDonateContact = false;
            Constant.mDonateBux = true;
            viewPager.setCurrentItem(2);
        }
        if (Constant.FromMUC_Chat == true) {
            Constant.printMsg("slider    1");
            Constant.FromMUC_Chat = false;
            viewPager.setCurrentItem(1);
        }
        if (Constant.mNynmFromSlider) {
            Constant.mNynmFromSlider = false;
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onBackPressed() {
        if (msg_ids != null) {
            msg_ids = null;
        }

        if (isForward) {
            if (msg_ids != null)
                msg_ids = null;
            isForward = false;
            finish();
        } else if (resideMenu.isOpened() == true) {
            resideMenu.closeMenu();
        } else {
            if (mHeaderLayout.getVisibility() == View.VISIBLE) {
                mHeaderLayout.setVisibility(View.GONE);
                mSliderLogoLayout.setVisibility(View.VISIBLE);
                mRightSliderMenu_Search.setVisibility(View.VISIBLE);
                mRightSliderMenu_Add.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
//                mHeaderEditText.setFocusable(false);
                mHeaderEditText.setText("");
                hideSoftKeyboard();

            } else {
                if (msg_ids != null) {
                    msg_ids = null;
                    finish();
                    return;
                }
                backpress = backpress + 1;
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        backpress = 0;
                    }
                }, 3000);
                if (backpress == 1) {
                    Toast.makeText(this, "Click again to close Application ",
                            Toast.LENGTH_SHORT).show();
                }
                if (backpress == 2) {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    finish();
//                    }
                }
            }
        }
    }

    public void screenArrangement() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        Constant.screenWidth = width;
        Constant.screenHeight = height;

        LinearLayout.LayoutParams searchEditParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        searchEditParama.width = width * 70 / 100;
        searchEditParama.gravity = Gravity.CENTER;
        search_edit.setLayoutParams(searchEditParama);

        FrameLayout.LayoutParams chatcl = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        chatcl.width = width * 6 / 100;
        chatcl.height = height * 4 / 100;
        chatcl.leftMargin = width * 26 / 100;
        chatcl.topMargin = height * 3 / 100;
        chatcl.gravity = Gravity.TOP;
        chatc.setLayoutParams(chatcl);
        chatc.setGravity(Gravity.CENTER);

        FrameLayout.LayoutParams groupcl = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        groupcl.width = width * 6 / 100;
        groupcl.height = height * 4 / 100;
        groupcl.leftMargin = width * 58 / 100;
        groupcl.topMargin = height * 3 / 100;
        groupcl.gravity = Gravity.TOP;
        groupc.setLayoutParams(groupcl);
        groupc.setGravity(Gravity.CENTER);

        FrameLayout.LayoutParams contcl = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        contcl.width = width * 6 / 100;
        contcl.height = height * 4 / 100;
        contcl.leftMargin = width * 93 / 100;
        contcl.topMargin = height * 3 / 100;
        contcl.gravity = Gravity.TOP;
        contactc.setLayoutParams(contcl);
        contactc.setGravity(Gravity.CENTER);

        AppBarLayout.LayoutParams mToolBarLayoutParams = new AppBarLayout.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        mToolBarLayoutParams.width = (int) width;
        mToolBarLayoutParams.height = (int) height * 8 / 100;
        mToolBarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        mToolBar.setLayoutParams(mToolBarLayoutParams);

        Toolbar.LayoutParams mToolBarForwardLayoutParama = new Toolbar.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mToolBarForwardLayoutParama.width = width * 100 / 100;
        mToolBarForwardLayoutParama.height = height * 8 / 100;
        mForwardLayout.setLayoutParams(mToolBarForwardLayoutParama);

        LinearLayout.LayoutParams leftBackPressParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        leftBackPressParama.width = (int) width * 8 / 100;
        leftBackPressParama.height = (int) width * 8 / 100;
        leftBackPressParama.gravity = Gravity.CENTER | Gravity.LEFT;
        mForwardBackImage.setLayoutParams(leftBackPressParama);

        LinearLayout.LayoutParams forwardTetParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        forwardTetParama.gravity = Gravity.CENTER | Gravity.LEFT;
        forwardTetParama.leftMargin = width * 5 / 100;
        mForwardText.setLayoutParams(forwardTetParama);

        AppBarLayout.LayoutParams mTabLayoutParams = new AppBarLayout.LayoutParams(
                TabLayout.LayoutParams.WRAP_CONTENT,
                TabLayout.LayoutParams.WRAP_CONTENT);
        mTabLayoutParams.width = (int) width;
        mTabLayoutParams.height = (int) height * 12 / 100;
        mTabLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED);
        tabLayout.setLayoutParams(mTabLayoutParams);

        Toolbar.LayoutParams mToolBarLayoutParama = new Toolbar.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mToolBarLayoutParama.width = width;
        mToolBarLayoutParama.height = height * 8 / 100;
        mToolBarLayoutParama.gravity = Gravity.CENTER;
        //mToolBarLayoutParama.leftMargin = width * 5 / 100;
        //mToolBarLayoutParama.rightMargin = width * 2 / 100;
        mToolbarIconLayout.setLayoutParams(mToolBarLayoutParama);
        mToolbarIconLayout.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams mLeftMenuParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mLeftMenuParams.width = (int) width * 8 / 100;
        mLeftMenuParams.height = (int) width * 8 / 100;
        mLeftMenuParams.gravity = Gravity.CENTER | Gravity.LEFT;
        mLeftSliderMenu_Img.setLayoutParams(mLeftMenuParams);

        LinearLayout.LayoutParams mLeftLogoLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mLeftLogoLayoutParams.width = (int) width * 76 / 100;
        mLeftLogoLayoutParams.height = (int) width * 8 / 100;
        mLeftLogoLayoutParams.gravity = Gravity.CENTER | Gravity.LEFT;
        mSliderLogoLayout.setLayoutParams(mLeftLogoLayoutParams);
        mSliderLogoLayout.setGravity(Gravity.CENTER | Gravity.LEFT);

        LinearLayout.LayoutParams mLeftLogoImageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mLeftLogoImageParams.width = (int) width * 8 / 100;
        mLeftLogoImageParams.height = (int) width * 8 / 100;
        mLeftLogoImageParams.gravity = Gravity.CENTER;
        mSliderLogo_Img.setLayoutParams(mLeftLogoImageParams);
        mSliderLogo_Img.setVisibility(View.GONE);

        LinearLayout.LayoutParams mLeftLogoParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mLeftLogoParams.width = (int) width * 40 / 100;
        mLeftLogoParams.height = (int) height * 6 / 100;
        mLeftLogoParams.leftMargin = (int) (width * 28 / 100);
        mLeftLogoParams.gravity = Gravity.CENTER;
        mLeftSliderLogo_Img.setLayoutParams(mLeftLogoParams);

        LinearLayout.LayoutParams mRightMenuParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mRightMenuParams.width = (int) width * 8 / 100;
        mRightMenuParams.height = (int) width * 6 / 100;
        mRightMenuParams.leftMargin = (int) width * 2 / 100;
        mRightMenuParams.gravity = Gravity.CENTER | Gravity.RIGHT;
        mRightSliderMenu_Search.setLayoutParams(mRightMenuParams);
        mRightSliderMenu_Add.setLayoutParams(mRightMenuParams);
        progressBarRefresh.setLayoutParams(mRightMenuParams);

        CoordinatorLayout.LayoutParams floatingParams = new CoordinatorLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        floatingParams.bottomMargin = (int) width * 4 / 100;
        // floatingParams.topMargin=height*10/100;
        floatingParams.rightMargin = (int) (width * 2.5 / 100);
        floatingParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        mFloatingActionBtn.setLayoutParams(floatingParams);
        mFloatingActionBtnGroupAdd.setLayoutParams(floatingParams);

        CoordinatorLayout.LayoutParams floatingRefresfParams = new CoordinatorLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        floatingRefresfParams.bottomMargin = (int) width * 4 / 100;
        // floatingRefresfParams.topMargin=height*10/100;
        floatingRefresfParams.rightMargin = (int) (width * 3 / 100);
        floatingRefresfParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        mFloatingActionBtnRefresh.setLayoutParams(floatingRefresfParams);

        LinearLayout.LayoutParams pagerLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        pagerLayoutParama.leftMargin = (int) width;
        pagerLayoutParama.rightMargin = (int) height * 73 / 100;


        LinearLayout.LayoutParams mViewPagerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        viewPager.setLayoutParams(mViewPagerParams);

        FrameLayout.LayoutParams leftLayoutParama = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        leftLayoutParama.width = width * 5 / 100;
        leftLayoutParama.gravity = Gravity.LEFT | Gravity.BOTTOM;
        mSliderLeft.setLayoutParams(leftLayoutParama);

        FrameLayout.LayoutParams rightLayoutParama = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        rightLayoutParama.width = (int) (width * 1 / 100);
        rightLayoutParama.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        mSliderRight.setLayoutParams(rightLayoutParama);

        LinearLayout.LayoutParams bottomViewParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomViewParama.height = (int) (height * 0.7 / 100);
        bottomViewParama.gravity = Gravity.CENTER;
        mViewBottom.setLayoutParams(bottomViewParama);

        LinearLayout.LayoutParams mBottomLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomLayoutParama.width = width;
        mBottomLayoutParama.height = height * 9 / 100;
        mBottomLayout.setLayoutParams(mBottomLayoutParama);

        LinearLayout.LayoutParams mBottomChatLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomChatLayoutParama.height = height * 9 / 100;
        mBottomChatLayoutParama.weight = 1;
        mBottomChatLayout.setLayoutParams(mBottomChatLayoutParama);
        mBottomCartLayout.setLayoutParams(mBottomChatLayoutParama);
        mBottomBuxsLayout.setLayoutParams(mBottomChatLayoutParama);

        LinearLayout.LayoutParams mBottomImageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomImageParama.width = (int) width * 10 / 100;
        mBottomImageParama.height = (int) height * 4 / 100;
        mBottomImageParama.topMargin = height * 1 / 100;
        mBottomImageParama.bottomMargin = (int) (height * 0.5 / 100);
        mBottomImageParama.gravity = Gravity.CENTER | Gravity.TOP;
        mBottomChatImage.setLayoutParams(mBottomImageParama);
        mBottomBuxsImage.setLayoutParams(mBottomImageParama);
        mBottomCartImage.setLayoutParams(mBottomImageParama);

        LinearLayout.LayoutParams mBottomTextParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomTextParama.gravity = Gravity.CENTER | Gravity.TOP;
        mBottomChatText.setLayoutParams(mBottomTextParama);
        mBottomBuxsText.setLayoutParams(mBottomTextParama);
        mBottomCartText.setLayoutParams(mBottomTextParama);
        mBottomChatText.setGravity(Gravity.CENTER | Gravity.TOP);
        mBottomBuxsText.setGravity(Gravity.CENTER | Gravity.TOP);
        mBottomCartText.setGravity(Gravity.CENTER | Gravity.TOP);

        if (width >= 600) {
            chatc.setTextSize(14);
            groupc.setTextSize(14);
            contactc.setTextSize(14);
            mBottomChatText.setTextSize(16);
            mBottomBuxsText.setTextSize(16);
            mBottomCartText.setTextSize(16);
            mForwardText.setTextSize(17);
        } else if (width > 501 && width < 600) {
            chatc.setTextSize(13);
            groupc.setTextSize(13);
            contactc.setTextSize(13);
            mBottomChatText.setTextSize(15);
            mBottomBuxsText.setTextSize(15);
            mBottomCartText.setTextSize(16);
            mForwardText.setTextSize(15);
        } else if (width > 260 && width < 500) {
            chatc.setTextSize(12);
            groupc.setTextSize(12);
            contactc.setTextSize(12);
            mBottomChatText.setTextSize(14);
            mBottomBuxsText.setTextSize(14);
            mBottomCartText.setTextSize(14);
            mForwardText.setTextSize(15);
        } else if (width <= 260) {
            chatc.setTextSize(11);
            groupc.setTextSize(11);
            contactc.setTextSize(11);
            mBottomChatText.setTextSize(13);
            mBottomBuxsText.setTextSize(13);
            mBottomCartText.setTextSize(13);
            mForwardText.setTextSize(14);
        }
    }

    public void mFinishBackgroundAcrtivity() {

        try {
            if (BannerActivity.mActivity != null)
                BannerActivity.mActivity.finish();
            if (BannerActivityChat.mActivity != null)
                BannerActivityChat.mActivity.finish();
            if (BannerActivityDazzAdapter.mActivity != null)
                BannerActivityDazzAdapter.mActivity.finish();
            if (BannerActivityLED.mActivity != null)
                BannerActivityLED.mActivity.finish();
            if (BannerActivityZzleAdapter.mActivity != null)
                BannerActivityZzleAdapter.mActivity.finish();
            if (BannerPreview.mActivity != null)
                BannerPreview.mActivity.finish();
            if (DazzPlainActivity.mActivity != null)
                DazzPlainActivity.mActivity.finish();
            if (DazzTabActivity.mActivity != null)
                DazzTabActivity.mActivity.finish();
            if (KaraokeActivity.mActivity != null)
                KaraokeActivity.mActivity.finish();
            if (KaraokeActivityGroup.mActivity != null)
                KaraokeActivityGroup.mActivity.finish();
            if (KaraokeListActivity.mActivity != null)
                KaraokeListActivity.mActivity.finish();
            if (KaraokeMainActivity.mActivity != null)
                KaraokeMainActivity.mActivity.finish();
            if (KaraokeMainGroup.mActivity != null)
                KaraokeMainGroup.mActivity.finish();
            if (KaraokeTab.mActivity != null)
                KaraokeTab.mActivity.finish();
            if (KaraokeYoutubeActivity.mActivity != null)
                KaraokeYoutubeActivity.mActivity.finish();
            if (SmackThatActivity.mActivity != null)
                SmackThatActivity.mActivity.finish();
            if (KonsActivity.mActivity != null)
                KonsActivity.mActivity.finish();
            if (KonsHomeScreen.mActivity != null)
                KonsHomeScreen.mActivity.finish();
            if (MultipleNynmActivity.mActivity != null)
                MultipleNynmActivity.mActivity.finish();
            if (NynmActivity.mActivity != null)
                NynmActivity.mActivity.finish();
            if (NynmAddActivity.mActivity != null)
                NynmAddActivity.mActivity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1002:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Constant.printMsg("Permission Granted");
                } else {
                    Toast.makeText(getApplicationContext(), "Allow Permission to Access", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class logoutTheUser extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SliderTesting.this,
                    AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setProgressDrawable(new ColorDrawable(
                    android.graphics.Color.BLUE));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null, postData;
            postData = postLogoutData();
            HttpConfig httpConfig = new HttpConfig();
            result = httpConfig.doPostMobizee(postData, KachingMeConfig.POST_LOGOUT);
            Constant.printMsg("SliderTesting Logout post data......" + postData);
            Constant.printMsg("SliderTesting Logout url......" + KachingMeConfig.POST_LOGOUT + "....." + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && result.length() > 0) {
                if (result.trim().equalsIgnoreCase("SUCCESS")) {
                    KachingMeApplication.getsharedpreferences_Editor().remove("wallpaper").commit();
                    Constant.freelistmain.clear();
                    Constant.printMsg("deleted db::beforeeeeee");
                    ed.remove("pin");
                    ed.remove("sec_count");
                    ed.clear();
                    ed.commit();

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SliderTesting.this);
                    Editor editorClear = sp.edit();
                    editorClear.clear();
                    editorClear.commit();
                    Editor editor = sp.edit();
                    editor.putString("activity_name", "");
                    editor.putBoolean("decline", false);
                    editor.putInt("nofreebie", 0);
                    editor.commit();
                    SharedPreferences sps = SliderTesting.this.getSharedPreferences(KachingMeApplication.getPereference_label(),
                            Activity.MODE_PRIVATE);
                    Editor editor1 = sps.edit();
                    editor1.putString("activity_name", "");
                    editor1.putBoolean("decline", false);
                    editor1.putInt("nofreebie", 0);
                    editor1.commit();
                    dbadapter.dumpDatabase();
                    db.dumpDatabase();
                    Constant.login = false;
                    Constant.emptyFreebie = false;
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Presence pr = new Presence(Presence.Type.unavailable);
                                TempConnectionService.connection.sendStanza(pr);
                                TempConnectionService.connection.disconnect();
                            } catch (Exception e) {

                            }
                        }
                    });
                    thread.start();
                    deleteInternalAndExternalStorage();
                    resideMenu.closeMenu();
                    commonMethods.postEarnedBux("logout");
                    Intent intent = new Intent(SliderTesting.this, com.wifin.kachingme.registration_and_login.MainActivity.class);
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Logout Failed.!Please try again later!",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error.!Please try again later!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void updateHedder(){
        try {
            tabLayout = (TabLayout) mActivity.findViewById(R.id.home_tab_layout);
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            tabLayout.setupWithViewPager(viewPager);

            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                tab.setCustomView(adapter.getTabView(i));
            }
        } catch (Exception e) {

        }
    }
}
