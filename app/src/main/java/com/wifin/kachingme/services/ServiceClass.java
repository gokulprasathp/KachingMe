package com.wifin.kachingme.services;

import com.wifin.kachingme.util.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class ServiceClass {

	public static String getServerCall(String URL) {
		String resp = null;
		try {
			Constant.printMsg("Call Service ::" + URL);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(URL));
			Constant.printMsg("RESPONSE : " + response);

			InputStream is = response.getEntity().getContent();

			BufferedReader rd = new BufferedReader(new InputStreamReader(is),
					8192);

			// Response
			// BufferedReader rd = new BufferedReader(new InputStreamReader(is,
			// "UTF-8"));
			String line;

			StringBuilder sb = new StringBuilder();
			int len;
			char[] buf = new char[20];
			while ((len = rd.read(buf)) != -1) {
				sb.append(buf, 0, len);
			}

			rd.close();
			resp = String.valueOf(sb);
			Constant.printMsg("Response Text ::" + sb);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return resp;
	}

	public static String postServerCall(String value, String urls)
			throws UnsupportedEncodingException {
		// Get user defined values

		// Create data variable for sent values to server
		Constant.printMsg("Call Service ::" + urls);
		Constant.printMsg("Call Service value ::" + value);
		String data = value;

		String text = "";
		BufferedReader reader = null;

		// Send data
		try {

			// Defined URL where to send data
			URL url = new URL(urls);

			// Send POST data request

			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the server response

			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;

			// Read Server Response
			while ((line = reader.readLine()) != null) {
				// Append server response in string
				sb.append(line + "\n");
			}

			text = sb.toString();
			Constant.printMsg("res" + text);
		} catch (Exception ex) {

		} finally {
			try {

				reader.close();
			}

			catch (Exception ex) {
			}
		}

		// Show response on activity
		return text;
	}

}
