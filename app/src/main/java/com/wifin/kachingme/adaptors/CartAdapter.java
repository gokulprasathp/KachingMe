/*
* @author Sivanesan
*
* @usage -  This class is used to display the cart list items
*
*
* */

package com.wifin.kachingme.adaptors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.cart.CartActivity;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.CartAdapDto;
import com.wifin.kachingme.registration_and_login.FreebieActivity;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Activity myacActivity;
    Context mContext = CartActivity.getContext();
    List<CartAdapDto> cartlist = new ArrayList<>();
    int width = Constant.screenWidth;
    int height = Constant.screenHeight;
    int pos;
    Dbhelper db;
    SharedPreferences sp;
    boolean insert = false;
    private ImageLoadingListener animateFirstListener = new FreebieActivity.AnimateFirstDisplayListener();
    private DisplayImageOptions options;

    public CartAdapter(Activity act, List<CartAdapDto> crlist) {
        // TODO Auto-generated constructor stub
        myacActivity = act;
        cartlist = crlist;

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));

        inflater = (LayoutInflater) myacActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.stub).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));

        db = new Dbhelper(mContext);
        sp = PreferenceManager.getDefaultSharedPreferences(myacActivity);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return cartlist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressLint({"ResourceAsColor", "DefaultLocale"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        final ViewHolder holder;
        width = Constant.screenWidth;
        height = Constant.screenHeight;

        vi = inflater.inflate(R.layout.cart_list_item, null);
        holder = new ViewHolder();

        holder.tvCartMerchantName = (TextView) vi.findViewById(R.id.tvCartMerchantName);
        holder.tvCartProductName = (TextView) vi.findViewById(R.id.tvCartProductName);
        holder.tvCartProductRegPrice = (TextView) vi.findViewById(R.id.tvCartProductRegPrice);
        holder.tvCartProductOffer = (TextView) vi.findViewById(R.id.tvCartProductOffer);
        holder.tvCartBuxsPrice = (TextView) vi.findViewById(R.id.tvCartBuxsPrice);

        holder.imgCartMerchant = (ImageView) vi.findViewById(R.id.imgCartMerchant);
        holder.imgCartBuxsProductImage = (ImageView) vi.findViewById(R.id.imgCartBuxsProductImage);

        holder.linearCartItem = (LinearLayout) vi.findViewById(R.id.linearCartListItem);
        holder.linearCartMerchant = (LinearLayout) vi.findViewById(R.id.linearCartMerchant);
        holder.linearCartProduct = (LinearLayout) vi.findViewById(R.id.linearCartProduct);
        holder.linearCartBuxs = (LinearLayout) vi.findViewById(R.id.linearCartBuxs);

        holder.imgCartBuxsProductImage.setTag(position);
        holder.imgCartMerchant.setTag(position);

        Constant.typeFace(myacActivity, holder.tvCartMerchantName);
        Constant.typeFace(myacActivity, holder.tvCartProductName);
        Constant.typeFace(myacActivity, holder.tvCartProductRegPrice);
        Constant.typeFace(myacActivity, holder.tvCartProductOffer);
        Constant.typeFace(myacActivity, holder.tvCartBuxsPrice);

        vi.setTag(holder);

        try {

            String merchantName = cartlist.get(position).getCompanyname();
            if (merchantName != null && !merchantName.isEmpty()) {

                String nameMerchantDisplay = merchantName.replace("Welcome to ", "");
                holder.tvCartMerchantName.setText(nameMerchantDisplay);

            }
            holder.tvCartProductName.setText(cartlist.get(position).getProductName());
            holder.tvCartProductRegPrice.setText("Reg.Price : " + cartlist.get(position).getOffer());
            holder.tvCartProductOffer.setText("Freebie");
            holder.tvCartBuxsPrice.setText(cartlist.get(position).getBuxs());
            ImageLoader.getInstance().displayImage(String.valueOf(cartlist.get(position).getMerphotoPath()).replaceAll(" ", "%20"), holder.imgCartMerchant, options, animateFirstListener);
            ImageLoader.getInstance().displayImage(String.valueOf(cartlist.get(position).getPhotoPath()).replaceAll(" ", "%20"), holder.imgCartBuxsProductImage, options, animateFirstListener);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return vi;
    }


    public void insertDB(ContentValues v) {
        try {
            int a = (int) db.open().getDatabaseObj().insert(Dbhelper.TABLE_RET, null, v);

            System.out.println("No of inserted rows in ret details :::::::::" + a);
        } catch (SQLException e) {
            System.out.println("Sql exception in new shop details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }
    }

    public void FetchRet() {

        Constant.redeemlist.clear();

        String mername = cartlist.get(pos).getMerchantName();
        String merbux = cartlist.get(pos).getBuxs();

        String tx, mn;
        int mid;

        Dbhelper db = new Dbhelper(mContext);
        Cursor c = null;
        try {
            c = db.open()
                    .getDatabaseObj()
                    .query(Dbhelper.TABLE_RET, null, null, null, null, null,
                            null);
            int txnm = c.getColumnIndex("name");
            int mnnm = c.getColumnIndex("bux");
            int it = c.getColumnIndex("id");

            System.out.println("The pending cart list in db ::::"
                    + c.getCount());

            if (c.getCount() > 0) {

                while (c.moveToNext()) {

                    tx = c.getString(txnm);
                    mn = c.getString(mnnm);
                    mid = c.getInt(it);
                    System.out.println("dbadd:nym:" + tx + "  " + mn + "  "
                            + mername + merbux);

                    if (tx != null) {

                        if (mername.equals(tx)) {

                            insert = true;

                            String bx = String.valueOf(Integer.valueOf(mn)
                                    + Integer.valueOf(merbux));

                            System.out.println("dbadd:nyminside:" + tx + "  "
                                    + mn + "  " + mername + "   " + merbux
                                    + "   " + bx);

                            ContentValues cv = new ContentValues();

                            cv.put("name", tx);
                            cv.put("bux", bx);

                            Update(tx, bx, mid, cv);

                            // insertDB(cv);

                        }
                    }

                }
            }
        } catch (SQLException e) {

            System.out.println("Sql exception in pending shop details ::::"
                    + e.toString());
        } finally {
            c.close();
            db.close();
        }

        if (insert == false) {

            ContentValues cv = new ContentValues();
            cv.put("name", cartlist.get(pos).getMerchantName());
            cv.put("bux", cartlist.get(pos).getBuxs());
            insertDB(cv);
        }
    }

    public void Update(String mer, String bx, int merid, ContentValues cv1) {
        // TODO Auto-generated method stub

        try {
            int a = (int) db.open().getDatabaseObj()
                    .update(Dbhelper.TABLE_RET, cv1, "id=" + merid, null);

            System.out.println("No of updated rows in ret details :::::::::"
                    + a);
        } catch (SQLException e) {
            System.out.println("Sql exception in new shop details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }

    }

    public void DbDeleteDeel() {
        // TODO Auto-generated method stub

        try {
            int a = db
                    .open()
                    .getDatabaseObj()
                    .delete(Dbhelper.TABLE_CART,
                            "deelid="
                                    + cartlist.get(pos)
                                    .getDeelOfferDiscountId(), null);
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

    public static class ViewHolder {
        public ImageView imgCartMerchant, imgCartBuxsProductImage;
        public TextView tvCartMerchantName, tvCartProductName, tvCartProductRegPrice, tvCartProductOffer, tvCartBuxsPrice;
        public LinearLayout linearCartItem, linearCartMerchant, linearCartProduct, linearCartBuxs;
    }
}