package com.wifin.kachingme.pojo;

import java.util.ArrayList;
import java.util.List;

public class FreebieMainDto {

	String advertiserId;
	String website;
	String firstName;
	String lastName;
	String companyLogoPath;
	String companyName;
	List<FreebieDto> freebilist = new ArrayList<FreebieDto>();

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(String advertiserId) {
		this.advertiserId = advertiserId;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCompanyLogoPath() {
		return companyLogoPath;
	}

	public void setCompanyLogoPath(String companyLogoPath) {
		this.companyLogoPath = companyLogoPath;
	}

	public List<FreebieDto> getFreebilist() {
		return freebilist;
	}

	public void setFreebilist(List<FreebieDto> freebilist) {
		this.freebilist = freebilist;
	}

}
