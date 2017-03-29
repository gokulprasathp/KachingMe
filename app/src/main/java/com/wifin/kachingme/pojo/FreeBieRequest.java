package com.wifin.kachingme.pojo;

public class FreeBieRequest {

	String otp;
	String phoneNumber;
	String lattitude;
	String longitude;
	// ArrayList<ContactListDto> contactListDtos = new
	// ArrayList<ContactListDto>();
	String country;
	String responseCode;

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getLattitude() {
		return lattitude;
	}

	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	// public ArrayList<ContactListDto> getContactListDtos() {
	// return contactListDtos;
	// }
	//
	// public void setContactListDtos(ArrayList<ContactListDto> contactListDtos)
	// {
	// this.contactListDtos = contactListDtos;
	// }

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
