package com.android.internal.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.os.SystemProperties;
import android.util.Log;
import android.util.TimedRemoteCaller;
import android.view.View;

public class TcpRttView extends View {
    private static final String NETD_RTT_PROPERY_KEY = "sys.netd.rtt";
    private static final String TAG = "TcpRtt";
    private int mHeaderBottom;
    private final Paint mTextBackgroundPaint;
    private final FontMetricsInt mTextMetrics = new FontMetricsInt();
    private final Paint mTextPaint;

    public TcpRttView(Context c) {
        super(c);
        setFocusableInTouchMode(true);
        this.mTextPaint = new Paint();
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextSize(getResources().getDisplayMetrics().density * 15.0f);
        this.mTextPaint.setARGB(255, 0, 0, 0);
        this.mTextBackgroundPaint = new Paint();
        this.mTextBackgroundPaint.setAntiAlias(false);
        this.mTextBackgroundPaint.setARGB(128, 255, 255, 255);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mTextPaint.getFontMetricsInt(this.mTextMetrics);
        this.mHeaderBottom = ((-this.mTextMetrics.ascent) + this.mTextMetrics.descent) + 2;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        int w = getWidth();
        int itemW = w / 7;
        int base = (-this.mTextMetrics.ascent) + 1;
        int bottom = this.mHeaderBottom;
        String rtt = SystemProperties.get(NETD_RTT_PROPERY_KEY, "0.0ms");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("rtt=");
        stringBuilder.append(rtt);
        Log.d(TAG, stringBuilder.toString());
        canvas.drawRect((float) (itemW * 5), 0.0f, (float) w, (float) bottom, this.mTextBackgroundPaint);
        canvas.drawText(rtt, (float) ((itemW * 5) + 1), (float) base, this.mTextPaint);
        postInvalidateDelayed(TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
    }
}
