package com.wifin.kachingme.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.OverScroller;
import android.widget.ScrollView;

import com.wifin.kaching.me.ui.R;

/**
 * Created by comp on 4/17/2017.
 */
public class CustomScrollView extends ScrollView
{

    public static int maxScrollSpeed  = 5000;

    public ScrollListener scrollListener;
    public CustomScrollView(Context context) {


        super(context);
    }

    private boolean enableScrolling = true;

    public boolean isEnableScrolling() {
        return enableScrolling;
    }

    public void setEnableScrolling(boolean enableScrolling) {
        this.enableScrolling = enableScrolling;
    }



    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setOnScrolledChanged(ScrollListener listener) {
        this.scrollListener = listener;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

//        scrollListener.onScrollChanged( l,  t,  oldl,  oldt);
        super.onScrollChanged(l, t, oldl, oldt);

    }

    public  interface ScrollListener{

        public  void onScrollChanged(int l, int t, int oldl, int oldt);

} @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {

    if (isEnableScrolling()) {
        return super.onInterceptTouchEvent(ev);
    } else {
        return false;
    }
}
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEnableScrolling()) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }
//    @Override
//    public void fling(int velocityY) {
//        super.fling(0);
//    }
//    @Override
//    public void fling(int velocityY) {
//        int topVelocityY = (int) ((Math.min(Math.abs(velocityY), maxScrollSpeed) ) * Math.signum(velocityY));
//        super.fling(topVelocityY);
//    }
}


