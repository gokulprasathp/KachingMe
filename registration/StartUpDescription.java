package com.wifin.kachingme.registration_and_login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wifin.kachingme.R;
import com.wifin.kachingme.applications.NiftyApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.CartAdapDto;
import com.wifin.kachingme.pojo.CartDetailsDto;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.WebConfig;

public class StartUpDescription extends Activity {

    LinearLayout mComapnyLayout;
    ImageView mProductHeaderImage, mProductImage;
    TextView mHeader, mCompanyName, mProductName, mProdValidity, mWebLink, mAddToCart;
    int height, width;
    DatabaseHelper dbAdapter;
    SharedPreferences sp;
    Editor ed;
    String country_code, mobile_no, full_mobile_no;
    ProgressDialog progressdialog;
    byte img_byte[];
    String TAG = StartUpDescription.class.getSimpleName();
    String mobileno;
    private ImageLoadingListener animateFirstListener = new NewStartUpActivity.AnimateFirstDisplayListener();
    private DisplayImageOptions options;
    String id, data;
    Dbhelper db;

    // WebView web_View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_description);
//		ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
//		View.inflate(this, R.layout.activity_start_up_description, vg);
        inialize();
        screenArrange();
        //logo.setVisibility(ImageView.INVISIBLE);
        //back.setVisibility(ImageView.VISIBLE);
        //cart.setVisibility(ImageView.INVISIBLE);
        //sideMenufoot.setVisibility(LinearLayout.GONE);
        db = new Dbhelper(getApplicationContext());
        //head.setText("Freebie");
        //head.setTextColor(Color.parseColor("#FFFFFF"));
        //head.setVisibility(View.VISIBLE);
        //Ka_newlogo.setVisibility(View.INVISIBLE);
        //headlay.setBackgroundColor(Color.parseColor("#FE0000"));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.stub).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();

        ImageLoader.getInstance().init(ImageLoaderConfiguration
                .createDefault(getApplicationContext()));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mobileno = bundle.getString("mobileno");
            country_code = bundle.getString("country_code");
            full_mobile_no = mobileno;
            Log.d(TAG, "Mobile NO::" + full_mobile_no);
        }

        Constant.phone = mobileno;

        Constant.printMsg("desc::" + mobileno + country_code);

        ImageLoader.getInstance().displayImage(
                String.valueOf(
                        Constant.freelistmain.get(Constant.newstlistpos)
                                .getCompanyLogoPath()).replaceAll(" ", "%20"),
                mProductHeaderImage, options, animateFirstListener);
        Constant.printMsg("sdpin pos::"
                + Constant.spinnerpos
                + "       "
                + Constant.freelistmain.get(Constant.newstlistpos)
                .getFreebilist().get(Constant.spinnerpos).getTagLine());
        mProductName.setText(Constant.freelistmain.get(Constant.newstlistpos)
                .getFreebilist().get(Constant.spinnerpos).getTagLine());
        mCompanyName.setText(Constant.freelistmain.get(Constant.newstlistpos).getCompanyName());

        mWebLink.setText(Constant.freelistmain.get(Constant.newstlistpos).getWebsite());
        mProdValidity.setText("Valid only at participating Location");

        ImageLoader.getInstance().displayImage(
                String.valueOf(
                        Constant.freelistmain.get(Constant.newstlistpos)
                                .getFreebilist().get(Constant.spinnerpos)
                                .getPhotoPath()).replaceAll(" ", "%20"),
                mProductImage, options, animateFirstListener);

//		ImageLoader.getInstance().displayImage(
//				String.valueOf(
//						Constant.freelistmain.get(Constant.newstlistpos)
//								.getFreebilist().get(Constant.spinnerpos)
//								.getQrCodePath()).replaceAll(" ", "%20"),
//				prodqr, options, animateFirstListener);

        dbAdapter = NiftyApplication.getDatabaseAdapter();
        sp = getSharedPreferences(NiftyApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        ed = sp.edit();

        country_code = Constant.countrycode;
        mobile_no = Constant.phone;
        full_mobile_no = mobile_no;
        img_byte = Constant.byteimage;
        // this.web_View = (WebView) findViewById(R.id.webview);
        // WebSettings settings = web_View.getSettings();
        // settings.setJavaScriptEnabled(true);
        // web_View.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

//		back.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				Intent i = new Intent(StartUpDescription.this,
//						NewStartUpActivity.class);
//				startActivity(i);
//				finish();
//
//			}
//		});
//		siva
        Linkify.addLinks(mWebLink, Linkify.WEB_URLS);

        mWebLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Constant.printMsg("clicked web_Link");

            }
        });
        mAddToCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (Connectivity.isConnected(StartUpDescription.this)) {

                    progressdialog = ProgressDialog.show(
                            StartUpDescription.this,
                            getResources().getString(R.string.please_wait),
                            getResources().getString(R.string.loading), true);
                    progressdialog.show();

                    id = Constant.freelistmain.get(Constant.newstlistpos)
                            .getFreebilist().get(Constant.spinnerpos).getId();

                    Constant.printMsg("called1");
                    data = jsonForm();
                    Constant.printMsg("ssss::::elseeeeeeee222222222" + data);
                    new posId().execute();

                } else {
                    Constant.printMsg("ssss::::elseeeeeee333333");
                    CommonMethods com = new CommonMethods(StartUpDescription.this);
                    com.okDialogBox(StartUpDescription.this, getResources().getString(
                            R.string.no_internet_connection));
                }

            }
        });

    }


    private void inialize() {
        // TODO Auto-generated method stub
        mComapnyLayout = (LinearLayout) findViewById(R.id.addCart_compayLayout);
        mProductHeaderImage = (ImageView) findViewById(R.id.startdes_imag);
        //prodqr = (ImageView) findViewById(R.id.startdes_qrimag);
        mProductImage = (ImageView) findViewById(R.id.startdes_primag);

        mCompanyName = (TextView) findViewById(R.id.addCart_companyName);
        mProductName = (TextView) findViewById(R.id.startdes_prod);
        mProdValidity = (TextView) findViewById(R.id.startdes_desc);
        mWebLink = (TextView) findViewById(R.id.web_link);
        mHeader = (TextView) findViewById(R.id.addCart_headtext);
        mAddToCart = (TextView) findViewById(R.id.startdes_cont);
        // web_View = (WebView) findViewById(R.id.webview);
    }

    private String jsonForm() {
        // TODO Auto-generated method stub
        String d = null;

        CartDetailsDto l = new CartDetailsDto();
        l.setBux(Constant.bux);
        l.setType(0);
        l.setPhoneNumber(Constant.phone);
        l.setDescription("Free Bie");
        l.setOfferId(Long.parseLong(id));
        d = new Gson().toJson(l);

        Constant.printMsg("order cart name ::::" + d.toString());
        return d;
    }

    public class posId extends AsyncTask<String, String, String> {
        // ProgressDialog progressDialog;

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressdialog.show();
        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();

            result = ht.doPostMobizee(data, WebConfig.CART_URL);

            Constant.printMsg("result dis post id" + WebConfig.CART_URL + data);

            return result;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            Constant.printMsg("priya test111 ::::::>>>>>>>> " + result);
            super.onPostExecute(result);
            progressdialog.dismiss();
            Constant.printMsg("sdfsdf:::post id::::::>>>>>"
                    + result.toString());

            if (result != null && result.length() > 0) {

                Constant.printMsg("result voutnnm" + result);

                CartAdapDto ca = new CartAdapDto();
                ca.setBuxs("0");
                ca.setDeelOfferDiscountId(Constant.freelistmain
                        .get(Constant.newstlistpos).getFreebilist()
                        .get(Constant.spinnerpos).getId());
                ca.setDiscount("Free");
                // ca.setItem(Constant.freelistmain.get(Constant.newstlistpos)
                // .getFreebilist().get(Constant.spinnerpos).getTagLine());
                ca.setItem(Constant.freelistmain.get(Constant.newstlistpos)
                        .getFreebilist().get(Constant.spinnerpos).getName());
                ca.setMerchantId(Constant.freelistmain.get(
                        Constant.newstlistpos).getAdvertiserId());
                ca.setOffer("Free");
                ca.setPhotoPath(Constant.freelistmain
                        .get(Constant.newstlistpos).getFreebilist()
                        .get(Constant.spinnerpos).getPhotoPath());
                ca.setQrCodePath(Constant.freelistmain
                        .get(Constant.newstlistpos).getFreebilist()
                        .get(Constant.spinnerpos).getQrCodePath());
                // ca.setProductName(Constant.freelistmain
                // .get(Constant.newstlistpos).getFreebilist()
                // .get(Constant.spinnerpos).getName());
                ca.setProductName(Constant.freelistmain.get(
                        Constant.newstlistpos).getCompanyName());
                ca.setType("0");
                ca.setMerchantName(Constant.freelistmain.get(
                        Constant.newstlistpos).getWebsite());
                ca.setMerphotoPath(Constant.freelistmain.get(
                        Constant.newstlistpos).getCompanyLogoPath());
                ca.setCompanyname(Constant.freelistmain.get(
                        Constant.newstlistpos).getCompanyName());

                Constant.cartfinal.add(ca);
                Constant.mPhoneNum = Constant.country + Constant.phone;
                // Constant.mPhoneNum = Constant.phone;
                Constant.mCartType = "0";
                /** delete cart table before insert */
                deleteCart();

                ContentValues cv = new ContentValues();
                Constant.printMsg("deel ::::::>>>>>>> "

                        + Constant.freelistmain.get(Constant.newstlistpos)
                        .getFreebilist().get(Constant.spinnerpos)
                        .getId());
                cv.put("phonenumber", Constant.country + Constant.phone);
                cv.put("deelid",
                        Constant.freelistmain.get(Constant.newstlistpos)
                                .getFreebilist().get(Constant.spinnerpos)
                                .getId());
                cv.put("type", "0");
                cv.put("desc", "Free Bie");
                cv.put("photopath",
                        Constant.freelistmain.get(Constant.newstlistpos)
                                .getFreebilist().get(Constant.spinnerpos)
                                .getPhotoPath());
                cv.put("bux", "0");
                cv.put("qrpath",
                        Constant.freelistmain.get(Constant.newstlistpos)
                                .getFreebilist().get(Constant.spinnerpos)
                                .getQrCodePath());
                cv.put("discount", "Free");
                cv.put("item", Constant.freelistmain.get(Constant.newstlistpos)
                        .getFreebilist().get(Constant.spinnerpos).getTagLine());
                cv.put("offer", " Free");
                cv.put("prodname",
                        Constant.freelistmain.get(Constant.newstlistpos)
                                .getFreebilist().get(Constant.spinnerpos)
                                .getName());
                cv.put("merchantname",
                        Constant.freelistmain.get(Constant.newstlistpos)
                                .getWebsite());

                cv.put("merchantid",
                        Constant.freelistmain.get(Constant.newstlistpos)
                                .getAdvertiserId());
                Constant.printMsg("paaathhhhh:::::::"
                        + Constant.freelistmain.get(Constant.newstlistpos)
                        .getCompanyLogoPath());
                cv.put("merchantimagepath",
                        Constant.freelistmain.get(Constant.newstlistpos)
                                .getCompanyLogoPath());
                cv.put("companyname",
                        Constant.freelistmain.get(Constant.newstlistpos)
                                .getCompanyName());
                Constant.printMsg("dell id   "
                        + Constant.freelistmain.get(Constant.newstlistpos)
                        .getCompanyName());

                insertDB(cv);
                Constant.printMsg("post id:: inserted::" + cv);

                Intent intent = new Intent(StartUpDescription.this,
                        StartUp.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(getApplicationContext(),
                        "Network Error!Please try again later!",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void deleteCart() {
        try {
            int a = (int) db.open().getDatabaseObj()
                    .delete(Dbhelper.TABLE_CART, null, null);

            Constant.printMsg("No of deleted rows in shop details :::::::::"
                    + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in deleted shop details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }

    }

    public void insertDB(ContentValues v) {
        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_CART, null, v);
            Constant.printMsg("No of inserted rows in shop details :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in new shop details ::::::" + e.toString());
        } finally {
            db.close();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(StartUpDescription.this, NewStartUpActivity.class));
        finish();
    }

    private void screenArrange() {
        // TODO Auto-generated method stub
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        Constant.screenHeight = height;
        Constant.screenWidth = width;

        LinearLayout.LayoutParams headerParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        headerParama.width = width;
        headerParama.height = height * 7 / 100;
        mHeader.setLayoutParams(headerParama);
        mHeader.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutMenuq = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutMenuq.width = width * 96 / 100;
        layoutMenuq.height = height * 9 / 100;
        layoutMenuq.gravity = Gravity.CENTER;
        layoutMenuq.setMargins(width * 2 / 100, width * 2 / 100, width * 2 / 100, 0);
        mComapnyLayout.setLayoutParams(layoutMenuq);
        mComapnyLayout.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutMenuqtx = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutMenuqtx.width = width * 30 / 100;
        layoutMenuqtx.height = height * 7 / 100;
        layoutMenuqtx.gravity = Gravity.CENTER;
        mProductHeaderImage.setLayoutParams(layoutMenuqtx);

        LinearLayout.LayoutParams layoutMenuqt1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutMenuqt1.height = height * 7 / 100;
        layoutMenuqt1.leftMargin = width * 3 / 100;
        layoutMenuqt1.gravity = Gravity.CENTER | Gravity.LEFT;
        mCompanyName.setLayoutParams(layoutMenuqt1);
        mCompanyName.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams tytext1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tytext1.width = width * 60 / 100;
        tytext1.height = height * 30 / 100;
        tytext1.gravity = Gravity.CENTER;
        tytext1.topMargin = height * 5 / 100;
        mProductImage.setLayoutParams(tytext1);

        LinearLayout.LayoutParams tytext = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tytext.gravity = Gravity.CENTER;
        tytext.topMargin = height * 5 / 100;
        mProductName.setLayoutParams(tytext);
        mProductName.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams tytextds = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tytextds.gravity = Gravity.CENTER;
        tytextds.topMargin = height * 1 / 100;
        mProdValidity.setLayoutParams(tytextds);
        mProdValidity.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams webLinkParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        webLinkParama.gravity = Gravity.CENTER;
        webLinkParama.topMargin = height * 5/2 / 100;
        mWebLink.setLayoutParams(webLinkParama);

        LinearLayout.LayoutParams tytextbt = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tytextbt.width = width * 40 / 100;
        tytextbt.height = height * 6 / 100;
        tytextbt.gravity = Gravity.CENTER;
        tytextbt.topMargin = height * 5 / 100;
        mAddToCart.setLayoutParams(tytextbt);
        mAddToCart.setGravity(Gravity.CENTER);

        if (width >= 600) {
            mHeader.setTextSize(20);
            mCompanyName.setTextSize(16);
            mProductName.setTextSize(16);
            mProdValidity.setTextSize(16);
            mWebLink.setTextSize(16);
            mAddToCart.setTextSize(16);
        } else if (width > 501 && width < 600) {
            mHeader.setTextSize(19);
            mCompanyName.setTextSize(15);
            mProductName.setTextSize(15);
            mProdValidity.setTextSize(15);
            mWebLink.setTextSize(15);
            mAddToCart.setTextSize(15);
        } else if (width > 260 && width < 500) {
            mHeader.setTextSize(18);
            mCompanyName.setTextSize(14);
            mProductName.setTextSize(14);
            mProdValidity.setTextSize(14);
            mWebLink.setTextSize(14);
            mAddToCart.setTextSize(14);
        } else if (width <= 260) {
            mHeader.setTextSize(17);
            mCompanyName.setTextSize(13);
            mProductName.setTextSize(13);
            mProdValidity.setTextSize(13);
            mWebLink.setTextSize(13);
            mAddToCart.setTextSize(13);
        }
    }
}
