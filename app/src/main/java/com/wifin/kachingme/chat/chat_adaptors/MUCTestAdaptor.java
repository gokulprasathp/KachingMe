//package com.wifin.kachingme.chat.chat_adaptors;
//
//import android.app.AlertDialog;
//import android.content.ActivityNotFoundException;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.database.SQLException;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.os.Looper;
//import android.preference.PreferenceManager;
//import android.provider.MediaStore;
//import android.support.v4.internal.view.SupportMenu;
//import android.support.v7.widget.RecyclerView;
//import android.text.SpannableStringBuilder;
//import android.text.method.LinkMovementMethod;
//import android.text.style.BackgroundColorSpan;
//import android.text.style.ClickableSpan;
//import android.text.style.ForegroundColorSpan;
//import android.util.SparseBooleanArray;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.MimeTypeMap;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import com.wifin.kachingme.applications.KachingMeApplication;
//import com.wifin.kachingme.chat.chat_common_classes.ContactView;
//import com.wifin.kachingme.emojicons.emojicon.EmojiconTextView;
//import com.wifin.kachingme.util.NetworkSharedPreference;
//import com.wifin.kachingme.settings.Network_Usage;
//import com.wifin.kaching.me.ui.R;
//import com.wifin.kachingme.chat.muc_chat.MUCTest;
//import com.wifin.kachingme.database.DatabaseHelper;
//import com.wifin.kachingme.database.Dbhelper;
//import com.wifin.kachingme.pojo.ContactsGetSet;
//import com.wifin.kachingme.pojo.MessageGetSet;
//import com.wifin.kachingme.util.Constant;
//import com.wifin.kachingme.util.NoUnderlineSpan;
//import com.wifin.kachingme.kaching_feature.dazz.BannerActivityChat;
//import com.wifin.kachingme.kaching_feature.dazz.BannerActivityLED;
//import com.wifin.kachingme.chat.muc_chat.OrientationGroup;
//
//import com.wifin.kachingme.services.TempConnectionService;
//import com.wifin.kachingme.util.AlertManager;
//import com.wifin.kachingme.util.AvatarManager;
//
//import com.wifin.kachingme.util.Emoji;
//import com.wifin.kachingme.util.Log;
//import com.wifin.kachingme.util.ScalingUtilities;
//import com.wifin.kachingme.util.Self_Destruct_Messages;
//import com.wifin.kachingme.util.TimeUtils;
//import com.wifin.kachingme.util.Utils;
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;
//import com.wifin.kachingme.util.RounderImageView;
//
//import org.acra.ACRAConstants;
//import org.apache.http.Header;
//import org.jivesoftware.smack.packet.Message;
//import org.jivesoftware.smack.util.stringencoder.Base64;
//import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;
//import org.jivesoftware.smackx.muc.MultiUserChat;
//import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
//import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
//import org.jivesoftware.smackx.xevent.MessageEventManager;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//
//public class
//        MUCTestAdaptor extends
//        RecyclerView.Adapter<MUCTestAdaptor.MUCChatViewHolder> {
//
//    public MUCTest mucTest;
//    public MultiUserChat muc;
//    public boolean bClick;
//    Bitmap unscaledBitmapRight;
//    boolean is_auto_dowload_image = false, is_auto_dowload_video = false,
//            is_auto_dowload_audio = false, is_auto_dowload_files = false;
//    ArrayList<String> mself_destruct_list = new ArrayList<String>();
//    String current_song = "";
//    ArrayList listTemp = new ArrayList();
//    String mAudioTagValue = "";
//    String mValue;
//    int mPressed;
//    ArrayList TempBtn = new ArrayList();
//    boolean mFirstRun = true;
//    int s = 0;
//    int mBtnClick = 0;
//    Boolean is_you;
//    double height = Constant.screenHeight;
//    double width = Constant.screenWidth;
//    Context context;
//    ArrayList<MessageGetSet> msg_list;
//    DatabaseHelper dbAdapter;
//    String jid;
//    String MEDIA_UPLOAD_URL = "";
//    int notificatiob_font_size = 12, status_font_siz = 12, msg_font_size = 16;
//    MUCChatViewHolder arg0;
//    String Normallist = "";
//    ArrayList Nemelist = new ArrayList();
//    ArrayList Meaninglist = new ArrayList();
//    SpannableStringBuilder ssb;
//    String mReplace, mRemove;
//    ArrayList<MediaPlayer> list = new ArrayList<MediaPlayer>();
//    Handler seekHandler = new Handler();
//    Runnable run;
//    long bites = 0;
//    Dbhelper db;
//    //
//    int font_size = 0;
//    int idx1;
//    int idx2;
//    ArrayList mMessegeList = new ArrayList();
//    ArrayList<ImageButton> PlayBtnlist = new ArrayList<ImageButton>();
//    NetworkSharedPreference mNetworkSharPref;
//    // ////////////////////////----Video
//    // Upload-----//////////////////////////////////////////////////////////////////////
//    ArrayList mMeaningList = new ArrayList();
//    Bitmap mSenderPhoto;
//    private SparseBooleanArray mSelectedItemsIds;
//    private List<String> replacePosition = new ArrayList<String>();
//
//    public MUCTestAdaptor(Context context, ArrayList<MessageGetSet> msg_list, String jid) {
//
//        this.context = context;
//        this.msg_list = msg_list;
//        dbAdapter = KachingMeApplication.getDatabaseAdapter();
//        this.jid = jid;
//        MEDIA_UPLOAD_URL = context.getString(R.string.webservice_host)
//                + context.getString(R.string.media_upload_webservice);
//        mNetworkSharPref = new NetworkSharedPreference(context);
//        mucTest = (MUCTest) context;
//
//        db = new Dbhelper(context);
//        SharedPreferences sharedPrefs = PreferenceManager
//                .getDefaultSharedPreferences(context);
//
//        mNetworkSharPref = new NetworkSharedPreference(context);
//
//        font_size = Integer.parseInt(sharedPrefs.getString("pref_font_size",
//                "16"));
//
//        if (font_size == 14) {
//            notificatiob_font_size = 10;
//            status_font_siz = 10;
//            msg_font_size = 14;
//        } else if (font_size == 18) {
//            notificatiob_font_size = 14;
//            status_font_siz = 14;
//            msg_font_size = 18;
//        }
//
//        dbAdapter = KachingMeApplication.getDatabaseAdapter();
//
//        is_auto_dowload_image = sharedPrefs.getBoolean(
//                "media_auto_download_images", false);
//        is_auto_dowload_video = sharedPrefs.getBoolean(
//                "media_auto_download_videos", false);
//        is_auto_dowload_audio = sharedPrefs.getBoolean(
//                "media_auto_download_audio", false);
//        is_auto_dowload_files = sharedPrefs.getBoolean(
//                "media_auto_download_files", false);
//
//
//        new File(Constant.local_image_dir).mkdirs();
//        new File(Constant.local_video_dir).mkdirs();
//        new File(Constant.local_audio_dir).mkdirs();
//        new File(Constant.local_files_dir).mkdirs();
//        mSelectedItemsIds = new SparseBooleanArray();
//
//
//        Constant.printMsg("MUCTest adapter item count constructor :" + msg_list.size());
//
//        try {
//            byte[] img_byte = KachingMeApplication.getAvatar();
//            if (img_byte != null) {
//                mSenderPhoto = BitmapFactory.decodeByteArray(
//                        img_byte, 0, img_byte.length);
//                mSenderPhoto = new AvatarManager()
//                        .roundCornerImage(mSenderPhoto, 180);
//
//                if (mSenderPhoto.getByteCount() < 5) {
//
//                    Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.avtar);
//                    mSenderPhoto = largeIcon;
//
//                }
//
////                            arg0.mRightSenderImage.setImageBitmap(new AvatarManager()
////                                    .roundCornerImage(bmp, 180));
//            }
//        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//
//        }
//
//    }
//
//    public MUCTestAdaptor() {
//
//    }
//
//    @Override
//    public MUCChatViewHolder onCreateViewHolder(
//            ViewGroup parent, int arg1) {
//        // TODO Auto-generated method stub
//
//        Constant.printMsg("MUCTest adapter item count OnCreatView :" + msg_list.size());
//
//        View viewChat = LayoutInflater.from(parent.getContext()).inflate(
//                R.layout.muc_test_listitem, parent, false);
//        MUCChatViewHolder chatViewHolder = new MUCChatViewHolder(
//                viewChat);
//        return chatViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(final MUCChatViewHolder arg0, final int arg1) {
//        Constant.printMsg("MUCTest adapter :" + msg_list.get(arg1).getMedia_wa_type().toString());
//
//        // Check for long press multi select options...
//        arg0.mRightChatLayout.setBackgroundColor(mSelectedItemsIds.get(arg1) ? Color.parseColor("#42f4f1") : Color.parseColor("#B3FFFFFF"));
//       /* arg0.mRightFrameLayout.setBackgroundColor(mSelectedItemsIds.get(arg1) ? Color.parseColor("#42f4f1") : Color.parseColor("#B3FFFFFF"));
//        arg0.mRightChatBackground.setBackgroundColor(mSelectedItemsIds.get(arg1) ? Color.parseColor("#42f4f1") : Color.parseColor("#e0e0e0"));
//        arg0.mRightChatStatusLayout.setBackgroundColor(mSelectedItemsIds.get(arg1) ? Color.parseColor("#42f4f1") : Color.parseColor("#e0e0e0"));
//      */  arg0.mLeftChatLayout.setBackgroundColor(mSelectedItemsIds.get(arg1) ? Color.parseColor("#42f4f1") : Color.parseColor("#E6FFFFFF"));
//       /* arg0.mLeftBackgroundLayout.setBackgroundColor(mSelectedItemsIds.get(arg1) ? Color.parseColor("#42f4f1") : Color.parseColor("#e0e0e0"));
//        arg0.mLeftChatStatusLayout.setBackgroundColor(mSelectedItemsIds.get(arg1) ? Color.parseColor("#42f4f1") : Color.parseColor("#e0e0e0"));
//        if(mSelectedItemsIds.get(arg1))
//        {
//            arg0.mLeftChatTip.setBackgroundResource(R.drawable.left_chat_tip_green);
//            arg0.mRightChatTipImg.setBackgroundResource(R.drawable.chat_tip_green);
//        }else
//        {
//            arg0.mLeftChatTip.setBackgroundResource(R.drawable.left_chat_tip);
//            arg0.mRightChatTipImg.setBackgroundResource(R.drawable.chat_tip);
//        }*/
//        this.arg0 = arg0;
//
//        MessageGetSet msg = msg_list.get(arg1);
//        String fromuser = msg.getKey_remote_jid();
//
//        if (msg.getRemote_resource().equals(
//                KachingMeApplication.getUserID() + KachingMeApplication.getHost())) {
//            is_you = true;
//        } else {
//            is_you = false;
//        }
//
//
//        if (Integer.parseInt(msg.getMedia_wa_type()) > 6 && Integer.parseInt(msg.getMedia_wa_type()) < 12) {
//
//
//            arg0.mRightChatLayout.setVisibility(View.GONE);
//            arg0.mLeftChatLayout.setVisibility(View.GONE);
//
//            arg0.mRightImageChatLayout.setVisibility(View.GONE);
//            arg0.mRightVideoChatLayout.setVisibility(View.GONE);
//            arg0.mRightContactChatLayout.setVisibility(View.GONE);
//            arg0.mRightFileChatLayout.setVisibility(View.GONE);
//            arg0.mRightAudioChatLayout.setVisibility(View.GONE);
//            arg0.mRightChatTipImg.setVisibility(View.GONE);
//            arg0.mRightSenderImage.setVisibility(View.GONE);
//            arg0.mLeftSenderImage.setVisibility(View.GONE);
//            arg0.mLeftChatTip.setVisibility(View.GONE);
//
//            arg0.mTxtDate.setVisibility(View.GONE);
////            lay_date.setVisibility(View.GONE);
//            arg0.mTxtNotice.setVisibility(View.VISIBLE);
//            String name = dbAdapter.getDisplay_name(msg.getRemote_resource());
//
//            if (name == null) {
//                name = msg.getRemote_resource().split("@")[0];
//            }
//
//            if (Integer.parseInt(msg.getMedia_wa_type()) == 7) {
//
//                if (is_you) {
//                    arg0.mTxtNotice.setText(String.format(context.getResources()
//                            .getString(R.string.you_added), Utils
//                            .getDisplayName(dbAdapter, msg.getData())));
//                } else if (msg.getData().equals(KachingMeApplication.getjid())) {
//                    arg0.mTxtNotice.setText(String.format(context.getResources()
//                            .getString(R.string.added_you), Utils
//                            .getDisplayName(dbAdapter, msg.getRemote_resource())));
//                } else {
//                    arg0.mTxtNotice.setText(String.format(context.getResources()
//                            .getString(R.string.added), Utils.getDisplayName(
//                            dbAdapter, msg.getRemote_resource()), Utils.getDisplayName(
//                            dbAdapter, msg.getData())));
//                }
//
//            } else if (Integer.parseInt(msg.getMedia_wa_type()) == 8) {
//
//                // txt_notice.setText(name +
//                // " "+context.getResources().getString(R.string.left_group));
//                if (is_you && msg.getData().equals(KachingMeApplication.getjid())) {
//                    arg0.mTxtNotice.setText(context.getResources().getString(
//                            R.string.you_left));
//                } else if (msg.getData().equals(msg.getRemote_resource())) {
//                    arg0.mTxtNotice.setText(String.format(context.getResources()
//                            .getString(R.string.abc_left), Utils
//                            .getDisplayName(dbAdapter, msg.getRemote_resource())));
//                } else if (is_you) {
//                    arg0.mTxtNotice.setText(String.format(context.getResources()
//                            .getString(R.string.you_removed), Utils
//                            .getDisplayName(dbAdapter, msg.getData())));
//                } else if (msg.getData().equals(KachingMeApplication.getjid())) {
//                    arg0.mTxtNotice.setText(String.format(context.getResources()
//                            .getString(R.string.removed_you), Utils
//                            .getDisplayName(dbAdapter, msg.getRemote_resource())));
//                } else {
//                    arg0.mTxtNotice.setText(String.format(context.getResources()
//                            .getString(R.string.removed), Utils.getDisplayName(
//                            dbAdapter, msg.getRemote_resource()), Utils.getDisplayName(
//                            dbAdapter, msg.getData())));
//                }
//
//            } else if (Integer.parseInt(msg.getMedia_wa_type()) == 10) {
//                if (is_you) {
//                    arg0.mTxtNotice.setText(String.format(context
//                                    .getString(R.string.you_changed_grop_subject_to),
//                            Utils.getDisplayName(dbAdapter, msg.getData())));
//                } else {
//                    arg0.mTxtNotice.setText(String.format(
//                            context.getString(R.string.changed_grop_subject),
//                            Utils.getDisplayName(dbAdapter, msg.getRemote_resource()),
//                            Utils.getDisplayName(dbAdapter, msg.getData())));
//                }
//
//
//            } else if (Integer.parseInt(msg.getMedia_wa_type()) == 11) {
//                if (is_you) {
//                    arg0.mTxtNotice.setText(context
//                            .getString(R.string.you_changed_grop_icon));
//                } else {
//                    arg0.mTxtNotice.setText(String.format(
//                            context.getString(R.string.changed_grop_icon),
//                            Utils.getDisplayName(dbAdapter, msg.getRemote_resource())));
//                }
//
//            } else if (Integer.parseInt(msg.getMedia_wa_type()) == 9) {
//                if (is_you) {
//                    arg0.mTxtNotice.setText(String.format(context.getResources()
//                            .getString(R.string.you_created_grou), msg.getData()));
//                } else {
//                    arg0.mTxtNotice.setText(String.format(context.getResources()
//                            .getString(R.string.created_group), name, msg.getData()));
//                }
//            }
//
//        } else {
//            arg0.mTxtNotice.setVisibility(View.GONE);
//            arg0.mTxtDate.setVisibility(View.GONE);
//            arg0.mLeftChatLayout.setVisibility(View.GONE);
//            arg0.mRightChatLayout.setVisibility(View.GONE);
//            arg0.mRightImageChatLayout.setVisibility(View.GONE);
//            arg0.mRightVideoChatLayout.setVisibility(View.GONE);
//            arg0.mRightContactChatLayout.setVisibility(View.GONE);
//            arg0.mRightFileChatLayout.setVisibility(View.GONE);
//            arg0.mRightAudioChatLayout.setVisibility(View.GONE);
//            arg0.mRightChatText.setVisibility(View.GONE);
//            arg0.mRightTextLayout.setVisibility(View.GONE);
//
//
//            // --------------------Right ----------------
//            if (msg_list.get(arg1).getKey_from_me() != 1) {
//
//                arg0.mRightChatLayout.setVisibility(View.VISIBLE);
//                arg0.mRightChatTipImg.setVisibility(View.VISIBLE);
//                arg0.mRightSenderImage.setVisibility(View.VISIBLE);
//                arg0.mLeftSenderImage.setVisibility(View.GONE);
//                arg0.mLeftChatTip.setVisibility(View.GONE);
//
//
//                // --------------------Right Text Chat----------------
//                if (Integer.parseInt(msg.getMedia_wa_type()) == 0) {
//
//
//                    arg0.mRightChatText.setVisibility(View.VISIBLE);
//                    arg0.mRightTextLayout.setVisibility(View.VISIBLE);
//                    arg0.mRightChatText.setTag(arg1);
//
//
//                    arg0.mRightChatText.setHighlightColor(Color.TRANSPARENT);
//                    arg0.mRightChatText.setMovementMethod(LinkMovementMethod
//                            .getInstance());
//                    arg0.mRightChatText.setText(new Emoji(context).getSmiledText(msg_list.get(arg1).getData().toString()));
//
//                    String text = msg_list.get(arg1).getData().toString();
//
//                    if (text.length() > 2) {
//
//                        char q = text.charAt(0);
//                        char p = text.charAt(1);
//
//                        Constant.printMsg("MUC text msg Right 1:" + q + "  " + p);
//
//                        if (q == '<') {
//
//                            if (p == '-') {
//                                arg0.mRightChatText.setHighlightColor(Color.TRANSPARENT);
//                                arg0.mRightChatText.setMovementMethod(LinkMovementMethod
//                                        .getInstance());
//                                //
//
//                                myMethod(text.substring(2));
//                                arg0.mRightChatText.setText(
//                                        addClickablePart(this.Normallist, arg0.mRightChatText),
//                                        TextView.BufferType.SPANNABLE);
//                            } else {
//                                arg0.mRightChatText.setHighlightColor(Color.TRANSPARENT);
//                                arg0.mRightChatText.setMovementMethod(LinkMovementMethod
//                                        .getInstance());
//                                arg0.mRightChatText.setText(new Emoji(context).getSmiledText(msg_list.get(arg1).getData().toString()));
//                            }
//
//                        }
//
//                    }
//
//
//                    EmojiconTextView txt_msg = arg0.mRightChatText;
//                    mValue = msg_list.get(arg1).getData().toString();
//
//                    String[] words = mValue.split("\\s+");
//
//
//                    if (text.length() > 3) {
//
//                        char s = text.charAt(0);
//                        char s1 = text.charAt(1);
//                        char s2 = text.charAt(2);
//                        txt_msg.setTypeface(null, Color.BLACK);
//
//                        Constant.printMsg("MUC text msg Right 2:" + s + "" + s1 + "  " + s2);
//
//                        txt_msg.setBackground(null);
//                        if (s == '<') {
//
//                            if (s1 == 'b' && s2 == '>') {
//
//                                txt_msg.setTextColor(Color.GREEN);
//                                txt_msg.setText(text.substring(3));
//                                txt_msg.setTypeface(null, Color.GREEN);
//
//                                txt_msg.setBackground(null);
//                                txt_msg.setMinimumWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
//                                txt_msg.setGravity(Gravity.CENTER
//                                        | Gravity.LEFT);
//                                txt_msg.setTextSize(20);
//                                txt_msg.setEmojiconSize(33);
//                                txt_msg.setTypeface(null, Typeface.NORMAL);
//
//                                FrameLayout.LayoutParams bubbleImgParams = new FrameLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                                bubbleImgParams.gravity = Gravity.RIGHT;
////                                bubbleImgParams.rightMargin = Constant.screenWidth * 15 / 100;
//
//                                txt_msg.setLayoutParams(bubbleImgParams);
//
//                            } else if (s1 == 'z' && s2 == '>') {
//
//                                txt_msg.setTextColor(Color.RED);
//                                txt_msg.setText(text.substring(3));
//                                txt_msg.setTypeface(null, Color.RED);
//
//                                txt_msg.setBackground(null);
//                                txt_msg.setMinimumWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
//                                txt_msg.setGravity(Gravity.CENTER
//                                        | Gravity.LEFT);
//                                txt_msg.setTextSize(20);
//                                txt_msg.setEmojiconSize(33);
//                                String dazzle = text.substring(3)
//                                        .toString();
//                                String[] parts = dazzle.split("-");
//
//                                String part1 = parts[0];
//                                String part2 = parts[1];
//                                txt_msg.setText(part1);
//                                txt_msg.setTypeface(null, Typeface.NORMAL);
//
//                                LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                                bubbleImgParams.gravity = Gravity.RIGHT;
////                                bubbleImgParams.rightMargin = Constant.screenWidth * 15 / 100;
//
//                                txt_msg.setLayoutParams(bubbleImgParams);
//
//                                // Intent intentt = new Intent(context,
//                                // KachingmeWebView.class);
//                                // context.startActivity(intentt);
//
//                            } else if (s1 == 'l' && s2 == '>') {
//
//                                txt_msg.setTextColor(Color.YELLOW);
//                                txt_msg.setText(text.substring(3));
//
//                                OrientationGroup.mZzleTextor = txt_msg
//                                        .getText().toString();
//                                txt_msg.setTypeface(null, Color.YELLOW);
//
//                                txt_msg.setBackground(null);
//                                txt_msg.setGravity(Gravity.CENTER
//                                        | Gravity.LEFT);
//                                txt_msg.setTextSize(20);
//                                txt_msg.setEmojiconSize(33);
//                                // HorizonalSlideshow.mZzleTextor = txt_msg
//                                // .getText().toString();
//                                // if (mZzleshow == true) {
//                                // mZzleshow = false;
//                                // Intent intent = new Intent(context,
//                                // KachingmeWebView.class);
//                                // context.startActivity(intent);
//                                // } else {
//                                // mZzleshow = true;
//                                // }
//                                txt_msg.setTypeface(null, Typeface.NORMAL);
//
//                                LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                                bubbleImgParams.gravity = Gravity.RIGHT;
////                                bubbleImgParams.rightMargin = Constant.screenWidth * 15 / 100;
//
//                                txt_msg.setLayoutParams(bubbleImgParams);
//
//                            } else if (s1 == 'x' && s2 == '>') {
//                                String dazzle = text.substring(3)
//                                        .toString();
//                                String[] parts = dazzle.split("-");
//                                String part1 = parts[0];
//                                String part2 = parts[1];
//                                String part3 = parts[2];
//                                String part4 = parts[3];
//                                String part5 = parts[4];
//
//                                Constant.printMsg("nymss::>>>" + part1
//                                        + "  " + part2 + "  " + part3
//                                        + "  " + part4 + "  " + part5);
//                                txt_msg.setText(part5);
//
//                                txt_msg.setTextColor(Color.BLUE);
//                                txt_msg.setText(part5);
//
////                                ZzlePreviewGroup.mZzleTextor = txt_msg
////                                        .getText().toString();
//                                txt_msg.setTypeface(null, Color.BLUE);
//
//                                txt_msg.setBackground(null);
//                                txt_msg.setGravity(Gravity.CENTER
//                                        | Gravity.LEFT);
//                                txt_msg.setTextSize(20);
//                                txt_msg.setEmojiconSize(33);
//                                txt_msg.setTypeface(null, Typeface.NORMAL);
//
//                                LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                                bubbleImgParams.gravity = Gravity.RIGHT;
////                                bubbleImgParams.rightMargin = Constant.screenWidth * 15 / 100;
//
//                                txt_msg.setLayoutParams(bubbleImgParams);
//
//
//                            } else if (s1 == 's' && s2 == '>') {
//                                String self_destruct = text.substring(3)
//                                        .toString();
//                                String[] parts = self_destruct.split("-");
//                                String part1 = parts[0];
//                                String part2 = parts[1];
//                                txt_msg.setText(part2);
//                                txt_msg.setTextColor(Color.BLACK);
//                                txt_msg.setTypeface(null, Color.BLACK);
//                                txt_msg.setBackground(null);
//                                txt_msg.setMinimumWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
//                                txt_msg.setGravity(Gravity.CENTER
//                                        | Gravity.LEFT);
//                                ArrayList<String> jid_list = new ArrayList<String>();
//                                for (int i = 0; i < parts.length; i++) {
//
//                                    if (i != 0 && i != 1) {
//                                        jid_list.add(parts[i]);
//
//                                    }
//
//                                    Constant.printMsg("jid_list ::::::: "
//                                            + jid_list + "    " + i);
//                                }
//
//                                mself_destruct_list.add(String.valueOf(arg1));
//                                Constant.mself_id = arg1;
//
//                                dbAdapter = KachingMeApplication
//                                        .getDatabaseAdapter();
//                                txt_msg.setTypeface(null, Typeface.NORMAL);
//
//                                LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                                bubbleImgParams.gravity = Gravity.RIGHT;
////                                bubbleImgParams.rightMargin = Constant.screenWidth * 15 / 100;
//
//                                txt_msg.setLayoutParams(bubbleImgParams);
//
//                                final MessageGetSet selectedItem;
//
//                                try {
//
//                                    if (Constant.msg_list_adapter.size() == Constant.mself_id) {
//
//                                        Constant.printMsg("part tst::::>>>>>>");
//                                    } else {
//                                        selectedItem = Constant.msg_list_adapter
//                                                .get(Constant.mself_id);
//                                        Constant.printMsg("part tst111::::>>>>>>>>"
//                                                + selectedItem
//                                                .get_id()
//                                                + "     "
//                                                + selectedItem
//                                                .getIs_sec_chat()
//                                                + "   "
//                                                + part1
//                                                + "    "
//                                                + KachingMeApplication
//                                                .getjid());
//
//                                        new Self_Destruct_Messages(context)
//                                                .setDestruct(
//                                                        ""
//                                                                + selectedItem
//                                                                .get_id(),
//                                                        Integer.valueOf(part1),
//                                                        KachingMeApplication
//                                                                .getjid());
//
//                                    }
//                                } catch (Exception e) {
//                                    // TODO: handle exception
//                                }
//                            } else if (s1 == 'k' && s2 == '>') {
//
//                                String kons_array = text.substring(3)
//                                        .toString();
//                                String[] parts = kons_array.split("-");
//                                final String part1 = parts[0];
//                                String part2 = parts[1];
//                                final String part3 = parts[2];
//
//                                Constant.printMsg("testststststtstststskkkkkk" + part1 + part2 + part3);
//                                String bubblecolor = part2;
//                                String mShape = part1.trim();
//                                if (!bubblecolor.equalsIgnoreCase("")) {
//                                    switch (bubblecolor) {
//                                        case "Blue":
//                                            if (mShape.equalsIgnoreCase("oval_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.blue_one);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cross_rect_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.blue_two);
//                                            }
//                                            if (mShape.equalsIgnoreCase("oval_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.blue_three);
//                                            }
//                                            if (mShape.equalsIgnoreCase("rect_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.blue_four);
//                                            }
//                                            if (mShape.equalsIgnoreCase("rect_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.blue_five);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cross_rect_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.blue_six);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cloud_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.blue_seven);
//                                            }
//                                            break;
//                                        case "Green":
//                                            if (mShape.equalsIgnoreCase("oval_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.green_one);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cross_rect_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.green_two);
//                                            }
//                                            if (mShape.equalsIgnoreCase("oval_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.green_three);
//                                            }
//                                            if (mShape.equalsIgnoreCase("rect_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.green_four);
//                                            }
//                                            if (mShape.equalsIgnoreCase("rect_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.green_five);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cross_rect_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.green_six);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cloud_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.green_seven);
//                                            }
//                                            break;
//                                        case "Pink":
//                                            if (mShape.equalsIgnoreCase("oval_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.pink_one);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cross_rect_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.pink_two);
//                                            }
//                                            if (mShape.equalsIgnoreCase("oval_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.pink_three);
//                                            }
//                                            if (mShape.equalsIgnoreCase("rect_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.pink_four);
//                                            }
//                                            if (mShape.equalsIgnoreCase("rect_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.pink_five);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cross_rect_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.pink_six);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cloud_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.pink_seven);
//                                            }
//                                            break;
//                                    }
//
//                                }
//
//                                txt_msg.setText(part3);
//
//
//                                LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                                bubbleImgParams.width = Constant.screenWidth * 25 / 100;
//                                bubbleImgParams.height = Constant.screenHeight * 8 / 100;
//                                bubbleImgParams.gravity = Gravity.RIGHT;
////                                    bubbleImgParams.rightMargin = Constant.screenWidth * 15 / 100;
////                                bubbleImgParams.topMargin = Constant.screenWidth * 5 / 100;
//
//                                txt_msg.setLayoutParams(bubbleImgParams);
//                                txt_msg.setGravity(Gravity.CENTER);
//                                txt_msg.setTypeface(null, Typeface.BOLD);
//                                txt_msg.setTextColor(Color.WHITE);                                if (Constant.screenWidth >= 600) {
//                                    Constant.printMsg("konss caleedd 1" + part3.length());
////                                        if (part3.length() == 4) {
//                                    Constant.printMsg("konss caleedd 6771 :::::" + part3.length() + "  "
//                                            + Constant.screenHeight + "  " + Constant.screenWidth);
//
//
//                                    txt_msg.setEmojiconSize(40);
//
//
//                                } else if (Constant.screenWidth > 501
//                                        && Constant.screenWidth < 600) {
//                                    Constant.printMsg("konss caleedd 890");
//
//                                    txt_msg.setEmojiconSize(39);
//
//
//
//                                } else if (Constant.screenWidth > 260
//                                        && Constant.screenWidth < 500) {
//                                    Constant.printMsg("caleedd 3");
//
//                                    txt_msg.setEmojiconSize(38);
//
//                                } else if (Constant.screenWidth <= 260) {
//                                    Constant.printMsg("caleedd 4");
//
//
//                                    txt_msg.setEmojiconSize(36);
//
//
//                                }
//
//                            } else {
//
//
//                                txt_msg.setTextColor(Color.BLACK);
//                                txt_msg.setTypeface(null, Color.BLACK);
//                                txt_msg.setBackground(null);
//                                txt_msg.setMinimumWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
//                                txt_msg.setGravity(Gravity.CENTER
//                                        | Gravity.LEFT);
//                                txt_msg.setTextSize(20);
//                                txt_msg.setEmojiconSize(33);
//                                txt_msg.setTypeface(null, Typeface.NORMAL);
//
//                                LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                                bubbleImgParams.gravity = Gravity.RIGHT;
////                                bubbleImgParams.rightMargin = Constant.screenWidth * 15 / 100;
//
//                                txt_msg.setLayoutParams(bubbleImgParams);
//
//                            }
//                        } else {
//                            txt_msg.setTextColor(Color.BLACK);
//                            txt_msg.setTypeface(null, Color.BLACK);
//                            txt_msg.setBackground(null);
//                            txt_msg.setMinimumWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
//                            txt_msg.setGravity(Gravity.CENTER
//                                    | Gravity.LEFT);
//                            txt_msg.setTextSize(20);
//                            txt_msg.setEmojiconSize(33);
//                            txt_msg.setTypeface(null, Typeface.NORMAL);
//
//                            LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                                    LinearLayout.LayoutParams.WRAP_CONTENT);
//                            bubbleImgParams.gravity = Gravity.RIGHT;
////                            bubbleImgParams.rightMargin = Constant.screenWidth * 15 / 100;
//
//                            txt_msg.setLayoutParams(bubbleImgParams);
//
//                        }
//
//                    } else {
//                        txt_msg.setTextColor(Color.BLACK);
//                        txt_msg.setTypeface(null, Color.BLACK);
//                        txt_msg.setBackground(null);
//                        txt_msg.setMinimumWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
//                        txt_msg.setGravity(Gravity.CENTER
//                                | Gravity.LEFT);
//                        txt_msg.setTextSize(20);
//                        txt_msg.setEmojiconSize(33);
//                        txt_msg.setTypeface(null, Typeface.NORMAL);
//
//                        LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                LinearLayout.LayoutParams.WRAP_CONTENT,
//                                LinearLayout.LayoutParams.WRAP_CONTENT);
//                        bubbleImgParams.gravity = Gravity.RIGHT;
////                        bubbleImgParams.rightMargin = Constant.screenWidth * 15 / 100;
//
//                        txt_msg.setLayoutParams(bubbleImgParams);
//
//                    }
//
//                    txt_msg.setTag(arg1);
//
//                    txt_msg.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            // TODO Auto-generated method stub
//                            // Constant.printMsg("clicked syms:::::>>>>>>");
//                            EmojiconTextView txt_msg = (EmojiconTextView) v
//                                    .findViewById(R.id.right_chat_text);
//
//                            boolean toggle = true;
//
//                            String text = txt_msg.getText().toString();
//
//
//                            if (txt_msg.getCurrentTextColor() == -65536) {
//
//                                Constant.zzle = false;
//
//                                BannerActivityLED.mZzleText = txt_msg
//                                        .getText().toString();
//                                Intent intent = new Intent(context,
//                                        BannerActivityLED.class);
//                                context.startActivity(intent);
//                            }
//
//                            if (txt_msg.getCurrentTextColor() == -256) {
//
//                                OrientationGroup.mZzleTextor = txt_msg
//                                        .getText().toString();
//
//                                // HorizonalSlideshow.mZzleTextor = txt_msg
//                                // .getText().toString();
//
//                                Intent intent = new Intent(context,
//                                        OrientationGroup.class);
//                                context.startActivity(intent);
//
//                                // }
//
//                            }
//                            if (txt_msg.getCurrentTextColor() == -16776961) {
//
//                                Constant.mZzleText = txt_msg.getText()
//                                        .toString();
//
//                                BannerActivityChat.mZzleText = txt_msg
//                                        .getText().toString();
//
//                                Intent intent = new Intent(context,
//                                        BannerActivityChat.class);
//                                context.startActivity(intent);
//
//                            }
//                        }
//
//                    });
//
//
//                    // Set Image view
////                    try {
////                        byte[] img_byte = KachingMeApplication.getAvatar();
////                        if (img_byte != null) {
////                            Bitmap bmp = BitmapFactory.decodeByteArray(
////                                    img_byte, 0, img_byte.length);
////                            arg0.mRightSenderImage.setImageBitmap(new AvatarManager()
////                                    .roundCornerImage(bmp, 180));
////                        }
////                    } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
////
////                    }
//
//                }
//                //----------------------Right Image Chat----------------
//                else if (Integer.parseInt(msg.getMedia_wa_type()) == 1) {
//                    arg0.mRightImageChatLayout.setVisibility(View.VISIBLE);
//                    arg0.mRightImageChat.setVisibility(View.VISIBLE);
//
//
//                    // Set timestamp
//                    Date date = new Date();
//                    date.setTime(msg_list.get(arg1).getTimestamp());
//                    SimpleDateFormat time_format = new SimpleDateFormat(
//                            "hh:mm a");
//
//                    arg0.mRightChatTimeTxt.setText(time_format.format(date));
//
//                    Constant.printMsg("MUC image : " + msg.getMedia_name());
//
//                    File file = null;
//                    try {
//                        file = new File(Constant.local_image_dir
//                                + msg.getMedia_name());
//                    } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//
//                    }
//
//
//                    String values = msg.get_id() + "," + fromuser;
//                    arg0.mRightImageUploadImage.setTag(values);
//                    arg0.mRightImageUploadImage.setVisibility(View.GONE);
//                    arg0.mRightImageProgressUpload.setVisibility(View.GONE);
//
//                    if (file != null) {
//
//                        if (unscaledBitmapRight == null)
//                            unscaledBitmapRight = ScalingUtilities
//                                    .decodeFile(Constant.local_image_dir
//                                            + msg.getMedia_name(), (int) width * 57 / 100, (int) height * 30 / 100);
//
//                        Log.d("Image Width",
//                                "Image Width::" + (int) width * 50 / 100 + " layout::"
//                                        + (int) height * 30 / 100
//                                        + " image view::"
//                                        + msg.getStatus()
//                                        + " image view height::"
//                                        + msg.getNeeds_push()
//                                        + "bitmap widh::"
//                                        + unscaledBitmapRight.getWidth());
//
//                        arg0.mRightImageChat.setImageBitmap(unscaledBitmapRight);
//                        arg0.mRightImageChat.setTag(Constant.local_image_dir
//                                + msg.getMedia_name());
//
//                        if (msg.getStatus() == 3
//                                && msg.getNeeds_push() == 1) {
//                            arg0.mRightImageUploadImage.setVisibility(View.GONE);
//                            arg0.mRightImageProgressUpload
//                                    .setVisibility(View.VISIBLE);
//                            new uploa_image().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,String.valueOf(msg.get_id()),
//                                    fromuser);
//                        } else if (msg.getMedia_url() == null
//                                && msg.getStatus() == 2) {
//                            arg0.mRightImageUploadImage.setVisibility(View.GONE);
//                            arg0.mRightImageProgressUpload
//                                    .setVisibility(View.VISIBLE);
//
//                        } else if (msg.getStatus() == 2
//                                && msg.getNeeds_push() == 1) {
//                            arg0.mRightImageUploadImage.setVisibility(View.GONE);
//                            arg0.mRightImageProgressUpload.setVisibility(View.GONE);
//
//                        }
//                    } else {
//                        arg0.mRightImageUploadImage.setVisibility(View.GONE);
//                        arg0.mRightImageProgressUpload.setVisibility(View.GONE);
//                        byte[] image_data = msg.getRow_data();
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(
//                                image_data, 0, image_data.length);
//                        arg0.mRightImageChat.setImageBitmap(bitmap);
//                        arg0.mRightImageChat.setTag(null);
//                    }
//
//                    arg0.mRightImageChat.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            if (v.getTag() != null) {
//                                String path = v.getTag().toString();
//                                File f = new File(path);
//                                Uri uri = Uri.fromFile(f);
//
//                                Intent intent = new Intent(
//                                        Intent.ACTION_VIEW);
//                                String mime = "*/*";
//                                MimeTypeMap mimeTypeMap = MimeTypeMap
//                                        .getSingleton();
//                                if (mimeTypeMap.hasExtension(mimeTypeMap
//                                        .getFileExtensionFromUrl(uri
//                                                .toString())))
//                                    mime = mimeTypeMap
//                                            .getMimeTypeFromExtension(mimeTypeMap
//                                                    .getFileExtensionFromUrl(uri
//                                                            .toString()));
//                                intent.setDataAndType(uri, mime);
//                                context.startActivity(intent);
//
//                            }
//
//                        }
//                    });
//
//                    final ProgressBar progressBar = arg0.mRightImageProgressUpload;
//                    final ImageView imgView = arg0.mRightImageUploadImage;
//
//                    arg0.mRightImageUploadImage
//                            .setOnClickListener(new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//                                    String[] val = v.getTag().toString()
//                                            .split(",");
//                                    new uploa_image().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,val[0],
//                                            val[1]);
//
//
//                                    progressBar
//                                            .setVisibility(View.VISIBLE);
//                                    imgView
//                                            .setVisibility(View.GONE);
//
//
//                                }
//                            });
//
//                } else if (Integer.parseInt(msg.getMedia_wa_type()) == 12) {
//
//                    File file = null;
//                    try {
//                        file = new File(Constant.local_image_dir
//                                + msg.getMedia_name());
//                    } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//
//                    }
//
//
//                    String values = msg.get_id() + "," + fromuser;
//                    arg0.mRightImageProgressUpload.setTag(values);
//                    arg0.mRightImageProgressUpload.setVisibility(View.GONE);
//                    arg0.mRightImageProgressUpload.setVisibility(View.GONE);
//                    if (file != null) {
//                        Bitmap unscaledBitmap = ScalingUtilities
//                                .decodeFile(Constant.local_image_dir
//                                        + msg.getMedia_name(), (int) width * 57 / 100, (int) height * 30 / 100);
//
//                        Log.d("Image Width",
//                                "Image Width::" + " image view::"
//                                        + arg0.mRightImageChat.getWidth()
//                                        + " image view height::"
//                                        + arg0.mRightImageChat.getHeight()
//                                        + "bitmap widh::"
//                                        + unscaledBitmap.getWidth());
//
//                        arg0.mRightImageChat.setImageBitmap(unscaledBitmap);
//                        arg0.mRightImageChat.setTag(Constant.local_image_dir
//                                + msg.getMedia_name());
//
//                        if (msg.getStatus() == 3
//                                && msg.getNeeds_push() == 1) {
//                            arg0.mRightImageProgressUpload.setVisibility(View.GONE);
//                            arg0.mRightImageProgressUpload
//                                    .setVisibility(View.VISIBLE);
//                            new uploa_image().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,String.valueOf(msg.get_id()),
//                                    fromuser);
//                        } else if (msg.getMedia_url() == null
//                                && msg.getStatus() == 2) {
//                            arg0.mRightImageProgressUpload.setVisibility(View.GONE);
//                            arg0.mRightImageProgressUpload
//                                    .setVisibility(View.VISIBLE);
//
//                        } else if (msg.getStatus() == 2
//                                && msg.getNeeds_push() == 1) {
//                            arg0.mRightImageProgressUpload.setVisibility(View.GONE);
//                            arg0.mRightImageProgressUpload.setVisibility(View.GONE);
//
//                        }
//                    } else {
//                        arg0.mRightImageProgressUpload.setVisibility(View.GONE);
//                        arg0.mRightImageProgressUpload.setVisibility(View.GONE);
//                        byte[] image_data = msg.getRow_data();
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(
//                                image_data, 0, image_data.length);
//                        arg0.mRightImageChat.setImageBitmap(bitmap);
//                        arg0.mRightImageChat.setTag(null);
//                    }
//
//                    arg0.mRightImageChat.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            if (v.getTag() != null) {
//                                String path = v.getTag().toString();
//                                File f = new File(path);
//                                Uri uri = Uri.fromFile(f);
//
//                                Bitmap bitmap = null;
//
//                                try {
//                                    bitmap = MediaStore.Images.Media
//                                            .getBitmap(context
//                                                            .getContentResolver(),
//                                                    uri);
//                                } catch (FileNotFoundException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                } catch (IOException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
//
//                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                bitmap.compress(Bitmap.CompressFormat.JPEG,
//                                        75, stream);
//                                byte[] byteArray = stream.toByteArray();
//
//                                Intent intent = new Intent(context,
//                                        OrientationGroup.class)
//                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.putExtra("pic", byteArray);
//                                context.startActivity(intent);
//
//                            }
//
//                        }
//                    });
//
//                    final ProgressBar progressBar = arg0.mRightImageProgressUpload;
//                    final ImageView imgView = arg0.mRightImageUploadImage;
//
//                    arg0.mRightImageProgressUpload
//                            .setOnClickListener(new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//                                    String[] val = v.getTag().toString()
//                                            .split(",");
//                                    new uploa_image().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,val[0],
//                                            val[1]);
//
//                                    progressBar
//                                            .setVisibility(View.VISIBLE);
//                                    imgView
//                                            .setVisibility(View.GONE);
//
//                                }
//                            });
//                }
//                // //////////////////******Right
//                // Video**************///////////////////////////////////////////////////////////////////////
//                else if (Integer.parseInt(msg.getMedia_wa_type()) == 2) {
//
//                    arg0.mRightVideo.setVisibility(View.VISIBLE);
//                    arg0.mRightVideoChatLayout.setVisibility(View.VISIBLE);
//
//
//                    arg0.mTxtDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP,
//                            notificatiob_font_size);
//                    arg0.mTxtSize.setTextSize(TypedValue.COMPLEX_UNIT_SP,
//                            notificatiob_font_size);
//
//                    arg0.mRightVideo.setFocusable(false);
//                    arg0.mBtnUploadVideo.setFocusable(false);
//                    arg0.mBtnUploadVideo.setFocusable(false);
//                    arg0.mBtnVideoPlay.setFocusable(false);
//                    arg0.mTxtDuration.setFocusable(false);
//                    arg0.mTxtSize.setFocusable(false);
//
//                    // Set timestamp
//                    Date date = new Date();
//                    date.setTime(msg_list.get(arg1).getTimestamp());
//                    SimpleDateFormat time_format = new SimpleDateFormat(
//                            "hh:mm a");
//
//                    arg0.mRightChatTimeTxt.setText(time_format.format(date));
//
//
//                    long second = (msg.getMedia_duration() / 1000) % 60;
//                    long minute = (msg.getMedia_duration() / (1000 * 60)) % 60;
//
//                    String time = String.format("%02d:%02d", minute, second);
//
//                    arg0.mTxtDuration.setText(time);
//
//
//                    arg0.mTxtSize.setText(Network_Usage.convertBytesToSuitableUnit(String.valueOf(msg.getMedia_size())));
//
//                    File file = new File(Constant.local_video_dir
//                            + msg.getMedia_name());
//
//
//                    arg0.mRightVideo.getLayoutParams().width = (int) width * 57 / 100;
//                    arg0.mRightVideo.getLayoutParams().height = (int) height * 28 / 100;
//
//                    String values = msg.get_id() + "," + fromuser;
//                    arg0.mBtnUploadVideo.setTag(values);
//                    arg0.mBtnUploadVideo.setVisibility(View.GONE);
//                    arg0.mProgressVideoUploadImage.setVisibility(View.GONE);
//
//                    try {
//                        byte[] image_data = msg.getRow_data();
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(
//                                image_data, 0, image_data.length);
//                        arg0.mRightVideo.setImageBitmap(bitmap);
//                        arg0.mRightVideo.setTag(null);
//                    } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//                        e.printStackTrace();
//                        // TODO: handle exception
//                    }
//
//                    if (msg.getStatus() == 3
//                            && msg.getNeeds_push() == 1) {
//                        arg0.mBtnUploadVideo.setVisibility(View.GONE);
//                        arg0.mProgressVideoUploadImage.setVisibility(View.VISIBLE);
//                        arg0.mBtnVideoPlay.setVisibility(View.GONE);
//                        new uploa_video().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,String.valueOf(msg.get_id()), fromuser);
//
//                    } else if (msg.getMedia_url() == null
//                            && msg.getStatus() == 2) {
//                        arg0.mBtnUploadVideo.setVisibility(View.GONE);
//                        arg0.mProgressVideoUploadImage.setVisibility(View.VISIBLE);
//                        arg0.mBtnVideoPlay.setVisibility(View.GONE);
//                            /*
//                             * new uploa_video().execute(c.getString(0),
//							 * fromuser);
//							 */
//                    } else if (msg.getStatus() == 2
//                            && msg.getNeeds_push() == 1) {
//                        arg0.mBtnUploadVideo.setVisibility(View.VISIBLE);
//                        arg0.mProgressVideoUploadImage.setVisibility(View.GONE);
//                        arg0.mBtnVideoPlay.setVisibility(View.GONE);
//
//                    } else {
//                        arg0.mBtnVideoPlay.setVisibility(View.VISIBLE);
//                        arg0.mBtnVideoPlay.setTag(Constant.local_video_dir
//                                + msg.getMedia_name());
//                        arg0.mBtnUploadVideo.setVisibility(View.GONE);
//                        arg0.mProgressVideoUploadImage.setVisibility(View.GONE);
//                    }
//
//                    arg0.mRightVideo.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            if (v.getTag() != null) {
//                                String path = v.getTag().toString();
//                                File f = new File(path);
//                                Uri uri = Uri.fromFile(f);
//                                Intent intent = new Intent(
//                                        Intent.ACTION_VIEW);
//                                String mime = "*/*";
//                                MimeTypeMap mimeTypeMap = MimeTypeMap
//                                        .getSingleton();
//                                if (mimeTypeMap.hasExtension(mimeTypeMap
//                                        .getFileExtensionFromUrl(uri
//                                                .toString())))
//                                    mime = mimeTypeMap
//                                            .getMimeTypeFromExtension(mimeTypeMap
//                                                    .getFileExtensionFromUrl(uri
//                                                            .toString()));
//                                intent.setDataAndType(uri, mime);
//                                context.startActivity(intent);
//                            }
//
//                        }
//                    });
//
//                    final ProgressBar progressBar = arg0.mProgressVideoUploadImage;
//                    final ImageView upl_img = arg0.mBtnUploadVideo;
//                    final ImageView ply_vid = arg0.mBtnVideoPlay;
//                    arg0.mBtnUploadVideo
//                            .setOnClickListener(new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//                                    String[] val = v.getTag().toString()
//                                            .split(",");
//                                    new uploa_video().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,val[0],
//                                            val[1]);
//
//                                    progressBar
//                                            .setVisibility(View.VISIBLE);
//                                    upl_img
//                                            .setVisibility(View.GONE);
//                                    ply_vid.setVisibility(View.GONE);
//                                }
//                            });
//                    arg0.mBtnVideoPlay.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            // TODO Auto-generated method stub
//                            String uri = v.getTag().toString();
//
//                            try {
//                                Intent intent = new Intent();
//                                intent.setAction(Intent.ACTION_VIEW);
//                                intent.setDataAndType(Uri.parse(uri),
//                                        "video/mp4");
//                                context.startActivity(intent);
//                            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//                                AlertDialog.Builder b = new AlertDialog.Builder(
//                                        context);
//                                b.setMessage(
//                                        context.getResources()
//                                                .getString(
//                                                        R.string.dont_have_app_to_play_video))
//                                        .setCancelable(false);
//                                b.setNegativeButton(
//                                        context.getResources().getString(
//                                                R.string.Ok),
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(
//                                                    DialogInterface dialog,
//                                                    int id) {
//
//                                            }
//                                        });
//                                AlertDialog alert = b.create();
//                                alert.show();
//                            }
//
//                        }
//                    });
//                }
//                // //////////////////******Right
//                // Audio**************///////////////////////////////////////////////////////////////////////
//                else if (msg.getMedia_wa_type().equals("3")) {
//
//                    arg0.mRightAudioChatLayout.setVisibility(View.VISIBLE);
//                    arg0.mBtnAudioCancelUpload.setVisibility(View.VISIBLE);
//                    arg0.mBtnAudioUpload.setVisibility(View.VISIBLE);
//                    arg0.mBtnAudioPlay.setVisibility(View.VISIBLE);
//                    arg0.mBtnAudioPlay.setBackgroundResource(R.drawable.ic_action_audio_play);
//
//
//                    arg0.mTxtAudioSize.setTextSize(TypedValue.COMPLEX_UNIT_SP,
//                            notificatiob_font_size);
//                    arg0.mTxtAudioLength.setTextSize(TypedValue.COMPLEX_UNIT_SP,
//                            notificatiob_font_size);
//
//
//                    // Set timestamp
//                    Date date = new Date();
//                    date.setTime(msg_list.get(arg1).getTimestamp());
//                    SimpleDateFormat time_format = new SimpleDateFormat(
//                            "hh:mm a");
//
//                    arg0.mRightChatTimeTxt.setText(time_format.format(date));
//
//                    arg0.mTxtAudioLength.setText(new TimeUtils()
//                            .millisToShortDHMS(msg.getMedia_duration()));
//
//                    arg0.mBtnAudioPlay.setFocusable(false);
//                    arg0.mBtnAudioUpload.setFocusable(false);
//                    arg0.mBtnAudioCancelUpload.setFocusable(false);
//                    arg0.mTxtAudioSize.setFocusable(false);
//                    arg0.mProgressAudio.setFocusable(false);
//                    arg0.mSeekAudioPlay.setFocusable(false);
//                    arg0.mTxtAudioSize.setText(new ScalingUtilities()
//                            .humanReadableByteCount(msg.getMedia_size(), true));
//
//                    arg0.mSeekAudioPlay.setProgress(0);
//                    arg0.mSeekAudioPlay.setMax(100);
//                    final MediaPlayer player = new MediaPlayer();
//                    arg0.mBtnAudioPlay.setTag(Constant.local_audio_dir
//                            + msg.getMedia_name());
//                    arg0.mSeekAudioPlay.setTag(msg.get_id());
//                    String values = msg.get_id() + "," + fromuser;
//                    arg0.mBtnAudioUpload.setTag(values);
//                    list.add(player);
//                    PlayBtnlist.add(arg0.mBtnAudioPlay);
//
//                    final ImageButton btn_play = arg0.mBtnAudioPlay;
//                    final ImageView btn_cancel_upload = arg0.mBtnAudioCancelUpload;
//                    final ProgressBar progress_audio = arg0.mProgressAudio;
//                    final ImageView btn_upload = arg0.mBtnAudioUpload;
//                    final SeekBar seek_audio = arg0.mSeekAudioPlay;
//
//                    arg0.mBtnAudioCancelUpload
//                            .setOnClickListener(new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//                                    // TODO Auto-generated method stub
//
//                                    new uploa_audio().cancel(true);
//                                    btn_upload.setVisibility(View.VISIBLE);
//                                    progress_audio.setVisibility(View.GONE);
//                                    btn_play.setVisibility(View.GONE);
//                                    // txt_audio_size.setVisibility(View.VISIBLE);
//                                    btn_cancel_upload
//                                            .setVisibility(View.GONE);
//                                    seek_audio.setVisibility(View.GONE);
//
//                                }
//                            });
//                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//                        @Override
//                        public void onCompletion(MediaPlayer mp) {
//                            // TODO Auto-generated method stub
//                            // Log.d("Play", "Player Finished.....");
//
//                            seekHandler.removeCallbacks(run);
//                            seek_audio.setProgress(0);
//
//                            if (s < Integer.valueOf(listTemp.get(
//                                    listTemp.size() - 1).toString())
//                                    && mFirstRun == true) {
//
//                                s++;
//                                if (s == Integer.valueOf(listTemp.get(
//                                        listTemp.size() - 1).toString()) - 1) {
//                                    s = Integer
//                                            .valueOf(listTemp.get(
//                                                    listTemp.size() - 1)
//                                                    .toString()) + 1;
//                                    mFirstRun = false;
//
//                                }
//                                Constant.printMsg("oooooooooooooolLL"
//                                        + s
//                                        + "     "
//                                        + listTemp.get(listTemp.size() - 1)
//                                        .toString());
//
//                            } else {
//                                btn_play.setBackgroundResource(R.drawable.ic_action_audio_play);
//
//                            }
//
//                        }
//                    });
//
//                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//
//                        @Override
//                        public void onPrepared(MediaPlayer mp) {
//                            // TODO Auto-generated method stub
//                            // Log.d("Player", "Player prepared....");
//                            mp.start();
//                            seek_audio.setProgress(0);
//                            seek_audio.setMax(mp.getDuration());
//                        }
//                    });
//
//                    final TextView audio_leng = arg0.mTxtAudioLength;
//
//                    class run implements Runnable {
//
//                        MediaPlayer mp;
//                        SeekBar seek;
//
//                        public run(MediaPlayer m, SeekBar se) {
//                            mp = m;
//                            seek = se;
//                        }
//
//                        @Override
//                        public void run() {
//
//                            if (player.isPlaying()) {
//                                seek.setProgress(mp.getCurrentPosition());
//                                audio_leng.setText(new TimeUtils()
//                                        .millisToShortDHMS(mp
//                                                .getCurrentPosition()));
//                            }
//                            seekHandler.postDelayed(this, 1);
//                        }
//
//                    }
//
//                    MessageGetSet msg1 = dbAdapter.getMessages_by_msg_id(String.valueOf(msg.get_id()));
//
//                    if (msg.getStatus() == 3
//                        /* && message.getNeeds_push() == 1 */) {
//                        btn_upload.setVisibility(View.GONE);
//                        btn_play.setVisibility(View.GONE);
//                        btn_cancel_upload.setVisibility(View.VISIBLE);
//                        progress_audio.setVisibility(View.VISIBLE);
//
//                        // txt_audio_size.setVisibility(View.GONE);
//                        seek_audio.setVisibility(View.GONE);
//                            /*
//                             * Log.d("ChatAdapter", "Audio upload called::" +
//							 * c.getString(3) + " 4::" + c.getInt(4) + " 5::" +
//							 * c.getInt(5));
//							 */
//                        long l = dbAdapter.setUpdateMessage_need_push(
//                                msg.getKey_id(), 0);
//                        // Log.d("is update", "Audio upload update::" + l);
//                        new uploa_audio().execute(String.valueOf(msg.get_id()), fromuser);
//                    } else /* if(c.getInt(4)==1 && c.getInt(5)==0) */ {
//                        btn_upload.setVisibility(View.GONE);
//                        btn_cancel_upload.setVisibility(View.GONE);
//                        progress_audio.setVisibility(View.GONE);
//                        btn_play.setVisibility(View.VISIBLE);
//                        // txt_audio_size.setVisibility(View.GONE);
//                        seek_audio.setVisibility(View.VISIBLE);
//
//                    }
//
//                    btn_upload.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            String[] val = v.getTag().toString().split(",");
//
//								/* new uploa_audio().execute(val[0],val[1]); */
//
//                            new uploa_audio().execute(val[0], val[1]);
//
//                            btn_upload.setVisibility(View.GONE);
//                            progress_audio.setVisibility(View.VISIBLE);
//                            btn_play.setVisibility(View.GONE);
//                            // txt_audio_size.setVisibility(View.GONE);
//                            btn_cancel_upload.setVisibility(View.VISIBLE);
//                            seek_audio.setVisibility(View.GONE);
//
//                        }
//
//                    });
//
//                    btn_play.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            // TODO Auto-generated method stub
//                            if (mPressed == 1) {
//                                TempBtn = new ArrayList();
//                            }
//
//                            mPressed = 0;
//                            listTemp.add(list.size());
//
//                            if (mAudioTagValue.equalsIgnoreCase((String) v
//                                    .getTag())) {
//
//                                Constant.printMsg("Oooop" + mAudioTagValue
//                                        + "   " + (String) v.getTag());
//
//                                mAudioTagValue = (String) v.getTag();
//
//                            } else {
//                                mAudioTagValue = (String) v.getTag();
//                                for (int i = 0; i < list.size(); i++) {
//
//                                    Constant.printMsg("KKKKKKP"
//                                            + v.getTag() + "    "
//                                            + list.get(i));
//
//                                    MediaPlayer plays = list.get(i);
//
//                                    // if (v.getTag().equals(plays.gette))
//
//                                    plays.stop();
//
//                                }
//
//                            }
//                            if (player.isPlaying()) {
//                                Constant.printMsg("ooop");
//                            }
//                            if (current_song == String.valueOf(btn_play
//                                    .getTag())) {
//
//                                if (player.isPlaying()) {
//                                    seekHandler.removeCallbacks(run);
//                                    player.pause();
//                                    // run.suspend();
//                                    System.out
//                                            .println("called click button playing"
//                                                    + btn_play.getTag());
//
//                                    // length=player.getCurrentPosition();
//                                    btn_upload.setVisibility(View.GONE);
//                                    progress_audio.setVisibility(View.GONE);
//                                    btn_play.setVisibility(View.VISIBLE);
//                                    // txt_audio_size.setVisibility(View.GONE);
//                                    btn_cancel_upload
//                                            .setVisibility(View.GONE);
//                                    seek_audio.setVisibility(View.VISIBLE);
//                                    // btn_play.setBackgroundResource(R.drawable.ic_action_audio_play);
//                                } else {
//                                    player.start();
//                                    current_song = String.valueOf(btn_play
//                                            .getTag());
//                                    seekHandler.postDelayed(new run(player,
//                                            seek_audio), 100);
//
//                                    Constant.printMsg("kkkkkkl");
//
//                                    // btn_play.setBackgroundResource(R.drawable.ic_action_audio_pause);
//
//                                }
//                            } else {
//                                if (player.isPlaying()) {
//                                    Constant.printMsg("kkkkkkl11");
//                                    player.stop();
//                                    // btn_play.setBackgroundResource(R.drawable.ic_action_audio_play);
//
//                                }
//
//                                // Log.d("Audio", "Path::" +
//                                // v.getTag().toString());
//                                System.out
//                                        .println("called click button pause"
//                                                + btn_play.getTag());
//
//                                seekHandler.removeCallbacks(run);
//
//                                try {
//                                    player.reset();
//                                    player.setDataSource(v.getTag()
//                                            .toString());
//                                    player.prepare();
//                                        /* player.prepareAsync(); */
//                                } catch (Exception e) {
//                                    // ACRA.getErrorReporter().handleException(e);
//                                    // TODO: handle exception
//                                }
//                                seekHandler.postDelayed(new run(player,
//                                        seek_audio), 100);
//                                current_song = String.valueOf(btn_play
//                                        .getTag());
//                                // btn_play.setBackgroundResource(R.drawable.ic_action_audio_pause);
//
//                            }
//                            for (int i = 0; i < PlayBtnlist.size(); i++) {
//
//                                ImageButton btn = PlayBtnlist.get(i);
//
//                                if (btn.getTag().equals(v.getTag())
//                                        && mBtnClick == 0) {
//
//                                    Constant.printMsg("CCCCCCCCCCCCCCCAA");
//
//                                    btn.setBackgroundResource(R.drawable.ic_action_audio_pause);
//                                    mBtnClick++;
//
//                                } else {
//
//                                    Constant.printMsg("CCCCCCCCCCCCCCCAB");
//
//                                    mBtnClick = 0;
//                                    btn.setBackgroundResource(R.drawable.ic_action_audio_play);
//                                }
//
//                            }
//
//                            if (TempBtn.size() > 0) {
//
//                                if (v.getTag().equals(
//                                        TempBtn.get(TempBtn.size() - 1)
//                                                .toString())) {
//                                    Constant.printMsg("aaaaaaaaaaa1");
//
//                                    v.setBackgroundResource(R.drawable.ic_action_audio_play);
//                                    player.pause();
//                                    TempBtn = new ArrayList();
//
//                                } else {
//                                    Constant.printMsg("aaaaaaaaaaa2");
//                                    v.setBackgroundResource(R.drawable.ic_action_audio_pause);
//                                    TempBtn = new ArrayList();
//                                    TempBtn.add(v.getTag());
//
//                                }
//
//                            } else {
//                                Constant.printMsg("aaaaaaaaaaa3");
//                                v.setBackgroundResource(R.drawable.ic_action_audio_pause);
//                                TempBtn.add(v.getTag());
//
//                            }
//
//
//                        }
//                    });
//
//                }
//                // //////////////////******Right
//                // Contact**************///////////////////////////////////////////////////////////////////////
//                else if (msg.getMedia_wa_type().equals("5")) {
//
//                    arg0.mRightContactChatLayout.setVisibility(View.VISIBLE);
//
//                    arg0.mBtnViewContact.setText(msg.getMedia_name());
//                    String values = msg.getData();
//                    arg0.mBtnViewContact.setTag(values);
//                    arg0.mBtnViewContact.setTextSize(
//                            TypedValue.COMPLEX_UNIT_SP, msg_font_size);
//                    try {
//                        byte[] image_data = msg.getRow_data();
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(
//                                image_data, 0, image_data.length);
//                        arg0.mImgContactAvatar.setImageBitmap(bitmap);
//                    } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//                        // TODO: handle exception
//                    }
//
//                    arg0.mImgContactAvatar.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            if (v.getTag() != null) {
//
//                            }
//
//                        }
//                    });
//
//                    arg0.mBtnViewContact
//                            .setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context,
//                                            ContactView.class);
//                                    intent.putExtra("vcard", v.getTag()
//                                            .toString());
//                                    intent.putExtra("status", false);
//                                    context.startActivity(intent);
//                                }
//                            });
//
//                }
//                // //////////////////******Right
//                // Location**************///////////////////////////////////////////////////////////////////////
//                else if (msg.getMedia_wa_type().equals("4")) {
//
//                    arg0.mRightImageChatLayout.setVisibility(View.VISIBLE);
//                    arg0.mRightImageChat.setVisibility(View.VISIBLE);
//
//
//                    Log.d("Location", "Lat::" + msg.getLatitude() + " Lon::"
//                            + msg.getLongitude());
//                    String lat_lon = msg.getLatitude() + ","
//                            + msg.getLongitude();
//                    arg0.mRightImageChat.setTag(lat_lon);
//
//
//                    String values = msg.get_id() + "," + fromuser;
//                    arg0.mRightImageUploadImage.setTag(values);
//                    arg0.mRightImageUploadImage.setVisibility(View.GONE);
//                    arg0.mRightImageProgressUpload.setVisibility(View.GONE);
//                    try {
//                        byte[] image_data = msg.getRow_data();
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(
//                                image_data, 0, image_data.length);
//                        Bitmap unscaledBitmap = ScalingUtilities
//                                .decodeFile(Constant.local_image_dir
//                                        + msg.getMedia_name(), (int) width * 57 / 100, (int) height * 30 / 100);
//                        arg0.mRightImageChat.setImageBitmap(bitmap);
//
//                    } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//                        // TODO: handle exception
//                    }
//                    if (msg.getStatus() == 3
//                            && msg.getNeeds_push() == 1) {
//                        arg0.mRightImageUploadImage.setVisibility(View.GONE);
//                        arg0.mRightImageProgressUpload.setVisibility(View.VISIBLE);
//                            /*
//                             * new uploa_Location().execute(c.getString(0),
//							 * fromuser);
//							 */
//                    }
//                    if (msg.getStatus() == 2
//                            && msg.getNeeds_push() == 0) {
//                        arg0.mRightImageUploadImage.setVisibility(View.VISIBLE);
//                        arg0.mRightImageProgressUpload.setVisibility(View.GONE);
//                    } else {
//                        arg0.mRightImageUploadImage.setVisibility(View.GONE);
//                        arg0.mRightImageProgressUpload.setVisibility(View.GONE);
//
//                    }
//
//                    arg0.mRightImageChat.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            if (v.getTag() != null) {
//                                String latitude = v.getTag().toString()
//                                        .split(",")[0];
//                                String longitude = v.getTag().toString()
//                                        .split(",")[1];
//                                String label = "I'm Here!";
//                                String uriBegin = "geo:" + latitude + ","
//                                        + longitude;
//                                String query = latitude + "," + longitude
//                                        + "(" + label + ")";
//                                String encodedQuery = Uri.encode(query);
//                                String uriString = uriBegin + "?q="
//                                        + encodedQuery + "&z=16";
//                                Uri uri = Uri.parse(uriString);
//                                String url = "http://maps.google.com/maps?center='" + v.getTag().toString() + "'&zoom=15&views=transit";
//                                try {
//                                    Intent mapIntent = new Intent(
//                                            Intent.ACTION_VIEW,
//                                            uri);
//                                    mapIntent.setPackage("com.google.android.apps.maps");
//                                    context.startActivity(mapIntent);
//                                } catch (ActivityNotFoundException exp) {
//                                    context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                                }
//
//                            }
//
//                        }
//                    });
//
//                    final ProgressBar progress_upload_image = arg0.mRightImageProgressUpload;
//                    final ImageView btn_image_upload = arg0.mRightImageUploadImage;
//                    arg0.mRightImageUploadImage
//                            .setOnClickListener(new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//                                    String[] val = v.getTag().toString()
//                                            .split(",");
//                                        /*
//										 * new uploa_Location().execute(val[0],
//										 * val[1]);
//										 */
//
//                                    progress_upload_image
//                                            .setVisibility(View.VISIBLE);
//                                    btn_image_upload
//                                            .setVisibility(View.VISIBLE);
//
//                                }
//                            });
//
//                }
//
//                // Set timestamp
//                Date date = new Date();
//                date.setTime(msg_list.get(arg1).getTimestamp());
//                SimpleDateFormat time_format = new SimpleDateFormat(
//                        "hh:mm a");
//
//                arg0.mRightChatTimeTxt.setText(time_format.format(date));
//
//
//                // Set tick mark color
//                if (msg_list.get(arg1).getStatus() == 3) {
//                    arg0.mRightChatStatusImg.setImageResource(R.drawable.message_unsent);
//                } else if (msg_list.get(arg1).getStatus() == 2) {
//                    arg0.mRightChatStatusImg
//                            .setImageResource(R.drawable.receipt_from_server);
//                } else if (msg_list.get(arg1).getStatus() == 1 || msg_list.get(arg1).getStatus() == 0) {
//                    arg0.mRightChatStatusImg
//                            .setImageResource(R.drawable.receipt_from_target);
//                } else if (msg_list.get(arg1).getStatus() == -1) {
//                    arg0.mRightChatStatusImg
//                            .setImageResource(R.drawable.receipt_read);
//                }
//
//                arg0.mRightSenderImage.setImageBitmap(mSenderPhoto);
//
//
//            }
//            //---------------------------------LEFT message alignment---------------------------
//            else {
//
//                arg0.mTxtNotice.setVisibility(View.GONE);
//                arg0.mTxtDate.setVisibility(View.GONE);
//                arg0.mLeftChatLayout.setVisibility(View.GONE);
//                arg0.mRightChatLayout.setVisibility(View.GONE);
//                arg0.mLeftImageChatLayout.setVisibility(View.GONE);
//                arg0.mLeftVideoChatLayout.setVisibility(View.GONE);
//                arg0.mLeftContactChatLayout.setVisibility(View.GONE);
//                arg0.mLeftFileChatLayout.setVisibility(View.GONE);
//                arg0.mLeftAudioChatLayout.setVisibility(View.GONE);
//                arg0.mLeftChatText.setVisibility(View.GONE);
//                arg0.mLeftContactChatLayout.setVisibility(View.GONE);
//
//                arg0.mLeftChatLayout.setVisibility(View.VISIBLE);
//                arg0.mLeftChatUsername.setVisibility(View.VISIBLE);
//
//                arg0.mLeftChatUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP,
//                        msg_font_size);
//                arg0.mRightChatTipImg.setVisibility(View.GONE);
//                arg0.mRightSenderImage.setVisibility(View.GONE);
//                arg0.mLeftSenderImage.setVisibility(View.VISIBLE);
//                arg0.mLeftChatTip.setVisibility(View.VISIBLE);
//
//                ContactsGetSet contact = new ContactsGetSet();
//                try {
//                    contact = dbAdapter.getContact(msg.getRemote_resource());
//                    Constant.printMsg("Left username : " + contact);
//                    if (contact == null) {
//                        arg0.mLeftChatUsername.setText(msg.getRemote_resource().split("@")[0]);
//                    } else {
//                        arg0.mLeftChatUsername.setText(contact.getDisplay_name());
//                    }
//
//                } catch (Exception e) {
//                    // ACRA.getErrorReporter().handleException(e);
//                    // TODO: handle exception
//                }
//
//                try {
//                    // byte[] img_byte = KachingMeApplication.getAvatar();
//                    Constant.printMsg("called image::::>>>>>>>");
//                    byte[] img_byte = contact.getPhoto_ts();
//                    if (img_byte != null) {
//                        Bitmap bmp = BitmapFactory.decodeByteArray(
//                                img_byte, 0, img_byte.length);
//                        arg0.mLeftSenderImage.setImageBitmap(new AvatarManager()
//                                .roundCornerImage(bmp, 180));
//                    }
//                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//
//                }
///*
//                try {
//                    Bitmap bmp = BitmapFactory
//                            .decodeFile(KachingMeApplication.PROFILE_PIC_DIR
//                                    + msg.getRemote_resource().split("@")[0] + ".png");
//                    Drawable drawable = new BitmapDrawable(bmp);
//                    // getSupportActionBar().setIcon(drawable);
//                    arg0.mLeftSenderImage.setImageDrawable(drawable);
//                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//                    // TODO: handle exception
//                }*/
//
//                if (Integer.parseInt(msg.getMedia_wa_type()) == 0) {
//
//                    arg0.mLeftChatText.setVisibility(View.VISIBLE);
//                    arg0.mLeftChatTextTime.setVisibility(View.VISIBLE);
//                    arg0.mLeftChatUsername.setVisibility(View.VISIBLE);
//
//
//                    arg0.mLeftChatText.setHighlightColor(Color.TRANSPARENT);
//                    arg0.mLeftChatText.setMovementMethod(LinkMovementMethod
//                            .getInstance());
//                    arg0.mLeftChatText.setText(msg_list.get(arg1).getData().toString());
//
//                    //   arg0.mLeftChatText.setText(msg.getData());
//                    String text = msg.getData();
//
//                    if (text.length() > 2) {
//
//                        char q = text.charAt(0);
//                        char p = text.charAt(1);
//
//                        if (q == '<') {
//
//                            if (p == '-') {
//                                arg0.mLeftChatText.setHighlightColor(Color.TRANSPARENT);
//                                arg0.mLeftChatText.setMovementMethod(LinkMovementMethod
//                                        .getInstance());
//                                //
//
//                                myMethod(text.substring(2));
//                                arg0.mLeftChatText.setText(
//                                        addClickablePart(this.Normallist, arg0.mLeftChatText),
//                                        TextView.BufferType.SPANNABLE);
//                            } else {
//                                arg0.mLeftChatText.setHighlightColor(Color.TRANSPARENT);
//                                arg0.mLeftChatText.setMovementMethod(LinkMovementMethod
//                                        .getInstance());
//                                arg0.mLeftChatText.setText(msg_list.get(arg1).getData().toString());
//
//                            }
//
//                        }
//                    }
//
//                    EmojiconTextView txt_msg = arg0.mLeftChatText;
//                    mValue = txt_msg.getText().toString();
//
//                    String[] words = mValue.split("\\s+");
//
//
//                    if (txt_msg.length() > 3) {
//
//                        char s = text.charAt(0);
//                        char s1 = text.charAt(1);
//                        char s2 = text.charAt(2);
//
//                        if (s == '<') {
//
//                            if (s1 == 'b' && s2 == '>') {
//
//                                txt_msg.setTextColor(Color.GREEN);
//                                txt_msg.setText(text.substring(3));
//                                txt_msg.setTypeface(null, Color.GREEN);
//
//                                txt_msg.setBackground(null);
//                                txt_msg.setMinimumWidth(Constant.screenWidth);
//                                txt_msg.setTextSize(20);
//                                txt_msg.setEmojiconSize(33);
//                                txt_msg.setTypeface(null, Typeface.NORMAL);
//
//                                LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                                bubbleImgParams.gravity = Gravity.LEFT;
////                                bubbleImgParams.leftMargin = Constant.screenWidth * 15 / 100;
//
//                                txt_msg.setLayoutParams(bubbleImgParams);
//
//                            } else if (s1 == 'z' && s2 == '>') {
//
//                                txt_msg.setTextColor(Color.RED);
//
//                                String dazzle = text.substring(3)
//                                        .toString();
//                                String[] parts = dazzle.split("-");
//
//                                String part1 = parts[0];
//                                String part2 = parts[1];
//                                String part3 = parts[2];
//                                String part4 = parts[3];
//                                String part5 = parts[4];
//
//                                txt_msg.setText(part1);
//
//                                ContentValues cv1 = new ContentValues();
//                                cv1.put("msg", part1);
//                                cv1.put("shape", part4);
//                                cv1.put("speed", part3);
//                                cv1.put("background", part5);
//
//                                insertToLEDDB(cv1);
//                                txt_msg.setTypeface(null, Color.RED);
//
//                                txt_msg.setBackground(null);
//                                txt_msg.setMinimumWidth(Constant.screenWidth);
//                                txt_msg.setTextSize(20);
//                                txt_msg.setEmojiconSize(33);
//                                txt_msg.setTypeface(null, Typeface.NORMAL);
//
//                                LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                                bubbleImgParams.gravity = Gravity.LEFT;
////                                bubbleImgParams.leftMargin = Constant.screenWidth * 15 / 100;
//
//                                txt_msg.setLayoutParams(bubbleImgParams);
//
//                            } else if (s1 == 'l' && s2 == '>') {
//
//                                txt_msg.setTextColor(Color.YELLOW);
//                                txt_msg.setText(text.substring(3));
//
//                                OrientationGroup.mZzleTextor = txt_msg
//                                        .getText().toString();
//                                txt_msg.setTypeface(null, Color.YELLOW);
//
//                                txt_msg.setBackground(null);
//                                txt_msg.setMinimumWidth(Constant.screenWidth);
//                                txt_msg.setTextSize(20);
//                                txt_msg.setEmojiconSize(33);
//                                txt_msg.setTypeface(null, Typeface.NORMAL);
//
//                                LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                                bubbleImgParams.gravity = Gravity.LEFT;
////                                bubbleImgParams.leftMargin = Constant.screenWidth * 15 / 100;
//
//                                txt_msg.setLayoutParams(bubbleImgParams);
//
//                            } else if (s1 == 'x' && s2 == '>') {
//                                String dazzle = text.substring(3)
//                                        .toString();
//                                String[] parts = dazzle.split("-");
//                                String part1 = parts[0];
//                                String part2 = parts[1];
//                                String part3 = parts[2];
//                                String part4 = parts[3];
//                                String part5 = parts[4];
//
//                                Constant.printMsg("nymss::>>>" + part1
//                                        + "  " + part2 + "  " + part3
//                                        + "  " + part4 + "  " + part5);
//                                ContentValues cv = new ContentValues();
//
//                                cv.put("msg", part5);
//                                cv.put("backgrount", part1);
//                                cv.put("font", part4);
//                                cv.put("speed", part2);
//                                cv.put("fontcolor", part3);
//
//                                insertzzleDB(cv);
//                                txt_msg.setTextColor(Color.BLUE);
//                                txt_msg.setText(part5);
//
////                                ZzlePreviewGroup.mZzleTextor = txt_msg
////                                        .getText().toString();
//                                txt_msg.setTypeface(null, Color.BLUE);
//
//                                txt_msg.setBackground(null);
//                                txt_msg.setTextSize(20);
//                                txt_msg.setEmojiconSize(33);
//                                txt_msg.setTypeface(null, Typeface.NORMAL);
//
//                                LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                                bubbleImgParams.gravity = Gravity.LEFT;
////                                bubbleImgParams.leftMargin = Constant.screenWidth * 15 / 100;
//
//                                txt_msg.setLayoutParams(bubbleImgParams);
//
//                            } else if (s1 == 's' && s2 == '>') {
//
//                                String self_destruct = text.substring(3)
//                                        .toString();
//                                String[] parts = self_destruct.split("-");
//                                String part1 = parts[0];
//                                String part2 = parts[1];
//                                txt_msg.setText(part2);
//
//                                ArrayList<String> jid_list = new ArrayList<String>();
//                                for (int i = 0; i < parts.length; i++) {
//
//                                    if (i != 0 && i != 1) {
//                                        jid_list.add(parts[i]);
//
//                                    }
//
//                                    Constant.printMsg("jid_list ::::::: "
//                                            + jid_list + "    " + i);
//                                }
//
//                                mself_destruct_list.add(String.valueOf(arg1));
//                                Constant.mself_id = arg1;
//
//                                dbAdapter = KachingMeApplication
//                                        .getDatabaseAdapter();
//
//                                final MessageGetSet selectedItem;
//                                txt_msg.setTypeface(null, Typeface.NORMAL);
//
//                                LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                                bubbleImgParams.gravity = Gravity.LEFT;
////                                bubbleImgParams.leftMargin = Constant.screenWidth * 15 / 100;
//
//                                txt_msg.setLayoutParams(bubbleImgParams);
//
//                                try {
//
//                                    if (Constant.msg_list_adapter.size() == Constant.mself_id) {
//
//                                        Constant.printMsg("part tst::::>>>>>>");
//                                    } else {
//                                        selectedItem = Constant.msg_list_adapter
//                                                .get(Constant.mself_id);
//                                        Constant.printMsg("part tst111::::>>>>>>>>"
//                                                + selectedItem
//                                                .get_id()
//                                                + "     "
//                                                + selectedItem
//                                                .getIs_sec_chat()
//                                                + "   "
//                                                + part1
//                                                + "    "
//                                                + KachingMeApplication
//                                                .getjid());
//                                        String member_jid = null;
//                                        for (int i = 0; i < jid_list.size(); i++) {
//                                            if (jid_list.get(i) == KachingMeApplication
//                                                    .getjid().split("@")[0]) {
//                                                member_jid = jid_list
//                                                        .get(i);
//                                            }
//                                        }
//
//                                        new Self_Destruct_Messages(context)
//                                                .setDestruct(
//                                                        ""
//                                                                + selectedItem
//                                                                .get_id(),
//                                                        Integer.valueOf(part1),
//                                                        member_jid
//                                                                + "@localhost");
//
//                                    }
//                                } catch (Exception e) {
//                                    // TODO: handle exception
//                                }
//
//                            } else if (s1 == 'k' && s2 == '>') {
//
//                                String kons_array = text.substring(3)
//                                        .toString();
//                                String[] parts = kons_array.split("-");
//                                final String part1 = parts[0];
//                                String part2 = parts[1];
//                                final String part3 = parts[2];
//
//                                Constant.printMsg("testststststtstststskkkkkk" + part1 + part2 + part3);
//                                String bubblecolor = part2;
//                                String mShape = part1.trim();
//                                if (!bubblecolor.equalsIgnoreCase("")) {
//                                    switch (bubblecolor) {
//                                        case "Blue":
//                                            if (mShape.equalsIgnoreCase("oval_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.blue_one);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cross_rect_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.blue_two);
//                                            }
//                                            if (mShape.equalsIgnoreCase("oval_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.blue_three);
//                                            }
//                                            if (mShape.equalsIgnoreCase("rect_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.blue_four);
//                                            }
//                                            if (mShape.equalsIgnoreCase("rect_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.blue_five);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cross_rect_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.blue_six);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cloud_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.blue_seven);
//                                            }
//                                            break;
//                                        case "Green":
//                                            if (mShape.equalsIgnoreCase("oval_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.green_one);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cross_rect_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.green_two);
//                                            }
//                                            if (mShape.equalsIgnoreCase("oval_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.green_three);
//                                            }
//                                            if (mShape.equalsIgnoreCase("rect_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.green_four);
//                                            }
//                                            if (mShape.equalsIgnoreCase("rect_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.green_five);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cross_rect_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.green_six);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cloud_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.green_seven);
//                                            }
//                                            break;
//                                        case "Pink":
//                                            if (mShape.equalsIgnoreCase("oval_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.pink_one);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cross_rect_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.pink_two);
//                                            }
//                                            if (mShape.equalsIgnoreCase("oval_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.pink_three);
//                                            }
//                                            if (mShape.equalsIgnoreCase("rect_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.pink_four);
//                                            }
//                                            if (mShape.equalsIgnoreCase("rect_right")) {
//                                                txt_msg.setBackgroundResource(R.drawable.pink_five);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cross_rect_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.pink_six);
//                                            }
//                                            if (mShape.equalsIgnoreCase("cloud_left")) {
//                                                txt_msg.setBackgroundResource(R.drawable.pink_seven);
//                                            }
//                                            break;
//                                    }
//
//                                }
//
//                                txt_msg.setText(part3);
//
//
//                                txt_msg.setTextColor(Color.WHITE);
//                                if (Constant.screenWidth >= 600) {
//                                    Constant.printMsg("konss caleedd 1" + part3.length());
////                                        if (part3.length() == 4) {
//                                    Constant.printMsg("konss caleedd 6771 :::::" + part3.length() + "  "
//                                            + Constant.screenHeight + "  " + Constant.screenWidth);
//
//                                    // text = " " + text + "  ";
//
//                                    LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                            LinearLayout.LayoutParams.WRAP_CONTENT,
//                                            LinearLayout.LayoutParams.WRAP_CONTENT);
//                                    bubbleImgParams.width = Constant.screenWidth * 30 / 100;
//                                    bubbleImgParams.height = Constant.screenHeight * 10 / 100;
//                                    bubbleImgParams.gravity = Gravity.LEFT;
////                                    bubbleImgParams.leftMargin = Constant.screenWidth * 15 / 100;
//                                    bubbleImgParams.topMargin = Constant.screenWidth * 5 / 100;
//
//                                    txt_msg.setLayoutParams(bubbleImgParams);
//                                    txt_msg.setGravity(Gravity.CENTER);
//                                    txt_msg.setTypeface(null, Typeface.BOLD);
//                                    txt_msg.setEmojiconSize(40);
//
//
//                                } else if (Constant.screenWidth > 501
//                                        && Constant.screenWidth < 600) {
//                                    Constant.printMsg("konss caleedd 890");
//                                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
//                                            LinearLayout.LayoutParams.WRAP_CONTENT,
//                                            LinearLayout.LayoutParams.WRAP_CONTENT);
//                                    textParams.width = Constant.screenWidth * 30 / 100;
//                                    textParams.height = Constant.screenHeight * 10 / 100;
//                                    textParams.gravity = Gravity.LEFT;
////                                    textParams.leftMargin = Constant.screenWidth * 15 / 100;
//                                    textParams.topMargin = Constant.screenWidth * 5 / 100;
//                                    txt_msg.setEmojiconSize(39);
//                                    txt_msg.setTypeface(null, Typeface.BOLD);
//
//                                    txt_msg.setLayoutParams(textParams);
//                                    txt_msg.setGravity(Gravity.CENTER);
//
//
//                                } else if (Constant.screenWidth > 260
//                                        && Constant.screenWidth < 500) {
//                                    Constant.printMsg("caleedd 3");
//                                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
//                                            LinearLayout.LayoutParams.WRAP_CONTENT,
//                                            LinearLayout.LayoutParams.WRAP_CONTENT);
//                                    textParams.width = Constant.screenWidth * 30 / 100;
//                                    textParams.height = Constant.screenHeight * 10 / 100;
//                                    textParams.gravity = Gravity.LEFT;
////                                    textParams.leftMargin = Constant.screenWidth * 15 / 100;
//                                    textParams.topMargin = Constant.screenWidth * 5 / 100;
//                                    txt_msg.setTypeface(null, Typeface.BOLD);
//
//                                    txt_msg.setLayoutParams(textParams);
//                                    txt_msg.setGravity(Gravity.CENTER);
//                                    txt_msg.setEmojiconSize(38);
//
//                                } else if (Constant.screenWidth <= 260) {
//                                    Constant.printMsg("caleedd 4");
//
//                                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
//                                            LinearLayout.LayoutParams.WRAP_CONTENT,
//                                            LinearLayout.LayoutParams.WRAP_CONTENT);
//                                    textParams.width = Constant.screenWidth * 30 / 100;
//                                    textParams.height = Constant.screenHeight * 8 / 100;
//                                    textParams.gravity = Gravity.LEFT;
////                                    textParams.leftMargin = Constant.screenWidth * 15 / 100;
//                                    textParams.topMargin = Constant.screenWidth * 5 / 100;
//                                    txt_msg.setTypeface(null, Typeface.BOLD);
//
//                                    txt_msg.setLayoutParams(textParams);
//                                    txt_msg.setGravity(Gravity.CENTER);
//                                    txt_msg.setEmojiconSize(36);
//
//
//                                }
//
//                            } else {
//
//                                txt_msg.setTextColor(Color.BLACK);
//                                txt_msg.setTypeface(null, Color.BLACK);
//
//                                txt_msg.setBackground(null);
//                                txt_msg.setMinimumWidth(Constant.screenWidth);
//                                txt_msg.setTextSize(20);
//                                txt_msg.setEmojiconSize(33);
//                                txt_msg.setTypeface(null, Typeface.NORMAL);
//
//                                LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                                bubbleImgParams.gravity = Gravity.LEFT;
////                                bubbleImgParams.leftMargin = Constant.screenWidth * 15 / 100;
//
//                                txt_msg.setLayoutParams(bubbleImgParams);
//
//                            }
//
//                        } else {
//                            txt_msg.setTextColor(Color.BLACK);
//                            txt_msg.setTypeface(null, Color.BLACK);
//
//                            txt_msg.setBackground(null);
//                            txt_msg.setMinimumWidth(Constant.screenWidth);
//                            txt_msg.setTextSize(20);
//                            txt_msg.setEmojiconSize(33);
//                            txt_msg.setTypeface(null, Typeface.NORMAL);
//
//                            LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                                    LinearLayout.LayoutParams.WRAP_CONTENT);
//                            bubbleImgParams.gravity = Gravity.LEFT;
////                            bubbleImgParams.leftMargin = Constant.screenWidth * 15 / 100;
//
//                            txt_msg.setLayoutParams(bubbleImgParams);
//
//                        }
//
//                        // }
//                    } else {
//                        txt_msg.setTextColor(Color.BLACK);
//                        txt_msg.setTypeface(null, Color.BLACK);
//
//                        txt_msg.setBackground(null);
//                        txt_msg.setMinimumWidth(Constant.screenWidth);
//                        txt_msg.setTextSize(20);
//                        txt_msg.setEmojiconSize(33);
//                        txt_msg.setTypeface(null, Typeface.NORMAL);
//
//                        LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
//                                LinearLayout.LayoutParams.WRAP_CONTENT,
//                                LinearLayout.LayoutParams.WRAP_CONTENT);
//                        bubbleImgParams.gravity = Gravity.LEFT;
////                        bubbleImgParams.leftMargin = Constant.screenWidth * 15 / 100;
//
//                        txt_msg.setLayoutParams(bubbleImgParams);
//
//                    }
//
//                    txt_msg.setTag(arg1);
//
//                    txt_msg.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            // TODO Auto-generated method stub
//
//                            EmojiconTextView txt_msg = (EmojiconTextView) v
//                                    .findViewById(R.id.left_chat_text);
//                                /*txt_msg_layout = (LinearLayout) v
//                                        .findViewById(R.id.ll_txt_msg);*/
//                            boolean toggle = true;
//
//                            String text = txt_msg.getText().toString();
//                            int textlength = text.length();
//                            if (txt_msg.getCurrentTextColor() == -16776961) {
//
//                                Constant.mZzleText = txt_msg.getText()
//                                        .toString();
//
//                                BannerActivityChat.mZzleText = txt_msg
//                                        .getText().toString();
//
//                                Intent intent = new Intent(context,
//                                        BannerActivityChat.class);
//                                context.startActivity(intent);
//
//                            }
//                            if (txt_msg.getCurrentTextColor() == -65536) {
//
//                                Constant.zzle = false;
//
//                                BannerActivityLED.mZzleText = txt_msg
//                                        .getText().toString();
//                                Intent intent = new Intent(context,
//                                        BannerActivityLED.class);
//                                context.startActivity(intent);
//                                // }
//
//                            }
//
//                            if (txt_msg.getCurrentTextColor() == -256) {
//
//                                OrientationGroup.mZzleTextor = txt_msg
//                                        .getText().toString();
//
//                                // HorizonalSlideshow.mZzleTextor = txt_msg
//                                // .getText().toString();
//
//                                Intent intent = new Intent(context,
//                                        OrientationGroup.class);
//                                context.startActivity(intent);
//
//                                // }
//
//                            }
//
//                        }
//                    });
//
//                    // Set timestamp
//                    Date date = new Date();
//                    date.setTime(msg_list.get(arg1).getTimestamp());
//                    SimpleDateFormat time_format = new SimpleDateFormat(
//                            "hh:mm a");
//
//                    arg0.mLeftChatTextTime.setText(time_format.format(date));
//
//                }
//                //-------------------------------LEFT image download-------------------------
//                else if (Integer.parseInt(msg.getMedia_wa_type()) == 1) {
//
//
//                    arg0.mLeftImageChatLayout.setVisibility(View.VISIBLE);
//                    arg0.mLeftImageChat.setVisibility(View.VISIBLE);
//
//
//                    // Set timestamp
//                    Date date = new Date();
//                    date.setTime(msg_list.get(arg1).getTimestamp());
//                    SimpleDateFormat time_format = new SimpleDateFormat(
//                            "hh:mm a");
//
//                    arg0.mLeftChatTextTime.setText(time_format.format(date));
//
//
//                    arg0.mLeftImageChat.getLayoutParams().width = (int) width * 57 / 100;
//                    arg0.mLeftImageChat.getLayoutParams().height = (int) height * 28 / 100;
//
//                    //      arg0.mLeftImageChat.setMaxHeight(i);
//
//                    String values = msg.getKey_id() + "," + msg.getMedia_url()
//                            + "," + fromuser;
//                    arg0.mLeftImageDownload.setVisibility(View.VISIBLE);
//                    arg0.mLeftImageDownload.setTag(values);
//
//                    if (msg.getMedia_name() == null) {
//                        byte[] img_byte = msg.getRow_data();
//                        if (img_byte != null) {
//                            Bitmap bmp = BitmapFactory.decodeByteArray(
//                                    img_byte, 0, img_byte.length);
//                            arg0.mLeftImageChat.setImageBitmap(bmp);
//                            try {
//                                bites = (long) img_byte.length;
//                                //  updateMediaNetwork_Receive(bites);
//                            } catch (Exception e) {
//
//                            }
//                        }
//                        if (is_auto_dowload_image) {
//                            new download_image().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg.getKey_id(),
//                                    msg.getMedia_url(), fromuser);
//                        } else {
//                            arg0.mLeftProgressDownloadImage
//                                    .setVisibility(View.GONE);
//
//                        }
//                    } else {
//                        arg0.mLeftImageDownload.setVisibility(View.GONE);
//                        arg0.mLeftProgressDownloadImage.setVisibility(View.GONE);
//
//                        File file = new File(Constant.local_image_dir
//                                + msg.getMedia_name());
//                        if (file.isFile()) {
//                            Bitmap unscaledBitmap = ScalingUtilities
//                                    .decodeFile(Constant.local_image_dir
//                                                    + msg.getMedia_name(), (int) width * 57 / 100,
//                                            (int) height * 28 / 100);
//
//                          /*  Log.d("Image Width",
//                                    "Image Width::" + i + " layout::"
//                                            + localView.getWidth()
//                                            + " image view::"
//                                            + image.getWidth()
//                                            + " image view height::"
//                                            + image.getHeight()
//                                            + "bitmap widh::"
//                                            + unscaledBitmap.getWidth());*/
//
//                            arg0.mLeftImageChat.setImageBitmap(unscaledBitmap);
//
//                            arg0.mLeftImageChat.setTag(Constant.local_image_dir
//                                    + msg.getMedia_name());
//
//                        } else {
//                            byte[] image_data = msg.getRow_data();
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(
//                                    image_data, 0, image_data.length);
//                            arg0.mLeftImageChat.setImageBitmap(bitmap);
//                            arg0.mLeftImageChat.setScaleType(ImageView.ScaleType.FIT_XY);
//                            arg0.mLeftImageChat.setTag(null);
//
//                        }
//
//                    }
//
//                    final ProgressBar progress_download_image = arg0.mLeftProgressDownloadImage;
//                    final ImageView btn_image_download = arg0.mLeftImageDownload;
//                    arg0.mLeftImageDownload
//                            .setOnClickListener(new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//
//                                    String[] val = v.getTag().toString()
//                                            .split(",");
//                                    new download_image().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,val[0],
//                                            val[1], val[2]);
//                                    progress_download_image
//                                            .setVisibility(View.VISIBLE);
//                                    btn_image_download
//                                            .setVisibility(View.GONE);
//                                    // TODO Auto-generated method stub
//
//                                }
//                            });
//
//                    arg0.mLeftImageChat.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            if (v.getTag() != null) {
//                                String path = v.getTag().toString();
//                                File f = new File(path);
//                                Uri uri = Uri.fromFile(f);
//                                Intent intent = new Intent(
//                                        Intent.ACTION_VIEW);
//                                String mime = "*/*";
//                                MimeTypeMap mimeTypeMap = MimeTypeMap
//                                        .getSingleton();
//                                if (mimeTypeMap.hasExtension(mimeTypeMap
//                                        .getFileExtensionFromUrl(uri
//                                                .toString())))
//                                    mime = mimeTypeMap
//                                            .getMimeTypeFromExtension(mimeTypeMap
//                                                    .getFileExtensionFromUrl(uri
//                                                            .toString()));
//                                intent.setDataAndType(uri, mime);
//                                context.startActivity(intent);
//                            }
//
//                        }
//                    });
//                } else if (Integer.parseInt(msg.getMedia_wa_type()) == 12) {
//
//
//                    arg0.mLeftImageChatLayout.setVisibility(View.VISIBLE);
//                    arg0.mLeftImageChatLayout.setVisibility(View.VISIBLE);
//                    arg0.mLeftImageChat.setVisibility(View.VISIBLE);
//
//                    // Set timestamp
//                    Date date = new Date();
//                    date.setTime(msg_list.get(arg1).getTimestamp());
//                    SimpleDateFormat time_format = new SimpleDateFormat(
//                            "hh:mm a");
//
//                    arg0.mLeftChatTextTime.setText(time_format.format(date));
//
//
//                    arg0.mLeftImageChat.getLayoutParams().width = (int) width * 57 / 100;
//                    arg0.mLeftImageChat.getLayoutParams().height = (int) height * 28 / 100;
//
//                    String values = msg.getKey_id() + "," + msg.getMedia_url()
//                            + "," + fromuser;
//                    arg0.mLeftImageDownload.setVisibility(View.VISIBLE);
//                    arg0.mLeftImageDownload.setTag(values);
//
//                    if (msg.getMedia_name() == null) {
//                        byte[] img_byte = msg.getRow_data();
//                        if (img_byte != null) {
//                            Bitmap bmp = BitmapFactory.decodeByteArray(
//                                    img_byte, 0, img_byte.length);
//                            arg0.mLeftImageChat.setImageBitmap(bmp);
//                            try {
//                                bites = (long) img_byte.length;
//                                updateMediaNetwork_Receive(bites);
//                            } catch (Exception e) {
//
//                            }
//                        }
//                        if (is_auto_dowload_image) {
//                            new download_image().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg.getKey_id(),
//                                    msg.getMedia_url(), fromuser);
//                        } else {
//                            arg0.mLeftProgressDownloadImage
//                                    .setVisibility(View.GONE);
//
//                        }
//                    } else {
//                        arg0.mLeftImageDownload.setVisibility(View.GONE);
//                        arg0.mLeftProgressDownloadImage.setVisibility(View.GONE);
//
//                        File file = new File(Constant.local_image_dir
//                                + msg.getMedia_name());
//                        if (file.isFile()) {
//
//
//                            Bitmap unscaledBitmap = ScalingUtilities
//                                    .decodeFile(Constant.local_image_dir
//                                                    + msg.getMedia_name(), (int) width * 57 / 100,
//                                            (int) height * 28 / 100);
//
//                            /*Log.d("Image Width",
//                                    "Image Width::" + i + " layout::"
//                                            + localView.getWidth()
//                                            + " image view::"
//                                            + image.getWidth()
//                                            + " image view height::"
//                                            + image.getHeight()
//                                            + "bitmap widh::"
//                                            + unscaledBitmap.getWidth());*/
//
//                            arg0.mLeftImageChat.setImageBitmap(unscaledBitmap);
//
//                            arg0.mLeftImageChat.setTag(Constant.local_image_dir
//                                    + msg.getMedia_name());
//
//                        } else {
//                            byte[] image_data = msg.getRow_data();
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(
//                                    image_data, 0, image_data.length);
//                            arg0.mLeftImageChat.setImageBitmap(bitmap);
//                            arg0.mLeftImageChat.setScaleType(ImageView.ScaleType.FIT_XY);
//                            arg0.mLeftImageChat.setTag(null);
//
//                        }
//
//                    }
//
//                    final ProgressBar progress_download_image = arg0.mLeftProgressDownloadImage;
//                    final ImageView btn_image_download = arg0.mLeftImageDownload;
//                    arg0.mLeftImageDownload
//                            .setOnClickListener(new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//
//                                    String[] val = v.getTag().toString()
//                                            .split(",");
//                                    new download_image().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,val[0],
//                                            val[1], val[2]);
//                                    progress_download_image
//                                            .setVisibility(View.VISIBLE);
//                                    btn_image_download
//                                            .setVisibility(View.GONE);
//                                    // TODO Auto-generated method stub
//
//                                }
//                            });
//
//                    arg0.mLeftImageChat.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            if (v.getTag() != null) {
//                                String path = v.getTag().toString();
//                                File f = new File(path);
//                                Uri uri = Uri.fromFile(f);
//
//                                Bitmap bitmap = null;
//
//                                try {
//                                    bitmap = MediaStore.Images.Media
//                                            .getBitmap(context
//                                                            .getContentResolver(),
//                                                    uri);
//                                } catch (FileNotFoundException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                } catch (IOException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
//
//                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                bitmap.compress(Bitmap.CompressFormat.JPEG,
//                                        75, stream);
//                                byte[] byteArray = stream.toByteArray();
//
//                                Intent intent = new Intent(context,
//                                        OrientationGroup.class)
//                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.putExtra("pic", byteArray);
//                                context.startActivity(intent);
//                            }
//
//                        }
//                    });
//                }
//                // //////////////////******LEFT
//                // Video**************///////////////////////////////////////////////////////////////////////
//                else if (Integer.parseInt(msg.getMedia_wa_type()) == 2) {
//                    // Set timestamp
//                    Date date1 = new Date();
//                    date1.setTime(msg_list.get(arg1).getTimestamp());
//                    SimpleDateFormat time_format = new SimpleDateFormat(
//                            "hh:mm a");
//
//                    arg0.mLeftChatTextTime.setText(time_format.format(date1));
//
//
//                    arg0.mLeftVideoChatLayout.setVisibility(View.VISIBLE);
//                    arg0.mLeftVideo.setVisibility(View.VISIBLE);
//
//
//                    arg0.mLeftTxtDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP,
//                            notificatiob_font_size);
//                    arg0.mLeftTxtSize.setTextSize(notificatiob_font_size,
//                            notificatiob_font_size);
//
//                    long second = (msg.getMedia_duration() / 1000) % 60;
//                    long minute = (msg.getMedia_duration() / (1000 * 60)) % 60;
//
//                    String time = String.format("%02d:%02d", minute, second);
//                    arg0.mLeftTxtDuration.setText(time);
//
//                    arg0.mLeftTxtSize.setText(Network_Usage.convertBytesToSuitableUnit(String.valueOf(msg.getMedia_size())));
//
//                    String values = msg.getKey_id() + "," + msg.getMedia_url()
//                            + "," + fromuser;
//
//                    arg0.mLeftBtnDownloadVideo.setTag(values);
//
//                    arg0.mLeftVideo.getLayoutParams().width = (int) width * 57 / 100;
//                    arg0.mLeftVideo.getLayoutParams().height = (int) height * 28 / 100;
//
//                    if (msg.getMedia_name() == null) {
//                        byte[] img_byte = msg.getRow_data();
//                        if (img_byte != null) {
//                            Bitmap bmp = BitmapFactory.decodeByteArray(
//                                    img_byte, 0, img_byte.length);
//                            arg0.mLeftVideo.setImageBitmap(bmp);
//                            arg0.mLeftVideo.setScaleType(ImageView.ScaleType.FIT_XY);
//                            try {
//                                bites = (long) img_byte.length;
//
//                                updateMediaNetwork_Receive(bites);
//                            } catch (Exception e) {
//
//                            }
//                        }
//                        if (is_auto_dowload_video) {
//                            new download_video().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg.getKey_id(),
//                                    msg.getMedia_url(), fromuser);
//                        } else {
//
//                            // //Constant.printMsg("Media Name::"+c.getString(12));
//
//                            arg0.mLeftVideoProgressUpload
//                                    .setVisibility(View.GONE);
//                            arg0.mLeftBtnDownloadVideo.setVisibility(View.VISIBLE);
//                        }
//                        arg0.mLeftBtnVideoPlay.setVisibility(View.GONE);
//                    } else {
//                        arg0.mLeftBtnDownloadVideo.setVisibility(View.GONE);
//                        arg0.mLeftVideoProgressUpload.setVisibility(View.GONE);
//
//                        File file = new File(Constant.local_video_dir
//                                + msg.getMedia_name());
//                        arg0.mLeftBtnVideoPlay.setTag(Constant.local_video_dir
//                                + msg.getMedia_name());
//                        try {
//                            byte[] image_data = msg.getRow_data();
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(
//                                    image_data, 0, image_data.length);
//                            arg0.mLeftVideo.setImageBitmap(bitmap);
//                            arg0.mLeftVideo.setScaleType(ImageView.ScaleType.FIT_XY);
//                            arg0.mLeftVideo.setTag(null);
//                        } catch (Exception e) {
//                            // ACRA.getErrorReporter().handleException(e);
//                            // TODO: handle exception
//                        }
//
//                        if (file.isFile()) {
//
//                            arg0.mLeftBtnVideoPlay.setVisibility(View.VISIBLE);
//
//                        } else {
//
//                            arg0.mLeftBtnVideoPlay.setVisibility(View.GONE);
//
//                        }
//
//                    }
//
//                    arg0.mLeftBtnDownloadVideo
//                            .setOnClickListener(new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//                                    String[] val = v.getTag().toString()
//                                            .split(",");
//                                    new download_video().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,val[0],
//                                            val[1], val[2]);
//                                    arg0.mLeftVideoProgressUpload
//                                            .setVisibility(View.VISIBLE);
//                                    arg0.mLeftBtnDownloadVideo
//                                            .setVisibility(View.GONE);
//
//                                }
//                            });
//
//                    arg0.mLeftBtnVideoPlay.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            // TODO Auto-generated method stub
//                            String uri = v.getTag().toString();
//
//								/*
//                                 * Intent intent = new Intent();
//								 * intent.setAction(Intent.ACTION_VIEW);
//								 * intent.setDataAndType(Uri.parse(uri),
//								 * "video/mp4"); context.startActivity(intent);
//								 */
//                            try {
//                                Intent intent = new Intent();
//                                intent.setAction(Intent.ACTION_VIEW);
//                                intent.setDataAndType(Uri.parse(uri),
//                                        "video/mp4");
//                                context.startActivity(intent);
//                            } catch (Exception e) {
//
//                                AlertDialog.Builder b = new AlertDialog.Builder(
//                                        context);
//                                b.setMessage(
//                                        context.getResources()
//                                                .getString(
//                                                        R.string.dont_have_app_to_play_video))
//                                        .setCancelable(false);
//                                b.setNegativeButton(
//                                        context.getResources().getString(
//                                                R.string.Ok),
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(
//                                                    DialogInterface dialog,
//                                                    int id) {
//
//                                            }
//                                        });
//                                AlertDialog alert = b.create();
//                                alert.show();
//                            }
//
//                        }
//                    });
//                }
//
//                // //////////////////******LEFT
//                // Audio**************///////////////////////////////////////////////////////////////////////
//                else if (msg.getMedia_wa_type().equals("3")) {
//                    // Log.d("Audio view", "Audio view called....");
//                  /*  final ImageButton btn_play = (ImageButton) convertView
//                            .findViewById(R.id.btn_audio_play);
//                    final ImageButton btn_download = (ImageButton) convertView
//                            .findViewById(R.id.btn_audio_upload);
//                    final ImageButton btn_cancel_download = (ImageButton) convertView
//                            .findViewById(R.id.btn_audio_cancel_upload);
//                    final TextView txt_audio_size = (TextView) convertView
//                            .findViewById(R.id.txt_audio_size);
//                    final ProgressBar progress_audio = (ProgressBar) convertView
//                            .findViewById(R.id.progress_audio);
//                    final SeekBar seek_audio = (SeekBar) convertView
//                            .findViewById(R.id.seek_audio_play);
//                    final TextView txt_duration = (TextView) convertView
//                            .findViewById(R.id.txt_audio_length);*/
//
//                    arg0.mLeftAudioChatLayout.setVisibility(View.VISIBLE);
//                    arg0.mLeftAudioCancelDownload.setVisibility(View.VISIBLE);
//                    arg0.mLeftAudioDownload.setVisibility(View.VISIBLE);
//                    arg0.mLeftBtnAudioPlay.setVisibility(View.VISIBLE);
//                    arg0.mLeftBtnAudioPlay.setBackgroundResource(R.drawable.ic_action_audio_play);
//
//                    arg0.mLeftTxtAudioSize.setTextSize(TypedValue.COMPLEX_UNIT_SP,
//                            notificatiob_font_size);
//                    arg0.mLeftTxtAudioLength.setTextSize(TypedValue.COMPLEX_UNIT_SP,
//                            notificatiob_font_size);
//
//                    arg0.mLeftTxtAudioLength.setText(new TimeUtils()
//                            .millisToShortDHMS(msg.getMedia_duration()));
//
//                    arg0.mLeftBtnAudioPlay.setFocusable(false);
//                    arg0.mLeftAudioDownload.setFocusable(false);
//                    arg0.mLeftAudioCancelDownload.setFocusable(false);
//                    arg0.mLeftTxtAudioSize.setFocusable(false);
//                    arg0.mLeftProgressAudio.setFocusable(false);
//                    arg0.mLeftSeekAudioPlay.setFocusable(false);
//
//                    arg0.mLeftSeekAudioPlay.setProgress(0);
//                    arg0.mLeftSeekAudioPlay.setMax(100);
//                    arg0.mLeftTxtAudioSize.setText(new ScalingUtilities()
//                            .humanReadableByteCount(msg.getMedia_size(), true));
//                    final MediaPlayer player = new MediaPlayer();
//                    arg0.mLeftBtnAudioPlay.setTag(Constant.local_audio_dir
//                            + msg.getMedia_name());
//                    arg0.mLeftSeekAudioPlay.setTag(msg.get_id());
//                    String values = msg.getKey_id() + "," + msg.getMedia_url()
//                            + "," + fromuser;
//
//                    arg0.mLeftAudioDownload.setTag(values);
//                    list.add(player);
//                    PlayBtnlist.add(arg0.mLeftBtnAudioPlay);
//
//                    arg0.mLeftAudioCancelDownload
//                            .setOnClickListener(new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//                                    // TODO Auto-generated method stub
//
//                                    new download_Audio().cancel(true);
//                                    arg0.mLeftAudioDownload
//                                            .setVisibility(View.VISIBLE);
//                                    arg0.mLeftProgressAudio.setVisibility(View.GONE);
//                                    arg0.mLeftBtnAudioPlay.setVisibility(View.GONE);
//                                    // txt_audio_size.setVisibility(View.VISIBLE);
//                                    arg0.mLeftAudioCancelDownload
//                                            .setVisibility(View.GONE);
//                                    arg0.mLeftSeekAudioPlay.setVisibility(View.GONE);
//
//                                }
//                            });
//                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//                        @Override
//                        public void onCompletion(MediaPlayer mp) {
//                            // TODO Auto-generated method stub
//                            // Log.d("Play", "Player Finished.....");
//                            seekHandler.removeCallbacks(run);
//                            arg0.mLeftSeekAudioPlay.setProgress(0);
//
//                            if (s < Integer.valueOf(listTemp.get(
//                                    listTemp.size() - 1).toString())
//                                    && mFirstRun == true) {
//
//                                s++;
//                                if (s == Integer.valueOf(listTemp.get(
//                                        listTemp.size() - 1).toString()) - 1) {
//                                    s = Integer
//                                            .valueOf(listTemp.get(
//                                                    listTemp.size() - 1)
//                                                    .toString()) + 1;
//                                    mFirstRun = false;
//
//                                }
//                                Constant.printMsg("oooooooooooooolLL"
//                                        + s
//                                        + "     "
//                                        + listTemp.get(listTemp.size() - 1)
//                                        .toString());
//
//                            } else {
//
//                                Constant.printMsg("ooooooooooooool" + s);
//                                arg0.mLeftBtnAudioPlay.setBackgroundResource(R.drawable.ic_action_audio_play);
//                            }
//
//                        }
//                    });
//
//                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//
//                        @Override
//                        public void onPrepared(MediaPlayer mp) {
//                            // TODO Auto-generated method stub
//                            // Log.d("Player", "Player prepared....");
//                            mp.start();
//                            arg0.mLeftSeekAudioPlay.setProgress(0);
//                            arg0.mLeftSeekAudioPlay.setMax(mp.getDuration());
//                        }
//                    });
//
//                    class run implements Runnable {
//
//                        MediaPlayer mp;
//                        SeekBar seek;
//
//                        public run(MediaPlayer m, SeekBar se) {
//                            mp = m;
//                            seek = se;
//                        }
//
//                        @Override
//                        public void run() {
//
//                            if (player.isPlaying()) {
//                                seek.setProgress(mp.getCurrentPosition());
//                                arg0.mLeftTxtAudioLength.setText(new TimeUtils()
//                                        .millisToShortDHMS(mp
//                                                .getCurrentPosition()));
//									/*
//									 * Log.d("Current Duration",
//									 * "Current Postion::" +
//									 * mp.getCurrentPosition() +
//									 * " Seek Potition::" + seek.getProgress());
//									 */
//                            }
//                            seekHandler.postDelayed(this, 1);
//                        }
//
//                    }
//
//                    if (msg.getMedia_name() == null) {
//                        if (is_auto_dowload_audio) {
//                            new download_Audio().execute(msg.getKey_id(),
//                                    msg.getMedia_url(), fromuser);
//                            arg0.mLeftAudioDownload.setVisibility(View.GONE);
//                            arg0.mLeftAudioCancelDownload.setVisibility(View.VISIBLE);
//                            arg0.mLeftProgressAudio.setVisibility(View.VISIBLE);
//                            arg0.mLeftBtnAudioPlay.setVisibility(View.GONE);
//                            // txt_audio_size.setVisibility(View.GONE);
//                            arg0.mLeftSeekAudioPlay.setVisibility(View.GONE);
//                        } else {
//                            arg0.mLeftAudioDownload.setVisibility(View.VISIBLE);
//                            arg0.mLeftAudioCancelDownload.setVisibility(View.GONE);
//                            arg0.mLeftProgressAudio.setVisibility(View.GONE);
//                            arg0.mLeftBtnAudioPlay.setVisibility(View.GONE);
//                            // txt_audio_size.setVisibility(View.VISIBLE);
//                            arg0.mLeftSeekAudioPlay.setVisibility(View.GONE);
//                        }
//                    } else {
//                        arg0.mLeftAudioDownload.setVisibility(View.GONE);
//                        arg0.mLeftAudioCancelDownload.setVisibility(View.GONE);
//                        arg0.mLeftProgressAudio.setVisibility(View.GONE);
//                        arg0.mLeftBtnAudioPlay.setVisibility(View.VISIBLE);
//                        // txt_audio_size.setVisibility(View.GONE);
//                        arg0.mLeftSeekAudioPlay.setVisibility(View.VISIBLE);
//                    }
//
//                    arg0.mLeftAudioDownload.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            String[] val = v.getTag().toString().split(",");
//
//								/* new uploa_audio().execute(val[0],val[1]); */
//
//                            new download_Audio().execute(val[0], val[1],
//                                    val[2]);
//
//                            arg0.mLeftAudioDownload.setVisibility(View.GONE);
//                            arg0.mLeftProgressAudio.setVisibility(View.VISIBLE);
//                            arg0.mLeftBtnAudioPlay.setVisibility(View.GONE);
//                            // txt_audio_size.setVisibility(View.GONE);
//                            arg0.mLeftAudioCancelDownload.setVisibility(View.VISIBLE);
//                            arg0.mLeftSeekAudioPlay.setVisibility(View.GONE);
//                            ;
//                        }
//
//                    });
//
//                    arg0.mLeftBtnAudioPlay.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            // TODO Auto-generated method stub
//                            if (mPressed == 0) {
//                                TempBtn = new ArrayList();
//                            }
//                            mPressed = 1;
//
//                            listTemp.add(list.size());
//                            if (mAudioTagValue.equalsIgnoreCase((String) v
//                                    .getTag())) {
//
//                                Constant.printMsg("Oooop" + mAudioTagValue
//                                        + "   " + (String) v.getTag());
//
//                                mAudioTagValue = (String) v.getTag();
//
//                            } else {
//                                mAudioTagValue = (String) v.getTag();
//                                for (int i = 0; i < list.size(); i++) {
//
//                                    Constant.printMsg("KKKKKKP"
//                                            + v.getTag() + "    "
//                                            + list.get(i));
//
//                                    MediaPlayer plays = list.get(i);
//
//                                    // if (v.getTag().equals(plays.gette))
//
//                                    plays.stop();
//
//                                }
//                            }
//                            if (player.isPlaying()) {
//                                seekHandler.removeCallbacks(run);
//                                player.pause();
//                                // run.suspend();
//
//                                // length=player.getCurrentPosition();
//                                arg0.mLeftAudioDownload.setVisibility(View.GONE);
//                                arg0.mLeftProgressAudio.setVisibility(View.GONE);
//                                arg0.mLeftBtnAudioPlay.setVisibility(View.VISIBLE);
//                                // txt_audio_size.setVisibility(View.GONE);
//                                arg0.mLeftAudioCancelDownload
//                                        .setVisibility(View.GONE);
//                                arg0.mLeftSeekAudioPlay.setVisibility(View.VISIBLE);
//                                // btn_play.setImageDrawable(context
//                                // .getResources()
//                                // .getDrawable(
//                                // R.drawable.ic_action_audio_play));
//                                arg0.mLeftBtnAudioPlay.setBackgroundResource(R.drawable.ic_action_audio_play);
//
//                            } else {
//
//                                // Log.d("Audio", "Path::" +
//                                // v.getTag().toString());
//
//                                seekHandler.removeCallbacks(run);
//
//                                try {
//                                    player.reset();
//                                    player.setDataSource(v.getTag()
//                                            .toString());
//                                    player.prepare();
//                                    current_song = String.valueOf(arg0.mLeftBtnAudioPlay
//                                            .getTag());
//										/* player.prepareAsync(); */
//                                } catch (Exception e) {
//                                    // ACRA.getErrorReporter().handleException(e);
//                                    // TODO: handle exception
//                                }
//                                seekHandler.postDelayed(new run(player,
//                                        arg0.mLeftSeekAudioPlay), 100);
//
//                                arg0.mLeftBtnAudioPlay.setBackgroundResource(R.drawable.ic_action_audio_pause);
//
//                            }
//                            for (int i = 0; i < PlayBtnlist.size(); i++) {
//
//                                ImageButton btn = PlayBtnlist.get(i);
//
//                                if (btn.getTag().equals(v.getTag())
//                                        && mBtnClick == 0) {
//
//                                    Constant.printMsg("CCCCCCCCCCCCCCCAA");
//
//                                    btn.setBackgroundResource(R.drawable.ic_action_audio_pause);
//                                    mBtnClick++;
//
//                                } else {
//
//                                    Constant.printMsg("CCCCCCCCCCCCCCCAB");
//
//                                    mBtnClick = 0;
//                                    btn.setBackgroundResource(R.drawable.ic_action_audio_play);
//                                }
//
//                            }
//                            if (TempBtn.size() > 0) {
//
//                                if (v.getTag().equals(
//                                        TempBtn.get(TempBtn.size() - 1)
//                                                .toString())) {
//
//                                    arg0.mLeftBtnAudioPlay.setBackgroundResource(R.drawable.ic_action_audio_play);
//                                    TempBtn = new ArrayList();
//
//                                } else {
//
//                                    arg0.mLeftBtnAudioPlay.setBackgroundResource(R.drawable.ic_action_audio_pause);
//                                    TempBtn = new ArrayList();
//                                    TempBtn.add(v.getTag());
//
//                                }
//
//                            } else {
//
//                                Constant.printMsg("ooloolool");
//                                arg0.mLeftBtnAudioPlay.setBackgroundResource(R.drawable.ic_action_audio_pause);
//                                TempBtn.add(v.getTag());
//
//                            }
//
//                        }
//
//                    });
//                    // Log.d("Audio view", "Audio view ended....");
//                }
//                // //////////////////******LEFT
//                // Contact**************///////////////////////////////////////////////////////////////////////
//                else if (msg.getMedia_wa_type().equals("5")) {
//
//                    arg0.mLeftContactChatLayout.setVisibility(View.VISIBLE);
//
//                    arg0.mLeftContactBtnViewContact.setText(msg.getMedia_name());
//                    String values = msg.getData();
//                    arg0.mLeftContactBtnViewContact.setTag(values);
//                    arg0.mLeftContactBtnViewContact.setTextSize(
//                            TypedValue.COMPLEX_UNIT_SP, msg_font_size);
//                    try {
//                        byte[] image_data = msg.getRow_data();
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(
//                                image_data, 0, image_data.length);
//                        arg0.mLeftContactImgContactAvatar.setImageBitmap(bitmap);
//                    } catch (Exception e) {
//                        // TODO: handle exception
//                        // ACRA.getErrorReporter().handleException(e);
//                    }
//
//                    arg0.mLeftContactImgContactAvatar.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            if (v.getTag() != null) {
//
//                            }
//
//                        }
//                    });
//
//                    arg0.mLeftContactBtnViewContact
//                            .setOnClickListener(new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//
//                                    Intent intent = new Intent(context,
//                                            ContactView.class);
//                                    intent.putExtra("vcard", v.getTag()
//                                            .toString());
//                                    intent.putExtra("status", true);
//                                    context.startActivity(intent);
//
//                                }
//                            });
//
//                }
//                // //////////////////******LEFT
//                // Location**************///////////////////////////////////////////////////////////////////////
//                else if (msg.getMedia_wa_type().equals("4")) {
//
//                   /* ImageView image = (ImageView) convertView
//                            .findViewById(R.id.map_image);*/
//                    arg0.mLeftImageChatLayout.setVisibility(View.VISIBLE);
//                    arg0.mLeftImageChat.setVisibility(View.VISIBLE);
//
//                    Log.d("Location", "Lat::" + msg.getLatitude() + " Lon::"
//                            + msg.getLongitude());
//                    String lat_lon = msg.getLatitude() + ","
//                            + msg.getLongitude();
//                    arg0.mLeftImageChat.setTag(lat_lon);
//
//                    arg0.mRightImageChat.getLayoutParams().width = (int) width * 57 / 100;
//                    arg0.mRightImageChat.getLayoutParams().height = (int) height * 30 / 100;
//
//
//                    String values = msg.get_id() + "," + fromuser;
//
//                    try {
//                        byte[] image_data = msg.getRow_data();
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(
//                                image_data, 0, image_data.length);
//                        arg0.mLeftImageChat.setImageBitmap(bitmap);
//                    } catch (Exception e) {
//                        // ACRA.getErrorReporter().handleException(e);
//                        // TODO: handle exception
//                    }
//                    if (msg.getStatus() == 1 && msg.getNeeds_push() == 1) {
//
//                        // new
//                        // uploa_Location().execute(c.getString(0),fromuser);
//                    } else if (msg.getStatus() == 1 && msg.getNeeds_push() == 0) {
//
//                    } else {
//
//                    }
//
//                    arg0.mLeftImageChat.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            if (v.getTag() != null) {
//                                String latitude = v.getTag().toString()
//                                        .split(",")[0];
//                                String longitude = v.getTag().toString()
//                                        .split(",")[1];
//                                String label = "I'm Here!";
//                                String uriBegin = "geo:" + latitude + ","
//                                        + longitude;
//                                String query = latitude + "," + longitude
//                                        + "(" + label + ")";
//                                String encodedQuery = Uri.encode(query);
//                                String uriString = uriBegin + "?q="
//                                        + encodedQuery + "&z=16";
//                                Uri uri = Uri.parse(uriString);
//                                String url = "http://maps.google.com/maps?center='" + v.getTag().toString() + "'&zoom=15&views=transit";
//                                try {
//                                    Intent mapIntent = new Intent(
//                                            Intent.ACTION_VIEW,
//                                            uri);
//                                    mapIntent.setPackage("com.google.android.apps.maps");
//                                    context.startActivity(mapIntent);
//                                } catch (ActivityNotFoundException exp) {
//                                    context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                                }
//
//
//                            }
//
//                        }
//                    });
//
//                }
//
//            }
//
//        }
//
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        Constant.printMsg("MUCTest adapter item count:" + msg_list.size());
//        // TODO Auto-generated method stub
//        return msg_list.size();
//    }
//
//    public void mScreenArrangement(MUCChatViewHolder vrsHolder) {
//
//
//        FrameLayout.LayoutParams rgt_chat_param = new FrameLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        rgt_chat_param.rightMargin = (int) width * 17 / 100;
//        vrsHolder.mRightChatLayout.setLayoutParams(rgt_chat_param);
//
//        LinearLayout.LayoutParams left_chat_param = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        left_chat_param.leftMargin = (int) width * 17 / 100;
//        left_chat_param.width = (int) width * 63 / 100;
//        left_chat_param.bottomMargin = (int) height * 1 / 100;
//        vrsHolder.mLeftBackgroundLayout.setLayoutParams(left_chat_param);
//        vrsHolder.mLeftBackgroundLayout.setPadding((int) width * 3 / 100, 0, 0, 0);
//
//        LinearLayout.LayoutParams rgt_text_params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        rgt_text_params.gravity = Gravity.CENTER_VERTICAL;
//        rgt_text_params.width = (int) width * 63 / 100;
////        rgt_text_params.gravity = Gravity.LEFT;
//        rgt_text_params.topMargin = (int) width * 1 / 100;
//        rgt_text_params.bottomMargin = (int) height * 2 / 100;
//        //rgt_text_params.leftMargin = (int) height * 3 / 100;
//        vrsHolder.mRightTextLayout.setLayoutParams(rgt_text_params);
//        vrsHolder.mRightTextLayout.setPadding((int) width * 3 / 100, 0, (int) width * 3 / 100, 0);
//
//        LinearLayout.LayoutParams rgt_background_params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        rgt_background_params.width = (int) width * 63 / 100;
//        vrsHolder.mRightChatBackground.setLayoutParams(rgt_background_params);
//        vrsHolder.mRightChatBackground.setPadding(0, 0, (int) width * 3 / 100, 0);
//
//        LinearLayout.LayoutParams lft_text_params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        lft_text_params.width = (int) width * 61 / 100;
//        lft_text_params.gravity = Gravity.TOP;
////        rgt_text_params.gravity = Gravity.LEFT;
//        // lft_text_params.bottomMargin = (int) height * 3 / 100;
//        lft_text_params.topMargin = (int) width * 1 / 100;
//        lft_text_params.bottomMargin = (int) height * 2 / 100;
//        vrsHolder.mLeftTextLayout.setLayoutParams(lft_text_params);
//        vrsHolder.mLeftTextLayout.setPadding((int) width * 3 / 100, 0, (int) width * 3 / 100, 0);
//
//        FrameLayout.LayoutParams rgt_chat_tip_params = new FrameLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        rgt_chat_tip_params.width = (int) width * 4 / 100;
//        rgt_chat_tip_params.height = (int) width * 6 / 100;
//        rgt_chat_tip_params.leftMargin = (int) width * 83 / 100;
//        vrsHolder.mRightChatTipImg.setLayoutParams(rgt_chat_tip_params);
//
//        FrameLayout.LayoutParams rgt_img_params = new FrameLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        rgt_img_params.width = (int) width * 13 / 100;
//        rgt_img_params.height = (int) width * 13 / 100;
//        rgt_img_params.leftMargin = (int) width * 85 / 100;
//        //  rgt_img_params.topMargin = (int) width * 1 / 100;
////        int margin = (int) width * 2 / 100;
////        rgt_img_params.setMargins(margin, margin, margin, margin);
//        vrsHolder.mRightSenderImage.setLayoutParams(rgt_img_params);
//
//
//        LinearLayout.LayoutParams left_img_params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        left_img_params.width = (int) width * 13 / 100;
//        left_img_params.height = (int) width * 13 / 100;
//        left_img_params.leftMargin = (int) width * 2 / 100;
//        vrsHolder.mLeftSenderImage.setLayoutParams(left_img_params);
//
//        LinearLayout.LayoutParams rgt_status_params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        //rgt_status_params.rightMargin = (int) width * 17 / 100;
//        rgt_status_params.height = (int) width * 6 / 100;
//
//        rgt_status_params.width = (int) width * 63 / 100;
//        rgt_status_params.bottomMargin = (int) height * 1 / 100;
//        vrsHolder.mRightChatStatusLayout.setLayoutParams(rgt_status_params);
//        // vrsHolder.mRightChatStatusLayout.setPadding(0, 0, 0, 8);
//
//        LinearLayout.LayoutParams lft_status_params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        //  lft_status_params.leftMargin = (int) width * 17 / 100;
//        //  lft_status_params.bottomMargin = (int) width * 3 / 100;
//        vrsHolder.mLeftChatStatusLayout.setLayoutParams(lft_status_params);
//
//        FrameLayout.LayoutParams lft_chat_tip_params = new FrameLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        lft_chat_tip_params.width = (int) width * 5 / 100;
//        lft_chat_tip_params.height = (int) width * 6 / 100;
//        lft_chat_tip_params.leftMargin = (int) width * 13 / 100;
//        vrsHolder.mLeftChatTip.setLayoutParams(lft_chat_tip_params);
//
//
//        FrameLayout.LayoutParams rgt_upl_img_params = new FrameLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        rgt_upl_img_params.width = (int) width * 57 / 100;
//        rgt_upl_img_params.height = (int) height * 28 / 100;
//        rgt_upl_img_params.topMargin = (int) height * 2 / 100;
//        rgt_upl_img_params.bottomMargin = 5;
//        rgt_upl_img_params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
//
//        vrsHolder.mRightImageChat.setLayoutParams(rgt_upl_img_params);
//        vrsHolder.mRightVideo.setLayoutParams(rgt_upl_img_params);
//
//
//        FrameLayout.LayoutParams left_download_icon_params = new FrameLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        left_download_icon_params.width = (int) width * 15/100;
//        left_download_icon_params.height = (int) width *15/100;
//        left_download_icon_params.gravity=Gravity.CENTER;
//        vrsHolder.mLeftImageDownload.setLayoutParams(left_download_icon_params);
//        vrsHolder.mLeftBtnDownloadVideo.setLayoutParams(left_download_icon_params);
//        vrsHolder.mBtnUploadVideo.setLayoutParams(left_download_icon_params);
//        vrsHolder.mRightImageUploadImage.setLayoutParams(left_download_icon_params);
//        vrsHolder.mBtnVideoPlay.setLayoutParams(left_download_icon_params);
//        vrsHolder.mLeftBtnVideoPlay.setLayoutParams(left_download_icon_params);
//
//
//        LinearLayout.LayoutParams rgt_contact_params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        rgt_contact_params.width = (int) width * 57 / 100;
//        rgt_contact_params.topMargin = (int) height * 2 / 100;
//        rgt_contact_params.bottomMargin = 5;
//        rgt_contact_params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
//        vrsHolder.mRightContactChatLayout.setLayoutParams(rgt_contact_params);
//
////        LinearLayout.LayoutParams lft_contact_params = new LinearLayout.LayoutParams(
////                LinearLayout.LayoutParams.WRAP_CONTENT,
////                LinearLayout.LayoutParams.WRAP_CONTENT);
////        lft_contact_params.width = (int) width * 57 / 100;
////        lft_contact_params.bottomMargin = 5;
////        lft_contact_params.gravity = Gravity.LEFT | Gravity.LEFT;
////        vrsHolder.mLeftContactChatLayout.setLayoutParams(lft_contact_params);
//
//        FrameLayout.LayoutParams lft_upl_img_params = new FrameLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        lft_upl_img_params.width = (int) width * 57 / 100;
//        lft_upl_img_params.height = (int) height * 28 / 100;
//        lft_upl_img_params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
//        vrsHolder.mLeftImageChat.setLayoutParams(lft_upl_img_params);
//
//        FrameLayout.LayoutParams rgt_audio_params = new FrameLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        rgt_audio_params.width = (int) width * 56 / 100;
//        rgt_audio_params.gravity = Gravity.CENTER;
//        vrsHolder.mSeekAudioPlay.setLayoutParams(rgt_audio_params);
//        vrsHolder.mProgressAudio.setLayoutParams(rgt_audio_params);
//        vrsHolder.mLeftSeekAudioPlay.setLayoutParams(rgt_audio_params);
//
//        LinearLayout.LayoutParams rgtaudio_length_params = new LinearLayout.LayoutParams(
//                FrameLayout.LayoutParams.WRAP_CONTENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
//        rgtaudio_length_params.width = (int) width * 44 / 100;
//        rgtaudio_length_params.leftMargin = 3;
//        vrsHolder.mLeftTxtAudioSize.setLayoutParams(rgtaudio_length_params);
//
//        LinearLayout.LayoutParams rgt_img_layout_params = new LinearLayout.LayoutParams(
//                FrameLayout.LayoutParams.WRAP_CONTENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
//        vrsHolder.mLeftImageChatLayout.setLayoutParams(rgt_img_layout_params);
//        vrsHolder.mLeftVideoChatLayout.setLayoutParams(rgt_img_layout_params);
//        vrsHolder.mLeftAudioChatLayout.setLayoutParams(rgt_img_layout_params);
//
//        LinearLayout.LayoutParams rgt_contact_layout_params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        rgt_contact_layout_params.bottomMargin = (int) height * 1 / 100;
//        vrsHolder.mLeftContactChatLayout.setLayoutParams(rgt_contact_layout_params);
//
//        mTextSizes(vrsHolder);
//
//    }
//
//    public void mTextSizes(MUCChatViewHolder vrsHolder) {
//        if (width >= 600) {
//
//            vrsHolder.mRightChatText.setTextSize(18);
//            vrsHolder.mRightChatText.setEmojiconSize(48);
//
//            vrsHolder.mLeftChatText.setTextSize(18);
//            vrsHolder.mLeftChatText.setEmojiconSize(48);
//
//        } else if (width > 501 && width < 600) {
//
//            vrsHolder.mRightChatText.setTextSize(16);
//            vrsHolder.mRightChatText.setEmojiconSize(46);
//
//            vrsHolder.mLeftChatText.setTextSize(16);
//            vrsHolder.mLeftChatText.setEmojiconSize(46);
//
//
//        } else if (width > 331 && width < 500) {
//
//            vrsHolder.mRightChatText.setTextSize(14);
//            vrsHolder.mRightChatText.setEmojiconSize(44);
//
//            vrsHolder.mLeftChatText.setTextSize(14);
//            vrsHolder.mLeftChatText.setEmojiconSize(44);
//
//
//        } else if (width > 260 && width < 330) {
//
//            vrsHolder.mRightChatText.setTextSize(13);
//            vrsHolder.mRightChatText.setEmojiconSize(42);
//
//            vrsHolder.mLeftChatText.setTextSize(13);
//            vrsHolder.mLeftChatText.setEmojiconSize(42);
//
//
//        } else if (width <= 260) {
//
//            vrsHolder.mRightChatText.setTextSize(12);
//            vrsHolder.mRightChatText.setEmojiconSize(42);
//
//            vrsHolder.mLeftChatText.setTextSize(12);
//            vrsHolder.mLeftChatText.setEmojiconSize(42);
//
//
//        }
//    }
//
//    void myMethod(String value) {
//        Constant.printMsg("Priya Test Nymn mymethod " + value);
//        this.Normallist = ACRAConstants.DEFAULT_STRING_VALUE;
//        String[] arr = value.split(" ");
//        for (String ss : arr) {
//            if (!ss.isEmpty()) {
//                Constant.printMsg("Priya Test Nymn ss " + ss);
//                if (ss.contains("<n>") && ss.contains("</n>")
//                        && ss.contains("<m>") && ss.contains("</m>")) {
//                    String[] subSplits = ss.split("</n>");
//                    Constant.printMsg("Priya Test Nymn subSplits "
//                            + subSplits);
//                    if (subSplits.length > 0) {
//                        String subSplitsMeaning = ss.substring(subSplits[0]
//                                .length() + 4);
//                        Constant.printMsg("Priya Test Nymn subSplitsMeaning "
//                                + subSplitsMeaning + "  k  " + 0
//                                + "  length " + subSplits[0].length()
//                                + "  "
//                                + ss.substring(subSplits[0].length()));
//                        this.Nemelist.add(subSplits[0].substring(3,
//                                subSplits[0].length()));
//                        mMessegeList.add(subSplits[0].substring(3,
//                                subSplits[0].length()));
//
//                        if (this.Normallist == null) {
//                            this.Normallist += "["
//                                    + subSplits[0].substring(3,
//                                    subSplits[0].length()) + "]";
//                        } else {
//                            if (!this.Normallist.isEmpty()) {
//                                this.Normallist += " ";
//                            }
//                            this.Normallist += "["
//                                    + subSplits[0].substring(3,
//                                    subSplits[0].length()) + "]";
//                        }
//                        this.Meaninglist.add(subSplitsMeaning.substring(3,
//                                subSplitsMeaning.length() - 4));
//                        mMeaningList.add(subSplitsMeaning.substring(3,
//                                subSplitsMeaning.length() - 4));
//                        Constant.printMsg("Priya Test Nymn List"
//                                + this.Nemelist + "  " + this.Meaninglist);
//                        Constant.printMsg("kkkkk"
//                                + subSplitsMeaning.substring(3,
//                                subSplitsMeaning.length() - 4));
//                    }
//                } else {
//                    Constant.printMsg("Priya Test Nymn subSplitsMeaning else "
//                            + ss);
//                    if (this.Normallist.isEmpty()) {
//                        this.Normallist += ss;
//                    } else {
//                        this.Normallist += " " + ss;
//                    }
//                }
//            }
//        }
//        Constant.printMsg("OOOOOOOOOOOOOOO" + this.Normallist + this.Nemelist
//                + this.Meaninglist);
//        Constant.printMsg("Priya Test Nymn NormalList" + this.Normallist);
//
//    }
//
//    private SpannableStringBuilder addClickablePart(String str, EmojiconTextView mRightChatText) {
//
//        // if (str.indexOf(0) == '<') {
//        // str = str.substring(2);
//        // }
//
//        Constant.printMsg("Priya Test Nymn Spannable" + str + "--"
//                + replacePosition);
//
//        this.ssb = new SpannableStringBuilder(str);
//        this.mRemove = str;
//        this.idx1 = str.indexOf("[");
//        this.idx2 = 0;
//        while (this.idx1 != -1) {
//            this.idx2 = str.indexOf("]", this.idx1) + 1;
//            this.ssb.setSpan(
//                    new ClickSpan(str.substring(this.idx1, this.idx2), mRightChatText),
//                    this.idx1, this.idx2, 0);
//            NoUnderlineSpan noUnderline = new NoUnderlineSpan();
//            this.ssb.setSpan(
//                    new ForegroundColorSpan(SupportMenu.CATEGORY_MASK),
//                    this.idx1, this.idx2 - 1, 33);
//            this.ssb.setSpan(
//                    new BackgroundColorSpan(Color.parseColor("#ffe74d")),
//                    this.idx1, this.idx2 - 2, 33);
//            if (this.bClick) {
//                this.bClick = false;
//            }
//            this.ssb.setSpan(
//                    new ForegroundColorSpan(Color.parseColor("#00123654")),
//                    this.idx2 - 2, this.idx2, 33);
//
//            this.ssb.setSpan(noUnderline, this.idx1, this.idx2 - 1, 33);
//            this.idx1 = str.indexOf("[", this.idx2);
//        }
//        String guess = "[";
//        String guess1 = "]";
//        String[] words = this.Normallist.split("\\s+");
//        int index = this.Normallist.indexOf(guess);
//        while (index >= 0) {
//            this.Normallist = removeCharAt(this.Normallist, index);
//            this.ssb.replace(index, index + 1,
//                    ACRAConstants.DEFAULT_STRING_VALUE);
//            index = this.Normallist.indexOf(guess, index);
//        }
//        index = this.Normallist.indexOf(guess1);
//        while (index >= 0) {
//            this.Normallist = removeCharAt(this.Normallist, index);
//            this.ssb.replace(index, index + 1,
//                    ACRAConstants.DEFAULT_STRING_VALUE);
//            index = this.Normallist.indexOf(guess1, index);
//        }
//
//        replacePosition = new ArrayList<String>();
//        for (int i = 0; i < ssb.toString().length(); i++) {
//            char tempCharecter = ssb.toString().charAt(i);
//            if (tempCharecter == Constant.mNynmsSpecialCharacter) {
//                replacePosition.add(String.valueOf(i));
//
//            }
//
//        }
//
//        for (int i = 0; i < replacePosition.size(); i++) {
//            this.ssb.setSpan(
//                    new ForegroundColorSpan(Color.parseColor("#00123654")),
//                    Integer.valueOf(replacePosition.get(i).toString()),
//                    Integer.valueOf(replacePosition.get(i).toString()) + 1, 33);
//        }
//
//        return this.ssb;
//    }
//
//    private String removeCharAt(String str, int i) {
//        // TODO Auto-generated method stub
//        return str.substring(0, i) + str.substring(i + 1);
//    }
//
//    // Updating the Shared Preference with Media Receive Network Usage....
//    public void updateMediaNetwork_Receive(long upValue) {
//        long val = 0;
//        HashMap<String, String> user = mNetworkSharPref
//                .getMedia_ReceiveDetails();
//
//        String value = user.get(NetworkSharedPreference.KEY_MEDIA_GET_RX);
//        if (value != null) {
//
//            val = Long.valueOf(value);
//
//        }
//        long update = upValue + val;
//
//        String data = String.valueOf(update);
//
//        Constant.printMsg("Media grp val:" + data);
//
//        mNetworkSharPref.setMediaData_Rec(data);
//
//    }
//
//    public void toggleSelection(int position) {
//        selectView(position, !mSelectedItemsIds.get(position));
//    }
//
//    public void removeSelection() {
//        mSelectedItemsIds = new SparseBooleanArray();
//        notifyDataSetChanged();
//    }
//
//    public void selectView(int position, boolean value) {
//        if (value)
//            mSelectedItemsIds.put(position, value);
//        else
//            mSelectedItemsIds.delete(position);
//
//        notifyDataSetChanged();
//    }
//
//    public int getSelectedCount() {
//        return mSelectedItemsIds.size();
//    }
//
//
//    // ////////////////////////----Image
//    // Download-----//////////////////////////////////////////////////////////////////////
//
//    public SparseBooleanArray getSelectedIds() {
//        return mSelectedItemsIds;
//    }
//
//    public void insertzzleDB(ContentValues v) {
//
//        try {
//            int a = (int) db.open().getDatabaseObj()
//                    .insert(Dbhelper.TABLE_ZZLE, null, v);
//
//            Constant.printMsg("No of inserted rows in shop details :::::::::"
//                    + a);
//        } catch (SQLException e) {
//            Constant.printMsg("Sql exception in new shop details ::::::"
//                    + e.toString());
//        } finally {
//            db.close();
//        }
//
//    }
//
//    protected void insertToLEDDB(ContentValues cv) {
//        // TODO Auto-generated method stub
//        try {
//            int a = (int) db.open().getDatabaseObj()
//                    .insert(Dbhelper.TABLE_LED, null, cv);
//            Constant.printMsg("No of inserted rows in zzle :::::::::" + a);
//        } catch (SQLException e) {
//            Constant.printMsg("Sql exception in ecpl details ::::::"
//                    + e.toString());
//        } finally {
//            db.close();
//        }
//    }
//
//    class MUCChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
//
//        // Right chat view initalization
//        FrameLayout mRightFrameLayout;
//        LinearLayout mRightAudioMainLayout;
//        LinearLayout mRightChatLayout, mRightAudioChatLayout, mRightContactChatLayout, mRightFileChatLayout, mRightTextLayout,
//                mRightChatStatusLayout, mRightChatBackground;
//        TextView mTxtNotice, mTxtDate, mTxtDuration, mTxtSize, mTxtAudioSize,
//                mTxtAudioLength, mTxtFileName, mTxtFileSize, mRightChatTimeTxt;
//        EmojiconTextView mRightChatText;
//        FrameLayout mRightImageChatLayout, mRightVideoChatLayout;
//        ImageView mRightImageChat, mRightImageUploadImage, mRightVideo,
//                mBtnUploadVideo, mBtnVideoPlay, mBtnAudioUpload,
//                mBtnAudioCancelUpload, mImgContactAvatar, mBtnUploadFile,
//                mBtnViewFile, mRightChatStatusImg, mRightChatTipImg;
//        ImageButton mBtnAudioPlay;
//        ProgressBar mRightImageProgressUpload, mProgressVideoUploadImage,
//                mProgressAudio, mProgressFile;
//        SeekBar mSeekAudioPlay;
//        Button mBtnViewContact;
//        RounderImageView mRightSenderImage;
//
//        // Left chat view initalization
//        LinearLayout mLeftChatLayout, mLeftAudioChatLayout,
//                mLeftContactChatLayout, mLeftFileChatLayout, mLeftTextLayout, mLeftChatStatusLayout, mLeftBackgroundLayout;
//        TextView mLeftTxtDuration, mLeftTxtSize,
//                mLeftTxtAudioSize, mLeftTxtAudioLength,
//                mLeftContactUsername, mLeftFileUsername, mLeftFileTxtFileName,
//                mLeftFileTxtFileSize, mLeftChatTextTime, mLeftChatUsername;
//        EmojiconTextView mLeftChatText;
//        FrameLayout mLeftImageChatLayout, mLeftVideoChatLayout;
//        ImageView mLeftImageChat, mLeftImageDownload, mLeftVideo,
//                mLeftBtnDownloadVideo, mLeftBtnVideoPlay,
//                mLeftAudioDownload, mLeftAudioCancelDownload,
//                mLeftContactImgContactAvatar, mBtnDownloadFile,
//                mLeftFileBtnViewFile, mLeftChatTip;
//        ImageButton mLeftBtnAudioPlay;
//        ProgressBar mLeftVideoProgressUpload, mLeftProgressDownloadImage,
//                mLeftProgressAudio, mLeftFileProgressFile;
//        SeekBar mLeftSeekAudioPlay;
//        Button mLeftContactBtnViewContact;
//        RounderImageView mLeftSenderImage;
//
//        LinearLayout mAddBlockLayout;
//
//        public MUCChatViewHolder(View view) {
//            super(view);
//            view.setTag(view);
//            view.setOnClickListener(this);
//            view.setOnLongClickListener(this);
//
//            //Commong msgs intialize
//            mTxtNotice = (TextView) view.findViewById(R.id.txt_notice);
//            mTxtDate = (TextView) view.findViewById(R.id.txt_date);
//
//            //Right msg
//
//
//            mRightFrameLayout = (FrameLayout) view
//                    .findViewById(R.id.right_frame_layout);
//            mRightTextLayout = (LinearLayout) view
//                    .findViewById(R.id.right_text_layout);
//            mRightChatLayout = (LinearLayout) view
//                    .findViewById(R.id.right_chat_layout);
//            mRightAudioChatLayout = (LinearLayout) view
//                    .findViewById(R.id.right_audio_chat_layout);
//            mRightContactChatLayout = (LinearLayout) view
//                    .findViewById(R.id.right_contact_chat_layout);
//            mRightFileChatLayout = (LinearLayout) view
//                    .findViewById(R.id.right_file_chat_layout);
//            mRightAudioMainLayout = (LinearLayout) view
//                    .findViewById(R.id.right_audio_layout);
//            mAddBlockLayout = (LinearLayout) view
//                    .findViewById(R.id.contact_layout);
//            mRightChatStatusLayout = (LinearLayout) view
//                    .findViewById(R.id.right_chat_status_layout);
//            mRightChatBackground = (LinearLayout) view
//                    .findViewById(R.id.right_chat_background);
//            mAddBlockLayout.setVisibility(View.GONE);
//
//            mRightChatText = (EmojiconTextView) view.findViewById(R.id.right_chat_text);
//            mTxtDuration = (TextView) view.findViewById(R.id.txt_duration);
//            mTxtSize = (TextView) view.findViewById(R.id.txt_size);
//            mTxtAudioSize = (TextView) view.findViewById(R.id.txt_audio_size);
//            mTxtAudioLength = (TextView) view.findViewById(R.id.txt_audio_length);
//            mTxtFileName = (TextView) view.findViewById(R.id.txt_file_name);
//            mTxtFileSize = (TextView) view.findViewById(R.id.txt_file_size);
//            mRightChatTimeTxt = (TextView) view .findViewById(R.id.right_chat_time_txt);
//
//            mRightImageChatLayout = (FrameLayout) view
//                    .findViewById(R.id.right_image_chat_layout);
//            mRightVideoChatLayout = (FrameLayout) view
//                    .findViewById(R.id.right_video_chat_layout);
//
//            mRightImageChat = (ImageView) view
//                    .findViewById(R.id.right_image_chat);
//            mRightImageUploadImage = (ImageView) view
//                    .findViewById(R.id.right_image_upload_image);
//            mRightVideo = (ImageView) view.findViewById(R.id.right_video);
//            mBtnUploadVideo = (ImageView) view
//                    .findViewById(R.id.btn_upload_video);
//            mBtnVideoPlay = (ImageView) view.findViewById(R.id.btn_video_play);
//            mBtnAudioPlay = (ImageButton) view.findViewById(R.id.btn_audio_play);
//            mBtnAudioUpload = (ImageView) view
//                    .findViewById(R.id.btn_audio_upload);
//            mBtnAudioCancelUpload = (ImageView) view
//                    .findViewById(R.id.btn_audio_cancel_upload);
//            mImgContactAvatar = (ImageView) view
//                    .findViewById(R.id.img_contact_avatar);
//            mBtnUploadFile = (ImageView) view
//                    .findViewById(R.id.btn_upload_file);
//            mBtnViewFile = (ImageView) view.findViewById(R.id.btn_view_file);
//            mRightChatStatusImg = (ImageView) view
//                    .findViewById(R.id.right_chat_status_img);
//            mRightChatTipImg = (ImageView) view
//                    .findViewById(R.id.right_chat_tip_img);
//
//            mRightImageProgressUpload = (ProgressBar) view
//                    .findViewById(R.id.right_image_progress_upload);
//            mProgressVideoUploadImage = (ProgressBar) view
//                    .findViewById(R.id.progress_video_upload_image);
//            mProgressAudio = (ProgressBar) view
//                    .findViewById(R.id.progress_audio);
//            mProgressFile = (ProgressBar) view.findViewById(R.id.progress_file);
//
//            mSeekAudioPlay = (SeekBar) view.findViewById(R.id.seek_audio_play);
//            mBtnViewContact = (Button) view.findViewById(R.id.btn_view_contact);
//            mRightSenderImage = (RounderImageView) view
//                    .findViewById(R.id.right_sender_image);
//
//            // Left msgs....
//            mLeftChatLayout = (LinearLayout) view
//                    .findViewById(R.id.left_chat_layout);
//            mLeftAudioChatLayout = (LinearLayout) view
//                    .findViewById(R.id.left_audio_chat_layout);
//            mLeftContactChatLayout = (LinearLayout) view
//                    .findViewById(R.id.left_contact_chat_layout);
//            mLeftFileChatLayout = (LinearLayout) view
//                    .findViewById(R.id.left_file_chat_layout);
//            mLeftTextLayout = (LinearLayout) view
//                    .findViewById(R.id.left_text_layout);
//            mLeftChatStatusLayout = (LinearLayout) view
//                    .findViewById(R.id.left_chat_status_layout);
//            mLeftBackgroundLayout = (LinearLayout) view
//                    .findViewById(R.id.left_background_layout);
//
//            mLeftChatText = (EmojiconTextView) view.findViewById(R.id.left_chat_text);
//            mLeftChatTextTime = (TextView) view
//                    .findViewById(R.id.left_txt_time);
//            mLeftTxtDuration = (TextView) view
//                    .findViewById(R.id.left_txt_duration);
//            mLeftTxtSize = (TextView) view.findViewById(R.id.left_txt_size);
//            mLeftTxtAudioSize = (TextView) view
//                    .findViewById(R.id.left_txt_audio_size);
//            mLeftTxtAudioLength = (TextView) view
//                    .findViewById(R.id.left_txt_audio_length);
//            mLeftContactUsername = (TextView) view
//                    .findViewById(R.id.left_contact_username);
//            mLeftFileUsername = (TextView) view
//                    .findViewById(R.id.left_file_username);
//            mLeftFileTxtFileName = (TextView) view
//                    .findViewById(R.id.left_file_txt_file_name);
//            mLeftFileTxtFileSize = (TextView) view
//                    .findViewById(R.id.left_file_txt_file_size);
//            mLeftChatUsername = (TextView) view
//                    .findViewById(R.id.left_chat_username);
//
//            mLeftImageChatLayout = (FrameLayout) view
//                    .findViewById(R.id.left_image_chat_layout);
//            mLeftVideoChatLayout = (FrameLayout) view
//                    .findViewById(R.id.left_video_chat_layout);
//
//            mLeftImageChat = (ImageView) view
//                    .findViewById(R.id.left_image_chat);
//            mLeftImageDownload = (ImageView) view
//                    .findViewById(R.id.left_image_upload_image);
//            mLeftVideo = (ImageView) view.findViewById(R.id.left_video);
//            mLeftBtnDownloadVideo = (ImageView) view
//                    .findViewById(R.id.left_btn_upload_video);
//            mLeftBtnVideoPlay = (ImageView) view
//                    .findViewById(R.id.left_btn_video_play);
//            mLeftBtnAudioPlay = (ImageButton) view
//                    .findViewById(R.id.left_btn_audio_play);
//            mLeftAudioDownload = (ImageView) view
//                    .findViewById(R.id.left_audio_upload);
//            mLeftAudioCancelDownload = (ImageView) view
//                    .findViewById(R.id.left_audio_cancel_upload);
//            mLeftContactImgContactAvatar = (ImageView) view
//                    .findViewById(R.id.left_contact_img_contact_avatar);
//            mBtnDownloadFile = (ImageView) view
//                    .findViewById(R.id.btn_download_file);
//            mLeftFileBtnViewFile = (ImageView) view
//                    .findViewById(R.id.left_file_btn_view_file);
//            mLeftChatTip = (ImageView) view
//                    .findViewById(R.id.left_chat_tip);
//
//            mLeftVideoProgressUpload = (ProgressBar) view
//                    .findViewById(R.id.left_progress_upload_image);
//            mLeftProgressDownloadImage = (ProgressBar) view
//                    .findViewById(R.id.left_image_progress_upload);
//            mLeftProgressAudio = (ProgressBar) view
//                    .findViewById(R.id.left_progress_audio);
//            mLeftFileProgressFile = (ProgressBar) view
//                    .findViewById(R.id.left_file_progress_file);
//
//            mLeftSeekAudioPlay = (SeekBar) view
//                    .findViewById(R.id.left_seek_audio_play);
//            mLeftContactBtnViewContact = (Button) view
//                    .findViewById(R.id.left_contact_btn_view_contact);
//            mLeftSenderImage = (RounderImageView) view
//                    .findViewById(R.id.left_sender_image);
//
//            try {
//                Constant.typeFace(context,mTxtNotice);
//                Constant.typeFace(context,mTxtDate);
//                Constant.typeFace(context,mTxtDuration);
//                Constant.typeFace(context,mTxtSize);
//                Constant.typeFace(context,mTxtAudioSize);
//                Constant.typeFace(context,mTxtAudioLength);
//                Constant.typeFace(context,mTxtFileName);
//                Constant.typeFace(context,mTxtFileSize);
//                Constant.typeFace(context,mRightChatTimeTxt);
//                Constant.typeFace(context,mLeftTxtDuration);
//                Constant.typeFace(context,mLeftTxtSize);
//                Constant.typeFace(context,mLeftTxtAudioSize);
//                Constant.typeFace(context,mLeftTxtAudioLength);
//                Constant.typeFace(context,mLeftContactUsername);
//                Constant.typeFace(context,mLeftFileUsername);
//                Constant.typeFace(context,mLeftFileTxtFileName);
//                Constant.typeFace(context,mLeftFileTxtFileSize);
//                Constant.typeFace(context,mLeftChatTextTime);
//                Constant.typeFace(context,mLeftChatUsername);
//                Constant.typeFace(context,mLeftContactBtnViewContact);
//                Constant.typeFace(context,mBtnViewContact);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            mScreenArrangement(this);
//
//        }
//
//        @Override
//        public void onClick(View v) {
//            if (mucTest != null) {
//                    mucTest.onClick(v, getAdapterPosition());
//            }
//
//        }
//
//        @Override
//        public boolean onLongClick(View v) {
//            if (mucTest != null) {
//                    mucTest.onLongClick(v, getAdapterPosition());
//            }
//
//            return true;
//        }
//    }
//
//    // ////////////////////////----Image
//    // Upload-----//////////////////////////////////////////////////////////////////////
//    public class uploa_image extends AsyncTask<String, String, String> {
//
//        MessageGetSet message = new MessageGetSet();
//        String file_upload_res = null;
//        String from, msg_id;
//        String url = null;
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            muc = TempConnectionService.MUC_MANAGER.getMultiUserChat(jid);
//            try {
//
//                muc.join(KachingMeApplication.getUserID()
//                        + KachingMeApplication.getHost());
//                muc.addMessageListener(TempConnectionService.muc_messageListener);
//
//            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//                // TODO: handle exception
//                e.printStackTrace();
//            }
//
//            Log.d("Message key id", "Message key id::" + params[0]);
//            message = dbAdapter.getMessages_by_msg_id(params[0]);
//            msg_id = params[0];
//            dbAdapter.setUpdateMessage_need_push(message.getKey_id(), 2);
//            from = params[1];
//
//            try {
//
//                final Message msg = new Message(jid, Message.Type.groupchat);
//                msg.setStanzaId("" + message.getKey_id());
//                msg.setBody("");
//
//                JivePropertiesManager.addProperty(msg, "media_type",
//                        message.getMedia_wa_type());
//                JivePropertiesManager.addProperty(msg, "mime_type",
//                        message.getMedia_mime_type());
//                JivePropertiesManager.addProperty(msg, "thumb_image",
//                        Base64.encodeToString(message.getRow_data()));
//                JivePropertiesManager.addProperty(msg, "media_duration",
//                        message.getMedia_duration());
//                JivePropertiesManager.addProperty(msg, "media_size",
//                        message.getMedia_size() / 1024);
//                JivePropertiesManager.addProperty(msg, "media_type", "1");
//                JivePropertiesManager.addProperty(msg, "ID", 5);
//
//                DeliveryReceiptRequest.addTo(msg);
//
//                RequestParams request_params = new RequestParams();
//
//                request_params.put("uploaded_file", new File(
//                        Constant.local_image_dir + message.getMedia_name())); // Upload
//                request_params.put("mType", "1");
//                request_params.put("mID", "" + message.getKey_id());
//                request_params.put("jID", "" + KachingMeApplication.getUserID()); // a
//                // File
//
//                AsyncHttpClient client = new AsyncHttpClient();
//                client.setTimeout(60000);
//                if (message.getMedia_url() == null) {
//                    client.post(
//                            MEDIA_UPLOAD_URL,
//                            request_params,
//                            new AsyncHttpResponseHandler(Looper.getMainLooper()) {
//
//                                @Override
//                                public void onFailure(int arg0, Header[] arg1,
//                                                      byte[] arg2, Throwable arg3) {
//                                    // TODO Auto-generated method stub
//                                    file_upload_res = null;
//                                   /* Log.e(TAG,
//                                            "on Failure file upload response::"
//                                                    + arg3.getMessage());*/
//                                    dbAdapter.setUpdateMessage_Media_url(
//                                            msg_id, null);
//                                }
//
//                                @Override
//                                public void onSuccess(int arg0, Header[] arg1,
//                                                      byte[] arg2) {
//                                    String content = new String(arg2);
//                                    try {
//                                        JSONArray jarray = new JSONArray(
//                                                content);
//                                        JSONObject jobject = jarray
//                                                .getJSONObject(0);
//                                        url = jobject.getString("url");
//
//                                        /*Log.d(TAG, "URL::" + url);*/
//
//                                    } catch (JSONException e1) {
//                                        // TODO Auto-generated catch block
//                                        e1.printStackTrace();
//                                    }
//
//                                    long l = dbAdapter
//                                            .setUpdateMessage_Media_url(msg_id,
//                                                    url);
//
//                                    JivePropertiesManager.addProperty(msg,
//                                            "path", url);
//                                    try {
//                                        DeliveryReceiptManager
//                                                .addDeliveryReceiptRequest(msg);
//                                        MessageEventManager
//                                                .addNotificationsRequests(msg,
//                                                        true, true, true, true);
//                                        muc.sendMessage(msg);
//
//                                    } catch (Exception e) {
//                                        // ACRA.getErrorReporter().handleException(e);
//                                        e.printStackTrace();
//                                    }
//
//                                }
//
//                            });
//                } else {
//                    file_upload_res = message.getMedia_url();
//                    JivePropertiesManager.addProperty(msg, "path",
//                            file_upload_res);
//                    try {
//                        MessageEventManager.addNotificationsRequests(msg, true,
//                                true, true, true);
//                        muc.sendMessage(msg);
//
//                    } catch (Exception e) {
//                        // ACRA.getErrorReporter().handleException(e);
//                        e.printStackTrace();
//                    }
//                }
//            } catch (Exception e) {
//                // ACRA.getErrorReporter().handleException(e);
//                e.printStackTrace();
//                // TODO: handle exception
//            }
//
//            return file_upload_res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            super.onPostExecute(result);
//
//			/*
//			 * if(file_upload_res==null) {
//			 * dbAdapter.setUpdateMessage_need_push(message.getKey_id(),0); }
//			 * else { dbAdapter.setUpdateMessage_status(message.getKey_id(),0);
//			 * }
//			 */
//			/*
//			 * Intent login_broadcast = new Intent("chat");
//			 * login_broadcast.putExtra("jid", from);
//			 * context.getApplicationContext().sendBroadcast(login_broadcast);
//			 */
//        }
//    }
//
//    public class uploa_video extends AsyncTask<String, String, String> {
//
//        MessageGetSet message = new MessageGetSet();
//        String file_upload_res = null;
//        String from, msg_id;
//        String url = null;
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            muc = TempConnectionService.MUC_MANAGER.getMultiUserChat(jid);
//            try {
//
//                muc.join(KachingMeApplication.getUserID()
//                        + KachingMeApplication.getHost());
//                muc.addMessageListener(TempConnectionService.muc_messageListener);
//
//            } catch (Exception e) {
//                // ACRA.getErrorReporter().handleException(e);
//                // TODO: handle exception
//                e.printStackTrace();
//            }
//
//            Log.d("Message key id", "Message key id::" + params[0]);
//            message = dbAdapter.getMessages_by_msg_id(params[0]);
//            dbAdapter.setUpdateMessage_need_push(message.getKey_id(), 2);
//            from = params[1];
//            msg_id = params[0];
//
//            try {
//
//                final Message msg = new Message(jid, Message.Type.groupchat);
//                JivePropertiesManager.addProperty(msg, "msg_type", 2);
//                msg.setStanzaId("" + message.getKey_id());
//                msg.setBody("");
//                JivePropertiesManager.addProperty(msg, "media_type",
//                        message.getMedia_wa_type());
//                JivePropertiesManager.addProperty(msg, "mime_type",
//                        message.getMedia_mime_type());
//                JivePropertiesManager.addProperty(msg, "thumb_image",
//                        Base64.encodeToString(message.getRow_data()));
//                JivePropertiesManager.addProperty(msg, "media_duration",
//                        message.getMedia_duration());
//                JivePropertiesManager.addProperty(msg, "media_size",
//                        message.getMedia_size() / (1024 * 1024));
//                JivePropertiesManager.addProperty(msg, "media_type", "2");
//                JivePropertiesManager.addProperty(msg, "ID", 5);
//
//                DeliveryReceiptRequest.addTo(msg);
//
//                RequestParams request_params = new RequestParams();
//
//                request_params.put("uploaded_file", new File(
//                        Constant.local_video_dir + message.getMedia_name()));
//                request_params.put("mType", "2");
//                request_params.put("mID", "" + message.getKey_id());
//                request_params.put("jID", "" + KachingMeApplication.getUserID());
//
//                AsyncHttpClient client = new AsyncHttpClient();
//                // client.setTimeout(60000);
//                if (message.getMedia_url() == null) {
//                    client.post(
//                            MEDIA_UPLOAD_URL,
//                            request_params,
//                            new AsyncHttpResponseHandler(Looper.getMainLooper()) {
//
//
//                                @Override
//                                public void onFailure(int arg0, Header[] arg1,
//                                                      byte[] arg2, Throwable arg3) {
//                                    // TODO Auto-generated method stub
//                                    file_upload_res = null;
//                                    Log.e("Video Upload",
//                                            "on Failure file upload response::"
//                                                    + arg3.getMessage());
//                                    dbAdapter.setUpdateMessage_Media_url(
//                                            msg_id, null);
//                                }
//
//                                @Override
//                                public void onSuccess(int arg0, Header[] arg1,
//                                                      byte[] arg2) {
//                                    // TODO Auto-generated method stub
//                                    String content = new String(arg2);
//                                    try {
//                                        JSONArray jarray = new JSONArray(
//                                                content);
//                                        JSONObject jobject = jarray
//                                                .getJSONObject(0);
//                                        url = jobject.getString("url");
//
//                                        Log.d("Video Upload", "URL::" + url);
//
//                                    } catch (JSONException e1) {
//                                        // ACRA.getErrorReporter().handleException(e1);
//                                        // TODO Auto-generated catch block
//                                        e1.printStackTrace();
//                                    }
//
//                                    long l = dbAdapter
//                                            .setUpdateMessage_Media_url(msg_id,
//                                                    url);
//
//                                    JivePropertiesManager.addProperty(msg,
//                                            "path", url);
//
//                                    try {
//                                        DeliveryReceiptManager
//                                                .addDeliveryReceiptRequest(msg);
//                                        MessageEventManager
//                                                .addNotificationsRequests(msg,
//                                                        true, true, true, true);
//                                        muc.sendMessage(msg);
//
//                                    } catch (Exception e) {
//
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                            });
//                } else {
//                    file_upload_res = message.getMedia_url();
//                    JivePropertiesManager.addProperty(msg, "path",
//                            file_upload_res);
//                    try {
//                        MessageEventManager.addNotificationsRequests(msg, true,
//                                true, true, true);
//                        muc.sendMessage(msg);
//
//                    } catch (Exception e) {
//                        // ACRA.getErrorReporter().handleException(e);
//                        e.printStackTrace();
//                    }
//                }
//            } catch (Exception e) {
//                // ACRA.getErrorReporter().handleException(e);
//                e.printStackTrace();
//                // TODO: handle exception
//            }
//
//            return file_upload_res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            super.onPostExecute(result);
//
//			/*
//			 * Intent login_broadcast = new Intent("chat");
//			 * login_broadcast.putExtra("jid", from);
//			 * context.getApplicationContext().sendBroadcast(login_broadcast);
//			 */
//
//        }
//    }
//
//    public class ClickSpan extends ClickableSpan {
//        private final/* synthetic */
//                String val$clickString;
//        EmojiconTextView mRightChatText;
//
//        ClickSpan(String str, EmojiconTextView mRightChatText) {
//            this.val$clickString = str;
//            this.mRightChatText = mRightChatText;
//        }
//
//        public void onClick(View widget) {
//            boolean bFlag;
//            int i;
//
//            mMessegeList = new ArrayList();
//            mMeaningList = new ArrayList();
//
////            Constant.printMsg("Priya Test Nymn click called"
////                    + MUC_Chat_Adapter.this.Normallist
////                    + "   "
////                    + widget.getTag()
////                    + "    "
////                    + Constant.msg_list_adapter.get(
////                    Integer.valueOf(String.valueOf(widget.getTag())))
////                    .getData());
////            MUC_Chat_Adapter.txt_msg = (EmojiconTextView) widget
////                    .findViewById(R.id.txt_msg);
//
//            myMethod(Constant.msg_list_adapter.get(
//                    Integer.valueOf(String.valueOf(widget.getTag()))).getData());
//            Normallist = "";
//
//            String[] nymsplit = mRightChatText.getText().toString()
//                    .split("\\s+");
//
//            Constant.printMsg("TRTRTRT"
//                    + mMessegeList
//                    + "    "
//                    + mMeaningList
//                    + "    "
//                    + val$clickString.subSequence(1,
//                    val$clickString.length() - 1));
//
//            for (int j = 0; j < nymsplit.length; j++) {
//
//                Constant.printMsg("TRTRTRT1111" + nymsplit[j]);
//
//                if (nymsplit[j].equals(val$clickString.subSequence(1,
//                        val$clickString.length() - 1))) {
//
//                    if (mMessegeList.contains(val$clickString.subSequence(1,
//                            val$clickString.length() - 1))) {
//
//                        for (int k = 0; k < mMessegeList.size(); k++) {
//
//                            if (val$clickString.subSequence(1,
//                                    val$clickString.length() - 1).equals(
//                                    mMessegeList.get(k).toString())) {
//
//                                Normallist += " " + "["
//                                        + mMeaningList.get(k).toString() + "]";
//
//                            }
//
//                        }
//
//                    } else if (mMeaningList.contains(val$clickString
//                            .subSequence(1, val$clickString.length() - 1))) {
//
//                        for (int k = 0; k < mMeaningList.size(); k++) {
//
//                            if (val$clickString.subSequence(1,
//                                    val$clickString.length() - 1).equals(
//                                    mMeaningList.get(k).toString())) {
//
//                                Normallist += " " + "["
//                                        + mMessegeList.get(k).toString() + "]";
//
//                            }
//
//                        }
//
//                    }
//
//                } else {
//
//                    if (mMessegeList.contains(nymsplit[j])) {
//
//                        for (int k = 0; k < mMessegeList.size(); k++) {
//
//                            if (nymsplit[j].equals(mMessegeList.get(k)
//                                    .toString())) {
//
//                                Normallist += " " + "["
//                                        + mMessegeList.get(k).toString() + "]";
//
//                            }
//
//                        }
//
//                    } else if (mMeaningList.contains(nymsplit[j])) {
//
//                        for (int k = 0; k < mMeaningList.size(); k++) {
//
//                            if (nymsplit[j].equals(mMeaningList.get(k)
//                                    .toString())) {
//
//                                Normallist += " " + "["
//                                        + mMeaningList.get(k).toString() + "]";
//
//                            }
//
//                        }
//
//                    } else {
//
//                        Normallist += " " + nymsplit[j];
//
//                    }
//
//                }
//
//            }
//
//            Constant.printMsg("FFFFFFFFFFFFFinal" + Normallist);
//
//            // CopyOfChatAdapter.this.Normallist = CopyOfChatAdapter.txt_msg
//            // .getText().toString();
//            // CopyOfChatAdapter.this.mReplace =
//            // CopyOfChatAdapter.this.Normallist;
//            // String[] words = CopyOfChatAdapter.this.mReplace.split("\\s+");
//            // for (String str : words) {
//            // CopyOfChatAdapter.this.mEachWord = str;
//            // Constant.printMsg("eachh :: >>> "
//            // + CopyOfChatAdapter.this.mEachWord + "   "
//            // + CopyOfChatAdapter.this.mReplace);
//            // bFlag = false;
//            // for (i = 0; i < CopyOfChatAdapter.this.Nemelist.size()
//            // && !CopyOfChatAdapter.this.mEachWord.isEmpty(); i++) {
//            // if (CopyOfChatAdapter.this.mEachWord
//            // .equalsIgnoreCase(CopyOfChatAdapter.this.Nemelist
//            // .get(i).toString())) {
//            // CopyOfChatAdapter.this.mReplace = CopyOfChatAdapter.this.mReplace
//            // .replace(CopyOfChatAdapter.this.mEachWord, "["
//            // + CopyOfChatAdapter.this.mEachWord
//            // + "]");
//            // Constant.printMsg("Priya Test Nymn Replace"
//            // + CopyOfChatAdapter.this.mReplace);
//            // bFlag = true;
//            // } else if (CopyOfChatAdapter.this.mEachWord
//            // .equalsIgnoreCase(CopyOfChatAdapter.this.Meaninglist
//            // .get(i).toString())) {
//            // CopyOfChatAdapter.this.mReplace = CopyOfChatAdapter.this.mReplace
//            // .replace(CopyOfChatAdapter.this.mEachWord, "["
//            // + CopyOfChatAdapter.this.mEachWord
//            // + "]");
//            // Constant.printMsg("Priya Test Nymn Replace1"
//            // + CopyOfChatAdapter.this.mReplace);
//            // bFlag = true;
//            // }
//            // if (bFlag) {
//            // break;
//            // }
//            // }
//            // }
//            // CopyOfChatAdapter.this.Normallist =
//            // CopyOfChatAdapter.this.mReplace;
//            // Constant.printMsg("clicked mvalue::>>>>"
//            // + CopyOfChatAdapter.this.Normallist);
//            // CopyOfChatAdapter.this.mEachWord =
//            // this.val$clickString.replaceAll(
//            // "\\[", ACRAConstant.DEFAULT_STRING_VALUE);
//            // Constant.printMsg("Priya Test Nymn Each"
//            // + CopyOfChatAdapter.this.mEachWord);
//            // CopyOfChatAdapter.this.mEachWord =
//            // CopyOfChatAdapter.this.mEachWord
//            // .replaceAll("\\]", ACRAConstant.DEFAULT_STRING_VALUE);
//            // bFlag = false;
//            // for (i = 0; i < CopyOfChatAdapter.this.Nemelist.size(); i++) {
//            // if (CopyOfChatAdapter.this.mEachWord
//            // .equalsIgnoreCase(CopyOfChatAdapter.this.Nemelist
//            // .get(i).toString())) {
//            // CopyOfChatAdapter.this.mReplace =
//            // CopyOfChatAdapter.this.mEachWord;
//            // CopyOfChatAdapter.this.mEachWord =
//            // CopyOfChatAdapter.this.Meaninglist
//            // .get(i).toString();
//            // CopyOfChatAdapter.this.Normallist =
//            // CopyOfChatAdapter.this.Normallist
//            // .replace(CopyOfChatAdapter.this.mReplace,
//            // CopyOfChatAdapter.this.mEachWord);
//            // Constant.printMsg("Priya Test Nymn if Each"
//            // + CopyOfChatAdapter.this.mEachWord + "  "
//            // + CopyOfChatAdapter.this.mReplace);
//            // bFlag = true;
//            // } else if (CopyOfChatAdapter.this.mEachWord
//            // .equalsIgnoreCase(CopyOfChatAdapter.this.Meaninglist
//            // .get(i).toString())) {
//            // CopyOfChatAdapter.this.mReplace =
//            // CopyOfChatAdapter.this.mEachWord;
//            // CopyOfChatAdapter.this.mEachWord =
//            // CopyOfChatAdapter.this.Nemelist
//            // .get(i).toString();
//            // CopyOfChatAdapter.this.Normallist =
//            // CopyOfChatAdapter.this.Normallist
//            // .replace(CopyOfChatAdapter.this.mReplace,
//            // CopyOfChatAdapter.this.mEachWord);
//            // Constant.printMsg("Priya Test Nymn elseif Each"
//            // + CopyOfChatAdapter.this.mEachWord + "  "
//            // + CopyOfChatAdapter.this.mReplace);
//            // bFlag = true;
//            // }
//            // if (bFlag) {
//            // CopyOfChatAdapter.this.bClick = true;
//            // break;
//            // }
//            // }
//            // Constant.printMsg("Priya Test Nymn normallist click"
//            // + CopyOfChatAdapter.this.Normallist);
//            // CopyOfChatAdapter.txt_msg.setMovementMethod(LinkMovementMethod
//            // .getInstance());
//
//            // Normallist = "[lop1] and [lop2]";
//
//            mRightChatText.setText(
//                    addClickablePart(Normallist, mRightChatText),
//                    TextView.BufferType.SPANNABLE);
//        }
//    }
//
//    // ////////////////////////----Audio
//    // Upload-----//////////////////////////////////////////////////////////////////////
//    public class uploa_audio extends AsyncTask<String, String, String> {
//
//        MessageGetSet message = new MessageGetSet();
//        String file_upload_res = null;
//        String from, url = null, msg_id;
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            muc = TempConnectionService.MUC_MANAGER.getMultiUserChat(jid);
//            try {
//
//                muc.join(KachingMeApplication.getUserID()
//                        + KachingMeApplication.getHost());
//                muc.addMessageListener(TempConnectionService.muc_messageListener);
//
//            } catch (Exception e) {
//                // ACRA.getErrorReporter().handleException(e);
//                // TODO: handle exception
//                e.printStackTrace();
//            }
//
//            Log.d("Message key id", "Message key id::" + params[0]);
//            message = dbAdapter.getMessages_by_msg_id(params[0]);
//            dbAdapter.setUpdateMessage_status(message.getKey_id(), 2);
//            from = params[1];
//            msg_id = params[0];
//
//            dbAdapter.setUpdateMessage_need_push(message.getKey_id(), 2);
//
//            try {
//
//                final Message msg = new Message(jid, Message.Type.groupchat);
//                JivePropertiesManager.addProperty(msg, "msg_type", 3);
//                msg.setStanzaId("" + message.getKey_id());
//                msg.setBody("");
//                JivePropertiesManager.addProperty(msg, "media_type",
//                        message.getMedia_wa_type());
//                JivePropertiesManager.addProperty(msg, "mime_type",
//                        message.getMedia_mime_type());
//                JivePropertiesManager.addProperty(msg, "media_duration",
//                        message.getMedia_duration());
//                JivePropertiesManager.addProperty(msg, "media_size",
//                        message.getMedia_size() / 1024);
//                JivePropertiesManager.addProperty(msg, "media_type", "3");
//                JivePropertiesManager.addProperty(msg, "ID", 5);
//                DeliveryReceiptRequest.addTo(msg);
//
//                RequestParams request_params = new RequestParams();
//                request_params.put("uploaded_file", new File(
//                        Constant.local_audio_dir + message.getMedia_name()));
//                request_params.put("mType", "3");
//                request_params.put("mID", "" + message.getKey_id());
//                request_params.put("jID", "" + KachingMeApplication.getUserID());
//
//                AsyncHttpClient client = new AsyncHttpClient();
//                client.setTimeout(60000);
//                if (message.getMedia_url() == null) {
//                    client.post(
//                            MEDIA_UPLOAD_URL,
//                            request_params,
//                            new AsyncHttpResponseHandler(Looper.getMainLooper()) {
//
//								/*
//								 * @Override public void onFailure(Throwable
//								 * error) { // TODO Auto-generated method stub
//								 * super.onFailure(error); file_upload_res =
//								 * null; Log.e(TAG,
//								 * "on Failure file upload response::" +
//								 * error.getMessage());
//								 * dbAdapter.setUpdateMessage_Media_url( msg_id,
//								 * null); }
//								 */
//
//								/*
//								 * @Override public void onSuccess(String
//								 * content) { // TODO Auto-generated method stub
//								 * dbAdapter
//								 * .setUpdateMessage_status(message.getKey_id(),
//								 * 0); try { JSONArray jarray=new
//								 * JSONArray(content); JSONObject
//								 * jobject=jarray.getJSONObject(0);
//								 * url=jobject.getString("url");
//								 *
//								 * Log.d(TAG,"URL::"+url);
//								 *
//								 * } catch (JSONException e1) { // TODO
//								 * Auto-generated catch block
//								 * e1.printStackTrace(); }
//								 *
//								 *
//								 * long l =
//								 * dbAdapter.setUpdateMessage_Media_url(
//								 * msg_id,url);
//								 *
//								 * JivePropertiesManager.addProperty(msg,"path",url
//								 * ); try { MessageEventManager
//								 * .addNotificationsRequests(msg, true, true,
//								 * true, true); muc.sendMessage(msg);
//								 *
//								 * } catch (Exception e) {
//								 *
//								 * e.printStackTrace(); }
//								 *
//								 * super.onSuccess(content); }
//								 */
//
//                                @Override
//                                public void onFailure(int arg0, Header[] arg1,
//                                                      byte[] arg2, Throwable arg3) {
//                                    // TODO Auto-generated method stub
//                                    file_upload_res = null;
//                                    Log.e("audio",
//                                            "on Failure file upload response::"
//                                                    + arg3.getMessage());
//                                    dbAdapter.setUpdateMessage_status(
//                                            message.getKey_id(), 3);
//                                    dbAdapter.setUpdateMessage_Media_url(
//                                            msg_id, null);
//                                }
//
//                                @Override
//                                public void onSuccess(int arg0, Header[] arg1,
//                                                      byte[] arg2) {
//                                    String content = new String(arg2);
//                                    dbAdapter.setUpdateMessage_status(
//                                            message.getKey_id(), 2);
//                                    try {
//                                        JSONArray jarray = new JSONArray(
//                                                content);
//                                        JSONObject jobject = jarray
//                                                .getJSONObject(0);
//                                        url = jobject.getString("url");
//
//                                        Log.d("audio", "URL::" + url);
//
//                                    } catch (JSONException e1) {
//                                        // TODO Auto-generated catch block
//                                        e1.printStackTrace();
//                                    }
//
//                                    long l = dbAdapter
//                                            .setUpdateMessage_Media_url(msg_id,
//                                                    url);
//
//                                    JivePropertiesManager.addProperty(msg,
//                                            "path", url);
//                                    try {
//                                        DeliveryReceiptManager
//                                                .addDeliveryReceiptRequest(msg);
//                                        MessageEventManager
//                                                .addNotificationsRequests(msg,
//                                                        true, true, true, true);
//                                        muc.sendMessage(msg);
//
//                                    } catch (Exception e) {
//                                        // ACRA.getErrorReporter().handleException(e);
//                                        e.printStackTrace();
//                                    }
//
//                                }
//
//                            });
//                } else {
//                    file_upload_res = message.getMedia_url();
//                    JivePropertiesManager.addProperty(msg, "path",
//                            file_upload_res);
//                    try {
//                        MessageEventManager.addNotificationsRequests(msg, true,
//                                true, true, true);
//                        muc.sendMessage(msg);
//
//                    } catch (Exception e) {
//                        // ACRA.getErrorReporter().handleException(e);
//                        e.printStackTrace();
//                    }
//                }
//            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//                e.printStackTrace();
//                // TODO: handle exception
//            }
//
//            return file_upload_res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            super.onPostExecute(result);
//
//			/*
//			 * if(file_upload_res==null) {
//			 * dbAdapter.setUpdateMessage_need_push(message.getKey_id(),0); }
//			 * else { dbAdapter.setUpdateMessage_status(message.getKey_id(),0);
//			 * }
//			 */
//
//			/*
//			 * Intent login_broadcast = new Intent("chat");
//			 * login_broadcast.putExtra("jid", from);
//			 * context.getApplicationContext().sendBroadcast(login_broadcast);
//			 */
//
//        }
//    }
//
//    public class download_image extends AsyncTask<String, String, String>
//
//    {
//        String msg_id, msg_url, data1, from, file_name;
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            // TODO Auto-generated method stub
//
//            super.onProgressUpdate(values);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            msg_id = params[0];
//            msg_url = params[1];
//            from = params[2];
//            final long time = System.currentTimeMillis();
//            ;
//            file_name = "" + time;
//
//            boolean success = (new File(Constant.local_image_dir)).mkdirs();
//            if (!success) {
//                Log.w("directory not created", "directory not created");
//            }
//
//            Log.d("Media", "Download file path::" + msg_url);
//            AsyncHttpClient client = new AsyncHttpClient();
//            client.setTimeout(60000);
//
//            String[] allowedContentTypes = new String[]{"image/png",
//                    "image/jpeg"};
//            client.get(msg_url,
//                    new AsyncHttpResponseHandler(Looper.getMainLooper()) {
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers,
//                                              byte[] binaryData, Throwable error) {
//                            // TODO Auto-generated method stub
//
//                            new AlertManager()
//                                    .showAlertDialog(
//                                            context,
//                                            context.getResources()
//                                                    .getString(
//                                                            R.string.sorry_image_downloading_fail),
//                                            true);
//                            Intent login_broadcast = new Intent("chat");
//                            login_broadcast.putExtra("jid", from);
//                            context.getApplicationContext().sendBroadcast(
//                                    login_broadcast);
//
//                            // super.onFailure(statusCode, headers, binaryData,
//                            // error);
//                        }
//
//                        /*
//						 * @Override public void onSuccess(byte[] fileData) { //
//						 * Do something with the file data1 =
//						 * String.valueOf(String.format(
//						 * Constant.local_image_dir + "%d.jpg", time));
//						 *
//						 * try { Bitmap myBitmap =
//						 * BitmapFactory.decodeByteArray( fileData, 0,
//						 * fileData.length); FileOutputStream stream = new
//						 * FileOutputStream(data1);
//						 *
//						 * ByteArrayOutputStream outstream = new
//						 * ByteArrayOutputStream();
//						 * myBitmap.compress(Bitmap.CompressFormat.JPEG, 85,
//						 * outstream); byte[] byteArray =
//						 * outstream.toByteArray();
//						 *
//						 * stream.write(byteArray); stream.close();
//						 *
//						 * dbAdapter.setUpdateMessage_MediaName(msg_id,
//						 * file_name + ".jpg");
//						 *
//						 * Intent login_broadcast = new Intent("chat");
//						 * login_broadcast.putExtra("jid", from);
//						 * context.getApplicationContext().sendBroadcast(
//						 * login_broadcast);
//						 *
//						 * } catch (Exception e) { // TODO: handle exception } }
//						 */
//                        @Override
//                        public void onSuccess(int arg0, Header[] arg1,
//                                              byte[] fileData) {
//                            // TODO Auto-generated method stub
//                            data1 = String.valueOf(String.format(
//                                    Constant.local_image_dir + "%d.jpg", time));
//
//                            try {
//                                Bitmap myBitmap = BitmapFactory
//                                        .decodeByteArray(fileData, 0,
//                                                fileData.length);
//                                FileOutputStream stream = new FileOutputStream(
//                                        data1);
//
//                                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
//                                myBitmap.compress(Bitmap.CompressFormat.JPEG,
//                                        85, outstream);
//                                byte[] byteArray = outstream.toByteArray();
//
//                                stream.write(byteArray);
//                                stream.close();
//
//                                dbAdapter.setUpdateMessage_MediaName(msg_id,
//                                        file_name + ".jpg");
//
//                                bites = (long) fileData.length;
//                                updateMediaNetwork_Receive(bites);
//
//                                Intent login_broadcast = new Intent("chat");
//                                login_broadcast.putExtra("jid", from);
//                                context.getApplicationContext().sendBroadcast(
//                                        login_broadcast);
//
//                            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//                                // TODO: handle exception
//                            }
//                        }
//                    });
//
//            try {
//                Log.d("Image Url", "Image Url::" + msg_url);
//                URL url = new URL(msg_url);
//                HttpURLConnection connection = (HttpURLConnection) url
//                        .openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                InputStream input = connection.getInputStream();
//                Bitmap myBitmap = BitmapFactory.decodeStream(input);
//
//                data1 = String.valueOf(String.format(Constant.local_image_dir
//                        + "%d.jpg", time));
//
//                FileOutputStream stream = new FileOutputStream(data1);
//
//                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
//                myBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outstream);
//                byte[] byteArray = outstream.toByteArray();
//
//                stream.write(byteArray);
//                stream.close();
//
//            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            // dbAdapter.setUpdateMessage_MediaName(msg_id,file_name+".jpg");
//
//			/*
//			 * Intent login_broadcast = new Intent("chat");
//			 * login_broadcast.putExtra("jid", from);
//			 * context.getApplicationContext().sendBroadcast(login_broadcast);
//			 */
//
//            super.onPostExecute(result);
//        }
//    }
//
//    // ////////////////////////----Video
//    // Download-----//////////////////////////////////////////////////////////////////////
//    public class download_video extends AsyncTask<String, String, String> {
//        String msg_id, msg_url, data1, from, file_name;
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            // TODO Auto-generated method stub
//            Log.d("Image Downloading", "Downloading in Progress.." + values[0]);
//            super.onProgressUpdate(values);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            msg_id = params[0];
//            msg_url = params[1];
//            from = params[2];
//            final long time = System.currentTimeMillis();
//            ;
//            file_name = "" + time;
//
//            boolean success = (new File(Constant.local_video_dir)).mkdirs();
//            if (!success) {
//                Log.w("directory not created", "directory not created");
//            }
//
//            AsyncHttpClient client = new AsyncHttpClient();
//            client.setTimeout(60000);
//            String[] allowedContentTypes = new String[]{".*"};
//            client.get(msg_url,
//                    new AsyncHttpResponseHandler(Looper.getMainLooper()) {
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers,
//                                              byte[] binaryData, Throwable error) {
//                            // TODO Auto-generated method stub
//
//                            new AlertManager()
//                                    .showAlertDialog(
//                                            context,
//                                            context.getResources()
//                                                    .getString(
//                                                            R.string.sorry_image_downloading_fail),
//                                            true);
//                            Intent login_broadcast = new Intent("chat");
//                            login_broadcast.putExtra("jid", from);
//                            context.getApplicationContext().sendBroadcast(
//                                    login_broadcast);
//
//                            // super.onFailure(statusCode, headers, binaryData,
//                            // error);
//                        }
//
//						/*
//						 * @Override public void onSuccess(byte[] fileData) { //
//						 * Do something with the file data1 =
//						 * String.valueOf(String.format(
//						 * Constant.local_video_dir + "%d.mp4", time));
//						 *
//						 * try { File file = new File(data1); FileOutputStream
//						 * fos = null; try {
//						 *
//						 * fos = new FileOutputStream(file);
//						 *
//						 * // Writes bytes from the specified byte array to //
//						 * this file output stream fos.write(fileData);
//						 *
//						 * } catch (FileNotFoundException e) {
//						 * //Constant.printMsg("File not found" + e); } catch
//						 * (IOException ioe) {
//						 * //Constant.printMsg("Exception while writing file "
//						 * + ioe); } finally { // close the streams using close
//						 * method try { if (fos != null) { fos.close(); } }
//						 * catch (IOException ioe) { System.out
//						 * .println("Error while closing stream: " + ioe); }
//						 *
//						 * }
//						 *
//						 * dbAdapter.setUpdateMessage_MediaName(msg_id,
//						 * file_name + ".mp4");
//						 *
//						 * Intent login_broadcast = new Intent("chat");
//						 * login_broadcast.putExtra("jid", from);
//						 * context.getApplicationContext().sendBroadcast(
//						 * login_broadcast);
//						 *
//						 * } catch (Exception e) { // TODO: handle exception } }
//						 */
//
//                        @Override
//                        public void onSuccess(int arg0, Header[] arg1,
//                                              byte[] fileData) {
//                            // TODO Auto-generated method stub
//
//                            data1 = String.valueOf(String.format(
//                                    Constant.local_video_dir + "%d.mp4", time));
//
//                            try {
//                                File file = new File(data1);
//                                FileOutputStream fos = null;
//                                try {
//
//                                    fos = new FileOutputStream(file);
//
//                                    // Writes bytes from the specified byte
//                                    // array to
//                                    // this file output stream
//                                    fos.write(fileData);
//
//                                    bites = (long) file.length();
//                                    updateMediaNetwork_Receive(bites);
//
//                                } catch (FileNotFoundException e) {
//                                    // Constant.printMsg("File not found" + e);
//                                } catch (IOException ioe) {
//                                    // Constant.printMsg("Exception while writing file "
//                                    // + ioe);
//                                } finally {
//                                    // close the streams using close method
//                                    try {
//                                        if (fos != null) {
//                                            fos.close();
//                                        }
//                                    } catch (IOException ioe) {
//
//                                    }
//
//                                }
//
//                                dbAdapter.setUpdateMessage_MediaName(msg_id,
//                                        file_name + ".mp4");
//
//                                Intent login_broadcast = new Intent("chat");
//                                login_broadcast.putExtra("jid", from);
//                                context.getApplicationContext().sendBroadcast(
//                                        login_broadcast);
//
//                            } catch (Exception e) {
//                                // ACRA.getErrorReporter().handleException(e);
//                                // TODO: handle exception
//                            }
//                        }
//                    });
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            // dbAdapter.setUpdateMessage_MediaName(msg_id,file_name+".mp4");
//
//			/*
//			 * Intent login_broadcast = new Intent("chat");
//			 * login_broadcast.putExtra("jid", from);
//			 * context.getApplicationContext().sendBroadcast(login_broadcast);
//			 */
//
//            super.onPostExecute(result);
//        }
//    }
//
//    // ////////////////////////----Audio
//    // Download-----//////////////////////////////////////////////////////////////////////
//    public class download_Audio extends AsyncTask<String, String, String> {
//        String msg_id, msg_url, data1, from, file_name;
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            // TODO Auto-generated method stub
//            Log.d("Image Downloading", "Downloading in Progress.." + values[0]);
//            super.onProgressUpdate(values);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            msg_id = params[0];
//            msg_url = params[1];
//            from = params[2];
//            final long time = System.currentTimeMillis();
//            ;
//            file_name = "" + time;
//
//            boolean success = (new File(Constant.local_audio_dir)).mkdirs();
//            if (!success) {
//                Log.w("directory not created", "directory not created");
//            }
//
//            AsyncHttpClient client = new AsyncHttpClient();
//            client.setTimeout(60000);
//            String[] allowedContentTypes = new String[]{".*", "audio/AMR"};
//            client.get(msg_url,
//                    new AsyncHttpResponseHandler(Looper.getMainLooper()) {
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers,
//                                              byte[] binaryData, Throwable error) {
//                            // TODO Auto-generated method stub
//
//                            new AlertManager()
//                                    .showAlertDialog(
//                                            context,
//                                            context.getResources()
//                                                    .getString(
//                                                            R.string.sorry_audio_downloading_fail),
//                                            true);
//                            Intent login_broadcast = new Intent("chat");
//                            login_broadcast.putExtra("jid", from);
//                            context.getApplicationContext().sendBroadcast(
//                                    login_broadcast);
//
//                            // super.onFailure(statusCode, headers, binaryData,
//                            // error);
//                        }
//
//
//                        @Override
//                        public void onSuccess(int arg0, Header[] arg1,
//                                              byte[] fileData) {
//                            // TODO Auto-generated method stub
//                            data1 = String.valueOf(String.format(
//                                    Constant.local_audio_dir + "%d.amr", time));
//
//                            try {
//                                File file = new File(data1);
//                                FileOutputStream fos = null;
//                                try {
//
//                                    fos = new FileOutputStream(file);
//
//                                    // Writes bytes from the specified byte
//                                    // array to
//                                    // this file output stream
//                                    fos.write(fileData);
//
//                                    bites = (long) file.length();
//                                    updateMediaNetwork_Receive(bites);
//
//                                } catch (FileNotFoundException e) {
//                                    // Constant.printMsg("File not found" + e);
//                                } catch (IOException ioe) {
//                                    // Constant.printMsg("Exception while writing file "
//                                    // + ioe);
//                                } finally {
//                                    // close the streams using close method
//                                    try {
//                                        if (fos != null) {
//                                            fos.close();
//                                        }
//                                    } catch (IOException ioe) {
//
//                                    }
//
//                                }
//
//                                dbAdapter.setUpdateMessage_MediaName(msg_id,
//                                        file_name + ".amr");
//
//                                Intent login_broadcast = new Intent("chat");
//                                login_broadcast.putExtra("jid", from);
//                                context.getApplicationContext().sendBroadcast(
//                                        login_broadcast);
//
//                            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//                                // TODO: handle exception
//                            }
//                        }
//                    });
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            // dbAdapter.setUpdateMessage_MediaName(msg_id,file_name+".amr");
//			/*
//			 * Intent login_broadcast = new Intent("chat");
//			 * login_broadcast.putExtra("jid", from);
//			 * context.getApplicationContext().sendBroadcast(login_broadcast);
//			 */
//
//            super.onPostExecute(result);
//        }
//    }
//
//
//}
