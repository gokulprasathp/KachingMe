package com.wifin.kachingme.util;

import org.apache.http.Header;
//import cz.msebera.android.httpclient.Header;
import a_vcard.android.util.Log;
import android.content.Context;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.loopj.android.http.*;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;
//import com.loopj.android.http.SyncHttpClient;


public class SendWeb {

//	public static void Add_Message_on_web(Context context, MessageGetSet message) {
//
//		final String TAG = SendWeb.class.getSimpleName();
//		AsyncHttpClient client = new AsyncHttpClient();
//
//		RequestParams perams = new RequestParams();
//		perams.put("Waction", "insert");
//		perams.put("Wtable", "tbl_otochat");
//		perams.put("mid", "'" + message.getKey_id() + "'");
//		perams.put("body", "'" + message.getData() + "'");
//		perams.put("mtype", "'" + message.getMedia_wa_type() + "'");
//		perams.put("fromjid", "'" + KachingMeApplication.getjid() + "'");
//		perams.put("tojid", "'" + message.getKey_remote_jid() + "'");
//		// perams.put("etime","'"+txt_name.getText().toString()+"'");
//		// perams.put("rtime","'"+txt_name.getText().toString()+"'");
//		perams.put("status", 0);
//
//		perams.put("chattype", message.getIs_sec_chat());
//
//		perams.put("stime", "FROM_UNIXTIME(" + message.getTimestamp() / 1000
//				+ ")");
//		perams.put(
//				"lat",
//				KachingMeApplication.getsharedpreferences().getString(
//						Constant.CURRENT_LAT, "0"));
//		perams.put(
//				"lon",
//				KachingMeApplication.getsharedpreferences().getString(
//						Constant.CURRENT_LOG, "0"));
//
//		if (message.getMedia_wa_type().equals("4")) {
//			perams.put("body",
//					"'" + message.getLatitude() + "," + message.getLongitude()
//							+ "'");
//		}
//
//		client.post(KachingMeConfig.USER_UPDATE_PHP,
//				perams, new AsyncHttpResponseHandler() {
//
//					@Override
//					public void onFinish() {
//
//						super.onFinish();
//
//					}
//
//					@Override
//					public void onStart() {
//						// called before request is started
//
//					}
//
//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							byte[] response) {
//						Log.d(TAG, "Inseret Message successfully::"
//								+ new String(response));
//
//					}
//
//					@Override
//					public void onFailure(int statusCode, Header[] headers,
//							byte[] errorResponse, Throwable e) {
//						// called when response HTTP status is "4XX" (eg. 401,
//						// 403, 404)
//					}
//
//					@Override
//					public void onRetry(int retryNo) {
//						// called when request is retried
//					}
//				});
//
//	}

//	public static void Update_Message_on_web(Context context, String mid,
//			int status, String rtime) {
//
//		final String TAG = SendWeb.class.getSimpleName();
//		SyncHttpClient client = new SyncHttpClient();
//
//		RequestParams perams = new RequestParams();
//		perams.put("Waction", "update");
//		perams.put("Wtable", "tbl_otochat");
//		perams.put("Wcolumn", "mid");
//		perams.put("Wvalue", "'" + mid + "'");
//		perams.put("status", status);
//		perams.put("rtime", "FROM_UNIXTIME(" + rtime + ")");
//
//		client.post(KachingMeConfig.USER_UPDATE_PHP,
//				perams, new AsyncHttpResponseHandler() {
//
//					@Override
//					public void onFinish() {
//
//						super.onFinish();
//
//					}
//
//					@Override
//					public void onStart() {
//						// called before request is started
//
//					}
//
//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							byte[] response) {
//						Log.d(TAG, "Inseret Message successfully::"
//								+ new String(response));
//
//					}
//
//					@Override
//					public void onFailure(int statusCode, Header[] headers,
//							byte[] errorResponse, Throwable e) {
//						// called when response HTTP status is "4XX" (eg. 401,
//						// 403, 404)
//					}
//
//					@Override
//					public void onRetry(int retryNo) {
//						// called when request is retried
//					}
//				});
//
//	}

//	public static void Update_Message_on_web_async(Context context, String mid,
//			int status, String rtime) {
//
//		final String TAG = SendWeb.class.getSimpleName();
//		AsyncHttpClient client = new AsyncHttpClient();
//
//		RequestParams perams = new RequestParams();
//		perams.put("Waction", "update");
//		perams.put("Wtable", "tbl_otochat");
//		perams.put("Wcolumn", "mid");
//		perams.put("Wvalue", "'" + mid + "'");
//		perams.put("status", status);
//		perams.put("rtime", "FROM_UNIXTIME(" + rtime + ")");
//
//		client.post(KachingMeConfig.USER_UPDATE_PHP,
//				perams, new AsyncHttpResponseHandler() {
//
//					@Override
//					public void onFinish() {
//
//						super.onFinish();
//
//					}
//
//					@Override
//					public void onStart() {
//						// called before request is started
//
//					}
//
//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							byte[] response) {
//						Log.d(TAG, "Inseret Message successfully::"
//								+ new String(response));
//
//					}
//
//					@Override
//					public void onFailure(int statusCode, Header[] headers,
//							byte[] errorResponse, Throwable e) {
//						// called when response HTTP status is "4XX" (eg. 401,
//						// 403, 404)
//					}
//
//					@Override
//					public void onRetry(int retryNo) {
//						// called when request is retried
//					}
//				});
//
//	}
//
//	public static void Update_Status_web_async(Context context, String jid,
//			String status) {
//
//		final String TAG = SendWeb.class.getSimpleName();
//		AsyncHttpClient client = new AsyncHttpClient();
//
//		RequestParams perams = new RequestParams();
//		perams.put("Waction", "update");
//		perams.put("Wtable", "tbl_user");
//		perams.put("Wcolumn", "jid");
//		perams.put("Wvalue", "'" + jid + "'");
//		perams.put("status", "'" + status + "'");
//
//		client.post(KachingMeConfig.USER_UPDATE_PHP,
//				perams, new AsyncHttpResponseHandler() {
//
//					@Override
//					public void onFinish() {
//
//						super.onFinish();
//
//					}
//
//					@Override
//					public void onStart() {
//						// called before request is started
//
//					}
//
//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							byte[] response) {
//						Log.d(TAG, "Inseret Message successfully::"
//								+ new String(response));
//
//					}
//
//					@Override
//					public void onFailure(int statusCode, Header[] headers,
//							byte[] errorResponse, Throwable e) {
//						// called when response HTTP status is "4XX" (eg. 401,
//						// 403, 404)
//					}
//
//					@Override
//					public void onRetry(int retryNo) {
//						// called when request is retried
//					}
//				});
//
//	}

	/*public static void Update_Profile_web_async(Context context, String jid,
			String name, String email, byte[] image) {

		final String TAG = SendWeb.class.getSimpleName();
		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams perams = new RequestParams();
		perams.put("Waction", "update");
		perams.put("Wtable", "tbl_user");
		perams.put("Wcolumn", "jid");
		perams.put("Wvalue", "'" + jid + "'");
		perams.put("name", "'" + name + "'");
		perams.put("email", "'" + email + "'");
		perams.put("avatar", image);

		client.post(KachingMeConfig.USER_UPDATE_PHP,
				perams, new AsyncHttpResponseHandler() {

					@Override
					public void onFinish() {

						super.onFinish();

					}

					@Override
					public void onStart() {
						// called before request is started

					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] response) {
						Log.d(TAG, "Inseret Message successfully::"
								+ new String(response));

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] errorResponse, Throwable e) {
						// called when response HTTP status is "4XX" (eg. 401,
						// 403, 404)
					}

					@Override
					public void onRetry(int retryNo) {
						// called when request is retried
					}
				});

	}*/
}
