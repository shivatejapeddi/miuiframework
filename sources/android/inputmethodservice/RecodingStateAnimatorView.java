package android.inputmethodservice;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import java.util.ArrayList;
import java.util.List;

public class RecodingStateAnimatorView extends View {
    private static final String TAG = "RecodingStateAnimatorView";
    private List<ValueAnimator> mAnimators;
    private BallParams[] mBallParams;
    private int mHeight;
    private long mLastInvalidTime;
    private int[] mLoadingColor;
    private State mPaddingState;
    private boolean mResume;
    private State mState;
    private int mWidth;

    private class BallParams {
        int color;
        float ctx;
        float cty;
        float dynamicVoiceMaxRad;
        float dynamicVoiceMinRad;
        boolean flag1;
        LinearGradientUtil gradientUtil;
        float hasVoiceMaxRad;
        float hasVoiceMinRad;
        int layer;
        float loadingMaxRad;
        float loadingMinRad;
        float noVoice2HasVoiceRad;
        float noVoiceMaxRad;
        float noVoiceMinRad;
        float nowX;
        float nowY;
        Paint paint;
        float rad;
        float scale;

        private BallParams() {
            this.scale = 1.0f;
        }

        /* synthetic */ BallParams(RecodingStateAnimatorView x0, AnonymousClass1 x1) {
            this();
        }
    }

    public class LinearGradientUtil {
        private int mEndColor;
        private int mStartColor;
        private float v;

        public float getV() {
            return this.v;
        }

        public LinearGradientUtil(int startColor, int endColor) {
            this.mStartColor = startColor;
            this.mEndColor = endColor;
        }

        public void setStartColor(int startColor) {
            this.mStartColor = startColor;
        }

        public void setEndColor(int endColor) {
            this.mEndColor = endColor;
        }

        public int getEndColor() {
            return this.mEndColor;
        }

        public int getStartColor() {
            return this.mStartColor;
        }

        public int getColor(float radio) {
            int redStart = Color.red(this.mStartColor);
            int blueStart = Color.blue(this.mStartColor);
            int greenStart = Color.green(this.mStartColor);
            int redEnd = Color.red(this.mEndColor);
            int blueEnd = Color.blue(this.mEndColor);
            int greenEnd = Color.green(this.mEndColor);
            this.v = radio;
            return Color.argb(255, (int) (((double) redStart) + (((double) (((float) (redEnd - redStart)) * radio)) + 0.5d)), (int) (((double) greenStart) + (((double) (((float) (greenEnd - greenStart)) * radio)) + 0.5d)), (int) (((double) blueStart) + (((double) (((float) (blueEnd - blueStart)) * radio)) + 0.5d)));
        }
    }

    public enum State {
        RECORDING_NO_VOICE,
        RECORDING_HAS_VOICE
    }

    public RecodingStateAnimatorView(Context context) {
        super(context);
        this.mBallParams = new BallParams[2];
        this.mPaddingState = State.RECORDING_NO_VOICE;
        this.mLoadingColor = new int[4];
        this.mAnimators = new ArrayList();
        this.mLastInvalidTime = -1;
        checkSize();
    }

    public RecodingStateAnimatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mBallParams = new BallParams[2];
        this.mPaddingState = State.RECORDING_NO_VOICE;
        this.mLoadingColor = new int[4];
        this.mAnimators = new ArrayList();
        this.mLastInvalidTime = -1;
        checkSize();
    }

    private void checkSize() {
        getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
                if (!(RecodingStateAnimatorView.this.mWidth == RecodingStateAnimatorView.this.getWidth() && RecodingStateAnimatorView.this.mHeight == RecodingStateAnimatorView.this.getHeight())) {
                    RecodingStateAnimatorView.this.init();
                }
                return true;
            }
        });
    }

    /* Access modifiers changed, original: protected */
    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == 0) {
            resume();
        } else {
            pause();
        }
    }

    /* JADX WARNING: Incorrect type for fill-array insn 0x0037, element type: float, insn element type: null */
    /* JADX WARNING: Incorrect type for fill-array insn 0x005d, element type: float, insn element type: null */
    private void processStateChange() {
        /*
        r9 = this;
        r0 = r9.mPaddingState;
        if (r0 == 0) goto L_0x0094;
    L_0x0004:
        r0 = r9.mResume;
        if (r0 == 0) goto L_0x0094;
    L_0x0008:
        r0 = r9.mState;
        r1 = android.inputmethodservice.RecodingStateAnimatorView.State.RECORDING_NO_VOICE;
        r2 = 0;
        if (r0 != r1) goto L_0x008a;
    L_0x000f:
        r0 = r9.mPaddingState;
        r1 = android.inputmethodservice.RecodingStateAnimatorView.State.RECORDING_HAS_VOICE;
        if (r0 != r1) goto L_0x008a;
    L_0x0015:
        r9.mPaddingState = r2;
        r9.cancelAnimators();
        r0 = r9.mBallParams;
        r1 = 1;
        r0 = r0[r1];
        r0 = r0.rad;
        r3 = r9.mBallParams;
        r1 = r3[r1];
        r1 = r1.noVoice2HasVoiceRad;
        r3 = r9.mBallParams;
        r4 = 0;
        r3 = r3[r4];
        r3 = r3.rad;
        r5 = r9.mBallParams;
        r4 = r5[r4];
        r4 = r4.noVoice2HasVoiceRad;
        r5 = 2;
        r6 = new float[r5];
        r6 = {0, 1065353216};
        r6 = android.animation.ValueAnimator.ofFloat(r6);
        r7 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        r6.setDuration(r7);
        r7 = new android.view.animation.DecelerateInterpolator;
        r7.<init>();
        r6.setInterpolator(r7);
        r7 = new android.inputmethodservice.RecodingStateAnimatorView$2;
        r7.<init>(r0, r1);
        r6.addUpdateListener(r7);
        r7 = r9.mAnimators;
        r7.add(r6);
        r6.start();
        r5 = new float[r5];
        r5 = {0, 1065353216};
        r5 = android.animation.ValueAnimator.ofFloat(r5);
        r7 = new android.view.animation.AccelerateDecelerateInterpolator;
        r7.<init>();
        r5.setInterpolator(r7);
        r7 = 350; // 0x15e float:4.9E-43 double:1.73E-321;
        r5.setDuration(r7);
        r7 = new android.inputmethodservice.RecodingStateAnimatorView$3;
        r7.<init>(r3, r4);
        r5.addUpdateListener(r7);
        r7 = new android.inputmethodservice.RecodingStateAnimatorView$4;
        r7.<init>();
        r5.addListener(r7);
        r7 = r9.mAnimators;
        r7.add(r5);
        r5.start();
        goto L_0x0092;
    L_0x008a:
        r9.cancelAnimators();
        r0 = r9.mPaddingState;
        r9.changeState(r0);
    L_0x0092:
        r9.mPaddingState = r2;
    L_0x0094:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.inputmethodservice.RecodingStateAnimatorView.processStateChange():void");
    }

    private void cancelAnimators() {
        for (ValueAnimator animator : this.mAnimators) {
            animator.removeAllUpdateListeners();
            animator.removeAllListeners();
            animator.cancel();
        }
        this.mAnimators.clear();
    }

    public State getPaddingState() {
        return this.mPaddingState;
    }

    public State getState() {
        return this.mState;
    }

    public void setPreState(State state) {
        if (state != null) {
            this.mPaddingState = state;
            if (this.mAnimators.size() == 0) {
                processStateChange();
                return;
            }
            return;
        }
        this.mPaddingState = null;
        this.mState = null;
        cancelAnimators();
    }

    public void setVolume(float v) {
        BallParams[] ballParamsArr = this.mBallParams;
        if (ballParamsArr != null) {
            ballParamsArr[1].scale = v;
        }
    }

    public void pause() {
        List<ValueAnimator> list = this.mAnimators;
        if (list != null) {
            for (ValueAnimator animator : list) {
                animator.pause();
            }
        }
    }

    public void resume() {
        List<ValueAnimator> list = this.mAnimators;
        if (list != null) {
            for (ValueAnimator animator : list) {
                animator.resume();
            }
        }
    }

    private int getNextLoadingColor(int color) {
        int r = -1;
        int index = 0;
        while (true) {
            int[] iArr = this.mLoadingColor;
            if (index >= iArr.length) {
                return iArr[(r + 1) % 4];
            }
            if (iArr[index] == color) {
                r = index;
            }
            index++;
        }
    }

    /* JADX WARNING: Incorrect type for fill-array insn 0x0012, element type: float, insn element type: null */
    /* JADX WARNING: Incorrect type for fill-array insn 0x00de, element type: float, insn element type: null */
    private void changeState(android.inputmethodservice.RecodingStateAnimatorView.State r9) {
        /*
        r8 = this;
        r8.cancelAnimators();
        r8.mState = r9;
        r0 = 0;
        r8.mPaddingState = r0;
        r0 = android.inputmethodservice.RecodingStateAnimatorView.State.RECORDING_HAS_VOICE;
        r1 = -1;
        r2 = 2;
        r3 = 1;
        r4 = 0;
        if (r9 != r0) goto L_0x010c;
    L_0x0010:
        r0 = new float[r2];
        r0 = {0, 1065353216};
        r0 = android.animation.ValueAnimator.ofFloat(r0);
        r5 = r8.mBallParams;
        r6 = r5[r4];
        r5 = r5[r4];
        r5 = r5.cty;
        r6.nowY = r5;
        r5 = r8.mBallParams;
        r6 = r5[r4];
        r5 = r5[r4];
        r5 = r5.ctx;
        r6.nowX = r5;
        r5 = r8.mBallParams;
        r6 = r5[r4];
        r6.flag1 = r3;
        r6 = r5[r4];
        r5 = r5[r4];
        r5 = r5.gradientUtil;
        r5 = r5.getV();
        r7 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r5 >= 0) goto L_0x004e;
    L_0x0043:
        r5 = r8.mBallParams;
        r5 = r5[r4];
        r5 = r5.gradientUtil;
        r5 = r5.getStartColor();
        goto L_0x0058;
    L_0x004e:
        r5 = r8.mBallParams;
        r5 = r5[r4];
        r5 = r5.gradientUtil;
        r5 = r5.getEndColor();
    L_0x0058:
        r6.color = r5;
        r5 = r8.mBallParams;
        r5 = r5[r4];
        r5 = r5.paint;
        r6 = r8.mBallParams;
        r6 = r6[r4];
        r6 = r6.color;
        r5.setColor(r6);
        r5 = r8.mBallParams;
        r5 = r5[r4];
        r5 = r5.rad;
        r6 = r8.mBallParams;
        r7 = r6[r3];
        r6 = r6[r4];
        r6 = r6.color;
        r7.color = r6;
        r6 = r8.mBallParams;
        r6 = r6[r3];
        r6 = r6.paint;
        r7 = r8.mBallParams;
        r7 = r7[r3];
        r7 = r7.color;
        r6.setColor(r7);
        r6 = r8.mBallParams;
        r6 = r6[r3];
        r6 = r6.paint;
        r7 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r6.setAlpha(r7);
        r6 = r8.mBallParams;
        r7 = r6[r3];
        r6 = r6[r3];
        r6 = r6.ctx;
        r7.nowX = r6;
        r6 = r8.mBallParams;
        r7 = r6[r3];
        r6 = r6[r3];
        r6 = r6.cty;
        r7.nowY = r6;
        r6 = r8.mBallParams;
        r7 = r6[r3];
        r7.flag1 = r3;
        r3 = r6[r4];
        r3 = r3.rad;
        r6 = 350; // 0x15e float:4.9E-43 double:1.73E-321;
        r0.setDuration(r6);
        r4 = new android.view.animation.AccelerateDecelerateInterpolator;
        r4.<init>();
        r0.setInterpolator(r4);
        r4 = new android.inputmethodservice.RecodingStateAnimatorView$5;
        r4.<init>(r5);
        r0.addUpdateListener(r4);
        r4 = new android.inputmethodservice.RecodingStateAnimatorView$6;
        r4.<init>();
        r0.addListener(r4);
        r0.setRepeatCount(r1);
        r0.setRepeatMode(r2);
        r1 = r8.mAnimators;
        r1.add(r0);
        r0.start();
        r1 = new float[r2];
        r1 = {0, 1065353216};
        r1 = android.animation.ValueAnimator.ofFloat(r1);
        r6 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        r1.setDuration(r6);
        r2 = new android.view.animation.AccelerateDecelerateInterpolator;
        r2.<init>();
        r1.setInterpolator(r2);
        r2 = new android.inputmethodservice.RecodingStateAnimatorView$7;
        r2.<init>(r3);
        r1.addUpdateListener(r2);
        r2 = new android.inputmethodservice.RecodingStateAnimatorView$8;
        r2.<init>(r1);
        r1.addListener(r2);
        r2 = r8.mAnimators;
        r2.add(r1);
        r1.start();
        goto L_0x01b9;
    L_0x010c:
        r0 = android.inputmethodservice.RecodingStateAnimatorView.State.RECORDING_NO_VOICE;
        if (r9 != r0) goto L_0x01b9;
    L_0x0110:
        r0 = r8.mBallParams;
        r5 = r0[r4];
        r5.layer = r4;
        r5 = r0[r4];
        r6 = r8.mLoadingColor;
        r6 = r6[r4];
        r5.color = r6;
        r0 = r0[r4];
        r0 = r0.paint;
        r5 = r8.mBallParams;
        r5 = r5[r4];
        r5 = r5.color;
        r0.setColor(r5);
        r0 = r8.mBallParams;
        r5 = r0[r4];
        r0 = r0[r4];
        r0 = r0.ctx;
        r5.nowX = r0;
        r0 = r8.mBallParams;
        r5 = r0[r4];
        r0 = r0[r4];
        r0 = r0.noVoiceMinRad;
        r5.rad = r0;
        r0 = r8.mBallParams;
        r0 = r0[r4];
        r0 = r0.gradientUtil;
        r5 = r8.mLoadingColor;
        r5 = r5[r4];
        r0.setStartColor(r5);
        r0 = r8.mBallParams;
        r0 = r0[r4];
        r0 = r0.gradientUtil;
        r5 = r8.mLoadingColor;
        r4 = r5[r4];
        r0.setEndColor(r4);
        r0 = r8.mBallParams;
        r4 = r0[r3];
        r4.layer = r3;
        r4 = r0[r3];
        r4.color = r1;
        r0 = r0[r3];
        r0 = r0.paint;
        r4 = r8.mBallParams;
        r4 = r4[r3];
        r4 = r4.color;
        r0.setColor(r4);
        r0 = r8.mBallParams;
        r4 = r0[r3];
        r0 = r0[r3];
        r0 = r0.ctx;
        r4.nowX = r0;
        r0 = r8.mBallParams;
        r4 = r0[r3];
        r0 = r0[r3];
        r0 = r0.noVoiceMaxRad;
        r4.rad = r0;
        r0 = new float[r2];
        r0 = {0, 1065353216};
        r0 = android.animation.ValueAnimator.ofFloat(r0);
        r3 = new android.view.animation.AccelerateDecelerateInterpolator;
        r3.<init>();
        r0.setInterpolator(r3);
        r3 = 600; // 0x258 float:8.41E-43 double:2.964E-321;
        r0.setDuration(r3);
        r3 = new android.inputmethodservice.RecodingStateAnimatorView$9;
        r3.<init>();
        r0.addUpdateListener(r3);
        r3 = new android.inputmethodservice.RecodingStateAnimatorView$10;
        r3.<init>();
        r0.addListener(r3);
        r0.setRepeatCount(r1);
        r0.setRepeatMode(r2);
        r1 = r8.mAnimators;
        r1.add(r0);
        r0.start();
        goto L_0x01ba;
    L_0x01ba:
        r8.invalidate();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.inputmethodservice.RecodingStateAnimatorView.changeState(android.inputmethodservice.RecodingStateAnimatorView$State):void");
    }

    public void init() {
        cancelAnimators();
        int[] iArr = this.mLoadingColor;
        iArr[0] = -13578767;
        iArr[1] = -79569;
        iArr[2] = -628617;
        iArr[3] = -12610564;
        this.mWidth = getWidth();
        this.mHeight = getHeight();
        this.mBallParams[0] = new BallParams(this, null);
        BallParams[] ballParamsArr = this.mBallParams;
        BallParams ballParams = ballParamsArr[0];
        int i = this.mWidth;
        ballParams.ctx = (float) (i / 2);
        ballParamsArr[0].cty = (float) (this.mHeight / 2);
        ballParamsArr[0].rad = (float) (i / 8);
        ballParamsArr[0].paint = new Paint();
        this.mBallParams[0].paint.setAntiAlias(true);
        ballParamsArr = this.mBallParams;
        ballParamsArr[0].color = this.mLoadingColor[3];
        ballParamsArr[0].paint.setColor(this.mBallParams[0].color);
        ballParamsArr = this.mBallParams;
        ballParamsArr[0].nowX = ballParamsArr[0].ctx;
        ballParamsArr = this.mBallParams;
        ballParamsArr[0].nowY = ballParamsArr[0].cty;
        ballParamsArr = this.mBallParams;
        BallParams ballParams2 = ballParamsArr[0];
        int i2 = this.mWidth;
        ballParams2.loadingMinRad = ((float) i2) / 11.5f;
        ballParamsArr[0].loadingMaxRad = ((float) i2) / 9.5f;
        ballParamsArr[0].noVoiceMaxRad = (float) (i2 / 5);
        ballParamsArr[0].noVoiceMinRad = (float) (i2 / 7);
        ballParamsArr[0].noVoice2HasVoiceRad = (float) (i2 / 9);
        ballParamsArr[0].layer = 0;
        ballParamsArr[0].hasVoiceMaxRad = (float) (((double) i2) / 7.5d);
        ballParamsArr[0].hasVoiceMinRad = (float) (i2 / 9);
        ballParamsArr[0].gradientUtil = new LinearGradientUtil(0, 0);
        this.mBallParams[1] = new BallParams(this, null);
        ballParamsArr = this.mBallParams;
        ballParams2 = ballParamsArr[1];
        i2 = this.mWidth;
        ballParams2.ctx = (float) (i2 / 2);
        ballParamsArr[1].cty = (float) (this.mHeight / 2);
        ballParamsArr[1].loadingMinRad = ((float) i2) / 11.5f;
        ballParamsArr[1].loadingMaxRad = ((float) i2) / 9.5f;
        ballParamsArr[1].noVoiceMaxRad = (float) (i2 / 11);
        ballParamsArr[1].noVoiceMinRad = (float) (i2 / 16);
        ballParamsArr[1].rad = (float) (i2 / 8);
        ballParamsArr[1].hasVoiceMaxRad = ((float) i2) / 3.5f;
        ballParamsArr[1].hasVoiceMinRad = (float) (i2 / 5);
        ballParamsArr[1].paint = new Paint();
        this.mBallParams[1].paint.setAntiAlias(true);
        ballParamsArr = this.mBallParams;
        ballParamsArr[1].color = this.mLoadingColor[0];
        ballParamsArr[1].paint.setColor(this.mBallParams[1].color);
        ballParamsArr = this.mBallParams;
        ballParamsArr[1].nowX = ballParamsArr[1].ctx;
        ballParamsArr = this.mBallParams;
        ballParamsArr[1].nowY = ballParamsArr[1].cty;
        ballParamsArr = this.mBallParams;
        ballParamsArr[1].noVoice2HasVoiceRad = (float) (this.mWidth / 30);
        ballParamsArr[1].layer = 1;
        ballParamsArr[1].gradientUtil = new LinearGradientUtil(0, 0);
        this.mResume = true;
        setPreState(State.RECORDING_NO_VOICE);
    }

    private void updateValueOrUi() {
        if (Math.abs(System.currentTimeMillis() - this.mLastInvalidTime) > 8) {
            invalidate();
            this.mLastInvalidTime = System.currentTimeMillis();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int len = this.mBallParams.length;
        for (int i = 0; i < len; i++) {
            for (BallParams params : this.mBallParams) {
                if (i == params.layer) {
                    canvas.drawCircle(params.nowX, params.nowY, params.rad, params.paint);
                }
            }
        }
    }
}
