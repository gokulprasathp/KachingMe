package com.wifin.kachingme.kaching_feature.dazz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.DazzAdapterLED;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by user on 12/2/2016.
 */
public class LEDLibrary extends Fragment implements OnItemClickDazz {
    LinearLayout dazzAppBar;
    Toolbar dazzToolBar;
    FrameLayout dazzFrameAction;
    ImageView imgDazzBack;
    RecyclerView dazzRecyclerView;
    List<String> dazzListValues = new ArrayList();
    DazzAdapterLED dazzAdapter = new DazzAdapterLED();
    int height, width;
    String query;
    Dbhelper db;
    ArrayList<String> mZzleLibList = new ArrayList<String>();
    int mLibCount;
    SharedPreferences sharedPrefs;
    boolean longclick = false;
    int pos;
    Activity act;
    View view;
    public static Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        db = new Dbhelper(getContext());
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        view = inflater.inflate(R.layout.activity_dazz, container, false);
        init();
        dazzActivity();
        query = "select msg from " + Dbhelper.TABLE_LED;
        collectData(query);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        dazzRecyclerView.setLayoutManager(linearLayoutManager);
        dazzRecyclerView.setHasFixedSize(true);

        dazzListValues = new ArrayList<>();

        for (int dazz = 0; dazz < 10; dazz++) {
            dazzListValues.add("Message :" + dazz);
        }
        if (mLibCount > 0) {
            dazzAdapter = new DazzAdapterLED(getActivity(), mZzleLibList);
            dazzRecyclerView.setAdapter(dazzAdapter);
        }
        return view;

    }


    public void init() {
        dazzAppBar = (LinearLayout) view.findViewById(R.id.dazzAppBar);
        dazzToolBar = (Toolbar) view.findViewById(R.id.dazzToolBar);
        dazzFrameAction = (FrameLayout) view.findViewById(R.id.dazzFrameAction);
        imgDazzBack = (ImageView) view.findViewById(R.id.imgDazzBack);
        dazzRecyclerView = (RecyclerView) view.findViewById(R.id.dazzRecyclerView);
    }

    private void dazzActivity() {
//        DisplayMetrics metrics = new DisplayMetrics();
//        getFragmentManager().getDefaultDisplay().getMetrics(metrics);
        width =  Constant.screenWidth;
        height = Constant.screenHeight;

        Constant.screenWidth = width;
        Constant.screenHeight = height;

        AppBarLayout.LayoutParams mToolBarLayoutParams = new AppBarLayout.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        mToolBarLayoutParams.width = (int) width;
        mToolBarLayoutParams.height = (int) height * 10 / 100;
        mToolBarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        dazzToolBar.setLayoutParams(mToolBarLayoutParams);

        FrameLayout.LayoutParams mBackBtnParams = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBackBtnParams.width = (int) width * 4 / 100;
        mBackBtnParams.height = (int) width * 6 / 100;
        mBackBtnParams.leftMargin = (int) width * 4 / 100;
        mBackBtnParams.gravity = Gravity.START | Gravity.CENTER;
        imgDazzBack.setLayoutParams(mBackBtnParams);

        LinearLayout.LayoutParams mRecyclerViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mRecyclerViewParams.width = (int) width;
        mRecyclerViewParams.height = (int) height;
//        dazzRecyclerView.setLayoutParams(mRecyclerViewParams);

        LinearLayout.LayoutParams mAddBtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mAddBtnParams.width = (int) width * 15/100;
        mAddBtnParams.height = (int) height * 8/ 100;
        mAddBtnParams.rightMargin = width * 2 / 100;
//        mHeaderImg.setLayoutParams(mAddBtnParams);
    }

    private ArrayList<String> collectData(String query) {
        // TODO Auto-generated method stub
        Cursor c = null;

        try {
            c = db.open().getDatabaseObj().rawQuery(query, null);

            Constant.printMsg("The count is::::::>>>> " + c.getCount());
            mLibCount = c.getCount();
            if (c.getCount() > 0) {
                while (c.moveToNext()) {

                    mZzleLibList.add(String.valueOf(c.getString(0)));
                    Constant.printMsg("linbrary ::: " + mZzleLibList);

                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            db.close();
        }

        LinkedHashSet hs = new LinkedHashSet();
        hs.addAll(mZzleLibList);
        mZzleLibList.clear();
        mZzleLibList.addAll(hs);

        Collections.reverse(mZzleLibList);

        Constant.printMsg("size dazz" + mZzleLibList.size());
        for (int i = 0; i < mZzleLibList.size(); i++) {
            if (mZzleLibList.get(i).equalsIgnoreCase("null")) {
                mZzleLibList.remove(i);
            }
            if (mZzleLibList.size() > 50) {

                Constant.printMsg("size in" + mZzleLibList.size()
                        + mZzleLibList.get(mZzleLibList.size() - 1).toString());

                deleteDb(mZzleLibList.get(mZzleLibList.size() - 1).toString());

                mZzleLibList.remove(mZzleLibList.size() - 1);

                Constant.printMsg("size out" + mZzleLibList.size()
                        + mZzleLibList.get(mZzleLibList.size() - 1).toString());

            }

            String lasttext = sharedPrefs.getString("lastdazzl", "");

            Constant.printMsg("last dazzzl::" + lasttext);

            if (mZzleLibList.contains(lasttext)) {

                int index = mZzleLibList.indexOf(lasttext);
                mZzleLibList.remove(index);
                mZzleLibList.add(0, lasttext);

            }

        }

        return mZzleLibList;
    }


    public void deleteDb(String pos) {
        // TODO Auto-generated method stub

        Cursor c = null;
        String q = "DELETE FROM " + Dbhelper.TABLE_ZZLE + " WHERE msg='" + pos
                + "'";
        try {
            c = db.open().getDatabaseObj().rawQuery(q, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
        } catch (SQLException e) {

        } finally {
            c.close();
            db.close();
        }
    }

    @Override
    public void onClick(View view, int position,String data, Context context) {

        Constant.printMsg("position of dazz clicked ::::>>>"+data+"  "+position+"   " +mZzleLibList.size());
        Constant.mPlainDazZ = false;
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("dazzStatus", false);
        editor.commit();
        if (longclick == false) {
            if (Constant.mFromGroup == true) {
                Constant.mFromGroup = false;
                Constant.mFromDazzLib = true;
                Constant.mDazzLib = true;
                Constant.mChatText = mZzleLibList.get(position)
                        .toString();
                Constant.mDazZLibText =  mZzleLibList.get(position)
                        .toString();
                DazzTabActivity.mDazzTabActivity.finish();
//                Intent intent = new Intent(context,
//                        DazzPlainActivity.class);
//                context.startActivity(intent);
//                finish();
            } else {
                Constant.mFromDazzLib = true;
                Constant.mDazzLib = true;
                Constant.mChatText = data;
                Constant.mDazZLibText =  data;
//                Intent intent = new Intent(context,
//                        DazzPlainActivity.class);
//                context.startActivity(intent);
                DazzTabActivity.mDazzTabActivity.finish();
//                finish();
            }
        }
    }
    @Override
    public void onLongClick(View view, final int position,final String data,final Context context) {
        pos = position;
        longclick = true;

//        AlertDialog.Builder builder = new AlertDialog.Builder(
//                context);
//        builder.setTitle("Alert");
//        builder.setMessage("Do You Want To Delete");
//        builder.setPositiveButton("Yes",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(
//                            DialogInterface dialogInterface, int i) {
//
//                        longclick = false;
//
//                        String dl = data;
//
//
//                        Intent intent = new Intent(context,
//                                DazzTabActivity.class);
//                        context.startActivity(intent);
//
//                        dialogInterface.cancel();
//                    }
//                });
//
//        builder.setNegativeButton("No",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//
//                    }
//                });
//
//        Dialog alertDialog = builder.create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.setCancelable(false);
//        alertDialog.show();



        AlertDialog.Builder b;
        b = new AlertDialog.Builder(context);

        b.setCancelable(false);
        b.setMessage("Do You Want To Delete").setCancelable(
                false);

        b.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                longclick = false;
                db = new Dbhelper(context);
                sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

//                dazzRecyclerView = (RecyclerView) view.findViewById(R.id.dazzRecyclerView);

                deleteZzle(data);
//
                Toast.makeText(context,
                        "Deleted", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context,
                        DazzTabActivity.class);
                context.startActivity(intent);


            }


        });
        b.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        b.setCancelable(true);

        AlertDialog alert = b.create();
        alert.show();

    }


//    public Context getContext() {
//        // TODO Auto-generated method stub
//        return context;
//    }

    private void deleteZzle(String data) {

        Cursor c = null;
        String q = "DELETE FROM " + Dbhelper.TABLE_LED
                + " WHERE msg='" + data + "'";
        try {
            c = db.open().getDatabaseObj()
                    .rawQuery(q, null);
            System.out
                    .println("No of deleted rows ::::::::::"
                            + c.getCount());
        } catch (SQLException e) {

        } finally {
            c.close();
            db.close();
        }

    }
}
