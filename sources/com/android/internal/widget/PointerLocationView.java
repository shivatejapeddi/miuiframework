package com.android.internal.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.hardware.input.InputManager;
import android.hardware.input.InputManager.InputDeviceListener;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Slog;
import android.view.ISystemGestureExclusionListener;
import android.view.ISystemGestureExclusionListener.Stub;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.VelocityTracker;
import android.view.VelocityTracker.Estimator;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowInsets;
import android.view.WindowManagerGlobal;
import android.view.WindowManagerPolicyConstants.PointerEventListener;
import java.util.ArrayList;

public class PointerLocationView extends View implements InputDeviceListener, PointerEventListener {
    private static final String ALT_STRATEGY_PROPERY_KEY = "debug.velocitytracker.alt";
    private static final String TAG = "Pointer";
    private int mActivePointerId;
    private final VelocityTracker mAltVelocity;
    @UnsupportedAppUsage
    private boolean mCurDown;
    @UnsupportedAppUsage
    private int mCurNumPointers;
    private final Paint mCurrentPointPaint;
    private int mHeaderBottom;
    private int mHeaderPaddingTop = 0;
    private final InputManager mIm;
    @UnsupportedAppUsage
    private int mMaxNumPointers;
    private final Paint mPaint;
    private final Paint mPathPaint;
    @UnsupportedAppUsage
    private final ArrayList<PointerState> mPointers = new ArrayList();
    @UnsupportedAppUsage
    private boolean mPrintCoords = true;
    private RectF mReusableOvalRect = new RectF();
    private final Region mSystemGestureExclusion = new Region();
    private ISystemGestureExclusionListener mSystemGestureExclusionListener = new Stub() {
        public void onSystemGestureExclusionChanged(int displayId, Region systemGestureExclusion) {
            Region exclusion = Region.obtain(systemGestureExclusion);
            Handler handler = PointerLocationView.this.getHandler();
            if (handler != null) {
                handler.post(new -$$Lambda$PointerLocationView$1$utsjc18145VWAe5S9LSLblHeqxc(this, exclusion));
            }
        }

        public /* synthetic */ void lambda$onSystemGestureExclusionChanged$0$PointerLocationView$1(Region exclusion) {
            PointerLocationView.this.mSystemGestureExclusion.set(exclusion);
            exclusion.recycle();
            PointerLocationView.this.invalidate();
        }
    };
    private final Paint mSystemGestureExclusionPaint;
    private final Path mSystemGestureExclusionPath = new Path();
    private final Paint mTargetPaint;
    private final PointerCoords mTempCoords = new PointerCoords();
    private final FasterStringBuilder mText = new FasterStringBuilder();
    private final Paint mTextBackgroundPaint;
    private final Paint mTextLevelPaint;
    private final FontMetricsInt mTextMetrics = new FontMetricsInt();
    private final Paint mTextPaint;
    private final ViewConfiguration mVC;
    private final VelocityTracker mVelocity;

    private static final class FasterStringBuilder {
        private char[] mChars = new char[64];
        private int mLength;

        public FasterStringBuilder clear() {
            this.mLength = 0;
            return this;
        }

        public FasterStringBuilder append(String value) {
            int valueLength = value.length();
            value.getChars(0, valueLength, this.mChars, reserve(valueLength));
            this.mLength += valueLength;
            return this;
        }

        public FasterStringBuilder append(int value) {
            return append(value, 0);
        }

        public FasterStringBuilder append(int value, int zeroPadWidth) {
            boolean negative = value < 0;
            if (negative) {
                value = -value;
                if (value < 0) {
                    append("-2147483648");
                    return this;
                }
            }
            int index = reserve(11);
            char[] chars = this.mChars;
            int index2;
            if (value == 0) {
                index2 = index + 1;
                chars[index] = '0';
                this.mLength++;
                return this;
            }
            int i;
            int index3;
            if (negative) {
                i = index + 1;
                chars[index] = '-';
            } else {
                i = index;
            }
            index = 1000000000;
            index2 = 10;
            while (value < index) {
                index /= 10;
                index2--;
                if (index2 < zeroPadWidth) {
                    index3 = i + 1;
                    chars[i] = '0';
                    i = index3;
                }
            }
            while (true) {
                int digit = value / index;
                value -= digit * index;
                index /= 10;
                index3 = i + 1;
                chars[i] = (char) (digit + 48);
                if (index == 0) {
                    this.mLength = index3;
                    return this;
                }
                i = index3;
            }
        }

        public FasterStringBuilder append(float value, int precision) {
            int scale = 1;
            for (int i = 0; i < precision; i++) {
                scale *= 10;
            }
            value = (float) (Math.rint((double) (((float) scale) * value)) / ((double) scale));
            if (((int) value) == 0 && value < 0.0f) {
                append("-");
            }
            append((int) value);
            if (precision != 0) {
                append(".");
                value = Math.abs(value);
                append((int) (((float) scale) * ((float) (((double) value) - Math.floor((double) value)))), precision);
            }
            return this;
        }

        public String toString() {
            return new String(this.mChars, 0, this.mLength);
        }

        private int reserve(int length) {
            int oldLength = this.mLength;
            int newLength = this.mLength + length;
            char[] oldChars = this.mChars;
            int oldCapacity = oldChars.length;
            if (newLength > oldCapacity) {
                char[] newChars = new char[(oldCapacity * 2)];
                System.arraycopy(oldChars, 0, newChars, 0, oldLength);
                this.mChars = newChars;
            }
            return oldLength;
        }
    }

    public static class PointerState {
        private Estimator mAltEstimator = new Estimator();
        private float mAltXVelocity;
        private float mAltYVelocity;
        private float mBoundingBottom;
        private float mBoundingLeft;
        private float mBoundingRight;
        private float mBoundingTop;
        private PointerCoords mCoords = new PointerCoords();
        @UnsupportedAppUsage
        private boolean mCurDown;
        private Estimator mEstimator = new Estimator();
        private boolean mHasBoundingBox;
        private int mToolType;
        private int mTraceCount;
        private boolean[] mTraceCurrent = new boolean[32];
        private float[] mTraceX = new float[32];
        private float[] mTraceY = new float[32];
        private float mXVelocity;
        private float mYVelocity;

        public void clearTrace() {
            this.mTraceCount = 0;
        }

        public void addTrace(float x, float y, boolean current) {
            float[] fArr = this.mTraceX;
            int traceCapacity = fArr.length;
            int i = this.mTraceCount;
            if (i == traceCapacity) {
                traceCapacity *= 2;
                float[] newTraceX = new float[traceCapacity];
                System.arraycopy(fArr, 0, newTraceX, 0, i);
                this.mTraceX = newTraceX;
                fArr = new float[traceCapacity];
                System.arraycopy(this.mTraceY, 0, fArr, 0, this.mTraceCount);
                this.mTraceY = fArr;
                boolean[] newTraceCurrent = new boolean[traceCapacity];
                System.arraycopy(this.mTraceCurrent, 0, newTraceCurrent, 0, this.mTraceCount);
                this.mTraceCurrent = newTraceCurrent;
            }
            fArr = this.mTraceX;
            i = this.mTraceCount;
            fArr[i] = x;
            this.mTraceY[i] = y;
            this.mTraceCurrent[i] = current;
            this.mTraceCount = i + 1;
        }
    }

    public PointerLocationView(Context c) {
        super(c);
        setFocusableInTouchMode(true);
        this.mIm = (InputManager) c.getSystemService(InputManager.class);
        this.mVC = ViewConfiguration.get(c);
        this.mTextPaint = new Paint();
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextSize(getResources().getDisplayMetrics().density * 10.0f);
        this.mTextPaint.setARGB(255, 0, 0, 0);
        this.mTextBackgroundPaint = new Paint();
        this.mTextBackgroundPaint.setAntiAlias(false);
        this.mTextBackgroundPaint.setARGB(128, 255, 255, 255);
        this.mTextLevelPaint = new Paint();
        this.mTextLevelPaint.setAntiAlias(false);
        this.mTextLevelPaint.setARGB(192, 255, 0, 0);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setARGB(255, 255, 255, 255);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth(2.0f);
        this.mCurrentPointPaint = new Paint();
        this.mCurrentPointPaint.setAntiAlias(true);
        this.mCurrentPointPaint.setARGB(255, 255, 0, 0);
        this.mCurrentPointPaint.setStyle(Style.STROKE);
        this.mCurrentPointPaint.setStrokeWidth(2.0f);
        this.mTargetPaint = new Paint();
        this.mTargetPaint.setAntiAlias(false);
        this.mTargetPaint.setARGB(255, 0, 0, 192);
        this.mPathPaint = new Paint();
        this.mPathPaint.setAntiAlias(false);
        this.mPathPaint.setARGB(255, 0, 96, 255);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth(1.0f);
        this.mSystemGestureExclusionPaint = new Paint();
        this.mSystemGestureExclusionPaint.setARGB(25, 255, 0, 0);
        this.mSystemGestureExclusionPaint.setStyle(Style.FILL_AND_STROKE);
        this.mPointers.add(new PointerState());
        this.mActivePointerId = 0;
        this.mVelocity = VelocityTracker.obtain();
        String altStrategy = SystemProperties.get(ALT_STRATEGY_PROPERY_KEY);
        if (altStrategy.length() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Comparing default velocity tracker strategy with ");
            stringBuilder.append(altStrategy);
            Log.d(TAG, stringBuilder.toString());
            this.mAltVelocity = VelocityTracker.obtain(altStrategy);
            return;
        }
        this.mAltVelocity = null;
    }

    public void setPrintCoords(boolean state) {
        this.mPrintCoords = state;
    }

    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (insets.getDisplayCutout() != null) {
            this.mHeaderPaddingTop = insets.getDisplayCutout().getSafeInsetTop();
        } else {
            this.mHeaderPaddingTop = 0;
        }
        return super.onApplyWindowInsets(insets);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mTextPaint.getFontMetricsInt(this.mTextMetrics);
        this.mHeaderBottom = ((this.mHeaderPaddingTop - this.mTextMetrics.ascent) + this.mTextMetrics.descent) + 2;
    }

    private void drawOval(Canvas canvas, float x, float y, float major, float minor, float angle, Paint paint) {
        canvas.save(1);
        canvas.rotate((float) (((double) (180.0f * angle)) / 3.141592653589793d), x, y);
        RectF rectF = this.mReusableOvalRect;
        rectF.left = x - (minor / 2.0f);
        rectF.right = (minor / 2.0f) + x;
        rectF.top = y - (major / 2.0f);
        rectF.bottom = (major / 2.0f) + y;
        canvas.drawOval(rectF, paint);
        canvas.restore();
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0599  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x057b  */
    public void onDraw(android.graphics.Canvas r29) {
        /*
        r28 = this;
        r8 = r28;
        r9 = r29;
        r10 = r28.getWidth();
        r11 = r10 / 7;
        r0 = r8.mHeaderPaddingTop;
        r1 = r8.mTextMetrics;
        r1 = r1.ascent;
        r0 = r0 - r1;
        r6 = 1;
        r12 = r0 + 1;
        r13 = r8.mHeaderBottom;
        r0 = r8.mPointers;
        r14 = r0.size();
        r0 = r8.mSystemGestureExclusion;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x0037;
    L_0x0024:
        r0 = r8.mSystemGestureExclusionPath;
        r0.reset();
        r0 = r8.mSystemGestureExclusion;
        r1 = r8.mSystemGestureExclusionPath;
        r0.getBoundaryPath(r1);
        r0 = r8.mSystemGestureExclusionPath;
        r1 = r8.mSystemGestureExclusionPaint;
        r9.drawPath(r0, r1);
    L_0x0037:
        r0 = r8.mActivePointerId;
        r15 = 0;
        if (r0 < 0) goto L_0x02bb;
    L_0x003c:
        r1 = r8.mPointers;
        r0 = r1.get(r0);
        r16 = r0;
        r16 = (com.android.internal.widget.PointerLocationView.PointerState) r16;
        r1 = 0;
        r0 = r8.mHeaderPaddingTop;
        r2 = (float) r0;
        r0 = r11 + -1;
        r3 = (float) r0;
        r4 = (float) r13;
        r5 = r8.mTextBackgroundPaint;
        r0 = r29;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = r8.mText;
        r0 = r0.clear();
        r1 = "P: ";
        r0 = r0.append(r1);
        r1 = r8.mCurNumPointers;
        r0 = r0.append(r1);
        r1 = " / ";
        r0 = r0.append(r1);
        r1 = r8.mMaxNumPointers;
        r0 = r0.append(r1);
        r0 = r0.toString();
        r1 = (float) r12;
        r2 = r8.mTextPaint;
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r9.drawText(r0, r5, r1, r2);
        r17 = r16.mTraceCount;
        r0 = r8.mCurDown;
        if (r0 == 0) goto L_0x008d;
    L_0x0087:
        r0 = r16.mCurDown;
        if (r0 != 0) goto L_0x008f;
    L_0x008d:
        if (r17 != 0) goto L_0x0100;
    L_0x008f:
        r1 = (float) r11;
        r0 = r8.mHeaderPaddingTop;
        r2 = (float) r0;
        r0 = r11 * 2;
        r0 = r0 - r6;
        r3 = (float) r0;
        r4 = (float) r13;
        r0 = r8.mTextBackgroundPaint;
        r18 = r0;
        r0 = r29;
        r19 = r5;
        r5 = r18;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = r8.mText;
        r0 = r0.clear();
        r1 = "X: ";
        r0 = r0.append(r1);
        r1 = r16.mCoords;
        r1 = r1.x;
        r0 = r0.append(r1, r6);
        r0 = r0.toString();
        r1 = r11 + 1;
        r1 = (float) r1;
        r2 = (float) r12;
        r3 = r8.mTextPaint;
        r9.drawText(r0, r1, r2, r3);
        r0 = r11 * 2;
        r1 = (float) r0;
        r0 = r8.mHeaderPaddingTop;
        r2 = (float) r0;
        r0 = r11 * 3;
        r0 = r0 - r6;
        r3 = (float) r0;
        r4 = (float) r13;
        r5 = r8.mTextBackgroundPaint;
        r0 = r29;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = r8.mText;
        r0 = r0.clear();
        r1 = "Y: ";
        r0 = r0.append(r1);
        r1 = r16.mCoords;
        r1 = r1.y;
        r0 = r0.append(r1, r6);
        r0 = r0.toString();
        r1 = r11 * 2;
        r1 = r1 + r6;
        r1 = (float) r1;
        r2 = (float) r12;
        r3 = r8.mTextPaint;
        r9.drawText(r0, r1, r2, r3);
        goto L_0x01ae;
    L_0x0100:
        r19 = r5;
        r0 = r16.mTraceX;
        r1 = r17 + -1;
        r0 = r0[r1];
        r1 = r16.mTraceX;
        r1 = r1[r15];
        r5 = r0 - r1;
        r0 = r16.mTraceY;
        r1 = r17 + -1;
        r0 = r0[r1];
        r1 = r16.mTraceY;
        r1 = r1[r15];
        r4 = r0 - r1;
        r1 = (float) r11;
        r0 = r8.mHeaderPaddingTop;
        r2 = (float) r0;
        r0 = r11 * 2;
        r0 = r0 - r6;
        r3 = (float) r0;
        r0 = (float) r13;
        r18 = java.lang.Math.abs(r5);
        r15 = r8.mVC;
        r15 = r15.getScaledTouchSlop();
        r15 = (float) r15;
        r15 = (r18 > r15 ? 1 : (r18 == r15 ? 0 : -1));
        if (r15 >= 0) goto L_0x013d;
    L_0x013a:
        r15 = r8.mTextBackgroundPaint;
        goto L_0x013f;
    L_0x013d:
        r15 = r8.mTextLevelPaint;
    L_0x013f:
        r18 = r0;
        r0 = r29;
        r21 = r4;
        r4 = r18;
        r7 = r5;
        r5 = r15;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = r8.mText;
        r0 = r0.clear();
        r1 = "dX: ";
        r0 = r0.append(r1);
        r0 = r0.append(r7, r6);
        r0 = r0.toString();
        r1 = r11 + 1;
        r1 = (float) r1;
        r2 = (float) r12;
        r3 = r8.mTextPaint;
        r9.drawText(r0, r1, r2, r3);
        r0 = r11 * 2;
        r1 = (float) r0;
        r0 = r8.mHeaderPaddingTop;
        r2 = (float) r0;
        r0 = r11 * 3;
        r0 = r0 - r6;
        r3 = (float) r0;
        r4 = (float) r13;
        r0 = java.lang.Math.abs(r21);
        r5 = r8.mVC;
        r5 = r5.getScaledTouchSlop();
        r5 = (float) r5;
        r0 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1));
        if (r0 >= 0) goto L_0x0186;
    L_0x0183:
        r0 = r8.mTextBackgroundPaint;
        goto L_0x0188;
    L_0x0186:
        r0 = r8.mTextLevelPaint;
    L_0x0188:
        r5 = r0;
        r0 = r29;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = r8.mText;
        r0 = r0.clear();
        r1 = "dY: ";
        r0 = r0.append(r1);
        r1 = r21;
        r0 = r0.append(r1, r6);
        r0 = r0.toString();
        r2 = r11 * 2;
        r2 = r2 + r6;
        r2 = (float) r2;
        r3 = (float) r12;
        r4 = r8.mTextPaint;
        r9.drawText(r0, r2, r3, r4);
    L_0x01ae:
        r0 = r11 * 3;
        r1 = (float) r0;
        r0 = r8.mHeaderPaddingTop;
        r2 = (float) r0;
        r0 = r11 * 4;
        r0 = r0 - r6;
        r3 = (float) r0;
        r4 = (float) r13;
        r5 = r8.mTextBackgroundPaint;
        r0 = r29;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = r8.mText;
        r0 = r0.clear();
        r1 = "Xv: ";
        r0 = r0.append(r1);
        r1 = r16.mXVelocity;
        r7 = 3;
        r0 = r0.append(r1, r7);
        r0 = r0.toString();
        r1 = r11 * 3;
        r1 = r1 + r6;
        r1 = (float) r1;
        r2 = (float) r12;
        r3 = r8.mTextPaint;
        r9.drawText(r0, r1, r2, r3);
        r0 = r11 * 4;
        r1 = (float) r0;
        r0 = r8.mHeaderPaddingTop;
        r2 = (float) r0;
        r0 = r11 * 5;
        r0 = r0 - r6;
        r3 = (float) r0;
        r4 = (float) r13;
        r5 = r8.mTextBackgroundPaint;
        r0 = r29;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = r8.mText;
        r0 = r0.clear();
        r1 = "Yv: ";
        r0 = r0.append(r1);
        r1 = r16.mYVelocity;
        r0 = r0.append(r1, r7);
        r0 = r0.toString();
        r1 = r11 * 4;
        r1 = r1 + r6;
        r1 = (float) r1;
        r2 = (float) r12;
        r3 = r8.mTextPaint;
        r9.drawText(r0, r1, r2, r3);
        r0 = r11 * 5;
        r1 = (float) r0;
        r0 = r8.mHeaderPaddingTop;
        r2 = (float) r0;
        r0 = r11 * 6;
        r0 = r0 - r6;
        r3 = (float) r0;
        r4 = (float) r13;
        r5 = r8.mTextBackgroundPaint;
        r0 = r29;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = r11 * 5;
        r1 = (float) r0;
        r0 = r8.mHeaderPaddingTop;
        r2 = (float) r0;
        r0 = r11 * 5;
        r0 = (float) r0;
        r3 = r16.mCoords;
        r3 = r3.pressure;
        r4 = (float) r11;
        r3 = r3 * r4;
        r0 = r0 + r3;
        r3 = r0 - r19;
        r4 = (float) r13;
        r5 = r8.mTextLevelPaint;
        r0 = r29;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = r8.mText;
        r0 = r0.clear();
        r1 = "Prs: ";
        r0 = r0.append(r1);
        r1 = r16.mCoords;
        r1 = r1.pressure;
        r2 = 2;
        r0 = r0.append(r1, r2);
        r0 = r0.toString();
        r1 = r11 * 5;
        r1 = r1 + r6;
        r1 = (float) r1;
        r2 = (float) r12;
        r3 = r8.mTextPaint;
        r9.drawText(r0, r1, r2, r3);
        r0 = r11 * 6;
        r1 = (float) r0;
        r0 = r8.mHeaderPaddingTop;
        r2 = (float) r0;
        r3 = (float) r10;
        r4 = (float) r13;
        r5 = r8.mTextBackgroundPaint;
        r0 = r29;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = r11 * 6;
        r1 = (float) r0;
        r0 = r8.mHeaderPaddingTop;
        r2 = (float) r0;
        r0 = r11 * 6;
        r0 = (float) r0;
        r3 = r16.mCoords;
        r3 = r3.size;
        r4 = (float) r11;
        r3 = r3 * r4;
        r0 = r0 + r3;
        r3 = r0 - r19;
        r4 = (float) r13;
        r5 = r8.mTextLevelPaint;
        r0 = r29;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = r8.mText;
        r0 = r0.clear();
        r1 = "Size: ";
        r0 = r0.append(r1);
        r1 = r16.mCoords;
        r1 = r1.size;
        r7 = 2;
        r0 = r0.append(r1, r7);
        r0 = r0.toString();
        r1 = r11 * 6;
        r1 = r1 + r6;
        r1 = (float) r1;
        r2 = (float) r12;
        r3 = r8.mTextPaint;
        r9.drawText(r0, r1, r2, r3);
        goto L_0x02bc;
    L_0x02bb:
        r7 = 2;
    L_0x02bc:
        r0 = 0;
        r15 = r0;
    L_0x02be:
        if (r15 >= r14) goto L_0x05ab;
    L_0x02c0:
        r0 = r8.mPointers;
        r0 = r0.get(r15);
        r16 = r0;
        r16 = (com.android.internal.widget.PointerLocationView.PointerState) r16;
        r6 = r16.mTraceCount;
        r0 = 0;
        r1 = 0;
        r2 = 0;
        r3 = 0;
        r4 = r8.mPaint;
        r5 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r17 = r10;
        r10 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r4.setARGB(r10, r5, r10, r10);
        r4 = 0;
        r19 = r2;
        r18 = r3;
        r3 = r0;
        r2 = r1;
    L_0x02e4:
        if (r4 >= r6) goto L_0x0346;
    L_0x02e6:
        r0 = r16.mTraceX;
        r21 = r0[r4];
        r0 = r16.mTraceY;
        r22 = r0[r4];
        r0 = java.lang.Float.isNaN(r21);
        if (r0 == 0) goto L_0x0301;
    L_0x02f8:
        r0 = 0;
        r19 = r0;
        r26 = r4;
        r27 = r11;
        r11 = r5;
        goto L_0x033e;
    L_0x0301:
        if (r19 == 0) goto L_0x0330;
    L_0x0303:
        r1 = r8.mPathPaint;
        r0 = r29;
        r23 = r1;
        r1 = r3;
        r24 = r2;
        r10 = r3;
        r3 = r21;
        r26 = r4;
        r4 = r22;
        r27 = r11;
        r11 = r5;
        r5 = r23;
        r0.drawLine(r1, r2, r3, r4, r5);
        r0 = r16.mTraceCurrent;
        r0 = r0[r26];
        if (r0 == 0) goto L_0x0326;
    L_0x0323:
        r0 = r8.mCurrentPointPaint;
        goto L_0x0328;
    L_0x0326:
        r0 = r8.mPaint;
    L_0x0328:
        r5 = r24;
        r9.drawPoint(r10, r5, r0);
        r18 = 1;
        goto L_0x0337;
    L_0x0330:
        r10 = r3;
        r26 = r4;
        r27 = r11;
        r11 = r5;
        r5 = r2;
    L_0x0337:
        r3 = r21;
        r2 = r22;
        r0 = 1;
        r19 = r0;
    L_0x033e:
        r4 = r26 + 1;
        r5 = r11;
        r11 = r27;
        r10 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        goto L_0x02e4;
    L_0x0346:
        r10 = r3;
        r26 = r4;
        r27 = r11;
        r11 = r5;
        r5 = r2;
        if (r18 == 0) goto L_0x03a2;
    L_0x034f:
        r0 = r8.mPaint;
        r4 = 64;
        r1 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r0.setARGB(r1, r1, r4, r11);
        r0 = r16.mXVelocity;
        r21 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r22 = r0 * r21;
        r0 = r16.mYVelocity;
        r23 = r0 * r21;
        r3 = r10 + r22;
        r24 = r5 + r23;
        r2 = r8.mPaint;
        r0 = r29;
        r1 = r10;
        r26 = r2;
        r2 = r5;
        r7 = r4;
        r4 = r24;
        r24 = r5;
        r5 = r26;
        r0.drawLine(r1, r2, r3, r4, r5);
        r0 = r8.mAltVelocity;
        if (r0 == 0) goto L_0x03a4;
    L_0x0380:
        r0 = r8.mPaint;
        r1 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r0.setARGB(r1, r7, r1, r11);
        r0 = r16.mAltXVelocity;
        r7 = r0 * r21;
        r0 = r16.mAltYVelocity;
        r21 = r21 * r0;
        r3 = r10 + r7;
        r4 = r24 + r21;
        r5 = r8.mPaint;
        r0 = r29;
        r1 = r10;
        r2 = r24;
        r0.drawLine(r1, r2, r3, r4, r5);
        goto L_0x03a4;
    L_0x03a2:
        r24 = r5;
    L_0x03a4:
        r0 = r8.mCurDown;
        if (r0 == 0) goto L_0x059c;
    L_0x03a8:
        r0 = r16.mCurDown;
        if (r0 == 0) goto L_0x059c;
    L_0x03ae:
        r1 = 0;
        r0 = r16.mCoords;
        r2 = r0.y;
        r0 = r28.getWidth();
        r3 = (float) r0;
        r0 = r16.mCoords;
        r4 = r0.y;
        r5 = r8.mTargetPaint;
        r0 = r29;
        r0.drawLine(r1, r2, r3, r4, r5);
        r0 = r16.mCoords;
        r1 = r0.x;
        r2 = 0;
        r0 = r16.mCoords;
        r3 = r0.x;
        r0 = r28.getHeight();
        r4 = (float) r0;
        r5 = r8.mTargetPaint;
        r0 = r29;
        r0.drawLine(r1, r2, r3, r4, r5);
        r0 = r16.mCoords;
        r0 = r0.pressure;
        r1 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r0 = r0 * r1;
        r7 = (int) r0;
        r0 = r8.mPaint;
        r1 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r2 = 255 - r7;
        r0.setARGB(r1, r7, r1, r2);
        r0 = r16.mCoords;
        r0 = r0.x;
        r2 = r16.mCoords;
        r2 = r2.y;
        r3 = r8.mPaint;
        r9.drawPoint(r0, r2, r3);
        r0 = r8.mPaint;
        r2 = 255 - r7;
        r0.setARGB(r1, r7, r2, r11);
        r0 = com.android.internal.widget.PointerLocationViewInjector.isCustomTouchStyleEnabled();
        if (r0 == 0) goto L_0x0441;
    L_0x0411:
        r0 = r16.mCoords;
        r1 = r0.x;
        r0 = r16.mCoords;
        r2 = r0.y;
        r0 = r16.mCoords;
        r3 = r0.touchMajor;
        r0 = r16.mCoords;
        r4 = r0.touchMinor;
        r0 = r16.mCoords;
        r5 = r0.orientation;
        r0 = r8.mPaint;
        r21 = r0;
        r0 = r29;
        r22 = r6;
        r6 = r21;
        com.android.internal.widget.PointerLocationViewInjector.drawOval(r0, r1, r2, r3, r4, r5, r6);
        r11 = r7;
        r26 = r10;
        r10 = 2;
        goto L_0x0472;
    L_0x0441:
        r22 = r6;
        r0 = r16.mCoords;
        r2 = r0.x;
        r0 = r16.mCoords;
        r3 = r0.y;
        r0 = r16.mCoords;
        r4 = r0.touchMajor;
        r0 = r16.mCoords;
        r5 = r0.touchMinor;
        r0 = r16.mCoords;
        r6 = r0.orientation;
        r1 = r8.mPaint;
        r0 = r28;
        r21 = r1;
        r1 = r29;
        r11 = r7;
        r26 = r10;
        r10 = 2;
        r7 = r21;
        r0.drawOval(r1, r2, r3, r4, r5, r6, r7);
    L_0x0472:
        r0 = r8.mPaint;
        r1 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r2 = 255 - r11;
        r3 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r0.setARGB(r1, r11, r3, r2);
        r0 = r16.mCoords;
        r2 = r0.x;
        r0 = r16.mCoords;
        r3 = r0.y;
        r0 = r16.mCoords;
        r4 = r0.toolMajor;
        r0 = r16.mCoords;
        r5 = r0.toolMinor;
        r0 = r16.mCoords;
        r6 = r0.orientation;
        r7 = r8.mPaint;
        r0 = r28;
        r1 = r29;
        r0.drawOval(r1, r2, r3, r4, r5, r6, r7);
        r0 = r16.mCoords;
        r0 = r0.toolMajor;
        r1 = 1060320051; // 0x3f333333 float:0.7 double:5.23867711E-315;
        r0 = r0 * r1;
        r1 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r1 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1));
        if (r1 >= 0) goto L_0x04b8;
    L_0x04b4:
        r0 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r6 = r0;
        goto L_0x04b9;
    L_0x04b8:
        r6 = r0;
    L_0x04b9:
        r0 = r8.mPaint;
        r1 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r7 = 0;
        r0.setARGB(r1, r11, r1, r7);
        r0 = r16.mCoords;
        r0 = r0.orientation;
        r0 = (double) r0;
        r0 = java.lang.Math.sin(r0);
        r2 = (double) r6;
        r0 = r0 * r2;
        r5 = (float) r0;
        r0 = r16.mCoords;
        r0 = r0.orientation;
        r0 = (double) r0;
        r0 = java.lang.Math.cos(r0);
        r0 = -r0;
        r2 = (double) r6;
        r0 = r0 * r2;
        r4 = (float) r0;
        r0 = r16.mToolType;
        if (r0 == r10) goto L_0x0523;
    L_0x04e4:
        r0 = r16.mToolType;
        r1 = 4;
        if (r0 != r1) goto L_0x04f0;
    L_0x04eb:
        r23 = r4;
        r20 = r5;
        goto L_0x0527;
        r0 = r16.mCoords;
        r0 = r0.x;
        r1 = r0 - r5;
        r0 = r16.mCoords;
        r0 = r0.y;
        r2 = r0 - r4;
        r0 = r16.mCoords;
        r0 = r0.x;
        r3 = r0 + r5;
        r0 = r16.mCoords;
        r0 = r0.y;
        r20 = r0 + r4;
        r0 = r8.mPaint;
        r21 = r0;
        r0 = r29;
        r23 = r4;
        r4 = r20;
        r20 = r5;
        r5 = r21;
        r0.drawLine(r1, r2, r3, r4, r5);
        goto L_0x054a;
    L_0x0523:
        r23 = r4;
        r20 = r5;
    L_0x0527:
        r0 = r16.mCoords;
        r1 = r0.x;
        r0 = r16.mCoords;
        r2 = r0.y;
        r0 = r16.mCoords;
        r0 = r0.x;
        r3 = r0 + r20;
        r0 = r16.mCoords;
        r0 = r0.y;
        r4 = r0 + r23;
        r5 = r8.mPaint;
        r0 = r29;
        r0.drawLine(r1, r2, r3, r4, r5);
        r0 = r16.mCoords;
        r1 = 25;
        r0 = r0.getAxisValue(r1);
        r0 = (double) r0;
        r0 = java.lang.Math.sin(r0);
        r5 = (float) r0;
        r0 = r16.mCoords;
        r0 = r0.x;
        r1 = r20 * r5;
        r0 = r0 + r1;
        r1 = r16.mCoords;
        r1 = r1.y;
        r4 = r23 * r5;
        r1 = r1 + r4;
        r2 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r3 = r8.mPaint;
        r9.drawCircle(r0, r1, r2, r3);
        r0 = r16.mHasBoundingBox;
        if (r0 == 0) goto L_0x0599;
    L_0x057b:
        r1 = r16.mBoundingLeft;
        r2 = r16.mBoundingTop;
        r3 = r16.mBoundingRight;
        r4 = r16.mBoundingBottom;
        r0 = r8.mPaint;
        r21 = r0;
        r0 = r29;
        r25 = r5;
        r5 = r21;
        r0.drawRect(r1, r2, r3, r4, r5);
        goto L_0x05a2;
    L_0x0599:
        r25 = r5;
        goto L_0x05a2;
    L_0x059c:
        r22 = r6;
        r26 = r10;
        r7 = 0;
        r10 = 2;
    L_0x05a2:
        r15 = r15 + 1;
        r7 = r10;
        r10 = r17;
        r11 = r27;
        goto L_0x02be;
    L_0x05ab:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.PointerLocationView.onDraw(android.graphics.Canvas):void");
    }

    private void logMotionEvent(String type, MotionEvent event) {
        int historyPos;
        int i;
        int action = event.getAction();
        int N = event.getHistorySize();
        int NI = event.getPointerCount();
        for (historyPos = 0; historyPos < N; historyPos++) {
            for (i = 0; i < NI; i++) {
                int id = event.getPointerId(i);
                event.getHistoricalPointerCoords(i, historyPos, this.mTempCoords);
                logCoords(type, action, i, this.mTempCoords, id, event);
            }
        }
        for (historyPos = 0; historyPos < NI; historyPos++) {
            i = event.getPointerId(historyPos);
            event.getPointerCoords(historyPos, this.mTempCoords);
            logCoords(type, action, historyPos, this.mTempCoords, i, event);
        }
    }

    private void logCoords(String type, int action, int index, PointerCoords coords, int id, MotionEvent event) {
        String prefix;
        int i = action;
        int i2 = index;
        PointerCoords pointerCoords = coords;
        MotionEvent motionEvent = event;
        int toolType = motionEvent.getToolType(i2);
        int buttonState = event.getButtonState();
        switch (i & 255) {
            case 0:
                prefix = "DOWN";
                break;
            case 1:
                prefix = "UP";
                break;
            case 2:
                prefix = "MOVE";
                break;
            case 3:
                prefix = "CANCEL";
                break;
            case 4:
                prefix = "OUTSIDE";
                break;
            case 5:
                if (i2 != ((i & 65280) >> 8)) {
                    prefix = "MOVE";
                    break;
                } else {
                    prefix = "DOWN";
                    break;
                }
            case 6:
                if (i2 != ((i & 65280) >> 8)) {
                    prefix = "MOVE";
                    break;
                } else {
                    prefix = "UP";
                    break;
                }
            case 7:
                prefix = "HOVER MOVE";
                break;
            case 8:
                prefix = "SCROLL";
                break;
            case 9:
                prefix = "HOVER ENTER";
                break;
            case 10:
                prefix = "HOVER EXIT";
                break;
            default:
                prefix = Integer.toString(action);
                break;
        }
        String str = ", ";
        String str2 = "deg";
        String str3 = str2;
        Log.i(TAG, this.mText.clear().append(type).append(" id ").append(id + 1).append(": ").append(prefix).append(" (").append(pointerCoords.x, 3).append(str).append(pointerCoords.y, 3).append(") Pressure=").append(pointerCoords.pressure, 3).append(" Size=").append(pointerCoords.size, 3).append(" TouchMajor=").append(pointerCoords.touchMajor, 3).append(" TouchMinor=").append(pointerCoords.touchMinor, 3).append(" ToolMajor=").append(pointerCoords.toolMajor, 3).append(" ToolMinor=").append(pointerCoords.toolMinor, 3).append(" Orientation=").append((float) (((double) (pointerCoords.orientation * 180.0f)) / 3.141592653589793d), 1).append(str2).append(" Tilt=").append((float) (((double) (pointerCoords.getAxisValue(25) * 180.0f)) / 3.141592653589793d), 1).append(str3).append(" Distance=").append(pointerCoords.getAxisValue(24), 1).append(" VScroll=").append(pointerCoords.getAxisValue(9), 1).append(" HScroll=").append(pointerCoords.getAxisValue(10), 1).append(" BoundingBox=[(").append(motionEvent.getAxisValue(32), 3).append(str).append(motionEvent.getAxisValue(33), 3).append(")").append(", (").append(motionEvent.getAxisValue(34), 3).append(str).append(motionEvent.getAxisValue(35), 3).append(")]").append(" ToolType=").append(MotionEvent.toolTypeToString(toolType)).append(" ButtonState=").append(MotionEvent.buttonStateToString(buttonState)).toString());
    }

    public void onPointerEvent(MotionEvent event) {
        int index;
        int p;
        PointerState ps;
        int i;
        int NP;
        PointerCoords coords;
        int historyPos;
        int N;
        PointerCoords coords2;
        MotionEvent motionEvent = event;
        int action = event.getAction();
        int NP2 = this.mPointers.size();
        int i2 = 1;
        if (action == 0 || (action & 255) == 5) {
            index = (action & 65280) >> 8;
            if (action == 0) {
                for (p = 0; p < NP2; p++) {
                    ps = (PointerState) this.mPointers.get(p);
                    ps.clearTrace();
                    ps.mCurDown = false;
                }
                this.mCurDown = true;
                this.mCurNumPointers = 0;
                this.mMaxNumPointers = 0;
                this.mVelocity.clear();
                VelocityTracker velocityTracker = this.mAltVelocity;
                if (velocityTracker != null) {
                    velocityTracker.clear();
                }
            }
            this.mCurNumPointers++;
            p = this.mMaxNumPointers;
            i = this.mCurNumPointers;
            if (p < i) {
                this.mMaxNumPointers = i;
            }
            p = motionEvent.getPointerId(index);
            while (NP2 <= p) {
                this.mPointers.add(new PointerState());
                NP2++;
            }
            i = this.mActivePointerId;
            if (i < 0 || !((PointerState) this.mPointers.get(i)).mCurDown) {
                this.mActivePointerId = p;
            }
            ps = (PointerState) this.mPointers.get(p);
            ps.mCurDown = true;
            InputDevice device = InputDevice.getDevice(event.getDeviceId());
            boolean z = (device == null || device.getMotionRange(32) == null) ? false : true;
            ps.mHasBoundingBox = z;
            NP = NP2;
        } else {
            NP = NP2;
        }
        int NI = event.getPointerCount();
        this.mVelocity.addMovement(motionEvent);
        this.mVelocity.computeCurrentVelocity(1);
        VelocityTracker velocityTracker2 = this.mAltVelocity;
        if (velocityTracker2 != null) {
            velocityTracker2.addMovement(motionEvent);
            this.mAltVelocity.computeCurrentVelocity(1);
        }
        int N2 = event.getHistorySize();
        int historyPos2 = 0;
        while (historyPos2 < N2) {
            int i3;
            int i4 = 0;
            while (i4 < NI) {
                PointerState ps2;
                i = motionEvent.getPointerId(i4);
                PointerState ps3 = this.mCurDown ? (PointerState) this.mPointers.get(i) : null;
                PointerCoords coords3 = ps3 != null ? ps3.mCoords : this.mTempCoords;
                motionEvent.getHistoricalPointerCoords(i4, historyPos2, coords3);
                if (this.mPrintCoords) {
                    coords = coords3;
                    ps2 = ps3;
                    i3 = i4;
                    historyPos = historyPos2;
                    N = N2;
                    logCoords(TAG, action, i4, coords, i, event);
                } else {
                    coords = coords3;
                    ps2 = ps3;
                    int i5 = i;
                    i3 = i4;
                    historyPos = historyPos2;
                    N = N2;
                }
                if (ps2 != null) {
                    coords2 = coords;
                    ps2.addTrace(coords2.x, coords2.y, false);
                }
                i4 = i3 + 1;
                historyPos2 = historyPos;
                N2 = N;
            }
            i3 = i4;
            N = N2;
            historyPos2++;
        }
        historyPos = historyPos2;
        N = N2;
        int i6 = 0;
        while (i6 < NI) {
            PointerState ps4;
            int id;
            N2 = motionEvent.getPointerId(i6);
            PointerState ps5 = this.mCurDown ? (PointerState) this.mPointers.get(N2) : null;
            PointerCoords coords4 = ps5 != null ? ps5.mCoords : this.mTempCoords;
            motionEvent.getPointerCoords(i6, coords4);
            if (this.mPrintCoords) {
                coords = coords4;
                ps4 = ps5;
                id = N2;
                logCoords(TAG, action, i6, coords4, N2, event);
            } else {
                coords = coords4;
                ps4 = ps5;
                id = N2;
            }
            if (ps4 != null) {
                coords2 = coords;
                ps4.addTrace(coords2.x, coords2.y, true);
                ps4.mXVelocity = this.mVelocity.getXVelocity(id);
                ps4.mYVelocity = this.mVelocity.getYVelocity(id);
                this.mVelocity.getEstimator(id, ps4.mEstimator);
                VelocityTracker velocityTracker3 = this.mAltVelocity;
                if (velocityTracker3 != null) {
                    ps4.mAltXVelocity = velocityTracker3.getXVelocity(id);
                    ps4.mAltYVelocity = this.mAltVelocity.getYVelocity(id);
                    this.mAltVelocity.getEstimator(id, ps4.mAltEstimator);
                }
                ps4.mToolType = motionEvent.getToolType(i6);
                if (ps4.mHasBoundingBox) {
                    index = 32;
                    ps4.mBoundingLeft = motionEvent.getAxisValue(32, i6);
                    ps4.mBoundingTop = motionEvent.getAxisValue(33, i6);
                    ps4.mBoundingRight = motionEvent.getAxisValue(34, i6);
                    ps4.mBoundingBottom = motionEvent.getAxisValue(35, i6);
                } else {
                    index = 32;
                }
            } else {
                index = 32;
            }
            i6++;
            id = index;
        }
        if (action == 1 || action == 3 || (action & 255) == 6) {
            index = (65280 & action) >> 8;
            p = motionEvent.getPointerId(index);
            if (p >= NP) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Got pointer ID out of bounds: id=");
                stringBuilder.append(p);
                stringBuilder.append(" arraysize=");
                stringBuilder.append(NP);
                stringBuilder.append(" pointerindex=");
                stringBuilder.append(index);
                stringBuilder.append(" action=0x");
                stringBuilder.append(Integer.toHexString(action));
                Slog.wtf(TAG, stringBuilder.toString());
                return;
            }
            boolean z2;
            ps = (PointerState) this.mPointers.get(p);
            ps.mCurDown = false;
            if (action == 1) {
                z2 = false;
            } else if (action == 3) {
                z2 = false;
            } else {
                this.mCurNumPointers--;
                if (this.mActivePointerId == p) {
                    if (index != 0) {
                        i2 = 0;
                    }
                    this.mActivePointerId = motionEvent.getPointerId(i2);
                }
                ps.addTrace(Float.NaN, Float.NaN, false);
            }
            this.mCurDown = z2;
            this.mCurNumPointers = z2;
        }
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        onPointerEvent(event);
        if (event.getAction() == 0 && !isFocused()) {
            requestFocus();
        }
        return true;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        int source = event.getSource();
        if ((source & 2) != 0) {
            onPointerEvent(event);
        } else if ((source & 16) != 0) {
            logMotionEvent("Joystick", event);
        } else if ((source & 8) != 0) {
            logMotionEvent("Position", event);
        } else {
            logMotionEvent("Generic", event);
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!shouldLogKey(keyCode)) {
            return super.onKeyDown(keyCode, event);
        }
        int repeatCount = event.getRepeatCount();
        String str = TAG;
        StringBuilder stringBuilder;
        if (repeatCount == 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Key Down: ");
            stringBuilder.append(event);
            Log.i(str, stringBuilder.toString());
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Key Repeat #");
            stringBuilder.append(repeatCount);
            stringBuilder.append(": ");
            stringBuilder.append(event);
            Log.i(str, stringBuilder.toString());
        }
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!shouldLogKey(keyCode)) {
            return super.onKeyUp(keyCode, event);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Key Up: ");
        stringBuilder.append(event);
        Log.i(TAG, stringBuilder.toString());
        return true;
    }

    private static boolean shouldLogKey(int keyCode) {
        boolean z = true;
        switch (keyCode) {
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                return true;
            default:
                if (!(KeyEvent.isGamepadButton(keyCode) || KeyEvent.isModifierKey(keyCode))) {
                    z = false;
                }
                return z;
        }
    }

    public boolean onTrackballEvent(MotionEvent event) {
        logMotionEvent("Trackball", event);
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mIm.registerInputDeviceListener(this, getHandler());
        if (shouldShowSystemGestureExclusion()) {
            try {
                WindowManagerGlobal.getWindowManagerService().registerSystemGestureExclusionListener(this.mSystemGestureExclusionListener, this.mContext.getDisplayId());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        this.mSystemGestureExclusion.setEmpty();
        logInputDevices();
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mIm.unregisterInputDeviceListener(this);
        try {
            WindowManagerGlobal.getWindowManagerService().unregisterSystemGestureExclusionListener(this.mSystemGestureExclusionListener, this.mContext.getDisplayId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void onInputDeviceAdded(int deviceId) {
        logInputDeviceState(deviceId, "Device Added");
    }

    public void onInputDeviceChanged(int deviceId) {
        logInputDeviceState(deviceId, "Device Changed");
    }

    public void onInputDeviceRemoved(int deviceId) {
        logInputDeviceState(deviceId, "Device Removed");
    }

    private void logInputDevices() {
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int logInputDeviceState : deviceIds) {
            logInputDeviceState(logInputDeviceState, "Device Enumerated");
        }
    }

    private void logInputDeviceState(int deviceId, String state) {
        InputDevice device = this.mIm.getInputDevice(deviceId);
        String str = ": ";
        String str2 = TAG;
        StringBuilder stringBuilder;
        if (device != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(state);
            stringBuilder.append(str);
            stringBuilder.append(device);
            Log.i(str2, stringBuilder.toString());
            return;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(state);
        stringBuilder.append(str);
        stringBuilder.append(deviceId);
        Log.i(str2, stringBuilder.toString());
    }

    private static boolean shouldShowSystemGestureExclusion() {
        return SystemProperties.getBoolean("debug.pointerlocation.showexclusion", false);
    }
}
