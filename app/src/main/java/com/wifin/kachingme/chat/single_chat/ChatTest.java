package com.wifin.kachingme.chat.single_chat;

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
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
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
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.google.android.gms.drive.DriveFile;
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
import com.wifin.kachingme.bradcast_recivers.GlobalBroadcast;
import com.wifin.kachingme.chat.chat_common_classes.ContactView_Normal;
import com.wifin.kachingme.chat.chat_common_classes.LocationShare;
import com.wifin.kachingme.chat.chat_common_classes.SendContact;
import com.wifin.kachingme.chat.chat_common_classes.SongList;
import com.wifin.kachingme.chat.muc_chat.OrientationGroup;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.deals.DealsActivity;
import com.wifin.kachingme.emojicons.emojicon.EmojiconEditText;
import com.wifin.kachingme.emojicons.emojicon.EmojiconFragmentGroup;
import com.wifin.kachingme.emojicons.emojicon.EmojiconGridFragment;
import com.wifin.kachingme.emojicons.emojicon.EmojiconTextView;
import com.wifin.kachingme.emojicons.emojicon.EmojiconsFragment;
import com.wifin.kachingme.emojicons.emojicon.emoji.Emojicon;
import com.wifin.kachingme.fragments.FavouriteContacts;
import com.wifin.kachingme.fragments.UserChatList;
import com.wifin.kachingme.kaching_feature.dazz.BannerActivityChat;
import com.wifin.kachingme.kaching_feature.dazz.BannerActivityDazzAdapter;
import com.wifin.kachingme.kaching_feature.dazz.BannerActivityLED;
import com.wifin.kachingme.kaching_feature.dazz.BannerActivityZzleAdapter;
import com.wifin.kachingme.kaching_feature.dazz.DazzPlainActivity;
import com.wifin.kachingme.kaching_feature.karaoke.KaraokeListActivity;
import com.wifin.kachingme.kaching_feature.kons.KonsHomeScreen;
import com.wifin.kachingme.kaching_feature.nynms.NynmActivity;
import com.wifin.kachingme.listeners.ChatCreatedListener;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.DonationDto;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.pojo.NymsPojo;
import com.wifin.kachingme.security.jncryptor.AES256JNCryptor;
import com.wifin.kachingme.security.jncryptor.JNCryptor;
import com.wifin.kachingme.services.DestructService;
import com.wifin.kachingme.services.GetJidPresenceIntentService;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.settings.Network_Usage;
import com.wifin.kachingme.settings.SharedPrefPrivacy;
import com.wifin.kachingme.settings.UserProfile;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.AlertUtils;
import com.wifin.kachingme.util.AvatarManager;
import com.wifin.kachingme.util.ChatDictionary;
import com.wifin.kachingme.util.CompressImage;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Emoji;
import com.wifin.kachingme.util.FileDialog;
import com.wifin.kachingme.util.GIFView;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.NetworkSharedPreference;
import com.wifin.kachingme.util.NoUnderlineSpan;
import com.wifin.kachingme.util.NotificationHandler;
import com.wifin.kachingme.util.ProfileRoundImg;
import com.wifin.kachingme.util.RounderImageView;
import com.wifin.kachingme.util.SelectionMode;
import com.wifin.kachingme.util.TimeUtils;
import com.wifin.kachingme.util.Utils;
import com.wifin.kachingme.util.cropimage.Util;

import org.acra.ACRAConstants;
import org.apache.http.Header;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ExceptionCallback;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketCollector.Configuration;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.iqrequest.IQRequestHandler;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.PlainStreamElement;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.util.stringencoder.Base64;
import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.privacy.PrivacyList;
import org.jivesoftware.smackx.privacy.PrivacyListManager;
import org.jivesoftware.smackx.privacy.packet.PrivacyItem;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import a_vcard.android.provider.Contacts;

//import com.kaching.me.login.NymsListActivity;

//import android.provider.Contacts.Intents;

public class ChatTest extends AppCompatActivity implements XMPPConnection,
        EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconFragmentGroup.OnEmojiconBackspaceClickedListener, AnimationListener, OnItemClickListenerInterface {

    private static final int RESULT_CODE_IMAGE = 11, RESULT_CODE_VIDEO = 22,
            RESULT_CODE_AUDIO = 33, RESULT_CODE_LOCATION = 44,
            RESULT_CODE_CONTACT = 55, RESULT_CODE_FILE = 77,
            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 99,
            CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 100,
            REQUEST_CODE_OPENER = 88, AUDIO_ACTIVITY_REQUEST_CODE = 1,
            RESULT_CODE_IMAGE_MULTIPLE = 101, REQUEST_CODE_ADD_CONTACT = 12;
    public static List<Integer> dest_msg_list = new ArrayList<>();
    public static List<DestructGetter> database_destList = new ArrayList<DestructGetter>();
    public static AbstractXMPPConnection connection = TempConnectionService.connection;
    public static String jid = "";
    public static ChatCreatedListener mChatCreatedListener;
    public static boolean isNotifyDataChanged = false;
    public static TextView txt_sub_title;
    public static boolean isThreadRun = false;
    public List<String> mPositionKey = new ArrayList<String>();
    public static RecyclerView listview;
    public static MessageEventManager messageEventManager = TempConnectionService.messageEventManager;
    public static Boolean IS_Front = false;
    public static HashMap<String,AsyncHttpClient> mAsyncUpload_Image = new HashMap<String, AsyncHttpClient>();
    public static HashMap<String,AsyncHttpClient> mAsyncUpload_Audio = new HashMap<String, AsyncHttpClient>();
    public static HashMap<String,AsyncHttpClient> mAsyncUpload_Video = new HashMap<String, AsyncHttpClient>();
    public static String home_title = "";
    public static boolean menuclick = false;
    public static boolean isJidOnline = false;
    public static boolean isDestructCall = false;
    String numberContactSend = null;
    // Added on 15th July 2016
    public static Handler mMyhandler = null;
    public static boolean isStatusTyping = false, acceptedBuxs = false;
    public static Handler handler = new Handler(Looper.getMainLooper());
    public static ArrayList dest_list = new ArrayList<Integer>();
    public static ArrayList dest_list_msgids = new ArrayList<Integer>();
    public static ArrayList dest_list_bombids = new ArrayList<Integer>();
    public static ArrayList dest_list_anim = new ArrayList<Integer>();
    public static ChatTest mParentActivity;
    public static boolean isSavedContact = false;
    public static boolean isFirstCall_Broadcast = false;
    public static ArrayList<MessageGetSet> msg_list = new ArrayList<MessageGetSet>();
    public static int k = 0;
    public static ArrayList<String> mself_destruct_list = new ArrayList<String>();
    public static ImageView mRightTickMark;
    public static TextView mRightDestTime;
    static Boolean IS_TYPED = true;

    static boolean isPlayAudio = false;
    static boolean isLeftPlayAudio = false;
    private static boolean is_available = false;
    public boolean bClick;
    public static boolean isFetchChatReady = false;
    public int ser_count = 0;
    public int sec = 1;
    public int counter;
    public long editNo = 0;
    public TextView mChatHedderUserStatusTxt;
    public PopupWindow pwindo;
    public String MEDIA_UPLOAD_URL;
    NetworkSharedPreference mNetworkSharPref;
    boolean is_auto_dowload_image = false, is_auto_dowload_video = false,
            is_auto_dowload_audio = false, is_auto_dowload_files = false;
    Handler seekHandler = new Handler();
    ImageView mTempView = null;
    long bites = 0;
    Runnable run;
    ArrayList<MediaPlayer> list = new ArrayList<MediaPlayer>();
    ArrayList<Button> PlayBtnlist = new ArrayList<Button>();
    int mPressed;
    ArrayList TempBtn = new ArrayList();
    boolean mFirstRun = true;
    int s = 0;
    int mBtnClick = 0;
    ArrayList listTemp = new ArrayList();
    String mAudioTagValue = "";
    String current_song = "";
    String Normallist = "";
    ArrayList Meaninglist = new ArrayList();
    ArrayList Nemelist = new ArrayList();
    ArrayList mMeaningList = new ArrayList();
    ArrayList mMessegeList = new ArrayList();
    AlertDialog.Builder b;
    ArrayList<String> mSeenList = new ArrayList<String>();
    int font_size = 0;
    int notificatiob_font_size = 12, status_font_siz = 12, msg_font_size = 16;
    String mReplace, mRemove;
    EditText mChatTypingTxt;
    String outputFile;
    String status_lock = "check";
    Boolean isBound = false;
    boolean bSelectText;
    int nymcount = 0;
    boolean nymboolean = true;
    // ChatManager chatManager;
    RounderImageView btn_send;
    int height = 0, width = 0;
    ImageView btn_emo;
    LinearLayout btn_title_layout;
    Button btn_title;
    TextView txt_title;
    public static EditText edt_msg;
    ListView listMeaning;
    GridView gridview_emo;
    String status = "";
    Boolean is_status_change = false;
    Boolean is_text = true;
    Boolean is_started = false, IS_SECRET_CHAT = false;
    MediaRecorder myRecorder;
    String contact_msg = "Received message from";
    String msg_ids = null, output;
    boolean mFirsttime = false;
    Boolean mIsMirrorEnabled = false, is_startrec = false;
    String mstatus = "1";
    Boolean mDest = false;
    Bitmap contactInfoImage;

    BroadcastReceiver lastseen_event = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            // Constant.printMsg("Broadcas Recievwd!!!!");
            if (intent.getAction().equals("lastseen_broadcast")) {

                try {
                    String from = intent.getStringExtra("from");
                    String type = intent.getStringExtra("type");
                    String type_msg = intent.getStringExtra("type_msg");
                    // new MyAsync().execute();

                    if (from != null) {
                        if (from.equalsIgnoreCase(jid)) {
                            if (type != null) {
                                if (type.equalsIgnoreCase(Constant.TYPING_STRING)) {
                                    mChatHedderUserStatusTxt.setText("typing...");

                                    if (type_msg != null) {

                                        if (mIsMirrorEnabled) {
                                            mChatTypingMsgLayout.setVisibility(View.VISIBLE);
                                            mChatTypingTxt.setTextColor(Color.parseColor("#fe0000"));
                                            mChatTypingTxt.setText(type_msg);
                                            int pos = mChatTypingTxt.getText().length();
                                            mChatTypingTxt.setSelection(pos);

                                        }
                                    }
                                    isStatusTyping = true;

                                    callOnlineStatus();

                                } else if (type
                                        .equalsIgnoreCase(Constant.STATUS_RECORDING)) {
                                    mChatHedderUserStatusTxt.setText("recording...");
                                    isStatusTyping = true;

                                    callOnlineStatus();
                                } else if (type
                                        .equalsIgnoreCase("jid_status_from_presence")) {

                                    Constant.printMsg("GGGGGGG Chat test recived inside GetJId");

                                    getJidOnlineStatus(jid);
                                }

                            }
                        }
                    }
                } catch (Exception e) {

                }

            }else if(intent.getAction().equals("getjid_online")) {

                try {
                    String msg = intent.getStringExtra("title_msg");
                    boolean status = intent.getBooleanExtra("status", false);

                    ChatTest.txt_sub_title.setText(msg);

                    ChatTest.isJidOnline = status;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            else if (intent.getAction().equals("chat")) {
                try {
                    String jid1 = intent.getStringExtra("jid");

                    if (jid1 != null) {
                        if (jid1.equals(jid)) {
                            //   updateCursor();
                            //                        new FetchChat()
                            //                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            Constant.printMsg("GGGGGGG Chat test recived inside chatttt");
                            if (!isFetchChatReady) {
                                isFetchChatReady = true;
//                                 ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
                                new FetchChat().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                            }

                        }
                    }
                } catch (Exception e) {

                }

            } else if (intent.getAction().equals("destruct_service")) {


                try {
                    boolean stopDest = stopService(new Intent(mParentActivity, DestructService.class));

                    Constant.printMsg("GGGGGGG Chat test recived inside destruct " + stopDest);
                    //                isDestructCall = true;
                    // new FetchChat().execute();
                } catch (Exception e) {

                }


            } else if (intent.getAction().equals("destruct_time")) {


                try {

                    String postion = intent.getStringExtra("position");
                    String time = intent.getStringExtra("time");
                    String jidvalue = intent.getStringExtra("jid");

                    if(jidvalue.equalsIgnoreCase(jid)) {
                        Constant.printMsg("GGGGGGG Chat test recived inside destruct destruct_time" + postion + "    " + time);
                        Constant.printMsg("DEEEEESSSSSTTTTT4 "+postion);


                        mRightDestTime = (TextView) findViewById(Integer.valueOf(postion) + 100000);
                        mRightDestTime.setText(time);
                        mRightTextview = (EmojiconTextView) findViewById(Integer.valueOf(postion));
                        mRightTipLayout = (FrameLayout) findViewById(Integer.valueOf(postion) + 200000);

                        if (Integer.valueOf(time) == 0) {

                            mRightTipLayout.setVisibility(View.GONE);
                            //  mRightTextview.setVisibility(View.GONE);
                            //mDynamicView.removeViewAt(Integer.valueOf(postion));

//                            try {
//
//                                for (int i=0;i<msg_list.size();i++){
//
//                                    if(String.valueOf(msg_list.get(i).getPosition()).equalsIgnoreCase(postion))
//
//                                    {
//
//                                        ChatTest.msg_list.remove(i);
//                                        ChatTest.dest_list_msgids.remove(i);
//                                        ChatTest.dest_list_bombids.remove(i);
//                                        ChatTest.dest_list.remove(i);
//                                        ChatTest.dest_list_anim.remove(i);
//                                    }
//
//                                }
//
//
//
//                            } catch (Exception e) {
//
//                            }


//                        k = msg_list.size()-1;

//                        mRightTipLayout=(FrameLayout) findViewById(Integer.valueOf(postion)+200000);
//                        mRightTipLayout.setVisibility(View.GONE);
//                        mRightDestTime.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {

                }


            } else if (intent.getAction().equals("update_tick")) {

                String postion = intent.getStringExtra("position");
                String status = intent.getStringExtra("status");

                Constant.printMsg("GGGGGGG Chat test recived inside update_tick");


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
                } catch (Exception e) {

                }

            } else if (intent.getAction().equals("update_left")) {

                Constant.printMsg("GGGGGGGPOGG<GLGMG<MG"+k+"       "+msg_list.size());

                k=msg_list.size()-1;

                try {

//                    k = Integer.valueOf(intent.getStringExtra("position"));

                    initializeListElememts(2);

                    if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 0) {
                        Constant.printMsg("GGGGGGGPOGG<GLGMG<MG111111");


                        leftTextChat();
                        setLeftChatText();

                    } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 1) {

                        leftImageChat();
                        synchronized (this) {
                            setLeftDownloadImage();
                        }
                    } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 2) {

                        leftVideoChat();
                        setLeftVideo();

                    } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 5) {

                        leftContactChat();
                        setLeftContact();

                    } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 3) {

                        //Audio
                        leftAudioChat();
                        setLeftAudio_Old();

                    } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 4) {

                        //Location
                        leftImageChat();
                        setLeftLocation();

                    }
                    mRightTipLayout.requestFocus();
                    edt_msg.requestFocus();

                } catch (Exception e) {

                    Constant.printMsg("HHHHHHHHHHFOJF"+e.toString());

                }

                k = k + 1;

            } else if (intent.getAction().equals("remove_subtitle")) {

                if (Connectivity.isOnline(mParentActivity)) {
                    try {
                        if (TempConnectionService.connection != null) {
                            if (TempConnectionService.connection.isAuthenticated())
                                getJidOnlineStatus(jid);
                        }
                    } catch (Exception e) {

                    }
                } else {
                    txt_sub_title.setText(context
                            .getString(R.string.tap_here_for_info));
                }

            } else if (intent.getAction().equals("secret_chat_local")) {
                Constant.printMsg("GGGGGGG Chat test recived inside secret");
                ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
            } else if (intent.getAction().equals("invisible")) {
                try {
                    mChatTypingMsgLayout.setVisibility(View.GONE);
                } catch (Exception e) {

                }
            }else if (intent.getAction().equals("update_image_cancel")) {
                try {
                    int j = intent.getIntExtra("key", -1);

                    if (j!=-1) {
                        try {
                            Constant.printMsg("......Chat Image CCCC...22.");
                            mRightImageChatUpload = (ImageView) findViewById(j + 800000);
                            mRightImageChatCancel = (ImageView) findViewById(j + 120000);
                            mRightImageProgress = (ProgressBar) findViewById(j + 900000);
                            mRightImageChatUpload.setVisibility(View.VISIBLE);
                            mRightImageChatCancel.setVisibility(View.GONE);
                            mRightImageProgress.setVisibility(View.GONE);


                        } catch (Exception e) {
                            Constant.printMsg("dosadllas" + e.toString());
                        }
                    }
                } catch (Exception e) {

                }
            }else if (intent.getAction().equals("update_audio_cancel")) {
                try {
                    mChatTypingMsgLayout.setVisibility(View.GONE);
                } catch (Exception e) {

                }
            }else if (intent.getAction().equals("update_video_cancel")) {
                try {
                    mChatTypingMsgLayout.setVisibility(View.GONE);
                } catch (Exception e) {

                }
            }else if (intent.getAction().equals("image_upload")) {

                int j = intent.getIntExtra("key", -1);


                Constant.printMsg("BBBBBBBB " + j);



                    if (j!=-1) {
                        try {
                            Constant.printMsg("......Chat Image CCCC...22.");
                            mRightImageChatUpload = (ImageView) findViewById(j + 800000);
                            mRightImageChatCancel = (ImageView) findViewById(j + 120000);
                            mRightImageProgress = (ProgressBar) findViewById(j + 900000);
                            mRightImageChatUpload.setVisibility(View.GONE);
                            mRightImageChatCancel.setVisibility(View.GONE);
                            mRightImageProgress.setVisibility(View.GONE);
                            mRightImageTickMark = (ImageView) findViewById(j + 600000);
                            mRightImageTickMark
                                    .setImageResource(R.drawable.receipt_from_server);

                        } catch (Exception e) {
                            Constant.printMsg("dosadllas" + e.toString());
                        }
                    }

              /*  for(int j=0;j<msg_list.size();j++){

                    if(msg_list.get(j).getKey_id().equalsIgnoreCase(key)){

                        Constant.printMsg("hjjjjjafasb");
                        mRightImageChatUpload=(ImageView) findViewById(j+800000);
                        mRightImageChatCancel=(ImageView) findViewById(j+120000);
                        mRightImageProgress=(ProgressBar) findViewById(j+900000);
                        mRightImageChatUpload.setVisibility(View.GONE);
                        mRightImageChatCancel.setVisibility(View.GONE);
                        mRightImageProgress.setVisibility(View.GONE);
                        mRightImageTickMark=(ImageView) findViewById(j+600000);
                        mRightImageTickMark
                                .setImageResource(R.drawable.receipt_from_server);
                    }

                }*/
            }
        }

    };
    Boolean is_startRed = false;
    AlertDialog.Builder mDeleteAlert;
    boolean mcheck = false;
    MenuItem item;
    Animation myFadeInAnimation;
    boolean isClicked;
    Cursor cursor;
    DatabaseHelper dbAdapter;
    MultiUserChat muc;
    PrivacyListManager privacyManager;
    Editor ed1;
    /**
     * ********** Php script path ***************
     */
    boolean isFirstCall = false;
    boolean isDeletEl = false;
    int IS_OWNER = 1;
    int Count = 1;
    // int[] self_desc_time = { 0, 2, 5, 30, 3600, 86400, 604800 };
    int[] self_desc_time = {0, 5, 10, 15};
    int selected_self_desc_time = 0;
    int selected_self_desc_index = 0;
    LinearLayout emoji_frag, zlay, mTextLay, linearLayoutPopup;
    SharedPreferences sp, pref, sp1;
    Editor ed;
    SharedPreferences sharedPrefs;
    //static String TAG = Chat.class.getSimpleName();
    Resources res;
    JNCryptor cryptor = new AES256JNCryptor();
    int i = 0;
    int count = 200;
    ArrayList<ContactsGetSet> users;
    int mBuxCount = 0;
    // SendTyping sendtyping = new SendTyping();
    // public static DeliveryReceiptManager mDeliveryReciept =
    // ServerConnectionAsync.mDeliveryReciept;
    Thread refresh;
    String lastseen = null;
    DriveFile file;
    ProgressDialog barProgressDialog;
    ImageView an_background;
    ImageView img_chat_background1;
    GIFView img_chat_background;
    ImageView img_chat_avatar;
    String mValue;
    SpannableStringBuilder ssb;
    boolean sidown = true, self = false;
    int idx1;
    int idx2;
    String toPrint = "";
    String txtsend;
    boolean bux = false;
    Button zbtn, zbbtn;
    ArrayList<String> imagesPathList;
    Animation shake, mMove;
    boolean mAnimation = false;
    View mZzleView;
    //    ImageButton mDownArrow;
    boolean mPopup = false, songListValue = false;
    int currentRotation = 0;
    int nymsCount = 0;
    ArrayList<String> nymsList = new ArrayList<String>();
    ArrayList<String> editList = new ArrayList<String>();
    ContactsGetSet contact;
    Bitmap bmp;
    String mFromNum, mToNUm, name, mkey, mData;
    Long MBuxs;
    Dbhelper db;
    ArrayList<String> meaningList = new ArrayList<String>();
    NynmAdapter meaningAdapter;
    List<NymsPojo> selectedtext = new ArrayList<NymsPojo>();
    List<String> songList;
    String songValue;
    String mParseString1 = "<n>wru1</n><m>where.are.you1</m> and <n>wru2</n><m>who.are.you1</m> and <n>hru3</n><m>how.are.you1</m>";
    String mParseString = "";
    String mParseString2 = "<n>wru1</n><m>where.are.you1</m> <n>hru2</n><m>how.are.you1</m>";
    String email;
    double sentMediaSize;
    int status_msg = 3;
    int typingStatusInterval = 0;
    ArrayList mFinalNyms = new ArrayList();
    ArrayList mFinalNymsMeaning = new ArrayList();
    ArrayList mFinalNymsMeaningLength = new ArrayList();
    String selectedWord_back = "";
    int mTextSizeVariation=0;
    public static String selectedWord_backTemp = "";
    // Chat Hedder Views
    ImageView mChatHedderBackImg, mChatHedderProfileImg, mChatHedderCopyImg, mChatHedderAttachmentImg, mChatHedderMenuImg, mToolTipImg;
    TextView mChatHedderUserTxt;
    LinearLayout mChatHedderLayout, mChatHedderTextLayout, mChatHedderCopyLayout, mChatHedderAttachmentLayout, mChatHedderMenuLayout, mDownArrowLayout,mSliderMenuLayout;
    boolean mMenuVisible = false;
    boolean mContactMenuVisible = false;
    LinearLayout mChatFooterLayout, mChatFooterEdittextLayout, mChatTypingMsgLayout;
    ImageView mChatFooterSlideMenuImg, mChatFooterEmojiconsImg;
    EmojiconEditText mChatFooterEdittext;
    RounderImageView mChatFooterSendBtn;
    int mTempLayoutHeight = 0;
    LinearLayout mChatMenuLayout;
    LinearLayout mChatOptionMenuLayout;
    //Menu option
    LinearLayout mChatOptionMenuMirrorchatLayout, mChatOptionMenuHedderLayout, mChatOptionMenuViewcontactLayout, mChatOptionMenuBlockLayout, mChatOptionMenuCallLayout, mChatOptionMenuLockchatLayout, mChatOptionMenuClearchatLayout;
    ImageView mChatOptionMenuMirrorchatImg, mChatOptionMenuViewcontactImg, mChatOptionMenuBlockImg, mChatOptionMenuCallImg, mChatOptionMenuLockchatImg, mChatOptionMenuClearchatImg;
    TextView mChatOptionMenuMirrorchatText, mChatOptionMenuViewcontactTxt, mChatOptionMenuBlockTxt, mChatOptionMenuCallTxt, mChatOptionMenuLockchatTxt, mChatOptionMenuClearchatTxt;
    //Attachment Icon menu window
    LinearLayout mchatHeadBackLayout, mChatMenuHedderLayout, mChatMenuGalleryLayout, mChatMenuShootLayout, mChatmenuVideoLayout, mChatMenuLocationLayout, mChatMenuContactLayout, mChatMenuAudioLayout;
    ImageView mChatMenuGalleryImg, mChatMenuShootImg, mChatMenuVideoImg, mChatMenuLocationImg, mChatMenuContactImg, mChatMenuAudioImg;
    TextView mChatMenuGalleryTxt, mChatMenuShootTxt, mChatMenuVideoTxt, mChatMenuLocationTxt, mChatMenuContactTxt, mChatMenuAudioTxt;
    LoaderManager loaderManager;
    int pos = 0;
    LinearLayout mDynamicView;
    ScrollView mScrollView;
    TextView mBlockContact, mAddContact;
    LinearLayout mContactLayout;
    ArrayList<Integer> mOnLongSelectedPostions = new ArrayList<Integer>();
    boolean mIsLogClick = false;
    LinearLayout mRightChatLayout;
    FrameLayout mRightTipLayout;
    // Right Textview..
    EmojiconTextView mRightTextview;
    TextView mRightSenderTimeText;
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

    // private ServiceConnection mConnection = new ServiceConnection() {
    // @Override
    // public void onServiceConnected(ComponentName className, IBinder service)
    // {
    //
    // // // mBoundService = ((KaChingMeService.LocalBinder) service)
    // // .getService();
    // // connection = mBoundService.getConnection();
    // // try {
    // // privacyManager = PrivacyListManager.getInstanceFor(connection);
    // // } catch (Exception e) {//
    // // ACRA.getErrorReporter().handleException(e);
    // // // TODO: handle exception
    // // }
    //
    // // chatmanagerListener = new ChatManagerListenerImpl(mBoundService,
    // // "");
    // //
    // // if (dbAdapter.getLogin().size() > 0) {
    // // chatmanagerListener = new ChatManagerListenerImpl(mBoundService,
    // // dbAdapter
    // // .getLogin().get(0).getUserName()
    // // + KachingMeApplication.getHost());
    // // }
    // // chatmanager = ChatManager.getInstanceFor(connection);
    // // chatmanager.addChatListener(chatmanagerListener);
    //
    // // Constant.printMsg("EEEEEEEEEEE onService Connection"
    // // + mBoundService.getManagerListener() + "--" + mBoundService
    // // + "--" + dbAdapter.getLogin().get(0).getUserName());
    // //
    // // if (mBoundService.getManagerListener() != null) {
    // // CommonMethods
    // // .prtMsg("EEEEEEEEEEE onService Connection if chatmangerListener "
    // // + mBoundService.getManagerListener());
    // // } else {
    // // CommonMethods
    // // .prtMsg("EEEEEEEEEEE onService Connection ELSE chatmangerListener "
    // // + mBoundService.getManagerListener());
    // // mBoundService.setChatManagerListener();
    // // }
    // //
    // // mBoundService.ResendMessages();
    //
    // // XMPPConnection connectionXmpp;
    // // ConnectionConfiguration config;
    // // connectionXmpp = (XMPPConnection)connection;
    // //
    // // org.jivesoftware.smack.AccountManager accountManager =
    // // connection.
    // //
    // // accountManager.deleteAccount();
    // // connection.disconnect();
    //
    // try {
    // Presence presence = new Presence(Presence.Type.available);
    // presence.setMode(Mode.available);
    // //
    // presence.setProperty("is_lastseen",sharedPrefs.getBoolean("privacy_lastseen",
    // // true));
    // // connection.sendStanza(presence);
    //
    // } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
    // // TODO: handle exception
    // }
    //
    // // new MyAsync().execute();
    // // connect();
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
    // Log.i("Chat Foerword Message",
    // "media wa tyoe::" + msg.getMedia_wa_type()
    // + " ID::" + msg.get_id());
    // // ////////*Text MEssage*////////////
    // if (msg.getMedia_wa_type().equals("0")) {
    //
    // sendMessage(msg.getData());
    // }
    // // ////////*Images Message*////////////
    // else if (msg.getMedia_wa_type().equals("1")) {
    //
    // Forword_Image(msg);
    //
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
    // "" + msg.getLongitude(), msg.getRow_data(),
    // msg.getData());
    // }
    // // ////////*Contacts Message*////////////
    // else if (msg.getMedia_wa_type().equals("5")) {
    // send_Contact(msg.getRow_data(), msg.getData(),
    // msg.getMedia_name());
    // Constant.printMsg("in contactsss::555555"
    // + msg.getRow_data() + msg.getData()
    // + msg.getMedia_name());
    // }
    // // ////////*File Message*////////////
    // else if (msg.getMedia_wa_type().equals("6")) {
    //
    // FORWORD_FILE(msg);
    //
    // }
    //
    // else if (msg.getMedia_wa_type().equals("11")) {
    //
    // Forword_Image(msg);
    //
    // }
    //
    // }
    // }
    // }
    //
    // // new Async_Privacy().execute();
    // }
    //
    // @Override
    // public void onServiceDisconnected(ComponentName className) {
    //
    // // mBoundService = null;
    // }
    //
    // };
    //Left Image
    ImageView mLeftImageChat;
    TextView mLeftImageTextTime;
    ImageView mLeftImageChatDownload;
    ProgressBar mLeftImagetProgressBar;
    //Left Video
    ImageView mLeftVideoChat;
    TextView mLeftVideoTimeText;
    ImageView mLeftVideoChatDownload;

    /*
                       * private class MyAsync extends AsyncTask<String, String, String> {
                       *
                       * String lastseen; Presence presence; Boolean is_lastseen_available=true;
                       *
                       * @Override protected void onPreExecute() { // TODO Auto-generated method
                       * stub super.onPreExecute();
                       *
                       * is_status_change = false; is_lastseen_available=true; }
                       *
                       * @Override protected String doInBackground(String... params) {
                       *
                       * Log.d("Chat","Last seen Called....."); try {
                       *
                       * Calendar cal_today = Calendar.getInstance(); SimpleDateFormat
                       * formatter_date = new SimpleDateFormat( "dd-MM-yyyy"); SimpleDateFormat
                       * formatter_last = new SimpleDateFormat( "dd-MM-yyyy hh:ssa");
                       * SimpleDateFormat formatter_today = new SimpleDateFormat( "hh:mma");
                       * Log.d("Last Activity", "Called" +
                       * LastActivityManager.isLastActivitySupported( connection, jid));
                       * LastActivity la = LastActivityManager.getLastActivity(connection, jid);
                       *
                       * Calendar cal = Calendar.getInstance(); Long l = (cal.getTimeInMillis() -
                       * (la.getIdleTime() * 1000)); cal.setTimeInMillis(l);
                       *
                       * String today = formatter_date.format(cal_today.getTime()); String
                       * last_seen_date = formatter_date.format(cal.getTime()); if
                       * (today.toString().equals(last_seen_date)) { //
                       * getSupportActionBar().setSubtitle
                       * ("last seen todat at "+formatter_today.format(cal.getTime()));
                       * //Constant.printMsg("last seen today at "+
                       * formatter_today.format(cal.getTime())); lastseen =
                       * getResources().getString(R.string.today_at)+" " +
                       * formatter_today.format(cal.getTime()); } else {
                       * //Constant.printMsg("last seen todat at " +
                       * formatter_today.format(cal.getTime())); lastseen = ""+
                       * formatter_last.format(cal.getTime()); }
                       *
                       * } catch (Exception e) { e.printStackTrace(); }
                       *
                       * try { Roster roster = connection.getRoster();
                       *
                       * presence = roster.getPresence(jid);
                       * if(presence.getProperty("is_lastseen")!=null) {
                       * Log.d("Chat","IS Last Seen"+presence.getProperty("is_lastseen"));
                       * is_lastseen_available=(Boolean)presence.getProperty("is_lastseen"); } }
                       * catch (Exception e) { // TODO: handle exception e.printStackTrace(); }
                       * return null; }
                       *
                       * @Override protected void onPostExecute(String result) {
                       * txt_sub_title.setVisibility(View.VISIBLE); try {
                       *
                       * Log.d(TAG,"Presence::"+presence.isAvailable()+" Presence Mode::"+presence.
                       * getMode()+" Lastseen available::"+is_lastseen_available); if
                       * (presence.isAvailable()) {
                       *
                       * if (presence.getMode() == Mode.away) { // Log.d("Chat", "AWAY........");
                       * if(is_lastseen_available) { txt_sub_title.setText(lastseen); } else {
                       * txt_sub_title.setVisibility(View.GONE); } is_available = false;
                       *
                       * } else{ //Log.d("Chat", "AWAYLABLE........");
                       * txt_sub_title.setText(getResources().getString(R.string.online));
                       * is_available = true; }
                       *
                       * } else if(is_lastseen_available){ //Log.d("Chat", "AWAY....2........");
                       * is_available = false; txt_sub_title.setText(lastseen); }
                       *
                       *
                       * is_status_change = true; }catch (Exception e) {
                       *
                       * } super.onPostExecute(result);
                       *
                       * }
                       *
                       * }
                       */
    ImageView mLeftVideoButtonPlay;
    ImageView mLeftVideoImgOverlay;
    TextView mLeftVideoSize;

    //    public void updateAdapter(Cursor data) {
    //
    //        // updateReadMessages();ss
    //        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
    //        mLayoutManager.setStackFromEnd(true);
    //        listview.setLayoutManager(mLayoutManager);
    //      /*   listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getApplicationContext()).build());
    //        listview.setAdapter(adaptor);*/
    //        listview.scrollToPosition(adaptor.getItemCount());
    //        if (!isFirstCall) {
    //
    //            if(!GlobalBroadcast.isServiceRunning(DestructService.class.getCanonicalName(), mParentActivity))
    //            startService(new Intent(mParentActivity, DestructService.class));
    //
    //            for (int i = 0; i < data.getCount(); i++) {
    //                dest_list.add(0);
    //                dest_list_msgids.add(0);
    //                dest_list_anim.add(0);
    //                dest_list_bombids.add(R.drawable.black_bomb);
    //            }
    //
    //            isFirstCall = true;
    //        }
    //
    //
    //      //  this.adaptor.swapCursor(data);
    ////        updateCursor(data);
    //
    //
    //    }
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
    Runnable AsyncConnection = new Runnable() {

        @Override
        public void run() {

            if (!IS_TYPED) {
                presence_typing(Constant.TYPING_STRING);
            }

            handler.postDelayed(AsyncConnection, typingStatusInterval);

        }

    };
    SeekBar mLeftAudioSeekBar;
    ProgressBar mLeftAudioDownloadProgress;
    TextView mLeftAudioSize;
    TextView mLeftAudioDuration;
    TextView mLeftAudioTextTime;
    ImageView mLeftAudioTickMark;
    private Handler mHandler = new Handler();
    private List<String> replacePosition = new ArrayList<String>();
    private Timer timer;
    private org.jivesoftware.smack.chat.Chat chat;
    private ActionMode mActionMode;
    private HashMap<String, Integer> emoticons = new HashMap<String, Integer>();
    private ArrayList<String> arrayListSmileys = new ArrayList<String>();
    private boolean isChangedStat = false;
    private boolean is_enter_is_send = false;
    private boolean is_new_sec = false;
    private boolean UserBlocked = false;
    private Uri fileUri;
    private DriveId mSelectedFileDriveId;
    private Menu menu = null;
    private Animation popupShow;
    private Animation popupHide;
    private boolean IsProcessOnAddContact = false;
    private TextView tvBlock;
    private ProgressDialog pDialog;
    private ViewGroup listViewheader;
    private String[] arrPath;
    private boolean[] thumbnailsselection;
    private int ids[];
    private NetworkSharedPreference mNewtSharPref;
    // The callbacks through which we will interact with the LoaderManager.
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    String mLedText ;

    public static boolean isAvailable(Context ctx, Intent intent) {
        final PackageManager mgr = ctx.getPackageManager();
        List<ResolveInfo> list = mgr.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    byte[] avatar;
    public static void updateAudioBtnStatus(boolean status) {
        isPlayAudio = status;
    }

    public static void updateLeftAudioBtnStatus(boolean status) {
        isLeftPlayAudio = status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            Constant.printMsg("Loader came 0");
            super.onCreate(savedInstanceState);
            Window window = getWindow();

            window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);

            setContentView(R.layout.chat_muc);

            dest_msg_list = new ArrayList<>();

            mParentActivity = this;
            //mCallbacks = this;

            MEDIA_UPLOAD_URL = KachingMeConfig.UPLOAD_MEDIA;

            dest_list = new ArrayList<Integer>();
            dest_list_msgids = new ArrayList<Integer>();
            dest_list_bombids = new ArrayList<Integer>();
            dest_list_anim = new ArrayList<Integer>();

            mIntVariable();
            mScreenArrangement();
            Constant.mKonsFromChat = false;

            loaderManager = getSupportLoaderManager();

            // new ServerConnectionAsync(this).execute();

            // Shared Preference
            mNewtSharPref = new NetworkSharedPreference(this);

            pDialog = new ProgressDialog(this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setCancelable(false);
            pref = PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext());
            getWindow().setSoftInputMode(
                    LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            btn_send = (RounderImageView) findViewById(R.id.sendButton);
            btn_send.setImageDrawable(getResources().getDrawable(
                    R.drawable.ic_action_device_access_mic));
            btn_emo = (ImageView) findViewById(R.id.btn_emo);
            edt_msg = (EditText) findViewById(R.id.edt_messagegrp);
            listview = (RecyclerView) findViewById(R.id.convlist);
            //        gridview_emo = (GridView) findViewById(R.id.gridview_emo);
            img_chat_background1 = (ImageView) findViewById(R.id.img_chat_background1);

            img_chat_background = (GIFView) findViewById(R.id.img_chat_background);
            emoji_frag = (LinearLayout) findViewById(R.id.emoji_frag);
            an_background = (ImageView) findViewById(R.id.anim_background);
            if (ChatDictionary.mDictionaryList.size() == 0) {
                fetchNymFrom();
            }
            Constant.mZzleGroup = false;
            Constant.mKonsFromChat = false;
            Constant.mBazzleGroup = false;
            Constant.mKonsGroup = false;

            Constant.mNynmFromSlider = false;
            Constant.mKonsFromSlider = false;
            Constant.mKroKFromSlider = false;
            // zbtn = (Button) findViewById(R.id.zzle_btn);
            //    zlay = (LinearLayout) findViewById(R.id.zzle_layout);
            // zbbtn = (Button) findViewById(R.id.zzle_bannerbtn);
            //        mDownArrow = (ImageButton) findViewById(R.id.btn_down_arrow);
            //    mZzleView = (View) findViewById(R.id.zzle_view);

            mTextLay = (LinearLayout) findViewById(R.id.text_lay);
            //    mEditTextLay = (LinearLayout) findViewById(R.id.edit_text_lay);
            shake = AnimationUtils.loadAnimation(this, R.anim.shakeanim);
            listMeaning = (ListView) findViewById(R.id.mnglist);
            mMove = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.reverse);
            //        mEditTextLay.setBackgroundResource(R.drawable.roundcorner);
            myFadeInAnimation = AnimationUtils
                    .loadAnimation(mParentActivity, R.anim.twin);
            users = new ArrayList<ContactsGetSet>();
            // users = dbAdapter.getAllContacts();
            res = getResources();
            KachingMeApplication.SELECTED_TAB = 0;
            edt_msg.setBackgroundColor(Color.TRANSPARENT);
            dbAdapter = KachingMeApplication.getDatabaseAdapter();
            db = new Dbhelper(getApplicationContext());

            sp1 = PreferenceManager.getDefaultSharedPreferences(this);

            TranslateAnimation anim = new TranslateAnimation(0, 150, 0, 0);
            anim.setDuration(500);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            popupShow = AnimationUtils.loadAnimation(this, R.anim.popin);
            popupShow.setAnimationListener(this);
            popupHide = AnimationUtils.loadAnimation(this, R.anim.popout);
            popupHide.setAnimationListener(this);

            // linearLayoutPopup = (LinearLayout) findViewById(R.id.pop_up);
            // linearLayoutPopup.setVisibility(LinearLayout.GONE);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Constant.screenHeight = metrics.heightPixels;
            Constant.screenWidth = metrics.widthPixels;

            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);

            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            is_enter_is_send = sharedPrefs.getBoolean("enter_is_send", false);

            sp = getSharedPreferences(KachingMeApplication.getPereference_label(),
                    Activity.MODE_PRIVATE);
            ed = sp.edit();


            String mFinalBlockedUser=null;
            mFinalBlockedUser = sp.getString(Constant.BLOCKED_USERS,"");
            String[] parts = mFinalBlockedUser.split("-");

            ArrayList mBlockedList=new ArrayList();
            for(int j=0;j<parts.length;j++){
                mBlockedList.add(parts[j].trim());
            }
            KachingMeApplication.setBlocked_user(mBlockedList);

            Constant.printMsg("BLLLLLLL"+sp.getString(Constant.BLOCKED_USERS,"")+"   "+mBlockedList);

            DisplayMetrics metricss = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metricss);
            Constant.screenHeight = metrics.heightPixels;
            Constant.screenWidth = metrics.widthPixels;
            int x = dbAdapter.getLastMsgid_chat(jid, sec);
            Constant.printMsg("looooollll   " + x);
            if (KachingMeApplication.getsharedpreferences().contains("wallpaper")) {

                Constant.wallType = sp1.getString("wallpaper_type", "");
                Constant.printMsg("type of wallpaper   " + Constant.wallType);

                if (Constant.wallType.equalsIgnoreCase("file"))
                {
                    File f = new File(KachingMeApplication.getsharedpreferences()
                            .getString("wallpaper", null));
                    img_chat_background1.setVisibility(View.VISIBLE);
                    img_chat_background1.setImageURI(Uri.fromFile(f));
                    img_chat_background.setVisibility(View.GONE);
                }
                else
                {
                    String wall = KachingMeApplication.getsharedpreferences()
                            .getString("wallpaper", "");
                    // Constant.printMsg("wallll   " + wall);
                    img_chat_background1.setVisibility(View.GONE);
                    img_chat_background.setVisibility(View.VISIBLE);
                    img_chat_background = (GIFView) findViewById(R.id.img_chat_background);

                    File f = new File(KachingMeApplication.getsharedpreferences()
                            .getString("wallpaper", null));
                    img_chat_background = (GIFView) findViewById(R.id.img_chat_background);

                    Constant.printMsg("paththththth ::::: >>>>>>> " + f);

                    String pathName = f.getAbsolutePath();

                    Constant.gifimage = pathName;
                    img_chat_background = (GIFView) findViewById(R.id.img_chat_background);
                }
            }

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                jid = bundle.getString("jid");

                getSupportActionBar().setTitle(bundle.getString("name"));
                try {
                    home_title = bundle.getString("name");

                } catch (Exception e) {
                    // TODO: handle exception

                    home_title = "";
                }


                try {

                    avatar = bundle.getByteArray("avatar");

                } catch (Exception e) {
                    Constant.printMsg("avatar exo " + e);

                }
                SliderTesting.msg_ids = null;
                if (bundle.getString("msg_ids") != null) {
                    Log.i("", "Forworded msges::" + bundle.getString("msg_id"));
                    msg_ids = bundle.getString("msg_ids");

                }

                Constant.printMsg("jid jid jid " + jid);

                Constant.mself_jid = jid;

                ConcurrentAsyncTaskExecutor.executeConcurrently(new MyAsync(), (new String[]{jid}));

                IS_SECRET_CHAT = bundle.getBoolean("IS_SECRET_CHAT");
                Constant.printMsg("jid:>>>>>>" + jid);
                Constant.mReceiverId = jid;
                if (IS_SECRET_CHAT) {

                    sec = 0;

                } else {
                    sec = 1;
                }

                Log.d("Chat",
                        "IS secret chat::" + sec + "   "
                                + bundle.getString("is_owner"));
                String bunto = bundle.getString("is_owner");
                Constant.printMsg("buvle che:" + bunto);
                if (bunto != null && bunto.equals("2")) {
                    // Do something here.
                } else {

                    IS_OWNER = 0;

                }

                ed.putInt(jid + "_sec_admin", 0);
                ed.commit();

                if (sp.contains(jid + "_sec_admin")) {
                    if (sp.getInt(jid + "_sec_admin", 0) == 0) {
                        IS_OWNER = 0;

                    } else {
                        IS_OWNER = 1;
                    }

                } else {
                    IS_OWNER = 1;
                }

                is_new_sec = bundle.getBoolean("is_new_sec", false);

                // is_new_sec = true;

            }

            if (Connectivity.isConnected(mParentActivity)) {

                new getKeyword().execute();

            }
            Constant.printMsg("bbbbliocked    " + jid + "    "
                    + KachingMeApplication.getBlocked_user());


          /*
           * actionBar.setLogo(R.drawable.avtar);
           * actionBar.setIcon(R.drawable.avtar);
           */
            txt_title = (TextView) findViewById(R.id.chat_hedder_user_txt);
            txt_sub_title = (TextView) findViewById(R.id.chat_hedder_user_status_txt);
            img_chat_avatar = (ImageView) findViewById(R.id.chat_hedder_profile_img);
            btn_title_layout = (LinearLayout) findViewById(R.id.chat_hedder_layout);

            try {

                if (home_title.length() > 15) {
                    txt_title.setText(home_title.substring(0, 15) + "...");
                } else {
                    txt_title.setText(home_title);
                }        // txt_sub_title.setVisibility(View.GONE);

            } catch (Exception e) {


            }


            // start
            //        new MyAsync1().execute();


            // Forward messages

            if (msg_ids != null) {
                String msg_id[] = msg_ids.toString().split(",");

                for (int i = 0; i < msg_id.length; i++) {
                    Log.i("Chat Foerword Message", "Message ID::"
                            + msg_id[i]);
                    MessageGetSet msg = dbAdapter
                            .getMessages_by_msg_id(msg_id[i]);
                    Log.i("Chat Foerword Message",
                            "media wa tyoe::" + msg.getMedia_wa_type()
                                    + " ID::" + msg.get_id());
                    msg.setPostion(i);

                    // ////////*Text MEssage*////////////
                    if (msg.getMedia_wa_type().equals("0")) {

                        sendMessage(msg.getData());
                    }
                    // ////////*Images Message*////////////
                    else if (msg.getMedia_wa_type().equals("1")) {

                        Forword_Image(msg);

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
                                "" + msg.getLongitude(), msg.getRow_data(),
                                msg.getData());
                    }
                    // ////////*Contacts Message*////////////
                    else if (msg.getMedia_wa_type().equals("5")) {
                        send_Contact(msg.getRow_data(), msg.getData(),
                                msg.getMedia_name());
                        Constant.printMsg("in contactsss::555555"
                                + msg.getRow_data() + msg.getData()
                                + msg.getMedia_name());
                    }
                    // ////////*File Message*////////////
                    else if (msg.getMedia_wa_type().equals("6")) {

                        FORWORD_FILE(msg);

                    } else if (msg.getMedia_wa_type().equals("11")) {

                        Forword_Image(msg);

                    }

                }
            }


//

//        ImageView mSenderRoundImage = new ImageView(mParentActivity);
//        mSenderRoundImage.setLayoutParams(mRoundImageParams);

            Constant.printMsg("LLLLLLLIS info dd" + Constant.FROM_CHAT_SCREEN);
            if(Constant.FROM_CHAT_SCREEN.equalsIgnoreCase("notification")){

                if(Constant.mReciverAvathor!=null){

                    ProfileRoundImg  mSenderImage=new ProfileRoundImg(Constant.mReciverAvathor);
                    img_chat_avatar.setImageDrawable(mSenderImage);

                }else{
                    Bitmap  bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2,Util.getBitmapOptions());
                    ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                    img_chat_avatar.setImageDrawable(mSenderImage);
                }


            }else  if (Constant.FROM_CHAT_SCREEN.equalsIgnoreCase("chat")) {

                try {
                    if (UserChatList.mProfileImagesList.size() > 0) {
                        //  mSenderRoundImage.setImageBitmap(UserChatList.mProfileImagesList.get(UserChatList.mPosition));

                        ProfileRoundImg mSenderImage = new ProfileRoundImg(UserChatList.mProfileImagesList.get(UserChatList.mPosition));
                        img_chat_avatar.setImageDrawable(mSenderImage);

                    } else {

                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2,Util.getBitmapOptions());
                        ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                        img_chat_avatar.setImageDrawable(mSenderImage);
                    }
                } catch (Exception e) {

                }

            }else if (Constant.FROM_CHAT_SCREEN.equalsIgnoreCase("contact")) {


                try {
                    if (FavouriteContacts.mProfileImagesList.size() > 0) {
                        //                        mSenderRoundImage.setImageBitmap(FavouriteContacts.mProfileImagesList.get(FavouriteContacts.mPosition));

                        ProfileRoundImg mSenderImage = new ProfileRoundImg(FavouriteContacts.mProfileImagesList.get(FavouriteContacts.mPosition));
                        img_chat_avatar.setImageDrawable(mSenderImage);

                    } else {

                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2);
                        ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                        img_chat_avatar.setImageDrawable(mSenderImage);
                    }
                    Constant.printMsg("LLLLLLLIS" + "   " + FavouriteContacts.mPosition + "  " + FavouriteContacts.mProfileImagesList);

                } catch (Exception e) {

                }

            }
            else
            {

                Constant.printMsg("LLLLLLLIS infosss"  );

                   try {
                        if(avatar!=null)
                    {


                    System.gc();
                    Bitmap bmp = BitmapFactory.decodeByteArray(avatar,
                            0, avatar.length,Util.getBitmapOptions());
                        //                        mSenderRoundImage.setImageBitmap(FavouriteContacts.mProfileImagesList.get(FavouriteContacts.mPosition));

                        ProfileRoundImg mSenderImage = new ProfileRoundImg(bmp);
                        img_chat_avatar.setImageDrawable(mSenderImage);

                    } else {

                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2,Util.getBitmapOptions());
                        ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                        img_chat_avatar.setImageDrawable(mSenderImage);
                    }
                } catch (Exception e) {

                }
            }
            try{
                if(img_chat_avatar.getDrawable()==null){

                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2,Util.getBitmapOptions());
                    ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                    img_chat_avatar.setImageDrawable(mSenderImage);

                }
            }catch (Exception e){

            }

            // end

            if (Constant.mselfdestruct == true) {

                if (IS_OWNER == 0) {
                    try {
                        if (sp.contains(jid + "_self_desc_time")) {
                            selected_self_desc_index = sp.getInt(jid
                                    + "_self_desc_time", 0);
                            selected_self_desc_time = self_desc_time[selected_self_desc_index];
                        } else {
                            ed.putInt(jid + "_self_desc_time", 0);
                            ed.commit();
                            selected_self_desc_time = self_desc_time[0];
                            selected_self_desc_index = 0;
                        }
                    } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                        // TODO: handle exception
                    }
                } else {
                    try {
                        selected_self_desc_time = sp.getInt(
                                jid + "_self_desc_time", 0);

                    } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                        // TODO: handle exception
                    }
                }

            }

            // Log.d("Chat", "SelfDestructionTime::" + selected_self_desc_time);

            Log.d("Is New Available",
                    "Is New Available::" + KachingMeApplication.getIsNetAvailable());

          /*gridview_emo.setAdapter(new SmileysAdapter(new Emoji(this)
                  .getArrayList(), mParentActivity, new Emoji(this).getEmoticons()));*/


            myFadeInAnimation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub

                    item.getActionView().clearAnimation();
                    item.setActionView(null);

                }
            });

         /* zbtn.setOnClickListener(new OnClickListener() {

              @Override
              public void onClick(View v) {
                  Constant.zzleFromgroup = false;

                  if (edt_msg.getText().toString().equalsIgnoreCase("")) {
                      Toast.makeText(getApplicationContext(),
                              "Please Enter some text", Toast.LENGTH_SHORT)
                              .show();
                  } else {
                      Constant.mChatText = edt_msg.getText().toString();
                      Intent intent = new Intent(mParentActivity, ZzleActivity.class);
                      startActivity(intent);
                  }

              }
          });*/

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

                    downArrowClickAction();
                    topMenuHideFunction();
                }
            });

            mChatFooterSlideMenuImg.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    downArrowClickAction();
                    topMenuHideFunction();
                }
            });


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


                        int currentapiVersion = Build.VERSION.SDK_INT;
                        if (currentapiVersion >= Build.VERSION_CODES.HONEYCOMB) {
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

                        Toast.makeText(mParentActivity, "Copied to clipboard", Toast.LENGTH_SHORT).show();

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

            mChatHedderAttachmentLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {


                    // This code to check for selcting msgs and deleting them...
                    if (mIsLogClick) {
                        mDeleteAlert = new AlertDialog.Builder(mParentActivity);
                        mDeleteAlert.setMessage("Are you sure you want to delete ?")
                                .setCancelable(false);
                        mDeleteAlert.setPositiveButton(getResources().getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        mcheck = true;
                                    }
                                });
                        mDeleteAlert.setNegativeButton(getResources().getString(R.string.Ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        mcheck = true;

                                        if (mOnLongSelectedPostions != null) {
                                            for (int i = (mOnLongSelectedPostions.size() - 1); i >= 0; i--) {
                                                MessageGetSet selectedItem = new MessageGetSet();
                                                selectedItem.set_id(msg_list.get(mOnLongSelectedPostions.get(i)).get_id());

                                                // adapter.remove(selectedItem);
                                                Constant.printMsg("delete id:::::"
                                                        + selectedItem.get_id());
                                                dbAdapter.setDeleteMessages_by_msgid(""
                                                        + selectedItem.get_id());

                                            }



                                            int msg_id = dbAdapter.getLastMsgid_chat(jid, sec);
                                            Constant.printMsg("id::::::::::::" + msg_id);
                                            if (dbAdapter.isExistinChatList_chat(jid, sec)) {
                                                dbAdapter.setUpdateChat_lits_chat(jid, msg_id, sec);
                                            } else {
                                                dbAdapter.setInsertChat_list_chat(jid, msg_id, sec);
                                            }

                                            if(msg_id==0)
                                            updateForHomeScreenList();

                                            //
                                            Intent iDest = new Intent(ChatTest.this, DestructService.class);
                                            stopService(iDest);

                                            isDeletEl = true;
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
                                        }
                                    }
                                });


                        AlertDialog alert = mDeleteAlert.create();
                        alert.show();


                        return;
                    }

                    if (mMenuVisible == false) {
                        Constant.printMsg("siva check seleted or not false.............." + mMenuVisible);
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
                        Constant.printMsg("siva check seleted or not true closed.............." + mMenuVisible);
                        mChatHedderAttachmentLayout
                                .setBackgroundColor(Color
                                        .parseColor("#00000000"));
                        mMenuVisible = false;
                        mAttachmentIconMenuPopup();
                        attachmentOptionMenuClickListener();
                        mToolTipImg.setVisibility(View.GONE);

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
                        //    mode.finish();
                        Intent intent = new Intent(mParentActivity, SliderTesting.class);
                        Log.i("Forword", "Message IDS " + msg_ids);
                        intent.putExtra("msg_ids", msg_ids);

                        startActivity(intent);
                        return;
                    }


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
            });


            btn_send.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        ArrayList<String> blocked = KachingMeApplication.getBlocked_user();
                        Constant.printMsg("blocked test msg send   " + blocked.contains(jid));

                        if (blocked.contains(jid)) {
                            callBlockedUserAlert();
                        } else {
                            mSendMsg();
                            mFinalNyms = new ArrayList();
                            mFinalNymsMeaning = new ArrayList();
                            mFinalNymsMeaningLength = new ArrayList();
                            editNo = 0;
                            topMenuHideFunction();
                        }
                    } catch (Exception e) {

                    }
                }

            });

            // edt_msg

            //
         /* zbbtn.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View v) {
                  // TODO Auto-generated method stub

                  menuclick = false;

                  if (!edt_msg.getText().toString().trim().equals("")) {

                      Constant.totalzzle = Count + Constant.totalzzle;

                      menuclick = true;
                      Constant.bux = sharedPrefs.getLong("buxvalue", 0);

                      Long buxval = Constant.bux + Constant.zzlepoints;
                      Constant.bux = buxval;

                      Editor e = sharedPrefs.edit();
                      e.putLong("buxvalue", buxval);
                      e.commit();

                      sendMessage("<z>" + edt_msg.getText().toString());
                  } else {
                      // Alert();

                  }
              }
          });*/

            // edt_msg.setInputType(InputType.TYPE_NULL|InputType.);
             edt_msg.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //                mEditTextLay.setBackgroundResource(R.drawable.roundcorner);

                    topMenuHideFunction();
                    // mDownArrow.setImageResource(R.drawable.rt_arrow);

                    v.setFocusable(true);
                    v.setFocusableInTouchMode(true);

                    if(mPopup){
                        downArrowClickAction();
                    }
                    if (emoji_frag.getVisibility() == View.VISIBLE) {
                        emoji_frag.setVisibility(View.GONE);
                        btn_emo.setImageResource(R.drawable.emoji_btn_normal);

                    }
                //    mPopup = false;
                    //mEditTextLay.setBackgroundResource(R.drawable.roundcorner);

                    if (is_enter_is_send) {

                        edt_msg.setInputType(1);

                    } else {

                        edt_msg.setInputType(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

                    }
                    edt_msg.onTouchEvent(event); // call native handler

                    return true; // consume touch even
                }
            });
            // edt_msg.setOnKeyListener(new OnKeyListener() {
            // public boolean onKey(View v, int keyCode, KeyEvent event) {
            // if (event.getAction() == KeyEvent.ACTION_DOWN) {
            // switch (keyCode) {
            // case KeyEvent.KEYCODE_ENTER:
            //
            // mSendMsg();
            //
            // return true;
            // default:
            // break;
            // }
            // }
            // return true;
            // }
            // });

            btn_emo.setImageResource(R.drawable.emoji_btn_normal);
            btn_emo.setOnClickListener(new OnClickListener() {

                private int counter;

                @Override
                public void onClick(View v) {

                    topMenuHideFunction();
                    // mDownArrow.setImageResource(R.drawable.rt_arrow);
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
                        // btn_emo.setImageResource(R.drawable.smiling36);
                        btn_emo.setImageResource(R.drawable.ic_action_hardware_keyboard);

                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(edt_msg.getWindowToken(),
                                        0);
                        // btn_emo.set
                    }
                }
            });

            edt_msg.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // TODO Auto-generated method stub
                    Constant.printMsg("Selected word is del :"
                            + mFinalNyms + "  "+selectedWord_back);

                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        Constant.printMsg("Selected word is del :"
                                + mFinalNyms + "  "+selectedWord_back);
                        if (keyCode == KeyEvent.KEYCODE_DEL) {

//                            if(selectedWord_backTemp.length()>0){
//                                selectedWord_back=selectedWord_backTemp;
//                                selectedWord_backTemp="";
//                            }
//
//                            if (selectedWord_back != "") {
//
//                                if (mFinalNyms.contains(selectedWord_back)) {
//
//                                    int pos1 = edt_msg.getText().toString()
//                                            .indexOf(selectedWord_back.trim());
//                                    int pos2 = pos1 + selectedWord_back.length();
//
//                                    edt_msg.getText().delete(pos1, pos2);
//
//                                }
//                                selectedWord_back = "";
//
//                                // String edtText = edt_msg.getText().toString();
//                                // edt_msg.setText(edtText.replace(selectedWord_back,""));
//                            }
                        }
                        if (is_enter_is_send)
                        {
                            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == EditorInfo.IME_ACTION_DONE)
                            {
                                try
                                {
                                    ArrayList<String> blocked = KachingMeApplication.getBlocked_user();

                                    Constant.printMsg("blocked test msg send   " + jid);

                                    if (blocked.contains(jid))
                                    {
                                        callBlockedUserAlert();
                                    }
                                    else
                                    {
                                        mSendMsg();
                                        mFinalNyms = new ArrayList();
                                        mFinalNymsMeaning = new ArrayList();
                                        mFinalNymsMeaningLength = new ArrayList();
                                        editNo = 0;
                                        topMenuHideFunction();
                                    }
                                }
                                catch (Exception e)
                                {

                                }
                            }
                        }
                        else
                        {

                        }
                    }
                    return false;
                }
            });

            edt_msg.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    Constant.userStatus = Constant.TYPING_STATUS;

                    Constant.printMsg("KUUUU" + s + "    " + start + "   "
                            + before + "    " + count);
                    if(TempConnectionService.connection!=null && TempConnectionService.connection.isAuthenticated()) {
                        Roster roster = Roster
                                .getInstanceFor(TempConnectionService.connection);
                        Presence presence = roster.getPresence(jid);
                        isJidOnline=presence.isAvailable();

                    }
                    if (isJidOnline) {
                        if (s.length() > 0
                                && Constant.USER_ONLINE_CHEKING_FOR_SEND_PRESENCE == true) {
                            Constant.printMsg("BBBBBB " + IS_TYPED + " "
                                    + s.length());
                            IS_TYPED = false;

                        }
                    }

                    // meaningList.clear(); // commenetd Chinmay

                    String txt = edt_msg.getText().toString();

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

                    for (int i = 0; i < words1.length; i += ChatTest.AUDIO_ACTIVITY_REQUEST_CODE) {
                        mParentActivity.meaningList.clear();
                        for (int j = 0; j < ChatDictionary.mDictionaryList.size(); j += ChatTest.AUDIO_ACTIVITY_REQUEST_CODE) {
                            Constant.printMsg("jkgad"+words1[i]);
                            if (words1[i]
                                    .equalsIgnoreCase(ChatDictionary.mDictionaryList
                                            .get(j).toString()) && !(txt.charAt(txt.length()-1)==' ')) {
                                String finalstring = ChatDictionary.mDictionaryMeaningList
                                        .get(j).toString();
                                String[] splMeaning = finalstring.split(",");
                                if (splMeaning.length > ChatTest.AUDIO_ACTIVITY_REQUEST_CODE) {
                                    for (int m = 0; m < splMeaning.length; m += ChatTest.AUDIO_ACTIVITY_REQUEST_CODE) {
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
                                                .size(); j += ChatTest.AUDIO_ACTIVITY_REQUEST_CODE) {
                                            mng = itemValue.toLowerCase();
                                            chk = ChatDictionary.mDictionaryMeaningList
                                                    .get(j).toString()
                                                    .toLowerCase();
                                            Constant.printMsg("nymn test ::::\t:: "
                                                    + mng + "   " + chk);
                                            if (mng.equalsIgnoreCase(chk)) {
                                                ChatTest access$0 = mParentActivity;
                                                access$0.nymcount += ChatTest.AUDIO_ACTIVITY_REQUEST_CODE;
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

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                      /* //Constant.printMsg("Before text changes::"+jid); */
                      /*
                       * if (is_available) { MessageEventManager messageeventmanager =
                       * mBoundService .getMessageEventManager();
                       * messageeventmanager.sendComposingNotification(jid, "12"); }
                       */
                    // sendtyping = new SendTyping();
                    // if (IS_TYPED) {
                    // IS_TYPED = false;
                    // Constant.userStatus = Constant.TYPING_STATUS;
                    // // sendtyping.execute();
                    // }

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Constant.printMsg("Selected word is backaaaa: "
                            + selectedWord_back + "  ");
                    if (s.length() == 0) {
                        if(TempConnectionService.connection!=null && TempConnectionService.connection.isAuthenticated()) {
                            Roster roster = Roster
                                    .getInstanceFor(TempConnectionService.connection);
                            Presence presence = roster.getPresence(jid);
                            isJidOnline=presence.isAvailable();

                        }
                        if (isJidOnline) {
                            presence_typing(Constant.TYPING_STRING);
                        }
                        is_text = false;
                        btn_send.setImageDrawable(getResources().getDrawable(
                                R.drawable.ic_action_device_access_mic));

                        // nymsCount = 0;
                        Constant.userStatus = Constant.ONLINE_STATUS;

                        selectedtext.clear();

                    } else {
                        is_text = true;
                        btn_send.setImageDrawable(getResources().getDrawable(
                                R.drawable.submit_btn));
                    }

                    int startSelection = edt_msg.getSelectionStart();

                    int length = 0;
                    String[] currentWord = edt_msg.getText().toString()
                            .split(" ");
                    for (int i=0;i<currentWord.length;i++) {
                         length = length + currentWord[i].length() + 1;

                        if (length > startSelection) {
                            selectedWord_back = currentWord[i];

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

//                    if(selectedWord_backTemp.length()>0){
//                        Constant.printMsg("Selected word is back:11 "
//                                + selectedWord_back + "  ");
//                        selectedWord_back=selectedWord_backTemp;
////                        selectedWord_backTemp="";
//                    }


                }

            });

        /*  gridview_emo.setOnItemClickListener(new OnItemClickListener() {

              @Override
              public void onItemClick(AdapterView<?> parent, View view,
                                      int i, long id) {

                  String value = gridview_emo.getAdapter().getItem(i)
                          .toString();
                  value = edt_msg.getText() + value;

                  // edt_msg.setText(new Emoji(mParentActivity).getSmiledText(value));
              }
          });
  */



         /* btn_title.setOnClickListener(new OnClickListener() {

              @Override
              public void onClick(View v) {
                  // TODO Auto-generated method stub
                  Intent intent = new Intent(mParentActivity, UserProfile.class);
                  intent.putExtra("jid", jid);
                  intent.putExtra("name", home_title);
                  startActivity(intent);
              }
          });*/
            mChatHedderTextLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(mParentActivity, UserProfile.class);
                    intent.putExtra("jid", jid);
                    intent.putExtra("name", home_title);
                    startActivity(intent);
                }
            });
            mChatHedderProfileImg.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(mParentActivity, UserProfile.class);
                    intent.putExtra("jid", jid);
                    intent.putExtra("name", home_title);
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
                    /*Intent intent = new Intent(mParentActivity, SliderTesting.class);
                    startActivity(intent);
                    finish();*/
                }
            });

            handler.postDelayed(AsyncConnection, 0);
            // handler.post(sendLastSeenData);

            mBlockContact.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    block_or_unblock();

                }
            });

            mAddContact.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    ContactsGetSet contact = dbAdapter.getContact_grp(jid);
                    Constant.printMsg("contact value :::: " + contact);
                    if (contact == null) {
                        contact = new ContactsGetSet();

                        contact.setJid(jid);
                        contact.setIsInContactList(1);
                        contact.setNumber(jid.split("@")[0]);
                    }


                    Intent intent = new Intent(
                            Intent.ACTION_INSERT,
                            ContactsContract.Contacts.CONTENT_URI);
                    intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, "+"
                            + contact.getNumber());
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                    intent.putExtra(
                            "finishActivityOnSaveCompleted",
                            true);
                    if (contact.getNifty_email() != null)
                        intent.putExtra(ContactsContract.Intents.Insert.EMAIL,
                                contact.getNifty_email());
                    if (contact.getNifty_name() != null) {

                    }

                    ChatTest.mParentActivity.mAddMethod(intent);
                    // intent.putExtra(Intents.Insert.NAME,
                    // result.getNifty_name());


                }
            });


            Constant.CURRENT_OPEN_JID = jid;


          /*adaptor = new ChatTest_Cursor_Adapter(mParentActivity, null, msg_list, jid, dest_list, dest_list_msgids);
          Constant.adapterTest_cursor = adaptor;
          LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
          mLayoutManager.setStackFromEnd(true);
          listview.setLayoutManager(mLayoutManager);
          listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getApplicationContext()).size(4).build());
          listview.setAdapter(adaptor);

          listview.scrollToPosition(adaptor.getItemCount() - 1);*/

            Constant.printMsg("Loader came 1");

            dbAdapter = KachingMeApplication.getDatabaseAdapter();
            this.jid = jid;
            MEDIA_UPLOAD_URL = KachingMeConfig.UPLOAD_MEDIA;
            mNetworkSharPref = new NetworkSharedPreference(mParentActivity);

            sp1 = PreferenceManager.getDefaultSharedPreferences(mParentActivity);
            ed1 = sp1.edit();
            sp = getSharedPreferences(
                    KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);

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


            final Boolean isMirrorEnabled = sp.getBoolean(jid + "_mirror", false);

            if (isMirrorEnabled) {
                mIsMirrorEnabled = true;
                mChatTypingMsgLayout.setVisibility(View.VISIBLE);
            } else {
                mIsMirrorEnabled = false;
            }


            isFirstCall = true;

            ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());





          /*if (dbAdapter.getJidIdPresentOrNot(jid) == 0) {

              isSavedContact = false;
          } else {
              if (contactExists(this,jid)){
                  isSavedContact = true;
              }else{
                  isSavedContact = false;
              }

          }*/
        } catch (Resources.NotFoundException e) {
        } catch (NumberFormatException e) {
        } catch (Exception e) {
        }

        mChatTypingTxt.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocus();
                v.setFocusableInTouchMode(true);

                mChatTypingMsgLayout.setVisibility(View.GONE);
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });



    }






    public boolean contactExists(Activity _activity, String number) {
        Constant.printMsg("siva check.....number......" + number);
        number = number.split("@")[0];
        Constant.printMsg("siva check.....after split number......" + number);

        if (number != null) {
            Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
            String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
            Cursor cur = _activity.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
            try {
                if (cur.moveToFirst()) {
                    return true;
                }
            } finally {
                if (cur != null)
                    cur.close();
            }
            return false;
        } else {
            return false;
        }
    }// contactExists

    public String getLastSeenStatus(Context mParentActivity) {
        HashMap<String, String> map = new SharedPrefPrivacy(mParentActivity)
                .getLastSeen();
        String last_seen = map.get(SharedPrefPrivacy.KEY_LAST_SEEN_GET);

        return last_seen;
    }

    protected void deleteArchive(String query) {
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

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(edt_msg, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(edt_msg);
    }

    @Override
    public void onClick(View view, int i) {
        Log.d("Chat", "List item click at::" + i);
        topMenuHideFunction();

        if (mActionMode == null) {
            //            try {
            //
            //
            //                MessageGetSet message = msg_list.get(k);
            //                if (message.getMedia_wa_type().equals("1")
            //                        || message.getMedia_wa_type().equals("2")) {
            //                    try {
            //                        File f = new File(Constant.local_image_dir
            //                                + msg_list.get(k).getMedia_name());
            //                        Uri uri = Uri.fromFile(f);
            //                        Intent intent = new Intent(
            //                                Intent.ACTION_VIEW);
            //                        String mime = "*/*";
            //                        MimeTypeMap mimeTypeMap = MimeTypeMap
            //                                .getSingleton();
            //                        if (mimeTypeMap.hasExtension(mimeTypeMap
            //                                .getFileExtensionFromUrl(uri.toString())))
            //                            mime = mimeTypeMap
            //                                    .getMimeTypeFromExtension(mimeTypeMap
            //                                            .getFileExtensionFromUrl(uri
            //                                                    .toString()));
            //                        intent.setDataAndType(uri, mime);
            //                        startActivity(intent);
            //                    } catch (Exception e) {
            //                        // TODO: handle exception
            //                    }
            //
            //
            //                } else if (message.getMedia_wa_type().equals("4")) {
            //
            //                    String urlAddress = "http://maps.google.com/maps?q="
            //                            + message.getLatitude() + ","
            //                            + message.getLongitude() + "("
            //                            + message.getData() + ")&iwloc=A&hl=es";
            //                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri
            //                            .parse(urlAddress));
            //
            //                    try {
            //                        startActivity(intent);
            //                    } catch (ActivityNotFoundException e) {
            //                        // TODO: handle exception
            //                        new AlertManager()
            //                                .showAlertDialog(
            //                                        mParentActivity,
            //                                        getResources()
            //                                                .getString(
            //                                                        R.string.no_activity_found_to_open_map_location),
            //                                        true);
            //                    }
            //                }
            //
            //            } catch (Exception ee) {
            //
            //            }
        } else
        {

        }
            // add or remove selection for current list item
//            onListItemSelect(i);


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

    @Override
    public void onLongClick(View view, int i) {
//        onListItemSelect(i);
        topMenuHideFunction();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.clear();
        MenuInflater inflater = getMenuInflater();
        this.menu = menu;

        if (KachingMeApplication.getBlocked_user().contains(jid)) {

            Log.d("PrepareOptionMenu", "User is Block");
            inflater.inflate(R.menu.chat_menu_unblock, menu);

        } else {

            Log.d("PrepareOptionMenu", "User is not Block");
            inflater.inflate(R.menu.chat_menu, menu);

        }

        if (IS_OWNER == 0 && sec == 0) {
            // menu.findItem(R.id.menu_self_des).setVisible(true);

            menu.findItem(R.id.menu_self_des).setVisible(false);
        } else {
            menu.findItem(R.id.menu_self_des).setVisible(false);

        }

        if (sp.contains(jid + "_lock")) {
            menu.findItem(R.id.menu_lock_chat)
                    .setTitle(res.getString(R.string.unlock_chat))
                    .setIcon(R.drawable.ic_action_unlock);
        } else {
            menu.findItem(R.id.menu_lock_chat)
                    .setTitle(res.getString(R.string.lock_chat))
                    .setIcon(R.drawable.ic_action_lock);
        }

        if (sec == 0) {
            menu.findItem(R.id.menu_lock_chat).setVisible(false);
        }

        return true;
    }

    @Override
    public void onBackPressed() {

        try {





            for(int i=0; i<list.size(); i++)
            {
                MediaPlayer player = list.get(i);
                if(player!=null) {

                    player.stop();
                    player.release();
                }
            }





            if (mIsLogClick) {
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

            } else {
                try {
                unregisterReceiver(lastseen_event);
                handler.removeCallbacks(AsyncConnection);
                // handler.removeCallbacks(sendLastSeenData);
                if (cursor != null) {
                    cursor.close();
                }
                Constant.printMsg("Call   Stop333");
                Constant.printMsg("Call   Destory");
                Log.d("Chat",
                        "BACK IS_FROM_NOTIFICATION ::"
                                + KachingMeApplication.getIS_FROM_NOTIFICATION());
                if (emoji_frag.getVisibility() == View.VISIBLE) {
                    emoji_frag.setVisibility(View.GONE);
                    btn_emo.setImageResource(R.drawable.emoji_btn_normal);
                } else if (KachingMeApplication.getIS_FROM_NOTIFICATION()) {
                    KachingMeApplication.setIS_FROM_NOTIFICATION(false);
                    // Intent upIntent = NavUtils.getParentActivityIntent(this);
                    // // upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // TaskStackBuilder.create(this)
                    // .addNextIntentWithParentStack(upIntent).startActivities();
//                    Intent intent = new Intent(mParentActivity, SliderTesting.class);
//                    intent.putExtra("SliderIntent","SliderTesting");
//                    startActivity(intent);
//                    finish();

                } else {

//                    Intent intent = new Intent(mParentActivity, SliderTesting.class);
//                    intent.putExtra("SliderIntent","SliderTesting");
//                    startActivity(intent);
//                    finish();
                    // super.onBackPressed();
                }

                seekHandler.removeCallbacks(run);
                    if (Constant.FROM_CHAT_SCREEN.equalsIgnoreCase("notification")) {
                        Intent intent = new Intent(mParentActivity, SliderTesting.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("SliderIntent", "SliderTesting");
                        startActivity(intent);
                        finish();
                    }
                    finish();
                super.onBackPressed();

            }catch (Exception e){
                   super.onBackPressed();
                }

            }



        } catch (Exception e) {

        }

    }

    @Override
         public boolean onOptionsItemSelected(MenuItem item) {

             Intent intent;
             switch (item.getItemId()) {
                 case android.R.id.home:

                     Log.d("Chat",
                             "BACK IS_FROM_NOTIFICATION ::"
                                     + KachingMeApplication.getIS_FROM_NOTIFICATION());
                     onBackPressed();

                     break;

                 case R.id.menu_veiew_contact:
                     intent = new Intent(this, UserProfile.class);
                     intent.putExtra("jid", jid);
                     intent.putExtra("name", home_title);
                     startActivity(intent);
                     break;
                 case R.id.menu_block:
                     if (KachingMeApplication.getIsNetAvailable()) {
                         if (item.getTitle().equals(
                                 getResources().getString(R.string.block))) {
                             Log.d("Optionmenu", "Block called");
                             new Async_Privacy().execute();
                         } else {
                             Log.d("Optionmenu", "Unblock called");
                           /* new unblock().execute(); */
                             unblock();
                             Log.d("Optionmenu", "Unblock called2");
                         }
                     } else {

                         new AlertUtils().Toast_call(
                                 this,
                                 getResources().getString(
                                         R.string.no_internet_connection));
                     }
                     break;
                 case R.id.menu_capture:

                     selectImage();

                     break;

                 case R.id.audio_file:

                     // Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                     // intent2.setType("audio/*");
                     // startActivityForResult(intent2, RESULT_CODE_AUDIO);

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
                                     dbAdapter.setDeleteMessages_Non_secret_chat(jid);

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
                * case R.id.menu_karaoke:
                *
                * Intent intentka = new Intent(mParentActivity, Test.class);
                * startActivity(intentka); break;
                */
                 case R.id.menu_self_des:

                     if (KachingMeApplication.getIsNetAvailable()) {
                         Show_Self_desc_time(selected_self_desc_index);
                     } else {
                         new AlertUtils().Toast_call(
                                 this,
                                 getResources().getString(
                                         R.string.no_internet_connection));
                     }
                     break;
                 case R.id.menu_star:
                     Intent intentst = new Intent(mParentActivity, DealsActivity.class);
                     startActivity(intentst);
                     if (menu != null) {
                         MenuItem item1 = menu.findItem(R.id.menu_star);
                         Constant.printMsg("onclcik 3" + item1);
                         if (item1 != null) {
                             Constant.printMsg("onclcik 4");
                             menu.findItem(R.id.menu_star).setVisible(false);

                         }
                     }
                     break;
                 case R.id.menu_call:
                     Intent callIntent = new Intent(Intent.ACTION_CALL);
                     callIntent.setData(Uri
                             .parse("tel:+" + jid.toString().split("@")[0]));
                     startActivity(callIntent);
                     break;

                 case R.id.menu_lock_chat:
                     if (item.getTitle().toString() == res.getString(R.string.lock_chat)
                             .toString()) {
                         lock_input(txt_title.getText().toString(), jid, true);
                     } else {
                         lock_input(txt_title.getText().toString(), jid, false);
                     }
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
                 //
                 // break;

                 default:
                     break;
             }

             return super.onOptionsItemSelected(item);
         }

    void doBindService() {

        // bindService(new Intent(mParentActivity, KaChingMeService.class),
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
    protected void onRestart() {

        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
        NotificationHandler.Instance().resetCounter();

      //  ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
        super.onRestart();
    }


    @Override
    protected void onResume() {

        // new FetchChat().execute();

        Constant.isFront = true;
        IS_Front = true;

        try {

            IntentFilter filter = new IntentFilter();
            filter.addAction("lastseen_broadcast");
            filter.addAction("typing");
            filter.addAction("chat");
            filter.addAction("destruct_service");
            filter.addAction("destruct_time");
            filter.addAction("network_status");
            filter.addAction("secret_chat_local");
            filter.addAction("invisible");
            filter.addAction("update_tick");
            filter.addAction("update_left");
            filter.addAction("image_upload");
            filter.addAction("remove_subtitle");
            filter.addAction("getjid_online");
            filter.addAction("update_image_cancel");
            filter.addAction("update_audio_cancel");
            filter.addAction("update_video_cancel");

            registerReceiver(lastseen_event, filter);

            mLedText = Constant.mZzleText;
            mAttachKachingFeature();

            getSupportActionBar().hide();
            mParentActivity = this;

            Constant.userStatus = Constant.ONLINE_STATUS;
            //        doBindService();
            getJidOnlineStatus(jid);

            updateReadMessages();


        } catch (Exception e) {

            Constant.printMsg("Chat broadcast...." + e.toString());
        }
        try {
            SliderTesting.mActivity.mFinishBackgroundAcrtivity();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {

        // LastActivity activity =
        // LastActivity.getLastActivity(TempConnectionService.connection,
        // "1234567890@yourdomain");

        // Roster roster = Roster
        // .getInstanceFor(TempConnectionService.connection);
        // Presence p = roster.getPresence("919001122334@localhost");
        //
        // Constant.printMsg("CCCCCCCCCCCCCCCCCCCCC" + p.getType() + "    "
        // + p.isAway() + "    " + p.getStatus() + "      " + p.getMode()
        // + "  " + jid);

        // Presence pr = new Presence(Presence.Type.unavailable);
        // pr.setMode(Presence.Mode.available);
        // pr.setStatus("Gokul");
        //
        // // LastActivity activity = new LastActivity();
        // // activity.setLastActivity(System.currentTimeMillis());
        //
        // LastActivityManager activityman = LastActivityManager
        // .getInstanceFor(connection);
        // try {
        //
        // Constant.printMsg("CCCCCCCCCCCCCCCCCCCCC"
        // + activityman.getLastActivity(jid) + "    "
        // + lastSeenStatus());
        //
        // } catch (NoResponseException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (XMPPErrorException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (NotConnectedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        //
        // Constant.printMsg("CCCCCCCCCCCCCCCCCCCCC" + pr.isAvailable() +
        // "    "
        // + pr.isAway() + "    " + pr.getType() + "      " + pr.getMode()
        // + "  " + jid);

        // try {
        // TempConnectionService.connection.sendStanza(pr);
        // } catch (Exception e1) {
        // // TODO Auto-generated catch block
        // Constant.printMsg("CCCCCCCCCCCCCCCCCCCCC333" + e1.toString());
        // }

        // TempConnectionService.connection.disconnect();
        // Roster rosters = Roster
        // .getInstanceFor(TempConnectionService.connection);
        // Presence ps = rosters.getPresence("919001122334@localhost");
        // Constant.printMsg("CCCCCCCCCCCCCCCCCCCCC111111" + ps.isAvailable()
        // + "    " + ps.isAway() + "    " + ps.getType() + "      "
        // + ps.getMode() + "  " + jid);
        // TempConnectionService.connection.disconnect();
        // mChatCreatedListener = null;
        //        doUnbindService();

        super.onDestroy();
    }

    boolean isPaused = false;

    @Override
    protected void onPause() {

        Constant.isFront = false;
        IS_Front = false;


        try {

                for(int i=0; i<list.size(); i++)
                {
                    MediaPlayer player = list.get(i);
                    if(player!=null) {

                        if(player.isPlaying())
                        player.pause();
                    }
                }

            topMenuHideFunction();


            //handler.removeCallbacks(null);



            Constant.printMsg("PPPPPPPPPPPPPPPPPPPPPPus");
        } catch (Exception e) {

        }
        Constant.printMsg("Call   Stop111");
        super.onPause();

    }

    @Override
    protected void onStop() {

        // sendLastSeen_Status();
        IS_Front = false;
        Constant.printMsg("Call   Stop222");
        super.onStop();
    }

    public void presence_typing(final String status) {

        Thread thread;
        System.gc();
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Constant.printMsg(" BBBB INside Presence typing");


                Message msg = new Message(jid, Message.Type.chat);
                msg.setStanzaId(status);

                if (mIsMirrorEnabled)
                    if(mChatFooterEdittext.getText().toString().length()>0){
                        msg.setBody(status + "@" + mChatFooterEdittext.getText().toString());

                    }else{
                        msg.setBody(status + "@" + " ");

                    }
                else
                    msg.setBody(status);

                 /* Presence presence = new Presence(Presence.Type.available);
                  presence.setTo(jid);
                  presence.setStatus(status + "@" + mChatFooterEdittext.getText().toString());*/
                try {


                    //   TempConnectionService.messageEventManager.sendComposingNotification(jid,status + "@" + mChatFooterEdittext.getText().toString());

                    IS_TYPED = true;
                    if (TempConnectionService.connection != null) {
                        if (TempConnectionService.connection.isConnected() && TempConnectionService.connection.isAuthenticated())

                            Constant.printMsg("jflfa"+msg.toXML());

                            TempConnectionService.connection.sendStanza(msg);
                    }


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });

        thread.start();

    }

    private void sendMessage(final String stringmsg) {

        status_msg = 3;

        // Presence presencePacket = new Presence(Presence.Type.subscribe);
        // presencePacket.setTo(jid);
        // try {
        // TempConnectionService.connection.sendStanza(presencePacket);
        // } catch (NotConnectedException e1) {
        // // TODO Auto-generated catch block
        // e1.printStackTrace();
        // }

        final String msg_id = "" + new Date().getTime();
        Constant.printMsg("Donate buxs msg_id start   " + msg_id);
        MessageGetSet msggetset = new MessageGetSet();

          /*
           * new AsyncTask<Message, String, String>() {
           *
           * @Override protected String doInBackground(Message... params) { //
           * TODO Auto-generated method stub
           *
           * return null; }
           *
           * }.execute(msg);
           */

        // final Message msg1=msg;
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

                    Message msg = new Message(jid, Message.Type.chat);
                    msg.setStanzaId(msg_id);
                    // msg.setBody(stringmsg);
                    msg.setBody(Utils.EncryptMessage(stringmsg));
                    msg.addExtension(new DeliveryReceiptRequest());

                    JivePropertiesManager.addProperty(msg, "media_type", 0);

                    if (sec == 0) {

                        JivePropertiesManager.addProperty(msg, "is_sec_chat",
                                sec);
                        JivePropertiesManager.addProperty(msg,
                                "self_desc_time",
                                sp.getInt(jid + "_self_desc_time", 0));
                    }

                    if(TempConnectionService.connection!=null)
                    Constant.printMsg("FFFFFFFFFFFFFFF" + connection
                            + "        " + connection.isConnected());
                    else {
                        Constant.printMsg("FFFFFFFFFFFFFFF connection null ");

                        startService(new Intent(ChatTest.this, TempConnectionService.class));
                    }

                    // chatManager = ChatManager.getInstanceFor(connection);

                    chat = TempConnectionService.chatmanager.createChat(jid,
                            TempConnectionService.mChatCreatedListener.getMessageListener());
                    DeliveryReceiptRequest.addTo(msg);
                    dest_list.add(Constant.mselected_self_destruct_time);
                    //                    stopService(new Intent(mParentActivity,DestructService.class));
                    //                    startService(new Intent(mParentActivity,DestructService.class));


                    if (Connectivity.isOnline(mParentActivity) == true && Connectivity.isTempConnection()) {
                        // chat.sendMessage(msg);
                        status_msg = 2;
                        TempConnectionService.connection.sendStanza(msg);

                        Constant.mselected_self_destruct_time = 0;

                        Constant.printMsg("dataaa ::::::: " + status_msg + "     " + msg.toString());
                    }

                    // if (KachingMeApplication.getIsNetAvailable()) {
                    // MessageEventManager.addNotifica  tionsRequests(msg, true,
                    // true, true, true);
                    // //
                    // DeliveryReceiptManager.getInstanceFor(connection).receiptMessageFor(msg);
                    // //
                    // DeliveryReceiptManager.getInstanceFor(connection).autoAddDeliveryReceiptRequests();
                    // // drm.addDeliveryReceiptRequest(msg);
                    // DeliveryReceiptRequest.addTo(msg);
                    // TempConnectionService.connection.sendStanza(msg);
                    // Constant.printMsg("EEEEEEEEEEE sent msg" + msg);
                    //
                    // }

                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                    // TODO: handle exception
                    // Chat.this.sendBroadcast(new Intent(
                    // "action.exception.SENT_MSG"));

                    Constant.printMsg("EEEEEEEEEEE Chat msg send excp"
                            + e.toString());
                }
            }
        };

        thread.start();
        Constant.printMsg("dataaa ::::::: " + stringmsg + "  " + "" + msg_id
                + "    " + jid);


        try {

            if (Connectivity.isOnline(mParentActivity) == true && Connectivity.isTempConnection()) {
                status_msg = 2;
            }
            msggetset.setData(stringmsg);
            msggetset.setKey_from_me(0);
            msggetset.setKey_id("" + msg_id);
            msggetset.setKey_remote_jid(jid);
            msggetset.setNeeds_push(1);
            msggetset.setSend_timestamp(new Date().getTime());
            msggetset.setStatus(status_msg);
            msggetset.setTimestamp(new Date().getTime());
            msggetset.setMedia_wa_type("0");
            msggetset.setIs_sec_chat(sec);
            msggetset.setSelf_des_time(sp.getInt(jid + "_self_desc_time", 0));
            msggetset.setIs_owner(IS_OWNER);
            msggetset.setPostion(msg_list.size());
Constant.printMsg("DDDDDDSJSJJSJS"+msg_list.size());
            Constant.printMsg("DEEEEESSSSSTTTTT2 "+msg_list.size());

//            if (adaptor != null) {
//
//
//
//                listview.scrollToPosition(adaptor.getItemCount() - 1);
//            }

            long id_insert = dbAdapter.setInsertMessages(msggetset);
            msggetset.set_id((int)id_insert);

            msg_list.add(msggetset);
            mPositionKey.add(msggetset.getKey_id());
            dest_list_msgids.add(id_insert);
            dest_list_bombids.add(R.drawable.black_bomb);
            dest_list_anim.add(0);


            initializeListElememts(1);
            rightTextChat();
            setRightChatText();
            k = k + 1;

            mRightTipLayout.requestFocus();

            //                    if(Constant.mselected_self_destruct_time!=0)
            //                    {
            //                        database_destList.add(new DestructGetter(stringmsg, id_insert));
            //
            //
            //                    }

            edt_msg.setText("");
            edt_msg.requestFocus();


            // new FetchChat().execute();

            int l = dbAdapter.getLastMsgid_chat(jid, sec);
            Constant.printMsg("looooollll   " + l);
            Constant.printMsg("Donate buxs msg_id insert   " + l);

            if (dbAdapter.isExistinChatList_chat(jid, sec)) {
                dbAdapter.setUpdateChat_lits_chat(jid, l, sec);
            } else {
                dbAdapter.setInsertChat_list_chat(jid, l, sec);
            }
//            if (mDest == true) {
//                mDest = false;
//                if (dbAdapter.isExistinChatList_chat(jid, sec)) {
//                    dbAdapter.setUpdateChat_lits_chat(jid, l-1, sec);
//                } else {
//                    dbAdapter.setInsertChat_list_chat(jid, l-1, sec);
//                }
//            }else {
//
//
//                if (dbAdapter.isExistinChatList_chat(jid, sec)) {
//                    dbAdapter.setUpdateChat_lits_chat(jid, l, sec);
//                } else {
//                    dbAdapter.setInsertChat_list_chat(jid, l, sec);
//                }
//            }
        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
        }


//        Intent login_broadcast;
//        login_broadcast = new Intent("chat");
//        login_broadcast.putExtra("jid", "" + jid);
//        sendBroadcast(login_broadcast);
        //
        //        login_broadcast = new Intent("chat_1");
        //        login_broadcast.putExtra("jid", "" + jid);
        //        sendBroadcast(login_broadcast);

        // Get size in double to update media network usage

        //        adaptor.notifyDataSetChanged();

        try {
            Constant.printMsg("Double msg:" + stringmsg);
            // String sizeval = createStringDataSize(stringmsg.length());
            long lng = stringmsg.getBytes().length;
            // bite = Double.parseDouble(format);
            updateNetwork_Message_Sent(lng);

        } catch (Exception e) {

        }
    }

    private void updateCursor() {
        cursor = dbAdapter.getMessagesCursor_Chat(jid, sec);

         /* msg_list.clear();
          try {
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

              Constant.msg_list_adapter = msg_list;
          } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
              // TODO: handle exception
          }*/

    }

    public void updateReadMessages() {

        final Set mPendingDeliveryAckMessege = new HashSet();

        final Set mFinalPendingDeliveryAckMessege = new HashSet();

        final SharedPreferences myPrefs = this.getSharedPreferences(
                "pending_msg_ack", MODE_PRIVATE);

        mFinalPendingDeliveryAckMessege.add(myPrefs.getStringSet(
                "pending_delivery_msg", null));

        try {

            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    ArrayList<MessageGetSet> unreadMessageList = new ArrayList<MessageGetSet>();
                    unreadMessageList = dbAdapter.getUnreadMessages(jid);
                    Constant.printMsg("Unread Message Called...::" +
                            unreadMessageList.size());


                    for (final MessageGetSet messageGetSet : unreadMessageList) {

                        dbAdapter.setUpdateMessage_display(jid,
                                messageGetSet.getKey_id());

                        final Message ack = new Message(jid,
                                Message.Type.chat);

                        if (TempConnectionService.connection != null) {
                            if (TempConnectionService.connection.isConnected()) {

                                ack.setSubject(Constant.STATUS_DISPLAYED);
                                ack.addExtension(new DeliveryReceipt(messageGetSet
                                        .getKey_id()));
                                try {
                                    Constant.printMsg("GGGGGGGGGGGGGGGSIU"
                                            + messageGetSet.getKey_id());

                                    TempConnectionService.connection
                                            .sendStanza(ack);
                                } catch (NotConnectedException e) {
                                    e.printStackTrace();
                                }
                            } else {

                                mPendingDeliveryAckMessege.add(messageGetSet
                                        .getKey_id() + "#" + jid);

                            }
                        } else {
                            mPendingDeliveryAckMessege.add(messageGetSet
                                    .getKey_id() + "#" + jid);
                        }

                        // StatusListenerMethods.sendReceipt(jid,
                        // messageGetSet.getKey_id(),
                        // Constant.STATUS_DISPLAYED);

                    }

                    mFinalPendingDeliveryAckMessege
                            .addAll(mPendingDeliveryAckMessege);
                    Constant.printMsg("AAAAAAAAAAAAAAAAAA!!!!"
                            + mPendingDeliveryAckMessege);
                    Constant.printMsg("AAAAAAAAAAAAAAAAAASSSSSSSSSSS!!!!"
                            + mFinalPendingDeliveryAckMessege);
                    Editor prefsEditor = myPrefs.edit();
                    prefsEditor.putStringSet("pending_delivery_msg",
                            mFinalPendingDeliveryAckMessege);
                    prefsEditor.commit();

                }
            });
            t.start();

        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
        }
        dbAdapter.setUpdate_unseen_message_count(jid, sec);

          /*
           * Intent login_broadcast=new Intent("chat");
           * login_broadcast.putExtra("jid",""+jid);
           * getApplicationContext().sendBroadcast(login_broadcast);
           */

    }

    public void unblock() {
        pDialog.show();
        try {

            ArrayList<String> list = new ArrayList<String>();

            PrivacyListManager mngr = PrivacyListManager
                    .getInstanceFor(connection);

            List<PrivacyItem> privacy_items = null;
            try {
                privacy_items = mngr.getPrivacyList(
                        KachingMeApplication.getUserID()).getItems();
            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                privacy_items = new ArrayList<PrivacyItem>();
                // TODO: handle exception
            }
            ArrayList<PrivacyItem> privacy_items_updated = new ArrayList<PrivacyItem>();
            for (PrivacyItem privacyItem : privacy_items) {
                Log.d("Privacy List", "List::" + privacyItem.getValue());

                if (!jid.equals(privacyItem.getValue())) {
                    list.add(privacyItem.getValue());
                    privacy_items_updated.add(privacyItem);
                }

            }

            String blocked = "";

            try {
                for (int i = 0; i < list.size(); i++) {

                    if (i == 0) {
                        blocked = list.get(i);
                    } else {
                        blocked += "," + list.get(i);
                    }
                }

            } catch (Exception e) {

            }

            String mRemainingJid="";
            String mFinalBlockedUser=null;
            mFinalBlockedUser = sp.getString(Constant.BLOCKED_USERS,"");
            String[] parts = mFinalBlockedUser.split("-");

            ArrayList mBlockedList=new ArrayList();
            for(int j=0;j<parts.length;j++){

                if(!parts[j].trim().equalsIgnoreCase(jid)) {
                    mBlockedList.add(parts[j]);
                    mRemainingJid+=parts[j].trim();
                }
            }



            ed.putString(Constant.BLOCKED_USERS, mRemainingJid);
            ed.commit();

            KachingMeApplication.setBlocked_user(mBlockedList);

            mngr.updatePrivacyList(KachingMeApplication.getUserID(),
                    privacy_items_updated);

            Log.d("Privacy List", "List unblocked..");
            supportInvalidateOptionsMenu();
            UserBlocked = false;
            if (tvBlock != null) {
                if (UserBlocked == true) {
                    tvBlock.setText(getResources().getString(R.string.unblock));
                    mBlockContact.setText(getResources().getString(R.string.unblock));
                }
                else {
                    tvBlock.setText(getResources().getString(R.string.block));
                    mBlockContact.setText(getResources().getString(R.string.block));
                }
            } if (UserBlocked == true) {
                mBlockContact.setText(getResources().getString(R.string.unblock));
                txt_sub_title.setVisibility(View.GONE);
            }
            else {
                mBlockContact.setText(getResources().getString(R.string.block));
                txt_sub_title.setVisibility(View.VISIBLE);
            }

            if (mChatOptionMenuBlockTxt != null) {
                mChatOptionMenuBlockTxt.setText(getResources().getString(R.string.block));
            }



        } catch (XMPPException e) {// ACRA.getErrorReporter().handleException(e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoResponseException e) {// ACRA.getErrorReporter().handleException(e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotConnectedException e) {// ACRA.getErrorReporter().handleException(e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        pDialog.hide();
    }

    public void image_picker(int result) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        try {
            if (result == 1) {
                // photoPickerIntent.setType("image/*");
                // photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,
                // true);
                // startActivityForResult(photoPickerIntent, RESULT_CODE_IMAGE);

                /*Intent intent = new Intent(mParentActivity,
                        CustomPhotoGalleryActivity.class);

                startActivityForResult(intent, RESULT_CODE_IMAGE_MULTIPLE);*/

                Intent intent = new Intent(ChatTest.this, AlbumSelectActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
                startActivityForResult(intent, Constants.REQUEST_CODE);

            } else if (result == 2) {
                photoPickerIntent.setType("video/*");
                startActivityForResult(photoPickerIntent, RESULT_CODE_VIDEO);

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

                isFirstCall = false;

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
          /*
           * RESULT_CODE_IMAGE = 11, RESULT_CODE_VIDEO = 22, RESULT_CODE_AUDIO =
           * 33, RESULT_CODE_LOCATION = 44, RESULT_CODE_CONTACT = 55,
           * RESULT_CODE_FILE = 77,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE =
           * 99,REQUEST_CODE_OPENER = 88
           */
            case RESULT_OK:

                if (resultCode == RESULT_OK) {

                    Intent data = new Intent();
                    output = data.getStringExtra("outputkaraoke");
                    upload_audio_File(output);

                }
                break;

            case RESULT_CODE_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    final String filePath = cursor.getString(columnIndex);

                    isFirstCall = false;

                    cursor.close();
                    File file = new File(filePath);
                    long length = file.length();

                    length = length / 1024;
                    if (length > 16384) {
                        new AlertManager().showAlertDialog(this, getResources()
                                        .getString(R.string.imagesize_must_be_smaller),
                                true);
                    } else {
                        new Thread(new Runnable() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                    }
                                });

                                uploadFile(filePath, true);

                            }
                        }).start();

                        // new Handler().postDelayed(new Runnable() {
                        // public void run() {
                        //
                        // uploadFile(filePath, true);
                        // }
                        //
                        // }, count * 8);
                    }
                }
                break;
            case RESULT_CODE_IMAGE_MULTIPLE:

                if (resultCode == RESULT_OK) {

                    imagesPathList = new ArrayList<String>();

                    isFirstCall = false;

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
                                public synchronized void run() {
                                    // TODO Auto-generated method stub

                                    try {
                                        for (int i = 0; i < Constant.mSelectedImage
                                                .size(); i++) {
                                            Constant.printMsg("IIIIIIIII");
                                            Constant.printMsg("test path sender::::::::::::: " + Constant.mSelectedImage
                                                    .get(i) + "   " + i);

                                            synchronized (this) {
                                                uploadFile(
                                                        String.valueOf(Constant.mSelectedImage
                                                                .get(i)), true);
                                            }


                                        //    k = k + 1;

//                                            Thread.sleep(1000);

                                        }

                                    } catch (Exception e) {
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
                break;
            case RESULT_CODE_VIDEO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage1 = imageReturnedIntent.getData();
                    String[] filePathColumn1 = {MediaStore.Images.Media.DATA};
                    Cursor cursor1 = getContentResolver().query(selectedImage1,
                            filePathColumn1, null, null, null);
                    cursor1.moveToFirst();

                    int columnIndex1 = cursor1.getColumnIndex(filePathColumn1[0]);
                    final String filePath1 = cursor1.getString(columnIndex1);

                    cursor1.close();
                    File file = new File(filePath1);
                    long length = file.length();

                    length = length / 1024;
                    if (length > 16384) {
                        new AlertManager().showAlertDialog(this, getResources()
                                        .getString(R.string.videosize_must_be_smaller),
                                true);
                    } else {
                        new Thread(new Runnable() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                    }
                                });

                                Constant.printMsg("video file path::" + filePath1);

                                uploadFile(filePath1, false);

                            }
                        }).start();
                    }
                }
                break;

            case RESULT_CODE_AUDIO:
                if (resultCode == RESULT_OK) {

                    Uri audioUri = imageReturnedIntent.getData();
                    String[] filePathColumn1 = {MediaStore.Images.Media.DATA};
                    Cursor cursor1 = getContentResolver().query(audioUri,
                            filePathColumn1, null, null, null);
                    cursor1.moveToFirst();

                    int columnIndex1 = cursor1.getColumnIndex(filePathColumn1[0]);
                    final String filePath1 = cursor1.getString(columnIndex1);

                    cursor1.close();

                    outputFile = Constant.local_audio_dir
                            + System.currentTimeMillis() + ".amr";

                    Constant.printMsg("audio file path::" + filePath1);

                    File f1 = new File(filePath1);
                    File f2 = new File(outputFile);

                    try {

                        copyDirectoryOneLocationToAnotherLocation(f1, f2);

                    } catch (IOException e) {

                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

                break;
            case RESULT_CODE_LOCATION:
                if (resultCode == RESULT_OK) {
                    Log.d("Result 44", "Result 44 ok....");
                    if (imageReturnedIntent.getExtras() != null) {

                        Log.d("MAP",
                                "Lat::" + imageReturnedIntent.getStringExtra("lat"));

                        Log.d("MAP",
                                "lon::" + imageReturnedIntent.getStringExtra("lon"));

                        try {
                            Log.d("MAP",
                                    "bytesize::"
                                            + imageReturnedIntent
                                            .getByteArrayExtra("map_thumb").length);

                            // Get size in bytes to update media network usage
                            long size = imageReturnedIntent
                                    .getByteArrayExtra("map_thumb").length;
                            updateMediaNetwork(size);
                        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                            // TODO: handle exception
                        }

                        send_Location(imageReturnedIntent.getStringExtra("lat"),
                                imageReturnedIntent.getStringExtra("lon"),
                                imageReturnedIntent.getByteArrayExtra("map_thumb"),
                                imageReturnedIntent.getStringExtra("place"));
                    }
                }
                break;
            case RESULT_CODE_CONTACT:
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = imageReturnedIntent.getData();
                    Cursor phones = this.getContentResolver().query(contactData,
                            null, null, null, null);
                    phones.moveToFirst();

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


                    String lookupKey = phones.getString(phones
                            .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    Constant.printMsg("in contactsssss ::em,aill before "  );
                    String id = contactData.getLastPathSegment();
                    Constant.printMsg("in contactsssss ::em,aill before" + lookupKey);
                    cursor = getContentResolver().query(Email.CONTENT_URI, null,
                            Email.CONTACT_ID + "=?", new String[]{id}, null);

                    if (cursor.moveToFirst()) {
                        Constant.printMsg("in contactsssss ::em,ail inside loop");
                        int emailIdx = cursor.getColumnIndex(Email.DATA);
                        email = cursor.getString(emailIdx);
                        Log.v("emaillll", "Got email: " + email);
                    }

                    Uri uri = Uri.withAppendedPath(
                            ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
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
                        intent.putExtra("email_id", email);
                        startActivityForResult(intent, 66);
                        Log.d("Vcard", vCard);

                        // Get size in bytes to update media network usage
                        long size = (long) fd.getDeclaredLength();
                        updateMediaNetwork(size);
                    } catch (Exception e1) {// ACRA.getErrorReporter().handleException(e1);

                        e1.printStackTrace();

                    }

                }{

                try {
                    Uri contactData = imageReturnedIntent.getData();
                    Cursor phones = this.getContentResolver().query(contactData,
                            null, null, null, null);
                    phones.moveToFirst();

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


                    String lookupKey = phones.getString(phones
                            .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    Constant.printMsg("in contactsssss ::em,aill before "  );
                    String id = contactData.getLastPathSegment();
                    Constant.printMsg("in contactsssss ::em,aill before" + lookupKey);
                    cursor = getContentResolver().query(Email.CONTENT_URI, null,
                            Email.CONTACT_ID + "=?", new String[]{id}, null);

                    if (cursor.moveToFirst()) {
                        Constant.printMsg("in contactsssss ::em,ail inside loop");
                        int emailIdx = cursor.getColumnIndex(Email.DATA);
                        email = cursor.getString(emailIdx);
                        Log.v("emaillll", "Got email: " + email);
                    }

                    Uri uri = Uri.withAppendedPath(
                            ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
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
                        intent.putExtra("email_id", email);
                        startActivityForResult(intent, 66);
                        Log.d("Vcard", vCard);

                        // Get size in bytes to update media network usage
                        long size = (long) fd.getDeclaredLength();
                        updateMediaNetwork(size);
                    } catch (Exception e1) {// ACRA.getErrorReporter().handleException(e1);

                        e1.printStackTrace();

                    }
                } catch (Exception e) {

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

                    }
                    String vcard = imageReturnedIntent.getStringExtra("vcard");
                    String name = imageReturnedIntent.getStringExtra("name");
                    send_Contact(avatar, vcard, name);
                    Constant.printMsg("in contactsssss ::66" + avatar + vcard
                            + name);
                    Log.d("Chat", "Send Contact Method called::" + vcard);
                }
                break;
            case RESULT_CODE_FILE:

                if (resultCode == Activity.RESULT_OK) {

                    String selected_file = imageReturnedIntent
                            .getStringExtra(FileDialog.RESULT_PATH);
                    Log.d("Chat", "Selected File::" + selected_file);
                    Constant.printMsg("selected file:::>>>>>>" + selected_file);
                    File file = new File(selected_file);

                    double megabytes = ((file.length() / 1024) / 1024);
                    Log.d("Chat", "File Size::" + megabytes);
                    // File file = new File(selected_file);
                    // long length = file.length();
                    // Constant.printMsg("length file::::::>>>>>" + length);
                    // length = length / 1024;
                    Constant.printMsg("length file::::::>>>>>" + megabytes);
                    // if (!selected_file.contains(".mp3")) {
                    if (megabytes > 16) {

                        new AlertManager()
                                .showAlertDialog(
                                        this,
                                        getResources().getString(
                                                R.string.filesize_must_be_smaller),
                                        true);

                    } else {
                        try {
                            Utils.Copyfile(file, new File(Constant.local_files_dir
                                    + file.getName()));
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        // SEND_FILE(selected_file);
                        upload_audio_File(selected_file);
                    }
                    // } else {
                    // Toast.makeText(getApplicationContext(), "Invalid File",
                    // Toast.LENGTH_LONG).show();
                    // }

                }

                break;
            case REQUEST_CODE_OPENER:
                if (requestCode == REQUEST_CODE_OPENER && resultCode == RESULT_OK) {
                    mSelectedFileDriveId = (DriveId) imageReturnedIntent
                            .getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                    // showMessage("File ID::"+mSelectedFileDriveId);
                    // new
                    // RetrieveDriveFileContentsAsyncTask(mParentActivity).execute(mSelectedFileDriveId);
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
                    long length = file.length();

                    length = length / 1024;
                    if (length > 16384) {
                        // if (file.length() > 26214400) {
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
                    long length = file.length();

                    length = length / 1024;
                    Constant.printMsg("video sizeee::::::: >>>>>" + length);
                    if (length > 16384) {
                        new AlertManager().showAlertDialog(this, getResources()
                                        .getString(R.string.videosize_must_be_smaller),
                                true);
                    } else {

                        uploadFile(fileUri.getPath(), false);
                    }
                }
                break;
            case REQUEST_CODE_ADD_CONTACT:


                if (resultCode == RESULT_OK) {
                    dbAdapter.updateIsInContactList(jid, 1,null);
                    Intent i = new Intent(mParentActivity, SliderTesting.class);
                    startActivity(i);
                    finish();

                    IsProcessOnAddContact = true;
                    //listview.removeHeaderView(listViewheader);

                } else {
                    IsProcessOnAddContact = false;
                }
                break;
        }
    }


    public synchronized void uploadFile(String strURL, boolean is_image) {
        synchronized (this) {
        i++;
        Constant.printMsg("getting url:::>>>>>>>>>" + strURL + "  " + i);
        // int i = 0;
        Constant.bux = sharedPrefs.getLong("buxvalue", 0);

        Long buxval = Constant.bux + Constant.imagepoints;
        Constant.bux = buxval;

        int point = sharedPrefs.getInt("imgpoint", 0);

        Constant.totalimg = point;

        Constant.totalimg = Count + Constant.totalimg;

        Editor e2 = sharedPrefs.edit();
        e2.putInt("imgpoint", Constant.totalimg);
        e2.commit();

        Editor e1 = sharedPrefs.edit();
        e1.putLong("buxvalue", buxval);
        e1.commit();

        String time = "" + System.currentTimeMillis();
        String File_name = time + String.valueOf(i) + ".jpg";
        Constant.printMsg("fillleeeeeee::::::::" + File_name + "  " + i);
        String file_path = "";
        int media_duration = 0;
        int size = 0;
        byte[] thumb = null;

        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        if (is_image) {

            try {

                byte[] byteArray = null;
                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                ByteArrayOutputStream outstream_thumb = new ByteArrayOutputStream();
                // Part 1: Decode image
//                Bitmap unscaledBitmap = ScalingUtilities.decodeFile(strURL,
//                        480, 800);

             /*   if (!(unscaledBitmap.getWidth() <= 480 && unscaledBitmap
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
                }*/

                Bitmap thumb_bitmap =  new CompressImage().compressImage(strURL, file_path,13);
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 80,
                        outstream_thumb);
                thumb = outstream_thumb.toByteArray();

                Constant.printMsg("Compress thumb:::::::" + thumb.length);

                boolean success = (new File(Constant.local_image_dir))
                        .mkdirs();
                if (!success) {
                    Log.w("directory not created", "directory not created");
                }

                file_path = Constant.local_image_dir + File_name;
                new CompressImage().compressImage(strURL, file_path,1);
                Constant.printMsg("file pathhhhhh:::::::" + file_path);
               /* FileOutputStream stream = new FileOutputStream(file_path);

                byteArray = outstream.toByteArray();

                stream.write(byteArray);
                stream.close();*/

                // Toast.makeText(mParentActivity, "Downloading Completed",
                // Toast.LENGTH_SHORT).show();
            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
            }

        } else {
            file_path = strURL;
            Constant.printMsg("file path from strurl::::>>>>" + file_path);
            Bitmap video_thumb = ThumbnailUtils.createVideoThumbnail(file_path,
                    MediaStore.Images.Thumbnails.MINI_KIND);

            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
            video_thumb.compress(Bitmap.CompressFormat.JPEG, 50, outstream);

            thumb = outstream.toByteArray();

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
            Log.d("Video Thumb size", "Video Thumbsize::" + (size / 1024)
                    / 1024 + " MB");

            try {
                if (!f.getPath().equals(Constant.local_video_dir + File_name)) {
                    Utils.Copyfile(f, new File(Constant.local_video_dir
                            + File_name));
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
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
        msggetset.setPostion(k);
        if (file_path.contains(".")) {
            Constant.printMsg("file path from strurl::::>>>>" + file_path);

            Constant.printMsg("file name:;" + File_name);

            msggetset.setMedia_mime_type(new Utils().getMimeType(file_path));
            if (is_image) {
                msggetset.setMedia_wa_type("" + 1);
            } else {
                msggetset.setMedia_wa_type("" + 2);
            }
            msggetset.setMedia_url(null);
            msggetset.setRow_data(thumb);
            msggetset.setMedia_duration(media_duration);
            msggetset.setMedia_size(size);
            msggetset.setIs_sec_chat(sec);
            msggetset.setSelf_des_time(selected_self_desc_time);
            long id_insert = dbAdapter.setInsertMessages(msggetset);
            msggetset.set_id((int) id_insert);

            // Get size in double to update media network usage
            try {

                File f = new File(strURL);
                long bite = (long) f.length();
                Constant.printMsg("Double :" + bite);
                updateMediaNetwork(bite);

            } catch (Exception e) {

            }


            if(is_image) {
               /* msg_list.add(msggetset);
                mPositionKey.add(msggetset.getKey_id());
                dest_list_msgids.add(id_insert);
                dest_list_bombids.add(R.drawable.black_bomb);
                dest_list_anim.add(0);

                mParentActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        initializeListElememts(1);
                        rightImageChat();
                        setRightImage();
                        k = k + 1;

                        mRightTipLayout.requestFocus();
                    }
                });
*/
                ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
            }else{
                ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
            }


            //                    if(Constant.mselected_self_destruct_time!=0)
            //                    {
            //                        database_destList.add(new DestructGetter(stringmsg, id_insert));
            //
            //
            //                    }


//            ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());

//            new FetchChat().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            // edt_msg.setText("");
//            msg_list.add(msggetset);
//            dest_list_msgids.add(id_insert);
//            dest_list_bombids.add(R.drawable.black_bomb);
//            dest_list_anim.add(0);
//
//            if (is_image) {
//
//                mParentActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        initializeListElememts(1);
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
//                        initializeListElememts(1);
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

        } else {
            Toast.makeText(getApplicationContext(), "Please Select Valid File",
                    Toast.LENGTH_SHORT).show();
        }
        int msg_id = dbAdapter.getLastMsgid_chat(jid, sec);
        Constant.printMsg("looooollll   " + msg_id);

        if (dbAdapter.isExistinChatList_chat(jid, sec)) {
            dbAdapter.setUpdateChat_lits_chat(jid, msg_id, sec);
        } else {
            dbAdapter.setInsertChat_list_chat(jid, msg_id, sec);
        }


//        Intent login_broadcast = new Intent("chat");
//        login_broadcast.putExtra("jid", "" + jid);
//        getApplicationContext().sendBroadcast(login_broadcast);
    }
  }

//    public void uploadLogo(byte[] strURL, boolean is_image) {
//        int i = 0;
//        i++;
//        Constant.bux = sharedPrefs.getLong("buxvalue", 0);
//
//        Long buxval = Constant.bux + Constant.imagepoints;
//        Constant.bux = buxval;
//
//        Editor e1 = sharedPrefs.edit();
//        e1.putLong("buxvalue", buxval);
//        e1.commit();
//
//        String time = "" + System.currentTimeMillis();
//        String File_name = time + String.valueOf(i) + ".jpg";
//        String file_path = "";
//        int media_duration = 0;
//        int size = 0;
//        byte[] thumb = null;
//
//        String strMyImagePath = null;
//        Bitmap scaledBitmap = null;
//
//        if (is_image) {
//            try {
//
//                byte[] byteArray = null;
//                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
//                ByteArrayOutputStream outstream_thumb = new ByteArrayOutputStream();
//                // Part 1: Decode image
//                Bitmap unscaledBitmap = BitmapFactory.decodeByteArray(strURL,
//                        0, strURL.length);
//                // Bitmap unscaledBitmap = bmp;
//
//                if (!(unscaledBitmap.getWidth() <= 480 && unscaledBitmap
//                        .getHeight() <= 800)) {
//                    // Part 2: Scale image
//                    scaledBitmap = ScalingUtilities.createScaledBitmap(
//                            unscaledBitmap, 480, 800);
//                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75,
//                            outstream);
//                    scaledBitmap.recycle();
//                } else {
//
//                    unscaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75,
//                            outstream);
//                    unscaledBitmap.recycle();
//                }
//
//                // Bitmap thumb_bitmap = strURL;
//
//                Bitmap thumb_bitmap = BitmapFactory.decodeByteArray(strURL, 0,
//                        strURL.length,Util.getBitmapOptions());
//
//                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 75,
//                        outstream_thumb);
//                thumb = outstream_thumb.toByteArray();
//
//                boolean success = (new File(Constant.local_image_dir))
//                        .mkdirs();
//                Constant.printMsg("logo success:;" + success + File_name);
//                if (!success) {
//                    Log.w("directory not created", "directory not created");
//                }
//
//                file_path = Constant.local_image_dir + File_name;
//
//                FileOutputStream stream = new FileOutputStream(file_path);
//
//                byteArray = outstream.toByteArray();
//
//                stream.write(byteArray);
//                stream.close();
//
//                // Toast.makeText(mParentActivity, "Downloading Completed",
//                // Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//                e.printStackTrace();
//            }
//
//        }
//
//        MessageGetSet msggetset = new MessageGetSet();
//        String packet_id = "" + new Date().getTime();
//
//        msggetset.setData("");
//        msggetset.setKey_from_me(0);
//        msggetset.setKey_id("" + packet_id);
//        msggetset.setKey_remote_jid(jid);
//        msggetset.setNeeds_push(1);
//        msggetset.setSend_timestamp(new Date().getTime());
//        msggetset.setStatus(3);
//        msggetset.setTimestamp(new Date().getTime());
//        msggetset.setThumb_image(thumb);
//
//        msggetset.setMedia_name(File_name);
//        if (file_path.contains(".")) {
//
//            msggetset.setMedia_mime_type(new Utils().getMimeType(file_path));
//            // if (is_image) {
//            msggetset.setMedia_wa_type("" + 11);
//            // }
//            msggetset.setMedia_url(null);
//            msggetset.setRow_data(thumb);
//            msggetset.setMedia_duration(media_duration);
//            msggetset.setMedia_size(size);
//            msggetset.setIs_sec_chat(sec);
//            msggetset.setSelf_des_time(selected_self_desc_time);
//            dbAdapter.setInsertMessages(msggetset);
//
//            // Get size in bytes to update media network usage
//            try {
//                long bite = (long) new File(file_path).length();
//
//                updateMediaNetwork(bite);
//
//            } catch (Exception e) {
//
//            }
//            // edt_msg.setText("");
//        } else {
//            Toast.makeText(getApplicationContext(), "Please Select Valid File",
//                    Toast.LENGTH_SHORT).show();
//        }
//        ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
//
//
//        int msg_id = dbAdapter.getLastMsgid_chat(jid, sec);
//        Constant.printMsg("looooollll   " + msg_id);
//
//        if (dbAdapter.isExistinChatList_chat(jid, sec)) {
//            dbAdapter.setUpdateChat_lits_chat(jid, msg_id, sec);
//        } else {
//            dbAdapter.setInsertChat_list_chat(jid, msg_id, sec);
//        }
//
//        Intent login_broadcast = new Intent("chat");
//        login_broadcast.putExtra("jid", "" + jid);
//        this.sendBroadcast(login_broadcast);
//
//        ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
//    }

    public void upload_audio_File(String strURL) {
        Constant.printMsg("StRing PaTH ::: >>>> " + strURL);
        String time = "" + System.currentTimeMillis();
        String File_name = time + ".jpg";
        String file_path = "";
        int media_duration = 0;
        int size = 0;
        file_path = strURL;
        File f = new File(strURL);
        File_name = f.getName();
        size = (int) f.length();
        Constant.printMsg("file Name :::::::" + File_name);
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

          /*
           * try { Utils.Copyfile(f, new
           * File(Constant.local_audio_dir+File_name)); } catch (IOException e) {
           * // TODO Auto-generated catch block e.printStackTrace(); }
           */
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
        msggetset.setPostion(k);
        if (file_path.contains(".")) {

            msggetset.setMedia_mime_type(new Utils().getMimeType(file_path));
            msggetset.setMedia_wa_type("" + 3);

            msggetset.setMedia_url(null);
            msggetset.setRow_data(null);
            msggetset.setMedia_duration(media_duration);
            msggetset.setMedia_size(size);
            msggetset.setIs_sec_chat(sec);
            msggetset.setSelf_des_time(selected_self_desc_time);
            long id_insert = dbAdapter.setInsertMessages(msggetset);
            msggetset.set_id((int) id_insert);
            // Get size in bytes to update media network usage
            try {
                long bite = (long) f.length();

                updateMediaNetwork(bite);

            } catch (Exception e) {

            }

            msg_list.add(msggetset);
            mPositionKey.add(msggetset.getKey_id());
            dest_list_msgids.add(id_insert);
            dest_list_bombids.add(R.drawable.black_bomb);
            dest_list_anim.add(0);

            mParentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initializeListElememts(1);
                    rightAudioChat();
                    setRightAudio_Old();
                    k = k + 1;

                    mRightTipLayout.requestFocus();
                    edt_msg.setText("");
                    edt_msg.requestFocus();
                }
            });

            //     new FetchChat().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please Select Valid File",
                    Toast.LENGTH_SHORT).show();
        }

        int msg_id = dbAdapter.getLastMsgid_chat(jid, sec);

        if (dbAdapter.isExistinChatList_chat(jid, sec)) {
            dbAdapter.setUpdateChat_lits_chat(jid, msg_id, sec);
        } else {
            dbAdapter.setInsertChat_list_chat(jid, msg_id, sec);
        }


//        Intent login_broadcast = new Intent("chat");
//        login_broadcast.putExtra("jid", "" + jid);
//        getApplicationContext().sendBroadcast(login_broadcast);
//
//        new FetchChat().execute();
    }

    // /////////////********SEND
    // FILE*************************//////////////////////////////////////////////////////////////
    public void SEND_FILE(String strURL) {
        Constant.printMsg("StRing path :::::: " + strURL);
        String time = "" + System.currentTimeMillis();
        String File_name = time + ".jpg";
        String file_path = "";
        int media_duration = 0;
        int size = 0;
        file_path = strURL;
        File f = new File(strURL);
        File_name = f.getName();
        size = (int) f.length();
        Constant.printMsg("file Name :::::::" + File_name);
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
        if (file_path.contains(".")) {

            Log.d("Chat", "MIME type::" + new Utils().getMimeType(file_path));
            msggetset.setMedia_mime_type(new Utils().getMimeType(file_path));
            msggetset.setMedia_wa_type("" + 6);
            msggetset.setMedia_size(size);
            msggetset.setIs_sec_chat(sec);
            msggetset.setSelf_des_time(selected_self_desc_time);
            dbAdapter.setInsertMessages(msggetset);

            // Get size in double to update media network usage
            try {

                long bite = (long) f.length();
                updateMediaNetwork(bite);

            } catch (Exception e) {

            }

              /*
               * try { Utils.Copyfile(f, new
               * File(Constant.local_files_dir+File_name)); } catch (IOException
               * e) { // TODO Auto-generated catch block e.printStackTrace(); }
               */

            ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
        } else {
            Toast.makeText(getApplicationContext(), "Please Select Valid File",
                    Toast.LENGTH_SHORT).show();
        }
        int msg_id = dbAdapter.getLastMsgid_chat(jid, sec);
        Constant.printMsg("looooollll   " + msg_id);

        if (dbAdapter.isExistinChatList_chat(jid, sec)) {
            dbAdapter.setUpdateChat_lits_chat(jid, msg_id, sec);
        } else {
            dbAdapter.setInsertChat_list_chat(jid, msg_id, sec);
        }

        Intent login_broadcast = new Intent("chat");
        login_broadcast.putExtra("jid", "" + jid);
        getApplicationContext().sendBroadcast(login_broadcast);

        ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
    }

    // /////////////********FORWORD
    // FILE*************************//////////////////////////////////////////////////////////////
    public void FORWORD_FILE(MessageGetSet msggetset) {

        String packet_id = "" + new Date().getTime();

        msggetset.setKey_from_me(0);
        msggetset.setKey_id("" + packet_id);
        msggetset.setKey_remote_jid(jid);
        msggetset.setNeeds_push(1);
        msggetset.setSend_timestamp(new Date().getTime());
        msggetset.setStatus(3);
        msggetset.setTimestamp(new Date().getTime());
        msggetset.setThumb_image(null);
        msggetset.setIs_sec_chat(sec);
        msggetset.setSelf_des_time(selected_self_desc_time);
        msggetset.setRemote_resource(null);
        dbAdapter.setInsertMessages(msggetset);


        int msg_id = dbAdapter.getLastMsgid_chat(jid, sec);
        Constant.printMsg("looooollll   " + msg_id);

        if (dbAdapter.isExistinChatList_chat(jid, sec)) {
            dbAdapter.setUpdateChat_lits_chat(jid, msg_id, sec);
        } else {
            dbAdapter.setInsertChat_list_chat(jid, msg_id, sec);
        }

        Intent login_broadcast = new Intent("chat");
        login_broadcast.putExtra("jid", "" + jid);
        getApplicationContext().sendBroadcast(login_broadcast);

        ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
    }

    // /////////////********SEND LOCATION
    // ********************////////////////////////////////////////////////////////////
    public void send_Location(final String lat, final String log,
                              final byte[] map_data, final String location_name) {

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
        final String packet_id = "" + new Date().getTime();

        msggetset.setData(location_name);
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
        msggetset.setPostion(k);

        msggetset.setMedia_wa_type("" + 4);

        msggetset.setRow_data(map_data);

        msggetset.setIs_sec_chat(sec);
        msggetset.setSelf_des_time(selected_self_desc_time);
        long id_insert = dbAdapter.setInsertMessages(msggetset);
        msggetset.set_id((int) id_insert);
        // edt_msg.setText("");

        msg_list.add(msggetset);
        mPositionKey.add(msggetset.getKey_id());
        dest_list_msgids.add(id_insert);
        dest_list_bombids.add(R.drawable.black_bomb);
        dest_list_anim.add(0);

        mParentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initializeListElememts(1);
                rightImageChat();
                setLocation();
                k = k + 1;

                mRightTipLayout.requestFocus();
                edt_msg.setText("");
                edt_msg.requestFocus();
            }
        });

//        new FetchChat().execute();
        int msg_id = dbAdapter.getLastMsgid_chat(jid, sec);

        if (dbAdapter.isExistinChatList_chat(jid, sec)) {
            dbAdapter.setUpdateChat_lits_chat(jid, msg_id, sec);
        } else {
            dbAdapter.setInsertChat_list_chat(jid, msg_id, sec);
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                Message msg = new Message();

                msg.setStanzaId("" + packet_id);
                // msg.setBody(location_name);
                msg.setBody(Utils.EncryptMessage(location_name));
                JivePropertiesManager.addProperty(msg, "media_type", 4);
                JivePropertiesManager.addProperty(msg, "thumb_image",
                        Base64.encodeToString(map_data));

                JivePropertiesManager.addProperty(msg, "lat", lat);
                JivePropertiesManager.addProperty(msg, "lon", log);

                if (sec == 0) {
                    JivePropertiesManager
                            .addProperty(msg, "is_owner", IS_OWNER);
                    JivePropertiesManager.addProperty(msg, "is_sec_chat", sec);
                    JivePropertiesManager.addProperty(msg, "self_desc_time",
                            selected_self_desc_time);
                }

                // DeliveryReceiptRequest dr = new DeliveryReceiptRequest();
                DeliveryReceiptRequest.addTo(msg);
                try {
                    // chatManager = ChatManager.getInstanceFor(connection);

                    if (chat == null) {

                        chat = TempConnectionService.chatmanager.createChat(
                                jid,
                                TempConnectionService.mChatCreatedListener.getMessageListener());
                    }
                    // MessageEventManager.addNotificationsRequests(msg, true,
                    // true, true, true);
                    Constant.printMsg("setmeg::::::::::>>>>>" + msg);
                    // chat.sendMessage(msg);

                    if (Connectivity.isOnline(mParentActivity) == true
                            && TempConnectionService.connection.isConnected() == true && TempConnectionService.connection.isAuthenticated() == true) {
                        chat.sendMessage(msg);
                        status_msg = 2;
                        // TempConnectionService.connection.sendStanza(msg);

                        Constant.printMsg("dataaa ::::::: " + status_msg);
                    }

                    // TempConnectionService.connection.sendStanza(msg);

                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                    // TODO: handle exception
                    Constant.printMsg("GGGGGGGG" + e.toString());
                }
            }
        };
        thread.start();
//        Intent login_broadcast = new Intent("chat");
//        login_broadcast.putExtra("jid", "" + jid);
//        getApplicationContext().sendBroadcast(login_broadcast);
//
//        new FetchChat().execute();
    }

    public void send_Contact(final byte[] avatar, final String vcard,
                             final String name) {

        MessageGetSet msggetset = new MessageGetSet();
        final String packet_id = "" + new Date().getTime();

        Constant.printMsg("vcard  " + vcard.toString());
        Constant.printMsg("Diliiip " + name+","+numberContactSend );

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
        msggetset.setIs_sec_chat(sec);
        msggetset.setSelf_des_time(selected_self_desc_time);
        msggetset.setIs_owner(IS_OWNER);

        long id_insert = dbAdapter.setInsertMessages(msggetset);
        msggetset.set_id((int) id_insert);
        // edt_msg.setText("");

        msg_list.add(msggetset);
        mPositionKey.add(msggetset.getKey_id());
        dest_list_msgids.add(id_insert);
        dest_list_bombids.add(R.drawable.black_bomb);
        dest_list_anim.add(0);

        mParentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initializeListElememts(1);
                rightContactChat();
                setRightContact();
                k = k + 1;

                mRightTipLayout.requestFocus();
                edt_msg.setText("");
                edt_msg.requestFocus();
            }
        });

//        new FetchChat().execute();
        int msg_id = dbAdapter.getLastMsgid_chat(jid, sec);
        Constant.printMsg("looooollll   " + msg_id);

        if (dbAdapter.isExistinChatList_chat(jid, sec)) {
            dbAdapter.setUpdateChat_lits_chat(jid, msg_id, sec);
        } else {
            dbAdapter.setInsertChat_list_chat(jid, msg_id, sec);
        }

          /*
           * chatManager =ChatManager.getInstanceFor(connection); chat =
           * chatManager.createChat(jid, mBoundService.getMessageListener());
           */

          /*
           * Message msg = new Message();
           * //JivePropertiesManager.addProperty(msg,"msg_type", 4);
           * msg.setPacketID("" + packet_id);
           * msg.setBody(Utils.EncryptMessage(vcard));
           *
           * JivePropertiesManager.addProperty(msg,"media_type", 5);
           *
           * if (avatar != null) {
           * JivePropertiesManager.addProperty(msg,"thumb_image", avatar); }
           *
           * JivePropertiesManager.addProperty(msg,"media_name", name); if(sec==0)
           * { //JivePropertiesManager.addProperty(msg,"is_owner", IS_OWNER);
           * JivePropertiesManager.addProperty(msg,"is_sec_chat", sec);
           * JivePropertiesManager.addProperty(msg,"self_desc_time",
           * selected_self_desc_time); }
           *
           *
           * drm.addDeliveryReceiptRequest(msg); try {
           * if(KachingMeApplication.getIsNetAvailable()) {
           *
           * MessageEventManager.addNotificationsRequests(msg, true, true, true,
           * true); chat.sendMessage(msg);
           *
           * }
           *
           * } catch (Exception e) { // TODO: handle exception
           * e.printStackTrace(); }
           */

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Message msg = new Message();
                    // msg.setPacketID("" + packet_id);
                    msg.setStanzaId("" + packet_id);
                    // msg.setBody(vcard);
                    msg.setBody(Utils.EncryptMessage(vcard));
                    JivePropertiesManager.addProperty(msg, "media_type", 5);
                    if (avatar != null) {
                        JivePropertiesManager.addProperty(msg, "thumb_image",
                                avatar);
                    }
                    JivePropertiesManager.addProperty(msg, "media_name", name+","+numberContactSend);
                    if (sec == 0) {
                        JivePropertiesManager.addProperty(msg, "is_sec_chat",
                                sec);
                        JivePropertiesManager.addProperty(msg,
                                "self_desc_time", selected_self_desc_time);
                    }

                    // chatManager = ChatManager.getInstanceFor(connection);
                    if (chat == null) {

                        chat = TempConnectionService.chatmanager.createChat(
                                jid,
                                TempConnectionService.mChatCreatedListener.getMessageListener());
                    }

                    if (KachingMeApplication.getIsNetAvailable()) {
                        MessageEventManager.addNotificationsRequests(msg, true,
                                true, true, true);

                        DeliveryReceiptRequest.addTo(msg);
                        Constant.printMsg("self Msg::::>>>>>>" + msg);
                        chat.sendMessage(msg);

                    }

                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        };

        thread.start();

//        Intent login_broadcast = new Intent("chat");
//        login_broadcast.putExtra("jid", "" + jid);
//        getApplicationContext().sendBroadcast(login_broadcast);
//
//        new FetchChat().execute();
    }

    /**
     * ************ Forword Image *******************************************************
     */
    public void Forword_Image(MessageGetSet msg_bean) {
        Log.d("Message send calles", "Message send callled...");
        MessageGetSet msggetset = new MessageGetSet();
        String packet_id = "" + new Date().getTime();
        msggetset = msg_bean;

          /* msggetset.setData(""); */
        msggetset.setKey_from_me(0);
        msggetset.setKey_id("" + packet_id);
        msggetset.setKey_remote_jid(jid);
        msggetset.setNeeds_push(1);
        msggetset.setSend_timestamp(new Date().getTime());
        msggetset.setStatus(3);
        msggetset.setTimestamp(new Date().getTime());
        msggetset.setIs_sec_chat(sec);
        msggetset.setSelf_des_time(selected_self_desc_time);
        msggetset.setRemote_resource(null);
        dbAdapter.setInsertMessages(msggetset);

        int msg_id = dbAdapter.getLastMsgid_chat(jid, sec);
        Constant.printMsg("looooollll   " + msg_id);

        if (dbAdapter.isExistinChatList_chat(jid, sec)) {
            dbAdapter.setUpdateChat_lits_chat(jid, msg_id, sec);
        } else {
            dbAdapter.setInsertChat_list_chat(jid, msg_id, sec);
        }

        // new FetchChat().execute();

        Intent login_broadcast = new Intent("chat");
        login_broadcast.putExtra("jid", "" + jid);
        getApplicationContext().sendBroadcast(login_broadcast);

        Log.d("Message send calles", "Message send ended...");
    }

    public void Forword_Video(MessageGetSet message_peram) {

        String packet_id = "" + new Date().getTime();
        message_peram.setKey_id("" + packet_id);
        message_peram.setKey_remote_jid(jid);
        message_peram.setKey_from_me(0);
        message_peram.setNeeds_push(1);
        message_peram.setSend_timestamp(new Date().getTime());
        message_peram.setStatus(3);
        message_peram.setTimestamp(new Date().getTime());
        message_peram.setIs_sec_chat(sec);
        message_peram.setSelf_des_time(selected_self_desc_time);
        message_peram.setRemote_resource(null);
        dbAdapter.setInsertMessages(message_peram);
        // edt_msg.setText("");

        int msg_id = dbAdapter.getLastMsgid_chat(jid, sec);
        Constant.printMsg("looooollll   " + msg_id);

        if (dbAdapter.isExistinChatList_chat(jid, sec)) {
            dbAdapter.setUpdateChat_lits_chat(jid, msg_id, sec);
        } else {
            dbAdapter.setInsertChat_list_chat(jid, msg_id, sec);
        }

        Intent login_broadcast = new Intent("chat");
        login_broadcast.putExtra("jid", "" + jid);
        getApplicationContext().sendBroadcast(login_broadcast);

        // new FetchChat().execute();
    }

    public void Forword_Audio(MessageGetSet message_peram) {

        MessageGetSet msggetset = message_peram;
        String packet_id = "" + new Date().getTime();
        msggetset.setKey_from_me(0);
        msggetset.setKey_id("" + packet_id);
        msggetset.setKey_remote_jid(jid);
        msggetset.setNeeds_push(1);
        msggetset.setSend_timestamp(new Date().getTime());
        msggetset.setStatus(3);
        msggetset.setTimestamp(new Date().getTime());
        msggetset.setIs_sec_chat(sec);
        msggetset.setSelf_des_time(selected_self_desc_time);
        msggetset.setRemote_resource(null);
        dbAdapter.setInsertMessages(msggetset);

        // new FetchChat().execute();

        int msg_id = dbAdapter.getLastMsgid_chat(jid, sec);
        Constant.printMsg("looooollll   " + msg_id);

        if (dbAdapter.isExistinChatList_chat(jid, sec)) {
            dbAdapter.setUpdateChat_lits_chat(jid, msg_id, sec);
        } else {
            dbAdapter.setInsertChat_list_chat(jid, msg_id, sec);
        }

        Intent login_broadcast = new Intent("chat");
        login_broadcast.putExtra("jid", "" + jid);
        getApplicationContext().sendBroadcast(login_broadcast);

        // new FetchChat().execute();
    }

    public void voice_dialog() {
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

        if (!success) {
            Log.e("directory not created", "directory not created");
        }
        final String file_path = Constant.local_audio_dir + System.currentTimeMillis() + ".amr";

        myRecorder.setOutputFile(file_path);

        try {
            myRecorder.prepare();
        } catch (IllegalStateException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        btn_record.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("Record", "Long Pressed");
                ch.start();
                return true;
            }
        });

        btn_record.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!is_started) {
                        try {
                            presence_recording();
                            myRecorder.start();
                        } catch (Exception e) {
                            // ACRA.getErrorReporter().handleException(e);
                            e.printStackTrace();
                        }

                        ch.setBase(SystemClock.elapsedRealtime());
                        Log.e("Record", "Action Up");
                        btn_record.setImageDrawable(getResources().getDrawable(R.drawable.btn_record));
                        ch.start();
                        is_started = true;
                        is_startrec = false;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (!is_startrec) {
                        Log.e("Record", "Action Down");
                        Log.e("Action Down", "Down" + is_startrec);

                        btn_record.setImageDrawable(getResources().getDrawable(R.drawable.btn_send));

                        ch.stop();
                        timer.cancel();
                        String chrono_text = ch.getText().toString();

                        if (chrono_text.equalsIgnoreCase("00:00")) {
                            ch.stop();
                            btn_record.setImageDrawable(getResources().getDrawable(R.drawable.btn_hold_talk));
                            Toast.makeText(getApplicationContext(), "Hold To Record", Toast.LENGTH_LONG).show();
                            is_startrec = true;
                            is_started = true;
                        }
                        try {
                            if (myRecorder != null) {
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
                        } catch (IllegalStateException e) {
                            Log.e("Chat Test Illegal", is_started + "\n" + is_startrec + "\n" + e.toString());

                            e.printStackTrace();

                            if (is_startrec) {
                                is_started = false;
                                is_startrec = false;
                                Log.e("Illegal Status", is_started + "\n" + is_startrec);
                            } else {
                                is_started = true;
                                is_startrec = false;
                                Log.e("Illegal Status", is_started + "\n" + is_startrec);
                            }
                        } catch (RuntimeException e) {
                            Log.e("Chat Test Runtime", is_started + "\n" + is_startrec + "\n" + e.toString());
                            e.printStackTrace();
                            btn_record.setImageDrawable(getResources().getDrawable(R.drawable.btn_hold_talk));
                            is_startrec = true;
                            is_started = false;
                        } catch (IOException e) {
                            Log.e("Chat Test", e.toString());
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.printMsg(is_startrec + "Start Rec");

                if (is_startrec) {
                    Toast.makeText(getApplicationContext(), "No Audio File Found..!!", Toast.LENGTH_LONG).show();
                } else {
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
    }

    public void Alert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mParentActivity);
        builder1.setMessage("Please Enter Some Text");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                final Dialog nagDialog = new Dialog(mParentActivity);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                nagDialog.setCancelable(true);
                dialog.cancel();
            }
        });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void Show_Self_desc_time(int selected) {
        final CharSequence[] items = getResources().getStringArray(R.array.self_des_time);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.self_des_time));
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Constant.mself_destruct_time == true) {
                    Constant.mself_destruct_msg = edt_msg.getText().toString().trim();
                    Constant.mself_jid = jid;
                    Constant.mself_destruct_time = false;
                    System.out.println("called self destruct send condition inside the btn click");
                    if (Constant.mself_destruct_msg.toString().length() != 0) {
                        dest_msg_list.add(Constant.mselected_self_destruct_time);
                        Constant.printMsg("msg:::"
                                + Constant.mself_destruct_msg + "time::::"
                                + Constant.mselected_self_destruct_time);

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
                        mDest = true;

                        sendMessage("<s>"
                                + Constant.mselected_self_destruct_time + "-"
                                + Constant.mself_destruct_msg + "-"
                                + Constant.mself_jid);

                        Constant.printMsg("Destruct service called 1" + GlobalBroadcast.isServiceRunning(DestructService.class.getCanonicalName(), mParentActivity));

                        if (!GlobalBroadcast.isServiceRunning(DestructService.class.getCanonicalName(), mParentActivity)) {
                            Constant.printMsg("Destruct service called 2" + GlobalBroadcast.isServiceRunning(DestructService.class.getCanonicalName(), mParentActivity));
                            startService(new Intent(mParentActivity, DestructService.class));
                        }
                    }
                }
            }
        });

        builder.setSingleChoiceItems(items, selected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub


                if(mPopup){
                    downArrowClickAction();
                }
                System.out.println("selected time postion:::::" + which);
                Constant.mselected_self_destruct_time = self_desc_time[which];
                Constant.printMsg("selected time:::::" + Constant.mselected_self_destruct_time);
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

    private void set_Self_Dest_time(int index) {

        Message msg = new Message();
        // JivePropertiesManager.addProperty(msg,"msg_type", 7);
        msg.setStanzaId("" + new Date().getTime());
        msg.setBody("");
        JivePropertiesManager.addProperty(msg, "media_type", 7);
          /*
           * JivePropertiesManager.addProperty(msg,"is_sec_chat", sec);
           * JivePropertiesManager.addProperty(msg,"self_desc_time",
           * selected_self_desc_time);
           * JivePropertiesManager.addProperty(msg,"self_desc_time_index",index);
           */

          /*
           * if (IS_OWNER == 0) {
           * JivePropertiesManager.addProperty(msg,"is_owner", 1); } else {
           * JivePropertiesManager.addProperty(msg,"is_owner", 0); }
           */

        // JivePropertiesManager.addProperty(msg,"is_owner", IS_OWNER);
        JivePropertiesManager.addProperty(msg, "is_sec_chat", sec);
        JivePropertiesManager.addProperty(msg, "self_desc_time",
                selected_self_desc_time);

        try {
            DeliveryReceiptManager drm = TempConnectionService.drm;

            drm.addDeliveryReceiptRequest(msg);
            MessageGetSet msggetset = new MessageGetSet();
            // chatManager = ChatManager.getInstanceFor(connection);
            if (chat == null) {

                chat = TempConnectionService.chatmanager.createChat(jid,
                        TempConnectionService.mChatCreatedListener.getMessageListener());
            }

            if (KachingMeApplication.getIsNetAvailable()) {
                MessageEventManager.addNotificationsRequests(msg, true, true,
                        true, true);
                chat.sendMessage(msg);
                is_new_sec = false;
            }

            msggetset.setData("");
            msggetset.setKey_from_me(0);
            msggetset.setKey_id("" + msg.getStanzaId());
            msggetset.setKey_remote_jid(jid);
            msggetset.setNeeds_push(1);
            msggetset.setSend_timestamp(new Date().getTime());
            msggetset.setStatus(3);
            msggetset.setTimestamp(new Date().getTime());
            msggetset.setMedia_wa_type("7");
            msggetset.setIs_sec_chat(sec);
            msggetset.setSelf_des_time(selected_self_desc_time);
            msggetset.setIs_owner(IS_OWNER);
            dbAdapter.setInsertMessages(msggetset);
            edt_msg.setText("");


            int msg_id = dbAdapter.getLastMsgid_chat(jid, sec);
            Constant.printMsg("looooollll   " + msg_id);

            if (dbAdapter.isExistinChatList_chat(jid, sec)) {
                dbAdapter.setUpdateChat_lits_chat(jid, msg_id, sec);
            } else {
                dbAdapter.setInsertChat_list_chat(jid, msg_id, sec);
            }

            Intent login_broadcast = new Intent("chat");
            login_broadcast.putExtra("jid", "" + jid);
            getApplicationContext().sendBroadcast(login_broadcast);

        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
            e.printStackTrace();
        }
    }

//    private void onListItemSelect(int i) {
//        Constant.adapterTest_cursor.toggleSelection(i);
//        boolean hasCheckedItems = Constant.adapterTest_cursor.getSelectedCount() > 0;
//
//        Constant.printMsg("Action Mode...." + hasCheckedItems);
//
//        if (hasCheckedItems && mActionMode == null)
//            // there are some selected items, start the actionMode
//            mActionMode = startSupportActionMode(new ActionModeCallback());
//
//        else if (!hasCheckedItems && mActionMode != null)
//            // there no selected items, finish the actionMode
//            mActionMode.finish();
//
//        if (mActionMode != null)
//            mActionMode.setTitle(String.valueOf(Constant.adapterTest_cursor
//                    .getSelectedCount()) + " selected");
//
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

                            Intent i = new Intent(getApplicationContext(),
                                    SliderTesting.class);
                            startActivity(i);
                            finish();
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

                                Intent i = new Intent(getApplicationContext(),
                                        SliderTesting.class);
                                startActivity(i);
                                finish();
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
                image_picker(1);
            }
        });
        mVideoText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                image_picker(2);
            }
        });
        dialog.show();


    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation.equals(popupShow)) {
            // buttonShowHidePopup.setText(getString(R.string.btn_hide_txt));
        } else if (animation.equals(popupHide)) {
            // buttonShowHidePopup.setText(getString(R.string.btn_show_txt));
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (animation.equals(popupShow)) {
            linearLayoutPopup.setVisibility(View.VISIBLE);
        } else if (animation.equals(popupHide)) {
            linearLayoutPopup.setVisibility(View.GONE);
        }
    }

    public void insertDBDonation(ContentValues v) {

        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_DONATE, null, v);

            Constant.printMsg("No of inserted rows in bux details :::::::::"
                    + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in new shop details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }

    }

    public void copyDirectoryOneLocationToAnotherLocation(File sourceLocation,
                                                          File targetLocation) throws IOException {

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

    }

    public void update_sent_count(int code) {
        Constant.printMsg("sent_msg_count msg update_received_count added is::"
                + code);
        Editor e = pref.edit();
        e.putInt("sent_msg_count", code);
        Constant.printMsg("sent_msg_count in chat  :::" + code);
        e.commit();
    }

    public void mSendMsg() {

        Constant.printMsg("sent_msg_count message count::");
        int sent_msg_count = pref.getInt("sent_msg_count", 0);
        Constant.printMsg("sent_msg_count msg count from slide show is::"
                + sent_msg_count);
        sent_msg_count = sent_msg_count + 1;
        Constant.printMsg("sent_msg_count msg countafter added is::"
                + sent_msg_count);
        update_sent_count(sent_msg_count);
        // mDownArrow.setImageResource(R.drawable.rt_arrow);
        // Constant.mTouch = false;
        if(mPopup){
            downArrowClickAction();
        }
        isClicked = true;
        mParseString = "";

        if (!edt_msg.getText().toString().trim().equals("")) {

            String txt = edt_msg.getText().toString();

            Constant.printMsg("onclcik" + txt);

            String[] words = txt.split("\\s+");

            Constant.bux = sharedPrefs.getLong("buxvalue", 0);

            Long buxval = Constant.bux + Constant.chatpoints;
            Constant.bux = buxval;

            Editor e = sharedPrefs.edit();
            e.putLong("buxvalue", buxval);
            e.commit();

            int point = sharedPrefs.getInt("chatpoint", 0);

            Constant.totalchat = point;

            Constant.totalchat = Count + Constant.totalchat;

            Editor e1 = sharedPrefs.edit();
            e1.putInt("chatpoint", Constant.totalchat);
            e1.commit();

            for (int i = 0; i < words.length; i++) {

                for (int j = 0; j < Constant.chatdeel.size(); j++) {

                    String wor = words[i].toLowerCase();
                    String chk = Constant.chatdeel.get(j).toString()
                            .toLowerCase();

                    Constant.printMsg("onclcik 1" + wor + " name " + chk
                            + "   size" + Constant.chatdeel.size());

                    if (wor.equals(chk)) {

                        Constant.blinkoffer.add(wor);

                        Constant.printMsg("onclcik 2 ch" + menu);

                        if (menu != null) {
                            item = menu.findItem(R.id.menu_star);

                            Constant.printMsg("onclcik 3 ch" + item);

                            if (item != null) {

                                Constant.printMsg("onclcik 4 ch");

                                item.setVisible(true);

                            }

                        }
                    }
                }

            }

            if (Constant.mDesTFromSlider == true) {
                Constant.mDesTFromSlider = false;
                Constant.mself_destruct_msg = edt_msg.getText().toString()
                        .trim();
                Constant.mself_jid = jid;
                if (Constant.mself_destruct_msg.toString().length() != 0) {

                    int points = sharedPrefs.getInt("destpoint", 0);

                    Constant.totaldest = points;

                    Constant.totaldest = Count + Constant.totaldest;
                    dest_msg_list.add(Constant.mTimeFromSlider);
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

                    Constant.printMsg("test desTTTTTTTT ::::::::::>>>>>>>1111 " + Constant.mself_jid);
                    mDest = true;


                    sendMessage("<s>" + Constant.mTimeFromSlider + "-"
                            + Constant.mself_destruct_msg + "-"
                            + Constant.mself_jid);
                    if (!GlobalBroadcast.isServiceRunning(DestructService.class.getCanonicalName(), mParentActivity))
                        startService(new Intent(mParentActivity, DestructService.class));
                    Constant.printMsg("msg:::" + Constant.mself_destruct_msg
                            + "time::::"
                            + Constant.mTimeFromSlider +
                            "    " + Constant.mself_jid);
                }
            } else {

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
                                                    + mFinalNymsMeaningLength
                                                    .get(j).toString()
                                                    .length()
                                                    + "   "
                                                    + (mFinalNyms.get(j)
                                                    .toString()
                                                    .length() - mFinalNymsMeaningLength
                                                    .get(j).toString()
                                                    .length())
                                                    + "   "
                                                    + mFinalNyms.get(j)
                                                    .toString()
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
                                                    (mFinalNymsMeaning
                                                            .get(j)
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
            }
            selectedtext.clear();
            Constant.printMsg("SSSSSSS" + edt_msg.getText().toString());
            nymcount = 0;
        }
        else
        {
            voice_dialog();
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

    // Media count Neetwork usage
    public void updateMesageCountForMedia() {
        try {
            int sent_msg_count = pref.getInt("sent_msg_count", 0);
            Constant.printMsg("sent_msg_count msg media::" + sent_msg_count);
            sent_msg_count = sent_msg_count + 1;
            Constant.printMsg("sent_msg_count msg countafter added media is::"
                    + sent_msg_count);
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

    @Override
    public void addAsyncStanzaListener(StanzaListener arg0, StanzaFilter arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addConnectionListener(ConnectionListener arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addOneTimeSyncCallback(StanzaListener arg0, StanzaFilter arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addPacketInterceptor(StanzaListener arg0, StanzaFilter arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    @Deprecated
    public void addPacketListener(StanzaListener arg0, StanzaFilter arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addPacketSendingListener(StanzaListener arg0, StanzaFilter arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addSyncStanzaListener(StanzaListener arg0, StanzaFilter arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public PacketCollector createPacketCollector(StanzaFilter arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PacketCollector createPacketCollector(Configuration arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PacketCollector createPacketCollectorAndSend(IQ arg0)
            throws NotConnectedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PacketCollector createPacketCollectorAndSend(StanzaFilter arg0,
                                                        Stanza arg1) throws NotConnectedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getConnectionCounter() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public <F extends ExtensionElement> F getFeature(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FromMode getFromMode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setFromMode(FromMode arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getHost() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getLastStanzaReceived() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getPacketReplyTimeout() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setPacketReplyTimeout(long arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getPort() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getServiceName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getStreamId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUser() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasFeature(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAnonymous() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAuthenticated() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isConnected() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSecureConnection() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isUsingCompression() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public IQRequestHandler registerIQRequestHandler(IQRequestHandler arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean removeAsyncStanzaListener(StanzaListener arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeConnectionListener(ConnectionListener arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removePacketCollector(PacketCollector arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removePacketInterceptor(StanzaListener arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    @Deprecated
    public boolean removePacketListener(StanzaListener arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removePacketSendingListener(StanzaListener arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean removeSyncStanzaListener(StanzaListener arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void send(PlainStreamElement arg0) throws NotConnectedException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendIqWithResponseCallback(IQ arg0, StanzaListener arg1)
            throws NotConnectedException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendIqWithResponseCallback(IQ arg0, StanzaListener arg1,
                                           ExceptionCallback arg2) throws NotConnectedException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendIqWithResponseCallback(IQ arg0, StanzaListener arg1,
                                           ExceptionCallback arg2, long arg3) throws NotConnectedException {
        // TODO Auto-generated method stub

    }

    @Override
    @Deprecated
    public void sendPacket(Stanza arg0) throws NotConnectedException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendStanza(Stanza arg0) throws NotConnectedException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendStanzaWithResponseCallback(Stanza arg0, StanzaFilter arg1,
                                               StanzaListener arg2) throws NotConnectedException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendStanzaWithResponseCallback(Stanza arg0, StanzaFilter arg1,
                                               StanzaListener arg2, ExceptionCallback arg3)
            throws NotConnectedException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendStanzaWithResponseCallback(Stanza arg0, StanzaFilter arg1,
                                               StanzaListener arg2, ExceptionCallback arg3, long arg4)
            throws NotConnectedException {
        // TODO Auto-generated method stub

    }

    @Override
    public IQRequestHandler unregisterIQRequestHandler(IQRequestHandler arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IQRequestHandler unregisterIQRequestHandler(String arg0,
                                                       String arg1, Type arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Down Arrow button clicked functionality
     */
    public void downArrowClickAction() {


        try {


            if (emoji_frag.getVisibility() == View.VISIBLE) {
                emoji_frag.setVisibility(View.GONE);
                btn_emo.setImageResource(R.drawable.emoji_btn_normal);

            }

            LinearLayout linearLayoutPopup = (LinearLayout) findViewById(R.id.pop_up);
            LinearLayout mdazzle = (LinearLayout) findViewById(R.id.dazzle_btn_lay);
            LinearLayout mbazzle = (LinearLayout) findViewById(R.id.bazzle_btn_lay);
            LinearLayout mkons = (LinearLayout) findViewById(R.id.kons_btn_lay);
            LinearLayout autodes = (LinearLayout) findViewById(R.id.auto_btn_lay);
            LinearLayout mnymn = (LinearLayout)
                    findViewById(R.id.nyms_btn_lay);

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

                    Intent intent = new Intent(mParentActivity,
                            NynmActivity.class);
                    startActivity(intent);

                    // sendMessage(mParseString);
                }

            });

            autodes.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (edt_msg.getText().toString().length() != 0) {
                        if (Connectivity.isOnline(mParentActivity)) {
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

                }

            });

            mdazzle.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // mDownArrow.setImageResource(R.drawable.rt_arrow);

                    if(mPopup){
                        downArrowClickAction();
                    }

                    //                if (edt_msg.getText().toString().equalsIgnoreCase("")) {
                    //                    Toast.makeText(getApplicationContext(),
                    //                            "Please Enter some text",
                    //                            Toast.LENGTH_SHORT).show();
                    //                } else {

                    Constant.mChatText = "";

                    try {
                        String[] words = edt_msg.getText().toString().split("\\s+");
                        if (mMessegeList.size() > 0) {
                            for (int i = 0; i < words.length; i++) {
                                for (int j = 0; j < mMessegeList.size(); j++) {

                                    try {

                                        if (mMessegeList.get(j).toString().substring(0, mMessegeList.get(j).toString().length() - 1).equalsIgnoreCase(words[i].substring(0, words[i].length() - 1))) {

                                            Constant.mChatText += words[i].substring(0, words[i].length() - 1) + " ";

                                        } else {
                                            Constant.mChatText += words[i] + " ";
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else {
                            Constant.mChatText = edt_msg.getText().toString();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(mParentActivity,
                            DazzPlainActivity.class);
                    startActivity(intent);
                    //                }

                 //   mPopup = false;

                }
            });
            mbazzle.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // mDownArrow.setImageResource(R.drawable.rt_arrow);

                    if(mPopup){
                        downArrowClickAction();
                    }
                    Intent intentka = new Intent(mParentActivity,
                            KaraokeListActivity.class);
                    startActivity(intentka);

                   // mPopup = false;

                }
            });

            mkons.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    // mDownArrow.setImageResource(R.drawable.rt_arrow);
                    if(mPopup){
                        downArrowClickAction();
                    }

                    Constant.mKonsFromChat = true;
                    Intent intent = new Intent(mParentActivity,
                            KonsHomeScreen.class);
                    startActivity(intent);

                 //   mPopup = false;

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
                mChatFooterSlideMenuImg.startAnimation(anim);

                TranslateAnimation anim1 = new TranslateAnimation(-(width * 75 / 100), 0, 0, 0);
                anim1.setDuration(500); // 1000 ms = 1second

                anim1.setAnimationListener(new AnimationListener() {
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


//                popupWindow.setAnimationStyle(R.style.AnimationPopup);
//
//                popupWindow.showAsDropDown(mChatFooterSlideMenuImg, -2, 60);

                // popupWindow.showAtLocation(mDownArrow, Gravity.BOTTOM,
                // 10,
                // 24);
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
                mChatFooterSlideMenuImg.startAnimation(anim);

                TranslateAnimation anims = new TranslateAnimation(0, -(width * 75 / 100), 0, 0);
                anims.setDuration(500); // 1000 ms = 1second

                anims.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        // Functionality here

                        mSliderMenuLayout.layout(mSliderMenuLayout.getLeft() - (width * 75 / 100),
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
        } catch (Exception e) {
            Constant.printMsg("sdlhldasldaslkd1111" + e.toString());
        }

    }

    public void mIntVariable() {

        try {
            // Chat Hedder Views initalization
            mChatHedderBackImg = (ImageView) findViewById(R.id.chat_hedder_back_img);
            mChatHedderProfileImg = (ImageView) findViewById(R.id.chat_hedder_profile_img);
            mChatHedderCopyImg = (ImageView) findViewById(R.id.chat_hedder_copy_img);
            mChatHedderAttachmentImg = (ImageView) findViewById(R.id.chat_hedder_attachment_img);
            mChatHedderMenuImg = (ImageView) findViewById(R.id.chat_hedder_menu_img);
            mToolTipImg = (ImageView) findViewById(R.id.tool_tip_img);

            mChatHedderUserTxt = (TextView) findViewById(R.id.chat_hedder_user_txt);
            mChatHedderUserStatusTxt = (TextView) findViewById(R.id.chat_hedder_user_status_txt);
            mChatHedderUserTxt.setTextColor(Color.parseColor("#ffffff"));
            mChatHedderUserStatusTxt.setTextColor(Color.parseColor("#ffffff"));
            mChatHedderUserTxt.setTypeface(null, Typeface.BOLD);

            mChatTypingTxt = (EditText) findViewById(R.id.typing_msg);
//            mChatTypingTxt.setEnabled(false);
            mChatTypingTxt.setMovementMethod(new ScrollingMovementMethod());
//            mChatTypingTxt.setedFocusable(false);
//            mChatTypingTxt.setFocusableInTouchMode(false);



            mchatHeadBackLayout = (LinearLayout) findViewById(R.id.mchatlayoutHeadBack);
            mChatHedderLayout = (LinearLayout) findViewById(R.id.chat_hedder_layout);
            mChatHedderTextLayout = (LinearLayout) findViewById(R.id.chat_hedder_text_layout);
            mChatHedderCopyLayout = (LinearLayout) findViewById(R.id.chat_hedder_copy_layout);
            mChatHedderAttachmentLayout = (LinearLayout) findViewById(R.id.chat_hedder_attachment_layout);
            mChatHedderMenuLayout = (LinearLayout) findViewById(R.id.chat_hedder_menu_layout);
            mChatHedderCopyLayout.setVisibility(View.GONE);

            // Chat Footer Views initalization
            mChatFooterLayout = (LinearLayout) findViewById(R.id.ll_chat);
            mDownArrowLayout = (LinearLayout) findViewById(R.id.btn_down_arrow_grp_layout);
            mChatFooterEdittextLayout = (LinearLayout) findViewById(R.id.chat_footer_edittext_layout);
            mChatTypingMsgLayout = (LinearLayout) findViewById(R.id.chat_typing_msg_layout);

            mChatFooterSlideMenuImg = (ImageView) findViewById(R.id.btn_down_arrow_grp);
            mChatFooterEmojiconsImg = (ImageView) findViewById(R.id.btn_emo);

            mChatFooterEdittext = (EmojiconEditText) findViewById(R.id.edt_messagegrp);
            mChatFooterEdittext.requestFocus();

            mChatFooterSendBtn = (RounderImageView) findViewById(R.id.sendButton);


            mDynamicView = (LinearLayout) findViewById(R.id.mdynamicView);
            mScrollView = (ScrollView) findViewById(R.id.scroll_view);

            mBlockContact = (TextView) findViewById(R.id.tv_block);
            mAddContact = (TextView) findViewById(R.id.tv_add_contact);

            mContactLayout = (LinearLayout) findViewById(R.id.contact_layout);
            mContactLayout.setVisibility(View.GONE);

            mSliderMenuLayout = (LinearLayout) findViewById(R.id.slider_menu_layout);
            mSliderMenuLayout.setVisibility(View.GONE);

        } catch (Exception e) {

        }
    }

    public void mScreenArrangement() {

        try {
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

            mChatFooterLayout.post(new Runnable() {
                @Override
                public void run() {
                    //maybe also works height = ll.getLayoutParams().height;
                    mTempLayoutHeight = mChatFooterLayout.getHeight();
                    LinearLayout.LayoutParams listviewParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    listviewParams.width = width;
//                    listviewParams.topMargin = height * 8 / 100;
                    listviewParams.gravity = Gravity.BOTTOM|Gravity.BOTTOM;
//                    listviewParams.bottomMargin = mChatFooterLayout.getHeight() + 2;
                    listview.setLayoutParams(listviewParams);
                    listview.scrollTo(0, listview.getHeight());
                    listMeaning.setLayoutParams(listviewParams);
                    // mChatTypingTxt.setLayoutParams(listviewParams);
                }
            });

            FrameLayout.LayoutParams mScrollViewParams = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            mScrollViewParams.topMargin = height * 8 / 100;
            mScrollViewParams.bottomMargin = height * 11 / 100;
            mScrollView.setLayoutParams(mScrollViewParams);

            if (width >= 600)
            {
                mChatHedderUserTxt.setTextSize(16);
                mChatHedderUserStatusTxt.setTextSize(14);
            }
            else if (width > 501 && width < 600)
            {
                mChatHedderUserTxt.setTextSize(14);
                mChatHedderUserStatusTxt.setTextSize(12);
            }
            else if (width > 260 && width < 500)
            {
                mChatHedderUserTxt.setTextSize(13);
                mChatHedderUserStatusTxt.setTextSize(11);
            }
            else if (width <= 260)
            {
                mChatHedderUserTxt.setTextSize(12);
                mChatHedderUserStatusTxt.setTextSize(11);
            }
        } catch (Exception e) {

        }

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

                slide_down.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        mChatMenuLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

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
            mChatOptionMenuMirrorchatLayout = (LinearLayout) findViewById(R.id.chat_option_menu_mirrorchat_layout);

            mChatOptionMenuViewcontactImg = (ImageView) findViewById(R.id.chat_option_menu_viewcontact_img);
            mChatOptionMenuBlockImg = (ImageView) findViewById(R.id.chat_option_menu_block_img);
            mChatOptionMenuCallImg = (ImageView) findViewById(R.id.chat_option_menu_call_img);
            mChatOptionMenuLockchatImg = (ImageView) findViewById(R.id.chat_option_menu_lockchat_img);
            mChatOptionMenuClearchatImg = (ImageView) findViewById(R.id.chat_option_menu_clearchat_img);
            mChatOptionMenuMirrorchatImg = (ImageView) findViewById(R.id.chat_option_menu_mirrorchat_img);

            mChatOptionMenuViewcontactTxt = (TextView) findViewById(R.id.chat_option_menu_viewcontact_txt);
            mChatOptionMenuBlockTxt = (TextView) findViewById(R.id.chat_option_menu_block_txt);
            mChatOptionMenuCallTxt = (TextView) findViewById(R.id.chat_option_menu_call_txt);
            mChatOptionMenuLockchatTxt = (TextView) findViewById(R.id.chat_option_menu_lockchat_txt);
            mChatOptionMenuClearchatTxt = (TextView) findViewById(R.id.chat_option_menu_clearchat_txt);
            mChatOptionMenuMirrorchatText = (TextView) findViewById(R.id.chat_option_menu_mirrorchat_txt);

            LinearLayout.LayoutParams hedderMenuParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            hedderMenuParams.width = width * 40 / 100;
            hedderMenuParams.height = height * 38 / 100;
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
            mChatOptionMenuMirrorchatLayout.setLayoutParams(hedderMenuLayoutParams);

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
            mChatOptionMenuMirrorchatImg.setLayoutParams(hedderMenuImgParams);

            mChatOptionMenuHedderLayout.setX(width * 29 / 100);
            mChatOptionMenuHedderLayout.setY(height * 8 / 100);
            mChatOptionMenuLayout.setVisibility(View.VISIBLE);

            if (width >= 600) {

                mChatOptionMenuViewcontactTxt.setTextSize(14);
                mChatOptionMenuBlockTxt.setTextSize(14);
                mChatOptionMenuCallTxt.setTextSize(14);
                mChatOptionMenuLockchatTxt.setTextSize(14);
                mChatOptionMenuClearchatTxt.setTextSize(14);
                mChatOptionMenuMirrorchatText.setTextSize(14);

            } else if (width > 501 && width < 600) {

                mChatOptionMenuViewcontactTxt.setTextSize(12);
                mChatOptionMenuBlockTxt.setTextSize(12);
                mChatOptionMenuCallTxt.setTextSize(12);
                mChatOptionMenuLockchatTxt.setTextSize(12);
                mChatOptionMenuClearchatTxt.setTextSize(12);
                mChatOptionMenuMirrorchatText.setTextSize(12);

            } else if (width > 260 && width < 500) {

                mChatOptionMenuViewcontactTxt.setTextSize(11);
                mChatOptionMenuBlockTxt.setTextSize(11);
                mChatOptionMenuCallTxt.setTextSize(11);
                mChatOptionMenuLockchatTxt.setTextSize(11);
                mChatOptionMenuClearchatTxt.setTextSize(11);
                mChatOptionMenuMirrorchatText.setTextSize(11);

            } else if (width <= 260) {

                mChatOptionMenuViewcontactTxt.setTextSize(10);
                mChatOptionMenuBlockTxt.setTextSize(10);
                mChatOptionMenuCallTxt.setTextSize(10);
                mChatOptionMenuLockchatTxt.setTextSize(10);
                mChatOptionMenuClearchatTxt.setTextSize(10);
                mChatOptionMenuMirrorchatText.setTextSize(10);

            }

            if (KachingMeApplication.getBlocked_user().contains(jid)) {
                mChatOptionMenuBlockTxt.setText(getResources().getString(R.string.unblock));

                UserBlocked = true;
            } else {
                mChatOptionMenuBlockTxt.setText(getResources().getString(R.string.block));
                UserBlocked = false;
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

                slide_down.setAnimationListener(new AnimationListener() {
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


            checkMirrorAtLoading();
        } catch (Resources.NotFoundException e) {

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
            }
        });
        mChatMenuAudioImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
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
            }
        });
        mChatMenuAudioLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                startActivity(new Intent(mParentActivity, SongList.class));
            }
        });
    }

    /**
     * Chat Menu items are clicked
     */
    public void chatOptionOnClickListeners() {

        // Button click
        mChatOptionMenuViewcontactImg.setOnClickListener(new OnClickListener() {
            Intent intent;

            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                viewUserProfile();
            }
        });
        mChatOptionMenuBlockImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                block_or_unblock();
            }
        });
        mChatOptionMenuCallImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                callUser();
            }
        });
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
        mChatOptionMenuMirrorchatImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                mirrorChat();
            }
        });


        //Layout click
        mChatOptionMenuViewcontactLayout.setOnClickListener(new OnClickListener() {
            Intent intent;

            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                viewUserProfile();
            }
        });
        mChatOptionMenuBlockLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                block_or_unblock();
            }
        });
        mChatOptionMenuCallLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                callUser();
            }
        });
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
        mChatOptionMenuMirrorchatLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMenuHideFunction();
                mirrorChat();
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


                            int iSize = msg_list.size();
                            dialog.cancel();
                            dbAdapter.setDeleteMessages_Non_secret_chat(jid);

                            clearDataList();

                            if(iSize>0)
                            updateForHomeScreenList();

                            dest_list = new ArrayList<Integer>();
                            dest_list_msgids = new ArrayList<Integer>();
                            dest_list_bombids = new ArrayList<Integer>();
                            dest_list_anim = new ArrayList<Integer>();
                            mDynamicView = (LinearLayout) findViewById(R.id.mdynamicView);
                            //  new FetchChat().execute();
                            k = 0;
                        }
                    });

            AlertDialog alert = b.create();
            alert.show();
        } catch (Resources.NotFoundException e) {

        }
    }

    public void clearDataList() {
        ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
    }

    /**
     * Lock the chat room
     */
    public void lockChat() {
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
     * Block or unblock the user...
     */
    public void block_or_unblock() {
        try {
            if (KachingMeApplication.getIsNetAvailable()) {


                if (!UserBlocked) {
                    Log.d("Optionmenu", "Block called");

//                    mBlockContact.setText(getResources().getString(R.string.unblock));
                    new Async_Privacy().execute();
                } else {
                    Log.d("Optionmenu", "Unblock called");
                          /* new unblock().execute(); */
                    unblock();
                    Log.d("Optionmenu", "Unblock called2");
                }
            } else {

                new AlertUtils().Toast_call(
                        mParentActivity,
                        getResources().getString(
                                R.string.no_internet_connection));
            }
        } catch (Resources.NotFoundException e) {

        }
    }

    /**
     * To see the User Profile
     */
    public void viewUserProfile() {

        Intent intent = new Intent(mParentActivity, UserProfile.class);
        intent.putExtra("jid", jid);
        intent.putExtra("name", home_title);
        startActivity(intent);
    }

    /**
     * Video recording start
     */
    public void startVideoRecording() {
        try {
            // image_picker(2);
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = Utils.getOutputMediaFileUri(1);
        // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image
        // file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    /**
     * Share location
     */
    public void locationShare() {
        Intent intent = new Intent(this, LocationShare.class);
        startActivityForResult(intent, 44);
    }



    public void mAddMethod(Intent intent) {

        mParentActivity.startActivityForResult(intent, REQUEST_CODE_ADD_CONTACT);

    }

    public void callOnlineStatus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    getJidOnlineStatus(jid);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void checkMirrorAtLoading() {
        final Boolean isMirrorEnabled = sp.getBoolean(jid + "_mirror", false);

        if (isMirrorEnabled) {
            mIsMirrorEnabled = true;
            mChatTypingMsgLayout.setVisibility(View.VISIBLE);
            mChatOptionMenuMirrorchatText.setText("Disable Mirror");
        } else {
            mIsMirrorEnabled = false;
            mChatOptionMenuMirrorchatText.setText("Enable Mirror");
        }

    }

    public void mirrorChat() {
        try {
            String mirrorTxt = "Mirror chat is the feature, that user can see the other user typing messages before it's going to send, this feature can access only both the users should enable the 'Miror chat'. \n" +
                    "Are you sure you want to enable mirror chat ?";


            final Boolean isMirrorEnabled = sp.getBoolean(jid + "_mirror", false);

            if (isMirrorEnabled) {
                mirrorTxt = "Are you sure you want to disable mirror chat ?";
            }


            AlertDialog.Builder b = new AlertDialog.Builder(mParentActivity);
            b.setMessage(mirrorTxt)
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

                            if (isMirrorEnabled) {
                                mIsMirrorEnabled = false;
                                ed.putBoolean(jid + "_mirror", false);
                                ed.commit();
                                mChatOptionMenuMirrorchatText.setText("Enable Mirror");
                                mChatTypingMsgLayout.setVisibility(View.GONE);
                            } else {
                                mIsMirrorEnabled = true;
                                ed.putBoolean(jid + "_mirror", true);
                                ed.commit();
                                mChatOptionMenuMirrorchatText.setText("Disable Mirror");
                                mChatTypingMsgLayout.setVisibility(View.VISIBLE);

                            }
                        }
                    });

            AlertDialog alert = b.create();
            alert.show();
        } catch (Resources.NotFoundException e) {

        }
    }

    public void checkDestMsg(MessageGetSet msg) {

        try {
            String mTempParse = null;
            Constant.printMsg("MSGID" + msg.get_id());
            if (msg.getData() != null) {
                mTempParse = msg.getData().toString();

                if (mTempParse.length() > 3) {


                    char s = mTempParse.charAt(0);
                    char s1 = mTempParse.charAt(1);
                    char s2 = mTempParse.charAt(2);

                    if (s == '<' && s1 == 's' && s2 == '>') {

                        if (!GlobalBroadcast.isServiceRunning(DestructService.class.getCanonicalName(), mParentActivity))
                            startService(new Intent(mParentActivity, DestructService.class));

                        String self_destruct = mTempParse.substring(3)
                                .toString();
                        String[] parts = self_destruct.split("-");
                        String part1 = parts[0];
                        String part2 = parts[1];

                        ChatTest.dest_list.add(part1.toString());
                        ChatTest.dest_list_msgids.add(msg.get_id());
                        ChatTest.dest_list_bombids.add(R.drawable.black_bomb);
                        ChatTest.dest_list_anim.add(0);

                    } else {
                        dest_list.add(0);
                        dest_list_msgids.add(msg.get_id());
                        dest_list_anim.add(0);
                        dest_list_bombids.add(R.drawable.black_bomb);
                    }
                } else {
                    dest_list.add(0);
                    dest_list_msgids.add(msg.get_id());
                    dest_list_anim.add(0);
                    dest_list_bombids.add(R.drawable.black_bomb);
                }
            } else {
                dest_list.add(0);
                dest_list_msgids.add(msg.get_id());
                dest_list_anim.add(0);
                dest_list_bombids.add(R.drawable.black_bomb);
            }
        } catch (Exception e) {

        }

    }

    public void fetchNymFrom() {
        try {
            SQLException e;
            Throwable th;
            Dbhelper dbhelper = new Dbhelper(getApplicationContext());
            Cursor c = null;
            ChatDictionary.mDictionaryList.clear();
            ChatDictionary.mDictionaryMeaningList.clear();

            try {
                String query = "SELECT * FROM nym ORDER BY name ";
                Dbhelper db = new Dbhelper(getApplicationContext());
                try {
                    c = db.open().getDatabaseObj().rawQuery(query, null);
                    int idIndex = c.getColumnIndex("id");
                    int txnm = c.getColumnIndex(Contacts.PeopleColumns.NAME);
                    int mnnm = c.getColumnIndex("meaning");
                    Constant.printMsg("The pending cart list in db ::::"
                            + c.getCount());
                    if (c.getCount() > 0) {
                        while (c.moveToNext()) {
                            String tx = c.getString(txnm);
                            String mn = c.getString(mnnm);
                            Integer idnm = Integer.valueOf(c.getInt(idIndex));
                            System.out
                                    .println("dbadd:nym:" + tx + "  " + mn + "  ");
                            ChatDictionary.mDictionaryList.add(tx);
                            ChatDictionary.mDictionaryMeaningList.add(mn);
                        }
                    } else {
                        Constant.printMsg("there is nothing in db::");
                    }
                    c.close();
                    db.close();
                    dbhelper = db;
                } catch (SQLException e2) {
                    e = e2;
                    dbhelper = db;
                } catch (Throwable th2) {
                    th = th2;
                    dbhelper = db;
                }
            } catch (SQLException e3) {
                e = e3;
                try {
                    Constant.printMsg("Sql exception in pending shop details ::::"
                            + e.toString());
                    c.close();
                    dbhelper.close();
                } catch (Throwable th3) {
                    th = th3;
                    c.close();
                    dbhelper.close();

                }
            }
        } catch (Exception e1) {

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Constant.printMsg("ontouch");
        topMenuHideFunction();
        return super.onTouchEvent(event);
    }

    /**
     * Seding recording status to other member
     */
    public void presence_recording() {
        timer = new Timer();
        // This timer task will be executed every 1 sec.
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message(jid, Message.Type.chat);
                msg.setStanzaId(Constant.STATUS_RECORDING);

                msg.setBody(Constant.STATUS_RECORDING);

                try {

                    TempConnectionService.connection.sendStanza(msg);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 0, 1000);
    }

    public int valueText()
    {
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String textSizePref = pref.getString("pref_font_size", "16");
        int textSize = Integer.valueOf(textSizePref);
        Constant.printMsg(" Screen Width :  " + width + " Pref Text Size " + textSizePref + " Right " + textSize);
        return textSize;
    }

    void rightTextChat() {

        try {
            mRightTextview = new EmojiconTextView(mParentActivity);
            mRightTextview.setGravity(Gravity.LEFT);
            mRightTextview.setTextColor(Color.parseColor("#000000"));
            mRightTextview.setPadding((int) width * 3 / 100, width * 1 / 100, (int) width * 3 / 100, width * 2 / 100);
            mRightTextview.setId(k);

            mRightSenderTimeText = new TextView(mParentActivity);
            mRightSenderTimeText.setGravity(Gravity.LEFT);
            mRightSenderTimeText.setTextSize(getTimeTxtSize());
            mRightSenderTimeText.setTextColor(Color.parseColor("#000000"));
            mRightSenderTimeText.setPadding((int) width * 1 / 100, 0, (int) width * 3 / 100, width * 1 / 100);

            LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mRightTextview.setLayoutParams(textviewParams);
            mRightSenderTimeText.setLayoutParams(textviewParams);

            LinearLayout textFooterLayout = new LinearLayout(mParentActivity);
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

            mRightTickMark = new ImageView(mParentActivity);
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

            mRightDestTime = new TextView(mParentActivity);
            mRightDestTime.setBackgroundResource(R.drawable.black_bomb);
            mRightDestTime.setGravity(Gravity.CENTER);
            mRightDestTime.setTextColor(Color.parseColor("#ffffff"));
            mRightDestTime.setLayoutParams(right_bomb_params);
            mRightDestTime.setVisibility(View.INVISIBLE);
            int poscount=k + 100000;
            mRightDestTime.setId(poscount);

            Constant.printMsg("DEEEEESSSSSTTTTT1 "+poscount);

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

            Constant.printMsg("Set Text " + e.toString());
        }
    }

    void rightImageChat() {

        Constant.printMsg("Chat Image....  design");

        try {
            mRightImageChat = new ImageView(mParentActivity);
            mRightImageChat.setPadding((int) width * 3 / 100, 0, (int) width * 1 / 100, width * 2 / 100);
            mRightImageChat.setScaleType(ImageView.ScaleType.FIT_XY);

            mRightImageTextTime = new TextView(mParentActivity);
            mRightImageTextTime.setGravity(Gravity.RIGHT);
            mRightImageTextTime.setTextSize(getTimeTxtSize());
            mRightImageTextTime.setTextColor(Color.parseColor("#000000"));
            mRightImageTextTime.setPadding((int) width * 1 / 100, 0, (int) width * 3 / 100, width * 1 / 100);
            //        mRightSenderTimeText.setText(time);

            FrameLayout.LayoutParams textviewParams = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textviewParams.width = width * 63 / 100;
            textviewParams.height = height * 30 / 100;
            textviewParams.topMargin = width * 2 / 100;
            textviewParams.rightMargin =  width * 1 / 100;
            textviewParams.gravity = Gravity.CENTER;
            mRightImageChat.setLayoutParams(textviewParams);
            //        mRightSenderTimeText.setLayoutParams(textviewParams);

            FrameLayout ImachChatLayout = new FrameLayout(mParentActivity);

            mRightImageChatUpload = new ImageView(mParentActivity);
            mRightImageChatUpload.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mRightImageChatUpload.setId(k + 800000);
            mRightImageChatUpload.setBackgroundResource(R.drawable.image_upload_normal);

            mRightImageChatCancel = new ImageView(mParentActivity);
            mRightImageChatCancel.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mRightImageChatCancel.setId(k + 120000);
            mRightImageChatCancel.setBackgroundResource(R.drawable.ic_action_content_remove);

            mRightImageProgress = new ProgressBar(mParentActivity);
            mRightImageProgress.setId(k+900000);


            FrameLayout.LayoutParams left_download_icon_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            left_download_icon_params.width = (int) width * 15 / 100;
            left_download_icon_params.height = (int) width * 15 / 100;
            left_download_icon_params.gravity = Gravity.CENTER;
            mRightImageChatUpload.setLayoutParams(left_download_icon_params);
            mRightImageChatCancel.setLayoutParams(left_download_icon_params);

            FrameLayout.LayoutParams left_upload_icon_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            left_upload_icon_params.width = (int) width * 16 / 100;
            left_upload_icon_params.height = (int) width * 16 / 100;
            left_upload_icon_params.gravity = Gravity.CENTER;
            mRightImageProgress.setLayoutParams(left_upload_icon_params);


            ImachChatLayout.addView(mRightImageChat);
            ImachChatLayout.addView(mRightImageProgress);
            ImachChatLayout.addView(mRightImageChatUpload);
            ImachChatLayout.addView(mRightImageChatCancel);


            LinearLayout textFooterLayout = new LinearLayout(mParentActivity);
            textFooterLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams textfooterParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textFooterLayout.setLayoutParams(textfooterParams);

            LinearLayout.LayoutParams mRightTickMarkParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mRightTickMarkParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

            mRightImageTickMark = new ImageView(mParentActivity);
            mRightImageTickMark.setId(k + 600000);
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

    void rightVideoChat() {

        try {
            mRightVideoChat = new ImageView(mParentActivity);
            mRightVideoChat.setPadding((int) width * 1 / 100, 0, (int) width * 1 / 100, width * 2 / 100);
            mRightVideoChat.setScaleType(ImageView.ScaleType.CENTER_CROP);

            mRightVideoTimeText = new TextView(mParentActivity);
            mRightVideoTimeText.setGravity(Gravity.RIGHT);
            mRightVideoTimeText.setTextSize(getTimeTxtSize());
            mRightVideoTimeText.setTextColor(Color.parseColor("#000000"));
            mRightVideoTimeText.setPadding((int) width * 1 / 100, width * 1 / 100, (int) width * 3 / 100, width * 1 / 100);
            //        mRightSenderTimeText.setText(time);


            FrameLayout.LayoutParams mLeftVideoChatParams = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftVideoChatParams.width = width * 59 / 100;
            mLeftVideoChatParams.height = height * 30 / 100;
            mLeftVideoChatParams.topMargin = width * 2 / 100;
            mLeftVideoChatParams.gravity = Gravity.CENTER;
            mRightVideoChat.setLayoutParams(mLeftVideoChatParams);
            //        mRightSenderTimeText.setLayoutParams(textviewParams);

            FrameLayout mLeftVideoChatLayout = new FrameLayout(mParentActivity);

            mRightVideoChatUpload = new ImageView(mParentActivity);
            mRightVideoChatUpload.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mRightVideoChatUpload.setBackgroundResource(R.drawable.image_upload_normal);

            mRightVideoChatCancel = new ImageView(mParentActivity);
            mRightVideoChatCancel.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mRightVideoChatCancel.setBackgroundResource(R.drawable.ic_action_content_remove);

            mRightVideoButtonPlay = new ImageView(mParentActivity);
            mRightVideoButtonPlay.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mRightVideoButtonPlay.setBackgroundResource(R.drawable.videooverlay);


            mRightVideoProgress = new ProgressBar(mParentActivity);

            FrameLayout.LayoutParams left_download_icon_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            left_download_icon_params.width = (int) width * 15 / 100;
            left_download_icon_params.height = (int) width * 15 / 100;
            left_download_icon_params.gravity = Gravity.CENTER;
            mRightVideoChatUpload.setLayoutParams(left_download_icon_params);
            mRightVideoChatCancel.setLayoutParams(left_download_icon_params);
       //     mRightVideoProgress.setLayoutParams(left_download_icon_params);
            mRightVideoButtonPlay.setLayoutParams(left_download_icon_params);

            FrameLayout.LayoutParams left_upload_icon_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            left_upload_icon_params.width = (int) width * 16 / 100;
            left_upload_icon_params.height = (int) width * 16 / 100;
            left_upload_icon_params.gravity = Gravity.CENTER;
            mRightVideoProgress.setLayoutParams(left_upload_icon_params);


            FrameLayout.LayoutParams videoFooter_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            videoFooter_params.gravity = Gravity.BOTTOM | Gravity.CENTER_VERTICAL;
            videoFooter_params.topMargin = width * 3 / 100;

            FrameLayout videoFooter = new FrameLayout(mParentActivity);
            videoFooter.setLayoutParams(videoFooter_params);

            FrameLayout.LayoutParams videoimage = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            videoimage.width = width * 10 / 100;
            videoimage.height = width * 8 / 100;
            videoimage.topMargin = width * 1 / 100;
            videoimage.gravity = Gravity.CENTER_VERTICAL;
            videoimage.leftMargin = width * 3 / 100;

            mRightVideoImgOverlay = new ImageView(mParentActivity);
            mRightVideoImgOverlay.setBackgroundResource(R.drawable.frame_overlay_gallery_video);
            mRightVideoImgOverlay.setLayoutParams(videoimage);

            FrameLayout.LayoutParams videoDuration_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            videoDuration_params.gravity = Gravity.LEFT;
            videoDuration_params.topMargin = width * 3 / 100;
            videoDuration_params.leftMargin = width * 12 / 100;

            mRightVideoDuration = new TextView(mParentActivity);
            mRightVideoDuration.setTextColor(Color.parseColor("#ffffff"));
            mRightVideoDuration.setText("1");
            mRightVideoDuration.setLayoutParams(videoDuration_params);

            FrameLayout.LayoutParams videoSize_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            videoSize_params.gravity = Gravity.RIGHT;
            videoSize_params.topMargin = width * 3 / 100;
            videoSize_params.rightMargin = width * 5 / 100;


            mRightVideoSize = new TextView(mParentActivity);
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

            LinearLayout textFooterLayout = new LinearLayout(mParentActivity);
            textFooterLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams textfooterParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textFooterLayout.setLayoutParams(textfooterParams);

            LinearLayout.LayoutParams mRightTickMarkParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mRightTickMarkParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

            mRightVideoTickMark = new ImageView(mParentActivity);
            mRightVideoTickMark.setId(k + 600000);
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

    void rightContactChat() {

        try {
            LinearLayout.LayoutParams contactLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            contactLayoutParams.width = width * 50 / 100;
            contactLayoutParams.height = height * 8 / 100;
            contactLayoutParams.topMargin = width * 2 / 100;
            contactLayoutParams.gravity = Gravity.CENTER;

            LinearLayout contactLayout = new LinearLayout(mParentActivity);
            contactLayout.setOrientation(LinearLayout.HORIZONTAL);
            contactLayout.setBackgroundColor(Color.parseColor("#80ffffff"));
            contactLayout.setLayoutParams(contactLayoutParams);

            LinearLayout.LayoutParams contactAvathorParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            contactAvathorParams.width = width * 10 / 100;
            contactAvathorParams.height = width * 10 / 100;
            contactAvathorParams.leftMargin = width * 5 / 100;

            mRightContactAvathor = new ImageView(mParentActivity);
            mRightContactAvathor.setBackgroundResource(R.drawable.silhouette121);
            mRightContactAvathor.setLayoutParams(contactAvathorParams);

            LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textviewParams.gravity = Gravity.CENTER_VERTICAL;

            mRightContactTextView = new EmojiconTextView(mParentActivity);
            mRightContactTextView.setGravity(Gravity.CENTER_VERTICAL);
            mRightContactTextView.setTextColor(Color.parseColor("#000000"));
            mRightContactTextView.setTypeface(Typeface.DEFAULT_BOLD);
            // textview.setPadding((int) width * 3 / 100, 0, (int) width * 3 / 100, width * 2 / 100);
            //        mRightContactTextView.setText(text);
            mRightContactTextView.setLayoutParams(textviewParams);


            mRightContactTextTime = new TextView(mParentActivity);
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

            LinearLayout textFooterLayout = new LinearLayout(mParentActivity);
            textFooterLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams textfooterParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textFooterLayout.setLayoutParams(textfooterParams);

            LinearLayout.LayoutParams mRightTickMarkParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mRightTickMarkParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

            mRightContactTickMark = new ImageView(mParentActivity);
            mRightContactTickMark.setId(k + 600000);
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

    void leftTextChat() {

        try {
            mLeftTextview = new EmojiconTextView(mParentActivity);
            mLeftTextview.setGravity(Gravity.LEFT);
            mLeftTextview.setTextColor(Color.parseColor("#000000"));
            mLeftTextview.setPadding((int) width * 2 / 100, 0, (int) width * 2 / 100, width * 2 / 100);
            mLeftTextview.setId(k);
            //        mLeftTextview.setText(text);

            mLeftSenderTimeText = new TextView(mParentActivity);
            mLeftSenderTimeText.setGravity(Gravity.LEFT);
            mLeftSenderTimeText.setTextSize(getTimeTxtSize());
            mLeftSenderTimeText.setTextColor(Color.parseColor("#000000"));
            mLeftSenderTimeText.setPadding((int) width * 2 / 100, width * 1 / 100, (int) width * 3 / 100, width * 1 / 100);
            //        mLeftSenderTimeText.setText(time);

            LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textviewParams.gravity=Gravity.LEFT;
            mLeftTextview.setLayoutParams(textviewParams);

            LinearLayout.LayoutParams mLeftSenderTimeTextParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftSenderTimeTextParams.rightMargin = width * 47 / 100;
            mLeftSenderTimeText.setLayoutParams(mLeftSenderTimeTextParams);


            FrameLayout.LayoutParams left_bomb_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            left_bomb_params.width = (int) width * 10 / 100;
            left_bomb_params.height = (int) width * 10 / 100;
            left_bomb_params.gravity = Gravity.CENTER_VERTICAL;
            left_bomb_params.leftMargin = width * 85 / 100;


            mRightDestTime = new TextView(mParentActivity);
            mRightDestTime.setBackgroundResource(R.drawable.black_bomb);
            mRightDestTime.setGravity(Gravity.CENTER);
            mRightDestTime.setTextColor(Color.parseColor("#ffffff"));
            mRightDestTime.setLayoutParams(left_bomb_params);
            mRightDestTime.setVisibility(View.INVISIBLE);
            mRightDestTime.setId(k + 100000);


            mRightChatLayout.addView(mLeftTextview);
            mRightChatLayout.addView(mLeftSenderTimeText);
            mRightTipLayout.addView(mRightDestTime);
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

    void leftImageChat() {

        try {
            mLeftImageChat = new ImageView(mParentActivity);
            mLeftImageChat.setPadding((int) width * 3 / 100, 0, (int) width * 1 / 100, width * 2 / 100);
//            mLeftImageChat.setPadding((int) width * 1 / 100, 0, (int) width * 1 / 100, width * 2 / 100);
            mLeftImageChat.setScaleType(ImageView.ScaleType.CENTER_CROP);

            mLeftImageTextTime = new TextView(mParentActivity);
            mLeftImageTextTime.setGravity(Gravity.LEFT);
            mLeftImageTextTime.setTextSize(getTimeTxtSize());
            mLeftImageTextTime.setTextColor(Color.parseColor("#000000"));
            mLeftImageTextTime.setPadding((int) width * 2 / 100, 0, (int) width * 3 / 100, width * 1 / 100);
            //        mLeftImageTextTime.setText(time);

            LinearLayout.LayoutParams mLeftSenderTimeTextParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftSenderTimeTextParams.rightMargin = width * 47/ 100;
            mLeftImageTextTime.setLayoutParams(mLeftSenderTimeTextParams);

            FrameLayout.LayoutParams textviewParams = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textviewParams.width = width * 57 / 100;
            textviewParams.height = height * 30 / 100;
            textviewParams.topMargin = width * 2 / 100;
            textviewParams.gravity = Gravity.CENTER;
            mLeftImageChat.setLayoutParams(textviewParams);
            //        mRightSenderTimeText.setLayoutParams(textviewParams);

            FrameLayout ImachChatLayout = new FrameLayout(mParentActivity);

            mLeftImageChatDownload = new ImageView(mParentActivity);
            mLeftImageChatDownload.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mLeftImageChatDownload.setBackgroundResource(R.drawable.image_download_normal);

            mLeftImagetProgressBar = new ProgressBar(mParentActivity);

            FrameLayout.LayoutParams left_download_icon_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            left_download_icon_params.width = (int) width * 15 / 100;
            left_download_icon_params.height = (int) width * 15 / 100;
            left_download_icon_params.gravity = Gravity.CENTER;
            mLeftImageChatDownload.setLayoutParams(left_download_icon_params);
            mLeftImagetProgressBar.setLayoutParams(left_download_icon_params);


            ImachChatLayout.addView(mLeftImageChat);
            ImachChatLayout.addView(mLeftImageChatDownload);
            ImachChatLayout.addView(mLeftImagetProgressBar);


            mRightChatLayout.addView(ImachChatLayout);
            mRightChatLayout.addView(mLeftImageTextTime);
            mRightTipLayout.addView(mRightChatLayout);

            mRightTipLayout.setPadding(0, 3, 0, 3);

            mDynamicView.addView(mRightTipLayout);
        } catch (Exception e) {

        }


    }

    void leftVideoChat() {

        try {
            mLeftVideoChat = new ImageView(mParentActivity);
            mLeftVideoChat.setPadding((int) width * 1 / 100, 0, (int) width * 1 / 100, width * 2 / 100);
            mLeftVideoChat.setScaleType(ImageView.ScaleType.CENTER_CROP);

            mLeftVideoTimeText = new TextView(mParentActivity);
            mLeftVideoTimeText.setGravity(Gravity.LEFT);
            mLeftVideoTimeText.setTextSize(getTimeTxtSize());
            mLeftVideoTimeText.setTextColor(Color.parseColor("#000000"));
            mLeftVideoTimeText.setPadding((int) width * 2 / 100, 0, (int) width * 3 / 100, width * 1 / 100);
            //        mLeftImageTextTime.setText(time);

            LinearLayout.LayoutParams mLeftSenderTimeTextParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftSenderTimeTextParams.rightMargin = width * 47 / 100;
            mLeftVideoTimeText.setLayoutParams(mLeftSenderTimeTextParams);
            //        mLeftVideoTimeText.setText(time);

            FrameLayout.LayoutParams mLeftVideoChatParams = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftVideoChatParams.width = width * 60 / 100;
            mLeftVideoChatParams.height = height * 30 / 100;
            mLeftVideoChatParams.topMargin = width * 2 / 100;
            mLeftVideoChatParams.gravity = Gravity.CENTER;
            mLeftVideoChat.setLayoutParams(mLeftVideoChatParams);
            //        mRightSenderTimeText.setLayoutParams(textviewParams);

            FrameLayout mLeftVideoChatLayout = new FrameLayout(mParentActivity);

            mLeftVideoChatDownload = new ImageView(mParentActivity);
            mLeftVideoChatDownload.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mLeftVideoChatDownload.setBackgroundResource(R.drawable.image_download_normal);

            mLeftVideoButtonPlay = new ImageView(mParentActivity);
            mLeftVideoButtonPlay.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mLeftVideoButtonPlay.setBackgroundResource(R.drawable.videooverlay);


            mLeftVideoProgress = new ProgressBar(mParentActivity);

            FrameLayout.LayoutParams left_download_icon_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            left_download_icon_params.width = (int) width * 15 / 100;
            left_download_icon_params.height = (int) width * 15 / 100;
            left_download_icon_params.gravity = Gravity.CENTER;
            mLeftVideoChatDownload.setLayoutParams(left_download_icon_params);
            mLeftVideoProgress.setLayoutParams(left_download_icon_params);
            mLeftVideoButtonPlay.setLayoutParams(left_download_icon_params);


            FrameLayout.LayoutParams videoFooter_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            videoFooter_params.gravity = Gravity.BOTTOM;

            FrameLayout videoFooter = new FrameLayout(mParentActivity);
            videoFooter.setLayoutParams(videoFooter_params);

            FrameLayout.LayoutParams videoimage = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            videoimage.width = width * 10 / 100;
            videoimage.height = width * 8 / 100;
            videoimage.gravity = Gravity.LEFT;
            videoimage.bottomMargin = width * 1 / 100;
            videoimage.leftMargin = width * 4 / 100;

            mLeftVideoImgOverlay = new ImageView(mParentActivity);
            mLeftVideoImgOverlay.setBackgroundResource(R.drawable.frame_overlay_gallery_video);
            mLeftVideoImgOverlay.setLayoutParams(videoimage);

            FrameLayout.LayoutParams videoDuration_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            videoDuration_params.gravity = Gravity.LEFT;
            videoDuration_params.topMargin = (width * 1 / 100) + 1;
            videoDuration_params.leftMargin = width * 13 / 100;

            mLeftVideoDuration = new TextView(mParentActivity);
            mLeftVideoDuration.setTextColor(Color.parseColor("#ffffff"));
            mLeftVideoDuration.setText("1");
            mLeftVideoDuration.setLayoutParams(videoDuration_params);

            FrameLayout.LayoutParams videoSize_params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            videoSize_params.gravity = Gravity.RIGHT;
            videoSize_params.topMargin = width * 1 / 100;
            videoSize_params.rightMargin = width * 5 / 100;
            videoSize_params.leftMargin = width * 47 / 100;


            mLeftVideoSize = new TextView(mParentActivity);
            mLeftVideoSize.setTextColor(Color.parseColor("#ffffff"));
            mLeftVideoSize.setLayoutParams(videoSize_params);

            videoFooter.addView(mLeftVideoImgOverlay);
            videoFooter.addView(mLeftVideoDuration);
            videoFooter.addView(mLeftVideoSize);


            mLeftVideoChatLayout.addView(mLeftVideoChat);

            if (msg_list.get(k).getMedia_name() != null) {


                mLeftVideoChatLayout.addView(mLeftVideoButtonPlay);

            } else {
                mLeftVideoChatLayout.addView(mLeftVideoChatDownload);
                mLeftVideoChatLayout.addView(mLeftVideoProgress);

            }
            mLeftVideoChatLayout.addView(videoFooter);


            mRightChatLayout.addView(mLeftVideoChatLayout);
            mRightChatLayout.addView(mLeftVideoTimeText);
            mRightTipLayout.addView(mRightChatLayout);

            mRightTipLayout.setPadding(0, 3, 0, 3);

            mDynamicView.addView(mRightTipLayout);
        } catch (Exception e) {

        }

    }

    void leftContactChat() {

        try {
            LinearLayout.LayoutParams contactLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            contactLayoutParams.width = width * 50 / 100;
            contactLayoutParams.height = height * 8 / 100;
            contactLayoutParams.topMargin = width * 2 / 100;
            contactLayoutParams.gravity = Gravity.CENTER;

            LinearLayout contactLayout = new LinearLayout(mParentActivity);
            contactLayout.setOrientation(LinearLayout.HORIZONTAL);
            contactLayout.setBackgroundColor(Color.parseColor("#80ffffff"));
            contactLayout.setLayoutParams(contactLayoutParams);

            LinearLayout.LayoutParams contactAvathorParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            contactAvathorParams.width = width * 10 / 100;
            contactAvathorParams.height = width * 10 / 100;
            contactAvathorParams.leftMargin = width * 5 / 100;

            mLeftContactAvathor = new ImageView(mParentActivity);
            mLeftContactAvathor.setBackgroundResource(R.drawable.silhouette121);
            mLeftContactAvathor.setLayoutParams(contactAvathorParams);

            LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textviewParams.gravity = Gravity.CENTER_VERTICAL;
            textviewParams.width = width * 35 / 100;
            textviewParams.leftMargin = width * 2 / 100;

            mLeftContactTextView = new EmojiconTextView(mParentActivity);
            mLeftContactTextView.setGravity(Gravity.CENTER_VERTICAL);
            mLeftContactTextView.setTextColor(Color.parseColor("#000000"));
            mLeftContactTextView.setTypeface(Typeface.DEFAULT_BOLD);
            // textview.setPadding((int) width * 3 / 100, 0, (int) width * 3 / 100, width * 2 / 100);
            //        mLeftContactTextView.setText(text);
            mLeftContactTextView.setLayoutParams(textviewParams);


            mLeftContactTextTime = new TextView(mParentActivity);
            mLeftContactTextTime.setGravity(Gravity.LEFT);
            mLeftContactTextTime.setTextSize(getTimeTxtSize());
            mLeftContactTextTime.setTextColor(Color.parseColor("#000000"));
            mLeftContactTextTime.setPadding((int) width * 2 / 100, width * 1 / 100, (int) width * 3 / 100, width * 1 / 100);
            //        mLeftImageTextTime.setText(time);

            LinearLayout.LayoutParams mLeftSenderTimeTextParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftSenderTimeTextParams.rightMargin = width * 47 / 100;
            mLeftContactTextTime.setLayoutParams(mLeftSenderTimeTextParams);


            LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftContactAvathor.setLayoutParams(timeParams);

            contactLayout.addView(mLeftContactTextView);
            contactLayout.addView(mLeftContactAvathor);

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

    /**
     * Initializing all list view elements..
     */
    public void initializeListElememts(int side) {

        // this.dest_list = list;


//        Constant.printMsg("MUCTest adapter item count constructor :" + msg_list.size());
//
//        try {
//            byte[] img_byte = KachingMeApplication.getAvatar();
//            if (img_byte != null) {
//                mSenderPhoto = BitmapFactory.decodeByteArray(
//                        img_byte, 0, img_byte.length);
//                mSenderPhoto = new AvatarManager()
//                        .roundCornerImage(mSenderPhoto, 180);
////                            arg0.mRightSenderImage.setImageBitmap(new AvatarManager()
////                                    .roundCornerImage(bmp, 180));
//            }
//        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//
//        }


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


        if (side == 1) {

            //Right...
            mRightTipLayout = new FrameLayout(mParentActivity);
            mRightTipLayout.setId(k + 200000);
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
            mRightChatLayoutParams.width = (int) width * 65 / 100;
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
            mRoundImageParams.topMargin = height * 1 / 100;
            mRoundImageParams.leftMargin = (int) width * 88 / 100;


            ImageView mSenderRoundImage = new ImageView(mParentActivity);
            mSenderRoundImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mSenderRoundImage.setLayoutParams(mRoundImageParams);

            System.gc();


            if (KachingMeApplication.getAvatar() != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(
                        KachingMeApplication.getAvatar(), 0,
                        KachingMeApplication.getAvatar().length, Util.getBitmapOptions());

                if (bmp != null) {

                    ProfileRoundImg mSenderImage = new ProfileRoundImg(bmp);
                    mSenderRoundImage.setImageDrawable(mSenderImage);
                } else {
                    Bitmap bm = null;

                     bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2, Util.getBitmapOptions());
                    ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                    mSenderRoundImage.setImageDrawable(mSenderImage);
                }
            } else {
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2, Util.getBitmapOptions());
                ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                mSenderRoundImage.setImageDrawable(mSenderImage);
            }

//            if (Constant.FROM_CHAT_SCREEN.equalsIgnoreCase("chat")) {
//
//                try {
//                    if (UserChatList.mProfileImagesList.size() > 0) {
//                      //  mSenderRoundImage.setImageBitmap(UserChatList.mProfileImagesList.get(UserChatList.mPosition));
//
//                        ProfileRoundImg mSenderImage = new ProfileRoundImg(UserChatList.mProfileImagesList.get(UserChatList.mPosition));
//                        mSenderRoundImage.setImageDrawable(mSenderImage);
//
//                    }else{
//
//                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2);
//                        ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
//                        mSenderRoundImage.setImageDrawable(mSenderImage);
//                    }
//                } catch (Exception e) {
//
//                }
//
//            }
//
//            if (Constant.FROM_CHAT_SCREEN.equalsIgnoreCase("contact")) {
//
//                Constant.printMsg("LLLLLLLIS" + "   " + FavouriteContacts.mPosition + "  " + FavouriteContacts.mProfileImagesList);
//
//                try {
//                    if (FavouriteContacts.mProfileImagesList.size() > 0) {
////                        mSenderRoundImage.setImageBitmap(FavouriteContacts.mProfileImagesList.get(FavouriteContacts.mPosition));
//
//                        ProfileRoundImg mSenderImage = new ProfileRoundImg(FavouriteContacts.mProfileImagesList.get(FavouriteContacts.mPosition));
//                        mSenderRoundImage.setImageDrawable(mSenderImage);
//
//                    }else{
//
//                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2);
//                        ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
//                        mSenderRoundImage.setImageDrawable(mSenderImage);
//                    }
//                } catch (Exception e) {
//
//                }
//
//            }


            mRightTipLayout.addView(mTipImage);
            mRightTipLayout.addView(mSenderRoundImage);


        } else {

            // Left...

            mRightTipLayout = new FrameLayout(mParentActivity);
            mRightTipLayout.setId(k + 200000);
            mRightTipLayout.setFocusable(true);
            mRightTipLayout.setFocusableInTouchMode(true);

            LinearLayout.LayoutParams mRightTipLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mRightTipLayout.setLayoutParams(mRightTipLayoutParams);

            mRightChatLayout = new LinearLayout(mParentActivity);
            mRightChatLayout.setGravity(Gravity.RIGHT);
            mRightChatLayout.setOrientation(LinearLayout.VERTICAL);
//            mRightChatLayout.setBackgroundColor(Color.parseColor("#cdcdcd"));
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
            mRoundImageParams.topMargin = height * 1 / 100;
            mRoundImageParams.leftMargin = (int) width * 1 / 100;

            ImageView mSenderRoundImage = new ImageView(mParentActivity);
            mSenderRoundImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mSenderRoundImage.setLayoutParams(mRoundImageParams);


            if(Constant.FROM_CHAT_SCREEN.equalsIgnoreCase("notification")){

                if(Constant.mReciverAvathor!=null){

                    ProfileRoundImg  mSenderImage=new ProfileRoundImg(Constant.mReciverAvathor);
                    mSenderRoundImage.setImageDrawable(mSenderImage);

                }else{
                    System.gc();
                    Bitmap  bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2, Util.getBitmapOptions());
                    ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                    mSenderRoundImage.setImageDrawable(mSenderImage);
                }


            }

            else if (Constant.FROM_CHAT_SCREEN.equalsIgnoreCase("chat")) {

                try {
                    if (UserChatList.mProfileImagesList.size() > 0) {
                        //  mSenderRoundImage.setImageBitmap(UserChatList.mProfileImagesList.get(UserChatList.mPosition));

                        ProfileRoundImg mSenderImage = new ProfileRoundImg(UserChatList.mProfileImagesList.get(UserChatList.mPosition));
                        mSenderRoundImage.setImageDrawable(mSenderImage);

                    } else {
                        System.gc();
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2,  Util.getBitmapOptions());
                        ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                        mSenderRoundImage.setImageDrawable(mSenderImage);
                    }
                } catch (Exception e) {

                }

            }else if (Constant.FROM_CHAT_SCREEN.equalsIgnoreCase("contact")) {

                Constant.printMsg("LLLLLLLIS" + "   " + FavouriteContacts.mPosition + "  " + FavouriteContacts.mProfileImagesList);

                try {
                    if (FavouriteContacts.mProfileImagesList.size() > 0) {
//                        mSenderRoundImage.setImageBitmap(FavouriteContacts.mProfileImagesList.get(FavouriteContacts.mPosition));

                        ProfileRoundImg mSenderImage = new ProfileRoundImg(FavouriteContacts.mProfileImagesList.get(FavouriteContacts.mPosition));
                        mSenderRoundImage.setImageDrawable(mSenderImage);

                    } else {
                        System.gc();
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2, Util.getBitmapOptions());
                        ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                        mSenderRoundImage.setImageDrawable(mSenderImage);
                    }
                } catch (Exception e) {

                }

            }else{




                try {

                    System.gc();
                    if(avatar!=null)
                    {


                        Bitmap bmp = BitmapFactory.decodeByteArray(avatar,
                                0, avatar.length, Util.getBitmapOptions());
                        //                        mSenderRoundImage.setImageBitmap(FavouriteContacts.mProfileImagesList.get(FavouriteContacts.mPosition));

                        ProfileRoundImg mSenderImage = new ProfileRoundImg(bmp);
                        mSenderRoundImage.setImageDrawable(mSenderImage);


                    } else {

                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2, Util.getBitmapOptions());
                        ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                        mSenderRoundImage.setImageDrawable(mSenderImage);
                    }
                } catch (Exception e) {

                }
            }

            Constant.printMsg("LLLLLLLIS info left"+mSenderRoundImage.getDrawable());

            try{
                if(mSenderRoundImage.getDrawable()==null){
                    System.gc();
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2,Util.getBitmapOptions());
                    ProfileRoundImg mSenderImage = new ProfileRoundImg(bm);
                    mSenderRoundImage.setImageDrawable(mSenderImage);

                }
            }catch (Exception e){

            }



            mRightTipLayout.addView(mTipImage);
            mRightTipLayout.addView(mSenderRoundImage);

        }


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

                Constant.printMsg("GGGGGGGGGGGGGGGGGGGGGGGGPOPO"+mIsLogClick);


                if (mIsLogClick) {

                    Constant.printMsg("GGGGGGGGGGGGGGGGGGGGGGGGPOPO");


                    String mediaType = msg_list.get(v.getId()-200000).getMedia_wa_type();
                    String splText = msg_list.get(v.getId()-200000).getData();


                    if (mOnLongSelectedPostions.contains(v.getId() - 200000)) {

                        boolean isOneMedia = false;
                        boolean isSple = false;


                        for (int i = 0; i < mOnLongSelectedPostions.size(); i++) {

                            Constant.printMsg("JJJJJJJJIO1112222" + v.getId() + "    " + mOnLongSelectedPostions.get(i).toString() + "   " + String.valueOf(v.getId() - 200000));


                            if(!getCopyPastMediaType(mediaType))
                            {
                                isOneMedia = true;
                                Constant.printMsg("Dilip copy loop" + " " + isOneMedia);
                            }

                           else  if(chekNynmDazzKon(splText))
                            {
                                isSple = true;
                            }

                            if (mOnLongSelectedPostions.get(i) == v.getId() - 200000) {

                                Constant.printMsg("JJJJJJJJIO111111" + v.getId() + "    " + mOnLongSelectedPostions);
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

                    } else {

                        if(!getCopyPastMediaType(mediaType))
                        {
                            mChatHedderCopyLayout.setVisibility(View.GONE);
                        }

                       else if(chekNynmDazzKon(splText))
                        {
                            mChatHedderCopyLayout.setVisibility(View.GONE);
                        }

                        mOnLongSelectedPostions.add((v.getId() - 200000));
                        v.setBackgroundColor(Color.parseColor("#FFFFD98F"));

                    }


                    if (mOnLongSelectedPostions.size() <= 0) {

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

                }else{
                    edt_msg.requestFocus();
                }




            }
        });

        mRightTipLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                try {
                    topMenuHideFunction();

                    mIsLogClick = true;

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

                    Constant.printMsg("JJJJJJJJIO" + v.getId() + "    " + mOnLongSelectedPostions + "   " + String.valueOf(v.getId() - 200000));

                    String mediaType = msg_list.get(v.getId()-200000).getMedia_wa_type();
                    String splText = msg_list.get(v.getId()-200000).getData();

                    if (mOnLongSelectedPostions.contains((v.getId() - 200000))) {

                        boolean isOneMedia = false;
                        boolean isSple = false;

                        for (int i = 0; i < mOnLongSelectedPostions.size(); i++) {

                            Constant.printMsg("JJJJJJJJIO1112222" + v.getId() + "    " + mOnLongSelectedPostions.get(i).toString() + "   " + String.valueOf(v.getId() - 200000));
                            if(!getCopyPastMediaType(mediaType))
                            {
                                isOneMedia = true;
                                Constant.printMsg("Dilip copy loop" + " " + isOneMedia);
                            }

                           else if(chekNynmDazzKon(splText))
                            {
                                isSple = true;
                            }



                            if (mOnLongSelectedPostions.get(i) == (v.getId() - 200000)) {

                                Constant.printMsg("JJJJJJJJIO111111" + v.getId() + "    " + mOnLongSelectedPostions);
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

                    } else {

                        if(!getCopyPastMediaType(mediaType))
                        {
                            mChatHedderCopyLayout.setVisibility(View.GONE);
                        }
                       else if(chekNynmDazzKon(splText))
                        {
                            mChatHedderCopyLayout.setVisibility(View.GONE);
                        }

                        mOnLongSelectedPostions.add((v.getId() - 200000));
                        v.setBackgroundColor(Color.parseColor("#FFFFD98F"));

                    }
                } catch (Exception e) {

                }


                return true;
            }
        });
    }

    void rightAudioChat() {


        try {
            LinearLayout audioLayout = new LinearLayout(mParentActivity);
            audioLayout.setOrientation(LinearLayout.HORIZONTAL);
            audioLayout.setPadding(0, width * 2 / 100, 0, 0);

            LinearLayout.LayoutParams playBtnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            playBtnParams.width = width * 9 / 100;
            playBtnParams.height = width * 8 / 100;
            playBtnParams.leftMargin = width * 1 / 100;
            playBtnParams.gravity = Gravity.CENTER_VERTICAL;

            mRightAudioBtnPlay = new Button(mParentActivity);
            mRightAudioBtnPlay.setBackgroundResource(R.drawable.ic_action_audio_play);
            mRightAudioBtnPlay.setLayoutParams(playBtnParams);


            mRightAudioBtnCancel = new Button(mParentActivity);
            mRightAudioBtnCancel.setBackgroundResource(R.drawable.ic_action_content_remove);
            mRightAudioBtnCancel.setLayoutParams(playBtnParams);

            LinearLayout.LayoutParams seekBarParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            seekBarParams.width = width * 52 / 100;
            seekBarParams.leftMargin = width * 1 / 100;
            seekBarParams.rightMargin = width * 2 / 100;
            seekBarParams.gravity = Gravity.CENTER_VERTICAL;

            mRightAudioSeekBar = new SeekBar(mParentActivity);
            mRightAudioSeekBar.setLayoutParams(seekBarParams);


            mRightAudioBtnUpload = new Button(mParentActivity);
            mRightAudioBtnUpload.setBackgroundResource(R.drawable.audio_upload_normal);
            mRightAudioBtnUpload.setLayoutParams(playBtnParams);

            LinearLayout.LayoutParams audioUploadProgressParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            audioUploadProgressParams.width = width * 52 / 100;
            audioUploadProgressParams.leftMargin = width * 1 / 100;
            audioUploadProgressParams.rightMargin = width * 2 / 100;
            audioUploadProgressParams.gravity = Gravity.CENTER_VERTICAL;

            mRightAudioUploadProgress = new ProgressBar(mParentActivity, null,
                    android.R.attr.progressBarStyleHorizontal);
            mRightAudioUploadProgress.setPadding(0, 0, width * 2 / 100, 0);
            mRightAudioUploadProgress.setLayoutParams(audioUploadProgressParams);


            LinearLayout audioTimeLayout = new LinearLayout(mParentActivity);
            audioLayout.setOrientation(LinearLayout.HORIZONTAL);
            audioLayout.setPadding(0, width * 2 / 100, 0, 0);

            mRightAudioSize = new TextView(mParentActivity);
            mRightAudioSize.setTextColor(Color.parseColor("#000000"));
            mRightAudioSize.setPadding(width * 3 / 100, 0, 0, 0);
            mRightAudioSize.setText("5.00 kb");

            LinearLayout.LayoutParams audioDurationParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            audioDurationParams.leftMargin = width * 35 / 100;

            mRightAudioDuration = new TextView(mParentActivity);
            mRightAudioDuration.setTextColor(Color.parseColor("#000000"));
            mRightAudioDuration.setText("00.00.00");
            mRightAudioDuration.setLayoutParams(audioDurationParams);

            mRightAudioTextTime = new TextView(mParentActivity);
            mRightAudioTextTime.setGravity(Gravity.LEFT);
            mRightAudioTextTime.setTextSize(getTimeTxtSize());
            mRightAudioTextTime.setTextColor(Color.parseColor("#000000"));
            mRightAudioTextTime.setPadding((int) width * 1 / 100, width * 1 / 100, (int) width * 3 / 100, width * 1 / 100);

            LinearLayout textFooterLayout = new LinearLayout(mParentActivity);
            textFooterLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams textfooterParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textFooterLayout.setLayoutParams(textfooterParams);


            LinearLayout.LayoutParams mRightTickMarkParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mRightTickMarkParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

            mRightAudioTickMark = new ImageView(mParentActivity);
            mRightAudioTickMark.setId(k + 600000);
            mRightAudioTickMark.setLayoutParams(mRightTickMarkParams);
            mRightAudioTickMark.setPadding((int) width * 2 / 100, width * 1 / 100, (int) width * 1 / 100, width * 1 / 100);


            LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textviewParams.gravity = Gravity.CENTER_VERTICAL;
            mRightAudioTextTime.setLayoutParams(textviewParams);
            audioLayout.setLayoutParams(textviewParams);
            mRightAudioTickMark.setLayoutParams(textviewParams);
            mRightAudioSize.setLayoutParams(textviewParams);



/*
        if(msg_list.get(k).getStatus()!=3)
        {
            audioLayout.addView(mRightAudioBtnPlay);
            audioLayout.addView(mRightAudioSeekBar);
        }else{
            audioLayout.addView(mRightAudioBtnPlay);
            audioLayout.addView(mRightAudioUploadProgress);
        }*/

            audioLayout.addView(mRightAudioBtnCancel);
            audioLayout.addView(mRightAudioBtnUpload);
            audioLayout.addView(mRightAudioUploadProgress);
            audioLayout.addView(mRightAudioBtnPlay);
            audioLayout.addView(mRightAudioSeekBar);

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

    void leftAudioChat() {


        try {
            LinearLayout audioLayout = new LinearLayout(mParentActivity);
            audioLayout.setOrientation(LinearLayout.HORIZONTAL);
            audioLayout.setPadding(0, width * 2 / 100, 0, 0);

            LinearLayout.LayoutParams playBtnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            playBtnParams.width = width * 9 / 100;
            playBtnParams.height = width * 8 / 100;
            playBtnParams.leftMargin = width * 1 / 100;
            playBtnParams.gravity = Gravity.CENTER_VERTICAL;

            mLeftAudioBtnPlay = new Button(mParentActivity);
            mLeftAudioBtnPlay.setId(k + 400000);
            mLeftAudioBtnPlay.setBackgroundResource(R.drawable.ic_action_audio_play);
            mLeftAudioBtnPlay.setLayoutParams(playBtnParams);

            mLeftAudioBtnCancel = new Button(mParentActivity);
            mLeftAudioBtnCancel.setBackgroundResource(R.drawable.ic_action_content_remove);
            mLeftAudioBtnCancel.setLayoutParams(playBtnParams);

            LinearLayout.LayoutParams seekBarParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            seekBarParams.width = width * 53 / 100;
            seekBarParams.leftMargin = width * 1 / 100;
            seekBarParams.rightMargin = width * 2 / 100;
            seekBarParams.gravity = Gravity.CENTER_VERTICAL;

            mLeftAudioSeekBar = new SeekBar(mParentActivity);
            mLeftAudioSeekBar.setId(k+500000);
            mLeftAudioSeekBar.setLayoutParams(seekBarParams);


            mLeftAudioBtnDownload = new Button(mParentActivity);
            mLeftAudioBtnDownload.setId(k + 300000);
            mLeftAudioBtnDownload.setBackgroundResource(R.drawable.audio_download_normal);
            mLeftAudioBtnDownload.setLayoutParams(playBtnParams);

            LinearLayout.LayoutParams audioUploadProgressParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            audioUploadProgressParams.width = width * 53 / 100;
            audioUploadProgressParams.leftMargin = width * 1 / 100;
            audioUploadProgressParams.rightMargin = width * 2 / 100;
            audioUploadProgressParams.gravity = Gravity.CENTER_VERTICAL;

            mLeftAudioDownloadProgress = new ProgressBar(mParentActivity, null,
                    android.R.attr.progressBarStyleHorizontal);
            mLeftAudioDownloadProgress.setPadding(0, 0, width * 2 / 100, 0);
            mLeftAudioDownloadProgress.setLayoutParams(audioUploadProgressParams);


            LinearLayout audioTimeLayout = new LinearLayout(mParentActivity);
            audioLayout.setOrientation(LinearLayout.HORIZONTAL);
            audioLayout.setPadding(0, width * 2 / 100, 0, 0);

            mLeftAudioSize = new TextView(mParentActivity);
            mLeftAudioSize.setTextColor(Color.parseColor("#000000"));
            mLeftAudioSize.setPadding(width * 2 / 100, 0, 0, 0);
            mLeftAudioSize.setText("5.00 kb");

            LinearLayout.LayoutParams audioDurationParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            //  audioDurationParams.leftMargin=width*32/100;
            audioDurationParams.rightMargin = width * 2 / 100;

            mLeftAudioDuration = new TextView(mParentActivity);
            mLeftAudioDuration.setTextColor(Color.parseColor("#000000"));
            mLeftAudioDuration.setText("00.00.00");
            mLeftAudioDuration.setLayoutParams(audioDurationParams);

            mLeftAudioTextTime = new TextView(mParentActivity);
            mLeftAudioTextTime.setGravity(Gravity.LEFT);
            mLeftAudioTextTime.setTextSize(getTimeTxtSize());
            mLeftAudioTextTime.setTextColor(Color.parseColor("#000000"));
            mLeftAudioTextTime.setPadding((int) width * 4 / 100, 0, (int) width * 3 / 100, width * 1 / 100);

            LinearLayout textFooterLayout = new LinearLayout(mParentActivity);
            textFooterLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams textfooterParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textFooterLayout.setLayoutParams(textfooterParams);

            mLeftAudioTickMark = new ImageView(mParentActivity);
            mLeftAudioTickMark.setPadding((int) width * 4 / 100, 0, (int) width * 3 / 100, width * 1 / 100);

            LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textviewParams.gravity = Gravity.CENTER_VERTICAL;
            mLeftAudioTextTime.setLayoutParams(textviewParams);
            audioLayout.setLayoutParams(textviewParams);
            mLeftAudioTickMark.setLayoutParams(textviewParams);

            LinearLayout.LayoutParams mLeftAudioSizeParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mLeftAudioSizeParams.width = width * 47 / 100;
            mLeftAudioSize.setLayoutParams(mLeftAudioSizeParams);

      /*  if(msg_list.get(k).getMedia_name()!=null){

                 audioLayout.addView(mLeftAudioBtnPlay);
                 audioLayout.addView(mLeftAudioSeekBar);

        }else{

            audioLayout.addView(mLeftAudioBtnDownload);
        }
*/


            audioLayout.addView(mLeftAudioBtnPlay);
            audioLayout.addView(mLeftAudioBtnCancel);
            audioLayout.addView(mLeftAudioSeekBar);
            audioLayout.addView(mLeftAudioBtnDownload);
            audioLayout.addView(mLeftAudioDownloadProgress);
            audioTimeLayout.addView(mLeftAudioSize);
            audioTimeLayout.addView(mLeftAudioDuration);

            textFooterLayout.addView(mLeftAudioTickMark);
            textFooterLayout.addView(mLeftAudioTextTime);
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

    public void setRightChatText() {

        Constant.printMsg("MUC text msg Right 1:" + k + "  " + msg_list.get(k).getData().toString());


        try {
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
//                        mTextMsg.setTextSize(valueText());
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
//                        mTextMsg.setTextSize(valueText());
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


                        LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        if (part3.length() > 5)
                            bubbleImgParams.width = Constant.screenWidth * 35 / 100;
                        else
                            bubbleImgParams.width = Constant.screenWidth * 28 / 100;

                        bubbleImgParams.height = Constant.screenHeight * 9 / 100;
                        bubbleImgParams.gravity = Gravity.RIGHT;
                        bubbleImgParams.topMargin = Constant.screenWidth * 2 / 100;
                        bubbleImgParams.rightMargin = Constant.screenWidth * 2/ 100;

                        mTextMsg.setLayoutParams(bubbleImgParams);
                        mTextMsg.setGravity(Gravity.CENTER);
                        Constant.typeFaceKons(getApplicationContext(), mTextMsg);
                        mTextMsg.setTextColor(Color.WHITE);
                        if (Constant.screenWidth >= 600) {

                            mTextMsg.setEmojiconSize(37);


                        } else if (Constant.screenWidth > 501
                                && Constant.screenWidth < 600) {

                            mTextMsg.setEmojiconSize(36);


                        } else if (Constant.screenWidth > 260
                                && Constant.screenWidth < 500) {
                            Constant.printMsg("caleedd 3");

                            mTextMsg.setEmojiconSize(35);

                        } else if (Constant.screenWidth <= 260) {
                            Constant.printMsg("caleedd 4");


                            mTextMsg.setEmojiconSize(34);


                        }

                    } else if (s1 == 'd' && s2 == '>') {
                        String texts = text.substring(3).toString();
                        String[] parts = texts.split("-");
                        final String part1 = parts[0];
                        String part2 = parts[1];

                        Constant.printMsg("testestest :::::::::::" + part1 + "    " + part2);


                        mTextMsg.setText("You donates "
                                + part1 + " BuxS for "
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


                        mSeenList.clear();
                        String query = "select buxdonate  from "
                                + Dbhelper.TABLE_DONATE;
                        callZzleDB(query);

                        HashSet hs = new HashSet();
                        hs.addAll(mSeenList);
                        mSeenList.clear();
                        mSeenList.addAll(hs);

                        //                    mTextMsg.setText(Constant.mSenderName
                        //                            + " accepted your BuxS");
                        mTextMsg.setTypeface(null, Color.BLACK);

                        mTextMsg.setBackground(null);
                        mTextMsg.setMinimumWidth(Constant.screenWidth);
                        mTextMsg.setTextSize(
                                TypedValue.COMPLEX_UNIT_SP,
                                msg_font_size);

                        if (mSeenList.contains(part2)) {

                        } else {

                            ContentValues cv = new ContentValues();
                            cv.put("buxdonate", part2);
                            callDonateBux(cv);

                            String jid = KachingMeApplication.getjid()
                                    .split("@")[0];
                            //                                    contact = dbAdapter.getContact(jid);
                            name = Constant.mSenderName;
                            String from_no = KachingMeApplication
                                    .getjid().split("@")[0];
                            mFromNum = KachingMeApplication.getjid()
                                    .split("@")[0];
                            mToNUm = Constant.mReceiverId
                                    .split("@")[0];

                            // MBuxs = Constant.donatepoint;
                            MBuxs = Long.valueOf(part1);

                            new getDonation().execute();


                        }


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
                    }
                    else if (s1 == 'r' && s2 == '>')
                    {
                        mTextMsg.setText("You Rejected buxs");
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
                    }
                    else {


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

                    boolean toggle = true;
                    int position = (Integer) v.getTag();

                    String text = mTextMsg.getText().toString();


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
                            mParentActivity.startActivity(intent);
                        }else {
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
                            mParentActivity.startActivity(intent);
                        }
                    }
                }

            });
        } catch (Exception e) {

        }


    }

    public void setLeftChatText() {


        try {
            // Set timestamp
            Date date = new Date();
            date.setTime(msg_list.get(k).getReceived_timestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mLeftSenderTimeText.setText(time_format.format(date));


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

                    } else if (s1 == 'z' && s2 == '>') {


                        txt_msg.setTextColor(Color.RED);

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
                        txt_msg.setTypeface(null, Color.RED);

                        txt_msg.setBackground(null);
//                        txt_msg.setTextSize(valueText());
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

                                    } else {
                                        if (Constant.mDefaultScroll == true) {

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
                        //                                bubbleImgParams.leftMargin = Constant.screenWidth * 15 / 100;

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
                        ContentValues cv = new ContentValues();

                        cv.put("msg", part5);
                        cv.put("backgrount", part1);
                        cv.put("font", part4);
                        cv.put("speed", part2);
                        cv.put("fontcolor", part3);

                        insertzzleDB(cv);

                        System.out.println("scroll::"
                                + Constant.msg_list_adapter.get(
                                Constant.msg_list_adapter
                                        .size() - 1)
                                .getData() + "    "
                                + Constant.msg_list_adapter.size());

                        txt_msg.setTextColor(Color.RED);
                        txt_msg.setText(part5);

                        //                                ZzlePreviewGroup.mZzleTextor = txt_msg
                        //                                        .getText().toString();
                        txt_msg.setTypeface(null, Color.RED);

                        txt_msg.setBackground(null);
//                        txt_msg.setTextSize(valueText());
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
                        //                                bubbleImgParams.leftMargin = Constant.screenWidth * 15 / 100;

                        txt_msg.setLayoutParams(bubbleImgParams);

                    } else if (s1 == 's' && s2 == '>') {

                        /*LinearLayout.LayoutParams bubbleImgParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        bubbleImgParams.gravity = Gravity.LEFT;

                        txt_msg.setLayoutParams(bubbleImgParams);*/

                        mRightDestTime.setVisibility(View.VISIBLE);

                        String self_destruct = text.substring(3)
                                .toString();
                        String[] parts = self_destruct.split("-");
                        String part1 = parts[0];
                        String part2 = parts[1];
                        String part3 = parts[2];

                        mRightDestTime.setText(part1);
                        Constant.jid = part3;
                        txt_msg.setText(part2);
                        txt_msg.setTextColor(Color.BLACK);
                        txt_msg.setBackground(null);

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
                        txt_msg.setTextColor(Color.WHITE);




                        if (Constant.screenWidth >= 600) {

                            txt_msg.setEmojiconSize(37);


                        } else if (Constant.screenWidth > 501
                                && Constant.screenWidth < 600) {

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


                        String texts = text.substring(3).toString();
                        String[] parts = texts.split("-");
                        final String part1 = parts[0];
                        String part2 = parts[1];



                        txt_msg.setText(Constant.mSenderName
                                + " donates " + part1
                                + " BuxS for you");
                        txt_msg.setTextColor(Color.MAGENTA);
                        txt_msg.setTypeface(null, Color.MAGENTA);

                        txt_msg.setBackground(null);
                        txt_msg.setMinimumWidth(Constant.screenWidth);
                        txt_msg.setTextSize(
                                TypedValue.COMPLEX_UNIT_SP,
                                msg_font_size);

                        // Constant.mDonateBuxList.add(object);
                    } else if (s1 == 'a' && s2 == '>') {

                        String texts = text.substring(3).toString();
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

                        txt_msg.setText(Constant.mSenderName
                                + " accepted your BuxS");
                        txt_msg.setTypeface(null, Color.BLACK);

                        txt_msg.setBackground(null);
                        txt_msg.setMinimumWidth(Constant.screenWidth);
                        txt_msg.setTextSize(
                                TypedValue.COMPLEX_UNIT_SP,
                                msg_font_size);

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

                    }
                    else if (s1 == 'r' && s2 == '>')
                    {
                        txt_msg.setText(Constant.mSenderName
                                + " rejected your BuxS");
                        txt_msg.setTypeface(null, Color.BLACK);
                        txt_msg.setBackground(null);
                        txt_msg.setMinimumWidth(Constant.screenWidth);
                        txt_msg.setTextSize(
                                TypedValue.COMPLEX_UNIT_SP,
                                msg_font_size);
                    }
                    else {
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

                    final EmojiconTextView txt_msg = (EmojiconTextView) v;
                                          /*txt_msg_layout = (LinearLayout) v
                                                  .findViewById(R.id.ll_txt_msg);*/
                    boolean toggle = true;
                    int position = (Integer) v.getTag();

                    String text = txt_msg.getText().toString();
                    int textlength = text.length();
                    if (txt_msg.getCurrentTextColor() == -65536) {
                        MessageGetSet selectedItem1 = Constant.msg_list_adapter
                                .get(position);
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
                            startActivity(intent);
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
                            startActivity(intent);
                        }
                    }
//                    if (txt_msg.getCurrentTextColor() == -65536) {
//
//                        Constant.zzle = false;
//
//                        BannerActivityLED.mZzleText = txt_msg
//                                .getText().toString();
//                        Intent intent = new Intent(mParentActivity,
//                                BannerActivityLED.class);
//                        startActivity(intent);
//                        // }
//
//                    }

                    if (txt_msg.getCurrentTextColor() == -256) {

                        OrientationGroup.mZzleTextor = txt_msg
                                .getText().toString();

                        // HorizonalSlideshow.mZzleTextor = txt_msg
                        // .getText().toString();

                        Intent intent = new Intent(mParentActivity,
                                OrientationGroup.class);
                        startActivity(intent);

                        // }

                    }
                    if (txt_msg.getCurrentTextColor() == -65281) {

                        Constant.mself_id = position;

                        dbAdapter = KachingMeApplication
                                .getDatabaseAdapter();

                        MessageGetSet selectedItem = null;

                        // Toast.makeText(
                        // mContext,
                        // "not contains   "
                        // + Constant.mself_id,
                        // Toast.LENGTH_SHORT).show();

                        ArrayList<String> mn = new ArrayList<String>();

                        mn.add(String.valueOf(position));

                        for (int i = 0; i < mn.size(); i++) {
                            int p = Integer.valueOf(mn.get(i));
                            MessageGetSet selectedItem1 = Constant.msg_list_adapter
                                    .get(p);

                            String value1 = selectedItem1.getData()
                                    .substring(3).toString();
                            String[] parts = value1.split("-");
                            final String part1 = parts[0];


                            Constant.printMsg("delete id:::::"
                                    + p
                                    + "  "
                                    + Constant.msg_list_adapter
                                    .get(p)
                                    + "    "
                                    + selectedItem1.getKey_id()
                                    + "     "
                                    + selectedItem1.getIs_owner()
                                    + "    "
                                    + selectedItem1
                                    .getKey_from_me()
                                    + "   "
                                    + selectedItem1
                                    .getKey_remote_jid());
                            mkey = selectedItem1.getKey_id();
                            mData = selectedItem1.getData()
                                    + "-accepted";

                            mBuxCount = part1.length();

                        }

                        if (mBuxCount < 10) {
                            b = new AlertDialog.Builder(
                                    ChatTest.this);
                            b.setCancelable(false);
                            b.setMessage("Do you accept the BuxS ?")
                                    .setCancelable(false);

                            b.setNegativeButton(
                                    getResources()
                                            .getString(
                                                    R.string.a_yes),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int id) {

                                            Constant.mBuxAccept = true;

                                            dbAdapter
                                                    .setUpdateMessage(
                                                            mkey,
                                                            mData);

                                            String dazzle = txt_msg
                                                    .getText()
                                                    .toString();
                                            String[] parts = dazzle
                                                    .split(" ");
                                            String part1 = parts[0];
                                            String part2 = parts[1];
                                            // String part3 =
                                            // parts[2];
                                            int len = parts.length;

                                            // String part3 =
                                            // parts[len - 3];
                                            String part3 = parts[len - 4];

                                            Long point = sp1
                                                    .getLong(
                                                            "buxvalue",
                                                            0);

                                            Constant.userId = sp1
                                                    .getLong(
                                                            "uservalue",
                                                            0);

                                            Long dtpoint = sp1
                                                    .getLong(
                                                            "donationpoint",
                                                            0);

                                            Constant.bux = point;

                                            Constant.point = dtpoint;

                                            Long bx = Long
                                                    .valueOf(part3);
                                            Constant.donatepoint = bx;
                                            Long buxvalue = Constant.bux
                                                    + bx;
                                            Constant.mDonatedBux = Constant.bux
                                                    + bx;

                                            Constant.bux = buxvalue;

                                            Editor e = sp1.edit();
                                            e.putLong("buxvalue",
                                                    buxvalue);
                                            e.putLong("uservalue",
                                                    Constant.userId);
                                            e.commit();

                                            Long pointdon = bx
                                                    + Constant.point;
                                            Constant.mAcceptedBuxS = Constant.donatepoint;
                                            // Editor e1 =
                                            // sp1.edit();
                                            // e1.putLong(
                                            // "donationpoint",
                                            // pointdon);
                                            // e1.commit();

                                            Constant.point = pointdon;

                                            String mydate = java.text.DateFormat
                                                    .getDateTimeInstance()
                                                    .format(Calendar.getInstance()
                                                            .getTime());
                                            Constant.mPushDate = mydate;

                                            String jid = KachingMeApplication
                                                    .getjid()
                                                    .split("@")[0];

                                            mToNUm = Constant.mReceiverId
                                                    .split("@")[0];

                                            name = Constant.mSenderName;

                                            SimpleDateFormat sdf = new SimpleDateFormat(
                                                    "dd/MM/yyyy");

                                            String today = sdf
                                                    .format(new Date());

                                            DonationDto dp = new DonationDto();

                                            dp.setName(name);
                                            dp.setDate(today);
                                            //
                                            // dp.setPoint(String
                                            // .valueOf(Constant.point));
                                            dp.setPoint(String
                                                    .valueOf(Constant.donatepoint));
                                            dp.setStatus("credit");
                                            Constant.donatelust
                                                    .add(dp);

                                            ContentValues cv = new ContentValues();

                                            cv.put("date", today);
                                            cv.put("points",
                                                    Constant.donatepoint);
                                            cv.put("name", name);
                                            cv.put("status",
                                                    "credit");

                                            insertDBDonation(cv);
                                            Intent intent = new Intent(
                                                    mParentActivity
                                                    ,
                                                    ChatTest.class);
                                            startActivity(intent);
                                        }
                                    });
                            b.setPositiveButton(
                                    "Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int id) {

                                            Constant.mBuxReject = true;

                                            dbAdapter
                                                    .setUpdateMessage(
                                                            mkey,
                                                            mData);
                                            finish();
                                            Intent intent = new Intent(
                                                    mParentActivity
                                                    ,
                                                    ChatTest.class);
                                            startActivity(intent);
                                        }
                                    });
                            b.setCancelable(true);

                            AlertDialog alert = b.create();
                            alert.show();
                        }
                    }
                }
            });
        } catch (Exception e) {

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
        // TODO Auto-generated method stub
        return str.substring(0, i) + str.substring(i + 1);
    }

    public void setRightImage() {
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


        String fromuser = msg_list.get(k).getKey_remote_jid();
        // Set timestamp
        Date date = new Date();
        date.setTime(msg_list.get(k).getSend_timestamp());
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
        mRightImageChatCancel.setTag(values);
        mRightImageChatUpload.setVisibility(View.GONE);
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



            //----------------------------
            boolean isConn = Connectivity.isOnline(mParentActivity);

            if(!mAsyncUpload_Image.containsKey(String.valueOf(msg_list.get(k).get_id())))
            mAsyncUpload_Image.put(String.valueOf(msg_list.get(k).get_id()), new AsyncHttpClient());


            Constant.printMsg("......Chat Image 0000...." + msg_list.get(k).getStatus()+"  "+msg_list.get(k).getNeeds_push() +"  "+k
            );


            if (msg_list.get(k).getNeeds_push() == 2
                    && msg_list.get(k).getStatus() == 3) {

                if(!isFirstCall)
                {
                    if (isConn) {
                        mRightImageChatUpload.setVisibility(View.GONE);
                        mRightImageChatCancel.setVisibility(View.VISIBLE);
                        mRightImageProgress
                                .setVisibility(View.VISIBLE);

                        Constant.printMsg("......Chat Image 3..22...." + msg_list.get(k).getStatus()+"  "+msg_list.get(k).getNeeds_push()+"  "+msg_list.get(k).get_id()
                        );

                        new uploa_image().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, String.valueOf(msg_list.get(k).get_id()),
                                fromuser);

                    } else {
                        mRightImageChatUpload.setVisibility(View.VISIBLE);
                        mRightImageChatCancel.setVisibility(View.GONE);
                        mRightImageProgress
                                .setVisibility(View.GONE);
                    }
                }else
                {


                    Constant.printMsg("......Chat Image 3..33...." + msg_list.get(k).getStatus()+"  "+msg_list.get(k).getNeeds_push()
                    );
                    mRightImageChatUpload.setVisibility(View.VISIBLE);
                    mRightImageChatCancel.setVisibility(View.GONE);
                    mRightImageProgress
                            .setVisibility(View.GONE);
                }

            }

         else   if (msg_list.get(k).getStatus() == 3 && msg_list.get(k).getNeeds_push() == 1) {
                if(!isFirstCall) {
                    if (isConn) {
                        mRightImageChatUpload.setVisibility(View.GONE);
                        mRightImageChatCancel.setVisibility(View.VISIBLE);
                        mRightImageProgress
                                .setVisibility(View.VISIBLE);

                        Constant.printMsg("......Chat Image 3..1...." + msg_list.get(k).getStatus() + "  " + msg_list.get(k).getNeeds_push() + "  " + msg_list.get(k).get_id()
                        );

                        dbAdapter.setUpdateMessage_need_push(msg_list.get(k).getKey_id(), 2);

                        new uploa_image().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, String.valueOf(msg_list.get(k).get_id()),
                                fromuser);
                    } else {
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
            }else if(msg_list.get(k).getNeeds_push() == 3)
            {
                mRightImageChatUpload.setVisibility(View.VISIBLE);
                mRightImageChatCancel.setVisibility(View.GONE);
                mRightImageProgress
                        .setVisibility(View.GONE);
            }

            else if (msg_list.get(k).getStatus() == 2
                    && msg_list.get(k).getMedia_url() != null) {
                mRightImageChatUpload.setVisibility(View.GONE);
                mRightImageChatCancel.setVisibility(View.GONE);
                mRightImageProgress.setVisibility(View.GONE);

            } else {
                mRightImageChatUpload.setVisibility(View.GONE);
                mRightImageChatCancel.setVisibility(View.GONE);
                mRightImageProgress.setVisibility(View.GONE);
            }


        } else {
            mRightImageChatUpload.setVisibility(View.GONE);
            mRightImageChatCancel.setVisibility(View.GONE);
            mRightImageProgress.setVisibility(View.GONE);
            byte[] image_data = msg_list.get(k).getRow_data();
            System.gc();
            Bitmap bitmap = BitmapFactory.decodeByteArray(
                    image_data, 0, image_data.length, Util.getBitmapOptions());
            mRightImageChat.setImageBitmap(bitmap);
            mRightImageChat.setTag(null);
        }

        mRightImageChat.setOnClickListener(new OnClickListener() {

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

        final ProgressBar progressBar = mRightImageProgress;
        final ImageView imgView = mRightImageChatUpload;
        final ImageView imgView_Cancel = mRightImageChatCancel;

        mRightImageChatUpload
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String[] val = v.getTag().toString()
                                .split(",");
                        if (Connectivity.isOnline(mParentActivity)) {
                            try {
                              //  mAsyncUpload_Image.put(val[0], new uploa_image());
                                new uploa_image().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, val[0],
                                        val[1]);


                                progressBar
                                        .setVisibility(View.VISIBLE);
                                imgView
                                        .setVisibility(View.GONE);
                                imgView_Cancel.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                Constant.printMsg("......Chat Image CCCC..exp.."  +e.toString());
                            }
                        } else {
                            Toast.makeText(mParentActivity, "No network connection", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


           mRightImageChatCancel
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String[] val = v.getTag().toString()
                                .split(",");
                    //    mRightImageChatCancel = (ImageView) findViewById(v.getId());
                  //     mRightImageTickMark = (ImageView) findViewById(v.getId()+580000);
                        int i =dbAdapter.getMessageStatusById(val[0]);

                        /*mRightImageChatCancel = (ImageView) findViewById(v.getId());
                        mRightImageChatUpload = (ImageView) findViewById(v.getId()+680000 );
                        mRightImageProgress = (ProgressBar) findViewById(v.getId()+780000);*/

                        Constant.printMsg("......Chat Image CCCC...."  + String.valueOf(val[0]));
                        if( i!=2)
                        {
                            if (Connectivity.isOnline(mParentActivity)) {
                                mAsyncUpload_Image.get(String.valueOf(val[0])).cancelAllRequests(true);


                                    Constant.printMsg("......Chat Image CCCC..inside.."  + v.getId());
                                    dbAdapter.setUpdateMessage_need_pushById(val[0], 3);



                            } else {
                                Toast.makeText(mParentActivity, "No network connection", Toast.LENGTH_SHORT).show();
                            }

                    }else
                        {

                            Constant.printMsg("......Chat Image CCCC..oo.."  + v.getId());
                            mRightImageProgress
                                    .setVisibility(View.GONE);
                            mRightImageChatUpload
                                    .setVisibility(View.GONE);

                            mRightImageChatCancel
                                    .setVisibility(View.GONE);
                        }


                    }
                });

        }catch (Exception e){

           Constant.printMsg("......Chat Image eeee..oo.."  + e.toString());

                    }

    }

    public void setRightVideo() {


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


            String fromuser = msg_list.get(k).getKey_remote_jid();
            // Set timestamp
            Date date = new Date();
            date.setTime(msg_list.get(k).getSend_timestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mRightVideoTimeText.setText(time_format.format(date));


            mRightVideoDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                    notificatiob_font_size);
            mRightVideoSize.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                    notificatiob_font_size);

            mRightVideoChat.setFocusable(false);
            mRightVideoChatUpload.setFocusable(false);
            mRightVideoChatCancel.setFocusable(false);
            mRightVideoButtonPlay.setFocusable(false);
            mRightVideoDuration.setFocusable(false);
            mRightVideoSize.setFocusable(false);


            long second = (msg_list.get(k).getMedia_duration() / 1000) % 60;
            long minute = (msg_list.get(k).getMedia_duration() / (1000 * 60)) % 60;

            String time = String.format("%02d:%02d", minute, second);

            mRightVideoDuration.setText(time);


            mRightVideoSize.setText(Network_Usage.convertBytesToSuitableUnit(String.valueOf(msg_list.get(k).getMedia_size())));

            File file = new File(Constant.local_video_dir
                    + msg_list.get(k).getMedia_name());


            mRightVideoChat.getLayoutParams().width = (int) width * 59 / 100;
            mRightVideoChat.getLayoutParams().height = (int) height * 30 / 100;

            String values = msg_list.get(k).get_id() + "," + fromuser;
            mRightVideoChatUpload.setTag(values);
            mRightVideoChatUpload.setVisibility(View.GONE);
            mRightVideoChatCancel.setTag(values);
            mRightVideoChatCancel.setVisibility(View.GONE);
            mRightVideoProgress.setVisibility(View.GONE);

            try {
                byte[] image_data = msg_list.get(k).getRow_data();
                System.gc();
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

            if (msg_list.get(k).getStatus() == 3 &&
                    msg_list.get(k).getNeeds_push() ==1) {

                Constant.printMsg("......Chat Video 3..1...." + isConnected
                );

                if (!isFirstCall) {
                    if(isConnected) {

                        mRightVideoChatUpload.setVisibility(View.GONE);
                        mRightVideoChatCancel.setVisibility(View.VISIBLE);
                        mRightVideoProgress.setVisibility(View.VISIBLE);
                        mRightVideoButtonPlay.setVisibility(View.GONE);
                        new uploa_video().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(msg_list.get(k).get_id()), fromuser);
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


                if(!isFirstCall)
                {
                    if(isConnected) {

                        mRightVideoChatUpload.setVisibility(View.GONE);
                        mRightVideoChatCancel.setVisibility(View.VISIBLE);
                        mRightVideoProgress.setVisibility(View.VISIBLE);
                        mRightVideoButtonPlay.setVisibility(View.GONE);
                        new uploa_video().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(msg_list.get(k).get_id()), fromuser);
                    } else
                    {
                        mRightVideoChatUpload.setVisibility(View.VISIBLE);
                        mRightVideoChatCancel.setVisibility(View.GONE);
                        mRightVideoProgress.setVisibility(View.GONE);
                        mRightVideoButtonPlay.setVisibility(View.GONE);

                    }
                }else {
                    mRightVideoChatUpload.setVisibility(View.GONE);
                    mRightVideoChatCancel.setVisibility(View.VISIBLE);
                    mRightVideoProgress.setVisibility(View.VISIBLE);
                    mRightVideoButtonPlay.setVisibility(View.GONE);

                }
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
                        startActivity(intent);
                    }

                }
            });

            final ProgressBar progressBar = mRightVideoProgress;
            final ImageView upl_img = mRightVideoChatUpload;
            final ImageView upl_img_cancel = mRightVideoChatCancel;
            final ImageView ply_vid = mRightVideoButtonPlay;
            mRightVideoChatUpload
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            boolean isConn = Connectivity.isOnline(mParentActivity);

                            Constant.printMsg("video upload online " + isConn);

                            if (isConn) {
                                String[] val = v.getTag().toString()
                                        .split(",");
                                new uploa_video().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, val[0],
                                        val[1]);

                                progressBar
                                        .setVisibility(View.VISIBLE);
                                upl_img
                                        .setVisibility(View.GONE);
                                upl_img_cancel.setVisibility(View.VISIBLE);
                                ply_vid.setVisibility(View.GONE);

                            }else
                            {
                                Toast.makeText(mParentActivity, "No Network Available", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            mRightVideoChatCancel
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {


                            boolean isConn = Connectivity.isOnline(mParentActivity);

                            Constant.printMsg("video can online " + isConn);


                            if (isConn) {
                                String[] val = v.getTag().toString()
                                        .split(",");
                              mAsyncUpload_Video.get(val[0]).cancelAllRequests(true);

                                progressBar
                                        .setVisibility(View.GONE);
                                upl_img
                                        .setVisibility(View.VISIBLE);
                                ply_vid.setVisibility(View.GONE);
                                upl_img_cancel.setVisibility(View.GONE);
                            }
                            else
                            {
                                Toast.makeText(mParentActivity, "No Network Available", Toast.LENGTH_LONG).show();
                            }
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
                        startActivity(intent);
                    } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                        AlertDialog.Builder b = new AlertDialog.Builder(
                                mParentActivity);
                        b.setMessage(
                                getResources()
                                        .getString(
                                                R.string.dont_have_app_to_play_video))
                                .setCancelable(false);
                        b.setNegativeButton(
                                getResources().getString(
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

    public void setRightContact() {
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


            String fromuser = msg_list.get(k).getKey_remote_jid();
            // Set timestamp
            Date date = new Date();
            date.setTime(msg_list.get(k).getSend_timestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mRightContactTextTime.setText(time_format.format(date));

            Constant.printMsg("Diliiip "+ msg_list.get(k).getMedia_name());
            String contactName = msg_list.get(k).getMedia_name().split(",")[0];


            mRightContactTextView.setText(contactName);
            String values = msg_list.get(k).getMedia_name();
            mRightContactTextView.setTag(values);
            mRightContactTextView.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP, msg_font_size);
            try {
                byte[] image_data = msg_list.get(k).getRow_data();
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
                            startActivity(intent);
                        }
                    });

        } catch (Exception e) {
        }

    }

    public void setLocation() {

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


            String fromuser = msg_list.get(k).getKey_remote_jid();
            // Set timestamp
            Date date = new Date();
            date.setTime(msg_list.get(k).getSend_timestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mRightImageTextTime.setText(time_format.format(date));


            Log.d("Location", "Lat::" + msg_list.get(k).getLatitude() + " Lon::"
                    + msg_list.get(k).getLongitude());
            String lat_lon = msg_list.get(k).getLatitude() + ","
                    + msg_list.get(k).getLongitude();
            mRightImageChat.setTag(lat_lon);


            String values = msg_list.get(k).get_id() + "," + fromuser;
            mRightImageChatUpload.setTag(values);
            mRightImageChatUpload.setVisibility(View.GONE);
            mRightImageChatCancel.setTag(values);
            mRightImageChatCancel.setVisibility(View.GONE);
            mRightImageProgress.setVisibility(View.GONE);
            try {
                byte[] image_data = msg_list.get(k).getRow_data();
                Bitmap bitmap = BitmapFactory.decodeByteArray(
                        image_data, 0, image_data.length,Util.getBitmapOptions());
//                Bitmap unscaledBitmap = ScalingUtilities
//                        .decodeFile(Constant.local_image_dir
//                                + msg_list.get(k).getMedia_name(), (int) width * 57 / 100, (int) height * 30 / 100);
                mRightImageChat.setImageBitmap(bitmap);

            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
            }
            if (msg_list.get(k).getStatus() == 3
                    && msg_list.get(k).getNeeds_push() == 1) {
                mRightImageChatUpload.setVisibility(View.GONE);
                //mRightImageProgress.setVisibility(View.VISIBLE);
                                     /*
                                      * new uploa_Location().execute(msg_list.get(position).get_id(),
                                      * fromuser);
                                      */
            }
            if (msg_list.get(k).getStatus() == 2
                    && msg_list.get(k).getNeeds_push() == 0) {
                mRightImageChatUpload.setVisibility(View.GONE);
                mRightImageProgress.setVisibility(View.GONE);
            } else {
                mRightImageChatUpload.setVisibility(View.GONE);
                mRightImageProgress.setVisibility(View.GONE);

            }

            mRightImageChat.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Constant.printMsg("Location Onclick. Right....");
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
                            startActivity(mapIntent);
                        } catch (ActivityNotFoundException exp) {
                            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
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

                           /* progress_upload_image
                                    .setVisibility(View.VISIBLE);
                            btn_image_upload
                                    .setVisibility(View.VISIBLE);*/

                        }
                    });

        } catch (Exception e) {
        }

    }



    protected void insertToLEDDB(ContentValues cv) {
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

    private void callDonateBux(ContentValues cv) {
        // TODO Auto-generated method stub
        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_DONATE, null, cv);
            Constant.printMsg("No of inserted rows in zzle :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in ecpl details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }
    }

    /**
     * Left Image Download..
     */
    public void setLeftDownloadImage() {
        synchronized (this) {
        try {
            mLeftImageChat.setVisibility(View.VISIBLE);

            // Set timestamp
            Date date = new Date();
            date.setTime(msg_list.get(k).getReceived_timestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mLeftImageTextTime.setText(time_format.format(date));

            String fromuser = msg_list.get(k).getKey_remote_jid();

            mLeftImageChat.getLayoutParams().width = (int) width * 59 / 100;
            mLeftImageChat.getLayoutParams().height = (int) height * 30 / 100;

            //      arg0.mLeftImageChat.setMaxHeight(i);

            String values = msg_list.get(k).getKey_id() + "," + msg_list.get(k).getMedia_url()
                    + "," + fromuser;
            mLeftImageChatDownload.setVisibility(View.VISIBLE);
            mLeftImageChatDownload.setTag(values);

            Constant.printMsg("cursor adapter image test >>>>>>>>>>>> " + msg_list.get(k).getMedia_url() + "    " + msg_list.get(k).get_id());

            if (msg_list.get(k).getMedia_name() == null) {

                if(msg_list.get(k).getRow_data()
                        !=null)
                    Constant.printMsg("Image row data not null");


                byte[] img_byte = msg_list.get(k).getRow_data();
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
                    if(Connectivity.isOnline(mParentActivity)) {
                        new download_image().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, msg_list.get(k).getKey_id(),
                                msg_list.get(k).getMedia_url(), fromuser);
                    }
                } else {
                    mLeftImagetProgressBar
                            .setVisibility(View.GONE);

                }
            } else {
                mLeftImageChatDownload.setVisibility(View.GONE);
                mLeftImagetProgressBar.setVisibility(View.GONE);

                File file = new File(Constant.local_image_dir
                        + msg_list.get(k).getMedia_name());
                if (file.isFile()) {

                    Constant.printMsg("Image row data not null333");

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
                            + msg_list.get(k).getMedia_name());

                } else {

                    Constant.printMsg("Image row data not null111");
                    byte[] image_data = msg_list.get(k).getRow_data();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(
                            image_data, 0, image_data.length);
                    mLeftImageChat.setImageBitmap(bitmap);
                    mLeftImageChat.setScaleType(ImageView.ScaleType.FIT_XY);
                    mLeftImageChat.setTag(null);

                }

            }

            final ProgressBar progress_download_image = mLeftImagetProgressBar;
            final ImageView btn_image_download = mLeftImageChatDownload;
            mLeftImageChatDownload
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if(Connectivity.isOnline(mParentActivity)) {

                                String[] val = v.getTag().toString()
                                        .split(",");
                                new download_image().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, val[0],
                                        val[1], val[2]);
                                progress_download_image
                                        .setVisibility(View.VISIBLE);
                                progress_download_image.setIndeterminate(true);
                                btn_image_download
                                        .setVisibility(View.GONE);
                            }else
                            {
                                Toast.makeText(mParentActivity,Constant.mToastMsgDownload,Toast.LENGTH_SHORT).show();
                            }

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
                        startActivity(intent);
                    }

                }
            });
        } catch (Exception e) {

        }
    }
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

    public void setLeftVideo() {
        // Set timestamp


        // Set timestamp
        Date date = new Date();
        date.setTime(msg_list.get(k).getReceived_timestamp());
        SimpleDateFormat time_format = new SimpleDateFormat(
                "hh:mm a");

        mLeftVideoTimeText.setText(time_format.format(date));

        String fromuser = msg_list.get(k).getKey_remote_jid();
        mLeftVideoChat.setVisibility(View.VISIBLE);
        mLeftVideoChat.setVisibility(View.VISIBLE);


        mLeftVideoDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                notificatiob_font_size);
        mLeftVideoSize.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                notificatiob_font_size);
//        mLeftVideoSize.setTextSize(notificatiob_font_size,
//                notificatiob_font_size);

        long second = (msg_list.get(k).getMedia_duration() / 1000) % 60;
        long minute = (msg_list.get(k).getMedia_duration() / (1000 * 60)) % 60;

        String time = String.format("%02d:%02d", minute, second);
        mLeftVideoDuration.setText(time);


        Constant.printMsg("DUUUUURE" + Network_Usage.convertBytesToSuitableUnit(String.valueOf(msg_list.get(k).getMedia_size())) + "     " + String.valueOf(msg_list.get(k).getMedia_size()));

        mLeftVideoSize.setText(Network_Usage.convertBytesToSuitableUnit(String.valueOf(msg_list.get(k).getMedia_size())));

        String values = msg_list.get(k).getKey_id() + "," + msg_list.get(k).getMedia_url()
                + "," + fromuser;

        mLeftVideoChatDownload.setTag(values);

        mLeftVideoChat.getLayoutParams().width = (int) width * 60 / 100;
        mLeftVideoChat.getLayoutParams().height = (int) height * 30 / 100;

        if (msg_list.get(k).getMedia_name() == null) {
            byte[] img_byte = msg_list.get(k).getRow_data();
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
                if(Connectivity.isOnline(mParentActivity)) {
                    new download_video().execute(msg_list.get(k).getKey_id(),
                            msg_list.get(k).getMedia_url(), fromuser);

                }
            } else {

                // //Constant.printMsg("Media Name::"+msg_list.get(position).getMedia_name());

                mLeftVideoProgress
                        .setVisibility(View.GONE);
                mLeftVideoChatDownload.setVisibility(View.VISIBLE);
            }
            //mLeftVideoButtonPlay.setVisibility(View.GONE);
        } else {
            mLeftVideoChatDownload.setVisibility(View.GONE);
            mLeftVideoProgress.setVisibility(View.GONE);

            File file = new File(Constant.local_video_dir
                    + msg_list.get(k).getMedia_name());
            mLeftVideoButtonPlay.setTag(Constant.local_video_dir
                    + msg_list.get(k).getMedia_name());
            try {
                byte[] image_data = msg_list.get(k).getRow_data();
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

                //mLeftVideoButtonPlay.setVisibility(View.VISIBLE);

            } else {

                // mLeftVideoButtonPlay.setVisibility(View.GONE);

            }

        }
        mLeftVideoChat
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {


                    }
                });
        mLeftVideoChatDownload
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(Connectivity.isOnline(mParentActivity)) {
                            String[] val = v.getTag().toString()
                                    .split(",");
                            new download_video().execute(val[0],
                                    val[1], val[2]);
                            mLeftVideoProgress
                                    .setVisibility(View.VISIBLE);
                            mLeftVideoChatDownload
                                    .setVisibility(View.GONE);
                        }else
                        {
                            Toast.makeText(mParentActivity,Constant.mToastMsgDownload,Toast.LENGTH_SHORT).show();
                        }

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
                    startActivity(intent);
                } catch (Exception e) {

                    AlertDialog.Builder b = new AlertDialog.Builder(
                            mParentActivity);
                    b.setMessage(
                            getResources()
                                    .getString(
                                            R.string.dont_have_app_to_play_video))
                            .setCancelable(false);
                    b.setNegativeButton(
                            getResources().getString(
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
    }

    public void setLeftContact() {


        try {
            // Set timestamp
            Date date = new Date();
            date.setTime(msg_list.get(k).getReceived_timestamp());
            SimpleDateFormat time_format = new SimpleDateFormat(
                    "hh:mm a");

            mLeftContactTextTime.setText(time_format.format(date));


            String contactName = msg_list.get(k).getMedia_name().split(",")[0];


            mLeftContactTextView.setText(contactName);
            String values = msg_list.get(k).getMedia_name();
            mLeftContactAvathor.setTag(values);
            mLeftContactTextView.setTag(values);
            mLeftContactTextView.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP, msg_font_size);
            try {
                byte[] image_data = msg_list.get(k).getRow_data();
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

                        Intent intent = new Intent(mParentActivity,
                                ContactView_Normal.class);
                        intent.putExtra("vcard", v.getTag()
                                .toString());
                        intent.putExtra("status", true);
                        startActivity(intent);
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
                            startActivity(intent);

                        }
                    });
        } catch (Exception e) {

        }

    }

    public void setLeftLocation() {

        // Set timestamp
        Date date = new Date();
        date.setTime(msg_list.get(k).getReceived_timestamp());
        SimpleDateFormat time_format = new SimpleDateFormat(
                "hh:mm a");

        mLeftImageTextTime.setText(time_format.format(date));


        String fromuser = msg_list.get(k).getKey_remote_jid();
        mLeftImageChat.setVisibility(View.VISIBLE);

        Log.d("Location", "Lat::" + msg_list.get(k).getLatitude() + " Lon::"
                + msg_list.get(k).getLongitude());
        String lat_lon = msg_list.get(k).getLatitude() + ","
                + msg_list.get(k).getLongitude();
        mLeftImageChat.setTag(lat_lon);

        mLeftImageChat.getLayoutParams().width = (int) width * 59 / 100;
        mLeftImageChat.getLayoutParams().height = (int) height * 30 / 100;
        mLeftImageChatDownload.setVisibility(View.GONE);
        mLeftImagetProgressBar.setVisibility(View.GONE);


        String values = msg_list.get(k).get_id() + "," + fromuser;

        try {
            byte[] image_data = msg_list.get(k).getRow_data();
            Bitmap bitmap = BitmapFactory.decodeByteArray(
                    image_data, 0, image_data.length,Util.getBitmapOptions());
            mLeftImageChat.setImageBitmap(bitmap);
        } catch (Exception e) {
            // ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
        }
        if (msg_list.get(k).getStatus() == 1 && msg_list.get(k).getNeeds_push() == 1) {

            // new
            // uploa_Location().execute(msg_list.get(position).get_id(),fromuser);
        } else if (msg_list.get(k).getStatus() == 1 && msg_list.get(k).getNeeds_push() == 0) {

        } else {

        }

        mLeftImageChat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Constant.printMsg("Location Onclick.....");

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
                        startActivity(mapIntent);
                    } catch (ActivityNotFoundException exp) {
                        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }

                }

            }
        });

    }

    /**
     * Set Left Audio
     */


//Right Audio play and cancel audio modification...

    public void setRightAudio_Old() {
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

        String fromuser = msg_list.get(k).getKey_remote_jid();
        String values = msg_list.get(k).get_id() + "," + fromuser;
        mRightAudioBtnCancel.setTag(values);
        mRightAudioBtnUpload.setTag(values);
        mRightAudioBtnCancel.setVisibility(View.VISIBLE);
        mRightAudioBtnUpload.setVisibility(View.VISIBLE);
        mRightAudioBtnPlay.setVisibility(View.VISIBLE);
        mRightAudioBtnPlay.setBackgroundResource(R.drawable.ic_action_audio_play);


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
                .millisToShortDHMS(msg_list.get(k).getMedia_duration()));

        mRightAudioBtnPlay.setFocusable(false);
        mRightAudioBtnUpload.setFocusable(false);
        mRightAudioBtnCancel.setFocusable(false);
        mRightAudioSize.setFocusable(false);
        mRightAudioUploadProgress.setFocusable(false);
        mRightAudioSeekBar.setFocusable(false);
        mRightAudioSize.setText(new CompressImage()
                .humanReadableByteCount(msg_list.get(k).getMedia_size(), true));

        mRightAudioSeekBar.setProgress(0);
        mRightAudioSeekBar.setMax(100);
        final MediaPlayer player = new MediaPlayer();
        mRightAudioBtnPlay.setTag(Constant.local_audio_dir
                + msg_list.get(k).getMedia_name());
        mRightAudioSeekBar.setTag(msg_list.get(k).get_id());

        list.add(player);
        PlayBtnlist.add(mRightAudioBtnPlay);

        final Button btn_play = mRightAudioBtnPlay;
        final Button btn_cancel_upload = mRightAudioBtnCancel;
        final ProgressBar progress_audio = mRightAudioUploadProgress;
        final Button btn_upload = mRightAudioBtnUpload;
        final SeekBar seek_audio = mRightAudioSeekBar;

        Constant.printMsg("Right Audio " + msg_list.get(k).getStatus());

        mRightAudioBtnCancel
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        String[] val = v.getTag().toString()
                                .split(",");
                        Constant.printMsg("Audio upload cancel 1 " + val[0]);
                       mAsyncUpload_Audio.get(val[0]).cancelAllRequests(true);
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

        final TextView audio_leng = mRightAudioDuration;

        class run implements Runnable {

            MediaPlayer mp;
            SeekBar seek;

            public run(MediaPlayer m, SeekBar se) {
                mp = m;
                seek = se;
            }

            @Override
            public void run() {


                try {
                    if (player.isPlaying()) {
                        seek.setProgress(mp.getCurrentPosition());
                        audio_leng.setText(new TimeUtils()
                                .millisToShortDHMS(mp
                                        .getCurrentPosition()));
                    }
                    seekHandler.postDelayed(this, 1);
                } catch (Exception e) {

                }
            }

        }

        MessageGetSet msg1 = dbAdapter.getMessages_by_msg_id(String.valueOf(msg_list.get(k).get_id()));


        boolean isConnected = Connectivity.isOnline(mParentActivity);

        if(!mAsyncUpload_Audio.containsKey(String.valueOf(msg_list.get(k).get_id())))
            mAsyncUpload_Audio.put(String.valueOf(msg_list.get(k).get_id()), new AsyncHttpClient());

        //-----------------------------

        if (msg_list.get(k).getStatus() == 3 &&
                msg_list.get(k).getNeeds_push() ==1) {

            Constant.printMsg("......Chat Video 3..1...." + isConnected
            );

            if (!isFirstCall) {
                if(isConnected) {

                    btn_upload.setVisibility(View.GONE);
                    progress_audio.setVisibility(View.VISIBLE);
                    progress_audio.setIndeterminate(true);
                    btn_play.setVisibility(View.GONE);
                    btn_cancel_upload.setVisibility(View.VISIBLE);
                    seek_audio.setVisibility(View.GONE);

                   /* long l = dbAdapter.setUpdateMessage_need_push(
                            msg.getKey_id(), 0);*/
                    new uploa_audio().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf( msg_list.get(k).get_id()), fromuser);
                } else
                {
                    btn_upload.setVisibility(View.VISIBLE);
                    btn_play.setVisibility(View.GONE);
                    btn_cancel_upload.setVisibility(View.GONE);
                    progress_audio.setVisibility(View.GONE);
                    seek_audio.setVisibility(View.GONE);

                }
            }else
            {
                btn_upload.setVisibility(View.GONE);
                btn_play.setVisibility(View.GONE);
                btn_cancel_upload.setVisibility(View.VISIBLE);
                progress_audio.setVisibility(View.VISIBLE);
                progress_audio.setFocusable(true);
                seek_audio.setVisibility(View.GONE);
            }


        } else if (msg_list.get(k).getStatus() == 3
                && msg_list.get(k).getNeeds_push() == 2 ) {


            if(!isFirstCall)
            {
                if(isConnected) {

                    btn_upload.setVisibility(View.GONE);
                    progress_audio.setVisibility(View.VISIBLE);
                    progress_audio.setIndeterminate(true);
                    btn_play.setVisibility(View.GONE);
                    btn_cancel_upload.setVisibility(View.VISIBLE);
                    seek_audio.setVisibility(View.GONE);

                    dbAdapter.setUpdateMessage_need_push(
                            msg_list.get(k).getKey_id(), 1);
                    new uploa_audio().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(msg_list.get(k).get_id()), fromuser);
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

        btn_upload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String[] val = v.getTag().toString().split(",");

								/* new uploa_audio().execute(val[0],val[1]); */

                new uploa_audio().execute(val[0], val[1]);

                btn_upload.setVisibility(View.GONE);
                progress_audio.setVisibility(View.VISIBLE);
                btn_play.setVisibility(View.GONE);
                // txt_audio_size.setVisibility(View.GONE);
                btn_cancel_upload.setVisibility(View.VISIBLE);
                seek_audio.setVisibility(View.GONE);

            }

        });

        btn_play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Constant.printMsg("GGGP{");
                Intent intent = new Intent("com.android.music.musicservicecommand");
                intent.putExtra("command", "pause");
                sendBroadcast(intent);

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

    public void setLeftAudio_Old() {

        // Set timestamp
        Date date = new Date();
        date.setTime(msg_list.get(k).getReceived_timestamp());
        SimpleDateFormat time_format = new SimpleDateFormat(
                "hh:mm a");

        mLeftAudioTextTime.setText(time_format.format(date));

        String fromuser = msg_list.get(k).getKey_remote_jid();
        mLeftAudioBtnCancel.setVisibility(View.VISIBLE);
        mLeftAudioBtnDownload.setVisibility(View.VISIBLE);
        mLeftAudioBtnPlay.setVisibility(View.VISIBLE);
        mLeftAudioDownloadProgress.setVisibility(View.VISIBLE);
        mLeftAudioBtnPlay.setBackgroundResource(R.drawable.ic_action_audio_play);

        mLeftAudioSize.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                notificatiob_font_size);
        mLeftAudioDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                notificatiob_font_size);

        mLeftAudioDuration.setText(new TimeUtils()
                .millisToShortDHMS(msg_list.get(k).getMedia_duration()));

        mLeftAudioBtnPlay.setFocusable(false);
        mLeftAudioBtnDownload.setFocusable(false);
        mLeftAudioBtnCancel.setFocusable(false);
        mLeftAudioSize.setFocusable(false);
        mLeftAudioDownloadProgress.setFocusable(false);
        mLeftAudioSeekBar.setFocusable(false);

        mLeftAudioSeekBar.setProgress(0);
        mLeftAudioSeekBar.setMax(100);
        mLeftAudioSize.setText(new CompressImage()
                .humanReadableByteCount(msg_list.get(k).getMedia_size(), true));
        final MediaPlayer player = new MediaPlayer();
        mLeftAudioBtnPlay.setTag(Constant.local_audio_dir
                + msg_list.get(k).getMedia_name());
        mLeftAudioSeekBar.setTag(msg_list.get(k).get_id());
        String values = msg_list.get(k).getKey_id() + "," + msg_list.get(k).getMedia_url()
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

                try{
                    if (player.isPlaying()) {

                        if(isPaused)
                        {
                            player.stop();
                            player.release();
                        }else {
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
                    }
                    seekHandler.postDelayed(this, 1);
                }catch (Exception e){

                }

            }

        }

        if (msg_list.get(k).getMedia_name() == null) {
            if (is_auto_dowload_audio) {
                new download_Audio().execute(msg_list.get(k).getKey_id(),
                        msg_list.get(k).getMedia_url(), fromuser);
                mLeftAudioBtnDownload.setVisibility(View.GONE);
                mLeftAudioBtnCancel.setVisibility(View.VISIBLE);
                mLeftAudioDownloadProgress.setVisibility(View.VISIBLE);
                mLeftAudioDownloadProgress.setIndeterminate(true);
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
                mLeftAudioBtnPlay=(Button)findViewById(v.getId()+100000);
                mLeftAudioSeekBar=(SeekBar)findViewById(v.getId()+200000);

                if(Connectivity.isOnline(mParentActivity))
                {

                    String[] val = v.getTag().toString().split(",");

								/* new uploa_audio().execute(val[0],val[1]); */

                    new download_Audio().execute(val[0], val[1],
                            val[2]);

                    mLeftAudioBtnDownload.setVisibility(View.GONE);
                    mLeftAudioDownloadProgress.setVisibility(View.VISIBLE);
                    mLeftAudioDownloadProgress.setIndeterminate(true);
                    mLeftAudioBtnPlay.setVisibility(View.GONE);
                    // txt_audio_size.setVisibility(View.GONE);
                    mLeftAudioBtnCancel.setVisibility(View.VISIBLE);
                    mLeftAudioSeekBar.setVisibility(View.GONE);
                }else
                {
                    Toast.makeText(mParentActivity,Constant.mToastMsgDownload,Toast.LENGTH_SHORT).show();
                }
            }

        });

        mLeftAudioBtnPlay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                mLeftAudioBtnDownload=(Button)findViewById(v.getId()-100000);
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
        // Log.d("Audio view", "Audio view ended....");
    }


    public static enum ScalingLogic {
        CROP, FIT
    }

    public class FetchChat extends AsyncTask<String, String, ArrayList<MessageGetSet>> {

        @Override
        protected void onPreExecute() {


            super.onPreExecute();
        }

        @Override
        protected ArrayList<MessageGetSet> doInBackground(String... params) {


            Constant.printMsg("Fetch Chat onPreExecute2");
            cursor = dbAdapter.getMessagesCursor_Chat(jid, sec);
            Constant.printMsg("Fetch Chat onPreExecute3");


            if (cursor != null) {

                Constant.printMsg("Fetch Chat onPreExecute 3.5  " + cursor.getCount());

                msg_list.clear();
                mPositionKey.clear();
                Constant.msg_list_adapter.clear();
                dest_list = new ArrayList<Integer>();
                dest_list_msgids = new ArrayList<Integer>();
                dest_list_bombids = new ArrayList<Integer>();
                dest_list_anim = new ArrayList<Integer>();


                try {
                    Constant.printMsg("Fetch Chat onPreExecute4" + isFirstCall);

                    if (cursor.moveToFirst()) {
                        do{
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
                            msg.setPostion(msg_list.size());


                            msg_list.add(msg);
                            mPositionKey.add(msg.getKey_id());


                           if (isFirstCall  || isDeletEl )
                                checkDestMsg(msg);

                        }while (cursor.moveToNext());
                    }


                } catch (Exception e) {
                    Constant.printMsg("Fetch Chat onPreExecute  Exppp " + e.toString());
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            }


            return msg_list;
        }

        @Override
        protected void onPostExecute(ArrayList<MessageGetSet> result) {


            if (result != null) {

                isDeletEl = false;

                if(msg_list.size()>0)
                {
                    if(msg_list.get(0).getMedia_wa_type().equalsIgnoreCase("40"))
                    {
                        msg_list.remove(0);
                        mPositionKey.remove(0);
                    }

                }

                mDynamicView.removeAllViews();

                Constant.msg_list_adapter = msg_list;

                //                if (!isFirstCall)
                //                    initializeListElememts();


                Constant.printMsg("Fetch Chat onPreExecute 6 " + msg_list.size());

                for (k = 0; k < msg_list.size(); k++) {


                    //Right Chat
                    if (msg_list.get(k).getKey_from_me() != 1) {

                        initializeListElememts(1);
                        //Right Text Chat
                        if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 0) {

                            //Text
                            rightTextChat();
                            setRightChatText();


                        } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 1) {

                            //Image
                            rightImageChat();
                            setRightImage();

                        } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 2) {

                            //Video
                            rightVideoChat();
                            setRightVideo();

                        } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 5) {

                            //Contact
                            rightContactChat();
                            setRightContact();

                        } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 3) {

                            //Audio
                            rightAudioChat();
                            setRightAudio_Old();

                        } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 4) {

                            //Location
                            rightImageChat();
                            setLocation();

                        }
                    } else {
                        initializeListElememts(2);

                        if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 0) {

                            leftTextChat();
                            setLeftChatText();

                        } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 1) {

                            leftImageChat();

                            synchronized (this) {
                                setLeftDownloadImage();
                            }

                        } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 2) {

                            leftVideoChat();
                            setLeftVideo();

                        } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 5) {

                            leftContactChat();
                            setLeftContact();

                        } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 3) {

                            //Audio
                            leftAudioChat();
                            setLeftAudio_Old();

                        } else if (Integer.parseInt(msg_list.get(k).getMedia_wa_type()) == 4) {

                            //Location
                            leftImageChat();
                            setLeftLocation();

                        }

                    }

                }
                try {

                    mRightTipLayout.requestFocus();
                    edt_msg.requestFocus();

                }catch (Exception e){

                }


            }

//            mRightTipLayout.requestFocus();
            //            Intent group_chat = new Intent("group_list");
            //            sendBroadcast(group_chat);
//            mScrollView.post(new Runnable() {
//
//                @Override
//                public void run() {
//                    mScrollView.scrollTo(0, 20000);
//                    //mScrollView.requestFocus();
//                }
//            });
            isFirstCall= false;
            isFetchChatReady = false;
            super.onPostExecute(result);


        }

    }

    class MyAsync extends AsyncTask<String, String, ContactsGetSet> {

        @Override
        protected ContactsGetSet doInBackground(String... params) {

            ContactsGetSet contact = null;
            // contactsGetSet contact = null;

            try {
                int isPresent = dbAdapter.getJidIdPresentOrNot(params[0]);
                Constant.printMsg("contact value 1:::: " + isPresent);

                if (isPresent == 0) {
                    try {
                        if (contact == null) {

                            Constant.printMsg("contact value 3:::: " + contact);

                            contact = new ContactsGetSet();
                            contact.setJid(params[0]);
                            contact.setIsInContactList(0);
                            contact.setNumber(params[0].split("@")[0]);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Constant.printMsg("contact value 2:::: " + dbAdapter.getIsInContactList(params[0]));
                        if (dbAdapter.getIsInContactList(params[0]) == 1) {
                            contact = dbAdapter.getContact_grp(params[0]);
                        } else {

                            Constant.printMsg("contact value 4:::: " + contact);

                            contact = new ContactsGetSet();
                            contact.setJid(params[0]);
                            contact.setIsInContactList(0);
                            contact.setNumber(params[0].split("@")[0]);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {

            }

            return contact;
        }

        @Override
        protected void onPostExecute(final ContactsGetSet result) {

            super.onPostExecute(result);

            if (result.getIsInContactList() != 1) {
                Constant.printMsg("result.getIsInContactList()"
                        + result.getIsInContactList());
                home_title = result.getNumber();
                if (home_title.length() > 15) {
                    txt_title.setText(home_title.substring(0, 15) + "...");
                } else {
                    txt_title.setText(home_title);
                }

                isSavedContact = false;
                 /* if (listViewheader == null) {
                      LayoutInflater inflater = getLayoutInflater();
                      listViewheader = (ViewGroup) inflater.inflate(
                              R.layout.header_view_new_contact, listview, false);

                      tvBlock = (TextView) listViewheader
                              .findViewById(R.id.tv_block);
                  }
                  if (KachingMeApplication.getBlocked_user().contains(jid)) {sad
                      tvBlock.setText(getResources().getString(R.string.unblock));
                      UserBlocked = true;
                  } else {
                      tvBlock.setText(getResources().getString(R.string.block));
                      UserBlocked = false;
                  }

                  tvBlock.setOnClickListener(new OnClickListener() {

                      @Override
                      public void onClick(View v) {

                          if (UserBlocked) {
                              unblock();
                          } else {
                              new Async_Privacy().execute();
                          }

                      }
                  });

                  listViewheader.findViewById(R.id.tv_add_contact)
                          .setOnClickListener(new OnClickListener() {

                              @Override
                              public void onClick(View v) {

                                  if (!IsProcessOnAddContact) {
                                      Intent intent = new Intent(
                                              Intent.ACTION_INSERT,
                                              ContactsContract.Contacts.CONTENT_URI);
                                      intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                                      intent.putExtra(Intents.Insert.PHONE, "+"
                                              + result.getNumber());
                                      intent.putExtra(Intents.Insert.PHONE_TYPE,
                                              Phone.TYPE_MOBILE);
                                      intent.putExtra(
                                              "finishActivityOnSaveCompleted",
                                              true);
                                      if (result.getNifty_email() != null)
                                          intent.putExtra(Intents.Insert.EMAIL,
                                                  result.getNifty_email());
                                      if (result.getNifty_name() != null)
                                          // intent.putExtra(Intents.Insert.NAME,
                                          // result.getNifty_name());

                                          startActivityForResult(intent,
                                                  REQUEST_CODE_ADD_CONTACT);
                                      IsProcessOnAddContact = true;
                                  }
                              }
                          });*/
                //listview.addHeaderView(listViewheader, null, false);
                //
            } else {
                Constant.printMsg("result.getIsInContactList() else"
                        + result.getIsInContactList());
                isSavedContact = true;
                home_title = result.getDisplay_name();
                if (home_title.length() > 15) {
                    txt_title.setText(home_title.substring(0, 15) + "...");
                } else {
                    txt_title.setText(home_title);
                }
            }

            if (!ChatTest.isSavedContact) {
                mContactLayout.setVisibility(View.VISIBLE);
                Constant.printMsg("asasfdhasdfhj"+ KachingMeApplication.getBlocked_user()+"   "+jid);
                if (KachingMeApplication.getBlocked_user().contains(jid))
                {
                    UserBlocked  = true;
                    mBlockContact.setText(getResources().getString(R.string.unblock));
                    txt_sub_title.setVisibility(View.GONE);
                }else
                {

                    UserBlocked  = false;
                    mBlockContact.setText(getResources().getString(R.string.block));
                    txt_sub_title.setVisibility(View.VISIBLE);
                }


            } else {
                mContactLayout.setVisibility(View.GONE);
                Constant.printMsg("asasfdhasdfhj"+ KachingMeApplication.getBlocked_user()+"   "+jid);
                if (KachingMeApplication.getBlocked_user().contains(jid))
                {
                    UserBlocked  = true;
                    mBlockContact.setText(getResources().getString(R.string.unblock));
                    txt_sub_title.setVisibility(View.GONE);
                }else
                {

                    UserBlocked  = false;
                    mBlockContact.setText(getResources().getString(R.string.block));
                    txt_sub_title.setVisibility(View.VISIBLE);
                }

            }

        }

    }

    public class Async_Privacy extends AsyncTask<String, String, String> {
        Boolean is_list_exist = false;

        @Override
        protected void onPreExecute() {

            try{
                pDialog.show();

            }catch (Exception e){

            }

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                privacyManager = PrivacyListManager.getInstanceFor(TempConnectionService.connection);

                if (privacyManager != null) {
                    Log.w("Chat", "Privacy Async called....");

                    String listName = KachingMeApplication.getUserID();

                    String user = jid;

                    PrivacyItem item = new PrivacyItem(PrivacyItem.Type.jid,
                            user, true, 1);
                    UserBlocked = true;
                    item.setFilterMessage(false);

                    ArrayList<PrivacyItem> privacyItem_list = new ArrayList<PrivacyItem>();
                    ArrayList<String> global_block_list = new ArrayList<String>();


                    privacyItem_list.add(item);
                    global_block_list.add(user);

                    if (is_list_exist == true) {
                        privacyManager.updatePrivacyList(listName,
                                privacyItem_list);
                    } else {
                        privacyManager.createPrivacyList(listName,
                                privacyItem_list);
                        privacyManager.setActiveListName(listName);
                    }

                    List<PrivacyList> lists = privacyManager.getPrivacyLists();
                    Log.d("Chat", "Privacy List Lengh::" + lists.size());
                    int i = 0;

                    int b = 0;
                    String blocked = "";
                    try {
                        List<PrivacyItem> privacy_items = privacyManager
                                .getPrivacyList(KachingMeApplication.getUserID())
                                .getItems();
                        for (PrivacyItem privacyItem : privacy_items) {
                            privacyItem_list.add(privacyItem);
                            global_block_list.add(privacy_items.toString());

                            if (b == 0) {
                                blocked = privacyItem.getValue();
                            } else {
                                blocked += "," + privacyItem.getValue();
                            }
                            b++;
                        }
                    } catch (Exception e) {

                    }

                    String mFinalBlockedUser=" ";
                    mFinalBlockedUser = sp.getString(Constant.BLOCKED_USERS,"");
                    String[] parts = mFinalBlockedUser.split("-");

                    ArrayList mBlockedList=new ArrayList();
                    for(int j=0;j<parts.length;j++){
                        mBlockedList.add(parts[j].trim());
                   }
                    mBlockedList.add(jid);

                    mFinalBlockedUser=mFinalBlockedUser+"-"+jid;
                    KachingMeApplication.setBlocked_user(mBlockedList);
                    Constant.printMsg("gsdkga"+mBlockedList+"    "+ KachingMeApplication.getBlocked_user());

                    ed.putString(Constant.BLOCKED_USERS, mFinalBlockedUser);
                    ed.commit();



//                    KachingMeApplication.setBlocked_user(global_block_list);


                }

            } catch (XMPPException e) {// ACRA.getErrorReporter().handleException(e);
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoResponseException e) {// ACRA.getErrorReporter().handleException(e);
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NotConnectedException e) {// ACRA.getErrorReporter().handleException(e);
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // dbAdapter.setDeleteMessages(jid);
            super.onPostExecute(result);
            supportInvalidateOptionsMenu();

            if (mBlockContact != null) {
                if (UserBlocked == true) {
                    mBlockContact.setText(getResources().getString(R.string.unblock));
                    txt_sub_title.setVisibility(View.GONE);
                    if (mChatOptionMenuBlockTxt != null) {
                        mChatOptionMenuBlockTxt.setText(getResources().getString(R.string.unblock));
                    }
                }
                else {
                    mBlockContact.setText(getResources().getString(R.string.block));
                    txt_sub_title.setVisibility(View.VISIBLE);
                    if (mChatOptionMenuBlockTxt != null) {
                        mChatOptionMenuBlockTxt.setText(getResources().getString(R.string.block));
                    }
                }
            }

            pDialog.cancel();
        }

    }

    public class unblock extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // // TODO Auto-generated method stub
            // try {
            //
            // Log.d("Privacy List", "List unblocked..1");
            //
            // ArrayList<String> list = new ArrayList<String>();
            //
            // PrivacyListManager mngr = mBoundService.getPrivacyListManager();
            // List<PrivacyItem> privacy_items = mngr.getPrivacyList(
            // KachingMeApplication.getUserID()).getItems();
            // ArrayList<PrivacyItem> privacy_items_updated = new
            // ArrayList<PrivacyItem>();
            // for (PrivacyItem privacyItem : privacy_items) {
            // if (!jid.equals(privacyItem.getValue())) {
            // list.add(privacyItem.getValue());
            // privacy_items_updated.add(privacyItem);
            // }
            //
            // }
            //
            // mngr.updatePrivacyList(KachingMeApplication.getUserID(),
            // privacy_items_updated);
            // KachingMeApplication.setBlocked_user(list);
            // Log.d("Privacy List", "List unblocked..");
            // supportInvalidateOptionsMenu();
            // } catch (XMPPException e) {//
            // ACRA.getErrorReporter().handleException(e);
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // } catch (NoResponseException e) {//
            // ACRA.getErrorReporter().handleException(e);
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // } catch (NotConnectedException e) {//
            // ACRA.getErrorReporter().handleException(e);
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            return null;
        }

    }

    private class ActionModeCallback implements ActionMode.Callback {
        boolean is_copy = true;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            is_copy = true;
            Constant.printMsg("Action Mode....");

            mode.getMenuInflater().inflate(R.menu.menu_chat_actionmode, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            if (is_copy) {
               MenuItem  itemCopy = menu.findItem(R.id.menu_copy);
                itemCopy.setVisible(true);
                return true;
            } else {
                MenuItem  itemCopy = menu.findItem(R.id.menu_copy);
                itemCopy.setVisible(false);
                return true;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // TODO Auto-generated method stub

            try {

                unregisterReceiver(lastseen_event);
            } catch (Exception e) {
            }
            is_copy = true;
//            Constant.adapterTest_cursor.removeSelection();
            mActionMode = null;

        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

//            SparseBooleanArray selected = Constant.adapterTest_cursor.getSelectedIds();

//            switch (item.getItemId()) {
//
//                case R.id.menu_delete:
//
//
//                    mDeleteAlert = new AlertDialog.Builder(mParentActivity);
//                    mDeleteAlert.setMessage("Are you sure you want to delete ?")
//                            .setCancelable(false);
//                    mDeleteAlert.setPositiveButton(getResources().getString(R.string.cancel),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                    mcheck = true;
//                                }
//                            });
//                    mDeleteAlert.setNegativeButton(getResources().getString(R.string.Ok),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//
//                                    mcheck = true;
//
//                                    SparseBooleanArray selected = Constant.adapterTest_cursor.getSelectedIds();
//
//                                    for (int i = (selected.size() - 1); i >= 0; i--) {
//                                        if (selected.valueAt(i)) {
//                                            MessageGetSet selectedItem = msg_list.get(selected
//                                                    .keyAt(i));
//                                            // adapter.remove(selectedItem);
//                                            Constant.printMsg("delete id:::::"
//                                                    + selectedItem.get_id());
//                                            dbAdapter.setDeleteMessages_by_msgid(""
//                                                    + selectedItem.get_id());
//                                        }
//                                    }
//
//                                    int msg_id = dbAdapter.getLastMsgid_chat(jid, sec);
//                                    Constant.printMsg("id::::::::::::" + msg_id);
//                                    if (dbAdapter.isExistinChatList_chat(jid, sec)) {
//                                        dbAdapter.setUpdateChat_lits_chat(jid, msg_id, sec);
//                                    } else {
//                                        dbAdapter.setInsertChat_list_chat(jid, msg_id, sec);
//                                    }
//
//
//                                    ConcurrentAsyncTaskExecutor.executeConcurrently(new FetchChat());
//
//
//                                }
//                            });
//
//                    AlertDialog alert = mDeleteAlert.create();
//                    alert.show();
//                    if (mcheck) {
//                        mcheck = false;
//                        mode.finish(); // Action picked, so close the CAB
//
//                    }
//                    break;
//
//                case R.id.menu_copy:
//
//
//                    Constant.printMsg("Dilip copy ");
//
//                    String copiedtext = "";
//                    int j = 0;
//
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
//
//                    int currentapiVersion = Build.VERSION.SDK_INT;
//                    if (currentapiVersion >= Build.VERSION_CODES.HONEYCOMB) {
//                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                      /*
//                       * ClipData clip = ClipData.newPlainText("label",
//                       * "Text to Copy"); clipboard.setPrimaryClip(clip);
//                       */
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
//                    mode.finish();
//                    Intent intent = new Intent(mParentActivity, SliderTesting.class);
//                    Log.i("Forword", "Message IDS " + msg_ids);
//                    intent.putExtra("msg_ids", msg_ids);
//
//                    startActivity(intent);
//                    // startActivityForResult(intent, 11);
//                    // mode.finish();
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

    private class AsyncCaller extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // this method will be running on UI thread
        }

        @Override
        protected Void doInBackground(Void... params) {

            // this method will be running on background thread so don't update
            // UI frome here
            // do your long running http tasks here,you dont want to pass
            // argument and u can access the parent class' variable url over
            // here
            for (int j = 0; j < Constant.mSelectedImage.size(); j++) {

                Constant.printMsg("UIM " + Constant.mSelectedImage.size());
                uploadFile(String.valueOf(Constant.mSelectedImage.get(j)),
                        true);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // this method will be running on UI thread
        }

    }

    private class MyAsync1 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Constant.printMsg("jid::::contact try" + jid);
                contact = new ContactsGetSet();
                contact = dbAdapter.getContact(jid);
                Constant.printMsg("jid::::contact bmp111111"
                        + contact.getPhoto_ts().length
                        + "contact.getPhoto_ts().length" + contact
                        + "contact.getPhoto_ts()::" + contact.getPhoto_ts());
                bmp = BitmapFactory.decodeByteArray(contact.getPhoto_ts(), 0,
                        contact.getPhoto_ts().length,Util.getBitmapOptions());
                Constant.printMsg("jid::::contact bmp"
                        + contact.getPhoto_ts().length
                        + "contact.getPhoto_ts().length" + contact
                        + "contact.getPhoto_ts()::" + contact.getPhoto_ts());

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
                    // img_chat_avatar.setImageResource(R.drawable.avtar);
                    img_chat_avatar.setImageBitmap(new AvatarManager()
                            .roundCornerImage(bmp, 180));

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

    public class DestructGetter {

        String msg;
        long id;

        DestructGetter(String msg, long id) {
            this.msg = msg;
            this.id = id;
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

    // ////////////////////////----Image
    // Upload-----//////////////////////////////////////////////////////////////////////
    public class uploa_image extends AsyncTask<String, String, String> {

        MessageGetSet message = new MessageGetSet();
        String url = null;
        String from;
        String msg_id;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            Constant.printMsg("......Chat Image Async canceled..."
            );
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);

            Constant.printMsg("......Chat Image Async canceled.1.."
                   );
        }

        @Override
        protected String doInBackground(String... params) {

            Log.d("Message key id", "Message key id::" + params[0]);
            Constant.printMsg("......Chat Image 0000.11..."
            + "  " + params[0]);
            msg_id = params[0];
            message = dbAdapter.getMessages_by_msg_id(params[0]);
            dbAdapter.setUpdateMessage_need_push(message.getKey_id(), 2);

            from = params[1];
            // Constant.printMsg("image url:" +
            // message.getMedia_name()+" is exist::"+new
            // File(Constant.local_image_dir+message.getMedia_name()).getAbsolutePath());df

            try {


                ChatManager chatManager = ChatManager
                        .getInstanceFor(TempConnectionService.connection);
                final org.jivesoftware.smack.chat.Chat chat = TempConnectionService.chatmanager.createChat(params[1],
                        TempConnectionService.mChatCreatedListener.getMessageListener());

                final Message msg = new Message();

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

                if (message.getIs_sec_chat() == 0) {

                    JivePropertiesManager.addProperty(msg, "is_sec_chat",
                            message.getIs_sec_chat());
                    JivePropertiesManager.addProperty(msg, "self_desc_time",
                            message.getSelf_des_time());
                }

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



                AsyncHttpClient client =  mAsyncUpload_Image.get(params[0]);
                client.setTimeout(60000);


                Constant.printMsg("GGGGGGGGGGGGGGGGGGPath" + Constant.local_image_dir + message.getMedia_name()+"    "+msg_id);

                if (message.getMedia_url() == null) {
                    client.post(mParentActivity,
                            MEDIA_UPLOAD_URL,
                            null,
                            request_params,"multipart/form-data",
                            new AsyncHttpResponseHandler(Looper.getMainLooper()) {


                                @Override
                                public void onFailure(int arg0, Header[] position,
                                                      byte[] arg2, Throwable arg3) {
                                    // TODO Auto-generated method stub
                                    dbAdapter.setUpdateMessage_Media_url(
                                            msg_id, null);

                                    long l = dbAdapter.setUpdateMessage_status(
                                            message.getKey_id(), 3);
                                    dbAdapter.setUpdateMessage_need_push(message.getKey_id(), 1);

                                    for(int j=0;j<msg_list.size();j++){

                                        if(msg_list.get(j).getKey_id().equalsIgnoreCase(message.getKey_id())){

                                            Constant.printMsg("hjjjjjafasb");
                                            mRightImageTickMark=(ImageView) findViewById(j+600000);
//                                            mRightImageChatUpload=(ImageView) findViewById(j+800000);
//                                            mRightImageProgress=(ProgressBar) findViewById(j+900000);
//                                            mRightImageChatUpload.setVisibility(View.GONE);
//                                            mRightImageProgress.setVisibility(View.GONE);
                                            mRightImageTickMark
                                                    .setImageResource(R.drawable.message_unsent);
                                        }

                                    }

//                                    Intent login_broadcast = new Intent("chat");
//                                    login_broadcast.putExtra("jid", from);
//                                    mParentActivity.getApplicationContext().sendBroadcast(login_broadcast);

                                }

                                @Override
                                public void onCancel() {
                                    int j =   mPositionKey.indexOf(message.getKey_id());
                                    Intent login_broadcast = new Intent("update_image_cancel");
                                    login_broadcast.putExtra("key", j);
                                    mParentActivity.getApplicationContext().sendBroadcast(login_broadcast);
                                    super.onCancel();
                                }

                                @Override
                                public void onSuccess(int arg0, Header[] position,
                                                      byte[] arg2) {
                                    String content = new String(arg2);

                                    String  url = null;


                                    try {


                                        JSONObject jsonObject_Image = new JSONObject(content);
//
                                        url = jsonObject_Image.getString("url");
                                        String msg_idddd = jsonObject_Image.getString("msgId");
                                        Constant.printMsg("hjjjjjafasb 111" +content + "  " +msg_idddd );

                                        Constant.printMsg("hjjjjjafasb " +mPositionKey.get(0) + " --" + msg_idddd );

                                        int j =   mPositionKey.indexOf(message.getKey_id());

                                        Constant.printMsg("hjjjjjafasb " + j);



                                      msg_list.get(j).setStatus(2);


                                    Intent login_broadcast = new Intent("image_upload");
                                    login_broadcast.putExtra("key", j);
                                    mParentActivity.getApplicationContext().sendBroadcast(login_broadcast);

//                                        mRightImageChatUpload=(ImageView) mParentActivity.findViewById(j+800000);
//                                        mRightImageChatCancel=(ImageView) mParentActivity.findViewById(j+120000);
//                                        mRightImageProgress=(ProgressBar) mParentActivity.findViewById(j+900000);
//                                        mRightImageChatUpload.setVisibility(View.GONE);
//                                        mRightImageChatCancel.setVisibility(View.GONE);
//                                        mRightImageProgress.setVisibility(View.GONE);
//                                        mRightImageTickMark=(ImageView) mParentActivity.findViewById(j+600000);
//                                        mRightImageTickMark
//                                                .setImageResource(R.drawable.receipt_from_server);


                                        dbAdapter.setUpdateMessage_status(
                                                message.getKey_id(), 2);



                                        Constant.printMsg("......Chat Image 0000.22.........."
                                                +"     " + message.getKey_id() +"    "+ msg_id);



                                    } catch (JSONException e1) {
                                        Constant.printMsg("path of img::::exp::::>>>>>>"
                                                + e1.toString());
                                    }
                                    try {
                                        long l = dbAdapter
                                                .setUpdateMessage_Media_url(
                                                        msg_id, url);
                                        Constant.printMsg("path of img::::::::>>>>>>"
                                                + url);


                                        JivePropertiesManager.addProperty(msg,
                                                "path", url);

                                        Constant.printMsg("path of img:::msg:::::>>>>>>"
                                                + msg.toXML());
                                    } catch (Exception e) {
                                        Constant.printMsg("Image upload error"
                                                + e.toString());
                                        Toast.makeText(mParentActivity,
                                                "Something went wrong",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    try {
                                        MessageEventManager
                                                .addNotificationsRequests(msg,
                                                        true, true, true, true);
                                        chat.sendMessage(msg);

                                        Constant.printMsg("......Chat Image 3..44...."
                                        );

                                    } catch (Exception e) {

                                    }


                                }

                            });
                } else {

                    JivePropertiesManager.addProperty(msg, "path",
                            message.getMedia_url());
                    try {
                        MessageEventManager.addNotificationsRequests(msg, true,
                                true, true, true);
                        chat.sendMessage(msg);
                    } catch (Exception e) {
                        // ACRA.getErrorReporter().handleException(e);
                        e.printStackTrace();
                    }

//                    Intent login_broadcast = new Intent("chat");
//                    login_broadcast.putExtra("jid", from);
//                    mParentActivity.getApplicationContext().sendBroadcast(login_broadcast);
                }
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                // TODO: handle exception
            }

            return url;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);


        }
    }

    Runnable mRunner = new Runnable() {
        @Override
        public void run() {
            try {
                mHandler.removeCallbacks(mRunner);
                mTempView.invalidate();
            } catch (Exception e) {

            }
        }
    };


    //Right Audio play and cancel audio modification...

    public class uploa_video extends AsyncTask<String, String, String> {

        MessageGetSet message = new MessageGetSet();
        String file_upload_res = null, url = null;
        String from, msg_id;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            Log.d("Upload_video", "Upload video called.......");
            Log.d("Message key id", "Message key id::" + params[0]);
            msg_id = params[0];
            message = dbAdapter.getMessages_by_msg_id(params[0]);
            dbAdapter.setUpdateMessage_need_push(message.getKey_id(), 2);
            dbAdapter.setUpdateMessage_status(message.getKey_id(), 3);
            from = params[1];

            try {
                ChatManager chatManager = ChatManager
                        .getInstanceFor(TempConnectionService.connection);
                final org.jivesoftware.smack.chat.Chat chat = TempConnectionService.chatmanager.createChat(params[1],
                        TempConnectionService.mChatCreatedListener.getMessageListener());
                final Message msg = new Message();
                // JivePropertiesManager.addProperty(msg,"msg_type", 2);
                msg.setStanzaId("" + message.getKey_id());
                msg.setBody("");
                // JivePropertiesManager.addProperty(msg,"path",upload_url+"uploads/video/"+file_upload_res);
                JivePropertiesManager.addProperty(msg, "media_type",
                        message.getMedia_wa_type());
                JivePropertiesManager.addProperty(msg, "mime_type",
                        message.getMedia_mime_type());
                JivePropertiesManager.addProperty(msg, "thumb_image",
                        Base64.encodeToString(message.getRow_data()));
                JivePropertiesManager.addProperty(msg, "media_duration",
                        message.getMedia_duration());
                JivePropertiesManager.addProperty(msg, "media_size",
                        message.getMedia_size() / 1054);
                  /*
                   * JivePropertiesManager.addProperty(msg,"is_sec_chat",
                   * message.getIs_sec_chat());
                   * JivePropertiesManager.addProperty(msg,"self_desc_time",
                   * message.getSelf_des_time());
                   */
                if (message.getIs_sec_chat() == 0) {
                    // JivePropertiesManager.addProperty(msg,"is_owner",
                    // message.getIs_owner());
                    JivePropertiesManager.addProperty(msg, "is_sec_chat",
                            message.getIs_sec_chat());
                    JivePropertiesManager.addProperty(msg, "self_desc_time",
                            message.getSelf_des_time());
                }

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
                                public void onProgress(int bytesWritten,
                                                       int totalSize) {
                                    // TODO Auto-generated method stub
                                    super.onProgress(bytesWritten, totalSize);
                                    float per = (100 * bytesWritten)
                                            / totalSize;
                                }

                                @Override
                                public void onCancel() {

                                    Constant.printMsg("video cancel upload...");

                                    super.onCancel();
                                }

                                @Override
                                public void onFailure(int arg0, Header[] position,
                                                      byte[] arg2, Throwable arg3) {

                                    dbAdapter.setUpdateMessage_need_push(message.getKey_id(), 1);
                                    dbAdapter.setUpdateMessage_status(message.getKey_id(), 3);
                                    file_upload_res = null;
                                    dbAdapter.setUpdateMessage_Media_url(
                                            msg_id, null);

                                    Intent login_broadcast = new Intent("chat");
                                    login_broadcast.putExtra("jid", from);
                                    mParentActivity.getApplicationContext().sendBroadcast(login_broadcast);

                                }

                                @Override
                                public void onSuccess(int arg0, Header[] position,
                                                      byte[] arg2) {

                                    String content = new String(arg2);
                                    Constant.printMsg("content::::>>>>"
                                            + content);

                                    dbAdapter.setUpdateMessage_status(
                                            message.getKey_id(), 2);
                                    String msgId = null;
                                    try {


                                        JSONObject jsonObject_Image = new JSONObject(content);
//
                                        url = jsonObject_Image.getString("url");
                                        msgId = jsonObject_Image.getString("msgId");


                                    } catch (JSONException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
                                    try {
                                        long l = dbAdapter
                                                .setUpdateMessage_Media_url(
                                                        msgId, url);

                                        JivePropertiesManager.addProperty(msg,
                                                "path", url);
                                    } catch (Exception e) {
                                        // ACRA.getErrorReporter().handleException(e);
                                        // TODO: handle exception
                                        e.printStackTrace();
                                    }
                                    try {
                                        MessageEventManager
                                                .addNotificationsRequests(msg,
                                                        true, true, true, true);
                                        chat.sendMessage(msg);

                                    } catch (Exception e) {
                                        // ACRA.getErrorReporter().handleException(e);
                                        // TODO: handle exception
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
                        chat.sendMessage(msg);
                    } catch (Exception e) {
                        // ACRA.getErrorReporter().handleException(e);
                        // TODO: handle exception
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

    private class getDonation extends AsyncTask<String, String, String> {

        // ProgressDialog progressdialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            // progressdialog = new ProgressDialog(getActivity());
            // progressdialog.setMessage(getResources()
            // .getString(R.string.loading));
            // progressdialog.show();
            Constant.printMsg("called");
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Constant.printMsg("details::::::::" + mFromNum + " to:::" + mToNUm
                    + "buxs:::::" + MBuxs);
            String result = null;
            HttpConfig ht = new HttpConfig();

            result = ht.httpget(KachingMeConfig.BUX_DONATION + mFromNum
                    + "&toNumber=" + mToNUm + "&buxs=" + MBuxs);
            Constant.printMsg("PRODUCT URL>>>>>>" + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // progressdialog.dismiss();
            try {
                Constant.printMsg("result is::::>>>>>>>>>>>" + result);

                if (result != null && result.length() > 0) {

                    if (result.equalsIgnoreCase("true")) {
                        Constant.printMsg("result is success::::>>>>>>>>>>>"
                                + result);

                        if (Constant.mBuxDonated == false) {
                            Constant.mBuxDonated = true;
                            Long point = sp1.getLong("buxvalue", 0);

                            Constant.userId = sp1.getLong("uservalue", 0);

                            Long dtpoint = sp1.getLong("donationpoint", 0);

                            Constant.bux = point;

                            Constant.point = dtpoint;

                            Long bx = Constant.donatepoint;
                            Long buxvalue = Constant.bux - bx;

                            Constant.bux = buxvalue;
                            Constant.printMsg("donated ::::>>> " + Constant.bux
                                    + "    " + bx);
                            Editor e = sp1.edit();
                            e.putLong("buxvalue", buxvalue);
                            e.putLong("uservalue", Constant.userId);
                            e.commit();

                            Constant.printMsg("consat:5555:"
                                    + Constant.donatepoint + Constant.bux + "vbx"
                                    + bx + "pt" + Constant.point);

                            Long pointdon = bx + Constant.point;

                            Editor e1 = sp1.edit();
                            e1.putLong("donationpoint", pointdon);
                            e1.commit();

                            Constant.point = pointdon;

                            System.out
                                    .println("name" + name + mToNUm + MBuxs
                                            + Constant.bux + bx + pointdon
                                            + Constant.point);

                            String mydate = java.text.DateFormat
                                    .getDateTimeInstance().format(
                                            System.currentTimeMillis());

                            SimpleDateFormat sdf = new SimpleDateFormat(
                                    "dd/MM/yyyy");

                            String today = sdf.format(new Date());

                            DonationDto dp = new DonationDto();

                            dp.setName(name);
                            dp.setDate(today);
                            dp.setPoint(String.valueOf(Constant.donatepoint));
                            dp.setStatus("debit");

                            Constant.donatelust.add(dp);
                            Constant.printMsg("name  " + name);
                            ContentValues cv = new ContentValues();
                            Constant.printMsg("nyms  " + today + "  "
                                    + Constant.donatepoint + "   " + name);
                            cv.put("date", today);
                            cv.put("points", Constant.donatepoint);
                            cv.put("name", name);
                            cv.put("status", "debit");

                            insertDBDonation(cv);
                        }
                        // Intent i = new Intent(getActivity(),
                        // NewBuxActivity.class);
                        // startActivity(i);
                        // getActivity().finish();
                    } else {
                        Toast.makeText(mParentActivity, "Invalid User", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {

                    Toast.makeText(mParentActivity, "Network Error..Try Again Later",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            }
        }

    }

    //Left Audio play and cancel audio modification...

    public class download_image extends AsyncTask<String, String, String> {
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

            file_name = "" + time;

            boolean success = (new File(Constant.local_image_dir)).mkdirs();
            if (!success) {
                Log.w("directory not created", "directory not created");
            }

            Constant.printMsg("UUUUURLLLL"+msg_url);

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
                            Intent login_broadcast = new Intent("chat");
                            login_broadcast.putExtra("jid", from);
                            getApplicationContext().sendBroadcast(
                                    login_broadcast);
                        }

                        @Override
                        public void onSuccess(int arg0, Header[] position,
                                              byte[] fileData) {
                            bites = (long) fileData.length;

                            long imMb = bites/(1024 * 1024);

                            Constant.printMsg("Download image " + imMb + "   " + Utils.getExternalFreeSpace(mParentActivity));

                            if(Utils.getExternalFreeSpace(mParentActivity)>200)
                            {
                                if((Utils.getExternalFreeSpace(mParentActivity)-imMb)>200)
                                {
                                    data1 = String.valueOf(String.format(
                                            Constant.local_image_dir + "%d.jpg", time));

                                    try {
//                                        Bitmap myBitmap = BitmapFactory
//                                                .decodeByteArray(fileData, 0,
//                                                        fileData.length);
                                        FileOutputStream stream = new FileOutputStream(
                                                data1);

//                                        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
//                                        myBitmap.compress(Bitmap.CompressFormat.JPEG,
//                                                85, outstream);
//                                        byte[] byteArray = outstream.toByteArray();

                                        stream.write(fileData);
                                        stream.close();

                                        dbAdapter.setUpdateMessage_MediaName(msg_id,
                                                file_name + ".jpg");


                                        Constant.printMsg("Media Rec" + bites);
                                        updateMediaNetwork_Receive(bites);

                                        Intent login_broadcast = new Intent("chat");
                                        login_broadcast.putExtra("jid", from);
                                        getApplicationContext().sendBroadcast(
                                                login_broadcast);

                                    } catch (Exception e) {
                                        // ACRA.getErrorReporter().handleException(e);
                                        // TODO: handle exception




                                    }
                                    return;
                                }
                            }

                            Intent login_broadcast = new Intent("no_memory");
                            login_broadcast.putExtra("jid", from);
                            getApplicationContext().sendBroadcast(
                                    login_broadcast);
                        }
                    });

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            super.onPostExecute(result);
        }
    }


    // ////////////////////////----Download
    // Video-----//////////////////////////////////////////////////////////////////////
    public class download_video extends AsyncTask<String, String, String> {
        String msg_id, msg_url, data1, from, file_name;

        @Override
        protected void onProgressUpdate(String... values) {
            Log.d("Image Downloading", "Downloading in Progress.." + values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            msg_id = params[0];
            msg_url = params[1];
            from = params[2];
            final long time = System.currentTimeMillis();

            file_name = "" + time;

            boolean success = (new File(Constant.local_video_dir)).mkdirs();
            if (!success) {
                Log.w("directory not created", "directory not created");
            }

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(60000);
            String[] allowedContentTypes = new String[]{"video/mp4"};
            client.get(msg_url,
                    new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] binaryData, Throwable error) {
                            // TODO Auto-generated method stub

                            // new AlertManager().showAlertDialog(context,
                            // "Sorry, Image downloading fail. Please Try again.",
                            // true);
                            Intent login_broadcast = new Intent("chat");
                            login_broadcast.putExtra("jid", from);
                            getApplicationContext().sendBroadcast(
                                    login_broadcast);
                            // super.onFailure(statusCode, headers, binaryData,
                            // error);
                        }


                        @Override
                        public void onSuccess(int arg0, Header[] position,
                                              byte[] fileData) {

                            bites = (long) fileData.length;
                            long imMb = bites/(1024 * 1024);

                            Constant.printMsg("Download image " + imMb + "   " + Utils.getExternalFreeSpace(mParentActivity));

                            if(Utils.getExternalFreeSpace(mParentActivity)>200) {
                                if ((Utils.getExternalFreeSpace(mParentActivity) - imMb) > 200) {
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


                                            Constant.printMsg("Media Rec" + bites);
                                            updateMediaNetwork_Receive(bites);

                                        } catch (FileNotFoundException e) {
                                            // Constant.printMsg("File not found" +
                                            // e);
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
                                        getApplicationContext().sendBroadcast(
                                                login_broadcast);

                                    } catch (Exception e) {
                                        // ACRA.getErrorReporter().handleException(e);
                                        // TODO: handle exception
                                    }
                                    return;
                                }
                            }

                            Intent login_broadcast = new Intent("no_memory");
                            login_broadcast.putExtra("jid", from);
                            getApplicationContext().sendBroadcast(
                                    login_broadcast);
                        }
                    });

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
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
    // Upload-----//////////////////////////////////////////////////////////////////////
    public class uploa_audio extends AsyncTask<String, String, String> {

        MessageGetSet message = new MessageGetSet();
        String file_upload_res = null, url = null;
        String from, msg_id;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            Log.d("Message key id", "Message key id::" + params[0]);
            message = dbAdapter.getMessages_by_msg_id(params[0]);
            from = params[1];
            msg_id = params[0];

            dbAdapter.setUpdateMessage_need_push(message.getKey_id(), 2);
            dbAdapter.setUpdateMessage_status(message.getKey_id(), 3);

            try {
                ChatManager chatManager = ChatManager
                        .getInstanceFor(TempConnectionService.connection);
                final org.jivesoftware.smack.chat.Chat chat = TempConnectionService.chatmanager.createChat(params[1],
                        TempConnectionService.mChatCreatedListener.getMessageListener());
                final Message msg = new Message();
                /* JivePropertiesManager.addProperty(msg,"msg_type", 3); */
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
                /*
                 * JivePropertiesManager.addProperty(msg,"is_sec_chat",
				 * message.getIs_sec_chat());
				 * JivePropertiesManager.addProperty(msg,"self_desc_time",
				 * message.getSelf_des_time());
				 */
                if (message.getIs_sec_chat() == 0) {
                    // JivePropertiesManager.addProperty(msg,"is_owner",
                    // message.getIs_owner());
                    JivePropertiesManager.addProperty(msg, "is_sec_chat",
                            message.getIs_sec_chat());
                    JivePropertiesManager.addProperty(msg, "self_desc_time",
                            message.getSelf_des_time());
                }
                DeliveryReceiptRequest dr = new DeliveryReceiptRequest();

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
                                public void onFailure(int arg0, Header[] position,
                                                      byte[] arg2, Throwable arg3) {
                                    dbAdapter.setUpdateMessage_status(
                                            message.getKey_id(), 3);
                                    // super.onFailure(arg3);
                                    file_upload_res = null;

                                    dbAdapter.setUpdateMessage_Media_url(
                                            msg_id, null);

                                    Intent login_broadcast = new Intent("chat");
                                    login_broadcast.putExtra("jid", from);
                                    getApplicationContext().sendBroadcast(login_broadcast);

                                }

                                @Override
                                public void onCancel() {

                                    Constant.printMsg("Audio upload cancel");
                                    super.onCancel();
                                }

                                @Override
                                public void onSuccess(int arg0, Header[] position,
                                                      byte[] arg2) {
                                    // TODO Auto-generated method stub
                                    String content = new String(arg2);
                                    Constant.printMsg("Audio upload cancel2"
                                            + content);
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
                                        MessageEventManager
                                                .addNotificationsRequests(msg,
                                                        true, true, true, true);

                                        Constant.printMsg("AUDIO DATAAAA" + msg.toString());

                                        chat.sendMessage(msg);

                                    } catch (Exception e) {
                                        // ACRA.getErrorReporter().handleException(e);
                                        // TODO: handle exception
                                        e.printStackTrace();
                                    }

                                    Intent login_broadcast = new Intent("chat");
                                    login_broadcast.putExtra("jid", from);
                                    getApplicationContext().sendBroadcast(login_broadcast);
                                }

                            });
                } else {
                    file_upload_res = message.getMedia_url();
                    JivePropertiesManager.addProperty(msg, "path",
                            file_upload_res);
                    try {
                        MessageEventManager.addNotificationsRequests(msg, true,
                                true, true, true);
                        chat.sendMessage(msg);
                    } catch (Exception e) {
                        // ACRA.getErrorReporter().handleException(e);
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    Intent login_broadcast = new Intent("chat");
                    login_broadcast.putExtra("jid", from);
                    getApplicationContext().sendBroadcast(login_broadcast);
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
                            // new AlertManager().showAlertDialog(context,
                            // "Sorry, Audio downloading fail. Please Try again.",
                            // true);
                            Intent login_broadcast = new Intent("chat");
                            login_broadcast.putExtra("jid", from);
                            getApplicationContext().sendBroadcast(
                                    login_broadcast);
                            // super.onFailure(statusCode, headers, binaryData,
                            // error);
                        }



                        @Override
                        public void onSuccess(int arg0, Header[] position,
                                              byte[] fileData) {


                            bites = (long) fileData.length;
                            long imMb = bites/(1024 * 1024);

                            Constant.printMsg("Download image " + imMb + "   " + Utils.getExternalFreeSpace(mParentActivity));

                            if(Utils.getExternalFreeSpace(mParentActivity)>200) {
                                if ((Utils.getExternalFreeSpace(mParentActivity) - imMb) > 200) {


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
                                            Constant.printMsg("Media Rec" + bites);
                                            updateMediaNetwork_Receive(bites);

                                        } catch (FileNotFoundException e) {
                                            // Constant.printMsg("File not found" +
                                            // e);
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

                                    } catch (Exception e) {
                                        // ACRA.getErrorReporter().handleException(e);
                                        // TODO: handle exception
                                    }

                                    return;
                                }
                            }

                            Intent login_broadcast = new Intent("no_memory");
                            login_broadcast.putExtra("jid", from);
                            getApplicationContext().sendBroadcast(
                                    login_broadcast);

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



    private void callBlockedUserAlert() {


        try {
            AlertDialog.Builder b;
            b = new AlertDialog.Builder(this);

            b.setCancelable(false);
            b.setMessage("Unblock this user to send message").setCancelable(
                    false);

            b.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //                unblock_all();

                }
            });

            b.setCancelable(true);

            AlertDialog alert = b.create();
            alert.show();
        } catch (Exception e) {

        }
    }

public void mAttachKachingFeature(){

    try{



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


    if (Constant.mKons == true) {
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

            Constant.mKons = false;
            Constant.printMsg("tedddddddddd  " + Constant.mKonsBackground + Constant.mKonsColor + Constant.mKonsText);
            sendMessage("<k>" + Constant.mKonsBackground + "-" + Constant.mKonsColor + "-" + Constant.mKonsText);
        }

    }

    if (Constant.mBazzle == true) {

        Constant.mBazzle = false;
        menuclick = false;

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

        sendMessage("<z>" + mLedText + "-" + Constant.mTimeZzle
                + "-" + Constant.mPreviewSpeed + "-"
                + Constant.mPreviewTextsize + "-" + Constant.shapeselected);

    }
    if (Constant.mBuxAccept == true) {
        Constant.mBuxAccept = false;
        contact = dbAdapter.getContact(jid);
        Constant.printMsg("conteac ::::: :::: " + contact);
        name = contact.getDisplay_name();
        String from_no = KachingMeApplication.getjid().split("@")[0];

        mFromNum = KachingMeApplication.getjid().split("@")[0];
        mToNUm = contact.getJid().split("@")[0];

        MBuxs = Constant.donatepoint;
        Constant.printMsg("bux ::::: " + name + " " + mFromNum + "  "
                + mToNUm);


        sendMessage("<a>" + Constant.mAcceptedBuxS);
        // new getDonation().execute();

    }
        if (Constant.mBuxReject)
        {
            Constant.mBuxReject = false;

            contact = dbAdapter.getContact(jid);
            Constant.printMsg("conteac rejected ::::: :::: " + contact);
            name = contact.getDisplay_name();
            String from_no = KachingMeApplication.getjid().split("@")[0];

            mFromNum = KachingMeApplication.getjid().split("@")[0];
            mToNUm = contact.getJid().split("@")[0];

            MBuxs = Constant.donatepoint;
            Constant.printMsg("bux ::::: " + name + " " + mFromNum + "  "
                    + mToNUm);

            sendMessage("<r>" + Constant.mRejectedBuxS);

        }
    if (Constant.mDonateBux == true) {
        Constant.mDonateBux = false;

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, +5);
        Constant.printMsg("dfreebie date ::::::" + c.getTime());
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Constant.printMsg("last date of freebie::::::::>>>>>>>"
                + df1.format(c.getTime()));
        String last = df1.format(c.getTime());

        int l = dbAdapter.getLastMsgid_chat(jid, sec);
        Constant.printMsg("Donate buxs msg_id sending method   " + l);
        sendMessage("<d>" + Constant.donatepoint + "-" + last);
    }
    if (Constant.mself_destruct_time == true) {
        Constant.mself_destruct_time = false;
        Constant.printMsg("called self destruct send condition");
        if (Constant.mself_destruct_msg.toString().length() != 0) {

            dest_msg_list.add(Constant.mselected_self_destruct_time);
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
            mDest = true;

            sendMessage("<s>" + Constant.mselected_self_destruct_time + "-"
                    + Constant.mself_destruct_msg);
        }
    }
    if (Constant.mZzle == true) {
        Constant.mZzle = false;
        if (Constant.mZzleText.length() != 0) {

            Constant.printMsg("syst" + Constant.mPreviewBackground
                    + Constant.mPreviewSpeed + Constant.mPreviewTextColor
                    + Constant.mPreviewTextsize + Constant.mZzleText);

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
            SimpleDateFormat df1 = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Constant.printMsg("last date of freebie::::::::>>>>>>>"
                    + df1.format(c.getTime()));
            String last = df1.format(c.getTime());
            Constant.printMsg("slider test "
                    + Constant.mPreviewBackground + "  "
                    + Constant.mPreviewSpeed + "  "
                    + Constant.mPreviewTextColor + "  "
                    + Constant.mPreviewTextsize + " " + Constant.mZzleText);
            sendMessage("<x>" + Constant.mPreviewBackground + "-"
                    + Constant.mPreviewSpeed + "-"
                    + Constant.mPreviewTextColor + "-"
                    + Constant.mPreviewTextsize + "-" + Constant.mZzleText
                    + "-" + last);
        }
    }


    if (Constant.karaoke == true) {

        Constant.printMsg("constant::" + Constant.file);
        upload_audio_File(Constant.file);
        Constant.karaoke = false;
    }

//    if (Constant.logo == true) {
//        Constant.printMsg("logog::" + Constant.logobit);
//        uploadLogo(Constant.logobit, true);
//        Constant.logo = false;
//    }
    Constant.printMsg("songListValue entered::");
    songListValue = Constant.songlist;
    if (songListValue) {
        Constant.printMsg("Constant.song_list.size()      "
                + Constant.song_list.size());
        for (int i = 0; i < Constant.song_list.size(); i++) {
            Constant.songPath = Constant.song_list.get(i).toString();

            Constant.printMsg("songListValue in chat::if"
                    + songListValue);
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
        Constant.printMsg("songListValue in chat::else" + songListValue);
    }
    }catch (Exception e){

    }
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


    public void updateForHomeScreenList()
    {
        MessageGetSet msggetset1 = new MessageGetSet();


        msggetset1.setData("");
        msggetset1.setKey_from_me(0);
        msggetset1.setKey_id("");
        msggetset1.setKey_remote_jid(jid);
        msggetset1.setNeeds_push(1);
        msggetset1.setSend_timestamp(new Date().getTime());
        msggetset1.setStatus(5);
        msggetset1.setTimestamp(new Date().getTime());
        msggetset1.setMedia_wa_type("40");
        msggetset1.setIs_sec_chat(1);
        msggetset1.setSelf_des_time(selected_self_desc_time);
        msggetset1.setIs_owner(IS_OWNER);

        Constant.printMsg("id::lll CCC:::::" + IS_OWNER + selected_self_desc_time+ " "+sec);

        dbAdapter.setInsertMessages(msggetset1);


        int msg_id = dbAdapter.getLastMsgid_chat(jid, sec);

        if (dbAdapter.isExistinChatList_chat(jid, sec)) {
            dbAdapter.setUpdateChat_lits_chat(jid, msg_id, sec);
        } else {
            dbAdapter.setInsertChat_list_chat(jid, msg_id, sec);
        }
    }



    public void getJidOnlineStatus(String jid)
    {
        try {
            Intent msgIntent = new Intent(ChatTest.this, GetJidPresenceIntentService.class);

            msgIntent.putExtra(GetJidPresenceIntentService.REQUEST_STRING, jid);
            startService(msgIntent);
        } catch (Exception e) {

        }

    }
}
