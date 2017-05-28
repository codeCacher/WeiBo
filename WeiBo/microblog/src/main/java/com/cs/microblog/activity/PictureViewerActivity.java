package com.cs.microblog.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.cs.microblog.R;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Supplier;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/23.
 */

public class PictureViewerActivity extends AppCompatActivity {

    private static final String TAG = "PictureViewerActivity";
    private ArrayList<String> image_urls;
    private ArrayList<String> mLargePicUrlList;
    private ViewPager vp_image_detail;
    private ArrayList<View> viewList;
    private MyPagerAdapter mMyPagerAdapter;
    private SparseArray<Animatable> animatableSparseArray;
    private int mClickIndex;
    private TextView tv_index;
    private WindowManager mWindowManager;
    private int mScreenWidth;
    private int mScreenHeight;
    private ArrayList<String> mThumbnailPicUrlList;
    private ImagePipeline mImagePipeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_viewer);
        bindView();
        getIntentExtra();
        initDate();
        initView();
    }

    private void initView() {
        initViewPager();
        tv_index.setText((mClickIndex + 1) + "/" + mLargePicUrlList.size());
        vp_image_detail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tv_index.setText((position + 1) + "/" + mLargePicUrlList.size());
                for (int i = 0; i < animatableSparseArray.size(); i++) {
                    if (i == position && animatableSparseArray.get(position) != null) {
                        animatableSparseArray.get(position).start();
                    }
                }
                SimpleDraweeView sdv_image_detail = (SimpleDraweeView) viewList.get(position).findViewById(R.id.sdv_image_detail);
                SubsamplingScaleImageView ssiv_image_detail = (SubsamplingScaleImageView) viewList.get(position).findViewById(R.id.ssiv_image_detail);

                loadPicture(sdv_image_detail, ssiv_image_detail, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void bindView() {
        vp_image_detail = (ViewPager) findViewById(R.id.vp_image_detail);
        tv_index = (TextView) findViewById(R.id.tv_index);
    }

    private void initDate() {
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = mWindowManager.getDefaultDisplay().getWidth();
        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();

        animatableSparseArray = new SparseArray<>();

        mImagePipeline = Fresco.getImagePipeline();
    }

    private void initViewPager() {
        viewList = new ArrayList<>();
        for (int i = 0; i < mLargePicUrlList.size(); i++) {
            viewList.add(View.inflate(this, R.layout.image_detail, null));
        }
        mMyPagerAdapter = new MyPagerAdapter();
        vp_image_detail.setAdapter(mMyPagerAdapter);
        vp_image_detail.setCurrentItem(mClickIndex);

        SimpleDraweeView sdv_image_detail = (SimpleDraweeView) viewList.get(mClickIndex).findViewById(R.id.sdv_image_detail);
        SubsamplingScaleImageView ssiv_image_detail = (SubsamplingScaleImageView) viewList.get(mClickIndex).findViewById(R.id.ssiv_image_detail);

        loadPicture(sdv_image_detail, ssiv_image_detail, mClickIndex);

        Fresco.getImagePipeline().resume();
    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        image_urls = (ArrayList<String>) intent.getSerializableExtra("image_urls");
        mLargePicUrlList = getLargePicUrlList();
        mThumbnailPicUrlList = getThumbnailPicUrlList();
        mClickIndex = intent.getIntExtra("clickIndex", 0);
    }

    public ArrayList<String> getLargePicUrlList() {
        ArrayList<String> largeUrls = new ArrayList<>();
        for (String url : image_urls) {
            largeUrls.add(url.replace("bmiddle", "large"));
        }
        return largeUrls;
    }

    public ArrayList<String> getThumbnailPicUrlList() {
        ArrayList<String> largeUrls = new ArrayList<>();
        for (String url : image_urls) {
            largeUrls.add(url.replace("bmiddle", "thumbnail"));
        }
        return largeUrls;
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mLargePicUrlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(viewList.get(position));
//            SimpleDraweeView sdv_image_detail = (SimpleDraweeView) viewList.get(position).findViewById(R.id.sdv_image_detail);
//            SubsamplingScaleImageView ssiv_image_detail = (SubsamplingScaleImageView) viewList.get(position).findViewById(R.id.ssiv_image_detail);
//
//            loadPicture(sdv_image_detail, ssiv_image_detail, position);
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

    }
    private void loadPicture(final SimpleDraweeView simpleDraweeView, final SubsamplingScaleImageView ssiv_image_detail, final int position) {
        if (mLargePicUrlList.get(position).endsWith(".gif")) {
            //show gif
            showGIF(simpleDraweeView, ssiv_image_detail, position);
        } else {
            //show normal picture and long picture
            showPicture(simpleDraweeView, ssiv_image_detail, position);
        }


    }

    private void showPicture(SimpleDraweeView simpleDraweeView, final SubsamplingScaleImageView ssiv_image_detail, final int position) {
        ssiv_image_detail.setVisibility(View.VISIBLE);
        simpleDraweeView.setVisibility(View.GONE);

        final ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(mLargePicUrlList.get(position)))
                .build();
        final ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(request, this);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onProgressUpdate(DataSource<CloseableReference<CloseableImage>> dataSource) {
                Log.i(TAG, position+"加载进度：" + dataSource.getProgress());
            }

            @Override
            protected void onNewResultImpl(final Bitmap bitmap) {
                int mBitmapWidth = bitmap.getWidth();
                final float mScale = 1f * mScreenWidth / mBitmapWidth;
                final PointF pointF = new PointF();
                pointF.set(mBitmapWidth / 2, 0);
                ssiv_image_detail.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
                ssiv_image_detail.setMinScale(mScale);
                ssiv_image_detail.setMaxScale(5);
                ImageRequest imageRequest = ImageRequest.fromUri(mLargePicUrlList.get(position));
                CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                        .getEncodedCacheKey(imageRequest, this);
                BinaryResource resource = ImagePipelineFactory.getInstance()
                        .getMainDiskStorageCache().getResource(cacheKey);
                final File file = ((FileBinaryResource) resource).getFile();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ssiv_image_detail.setImage(ImageSource.uri(file.getAbsolutePath()));
                        ssiv_image_detail.setScaleAndCenter(mScale, pointF);
                    }
                });
            }

            @Override
            protected void onFailureImpl(DataSource dataSource) {
            }
        }, CallerThreadExecutor.getInstance());
    }
    private void showGIF(SimpleDraweeView simpleDraweeView, SubsamplingScaleImageView ssiv_image_detail, final int position) {
        ssiv_image_detail.setVisibility(View.GONE);
        simpleDraweeView.setVisibility(View.VISIBLE);
        ControllerListener<ImageInfo> controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                if (anim != null) {
                    anim.start();
                    animatableSparseArray.put(position, anim);
                }
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
            }
        };

        class MyProgressBarDrawable extends ProgressBarDrawable {
            @Override
            protected boolean onLevelChange(int level) {
                Log.i(TAG, position + "加载进度：" + level);
                return true;
            }
        }

//        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setProgressBarImage(new MyProgressBarDrawable())
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setFadeDuration(0)
//                .setPlaceholderImage(new MyCustomDrawable())
//                .setBackgrounds(backgroundList)
//                .setOverlays(overlaysList)
                .build();
        simpleDraweeView.setHierarchy(hierarchy);

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(ImageRequest.fromUri(Uri.parse(mThumbnailPicUrlList.get(position))))
                .setImageRequest(ImageRequest.fromUri( Uri.parse(mLargePicUrlList.get(position))))
                .setControllerListener(controllerListener)
                .setOldController(simpleDraweeView.getController())
                .build();

        simpleDraweeView.setController(controller);
    }

}
