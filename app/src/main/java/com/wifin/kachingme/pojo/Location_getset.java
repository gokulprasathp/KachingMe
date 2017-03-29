package com.wifin.kachingme.pojo;

public class Location_getset {
	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public Boolean getIs_selected() {
		return is_selected;
	}

	public void setIs_selected(Boolean is_selected) {
		this.is_selected = is_selected;
	}

	String image_path;
	String vicinity;
	String name;
	String icon_path;

	public String getIcon_path() {
		return icon_path;
	}

	public void setIcon_path(String icon_path) {
		this.icon_path = icon_path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	double lat;
	double lon;
	Boolean is_selected;

}
