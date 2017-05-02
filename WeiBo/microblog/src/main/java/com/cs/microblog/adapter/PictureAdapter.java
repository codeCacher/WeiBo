package com.cs.microblog.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.cs.microblog.R;
import com.cs.microblog.custom.PicUrl;
import com.cs.microblog.view.AutoFitImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/1.
 */

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.MyHolder> {
    private Context context;
    private ArrayList<PicUrl> picUrls;
    private int picNumber;
    private RecyclerView parent;
    private Bitmap bitmap;

    PictureAdapter(Context context, ArrayList<PicUrl> picUrls, RecyclerView parent) {
        this.context = context;
        this.picUrls = picUrls;
        this.picNumber = picUrls.size();
        this.parent = parent;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(View.inflate(context, R.layout.blog_picture, null));
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        //get larg picture url
        final String url = picUrls.get(position).getThumbnail_pic();
        final String largeUrl = url.replace("thumbnail", "large");

        //get screen size
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final int screenWidth = windowManager.getDefaultDisplay().getWidth();

        //only when picNumber = 1,need to get the picture's size
        if (picNumber == 1) {

            //when get the pictur size,set the ImageView
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    ViewGroup.LayoutParams layoutParams = holder.getAfiv_picture().getLayoutParams();
                    if (bitmap.getWidth() >= bitmap.getHeight()) {
                        layoutParams.width = screenWidth * 2 / 3;
                        layoutParams.height = screenWidth / 2;
                    } else {
                        layoutParams.width = screenWidth / 2;
                        layoutParams.height = screenWidth * 2 / 3;
                    }
                    holder.getAfiv_picture().setLayoutParams(layoutParams);
                    Picasso.with(context).load(largeUrl).into(holder.getAfiv_picture());
                }
            };

            //asynchronous get the picture
            new Thread() {
                @Override
                public void run() {
                    try {
                        bitmap = Picasso.with(context).load(url).get();
                        handler.sendEmptyMessage(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            //when picNumber > 1,needn't get the picture size
            ViewGroup.LayoutParams layoutParams = holder.getAfiv_picture().getLayoutParams();
            layoutParams.width = (parent.getWidth() - holder.getLl_picture_root().getPaddingLeft() * 6) / 3;
            layoutParams.height = (parent.getWidth() - holder.getLl_picture_root().getPaddingLeft() * 6) / 3;
            Picasso.with(context).load(largeUrl).into(holder.getAfiv_picture());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return picUrls.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final AutoFitImageView afiv_picture;
        private final LinearLayout ll_picture_root;

        public MyHolder(View itemView) {
            super(itemView);
            afiv_picture = (AutoFitImageView) itemView.findViewById(R.id.afiv_picture);
            ll_picture_root = (LinearLayout) itemView.findViewById(R.id.ll_picture_root);
        }

        public AutoFitImageView getAfiv_picture() {
            return afiv_picture;
        }

        public LinearLayout getLl_picture_root() {
            return ll_picture_root;
        }
    }

    //Getter and Setter
    public ArrayList<PicUrl> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(ArrayList<PicUrl> picUrls) {
        this.picUrls = picUrls;
    }

}
