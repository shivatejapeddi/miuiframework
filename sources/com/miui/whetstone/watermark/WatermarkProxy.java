package com.miui.whetstone.watermark;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.miui.whetstone.watermark.IWatermarkCallback.Stub;

public final class WatermarkProxy {
    private static final String TAG = "WatermarkProxy";
    private static WatermarkProxy sInstance = null;
    private Context mContext;

    private static class CallbackTransport extends Stub {
        private static final int TYPE_ON_DECODE = 2;
        private static final int TYPE_ON_ENCODE = 1;
        private final WatermarkCallback mCallback;
        private final Handler mCallbackHandler;

        CallbackTransport(WatermarkCallback callback, Looper looper) {
            this.mCallback = callback;
            if (looper == null) {
                this.mCallbackHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        CallbackTransport.this._handleMessage(msg);
                    }
                };
            } else {
                this.mCallbackHandler = new Handler(looper) {
                    public void handleMessage(Message msg) {
                        CallbackTransport.this._handleMessage(msg);
                    }
                };
            }
        }

        public void onEncodeWatermark(Bitmap bmp) {
            Log.d(WatermarkProxy.TAG, "watermark CallbackTransport onEncodeWatermark");
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = bmp;
            this.mCallbackHandler.sendMessage(msg);
        }

        public void onDecodeWatermark(String watermark) {
            Log.d(WatermarkProxy.TAG, "watermark CallbackTransport onDecodeWatermark");
            Message msg = Message.obtain();
            msg.what = 2;
            msg.obj = watermark;
            this.mCallbackHandler.sendMessage(msg);
        }

        private void _handleMessage(Message msg) {
            int i = msg.what;
            String str = WatermarkProxy.TAG;
            StringBuilder stringBuilder;
            if (i == 1) {
                Bitmap encodeBmp = msg.obj;
                stringBuilder = new StringBuilder();
                stringBuilder.append("watermark _handleMessage encode: ");
                stringBuilder.append(msg.what);
                Log.d(str, stringBuilder.toString());
                this.mCallback.onEncodeWatermarkDone(encodeBmp);
            } else if (i != 2) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("unknow msg type: ");
                stringBuilder2.append(msg.what);
                Log.w(str, stringBuilder2.toString());
            } else {
                String watermark = msg.obj;
                stringBuilder = new StringBuilder();
                stringBuilder.append("watermark _handleMessage decode: ");
                stringBuilder.append(msg.what);
                Log.d(str, stringBuilder.toString());
                this.mCallback.onDecodeWatermarkDone(watermark);
            }
        }
    }

    public static synchronized WatermarkProxy get(Context context) {
        WatermarkProxy watermarkProxy;
        synchronized (WatermarkProxy.class) {
            if (sInstance == null) {
                sInstance = new WatermarkProxy(context);
            }
            watermarkProxy = sInstance;
        }
        return watermarkProxy;
    }

    private WatermarkProxy(Context context) {
        this.mContext = context;
    }

    public void encodeWatermark(Bitmap bmp, String watermark, WatermarkCallback callback, Looper looper) {
        Log.d(TAG, "encodeWatermark");
    }

    public void decodeWatermark(Bitmap bmp, WatermarkCallback callback, Looper looper) {
        Log.d(TAG, "decodeWatermark");
    }
}
