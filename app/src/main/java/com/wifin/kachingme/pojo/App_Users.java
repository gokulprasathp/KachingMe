package com.wifin.kachingme.pojo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class App_Users {

	public String uid, jid, email, name, status;
	public byte[] avatar;

}
