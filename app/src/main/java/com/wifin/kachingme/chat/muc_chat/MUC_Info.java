package com.wifin.kachingme.chat.muc_chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts.Intents;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.async_tasks.ConcurrentAsyncTaskExecutor;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.listeners.MUC_MessageListener;
import com.wifin.kachingme.listeners.MUC_SubjectChangeListener;
import com.wifin.kachingme.pojo.Chat_list_GetSet;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.GroupParticipantGetSet;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.AlertUtils;
import com.wifin.kachingme.util.AvatarManager;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.MultiselectMembers;
import com.wifin.kachingme.util.ProfileRoundImg;
import com.wifin.kachingme.util.Select_MUC_Admin;
import com.wifin.kachingme.util.SendWeb_Group;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.Roster.SubscriptionMode;
import org.jivesoftware.smackx.bookmarks.BookmarkManager;
import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MucEnterConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xroster.RosterExchangeManager;
import org.json.JSONObject;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class MUC_Info extends HeaderActivity implements OnClickListener {

    private static String TAG = MUC_Info.class.getSimpleName();
    ImageView img_avatar;
    TextView txt_subject, txt_member, txt_created;
    TextView btn_edit_sunject;
    ListView list;
    LinearLayout btn_add_members, mTopLayout;
    TextView btn_delete;
    View mEditBtnSeperator;
    MultiUserChat muc;
    boolean isContactNumberExits = false;
    String jid;
    ProgressDialog progressdialog;
    Handler h;
    Thread refresh;
    Boolean create = true;
    VCard vc = new VCard();
    DatabaseHelper dbAdapter;
    UserListAdapter adapter;
    boolean loopRunning = true;
    ArrayList<ContactsGetSet> contact_list;
    ArrayList<ContactsGetSet> contact_list_duplicate;
    String mem_list, admin;
    String[] mem_ar;
    Boolean Is_Admin = false;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    // Merlin merlin;
    Cursor cursor;
    boolean is_chat_exist = false;
    Boolean is_connected = true;
    Resources res;
    /* Advanced group */
    String group_type = null;
    String group_question = null;
    String group_options = null;
    String group_admin = null;
    int int_group_type;
    Chat_list_GetSet chat_list;
    GroupParticipantGetSet group_partcipant_getset;
    int REQUEST_CODE_ADD_CONTACT = 12;
    ArrayList<String> resultListContacts = null;
    ProgressDialog myPd_ring = null;
    String selected_m[];
    boolean active = true;
    int height = 0;
    int width = 0;
    ImageView back_arrow_btn;
    public static ArrayList<ContactsGetSet> selected_users;
    // NEw
    private String res_jid = null;
    private List<String> jidList;
    private String myUserJid = null;
    BroadcastReceiver lastseen_event = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            // Constant.printMsg("Broadcas Recievwd!!!!");
            if (intent.getAction().equals("update_list")) {
                Log.d("MUC_info_b1", "List Size:");

               /* if (!myPd_ring.isShowing())
                    myPd_ring.show();*/

                // Initialize variable myUserJid
                setMyUserJid();
                Is_Admin = dbAdapter.getGroupAdmin_Info(jid, myUserJid);

                if (!Is_Admin) {
                    mNextBtn.setVisibility(View.GONE);
                } else {
                    mNextBtn.setVisibility(View.VISIBLE);
                }

                new MyAsync().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

            } else if (intent.getAction().equals("network_status")) {
                if (Connectivity.isOnline(MUC_Info.this) && Connectivity.isTempConnection()) {
                    // connection = mBoundService.getConnection();
                    Log.d("MUC_info_b2", "List Size:");
                    new MyAsync().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                }
            } else if (intent.getAction().equals(
                    Constant.BROADCAST_UPDATE_GROUP_ICON)) {
                try {
                    Log.d(TAG, "Group icon update broadcast recieved");
                    Bitmap bmp = BitmapFactory
                            .decodeFile(KachingMeApplication.PROFILE_PIC_DIR
                                    + jid.toString().split("@")[0] + ".png");
                    ProfileRoundImg groupRoundImage = new ProfileRoundImg(bmp);
                    img_avatar.invalidate();
                    img_avatar.setImageDrawable(groupRoundImage);
                    Constant.mGroupInfoProfile = bmp;
                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                    e.printStackTrace();
                    // TODO: handle exception
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.muc_info);
        getSupportActionBar().hide();
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.muc_info, vg);

        img_avatar = (ImageView) findViewById(R.id.img_avatar);
        txt_subject = (TextView) findViewById(R.id.txt_subject);
        txt_member = (TextView) findViewById(R.id.txt_members);
        btn_edit_sunject = (TextView) findViewById(R.id.btn_edit_subjects);
        back_arrow_btn = (ImageView) findViewById(R.id.back_btn);
        txt_created = (TextView) findViewById(R.id.txt_created);
        mTopLayout = (LinearLayout) findViewById(R.id.top_layout);
        mEditBtnSeperator = (View) findViewById(R.id.edit_btn_seperator);
        btn_delete = (TextView) findViewById(R.id.btn_delete);
        list = (ListView) findViewById(R.id.list_experience);
        dbAdapter = KachingMeApplication.getDatabaseAdapter();

        Constant.FROM_CHAT_SCREEN = "info";

        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage(getResources().getString(R.string.loading));

        Constant.typeFace(this, txt_subject);
        Constant.typeFace(this, txt_member);
        Constant.typeFace(this, btn_edit_sunject);
        Constant.typeFace(this, txt_created);
        Constant.typeFace(this, btn_delete);

        btn_delete.setOnClickListener(this);
        btn_edit_sunject.setOnClickListener(this);


        mHeading.setText("Group Info");
        mHeaderImg.setVisibility(View.GONE);
        mNextBtn.setImageResource(R.drawable.add_member);
        mFooterLayout.setVisibility(View.GONE);
        mFooterView.setVisibility(View.GONE);
        screenArrangement();
        myPd_ring = new ProgressDialog(this);
        myPd_ring.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        myPd_ring.setMessage("Loading...");

        View view = new View(this);
        list.addFooterView(view);
        res = getResources();


        back_arrow_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        mNextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isListPresent = false;

                if (dbAdapter.get_Select_Group_Contacts(jid) != null) {
                    if (dbAdapter.get_Select_Group_Contacts(jid).size() > 0)
                        isListPresent = true;

                }

                if (Connectivity.isOnline(MUC_Info.this) && Connectivity.isTempConnection()) {

                    if (isListPresent) {
                        Intent intent = new Intent(MUC_Info.this, MultiselectMembers.class);
                        intent.putExtra("jid", jid);
                        startActivityForResult(intent, 0);
                    } else {
                        new AlertUtils().Toast_call(MUC_Info.this, "No KachingMe members to add in this group");
                    }
                } else {
                    new AlertUtils().Toast_call(MUC_Info.this, getResources()
                            .getString(R.string.no_internet_connection));
                }
            }
        });
        // merlin = new Merlin.Builder().withConnectableCallbacks().build(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            active = false;

            jid = bundle.getString("jid");
            Constant.printMsg("jidjidjid   " + jid);

            if (TempConnectionService.connection != null) {

                if (TempConnectionService.connection.isConnected() && TempConnectionService.connection.isAuthenticated()) {
                    MultiUserChatManager multiUserChatManager = MultiUserChatManager
                            .getInstanceFor(TempConnectionService.connection);

                    try {
                        muc = multiUserChatManager
                                .getMultiUserChat(JidCreate.entityBareFrom(jid));
                    } catch (XmppStringprepException e) {
                        e.printStackTrace();
                    }
                }
            }

            // if (Constant.jid == null) {
            // Constant.jid = jid;
            // } else {
            // jid = Constant.jid;
            // }

            txt_subject.setText(dbAdapter.getContact(jid).getDisplay_name());
//            getSupportActionBar().setTitle(
//                    getResources().getString(R.string.group_info));

			/*
             * if(bundle.getByteArray("avatar")!=null) { byte
			 * avatar[]=bundle.getByteArray("avatar"); Bitmap bitmap =
			 * BitmapFactory.decodeByteArray(avatar, 0,avatar.length);
			 * img_avatar.setImageBitmap(new
			 * AvatarManager().roundCornerImage(bitmap, 180));
			 *
			 * }
			 */


            try {
                Bitmap bmp = BitmapFactory
                        .decodeFile(KachingMeApplication.PROFILE_PIC_DIR
                                + jid.toString().split("@")[0] + ".png");
                Drawable drawable = new BitmapDrawable(bmp);
                ProfileRoundImg groupRoundImg = new ProfileRoundImg(bmp);
                img_avatar.setImageDrawable(groupRoundImg);
                Constant.mGroupInfoProfile = bmp;
                // getSupportActionBar().setIcon(drawable);
                //img_avatar.setImageDrawable(drawable);
            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                // TODO: handle exception
            }

        }

        // Initialize variable myUserJid
        setMyUserJid();
        Is_Admin = dbAdapter.getGroupAdmin_Info(jid, myUserJid);

        if (!Is_Admin) {
            mNextBtn.setVisibility(View.GONE);
        }

        if (dbAdapter.isGroupMember(jid, myUserJid))
            int_group_type = 1;
        else
            int_group_type = 0;

        sp = getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        editor = sp.edit();
        Log.d("MUC_info",
                "jid::" + jid + " admin::" + sp.getString(jid + "_admin", ""));
        // admin = sp.getString(jid + "_admin", "");

        String uid = KachingMeApplication.getUserID() + KachingMeApplication.getHost();
        Constant.printMsg("UID::" + uid + " admin::" + admin);
//        if (admin.equals(uid)) {
//            Log.d("Condition", "UID::" + uid + " admin::" + admin);
//            Log.d("MUC_info", "IS_admin true");
//          //  Is_Admin = true;
//        }

        group_type = sp.getString(jid + "_group_type", "none");
        group_question = sp.getString(jid + "_group_question", "none");
        //  group_admin = sp.getString(jid + "_admin", "none");
        // txt_group_topic.setText(group_question);
        //  int_group_type = Integer.parseInt(group_type);
        registerForContextMenu(list);

        cursor = dbAdapter.getChat_list();
        chat_list = dbAdapter.getChat_List(jid);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a, dd/MM/yyyy");
        txt_created.setText("Created "
                + sdf.format(new Date(chat_list.getTimestamp() * 1000L)));

        img_avatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MUC_Info.this,
                        GroupProfilePic.class);
                intent.putExtra("jid", jid);
                startActivity(intent);

            }
        });

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Constant.FROM_CHAT_SCREEN = "";

                final ContactsGetSet contact = (ContactsGetSet) arg0
                        .getAdapter().getItem(arg2);

                is_chat_exist = false;
                Constant.printMsg("jiddddddd  " + contact.getJid());
                if (!contact.getJid().equals(KachingMeApplication.getjid())) {

                    if (!dbAdapter.isjidExist(contact.getJid())) {
                        Log.d("Message",
                                "Contact does not exist::" + contact.getJid());
                        // Add_New_Contact(cursor.getString(8));
                        new AsyncAdd_Contact().execute(contact.getJid());
                    }

                    if (sp.contains(contact.getJid() + "_lock")) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(
                                MUC_Info.this);

                        alert.setTitle(res.getString(R.string.unlock));
                        alert.setMessage(res.getString(R.string.unlock) + " "
                                + contact.getDisplay_name());
                        final EditText input = new EditText(MUC_Info.this);
                        input.setInputType(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        input.setTransformationMethod(PasswordTransformationMethod
                                .getInstance());
                        alert.setView(input);

                        alert.setPositiveButton(res.getString(R.string.unlock),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        String value = input.getText()
                                                .toString();
                                        if (value.equals(sp
                                                .getString("pin", ""))) {
                                            if (cursor.moveToFirst()) {
                                                do {
                                                    if (contact
                                                            .getJid()
                                                            .equals(cursor
                                                                    .getString(8))) {
                                                        Log.e("Muc_Chat Info",
                                                                "Chat exist::"
                                                                        + cursor.getString(8));

                                                        Intent intent = new Intent(
                                                                MUC_Info.this,
                                                                ChatTest.class);
                                                        intent.putExtra(
                                                                "jid",
                                                                cursor.getString(8));
                                                        intent.putExtra(
                                                                "name",
                                                                cursor.getString(1));
                                                        intent.putExtra(
                                                                "is_owner",
                                                                ""
                                                                        + cursor.getInt(12));
                                                        intent.putExtra(
                                                                "avatar",
                                                                cursor.getBlob(2));
                                                        if (cursor.getInt(11) == 1) {
                                                            is_chat_exist = true;
                                                            intent.putExtra(
                                                                    "IS_SECRET_CHAT",
                                                                    false);

                                                            startActivity(intent);
                                                        }

														/*
                                                         * else {
														 * intent.putExtra
														 * ("IS_SECRET_CHAT"
														 * ,true);
														 * startActivity(
														 * intent); }
														 */

                                                    }

                                                } while (cursor.moveToNext());
                                            }

                                            if (!is_chat_exist) {

                                                Intent intent = new Intent(
                                                        MUC_Info.this,
                                                        ChatTest.class);
                                                intent.putExtra("jid",
                                                        contact.getJid());
                                                intent.putExtra("name", contact
                                                        .getDisplay_name());
                                                intent.putExtra("is_owner",
                                                        "" + 1);
                                                intent.putExtra("avatar",
                                                        contact.getPhoto_ts());
                                                intent.putExtra(
                                                        "IS_SECRET_CHAT", false);
                                                startActivity(intent);
                                            }
                                        } else {
                                            new AlertManager().showAlertDialog(
                                                    MUC_Info.this,
                                                    res.getString(R.string.you_are_entered_incorrect_pin),
                                                    true);

                                        }
                                    }
                                });

                        alert.setNegativeButton(res.getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        // Canceled.

                                    }
                                });

                        alert.show();

                    } else {
                        if (cursor.moveToFirst()) {
                            do {
                                if (contact.getJid()
                                        .equals(cursor.getString(8))) {
                                    Log.e("Muc_Chat Info", "Chat exist::"
                                            + cursor.getString(8));

                                    Intent intent = new Intent(MUC_Info.this,
                                            ChatTest.class);
                                    intent.putExtra("jid", cursor.getString(8));
                                    intent.putExtra("name", cursor.getString(1));
                                    intent.putExtra("is_owner",
                                            "" + cursor.getInt(12));
                                    intent.putExtra("avatar", cursor.getBlob(2));
                                    if (cursor.getInt(11) == 1) {
                                        is_chat_exist = true;
                                        intent.putExtra("IS_SECRET_CHAT", false);
                                        Log.d("Message",
                                                "Contact getIsInContactList::"
                                                        + contact
                                                        .getIsInContactList());

                                        startActivity(intent);
                                    }/*
                                     * else {
									 * intent.putExtra("IS_SECRET_CHAT",true);
									 * startActivity(intent); }
									 */

                                }

                            } while (cursor.moveToNext());
                        }

                        if (!is_chat_exist) {

                            Intent intent = new Intent(MUC_Info.this,
                                    ChatTest.class);
                            intent.putExtra("jid", contact.getJid());
                            intent.putExtra("name", contact.getDisplay_name());
                            intent.putExtra("is_owner", "" + 1);
                            intent.putExtra("avatar", contact.getPhoto_ts());
                            intent.putExtra("IS_SECRET_CHAT", false);
                            startActivity(intent);
                        }
                    }
                }
                Log.e("Muc_Info", "JID::" + contact.getJid());
            }
        });


        new MyAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                "nothing");


        // Initializing muc variable...
        if (TempConnectionService.connection != null) {
            if (TempConnectionService.connection.isAuthenticated())
                initializeMUC();
        }

    }


    // private ServiceConnection mConnection = new ServiceConnection() {
    // @Override
    // public void onServiceConnected(ComponentName className, IBinder service)
    // {
    //
    // mBoundService = ((KaChingMeService.LocalBinder) service)
    // .getService();
    // connection = mBoundService.getConnection();
    //
    // Log.d(TAG, "Admin ID::" + KachingMeApplication.getUserID()
    // + KachingMeApplication.getHost());
    // if (dbAdapter.isGroupMember(jid, KachingMeApplication.getjid())) {
    // Thread th = new Thread() {
    // @Override
    // public void run() {
    // muc = mBoundService.getMUC_MANAGER().getMultiUserChat(
    // jid);
    // // muc=mBoundService.getMUC();
    // try {
    // DiscussionHistory history = new DiscussionHistory();
    // history.setSince(Utils.getBookmarkDate(sp
    // .getString(Constant.LAST_REFRESH_TIME
    // + "_" + jid,
    // Utils.getBookmarkTime())));
    // muc.join(KachingMeApplication.getUserID()
    // + KachingMeApplication.getHost(), null,
    // history, 30000L);
    // muc.addMessageListener(new MUC_MessageListeners(
    // MUC_Info.this, mBoundService));
    // muc.addSubjectUpdatedListener(new MUC_SubjectChangeListeners(
    // MUC_Info.this, mBoundService));
    //
    // } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
    // e.printStackTrace();
    // // TODO: handle exception
    // }
    // }
    // };
    // th.start();
    // }
    //
    // }
    //
    // @Override
    // public void onServiceDisconnected(ComponentName className) {
    //
    // mBoundService = null;
    // }
    // };
    //
    // void doBindService() {
    //
    // bindService(new Intent(MUC_Info.this, KaChingMeService.class),
    // mConnection, Context.BIND_AUTO_CREATE);
    // isBound = true;
    // }
    //
    // void doUnbindService() {
    // if (isBound) {
    //
    // unbindService(mConnection);
    // isBound = false;
    // }
    // }

    private void screenArrangement() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        Constant.screenWidth = width;
        Constant.screenHeight = height;

        LinearLayout.LayoutParams mTopLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mTopLayoutParams.width = (int) width;
        mTopLayoutParams.height = (int) height * 15 / 100;
        mTopLayout.setLayoutParams(mTopLayoutParams);

        LinearLayout.LayoutParams mSeperatorParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mSeperatorParams.width = (int) (width * 0.5 / 100);
        mSeperatorParams.height = (int) height * 8 / 100;
        mSeperatorParams.gravity = Gravity.CENTER;
        mEditBtnSeperator.setLayoutParams(mSeperatorParams);

        LinearLayout.LayoutParams mGroupImgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mGroupImgParams.width = (int) width * 23 / 100;
        mGroupImgParams.height = (int) width * 23 / 100;
        mGroupImgParams.leftMargin = width * 2 / 100;
//        mGroupImgParams.topMargin = width * 2 / 100;
        mGroupImgParams.gravity = Gravity.CENTER;
        img_avatar.setLayoutParams(mGroupImgParams);

        LinearLayout.LayoutParams mGroupEditParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mGroupEditParams.width = (int) width * 8 / 100;
        mGroupEditParams.height = (int) width * 8 / 100;
        mGroupEditParams.gravity = Gravity.CENTER;
        mGroupEditParams.leftMargin = width * 3 / 100;
        btn_edit_sunject.setLayoutParams(mGroupEditParams);


        LinearLayout.LayoutParams mGroupMemCountParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mGroupMemCountParams.width = (int) width;
        mGroupMemCountParams.height = (int) height * 8 / 100;
        mGroupMemCountParams.leftMargin = width * 3 / 100;
        mGroupMemCountParams.gravity = Gravity.CENTER;
//        mGroupEditParams.topMargin = width * 2 / 100;
        txt_member.setLayoutParams(mGroupMemCountParams);
        txt_member.setGravity(Gravity.CENTER | Gravity.LEFT);

        LinearLayout.LayoutParams mGroupLabelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mGroupLabelParams.width = (int) width * 56 / 100;
        mGroupLabelParams.height = (int) height * 4 / 100;
        mGroupLabelParams.leftMargin = width * 2 / 100;
        mGroupLabelParams.topMargin = height * 1 / 100;
        mGroupLabelParams.gravity = Gravity.CENTER;
        txt_subject.setLayoutParams(mGroupLabelParams);
        txt_subject.setGravity(Gravity.LEFT | Gravity.CENTER);

        LinearLayout.LayoutParams mGroupLabelParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mGroupLabelParams1.width = (int) width * 56 / 100;
        mGroupLabelParams1.height = (int) height * 4 / 100;
        mGroupLabelParams1.leftMargin = width * 2 / 100;
        mGroupLabelParams1.gravity = Gravity.CENTER;
        txt_created.setLayoutParams(mGroupLabelParams1);
        txt_created.setGravity(Gravity.LEFT | Gravity.CENTER);


        LinearLayout.LayoutParams mGroupDeleteBtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mGroupDeleteBtnParams.width = (int) width * 50 / 100;
        mGroupDeleteBtnParams.height = (int) height * 7 / 100;
        mGroupDeleteBtnParams.bottomMargin = height * 5 / 100;
        mGroupDeleteBtnParams.topMargin = width * 5 / 100;
        mGroupDeleteBtnParams.gravity = Gravity.CENTER;
        btn_delete.setLayoutParams(mGroupDeleteBtnParams);
        btn_delete.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams mheaderLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//        mheaderLayoutParams.width = (int) width * 50/100;
        mheaderLayoutParams.height = (int) height * 90 / 100;
//        mheaderLayoutParams.bottomMargin = height * 5 /100;
//        mheaderLayoutParams.topMargin = width *5 / 100;
        mheaderLayoutParams.gravity = Gravity.CENTER;
//        mContentLayout.setLayoutParams(mheaderLayoutParams);

        if (width >= 600) {

            txt_created.setTextSize(16);
            txt_subject.setTextSize(17);
            txt_member.setTextSize(17);
            btn_delete.setTextSize(17);


        } else if (width < 600 && width >= 480) {
            txt_created.setTextSize(15);
            txt_subject.setTextSize(16);
            txt_member.setTextSize(16);

            btn_delete.setTextSize(16);


        } else if (width < 480 && width >= 320) {
            txt_created.setTextSize(13);
            txt_subject.setTextSize(14);
            txt_member.setTextSize(14);
            btn_delete.setTextSize(14);

        } else if (width < 320) {
            txt_created.setTextSize(11);
            txt_subject.setTextSize(12);
            txt_member.setTextSize(12);

            btn_delete.setTextSize(12);

        }

    }

    @Override
    protected void onResume() {
        // merlin.bind();
        // doBindService();
        IntentFilter filter = new IntentFilter();
        filter.addAction("update_list");
        filter.addAction("network_status");
        filter.addAction(Constant.BROADCAST_UPDATE_GROUP_ICON);
        registerReceiver(lastseen_event, filter);
        // doBindService();
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        // merlin.unbind();
    }

    @Override
    protected void onDestroy() {

        // doUnbindService();
        unregisterReceiver(lastseen_event);

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:

                long admin_cnt = dbAdapter.getGroupMembersAdmin_count(jid);
                Constant.printMsg("Admin count " + admin_cnt);
                String[] mm = sp.getString(jid, "").toString().split(",");
                int memListSize = 0;


                if (mm != null)
                    memListSize = mm.length;


                if (Is_Admin == true && admin_cnt == 1 && memListSize > 1) {

                    String mm_list = "";
                    final String list;
                    int i = 0;
                    for (String string : mm) {
                        if (i == 0) {
                            mm_list = "'" + string + "'";
                        } else {
                            mm_list = mm_list + ",'" + string + "'";
                        }

                        i++;
                    }

                    list = mm_list;
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(
                            getResources().getString(R.string.delete_n_exit_group))
                            .setMessage(
                                    getResources().getString(
                                            R.string.you_must_assign_group_admin))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(
                                    getResources().getString(R.string.Ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // Yes button clicked, do something
                                            // Log.d("Muc_info","Is internet available::"+Connectivity.isOnline(MUC_Info.this)  &&  Connectivity.isTempConnection());
                                            if (KachingMeApplication
                                                    .getIsNetAvailable()) {
                                                Intent intente = new Intent(
                                                        MUC_Info.this,
                                                        Select_MUC_Admin.class);
                                                intente.putExtra("jids", jid);
                                                startActivityForResult(intente, 2);

                                            } else {
                                                new AlertUtils()
                                                        .Toast_call(
                                                                MUC_Info.this,
                                                                getResources()
                                                                        .getString(
                                                                                R.string.no_internet_connection));
                                            }

                                        }
                                    })

                                    // Do nothing on no
                            .show();

                } else {
                    if (Connectivity.isOnline(MUC_Info.this) && Connectivity.isTempConnection()) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle(
                                getResources().getString(
                                        R.string.delete_n_exit_group))
                                .setMessage(
                                        getResources().getString(
                                                R.string.are_you_sure_delete_group))
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(
                                        getResources().getString(R.string.yes),
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                // TODO Auto-generated method stub
                                                Remove_Meber(KachingMeApplication
                                                        .getUserID()
                                                        + KachingMeApplication
                                                        .getHost());
                                                Delete_Local();
                                                Intent intent = new Intent(
                                                        MUC_Info.this,
                                                        SliderTesting.class);
                                                intent.putExtra("is_group_tab",
                                                        "true");
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                .setNegativeButton(
                                        getResources().getString(R.string.No),
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                // TODO Auto-generated method stub

                                            }
                                        }).show();

                    } else {
                        new AlertUtils().Toast_call(MUC_Info.this, getResources()
                                .getString(R.string.no_internet_connection));
                    }
                }

                break;
            case R.id.btn_edit_subjects:
                if (Connectivity.isOnline(MUC_Info.this) && Connectivity.isTempConnection()) {
                    setStatus();
                } else {
                    new AlertUtils().Toast_call(MUC_Info.this, getResources()
                            .getString(R.string.no_internet_connection));
                }
                break;
            default:
                break;
        }

    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        Adapter adapter = list.getAdapter();
        Object item = adapter.getItem(info.position);
        ContactsGetSet contact = contact_list_duplicate.get(info.position);
        String[] menuItems = getResources().getStringArray(
                R.array.ar_menu_userchat);
        String s = contact.getDisplay_name();
        boolean atleastOneAlpha = s.matches(".*[a-zA-Z]+.*");
        Constant.printMsg("numberrrrr  " + s + "    " + atleastOneAlpha + "  " + contact.getIs_admin());
        menu.setHeaderTitle("Choose");
        if (Is_Admin) {
            if (contact.getDisplay_name() != null
                    || contact.getDisplay_name().equals("")) {
                menu.add(
                        0,
                        info.position,
                        1,
                        String.format(
                                getResources().getString(
                                        R.string.remove_from_group),
                                contact.getDisplay_name()));
                if (contact.getIs_admin() != 1) {
                    menu.add(
                            0,
                            info.position,
                            2,
                            getResources().getString(
                                    R.string.add_admin));
                }
            } else {
                menu.add(0, info.position, 1, String.format(getResources()
                        .getString(R.string.remove_from_group), contact
                        .getJid().split("@")[0]));
                if (contact.getIs_admin() != 1) {
                    menu.add(
                            0,
                            info.position,
                            2,
                            getResources().getString(
                                    R.string.add_admin));
                }
            }
        }
        if (atleastOneAlpha) {

        } else {
            menu.add(1, info.position, 2, String.format(getResources()
                    .getString(R.string.add_from_group), contact
                    .getDisplay_name()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Constant.printMsg("menuItemIndex  " + Is_Admin + "   ");

        if (Is_Admin) {

            // getMenuInflater().inflate(R.menu.group_info, menu);

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.muc_info_add:

                if (Connectivity.isOnline(MUC_Info.this) && Connectivity.isTempConnection()) {
                    intent = new Intent(this, MultiselectMembers.class);
                    intent.putExtra("jid", jid);
                    startActivityForResult(intent, 0);
                } else {
                    new AlertUtils().Toast_call(MUC_Info.this, getResources()
                            .getString(R.string.no_internet_connection));
                }
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // Initializing MUC variable..
    public void initializeMUC() {
        Thread mucThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    muc = TempConnectionService.MUC_MANAGER.getMultiUserChat(JidCreate.entityBareFrom(jid));
                } catch (XmppStringprepException e) {
                    e.printStackTrace();
                }
                try {
                    MucEnterConfiguration.Builder build =  muc.getEnterConfigurationBuilder(Resourcepart.from("group"));

                    build.requestHistorySince(new Date());
                    build.requestMaxStanzasHistory(0);
                    build.requestMaxCharsHistory(0);
                    build.timeoutAfter(6000000L);

                    MucEnterConfiguration musOb =  build.build();


//                if (!muc.isJoined()) {
                    muc.join(musOb);
                    muc.addMessageListener(new MUC_MessageListener(MUC_Info.this, MUC_Info.this));
                    muc.addSubjectUpdatedListener(new MUC_SubjectChangeListener(
                            MUC_Info.this));

                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                    e.printStackTrace();
                    // TODO: handle exception
                }
            }
        });

        mucThread.start();

    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        int menuItemIndex = item.getItemId();
        Constant.printMsg("checkkkk   " + item.getItemId() + "   "
                + item.toString() + "  " + item.getGroupId());
        if (item.getGroupId() == 0) {

            if (item.getOrder() == 1 && Connectivity.isOnline(MUC_Info.this) && Connectivity.isTempConnection()) {
                Constant.printMsg("checkkkkassasa   " + item.getItemId() + "   "
                        + item.toString() + "  " + item.getTitle());
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(
                        getResources().getString(R.string.remove_member))
                        .setMessage(
                                String.format(
                                        getResources()
                                                .getString(
                                                        R.string.are_you_sure_you_want_to_remove),
                                        item.getTitle().toString()
                                                .toLowerCase()))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(
                                getResources().getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // Yes button clicked, do something

                                       // Remove_Meber(item.getItemId());

                                        Integer[] val = {item.getItemId()};
                                        ConcurrentAsyncTaskExecutor.executeConcurrently(new Remove_Async(), val);
                                        dialog.cancel();



                                    }
                                })

                        .setNegativeButton(
                                getResources().getString(R.string.No),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        dialog.cancel();

                                    }
                                }) ;// Do nothing on no
                builder.show();

                //

            } else if (item.getOrder() == 2 && Connectivity.isOnline(MUC_Info.this) && Connectivity.isTempConnection()) { // Make as group admin

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(
                        getResources().getString(R.string.make_admin))
                        .setMessage(
                                String.format(
                                        getResources()
                                                .getString(
                                                        R.string.are_you_sure_you_want_to_make_admin),
                                        contact_list_duplicate.get(item.getItemId()).getDisplay_name()
                                                .toLowerCase()))
                        .setPositiveButton(
                                getResources().getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // Yes button clicked, do something

                                        addNew_Admin(contact_list_duplicate.get(item.getItemId()).getJid());


                                    }
                                })

                        .setNegativeButton(
                                getResources().getString(R.string.No),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub

                                    }
                                }) // Do nothing on no
                        .show();

                //

            } else {
                new AlertUtils().Toast_call(MUC_Info.this, getResources()
                        .getString(R.string.no_internet_connection));
            }
        }

        if (item.getGroupId() == 1) {
            Constant.printMsg("itemmmm " + item.getItemId());
            Intent intent = new Intent(Intent.ACTION_INSERT,
                    ContactsContract.Contacts.CONTENT_URI);
            intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
            intent.putExtra(Intents.Insert.PHONE,
                    "+"
                            + contact_list_duplicate.get(item.getItemId()).getJid()
                            .split("@")[0]);
            intent.putExtra(Intents.Insert.PHONE_TYPE, Phone.TYPE_MOBILE);
            intent.putExtra("finishActivityOnSaveCompleted", true);

            // if (result.getNifty_email() != null)
            // intent.putExtra(Intents.Insert.EMAIL,
            // result.getNifty_email());
            // if (result.getNifty_name() != null)
            // intent.putExtra(Intents.Insert.NAME,
            // result.getNifty_name());

            startActivityForResult(intent, REQUEST_CODE_ADD_CONTACT);
        }
        return true;

    }

//    public void Remove_Meber(final int positon) {
//
//
//       try{
//                    Log.d(TAG, "LLLLLLLL Removed Member::" + contact_list_duplicate.get(positon).getJid());
//                    muc.revokeOwnership(contact_list_duplicate.get(positon).getJid());
//
//                    String mem_list = null;
//                    Collection<Affiliate> owner = muc.getOwners();
//
//                    int i = 0;
//                    for (Affiliate affiliate : owner) {
//                        if (i == 0) {
//                            mem_list = affiliate.getJid();
//                        } else {
//                            mem_list = mem_list + "," + affiliate.getJid();
//                        }
//                        i++;
//                        Log.d("MUC_info", "LLLLLLLL Owner Left::" + affiliate.getJid());
//                    }
//                    String packate_id = "" + new Date().getTime();
//
//                    dbAdapter.deleteGroupMembers(jid, contact_list_duplicate.get(positon)
//                            .getJid());
//
//                    Message msg = new Message(jid, Type.groupchat);
//                    msg.setStanzaId(Constant.MEMBERREMOVEMESSAGE + packate_id);
//
//                    // msg.setSubject(muc.getSubject());
//                    msg.setBody(mem_list);
//                    JivePropertiesManager.addProperty(msg, "Removed_member",
//                            contact_list_duplicate.get(positon).getJid());
//                    JivePropertiesManager.addProperty(msg, "ID", 2);
//                    JivePropertiesManager.addProperty(msg, "media_wa_type", "0");
//                    muc.sendMessage(msg);
//                    Constant.printMsg("LLLLLL mesg sent");
//                    editor.putString(jid, mem_list);
//                    editor.commit();
//
//                    muc.revokeMembership(contact_list_duplicate.get(positon).getJid());
//
////            SendWeb_Group.Update_Memberlist_on_web_async(MUC_Info.this, jid,
////                    mem_list);
//
//                    Intent chat_broadcast = new Intent("update_list");
//                    sendBroadcast(chat_broadcast);
//
//                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//                    Constant.printMsg("LLLLLL remove mem exp :" + e.toString());
//                }
//
//
//
//
//        //	 new MyAsync().execute();
//    }

    public void Add_Meber(String mem_jid) {

        jidList = new ArrayList<String>();

        Log.d("MUC_ADD_Anormal", "List Size:" + Arrays.toString(selected_m));

        for (int j = 0; j < selected_m.length; j++) {
            mem_jid = selected_m[j];

            try {
                Log.d(TAG, "Invited Member::" + mem_jid +" "+muc.getSubject());

                // muc.invite(mem_jid,"");

                muc.invite(JidCreate.entityBareFrom(mem_jid),"invite");

                muc.grantOwnership(JidCreate.from(mem_jid));

                String mem_list = null;
                Collection<Affiliate> owner = muc.getOwners();

                int i = 0;
                for (Affiliate affiliate : owner) {
                    if (i == 0) {
                        mem_list = affiliate.getJid().toString();
                    } else {
                        mem_list = mem_list + "," + affiliate.getJid();
                    }
                    i++;
                    Log.d("MUC_info", "Owner Add::" + affiliate.getJid());
                }

                group_partcipant_getset = new GroupParticipantGetSet();
                group_partcipant_getset.setAdmin(0);
                group_partcipant_getset.setGjid(jid);
                group_partcipant_getset.setJid(mem_jid);
                dbAdapter.addGroupMembers(group_partcipant_getset);

                Message msg1 = new Message(JidCreate.from(jid), Type.groupchat);
                Log.e("MUC_info", "Room subject::" + muc.getSubject());

                msg1.setBody(mem_list);
                JivePropertiesManager
                        .addProperty(msg1, "Added_member", mem_jid);
                JivePropertiesManager.addProperty(msg1, "ID", 1);
                msg1.setStanzaId(Constant.MEMBERADDMESSAGE
                        + new Date().getTime());
                JivePropertiesManager.addProperty(msg1, "media_wa_type", "0");
                muc.sendMessage(msg1);
                editor.putString(jid, mem_list);
                editor.commit();

//                SendWeb_Group.Update_Memberlist_on_web_async(MUC_Info.this,
//                        jid, mem_list);

                jidList.add(jid);

            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                // TODO: handle exception
            }

        }

        //  UpdateContactList();

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new MyNewAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    "string");

        } else {
            new MyNewAsync().execute();
        }*/

    }

//    private void UpdateContactList() {
//        Boolean count_admin = false;
//        String my_jid = KachingMeApplication.getUserID()
//                + KachingMeApplication.getHost();
//
//        count_admin = false;
//        contact_list.clear();
//
//        if (jidList != null) {
//            for (String jidObj : jidList) {
//                jid = jidObj;
//
//
//                mem_list = sp.getString(jid, "");
//
//                admin = (dbAdapter.getGroupAdmin(jid)).getJid();
//
//                ArrayList<GroupParticipantGetSet> list = new ArrayList<GroupParticipantGetSet>();
//                Constant.printMsg("group jid ::::::::   " + jid);
//
//                list = dbAdapter.getGroupMembers(jid);
//
//                for (GroupParticipantGetSet groupParticipantGetSet : list) {
//                    ContactsGetSet contact = dbAdapter
//                            .getContact_grp(groupParticipantGetSet.getJid());
//
//                    if (contact == null) {
//                        contact = new ContactsGetSet();
//                        if (groupParticipantGetSet.getJid().equals(my_jid)) {
//                            contact.setDisplay_name("You");
//                        } else {
//                            contact.setDisplay_name(groupParticipantGetSet
//                                    .getJid().split("@")[0]);
//                        }
//
//                        contact.setJid(groupParticipantGetSet.getJid());
//
//                    }
//
//                    if (groupParticipantGetSet.getJid().equals(my_jid)) {
//                        contact.setDisplay_name("You");
//                        contact.setPhoto_ts(KachingMeApplication.getAvatar());
//                        contact.setJid(my_jid);
//                    }
//
//
//                    if (groupParticipantGetSet.getAdmin() == 1) {
//                        contact.setIs_admin(1);
//                    } else {
//                        contact.setIs_admin(0);
//                    }
//                    Log.d(TAG, "Member JID::" + contact.getJid());
//                    Log.d(TAG,
//                            "Member 2 JID::"
//                                    + groupParticipantGetSet.getJid());
//                    try {
//                        if (contact.getDisplay_name().length() > 0) {
//
//                            Constant.printMsg("MUC Info Profile names Disp Before: " + contact.getDisplay_name());
//
//                            if (contact.getDisplay_name().matches("[0-9]+")) {
//
//                                Constant.printMsg("MUC Info Profile names Before: " + contact
//                                .getJid());
//
//                                if (Connectivity.isOnline(MUC_Info.this) && Connectivity.isTempConnection())
//                                    setProfileName(contact);
//                            }
//
//                            contact_list.add(contact);
//                        }
//                    } catch (Exception e) {
//
//                    }
//
//                }
//
//
//            }// ending List for Loop
//
//            Constant.printMsg("Update Infor b" + contact_list.size());
//            contact_list_duplicate = contact_list;
//            Constant.printMsg("Update Infor a" + contact_list_duplicate.size());
//
//
//        }
//
//
//    }


    public void setProfileName(ContactsGetSet contact) {
        try {
            // connection = NiftyService.connection;
            if (TempConnectionService.connection != null) {
                vc = VCardManager.getInstanceFor(
                        TempConnectionService.connection).loadVCard(
                        JidCreate.entityBareFrom(contact.getJid()));

                Constant.printMsg("MUC Info Profile names Caaaaa::::::: "
                        + vc.getFirstName());

                if (vc.getFirstName() != null) {
                    contact.setProfile_name(vc.getFirstName());
                    contact.setNifty_name(vc.getFirstName());
                    long upID = dbAdapter.setUpdateRegisterName(contact.getJid(), vc.getFirstName(), jid);

                    Constant.printMsg("MUC Info Profile names ::::::: " + contact.getJid() + dbAdapter.isjidExist(contact.getJid()) +
                            +upID);
                }
            }

        } catch (NoResponseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (XMPPErrorException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (NotConnectedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (Exception e1) {
            Constant.printMsg("MUC Info Profile names 000:::::: "
                    + e1.toString());
        }
    }


    // Only for updating the contact list

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Constant.printMsg("callde request code 111");
            if (resultCode == 1) {
                if (data != null) {
                    res_jid = data.getStringExtra("jid");

                    Constant.printMsg("callde request code resjid : " + res_jid);

                    if (res_jid != null) {

                        Constant.printMsg("callde request code resjid : " + res_jid.length());

                        if (res_jid.length() > 0) {

                            String selected[] = res_jid.split(",");

                            selected_m = res_jid.split(",");

                            if (resultListContacts != null) {
                                if (resultListContacts.size() > 0)
                                    resultListContacts.clear();
                            }

                            resultListContacts = new ArrayList<String>(
                                    Arrays.asList(selected));
                            // Toast.makeText(MUC_Info.this, ""+jid,
                            // Toast.LENGTH_LONG).show();
                            Log.d("Mvc", "Selected jid::" + res_jid + "--"
                                    + resultListContacts.size());

                            contact_list = new ArrayList<ContactsGetSet>();
                            contact_list.clear();

                            if (resultListContacts != null) {
                                if (resultListContacts.size() > 0) {

                                    if (MultiselectMembers.myPd_ring != null)
                                        MultiselectMembers.myPd_ring.dismiss();

                                    myPd_ring.show();
                                    myPd_ring.setCancelable(false);
                                    myPd_ring.setCanceledOnTouchOutside(false);

                                    active = true;

                                    new LoopContactsAsync().executeOnExecutor(
                                            AsyncTask.THREAD_POOL_EXECUTOR);

                                }
                            }

					/*
                     * if (!dbAdapter.isGroupMember(this.jid, jid)) {
					 * Add_Meber(jid); } else { Toast.makeText( MUC_Info.this,
					 * String.format( getResources() .getString(
					 * R.string.already_member_of_group),
					 * dbAdapter.getContact(jid) .getDisplay_name()),
					 * Toast.LENGTH_SHORT).show();
					 */

                        }
                    }
                }
            }
        } else if (requestCode == 2) {
            Constant.printMsg("callde request code 222");
            if (resultCode == 2) {
                if (data != null) {
                    String jid = data.getStringExtra("jid");
                    Log.d("Activity Result", "Selected Admin jid::" + jid);
                    if (jid != null && jid != "") {
                        Admin_exit(jid);
                    }
                    // Add_Meber(jid);
                    Intent ii = new Intent(MUC_Info.this, SliderTesting.class);
                    startActivity(ii);
                }
            }
        } else if (REQUEST_CODE_ADD_CONTACT == 12) {
            Constant.printMsg("called add to contact");

            Constant.mFromGroupInfo = true;

            Intent ii = new Intent(MUC_Info.this, SliderTesting.class);
            startActivity(ii);
            Log.d(TAG, "true");

        }
    }

    public void addNew_Admin(String new_admin) {
        try {
            Form f1 = muc.getConfigurationForm();
            List<String> admin = new ArrayList<String>();
            admin.add(new_admin);
            Form submitForm = f1.createAnswerForm();
            JSONObject main_job = new JSONObject();

            for (Iterator fields = f1.getFields().iterator(); fields.hasNext(); ) {
                FormField field = (FormField) fields.next();

                String s = "";

                Iterator<String> am = field.getValues().iterator();

                while (am.hasNext()) {
                    s = am.next();

                }

                if (field.getVariable().equals("muc#roomconfig_roomdesc")) {

                    JSONObject json = new JSONObject(s);
                    JSONObject j_obj = json.getJSONObject("data");

                    String previous_Admin = j_obj.getString(Constant.GROUP_ADMIN);

                    Constant.printMsg("MUC previous admins :" + previous_Admin);

                    j_obj.put(Constant.GROUP_ADMIN, previous_Admin + "," + new_admin);
                    j_obj.put(Constant.GROUP_TYPE,
                            sp.getString(jid + "_group_type", "none"));
                    j_obj.put(Constant.GROUP_TOPIC,
                            sp.getString(jid + "_group_question", "none"));
                    j_obj.put(Constant.TOPIC_OPTION, sp.getString(jid
                            + "_group_question_options", "none"));
                    j_obj.put(Constant.TIMESTAMP,
                            "" + chat_list.getTimestamp());
                    main_job.put("data", j_obj);
                    // Log.d("Muc_invitation", "Room Admin::" + room_admin);
                }
                // //Constant.printMsg("Form Field::"+field.getLabel()+"::"+field.getVariable()+"::"+s);
            }

            Log.d(TAG, "New Room Configuration::" + main_job.toString());
            submitForm
                    .setAnswer("muc#roomconfig_roomdesc", main_job.toString());
            muc.sendConfigurationForm(submitForm);


            Message msg = new Message(JidCreate.from(jid), Type.groupchat);
            msg.setBody("Change admin");
            JivePropertiesManager.addProperty(msg, "ID", 4);
            JivePropertiesManager.addProperty(msg, "media_type",
                    "0");
            JivePropertiesManager
                    .addProperty(msg, "Add_Admin", new_admin);

            msg.setStanzaId("memberaddadmin_"
                    + new Date().getTime());
            muc.sendMessage(msg);

            Constant.printMsg("MUC New Admin " + msg.toXML());

            long l = dbAdapter.updateGroupMembers(jid, new_admin, 1);
            Log.d(TAG, "Room Admin updated::" + l);

//            SendWeb_Group.Update_Admin_on_web_async(MUC_Info.this, jid,
//                    new_admin);


            Intent chat_broadcast = new Intent("update_list");
            sendBroadcast(chat_broadcast);

        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            Constant.printMsg("MUC Admin " + e.toString());
        }
    }

    public void Admin_exit(String new_admin) {
        try {

            Form f1 = muc.getConfigurationForm();
            List<String> admin = new ArrayList<String>();
            admin.add(new_admin);
            Form submitForm = f1.createAnswerForm();
            JSONObject main_job = new JSONObject();

            for (Iterator fields = f1.getFields().iterator(); fields.hasNext(); ) {
                FormField field = (FormField) fields.next();

                String s = "";

                Iterator<String> am = field.getValues().iterator();

                while (am.hasNext()) {
                    s = am.next();

                }

                if (field.getVariable().equals("muc#roomconfig_roomdesc")) {

                    JSONObject json = new JSONObject(s);
                    JSONObject j_obj = json.getJSONObject("data");
                    j_obj.put(Constant.GROUP_ADMIN, new_admin);
                    j_obj.put(Constant.GROUP_TYPE,
                            sp.getString(jid + "_group_type", "none"));
                    j_obj.put(Constant.GROUP_TOPIC,
                            sp.getString(jid + "_group_question", "none"));
                    j_obj.put(Constant.TOPIC_OPTION, sp.getString(jid
                            + "_group_question_options", "none"));
                    j_obj.put(Constant.TIMESTAMP,
                            "" + chat_list.getTimestamp());
                    main_job.put("data", j_obj);
                    // Log.d("Muc_invitation", "Room Admin::" + room_admin);
                }
                // //Constant.printMsg("Form Field::"+field.getLabel()+"::"+field.getVariable()+"::"+s);
            }

            Log.d(TAG, "New Room Configuration::" + main_job.toString());
            submitForm
                    .setAnswer("muc#roomconfig_roomdesc", main_job.toString());
            muc.sendConfigurationForm(submitForm);

            try {

                muc.revokeOwnership(JidCreate.from(KachingMeApplication.getUserID()
                        + KachingMeApplication.getHost()));

                String mem_list = null;
                Collection<Affiliate> owner = muc.getOwners();

                int i = 0;
                for (Affiliate affiliate : owner) {
                    if (i == 0) {
                        mem_list = affiliate.getJid().toString();
                    } else {
                        mem_list = mem_list + "," + affiliate.getJid();
                    }
                    i++;
                    Log.d("MUC_info", "Owner::" + affiliate.getJid());
                }

                Message msg = new Message(JidCreate.from(jid), Type.groupchat);

                msg.setBody(mem_list);
                JivePropertiesManager.addProperty(msg, "ID", 2);
                JivePropertiesManager.addProperty(
                        msg,
                        "Removed_member",
                        KachingMeApplication.getUserID()
                                + KachingMeApplication.getHost());

                msg.setStanzaId(Constant.MEMBERREMOVEMESSAGE
                        + new Date().getTime());
                JivePropertiesManager.addProperty(msg, "media_wa_type", "0");
                muc.sendMessage(msg);
                Delete_Local();
                muc.leave();
//                SendWeb_Group.Update_Admin_on_web_async(MUC_Info.this, jid,
//                        new_admin);
//                SendWeb_Group.Update_Memberlist_on_web_async(MUC_Info.this,
//                        jid, mem_list);

            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                // TODO: handle exception
            }

        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            e.printStackTrace();
        }
    }

    public void Delete_Local() {
        editor.remove(jid);
        editor.remove(jid + "_admin");

        editor.commit();

        dbAdapter.setDeleteContact(jid);
        dbAdapter.setDeleteMessages(jid);
        dbAdapter.setDeleteChatList(jid);
        dbAdapter.deleteGroup(jid);
        try {
            BookmarkManager bm = BookmarkManager
                    .getBookmarkManager(TempConnectionService.connection);
            bm.removeBookmarkedConference(JidCreate.entityBareFrom(jid));
        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
        }

    }


    public class Remove_Async extends AsyncTask<Integer,String,String>
    {
        int positon;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdialog.show();
        }
        @Override
        protected String doInBackground(Integer... strings) {

            try{
            positon = strings[0];

            Log.d(TAG, "LLLLLLLL Removed Member::" + contact_list_duplicate.get(positon).getJid());
            muc.revokeOwnership(JidCreate.from(contact_list_duplicate.get(positon).getJid()));

            String mem_list = null;
            Collection<Affiliate> owner = muc.getOwners();

            int i = 0;
            for (Affiliate affiliate : owner) {
                if (i == 0) {
                    mem_list = affiliate.getJid().toString();
                } else {
                    mem_list = mem_list + "," + affiliate.getJid();
                }
                i++;
                Log.d("MUC_info", "LLLLLLLL Owner Left::" + affiliate.getJid());
            }
            String packate_id = "" + new Date().getTime();

            dbAdapter.deleteGroupMembers(jid, contact_list_duplicate.get(positon)
                    .getJid());

            Message msg = new Message(JidCreate.from(jid), Type.groupchat);
            msg.setStanzaId(Constant.MEMBERREMOVEMESSAGE + packate_id);

            // msg.setSubject(muc.getSubject());
            msg.setBody(mem_list);
            JivePropertiesManager.addProperty(msg, "Removed_member",
                    contact_list_duplicate.get(positon).getJid());
            JivePropertiesManager.addProperty(msg, "ID", 2);
            JivePropertiesManager.addProperty(msg, "media_wa_type", "0");
            muc.sendMessage(msg);
            Constant.printMsg("LLLLLL mesg sent");
            editor.putString(jid, mem_list);
            editor.commit();

            muc.revokeMembership(JidCreate.from(contact_list_duplicate.get(positon).getJid()));

//            SendWeb_Group.Update_Memberlist_on_web_async(MUC_Info.this, jid,
//                    mem_list);



        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
        Constant.printMsg("LLLLLL remove mem exp :" + e.toString());
    }

        return null;
        }



        @Override
        protected void onPostExecute(String s) {
            progressdialog.cancel();

            Intent chat_broadcast = new Intent("update_list");
            sendBroadcast(chat_broadcast);

            super.onPostExecute(s);
        }
    }

    public void Remove_Meber(String jid_mem) {

        try {

            muc.revokeOwnership(JidCreate.from(jid_mem));
            String mem_list = null;
            Collection<Affiliate> owner = muc.getOwners();

            int i = 0;
            for (Affiliate affiliate : owner) {
                if (i == 0) {
                    mem_list = affiliate.getJid().toString();
                } else {
                    mem_list = mem_list + "," + affiliate.getJid();
                }
                i++;
                Log.d("MUC_info", "Owner::" + affiliate.getJid());
            }
            dbAdapter.deleteGroupMembers(jid, jid_mem);
            Log.e("MUC_info", "Room subject::" + muc.getSubject());
            Message msg = new Message(JidCreate.from(jid), Type.groupchat);
            // msg.setSubject(muc.getSubject());
            msg.setBody(mem_list);
            JivePropertiesManager.addProperty(msg, "ID", 2);
            JivePropertiesManager.addProperty(msg, "Removed_member", jid_mem);
            msg.setPacketID(Constant.MEMBERREMOVEMESSAGE
                    + new Date().getTime());
            JivePropertiesManager.addProperty(msg, "media_wa_type", "0");
            muc.sendMessage(msg);

            muc.leave();
//            SendWeb_Group.Update_Memberlist_on_web_async(MUC_Info.this, jid,
//                    mem_list);

        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            e.printStackTrace();
            // TODO: handle exception
        }

    }

    public void setStatus() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Subject");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);

        input.setText(txt_subject.getText().toString());
        input.setSelectAllOnFocus(true);
        alert.setView(input);

        alert.setPositiveButton(getResources().getString(R.string.Ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String value = input.getText().toString();

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //  muc.changeSubject(value);

                                    Message msg = new Message(JidCreate.from(jid), Type.groupchat);
                                    msg.setStanzaId(Constant.SUBJECTCHNAGE
                                            + new Date().getTime());
                                    msg.setSubject("" + value);
                                    msg.setBody("");
                                    muc.sendMessage(msg);

                                    Constant.printMsg("MUC Subject " + msg.toXML());

                                    try {

                                        Form f1 = muc.getConfigurationForm();
                                        Form submitForm = f1.createAnswerForm();
                                        submitForm.setAnswer("muc#roomconfig_roomname",
                                                value);
                                        muc.sendConfigurationForm(submitForm);
                                    } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                                        // TODO: handle exception

                                        Constant.printMsg("powieq" + e.toString());

                                    }

//                            MUC_Info.this.runOnUiThread(new Runnable() {
//                                public void run() {
//                                    SendWeb_Group.Update_Status_on_web_async(
//                                            MUC_Info.this, jid, txt_subject
//                                                    .getText().toString());
//                                }
//                            });

                                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                                    Constant.printMsg("powieq121212" + e.toString());
                                    e.printStackTrace();
                                    // TODO: handle exception
                                }
                            }
                        });
                        thread.start();
                        txt_subject.setText(value);


                    }
                });

        alert.setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

        alert.show();
    }

    public String getRow_id(String phoneNumber) {
        String row_id = "";
        ContentResolver contentResolver = getContentResolver();

        Uri uri = Uri.withAppendedPath(
                PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));

        String[] projection = new String[]{PhoneLookup.DISPLAY_NAME,
                PhoneLookup._ID};

        Cursor cursor = contentResolver
                .query(uri, projection, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String contactName = cursor.getString(cursor
                        .getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME));
                String contactId = cursor.getString(cursor
                        .getColumnIndexOrThrow(PhoneLookup._ID));
                row_id = contactId;
                Log.d("Service", "contactMatch name: " + contactName);
                Log.d("Service", "contactMatch id: " + contactId);
            }
            cursor.close();
        }
        return row_id;
    }

    public String setMyUserJid() {

        try {
            myUserJid = dbAdapter.getLogin().get(0).getUserName()
                    + KachingMeApplication.getHost();
        } catch (Exception e) {

        }

        return myUserJid;
    }

    private class MyAsync extends AsyncTask<String, String, String> {
        Boolean count_admin = false;
        String my_jid = KachingMeApplication.getUserID()
                + KachingMeApplication.getHost();

        @Override
        protected void onPreExecute() {
            progressdialog.show();

            if (!myPd_ring.isShowing())
                myPd_ring.dismiss();

            count_admin = false;
            contact_list = new ArrayList<ContactsGetSet>();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            contact_list = new ArrayList<ContactsGetSet>();
            contact_list.clear();
            mem_list = sp.getString(jid, "");

            admin = (dbAdapter.getGroupAdmin(jid)).getJid();

            ArrayList<GroupParticipantGetSet> list = new ArrayList<GroupParticipantGetSet>();
            Constant.printMsg("group jid ::::::::   " + jid);

            list = dbAdapter.getGroupMembers(jid);

            for (GroupParticipantGetSet groupParticipantGetSet : list) {
                ContactsGetSet contact = dbAdapter
                        .getContact_grp(groupParticipantGetSet.getJid());

                if (contact != null) {
                    if (contact.getJid() != null)
                        Constant.printMsg("MUC Info Profile names  jid ::::::::   " + contact.getJid());

                }

                if (contact == null) {
                    contact = new ContactsGetSet();
                    if (groupParticipantGetSet.getJid().equals(my_jid)) {
                        contact.setDisplay_name("You");
                    } else {
                        contact.setDisplay_name(groupParticipantGetSet.getJid()
                                .split("@")[0]);

                    }

                    contact.setJid(groupParticipantGetSet.getJid());

                }
                Constant.printMsg("MUC Info Profile names  jid ::::::::   " + groupParticipantGetSet.getRegister_name());
                if (contact.getNifty_name() == null) {
                    contact.setNifty_name(groupParticipantGetSet.getRegister_name());
                }

                if (groupParticipantGetSet.getJid().equals(my_jid)) {
                    contact.setDisplay_name("You");
                    contact.setPhoto_ts(KachingMeApplication.getAvatar());
                    contact.setJid(my_jid);
                }

                if (groupParticipantGetSet.getAdmin() == 1) {
                    contact.setIs_admin(1);
                } else {
                    contact.setIs_admin(0);
                }


                Log.d(TAG, "Member JID::" + contact.getJid());
                Log.d(TAG, "Member 2 JID::" + groupParticipantGetSet.getJid());


                try {
                    if (contact.getDisplay_name().length() > 0) {

                        if (contact.getDisplay_name().matches("[0-9]+")) {
                            //  setProfileName(contact);sd
                            isContactNumberExits = true;

                        }

                        contact_list.add(contact);
                    }
                } catch (Exception e) {

                }

                /*if (Is_Admin) {
                    contact_list.add(contact);
                } else {
                    Log.d("", "Contact jid::" + contact.getJid()
                            + " group_admin::" + group_admin);
                    if (int_group_type > 0) {
                        if (groupParticipantGetSet.getAdmin()==1) {
                            contact_list.add(contact);
                        }
                    } else {
                        contact_list.add(contact);
                    }
                }*/

            }


            Log.d("MUC_info_normal", "List Sizesqwqw:" + contact_list.size());

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

           /* if (!myPd_ring.isShowing())
                myPd_ring.show();*/

            Log.d("MUC_info_normal", "List Size:" + contact_list.size());
            Constant.printMsg("List Size:" + contact_list.size());

            if (active) {
                HashSet<String> hashSet = new HashSet<String>();
                HashSet<ContactsGetSet> hashSetObj = new HashSet<ContactsGetSet>();

                for (ContactsGetSet contObj : contact_list) {
                    if (dbAdapter.isGroupMember(MUC_Info.this.jid,
                            contObj.getJid()))
                        if (hashSet.add(contObj.getJid())) {

                            try {
                                if (contObj.getDisplay_name().length() > 0) {

                                    hashSetObj.add(contObj);
                                }
                            } catch (Exception e) {

                            }
                        }
                }

                Log.d("MUC_Both_Anormal", "List Size:" + hashSet.size() + "--"
                        + contact_list.size() + "--" + hashSetObj.size());
                if ((hashSet.size() > 0)
                        && (hashSet.size() < contact_list.size())) {
                    // hashSet.addAll(contact_list);
                    contact_list.clear();
                    contact_list.addAll(hashSetObj);

                    contact_list_duplicate = contact_list;

                    hashSet.clear();
                }
            } else {
                contact_list_duplicate = contact_list;


                if (myPd_ring.isShowing())
                    myPd_ring.dismiss();
            }

            Log.d("MUC_info_Anormal", "List Size:" + contact_list.size());

            // Constant.printMsg(":print size ::::: "+customer.getJid() +
            // "    "+contact_list.size());
            contact_list_duplicate = contact_list;
            selected_users = contact_list;
            adapter = new UserListAdapter(MUC_Info.this,
                    R.layout.muc_mem_list_items, contact_list);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            txt_member.setText(contact_list.size() + " "
                    + getResources().getString(R.string.out_of_200_members));

            progressdialog.cancel();

            if (Connectivity.isOnline(MUC_Info.this) && Connectivity.isTempConnection() && isContactNumberExits)
                new UpdateProfileAsync().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

			/*
             * if(resultListContacts!=null) { if(resultListContacts.size()>0) {
			 *
			 * resultListContacts.remove(0); if(resultListContacts.size()>0)
			 * Add_Meber(resultListContacts.get(0));
			 *
			 * } else { adapter = new UserListAdapter(MUC_Info.this,
			 * R.layout.muc_mem_list_items, contact_list);
			 * list.setAdapter(adapter); adapter.notifyDataSetChanged();
			 *
			 * txt_member.setText(contact_list.size() + " " +
			 * getResources().getString(R.string.out_of_200_members));
			 *
			 *
			 * if(resultListContacts!=null) resultListContacts.clear();
			 *
			 * if(myPd_ring!=null) myPd_ring.cancel();
			 *
			 *
			 * } } else {
			 *
			 *
			 *
			 *
			 * txt_member.setText(contact_list.size() + " " +
			 * getResources().getString(R.string.out_of_200_members));
			 *
			 *
			 * if(resultListContacts!=null) resultListContacts.clear();
			 *
			 * if(myPd_ring!=null) myPd_ring.cancel();
			 *
			 *
			 * }
			 */

        }

    }

    private class UpdateProfileAsync extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                for (ContactsGetSet contact : contact_list) {

                    Constant.printMsg("MUC Info Profile names 111 : " + contact.getNifty_name() + "  " + contact.getDisplay_name());

                    if (contact.getNifty_name()
                            == null) {
                        if (contact.getDisplay_name().length() > 0) {

                            if (contact.getDisplay_name().matches("[0-9]+")) {
                                setProfileName(contact);


                            }
                        }

                    }
                }
            } catch (Exception e) {

            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            Log.d("MUC_info_normal", "List Size:" + contact_list.size());
            Constant.printMsg("List Size:" + contact_list.size());

            // Constant.printMsg(":print size ::::: "+customer.getJid() +
            // "    "+contact_list.size());


            contact_list_duplicate = contact_list;

            adapter = new UserListAdapter(MUC_Info.this,
                    R.layout.muc_mem_list_items, contact_list);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            super.onPostExecute(result);

        }

    }

    // Async is used for updating the contacts in group
    private class LoopContactsAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Add_Meber(resultListContacts.get(0));
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // if(myPd_ring.isShowing())
            // myPd_ring.dismiss();

            Constant.printMsg("onPost " + myPd_ring.isShowing());

            if (myPd_ring.isShowing())
                myPd_ring.dismiss();

            Constant.printMsg("onPost " + myPd_ring.isShowing());
//            adapter = new UserListAdapter(MUC_Info.this,
//                    R.layout.muc_mem_list_items, contact_list);
//            list.setAdapter(adapter);

            contact_list_duplicate = contact_list;
            adapter.notifyDataSetChanged();

           /* if (active) {

                // start a new thread to process job
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(6000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        MUC_Info.this.runOnUiThread(new Runnable() {
                            public void run() {

                                if (myPd_ring.isShowing())
                                    myPd_ring.cancel();
                            }
                        });

                    }
                }).start();
            } else {
                if (myPd_ring.isShowing())
                    myPd_ring.cancel();
            }*/


            super.onPostExecute(result);
        }

    }

    private class UserListAdapter extends ArrayAdapter<ContactsGetSet> {
        ArrayList<ContactsGetSet> list;
        Context context;
        private int resource;
        private LayoutInflater layoutInflater;

        public UserListAdapter(Context context, int textViewResourceId,
                               ArrayList<ContactsGetSet> objects) {
            super(context, textViewResourceId, objects);
            this.resource = textViewResourceId;
            layoutInflater = LayoutInflater.from(context);
            list = objects;
            this.context = context;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

//            View vi=convertView;
//            if(convertView==null)


            ViewHolder holder;

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            LinearLayout container = (LinearLayout) (inflater.inflate(resource,
                    parent, false));
            ContactsGetSet user = list.get(position);

            // holder
            holder = new ViewHolder();

            // set name

            holder.name = (TextView) container.findViewById(R.id.name);
            holder.txt_aff = (TextView) container.findViewById(R.id.txt_aff);
            holder.img_group_yes_no_indicator = (ImageView) container
                    .findViewById(R.id.img_yes_no_indicator);
            holder.txt_selected_options = (TextView) container
                    .findViewById(R.id.txt_selected_option);
            holder.status = (TextView) container.findViewById(R.id.status);
            holder.profile = (TextView) container.findViewById(R.id.profile);
            holder.img = (ImageView) container
                    .findViewById(R.id.avtarimg);

            LinearLayout.LayoutParams mGroupTextItemParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mGroupTextItemParams.width = (int) width * 45 / 100;
//            mGroupTextItemParams.height = (int) height * 5 / 100;
            mGroupTextItemParams.leftMargin = width * 2 / 100;
//            mGroupImgItemParams.topMargin = height * 1 / 100;
            mGroupTextItemParams.gravity = Gravity.CENTER;
            holder.name.setLayoutParams(mGroupTextItemParams);

            LinearLayout.LayoutParams mGroupProfileTextItemParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            mGroupTextItemParams.width = (int) width * 16 /100 ;
            mGroupProfileTextItemParams.height = (int) height * 5 / 100;
//            mGroupProfileTextItemParams.rightMargin = width * 2 / 100;
//            mGroupImgItemParams.topMargin = height * 1 / 100;
            mGroupProfileTextItemParams.gravity = Gravity.CENTER;
            holder.txt_aff.setLayoutParams(mGroupProfileTextItemParams);
            holder.txt_aff.setGravity(Gravity.CENTER | Gravity.RIGHT);

            LinearLayout.LayoutParams mGroupImgItemParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mGroupImgItemParams.width = (int) width * 25 / 100;
            mGroupImgItemParams.height = (int) width * 25 / 100;
//            mGroupImgItemParams.leftMargin = width * 2 / 100;
//            mGroupImgItemParams.topMargin = height * 1 / 100;
            mGroupImgItemParams.gravity = Gravity.CENTER;
            holder.img.setLayoutParams(mGroupImgItemParams);
            holder.img.setPadding(width * 3 / 100, width * 3 / 100, width * 3 / 100, width * 3 / 100);
            // holder.img.setScaleType(ImageView.ScaleType.FIT_XY);

//            img.setPadding(width * 5/100,width * 5/100,width * 5/100,width * 5/100);

            if (width >= 600) {

                holder.name.setTextSize(17);
                holder.txt_aff.setTextSize(17);

            } else if (width < 600 && width >= 480) {
                holder.name.setTextSize(16);
                holder.txt_aff.setTextSize(16);

            } else if (width < 480 && width >= 320) {
                holder.name.setTextSize(14);
                holder.txt_aff.setTextSize(14);


            } else if (width < 320) {
                holder.name.setTextSize(12);
                holder.txt_aff.setTextSize(12);

            }


            container.setTag(holder);


            if (user.getDisplay_name().matches("[0-9]+")) {

                Constant.printMsg("MUC Info Profile names : " + user.getNifty_name() + "  " + user.getStatus());
                holder.profile.setVisibility(View.VISIBLE);

            } else {
                holder.profile.setVisibility(View.GONE);
            }

            holder.name.setText(user.getDisplay_name());
            Log.d("UserChat", "User::" + user.getJid());
            Log.d("UserChat", "User::" + admin);
            if (user.getIs_admin() == 1) {
                holder.txt_aff.setVisibility(View.VISIBLE);
            }

//            System.gc();
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = false;
//            options.inPreferredConfig = Bitmap.Config.RGB_565;
//            options.inDither = true;
//            options.inSampleSize = 2;
            try {
                Bitmap bmp = null;
                System.gc();
                /*BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inDither = true;*/
                bmp = BitmapFactory.decodeByteArray(
                        user.getPhoto_ts(), 0,
                        user.getPhoto_ts().length);
                ProfileRoundImg roundImageProfile = new ProfileRoundImg(bmp);
                holder.img.setImageDrawable(roundImageProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }


//            try {
//
//                try {
//                    bmp = BitmapFactory.decodeByteArray(user.getPhoto_ts(), 0, user.getPhoto_ts().length, options);
//
//                } catch (OutOfMemoryError e) {
//                    Log.e("Map", "Tile Loader (241) Out Of Memory Error " + e.getLocalizedMessage());
//                    System.gc();
//                } catch (Exception e) {
//
//                }
//
//                ProfileRoundImg groupRoundImg = new ProfileRoundImg(bmp);
//                //holder.img.setBackgroundColor(R.drawable.profile_square_bg);
//                holder.img.setImageDrawable(groupRoundImg);
//                //holder.img.setImageBitmap(new AvatarManager().roundCornerImage(bmp, 180));
//            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//                // TODO: handle exception
//            }

            try {
                if (user.getNifty_name() != null) {
                    if (user.getNifty_name().contains(" ")) {
                        if (user.getNifty_name().split(" ")[0] != null && user.getNifty_name().split(" ")[1] != null)
                            holder.profile.setText(" ~ " + user.getNifty_name());
                        else if (user.getNifty_name().split(" ")[0] == null && user.getNifty_name().split(" ")[1] != null)
                            holder.profile.setText(" ~ " + user.getNifty_name().split(" ")[1]);
                        else if (user.getNifty_name().split(" ")[0] != null && user.getNifty_name().split(" ")[1] == null)
                            holder.profile.setText(" ~ " + user.getNifty_name().split(" ")[0]);
                        else
                            holder.profile.setVisibility(View.GONE);
                    } else
                        holder.profile.setText(" ~ " + user.getNifty_name());
                } else
                    holder.profile.setVisibility(View.GONE);
            } catch (Exception e) {

                Constant.printMsg("MUC Profile " + e.toString());

                holder.profile.setVisibility(View.GONE);
            }

            // set status

          /*  try {
                // connection = NiftyService.connection;
                if (TempConnectionService.connection != null) {
                    vc = VCardManager.getInstanceFor(
                            TempConnectionService.connection).loadVCard(
                            user.getJid());

                    Constant.printMsg("connection ::::::: "
                            + vc.getFirstName());

                    holder.profile.setText(" - " + vc.getFirstName());
                }

            } catch (NoResponseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (XMPPErrorException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NotConnectedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }*/
            try {
                holder.status.setText(user.getStatus());
            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                // TODO: handle exceptionas
                e.printStackTrace();
            }

            if ((int_group_type > 1 && int_group_type < 4)) {

                if (dbAdapter.getADV_Group_ans(jid, user.getJid()) == 0) {
                    holder.img_group_yes_no_indicator
                            .setVisibility(View.VISIBLE);
                    holder.img_group_yes_no_indicator.setImageDrawable(res
                            .getDrawable(R.drawable.ic_group_yes));
                } else if (dbAdapter.getADV_Group_ans(jid, user.getJid()) == 1) {
                    holder.img_group_yes_no_indicator
                            .setVisibility(View.VISIBLE);
                    holder.img_group_yes_no_indicator.setImageDrawable(res
                            .getDrawable(R.drawable.ic_group_no));
                } else {
                    holder.img_group_yes_no_indicator.setVisibility(View.GONE);
                }

            } else if (int_group_type == 4) {
                holder.txt_selected_options.setVisibility(View.VISIBLE);
                holder.txt_selected_options.setText(""
                        + dbAdapter.getADV_Group_ans(jid, user.getJid()));
            }
            return container;
        }

        public class ViewHolder {

            public TextView name;
            public TextView txt_aff;
            public TextView txt_selected_options;
            public TextView status;
            public TextView profile;

            public ImageView img_group_yes_no_indicator, img;

        }
    }

    public class AsyncAdd_Contact extends AsyncTask<String, String, String> {
        private VCard vc = new VCard();
        private String jid;
        private ContactsGetSet contact = new ContactsGetSet();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            jid = params[0];
            try {

                Log.d(TAG, "Load vcard for::" + jid);
                vc = VCardManager.getInstanceFor(
                        TempConnectionService.connection).loadVCard(JidCreate.entityBareFrom(jid));
                contact.setStatus(vc.getField("SORT-STRING"));
                contact.setNifty_email(vc.getEmailWork());
                contact.setNifty_name(vc.getFirstName());
                contact.setJid(vc.getJabberId());
                contact.setDisplay_name(jid.split("@")[0]);
                contact.setIs_niftychat_user(1);
                contact.setJid(jid);
                contact.setNumber(jid.split("@")[0]);
                contact.setIsInContactList(1);
                if (getRow_id(jid.split("@")[0]).equals("")) {
                    contact.setRaw_contact_id("" + new Date().getTime());
                } else {
                    String s = getRow_id(jid.split("@")[0]);
                    contact.setRaw_contact_id(s);
                }

                contact.setUnseen_msg_count(0);

                contact.setPhoto_ts(new AvatarManager().saveBitemap(vc
                        .getAvatar()));

                Log.i(TAG, "VCARD::" + vc.toString());
                Log.d(TAG, "VC_EMail::" + vc.getEmailWork());
                Log.d(TAG, "VC_FIRST_NAME::" + vc.getFirstName());
                Log.d(TAG, "VC_JID::" + vc.getJabberId());
                // Log.d(TAG, "VC_IS_UPDATED::"+l);

                if (!contact.getJid().equals(
                        KachingMeApplication.getUserID()
                                + KachingMeApplication.getHost())) {
                    Log.d(TAG, "Subscribed user::" + vc.getJabberId());
                    Presence subscribed = new Presence(Presence.Type.subscribe);
                    subscribed.setTo(vc.getJabberId());
                    TempConnectionService.connection.sendStanza(subscribed);

                    Roster roster = Roster
                            .getInstanceFor(TempConnectionService.connection);
                    roster.setSubscriptionMode(SubscriptionMode.accept_all);
                    roster.createEntry(JidCreate.bareFrom(vc.getJabberId()), vc.getJabberId(), null);

                    RosterExchangeManager rem = new RosterExchangeManager(
                            TempConnectionService.connection);
                    rem.send(roster, JidCreate.from(vc.getJabberId()));

                }

            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();

            }

            Log.d("Vcard", "VCard Loaded For::" + vc.toXML());
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (vc.getJabberId() != null
                    && !contact.getJid().equals(
                    KachingMeApplication.getUserID()
                            + KachingMeApplication.getHost()))

            {

                if (!dbAdapter.isjidExist(vc.getJabberId())) {
                    dbAdapter.insertContacts(contact);
                } else {
                    dbAdapter.setUpdateVcard(contact);
                }
            }

            Intent intent = new Intent("Add_New_Contact");
            intent.putExtra("jid", jid);
            sendBroadcast(intent);
            super.onPostExecute(result);
        }
    }


}
