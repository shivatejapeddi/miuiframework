package android.media;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Cea708CCParser.CaptionEvent;
import android.media.Cea708CCParser.CaptionPenAttr;
import android.media.Cea708CCParser.CaptionPenColor;
import android.media.Cea708CCParser.CaptionPenLocation;
import android.media.Cea708CCParser.CaptionWindow;
import android.media.Cea708CCParser.CaptionWindowAttr;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Layout.Alignment;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.CaptioningManager;
import android.view.accessibility.CaptioningManager.CaptionStyle;
import android.widget.RelativeLayout;
import com.android.internal.widget.SubtitleView;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import miui.maml.util.net.SimpleRequest;

/* compiled from: Cea708CaptionRenderer */
class Cea708CCWidget extends ClosedCaptionWidget implements DisplayListener {
    private final CCHandler mCCHandler;

    /* compiled from: Cea708CaptionRenderer */
    static class CCHandler implements Callback {
        private static final int CAPTION_ALL_WINDOWS_BITMAP = 255;
        private static final long CAPTION_CLEAR_INTERVAL_MS = 60000;
        private static final int CAPTION_WINDOWS_MAX = 8;
        private static final boolean DEBUG = false;
        private static final int MSG_CAPTION_CLEAR = 2;
        private static final int MSG_DELAY_CANCEL = 1;
        private static final String TAG = "CCHandler";
        private static final int TENTHS_OF_SECOND_IN_MILLIS = 100;
        private final CCLayout mCCLayout;
        private final CCWindowLayout[] mCaptionWindowLayouts = new CCWindowLayout[8];
        private CCWindowLayout mCurrentWindowLayout;
        private final Handler mHandler;
        private boolean mIsDelayed = false;
        private final ArrayList<CaptionEvent> mPendingCaptionEvents = new ArrayList();

        public CCHandler(CCLayout ccLayout) {
            this.mCCLayout = ccLayout;
            this.mHandler = new Handler((Callback) this);
        }

        public boolean handleMessage(Message msg) {
            int i = msg.what;
            if (i == 1) {
                delayCancel();
                return true;
            } else if (i != 2) {
                return false;
            } else {
                clearWindows(255);
                return true;
            }
        }

        public void processCaptionEvent(CaptionEvent event) {
            if (this.mIsDelayed) {
                this.mPendingCaptionEvents.add(event);
                return;
            }
            switch (event.type) {
                case 1:
                    sendBufferToCurrentWindow((String) event.obj);
                    break;
                case 2:
                    sendControlToCurrentWindow(((Character) event.obj).charValue());
                    break;
                case 3:
                    setCurrentWindowLayout(((Integer) event.obj).intValue());
                    break;
                case 4:
                    clearWindows(((Integer) event.obj).intValue());
                    break;
                case 5:
                    displayWindows(((Integer) event.obj).intValue());
                    break;
                case 6:
                    hideWindows(((Integer) event.obj).intValue());
                    break;
                case 7:
                    toggleWindows(((Integer) event.obj).intValue());
                    break;
                case 8:
                    deleteWindows(((Integer) event.obj).intValue());
                    break;
                case 9:
                    delay(((Integer) event.obj).intValue());
                    break;
                case 10:
                    delayCancel();
                    break;
                case 11:
                    reset();
                    break;
                case 12:
                    setPenAttr((CaptionPenAttr) event.obj);
                    break;
                case 13:
                    setPenColor((CaptionPenColor) event.obj);
                    break;
                case 14:
                    setPenLocation((CaptionPenLocation) event.obj);
                    break;
                case 15:
                    setWindowAttr((CaptionWindowAttr) event.obj);
                    break;
                case 16:
                    defineWindow((CaptionWindow) event.obj);
                    break;
            }
        }

        private void setCurrentWindowLayout(int windowId) {
            if (windowId >= 0) {
                CCWindowLayout windowLayout = this.mCaptionWindowLayouts;
                if (windowId < windowLayout.length) {
                    windowLayout = windowLayout[windowId];
                    if (windowLayout != null) {
                        this.mCurrentWindowLayout = windowLayout;
                    }
                }
            }
        }

        private ArrayList<CCWindowLayout> getWindowsFromBitmap(int windowBitmap) {
            ArrayList<CCWindowLayout> windows = new ArrayList();
            for (int i = 0; i < 8; i++) {
                if (((1 << i) & windowBitmap) != 0) {
                    CCWindowLayout windowLayout = this.mCaptionWindowLayouts[i];
                    if (windowLayout != null) {
                        windows.add(windowLayout);
                    }
                }
            }
            return windows;
        }

        private void clearWindows(int windowBitmap) {
            if (windowBitmap != 0) {
                Iterator it = getWindowsFromBitmap(windowBitmap).iterator();
                while (it.hasNext()) {
                    ((CCWindowLayout) it.next()).clear();
                }
            }
        }

        private void displayWindows(int windowBitmap) {
            if (windowBitmap != 0) {
                Iterator it = getWindowsFromBitmap(windowBitmap).iterator();
                while (it.hasNext()) {
                    ((CCWindowLayout) it.next()).show();
                }
            }
        }

        private void hideWindows(int windowBitmap) {
            if (windowBitmap != 0) {
                Iterator it = getWindowsFromBitmap(windowBitmap).iterator();
                while (it.hasNext()) {
                    ((CCWindowLayout) it.next()).hide();
                }
            }
        }

        private void toggleWindows(int windowBitmap) {
            if (windowBitmap != 0) {
                Iterator it = getWindowsFromBitmap(windowBitmap).iterator();
                while (it.hasNext()) {
                    CCWindowLayout windowLayout = (CCWindowLayout) it.next();
                    if (windowLayout.isShown()) {
                        windowLayout.hide();
                    } else {
                        windowLayout.show();
                    }
                }
            }
        }

        private void deleteWindows(int windowBitmap) {
            if (windowBitmap != 0) {
                Iterator it = getWindowsFromBitmap(windowBitmap).iterator();
                while (it.hasNext()) {
                    CCWindowLayout windowLayout = (CCWindowLayout) it.next();
                    windowLayout.removeFromCaptionView();
                    this.mCaptionWindowLayouts[windowLayout.getCaptionWindowId()] = null;
                }
            }
        }

        public void reset() {
            this.mCurrentWindowLayout = null;
            this.mIsDelayed = false;
            this.mPendingCaptionEvents.clear();
            for (int i = 0; i < 8; i++) {
                CCWindowLayout[] cCWindowLayoutArr = this.mCaptionWindowLayouts;
                if (cCWindowLayoutArr[i] != null) {
                    cCWindowLayoutArr[i].removeFromCaptionView();
                }
                this.mCaptionWindowLayouts[i] = null;
            }
            this.mCCLayout.setVisibility(4);
            this.mHandler.removeMessages(2);
        }

        private void setWindowAttr(CaptionWindowAttr windowAttr) {
            CCWindowLayout cCWindowLayout = this.mCurrentWindowLayout;
            if (cCWindowLayout != null) {
                cCWindowLayout.setWindowAttr(windowAttr);
            }
        }

        private void defineWindow(CaptionWindow window) {
            if (window != null) {
                int windowId = window.id;
                if (windowId >= 0) {
                    CCWindowLayout windowLayout = this.mCaptionWindowLayouts;
                    if (windowId < windowLayout.length) {
                        windowLayout = windowLayout[windowId];
                        if (windowLayout == null) {
                            windowLayout = new CCWindowLayout(this.mCCLayout.getContext());
                        }
                        windowLayout.initWindow(this.mCCLayout, window);
                        this.mCaptionWindowLayouts[windowId] = windowLayout;
                        this.mCurrentWindowLayout = windowLayout;
                    }
                }
            }
        }

        private void delay(int tenthsOfSeconds) {
            if (tenthsOfSeconds >= 0 && tenthsOfSeconds <= 255) {
                this.mIsDelayed = true;
                Handler handler = this.mHandler;
                handler.sendMessageDelayed(handler.obtainMessage(1), (long) (tenthsOfSeconds * 100));
            }
        }

        private void delayCancel() {
            this.mIsDelayed = false;
            processPendingBuffer();
        }

        private void processPendingBuffer() {
            Iterator it = this.mPendingCaptionEvents.iterator();
            while (it.hasNext()) {
                processCaptionEvent((CaptionEvent) it.next());
            }
            this.mPendingCaptionEvents.clear();
        }

        private void sendControlToCurrentWindow(char control) {
            CCWindowLayout cCWindowLayout = this.mCurrentWindowLayout;
            if (cCWindowLayout != null) {
                cCWindowLayout.sendControl(control);
            }
        }

        private void sendBufferToCurrentWindow(String buffer) {
            CCWindowLayout cCWindowLayout = this.mCurrentWindowLayout;
            if (cCWindowLayout != null) {
                cCWindowLayout.sendBuffer(buffer);
                this.mHandler.removeMessages(2);
                Handler handler = this.mHandler;
                handler.sendMessageDelayed(handler.obtainMessage(2), 60000);
            }
        }

        private void setPenAttr(CaptionPenAttr attr) {
            CCWindowLayout cCWindowLayout = this.mCurrentWindowLayout;
            if (cCWindowLayout != null) {
                cCWindowLayout.setPenAttr(attr);
            }
        }

        private void setPenColor(CaptionPenColor color) {
            CCWindowLayout cCWindowLayout = this.mCurrentWindowLayout;
            if (cCWindowLayout != null) {
                cCWindowLayout.setPenColor(color);
            }
        }

        private void setPenLocation(CaptionPenLocation location) {
            CCWindowLayout cCWindowLayout = this.mCurrentWindowLayout;
            if (cCWindowLayout != null) {
                cCWindowLayout.setPenLocation(location.row, location.column);
            }
        }
    }

    /* compiled from: Cea708CaptionRenderer */
    static class ScaledLayout extends ViewGroup {
        private static final boolean DEBUG = false;
        private static final String TAG = "ScaledLayout";
        private static final Comparator<Rect> mRectTopLeftSorter = new Comparator<Rect>() {
            public int compare(Rect lhs, Rect rhs) {
                if (lhs.top != rhs.top) {
                    return lhs.top - rhs.top;
                }
                return lhs.left - rhs.left;
            }
        };
        private Rect[] mRectArray;

        /* compiled from: Cea708CaptionRenderer */
        static class ScaledLayoutParams extends LayoutParams {
            public static final float SCALE_UNSPECIFIED = -1.0f;
            public float scaleEndCol;
            public float scaleEndRow;
            public float scaleStartCol;
            public float scaleStartRow;

            public ScaledLayoutParams(float scaleStartRow, float scaleEndRow, float scaleStartCol, float scaleEndCol) {
                super(-1, -1);
                this.scaleStartRow = scaleStartRow;
                this.scaleEndRow = scaleEndRow;
                this.scaleStartCol = scaleStartCol;
                this.scaleEndCol = scaleEndCol;
            }

            public ScaledLayoutParams(Context context, AttributeSet attrs) {
                super(-1, -1);
            }
        }

        public ScaledLayout(Context context) {
            super(context);
        }

        public LayoutParams generateLayoutParams(AttributeSet attrs) {
            return new ScaledLayoutParams(getContext(), attrs);
        }

        /* Access modifiers changed, original: protected */
        public boolean checkLayoutParams(LayoutParams p) {
            return p instanceof ScaledLayoutParams;
        }

        /* Access modifiers changed, original: protected */
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int i;
            int widthSpecSize;
            int i2;
            int j;
            int widthSpecSize2 = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
            int width = (widthSpecSize2 - getPaddingLeft()) - getPaddingRight();
            int height = (heightSpecSize - getPaddingTop()) - getPaddingBottom();
            int count = getChildCount();
            this.mRectArray = new Rect[count];
            int i3 = 0;
            while (i3 < count) {
                View child = getChildAt(i3);
                LayoutParams params = child.getLayoutParams();
                if (params instanceof ScaledLayoutParams) {
                    float scaleStartRow = ((ScaledLayoutParams) params).scaleStartRow;
                    float scaleEndRow = ((ScaledLayoutParams) params).scaleEndRow;
                    float scaleStartCol = ((ScaledLayoutParams) params).scaleStartCol;
                    float scaleEndCol = ((ScaledLayoutParams) params).scaleEndCol;
                    LayoutParams layoutParams;
                    if (scaleStartRow < 0.0f || scaleStartRow > 1.0f) {
                        i = heightSpecSize;
                        layoutParams = params;
                        throw new RuntimeException("A child of ScaledLayout should have a range of scaleStartRow between 0 and 1");
                    } else if (scaleEndRow < scaleStartRow || scaleStartRow > 1.0f) {
                        i = heightSpecSize;
                        layoutParams = params;
                        throw new RuntimeException("A child of ScaledLayout should have a range of scaleEndRow between scaleStartRow and 1");
                    } else if (scaleEndCol < 0.0f || scaleEndCol > 1.0f) {
                        i = heightSpecSize;
                        layoutParams = params;
                        throw new RuntimeException("A child of ScaledLayout should have a range of scaleStartCol between 0 and 1");
                    } else if (scaleEndCol < scaleStartCol || scaleEndCol > 1.0f) {
                        i = heightSpecSize;
                        layoutParams = params;
                        throw new RuntimeException("A child of ScaledLayout should have a range of scaleEndCol between scaleStartCol and 1");
                    } else {
                        widthSpecSize = widthSpecSize2;
                        i = heightSpecSize;
                        this.mRectArray[i3] = new Rect((int) (((float) width) * scaleStartCol), (int) (((float) height) * scaleStartRow), (int) (((float) width) * scaleEndCol), (int) (((float) height) * scaleEndRow));
                        widthSpecSize2 = MeasureSpec.makeMeasureSpec((int) (((float) width) * (scaleEndCol - scaleStartCol)), 1073741824);
                        child.measure(widthSpecSize2, MeasureSpec.makeMeasureSpec(0, 0));
                        if (child.getMeasuredHeight() > this.mRectArray[i3].height()) {
                            int overflowedHeight = ((child.getMeasuredHeight() - this.mRectArray[i3].height()) + 1) / 2;
                            Rect rect = this.mRectArray[i3];
                            rect.bottom += overflowedHeight;
                            rect = this.mRectArray[i3];
                            rect.top -= overflowedHeight;
                            if (this.mRectArray[i3].top < 0) {
                                rect = this.mRectArray[i3];
                                rect.bottom -= this.mRectArray[i3].top;
                                this.mRectArray[i3].top = 0;
                            }
                            if (this.mRectArray[i3].bottom > height) {
                                Rect rect2 = this.mRectArray[i3];
                                rect2.top -= this.mRectArray[i3].bottom - height;
                                this.mRectArray[i3].bottom = height;
                            }
                        }
                        child.measure(widthSpecSize2, MeasureSpec.makeMeasureSpec((int) (((float) height) * (scaleEndRow - scaleStartRow)), 1073741824));
                        i3++;
                        widthSpecSize2 = widthSpecSize;
                        heightSpecSize = i;
                    }
                } else {
                    i = heightSpecSize;
                    throw new RuntimeException("A child of ScaledLayout cannot have the UNSPECIFIED scale factors");
                }
            }
            widthSpecSize = widthSpecSize2;
            i = heightSpecSize;
            widthSpecSize2 = 0;
            int[] visibleRectGroup = new int[count];
            Rect[] visibleRectArray = new Rect[count];
            for (i2 = 0; i2 < count; i2++) {
                if (getChildAt(i2).getVisibility() == 0) {
                    visibleRectGroup[widthSpecSize2] = widthSpecSize2;
                    visibleRectArray[widthSpecSize2] = this.mRectArray[i2];
                    widthSpecSize2++;
                }
            }
            Arrays.sort(visibleRectArray, 0, widthSpecSize2, mRectTopLeftSorter);
            for (i2 = 0; i2 < widthSpecSize2 - 1; i2++) {
                for (j = i2 + 1; j < widthSpecSize2; j++) {
                    if (Rect.intersects(visibleRectArray[i2], visibleRectArray[j])) {
                        visibleRectGroup[j] = visibleRectGroup[i2];
                        visibleRectArray[j].set(visibleRectArray[j].left, visibleRectArray[i2].bottom, visibleRectArray[j].right, visibleRectArray[i2].bottom + visibleRectArray[j].height());
                    }
                }
            }
            for (i2 = widthSpecSize2 - 1; i2 >= 0; i2--) {
                if (visibleRectArray[i2].bottom > height) {
                    j = visibleRectArray[i2].bottom - height;
                    for (int j2 = 0; j2 <= i2; j2++) {
                        if (visibleRectGroup[i2] == visibleRectGroup[j2]) {
                            visibleRectArray[j2].set(visibleRectArray[j2].left, visibleRectArray[j2].top - j, visibleRectArray[j2].right, visibleRectArray[j2].bottom - j);
                        }
                    }
                }
            }
            setMeasuredDimension(widthSpecSize, i);
        }

        /* Access modifiers changed, original: protected */
        public void onLayout(boolean changed, int l, int t, int r, int b) {
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != 8) {
                    child.layout(this.mRectArray[i].left + paddingLeft, this.mRectArray[i].top + paddingTop, this.mRectArray[i].right + paddingTop, this.mRectArray[i].bottom + paddingLeft);
                }
            }
        }

        public void dispatchDraw(Canvas canvas) {
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != 8) {
                    Rect[] rectArr = this.mRectArray;
                    if (i < rectArr.length) {
                        int childLeft = rectArr[i].left + paddingLeft;
                        int childTop = this.mRectArray[i].top + paddingTop;
                        int saveCount = canvas.save();
                        canvas.translate((float) childLeft, (float) childTop);
                        child.draw(canvas);
                        canvas.restoreToCount(saveCount);
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* compiled from: Cea708CaptionRenderer */
    static class CCLayout extends ScaledLayout implements ClosedCaptionLayout {
        private static final float SAFE_TITLE_AREA_SCALE_END_X = 0.9f;
        private static final float SAFE_TITLE_AREA_SCALE_END_Y = 0.9f;
        private static final float SAFE_TITLE_AREA_SCALE_START_X = 0.1f;
        private static final float SAFE_TITLE_AREA_SCALE_START_Y = 0.1f;
        private final ScaledLayout mSafeTitleAreaLayout;

        public CCLayout(Context context) {
            super(context);
            this.mSafeTitleAreaLayout = new ScaledLayout(context);
            addView((View) this.mSafeTitleAreaLayout, (LayoutParams) new ScaledLayoutParams(0.1f, 0.9f, 0.1f, 0.9f));
        }

        public void addOrUpdateViewToSafeTitleArea(CCWindowLayout captionWindowLayout, ScaledLayoutParams scaledLayoutParams) {
            if (this.mSafeTitleAreaLayout.indexOfChild(captionWindowLayout) < 0) {
                this.mSafeTitleAreaLayout.addView((View) captionWindowLayout, (LayoutParams) scaledLayoutParams);
            } else {
                this.mSafeTitleAreaLayout.updateViewLayout(captionWindowLayout, scaledLayoutParams);
            }
        }

        public void removeViewFromSafeTitleArea(CCWindowLayout captionWindowLayout) {
            this.mSafeTitleAreaLayout.removeView(captionWindowLayout);
        }

        public void setCaptionStyle(CaptionStyle style) {
            int count = this.mSafeTitleAreaLayout.getChildCount();
            for (int i = 0; i < count; i++) {
                ((CCWindowLayout) this.mSafeTitleAreaLayout.getChildAt(i)).setCaptionStyle(style);
            }
        }

        public void setFontScale(float fontScale) {
            int count = this.mSafeTitleAreaLayout.getChildCount();
            for (int i = 0; i < count; i++) {
                ((CCWindowLayout) this.mSafeTitleAreaLayout.getChildAt(i)).setFontScale(fontScale);
            }
        }
    }

    /* compiled from: Cea708CaptionRenderer */
    static class CCView extends SubtitleView {
        private static final CaptionStyle DEFAULT_CAPTION_STYLE = CaptionStyle.DEFAULT;

        public CCView(Context context) {
            this(context, null);
        }

        public CCView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public CCView(Context context, AttributeSet attrs, int defStyleAttr) {
            this(context, attrs, defStyleAttr, 0);
        }

        public CCView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public void setCaptionStyle(CaptionStyle style) {
            setForegroundColor(style.hasForegroundColor() ? style.foregroundColor : DEFAULT_CAPTION_STYLE.foregroundColor);
            setBackgroundColor(style.hasBackgroundColor() ? style.backgroundColor : DEFAULT_CAPTION_STYLE.backgroundColor);
            setEdgeType(style.hasEdgeType() ? style.edgeType : DEFAULT_CAPTION_STYLE.edgeType);
            setEdgeColor(style.hasEdgeColor() ? style.edgeColor : DEFAULT_CAPTION_STYLE.edgeColor);
            setTypeface(style.getTypeface());
        }
    }

    /* compiled from: Cea708CaptionRenderer */
    static class CCWindowLayout extends RelativeLayout implements OnLayoutChangeListener {
        private static final int ANCHOR_HORIZONTAL_16_9_MAX = 209;
        private static final int ANCHOR_HORIZONTAL_MODE_CENTER = 1;
        private static final int ANCHOR_HORIZONTAL_MODE_LEFT = 0;
        private static final int ANCHOR_HORIZONTAL_MODE_RIGHT = 2;
        private static final int ANCHOR_MODE_DIVIDER = 3;
        private static final int ANCHOR_RELATIVE_POSITIONING_MAX = 99;
        private static final int ANCHOR_VERTICAL_MAX = 74;
        private static final int ANCHOR_VERTICAL_MODE_BOTTOM = 2;
        private static final int ANCHOR_VERTICAL_MODE_CENTER = 1;
        private static final int ANCHOR_VERTICAL_MODE_TOP = 0;
        private static final int MAX_COLUMN_COUNT_16_9 = 42;
        private static final float PROPORTION_PEN_SIZE_LARGE = 1.25f;
        private static final float PROPORTION_PEN_SIZE_SMALL = 0.75f;
        private static final String TAG = "CCWindowLayout";
        private final SpannableStringBuilder mBuilder;
        private CCLayout mCCLayout;
        private CCView mCCView;
        private CaptionStyle mCaptionStyle;
        private int mCaptionWindowId;
        private final List<CharacterStyle> mCharacterStyles;
        private float mFontScale;
        private int mLastCaptionLayoutHeight;
        private int mLastCaptionLayoutWidth;
        private int mRow;
        private int mRowLimit;
        private float mTextSize;
        private String mWidestChar;

        public CCWindowLayout(Context context) {
            this(context, null);
        }

        public CCWindowLayout(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public CCWindowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            this(context, attrs, defStyleAttr, 0);
        }

        public CCWindowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            this.mRowLimit = 0;
            this.mBuilder = new SpannableStringBuilder();
            this.mCharacterStyles = new ArrayList();
            this.mRow = -1;
            this.mCCView = new CCView(context);
            addView((View) this.mCCView, (LayoutParams) new RelativeLayout.LayoutParams(-2, -2));
            CaptioningManager captioningManager = (CaptioningManager) context.getSystemService(Context.CAPTIONING_SERVICE);
            this.mFontScale = captioningManager.getFontScale();
            setCaptionStyle(captioningManager.getUserStyle());
            this.mCCView.setText((CharSequence) "");
            updateWidestChar();
        }

        public void setCaptionStyle(CaptionStyle style) {
            this.mCaptionStyle = style;
            this.mCCView.setCaptionStyle(style);
        }

        public void setFontScale(float fontScale) {
            this.mFontScale = fontScale;
            updateTextSize();
        }

        public int getCaptionWindowId() {
            return this.mCaptionWindowId;
        }

        public void setCaptionWindowId(int captionWindowId) {
            this.mCaptionWindowId = captionWindowId;
        }

        public void clear() {
            clearText();
            hide();
        }

        public void show() {
            setVisibility(0);
            requestLayout();
        }

        public void hide() {
            setVisibility(4);
            requestLayout();
        }

        public void setPenAttr(CaptionPenAttr penAttr) {
            this.mCharacterStyles.clear();
            if (penAttr.italic) {
                this.mCharacterStyles.add(new StyleSpan(2));
            }
            if (penAttr.underline) {
                this.mCharacterStyles.add(new UnderlineSpan());
            }
            int i = penAttr.penSize;
            if (i == 0) {
                this.mCharacterStyles.add(new RelativeSizeSpan(0.75f));
            } else if (i == 2) {
                this.mCharacterStyles.add(new RelativeSizeSpan((float) PROPORTION_PEN_SIZE_LARGE));
            }
            i = penAttr.penOffset;
            if (i == 0) {
                this.mCharacterStyles.add(new SubscriptSpan());
            } else if (i == 2) {
                this.mCharacterStyles.add(new SuperscriptSpan());
            }
        }

        public void setPenColor(CaptionPenColor penColor) {
        }

        public void setPenLocation(int row, int column) {
            if (this.mRow >= 0) {
                for (int r = this.mRow; r < row; r++) {
                    appendText("\n");
                }
            }
            this.mRow = row;
        }

        public void setWindowAttr(CaptionWindowAttr windowAttr) {
        }

        public void sendBuffer(String buffer) {
            appendText(buffer);
        }

        public void sendControl(char control) {
        }

        public void initWindow(CCLayout ccLayout, CaptionWindow captionWindow) {
            StringBuilder stringBuilder;
            float halfMaxWidthScale;
            CCLayout cCLayout = ccLayout;
            CaptionWindow captionWindow2 = captionWindow;
            CCLayout cCLayout2 = this.mCCLayout;
            if (cCLayout2 != cCLayout) {
                if (cCLayout2 != null) {
                    cCLayout2.removeOnLayoutChangeListener(this);
                }
                this.mCCLayout = cCLayout;
                this.mCCLayout.addOnLayoutChangeListener(this);
                updateWidestChar();
            }
            int i = 99;
            float scaleRow = ((float) captionWindow2.anchorVertical) / ((float) (captionWindow2.relativePositioning ? 99 : 74));
            float scaleCol = (float) captionWindow2.anchorHorizontal;
            if (!captionWindow2.relativePositioning) {
                i = 209;
            }
            scaleCol /= (float) i;
            int i2 = (scaleRow > 0.0f ? 1 : (scaleRow == 0.0f ? 0 : -1));
            String str = TAG;
            if (i2 < 0 || scaleRow > 1.0f) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("The vertical position of the anchor point should be at the range of 0 and 1 but ");
                stringBuilder.append(scaleRow);
                Log.i(str, stringBuilder.toString());
                scaleRow = Math.max(0.0f, Math.min(scaleRow, 1.0f));
            }
            if (scaleCol < 0.0f || scaleCol > 1.0f) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("The horizontal position of the anchor point should be at the range of 0 and 1 but ");
                stringBuilder.append(scaleCol);
                Log.i(str, stringBuilder.toString());
                scaleCol = Math.max(0.0f, Math.min(scaleCol, 1.0f));
            }
            i2 = 17;
            int horizontalMode = captionWindow2.anchorId % 3;
            int verticalMode = captionWindow2.anchorId / 3;
            float scaleStartRow = 0.0f;
            float scaleEndRow = 1.0f;
            float scaleStartCol = 0.0f;
            float scaleEndCol = 1.0f;
            if (horizontalMode == 0) {
                i2 = 3;
                this.mCCView.setAlignment(Alignment.ALIGN_NORMAL);
                scaleStartCol = scaleCol;
            } else if (horizontalMode == 1) {
                float gap = Math.min(1.0f - scaleCol, scaleCol);
                int columnCount = Math.min(getScreenColumnCount(), captionWindow2.columnCount + 1);
                StringBuilder widestTextBuilder = new StringBuilder();
                i = 0;
                while (i < columnCount) {
                    widestTextBuilder.append(this.mWidestChar);
                    i++;
                    cCLayout = ccLayout;
                }
                Paint paint = new Paint();
                paint.setTypeface(this.mCaptionStyle.getTypeface());
                paint.setTextSize(this.mTextSize);
                float maxWindowWidth = paint.measureText(widestTextBuilder.toString());
                if (this.mCCLayout.getWidth() > 0) {
                    maxWindowWidth = (maxWindowWidth / 2.0f) / (((float) this.mCCLayout.getWidth()) * 0.8f);
                } else {
                    maxWindowWidth = 0.0f;
                }
                halfMaxWidthScale = maxWindowWidth;
                if (halfMaxWidthScale <= 0.0f || halfMaxWidthScale >= scaleCol) {
                    i2 = 1;
                    this.mCCView.setAlignment(Alignment.ALIGN_CENTER);
                    scaleStartCol = scaleCol - gap;
                    scaleEndCol = scaleCol + gap;
                } else {
                    int gravity = 3;
                    this.mCCView.setAlignment(Alignment.ALIGN_NORMAL);
                    scaleStartCol = scaleCol - halfMaxWidthScale;
                    scaleEndCol = 1.0f;
                    i2 = gravity;
                }
            } else if (horizontalMode == 2) {
                i2 = 5;
                this.mCCView.setAlignment(Alignment.ALIGN_RIGHT);
                scaleEndCol = scaleCol;
            }
            if (verticalMode == 0) {
                i2 |= 48;
                scaleStartRow = scaleRow;
            } else if (verticalMode == 1) {
                i2 |= 16;
                halfMaxWidthScale = Math.min(1.0f - scaleRow, scaleRow);
                scaleStartRow = scaleRow - halfMaxWidthScale;
                scaleEndRow = scaleRow + halfMaxWidthScale;
            } else if (verticalMode == 2) {
                i2 |= 80;
                scaleEndRow = scaleRow;
            }
            this.mCCLayout.addOrUpdateViewToSafeTitleArea(this, new ScaledLayoutParams(scaleStartRow, scaleEndRow, scaleStartCol, scaleEndCol));
            setCaptionWindowId(captionWindow2.id);
            setRowLimit(captionWindow2.rowCount);
            setGravity(i2);
            if (captionWindow2.visible) {
                show();
            } else {
                hide();
            }
        }

        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            int width = right - left;
            int height = bottom - top;
            if (width != this.mLastCaptionLayoutWidth || height != this.mLastCaptionLayoutHeight) {
                this.mLastCaptionLayoutWidth = width;
                this.mLastCaptionLayoutHeight = height;
                updateTextSize();
            }
        }

        private void updateWidestChar() {
            Paint paint = new Paint();
            paint.setTypeface(this.mCaptionStyle.getTypeface());
            Charset latin1 = Charset.forName(SimpleRequest.ISO_8859_1);
            float widestCharWidth = 0.0f;
            for (int i = 0; i < 256; i++) {
                String ch = new String(new byte[]{(byte) i}, latin1);
                float charWidth = paint.measureText(ch);
                if (widestCharWidth < charWidth) {
                    widestCharWidth = charWidth;
                    this.mWidestChar = ch;
                }
            }
            updateTextSize();
        }

        private void updateTextSize() {
            if (this.mCCLayout != null) {
                StringBuilder widestTextBuilder = new StringBuilder();
                int screenColumnCount = getScreenColumnCount();
                for (int i = 0; i < screenColumnCount; i++) {
                    widestTextBuilder.append(this.mWidestChar);
                }
                String widestText = widestTextBuilder.toString();
                Paint paint = new Paint();
                paint.setTypeface(this.mCaptionStyle.getTypeface());
                float startFontSize = 0.0f;
                float endFontSize = 255.0f;
                while (startFontSize < endFontSize) {
                    float testTextSize = (startFontSize + endFontSize) / 2.0f;
                    paint.setTextSize(testTextSize);
                    if (((float) this.mCCLayout.getWidth()) * 0.8f > paint.measureText(widestText)) {
                        startFontSize = 0.01f + testTextSize;
                    } else {
                        endFontSize = testTextSize - 0.01f;
                    }
                }
                this.mTextSize = this.mFontScale * endFontSize;
                this.mCCView.setTextSize(this.mTextSize);
            }
        }

        private int getScreenColumnCount() {
            return 42;
        }

        public void removeFromCaptionView() {
            CCLayout cCLayout = this.mCCLayout;
            if (cCLayout != null) {
                cCLayout.removeViewFromSafeTitleArea(this);
                this.mCCLayout.removeOnLayoutChangeListener(this);
                this.mCCLayout = null;
            }
        }

        public void setText(String text) {
            updateText(text, false);
        }

        public void appendText(String text) {
            updateText(text, true);
        }

        public void clearText() {
            this.mBuilder.clear();
            this.mCCView.setText((CharSequence) "");
        }

        private void updateText(String text, boolean appended) {
            if (!appended) {
                this.mBuilder.clear();
            }
            if (text != null && text.length() > 0) {
                int length = this.mBuilder.length();
                this.mBuilder.append((CharSequence) text);
                for (CharacterStyle characterStyle : this.mCharacterStyles) {
                    SpannableStringBuilder spannableStringBuilder = this.mBuilder;
                    spannableStringBuilder.setSpan(characterStyle, length, spannableStringBuilder.length(), 33);
                }
            }
            CharSequence truncatedText = "\n";
            String[] lines = TextUtils.split(this.mBuilder.toString(), (String) truncatedText);
            String truncatedText2 = TextUtils.join(truncatedText, Arrays.copyOfRange(lines, Math.max(0, lines.length - (this.mRowLimit + 1)), lines.length));
            SpannableStringBuilder spannableStringBuilder2 = this.mBuilder;
            spannableStringBuilder2.delete(0, spannableStringBuilder2.length() - truncatedText2.length());
            int start = 0;
            int last = this.mBuilder.length() - 1;
            int end = last;
            while (start <= end && this.mBuilder.charAt(start) <= ' ') {
                start++;
            }
            while (end >= start && this.mBuilder.charAt(end) <= ' ') {
                end--;
            }
            if (start == 0 && end == last) {
                this.mCCView.setText((CharSequence) this.mBuilder);
                return;
            }
            SpannableStringBuilder trim = new SpannableStringBuilder();
            trim.append(this.mBuilder);
            if (end < last) {
                trim.delete(end + 1, last + 1);
            }
            if (start > 0) {
                trim.delete(0, start);
            }
            this.mCCView.setText((CharSequence) trim);
        }

        public void setRowLimit(int rowLimit) {
            if (rowLimit >= 0) {
                this.mRowLimit = rowLimit;
                return;
            }
            throw new IllegalArgumentException("A rowLimit should have a positive number");
        }
    }

    public Cea708CCWidget(Context context) {
        this(context, null);
    }

    public Cea708CCWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Cea708CCWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Cea708CCWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCCHandler = new CCHandler((CCLayout) this.mClosedCaptionLayout);
    }

    public ClosedCaptionLayout createCaptionLayout(Context context) {
        return new CCLayout(context);
    }

    public void emitEvent(CaptionEvent event) {
        this.mCCHandler.processCaptionEvent(event);
        setSize(getWidth(), getHeight());
        if (this.mListener != null) {
            this.mListener.onChanged(this);
        }
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ((ViewGroup) this.mClosedCaptionLayout).draw(canvas);
    }
}
