package com.wifin.kachingme.util;

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.UnderlineSpan;

public class NoUnderlineSpan extends UnderlineSpan {
	public NoUnderlineSpan() {
	}

	public NoUnderlineSpan(Parcel src) {
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setUnderlineText(false);
	}

}
