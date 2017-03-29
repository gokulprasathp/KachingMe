package com.wifin.kachingme.deals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.wifin.kachingme.chat_home.MainActivity;
import com.wifin.kachingme.adaptors.StoreAdapter;
import com.wifin.kachingme.pojo.DicDto;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kaching.me.ui.R;

public class StoreActivity extends MainActivity {

	public static Context context;
	ListView summary;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
		View.inflate(this, R.layout.activity_store, vg);
		// setContentView(R.layout.activity_store);

		summary = (ListView) findViewById(R.id.store_list);
		logo.setVisibility(ImageView.GONE);
		back.setVisibility(ImageView.VISIBLE);
		cart.setVisibility(ImageView.INVISIBLE);
		head.setText("Deel");
		context = StoreActivity.this;
		JsonForm();

		Constant.printMsg("Constant.menuId" + Constant.menuId);

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(StoreActivity.this, DealsActivity.class);
				startActivity(i);
				finish();

			}
		});

	}

	private void JsonForm() {
		// TODO Auto-generated method stub

		if (Connectivity.isConnected(StoreActivity.this)) {

			new postWord().execute();

		}

	}

	public class postWord extends AsyncTask<String, String, String> {
		ProgressDialog progressDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub

			super.onPreExecute();

			progressDialog = new ProgressDialog(StoreActivity.this,
					AlertDialog.THEME_HOLO_LIGHT);
			progressDialog.setMessage("Please Wait...");
			progressDialog.setProgressDrawable(new ColorDrawable(
					android.graphics.Color.BLUE));
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String result = null;
			HttpConfig ht = new HttpConfig();
			Constant.printMsg("result dis" + KachingMeConfig.Dic_URL
					+ Constant.menuId);
			result = ht.httpget(KachingMeConfig.Dic_URL + Constant.menuId);

			Constant.printMsg("result dis" + KachingMeConfig.Dic_URL
					+ Constant.menuId + "response:::" + result);

			Constant.printMsg("the dic values::::" + result);

			Constant.printMsg("result dis" + result);
			return result;
		}

		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();

			if (result != null && result.length() > 0) {

				Constant.dictlist.clear();
				JSONArray jarray;
				try {
					jarray = new JSONArray(result);
					for (int i = 0; i < jarray.length(); i++) {
						JSONObject jObject = jarray.getJSONObject(i);

						DicDto m = new DicDto();
						m.setMerchantId(jObject.getString("merchantId"));
						m.setWebsite(jObject.getString("website"));
						m.setFirstName(jObject.getString("firstName"));
						m.setCompanyLogoPath(jObject
								.getString("companyLogoPath"));
						m.setDeelCounts(jObject.getString("deelCounts"));

						Constant.printMsg("list Dtyo::" + m.getWebsite());

						Constant.dictlist.add(m);

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (Constant.dictlist.size() > 0) {

					summary.setAdapter(new StoreAdapter(StoreActivity.this,
							Constant.dictlist));
				} else {

					AlertDialog.Builder builder1 = new AlertDialog.Builder(
							StoreActivity.this);
					builder1.setMessage("Sorry No Deals Near By Your Location");
					builder1.setCancelable(true);
					builder1.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									final Dialog nagDialog = new Dialog(
											StoreActivity.this);
									nagDialog
											.requestWindowFeature(Window.FEATURE_NO_TITLE);
									nagDialog
											.getWindow()
											.setBackgroundDrawable(
													new ColorDrawable(
															android.graphics.Color.TRANSPARENT));

									nagDialog.setCancelable(true);

									Intent i = new Intent(StoreActivity.this,
											DealsActivity.class);
									startActivity(i);
									finish();

								}
							});

					AlertDialog alert11 = builder1.create();
					alert11.show();

				}

			} else {
				Toast.makeText(getApplicationContext(),
						"Network Error!Please try again later!",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub

		Intent i = new Intent(StoreActivity.this, DealsActivity.class);
		startActivity(i);
		finish();
	}

	public static Context getContext() {
		// TODO Auto-generated method stub
		return context;
	}

}
