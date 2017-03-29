package com.wifin.kachingme.util;

import com.google.android.gms.maps.model.LatLng;

import android.location.Address;

public class GeoSearchResult {

	private Address address;

	public GeoSearchResult(Address address) {
		this.address = address;
	}

	public String getAddress() {

		String display_address = "";

		display_address += address.getAddressLine(0) + "\n";

		for (int i = 1; i < address.getMaxAddressLineIndex(); i++) {
			display_address += address.getAddressLine(i) + ", ";
		}

		display_address = display_address.substring(0,
				display_address.length() - 2);

		return display_address;
	}

	// public String getLatLng(){
	//
	// String LatLng = "";
	//
	// LatLng += "Latitude: " + address.getLatitude() + "\n";
	// LatLng += "Longitude: " + address.getLongitude() + "\n";
	//
	// LatLng = LatLng.substring(0, LatLng.length() - 2);
	//
	// // double lat = address.getLatitude();
	// // double longi = address.getLongitude();
	//
	//
	// return LatLng;
	// }

	public LatLng getLatLng() {

		String LatLng = "";
		double lat;
		double lon;

		LatLng += "Latitude: " + address.getLatitude() + "\n";
		LatLng += "Longitude: " + address.getLongitude() + "\n";

		lat = address.getLatitude();
		lon = address.getLongitude();

		LatLng = LatLng.substring(0, LatLng.length() - 2);

		// double lat = address.getLatitude();
		// double longi = address.getLongitude();
		LatLng latLng = new LatLng(lat, lon);

		return latLng;
	}

	public String toString() {
		String display_address = "";

		if (address.getFeatureName() != null) {
			display_address += address + ", ";
		}

		for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
			display_address += address.getAddressLine(i);
		}

		return display_address;
	}
}