package com.wifin.kachingme.services;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.async_tasks.ResendMessageAsync;
import com.wifin.kachingme.chat.muc_chat.MUCTest;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.listeners.ChatCreatedListener;
import com.wifin.kachingme.listeners.MUC_InvitationListener;
import com.wifin.kachingme.listeners.MUC_ListenerMethods;
import com.wifin.kachingme.listeners.MUC_MessageListener;
import com.wifin.kachingme.listeners.MUC_SubjectChangeListener;
import com.wifin.kachingme.listeners.MessageEventNooficationList;
import com.wifin.kachingme.listeners.RecieptRecievedListener;
import com.wifin.kachingme.listeners.RosterListener;
import com.wifin.kachingme.pojo.LoginGetSet;
import com.wifin.kachingme.registration_and_login.OtpVerification;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.Utils;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.Roster.SubscriptionMode;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.dns.HostAddress;
import org.jivesoftware.smackx.bookmarks.BookmarkManager;
import org.jivesoftware.smackx.bookmarks.BookmarkedConference;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MucEnterConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.ping.packet.Ping;
import org.jivesoftware.smackx.privacy.PrivacyListManager;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.xevent.MessageEventManager;
import org.jivesoftware.smackx.xroster.RosterExchangeManager;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;



import javax.net.ssl.SSLContext;

import de.duenndns.ssl.MemorizingTrustManager;

import static org.jivesoftware.smack.filter.jidtype.AbstractJidTypeFilter.JidType.EntityBareJid;


public class TempConnectionService extends Service implements
        ConnectionListener {

    private static final String PING_ALARM = "org.yaxim.androidclient.PING_ALARM";
    private Intent mPingAlarmIntent = new Intent(PING_ALARM);
    private static final String PONG_TIMEOUT_ALARM = "org.yaxim.androidclient.PONG_TIMEOUT_ALARM";
    private Intent mPongTimeoutAlarmIntent = new Intent(PONG_TIMEOUT_ALARM);
    // private Service mService;
    final static private int PACKET_TIMEOUT = 60000;
    public static AbstractXMPPConnection connection;
    public static DeliveryReceiptManager mDeliveryReciept;
    public static MessageEventManager messageEventManager;
    public static ChatCreatedListener mChatCreatedListener;
    public static MultiUserChatManager MUC_MANAGER;
    public static MUC_MessageListener muc_messageListener;
    public static MultiUserChat muc;
    public static PrivacyListManager privacymanager;
    public static DeliveryReceiptManager drm;
    public static ChatManager chatmanager;
    public static Roster roster;
    public static ReconnectionManager mReconnectionManager;
    public DatabaseHelper dbadapter;
    SharedPreferences sp;

    // long rand = 0;
    boolean login_attempt = false;
    Editor editor;
    Context context = this;
    Thread mAuthendicationChecking = null;
    Handler mHandler = new Handler(Looper.getMainLooper());
    private AlarmManager mAlarmManager;
    private StanzaListener mPongListener;
    private String mPingID;
    private long mPingTimestamp;
    private PendingIntent mPingAlarmPendIntent;
    private PendingIntent mPongTimeoutAlarmPendIntent;
    private PongTimeoutAlarmReceiver mPongTimeoutAlarmReceiver = new PongTimeoutAlarmReceiver();
    private BroadcastReceiver mPingAlarmReceiver = new PingAlarmReceiver();

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Constant.printMsg("DDDDDDDDDDDDDDDDDDDDDD  call service");

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                KachingMeApplication.setIsNetAvailable(true);

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                KachingMeApplication.setIsNetAvailable(true);
        } else {
            KachingMeApplication.setIsNetAvailable(false);
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                if (connection != null) {
                    if (connection.isConnected() == false)
                        try {
                            setConnection();
                        } catch (XmppStringprepException e) {
                            e.printStackTrace();
                        }
                } else {
                    try {
                        setConnection();
                    } catch (XmppStringprepException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setConnection() throws XmppStringprepException {

        dbadapter = KachingMeApplication.getDatabaseAdapter();
        ArrayList<LoginGetSet> mCredentialList = new ArrayList<LoginGetSet>();
        mCredentialList = dbadapter.getLogin();
        String userName = null;
        String PassWord= null;
        if (mCredentialList.size() > 0) {

            userName = mCredentialList.get(0).getUserName();
            PassWord = mCredentialList.get(0).getPassword();

        }

        context = this;

        DomainBareJid domainBareJid = JidCreate.domainBareFrom(KachingMeConfig.CHAT_HOST) ;
        XMPPTCPConnectionConfiguration.Builder configBuilder=
                XMPPTCPConnectionConfiguration.builder();
        configBuilder.setServiceName(domainBareJid);
        try {
            configBuilder.setHostAddress(InetAddress.getByName(KachingMeConfig.CHAT_SERVER));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        configBuilder.setUsernameAndPassword(userName, PassWord);
        configBuilder.setPort(KachingMeConfig.CHAT_PORT);
        configBuilder.setResource("Messnger");

//        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration
//                .builder();
//        // configBuilder.setUsernameAndPassword("username", "password");
//        configBuilder.setResource("Messnger");
//        configBuilder.setUsernameAndPassword(userName,PassWord);
//        configBuilder.setXmppDomain((DomainBareJid)JidCreate.bareFrom(KachingMeConfig.CHAT_HOST));
//        configBuilder.setPort(KachingMeConfig.CHAT_PORT);
//        configBuilder.setHost(KachingMeConfig.CHAT_SERVER);
        configBuilder.setSendPresence(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            configBuilder.setKeystoreType("AndroidCAStore");
            // config.setTruststorePassword(null);
            configBuilder.setKeystorePath(null);
        } else {
            configBuilder.setKeystoreType("BKS");
            String path = System.getProperty("javax.net.ssl.trustStore");
            if (path == null)
                path = System.getProperty("java.home") + File.separator + "etc"
                        + File.separator + "security" + File.separator
                        + "cacerts.bks";
            configBuilder.setKeystorePath(path);

        }

        configBuilder
                .setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible);
        configBuilder.setDebuggerEnabled(true);

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, MemorizingTrustManager.getInstanceList(context),
                    new SecureRandom());
            configBuilder.setCustomSSLContext(sc);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (KeyManagementException e) {
            throw new IllegalStateException(e);
        }
        XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
        XMPPTCPConnection.setUseStreamManagementDefault(true);

        SASLAuthentication.unBlacklistSASLMechanism("SCRAM-SHA-1");
        SASLAuthentication.blacklistSASLMechanism("MD5");

        connection = new XMPPTCPConnection(configBuilder.build());
        connection.setPacketReplyTimeout(6000000L);
        connection.addConnectionListener(this);
//        connection.setReplyToUnknownIq(true);

        ((XMPPTCPConnection) connection).setUseStreamManagement(true);

        // connection.addConnectionListener(new connection_listeners());

//        Log.d("Connection", "Method called..");

        // if(RestartNifyService.isOnline(NiftyService.this))
        try {
            // Constant.printMsg("DDDDDDDDDDDD" + connection.isConnected());
//            if(connection!=null)
//            Constant.printMsg("DDDDDDDDDZZZZZZ" + connection.isConnected()+"  "+connection.isAuthenticated());


            connection.connect().login();

            mReconnectionManager = ReconnectionManager
                    .getInstanceFor(connection);
            mReconnectionManager.enableAutomaticReconnection();

            Constant.printMsg("DDDDDDDDDDDD" + connection.isConnected()
                    + "          " + connection.isAuthenticated());

            Constant.printMsg("DDDDDDDDDDDD" + connection.isConnected()
                    + "        " + connection.isAuthenticated());

        } catch (SmackException.ConnectionException e) {
            Constant.printMsg("DDDDDDDDDDDD2" + e.toString());

//            Constant.printMsg("DDDDDDDDDDDD1222" + e.getLocalizedMessage().toString()+ " " + e.getMessage().toString()+ " " + e.getCause().toString());
//            for (HostAddress obj : e)
//            {
//                Constant.printMsg("DDDDDDDDDDDD1" + obj.toString());
//            }
        } catch (IOException e) {
            // TODO Auto-generated catch block

            Constant.printMsg("DDDDDDDDDDDD2" + e.toString());
            e.printStackTrace();
        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            Constant.printMsg("DDDDDDDDDDDD3" + e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            Constant.printMsg("DDDDDDDDDDDD4" + e.toString());

        }

        try {

            mConnectionAliveChecking();

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void reConnection() throws SmackException {

    }

    @Override
    public void authenticated(XMPPConnection arg0, boolean arg1) {
        // TODO Auto-generated method stub

//        if (Signin.isLogintry){
//            Constant.printMsg("login inside Tempconnection for login autentication");
//            CommonMethods com = new CommonMethods(context);
//            com.loginUserProcess();
//            Signin.isLogintry=false;
//        }

        // Re_connecting_groups();

        // ServerPingWithAlarmManager.getInstanceFor(connection);
        //   ServerPingWithAlarmManager.onCreate(context);
        try {
            this.mAlarmManager = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            context.registerReceiver(mPingAlarmReceiver, new IntentFilter(
                    PING_ALARM));
            context.registerReceiver(mPongTimeoutAlarmReceiver, new IntentFilter(
                    PONG_TIMEOUT_ALARM));
            // reference PingManager, set ping flood protection to 10s
            PingManager pingManager = PingManager.getInstanceFor(connection);
            pingManager.setPingInterval(60 * 1000);
            registerPongListener();
        } catch (Exception e) {

            e.printStackTrace();
        }


        commonListeners();

        Constant.printMsg("DDDDDONE Resend " + connection.isAuthenticated());

        if (connection.isAuthenticated()) {
            new ResendMessageAsync(this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

            if (Utils.isActivityIsFront(context, ChatTest.class.getCanonicalName().toString())) {
                Intent login_broadcast;
                login_broadcast = new Intent("remove_subtitle");
                context.sendBroadcast(login_broadcast);
            }
        }

        Constant.printMsg("DDDDDONE 0111");

        // if (Chat.IS_Front) {

        try {
            // Presence presence = new Presence(Presence.Type.available);
            // presence.setMode(Mode.available);
            // //
            // presence.setProperty("is_lastseen",sharedPrefs.getBoolean("privacy_lastseen",
            // // true));
            // connection.sendStanza(presence);

        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            Constant.printMsg("DDDDDD presence exp " + e.toString());
        }

        ChatTest.connection = connection;
        ChatTest.messageEventManager = messageEventManager;
        // Chat.mDeliveryReciept = mDeliveryReciept;

        // } else if (MUC_Chat.IS_Front) {
        sp = context.getSharedPreferences(
                KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
        editor = sp.edit();

        MUCTest.connection = connection;
        MUCTest.messageEventManager = messageEventManager;

        if (muc != null) {

            try {

                Constant.printMsg("DDDDDDDDDDDDDDDD" + muc.isJoined()
                        + "          " + MUCTest.jid + "       "
                        + KachingMeApplication.getUserID() + "      "
                        + KachingMeApplication.getHost());

                MUCTest.muc = muc;
                // NewGroup_MemberList.muc = muc;

            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
                e.printStackTrace();

                Constant.printMsg("DDDDDDDDDDDDDDDD" + e.toString());
            }
        }
        // }

    }

    @Override
    public void connected(XMPPConnection arg0) {
        // TODO Auto-generated method stub

        // Presence pr = new Presence(Presence.Type.available);
        // History his = new History("67766");
        // pr.addExtension(his);
        //
        // Constant.printMsg("DDDDDD presence XML " + pr.toXML());
        // ReconnectionManager.getInstanceFor(connection)
        // .enableAutomaticReconnection();
        // final Roster roster = Roster
        // .getInstanceFor(TempConnectionEstablishment.connection);
        // Presence p = roster.getPresence("919001122334@localhost");
        // Presence pr = new Presence(Presence.Type.available, "I am busy", 42,
        // Mode.dnd);
        //
        // try {
        // connection.sendStanza(pr);
        // } catch (Exception e1) {
        // // TODO Auto-generated catch block
        // Constant.printMsg("CCCCCCCC222" + e1.toString());
        // }

        PrivacyListManager.getInstanceFor(connection);
        //
        // Log.d("NiftyService", " Presence Roster Request Listener Added");
        Roster.setDefaultSubscriptionMode(SubscriptionMode.accept_all);
        roster = Roster.getInstanceFor(connection);
        roster.setSubscriptionMode(SubscriptionMode.accept_all);
        roster.addRosterListener(new RosterListener(context));
        //
        // // Presence subsription
        // if (Chat.jid != null) {
        // Log.d("NiftyService", " Presence subscription");
        // Presence presencePacket = new Presence(Presence.Type.subscribe);
        // presencePacket.setTo(Chat.jid);
        // try {
        // connection.sendStanza(presencePacket);
        // } catch (NotConnectedException e1) {
        // // TODO Auto-generated catch block
        // e1.printStackTrace();
        // }
        //
        // }
        if (Constant.registrationLoginNoConnection) {
            Constant.registrationLoginNoConnection = false;
            android.os.Message m = new android.os.Message();
            Bundle b = new Bundle();
            b.putInt("what", 2);
            m.setData(b);
            OtpVerification.mHandelSininMsg.sendMessage(m);
        }
        Constant.printMsg("DDDDDONE 0");

        authenticationProcess();




    }

    @Override
    public void connectionClosed() {

    }

    @Override
    public void connectionClosedOnError(Exception arg0) {

    }

    @Override
    public void reconnectingIn(int arg0) {
        // TODO Auto-generated method stub
        Constant.printMsg("DDDDDONE 3 reconnectingIn" + arg0);

    }

    @Override
    public void reconnectionFailed(Exception arg0) {

        // Constant.printMsg("DDDDDONE 4 reconnectionFailed" +
        // arg0.toString());
        //
        // try {
        // reConnection();
        // } catch (SmackException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

    }

    @Override
    public void reconnectionSuccessful() {
        // TODO Auto-generated method stub

        Re_connecting_groups();

    }

    public void authenticationProcess() {
        if (!connection.isAuthenticated()) {
            dbadapter = KachingMeApplication.getDatabaseAdapter();
            ArrayList<LoginGetSet> mCredentialList=new ArrayList<LoginGetSet>();
            mCredentialList = dbadapter.getLogin();
            if (mCredentialList.size() > 0) {
                try {
                    String userName = mCredentialList.get(0).getUserName();
                    String PassWord = mCredentialList.get(0).getPassword();

                    Constant.printMsg("DDDDDLOGIN  "
                            + userName + "---"
                            + PassWord + "---"
                            + "Messnger");

//                    connection.login(userName,
//                            PassWord,
//                            "Messnger");

//                    if (Constant.mIsLogin) {
//                        Constant.mIsLogin = false;
//                        Signin.mHandelSininMsg.sendEmptyMessage(1);
//                    }
                    if (Constant.registrationLogin) {
                        Constant.printMsg("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJPPPPP inside Connection boolean .." + Constant.registrationLogin);
                        Constant.registrationLogin = false;
                        android.os.Message m = new android.os.Message();
                        Bundle b = new Bundle();
                        b.putInt("what", 1);
                        m.setData(b);
                        OtpVerification.mHandelSininMsg.sendMessage(m);
                    }
                    // Used to send ack for received messages

                    final ArrayList mFinalPendingDeliveryAckMessege = new ArrayList();

                    final SharedPreferences myPrefs = this
                            .getSharedPreferences("pending_msg_ack",
                                    MODE_PRIVATE);

                    mFinalPendingDeliveryAckMessege.addAll(myPrefs
                            .getStringSet("pending_delivery_msg", null));

                    for (int i = 0; i < mFinalPendingDeliveryAckMessege.size(); i++) {

                        String[] mData = String.valueOf(
                                mFinalPendingDeliveryAckMessege.get(i)).split(
                                "#");
                        if (mData.length >= 2) {
                            String Messegeid = mData[0];
                            String Messegejid = mData[1];

                            final Message ack = new Message(Messegejid,
                                    Constant.STATUS_DISPLAYED);

                            ack.addExtension(new DeliveryReceipt(Messegeid));

                            if (TempConnectionService.connection.isConnected()) {

                                try {
                                    TempConnectionService.connection
                                            .sendStanza(ack);
                                    mFinalPendingDeliveryAckMessege.remove(i);

                                } catch (SmackException.NotConnectedException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                    }

                    final Set mFinalPendingDeliveryAckMessegeSet = new HashSet();
                    mFinalPendingDeliveryAckMessegeSet
                            .addAll(mFinalPendingDeliveryAckMessege);
                    Editor prefsEditor = myPrefs.edit();
                    prefsEditor.putStringSet("pending_delivery_msg",
                            mFinalPendingDeliveryAckMessegeSet);
                    prefsEditor.commit();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Constant.printMsg("DDDDDONE login " + e.toString());
                }
            }
        }
    }

    public void commonListeners() {
        // Common connection establish for UnUi Classes
        mChatCreatedListener = new ChatCreatedListener(context,
                KachingMeApplication.getjid());
        Constant.printMsg("Commecton jid" + KachingMeApplication.getjid());

        ChatTest.mChatCreatedListener = mChatCreatedListener;

        //
        messageEventManager = MessageEventManager
                .getInstanceFor(TempConnectionService.connection);
        messageEventManager
                .addMessageEventNotificationListener(new MessageEventNooficationList(
                        context));

        mDeliveryReciept = DeliveryReceiptManager
                .getInstanceFor(TempConnectionService.connection);
        // drm.autoAddDeliveryReceiptRequests();
        mDeliveryReciept.getAutoReceiptMode();
        mDeliveryReciept
                .addReceiptReceivedListener(new RecieptRecievedListener(
                        context));



        chatmanager = ChatManager.getInstanceFor(connection);
        chatmanager.addChatListener(mChatCreatedListener);

        // ProviderManager.addExtensionProvider(DeliveryReceipt.ELEMENT,
        // DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());
        // ProviderManager.addExtensionProvider(DeliveryReceiptRequest.ELEMENT,
        // new DeliveryReceiptRequest().getNamespace(),
        // new DeliveryReceiptRequest.Provider());
        // drm = DeliveryReceiptManager.getInstanceFor(connection);
        // drm.autoAddDeliveryReceiptRequests();
        // drm.getAutoReceiptMode();
        // drm.addReceiptReceivedListener(new
        // RecieptRecievedListener(context));


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                     addListeners();
                } catch (InterruptedException e) {

                }
            }
        }).start();

    }

    public void addListeners() {

        try {


                login_attempt = true;
            /*
             * ConfigureProviderManager cp = new ConfigureProviderManager();
			 * cp.configureProviderManager();
			 */

			/*
             * ServiceDiscoveryManager sdm = ServiceDiscoveryManager
			 * .getInstanceFor(connection);
			 * sdm.addFeature("http: / / jabber.org / protocol / disco # info "
			 * ); sdm.addFeature(" http://jabber.org/protocol/caps ");
			 * sdm.addFeature(" urn: xmpp: avatar: metadata ");
			 * sdm.addFeature("urn: xmpp: avatar: metadata + notify");
			 * sdm.addFeature("urn: xmpp: avatar: data");
			 * sdm.addFeature("http://jabber.org/protocol/nick");
			 * sdm.addFeature("http://jabber.org/protocol/nick+notify");
			 * sdm.addFeature("http://jabber.org/protocol/xhtml-im");
			 * sdm.addFeature("http:// jabber.org / protocol / muc ");
			 * sdm.addFeature(" http://jabber.org/protocol/commands ");
			 * sdm.addFeature
			 * (" http://jabber.org/protocol/si/profile/file- transfer ");
			 * sdm.addFeature(" http://jabber.org/protocol/si ");
			 * sdm.addFeature(" http://jabber.org/protocol/bytestreams ");
			 * sdm.addFeature(" http:/ / jabber.org / protocol / ibb ");
			 * sdm.addFeature(" http://jabber.org/protocol/feature-neg ");
			 * sdm.addFeature(" jabber: iq: privacy ");
			 * sdm.addFeature("http://jabber.org/protocol/muc#user");
			 * sdm.addFeature("http://jabber.org/protocol/muc#admin");
			 * sdm.addFeature("http://jabber.org/protocol/muc#owner");
			 * sdm.addFeature("http://jabber.org/protocol/muc#owner");
			 * Log.d("Listener", "Listener Method called.....");
			 * ProviderManager.addExtensionProvider(DeliveryReceipt.ELEMENT,
			 * DeliveryReceipt.NAMESPACE,new DeliveryReceipt.Provider());
			 * ProviderManager
			 * .addExtensionProvider(DeliveryReceiptRequest.ELEMENT,new
			 * DeliveryReceiptRequest().getNamespace(),new
			 * DeliveryReceiptRequest.Provider());
			 */

            drm = DeliveryReceiptManager.getInstanceFor(connection);
            // drm.autoAddDeliveryReceiptRequests();
            drm.getAutoReceiptMode();
            drm.addReceiptReceivedListener(new RecieptRecievedListener(
                    context));

            RosterExchangeManager rem = new RosterExchangeManager(connection);
            // rem.addRosterListener(new RosterExchange_impl(connection));

            muc_messageListener = new MUC_MessageListener(context, context);

            MUC_MANAGER = MultiUserChatManager.getInstanceFor(connection);
            MUC_MANAGER.addInvitationListener(new MUC_InvitationListener(
                    context, context));

            BookmarkManager bm = BookmarkManager.getBookmarkManager(connection);
            ArrayList<String> rm_bm = new ArrayList<String>();

            Collection<BookmarkedConference> l = bm.getBookmarkedConferences();
            Constant.printMsg("BookMarked Rooms:" + l.size());
            for (BookmarkedConference bookmarkedConference : l) {
                RoomInfo room_info = null;
                try {

                    if (bookmarkedConference.getJid().toString().length() > 0) {
                        // Create group in local database when re logging in device
                        if (!dbadapter.isjidExist(bookmarkedConference.getJid().toString())) {

                            Constant.printMsg("CCCC login grp + " + bookmarkedConference.getJid());

                            new MUC_ListenerMethods(context)
                                    .setJoinRoom(bookmarkedConference.getJid().toString(), null);

                            continue;
                        }

                        room_info = MUC_MANAGER.getRoomInfo(bookmarkedConference
                                .getJid());
                    }
                } catch (Exception e) {

                    // ACRA.getErrorReporter().handleException(e);
                }
                if (room_info != null) {
                    try {

                        SharedPreferences sp_1 = KachingMeApplication
                                .getsharedpreferences();

                        String last = sp_1.getString(
                                Constant.LAST_REFRESH_TIME + "_"
                                        + bookmarkedConference.getJid(), null);
                        long m = 0;
                        // long now = new Date().getTime();
                        // if (last != 0) {
                        // m = (now - last) / 1000;
                         // }
//                        Log.d("MMM", "Last Refresh time::" + last + " m::"
//                                + bookmarkedConference.getNickname() + "  "
//                                + new Date());
                        // Log.d(TAG,
                        // "Bookmark_time::"
                        // + bookmarkedConference.getNickname());

                        Constant.printMsg("PPPPPPPPPPPPPPPPPPPPP"
                                + bookmarkedConference.getJid());

                        muc = MultiUserChatManager
                                .getInstanceFor(connection)
                                .getMultiUserChat(bookmarkedConference.getJid());

                        muc.addMessageListener(muc_messageListener);
                        muc.addSubjectUpdatedListener(new MUC_SubjectChangeListener(
                                context));

                        MucEnterConfiguration.Builder build =  muc.getEnterConfigurationBuilder(Resourcepart.from(dbadapter.getLogin().get(0).getUserName()
                                + KachingMeApplication.getHost()));

                        build.requestHistorySince(Utils.getBookmarkDate(last));
                        build.timeoutAfter(6000000L);

                        MucEnterConfiguration musOb =  build.build();

                        if (!muc.isJoined()) {
                            muc.join(musOb);

                            Constant.printMsg("RRRRR temp Conn 1111"
                                    + bookmarkedConference.getJid() + "hhhhhhhh"
                                    + dbadapter.getLogin().get(0).getUserName()
                                    + KachingMeApplication.getHost());
                        }
                        Constant.printMsg("RRRRR temp Conn "
                                + bookmarkedConference.getJid() + "hhhhhhhh"
                                + dbadapter.getLogin().get(0).getUserName()
                                + KachingMeApplication.getHost());

                    } catch (Exception e) {
                        // TODO: handle exception
                        // //ACRA.getErrorReporter().handleException(e);
                        e.printStackTrace();
                    }
                } else {
                    rm_bm.add(bookmarkedConference.getJid().toString());
                }

                Constant.printMsg("Bookmarked Room:"
                        + bookmarkedConference.getJid());

            }

            for (String string : rm_bm) {

                // Log.d("Room Removes", string);
                // bm_1.removeBookmarkedConference(string);
                // dbadapter.setDeleteContact(string);
            }

        } catch (Exception e) {
            Constant.printMsg("DDDD ee"
                    + e.toString());
        }

        Constant.printMsg("DDDD ee fff"
               );

    }

    public void Re_connecting_groups() {

        try {

            BookmarkManager bm = BookmarkManager.getBookmarkManager(connection);
            BookmarkManager bm_1 = BookmarkManager
                    .getBookmarkManager(connection);
            ArrayList<String> rm_bm = new ArrayList<String>();

            Collection<BookmarkedConference> l = bm.getBookmarkedConferences();
            Constant.printMsg("BookMarked Rooms:" + l.size());
            for (BookmarkedConference bookmarkedConference : l) {

                Constant.printMsg("PPPPPPPPPPPPPPPPPPPPPP"
                        + bookmarkedConference.getJid());

                try {

                    muc = MultiUserChatManager.getInstanceFor(connection)
                            .getMultiUserChat(bookmarkedConference.getJid());

                    muc.addMessageListener(muc_messageListener);
                    muc.addSubjectUpdatedListener(new MUC_SubjectChangeListener(
                            context));



                    MucEnterConfiguration.Builder build =  muc.getEnterConfigurationBuilder(Resourcepart.from(dbadapter.getLogin().get(0).getUserName()
                            + KachingMeApplication.getHost()));

                    build.requestHistorySince(Utils.getBookmarkDate(bookmarkedConference
                            .getNickname().toString()));
                    build.timeoutAfter(6000000L);

                    MucEnterConfiguration musOb =  build.build();


                    muc.join(musOb);

                    // muc.join(bookmarkedConference.getJid());

                } catch (Exception e) {
                    // TODO: handle exception

                    Constant.printMsg("DDDDD2" + e.toString());
                }

                // RoomInfo room_info = null;
                // try {
                //
                // room_info = MUC_MANAGER.getRoomInfo(bookmarkedConference
                // .getJid());
                // } catch (Exception e) {
                // // ACRA.getErrorReporter().handleException(e);
                // }
                // if (room_info != null) {
                // try {
                //
                // SharedPreferences sp_1 = getSharedPreferences(
                // KachingMeApplication.getPereference_label(),
                // Activity.MODE_PRIVATE);
                // long last = sp_1.getLong("last_refresh_time", 0);
                // long m = 0;
                // long now = new Date().getTime();
                // if (last != 0) {
                // m = (now - last) / 1000;
                // }
                // muc = MultiUserChatManager
                // .getInstanceFor(connection)
                // .getMultiUserChat(bookmarkedConference.getJid());
                // // muc = new MultiUserChat(connection,
                // // bookmarkedConference.getJid());
                //
                // muc.addMessageListener(muc_messageListener);
                // muc.addSubjectUpdatedListener(new MUC_SubjectChangeListeners(
                // KaChingMeService.this, this));
                // DiscussionHistory history = new DiscussionHistory();
                // history.setSince(Utils
                // .getBookmarkDate(bookmarkedConference
                // .getNickname()));
                // muc.join(KachingMeApplication.getUserID()
                // + KachingMeApplication.getHost(), null, history,
                // 1000L);
                //
                // } catch (Exception e) {
                // // TODO: handle exception
                // // ACRA.getErrorReporter().handleException(e);
                // // e.printStackTrace();
                // }
                // } else {
                // rm_bm.add(bookmarkedConference.getJid());
                // }
                //
                // Constant.printMsg("Bookmarked Room:"
                // + bookmarkedConference.getJid());

            }

            for (String string : rm_bm) {

                // Log.d("Room Removes", string);
                // bm_1.removeBookmarkedConference(string);
                // dbadapter.setDeleteContact(string);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mConnectionAliveChecking() {

        mAuthendicationChecking = new Thread(new Runnable() {

            @Override
            public void run() {
            }
        });

        mHandler.postDelayed(mAuthendicationChecking, 5000);

    }

    private void mObjectsAliveChecking() {

        try {

            if (TempConnectionService.mDeliveryReciept == null) {

                mDeliveryReciept = DeliveryReceiptManager
                        .getInstanceFor(TempConnectionService.connection);
                // drm.autoAddDeliveryReceiptRequests();
                mDeliveryReciept.getAutoReceiptMode();
                mDeliveryReciept
                        .addReceiptReceivedListener(new RecieptRecievedListener(
                                context));

            }

            if (TempConnectionService.messageEventManager == null) {

                messageEventManager = MessageEventManager
                        .getInstanceFor(TempConnectionService.connection);
                messageEventManager
                        .addMessageEventNotificationListener(new MessageEventNooficationList(
                                context));

            }

            if (TempConnectionService.mChatCreatedListener == null) {

                mChatCreatedListener = new ChatCreatedListener(context,
                        KachingMeApplication.getjid());
                Constant.printMsg("Commecton jid" + KachingMeApplication.getjid());

                ChatTest.mChatCreatedListener = mChatCreatedListener;

            }

            if (TempConnectionService.MUC_MANAGER == null) {

                MUC_MANAGER = MultiUserChatManager.getInstanceFor(connection);
                MUC_MANAGER
                        .addInvitationListener(new MUC_InvitationListener(
                                context, context));

            }

            if (TempConnectionService.muc_messageListener == null) {

                muc_messageListener = new MUC_MessageListener(context,
                        context);

            }

            if (TempConnectionService.muc == null) {

                Re_connecting_groups();

            }

            if (TempConnectionService.drm == null) {

                drm = DeliveryReceiptManager.getInstanceFor(connection);
                // drm.autoAddDeliveryReceiptRequests();
                drm.getAutoReceiptMode();
                drm.addReceiptReceivedListener(new RecieptRecievedListener(
                        context));

            }

            if (TempConnectionService.chatmanager == null) {

                chatmanager = ChatManager.getInstanceFor(connection);
                chatmanager.addChatListener(mChatCreatedListener);

            }

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    /**
     * Registers a smack packet listener for IQ packets, intended to recognize
     * "pongs" with a packet id matching the last "ping" sent to the server.
     * <p/>
     * Also sets up the AlarmManager Timer plus necessary intents.
     */
    private void registerPongListener() {
        // reset ping expectation on new connection
        mPingID = null;

        if (mPongListener != null)
            connection.removeAsyncStanzaListener(mPongListener);

        mPongListener = new StanzaListener() {

            @Override
            public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {
                if (packet == null)
                    return;

                gotServerPong(packet.getStanzaId());
            }

//            @Override
//            public void processPacket(Stanza packet) {
//                if (packet == null)
//                    return;
//
//                gotServerPong(packet.getStanzaId());
//            }

        };

        connection.addAsyncStanzaListener(mPongListener, new StanzaTypeFilter(
                IQ.class));
        mPingAlarmPendIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), 0, mPingAlarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mPongTimeoutAlarmPendIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), 0, mPongTimeoutAlarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis()
                        + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, mPingAlarmPendIntent);
    }

    private void gotServerPong(String pongID) {
        long latency = System.currentTimeMillis() - mPingTimestamp;
        if (pongID != null && pongID.equals(mPingID))
            Constant.printMsg(String.format("Ping: server latency %1.3fs",
                    latency / 1000.));
        else
            Constant.printMsg(String.format(
                    "Ping: server latency %1.3fs (estimated)",
                    latency / 1000.));
        mPingID = null;
        mAlarmManager.cancel(mPongTimeoutAlarmPendIntent);
    }

    public void serverPong() {
        Ping ping = new Ping();
        ping.setType(Ping.Type.get);
        ping.setTo(KachingMeConfig.CHAT_SERVER);
        mPingID = ping.getStanzaId();
        Constant.printMsg("Ping: sending ping " + mPingID);
        try {
            connection.sendStanza(ping);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        registerPongTimeout(PACKET_TIMEOUT + 3000, mPingID);
    }

    /**
     * Register a "pong" timeout on the connection.
     */
    private void registerPongTimeout(long wait_time, String id) {
        mPingID = id;
        mPingTimestamp = System.currentTimeMillis();
        Constant.printMsg(String.format(
                "Ping: registering timeout for %s: %1.3fs", id,
                wait_time / 1000.));
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + wait_time, mPongTimeoutAlarmPendIntent);
    }

    public void stopService() {
        new TempConnectionService().stopHandler();

        if (TempConnectionService.mReconnectionManager != null) {
            TempConnectionService.mReconnectionManager
                    .disableAutomaticReconnection();
        }

        boolean isServiceStopped = context.stopService(new Intent(context
                .getApplicationContext(), TempConnectionService.class));

        Constant.printMsg("GGGGGG  Service stopped TempConn "
                + isServiceStopped);

        if (isServiceStopped) {
            TempConnectionService.mDeliveryReciept = null;
            TempConnectionService.messageEventManager = null;
            TempConnectionService.mChatCreatedListener = null;
            TempConnectionService.MUC_MANAGER = null;
            TempConnectionService.muc_messageListener = null;
            TempConnectionService.muc = null;
            TempConnectionService.privacymanager = null;
            TempConnectionService.drm = null;
            TempConnectionService.chatmanager = null;
            TempConnectionService.roster = null;
            TempConnectionService.mReconnectionManager = null;
            TempConnectionService.connection = null;
        }
    }

    public void stopHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mAuthendicationChecking);
            Constant.printMsg("GGGGGG  Service Handler stopped TempConn");
        }
    }

    @Override
    public void onDestroy() {

        try {

            if (connection != null) {
                if (connection.isAuthenticated()) {
                    unregisterReceiver(mPingAlarmReceiver);
                    unregisterReceiver(mPongTimeoutAlarmReceiver);
                }
            }
        } catch (Exception e) {

        }

        super.onDestroy();
    }

    /**
     * BroadcastReceiver to trigger sending pings to the server
     */
    private class PingAlarmReceiver extends BroadcastReceiver {
        public void onReceive(Context ctx, Intent i) {
            serverPong();
        }
    }

    /**
     * BroadcastReceiver to trigger reconnect on pong timeout.
     */
    private class PongTimeoutAlarmReceiver extends BroadcastReceiver {
        public void onReceive(Context ctx, Intent i) {
            Constant.printMsg("Ping: timeout for " + mPingID);
            // onDisconnected("Ping timeout");
        }
    }
}
