package com.android.internal.app;

import android.animation.ObjectAnimator;
import android.animation.TimeAnimator;
import android.animation.TimeAnimator.TimeListener;
import android.app.Activity;
import android.app.slice.Slice;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings.System;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.internal.R;
import org.json.JSONObject;

public class PlatLogoActivity extends Activity {
    static final String TOUCH_STATS = "touch.stats";
    static final Paint sPaint = new Paint();
    BackslashDrawable mBackslash;
    int mClicks;
    ImageView mOneView;
    double mPressureMax = -1.0d;
    double mPressureMin = 0.0d;
    ImageView mZeroView;

    private static class BackslashDrawable extends Drawable implements TimeListener {
        TimeAnimator mAnimator = new TimeAnimator();
        Matrix mMatrix = new Matrix();
        Paint mPaint = new Paint();
        BitmapShader mShader;
        Bitmap mTile;

        public void draw(Canvas canvas) {
            canvas.drawPaint(this.mPaint);
        }

        BackslashDrawable(int width) {
            this.mTile = Bitmap.createBitmap(width, width, Config.ALPHA_8);
            this.mAnimator.setTimeListener(this);
            Canvas tileCanvas = new Canvas(this.mTile);
            float w = (float) tileCanvas.getWidth();
            float h = (float) tileCanvas.getHeight();
            Path path = new Path();
            path.moveTo(0.0f, 0.0f);
            path.lineTo(w / 2.0f, 0.0f);
            path.lineTo(w, h / 2.0f);
            path.lineTo(w, h);
            path.close();
            path.moveTo(0.0f, h / 2.0f);
            path.lineTo(w / 2.0f, h);
            path.lineTo(0.0f, h);
            path.close();
            Paint slashPaint = new Paint();
            slashPaint.setAntiAlias(true);
            slashPaint.setStyle(Style.FILL);
            slashPaint.setColor(-16777216);
            tileCanvas.drawPath(path, slashPaint);
            this.mShader = new BitmapShader(this.mTile, TileMode.REPEAT, TileMode.REPEAT);
            this.mPaint.setShader(this.mShader);
        }

        public void startAnimating() {
            if (!this.mAnimator.isStarted()) {
                this.mAnimator.start();
            }
        }

        public void stopAnimating() {
            if (this.mAnimator.isStarted()) {
                this.mAnimator.cancel();
            }
        }

        public void setAlpha(int alpha) {
            this.mPaint.setAlpha(alpha);
        }

        public void setColorFilter(ColorFilter colorFilter) {
            this.mPaint.setColorFilter(colorFilter);
        }

        public int getOpacity() {
            return -3;
        }

        public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
            if (this.mShader != null) {
                this.mMatrix.postTranslate(((float) deltaTime) / 4.0f, 0.0f);
                this.mShader.setLocalMatrix(this.mMatrix);
                invalidateSelf();
            }
        }
    }

    static class OneDrawable extends Drawable {
        int mTintColor;

        OneDrawable() {
        }

        public void draw(Canvas canvas) {
            PlatLogoActivity.sPaint.setColor(this.mTintColor | -16777216);
            canvas.save();
            canvas.scale(((float) canvas.getWidth()) / 24.0f, ((float) canvas.getHeight()) / 24.0f);
            Path p = new Path();
            p.moveTo(12.0f, 21.83f);
            p.rLineTo(0.0f, -19.67f);
            p.rLineTo(-5.0f, 0.0f);
            canvas.drawPath(p, PlatLogoActivity.sPaint);
            canvas.restore();
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }

        public void setTintList(ColorStateList tint) {
            this.mTintColor = tint.getDefaultColor();
        }

        public int getOpacity() {
            return -3;
        }
    }

    static class ZeroDrawable extends Drawable {
        int mTintColor;

        ZeroDrawable() {
        }

        public void draw(Canvas canvas) {
            PlatLogoActivity.sPaint.setColor(this.mTintColor | -16777216);
            canvas.save();
            canvas.scale(((float) canvas.getWidth()) / 24.0f, ((float) canvas.getHeight()) / 24.0f);
            canvas.drawCircle(12.0f, 12.0f, 10.0f, PlatLogoActivity.sPaint);
            canvas.restore();
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }

        public void setTintList(ColorStateList tint) {
            this.mTintColor = tint.getDefaultColor();
        }

        public int getOpacity() {
            return -3;
        }
    }

    static {
        sPaint.setStyle(Style.STROKE);
        sPaint.setStrokeWidth(4.0f);
        sPaint.setStrokeCap(Cap.SQUARE);
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        BackslashDrawable backslashDrawable = this.mBackslash;
        if (backslashDrawable != null) {
            backslashDrawable.stopAnimating();
        }
        this.mClicks = 0;
        super.onPause();
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        float dp = getResources().getDisplayMetrics().density;
        getWindow().getDecorView().setSystemUiVisibility(768);
        getWindow().setNavigationBarColor(0);
        getWindow().setStatusBarColor(0);
        getActionBar().hide();
        setContentView((int) R.layout.platlogo_layout);
        this.mBackslash = new BackslashDrawable((int) (50.0f * dp));
        this.mOneView = (ImageView) findViewById(R.id.one);
        this.mOneView.setImageDrawable(new OneDrawable());
        this.mZeroView = (ImageView) findViewById(R.id.zero);
        this.mZeroView.setImageDrawable(new ZeroDrawable());
        ViewGroup root = (ViewGroup) this.mOneView.getParent();
        root.setClipChildren(false);
        root.setBackground(this.mBackslash);
        root.getBackground().setAlpha(32);
        OnTouchListener tl = new OnTouchListener() {
            long mClickTime;
            float mOffsetX;
            float mOffsetY;
            ObjectAnimator mRotAnim;

            /* JADX WARNING: Missing block: B:5:0x0012, code skipped:
            if (r0 != 3) goto L_0x00b5;
     */
            public boolean onTouch(android.view.View r10, android.view.MotionEvent r11) {
                /*
                r9 = this;
                r0 = com.android.internal.app.PlatLogoActivity.this;
                r0.measureTouchPressure(r11);
                r0 = r11.getActionMasked();
                r1 = 2;
                r2 = 1;
                if (r0 == 0) goto L_0x004e;
            L_0x000d:
                if (r0 == r2) goto L_0x0031;
            L_0x000f:
                if (r0 == r1) goto L_0x0016;
            L_0x0011:
                r1 = 3;
                if (r0 == r1) goto L_0x0034;
            L_0x0014:
                goto L_0x00b5;
            L_0x0016:
                r0 = r11.getRawX();
                r1 = r9.mOffsetX;
                r0 = r0 - r1;
                r10.setX(r0);
                r0 = r11.getRawY();
                r1 = r9.mOffsetY;
                r0 = r0 - r1;
                r10.setY(r0);
                r0 = 9;
                r10.performHapticFeedback(r0);
                goto L_0x00b5;
            L_0x0031:
                r10.performClick();
            L_0x0034:
                r0 = r10.animate();
                r1 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
                r0 = r0.scaleX(r1);
                r0.scaleY(r1);
                r0 = r9.mRotAnim;
                if (r0 == 0) goto L_0x0048;
            L_0x0045:
                r0.cancel();
            L_0x0048:
                r0 = com.android.internal.app.PlatLogoActivity.this;
                r0.testOverlap();
                goto L_0x00b5;
            L_0x004e:
                r0 = r10.animate();
                r3 = 1066192077; // 0x3f8ccccd float:1.1 double:5.26768877E-315;
                r0 = r0.scaleX(r3);
                r0.scaleY(r3);
                r0 = r10.getParent();
                r0.bringChildToFront(r10);
                r0 = r11.getRawX();
                r3 = r10.getX();
                r0 = r0 - r3;
                r9.mOffsetX = r0;
                r0 = r11.getRawY();
                r3 = r10.getY();
                r0 = r0 - r3;
                r9.mOffsetY = r0;
                r3 = java.lang.System.currentTimeMillis();
                r5 = r9.mClickTime;
                r5 = r3 - r5;
                r7 = 350; // 0x15e float:4.9E-43 double:1.73E-321;
                r0 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
                if (r0 >= 0) goto L_0x00b2;
            L_0x0087:
                r0 = android.view.View.ROTATION;
                r1 = new float[r1];
                r5 = 0;
                r6 = r10.getRotation();
                r1[r5] = r6;
                r5 = r10.getRotation();
                r6 = 1163984896; // 0x45610000 float:3600.0 double:5.750849494E-315;
                r5 = r5 + r6;
                r1[r2] = r5;
                r0 = android.animation.ObjectAnimator.ofFloat(r10, r0, r1);
                r9.mRotAnim = r0;
                r0 = r9.mRotAnim;
                r5 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
                r0.setDuration(r5);
                r0 = r9.mRotAnim;
                r0.start();
                r0 = 0;
                r9.mClickTime = r0;
                goto L_0x00b5;
            L_0x00b2:
                r9.mClickTime = r3;
            L_0x00b5:
                return r2;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.app.PlatLogoActivity$AnonymousClass1.onTouch(android.view.View, android.view.MotionEvent):boolean");
            }
        };
        findViewById(R.id.one).setOnTouchListener(tl);
        findViewById(R.id.zero).setOnTouchListener(tl);
        findViewById(R.id.text).setOnTouchListener(tl);
    }

    private void testOverlap() {
        float width = (float) this.mZeroView.getWidth();
        if (Math.hypot((double) ((this.mZeroView.getX() + (width * 0.2f)) - this.mOneView.getX()), (double) ((this.mZeroView.getY() + (width * 0.3f)) - this.mOneView.getY())) >= ((double) (width * 0.2f)) || Math.abs((this.mOneView.getRotation() % 360.0f) - 315.0f) >= 15.0f) {
            this.mBackslash.stopAnimating();
            return;
        }
        this.mOneView.animate().x(this.mZeroView.getX() + (0.2f * width));
        this.mOneView.animate().y(this.mZeroView.getY() + (0.3f * width));
        ImageView imageView = this.mOneView;
        imageView.setRotation(imageView.getRotation() % 360.0f);
        this.mOneView.animate().rotation(315.0f);
        this.mOneView.performHapticFeedback(16);
        this.mBackslash.startAnimating();
        this.mClicks++;
        if (this.mClicks >= 7) {
            launchNextStage();
        }
    }

    private void launchNextStage() {
        ContentResolver cr = getContentResolver();
        String str = System.EGG_MODE;
        String str2 = "com.android.internal.app.PlatLogoActivity";
        if (System.getLong(cr, str, 0) == 0) {
            try {
                System.putLong(cr, str, System.currentTimeMillis());
            } catch (RuntimeException e) {
                Log.e(str2, "Can't write settings", e);
            }
        }
        try {
            startActivity(new Intent(Intent.ACTION_MAIN).setFlags(268468224).addCategory("com.android.internal.category.PLATLOGO"));
        } catch (ActivityNotFoundException e2) {
            Log.e(str2, "No more eggs.");
        }
        finish();
    }

    private void measureTouchPressure(MotionEvent event) {
        float pressure = event.getPressure();
        int actionMasked = event.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked == 2) {
                if (((double) pressure) < this.mPressureMin) {
                    this.mPressureMin = (double) pressure;
                }
                if (((double) pressure) > this.mPressureMax) {
                    this.mPressureMax = (double) pressure;
                }
            }
        } else if (this.mPressureMax < 0.0d) {
            double d = (double) pressure;
            this.mPressureMax = d;
            this.mPressureMin = d;
        }
    }

    private void syncTouchPressure() {
        String str = TOUCH_STATS;
        String str2 = Slice.SUBTYPE_MAX;
        String str3 = "min";
        try {
            String touchDataJson = System.getString(getContentResolver(), str);
            JSONObject touchData = new JSONObject(touchDataJson != null ? touchDataJson : "{}");
            if (touchData.has(str3)) {
                this.mPressureMin = Math.min(this.mPressureMin, touchData.getDouble(str3));
            }
            if (touchData.has(str2)) {
                this.mPressureMax = Math.max(this.mPressureMax, touchData.getDouble(str2));
            }
            if (this.mPressureMax >= 0.0d) {
                touchData.put(str3, this.mPressureMin);
                touchData.put(str2, this.mPressureMax);
                System.putString(getContentResolver(), str, touchData.toString());
            }
        } catch (Exception e) {
            Log.e("com.android.internal.app.PlatLogoActivity", "Can't write touch settings", e);
        }
    }

    public void onStart() {
        super.onStart();
        syncTouchPressure();
    }

    public void onStop() {
        syncTouchPressure();
        super.onStop();
    }
}
