package com.android.internal.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.SystemProperties;
import android.util.Log;

public class PointerLocationViewInjector {
    private static final String CUSTOM_TOUCH_STYLE_ENABLED = "debug.customtouchstyle.enabled";
    private static final String CUSTOM_TOUCH_STYLE_OVAL_SIZE = "debug.customtouchstyle.ovalsize";
    private static final String CUSTOM_TOUCH_STYLE_PAINT_COLOR = "debug.customtouchstyle.paintcolor";
    private static final String TAG = PointerLocationViewInjector.class.getSimpleName();
    private static boolean sCustomTouchStyleEnabled = SystemProperties.getBoolean(CUSTOM_TOUCH_STYLE_ENABLED, false);

    public static boolean isCustomTouchStyleEnabled() {
        return sCustomTouchStyleEnabled;
    }

    public static void drawOval(Canvas canvas, float x, float y, float major, float minor, float angle, Paint paint) {
        Style oldStyle = paint.getStyle();
        int oldColor = paint.getColor();
        float customMajor = major;
        float customMinor = minor;
        if (isCustomTouchStyleEnabled()) {
            paint.setStyle(Style.FILL);
            int customColor = SystemProperties.getInt(CUSTOM_TOUCH_STYLE_PAINT_COLOR, -1);
            if (customColor != -1) {
                paint.setColor(customColor);
            }
            String ovalSize = SystemProperties.get(CUSTOM_TOUCH_STYLE_OVAL_SIZE);
            if (!(ovalSize == null || ovalSize.isEmpty())) {
                try {
                    float customSize = Float.parseFloat(ovalSize);
                    customMajor = customSize;
                    customMinor = customSize;
                } catch (NumberFormatException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        canvas.save(1);
        canvas.rotate((float) (((double) (180.0f * angle)) / 3.141592653589793d), x, y);
        RectF ovalRect = new RectF();
        ovalRect.left = x - (customMinor / 2.0f);
        ovalRect.right = (customMinor / 2.0f) + x;
        ovalRect.top = y - (customMajor / 2.0f);
        ovalRect.bottom = (customMajor / 2.0f) + y;
        canvas.drawOval(ovalRect, paint);
        canvas.restore();
        paint.setStyle(oldStyle);
        paint.setColor(oldColor);
    }
}
