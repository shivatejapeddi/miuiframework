package android.graphics.drawable;

import android.annotation.UnsupportedAppUsage;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Insets;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable.ConstantState;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import com.android.internal.R;
import com.android.internal.app.DumpHeapActivity;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class GradientDrawable extends Drawable {
    private static final float DEFAULT_INNER_RADIUS_RATIO = 3.0f;
    private static final float DEFAULT_THICKNESS_RATIO = 9.0f;
    public static final int LINE = 2;
    public static final int LINEAR_GRADIENT = 0;
    public static final int OVAL = 1;
    public static final int RADIAL_GRADIENT = 1;
    private static final int RADIUS_TYPE_FRACTION = 1;
    private static final int RADIUS_TYPE_FRACTION_PARENT = 2;
    private static final int RADIUS_TYPE_PIXELS = 0;
    public static final int RECTANGLE = 0;
    public static final int RING = 3;
    public static final int SWEEP_GRADIENT = 2;
    private int mAlpha;
    private BlendModeColorFilter mBlendModeColorFilter;
    private ColorFilter mColorFilter;
    @UnsupportedAppUsage
    private final Paint mFillPaint;
    private boolean mGradientIsDirty;
    private float mGradientRadius;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050917)
    private GradientState mGradientState;
    private Paint mLayerPaint;
    private boolean mMutated;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124051827)
    private Rect mPadding;
    private final Path mPath;
    private boolean mPathIsDirty;
    private final RectF mRect;
    private Path mRingPath;
    @UnsupportedAppUsage
    private Paint mStrokePaint;

    static final class GradientState extends ConstantState {
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050917)
        public int mAngle = 0;
        int[] mAttrCorners;
        int[] mAttrGradient;
        int[] mAttrPadding;
        int[] mAttrSize;
        int[] mAttrSolid;
        int[] mAttrStroke;
        BlendMode mBlendMode = Drawable.DEFAULT_BLEND_MODE;
        float mCenterX = 0.5f;
        float mCenterY = 0.5f;
        public int mChangingConfigurations;
        int mDensity = 160;
        public boolean mDither = false;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050917)
        public int mGradient = 0;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050917)
        public int[] mGradientColors;
        float mGradientRadius = 0.5f;
        int mGradientRadiusType = 0;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050917)
        public int mHeight = -1;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050917)
        public int mInnerRadius = -1;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050917)
        public float mInnerRadiusRatio = 3.0f;
        boolean mOpaqueOverBounds;
        boolean mOpaqueOverShape;
        public Insets mOpticalInsets = Insets.NONE;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050917)
        public Orientation mOrientation;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050917)
        public Rect mPadding = null;
        @UnsupportedAppUsage
        public float[] mPositions;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050917)
        public float mRadius = 0.0f;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050917)
        public float[] mRadiusArray = null;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050917)
        public int mShape = 0;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050917)
        public ColorStateList mSolidColors;
        public ColorStateList mStrokeColors;
        @UnsupportedAppUsage(trackingBug = 124050917)
        public float mStrokeDashGap = 0.0f;
        @UnsupportedAppUsage(trackingBug = 124050917)
        public float mStrokeDashWidth = 0.0f;
        @UnsupportedAppUsage(trackingBug = 124050917)
        public int mStrokeWidth = -1;
        public int[] mTempColors;
        public float[] mTempPositions;
        int[] mThemeAttrs;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050218)
        public int mThickness = -1;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050218)
        public float mThicknessRatio = GradientDrawable.DEFAULT_THICKNESS_RATIO;
        ColorStateList mTint = null;
        boolean mUseLevel = false;
        boolean mUseLevelForShape = true;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050917)
        public int mWidth = -1;

        public GradientState(Orientation orientation, int[] gradientColors) {
            setOrientation(orientation);
            setGradientColors(gradientColors);
        }

        public GradientState(GradientState orig, Resources res) {
            this.mChangingConfigurations = orig.mChangingConfigurations;
            this.mShape = orig.mShape;
            this.mGradient = orig.mGradient;
            this.mAngle = orig.mAngle;
            this.mOrientation = orig.mOrientation;
            this.mSolidColors = orig.mSolidColors;
            int[] iArr = orig.mGradientColors;
            if (iArr != null) {
                this.mGradientColors = (int[]) iArr.clone();
            }
            float[] fArr = orig.mPositions;
            if (fArr != null) {
                this.mPositions = (float[]) fArr.clone();
            }
            this.mStrokeColors = orig.mStrokeColors;
            this.mStrokeWidth = orig.mStrokeWidth;
            this.mStrokeDashWidth = orig.mStrokeDashWidth;
            this.mStrokeDashGap = orig.mStrokeDashGap;
            this.mRadius = orig.mRadius;
            fArr = orig.mRadiusArray;
            if (fArr != null) {
                this.mRadiusArray = (float[]) fArr.clone();
            }
            Rect rect = orig.mPadding;
            if (rect != null) {
                this.mPadding = new Rect(rect);
            }
            this.mWidth = orig.mWidth;
            this.mHeight = orig.mHeight;
            this.mInnerRadiusRatio = orig.mInnerRadiusRatio;
            this.mThicknessRatio = orig.mThicknessRatio;
            this.mInnerRadius = orig.mInnerRadius;
            this.mThickness = orig.mThickness;
            this.mDither = orig.mDither;
            this.mOpticalInsets = orig.mOpticalInsets;
            this.mCenterX = orig.mCenterX;
            this.mCenterY = orig.mCenterY;
            this.mGradientRadius = orig.mGradientRadius;
            this.mGradientRadiusType = orig.mGradientRadiusType;
            this.mUseLevel = orig.mUseLevel;
            this.mUseLevelForShape = orig.mUseLevelForShape;
            this.mOpaqueOverBounds = orig.mOpaqueOverBounds;
            this.mOpaqueOverShape = orig.mOpaqueOverShape;
            this.mTint = orig.mTint;
            this.mBlendMode = orig.mBlendMode;
            this.mThemeAttrs = orig.mThemeAttrs;
            this.mAttrSize = orig.mAttrSize;
            this.mAttrGradient = orig.mAttrGradient;
            this.mAttrSolid = orig.mAttrSolid;
            this.mAttrStroke = orig.mAttrStroke;
            this.mAttrCorners = orig.mAttrCorners;
            this.mAttrPadding = orig.mAttrPadding;
            this.mDensity = Drawable.resolveDensity(res, orig.mDensity);
            int i = orig.mDensity;
            int i2 = this.mDensity;
            if (i != i2) {
                applyDensityScaling(i, i2);
            }
        }

        public final void setDensity(int targetDensity) {
            if (this.mDensity != targetDensity) {
                int sourceDensity = this.mDensity;
                this.mDensity = targetDensity;
                applyDensityScaling(sourceDensity, targetDensity);
            }
        }

        public boolean hasCenterColor() {
            int[] iArr = this.mGradientColors;
            return iArr != null && iArr.length == 3;
        }

        private void applyDensityScaling(int sourceDensity, int targetDensity) {
            int i = this.mInnerRadius;
            if (i > 0) {
                this.mInnerRadius = Drawable.scaleFromDensity(i, sourceDensity, targetDensity, true);
            }
            i = this.mThickness;
            if (i > 0) {
                this.mThickness = Drawable.scaleFromDensity(i, sourceDensity, targetDensity, true);
            }
            if (this.mOpticalInsets != Insets.NONE) {
                this.mOpticalInsets = Insets.of(Drawable.scaleFromDensity(this.mOpticalInsets.left, sourceDensity, targetDensity, true), Drawable.scaleFromDensity(this.mOpticalInsets.top, sourceDensity, targetDensity, true), Drawable.scaleFromDensity(this.mOpticalInsets.right, sourceDensity, targetDensity, true), Drawable.scaleFromDensity(this.mOpticalInsets.bottom, sourceDensity, targetDensity, true));
            }
            Rect rect = this.mPadding;
            if (rect != null) {
                rect.left = Drawable.scaleFromDensity(rect.left, sourceDensity, targetDensity, false);
                rect = this.mPadding;
                rect.top = Drawable.scaleFromDensity(rect.top, sourceDensity, targetDensity, false);
                rect = this.mPadding;
                rect.right = Drawable.scaleFromDensity(rect.right, sourceDensity, targetDensity, false);
                rect = this.mPadding;
                rect.bottom = Drawable.scaleFromDensity(rect.bottom, sourceDensity, targetDensity, false);
            }
            float f = this.mRadius;
            if (f > 0.0f) {
                this.mRadius = Drawable.scaleFromDensity(f, sourceDensity, targetDensity);
            }
            float[] fArr = this.mRadiusArray;
            if (fArr != null) {
                fArr[0] = (float) Drawable.scaleFromDensity((int) fArr[0], sourceDensity, targetDensity, true);
                fArr = this.mRadiusArray;
                fArr[1] = (float) Drawable.scaleFromDensity((int) fArr[1], sourceDensity, targetDensity, true);
                fArr = this.mRadiusArray;
                fArr[2] = (float) Drawable.scaleFromDensity((int) fArr[2], sourceDensity, targetDensity, true);
                fArr = this.mRadiusArray;
                fArr[3] = (float) Drawable.scaleFromDensity((int) fArr[3], sourceDensity, targetDensity, true);
            }
            i = this.mStrokeWidth;
            if (i > 0) {
                this.mStrokeWidth = Drawable.scaleFromDensity(i, sourceDensity, targetDensity, true);
            }
            if (this.mStrokeDashWidth > 0.0f) {
                this.mStrokeDashWidth = Drawable.scaleFromDensity(this.mStrokeDashGap, sourceDensity, targetDensity);
            }
            f = this.mStrokeDashGap;
            if (f > 0.0f) {
                this.mStrokeDashGap = Drawable.scaleFromDensity(f, sourceDensity, targetDensity);
            }
            if (this.mGradientRadiusType == 0) {
                this.mGradientRadius = Drawable.scaleFromDensity(this.mGradientRadius, sourceDensity, targetDensity);
            }
            i = this.mWidth;
            if (i > 0) {
                this.mWidth = Drawable.scaleFromDensity(i, sourceDensity, targetDensity, true);
            }
            i = this.mHeight;
            if (i > 0) {
                this.mHeight = Drawable.scaleFromDensity(i, sourceDensity, targetDensity, true);
            }
        }

        public boolean canApplyTheme() {
            if (this.mThemeAttrs == null && this.mAttrSize == null && this.mAttrGradient == null && this.mAttrSolid == null && this.mAttrStroke == null && this.mAttrCorners == null && this.mAttrPadding == null) {
                ColorStateList colorStateList = this.mTint;
                if (colorStateList == null || !colorStateList.canApplyTheme()) {
                    colorStateList = this.mStrokeColors;
                    if (colorStateList == null || !colorStateList.canApplyTheme()) {
                        colorStateList = this.mSolidColors;
                        if ((colorStateList == null || !colorStateList.canApplyTheme()) && !super.canApplyTheme()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        public Drawable newDrawable() {
            return new GradientDrawable(this, null);
        }

        public Drawable newDrawable(Resources res) {
            GradientState state;
            if (Drawable.resolveDensity(res, this.mDensity) != this.mDensity) {
                state = new GradientState(this, res);
            } else {
                state = this;
            }
            return new GradientDrawable(state, res);
        }

        public int getChangingConfigurations() {
            int i = this.mChangingConfigurations;
            ColorStateList colorStateList = this.mStrokeColors;
            int i2 = 0;
            i |= colorStateList != null ? colorStateList.getChangingConfigurations() : 0;
            colorStateList = this.mSolidColors;
            i |= colorStateList != null ? colorStateList.getChangingConfigurations() : 0;
            colorStateList = this.mTint;
            if (colorStateList != null) {
                i2 = colorStateList.getChangingConfigurations();
            }
            return i | i2;
        }

        public void setShape(int shape) {
            this.mShape = shape;
            computeOpacity();
        }

        public void setGradientType(int gradient) {
            this.mGradient = gradient;
        }

        public void setGradientCenter(float x, float y) {
            this.mCenterX = x;
            this.mCenterY = y;
        }

        public void setOrientation(Orientation orientation) {
            this.mAngle = getAngleFromOrientation(orientation);
            this.mOrientation = orientation;
        }

        public Orientation getOrientation() {
            updateGradientStateOrientation();
            return this.mOrientation;
        }

        private void updateGradientStateOrientation() {
            if (this.mGradient == 0) {
                int angle = this.mAngle;
                if (angle % 45 == 0) {
                    Orientation orientation;
                    if (angle == 0) {
                        orientation = Orientation.LEFT_RIGHT;
                    } else if (angle == 45) {
                        orientation = Orientation.BL_TR;
                    } else if (angle == 90) {
                        orientation = Orientation.BOTTOM_TOP;
                    } else if (angle == 135) {
                        orientation = Orientation.BR_TL;
                    } else if (angle == 180) {
                        orientation = Orientation.RIGHT_LEFT;
                    } else if (angle == 225) {
                        orientation = Orientation.TR_BL;
                    } else if (angle == 270) {
                        orientation = Orientation.TOP_BOTTOM;
                    } else if (angle != 315) {
                        orientation = Orientation.LEFT_RIGHT;
                    } else {
                        orientation = Orientation.TL_BR;
                    }
                    this.mOrientation = orientation;
                    return;
                }
                throw new IllegalArgumentException("Linear gradient requires 'angle' attribute to be a multiple of 45");
            }
        }

        private int getAngleFromOrientation(Orientation orientation) {
            if (orientation == null) {
                return 0;
            }
            switch (orientation) {
                case TOP_BOTTOM:
                    return 270;
                case TR_BL:
                    return 225;
                case RIGHT_LEFT:
                    return 180;
                case BR_TL:
                    return 135;
                case BOTTOM_TOP:
                    return 90;
                case BL_TR:
                    return 45;
                case TL_BR:
                    return 315;
                default:
                    return 0;
            }
        }

        public void setGradientColors(int[] colors) {
            this.mGradientColors = colors;
            this.mSolidColors = null;
            computeOpacity();
        }

        public void setSolidColors(ColorStateList colors) {
            this.mGradientColors = null;
            this.mSolidColors = colors;
            computeOpacity();
        }

        private void computeOpacity() {
            boolean z = false;
            this.mOpaqueOverBounds = false;
            this.mOpaqueOverShape = false;
            if (this.mGradientColors != null) {
                int i = 0;
                while (true) {
                    int[] iArr = this.mGradientColors;
                    if (i >= iArr.length) {
                        break;
                    } else if (GradientDrawable.isOpaque(iArr[i])) {
                        i++;
                    } else {
                        return;
                    }
                }
            }
            if (this.mGradientColors != null || this.mSolidColors != null) {
                this.mOpaqueOverShape = true;
                if (this.mShape == 0 && this.mRadius <= 0.0f && this.mRadiusArray == null) {
                    z = true;
                }
                this.mOpaqueOverBounds = z;
            }
        }

        public void setStroke(int width, ColorStateList colors, float dashWidth, float dashGap) {
            this.mStrokeWidth = width;
            this.mStrokeColors = colors;
            this.mStrokeDashWidth = dashWidth;
            this.mStrokeDashGap = dashGap;
            computeOpacity();
        }

        public void setCornerRadius(float radius) {
            if (radius < 0.0f) {
                radius = 0.0f;
            }
            this.mRadius = radius;
            this.mRadiusArray = null;
            computeOpacity();
        }

        public void setCornerRadii(float[] radii) {
            this.mRadiusArray = radii;
            if (radii == null) {
                this.mRadius = 0.0f;
            }
            computeOpacity();
        }

        public void setSize(int width, int height) {
            this.mWidth = width;
            this.mHeight = height;
        }

        public void setGradientRadius(float gradientRadius, int type) {
            this.mGradientRadius = gradientRadius;
            this.mGradientRadiusType = type;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface GradientType {
    }

    public enum Orientation {
        TOP_BOTTOM,
        TR_BL,
        RIGHT_LEFT,
        BR_TL,
        BOTTOM_TOP,
        BL_TR,
        LEFT_RIGHT,
        TL_BR
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RadiusType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Shape {
    }

    public GradientDrawable() {
        this(new GradientState(Orientation.TOP_BOTTOM, null), null);
    }

    public GradientDrawable(Orientation orientation, int[] colors) {
        this(new GradientState(orientation, colors), null);
    }

    public boolean getPadding(Rect padding) {
        Rect rect = this.mPadding;
        if (rect == null) {
            return super.getPadding(padding);
        }
        padding.set(rect);
        return true;
    }

    public void setCornerRadii(float[] radii) {
        this.mGradientState.setCornerRadii(radii);
        this.mPathIsDirty = true;
        invalidateSelf();
    }

    public float[] getCornerRadii() {
        return (float[]) this.mGradientState.mRadiusArray.clone();
    }

    public void setCornerRadius(float radius) {
        this.mGradientState.setCornerRadius(radius);
        this.mPathIsDirty = true;
        invalidateSelf();
    }

    public float getCornerRadius() {
        return this.mGradientState.mRadius;
    }

    public void setStroke(int width, int color) {
        setStroke(width, color, 0.0f, 0.0f);
    }

    public void setStroke(int width, ColorStateList colorStateList) {
        setStroke(width, colorStateList, 0.0f, 0.0f);
    }

    public void setStroke(int width, int color, float dashWidth, float dashGap) {
        this.mGradientState.setStroke(width, ColorStateList.valueOf(color), dashWidth, dashGap);
        setStrokeInternal(width, color, dashWidth, dashGap);
    }

    public void setStroke(int width, ColorStateList colorStateList, float dashWidth, float dashGap) {
        int color;
        this.mGradientState.setStroke(width, colorStateList, dashWidth, dashGap);
        if (colorStateList == null) {
            color = 0;
        } else {
            color = colorStateList.getColorForState(getState(), 0);
        }
        setStrokeInternal(width, color, dashWidth, dashGap);
    }

    private void setStrokeInternal(int width, int color, float dashWidth, float dashGap) {
        if (this.mStrokePaint == null) {
            this.mStrokePaint = new Paint(1);
            this.mStrokePaint.setStyle(Style.STROKE);
        }
        this.mStrokePaint.setStrokeWidth((float) width);
        this.mStrokePaint.setColor(color);
        DashPathEffect e = null;
        if (dashWidth > 0.0f) {
            e = new DashPathEffect(new float[]{dashWidth, dashGap}, 0.0f);
        }
        this.mStrokePaint.setPathEffect(e);
        this.mGradientIsDirty = true;
        invalidateSelf();
    }

    public void setSize(int width, int height) {
        this.mGradientState.setSize(width, height);
        this.mPathIsDirty = true;
        invalidateSelf();
    }

    public void setShape(int shape) {
        this.mRingPath = null;
        this.mPathIsDirty = true;
        this.mGradientState.setShape(shape);
        invalidateSelf();
    }

    public int getShape() {
        return this.mGradientState.mShape;
    }

    public void setGradientType(int gradient) {
        this.mGradientState.setGradientType(gradient);
        this.mGradientIsDirty = true;
        invalidateSelf();
    }

    public int getGradientType() {
        return this.mGradientState.mGradient;
    }

    public void setGradientCenter(float x, float y) {
        this.mGradientState.setGradientCenter(x, y);
        this.mGradientIsDirty = true;
        invalidateSelf();
    }

    public float getGradientCenterX() {
        return this.mGradientState.mCenterX;
    }

    public float getGradientCenterY() {
        return this.mGradientState.mCenterY;
    }

    public void setGradientRadius(float gradientRadius) {
        this.mGradientState.setGradientRadius(gradientRadius, 0);
        this.mGradientIsDirty = true;
        invalidateSelf();
    }

    public float getGradientRadius() {
        if (this.mGradientState.mGradient != 1) {
            return 0.0f;
        }
        ensureValidRect();
        return this.mGradientRadius;
    }

    public void setUseLevel(boolean useLevel) {
        this.mGradientState.mUseLevel = useLevel;
        this.mGradientIsDirty = true;
        invalidateSelf();
    }

    public boolean getUseLevel() {
        return this.mGradientState.mUseLevel;
    }

    private int modulateAlpha(int alpha) {
        int scale = this.mAlpha;
        return (alpha * (scale + (scale >> 7))) >> 8;
    }

    public Orientation getOrientation() {
        return this.mGradientState.getOrientation();
    }

    public void setOrientation(Orientation orientation) {
        this.mGradientState.setOrientation(orientation);
        this.mGradientIsDirty = true;
        invalidateSelf();
    }

    public void setColors(int[] colors) {
        setColors(colors, null);
    }

    public void setColors(int[] colors, float[] offsets) {
        this.mGradientState.setGradientColors(colors);
        this.mGradientState.mPositions = offsets;
        this.mGradientIsDirty = true;
        invalidateSelf();
    }

    public int[] getColors() {
        return this.mGradientState.mGradientColors == null ? null : (int[]) this.mGradientState.mGradientColors.clone();
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x003a  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00ba  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0060  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0144  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00f3  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x01b5  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x01b1  */
    public void draw(android.graphics.Canvas r22) {
        /*
        r21 = this;
        r0 = r21;
        r7 = r22;
        r1 = r21.ensureValidRect();
        if (r1 != 0) goto L_0x000b;
    L_0x000a:
        return;
    L_0x000b:
        r1 = r0.mFillPaint;
        r8 = r1.getAlpha();
        r1 = r0.mStrokePaint;
        r2 = 0;
        if (r1 == 0) goto L_0x001b;
    L_0x0016:
        r1 = r1.getAlpha();
        goto L_0x001c;
    L_0x001b:
        r1 = r2;
    L_0x001c:
        r9 = r1;
        r10 = r0.modulateAlpha(r8);
        r11 = r0.modulateAlpha(r9);
        r12 = 0;
        if (r11 <= 0) goto L_0x0036;
    L_0x0028:
        r1 = r0.mStrokePaint;
        if (r1 == 0) goto L_0x0036;
    L_0x002c:
        r1 = r1.getStrokeWidth();
        r1 = (r1 > r12 ? 1 : (r1 == r12 ? 0 : -1));
        if (r1 <= 0) goto L_0x0036;
    L_0x0034:
        r1 = 1;
        goto L_0x0037;
    L_0x0036:
        r1 = r2;
    L_0x0037:
        r14 = r1;
        if (r10 <= 0) goto L_0x003c;
    L_0x003a:
        r1 = 1;
        goto L_0x003d;
    L_0x003c:
        r1 = r2;
    L_0x003d:
        r15 = r1;
        r6 = r0.mGradientState;
        r1 = r0.mColorFilter;
        if (r1 == 0) goto L_0x0045;
    L_0x0044:
        goto L_0x0047;
    L_0x0045:
        r1 = r0.mBlendModeColorFilter;
    L_0x0047:
        r5 = r1;
        r4 = 2;
        if (r14 == 0) goto L_0x005c;
    L_0x004b:
        if (r15 == 0) goto L_0x005c;
    L_0x004d:
        r1 = r6.mShape;
        if (r1 == r4) goto L_0x005c;
    L_0x0051:
        r1 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        if (r11 >= r1) goto L_0x005c;
    L_0x0055:
        r3 = r0.mAlpha;
        if (r3 < r1) goto L_0x005b;
    L_0x0059:
        if (r5 == 0) goto L_0x005c;
    L_0x005b:
        r2 = 1;
    L_0x005c:
        r16 = r2;
        if (r16 == 0) goto L_0x00ba;
    L_0x0060:
        r1 = r0.mLayerPaint;
        if (r1 != 0) goto L_0x006b;
    L_0x0064:
        r1 = new android.graphics.Paint;
        r1.<init>();
        r0.mLayerPaint = r1;
    L_0x006b:
        r1 = r0.mLayerPaint;
        r2 = r6.mDither;
        r1.setDither(r2);
        r1 = r0.mLayerPaint;
        r2 = r0.mAlpha;
        r1.setAlpha(r2);
        r1 = r0.mLayerPaint;
        r1.setColorFilter(r5);
        r1 = r0.mStrokePaint;
        r17 = r1.getStrokeWidth();
        r1 = r0.mRect;
        r1 = r1.left;
        r2 = r1 - r17;
        r1 = r0.mRect;
        r1 = r1.top;
        r3 = r1 - r17;
        r1 = r0.mRect;
        r1 = r1.right;
        r18 = r1 + r17;
        r1 = r0.mRect;
        r1 = r1.bottom;
        r19 = r1 + r17;
        r1 = r0.mLayerPaint;
        r20 = r1;
        r1 = r22;
        r12 = r4;
        r4 = r18;
        r12 = r5;
        r5 = r19;
        r13 = r6;
        r6 = r20;
        r1.saveLayer(r2, r3, r4, r5, r6);
        r1 = r0.mFillPaint;
        r2 = 0;
        r1.setColorFilter(r2);
        r1 = r0.mStrokePaint;
        r1.setColorFilter(r2);
        goto L_0x00ef;
    L_0x00ba:
        r12 = r5;
        r13 = r6;
        r1 = r0.mFillPaint;
        r1.setAlpha(r10);
        r1 = r0.mFillPaint;
        r2 = r13.mDither;
        r1.setDither(r2);
        r1 = r0.mFillPaint;
        r1.setColorFilter(r12);
        if (r12 == 0) goto L_0x00dc;
    L_0x00cf:
        r1 = r13.mSolidColors;
        if (r1 != 0) goto L_0x00dc;
    L_0x00d3:
        r1 = r0.mFillPaint;
        r2 = r0.mAlpha;
        r2 = r2 << 24;
        r1.setColor(r2);
    L_0x00dc:
        if (r14 == 0) goto L_0x00ef;
    L_0x00de:
        r1 = r0.mStrokePaint;
        r1.setAlpha(r11);
        r1 = r0.mStrokePaint;
        r2 = r13.mDither;
        r1.setDither(r2);
        r1 = r0.mStrokePaint;
        r1.setColorFilter(r12);
    L_0x00ef:
        r1 = r13.mShape;
        if (r1 == 0) goto L_0x0144;
    L_0x00f3:
        r2 = 1;
        if (r1 == r2) goto L_0x0133;
    L_0x00f6:
        r2 = 2;
        if (r1 == r2) goto L_0x0110;
    L_0x00f9:
        r2 = 3;
        if (r1 == r2) goto L_0x00fe;
    L_0x00fc:
        goto L_0x01af;
    L_0x00fe:
        r1 = r0.buildRing(r13);
        r2 = r0.mFillPaint;
        r7.drawPath(r1, r2);
        if (r14 == 0) goto L_0x01af;
    L_0x0109:
        r2 = r0.mStrokePaint;
        r7.drawPath(r1, r2);
        goto L_0x01af;
    L_0x0110:
        r6 = r0.mRect;
        r17 = r6.centerY();
        if (r14 == 0) goto L_0x012f;
    L_0x0118:
        r2 = r6.left;
        r4 = r6.right;
        r5 = r0.mStrokePaint;
        r1 = r22;
        r3 = r17;
        r18 = r5;
        r5 = r17;
        r19 = r6;
        r6 = r18;
        r1.drawLine(r2, r3, r4, r5, r6);
        goto L_0x01af;
    L_0x012f:
        r19 = r6;
        goto L_0x01af;
    L_0x0133:
        r1 = r0.mRect;
        r2 = r0.mFillPaint;
        r7.drawOval(r1, r2);
        if (r14 == 0) goto L_0x01af;
    L_0x013c:
        r1 = r0.mRect;
        r2 = r0.mStrokePaint;
        r7.drawOval(r1, r2);
        goto L_0x01af;
    L_0x0144:
        r1 = r13.mRadiusArray;
        if (r1 == 0) goto L_0x015c;
    L_0x0148:
        r21.buildPathIfDirty();
        r1 = r0.mPath;
        r2 = r0.mFillPaint;
        r7.drawPath(r1, r2);
        if (r14 == 0) goto L_0x01af;
    L_0x0154:
        r1 = r0.mPath;
        r2 = r0.mStrokePaint;
        r7.drawPath(r1, r2);
        goto L_0x01af;
    L_0x015c:
        r1 = r13.mRadius;
        r2 = 0;
        r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r1 <= 0) goto L_0x018d;
    L_0x0163:
        r1 = r13.mRadius;
        r2 = r0.mRect;
        r2 = r2.width();
        r3 = r0.mRect;
        r3 = r3.height();
        r2 = java.lang.Math.min(r2, r3);
        r3 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r2 = r2 * r3;
        r1 = java.lang.Math.min(r1, r2);
        r2 = r0.mRect;
        r3 = r0.mFillPaint;
        r7.drawRoundRect(r2, r1, r1, r3);
        if (r14 == 0) goto L_0x018c;
    L_0x0185:
        r2 = r0.mRect;
        r3 = r0.mStrokePaint;
        r7.drawRoundRect(r2, r1, r1, r3);
    L_0x018c:
        goto L_0x01af;
    L_0x018d:
        r1 = r0.mFillPaint;
        r1 = r1.getColor();
        if (r1 != 0) goto L_0x019f;
    L_0x0195:
        if (r12 != 0) goto L_0x019f;
    L_0x0197:
        r1 = r0.mFillPaint;
        r1 = r1.getShader();
        if (r1 == 0) goto L_0x01a6;
    L_0x019f:
        r1 = r0.mRect;
        r2 = r0.mFillPaint;
        r7.drawRect(r1, r2);
    L_0x01a6:
        if (r14 == 0) goto L_0x01af;
    L_0x01a8:
        r1 = r0.mRect;
        r2 = r0.mStrokePaint;
        r7.drawRect(r1, r2);
    L_0x01af:
        if (r16 == 0) goto L_0x01b5;
    L_0x01b1:
        r22.restore();
        goto L_0x01c1;
    L_0x01b5:
        r1 = r0.mFillPaint;
        r1.setAlpha(r8);
        if (r14 == 0) goto L_0x01c1;
    L_0x01bc:
        r1 = r0.mStrokePaint;
        r1.setAlpha(r9);
    L_0x01c1:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.GradientDrawable.draw(android.graphics.Canvas):void");
    }

    public void setXfermode(Xfermode mode) {
        super.setXfermode(mode);
        this.mFillPaint.setXfermode(mode);
    }

    public void setAntiAlias(boolean aa) {
        this.mFillPaint.setAntiAlias(aa);
    }

    private void buildPathIfDirty() {
        GradientState st = this.mGradientState;
        if (this.mPathIsDirty) {
            ensureValidRect();
            this.mPath.reset();
            this.mPath.addRoundRect(this.mRect, st.mRadiusArray, Direction.CW);
            this.mPathIsDirty = false;
        }
    }

    public void setInnerRadiusRatio(float innerRadiusRatio) {
        if (innerRadiusRatio > 0.0f) {
            this.mGradientState.mInnerRadiusRatio = innerRadiusRatio;
            this.mPathIsDirty = true;
            invalidateSelf();
            return;
        }
        throw new IllegalArgumentException("Ratio must be greater than zero");
    }

    public float getInnerRadiusRatio() {
        return this.mGradientState.mInnerRadiusRatio;
    }

    public void setInnerRadius(int innerRadius) {
        this.mGradientState.mInnerRadius = innerRadius;
        this.mPathIsDirty = true;
        invalidateSelf();
    }

    public int getInnerRadius() {
        return this.mGradientState.mInnerRadius;
    }

    public void setThicknessRatio(float thicknessRatio) {
        if (thicknessRatio > 0.0f) {
            this.mGradientState.mThicknessRatio = thicknessRatio;
            this.mPathIsDirty = true;
            invalidateSelf();
            return;
        }
        throw new IllegalArgumentException("Ratio must be greater than zero");
    }

    public float getThicknessRatio() {
        return this.mGradientState.mThicknessRatio;
    }

    public void setThickness(int thickness) {
        this.mGradientState.mThickness = thickness;
        this.mPathIsDirty = true;
        invalidateSelf();
    }

    public int getThickness() {
        return this.mGradientState.mThickness;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        if (this.mGradientState.mPadding == null) {
            this.mGradientState.mPadding = new Rect();
        }
        this.mGradientState.mPadding.set(left, top, right, bottom);
        this.mPadding = this.mGradientState.mPadding;
        invalidateSelf();
    }

    private Path buildRing(GradientState st) {
        if (this.mRingPath != null && (!st.mUseLevelForShape || !this.mPathIsDirty)) {
            return this.mRingPath;
        }
        this.mPathIsDirty = false;
        float sweep = st.mUseLevelForShape ? (((float) getLevel()) * 360.0f) / 10000.0f : 360.0f;
        RectF bounds = new RectF(this.mRect);
        float x = bounds.width() / 2.0f;
        float y = bounds.height() / 2.0f;
        float thickness = st.mThickness != -1 ? (float) st.mThickness : bounds.width() / st.mThicknessRatio;
        float radius = st.mInnerRadius != -1 ? (float) st.mInnerRadius : bounds.width() / st.mInnerRadiusRatio;
        RectF innerBounds = new RectF(bounds);
        innerBounds.inset(x - radius, y - radius);
        bounds = new RectF(innerBounds);
        bounds.inset(-thickness, -thickness);
        Path path = this.mRingPath;
        if (path == null) {
            this.mRingPath = new Path();
        } else {
            path.reset();
        }
        path = this.mRingPath;
        if (sweep >= 360.0f || sweep <= -360.0f) {
            path.addOval(bounds, Direction.CW);
            path.addOval(innerBounds, Direction.CCW);
        } else {
            path.setFillType(FillType.EVEN_ODD);
            path.moveTo(x + radius, y);
            path.lineTo((x + radius) + thickness, y);
            path.arcTo(bounds, 0.0f, sweep, false);
            path.arcTo(innerBounds, sweep, -sweep, false);
            path.close();
        }
        return path;
    }

    public void setColor(int argb) {
        this.mGradientState.setSolidColors(ColorStateList.valueOf(argb));
        this.mFillPaint.setColor(argb);
        invalidateSelf();
    }

    public void setColor(ColorStateList colorStateList) {
        if (colorStateList == null) {
            setColor(0);
            return;
        }
        int color = colorStateList.getColorForState(getState(), 0);
        this.mGradientState.setSolidColors(colorStateList);
        this.mFillPaint.setColor(color);
        invalidateSelf();
    }

    public ColorStateList getColor() {
        return this.mGradientState.mSolidColors;
    }

    /* Access modifiers changed, original: protected */
    public boolean onStateChange(int[] stateSet) {
        boolean invalidateSelf = false;
        GradientState s = this.mGradientState;
        ColorStateList solidColors = s.mSolidColors;
        if (solidColors != null) {
            int newColor = solidColors.getColorForState(stateSet, 0);
            if (this.mFillPaint.getColor() != newColor) {
                this.mFillPaint.setColor(newColor);
                invalidateSelf = true;
            }
        }
        Paint strokePaint = this.mStrokePaint;
        if (strokePaint != null) {
            ColorStateList strokeColors = s.mStrokeColors;
            if (strokeColors != null) {
                int newColor2 = strokeColors.getColorForState(stateSet, 0);
                if (strokePaint.getColor() != newColor2) {
                    strokePaint.setColor(newColor2);
                    invalidateSelf = true;
                }
            }
        }
        if (!(s.mTint == null || s.mBlendMode == null)) {
            this.mBlendModeColorFilter = updateBlendModeFilter(this.mBlendModeColorFilter, s.mTint, s.mBlendMode);
            invalidateSelf = true;
        }
        if (!invalidateSelf) {
            return false;
        }
        invalidateSelf();
        return true;
    }

    public boolean isStateful() {
        GradientState s = this.mGradientState;
        return super.isStateful() || ((s.mSolidColors != null && s.mSolidColors.isStateful()) || ((s.mStrokeColors != null && s.mStrokeColors.isStateful()) || (s.mTint != null && s.mTint.isStateful())));
    }

    public boolean hasFocusStateSpecified() {
        GradientState s = this.mGradientState;
        return (s.mSolidColors != null && s.mSolidColors.hasFocusStateSpecified()) || ((s.mStrokeColors != null && s.mStrokeColors.hasFocusStateSpecified()) || (s.mTint != null && s.mTint.hasFocusStateSpecified()));
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mGradientState.getChangingConfigurations();
    }

    public void setAlpha(int alpha) {
        if (alpha != this.mAlpha) {
            this.mAlpha = alpha;
            invalidateSelf();
        }
    }

    public int getAlpha() {
        return this.mAlpha;
    }

    public void setDither(boolean dither) {
        if (dither != this.mGradientState.mDither) {
            this.mGradientState.mDither = dither;
            invalidateSelf();
        }
    }

    public ColorFilter getColorFilter() {
        return this.mColorFilter;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (colorFilter != this.mColorFilter) {
            this.mColorFilter = colorFilter;
            invalidateSelf();
        }
    }

    public void setTintList(ColorStateList tint) {
        GradientState gradientState = this.mGradientState;
        gradientState.mTint = tint;
        this.mBlendModeColorFilter = updateBlendModeFilter(this.mBlendModeColorFilter, tint, gradientState.mBlendMode);
        invalidateSelf();
    }

    public void setTintBlendMode(BlendMode blendMode) {
        GradientState gradientState = this.mGradientState;
        gradientState.mBlendMode = blendMode;
        this.mBlendModeColorFilter = updateBlendModeFilter(this.mBlendModeColorFilter, gradientState.mTint, blendMode);
        invalidateSelf();
    }

    public int getOpacity() {
        return (this.mAlpha == 255 && this.mGradientState.mOpaqueOverBounds && isOpaqueForState()) ? -1 : -3;
    }

    /* Access modifiers changed, original: protected */
    public void onBoundsChange(Rect r) {
        super.onBoundsChange(r);
        this.mRingPath = null;
        this.mPathIsDirty = true;
        this.mGradientIsDirty = true;
    }

    /* Access modifiers changed, original: protected */
    public boolean onLevelChange(int level) {
        super.onLevelChange(level);
        this.mGradientIsDirty = true;
        this.mPathIsDirty = true;
        invalidateSelf();
        return true;
    }

    private boolean ensureValidRect() {
        if (this.mGradientIsDirty) {
            this.mGradientIsDirty = false;
            Rect bounds = getBounds();
            float inset = 0.0f;
            Paint paint = this.mStrokePaint;
            if (paint != null) {
                inset = paint.getStrokeWidth() * 0.5f;
            }
            GradientState st = this.mGradientState;
            this.mRect.set(((float) bounds.left) + inset, ((float) bounds.top) + inset, ((float) bounds.right) - inset, ((float) bounds.bottom) - inset);
            int[] gradientColors = st.mGradientColors;
            if (gradientColors != null) {
                RectF r = this.mRect;
                float f = 1.0f;
                float level;
                float x0;
                float y0;
                if (st.mGradient == 0) {
                    float y02;
                    float y1;
                    if (st.mUseLevel) {
                        f = ((float) getLevel()) / 10000.0f;
                    }
                    level = f;
                    switch (st.getOrientation()) {
                        case TOP_BOTTOM:
                            x0 = r.left;
                            y02 = r.top;
                            f = x0;
                            y1 = r.bottom * level;
                            break;
                        case TR_BL:
                            x0 = r.right;
                            y02 = r.top;
                            f = r.left * level;
                            y1 = r.bottom * level;
                            break;
                        case RIGHT_LEFT:
                            x0 = r.right;
                            y02 = r.top;
                            f = r.left * level;
                            y1 = y02;
                            break;
                        case BR_TL:
                            x0 = r.right;
                            y02 = r.bottom;
                            f = r.left * level;
                            y1 = r.top * level;
                            break;
                        case BOTTOM_TOP:
                            x0 = r.left;
                            y02 = r.bottom;
                            f = x0;
                            y1 = r.top * level;
                            break;
                        case BL_TR:
                            x0 = r.left;
                            y02 = r.bottom;
                            f = r.right * level;
                            y1 = r.top * level;
                            break;
                        case LEFT_RIGHT:
                            x0 = r.left;
                            y02 = r.top;
                            f = r.right * level;
                            y1 = y02;
                            break;
                        default:
                            x0 = r.left;
                            y02 = r.top;
                            f = r.right * level;
                            y1 = r.bottom * level;
                            break;
                    }
                    Shader shader = r11;
                    Paint paint2 = this.mFillPaint;
                    Shader linearGradient = new LinearGradient(x0, y02, f, y1, gradientColors, st.mPositions, TileMode.CLAMP);
                    paint2.setShader(shader);
                } else if (st.mGradient == 1) {
                    level = r.left + ((r.right - r.left) * st.mCenterX);
                    y0 = r.top + ((r.bottom - r.top) * st.mCenterY);
                    f = st.mGradientRadius;
                    if (st.mGradientRadiusType == 1) {
                        f *= Math.min(st.mWidth >= 0 ? (float) st.mWidth : r.width(), st.mHeight >= 0 ? (float) st.mHeight : r.height());
                    } else if (st.mGradientRadiusType == 2) {
                        f *= Math.min(r.width(), r.height());
                    }
                    if (st.mUseLevel) {
                        f *= ((float) getLevel()) / 10000.0f;
                    }
                    this.mGradientRadius = f;
                    if (f <= 0.0f) {
                        x0 = 0.001f;
                    } else {
                        x0 = f;
                    }
                    this.mFillPaint.setShader(new RadialGradient(level, y0, x0, gradientColors, null, TileMode.CLAMP));
                } else if (st.mGradient == 2) {
                    float[] tempPositions;
                    y0 = r.left + ((r.right - r.left) * st.mCenterX);
                    x0 = r.top + ((r.bottom - r.top) * st.mCenterY);
                    int[] tempColors = gradientColors;
                    if (st.mUseLevel) {
                        tempColors = st.mTempColors;
                        int length = gradientColors.length;
                        if (tempColors == null || tempColors.length != length + 1) {
                            int[] iArr = new int[(length + 1)];
                            st.mTempColors = iArr;
                            tempColors = iArr;
                        }
                        System.arraycopy(gradientColors, 0, tempColors, 0, length);
                        tempColors[length] = gradientColors[length - 1];
                        tempPositions = st.mTempPositions;
                        float[] tempPositions2 = 1065353216 / ((float) (length - 1));
                        if (tempPositions == null || tempPositions.length != length + 1) {
                            float[] fArr = new float[(length + 1)];
                            st.mTempPositions = fArr;
                            tempPositions = fArr;
                        }
                        float level2 = ((float) getLevel()) / 10000.0f;
                        for (int i = 0; i < length; i++) {
                            tempPositions[i] = (((float) i) * tempPositions2) * level2;
                        }
                        tempPositions[length] = 1.0f;
                    } else {
                        tempPositions = null;
                    }
                    this.mFillPaint.setShader(new SweepGradient(y0, x0, tempColors, tempPositions));
                }
                if (st.mSolidColors == null) {
                    this.mFillPaint.setColor(-16777216);
                }
            }
        }
        return this.mRect.isEmpty() ^ 1;
    }

    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs, theme);
        this.mGradientState.setDensity(Drawable.resolveDensity(r, 0));
        TypedArray a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.GradientDrawable);
        updateStateFromTypedArray(a);
        a.recycle();
        inflateChildElements(r, parser, attrs, theme);
        updateLocalState(r);
    }

    public void applyTheme(Theme t) {
        super.applyTheme(t);
        GradientState state = this.mGradientState;
        if (state != null) {
            state.setDensity(Drawable.resolveDensity(t.getResources(), 0));
            if (state.mThemeAttrs != null) {
                TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.GradientDrawable);
                updateStateFromTypedArray(a);
                a.recycle();
            }
            if (state.mTint != null && state.mTint.canApplyTheme()) {
                state.mTint = state.mTint.obtainForTheme(t);
            }
            if (state.mSolidColors != null && state.mSolidColors.canApplyTheme()) {
                state.mSolidColors = state.mSolidColors.obtainForTheme(t);
            }
            if (state.mStrokeColors != null && state.mStrokeColors.canApplyTheme()) {
                state.mStrokeColors = state.mStrokeColors.obtainForTheme(t);
            }
            applyThemeChildElements(t);
            updateLocalState(t.getResources());
        }
    }

    private void updateStateFromTypedArray(TypedArray a) {
        GradientState state = this.mGradientState;
        state.mChangingConfigurations |= a.getChangingConfigurations();
        state.mThemeAttrs = a.extractThemeAttrs();
        state.mShape = a.getInt(3, state.mShape);
        state.mDither = a.getBoolean(0, state.mDither);
        if (state.mShape == 3) {
            state.mInnerRadius = a.getDimensionPixelSize(7, state.mInnerRadius);
            if (state.mInnerRadius == -1) {
                state.mInnerRadiusRatio = a.getFloat(4, state.mInnerRadiusRatio);
            }
            state.mThickness = a.getDimensionPixelSize(8, state.mThickness);
            if (state.mThickness == -1) {
                state.mThicknessRatio = a.getFloat(5, state.mThicknessRatio);
            }
            state.mUseLevelForShape = a.getBoolean(6, state.mUseLevelForShape);
        }
        int tintMode = a.getInt(9, -1);
        if (tintMode != -1) {
            state.mBlendMode = Drawable.parseBlendMode(tintMode, BlendMode.SRC_IN);
        }
        ColorStateList tint = a.getColorStateList(1);
        if (tint != null) {
            state.mTint = tint;
        }
        state.mOpticalInsets = Insets.of(a.getDimensionPixelSize(10, state.mOpticalInsets.left), a.getDimensionPixelSize(11, state.mOpticalInsets.top), a.getDimensionPixelSize(12, state.mOpticalInsets.right), a.getDimensionPixelSize(13, state.mOpticalInsets.bottom));
    }

    public boolean canApplyTheme() {
        GradientState gradientState = this.mGradientState;
        return (gradientState != null && gradientState.canApplyTheme()) || super.canApplyTheme();
    }

    private void applyThemeChildElements(Theme t) {
        TypedArray a;
        GradientState st = this.mGradientState;
        if (st.mAttrSize != null) {
            a = t.resolveAttributes(st.mAttrSize, R.styleable.GradientDrawableSize);
            updateGradientDrawableSize(a);
            a.recycle();
        }
        if (st.mAttrGradient != null) {
            a = t.resolveAttributes(st.mAttrGradient, R.styleable.GradientDrawableGradient);
            try {
                updateGradientDrawableGradient(t.getResources(), a);
            } finally {
                a.recycle();
            }
        }
        if (st.mAttrSolid != null) {
            a = t.resolveAttributes(st.mAttrSolid, R.styleable.GradientDrawableSolid);
            updateGradientDrawableSolid(a);
            a.recycle();
        }
        if (st.mAttrStroke != null) {
            a = t.resolveAttributes(st.mAttrStroke, R.styleable.GradientDrawableStroke);
            updateGradientDrawableStroke(a);
            a.recycle();
        }
        if (st.mAttrCorners != null) {
            a = t.resolveAttributes(st.mAttrCorners, R.styleable.DrawableCorners);
            updateDrawableCorners(a);
            a.recycle();
        }
        if (st.mAttrPadding != null) {
            a = t.resolveAttributes(st.mAttrPadding, R.styleable.GradientDrawablePadding);
            updateGradientDrawablePadding(a);
            a.recycle();
        }
    }

    private void inflateChildElements(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        int innerDepth = parser.getDepth() + 1;
        while (true) {
            int next = parser.next();
            int type = next;
            if (next != 1) {
                next = parser.getDepth();
                int depth = next;
                if (next < innerDepth && type == 3) {
                    return;
                }
                if (type == 2) {
                    if (depth <= innerDepth) {
                        String name = parser.getName();
                        TypedArray a;
                        if (name.equals(DumpHeapActivity.KEY_SIZE)) {
                            a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.GradientDrawableSize);
                            updateGradientDrawableSize(a);
                            a.recycle();
                        } else if (name.equals("gradient")) {
                            a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.GradientDrawableGradient);
                            updateGradientDrawableGradient(r, a);
                            a.recycle();
                        } else if (name.equals("solid")) {
                            a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.GradientDrawableSolid);
                            updateGradientDrawableSolid(a);
                            a.recycle();
                        } else if (name.equals("stroke")) {
                            a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.GradientDrawableStroke);
                            updateGradientDrawableStroke(a);
                            a.recycle();
                        } else if (name.equals("corners")) {
                            a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.DrawableCorners);
                            updateDrawableCorners(a);
                            a.recycle();
                        } else if (name.equals("padding")) {
                            a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.GradientDrawablePadding);
                            updateGradientDrawablePadding(a);
                            a.recycle();
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Bad element under <shape>: ");
                            stringBuilder.append(name);
                            Log.w("drawable", stringBuilder.toString());
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    private void updateGradientDrawablePadding(TypedArray a) {
        GradientState st = this.mGradientState;
        st.mChangingConfigurations |= a.getChangingConfigurations();
        st.mAttrPadding = a.extractThemeAttrs();
        if (st.mPadding == null) {
            st.mPadding = new Rect();
        }
        Rect pad = st.mPadding;
        pad.set(a.getDimensionPixelOffset(0, pad.left), a.getDimensionPixelOffset(1, pad.top), a.getDimensionPixelOffset(2, pad.right), a.getDimensionPixelOffset(3, pad.bottom));
        this.mPadding = pad;
    }

    private void updateDrawableCorners(TypedArray a) {
        GradientState st = this.mGradientState;
        st.mChangingConfigurations |= a.getChangingConfigurations();
        st.mAttrCorners = a.extractThemeAttrs();
        int radius = a.getDimensionPixelSize(0, (int) st.mRadius);
        setCornerRadius((float) radius);
        int topLeftRadius = a.getDimensionPixelSize(1, radius);
        int topRightRadius = a.getDimensionPixelSize(2, radius);
        int bottomLeftRadius = a.getDimensionPixelSize(3, radius);
        int bottomRightRadius = a.getDimensionPixelSize(4, radius);
        if (topLeftRadius != radius || topRightRadius != radius || bottomLeftRadius != radius || bottomRightRadius != radius) {
            setCornerRadii(new float[]{(float) topLeftRadius, (float) topLeftRadius, (float) topRightRadius, (float) topRightRadius, (float) bottomRightRadius, (float) bottomRightRadius, (float) bottomLeftRadius, (float) bottomLeftRadius});
        }
    }

    private void updateGradientDrawableStroke(TypedArray a) {
        GradientState st = this.mGradientState;
        st.mChangingConfigurations |= a.getChangingConfigurations();
        st.mAttrStroke = a.extractThemeAttrs();
        int width = a.getDimensionPixelSize(0, Math.max(0, st.mStrokeWidth));
        float dashWidth = a.getDimension(2, st.mStrokeDashWidth);
        ColorStateList colorStateList = a.getColorStateList(1);
        if (colorStateList == null) {
            colorStateList = st.mStrokeColors;
        }
        if (dashWidth != 0.0f) {
            setStroke(width, colorStateList, dashWidth, a.getDimension(4.2E-45f, st.mStrokeDashGap));
        } else {
            setStroke(width, colorStateList);
        }
    }

    private void updateGradientDrawableSolid(TypedArray a) {
        GradientState st = this.mGradientState;
        st.mChangingConfigurations |= a.getChangingConfigurations();
        st.mAttrSolid = a.extractThemeAttrs();
        ColorStateList colorStateList = a.getColorStateList(null);
        if (colorStateList != null) {
            setColor(colorStateList);
        }
    }

    private void updateGradientDrawableGradient(Resources r, TypedArray a) {
        int prevEnd;
        TypedArray typedArray = a;
        GradientState st = this.mGradientState;
        st.mChangingConfigurations |= a.getChangingConfigurations();
        st.mAttrGradient = a.extractThemeAttrs();
        st.mCenterX = getFloatOrFraction(typedArray, 5, st.mCenterX);
        st.mCenterY = getFloatOrFraction(typedArray, 6, st.mCenterY);
        st.mUseLevel = typedArray.getBoolean(2, st.mUseLevel);
        st.mGradient = typedArray.getInt(4, st.mGradient);
        boolean hasGradientColors = st.mGradientColors != null;
        boolean hasGradientCenter = st.hasCenterColor();
        int prevStart = hasGradientColors ? st.mGradientColors[0] : 0;
        int prevCenter = hasGradientCenter ? st.mGradientColors[1] : 0;
        if (st.hasCenterColor()) {
            prevEnd = st.mGradientColors[2];
        } else if (hasGradientColors) {
            prevEnd = st.mGradientColors[1];
        } else {
            prevEnd = 0;
        }
        int startColor = typedArray.getColor(0, prevStart);
        boolean hasCenterColor = typedArray.hasValue(8) || hasGradientCenter;
        int centerColor = typedArray.getColor(8, prevCenter);
        int endColor = typedArray.getColor(1, prevEnd);
        if (hasCenterColor) {
            st.mGradientColors = new int[3];
            st.mGradientColors[0] = startColor;
            st.mGradientColors[1] = centerColor;
            st.mGradientColors[2] = endColor;
            st.mPositions = new float[3];
            st.mPositions[0] = 0.0f;
            st.mPositions[1] = st.mCenterX != 0.5f ? st.mCenterX : st.mCenterY;
            st.mPositions[2] = 1.0f;
        } else {
            st.mGradientColors = new int[2];
            st.mGradientColors[0] = startColor;
            st.mGradientColors[1] = endColor;
        }
        st.mAngle = ((((int) typedArray.getFloat(3, (float) st.mAngle)) % 360) + 360) % 360;
        TypedValue tv = typedArray.peekValue(7);
        if (tv != null) {
            float radius;
            int radiusType;
            if (tv.type == 6) {
                radius = tv.getFraction(1.0f, 1.0f);
                if (((tv.data >> 0) & 15) == 1) {
                    radiusType = 2;
                } else {
                    radiusType = 1;
                }
            } else if (tv.type == 5) {
                radius = tv.getDimension(r.getDisplayMetrics());
                radiusType = 0;
            } else {
                radius = tv.getFloat();
                radiusType = 0;
            }
            st.mGradientRadius = radius;
            st.mGradientRadiusType = radiusType;
        }
    }

    private void updateGradientDrawableSize(TypedArray a) {
        GradientState st = this.mGradientState;
        st.mChangingConfigurations |= a.getChangingConfigurations();
        st.mAttrSize = a.extractThemeAttrs();
        st.mWidth = a.getDimensionPixelSize(1, st.mWidth);
        st.mHeight = a.getDimensionPixelSize(0, st.mHeight);
    }

    private static float getFloatOrFraction(TypedArray a, int index, float defaultValue) {
        TypedValue tv = a.peekValue(index);
        float v = defaultValue;
        if (tv == null) {
            return v;
        }
        return tv.type == 6 ? tv.getFraction(1.0f, 1.0f) : tv.getFloat();
    }

    public int getIntrinsicWidth() {
        return this.mGradientState.mWidth;
    }

    public int getIntrinsicHeight() {
        return this.mGradientState.mHeight;
    }

    public Insets getOpticalInsets() {
        return this.mGradientState.mOpticalInsets;
    }

    public ConstantState getConstantState() {
        this.mGradientState.mChangingConfigurations = getChangingConfigurations();
        return this.mGradientState;
    }

    private boolean isOpaqueForState() {
        if (this.mGradientState.mStrokeWidth >= 0) {
            Paint paint = this.mStrokePaint;
            if (!(paint == null || isOpaque(paint.getColor()))) {
                return false;
            }
        }
        if (this.mGradientState.mGradientColors != null || isOpaque(this.mFillPaint.getColor())) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0074  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0040  */
    /* JADX WARNING: Missing block: B:7:0x001f, code skipped:
            if (r2.getAlpha() != r9.mFillPaint.getAlpha()) goto L_0x0023;
     */
    public void getOutline(android.graphics.Outline r10) {
        /*
        r9 = this;
        r0 = r9.mGradientState;
        r1 = r9.getBounds();
        r2 = r0.mOpaqueOverShape;
        r3 = 1;
        if (r2 == 0) goto L_0x0023;
    L_0x000b:
        r2 = r9.mGradientState;
        r2 = r2.mStrokeWidth;
        if (r2 <= 0) goto L_0x0021;
    L_0x0011:
        r2 = r9.mStrokePaint;
        if (r2 == 0) goto L_0x0021;
    L_0x0015:
        r2 = r2.getAlpha();
        r4 = r9.mFillPaint;
        r4 = r4.getAlpha();
        if (r2 != r4) goto L_0x0023;
    L_0x0021:
        r2 = r3;
        goto L_0x0024;
    L_0x0023:
        r2 = 0;
    L_0x0024:
        r4 = 0;
        if (r2 == 0) goto L_0x0036;
    L_0x0027:
        r5 = r9.mFillPaint;
        r5 = r5.getAlpha();
        r5 = r9.modulateAlpha(r5);
        r5 = (float) r5;
        r6 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r5 = r5 / r6;
        goto L_0x0037;
    L_0x0036:
        r5 = r4;
    L_0x0037:
        r10.setAlpha(r5);
        r5 = r0.mShape;
        r6 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        if (r5 == 0) goto L_0x0074;
    L_0x0040:
        if (r5 == r3) goto L_0x0070;
    L_0x0042:
        r3 = 2;
        if (r5 == r3) goto L_0x0046;
    L_0x0045:
        return;
    L_0x0046:
        r3 = r9.mStrokePaint;
        if (r3 != 0) goto L_0x004e;
    L_0x004a:
        r3 = 953267991; // 0x38d1b717 float:1.0E-4 double:4.709769656E-315;
        goto L_0x0053;
    L_0x004e:
        r3 = r3.getStrokeWidth();
        r3 = r3 * r6;
    L_0x0053:
        r4 = r1.centerY();
        r4 = (float) r4;
        r5 = r4 - r3;
        r5 = (double) r5;
        r5 = java.lang.Math.floor(r5);
        r5 = (int) r5;
        r6 = r4 + r3;
        r6 = (double) r6;
        r6 = java.lang.Math.ceil(r6);
        r6 = (int) r6;
        r7 = r1.left;
        r8 = r1.right;
        r10.setRect(r7, r5, r8, r6);
        return;
    L_0x0070:
        r10.setOval(r1);
        return;
    L_0x0074:
        r3 = r0.mRadiusArray;
        if (r3 == 0) goto L_0x0081;
    L_0x0078:
        r9.buildPathIfDirty();
        r3 = r9.mPath;
        r10.setConvexPath(r3);
        return;
    L_0x0081:
        r3 = 0;
        r5 = r0.mRadius;
        r4 = (r5 > r4 ? 1 : (r5 == r4 ? 0 : -1));
        if (r4 <= 0) goto L_0x009c;
    L_0x0088:
        r4 = r0.mRadius;
        r5 = r1.width();
        r7 = r1.height();
        r5 = java.lang.Math.min(r5, r7);
        r5 = (float) r5;
        r5 = r5 * r6;
        r3 = java.lang.Math.min(r4, r5);
    L_0x009c:
        r10.setRoundRect(r1, r3);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.GradientDrawable.getOutline(android.graphics.Outline):void");
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mGradientState = new GradientState(this.mGradientState, null);
            updateLocalState(null);
            this.mMutated = true;
        }
        return this;
    }

    public void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    static boolean isOpaque(int color) {
        return ((color >> 24) & 255) == 255;
    }

    private GradientDrawable(GradientState state, Resources res) {
        this.mFillPaint = new Paint(1);
        this.mAlpha = 255;
        this.mPath = new Path();
        this.mRect = new RectF();
        this.mPathIsDirty = true;
        this.mGradientState = state;
        updateLocalState(res);
    }

    private void updateLocalState(Resources res) {
        GradientState state = this.mGradientState;
        if (state.mSolidColors != null) {
            this.mFillPaint.setColor(state.mSolidColors.getColorForState(getState(), 0));
        } else if (state.mGradientColors == null) {
            this.mFillPaint.setColor(0);
        } else {
            this.mFillPaint.setColor(-16777216);
        }
        this.mPadding = state.mPadding;
        if (state.mStrokeWidth >= 0) {
            this.mStrokePaint = new Paint(1);
            this.mStrokePaint.setStyle(Style.STROKE);
            this.mStrokePaint.setStrokeWidth((float) state.mStrokeWidth);
            if (state.mStrokeColors != null) {
                this.mStrokePaint.setColor(state.mStrokeColors.getColorForState(getState(), 0));
            }
            if (state.mStrokeDashWidth != 0.0f) {
                this.mStrokePaint.setPathEffect(new DashPathEffect(new float[]{state.mStrokeDashWidth, state.mStrokeDashGap}, 0.0f));
            }
        }
        this.mBlendModeColorFilter = updateBlendModeFilter(this.mBlendModeColorFilter, state.mTint, state.mBlendMode);
        this.mGradientIsDirty = true;
        state.computeOpacity();
    }
}
