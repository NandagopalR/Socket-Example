package com.nanda.socketexample.data.response.userlist;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ContactData {

    @SerializedName("rostersExists")
    private boolean rostersExists;

    @SerializedName("status")
    private String status;

    @SerializedName("queryResult")
    private List<UserListModel> userListModelList;

    public List<UserListModel> getUserListModelList() {
        return userListModelList;
    }

    public void setUserListModelList(List<UserListModel> userListModelList) {
        this.userListModelList = userListModelList;
    }

    public void setRostersExists(boolean rostersExists) {
        this.rostersExists = rostersExists;
    }

    public boolean isRostersExists() {
        return rostersExists;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}