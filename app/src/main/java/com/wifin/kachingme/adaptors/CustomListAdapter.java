package com.wifin.kachingme.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    Context customContext;
    List<String> customListArray = new ArrayList();
    LayoutInflater inflaterListArray;
    int selectedPos = 0;

    public CustomListAdapter() {
    }

    public CustomListAdapter(Context customContext, List<String> customListArray, int selectedPosition) {
        this.customContext = customContext;
        this.customListArray = customListArray;
        this.selectedPos = selectedPosition;
        inflaterListArray = (LayoutInflater) customContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return customListArray.size();
    }

    @Override
    public Object getItem(int position) {
        return customListArray.indexOf(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolderAlert viewHolderAlert;

        if (convertView == null) {
            convertView = inflaterListArray.inflate(R.layout.alert_list_item, parent, false);

            viewHolderAlert = new ViewHolderAlert();

            viewHolderAlert.radioListAlert = (RadioButton) convertView.findViewById(R.id.radioListAlert);
            try {
                Constant.typeFace(customContext, viewHolderAlert.radioListAlert);
            } catch (Exception e) {
                e.printStackTrace();
            }
            viewHolderAlert.radioListAlert.setText(customListArray.get(position));

            viewHolderAlert.radioListAlert.setTextSize(18);

            if (position == selectedPos) {
                viewHolderAlert.radioListAlert.setChecked(true);

            } else {
                viewHolderAlert.radioListAlert.setChecked(false);
            }
        }
        return convertView;
    }

    static class ViewHolderAlert {
        RadioButton radioListAlert;
    }
}
