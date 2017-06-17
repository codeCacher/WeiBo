package com.cs.microblog.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.cs.microblog.R;
import com.cs.microblog.bean.PictureInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/14.
 */

public class SudokuImage extends RelativeLayout {

    private static final String TAG = "SudokuImage";
    private View view;
    private ArrayList<ImageWithType> ivList;
    private ArrayList<TableRow> trList;
    private WindowManager mWM;
    private int mScreenWidth;
    private int mScreenHeight;
    private TableLayout tl_root;
    private ViewGroup.LayoutParams layoutParams;
    private MarginLayoutParams marginLayoutParams;
    public int maxRequestCount = 2;
    public int processingRequestCount = 0;
    private int doneRequestCount = 0;
    private boolean isStoped = false;
    private Handler handler;


    public void setMaxProcessRequestCount(int count){
        this.maxRequestCount = count;
    }
    public int getMaxProcessRequestCount(){
        return maxRequestCount;
    }

    public int getProcessingRequestCount(){
        return processingRequestCount;
    }
    public int getDoneRequestCount(){
        return doneRequestCount;
    }

    public void stop(){
        isStoped = true;
    }

    public void pause(){
        isStoped = true;
    }

    public void resume(){
        isStoped = false;
        if(handler==null){
            return;
        }
        for(int i=0;i<maxRequestCount;i++) {
            handler.sendEmptyMessage(0);
        }
    }

    public boolean isPaused(){
        return isStoped;
    }

    private class MyHandler extends Handler {
        private final ArrayList<String> imageUrls;

        MyHandler(ArrayList<String> imageUrls){
            this.imageUrls = imageUrls;
        }
        @Override
        public void handleMessage(Message msg) {
            if (processingRequestCount < maxRequestCount && doneRequestCount+processingRequestCount<imageUrls.size()&&!isStoped) {
                int index = processingRequestCount+doneRequestCount;
                processingRequestCount++;
                ivList.get(index).loadImageUrl(getContext(), imageUrls.get(index), new ImageWithType.CallBack() {
                    @Override
                    public void OnSuccess() {
                        Log.e(TAG, "first picture load success");
                        processingRequestCount--;
                        doneRequestCount++;
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void OnFailure() {
                        Log.e(TAG, "first picture load failure");
                        processingRequestCount--;
                        doneRequestCount++;
                        handler.sendEmptyMessage(0);
                    }
                });
            }
        }
    }

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

    public void setImageUrls(ArrayList<String> imageUrls) {
        if (imageUrls != null) {
            setImageVisible(imageUrls.size());
            setImageDimension(imageUrls.size());
            loadImage(imageUrls);
        }
    }

    public void setImageVisible(int size) {
        setAllGone();
        for (int i = 0; i < size; i++) {
            ivList.get(i).setVisibility(VISIBLE);
            //TODO
            ivList.get(i).clearImage();
        }
        switch (size) {
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

    /**
     * start load request
     * the most processing request count is controlled by maxRequestCount
     * @param imageUrls
     */
    public void loadImage(final ArrayList<String> imageUrls) {
        if (imageUrls != null) {
//            for (int i = 0; i < imageUrls.size(); i++) {
//                ivList.get(i).loadImageUrl(getContext(), imageUrls.get(i),null);
//            }
            if (imageUrls.size() == 0) {
                return;
            }
            handler = new MyHandler(imageUrls);
            processingRequestCount=0;
            doneRequestCount=0;
            isStoped = false;
            for(int i=0;i<maxRequestCount;i++){
                if(i<imageUrls.size()){
                    processingRequestCount++;
                    ivList.get(i).loadImageUrl(getContext(), imageUrls.get(i), new ImageWithType.CallBack() {
                        @Override
                        public void OnSuccess() {
                            Log.e(TAG, "first picture load success");
                            processingRequestCount--;
                            doneRequestCount++;
                            handler.sendEmptyMessage(0);
                        }

                        @Override
                        public void OnFailure() {
                            Log.e(TAG, "first picture load failure");
                            processingRequestCount--;
                            doneRequestCount++;
                            handler.sendEmptyMessage(0);
                        }
                    });
                }
            }

        }
    }

    public void setImageDimension(int size) {
        if (size == 0) {
            return;
        }
        if (size == 1) {
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
            for (int i = 0; i < size; i++) {
                ivList.get(i).setLayoutParams(layoutParams);
            }
        }
    }

    public interface OnItemClickListener {
        void OnItemClicked(int index, ArrayList<String> imageUrlList);
    }

    public void setOnItemClickListener(final ArrayList<String> imageUrls, final OnItemClickListener onItemClickListener) {
        for (ImageWithType item : ivList) {
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClicked(ivList.indexOf(v), imageUrls);
                }
            });
        }
    }
}