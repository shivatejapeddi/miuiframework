package miui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioSystem;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.miui.R;
import android.os.Build.VERSION;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import miui.util.FeatureParser;

public class SpectrumVisualizer extends ImageView {
    private static final int CONSIDER_SAMPLE_LENGTH = 160;
    public static boolean IS_LPA_DECODE = SystemProperties.getBoolean("persist.sys.lpa.decode", false);
    private static final int RES_DEFAULT_SLIDING_DOT_BAR_ID = 285671771;
    private static final int RES_DEFAULT_SLIDING_PANEL_ID = 285671770;
    private static final int RES_DEFAULT_SLIDING_SHADOW_DOT_BAR_ID = 285671772;
    private static final String TAG = "SpectrumVisualizer";
    private static final int VISUALIZATION_SAMPLE_LENGTH = 256;
    private float INDEX_SCALE_FACTOR;
    private final int MAX_VALID_SAMPLE;
    private float SAMPLE_SCALE_FACTOR;
    private float VISUALIZE_DESC_HEIGHT;
    int mAlphaWidthNum;
    private Bitmap mCachedBitmap;
    private Canvas mCachedCanvas;
    int mCellSize;
    int mDotbarHeight;
    private DotBarDrawer mDrawer;
    private boolean mEnableDrawing;
    private boolean mIsEnableUpdate;
    private boolean mIsNeedCareStreamActive;
    private OnDataCaptureListener mOnDataCaptureListener;
    Paint mPaint;
    int[] mPixels;
    float[] mPointData;
    private short[] mSampleBuf;
    int mShadowDotbarHeight;
    int[] mShadowPixels;
    private boolean mSoftDrawEnabled;
    private int mVisualizationHeight;
    int mVisualizationHeightNum;
    private int mVisualizationWidth;
    int mVisualizationWidthNum;
    private Visualizer mVisualizer;

    private interface DotBarDrawer {
        void drawDotBar(Canvas canvas, int i);
    }

    class AsymmetryDotBar implements DotBarDrawer {
        AsymmetryDotBar() {
        }

        public void drawDotBar(Canvas canvas, int index) {
            int top = ((int) (((double) ((((float) SpectrumVisualizer.this.mDotbarHeight) * (1.0f - SpectrumVisualizer.this.mPointData[index])) / ((float) SpectrumVisualizer.this.mCellSize))) + 0.5d)) * SpectrumVisualizer.this.mCellSize;
            if (top < SpectrumVisualizer.this.mDotbarHeight) {
                canvas.drawBitmap(SpectrumVisualizer.this.mPixels, SpectrumVisualizer.this.mCellSize * top, SpectrumVisualizer.this.mCellSize, SpectrumVisualizer.this.mCellSize * index, top, SpectrumVisualizer.this.mCellSize, SpectrumVisualizer.this.mDotbarHeight - top, true, SpectrumVisualizer.this.mPaint);
            }
        }
    }

    class SymmetryDotBar implements DotBarDrawer {
        SymmetryDotBar() {
        }

        public void drawDotBar(Canvas canvas, int index) {
            int top = ((int) (((double) ((((float) SpectrumVisualizer.this.mDotbarHeight) * (1.0f - SpectrumVisualizer.this.mPointData[index])) / ((float) SpectrumVisualizer.this.mCellSize))) + 0.5d)) * SpectrumVisualizer.this.mCellSize;
            if (top < SpectrumVisualizer.this.mDotbarHeight) {
                canvas.drawBitmap(SpectrumVisualizer.this.mPixels, SpectrumVisualizer.this.mCellSize * top, SpectrumVisualizer.this.mCellSize, SpectrumVisualizer.this.mCellSize * index, top, SpectrumVisualizer.this.mCellSize, SpectrumVisualizer.this.mDotbarHeight - top, true, SpectrumVisualizer.this.mPaint);
            }
            int bottom = ((int) (((double) ((((float) SpectrumVisualizer.this.mShadowDotbarHeight) * SpectrumVisualizer.this.mPointData[index]) / ((float) SpectrumVisualizer.this.mCellSize))) + 0.5d)) * SpectrumVisualizer.this.mCellSize;
            if (bottom > SpectrumVisualizer.this.mShadowDotbarHeight) {
                bottom = SpectrumVisualizer.this.mShadowDotbarHeight;
            }
            if (bottom > 0) {
                canvas.drawBitmap(SpectrumVisualizer.this.mShadowPixels, 0, SpectrumVisualizer.this.mCellSize, SpectrumVisualizer.this.mCellSize * index, SpectrumVisualizer.this.mDotbarHeight, SpectrumVisualizer.this.mCellSize, bottom, true, SpectrumVisualizer.this.mPaint);
            }
        }
    }

    public SpectrumVisualizer(Context context) {
        super(context);
        this.mPaint = new Paint();
        this.mSampleBuf = new short[160];
        this.mSoftDrawEnabled = true;
        this.mOnDataCaptureListener = new OnDataCaptureListener() {
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
            }

            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
                SpectrumVisualizer.this.update(bytes);
            }
        };
        this.MAX_VALID_SAMPLE = 20;
        init(context, null);
    }

    public SpectrumVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mPaint = new Paint();
        this.mSampleBuf = new short[160];
        this.mSoftDrawEnabled = true;
        this.mOnDataCaptureListener = /* anonymous class already generated */;
        this.MAX_VALID_SAMPLE = 20;
        init(context, attrs);
    }

    public SpectrumVisualizer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mPaint = new Paint();
        this.mSampleBuf = new short[160];
        this.mSoftDrawEnabled = true;
        this.mOnDataCaptureListener = /* anonymous class already generated */;
        this.MAX_VALID_SAMPLE = 20;
        init(context, attrs);
    }

    public void setBitmaps(Bitmap panel, Bitmap dotbar, Bitmap shadow) {
        setImageBitmap(panel);
        setBitmaps(panel.getWidth(), panel.getHeight(), dotbar, shadow);
    }

    public void setBitmaps(int width, int height, Bitmap dotbar, Bitmap shadow) {
        this.mVisualizationWidth = width;
        this.mVisualizationHeight = height;
        this.mCellSize = dotbar.getWidth();
        this.mDotbarHeight = dotbar.getHeight();
        int i = this.mDotbarHeight;
        int i2 = this.mVisualizationHeight;
        if (i > i2) {
            this.mDotbarHeight = i2;
        }
        int i3 = this.mCellSize;
        int i4 = this.mDotbarHeight;
        this.mPixels = new int[(i3 * i4)];
        dotbar.getPixels(this.mPixels, 0, i3, 0, 0, i3, i4);
        i = this.mVisualizationWidth;
        i2 = this.mCellSize;
        this.mVisualizationWidthNum = i / i2;
        this.mVisualizationHeightNum = this.mDotbarHeight / i2;
        this.SAMPLE_SCALE_FACTOR = 20.0f / ((float) this.mVisualizationHeightNum);
        this.INDEX_SCALE_FACTOR = (float) Math.log((double) (this.mVisualizationWidthNum / 3));
        this.VISUALIZE_DESC_HEIGHT = 1.0f / ((float) this.mVisualizationHeightNum);
        i = this.mVisualizationWidthNum;
        this.mPointData = new float[i];
        if (this.mAlphaWidthNum == 0) {
            this.mAlphaWidthNum = i / 2;
        }
        this.mShadowPixels = null;
        if (shadow != null) {
            this.mShadowDotbarHeight = shadow.getHeight();
            i = this.mShadowDotbarHeight;
            i2 = this.mDotbarHeight;
            i += i2;
            int i5 = this.mVisualizationHeight;
            if (i > i5) {
                this.mShadowDotbarHeight = i5 - i2;
            }
            int i6 = this.mShadowDotbarHeight;
            i4 = this.mCellSize;
            if (i6 < i4) {
                this.mDrawer = new AsymmetryDotBar();
                return;
            }
            this.mShadowPixels = new int[(i4 * i6)];
            shadow.getPixels(this.mShadowPixels, 0, i4, 0, 0, i4, i6);
            this.mDrawer = new SymmetryDotBar();
        } else {
            this.mDrawer = new AsymmetryDotBar();
        }
    }

    public void setAlphaNum(int num) {
        if (num <= 0) {
            this.mAlphaWidthNum = 0;
            return;
        }
        int i = this.mVisualizationWidthNum;
        this.mAlphaWidthNum = num > i / 2 ? i / 2 : num;
    }

    private void init(Context context, AttributeSet attrs) {
        Drawable panelDrawable = null;
        Drawable dotBarDrawble = null;
        Drawable shadowDotbarDrawable = null;
        boolean symmetry = false;
        this.mEnableDrawing = true;
        this.mIsNeedCareStreamActive = true;
        this.mAlphaWidthNum = 0;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpectrumVisualizer);
            panelDrawable = a.getDrawable(3);
            dotBarDrawble = a.getDrawable(2);
            shadowDotbarDrawable = a.getDrawable(4);
            symmetry = a.getBoolean(5, false);
            this.mAlphaWidthNum = a.getInt(0, this.mAlphaWidthNum);
            this.mIsEnableUpdate = a.getBoolean(6, false);
            this.mIsNeedCareStreamActive = a.getBoolean(1, false);
            a.recycle();
        }
        if (panelDrawable == null) {
            panelDrawable = context.getResources().getDrawable(285671770);
        }
        Bitmap panelBm = ((BitmapDrawable) panelDrawable).getBitmap();
        if (dotBarDrawble == null) {
            dotBarDrawble = context.getResources().getDrawable(285671771);
        }
        Bitmap dotBar = ((BitmapDrawable) dotBarDrawble).getBitmap();
        Bitmap shadowDotBar = null;
        if (symmetry) {
            if (shadowDotbarDrawable == null) {
                shadowDotbarDrawable = context.getResources().getDrawable(285671772);
            }
            shadowDotBar = ((BitmapDrawable) shadowDotbarDrawable).getBitmap();
        }
        setBitmaps(panelBm, dotBar, shadowDotBar);
    }

    public int getVisualHeight() {
        return this.mVisualizationHeight;
    }

    public int getVisualWidth() {
        return this.mVisualizationWidth;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mEnableDrawing) {
            if (this.mSoftDrawEnabled) {
                canvas.drawBitmap(drawToBitmap(), 0.0f, 0.0f, null);
            } else {
                drawInternal(canvas);
            }
        }
    }

    public void setSoftDrawEnabled(boolean endabled) {
        this.mSoftDrawEnabled = endabled;
        if (!endabled) {
            Bitmap bitmap = this.mCachedBitmap;
            if (bitmap != null) {
                bitmap.recycle();
                this.mCachedBitmap = null;
                this.mCachedCanvas = null;
            }
        }
    }

    private Bitmap drawToBitmap() {
        Bitmap bm = this.mCachedBitmap;
        Canvas canvas = this.mCachedCanvas;
        if (!(bm == null || (bm.getWidth() == getWidth() && bm.getHeight() == getHeight()))) {
            bm.recycle();
            bm = null;
        }
        if (bm == null) {
            bm = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
            this.mCachedBitmap = bm;
            canvas = new Canvas(bm);
            this.mCachedCanvas = canvas;
        }
        canvas.drawColor(0, Mode.CLEAR);
        drawInternal(canvas);
        return bm;
    }

    private void drawInternal(Canvas canvas) {
        int i;
        this.mPaint.setAlpha(255);
        int end = this.mVisualizationWidthNum - this.mAlphaWidthNum;
        for (i = this.mAlphaWidthNum; i < end; i++) {
            this.mDrawer.drawDotBar(canvas, i);
        }
        for (i = this.mAlphaWidthNum; i > 0; i--) {
            this.mPaint.setAlpha((i * 255) / this.mAlphaWidthNum);
            this.mDrawer.drawDotBar(canvas, i - 1);
            this.mDrawer.drawDotBar(canvas, this.mVisualizationWidthNum - i);
        }
    }

    public boolean isUpdateEnabled() {
        return this.mIsEnableUpdate;
    }

    public void enableDrawing(boolean enable) {
        this.mEnableDrawing = enable;
    }

    public void enableUpdate(boolean enable) {
        try {
            if (this.mIsEnableUpdate != enable) {
                if (enable && this.mVisualizer == null) {
                    if (IS_LPA_DECODE) {
                        Log.v("SpectrumVisualizer", "lpa decode is on, can't enable");
                    } else {
                        this.mVisualizer = new Visualizer(0);
                        if (!this.mVisualizer.getEnabled()) {
                            this.mVisualizer.setCaptureSize(512);
                            this.mVisualizer.setDataCaptureListener(this.mOnDataCaptureListener, Visualizer.getMaxCaptureRate(), false, true);
                            this.mVisualizer.setEnabled(true);
                        }
                    }
                } else if (!(enable || this.mVisualizer == null)) {
                    this.mVisualizer.setEnabled(false);
                    if (VERSION.SDK_INT < 22) {
                        if (!FeatureParser.getBoolean("is_xiaomi_device", false)) {
                            Thread.sleep(50);
                        }
                    }
                    this.mVisualizer.release();
                    this.mVisualizer = null;
                }
                this.mIsEnableUpdate = enable;
            }
        } catch (IllegalStateException | RuntimeException e) {
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void update(byte[] fFtBuffer) {
        if (!this.mIsNeedCareStreamActive || AudioSystem.isStreamActive(3, 0)) {
            enableDrawing(true);
            if (fFtBuffer != null) {
                int i;
                int a;
                int b;
                int c;
                short[] sampleBuf = this.mSampleBuf;
                int sampleLen = sampleBuf.length;
                for (i = 0; i < sampleLen; i++) {
                    a = fFtBuffer[i * 2];
                    b = fFtBuffer[(i * 2) + 1];
                    c = (int) Math.sqrt((double) ((a * a) + (b * b)));
                    int i2 = 32767;
                    if (c < 32767) {
                        i2 = c;
                    }
                    sampleBuf[i] = (short) i2;
                }
                i = 0;
                a = 0;
                for (b = 0; b < this.mVisualizationWidthNum; b++) {
                    float f;
                    float rawData;
                    c = 0;
                    while (a < sampleLen) {
                        c = Math.max(c, sampleBuf[i]);
                        i++;
                        a += this.mVisualizationWidthNum;
                    }
                    a -= sampleLen;
                    if (c > 1) {
                        f = (float) (Math.log((double) (b + 2)) / ((double) this.INDEX_SCALE_FACTOR));
                        rawData = (((float) (c - 1)) * f) * f;
                    } else {
                        rawData = 0.0f;
                    }
                    if (rawData > 20.0f) {
                        f = (float) this.mVisualizationHeightNum;
                    } else {
                        f = rawData / this.SAMPLE_SCALE_FACTOR;
                    }
                    float[] fArr = this.mPointData;
                    fArr[b] = Math.max(f / ((float) this.mVisualizationHeightNum), fArr[b] - this.VISUALIZE_DESC_HEIGHT);
                }
                invalidate();
                return;
            }
            return;
        }
        enableDrawing(false);
    }
}
