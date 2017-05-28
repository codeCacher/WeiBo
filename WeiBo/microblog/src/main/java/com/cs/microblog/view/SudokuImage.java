package com.cs.microblog.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.cs.microblog.R;
import com.cs.microblog.custom.PictureInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/14.
 */

public class SudokuImage extends RelativeLayout {

    private View view;
    private ArrayList<String> imageUrls;
    private ArrayList<ImageWithType> ivList;
    private ArrayList<TableRow> trList;
    private WindowManager mWM;
    private int mScreenWidth;
    private int mScreenHeight;
    private TableLayout tl_root;
    private ViewGroup.LayoutParams layoutParams;
    private MarginLayoutParams marginLayoutParams;

    public SudokuImage(Context context) {
        this(context, null);
    }

    public SudokuImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SudokuImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
        boundBiew();
        initDate(context);
    }

    private void initDate(Context context) {
        mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mScreenHeight = mWM.getDefaultDisplay().getHeight();
        mScreenWidth = mWM.getDefaultDisplay().getWidth();

        imageUrls = new ArrayList<>();
    }

    private void boundBiew() {
        ivList = new ArrayList<>();
        trList = new ArrayList<>();
        ivList.clear();
        trList.clear();
        ivList.add((ImageWithType) findViewById(R.id.iv_0));
        ivList.add((ImageWithType) findViewById(R.id.iv_1));
        ivList.add((ImageWithType) findViewById(R.id.iv_2));
        ivList.add((ImageWithType) findViewById(R.id.iv_3));
        ivList.add((ImageWithType) findViewById(R.id.iv_4));
        ivList.add((ImageWithType) findViewById(R.id.iv_5));
        ivList.add((ImageWithType) findViewById(R.id.iv_6));
        ivList.add((ImageWithType) findViewById(R.id.iv_7));
        ivList.add((ImageWithType) findViewById(R.id.iv_8));
        trList.add((TableRow) findViewById(R.id.tr_0));
        trList.add((TableRow) findViewById(R.id.tr_1));
        trList.add((TableRow) findViewById(R.id.tr_2));
        tl_root = (TableLayout) findViewById(R.id.tl_root);
    }

    private void initView(Context context) {
        view = View.inflate(context, R.layout.sudoku_image, this);
    }

    public void setImageUrls(ArrayList<String> urls) {
        this.imageUrls = urls;
    }

    public void showImageUrls() {
        if(imageUrls != null) {
            setImageVisible();
            setImageDimension();
            loadImage();
        }
    }

    public void setImageVisible() {
        if(imageUrls != null) {
            setAllGone();
            for (int i = 0; i < imageUrls.size(); i++) {
                ivList.get(i).setVisibility(VISIBLE);
                //TODO
                ivList.get(i).clearImage();
            }
            switch (imageUrls.size()) {
                case 0:
                    break;
                case 1:
                case 2:
                case 3:
                    setRowVisible(0);
                    break;
                case 4:
                case 5:
                case 6:
                    setRowVisible(1);
                    break;
                case 7:
                case 8:
                case 9:
                    setRowVisible(2);
                    break;
            }

        }
    }

    private void setAllGone() {
        for (TableRow tr : trList) {
            tr.setVisibility(GONE);
        }
        for (ImageWithType sv : ivList) {
            sv.setVisibility(GONE);
            sv.setType(PictureInfo.PICTURE_NOMAL);
        }
    }

    private void setRowVisible(int lastVisibleRow) {
        for (int i = 0; i <= lastVisibleRow; i++) {
            trList.get(i).setVisibility(VISIBLE);
        }
    }

    public void loadImage() {
        if(imageUrls != null) {
            for (int i = 0; i < imageUrls.size(); i++) {
                ivList.get(i).loadImageUrl(getContext(), imageUrls.get(i));
            }
        }
    }

    public void setImageDimension() {
        if(imageUrls != null) {
            if (imageUrls.size() == 0) {
                return;
            }
            if (imageUrls.size() == 1) {
                layoutParams = ivList.get(0).getLayoutParams();
                marginLayoutParams = (MarginLayoutParams) ivList.get(0).getLayoutParams();
                layoutParams.width = (mScreenWidth - marginLayoutParams.leftMargin * 2 - tl_root.getPaddingLeft() * 2);
                layoutParams.height = mScreenWidth * 2 / 3;
                ivList.get(0).setLayoutParams(layoutParams);
            } else {
                layoutParams = ivList.get(0).getLayoutParams();
                marginLayoutParams = (MarginLayoutParams) ivList.get(0).getLayoutParams();
                layoutParams.width = (mScreenWidth - marginLayoutParams.leftMargin * 6 - tl_root.getPaddingLeft() * 2) / 3;
                layoutParams.height = (mScreenWidth - marginLayoutParams.leftMargin * 6 - tl_root.getPaddingLeft() * 2) / 3;
                for (int i = 0; i < imageUrls.size(); i++) {
                    ivList.get(i).setLayoutParams(layoutParams);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void OnItemClicked(int index,ArrayList<String> imageUrlList);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        for (ImageWithType item : ivList) {
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClicked(ivList.indexOf(v),imageUrls);
                }
            });
        }
    }
}