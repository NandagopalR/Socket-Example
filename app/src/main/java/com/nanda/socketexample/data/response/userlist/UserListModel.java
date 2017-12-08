package com.nanda.socketexample.data.response.userlist;

import com.google.gson.annotations.SerializedName;

public class UserListModel {

    @SerializedName("permission")
    private String permission;

    @SerializedName("name")
    private String name;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("userID")
    private String userID;

    private boolean isMultiSelect = false;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isMultiSelect() {
        return isMultiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        isMultiSelect = multiSelect;
    }
}