package com.cs.microblog.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.bean.PictureInfo;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

/**
 * Created by Administrator on 2017/5/14.
 */

public class ImageWithType extends RelativeLayout {

    private View view;
    private TextView tv_type;
    private SimpleDraweeView sdv_picture;
    private int screenHeight;
    private int screenWidth;
    private WindowManager windowManager;
    private int imageType;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            setType(msg.what);
        }
    };


    public ImageWithType(Context context) {
        this(context, null);
    }

    public ImageWithType(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageWithType(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        boundView();
        initDate();

    }

    private void initDate() {
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        screenHeight = windowManager.getDefaultDisplay().getHeight();
    }

    private void boundView() {
        sdv_picture = (SimpleDraweeView) findViewById(R.id.sdv_picture);
        tv_type = (TextView) findViewById(R.id.tv_type);
    }

    private void initView(Context context) {
        view = View.inflate(context, R.layout.image_with_type, this);
    }

    public void setType(int type) {
        imageType = type;

        switch (type) {
            case PictureInfo.PICTURE_GIF:
                tv_type.setVisibility(VISIBLE);
                tv_type.setText("动图");
                break;
            case PictureInfo.PICTURE_LONG:
                tv_type.setVisibility(VISIBLE);
                tv_type.setText("长图");
                break;
            case PictureInfo.PICTURE_NOMAL:
                tv_type.setVisibility(GONE);
                break;
        }
    }

    public void clearImage() {
        sdv_picture.setImageResource(R.color.colorGrayBlack);
    }

    interface CallBack{
        void OnSuccess();
        void OnFailure();
    }

    public void loadImageUrl(final Context context, final String url, final CallBack callBack) {
        Postprocessor longImagePostprocessor = new BasePostprocessor() {

            private CloseableReference<Bitmap> bitmapRef;

            @Override
            public String getName() {
                return "longImagePostprocessor";
            }
            @Override
            public CloseableReference<Bitmap> process(
                    Bitmap sourceBitmap,
                    PlatformBitmapFactory bitmapFactory) {

                int width = sourceBitmap.getWidth();
                int height = sourceBitmap.getHeight();

                Message msg = Message.obtain();

                if (url.endsWith(".gif")) {
                    imageType = PictureInfo.PICTURE_GIF;
                } else if (0.1f * height / width > 0.1f * screenHeight / screenWidth) {
                    imageType = PictureInfo.PICTURE_LONG;
                } else {
                    imageType = PictureInfo.PICTURE_NOMAL;
                }

                msg.what = imageType;
                handler.sendMessage(msg);


                if (imageType == PictureInfo.PICTURE_LONG) {
                    float scale = 1f * sdv_picture.getHeight()/sdv_picture.getWidth();
                    bitmapRef = bitmapFactory.createBitmap(
                            sourceBitmap.getWidth(),
                            (int) (sourceBitmap.getWidth()*scale));
                    try {
                        Bitmap destBitmap = bitmapRef.get();
                        for (int x = 0; x < sourceBitmap.getWidth(); x ++) {
                            for (int y = 0; y < (int) (sourceBitmap.getWidth()*scale); y ++) {
                                destBitmap.setPixel(x, y, sourceBitmap.getPixel(x, y));
                            }
                        }
                        return CloseableReference.cloneOrNull(bitmapRef);
                    } finally {
                        CloseableReference.closeSafely(bitmapRef);
                    }
                } else {
                    bitmapRef = bitmapFactory.createBitmap(
                            sourceBitmap.getWidth(),
                            sourceBitmap.getHeight(),
                            sourceBitmap.getConfig());
                    try {
                        process(bitmapRef.get(), sourceBitmap);
                        return CloseableReference.cloneOrNull(bitmapRef);
                    } finally {
                        CloseableReference.cloneOrNull(bitmapRef);
                    }
                }
            }
        };

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setPostprocessor(longImagePostprocessor)
                .build();

        BaseControllerListener<ImageInfo> controllerListener = new BaseControllerListener<ImageInfo>() {

            private int height;
            private int width;

            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                width = imageInfo.getWidth();
                height = imageInfo.getHeight();

                if (url.endsWith(".gif")) {
                    setType(PictureInfo.PICTURE_GIF);
                    if (anim != null) {
                        anim.start();
                    }
                } else if (imageType == PictureInfo.PICTURE_LONG) {
                    setType(PictureInfo.PICTURE_LONG);
                } else {
                    setType(PictureInfo.PICTURE_NOMAL);
                }
                callBack.OnSuccess();
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                callBack.OnFailure();
                throwable.printStackTrace();
            }
        };
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(sdv_picture.getController())
                .setControllerListener(controllerListener)
                .build();
        sdv_picture.setController(controller);
    }

}