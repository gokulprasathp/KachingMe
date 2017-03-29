package com.wifin.kachingme.chat.chat_common_classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import a_vcard.android.syncml.pim.PropertyNode;
import a_vcard.android.syncml.pim.VDataBuilder;
import a_vcard.android.syncml.pim.VNode;
import a_vcard.android.syncml.pim.vcard.VCardException;
import a_vcard.android.syncml.pim.vcard.VCardParser;
import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.SherlockBaseActivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;

public class ContactView_Normal extends SherlockBaseActivity {

    String vcard;
    ImageView img_avatar;
    TextView txt_name;
    LinearLayout ll_phone;
    byte[] byte_av = null;
    Button btn_add_to_contact;
    Boolean status;
    ArrayList<String> phone = new ArrayList<String>();
    static String TAG = ContactView.class.getSimpleName();
    ArrayList<String> mList = new ArrayList<String>();
    String mContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_view);

        img_avatar = (ImageView) findViewById(R.id.img_avatar);
        txt_name = (TextView) findViewById(R.id.txt_name);
        ll_phone = (LinearLayout) findViewById(R.id.ll_phone);

        btn_add_to_contact = (Button) findViewById(R.id.btn_add_to_contact);

        Constant.typeFace(this,txt_name);
        Constant.typeFace(this,btn_add_to_contact);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            vcard = bundle.getString("vcard");
            status = bundle.getBoolean("status");
            if (status) {
                btn_add_to_contact.setVisibility(View.VISIBLE);
            } else {
                btn_add_to_contact.setVisibility(View.GONE);
            }
            Log.d("vcard", "Vcard::" + vcard);

        }

      /*  VCardParser parser = new VCardParser();
        VDataBuilder builder = new VDataBuilder();

        // parse the string

        try {
            boolean parsed = parser.parse(vcard, "UTF-8", builder);
        } catch (VCardException e) {// ACRA.getErrorReporter().handleException(e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {// ACRA.getErrorReporter().handleException(e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        Cursor phones = getContentResolver().query(
                Phone.CONTENT_URI, null, null,
                null, null);
        while (phones.moveToNext()) {
            String name = phones
                    .getString(phones
                            .getColumnIndex(Phone.DISPLAY_NAME));
            String phoneNumber = phones
                    .getString(phones
                            .getColumnIndex(Phone.NUMBER));
            mList.add(phoneNumber);
            Constant.printMsg("list of contacts " + mList);
            // Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG)
            // .show();

        }
        phones.close();


        String contactName = vcard.split(",")[0];
        String contactPhone = vcard.split(",")[1];


        if (contactName!=null) {
            txt_name.setText(contactName);
            // we have the name now
            // break;
        }

        if (contactPhone!=null) {
            Log.d("contact_view", "Contact No::" + contactPhone);
            TextView txt_phone = new TextView(this);
            txt_phone.setTextSize(19);
            txt_phone.setText("TEL" + ": " + contactPhone);
            ll_phone.addView(txt_phone);
            phone.add(contactPhone);

            Constant.printMsg("print   " + contactPhone + "   "
                    + contactPhone);
            mContact = contactPhone;

            // break;
        }


     /*   // get all parsed contacts
        List<VNode> pimContacts = builder.vNodeList;

        // do something for all the contacts
        for (VNode contact : pimContacts) {
            ArrayList<PropertyNode> props = contact.propList;

            // contact name - FN property
            String name = null;
            for (PropertyNode prop : props) {

                // Log.d("vcard",prop.propName+"::"+prop.propValue);
                if ("FN".equals(prop.propName)) {
                    name = prop.propValue;
                    txt_name.setText(name);
                    // we have the name now
                    // break;
                } else if ("PHOTO".equals(prop.propName)) {

                    byte_av = Base64.decode((prop.propValue).toString(), 0);
                    Bitmap avatar = BitmapFactory.decodeByteArray(byte_av, 0,
                            byte_av.length);
                    img_avatar.setImageBitmap(avatar);
                    // break;
                } else if ("TEL".equals(prop.propName)) {
                    Log.d("contact_view", "Contact No::" + prop.propValue);
                    TextView txt_phone = new TextView(this);
                    txt_phone.setTextSize(19);
                    txt_phone.setText(prop.propName + ": " + prop.propValue);
                    ll_phone.addView(txt_phone);
                    phone.add(prop.propValue);

                    Constant.printMsg("print   " + prop.propName + "   "
                            + prop.propValue);
                    mContact = prop.propValue;

                    // break;
                } else if (prop.propName != null && prop.propValue != null) {
                    if (TextUtils.isDigitsOnly(prop.propValue)) {
                        Log.d(TAG + "last ", "Name::" + prop.propName
                                + " Value::" + prop.propValue);
                        TextView txt_phone = new TextView(this);
                        txt_phone.setTextSize(19);
                        txt_phone
                                .setText(prop.propName + ": " + prop.propValue);
                        ll_phone.addView(txt_phone);
                        phone.add(prop.propValue);
                        Constant.printMsg("print1   " + prop.propName
                                + "   " + prop.propValue);
                        mContact = prop.propValue;

                    }
                    // break;
                }
            }
            // Constant.printMsg("Found contact: " + name);
        }
*/
        btn_add_to_contact.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

                int rawContactID = ops.size();

                // Adding insert operation to operations list
                // to insert a new raw contact in the table
                // ContactsContract.RawContacts

                if (mList.contains(mContact)) {
                    Toast.makeText(getApplicationContext(), "Exist",
                            Toast.LENGTH_LONG).show();
                } else {

                    ops.add(ContentProviderOperation
                            .newInsert(RawContacts.CONTENT_URI)
                            .withValue(
                                    RawContacts.ACCOUNT_TYPE,
                                    null)
                            .withValue(RawContacts.ACCOUNT_NAME, null).build());

                    // Adding insert operation to operations list
                    // to insert display name in the table ContactsContract.Data
                    Constant.printMsg("Contact name :::: "
                            + StructuredName.DISPLAY_NAME + "   "
                            + txt_name.getText() + "   " + ops.size() + "   ");

                    ops.add(ContentProviderOperation
                            .newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(
                                    ContactsContract.Data.RAW_CONTACT_ID,
                                    rawContactID)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                    StructuredName.CONTENT_ITEM_TYPE)
                            .withValue(StructuredName.DISPLAY_NAME,
                                    txt_name.getText()).build());

                    // Adding insert operation to operations list
                    // to insert Mobile Number in the table
                    // ContactsContract.Data
                    for (int i = 0; i < phone.size(); i++) {

                        Constant.printMsg("Contact name :::: "
                                + phone.get(i));
                        ops.add(ContentProviderOperation
                                .newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(
                                        ContactsContract.Data.RAW_CONTACT_ID,
                                        rawContactID)
                                .withValue(ContactsContract.Data.MIMETYPE,
                                        Phone.CONTENT_ITEM_TYPE)
                                .withValue(Phone.NUMBER, phone.get(i))
                                .withValue(Phone.TYPE,
                                        Phone.TYPE_MOBILE)
                                .build());
                    }

                    if (byte_av != null) { // If an image is selected
                        // successfully

                        // Adding insert operation to operations list
                        // to insert Photo in the table ContactsContract.Data
                        ops.add(ContentProviderOperation
                                .newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(
                                        ContactsContract.Data.RAW_CONTACT_ID,
                                        rawContactID)
                                .withValue(
                                        ContactsContract.Data.IS_SUPER_PRIMARY,
                                        1)
                                .withValue(ContactsContract.Data.MIMETYPE,
                                        Photo.CONTENT_ITEM_TYPE)
                                .withValue(
                                        Photo.PHOTO,
                                        byte_av).build());

                    }
                    try {
                        // Executing all the insert operations as a single
                        // database
                        // transaction
                        getContentResolver().applyBatch(
                                ContactsContract.AUTHORITY, ops);
                        Toast.makeText(getBaseContext(),
                                "Contact is successfully added",
                                Toast.LENGTH_SHORT).show();
                    } catch (RemoteException e) {// ACRA.getErrorReporter().handleException(e);
                        e.printStackTrace();
                    } catch (OperationApplicationException e) {// ACRA.getErrorReporter().handleException(e);
                        e.printStackTrace();
                    }

                    finish();
                }
            }
        });

    }

}
