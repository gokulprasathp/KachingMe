/*
* @author Sivanesan
*
* @usage -  This class is used to display the cart screen
*
*
* */

package com.wifin.kachingme.cart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.CartAdapter;
import com.wifin.kachingme.buxs.BuxSActivity;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.deals.VerifyShop;
import com.wifin.kachingme.pojo.CartAdapDto;
import com.wifin.kachingme.util.Constant;

public class CartActivity extends HeaderActivity {

    public static Context context;
    ListView summary;
    Dbhelper db;
    SharedPreferences sp;

    public static Context getContext() {
        // TODO Auto-generated method stub
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_cart);
        getSupportActionBar().hide();
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.activity_cart, vg);
        summary = (ListView) findViewById(R.id.cart_list);
        mHeading.setText("CarT");
        mNextBtn.setVisibility(View.GONE);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CartActivity.this, SliderTesting.class);
                startActivity(i);
                finish();
            }
        });
        context = CartActivity.this;
        Constant.printMsg("cart list final check" + Constant.cartfinal);
        db = new Dbhelper(getApplicationContext());
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        Constant.cartfinal.clear();
        String query = "select * from " + Dbhelper.TABLE_CART;
        getCartData(query);
        Constant.bux = sp.getLong("buxvalue", 0);
        Constant.userId = sp.getLong("uservalue", 0);

        Constant.printMsg("constan::cart" + Constant.bux + Constant.userId);

        if (Constant.cartfinal.size() > 0) {
            summary.setAdapter(new CartAdapter(this, Constant.cartfinal));
        } else {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(
                    CartActivity.this);
            builder1.setMessage("Sorry Cart Is Empty");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            final Dialog nagDialog = new Dialog(
                                    CartActivity.this);
                            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            nagDialog.getWindow().setBackgroundDrawable(
                                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            nagDialog.setCancelable(true);
                            Constant.cartboolean = false;

                            Intent i = new Intent(CartActivity.this,
                                    SliderTesting.class);
                            startActivity(i);
                            finish();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }

        mChatLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(CartActivity.this,
                        SliderTesting.class));

            }
        });
        mBuxLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(CartActivity.this,
                        BuxSActivity.class));
            }
        });
        mCartLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                startActivity(new Intent(CartActivity.this,
//                        CartActivity.class));
            }
        });
        summary.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Constant.mFreebieName = Constant.cartfinal.get(arg2).getItem();
                Constant.mFreebieMerchantName = Constant.cartfinal.get(arg2)
                        .getCompanyname();
                Constant.mFreebieId = Constant.cartfinal.get(arg2)
                        .getDeelOfferDiscountId();
                Constant.mFreebieMerchantId = Constant.cartfinal.get(arg2)
                        .getMerchantId();
                Constant.mFreeBieImgUrl = Constant.cartfinal.get(arg2)
                        .getPhotoPath();
                Constant.mFreebieShopWebsite = Constant.cartfinal.get(arg2)
                        .getMerchantName();
                Constant.printMsg("pathhh ::::::::: "
                        + Constant.cartfinal.get(arg2).getMerphotoPath());
                Constant.mFreeBieMerchantImgUrl = Constant.cartfinal.get(arg2)
                        .getMerphotoPath();
                startActivity(new Intent(CartActivity.this, VerifyShop.class));
                finish();
            }
        });
    }

    private void getCartData(String query) {
        // TODO Auto-generated method stub
        Cursor c = null;
        String buxno, deelid, discountno, itemname, mercid, off, phto, qr, prname, ty, mn, merchant_img, mphone, mcompany, mValidity;

        try {
            c = db.open().getDatabaseObj().rawQuery(query, null);

            Constant.printMsg("The count is::::::>>>> " + c.getCount());
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
            int validity = c.getColumnIndex("validity");

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
                    mValidity = c.getString(validity);

                    Constant.printMsg("dbadd::" + buxno + "  " + deelid
                            + "  " + discountno + "    " + itemname + "     "
                            + mercid + "     " + off + "     " + phto
                            + "      " + qr + "      " + ty + " merimg "
                            + merchant_img + "  " + mValidity);

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
                    p.setValidity(mValidity);
                    Constant.cartfinal.add(p);
                }

            }

        } catch (Exception e) {
            // TODO: handle exception
        } finally {

            db.close();

        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        Intent i = new Intent(CartActivity.this, SliderTesting.class);
        startActivity(i);
        finish();
    }

}
