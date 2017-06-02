package com.wifin.kachingme.chat.chat_common_classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import org.acra.ACRA;

import a_vcard.android.syncml.pim.PropertyNode;
import a_vcard.android.syncml.pim.VDataBuilder;
import a_vcard.android.syncml.pim.VNode;
import a_vcard.android.syncml.pim.vcard.VCardException;
import a_vcard.android.syncml.pim.vcard.VCardParser;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.SherlockBaseActivity;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;

public class SendContact extends SherlockBaseActivity {

	String vcard, email_id;
	ImageView img_avatar;
	TextView txt_name, email_id_name;
	LinearLayout ll_phone;
	byte[] byte_av;
	Button btn_send, btn_cancel;
	SharedPreferences sharedPrefs;
	int Count = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_contact);
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		img_avatar = (ImageView) findViewById(R.id.img_avatar);
		txt_name = (TextView) findViewById(R.id.txt_name);
		ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		email_id_name = (TextView) findViewById(R.id.email_id_name);

        Constant.typeFace(this,txt_name);
        Constant.typeFace(this,btn_send);
        Constant.typeFace(this,btn_cancel);
        Constant.typeFace(this,email_id_name);


        Bundle bundle = getIntent().getExtras();
		email_id_name.setText("");
		if (bundle != null) {
			vcard = bundle.getString("vcard");
			email_id = bundle.getString("email_id");
			Constant.printMsg("] vcard::" + email_id);
			if (email_id != null && email_id.length() > 0) {
				Constant.printMsg("if vcard::");
				email_id_name.setVisibility(View.VISIBLE);
				email_id_name.setText("Email id :" + email_id);

			} else {
				Constant.printMsg("elseeeee vcard::");
				email_id_name.setVisibility(View.GONE);
			}
			Log.d("vcard", "Vcard::" + vcard + "email_id:" + email_id);

		}

		VCardParser parser = new VCardParser();
		VDataBuilder builder = new VDataBuilder();

		// parse the string

		try {
			boolean parsed = parser.parse(vcard, "UTF-8", builder);
		} catch (VCardException e) {//
//			ACRA.getErrorReporter().handleException(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {//
//			ACRA.getErrorReporter().handleException(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// get all parsed contacts
		List<VNode> pimContacts = builder.vNodeList;

		// do something for all the contacts
		for (VNode contact : pimContacts) {
			ArrayList<PropertyNode> props = contact.propList;

			// contact name - FN property
			String name = null;
			for (PropertyNode prop : props) {

				Log.d("vcard", prop.propName + "::" + prop.propValue);
				if ("FN".equals(prop.propName)) {
					name = prop.propValue;
					txt_name.setText(name);
					// we have the name now
					// break;
				}
				if ("PHOTO".equals(prop.propName)) {

					byte_av = Base64.decode((prop.propValue).toString(), 0);
					Bitmap avatar = BitmapFactory.decodeByteArray(byte_av, 0,
							byte_av.length);
					img_avatar.setImageBitmap(avatar);
				}
				if ("TEL".equals(prop.propName)) {
					TextView txt_phone = new TextView(this);
					txt_phone.setTextSize(19);
					txt_phone.setText(prop.propName + ": " + prop.propValue);
					ll_phone.addView(txt_phone);
				} else if (prop.propName != null && prop.propValue != null) {
					if (TextUtils.isDigitsOnly(prop.propValue)) {

						TextView txt_phone = new TextView(this);
						txt_phone.setTextSize(19);
						txt_phone
								.setText(prop.propName + ": " + prop.propValue);
						ll_phone.addView(txt_phone);

					}
					// break;
				}

				if (email_id != null) {
					email_id_name.setVisibility(View.VISIBLE);
					email_id_name.setText("Email id :" + email_id);

				} else {
					email_id_name.setVisibility(View.GONE);
				}

			}

			// similarly for other properties (N, ORG, TEL, etc)
			// ...

			// Constant.printMsg("Found contact: " + name);
		}

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();
			}
		});

		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Constant.bux = sharedPrefs.getLong("buxvalue", 0);

				Long buxval = Constant.bux + Constant.contactpoints;
				Constant.bux = buxval;

				Constant.totalcontact = Count + Constant.totalcontact;

				Editor e = sharedPrefs.edit();
				e.putLong("buxvalue", buxval);
				e.commit();

				Intent intentMessage = new Intent();
				intentMessage.putExtra("contact_thumb", byte_av);
				intentMessage.putExtra("vcard", vcard);

				if (email_id != null && email_id.length() > 0) {
					Constant.printMsg("if vcard::");

					intentMessage.putExtra("name", txt_name.getText()
							.toString() + " \n " + email_id);

				} else {
					Constant.printMsg("elseeeee vcard::");
					intentMessage.putExtra("name", txt_name.getText()
							.toString());

				}

				// Set The Result in Intents

				setResult(RESULT_OK, intentMessage);
				// finish The activity
				finish();
			}
		});
	}

}
