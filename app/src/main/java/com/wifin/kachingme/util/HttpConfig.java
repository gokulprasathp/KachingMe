package com.wifin.kachingme.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.StrictMode;
import android.util.Log;

public class HttpConfig {
	String TAG = "KaChing.me";

	public String httpget(String URL) {

		String responseString = null;

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		try {
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 10 * 1000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 10 * 1000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpGet hg = new HttpGet(URL);
			HttpResponse response = httpclient.execute(hg);
			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			} else {
				responseString = null;
				response.getEntity().getContent().close();
				// throw new IOException(statusLine.getReasonPhrase());
			}
            Constant.printMsg("Exceptio mmmmmmmmmmresponseStringmmmmmmm....."+responseString);
        } catch (ConnectTimeoutException e) {
			responseString = null;
            Constant.printMsg("Exceptio mmmmmmmmmmmmmmmmmmm....."+e);
		} catch (Exception e) {
			// TODO: handle exception
			responseString = null;
			// e.printStackTrace();
            Constant.printMsg("Exceptio mmmmmmmmmmmmmmmmmmm....."+e);
        }
		return responseString;
	}

	// public String httppost(String URL, Object a) {
	//
	// String responseString = null;
	// int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	// if (currentapiVersion >=
	// android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
	// StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
	// .permitAll().build();
	// StrictMode.setThreadPolicy(policy);
	// }
	// try {
	// HttpParams httpParameters = new BasicHttpParams();
	// int timeoutConnection = 10 * 1000;
	// HttpConnectionParams.setConnectionTimeout(httpParameters,
	// timeoutConnection);
	// int timeoutSocket = 10 * 1000;
	// HttpClient client = new DefaultHttpClient();
	// HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
	// HttpResponse response;
	// HttpPost post = new HttpPost(URL);
	// response = client.execute(post);
	// if (response != null) {
	// responseString = response.getEntity().getContent().toString();
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return responseString;
	//
	// }

	// public String phpPost1(List<NameValuePair> nameValuePairs, String url) {
	// String res = null;
	// Log.v(TAG, "postURL: " + url);
	//
	// // HttpClient
	// HttpClient httpClient = new DefaultHttpClient();
	// HttpConnectionParams
	// .setConnectionTimeout(httpClient.getParams(), 60000);
	// // post header
	// HttpPost httpPost = new HttpPost(url);
	// Constant.printMsg("The posting data is :::::::" + nameValuePairs);
	// try {
	// httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	// } catch (UnsupportedEncodingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// // execute HTTP post request
	// HttpResponse response = null;
	// try {
	// response = httpClient.execute(httpPost);
	// } catch (ClientProtocolException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// HttpEntity resEntity = response.getEntity();
	// if (resEntity != null) {
	// String responseStr = null;
	// try {
	// res = EntityUtils.toString(resEntity).trim();
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// Log.v(TAG, "Response: " + responseStr);
	// // you can add an if statement here and do other actions based on
	// // the response
	// }
	// return res;
	// }

	// public HttpResponse httploginpost(String URL, List<NameValuePair> a) {
	// // InputStream in = null;
	// int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	// if (currentapiVersion >=
	// android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
	// StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
	// .permitAll().build();
	// StrictMode.setThreadPolicy(policy);
	// }
	//
	// HttpClient client = new DefaultHttpClient();
	//
	// HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
	//
	// HttpResponse response = null;
	//
	// try {
	//
	// HttpPost post = new HttpPost(URL);
	// post.setEntity(new UrlEncodedFormEntity(a));
	// response = client.execute(post);
	// // if (response != null) {
	// // // in = response.getEntity().getContent();
	// // ByteArrayOutputStream out = new ByteArrayOutputStream();
	// // response.getEntity().writeTo(out);
	// // out.close();
	// //
	// // Constant.printMsg(out.toString());
	// // }
	//
	// }
	//
	// catch (Exception e) {
	//
	// e.printStackTrace();
	//
	// }
	// return response;
	//
	// }

	// public StatusLine httppost1(String pd, String url) {
	// // TODO Auto-generated method stub
	// if (android.os.Build.VERSION.SDK_INT > 9) {
	// StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
	// .permitAll().build();
	// StrictMode.setThreadPolicy(policy);
	// }
	// InputStream in = null;
	// HttpClient client = new DefaultHttpClient();
	//
	// HttpConnectionParams.setConnectionTimeout(client.getParams(), 60000);
	//
	// HttpResponse response;
	//
	// StatusLine statusLine = null;
	//
	// try {
	// Constant.printMsg("I am inside the Post===========>");
	// Constant.printMsg("posting url is :::::" + url);
	// HttpPost post = new HttpPost(url);
	//
	// // Gson gson = new Gson();
	// List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	// nameValuePairs.add(new BasicNameValuePair("Data", pd));
	// Constant.printMsg(" =>=>=>=>=> final posting datas <=======>"
	// + nameValuePairs.toString());
	//
	// post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	// client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
	// System.getProperty("http.agent"));
	// response = client.execute(post);
	// statusLine = response.getStatusLine();
	// Constant.printMsg(statusLine);
	// Constant.printMsg(response);
	// if (response != null) {
	//
	// in = response.getEntity().getContent();
	// }
	// Constant.printMsg(in.toString());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return statusLine;
	// }

	@SuppressWarnings("unused")
	public String doPostMobizee(String content, String urlValue) {
		StringBuffer response = new StringBuffer();
		int status = 0;
		boolean responsecode = false;
		Log.v(TAG, "Content :: " + content);
		String postData = null;
		Constant.printMsg("The posting url :::::::" + urlValue);
		String urlString = urlValue;
		content = content.replace("&", "%26");
		try {

			postData = new String(content.getBytes(), "utf-8");
			Log.v(TAG, "After encrypted" + postData);
		} catch (Exception e) {

		}

		StringBuffer sb = new StringBuffer();
		HttpParams httpParameters = new BasicHttpParams();
		// int timeoutConnection = 10 * 1000;
		// HttpConnectionParams.setConnectionTimeout(httpParameters,
		// timeoutConnection);
		// int timeoutSocket = 10 * 1000;
		// HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpURLConnection connection = null;
		try {
			URL url = new URL(urlString);
			try {
				connection = (HttpURLConnection) url.openConnection();
			} catch (Exception e) {
				// TODO: handle exception
				responsecode = false;
			}
			byte[] buff;

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(postData.getBytes().length));
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setConnectTimeout(30 * 1000);
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(
					connection.getOutputStream(), "UTF-8"));
			wr.write(postData);
			wr.flush();
			wr.close();
			Log.v(TAG, "After posting************ ");
			Log.v(TAG, "Response Code" + connection.getResponseCode());
			// Get Response
			InputStream is;

			if (connection.getResponseCode() <= 400) {
				is = connection.getInputStream();
			} else {
				is = connection.getErrorStream();

			}
			status = connection.getResponseCode();

			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;

			while ((line = rd.readLine()) != null) {
				responsecode = true;
				response.append(line);
				response.append('\r');
				Constant.printMsg("buffred data is :::::" + response);
			}
			rd.close();
		} catch (SocketTimeoutException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			connection.disconnect();
		}
		Constant.printMsg("The server response is ::::::" + status);
		return response.toString();

	}

    public String doPostNameValue(String url, ArrayList<NameValuePair> nameValuePairs) {
        StringBuilder result = new StringBuilder();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpclient.execute(httppost);
            HttpResponse response = httpclient.execute(httppost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            while ((sResponse = reader.readLine()) != null) {
                result = result.append(sResponse);
            }
        } catch (Exception e) {
        }
        return result.toString();
    }
}
