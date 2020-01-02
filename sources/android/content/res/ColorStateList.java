package android.content.res;

import android.annotation.UnsupportedAppUsage;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.util.SparseArray;
import android.util.StateSet;
import com.android.ims.ImsConfig;
import com.android.internal.R;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ColorStateList extends ComplexColor implements Parcelable {
    public static final Creator<ColorStateList> CREATOR = new Creator<ColorStateList>() {
        public ColorStateList[] newArray(int size) {
            return new ColorStateList[size];
        }

        public ColorStateList createFromParcel(Parcel source) {
            int N = source.readInt();
            int[][] stateSpecs = new int[N][];
            for (int i = 0; i < N; i++) {
                stateSpecs[i] = source.createIntArray();
            }
            return new ColorStateList(stateSpecs, source.createIntArray());
        }
    };
    private static final int DEFAULT_COLOR = -65536;
    private static final int[][] EMPTY = new int[][]{new int[0]};
    private static final String TAG = "ColorStateList";
    private static final SparseArray<WeakReference<ColorStateList>> sCache = new SparseArray();
    private int mChangingConfigurations;
    @UnsupportedAppUsage
    private int[] mColors;
    @UnsupportedAppUsage
    private int mDefaultColor;
    @UnsupportedAppUsage
    private ColorStateListFactory mFactory;
    private boolean mIsOpaque;
    @UnsupportedAppUsage
    private int[][] mStateSpecs;
    private int[][] mThemeAttrs;

    private static class ColorStateListFactory extends ConstantState<ComplexColor> {
        private final ColorStateList mSrc;

        @UnsupportedAppUsage
        public ColorStateListFactory(ColorStateList src) {
            this.mSrc = src;
        }

        public int getChangingConfigurations() {
            return this.mSrc.mChangingConfigurations;
        }

        public ColorStateList newInstance() {
            return this.mSrc;
        }

        public ColorStateList newInstance(Resources res, Theme theme) {
            return this.mSrc.obtainForTheme(theme);
        }
    }

    @UnsupportedAppUsage
    private ColorStateList() {
    }

    public ColorStateList(int[][] states, int[] colors) {
        this.mStateSpecs = states;
        this.mColors = colors;
        onColorsChanged();
    }

    public static ColorStateList valueOf(int color) {
        synchronized (sCache) {
            int index = sCache.indexOfKey(color);
            if (index >= 0) {
                ColorStateList cached = (ColorStateList) ((WeakReference) sCache.valueAt(index)).get();
                if (cached != null) {
                    return cached;
                }
                sCache.removeAt(index);
            }
            for (int i = sCache.size() - 1; i >= 0; i--) {
                if (((WeakReference) sCache.valueAt(i)).get() == null) {
                    sCache.removeAt(i);
                }
            }
            ColorStateList csl = new ColorStateList(EMPTY, new int[]{color});
            sCache.put(color, new WeakReference(csl));
            return csl;
        }
    }

    private ColorStateList(ColorStateList orig) {
        if (orig != null) {
            this.mChangingConfigurations = orig.mChangingConfigurations;
            this.mStateSpecs = orig.mStateSpecs;
            this.mDefaultColor = orig.mDefaultColor;
            this.mIsOpaque = orig.mIsOpaque;
            this.mThemeAttrs = (int[][]) orig.mThemeAttrs.clone();
            this.mColors = (int[]) orig.mColors.clone();
        }
    }

    @Deprecated
    public static ColorStateList createFromXml(Resources r, XmlPullParser parser) throws XmlPullParserException, IOException {
        return createFromXml(r, parser, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x0017  */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0012  */
    public static android.content.res.ColorStateList createFromXml(android.content.res.Resources r4, org.xmlpull.v1.XmlPullParser r5, android.content.res.Resources.Theme r6) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r0 = android.util.Xml.asAttributeSet(r5);
    L_0x0004:
        r1 = r5.next();
        r2 = r1;
        r3 = 2;
        if (r1 == r3) goto L_0x0010;
    L_0x000c:
        r1 = 1;
        if (r2 == r1) goto L_0x0010;
    L_0x000f:
        goto L_0x0004;
    L_0x0010:
        if (r2 != r3) goto L_0x0017;
    L_0x0012:
        r1 = createFromXmlInner(r4, r5, r0, r6);
        return r1;
    L_0x0017:
        r1 = new org.xmlpull.v1.XmlPullParserException;
        r3 = "No start tag found";
        r1.<init>(r3);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.res.ColorStateList.createFromXml(android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.content.res.Resources$Theme):android.content.res.ColorStateList");
    }

    static ColorStateList createFromXmlInner(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        String name = parser.getName();
        if (name.equals("selector")) {
            ColorStateList colorStateList = new ColorStateList();
            colorStateList.inflate(r, parser, attrs, theme);
            return colorStateList;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(parser.getPositionDescription());
        stringBuilder.append(": invalid color state list tag ");
        stringBuilder.append(name);
        throw new XmlPullParserException(stringBuilder.toString());
    }

    public ColorStateList withAlpha(int alpha) {
        int[] colors = new int[this.mColors.length];
        int len = colors.length;
        for (int i = 0; i < len; i++) {
            colors[i] = (this.mColors[i] & 16777215) | (alpha << 24);
        }
        return new ColorStateList(this.mStateSpecs, colors);
    }

    private void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        AttributeSet attributeSet = attrs;
        int i = 1;
        int innerDepth = parser.getDepth() + 1;
        int changingConfigurations = 0;
        int defaultColor = -65536;
        boolean hasUnresolvedAttrs = false;
        int[][] stateSpecList = (int[][]) ArrayUtils.newUnpaddedArray(int[].class, 20);
        int[][] themeAttrsList = new int[stateSpecList.length][];
        int[] colorList = new int[stateSpecList.length];
        int listSize = 0;
        while (true) {
            int next = parser.next();
            int type = next;
            int i2;
            if (next == i) {
                i2 = type;
                break;
            }
            next = parser.getDepth();
            int depth = next;
            int i3;
            if (next < innerDepth && type == 3) {
                i3 = innerDepth;
                i2 = type;
                break;
            }
            if (type != 2 || depth > innerDepth) {
                i3 = innerDepth;
                i2 = type;
                int i4 = depth;
            } else if (parser.getName().equals(ImsConfig.EXTRA_CHANGED_ITEM)) {
                TypedArray a = Resources.obtainAttributes(r, theme, attributeSet, R.styleable.ColorStateListItem);
                Object themeAttrs = a.extractThemeAttrs();
                i3 = innerDepth;
                innerDepth = a.getColor(0, Color.MAGENTA);
                float alphaMod = a.getFloat(1, 1.0f);
                changingConfigurations |= a.getChangingConfigurations();
                a.recycle();
                type = attrs.getAttributeCount();
                int changingConfigurations2 = changingConfigurations;
                int[] stateSpec = new int[type];
                next = 0;
                depth = 0;
                while (next < type) {
                    int numAttrs = type;
                    type = attributeSet.getAttributeNameResource(next);
                    if (!(type == 16843173 || type == 16843551)) {
                        int j = depth + 1;
                        stateSpec[depth] = attributeSet.getAttributeBooleanValue(next, false) ? type : -type;
                        depth = j;
                    }
                    next++;
                    Resources resources = r;
                    type = numAttrs;
                }
                Object stateSpec2 = StateSet.trimStateSet(stateSpec, depth);
                next = modulateColorAlpha(innerDepth, alphaMod);
                if (listSize == 0 || stateSpec2.length == 0) {
                    defaultColor = next;
                }
                if (themeAttrs != null) {
                    hasUnresolvedAttrs = true;
                }
                colorList = GrowingArrayUtils.append(colorList, listSize, next);
                themeAttrsList = (int[][]) GrowingArrayUtils.append((Object[]) themeAttrsList, listSize, themeAttrs);
                stateSpecList = (int[][]) GrowingArrayUtils.append((Object[]) stateSpecList, listSize, stateSpec2);
                listSize++;
                innerDepth = i3;
                changingConfigurations = changingConfigurations2;
                i = 1;
            } else {
                i3 = innerDepth;
            }
            innerDepth = i3;
            i = 1;
        }
        this.mChangingConfigurations = changingConfigurations;
        this.mDefaultColor = defaultColor;
        if (hasUnresolvedAttrs) {
            this.mThemeAttrs = new int[listSize][];
            System.arraycopy(themeAttrsList, 0, this.mThemeAttrs, 0, listSize);
        } else {
            this.mThemeAttrs = null;
        }
        this.mColors = new int[listSize];
        this.mStateSpecs = new int[listSize][];
        System.arraycopy(colorList, 0, this.mColors, 0, listSize);
        System.arraycopy(stateSpecList, 0, this.mStateSpecs, 0, listSize);
        onColorsChanged();
    }

    @UnsupportedAppUsage
    public boolean canApplyTheme() {
        return this.mThemeAttrs != null;
    }

    private void applyTheme(Theme t) {
        if (this.mThemeAttrs != null) {
            boolean hasUnresolvedAttrs = false;
            int[][] themeAttrsList = this.mThemeAttrs;
            int N = themeAttrsList.length;
            for (int i = 0; i < N; i++) {
                if (themeAttrsList[i] != null) {
                    float defaultAlphaMod;
                    TypedArray a = t.resolveAttributes(themeAttrsList[i], R.styleable.ColorStateListItem);
                    if (themeAttrsList[i][0] != 0) {
                        defaultAlphaMod = ((float) Color.alpha(this.mColors[i])) / 255.0f;
                    } else {
                        defaultAlphaMod = 1.0f;
                    }
                    themeAttrsList[i] = a.extractThemeAttrs(themeAttrsList[i]);
                    if (themeAttrsList[i] != null) {
                        hasUnresolvedAttrs = true;
                    }
                    this.mColors[i] = modulateColorAlpha(a.getColor(0, this.mColors[i]), a.getFloat(Float.MIN_VALUE, defaultAlphaMod));
                    this.mChangingConfigurations |= a.getChangingConfigurations();
                    a.recycle();
                }
            }
            if (!hasUnresolvedAttrs) {
                this.mThemeAttrs = null;
            }
            onColorsChanged();
        }
    }

    @UnsupportedAppUsage
    public ColorStateList obtainForTheme(Theme t) {
        if (t == null || !canApplyTheme()) {
            return this;
        }
        ColorStateList clone = new ColorStateList(this);
        clone.applyTheme(t);
        return clone;
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mChangingConfigurations;
    }

    private int modulateColorAlpha(int baseColor, float alphaMod) {
        if (alphaMod == 1.0f) {
            return baseColor;
        }
        return (16777215 & baseColor) | (MathUtils.constrain((int) ((((float) Color.alpha(baseColor)) * alphaMod) + 0.5f), 0, 255) << 24);
    }

    public boolean isStateful() {
        int[][] iArr = this.mStateSpecs;
        return iArr.length >= 1 && iArr[0].length > 0;
    }

    public boolean hasFocusStateSpecified() {
        return StateSet.containsAttribute(this.mStateSpecs, 16842908);
    }

    public boolean isOpaque() {
        return this.mIsOpaque;
    }

    public int getColorForState(int[] stateSet, int defaultColor) {
        int setLength = this.mStateSpecs.length;
        for (int i = 0; i < setLength; i++) {
            if (StateSet.stateSetMatches(this.mStateSpecs[i], stateSet)) {
                return this.mColors[i];
            }
        }
        return defaultColor;
    }

    public int getDefaultColor() {
        return this.mDefaultColor;
    }

    @UnsupportedAppUsage
    public int[][] getStates() {
        return this.mStateSpecs;
    }

    @UnsupportedAppUsage
    public int[] getColors() {
        return this.mColors;
    }

    public boolean hasState(int state) {
        for (int[] states : this.mStateSpecs) {
            int stateCount = states.length;
            int stateIndex = 0;
            while (stateIndex < stateCount) {
                if (states[stateIndex] == state || states[stateIndex] == (~state)) {
                    return true;
                }
                stateIndex++;
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ColorStateList{mThemeAttrs=");
        stringBuilder.append(Arrays.deepToString(this.mThemeAttrs));
        stringBuilder.append("mChangingConfigurations=");
        stringBuilder.append(this.mChangingConfigurations);
        stringBuilder.append("mStateSpecs=");
        stringBuilder.append(Arrays.deepToString(this.mStateSpecs));
        stringBuilder.append("mColors=");
        stringBuilder.append(Arrays.toString(this.mColors));
        stringBuilder.append("mDefaultColor=");
        stringBuilder.append(this.mDefaultColor);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    @UnsupportedAppUsage
    private void onColorsChanged() {
        int defaultColor = -65536;
        boolean isOpaque = true;
        int[][] states = this.mStateSpecs;
        int[] colors = this.mColors;
        int N = states.length;
        if (N > 0) {
            int i;
            defaultColor = colors[0];
            for (i = N - 1; i > 0; i--) {
                if (states[i].length == 0) {
                    defaultColor = colors[i];
                    break;
                }
            }
            for (i = 0; i < N; i++) {
                if (Color.alpha(colors[i]) != 255) {
                    isOpaque = false;
                    break;
                }
            }
        }
        this.mDefaultColor = defaultColor;
        this.mIsOpaque = isOpaque;
    }

    public ConstantState<ComplexColor> getConstantState() {
        if (this.mFactory == null) {
            this.mFactory = new ColorStateListFactory(this);
        }
        return this.mFactory;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (canApplyTheme()) {
            Log.w(TAG, "Wrote partially-resolved ColorStateList to parcel!");
        }
        dest.writeInt(N);
        for (int[] writeIntArray : this.mStateSpecs) {
            dest.writeIntArray(writeIntArray);
        }
        dest.writeIntArray(this.mColors);
    }
}
