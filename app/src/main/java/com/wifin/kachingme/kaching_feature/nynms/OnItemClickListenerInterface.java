package com.wifin.kachingme.kaching_feature.nynms;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by comp on 10/15/2016.
 */
public interface OnItemClickListenerInterface {
    public void onClick(View view, int position);
    public void onLongClick(View view, int position);
}