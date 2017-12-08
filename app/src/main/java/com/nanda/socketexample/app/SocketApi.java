package com.nanda.socketexample.app;

import com.nanda.socketexample.data.response.userlist.ContactListResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface SocketApi {

    @GET(ApiConstants.API_CONTACTS + "{userId}")
    Observable<ContactListResponse> fetchContacts(@Path("userId") String id);

}
