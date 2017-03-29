package com.wifin.kachingme.chat.muc_chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFile.DownloadProgressListener;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.NynmAdapter;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.OnItemClickListenerInterface;
import com.wifin.kachingme.async_tasks.ConcurrentAsyncTaskExecutor;
import com.wifin.kachingme.chat.chat_common_classes.ContactView_Normal;
import com.wifin.kachingme.chat.chat_common_classes.LocationShare;
import com.wifin.kachingme.chat.chat_common_classes.SendContact;
import com.wifin.kachingme.chat.chat_common_classes.SongList;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.emojicons.emojicon.EmojiconEditText;
import com.wifin.kachingme.emojicons.emojicon.EmojiconFragmentGroup;
import com.wifin.kachingme.emojicons.emojicon.EmojiconGridFragment;
import com.wifin.kachingme.emojicons.emojicon.EmojiconTextView;
import com.wifin.kachingme.emojicons.emojicon.emoji.Emojicon;
import com.wifin.kachingme.fragments.GroupChatList;
import com.wifin.kachingme.fragments.UserChatList;
import com.wifin.kachingme.kaching_feature.dazz.BannerActivityChat;
import com.wifin.kachingme.kaching_feature.dazz.BannerActivityDazzAdapter;
import com.wifin.kachingme.kaching_feature.dazz.BannerActivityLED;
import com.wifin.kachingme.kaching_feature.dazz.BannerActivityZzleAdapter;
import com.wifin.kachingme.kaching_feature.dazz.DazzPlainActivity;
import com.wifin.kachingme.kaching_feature.karaoke.KaraokeListActivity;
import com.wifin.kachingme.kaching_feature.kons.KonsHomeScreen;
import com.wifin.kachingme.kaching_feature.nynms.NynmActivity;
import com.wifin.kachingme.listeners.MUC_SubjectChangeListener;
import com.wifin.kachingme.pojo.ADV_group_GetSet;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.GroupParticipantGetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.pojo.NymsPojo;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.settings.Network_Usage;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.AvatarManager;
import com.wifin.kachingme.util.ChatDictionary;
import com.wifin.kachingme.util.CompressImage;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Emoji;
import com.wifin.kachingme.util.FileDialog;
import com.wifin.kachingme.util.GIFView;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.MyLocation.LocationResult;
import com.wifin.kachingme.util.NetworkSharedPreference;
import com.wifin.kachingme.util.NoUnderlineSpan;
import com.wifin.kachingme.util.NotificationHandler;
import com.wifin.kachingme.util.ProfileRoundImg;
import com.wifin.kachingme.util.RounderImageView;
//import com.wifin.kachingme.util.ScalingUtilities;
import com.wifin.kachingme.util.SelectionMode;
import com.wifin.kachingme.util.SendWeb_Group;
import com.wifin.kachingme.util.TimeUtils;
import com.wifin.kachingme.util.Utils;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.cropimage.Util;

import org.acra.ACRAConstants;
import org.apache.http.Header;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.util.stringencoder.Base64;
import org.jivesoftware.smackx.bookmarks.BookmarkManager;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.packet.ChatStateExtension;
import org.jivesoftware.smackx.iqlast.LastActivityManager;
import org.jivesoftware.smackx.iqlast.packet.LastActivity;
import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.xevent.MessageEventManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MUCTest extends AppCompatActivity implements
        EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconFragmentGroup.OnEmojiconBackspaceClickedListener, OnItemClickListenerInterface {



    public MUCTest mucTest;
    public boolean bClick;
    Bitmap unscaledBitmapRight;
    boolean is_auto_dowload_image = false, is_auto_dowload_video = false,
            is_auto_dowload_audio = false, is_auto_dowload_files = false;
    ArrayList<String> mself_destruct_list = new ArrayList<String>();
    String current_song = "";
    ArrayList listTemp = new ArrayList();
    String mAudioTagValue = "";
    String mValue;
    static Boolean IS_Recorded = true;
    int mPressed;
    ArrayList TempBtn = new ArrayList();
    boolean mFirstRun = true;
    int s = 0;
    String numberContactSend = null;
    int mBtnClick = 0;
    Boolean is_you;
    int k =0;
    LinearLayout mRightChatLayout;
    FrameLayout mRightTipLayout;
    //   Context context;

    String MEDIA_UPLOAD_URL = "";
    int notificatiob_font_size = 12, status_font_siz = 12, msg_font_size = 16;
    String Normallist = "";
    ArrayList Nemelist = new ArrayList();
    ArrayList Meaninglist = new ArrayList();
    SpannableStringBuilder ssb;
    String mReplace, mRemove;
    ArrayList<MediaPlayer> list = new ArrayList<MediaPlayer>();
    public static HashMap<String, AsyncHttpClient> mAsyncUpload_Image = new HashMap<String, AsyncHttpClient>();
    public static HashMap<String,AsyncHttpClient> mAsyncUpload_Audio = new HashMap<String, AsyncHttpClient>();
    public static HashMap<String,AsyncHttpClient> mAsyncUpload_Video = new HashMap<String, AsyncHttpClient>();
    Handler seekHandler = new Handler();
    Runnable run;
    long bites = 0;
    int mTextSizeVariation=0;
    public static String selectedWord_backTemp = "";
    //
    int font_size = 0;
    int idx1;
    int idx2;
    ArrayList mMessegeList = new ArrayList();
    ArrayList<Button> PlayBtnlist = new ArrayList<Button>();
    NetworkSharedPreference mNetworkSharPref;
    // ////////////////////////----Video
    // Upload-----//////////////////////////////////////////////////////////////////////
    ArrayList mMeaningList = new ArrayList();
    Bitmap mSenderPhoto;
    private SparseBooleanArray mSelectedItemsIds;
    private List<String> replacePosition = new ArrayList<String>();
    LinearLayout mContactLayout;
    HashMap<String , Integer> colorMap = new HashMap<String , Integer>();
    AlertDialog.Builder mDeleteAlert;


    //Notification texts
    TextView mNotificationText;

    // Right Textview..
    EmojiconTextView mRightTextview;
    TextView mRightSenderTimeText;
    ImageView mRightTickMark;
    public static TextView mRightDestTime;


    //Right Image
    ImageView mRightImageChat;
    TextView mRightImageTextTime;
    ImageView mRightImageTickMark;
    ImageView mRightImageChatUpload;
    ImageView mRightImageChatCancel;
    ProgressBar mRightImageProgress;

    //Right Video
    ImageView mRightVideoChat;
    TextView mRightVideoTimeText;
    ImageView mRightVideoChatUpload;
    ImageView mRightVideoChatCancel;
    ImageView mRightVideoTickMark;
    TextView mRightVideoSize;
    TextView mRightVideoDuration;
    ImageView mRightVideoButtonPlay;
    ImageView mRightVideoImgOverlay;
    ProgressBar mRightVideoProgress;

    //Right Contact
    ImageView mRightContactAvathor;
    EmojiconTextView mRightContactTextView;
    TextView mRightContactTextTime;
    ImageView mRightContactTickMark;

    // Rigtht Audio
    Button mRightAudioBtnPlay;
    Button mRightAudioBtnCancel;
    Button mRightAudioBtnUpload;
    SeekBar mRightAudioSeekBar;
    ProgressBar mRightAudioUploadProgress;
    TextView mRightAudioSize;
    TextView mRightAudioDuration;
    TextView mRightAudioTextTime;
    ImageView mRightAudioTickMark;


    // Left Textview..
    EmojiconTextView mLeftTextview;
    TextView mLeftSenderTimeText;
    EmojiconTextView mLeftTextProfile;


    //Left Image
    ImageView mLeftImageChat;
    TextView mLeftImageTextTime;
    ImageView mLeftImageChatDownload;
    ProgressBar mLeftImagetProgressBar;


    //Left Video
    ImageView mLeftVideoChat;
    TextView mLeftVideoTimeText;
    ImageView mLeftVideoChatDownload;
    ImageView mLeftVideoButtonPlay;
    ImageView mLeftVideoImgOverlay;
    TextView mLeftVideoSize;
    TextView mLeftVideoDuration;
    ProgressBar mLeftVideoProgress;

    //Left Contact
    ImageView mLeftContactAvathor;
    EmojiconTextView mLeftContactTextView;
    TextView mLeftContactTextTime;


    // Left Audio
    Button mLeftAudioBtnPlay;
    Button mLeftAudioBtnCancel;
    Button mLeftAudioBtnDownload;
    SeekBar mLeftAudioSeekBar;
    ProgressBar mLeftAudioDownloadProgress;
    TextView mLeftAudioSize;
    TextView mLeftAudioDuration;
    TextView mLeftAudioTextTime;
    ImageView mLeftAudioTickMark;

    LinearLayout mDynamicView;
    ScrollView mScrollView;

    String mstatus = "1";
    ArrayList<String> mSeenList = new ArrayList<String>();

    private static final int REQUEST_CODE_OPENER = 88,
            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 99,
            CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 100,
            RESULT_CODE_IMAGE_MULTIPLE = 101, NYNM_REQUEST_CODE = 1;
    public static AbstractXMPPConnection connection = TempConnectionService.connection;
    public static MessageEventManager messageEventManager = TempConnectionService.messageEventManager;
    public static MultiUserChat muc = TempConnectionService.muc;
    public static boolean menuclick = false;
    public static String jid="";
    BroadcastReceiver lastseen_event = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Constant.printMsg("Broadcas Recievwd!!!!" + intent.getAction());
            if (intent.getAction().equals("chat")) {

                Constant.printMsg("MUC Broadcast ..chat..");

                String jid1 = intent.getStringExtra("jid");

                if (jid1 != null) {
                    if (jid1.equals(jid)) {
                        //  updateCursor();
                        if (!isFetchChatReady) {
                            isFetchChatReady = true;
//                            ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
                            new FetchChat().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                        }
                    }

                    //  Update_Advacegroup();
                }
            }else if (intent.getAction().equals("MUC_delivary_notification")) {

                Constant.printMsg("MUC Broadcast MUC_delivary_notification");
                ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());

            }  else if (intent.getAction().equals("start_typing")) {

                Constant.printMsg("MUC Broadcast start_typing "
                        + dbAdapter.getLogin().get(0).getUserName()
                        + KachingMeApplication.getHost());

                String jid2 = intent.getStringExtra("jid");

                if (jid2 != null) {

                    if (jid2.toString().split("@")[0] != null) {
                        // typing or recording
                        String type = null;
                        if (intent.getStringExtra("type").equalsIgnoreCase(
                                Constant.TYPING_STATUS_GROUP)) {
                            type = " typing...";
                        } else {
                            type = " recording...";
                        }

                        try {

                            if (!jid2.equalsIgnoreCase(dbAdapter.getLogin()
                                    .get(0).getUserName()
                                    + KachingMeApplication.getHost())) {

                                ContactsGetSet contact = dbAdapter
                                        .getContact(jid2);
                                Constant.printMsg("MUC Broadcast start_typing"
                                        + contact.getDisplay_name()
                                        + "  nUMBER : "
                                        + contact.getNumber()
                                        + "  jID " + jid2);
                                if (contact.getDisplay_name() != null) {

                                    txt_sub_title.setVisibility(View.VISIBLE);
                                    txt_sub_title.setText(contact
                                            .getDisplay_name() + type);
                                } else if (contact.getNumber() != null) {
                                    txt_sub_title.setVisibility(View.VISIBLE);
                                    txt_sub_title.setText(contact.getNumber()
                                            + type);
                                } else {
                                    txt_sub_title.setVisibility(View.VISIBLE);
                                    txt_sub_title.setText(jid2.toString().split("@")[0]
                                            + type);
                                }

                                pauseSubTitle();

                            }

                        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                            // TODO: handle exception
                        }
                    }
                }

            }else if (intent.getAction().equals("update_tick")) {

                Constant.printMsg("MUC Broadcast ..update tick..");

                String postion = intent.getStringExtra("position");
                String status = intent.getStringExtra("status");

//                mRightTickMark
//                        .setImageResource(R.drawable.receipt_from_target);
//            } else if (msg_list.get(k).getStatus() == -1) {
//                mRightTickMark
//                        .setImageResource(R.drawable.receipt_read);
                try {


                mRightTickMark = (ImageView) findViewById(Integer.valueOf(postion) + 600000);

                if (status.equalsIgnoreCase("delivered")) {
                    mRightTickMark.setImageResource(R.drawable.receipt_from_target);
                } else if (status.equalsIgnoreCase("displayed")) {
                    mRightTickMark.setImageResource(R.drawable.receipt_read);
                } else if (status.equalsIgnoreCase("failed")) {
                    mRightTickMark.setImageResource(R.drawable.message_unsent);
                } else if (status.equalsIgnoreCase("server")) {
                    mRightTickMark.setImageResource(R.drawable.receipt_from_server);
                }
                }catch (Exception e){

                }
            } else if (intent.getAction().equals("update_left")) {


                Constant.printMsg("MUC Broadcast ..update left..");

                try {

//                    k = Integer.valueOf(intent.getStringExtra("position"));

                    intializationElements(2);

                    k = msg_list.size()-1;

                    if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 0) {

                        leftTextChat();
                        setLeftChatText();

                    } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 1) {

                        leftImageChat();
                        setLeftImage();
                    }
                    else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 2) {

                        leftVideoChat();
                        setLeftVideo();

                    } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 5) {

                        leftContactChat();
                        setLeftContact();

                    }else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 3) {

                        //Audio
                        leftAudioChat();
                        setLeftAudio_Old();

                    }  else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 4) {

                        //Location
                        leftImageChat();
                        setLeftLocation();

                    }
                    mRightTipLayout.requestFocus();
                    edt_msg.requestFocus();

                }catch (Exception e){

                }
                k=k+1;

            }else if (intent.getAction().equals("muc_fetch_chat")) {

                Constant.printMsg("MUC Broadcast ..muc_fetch_chat..");

                ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
            }else if (intent.getAction().equals(
                    Constant.BROADCAST_UPDATE_GROUP_ICON)) {

                Constant.printMsg("MUC Broadcast ..update grp icon..");
                /*
                 * try { Bitmap bmp = BitmapFactory
				 * .decodeFile(KachingMeApplication.PROFILE_PIC_DIR +
				 * jid.toString().split("@")[0] + ".png"); Drawable drawable =
				 * new BitmapDrawable(bmp); //
				 * getSupportActionBar().setIcon(drawable);
				 * getSupportActionBar().setIcon(drawable); } catch (Exception
				 * e) { // TODO: handle exception }
				 */
            } else if (intent.getAction().equals("lastseen_broadcast")) {

                Constant.printMsg("MUC Broadcast ..lastseen_broadcast.");


                String name = null;
                String jid_loc = intent.getStringExtra("from");

                String type = intent.getStringExtra("type");

                if (type != null) {
                    // if (type.equalsIgnoreCase(Constant.TYPING_STRING)) {
                    //
                    // if (jid_loc != null && dbAdapter != null) {
                    // CommonMethods
                    // .prtMsg("HHHHHH  msg event listener  MUC broadcast"
                    // + jid_loc + "--" + dbAdapter);
                    // ContactsGetSet cont = dbAdapter.getContact(jid_loc);
                    // if (cont != null)
                    // name = cont.getDisplay_name();
                    //
                    // txt_sub_title.setVisibility(View.VISIBLE);
                    //
                    // if (name != null)
                    // txt_sub_title.setText(name + " typing...");
                    // else {
                    // txt_sub_title.setText(""
                    // + jid.replace(
                    // KachingMeApplication.getjid(), "")
                    // + " typing...");
                    // }
                    //
                    // isTyping = false;
                    //
                    // handlerThread.post(updatTitle);
                    // }
                    // }
                }
            }

        }
    };
    public static Boolean IS_Front = false;
    public static String home_title;
    public static Handler mMyhandler = null;
    public static boolean isTyping = true;
    public static Handler handlerThread = new Handler(Looper.getMainLooper());
    public static Handler handler = new Handler(Looper.getMainLooper());
    public static boolean mIsThreadRunning = false;
    public static boolean isFetchChatReady = false;
    // MUC_Chat_Adapter adapter;
    static DatabaseHelper dbAdapter;
    private Timer timer;
    static LinearLayout ll_chat, mDownArrowLayout;
    static LinearLayout ll_no_longer_participant;
    // MessageEventManager messageEventManager;
    static String TAG = MUCTest.class.getSimpleName();
    public LocationResult locationResult = new LocationResult() {

        @Override
        public void gotLocation(Location location) {
            // TODO Auto-generated method stub

            // Toast.makeText(getApplicationContext(), "Got Location",
            // Toast.LENGTH_LONG).show();

            try {
                double Longitude = location.getLongitude();
                double Latitude = location.getLatitude();
                Log.d(TAG, "Current LON::" + Longitude + " LAT::" + Latitude);

                Editor prefsEditor = KachingMeApplication
                        .getsharedpreferences_Editor();
                prefsEditor.putString(Constant.CURRENT_LOG, Longitude + "");
                prefsEditor.putString(Constant.CURRENT_LAT, Latitude + "");
                prefsEditor.commit();
                // Constant.printMsg("SHARE PREFERENCE ME PUT KAR DIYA.");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    DownloadProgressListener listener = new DownloadProgressListener() {
        @Override
        public void onProgress(long bytesDownloaded, long bytesExpected) {
            // Update progress dialog with the latest progress.
            int progress = (int) (bytesDownloaded * 100 / bytesExpected);
            Log.d(TAG, String.format("Loading progress: %d percent", progress));
            barProgressDialog.setProgress(progress);
        }
    };
    // Runnable AsyncConnection = new Runnable() {
    //
    // @Override
    // public void run() {
    //
    // if (!isTyping) {
    // try {
    //
    // Constant.printMsg("HandlerThread in sleep");
    //
    // Thread.sleep(2000);
    //
    // isTyping = true;
    //
    // runOnUiThread(new Runnable() {
    // public void run() {
    // txt_sub_title.setText("");
    // }
    // });
    //
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    //
    // if (istxtListner) {
    //
    // Constant.printMsg("HandlerThread send presence");
    //
    // presence_typing();
    //
    // }
    //
    // }
    //
    // };
    static Boolean IS_TYPED = true;
    private final Runnable updatTitle = new Runnable() {
        public void run() {
            try {

                Constant.printMsg("HandlerThread");

                connect();

                // handlerThread.postDelayed(updatTitle, 3000);
            } catch (Exception e) {
                // Constant.printMsg("HHHHHH" + e.toString());
            }
        }
    };
    public Resources res;
    public List<GroupParticipantGetSet> mGroupMembersList = new ArrayList<GroupParticipantGetSet>();
    public long editNo = 0;
    //Menu option
    LinearLayout mChatOptionMenuHedderLayout, mChatOptionMenuViewcontactLayout, mChatOptionMenuBlockLayout, mChatOptionMenuCallLayout, mChatOptionMenuLockchatLayout, mChatOptionMenuClearchatLayout;
    ImageView mChatOptionMenuViewcontactImg, mChatOptionMenuBlockImg, mChatOptionMenuCallImg, mChatOptionMenuLockchatImg, mChatOptionMenuClearchatImg;
    TextView mChatOptionMenuViewcontactTxt, mChatOptionMenuBlockTxt, mChatOptionMenuCallTxt, mChatOptionMenuLockchatTxt, mChatOptionMenuClearchatTxt;
    //Attachment Icon menu window
    LinearLayout mchatHeadBackLayout, mChatMenuHedderLayout, mChatMenuGalleryLayout, mChatMenuShootLayout, mChatmenuVideoLayout, mChatMenuLocationLayout, mChatMenuContactLayout, mChatMenuAudioLayout,mSliderMenuLayout;
    ImageView mChatMenuGalleryImg, mChatMenuShootImg, mChatMenuVideoImg, mChatMenuLocationImg, mChatMenuContactImg, mChatMenuAudioImg;
    TextView mChatMenuGalleryTxt, mChatMenuShootTxt, mChatMenuVideoTxt, mChatMenuLocationTxt, mChatMenuContactTxt, mChatMenuAudioTxt;
    // public AbstractXMPPConnection connection;
    public static MUCTest mParentActivity;
    SharedPreferences sharedPrefs;
    String status_lock = "check";
    Dbhelper db;
    // KaChingMeService mBoundService;
    Boolean isBound = false;
    String mParseString = "";
    RounderImageView btn_send;
    ImageView btn_emo;
    EditText edt_msg;
    ListView list_option, listMeaning;
    public static  RecyclerView listview;
    // GridView gridview_emo;
    String status = "", subject = "";
    LinearLayout btn_title_layout;
    Button btn_yes, btn_no;
    ImageButton btn_like, btn_dislike;
    TextView txt_title, txt_sub_title, txt_topic, txt_like, txt_dislike,
            txt_selected, txt_voted;
    NynmAdapter meaningAdapter;
    int nymcount = 0;
    List<NymsPojo> selectedtext = new ArrayList<NymsPojo>();
    Boolean is_status_change = false, is_enter_is_send = false;
    boolean bSelectText;
    Cursor cursor;
    SharedPreferences sharedPrefs1, pref, sp1;
    Boolean create = true;
    LinearLayout ll_phone;
    LinearLayout ll_text_message;
    LinearLayout ll_main;
    MediaRecorder myRecorder;
    Boolean is_started = false, is_text = true;
    public static  ArrayList<MessageGetSet> msg_list = new ArrayList<MessageGetSet>();
    String msg_ids = null, title;
    byte[] avatar = null;
    LinearLayout emoji_frag, zlay;
    long last_msg;
    SharedPreferences sp;
    Editor editor;
    Thread refresh;
    /* Advanced group */
    String group_type = null;
    String group_question = null;
    String group_options = null;
    String group_admin = null;
    int int_group_type;
    ArrayList<ADV_group_GetSet> adv_grp_list = new ArrayList<ADV_group_GetSet>();
    DriveFile file;
    ProgressDialog barProgressDialog;
    //	ActionBar actionBar;
    ImageView img_chat_background1;
    GIFView img_chat_background;
    Button zbtn, zbbtn;
    Animation shake, mMove;
    boolean mAnimation = false;
    View mZzleView;
    ImageButton mDownArrow;
    LinearLayout mTextLay, mEditTextLay;
    boolean mPopup = false;
    int Count = 1;
    int currentRotation = 0;
    ImageView img_chat_avatar;
    Bitmap bmp;
    int selected_self_desc_index = 0;
    int[] self_desc_time = {0, 5, 10, 15};
    String songValue, outputFile;
    ArrayList<String> imagesPathList;
    int i = 0;
    // public boolean istxtListner = false;
    int height = 0, width = 0;
//    MyLocation myLocation = new MyLocation();
    ArrayList<String> meaningList = new ArrayList<String>();
    ArrayList mFinalNyms = new ArrayList();
    ArrayList mFinalNymsMeaning = new ArrayList();
    ArrayList mFinalNymsMeaningLength = new ArrayList();
    String selectedWord_back = "";
    Integer timeInMillisec = 0;
    // Chat Hedder Views
    ImageView mChatHedderBackImg, mChatHedderProfileImg,
            mChatHedderAttachmentImg, mChatHedderMenuImg, mToolTipImg,mChatHedderCopyImg;

    // Runnable temp = new Runnable() {
    //
    // @Override
    // public void run() {
    // // TODO Auto-generated method stub
    // /*
    // * if(create){ create=false;
    // */
    // refresh = new Thread(AsyncConnection, "connection thread");
    // refresh.start();
    // /* } */
    // }
    // };

    // Runnable AsyncConnection = new Runnable() {
    //
    // @Override
    // public void run() {
    //
    // if (!IS_TYPED) {
    // presence_typing();
    // }
    //
    // // handler.postDelayed(AsyncConnection, 0);
    //
    // }
    //
    // };
    TextView mChatHedderUserTxt, mChatHedderUserStatusTxt;
    LinearLayout mChatHedderLayout, mChatHedderTextLayout,mChatHedderCopyLayout, mChatHedderAttachmentLayout, mChatHedderMenuLayout;
    boolean mMenuVisible = false;
    boolean mContactMenuVisible = false;
    // public void presence_typing() {
    //
    // Thread thread = new Thread(new Runnable() {
    //
    // @Override
    // public void run() {
    // // TODO Auto-generated method stub
    //
    // Constant.printMsg(" BBBB INside Presence typing");
    //
    // Presence presence = new Presence(Presence.Type.available);
    // presence.setTo(jid);
    // presence.setStatus(Constant.TYPING_STRING);
    // try {
    // TempConnectionService.connection.sendStanza(presence);
    // IS_TYPED = true;
    //
    // try {
    // Thread.sleep(2000);
    // presence.setStatus(Constant.TYPING_OnLine);
    // TempConnectionService.connection.sendStanza(presence);
    //
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //
    // }
    // });
    //
    // thread.start();
    //
    // // Presence presence = new Presence(
    // // org.jivesoftware.smack.packet.Presence.Type.available);
    // // presence.setStatus(Constant.TYPING_STRING);
    // // try {
    // // TempConnectionService.connection.sendStanza(presence);
    // // } catch (NotConnectedException e) {
    // // // TODO Auto-generated catch block
    // // e.printStackTrace();
    // // }
    //
    // // istxtListner = false;
    // }
    // Chat Footer Views
    LinearLayout mChatFooterLayout, mChatFooterEdittextLayout;
    ImageView mChatFooterSlideMenuImg, mChatFooterEmojiconsImg;
    EmojiconEditText mChatFooterEdittext;
    RounderImageView mChatFooterSendBtn;
    int mTempLayoutHeight = 0;
    LinearLayout mChatMenuLayout;
    LinearLayout mChatOptionMenuLayout;
    boolean isFirstTime = true;
    //    public static  MUCTestAdaptor adaptor;
    private HashMap<String, Integer> emoticons = new HashMap<String, Integer>();
    private ArrayList<String> arrayListSmileys = new ArrayList<String>();
    private ActionMode mActionMode;
    private Uri fileUri;
    private DriveId mSelectedFileDriveId;

    // private ServiceConnection mConnection = new ServiceConnection() {
    // @Override
    // public void onServiceConnected(ComponentName className, IBinder service)
    // {
    //
    // if(Constant.isModeDebug)
    // Constant.printMsg("Aaa  came to onServiceConnected MUC");
    //
    // mBoundService = ((KaChingMeService.LocalBinder) service).getService();
    // connection = mBoundService.getConnection();
    // Constant.printMsg("ComponentName    " + className + "IBinder     "
    // + service + "    ff " + jid + "     "
    // + KachingMeApplication.getjid());
    // mBoundService.ResendMessages();
    // // if(mBoundService != null)
    // // {
    // // if (connection == null || !connection.isAuthenticated()
    // // || !connection.isConnected() )
    // // {
    // // mBoundService.connect();
    // // Constant.printMsg("Aaa  came to onServiceConnected MUC");
    // // }
    // // mBoundService.ResendMessages();
    // // }
    //
    // if (dbAdapter.isGroupMember(jid, KachingMeApplication.getjid())) {
    // Thread th = new Thread() {
    // @Override
    // public void run() {
    //
    // try {
    // Log.d(TAG,
    // "IS Connected::" + connection.isConnected()
    // + " JID::" + jid);
    //
    // muc = mBoundService.getMUC_MANAGER()
    // .getMultiUserChat(jid);
    // DiscussionHistory history = new DiscussionHistory();
    // history.setSince(Utils.getBookmarkDate(sp
    // .getString(Constant.LAST_REFRESH_TIME
    // + "_" + jid,
    // Utils.getBookmarkTime())));
    // muc.join(KachingMeApplication.getUserID()
    // + KachingMeApplication.getHost(), null,
    // history, 30000L);
    // muc.addMessageListener(new MUC_MessageListeners(
    // MUC_Chat.this, mBoundService));
    // muc.addSubjectUpdatedListener(new MUC_SubjectChangeListeners(
    // MUC_Chat.this, mBoundService));
    //
    // } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
    // // TODO: handle exception
    // e.printStackTrace();
    // }
    // }
    // };
    // th.start();
    // } else {
    // ll_chat.setVisibility(View.GONE);
    // ll_no_longer_participant.setVisibility(View.VISIBLE);
    // }
    // new FetchChat().execute();
    //
    //
    //
    // if (msg_ids != null) {
    // String msg_id[] = msg_ids.toString().split(",");
    //
    // if (msg_id.length == 1
    // && dbAdapter.getMessages_by_msg_id(msg_id[0])
    // .getMedia_wa_type().equals("0")) {
    // edt_msg.setText(dbAdapter.getMessages_by_msg_id(msg_id[0])
    // .getData());
    // } else {
    // for (int i = 0; i < msg_id.length; i++) {
    // Log.i("Chat Foerword Message", "Message ID::"
    // + msg_id[i]);
    // MessageGetSet msg = dbAdapter
    // .getMessages_by_msg_id(msg_id[i]);
    // // ////////*Text MEssage*////////////
    // if (msg.getMedia_wa_type().equals("0")) {
    // sendMessage(msg.getData());
    // }
    // // ////////*Images Message*////////////
    // else if (msg.getMedia_wa_type().equals("1")) {
    //
    // sendImage(msg);
    // }
    // // ////////*Videos Message*////////////
    // else if (msg.getMedia_wa_type().equals("2")) {
    // Forword_Video(msg);
    // }
    // // ////////*Audios Message*////////////
    // else if (msg.getMedia_wa_type().equals("3")) {
    // Forword_Audio(msg);
    // }
    // // ////////*Location Message*////////////
    // else if (msg.getMedia_wa_type().equals("4")) {
    // send_Location("" + msg.getLatitude(),
    // "" + msg.getLongitude(), msg.getRow_data());
    // }
    // // ////////*Contacts Message*////////////
    // else if (msg.getMedia_wa_type().equals("5")) {
    // send_Contact(msg.getRow_data(), msg.getData(),
    // msg.getMedia_name());
    // }
    // // ////////*FILE Message*////////////
    // else if (msg.getMedia_wa_type().equals("6")) {
    // FORWORD_FILE(msg);
    // } else if (msg.getMedia_wa_type().equals("12")) {
    //
    // sendImage(msg);
    // }
    //
    // }
    // }
    // }
    // // new MyAsync().execute();
    // }
    //
    // @Override
    // public void onServiceDisconnected(ComponentName className) {
    //
    // if(Constant.isModeDebug)
    // Constant.printMsg("Aaa  came to onServiceDisconnected MUC");
    //
    // mBoundService = null;
    // }
    // };
    private NetworkSharedPreference mNewtSharPref;
    private boolean isLeftPlayAudio = false;
    private boolean is_startrec = false;
    ArrayList<Integer> mOnLongSelectedPostions=new ArrayList<Integer>();
    private boolean mIsLogClick = false;

    public static void checkGroupMember() {
        Constant.printMsg("DDDDDDDDDD"
                + dbAdapter.isGroupMember(jid, KachingMeApplication.getjid())
                + "   " + jid + "   " + KachingMeApplication.getjid());

        if (dbAdapter.isGroupMember(jid, KachingMeApplication.getjid())) {

        } else {
            ll_chat.setVisibility(View.GONE);
            ll_no_longer_participant.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            Constant.printMsg("Aaa on create MUC");

            Constant.mFromGroupAudio = false;
            Constant.mKrokFromGroup = false;
            Constant.zzleFromgroup = false;

            Constant.mZzle = false;
            Constant.mBazzle = false;
            Constant.mKons = false;
            super.onCreate(savedInstanceState);
            setContentView(R.layout.chat_muc);

            mParentActivity = this;

            mIntVariable();
            mScreenArrangement();



            pref = PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext());

            // Shared Preference
            mNewtSharPref = new NetworkSharedPreference(this);

            btn_send = (RounderImageView) findViewById(R.id.sendButton);
            btn_send.setImageDrawable(getResources().getDrawable(
                    R.drawable.ic_action_device_access_mic));

            btn_emo = (ImageView) findViewById(R.id.btn_emo);
            edt_msg = (EditText) findViewById(R.id.edt_messagegrp);
            //	txt_topic = (TextView) findViewById(R.id.txt_question);
            listview = (RecyclerView) findViewById(R.id.convlist);
            img_chat_background1 = (ImageView) findViewById(R.id.img_chat_background1);

            img_chat_background = (GIFView) findViewById(R.id.img_chat_background);
            ll_phone = (LinearLayout) findViewById(R.id.ll_name);
            ll_chat = (LinearLayout) findViewById(R.id.ll_chat);
            ll_no_longer_participant = (LinearLayout) findViewById(R.id.ll_no_group_member);
            ll_text_message = (LinearLayout) findViewById(R.id.ll_text);
            ll_main = (LinearLayout) findViewById(R.id.ll_main);
            listMeaning = (ListView) findViewById(R.id.mnglist);
            emoji_frag = (LinearLayout) findViewById(R.id.emoji_frag);

//		zbtn = (Button) findViewById(R.id.zzle_btn);
//		zlay = (LinearLayout) findViewById(R.id.zzle_layout);
//		zbbtn = (Button) findViewById(R.id.zzle_bannerbtn);
            mDownArrow = (ImageButton) findViewById(R.id.btn_down_arrow_grp);
//		mZzleView = (View) findViewById(R.id.zzle_view_grp);

//            myLocation.getLocation(getApplicationContext(), locationResult);
            Constant.zzleFromgroup = false;
//            boolean r = myLocation.getLocation(getApplicationContext(),
//                    locationResult);
            edt_msg.setBackgroundColor(Color.TRANSPARENT);

            shake = AnimationUtils.loadAnimation(this, R.anim.shakeanim);
            mMove = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.reverse);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Constant.screenHeight = metrics.heightPixels;
            Constant.screenWidth = metrics.widthPixels;
            db = new Dbhelper(getApplicationContext());

            sharedPrefs1 = PreferenceManager.getDefaultSharedPreferences(this);
            sp1 = PreferenceManager.getDefaultSharedPreferences(this);

            KachingMeApplication.SELECTED_TAB = 1;
            dbAdapter = KachingMeApplication.getDatabaseAdapter();

            KachingMeApplication.setUserID(dbAdapter.getLogin().get(0).getUserName());

            sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this);
            is_enter_is_send = sharedPrefs.getBoolean("enter_is_send", false);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            if (KachingMeApplication.getsharedpreferences().contains("wallpaper")) {
                // File f = new File(KachingMeApplication.getsharedpreferences()
                // .getString("wallpaper", null));
                //
                // img_chat_background.setImageURI(Uri.fromFile(f));

                Constant.wallType = sp1.getString("wallpaper_type", "");
                Constant.printMsg("type of wallpaper   " + Constant.wallType);

                if (Constant.wallType.equalsIgnoreCase("file")) {
                    File f = new File(KachingMeApplication.getsharedpreferences()
                            .getString("wallpaper", null));
                    //                img_chat_background1.setVisibility(View.VISIBLE);
                    //                img_chat_background1.setImageURI(Uri.fromFile(f));
                    img_chat_background.setVisibility(View.GONE);

                } else {

                    String wall = KachingMeApplication.getsharedpreferences()
                            .getString("wallpaper", "");
                    // Constant.printMsg("wallll   " + wall);
                    //                img_chat_background1.setVisibility(View.GONE);
                    img_chat_background.setVisibility(View.VISIBLE);
                    img_chat_background = (GIFView) findViewById(R.id.img_chat_background);

                    File f = new File(KachingMeApplication.getsharedpreferences()
                            .getString("wallpaper", null));
                    img_chat_background = (GIFView) findViewById(R.id.img_chat_background);

                    Constant.printMsg("paththththth ::::: >>>>>>> " + f);

                    String pathName = f.getAbsolutePath();
                    Toast.makeText(getApplicationContext(), "" + pathName,
                            Toast.LENGTH_LONG).show();
                    Constant.gifimage = pathName;
                    img_chat_background = (GIFView) findViewById(R.id.img_chat_background);
                }

            }

            res = getResources();
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setDisplayShowHomeEnabled(true);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

                jid = bundle.getString("jid");


                try {
                    MultiUserChatManager multiUserChatManager = MultiUserChatManager
                            .getInstanceFor(TempConnectionService.connection);

                    MultiUserChat multiUserChat = multiUserChatManager
                            .getMultiUserChat(jid);

                    muc = multiUserChat;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // new ServerConnectionAsync(this).execute();

                // Toast.makeText(this, "" + jid, Toast.LENGTH_LONG).show();

                //			getSupportActionBar().setTitle(bundle.getString("name"));
                subject = bundle.getString("name");
                if(subject!=null)
                {
                    if (subject.length() > 15) {
                        home_title = subject.substring(0, 15) + "...";
                    } else {
                        home_title = subject;
                    }
                }

                Log.d(TAG, "Notification 2 Room jid::" + jid + " Name::" + subject);

                // if (bundle.getByteArray("avatar") != null) {
                // avatar = bundle.getByteArray("avatar");
                // Drawable drawable = new BitmapDrawable(
                // BitmapFactory.decodeByteArray(avatar, 0, avatar.length));
                // getSupportActionBar().setIcon(drawable);
                // Constant.printMsg("avatar::::");
                // } else {
                // Constant.printMsg("avatar::::null");
                // }
                SliderTesting.msg_ids = null;
                if (bundle.getString("msg_ids") != null) {
                    Log.i("", "Forworded msges::" + bundle.getString("msg_id"));
                    msg_ids = bundle.getString("msg_ids");


                }


            }

            // last_msg = sharedPrefs.getLong("last_refresh_time_" + jid, 0);

		/*actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);*/

//		LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View v1 = inflator.inflate(R.layout.abs__action_bar_title_item_1, null);

            txt_title = (TextView) findViewById(R.id.chat_hedder_user_txt);
            txt_sub_title = (TextView) findViewById(R.id.chat_hedder_user_status_txt);
            img_chat_avatar = (ImageView) findViewById(R.id.chat_hedder_profile_img);
            btn_title_layout = (LinearLayout) findViewById(R.id.chat_hedder_layout);
            txt_title.setText(home_title);
            txt_sub_title.setVisibility(View.GONE);
            // start
//        new MyAsync1().execute();

            if(Constant.FROM_CHAT_SCREEN.equalsIgnoreCase("notification")){

                if(Constant.mReciverAvathor!=null){

                    ProfileRoundImg  mSenderImage=new ProfileRoundImg(Constant.mReciverAvathor);
                    img_chat_avatar.setImageDrawable(mSenderImage);

                }else{
                    Bitmap  bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2, Util.getBitmapOptions());
                    ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                    img_chat_avatar.setImageDrawable(mSenderImage);
                }


            }else if (Constant.FROM_CHAT_SCREEN.equalsIgnoreCase("group")) {
                System.out.println("GHGHGYHGYGHTY"+GroupChatList.mProfileImagesList+"     "+GroupChatList.mPosition);
                try {
                    if (GroupChatList.mProfileImagesList.size() > 0) {
                        //  mSenderRoundImage.setImageBitmap(UserChatList.mProfileImagesList.get(UserChatList.mPosition));
                        ProfileRoundImg mSenderImage = new ProfileRoundImg(GroupChatList.mProfileImagesList.get(GroupChatList.mPosition));
                        img_chat_avatar.setImageDrawable(mSenderImage);

                    }else{

                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2,Util.getBitmapOptions());
                        ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                        img_chat_avatar.setImageDrawable(mSenderImage);
                    }
                } catch (Exception e) {

                }

            }else if (Constant.FROM_CHAT_SCREEN.equalsIgnoreCase("chat")) {


                try {
                    if (UserChatList.mProfileImagesList.size() > 0) {
                        ProfileRoundImg mSenderImage = new ProfileRoundImg(UserChatList.mProfileImagesList.get(UserChatList.mPosition));
                        img_chat_avatar.setImageDrawable(mSenderImage);
                    }else{
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2);
                        ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                        img_chat_avatar.setImageDrawable(mSenderImage);
                    }
                } catch (Exception e) {

                }

            }




            // end
            if (Connectivity.isConnected(mParentActivity)) {

                // new getKeyword().execute();

            }
            mDynamicView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    downArrowClickAction();
                    topMenuHideFunction();
                    return false;
                }
            });
            mScrollView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                    downArrowClickAction();
                    topMenuHideFunction();
                    return false;
                }
            });

            mDownArrowLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    topMenuHideFunction();
                    downArrowClickAction();
                }
            });

            mDownArrow.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    topMenuHideFunction();
                    downArrowClickAction();
                }
            });

            mChatHedderAttachmentLayout
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            // For deleting the msgs which are selected..
                            if (mIsLogClick) {

                                mDeleteAlert = new AlertDialog.Builder(mParentActivity);
                                mDeleteAlert.setMessage("Are you sure you want to delete ?")
                                        .setCancelable(false);
                                mDeleteAlert.setPositiveButton(getResources().getString(R.string.cancel),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });
                                mDeleteAlert.setNegativeButton(getResources().getString(R.string.Ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                for (int i = (mOnLongSelectedPostions.size() - 1); i >= 0; i--) {
                                                    MessageGetSet selectedItem = msg_list.get(mOnLongSelectedPostions
                                                            .get(i));
                                                    // adapter.remove(selectedItem);
                                                    dbAdapter.setDeleteMessages_by_msgid(""
                                                            + selectedItem.get_id());
                                                }


                                                int msg_id = dbAdapter.getLastMsgid(jid);

                                                if (dbAdapter.isExistinChatList(jid)) {
                                                    dbAdapter.setUpdateChat_lits(jid, msg_id);
                                                } else {
                                                    dbAdapter.setInsertChat_list(jid, msg_id);
                                                }

                                                ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());

                                                //change hedder
                                                mOnLongSelectedPostions.clear();
                                                mIsLogClick = false;
                                                mChatHedderAttachmentImg.setBackgroundResource(R.drawable.clip);
                                                mChatHedderMenuImg.setBackgroundResource(R.drawable.menu_right);

                                                LinearLayout.LayoutParams hedderTextParams = new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                                hedderTextParams.width = width * 50 / 100;
                                                hedderTextParams.leftMargin = width * 3 / 100;
                                                hedderTextParams.gravity = Gravity.CENTER_VERTICAL;
                                                mChatHedderTextLayout.setLayoutParams(hedderTextParams);


                                                LinearLayout.LayoutParams hedderAttachmentParams = new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                                hedderAttachmentParams.width = width * 4 / 100;
                                                hedderAttachmentParams.height = width * 8 / 100;
                                                hedderAttachmentParams.gravity = Gravity.CENTER;
                                                mChatHedderAttachmentImg.setLayoutParams(hedderAttachmentParams);

                                                LinearLayout.LayoutParams hedderMenuParams = new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                                hedderMenuParams.width = (width * 2 / 100) - 2;
                                                hedderMenuParams.height = width * 7 / 100;
                                                hedderMenuParams.gravity = Gravity.CENTER;
                                                mChatHedderMenuImg.setLayoutParams(hedderMenuParams);

                                                mChatHedderCopyLayout.setVisibility(View.GONE);

                                                return;

                                            }
                                        });


                                AlertDialog alert = mDeleteAlert.create();
                                alert.show();

                            }
                            else{

                                if (mMenuVisible == false) {

                                    mChatHedderMenuLayout.setBackgroundColor(Color
                                            .parseColor("#00000000"));
                                    mContactMenuVisible = false;
                                    if (mChatOptionMenuLayout != null) {
                                        mChatOptionMenuLayout.setVisibility(View.GONE);
                                    }
                                    mToolTipImg.setVisibility(View.GONE);

                                    mChatHedderAttachmentLayout
                                            .setBackgroundColor(Color
                                                    .parseColor("#59000000"));
                                    mMenuVisible = true;
                                    mAttachmentIconMenuPopup();
                                    attachmentOptionMenuClickListener();
                                    FrameLayout.LayoutParams hedderToolTipParams = new FrameLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT);
                                    hedderToolTipParams.width = width * 5 / 100;
                                    hedderToolTipParams.height = width * 3 / 100;
                                    hedderToolTipParams.topMargin = height * 8 / 100;
                                    hedderToolTipParams.leftMargin = width * 81 / 100;
                                    mToolTipImg.setLayoutParams(hedderToolTipParams);
                                    mToolTipImg.setVisibility(View.VISIBLE);

                                } else {

                                    mChatHedderAttachmentLayout
                                            .setBackgroundColor(Color
                                                    .parseColor("#00000000"));
                                    mMenuVisible = false;
                                    mAttachmentIconMenuPopup();
                                    attachmentOptionMenuClickListener();
                                    mToolTipImg.setVisibility(View.GONE);

                                }

                            }
                        }
                    });

            mChatHedderMenuLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (mIsLogClick) {
                        String msg_ids = "";
                        int k = 0;

                        if (mOnLongSelectedPostions != null) {
                            for (int i = 0; i < mOnLongSelectedPostions.size(); i++) {

                                if (k == 0) {
                                    msg_ids = ""
                                            + msg_list.get(mOnLongSelectedPostions.get(i))
                                            .get_id();
                                } else {
                                    msg_ids = msg_ids
                                            + ","
                                            + msg_list.get(mOnLongSelectedPostions.get(i))
                                            .get_id();
                                }
                                k++;

                            }
                        }

//                        mIsLogClick = false;
//                        mOnLongSelectedPostions.clear();
//                        mChatHedderAttachmentImg.setBackgroundResource(R.drawable.clip);
//                        mChatHedderMenuImg.setBackgroundResource(R.drawable.menu_right);

                        Intent intent = new Intent(mParentActivity, SliderTesting.class);
                        Log.i("Forword", "Message IDS " + msg_ids);
                        intent.putExtra("msg_ids", msg_ids);

                        startActivityForResult(intent, 11);


                    }
                    else{

                        String query = "select status from " + Dbhelper.TABLE_LOCK
                                + " where jid = '" + jid + "'";
                        lock_status(query);


                        if (mContactMenuVisible == false) {

                            mChatHedderAttachmentLayout.setBackgroundColor(Color
                                    .parseColor("#00000000"));
                            mMenuVisible = false;
                            if (mChatMenuLayout != null) {
                                mChatMenuLayout.setVisibility(View.GONE);
                            }
                            mToolTipImg.setVisibility(View.GONE);

                            mChatHedderMenuLayout.setBackgroundColor(Color
                                    .parseColor("#59000000"));
                            mContactMenuVisible = true;
                            chatOptionMenu();

                            if (status_lock.equalsIgnoreCase("lock")) {
                                mChatOptionMenuLockchatTxt.setText("Unlock Chat");


                            } else {
                                mChatOptionMenuLockchatTxt.setText("Lock Chat");

                            }

                            chatOptionOnClickListeners();
                            FrameLayout.LayoutParams hedderToolTipParams = new FrameLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            hedderToolTipParams.width = width * 5 / 100;
                            hedderToolTipParams.height = width * 3 / 100;
                            hedderToolTipParams.topMargin = height * 8 / 100;
                            hedderToolTipParams.leftMargin = width * 91 / 100;
                            mToolTipImg.setLayoutParams(hedderToolTipParams);
                            mToolTipImg.setVisibility(View.VISIBLE);

                        } else {
                            mChatHedderMenuLayout.setBackgroundColor(Color
                                    .parseColor("#00000000"));
                            mContactMenuVisible = false;
                            chatOptionMenu();

                            if (status_lock.equalsIgnoreCase("lock")) {
                                mChatOptionMenuLockchatTxt.setText("Unlock Chat");


                            } else {
                                mChatOptionMenuLockchatTxt.setText("Lock Chat");

                            }

                            chatOptionOnClickListeners();
                            mToolTipImg.setVisibility(View.GONE);

                        }

                    }
                }
            });

            btn_send.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    
                    Constant.printMsg("sent_msg_count message count::");
                    int sent_msg_count = pref.getInt("sent_msg_count", 0);
                    Constant.printMsg("sent_msg_count msg count from slide show is::"
                            + sent_msg_count);
                    sent_msg_count = sent_msg_count + 1;
                    Constant.printMsg("sent_msg_count msg countafter added is::"
                            + sent_msg_count);
                    update_sent_count(sent_msg_count);
                    // mDownArrow.setImageResource(R.drawable.rt_arrow);
                    if(mPopup){
                        downArrowClickAction();
                    }
                    mParseString = "";
                    if (!edt_msg.getText().toString().trim().equals("")) {

                        nynm_Send_Msg();

                        selectedtext.clear();

                        nymcount = 0;

                        // sendMessage(edt_msg.getText().toString());

                    } else {
                        voice_dialog();

                    }

                    mFinalNyms = new ArrayList();
                    mFinalNymsMeaning = new ArrayList();
                    mFinalNymsMeaningLength = new ArrayList();
                    editNo = 0;
                }
            });

	/*	zbbtn.setOnClickListener(new OnClickListener() {
            @Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				menuclick = false;

				if (!edt_msg.getText().toString().trim().equals("")) {
					menuclick = true;
					Constant.bux = sharedPrefs1.getLong("buxvalue", 0);

					Long buxval = Constant.bux + Constant.zzlepoints;
					Constant.bux = buxval;

					Editor e = sharedPrefs1.edit();
					e.putLong("buxvalue", buxval);
					e.commit();

					sendMessage("<z>" + edt_msg.getText().toString());
				} else {
					// Alert();

				}

			}
		});*/

            btn_emo.setImageResource(R.drawable.emoji_btn_normal);
            btn_emo.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // mDownArrow.setImageResource(R.drawable.rt_arrow);
                    topMenuHideFunction();
                    if(mPopup){
                        downArrowClickAction();
                    }
                    if (emoji_frag.getVisibility() == View.VISIBLE) {
                        emoji_frag.setVisibility(View.GONE);
                        btn_emo.setImageResource(R.drawable.emoji_btn_normal);
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                .toggleSoftInput(InputMethodManager.SHOW_FORCED,
                                        InputMethodManager.HIDE_IMPLICIT_ONLY);
                    } else {
                        emoji_frag.setVisibility(View.VISIBLE);
                        btn_emo.setImageResource(R.drawable.ic_action_hardware_keyboard);
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(edt_msg.getWindowToken(),
                                        0);
                        // btn_emo.set
                    }
                }
            });

            edt_msg.setInputType(InputType.TYPE_NULL);
            edt_msg.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    topMenuHideFunction();
                    // mDownArrow.setImageResource(R.drawable.rt_arrow);
                    if(mPopup){
                        downArrowClickAction();
                    }
                    edt_msg.setInputType(InputType.TYPE_CLASS_TEXT);
                    edt_msg.onTouchEvent(event); // call native handler
                    return true; // consume touch even
                }
            });

            edt_msg.setOnKeyListener(new OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // TODO Auto-generated method stub
                    if ((event.getAction() == KeyEvent.ACTION_DOWN)
                            && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        // Perform action on key press
                        if (is_enter_is_send) {
                            if (edt_msg.getText().length() == 0)
                            {
                                Toast.makeText(getApplicationContext(), "Enter Message to Send", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                sendMessage(edt_msg.getText().toString());
                            }
                        }
                        return true;
                    }

                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_DEL) {
//
//                            if (selectedWord_back != "") {
//
//                                if (mFinalNyms.contains(selectedWord_back)) {
//
//                                    int pos1 = edt_msg.getText().toString()
//                                            .indexOf(selectedWord_back.trim());
//                                    int pos2 = pos1 + selectedWord_back.length();
//                                    Constant.printMsg("Selected word is del :"
//                                            + selectedWord_back + "  " + pos1
//                                            + "  " + pos2);
//                                    edt_msg.getText().delete(pos1, pos2);
//
//                                }
//                                selectedWord_back = "";
//
//                                // String edtText = edt_msg.getText().toString();
//                                // edt_msg.setText(edtText.replace(selectedWord_back,""));
//                            }
                        }
                    }
                    return false;
                }
            });

            edt_msg.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {

                    // if (!istxtListner) {
                    if (s.length() > 0 && IS_TYPED == true) {
                        // istxtListner = true;
                        IS_TYPED = false;

                        // handlerThread.post(updatTitle);
                        // }
                    }

                    String txt = edt_msg.getText().toString();

                    nynmText_Display(txt, s, count);

                    // if (Constant.group_typingstatusListner == false) {
                    // Intent intentService = new Intent(MUC_Chat.this,
                    // IntentTypingService.class);
                    // intentService.putExtra("jid", jid);
                    // startService(intentService);
                    // }

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                    // Constant.printMsg("Aaa  came to beforeTextChanged");

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Constant.printMsg("Aaa  came to afterTextChanged");

                    // istxtListner = false;

                    if (s.length() == 0) {
                        is_text = false;
                        btn_send.setImageDrawable(getResources().getDrawable(
                                R.drawable.ic_action_device_access_mic));

                        selectedtext.clear();

                    } else {
                        is_text = true;

                        btn_send.setImageDrawable(getResources().getDrawable(
                                R.drawable.submit_btn));
                    }

                    int startSelection = edt_msg.getSelectionStart();

                    int length = 0;

                    for (String currentWord : edt_msg.getText().toString()
                            .split(" ")) {
                        Constant.printMsg(currentWord);
                        length = length + currentWord.length() + 1;
                        if (length > startSelection) {
                            selectedWord_back = currentWord;
                            break;
                        }

                    }
                    Constant.printMsg("Selected word is back: "
                            + selectedWord_back + "  ");

                    if(mTextSizeVariation<=edt_msg.getText().toString().length()){

                        selectedWord_backTemp="";

                    }else if(s.length()>0){

                        try {
                            if(selectedWord_backTemp.length()>0){
                                selectedWord_back=selectedWord_backTemp;
                                selectedWord_backTemp="";
                            }

                            if (selectedWord_back != "") {

                                if (mFinalNyms.contains(selectedWord_back)) {

                                    int pos1 = edt_msg.getText().toString()
                                            .indexOf(selectedWord_back.trim());
                                    int pos2 = pos1 + selectedWord_back.length();

                                    edt_msg.getText().delete(pos1, pos2);

                                }
                                selectedWord_back = "";

                                // String edtText = edt_msg.getText().toString();
                                // edt_msg.setText(edtText.replace(selectedWord_back,""));
                            }
                        }catch (Exception e){

                        }

                    }

                    mTextSizeVariation=s.length();

                }
            });

            // listview.setOnItemLongClickListener(new OnItemLongClickListener() {
            //
            // public boolean onItemLongClick(AdapterView<?> parent, View view,
            // int position, long id) {
            // onListItemSelect(position);
            // return true;
            // }
            // });

            // listview.setOnItemClickListener(new OnItemClickListener() {
            //
            // @Override
            // public void onItemClick(AdapterView<?> parent, View view,
            // int position, long id) {
            //
            // Log.d("Chat", "List item click at::" + position);
            // if (mActionMode == null) {
            // MessageGetSet message = msg_list.get(position);
            // if (message.getMedia_wa_type().equals("1")
            // || message.getMedia_wa_type().equals("2")) {
            // try {
            // Log.d(TAG,
            // "Image path::"
            // + Constant.local_image_dir
            // + msg_list.get(position)
            // .getMedia_name());
            // File f = new File(Constant.local_image_dir
            // + msg_list.get(position).getMedia_name());
            // Uri uri = Uri.fromFile(f);
            // Intent intent = new Intent(
            // android.content.Intent.ACTION_VIEW);
            // String mime = "*/*";
            // MimeTypeMap mimeTypeMap = MimeTypeMap
            // .getSingleton();
            // if (mimeTypeMap.hasExtension(mimeTypeMap
            // .getFileExtensionFromUrl(uri.toString())))
            // mime = mimeTypeMap
            // .getMimeTypeFromExtension(mimeTypeMap
            // .getFileExtensionFromUrl(uri
            // .toString()));
            // intent.setDataAndType(uri, mime);
            // startActivity(intent);
            // } catch (Exception e) {
            // // TODO: handle exception
            // }
            // } else if (message.getMedia_wa_type().equals("4")) {
            //
            // String urlAddress = "http://maps.google.com/maps?q="
            // + message.getLatitude() + ","
            // + message.getLongitude() + "("
            // + message.getData() + ")&iwloc=A&hl=es";
            // Intent intent = new Intent(Intent.ACTION_VIEW, Uri
            // .parse(urlAddress));
            //
            // try {
            // startActivity(intent);
            // } catch (ActivityNotFoundException e) {
            // // TODO: handle exception
            // new AlertManager()
            // .showAlertDialog(
            // mParentActivity,
            // getResources()
            // .getString(
            // R.string.no_activity_found_to_open_map_location),
            // true);
            // }
            // }
            // } else
            // // add or remove selection for current list item
            // onListItemSelect(position);
            //
            // }
            // });

            mChatHedderTextLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(mParentActivity, MUC_Info.class);
                    intent.putExtra("jid", jid);
                    intent.putExtra("subject", subject);
                    intent.putExtra("avatar", avatar);
                    startActivity(intent);
                }
            });
            mChatHedderProfileImg.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(mParentActivity, MUC_Info.class);
                    intent.putExtra("jid", jid);
                    intent.putExtra("subject", subject);
                    intent.putExtra("avatar", avatar);
                    startActivity(intent);
                }
            });
            mchatHeadBackLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            mChatHedderBackImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_button_select));
            mChatHedderBackImg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    onBackPressed();
                    /*Constant.FromMUC_Chat = true;
                    Intent intent = new Intent(mParentActivity, SliderTesting.class);
                    startActivity(intent);
                    finish();*/
                }
            });

        /*
         * sp=getSharedPreferences(KachingMeApplication.getPereference_label(),Activity
		 * .MODE_PRIVATE); editor=sp.edit();
		 * group_type=sp.getString(jid+"_group_type","none");
		 * group_question=sp.getString(jid+"_group_question","none");
		 * Log.d(TAG,"group_type::"+group_type); if(group_type.equals("1")) {
		 * ll_chat.setVisibility(View.GONE); }
		 */

            sp = getSharedPreferences(KachingMeApplication.getPereference_label(),
                    Activity.MODE_PRIVATE);
            editor = sp.edit();
            group_type = sp.getString(jid + "_group_type", "none");
            group_question = sp.getString(jid + "_group_question", "none");
            group_admin = sp.getString(jid + "_admin", "none");
            group_options = sp.getString(jid + "_group_question_options", "none");
            //txt_topic.setText(group_question);
            int_group_type = Integer.parseInt(group_type);
            Log.d(TAG, "group_type::" + group_type);
            ll_chat.setVisibility(View.GONE);

		/*
         * adv_grp_list.clear(); adv_grp_list=dbAdapter.getADV_Group(jid);
		 */

            if (group_type.equals("1")
                    && !group_admin.equals(KachingMeApplication.getUserID()
                    + KachingMeApplication.getHost())) {
                ll_chat.setVisibility(View.GONE);
            } else if (group_type.equals("2")) {
                ll_chat.setVisibility(View.GONE);
                View hiddenInfo = null;

                hiddenInfo = getLayoutInflater().inflate(
                        R.layout.inflater_muc_yes_no, ll_main, false);
                btn_yes = (Button) hiddenInfo.findViewById(R.id.btn_yes);
                btn_no = (Button) hiddenInfo.findViewById(R.id.btn_no);

                ll_main.addView(hiddenInfo);

                btn_yes.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dbAdapter.getADV_Group_is_exist(
                                jid,
                                KachingMeApplication.getUserID()
                                        + KachingMeApplication.getHost()) == 0) {
                            sendGroupAnswer("" + 0);
                        } else {
                            // showMessage(getResources().getString(
                            // R.string.already_given_answer));
                        }

                    }
                });

                btn_no.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(MUC_Chat.this,"No pressed",Toast.LENGTH_SHORT).show();

                        if (dbAdapter.getADV_Group_is_exist(
                                jid,
                                KachingMeApplication.getUserID()
                                        + KachingMeApplication.getHost()) == 0) {
                            sendGroupAnswer("" + 1);
                        } else {
                            // showMessage(getResources().getString(
                            // R.string.already_given_answer));
                        }
                    }
                });

            } else if (group_type.equals("3")) {
                ll_chat.setVisibility(View.GONE);
                View hiddenInfo = null;

                hiddenInfo = getLayoutInflater().inflate(
                        R.layout.inflater_muc_like_dislike, ll_main, false);
                btn_like = (ImageButton) hiddenInfo.findViewById(R.id.btn_like);
                btn_dislike = (ImageButton) hiddenInfo
                        .findViewById(R.id.btn_dislike);
                txt_like = (TextView) hiddenInfo.findViewById(R.id.txt_like);
                txt_dislike = (TextView) hiddenInfo.findViewById(R.id.txt_dislike);
                ll_main.addView(hiddenInfo);

                btn_like.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dbAdapter.getADV_Group_is_exist(
                                jid,
                                KachingMeApplication.getUserID()
                                        + KachingMeApplication.getHost()) == 0) {
                            sendGroupAnswer("" + 0);
                        } else {
                            // showMessage(getResources().getString(
                            // R.string.already_given_answer));
                        }

                    }
                });

                btn_dislike.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(MUC_Chat.this,"No pressed",Toast.LENGTH_SHORT).show();

                        if (dbAdapter.getADV_Group_is_exist(
                                jid,
                                KachingMeApplication.getUserID()
                                        + KachingMeApplication.getHost()) == 0) {
                            sendGroupAnswer("" + 1);
                        } else {
                            // showMessage(getResources().getString(
                            // R.string.already_given_answer));
                        }
                    }
                });
            } else if (int_group_type == 4) {
                View hiddenInfo = null;

                hiddenInfo = getLayoutInflater().inflate(
                        R.layout.inflater_custome_option, ll_main, false);
                list_option = (ListView) hiddenInfo.findViewById(R.id.list_option);
                Button btn_vote = (Button) hiddenInfo.findViewById(R.id.btn_send);
                txt_selected = (TextView) hiddenInfo
                        .findViewById(R.id.txt_selected);
                txt_voted = (TextView) hiddenInfo.findViewById(R.id.txt_voted);
                ll_main.addView(hiddenInfo);

                String option_ar[] = group_options.split(",");

                adv_grp_list = dbAdapter.getADV_Group(jid);
                for (int i = 0; i < option_ar.length; i++) {
                    try {
                        int count = dbAdapter.getADV_Group_by_and(jid, "" + i);
                        option_ar[i] = option_ar[i] + "  " + (count * 100)
                                / adv_grp_list.size() + "%";
                    } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                        // TODO: handle exception
                    }
                }

                if (dbAdapter.getADV_Group_ans(jid, KachingMeApplication.getUserID()
                        + KachingMeApplication.getHost()) == -1) {

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_single_choice,
                            option_ar);
                    list_option.setAdapter(adapter);
                    list_option.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    adapter.notifyDataSetChanged();
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1, option_ar);
                    list_option.setAdapter(adapter);
                    list_option.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    adapter.notifyDataSetChanged();
                    txt_selected.setText(res.getString(R.string.you_selected)
                            + group_options.split(",")[dbAdapter.getADV_Group_ans(
                            jid, KachingMeApplication.getUserID()
                                    + KachingMeApplication.getHost()) - 1]);
                }

                txt_voted.setText(adv_grp_list.size() + " Person voted");
                // list_option.setItemChecked((dbAdapter.getADV_Group_ans(jid,
                // KachingMeApplication.getUserID()+KachingMeApplication.getHost())-1),true);

                btn_vote.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Log.d(TAG,
                                "Selcted option::"
                                        + list_option.getCheckedItemPosition());
                        if (dbAdapter.getADV_Group_is_exist(
                                jid,
                                KachingMeApplication.getUserID()
                                        + KachingMeApplication.getHost()) == 0) {

                            sendGroupAnswer(""
                                    + (list_option.getCheckedItemPosition() + 1));
                        } else {
                            // showMessage(getResources().getString(
                            // R.string.already_given_answer));
                        }
                    }
                });

            } else if (int_group_type == 5) {
                View hiddenInfo = null;

                hiddenInfo = getLayoutInflater().inflate(
                        R.layout.inflater_custome_option, ll_main, false);
                list_option = (ListView) hiddenInfo.findViewById(R.id.list_option);
                Button btn_vote = (Button) hiddenInfo.findViewById(R.id.btn_send);
                txt_selected = (TextView) hiddenInfo
                        .findViewById(R.id.txt_selected);
                txt_voted = (TextView) hiddenInfo.findViewById(R.id.txt_voted);
                ll_main.addView(hiddenInfo);

                String option_ar[] = group_options.split(",");
                int option_answer[] = new int[option_ar.length];
                adv_grp_list = dbAdapter.getADV_Group(jid);
                String[] answers = dbAdapter.getADV_Group_answers(jid);
                /*
                 * option_ar[i]=option_ar[i]+"  "+(count * 100) /
                 * adv_grp_list.size()+"%";
                 */
                for (int k = 0; k < answers.length; k++) {
                    Log.d(TAG, "Answers::" + answers[k]);
                    String[] ans = answers[k].split(",");
                    for (int l = 0; l < ans.length; l++) {
                        int i = option_answer[Integer.parseInt(ans[l])];
                        i++;
                        option_answer[Integer.parseInt(ans[l])] = i;

                    }

                }

                for (int i = 0; i < option_ar.length; i++) {
                    try {

                        option_ar[i] = option_ar[i] + "  "
                                + (option_answer[i] * 100) / adv_grp_list.size()
                                + "%";

                    } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                        // TODO: handle exception
                    }
                }

                if (dbAdapter.getADV_Group_ans(jid, KachingMeApplication.getUserID()
                        + KachingMeApplication.getHost()) == -1) {

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_multiple_choice,
                            option_ar);
                    list_option.setAdapter(adapter);
                    list_option.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    adapter.notifyDataSetChanged();
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1, option_ar);
                    list_option.setAdapter(adapter);
                    list_option.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    adapter.notifyDataSetChanged();
                    String selected_ans[] = dbAdapter.getADV_Group_ans_multi(
                            jid,
                            KachingMeApplication.getUserID()
                                    + KachingMeApplication.getHost()).split(",");
                    String selected = "";
                    for (int i = 0; i < selected_ans.length; i++) {
                        if (i == 0)
                            selected = group_options.split(",")[Integer
                                    .parseInt(selected_ans[i])];
                        else
                            selected = selected
                                    + ", "
                                    + group_options.split(",")[Integer
                                    .parseInt(selected_ans[i])];

                    }
                    txt_selected.setText(res.getString(R.string.you_selected) + " "
                            + selected);
                }
                txt_voted.setText(adv_grp_list.size() + " Person voted");

                // list_option.setItemChecked((dbAdapter.getADV_Group_ans(jid,
                // KachingMeApplication.getUserID()+KachingMeApplication.getHost())-1),true);

                btn_vote.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SparseBooleanArray ar = list_option
                                .getCheckedItemPositions();
                        int j = 0;
                        String answer = "";
                        for (int i = 0; i < ar.size(); i++) {

                            if (ar.get(i)) {
                                if (j == 0) {
                                    answer = "" + i;
                                } else {
                                    answer = answer + "," + i;
                                }
                                j++;
                            }
                        }
                        Log.d(TAG, "Selected answers::" + answer);
                        if (answer.toString().length() == 0) {
                            // showMessage(getResources().getString(
                            // R.string.please_select_atleast_one_option));
                        }
                        if (dbAdapter.getADV_Group_is_exist(
                                jid,
                                KachingMeApplication.getUserID()
                                        + KachingMeApplication.getHost()) == 0) {
                            sendGroupAnswer(answer);
                        } else {
                            // showMessage(getResources().getString(
                            // R.string.already_given_answer));
                        }
                    }
                });

            } else {
                ll_chat.setVisibility(View.VISIBLE);
            }

		/*if (int_group_type > 0
				|| (int_group_type == 1 && group_admin.equals(KachingMeApplication
						.getUserID() + KachingMeApplication.getHost()))) {
			txt_topic.setVisibility(View.VISIBLE);
			// listview.setVisibility(View.GONE);
		} else {
			txt_topic.setVisibility(View.GONE);
		}*/

            if (int_group_type > 1) {
                //			listview.setVisibility(View.GONE);
            }


            //Forward messages received

            if (msg_ids != null) {
                String msg_id[] = msg_ids.toString().split(",");


                for (int i = 0; i < msg_id.length; i++) {
                    Log.i("Chat Foerword Message", "Message ID::"
                            + msg_id[i]);

                    try{
                  MessageGetSet msg = dbAdapter
                            .getMessages_by_msg_id(msg_id[i]);
                    // ////////*Text MEssage*////////////

                    if(msg!=null) {
                        if (msg.getMedia_wa_type().equals("0")) {
                            sendMessage(msg.getData());
                        }
                        // ////////*Images Message*////////////
                        else if (msg.getMedia_wa_type().equals("1")) {

                            sendImage(msg);
                        }
                        // ////////*Videos Message*////////////
                        else if (msg.getMedia_wa_type().equals("2")) {
                            Forword_Video(msg);
                        }
                        // ////////*Audios Message*////////////
                        else if (msg.getMedia_wa_type().equals("3")) {
                            Forword_Audio(msg);
                        }
                        // ////////*Location Message*////////////
                        else if (msg.getMedia_wa_type().equals("4")) {
                            send_Location("" + msg.getLatitude(),
                                    "" + msg.getLongitude(), msg.getRow_data());
                        }
                        // ////////*Contacts Message*////////////
                        else if (msg.getMedia_wa_type().equals("5")) {
                            send_Contact(msg.getRow_data(), msg.getData(),
                                    msg.getMedia_name());
                        }
                        // ////////*FILE Message*////////////
                        else if (msg.getMedia_wa_type().equals("6")) {
                            FORWORD_FILE(msg);
                        } else if (msg.getMedia_wa_type().equals("12")) {

                            sendImage(msg);
                        }
                    }
                    }catch (Exception e){

                    }

                }
            }


            Update_Advacegroup();
            NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notifManager.cancelAll();
            NotificationHandler.Instance().resetCounter();

            new FetchChat().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            // handlerThread.post(syncTyping);


            //checkGroupMember();






       /* adaptor = new MUCTestAdaptor(mParentActivity, msg_list, jid);
        Constant.adapter_muc_test = adaptor;
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setStackFromEnd(true);
        listview.setLayoutManager(mLayoutManager);

        if (isFirstTime) {
            isFirstTime = false;
            listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getApplicationContext()).size(4).build());
        }


        Constant.msg_list_adapter = msg_list;
        // cursor.close();
        // Constant.msg_list_adapter = msg_list;

        //Collections.reverse(msg_list);
        listview.setAdapter(adaptor);*/
        } catch (Resources.NotFoundException e) {

        } catch (NumberFormatException e) {

        }

    }

    public void connect() {

        // temp.run();

    }

    /**
     * To display the Nynm words while typing in keyboard
     *
     * @param txt
     */
    public void nynmText_Display(String txt, CharSequence s, int count) {
        try {
            if (txt.length() > 0 && txt.charAt(txt.length() - 1) == ' '
                    && mParentActivity.bSelectText) {
                mParentActivity.meaningList.clear();
                mParentActivity.listMeaning.setVisibility(View.GONE);
                mParentActivity.listview.setVisibility(View.VISIBLE);
                mParentActivity.bSelectText = false;
                return;
            }
            if (txt.length() > 0 && txt.charAt(txt.length() - 1) == ' '
                    && mParentActivity.bSelectText) {
                mParentActivity.meaningList.clear();
                mParentActivity.listMeaning.setVisibility(View.GONE);
                mParentActivity.listview.setVisibility(View.VISIBLE);
                mParentActivity.bSelectText = false;
                return;
            }
            mParentActivity.meaningList.clear();
            mParentActivity.listMeaning.setVisibility(View.GONE);
            mParentActivity.listview.setVisibility(View.VISIBLE);
            mParentActivity.bSelectText = false;
            Constant.printMsg("onclcik" + txt);
            String[] words1 = txt.split("\\s+");

            for (int i = 0; i < words1.length; i += MUCTest.NYNM_REQUEST_CODE) {
                mParentActivity.meaningList.clear();
                for (int j = 0; j < ChatDictionary.mDictionaryList.size(); j += MUCTest.NYNM_REQUEST_CODE) {
                    if (words1[i]
                            .equalsIgnoreCase(ChatDictionary.mDictionaryList
                                    .get(j).toString())&& !(txt.charAt(txt.length()-1)==' ')) {
                        String finalstring = ChatDictionary.mDictionaryMeaningList
                                .get(j).toString();
                        String[] splMeaning = finalstring.split(",");
                        if (splMeaning.length > MUCTest.NYNM_REQUEST_CODE) {
                            for (int m = 0; m < splMeaning.length; m += MUCTest.NYNM_REQUEST_CODE) {
                                mParentActivity.meaningList.add(splMeaning[m]);
                                Constant.printMsg("meaning ::>>> "
                                        + mParentActivity.meaningList);
                            }
                        } else {
                            mParentActivity.meaningList.add(finalstring);
                            Constant.printMsg("meaning ::>>> 1"
                                    + mParentActivity.meaningList);
                        }
                    }
                }
            }
            if (mParentActivity.meaningList.size() > 0) {
                mParentActivity.meaningAdapter = new NynmAdapter(mParentActivity,
                        mParentActivity.meaningList);
                mParentActivity.listMeaning.setAdapter(mParentActivity.meaningAdapter);

            }
            if (count > 0) {

                //                    Constant.adapterTest_cursor.getFilter().filter(s);
                mParentActivity.listMeaning.setVisibility(View.VISIBLE);
                mParentActivity.listview.setVisibility(View.VISIBLE);
                mParentActivity.listMeaning
                        .setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0,
                                                    View arg1, int i, long arg3) {

                                editNo++;

                                String itemValue = (String) mParentActivity.listMeaning
                                        .getItemAtPosition(i);
                                Constant.printMsg("Selected Item "
                                        + itemValue);
                                Constant.mSelectedMeaning = itemValue;

                                String mng = "";
                                String chk = "";
                                String msg = "";
                                for (int j = 0; j < ChatDictionary.mDictionaryMeaningList
                                        .size(); j += MUCTest.NYNM_REQUEST_CODE) {
                                    mng = itemValue.toLowerCase();
                                    chk = ChatDictionary.mDictionaryMeaningList
                                            .get(j).toString()
                                            .toLowerCase();
                                    Constant.printMsg("nymn test ::::\t:: "
                                            + mng + "   " + chk);
                                    if (mng.equalsIgnoreCase(chk)) {
                                        MUCTest access$0 = mParentActivity;
                                        access$0.nymcount += MUCTest.NYNM_REQUEST_CODE;
                                        String pojocount = String
                                                .valueOf(mParentActivity.nymcount);
                                        msg = ChatDictionary.mDictionaryList
                                                .get(j).toString();
                                        Constant.printMsg("nymn test ::::\t::1 "
                                                + msg);
                                        NymsPojo np = new NymsPojo();
                                        np.setMeaning(new StringBuilder(
                                                String.valueOf(mng))
                                                .append(mParentActivity.nymcount)
                                                .toString());
                                        np.setText(msg);
                                        mParentActivity.selectedtext.add(np);
                                        mParentActivity.bSelectText = true;
                                        Constant.printMsg("nymn test ::::\t::2 "
                                                + mParentActivity.selectedtext
                                                .size()
                                                + "    "
                                                + mng + "   " + msg);
                                    }
                                }

                                mFinalNyms.add(msg + editNo);
                                mFinalNymsMeaning.add(mng + editNo);
                                mFinalNymsMeaningLength.add(editNo);

                                mParentActivity.listMeaning
                                        .setVisibility(View.GONE);
                                mParentActivity.listview
                                        .setVisibility(View.VISIBLE);
                                selectedWord_backTemp=msg + editNo;
                                Constant.printMsg("SSSSSSSSSSSSSSSSSSS"
                                        + edt_msg.getText().toString());

                                // Update edit text value....
                                int startSelection = edt_msg
                                        .getSelectionStart();

                                String selectedWord = "";
                                int length = 0;

                                for (String currentWord : edt_msg.getText()
                                        .toString().split(" ")) {
                                    Constant.printMsg(currentWord);
                                    length = length + currentWord.length()
                                            + 1;
                                    if (length > startSelection) {
                                        selectedWord = currentWord;

                                        Spannable spannable = new SpannableString(
                                                String.valueOf(editNo)
                                                        + " ".toString()
                                                        .trim());
                                        spannable.setSpan(
                                                new ForegroundColorSpan(
                                                        Color.parseColor("#00000000")),
                                                0,
                                                ((String.valueOf(editNo) + " ")
                                                        .toString()).trim()
                                                        .length(), 0);
                                        spannable.setSpan(new RelativeSizeSpan(0.0f), 0,
                                                ((String.valueOf(editNo) + " ")
                                                        .toString()).trim()
                                                        .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        edt_msg.setText(edt_msg.getText()
                                                .append(spannable));
                                        edt_msg.setText(edt_msg.getText()
                                                .append(" "));

                                        int pos = edt_msg
                                                .getText()
                                                .toString()
                                                .indexOf(
                                                        selectedWord
                                                                + String.valueOf(editNo));
                                        edt_msg.setSelection(pos
                                                + selectedWord.length()
                                                + String.valueOf(editNo)
                                                .length() + 1);

                                        break;
                                    }

                                }
                                Constant.printMsg("Selected word is: "
                                        + selectedWord + "  ");

                            }

                        });

                return;
            }
            mParentActivity.listMeaning.setVisibility(View.GONE);
            mParentActivity.listview.setVisibility(View.VISIBLE);
        } catch (Exception e) {

        }
//		mParentActivity.listview.setVisibility(View.VISIBLE);

    }

    public void Update_Advacegroup() {
        try {
            adv_grp_list = dbAdapter.getADV_Group(jid);
            if (adv_grp_list.size() > 0) {
                int count_yes = dbAdapter.getADV_Group_by_and(jid, "0");
                // int count_no=dbAdapter.getADV_Group_by_and(jid,"1");
                int yes_per = (count_yes * 100) / adv_grp_list.size();
                if (group_type.equals("2")) {
                    btn_yes.setText(getResources().getString(R.string.yes) + " "
                            + "(" + yes_per + "%)");
                    btn_no.setText(getResources().getString(R.string.No) + " "
                            + "(" + (100 - yes_per) + "%)");
                } else if (group_type.equals("3")) {
                    txt_like.setText(yes_per + "%");
                    txt_dislike.setText((100 - yes_per) + "%");
                } else if (int_group_type == 4) {
                    String option_ar[] = group_options.split(",");
                    adv_grp_list = dbAdapter.getADV_Group(jid);
                    for (int i = 0; i < option_ar.length; i++) {
                        try {
                            int count = dbAdapter.getADV_Group_by_and(jid, "" + i);
                            option_ar[i] = option_ar[i] + "  " + (count * 100)
                                    / adv_grp_list.size() + "%";
                        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                            // TODO: handle exception
                        }
                    }

                    if (dbAdapter.getADV_Group_ans(
                            jid,
                            KachingMeApplication.getUserID()
                                    + KachingMeApplication.getHost()) == -1) {

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                this,
                                android.R.layout.simple_list_item_single_choice,
                                option_ar);
                        list_option.setAdapter(adapter);
                        list_option.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        adapter.notifyDataSetChanged();
                    } else {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                this, android.R.layout.simple_list_item_1,
                                option_ar);
                        list_option.setAdapter(adapter);
                        list_option.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        adapter.notifyDataSetChanged();
                        txt_selected.setText(res.getString(R.string.you_selected)
                                + " "
                                + group_options.split(",")[dbAdapter
                                .getADV_Group_ans(
                                        jid,
                                        dbAdapter.getLogin().get(0).getUserName()
                                                + KachingMeApplication
                                                .getHost()) - 1]);
                    }
                }
            }
        } catch (Resources.NotFoundException e) {

            Constant.printMsg("Group Icon" + e.toString());
        }
    }

    public void sendGroupAnswer(String ans) {

        try {
            Message msg = new Message(jid, Type.groupchat);
            msg.setPacketID("" + new Date().getTime());
            msg.setBody(Utils.EncryptMessage(ans));
            JivePropertiesManager.addProperty(msg, "ID", 6);
            JivePropertiesManager.addProperty(msg, "group_type", group_type);
            try {
                muc.sendMessage(msg);

                ADV_group_GetSet advgroup = new ADV_group_GetSet();
                advgroup.setJid(jid);
                advgroup.setMember_id(KachingMeApplication.getUserID()
                        + KachingMeApplication.getHost());
                advgroup.setAnswer(ans);
                advgroup.setGroup_type(Integer.parseInt(group_type));

                /*
                 * if(dbAdapter.getADV_Group_is_exist(jid,
                 * KachingMeApplication.getUserID()+KachingMeApplication.getHost())>0) {
                 * //dbAdapter.setUpdateAdv_group_and(advgroup); } else {
                 */
                dbAdapter.insertADV_group(advgroup);
                /* } */

            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
                e.printStackTrace();
            }

            Update_Advacegroup();
        } catch (Exception e) {

        }

    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconFragmentGroup.input(edt_msg, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconFragmentGroup.backspace(edt_msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.muc_chat_menu, menu);

		/*
		 * int api= Integer.valueOf(android.os.Build.VERSION.SDK);
		 *
		 * //Log.d(TAG,"ANDROID API::"+api);
		 *
		 * if(api>=18) {
		 * menu.findItem(R.id.menu_muc_menu_opm).setVisible(false);
		 *
		 *
		 * }
		 */

        if (int_group_type > 0)

        {
            menu.findItem(R.id.menu_attachment).setVisible(false);
        }
        if (sp.contains(jid + "_lock")) {
            menu.findItem(R.id.menu_muc_lock_chat)
                    .setTitle(res.getString(R.string.unlock_chat))
                    .setIcon(R.drawable.ic_action_unlock);
        } else {
            menu.findItem(R.id.menu_muc_lock_chat)
                    .setTitle(res.getString(R.string.lock_chat))
                    .setIcon(R.drawable.ic_action_lock);
        }

        return true;
    }

	/*
	 * messageEventManager.addMessageEventNotificationListener(new
	 * MessageEventNotificationListener() { public void
	 * deliveredNotification(String from, String packetID) {
	 * //Constant.printMsg("The message has been delivered (" + from + ", " +
	 * packetID + ")"); }
	 *
	 * public void displayedNotification(String from, String packetID) {
	 * //Constant.printMsg("The message has been displayed (" + from + ", " +
	 * packetID + ")"); }
	 *
	 * public void composingNotification(String from, String packetID) {
	 * //Constant.printMsg("The message's receiver is composing a reply (" +
	 * from + ", " + packetID + ")"); }
	 *
	 * public void offlineNotification(String from, String packetID) {
	 * //Constant.printMsg("The message's receiver is offline (" + from + ", "
	 * + packetID + ")"); }
	 *
	 * public void cancelledNotification(String from, String packetID) {
	 * //System
	 * .out.println("The message's receiver cancelled composing a reply (" +
	 * from + ", " + packetID + ")"); } });
	 */

    @Override
    public void onBackPressed() {
		/*
		 * if(KachingMeApplication.getIS_FROM_NOTIFICATION()) {
		 * KachingMeApplication.setIS_FROM_NOTIFICATION(false); Intent upIntent =
		 * NavUtils.getParentActivityIntent(this);
		 * upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 * TaskStackBuilder.create(this) .addNextIntentWithParentStack(upIntent)
		 * .startActivities();
		 *
		 * }
		 */

        try {
            for(int i=0; i<list.size(); i++)
            {
                MediaPlayer player = list.get(i);
                if(player!=null) {

                    player.stop();
                    player.release();
                }
            }

            seekHandler.removeCallbacks(run);
        } catch (Exception e) {

        }

        if (mIsLogClick) {
            try {
                for (int i = 0; i < mOnLongSelectedPostions.size(); i++) {

                    mRightTipLayout = (FrameLayout) findViewById(Integer.valueOf(mOnLongSelectedPostions.get(i)) + 200000);
                    {

                        mRightTipLayout.setBackgroundColor(Color.parseColor("#00000000"));
                        // mOnLongSelectedPostions.remove(i);
                    }


                }

                mOnLongSelectedPostions.clear();
                mIsLogClick = false;
                mChatHedderAttachmentImg.setBackgroundResource(R.drawable.clip);
                mChatHedderMenuImg.setBackgroundResource(R.drawable.menu_right);

                LinearLayout.LayoutParams hedderTextParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                hedderTextParams.width = width * 50 / 100;
                hedderTextParams.leftMargin = width * 3 / 100;
                hedderTextParams.gravity = Gravity.CENTER_VERTICAL;
                mChatHedderTextLayout.setLayoutParams(hedderTextParams);


                LinearLayout.LayoutParams hedderAttachmentParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                hedderAttachmentParams.width = width * 4 / 100;
                hedderAttachmentParams.height = width * 8 / 100;
                hedderAttachmentParams.gravity = Gravity.CENTER;
                mChatHedderAttachmentImg.setLayoutParams(hedderAttachmentParams);

                LinearLayout.LayoutParams hedderMenuParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                hedderMenuParams.width = (width * 2 / 100) - 2;
                hedderMenuParams.height = width * 7 / 100;
                hedderMenuParams.gravity = Gravity.CENTER;
                mChatHedderMenuImg.setLayoutParams(hedderMenuParams);

                mChatHedderCopyLayout.setVisibility(View.GONE);
            } catch (Exception e) {

            }

        } else {

            try {
            unregisterReceiver(lastseen_event);
            handler.removeCallbacks(AsyncConnection);

            if (cursor != null) {
                  cursor.close();
                }

            if (emoji_frag.getVisibility() == View.VISIBLE) {
                emoji_frag.setVisibility(View.GONE);
                btn_emo.setImageResource(R.drawable.emoji_btn_normal);
            } else if (KachingMeApplication.getIS_FROM_NOTIFICATION()) {
                KachingMeApplication.setIS_FROM_NOTIFICATION(false);
                // Intent upIntent = NavUtils.getParentActivityIntent(this);
                //
                // TaskStackBuilder.create(this)
                // .addNextIntentWithParentStack(upIntent).startActivities();
                Constant.FromMUC_Chat = true;
//                Intent intent = new Intent(mParentActivity, SliderTesting.class);
//                startActivity(intent);
//                finish();
                // KachingMeApplication.setIS_FROM_NOTIFICATION(false);
                // Intent upIntent = NavUtils.getParentActivityIntent(this);
                // // upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // TaskStackBuilder.create(this)
                // .addNextIntentWithParentStack(upIntent).startActivities();
                //
                // Intent intent = new Intent(MUC_Chat.this, SliderTesting.class);
                // startActivity(intent);
                // finish();
            } else {
                // super.onBackPressed();

                if (Constant.fromChat == true) {
//                    Intent intent = new Intent(mParentActivity, SliderTesting.class);
//                    startActivity(intent);
//                    finish();
                } else {
                    Constant.FromMUC_Chat = true;
//                    Intent intent = new Intent(mParentActivity, SliderTesting.class);
//                    startActivity(intent);
//                    finish();
                }

            }

            }catch (Exception e){
                super.onBackPressed();
            }
            if (Constant.FROM_CHAT_SCREEN.equalsIgnoreCase("notification")) {
                Intent intent = new Intent(mParentActivity, SliderTesting.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("SliderIntent", "SliderTesting");
                startActivity(intent);
                finish();
            }
            super.onBackPressed();

        }

    }

    @Override
         public boolean onOptionsItemSelected(MenuItem item) {

             Intent intent;
             switch (item.getItemId()) {
                 case android.R.id.home:

                     onBackPressed();
                     return true;

                 case R.id.menu_muc_menu:
                     intent = new Intent(this, MUC_Info.class);
                     intent.putExtra("jid", jid);
                     intent.putExtra("subject", subject);
                     intent.putExtra("avatar", avatar);
                     startActivity(intent);
                     break;

             /*
              * case R.id.menu_muc_menu_opm: intent=new Intent(this,MUC_Info.class);
              * intent.putExtra("jid",jid); intent.putExtra("subject",subject);
              * startActivity(intent); break;
              */

             /*
              * case R.id.menu_image: image_picker(11); break;
              *
              * case R.id.menu_videos: image_picker(22); break;
              */

                 case R.id.menu_capture:

                     selectImage();

                     break;
                 case R.id.audio_file:

                     // Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                     // intent2.setType("audio/*");
                     // startActivityForResult(intent2, RESULT_CODE_AUDIO);
                     Constant.mFromGroupAudio = true;
                     startActivity(new Intent(this, SongList.class));

                     break;
                 case R.id.clear_chat:
                     AlertDialog.Builder b = new AlertDialog.Builder(mParentActivity);
                     b.setMessage("Are you sure you want to clear this chat ?")
                             .setCancelable(false);
                     b.setPositiveButton(getResources().getString(R.string.cancel),
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                     dialog.cancel();

                                 }
                             });
                     b.setNegativeButton(getResources().getString(R.string.Ok),
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                     dialog.cancel();

                                     dbAdapter.setGroupDeleteMessages(jid);
                                     int msg_id = dbAdapter.getLastMsgid(jid);
                                     dbAdapter.setUpdateChat_lits(jid, msg_id);
     //                                clearGroupHistory(jid);
                                     ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
                                 }
                             });

                     AlertDialog alert = b.create();
                     alert.show();

                     break;

                 case R.id.menu_image:
                     // image_picker(1);
                     // create Intent to take a picture and return control to the calling
                     // application
                     intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                     fileUri = Utils.getOutputMediaFileUri(1);
                     // create a file to save the image
                     intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image
                     // file name

                     // start the image capture Intent
                     startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

                     break;
                 case R.id.menu_videos:
                     // image_picker(2);
                     intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                     fileUri = Utils.getOutputMediaFileUri(2); // create a file to save
                     // the video
                     intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image
                     // file name
                     intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.8); // set the
                     // video
                     // image
                     // quality
                     // to high
                     intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 26214400);
                     // start the Video Capture Intent
                     startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

                     break;
                 case R.id.menu_location:
                     intent = new Intent(this, LocationShare.class);
                     startActivityForResult(intent, 44);
                     break;

                 case R.id.menu_contact:
                     intent = new Intent(Intent.ACTION_PICK,
                             ContactsContract.Contacts.CONTENT_URI);
                     startActivityForResult(intent, 55);
                     break;

                 case R.id.menu_file:
                     intent = new Intent(getBaseContext(), FileDialog.class);
                     intent.putExtra(FileDialog.START_PATH, "/mnt");
                     // can user select directories or not
                     intent.putExtra(FileDialog.CAN_SELECT_DIR, false);
                     intent.putExtra(FileDialog.SELECTION_MODE, SelectionMode.MODE_OPEN);
                     // alternatively you can set file filter
                     intent.putExtra(FileDialog.FORMAT_FILTER, new String[]{"ade",
                             "adp", "bat", "chm", "cmd", "com", "cpl", "exe", "hta",
                             "ins", "isp", "jse", "lib", "lnk", "mde", "msc", "msp",
                             "mst", "pif", "scr", "sct", "shb", "sys", "vb", "vbe",
                             "vbs", "vxd", "wsc", "wsf", "wsh"});
                     startActivityForResult(intent, 77);
                     break;
                 // case R.id.menu_google_drive_file:
                 // openGoogleDriveFiles();
                 // break;
             /*
              * case R.id.menu_karaoke: Intent intentka = new Intent(MUC_Chat.this,
              * KaraokeMainGroup.class); startActivity(intentka); break;
              */
                 case R.id.menu_muc_lock_chat:
                     Log.d(TAG, "Menu Title::" + item.getTitle() + " Lock resource::"
                             + res.getString(R.string.lock_chat).toString());
                     if (item.getTitle().toString() == res.getString(R.string.lock_chat)
                             .toString()) {
                         lock_input(txt_title.getText().toString(), jid, true);
                     } else {
                         lock_input(txt_title.getText().toString(), jid, false);
                     }
                     break;
                 default:
                     break;
                 // case R.id.menu_zzle:
                 // menuclick = false;
                 //
                 // if (!edt_msg.getText().toString().trim().equals("")) {
                 // menuclick = true;
                 // Constant.bux = sharedPrefs.getLong("buxvalue", 0);
                 //
                 // Long buxval = Constant.bux + Constant.zzlepoints;
                 // Constant.bux = buxval;
                 //
                 // Editor e = sharedPrefs.edit();
                 // e.putLong("buxvalue", buxval);
                 // e.commit();
                 //
                 // sendMessage("<z>" + edt_msg.getText().toString());
                 // } else {
                 // Alert();
                 //
                 // }

                 // break;

             }

             return super.onOptionsItemSelected(item);
         }

    /**
     * For sending the messages
     */
    public void mSendMsg() {
    }

    /**
     * Used to send Nynm format string in btn_send on click ..
     */
    public void nynm_Send_Msg() {

        String mFInalTxt = "";

        Constant.printMsg("YYYYYYYYYYYYY" + mFinalNyms
                + mFinalNymsMeaningLength + mFinalNymsMeaning);

        if (mFinalNyms.size() > 0) {

            String text = edt_msg.getText().toString();
            String[] splited = text.split("\\s+");

            for (int i = 0; i < splited.length; i++) {

                Constant.printMsg("QQQQQQQ" + splited[i]);

                if (containsCaseInsensitive(splited[i],mFinalNyms)) {

                    Constant.printMsg("QQQQQQQ1111" + splited[i]);

                    for (int j = 0; j < mFinalNyms.size(); j++) {

                        if (mFinalNyms.get(j).toString()
                                .equalsIgnoreCase(splited[i])) {

                            System.out
                                    .println("YYYYYYYYYYYYY"
                                            + mFinalNyms
                                            + mFinalNymsMeaningLength
                                            + "   "
                                            + mFinalNymsMeaningLength.get(j)
                                            .toString().length()
                                            + "   "
                                            + (mFinalNyms.get(j).toString()
                                            .length() - mFinalNymsMeaningLength
                                            .get(j).toString().length())
                                            + "   "
                                            + mFinalNyms.get(j).toString()
                                            .charAt(0));

                            mFInalTxt += " "
                                    + "<n>"
                                    + mFinalNyms
                                    .get(j)
                                    .toString()
                                    .substring(
                                            0,
                                            (mFinalNyms.get(j)
                                                    .toString()
                                                    .length()
                                                    - mFinalNymsMeaningLength
                                                    .get(j)
                                                    .toString()
                                                    .length() + 1))
                                    + "</n>"
                                    + "<m>"
                                    + mFinalNymsMeaning
                                    .get(j)
                                    .toString()
                                    .substring(
                                            0,
                                            (mFinalNymsMeaning.get(j)
                                                    .toString()
                                                    .length()
                                                    - mFinalNymsMeaningLength
                                                    .get(j)
                                                    .toString()
                                                    .length() + 1))
                                    + "</m>";

                        }

                    }

                } else {

                    Constant.printMsg("QQQQQQQ2222" + splited[i]);

                    mFInalTxt += " " + splited[i];

                }

            }

            Constant.printMsg("GGGGGGGGGGGGGGG" + mFInalTxt);
            Constant.bux = sharedPrefs.getLong("buxvalue", 0);

            Long buxval1 = Constant.bux + Constant.nynMpoint;
            Constant.bux = buxval1;

            Editor edit = sharedPrefs.edit();
            edit.putLong("buxvalue", buxval1);
            edit.commit();

            int point1 = sharedPrefs.getInt("nympoint", 0);

            Constant.totalchat = point1;

            Constant.totalchat = Count + Constant.totalchat;

            Editor e11 = sharedPrefs.edit();
            e11.putInt("nympoint", Constant.totalchat);
            e11.commit();
            sendMessage("<-" + mFInalTxt);

        } else {

            sendMessage(edt_msg.getText().toString());
        }

        // if (selectedtext.size() > 0) {
        //
        // String text = edt_msg.getText().toString();
        // String[] splited = text.split("\\s+");
        // int point1 = pref.getInt("nympoint", 0);
        //
        // Constant.totalnym = point1;
        //
        // Constant.totalnym = Count + Constant.totalnym;
        //
        // Editor e2 = pref.edit();
        // e2.putInt("nympoint", Constant.totalnym);
        // e2.commit();
        //
        // Constant.bux = pref.getLong("buxvalue", 0);
        //
        // Long buxval1 = Constant.bux + Constant.nympoints;
        // Constant.bux = buxval1;
        //
        // Editor e3 = pref.edit();
        // e3.putLong("buxvalue", buxval1);
        // e3.commit();
        //
        // String[] nymsplit = text.split("\\s+");
        //
        // int loopcount = 0;
        //
        // String chknymtext = "", chknymmng = "";
        //
        // for (int i = 0; i < nymsplit.length; i++) {
        //
        // boolean duplicatecheck = true;
        //
        // // String wormsg =
        // // words[i].toLowerCase().trim(); // Coommneted
        // // as nymsplit s created for same
        // String wormsg = nymsplit[i].toLowerCase().trim();
        //
        // for (int j = 0; j < selectedtext.size(); j++) {
        //
        // chknymtext = selectedtext.get(j).getText().toString()
        // .toLowerCase().trim();
        //
        // chknymmng = selectedtext.get(j).getMeaning().toString()
        // .toLowerCase().trim();
        //
        // Constant.printMsg("for" + "   " + chknymmng + "    "
        // + chknymtext + "    " + wormsg);
        //
        // if (wormsg.equals(chknymtext)) {
        //
        // loopcount++;
        //
        // String countno = String.valueOf(loopcount);
        // Constant.printMsg("for otuside" + "   " + chknymmng
        // + "    " + chknymtext + "    " + countno);
        //
        // if (chknymmng.contains(countno)) {
        //
        // Constant.printMsg("for inside" + "   " + chknymmng
        // + "    " + chknymtext + "    " + countno);
        //
        // duplicatecheck = false;
        //
        // if (mParseString == null) {
        //
        // mParseString += "<n>" + chknymtext + countno
        // + "</n>" + "<m>" + chknymmng + "</m>";
        //
        // } else {
        //
        // mParseString += " " + "<n>" + chknymtext
        // + countno + "</n>" + "<m>" + chknymmng
        // + "</m>";
        //
        // }
        //
        // Constant.printMsg("for inside" + "   " + chknymmng
        // + "    " + chknymtext + "    " + countno
        // + "   " + mParseString);
        // // nymboolean = false;
        // }
        //
        // }
        //
        // }
        //
        // if (duplicatecheck) {
        //
        // if (mParseString == null) {
        //
        // mParseString += wormsg;
        //
        // } else {
        //
        // mParseString += " " + wormsg;
        //
        // }
        //
        // }
        //
        // Constant.printMsg("for finish" + "   " + chknymmng + "    "
        // + chknymtext + "    " + mParseString);
        //
        // }
        //
        // sendMessage("<-" + mParseString);
        //
        // // sendMessage(mParseString);
        // } else {
        //
        // sendMessage(edt_msg.getText().toString());
        // }
    }

    // public void mSendMessege() {
    //
    //
    // }

    // public class SendTyping extends AsyncTask<String, String, String> {
    //
    // @Override
    // protected void onPreExecute() {
    // // isTyping = false;
    // // TODO Auto-generated method stub
    // super.onPreExecute();
    // }
    //
    // @Override
    // protected String doInBackground(String... params) {
    // // TODO Auto-generated method stub
    // Constant.printMsg("Aaa  came to sendTYping "
    // + KachingMeApplication.getjid() + "-" + jid);
    // try {
    //
    // ArrayList<String> list = dbAdapter.getGroupMembersForTyping(
    // jid, KachingMeApplication.getjid());
    // Constant.printMsg("Aaa  came to sendTYping " + list);
    // for (int i = 0; i < list.size(); i++) {
    //
    // Constant.printMsg("Aaa  came to loop sendtyping " + i
    // + "--" + list.get(i));
    // sendNotification(list.get(i));
    // }
    //
    // } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // return null;
    // }
    //
    // @Override
    // protected void onPostExecute(String result) {
    // // TODO Auto-generated method stub
    //
    // isTyping = true;
    //
    // Constant.printMsg("GGGGGGGGGGGG   onPost execute:");
    //
    // super.onPostExecute(result);
    // IS_TYPED = true;
    // }
    //
    // }

    public void Alert() {

        try {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(mParentActivity);
            builder1.setMessage("Please Enter Some Text");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    final Dialog nagDialog = new Dialog(mParentActivity);
                    nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    nagDialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.TRANSPARENT));

                    nagDialog.setCancelable(true);

                    dialog.cancel();
                }
            });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } catch (Exception e) {

        }

    }


    void doBindService() {

        // bindService(new Intent(MUC_Chat.this, KaChingMeService.class),
        // mConnection,
        // Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    void doUnbindService() {
        if (isBound) {

            // unbindService(mConnection);
            isBound = false;
        }
    }

    @Override
    protected void onResume() {

        super.onResume();


        mAttachKachingFeatures();

        getSupportActionBar().hide();

        Constant.printMsg("Aaa  came to resume open MUC pause :"
                + Constant.isMUC_Paused + "--" + Constant.mServiceDoneGroup);

        checkGroupMember();

        IS_Front = true;
        updateReadMessages();

        handler.postDelayed(AsyncConnection, 0);
        // handler.postDelayed(AsyncConnection, 0);

        // doBindService();

        // if(Constant.isMUC_Paused)
        // {
        // Constant.printMsg("Aaa  came to resume MUC1");
        // Constant.isMUC_Paused = false;
        // doBindService();
        // }
        //
        //
        // if (Constant.mServiceDoneGroup)
        // {
        // Constant.printMsg("Aaa  came to resume MUC2");
        // doBindService();
        // Constant.printMsg("Aaa  M_Chat Constant"
        // + Constant.mServiceDoneGroup);
        // // Constant.mServiceDoneGroup = false;
        // Constant.printMsg("Aaa  M_Chat Constant after reset "
        // + Constant.mServiceDoneGroup);
        // }
        //

        // mMyhandler = new android.os.Handler() {
        // @Override
        // public void handleMessage(android.os.Message msg) {
        //
        //
        // // Constant.printMsg("DDDDD  came to resume MUC3");
        // //
        // // new ServerConnectionAsync(MUC_Chat.this).execute();
        //
        // }
        // };

        IntentFilter filter = new IntentFilter();
        filter.addAction("lastseen_broadcast");
        filter.addAction("typing_group");
        filter.addAction(Constant.BROADCAST_UPDATE_GROUP_ICON);
        filter.addAction("chat");
        filter.addAction("MUC_delivary_notification");
        filter.addAction("muc_fetch_chat");
        filter.addAction("start_typing");
        filter.addAction("update_tick");
        filter.addAction("update_left");

        registerReceiver(lastseen_event, filter);
        if(Constant.FROM_CHAT_SCREEN.equalsIgnoreCase("info")){

            if(Constant.mGroupInfoProfile!=null){

                ProfileRoundImg  mSenderImage=new ProfileRoundImg(Constant.mGroupInfoProfile);
                img_chat_avatar.setImageDrawable(mSenderImage);

            }else{
                Bitmap  bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2, Util.getBitmapOptions());
                ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                img_chat_avatar.setImageDrawable(mSenderImage);
            }
            Constant.FROM_CHAT_SCREEN="";

        }
        try {
            SliderTesting.mActivity.mFinishBackgroundAcrtivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            Constant.isMUC_Paused = false;

            // TempConnectionService.connection.disconnect();

            if (Constant.isModeDebug)
                Constant.printMsg("Aaa  came to destroy MUC");

            IS_Front = false;
            // doUnbindService();
            handlerThread.removeCallbacks(updatTitle);
            handler.removeCallbacks(AsyncConnection);
            // handlerThread.removeCallbacks(syncTyping);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        IS_Front = false;
        Constant.isMUC_Paused = true;
        if (Constant.isModeDebug)
            Constant.printMsg("Aaa  came to stop MUC");
        // doUnbindService();
		/* cursor.close(); */
        super.onStop();
    }

    // public void openGoogleDriveFiles() {
    // IntentSender intentSender = Drive.DriveApi.newOpenFileActivityBuilder()
    // // .setMimeType(new String[]{ "video/mp4", "image/jpeg" ,"image/png" })
    // .setMimeType(Constant.MIME_TYPES).build(getGoogleApiClient());
    // try {
    // startIntentSenderForResult(intentSender, REQUEST_CODE_OPENER, null,
    // 0, 0, 0);
    // } catch (SendIntentException e) {
    // Log.w(TAG, "Unable to send intent" + e.getMessage());
    // }
    //
    // }

    // @Override
    // public void onConnected(Bundle connectionHint) {
    // super.onConnected(connectionHint);
    //
    // // If there is a selected file, open its contents.
    // if (mSelectedFileDriveId != null) {
    //
    // /*
    // * DriveFile file=Drive.DriveApi.getFile(getGoogleApiClient(),
    // * mSelectedFileDriveId);
    // * gdrive_metafile_result=file.getMetadata(getGoogleApiClient
    // * ()).await();
    // *
    // * file.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null)
    // * .setResultCallback(contentsOpenedCallback);
    // */
    // /*
    // * file = Drive.DriveApi.getFile(getGoogleApiClient(),
    // * mSelectedFileDriveId); file.open(getGoogleApiClient(),
    // * DriveFile.MODE_READ_ONLY, null)
    // * .setResultCallback(contentsOpenedCallback);
    // */
    //
    // new RetrieveDriveFileContentsAsyncTask(MUC_Chat.this)
    // .execute(mSelectedFileDriveId);
    //
    // mSelectedFileDriveId = null;
    //
    // return;
    // }
    //
    // }

	/*
	 * final private ResultCallback<DriveIdResult> idCallback = new
	 * ResultCallback<DriveIdResult>() {
	 *
	 * @Override public void onResult(DriveIdResult result) { new
	 * RetrieveDriveFileContentsAsyncTask(
	 * MUC_Chat.this).execute(result.getDriveId()); } };
	 */

    // ResultCallback<DriveContentsResult> contentsOpenedCallback = new
    // ResultCallback<DriveContentsResult>() {
    // @Override
    // public void onResult(DriveContentsResult result) {
    // if (!result.getStatus().isSuccess()) {
    // // display an error saying file can't be opened
    // return;
    // }
    // // DriveContents object contains pointers
    // // to the actual byte stream
    //
    // String saveFilePath = null;
    // DriveContents contents = result.getDriveContents();
    //
    // MetadataResult res = file.getMetadata(getGoogleApiClient()).await();
    // Metadata meta = res.getMetadata();
    //
    // try {
    //
    // final int BUFFER_SIZE = 4096;
    // InputStream inputStream = contents.getInputStream();
    // if (meta.getTitle().contains(".")) {
    // saveFilePath = Constant.local_files_dir + meta.getTitle();
    // } else {
    // saveFilePath = Constant.local_files_dir + meta.getTitle()
    // + "." + meta.getFileExtension();
    // }
    // Log.d(TAG, "File path::" + saveFilePath);
    // // contents=inputStream.toString();
    //
    // FileOutputStream outputStream = new FileOutputStream(
    // saveFilePath);
    //
    // int bytesRead = 0;
    // byte[] buffer = new byte[BUFFER_SIZE];
    // while ((bytesRead = inputStream.read(buffer)) != -1) {
    // outputStream.write(buffer, 0, bytesRead);
    // }
    //
    // outputStream.close();
    // inputStream.close();
    //
    // Log.d(TAG,
    // "Google Drive File size::"
    // + new File(saveFilePath).length() / 1024);
    //
    // SEND_FILE(saveFilePath);
    // } catch (Exception e) {
    // e.printStackTrace();
    // // ACRA.getErrorReporter().handleException(e);
    // // TODO: handle exception
    // }
    //
    // }
    // };

    @Override
    protected void onPause() {
        topMenuHideFunction();
        Constant.isMUC_Paused = true;

        IS_Recorded = true;

        if(timer!=null)
            timer.cancel();

        if (Constant.isModeDebug)
            Constant.printMsg("Aaa  came to pause MUC");

        try {
            unregisterReceiver(lastseen_event);
        } catch (Exception e) {

        }

        try {
            for(int i=0; i<list.size(); i++)
            {
                MediaPlayer player = list.get(i);
                if(player!=null) {

                    player.stop();
                    player.release();
                }
            }

            seekHandler.removeCallbacks(run);
        } catch (Exception e) {

        }


        super.onPause();
    }

    // final private class RetrieveDriveFileContentsAsyncTask extends
    // ApiClientAsyncTask<DriveId, Boolean, String> {
    // String saveFilePath = null;
    //
    // public RetrieveDriveFileContentsAsyncTask(Context context) {
    // super(context);
    // }
    //
    // @Override
    // protected void onPreExecute() {
    // // TODO Auto-generated method stub
    // barProgressDialog = new ProgressDialog(MUC_Chat.this);
    // barProgressDialog.setTitle(getResources().getString(
    // R.string.downloadind_file));
    // barProgressDialog.setMessage(getResources().getString(
    // R.string.download_in_progress));
    // barProgressDialog
    // .setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
    // barProgressDialog.setProgress(0);
    // barProgressDialog.setMax(100);
    // barProgressDialog.show();
    //
    // super.onPreExecute();
    // }
    //
    // @Override
    // protected void onProgressUpdate(Boolean... values) {
    // // TODO Auto-generated method stub
    // super.onProgressUpdate(values);
    // }
    //
    // @Override
    // protected String doInBackgroundConnected(DriveId... params) {
    // String contents = null;
    // DriveFile file = Drive.DriveApi.getFile(getGoogleApiClient(),
    // params[0]);
    // DriveContentsResult contentsResult = file.open(
    // getGoogleApiClient(), DriveFile.MODE_READ_ONLY, listener)
    // .await();
    // MetadataResult res = file.getMetadata(getGoogleApiClient()).await();
    // Metadata meta = res.getMetadata();
    //
    // try {
    //
    // final int BUFFER_SIZE = 4096;
    // InputStream inputStream = contentsResult.getDriveContents()
    // .getInputStream();
    // if (meta.getTitle().contains(".")) {
    // saveFilePath = Constant.local_files_dir + meta.getTitle();
    // } else {
    // saveFilePath = Constant.local_files_dir + meta.getTitle()
    // + "." + meta.getFileExtension();
    // }
    // Log.d(TAG, "File path::" + saveFilePath);
    // contents = inputStream.toString();
    //
    // FileOutputStream outputStream = new FileOutputStream(
    // saveFilePath);
    //
    // int bytesRead = -1;
    // byte[] buffer = new byte[BUFFER_SIZE];
    // while ((bytesRead = inputStream.read(buffer)) != -1) {
    // outputStream.write(buffer, 0, bytesRead);
    // }
    //
    // outputStream.close();
    // inputStream.close();
    // } catch (Exception e) {
    // e.printStackTrace();
    // // TODO: handle exception
    // }
    //
    // return contents;
    // }
    //
    // @Override
    // protected void onPostExecute(String result) {
    //
    // super.onPostExecute(result);
    // barProgressDialog.dismiss();
    // if (result == null) {
    // showMessage("Error while downloading from the file from google drive");
    // return;
    // }
    //
    // SEND_FILE(saveFilePath);
    // // showMessage("File Saved at successfully");
    // // showMessage("File contents: " + result);
    // }
    // }

    @Override
    protected void onRestart() {
        Constant.isMUC_Paused = true;

        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
        NotificationHandler.Instance().resetCounter();


    //    ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
        Constant.printMsg("Aaa  came to restart MUC");
        super.onRestart();
    }

    @Override
    public void onClick(View view, int position) {
        Log.d("Chat", "List item click at::" + position);
        try {
            topMenuHideFunction();
            if (mActionMode == null) {
                MessageGetSet message = msg_list.get(position);
                if (message.getMedia_wa_type().equals("1")
                        || message.getMedia_wa_type().equals("2")) {
                    try {
                        Log.d(TAG,
                                "Image path::"
                                        + Constant.local_image_dir
                                        + msg_list.get(position)
                                        .getMedia_name());
                        File f = new File(Constant.local_image_dir
                                + msg_list.get(position).getMedia_name());
                        Uri uri = Uri.fromFile(f);
                        Intent intent = new Intent(
                                Intent.ACTION_VIEW);
                        String mime = "*/*";
                        MimeTypeMap mimeTypeMap = MimeTypeMap
                                .getSingleton();
                        if (mimeTypeMap.hasExtension(mimeTypeMap
                                .getFileExtensionFromUrl(uri.toString())))
                            mime = mimeTypeMap
                                    .getMimeTypeFromExtension(mimeTypeMap
                                            .getFileExtensionFromUrl(uri
                                                    .toString()));
                        intent.setDataAndType(uri, mime);
                        startActivity(intent);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                } else if (message.getMedia_wa_type().equals("4")) {

                    String urlAddress = "http://maps.google.com/maps?q="
                            + message.getLatitude() + ","
                            + message.getLongitude() + "("
                            + message.getData() + ")&iwloc=A&hl=es";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(urlAddress));

                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // TODO: handle exception
                        new AlertManager()
                                .showAlertDialog(
                                        mParentActivity,
                                        getResources()
                                                .getString(
                                                        R.string.no_activity_found_to_open_map_location),
                                        true);
                    }
                }
            } else
            {

            }
                // add or remove selection for current list item
//                onListItemSelect(position);
        } catch (Resources.NotFoundException e) {

        }
    }

    @Override
    public void onLongClick(View view, int position) {
        topMenuHideFunction();
//        onListItemSelect(position);
    }

    private void sendMessage(final String text_message) {

        Log.d("MUC_Chat", "Is joined..1");
        final String msg_id = "" + new Date().getTime();

        MessageGetSet msggetset = new MessageGetSet();

        try {
            Log.d("MUC_Chat", "Is joined..2");
            msggetset.setData(text_message.toString());
            msggetset.setKey_from_me(0);
            msggetset.setKey_id("" + msg_id);

            msggetset.setKey_remote_jid(jid);
            msggetset.setNeeds_push(1);
            msggetset.setSend_timestamp(new Date().getTime());

            if (Connectivity.isOnline(mParentActivity) && Connectivity.isTempConnection()) {
                msggetset.setStatus(2);

            } else {
                msggetset.setStatus(3);

            }

            msggetset.setTimestamp(new Date().getTime());

            msggetset.setMedia_wa_type("0");
            msggetset.setRemote_resource(KachingMeApplication.getUserID()
                    + KachingMeApplication.getHost());
            msggetset.setIs_sec_chat(1);
            long l =   dbAdapter.setInsertMessages(msggetset);

            msggetset.set_id((int)l);


           /* if (dbAdapter.isExistinChatList_chat(jid, 0)) {
                dbAdapter.setUpdateChat_lits_chat(jid, (int)l-1, 0);
            } else {
                dbAdapter.setInsertChat_list_chat(jid, (int)l-1, 0);
            }*/


         //   dbAdapter.setInsertChat_list(jid, (int)l);
            edt_msg.setText("");
            Constant.printMsg("MUC_Cha Is joined..3 " + msggetset.toString());
            // Get size in double to update msg network usage
            try {

                long bite = text_message.toString().getBytes().length;
                Constant.printMsg("Double :" + bite);
                updateNetwork_Message_Sent(bite);

            } catch (Exception e) {

            }

            msg_list.add(msggetset);

            intializationElements(1);
            rightTextChat();
            setRightChatText();
            k = k + 1;

            mRightTipLayout.requestFocus();
            edt_msg.setText("");
            edt_msg.requestFocus();



//
//            Log.d("MUC_Chat", "Is joined..4");
//            int l = dbAdapter.getLastMsgid(jid);
//
//            if (dbAdapter.isExistinChatList(jid)) {
//                dbAdapter.setUpdateChat_lits(jid, l);
//            } else {
//                dbAdapter.setInsertChat_list(jid, l);
//            }
//            Log.d("MUC_Chat", "Is joined..5");
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {

                        ArrayList<String> list = dbAdapter
                                .getGroupMembersForTyping(jid,
                                        KachingMeApplication.getjid());

                        Log.d("MUC_Chat", "Is joined..6a");
                        Message msg = new Message(jid, Type.groupchat);
                        Log.d("MUC_Chat", "Is joined..6b");
                        msg.setStanzaId(msg_id);
                        Log.d("MUC_Chat", "Is joined..6c");
                        msg.setBody(Utils.EncryptMessage(text_message
                                .toString()));
                        Log.d("MUC_Chat", "Is joined..6d");
                        // msg.setBody(text_message.toString());
                        JivePropertiesManager.addProperty(msg, "ID", 5);
                        Log.d("MUC_Chat", "Is joined..6e");
                        JivePropertiesManager.addProperty(msg, "media_type",
                                "0");
                        Log.d("MUC_Chat", "Is joined..7" + muc.isJoined());

                        Constant.printMsg("RRRR " + msg.toString());

                        MessageEventManager.addNotificationsRequests(msg, true,
                                true, true, true);
                        DeliveryReceiptRequest.addTo(msg);

                        muc.sendMessage(msg);

                        // muc.sendMessage(msg);
                    } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);

                        // if(mBoundService!=null)
                        // {
                        // mBoundService.connect();
                        //
                        // Constant.printMsg("Aaa Exception in send msg 1 if condition");
                        // }

                        Constant.printMsg("Aaa Exception in send msg 1 :"
                                + e.toString());
                    }
                }
            };

            thread.start();


            int msg_idll = dbAdapter.getLastMsgid(jid);

            if (dbAdapter.isExistinChatList(jid)) {
                dbAdapter.setUpdateChat_lits(jid, msg_idll);
            } else {
                dbAdapter.setInsertChat_list(jid, msg_idll);
            }



        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            Constant.printMsg("Aaa Exception in send msg 2 :" + e.toString());
        }
//        if (KachingMeApplication.getIsNetAvailable()) {
//            dbAdapter.setUpdateMessage_status(jid, msggetset.getKey_id(), 2);
//        }
        // android.util.Log.d(TAG,
        // "Internet::"+KachingMeApplication.getIsNetAvailable()+"::"+connection.isConnected());
        // SendWeb_Group.Add_Message_on_web(mParentActivity, msggetset);
//        Intent login_broadcast = new Intent("chat");
//        login_broadcast.putExtra("jid", "" + jid);
//        sendBroadcast(login_broadcast);
    }

    public void sendNotification(String jid) {
        MessageEventManager messageeventmanager = TempConnectionService.messageEventManager;
        try {
            messageeventmanager.sendComposingNotification(jid,
                    Constant.TYPING_STATUS_GROUP);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void updateCursor() {
        cursor = dbAdapter.getMessagesCursor(jid);

    }

    public void updateReadMessages() {

        try {
            ArrayList<MessageGetSet> unreadMessageList = new ArrayList<MessageGetSet>();
            unreadMessageList = dbAdapter.getUnreadMessages(jid);
            // Constant.printMsg("Unread Message Called...::"+unreadMessageList.size());

            for (MessageGetSet messageGetSet : unreadMessageList) {

                dbAdapter.setUpdateMessage_display(jid, messageGetSet.getKey_id());

            }

            dbAdapter.setUpdateContact_unseen_msg(jid, 0);
        } catch (Exception e) {

        }
    }

    // ///////////*********** Audio Recorde Dialog
    // ****************///////////////////////////////////////////
    public void voice_dialog() {
        try {
            myRecorder = new MediaRecorder();
            myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            boolean success = (new File(Constant.local_audio_dir)).mkdirs();
            final Dialog dialog = new Dialog(this);
            dialog.getWindow();
            View localView = getWindow().getDecorView();
            int i = 90 * Math.min(localView.getWidth(), localView.getHeight()) / 100;
            dialog.getWindow().getAttributes().width = i;
            dialog.getWindow().getAttributes().height = LayoutParams.MATCH_PARENT;
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.voice_dialog);
            final Chronometer ch = (Chronometer) dialog.findViewById(R.id.chronometer1);
            Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
            final Button btn_send = (Button) dialog.findViewById(R.id.btn_send);
            final ImageButton btn_record = (ImageButton) dialog.findViewById(R.id.btn_record);
            is_started = false;
            is_startrec = true;

            if (!success)
            {
                Log.e("directory not created", "directory not created");
            }
            final String file_path = Constant.local_audio_dir + System.currentTimeMillis() + ".amr";

            myRecorder.setOutputFile(file_path);

            try
            {
                myRecorder.prepare();
            }
            catch (IllegalStateException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            btn_record.setOnLongClickListener(new OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    Log.e("Record", "Long Pressed");
                    ch.start();
                    return true;
                }
            });

            btn_record.setOnTouchListener(new OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    // TODO Auto-generated method stub
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        if (!is_started)
                        {
                            Log.e("Chat Action Up 1 ", is_started + "\n" + is_startrec);

                            try
                            {
                                myRecorder.start();

                                IS_Recorded = false;
                            }
                            catch (Exception e)
                            {
                                // ACRA.getErrorReporter().handleException(e);
                                e.printStackTrace();
                            }



                            ch.setBase(SystemClock.elapsedRealtime());
                            Log.e("Record", "Action Up");
                            Log.e("Chat Action Up 2 ", is_started + "\n" + is_startrec);
                            btn_record.setImageDrawable(getResources().getDrawable(R.drawable.btn_record));
                            ch.start();
                            is_started = true;
                            is_startrec = false;
                        }
                    }
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                    {

                        IS_Recorded = true;
                        if (!is_startrec)
                        {

                            if(timer!=null)
                                timer.cancel();


                            Log.e("Record", "Action Down");

                            Log.e("Action Down", "Down" + is_startrec);

                            btn_record.setImageDrawable(getResources().getDrawable(R.drawable.btn_send));

                            ch.stop();



                            String chrono_text = ch.getText().toString();

                            if (chrono_text.equalsIgnoreCase("00:00"))
                            {
                                ch.stop();
                                btn_record.setImageDrawable(getResources().getDrawable(R.drawable.btn_hold_talk));
                                Toast.makeText(getApplicationContext(), "Hold To Record", Toast.LENGTH_LONG).show();
                                is_startrec = true;
                                is_started = true;
                            }

                            try
                            {
                                if (myRecorder != null)
                                {
                                    myRecorder.stop();
                                    myRecorder.release();
                                    myRecorder = null;
                                }

                                File f = new File(file_path);
                                String File_name = f.getName();
                                int size = (int) f.length();
                                int mediaDuration = 0;

                                MediaPlayer mediaAudioPlayer = new MediaPlayer();
                                mediaAudioPlayer.setDataSource(file_path);
                                mediaAudioPlayer.prepare();
                                mediaDuration = mediaAudioPlayer.getDuration();

                                is_startrec = false;
                                is_started = false;

                                Log.e("Chat Test Duration", mediaDuration + "\n" + size + "\n" + File_name);
                            }
                            catch (IllegalStateException e)
                            {
                                Log.e("Chat Test Illegal", is_started + "\n" + is_startrec + "\n" + e.toString());

                                e.printStackTrace();

                                if (is_startrec)
                                {
                                    is_started = false;
                                    is_startrec = false;
                                    Log.e("Illegal Status 1", is_started + "\n" + is_startrec);
                                }
                                else
                                {
                                    is_started = true;
                                    is_startrec = false;
                                    Log.e("Illegal Status 2", is_started + "\n" + is_startrec);
                                }
                            }
                            catch (RuntimeException e)
                            {
                                Log.e("Chat Test Runtime", is_started + "\n" + is_startrec + "\n" + e.toString());
                                e.printStackTrace();
                                btn_record.setImageDrawable(getResources().getDrawable(R.drawable.btn_hold_talk));
                                is_startrec = true;
                                is_started = false;
                                Log.e("Chat Test Runtime 1", is_started + "\n" + is_startrec + "\n" + e.toString());
                            }
                            catch (IOException e)
                            {
                                Log.e("Chat Test", e.toString());
                                e.printStackTrace();
                            }
                        }
                    }
                    return true;
                }
            });

            btn_cancel.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                }
            });

            btn_send.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Constant.printMsg(is_startrec + "Start Rec");

                    if (is_startrec)
                    {
                        Toast.makeText(getApplicationContext(), "No Audio File Found..!!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        File f = new File(file_path);
                        String File_name = f.getName();
                        int size = (int) f.length();
                        Log.e("Chat Test Send", size + "\n" + File_name);
                        upload_audio_File(file_path);
                        dialog.dismiss();
                    }
                }
            });

            dialog.setCancelable(false);
            dialog.show();
        } catch (IllegalStateException e) {

        }
    }

    private void topMenuHideFunction() {
        if (mMenuVisible) {
            mChatHedderAttachmentLayout
                    .setBackgroundColor(Color
                            .parseColor("#00000000"));
            mMenuVisible = false;
            mAttachmentIconMenuPopup();
            attachmentOptionMenuClickListener();
            mToolTipImg.setVisibility(View.GONE);
        } else {
            if (mContactMenuVisible) {
                String query = "select status from " + Dbhelper.TABLE_LOCK
                        + " where jid = '" + jid + "'";
                lock_status(query);
                mChatHedderMenuLayout.setBackgroundColor(Color
                        .parseColor("#00000000"));
                mContactMenuVisible = false;
                chatOptionMenu();
                if (status_lock.equalsIgnoreCase("lock")) {
                    mChatOptionMenuLockchatTxt.setText("Unlock Chat");
                } else {
                    mChatOptionMenuLockchatTxt.setText("Lock Chat");
                }
                chatOptionOnClickListeners();
                mToolTipImg.setVisibility(View.GONE);
            }
        }
    }

    // /////////////////************ IMAGE / VIDEO PICKER
    // *****************/////////////////////////////////
    public void image_picker(int result) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        try {
            if (result == 11) {
                // photoPickerIntent.setType("image/*");
                // startActivityForResult(photoPickerIntent, 11);

                /*Intent intent = new Intent(mParentActivity,
                        CustomPhotoGalleryActivity.class);

                startActivityForResult(intent, RESULT_CODE_IMAGE_MULTIPLE);*/

                Intent intent = new Intent(MUCTest.this, AlbumSelectActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
                startActivityForResult(intent, Constants.REQUEST_CODE);

            } else if (result == 22) {
                photoPickerIntent.setType("video/*");
                startActivityForResult(photoPickerIntent, 22);
            }

        } catch (ActivityNotFoundException e) {
            AlertDialog.Builder b = new AlertDialog.Builder(mParentActivity);
            b.setMessage(
                    getResources().getString(R.string.it_seems_like_no_gallery))
                    .setCancelable(false);
            b.setNegativeButton(getResources().getString(R.string.Ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = b.create();
            alert.show();
        }
    }

    // /////////////************Activity
    // Results********************//////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && imageReturnedIntent != null)
        {
            Constant.mSelectedImage = new ArrayList();

            ArrayList<Image> images = imageReturnedIntent.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);

            StringBuffer stringBuffer = new StringBuffer();

            for (int i = 0, l = images.size(); i < l; i++)
            {
                stringBuffer.append(images.get(i).path + "|");

                Constant.mSelectedImage.add(images.get(i).path);
            }
            Constant.mImagepath = stringBuffer.toString();

            if (resultCode == RESULT_OK)
            {
                imagesPathList = new ArrayList<>();

                String[] imagesPath = Constant.mImagepath.split("\\|");

                System.out.println("img path url select multiple:::::::::>>>>>>>>" + imagesPath);

                for (int i = 0; i < imagesPath.length; i++)
                {
                    System.out.println("img path url select multiple:::::::::>>>>>>>>" + imagesPath[i]);

                    imagesPathList.add(imagesPath[i]);

                    File file = new File(imagesPath[i]);

                    final String filepath = imagesPath[i];

                    long length = file.length();

                    length = length / 1024;

                    if (length > 16384)
                    {
                        new AlertManager().showAlertDialog(this, getResources().getString(R.string.imagesize_must_be_smaller), true);
                    }
                    else
                    {
                        Thread t = new Thread(new Runnable()
                        {
                            @Override
                            public synchronized void run()
                            {
                                // TODO Auto-generated method stub

                                try
                                {
                                    for (int i = 0; i < Constant.mSelectedImage.size(); i++)
                                    {
                                        Constant.printMsg("IIIIIIIII");

                                        Constant.printMsg("test path sender::::::::::::: " + Constant.mSelectedImage.get(i) + "   " + i);

                                        synchronized (this)
                                        {
                                            uploadFile(String.valueOf(Constant.mSelectedImage.get(i)), true);
                                        }
                                    }
                                }
                                catch (Exception e)
                                {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        });
                        t.start();

                        i = imagesPath.length;
                    }
                }
            }
        }

        switch (requestCode) {
            case 11:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    final String filePath = cursor.getString(columnIndex);

                    cursor.close();

                    new Thread(new Runnable() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                }
                            });

                            uploadFile(filePath, true);

                        }
                    }).start();

                }
                break;
            case 22:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    final String filePath = cursor.getString(columnIndex);

                    cursor.close();

                    new Thread(new Runnable() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                }
                            });

                            uploadFile(filePath, false);

                        }
                    }).start();

                }
                break;

            case 33:
                if (resultCode == RESULT_OK) {
                    Uri audioUri = imageReturnedIntent.getData();
                    Log.d("", "Recorded uri::" + audioUri.getPath().toString());

                }

                break;
            case 44:

                if (resultCode == RESULT_OK) {
                    if (imageReturnedIntent.getExtras() != null) {

                        try {
                            // Get size in bytes to update media network usage
                            long size = imageReturnedIntent
                                    .getByteArrayExtra("map_thumb").length;
                            updateMediaNetwork(size);
                        } catch (Exception e) {

                        }

                        send_Location(imageReturnedIntent.getStringExtra("lat"),
                                imageReturnedIntent.getStringExtra("lon"),
                                imageReturnedIntent.getByteArrayExtra("map_thumb"));
                    }
                }
                break;
            case 55:
                if (resultCode == Activity.RESULT_OK) {





                    Uri contactData = imageReturnedIntent.getData();
                    Cursor phones = managedQuery(contactData, null, null, null,
                            null);
                    phones.moveToFirst();

                    String lookupKey = phones.getString(phones
                            .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    Uri uri = Uri.withAppendedPath(
                            ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);


                    String id_data = phones.getString(
                            phones.getColumnIndex(ContactsContract.Contacts._ID));

                    if (Integer.parseInt(phones.getString(phones.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = mParentActivity.getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id_data}, null);
                        while (pCur.moveToNext()) {
                            numberContactSend = pCur.getString(pCur.getColumnIndex("data1"));
                            if(numberContactSend!=null)
                            {

                                numberContactSend = numberContactSend.replaceAll("\\p{P}","");
                                numberContactSend = numberContactSend.replaceAll(" ","");
                                numberContactSend = numberContactSend.replaceAll("-","");
                            }

                            Constant.printMsg("Diliiip " + numberContactSend );
                        }
                        pCur.close();
                    }

                    AssetFileDescriptor fd;
                    try {
                        fd = this.getContentResolver().openAssetFileDescriptor(uri,
                                "r");
                        FileInputStream fis = fd.createInputStream();
                        byte[] buf = new byte[(int) fd.getDeclaredLength()];
                        fis.read(buf);
                        String vCard = new String(buf);
                        Intent intent = new Intent(this, SendContact.class);
                        intent.putExtra("vcard", vCard);
                        startActivityForResult(intent, 66);
                        Log.d("Vcard", vCard);

                        // Get size in double to update media network usage

                        long bite = (long) fd.getDeclaredLength();
                        updateMediaNetwork(bite);
                    } catch (Exception e1) {
                        // ACRA.getErrorReporter().handleException(e1);
                        e1.printStackTrace();
                    }

                }
                break;
            case 66:
                if (resultCode == RESULT_OK) {
                    byte[] avatar = null;
                    try {
                        avatar = imageReturnedIntent
                                .getByteArrayExtra("contact_thumb");
                    } catch (Exception e) {
                        // ACRA.getErrorReporter().handleException(e);
                    }
                    String vcard = imageReturnedIntent.getStringExtra("vcard");
                    String name = imageReturnedIntent.getStringExtra("name");
                    send_Contact(avatar, vcard, name);
                }
                break;
            case 77:

                if (resultCode == Activity.RESULT_OK) {

                    String selected_file = imageReturnedIntent
                            .getStringExtra(FileDialog.RESULT_PATH);
                    Log.d("Chat", "Selected File::" + selected_file);
                    File file = new File(selected_file);

                    double megabytes = ((file.length() / 1024) / 1024);
                    Log.d("Chat", "File Size::" + megabytes);
                    if (megabytes > 25) {
                        Toast.makeText(
                                this,
                                getResources().getString(
                                        R.string.filesize_must_be_smaller),
                                Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            Utils.Copyfile(file, new File(Constant.local_files_dir
                                    + file.getName()));
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        SEND_FILE(selected_file);
                    }
                }

                break;
            case REQUEST_CODE_OPENER:
                if (requestCode == REQUEST_CODE_OPENER && resultCode == RESULT_OK) {
                    mSelectedFileDriveId = (DriveId) imageReturnedIntent
                            .getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                    // showMessage("File ID::"+mSelectedFileDriveId);

                    // new
                    // RetrieveDriveFileContentsAsyncTask(MUC_Chat.this).execute(mSelectedFileDriveId);
				/*
				 * file = Drive.DriveApi.getFile(getGoogleApiClient(),
				 * mSelectedFileDriveId); file.open(getGoogleApiClient(),
				 * DriveFile.MODE_READ_ONLY, null)
				 * .setResultCallback(contentsOpenedCallback);
				 */

                }
                break;
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // Image captured and saved to fileUri specified in the Intent
				/*
				 * Toast.makeText(this, "Image saved to:\n" + fileUri.getPath(),
				 * Toast.LENGTH_LONG).show();
				 */
                    File file = new File(fileUri.getPath());
                    if (file.length() > 26214400) {
                        new AlertManager().showAlertDialog(this, getResources()
                                        .getString(R.string.imagesize_must_be_smaller),
                                true);
                    } else {

                        uploadFile(fileUri.getPath(), true);
                    }
                }
                break;

            case CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // Image captured and saved to fileUri specified in the Intent
				/*
				 * Toast.makeText(this, "Image saved to:\n" + fileUri.getPath(),
				 * Toast.LENGTH_LONG).show();
				 */
                    File file = new File(fileUri.getPath());
                    if (file.length() > 26214400) {
                        new AlertManager().showAlertDialog(this, getResources()
                                        .getString(R.string.videosize_must_be_smaller),
                                true);
                    } else {

                        uploadFile(fileUri.getPath(), false);
                    }
                }
                break;

            case RESULT_CODE_IMAGE_MULTIPLE:
                if (resultCode == RESULT_OK) {

                    imagesPathList = new ArrayList<String>();

                    // String[] imagesPath = imageReturnedIntent
                    // .getStringExtra("data").split("\\|");
                    String[] imagesPath = Constant.mImagepath.split("\\|");
                    System.out
                            .println("img path url select multiple:::::::::>>>>>>>>"
                                    + imagesPath);
                    for (int i = 0; i < imagesPath.length; i++) {
                        System.out
                                .println("img path url select multiple:::::::::>>>>>>>>"
                                        + imagesPath[i]);
                        imagesPathList.add(imagesPath[i]);

                        File file = new File(imagesPath[i]);

                        final String filepath = imagesPath[i];
                        long length = file.length();

                        length = length / 1024;
                        if (length > 16384) {
                            new AlertManager().showAlertDialog(this, getResources()
                                            .getString(R.string.imagesize_must_be_smaller),
                                    true);
                        } else {
                            // new Thread(new Runnable() {
                            // public void run() {
                            // runOnUiThread(new Runnable() {
                            // public void run() {
                            //
                            // }
                            // });
                            // Constant.printMsg("file path:::::::::"
                            // + filepath);
                            // uploadFile(filepath, true);
                            //
                            // }
                            // }).start();

                            // new Handler().postDelayed(new Runnable() {
                            // public void run() {

                            // for (int j = 0; j <
                            // KachingUtill.mSelectedImage.size(); j++) {
                            //
                            // Constant.printMsg("callll");
                            //
                            // final int pos = j;

                            Thread t = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub

                                    try {
                                        for (int i = 0; i < Constant.mSelectedImage
                                                .size(); i++) {
                                            Constant.printMsg("IIIIIIIII");

                                            uploadFile(
                                                    String.valueOf(Constant.mSelectedImage
                                                            .get(i)), true);
                                            Thread.sleep(1000);

                                        }

                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                }

                            });
                            t.start();

                            // }
                            i = imagesPath.length;

                            // }
                            //
                            // }, count * 5);
                        }
                    }

                }

			/*
			 * case 11:
			 *
			 * this.finish(); break;
			 */
        }
    }

    // ////////********************UPLOAD IMAGE / VIDEOS
    // *******************///////////////////////
    public void uploadFile(String strURL, boolean is_image) {
        try {
            i++;

            String time = "" + System.currentTimeMillis();
            String File_name = time + String.valueOf(i) + ".jpg";
            String file_path = "";
            int media_duration = 0;
            int size = 0;
            byte[] thumb = null;

            String strMyImagePath = null;
            Bitmap scaledBitmap = null;
            String urlString = "";
            if (is_image) {

                try {

                    byte[] byteArray = null;
                    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                    ByteArrayOutputStream outstream_thumb = new ByteArrayOutputStream();


                    Bitmap thumb_bitmap =  new CompressImage().compressImage(strURL, file_path, 13);
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 80,
                            outstream_thumb);
                    thumb = outstream_thumb.toByteArray();

                    boolean success = (new File(Constant.local_image_dir))
                            .mkdirs();
                    if (!success) {
                        Log.w("directory not created", "directory not created");
                    }

                    file_path = Constant.local_image_dir + File_name;
                    new CompressImage().compressImage(strURL, file_path,1);
//                    FileOutputStream stream = new FileOutputStream(file_path);
//
//                    byteArray = outstream.toByteArray();
//
//                    stream.write(byteArray);
//                    stream.close();

                    // Toast.makeText(context, "Downloading Completed",
                    // Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // ACRA.getErrorReporter().handleException(e);
                    e.printStackTrace();
                }

                urlString = KachingMeConfig.UPLOAD_MEDIA_PHP;

            } else {
                file_path = strURL;
                urlString = KachingMeConfig.UPLOAD_MEDIA_PHP;
                Bitmap video_thumb = ThumbnailUtils.createVideoThumbnail(file_path,
                        MediaStore.Images.Thumbnails.MINI_KIND);

                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                video_thumb.compress(Bitmap.CompressFormat.JPEG, 50, outstream);

                thumb = outstream.toByteArray();

                File f = new File(strURL);
                File_name = f.getName();
                size = (int) f.length();
                try {

                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(this, Uri.fromFile(f));
                    String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    timeInMillisec = Integer.parseInt(duration);



                    try {
                        if (!f.getPath().equals(
                                Constant.local_video_dir + File_name)) {
                            Utils.Copyfile(f, new File(Constant.local_video_dir
                                    + File_name));
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        // ACRA.getErrorReporter().handleException(e);
                        e.printStackTrace();
                    }

                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                    e.printStackTrace();
                }

            }

            MessageGetSet msggetset = new MessageGetSet();
            String packet_id = "" + new Date().getTime();

            msggetset.setData("");
            msggetset.setKey_from_me(0);
            msggetset.setKey_id("" + packet_id);
            msggetset.setKey_remote_jid(jid);
            msggetset.setNeeds_push(1);
            msggetset.setSend_timestamp(new Date().getTime());
            msggetset.setStatus(3);
            msggetset.setTimestamp(new Date().getTime());
            msggetset.setThumb_image(thumb);

            msggetset.setMedia_name(File_name);
            msggetset.setMedia_mime_type(new Utils().getMimeType(file_path));
            if (is_image) {
                msggetset.setMedia_wa_type("" + 1);
            } else {
                msggetset.setMedia_wa_type("" + 2);
            }
            msggetset.setMedia_url(null);
            msggetset.setRow_data(thumb);
            msggetset.setMedia_duration(timeInMillisec);
            msggetset.setMedia_size(size);
            msggetset.setRemote_resource(KachingMeApplication.getUserID()
                    + KachingMeApplication.getHost());
            msggetset.setIs_sec_chat(1);
            long idvalue=dbAdapter.setInsertMessages(msggetset);
            msggetset.set_id((int)idvalue);

            // Get size in double to update media network usage
            try {

                File f = new File(strURL);
                long bite = (long) f.length();
                updateMediaNetwork(bite);

            } catch (Exception e) {

            }


            int msg_id = dbAdapter.getLastMsgid(jid);

            if (dbAdapter.isExistinChatList(jid)) {
                dbAdapter.setUpdateChat_lits(jid, msg_id);
            } else {
                dbAdapter.setInsertChat_list(jid, msg_id);
            }


//            msg_list.add(msggetset);
//
//            if (is_image) {
//
//                mParentActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        intializationElements(1);
//                        rightImageChat();
//                        setRightImage();
//                        k = k + 1;
//
//                        mRightTipLayout.requestFocus();
//                        edt_msg.setText("");
//                        edt_msg.requestFocus();
//                    }
//                });
//            } else {
//
//                mParentActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        intializationElements(1);
//                        rightVideoChat();
//                        setRightVideo();
//                        k = k + 1;
//
//                        mRightTipLayout.requestFocus();
//                        edt_msg.setText("");
//                        edt_msg.requestFocus();
//                    }
//                });
//            }

            ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
        } catch (Exception e) {

        }

    }

    /*public void uploadLogo(byte[] strURL, boolean is_image) {

        try {
            Constant.bux = sharedPrefs1.getLong("buxvalue", 0);

            Long buxval = Constant.bux + Constant.imagepoints;
            Constant.bux = buxval;

            Editor e1 = sharedPrefs1.edit();
            e1.putLong("buxvalue", buxval);
            e1.commit();

            String time = "" + System.currentTimeMillis();
            String File_name = time + ".jpg";
            String file_path = "";
            int media_duration = 0;
            int size = 0;
            byte[] thumb = null;

            String strMyImagePath = null;
            Bitmap scaledBitmap = null;
            String urlString = "";

            Constant.printMsg("path:;" + strURL + is_image);

            if (is_image) {

                try {
                    Constant.printMsg("path:;inside" + strURL + is_image);

                    byte[] byteArray = null;
                    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                    ByteArrayOutputStream outstream_thumb = new ByteArrayOutputStream();
                    // Part 1: Decode image
                    Bitmap unscaledBitmap = BitmapFactory.decodeByteArray(strURL,
                            0, strURL.length);

                    if (!(unscaledBitmap.getWidth() <= 480 && unscaledBitmap
                            .getHeight() <= 800)) {
                        // Part 2: Scale image
                        scaledBitmap = ScalingUtilities.createScaledBitmap(
                                unscaledBitmap, 480, 800);
                        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75,
                                outstream);
                        scaledBitmap.recycle();
                    } else {

                        unscaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75,
                                outstream);
                        unscaledBitmap.recycle();
                    }

                    Bitmap thumb_bitmap = BitmapFactory.decodeByteArray(strURL, 0,
                            strURL.length);
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 75,
                            outstream_thumb);
                    thumb = outstream_thumb.toByteArray();

                    boolean success = (new File(Constant.local_image_dir))
                            .mkdirs();

                    Constant.printMsg("logo success:;" + success + File_name);

                    if (!success) {
                        Log.w("directory not created", "directory not created");
                    }

                    file_path = Constant.local_image_dir + File_name;

                    FileOutputStream stream = new FileOutputStream(file_path);

                    byteArray = outstream.toByteArray();

                    stream.write(byteArray);
                    stream.close();

                    // Toast.makeText(context, "Downloading Completed",
                    // Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // ACRA.getErrorReporter().handleException(e);
                    e.printStackTrace();
                }

                urlString = getString(R.string.webservice_host)
                        + getString(R.string.media_upload_webservice);

            }

            Constant.printMsg("file:;" + file_path);

            MessageGetSet msggetset = new MessageGetSet();
            String packet_id = "" + new Date().getTime();

            msggetset.setData("");
            msggetset.setKey_from_me(0);
            msggetset.setKey_id("" + packet_id);
            msggetset.setKey_remote_jid(jid);
            msggetset.setNeeds_push(1);
            msggetset.setSend_timestamp(new Date().getTime());
            msggetset.setStatus(3);
            msggetset.setTimestamp(new Date().getTime());
            msggetset.setThumb_image(thumb);

            msggetset.setMedia_name(File_name);
            msggetset.setMedia_mime_type(new Utils().getMimeType(file_path));
            // if (is_image) {
            msggetset.setMedia_wa_type("" + 12);
            // }
            msggetset.setMedia_url(null);
            msggetset.setRow_data(thumb);
            msggetset.setMedia_duration(media_duration);
            msggetset.setMedia_size(size);
            msggetset.setRemote_resource(KachingMeApplication.getUserID()
                    + KachingMeApplication.getHost());
            msggetset.setIs_sec_chat(1);
            dbAdapter.setInsertMessages(msggetset);


            int msg_id = dbAdapter.getLastMsgid(jid);

            if (dbAdapter.isExistinChatList(jid)) {
                dbAdapter.setUpdateChat_lits(jid, msg_id);
            } else {
                dbAdapter.setInsertChat_list(jid, msg_id);
            }


            Intent login_broadcast = new Intent("chat");
            login_broadcast.putExtra("jid", "" + jid);
            sendBroadcast(login_broadcast);
        } catch (Exception e) {

        }

    }*/

    // ////////////////////////************** SEND
    // LOCATION*******************//////////////////////////////////////
    public void send_Location(String lat, String log, byte[] map_data) {


        try {
            int point = sharedPrefs.getInt("locpoint", 0);

            Constant.totalloc = point;

            Constant.totalloc = Count + Constant.totalloc;

            Editor e1 = sharedPrefs.edit();
            e1.putInt("locpoint", Constant.totalloc);
            e1.commit();
            Constant.bux = sharedPrefs.getLong("buxvalue", 0);

            Long buxval1 = Constant.bux + Constant.wheRpoint;
            Constant.bux = buxval1;

            Editor e3 = sharedPrefs.edit();
            e3.putLong("buxvalue", buxval1);
            e3.commit();


            Log.d("Location Recieved", "Lat::" + lat + "Log::" + log);
            MessageGetSet msggetset = new MessageGetSet();
            String packet_id = "" + new Date().getTime();

            msggetset.setData("");
            msggetset.setKey_from_me(0);
            msggetset.setKey_id("" + packet_id);
            msggetset.setKey_remote_jid(jid);
            msggetset.setNeeds_push(1);
            msggetset.setSend_timestamp(new Date().getTime());
            msggetset.setStatus(3);
            msggetset.setTimestamp(new Date().getTime());
            msggetset.setThumb_image(null);
            msggetset.setLatitude(Double.parseDouble(lat));
            msggetset.setLongitude(Double.parseDouble(log));
            msggetset.setRemote_resource(KachingMeApplication.getUserID()
                    + KachingMeApplication.getHost());
            // msggetset.setMedia_size(5);
            msggetset.setMedia_wa_type("" + 4);

            msggetset.setRow_data(map_data);

            msggetset.setIs_sec_chat(1);

            dbAdapter.setInsertMessages(msggetset);
            // edt_msg.setText("");


            int msg_id = dbAdapter.getLastMsgid(jid);

            if (dbAdapter.isExistinChatList(jid)) {
                dbAdapter.setUpdateChat_lits(jid, msg_id);
            } else {
                dbAdapter.setInsertChat_list(jid, msg_id);
            }

            Message msg = new Message(jid, Type.groupchat);
            JivePropertiesManager.addProperty(msg, "msg_type", 4);
            msg.setPacketID("" + packet_id);
            msg.setBody("");
            JivePropertiesManager.addProperty(msg, "media_type", 4);
            JivePropertiesManager.addProperty(msg, "thumb_image",
                    Base64.encodeToString(map_data, 0, map_data.length));

            JivePropertiesManager.addProperty(msg, "lat", lat);
            JivePropertiesManager.addProperty(msg, "lon", log);
            JivePropertiesManager.addProperty(msg, "media_type", "4");
            JivePropertiesManager.addProperty(msg, "ID", 5);

            try {/*
                 * if(KachingMeApplication.getIsNetAvailable()) {
                 */
                DeliveryReceiptRequest.addTo(msg);
                MessageEventManager.addNotificationsRequests(msg, true, true, true,
                        true);
                muc.sendMessage(msg);

                /* } */

            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
                e.printStackTrace();
            }

            msg_list.add(msggetset);


            mParentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    intializationElements(1);
                    rightImageChat();
                    setRightLocation();
                    k = k + 1;

                    mRightTipLayout.requestFocus();
                    edt_msg.setText("");
                    edt_msg.requestFocus();
                }
            });

            ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());

            SendWeb_Group.Add_Message_on_web(mParentActivity, msggetset);
        } catch (NumberFormatException e) {

        }
    }

    // ////////////////////////************** SEND
    // CONTACT*******************//////////////////////////////////////
    public void send_Contact(final byte[] avatar, final String vcard,
                             final String name) {

        MessageGetSet msggetset = new MessageGetSet();
        final String packet_id = "" + new Date().getTime();

        msggetset.setData(vcard);
        msggetset.setKey_from_me(0);
        msggetset.setKey_id("" + packet_id);
        msggetset.setKey_remote_jid(jid);
        msggetset.setNeeds_push(1);
        msggetset.setSend_timestamp(new Date().getTime());
        msggetset.setStatus(3);
        msggetset.setTimestamp(new Date().getTime());
        msggetset.setThumb_image(null);

        msggetset.setMedia_name(name+","+numberContactSend);

        msggetset.setMedia_wa_type("" + 5);

        msggetset.setRow_data(avatar);

        msggetset.setRemote_resource(KachingMeApplication.getUserID()
                + KachingMeApplication.getHost());

        msggetset.setIs_sec_chat(1);
        dbAdapter.setInsertMessages(msggetset);
        // edt_msg.setText("");


        int msg_id = dbAdapter.getLastMsgid(jid);

        if (dbAdapter.isExistinChatList(jid)) {
            dbAdapter.setUpdateChat_lits(jid, msg_id);
        } else {
            dbAdapter.setInsertChat_list(jid, msg_id);
        }

        mParentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                intializationElements(1);
                rightContactChat();
                setRightContact();
                k = k + 1;

                mRightTipLayout.requestFocus();
                edt_msg.setText("");
                edt_msg.requestFocus();
            }
        });

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

                    Message msg = new Message(jid, Type.groupchat);
                    JivePropertiesManager.addProperty(msg, "msg_type", 4);
                    msg.setPacketID("" + packet_id);
                    msg.setBody(Utils.EncryptMessage(vcard));
                    // msg.setBody(vcard);
                    JivePropertiesManager.addProperty(msg, "media_type", "5");
                    JivePropertiesManager.addProperty(msg, "ID", 5);

                    if (avatar != null) {
                        JivePropertiesManager.addProperty(msg, "thumb_image",
                                avatar);
                    }

                    JivePropertiesManager.addProperty(msg, "media_name", name+","+numberContactSend);

                    DeliveryReceiptRequest.addTo(msg);
                    try {
                        MessageEventManager.addNotificationsRequests(msg, true,
                                true, true, true);
                        muc.sendMessage(msg);

                    } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        };

        thread.start();
        ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());

        SendWeb_Group.Add_Message_on_web(mParentActivity, msggetset);
    }

    // //////////////////////************** SEND
    // AUDIO*******************//////////////////////////////////////
    public void upload_audio_File(String strURL) {

        try {
            String time = "" + System.currentTimeMillis();
            String File_name = time + ".jpg";
            String file_path = "";
            int media_duration = 0;
            int size = 0;
            file_path = strURL;
            File f = new File(strURL);
            File_name = f.getName();
            size = (int) f.length();
            try {
                MediaPlayer mp = new MediaPlayer();
                mp.setDataSource(file_path);
                mp.prepare();

                media_duration = mp.getDuration();

                Log.d("Media", "Duration::" + media_duration);
            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
            }
            Log.d("Audio size", "Audio ::" + size);

            MessageGetSet msggetset = new MessageGetSet();
            String packet_id = "" + new Date().getTime();

            msggetset.setData("");
            msggetset.setKey_from_me(0);
            msggetset.setKey_id("" + packet_id);
            msggetset.setKey_remote_jid(jid);
            msggetset.setNeeds_push(1);
            msggetset.setSend_timestamp(new Date().getTime());
            msggetset.setStatus(3);
            msggetset.setTimestamp(new Date().getTime());
            msggetset.setThumb_image(null);
            msggetset.setMedia_name(File_name);
            msggetset.setMedia_mime_type(new Utils().getMimeType(file_path));
            msggetset.setMedia_wa_type("" + 3);
            msggetset.setRemote_resource(KachingMeApplication.getUserID()
                    + KachingMeApplication.getHost());
            msggetset.setMedia_url(null);
            msggetset.setRow_data(null);
            msggetset.setMedia_duration(media_duration);
            msggetset.setMedia_size(size);

            msggetset.setIs_sec_chat(1);
            dbAdapter.setInsertMessages(msggetset);

            // Get size in bytes to update media network usage
            try {
                long bite = (long) f.length();
                updateMediaNetwork(bite);

            } catch (Exception e) {

            }
		/*
		 * try { Utils.Copyfile(f, new
		 * File(Constant.local_audio_dir+File_name)); } catch (IOException e) {
		 * // TODO Auto-generated catch block e.printStackTrace(); }
		 */

            int msg_id = dbAdapter.getLastMsgid(jid);

            if (dbAdapter.isExistinChatList(jid)) {
                dbAdapter.setUpdateChat_lits(jid, msg_id);
            } else {
                dbAdapter.setInsertChat_list(jid, msg_id);
            }

            msg_list.add(msggetset);

            mParentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    intializationElements(1);
                    rightAudioChat();
                    setRightAudio_Old();
                    k = k + 1;

                    mRightTipLayout.requestFocus();
                    edt_msg.setText("");
                    edt_msg.requestFocus();
                }
            });

            Intent login_broadcast = new Intent("chat");
            login_broadcast.putExtra("jid", "" + jid);
            sendBroadcast(login_broadcast);
        } catch (Exception e) {

        }

    }

    // ///////////////////********* Forword
    // IMAGE*************//////////////////////
    private void sendImage(MessageGetSet msggetset) {

        try {
            String packet_id = "" + new Date().getTime();

            msggetset.setKey_id("" + packet_id);
            msggetset.setKey_remote_jid(jid);
            msggetset.setKey_from_me(0);
            msggetset.setNeeds_push(1);
            msggetset.setSend_timestamp(new Date().getTime());
            msggetset.setStatus(3);
            msggetset.setTimestamp(new Date().getTime());
            msggetset.setMedia_wa_type("" + 1);
            msggetset.setRemote_resource(KachingMeApplication.getUserID()
                    + KachingMeApplication.getHost());
            msggetset.setIs_sec_chat(1);
            dbAdapter.setInsertMessages(msggetset);

            // edt_msg.setText("");


            int msg_id = dbAdapter.getLastMsgid(jid);

            if (dbAdapter.isExistinChatList(jid)) {
                dbAdapter.setUpdateChat_lits(jid, msg_id);
            } else {
                dbAdapter.setInsertChat_list(jid, msg_id);
            }

            //
            Intent login_broadcast = new Intent("chat");
            login_broadcast.putExtra("jid", "" + jid);
            sendBroadcast(login_broadcast);

        } catch (Exception e) {

        }
    }

    // ////////////////////***** Forword
    // Video**********//////////////////////////////////////////////////////////////
    public void Forword_Video(MessageGetSet msggetset) {

        try {
            String packet_id = "" + new Date().getTime();

            msggetset.setKey_id("" + packet_id);
            msggetset.setKey_remote_jid(jid);
            msggetset.setKey_from_me(0);
            msggetset.setNeeds_push(1);
            msggetset.setSend_timestamp(new Date().getTime());
            msggetset.setStatus(3);
            msggetset.setTimestamp(new Date().getTime());
            msggetset.setMedia_wa_type("2");
            msggetset.setRemote_resource(KachingMeApplication.getUserID()
                    + KachingMeApplication.getHost());
            msggetset.setIs_sec_chat(1);
            dbAdapter.setInsertMessages(msggetset);

            // edt_msg.setText("");


            int msg_id = dbAdapter.getLastMsgid(jid);

            if (dbAdapter.isExistinChatList(jid)) {
                dbAdapter.setUpdateChat_lits(jid, msg_id);
            } else {
                dbAdapter.setInsertChat_list(jid, msg_id);
            }


            Intent login_broadcast = new Intent("chat");
            login_broadcast.putExtra("jid", "" + jid);
            sendBroadcast(login_broadcast);
        } catch (Exception e) {

        }

    }

    // ////////////////////***** Forword
    // Audio**********//////////////////////////////////////////////////////////////
    public void Forword_Audio(MessageGetSet msggetset) {

        try {
            String packet_id = "" + new Date().getTime();

            msggetset.setKey_id("" + packet_id);
            msggetset.setKey_remote_jid(jid);
            msggetset.setKey_from_me(0);
            msggetset.setNeeds_push(1);
            msggetset.setSend_timestamp(new Date().getTime());
            msggetset.setStatus(3);
            msggetset.setTimestamp(new Date().getTime());
            msggetset.setMedia_wa_type("3");
            msggetset.setRemote_resource(KachingMeApplication.getUserID()
                    + KachingMeApplication.getHost());
            msggetset.setIs_sec_chat(1);
            dbAdapter.setInsertMessages(msggetset);


            int msg_id = dbAdapter.getLastMsgid(jid);

            if (dbAdapter.isExistinChatList(jid)) {
                dbAdapter.setUpdateChat_lits(jid, msg_id);
            } else {
                dbAdapter.setInsertChat_list(jid, msg_id);
            }


            Intent login_broadcast = new Intent("chat");
            login_broadcast.putExtra("jid", "" + jid);
            sendBroadcast(login_broadcast);
        } catch (Exception e) {

        }

    }

    // /////////////********SEND
    // FILE*************************//////////////////////////////////////////////////////////////
    public void SEND_FILE(String strURL) {

        try {
            String time = "" + System.currentTimeMillis();
            String File_name = time + ".jpg";
            String file_path = "";
            int media_duration = 0;
            int size = 0;
            file_path = strURL;
            File f = new File(strURL);
            File_name = f.getName();
            size = (int) f.length();

            Log.d("Chat", "File Size ::" + size);

            MessageGetSet msggetset = new MessageGetSet();
            String packet_id = "" + new Date().getTime();

            msggetset.setData("");
            msggetset.setKey_from_me(0);
            msggetset.setKey_id("" + packet_id);
            msggetset.setKey_remote_jid(jid);
            msggetset.setNeeds_push(1);
            msggetset.setSend_timestamp(new Date().getTime());
            msggetset.setStatus(3);
            msggetset.setTimestamp(new Date().getTime());
            msggetset.setThumb_image(null);
            msggetset.setMedia_name(File_name);
            Log.d("Chat", "MIME type::" + new Utils().getMimeType(file_path));
            msggetset.setMedia_mime_type(new Utils().getMimeType(file_path));
            msggetset.setMedia_wa_type("" + 6);

            msggetset.setMedia_url(null);
            msggetset.setRow_data(null);
            msggetset.setMedia_duration(media_duration);
            msggetset.setMedia_size(size);
            msggetset.setRemote_resource(KachingMeApplication.getUserID()
                    + KachingMeApplication.getHost());
            msggetset.setIs_sec_chat(1);
            dbAdapter.setInsertMessages(msggetset);

            // Get size in double to update media network usage
            try {

                long bite = (long) f.length();
                updateMediaNetwork(bite);

            } catch (Exception e) {

            }

		/*
		 * try { Utils.Copyfile(f, new
		 * File(Constant.local_files_dir+File_name)); } catch (IOException e) {
		 * // TODO Auto-generated catch block e.printStackTrace(); }
		 */


            int msg_id = dbAdapter.getLastMsgid(jid);

            if (dbAdapter.isExistinChatList(jid)) {
                dbAdapter.setUpdateChat_lits(jid, msg_id);
            } else {
                dbAdapter.setInsertChat_list(jid, msg_id);
            }


            Intent login_broadcast = new Intent("chat");
            login_broadcast.putExtra("jid", "" + jid);
            sendBroadcast(login_broadcast);
        } catch (Exception e) {

        }

    }

    // /////////////********FORWORD
    // FILE*************************//////////////////////////////////////////////////////////////
    public void FORWORD_FILE(MessageGetSet msggetset) {

        try {
            String packet_id = "" + new Date().getTime();

            msggetset.setData("");
            msggetset.setKey_from_me(0);
            msggetset.setKey_id("" + packet_id);
            msggetset.setKey_remote_jid(jid);
            msggetset.setNeeds_push(1);
            msggetset.setSend_timestamp(new Date().getTime());
            msggetset.setStatus(3);
            msggetset.setTimestamp(new Date().getTime());
            msggetset.setThumb_image(null);
            msggetset.setRemote_resource(KachingMeApplication.getUserID()
                    + KachingMeApplication.getHost());
            msggetset.setIs_sec_chat(1);
            dbAdapter.setInsertMessages(msggetset);


            int msg_id = dbAdapter.getLastMsgid(jid);

            if (dbAdapter.isExistinChatList(jid)) {
                dbAdapter.setUpdateChat_lits(jid, msg_id);
            } else {
                dbAdapter.setInsertChat_list(jid, msg_id);
            }


            Intent login_broadcast = new Intent("chat");
            login_broadcast.putExtra("jid", "" + jid);
            sendBroadcast(login_broadcast);
        } catch (Exception e) {

        }

    }

    private boolean CheckMessageTypeValid(int position) {
        if (cursor.moveToPosition(position)) {

            String strType = cursor.getString(10);
            if (strType.matches("0|1|2|3|4|5|6"))
                return true;

        }

        return false;

    }

//    private void onListItemSelect(int position) {
//        if (CheckMessageTypeValid(position)) {
//            Constant.adapter_muc_test.toggleSelection(position);
//            boolean hasCheckedItems = Constant.adapter_muc_test.getSelectedCount() > 0;
//
//            if (hasCheckedItems && mActionMode == null)
//                // there are some selected items, start the actionMode
//                mActionMode = startSupportActionMode(new ActionModeCallback());
//            else if (!hasCheckedItems && mActionMode != null)
//                // there no selected items, finish the actionMode
//                mActionMode.finish();
//
//            if (mActionMode != null)
//                mActionMode.setTitle(String.valueOf(Constant.adapter_muc_test
//                        .getSelectedCount()) + " selected");
//        }
//    }

    public void lock_input(final String name, final String jid, final Boolean is_lock) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        String positive_label;
        if (!is_lock) {
            alert.setTitle(res.getString(R.string.lock_chat));
            alert.setMessage(res.getString(R.string.lock) + " " + name);
            positive_label = res.getString(R.string.lock);
        } else {
            alert.setTitle(res.getString(R.string.unlock_chat));
            alert.setMessage(res.getString(R.string.unlock) + " " + name);
            positive_label = res.getString(R.string.unlock);
        }

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_CLASS_NUMBER);
        input.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        int maxLength = 4;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        input.setFilters(FilterArray);

        alert.setView(input);
        alert.setPositiveButton(positive_label,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        if (!is_lock) {
                            // ed.putBoolean(jid + "_lock", true);
                            //
                            ContentValues cv = new ContentValues();
                            cv.put("jid", jid);
                            cv.put("password", value);
                            cv.put("jid_name", name);
                            cv.put("status", "lock");
                            insertToLock(cv);
                            Constant.printMsg("inserted lock details ::::::   "
                                    + jid + "  " + value);

//                            Intent i = new Intent(getApplicationContext(),
//                                    SliderTesting.class);
//                            startActivity(i);
                        } else {
                            String query = "select password from "
                                    + Dbhelper.TABLE_LOCK + " where jid = '"
                                    + jid + "'";
                            Constant.printMsg("bhaththam value" + value
                                    + "   " + status_lock);
                            lock_status(query);
                            if (value.equals(status_lock)) {
                                Constant.printMsg("delete :::::");
                                String delete_query1 = "delete from "
                                        + Dbhelper.TABLE_LOCK
                                        + " where jid = '" + jid + "'";
                                delete_query(delete_query1);
                                Constant.printMsg("inserted lock details ::::::   "
                                        + jid + "  " + value);

//                                Intent i = new Intent(getApplicationContext(),
//                                        SliderTesting.class);
//                                startActivity(i);
                            } else {
                                new AlertManager().showAlertDialog(
                                        getApplicationContext(),
                                        res.getString(R.string.you_are_entered_incorrect_pin),
                                        true);
                            }

                            // ed.remove(jid + "_lock");

                        }
                    }
                });

        alert.setNegativeButton(res.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

        alert.show();
    }

    /**
     * To get the User lock status
     */

    private String lock_status(String query) {
        // TODO Auto-generated method stub
        Cursor c = null;

        try {

            Constant.printMsg("query  " + query);

            c = db.open().getDatabaseObj().rawQuery(query, null);
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
            // c.close();
            // db.close();
        }
        return status_lock;

    }

    private void selectImage() {

        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.gallery_popup);
            dialog.setCancelable(true);

            TextView mGalleryText = (TextView) dialog.findViewById(R.id.gallery_text);
            TextView mPhotoText = (TextView) dialog.findViewById(R.id.photo_text);
            TextView mVideoText = (TextView) dialog
                    .findViewById(R.id.video_text);

            LinearLayout mGalleryLayout = (LinearLayout) dialog.findViewById(R.id.header_layout);
            LinearLayout mSelectionLayout = (LinearLayout) dialog.findViewById(R.id.selection_layout);


            LinearLayout.LayoutParams galleryLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            galleryLayoutParams.width = width * 60 / 100;
            galleryLayoutParams.height = height * 5 / 100;
            galleryLayoutParams.gravity = Gravity.CENTER;
            mGalleryLayout.setLayoutParams(galleryLayoutParams);


            LinearLayout.LayoutParams text_layout = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            text_layout.width = width * 60 / 100;
            text_layout.height = width * 21 / 100;
            text_layout.gravity = Gravity.CENTER;
            mSelectionLayout.setLayoutParams(text_layout);


            LinearLayout.LayoutParams textparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textparams.width = width * 60 / 100;
            textparams.leftMargin = width * 5 / 100;
            textparams.height = width * 10 / 100;
            textparams.gravity = Gravity.CENTER;
            mPhotoText.setLayoutParams(textparams);
            mVideoText.setLayoutParams(textparams);
            mGalleryText.setLayoutParams(textparams);
            mGalleryText.setGravity(Gravity.CENTER | Gravity.LEFT);
            mGalleryText.setLeft(width * 5 / 100);
            mVideoText.setGravity(Gravity.CENTER | Gravity.LEFT);
            mPhotoText.setGravity(Gravity.CENTER | Gravity.LEFT);
            Constant.typeFace(this, mGalleryText);
            Constant.typeFace(this, mVideoText);
            Constant.typeFace(this, mPhotoText);


            if (width >= 600) {

                mGalleryText.setTextSize(17);
                mPhotoText.setTextSize(17);
                mVideoText.setTextSize(17);

            } else if (width > 501 && width < 600) {

                mGalleryText.setTextSize(16);
                mPhotoText.setTextSize(16);
                mVideoText.setTextSize(16);

            } else if (width > 260 && width < 500) {

                mGalleryText.setTextSize(15);
                mPhotoText.setTextSize(15);
                mVideoText.setTextSize(15);

            } else if (width <= 260) {

                mGalleryText.setTextSize(14);
                mPhotoText.setTextSize(14);
                mVideoText.setTextSize(14);

            }


            mPhotoText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    image_picker(11);
                }
            });
            mVideoText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    image_picker(22);
                }
            });
            dialog.show();
        } catch (Exception e) {

        }


    }

    public void Show_Self_desc_time(int selected) {
        final CharSequence[] items = getResources().getStringArray(
                R.array.self_des_time);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.self_des_time));
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Constant.mself_destruct_time == true) {
                    Constant.mself_destruct_msg = edt_msg.getText().toString()
                            .trim();
                    Constant.mself_jid = jid;
                    Constant.mself_destruct_time = false;
                    System.out
                            .println("called self destruct send condition inside the btn click");

                    ArrayList<GroupParticipantGetSet> list = new ArrayList<GroupParticipantGetSet>();
                    Constant.printMsg("group jid ::::::::   " + jid);

                    list = dbAdapter.getGroupMembers(jid);
                    String list_jid = null;

                    for (int i = 0; i < list.size(); i++) {

                        if (list_jid == null) {
                            list_jid = list.get(i).getJid().split("@")[0];
                        } else {
                            list_jid += "-"
                                    + list.get(i).getJid().split("@")[0];
                        }
                        Constant.printMsg("group member ::::>>>>>>   "
                                + list_jid);
                    }

                    // for (GroupParticipantGetSet groupParticipantGetSet :
                    // list) {
                    // contactsGetSet contact = dbAdapter
                    // .getContact_grp(groupParticipantGetSet.getJid());
                    //
                    // if (contact == null) {
                    // contact = new contactsGetSet();
                    // if (groupParticipantGetSet.getJid().equals(my_jid)) {
                    // contact.setDisplay_name("You");
                    // } else {
                    // contact.setDisplay_name(groupParticipantGetSet
                    // .getJid().split("@")[0]);
                    // }
                    //
                    // contact.setJid(groupParticipantGetSet.getJid());
                    //
                    // }
                    //
                    // if (groupParticipantGetSet.getJid().equals(my_jid)) {
                    // contact.setDisplay_name("You");
                    // contact.setPhoto_ts(KachingMeApplication.getAvatar());
                    // contact.setJid(my_jid);
                    // }
                    //
                    // Log.d(TAG, "Member JID::" + contact.getJid());
                    // Log.d(TAG,
                    // "Member 2 JID::"
                    // + groupParticipantGetSet.getJid());
                    //
                    // if (Is_Admin) {
                    // contact_list.add(contact);
                    // } else {
                    // Log.d("", "Contact jid::" + contact.getJid()
                    // + " group_admin::" + group_admin);
                    // if (int_group_type > 0) {
                    // if (contact.getJid().equals(group_admin)) {
                    // contact_list.add(contact);
                    // }
                    // } else {
                    // contact_list.add(contact);
                    // }
                    // }
                    //
                    // }

                    if (Constant.mself_destruct_msg.toString().length() != 0) {

                        int point = sharedPrefs.getInt("destpoint", 0);

                        Constant.totaldest = point;

                        Constant.totaldest = Count + Constant.totaldest;

                        Editor e2 = sharedPrefs.edit();
                        e2.putInt("destpoint", Constant.totaldest);
                        e2.commit();

                        Constant.bux = sharedPrefs.getLong("buxvalue", 0);

                        Long buxval1 = Constant.bux + Constant.desTpoint;
                        Constant.bux = buxval1;
                        Constant.printMsg("dest buxxxx:::::" + buxval1 + "   " + Constant.totaldest + "   " + Constant.bux);

                        Editor e3 = sharedPrefs.edit();
                        e3.putLong("buxvalue", buxval1);
                        e3.commit();


                        sendMessage("<s>"
                                + Constant.mselected_self_destruct_time + "-"
                                + Constant.mself_destruct_msg + "-" + list_jid);
                        Constant.printMsg("msg:::"
                                + Constant.mself_destruct_msg + "time::::"
                                + Constant.mselected_self_destruct_time);
                    }
                }

            }
        });

        builder.setSingleChoiceItems(items, selected,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        if(mPopup){
                            downArrowClickAction();
                        }
                        System.out
                                .println("selected time postion:::::" + which);
                        Constant.mselected_self_destruct_time = self_desc_time[which];
                        Constant.printMsg("selected time:::::"
                                + Constant.mselected_self_destruct_time);

                        Constant.mself_destruct_time = true;

                        // selected_self_desc_time = self_desc_time[which];
                        // selected_self_desc_index = which;
                        // SharedPreferences sp = getSharedPreferences(
                        // KachingMeApplication.getPereference_label(),
                        // Activity.MODE_PRIVATE);
                        // SharedPreferences.Editor editor = sp.edit();
                        // editor.putInt(jid + "_self_desc_time",
                        // selected_self_desc_time);
                        // editor.commit();

                    }
                });
        builder.show();

    }

    public void copyDirectoryOneLocationToAnotherLocation(File sourceLocation,
                                                          File targetLocation) throws IOException {

        try {
            Constant.printMsg("outp::st" + sourceLocation + " " + targetLocation
                    + " " + outputFile);

            if (sourceLocation.isDirectory()) {
                if (!targetLocation.exists()) {
                    targetLocation.mkdir();
                }

                String[] children = sourceLocation.list();
                for (int i = 0; i < sourceLocation.listFiles().length; i++) {

                    copyDirectoryOneLocationToAnotherLocation(new File(
                            sourceLocation, children[i]), new File(targetLocation,
                            children[i]));
                }
            } else {

                InputStream in = new FileInputStream(sourceLocation);

                OutputStream out = new FileOutputStream(targetLocation);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }

            Constant.printMsg("outp::" + outputFile);

            upload_audio_File(outputFile);
        } catch (IOException e) {

        }

    }

    public void update_sent_count(int code) {
        try {
            Constant.printMsg("sent_msg_count msg update_received_count added is::"
                    + code);
            Editor e = pref.edit();
            e.putInt("sent_msg_count", code);
            Constant.printMsg("sent_msg_count in muc_chat  :::" + code);
            e.commit();
        } catch (Exception e1) {

        }
    }

    // Updating the Shared Preference with Media Sent Network Usage....
    public void updateMediaNetwork(long upValue) {
        try {
            long val = 0;
            HashMap<String, String> user = mNewtSharPref.getMedia_SentDetails();

            String value = user.get(NetworkSharedPreference.KEY_MEDIA_GET_SX);
            if (value != null) {

                val = Long.parseLong(value);
                Constant.printMsg("session val:" + val);
            }
            long update = upValue + val;

            Constant.printMsg("session :" + update);

            String data = String.valueOf(update);

            Constant.printMsg("session STR:" + data);

            mNewtSharPref.setMediaData_Sent(data);

            updateMesageCountForMedia();
        } catch (NumberFormatException e) {

        }

    }

    // Update message count for media Network Usage
    public void updateMesageCountForMedia() {

        try {
            int sent_msg_count = pref.getInt("sent_msg_count", 0);

            sent_msg_count = sent_msg_count + 1;

            update_sent_count(sent_msg_count);
        } catch (Exception e) {

        }
    }

    // Updating the Shared Preference with Messaging Sent Network Usage....
    public void updateNetwork_Message_Sent(long bite) {
        try {
            long val = 0;
            // get user data from session
            HashMap<String, String> user = mNewtSharPref.getMessage_SentDetails();

            // name
            String value = user.get(NetworkSharedPreference.KEY_MESSAGE_GET_SX);
            if (value != null) {

                val = Long.parseLong(value);
            }
            Long update = bite + val;

            String data = String.valueOf(update);

            mNewtSharPref.setMessageData_Sent(data);
        } catch (NumberFormatException e) {

        }
    }

    public void sendMUCTypingStatus(final String statusTyping) {

        final String frm = dbAdapter.getLogin().get(0).getUserName()
                + KachingMeApplication.getHost();
        Thread typing = new Thread(new Runnable() {

            @Override
            public void run() {
                mIsThreadRunning = true;
                // check if you are connected to group
                if (muc != null) {
                    try {
                        // create packet
                        Message statusPacket = new Message();
                        // set body to null
                        statusPacket.setBody(statusTyping);
                        // set packet type to group chat
                        statusPacket.setType(Type.groupchat);
                        // set subject to null
                        statusPacket.setSubject(null);
                        // set to the group name
                        // statusPacket.setTo(muc.getRoom());
                        // set from my current jis example : me@domain.com
                        statusPacket.setFrom(frm);
                        // get the chat state extension and pass our state
                        JivePropertiesManager
                                .addProperty(statusPacket, "ID", 5);

                        JivePropertiesManager.addProperty(statusPacket,
                                "media_type", "0");
                        ChatStateExtension extension = new ChatStateExtension(
                                ChatState.composing);
                        // add the extention to our packet
                        statusPacket.addExtension(extension);

                        MessageEventManager.addNotificationsRequests(
                                statusPacket, true, true, true, true);
                        // get the connection and send the packet

                        Constant.printMsg("jasdasjdas"+statusPacket.toXML());
                        muc.sendMessage(statusPacket);

                        try {
                            Thread.sleep(2000);
                            IS_TYPED = true;
                            mIsThreadRunning = false;
                            Constant.printMsg("TWO MINS");

                            // istxtListner = false;
                            // txt_sub_title.setText("");

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        Constant.printMsg("MUC Typing");
                    } catch (NotConnectedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        if (mIsThreadRunning == false) {
            typing.start();
        }

    }

    Runnable AsyncConnection = new Runnable() {

        @Override
        public void run() {

            if (!IS_TYPED) {
                sendMUCTypingStatus(Constant.TYPING_STATUS_GROUP);
            }

            if (!IS_Recorded) {
                sendMUCTypingStatus(Constant.TYPING_STATUS_RECORDING);
            }
            handler.postDelayed(AsyncConnection, 500);
        }

    };

    public void pauseSubTitle() {
        try {

            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            public void run() {

                                txt_sub_title.setText("");
                                txt_sub_title.setVisibility(View.GONE);
                            }

                        });
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }).start();

        } catch (Exception exp) {

        }
    }

    public void clearGroupHistory(String bookmarkedConference) {

        RoomInfo room_info = null;
        try {

            room_info = TempConnectionService.MUC_MANAGER
                    .getRoomInfo(bookmarkedConference);
        } catch (Exception e) {
            // ACRA.getErrorReporter().handleException(e);
        }
        if (room_info != null) {
            try {

//                MUCInitialPresence.History h=new MUCInitialPresence.History();
//                h.setMaxChars(0);
//                h.setMaxStanzas(0);
//
//                MUCInitialPresence mucInitialPresence = new MUCInitialPresence();
//                mucInitialPresence.setHistory(h);
//
//                Presence joinPresence = new Presence(Presence.Type.available);
//                joinPresence.setTo(jid);
//                joinPresence.addExtension(mucInitialPresence);
//                TempConnectionService.connection.sendStanza(joinPresence);

//                BookmarkManager   bm1 = BookmarkManager
//                        .getBookmarkManager(TempConnectionService.connection);
//
//                bm1.addBookmarkedConference(jid, jid, true,
//                        Utils.getBookmarkTime(), "");

                muc = MultiUserChatManager.getInstanceFor(connection)
                        .getMultiUserChat(bookmarkedConference);
                // muc = new MultiUserChat(connection,
                // bookmarkedConference.getJid());

                muc.addMessageListener(TempConnectionService.muc_messageListener);
                muc.addSubjectUpdatedListener(new MUC_SubjectChangeListener(
                        this));
                DiscussionHistory history = new DiscussionHistory();
                history.setSince(new Date());
                history.setMaxStanzas(0);
                history.setMaxChars(0);
//                if (!muc.isJoined()) {
                muc.join(dbAdapter.getLogin().get(0).getUserName()
                                + KachingMeApplication.getHost(), null, history,
                        6000000L);
////                }



            } catch (Exception e) {
                // TODO: handle exception
                // //ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
            }
        }
    }

    public void mIntVariable() {

        try {
            // Chat Hedder Views initalization
            mChatHedderBackImg = (ImageView) findViewById(R.id.chat_hedder_back_img);
            mChatHedderProfileImg = (ImageView) findViewById(R.id.chat_hedder_profile_img);
            mChatHedderAttachmentImg = (ImageView) findViewById(R.id.chat_hedder_attachment_img);
            mChatHedderMenuImg = (ImageView) findViewById(R.id.chat_hedder_menu_img);
            mToolTipImg = (ImageView) findViewById(R.id.tool_tip_img);
            mChatHedderCopyImg = (ImageView) findViewById(R.id.chat_hedder_copy_img);

            mChatHedderUserTxt = (TextView) findViewById(R.id.chat_hedder_user_txt);
            mChatHedderUserStatusTxt = (TextView) findViewById(R.id.chat_hedder_user_status_txt);
            mChatHedderUserTxt.setTextColor(Color.parseColor("#ffffff"));
            mChatHedderUserStatusTxt.setTextColor(Color.parseColor("#ffffff"));
            mChatHedderUserTxt.setTypeface(null, Typeface.BOLD);

            mchatHeadBackLayout = (LinearLayout) findViewById(R.id.mchatlayoutHeadBack);
            mChatHedderLayout = (LinearLayout) findViewById(R.id.chat_hedder_layout);
            mChatHedderTextLayout = (LinearLayout) findViewById(R.id.chat_hedder_text_layout);
            mChatHedderCopyLayout= (LinearLayout) findViewById(R.id.chat_hedder_copy_layout);
            mChatHedderAttachmentLayout = (LinearLayout) findViewById(R.id.chat_hedder_attachment_layout);
            mChatHedderMenuLayout = (LinearLayout) findViewById(R.id.chat_hedder_menu_layout);
            mChatHedderCopyLayout.setVisibility(View.GONE);

            // Chat Footer Views initalization
            mChatFooterLayout = (LinearLayout) findViewById(R.id.ll_chat);
            mDownArrowLayout = (LinearLayout) findViewById(R.id.btn_down_arrow_grp_layout);
            mChatFooterEdittextLayout = (LinearLayout) findViewById(R.id.chat_footer_edittext_layout);

            mChatFooterSlideMenuImg = (ImageView) findViewById(R.id.btn_down_arrow_grp);
            mChatFooterEmojiconsImg = (ImageView) findViewById(R.id.btn_emo);

            mChatFooterEdittext = (EmojiconEditText) findViewById(R.id.edt_messagegrp);
            mChatFooterEdittext.requestFocus();

            mChatFooterSendBtn = (RounderImageView) findViewById(R.id.sendButton);

            mDynamicView = (LinearLayout) findViewById(R.id.mdynamicView);
            mScrollView= (ScrollView) findViewById(R.id.scroll_view);

            mContactLayout = (LinearLayout) findViewById(R.id.contact_layout);
            mContactLayout.setVisibility(View.GONE);

            mSliderMenuLayout = (LinearLayout) findViewById(R.id.slider_menu_layout);
            mSliderMenuLayout.setVisibility(View.GONE);
            
        } catch (Exception e) {

        }

    }


    public void mScreenArrangement() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        LinearLayout.LayoutParams hedderLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        hedderLayoutParams.width = width;
        hedderLayoutParams.height = height * 8 / 100;
        mChatHedderLayout.setLayoutParams(hedderLayoutParams);

        LinearLayout.LayoutParams headBackLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        headBackLayout.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        headBackLayout.height = LinearLayout.LayoutParams.MATCH_PARENT;
        headBackLayout.weight = 2;
        headBackLayout.gravity = Gravity.CENTER;
        mchatHeadBackLayout.setLayoutParams(headBackLayout);

        LinearLayout.LayoutParams hedderBackBtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        hedderBackBtnParams.width = width * 6 / 100;
        hedderBackBtnParams.height = width * 6 / 100;
        hedderBackBtnParams.leftMargin = width * 5 / 100;
        hedderBackBtnParams.gravity = Gravity.CENTER_VERTICAL;
        mChatHedderBackImg.setLayoutParams(hedderBackBtnParams);

        LinearLayout.LayoutParams hedderProfileImgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        hedderProfileImgParams.width = width * 12 / 100;
        hedderProfileImgParams.height = width * 12 / 100;
        hedderProfileImgParams.leftMargin = width * 5 / 100;
        hedderProfileImgParams.gravity = Gravity.CENTER_VERTICAL;
        mChatHedderProfileImg.setLayoutParams(hedderProfileImgParams);

        LinearLayout.LayoutParams hedderTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        hedderTextParams.width = width * 50 / 100;
        hedderTextParams.leftMargin = width * 3 / 100;
        hedderTextParams.gravity = Gravity.CENTER_VERTICAL;
        mChatHedderTextLayout.setLayoutParams(hedderTextParams);

        LinearLayout.LayoutParams hedderAttachmentLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        hedderAttachmentLayoutParams.width = width * 10 / 100;
        mChatHedderAttachmentLayout
                .setLayoutParams(hedderAttachmentLayoutParams);
        mChatHedderMenuLayout.setLayoutParams(hedderAttachmentLayoutParams);
        mChatHedderCopyLayout.setLayoutParams(hedderAttachmentLayoutParams);

        LinearLayout.LayoutParams hedderAttachmentParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        hedderAttachmentParams.width = width * 4 / 100;
        hedderAttachmentParams.height = width * 8 / 100;
        hedderAttachmentParams.gravity = Gravity.CENTER;
        mChatHedderAttachmentImg.setLayoutParams(hedderAttachmentParams);

        LinearLayout.LayoutParams hedderMenuParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        hedderMenuParams.width = (width * 2 / 100) - 2;
        hedderMenuParams.height = width * 7 / 100;
        hedderMenuParams.gravity = Gravity.CENTER;
        hedderMenuParams.rightMargin = width * 4 / 100;
        mChatHedderMenuImg.setLayoutParams(hedderMenuParams);

        LinearLayout.LayoutParams FooterLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        FooterLayoutParams.width = width;
        FooterLayoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_VERTICAL;
        mChatFooterLayout.setPadding(0, height * 2 / 100, 0, height * 2 / 100);
        mChatFooterLayout.setLayoutParams(FooterLayoutParams);

        LinearLayout.LayoutParams footerSlideMenuParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        footerSlideMenuParams.width = width * 7 / 100;
        footerSlideMenuParams.height = height * 2 / 100;
        footerSlideMenuParams.gravity = Gravity.CENTER;
        mChatFooterSlideMenuImg.setLayoutParams(footerSlideMenuParams);


        LinearLayout.LayoutParams footerDownArrowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        footerDownArrowParams.width = width * 13 / 100;
        footerDownArrowParams.gravity = Gravity.CENTER | Gravity.CENTER;
        mDownArrowLayout.setLayoutParams(footerDownArrowParams);

        LinearLayout.LayoutParams footerEdittextLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        footerEdittextLayoutParams.width = width * 72 / 100;
        footerEdittextLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        mChatFooterEdittextLayout.setLayoutParams(footerEdittextLayoutParams);

        LinearLayout.LayoutParams footerEdittextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        footerEdittextParams.width = width * 60 / 100;
        // footerEdittextParams.height = height * 12 / 100;
        mChatFooterEdittext.setLayoutParams(footerEdittextParams);

        LinearLayout.LayoutParams footerEmojiParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        footerEmojiParams.width = width * 6 / 100;
        footerEmojiParams.height = width * 6 / 100;
        footerEmojiParams.gravity = Gravity.CENTER_VERTICAL;
        mChatFooterEmojiconsImg.setLayoutParams(footerEmojiParams);

        LinearLayout.LayoutParams footerSendBtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        footerSendBtnParams.width = width * 11 / 100;
        footerSendBtnParams.height = width * 11 / 100;
        footerSendBtnParams.leftMargin = width * 2 / 100;
        footerSendBtnParams.gravity = Gravity.CENTER_VERTICAL;
        mChatFooterSendBtn.setLayoutParams(footerSendBtnParams);


        FrameLayout.LayoutParams mScrollViewParams = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mScrollViewParams.topMargin=height*8/100;
        mScrollViewParams.bottomMargin=height*11/100;
        mScrollView.setLayoutParams(mScrollViewParams);


        mChatFooterLayout.post(new Runnable() {
            @Override
            public void run() {
                //maybe also works height = ll.getLayoutParams().height;
                mTempLayoutHeight = mChatFooterLayout.getHeight();
                LinearLayout.LayoutParams listviewParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                listviewParams.width = width;
//                listviewParams.topMargin = height * 8 / 100;
                listviewParams.gravity = Gravity.BOTTOM|Gravity.BOTTOM;
//                listviewParams.bottomMargin = mChatFooterLayout.getHeight() + 2;
                listview.setLayoutParams(listviewParams);
                listview.scrollTo(0, listview.getHeight());
                listMeaning.setLayoutParams(listviewParams);


            }
        });

        if (width >= 600) {
            mChatHedderUserTxt.setTextSize(16);
            mChatHedderUserStatusTxt.setTextSize(14);

        } else if (width > 501 && width < 600) {
            mChatHedderUserTxt.setTextSize(14);
            mChatHedderUserStatusTxt.setTextSize(12);

        } else if (width > 260 && width < 500) {
            mChatHedderUserTxt.setTextSize(13);
            mChatHedderUserStatusTxt.setTextSize(11);

        } else if (width <= 260) {
            mChatHedderUserTxt.setTextSize(12);
            mChatHedderUserStatusTxt.setTextSize(11);

        }

    }

    /**
     * Down Arrow button clicked functionality
     */
    public void downArrowClickAction() {


        if (emoji_frag.getVisibility() == View.VISIBLE) {
            emoji_frag.setVisibility(View.GONE);
            btn_emo.setImageResource(R.drawable.emoji_btn_normal);

        }

        LinearLayout linearLayoutPopup = (LinearLayout) findViewById(R.id.pop_up);
        LinearLayout mdazzle = (LinearLayout) findViewById(R.id.dazzle_btn_lay);
        LinearLayout mbazzle = (LinearLayout) findViewById(R.id.bazzle_btn_lay);
        LinearLayout mkons = (LinearLayout) findViewById(R.id.kons_btn_lay);
        LinearLayout autodes = (LinearLayout) findViewById(R.id.auto_btn_lay);
        LinearLayout mnymn = (LinearLayout) findViewById(R.id.nyms_btn_lay);

        autodes.setVisibility(View.GONE);

        ImageView mdazzleImg = (ImageView) findViewById(R.id.dazzle_btn_img);
        ImageView mbazzleImg = (ImageView) findViewById(R.id.bazzle_btn_img);
        ImageView mkonsImg = (ImageView) findViewById(R.id.kons_btn_img);
        ImageView autodesImg = (ImageView) findViewById(R.id.auto_btn_img);
        ImageView mnymnImg = (ImageView) findViewById(R.id.nyms_btn_img);
        ImageView mDownTipImg = (ImageView) findViewById(R.id.downtip_img);

        TextView mdazzleTxt = (TextView) findViewById(R.id.dazzle_btn_txt);
        TextView mbazzleTxt = (TextView) findViewById(R.id.bazzle_btn_txt);
        TextView mkonsTxt = (TextView) findViewById(R.id.kons_btn_txt);
        TextView autodesTxt = (TextView) findViewById(R.id.auto_btn_txt);
        TextView mnymnTxt = (TextView) findViewById(R.id.nyms_btn_txt);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(
                displayMetrics);
        int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        FrameLayout.LayoutParams slideMenuLayoutParamsHedder = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        slideMenuLayoutParamsHedder.leftMargin = (width * 3 / 100);
        mSliderMenuLayout.setLayoutParams(slideMenuLayoutParamsHedder);

        LinearLayout.LayoutParams slideMenuLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        slideMenuLayoutParams.height = height * 10 / 100;
        slideMenuLayoutParams.topMargin = height * 5 / 100;

        linearLayoutPopup.setLayoutParams(slideMenuLayoutParams);

        LinearLayout.LayoutParams slideItemsLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        slideItemsLayoutParams.width = width * 16 / 100;
        slideItemsLayoutParams.height = height * 10 / 100;
        slideItemsLayoutParams.gravity = Gravity.CENTER;
        mdazzle.setLayoutParams(slideItemsLayoutParams);
        mbazzle.setLayoutParams(slideItemsLayoutParams);
        mkons.setLayoutParams(slideItemsLayoutParams);
        autodes.setLayoutParams(slideItemsLayoutParams);
        mnymn.setLayoutParams(slideItemsLayoutParams);

        LinearLayout.LayoutParams slideItemsImgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        slideItemsImgParams.width = width * 10 / 100;
        slideItemsImgParams.height = height * 5 / 100;
        slideItemsImgParams.bottomMargin = width * 1 / 100;
        slideItemsImgParams.gravity = Gravity.CENTER;
        mdazzleImg.setLayoutParams(slideItemsImgParams);

        LinearLayout.LayoutParams slideItemsImgParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        slideItemsImgParams1.width = width * 10 / 100;
        slideItemsImgParams1.height = height * 4 / 100;
        slideItemsImgParams1.bottomMargin = width * 1 / 100;
        slideItemsImgParams1.gravity = Gravity.CENTER;
        mbazzleImg.setLayoutParams(slideItemsImgParams1);
        mkonsImg.setLayoutParams(slideItemsImgParams1);
        autodesImg.setLayoutParams(slideItemsImgParams1);
        mnymnImg.setLayoutParams(slideItemsImgParams1);

        LinearLayout.LayoutParams slideItemsDownImgParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        slideItemsDownImgParams1.width = width * 6 / 100;
        slideItemsDownImgParams1.height = height * 2 / 100;
        slideItemsDownImgParams1.leftMargin = width * 1 / 100;
        mDownTipImg.setLayoutParams(slideItemsDownImgParams1);

        mnymn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mPopup){
                    downArrowClickAction();
                }
                Constant.mNynmFromSlider = false;
                Constant.mNynmFromGroup = true;
                Intent intent = new Intent(mParentActivity,
                        NynmActivity.class);
                startActivity(intent);

                // sendMessage(mParseString);
            }

        });

        autodes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                try {
                    if (edt_msg.getText().toString().length() != 0) {
                        if (KachingMeApplication.getIsNetAvailable()) {
                            Show_Self_desc_time(selected_self_desc_index);
                            Constant.mselfdestruct = true;

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Check Your Network Connection",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter some text",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }

            }

        });

        mdazzle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    // mDownArrow.setImageResource(R.drawable.rt_arrow);

                    if(mPopup){
                        downArrowClickAction();
                    }
                    Constant.zzleFromgroup = true;

                    Constant.mChatText="";

                    try {
                        String[] words = edt_msg.getText().toString().split("\\s+");
                        if(mMessegeList.size()>0) {
                            for (int i = 0; i < words.length; i++) {
                                for (int j = 0; j < mMessegeList.size(); j++) {

                                    try {

                                        if (mMessegeList.get(j).toString().substring(0,mMessegeList.get(j).toString().length()-1).equalsIgnoreCase(words[i].substring(0, words[i].length() - 1))) {

                                            Constant.mChatText += words[i].substring(0, words[i].length() - 1) + " ";

                                        } else {
                                            Constant.mChatText += words[i] + " ";
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }else{
                            Constant.mChatText = edt_msg.getText().toString();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(mParentActivity,
                            DazzPlainActivity.class);
                    startActivity(intent);
//                }

                    mPopup = false;
                } catch (Exception e) {

                }

            }
        });
        mbazzle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    
                    Constant.mKrokFromGroup = true;
                    Constant.mKroKFromSlider = false;
                    // mDownArrow.setImageResource(R.drawable.rt_arrow);

                    if(mPopup){
                        downArrowClickAction();
                    }

                    Intent intentka = new Intent(mParentActivity,
                            KaraokeListActivity.class);
                    startActivity(intentka);

                    //mPopup = false;
                } catch (Exception e) {

                }

            }
        });

        mkons.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    
                    // mDownArrow.setImageResource(R.drawable.rt_arrow);
                    if(mPopup){
                        downArrowClickAction();
                    }

                    Intent intent = new Intent(mParentActivity,
                            KonsHomeScreen.class);
                    startActivity(intent);

                  //  mPopup = false;
                } catch (Exception e) {

                }

            }
        });

        if (mPopup == false) {
            Constant.printMsg("trueeee::::>>>>>>>");
            mPopup = true;

            // mDownArrow.setImageResource(R.drawable.lt_arrow);

            Constant.printMsg("rotate:1" + currentRotation);

            RotateAnimation anim = new RotateAnimation(currentRotation,
                    currentRotation + 180, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            currentRotation = (currentRotation + 180) % 360;

            Constant.printMsg("rotate:2" + currentRotation);

            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(600);
            anim.setFillEnabled(true);
            anim.setFillAfter(true);
            mDownArrow.startAnimation(anim);

            TranslateAnimation anim1 = new TranslateAnimation(-(width * 65 / 100), 0, 0, 0);
            anim1.setDuration(500); // 1000 ms = 1second

            anim1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation arg0) {

                    mSliderMenuLayout.layout(mSliderMenuLayout.getLeft(),
                            mSliderMenuLayout.getTop(), mSliderMenuLayout.getRight(),
                            mSliderMenuLayout.getBottom());
                    mSliderMenuLayout.setVisibility(View.VISIBLE);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub
                    mSliderMenuLayout.setVisibility(View.VISIBLE);


                }

            });

            mSliderMenuLayout.startAnimation(anim1);
            anim1.setFillAfter(true);

//                    popupWindow.showAtLocation(mDownArrow, Gravity.TOP,
//                    10,
//                    24);
        } else {

            mPopup = false;
            Constant.printMsg("falseeee::::>>>>>>>");

          

            Constant.printMsg("rotate:3" + currentRotation);

            RotateAnimation anim = new RotateAnimation(currentRotation,
                    currentRotation - 180, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            currentRotation = (currentRotation - 180) % 360;
            Constant.printMsg("rotate:4" + currentRotation);
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(600);
            anim.setFillEnabled(true);

            anim.setFillAfter(true);
            mDownArrow.startAnimation(anim);

            TranslateAnimation anims = new TranslateAnimation(0, -(width * 65 / 100), 0, 0);
            anims.setDuration(500); // 1000 ms = 1second

            anims.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation arg0) {
                    // Functionality here

                    mSliderMenuLayout.layout(mSliderMenuLayout.getLeft() - (width * 65 / 100),
                            mSliderMenuLayout.getTop(), mSliderMenuLayout.getRight(),
                            mSliderMenuLayout.getBottom());

                    mSliderMenuLayout.setVisibility(View.GONE);


                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

//                        mSlider_Img
//                                .setBackgroundResource(R.drawable.btn_mediatype_reverse);

                }

            });

            mSliderMenuLayout.startAnimation(anims);
            anims.setFillAfter(true);

            // mDownArrow.setImageResource(R.drawable.rt_arrow);
        }


    }

    /**
     * Attachment Icon click listener...
     */
    public void attachmentOptionMenuClickListener() {
        //Imageview click
        mChatMenuGalleryImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                selectImage();
            }
        });
        mChatMenuShootImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                imageCapture();
            }
        });
        mChatMenuVideoImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                startVideoRecording();
            }
        });
        mChatMenuLocationImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                locationShare();
            }
        });
        mChatMenuContactImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                Intent intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 55);
                ;
            }
        });
        mChatMenuAudioImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                Constant.mFromGroupAudio = true;
                startActivity(new Intent(mParentActivity, SongList.class));
            }
        });


        // Layout click
        mChatMenuGalleryLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                selectImage();
            }
        });
        mChatMenuShootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                imageCapture();
            }
        });
        mChatmenuVideoLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                startVideoRecording();
            }
        });
        mChatMenuLocationLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                locationShare();
            }
        });
        mChatMenuContactLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                Intent intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 55);
                ;
            }
        });
        mChatMenuAudioLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                Constant.mFromGroupAudio = true;
                startActivity(new Intent(mParentActivity, SongList.class));
            }
        });
    }

    /**
     * Chat Menu items are clicked
     */
    public void chatOptionOnClickListeners() {

        // Button click
        mChatOptionMenuViewcontactTxt.setText("Group info");
        mChatOptionMenuViewcontactImg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                viewUserProfile();
            }
        });
        mChatOptionMenuBlockImg.setVisibility(View.GONE);
        mChatOptionMenuCallImg.setVisibility(View.GONE);
        mChatOptionMenuLockchatImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                lockChat();
            }
        });
        mChatOptionMenuClearchatImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                clearChatData();
            }
        });


        //Layout click
        mChatOptionMenuViewcontactLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                viewUserProfile();
            }
        });
        mChatOptionMenuBlockLayout.setVisibility(View.GONE);
        mChatOptionMenuCallLayout.setVisibility(View.GONE);
        mChatOptionMenuLockchatLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                lockChat();
            }
        });
        mChatOptionMenuClearchatLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                clearChatData();
            }
        });
    }

    /**
     * Clear chat messages data
     */
    public void clearChatData() {
        try {
            AlertDialog.Builder b = new AlertDialog.Builder(mParentActivity);
            b.setMessage("Are you sure you want to clear this chat ?")
                    .setCancelable(false);
            b.setPositiveButton(getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });
            b.setNegativeButton(getResources().getString(R.string.Ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            dbAdapter.setGroupDeleteMessages(jid);
                            int msg_id = dbAdapter.getLastMsgid(jid);
                            dbAdapter.setUpdateChat_lits(jid, msg_id);
                            mDynamicView=(LinearLayout) findViewById(R.id.mdynamicView);
//                            clearGroupHistory(jid);
                            clearDataList();
                            k = 0;
                        }
                    });

            AlertDialog alert = b.create();
            alert.show();
        } catch (Resources.NotFoundException e) {

        }
    }

    public void clearDataList() {
        String Bookmarked_time = Utils.getBookmarkTime();
        Editor editor1 = sp.edit();
        editor1.putString(Constant.LAST_REFRESH_TIME + "_"
                + jid, Bookmarked_time);
        editor1.commit();

        BookmarkManager bm1;
        try {
            bm1 = BookmarkManager
                    .getBookmarkManager(TempConnectionService.connection);

            bm1.addBookmarkedConference(jid, jid, true,
                    Bookmarked_time, "");


        } catch (Exception e1) {
            // TODO Auto-generated catch block
            // ACRA.getErrorReporter().handleException(e1);
            e1.printStackTrace();
        }

        msg_list.clear();
        ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
    }

    /**
     * Lock the chat room
     */
    public void lockChat() {
//      Log.d(TAG, "Menu Title::" + item.getTitle() + " Lock resource::"
//                + res.getString(R.string.lock_chat).toString());

        try {
            String query = "select status from " + Dbhelper.TABLE_LOCK
                    + " where jid = '" + jid + "'";
            lock_status(query);

            if (status_lock.equalsIgnoreCase("lock")) {
                lock_input(txt_title.getText().toString(), jid, true);
            } else {

                lock_input(txt_title.getText().toString(), jid, false);
            }
        } catch (Exception e) {

        }
    }

    /**
     * Calling the user from chat
     */
    public void callUser() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri
                .parse("tel:+" + jid.toString().split("@")[0]));
        startActivity(callIntent);
    }

    /**
     * To see the User Profile
     */
    public void viewUserProfile() {

        Intent intent = new Intent(this, MUC_Info.class);
        intent.putExtra("jid", jid);
        intent.putExtra("subject", subject);
        intent.putExtra("avatar", avatar);
        startActivity(intent);
    }

    /**
     * Video recording start
     */
    public void startVideoRecording() {
        // image_picker(2);
        try {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            fileUri = Utils.getOutputMediaFileUri(2); // create a file to save
            // the video
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image
            // file name
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.8); // set the
            // video
            // image
            // quality
            // to high
            intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 26214400);
            // start the Video Capture Intent
            startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        } catch (Exception e) {

        }
    }

    /**
     * Capture Image
     */
    public void imageCapture() {
        // image_picker(1);
        // create Intent to take a picture and return control to the calling
        // application
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = Utils.getOutputMediaFileUri(1);
            // create a file to save the image
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image
            // file name

            // start the image capture Intent
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } catch (Exception e) {

        }
    }

    /**
     * Share location
     */
    public void locationShare() {
        Intent intent = new Intent(this, LocationShare.class);
        startActivityForResult(intent, 44);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            topMenuHideFunction();
            Constant.printMsg("ontouch");

        } catch (Exception e) {

        }

        return super.onTouchEvent(event);
    }

    /**
     * Attachment Icon Menu pop initialization....
     */
    public void mAttachmentIconMenuPopup() {


        try {
            mChatMenuHedderLayout = (LinearLayout) findViewById(R.id.chat_menu__hedder_layout);
            mChatMenuLayout = (LinearLayout) findViewById(R.id.chat_menu_layout);
            mChatMenuGalleryLayout = (LinearLayout) findViewById(R.id.chat_menu_gallery_layout);
            mChatMenuShootLayout = (LinearLayout) findViewById(R.id.chat_menu_shoot_layout);
            mChatmenuVideoLayout = (LinearLayout) findViewById(R.id.chat_menu_video_layout);
            mChatMenuLocationLayout = (LinearLayout) findViewById(R.id.chat_menu_location_layout);
            mChatMenuContactLayout = (LinearLayout) findViewById(R.id.chat_menu_contact_layout);
            mChatMenuAudioLayout = (LinearLayout) findViewById(R.id.chat_menu_audio_layout);

            mChatMenuGalleryImg = (ImageView) findViewById(R.id.chat_menu_gallery_img);
            mChatMenuShootImg = (ImageView) findViewById(R.id.chat_menu_shoot_img);
            mChatMenuVideoImg = (ImageView) findViewById(R.id.chat_menu_video_img);
            mChatMenuLocationImg = (ImageView) findViewById(R.id.chat_menu_location_img);
            mChatMenuContactImg = (ImageView) findViewById(R.id.chat_menu_contact_img);
            mChatMenuAudioImg = (ImageView) findViewById(R.id.chat_menu_audio_img);

            mChatMenuGalleryTxt = (TextView) findViewById(R.id.chat_menu_gallery_txt);
            mChatMenuShootTxt = (TextView) findViewById(R.id.chat_menu_shoot_txt);
            mChatMenuVideoTxt = (TextView) findViewById(R.id.chat_menu_video_txt);
            mChatMenuLocationTxt = (TextView) findViewById(R.id.chat_menu_location_txt);
            mChatMenuContactTxt = (TextView) findViewById(R.id.chat_menu_contact_txt);
            mChatMenuAudioTxt = (TextView) findViewById(R.id.chat_menu_audio_txt);

            Constant.typeFace(this, mChatMenuGalleryTxt);
            Constant.typeFace(this, mChatMenuShootTxt);
            Constant.typeFace(this, mChatMenuVideoTxt);
            Constant.typeFace(this, mChatMenuLocationTxt);
            Constant.typeFace(this, mChatMenuContactTxt);
            Constant.typeFace(this, mChatMenuAudioTxt);

            LinearLayout.LayoutParams hedderMenuParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            hedderMenuParams.width = width * 40 / 100;
            hedderMenuParams.height = height * 38 / 100;
            hedderMenuParams.gravity = Gravity.CENTER;
            mChatMenuLayout.setLayoutParams(hedderMenuParams);

            LinearLayout.LayoutParams hedderMenuLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            hedderMenuLayoutParams.width = width * 40 / 100;
            hedderMenuLayoutParams.height = height * 6 / 100;
            hedderMenuLayoutParams.gravity = Gravity.CENTER_VERTICAL;
            mChatMenuGalleryLayout.setLayoutParams(hedderMenuLayoutParams);
            mChatMenuShootLayout.setLayoutParams(hedderMenuLayoutParams);
            mChatmenuVideoLayout.setLayoutParams(hedderMenuLayoutParams);
            mChatMenuLocationLayout.setLayoutParams(hedderMenuLayoutParams);
            mChatMenuContactLayout.setLayoutParams(hedderMenuLayoutParams);
            mChatMenuAudioLayout.setLayoutParams(hedderMenuLayoutParams);

            LinearLayout.LayoutParams hedderMenuImgParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            hedderMenuImgParams.width = width * 4 / 100;
            hedderMenuImgParams.height = width * 4 / 100;
            hedderMenuImgParams.rightMargin = width * 3 / 100;
            hedderMenuImgParams.leftMargin = width * 3 / 100;
            hedderMenuImgParams.gravity = Gravity.CENTER_VERTICAL;
            mChatMenuGalleryImg.setLayoutParams(hedderMenuImgParams);
            mChatMenuShootImg.setLayoutParams(hedderMenuImgParams);
            mChatMenuVideoImg.setLayoutParams(hedderMenuImgParams);
            mChatMenuLocationImg.setLayoutParams(hedderMenuImgParams);
            mChatMenuContactImg.setLayoutParams(hedderMenuImgParams);
            mChatMenuAudioImg.setLayoutParams(hedderMenuImgParams);

            mChatMenuHedderLayout.setX(width * 25 / 100);
            mChatMenuHedderLayout.setY(height * 8 / 100);
            mChatMenuLayout.setVisibility(View.VISIBLE);

            if (width >= 600) {
                mChatMenuGalleryTxt.setTextSize(14);
                mChatMenuShootTxt.setTextSize(14);
                mChatMenuVideoTxt.setTextSize(14);
                mChatMenuLocationTxt.setTextSize(14);
                mChatMenuContactTxt.setTextSize(14);
                mChatMenuAudioTxt.setTextSize(14);

            } else if (width > 501 && width < 600) {

                mChatMenuGalleryTxt.setTextSize(12);
                mChatMenuShootTxt.setTextSize(12);
                mChatMenuVideoTxt.setTextSize(12);
                mChatMenuLocationTxt.setTextSize(12);
                mChatMenuContactTxt.setTextSize(12);
                mChatMenuAudioTxt.setTextSize(12);

            } else if (width > 260 && width < 500) {

                mChatMenuGalleryTxt.setTextSize(11);
                mChatMenuShootTxt.setTextSize(11);
                mChatMenuVideoTxt.setTextSize(11);
                mChatMenuLocationTxt.setTextSize(11);
                mChatMenuContactTxt.setTextSize(11);
                mChatMenuAudioTxt.setTextSize(11);

            } else if (width <= 260) {

                mChatMenuGalleryTxt.setTextSize(10);
                mChatMenuShootTxt.setTextSize(10);
                mChatMenuVideoTxt.setTextSize(10);
                mChatMenuLocationTxt.setTextSize(10);
                mChatMenuContactTxt.setTextSize(10);
                mChatMenuAudioTxt.setTextSize(10);

            }

            if (mMenuVisible) {

                Animation slide_down = AnimationUtils.loadAnimation(
                        getApplicationContext(), R.anim.slide_down);

                // Animation slide_up = AnimationUtils.loadAnimation(
                // getApplicationContext(), R.anim.slide_up);

                // Start animation
                mChatMenuHedderLayout.startAnimation(slide_down);
            } else {

                Animation slide_down = AnimationUtils.loadAnimation(
                        getApplicationContext(), R.anim.slide_up);

                // Animation slide_up = AnimationUtils.loadAnimation(
                // getApplicationContext(), R.anim.slide_up);

                // Start animation
                mChatMenuHedderLayout.startAnimation(slide_down);

            }
        } catch (Resources.NotFoundException e) {

        }

    }

    public void chatOptionMenu() {


        try {
            mChatOptionMenuHedderLayout = (LinearLayout) findViewById(R.id.chat_option_menu_hedder_layout);
            mChatOptionMenuLayout = (LinearLayout) findViewById(R.id.chat_option_menu_layout);
            mChatOptionMenuViewcontactLayout = (LinearLayout) findViewById(R.id.chat_option_menu_viewcontact_layout);
            mChatOptionMenuBlockLayout = (LinearLayout) findViewById(R.id.chat_option_menu_block_layout);
            mChatOptionMenuCallLayout = (LinearLayout) findViewById(R.id.chat_option_menu_call_layout);
            mChatOptionMenuLockchatLayout = (LinearLayout) findViewById(R.id.chat_option_menu_lockchat_layout);
            mChatOptionMenuClearchatLayout = (LinearLayout) findViewById(R.id.chat_option_menu_clearchat_layout);

            mChatOptionMenuViewcontactImg = (ImageView) findViewById(R.id.chat_option_menu_viewcontact_img);
            mChatOptionMenuBlockImg = (ImageView) findViewById(R.id.chat_option_menu_block_img);
            mChatOptionMenuCallImg = (ImageView) findViewById(R.id.chat_option_menu_call_img);
            mChatOptionMenuLockchatImg = (ImageView) findViewById(R.id.chat_option_menu_lockchat_img);
            mChatOptionMenuClearchatImg = (ImageView) findViewById(R.id.chat_option_menu_clearchat_img);

            mChatOptionMenuViewcontactTxt = (TextView) findViewById(R.id.chat_option_menu_viewcontact_txt);
            mChatOptionMenuBlockTxt = (TextView) findViewById(R.id.chat_option_menu_block_txt);
            mChatOptionMenuCallTxt = (TextView) findViewById(R.id.chat_option_menu_call_txt);
            mChatOptionMenuLockchatTxt = (TextView) findViewById(R.id.chat_option_menu_lockchat_txt);
            mChatOptionMenuClearchatTxt = (TextView) findViewById(R.id.chat_option_menu_clearchat_txt);

            Constant.typeFace(this, mChatOptionMenuViewcontactTxt);
            Constant.typeFace(this, mChatOptionMenuBlockTxt);
            Constant.typeFace(this, mChatOptionMenuCallTxt);
            Constant.typeFace(this, mChatOptionMenuLockchatTxt);
            Constant.typeFace(this, mChatOptionMenuClearchatTxt);

            LinearLayout.LayoutParams hedderMenuParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            hedderMenuParams.width = width * 40 / 100;
            hedderMenuParams.height = height * 17 / 100;
            hedderMenuParams.gravity = Gravity.CENTER;
            mChatOptionMenuLayout.setLayoutParams(hedderMenuParams);

            LinearLayout.LayoutParams hedderMenuLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            hedderMenuLayoutParams.width = width * 40 / 100;
            hedderMenuLayoutParams.height = height * 6 / 100;
            hedderMenuLayoutParams.gravity = Gravity.CENTER_VERTICAL;
            mChatOptionMenuViewcontactLayout
                    .setLayoutParams(hedderMenuLayoutParams);
            mChatOptionMenuBlockLayout.setLayoutParams(hedderMenuLayoutParams);
            mChatOptionMenuCallLayout.setLayoutParams(hedderMenuLayoutParams);
            mChatOptionMenuLockchatLayout.setLayoutParams(hedderMenuLayoutParams);
            mChatOptionMenuClearchatLayout.setLayoutParams(hedderMenuLayoutParams);

            LinearLayout.LayoutParams hedderMenuImgParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            hedderMenuImgParams.width = width * 4 / 100;
            hedderMenuImgParams.height = width * 4 / 100;
            hedderMenuImgParams.rightMargin = width * 3 / 100;
            hedderMenuImgParams.leftMargin = width * 3 / 100;
            hedderMenuImgParams.gravity = Gravity.CENTER_VERTICAL;
            mChatOptionMenuViewcontactImg.setLayoutParams(hedderMenuImgParams);
            mChatOptionMenuBlockImg.setLayoutParams(hedderMenuImgParams);
            mChatOptionMenuCallImg.setLayoutParams(hedderMenuImgParams);
            mChatOptionMenuLockchatImg.setLayoutParams(hedderMenuImgParams);
            mChatOptionMenuClearchatImg.setLayoutParams(hedderMenuImgParams);

            mChatOptionMenuHedderLayout.setX(width * 29 / 100);
            mChatOptionMenuHedderLayout.setY(height * 8 / 100);
            mChatOptionMenuLayout.setVisibility(View.VISIBLE);

            if (width >= 600) {

                mChatOptionMenuViewcontactTxt.setTextSize(14);
                mChatOptionMenuBlockTxt.setTextSize(14);
                mChatOptionMenuCallTxt.setTextSize(14);
                mChatOptionMenuLockchatTxt.setTextSize(14);
                mChatOptionMenuClearchatTxt.setTextSize(14);

            } else if (width > 501 && width < 600) {

                mChatOptionMenuViewcontactTxt.setTextSize(12);
                mChatOptionMenuBlockTxt.setTextSize(12);
                mChatOptionMenuCallTxt.setTextSize(12);
                mChatOptionMenuLockchatTxt.setTextSize(12);
                mChatOptionMenuClearchatTxt.setTextSize(12);

            } else if (width > 260 && width < 500) {

                mChatOptionMenuViewcontactTxt.setTextSize(11);
                mChatOptionMenuBlockTxt.setTextSize(11);
                mChatOptionMenuCallTxt.setTextSize(11);
                mChatOptionMenuLockchatTxt.setTextSize(11);
                mChatOptionMenuClearchatTxt.setTextSize(11);

            } else if (width <= 260) {

                mChatOptionMenuViewcontactTxt.setTextSize(10);
                mChatOptionMenuBlockTxt.setTextSize(10);
                mChatOptionMenuCallTxt.setTextSize(10);
                mChatOptionMenuLockchatTxt.setTextSize(10);
                mChatOptionMenuClearchatTxt.setTextSize(10);

            }

            if (mContactMenuVisible) {

                Animation slide_down = AnimationUtils.loadAnimation(
                        getApplicationContext(), R.anim.slide_down);

                // Animation slide_up = AnimationUtils.loadAnimation(
                // getApplicationContext(), R.anim.slide_up);

                // Start animation
                mChatOptionMenuHedderLayout.startAnimation(slide_down);
            } else {

                Animation slide_down = AnimationUtils.loadAnimation(
                        getApplicationContext(), R.anim.slide_up);

                // Animation slide_up = AnimationUtils.loadAnimation(
                // getApplicationContext(), R.anim.slide_up);

                // Start animation
                mChatOptionMenuHedderLayout.startAnimation(slide_down);

                slide_down.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        mChatOptionMenuLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        } catch (Resources.NotFoundException e) {

        }

    }


     private void delete_query(String query) {
        // TODO Auto-generated method stub
        Cursor c = null;

        try {
            c = db.open().getDatabaseObj().rawQuery(query, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
        } catch (SQLException e) {

        } finally {
            if (c != null) {
                c.close();
            }

            db.close();
        }
    }

    protected void insertToLock(ContentValues cv) {
        // TODO Auto-generated method stub
        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_LOCK, null, cv);
            Constant.printMsg("No of inserted rows in lock :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in ecpl details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }
    }

    private class MyAsync extends AsyncTask<String, String, String> {

        String lastseen;
        Presence presence;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            is_status_change = false;

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Calendar cal_today = Calendar.getInstance();
                SimpleDateFormat formatter_date = new SimpleDateFormat(
                        "dd-MM-yyyy");
                SimpleDateFormat formatter_last = new SimpleDateFormat(
                        "dd-MM-yyyy hh:ssa");
                SimpleDateFormat formatter_today = new SimpleDateFormat(
                        "hh:mma");

                LastActivity la = LastActivityManager
                        .getInstanceFor(connection).getLastActivity(jid);

                Calendar cal = Calendar.getInstance();
                Long l = (cal.getTimeInMillis() - (la.getIdleTime() * 1000));
                cal.setTimeInMillis(l);

                String today = formatter_date.format(cal_today.getTime());
                String last_seen_date = formatter_date.format(cal.getTime());
                if (today.toString().equals(last_seen_date)) {
                    // getSupportActionBar().setSubtitle("last seen todat at "+formatter_today.format(cal.getTime()));
                    // Constant.printMsg("last seen today at "+formatter_today.format(cal.getTime()));
                    lastseen = "last seen today at "
                            + formatter_today.format(cal.getTime());
                } else {
                    // Constant.printMsg("last seen todat at "+formatter_today.format(cal.getTime()));
                    lastseen = "last seen at "
                            + formatter_last.format(cal.getTime());
                }

            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
            }

            try {
                Roster roster = Roster.getInstanceFor(connection);
                presence = roster.getPresence(jid);

                // Presence presence = new Pre
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (presence.isAvailable()) {
                //getSupportActionBar().setSubtitle("online");
            } else {
                //getSupportActionBar().setSubtitle(lastseen);
            }
            is_status_change = true;

            // updateReadMessages();
			/*
			 * messageEventManager = new MessageEventManager(connection);
			 * messageEventManager.addMessageEventNotificationListener(new
			 * messagenot());
			 */
            super.onPostExecute(result);

        }

    }



    private class FetchChat extends AsyncTask<String, String, ArrayList<MessageGetSet>> {

        @Override
        protected void onPreExecute() {
            Constant.printMsg("Fetch Chat onPreExecute1");
            super.onPreExecute();
        }

        @Override
        protected ArrayList<MessageGetSet> doInBackground(String... params) {
            Constant.printMsg("Fetch Chat onPreExecute2");
            cursor = dbAdapter.getMessagesCursor(jid);
            Constant.printMsg("Fetch Chat onPreExecute3");

            try {


            if (cursor != null) {

                Constant.printMsg("Fetch Chat onPreExecute 3.5  " + cursor.getCount());

                msg_list.clear();
                Constant.msg_list_adapter.clear();
                try {
                    Constant.printMsg("Fetch Chat onPreExecute4");

                    if (cursor.moveToFirst()) {
                        do {

                            MessageGetSet msg = new MessageGetSet();
                            msg.set_id(cursor.getInt(0));
                            msg.setData(cursor.getString(6));
                            msg.setKey_from_me(cursor.getInt(2));
                            msg.setKey_id(cursor.getString(3));
                            msg.setKey_remote_jid(cursor.getString(1));
                            msg.setLatitude(cursor.getDouble(14));
                            msg.setLongitude(cursor.getDouble(15));
                            msg.setMedia_duration(cursor.getInt(24));
                            msg.setMedia_hash(cursor.getString(13));
                            msg.setMedia_mime_type(cursor.getString(9));
                            msg.setMedia_name(cursor.getString(12));
                            msg.setMedia_size(cursor.getInt(11));
                            msg.setMedia_url(cursor.getString(8));
                            msg.setMedia_wa_type(cursor.getString(10));
                            msg.setNeeds_push(cursor.getInt(5));
                            msg.setOrigin(cursor.getInt(25));
                            msg.setReceipt_device_timestamp(cursor.getLong(21));
                            msg.setReceipt_server_timestamp(cursor.getLong(20));
                            msg.setReceived_timestamp(cursor.getLong(18));
                            msg.setRemote_resource(cursor.getString(17));
                            msg.setRow_data(cursor.getBlob(22));
                            msg.setSend_timestamp(cursor.getLong(19));
                            msg.setStatus(cursor.getInt(4));
                            msg.setThumb_image(cursor.getBlob(16));
                            msg.setTimestamp(cursor.getLong(7));

                            msg_list.add(msg);

                        } while (cursor.moveToNext());
                    }


                } catch (Exception e) {
                    Constant.printMsg("Fetch Chat onPreExecute  Exppp " + e.toString());
                } finally {
                    if (cursor != null)
                        cursor.close();
                }

            }
                }catch (Exception e){

            }
            return msg_list;
        }

        @Override
        protected void onPostExecute(ArrayList<MessageGetSet> result) {

            if (result != null)  {
                try   {

                    Constant.msg_list_adapter=msg_list;

                    mDynamicView.removeAllViews();


                    Constant.printMsg("Fetch Chat onPreExecute 6 " + msg_list.size());

                    for (k = 0; k < msg_list.size(); k++) {

                        if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) > 6 && Integer.parseInt(msg_list.get(k).getMedia_wa_type()) < 12) {

                            notificationMessages();
                        }


                        //Right Chat
                        else if (msg_list.get(k).getKey_from_me() != 1) {

                            intializationElements(1);
                            //Right Text Chat
                            if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 0) {

                                //Text
                                rightTextChat();
                                setRightChatText();


                            } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 1) {

                                //Image
                                rightImageChat();
                                setRightImage();

                            }  else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 2) {

                                //Video
                                rightVideoChat();
                                setRightVideo();

                            }  else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 5) {

                                //Contact
                                rightContactChat();
                                setRightContact();

                            }  else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 3) {

                                //Audio
                                rightAudioChat();
                                setRightAudio_Old();

                            }  else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 4) {

                                //Location
                                rightImageChat();
                                setRightLocation();

                            }
                        }else{
                            intializationElements(2);

                            if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 0) {

                                leftTextChat();
                                setLeftChatText();

                            } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 1) {

                                leftImageChat();
                                setLeftImage();
                            }
                            else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 2) {

                                leftVideoChat();
                                setLeftVideo();

                            } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 5) {

                                leftContactChat();
                                setLeftContact();

                            }else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 3) {

                                //Audio
                                leftAudioChat();
                                setLeftAudio_Old();

                            }  else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 4) {

                                //Location
                                leftImageChat();
                                setLeftLocation();

                            }

                        }

                    }



                    mRightTipLayout.requestFocus();
                    edt_msg.requestFocus();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            isFirstTime=false;
            isFetchChatReady = false;

            super.onPostExecute(result);

        }



    }

    private class ActionModeCallback implements ActionMode.Callback {
        boolean is_copy = true;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            is_copy = true;
            mode.getMenuInflater().inflate(R.menu.menu_chat_actionmode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // TODO Auto-generated method stub
            if (is_copy) {
                MenuItem item = menu.findItem(R.id.menu_copy);
                item.setVisible(true);
                return true;
            } else {
                MenuItem item = menu.findItem(R.id.menu_copy);
                item.setVisible(false);
                return true;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // TODO Auto-generated method stub
            is_copy = true;
//            Constant.adapter_muc_test.removeSelection();
            mActionMode = null;

        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

//            SparseBooleanArray selected = Constant.adapter_muc_test.getSelectedIds();

//            switch (item.getItemId()) {
//
//                case R.id.menu_delete:
//
//                    for (int i = (selected.size() - 1); i >= 0; i--) {
//                        if (selected.valueAt(i)) {
//                            MessageGetSet selectedItem = msg_list.get(selected
//                                    .keyAt(i));
//                            // adapter.remove(selectedItem);
//                            dbAdapter.setDeleteMessages_by_msgid(""
//                                    + selectedItem.get_id());
//                        }
//                    }
//                    mode.finish(); // Action picked, so close the CAB
//
//                    int msg_id = dbAdapter.getLastMsgid(jid);
//
//                    if (dbAdapter.isExistinChatList(jid)) {
//                        dbAdapter.setUpdateChat_lits(jid, msg_id);
//                    } else {
//                        dbAdapter.setInsertChat_list(jid, msg_id);
//                    }
//
//                    ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
//                    break;
//
//                case R.id.menu_copy:
//                    String copiedtext = "";
//                    int j = 0;
//
//                    // ClipboardManager clipboard = (ClipboardManager)
//                    // getSystemService(Context.CLIPBOARD_SERVICE);
//                    if (selected != null) {
//                        for (int i = 0; i < selected.size(); i++) {
//                            if (selected.valueAt(i)) {
//
//                                Log.i("Selected", selected.keyAt(i)
//                                        + " was selected ::" + msg_list.size());
//                                Log.i("Select",
//                                        "Selected ID::"
//                                                + msg_list.get(selected.keyAt(i))
//                                                .get_id());
//                                if (j == 0) {
//                                    copiedtext = msg_list.get(selected.keyAt(i))
//                                            .getData();
//                                } else {
//                                    copiedtext = copiedtext
//                                            + "\n"
//                                            + msg_list.get(selected.keyAt(i))
//                                            .getData();
//                                    ;
//                                }
//                                j++;
//                            }
//                        }
//                    }
//                    // clipboard.setText(copiedtext);
//
//                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//                    if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
//                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//					/*
//					 * ClipData clip = ClipData.newPlainText("label",
//					 * "Text to Copy"); clipboard.setPrimaryClip(clip);
//					 */
//                        clipboard.setText(copiedtext);
//
//                    } else {
//                        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                        clipboard.setText(copiedtext);
//                    }
//
//                    mode.finish();
//                    break;
//                case R.id.menu_forword:
//                    String msg_ids = "";
//                    int k = 0;
//
//                    if (selected != null) {
//                        for (int i = 0; i < selected.size(); i++) {
//                            if (selected.valueAt(i)) {
//
//                                if (k == 0) {
//                                    msg_ids = ""
//                                            + msg_list.get(selected.keyAt(i))
//                                            .get_id();
//                                } else {
//                                    msg_ids = msg_ids
//                                            + ","
//                                            + msg_list.get(selected.keyAt(i))
//                                            .get_id();
//                                }
//                                k++;
//                            }
//                        }
//                    }
//
//                    Intent intent = new Intent(mParentActivity, SliderTesting.class);
//                    Log.i("Forword", "Message IDS " + msg_ids);
//                    intent.putExtra("msg_ids", msg_ids);
//
//                    startActivityForResult(intent, 11);
//                    mode.finish();
//                    break;
//
//                default:
//                    break;
//            }

            return false;

        }

    }

    public class getKeyword extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            // TODO Auto-generated method stub

            super.onPreExecute();

        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();

            result = ht.httpget(KachingMeConfig.KEYWORD_URL);

            Constant.printMsg("the key values::::" + result);

            Constant.printMsg("result dis" + result);
            return result;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (result != null && result.length() > 0) {

                Constant.chatdeel.clear();

                JSONArray jarray;
                try {
                    jarray = new JSONArray(result);
                    for (int i = 0; i < jarray.length(); i++) {

                        Constant.chatdeel.add(jarray.getString(i));

                        Constant.printMsg("ouyput:::" + Constant.chatdeel);

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                // Toast.makeText(getApplicationContext(),
                // "Network Error!Please try again later!",
                // Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class Msync1 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Constant.printMsg("jid::::contact try" + jid);
                bmp = BitmapFactory.decodeFile(KachingMeApplication.PROFILE_PIC_DIR
                        + jid.toString().split("@")[0] + ".png");
                Constant.printMsg("jid::::contact bmp"
                        + KachingMeApplication.PROFILE_PIC_DIR
                        + jid.toString().split("@")[0] + ".png" + bmp);

            } catch (Exception e) {
                // //ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                Constant.printMsg("jid::::bmp exce" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            try {
                if (bmp != null) {
                    Constant.printMsg("jid::::bmp" + jid);
                    Drawable drawable = new BitmapDrawable(bmp);
                    // getSupportActionBar().setIcon(drawable);
                    img_chat_avatar.setImageDrawable(drawable);

                } else {
                    Constant.printMsg("jid::::bmp else" + jid);
                    img_chat_avatar.setImageDrawable(getApplicationContext()
                            .getResources().getDrawable(
                                    R.drawable.chat_img_avtar));
                }

            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
                e.printStackTrace();
                Constant.printMsg("jid::::bmp exce" + e);
            }

        }

    }

    /**
     * Seding recording status to other member
     *
     */
    public void presence_recording()
    {
        timer = new Timer();
        // This timer task will be executed every 1 sec.
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message(jid, Type.chat);
                msg.setStanzaId(Constant.TYPING_STATUS_RECORDING);

                msg.setBody(Constant.TYPING_STATUS_RECORDING);

                try {


                    if(Connectivity.isOnline(mParentActivity) && Connectivity.isTempConnection()){
                            TempConnectionService.connection.sendStanza(msg);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 0, 1000);
    }
















































































































    // ////////////////////////----Image
    // Upload-----//////////////////////////////////////////////////////////////////////
    public class uploa_image extends AsyncTask<String, String, String> {

        MessageGetSet message = new MessageGetSet();
        String file_upload_res = null;
        String from, msg_id;
        String url = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
////            muc = TempConnectionService.MUC_MANAGER.getMultiUserChat(jid);
//            try {
//
////                muc.join(KachingMeApplication.getUserID()
////                        + KachingMeApplication.getHost());
////                muc.addMessageListener(TempConnectionService.muc_messageListener);
//
//            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//                // TODO: handle exception
//                e.printStackTrace();
//            }

            Log.d("Message key id", "Message key id::" + params[0]);
            message = dbAdapter.getMessages_by_msg_id(params[0]);
            msg_id = params[0];
            dbAdapter.setUpdateMessage_need_push(message.getKey_id(), 2);
            from = params[1];

            try {

                final Message msg = new Message(jid, Type.groupchat);
                msg.setStanzaId("" + message.getKey_id());
                msg.setBody("");

                JivePropertiesManager.addProperty(msg, "media_type",
                        message.getMedia_wa_type());
                JivePropertiesManager.addProperty(msg, "mime_type",
                        message.getMedia_mime_type());
                JivePropertiesManager.addProperty(msg, "thumb_image",
                        Base64.encodeToString(message.getRow_data()));
                JivePropertiesManager.addProperty(msg, "media_duration",
                        message.getMedia_duration());
                JivePropertiesManager.addProperty(msg, "media_size",
                        message.getMedia_size() / 1024);
                JivePropertiesManager.addProperty(msg, "media_type", "1");
                JivePropertiesManager.addProperty(msg, "ID", 5);

                DeliveryReceiptRequest.addTo(msg);

                RequestParams request_params = new RequestParams();

                request_params.setUseJsonStreamer(false);


                request_params.put("primaryNo", "" + KachingMeApplication.getUserID().split("@")[0]);
                request_params.put("fileType",  "1");
                request_params.put("fileName",  "null");
                request_params.put("latitude",  "null");
                request_params.put("longitude", "null");
                request_params.put("reciverId", "1");
                request_params.put("groupId", "null");
                request_params.put("msgId", "" + message.getKey_id());
                request_params.put("file", new File(
                        Constant.local_image_dir + message.getMedia_name()), "image/jpeg");
                // File

                AsyncHttpClient client = mAsyncUpload_Image.get(params[0]);
                client.setTimeout(60000);
                if (message.getMedia_url() == null) {
                    client.post(mParentActivity,
                            MEDIA_UPLOAD_URL,
                            null,
                            request_params,"multipart/form-data",
                            new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                                @Override
                                public void onFailure(int arg0, Header[] arg1,
                                                      byte[] arg2, Throwable arg3) {
                                    // TODO Auto-generated method stub
                                    file_upload_res = null;

                                    dbAdapter.setUpdateMessage_need_push(message.getKey_id(), 1);
                                    dbAdapter.setUpdateMessage_status(
                                            message.getKey_id(), 3);
                                   /* Log.e(TAG,
                                            "on Failure file upload response::"
                                                    + arg3.getMessage());*/
                                    dbAdapter.setUpdateMessage_Media_url(
                                            msg_id, null);

                                    Intent login_broadcast = new Intent("chat");
                                    login_broadcast.putExtra("jid", from);
                                    mParentActivity.getApplicationContext().sendBroadcast(login_broadcast);
                                }

                                @Override
                                public void onCancel() {
                                    super.onCancel();
                                }

                                @Override
                                public void onSuccess(int arg0, Header[] arg1,
                                                      byte[] arg2) {
                                    String content = new String(arg2);

                                    dbAdapter.setUpdateMessage_status(
                                            message.getKey_id(), 2);

                                    try {


                                        JSONObject jsonObject_Image = new JSONObject(content);
//
                                        url = jsonObject_Image.getString("url");
                                        msg_id = jsonObject_Image.getString("msgId");

                                        /*Log.d(TAG, "URL::" + url);*/

                                    } catch (JSONException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }

                                    long l = dbAdapter
                                            .setUpdateMessage_Media_url(msg_id,
                                                    url);

                                    JivePropertiesManager.addProperty(msg,
                                            "path", url);
                                    try {
                                        DeliveryReceiptManager
                                                .addDeliveryReceiptRequest(msg);
                                        MessageEventManager
                                                .addNotificationsRequests(msg,
                                                        true, true, true, true);
                                        muc.sendMessage(msg);

                                    } catch (Exception e) {
                                        // ACRA.getErrorReporter().handleException(e);
                                        e.printStackTrace();
                                    }

                                    Intent login_broadcast = new Intent("chat");
                                    login_broadcast.putExtra("jid", from);
                                    mParentActivity.getApplicationContext().sendBroadcast(login_broadcast);

                                }

                            });
                } else {
                    file_upload_res = message.getMedia_url();
                    JivePropertiesManager.addProperty(msg, "path",
                            file_upload_res);
                    try {
                        MessageEventManager.addNotificationsRequests(msg, true,
                                true, true, true);
                        muc.sendMessage(msg);

                    } catch (Exception e) {
                        // ACRA.getErrorReporter().handleException(e);
                        e.printStackTrace();
                    }

                    Intent login_broadcast = new Intent("chat");
                    login_broadcast.putExtra("jid", from);
                    mParentActivity.getApplicationContext().sendBroadcast(login_broadcast);
                }
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                // TODO: handle exception
            }

            return file_upload_res;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

			/*
			 * if(file_upload_res==null) {
			 * dbAdapter.setUpdateMessage_need_push(message.getKey_id(),0); }
			 * else { dbAdapter.setUpdateMessage_status(message.getKey_id(),0);
			 * }
			 */

        }
    }

    public class uploa_video extends AsyncTask<String, String, String> {

        MessageGetSet message = new MessageGetSet();
        String file_upload_res = null;
        String from, msg_id;
        String url = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

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


            message = dbAdapter.getMessages_by_msg_id(params[0]);
            long pushB = dbAdapter.setUpdateMessage_need_push(message.getKey_id(), 2);

            dbAdapter.setUpdateMessage_status(message.getKey_id(), 3);
            Constant.printMsg("......Chat Video 3..data1...." +pushB
            );
            from = params[1];
            msg_id = params[0];

            try {

                final Message msg = new Message(jid, Type.groupchat);
                JivePropertiesManager.addProperty(msg, "msg_type", 2);
                msg.setStanzaId("" + message.getKey_id());
                msg.setBody("");
                JivePropertiesManager.addProperty(msg, "media_type",
                        message.getMedia_wa_type());
                JivePropertiesManager.addProperty(msg, "mime_type",
                        message.getMedia_mime_type());
                JivePropertiesManager.addProperty(msg, "thumb_image",
                        Base64.encodeToString(message.getRow_data()));
                JivePropertiesManager.addProperty(msg, "media_duration",
                        message.getMedia_duration());
                JivePropertiesManager.addProperty(msg, "media_size",
                        message.getMedia_size() / (1024 * 1024));
                JivePropertiesManager.addProperty(msg, "media_type", "2");
                JivePropertiesManager.addProperty(msg, "ID", 5);

                DeliveryReceiptRequest.addTo(msg);

                RequestParams request_params = new RequestParams();


                request_params.setUseJsonStreamer(false);


                request_params.put("primaryNo", "" + KachingMeApplication.getUserID().split("@")[0]);
                request_params.put("fileType",  "3");
                request_params.put("fileName",  "null");
                request_params.put("latitude",  "null");
                request_params.put("longitude", "null");
                request_params.put("reciverId", "1");
                request_params.put("groupId", "null");
                request_params.put("msgId", "" + message.getKey_id());
                request_params.put("file", new File(
                        Constant.local_video_dir + message.getMedia_name()));

                AsyncHttpClient client = mAsyncUpload_Video.get(params[0]);
                // client.setTimeout(60000);
                if (message.getMedia_url() == null) {
                    client.post(mParentActivity,
                            MEDIA_UPLOAD_URL,
                            null,
                            request_params,"multipart/form-data",
                            new AsyncHttpResponseHandler(Looper.getMainLooper()) {


                                @Override
                                public void onFailure(int arg0, Header[] arg1,
                                                      byte[] arg2, Throwable arg3) {
                                    // TODO Auto-generated method stub
                                    file_upload_res = null;
                                    Log.e("Video Upload",
                                            "on Failure file upload response::"
                                                    + arg3.getMessage());
                                    dbAdapter.setUpdateMessage_Media_url(
                                            msg_id, null);

                                    dbAdapter.setUpdateMessage_need_push(message.getKey_id(), 1);
                                    dbAdapter.setUpdateMessage_status(message.getKey_id(), 3);

                                    Intent login_broadcast = new Intent("chat");
                                    login_broadcast.putExtra("jid", from);
                                    mParentActivity.getApplicationContext().sendBroadcast(login_broadcast);
                                }

                                @Override
                                public void onCancel() {
                                    super.onCancel();
                                }

                                @Override
                                public void onSuccess(int arg0, Header[] arg1,
                                                      byte[] arg2) {

                                    String content = new String(arg2);
                                    try {


                                        JSONObject jsonObject_Image = new JSONObject(content);
//
                                        url = jsonObject_Image.getString("url");
                                        msg_id = jsonObject_Image.getString("msgId");

                                    } catch (JSONException e1) {
                                        // ACRA.getErrorReporter().handleException(e1);
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }

                                    long l = dbAdapter
                                            .setUpdateMessage_Media_url(msg_id,
                                                    url);

                                    JivePropertiesManager.addProperty(msg,
                                            "path", url);

                                    try {
                                        DeliveryReceiptManager
                                                .addDeliveryReceiptRequest(msg);
                                        MessageEventManager
                                                .addNotificationsRequests(msg,
                                                        true, true, true, true);
                                        muc.sendMessage(msg);

                                    } catch (Exception e) {

                                        e.printStackTrace();
                                    }

                                    dbAdapter.setUpdateMessage_status(message.getKey_id(), 2);

                                    Intent login_broadcast = new Intent("chat");
                                    login_broadcast.putExtra("jid", from);
                                    mParentActivity.getApplicationContext().sendBroadcast(login_broadcast);
                                }

                            });
                } else {
                    file_upload_res = message.getMedia_url();
                    JivePropertiesManager.addProperty(msg, "path",
                            file_upload_res);
                    try {
                        MessageEventManager.addNotificationsRequests(msg, true,
                                true, true, true);
                        muc.sendMessage(msg);

                    } catch (Exception e) {
                        // ACRA.getErrorReporter().handleException(e);
                        e.printStackTrace();
                    }

                    Intent login_broadcast = new Intent("chat");
                    login_broadcast.putExtra("jid", from);
                    mParentActivity.getApplicationContext().sendBroadcast(login_broadcast);
                }
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                // TODO: handle exception
            }

            return file_upload_res;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);



        }
    }

    public class ClickSpan extends ClickableSpan {
        private final/* synthetic */
                String val$clickString;
        EmojiconTextView mRightChatText;

        ClickSpan(String str, EmojiconTextView mRightChatText) {
            this.val$clickString = str;
            this.mRightChatText = mRightChatText;
        }

        public void onClick(View widget) {
            boolean bFlag;
            int i;

            try {
                mMessegeList = new ArrayList();
                mMeaningList = new ArrayList();

                Constant.printMsg("GGGGGGGGGGGGGGGKUL" + Constant.msg_list_adapter.get(
                        Integer.valueOf(String.valueOf(widget.getId()))).getData() + "    " + widget.getTag() + widget.getId());

                myMethod(Constant.msg_list_adapter.get(
                        Integer.valueOf(String.valueOf(widget.getId()))).getData());
                Normallist = "";

//                Set mtempMessegeSet = new HashSet();
//                mtempMessegeSet.addAll(mMessegeList);
//
//                Set mtempMeaningSet = new HashSet();
//                mtempMeaningSet.addAll(mMeaningList);
//
//                mMessegeList = new ArrayList();
//                mMeaningList = new ArrayList();
//
//
//                mMessegeList.addAll(mtempMessegeSet);
//                mMeaningList.addAll(mtempMeaningSet);

                String[] nymsplit = mRightChatText.getText().toString()
                        .split("\\s+");

                Constant.printMsg("TRTRTRT"
                        + mMessegeList
                        + "    "
                        + mMeaningList
                        + "    "
                        + val$clickString.subSequence(1,
                        val$clickString.length() - 1) + "    ");

//                ArrayList mTempMsgValue=new ArrayList();
//                ArrayList mTempMeanValue=new ArrayList();
//
//                for(int x=0;x<mMessegeList.size();x++){
//
//                    mTempMsgValue.add(x);
//                    mTempMeanValue.add(x);
//                }
//
//                for(int x=0;x<mMessegeList.size();x++){
//
//                    char mPos=mMessegeList.get(x).toString().charAt(mMessegeList.get(x).toString().length()-1);
//                    int mPosMean=Integer.valueOf(mMeaningList.get(x).toString().charAt(mMeaningList.get(x).toString().length()-1));
//                    Constant.printMsg("FFGSDSDD" + mTempMsgValue+"     "+mTempMeanValue+"  "+mMessegeList.get(x).toString().charAt(mMessegeList.get(x).toString().length()-1)+"   "+Character.getNumericValuemPos);
////                    mTempMsgValue.set(mPos,mMessegeList.get(x).toString());
////                    mTempMeanValue.set(mPosMean,mMeaningList.get(x).toString());
//                }
//
//                Constant.printMsg("FFGSDSDD" + mTempMsgValue+"     "+mTempMeanValue);
                for (int j = 0; j < nymsplit.length; j++) {

                    Constant.printMsg("TRTRTRT1111" + nymsplit[j]);

                    if (nymsplit[j].equals(val$clickString.subSequence(1,
                            val$clickString.length() - 1))) {

                        if (mMessegeList.contains(val$clickString.subSequence(1,
                                val$clickString.length() - 1))) {

                            for (int k = 0; k < mMessegeList.size(); k++) {

                                if (val$clickString.subSequence(1,
                                        val$clickString.length() - 1).equals(
                                        mMessegeList.get(k).toString())) {

                                    Normallist += " " + "["
                                            + mMeaningList.get(k).toString() + "]";

                                }

                            }

                        } else if (mMeaningList.contains(val$clickString
                                .subSequence(1, val$clickString.length() - 1))) {

                            for (int k = 0; k < mMeaningList.size(); k++) {

                                if (val$clickString.subSequence(1,
                                        val$clickString.length() - 1).equals(
                                        mMeaningList.get(k).toString())) {

                                    Normallist += " " + "["
                                            + mMessegeList.get(k).toString() + "]";

                                }

                            }

                        }

                    } else {

                        if (mMessegeList.contains(nymsplit[j])) {

                            for (int k = 0; k < mMessegeList.size(); k++) {

                                if (nymsplit[j].equals(mMessegeList.get(k)
                                        .toString())) {

                                    Normallist += " " + "["
                                            + mMessegeList.get(k).toString() + "]";

                                }

                            }

                        } else if (mMeaningList.contains(nymsplit[j])) {

                            for (int k = 0; k < mMeaningList.size(); k++) {

                                if (nymsplit[j].equals(mMeaningList.get(k)
                                        .toString())) {

                                    Normallist += " " + "["
                                            + mMeaningList.get(k).toString() + "]";

                                }

                            }

                        } else {

                            Normallist += " " + nymsplit[j];

                        }

                    }

                }

                Constant.printMsg("FFFFFFFFFFFFFinal" + Normallist);


                mRightChatText.setText(
                        addClickablePart(Normallist, mRightChatText),
                        TextView.BufferType.SPANNABLE);
            } catch (NumberFormatException e) {

            }
        }
    }

    // ////////////////////////----Audio
    // Upload-----//////////////////////////////////////////////////////////////////////
    public class uploa_audio extends AsyncTask<String, String, String> {

        MessageGetSet message = new MessageGetSet();
        String file_upload_res = null;
        String from, url = null, msg_id;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

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

            Log.d("Message key id", "Message key id::" + params[0]);
            message = dbAdapter.getMessages_by_msg_id(params[0]);
            dbAdapter.setUpdateMessage_status(message.getKey_id(), 3);
            from = params[1];
            msg_id = params[0];

            dbAdapter.setUpdateMessage_need_push(message.getKey_id(), 2);

            try {

                final Message msg = new Message(jid, Type.groupchat);
                JivePropertiesManager.addProperty(msg, "msg_type", 3);
                msg.setStanzaId("" + message.getKey_id());
                msg.setBody("");
                JivePropertiesManager.addProperty(msg, "media_type",
                        message.getMedia_wa_type());
                JivePropertiesManager.addProperty(msg, "mime_type",
                        message.getMedia_mime_type());
                JivePropertiesManager.addProperty(msg, "media_duration",
                        message.getMedia_duration());
                JivePropertiesManager.addProperty(msg, "media_size",
                        message.getMedia_size() / 1024);
                JivePropertiesManager.addProperty(msg, "media_type", "3");
                JivePropertiesManager.addProperty(msg, "ID", 5);
                DeliveryReceiptRequest.addTo(msg);

                RequestParams request_params = new RequestParams();
                request_params.setUseJsonStreamer(false);


                request_params.put("primaryNo", "" + KachingMeApplication.getUserID().split("@")[0]);
                request_params.put("fileType",  "2");
                request_params.put("fileName",  "null");
                request_params.put("latitude",  "null");
                request_params.put("longitude", "null");
                request_params.put("reciverId", "1");
                request_params.put("groupId", "null");
                request_params.put("msgId", "" + message.getKey_id());
                request_params.put("file", new File(
                        Constant.local_audio_dir + message.getMedia_name()));

                AsyncHttpClient client = mAsyncUpload_Audio.get(params[0]);
                client.setTimeout(60000);
                if (message.getMedia_url() == null) {
                    client.post(mParentActivity,
                            MEDIA_UPLOAD_URL,
                            null,
                            request_params,"multipart/form-data",
                            new AsyncHttpResponseHandler(Looper.getMainLooper()) {


                                @Override
                                public void onFailure(int arg0, Header[] arg1,
                                                      byte[] arg2, Throwable arg3) {

                                    file_upload_res = null;
                                    Log.e("audio",
                                            "on Failure file upload response::"
                                                    + arg3.getMessage());
                                    dbAdapter.setUpdateMessage_status(
                                            message.getKey_id(), 3);
                                    dbAdapter.setUpdateMessage_need_push(message.getKey_id(), 1);
                                    dbAdapter.setUpdateMessage_Media_url(
                                            msg_id, null);

                                    Intent login_broadcast = new Intent("chat");
                                    login_broadcast.putExtra("jid", from);
                                    mParentActivity.getApplicationContext().sendBroadcast(login_broadcast);
                                }

                                @Override
                                public void onSuccess(int arg0, Header[] arg1,
                                                      byte[] arg2) {
                                    String content = new String(arg2);
                                    dbAdapter.setUpdateMessage_status(
                                            message.getKey_id(), 2);

                                    try {


                                        JSONObject jsonObject_Image = new JSONObject(content);
//
                                        url = jsonObject_Image.getString("url");
                                        msg_id = jsonObject_Image.getString("msgId");

                                    } catch (JSONException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }

                                    long l = dbAdapter
                                            .setUpdateMessage_Media_url(msg_id,
                                                    url);

                                    JivePropertiesManager.addProperty(msg,
                                            "path", url);
                                    try {
                                        DeliveryReceiptManager
                                                .addDeliveryReceiptRequest(msg);
                                        MessageEventManager
                                                .addNotificationsRequests(msg,
                                                        true, true, true, true);
                                        muc.sendMessage(msg);

                                    } catch (Exception e) {
                                        // ACRA.getErrorReporter().handleException(e);
                                        e.printStackTrace();
                                    }

                                    Intent login_broadcast = new Intent("chat");
                                    login_broadcast.putExtra("jid", from);
                                    mParentActivity.getApplicationContext().sendBroadcast(login_broadcast);

                                }

                            });
                } else {
                    file_upload_res = message.getMedia_url();
                    JivePropertiesManager.addProperty(msg, "path",
                            file_upload_res);
                    try {
                        MessageEventManager.addNotificationsRequests(msg, true,
                                true, true, true);
                        muc.sendMessage(msg);

                    } catch (Exception e) {
                        // ACRA.getErrorReporter().handleException(e);
                        e.printStackTrace();
                    }


                    Intent login_broadcast = new Intent("chat");
                    login_broadcast.putExtra("jid", from);
                    mParentActivity.getApplicationContext().sendBroadcast(login_broadcast);
                }
            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                // TODO: handle exception
            }

            return file_upload_res;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

			/*
			 * if(file_upload_res==null) {
			 * dbAdapter.setUpdateMessage_need_push(message.getKey_id(),0); }
			 * else { dbAdapter.setUpdateMessage_status(message.getKey_id(),0);
			 * }
			 */






        }
    }

    public class download_image extends AsyncTask<String, String, String>

    {
        String msg_id, msg_url, data1, from, file_name;

        @Override
        protected void onProgressUpdate(String... values) {
            // TODO Auto-generated method stub

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            msg_id = params[0];
            msg_url = params[1];
            from = params[2];
            final long time = System.currentTimeMillis();
            ;
            file_name = "" + time;

            boolean success = (new File(Constant.local_image_dir)).mkdirs();
            if (!success) {
                Log.w("directory not created", "directory not created");
            }

            Log.d("Media", "Download file path::" + msg_url);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(60000);

            String[] allowedContentTypes = new String[]{"image/png",
                    "image/jpeg"};
            client.get(msg_url,
                    new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] binaryData, Throwable error) {
                            // TODO Auto-generated method stub

                            new AlertManager()
                                    .showAlertDialog(
                                            mParentActivity,
                                            mParentActivity.getResources()
                                                    .getString(
                                                            R.string.sorry_image_downloading_fail),
                                            true);
                            Intent login_broadcast = new Intent("chat");
                            login_broadcast.putExtra("jid", from);
                            mParentActivity.getApplicationContext().sendBroadcast(
                                    login_broadcast);

                            // super.onFailure(statusCode, headers, binaryData,
                            // error);
                        }

                        /*
						 * @Override public void onSuccess(byte[] fileData) { //
						 * Do something with the file data1 =
						 * String.valueOf(String.format(
						 * Constant.local_image_dir + "%d.jpg", time));
						 *
						 * try { Bitmap myBitmap =
						 * BitmapFactory.decodeByteArray( fileData, 0,
						 * fileData.length); FileOutputStream stream = new
						 * FileOutputStream(data1);
						 *
						 * ByteArrayOutputStream outstream = new
						 * ByteArrayOutputStream();
						 * myBitmap.compress(Bitmap.CompressFormat.JPEG, 85,
						 * outstream); byte[] byteArray =
						 * outstream.toByteArray();
						 *
						 * stream.write(byteArray); stream.close();
						 *
						 * dbAdapter.setUpdateMessage_MediaName(msg_id,
						 * file_name + ".jpg");
						 *
						 * Intent login_broadcast = new Intent("chat");
						 * login_broadcast.putExtra("jid", from);
						 * context.getApplicationContext().sendBroadcast(
						 * login_broadcast);
						 *
						 * } catch (Exception e) { // TODO: handle exception } }
						 */
                        @Override
                        public void onSuccess(int arg0, Header[] arg1,
                                              byte[] fileData) {
                            // TODO Auto-generated method stub
                            data1 = String.valueOf(String.format(
                                    Constant.local_image_dir + "%d.jpg", time));

                            try {
                                Bitmap myBitmap = BitmapFactory
                                        .decodeByteArray(fileData, 0,
                                                fileData.length);
                                FileOutputStream stream = new FileOutputStream(
                                        data1);

                                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                                myBitmap.compress(Bitmap.CompressFormat.JPEG,
                                        85, outstream);
                                byte[] byteArray = outstream.toByteArray();

                                stream.write(byteArray);
                                stream.close();

                                dbAdapter.setUpdateMessage_MediaName(msg_id,
                                        file_name + ".jpg");

                                bites = (long) fileData.length;
                                updateMediaNetwork_Receive(bites);

                                Intent login_broadcast = new Intent("chat");
                                login_broadcast.putExtra("jid", from);
                                mParentActivity.getApplicationContext().sendBroadcast(
                                        login_broadcast);

                            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                                // TODO: handle exception
                            }
                        }
                    });

            try {
                Log.d("Image Url", "Image Url::" + msg_url);
                URL url = new URL(msg_url);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                data1 = String.valueOf(String.format(Constant.local_image_dir
                        + "%d.jpg", time));

                FileOutputStream stream = new FileOutputStream(data1);

                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outstream);
                byte[] byteArray = outstream.toByteArray();

                stream.write(byteArray);
                stream.close();

            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            // dbAdapter.setUpdateMessage_MediaName(msg_id,file_name+".jpg");

			/*
			 * Intent login_broadcast = new Intent("chat");
			 * login_broadcast.putExtra("jid", from);
			 * context.getApplicationContext().sendBroadcast(login_broadcast);
			 */

            super.onPostExecute(result);
        }
    }

    // ////////////////////////----Video
    // Download-----//////////////////////////////////////////////////////////////////////
    public class download_video extends AsyncTask<String, String, String> {
        String msg_id, msg_url, data1, from, file_name;

        @Override
        protected void onProgressUpdate(String... values) {
            // TODO Auto-generated method stub
            Log.d("Image Downloading", "Downloading in Progress.." + values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            msg_id = params[0];
            msg_url = params[1];
            from = params[2];
            final long time = System.currentTimeMillis();
            ;
            file_name = "" + time;

            boolean success = (new File(Constant.local_video_dir)).mkdirs();
            if (!success) {
                Log.w("directory not created", "directory not created");
            }

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(60000);
            String[] allowedContentTypes = new String[]{".*"};
            client.get(msg_url,
                    new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] binaryData, Throwable error) {
                            // TODO Auto-generated method stub

                            new AlertManager()
                                    .showAlertDialog(
                                            mParentActivity,
                                            mParentActivity.getResources()
                                                    .getString(
                                                            R.string.sorry_image_downloading_fail),
                                            true);
                            Intent login_broadcast = new Intent("chat");
                            login_broadcast.putExtra("jid", from);
                            mParentActivity.getApplicationContext().sendBroadcast(
                                    login_broadcast);

                            // super.onFailure(statusCode, headers, binaryData,
                            // error);
                        }

						/*
						 * @Override public void onSuccess(byte[] fileData) { //
						 * Do something with the file data1 =
						 * String.valueOf(String.format(
						 * Constant.local_video_dir + "%d.mp4", time));
						 *
						 * try { File file = new File(data1); FileOutputStream
						 * fos = null; try {
						 *
						 * fos = new FileOutputStream(file);
						 *
						 * // Writes bytes from the specified byte array to //
						 * this file output stream fos.write(fileData);
						 *
						 * } catch (FileNotFoundException e) {
						 * //Constant.printMsg("File not found" + e); } catch
						 * (IOException ioe) {
						 * //Constant.printMsg("Exception while writing file "
						 * + ioe); } finally { // close the streams using close
						 * method try { if (fos != null) { fos.close(); } }
						 * catch (IOException ioe) { System.out
						 * .println("Error while closing stream: " + ioe); }
						 *
						 * }
						 *
						 * dbAdapter.setUpdateMessage_MediaName(msg_id,
						 * file_name + ".mp4");
						 *
						 * Intent login_broadcast = new Intent("chat");
						 * login_broadcast.putExtra("jid", from);
						 * context.getApplicationContext().sendBroadcast(
						 * login_broadcast);
						 *
						 * } catch (Exception e) { // TODO: handle exception } }
						 */

                        @Override
                        public void onSuccess(int arg0, Header[] arg1,
                                              byte[] fileData) {
                            // TODO Auto-generated method stub

                            data1 = String.valueOf(String.format(
                                    Constant.local_video_dir + "%d.mp4", time));

                            try {
                                File file = new File(data1);
                                FileOutputStream fos = null;
                                try {

                                    fos = new FileOutputStream(file);

                                    // Writes bytes from the specified byte
                                    // array to
                                    // this file output stream
                                    fos.write(fileData);

                                    bites = (long) file.length();
                                    updateMediaNetwork_Receive(bites);

                                } catch (FileNotFoundException e) {
                                    // Constant.printMsg("File not found" + e);
                                } catch (IOException ioe) {
                                    // Constant.printMsg("Exception while writing file "
                                    // + ioe);
                                } finally {
                                    // close the streams using close method
                                    try {
                                        if (fos != null) {
                                            fos.close();
                                        }
                                    } catch (IOException ioe) {

                                    }

                                }

                                dbAdapter.setUpdateMessage_MediaName(msg_id,
                                        file_name + ".mp4");

                                Intent login_broadcast = new Intent("chat");
                                login_broadcast.putExtra("jid", from);
                                mParentActivity.getApplicationContext().sendBroadcast(
                                        login_broadcast);

                            } catch (Exception e) {
                                // ACRA.getErrorReporter().handleException(e);
                                // TODO: handle exception
                            }
                        }
                    });

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            // dbAdapter.setUpdateMessage_MediaName(msg_id,file_name+".mp4");

			/*
			 * Intent login_broadcast = new Intent("chat");
			 * login_broadcast.putExtra("jid", from);
			 * context.getApplicationContext().sendBroadcast(login_broadcast);
			 */

            super.onPostExecute(result);
        }
    }

    // ////////////////////////----Audio
    // Download-----//////////////////////////////////////////////////////////////////////
    public class download_Audio extends AsyncTask<String, String, String> {
        String msg_id, msg_url, data1, from, file_name;

        @Override
        protected void onProgressUpdate(String... values) {
            // TODO Auto-generated method stub
            Log.d("Image Downloading", "Downloading in Progress.." + values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            msg_id = params[0];
            msg_url = params[1];
            from = params[2];
            final long time = System.currentTimeMillis();
            ;
            file_name = "" + time;

            boolean success = (new File(Constant.local_audio_dir)).mkdirs();
            if (!success) {
                Log.w("directory not created", "directory not created");
            }

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(60000);
            String[] allowedContentTypes = new String[]{".*", "audio/AMR"};
            client.get(msg_url,
                    new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] binaryData, Throwable error) {
                            // TODO Auto-generated method stub

                            new AlertManager()
                                    .showAlertDialog(
                                            mParentActivity,
                                            mParentActivity.getResources()
                                                    .getString(
                                                            R.string.sorry_audio_downloading_fail),
                                            true);
                            Intent login_broadcast = new Intent("chat");
                            login_broadcast.putExtra("jid", from);
                            mParentActivity.getApplicationContext().sendBroadcast(
                                    login_broadcast);

                            // super.onFailure(statusCode, headers, binaryData,
                            // error);
                        }


                        @Override
                        public void onSuccess(int arg0, Header[] arg1,
                                              byte[] fileData) {
                            // TODO Auto-generated method stub
                            data1 = String.valueOf(String.format(
                                    Constant.local_audio_dir + "%d.amr", time));

                            try {
                                File file = new File(data1);
                                FileOutputStream fos = null;
                                try {

                                    fos = new FileOutputStream(file);

                                    // Writes bytes from the specified byte
                                    // array to
                                    // this file output stream
                                    fos.write(fileData);

                                    bites = (long) file.length();
                                    updateMediaNetwork_Receive(bites);

                                } catch (FileNotFoundException e) {
                                    // Constant.printMsg("File not found" + e);
                                } catch (IOException ioe) {
                                    // Constant.printMsg("Exception while writing file "
                                    // + ioe);
                                } finally {
                                    // close the streams using close method
                                    try {
                                        if (fos != null) {
                                            fos.close();
                                        }
                                    } catch (IOException ioe) {

                                    }

                                }

                                dbAdapter.setUpdateMessage_MediaName(msg_id,
                                        file_name + ".amr");

                                Intent login_broadcast = new Intent("chat");
                                login_broadcast.putExtra("jid", from);
                                getApplicationContext().sendBroadcast(
                                        login_broadcast);

                            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                                // TODO: handle exception
                            }
                        }
                    });

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            // dbAdapter.setUpdateMessage_MediaName(msg_id,file_name+".amr");
			/*
			 * Intent login_broadcast = new Intent("chat");
			 * login_broadcast.putExtra("jid", from);
			 * context.getApplicationContext().sendBroadcast(login_broadcast);
			 */

            super.onPostExecute(result);
        }
    }


    void myMethod(String value) {

        mMessegeList=new ArrayList();
        mMeaningList=new ArrayList();
        try {
            Constant.printMsg("Priya Test Nymn mymethod " + value);
            this.Normallist = ACRAConstants.DEFAULT_STRING_VALUE;
            String[] arr = value.split(" ");
            for (String ss : arr) {
                if (!ss.isEmpty()) {
                    Constant.printMsg("Priya Test Nymn ss " + ss);
                    if (ss.contains("<n>") && ss.contains("</n>")
                            && ss.contains("<m>") && ss.contains("</m>")) {
                        String[] subSplits = ss.split("</n>");
                        Constant.printMsg("Priya Test Nymn subSplits "
                                + subSplits);
                        if (subSplits.length > 0) {
                            String subSplitsMeaning = ss.substring(subSplits[0]
                                    .length() + 4);
                            Constant.printMsg("Priya Test Nymn subSplitsMeaning "
                                    + subSplitsMeaning + "  k  " + 0
                                    + "  length " + subSplits[0].length()
                                    + "  "
                                    + ss.substring(subSplits[0].length()));
                            this.Nemelist.add(subSplits[0].substring(3,
                                    subSplits[0].length()));
                            mMessegeList.add(subSplits[0].substring(3,
                                    subSplits[0].length()));

                            if (this.Normallist == null) {
                                this.Normallist += "["
                                        + subSplits[0].substring(3,
                                        subSplits[0].length()) + "]";
                            } else {
                                if (!this.Normallist.isEmpty()) {
                                    this.Normallist += " ";
                                }
                                this.Normallist += "["
                                        + subSplits[0].substring(3,
                                        subSplits[0].length()) + "]";
                            }
//                            this.Meaninglist.add(subSplitsMeaning.substring(3,
//                                    subSplitsMeaning.length() - 4));
                            mMeaningList.add(subSplitsMeaning.substring(3,
                                    subSplitsMeaning.length() - 4));
                            Constant.printMsg("Priya Test Nymn List"
                                    + this.Nemelist + "  " + this.Meaninglist);
                            Constant.printMsg("kkkkk"
                                    + subSplitsMeaning.substring(3,
                                    subSplitsMeaning.length() - 4));
                        }
                    } else {
                        Constant.printMsg("Priya Test Nymn subSplitsMeaning else "
                                + ss);
                        if (this.Normallist.isEmpty()) {
                            this.Normallist += ss;
                        } else {
                            this.Normallist += " " + ss;
                        }
                    }
                }
            }
            Constant.printMsg("OOOOOOOOOOOOOOO"+ mMessegeList
                    + mMeaningList);
            Constant.printMsg("Priya Test Nymn NormalList" + this.Normallist);
        } catch (Exception e) {

        }

    }

    private SpannableStringBuilder addClickablePart(String str, EmojiconTextView mRightChatText) {


        try {
            Constant.printMsg("Priya Test Nymn Spannable" + str + "--"
                    + replacePosition);

            this.ssb = new SpannableStringBuilder(str);
            this.mRemove = str;
            this.idx1 = str.indexOf("[");
            this.idx2 = 0;
            while (this.idx1 != -1) {
                this.idx2 = str.indexOf("]", this.idx1) + 1;
                this.ssb.setSpan(
                        new ClickSpan(str.substring(this.idx1, this.idx2), mRightChatText),
                        this.idx1, this.idx2, 0);
                NoUnderlineSpan noUnderline = new NoUnderlineSpan();
                this.ssb.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#000000")),
                        this.idx1, this.idx2 - 1, 33);
                this.ssb.setSpan(
                        new BackgroundColorSpan(Color.parseColor("#e05a5a")),
                        this.idx1, this.idx2 - 2, 33);
                if (this.bClick) {
                    this.bClick = false;
                }
                this.ssb.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#00123654")),
                        this.idx2 - 2, this.idx2, 33);
                this.ssb.setSpan(new RelativeSizeSpan(0.0f), this.idx2 - 2, this.idx2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                this.ssb.setSpan(noUnderline, this.idx1, this.idx2 - 1, 33);
                this.idx1 = str.indexOf("[", this.idx2);
            }
            String guess = "[";
            String guess1 = "]";
            String[] words = this.Normallist.split("\\s+");
            int index = this.Normallist.indexOf(guess);
            while (index >= 0) {
                this.Normallist = removeCharAt(this.Normallist, index);
                this.ssb.replace(index, index + 1,
                        ACRAConstants.DEFAULT_STRING_VALUE);
                index = this.Normallist.indexOf(guess, index);
            }
            index = this.Normallist.indexOf(guess1);
            while (index >= 0) {
                this.Normallist = removeCharAt(this.Normallist, index);
                this.ssb.replace(index, index + 1,
                        ACRAConstants.DEFAULT_STRING_VALUE);
                index = this.Normallist.indexOf(guess1, index);
            }

            replacePosition = new ArrayList<String>();
            for (int i = 0; i < ssb.toString().length(); i++) {
                char tempCharecter = ssb.toString().charAt(i);
                if (tempCharecter == Constant.mNynmsSpecialCharacter) {
                    replacePosition.add(String.valueOf(i));

                }

            }

            for (int i = 0; i < replacePosition.size(); i++) {
                this.ssb.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#00123654")),
                        Integer.valueOf(replacePosition.get(i).toString()),
                        Integer.valueOf(replacePosition.get(i).toString()) + 1, 33);

            }


        } catch (Exception e) {

        }

        return this.ssb;
    }

    private String removeCharAt(String str, int i) {

        return str.substring(0, i) + str.substring(i + 1);

    }

    // Updating the Shared Preference with Media Receive Network Usage....
    public void updateMediaNetwork_Receive(long upValue) {
        try {
            long val = 0;
            HashMap<String, String> user = mNetworkSharPref
                    .getMedia_ReceiveDetails();

            String value = user.get(NetworkSharedPreference.KEY_MEDIA_GET_RX);
            if (value != null) {

                val = Long.valueOf(value);

            }
            long update = upValue + val;

            String data = String.valueOf(update);

            Constant.printMsg("Media grp val:" + data);

            mNetworkSharPref.setMediaData_Rec(data);
        } catch (NumberFormatException e) {

        }

    }



    // ////////////////////////----Image
    // Download-----//////////////////////////////////////////////////////////////////////


    public void insertzzleDB(ContentValues v) {

        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_ZZLE, null, v);

            Constant.printMsg("No of inserted rows in shop details :::::::::"
                    + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in new shop details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }

    }

    protected void insertToLEDDB(ContentValues cv) {
        // TODO Auto-generated method stub
        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_LED, null, cv);
            Constant.printMsg("No of inserted rows in zzle :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in ecpl details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }
    }



    public void intializationElements(int side)
    {
        try {
            MEDIA_UPLOAD_URL = KachingMeConfig.UPLOAD_MEDIA;
            mNetworkSharPref = new NetworkSharedPreference(mParentActivity);

            db = new Dbhelper(mParentActivity);
            SharedPreferences sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(mParentActivity);

            mNetworkSharPref = new NetworkSharedPreference(mParentActivity);

            font_size = Integer.parseInt(sharedPrefs.getString("pref_font_size",
                    "16"));

            if (font_size == 14) {
                notificatiob_font_size = 10;
                status_font_siz = 10;
                msg_font_size = 14;
            } else if (font_size == 18) {
                notificatiob_font_size = 14;
                status_font_siz = 14;
                msg_font_size = 18;
            }

            dbAdapter = KachingMeApplication.getDatabaseAdapter();

            is_auto_dowload_image = sharedPrefs.getBoolean(
                    "media_auto_download_images", false);
            is_auto_dowload_video = sharedPrefs.getBoolean(
                    "media_auto_download_videos", false);
            is_auto_dowload_audio = sharedPrefs.getBoolean(
                    "media_auto_download_audio", false);
            is_auto_dowload_files = sharedPrefs.getBoolean(
                    "media_auto_download_files", false);


            new File(Constant.local_image_dir).mkdirs();
            new File(Constant.local_video_dir).mkdirs();
            new File(Constant.local_audio_dir).mkdirs();
            new File(Constant.local_files_dir).mkdirs();
            mSelectedItemsIds = new SparseBooleanArray();


            Constant.printMsg("MUCTest adapter item count constructor :" + msg_list.size());

            try {
                byte[] img_byte = KachingMeApplication.getAvatar();
                if (img_byte != null) {
                    mSenderPhoto = BitmapFactory.decodeByteArray(
                            img_byte, 0, img_byte.length,Util.getBitmapOptions());
                    mSenderPhoto = new AvatarManager()
                            .roundCornerImage(mSenderPhoto, 180);

                    if (mSenderPhoto.getByteCount() < 5) {

                        Bitmap largeIcon = BitmapFactory.decodeResource(mParentActivity.getResources(), R.drawable.avtar);
                        mSenderPhoto = largeIcon;

                    }

                    //                            arg0.mRightSenderImage.setImageBitmap(new AvatarManager()
                    //                                    .roundCornerImage(bmp, 180));
                }else{
                    Bitmap largeIcon = BitmapFactory.decodeResource(mParentActivity.getResources(), R.drawable.avtar);
                    mSenderPhoto = largeIcon;
                }
            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);

            }


            if(side==1) {

                //Right...
                mRightTipLayout = new FrameLayout(mParentActivity);
                mRightTipLayout.setId(k+200000);
                mRightTipLayout.setFocusable(true);
                mRightTipLayout.setFocusableInTouchMode(true);

                LinearLayout.LayoutParams mRightTipLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mRightTipLayout.setLayoutParams(mRightTipLayoutParams);

                mRightChatLayout = new LinearLayout(mParentActivity);
                mRightChatLayout.setGravity(Gravity.RIGHT);
                mRightChatLayout.setOrientation(LinearLayout.VERTICAL);
                mRightChatLayout.setBackgroundResource(R.drawable.shadow_effect);

                FrameLayout.LayoutParams mRightChatLayoutParams = new FrameLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mRightChatLayoutParams.width = (int) width * 66 / 100;
                mRightChatLayoutParams.leftMargin = (int) width * 19 / 100;
                mRightChatLayout.setLayoutParams(mRightChatLayoutParams);

                FrameLayout.LayoutParams mTipImageParams = new FrameLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mTipImageParams.width = (int) width * 5 / 100;
                mTipImageParams.height = (int) width * 6 / 100;
                mTipImageParams.leftMargin = (int) width * 83 / 100;

                ImageView mTipImage = new ImageView(mParentActivity);
                mTipImage.setBackgroundResource(R.drawable.chat_tip);
                mTipImage.setLayoutParams(mTipImageParams);

                FrameLayout.LayoutParams mRoundImageParams = new FrameLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mRoundImageParams.width = (int) width * 11 / 100;
                mRoundImageParams.height = (int) width * 11 / 100;
                mRoundImageParams.topMargin=height*1/100;
                mRoundImageParams.leftMargin = (int) width * 88 / 100;

                ImageView mSenderRoundImage = new ImageView(mParentActivity);
                mSenderRoundImage.setLayoutParams(mRoundImageParams);
                System.gc();

                if(KachingMeApplication.getAvatar()!=null) {

                    byte[] img_byte = KachingMeApplication.getAvatar();
                    Bitmap bmp = BitmapFactory.decodeByteArray(
                            KachingMeApplication.getAvatar(), 0,
                            KachingMeApplication.getAvatar().length,Util.getBitmapOptions());

                    if (bmp != null) {

                        ProfileRoundImg mSenderImage = new ProfileRoundImg(bmp);
                        mSenderRoundImage.setImageDrawable(mSenderImage);
                    } else {

                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2,Util.getBitmapOptions());
                        ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                        mSenderRoundImage.setImageDrawable(mSenderImage);
                    }
                }else{

                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2,Util.getBitmapOptions());
                    ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                    mSenderRoundImage.setImageDrawable(mSenderImage);
                }

                //            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2);
                //            ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                //
                //            ImageView mSenderRoundImage = new ImageView(mParentActivity);
                //            mSenderRoundImage.setImageDrawable(mSenderImage);
                //            mSenderRoundImage.setLayoutParams(mRoundImageParams);

                mRightTipLayout.addView(mTipImage);
                mRightTipLayout.addView(mSenderRoundImage);

            }
            else {

                // Left...

                mLeftTextProfile = new EmojiconTextView(mParentActivity);
                mLeftTextProfile.setGravity(Gravity.LEFT);
                mLeftTextProfile.setTextColor(Color.parseColor("#CC494040"));
                mLeftTextProfile.setPadding((int) width * 2 / 100, 0, (int) width * 2/ 100, (int)width *1 / 100);

                mRightTipLayout = new FrameLayout(mParentActivity);
                mRightTipLayout.setId(k+200000);
                mRightTipLayout.setFocusable(true);
                mRightTipLayout.setFocusableInTouchMode(true);

                LinearLayout.LayoutParams mRightTipLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mRightTipLayout.setLayoutParams(mRightTipLayoutParams);

                mRightChatLayout = new LinearLayout(mParentActivity);
                mRightChatLayout.setGravity(Gravity.RIGHT);
                mRightChatLayout.setOrientation(LinearLayout.VERTICAL);
                mRightChatLayout.setBackgroundResource(R.drawable.shadow_effect_left);

                FrameLayout.LayoutParams mRightChatLayoutParams = new FrameLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mRightChatLayoutParams.width = (int) width * 65 / 100;
                mRightChatLayoutParams.leftMargin = (int) width * 16 / 100;
                mRightChatLayout.setLayoutParams(mRightChatLayoutParams);

                FrameLayout.LayoutParams mTipImageParams = new FrameLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mTipImageParams.width = (int) width * 5 / 100;
                mTipImageParams.height = (int) width * 6 / 100;
                mTipImageParams.leftMargin = (int) width * 12 / 100;

                ImageView mTipImage = new ImageView(mParentActivity);
                mTipImage.setBackgroundResource(R.drawable.left_chat_tip);
                mTipImage.setLayoutParams(mTipImageParams);

                FrameLayout.LayoutParams mRoundImageParams = new FrameLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mRoundImageParams.width = (int) width * 11 / 100;
                mRoundImageParams.height = (int) width * 11 / 100;
                mRoundImageParams.topMargin=height*1/100;
                mRoundImageParams.leftMargin = (int) width * 1 / 100;

                Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher2);
                ProfileRoundImg mSenderImage=null;

                ImageView mSenderRoundImage=new ImageView(mParentActivity);
                mSenderRoundImage.setLayoutParams(mRoundImageParams);

                ContactsGetSet contact = new ContactsGetSet();
                try {
                    contact = dbAdapter.getContact(msg_list.get(k).getRemote_resource());


                } catch (Exception e) {
                    // ACRA.getErrorReporter().handleException(e);
                    // TODO: handle exception
                }

                try {
                    System.gc();
                    byte[] img_byte = contact.getPhoto_ts();
                    Constant.printMsg("kldfksjfks"+img_byte.length);
                        if (img_byte != null) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(
                                    img_byte, 0, img_byte.length, Util.getBitmapOptions());
                            mSenderImage = new ProfileRoundImg(bmp);
                            mSenderRoundImage.setImageDrawable(mSenderImage);

                            //                    mSenderRoundImage.setImageBitmap(new AvatarManager()
                            //                            .roundCornerImage(bmp, 180));
                        } else {
                            bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2, Util.getBitmapOptions());
                            mSenderImage = new ProfileRoundImg(bm);
                            mSenderRoundImage.setImageDrawable(mSenderImage);
                        }
                    }
                 catch (Exception e) {// ACRA.getErrorReporter().handleException(e);

                }



                mRightTipLayout.addView(mTipImage);
                mRightTipLayout.addView(mSenderRoundImage);

                if (width >= 600) {
                    mLeftTextProfile.setTextSize(14);
                    mLeftTextProfile.setEmojiconSize(48);

                } else if (width > 501 && width < 600) {


                    mLeftTextProfile.setTextSize(12);
                    mLeftTextProfile.setEmojiconSize(46);


                } else if (width > 331 && width < 500) {


                    mLeftTextProfile.setTextSize(10);
                    mLeftTextProfile.setEmojiconSize(44);

                } else if (width > 260 && width < 330) {


                    mLeftTextProfile.setTextSize(10);
                    mLeftTextProfile.setEmojiconSize(42);

                } else if (width <= 260) {


                    mLeftTextProfile.setTextSize(9);
                    mLeftTextProfile.setEmojiconSize(42);

                }

            }

            mChatHedderCopyLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mIsLogClick) {
                        String copiedtext = "";
                        int j = 0;
                        ArrayList<MessageGetSet> mTempCopy=new ArrayList<MessageGetSet>();
                        if (mOnLongSelectedPostions != null) {
                            for (int i = 0; i < mOnLongSelectedPostions.size(); i++) {

    /*
                                    Log.i("Selected", mOnLongSelectedPostions.get(i)
                                            + " was selected ::" + msg_list.size());
                                    Log.i("Select",
                                            "Selected ID::"
                                                    + msg_list.get(mOnLongSelectedPostions.get(i))
                                                    .get_id());*/
                                if (j == 0) {
//                                    copiedtext = msg_list.get(mOnLongSelectedPostions.get(i))
//                                            .getData();
                                    mTempCopy.add(msg_list.get(mOnLongSelectedPostions.get(i)));
                                } else {
//                                    copiedtext = copiedtext
//                                            + "\n"
//                                            + msg_list.get(mOnLongSelectedPostions.get(i))
//                                            .getData();
                                    mTempCopy.add(msg_list.get(mOnLongSelectedPostions.get(i)));
                                }
                                j++;

                            }
                        }

                        Collections.sort(mTempCopy, new Comparator<MessageGetSet>() {
                            @Override
                            public int compare(MessageGetSet lhs, MessageGetSet rhs) {
                                return String.valueOf(lhs.get_id()).compareTo(String.valueOf(rhs.get_id()));
                            }

                        });

                        copiedtext="";

                        for(int i=0;i<mTempCopy.size();i++){

                            copiedtext+=mTempCopy.get(i).getData()+ "\n";

                        }

                        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                        if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                          /*
                           * ClipData clip = ClipData.newPlainText("label",
                           * "Text to Copy"); clipboard.setPrimaryClip(clip);
                           */
                            clipboard.setText(copiedtext);

                        } else {
                            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            clipboard.setText(copiedtext);
                        }

                        Toast.makeText(mParentActivity,"Copied to clipboard", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < mOnLongSelectedPostions.size(); i++) {

                            mRightTipLayout = (FrameLayout) findViewById(Integer.valueOf(mOnLongSelectedPostions.get(i)) + 200000);
                            {

                                mRightTipLayout.setBackgroundColor(Color.parseColor("#00000000"));
                                // mOnLongSelectedPostions.remove(i);
                            }


                        }
                        //change hedder
                        mOnLongSelectedPostions.clear();
                        mIsLogClick = false;
                        mChatHedderAttachmentImg.setBackgroundResource(R.drawable.clip);
                        mChatHedderMenuImg.setBackgroundResource(R.drawable.menu_right);

                        LinearLayout.LayoutParams hedderTextParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        hedderTextParams.width = width * 50 / 100;
                        hedderTextParams.leftMargin = width * 3 / 100;
                        hedderTextParams.gravity = Gravity.CENTER_VERTICAL;
                        mChatHedderTextLayout.setLayoutParams(hedderTextParams);


                        LinearLayout.LayoutParams hedderAttachmentParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        hedderAttachmentParams.width = width * 4 / 100;
                        hedderAttachmentParams.height = width * 8 / 100;
                        hedderAttachmentParams.gravity = Gravity.CENTER;
                        mChatHedderAttachmentImg.setLayoutParams(hedderAttachmentParams);

                        LinearLayout.LayoutParams hedderMenuParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        hedderMenuParams.width = (width * 2 / 100) - 2;
                        hedderMenuParams.height = width * 7 / 100;
                        hedderMenuParams.gravity = Gravity.CENTER;
                        mChatHedderMenuImg.setLayoutParams(hedderMenuParams);

                        mChatHedderCopyLayout.setVisibility(View.GONE);
                    }
                }
            });

            mRightTipLayout.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    v.requestFocus();
                    v.setFocusableInTouchMode(true);

                    if(!mIsLogClick) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }

                    return false;
                }
            });

            mRightTipLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    topMenuHideFunction();

                    if(mIsLogClick){

                        String mediaType = msg_list.get(v.getId()-200000).getMedia_wa_type();
                        String splText = msg_list.get(v.getId()-200000).getData();

                        if(mOnLongSelectedPostions.contains(v.getId()-200000)){
                            boolean isOneMedia = false;
                            boolean isSple = false;
                            for(int i=0;i<mOnLongSelectedPostions.size();i++){

                                if(!getCopyPastMediaType(mediaType))
                                {
                                    isOneMedia = true;
                                    Constant.printMsg("Dilip copy loop" + " " + isOneMedia);
                                }

                                else  if(chekNynmDazzKon(splText))
                                {
                                    isSple = true;
                                }

                                Constant.printMsg("JJJJJJJJIO1112222"+v.getId()+"    "+mOnLongSelectedPostions.get(i).toString()+"   "+String.valueOf(v.getId()-200000));

                                if(mOnLongSelectedPostions.get(i) == v.getId()-200000){

                                    Constant.printMsg("JJJJJJJJIO111111"+v.getId()+"    "+mOnLongSelectedPostions);
                                    v.setBackgroundColor(Color.parseColor("#00000000"));
                                    mOnLongSelectedPostions.remove(i);
                                    isOneMedia = false;
                                    isSple = false;

                                }
                            }

                            if(isOneMedia)
                            {
                                mChatHedderCopyLayout.setVisibility(View.GONE);
                            }else
                            {
                                mChatHedderCopyLayout.setVisibility(View.VISIBLE);
                            }

                            if(isSple)
                            {
                                mChatHedderCopyLayout.setVisibility(View.GONE);
                            }else
                            {
                                mChatHedderCopyLayout.setVisibility(View.VISIBLE);
                            }

                        }else{

                            if(!getCopyPastMediaType(mediaType))
                            {
                                mChatHedderCopyLayout.setVisibility(View.GONE);
                            }

                            else if(chekNynmDazzKon(splText))
                            {
                                mChatHedderCopyLayout.setVisibility(View.GONE);
                            }

                            mOnLongSelectedPostions.add((v.getId()-200000));
                            v.setBackgroundColor(Color.parseColor("#FFFFD98F"));

                        }

                        if(mOnLongSelectedPostions.size()<=0){

                            mOnLongSelectedPostions.clear();
                            mIsLogClick=false;
                            mChatHedderAttachmentImg.setBackgroundResource(R.drawable.clip);
                            mChatHedderMenuImg.setBackgroundResource(R.drawable.menu_right);

                            LinearLayout.LayoutParams hedderTextParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            hedderTextParams.width = width * 50 / 100;
                            hedderTextParams.leftMargin = width * 3 / 100;
                            hedderTextParams.gravity = Gravity.CENTER_VERTICAL;
                            mChatHedderTextLayout.setLayoutParams(hedderTextParams);


                            LinearLayout.LayoutParams hedderAttachmentParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            hedderAttachmentParams.width = width * 4 / 100;
                            hedderAttachmentParams.height = width * 8 / 100;
                            hedderAttachmentParams.gravity = Gravity.CENTER;
                            mChatHedderAttachmentImg.setLayoutParams(hedderAttachmentParams);

                            LinearLayout.LayoutParams hedderMenuParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            hedderMenuParams.width = (width * 2 / 100) - 2;
                            hedderMenuParams.height = width * 7 / 100;
                            hedderMenuParams.gravity = Gravity.CENTER;
                            mChatHedderMenuImg.setLayoutParams(hedderMenuParams);

                            mChatHedderCopyLayout.setVisibility(View.GONE);
                        }
                    }else{
                        edt_msg.requestFocus();
                    }



                }
            });


            mRightTipLayout.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    topMenuHideFunction();

                    mIsLogClick=true;

                    LinearLayout.LayoutParams hedderTextParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    hedderTextParams.width = width * 40 / 100;
                    hedderTextParams.leftMargin = width * 3 / 100;
                    hedderTextParams.gravity = Gravity.CENTER_VERTICAL;
                    mChatHedderTextLayout.setLayoutParams(hedderTextParams);

                    LinearLayout.LayoutParams hedderAttachmentParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    hedderAttachmentParams.width = width * 11 / 100;
                    hedderAttachmentParams.height = width * 8 / 100;
                    hedderAttachmentParams.gravity = Gravity.CENTER;
                    mChatHedderAttachmentImg.setLayoutParams(hedderAttachmentParams);
                    mChatHedderMenuImg.setLayoutParams(hedderAttachmentParams);
                    mChatHedderCopyImg.setLayoutParams(hedderAttachmentParams);

                    mChatHedderCopyLayout.setVisibility(View.VISIBLE);


                    mChatHedderAttachmentImg.setBackgroundResource(R.drawable.ic_action_content_discard);
                    mChatHedderMenuImg.setBackgroundResource(R.drawable.ic_action_social_forward);

                    Constant.printMsg("JJJJJJJJIO"+v.getId()+"    "+mOnLongSelectedPostions+"   "+String.valueOf(v.getId()-200000));

                    String mediaType = msg_list.get(v.getId()-200000).getMedia_wa_type();
                    String splText = msg_list.get(v.getId()-200000).getData();

                    if(mOnLongSelectedPostions.contains((v.getId()-200000))){

                        boolean isOneMedia = false;
                        boolean isSple = false;

                        for(int i=0;i<mOnLongSelectedPostions.size();i++){

                            Constant.printMsg("JJJJJJJJIO1112222"+v.getId()+"    "+mOnLongSelectedPostions.get(i).toString()+"   "+String.valueOf(v.getId()-200000));
                            if(!getCopyPastMediaType(mediaType))
                            {
                                isOneMedia = true;
                                Constant.printMsg("Dilip copy loop" + " " + isOneMedia);
                            }

                            else if(chekNynmDazzKon(splText))
                            {
                                isSple = true;
                            }
                            if(mOnLongSelectedPostions.get(i)== (v.getId()-200000)){

                                Constant.printMsg("JJJJJJJJIO111111"+v.getId()+"    "+mOnLongSelectedPostions);
                                v.setBackgroundColor(Color.parseColor("#00000000"));
                                mOnLongSelectedPostions.remove(i);

                                isOneMedia = false;

                                isSple = false;

                            }
                        }

                        if(isOneMedia)
                        {
                            mChatHedderCopyLayout.setVisibility(View.GONE);
                        }else
                        {
                            mChatHedderCopyLayout.setVisibility(View.VISIBLE);
                        }

                        if(isSple)
                        {
                            mChatHedderCopyLayout.setVisibility(View.GONE);
                        }else
                        {
                            mChatHedderCopyLayout.setVisibility(View.VISIBLE);
                        }

                    }else{

                        if(!getCopyPastMediaType(mediaType))
                        {
                            mChatHedderCopyLayout.setVisibility(View.GONE);
                        }
                        else if(chekNynmDazzKon(splText))
                        {
                            mChatHedderCopyLayout.setVisibility(View.GONE);
                        }

                        mOnLongSelectedPostions.add((v.getId()-200000));
                        v.setBackgroundColor(Color.parseColor("#FFFFD98F"));

                    }

                    return true;
                }
            });
        } catch (NumberFormatException e) {

        }

    }



    void rightTextChat(){

        try {
            mRightTextview = new EmojiconTextView(MUCTest.this);
            mRightTextview.setGravity(Gravity.LEFT);
            mRightTextview.setTextColor(Color.parseColor("#000000"));
            mRightTextview.setPadding((int) width * 3 / 100, width * 1 / 100, (int) width * 3 / 100, width * 2 / 100);
            mRightTextview.setId(k);

            mRightSenderTimeText = new TextView(MUCTest.this);
            mRightSenderTimeText.setGravity(Gravity.LEFT);
            mRightSenderTimeText.setTextSize(getTimeTxtSize());
            mRightSenderTimeText.setTextColor(Color.parseColor("#000000"));
            mRightSenderTimeText.setPadding((int) width * 1 / 100, 0, (int) width * 3 / 100, width * 1 / 100);


            LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mRightTextview.setLayoutParams(textviewParams);
            mRightSenderTimeText.setLayoutParams(textviewParams);


            LinearLayout textFooterLayout = new LinearLayout(MUCTest.this);
            textFooterLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams textfooterParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            textfooterParams.topMargin = width * 1 / 100;
            textFooterLayout.setLayoutParams(textfooterParams);


            LinearLayout.LayoutParams mRightTickMarkParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mRightTickMarkParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

            mRightTickMark = new ImageView(MUCTest.this);
            mRightTickMark.setId(k + 600000);
            mRightTickMark.setLayoutParams(mRightTickMarkParams);
            mRightTickMark.setPadding((int) width * 2 / 100, 0, (int) width * 1 / 100, width * 1 / 100);


            FrameLayout.LayoutParams right_bomb_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            right_bomb_params.width = (int) width * 10 / 100;
            right_bomb_params.height = (int) width * 10 / 100;
            right_bomb_params.gravity = Gravity.CENTER_VERTICAL;
            right_bomb_params.leftMargin = width * 5 / 100;


            mRightDestTime = new TextView(MUCTest.this);
            mRightDestTime.setBackgroundResource(R.drawable.black_bomb);
            mRightDestTime.setGravity(Gravity.CENTER);
            mRightDestTime.setTextColor(Color.parseColor("#ffffff"));
            mRightDestTime.setLayoutParams(right_bomb_params);
            mRightDestTime.setVisibility(View.INVISIBLE);
            mRightDestTime.setId(k + 100000);

            textFooterLayout.addView(mRightTickMark);
            textFooterLayout.addView(mRightSenderTimeText);
            mRightChatLayout.addView(mRightTextview);
            mRightChatLayout.addView(textFooterLayout);
            mRightTipLayout.addView(mRightDestTime);
            mRightTipLayout.addView(mRightChatLayout);

            mRightTipLayout.setPadding(0, 3, 0, 3);

            mDynamicView.addView(mRightTipLayout);

            if (width >= 600) {

                mRightTextview.setTextSize(18);
                mRightTextview.setEmojiconSize(48);

            } else if (width > 501 && width < 600) {

                mRightTextview.setTextSize(16);
                mRightTextview.setEmojiconSize(46);


            } else if (width > 331 && width < 500) {

                mRightTextview.setTextSize(14);
                mRightTextview.setEmojiconSize(44);

            } else if (width > 260 && width < 330) {

                mRightTextview.setTextSize(13);
                mRightTextview.setEmojiconSize(42);

            } else if (width <= 260) {

                mRightTextview.setTextSize(12);
                mRightTextview.setEmojiconSize(42);

            }
        } catch (Exception e) {

        }


    }



    void rightImageChat(){

        try {
            mRightImageChat = new ImageView(mParentActivity);
            mRightImageChat.setPadding((int) width * 3 / 100, 0, (int) width * 1 / 100, width * 2 / 100);
            mRightImageChat.setScaleType(ImageView.ScaleType.CENTER_CROP);

            mRightImageTextTime=new TextView(mParentActivity);
            mRightImageTextTime.setGravity(Gravity.RIGHT);
            mRightImageTextTime.setTextSize(getTimeTxtSize());
            mRightImageTextTime.setTextColor(Color.parseColor("#000000"));
            mRightImageTextTime.setPadding((int) width * 1 / 100, 0, (int) width * 3 / 100, width * 1 / 100);
            //        mRightSenderTimeText.setText(time);

            FrameLayout.LayoutParams textviewParams = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textviewParams.width=  width * 57 / 100;
            textviewParams.height= height * 30 / 100;
            textviewParams.topMargin=width * 2 / 100;
            textviewParams.gravity=Gravity.CENTER;
            mRightImageChat.setLayoutParams(textviewParams);
            //        mRightSenderTimeText.setLayoutParams(textviewParams);

            FrameLayout ImachChatLayout=new FrameLayout(mParentActivity);

            mRightImageChatUpload = new ImageView(mParentActivity);
            mRightImageChatUpload.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mRightImageChatUpload.setBackgroundResource(R.drawable.image_upload_normal);

            mRightImageChatCancel = new ImageView(mParentActivity);
            mRightImageChatCancel.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mRightImageChatCancel.setBackgroundResource(R.drawable.ic_action_content_remove);

            mRightImageProgress=new ProgressBar(mParentActivity);

            FrameLayout.LayoutParams left_download_icon_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            left_download_icon_params.width = (int) width * 15/100;
            left_download_icon_params.height = (int) width *15/100;
            left_download_icon_params.gravity=Gravity.CENTER;
            mRightImageChatUpload.setLayoutParams(left_download_icon_params);
            mRightImageChatCancel.setLayoutParams(left_download_icon_params);


            FrameLayout.LayoutParams left_download_progres_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            left_download_progres_params.width = (int) width * 16/100;
            left_download_progres_params.height = (int) width *16/100;
            left_download_progres_params.gravity=Gravity.CENTER;
            mRightImageProgress.setLayoutParams(left_download_progres_params);


            ImachChatLayout.addView(mRightImageChat);
            ImachChatLayout.addView(mRightImageChatUpload);
            ImachChatLayout.addView(mRightImageChatCancel);
            ImachChatLayout.addView(mRightImageProgress);


            LinearLayout textFooterLayout=new LinearLayout(mParentActivity);
            textFooterLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams textfooterParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textFooterLayout.setLayoutParams(textfooterParams);

            LinearLayout.LayoutParams mRightTickMarkParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mRightTickMarkParams.gravity=Gravity.LEFT|Gravity.CENTER_VERTICAL;

            mRightImageTickMark=new ImageView(mParentActivity);
            mRightImageTickMark.setId(k+600000);
            mRightImageTickMark.setLayoutParams(mRightTickMarkParams);
            mRightImageTickMark.setPadding((int) width * 2 / 100, 0, (int) width * 1 / 100, width * 1 / 100);

            textFooterLayout.addView(mRightImageTickMark);
            textFooterLayout.addView(mRightImageTextTime);

            mRightChatLayout.addView(ImachChatLayout);
            mRightChatLayout.addView(textFooterLayout);
            mRightTipLayout.addView(mRightChatLayout);

            mRightTipLayout.setPadding(0, 3, 0, 3);

            mDynamicView.addView(mRightTipLayout);
        } catch (Exception e) {

        }

    }


    void rightVideoChat(){

        try {
            mRightVideoChat = new ImageView(mParentActivity);
            mRightVideoChat.setPadding((int) width * 1 / 100, 0, (int) width * 1 / 100, width * 2 / 100);
            mRightVideoChat.setScaleType(ImageView.ScaleType.CENTER_CROP);

            mRightVideoTimeText=new TextView(mParentActivity);
            mRightVideoTimeText.setGravity(Gravity.RIGHT);
            mRightVideoTimeText.setTextSize(getTimeTxtSize());
            mRightVideoTimeText.setTextColor(Color.parseColor("#000000"));
            mRightVideoTimeText.setPadding((int) width * 1 / 100, width * 1 / 100, (int) width * 3 / 100, width * 1 / 100);
            //        mRightSenderTimeText.setText(time);


            FrameLayout.LayoutParams mLeftVideoChatParams = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftVideoChatParams.width=  width * 59 / 100;
            mLeftVideoChatParams.height= height * 30 / 100;
            mLeftVideoChatParams.topMargin=width * 2 / 100;
            mLeftVideoChatParams.gravity=Gravity.CENTER;
            mRightVideoChat.setLayoutParams(mLeftVideoChatParams);
            //        mRightSenderTimeText.setLayoutParams(textviewParams);

            FrameLayout mLeftVideoChatLayout=new FrameLayout(mParentActivity);

            mRightVideoChatUpload = new ImageView(mParentActivity);
            mRightVideoChatUpload.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mRightVideoChatUpload.setBackgroundResource(R.drawable.image_upload_normal);




            mRightVideoChatCancel = new ImageView(mParentActivity);
            mRightVideoChatCancel.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mRightVideoChatCancel.setBackgroundResource(R.drawable.ic_action_content_remove);

            mRightVideoButtonPlay = new ImageView(mParentActivity);
            mRightVideoButtonPlay.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mRightVideoButtonPlay.setBackgroundResource(R.drawable.videooverlay);


            mRightVideoProgress=new ProgressBar(mParentActivity);

            FrameLayout.LayoutParams left_download_icon_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            left_download_icon_params.width = (int) width * 15/100;
            left_download_icon_params.height = (int) width *15/100;
            left_download_icon_params.gravity=Gravity.CENTER;
            mRightVideoChatUpload.setLayoutParams(left_download_icon_params);
            mRightVideoChatCancel.setLayoutParams(left_download_icon_params);
            mRightVideoButtonPlay.setLayoutParams(left_download_icon_params);

            FrameLayout.LayoutParams left_download_progress_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            left_download_progress_params.width = (int) width * 16/100;
            left_download_progress_params.height = (int) width *16/100;
            left_download_progress_params.gravity=Gravity.CENTER;
            mRightVideoProgress.setLayoutParams(left_download_progress_params);


            FrameLayout.LayoutParams videoFooter_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            videoFooter_params.gravity=Gravity.BOTTOM|Gravity.CENTER_VERTICAL;
            videoFooter_params.topMargin=width*3/100;

            FrameLayout videoFooter=new FrameLayout(mParentActivity);
            videoFooter.setLayoutParams(videoFooter_params);

            FrameLayout.LayoutParams videoimage = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            videoimage.width=width*10/100;
            videoimage.height=width*8/100;
            videoimage.topMargin=width*1/100;
            videoimage.gravity=Gravity.CENTER_VERTICAL;
            videoimage.leftMargin=width*3/100;

            mRightVideoImgOverlay=new ImageView(mParentActivity);
            mRightVideoImgOverlay.setBackgroundResource(R.drawable.frame_overlay_gallery_video);
            mRightVideoImgOverlay.setLayoutParams(videoimage);

            FrameLayout.LayoutParams videoDuration_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            videoDuration_params.gravity=Gravity.LEFT;
            videoDuration_params.topMargin=width*3/100;
            videoDuration_params.leftMargin=width*12/100;

            mRightVideoDuration=new TextView(mParentActivity);
            mRightVideoDuration.setTextColor(Color.parseColor("#ffffff"));
            mRightVideoDuration.setText("1");
            mRightVideoDuration.setLayoutParams(videoDuration_params);

            FrameLayout.LayoutParams videoSize_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            videoSize_params.gravity=Gravity.RIGHT;
            videoSize_params.topMargin=width*3/100;
            videoSize_params.rightMargin=width*5/100;


            mRightVideoSize=new TextView(mParentActivity);
            mRightVideoSize.setTextColor(Color.parseColor("#ffffff"));
            mRightVideoSize.setText("1");
            mRightVideoSize.setLayoutParams(videoSize_params);

            videoFooter.addView(mRightVideoImgOverlay);
            videoFooter.addView(mRightVideoDuration);
            videoFooter.addView(mRightVideoSize);


            mLeftVideoChatLayout.addView(mRightVideoChat);
            mLeftVideoChatLayout.addView(mRightVideoChatUpload);
            mLeftVideoChatLayout.addView(mRightVideoChatCancel);
            mLeftVideoChatLayout.addView(mRightVideoProgress);
            mLeftVideoChatLayout.addView(mRightVideoButtonPlay);
            mLeftVideoChatLayout.addView(videoFooter);

            LinearLayout textFooterLayout=new LinearLayout(mParentActivity);
            textFooterLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams textfooterParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textFooterLayout.setLayoutParams(textfooterParams);

            LinearLayout.LayoutParams mRightTickMarkParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mRightTickMarkParams.gravity=Gravity.LEFT|Gravity.CENTER_VERTICAL;

            mRightVideoTickMark=new ImageView(mParentActivity);
            mRightVideoTickMark.setId(k+600000);
            mRightVideoTickMark.setLayoutParams(mRightTickMarkParams);
            mRightVideoTickMark.setPadding((int) width * 2 / 100, width * 1 / 100, (int) width * 1 / 100, width * 1 / 100);

            textFooterLayout.addView(mRightVideoTickMark);
            textFooterLayout.addView(mRightVideoTimeText);

            mRightChatLayout.addView(mLeftVideoChatLayout);
            mRightChatLayout.addView(textFooterLayout);
            mRightTipLayout.addView(mRightChatLayout);

            mRightTipLayout.setPadding(0, 3, 0, 3);

            mDynamicView.addView(mRightTipLayout);
        } catch (Exception e) {

        }


    }

    void rightContactChat(){

        try {
            LinearLayout.LayoutParams contactLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            contactLayoutParams.width=width*50/100;
            contactLayoutParams.height=height*8/100;
            contactLayoutParams.topMargin=width*2/100;
            contactLayoutParams.gravity=Gravity.CENTER;

            LinearLayout contactLayout=new LinearLayout(mParentActivity);
            contactLayout.setOrientation(LinearLayout.HORIZONTAL);
            contactLayout.setBackgroundColor(Color.parseColor("#80ffffff"));
            contactLayout.setLayoutParams(contactLayoutParams);

            LinearLayout.LayoutParams contactAvathorParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            contactAvathorParams.width=width*10/100;
            contactAvathorParams.height=width*10/100;
            contactAvathorParams.leftMargin=width*5/100;

            mRightContactAvathor=new ImageView(mParentActivity);
            mRightContactAvathor.setBackgroundResource(R.drawable.silhouette121);
            mRightContactAvathor.setLayoutParams(contactAvathorParams);

            LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textviewParams.gravity=Gravity.CENTER_VERTICAL;

            mRightContactTextView= new EmojiconTextView(mParentActivity);
            mRightContactTextView.setGravity(Gravity.CENTER_VERTICAL);
            mRightContactTextView.setTextColor(Color.parseColor("#000000"));
            mRightContactTextView.setTypeface(Typeface.DEFAULT_BOLD);
            // textview.setPadding((int) width * 3 / 100, 0, (int) width * 3 / 100, width * 2 / 100);
            //        mRightContactTextView.setText(text);
            mRightContactTextView.setLayoutParams(textviewParams);


            mRightContactTextTime=new TextView(mParentActivity);
            mRightContactTextTime.setGravity(Gravity.LEFT);
            mRightContactTextTime.setTextSize(getTimeTxtSize());
            mRightContactTextTime.setTextColor(Color.parseColor("#000000"));
            mRightContactTextTime.setPadding((int) width * 1 / 100, width * 1 / 100, (int) width * 3 / 100, width * 1 / 100);
            //        mRightSenderTimeText.setText(time);

            LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mRightContactTextTime.setLayoutParams(timeParams);
            mRightContactAvathor.setLayoutParams(timeParams);

            contactLayout.addView(mRightContactAvathor);
            contactLayout.addView(mRightContactTextView);

            LinearLayout textFooterLayout=new LinearLayout(mParentActivity);
            textFooterLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams textfooterParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textFooterLayout.setLayoutParams(textfooterParams);

            LinearLayout.LayoutParams mRightTickMarkParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mRightTickMarkParams.gravity=Gravity.LEFT|Gravity.CENTER_VERTICAL;

            mRightContactTickMark=new ImageView(mParentActivity);
            mRightContactTickMark.setId(k+600000);
            mRightContactTickMark.setLayoutParams(mRightTickMarkParams);
            mRightContactTickMark.setPadding((int) width * 2 / 100, width * 1 / 100, (int) width * 1 / 100, width * 1 / 100);

            textFooterLayout.addView(mRightContactTickMark);
            textFooterLayout.addView(mRightContactTextTime);


            mRightChatLayout.addView(contactLayout);
            mRightChatLayout.addView(textFooterLayout);
            mRightTipLayout.addView(mRightChatLayout);

            mRightTipLayout.setPadding(0, 3, 0, 3);

            mDynamicView.addView(mRightTipLayout);

            if (width >= 600) {

                mRightContactTextView.setTextSize(18);
                mRightContactTextView.setEmojiconSize(48);

            } else if (width > 501 && width < 600) {

                mRightContactTextView.setTextSize(16);
                mRightContactTextView.setEmojiconSize(46);


            } else if (width > 331 && width < 500) {

                mRightContactTextView.setTextSize(14);
                mRightContactTextView.setEmojiconSize(44);

            } else if (width > 260 && width < 330) {

                mRightContactTextView.setTextSize(13);
                mRightContactTextView.setEmojiconSize(42);

            } else if (width <= 260) {

                mRightContactTextView.setTextSize(12);
                mRightContactTextView.setEmojiconSize(42);

            }
        } catch (Exception e) {

        }


    }





    void rightAudioChat(){


        try {
            LinearLayout audioLayout=new LinearLayout(mParentActivity);
            audioLayout.setOrientation(LinearLayout.HORIZONTAL);
            audioLayout.setPadding(0,width*2/100,0,0);

            LinearLayout.LayoutParams playBtnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            playBtnParams.width=width*9/100;
            playBtnParams.height=width*8/100;
            playBtnParams.leftMargin=width*1/100;
            playBtnParams.gravity=Gravity.CENTER_VERTICAL;

            mRightAudioBtnPlay=new Button(mParentActivity);
            mRightAudioBtnPlay.setBackgroundResource(R.drawable.ic_action_audio_play);
            mRightAudioBtnPlay.setLayoutParams(playBtnParams);


            mRightAudioBtnCancel=new Button(mParentActivity);
            mRightAudioBtnCancel.setBackgroundResource(R.drawable.icon_cancel);
            mRightAudioBtnCancel.setLayoutParams(playBtnParams);

            LinearLayout.LayoutParams seekBarParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            seekBarParams.width=width*52/100;
            seekBarParams.leftMargin=width*1/100;
            seekBarParams.rightMargin=width*2/100;
            seekBarParams.gravity=Gravity.CENTER_VERTICAL;

            mRightAudioSeekBar=new SeekBar(mParentActivity);
            mRightAudioSeekBar.setLayoutParams(seekBarParams);


            mRightAudioBtnUpload=new Button(mParentActivity);
            mRightAudioBtnUpload.setBackgroundResource(R.drawable.audio_upload_normal);
            mRightAudioBtnUpload.setLayoutParams(playBtnParams);

            LinearLayout.LayoutParams audioUploadProgressParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            audioUploadProgressParams.width=width*52/100;
            audioUploadProgressParams.leftMargin=width*1/100;
            audioUploadProgressParams.rightMargin=width*2/100;
            audioUploadProgressParams.gravity=Gravity.CENTER_VERTICAL;

            mRightAudioUploadProgress=new ProgressBar(mParentActivity, null,
                    android.R.attr.progressBarStyleHorizontal);
            mRightAudioUploadProgress.setPadding(0,0,width*2/100,0);
            mRightAudioUploadProgress.setLayoutParams(audioUploadProgressParams);


            LinearLayout audioTimeLayout=new LinearLayout(mParentActivity);
            audioLayout.setOrientation(LinearLayout.HORIZONTAL);
            audioLayout.setPadding(0,width*2/100,0,0);

            mRightAudioSize=new TextView(mParentActivity);
            mRightAudioSize.setTextColor(Color.parseColor("#000000"));
            mRightAudioSize.setPadding(width*3/100,0,0,0);
            mRightAudioSize.setText("5.00 kb");

            LinearLayout.LayoutParams audioDurationParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            audioDurationParams.leftMargin=width*35/100;

            mRightAudioDuration=new TextView(mParentActivity);
            mRightAudioDuration.setTextColor(Color.parseColor("#000000"));
            mRightAudioDuration.setText("00.00.00");
            mRightAudioDuration.setLayoutParams(audioDurationParams);

            mRightAudioTextTime=new TextView(mParentActivity);
            mRightAudioTextTime.setGravity(Gravity.LEFT);
            mRightAudioTextTime.setTextSize(getTimeTxtSize());
            mRightAudioTextTime.setTextColor(Color.parseColor("#000000"));
            mRightAudioTextTime.setPadding((int) width * 1 / 100, width * 1 / 100, (int) width * 3 / 100, width * 1 / 100);

            LinearLayout textFooterLayout=new LinearLayout(mParentActivity);
            textFooterLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams textfooterParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textFooterLayout.setLayoutParams(textfooterParams);


            LinearLayout.LayoutParams mRightTickMarkParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mRightTickMarkParams.gravity=Gravity.LEFT|Gravity.CENTER_VERTICAL;

            mRightAudioTickMark=new ImageView(mParentActivity);
            mRightAudioTickMark.setId(k+600000);
            mRightAudioTickMark.setLayoutParams(mRightTickMarkParams);
            mRightAudioTickMark.setPadding((int) width * 2 / 100, width * 1 / 100, (int) width * 1 / 100, width * 1 / 100);


            LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textviewParams.gravity=Gravity.CENTER_VERTICAL;
            mRightAudioTextTime.setLayoutParams(textviewParams);
            audioLayout.setLayoutParams(textviewParams);
            mRightAudioTickMark.setLayoutParams(textviewParams);
            mRightAudioSize.setLayoutParams(textviewParams);

            audioLayout.addView(mRightAudioBtnCancel);
            audioLayout.addView(mRightAudioBtnUpload);
            audioLayout.addView(mRightAudioUploadProgress);
            audioLayout.addView(mRightAudioBtnPlay);
            audioLayout.addView(mRightAudioSeekBar);

       /* if(msg_list.get(k).getStatus()!=3)
        {
            audioLayout.addView(mRightAudioBtnPlay);
            audioLayout.addView(mRightAudioSeekBar);
        }else{
            audioLayout.addView(mRightAudioBtnPlay);
            audioLayout.addView(mRightAudioUploadProgress);
        }*/


            audioTimeLayout.addView(mRightAudioSize);
            audioTimeLayout.addView(mRightAudioDuration);

            textFooterLayout.addView(mRightAudioTickMark);
            textFooterLayout.addView(mRightAudioTextTime);
            mRightChatLayout.addView(audioLayout);
            mRightChatLayout.addView(audioTimeLayout);
            mRightChatLayout.addView(textFooterLayout);
            mRightTipLayout.addView(mRightChatLayout);

            mRightTipLayout.setPadding(0, 3, 0, 3);

            mDynamicView.addView(mRightTipLayout);


            if (width >= 600) {

                mRightAudioSize.setTextSize(12);
                mRightAudioDuration.setTextSize(12);

            } else if (width > 501 && width < 600) {

                mRightAudioSize.setTextSize(11);
                mRightAudioDuration.setTextSize(11);

            } else if (width > 331 && width < 500) {

                mRightAudioSize.setTextSize(10);
                mRightAudioDuration.setTextSize(10);

            } else if (width > 260 && width < 330) {

                mRightAudioSize.setTextSize(9);
                mRightAudioDuration.setTextSize(9);

            } else if (width <= 260) {

                mRightAudioSize.setTextSize(9);
                mRightAudioDuration.setTextSize(9);

            }
        } catch (Exception e) {

        }

    }


    void leftAudioChat(){


        try {
            LinearLayout audioLayout=new LinearLayout(mParentActivity);
            audioLayout.setOrientation(LinearLayout.HORIZONTAL);
            audioLayout.setPadding(0,width*2/100,0,0);

            LinearLayout.LayoutParams playBtnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            playBtnParams.width=width*9/100;
            playBtnParams.height=width*8/100;
            playBtnParams.leftMargin=width*1/100;
            playBtnParams.gravity=Gravity.CENTER_VERTICAL;

            mLeftAudioBtnPlay=new Button(mParentActivity);
            mLeftAudioBtnPlay.setId(k + 400000);
            mLeftAudioBtnPlay.setBackgroundResource(R.drawable.ic_action_audio_play);
            mLeftAudioBtnPlay.setLayoutParams(playBtnParams);

            mLeftAudioBtnCancel=new Button(mParentActivity);
            mLeftAudioBtnCancel.setId(k + 1100000);
            mLeftAudioBtnCancel.setBackgroundResource(R.drawable.icon_cancel);
            mLeftAudioBtnCancel.setLayoutParams(playBtnParams);

            LinearLayout.LayoutParams seekBarParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            seekBarParams.width=width*53/100;
            seekBarParams.leftMargin=width*1/100;
            seekBarParams.rightMargin=width*2/100;
            seekBarParams.gravity=Gravity.CENTER_VERTICAL;

            mLeftAudioSeekBar=new SeekBar(mParentActivity);
            mLeftAudioSeekBar.setId(k + 500000);
            mLeftAudioSeekBar.setLayoutParams(seekBarParams);


            mLeftAudioBtnDownload =new Button(mParentActivity);
            mLeftAudioBtnDownload.setId(k + 300000);
            mLeftAudioBtnDownload.setBackgroundResource(R.drawable.audio_download_normal);
            mLeftAudioBtnDownload.setLayoutParams(playBtnParams);

            LinearLayout.LayoutParams audioUploadProgressParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            audioUploadProgressParams.width=width*53/100;
            audioUploadProgressParams.leftMargin=width*1/100;
            audioUploadProgressParams.rightMargin=width*2/100;
            audioUploadProgressParams.gravity=Gravity.CENTER_VERTICAL;

            mLeftAudioDownloadProgress =new ProgressBar(mParentActivity, null,
                    android.R.attr.progressBarStyleHorizontal);
            mLeftAudioDownloadProgress.setId(k + 1200000);
            mLeftAudioDownloadProgress.setPadding(0, 0, width * 2 / 100, 0);
            mLeftAudioDownloadProgress.setLayoutParams(audioUploadProgressParams);


            LinearLayout audioTimeLayout=new LinearLayout(mParentActivity);
            audioLayout.setOrientation(LinearLayout.HORIZONTAL);
            audioLayout.setPadding(0,width*2/100,0,0);

            mLeftAudioSize=new TextView(mParentActivity);
            mLeftAudioSize.setTextColor(Color.parseColor("#000000"));
            mLeftAudioSize.setPadding(width*2/100,0,0,0);
            mLeftAudioSize.setText("5.00 kb");

            LinearLayout.LayoutParams audioDurationParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            //  audioDurationParams.leftMargin=width*32/100;
            audioDurationParams.rightMargin=width*2/100;

            mLeftAudioDuration=new TextView(mParentActivity);
            mLeftAudioDuration.setTextColor(Color.parseColor("#000000"));
            mLeftAudioDuration.setText("00.00.00");
            mLeftAudioDuration.setLayoutParams(audioDurationParams);

            mLeftAudioTextTime=new TextView(mParentActivity);
            mLeftAudioTextTime.setGravity(Gravity.LEFT);
            mLeftAudioTextTime.setTextSize(getTimeTxtSize());
            mLeftAudioTextTime.setTextColor(Color.parseColor("#000000"));
            mLeftAudioTextTime.setPadding((int) width * 4 / 100, 0, (int) width * 3 / 100, width * 1 / 100);

            LinearLayout textFooterLayout=new LinearLayout(mParentActivity);
            textFooterLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams textfooterParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textFooterLayout.setLayoutParams(textfooterParams);

            mLeftAudioTickMark=new ImageView(mParentActivity);
            mLeftAudioTickMark.setPadding((int) width * 4 / 100, 0, (int) width * 3 / 100, width * 1 / 100);

            LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textviewParams.gravity=Gravity.CENTER_VERTICAL;
            mLeftAudioTextTime.setLayoutParams(textviewParams);
            audioLayout.setLayoutParams(textviewParams);
            mLeftAudioTickMark.setLayoutParams(textviewParams);

            LinearLayout.LayoutParams mLeftAudioSizeParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftAudioSizeParams.width=width*40/100;
            mLeftAudioSize.setLayoutParams(mLeftAudioSizeParams);

     /*   if(msg_list.get(k).getMedia_name()!=null){

            audioLayout.addView(mLeftAudioBtnPlay);
            audioLayout.addView(mLeftAudioSeekBar);

        }else{

            audioLayout.addView(mLeftAudioBtnDownload);
        }*/

            audioLayout.addView(mLeftAudioBtnCancel);
            audioLayout.addView(mLeftAudioBtnDownload);
            audioLayout.addView(mLeftAudioDownloadProgress);
            audioTimeLayout.addView(mLeftAudioSize);
            audioTimeLayout.addView(mLeftAudioDuration);
            audioLayout.addView(mLeftAudioBtnPlay);
            audioLayout.addView(mLeftAudioSeekBar);

            textFooterLayout.addView(mLeftAudioTickMark);
            textFooterLayout.addView(mLeftAudioTextTime);

            mRightChatLayout.addView(mLeftTextProfile);
            mRightChatLayout.addView(audioLayout);
            mRightChatLayout.addView(audioTimeLayout);
            mRightChatLayout.addView(textFooterLayout);
            mRightTipLayout.addView(mRightChatLayout);

            mRightTipLayout.setPadding(0, 3, 0, 3);

            mDynamicView.addView(mRightTipLayout);


            if (width >= 600) {

                mLeftAudioSize.setTextSize(12);
                mLeftAudioDuration.setTextSize(12);

            } else if (width > 501 && width < 600) {

                mLeftAudioSize.setTextSize(11);
                mLeftAudioDuration.setTextSize(11);

            } else if (width > 331 && width < 500) {

                mLeftAudioSize.setTextSize(10);
                mLeftAudioDuration.setTextSize(10);

            } else if (width > 260 && width < 330) {

                mLeftAudioSize.setTextSize(9);
                mLeftAudioDuration.setTextSize(9);

            } else if (width <= 260) {

                mLeftAudioSize.setTextSize(9);
                mLeftAudioDuration.setTextSize(9);

            }
        } catch (Exception e) {

        }

    }


    void leftTextChat(){

        try {
            mLeftTextview = new EmojiconTextView(MUCTest.this);
            mLeftTextview.setGravity(Gravity.LEFT);

            mLeftTextview.setTextColor(Color.parseColor("#000000"));
            mLeftTextview.setPadding((int) width * 2 / 100, 0, (int) width * 2 / 100, width * 2 / 100);
            mLeftTextview.setId(k);
            //        mLeftTextview.setText(text);

            mLeftSenderTimeText = new TextView(MUCTest.this);
            mLeftSenderTimeText.setGravity(Gravity.LEFT);
            mLeftSenderTimeText.setTextSize(getTimeTxtSize());
            mLeftSenderTimeText.setTextColor(Color.parseColor("#000000"));
            mLeftSenderTimeText.setPadding((int) width * 2 / 100, 0, (int) width * 3 / 100, width * 1 / 100);

            //        mLeftSenderTimeText.setText(time);

            LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftTextview.setLayoutParams(textviewParams);

            LinearLayout.LayoutParams mLeftSenderTimeTextParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftSenderTimeTextParams.rightMargin = width * 47 / 100;
            mLeftSenderTimeText.setLayoutParams(mLeftSenderTimeTextParams);

            mRightChatLayout.addView(mLeftTextProfile);
            mRightChatLayout.addView(mLeftTextview);
            mRightChatLayout.addView(mLeftSenderTimeText);
            mRightTipLayout.addView(mRightChatLayout);

            mRightTipLayout.setPadding(0, 3, 0, 3);

            mDynamicView.addView(mRightTipLayout);


            if (width >= 600) {

                mLeftTextview.setTextSize(18);
                mLeftTextview.setEmojiconSize(48);


            } else if (width > 501 && width < 600) {

                mLeftTextview.setTextSize(16);
                mLeftTextview.setEmojiconSize(46);



            } else if (width > 331 && width < 500) {

                mLeftTextview.setTextSize(14);
                mLeftTextview.setEmojiconSize(44);


            } else if (width > 260 && width < 330) {

                mLeftTextview.setTextSize(13);
                mLeftTextview.setEmojiconSize(42);


            } else if (width <= 260) {

                mLeftTextview.setTextSize(12);
                mLeftTextview.setEmojiconSize(42);


            }
        } catch (Exception e) {

        }

    }

    void leftImageChat(){

        try {
            mLeftImageChat = new ImageView(mParentActivity);
            mLeftImageChat.setPadding((int) width * 1 / 100, 0, (int) width * 1 / 100, width * 2 / 100);
            mLeftImageChat.setScaleType(ImageView.ScaleType.CENTER_CROP);

            mLeftImageTextTime=new TextView(mParentActivity);
            mLeftImageTextTime.setGravity(Gravity.LEFT);
            mLeftImageTextTime.setTextSize(getTimeTxtSize());
            mLeftImageTextTime.setTextColor(Color.parseColor("#000000"));
            mLeftImageTextTime.setPadding((int) width * 2 / 100, 0, (int) width * 3 / 100, width * 1 / 100);
            //        mLeftImageTextTime.setText(time);

            LinearLayout.LayoutParams mLeftSenderTimeTextParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftSenderTimeTextParams.rightMargin=width*45/100;
            mLeftImageTextTime.setLayoutParams(mLeftSenderTimeTextParams);

            FrameLayout.LayoutParams textviewParams = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textviewParams.width=  width * 59 / 100;
            textviewParams.height= height * 30 / 100;
            textviewParams.topMargin=width * 2 / 100;
            textviewParams.bottomMargin=width * 2 / 100;
            textviewParams.gravity=Gravity.CENTER;
            mLeftImageChat.setLayoutParams(textviewParams);
            //        mRightSenderTimeText.setLayoutParams(textviewParams);

            FrameLayout ImachChatLayout=new FrameLayout(mParentActivity);

            mLeftImageChatDownload = new ImageView(mParentActivity);
            mLeftImageChatDownload.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mLeftImageChatDownload.setBackgroundResource(R.drawable.image_download_normal);

            mLeftImagetProgressBar =new ProgressBar(mParentActivity);

            FrameLayout.LayoutParams left_download_icon_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            left_download_icon_params.width = (int) width * 15/100;
            left_download_icon_params.height = (int) width *15/100;
            left_download_icon_params.gravity=Gravity.CENTER;
            mLeftImageChatDownload.setLayoutParams(left_download_icon_params);
            mLeftImagetProgressBar.setLayoutParams(left_download_icon_params);


            ImachChatLayout.addView(mLeftImageChat);
            ImachChatLayout.addView(mLeftImageChatDownload);
            ImachChatLayout.addView(mLeftImagetProgressBar);

            mRightChatLayout.addView(mLeftTextProfile);
            mRightChatLayout.addView(ImachChatLayout);
            mRightChatLayout.addView(mLeftImageTextTime);
            mRightTipLayout.addView(mRightChatLayout);

            mRightTipLayout.setPadding(0, 3, 0, 3);

            mDynamicView.addView(mRightTipLayout);
        } catch (Exception e) {

        }


    }

    void leftVideoChat(){

        try {
            mLeftVideoChat = new ImageView(mParentActivity);
            mLeftVideoChat.setPadding((int) width * 1 / 100, 0, (int) width * 1 / 100, width * 2 / 100);
            mLeftVideoChat.setScaleType(ImageView.ScaleType.CENTER_CROP);

            mLeftVideoTimeText=new TextView(mParentActivity);
            mLeftVideoTimeText.setGravity(Gravity.LEFT);
            mLeftVideoTimeText.setTextSize(getTimeTxtSize());
            mLeftVideoTimeText.setTextColor(Color.parseColor("#000000"));
            mLeftVideoTimeText.setPadding((int) width * 2 / 100, 0, (int) width * 3 / 100, width * 1 / 100);
            //        mLeftImageTextTime.setText(time);

            LinearLayout.LayoutParams mLeftSenderTimeTextParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftSenderTimeTextParams.rightMargin=width*38/100;
            mLeftVideoTimeText.setLayoutParams(mLeftSenderTimeTextParams);
            //        mLeftVideoTimeText.setText(time);

            FrameLayout.LayoutParams mLeftVideoChatParams = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftVideoChatParams.width=  width * 60 / 100;
            mLeftVideoChatParams.height= height * 30 / 100;
            mLeftVideoChatParams.topMargin=width * 2 / 100;
            mLeftVideoChatParams.gravity=Gravity.CENTER;
            mLeftVideoChat.setLayoutParams(mLeftVideoChatParams);
            //        mRightSenderTimeText.setLayoutParams(textviewParams);

            FrameLayout mLeftVideoChatLayout=new FrameLayout(mParentActivity);

            mLeftVideoChatDownload = new ImageView(mParentActivity);
            mLeftVideoChatDownload.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mLeftVideoChatDownload.setBackgroundResource(R.drawable.image_download_normal);

            mLeftVideoButtonPlay = new ImageView(mParentActivity);
            mLeftVideoButtonPlay.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mLeftVideoButtonPlay.setBackgroundResource(R.drawable.videooverlay);


            mLeftVideoProgress =new ProgressBar(mParentActivity);

            FrameLayout.LayoutParams left_download_icon_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            left_download_icon_params.width = (int) width * 15/100;
            left_download_icon_params.height = (int) width *15/100;
            left_download_icon_params.gravity=Gravity.CENTER;
            mLeftVideoChatDownload.setLayoutParams(left_download_icon_params);
            mLeftVideoProgress.setLayoutParams(left_download_icon_params);
            mLeftVideoButtonPlay.setLayoutParams(left_download_icon_params);


            FrameLayout.LayoutParams videoFooter_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            videoFooter_params.gravity=Gravity.BOTTOM;

            FrameLayout videoFooter=new FrameLayout(mParentActivity);
            videoFooter.setLayoutParams(videoFooter_params);

            FrameLayout.LayoutParams videoimage = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            videoimage.width=width*10/100;
            videoimage.height=width*8/100;
            videoimage.gravity=Gravity.LEFT;
            videoimage.bottomMargin=width*1/100;
            videoimage.leftMargin=width*4/100;

            mLeftVideoImgOverlay=new ImageView(mParentActivity);
            mLeftVideoImgOverlay.setBackgroundResource(R.drawable.frame_overlay_gallery_video);
            mLeftVideoImgOverlay.setLayoutParams(videoimage);

            FrameLayout.LayoutParams videoDuration_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            videoDuration_params.gravity=Gravity.LEFT;
            videoDuration_params.topMargin=(width*1/100)+1;
            videoDuration_params.leftMargin=width*13/100;

            mLeftVideoDuration=new TextView(mParentActivity);
            mLeftVideoDuration.setTextColor(Color.parseColor("#ffffff"));
            mLeftVideoDuration.setText("1");
            mLeftVideoDuration.setLayoutParams(videoDuration_params);

            FrameLayout.LayoutParams videoSize_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            videoSize_params.gravity=Gravity.RIGHT;
            videoSize_params.topMargin=width*1/100;
            videoSize_params.rightMargin=width*5/100;
            videoSize_params.leftMargin=width*47/100;


            mLeftVideoSize = new TextView(mParentActivity);
            mLeftVideoSize.setTextColor(Color.parseColor("#ffffff"));
            mLeftVideoSize.setLayoutParams(videoSize_params);

            videoFooter.addView(mLeftVideoImgOverlay);
            videoFooter.addView(mLeftVideoDuration);
            videoFooter.addView(mLeftVideoSize);


            mLeftVideoChatLayout.addView(mLeftVideoChat);

            if(msg_list.get(k).getMedia_name()!=null){


                mLeftVideoChatLayout.addView(mLeftVideoButtonPlay);

            }else{
                mLeftVideoChatLayout.addView(mLeftVideoChatDownload);
                mLeftVideoChatLayout.addView(mLeftVideoProgress);

            }
            mLeftVideoChatLayout.addView(videoFooter);

            mRightChatLayout.addView(mLeftTextProfile);
            mRightChatLayout.addView(mLeftVideoChatLayout);
            mRightChatLayout.addView(mLeftVideoTimeText);
            mRightTipLayout.addView(mRightChatLayout);

            mRightTipLayout.setPadding(0, 3, 0, 3);

            mDynamicView.addView(mRightTipLayout);
        } catch (Exception e) {

        }

    }

    void leftContactChat(){

        try {
            LinearLayout.LayoutParams contactLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            contactLayoutParams.width=width*50/100;
            contactLayoutParams.height=height*8/100;
            contactLayoutParams.topMargin=width*2/100;
            contactLayoutParams.gravity=Gravity.CENTER;

            LinearLayout contactLayout=new LinearLayout(mParentActivity);
            contactLayout.setOrientation(LinearLayout.HORIZONTAL);
            contactLayout.setBackgroundColor(Color.parseColor("#80ffffff"));
            contactLayout.setLayoutParams(contactLayoutParams);

            LinearLayout.LayoutParams contactAvathorParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            contactAvathorParams.width=width*10/100;
            contactAvathorParams.height=width*10/100;
            contactAvathorParams.leftMargin=width*5/100;

            mLeftContactAvathor=new ImageView(mParentActivity);
            mLeftContactAvathor.setBackgroundResource(R.drawable.silhouette121);
            mLeftContactAvathor.setLayoutParams(contactAvathorParams);

            LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textviewParams.gravity=Gravity.CENTER_VERTICAL;
            textviewParams.width=width*35/100;
            textviewParams.leftMargin=width*2/100;

            mLeftContactTextView = new EmojiconTextView(mParentActivity);
            mLeftContactTextView.setGravity(Gravity.CENTER_VERTICAL);
            mLeftContactTextView.setTextColor(Color.parseColor("#000000"));
            mLeftContactTextView.setTypeface(Typeface.DEFAULT_BOLD);
            // textview.setPadding((int) width * 3 / 100, 0, (int) width * 3 / 100, width * 2 / 100);
            //        mLeftContactTextView.setText(text);
            mLeftContactTextView.setLayoutParams(textviewParams);


            mLeftContactTextTime=new TextView(mParentActivity);
            mLeftContactTextTime.setGravity(Gravity.LEFT);
            mLeftContactTextTime.setTextSize(getTimeTxtSize());
            mLeftContactTextTime.setTextColor(Color.parseColor("#000000"));
            mLeftContactTextTime.setPadding((int) width * 2 / 100, width * 1 / 100, (int) width * 3 / 100, width * 1 / 100);
            //        mLeftImageTextTime.setText(time);

            LinearLayout.LayoutParams mLeftSenderTimeTextParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftSenderTimeTextParams.rightMargin=width*38/100;
            mLeftContactTextTime.setLayoutParams(mLeftSenderTimeTextParams);


            LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftContactAvathor.setLayoutParams(timeParams);

            contactLayout.addView(mLeftContactTextView);
            contactLayout.addView(mLeftContactAvathor);

            mRightChatLayout.addView(mLeftTextProfile);
            mRightChatLayout.addView(contactLayout);
            mRightChatLayout.addView(mLeftContactTextTime);
            mRightTipLayout.addView(mRightChatLayout);

            mRightTipLayout.setPadding(0, 3, 0, 3);

            mDynamicView.addView(mRightTipLayout);

            if (width >= 600) {

                mLeftContactTextView.setTextSize(18);
                mLeftContactTextView.setEmojiconSize(48);

            } else if (width > 501 && width < 600) {

                mLeftContactTextView.setTextSize(16);
                mLeftContactTextView.setEmojiconSize(46);


            } else if (width > 331 && width < 500) {

                mLeftContactTextView.setTextSize(14);
                mLeftContactTextView.setEmojiconSize(44);

            } else if (width > 260 && width < 330) {

                mLeftContactTextView.setTextSize(13);
                mLeftContactTextView.setEmojiconSize(42);

            } else if (width <= 260) {

                mLeftContactTextView.setTextSize(12);
                mLeftContactTextView.setEmojiconSize(42);

            }
        } catch (Exception e) {

        }


    }



    public void setRightChatText() {
        try {
            Constant.printMsg("MUC text msg Right 1:" + k + "  " + msg_list.get(k).getData().toString());


            if (msg_list.get(k).getStatus() == 3) {
                mRightTickMark.setImageResource(R.drawable.message_unsent);
            } else if (msg_list.get(k).getStatus() == 2) {
                mRightTickMark
                        .setImageResource(R.drawable.receipt_from_server);
            } else if (msg_list.get(k).getStatus() == 1 || msg_list.get(k).getStatus() == 0) {
                mRightTickMark
                        .setImageResource(R.drawable.receipt_from_target);
            } else if (msg_list.get(k).getStatus() == -1) {
                mRightTickMark
                        .setImageResource(R.drawable.receipt_read);
            }


            // Set timestamp
            Date date = new Date();
            date.setTime(msg_list.get(k).getSend_timestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mRightSenderTimeText.setText(time_format.format(date));


            mRightTextview.setTag(k);

            mRightTextview.setHighlightColor(Color.TRANSPARENT);
            mRightTextview.setMovementMethod(LinkMovementMethod
                    .getInstance());
            mRightTextview.setText(new Emoji(mParentActivity).getSmiledText(msg_list.get(k).getData().toString()));

            String text = msg_list.get(k).getData().toString();

            if (text.length() > 2) {

                char q = text.charAt(0);
                char p = text.charAt(1);

                Constant.printMsg("MUC text msg Right 1:" + q + "  " + p);

                if (q == '<') {

                    if (p == '-') {
                        mRightTextview.setHighlightColor(Color.TRANSPARENT);
                        mRightTextview.setMovementMethod(LinkMovementMethod
                                .getInstance());
                        //

                        myMethod(text.substring(2));
                        mRightTextview.setText(
                                addClickablePart(this.Normallist, mRightTextview),
                                TextView.BufferType.SPANNABLE);
                    } else {
                        mRightTextview.setHighlightColor(Color.TRANSPARENT);
                        mRightTextview.setMovementMethod(LinkMovementMethod
                                .getInstance());
                        mRightTextview.setText(new Emoji(mParentActivity).getSmiledText(msg_list.get(k).getData().toString()));
                    }

                }

            }


            final EmojiconTextView mTextMsg = mRightTextview;
            mValue = msg_list.get(k).getData().toString();

            String[] words = mValue.split("\\s+");


            if (text.length() > 3) {

                char s = text.charAt(0);
                char s1 = text.charAt(1);
                char s2 = text.charAt(2);
                mTextMsg.setTypeface(null, Color.BLACK);

                Constant.printMsg("MUC text msg Right 2:" + s + "" + s1 + "  " + s2);

                mTextMsg.setBackground(null);
                if (s == '<') {

                    if (s1 == 'b' && s2 == '>') {

                        mTextMsg.setTextColor(Color.GREEN);
                        mTextMsg.setText(text.substring(3));
                        mTextMsg.setTypeface(null, Color.GREEN);

                        mTextMsg.setBackground(null);
                        mTextMsg.setGravity(Gravity.CENTER
                                | Gravity.LEFT);
                        mTextMsg.setTextSize(valueText());
                        mTextMsg.setEmojiconSize(33);
                        mTextMsg.setTypeface(null, Typeface.NORMAL);

                        LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        bubbleImgParams.gravity = Gravity.RIGHT;
                        //                                bubbleImgParams.rightMargin = Constant.screenWidth * 15 / 100;

                        mTextMsg.setLayoutParams(bubbleImgParams);

                    } else if (s1 == 'z' && s2 == '>') {

                        mTextMsg.setTextColor(Color.RED);
                        mTextMsg.setText(text.substring(3));
                        mTextMsg.setTypeface(null, Color.RED);

                        mTextMsg.setBackground(null);
                        mTextMsg.setMinimumWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                        mTextMsg.setGravity(Gravity.CENTER
                                | Gravity.LEFT);
                        mTextMsg.setTextSize(valueText());
                        mTextMsg.setEmojiconSize(33);
                        String dazzle = text.substring(3)
                                .toString();
                        String[] parts = dazzle.split("-");

                        String part1 = parts[0];
                        String part2 = parts[1];
                        mTextMsg.setText(part1);
                        mTextMsg.setTypeface(null, Typeface.NORMAL);

                        LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        bubbleImgParams.gravity = Gravity.RIGHT;

                        mTextMsg.setLayoutParams(bubbleImgParams);

                    } else if (s1 == 'l' && s2 == '>') {

                        mTextMsg.setTextColor(Color.YELLOW);
                        mTextMsg.setText(text.substring(3));

                        OrientationGroup.mZzleTextor = mTextMsg
                                .getText().toString();
                        mTextMsg.setTypeface(null, Color.YELLOW);

                        mTextMsg.setBackground(null);
                        mTextMsg.setMinimumWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                        mTextMsg.setGravity(Gravity.CENTER
                                | Gravity.LEFT);
                        mTextMsg.setTextSize(valueText());
                        mTextMsg.setEmojiconSize(33);

                        mTextMsg.setTypeface(null, Typeface.NORMAL);

                        LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        bubbleImgParams.gravity = Gravity.RIGHT;

                        mTextMsg.setLayoutParams(bubbleImgParams);

                    } else if (s1 == 'x' && s2 == '>') {
                        String dazzle = text.substring(3)
                                .toString();
                        String[] parts = dazzle.split("-");
                        String part1 = parts[0];
                        String part2 = parts[1];
                        String part3 = parts[2];
                        String part4 = parts[3];
                        String part5 = parts[4];

                        Constant.printMsg("nymss::>>>" + part1
                                + "  " + part2 + "  " + part3
                                + "  " + part4 + "  " + part5);
                        mTextMsg.setText(part5);

                        mTextMsg.setTextColor(Color.RED);
                        mTextMsg.setText(part5);

                        //                                ZzlePreviewGroup.mZzleTextor = mTextMsg
                        //                                        .getText().toString();
                        mTextMsg.setTypeface(null, Color.RED);

                        mTextMsg.setBackground(null);
                        mTextMsg.setMinimumWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                        mTextMsg.setGravity(Gravity.CENTER
                                | Gravity.LEFT);
                        mTextMsg.setTextSize(valueText());
                        mTextMsg.setEmojiconSize(33);
                        mTextMsg.setTypeface(null, Typeface.NORMAL);

                        LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        bubbleImgParams.gravity = Gravity.RIGHT;
                        //                                bubbleImgParams.rightMargin = Constant.screenWidth * 15 / 100;

                        mTextMsg.setLayoutParams(bubbleImgParams);

                    } else if (s1 == 's' && s2 == '>') {

                        mRightDestTime.setVisibility(View.VISIBLE);

                        String self_destruct = text.substring(3)
                                .toString();
                        String[] parts = self_destruct.split("-");
                        String part1 = parts[0];
                        String part2 = parts[1];
                        String part3 = parts[2];

                        mRightDestTime.setText(part1);
                        Constant.jid = part3;
                        mTextMsg.setText(part2);
                        mTextMsg.setTextColor(Color.BLACK);
                        mTextMsg.setBackground(null);

                        ArrayList<String> jid_list = new ArrayList<String>();
                        for (int i = 0; i < parts.length; i++) {

                            if (i != 0 && i != 1) {
                                jid_list.add(parts[i]);

                            }

                            Constant.printMsg("jid_list ::::::: "
                                    + jid_list + "    " + i);
                        }

                        mself_destruct_list.add(String.valueOf(k));
                        Constant.mself_id = k;

                        //                      dbAdapter = KachingMeApplication
                        //                              .getDatabaseAdapter();
                        //
                        //                      final MessageGetSet selectedItem;
                        //
                        //
                        //                      try {
                        //
                        //                          if (Constant.msg_list_adapter.size() == Constant.mself_id) {
                        //
                        //                              Constant.printMsg("part tst::::>>>>>>");
                        //                          } else {
                        //
                        //
                        //                              selectedItem = Constant.msg_list_adapter
                        //                                      .get(Constant.mself_id);
                        //                              Constant.printMsg("part tst111::::>>>>>>>>"
                        //                                      + selectedItem
                        //                                      .get_id()
                        //                                      + "     "
                        //                                      + selectedItem
                        //                                      .getIs_sec_chat()
                        //                                      + "   "
                        //                                      + part1
                        //                                      + "    "
                        //                                      + KachingMeApplication
                        //                                      .getjid() + "    " + ChatTest.dest_msg_list.size());
                        //
                        //  //
                        //                              if (!mDestPositionIds.get(i)) {
                        //
                        //
                        //                                  Constant.printMsg("update dest status : " + mDestPositionIds.get(i));
                        //
                        //                                  setDestListView(true, i);
                        //                                  Constant.printMsg("update dest status : " + mDestPositionIds.get(i));
                        //
                        //  //                                            Runnable r = new MyThread(ChatTest.dest_msg_list.size() - 1, ChatTest.dest_msg_list.get(ChatTest.dest_msg_list.size() - 1), t, arg0, t.getId());
                        //  //                                            new Thread(r).start();
                        //
                        //                              }
                        //  gg
                        //                                          (new Thread(new Runnable() {
                        //
                        //                                              int i ;
                        //                                              @Override
                        //                                              public void run() {
                        //
                        //
                        //                                              }
                        //                                          })).start();
                        //
                        //
                        //                          }
                        //                      } catch (Exception e) {
                        //                          // TODO: handle exception
                        //                      }
                    } else if (s1 == 'k' && s2 == '>') {

                        String kons_array = text.substring(3)
                                .toString();
                        String[] parts = kons_array.split("-");
                        final String part1 = parts[0];
                        String part2 = parts[1];
                        final String part3 = parts[2];

                        Constant.printMsg("testststststtstststskkkkkk" + part1 + part2 + part3);
                        String bubblecolor = part2;
                        String mShape = part1.trim();
                        if (!bubblecolor.equalsIgnoreCase("")) {
                            switch (bubblecolor) {
                                case "Blue":
                                    if (mShape.equalsIgnoreCase("oval_left")) {
                                        mTextMsg.setBackgroundResource(R.drawable.blue_one);
                                    }
                                    if (mShape.equalsIgnoreCase("cross_rect_right")) {
                                        mTextMsg.setBackgroundResource(R.drawable.blue_two);
                                    }
                                    if (mShape.equalsIgnoreCase("oval_right")) {
                                        mTextMsg.setBackgroundResource(R.drawable.blue_three);
                                    }
                                    if (mShape.equalsIgnoreCase("rect_left")) {
                                        mTextMsg.setBackgroundResource(R.drawable.blue_four);
                                    }
                                    if (mShape.equalsIgnoreCase("rect_right")) {
                                        mTextMsg.setBackgroundResource(R.drawable.blue_five);
                                    }
                                    if (mShape.equalsIgnoreCase("cross_rect_left")) {
                                        mTextMsg.setBackgroundResource(R.drawable.blue_six);
                                    }
                                    if (mShape.equalsIgnoreCase("cloud_left")) {
                                        mTextMsg.setBackgroundResource(R.drawable.blue_seven);
                                    }
                                    break;
                                case "Green":
                                    if (mShape.equalsIgnoreCase("oval_left")) {
                                        mTextMsg.setBackgroundResource(R.drawable.green_one);
                                    }
                                    if (mShape.equalsIgnoreCase("cross_rect_right")) {
                                        mTextMsg.setBackgroundResource(R.drawable.green_two);
                                    }
                                    if (mShape.equalsIgnoreCase("oval_right")) {
                                        mTextMsg.setBackgroundResource(R.drawable.green_three);
                                    }
                                    if (mShape.equalsIgnoreCase("rect_left")) {
                                        mTextMsg.setBackgroundResource(R.drawable.green_four);
                                    }
                                    if (mShape.equalsIgnoreCase("rect_right")) {
                                        mTextMsg.setBackgroundResource(R.drawable.green_five);
                                    }
                                    if (mShape.equalsIgnoreCase("cross_rect_left")) {
                                        mTextMsg.setBackgroundResource(R.drawable.green_six);
                                    }
                                    if (mShape.equalsIgnoreCase("cloud_left")) {
                                        mTextMsg.setBackgroundResource(R.drawable.green_seven);
                                    }
                                    break;
                                case "Pink":
                                    if (mShape.equalsIgnoreCase("oval_left")) {
                                        mTextMsg.setBackgroundResource(R.drawable.pink_one);
                                    }
                                    if (mShape.equalsIgnoreCase("cross_rect_right")) {
                                        mTextMsg.setBackgroundResource(R.drawable.pink_two);
                                    }
                                    if (mShape.equalsIgnoreCase("oval_right")) {
                                        mTextMsg.setBackgroundResource(R.drawable.pink_three);
                                    }
                                    if (mShape.equalsIgnoreCase("rect_left")) {
                                        mTextMsg.setBackgroundResource(R.drawable.pink_four);
                                    }
                                    if (mShape.equalsIgnoreCase("rect_right")) {
                                        mTextMsg.setBackgroundResource(R.drawable.pink_five);
                                    }
                                    if (mShape.equalsIgnoreCase("cross_rect_left")) {
                                        mTextMsg.setBackgroundResource(R.drawable.pink_six);
                                    }
                                    if (mShape.equalsIgnoreCase("cloud_left")) {
                                        mTextMsg.setBackgroundResource(R.drawable.pink_seven);
                                    }
                                    break;
                            }

                        }

                        mTextMsg.setText(part3);


                        mTextMsg.setTextColor(Color.WHITE);


                        LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        if (part3.length() > 5)
                            bubbleImgParams.width = Constant.screenWidth * 35 / 100;
                        else
                            bubbleImgParams.width = Constant.screenWidth * 28 / 100;

                        bubbleImgParams.height = Constant.screenHeight * 9 / 100;
                        bubbleImgParams.topMargin = Constant.screenWidth * 2 / 100;
                        bubbleImgParams.rightMargin = Constant.screenWidth * 2 / 100;
                        bubbleImgParams.gravity = Gravity.RIGHT;

                        mTextMsg.setLayoutParams(bubbleImgParams);
                        mTextMsg.setGravity(Gravity.CENTER);
                        Constant.typeFaceKons(getApplicationContext(), mTextMsg);

                        mTextMsg.setTextColor(Color.WHITE);
                        if (Constant.screenWidth >= 600) {

                            mTextMsg.setEmojiconSize(40);


                        } else if (Constant.screenWidth > 501
                                && Constant.screenWidth < 600) {

                            mTextMsg.setEmojiconSize(39);


                        } else if (Constant.screenWidth > 260
                                && Constant.screenWidth < 500) {
                            Constant.printMsg("caleedd 3");

                            mTextMsg.setEmojiconSize(38);

                        } else if (Constant.screenWidth <= 260) {
                            Constant.printMsg("caleedd 4");


                            mTextMsg.setEmojiconSize(36);


                        }

                    } else if (s1 == 'd' && s2 == '>') {

                        mTextMsg.setText("You donates "
                                + text.substring(3) + " BuxS for "
                                + Constant.mSenderName);

                        mTextMsg.setTextColor(Color.MAGENTA);
                        mTextMsg.setTypeface(null, Typeface.NORMAL);

                        mTextMsg.setBackground(null);
                        mTextMsg.setMinimumWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                        mTextMsg.setTextSize(
                                TypedValue.COMPLEX_UNIT_SP,
                                msg_font_size);
                        mTextMsg.setGravity(Gravity.CENTER
                                | Gravity.LEFT);
                    } else if (s1 == 'a' && s2 == '>') {

                        mTextMsg.setText("You Accept buxs");
                        mTextMsg.setTypeface(null, Typeface.NORMAL);
                        mTextMsg.setTextColor(Color.BLACK);
                        mTextMsg.setBackground(null);
                        mTextMsg.setMinimumWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                        mTextMsg.setTextSize(
                                TypedValue.COMPLEX_UNIT_SP,
                                msg_font_size);
                        mTextMsg.setGravity(Gravity.CENTER
                                | Gravity.LEFT);
                        mTextMsg.setTextSize(
                                TypedValue.COMPLEX_UNIT_SP,
                                msg_font_size);
                    } else {


                        mTextMsg.setTextColor(Color.BLACK);
                        mTextMsg.setTypeface(null, Typeface.NORMAL);
                        mTextMsg.setBackground(null);
                        mTextMsg.setMinimumWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                        mTextMsg.setGravity(Gravity.CENTER
                                | Gravity.LEFT);
                        mTextMsg.setTextSize(valueText());
                        mTextMsg.setEmojiconSize(33);
                        LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        bubbleImgParams.gravity = Gravity.RIGHT;
                        //                                bubbleImgParams.rightMargin = Constant.screenWidth * 15 / 100;

                        mTextMsg.setLayoutParams(bubbleImgParams);

                    }
                } else {
                    mTextMsg.setTextColor(Color.BLACK);
                    mTextMsg.setTypeface(null, Color.BLACK);
                    mTextMsg.setBackground(null);
                    mTextMsg.setMinimumWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                    mTextMsg.setGravity(Gravity.CENTER
                            | Gravity.LEFT);
                    mTextMsg.setTextSize(valueText());
                    mTextMsg.setEmojiconSize(33);
                    mTextMsg.setTypeface(null, Typeface.NORMAL);

                    LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    bubbleImgParams.gravity = Gravity.RIGHT;
                    //                            bubbleImgParams.rightMargin = Constant.screenWidth * 15 / 100;

                    mTextMsg.setLayoutParams(bubbleImgParams);

                }

            } else {
                mTextMsg.setTextColor(Color.BLACK);
                mTextMsg.setTypeface(null, Color.BLACK);
                mTextMsg.setBackground(null);
                //                        mTextMsg.setMinimumWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                mTextMsg.setGravity(Gravity.CENTER
                        | Gravity.RIGHT);
                mTextMsg.setTextSize(valueText());
                mTextMsg.setEmojiconSize(33);

                mTextMsg.setTypeface(null, Typeface.NORMAL);

                LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                bubbleImgParams.gravity = Gravity.RIGHT;
                //                        bubbleImgParams.rightMargin = Constant.screenWidth * 15 / 100;

                mTextMsg.setLayoutParams(bubbleImgParams);

            }

            mTextMsg.setTag(k);

            mTextMsg.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // Constant.printMsg("clicked syms:::::>>>>>>");
                    //                EmojiconTextView mTextMsg = (EmojiconTextView) v
                    //                        .findViewById(R.id.right_chat_text);
                    int position = (Integer) v.getTag();

                    String text = mTextMsg.getText().toString();

                    boolean toggle = true;

                    //String text = mTextMsg.getText().toString();


//                    if (mTextMsg.getCurrentTextColor() == -65536) {
//
//                        Constant.zzle = false;
//
//                        BannerActivityLED.mZzleText = mTextMsg
//                                .getText().toString();
//                        Intent intent = new Intent(mParentActivity,
//                                BannerActivityLED.class);
//                        mParentActivity.startActivity(intent);
//                    }

                    if (mTextMsg.getCurrentTextColor() == -256) {

                        OrientationGroup.mZzleTextor = mTextMsg
                                .getText().toString();

                        // HorizonalSlideshow.mZzleTextor = mTextMsg
                        // .getText().toString();

                        Intent intent = new Intent(mParentActivity,
                                OrientationGroup.class);
                        mParentActivity.startActivity(intent);

                        // }

                    }
                    if (mTextMsg.getCurrentTextColor() == -65536) {
                        MessageGetSet selectedItem1 = Constant.msg_list_adapter
                                .get(position);
                        System.out.println("testing group dazZ" + selectedItem1.getData());
                        if (selectedItem1.getData().startsWith("<x>")) {
                            Constant.mZzleText = mTextMsg.getText()
                                    .toString();

                            BannerActivityChat.mZzleText = mTextMsg
                                    .getText().toString();
                            String value1 = selectedItem1.getData()
                                    .substring(3).toString();
                            String[] parts = value1.split("-");

                            String part1 = parts[0];
                            String part2 = parts[1];
                            String part3 = parts[2];
                            String part4 = parts[3];
                            String part5 = parts[4];
                            BannerActivityChat.mZzleTextBackground = part1;
                            BannerActivityChat.mZzleTextColor = part3;
                            BannerActivityChat.mZzleTextSize = part4;
                            BannerActivityChat.mZzleTextSpeed = part2;
                            Intent intent = new Intent(mParentActivity,
                                    BannerActivityChat.class);
                            startActivity(intent);
                        } else {
                            Constant.zzle = false;

                            BannerActivityLED.mZzleText = mTextMsg
                                    .getText().toString();

                            String value1 = selectedItem1.getData()
                                    .substring(3).toString();
                            String[] parts = value1.split("-");
                            String part1 = parts[0];
                            String part2 = parts[1];
                            String part3 = parts[2];
                            String part4 = parts[3];
                            String part5 = parts[4];
                            BannerActivityLED.mZzleTextBackground =part5;
                            BannerActivityLED.mZzleTextSpeed =part3;
                            BannerActivityLED.mZzleTextSize =part4;


                            Intent intent = new Intent(mParentActivity,
                                    BannerActivityLED.class);
                            startActivity(intent);
                        }
                    }

                }

            });
        } catch (Exception e) {

        }


    }


    public void setLeftChatText(){


        try {
// Set timestamp
            Date date = new Date();
            date.setTime(msg_list.get(k).getReceived_timestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mLeftSenderTimeText.setText(time_format.format(date));


            String displayName = null;
            // Left sender name...
            ContactsGetSet contact = new ContactsGetSet();
            try {
                contact = dbAdapter.getContact(msg_list.get(k).getRemote_resource());
                Constant.printMsg("Left username : " + contact);
                if (contact == null) {
                    mLeftTextProfile.setText(msg_list.get(k).getRemote_resource().split("@")[0]);
                    displayName = msg_list.get(k).getRemote_resource().split("@")[0];
                } else if (contact.getDisplay_name()!=null){
                    mLeftTextProfile.setText(contact.getDisplay_name());
                    displayName = contact.getDisplay_name();
                }else
                {
                    mLeftTextProfile.setText(msg_list.get(k).getRemote_resource().split("@")[0]);
                    displayName = msg_list.get(k).getRemote_resource().split("@")[0];
                }

                if(displayName.length()>1)
                {
                    getColorLeftUser(displayName);
                }else {
                    mLeftTextProfile.setText(msg_list.get(k).getRemote_resource().split("@")[0]);
                    displayName = msg_list.get(k).getRemote_resource().split("@")[0];

                    getColorLeftUser(displayName);
                }



            } catch (Exception e) {

                Constant.printMsg("Display name : " + e.toString());
                mLeftTextProfile.setText(msg_list.get(k).getRemote_resource().split("@")[0]);
                displayName = msg_list.get(k).getRemote_resource().split("@")[0];
                getColorLeftUser(displayName);
            }


            mLeftTextview.setHighlightColor(Color.TRANSPARENT);
            mLeftTextview.setMovementMethod(LinkMovementMethod
                    .getInstance());
            mLeftTextview.setText(msg_list.get(k).getData().toString());

            //   mLeftTextview.setText(msg_list.get(position).getData());
            String text = msg_list.get(k).getData();

            if (text.length() > 2) {

                char q = text.charAt(0);
                char p = text.charAt(1);

                if (q == '<') {

                    if (p == '-') {
                        mLeftTextview.setHighlightColor(Color.TRANSPARENT);
                        mLeftTextview.setMovementMethod(LinkMovementMethod
                                .getInstance());
                        //

                        myMethod(text.substring(2));
                        mLeftTextview.setText(
                                addClickablePart(this.Normallist, mLeftTextview),
                                TextView.BufferType.SPANNABLE);
                    } else {
                        mLeftTextview.setHighlightColor(Color.TRANSPARENT);
                        mLeftTextview.setMovementMethod(LinkMovementMethod
                                .getInstance());
                        mLeftTextview.setText(msg_list.get(k).getData().toString());

                    }

                }
            }

            EmojiconTextView txt_msg = mLeftTextview;
            mValue = txt_msg.getText().toString();

            String[] words = mValue.split("\\s+");

            if (txt_msg.length() > 3) {

                char s = text.charAt(0);
                char s1 = text.charAt(1);
                char s2 = text.charAt(2);

                if (s == '<') {

                    if (s1 == 'b' && s2 == '>') {
                        txt_msg.setTextColor(Color.GREEN);
                        txt_msg.setText(text.substring(3));
                        txt_msg.setTypeface(null, Color.GREEN);

                        txt_msg.setBackground(null);
                        txt_msg.setTextSize(valueText());
                        txt_msg.setEmojiconSize(33);
                        txt_msg.setTypeface(null, Typeface.NORMAL);

                        LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        bubbleImgParams.gravity = Gravity.LEFT;
                        //                                bubbleImgParams.leftMargin = Constant.screenWidth * 15 / 100;

                        txt_msg.setLayoutParams(bubbleImgParams);
                    }
                    else if (s1 == 'z' && s2 == '>') {

                        txt_msg.setTextColor(Color.RED);
                        txt_msg.setText(text.substring(3));
                        txt_msg.setTypeface(null, Color.RED);

                        txt_msg.setBackground(null);
                        txt_msg.setMinimumWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                        txt_msg.setGravity(Gravity.CENTER
                                | Gravity.LEFT);
                        txt_msg.setTextSize(valueText());
                        txt_msg.setEmojiconSize(33);
                        String dazzle = text.substring(3)
                                .toString();
                        String[] parts = dazzle.split("-");

                        String part1 = parts[0];
                        String part2 = parts[1];
                        String part3 = parts[2];
                        String part4 = parts[3];
                        String part5 = parts[4];
                        txt_msg.setText(part1);

                        ContentValues cv1 = new ContentValues();
                        cv1.put("msg", part1);
                        cv1.put("shape", part4);
                        cv1.put("speed", part3);
                        cv1.put("background", part5);

                        insertToLEDDB(cv1);

                        txt_msg.setTypeface(null, Typeface.NORMAL);

                        String query1 = "select status  from "
                                + Dbhelper.TABLE_DEFAULT_STATUS;
                        callstatusDB(query1);

                        if (mstatus.equalsIgnoreCase("1")) {
                            Constant.mDefaultScroll = true;
                        } else if (mstatus
                                .equalsIgnoreCase("0")) {
                            Constant.mDefaultScroll = false;
                        }

                        if (Constant.msg_list_adapter
                                .get(Constant.msg_list_adapter
                                        .size() - 1).getData() != null) {
                            String scroll = Constant.msg_list_adapter
                                    .get(Constant.msg_list_adapter
                                            .size() - 1).getData()
                                    .toString();
                            if (scroll.length() > 3) {
                                char ss = scroll.charAt(0);
                                char ss1 = scroll.charAt(1);
                                char ss2 = scroll.charAt(2);

                                if (ss1 == 'z' && ss2 == '>') {
                                    String[] scrollparts = scroll
                                            .split("-");

                                    String scrollpart1 = scrollparts[0];
                                    String scrollpart2 = scrollparts[1];
                                    String scrollpart3 = scrollparts[2];
                                    String scrollpart4 = scrollparts[3];
                                    String scrollpart5 = scrollparts[4];
                                    System.out.println("gooodddddddd ????? " + scrollpart1 + "   " + scrollpart2
                                            + "    " + scrollpart3 + "    " + scrollpart4 + "   " + scrollpart5);
                                    mSeenList.clear();
                                    String query = "select seen  from "
                                            + Dbhelper.TABLE_ZZLE;
                                    callZzleDB(query);
                                    Constant.mZzleText = txt_msg
                                            .getText().toString();
                                    HashSet hs = new HashSet();

                                    hs.addAll(mSeenList);
                                    mSeenList.clear();
                                    mSeenList.addAll(hs);

                                    if (mSeenList.contains(scrollpart2)) {

                                    } else
                                    {
                                        if (Constant.mDefaultScroll == true)
                                        {
                                            BannerActivityZzleAdapter.mZzleText
                                                    = scrollpart1.substring(3);
                                            BannerActivityZzleAdapter.mZzleTexTime
                                                    = scrollpart2;
                                            BannerActivityZzleAdapter.mZzleTextShape = scrollpart5;
                                            BannerActivityZzleAdapter.mZzleTextSize = scrollpart4;
                                            BannerActivityZzleAdapter.mZzleTextSpeed = scrollpart3;
                                            Intent intent = new
                                                    Intent(
                                                    mParentActivity,
                                                    BannerActivityZzleAdapter.class);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            }
                        }

                        LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        bubbleImgParams.gravity = Gravity.LEFT;

                        txt_msg.setLayoutParams(bubbleImgParams);

                    } else if (s1 == 'l' && s2 == '>') {
                        txt_msg.setTextColor(Color.YELLOW);
                        txt_msg.setText(text.substring(3));

                        OrientationGroup.mZzleTextor = txt_msg
                                .getText().toString();
                        txt_msg.setTypeface(null, Color.YELLOW);

                        txt_msg.setBackground(null);
                        txt_msg.setTextSize(valueText());
                        txt_msg.setEmojiconSize(33);
                        txt_msg.setTypeface(null, Typeface.NORMAL);

                        LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        bubbleImgParams.gravity = Gravity.LEFT;
                        //                                bubbleImgParams.leftMargin = Constant.screenWidth * 15 / 100;

                        txt_msg.setLayoutParams(bubbleImgParams);

                    } else if (s1 == 'x' && s2 == '>') {
                        String dazzle = text.substring(3)
                                .toString();
                        String[] parts = dazzle.split("-");
                        String part1 = parts[0];
                        String part2 = parts[1];
                        String part3 = parts[2];
                        String part4 = parts[3];
                        String part5 = parts[4];

                        Constant.printMsg("nymss::>>>" + part1
                                + "  " + part2 + "  " + part3
                                + "  " + part4 + "  " + part5);
                        txt_msg.setText(part5);
                        ContentValues cv = new ContentValues();

                        cv.put("msg", part5);
                        cv.put("backgrount", part1);
                        cv.put("font", part4);
                        cv.put("speed", part2);
                        cv.put("fontcolor", part3);

                        insertzzleDB(cv);

                        txt_msg.setTextColor(Color.RED);
                        txt_msg.setText(part5);

                        //                                ZzlePreviewGroup.mZzleTextor = txt_msg
                        //                                        .getText().toString();
                        txt_msg.setTypeface(null, Color.RED);

                        txt_msg.setBackground(null);
                        txt_msg.setGravity(Gravity.CENTER
                                | Gravity.LEFT);
                        txt_msg.setTextSize(valueText());
                        txt_msg.setEmojiconSize(33);
                        txt_msg.setTypeface(null, Typeface.NORMAL);

                        String query1 = "select status  from "
                                + Dbhelper.TABLE_DEFAULT_STATUS;
                        callstatusDB(query1);

                        if (mstatus.equalsIgnoreCase("1")) {
                            Constant.mDefaultScroll = true;
                        } else if (mstatus
                                .equalsIgnoreCase("0")) {
                            Constant.mDefaultScroll = false;
                        }

                        if (Constant.msg_list_adapter
                                .get(Constant.msg_list_adapter
                                        .size() - 1).getData() != null) {
                            String scroll = Constant.msg_list_adapter
                                    .get(Constant.msg_list_adapter
                                            .size() - 1).getData();

                            if (scroll.length() > 3) {
                                char ss = scroll.charAt(0);
                                char ss1 = scroll.charAt(1);
                                char ss2 = scroll.charAt(2);
                                if (ss1 == 'x' && ss2 == '>') {
                                    String[] scrollparts = scroll
                                            .split("-");
                                    String scrollpart1 = scrollparts[0];
                                    String scrollpart2 = scrollparts[1];
                                    String scrollpart3 = scrollparts[2];
                                    String scrollpart4 = scrollparts[3];
                                    String scrollpart5 = scrollparts[4];
                                    String scrollpart6 = scrollparts[5];

                                    mSeenList.clear();
                                    String query = "select seen  from "
                                            + Dbhelper.TABLE_ZZLE;
                                    callZzleDB(query);
                                    Constant.mZzleText = txt_msg
                                            .getText().toString();
                                    HashSet hs = new HashSet();

                                    hs.addAll(mSeenList);
                                    mSeenList.clear();
                                    mSeenList.addAll(hs);

                                    if (mSeenList
                                            .contains(scrollpart6)) {

                                    } else {

                                        if (Constant.mDefaultScroll == true) {

                                            if(k==msg_list.size()-1) {
                                                BannerActivityDazzAdapter.mZzleText = scrollpart5;
                                                BannerActivityDazzAdapter.mZzleTexTime = scrollpart6;
                                                Intent intent = new Intent(
                                                        mParentActivity,
                                                        BannerActivityDazzAdapter.class);
                                                startActivity(intent);
                                            }

                                        }
                                    }

                                }
                            }
                        }
                        LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        bubbleImgParams.gravity = Gravity.LEFT;

                        txt_msg.setLayoutParams(bubbleImgParams);


                    } else if (s1 == 'k' && s2 == '>') {

                        String kons_array = text.substring(3)
                                .toString();
                        String[] parts = kons_array.split("-");
                        final String part1 = parts[0];
                        String part2 = parts[1];
                        final String part3 = parts[2];

                        Constant.printMsg("testststststtstststskkkkkk" + part1 + part2 + part3);
                        String bubblecolor = part2;
                        String mShape = part1.trim();
                        if (!bubblecolor.equalsIgnoreCase("")) {
                            switch (bubblecolor) {
                                case "Blue":
                                    if (mShape.equalsIgnoreCase("oval_left")) {
                                        txt_msg.setBackgroundResource(R.drawable.blue_one);
                                    }
                                    if (mShape.equalsIgnoreCase("cross_rect_right")) {
                                        txt_msg.setBackgroundResource(R.drawable.blue_two);
                                    }
                                    if (mShape.equalsIgnoreCase("oval_right")) {
                                        txt_msg.setBackgroundResource(R.drawable.blue_three);
                                    }
                                    if (mShape.equalsIgnoreCase("rect_left")) {
                                        txt_msg.setBackgroundResource(R.drawable.blue_four);
                                    }
                                    if (mShape.equalsIgnoreCase("rect_right")) {
                                        txt_msg.setBackgroundResource(R.drawable.blue_five);
                                    }
                                    if (mShape.equalsIgnoreCase("cross_rect_left")) {
                                        txt_msg.setBackgroundResource(R.drawable.blue_six);
                                    }
                                    if (mShape.equalsIgnoreCase("cloud_left")) {
                                        txt_msg.setBackgroundResource(R.drawable.blue_seven);
                                    }
                                    break;
                                case "Green":
                                    if (mShape.equalsIgnoreCase("oval_left")) {
                                        txt_msg.setBackgroundResource(R.drawable.green_one);
                                    }
                                    if (mShape.equalsIgnoreCase("cross_rect_right")) {
                                        txt_msg.setBackgroundResource(R.drawable.green_two);
                                    }
                                    if (mShape.equalsIgnoreCase("oval_right")) {
                                        txt_msg.setBackgroundResource(R.drawable.green_three);
                                    }
                                    if (mShape.equalsIgnoreCase("rect_left")) {
                                        txt_msg.setBackgroundResource(R.drawable.green_four);
                                    }
                                    if (mShape.equalsIgnoreCase("rect_right")) {
                                        txt_msg.setBackgroundResource(R.drawable.green_five);
                                    }
                                    if (mShape.equalsIgnoreCase("cross_rect_left")) {
                                        txt_msg.setBackgroundResource(R.drawable.green_six);
                                    }
                                    if (mShape.equalsIgnoreCase("cloud_left")) {
                                        txt_msg.setBackgroundResource(R.drawable.green_seven);
                                    }
                                    break;
                                case "Pink":
                                    if (mShape.equalsIgnoreCase("oval_left")) {
                                        txt_msg.setBackgroundResource(R.drawable.pink_one);
                                    }
                                    if (mShape.equalsIgnoreCase("cross_rect_right")) {
                                        txt_msg.setBackgroundResource(R.drawable.pink_two);
                                    }
                                    if (mShape.equalsIgnoreCase("oval_right")) {
                                        txt_msg.setBackgroundResource(R.drawable.pink_three);
                                    }
                                    if (mShape.equalsIgnoreCase("rect_left")) {
                                        txt_msg.setBackgroundResource(R.drawable.pink_four);
                                    }
                                    if (mShape.equalsIgnoreCase("rect_right")) {
                                        txt_msg.setBackgroundResource(R.drawable.pink_five);
                                    }
                                    if (mShape.equalsIgnoreCase("cross_rect_left")) {
                                        txt_msg.setBackgroundResource(R.drawable.pink_six);
                                    }
                                    if (mShape.equalsIgnoreCase("cloud_left")) {
                                        txt_msg.setBackgroundResource(R.drawable.pink_seven);
                                    }
                                    break;
                            }

                        }

                        txt_msg.setText(part3);


                        txt_msg.setTextColor(Color.WHITE);
                        LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        if (part3.length() > 5)
                            bubbleImgParams.width = Constant.screenWidth * 35 / 100;
                        else
                            bubbleImgParams.width = Constant.screenWidth * 28 / 100;
                        bubbleImgParams.height = Constant.screenHeight * 9 / 100;
                        bubbleImgParams.gravity = Gravity.LEFT;
                        bubbleImgParams.topMargin = Constant.screenWidth * 2 / 100;
                        bubbleImgParams.leftMargin = Constant.screenWidth * 2 / 100;

                        txt_msg.setLayoutParams(bubbleImgParams);
                        txt_msg.setGravity(Gravity.CENTER);
                        Constant.typeFaceKons(getApplicationContext(), txt_msg);
                        if (Constant.screenWidth >= 600) {
                            Constant.printMsg("konss caleedd 1" + part3.length());
                            //                                        if (part3.length() == 4) {
                            Constant.printMsg("konss caleedd 6771 :::::" + part3.length() + "  "
                                    + Constant.screenHeight + "  " + Constant.screenWidth);

                            // text = " " + text + "  ";


                            txt_msg.setEmojiconSize(37);


                        } else if (Constant.screenWidth > 501
                                && Constant.screenWidth < 600) {
                            Constant.printMsg("konss caleedd 890");

                            txt_msg.setEmojiconSize(36);


                        } else if (Constant.screenWidth > 260
                                && Constant.screenWidth < 500) {
                            Constant.printMsg("caleedd 3");

                            txt_msg.setEmojiconSize(35);

                        } else if (Constant.screenWidth <= 260) {
                            Constant.printMsg("caleedd 4");


                            txt_msg.setEmojiconSize(34);


                        }

                    } else if (s1 == 'd' && s2 == '>') {

                        txt_msg.setText(Constant.mSenderName
                                + " donates " + text.substring(3)
                                + " BuxS for you");
                        txt_msg.setTextColor(Color.MAGENTA);
                        txt_msg.setTypeface(null, Color.MAGENTA);

                        txt_msg.setBackground(null);
                        txt_msg.setMinimumWidth(Constant.screenWidth);
                        txt_msg.setTextSize(
                                TypedValue.COMPLEX_UNIT_SP,
                                msg_font_size);

                        // Constant.mDonateBuxList.add(object);
                        //                } else if (s1 == 'a' && s2 == '>') {
                        //
                        //                    String texts = text.substring(3).toString();
                        //                    String[] parts = texts.split("-");
                        //                    final String part1 = parts[0];
                        //                    String part2 = parts[1];
                        //                    mSeenList.clear();
                        //                    String query = "select buxdonate  from "
                        //                            + Dbhelper.TABLE_DONATE;
                        //                    callZzleDB(query);
                        //
                        //                    HashSet hs = new HashSet();
                        //                    hs.addAll(mSeenList);
                        //                    mSeenList.clear();
                        //                    mSeenList.addAll(hs);
                        //
                        //                    txt_msg.setText(Constant.mSenderName
                        //                            + " accepted your BuxS");
                        //                    txt_msg.setTypeface(null, Color.BLACK);
                        //
                        //                    txt_msg.setBackground(null);
                        //                    txt_msg.setMinimumWidth(Constant.screenWidth);
                        //                    txt_msg.setTextSize(
                        //                            TypedValue.COMPLEX_UNIT_SP,
                        //                            msg_font_size);
                        //
                        //                    if (mSeenList.contains(part2)) {
                        //
                        //                    } else {
                        //
                        //                        ContentValues cv = new ContentValues();
                        //                        cv.put("buxdonate", part2);
                        //                        callDonateBux(cv);
                        //
                        //                        String jid = KachingMeApplication.getjid()
                        //                                .split("@")[0];
                        //                        contact = dbAdapter.getContact(jid);
                        //                        name = Constant.mSenderName;
                        //                        String from_no = KachingMeApplication
                        //                                .getjid().split("@")[0];
                        //                        mFromNum = KachingMeApplication.getjid()
                        //                                .split("@")[0];
                        //                        mToNUm = Constant.mReceiverId
                        //                                .split("@")[0];
                        //
                        //                        // MBuxs = Constant.donatepoint;
                        //                        MBuxs = Long.valueOf(part1);
                        //
                        //                        new getDonation().execute();
                        //                        // Intent intent = new Intent(context,
                        //                        // com.wifin.kaching.me.ui.Chat.class);
                        //                        // context.startActivity(intent);
                        //                    }
                        //
                    } else {
                        txt_msg.setTextColor(Color.BLACK);

                        txt_msg.setBackground(null);
                        txt_msg.setTextSize(valueText());
                        txt_msg.setEmojiconSize(33);
                        txt_msg.setTypeface(null, Typeface.NORMAL);

                        LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        bubbleImgParams.gravity = Gravity.LEFT;
                        //                                bubbleImgParams.leftMargin = Constant.screenWidth * 15 / 100;

                        txt_msg.setLayoutParams(bubbleImgParams);


                    }

                } else {
                    txt_msg.setTextColor(Color.BLACK);
                    txt_msg.setTypeface(null, Typeface.NORMAL);

                    txt_msg.setBackground(null);
                    txt_msg.setTextSize(valueText());
                    txt_msg.setEmojiconSize(33);
                    LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    bubbleImgParams.gravity = Gravity.LEFT;
                    //                            bubbleImgParams.leftMargin = Constant.screenWidth * 15 / 100;

                    txt_msg.setLayoutParams(bubbleImgParams);

                }

                // }
            } else {

                txt_msg.setTextColor(Color.BLACK);
                txt_msg.setTypeface(null, Typeface.NORMAL);

                txt_msg.setBackground(null);
                txt_msg.setMinimumWidth(Constant.screenWidth);
                txt_msg.setTextSize(valueText());
                txt_msg.setEmojiconSize(33);
                LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                bubbleImgParams.gravity = Gravity.LEFT;
                //                        bubbleImgParams.leftMargin = Constant.screenWidth * 15 / 100;

                txt_msg.setLayoutParams(bubbleImgParams);

            }

            txt_msg.setTag(k);

            txt_msg.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // Constant.printMsg("clicked syms:::::>>>>>>");
                    final EmojiconTextView txt_msg = (EmojiconTextView) v;

                    boolean toggle = true;

                    int position = (Integer) v.getTag();

                    String text = txt_msg.getText().toString();


//                    if (txt_msg.getCurrentTextColor() == -65536) {
//
//                        Constant.zzle = false;
//
//                        BannerActivityLED.mZzleText = txt_msg
//                                .getText().toString();
//                        Intent intent = new Intent(mParentActivity,
//                                BannerActivityLED.class);
//                        mParentActivity.startActivity(intent);
//                    }

                    if (txt_msg.getCurrentTextColor() == -256) {

                        OrientationGroup.mZzleTextor = txt_msg
                                .getText().toString();

                        // HorizonalSlideshow.mZzleTextor = txt_msg
                        // .getText().toString();

                        Intent intent = new Intent(mParentActivity,
                                OrientationGroup.class);
                        mParentActivity.startActivity(intent);

                        // }

                    }
                    if (txt_msg.getCurrentTextColor() == -65536) {
                        MessageGetSet selectedItem1 = Constant.msg_list_adapter
                                .get(position);
                        System.out.println("testing group dazZ"+selectedItem1.getData());
                        if (selectedItem1.getData().startsWith("<x>")) {
                            Constant.mZzleText = txt_msg.getText()
                                    .toString();

                            BannerActivityChat.mZzleText = txt_msg
                                    .getText().toString();
                            String value1 = selectedItem1.getData()
                                    .substring(3).toString();
                            String[] parts = value1.split("-");

                            String part1 = parts[0];
                            String part2 = parts[1];
                            String part3 = parts[2];
                            String part4 = parts[3];
                            String part5 = parts[4];
                            BannerActivityChat.mZzleTextBackground = part1;
                            BannerActivityChat.mZzleTextColor = part3;
                            BannerActivityChat.mZzleTextSize = part4;
                            BannerActivityChat.mZzleTextSpeed = part2;
                            Intent intent = new Intent(mParentActivity,
                                    BannerActivityChat.class);
                            mParentActivity.startActivity(intent);
                        } else {
                            Constant.zzle = false;

                            BannerActivityLED.mZzleText = txt_msg
                                    .getText().toString();

                            String value1 = selectedItem1.getData()
                                    .substring(3).toString();
                            String[] parts = value1.split("-");
                            String part1 = parts[0];
                            String part2 = parts[1];
                            String part3 = parts[2];
                            String part4 = parts[3];
                            String part5 = parts[4];
                            BannerActivityLED.mZzleTextBackground =part5;
                            BannerActivityLED.mZzleTextSpeed =part3;
                            BannerActivityLED.mZzleTextSize =part4;

                            Intent intent = new Intent(mParentActivity,
                                    BannerActivityLED.class);
                            mParentActivity.startActivity(intent);
                        }
                    }
                }

            });
        } catch (Exception e) {

        }


    }



    public void setRightImage()
    {
        try {
            mRightImageChat.setVisibility(View.VISIBLE);


            if (msg_list.get(k).getStatus() == 3) {
                mRightImageTickMark.setImageResource(R.drawable.message_unsent);
            } else if (msg_list.get(k).getStatus() == 2) {
                mRightImageTickMark
                        .setImageResource(R.drawable.receipt_from_server);
            } else if (msg_list.get(k).getStatus() == 1 || msg_list.get(k).getStatus() == 0) {
                mRightImageTickMark
                        .setImageResource(R.drawable.receipt_from_target);
            } else if (msg_list.get(k).getStatus() == -1) {
                mRightImageTickMark
                        .setImageResource(R.drawable.receipt_read);
            }

            String fromuser = msg_list.get(k).getKey_remote_jid();

            // Set timestamp
            Date date = new Date();
            date.setTime(msg_list.get(k).getTimestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mRightImageTextTime.setText(time_format.format(date));

            Constant.printMsg("MUC image : " + msg_list.get(k).getMedia_name());

            File file = null;
            try {
                file = new File(Constant.local_image_dir
                        + msg_list.get(k).getMedia_name());
            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);

            }


            String values = msg_list.get(k).get_id() + "," + fromuser;
            mRightImageChatUpload.setTag(values);
            mRightImageChatUpload.setVisibility(View.GONE);
            mRightImageChatCancel.setTag(values);
            mRightImageChatCancel.setVisibility(View.GONE);
            mRightImageProgress.setVisibility(View.GONE);

            if (file != null) {

                Bitmap unscaledBitmapRight =  new CompressImage().compressImage(Constant.local_image_dir
                        + msg_list.get(k).getMedia_name(), "",1);

                Log.d("Image Width",
                        "Image Width::" + (int) width * 50 / 100 + " layout::"
                                + (int) height * 30 / 100
                                + " image view::"
                                + msg_list.get(k).getStatus()
                                + " image view height::"
                                + msg_list.get(k).getNeeds_push()
                                + "bitmap widh::"
                                + unscaledBitmapRight.getWidth());

                mRightImageChat.setImageBitmap(unscaledBitmapRight);
                mRightImageChat.setTag(Constant.local_image_dir
                        + msg_list.get(k).getMedia_name());

                boolean isConn = Connectivity.isOnline(mParentActivity);

                if(!mAsyncUpload_Image.containsKey(String.valueOf(msg_list.get(k).get_id())))
                    mAsyncUpload_Image.put(String.valueOf(msg_list.get(k).get_id()), new AsyncHttpClient());

                if (msg_list.get(k).getStatus() == 3
                        && msg_list.get(k).getNeeds_push() == 1) {

                    if (!isFirstTime) {
                        if(isConn) {
                            mRightImageChatUpload.setVisibility(View.GONE);
                            mRightImageChatCancel.setVisibility(View.VISIBLE);
                            mRightImageProgress
                                    .setVisibility(View.VISIBLE);

                            dbAdapter.setUpdateMessage_need_push(msg_list.get(k).getKey_id(), 2);

                            new uploa_image().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(msg_list.get(k).get_id()),
                                    fromuser);
                        }else
                        {
                            mRightImageChatUpload.setVisibility(View.VISIBLE);
                            mRightImageChatCancel.setVisibility(View.GONE);
                            mRightImageProgress
                                    .setVisibility(View.GONE);
                        }
                    }else
                    {
                        mRightImageChatUpload.setVisibility(View.VISIBLE);
                        mRightImageChatCancel.setVisibility(View.GONE);
                        mRightImageProgress
                                .setVisibility(View.GONE);
                    }
                } else if (msg_list.get(k).getNeeds_push() == 2
                        && msg_list.get(k).getStatus() == 3) {

                    if(!isFirstTime)
                    {
                        if(isConn) {
                            mRightImageChatUpload.setVisibility(View.GONE);
                            mRightImageChatCancel.setVisibility(View.VISIBLE);
                            mRightImageProgress
                                    .setVisibility(View.VISIBLE);
                            new uploa_image().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(msg_list.get(k).get_id()),
                                    fromuser);
                        }else
                        {
                            mRightImageChatUpload.setVisibility(View.VISIBLE);
                            mRightImageChatCancel.setVisibility(View.GONE);
                            mRightImageProgress
                                    .setVisibility(View.GONE);
                        }
                    }else {

                        mRightImageChatUpload.setVisibility(View.GONE);
                        mRightImageChatCancel.setVisibility(View.VISIBLE);
                        mRightImageProgress
                                .setVisibility(View.VISIBLE);
                    }

                } else if (msg_list.get(k).getStatus() == 2
                        && msg_list.get(k).getMedia_url() != null) {
                    mRightImageChatUpload.setVisibility(View.GONE);
                    mRightImageProgress.setVisibility(View.GONE);
                    mRightImageChatCancel.setVisibility(View.GONE);

                }
            } else {
                mRightImageChatUpload.setVisibility(View.GONE);
                mRightImageProgress.setVisibility(View.GONE);
                mRightImageChatCancel.setVisibility(View.GONE);
                byte[] image_data = msg_list.get(k).getRow_data();
                Bitmap bitmap = BitmapFactory.decodeByteArray(
                        image_data, 0, image_data.length,Util.getBitmapOptions());
                mRightImageChat.setImageBitmap(bitmap);
                mRightImageChat.setTag(null);
            }

            mRightImageChat.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(Connectivity.isOnline(mParentActivity)) {

                        if (v.getTag() != null) {
                            String path = v.getTag().toString();
                            File f = new File(path);
                            Uri uri = Uri.fromFile(f);

                            Intent intent = new Intent(
                                    Intent.ACTION_VIEW);
                            String mime = "*/*";
                            MimeTypeMap mimeTypeMap = MimeTypeMap
                                    .getSingleton();
                            if (mimeTypeMap.hasExtension(mimeTypeMap
                                    .getFileExtensionFromUrl(uri
                                            .toString())))
                                mime = mimeTypeMap
                                        .getMimeTypeFromExtension(mimeTypeMap
                                                .getFileExtensionFromUrl(uri
                                                        .toString()));
                            intent.setDataAndType(uri, mime);
                            mParentActivity.startActivity(intent);

                        }
                    }else
                    {
                        Toast.makeText(mParentActivity, "No network available", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            final ProgressBar progressBar = mRightImageProgress;
            final ImageView imgView = mRightImageChatUpload;
            final ImageView imgView_cancel = mRightImageChatCancel;

            mRightImageChatUpload
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String[] val = v.getTag().toString()
                                    .split(",");
                            new uploa_image().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, val[0],
                                    val[1]);


                            progressBar
                                    .setVisibility(View.VISIBLE);
                            imgView
                                    .setVisibility(View.GONE);
                            imgView_cancel
                                    .setVisibility(View.VISIBLE);


                        }
                    });

            mRightImageChatCancel
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String[] val = v.getTag().toString()
                                    .split(",");


                            mAsyncUpload_Image.get(val[0]).cancelAllRequests(true);


                            progressBar
                                    .setVisibility(View.GONE);
                            imgView
                                    .setVisibility(View.VISIBLE );
                            imgView_cancel
                                    .setVisibility(View.GONE);


                        }
                    });
        } catch (Exception e) {

        }

    }

    public void setRightVideo()
    {


        try {
            if (msg_list.get(k).getStatus() == 3) {
                mRightVideoTickMark.setImageResource(R.drawable.message_unsent);
            } else if (msg_list.get(k).getStatus() == 2) {
                mRightVideoTickMark
                        .setImageResource(R.drawable.receipt_from_server);
            } else if (msg_list.get(k).getStatus() == 1 || msg_list.get(k).getStatus() == 0) {
                mRightVideoTickMark
                        .setImageResource(R.drawable.receipt_from_target);
            } else if (msg_list.get(k).getStatus() == -1) {
                mRightVideoTickMark
                        .setImageResource(R.drawable.receipt_read);
            }
            // Set timestamp
            Date date = new Date();
            date.setTime(msg_list.get(k).getSend_timestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mRightVideoTimeText.setText(time_format.format(date));

            MessageGetSet  msg = msg_list.get(k);
            String fromuser = msg.getKey_remote_jid();
            mRightVideoChat.setVisibility(View.VISIBLE);


            mRightVideoDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                    notificatiob_font_size);
            mRightVideoSize.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                    notificatiob_font_size);

            mRightVideoChat.setFocusable(false);
            mRightVideoChatUpload.setFocusable(false);
            mRightVideoChatCancel.setFocusable(false);
            mRightVideoChatUpload.setFocusable(false);
            mRightVideoButtonPlay.setFocusable(false);
            mRightVideoDuration.setFocusable(false);
            mRightVideoSize.setFocusable(false);


            long second = (msg.getMedia_duration() / 1000) % 60;
            long minute = (msg.getMedia_duration() / (1000 * 60)) % 60;

            String time = String.format("%02d:%02d", minute, second);

            mRightVideoDuration.setText(time);


            mRightVideoSize.setText(Network_Usage.convertBytesToSuitableUnit(String.valueOf(msg.getMedia_size())));

            File file = new File(Constant.local_video_dir
                    + msg.getMedia_name());


            mRightVideoChat.getLayoutParams().width = (int) width * 57 / 100;
            mRightVideoChat.getLayoutParams().height = (int) height * 28 / 100;

            String values = msg.get_id() + "," + fromuser;
            mRightVideoChatUpload.setTag(values);
            mRightVideoChatUpload.setVisibility(View.GONE);
            mRightVideoChatCancel.setTag(values);
            mRightVideoChatCancel.setVisibility(View.GONE);
            mRightVideoProgress.setVisibility(View.GONE);

            try {
                byte[] image_data = msg.getRow_data();
                Bitmap bitmap = BitmapFactory.decodeByteArray(
                        image_data, 0, image_data.length,Util.getBitmapOptions());
                mRightVideoChat.setImageBitmap(bitmap);
                mRightVideoChat.setTag(null);
            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                // TODO: handle exception
            }


            final boolean isConnected = Connectivity.isOnline(mParentActivity);
            if(!mAsyncUpload_Video.containsKey(String.valueOf(msg_list.get(k).get_id())))
                mAsyncUpload_Video.put(String.valueOf(msg_list.get(k).get_id()), new AsyncHttpClient());

            Constant.printMsg("......Chat Video 3..data...." + msg_list.get(k).getNeeds_push()+" "+msg_list.get(k).getStatus()
            );

            if (msg_list.get(k).getStatus() == 3 &&
                    msg_list.get(k).getNeeds_push() ==1) {

                Constant.printMsg("......Chat Video 3..1...." + isConnected
                );

                if (!isFirstTime) {
                    if(isConnected) {

                        mRightVideoChatUpload.setVisibility(View.GONE);
                        mRightVideoChatCancel.setVisibility(View.VISIBLE);
                        mRightVideoProgress.setVisibility(View.VISIBLE);
                        mRightVideoButtonPlay.setVisibility(View.GONE);
                        ConcurrentAsyncTaskExecutor.executeConcurrently( new uploa_video(), String.valueOf(msg_list.get(k).get_id()), fromuser);
                    } else
                    {
                        mRightVideoChatUpload.setVisibility(View.VISIBLE);
                        mRightVideoChatCancel.setVisibility(View.GONE);
                        mRightVideoProgress.setVisibility(View.GONE);
                        mRightVideoButtonPlay.setVisibility(View.GONE);

                    }
                }else
                {
                    mRightVideoChatUpload.setVisibility(View.VISIBLE);
                    mRightVideoChatCancel.setVisibility(View.GONE);
                    mRightVideoProgress.setVisibility(View.GONE);
                    mRightVideoButtonPlay.setVisibility(View.GONE);
                }


            } else if (msg_list.get(k).getStatus() == 3
                    && msg_list.get(k).getNeeds_push() == 2 ) {

                Constant.printMsg("......Chat Video 2..null url...."
                );

                mRightVideoChatUpload.setVisibility(View.GONE);
                mRightVideoChatCancel.setVisibility(View.VISIBLE);
                mRightVideoProgress.setVisibility(View.VISIBLE);
                mRightVideoButtonPlay.setVisibility(View.GONE);


            } else if (msg_list.get(k).getStatus() == 2
                    && msg_list.get(k).getMedia_url() != null) {

                Constant.printMsg("......Chat Video 2..not null url...."
                );

                mRightVideoButtonPlay.setVisibility(View.VISIBLE);
                mRightVideoButtonPlay.setTag(Constant.local_video_dir
                        + msg_list.get(k).getMedia_name());
                mRightVideoChatUpload.setVisibility(View.GONE);
                mRightVideoChatCancel.setVisibility(View.GONE);
                mRightVideoProgress.setVisibility(View.GONE);

            } else {

                Constant.printMsg("......Chat Video 0 1 -1......"
                );

                mRightVideoButtonPlay.setVisibility(View.VISIBLE);
                mRightVideoButtonPlay.setTag(Constant.local_video_dir
                        + msg_list.get(k).getMedia_name());
                mRightVideoChatUpload.setVisibility(View.GONE);
                mRightVideoChatCancel.setVisibility(View.GONE);
                mRightVideoProgress.setVisibility(View.GONE);
            }

            mRightVideoChat.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getTag() != null) {
                        String path = v.getTag().toString();
                        File f = new File(path);
                        Uri uri = Uri.fromFile(f);
                        Intent intent = new Intent(
                                Intent.ACTION_VIEW);
                        String mime = "*/*";
                        MimeTypeMap mimeTypeMap = MimeTypeMap
                                .getSingleton();
                        if (mimeTypeMap.hasExtension(mimeTypeMap
                                .getFileExtensionFromUrl(uri
                                        .toString())))
                            mime = mimeTypeMap
                                    .getMimeTypeFromExtension(mimeTypeMap
                                            .getFileExtensionFromUrl(uri
                                                    .toString()));
                        intent.setDataAndType(uri, mime);
                        mParentActivity.startActivity(intent);
                    }

                }
            });

            final ProgressBar progressBar = mRightVideoProgress;
            final ImageView upl_img = mRightVideoChatUpload;
            final ImageView upl_img_cancl = mRightVideoChatCancel;
            final ImageView ply_vid = mRightVideoButtonPlay;
            mRightVideoChatUpload
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String[] val = v.getTag().toString()
                                    .split(",");
                            ConcurrentAsyncTaskExecutor.executeConcurrently( new uploa_video(),val[0],
                                    val[1]);


                            progressBar
                                    .setVisibility(View.VISIBLE);
                            upl_img
                                    .setVisibility(View.GONE);
                            upl_img_cancl
                                    .setVisibility(View.VISIBLE);
                            ply_vid.setVisibility(View.GONE);
                        }
                    });

            mRightVideoChatCancel
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String[] val = v.getTag().toString()
                                    .split(",");
                            mAsyncUpload_Video.get(val[0]).cancelAllRequests(true);


                            progressBar
                                    .setVisibility(View.GONE);
                            upl_img
                                    .setVisibility(View.VISIBLE);
                            upl_img_cancl
                                    .setVisibility(View.GONE);
                            ply_vid.setVisibility(View.GONE);
                        }
                    });
            mRightVideoButtonPlay.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String uri = v.getTag().toString();

                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(uri),
                                "video/mp4");
                        mParentActivity.startActivity(intent);
                    } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                        AlertDialog.Builder b = new AlertDialog.Builder(
                                mParentActivity);
                        b.setMessage(
                                mParentActivity.getResources()
                                        .getString(
                                                R.string.dont_have_app_to_play_video))
                                .setCancelable(false);
                        b.setNegativeButton(
                                mParentActivity.getResources().getString(
                                        R.string.Ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int id) {

                                    }
                                });
                        AlertDialog alert = b.create();
                        alert.show();
                    }

                }
            });
        } catch (Exception e) {

        }
    }

    public void setRightContact()
    {


        try {
            if (msg_list.get(k).getStatus() == 3) {
                mRightContactTickMark.setImageResource(R.drawable.message_unsent);
            } else if (msg_list.get(k).getStatus() == 2) {
                mRightContactTickMark
                        .setImageResource(R.drawable.receipt_from_server);
            } else if (msg_list.get(k).getStatus() == 1 || msg_list.get(k).getStatus() == 0) {
                mRightContactTickMark
                        .setImageResource(R.drawable.receipt_from_target);
            } else if (msg_list.get(k).getStatus() == -1) {
                mRightContactTickMark
                        .setImageResource(R.drawable.receipt_read);
            }

// Set timestamp
            Date date = new Date();
            date.setTime(msg_list.get(k).getSend_timestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mRightContactTextTime.setText(time_format.format(date));

            MessageGetSet msg = msg_list.get(k);

            Constant.printMsg("Diliiip "+ msg_list.get(k).getMedia_name());
            String contactName = msg_list.get(k).getMedia_name().split(",")[0];


            mRightContactTextView.setText(contactName);
            String values = msg_list.get(k).getMedia_name();
            mRightContactTextView.setTag(values);
            mRightContactTextView.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP, msg_font_size);
            try {
                byte[] image_data = msg.getRow_data();
                Bitmap bitmap = BitmapFactory.decodeByteArray(
                        image_data, 0, image_data.length,Util.getBitmapOptions());
                mRightContactAvathor.setImageBitmap(bitmap);
            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
            }

            mRightContactAvathor.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getTag() != null) {

                    }

                }
            });

            mRightContactTextView
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mParentActivity,
                                    ContactView_Normal.class);
                            intent.putExtra("vcard", v.getTag()
                                    .toString());
                            intent.putExtra("status", false);
                            mParentActivity.startActivity(intent);
                        }
                    });
        } catch (Exception e) {

        }

    }

    public void setRightLocation()
    {

        try {
            if (msg_list.get(k).getStatus() == 3) {
                mRightImageTickMark.setImageResource(R.drawable.message_unsent);
            } else if (msg_list.get(k).getStatus() == 2) {
                mRightImageTickMark
                        .setImageResource(R.drawable.receipt_from_server);
            } else if (msg_list.get(k).getStatus() == 1 || msg_list.get(k).getStatus() == 0) {
                mRightImageTickMark
                        .setImageResource(R.drawable.receipt_from_target);
            } else if (msg_list.get(k).getStatus() == -1) {
                mRightImageTickMark
                        .setImageResource(R.drawable.receipt_read);
            }
            // Set timestamp
            Date date = new Date();
            date.setTime(msg_list.get(k).getSend_timestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mRightImageTextTime.setText(time_format.format(date));

            MessageGetSet msg = msg_list.get(k);
            String fromuser = msg.getKey_remote_jid();
            mRightImageChat.setVisibility(View.VISIBLE);


            Log.d("Location", "Lat::" + msg.getLatitude() + " Lon::"
                    + msg.getLongitude());
            String lat_lon = msg.getLatitude() + ","
                    + msg.getLongitude();
            mRightImageChat.setTag(lat_lon);


            String values = msg.get_id() + "," + fromuser;
            mRightImageChatUpload.setTag(values);
            mRightImageChatUpload.setVisibility(View.GONE);
            mRightImageProgress.setVisibility(View.GONE);
            try {
                byte[] image_data = msg.getRow_data();
                Bitmap bitmap = BitmapFactory.decodeByteArray(
                        image_data, 0, image_data.length,Util.getBitmapOptions());
//                Bitmap unscaledBitmap = ScalingUtilities
//                        .decodeFile(Constant.local_image_dir
//                                + msg.getMedia_name(), (int) width * 57 / 100, (int) height * 30 / 100);
                mRightImageChat.setImageBitmap(bitmap);

            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
            }
            if (msg.getStatus() == 3
                    && msg.getNeeds_push() == 1) {
                mRightImageChatUpload.setVisibility(View.GONE);
                mRightImageProgress.setVisibility(View.VISIBLE);

            }
            if (msg.getStatus() == 2
                    && msg.getNeeds_push() == 0) {
                mRightImageChatUpload.setVisibility(View.VISIBLE);
                mRightImageProgress.setVisibility(View.GONE);
            } else {
                mRightImageChatUpload.setVisibility(View.GONE);
                mRightImageProgress.setVisibility(View.GONE);

            }

            mRightImageChat.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getTag() != null) {
                        String latitude = v.getTag().toString()
                                .split(",")[0];
                        String longitude = v.getTag().toString()
                                .split(",")[1];
                        String label = "I'm Here!";
                        String uriBegin = "geo:" + latitude + ","
                                + longitude;
                        String query = latitude + "," + longitude
                                + "(" + label + ")";
                        String encodedQuery = Uri.encode(query);
                        String uriString = uriBegin + "?q="
                                + encodedQuery + "&z=16";
                        Uri uri = Uri.parse(uriString);
                        String url = "http://maps.google.com/maps?center='" + v.getTag().toString() + "'&zoom=15&views=transit";
                        try {
                            Intent mapIntent = new Intent(
                                    Intent.ACTION_VIEW,
                                    uri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            mParentActivity.startActivity(mapIntent);
                        } catch (ActivityNotFoundException exp) {
                            mParentActivity.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                        }

                    }

                }
            });

            final ProgressBar progress_upload_image = mRightImageProgress;
            final ImageView btn_image_upload = mRightImageChatUpload;
            mRightImageChatUpload
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String[] val = v.getTag().toString()
                                    .split(",");
                                            /*
                                             * new uploa_Location().execute(val[0],
                                             * val[1]);
                                             */

                            progress_upload_image
                                    .setVisibility(View.VISIBLE);
                            btn_image_upload
                                    .setVisibility(View.VISIBLE);

                        }
                    });
        } catch (Exception e) {

        }

    }




    //----------Left ----------
    public void setLeftImage()
    {

        try {
            MessageGetSet msg = msg_list.get(k);
            mLeftImageChat.setVisibility(View.VISIBLE);
            String fromuser = msg.getKey_remote_jid();

            // Set timestamp
            Date date = new Date();
            date.setTime(msg_list.get(k).getTimestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mLeftImageTextTime.setText(time_format.format(date));

            String displayName = null;
            // Left sender name...
            ContactsGetSet contact = new ContactsGetSet();
            try {
                contact = dbAdapter.getContact(msg_list.get(k).getRemote_resource());
                Constant.printMsg("Left username : " + contact);
                if (contact == null) {
                    mLeftTextProfile.setText(msg_list.get(k).getRemote_resource().split("@")[0]);
                    displayName = msg_list.get(k).getRemote_resource().split("@")[0];
                } else {
                    mLeftTextProfile.setText(contact.getDisplay_name());
                    displayName = contact.getDisplay_name();
                }

                getColorLeftUser(displayName);

            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
            }

            mLeftImageChat.getLayoutParams().width = (int) width * 57 / 100;
            mLeftImageChat.getLayoutParams().height = (int) height * 28 / 100;

            //      mLeftImageChat.setMaxHeight(i);

            String values = msg.getKey_id() + "," + msg.getMedia_url()
                    + "," + fromuser;
            mLeftImageChatDownload.setVisibility(View.VISIBLE);
            mLeftImageChatDownload.setTag(values);

            if (msg.getMedia_name() == null) {
                byte[] img_byte = msg.getRow_data();
                if (img_byte != null) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(
                            img_byte, 0, img_byte.length);
                    mLeftImageChat.setImageBitmap(bmp);
                    try {
                        bites = (long) img_byte.length;
                        //  updateMediaNetwork_Receive(bites);
                    } catch (Exception e) {

                    }
                }
                if (is_auto_dowload_image) {
                    new download_image().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg.getKey_id(),
                            msg.getMedia_url(), fromuser);
                    mLeftImageChatDownload.setVisibility(View.GONE);
                } else {
                    mLeftImagetProgressBar
                            .setVisibility(View.GONE);

                }
            } else {
                mLeftImageChatDownload.setVisibility(View.GONE);
                mLeftImagetProgressBar.setVisibility(View.GONE);

                File file = new File(Constant.local_image_dir
                        + msg.getMedia_name());
                if (file.isFile()) {
                    Bitmap unscaledBitmap = new CompressImage().compressImage(Constant.local_image_dir
                            + msg_list.get(k).getMedia_name(), "",1);

                              /*  Log.d("Image Width",
                                        "Image Width::" + i + " layout::"
                                                + localView.getWidth()
                                                + " image view::"
                                                + image.getWidth()
                                                + " image view height::"
                                                + image.getHeight()
                                                + "bitmap widh::"
                                                + unscaledBitmap.getWidth());*/

                    mLeftImageChat.setImageBitmap(unscaledBitmap);

                    mLeftImageChat.setTag(Constant.local_image_dir
                            + msg.getMedia_name());

                } else {
                    byte[] image_data = msg.getRow_data();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(
                            image_data, 0, image_data.length,Util.getBitmapOptions());
                    mLeftImageChat.setImageBitmap(bitmap);
                    mLeftImageChat.setScaleType(ImageView.ScaleType.FIT_XY);
                    mLeftImageChat.setTag(null);

                }

            }

            final ProgressBar progress_download_image =mLeftImagetProgressBar;
            final ImageView btn_image_download = mLeftImageChatDownload;
            mLeftImageChatDownload
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            String[] val = v.getTag().toString()
                                    .split(",");
                            new download_image().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, val[0],
                                    val[1], val[2]);
                            progress_download_image
                                    .setVisibility(View.VISIBLE);
                            btn_image_download
                                    .setVisibility(View.GONE);
                            // TODO Auto-generated method stub

                        }
                    });

            mLeftImageChat.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getTag() != null) {
                        String path = v.getTag().toString();
                        File f = new File(path);
                        Uri uri = Uri.fromFile(f);
                        Intent intent = new Intent(
                                Intent.ACTION_VIEW);
                        String mime = "*/*";
                        MimeTypeMap mimeTypeMap = MimeTypeMap
                                .getSingleton();
                        if (mimeTypeMap.hasExtension(mimeTypeMap
                                .getFileExtensionFromUrl(uri
                                        .toString())))
                            mime = mimeTypeMap
                                    .getMimeTypeFromExtension(mimeTypeMap
                                            .getFileExtensionFromUrl(uri
                                                    .toString()));
                        intent.setDataAndType(uri, mime);
                        mParentActivity.startActivity(intent);
                    }

                }
            });
        } catch (Exception e) {

        }
    }




    public void setLeftContact()
    {
        try {
            // Set timestamp
            Date date = new Date();
            date.setTime(msg_list.get(k).getSend_timestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mLeftContactTextTime.setText(time_format.format(date));

            String displayName = null;
            // Left sender name...
            ContactsGetSet contact = new ContactsGetSet();

            String contactName = msg_list.get(k).getMedia_name().split(",")[0];

            try {
                contact = dbAdapter.getContact(msg_list.get(k).getRemote_resource());
                Constant.printMsg("Left username : " + contact);
                if (contact == null) {
                    mLeftTextProfile.setText(msg_list.get(k).getRemote_resource().split("@")[0]);
                    displayName = msg_list.get(k).getRemote_resource().split("@")[0];
                } else {
                    mLeftTextProfile.setText(contact.getDisplay_name());
                    displayName = contact.getDisplay_name();
                }

                getColorLeftUser(displayName);

            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
            }

            MessageGetSet msg = msg_list.get(k);
            mLeftContactTextView.setText(contactName);
            String values = msg_list.get(k).getMedia_name();
            mLeftContactAvathor.setTag(values);
            mLeftContactTextView.setTag(values);
            mLeftContactTextView.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP, msg_font_size);
            try {
                byte[] image_data = msg.getRow_data();
                Bitmap bitmap = BitmapFactory.decodeByteArray(
                        image_data, 0, image_data.length,Util.getBitmapOptions());
                mLeftContactAvathor.setImageBitmap(bitmap);
            } catch (Exception e) {
                // TODO: handle exception
                // ACRA.getErrorReporter().handleException(e);
            }

            mLeftContactAvathor.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getTag() != null) {

                    }

                }
            });

            mLeftContactTextView
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(mParentActivity,
                                    ContactView_Normal.class);
                            intent.putExtra("vcard", v.getTag()
                                    .toString());
                            intent.putExtra("status", true);
                            mParentActivity.startActivity(intent);

                        }
                    });
        } catch (Exception e) {

        }

    }

    public void setLeftVideo()
    {

        try {
            MessageGetSet msg = msg_list.get(k);
            String fromuser = msg.getKey_remote_jid();
            // Set timestamp
            Date date1 = new Date();
            date1.setTime(msg_list.get(k).getTimestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mLeftVideoTimeText.setText(time_format.format(date1));


            String displayName = null;
            // Left sender name...
            ContactsGetSet contact = new ContactsGetSet();
            try {
                contact = dbAdapter.getContact(msg_list.get(k).getRemote_resource());
                Constant.printMsg("Left username : " + contact);
                if (contact == null) {
                    mLeftTextProfile.setText(msg_list.get(k).getRemote_resource().split("@")[0]);
                    displayName = msg_list.get(k).getRemote_resource().split("@")[0];
                } else {
                    mLeftTextProfile.setText(contact.getDisplay_name());
                    displayName = contact.getDisplay_name();
                }

                getColorLeftUser(displayName);

            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
            }

            mLeftVideoChat.setVisibility(View.VISIBLE);


            mLeftVideoDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                    notificatiob_font_size);
            mLeftVideoSize.setTextSize(notificatiob_font_size,
                    notificatiob_font_size);

            long second = (msg.getMedia_duration() / 1000) % 60;
            long minute = (msg.getMedia_duration() / (1000 * 60)) % 60;

            String time = String.format("%02d:%02d", minute, second);
            mLeftVideoDuration.setText(time);

            mLeftVideoSize.setText(Network_Usage.convertBytesToSuitableUnit(String.valueOf(msg.getMedia_size())));

            String values = msg.getKey_id() + "," + msg.getMedia_url()
                    + "," + fromuser;

            mLeftVideoChatDownload.setTag(values);

            mLeftVideoChat.getLayoutParams().width = (int) width * 57 / 100;
            mLeftVideoChat.getLayoutParams().height = (int) height * 28 / 100;

            if (msg.getMedia_name() == null) {
                byte[] img_byte = msg.getRow_data();
                if (img_byte != null) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(
                            img_byte, 0, img_byte.length,Util.getBitmapOptions());
                    mLeftVideoChat.setImageBitmap(bmp);
                    mLeftVideoChat.setScaleType(ImageView.ScaleType.FIT_XY);
                    try {
                        bites = (long) img_byte.length;

                        updateMediaNetwork_Receive(bites);
                    } catch (Exception e) {

                    }
                }
                if (is_auto_dowload_video) {
                    new download_video().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg.getKey_id(),
                            msg.getMedia_url(), fromuser);
                } else {

                    // //Constant.printMsg("Media Name::"+c.getString(12));

                    mLeftVideoProgress
                            .setVisibility(View.GONE);
                    mLeftVideoChatDownload.setVisibility(View.VISIBLE);
                }
                mLeftVideoButtonPlay.setVisibility(View.GONE);
            } else {
                mLeftVideoChatDownload.setVisibility(View.GONE);
                mLeftVideoProgress.setVisibility(View.GONE);

                File file = new File(Constant.local_video_dir
                        + msg.getMedia_name());
                mLeftVideoButtonPlay.setTag(Constant.local_video_dir
                        + msg.getMedia_name());
                try {
                    byte[] image_data = msg.getRow_data();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(
                            image_data, 0, image_data.length,Util.getBitmapOptions());
                    mLeftVideoChat.setImageBitmap(bitmap);
                    mLeftVideoChat.setScaleType(ImageView.ScaleType.FIT_XY);
                    mLeftVideoChat.setTag(null);
                } catch (Exception e) {
                    // ACRA.getErrorReporter().handleException(e);
                    // TODO: handle exception
                }

                if (file.isFile()) {

                    mLeftVideoButtonPlay.setVisibility(View.VISIBLE);

                } else {

                    mLeftVideoButtonPlay.setVisibility(View.GONE);

                }

            }

            mLeftVideoChatDownload
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String[] val = v.getTag().toString()
                                    .split(",");
                            new download_video().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, val[0],
                                    val[1], val[2]);
                            mLeftVideoProgress
                                    .setVisibility(View.VISIBLE);
                            mLeftVideoChatDownload
                                    .setVisibility(View.GONE);

                        }
                    });

            mLeftVideoButtonPlay.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String uri = v.getTag().toString();

                                    /*
                                     * Intent intent = new Intent();
                                     * intent.setAction(Intent.ACTION_VIEW);
                                     * intent.setDataAndType(Uri.parse(uri),
                                     * "video/mp4"); context.startActivity(intent);
                                     */
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(uri),
                                "video/mp4");
                        mParentActivity.startActivity(intent);
                    } catch (Exception e) {

                        AlertDialog.Builder b = new AlertDialog.Builder(
                                mParentActivity);
                        b.setMessage(
                                mParentActivity.getResources()
                                        .getString(
                                                R.string.dont_have_app_to_play_video))
                                .setCancelable(false);
                        b.setNegativeButton(
                                mParentActivity.getResources().getString(
                                        R.string.Ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int id) {

                                    }
                                });
                        AlertDialog alert = b.create();
                        alert.show();
                    }

                }
            });
        } catch (Exception e) {

        }
    }

    public void setLeftLocation()
    {

        try {
            // Set timestamp
            Date date = new Date();
            date.setTime(msg_list.get(k).getReceived_timestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mLeftImageTextTime.setText(time_format.format(date));


            String displayName = null;
            // Left sender name...
            ContactsGetSet contact = new ContactsGetSet();
            try {
                contact = dbAdapter.getContact(msg_list.get(k).getRemote_resource());
                Constant.printMsg("Left username : " + contact);
                if (contact == null) {
                    mLeftTextProfile.setText(msg_list.get(k).getRemote_resource().split("@")[0]);
                    displayName = msg_list.get(k).getRemote_resource().split("@")[0];
                } else {
                    mLeftTextProfile.setText(contact.getDisplay_name());
                    displayName = contact.getDisplay_name();
                }

                getColorLeftUser(displayName);

            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
            }

            MessageGetSet msg = msg_list.get(k);
            String fromuser = msg.getKey_remote_jid();
            mLeftImageChat.setVisibility(View.VISIBLE);

            Log.d("Location", "Lat::" + msg.getLatitude() + " Lon::"
                    + msg.getLongitude());
            String lat_lon = msg.getLatitude() + ","
                    + msg.getLongitude();
            mLeftImageChat.setTag(lat_lon);

            mLeftImageChat.getLayoutParams().width = (int) width * 57 / 100;
            mLeftImageChat.getLayoutParams().height = (int) height * 30 / 100;
            mLeftImageChatDownload.setVisibility(View.GONE);
            mLeftImagetProgressBar.setVisibility(View.GONE);


            String values = msg.get_id() + "," + fromuser;

            try {
                byte[] image_data = msg.getRow_data();
                Bitmap bitmap = BitmapFactory.decodeByteArray(
                        image_data, 0, image_data.length,Util.getBitmapOptions());
                mLeftImageChat.setImageBitmap(bitmap);
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
            }
            if (msg.getStatus() == 1 && msg.getNeeds_push() == 1) {

                // new
                // uploa_Location().execute(c.getString(0),fromuser);
            } else if (msg.getStatus() == 1 && msg.getNeeds_push() == 0) {

            } else {

            }

            mLeftImageChat.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getTag() != null) {
                        String latitude = v.getTag().toString()
                                .split(",")[0];
                        String longitude = v.getTag().toString()
                                .split(",")[1];
                        String label = "I'm Here!";
                        String uriBegin = "geo:" + latitude + ","
                                + longitude;
                        String query = latitude + "," + longitude
                                + "(" + label + ")";
                        String encodedQuery = Uri.encode(query);
                        String uriString = uriBegin + "?q="
                                + encodedQuery + "&z=16";
                        Uri uri = Uri.parse(uriString);
                        String url = "http://maps.google.com/maps?center='" + v.getTag().toString() + "'&zoom=15&views=transit";
                        try {
                            Intent mapIntent = new Intent(
                                    Intent.ACTION_VIEW,
                                    uri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            mParentActivity.startActivity(mapIntent);
                        } catch (ActivityNotFoundException exp) {
                            mParentActivity.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                        }


                    }

                }
            });
        } catch (Exception e) {

        }

    }




    public void setRightAudio_Old()
    {


        MessageGetSet msg = msg_list.get(k);
        String fromuser = msg.getKey_remote_jid();
        mRightAudioBtnCancel.setVisibility(View.VISIBLE);
        mRightAudioBtnUpload.setVisibility(View.VISIBLE);
        mRightAudioBtnPlay.setVisibility(View.VISIBLE);
        mRightAudioBtnPlay.setBackgroundResource(R.drawable.ic_action_audio_play);


        if (msg_list.get(k).getStatus() == 3) {
            mRightAudioTickMark.setImageResource(R.drawable.message_unsent);
        } else if (msg_list.get(k).getStatus() == 2) {
            mRightAudioTickMark
                    .setImageResource(R.drawable.receipt_from_server);
        } else if (msg_list.get(k).getStatus() == 1 || msg_list.get(k).getStatus() == 0) {
            mRightAudioTickMark
                    .setImageResource(R.drawable.receipt_from_target);
        } else if (msg_list.get(k).getStatus() == -1) {
            mRightAudioTickMark
                    .setImageResource(R.drawable.receipt_read);
        }


        mRightAudioSize.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                notificatiob_font_size);
        mRightAudioDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                notificatiob_font_size);


        // Set timestamp
        Date date = new Date();
        date.setTime(msg_list.get(k).getSend_timestamp());
        SimpleDateFormat time_format = new SimpleDateFormat(
                "hh:mm a");

        mRightAudioTextTime.setText(time_format.format(date));

        mRightAudioDuration.setText(new TimeUtils()
                .millisToShortDHMS(msg.getMedia_duration()));

        mRightAudioBtnPlay.setFocusable(false);
        mRightAudioBtnUpload.setFocusable(false);
        mRightAudioBtnCancel.setFocusable(false);
        mRightAudioSize.setFocusable(false);
        mRightAudioUploadProgress.setFocusable(false);
        mRightAudioSeekBar.setFocusable(false);
        mRightAudioSize.setText(new CompressImage()
                .humanReadableByteCount(msg.getMedia_size(), true));

        mRightAudioSeekBar.setProgress(0);
        mRightAudioSeekBar.setMax(100);
        final MediaPlayer player = new MediaPlayer();
        mRightAudioBtnPlay.setTag(Constant.local_audio_dir
                + msg.getMedia_name());
        mRightAudioSeekBar.setTag(msg.get_id());
        String values = msg.get_id() + "," + fromuser;
        mRightAudioBtnUpload.setTag(values);
        list.add(player);
        PlayBtnlist.add(mRightAudioBtnPlay);

        final Button btn_play = mRightAudioBtnPlay;
        final Button btn_cancel_upload = mRightAudioBtnCancel;
        final ProgressBar progress_audio =mRightAudioUploadProgress;
        final Button btn_upload =mRightAudioBtnUpload;
        final SeekBar seek_audio = mRightAudioSeekBar;

        mRightAudioBtnCancel
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        mAsyncUpload_Audio.get(String.valueOf(msg_list.get(k).get_id())).cancelAllRequests(true);
                        btn_upload.setVisibility(View.VISIBLE);
                        progress_audio.setVisibility(View.GONE);
                        btn_play.setVisibility(View.GONE);
                        // txt_audio_size.setVisibility(View.VISIBLE);
                        btn_cancel_upload
                                .setVisibility(View.GONE);
                        seek_audio.setVisibility(View.GONE);

                    }
                });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                // Log.d("Play", "Player Finished.....");

                seekHandler.removeCallbacks(run);
                seek_audio.setProgress(0);

                if (s < Integer.valueOf(listTemp.get(
                        listTemp.size() - 1).toString())
                        && mFirstRun == true) {

                    s++;
                    if (s == Integer.valueOf(listTemp.get(
                            listTemp.size() - 1).toString()) - 1) {
                        s = Integer
                                .valueOf(listTemp.get(
                                        listTemp.size() - 1)
                                        .toString()) + 1;
                        mFirstRun = false;

                    }
                    Constant.printMsg("oooooooooooooolLL"
                            + s
                            + "     "
                            + listTemp.get(listTemp.size() - 1)
                            .toString());

                } else {
                    btn_play.setBackgroundResource(R.drawable.ic_action_audio_play);

                }

            }
        });

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                // Log.d("Player", "Player prepared....");
                mp.start();
                seek_audio.setProgress(0);
                seek_audio.setMax(mp.getDuration());
            }
        });

        //   final TextView mRightAudioDuration = mRightAudioDuration;

        class run implements Runnable {

            MediaPlayer mp;
            SeekBar seek;

            public run(MediaPlayer m, SeekBar se) {
                mp = m;
                seek = se;
            }

            @Override
            public void run() {

                if (player.isPlaying()) {
                    seek.setProgress(mp.getCurrentPosition());
                    mRightAudioDuration.setText(new TimeUtils()
                            .millisToShortDHMS(mp
                                    .getCurrentPosition()));
                }
                seekHandler.postDelayed(this, 1);
            }

        }

        MessageGetSet msg1 = dbAdapter.getMessages_by_msg_id(String.valueOf(msg.get_id()));

        boolean isConnected = Connectivity.isOnline(mParentActivity);

        if(!mAsyncUpload_Audio.containsKey(String.valueOf(msg_list.get(k).get_id())))
            mAsyncUpload_Audio.put(String.valueOf(msg_list.get(k).get_id()), new AsyncHttpClient());

        //-----------------------------

        if (msg_list.get(k).getStatus() == 3 &&
                msg_list.get(k).getNeeds_push() ==1) {

            Constant.printMsg("......Chat Video 3..1...." + isConnected
            );

            if (!isFirstTime) {
                if(isConnected) {

                    btn_upload.setVisibility(View.GONE);
                    progress_audio.setVisibility(View.VISIBLE);
                    progress_audio.setIndeterminate(true);
                    btn_play.setVisibility(View.GONE);
                    btn_cancel_upload.setVisibility(View.VISIBLE);
                    seek_audio.setVisibility(View.GONE);

                   /* long l = dbAdapter.setUpdateMessage_need_push(
                            msg.getKey_id(), 0);*/
                   new uploa_audio().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(msg.get_id()), fromuser);
                } else
                {
                    btn_upload.setVisibility(View.VISIBLE);
                    btn_play.setVisibility(View.GONE);
                    btn_cancel_upload.setVisibility(View.GONE);
                    progress_audio.setVisibility(View.GONE);
                    seek_audio.setVisibility(View.GONE);

                }
            }


        } else if (msg_list.get(k).getStatus() == 3
                && msg_list.get(k).getNeeds_push() == 2 ) {


            if(!isFirstTime)
            {
                if(isConnected) {

                    btn_upload.setVisibility(View.GONE);
                    progress_audio.setVisibility(View.VISIBLE);
                    progress_audio.setIndeterminate(true);
                    btn_play.setVisibility(View.GONE);
                    btn_cancel_upload.setVisibility(View.VISIBLE);
                    seek_audio.setVisibility(View.GONE);

                    dbAdapter.setUpdateMessage_need_push(
                            msg.getKey_id(), 1);
                   new uploa_audio().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,String.valueOf(msg.get_id()), fromuser);
                } else
                {
                    btn_upload.setVisibility(View.VISIBLE);
                    btn_play.setVisibility(View.GONE);
                    btn_cancel_upload.setVisibility(View.GONE);
                    progress_audio.setVisibility(View.GONE);
                    seek_audio.setVisibility(View.GONE);

                }
            }
            else {

                btn_upload.setVisibility(View.GONE);
                btn_play.setVisibility(View.GONE);
                btn_cancel_upload.setVisibility(View.VISIBLE);
                progress_audio.setVisibility(View.VISIBLE);
                progress_audio.setFocusable(true);
                seek_audio.setVisibility(View.GONE);
            }


        } else if (msg_list.get(k).getStatus() == 2
                && msg_list.get(k).getMedia_url() != null) {

            Constant.printMsg("......Chat Video 2..not null url...."
            );

            btn_upload.setVisibility(View.GONE);
            btn_play.setVisibility(View.VISIBLE);
            btn_cancel_upload.setVisibility(View.GONE);
            progress_audio.setVisibility(View.GONE);
            seek_audio.setVisibility(View.VISIBLE);

        } else {

            Constant.printMsg("......Chat Video 0 1 -1......"
            );

            btn_upload.setVisibility(View.GONE);
            progress_audio.setVisibility(View.GONE);
            btn_play.setVisibility(View.VISIBLE);
            btn_cancel_upload.setVisibility(View.GONE);
            seek_audio.setVisibility(View.VISIBLE);
        }

        //-------------------------------



        btn_upload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Connectivity.isOnline(mParentActivity)) {
                    String[] val = v.getTag().toString().split(",");

								/* new uploa_audio().execute(val[0],val[1]); */

                    new uploa_audio().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, val[0], val[1]);

                    btn_upload.setVisibility(View.GONE);
                    progress_audio.setVisibility(View.VISIBLE);
                    btn_play.setVisibility(View.GONE);
                    // txt_audio_size.setVisibility(View.GONE);
                    btn_cancel_upload.setVisibility(View.VISIBLE);
                    seek_audio.setVisibility(View.GONE);
                } else
                {
                    Toast.makeText(mParentActivity,"No network available", Toast.LENGTH_SHORT).show();
                }

            }

        });

        btn_play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mPressed == 1) {
                    TempBtn = new ArrayList();
                }

                mPressed = 0;
                listTemp.add(list.size());

                if (mAudioTagValue.equalsIgnoreCase((String) v
                        .getTag())) {

                    Constant.printMsg("Oooop" + mAudioTagValue
                            + "   " + (String) v.getTag());

                    mAudioTagValue = (String) v.getTag();

                } else {
                    mAudioTagValue = (String) v.getTag();
                    for (int i = 0; i < list.size(); i++) {

                        Constant.printMsg("KKKKKKP"
                                + v.getTag() + "    "
                                + list.get(i));

                        MediaPlayer plays = list.get(i);

                        // if (v.getTag().equals(plays.gette))

                        plays.stop();

                    }

                }
                if (player.isPlaying()) {
                    Constant.printMsg("ooop");
                }
                if (current_song == String.valueOf(btn_play
                        .getTag())) {

                    if (player.isPlaying()) {
                        seekHandler.removeCallbacks(run);
                        player.pause();
                        // run.suspend();
                        System.out
                                .println("called click button playing"
                                        + btn_play.getTag());

                        // length=player.getCurrentPosition();
                        btn_upload.setVisibility(View.GONE);
                        progress_audio.setVisibility(View.GONE);
                        btn_play.setVisibility(View.VISIBLE);
                        // txt_audio_size.setVisibility(View.GONE);
                        btn_cancel_upload
                                .setVisibility(View.GONE);
                        seek_audio.setVisibility(View.VISIBLE);
                        // btn_play.setBackgroundResource(R.drawable.ic_action_audio_play);
                    } else {
                        player.start();
                        current_song = String.valueOf(btn_play
                                .getTag());
                        seekHandler.postDelayed(new run(player,
                                seek_audio), 100);

                        Constant.printMsg("kkkkkkl");

                        // btn_play.setBackgroundResource(R.drawable.ic_action_audio_pause);

                    }
                } else {
                    if (player.isPlaying()) {
                        Constant.printMsg("kkkkkkl11");
                        player.stop();
                        // btn_play.setBackgroundResource(R.drawable.ic_action_audio_play);

                    }

                    // Log.d("Audio", "Path::" +
                    // v.getTag().toString());
                    System.out
                            .println("called click button pause"
                                    + btn_play.getTag());

                    seekHandler.removeCallbacks(run);

                    try {
                        player.reset();
                        player.setDataSource(v.getTag()
                                .toString());
                        player.prepare();
                                        /* player.prepareAsync(); */
                    } catch (Exception e) {
                        // ACRA.getErrorReporter().handleException(e);
                        // TODO: handle exception
                    }
                    seekHandler.postDelayed(new run(player,
                            seek_audio), 100);
                    current_song = String.valueOf(btn_play
                            .getTag());
                    // btn_play.setBackgroundResource(R.drawable.ic_action_audio_pause);

                }
                for (int i = 0; i < PlayBtnlist.size(); i++) {

                    Button btn = PlayBtnlist.get(i);

                    if (btn.getTag().equals(v.getTag())
                            && mBtnClick == 0) {

                        Constant.printMsg("CCCCCCCCCCCCCCCAA");

                        btn.setBackgroundResource(R.drawable.ic_action_audio_pause);
                        mBtnClick++;

                    } else {

                        Constant.printMsg("CCCCCCCCCCCCCCCAB");

                        mBtnClick = 0;
                        btn.setBackgroundResource(R.drawable.ic_action_audio_play);
                    }

                }

                if (TempBtn.size() > 0) {

                    if (v.getTag().equals(
                            TempBtn.get(TempBtn.size() - 1)
                                    .toString())) {
                        Constant.printMsg("aaaaaaaaaaa1");

                        v.setBackgroundResource(R.drawable.ic_action_audio_play);
                        player.pause();
                        TempBtn = new ArrayList();

                    } else {
                        Constant.printMsg("aaaaaaaaaaa2");
                        v.setBackgroundResource(R.drawable.ic_action_audio_pause);
                        TempBtn = new ArrayList();
                        TempBtn.add(v.getTag());

                    }

                } else {
                    Constant.printMsg("aaaaaaaaaaa3");
                    v.setBackgroundResource(R.drawable.ic_action_audio_pause);
                    TempBtn.add(v.getTag());

                }


            }
        });

    }



    public void setLeftAudio_Old()
    {
        try {

            // Set timestamp
            Date date = new Date();
            date.setTime(msg_list.get(k).getTimestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mLeftAudioTextTime.setText(time_format.format(date));

            MessageGetSet msg = msg_list.get(k);
            String fromuser = msg.getKey_remote_jid();
            mLeftAudioBtnCancel.setVisibility(View.VISIBLE);
            mLeftAudioBtnDownload.setVisibility(View.VISIBLE);
            mLeftAudioBtnPlay.setVisibility(View.VISIBLE);
            mLeftAudioBtnPlay.setBackgroundResource(R.drawable.ic_action_audio_play);

            mLeftAudioSize.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                    notificatiob_font_size);
            mLeftAudioDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                    notificatiob_font_size);

            mLeftAudioDuration.setText(new TimeUtils()
                    .millisToShortDHMS(msg.getMedia_duration()));


            String displayName = null;
            // Left sender name...
            ContactsGetSet contact = new ContactsGetSet();
            try {
                contact = dbAdapter.getContact(msg_list.get(k).getRemote_resource());
                Constant.printMsg("Left username : " + contact);
                if (contact == null) {
                    mLeftTextProfile.setText(msg_list.get(k).getRemote_resource().split("@")[0]);
                    displayName = msg_list.get(k).getRemote_resource().split("@")[0];
                } else {
                    mLeftTextProfile.setText(contact.getDisplay_name());
                    displayName = contact.getDisplay_name();
                }

                getColorLeftUser(displayName);

            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
            }

            mLeftAudioBtnPlay.setFocusable(false);
            mLeftAudioBtnDownload.setFocusable(false);
            mLeftAudioBtnCancel.setFocusable(false);
            mLeftAudioSize.setFocusable(false);
            mLeftAudioDownloadProgress.setFocusable(false);
            mLeftAudioSeekBar.setFocusable(false);

            mLeftAudioSeekBar.setProgress(0);
            mLeftAudioSeekBar.setMax(100);
            mLeftAudioSize.setText(new CompressImage()
                    .humanReadableByteCount(msg.getMedia_size(), true));
            final MediaPlayer player = new MediaPlayer();
            mLeftAudioBtnPlay.setTag(Constant.local_audio_dir
                    + msg.getMedia_name());
            mLeftAudioSeekBar.setTag(msg.get_id());
            String values = msg.getKey_id() + "," + msg.getMedia_url()
                    + "," + fromuser;

            mLeftAudioBtnDownload.setTag(values);
            list.add(player);
            PlayBtnlist.add(mLeftAudioBtnPlay);

            mLeftAudioBtnCancel
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub

                            new download_Audio().cancel(true);
                            mLeftAudioBtnDownload
                                    .setVisibility(View.VISIBLE);
                            mLeftAudioDownloadProgress.setVisibility(View.GONE);
                            mLeftAudioBtnPlay.setVisibility(View.GONE);
                            // txt_audio_size.setVisibility(View.VISIBLE);
                            mLeftAudioBtnCancel
                                    .setVisibility(View.GONE);
                            mLeftAudioSeekBar.setVisibility(View.GONE);

                        }
                    });
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    // Log.d("Play", "Player Finished.....");
                    seekHandler.removeCallbacks(run);
                    mLeftAudioSeekBar.setProgress(0);

                    if (s < Integer.valueOf(listTemp.get(
                            listTemp.size() - 1).toString())
                            && mFirstRun == true) {

                        s++;
                        if (s == Integer.valueOf(listTemp.get(
                                listTemp.size() - 1).toString()) - 1) {
                            s = Integer
                                    .valueOf(listTemp.get(
                                            listTemp.size() - 1)
                                            .toString()) + 1;
                            mFirstRun = false;

                        }
                        Constant.printMsg("oooooooooooooolLL"
                                + s
                                + "     "
                                + listTemp.get(listTemp.size() - 1)
                                .toString());

                    } else {

                        Constant.printMsg("ooooooooooooool" + s);
                        mLeftAudioBtnPlay.setBackgroundResource(R.drawable.ic_action_audio_play);
                    }

                }
            });

            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    // Log.d("Player", "Player prepared....");
                    mp.start();
                    mLeftAudioSeekBar.setProgress(0);
                    mLeftAudioSeekBar.setMax(mp.getDuration());
                }
            });

            class run implements Runnable {

                MediaPlayer mp;
                SeekBar seek;

                public run(MediaPlayer m, SeekBar se) {
                    mp = m;
                    seek = se;
                }

                @Override
                public void run() {

                    if (player.isPlaying()) {
                        seek.setProgress(mp.getCurrentPosition());
                        mLeftAudioDuration.setText(new TimeUtils()
                                .millisToShortDHMS(mp
                                        .getCurrentPosition()));
                                        /*
                                         * Log.d("Current Duration",
                                         * "Current Postion::" +
                                         * mp.getCurrentPosition() +
                                         * " Seek Potition::" + seek.getProgress());
                                         */
                    }
                    seekHandler.postDelayed(this, 1);
                }

            }

            if (msg.getMedia_name() == null) {
                if (is_auto_dowload_audio) {
                    new download_Audio().execute(msg.getKey_id(),
                            msg.getMedia_url(), fromuser);
                    mLeftAudioBtnDownload.setVisibility(View.GONE);
                    mLeftAudioBtnCancel.setVisibility(View.VISIBLE);
                    mLeftAudioDownloadProgress.setVisibility(View.VISIBLE);
                    mLeftAudioBtnPlay.setVisibility(View.GONE);
                    // txt_audio_size.setVisibility(View.GONE);
                    mLeftAudioSeekBar.setVisibility(View.GONE);
                } else {
                    mLeftAudioBtnDownload.setVisibility(View.VISIBLE);
                    mLeftAudioBtnCancel.setVisibility(View.GONE);
                    mLeftAudioDownloadProgress.setVisibility(View.GONE);
                    mLeftAudioBtnPlay.setVisibility(View.GONE);
                    // txt_audio_size.setVisibility(View.VISIBLE);
                    mLeftAudioSeekBar.setVisibility(View.GONE);
                }
            } else {
                mLeftAudioBtnDownload.setVisibility(View.GONE);
                mLeftAudioBtnCancel.setVisibility(View.GONE);
                mLeftAudioDownloadProgress.setVisibility(View.GONE);
                mLeftAudioBtnPlay.setVisibility(View.VISIBLE);
                // txt_audio_size.setVisibility(View.GONE);
                mLeftAudioSeekBar.setVisibility(View.VISIBLE);
            }

            mLeftAudioBtnDownload.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {


                    mLeftAudioBtnDownload=(Button)findViewById(v.getId());
                    mLeftAudioBtnCancel=(Button)findViewById(v.getId()+800000);
                    mLeftAudioDownloadProgress=(ProgressBar)findViewById(v.getId()+900000);
                    mLeftAudioBtnPlay=(Button)findViewById(v.getId()+100000);
                    mLeftAudioSeekBar=(SeekBar)findViewById(v.getId()+200000);


                    mLeftAudioDownloadProgress.setIndeterminate(true);

                    String[] val = v.getTag().toString().split(",");

                                    /* new uploa_audio().execute(val[0],val[1]); */

                    new download_Audio().execute(val[0], val[1],
                            val[2]);

                    mLeftAudioBtnDownload.setVisibility(View.GONE);
                    mLeftAudioDownloadProgress.setVisibility(View.VISIBLE);
                    mLeftAudioBtnPlay.setVisibility(View.GONE);
                    // txt_audio_size.setVisibility(View.GONE);
                    mLeftAudioBtnCancel.setVisibility(View.VISIBLE);
                    mLeftAudioSeekBar.setVisibility(View.GONE);
                    ;
                }

            });

            mLeftAudioBtnPlay.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mLeftAudioBtnDownload=(Button)findViewById(v.getId()-100000);
                    mLeftAudioBtnCancel=(Button)findViewById(v.getId()+700000);
                    mLeftAudioDownloadProgress=(ProgressBar)findViewById(v.getId()+800000);
                    mLeftAudioBtnPlay=(Button)findViewById(v.getId());
                    mLeftAudioSeekBar=(SeekBar)findViewById(v.getId()+100000);


                    if (mPressed == 0) {
                        TempBtn = new ArrayList();
                    }
                    mPressed = 1;

                    listTemp.add(list.size());
                    if (mAudioTagValue.equalsIgnoreCase((String) v
                            .getTag())) {

                        Constant.printMsg("Oooop" + mAudioTagValue
                                + "   " + (String) v.getTag());

                        mAudioTagValue = (String) v.getTag();

                    } else {
                        mAudioTagValue = (String) v.getTag();
                        for (int i = 0; i < list.size(); i++) {

                            Constant.printMsg("KKKKKKP"
                                    + v.getTag() + "    "
                                    + list.get(i));

                            MediaPlayer plays = list.get(i);

                            // if (v.getTag().equals(plays.gette))

                            plays.stop();

                        }
                    }
                    if (player.isPlaying()) {
                        seekHandler.removeCallbacks(run);
                        player.pause();
                        // run.suspend();

                        // length=player.getCurrentPosition();
                        mLeftAudioBtnDownload.setVisibility(View.GONE);
                        mLeftAudioDownloadProgress.setVisibility(View.GONE);
                        mLeftAudioBtnPlay.setVisibility(View.VISIBLE);
                        // txt_audio_size.setVisibility(View.GONE);
                        mLeftAudioBtnCancel
                                .setVisibility(View.GONE);
                        mLeftAudioSeekBar.setVisibility(View.VISIBLE);
                        // btn_play.setImageDrawable(context
                        // .getResources()
                        // .getDrawable(
                        // R.drawable.ic_action_audio_play));
                        mLeftAudioBtnPlay.setBackgroundResource(R.drawable.ic_action_audio_play);

                    } else {

                        // Log.d("Audio", "Path::" +
                        // v.getTag().toString());

                        seekHandler.removeCallbacks(run);

                        try {
                            player.reset();
                            player.setDataSource(v.getTag()
                                    .toString());
                            player.prepare();
                            current_song = String.valueOf(mLeftAudioBtnPlay
                                    .getTag());
                                             /* player.prepareAsync(); */
                        } catch (Exception e) {
                            // ACRA.getErrorReporter().handleException(e);
                            // TODO: handle exception
                        }
                        seekHandler.postDelayed(new run(player,
                                mLeftAudioSeekBar), 100);

                        mLeftAudioBtnPlay.setBackgroundResource(R.drawable.ic_action_audio_pause);

                    }
                    for (int i = 0; i < PlayBtnlist.size(); i++) {

                        Button btn = PlayBtnlist.get(i);

                        if (btn.getTag().equals(v.getTag())
                                && mBtnClick == 0) {

                            Constant.printMsg("CCCCCCCCCCCCCCCAA");

                            btn.setBackgroundResource(R.drawable.ic_action_audio_pause);
                            mBtnClick++;

                        } else {

                            Constant.printMsg("CCCCCCCCCCCCCCCAB");

                            mBtnClick = 0;
                            btn.setBackgroundResource(R.drawable.ic_action_audio_play);
                        }

                    }
                    if (TempBtn.size() > 0) {

                        if (v.getTag().equals(
                                TempBtn.get(TempBtn.size() - 1)
                                        .toString())) {

                            mLeftAudioBtnPlay.setBackgroundResource(R.drawable.ic_action_audio_play);
                            TempBtn = new ArrayList();

                        } else {

                            mLeftAudioBtnPlay.setBackgroundResource(R.drawable.ic_action_audio_pause);
                            TempBtn = new ArrayList();
                            TempBtn.add(v.getTag());

                        }

                    } else {

                        Constant.printMsg("ooloolool");
                        mLeftAudioBtnPlay.setBackgroundResource(R.drawable.ic_action_audio_pause);
                        TempBtn.add(v.getTag());

                    }

                }

            });
        } catch (Exception e) {

        }
        // Log.d("Audio view", "Audio view ended....");
    }


    private void callstatusDB(String query1) {
        // TODO Auto-generated method stub
        Cursor c = null;

        try {
            c = db.open().getDatabaseObj().rawQuery(query1, null);
            Constant.printMsg("The selected elist activity count is ::::::"
                    + c.getCount());
            if (c.getCount() > 0) {
                Constant.printMsg("Caling sysout:::::::::::::::::::::::::");
                while (c.moveToNext()) {
                    // mSeenList.add(c.getString(0));
                    mstatus = String.valueOf(c.getString(0));
                    Constant.printMsg("Constant.mDefaultScroll = true;::::"
                            + mstatus);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void callZzleDB(String query) {
        Cursor c = null;

        try {
            c = db.open().getDatabaseObj().rawQuery(query, null);
            Constant.printMsg("The selected elist activity count is ::::::"
                    + c.getCount());
            if (c.getCount() > 0) {
                Constant.printMsg("Caling sysout:::::::::::::::::::::::::");
                while (c.moveToNext()) {
                    mSeenList.add(c.getString(0));
                }
            }
        } catch (Exception e) {
        }
    }

    boolean isDisplayed_notification = false;

    public void notificationMessages()
    {


        try {

            boolean isSomeOne = false;

            MessageGetSet msg = msg_list.get(k);

            if (msg.getData().equalsIgnoreCase(
                    KachingMeApplication.getUserID() + KachingMeApplication.getHost())) {
                isSomeOne = true;
            } else {
                isSomeOne = false;
            }




            Constant.printMsg("Notification loop 000: " + isDisplayed_notification +"  "+Integer.parseInt(msg_list.get(k).getMedia_wa_type())+"  "+msg.getRemote_resource()+" "+ KachingMeApplication.getUserID() + KachingMeApplication.getHost()+"  "+is_you);

            if(!isDisplayed_notification || Integer.parseInt(msg_list.get(k).getMedia_wa_type())!=7 || !isSomeOne) {
                mRightTipLayout = new FrameLayout(mParentActivity);
                mRightTipLayout.setId(k + 200000);
                mRightTipLayout.setFocusable(true);
                mRightTipLayout.setFocusableInTouchMode(true);

                LinearLayout.LayoutParams mTextNotifyParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mTextNotifyParams.gravity = Gravity.CENTER | Gravity.CENTER;
                mRightTipLayout.setLayoutParams(mTextNotifyParams);


                mNotificationText = new TextView(mParentActivity);
                mNotificationText.setGravity(Gravity.CENTER);
                mNotificationText.setLayoutParams(mTextNotifyParams);

                mRightTipLayout.addView(mNotificationText);
                mDynamicView.addView(mRightTipLayout);


                mNotificationText.setVisibility(View.VISIBLE);




                if (msg.getRemote_resource().equals(
                        KachingMeApplication.getUserID() + KachingMeApplication.getHost())) {
                    is_you = true;
                } else {
                    is_you = false;
                }


                mNotificationText.setVisibility(View.VISIBLE);
                String name = dbAdapter.getDisplay_name(msg.getRemote_resource());

                if (name == null) {
                    name = msg.getRemote_resource().split("@")[0];
                }


                if (Integer.parseInt(msg.getMedia_wa_type()) == 7) {

                    if (is_you) {


                        mNotificationText.setText(String.format(getResources()
                                .getString(R.string.you_added), Utils
                                .getDisplayName(dbAdapter, msg.getData())));
                    } else if (msg.getData().equals(KachingMeApplication.getjid())) {

                        Constant.printMsg("Notification websiver : " + msg.getKey_id() + " " + msg.get_id());


                        isDisplayed_notification = true;
                        mNotificationText.setText(String.format(getResources()
                                .getString(R.string.added_you), Utils
                                .getDisplayName(dbAdapter, msg.getRemote_resource())));

                    } else {
                        mNotificationText.setText(String.format(getResources()
                                .getString(R.string.added), Utils.getDisplayName(
                                dbAdapter, msg.getRemote_resource()), Utils.getDisplayName(
                                dbAdapter, msg.getData())));
                    }

                } else if (Integer.parseInt(msg.getMedia_wa_type()) == 8) {

                    // txt_notice.setText(name +
                    // " "+context.getResources().getString(R.string.left_group));
                    if (is_you && msg.getData().equals(KachingMeApplication.getjid())) {
                        mNotificationText.setText(getResources().getString(
                                R.string.you_left));
                    } else if (msg.getData().equals(msg.getRemote_resource())) {
                        mNotificationText.setText(String.format(getResources()
                                .getString(R.string.abc_left), Utils
                                .getDisplayName(dbAdapter, msg.getRemote_resource())));
                    } else if (is_you) {
                        mNotificationText.setText(String.format(getResources()
                                .getString(R.string.you_removed), Utils
                                .getDisplayName(dbAdapter, msg.getData())));
                    } else if (msg.getData().equals(KachingMeApplication.getjid())) {
                        mNotificationText.setText(String.format(getResources()
                                .getString(R.string.removed_you), Utils
                                .getDisplayName(dbAdapter, msg.getRemote_resource())));
                    } else {
                        mNotificationText.setText(String.format(getResources()
                                .getString(R.string.removed), Utils.getDisplayName(
                                dbAdapter, msg.getRemote_resource()), Utils.getDisplayName(
                                dbAdapter, msg.getData())));
                    }

                } else if (Integer.parseInt(msg.getMedia_wa_type()) == 10) {
                    if (is_you) {
                        mNotificationText.setText(String.format(getString(R.string.you_changed_grop_subject_to),
                                Utils.getDisplayName(dbAdapter, msg.getData())));

                    } else {
                        mNotificationText.setText(String.format(
                                getString(R.string.changed_grop_subject),
                                Utils.getDisplayName(dbAdapter, msg.getRemote_resource()),
                                Utils.getDisplayName(dbAdapter, msg.getData())));
                    }

                    if(msg.getData()!=null)
                    {
                        String title = null;

                        if (subject.length() > 15) {
                            title = msg.getData().substring(0, 15) + "...";
                        } else {
                            title = msg.getData();
                        }
                        txt_title.setText(title);
                    }


                } else if (Integer.parseInt(msg.getMedia_wa_type()) == 11) {
                    if (is_you) {
                        mNotificationText.setText(getString(R.string.you_changed_grop_icon));
                    } else {
                        mNotificationText.setText(String.format(
                                getString(R.string.changed_grop_icon),
                                Utils.getDisplayName(dbAdapter, msg.getRemote_resource())));
                    }

                } else if (Integer.parseInt(msg.getMedia_wa_type()) == 9) {
                    if (is_you) {
                        mNotificationText.setText(String.format(getResources()
                                .getString(R.string.you_created_grou), msg.getData()));
                    } else {
                        mNotificationText.setText(String.format(getResources()
                                .getString(R.string.created_group), name, msg.getData()));
                    }


                }
            }
        } catch (NumberFormatException e) {

        } catch (Resources.NotFoundException e) {
        }
    }



    public void getColorLeftUser(String userName)
    {

        try {
            int rand =0 ;

            if(colorMap.containsKey(userName)){
                rand = colorMap.get(userName);
            }else{
                rand = getRandomColor();
                colorMap.put(userName, rand); // Put the two parts into the map
            }

            mLeftTextProfile.setTextColor(Color.parseColor("#CC494040"));
        } catch (Exception e) {

        }
    }


    public int getRandomColor(){


        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void mAttachKachingFeatures()
    {

        try{


        if (Constant.mBazzleGroup == true) {

            Constant.mBazzleGroup = false;

            menuclick = false;

            menuclick = true;
            int point = sharedPrefs.getInt("zzlepoint", 0);

            Constant.totalzzle = point;

            Constant.totalzzle = Count + Constant.totalzzle;

            Editor e1 = sharedPrefs.edit();
            e1.putInt("zzlepoint", Constant.totalzzle);
            e1.commit();

            menuclick = true;
            Constant.bux = sharedPrefs.getLong("buxvalue", 0);

            Long buxval = Constant.bux + Constant.zzlepoints;
            Constant.bux = buxval;

            Editor e = sharedPrefs.edit();
            e.putLong("buxvalue", buxval);
            e.commit();
            sendMessage("<z>" + Constant.mZzleText + "-" + Constant.mTimeZzle
                    + "-" + Constant.mPreviewSpeed + "-"
                    + Constant.mPreviewTextsize + "-" + Constant.shapeselected);

        }

        if (Constant.songlist) {
            Constant.printMsg("songListValue in chat::if"
                    + Constant.songlist);
            for (int i = 0; i < Constant.song_list.size(); i++) {
                Constant.songPath = Constant.song_list.get(i).toString();
                songValue = Constant.songPath;
                outputFile = Constant.local_audio_dir
                        + System.currentTimeMillis() + ".amr";

                Constant.printMsg("audio file path::" + songValue);

                File f1 = new File(songValue);
                File f2 = new File(outputFile);

                try {

                    copyDirectoryOneLocationToAnotherLocation(f1, f2);

                } catch (IOException e) {

                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            Constant.songlist = false;

        } else {
            Constant.printMsg("songListValue in chat::else"
                    + Constant.songlist);
        }

        if (Constant.attachNym == true) {

            Constant.printMsg("GGGGGGGG1111  " + Constant.attchnymstring);

            Constant.attachNym = false;
            Constant.bux = sharedPrefs.getLong("buxvalue", 0);

            Long buxval1 = Constant.bux + Constant.nynMpoint;
            Constant.bux = buxval1;

            Editor edit = sharedPrefs.edit();
            edit.putLong("buxvalue", buxval1);
            edit.commit();

            int point1 = sharedPrefs.getInt("nympoint", 0);

            Constant.totalchat = point1;

            Constant.totalchat = Count + Constant.totalchat;

            Editor e11 = sharedPrefs.edit();
            e11.putInt("nympoint", Constant.totalchat);
            e11.commit();
            sendMessage("<-" + Constant.attchnymstring);
        }


        if (Constant.mKonsGroup == true) {
            for (int i = 0; i < Constant.konsSelectedList.size(); i++) {

                Constant.mKonsText = Constant.konsSelectedList.get(i)
                        .toString();
                Constant.mKonsBackground = Constant.konsBackgroundList.get(i).toString();
                Constant.mKonsColor = Constant.konsColorList.get(i).toString();
                int point = sharedPrefs.getInt("konpoint", 0);

                Constant.totalkon = point;

                Constant.totalkon = Count + Constant.totalkon;

                Editor e2 = sharedPrefs.edit();
                e2.putInt("konpoint", Constant.totalkon);
                e2.commit();

                Constant.bux = sharedPrefs.getLong("buxvalue", 0);

                Long buxval1 = Constant.bux + Constant.konspoint;
                Constant.bux = buxval1;

                Editor e3 = sharedPrefs.edit();
                e3.putLong("buxvalue", buxval1);
                e3.commit();

                Constant.mKonsGroup = false;
                Constant.printMsg("tedddddddddd  " + Constant.mKonsBackground + Constant.mKonsColor + Constant.mKonsText);
                sendMessage("<k>" + Constant.mKonsBackground + "-" + Constant.mKonsColor + "-" + Constant.mKonsText);
            }

        }
        if (Constant.karaoke == true) {
            upload_audio_File(Constant.file);
            Constant.karaoke = false;
        }

        // if (Constant.karaokegroup == true) {
        // upload_audio_File(Constant.filegroup);
        // Constant.karaokegroup = false;
        // }

        if (Constant.logogroup == true) {
            Constant.printMsg("logog::" + Constant.logobitgroup);
//            uploadLogo(Constant.logobitgroup, true);
            Constant.logogroup = false;
        }

        if (Constant.mZzleGroup == true) {
            Constant.mZzleGroup = false;
            // sendMessage("<x>" + Constant.mZzleTextGroup);
            Constant.printMsg("zzzzzzzz   " + Constant.mPreviewBackground + "-"
                    + Constant.mPreviewSpeed + "-"
                    + Constant.mPreviewTextColor + "-"
                    + Constant.mPreviewTextsize + "-"
                    + Constant.mZzleTextGroup);


            int point = sharedPrefs.getInt("zzlepoint", 0);

            Constant.totalzzle = point;

            Constant.totalzzle = Count + Constant.totalzzle;

            Editor e1 = sharedPrefs.edit();
            e1.putInt("zzlepoint", Constant.totalzzle);
            e1.commit();

            menuclick = true;
            Constant.bux = sharedPrefs.getLong("buxvalue", 0);

            Long buxval = Constant.bux + Constant.zzlepoints;
            Constant.bux = buxval;

            Editor e = sharedPrefs.edit();
            e.putLong("buxvalue", buxval);
            e.commit();

            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, +5);
            Constant.printMsg("dfreebie date ::::::" + c.getTime());
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Constant.printMsg("last date of freebie::::::::>>>>>>>"
                    + df1.format(c.getTime()));
            String last = df1.format(c.getTime());
            sendMessage("<x>" + Constant.mPreviewBackground + "-"
                    + Constant.mPreviewSpeed + "-" + Constant.mPreviewTextColor
                    + "-" + Constant.mPreviewTextsize + "-"
                    + Constant.mZzleText + "-" + last);
        }
        }catch (Exception e){

        }


    }

    public int valueText()
    {
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String textSizePref = pref.getString("pref_font_size", "16");
        int textSize = Integer.valueOf(textSizePref);
        Constant.printMsg(" Screen Width :  " + width + " Pref Text Size " + textSizePref + " Right " + textSize);
        return textSize;
    }


    public int getTimeTxtSize()
    {
        return (valueText()/2);
    }

    public boolean containsCaseInsensitive(String s, List<String> l){
        for (String string : l){
            if (string.equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }

    public boolean getCopyPastMediaType(String mediaType)
    {
        boolean isMedia = true;

        if(!mediaType.trim().equalsIgnoreCase("0"))
            isMedia = false;

        Constant.printMsg("Dilip copy " + mediaType +" " + mediaType.trim().equalsIgnoreCase("4")+ " " + isMedia);

        return isMedia;
    }

    public boolean chekNynmDazzKon(String text)
    {
        boolean isSpl = false;

        if(text!=null)
        {
            if(text.length()>0) {
                if (text.charAt(0) == '<')
                    isSpl = true;
            }
        }


        return isSpl;
    }

}
