package com.wifin.kachingme.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Wifin on 24/11/2016.
 */

public class Custom_Text_View extends TextView
{
    public Custom_Text_View(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public Custom_Text_View(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public Custom_Text_View(Context context)
    {
        super(context);
        init();
    }

    public void init()
    {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SEGOEUI.TTF");
        setTypeface(tf);
    }
}
