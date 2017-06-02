package com.wifin.kachingme.chat.chat_adaptors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.l4digital.fastscroll.FastScroller;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.viethoa.RecyclerViewFastScroller;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.fragments.FavouriteContacts;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.ProfileRoundImg;

import java.util.ArrayList;

/**
 * Created by siva on 14-10-2016.
 */
public class FavouriteContactsAdapter extends RecyclerView.Adapter<FavouriteContactsAdapter.VersionViewHolder>
        implements FastScroller.SectionIndexer {
    public static ImageLoader imageLoader;
    public int[] imageProfile;
    Context contextContact;
    String TAG = FavouriteContactsAdapter.class.getSimpleName();
    ArrayList<ContactsGetSet> list;
    String status_lock;
    Dbhelper dbHelper;
    Bitmap mTempIcon = null;
    Bitmap mTempIconForChat = null;
    DisplayImageOptions options;

    public FavouriteContactsAdapter(Context context, int textViewResourceId, ArrayList<ContactsGetSet> objects) {
        this.contextContact = context;
        list = objects;
        Constant.printMsg("test......" + list.size());
        dbHelper = new Dbhelper(contextContact);
        this.imageProfile = imageProfile;
        mTempIcon = BitmapFactory.decodeResource(contextContact.getResources(), R.drawable.avtar);
        mTempIconForChat = BitmapFactory.decodeResource(contextContact.getResources(), R.drawable.ic_launcher2);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(contextContact));
        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(5))
                .showStubImage(R.drawable.avtar)
                .showImageForEmptyUri(R.drawable.avtar)
                .cacheOnDisc().cacheInMemory().build();
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 80;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_card_item, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }

    @Override
    public String getSectionText(int pos) {
        if (pos < 0 || pos >= list.size())
            return null;

        String name = list.get(pos).getDisplay_name();
        if (name == null || name.length() < 1)
            return null;

        return list.get(pos).getDisplay_name().substring(0, 1);
    }

    @Override
    public void onBindViewHolder(VersionViewHolder versionViewHolder, final int position) {
//        versionViewHolder.imgCardProfileImage.setImageResource(imageProfile[i]);
//        versionViewHolder.tvCardContactName.setText(contactListName.get(i));
//        versionViewHolder.tvCardContactNumber.setText(contactListNumber.get(i));
        versionViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemSelectionProcess(position);
            }
        });
        ContactsGetSet user = list.get(position);
        Constant.printMsg("data fav......" + user.getDisplay_name());
//        try {
//            byte image_data1[] = user.getPhoto_ts();
//            if (image_data1 != null) {
//                Bitmap bmp = BitmapFactory.decodeByteArray(image_data1, 0,
//                        image_data1.length);
//                // Bitmap bmp =
//                // BitmapFactory.decodeByteArray(image_data, 0,
//                // image_data.length);
//                versionViewHolder.imgCardProfileImage.setImageBitmap(bmp);
//                FavouriteContacts.mProfileImagesList.add(bmp);
//
//            } else {
//                versionViewHolder.imgCardProfileImage.setImageDrawable(contextContact.getResources()
//                        .getDrawable(R.drawable.user_profile));
//                FavouriteContacts.mProfileImagesList.add(mTempIcon);
//
//            }
//        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//            // TODO: handle exception
//        }


        try {
            Bitmap bitmap = ((BitmapDrawable) versionViewHolder.imgCardProfileImage.getDrawable()).getBitmap();
            FavouriteContacts.mProfileImagesList.add(bitmap);
        } catch (Exception e) {
            FavouriteContacts.mProfileImagesList.add(mTempIconForChat);
        }


        if (user.getPhone_label() != null && !user.getPhone_label().isEmpty()) {
            //versionViewHolder.imgCardProfileImage.setImageBitmap(getRoundedCornerBitmap(user.getPhoto_bitmap()));
            //ProfileRoundImg mSenderImage = new ProfileRoundImg(user.getPhoto_bitmap());
            //versionViewHolder.imgCardProfileImage.setImageDrawable(mSenderImage);
            //FavouriteContacts.mProfileImagesList.add(user.getPhoto_bitmap());
            try {
                if (Connectivity.isConnected(contextContact) && imageLoader != null) {
//                    imageLoader.getDiscCache().clear();
//                    imageLoader.getMemoryCache().clear();
//                    MemoryCacheUtils.removeFromCache(user.getPhone_label(), imageLoader.getMemoryCache());
//                    DiskCacheUtils.removeFromCache(user.getPhone_label(), imageLoader.getDiskCache());
                }

                imageLoader.displayImage(user.getPhone_label(), versionViewHolder.imgCardProfileImage, options);
                //Bitmap bitmap = ((BitmapDrawable)versionViewHolder.imgCardProfileImage.getDrawable()).getBitmap();
                //FavouriteContacts.mProfileImagesList.add(user.getPhoto_bitmap());
                Constant.printMsg("position......... for contacts........" + position);
                imageLoader.loadImage(user.getPhone_label(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        super.onLoadingStarted(imageUri, view);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Constant.printMsg("position......... for onLoadingComplete........" + position);
                        try {
                            FavouriteContacts.mProfileImagesList.set(position, loadedImage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        FavouriteContacts.mProfileImagesList.add(loadedImage);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        Constant.printMsg("position......... for onLoadingFailed........" + position);
                        try {
                            FavouriteContacts.mProfileImagesList.set(position, mTempIconForChat);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        FavouriteContacts.mProfileImagesList.add(mTempIconForChat);
                        super.onLoadingFailed(imageUri, view, failReason);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ProfileRoundImg mSenderImage = new ProfileRoundImg(mTempIcon);
            versionViewHolder.imgCardProfileImage.setImageDrawable(mSenderImage);
            //versionViewHolder.imgCardProfileImage.setImageBitmap(getRoundedCornerBitmap(mTempIcon));
            FavouriteContacts.mProfileImagesList.add(mTempIconForChat);
        }
        if (user.getStatus() != null && !user.getStatus().isEmpty()) {

            try {
                versionViewHolder.tvCardContactNumber.setText(user.getStatus());
            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                // TODO: handle exceptionas
                e.printStackTrace();
            }
        } else {
            if (user.getIs_niftychat_user() != 0
                    && user.getIs_niftychat_user() == 1) {
                versionViewHolder.tvCardContactNumber.setText(R.string.hey_im_usning_niftycha);
            } else {
                versionViewHolder.tvCardContactNumber.setText(user.getNumber());
            }

        }

        versionViewHolder.tvCardContactName.setText(user.getDisplay_name());
        Constant.printMsg("siva get user statu...."
                + user.getIs_niftychat_user());
        if (user.getIs_niftychat_user() != 0
                && user.getIs_niftychat_user() == 1) {
            versionViewHolder.tvCardProfileStatus.setImageResource(R.drawable.contact_user);
        } else {
            versionViewHolder.tvCardProfileStatus.setImageResource(R.drawable.contact_invite);
        }

    }

    private String lock_status(String query) {
        // TODO Auto-generated method stub
        Cursor c = null;
        try {
            Constant.printMsg("query  " + query);
            c = dbHelper.open().getDatabaseObj().rawQuery(query, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
            c.moveToFirst();
            if (c.getCount() > 0) {
                Constant.printMsg("lock_status " + c.getString(0));
                status_lock = c.getString(0);
                Constant.printMsg("status_lock   " + status_lock);
            } else {
                status_lock = "not_lock";
            }
        } catch (SQLException e) {

        } finally {
            if (c != null)
                c.close();
            if (dbHelper != null)
                dbHelper.close();
        }
        return status_lock;
    }

    private void itemSelectionProcess(final int position) {

        try {
            Constant.mFromContactScreen = true;
            FavouriteContacts.mPosition = position;
            Constant.FROM_CHAT_SCREEN = "contact";

            String query = "select status from " + Dbhelper.TABLE_LOCK
                    + " where jid = '" + list.get(position).getJid() + "'";
            Constant.printMsg("bhaththam " + query);
            lock_status(query);
            if (status_lock.equalsIgnoreCase("lock")) {
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        contextContact);

                alert.setTitle(contextContact.getResources().getString(R.string.unlock));
                alert.setMessage(contextContact.getResources().getString(R.string.unlock) + " "
                        + list.get(position).getDisplay_name());
                final EditText input = new EditText(contextContact);
                input.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_CLASS_NUMBER);
                input.setTransformationMethod(PasswordTransformationMethod
                        .getInstance());
                alert.setView(input);

                alert.setPositiveButton(contextContact.getResources().getString(R.string.unlock),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                String value = input.getText().toString();
                                String query = "select password from "
                                        + Dbhelper.TABLE_LOCK
                                        + " where jid = '"
                                        + list.get(position).getJid()
                                        + "'";
                                Constant.printMsg("bhaththam value"
                                        + value);
                                lock_status(query);
                                if (value.equals(status_lock)) {

                                    Intent intent = new Intent(contextContact, ChatTest.class);
                                    intent.putExtra("jid",
                                            list.get(position).getJid());
                                    intent.putExtra("name",
                                            list.get(position)
                                                    .getDisplay_name());
                                    intent.putExtra("avatar",
                                            list.get(position)
                                                    .getPhoto_ts());
                                    intent.putExtra("avatar",
                                            list.get(position)
                                                    .getPhoto_ts());
                                    intent.putExtra("msg_ids", SliderTesting.msg_ids);
                                    intent.putExtra("IS_SECRET_CHAT", false);
                                    intent.putExtra("is_owner", "1");
                                    contextContact.startActivity(intent);

                                    // getActivity().finish();
                                } else {
                                    new AlertManager().showAlertDialog(
                                            contextContact,
                                            contextContact.getResources().getString(R.string.you_are_entered_incorrect_pin),
                                            true);

                                }
                            }
                        });

                alert.setNegativeButton(contextContact.getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // Canceled.

                            }
                        });

                alert.show();

            } else {
                Constant.chatlist.clear();
                if (list.get(position).getIs_niftychat_user() != 0
                        && list.get(position).getIs_niftychat_user() == 1) {

                    Constant.mSenderName = list.get(position)
                            .getDisplay_name();
                    Intent intent = new Intent(contextContact, ChatTest.class);
                    intent.putExtra("jid", list.get(position).getJid());
                    intent.putExtra("name", list.get(position)
                            .getDisplay_name());

                    intent.putExtra("avatar", list.get(position)
                            .getPhoto_ts());
                    intent.putExtra("avatar", list.get(position)
                            .getPhoto_ts());
                    intent.putExtra("msg_ids", SliderTesting.msg_ids);
                    intent.putExtra("IS_SECRET_CHAT", false);
                    intent.putExtra("is_owner", "1");
                    contextContact.startActivity(intent);
                    // getActivity().finish();
                } else {

                    Intent mailer = new Intent(Intent.ACTION_SEND);
                    mailer.setType("text/plain");
                    // mailer.setType("message/rfc822");
                    mailer.putExtra(
                            "sms_body",
                            "Check out Kaching.Me Messanger for your smartphone.\n Download it from Google play store.");
                    mailer.putExtra("address", list.get(position).getJid()
                            .split("@")[0]);

                    mailer.putExtra(Intent.EXTRA_SUBJECT, "KaChing.me");
                    mailer.putExtra(
                            Intent.EXTRA_TEXT,
                            "Hey,\n\n"
                                    + "I just downloaded Kaching.Me Messanger on my Android.\n\n"
                                    + "It is a smartphone messanger which replaces SMS. This app event lets me send pictures,video and other multimedia.\n\n"
                                    + "Kaching.Me is available for Android and there is no PIN or username to remember - it work just like SMS and uses your internet data plan.\n\n"
                                    + "Get it now from Google play and say good-bye to SMS!");

                    contextContact.startActivity(Intent.createChooser(mailer,
                            "Share via..."));
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    class VersionViewHolder extends RecyclerView.ViewHolder {
        CardView cardContactItem;

        TextView tvCardContactName, tvCardContactNumber, mOnlineStatus;
        ImageView tvCardProfileStatus;
        ImageView imgCardProfileImage;
        LinearLayout linearContListRow, linearContUserDetails;
        FrameLayout frameContUserProfile;
        View mView;

        public VersionViewHolder(View itemView) {
            super(itemView);

            cardContactItem = (CardView) itemView.findViewById(R.id.cardContactList);
            tvCardContactName = (TextView) itemView.findViewById(R.id.tvCardContactName);
            tvCardContactNumber = (TextView) itemView.findViewById(R.id.tvCardContactNumber);
            mOnlineStatus = (TextView) itemView.findViewById(R.id.contact_onLineStatus);
            tvCardProfileStatus = (ImageView) itemView.findViewById(R.id.tvCardProfileStatusImage);
            imgCardProfileImage = (ImageView) itemView.findViewById(R.id.imgCardProfileImage);
            linearContListRow = (LinearLayout) itemView.findViewById(R.id.linearContListRow);
            linearContUserDetails = (LinearLayout) itemView.findViewById(R.id.linearContUserDetails);
            frameContUserProfile = (FrameLayout) itemView.findViewById(R.id.frameContUserProfile);
            mView = (View) itemView.findViewById(R.id.contacts_view);

            try {
                Constant.typeFace(contextContact, tvCardContactName);
                Constant.typeFace(contextContact, tvCardContactNumber);
                Constant.typeFace(contextContact, mOnlineStatus);
            } catch (Exception e) {
                e.printStackTrace();
            }


            int width = Constant.screenWidth;
            int height = Constant.screenHeight;

            CardView.LayoutParams viewParama = new CardView.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            viewParama.width = width;
            viewParama.height = height * 1 / 2 / 100;
            viewParama.gravity = Gravity.BOTTOM | Gravity.CENTER;
            mView.setLayoutParams(viewParama);

            CardView.LayoutParams linearListRow = new CardView.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            linearListRow.width = width;
            linearListRow.height = (int) (height * 13.5 / 100);
            linearContListRow.setLayoutParams(linearListRow);
            linearContListRow.setGravity(Gravity.CENTER | Gravity.LEFT);

            LinearLayout.LayoutParams imageLayoutParama = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            imageLayoutParama.width = width * 15 / 100;
            imageLayoutParama.height = (int) (height * 13.5 / 100);
            imageLayoutParama.leftMargin = width * 4 / 100;
            imageLayoutParama.rightMargin = width * 4 / 100;
            frameContUserProfile.setLayoutParams(imageLayoutParama);

            FrameLayout.LayoutParams circularImage = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            circularImage.width = width * 15 / 100;
            circularImage.height = height * 9 / 100;
            circularImage.gravity = Gravity.CENTER;
            imgCardProfileImage.setLayoutParams(circularImage);
            //imgCardProfileImage.setScaleType(ImageView.ScaleType.FIT_XY);

            LinearLayout.LayoutParams rightSecondsParama = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            rightSecondsParama.width = width * 12 / 100;
            rightSecondsParama.height = height * 7 / 100;
            rightSecondsParama.leftMargin = width * 1 / 100;
            rightSecondsParama.rightMargin = width * 1 / 100;
            rightSecondsParama.gravity = Gravity.CENTER;
            tvCardProfileStatus.setLayoutParams(rightSecondsParama);

            FrameLayout.LayoutParams statusTextParama = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            statusTextParama.width = width * 5 / 100;
            statusTextParama.height = width * 5 / 100;
            statusTextParama.bottomMargin = height * 2 / 100;
            statusTextParama.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            mOnlineStatus.setLayoutParams(statusTextParama);

            LinearLayout.LayoutParams linearDetails = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            linearDetails.width = width * 58 / 100;
            linearDetails.height = height * 13 / 100;
            linearContUserDetails.setLayoutParams(linearDetails);

            LinearLayout.LayoutParams userNameParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            userNameParama.width = width * 58 / 100;
            userNameParama.height = (int) (height * (13.5 / 2) / 100);
            tvCardContactName.setLayoutParams(userNameParama);
            tvCardContactName.setGravity(Gravity.CENTER | Gravity.LEFT | Gravity.BOTTOM);
            tvCardContactName.setPadding(0, 0, 0, width * 1 / 2 / 100);

            LinearLayout.LayoutParams chatMsgParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            chatMsgParama.width = width * 57 / 100;
            chatMsgParama.height = (int) (height * (13.5 / 2) / 100);
            tvCardContactNumber.setLayoutParams(chatMsgParama);
            tvCardContactNumber.setGravity(Gravity.CENTER | Gravity.LEFT | Gravity.TOP);
            tvCardContactNumber.setPadding(0, width * 1 / 2 / 100, 0, 0);

            if (width >= 600) {
                tvCardContactName.setTextSize(19);
                tvCardContactNumber.setTextSize(16);
            } else if (width > 501 && width < 600) {
                tvCardContactName.setTextSize(17);
                tvCardContactNumber.setTextSize(14);
            } else if (width > 260 && width < 500) {
                tvCardContactName.setTextSize(15);
                tvCardContactNumber.setTextSize(12);
            } else if (width <= 260) {
                tvCardContactName.setTextSize(13);
                tvCardContactNumber.setTextSize(10);
            }
        }
    }
}
