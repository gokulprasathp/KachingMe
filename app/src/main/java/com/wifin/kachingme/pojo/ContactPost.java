package com.wifin.kachingme.pojo;

import java.util.List;

public class ContactPost {

	private String phoneNumber;

	private List<UserContactDto> userContactDtos;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<UserContactDto> getUserContactDtos() {
		return userContactDtos;
	}

	public void setUserContactDtos(List<UserContactDto> userContactDtos) {
		this.userContactDtos = userContactDtos;
	}

}
