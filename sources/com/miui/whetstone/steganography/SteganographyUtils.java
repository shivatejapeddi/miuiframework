package com.miui.whetstone.steganography;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;

public class SteganographyUtils {
    private static String TAG = "Whet_SteganographyUtils";

    public static Bitmap encodeWatermark(Bitmap bmp, String watermark) {
        Bitmap ret = null;
        if (bmp == null || TextUtils.isEmpty(watermark)) {
            return null;
        }
        try {
            ret = Steg.withInput(bmp).encode(watermark).intoBitmap();
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("encodeWatermark Exception e:");
            stringBuilder.append(e.getMessage());
            Log.w(str, stringBuilder.toString());
        }
        return ret;
    }

    public static Bitmap encodeWatermark(String bmpPath, String watermark) {
        Bitmap ret = null;
        if (TextUtils.isEmpty(bmpPath) || TextUtils.isEmpty(watermark)) {
            return null;
        }
        try {
            ret = Steg.withInput(bmpPath).encode(watermark).intoBitmap();
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("encodeWatermark Exception e:");
            stringBuilder.append(e.getMessage());
            Log.w(str, stringBuilder.toString());
        }
        return ret;
    }

    public static Bitmap encodeWatermark(File bmpFile, String watermark) {
        Bitmap ret = null;
        if (bmpFile == null || TextUtils.isEmpty(watermark)) {
            return null;
        }
        try {
            ret = Steg.withInput(bmpFile).encode(watermark).intoBitmap();
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("encodeWatermark Exception e:");
            stringBuilder.append(e.getMessage());
            Log.w(str, stringBuilder.toString());
        }
        return ret;
    }

    public static File encodeWatermark(File srcBmpFile, File desBmpFile, String watermark) {
        File ret = null;
        if (srcBmpFile == null || desBmpFile == null || TextUtils.isEmpty(watermark)) {
            return null;
        }
        try {
            ret = Steg.withInput(srcBmpFile).encode(watermark).intoFile(desBmpFile);
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("encodeWatermark Exception e:");
            stringBuilder.append(e.getMessage());
            Log.w(str, stringBuilder.toString());
        }
        return ret;
    }

    public static File encodeWatermark(Bitmap bmp, File desBmpFile, String watermark) {
        File ret = null;
        if (bmp == null || desBmpFile == null || TextUtils.isEmpty(watermark)) {
            return null;
        }
        try {
            ret = Steg.withInput(bmp).encode(watermark).intoFile(desBmpFile);
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("encodeWatermark Exception e:");
            stringBuilder.append(e.getMessage());
            Log.w(str, stringBuilder.toString());
        }
        return ret;
    }

    public static File encodeWatermark(String srcBmpPath, String desBmpPath, String watermark) {
        File ret = null;
        if (srcBmpPath == null || desBmpPath == null || TextUtils.isEmpty(watermark)) {
            return null;
        }
        try {
            ret = Steg.withInput(srcBmpPath).encode(watermark).intoFile(desBmpPath);
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("encodeWatermark Exception e:");
            stringBuilder.append(e.getMessage());
            Log.w(str, stringBuilder.toString());
        }
        return ret;
    }

    public static File encodeWatermark(Bitmap bmp, String desBmpPath, String watermark) {
        File ret = null;
        if (bmp == null || desBmpPath == null || TextUtils.isEmpty(watermark)) {
            return null;
        }
        try {
            ret = Steg.withInput(bmp).encode(watermark).intoFile(desBmpPath);
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("encodeWatermark Exception e:");
            stringBuilder.append(e.getMessage());
            Log.w(str, stringBuilder.toString());
        }
        return ret;
    }

    public static String decodeWatermark(Bitmap bmp) {
        String ret = null;
        if (bmp == null) {
            return null;
        }
        try {
            ret = Steg.withInput(bmp).decode().intoString();
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("decodeWatermark Exception e:");
            stringBuilder.append(e.getMessage());
            Log.w(str, stringBuilder.toString());
        }
        return ret;
    }

    public static String decodeWatermark(File bmpFile) {
        String ret = null;
        if (bmpFile == null) {
            return null;
        }
        try {
            ret = Steg.withInput(bmpFile).decode().intoString();
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("decodeWatermark Exception e:");
            stringBuilder.append(e.getMessage());
            Log.w(str, stringBuilder.toString());
        }
        return ret;
    }

    public static String decodeWatermark(String bmpPath) {
        String ret = null;
        if (TextUtils.isEmpty(bmpPath)) {
            return null;
        }
        try {
            ret = Steg.withInput(bmpPath).decode().intoString();
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("decodeWatermark Exception e:");
            stringBuilder.append(e.getMessage());
            Log.w(str, stringBuilder.toString());
        }
        return ret;
    }
}
