package com.cs.microblog.global;

import android.app.Application;

import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * Created by Administrator on 2017/5/11.
 * to init the Fresco
 */

public class MyApplication extends Application {
    private static int MAX_MEM = 100 * ByteConstants.MB;
    private MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
            MAX_MEM,
            Integer.MAX_VALUE,
            MAX_MEM,
            Integer.MAX_VALUE,
            Integer.MAX_VALUE);
    Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
        @Override
        public MemoryCacheParams get() {
            return bitmapCacheParams;
        }
    };
    private ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(this)
            .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams).build();

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this, imagePipelineConfig);
    }
}
