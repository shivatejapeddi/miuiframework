package android.widget;

import android.animation.ObjectAnimator;
import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore.Audio.AudioColumns;
import android.text.Layout;
import android.text.StaticLayout.Builder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.AllCapsTransformationMethod;
import android.text.method.TransformationMethod2;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import com.android.internal.R;

public class Switch extends CompoundButton {
    private static final int[] CHECKED_STATE_SET = new int[]{16842912};
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int THUMB_ANIMATION_DURATION = 250;
    private static final FloatProperty<Switch> THUMB_POS = new FloatProperty<Switch>("thumbPos") {
        public Float get(Switch object) {
            return Float.valueOf(object.mThumbPosition);
        }

        public void setValue(Switch object, float value) {
            object.setThumbPosition(value);
        }
    };
    private static final int TOUCH_MODE_DOWN = 1;
    private static final int TOUCH_MODE_DRAGGING = 2;
    private static final int TOUCH_MODE_IDLE = 0;
    private boolean mHasThumbTint;
    private boolean mHasThumbTintMode;
    private boolean mHasTrackTint;
    private boolean mHasTrackTintMode;
    private int mMinFlingVelocity;
    @UnsupportedAppUsage
    private Layout mOffLayout;
    @UnsupportedAppUsage
    private Layout mOnLayout;
    private ObjectAnimator mPositionAnimator;
    private boolean mShowText;
    private boolean mSplitTrack;
    private int mSwitchBottom;
    @UnsupportedAppUsage
    private int mSwitchHeight;
    private int mSwitchLeft;
    @UnsupportedAppUsage
    private int mSwitchMinWidth;
    private int mSwitchPadding;
    private int mSwitchRight;
    private int mSwitchTop;
    private TransformationMethod2 mSwitchTransformationMethod;
    @UnsupportedAppUsage
    private int mSwitchWidth;
    private final Rect mTempRect;
    private ColorStateList mTextColors;
    private CharSequence mTextOff;
    private CharSequence mTextOn;
    private TextPaint mTextPaint;
    private BlendMode mThumbBlendMode;
    @UnsupportedAppUsage
    private Drawable mThumbDrawable;
    private float mThumbPosition;
    private int mThumbTextPadding;
    private ColorStateList mThumbTintList;
    @UnsupportedAppUsage
    private int mThumbWidth;
    private int mTouchMode;
    private int mTouchSlop;
    private float mTouchX;
    private float mTouchY;
    private BlendMode mTrackBlendMode;
    @UnsupportedAppUsage
    private Drawable mTrackDrawable;
    private ColorStateList mTrackTintList;
    private boolean mUseFallbackLineSpacing;
    private VelocityTracker mVelocityTracker;

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<Switch> {
        private boolean mPropertiesMapped = false;
        private int mShowTextId;
        private int mSplitTrackId;
        private int mSwitchMinWidthId;
        private int mSwitchPaddingId;
        private int mTextOffId;
        private int mTextOnId;
        private int mThumbId;
        private int mThumbTextPaddingId;
        private int mThumbTintBlendModeId;
        private int mThumbTintId;
        private int mThumbTintModeId;
        private int mTrackId;
        private int mTrackTintBlendModeId;
        private int mTrackTintId;
        private int mTrackTintModeId;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mShowTextId = propertyMapper.mapBoolean("showText", 16843949);
            this.mSplitTrackId = propertyMapper.mapBoolean("splitTrack", 16843852);
            this.mSwitchMinWidthId = propertyMapper.mapInt("switchMinWidth", 16843632);
            this.mSwitchPaddingId = propertyMapper.mapInt("switchPadding", 16843633);
            this.mTextOffId = propertyMapper.mapObject("textOff", 16843045);
            this.mTextOnId = propertyMapper.mapObject("textOn", 16843044);
            this.mThumbId = propertyMapper.mapObject("thumb", 16843074);
            this.mThumbTextPaddingId = propertyMapper.mapInt("thumbTextPadding", 16843634);
            this.mThumbTintId = propertyMapper.mapObject("thumbTint", 16843889);
            this.mThumbTintBlendModeId = propertyMapper.mapObject("thumbTintBlendMode", 10);
            this.mThumbTintModeId = propertyMapper.mapObject("thumbTintMode", 16843890);
            this.mTrackId = propertyMapper.mapObject(AudioColumns.TRACK, 16843631);
            this.mTrackTintId = propertyMapper.mapObject("trackTint", 16843993);
            this.mTrackTintBlendModeId = propertyMapper.mapObject("trackTintBlendMode", 13);
            this.mTrackTintModeId = propertyMapper.mapObject("trackTintMode", 16843994);
            this.mPropertiesMapped = true;
        }

        public void readProperties(Switch node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readBoolean(this.mShowTextId, node.getShowText());
                propertyReader.readBoolean(this.mSplitTrackId, node.getSplitTrack());
                propertyReader.readInt(this.mSwitchMinWidthId, node.getSwitchMinWidth());
                propertyReader.readInt(this.mSwitchPaddingId, node.getSwitchPadding());
                propertyReader.readObject(this.mTextOffId, node.getTextOff());
                propertyReader.readObject(this.mTextOnId, node.getTextOn());
                propertyReader.readObject(this.mThumbId, node.getThumbDrawable());
                propertyReader.readInt(this.mThumbTextPaddingId, node.getThumbTextPadding());
                propertyReader.readObject(this.mThumbTintId, node.getThumbTintList());
                propertyReader.readObject(this.mThumbTintBlendModeId, node.getThumbTintBlendMode());
                propertyReader.readObject(this.mThumbTintModeId, node.getThumbTintMode());
                propertyReader.readObject(this.mTrackId, node.getTrackDrawable());
                propertyReader.readObject(this.mTrackTintId, node.getTrackTintList());
                propertyReader.readObject(this.mTrackTintBlendModeId, node.getTrackTintBlendMode());
                propertyReader.readObject(this.mTrackTintModeId, node.getTrackTintMode());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    public Switch(Context context) {
        this(context, null);
    }

    public Switch(Context context, AttributeSet attrs) {
        this(context, attrs, 16843839);
    }

    public Switch(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Switch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Context context2 = context;
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mThumbTintList = null;
        this.mThumbBlendMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbTintMode = false;
        this.mTrackTintList = null;
        this.mTrackBlendMode = null;
        this.mHasTrackTint = false;
        this.mHasTrackTintMode = false;
        this.mVelocityTracker = VelocityTracker.obtain();
        this.mTempRect = new Rect();
        this.mTextPaint = new TextPaint(1);
        Resources res = getResources();
        this.mTextPaint.density = res.getDisplayMetrics().density;
        this.mTextPaint.setCompatibilityScaling(res.getCompatibilityInfo().applicationScale);
        TypedArray a = context2.obtainStyledAttributes(attrs, R.styleable.Switch, defStyleAttr, defStyleRes);
        TypedArray a2 = a;
        saveAttributeDataForStyleable(context, R.styleable.Switch, attrs, a, defStyleAttr, defStyleRes);
        this.mThumbDrawable = a2.getDrawable(2);
        Drawable drawable = this.mThumbDrawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        this.mTrackDrawable = a2.getDrawable(4);
        drawable = this.mTrackDrawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        this.mTextOn = a2.getText(0);
        this.mTextOff = a2.getText(1);
        this.mShowText = a2.getBoolean(11, true);
        this.mThumbTextPadding = a2.getDimensionPixelSize(7, 0);
        this.mSwitchMinWidth = a2.getDimensionPixelSize(5, 0);
        this.mSwitchPadding = a2.getDimensionPixelSize(6, 0);
        this.mSplitTrack = a2.getBoolean(8, false);
        this.mUseFallbackLineSpacing = context.getApplicationInfo().targetSdkVersion >= 28;
        ColorStateList thumbTintList = a2.getColorStateList(9);
        if (thumbTintList != null) {
            this.mThumbTintList = thumbTintList;
            this.mHasThumbTint = true;
        }
        BlendMode thumbTintMode = Drawable.parseBlendMode(a2.getInt(10, -1), null);
        if (this.mThumbBlendMode != thumbTintMode) {
            this.mThumbBlendMode = thumbTintMode;
            this.mHasThumbTintMode = true;
        }
        if (this.mHasThumbTint || this.mHasThumbTintMode) {
            applyThumbTint();
        }
        ColorStateList trackTintList = a2.getColorStateList(12);
        if (trackTintList != null) {
            this.mTrackTintList = trackTintList;
            this.mHasTrackTint = true;
        }
        BlendMode trackTintMode = Drawable.parseBlendMode(a2.getInt(13, -1), null);
        if (this.mTrackBlendMode != trackTintMode) {
            this.mTrackBlendMode = trackTintMode;
            this.mHasTrackTintMode = true;
        }
        if (this.mHasTrackTint || this.mHasTrackTintMode) {
            applyTrackTint();
        }
        int appearance = a2.getResourceId(3, 0);
        if (appearance != 0) {
            setSwitchTextAppearance(context2, appearance);
        }
        a2.recycle();
        ViewConfiguration config = ViewConfiguration.get(context);
        this.mTouchSlop = config.getScaledTouchSlop();
        this.mMinFlingVelocity = config.getScaledMinimumFlingVelocity();
        refreshDrawableState();
        setChecked(isChecked());
    }

    public void setSwitchTextAppearance(Context context, int resid) {
        TypedArray appearance = context.obtainStyledAttributes(resid, R.styleable.TextAppearance);
        ColorStateList colors = appearance.getColorStateList(3);
        if (colors != null) {
            this.mTextColors = colors;
        } else {
            this.mTextColors = getTextColors();
        }
        int ts = appearance.getDimensionPixelSize(0, 0);
        if (!(ts == 0 || ((float) ts) == this.mTextPaint.getTextSize())) {
            this.mTextPaint.setTextSize((float) ts);
            requestLayout();
        }
        setSwitchTypefaceByIndex(appearance.getInt(1, -1), appearance.getInt(2, -1));
        if (appearance.getBoolean(11, false)) {
            this.mSwitchTransformationMethod = new AllCapsTransformationMethod(getContext());
            this.mSwitchTransformationMethod.setLengthChangesAllowed(true);
        } else {
            this.mSwitchTransformationMethod = null;
        }
        appearance.recycle();
    }

    private void setSwitchTypefaceByIndex(int typefaceIndex, int styleIndex) {
        Typeface tf = null;
        if (typefaceIndex == 1) {
            tf = Typeface.SANS_SERIF;
        } else if (typefaceIndex == 2) {
            tf = Typeface.SERIF;
        } else if (typefaceIndex == 3) {
            tf = Typeface.MONOSPACE;
        }
        setSwitchTypeface(tf, styleIndex);
    }

    public void setSwitchTypeface(Typeface tf, int style) {
        float f = 0.0f;
        boolean z = false;
        if (style > 0) {
            if (tf == null) {
                tf = Typeface.defaultFromStyle(style);
            } else {
                tf = Typeface.create(tf, style);
            }
            setSwitchTypeface(tf);
            int need = (~(tf != null ? tf.getStyle() : 0)) & style;
            TextPaint textPaint = this.mTextPaint;
            if ((need & 1) != 0) {
                z = true;
            }
            textPaint.setFakeBoldText(z);
            TextPaint textPaint2 = this.mTextPaint;
            if ((need & 2) != 0) {
                f = -0.25f;
            }
            textPaint2.setTextSkewX(f);
            return;
        }
        this.mTextPaint.setFakeBoldText(false);
        this.mTextPaint.setTextSkewX(0.0f);
        setSwitchTypeface(tf);
    }

    public void setSwitchTypeface(Typeface tf) {
        if (this.mTextPaint.getTypeface() != tf) {
            this.mTextPaint.setTypeface(tf);
            requestLayout();
            invalidate();
        }
    }

    public void setSwitchPadding(int pixels) {
        this.mSwitchPadding = pixels;
        requestLayout();
    }

    public int getSwitchPadding() {
        return this.mSwitchPadding;
    }

    public void setSwitchMinWidth(int pixels) {
        this.mSwitchMinWidth = pixels;
        requestLayout();
    }

    public int getSwitchMinWidth() {
        return this.mSwitchMinWidth;
    }

    public void setThumbTextPadding(int pixels) {
        this.mThumbTextPadding = pixels;
        requestLayout();
    }

    public int getThumbTextPadding() {
        return this.mThumbTextPadding;
    }

    public void setTrackDrawable(Drawable track) {
        Drawable drawable = this.mTrackDrawable;
        if (drawable != null) {
            drawable.setCallback(null);
        }
        this.mTrackDrawable = track;
        if (track != null) {
            track.setCallback(this);
        }
        requestLayout();
    }

    public void setTrackResource(int resId) {
        setTrackDrawable(getContext().getDrawable(resId));
    }

    public Drawable getTrackDrawable() {
        return this.mTrackDrawable;
    }

    public void setTrackTintList(ColorStateList tint) {
        this.mTrackTintList = tint;
        this.mHasTrackTint = true;
        applyTrackTint();
    }

    public ColorStateList getTrackTintList() {
        return this.mTrackTintList;
    }

    public void setTrackTintMode(Mode tintMode) {
        setTrackTintBlendMode(tintMode != null ? BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    public void setTrackTintBlendMode(BlendMode blendMode) {
        this.mTrackBlendMode = blendMode;
        this.mHasTrackTintMode = true;
        applyTrackTint();
    }

    public Mode getTrackTintMode() {
        BlendMode mode = getTrackTintBlendMode();
        return mode != null ? BlendMode.blendModeToPorterDuffMode(mode) : null;
    }

    public BlendMode getTrackTintBlendMode() {
        return this.mTrackBlendMode;
    }

    private void applyTrackTint() {
        if (this.mTrackDrawable == null) {
            return;
        }
        if (this.mHasTrackTint || this.mHasTrackTintMode) {
            this.mTrackDrawable = this.mTrackDrawable.mutate();
            if (this.mHasTrackTint) {
                this.mTrackDrawable.setTintList(this.mTrackTintList);
            }
            if (this.mHasTrackTintMode) {
                this.mTrackDrawable.setTintBlendMode(this.mTrackBlendMode);
            }
            if (this.mTrackDrawable.isStateful()) {
                this.mTrackDrawable.setState(getDrawableState());
            }
        }
    }

    public void setThumbDrawable(Drawable thumb) {
        Drawable drawable = this.mThumbDrawable;
        if (drawable != null) {
            drawable.setCallback(null);
        }
        this.mThumbDrawable = thumb;
        if (thumb != null) {
            thumb.setCallback(this);
        }
        requestLayout();
    }

    public void setThumbResource(int resId) {
        setThumbDrawable(getContext().getDrawable(resId));
    }

    public Drawable getThumbDrawable() {
        return this.mThumbDrawable;
    }

    public void setThumbTintList(ColorStateList tint) {
        this.mThumbTintList = tint;
        this.mHasThumbTint = true;
        applyThumbTint();
    }

    public ColorStateList getThumbTintList() {
        return this.mThumbTintList;
    }

    public void setThumbTintMode(Mode tintMode) {
        setThumbTintBlendMode(tintMode != null ? BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    public void setThumbTintBlendMode(BlendMode blendMode) {
        this.mThumbBlendMode = blendMode;
        this.mHasThumbTintMode = true;
        applyThumbTint();
    }

    public Mode getThumbTintMode() {
        BlendMode mode = getThumbTintBlendMode();
        return mode != null ? BlendMode.blendModeToPorterDuffMode(mode) : null;
    }

    public BlendMode getThumbTintBlendMode() {
        return this.mThumbBlendMode;
    }

    private void applyThumbTint() {
        if (this.mThumbDrawable == null) {
            return;
        }
        if (this.mHasThumbTint || this.mHasThumbTintMode) {
            this.mThumbDrawable = this.mThumbDrawable.mutate();
            if (this.mHasThumbTint) {
                this.mThumbDrawable.setTintList(this.mThumbTintList);
            }
            if (this.mHasThumbTintMode) {
                this.mThumbDrawable.setTintBlendMode(this.mThumbBlendMode);
            }
            if (this.mThumbDrawable.isStateful()) {
                this.mThumbDrawable.setState(getDrawableState());
            }
        }
    }

    public void setSplitTrack(boolean splitTrack) {
        this.mSplitTrack = splitTrack;
        invalidate();
    }

    public boolean getSplitTrack() {
        return this.mSplitTrack;
    }

    public CharSequence getTextOn() {
        return this.mTextOn;
    }

    public void setTextOn(CharSequence textOn) {
        this.mTextOn = textOn;
        requestLayout();
    }

    public CharSequence getTextOff() {
        return this.mTextOff;
    }

    public void setTextOff(CharSequence textOff) {
        this.mTextOff = textOff;
        requestLayout();
    }

    public void setShowText(boolean showText) {
        if (this.mShowText != showText) {
            this.mShowText = showText;
            requestLayout();
        }
    }

    public boolean getShowText() {
        return this.mShowText;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int thumbWidth;
        int thumbHeight;
        int maxTextWidth;
        int trackHeight;
        if (this.mShowText) {
            if (this.mOnLayout == null) {
                this.mOnLayout = makeLayout(this.mTextOn);
            }
            if (this.mOffLayout == null) {
                this.mOffLayout = makeLayout(this.mTextOff);
            }
        }
        Rect padding = this.mTempRect;
        Drawable drawable = this.mThumbDrawable;
        if (drawable != null) {
            drawable.getPadding(padding);
            thumbWidth = (this.mThumbDrawable.getIntrinsicWidth() - padding.left) - padding.right;
            thumbHeight = this.mThumbDrawable.getIntrinsicHeight();
        } else {
            thumbWidth = 0;
            thumbHeight = 0;
        }
        if (this.mShowText) {
            maxTextWidth = Math.max(this.mOnLayout.getWidth(), this.mOffLayout.getWidth()) + (this.mThumbTextPadding * 2);
        } else {
            maxTextWidth = 0;
        }
        this.mThumbWidth = Math.max(maxTextWidth, thumbWidth);
        Drawable drawable2 = this.mTrackDrawable;
        if (drawable2 != null) {
            drawable2.getPadding(padding);
            trackHeight = this.mTrackDrawable.getIntrinsicHeight();
        } else {
            padding.setEmpty();
            trackHeight = 0;
        }
        int paddingLeft = padding.left;
        int paddingRight = padding.right;
        Insets inset = this.mThumbDrawable;
        if (inset != null) {
            inset = inset.getOpticalInsets();
            paddingLeft = Math.max(paddingLeft, inset.left);
            paddingRight = Math.max(paddingRight, inset.right);
        }
        int switchWidth = Math.max(this.mSwitchMinWidth, ((this.mThumbWidth * 2) + paddingLeft) + paddingRight);
        int switchHeight = Math.max(trackHeight, thumbHeight);
        this.mSwitchWidth = switchWidth;
        this.mSwitchHeight = switchHeight;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight() < switchHeight) {
            setMeasuredDimension(getMeasuredWidthAndState(), switchHeight);
        }
    }

    public void onPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        super.onPopulateAccessibilityEventInternal(event);
        CharSequence text = isChecked() ? this.mTextOn : this.mTextOff;
        if (text != null) {
            event.getText().add(text);
        }
    }

    private Layout makeLayout(CharSequence text) {
        CharSequence transformed;
        TransformationMethod2 transformationMethod2 = this.mSwitchTransformationMethod;
        if (transformationMethod2 != null) {
            transformed = transformationMethod2.getTransformation(text, this);
        } else {
            transformed = text;
        }
        return Builder.obtain(transformed, 0, transformed.length(), this.mTextPaint, (int) Math.ceil((double) Layout.getDesiredWidth(transformed, 0, transformed.length(), this.mTextPaint, getTextDirectionHeuristic()))).setUseLineSpacingFromFallbacks(this.mUseFallbackLineSpacing).build();
    }

    private boolean hitThumb(float x, float y) {
        boolean z = false;
        if (this.mThumbDrawable == null) {
            return false;
        }
        int thumbOffset = getThumbOffset();
        this.mThumbDrawable.getPadding(this.mTempRect);
        int thumbTop = this.mSwitchTop;
        int i = this.mTouchSlop;
        thumbTop -= i;
        int thumbLeft = (this.mSwitchLeft + thumbOffset) - i;
        i = ((this.mThumbWidth + thumbLeft) + this.mTempRect.left) + this.mTempRect.right;
        int i2 = this.mTouchSlop;
        i += i2;
        int thumbBottom = this.mSwitchBottom + i2;
        if (x > ((float) thumbLeft) && x < ((float) i) && y > ((float) thumbTop) && y < ((float) thumbBottom)) {
            z = true;
        }
        return z;
    }

    /* JADX WARNING: Missing block: B:6:0x0012, code skipped:
            if (r0 != 3) goto L_0x00ba;
     */
    public boolean onTouchEvent(android.view.MotionEvent r10) {
        /*
        r9 = this;
        r0 = r9.mVelocityTracker;
        r0.addMovement(r10);
        r0 = r10.getActionMasked();
        r1 = 1;
        if (r0 == 0) goto L_0x00a0;
    L_0x000c:
        r2 = 2;
        if (r0 == r1) goto L_0x008c;
    L_0x000f:
        if (r0 == r2) goto L_0x0016;
    L_0x0011:
        r3 = 3;
        if (r0 == r3) goto L_0x008c;
    L_0x0014:
        goto L_0x00ba;
    L_0x0016:
        r3 = r9.mTouchMode;
        if (r3 == 0) goto L_0x008a;
    L_0x001a:
        if (r3 == r1) goto L_0x0056;
    L_0x001c:
        if (r3 == r2) goto L_0x001f;
    L_0x001e:
        goto L_0x008b;
    L_0x001f:
        r2 = r10.getX();
        r3 = r9.getThumbScrollRange();
        r4 = r9.mTouchX;
        r4 = r2 - r4;
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = 0;
        if (r3 == 0) goto L_0x0034;
    L_0x0030:
        r7 = (float) r3;
        r7 = r4 / r7;
        goto L_0x003c;
    L_0x0034:
        r7 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r7 <= 0) goto L_0x003a;
    L_0x0038:
        r7 = r5;
        goto L_0x003c;
    L_0x003a:
        r7 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
    L_0x003c:
        r8 = r9.isLayoutRtl();
        if (r8 == 0) goto L_0x0043;
    L_0x0042:
        r7 = -r7;
    L_0x0043:
        r8 = r9.mThumbPosition;
        r8 = r8 + r7;
        r5 = android.util.MathUtils.constrain(r8, r6, r5);
        r6 = r9.mThumbPosition;
        r6 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r6 == 0) goto L_0x0055;
    L_0x0050:
        r9.mTouchX = r2;
        r9.setThumbPosition(r5);
    L_0x0055:
        return r1;
    L_0x0056:
        r3 = r10.getX();
        r4 = r10.getY();
        r5 = r9.mTouchX;
        r5 = r3 - r5;
        r5 = java.lang.Math.abs(r5);
        r6 = r9.mTouchSlop;
        r6 = (float) r6;
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 > 0) goto L_0x007c;
    L_0x006d:
        r5 = r9.mTouchY;
        r5 = r4 - r5;
        r5 = java.lang.Math.abs(r5);
        r6 = r9.mTouchSlop;
        r6 = (float) r6;
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 <= 0) goto L_0x008b;
    L_0x007c:
        r9.mTouchMode = r2;
        r2 = r9.getParent();
        r2.requestDisallowInterceptTouchEvent(r1);
        r9.mTouchX = r3;
        r9.mTouchY = r4;
        return r1;
    L_0x008b:
        goto L_0x00ba;
    L_0x008c:
        r3 = r9.mTouchMode;
        if (r3 != r2) goto L_0x0097;
    L_0x0090:
        r9.stopDrag(r10);
        super.onTouchEvent(r10);
        return r1;
    L_0x0097:
        r1 = 0;
        r9.mTouchMode = r1;
        r1 = r9.mVelocityTracker;
        r1.clear();
        goto L_0x00ba;
    L_0x00a0:
        r2 = r10.getX();
        r3 = r10.getY();
        r4 = r9.isEnabled();
        if (r4 == 0) goto L_0x00ba;
    L_0x00ae:
        r4 = r9.hitThumb(r2, r3);
        if (r4 == 0) goto L_0x00ba;
    L_0x00b4:
        r9.mTouchMode = r1;
        r9.mTouchX = r2;
        r9.mTouchY = r3;
    L_0x00ba:
        r1 = super.onTouchEvent(r10);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.Switch.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private void cancelSuperTouch(MotionEvent ev) {
        MotionEvent cancel = MotionEvent.obtain(ev);
        cancel.setAction(3);
        super.onTouchEvent(cancel);
        cancel.recycle();
    }

    private void stopDrag(MotionEvent ev) {
        this.mTouchMode = 0;
        boolean newState = true;
        boolean commitChange = ev.getAction() == 1 && isEnabled();
        boolean oldState = isChecked();
        if (commitChange) {
            this.mVelocityTracker.computeCurrentVelocity(1000);
            float xvel = this.mVelocityTracker.getXVelocity();
            if (Math.abs(xvel) <= ((float) this.mMinFlingVelocity)) {
                newState = getTargetCheckedState();
            } else if (isLayoutRtl() ? xvel >= 0.0f : xvel <= 0.0f) {
                newState = false;
            }
        } else {
            newState = oldState;
        }
        if (newState != oldState) {
            playSoundEffect(0);
        }
        setChecked(newState);
        cancelSuperTouch(ev);
    }

    private void animateThumbToCheckedState(boolean newCheckedState) {
        float targetPosition = newCheckedState ? 1.0f : 0.0f;
        this.mPositionAnimator = ObjectAnimator.ofFloat((Object) this, THUMB_POS, targetPosition);
        this.mPositionAnimator.setDuration(250);
        this.mPositionAnimator.setAutoCancel(true);
        this.mPositionAnimator.start();
    }

    @UnsupportedAppUsage
    private void cancelPositionAnimator() {
        ObjectAnimator objectAnimator = this.mPositionAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    private boolean getTargetCheckedState() {
        return this.mThumbPosition > 0.5f;
    }

    @UnsupportedAppUsage
    private void setThumbPosition(float position) {
        this.mThumbPosition = position;
        invalidate();
    }

    public void toggle() {
        setChecked(isChecked() ^ 1);
    }

    public void setChecked(boolean checked) {
        super.setChecked(checked);
        checked = isChecked();
        if (isAttachedToWindow() && isLaidOut()) {
            animateThumbToCheckedState(checked);
            return;
        }
        cancelPositionAnimator();
        setThumbPosition(checked ? 1.0f : 0.0f);
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int switchLeft;
        int switchRight;
        int switchBottom;
        super.onLayout(changed, left, top, right, bottom);
        int opticalInsetLeft = 0;
        int opticalInsetRight = 0;
        if (this.mThumbDrawable != null) {
            Rect trackPadding = this.mTempRect;
            Drawable drawable = this.mTrackDrawable;
            if (drawable != null) {
                drawable.getPadding(trackPadding);
            } else {
                trackPadding.setEmpty();
            }
            Insets insets = this.mThumbDrawable.getOpticalInsets();
            opticalInsetLeft = Math.max(0, insets.left - trackPadding.left);
            opticalInsetRight = Math.max(0, insets.right - trackPadding.right);
        }
        if (isLayoutRtl()) {
            switchLeft = getPaddingLeft() + opticalInsetLeft;
            switchRight = ((this.mSwitchWidth + switchLeft) - opticalInsetLeft) - opticalInsetRight;
        } else {
            switchRight = (getWidth() - getPaddingRight()) - opticalInsetRight;
            switchLeft = ((switchRight - this.mSwitchWidth) + opticalInsetLeft) + opticalInsetRight;
        }
        int gravity = getGravity() & 112;
        if (gravity == 16) {
            gravity = ((getPaddingTop() + getHeight()) - getPaddingBottom()) / 2;
            switchBottom = this.mSwitchHeight;
            gravity -= switchBottom / 2;
            switchBottom += gravity;
        } else if (gravity != 80) {
            gravity = getPaddingTop();
            switchBottom = this.mSwitchHeight + gravity;
        } else {
            switchBottom = getHeight() - getPaddingBottom();
            gravity = switchBottom - this.mSwitchHeight;
        }
        this.mSwitchLeft = switchLeft;
        this.mSwitchTop = gravity;
        this.mSwitchBottom = switchBottom;
        this.mSwitchRight = switchRight;
    }

    public void draw(Canvas c) {
        int trackLeft;
        int trackTop;
        Rect padding = this.mTempRect;
        int switchLeft = this.mSwitchLeft;
        int switchTop = this.mSwitchTop;
        int switchRight = this.mSwitchRight;
        int switchBottom = this.mSwitchBottom;
        int thumbInitialLeft = getThumbOffset() + switchLeft;
        Insets thumbInsets = this.mThumbDrawable;
        if (thumbInsets != null) {
            thumbInsets = thumbInsets.getOpticalInsets();
        } else {
            thumbInsets = Insets.NONE;
        }
        Drawable drawable = this.mTrackDrawable;
        if (drawable != null) {
            drawable.getPadding(padding);
            thumbInitialLeft += padding.left;
            trackLeft = switchLeft;
            trackTop = switchTop;
            int trackRight = switchRight;
            int trackBottom = switchBottom;
            if (thumbInsets != Insets.NONE) {
                if (thumbInsets.left > padding.left) {
                    trackLeft += thumbInsets.left - padding.left;
                }
                if (thumbInsets.top > padding.top) {
                    trackTop += thumbInsets.top - padding.top;
                }
                if (thumbInsets.right > padding.right) {
                    trackRight -= thumbInsets.right - padding.right;
                }
                if (thumbInsets.bottom > padding.bottom) {
                    trackBottom -= thumbInsets.bottom - padding.bottom;
                }
            }
            this.mTrackDrawable.setBounds(trackLeft, trackTop, trackRight, trackBottom);
        }
        drawable = this.mThumbDrawable;
        if (drawable != null) {
            drawable.getPadding(padding);
            trackLeft = thumbInitialLeft - padding.left;
            trackTop = (this.mThumbWidth + thumbInitialLeft) + padding.right;
            this.mThumbDrawable.setBounds(trackLeft, switchTop, trackTop, switchBottom);
            Drawable background = getBackground();
            if (background != null) {
                background.setHotspotBounds(trackLeft, switchTop, trackTop, switchBottom);
            }
        }
        super.draw(c);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        super.onDraw(canvas);
        Rect padding = this.mTempRect;
        Drawable trackDrawable = this.mTrackDrawable;
        if (trackDrawable != null) {
            trackDrawable.getPadding(padding);
        } else {
            padding.setEmpty();
        }
        int switchInnerTop = padding.top + this.mSwitchTop;
        int switchInnerBottom = this.mSwitchBottom - padding.bottom;
        Drawable thumbDrawable = this.mThumbDrawable;
        if (trackDrawable != null) {
            if (!this.mSplitTrack || thumbDrawable == null) {
                trackDrawable.draw(canvas2);
            } else {
                Insets insets = thumbDrawable.getOpticalInsets();
                thumbDrawable.copyBounds(padding);
                padding.left += insets.left;
                padding.right -= insets.right;
                int saveCount = canvas.save();
                canvas2.clipRect(padding, Op.DIFFERENCE);
                trackDrawable.draw(canvas2);
                canvas2.restoreToCount(saveCount);
            }
        }
        int saveCount2 = canvas.save();
        if (thumbDrawable != null) {
            thumbDrawable.draw(canvas2);
        }
        Layout switchText = getTargetCheckedState() ? this.mOnLayout : this.mOffLayout;
        if (switchText != null) {
            int cX;
            int[] drawableState = getDrawableState();
            ColorStateList colorStateList = this.mTextColors;
            if (colorStateList != null) {
                this.mTextPaint.setColor(colorStateList.getColorForState(drawableState, 0));
            }
            this.mTextPaint.drawableState = drawableState;
            if (thumbDrawable != null) {
                Rect bounds = thumbDrawable.getBounds();
                cX = bounds.left + bounds.right;
            } else {
                cX = getWidth();
            }
            canvas2.translate((float) ((cX / 2) - (switchText.getWidth() / 2)), (float) (((switchInnerTop + switchInnerBottom) / 2) - (switchText.getHeight() / 2)));
            switchText.draw(canvas2);
        }
        canvas2.restoreToCount(saveCount2);
    }

    public int getCompoundPaddingLeft() {
        if (!isLayoutRtl()) {
            return super.getCompoundPaddingLeft();
        }
        int padding = super.getCompoundPaddingLeft() + this.mSwitchWidth;
        if (!TextUtils.isEmpty(getText())) {
            padding += this.mSwitchPadding;
        }
        return padding;
    }

    public int getCompoundPaddingRight() {
        if (isLayoutRtl()) {
            return super.getCompoundPaddingRight();
        }
        int padding = super.getCompoundPaddingRight() + this.mSwitchWidth;
        if (!TextUtils.isEmpty(getText())) {
            padding += this.mSwitchPadding;
        }
        return padding;
    }

    private int getThumbOffset() {
        float thumbPosition;
        if (isLayoutRtl()) {
            thumbPosition = 1.0f - this.mThumbPosition;
        } else {
            thumbPosition = this.mThumbPosition;
        }
        return (int) ((((float) getThumbScrollRange()) * thumbPosition) + 0.5f);
    }

    private int getThumbScrollRange() {
        Drawable drawable = this.mTrackDrawable;
        if (drawable == null) {
            return 0;
        }
        Rect padding = this.mTempRect;
        drawable.getPadding(padding);
        Insets insets = this.mThumbDrawable;
        if (insets != null) {
            insets = insets.getOpticalInsets();
        } else {
            insets = Insets.NONE;
        }
        return ((((this.mSwitchWidth - this.mThumbWidth) - padding.left) - padding.right) - insets.left) - insets.right;
    }

    /* Access modifiers changed, original: protected */
    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    /* Access modifiers changed, original: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int[] state = getDrawableState();
        boolean changed = false;
        Drawable thumbDrawable = this.mThumbDrawable;
        if (thumbDrawable != null && thumbDrawable.isStateful()) {
            changed = false | thumbDrawable.setState(state);
        }
        Drawable trackDrawable = this.mTrackDrawable;
        if (trackDrawable != null && trackDrawable.isStateful()) {
            changed |= trackDrawable.setState(state);
        }
        if (changed) {
            invalidate();
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        Drawable drawable = this.mThumbDrawable;
        if (drawable != null) {
            drawable.setHotspot(x, y);
        }
        drawable = this.mTrackDrawable;
        if (drawable != null) {
            drawable.setHotspot(x, y);
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mThumbDrawable || who == this.mTrackDrawable;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mThumbDrawable;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        drawable = this.mTrackDrawable;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        ObjectAnimator objectAnimator = this.mPositionAnimator;
        if (objectAnimator != null && objectAnimator.isStarted()) {
            this.mPositionAnimator.end();
            this.mPositionAnimator = null;
        }
    }

    public CharSequence getAccessibilityClassName() {
        return Switch.class.getName();
    }

    /* Access modifiers changed, original: protected */
    public void onProvideStructure(ViewStructure structure, int viewFor, int flags) {
        CharSequence switchText = isChecked() ? this.mTextOn : this.mTextOff;
        if (!TextUtils.isEmpty(switchText)) {
            CharSequence oldText = structure.getText();
            if (TextUtils.isEmpty(oldText)) {
                structure.setText(switchText);
                return;
            }
            StringBuilder newText = new StringBuilder();
            newText.append(oldText);
            newText.append(' ');
            newText.append(switchText);
            structure.setText(newText);
        }
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        CharSequence switchText = isChecked() ? this.mTextOn : this.mTextOff;
        if (!TextUtils.isEmpty(switchText)) {
            CharSequence oldText = info.getText();
            if (TextUtils.isEmpty(oldText)) {
                info.setText(switchText);
                return;
            }
            StringBuilder newText = new StringBuilder();
            newText.append(oldText);
            newText.append(' ');
            newText.append(switchText);
            info.setText(newText);
        }
    }
}
