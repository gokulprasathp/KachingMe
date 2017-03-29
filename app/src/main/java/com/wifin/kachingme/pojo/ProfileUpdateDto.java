package com.wifin.kachingme.pojo;

import android.graphics.Bitmap;

/**
 * Created by siva(wifin) on 30/12/2016
 */

public class ProfileUpdateDto {

    private  String phoneNumber;

    private  String profilePhoto;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
