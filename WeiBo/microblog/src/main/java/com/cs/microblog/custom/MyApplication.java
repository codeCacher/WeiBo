package com.cs.microblog.custom;

import android.app.Application;
import android.content.Context;

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
    private static int MAX_MEM = 50* ByteConstants.MB;

    private ImagePipelineConfig getConfigureCaches(Context context) {

        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(MAX_MEM, Integer.MAX_VALUE, MAX_MEM, Integer.MAX_VALUE, Integer.MAX_VALUE);
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };
        ImagePipelineConfig.Builder builder = ImagePipelineConfig.newBuilder(context);
        builder.setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams);
        return builder.build();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this, getConfigureCaches(this));
    }
}
