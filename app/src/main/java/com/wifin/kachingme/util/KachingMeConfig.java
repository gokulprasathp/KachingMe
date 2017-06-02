package com.wifin.kachingme.util;
/**
 * Created by siva(wifin)
 */
public class KachingMeConfig {

    /**Pointed at Production Server*/
//    public static String TEST_IP = "https://web.kaching.me/kachingme/rest/";
//    public static String MERI_IP = "https://web.kaching.me/kachingme/";
//    public static String PHP_IP = "http://webphp.kaching.me:8080/NiftyChat/";
//    public static String CHAT_SERVER = "chat.kaching.me";
//    public static String CHAT_HOST = "chat.kaching.me";
//    public static int CHAT_PORT = 5222;

    /**Pointed at ESDS Server*/
    public static String TEST_IP = "http://115.124.98.178:8080/kachingme/rest/";
    public static String MERI_IP = "http://115.124.98.178:8080/kachingme/";
    public static String PHP_IP = "http://115.124.98.178/NiftyChat/";
    public static String CHAT_SERVER = "115.124.98.178";
    public static String CHAT_HOST = "localhost";
    public static int CHAT_PORT = 5222;

    /**Registration Term and Condition Links*/

    public static String TERMS_OF_SERVICE = MERI_IP+"termsandconditionmob";
    public static String PRIVACY_POLICY =MERI_IP+ "privecyandpolicymob";
    /**Pointed at Java services*/

    public static String MERID_URL = MERI_IP+ "getDeelDiscountList.json?merchantId=";
    public static String REGISTER_URL = TEST_IP + "createuser.json?";
    public static String Contact_Us_URL = TEST_IP + "saveuserreports.json";
    public static String OTP_URL = TEST_IP + "getstzzlecampgndetails.json";
    public static String SECONDARY_URL = TEST_IP+ "addsecondarynumber.json?primaryNumber=";
    public static String SECONDARY_URL_RES = TEST_IP + "approvesecondary.json?secondaryNumber=";
    public static String Email_Verification = TEST_IP+ "emailverification.json?";
    public static String Delete_Account = TEST_IP + "deleteuseraccount.json?";
    public static String Delete_Secondary_Number = TEST_IP+ "deletesecondary.json?";
    public static String Contact_URL = TEST_IP + "savecontactlist.json";
    public static String KEYWORD_URL = TEST_IP + "getAllKeywords.json";
    public static String Dic_URL = TEST_IP+ "getMerchantDtoBykeyword.json?keyword=";
    public static String CART_URL = TEST_IP + "addcart.json";
    public static String REDEEM_SHOP_REGISTER = TEST_IP+ "registershopdetails.json";
    public static String REDEEM_SHOP_DATA = TEST_IP + "getshopbylocation.json?";
    public static String REDEEM_POST_URL = TEST_IP + "addclaimdetails.json";
    public static String BUX_DONATION = TEST_IP+ "buxdonation.json?senderNumber=";
    public static String GET_CARTDETAILS = TEST_IP+ "getuserdatas.json?phoneNumber=";
    public static String GET_PRIMARY_NUMBER = TEST_IP + "getstzzlecampgndetails.json";
    public static String UPDATE_PROFILE = TEST_IP + "updateUserProfilephoto.json?";
    public static String UPDATE_STATUS = TEST_IP + "updateUserProfileStatus.json?";
    public static String UPDATE_EMAIL_NAME = TEST_IP + "updateUserNameAndEmail?";
    public static String GET_LOGIN_OTP = TEST_IP + "getlogincheck.json";
    public static String POST_LOGIN_OTP = TEST_IP + "getloginresponse.json";
    public static String POST_LOGOUT = TEST_IP + "getLogout.json";
    public static String UPLOAD_MEDIA = TEST_IP+ "uploadM.json";
    public static String GET_GROUP_PROFILE = TEST_IP+ "getLatestGroupUrl";
    public static String GET_BUXS = TEST_IP+ "getBuxMaster.json";
    public static String Buxs = TEST_IP + "buxsactivitycounter.json";
    public static String RESEND_MISSED_CALL = TEST_IP + "getReSendMissedCall.json?";
    public static String FEEDBACK_USER = TEST_IP + "feedback.json";
    public static String POST_EARNED_BUXS = TEST_IP + "buxsearned.json";

    /**Pointed at php services*/

//    public static String FORGET_PASSWORD_PHP = PHP_IP+ "forgot_pass.php";
//    public static String IS_USER_REGISTER_PHP= PHP_IP+ "isuserregister.php";
//    public static String USER_GETDATA_PHP= PHP_IP+ "selectdata.php";
//    public static String UPLOAD_GROUP_ICON_PHP= PHP_IP+ "upload_groupicon.php";
//    public static String UPLOAD_MEDIA_PHP= PHP_IP+ "upload_media.php";
//    public static String REGISTRATION_PHP= PHP_IP+ "user_regisrtation.php";
//    public static String DELETE_USER_PHP= PHP_IP+ "deleteuser.php";
//    public static String USER_UPDATE_PHP= PHP_IP+ "insert_updatedata.php";
//    public static String UPLOAD_GROUP_ICON_FOLDER_JPEG_PHP= PHP_IP+ "uploads//groupicon//";
//    public static String UPLOAD_GROUP_ICON_FOLDER_PNG_PHP= PHP_IP+ "/uploads/groupicon/";
}
