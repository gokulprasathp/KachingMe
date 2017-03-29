/*
* @author Gokul
*
* @usage -  This class is used to display the list of freebee from server
*
*
* */

package com.wifin.kachingme.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.FreebieMainDto;
import com.wifin.kachingme.registration_and_login.FreebieActivity;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OfferAdapter extends BaseAdapter {

    public static HashMap<String, SpinnerValue> hm = new HashMap<String, SpinnerValue>();
    static List<FreebieMainDto> newStartup_list = new ArrayList<FreebieMainDto>();
    private static LayoutInflater inflater = null;
    public Resources res;
    Activity myacActivity;
    Context mContext = FreebieActivity.getContext();
    int width = Constant.screenWidth;
    int height = Constant.screenHeight;
    ArrayList<String> spinnerList;
    ArrayAdapter<String> mSpinnerAdapter;
    Map<List<Integer>, String> spinnerSelectedList = new HashMap<List<Integer>, String>();
    ArrayList<View> test = new ArrayList<View>();
    ArrayList<View> viewExpiryTitle = new ArrayList<View>();
    ArrayList<View> viewExpiryValue = new ArrayList<View>();
    Dbhelper db;
    SharedPreferences sharedpreferences, sharedPreferencesMode;
    int i = 0;
    View vi;
    private ImageLoadingListener animateFirstListener = new FreebieActivity.AnimateFirstDisplayListener();
    private DisplayImageOptions options;
    private HashMap<Integer, Integer> mapData;

    public OfferAdapter(Activity act, List<FreebieMainDto> list,
                        Resources resLocal, HashMap<Integer, Integer> myMap) {
        myacActivity = act;
        newStartup_list = list;
        this.mapData = myMap;
        res = resLocal;
        inflater = (LayoutInflater) myacActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.stub).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();
        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(mContext));
        db = new Dbhelper(myacActivity);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(myacActivity);
        Constant.hm.clear();
        Constant.spinnerList.clear();
        sharedPreferencesMode = act.getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
    }

    public static void printMap(HashMap<String, SpinnerValue> map, int pos) {

        Set<String> keys = map.keySet();
        for (String p : keys) {
            if (hm.containsKey(map.get(p))) {
                hm.remove(map.get(p));
                Constant.hm.remove(map.get(p));
            } else {

            }

        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return newStartup_list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        vi = convertView;
        final ViewHolder holder;
        width = Constant.screenWidth;
        height = Constant.screenHeight;
        if (vi == null) {
            vi = inflater.inflate(R.layout.newoffer, parent, false);
            holder = new ViewHolder();
            holder.mImageView = (ImageView) vi.findViewById(R.id.spinimg);
            holder.mSpinnerDropDown = (ImageView) vi.findViewById(R.id.spinimg_right);
            holder.mSpinner = (Spinner) vi.findViewById(R.id.spinneradap);
            holder.mProductName = (TextView) vi.findViewById(R.id.spin_text);
            holder.mSpinnerLayout = (FrameLayout) vi.findViewById(R.id.spinner_layout);
            holder.mSpinnerLayoutTop = (LinearLayout) vi.findViewById(R.id.spinner_layoutTop);
            holder.mExpiryDateTitle = (TextView) vi.findViewById(R.id.spin_expiryTitle);
            holder.mExpiryDateValue = (TextView) vi.findViewById(R.id.spin_expiryValue);
            try {
                Constant.typeFace(mContext, holder.mProductName);
                Constant.typeFace(mContext, holder.mExpiryDateTitle);
                Constant.typeFace(mContext, holder.mExpiryDateValue);
            } catch (Exception e) {
                e.printStackTrace();
            }

            final Object tagObj = "" + position;
            holder.mSpinner.setTag(tagObj);
            test.add(holder.mSpinner);
            viewExpiryTitle.add(holder.mExpiryDateTitle);
            viewExpiryValue.add(holder.mExpiryDateValue);

            LinearLayout.LayoutParams buttonParamsimg = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParamsimg.width = width * 40 / 100;
            buttonParamsimg.height = height * 12 / 100;
            buttonParamsimg.setMargins(width * 2 / 100, height * 3 / 100, width * 2 / 100, height * 3 / 100);
            holder.mImageView.setLayoutParams(buttonParamsimg);

            LinearLayout.LayoutParams spinnerLayoutTopParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            spinnerLayoutTopParama.width = width * 57 / 100;
            spinnerLayoutTopParama.height = height * 16 / 100;
            spinnerLayoutTopParama.leftMargin = width * 3 / 100;
            holder.mSpinnerLayoutTop.setLayoutParams(spinnerLayoutTopParama);
            holder.mSpinnerLayoutTop.setGravity(Gravity.CENTER | Gravity.LEFT);

            LinearLayout.LayoutParams buttonParamstxt = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParamstxt.gravity = Gravity.LEFT | Gravity.CENTER;
            holder.mProductName.setLayoutParams(buttonParamstxt);

            LinearLayout.LayoutParams spinnerLayoutParama = new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            spinnerLayoutParama.width = width * 38 / 100;
            spinnerLayoutParama.height = height * 6 / 100;
            spinnerLayoutParama.topMargin = height * 1 / 2 / 100;
            holder.mSpinnerLayout.setLayoutParams(spinnerLayoutParama);

            FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParams.width = width * 38 / 100;
            buttonParams.height = height * 6 / 100;
            buttonParams.gravity = Gravity.CENTER | Gravity.LEFT;
            holder.mSpinner.setLayoutParams(buttonParams);

            FrameLayout.LayoutParams spinnerDropParams = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            spinnerDropParams.width = width * 5 / 100;
            spinnerDropParams.height = width * 5 / 100;
            spinnerDropParams.gravity = Gravity.CENTER | Gravity.RIGHT;
            spinnerDropParams.rightMargin = width * 2 / 100;
            holder.mSpinnerDropDown.setLayoutParams(spinnerDropParams);

            LinearLayout.LayoutParams expiryTitleParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            expiryTitleParama.gravity = Gravity.LEFT | Gravity.CENTER;
            expiryTitleParama.topMargin = height * 1 / 2 / 100;
            holder.mExpiryDateTitle.setLayoutParams(expiryTitleParama);

            LinearLayout.LayoutParams expiryValueParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            expiryValueParama.gravity = Gravity.LEFT | Gravity.CENTER;
            expiryValueParama.topMargin = height * 1 / 2 / 100;
            holder.mExpiryDateValue.setLayoutParams(expiryValueParama);

            if (width >= 600) {
                holder.mProductName.setTextSize(16);
                holder.mExpiryDateTitle.setTextSize(14);
                holder.mExpiryDateValue.setTextSize(14);
            } else if (width > 501 && width < 600) {
                holder.mProductName.setTextSize(15);
                holder.mExpiryDateTitle.setTextSize(13);
                holder.mExpiryDateValue.setTextSize(13);
            } else if (width > 260 && width < 500) {
                holder.mProductName.setTextSize(14);
                holder.mExpiryDateTitle.setTextSize(12);
                holder.mExpiryDateValue.setTextSize(12);
            } else if (width <= 260) {
                holder.mProductName.setTextSize(13);
                holder.mExpiryDateTitle.setTextSize(11);
                holder.mExpiryDateValue.setTextSize(11);
            }
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        if (newStartup_list.get(position).getCompanyName() != null) {
            holder.mProductName.setText(newStartup_list.get(position).getCompanyName());
        } else {
            holder.mProductName.setVisibility(View.GONE);
        }
        ImageLoader.getInstance().displayImage(String.valueOf(
                newStartup_list.get(position).getCompanyLogoPath())
                .replaceAll(" ", "%20"), holder.mImageView, options, animateFirstListener);
        Constant.spinnerList.clear();
        spinnerList = new ArrayList<String>();
        for (int i = 0; i < newStartup_list.get(position).getFreebilist().size(); i++) {
            spinnerSelectedList.clear();
            if (newStartup_list.get(position).getFreebilist().get(i).getName().toString() != null) {
                spinnerList.add(newStartup_list.get(position).getFreebilist().get(i).getName().toString());
                mSpinnerAdapter = new ArrayAdapter<String>(myacActivity, R.layout.spinner_item, spinnerList);
                mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if (!spinnerList.get(0).equalsIgnoreCase("Select Offer")) {
                    spinnerList.add(0, "Select Offer");
                }
                LinearLayout.LayoutParams spinnerParama = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                spinnerParama.gravity = Gravity.LEFT | Gravity.CENTER;
                spinnerParama.leftMargin = width * 1 / 2 / 100;
                final ArrayAdapter<String> spinnerCategoryOne = new ArrayAdapter<String>(myacActivity, R.layout.freebie_spinner_dropdown, spinnerList) {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        ((TextView) v).setHeight(height * 6 / 100);
                        ((TextView) v).setPadding(width * 2 / 100, 0, 0, 0);
                        ((TextView) v).setGravity(Gravity.LEFT | Gravity.CENTER);
                        if (width >= 600) {
                            ((TextView) v).setTextSize(15);
                        } else if (width > 501 && width < 600) {
                            ((TextView) v).setTextSize(14);
                        } else if (width > 260 && width < 500) {
                            ((TextView) v).setTextSize(13);
                        } else if (width <= 260) {
                            ((TextView) v).setTextSize(12);
                        }
                        return v;
                    }

                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View v = super.getDropDownView(position, convertView, parent);
                        ((TextView) v).setHeight(height * 6 / 100);
                        ((TextView) v).setPadding(width * 2 / 100, 0, 0, 0);
                        ((TextView) v).setGravity(Gravity.LEFT | Gravity.CENTER);
                        if (width >= 600) {
                            ((TextView) v).setTextSize(15);
                        } else if (width > 501 && width < 600) {
                            ((TextView) v).setTextSize(14);
                        } else if (width > 260 && width < 500) {
                            ((TextView) v).setTextSize(13);
                        } else if (width <= 260) {
                            ((TextView) v).setTextSize(12);
                        }
                        return v;
                    }
                };
                holder.mSpinner.setAdapter(spinnerCategoryOne);

                if (mapData.get(position) != null) {
                    holder.mSpinner.setSelection(mapData.get(position));
                }
                hm.clear();
                Constant.hm.clear();

                holder.mSpinner.setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // TODO Auto-generated method stub
                        for (int j = 0; j < test.size(); j++) {
                            Spinner views = (Spinner) test.get(j);
                            views.setSelection(0);
                            TextView textTitle = (TextView) viewExpiryTitle.get(j);
                            TextView textValue = (TextView) viewExpiryValue.get(j);
                            textTitle.setVisibility(View.GONE);
                            textValue.setVisibility(View.GONE);
                        }
                        holder.mSpinner.setAdapter(spinnerCategoryOne);
                        return false;
                    }
                });
                holder.mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int pos, long id) {
                        // TODO Auto-generated method stub
                        String itemValue = (String) holder.mSpinner.getItemAtPosition(pos);
                        if (itemValue.contains("Select Offer")) {
                            FreebieActivity.f_next.setVisibility(View.GONE);
                        } else {
                            FreebieActivity.f_next.setVisibility(View.VISIBLE);
                        }
                        hm.clear();
                        Constant.hm.clear();
                        Constant.spinnerpos = pos;
                        Constant.mselectedFreebie = itemValue;
                        List<Integer> valSetOne = new ArrayList<Integer>();
                        valSetOne.add(position);
                        valSetOne.add(pos);
                        spinnerSelectedList.put(valSetOne, itemValue);

                        hm.put(String.valueOf(position), new SpinnerValue(itemValue, pos));
                        Constant.hm.put(String.valueOf(position), new SpinnerValue(itemValue, pos));
                        printMap(hm, pos);
                        if (pos != 0) {
                            Constant.spinnerList.add(itemValue);
                        } else {

                        }
                        Set<Map.Entry<String, SpinnerValue>> setMap = Constant.hm.entrySet();
                        Iterator<Map.Entry<String, SpinnerValue>> iteratorMap = setMap.iterator();
                        while (iteratorMap.hasNext()) {
                            Map.Entry<String, SpinnerValue> entry = (Map.Entry<String, SpinnerValue>) iteratorMap
                                    .next();
                            SpinnerValue Key = entry.getValue();
                            if (!Key.getvalue().equalsIgnoreCase("Select Offer")) {
                                int finalspin = Key.getspinpos();
                                String finalimg = entry.getKey();
                                String expDate = newStartup_list.get(Integer.valueOf(finalimg)).getFreebilist().get(finalspin - 1).getValid_untilSrt();
                                holder.mExpiryDateTitle.setVisibility(View.VISIBLE);
                                holder.mExpiryDateValue.setVisibility(View.VISIBLE);
                                holder.mExpiryDateValue.setText(expDate);
                                Editor e = sharedPreferencesMode.edit();
                                e.putString("ExpiryDate", expDate);
                                e.commit();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });

            } else {
            }
        }
        return vi;
    }

    public static class ViewHolder {
        public ImageView mImageView, mSpinnerDropDown;
        public LinearLayout mSpinnerLayoutTop;
        public Spinner mSpinner;
        public TextView mProductName, mExpiryDateTitle, mExpiryDateValue;
        FrameLayout mSpinnerLayout;
    }

}