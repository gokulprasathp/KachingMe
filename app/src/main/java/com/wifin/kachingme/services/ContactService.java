package com.wifin.kachingme.services;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;

import com.google.gson.Gson;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.ContactPost;
import com.wifin.kachingme.pojo.ContactResponseDto;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.UserContactDto;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.Utils;
import com.wifin.kachingme.util.KachingMeConfig;

import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.ArrayList;
import java.util.List;

import a_vcard.android.util.Log;

public class ContactService extends Service {

    String phone;
    ArrayList<UserContactDto> conlist = new ArrayList<UserContactDto>();
    Dbhelper db;
    DatabaseHelper dbAdapter;
    String country_code;
    // AbstractXMPPConnection connection;
    VCard vc = new VCard();
    private int mContactCount;
//    Activity activity;
//
//    public ContactService(Activity activity) {
//        this.activity = activity;
//    }

    private ContentObserver mObserver = new ContentObserver(new Handler()) {

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            final int currentCount = getContactCount();

            Constant.printMsg("siva onchange check current count contact service"
                    + currentCount);
            country_code = KachingMeApplication.getsharedpreferences().getString(
                    Constant.COUNTRY_CODE_LABEL, "");
            if (currentCount < mContactCount) {
                /** CONTACT DELETED.. */
                Constant.printMsg("siva onchange contact service inisde delete"
                        + currentCount + "....." + mContactCount);
                deleteAddedContact();
            } else {
                if (currentCount == mContactCount) {
                    /** CONTACT UPDATED */
                    Constant.printMsg("siva onchange contact service inisde UPDATED"
                            + currentCount);
                    UpdateAddedContact();
                } else {
                    if (currentCount > mContactCount) {
                        /** NEW CONTACT. */
                        Constant.printMsg("siva onchange contact service inisde New Contact"
                                + currentCount);
                        createdNewContact();
                    }
                }
            }
            mContactCount = currentCount;
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContactCount = getContactCount();
        dbAdapter = KachingMeApplication.getDatabaseAdapter();
        db = new Dbhelper(getApplicationContext());
        Log.d("Contact Service", mContactCount + "");
        Constant.printMsg("siva ContactService");
        this.getContentResolver().registerContentObserver(
                ContactsContract.Contacts.CONTENT_URI, true, mObserver);
    }

    private int getContactCount() {
        Constant.printMsg("siva ContactService get count");
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI, null, null, null,
                    null);
            if (cursor != null) {
                return cursor.getCount();
            } else {
                return 0;
            }
        } catch (Exception ignore) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    protected void UpdateAddedContact() {
        // TODO Auto-generated method stub
        conlist.clear();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        // String[] projection = new String[] {
        // ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        // ContactsContract.CommonDataKinds.Phone.NUMBER };

        Cursor people = getContentResolver().query(uri, null, null, null, null);
        try {

            int indexName = people
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int indexNumber = people
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int count = 0;
            int indexRowId = people
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID);
            // people.moveToFirst();
            boolean numberEdit = false;
            while (people.moveToNext()) {
                count++;
                String name = people.getString(indexName).trim();
                String number = people.getString(indexNumber).trim();
                int row_contact_id = people.getInt(indexRowId);
                // if (number.length() > 9) {

                UserContactDto cd = new UserContactDto();
                String alphaAndDigits = number.replaceAll("[^0-9+]", "");
                // String num = number.replaceAll("\\s+", "");
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

                // String strName = name.replaceAll("\\s+", "");
                if (alphaAndDigits.startsWith("+")) {
                    cd.setContactNumbe(""
                            + alphaAndDigits.substring(1,
                            alphaAndDigits.length()));
                } else {
                    cd.setContactNumbe(country_code + alphaAndDigits);
                }
                cd.setContactName(name);
                cd.setuId(String.valueOf(count));
                conlist.add(cd);

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
                // Constant.printMsg("siva last duty:name.. present or not");
                // Constant.printMsg("siva last duty:name..123..." + strName
                // +"...db name..."+
                // dbAdapter.getcontactName(contects.getJid()));

                if (!contects.getJid().equals(
                        KachingMeApplication.getUserID()
                                + KachingMeApplication.getHost())) {
                    if (dbAdapter.getJidIdPresentOrNot(contects.getJid()) == 0) {
                        numberEdit = true;
                        dbAdapter.insertContacts(contects);
                        deleteAddedContact();
                    } else if (dbAdapter.getIsInContactList(contects.getJid()) == 0) {
                        dbAdapter.updateIsInContactList(contects.getJid(), 1,name);
                    }
                    // Constant.printMsg("siva last duty:name.." + strName
                    // +"...db name..."+
                    // dbAdapter.getcontactName(contects.getJid()));
                    if (dbAdapter.getcontactName(contects.getJid()) != null
                            && !dbAdapter.getcontactName(contects.getJid())
                            .equalsIgnoreCase(name)) {
                        dbAdapter.contactNameUpdate(contects.getJid(), name);
                        Constant.printMsg("siva inside the name edit......"
                                + name);
                    }
                }
                if (people.isLast() && numberEdit) {
                    Constant.contactlist.addAll(conlist);
                    if (Connectivity.isConnected(this)){
                        new postcontact().execute();
                    }else{
                        Constant.printMsg("siva test.......");
                        Intent login_broadcast = new Intent("contact_update");
                        getApplicationContext().sendBroadcast(login_broadcast);
                    }
                }
            }

            // data1 = jsonFormContact();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            people.close();

        }
    }

    protected void createdNewContact() {
        // TODO Auto-generated method stub
        conlist.clear();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        // String[] projection = new String[] {
        // ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        // ContactsContract.CommonDataKinds.Phone.NUMBER };
        Cursor people = getContentResolver().query(uri, null, null, null, null);
        try {

            int indexName = people
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int indexNumber = people
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int indexRowId = people
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID);
            int count = 0;

            while (people.moveToNext()) {
                // int row_contact_id = people
                // .getInt(people
                // .getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
                count++;
                // String name = people.getString(indexName).trim();
                // String number = people.getString(indexNumber).trim();
                String name = people.getString(indexName);
                String number = people.getString(indexNumber);
                int row_contact_id = people.getInt(indexRowId);
                // UserListGetSet userlist = new UserListGetSet();
                Constant.printMsg("siva onchange contact get particular contact name........." + name);
                Constant.printMsg("siva onchange contact get particular contact number........." + number);
                // if (number.length() > 9) {

                UserContactDto cd = new UserContactDto();
                String alphaAndDigits = number.replaceAll("[^0-9+]", "");
                // String num = number.replaceAll("\\s+", "");
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

                String strName = name.replaceAll("\\s+", "");
                if (alphaAndDigits.startsWith("+")) {
                    cd.setContactNumbe(""
                            + alphaAndDigits.substring(1,
                            alphaAndDigits.length()));
                } else {
                    cd.setContactNumbe(country_code + alphaAndDigits);
                }
                cd.setContactName(strName);
                cd.setuId(String.valueOf(count));
                conlist.add(cd);

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
                Constant.printMsg("siva conatct list:::name.." + strName
                        + "...number..." + alphaAndDigits
                        + "..number length...  " + number.length());
                if (!contects.getJid().equals(
                        KachingMeApplication.getUserID()
                                + KachingMeApplication.getHost())) {
                    if (dbAdapter.getJidIdPresentOrNot(contects.getJid()) == 0) {
                        Constant.printMsg("siva onchange contact inserted particular contact name........." + name);
                        dbAdapter.insertContacts(contects);
                    } else if (dbAdapter.getIsInContactList(contects.getJid()) == 0) {
                        Constant.printMsg("siva onchange contact update in insert particular contact name........." + name);
                        dbAdapter.updateIsInContactList(contects.getJid(), 1,name);
                    } else {
                        /** need to check diaplay name here */
                        // if (condition) {
                        // dbAdapter.updateInsertedContacts(jid, primaryNumber);
                        // }
                    }
                }
            }
            Constant.contactlist.addAll(conlist);
            // data1 = jsonFormContact();
            if (Connectivity.isConnected(this)){
                new postcontact().execute();
            }else{
                Constant.printMsg("siva test.......");
                Intent login_broadcast = new Intent("contact_update");
                getApplicationContext().sendBroadcast(login_broadcast);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            people.close();
        }
    }

    protected void deleteAddedContact() {
        // TODO Auto-generated method stub
        ArrayList<UserContactDto> contactDeletelist = new ArrayList<UserContactDto>();
        Constant.printMsg("siva onchange contact service inisde delete enter");
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        // String[] projection = new String[] {
        // ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        // ContactsContract.CommonDataKinds.Phone.NUMBER };

        Cursor people = getContentResolver().query(uri, null, null, null, null);
        try {

            int indexName = people
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int indexNumber = people
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int count = 0;
            // int row_contact_id = people
            // .getInt(people
            // .getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
            while (people.moveToNext()) {
                count++;
                String name = people.getString(indexName).trim();
                String number = people.getString(indexNumber).trim();

                UserContactDto cd = new UserContactDto();
                String alphaAndDigits = number.replaceAll("[^0-9+]", "");
                // String num = number.replaceAll("\\s+", "");
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
                Constant.printMsg("siva onchange contact service inisde delete enter 1"
                        + number);

                String strName = name.replaceAll("\\s+", "");
                if (alphaAndDigits.startsWith("+")) {
                    cd.setContactNumbe(""
                            + alphaAndDigits.substring(1,
                            alphaAndDigits.length()));
                } else {
                    cd.setContactNumbe(country_code + alphaAndDigits);
                }
                cd.setContactName(strName);
                if (alphaAndDigits.startsWith("+")) {
                    cd.setuId(""
                            + alphaAndDigits.substring(1,
                            alphaAndDigits.length())
                            + KachingMeApplication.getHost());
                } else {
                    cd.setuId(country_code + alphaAndDigits
                            + KachingMeApplication.getHost());
                }
                contactDeletelist.add(cd);
                Constant.printMsg("siva conatct list:::name.." + strName
                        + "...number..." + alphaAndDigits
                        + "...number length...  " + number.length());
                if (people.isLast()) {
                    Constant.printMsg("siva onchange loop getting stoped in delete");
                    dbAdapter.checkDbForRemoveContact(contactDeletelist);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            people.close();
            Constant.printMsg("siva test.......");
            Intent login_broadcast = new Intent("contact_update");
            getApplicationContext().sendBroadcast(login_broadcast);

//            Message m = new Message();
//            Bundle b = new Bundle();
//            b.putInt("what", 1);
//            m.setData(b);
//            FavouriteContacts.handlerNew.sendMessage(m);
        }
    }

    public String jsonFormContact() {
        String d = null;
        phone = KachingMeApplication.getjid().split("@")[0];
        Constant.printMsg("siva post ta nuber...." + phone);
        ContactPost l = new ContactPost();
        l.setPhoneNumber(phone);
        l.setUserContactDtos(conlist);
        d = new Gson().toJson(l);
        Constant.printMsg("siva post  ::::" + d.toString());
        return d;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    // public void insertDBPrimaryNumber(ContentValues v) {
    //
    // try {
    // int a = (int) db.open().getDatabaseObj()
    // .insert(Dbhelper.TABLE_PRIMENUMBER, null, v);
    //
    // Constant.printMsg("No of inserted rows in primary details :::::::::"
    // + a);
    // } catch (SQLException e) {
    // Constant.printMsg("Sql exception in new primary details ::::::"
    // + e.toString());
    // } finally {
    // db.close();
    // }
    //
    // }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mObserver);
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
                Constant.printMsg("siva post url result dis comtactbefore"
                        + KachingMeConfig.Contact_URL);
                result = ht.doPostMobizee(jsonFormContact(),
                        KachingMeConfig.Contact_URL);
                Constant.printMsg("siva result dis comtact" + result);

            } catch (Exception e) {
                // TODO: handle exception
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

//                            activity.runOnUiThread(new Runnable() {
//
//                                @Override
//                                public void run() {
////                                Message m = new Message();
////                                Bundle b = new Bundle();
////                                b.putInt("what", 1);
////                                m.setData(b);
////                                FavouriteContacts.handlerNew.sendMessage(m);
//                                    FavouriteContacts.loadContactList();
//                                }
//                            });
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
