package com.wifin.kachingme.pojo;

import android.graphics.Bitmap;

public class ContactsGetSet {
    long last_seen;
    String nifty_name;
    private int id;
    private boolean is_checked = false;
    private String Nifty_email;
    private String jid;
    private int is_niftychat_user, isInContactList = 0;
    private String status;
    private String number;
    private String raw_contact_id;
    private String display_name;
    private String phone_type;
    private String phone_label;
    private int is_admin;
    private String profile_name;
    private int unseen_msg_count;
    private byte[] photo_ts;
    private Bitmap photo_bitmap;

    public Bitmap getPhoto_bitmap() {
        return photo_bitmap;
    }

    public void setPhoto_bitmap(Bitmap photo_bitmap) {
        this.photo_bitmap = photo_bitmap;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    public int getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(int is_admin) {
        this.is_admin = is_admin;
    }

    public boolean isIs_checked() {
        return is_checked;
    }

    public void setIs_checked(boolean is_checked) {
        this.is_checked = is_checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public int getIs_niftychat_user() {
        return is_niftychat_user;
    }

    public void setIs_niftychat_user(int is_niftychat_user) {
        this.is_niftychat_user = is_niftychat_user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRaw_contact_id() {
        return raw_contact_id;
    }

    public void setRaw_contact_id(String raw_contact_id) {
        this.raw_contact_id = raw_contact_id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getPhone_type() {
        return phone_type;
    }

    public void setPhone_type(String phone_type) {
        this.phone_type = phone_type;
    }

    public String getPhone_label() {
        return phone_label;
    }

    public void setPhone_label(String phone_label) {
        this.phone_label = phone_label;
    }

    public int getUnseen_msg_count() {
        return unseen_msg_count;
    }

    public void setUnseen_msg_count(int unseen_msg_count) {
        this.unseen_msg_count = unseen_msg_count;
    }

    public byte[] getPhoto_ts() {
        return photo_ts;
    }

    public void setPhoto_ts(byte[] photo_ts) {
        this.photo_ts = photo_ts;
    }

    public long getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(long last_seen) {
        this.last_seen = last_seen;
    }

    public String getNifty_name() {
        return nifty_name;
    }

    public void setNifty_name(String nifty_name) {
        this.nifty_name = nifty_name;
    }

    public String getNifty_email() {
        return Nifty_email;
    }

    public void setNifty_email(String nifty_email) {
        Nifty_email = nifty_email;
    }

    public int getIsInContactList() {
        return isInContactList;
    }

    public void setIsInContactList(int isInContactList) {
        this.isInContactList = isInContactList;
    }
}
