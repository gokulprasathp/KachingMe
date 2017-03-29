package com.wifin.kachingme.pojo;

import java.util.List;

/**
 * 
 * @author Sivanesan
 * 
 */
public class ContactSyncDto {

	private String name;
	private String contactNumber;
	private String uId;
	private String primaryContactNumber;
	private List<String> secondaryContactNumbers;
    private String status;
    private String profilePhoto;
    private String emailId;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getPrimaryContactNumber() {
		return primaryContactNumber;
	}

	public void setPrimaryContactNumber(String primaryContactNumber) {
		this.primaryContactNumber = primaryContactNumber;
	}

	public List<String> getSecondaryContactNumbers() {
		return secondaryContactNumbers;
	}

	public void setSecondaryContactNumbers(List<String> secondaryContactNumbers) {
		this.secondaryContactNumbers = secondaryContactNumbers;
	}

}
