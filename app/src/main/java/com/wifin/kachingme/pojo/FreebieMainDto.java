package com.wifin.kachingme.pojo;

import java.util.ArrayList;
import java.util.List;

public class FreebieMainDto {

	private String advertiserId;
    private String website;
    private String firstName;
    private String lastName;
    private String companyLogoPath;
    private String companyName;
    private List<FreebieDto> freebilist = new ArrayList<FreebieDto>();

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
