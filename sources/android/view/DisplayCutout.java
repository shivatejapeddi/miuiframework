package android.view;

import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.proto.ProtoOutputStream;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DisplayCutout {
    private static final String BOTTOM_MARKER = "@bottom";
    public static final int BOUNDS_POSITION_BOTTOM = 3;
    public static final int BOUNDS_POSITION_LEFT = 0;
    public static final int BOUNDS_POSITION_LENGTH = 4;
    public static final int BOUNDS_POSITION_RIGHT = 2;
    public static final int BOUNDS_POSITION_TOP = 1;
    private static final Object CACHE_LOCK = new Object();
    private static final String DP_MARKER = "@dp";
    public static final String EMULATION_OVERLAY_CATEGORY = "com.android.internal.display_cutout_emulation";
    public static final DisplayCutout NO_CUTOUT;
    private static final Pair<Path, DisplayCutout> NULL_PAIR = new Pair(null, null);
    private static final String RIGHT_MARKER = "@right";
    private static final String TAG = "DisplayCutout";
    private static final Rect ZERO_RECT = new Rect();
    @GuardedBy({"CACHE_LOCK"})
    private static Pair<Path, DisplayCutout> sCachedCutout = NULL_PAIR;
    @GuardedBy({"CACHE_LOCK"})
    private static float sCachedDensity;
    @GuardedBy({"CACHE_LOCK"})
    private static int sCachedDisplayHeight;
    @GuardedBy({"CACHE_LOCK"})
    private static int sCachedDisplayWidth;
    @GuardedBy({"CACHE_LOCK"})
    private static String sCachedSpec;
    private final Bounds mBounds;
    private final Rect mSafeInsets;

    private static class Bounds {
        private final Rect[] mRects;

        private Bounds(Rect left, Rect top, Rect right, Rect bottom, boolean copyArguments) {
            this.mRects = new Rect[4];
            this.mRects[0] = DisplayCutout.getCopyOrRef(left, copyArguments);
            this.mRects[1] = DisplayCutout.getCopyOrRef(top, copyArguments);
            this.mRects[2] = DisplayCutout.getCopyOrRef(right, copyArguments);
            this.mRects[3] = DisplayCutout.getCopyOrRef(bottom, copyArguments);
        }

        private Bounds(Rect[] rects, boolean copyArguments) {
            StringBuilder stringBuilder;
            int i;
            if (rects.length != 4) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("rects must have exactly 4 elements: rects=");
                stringBuilder.append(Arrays.toString(rects));
                throw new IllegalArgumentException(stringBuilder.toString());
            } else if (copyArguments) {
                this.mRects = new Rect[4];
                for (i = 0; i < 4; i++) {
                    this.mRects[i] = new Rect(rects[i]);
                }
            } else {
                i = rects.length;
                int i2 = 0;
                while (i2 < i) {
                    if (rects[i2] != null) {
                        i2++;
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("rects must have non-null elements: rects=");
                        stringBuilder.append(Arrays.toString(rects));
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                }
                this.mRects = rects;
            }
        }

        private boolean isEmpty() {
            for (Rect rect : this.mRects) {
                if (!rect.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        private Rect getRect(int pos) {
            return new Rect(this.mRects[pos]);
        }

        private Rect[] getRects() {
            Rect[] rects = new Rect[4];
            for (int i = 0; i < 4; i++) {
                rects[i] = new Rect(this.mRects[i]);
            }
            return rects;
        }

        public int hashCode() {
            int result = 0;
            for (Rect rect : this.mRects) {
                result = (48271 * result) + rect.hashCode();
            }
            return result;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Bounds)) {
                return false;
            }
            return Arrays.deepEquals(this.mRects, ((Bounds) o).mRects);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Bounds=");
            stringBuilder.append(Arrays.toString(this.mRects));
            return stringBuilder.toString();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface BoundsPosition {
    }

    public static final class ParcelableWrapper implements Parcelable {
        public static final Creator<ParcelableWrapper> CREATOR = new Creator<ParcelableWrapper>() {
            public ParcelableWrapper createFromParcel(Parcel in) {
                return new ParcelableWrapper(ParcelableWrapper.readCutoutFromParcel(in));
            }

            public ParcelableWrapper[] newArray(int size) {
                return new ParcelableWrapper[size];
            }
        };
        private DisplayCutout mInner;

        public ParcelableWrapper() {
            this(DisplayCutout.NO_CUTOUT);
        }

        public ParcelableWrapper(DisplayCutout cutout) {
            this.mInner = cutout;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            writeCutoutToParcel(this.mInner, out, flags);
        }

        public static void writeCutoutToParcel(DisplayCutout cutout, Parcel out, int flags) {
            if (cutout == null) {
                out.writeInt(-1);
            } else if (cutout == DisplayCutout.NO_CUTOUT) {
                out.writeInt(0);
            } else {
                out.writeInt(1);
                out.writeTypedObject(cutout.mSafeInsets, flags);
                out.writeTypedArray(cutout.mBounds.getRects(), flags);
            }
        }

        public void readFromParcel(Parcel in) {
            this.mInner = readCutoutFromParcel(in);
        }

        public static DisplayCutout readCutoutFromParcel(Parcel in) {
            int variant = in.readInt();
            if (variant == -1) {
                return null;
            }
            if (variant == 0) {
                return DisplayCutout.NO_CUTOUT;
            }
            Rect safeInsets = (Rect) in.readTypedObject(Rect.CREATOR);
            Rect[] bounds = new Rect[4];
            in.readTypedArray(bounds, Rect.CREATOR);
            return new DisplayCutout(safeInsets, bounds, false);
        }

        public DisplayCutout get() {
            return this.mInner;
        }

        public void set(ParcelableWrapper cutout) {
            this.mInner = cutout.get();
        }

        public void set(DisplayCutout cutout) {
            this.mInner = cutout;
        }

        public int hashCode() {
            return this.mInner.hashCode();
        }

        public boolean equals(Object o) {
            return (o instanceof ParcelableWrapper) && this.mInner.equals(((ParcelableWrapper) o).mInner);
        }

        public String toString() {
            return String.valueOf(this.mInner);
        }
    }

    static {
        Rect rect = ZERO_RECT;
        NO_CUTOUT = new DisplayCutout(rect, rect, rect, rect, rect, false);
    }

    public DisplayCutout(Insets safeInsets, Rect boundLeft, Rect boundTop, Rect boundRight, Rect boundBottom) {
        this(safeInsets.toRect(), boundLeft, boundTop, boundRight, boundBottom, true);
    }

    @Deprecated
    public DisplayCutout(Rect safeInsets, List<Rect> boundingRects) {
        this(safeInsets, extractBoundsFromList(safeInsets, boundingRects), true);
    }

    private DisplayCutout(Rect safeInsets, Rect boundLeft, Rect boundTop, Rect boundRight, Rect boundBottom, boolean copyArguments) {
        this.mSafeInsets = getCopyOrRef(safeInsets, copyArguments);
        this.mBounds = new Bounds(boundLeft, boundTop, boundRight, boundBottom, copyArguments);
    }

    private DisplayCutout(Rect safeInsets, Rect[] bounds, boolean copyArguments) {
        this.mSafeInsets = getCopyOrRef(safeInsets, copyArguments);
        this.mBounds = new Bounds(bounds, copyArguments);
    }

    private DisplayCutout(Rect safeInsets, Bounds bounds) {
        this.mSafeInsets = safeInsets;
        this.mBounds = bounds;
    }

    private static Rect getCopyOrRef(Rect r, boolean copyArguments) {
        if (r == null) {
            return ZERO_RECT;
        }
        if (copyArguments) {
            return new Rect(r);
        }
        return r;
    }

    public static Rect[] extractBoundsFromList(Rect safeInsets, List<Rect> boundingRects) {
        Rect[] sortedBounds = new Rect[4];
        for (int i = 0; i < sortedBounds.length; i++) {
            sortedBounds[i] = ZERO_RECT;
        }
        if (!(safeInsets == null || boundingRects == null)) {
            for (Rect bound : boundingRects) {
                if (bound.left == 0) {
                    sortedBounds[0] = bound;
                } else if (bound.top == 0) {
                    sortedBounds[1] = bound;
                } else if (safeInsets.right > 0) {
                    sortedBounds[2] = bound;
                } else if (safeInsets.bottom > 0) {
                    sortedBounds[3] = bound;
                }
            }
        }
        return sortedBounds;
    }

    public boolean isBoundsEmpty() {
        return this.mBounds.isEmpty();
    }

    public boolean isEmpty() {
        return this.mSafeInsets.equals(ZERO_RECT);
    }

    public int getSafeInsetTop() {
        return this.mSafeInsets.top;
    }

    public int getSafeInsetBottom() {
        return this.mSafeInsets.bottom;
    }

    public int getSafeInsetLeft() {
        return this.mSafeInsets.left;
    }

    public int getSafeInsetRight() {
        return this.mSafeInsets.right;
    }

    public Rect getSafeInsets() {
        return new Rect(this.mSafeInsets);
    }

    public List<Rect> getBoundingRects() {
        List<Rect> result = new ArrayList();
        for (Rect bound : getBoundingRectsAll()) {
            if (!bound.isEmpty()) {
                result.add(new Rect(bound));
            }
        }
        return result;
    }

    public Rect[] getBoundingRectsAll() {
        return this.mBounds.getRects();
    }

    public Rect getBoundingRectLeft() {
        return this.mBounds.getRect(0);
    }

    public Rect getBoundingRectTop() {
        return this.mBounds.getRect(1);
    }

    public Rect getBoundingRectRight() {
        return this.mBounds.getRect(2);
    }

    public Rect getBoundingRectBottom() {
        return this.mBounds.getRect(3);
    }

    public int hashCode() {
        return (this.mSafeInsets.hashCode() * 48271) + this.mBounds.hashCode();
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof DisplayCutout)) {
            return false;
        }
        DisplayCutout c = (DisplayCutout) o;
        if (!(this.mSafeInsets.equals(c.mSafeInsets) && this.mBounds.equals(c.mBounds))) {
            z = false;
        }
        return z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DisplayCutout{insets=");
        stringBuilder.append(this.mSafeInsets);
        stringBuilder.append(" boundingRect={");
        stringBuilder.append(this.mBounds);
        stringBuilder.append("}}");
        return stringBuilder.toString();
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        this.mSafeInsets.writeToProto(proto, 1146756268033L);
        this.mBounds.getRect(0).writeToProto(proto, 1146756268035L);
        this.mBounds.getRect(1).writeToProto(proto, 1146756268036L);
        this.mBounds.getRect(2).writeToProto(proto, 1146756268037L);
        this.mBounds.getRect(3).writeToProto(proto, 1146756268038L);
        proto.end(token);
    }

    public DisplayCutout inset(int insetLeft, int insetTop, int insetRight, int insetBottom) {
        if ((insetLeft == 0 && insetTop == 0 && insetRight == 0 && insetBottom == 0) || isBoundsEmpty()) {
            return this;
        }
        Rect safeInsets = new Rect(this.mSafeInsets);
        if (insetTop > 0 || safeInsets.top > 0) {
            safeInsets.top = atLeastZero(safeInsets.top - insetTop);
        }
        if (insetBottom > 0 || safeInsets.bottom > 0) {
            safeInsets.bottom = atLeastZero(safeInsets.bottom - insetBottom);
        }
        if (insetLeft > 0 || safeInsets.left > 0) {
            safeInsets.left = atLeastZero(safeInsets.left - insetLeft);
        }
        if (insetRight > 0 || safeInsets.right > 0) {
            safeInsets.right = atLeastZero(safeInsets.right - insetRight);
        }
        if (insetLeft == 0 && insetTop == 0 && this.mSafeInsets.equals(safeInsets)) {
            return this;
        }
        Rect[] bounds = this.mBounds.getRects();
        for (int i = 0; i < bounds.length; i++) {
            if (!bounds[i].equals(ZERO_RECT)) {
                bounds[i].offset(-insetLeft, -insetTop);
            }
        }
        return new DisplayCutout(safeInsets, bounds, false);
    }

    public DisplayCutout replaceSafeInsets(Rect safeInsets) {
        return new DisplayCutout(new Rect(safeInsets), this.mBounds);
    }

    private static int atLeastZero(int value) {
        return value < 0 ? 0 : value;
    }

    @VisibleForTesting
    public static DisplayCutout fromBoundingRect(int left, int top, int right, int bottom, int pos) {
        Rect[] bounds = new Rect[4];
        for (int i = 0; i < 4; i++) {
            Rect rect;
            if (pos == i) {
                rect = new Rect(left, top, right, bottom);
            } else {
                rect = new Rect();
            }
            bounds[i] = rect;
        }
        return new DisplayCutout(ZERO_RECT, bounds, false);
    }

    public static DisplayCutout fromBounds(Rect[] bounds) {
        return new DisplayCutout(ZERO_RECT, bounds, false);
    }

    public static DisplayCutout fromResourcesRectApproximation(Resources res, int displayWidth, int displayHeight) {
        return fromSpec(res.getString(R.string.config_mainBuiltInDisplayCutoutRectApproximation), displayWidth, displayHeight, ((float) DisplayMetrics.DENSITY_DEVICE_STABLE) / 160.0f);
    }

    public static Path pathFromResources(Resources res, int displayWidth, int displayHeight) {
        return (Path) pathAndDisplayCutoutFromSpec(res.getString(R.string.config_mainBuiltInDisplayCutout), displayWidth, displayHeight, ((float) DisplayMetrics.DENSITY_DEVICE_STABLE) / 160.0f).first;
    }

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    public static DisplayCutout fromSpec(String spec, int displayWidth, int displayHeight, float density) {
        return (DisplayCutout) pathAndDisplayCutoutFromSpec(spec, displayWidth, displayHeight, density).second;
    }

    /* JADX WARNING: Missing block: B:22:0x002f, code skipped:
            r0 = r22.trim();
     */
    /* JADX WARNING: Missing block: B:23:0x003a, code skipped:
            if (r0.endsWith(RIGHT_MARKER) == false) goto L_0x0051;
     */
    /* JADX WARNING: Missing block: B:24:0x003c, code skipped:
            r4 = (float) r1;
            r0 = r0.substring(0, r0.length() - RIGHT_MARKER.length()).trim();
     */
    /* JADX WARNING: Missing block: B:25:0x0051, code skipped:
            r4 = ((float) r1) / 2.0f;
     */
    /* JADX WARNING: Missing block: B:26:0x0055, code skipped:
            r6 = r0.endsWith(DP_MARKER);
     */
    /* JADX WARNING: Missing block: B:27:0x005b, code skipped:
            if (r6 == false) goto L_0x006c;
     */
    /* JADX WARNING: Missing block: B:28:0x005d, code skipped:
            r0 = r0.substring(0, r0.length() - DP_MARKER.length());
     */
    /* JADX WARNING: Missing block: B:30:0x0073, code skipped:
            if (r0.contains(BOTTOM_MARKER) == false) goto L_0x008c;
     */
    /* JADX WARNING: Missing block: B:31:0x0075, code skipped:
            r8 = r0.split(BOTTOM_MARKER, 2);
            r0 = r8[0].trim();
            r8 = r8[1].trim();
            r7 = r0;
     */
    /* JADX WARNING: Missing block: B:32:0x008c, code skipped:
            r8 = null;
            r7 = r0;
     */
    /* JADX WARNING: Missing block: B:33:0x008e, code skipped:
            r9 = android.graphics.Region.obtain();
     */
    /* JADX WARNING: Missing block: B:36:0x0096, code skipped:
            r10 = android.util.PathParser.createPathFromPathData(r7);
            r11 = new android.graphics.Matrix();
     */
    /* JADX WARNING: Missing block: B:37:0x009e, code skipped:
            if (r6 == false) goto L_0x00a3;
     */
    /* JADX WARNING: Missing block: B:38:0x00a0, code skipped:
            r11.postScale(r3, r3);
     */
    /* JADX WARNING: Missing block: B:39:0x00a3, code skipped:
            r11.postTranslate(r4, 0.0f);
            r10.transform(r11);
            r12 = new android.graphics.Rect();
            toRectAndAddToRegion(r10, r9, r12);
            r15 = r12.bottom;
     */
    /* JADX WARNING: Missing block: B:40:0x00b5, code skipped:
            if (r8 == null) goto L_0x00e2;
     */
    /* JADX WARNING: Missing block: B:42:?, code skipped:
            r14 = android.util.PathParser.createPathFromPathData(r8);
     */
    /* JADX WARNING: Missing block: B:43:0x00bb, code skipped:
            r11.postTranslate(0.0f, (float) r2);
            r14.transform(r11);
            r10.addPath(r14);
            r0 = new android.graphics.Rect();
            toRectAndAddToRegion(r14, r9, r0);
            r5 = r2 - r0.top;
            r20 = r0;
     */
    /* JADX WARNING: Missing block: B:44:0x00d5, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:45:0x00d6, code skipped:
            android.util.Log.wtf(TAG, "Could not inflate bottom cutout: ", r0);
     */
    /* JADX WARNING: Missing block: B:46:0x00e1, code skipped:
            return NULL_PAIR;
     */
    /* JADX WARNING: Missing block: B:47:0x00e2, code skipped:
            r5 = 0;
            r20 = null;
     */
    /* JADX WARNING: Missing block: B:48:0x00e5, code skipped:
            r21 = r15;
            r15 = new android.util.Pair(r10, new android.view.DisplayCutout(new android.graphics.Rect(0, r15, 0, r5), null, r12, null, r20, false));
            r16 = CACHE_LOCK;
     */
    /* JADX WARNING: Missing block: B:49:0x0107, code skipped:
            monitor-enter(r16);
     */
    /* JADX WARNING: Missing block: B:51:?, code skipped:
            sCachedSpec = r7;
            sCachedDisplayWidth = r1;
            sCachedDisplayHeight = r2;
            sCachedDensity = r3;
            sCachedCutout = r15;
     */
    /* JADX WARNING: Missing block: B:52:0x0112, code skipped:
            monitor-exit(r16);
     */
    /* JADX WARNING: Missing block: B:53:0x0113, code skipped:
            return r15;
     */
    /* JADX WARNING: Missing block: B:57:0x0117, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:58:0x0118, code skipped:
            android.util.Log.wtf(TAG, "Could not inflate cutout: ", r0);
     */
    /* JADX WARNING: Missing block: B:59:0x0123, code skipped:
            return NULL_PAIR;
     */
    private static android.util.Pair<android.graphics.Path, android.view.DisplayCutout> pathAndDisplayCutoutFromSpec(java.lang.String r22, int r23, int r24, float r25) {
        /*
        r1 = r23;
        r2 = r24;
        r3 = r25;
        r0 = android.text.TextUtils.isEmpty(r22);
        if (r0 == 0) goto L_0x000f;
    L_0x000c:
        r0 = NULL_PAIR;
        return r0;
    L_0x000f:
        r4 = CACHE_LOCK;
        monitor-enter(r4);
        r0 = sCachedSpec;	 Catch:{ all -> 0x0126 }
        r5 = r22;
        r0 = r5.equals(r0);	 Catch:{ all -> 0x0124 }
        if (r0 == 0) goto L_0x002e;
    L_0x001c:
        r0 = sCachedDisplayWidth;	 Catch:{ all -> 0x0124 }
        if (r0 != r1) goto L_0x002e;
    L_0x0020:
        r0 = sCachedDisplayHeight;	 Catch:{ all -> 0x0124 }
        if (r0 != r2) goto L_0x002e;
    L_0x0024:
        r0 = sCachedDensity;	 Catch:{ all -> 0x0124 }
        r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));
        if (r0 != 0) goto L_0x002e;
    L_0x002a:
        r0 = sCachedCutout;	 Catch:{ all -> 0x0124 }
        monitor-exit(r4);	 Catch:{ all -> 0x0124 }
        return r0;
    L_0x002e:
        monitor-exit(r4);	 Catch:{ all -> 0x0124 }
        r0 = r22.trim();
        r4 = "@right";
        r4 = r0.endsWith(r4);
        r5 = 0;
        if (r4 == 0) goto L_0x0051;
    L_0x003c:
        r4 = (float) r1;
        r6 = r0.length();
        r7 = "@right";
        r7 = r7.length();
        r6 = r6 - r7;
        r6 = r0.substring(r5, r6);
        r0 = r6.trim();
        goto L_0x0055;
    L_0x0051:
        r4 = (float) r1;
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = r4 / r6;
    L_0x0055:
        r6 = "@dp";
        r6 = r0.endsWith(r6);
        if (r6 == 0) goto L_0x006c;
    L_0x005d:
        r7 = r0.length();
        r8 = "@dp";
        r8 = r8.length();
        r7 = r7 - r8;
        r0 = r0.substring(r5, r7);
    L_0x006c:
        r7 = 0;
        r8 = "@bottom";
        r8 = r0.contains(r8);
        if (r8 == 0) goto L_0x008c;
    L_0x0075:
        r8 = 2;
        r9 = "@bottom";
        r8 = r0.split(r9, r8);
        r9 = r8[r5];
        r0 = r9.trim();
        r9 = 1;
        r9 = r8[r9];
        r7 = r9.trim();
        r8 = r7;
        r7 = r0;
        goto L_0x008e;
    L_0x008c:
        r8 = r7;
        r7 = r0;
    L_0x008e:
        r9 = android.graphics.Region.obtain();
        r0 = android.util.PathParser.createPathFromPathData(r7);	 Catch:{ all -> 0x0117 }
        r10 = r0;
        r0 = new android.graphics.Matrix;
        r0.<init>();
        r11 = r0;
        if (r6 == 0) goto L_0x00a3;
    L_0x00a0:
        r11.postScale(r3, r3);
    L_0x00a3:
        r0 = 0;
        r11.postTranslate(r4, r0);
        r10.transform(r11);
        r12 = new android.graphics.Rect;
        r12.<init>();
        toRectAndAddToRegion(r10, r9, r12);
        r15 = r12.bottom;
        r13 = 0;
        if (r8 == 0) goto L_0x00e2;
    L_0x00b7:
        r14 = android.util.PathParser.createPathFromPathData(r8);	 Catch:{ all -> 0x00d5 }
        r5 = (float) r2;
        r11.postTranslate(r0, r5);
        r14.transform(r11);
        r10.addPath(r14);
        r0 = new android.graphics.Rect;
        r0.<init>();
        toRectAndAddToRegion(r14, r9, r0);
        r5 = r0.top;
        r5 = r2 - r5;
        r20 = r0;
        goto L_0x00e5;
    L_0x00d5:
        r0 = move-exception;
        r5 = r0;
        r0 = r5;
        r5 = "DisplayCutout";
        r14 = "Could not inflate bottom cutout: ";
        android.util.Log.wtf(r5, r14, r0);
        r5 = NULL_PAIR;
        return r5;
    L_0x00e2:
        r5 = 0;
        r20 = r13;
    L_0x00e5:
        r14 = new android.graphics.Rect;
        r0 = 0;
        r14.<init>(r0, r15, r0, r5);
        r0 = new android.view.DisplayCutout;
        r16 = 0;
        r17 = 0;
        r19 = 0;
        r13 = r0;
        r21 = r15;
        r15 = r16;
        r16 = r12;
        r18 = r20;
        r13.<init>(r14, r15, r16, r17, r18, r19);
        r0 = new android.util.Pair;
        r0.<init>(r10, r13);
        r15 = r0;
        r16 = CACHE_LOCK;
        monitor-enter(r16);
        sCachedSpec = r7;	 Catch:{ all -> 0x0114 }
        sCachedDisplayWidth = r1;	 Catch:{ all -> 0x0114 }
        sCachedDisplayHeight = r2;	 Catch:{ all -> 0x0114 }
        sCachedDensity = r3;	 Catch:{ all -> 0x0114 }
        sCachedCutout = r15;	 Catch:{ all -> 0x0114 }
        monitor-exit(r16);	 Catch:{ all -> 0x0114 }
        return r15;
    L_0x0114:
        r0 = move-exception;
        monitor-exit(r16);	 Catch:{ all -> 0x0114 }
        throw r0;
    L_0x0117:
        r0 = move-exception;
        r5 = r0;
        r0 = r5;
        r5 = "DisplayCutout";
        r10 = "Could not inflate cutout: ";
        android.util.Log.wtf(r5, r10, r0);
        r5 = NULL_PAIR;
        return r5;
    L_0x0124:
        r0 = move-exception;
        goto L_0x0129;
    L_0x0126:
        r0 = move-exception;
        r5 = r22;
    L_0x0129:
        monitor-exit(r4);	 Catch:{ all -> 0x0124 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.DisplayCutout.pathAndDisplayCutoutFromSpec(java.lang.String, int, int, float):android.util.Pair");
    }

    private static void toRectAndAddToRegion(Path p, Region inoutRegion, Rect inoutRect) {
        RectF rectF = new RectF();
        p.computeBounds(rectF, false);
        rectF.round(inoutRect);
        inoutRegion.op(inoutRect, Op.UNION);
    }
}
