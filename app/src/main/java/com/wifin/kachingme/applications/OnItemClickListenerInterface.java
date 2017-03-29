package com.wifin.kachingme.applications;

import android.view.View;

/**
 * Created by comp on 10/15/2016.
 */
public interface OnItemClickListenerInterface {
    public void onClick(View view, int position);
    public void onLongClick(View view, int position);
}