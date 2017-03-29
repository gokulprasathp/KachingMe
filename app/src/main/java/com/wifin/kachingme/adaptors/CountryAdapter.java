package com.wifin.kachingme.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.wifin.kachingme.pojo.CountryCodeGetSet;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.registration_and_login.Signin;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter<CountryCodeGetSet> {

	private Activity activity;
	private ArrayList data;
	public Resources res;
	CountryCodeGetSet tempValues = null;
	LayoutInflater inflater;

	/************* CustomAdapter Constructor *****************/
	public CountryAdapter(Signin activitySpinner, int textViewResourceId,
						  ArrayList objects, Resources resLocal) {
		super(activitySpinner, textViewResourceId, objects);

		/********** Take passed values **********/
		activity = activitySpinner;
		data = objects;
		res = resLocal;

		/*********** Layout inflator to call external xml layout () **********************/
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	// This funtion called for each row ( Called data.size() times )
	public View getCustomView(int position, View convertView, ViewGroup parent) {

		/********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
		View row = inflater.inflate(R.layout.country_list_item, parent, false);

		/***** Get each Model object from Arraylist ********/
		tempValues = null;
		tempValues = (CountryCodeGetSet) data.get(position);

		TextView country = (TextView) row.findViewById(R.id.txt_country);
		TextView code = (TextView) row.findViewById(R.id.txt_code);
        Constant.typeFace(activity,country);

		/*
		 * if(position==0){
		 *
		 * // Default selected Spinner item
		 * label.setText("Please select company"); sub.setText(""); }
		 */
		/*
		 * else {
		 */
		// Set values for spinner each row
		try {
			country.setText(tempValues.getName());
		} catch (Exception e) {
			// TODO: handle exception
		}
		// code.setText(tempValues.getCode());

		/* } */

		return row;
	}
}
