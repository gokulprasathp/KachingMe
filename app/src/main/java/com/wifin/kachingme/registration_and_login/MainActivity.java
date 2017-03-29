/*
* @author Sivanesan
*
* @usage -  This class as Luncher Activity
*
* */

package com.wifin.kachingme.registration_and_login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.SQLException;
import android.net.ParseException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.CartAdapDto;
import com.wifin.kachingme.pojo.DonationDto;
import com.wifin.kachingme.pojo.FreebieDto;
import com.wifin.kachingme.pojo.FreebieMainDto;
import com.wifin.kachingme.pojo.NymsPojo;
import com.wifin.kachingme.pojo.RedeemDto;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.Constant;

import org.jivesoftware.smack.AbstractXMPPConnection;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Wifintech on 12-Sep-16.
 */
public class MainActivity extends BaseActivity {

    Boolean isBound, freedecline = false;
    SharedPreferences sp, sharedPreferences;
    SharedPreferences.Editor editor;
    int width, height;
    Dbhelper db;
    String phone, code;
    String mFreebieLastDay = "";
    Date mFreebieLastDayDate, mTodayDate;
    int mFreebieCount = 0;
    int mFreeBie = 0;
    String actvity;
    DatabaseHelper dbadapter;
    private KachingMeApplication globalApplication;

    public static void deleteCache(Context context) {
        Constant.printMsg("file diR :::: >>>>> ");
        try {
            File dir = context.getCacheDir();
            Constant.printMsg("file diR :::: >>>>> " + dir);
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
                Constant.printMsg("deleted ::::; ");
            }
        } catch (Exception e) {
            Constant.printMsg("deleted ::::; " + e.toString());
        }
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
            return dir.delete();
        }
        return dir.delete();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(null);
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        globalApplication = (KachingMeApplication) getApplication();
        deleteCache(this);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        // sp = getSharedPreferences(KachingMeApplication.getPereference_label(),
        // Activity.MODE_PRIVATE);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        editor = sp.edit();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        Constant.screenHeight = height;
        Constant.screenWidth = width;
        dbadapter = KachingMeApplication.getDatabaseAdapter();
        db = new Dbhelper(getApplicationContext());
        Constant.blinkoffer.clear();
        Constant.blinkPos.clear();
        Constant.addedNyms.clear();
        Constant.NewNyms.clear();
        Constant.mDictionaryList.clear();
        Constant.mDictionaryMeaningList.clear();
        Constant.printMsg("login List Size ::> >> "
                + sp.getInt("nofreebie", 0));
        mFreeBie = sp.getInt("nofreebie", 0);
        actvity = sp.getString("activity_name", "");
        freedecline = sp.getBoolean("decline", false);
        Constant.printMsg("mainacitivity free decline in main activit is::" + freedecline);
        Constant.printMsg("mainactivity status ::>> >>> " + actvity);
        /*calling here three class based on freebe and login session
        Class as StartUpDazz,SliderTesting,FreebieActivity,AddToCart*/
        try {
            String regId = sharedPreferences.getString("regId", null);
            Constant.printMsg("Firebasr Register Id........" + regId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        redirectActivity();
    }

    private void redirectActivity() {
        if (actvity.length() == 0 || actvity.equalsIgnoreCase("SliderTesting")) {
            Constant.printMsg("mainactivity called main:::::::::asaSas...." + dbadapter.getLogin().size() + "     " + mFreeBie);
            if (dbadapter.getLogin().size() == 0) {
                /*User not login yet */
                Constant.printMsg("mainactivity called main:::::::::");
                if (mFreeBie == 1) {
                    fetchFrom();
                    fetchNymFrom();
                    fetchDonate();
                    fetchFreebie();
                    FetchRet();
                    FetchPreference();
                    Constant.freebie = true;
                    Constant.printMsg("User ID::"
                            + dbadapter.getLogin().get(0).getUserName());
                    KachingMeApplication.setUserID(dbadapter.getLogin().get(0)
                            .getUserName());
                    KachingMeApplication.setNifty_name(dbadapter.getLogin().get(0)
                            .getNifty_name());
                    KachingMeApplication.setNifty_email(dbadapter.getLogin().get(0)
                            .getNifty_email());
                    KachingMeApplication.setAvatar(dbadapter.getLogin().get(0)
                            .getAvatar());
                    KachingMeApplication.setStatus(dbadapter.getLogin().get(0)
                            .getStatus());
                    KachingMeApplication.setAcra();
                    startActivity(new Intent(this, StartUpDazz.class));
                    finish();

                } else {
//					Constant.printMsg("called main1:::::::::");
//					DbDelete();
//					DbDeleteDeel();
//					DbDeleteNym();
//					DbDeleteBux();
//					DbDeleteDonation();
//					DbDeleteZZle();
//					DbDeleteWish();
//					DbDeleteFreeBie();
//					DbDeleteMer();
//					DbdeletePrimary();
                    // Intent intent = new Intent(RegistrationMainActivity.this,
                    // SocialActivity.class);
                    startActivity(new Intent(MainActivity.this, Slideshow.class));
                    finish();
                }
            } else if (freedecline) {
                Constant.printMsg("mainactivity called main1:::::::::");
                Intent intent = new Intent(MainActivity.this, StartUpDazz.class);
                startActivity(intent);
                finish();
            } else {
                Constant.printMsg("mainactivity called:::::::::else check  proper login.." + sharedPreferences.getString("loginSucess", ""));
                if (sharedPreferences.getString("loginSucess", "").equalsIgnoreCase("failure")) {
                    Constant.printMsg("mainactivity called:::::::::else not proper login");
                    dbadapter.deletelogin();
                    //db.dumpDatabase();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("loginSucess", "Success");
                    editor.commit();
                    startActivity(new Intent(MainActivity.this, Slideshow.class));
                    finish();
                } else {
                    Constant.printMsg("mainactivity called:::::::::else");
                    fetchFrom();
                    fetchNymFrom();
                    fetchDonate();
                    fetchFreebie();
                    FetchRet();
                    FetchPreference();
                    Constant.freebie = true;

                    // Constant("User ID::"+dbadapter.getLogin().get(0).getUserName());
                    KachingMeApplication.setUserID(dbadapter.getLogin().get(0)
                            .getUserName());
                    KachingMeApplication.setNifty_name(dbadapter.getLogin().get(0)
                            .getNifty_name());
                    KachingMeApplication.setNifty_email(dbadapter.getLogin().get(0)
                            .getNifty_email());
                    KachingMeApplication.setAvatar(dbadapter.getLogin().get(0)
                            .getAvatar());
                    KachingMeApplication.setStatus(dbadapter.getLogin().get(0)
                            .getStatus());
                    KachingMeApplication.setAcra();
                    int currentApiVersion = android.os.Build.VERSION.SDK_INT;

                    try {
                        if (currentApiVersion >= 17) {
                            if (Global.getInt(getContentResolver(),
                                    Global.AUTO_TIME) == 1) {
                            } else {
                                autoTimeEnable();
                            }
                        } else {
                            if (Settings.System.getInt(getContentResolver(),
                                    Settings.System.AUTO_TIME) == 1) {
                            } else {
                                autoTimeEnable();
                            }
                        }
                    } catch (SettingNotFoundException e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }

                    Calendar c = Calendar.getInstance();
                    Constant.printMsg("mainactivity Current time => " + c.getTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
                    String currentDateandTime = sdf.format(c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDate = df.format(c.getTime());

                    String query = "select date  from " + Dbhelper.TABLE_FREEBIE;
                    callFreebieDB(query);

                    if (mFreebieCount != 0) {
                        mTodayDate = new Date();
                        try {
                            try {
                                mTodayDate = df.parse(formattedDate);
                            } catch (java.text.ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        mFreebieLastDayDate = new Date();
                        try {
                            Constant.printMsg("fffffffffffffffffffffffffff"
                                    + mFreebieLastDay);
                            mFreebieLastDayDate = df.parse(mFreebieLastDay);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (java.text.ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Constant.printMsg("last::::" + mFreebieLastDayDate
                                + "   " + mTodayDate + "  SPPP "
                                + sp.getString("freebie_dazz_date", ""));
                        String var = sp.getString("freebie_dazz_date", "");
                        if (var.length() > 0) {
                            if (sp.getString("freebie_dazz_date", "") != formattedDate) {
                                Constant.printMsg("last::::11111"
                                        + mFreebieLastDayDate + "   " + mTodayDate
                                        + "  SPPP "
                                        + sp.getString("freebie_dazz_date", ""));
                                if (!mTodayDate.after(mFreebieLastDayDate)) {

                                    startActivity(new Intent(this, SliderTesting.class));
                                    finish();
                                } else {
                                    if (Double.valueOf(currentDateandTime) > 1200) {
                                        editor = sp.edit();
                                        // editor.putBoolean("freebie_status",
                                        // true);
                                        editor.putString("freebie_dazz_date",
                                                formattedDate);
                                        editor.commit();

                                        Constant.mFrebieScroll = true;
                                        startActivity(new Intent(this, StartUpDazz.class));
                                        finish();
                                    } else {
                                        startActivity(new Intent(this, SliderTesting.class));
                                        finish();
                                    }
                                }
                            } else {
                                startActivity(new Intent(this, SliderTesting.class));
                                finish();

                            }
                        } else {
                            Constant.printMsg("FASTTT   " + formattedDate);
                            editor = sp.edit();
                            // editor.putBoolean("freebie_status", true);
                            editor.putString("freebie_dazz_date", formattedDate);
                            editor.commit();
                            Constant.mFrebieScroll = true;
                            startActivity(new Intent(this, SliderTesting.class));
                            finish();
                        }
                    } else {
                        startActivity(new Intent(this, SliderTesting.class));
                        finish();
                    }
                }
            }
        } else if (freedecline) {
            Constant.printMsg("mainactivity called freedecline:::::::::" + freedecline);
            startActivity(new Intent(MainActivity.this, SliderTesting.class));
            finish();
        } else {
            if (actvity.equalsIgnoreCase("FreebieActivity")) {
                fetchFreebie();
                Constant.printMsg("mainactivity sizeeeeeee ::: >>> "
                        + Constant.freelistmain.size());
                startActivity(new Intent(this, FreebieActivity.class));
                finish();
            } else if (actvity.equalsIgnoreCase("AddToCart")) {
                fetchFreebie();
                Constant.printMsg("mainactivity sizeeeeeee ::: >>> "
                        + Constant.freelistmain.size());
                startActivity(new Intent(this, AddToCart.class));
                finish();
            } else if (actvity.equalsIgnoreCase("WelcomeActivity")) {
                fetchFreebie();
                Constant.printMsg("mainactivity sizeeeeeee ::: >>> "
                        + Constant.freelistmain.size());
                startActivity(new Intent(this, WelcomeActivity.class));
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        globalApplication.onActivityResumed(this);
        Constant.printMsg("IS Resume base::" + globalApplication.isApplicationBroughtToBackground());
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public AbstractXMPPConnection getServiceConnection() {
        return TempConnectionService.connection;
    }
    public void DbDelete() {
        // TODO Auto-generated method stub
        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_BUX, null, null);
            System.out
                    .println("No of deleted rows from bux data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeleteDeel() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_CART, null, null);
            System.out
                    .println("No of deleted rows from deel data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeleteNym() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_NYM, null, null);
            System.out
                    .println("No of deleted rows from deel data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeleteBux() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_BUX, null, null);
            System.out
                    .println("No of deleted rows from deel data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeleteZZle() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_ZZLE, null, null);
            System.out
                    .println("No of deleted rows from deel data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeleteWish() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_WISH, null, null);
            System.out
                    .println("No of deleted rows from deel data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeleteFreeBie() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_FREEBIE, null, null);
            System.out
                    .println("No of deleted rows from freebie data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeleteMer() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_RET, null, null);
            System.out
                    .println("No of deleted rows from retmetr data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbdeletePrimary() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_PRIMENUMBER, null, null);
            System.out
                    .println("No of deleted rows from primary data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular primary for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void DbDeleteDonation() {
        // TODO Auto-generated method stub

        try {
            int a = db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_DONATE, null, null);
            System.out
                    .println("No of deleted rows from deel data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public void fetchFrom() {
        Constant.cartfinal.clear();

        String buxno, deelid, discountno, itemname, mercid, off, phto, qr, prname, ty, mn, merchant_img, mphone, mcompany, validity;

        Dbhelper db = new Dbhelper(getApplicationContext());
        Cursor c = null;
        try {
            c = db.open()
                    .getDatabaseObj()
                    .query(Dbhelper.TABLE_CART, null, null, null, null, null,
                            null);
            int descr = c.getColumnIndex("desc");
            int phnum = c.getColumnIndex("phonenumber");
            int dellid = c.getColumnIndex("deelid");
            int merchantid = c.getColumnIndex("merchantid");
            int prodname = c.getColumnIndex("prodname");
            int item = c.getColumnIndex("item");
            int offer = c.getColumnIndex("offer");
            int discount = c.getColumnIndex("discount");
            int type = c.getColumnIndex("type");
            int bux = c.getColumnIndex("bux");
            int photopath = c.getColumnIndex("photopath");
            int qrpath = c.getColumnIndex("qrpath");
            int merchantnm = c.getColumnIndex("merchantname");
            int merchantimgpath = c.getColumnIndex("merchantimagepath");
            int company = c.getColumnIndex("companyname");
            int validity_index = c.getColumnIndex("validity");
            Constant.printMsg("The pending cart list in db ::::"
                    + c.getCount());
            if (c.getCount() > 0) {

                while (c.moveToNext()) {
                    buxno = c.getString(bux);
                    deelid = c.getString(dellid);
                    discountno = String.valueOf(c.getInt(discount));
                    itemname = c.getString(item);
                    mercid = String.valueOf(c.getInt(merchantid));
                    off = c.getString(offer);
                    phto = c.getString(photopath);
                    qr = c.getString(qrpath);
                    prname = c.getString(prodname);
                    ty = String.valueOf(c.getInt(type));
                    mn = c.getString(merchantnm);
                    merchant_img = c.getString(merchantimgpath);
                    mphone = c.getString(phnum);
                    mcompany = c.getString(company);
                    validity = c.getString(validity_index);

                    Constant.printMsg("dbadd::" + buxno + "  " + deelid + "  "
                            + discountno + "    " + itemname + "     " + mercid
                            + "     " + off + "     " + phto + "      " + qr
                            + "      " + ty + " merimg " + merchant_img);

                    CartAdapDto p = new CartAdapDto();
                    p.setBuxs(buxno);
                    p.setDeelOfferDiscountId(deelid);
                    p.setDiscount(discountno);
                    p.setItem(itemname);
                    p.setMerchantId(mercid);
                    p.setOffer(off);
                    p.setPhotoPath(phto);
                    p.setProductName(prname);
                    p.setQrCodePath(qr);
                    p.setType(ty);
                    p.setMerchantName(mn);
                    p.setMerphotoPath(merchant_img);
                    p.setCompanyname(mcompany);
                    p.setValidity(validity);
                    Constant.cartfinal.add(p);
                    Constant.printMsg("nummm :::::>>>>>>>"
                            + mphone.toString().substring(2) + "company  "
                            + mcompany);
                    Constant.mPhoneNum = mphone;
                    // Constant.mPhoneNum = mphone.toString().substring(2);
                    Constant.mCartType = ty;
                }

            }
        } catch (SQLException e) {

            Constant.printMsg("Sql exception in pending shop details ::::"
                    + e.toString());
        } finally {
            c.close();
            db.close();
        }
    }

    public void fetchNymFrom() {

        Constant.addedNyms.clear();

        String tx, mn;

        Dbhelper db = new Dbhelper(getApplicationContext());
        Cursor c = null;
        try {
            c = db.open()
                    .getDatabaseObj()
                    .query(Dbhelper.TABLE_NYM, null, null, null, null, null,
                            null);
            int txnm = c.getColumnIndex("name");
            int mnnm = c.getColumnIndex("meaning");

            Constant.printMsg("The pending cart list in db ::::"
                    + c.getCount());

            if (c.getCount() > 0) {

                while (c.moveToNext()) {

                    tx = c.getString(txnm);
                    mn = c.getString(mnnm);

                    Constant.printMsg("dbadd:nym:" + tx + "  " + mn + "  ");

                    NymsPojo p = new NymsPojo();
                    p.setMeaning(mn);
                    p.setText(tx);

                    Constant.addedNyms.add(p);
                }
            }
        } catch (SQLException e) {

            Constant.printMsg("Sql exception in pending shop details ::::"
                    + e.toString());
        } finally {
            c.close();
            db.close();
        }
        SetList();
    }

    public void SetList() {
        // TODO Auto-generated method stub
        // Added by Chinmay
        Constant.mDictionaryList.clear();
        Constant.mDictionaryMeaningList.clear();
        // Added by Chinmay
        if (Constant.addedNyms.size() > 0) {

            for (int i = 0; i < Constant.addedNyms.size(); i++) {

                LinkedHashSet hs = new LinkedHashSet();

                hs.addAll(Constant.mDictionaryList);
                Constant.mDictionaryList.clear();
                Constant.mDictionaryList.addAll(hs);

                LinkedHashSet hs1 = new LinkedHashSet();
                hs1.addAll(Constant.mDictionaryMeaningList);
                Constant.mDictionaryMeaningList.clear();
                Constant.mDictionaryMeaningList.addAll(hs1);

                Constant.printMsg("nyms::::>>>" + Constant.addedNyms.size());
                Constant.printMsg("nyms constant::>>>>>"
                        + Constant.mDictionaryList + "  "
                        + Constant.mDictionaryMeaningList);

                String ny = Constant.addedNyms.get(i).getText().toString();
                String nymm = Constant.addedNyms.get(i).getMeaning().toString();

                if (Constant.mDictionaryList.size() > 0) {

                    Constant.printMsg("List Size"
                            + Constant.mDictionaryList.size());

                    // if (!Constant.mDictionaryList.contains(ny)) { //
                    // Chinmay Commented
                    Constant.mDictionaryList.add(ny);
                    Constant.mDictionaryMeaningList.add(nymm);

                    // }

                } else {

                    Constant.mDictionaryList.add(ny);
                    Constant.mDictionaryMeaningList.add(nymm);

                }

            }
        }

    }

    public void FetchRet() {

        Constant.redeemlist.clear();

        String tx, mn;

        Dbhelper db = new Dbhelper(getApplicationContext());
        Cursor c = null;
        try {
            c = db.open()
                    .getDatabaseObj()
                    .query(Dbhelper.TABLE_RET, null, null, null, null, null,
                            null);
            int txnm = c.getColumnIndex("name");
            int mnnm = c.getColumnIndex("bux");

            Constant.printMsg("The pending cart list in db ::::"
                    + c.getCount());

            if (c.getCount() > 0) {

                while (c.moveToNext()) {

                    tx = c.getString(txnm);
                    mn = c.getString(mnnm);

                    Constant.printMsg("dbadd:nym:" + tx + "  " + mn + "  ");

                    RedeemDto p = new RedeemDto();
                    p.setName(tx);
                    p.setBux(mn);

                    Constant.redeemlist.add(p);
                }
            }
        } catch (SQLException e) {

            Constant.printMsg("Sql exception in pending shop details ::::"
                    + e.toString());
        } finally {
            c.close();
            db.close();
        }
    }

    public void fetchDonate() {

        Constant.donatelust.clear();

        String tx, dt, bux_status;
        Long pt;

        Dbhelper db = new Dbhelper(getApplicationContext());
        Cursor c = null;
        try {

            c = db.open()
                    .getDatabaseObj()
                    .query(Dbhelper.TABLE_DONATE, null, null, null, null, null,
                            null);
            int txnm = c.getColumnIndex("name");
            int mnnm = c.getColumnIndex("date");
            int poi = c.getColumnIndex("points");
            int status = c.getColumnIndex("status");
            Constant.printMsg("The pending donate list in db ::::"
                    + c.getCount());

            if (c.getCount() > 0) {

                while (c.moveToNext()) {

                    tx = c.getString(txnm);
                    dt = c.getString(mnnm);
                    pt = c.getLong(poi);
                    bux_status = c.getString(status);
                    Constant.printMsg("donate:" + tx + "  " + dt + "  " + pt);

                    DonationDto p = new DonationDto();
                    p.setDate(dt);
                    p.setName(tx);
                    p.setPoint(String.valueOf(pt));
                    p.setStatus(bux_status);

                    Constant.donatelust.add(p);
                }
            }
        } catch (SQLException e) {

            Constant.printMsg("Sql exception in pending shop details ::::"
                    + e.toString());
        } finally {
            c.close();
            db.close();
        }
    }

    private void callFreebieDB(String query) {
        // TODO Auto-generated method stub
        Cursor c = null;

        try {
            c = db.open().getDatabaseObj().rawQuery(query, null);
            System.out
                    .println("The selected elist activity freebie count is date ::::::"
                            + c.getCount());
            mFreebieCount = c.getCount();
            if (c.getCount() > 0) {
                Constant.printMsg("Caling sysout:::::::::::::::::::::::::");
                while (c.moveToNext()) {
                    if (c.getString(0) != null) {
                        mFreebieLastDay = c.getString(0);
                        System.out
                                .println("Caling sysout:::::::::::::::::::::::::"
                                        + mFreebieLastDay);
                        mFreebieCount = c.getCount();

                    }

                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void fetchFreebie() {

        Constant.freelistmain.clear();

        String ph, mid, fna, lna, compath, web, flist, codecountry, companyName;

        Dbhelper db = new Dbhelper(getApplicationContext());
        Cursor c = null;
        try {

            c = db.open()
                    .getDatabaseObj()
                    .query(Dbhelper.TABLE_FREEBIE, null, null, null, null,
                            null, null);
            int pno = c.getColumnIndex("phonenumber");
            int merid = c.getColumnIndex("advertiserId");
            int fir = c.getColumnIndex("firstName");
            int las = c.getColumnIndex("lastName");
            int path = c.getColumnIndex("companyLogoPath");
            int websit = c.getColumnIndex("website");
            int freelist = c.getColumnIndex("freebielist");
            int cod = c.getColumnIndex("countryc");
            int companyName_index = c.getColumnIndex("companyName");

            Constant.printMsg("The pending freebie list in db ::::"
                    + c.getCount());

            if (c.getCount() > 0) {

                while (c.moveToNext()) {

                    phone = c.getString(pno);
                    code = c.getString(cod);

                    codecountry = c.getString(cod);
                    ph = c.getString(pno);
                    mid = c.getString(merid);
                    fna = c.getString(fir);
                    lna = c.getString(las);
                    compath = c.getString(path);
                    web = c.getString(websit);
                    flist = c.getString(freelist);
                    codecountry = c.getString(cod);
                    companyName = c.getString(companyName_index);

                    Constant.printMsg("donate:" + ph + "  " + mid + "  " + mid
                            + "  " + fna + "  " + lna);

                    FreebieMainDto p = new FreebieMainDto();

                    if (fna != null) {
                        Constant.printMsg("donatettt:" + fna + "  " + lna);
                        p.setAdvertiserId(mid);
                        p.setCompanyLogoPath(compath);
                        p.setFirstName(fna);
                        p.setLastName(lna);
                        p.setWebsite(web);
                        p.setWebsite(companyName);

                        if (c.getString(freelist) != null) {
                            Gson g = new Gson();
                            InputStream is = new ByteArrayInputStream(c
                                    .getString(freelist).getBytes());
                            Reader reader = new InputStreamReader(is);
                            Type fooType = new TypeToken<List<FreebieDto>>() {
                            }.getType();

                            List<FreebieDto> li = g.fromJson(reader, fooType);
                            if (li != null) {
                                p.setFreebilist(li);
                            }
                        }
                        Constant.freelistmain.add(p);
                    }
                }
            }
        } catch (SQLException e) {

            Constant.printMsg("Sql exception in pending shop details ::::"
                    + e.toString());
        } finally {
            c.close();
            db.close();
        }
    }

    public void FetchPreference() {
        // TODO Auto-generated method stub

        int tpoint = sp.getInt("intropoint", 0);

        Constant.totalcontact = tpoint;

        int point = sp.getInt("chatpoint", 0);

        Constant.totalchat = point;

        int point1 = sp.getInt("zzlepoint", 0);

        Constant.totalzzle = point1;

        int point2 = sp.getInt("konpoint", 0);

        Constant.totalkon = point2;

        int point3 = sp.getInt("imgpoint", 0);

        Constant.totalimg = point3;

        int point4 = sp.getInt("locpoint", 0);

        Constant.totalloc = point4;

        int point5 = sp.getInt("nympoint", 0);

        Constant.totalnym = point5;

        int point6 = sp.getInt("deelpoint", 0);

        Constant.totaldeel = point6;

        int point7 = sp.getInt("wishpoint", 0);

        Constant.totalwish = point7;

    }

    public void autoTimeEnable() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getApplicationContext(), AlertDialog.THEME_HOLO_LIGHT);
        // alertDialogBuilder.setTitle("Your Title");
        alertDialogBuilder.setMessage("Please Enable Automatic Date and Time")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getApplicationContext()
                                .startActivity(
                                        new Intent(
                                                Settings.ACTION_DATE_SETTINGS));
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
