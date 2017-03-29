package com.wifin.kachingme.chat.chat_common_classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.registration_and_login.FreebieActivity;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import a_vcard.android.util.Log;

public class CustomPhotoGalleryActivity extends HeaderActivity {

    LinearLayout mBtnLayout;
    List<String> wordList = new ArrayList<String>();
    int width = 0;
    int height = 0;
    Cursor imagecursor;
    private GridView grdImages;
    private TextView btnSelect, mCancelBtn;
    private ImageAdapter imageAdapter;
    private String[] arrPath;
    private boolean[] thumbnailsselection;
    private int ids[];
    private int count;
    private ImageLoadingListener animateFirstListener = new FreebieActivity.AnimateFirstDisplayListener();
    private DisplayImageOptions options;

    private Bitmap[] thumbnails;

    /**
     * Overrides methods
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        // setContentView(R.layout.custom_gallery_item);
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.custom_gallery_item, vg);

        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.stub).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(this));



        grdImages = (GridView) findViewById(R.id.grdImages);
        btnSelect = (TextView) findViewById(R.id.btnSelect);
        mFooterLayout.setVisibility(View.GONE);
        mHeaderImg.setVisibility(View.GONE);
        mCancelBtn = (TextView) findViewById(R.id.btncancel);
        mBtnLayout = (LinearLayout) findViewById(R.id.btn_layout);
        mNextBtn.setVisibility(View.GONE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        Constant.screenHeight = height;
        Constant.screenWidth = width;
        screenArrangements();
//		logo.setVisibility(ImageView.GONE);
//		back.setVisibility(ImageView.VISIBLE);
//		cart.setVisibility(ImageView.INVISIBLE);
//		head.setText("Select Pictures");
        mHeading.setText("AllPhotos");
        mBackBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();

            }
        });

        final String[] columns = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        imagecursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int image_column_index = imagecursor
                .getColumnIndex(MediaStore.Images.Media._ID);
        System.out.println(" imagecursor.getCount()::::"
                + imagecursor.getCount());
        this.count = imagecursor.getCount();
        this.thumbnails = new Bitmap[this.count];
        this.arrPath = new String[this.count];
        ids = new int[count];
        System.out.println("berfore:::::::::" + arrPath.length + "    "
                + ids.length);
        this.thumbnailsselection = new boolean[this.count];
        for (int i = 0; i < this.count; i++) {
            imagecursor.moveToPosition(i);
            int id = imagecursor.getInt(image_column_index);
            ids[i] = imagecursor.getInt(image_column_index);
            int dataColumnIndex = imagecursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
                    getApplicationContext().getContentResolver(), id,
                    MediaStore.Images.Thumbnails.MICRO_KIND, null);
            arrPath[i] = imagecursor.getString(dataColumnIndex);
            wordList = Arrays.asList(arrPath);
            System.out.println("sizeeee   " + arrPath.length + wordList.size()
                    + imagecursor.getString(dataColumnIndex) + "       "
                    + ids[i]);
        }
        HashSet hs = new HashSet();
        hs.addAll(wordList);
        wordList = new ArrayList<String>();
        wordList.addAll(hs);
        System.out.println("sizeee:>>>>" + wordList.size());
        // arrPath = null;
        // ids = null;
        // for (int i = 0; i < wordList.size(); i++) {
        //
        // ids[i] = i;
        //
        // arrPath[i] = wordList.get(i);
        //
        // System.out.println("sizeeee111   " + arrPath.length
        // + wordList.size());
        // }
        imageAdapter = new ImageAdapter();
        grdImages.setAdapter(imageAdapter);
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(25);
        set.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(170);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        grdImages.setLayoutAnimation(controller);
        mCancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSelect.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                final int len = thumbnailsselection.length;
                int cnt = 0;
                int limit = 0;

                for (int i = 0; i < len; i++) {
                    if (thumbnailsselection[i]) {

                        limit++;

                    }
                }

                if (limit >= 20) {

                    Toast.makeText(getApplicationContext(),
                            "Please select Within 20 Images", Toast.LENGTH_LONG)
                            .show();

                } else {

                    String selectImages = "";
                    Constant.mSelectedImage = new ArrayList();
                    for (int i = 0; i < len; i++) {
                        if (thumbnailsselection[i]) {
                            cnt++;
                            selectImages = selectImages + arrPath[i] + "|";
                            Constant.mSelectedImage.add(arrPath[i]);

                            System.out
                                    .println("selected images path::::>>>>>>>>>"
                                            + arrPath[i]
                                            + " image  "
                                            + selectImages);
                        }
                    }

                    System.out.println("iiiiiiiiiiiiii"
                            + Constant.mSelectedImage + "    " + len);
                    Constant.mImagepath = selectImages;
                    if (cnt == 0) {
                        Toast.makeText(getApplicationContext(),
                                "Please select at least one image",
                                Toast.LENGTH_LONG).show();
                    } else {

                        Log.d("SelectedImages", selectImages);
                        Intent i = new Intent();
                        i.putExtra("data", selectImages);
                        setResult(Activity.RESULT_OK, i);
                        finish();
                    }
                }
            }
        });
    }

    private void screenArrangements() {


        LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mainLayoutParams.width = width;
        mainLayoutParams.height = height;
        //mContentLayout.setLayoutParams(mainLayoutParams);

        LinearLayout.LayoutParams gridllistParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        gridllistParams.width = width * 98 / 100;
        gridllistParams.leftMargin = width * 1 / 100;
        gridllistParams.height = height * 70 / 100;
        gridllistParams.topMargin = height * 0;
        grdImages.setLayoutParams(gridllistParams);

        LinearLayout.LayoutParams btnlayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btnlayoutParams.width = width;
        btnlayoutParams.height = height * 15 / 100;
        btnlayoutParams.gravity = Gravity.CENTER;
        mBtnLayout.setLayoutParams(btnlayoutParams);


        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btnParams.width = (int) (width * 35.5 / 100);
        btnParams.height = height * 7 / 100;
        btnParams.leftMargin = (int) (width * 12.5 / 100);
        btnParams.topMargin = width * 5 / 100;
        btnParams.gravity = Gravity.CENTER;
//		btnSelect.setLayoutParams(btnParams);
        mCancelBtn.setLayoutParams(btnParams);
//		btnSelect.setGravity(Gravity.CENTER);
        mCancelBtn.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams btnParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btnParams1.width = (int) (width * 35.5 / 100);
        btnParams1.height = height * 7 / 100;
        btnParams1.leftMargin = (int) (width * 5.0 / 100);
        btnParams1.topMargin = width * 5 / 100;
        btnParams1.gravity = Gravity.CENTER;
        btnSelect.setLayoutParams(btnParams1);
//		mCancelBtn.setLayoutParams(btnParams);
        btnSelect.setGravity(Gravity.CENTER);
//		mCancelBtn.setGravity(Gravity.CENTER);


        if (width >= 600) {

            btnSelect.setTextSize(17);
            mCancelBtn.setTextSize(17);


        } else if (width < 600 && width >= 480) {
            btnSelect.setTextSize(16);
            mCancelBtn.setTextSize(16);


        } else if (width < 480 && width >= 320) {
            btnSelect.setTextSize(14);
            mCancelBtn.setTextSize(14);


        } else if (width < 320) {
            btnSelect.setTextSize(12);
            mCancelBtn.setTextSize(12);


        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();

    }

    /**
     * Class method
     */

    /**
     * This method used to set bitmap.
     *
     * @param iv represented ImageView
     * @param id represented id
     */

    private void setBitmap(final ImageView iv, final int id) {

        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                return MediaStore.Images.Thumbnails.getThumbnail(
                        getApplicationContext().getContentResolver(), id,
                        MediaStore.Images.Thumbnails.MICRO_KIND, null);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                iv.setImageBitmap(result);
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (imagecursor != null) {
            imagecursor.close();

        }
    }

    /**
     * List adapter
     */

    public class ImageAdapter extends BaseAdapter {
        int limit = 0;
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.custom_gallery, null);
                holder.imgThumb = (ImageView) convertView
                        .findViewById(R.id.imgThumb);
                holder.chkImage = (CheckBox) convertView
                        .findViewById(R.id.chkImage);
                FrameLayout.LayoutParams gridllistParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                gridllistParams.width = width * 32 / 100;
                gridllistParams.height = width * 32 / 100;
                gridllistParams.topMargin = height * 0;
                holder.imgThumb.setLayoutParams(gridllistParams);


                FrameLayout.LayoutParams gridchecklistParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                gridchecklistParams.width = width * 7 / 100;
                gridchecklistParams.height = width * 7 / 100;
                gridchecklistParams.leftMargin = width * 24 / 100;
                holder.chkImage.setLayoutParams(gridchecklistParams);
                holder.chkImage.setGravity(Gravity.RIGHT | Gravity.TOP);

//				holder.chkImage.setPadding(width *1 /100 ,width * 1 /100 ,width * 1 /100 ,width * 1 /100 );
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.chkImage.setId(position);
            holder.imgThumb.setId(position);

            holder.chkImage.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();

                    if (thumbnailsselection[id]) {

                        cb.setChecked(false);
                        thumbnailsselection[id] = false;

                    } else {

                        cb.setChecked(true);
                        thumbnailsselection[id] = true;

                    }
                    System.out.println("checked Image:::::>>>>>"
                            + thumbnailsselection[id] + "   " + ids);
                }
            });
            holder.imgThumb.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    int id = holder.chkImage.getId();

                    System.out.println("selectimage::" + limit);

                    if (thumbnailsselection[id]) {

                        holder.chkImage.setChecked(false);
                        thumbnailsselection[id] = false;

                    } else {

                        holder.chkImage.setChecked(true);
                        thumbnailsselection[id] = true;

                    }
                    System.out.println("checked Image:::::>>>>>"
                            + thumbnailsselection[id] + "   " + ids);
                }
            });
            try {
//                setBitmap(holder.imgThumb, ids[position]);
                /*com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                        String.valueOf(
                                "file://"+arrPath[position]).replaceAll(" ", "%20"),
                        holder.imgThumb, options, animateFirstListener);*/
                holder.imgThumb.setImageBitmap(thumbnails[position]);
                System.out.println("checked Image:::::>>>>>123"
                        + thumbnailsselection + "   " + ids);
            } catch (Throwable e) {
            }

            holder.chkImage.setChecked(thumbnailsselection[position]);
            holder.id = position;
            return convertView;
        }
    }

    /**
     * Inner class
     *
     * @author tasol
     */
    class ViewHolder {
        ImageView imgThumb;
        CheckBox chkImage;
        int id;
    }
}
