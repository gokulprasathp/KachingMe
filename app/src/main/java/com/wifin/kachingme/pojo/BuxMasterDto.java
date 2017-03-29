package com.wifin.kachingme.pojo;

/**
 * Created by Siva(Wifin) on 27-03-2017.
 */
public class BuxMasterDto {

    private Integer buxMasterId;
    private String activity;
    private Integer buxsForActivity;

    public Integer getBuxMasterId() {
        return buxMasterId;
    }

    public void setBuxMasterId(Integer buxMasterId) {
        this.buxMasterId = buxMasterId;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Integer getBuxsForActivity() {
        return buxsForActivity;
    }

    public void setBuxsForActivity(Integer buxsForActivity) {
        this.buxsForActivity = buxsForActivity;
    }
}
