package com.wifin.kachingme.chat.muc_chat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.SherlockBaseActivity;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.Utils;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.cropimage.CropImage;

import org.apache.http.Header;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

//import org.apache.http.Header;
//import cz.msebera.android.httpclient.Header;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;

public class GroupProfilePic extends SherlockBaseActivity {

    private static final int RESULT_CODE_GALLERY = 1, RESULT_CODE_CAMERA = 2,
            REQUEST_CODE_CROP_IMAGE = 3;
    public static String TAG = GroupProfilePic.class.getSimpleName();
    BroadcastReceiver broadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().toString()
                    .equals(Constant.BROADCAST_UPDATE_GROUP_ICON)) {
                try {
                    Log.d(TAG, "Group icon update broadcast recieved");
                    Bitmap bmp = BitmapFactory
                            .decodeFile(KachingMeApplication.PROFILE_PIC_DIR
                                    + jid.toString().split("@")[0] + ".png");
                    img_profile_pic.invalidate();
                    img_profile_pic.setImageBitmap(bmp);
                } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                    e.printStackTrace();
                    // TODO: handle exception
                }
            }

        }

    };
    ImageView img_profile_pic;
    Resources res;
    String jid;
    ProgressDialog pd;
    DatabaseHelper dbadapter;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_profile_pic);

        img_profile_pic = (ImageView) findViewById(R.id.img_profile_pic);

        res = getResources();

        getSupportActionBar().setTitle(res.getString(R.string.group_icon));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jid = bundle.getString("jid");
        }

        pd = new ProgressDialog(GroupProfilePic.this);
        pd.setMessage(res.getString(R.string.uploading) + "....");
        try {
            Bitmap bmp = BitmapFactory
                    .decodeFile(KachingMeApplication.PROFILE_PIC_DIR
                            + jid.toString().split("@")[0] + ".png");
            Drawable drawable = new BitmapDrawable(bmp);
            // getSupportActionBar().setIcon(drawable);
            img_profile_pic.setImageDrawable(drawable);

        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
        }

        dbadapter = KachingMeApplication.getDatabaseAdapter();

    }

    // private ServiceConnection mConnection = new ServiceConnection() {
    // @Override
    // public void onServiceConnected(ComponentName className, IBinder service)
    // {
    //
    // mBoundService = ((KaChingMeService.LocalBinder) service)
    // .getService();
    // connection = mBoundService.getConnection();
    // if (connection.isConnected()) {
    // try {
    // SharedPreferences sp = getSharedPreferences(
    // KachingMeApplication.getPereference_label(),
    // Activity.MODE_PRIVATE);
    // DiscussionHistory history = new DiscussionHistory();
    // history.setSince(Utils.getBookmarkDate(sp.getString(
    // Constant.LAST_REFRESH_TIME + "_" + jid,
    // Utils.getBookmarkTime())));
    // muc = mBoundService.getMUC_MANAGER().getMultiUserChat(jid);
    // muc.join(
    // KachingMeApplication.getUserID()
    // + KachingMeApplication.getHost(), null,
    // history, 30000L);
    // } catch (XMPPException e) {// ACRA.getErrorReporter().handleException(e);
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
    // bindService(new Intent(GroupProfilePic.this, KaChingMeService.class),
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_edit_profile_pic:
                selectImage();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        // merlin.bind();
        // doBindService();
        registerReceiver(broadcast, new IntentFilter(
                Constant.BROADCAST_UPDATE_GROUP_ICON));

        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        // doUnbindService();
        unregisterReceiver(broadcast);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {

        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {

            case RESULT_CODE_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    final String filePath = cursor.getString(columnIndex);
                    Constant.printMsg("File Upload " + filePath);

                    cursor.close();

                    if (filePath != null) {


                        File file = null;
                        try {
                            file = new File(filePath);
                        } catch (Exception e) {
                            Toast.makeText(this, "File path not found. Try again", Toast.LENGTH_SHORT).show();
                        }
                        if (file.length() > 26214400) {
                            new AlertManager().showAlertDialog(this, getResources()
                                            .getString(R.string.imagesize_must_be_smaller),
                                    true);
                        } else {
                    /* upload_profile_pic(filePath); */
                            Intent intent = new Intent(this, CropImage.class);
                            intent.setType("image/*");
                            intent.putExtra(CropImage.IMAGE_PATH, filePath);

					/*
                     * intent.putExtra("outputX", 256);
					 * intent.putExtra("outputY", 256);
					 */
                            intent.putExtra("aspectX", 1);
                            intent.putExtra("aspectY", 1);
                            intent.putExtra("scale", true);
                            intent.putExtra("return-data", true);
                            startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);

                            // uploadFile(filePath, true);
                        }
                    } else {
                        Toast.makeText(this, "File path not found. Try again", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case RESULT_CODE_CAMERA:
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

					/* upload_profile_pic(fileUri.getPath()); */
                        Intent intent = new Intent(GroupProfilePic.this,
                                CropImage.class);
                        intent.setType("image/*");
                        intent.putExtra(CropImage.IMAGE_PATH, fileUri.getPath());
					/*
					 * intent.putExtra("outputX", 256);
					 * intent.putExtra("outputY", 256);
					 */
                        intent.putExtra("aspectX", 1);
                        intent.putExtra("aspectY", 1);
                        intent.putExtra("scale", true);
                        intent.putExtra("return-data", true);
                       startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
                      //  startActivity(intent);
                        // uploadFile(fileUri.getPath(), true);
                    }
                }
                break;
            case REQUEST_CODE_CROP_IMAGE:

                Constant.printMsg("kdjfafaf");

                if (resultCode == RESULT_OK) {
                    Constant.printMsg("kdjfafaf11111");
                    Log.d(TAG, "Image Cropped");
                    final Bundle extras = imageReturnedIntent.getExtras();

                    if (extras != null) {
                        try {
                            Bitmap bitmap = CropImage.croppedImage;
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            img_profile_pic.invalidate();
                            img_profile_pic.setImageBitmap(bitmap);
                            upload_profile_pic(out.toByteArray());

                            CropImage.croppedImage = null;
                        } catch (Exception e) {

                        }

                    }

                }
                break;

        }
    }

    public void upload_profile_pic(final byte[] filedata) {

        boolean success = (new File(KachingMeApplication.PROFILE_PIC_DIR)).mkdirs();
        RequestParams request_params = new RequestParams();
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(
                    KachingMeApplication.PROFILE_PIC_DIR + jid.split("@")[0]
                            + ".png"));
            fos.write(filedata);
            fos.close();

            request_params.put("uploaded_file", new File(
                    KachingMeApplication.PROFILE_PIC_DIR + jid.split("@")[0]
                            + ".png"));
            request_params.put("filename", jid.split("@")[0]);

        } catch (FileNotFoundException e) {// ACRA.getErrorReporter().handleException(e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {// ACRA.getErrorReporter().handleException(e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.post(KachingMeConfig.UPLOAD_GROUP_ICON_PHP,
                request_params,
                new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        pd.show();
                        super.onStart();
                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        pd.dismiss();

                        img_profile_pic.setImageURI(Uri.fromFile(new File(
                                KachingMeApplication.PROFILE_PIC_DIR
                                        + jid.split("@")[0] + ".png")));

                        Intent chat_broadcast = new Intent(
                                Constant.BROADCAST_UPDATE_GROUP_ICON);
                        chat_broadcast.putExtra("jid", "" + jid);
                        getApplicationContext().sendBroadcast(chat_broadcast);

                        super.onFinish();
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub

                        FileOutputStream fos;
                        try {
                            fos = new FileOutputStream(new File(
                                    KachingMeApplication.PROFILE_PIC_DIR
                                            + jid.split("@")[0] + ".png"));
                            fos.write(filedata);
                            fos.close();

                            Message message = new Message(jid, Type.groupchat);
                            message.setStanzaId(Constant.GROUPICONCHNAGE
                                    + new Date().getTime());
                            message.setBody("");

                            JivePropertiesManager.addProperty(message, "ID", 8);
                            JivePropertiesManager.addProperty(message,
                                    "memberid", KachingMeApplication.getjid());
                            MUCTest.muc.sendMessage(message);


                            MessageGetSet msg = new MessageGetSet();
                            msg.setData("");
                            msg.setKey_from_me(0);
                            msg.setKey_id(message.getPacketID());
                            msg.setKey_remote_jid(jid);
                            msg.setNeeds_push(0);
                            msg.setStatus(0);
                            msg.setTimestamp(new Date().getTime());
                            msg.setRemote_resource(KachingMeApplication.getjid());
                            msg.setMedia_wa_type("11");
                            dbadapter.setInsertMessages(msg);

                            int msg_id = dbadapter.getLastMsgid(jid);
                            if (dbadapter.isExistinChatList(jid)) {
                                dbadapter.setUpdateChat_lits(jid, msg_id);
                            } else {
                                dbadapter.setInsertChat_list(jid, msg_id);
                            }

                        } catch (FileNotFoundException e) {// ACRA.getErrorReporter().handleException(e);
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {// ACRA.getErrorReporter().handleException(e);
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (NotConnectedException e) {// ACRA.getErrorReporter().handleException(e);
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (Exception e) {

                        }

                    }

                });

    }

    private void selectImage() {

        final CharSequence[] options = {"Gallery", "Camera"};

        AlertDialog.Builder builder = new AlertDialog.Builder(
                GroupProfilePic.this);
        builder.setTitle("Group icon");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    fileUri = Utils.getOutputMediaFileUri(1);
                    // create a file to save the image
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set
                    // the
                    // image
                    // file
                    // name

                    // start the image capture Intent
                    startActivityForResult(intent, RESULT_CODE_CAMERA);

                    // image_picker(1);
                } else if (options[item].equals("Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_CODE_GALLERY);
                    // image_picker(2);

                }
				/*
				 * else if (options[item].equals("Cancel")) { dialog.dismiss();
				 * }
				 */
            }
        });
        builder.show();
    }
}
