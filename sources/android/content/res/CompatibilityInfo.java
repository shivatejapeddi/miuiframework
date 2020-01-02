package android.content.res;

import android.annotation.UnsupportedAppUsage;
import android.content.pm.ApplicationInfo;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;

public class CompatibilityInfo implements Parcelable {
    private static final int ALWAYS_NEEDS_COMPAT = 2;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public static final Creator<CompatibilityInfo> CREATOR = new Creator<CompatibilityInfo>() {
        public CompatibilityInfo createFromParcel(Parcel source) {
            return new CompatibilityInfo(source, null);
        }

        public CompatibilityInfo[] newArray(int size) {
            return new CompatibilityInfo[size];
        }
    };
    @UnsupportedAppUsage
    public static final CompatibilityInfo DEFAULT_COMPATIBILITY_INFO = new CompatibilityInfo() {
    };
    public static final int DEFAULT_NORMAL_SHORT_DIMENSION = 320;
    public static final float MAXIMUM_ASPECT_RATIO = 1.7791667f;
    private static final int NEEDS_COMPAT_RES = 16;
    private static final int NEEDS_SCREEN_COMPAT = 8;
    private static final int NEVER_NEEDS_COMPAT = 4;
    private static final int SCALING_REQUIRED = 1;
    static final String TAG = "CompatibilityInfo";
    public final int applicationDensity;
    public final float applicationInvertedScale;
    @UnsupportedAppUsage
    public final float applicationScale;
    private final int mCompatibilityFlags;

    public class Translator {
        @UnsupportedAppUsage
        public final float applicationInvertedScale;
        @UnsupportedAppUsage
        public final float applicationScale;
        private Rect mContentInsetsBuffer;
        private Region mTouchableAreaBuffer;
        private Rect mVisibleInsetsBuffer;

        Translator(float applicationScale, float applicationInvertedScale) {
            this.mContentInsetsBuffer = null;
            this.mVisibleInsetsBuffer = null;
            this.mTouchableAreaBuffer = null;
            this.applicationScale = applicationScale;
            this.applicationInvertedScale = applicationInvertedScale;
        }

        Translator(CompatibilityInfo this$0) {
            this(this$0.applicationScale, this$0.applicationInvertedScale);
        }

        @UnsupportedAppUsage
        public void translateRectInScreenToAppWinFrame(Rect rect) {
            rect.scale(this.applicationInvertedScale);
        }

        @UnsupportedAppUsage
        public void translateRegionInWindowToScreen(Region transparentRegion) {
            transparentRegion.scale(this.applicationScale);
        }

        @UnsupportedAppUsage
        public void translateCanvas(Canvas canvas) {
            if (this.applicationScale == 1.5f) {
                canvas.translate(0.0026143792f, 0.0026143792f);
            }
            float f = this.applicationScale;
            canvas.scale(f, f);
        }

        @UnsupportedAppUsage
        public void translateEventInScreenToAppWindow(MotionEvent event) {
            event.scale(this.applicationInvertedScale);
        }

        @UnsupportedAppUsage
        public void translateWindowLayout(LayoutParams params) {
            params.scale(this.applicationScale);
        }

        @UnsupportedAppUsage
        public void translateRectInAppWindowToScreen(Rect rect) {
            rect.scale(this.applicationScale);
        }

        @UnsupportedAppUsage
        public void translateRectInScreenToAppWindow(Rect rect) {
            rect.scale(this.applicationInvertedScale);
        }

        public void translatePointInScreenToAppWindow(PointF point) {
            float scale = this.applicationInvertedScale;
            if (scale != 1.0f) {
                point.x *= scale;
                point.y *= scale;
            }
        }

        public void translateLayoutParamsInAppWindowToScreen(LayoutParams params) {
            params.scale(this.applicationScale);
        }

        @UnsupportedAppUsage
        public Rect getTranslatedContentInsets(Rect contentInsets) {
            if (this.mContentInsetsBuffer == null) {
                this.mContentInsetsBuffer = new Rect();
            }
            this.mContentInsetsBuffer.set(contentInsets);
            translateRectInAppWindowToScreen(this.mContentInsetsBuffer);
            return this.mContentInsetsBuffer;
        }

        public Rect getTranslatedVisibleInsets(Rect visibleInsets) {
            if (this.mVisibleInsetsBuffer == null) {
                this.mVisibleInsetsBuffer = new Rect();
            }
            this.mVisibleInsetsBuffer.set(visibleInsets);
            translateRectInAppWindowToScreen(this.mVisibleInsetsBuffer);
            return this.mVisibleInsetsBuffer;
        }

        public Region getTranslatedTouchableArea(Region touchableArea) {
            if (this.mTouchableAreaBuffer == null) {
                this.mTouchableAreaBuffer = new Region();
            }
            this.mTouchableAreaBuffer.set(touchableArea);
            this.mTouchableAreaBuffer.scale(this.applicationScale);
            return this.mTouchableAreaBuffer;
        }
    }

    /* synthetic */ CompatibilityInfo(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    @UnsupportedAppUsage
    public CompatibilityInfo(ApplicationInfo appInfo, int screenLayout, int sw, boolean forceCompat) {
        int compatFlags = 0;
        if (appInfo.targetSdkVersion < 26) {
            compatFlags = 0 | 16;
        }
        int sizeInfo;
        if (appInfo.requiresSmallestWidthDp == 0 && appInfo.compatibleWidthLimitDp == 0 && appInfo.largestWidthLimitDp == 0) {
            sizeInfo = 0;
            boolean anyResizeable = false;
            if ((appInfo.flags & 2048) != 0) {
                sizeInfo = 0 | 8;
                anyResizeable = true;
                if (!forceCompat) {
                    sizeInfo |= 34;
                }
            }
            if ((appInfo.flags & 524288) != 0) {
                anyResizeable = true;
                if (!forceCompat) {
                    sizeInfo |= 34;
                }
            }
            if ((appInfo.flags & 4096) != 0) {
                anyResizeable = true;
                sizeInfo |= 2;
            }
            if (forceCompat) {
                sizeInfo &= -3;
            }
            compatFlags |= 8;
            int i = screenLayout & 15;
            if (i == 3) {
                if ((sizeInfo & 8) != 0) {
                    compatFlags &= -9;
                }
                if ((appInfo.flags & 2048) != 0) {
                    compatFlags |= 4;
                }
            } else if (i == 4) {
                if ((sizeInfo & 32) != 0) {
                    compatFlags &= -9;
                }
                if ((appInfo.flags & 524288) != 0) {
                    compatFlags |= 4;
                }
            }
            if ((268435456 & screenLayout) == 0) {
                compatFlags = (compatFlags & -9) | 4;
            } else if ((sizeInfo & 2) != 0) {
                compatFlags &= -9;
            } else if (!anyResizeable) {
                compatFlags |= 2;
            }
            i = appInfo.getOverrideDensity();
            if ((appInfo.flags & 8192) == 0) {
                this.applicationDensity = 160;
                this.applicationScale = ((float) DisplayMetrics.DENSITY_DEVICE) / 160.0f;
                this.applicationInvertedScale = 1.0f / this.applicationScale;
                compatFlags |= 1;
            } else if (i != 0) {
                this.applicationDensity = i;
                this.applicationScale = ((float) DisplayMetrics.DENSITY_DEVICE) / ((float) this.applicationDensity);
                this.applicationInvertedScale = 1.0f / this.applicationScale;
                compatFlags |= 1;
            } else {
                this.applicationDensity = DisplayMetrics.DENSITY_DEVICE;
                this.applicationScale = 1.0f;
                this.applicationInvertedScale = 1.0f;
            }
        } else {
            int required;
            if (appInfo.requiresSmallestWidthDp != 0) {
                required = appInfo.requiresSmallestWidthDp;
            } else {
                required = appInfo.compatibleWidthLimitDp;
            }
            if (required == 0) {
                required = appInfo.largestWidthLimitDp;
            }
            int compat = appInfo.compatibleWidthLimitDp != 0 ? appInfo.compatibleWidthLimitDp : required;
            if (compat < required) {
                compat = required;
            }
            int largest = appInfo.largestWidthLimitDp;
            if (required > 320) {
                compatFlags |= 4;
            } else if (largest != 0 && sw > largest) {
                compatFlags |= 10;
            } else if (compat >= sw) {
                compatFlags |= 4;
            } else if (forceCompat) {
                compatFlags |= 8;
            }
            sizeInfo = appInfo.getOverrideDensity();
            if (sizeInfo != 0) {
                this.applicationDensity = sizeInfo;
                this.applicationScale = ((float) DisplayMetrics.DENSITY_DEVICE) / ((float) this.applicationDensity);
                this.applicationInvertedScale = 1.0f / this.applicationScale;
                compatFlags |= 1;
            } else {
                this.applicationDensity = DisplayMetrics.DENSITY_DEVICE;
                this.applicationScale = 1.0f;
                this.applicationInvertedScale = 1.0f;
            }
        }
        this.mCompatibilityFlags = compatFlags;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mCompatibilityFlags - ");
        stringBuilder.append(Integer.toHexString(this.mCompatibilityFlags));
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        stringBuilder = new StringBuilder();
        stringBuilder.append("applicationDensity - ");
        stringBuilder.append(this.applicationDensity);
        Log.d(str, stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("applicationScale - ");
        stringBuilder.append(this.applicationScale);
        Log.d(str, stringBuilder.toString());
    }

    private CompatibilityInfo(int compFlags, int dens, float scale, float invertedScale) {
        this.mCompatibilityFlags = compFlags;
        this.applicationDensity = dens;
        this.applicationScale = scale;
        this.applicationInvertedScale = invertedScale;
    }

    @UnsupportedAppUsage
    private CompatibilityInfo() {
        this(4, DisplayMetrics.DENSITY_DEVICE, 1.0f, 1.0f);
    }

    @UnsupportedAppUsage
    public boolean isScalingRequired() {
        return (this.mCompatibilityFlags & 1) != 0;
    }

    @UnsupportedAppUsage
    public boolean supportsScreen() {
        return (this.mCompatibilityFlags & 8) == 0;
    }

    public boolean neverSupportsScreen() {
        return (this.mCompatibilityFlags & 2) != 0;
    }

    public boolean alwaysSupportsScreen() {
        return (this.mCompatibilityFlags & 4) != 0;
    }

    public boolean needsCompatResources() {
        return (this.mCompatibilityFlags & 16) != 0;
    }

    @UnsupportedAppUsage
    public Translator getTranslator() {
        return isScalingRequired() ? new Translator(this) : null;
    }

    public void applyToDisplayMetrics(DisplayMetrics inoutDm) {
        if (supportsScreen()) {
            inoutDm.widthPixels = inoutDm.noncompatWidthPixels;
            inoutDm.heightPixels = inoutDm.noncompatHeightPixels;
        } else {
            computeCompatibleScaling(inoutDm, inoutDm);
        }
        if (isScalingRequired()) {
            float invertedRatio = this.applicationInvertedScale;
            inoutDm.density = inoutDm.noncompatDensity * invertedRatio;
            inoutDm.densityDpi = (int) ((((float) inoutDm.noncompatDensityDpi) * invertedRatio) + 0.5f);
            inoutDm.scaledDensity = inoutDm.noncompatScaledDensity * invertedRatio;
            inoutDm.xdpi = inoutDm.noncompatXdpi * invertedRatio;
            inoutDm.ydpi = inoutDm.noncompatYdpi * invertedRatio;
            inoutDm.widthPixels = (int) ((((float) inoutDm.widthPixels) * invertedRatio) + 0.5f);
            inoutDm.heightPixels = (int) ((((float) inoutDm.heightPixels) * invertedRatio) + 0.5f);
        }
    }

    public void applyToConfiguration(int displayDensity, Configuration inoutConfig) {
        if (!supportsScreen()) {
            inoutConfig.screenLayout = (inoutConfig.screenLayout & -16) | 2;
            inoutConfig.screenWidthDp = inoutConfig.compatScreenWidthDp;
            inoutConfig.screenHeightDp = inoutConfig.compatScreenHeightDp;
            inoutConfig.smallestScreenWidthDp = inoutConfig.compatSmallestScreenWidthDp;
        }
        inoutConfig.densityDpi = displayDensity;
        if (isScalingRequired()) {
            inoutConfig.densityDpi = (int) ((((float) inoutConfig.densityDpi) * this.applicationInvertedScale) + 0.5f);
        }
    }

    @UnsupportedAppUsage
    public static float computeCompatibleScaling(DisplayMetrics dm, DisplayMetrics outDm) {
        int shortSize;
        int longSize;
        int newWidth;
        int newHeight;
        int width = dm.noncompatWidthPixels;
        int height = dm.noncompatHeightPixels;
        if (width < height) {
            shortSize = width;
            longSize = height;
        } else {
            shortSize = height;
            longSize = width;
        }
        int newShortSize = (int) ((dm.density * 320.0f) + 1056964608);
        float aspect = ((float) longSize) / ((float) shortSize);
        if (aspect > 1.7791667f) {
            aspect = 1.7791667f;
        }
        int newLongSize = (int) ((((float) newShortSize) * aspect) + 0.5f);
        if (width < height) {
            newWidth = newShortSize;
            newHeight = newLongSize;
        } else {
            newWidth = newLongSize;
            newHeight = newShortSize;
        }
        float sw = ((float) width) / ((float) newWidth);
        float sh = ((float) height) / ((float) newHeight);
        float scale = sw < sh ? sw : sh;
        if (scale < 1.0f) {
            scale = 1.0f;
        }
        if (outDm != null) {
            outDm.widthPixels = newWidth;
            outDm.heightPixels = newHeight;
        }
        return scale;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        try {
            CompatibilityInfo oc = (CompatibilityInfo) o;
            if (this.mCompatibilityFlags == oc.mCompatibilityFlags && this.applicationDensity == oc.applicationDensity && this.applicationScale == oc.applicationScale && this.applicationInvertedScale == oc.applicationInvertedScale) {
                return true;
            }
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("{");
        sb.append(this.applicationDensity);
        sb.append("dpi");
        if (isScalingRequired()) {
            sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            sb.append(this.applicationScale);
            sb.append("x");
        }
        if (!supportsScreen()) {
            sb.append(" resizing");
        }
        if (neverSupportsScreen()) {
            sb.append(" never-compat");
        }
        if (alwaysSupportsScreen()) {
            sb.append(" always-compat");
        }
        sb.append("}");
        return sb.toString();
    }

    public int hashCode() {
        return (((((((17 * 31) + this.mCompatibilityFlags) * 31) + this.applicationDensity) * 31) + Float.floatToIntBits(this.applicationScale)) * 31) + Float.floatToIntBits(this.applicationInvertedScale);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mCompatibilityFlags);
        dest.writeInt(this.applicationDensity);
        dest.writeFloat(this.applicationScale);
        dest.writeFloat(this.applicationInvertedScale);
    }

    private CompatibilityInfo(Parcel source) {
        this.mCompatibilityFlags = source.readInt();
        this.applicationDensity = source.readInt();
        this.applicationScale = source.readFloat();
        this.applicationInvertedScale = source.readFloat();
    }
}
