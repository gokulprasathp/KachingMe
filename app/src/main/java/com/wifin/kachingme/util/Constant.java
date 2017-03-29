/*
* @author Sivanesan
*
* @usage -  This class to keep all static variables
*
* */

package com.wifin.kachingme.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Environment;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wifin.kachingme.adaptors.SpinnerValue;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.pojo.CartAdapDto;
import com.wifin.kachingme.pojo.Chat_list_home_GetSet;
import com.wifin.kachingme.pojo.ContactRedDto;
import com.wifin.kachingme.pojo.DicDto;
import com.wifin.kachingme.pojo.DonationDto;
import com.wifin.kachingme.pojo.FreeBieResponse;
import com.wifin.kachingme.pojo.FreebieMainDto;
import com.wifin.kachingme.pojo.LoginResponseDto;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.pojo.NymsPojo;
import com.wifin.kachingme.pojo.RedeemDto;
import com.wifin.kachingme.pojo.RestUserDetailsDto;
import com.wifin.kachingme.pojo.UserContactDto;
import com.wifin.kachingme.pojo.WishListDto;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smackx.privacy.PrivacyListManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Constant {

    // public static boolean currentPos = false;
    // public static int currentPosList;
    public static final String BROADCAST_COMPLETE_CONTACT_RE_SYNC_SERVICE = "Contact Update";
    public static final String BROADCAST_COMPLETE_CONTACT_RE_SYNC_SERVICE_LAST = "Contact Update";
    public static final String MESSAGE_KEY = "message";
    public static double SCREEN_WIDTH = 0, SCREEN_HEIGHT = 0;
    public static float widthinDp;
    public static float heightinDp;
    public static Boolean mPlainDazZ;
    public static ArrayList<Chat_list_home_GetSet> chatlist = new ArrayList<Chat_list_home_GetSet>();
    public static ArrayList nynmTitle = new ArrayList();
    public static ArrayList nynmMessage = new ArrayList();
    public static String mJid;
    public static String mDisplayName;
    public static String mSlectedActivity;
    public static String mLastName;
    public static int mChatListint11;
    public static String mDazZType;
    public static String mBackDazzColor;
    public static String mKonsColor = "";
    public static String mKonsBackground = "";
    public static ArrayList<String> konsBackgroundList = new ArrayList<String>();
    public static ArrayList<String> konsColorList = new ArrayList<String>();
    public static ArrayList<String> mNynmMeaningList = new ArrayList<String>();
    public static boolean emptyFreebie;
    public static boolean registrationLogin;
    public static boolean registrationLoginNoConnection;

    // public static String WEBSERVICE_HOST = "http://166.78.47.59/NiftyChat/";
    /* public static String WEBSERVICE_HOST = "http://192.168.1.11/NiftyChat/"; */
    public static String mNymTextValue;
    public static String jid;
    public static String mToastMsgDownload = "The download was unable to complete. Please try again later";
    // public static String COUNTRY_CODE = "91";
    // public static String SERVER = "166.78.47.59";
    // public static String HOST = "niftytest.com";
    public static int PHONE_NO_SIZE_MIN = 10;
    public static int PHONE_NO_SIZE_MAX = 12;
    public static Boolean DEBUG = true;
    public static String JID = KachingMeApplication.getUserID()
            + KachingMeApplication.getHost();
    public static String local_image_dir = Environment
            .getExternalStorageDirectory() + "/Kaching.me/Kaching.me Picture/";
    public static String local_video_dir = Environment
            .getExternalStorageDirectory() + "/Kaching.me/Kaching.me Video/";
    public static String local_audio_dir = Environment
            .getExternalStorageDirectory() + "/Kaching.me/Kaching.me Audio/";
    // public static String MEDIA_UPLOAD_URL =
    // WEBSERVICE_HOST+"upload_media.php";
    // public static String GROU_ICON_UPLOAD_URL =
    // WEBSERVICE_HOST+"upload_groupicon.php";
    public static String local_files_dir = Environment
            .getExternalStorageDirectory() + "/Kaching.me/Kaching.me File/";
    // public static String PROFILE_PIC_DIR = "/data/data/"
    // + KachingMeApplication.PACKAGE_NAME + "/profile_pic/";
    public static String local_database_dir = Environment
            .getExternalStorageDirectory() + "/Kaching.me/Kaching.me Backup/";
    public static String local_profile_picture_dir = Environment
            .getExternalStorageDirectory() + "/Kaching.me/ProfilePictures/";
    public static String ENCRYPTION_KEY = "password";
    public static String[] MIME_TYPES = {"image/gif", "video/mp4",
            "image/png", "*/*", "application/msword", "text/plain",
            "application/xml", "image/jpeg", "image/png", "image/tiff",
            "image/tiff-fx", "image/bmp", "image/x-bmp", "video/webm",
            "audio/webm", "video/quicktime", "video/vnd.avi", "video/avi",
            "video/msvideo", "video/x-msvideo", "video/MP2P", "video/MP1S",
            "video/x-ms-wmv", "video/x-flv", "video/mp4", "audio/mp4",
            "video/ogg", "audio/ogg", "application/ogg", "text/csv",
            "text/css", "text/html", "application/msword",
            "application/vnd.ms-excel", "application/vnd.ms-powerpoint",
            "application/pdf", "image/vnd.dxf", "application/octet-stream",
            "application/zip", "application/x-rar-compressed"};
    // Group Json object names
    public static String GROUP_ADMIN = "group_admin";
    public static String GROUP_TYPE = "group_type";
    public static String GROUP_TOPIC = "group_topic";
    public static String TOPIC_OPTION = "topic_options";
    public static String TIMESTAMP = "timestamp";
    public static String COUNTRY_CODE_LABEL = "countr_code";
    public static String BROADCAST_UPDATE_GROUP_ICON = "BROADCAST_UPDATE_GROUP_ICON";
    public static String CONTACT_GROUP_SYNC_STATUS = "CONTACT_GROUP_SYNC_STATUS";
    public static String PROFILE_UPDATE = "Profile_Update";
    public static String NIFTYCHAT_TEAMNO = "12054902604";
    public static String SUBJECTCHNAGE = "subjectchange_";
    public static String MEMBERREMOVEMESSAGE = "memberremovemessage_";


    /* Phase-2 Constants */
    public static String GROUPICONCHNAGE = "Groupiconchange_";
    public static String MEMBERADDMESSAGE = "memberaddmessage_";
    public static String BLOCKED_USERS = "blocked_users";
    // public static String WEBSERVICE_HOST =
    // "http://mobilewebs.net/mojoomla/demo/opencart/niftychat/";
	/*
	 * public static String INSERT_UPDATE_WEBSERVICE = WEBSERVICE_HOST +
	 * "insert_updatedata.php"; public static String SELECT_WEBSERVICE =
	 * WEBSERVICE_HOST + "selectdata.php"; public static String
	 * DELETE_WEBSERVICE = WEBSERVICE_HOST + "deletedata.php"; public static
	 * String SEARCH_WEBSERVICE = WEBSERVICE_HOST + "searchdata.php"; public
	 * static String Wquery = "Wquery";
	 *
	 * public static String Waction = "Waction"; public static String Wtable =
	 * "Wtable"; public static String Wtable1 = "Wtable1"; public static String
	 * WT1column = "WT1column"; public static String WT1cond = "WT1cond"; public
	 * static String WT1value = "WT1value";
	 *
	 * public static String Wtable2 = "Wtable2"; public static String WT2column
	 * = "WT2column"; public static String WT2cond = "WT2cond"; public static
	 * String WT2value = "WT2value";
	 *
	 * public static String Wcolumn = "Wcolumn"; public static String Wvalue =
	 * "Wvalue"; public static String Wcond = "Wcond";
	 *
	 * public static String Wcolumn1 = "Wcolumn1"; public static String Wvalue1
	 * = "Wvalue1"; public static String Wcond1 = "Wcond1"; public static String
	 * Wcontype = "Wcontype";
	 *
	 * public static String Wdeleteimagename = "Wdeleteimagename"; public static
	 * String Wdeletevideoname = "Wdeletevideoname"; public static String
	 * Wdeletepdfname = "Wdeletepdfname";
	 *
	 * public static String ID = "id"; public static String jid = "jid"; public
	 * static String TBL_EXPERIENCE = "tbl_experince"; public static String
	 * TBL_EDUCATION = "tbl_education"; public static String TBL_SKILLS =
	 * "tbl_skill"; public static String TBL_DESIGNATION = "tbl_designation";
	 * public static String TBL_CERTIFICATION = "tbl_certification"; public
	 * static String TBL_COMPANY = "tbl_company"; public static String
	 * TBL_PRODUCT = "tbl_product"; public static String TBL_PRODUCT_LIKE =
	 * "tbl_product_like"; public static String TBL_GALLERY = "tbl_gallery";
	 * public static String TBL_CONTACT = "tbl_contact";
	 */
    public static String BUSINESS_IMAGE_DIRECTORY = "uploads/";
    /* Search Score ratting */
    public static double search_scrore_update = 0.5;
    public static double search_score_like = 0.5;
    public static String LAST_REFRESH_TIME = "last_refresh_time";
    public static String NETWORK_STATUS = "network_status";
    public static String DEFAULT_SCROLL = "on";
    public static String CURRENT_LAT = "CURRENT_LAT";
	/*
	 * public static String
	 * MEDIA_UPLOAD_URL="http://192.168.1.123/NiftyChat/upload_media.php";
	 */


    /*siva integrationsa*/
    public static String CURRENT_LOG = "CURRENT_LOG";
    public static boolean group_lock = false;
    public static boolean pause_mode = false;
    public static String attchnymstring;
    public static boolean attachNym = false;
    public static boolean mFromGroupInfo = false;
    public static boolean ifSecondary = false;
    public static boolean mFromMemberList = false;
    public static int qty = 0;
    public static int cartqty = 0;
    public static String abbreviation;
    public static String menuId;
    public static String mgroupID;
    public static int selected_block_position = 0;

    public static boolean mDelete = false;
    public static List<CartAdapDto> cartfinal = new ArrayList<CartAdapDto>();
    public static ArrayList<String> mDonateBuxList = new ArrayList<String>();
    public static String Otp;
    public static boolean mTouch = false;

    public static String mPrimarynum;
    public static String mSong;
    public static String mtime;
    public static boolean mBackInvisible;
    public static String mFreePeeName;
    public static String mFreePeeShopName;
    public static String mFreeBieMerchantName;
    public static String mFreePeeShopId;
    public static String mFreePeeId;
    public static String mFreePeeShopAddress;
    public static String mCartType;
    public static HashMap<String, SpinnerValue> hm = new HashMap<String, SpinnerValue>();
    public static String mPhoneNum;
    public static boolean mIsRedeemBtnEnabled;
    public static boolean mFromsettingVerfication = false;
    public static boolean mFromDazzLib = false;
    public static boolean mFromDazzLed = false;
    public static String mVerifiedNum;
    public static boolean mDazzLib = false;
    public static String pushNum;
    public static Long mDonatedBux;
    public static String mPushDate;
    public static String mPushMsg;
    public static String mFreebieName;
    public static String mFreebieId;
    public static String mFreebieMerchantId;
    public static String mFreebieMerchantName;
    public static String mFreebieShopWebsite;
    public static String mFreebieShopName;
    public static String mFreebieShopId;
    public static String mFreebieShopAddress;
    public static String mFreebieShopEmailid;
    public static String mFreebieShopPhoneNumber;
    public static String mFreebieCurrentCountry;
    public static String mFreePeeMerchantId;
    public static String mFreePeeMerchantName;
    public static boolean mPrimarynumBtn = false;
    public static boolean mFromChatScreen = false;
    public static boolean mSenderScroll = false;
    public static boolean mFromDonateContact = false;
    public static String mSenderName;
    public static boolean mBuxDonated = false;
    public static boolean mFromGroup = false;

    public static Bitmap mProfileImage;

    public static List<ContactRedDto> contatclistmain = new ArrayList<ContactRedDto>();

    public static List<RestUserDetailsDto> restlistmain = new ArrayList<RestUserDetailsDto>();
    public static List<LoginResponseDto> listmainres = new ArrayList<LoginResponseDto>();
    public static List<FreeBieResponse> listmain = new ArrayList<FreeBieResponse>();
    public static List<DicDto> dictlist = new ArrayList<DicDto>();
    public static String mPreviewText;
    public static boolean karaokegroup = false;
    public static boolean mKonsFromSlider = false;
    public static boolean mKonsFromChat = false;
    public static boolean mKonsFromGroup = false;

    public static boolean mNynmFromSlider = false;
    public static boolean mNynmFromGroup = false;

    public static boolean mDazZFromSlider = false;
    public static boolean mKroKFromSlider = false;

    public static String filegroup;
    public static String mFreeBieMerchantImgUrl;
    public static String mFreeBieImgUrl;
    public static List<NymsPojo> NewNyms = new ArrayList();
    ;
    public static List<WishListDto> addedwish = new ArrayList<WishListDto>();

    public static int NymPosition;
    public static ArrayList Deallist = new ArrayList();
    public static ArrayList blinkPos = new ArrayList();
    public static String mselectedFreebie;
    public static ArrayList contactname = new ArrayList();
    public static ArrayList contactnumber = new ArrayList();

    public static boolean cartboolean = false;
    public static String profileimg;
    public static String profilename;
    public static String profilemail;
    public static String manualmail = "";
    public static int newstlistpos;

    public static List<String> popupwors = new ArrayList<String>();
    public static boolean addverification = false;
    public static ArrayList removepos = new ArrayList();
    public static String dob;
    public static String Imei_no = null;
    public static int mselectedPosition;
    // Google Project Number
    public static String GOOGLE_PROJECT_ID = "772480990282";
    // public static String GOOGLE_PROJECT_ID = "240648313288";

    public static String device_id = null;
    public static byte[] byteimage = null;
    public static Bitmap bitmapImage = null;
    public static int spinnerpos = 0;
    public static boolean ref;

    public static String responsecode;
    public static int deelpoints = 50;
    public static int nympoints = 50;
    public static int contactpoints = 50;
    public static int locationpoints = 25;
    public static int karaokepoints = 50;
    public static int intropoint = 50;
    public static int wishpoints = 50;
    public static int regpoints = 10000;


    public static boolean mselflabel = false;


    public static String gender;
    public static ArrayList<DonationDto> donatelust = new ArrayList<DonationDto>();
    public static String otpnumner;
    public static int totaldeel = 0;
    public static int totalnym = 0;
    public static int totalkar = 0;
    public static int totalcontact = 0;
    public static int totalwish = 0;
    public static String mFirstName;
    public static ArrayList<RedeemDto> redeemlist = new ArrayList<RedeemDto>();
    public static String mChatTextGroup = "";
    public static String wishlistname;
    public static Bitmap homebitmap;
    public static String fullmob;
    //public static ArrayList<String> muserlist = new ArrayList<String>();
    public static ArrayList<String> mstatuslist = new ArrayList<String>();
    public static int mposScroll = 0;
    public static boolean mDefaultScroll = true;
    public static boolean mFrebieScroll = false;
    public static String mFirstNameText;
    public static String mSecondNameText;
    public static String mPassword;
    public static String mRepassword;
    public static String mDateOfBirth;
    public static String mGender;
    public static boolean mFromVerfication = false;
    public static String songName;
    public static String touchTrue = "null";
    public static ArrayList<String> spinnerList = new ArrayList<String>();

    public static boolean freebieflag;

    public static boolean mServiceDone = false;

    public static String mCurrentUser = "";

    public static String TAP_HERE = "11";

    public static boolean isNiftyApplicationRunning = false;

    public static boolean isRegisteredBackPress = false;

    public static boolean chatOnCreate = true;
    public static boolean chatOnResume = true;

    public static PrivacyListManager privacyManager;
    public static String loginOtp;
    public static String loginCountryCode;
    public static String loginMobileNumber;
    public static boolean group_typingstatusListner = false;
    public static boolean isFbGplus;
    public static String wallType;
    public static boolean fromChat = false;
    public static boolean zzleFromgroup = false;
    public static String FROM_CHAT_SCREEN = "";
    public static Bitmap mReciverAvathor = null;
    public static String gifimage;
    public static boolean mFromGroupAudio = false;
    public static int screenWidth;
    public static int screenHeight;
    public static String message;
    public static ArrayList<String> konsSelectedList = new ArrayList<String>();
    public static int mobileno;
    public static String phone;
    public static String country;
    public static boolean zzle = false;
    public static boolean mKrokFromGroup = false;
    public static boolean FromMUC_Chat = false;
    public static int requestCode = 5;
    public static String mTimeZzle;
    public static boolean mDonateBux = false;
    public static boolean mBuxAccept = false;
    public static boolean mBuxReject = false;
    public static String shapeselected="";
    public static Long mAcceptedBuxS;
    public static Long mRejectedBuxS;
    public static List<FreebieMainDto> freelistmain = new ArrayList<FreebieMainDto>();
    public static String mKonsTextGroup = "";
    public static boolean mKonsGroup = false;
    public static boolean mBazzle = false;
    public static boolean mBazzleGroup = false;
    public static String mPreviewTextColor="";
    public static String mPreviewBackground="";
    public static String mPreviewSpeed="";
    public static String mPreviewTextsize="";
    public static boolean karaoke = false;
    public static boolean mDesTFromSlider = false;
    public static int mTimeFromSlider = 0;
    public static ArrayList<String> konsSelectedListgroup = new ArrayList<String>();
    public static String file;
    //public static CopyOfChatAdapter adapter;
//    public static ChatTest_Cursor_Adapter adapterTest_cursor;
//    public static BroadcastTestAdaptor mBroadAdptor;
//    public static MUCTestAdaptor adapter_muc_test;
    public static boolean sync = false;
    public static List<NymsPojo> addedNyms = new ArrayList();
    public static ArrayList chatdeel = new ArrayList();
    public static ArrayList blinkoffer = new ArrayList();
    public static String merchantid;
    public static String countrycode;
    public static String pass;
    public static String first;
    public static ArrayList<UserContactDto> contactlist = new ArrayList<UserContactDto>();
    public static ArrayList<CartAdapDto> cartadap = new ArrayList<CartAdapDto>();
    public static Long bux;
    public static Long userId;
    public static int chatpoints = 10;
    public static int imagepoints = 10;
    public static int zzlepoints = 50;
    public static int konspoint = 25;
    public static int desTpoint = 10;
    public static int wheRpoint = 25;
    public static int nynMpoint = 50;
    public static boolean logo = false;
    public static boolean logogroup = false;
    public static boolean mselfdestruct = false;
    public static byte[] logobit;
    public static byte[] logobitgroup;
    public static Long point;
    public static Long donatepoint;
    public static int totalchat = 0;
    public static int totalzzle = 0;
    public static int totaldest = 0;
    public static int totalloc = 0;
    public static int totalimg = 0;
    public static int totalkon = 0;
    public static String mChatText = "";
    public static String mDazZLibText = "";
    public static String mZzleText = "";
    public static String mKonsText = "";
    public static boolean mKons = false;
    public static boolean mZzle = false;
    public static String mZzleTextGroup = "";
    public static boolean mZzleGroup = false;
    public static String url;
    public static boolean freebie = false;
    public static boolean login = false;
    public static boolean settings = false;
    public static int mselected_self_destruct_time;
    public static boolean mself_destruct_time = false;
    public static String mself_destruct_msg;
    public static String mself_jid;
    public static int mself_id;
    public static ArrayList<MessageGetSet> msg_list_adapter = new ArrayList<MessageGetSet>();
    public static String mImagepath;
    public static String mReceiverId;
    public static String mSelectedMeaning = "";
    public static boolean songlist = false;
    public static String songPath;
    public static ArrayList<String> song_list = new ArrayList<String>();
    public static boolean fb;
    public static ArrayList<String> mList = new ArrayList<String>();
    public static boolean mServiceDoneGroup = false;
    public static String ONLINE_STATUS = "13";
    public static String TYPING_STATUS = "12";
    public static String TYPING_STATUS_GROUP = "typing";
    public static String TYPING_STATUS_RECORDING = "1214";
    public static String STATUS_RECORDING = "recording";
    public static String userStatus = "";
    public static boolean isOnline = false;
    public static boolean isModeDebug = true;
    public static boolean isMUC_Paused = false;
    public static AbstractXMPPConnection connection;
    public static char mNynmsSpecialCharacter = '.';
    public static String TYPING_STRING = "typing";
    public static String TYPING_OnLine = "online";
    public static String STATUS_DELIVERED = "delivered";
    public static String STATUS_DISPLAYED = "displayed";
    public static String CURRENT_OPEN_JID = "";
    public static boolean USER_ONLINE_CHEKING_FOR_SEND_PRESENCE = false;
    public static ArrayList mDictionaryList = new ArrayList();
    public static ArrayList mDictionaryMeaningList = new ArrayList();
    public static int width;
    public static int height;
    public static boolean mFromContactScreen = false;
    public static ArrayList mSelectedImage = new ArrayList();
    public static boolean mFromBrodAudio = false;
//    public static BroadcastTestAdaptor adapterTest_Broad;
    public static boolean mIsLogin = false;
    public static String mNymnQuirePosition = "";
    public static Bitmap mGroupInfoProfile=null;
    public static boolean isFront = false;
    public static boolean isFirebaseBitmap=false;

    public static String jsonString_Builder = null;
    public static List<Chat_list_home_GetSet> chatHomeList = new ArrayList<Chat_list_home_GetSet>();
    public static boolean isOtpVerification;

    public static void typeFaceKons(Context myContext, TextView type) {
        type.setTypeface(Typeface.createFromAsset(myContext.getAssets(),
                "fonts/kons.ttf"));
    }

    public static void typeFace(Context myContext, TextView type) {
        type.setTypeface(Typeface.createFromAsset(myContext.getAssets(),
                "fonts/SEGOEUI.TTF"));
    }

    public static void typeFace(Context myContext, EditText type) {
        type.setTypeface(Typeface.createFromAsset(myContext.getAssets(),
                "fonts/SEGOEUI.TTF"));
    }

    public static void typeFace(Context myContext, Button type) {
        type.setTypeface(Typeface.createFromAsset(myContext.getAssets(),
                "fonts/SEGOEUI.TTF"));
    }

    public static void typeFace(Context myContext, RadioButton type) {
        type.setTypeface(Typeface.createFromAsset(myContext.getAssets(),
                "fonts/SEGOEUI.TTF"));
    }

    public static void typeFace(Context myContext, CheckBox type) {
        type.setTypeface(Typeface.createFromAsset(myContext.getAssets(),

                "fonts/SEGOEUI.TTF"));
    }

    public static void typeFaceBold(Context myContext, TextView type) {
        type.setTypeface(Typeface.createFromAsset(myContext.getAssets(),
                "fonts/SEGOEUI.TTF"));
    }

    public static void printMsg(String msg) {
        // TODO Auto-generated method stub
        System.out.println("siva kachingme check......." + msg);
    }
}
