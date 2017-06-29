package com.cs.microblog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.bean.User;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/27.
 */

public class UserHomeAdapter extends RecyclerView.Adapter {

    private static final int ITEM_MORE = 1;
    private static final int ITEM_INFOR = 0;

    private Context mContext;
    private User mUser;

    public UserHomeAdapter(Context context, User user) {
        this.mContext = context;
        this.mUser = user;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_INFOR) {
            return new UserInfoVeiwHolder(LayoutInflater.from(mContext).inflate(R.layout.user_info_item, parent, false));
        } else {
            return new MoreInfoVeiwHolder(LayoutInflater.from(mContext).inflate(R.layout.user_more_info, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if(itemViewType ==ITEM_INFOR){
            UserInfoVeiwHolder userInfoVeiwHolder = (UserInfoVeiwHolder) holder;
            switch (position){
                case 0:
                    userInfoVeiwHolder.tvTitle.setText("所在地");
                    if(mUser!=null){
                        userInfoVeiwHolder.tvContent.setText(mUser.getLocation());
                    }
                    break;
                case 1:
                    userInfoVeiwHolder.tvTitle.setText("简介");
                    if(mUser!=null) {
                        userInfoVeiwHolder.tvContent.setText(mUser.getDescription());
                    }
                    break;
            }
        }else if(itemViewType ==ITEM_MORE){
            MoreInfoVeiwHolder moreInfoVeiwHolder = (MoreInfoVeiwHolder) holder;
            moreInfoVeiwHolder.tvMoreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到详细界面
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 2) {
            return ITEM_MORE;
        } else {
            return ITEM_INFOR;
        }
    }

    class UserInfoVeiwHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;

        public UserInfoVeiwHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MoreInfoVeiwHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_more_info)
        TextView tvMoreInfo;

        public MoreInfoVeiwHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
