package com.nanda.socketexample.data.response.userlist;

import com.google.gson.annotations.SerializedName;

public class ContactListResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private ContactData contactData;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setContactData(ContactData contactData) {
        this.contactData = contactData;
    }

    public ContactData getContactData() {
        return contactData;
    }
}