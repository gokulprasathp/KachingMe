package com.wifin.kachingme.util;

import org.apache.http.Header;
//import cz.msebera.android.httpclient.Header;
import android.content.Context;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.loopj.android.http.*;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;
//import com.loopj.android.http.SyncHttpClient;


public class SendWeb_Group {

	public static void Add_Message_on_web(Context context, MessageGetSet message) {

		final String TAG = SendWeb_Group.class.getSimpleName();
		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams perams = new RequestParams();
		perams.put("Waction", "insert");
		perams.put("Wtable", "tbl_gchat");
		perams.put("mid", "'" + message.getKey_id() + "'");
		perams.put("body", "'" + message.getData() + "'");
		perams.put("mtype", "'" + message.getMedia_wa_type() + "'");
		perams.put("fromjid", "'" + KachingMeApplication.getjid() + "'");
		perams.put("tojid", "'" + message.getKey_remote_jid() + "'");
		// perams.put("etime","'"+txt_name.getText().toString()+"'");
		// perams.put("rtime","'"+txt_name.getText().toString()+"'");
		perams.put("status", 0);

		// perams.put("chattype",message.getIs_sec_chat());

		perams.put("stime", "FROM_UNIXTIME(" + message.getTimestamp() / 1000
				+ ")");
		perams.put(
				"lat",
				KachingMeApplication.getsharedpreferences().getString(
						Constant.CURRENT_LAT, "0"));
		perams.put(
				"lon",
				KachingMeApplication.getsharedpreferences().getString(
						Constant.CURRENT_LOG, "0"));

		if (message.getMedia_wa_type().equals("4")) {
			perams.put("body",
					"'" + message.getLatitude() + "," + message.getLongitude()
							+ "'");
		}

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
						Log.d(TAG, "Inseret Group Message successfully::"
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

	}

	public static void Update_Message_on_web(Context context, String mid,
			int status, String rtime) {

		final String TAG = SendWeb.class.getSimpleName();
		SyncHttpClient client = new SyncHttpClient();

		RequestParams perams = new RequestParams();
		perams.put("Waction", "update");
		perams.put("Wtable", "tbl_gchat");
		perams.put("Wcolumn", "mid");
		perams.put("Wvalue", "'" + mid + "'");
		perams.put("status", status);
		// perams.put("rtime","FROM_UNIXTIME("+rtime+")");

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

	}

	public static void Update_Message_on_web_async(Context context, String mid,
			int status, String rtime) {

		final String TAG = SendWeb.class.getSimpleName();
		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams perams = new RequestParams();
		perams.put("Waction", "update");
		perams.put("Wtable", "tbl_gchat");
		perams.put("Wcolumn", "mid");
		perams.put("Wvalue", "'" + mid + "'");
		perams.put("status", status);
		perams.put("rtime", "FROM_UNIXTIME(" + rtime + ")");

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

	}

	public static void Update_Status_on_web_async(Context context, String gjid,
			String subject) {

		final String TAG = SendWeb.class.getSimpleName();
		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams perams = new RequestParams();
		perams.put("Waction", "update");
		perams.put("Wtable", "tbl_glist");
		perams.put("Wcolumn", "gjid");
		perams.put("Wvalue", "'" + gjid + "'");
		perams.put("gsub", "'" + subject + "'");

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

	}

	public static void Update_Memberlist_on_web_async(Context context,
			String gjid, String memberlist) {

		final String TAG = SendWeb.class.getSimpleName();
		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams perams = new RequestParams();
		perams.put("Waction", "update");
		perams.put("Wtable", "tbl_glist");
		perams.put("Wcolumn", "gjid");
		perams.put("Wvalue", "'" + gjid + "'");
		perams.put("memberlist", "'" + memberlist + "'");

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

	}

	public static void Update_Memberlist_on_web(Context context, String gjid,
			String memberlist) {

		final String TAG = SendWeb.class.getSimpleName();
		SyncHttpClient client = new SyncHttpClient();

		RequestParams perams = new RequestParams();
		perams.put("Waction", "update");
		perams.put("Wtable", "tbl_glist");
		perams.put("Wcolumn", "gjid");
		perams.put("Wvalue", "'" + gjid + "'");
		perams.put("memberlist", "'" + memberlist + "'");

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

	}

	public static void Update_Gicon_on_web(Context context, String gjid,
			String gicon) {

		final String TAG = SendWeb.class.getSimpleName();
		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams perams = new RequestParams();
		perams.put("Waction", "update");
		perams.put("Wtable", "tbl_glist");
		perams.put("Wcolumn", "gjid");
		perams.put("Wvalue", "'" + gjid + "'");
		perams.put("gicon", "'" + gicon + "'");

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

	}

	public static void Update_Admin_on_web_async(Context context, String gjid,
			String adminjid) {

		final String TAG = SendWeb.class.getSimpleName();
		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams perams = new RequestParams();
		perams.put("Waction", "update");
		perams.put("Wtable", "tbl_glist");
		perams.put("Wcolumn", "gjid");
		perams.put("Wvalue", "'" + gjid + "'");
		perams.put("adminjid", "'" + adminjid + "'");

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

	}

	public static void Add_Group_on_web(Context context, String gjid,
			String subject, String adminjid, String memberlist, String ctime,
			String gicon) {

		final String TAG = SendWeb_Group.class.getSimpleName();
		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams perams = new RequestParams();
		perams.put("Waction", "insert");
		perams.put("Wtable", "tbl_glist");
		perams.put("gjid", "'" + gjid + "'");
		perams.put("gsub", "'" + subject + "'");
		perams.put("adminjid", "'" + adminjid + "'");
		perams.put("memberlist", "'" + memberlist + "'");
		perams.put("gicon", "'" + gicon + "'");
		perams.put("ctime", "FROM_UNIXTIME(" + ctime + ")");
		// perams.put("lat",KachingMeApplication.getsharedpreferences().getString(Constant.CURRENT_LAT,
		// "0"));
		// perams.put("lon",KachingMeApplication.getsharedpreferences().getString(Constants.CURRENT_LOG,
		// "0"));

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
						Log.d(TAG, "Inseret Group successfully::"
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

	}
}
