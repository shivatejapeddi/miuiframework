package android.view;

import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.SparseIntArray;
import android.view.WindowInsets.Type;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

public class InsetsState implements Parcelable {
    public static final Creator<InsetsState> CREATOR = new Creator<InsetsState>() {
        public InsetsState createFromParcel(Parcel in) {
            return new InsetsState(in);
        }

        public InsetsState[] newArray(int size) {
            return new InsetsState[size];
        }
    };
    static final int FIRST_TYPE = 0;
    static final int INSET_SIDE_BOTTOM = 3;
    static final int INSET_SIDE_LEFT = 0;
    static final int INSET_SIDE_RIGHT = 2;
    static final int INSET_SIDE_TOP = 1;
    static final int INSET_SIDE_UNKNWON = 4;
    static final int LAST_TYPE = 10;
    public static final int TYPE_BOTTOM_GESTURES = 5;
    public static final int TYPE_BOTTOM_TAPPABLE_ELEMENT = 9;
    public static final int TYPE_IME = 10;
    public static final int TYPE_LEFT_GESTURES = 6;
    public static final int TYPE_NAVIGATION_BAR = 1;
    public static final int TYPE_RIGHT_GESTURES = 7;
    public static final int TYPE_SHELF = 1;
    public static final int TYPE_SIDE_BAR_1 = 1;
    public static final int TYPE_SIDE_BAR_2 = 2;
    public static final int TYPE_SIDE_BAR_3 = 3;
    public static final int TYPE_TOP_BAR = 0;
    public static final int TYPE_TOP_GESTURES = 4;
    public static final int TYPE_TOP_TAPPABLE_ELEMENT = 8;
    private final Rect mDisplayFrame = new Rect();
    private final ArrayMap<Integer, InsetsSource> mSources = new ArrayMap();

    @Retention(RetentionPolicy.SOURCE)
    public @interface InsetSide {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface InternalInsetType {
    }

    public InsetsState(InsetsState copy) {
        set(copy);
    }

    public InsetsState(InsetsState copy, boolean copySources) {
        set(copy, copySources);
    }

    public WindowInsets calculateInsets(Rect frame, boolean isScreenRound, boolean alwaysConsumeSystemBars, DisplayCutout cutout, Rect legacyContentInsets, Rect legacyStableInsets, int legacySoftInputMode, SparseIntArray typeSideMap) {
        Rect rect = frame;
        Rect rect2 = legacyContentInsets;
        Rect rect3 = legacyStableInsets;
        Insets[] typeInsetsMap = new Insets[7];
        Insets[] typeMaxInsetsMap = new Insets[7];
        boolean[] typeVisibilityMap = new boolean[7];
        Rect relativeFrame = new Rect(rect);
        Rect relativeFrameMax = new Rect(rect);
        if (!(ViewRootImpl.sNewInsetsMode == 2 || rect2 == null || rect3 == null)) {
            WindowInsets.assignCompatInsets(typeInsetsMap, rect2);
            WindowInsets.assignCompatInsets(typeMaxInsetsMap, rect3);
        }
        int type = 0;
        while (type <= 10) {
            InsetsSource source = (InsetsSource) this.mSources.get(Integer.valueOf(type));
            if (source != null) {
                boolean z = true;
                boolean z2 = ViewRootImpl.sNewInsetsMode != 2 && (type == 0 || type == 1);
                boolean skipSystemBars = z2;
                z2 = source.getType() == 10 && (legacySoftInputMode & 16) == 0;
                boolean skipIme = z2;
                if (ViewRootImpl.sNewInsetsMode != 0 || (toPublicType(type) & Type.compatSystemInsets()) == 0) {
                    z = false;
                }
                boolean skipLegacyTypes = z;
                if (skipSystemBars || skipIme || skipLegacyTypes) {
                    typeVisibilityMap[Type.indexOf(toPublicType(type))] = source.isVisible();
                } else {
                    processSource(source, relativeFrame, false, typeInsetsMap, typeSideMap, typeVisibilityMap);
                    if (source.getType() != 10) {
                        processSource(source, relativeFrameMax, true, typeMaxInsetsMap, null, null);
                    }
                }
            }
            type++;
        }
        return new WindowInsets(typeInsetsMap, typeMaxInsetsMap, typeVisibilityMap, isScreenRound, alwaysConsumeSystemBars, cutout);
    }

    private void processSource(InsetsSource source, Rect relativeFrame, boolean ignoreVisibility, Insets[] typeInsetsMap, SparseIntArray typeSideMap, boolean[] typeVisibilityMap) {
        Insets insets = source.calculateInsets(relativeFrame, ignoreVisibility);
        int type = toPublicType(source.getType());
        processSourceAsPublicType(source, typeInsetsMap, typeSideMap, typeVisibilityMap, insets, type);
        if (type == 16) {
            processSourceAsPublicType(source, typeInsetsMap, typeSideMap, typeVisibilityMap, insets, 8);
        }
    }

    private void processSourceAsPublicType(InsetsSource source, Insets[] typeInsetsMap, SparseIntArray typeSideMap, boolean[] typeVisibilityMap, Insets insets, int type) {
        int index = Type.indexOf(type);
        Insets existing = typeInsetsMap[index];
        if (existing == null) {
            typeInsetsMap[index] = insets;
        } else {
            typeInsetsMap[index] = Insets.max(existing, insets);
        }
        if (typeVisibilityMap != null) {
            typeVisibilityMap[index] = source.isVisible();
        }
        if (typeSideMap != null && !Insets.NONE.equals(insets) && getInsetSide(insets) != 4) {
            typeSideMap.put(source.getType(), getInsetSide(insets));
        }
    }

    private int getInsetSide(Insets insets) {
        if (insets.left != 0) {
            return 0;
        }
        if (insets.top != 0) {
            return 1;
        }
        if (insets.right != 0) {
            return 2;
        }
        if (insets.bottom != 0) {
            return 3;
        }
        return 4;
    }

    public InsetsSource getSource(int type) {
        return (InsetsSource) this.mSources.computeIfAbsent(Integer.valueOf(type), -$$Lambda$cZhmLzK8aetUdx4VlP9w5jR7En0.INSTANCE);
    }

    public void setDisplayFrame(Rect frame) {
        this.mDisplayFrame.set(frame);
    }

    public Rect getDisplayFrame() {
        return this.mDisplayFrame;
    }

    public void removeSource(int type) {
        this.mSources.remove(Integer.valueOf(type));
    }

    public void set(InsetsState other) {
        set(other, false);
    }

    public void set(InsetsState other, boolean copySources) {
        this.mDisplayFrame.set(other.mDisplayFrame);
        this.mSources.clear();
        if (copySources) {
            for (int i = 0; i < other.mSources.size(); i++) {
                InsetsSource source = (InsetsSource) other.mSources.valueAt(i);
                this.mSources.put(Integer.valueOf(source.getType()), new InsetsSource(source));
            }
            return;
        }
        this.mSources.putAll(other.mSources);
    }

    public void addSource(InsetsSource source) {
        this.mSources.put(Integer.valueOf(source.getType()), source);
    }

    public int getSourcesCount() {
        return this.mSources.size();
    }

    public InsetsSource sourceAt(int index) {
        return (InsetsSource) this.mSources.valueAt(index);
    }

    public static ArraySet<Integer> toInternalType(int insetTypes) {
        ArraySet<Integer> result = new ArraySet();
        if ((insetTypes & 1) != 0) {
            result.add(Integer.valueOf(0));
        }
        if ((insetTypes & 4) != 0) {
            result.add(Integer.valueOf(1));
            result.add(Integer.valueOf(2));
            result.add(Integer.valueOf(3));
        }
        if ((insetTypes & 2) != 0) {
            result.add(Integer.valueOf(10));
        }
        return result;
    }

    static int toPublicType(int type) {
        switch (type) {
            case 0:
                return 1;
            case 1:
            case 2:
            case 3:
                return 4;
            case 4:
            case 5:
                return 16;
            case 6:
            case 7:
                return 8;
            case 8:
            case 9:
                return 32;
            case 10:
                return 2;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown type: ");
                stringBuilder.append(type);
                throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    /* JADX WARNING: Missing block: B:12:0x0012, code skipped:
            return true;
     */
    public static boolean getDefaultVisibility(int r2) {
        /*
        r0 = 1;
        if (r2 == 0) goto L_0x0012;
    L_0x0003:
        if (r2 == r0) goto L_0x0012;
    L_0x0005:
        r1 = 2;
        if (r2 == r1) goto L_0x0012;
    L_0x0008:
        r1 = 3;
        if (r2 == r1) goto L_0x0012;
    L_0x000b:
        r1 = 10;
        if (r2 == r1) goto L_0x0010;
    L_0x000f:
        return r0;
    L_0x0010:
        r0 = 0;
        return r0;
    L_0x0012:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.InsetsState.getDefaultVisibility(int):boolean");
    }

    public void dump(String prefix, PrintWriter pw) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("InsetsState");
        pw.println(stringBuilder.toString());
        for (int i = this.mSources.size() - 1; i >= 0; i--) {
            InsetsSource insetsSource = (InsetsSource) this.mSources.valueAt(i);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(prefix);
            stringBuilder2.append("  ");
            insetsSource.dump(stringBuilder2.toString(), pw);
        }
    }

    public static String typeToString(int type) {
        switch (type) {
            case 0:
                return "TYPE_TOP_BAR";
            case 1:
                return "TYPE_SIDE_BAR_1";
            case 2:
                return "TYPE_SIDE_BAR_2";
            case 3:
                return "TYPE_SIDE_BAR_3";
            case 4:
                return "TYPE_TOP_GESTURES";
            case 5:
                return "TYPE_BOTTOM_GESTURES";
            case 6:
                return "TYPE_LEFT_GESTURES";
            case 7:
                return "TYPE_RIGHT_GESTURES";
            case 8:
                return "TYPE_TOP_TAPPABLE_ELEMENT";
            case 9:
                return "TYPE_BOTTOM_TAPPABLE_ELEMENT";
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("TYPE_UNKNOWN_");
                stringBuilder.append(type);
                return stringBuilder.toString();
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InsetsState state = (InsetsState) o;
        if (!this.mDisplayFrame.equals(state.mDisplayFrame) || this.mSources.size() != state.mSources.size()) {
            return false;
        }
        for (int i = this.mSources.size() - 1; i >= 0; i--) {
            InsetsSource source = (InsetsSource) this.mSources.valueAt(i);
            InsetsSource otherSource = (InsetsSource) state.mSources.get(Integer.valueOf(source.getType()));
            if (otherSource == null || !otherSource.equals(source)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mDisplayFrame, this.mSources});
    }

    public InsetsState(Parcel in) {
        readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mDisplayFrame, flags);
        dest.writeInt(this.mSources.size());
        for (int i = 0; i < this.mSources.size(); i++) {
            dest.writeParcelable((Parcelable) this.mSources.valueAt(i), flags);
        }
    }

    public void readFromParcel(Parcel in) {
        this.mSources.clear();
        this.mDisplayFrame.set((Rect) in.readParcelable(null));
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            InsetsSource source = (InsetsSource) in.readParcelable(null);
            this.mSources.put(Integer.valueOf(source.getType()), source);
        }
    }
}
