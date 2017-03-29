package com.wifin.kachingme.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.wifin.kaching.me.ui.R;

public class Emoji {

	private HashMap<String, Integer> emoticons = new HashMap<String, Integer>();
	private ArrayList<String> arrayListSmileys = new ArrayList<String>();
	Context context;

	public Emoji(Context con) {
		context = con;

		// emoticons.put("czyll", R.drawable.chennai);
		emoticons.put(":-)", R.drawable.em_1);
		emoticons.put(";-)", R.drawable.em_2);
		emoticons.put(":-(", R.drawable.em_3);
		emoticons.put(":'-(", R.drawable.em_4);
		emoticons.put("B-)", R.drawable.em_5);
		emoticons.put(":-*", R.drawable.em_6);
		emoticons.put(":-P", R.drawable.em_7);
		emoticons.put(":O", R.drawable.em_8);
		emoticons.put("O:-)", R.drawable.em_9);
		emoticons.put(":-O", R.drawable.em_10);
		emoticons.put(":-[", R.drawable.em_11);
		emoticons.put(":-!", R.drawable.em_12);
		emoticons.put(":-/", R.drawable.em_13);
		emoticons.put(":-D", R.drawable.em_14);
		emoticons.put("o_O", R.drawable.em_15);
		emoticons.put(":-X", R.drawable.em_16);

		fillArrayList();
	}

	public HashMap<String, Integer> getEmoticons() {

		return emoticons;
	}

	public ArrayList<String> getArrayList() {

		return arrayListSmileys;
	}

	private void fillArrayList() {
		Iterator<Entry<String, Integer>> iterator = emoticons.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, Integer> entry = iterator.next();
			arrayListSmileys.add(entry.getKey());
		}
	}

	public Spannable getSmiledText(String text) {

		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		int index;
		for (index = 0; index < builder.length(); index++) {
			for (Entry<String, Integer> entry : emoticons.entrySet()) {
				int length = entry.getKey().length();
				if (index + length > builder.length())
					continue;

				if (builder.subSequence(index, index + length).toString()
						.equals(entry.getKey())) {
					Drawable d = context.getResources().getDrawable(
							entry.getValue());
					d.setBounds(0, 0, d.getIntrinsicWidth() / 2,
							d.getIntrinsicHeight() / 2);
					ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
					builder.setSpan(span, index, index + length,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					index += length - 1;
					break;
				}
			}
		}
		return builder;
	}

	public Spannable getSmiledText_List(String text) {

		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		int index;
		for (index = 0; index < builder.length(); index++) {
			for (Entry<String, Integer> entry : emoticons.entrySet()) {
				int length = entry.getKey().length();
				if (index + length > builder.length())
					continue;

				if (builder.subSequence(index, index + length).toString()
						.equals(entry.getKey())) {
					Drawable d = context.getResources().getDrawable(
							entry.getValue());
					d.setBounds(0, 0, d.getIntrinsicWidth() / 3,
							d.getIntrinsicHeight() / 3);
					ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
					builder.setSpan(span, index, index + length,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					index += length - 1;
					break;
				}
			}
		}
		return builder;
	}
}
