package com.wifin.kachingme.pojo;

public class UserListGetSet {

	String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	boolean isUser;

	public boolean getIsUser() {
		return isUser;
	}

	public void setIsUser(boolean isUser) {
		this.isUser = isUser;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	String id;
	String name;
	byte[] avtar;

	public byte[] getAvtar() {
		return avtar;
	}

	public void setAvtar(byte[] avtar) {
		this.avtar = avtar;
	}

}
