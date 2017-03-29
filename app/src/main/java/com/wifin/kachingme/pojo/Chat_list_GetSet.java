package com.wifin.kachingme.pojo;

public class Chat_list_GetSet {

	int _id;
	int is_sec_chat;
	int unseen_msg_count;
	long timestamp;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getUnseen_msg_count() {
		return unseen_msg_count;
	}

	public void setUnseen_msg_count(int unseen_msg_count) {
		this.unseen_msg_count = unseen_msg_count;
	}

	public int getIs_sec_chat() {
		return is_sec_chat;
	}

	public void setIs_sec_chat(int is_sec_chat) {
		this.is_sec_chat = is_sec_chat;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getKey_remote_jid() {
		return key_remote_jid;
	}

	public void setKey_remote_jid(String key_remote_jid) {
		this.key_remote_jid = key_remote_jid;
	}

	public int getMessage_table_id() {
		return message_table_id;
	}

	public void setMessage_table_id(int message_table_id) {
		this.message_table_id = message_table_id;
	}

	String key_remote_jid;
	int message_table_id;

}
