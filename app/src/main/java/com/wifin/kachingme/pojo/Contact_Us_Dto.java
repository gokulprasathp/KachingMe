package com.wifin.kachingme.pojo;

import java.util.ArrayList;

public class Contact_Us_Dto {
	String phoneNumber;
	String description;
	ArrayList<String> imageStrings = new ArrayList<String>();

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<String> getImageStrings() {
		return imageStrings;
	}

	public void setImageStrings(ArrayList<String> imageStrings) {
		this.imageStrings = imageStrings;
	}

}
