package com.nanda.socketexample.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nanda.socketexample.R;
import com.nanda.socketexample.data.response.userlist.UserListModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactsViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<UserListModel> userListModelList;
    private Set<Integer> selectedItemSet;

    public ContactListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        userListModelList = new ArrayList<>();
        selectedItemSet = new HashSet<>();
    }

    public void setUserListModelList(List<UserListModel> itemList) {
        if (itemList == null) {
            return;
        }

        userListModelList.clear();
        userListModelList.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_user, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        UserListModel model = userListModelList.get(position);
        holder.bindDataToview(model);
    }

    @Override
    public int getItemCount() {
        return userListModelList.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_mobile)
        TextView tvMobile;
        @BindView(R.id.tv_permission)
        TextView tvPermission;
        @BindView(R.id.tv_id)
        TextView tvId;
        @BindView(R.id.layout_contact_content)
        LinearLayout layoutContent;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindDataToview(UserListModel model) {

            layoutContent.setBackgroundColor(ContextCompat.getColor(context, model.isMultiSelect()
                    ? R.color.bg_grey : R.color.bg_white));

            tvName.setText(model.getName());
            tvMobile.setText(model.getMobile());
            tvPermission.setText(String.format("Permission: ", model.getPermission()));
            tvId.setText(model.getUserID());

        }

        @OnClick(R.id.layout_contact_content)
        public void onItemClicked() {
            int position = getAdapterPosition();

            if (position < 0)
                return;

            if (selectedItemSet.contains(position)) {
                selectedItemSet.remove(position);
                userListModelList.get(position).setMultiSelect(false);
            } else {
                selectedItemSet.add(position);
                userListModelList.get(position).setMultiSelect(true);
            }
            notifyDataSetChanged();
        }
    }

}
