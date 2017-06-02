package com.wifin.kachingme.applications;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.multidex.MultiDex;

import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.Log;

//import org.acra.ACRA;

import java.util.ArrayList;
import java.util.List;

//@ReportsCrashes(formUri = "https://mahesh.cloudant.com/acra-niftychat/_design/acra-storage/_update/report", reportType = HttpSender.Type.JSON, httpMethod = HttpSender.Method.POST, formUriBasicAuthLogin = "sorseparkndidedingengtom", formUriBasicAuthPassword = "JqePy0XCT7UhhfkvGqbcLic0",
//// formKey = "", // This is required for backward compatibility but not used
//        customReportContent = {ReportField.APP_VERSION_CODE,
//                ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION,
//                ReportField.PACKAGE_NAME, ReportField.REPORT_ID, ReportField.BUILD,
//                ReportField.STACK_TRACE, ReportField.USER_EMAIL, ReportField.USER_IP,
//                ReportField.AVAILABLE_MEM_SIZE, ReportField.TOTAL_MEM_SIZE,
//                ReportField.CUSTOM_DATA
//
//        }
//
//)
public class KachingMeApplication extends Application {

    public static DatabaseHelper dbadapter;
    public static String userID;
    public static String app_pereferences = "niftychat_pereferences";
    public static String host = "@"+ KachingMeConfig.CHAT_HOST;
    public static String host_12 = KachingMeConfig.CHAT_HOST;
    public static String Nifty_name = "", Nifty_email = "", Status = "";
    // public static String host="@niftytest.com";

    // public static String host="@kaching.me";
    // public static String host_12="kaching.me";
    public static int SELECTED_TAB = 0;
    public static ArrayList<String> Blocked_user = new ArrayList<String>();
    public static Boolean IS_FROM_NOTIFICATION = false;
    public static String PACKAGE_NAME;
    public static String PROFILE_PIC_DIR;
    public static SharedPreferences sp;
    public static Editor editor;
    public static Boolean isNetAvailable = false;
    public static byte[] avatar = null;
    private boolean inForeground = true;
    private int resumed = 0;
    private int paused = 0;

    public static String getPROFILE_PIC_DIR() {
        return PROFILE_PIC_DIR;
    }

    public static void setPROFILE_PIC_DIR(String pACKAGE_NAME) {
        PROFILE_PIC_DIR = pACKAGE_NAME;
    }

    public static Boolean getIS_FROM_NOTIFICATION() {
        return IS_FROM_NOTIFICATION;
    }

    public static void setIS_FROM_NOTIFICATION(Boolean iS_FROM_NOTIFICATION) {
        IS_FROM_NOTIFICATION = iS_FROM_NOTIFICATION;
    }

    public static Boolean getIsNetAvailable() {
        return isNetAvailable;
    }

    public static void setIsNetAvailable(Boolean isNetAvailable) {
        KachingMeApplication.isNetAvailable = isNetAvailable;
    }

    public static ArrayList<String> getBlocked_user() {
        return Blocked_user;
    }

    public static void setBlocked_user(ArrayList<String> blocked_user) {
        Blocked_user = blocked_user;
    }

    public static String getStatus() {
        return Status;
    }

    public static void setStatus(String status) {
        Status = status;
    }

    public static byte[] getAvatar() {
        return avatar;
    }

    public static void setAvatar(byte[] avatar) {
        KachingMeApplication.avatar = avatar;
    }

    public static String getNifty_email() {
        return Nifty_email;
    }

    public static void setNifty_email(String nifty_email) {
        Nifty_email = nifty_email;
    }

    public static String getjid() {
        return getUserID() + getHost();
    }

    public static String getNifty_name() {
        return Nifty_name;
    }

    public static void setNifty_name(String nifty_name) {
        Nifty_name = nifty_name;
    }

    public static DatabaseHelper getDatabaseAdapter() {
        return dbadapter;
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userid) {
        userID = userid;
    }

    public static String getPereference_label() {
        return app_pereferences;
    }

    public static String getHost() {
        return host;
    }

    public static String getHost_12() {
        return host_12;
    }

    public static void setAcra() {
        // ACRA.getErrorReporter().putCustomData("JID",getjid());
    }

    public static SharedPreferences getsharedpreferences() {
        return sp;
    }

    public static Editor getsharedpreferences_Editor() {
        return editor;
    }

    // Used after deleting the account...in slidershow activity
    public void reCreate() {
        Constant.printMsg("VVVVVVVVVVV KachingMeApplication inside : "
                + Constant.isNiftyApplicationRunning);
//        ACRA.init(this);

        try {

            PACKAGE_NAME = this.getApplicationContext().getPackageName();
            PROFILE_PIC_DIR = "/data/data/"
                    + this.getApplicationContext().getPackageName()
                    + "/profile_pic/";

            DatabaseHelper.version_val = DatabaseHelper.version_val + 1;

            dbadapter = DatabaseHelper.getDBAdapterInstance(this);
            dbadapter.openDataBase();

            // if (dbadapter.getLogin() != null && dbadapter.getLogin().size() > 0)
            // {
            //
            // ACRA.getErrorReporter().putCustomData(
            // "JID",
            // dbadapter.getLogin().get(0).getUserName()
            // + KachingMeApplication.getHost());
            // }

            DatabaseHelper dbadapter = KachingMeApplication.getDatabaseAdapter();
            if (!isMyServiceRunning(TempConnectionService.class)) {
                Constant.printMsg("DDDDDDDDDDDD" + "Nifty");

                 startService(new Intent(this, TempConnectionService.class));

            } else {
                Log.d("Service", "Service already runnnig...");
            }

            sp = getSharedPreferences(app_pereferences, Activity.MODE_PRIVATE);
            editor = sp.edit();

        } catch (Exception e) {

        }


    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        SSLCertificateHandler.nuke();
//        ACRA.init(this);

        PACKAGE_NAME = this.getApplicationContext().getPackageName();
        PROFILE_PIC_DIR = "/data/data/"
                + this.getApplicationContext().getPackageName()
                + "/profile_pic/";


        dbadapter = DatabaseHelper.getDBAdapterInstance(this);
        dbadapter.openDataBase();

        // if (dbadapter.getLogin() != null && dbadapter.getLogin().size() > 0)
        // {
        //
        // ACRA.getErrorReporter().putCustomData(
        // "JID",
        // dbadapter.getLogin().get(0).getUserName()
        // + KachingMeApplication.getHost());
        // }
        Constant.printMsg("DDDDDDDDDDDD" + "CALLED NIFTY" + "     " + isMyServiceRunning(TempConnectionService.class) + "     " + TempConnectionService.connection);
        DatabaseHelper dbadapter = KachingMeApplication.getDatabaseAdapter();
        if (!isMyServiceRunning(TempConnectionService.class) || TempConnectionService.connection == null) {
            Constant.printMsg("DDDDDDDDDDDD" + "Nifty");

            startService(new Intent(this, TempConnectionService.class));

        } else {
            Log.d("Service", "Service already runnnig...");
        }

        sp = getSharedPreferences(app_pereferences, Activity.MODE_PRIVATE);


        editor = sp.edit();

        String string = sp.getString(Constant.BLOCKED_USERS, "");
        String[] parts = string.split("-");

        ArrayList mBlockedList = new ArrayList();
        for (int i = 0; i < parts.length; i++) {
            mBlockedList.add(parts[i].trim());
        }
        KachingMeApplication.setBlocked_user(mBlockedList);

        // init();
    }

    // KaChingMeService mBoundService;
    // Boolean isBound;
    // public AbstractXMPPConnection connection;
    // /* private ChatManager chatManager; */
    // private ServiceConnection mConnection = new ServiceConnection() {
    // public void onServiceConnected(ComponentName className, IBinder service)
    // {
    //
    // mBoundService = ((KaChingMeService.LocalBinder) service)
    // .getService();
    // connection = mBoundService.getConnection();
    //
    // }
    //
    // public void onServiceDisconnected(ComponentName className) {
    //
    // mBoundService = null;
    // }
    // };

    // void doBindService() {
    //
    // bindService(new Intent(KachingMeApplication.this, KaChingMeService.class),
    // mConnection, Context.BIND_AUTO_CREATE);
    // isBound = true;
    // }
    //
    // void doUnbindService() {
    // if (isBound) {
    //
    // unbindService(mConnection);
    // isBound = false;
    // }
    // }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /*
     * private void init() { MyVolley.init(this); }
     */
    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();

        // Constant.printMsg("Application Terminated..");
        dbadapter.close();
    }

    public void onActivityResumed(Activity activity) {
        ++resumed;

        if (!inForeground) {
            // Don't check for foreground or background right away
            // finishing an activity and starting a new one will trigger to many
            // foreground <---> background switches
            //
            // In half a second call foregroundOrBackground
            Log.d("", "Not foregroud");
        }
    }

    public void onActivityPaused(Activity activity) {
        ++paused;

        if (inForeground) {
            // Don't check for foreground or background right away
            // finishing an activity and starting a new one will trigger to many
            // foreground <---> background switches
            //
            // In half a second call foregroundOrBackground
            Log.d("", "Is foregroud");
        }
    }

    public Boolean foregroundOrBackground() {
        if (paused >= resumed && inForeground) {
            inForeground = false;
        } else if (resumed > paused && !inForeground) {
            inForeground = true;
        }

        return inForeground;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public boolean isApplicationBroughtToBackground() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(getPackageName())) {
                return true;
            }
        }

        return false;
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            //SCREEN IS NOT SHOWING

            Constant.printMsg("AAAAAAAAA1");
        }
        if (level == ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            //SCREEN IS NOT SHOWING

            Constant.printMsg("AAAAAAAAA2");
        }
        if (level == ComponentCallbacks2.TRIM_MEMORY_COMPLETE) {
            //SCREEN IS NOT SHOWING

            Constant.printMsg("AAAAAAAAA3");
        }
        if (level == ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            //SCREEN IS NOT SHOWING

            Constant.printMsg("AAAAAAAAA4");
        }
        if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL) {
            //SCREEN IS NOT SHOWING

            Constant.printMsg("AAAAAAAAA5");
        }
        if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) {
            //SCREEN IS NOT SHOWING

            Constant.printMsg("AAAAAAAAA6");
        }
        if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE) {
            //SCREEN IS NOT SHOWING

            Constant.printMsg("AAAAAAAAA7");
        }
    }
}
