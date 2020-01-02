package com.android.internal.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.ImageInfo;
import android.graphics.ImageDecoder.Source;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Size;

public class ImageUtils {
    private static final int ALPHA_TOLERANCE = 50;
    private static final int COMPACT_BITMAP_SIZE = 64;
    private static final int TOLERANCE = 20;
    private int[] mTempBuffer;
    private Bitmap mTempCompactBitmap;
    private Canvas mTempCompactBitmapCanvas;
    private Paint mTempCompactBitmapPaint;
    private final Matrix mTempMatrix = new Matrix();

    public boolean isGrayscale(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        if (height > 64 || width > 64) {
            if (this.mTempCompactBitmap == null) {
                this.mTempCompactBitmap = Bitmap.createBitmap(64, 64, Config.ARGB_8888);
                this.mTempCompactBitmapCanvas = new Canvas(this.mTempCompactBitmap);
                this.mTempCompactBitmapPaint = new Paint(1);
                this.mTempCompactBitmapPaint.setFilterBitmap(true);
            }
            this.mTempMatrix.reset();
            this.mTempMatrix.setScale(64.0f / ((float) width), 64.0f / ((float) height), 0.0f, 0.0f);
            this.mTempCompactBitmapCanvas.drawColor(0, Mode.SRC);
            this.mTempCompactBitmapCanvas.drawBitmap(bitmap, this.mTempMatrix, this.mTempCompactBitmapPaint);
            bitmap = this.mTempCompactBitmap;
            height = 64;
            width = 64;
        }
        int size = height * width;
        ensureBufferSize(size);
        bitmap.getPixels(this.mTempBuffer, 0, width, 0, 0, width, height);
        for (int i = 0; i < size; i++) {
            if (!isGrayscale(this.mTempBuffer[i])) {
                return false;
            }
        }
        return true;
    }

    private void ensureBufferSize(int size) {
        int[] iArr = this.mTempBuffer;
        if (iArr == null || iArr.length < size) {
            this.mTempBuffer = new int[size];
        }
    }

    public static boolean isGrayscale(int color) {
        boolean z = true;
        if (((color >> 24) & 255) < 50) {
            return true;
        }
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;
        if (Math.abs(r - g) >= 20 || Math.abs(r - b) >= 20 || Math.abs(g - b) >= 20) {
            z = false;
        }
        return z;
    }

    public static Bitmap buildScaledBitmap(Drawable drawable, int maxWidth, int maxHeight) {
        if (drawable == null) {
            return null;
        }
        int originalWidth = drawable.getIntrinsicWidth();
        int originalHeight = drawable.getIntrinsicHeight();
        if (originalWidth <= maxWidth && originalHeight <= maxHeight && (drawable instanceof BitmapDrawable)) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        if (originalHeight <= 0 || originalWidth <= 0) {
            return null;
        }
        float ratio = Math.min(1.0f, Math.min(((float) maxWidth) / ((float) originalWidth), ((float) maxHeight) / ((float) originalHeight)));
        int scaledWidth = (int) (((float) originalWidth) * ratio);
        int scaledHeight = (int) (((float) originalHeight) * ratio);
        Bitmap result = Bitmap.createBitmap(scaledWidth, scaledHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        drawable.setBounds(0, 0, scaledWidth, scaledHeight);
        drawable.draw(canvas);
        return result;
    }

    public static int calculateSampleSize(Size currentSize, Size requestedSize) {
        int inSampleSize = 1;
        if (currentSize.getHeight() > requestedSize.getHeight() || currentSize.getWidth() > requestedSize.getWidth()) {
            int halfHeight = currentSize.getHeight() / 2;
            int halfWidth = currentSize.getWidth() / 2;
            while (halfHeight / inSampleSize >= requestedSize.getHeight() && halfWidth / inSampleSize >= requestedSize.getWidth()) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /* JADX WARNING: Missing block: B:10:0x002d, code skipped:
            if (r0 != null) goto L_0x002f;
     */
    /* JADX WARNING: Missing block: B:12:?, code skipped:
            r0.close();
     */
    /* JADX WARNING: Missing block: B:13:0x0033, code skipped:
            r3 = move-exception;
     */
    /* JADX WARNING: Missing block: B:14:0x0034, code skipped:
            r1.addSuppressed(r3);
     */
    public static android.graphics.Bitmap loadThumbnail(android.content.ContentResolver r4, android.net.Uri r5, android.util.Size r6) throws java.io.IOException {
        /*
        r0 = r4.acquireContentProviderClient(r5);
        r1 = new android.os.Bundle;	 Catch:{ all -> 0x002a }
        r1.<init>();	 Catch:{ all -> 0x002a }
        r2 = "android.content.extra.SIZE";
        r3 = android.graphics.Point.convert(r6);	 Catch:{ all -> 0x002a }
        r1.putParcelable(r2, r3);	 Catch:{ all -> 0x002a }
        r2 = new com.android.internal.util.-$$Lambda$ImageUtils$UJyN8OeHYbkY_xJzm1U3D7W4PNY;	 Catch:{ all -> 0x002a }
        r2.<init>(r0, r5, r1);	 Catch:{ all -> 0x002a }
        r2 = android.graphics.ImageDecoder.createSource(r2);	 Catch:{ all -> 0x002a }
        r3 = new com.android.internal.util.-$$Lambda$ImageUtils$rnRZcgsdC1BtH9FpHTN2Kf_FXwE;	 Catch:{ all -> 0x002a }
        r3.<init>(r6);	 Catch:{ all -> 0x002a }
        r2 = android.graphics.ImageDecoder.decodeBitmap(r2, r3);	 Catch:{ all -> 0x002a }
        if (r0 == 0) goto L_0x0029;
    L_0x0026:
        r0.close();
    L_0x0029:
        return r2;
    L_0x002a:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x002c }
    L_0x002c:
        r2 = move-exception;
        if (r0 == 0) goto L_0x0037;
    L_0x002f:
        r0.close();	 Catch:{ all -> 0x0033 }
        goto L_0x0037;
    L_0x0033:
        r3 = move-exception;
        r1.addSuppressed(r3);
    L_0x0037:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.util.ImageUtils.loadThumbnail(android.content.ContentResolver, android.net.Uri, android.util.Size):android.graphics.Bitmap");
    }

    static /* synthetic */ void lambda$loadThumbnail$1(Size size, ImageDecoder decoder, ImageInfo info, Source source) {
        decoder.setAllocator(1);
        int sample = calculateSampleSize(info.getSize(), size);
        if (sample > 1) {
            decoder.setTargetSampleSize(sample);
        }
    }
}
