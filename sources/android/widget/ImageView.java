package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.content.ContentResolver.OpenResourceIdResult;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewHierarchyEncoder;
import android.view.accessibility.AccessibilityEvent;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.R;
import java.io.IOException;

@RemoteView
public class ImageView extends View {
    private static final String LOG_TAG = "ImageView";
    private static boolean sCompatAdjustViewBounds;
    private static boolean sCompatDone;
    private static boolean sCompatDrawableVisibilityDispatch;
    private static boolean sCompatUseCorrectStreamDensity;
    private static final ScaleToFit[] sS2FArray = new ScaleToFit[]{ScaleToFit.FILL, ScaleToFit.START, ScaleToFit.CENTER, ScaleToFit.END};
    private static final ScaleType[] sScaleTypeArray = new ScaleType[]{ScaleType.MATRIX, ScaleType.FIT_XY, ScaleType.FIT_START, ScaleType.FIT_CENTER, ScaleType.FIT_END, ScaleType.CENTER, ScaleType.CENTER_CROP, ScaleType.CENTER_INSIDE};
    @UnsupportedAppUsage
    private boolean mAdjustViewBounds;
    @UnsupportedAppUsage
    private int mAlpha;
    private int mBaseline;
    private boolean mBaselineAlignBottom;
    private ColorFilter mColorFilter;
    @UnsupportedAppUsage
    private boolean mCropToPadding;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124051687)
    private Matrix mDrawMatrix;
    @UnsupportedAppUsage
    private Drawable mDrawable;
    private BlendMode mDrawableBlendMode;
    @UnsupportedAppUsage
    private int mDrawableHeight;
    private ColorStateList mDrawableTintList;
    @UnsupportedAppUsage
    private int mDrawableWidth;
    private boolean mHasAlpha;
    private boolean mHasColorFilter;
    private boolean mHasDrawableBlendMode;
    private boolean mHasDrawableTint;
    private boolean mHasXfermode;
    private boolean mHaveFrame;
    private int mLevel;
    private Matrix mMatrix;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mMaxHeight;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mMaxWidth;
    private boolean mMergeState;
    @UnsupportedAppUsage
    private BitmapDrawable mRecycleableBitmapDrawable;
    @UnsupportedAppUsage
    private int mResource;
    private ScaleType mScaleType;
    private int[] mState;
    private final RectF mTempDst;
    private final RectF mTempSrc;
    @UnsupportedAppUsage
    private Uri mUri;
    private final int mViewAlphaScale;
    private Xfermode mXfermode;

    private class ImageDrawableCallback implements Runnable {
        private final Drawable drawable;
        private final int resource;
        private final Uri uri;

        ImageDrawableCallback(Drawable drawable, Uri uri, int resource) {
            this.drawable = drawable;
            this.uri = uri;
            this.resource = resource;
        }

        public void run() {
            ImageView.this.setImageDrawable(this.drawable);
            ImageView.this.mUri = this.uri;
            ImageView.this.mResource = this.resource;
        }
    }

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<ImageView> {
        private int mAdjustViewBoundsId;
        private int mBaselineAlignBottomId;
        private int mBaselineId;
        private int mBlendModeId;
        private int mCropToPaddingId;
        private int mMaxHeightId;
        private int mMaxWidthId;
        private boolean mPropertiesMapped = false;
        private int mScaleTypeId;
        private int mSrcId;
        private int mTintId;
        private int mTintModeId;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mAdjustViewBoundsId = propertyMapper.mapBoolean("adjustViewBounds", 16843038);
            this.mBaselineId = propertyMapper.mapInt("baseline", 16843548);
            this.mBaselineAlignBottomId = propertyMapper.mapBoolean("baselineAlignBottom", 16843042);
            this.mBlendModeId = propertyMapper.mapObject("blendMode", 9);
            this.mCropToPaddingId = propertyMapper.mapBoolean("cropToPadding", 16843043);
            this.mMaxHeightId = propertyMapper.mapInt("maxHeight", 16843040);
            this.mMaxWidthId = propertyMapper.mapInt("maxWidth", 16843039);
            this.mScaleTypeId = propertyMapper.mapObject("scaleType", 16843037);
            this.mSrcId = propertyMapper.mapObject("src", 16843033);
            this.mTintId = propertyMapper.mapObject("tint", 16843041);
            this.mTintModeId = propertyMapper.mapObject("tintMode", 16843771);
            this.mPropertiesMapped = true;
        }

        public void readProperties(ImageView node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readBoolean(this.mAdjustViewBoundsId, node.getAdjustViewBounds());
                propertyReader.readInt(this.mBaselineId, node.getBaseline());
                propertyReader.readBoolean(this.mBaselineAlignBottomId, node.getBaselineAlignBottom());
                propertyReader.readObject(this.mBlendModeId, node.getImageTintBlendMode());
                propertyReader.readBoolean(this.mCropToPaddingId, node.getCropToPadding());
                propertyReader.readInt(this.mMaxHeightId, node.getMaxHeight());
                propertyReader.readInt(this.mMaxWidthId, node.getMaxWidth());
                propertyReader.readObject(this.mScaleTypeId, node.getScaleType());
                propertyReader.readObject(this.mSrcId, node.getDrawable());
                propertyReader.readObject(this.mTintId, node.getImageTintList());
                propertyReader.readObject(this.mTintModeId, node.getImageTintMode());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    public enum ScaleType {
        MATRIX(0),
        FIT_XY(1),
        FIT_START(2),
        FIT_CENTER(3),
        FIT_END(4),
        CENTER(5),
        CENTER_CROP(6),
        CENTER_INSIDE(7);
        
        final int nativeInt;

        private ScaleType(int ni) {
            this.nativeInt = ni;
        }
    }

    public ImageView(Context context) {
        super(context);
        this.mResource = 0;
        this.mHaveFrame = false;
        this.mAdjustViewBounds = false;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mColorFilter = null;
        this.mHasColorFilter = false;
        this.mHasXfermode = false;
        this.mAlpha = 255;
        this.mHasAlpha = false;
        this.mViewAlphaScale = 256;
        this.mDrawable = null;
        this.mRecycleableBitmapDrawable = null;
        this.mDrawableTintList = null;
        this.mDrawableBlendMode = null;
        this.mHasDrawableTint = false;
        this.mHasDrawableBlendMode = false;
        this.mState = null;
        this.mMergeState = false;
        this.mLevel = 0;
        this.mDrawMatrix = null;
        this.mTempSrc = new RectF();
        this.mTempDst = new RectF();
        this.mBaseline = -1;
        this.mBaselineAlignBottom = false;
        initImageView();
    }

    public ImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mResource = 0;
        this.mHaveFrame = false;
        this.mAdjustViewBounds = false;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mColorFilter = null;
        this.mHasColorFilter = false;
        this.mHasXfermode = false;
        this.mAlpha = 255;
        this.mHasAlpha = false;
        this.mViewAlphaScale = 256;
        this.mDrawable = null;
        this.mRecycleableBitmapDrawable = null;
        this.mDrawableTintList = null;
        this.mDrawableBlendMode = null;
        this.mHasDrawableTint = false;
        this.mHasDrawableBlendMode = false;
        this.mState = null;
        this.mMergeState = false;
        this.mLevel = 0;
        this.mDrawMatrix = null;
        this.mTempSrc = new RectF();
        this.mTempDst = new RectF();
        this.mBaseline = -1;
        this.mBaselineAlignBottom = false;
        initImageView();
        if (getImportantForAutofill() == 0) {
            setImportantForAutofill(2);
        }
        int i = defStyleRes;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageView, defStyleAttr, i);
        TypedArray a2 = a;
        saveAttributeDataForStyleable(context, R.styleable.ImageView, attrs, a, defStyleAttr, i);
        Drawable d = a2.getDrawable(0);
        if (d != null) {
            setImageDrawable(d);
        }
        this.mBaselineAlignBottom = a2.getBoolean(6, false);
        this.mBaseline = a2.getDimensionPixelSize(8, -1);
        setAdjustViewBounds(a2.getBoolean(2, false));
        setMaxWidth(a2.getDimensionPixelSize(3, Integer.MAX_VALUE));
        setMaxHeight(a2.getDimensionPixelSize(4, Integer.MAX_VALUE));
        int index = a2.getInt(1, -1);
        if (index >= 0) {
            setScaleType(sScaleTypeArray[index]);
        }
        if (a2.hasValue(5)) {
            this.mDrawableTintList = a2.getColorStateList(5);
            this.mHasDrawableTint = true;
            this.mDrawableBlendMode = BlendMode.SRC_ATOP;
            this.mHasDrawableBlendMode = true;
        }
        if (a2.hasValue(9)) {
            this.mDrawableBlendMode = Drawable.parseBlendMode(a2.getInt(9, -1), this.mDrawableBlendMode);
            this.mHasDrawableBlendMode = true;
        }
        applyImageTint();
        int alpha = a2.getInt(10, 255);
        if (alpha != 255) {
            setImageAlpha(alpha);
        }
        this.mCropToPadding = a2.getBoolean(7, false);
        a2.recycle();
    }

    private void initImageView() {
        this.mMatrix = new Matrix();
        this.mScaleType = ScaleType.FIT_CENTER;
        if (!sCompatDone) {
            int targetSdkVersion = this.mContext.getApplicationInfo().targetSdkVersion;
            boolean z = false;
            sCompatAdjustViewBounds = targetSdkVersion <= 17;
            sCompatUseCorrectStreamDensity = targetSdkVersion > 23;
            if (targetSdkVersion < 24) {
                z = true;
            }
            sCompatDrawableVisibilityDispatch = z;
            sCompatDone = true;
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean verifyDrawable(Drawable dr) {
        return this.mDrawable == dr || super.verifyDrawable(dr);
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    public void invalidateDrawable(Drawable dr) {
        if (dr == this.mDrawable) {
            if (dr != null) {
                int w = dr.getIntrinsicWidth();
                int h = dr.getIntrinsicHeight();
                if (!(w == this.mDrawableWidth && h == this.mDrawableHeight)) {
                    this.mDrawableWidth = w;
                    this.mDrawableHeight = h;
                    configureBounds();
                }
            }
            invalidate();
            return;
        }
        super.invalidateDrawable(dr);
    }

    public boolean hasOverlappingRendering() {
        return (getBackground() == null || getBackground().getCurrent() == null) ? false : true;
    }

    public void onPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        super.onPopulateAccessibilityEventInternal(event);
        CharSequence contentDescription = getContentDescription();
        if (!TextUtils.isEmpty(contentDescription)) {
            event.getText().add(contentDescription);
        }
    }

    public boolean getAdjustViewBounds() {
        return this.mAdjustViewBounds;
    }

    @RemotableViewMethod
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        this.mAdjustViewBounds = adjustViewBounds;
        if (adjustViewBounds) {
            setScaleType(ScaleType.FIT_CENTER);
        }
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    @RemotableViewMethod
    public void setMaxWidth(int maxWidth) {
        this.mMaxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    @RemotableViewMethod
    public void setMaxHeight(int maxHeight) {
        this.mMaxHeight = maxHeight;
    }

    public Drawable getDrawable() {
        if (this.mDrawable == this.mRecycleableBitmapDrawable) {
            this.mRecycleableBitmapDrawable = null;
        }
        return this.mDrawable;
    }

    @RemotableViewMethod(asyncImpl = "setImageResourceAsync")
    public void setImageResource(int resId) {
        int oldWidth = this.mDrawableWidth;
        int oldHeight = this.mDrawableHeight;
        updateDrawable(null);
        this.mResource = resId;
        this.mUri = null;
        resolveUri();
        if (!(oldWidth == this.mDrawableWidth && oldHeight == this.mDrawableHeight)) {
            requestLayout();
        }
        invalidate();
    }

    @UnsupportedAppUsage
    public Runnable setImageResourceAsync(int resId) {
        Drawable d = null;
        if (resId != 0) {
            try {
                d = getContext().getDrawable(resId);
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to find resource: ");
                stringBuilder.append(resId);
                Log.w(LOG_TAG, stringBuilder.toString(), e);
                resId = 0;
            }
        }
        return new ImageDrawableCallback(d, null, resId);
    }

    @RemotableViewMethod(asyncImpl = "setImageURIAsync")
    public void setImageURI(Uri uri) {
        if (this.mResource == 0) {
            Uri uri2 = this.mUri;
            if (uri2 == uri) {
                return;
            }
            if (!(uri == null || uri2 == null || !uri.equals(uri2))) {
                return;
            }
        }
        updateDrawable(null);
        this.mResource = 0;
        this.mUri = uri;
        int oldWidth = this.mDrawableWidth;
        int oldHeight = this.mDrawableHeight;
        resolveUri();
        if (!(oldWidth == this.mDrawableWidth && oldHeight == this.mDrawableHeight)) {
            requestLayout();
        }
        invalidate();
    }

    @UnsupportedAppUsage
    public Runnable setImageURIAsync(Uri uri) {
        Runnable runnable = null;
        if (this.mResource == 0) {
            Uri uri2 = this.mUri;
            if (uri2 == uri || !(uri == null || uri2 == null || !uri.equals(uri2))) {
                return null;
            }
        }
        if (uri != null) {
            runnable = getDrawableFromUri(uri);
        }
        Runnable d = runnable;
        if (d == null) {
            uri = null;
        }
        return new ImageDrawableCallback(d, uri, 0);
    }

    public void setImageDrawable(Drawable drawable) {
        if (this.mDrawable != drawable) {
            this.mResource = 0;
            this.mUri = null;
            int oldWidth = this.mDrawableWidth;
            int oldHeight = this.mDrawableHeight;
            updateDrawable(drawable);
            if (!(oldWidth == this.mDrawableWidth && oldHeight == this.mDrawableHeight)) {
                requestLayout();
            }
            invalidate();
        }
    }

    @RemotableViewMethod(asyncImpl = "setImageIconAsync")
    public void setImageIcon(Icon icon) {
        setImageDrawable(icon == null ? null : icon.loadDrawable(this.mContext));
    }

    public Runnable setImageIconAsync(Icon icon) {
        return new ImageDrawableCallback(icon == null ? null : icon.loadDrawable(this.mContext), null, 0);
    }

    public void setImageTintList(ColorStateList tint) {
        this.mDrawableTintList = tint;
        this.mHasDrawableTint = true;
        applyImageTint();
    }

    public ColorStateList getImageTintList() {
        return this.mDrawableTintList;
    }

    public void setImageTintMode(Mode tintMode) {
        setImageTintBlendMode(tintMode != null ? BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    public void setImageTintBlendMode(BlendMode blendMode) {
        this.mDrawableBlendMode = blendMode;
        this.mHasDrawableBlendMode = true;
        applyImageTint();
    }

    public Mode getImageTintMode() {
        BlendMode blendMode = this.mDrawableBlendMode;
        return blendMode != null ? BlendMode.blendModeToPorterDuffMode(blendMode) : null;
    }

    public BlendMode getImageTintBlendMode() {
        return this.mDrawableBlendMode;
    }

    private void applyImageTint() {
        if (this.mDrawable == null) {
            return;
        }
        if (this.mHasDrawableTint || this.mHasDrawableBlendMode) {
            this.mDrawable = this.mDrawable.mutate();
            if (this.mHasDrawableTint) {
                this.mDrawable.setTintList(this.mDrawableTintList);
            }
            if (this.mHasDrawableBlendMode) {
                this.mDrawable.setTintBlendMode(this.mDrawableBlendMode);
            }
            if (this.mDrawable.isStateful()) {
                this.mDrawable.setState(getDrawableState());
            }
        }
    }

    @RemotableViewMethod
    public void setImageBitmap(Bitmap bm) {
        this.mDrawable = null;
        BitmapDrawable bitmapDrawable = this.mRecycleableBitmapDrawable;
        if (bitmapDrawable == null) {
            this.mRecycleableBitmapDrawable = new BitmapDrawable(this.mContext.getResources(), bm);
        } else {
            bitmapDrawable.setBitmap(bm);
        }
        setImageDrawable(this.mRecycleableBitmapDrawable);
    }

    public void setImageState(int[] state, boolean merge) {
        this.mState = state;
        this.mMergeState = merge;
        if (this.mDrawable != null) {
            refreshDrawableState();
            resizeFromDrawable();
        }
    }

    public void setSelected(boolean selected) {
        super.setSelected(selected);
        resizeFromDrawable();
    }

    @RemotableViewMethod
    public void setImageLevel(int level) {
        this.mLevel = level;
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.setLevel(level);
            resizeFromDrawable();
        }
    }

    public void setScaleType(ScaleType scaleType) {
        if (scaleType == null) {
            throw new NullPointerException();
        } else if (this.mScaleType != scaleType) {
            this.mScaleType = scaleType;
            requestLayout();
            invalidate();
        }
    }

    public ScaleType getScaleType() {
        return this.mScaleType;
    }

    public Matrix getImageMatrix() {
        Matrix matrix = this.mDrawMatrix;
        if (matrix == null) {
            return new Matrix(Matrix.IDENTITY_MATRIX);
        }
        return matrix;
    }

    public void setImageMatrix(Matrix matrix) {
        Object matrix2;
        if (matrix2 != null && matrix2.isIdentity()) {
            matrix2 = null;
        }
        if ((matrix2 == null && !this.mMatrix.isIdentity()) || (matrix2 != null && !this.mMatrix.equals(matrix2))) {
            this.mMatrix.set(matrix2);
            configureBounds();
            invalidate();
        }
    }

    public boolean getCropToPadding() {
        return this.mCropToPadding;
    }

    public void setCropToPadding(boolean cropToPadding) {
        if (this.mCropToPadding != cropToPadding) {
            this.mCropToPadding = cropToPadding;
            requestLayout();
            invalidate();
        }
    }

    @UnsupportedAppUsage
    private void resolveUri() {
        if (this.mDrawable == null && getResources() != null) {
            Drawable d = null;
            int i = this.mResource;
            String str = LOG_TAG;
            if (i != 0) {
                try {
                    d = this.mContext.getDrawable(this.mResource);
                } catch (Exception e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to find resource: ");
                    stringBuilder.append(this.mResource);
                    Log.w(str, stringBuilder.toString(), e);
                    this.mResource = 0;
                }
            } else {
                Uri uri = this.mUri;
                if (uri != null) {
                    d = getDrawableFromUri(uri);
                    if (d == null) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("resolveUri failed on bad bitmap uri: ");
                        stringBuilder2.append(this.mUri);
                        Log.w(str, stringBuilder2.toString());
                        this.mUri = null;
                    }
                } else {
                    return;
                }
            }
            updateDrawable(d);
        }
    }

    private Drawable getDrawableFromUri(Uri uri) {
        StringBuilder stringBuilder;
        String scheme = uri.getScheme();
        boolean equals = ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme);
        Drawable drawable = null;
        String str = "Unable to open content: ";
        String str2 = LOG_TAG;
        if (equals) {
            try {
                OpenResourceIdResult r = this.mContext.getContentResolver().getResourceId(uri);
                drawable = r.r.getDrawable(r.id, this.mContext.getTheme());
                return drawable;
            } catch (Exception e) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(uri);
                Log.w(str2, stringBuilder.toString(), e);
                return drawable;
            }
        } else if (!"content".equals(scheme) && !ContentResolver.SCHEME_FILE.equals(scheme)) {
            return Drawable.createFromPath(uri.toString());
        } else {
            try {
                return ImageDecoder.decodeDrawable(ImageDecoder.createSource(this.mContext.getContentResolver(), uri, sCompatUseCorrectStreamDensity ? getResources() : null), -$$Lambda$ImageView$GWf2-Z-LHjSbTbrF-I3WzfR0LeM.INSTANCE);
            } catch (IOException e2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(uri);
                Log.w(str2, stringBuilder.toString(), e2);
                return drawable;
            }
        }
    }

    public int[] onCreateDrawableState(int extraSpace) {
        int[] iArr = this.mState;
        if (iArr == null) {
            return super.onCreateDrawableState(extraSpace);
        }
        if (this.mMergeState) {
            return View.mergeDrawableStates(super.onCreateDrawableState(iArr.length + extraSpace), this.mState);
        }
        return iArr;
    }

    @UnsupportedAppUsage
    private void updateDrawable(Drawable d) {
        Drawable drawable = this.mRecycleableBitmapDrawable;
        if (!(d == drawable || drawable == null)) {
            drawable.setBitmap(null);
        }
        boolean sameDrawable = false;
        Drawable drawable2 = this.mDrawable;
        boolean z = false;
        if (drawable2 != null) {
            sameDrawable = drawable2 == d;
            this.mDrawable.setCallback(null);
            unscheduleDrawable(this.mDrawable);
            if (!(sCompatDrawableVisibilityDispatch || sameDrawable || !isAttachedToWindow())) {
                this.mDrawable.setVisible(false, false);
            }
        }
        this.mDrawable = d;
        if (d != null) {
            d.setCallback(this);
            d.setLayoutDirection(getLayoutDirection());
            if (d.isStateful()) {
                d.setState(getDrawableState());
            }
            if (!sameDrawable || sCompatDrawableVisibilityDispatch) {
                if (sCompatDrawableVisibilityDispatch) {
                    if (getVisibility() == 0) {
                        z = true;
                    }
                } else if (isAttachedToWindow() && getWindowVisibility() == 0 && isShown()) {
                    z = true;
                }
                d.setVisible(z, true);
            }
            d.setLevel(this.mLevel);
            this.mDrawableWidth = d.getIntrinsicWidth();
            this.mDrawableHeight = d.getIntrinsicHeight();
            applyImageTint();
            applyColorFilter();
            applyAlpha();
            applyXfermode();
            configureBounds();
            return;
        }
        this.mDrawableHeight = -1;
        this.mDrawableWidth = -1;
    }

    @UnsupportedAppUsage
    private void resizeFromDrawable() {
        Drawable d = this.mDrawable;
        if (d != null) {
            int w = d.getIntrinsicWidth();
            if (w < 0) {
                w = this.mDrawableWidth;
            }
            int h = d.getIntrinsicHeight();
            if (h < 0) {
                h = this.mDrawableHeight;
            }
            if (w != this.mDrawableWidth || h != this.mDrawableHeight) {
                this.mDrawableWidth = w;
                this.mDrawableHeight = h;
                requestLayout();
            }
        }
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.setLayoutDirection(layoutDirection);
        }
    }

    @UnsupportedAppUsage
    private static ScaleToFit scaleTypeToScaleToFit(ScaleType st) {
        return sS2FArray[st.nativeInt - 1];
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int h;
        int w;
        int widthSize;
        int heightSize;
        int i = widthMeasureSpec;
        int i2 = heightMeasureSpec;
        resolveUri();
        float desiredAspect = 0.0f;
        boolean resizeWidth = false;
        boolean resizeHeight = false;
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if (this.mDrawable == null) {
            this.mDrawableWidth = -1;
            this.mDrawableHeight = -1;
            h = 0;
            w = 0;
        } else {
            w = this.mDrawableWidth;
            h = this.mDrawableHeight;
            if (w <= 0) {
                w = 1;
            }
            if (h <= 0) {
                h = 1;
            }
            if (this.mAdjustViewBounds) {
                boolean z = true;
                resizeWidth = widthSpecMode != 1073741824;
                if (heightSpecMode == 1073741824) {
                    z = false;
                }
                resizeHeight = z;
                desiredAspect = ((float) w) / ((float) h);
            }
        }
        int pleft = this.mPaddingLeft;
        int pright = this.mPaddingRight;
        int ptop = this.mPaddingTop;
        int pbottom = this.mPaddingBottom;
        if (resizeWidth || resizeHeight) {
            widthSize = resolveAdjustedSize((w + pleft) + pright, this.mMaxWidth, i);
            heightSize = resolveAdjustedSize((h + ptop) + pbottom, this.mMaxHeight, i2);
            if (desiredAspect != 0.0f) {
                float actualAspect = ((float) ((widthSize - pleft) - pright)) / ((float) ((heightSize - ptop) - pbottom));
                if (((double) Math.abs(actualAspect - desiredAspect)) > 1.0E-7d) {
                    boolean done;
                    if (resizeWidth) {
                        heightSpecMode = (((int) (((float) ((heightSize - ptop) - pbottom)) * desiredAspect)) + pleft) + pright;
                        if (resizeHeight || sCompatAdjustViewBounds) {
                            done = false;
                        } else {
                            done = false;
                            widthSize = resolveAdjustedSize(heightSpecMode, this.mMaxWidth, i);
                        }
                        if (heightSpecMode <= widthSize) {
                            widthSize = heightSpecMode;
                            done = true;
                        }
                    } else {
                        done = false;
                    }
                    if (!done && resizeHeight) {
                        widthSpecMode = (((int) (((float) ((widthSize - pleft) - pright)) / desiredAspect)) + ptop) + pbottom;
                        if (!resizeWidth && sCompatAdjustViewBounds == 0) {
                            heightSize = resolveAdjustedSize(widthSpecMode, this.mMaxHeight, i2);
                        }
                        if (widthSpecMode <= heightSize) {
                            heightSize = widthSpecMode;
                        }
                    }
                }
            }
        } else {
            h += ptop + pbottom;
            w = Math.max(w + (pleft + pright), getSuggestedMinimumWidth());
            h = Math.max(h, getSuggestedMinimumHeight());
            widthSize = View.resolveSizeAndState(w, i, 0);
            heightSize = View.resolveSizeAndState(h, i2, 0);
            int i3 = widthSpecMode;
            int i4 = heightSpecMode;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    private int resolveAdjustedSize(int desiredSize, int maxSize, int measureSpec) {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == Integer.MIN_VALUE) {
            return Math.min(Math.min(desiredSize, specSize), maxSize);
        }
        if (specMode == 0) {
            return Math.min(desiredSize, maxSize);
        }
        if (specMode != 1073741824) {
            return result;
        }
        return specSize;
    }

    /* Access modifiers changed, original: protected */
    public boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
        this.mHaveFrame = true;
        configureBounds();
        return changed;
    }

    private void configureBounds() {
        if (this.mDrawable != null && this.mHaveFrame) {
            int dwidth = this.mDrawableWidth;
            int dheight = this.mDrawableHeight;
            int vwidth = (getWidth() - this.mPaddingLeft) - this.mPaddingRight;
            int vheight = (getHeight() - this.mPaddingTop) - this.mPaddingBottom;
            boolean fits = (dwidth < 0 || vwidth == dwidth) && (dheight < 0 || vheight == dheight);
            if (dwidth <= 0 || dheight <= 0 || ScaleType.FIT_XY == this.mScaleType) {
                this.mDrawable.setBounds(0, 0, vwidth, vheight);
                this.mDrawMatrix = null;
            } else {
                this.mDrawable.setBounds(0, 0, dwidth, dheight);
                float dx;
                float dy;
                if (ScaleType.MATRIX == this.mScaleType) {
                    if (this.mMatrix.isIdentity()) {
                        this.mDrawMatrix = null;
                    } else {
                        this.mDrawMatrix = this.mMatrix;
                    }
                } else if (fits) {
                    this.mDrawMatrix = null;
                } else if (ScaleType.CENTER == this.mScaleType) {
                    this.mDrawMatrix = this.mMatrix;
                    this.mDrawMatrix.setTranslate((float) Math.round(((float) (vwidth - dwidth)) * 0.5f), (float) Math.round(((float) (vheight - dheight)) * 0.5f));
                } else if (ScaleType.CENTER_CROP == this.mScaleType) {
                    float scale;
                    this.mDrawMatrix = this.mMatrix;
                    dx = 0.0f;
                    dy = 0.0f;
                    if (dwidth * vheight > vwidth * dheight) {
                        scale = ((float) vheight) / ((float) dheight);
                        dx = (((float) vwidth) - (((float) dwidth) * scale)) * 0.5f;
                    } else {
                        scale = ((float) vwidth) / ((float) dwidth);
                        dy = (((float) vheight) - (((float) dheight) * scale)) * 0.5f;
                    }
                    this.mDrawMatrix.setScale(scale, scale);
                    this.mDrawMatrix.postTranslate((float) Math.round(dx), (float) Math.round(dy));
                } else if (ScaleType.CENTER_INSIDE == this.mScaleType) {
                    this.mDrawMatrix = this.mMatrix;
                    if (dwidth > vwidth || dheight > vheight) {
                        dx = Math.min(((float) vwidth) / ((float) dwidth), ((float) vheight) / ((float) dheight));
                    } else {
                        dx = 1.0f;
                    }
                    dy = (float) Math.round((((float) vwidth) - (((float) dwidth) * dx)) * 0.5f);
                    float dy2 = (float) Math.round((((float) vheight) - (((float) dheight) * dx)) * 0.5f);
                    this.mDrawMatrix.setScale(dx, dx);
                    this.mDrawMatrix.postTranslate(dy, dy2);
                } else {
                    this.mTempSrc.set(0.0f, 0.0f, (float) dwidth, (float) dheight);
                    this.mTempDst.set(0.0f, 0.0f, (float) vwidth, (float) vheight);
                    this.mDrawMatrix = this.mMatrix;
                    this.mDrawMatrix.setRectToRect(this.mTempSrc, this.mTempDst, scaleTypeToScaleToFit(this.mScaleType));
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mDrawable;
        if (drawable != null && drawable.isStateful() && drawable.setState(getDrawableState())) {
            invalidateDrawable(drawable);
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.setHotspot(x, y);
        }
    }

    public void animateTransform(Matrix matrix) {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            if (matrix == null) {
                this.mDrawable.setBounds(0, 0, (getWidth() - this.mPaddingLeft) - this.mPaddingRight, (getHeight() - this.mPaddingTop) - this.mPaddingBottom);
                this.mDrawMatrix = null;
            } else {
                drawable.setBounds(0, 0, this.mDrawableWidth, this.mDrawableHeight);
                if (this.mDrawMatrix == null) {
                    this.mDrawMatrix = new Matrix();
                }
                this.mDrawMatrix.set(matrix);
            }
            invalidate();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mDrawable != null && this.mDrawableWidth != 0 && this.mDrawableHeight != 0) {
            if (this.mDrawMatrix == null && this.mPaddingTop == 0 && this.mPaddingLeft == 0) {
                this.mDrawable.draw(canvas);
            } else {
                int saveCount = canvas.getSaveCount();
                canvas.save();
                if (this.mCropToPadding) {
                    int scrollX = this.mScrollX;
                    int scrollY = this.mScrollY;
                    canvas.clipRect(this.mPaddingLeft + scrollX, this.mPaddingTop + scrollY, ((this.mRight + scrollX) - this.mLeft) - this.mPaddingRight, ((this.mBottom + scrollY) - this.mTop) - this.mPaddingBottom);
                }
                canvas.translate((float) this.mPaddingLeft, (float) this.mPaddingTop);
                Matrix matrix = this.mDrawMatrix;
                if (matrix != null) {
                    canvas.concat(matrix);
                }
                this.mDrawable.draw(canvas);
                canvas.restoreToCount(saveCount);
            }
        }
    }

    @ExportedProperty(category = "layout")
    public int getBaseline() {
        if (this.mBaselineAlignBottom) {
            return getMeasuredHeight();
        }
        return this.mBaseline;
    }

    public void setBaseline(int baseline) {
        if (this.mBaseline != baseline) {
            this.mBaseline = baseline;
            requestLayout();
        }
    }

    public void setBaselineAlignBottom(boolean aligned) {
        if (this.mBaselineAlignBottom != aligned) {
            this.mBaselineAlignBottom = aligned;
            requestLayout();
        }
    }

    public boolean getBaselineAlignBottom() {
        return this.mBaselineAlignBottom;
    }

    public final void setColorFilter(int color, Mode mode) {
        setColorFilter(new PorterDuffColorFilter(color, mode));
    }

    @RemotableViewMethod
    public final void setColorFilter(int color) {
        setColorFilter(color, Mode.SRC_ATOP);
    }

    public final void clearColorFilter() {
        setColorFilter(null);
    }

    public final void setXfermode(Xfermode mode) {
        if (this.mXfermode != mode) {
            this.mXfermode = mode;
            this.mHasXfermode = true;
            applyXfermode();
            invalidate();
        }
    }

    public ColorFilter getColorFilter() {
        return this.mColorFilter;
    }

    public void setColorFilter(ColorFilter cf) {
        if (this.mColorFilter != cf) {
            this.mColorFilter = cf;
            this.mHasColorFilter = true;
            applyColorFilter();
            invalidate();
        }
    }

    public int getImageAlpha() {
        return this.mAlpha;
    }

    @RemotableViewMethod
    public void setImageAlpha(int alpha) {
        setAlpha(alpha);
    }

    @RemotableViewMethod
    @Deprecated
    public void setAlpha(int alpha) {
        alpha &= 255;
        if (this.mAlpha != alpha) {
            this.mAlpha = alpha;
            this.mHasAlpha = true;
            applyAlpha();
            invalidate();
        }
    }

    private void applyXfermode() {
        Drawable drawable = this.mDrawable;
        if (drawable != null && this.mHasXfermode) {
            this.mDrawable = drawable.mutate();
            this.mDrawable.setXfermode(this.mXfermode);
        }
    }

    private void applyColorFilter() {
        Drawable drawable = this.mDrawable;
        if (drawable != null && this.mHasColorFilter) {
            this.mDrawable = drawable.mutate();
            this.mDrawable.setColorFilter(this.mColorFilter);
        }
    }

    private void applyAlpha() {
        Drawable drawable = this.mDrawable;
        if (drawable != null && this.mHasAlpha) {
            this.mDrawable = drawable.mutate();
            this.mDrawable.setAlpha((this.mAlpha * 256) >> 8);
        }
    }

    public boolean isOpaque() {
        if (!super.isOpaque()) {
            Drawable drawable = this.mDrawable;
            if (!(drawable != null && this.mXfermode == null && drawable.getOpacity() == -1 && ((this.mAlpha * 256) >> 8) == 255 && isFilledByImage())) {
                return false;
            }
        }
        return true;
    }

    private boolean isFilledByImage() {
        Rect bounds = this.mDrawable;
        boolean z = false;
        if (bounds == null) {
            return false;
        }
        bounds = bounds.getBounds();
        Matrix matrix = this.mDrawMatrix;
        if (matrix == null) {
            if (bounds.left <= 0 && bounds.top <= 0 && bounds.right >= getWidth() && bounds.bottom >= getHeight()) {
                z = true;
            }
            return z;
        } else if (!matrix.rectStaysRect()) {
            return false;
        } else {
            RectF boundsSrc = this.mTempSrc;
            RectF boundsDst = this.mTempDst;
            boundsSrc.set(bounds);
            matrix.mapRect(boundsDst, boundsSrc);
            if (boundsDst.left <= 0.0f && boundsDst.top <= 0.0f && boundsDst.right >= ((float) getWidth()) && boundsDst.bottom >= ((float) getHeight())) {
                z = true;
            }
            return z;
        }
    }

    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
        Drawable drawable = this.mDrawable;
        if (drawable != null && !sCompatDrawableVisibilityDispatch) {
            drawable.setVisible(isVisible, false);
        }
    }

    @RemotableViewMethod
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        Drawable drawable = this.mDrawable;
        if (drawable != null && sCompatDrawableVisibilityDispatch) {
            drawable.setVisible(visibility == 0, false);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Drawable drawable = this.mDrawable;
        if (drawable != null && sCompatDrawableVisibilityDispatch) {
            drawable.setVisible(getVisibility() == 0, false);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Drawable drawable = this.mDrawable;
        if (drawable != null && sCompatDrawableVisibilityDispatch) {
            drawable.setVisible(false, false);
        }
    }

    public CharSequence getAccessibilityClassName() {
        return ImageView.class.getName();
    }

    /* Access modifiers changed, original: protected */
    public void encodeProperties(ViewHierarchyEncoder stream) {
        super.encodeProperties(stream);
        stream.addProperty("layout:baseline", getBaseline());
    }

    public boolean isDefaultFocusHighlightNeeded(Drawable background, Drawable foreground) {
        Drawable drawable = this.mDrawable;
        boolean lackFocusState = (drawable != null && drawable.isStateful() && this.mDrawable.hasFocusStateSpecified()) ? false : true;
        if (super.isDefaultFocusHighlightNeeded(background, foreground) && lackFocusState) {
            return true;
        }
        return false;
    }
}
