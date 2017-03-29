package com.wifin.kachingme.social_media;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wifin.kachingme.chat_home.MainActivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kaching.me.ui.R;

public class WebViewActivity extends MainActivity {

	WebView browser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
		View.inflate(this, R.layout.socialwebview, vg);
		sideMenufoot.setVisibility(View.VISIBLE);
		logo.setVisibility(View.INVISIBLE);
		footer.setVisibility(View.GONE);
		head.setText("Tell a Friend");
		back.setVisibility(View.VISIBLE);
		head.setTextColor(Color.parseColor("#FFFFFF"));
		back.setBackgroundResource(R.drawable.arrow);
		headlay.setBackgroundColor(Color.parseColor("#FE0000"));
		Ka_newlogo.setVisibility(View.INVISIBLE);

		browser = (WebView) findViewById(R.id.webview);

		if (Constant.fb == true) {

			browser.setWebViewClient(new MyWebViewClient());
			WebSettings settings = browser.getSettings();
			settings.setDomStorageEnabled(true);
			browser.setWebChromeClient(new WebChromeClient());
			browser.loadUrl("https://www.facebook.com/sharer/sharer.php?u=http://115.124.98.178:8080/kachingme");

		} else {

			browser.setWebViewClient(new MyWebViewClient());
			WebSettings settings = browser.getSettings();
			settings.setDomStorageEnabled(true);
			browser.setWebChromeClient(new WebChromeClient());
			browser.loadUrl("https://twitter.com/intent/tweet?=text=Hey,\n\n"
					+ "I just downloaded Kaching.Me Messanger on my Android.\n\n"
					+ "It is a smartphone messanger which replaces SMS. This app event lets me send pictures,video and other multimedia.\n\n"
					+ "Kaching.Me is available for Android and there is no PIN or username to remember - it work just like SMS and uses your internet data plan.\n\n"
					+ "Get it now from Google play and say good-bye to SMS!");

		}
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);

			return true;
		}
	}
}
