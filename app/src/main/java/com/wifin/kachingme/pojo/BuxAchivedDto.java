package com.wifin.kachingme.pojo;

import java.util.Map;

public class BuxAchivedDto {
	private String userId;
	private Map<String, String> buxsEarned;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, String> getBuxsEarned() {
		return buxsEarned;
	}

	public void setBuxsEarned(Map<String, String> buxsEarned) {
		this.buxsEarned = buxsEarned;
	}

}
