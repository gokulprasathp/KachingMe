package com.wifin.kachingme.pojo;

public class LoginGetSet {
	int id;
	String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	String UserName;
	String Password;
	String nifty_name, nifty_email;

	public String getNifty_name() {
		return nifty_name;
	}

	public void setNifty_name(String nifty_name) {
		this.nifty_name = nifty_name;
	}

	public String getNifty_email() {
		return nifty_email;
	}

	public void setNifty_email(String nifty_email) {
		this.nifty_email = nifty_email;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	byte[] avatar = null;

}
