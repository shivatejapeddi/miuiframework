package com.miui.whetstone.watermark;

import android.graphics.Bitmap;

public interface WatermarkCallback {
    void onDecodeWatermarkDone(String str);

    void onEncodeWatermarkDone(Bitmap bitmap);
}
