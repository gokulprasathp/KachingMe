package com.wifin.kachingme.kaching_feature.kons;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.KonsAdaptor;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.kaching_feature.nynms.OnItemClickListenerInterface;
import com.wifin.kachingme.pojo.KonesCheckPojo;
import com.wifin.kachingme.pojo.KonsDto;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.Collections;

public class KonsHomeScreen extends Activity implements OnItemClickListenerInterface
{
    public static ArrayList<KonsDto> mKonsList = new ArrayList<KonsDto>();
    public static ArrayList<KonesCheckPojo> mKonsCheckList = new ArrayList<KonesCheckPojo>();
    int width = 0, height = 0;
    ImageView mBackBtn_Img, mKons_Img, mKonsAdd_img;
    TextView mKons_Txt;
    FloatingActionButton mFloatingActionBtn;
    Button mKonsCalcelBtn, mKonsAttachBtn;
    LinearLayout mKonsAttachLayout;
    AppBarLayout mAppBarLayout;
    Toolbar mToolBar;
    RecyclerView KonsRecyclerView;
    KonsAdaptor mKonsadapter;
    ArrayList mValueList = new ArrayList();
    Dbhelper db;
    String query;
    byte[] mkonsImgByte;
    String state = null;
    boolean isLongPress;
    public static KonsHomeScreen mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.kons_home_screen);

        mIntVariable();
        mScreenArrangement();
        mActivity=this;

        mBackBtn_Img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Constant.mDelete = false;
                finish();
            }
        });

        mKonsCalcelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Constant.mDelete = false;

                if (Constant.mKonsFromSlider)
                {
                    finish();
                }
                else
                {
                    finish();
                }
            }
        });

        mKonsAdd_img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Constant.mDelete == true)
                {
                    if (mKonsCheckList.size() > 0)
                    {
                        deleteAlert();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please select atleast one to delete", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Intent in = new Intent(KonsHomeScreen.this, KonsActivity.class);
                    startActivity(in);
                }
            }
        });

        /*mKonsAttachBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Constant.printMsg("mflotingActionBtn");
                Constant.printMsg("goododdodoo " + Constant.mDelete);

                if (mKonsList.size() > 0)
                {
                    Constant.mDelete = false;

                    Constant.printMsg("data konse size...." + mKonsCheckList.size());

                    for (int i = 0; i < mKonsCheckList.size(); i++)
                    {
                        Constant.printMsg("data konse size....1" + mKonsCheckList.size() + mKonsCheckList.get(i).isChecked());

                        if (mKonsCheckList.get(i).isChecked())
                        {
                            Constant.konsSelectedList.add(mKonsCheckList.get(i).getMessage().toString().trim());
                            Constant.mKonsBackground = mKonsCheckList.get(i).getBackground().toString().trim();
                            Constant.konsBackgroundList.add(Constant.mKonsBackground);
                            Constant.mKonsColor = mKonsCheckList.get(i).getColor().toString().trim();
                            Constant.konsColorList.add(Constant.mKonsColor);
                            Constant.printMsg("date konse in konzsd" + mKonsCheckList.get(i).getMessage().toString().trim());
                        }

                        if (i + 1 == mKonsCheckList.size())
                        {
                            forceActivity();
                            Constant.printMsg("data  konse for size...." + mKonsCheckList.size());
                        }
                        Constant.printMsg("data konse i...." + Constant.konsSelectedList + Constant.konsBackgroundList + Constant.konsColorList);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please create KonS and send", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        mKonsAttachBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Constant.printMsg("mflotingActionBtn");
                Constant.printMsg("goododdodoo " + Constant.mDelete);

                if (mKonsList.size() > 0)
                {
                    Constant.mDelete = false;

                    Constant.printMsg("data konse size...." + mKonsCheckList.size());

                    if (mKonsCheckList.size() > 0)
                    {
                        for (int i = 0; i < mKonsCheckList.size(); i++)
                        {
                            Constant.printMsg("data konse size....1" + mKonsCheckList.size() + mKonsCheckList.get(i).isChecked());

                            if (mKonsCheckList.get(i).isChecked())
                            {
                                Constant.konsSelectedList.add(mKonsCheckList.get(i).getMessage().toString().trim());
                                Constant.mKonsBackground = mKonsCheckList.get(i).getBackground().toString().trim();
                                Constant.konsBackgroundList.add(Constant.mKonsBackground);
                                Constant.mKonsColor = mKonsCheckList.get(i).getColor().toString().trim();
                                Constant.konsColorList.add(Constant.mKonsColor);
                                Constant.printMsg("date konse in konzsd" + mKonsCheckList.get(i).getMessage().toString().trim());
                            }

                            if (i + 1 == mKonsCheckList.size())
                            {
                                forceActivity();
                                Constant.printMsg("data  konse for size...." + mKonsCheckList.size());
                            }
                            Constant.printMsg("data konse i...." + Constant.konsSelectedList + Constant.konsBackgroundList + Constant.konsColorList);
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please Select KonS to Attach", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please create KonS and send", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void forceActivity()
    {
        // TODO Auto-generated method stub

        Constant.printMsg("testing acttt ");

        if (Constant.konsSelectedList.size() > 0)
        {
            if (Constant.mKonsFromSlider)
            {
                Constant.mKons = true;
                Constant.mKonsGroup = true;
                Intent intent = new Intent(KonsHomeScreen.this, SliderTesting.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Constant.mDelete = false;

                if (Constant.konsSelectedList.size() > 0)
                {
                    if (Constant.mKonsFromChat)
                    {
                        Constant.mKons = true;
                        Constant.mKonsFromChat = false;
                        finish();
                    }
                    else
                    {
                        Constant.mKonsGroup = true;
                        finish();
                    }
                }
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Please select atleast one kons", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void mIntVariable()
    {
        mKonsAttachLayout = (LinearLayout) findViewById(R.id.kons_attach_layout);
        mKonsCalcelBtn = (Button) findViewById(R.id.kons_cancel_btn);
        mKonsAttachBtn = (Button) findViewById(R.id.kons_attach_btn);

        mFloatingActionBtn = (FloatingActionButton) findViewById(R.id.kons_floatingbtn);

        mBackBtn_Img = (ImageView) findViewById(R.id.back_img);
        mKons_Img = (ImageView) findViewById(R.id.kong_img);
        mKonsAdd_img = (ImageView) findViewById(R.id.add_kons_img);
        mKons_Txt = (TextView) findViewById(R.id.kons_text);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.kons_appbar_layout);
        mToolBar = (Toolbar) findViewById(R.id.kons_toolbar_layout);

        Constant.typeFace(this, mKons_Txt);
        Constant.typeFace(this, mKonsCalcelBtn);
        Constant.typeFace(this, mKonsAttachBtn);

        KonsRecyclerView = (RecyclerView) findViewById(R.id.kons_recyclerview);

        int spacingInPixels = 0;

        KonsRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }

    private void mScreenArrangement()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        Constant.screenWidth = width;
        Constant.screenHeight = height;

        AppBarLayout.LayoutParams mToolBarLayoutParams = new AppBarLayout.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        mToolBarLayoutParams.width = (int) width;
        mToolBarLayoutParams.height = (int) height * 8 / 100;
        mToolBarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        mToolBar.setLayoutParams(mToolBarLayoutParams);

        FrameLayout.LayoutParams mBackBtnParams = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBackBtnParams.width = width * 6 / 100;
        mBackBtnParams.height = width * 6 / 100;
        mBackBtnParams.leftMargin = (int) width * 4 / 100;
        mBackBtnParams.gravity = Gravity.START | Gravity.CENTER;
        mBackBtn_Img.setLayoutParams(mBackBtnParams);

        FrameLayout.LayoutParams mKonsAddBtnParams = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mKonsAddBtnParams.width = (int) width * 7 / 100;
        mKonsAddBtnParams.height = (int) width * 7 / 100;
        mKonsAddBtnParams.rightMargin = (int) width * 5 / 100;
        mKonsAddBtnParams.gravity = Gravity.END | Gravity.CENTER;
        mKonsAdd_img.setLayoutParams(mKonsAddBtnParams);

        LinearLayout.LayoutParams mKonsImgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mKonsImgParams.width = (int) width * 8 / 100;
        mKonsImgParams.height = (int) width * 5 / 100;
        mKonsImgParams.rightMargin = (int) width * 2 / 100;
        mKonsImgParams.gravity = Gravity.CENTER;
        mKons_Img.setLayoutParams(mKonsImgParams);

        LinearLayout.LayoutParams mKonsTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mKonsTextParams.rightMargin = (int) width * 2 / 100;
        mKons_Txt.setLayoutParams(mKonsTextParams);

        LinearLayout.LayoutParams mKonsRecyclerViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mKonsRecyclerViewParams.width = (int) width;
        mKonsRecyclerViewParams.height = (int) height * 79 / 100;
        mKonsRecyclerViewParams.topMargin = (int) height * 8 / 100;
        KonsRecyclerView.setLayoutParams(mKonsRecyclerViewParams);

        FrameLayout.LayoutParams mKonsbtnlayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        mKonsbtnlayoutParams.width = (int) width;
        mKonsbtnlayoutParams.height = (int) height * 7 / 100;
        mKonsbtnlayoutParams.topMargin = (int) height * 80 / 100;
        mKonsAttachLayout.setLayoutParams(mKonsbtnlayoutParams);

        LinearLayout.LayoutParams mKonsAttachbtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mKonsAttachbtnParams.width = (int) width * 35 / 100;
        mKonsAttachbtnParams.height = (int) height * 7 / 100;
        mKonsAttachbtnParams.leftMargin = (int) width * 5 / 100;
        mKonsAttachBtn.setLayoutParams(mKonsAttachbtnParams);

        LinearLayout.LayoutParams mKonsCancelbtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mKonsCancelbtnParams.width = (int) width * 35 / 100;
        mKonsCancelbtnParams.height = (int) height * 7 / 100;
        mKonsCancelbtnParams.leftMargin = (int) width * 5 / 100;
        mKonsCalcelBtn.setLayoutParams(mKonsCancelbtnParams);

        FrameLayout.LayoutParams floatingParams = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        floatingParams.bottomMargin = (int) height * 12 / 100;
        floatingParams.rightMargin = (int) width * 5 / 100;
        floatingParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        mFloatingActionBtn.setLayoutParams(floatingParams);

        if (width >= 600)
        {
            mKons_Txt.setTextSize(19);
            mKonsCalcelBtn.setTextSize(17);
            mKonsAttachBtn.setTextSize(17);
        }
        else if (width > 501 && width < 600)
        {
            mKons_Txt.setTextSize(17);
            mKonsCalcelBtn.setTextSize(15);
            mKonsAttachBtn.setTextSize(15);
        }
        else if (width > 331 && width < 500)
        {
            mKons_Txt.setTextSize(15);
            mKonsCalcelBtn.setTextSize(14);
            mKonsAttachBtn.setTextSize(14);
        }
        else if (width > 260 && width < 330)
        {
            mKons_Txt.setTextSize(14);
            mKonsCalcelBtn.setTextSize(13);
            mKonsAttachBtn.setTextSize(13);
        }
        else if (width <= 260)
        {
            mKons_Txt.setTextSize(14);
            mKonsCalcelBtn.setTextSize(13);
            mKonsAttachBtn.setTextSize(13);
        }
    }

    private ArrayList<KonsDto> collectData(String query)
    {
        // TODO Auto-generated method stub
        Cursor c = null;

        try
        {
            c = db.open().getDatabaseObj().rawQuery(query, null);

            Constant.printMsg("The selected elist activity count is ::::::"
                    + c.getCount());
//            mKonsCount = c.getCount();
            // int check_index = c.getColumnIndex("check");
            if (c.getCount() > 0) {
                Constant.printMsg("Caling sysout:::::::::::::::::::::::::");
                while (c.moveToNext()) {
                    KonsDto kp = new KonsDto();
                    kp.setText(String.valueOf(c.getString(1)));
                    Constant.printMsg("sannata22   " + String.valueOf(c.getString(1)) + String.valueOf(c.getString(3)) + String.valueOf(c.getString(4)));

                    mkonsImgByte = c.getBlob(2);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(mkonsImgByte, 0, mkonsImgByte.length);

                    Constant.printMsg("sannata11   " + bitmap);

                    kp.setImage(bitmap);
                    kp.setBackground(String.valueOf(c.getString(3)));
                    kp.setColor(String.valueOf(c.getString(4)));

                    mKonsList.add(kp);
                    // mKonsList.add(String.valueOf(c.getString(1)));
                    Collections.reverse(mKonsList);

                }

            }

        } catch (Exception e) {
            // TODO: handle exception
        } finally {

            db.close();

        }
        return mKonsList;
    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position) {

        Constant.printMsg("kons testttt111 " + isLongPress);
        mKonsAdd_img.setImageResource(R.drawable.delete);

        if (isLongPress)
        {
            Constant.printMsg("log presss on grid...............");
            state = "longpress";
            Constant.mselectedPosition = position;
            mKonsadapter = new KonsAdaptor(KonsHomeScreen.this, mKonsList, state);
            KonsRecyclerView.setAdapter(mKonsadapter);
            mKonsadapter.notifyDataSetChanged();
            isLongPress = false;
            Constant.mDelete = true;

        }
    }

    protected void deleteQuery(String delete_query)
    {
        Cursor c = null;

        try
        {
            c = db.open().getDatabaseObj().rawQuery(delete_query, null);
            Constant.printMsg("No of updated rows in db delete::billllll:::"
                    + c.getCount());
        }
        catch (SQLException e)
        {

            e.printStackTrace();
        }
        finally
        {
            c.close();
        }

        db.close();
    }

    private void deleteAlert()
    {
        AlertDialog.Builder b;
        b = new AlertDialog.Builder(this);

        b.setCancelable(false);
        b.setMessage("Are you sure you want to delete").setCancelable(false);
        b.setNegativeButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                for (int i = 0; i < mKonsCheckList.size(); i++)
                {
                    if (mKonsCheckList.get(i).isChecked())
                    {
                        String delete_query = "DELETE FROM "
                                + Dbhelper.TABLE_KONS + " Where msg = '"
                                + mKonsCheckList.get(i).getMessage() + "'";
                        Constant.printMsg("selected delete query:::>>"
                                + delete_query);
                        deleteQuery(delete_query);
                        // collectData(query);
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }
                Constant.mDelete = false;
            }
        });

        b.setPositiveButton("NO", new DialogInterface.OnClickListener()
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

    @Override
    public void onBackPressed()
    {
        Constant.mDelete = false;

        if (Constant.mKonsFromSlider)
        {
            Constant.mKonsFromSlider = false;
            Intent i = new Intent(KonsHomeScreen.this, SliderTesting.class);
            startActivity(i);
            finish();
        }
        else
        {
            finish();
        }
        super.onBackPressed();
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration
    {
        private int space;

        public SpacesItemDecoration(int space)
        {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0)
            {
                outRect.top = space;
            }
            else
            {
                outRect.top = 0;
            }
        }
    }

    @Override
    protected void onResume()
    {
        mKonsList.clear();
        Constant.konsSelectedList.clear();
        db = new Dbhelper(getApplicationContext());
        query = "select * from " + Dbhelper.TABLE_KONS;
        collectData(query);
        isLongPress = true;
        mKonsCheckList.clear();
        Constant.konsSelectedList.clear();
        Constant.konsBackgroundList.clear();
        Constant.konsColorList.clear();

        GridLayoutManager lLayout;
        state = "normal";
        lLayout = new GridLayoutManager(this, 3);
        Constant.printMsg("sannata   " + mKonsList.size());
        KonsRecyclerView.setLayoutManager(lLayout);

        if (mKonsList.size() > 0)
        {
            mKonsadapter = new KonsAdaptor(KonsHomeScreen.this, mKonsList, state);
            KonsRecyclerView.setAdapter(mKonsadapter);
            mKonsadapter.notifyDataSetChanged();
        }
        super.onResume();
    }
}
