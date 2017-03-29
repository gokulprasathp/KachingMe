package com.wifin.kachingme.adaptors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.wifin.kachingme.R;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.FreebieDto;
import com.wifin.kachingme.pojo.FreebieMainDto;
import com.wifin.kachingme.pojo.NewStartUp;
import com.wifin.kachingme.registration_and_login.NewStartUpActivity;
import com.wifin.kachingme.util.Constant;

public class OfferAdapterNew extends BaseAdapter {

    Activity myacActivity;
    Context mContext = NewStartUpActivity.getContext();
    List<NewStartUp> freeList;
    private static LayoutInflater inflater = null;
    int width = Constant.screenWidth;
    int height = Constant.screenHeight;
    Bitmap myBitmap = null;
    String src;
    ImageView web, web2;
    TextView ivText;
    String IMAGE_URL1;
    private ImageLoadingListener animateFirstListener = new NewStartUpActivity.AnimateFirstDisplayListener();
    private DisplayImageOptions options;
    public Resources res;
    ArrayList<String> spinnerList;
    static List<FreebieMainDto> newStartup_list = new ArrayList<FreebieMainDto>();
    ArrayList<FreebieDto> freelist = new ArrayList<FreebieDto>();
    ArrayAdapter<String> mSpinnerAdapter;
    Map<List<Integer>, String> spinnerSelectedList = new HashMap<List<Integer>, String>();
    ArrayList<View> test = new ArrayList<View>();
    TextView marqueeText;
    Dbhelper db;
    SharedPreferences sharedpreferences;
    Editor editor;
    public static final int listpos = 0;
    public static final int spinnerpos = 0;
    public static String mcompany = "";
    private HashMap<Integer, Integer> mapData;
    int i = 0;
    public static HashMap<String, SpinnerValue> hm = new HashMap<String, SpinnerValue>();
    View vi;

    public OfferAdapterNew(Activity act, List<FreebieMainDto> list,
                           Resources resLocal, HashMap<Integer, Integer> myMap) {
        // TODO Auto-generated constructor stub
        myacActivity = act;
        newStartup_list = list;
        this.mapData = myMap;
        Constant.printMsg("sixeeeee:::::::::" + list.size());
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
        // Constant.spinnerpos = -1;
        Constant.hm.clear();
        Constant.spinnerList.clear();
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
            // holder.sp = (TextView) vi.findViewById(R.id.spinneradap);
            holder.mSpinner = (Spinner) vi.findViewById(R.id.spinneradap);
            holder.mProductName = (TextView) vi.findViewById(R.id.spin_text);
            holder.mSpinnerLayout = (FrameLayout) vi.findViewById(R.id.spinner_layout);
            holder.mSpinnerLayoutTop = (LinearLayout) vi.findViewById(R.id.spinner_layoutTop);

            final Object tagObj = "" + position;
            holder.mSpinner.setTag(tagObj);
            test.add(holder.mSpinner);

            LinearLayout.LayoutParams buttonParamsimg = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParamsimg.width = width * 36 / 100;
            buttonParamsimg.height = height * 10 / 100;
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
            // buttonParamstxt.height = height * 5 / 100;
            holder.mProductName.setLayoutParams(buttonParamstxt);

            LinearLayout.LayoutParams spinnerLayoutParama = new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            spinnerLayoutParama.width = width * 38 / 100;
            spinnerLayoutParama.height = height * 6 / 100;
            spinnerLayoutParama.topMargin = height * 1 / 100;
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

            if (width >= 600) {
                holder.mProductName.setTextSize(17);
            } else if (width > 501 && width < 600) {
                holder.mProductName.setTextSize(16);
            } else if (width > 260 && width < 500) {
                holder.mProductName.setTextSize(15);
            } else if (width <= 260) {
                holder.mProductName.setTextSize(14);
            }

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        Constant.printMsg("company........." + newStartup_list.get(position).getCompanyName() + "....."
                + newStartup_list.get(position).getFirstName());
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
        // spinnerList.add("Choose a freebie");
        Constant.printMsg("size:;COMAKA" + newStartup_list.get(position).getCompanyLogoPath());

        for (int i = 0; i < newStartup_list.get(position).getFreebilist().size(); i++) {
            spinnerSelectedList.clear();
            if (newStartup_list.get(position).getFreebilist().get(i).getName().toString() != null) {
                // spinnerList.add("select");
                spinnerList.add(newStartup_list.get(position).getFreebilist().get(i).getName().toString());
                Constant.printMsg("spinnerList::::::::::::"
                        + newStartup_list.get(position).getFreebilist().get(i).getTagLine().toString()
                        + "   " + newStartup_list.get(position).getFreebilist().get(i).getName()
                        + "    " + newStartup_list.get(position).getFreebilist().get(i).getNoOfFreebies());
                mSpinnerAdapter = new ArrayAdapter<String>(myacActivity, R.layout.spinner_item, spinnerList);
                mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if (!spinnerList.get(0).equalsIgnoreCase("Select Offer")) {
                    spinnerList.add(0, "Select Offer");
                }

                final ArrayAdapter<String> spinnerCategoryOne = new ArrayAdapter<String>(myacActivity,
                        android.R.layout.simple_spinner_dropdown_item, spinnerList);
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
                        if (itemValue.contains("Select Offer")){
                            NewStartUpActivity.f_next.setVisibility(View.GONE);
                        }else{
                            NewStartUpActivity.f_next.setVisibility(View.VISIBLE);
                        }
                        Constant.printMsg("Selected Item " + itemValue);
                        hm.clear();
                        Constant.hm.clear();
                        Constant.spinnerpos = pos;
                        Constant.mselectedFreebie = itemValue;
                        List<Integer> valSetOne = new ArrayList<Integer>();
                        valSetOne.add(position);
                        valSetOne.add(pos);
                        spinnerSelectedList.put(valSetOne, itemValue);

                        Constant.printMsg("Selected Item pos::: "
                                + position + spinnerSelectedList.size()
                                + "spinnerSelectedList::"
                                + spinnerSelectedList);
                        hm.put(String.valueOf(position), new SpinnerValue(itemValue, pos));
                        Constant.hm.put(String.valueOf(position), new SpinnerValue(itemValue, pos));
                        printMap(hm, pos);
                        if (pos != 0) {
                            Constant.spinnerList.add(itemValue);
                        } else {

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });

            } else {
                Constant.printMsg("called::::::::else");
                // holder.sp.setAdapter(null);
            }
        }
        return vi;
    }

    public static void printMap(HashMap<String, SpinnerValue> map, int pos) {

        Set<String> keys = map.keySet();
        for (String p : keys) {
            Constant.printMsg(p + "==>" + map.get(p));
            int Key = map.get(p).getspinpos();
            Constant.printMsg("oomm total" + hm.size() + "hm::" + hm
                    + "Constant.hm::" + Constant.hm + "Key:::::spinpos" + Key
                    + "map.get(p).getvalue():" + map.get(p).getvalue());
            // if (hm.containsKey(map.get(p)) || (Key == 0)) {

            if (hm.containsKey(map.get(p))) {
                Constant.printMsg("oomm map.get(p)::" + map.get(p));
                hm.remove(map.get(p));
                Constant.hm.remove(map.get(p));
                // hm.remove(map.get(p).getspinpos());
                // Constant.hm.remove(map.get(p).getspinpos());
                // hm.remove(map.get(p).getvalue());
                // Constant.hm.remove(map.get(p).getvalue());
                Constant.printMsg("oomm if" + hm.size() + "hhh" + hm
                        + "Constant.hm::" + Constant.hm + "Key::in iffffffffff"
                        + Key + "map.get(p).getvalue():idffff"
                        + map.get(p).getvalue());
            } else {
                Constant.printMsg("oomm else" + hm.size() + "hmmm" + hm
                        + "Constant.hm::" + Constant.hm + "Key in selse::"
                        + Key + "map.get(p).getvalue():else"
                        + map.get(p).getvalue());

            }

        }
        Constant.printMsg("nwe start up::in offer" + Constant.hm.size()
                + Constant.hm);
    }

    protected void setshared(int newstlistpos, int spinnerpos2, String mcompany2) {
        // TODO Auto-generated method stub
        Constant.printMsg("position:::::>>>>>" + newstlistpos + "   "
                + spinnerpos2);
        editor = sharedpreferences.edit();
        editor.putInt("listpos", newstlistpos);
        editor.putInt("spinnerpos", spinnerpos2);
        editor.putString("company", mcompany2);
        editor.commit();
    }

    protected void insertToDB(ContentValues cv) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_FREEBIE, null, cv);
            Constant.printMsg("No of inserted rows in freebie :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in kaching details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }

    }

    public static class ViewHolder {
        public ImageView mImageView, mSpinnerDropDown;
        public LinearLayout mSpinnerLayoutTop;
        FrameLayout mSpinnerLayout;
        public Spinner mSpinner;
        public TextView mProductName;
    }

}