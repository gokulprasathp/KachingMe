package com.wifin.kachingme.kaching_feature.dazz;

import android.content.Context;
import android.view.View;

/**
 * Created by user on 12/2/2016.
 */
public interface OnItemClickDazz {
    public void onClick(View view, int position, String data, Context context);
    public void onLongClick(View view, int position, String data, Context context);
}
