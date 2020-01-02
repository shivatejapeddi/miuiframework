package android.media;

import android.content.Context;
import android.media.SubtitleTrack.Cue;
import android.media.SubtitleTrack.RenderingWidget;
import android.media.SubtitleTrack.RenderingWidget.OnChangedListener;
import android.text.Layout.Alignment;
import android.text.SpannableStringBuilder;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.accessibility.CaptioningManager;
import android.view.accessibility.CaptioningManager.CaptionStyle;
import android.view.accessibility.CaptioningManager.CaptioningChangeListener;
import android.widget.LinearLayout;
import com.android.internal.widget.SubtitleView;
import java.util.ArrayList;
import java.util.Vector;

/* compiled from: WebVttRenderer */
class WebVttRenderingWidget extends ViewGroup implements RenderingWidget {
    private static final boolean DEBUG = false;
    private static final int DEBUG_CUE_BACKGROUND = -2130771968;
    private static final int DEBUG_REGION_BACKGROUND = -2147483393;
    private static final CaptionStyle DEFAULT_CAPTION_STYLE = CaptionStyle.DEFAULT;
    private static final float LINE_HEIGHT_RATIO = 0.0533f;
    private CaptionStyle mCaptionStyle;
    private final CaptioningChangeListener mCaptioningListener;
    private final ArrayMap<TextTrackCue, CueLayout> mCueBoxes;
    private float mFontSize;
    private boolean mHasChangeListener;
    private OnChangedListener mListener;
    private final CaptioningManager mManager;
    private final ArrayMap<TextTrackRegion, RegionLayout> mRegionBoxes;

    /* compiled from: WebVttRenderer */
    private static class CueLayout extends LinearLayout {
        private boolean mActive;
        private CaptionStyle mCaptionStyle;
        public final TextTrackCue mCue;
        private float mFontSize;
        private int mOrder;

        public CueLayout(Context context, TextTrackCue cue, CaptionStyle captionStyle, float fontSize) {
            super(context);
            this.mCue = cue;
            this.mCaptionStyle = captionStyle;
            this.mFontSize = fontSize;
            int i = 0;
            int i2 = 1;
            boolean horizontal = cue.mWritingDirection == 100;
            if (horizontal) {
                i = 1;
            }
            setOrientation(i);
            switch (cue.mAlignment) {
                case 200:
                    if (!horizontal) {
                        i2 = 16;
                    }
                    setGravity(i2);
                    break;
                case 201:
                    setGravity(Gravity.START);
                    break;
                case 202:
                    setGravity(Gravity.END);
                    break;
                case 203:
                    setGravity(3);
                    break;
                case 204:
                    setGravity(5);
                    break;
            }
            update();
        }

        public void setCaptionStyle(CaptionStyle style, float fontSize) {
            this.mCaptionStyle = style;
            this.mFontSize = fontSize;
            int n = getChildCount();
            for (int i = 0; i < n; i++) {
                View child = getChildAt(i);
                if (child instanceof SpanLayout) {
                    ((SpanLayout) child).setCaptionStyle(style, fontSize);
                }
            }
        }

        public void prepForPrune() {
            this.mActive = false;
        }

        public void update() {
            Alignment alignment;
            this.mActive = true;
            removeAllViews();
            int cueAlignment = WebVttRenderingWidget.resolveCueAlignment(getLayoutDirection(), this.mCue.mAlignment);
            if (cueAlignment == 203) {
                alignment = Alignment.ALIGN_LEFT;
            } else if (cueAlignment != 204) {
                alignment = Alignment.ALIGN_CENTER;
            } else {
                alignment = Alignment.ALIGN_RIGHT;
            }
            CaptionStyle captionStyle = this.mCaptionStyle;
            float fontSize = this.mFontSize;
            for (TextTrackCueSpan[] spanLayout : this.mCue.mLines) {
                SpanLayout lineBox = new SpanLayout(getContext(), spanLayout);
                lineBox.setAlignment(alignment);
                lineBox.setCaptionStyle(captionStyle, fontSize);
                addView((View) lineBox, -2, -2);
            }
        }

        /* Access modifiers changed, original: protected */
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        public void measureForParent(int widthMeasureSpec, int heightMeasureSpec) {
            int maximumSize;
            TextTrackCue cue = this.mCue;
            int specWidth = MeasureSpec.getSize(widthMeasureSpec);
            int specHeight = MeasureSpec.getSize(heightMeasureSpec);
            int absAlignment = WebVttRenderingWidget.resolveCueAlignment(getLayoutDirection(), cue.mAlignment);
            if (absAlignment != 200) {
                if (absAlignment == 203) {
                    maximumSize = 100 - cue.mTextPosition;
                } else if (absAlignment != 204) {
                    maximumSize = 0;
                } else {
                    maximumSize = cue.mTextPosition;
                }
            } else if (cue.mTextPosition <= 50) {
                maximumSize = cue.mTextPosition * 2;
            } else {
                maximumSize = (100 - cue.mTextPosition) * 2;
            }
            measure(MeasureSpec.makeMeasureSpec((Math.min(cue.mSize, maximumSize) * specWidth) / 100, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(specHeight, Integer.MIN_VALUE));
        }

        public void setOrder(int order) {
            this.mOrder = order;
        }

        public boolean isActive() {
            return this.mActive;
        }

        public TextTrackCue getCue() {
            return this.mCue;
        }
    }

    /* compiled from: WebVttRenderer */
    private static class RegionLayout extends LinearLayout {
        private CaptionStyle mCaptionStyle;
        private float mFontSize;
        private final TextTrackRegion mRegion;
        private final ArrayList<CueLayout> mRegionCueBoxes = new ArrayList();

        public RegionLayout(Context context, TextTrackRegion region, CaptionStyle captionStyle, float fontSize) {
            super(context);
            this.mRegion = region;
            this.mCaptionStyle = captionStyle;
            this.mFontSize = fontSize;
            setOrientation(1);
            setBackgroundColor(captionStyle.windowColor);
        }

        public void setCaptionStyle(CaptionStyle captionStyle, float fontSize) {
            this.mCaptionStyle = captionStyle;
            this.mFontSize = fontSize;
            int cueCount = this.mRegionCueBoxes.size();
            for (int i = 0; i < cueCount; i++) {
                ((CueLayout) this.mRegionCueBoxes.get(i)).setCaptionStyle(captionStyle, fontSize);
            }
            setBackgroundColor(captionStyle.windowColor);
        }

        public void measureForParent(int widthMeasureSpec, int heightMeasureSpec) {
            TextTrackRegion region = this.mRegion;
            measure(MeasureSpec.makeMeasureSpec((((int) region.mWidth) * MeasureSpec.getSize(widthMeasureSpec)) / 100, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), Integer.MIN_VALUE));
        }

        public void prepForPrune() {
            int cueCount = this.mRegionCueBoxes.size();
            for (int i = 0; i < cueCount; i++) {
                ((CueLayout) this.mRegionCueBoxes.get(i)).prepForPrune();
            }
        }

        public void put(TextTrackCue cue) {
            int cueCount = this.mRegionCueBoxes.size();
            for (int i = 0; i < cueCount; i++) {
                CueLayout cueBox = (CueLayout) this.mRegionCueBoxes.get(i);
                if (cueBox.getCue() == cue) {
                    cueBox.update();
                    return;
                }
            }
            CueLayout cueBox2 = new CueLayout(getContext(), cue, this.mCaptionStyle, this.mFontSize);
            this.mRegionCueBoxes.add(cueBox2);
            addView((View) cueBox2, -2, -2);
            if (getChildCount() > this.mRegion.mLines) {
                removeViewAt(0);
            }
        }

        public boolean prune() {
            int cueCount = this.mRegionCueBoxes.size();
            int i = 0;
            while (i < cueCount) {
                CueLayout cueBox = (CueLayout) this.mRegionCueBoxes.get(i);
                if (!cueBox.isActive()) {
                    this.mRegionCueBoxes.remove(i);
                    removeView(cueBox);
                    cueCount--;
                    i--;
                }
                i++;
            }
            return this.mRegionCueBoxes.isEmpty();
        }

        public TextTrackRegion getRegion() {
            return this.mRegion;
        }
    }

    /* compiled from: WebVttRenderer */
    private static class SpanLayout extends SubtitleView {
        private final SpannableStringBuilder mBuilder = new SpannableStringBuilder();
        private final TextTrackCueSpan[] mSpans;

        public SpanLayout(Context context, TextTrackCueSpan[] spans) {
            super(context);
            this.mSpans = spans;
            update();
        }

        public void update() {
            SpannableStringBuilder builder = this.mBuilder;
            TextTrackCueSpan[] spans = this.mSpans;
            builder.clear();
            builder.clearSpans();
            int spanCount = spans.length;
            for (int i = 0; i < spanCount; i++) {
                if (spans[i].mEnabled) {
                    builder.append(spans[i].mText);
                }
            }
            setText((CharSequence) builder);
        }

        public void setCaptionStyle(CaptionStyle captionStyle, float fontSize) {
            setBackgroundColor(captionStyle.backgroundColor);
            setForegroundColor(captionStyle.foregroundColor);
            setEdgeColor(captionStyle.edgeColor);
            setEdgeType(captionStyle.edgeType);
            setTypeface(captionStyle.getTypeface());
            setTextSize(fontSize);
        }
    }

    public WebVttRenderingWidget(Context context) {
        this(context, null);
    }

    public WebVttRenderingWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebVttRenderingWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public WebVttRenderingWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mRegionBoxes = new ArrayMap();
        this.mCueBoxes = new ArrayMap();
        this.mCaptioningListener = new CaptioningChangeListener() {
            public void onFontScaleChanged(float fontScale) {
                float fontSize = (((float) WebVttRenderingWidget.this.getHeight()) * fontScale) * WebVttRenderingWidget.LINE_HEIGHT_RATIO;
                WebVttRenderingWidget webVttRenderingWidget = WebVttRenderingWidget.this;
                webVttRenderingWidget.setCaptionStyle(webVttRenderingWidget.mCaptionStyle, fontSize);
            }

            public void onUserStyleChanged(CaptionStyle userStyle) {
                WebVttRenderingWidget webVttRenderingWidget = WebVttRenderingWidget.this;
                webVttRenderingWidget.setCaptionStyle(userStyle, webVttRenderingWidget.mFontSize);
            }
        };
        setLayerType(1, null);
        this.mManager = (CaptioningManager) context.getSystemService(Context.CAPTIONING_SERVICE);
        this.mCaptionStyle = this.mManager.getUserStyle();
        this.mFontSize = (this.mManager.getFontScale() * ((float) getHeight())) * LINE_HEIGHT_RATIO;
    }

    public void setSize(int width, int height) {
        measure(MeasureSpec.makeMeasureSpec(width, 1073741824), MeasureSpec.makeMeasureSpec(height, 1073741824));
        layout(0, 0, width, height);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        manageChangeListener();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        manageChangeListener();
    }

    public void setOnChangedListener(OnChangedListener listener) {
        this.mListener = listener;
    }

    public void setVisible(boolean visible) {
        if (visible) {
            setVisibility(0);
        } else {
            setVisibility(8);
        }
        manageChangeListener();
    }

    private void manageChangeListener() {
        boolean needsListener = isAttachedToWindow() && getVisibility() == 0;
        if (this.mHasChangeListener != needsListener) {
            this.mHasChangeListener = needsListener;
            if (needsListener) {
                this.mManager.addCaptioningChangeListener(this.mCaptioningListener);
                setCaptionStyle(this.mManager.getUserStyle(), (this.mManager.getFontScale() * ((float) getHeight())) * LINE_HEIGHT_RATIO);
                return;
            }
            this.mManager.removeCaptioningChangeListener(this.mCaptioningListener);
        }
    }

    public void setActiveCues(Vector<Cue> activeCues) {
        Context context = getContext();
        CaptionStyle captionStyle = this.mCaptionStyle;
        float fontSize = this.mFontSize;
        prepForPrune();
        int count = activeCues.size();
        for (int i = 0; i < count; i++) {
            TextTrackCue cue = (TextTrackCue) activeCues.get(i);
            TextTrackRegion region = cue.mRegion;
            if (region != null) {
                RegionLayout regionBox = (RegionLayout) this.mRegionBoxes.get(region);
                if (regionBox == null) {
                    regionBox = new RegionLayout(context, region, captionStyle, fontSize);
                    this.mRegionBoxes.put(region, regionBox);
                    addView((View) regionBox, -2, -2);
                }
                regionBox.put(cue);
            } else {
                CueLayout cueBox = (CueLayout) this.mCueBoxes.get(cue);
                if (cueBox == null) {
                    cueBox = new CueLayout(context, cue, captionStyle, fontSize);
                    this.mCueBoxes.put(cue, cueBox);
                    addView((View) cueBox, -2, -2);
                }
                cueBox.update();
                cueBox.setOrder(i);
            }
        }
        prune();
        setSize(getWidth(), getHeight());
        OnChangedListener onChangedListener = this.mListener;
        if (onChangedListener != null) {
            onChangedListener.onChanged(this);
        }
    }

    private void setCaptionStyle(CaptionStyle captionStyle, float fontSize) {
        int i;
        captionStyle = DEFAULT_CAPTION_STYLE.applyStyle(captionStyle);
        this.mCaptionStyle = captionStyle;
        this.mFontSize = fontSize;
        int cueCount = this.mCueBoxes.size();
        for (i = 0; i < cueCount; i++) {
            ((CueLayout) this.mCueBoxes.valueAt(i)).setCaptionStyle(captionStyle, fontSize);
        }
        i = this.mRegionBoxes.size();
        for (int i2 = 0; i2 < i; i2++) {
            ((RegionLayout) this.mRegionBoxes.valueAt(i2)).setCaptionStyle(captionStyle, fontSize);
        }
    }

    private void prune() {
        int regionCount = this.mRegionBoxes.size();
        int i = 0;
        while (i < regionCount) {
            RegionLayout regionBox = (RegionLayout) this.mRegionBoxes.valueAt(i);
            if (regionBox.prune()) {
                removeView(regionBox);
                this.mRegionBoxes.removeAt(i);
                regionCount--;
                i--;
            }
            i++;
        }
        i = this.mCueBoxes.size();
        int i2 = 0;
        while (i2 < i) {
            CueLayout cueBox = (CueLayout) this.mCueBoxes.valueAt(i2);
            if (!cueBox.isActive()) {
                removeView(cueBox);
                this.mCueBoxes.removeAt(i2);
                i--;
                i2--;
            }
            i2++;
        }
    }

    private void prepForPrune() {
        int i;
        int regionCount = this.mRegionBoxes.size();
        for (i = 0; i < regionCount; i++) {
            ((RegionLayout) this.mRegionBoxes.valueAt(i)).prepForPrune();
        }
        i = this.mCueBoxes.size();
        for (int i2 = 0; i2 < i; i2++) {
            ((CueLayout) this.mCueBoxes.valueAt(i2)).prepForPrune();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int regionCount = this.mRegionBoxes.size();
        for (i = 0; i < regionCount; i++) {
            ((RegionLayout) this.mRegionBoxes.valueAt(i)).measureForParent(widthMeasureSpec, heightMeasureSpec);
        }
        i = this.mCueBoxes.size();
        for (int i2 = 0; i2 < i; i2++) {
            ((CueLayout) this.mCueBoxes.valueAt(i2)).measureForParent(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int i;
        int viewportWidth = r - l;
        int viewportHeight = b - t;
        setCaptionStyle(this.mCaptionStyle, (this.mManager.getFontScale() * LINE_HEIGHT_RATIO) * ((float) viewportHeight));
        int regionCount = this.mRegionBoxes.size();
        for (i = 0; i < regionCount; i++) {
            layoutRegion(viewportWidth, viewportHeight, (RegionLayout) this.mRegionBoxes.valueAt(i));
        }
        i = this.mCueBoxes.size();
        for (int i2 = 0; i2 < i; i2++) {
            layoutCue(viewportWidth, viewportHeight, (CueLayout) this.mCueBoxes.valueAt(i2));
        }
    }

    private void layoutRegion(int viewportWidth, int viewportHeight, RegionLayout regionBox) {
        TextTrackRegion region = regionBox.getRegion();
        int regionHeight = regionBox.getMeasuredHeight();
        int regionWidth = regionBox.getMeasuredWidth();
        int left = (int) ((((float) (viewportWidth - regionWidth)) * region.mViewportAnchorPointX) / 1120403456);
        int top = (int) ((((float) (viewportHeight - regionHeight)) * region.mViewportAnchorPointY) / 100.0f);
        regionBox.layout(left, top, left + regionWidth, top + regionHeight);
    }

    private void layoutCue(int viewportWidth, int viewportHeight, CueLayout cueBox) {
        int xPosition;
        int paddingLeft;
        int paddingRight;
        int top;
        CueLayout cueLayout = cueBox;
        TextTrackCue cue = cueBox.getCue();
        int direction = getLayoutDirection();
        int absAlignment = resolveCueAlignment(direction, cue.mAlignment);
        boolean cueSnapToLines = cue.mSnapToLines;
        int size = (cueBox.getMeasuredWidth() * 100) / viewportWidth;
        if (absAlignment == 203) {
            xPosition = cue.mTextPosition;
        } else if (absAlignment != 204) {
            xPosition = cue.mTextPosition - (size / 2);
        } else {
            xPosition = cue.mTextPosition - size;
        }
        if (direction == 1) {
            xPosition = 100 - xPosition;
        }
        if (cueSnapToLines) {
            paddingLeft = (getPaddingLeft() * 100) / viewportWidth;
            paddingRight = (getPaddingRight() * 100) / viewportWidth;
            if (xPosition < paddingLeft && xPosition + size > paddingLeft) {
                xPosition += paddingLeft;
                size -= paddingLeft;
            }
            float rightEdge = (float) (100 - paddingRight);
            if (((float) xPosition) < rightEdge && ((float) (xPosition + size)) > rightEdge) {
                size -= paddingRight;
            }
        }
        paddingLeft = (xPosition * viewportWidth) / 100;
        paddingRight = (size * viewportWidth) / 100;
        int yPosition = calculateLinePosition(cueLayout);
        int height = cueBox.getMeasuredHeight();
        if (yPosition < 0) {
            top = viewportHeight + (yPosition * height);
        } else {
            top = ((viewportHeight - height) * yPosition) / 100;
        }
        cueLayout.layout(paddingLeft, top, paddingLeft + paddingRight, top + height);
    }

    private int calculateLinePosition(CueLayout cueBox) {
        TextTrackCue cue = cueBox.getCue();
        Integer linePosition = cue.mLinePosition;
        boolean snapToLines = cue.mSnapToLines;
        boolean autoPosition = linePosition == null;
        if (!snapToLines && !autoPosition && (linePosition.intValue() < 0 || linePosition.intValue() > 100)) {
            return 100;
        }
        if (!autoPosition) {
            return linePosition.intValue();
        }
        if (snapToLines) {
            return -(cueBox.mOrder + 1);
        }
        return 100;
    }

    private static int resolveCueAlignment(int layoutDirection, int alignment) {
        int i = 203;
        if (alignment == 201) {
            if (layoutDirection != 0) {
                i = 204;
            }
            return i;
        } else if (alignment != 202) {
            return alignment;
        } else {
            if (layoutDirection == 0) {
                i = 204;
            }
            return i;
        }
    }
}
