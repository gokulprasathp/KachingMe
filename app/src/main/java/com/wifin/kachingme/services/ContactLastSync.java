package com.wifin.kachingme.services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;

import com.google.gson.Gson;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.ContactPost;
import com.wifin.kachingme.pojo.ContactResponseDto;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.GroupParticipantGetSet;
import com.wifin.kachingme.pojo.UserContactDto;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.util.Utils;

import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.ArrayList;
import java.util.List;

public class ContactLastSync extends IntentService {

    // AbstractXMPPConnection connection;

    public static String MOBILENO;
    public static String TAG = ContactLastSync.class.getSimpleName();
    Handler h;
    Thread refresh;
    DatabaseHelper dbAdapter;
    ContentResolver cr;
    VCard vc = new VCard();
    ArrayList<String> Roster_list = new ArrayList<String>();
    int no = 0;
    String country_code;
    // private int result = Activity.RESULT_CANCELED;
    GroupParticipantGetSet group_partcipant_getset;
    ArrayList<UserContactDto> contactList = new ArrayList<UserContactDto>();
    //AbstractXMPPConnection connection;
//    Activity activity;

    public ContactLastSync() {
        super(ContactLastSync.class.getSimpleName());
        // TODO Auto-generated constructor stub
        //connection = TempConnectionService.connection;
//        this.activity=activity;

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        Constant.printMsg("siva inside lastSync...");
        KachingMeApplication.getsharedpreferences_Editor()
                .putBoolean("IS_RE_SYNC", true).commit();
        refreshContact();
        // connection = KaChingMeService.connection;

        KachingMeApplication.getsharedpreferences_Editor()
                .putBoolean(Constant.CONTACT_GROUP_SYNC_STATUS, true).commit();

        KachingMeApplication.getsharedpreferences_Editor()
                .putBoolean("IS_RE_SYNC", false).commit();

        Intent intn = new Intent("Profile_Update");
        sendBroadcast(intn);
        Intent intn2 = new Intent(
                Constant.BROADCAST_COMPLETE_CONTACT_RE_SYNC_SERVICE_LAST);
        sendBroadcast(intn2);

    }


    public String jsonFormContact() {
        String d = null;
        String phone = KachingMeApplication.getjid().split("@")[0];
//        String phone = preferences.getString("MyPrimaryNumber", "");
        Constant.printMsg("siva post ta nuber...." + phone);
        ContactPost l = new ContactPost();
        l.setPhoneNumber(phone);
        l.setUserContactDtos(contactList);
        d = new Gson().toJson(l);
        Constant.printMsg("siva post  ::::" + d.toString());
        return d;
    }

    public void refreshContact() {
        // TODO Auto-generated method stub
//		RequestParams request_params = new RequestParams();
//		String str=KachingMeApplication.getUserID()
//                + KachingMeApplication.getHost();
//		request_params.put("Wquery", "'SELECT * FROM tbl_user WHERE jid in ("
//				+ str + ")'");
//		Constant.printMsg("query........"+request_params);

        int row_contact_id;
        cr = getContentResolver();
        dbAdapter = KachingMeApplication.getDatabaseAdapter();

        country_code = KachingMeApplication.getsharedpreferences().getString(
                Constant.COUNTRY_CODE_LABEL, "");
        Constant.printMsg("siva country code...." + country_code);
        KachingMeApplication.getsharedpreferences_Editor()
                .putBoolean(Constant.CONTACT_GROUP_SYNC_STATUS, false)
                .commit();
        Cursor phones = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
        int indexName = phones
                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = phones
                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int indexRowId = phones
                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID);
        try {
            while (phones.moveToNext()) {

                String name = phones.getString(indexName);
                String phoneNumber = phones.getString(indexNumber);
                row_contact_id = phones.getInt(indexRowId);
                no++;
                // UserListGetSet userlist = new UserListGetSet();
                row_contact_id = phones
                        .getInt(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
                String alphaAndDigits = phoneNumber.replaceAll("[^0-9+]", "");
                if (phones != null) {
//					Log.d(TAG, "Cursor size::" + phones.getCount() + "::"
//							+ phones.getPosition());

                }
                Constant.printMsg("siva inside lastsync Mobile No Before:::"
                        + alphaAndDigits);

                try {
                    if (Utils.check_prefix(alphaAndDigits, "00", 2)) {
                        alphaAndDigits = ""
                                + alphaAndDigits.substring(2,
                                alphaAndDigits.length());
                    } else {
                        if (Utils.check_prefix(alphaAndDigits, "0", 1)) {
                            alphaAndDigits = alphaAndDigits.substring(1,
                                    alphaAndDigits.length());
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                Constant.printMsg("siva inside lastsync Mobile No After::"
                        + alphaAndDigits);
                Constant.printMsg("siva inside lastsync Mobile No After countryu code::"
                        + country_code);
                // /
                // dbAdapter.setDeleteContact(alphaAndDigits+KachingMeApplication.getHost());
                Constant.printMsg("siva inside lastsync condition for inserting::"
                        + alphaAndDigits + KachingMeApplication.getHost());
                UserContactDto userDto = new UserContactDto();
                userDto.setuId(String.valueOf(no++));
                userDto.setContactName(name);
                if (alphaAndDigits.startsWith("+")) {
                    userDto.setContactNumbe(""
                            + alphaAndDigits.substring(1,
                            alphaAndDigits.length()));
                } else {
                    userDto.setContactNumbe(country_code + alphaAndDigits);
                }
                contactList.add(userDto);

                // myContactNumber=alphaAndDigits;
                ContactsGetSet contects = new ContactsGetSet();
                contects.setDisplay_name(name);
                contects.setIs_niftychat_user(0);
                if (alphaAndDigits.startsWith("+")) {
                    contects.setJid(""
                            + alphaAndDigits.substring(1,
                            alphaAndDigits.length())
                            + KachingMeApplication.getHost());
                } else {
                    contects.setJid(country_code + alphaAndDigits
                            + KachingMeApplication.getHost());

                }
                contects.setNumber(alphaAndDigits);
                contects.setPhone_label("");
                contects.setRaw_contact_id("" + row_contact_id);
                contects.setPhone_type("");
                contects.setIsInContactList(1);
                contects.setUnseen_msg_count(0);
                Constant.printMsg("siva lastsync........next condition to server"
                        + contects.getJid() + "...just checkl...."
                        + KachingMeApplication.getUserID()
                        + KachingMeApplication.getHost());
                /** here check if there is any contact no as usernumber */
                if (!contects.getJid().equals(
                        KachingMeApplication.getUserID()
                                + KachingMeApplication.getHost())) {
                    if (dbAdapter.getJidIdPresentOrNot(contects.getJid()) == 0) {
                        dbAdapter.insertContacts(contects);
                    } else if (dbAdapter.getIsInContactList(contects.getJid()) == 0) {
                        dbAdapter.updateIsInContactList(contects.getJid(), 1,name);
                    }
                }
                if (phones.isLast()) {
                    Constant.printMsg("loop getting stoped"
                            + jsonFormContact());
                    if (Connectivity.isConnected(this)) {
                        new postcontact().execute();
                    } else {
                        Constant.printMsg("siva test.......");
                        Intent login_broadcast = new Intent("contact_update");
                        getApplicationContext().sendBroadcast(login_broadcast);
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            phones.close();
        }
    }

    public class postcontact extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();
            try {
                result = ht.doPostMobizee(jsonFormContact(),
                        KachingMeConfig.Contact_URL);
                Constant.printMsg("siva result dis comtact.."
                        + KachingMeConfig.Contact_URL + ".." + result);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(final String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null && result.length() > 0) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CommonMethods com = new CommonMethods(getApplicationContext());
                            if (com.isJSONValid(result.trim())) {
                                try {
                                    Gson g = new Gson();
                                    ContactResponseDto contactReponse;
                                    contactReponse = g.fromJson(result.trim(),
                                            ContactResponseDto.class);
                                    if (contactReponse.getContactListDtos() != null
                                            && contactReponse.getContactListDtos().size() > 0) {
                                        for (int i = 0; i < contactReponse
                                                .getContactListDtos().size(); i++) {
                                            String primaryNumber = contactReponse
                                                    .getContactListDtos().get(i)
                                                    .getPrimaryContactNumber();
                                            Constant.printMsg("siva lastsynch contact from name..."
                                                    + primaryNumber);
                                            if (primaryNumber != null) {
                                                /** kaching me User */
                                                Constant.printMsg("siva lastsynch contact from name..."
                                                        + primaryNumber + KachingMeApplication.getHost());
//                                            try {
//                                                vc = VCardManager.getInstanceFor(
//                                                        TempConnectionService.connection).loadVCard(
//                                                        primaryNumber + KachingMeApplication.getHost());
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                            byte[] photo_ts = null;
//                                            if (vc.getAvatar() != null) {
//                                                photo_ts = new AvatarManager()
//                                                        .saveBitemap(vc.getAvatar());
//                                            }
//                                            /**calling php for sync status*/
//                                            com.syncDataForStatus(primaryNumber, photo_ts, null);
                                                String status = contactReponse.getContactListDtos().get(i).getStatus();
                                                String photo = contactReponse.getContactListDtos().get(i).getProfilePhoto();
                                                String email = contactReponse.getContactListDtos().get(i).getEmailId();

                                                if (dbAdapter.getJidIdPresentOrNot(primaryNumber +
                                                        KachingMeApplication.getHost()) != 0) {
                                                    dbAdapter.updateInsertedContacts(
                                                            primaryNumber + KachingMeApplication.getHost(),
                                                            primaryNumber, photo, status,email);
                                                    if (photo!=null && !photo.isEmpty()){
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                            com.new getImagefromUrlAsy().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                                                    primaryNumber , photo, status, null);
                                                        else
                                                            com.new getImagefromUrlAsy().execute(primaryNumber , photo, status, null);
                                                    }
                                                } else {
                                                    List<String> secondaryList = contactReponse
                                                            .getContactListDtos().get(i).getSecondaryContactNumbers();
                                                    for (int j = 0; j < secondaryList.size(); j++) {
                                                        Constant.printMsg("siva laostsynch cntact from name..."
                                                                + secondaryList.get(j)
                                                                + KachingMeApplication.getHost());
                                                        if (dbAdapter.getJidIdPresentOrNot(secondaryList.get(j) +
                                                                KachingMeApplication.getHost()) != 0) {
                                                            dbAdapter.updateInsertedContacts(
                                                                    secondaryList.get(j) + KachingMeApplication.getHost(),
                                                                    secondaryList.get(j), photo, status,email);
                                                            if (photo!=null && !photo.isEmpty()){
                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                                    com.new getImagefromUrlAsy().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                                                            secondaryList.get(j) , photo, status, null);
                                                                else
                                                                    com.new getImagefromUrlAsy().execute(secondaryList.get(j) , photo, status, null);
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                            } else {
                                                /** not a kaching me User */
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }
                            }
                        } finally {
                            Constant.printMsg("siva test.......");
                            Intent login_broadcast = new Intent("contact_update");
                            getApplicationContext().sendBroadcast(login_broadcast);
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Message m = new Message();
//                                Bundle b = new Bundle();
//                                b.putInt("what", 1);
//                                m.setData(b);
//                                FavouriteContacts.handlerNew.sendMessage(m);
//                            FavouriteContacts.loadContactList();
//                            }
//                        });
                        }
                    }
                });
                thread.start();

            } else {
                // Toast.makeText(getApplicationContext(),
                // "Network Error!Please try again later!",
                // Toast.LENGTH_SHORT).show();
            }
        }
    }
}
