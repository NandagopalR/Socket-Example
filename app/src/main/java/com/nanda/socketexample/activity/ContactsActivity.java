package com.nanda.socketexample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nanda.socketexample.R;
import com.nanda.socketexample.adapter.ContactListAdapter;
import com.nanda.socketexample.app.AppController;
import com.nanda.socketexample.base.BaseActivity;
import com.nanda.socketexample.data.repo.SocketRepo;
import com.nanda.socketexample.data.response.userlist.UserListModel;
import com.nanda.socketexample.utils.LoggerUtils;
import com.nanda.socketexample.utils.RxJavaUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class ContactsActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private boolean isLookup;
    private SocketRepo socketRepo;
    private ContactListAdapter adapter;
    private String userId = "5a2529212414ea5c5979ef82";

    public static Intent getCallingIntent(Context context, boolean isLookup) {
        return new Intent(context, ContactsActivity.class).putExtra("is_lookup", isLookup);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
        socketRepo = AppController.getInstance().getSocketRepo();

        if (getIntent().getExtras() != null) {
            isLookup = getIntent().getExtras().getBoolean("is_lookup");
        }

        adapter = new ContactListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);

        fetchContactList();
    }

    private void fetchContactList() {
        socketRepo.fetchContactList(userId)
                .compose(RxJavaUtils.applyObserverSchedulers())
                .subscribe(new Action1<List<UserListModel>>() {
                    @Override
                    public void call(List<UserListModel> userListModels) {
                        if (userListModels != null && userListModels.size() > 0) {
                            adapter.setUserListModelList(userListModels);
                        }
                    }
                }, throwable -> {
                    LoggerUtils.logUnExpectedException(throwable);
                });
    }
}
