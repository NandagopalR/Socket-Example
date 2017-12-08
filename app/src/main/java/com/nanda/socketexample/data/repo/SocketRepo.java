package com.nanda.socketexample.data.repo;

import com.nanda.socketexample.app.SocketApi;
import com.nanda.socketexample.data.response.userlist.UserListModel;
import com.nanda.socketexample.utils.LoggerUtils;
import com.nanda.socketexample.utils.RxJavaUtils;

import java.util.List;

import rx.Observable;

public class SocketRepo {

    private SocketApi api;

    public SocketRepo(SocketApi api) {
        this.api = api;
    }

    public Observable<List<UserListModel>> fetchContactList(String userId) {
        return api.fetchContacts(userId)
                .compose(RxJavaUtils.applyErrorTransformer())
                .map(response -> response != null && response.getContactData() != null ? response.getContactData().getUserListModelList() : null)
                .doOnError(throwable -> LoggerUtils.logUnExpectedException(throwable));

    }
}
