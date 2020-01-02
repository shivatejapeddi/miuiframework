package android.graphics.drawable;

import android.annotation.UnsupportedAppUsage;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.util.AttributeSet;
import android.util.StateSet;
import com.android.ims.ImsConfig;
import com.android.internal.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class StateListDrawable extends DrawableContainer {
    private static final boolean DEBUG = false;
    private static final String TAG = "StateListDrawable";
    private boolean mMutated;
    @UnsupportedAppUsage
    private StateListState mStateListState;

    static class StateListState extends DrawableContainerState {
        int[][] mStateSets;
        int[] mThemeAttrs;

        StateListState(StateListState orig, StateListDrawable owner, Resources res) {
            super(orig, owner, res);
            if (orig != null) {
                this.mThemeAttrs = orig.mThemeAttrs;
                this.mStateSets = orig.mStateSets;
                return;
            }
            this.mThemeAttrs = null;
            this.mStateSets = new int[getCapacity()][];
        }

        /* Access modifiers changed, original: 0000 */
        public void mutate() {
            int[] iArr = this.mThemeAttrs;
            this.mThemeAttrs = iArr != null ? (int[]) iArr.clone() : null;
            int[][] iArr2 = this.mStateSets;
            int[][] stateSets = new int[iArr2.length][];
            for (int i = iArr2.length - 1; i >= 0; i--) {
                int[][] iArr3 = this.mStateSets;
                stateSets[i] = iArr3[i] != null ? (int[]) iArr3[i].clone() : null;
            }
            this.mStateSets = stateSets;
        }

        /* Access modifiers changed, original: 0000 */
        @UnsupportedAppUsage
        public int addStateSet(int[] stateSet, Drawable drawable) {
            int pos = addChild(drawable);
            this.mStateSets[pos] = stateSet;
            return pos;
        }

        /* Access modifiers changed, original: 0000 */
        public int indexOfStateSet(int[] stateSet) {
            int[][] stateSets = this.mStateSets;
            int N = getChildCount();
            for (int i = 0; i < N; i++) {
                if (StateSet.stateSetMatches(stateSets[i], stateSet)) {
                    return i;
                }
            }
            return -1;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean hasFocusStateSpecified() {
            return StateSet.containsAttribute(this.mStateSets, 16842908);
        }

        public Drawable newDrawable() {
            return new StateListDrawable(this, null);
        }

        public Drawable newDrawable(Resources res) {
            return new StateListDrawable(this, res);
        }

        public boolean canApplyTheme() {
            return this.mThemeAttrs != null || super.canApplyTheme();
        }

        public void growArray(int oldSize, int newSize) {
            super.growArray(oldSize, newSize);
            int[][] newStateSets = new int[newSize][];
            System.arraycopy(this.mStateSets, 0, newStateSets, 0, oldSize);
            this.mStateSets = newStateSets;
        }
    }

    public StateListDrawable() {
        this(null, null);
    }

    public void addState(int[] stateSet, Drawable drawable) {
        if (drawable != null) {
            this.mStateListState.addStateSet(stateSet, drawable);
            onStateChange(getState());
        }
    }

    public boolean isStateful() {
        return true;
    }

    public boolean hasFocusStateSpecified() {
        return this.mStateListState.hasFocusStateSpecified();
    }

    /* Access modifiers changed, original: protected */
    public boolean onStateChange(int[] stateSet) {
        boolean changed = super.onStateChange(stateSet);
        int idx = this.mStateListState.indexOfStateSet(stateSet);
        if (idx < 0) {
            idx = this.mStateListState.indexOfStateSet(StateSet.WILD_CARD);
        }
        return selectDrawable(idx) || changed;
    }

    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        TypedArray a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.StateListDrawable);
        super.inflateWithAttributes(r, parser, a, 1);
        updateStateFromTypedArray(a);
        updateDensity(r);
        a.recycle();
        inflateChildElements(r, parser, attrs, theme);
        onStateChange(getState());
    }

    @UnsupportedAppUsage
    private void updateStateFromTypedArray(TypedArray a) {
        StateListState state = this.mStateListState;
        state.mChangingConfigurations |= a.getChangingConfigurations();
        state.mThemeAttrs = a.extractThemeAttrs();
        state.mVariablePadding = a.getBoolean(2, state.mVariablePadding);
        state.mConstantSize = a.getBoolean(3, state.mConstantSize);
        state.mEnterFadeDuration = a.getInt(4, state.mEnterFadeDuration);
        state.mExitFadeDuration = a.getInt(5, state.mExitFadeDuration);
        state.mDither = a.getBoolean(0, state.mDither);
        state.mAutoMirrored = a.getBoolean(6, state.mAutoMirrored);
    }

    private void inflateChildElements(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        StateListState state = this.mStateListState;
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
                    if (depth > innerDepth) {
                        continue;
                    } else if (parser.getName().equals(ImsConfig.EXTRA_CHANGED_ITEM)) {
                        TypedArray a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.StateListDrawableItem);
                        Drawable dr = a.getDrawable(null);
                        a.recycle();
                        int[] states = extractStateSet(attrs);
                        if (dr == null) {
                            while (true) {
                                int next2 = parser.next();
                                type = next2;
                                if (next2 != 4) {
                                    break;
                                }
                            }
                            if (type == 2) {
                                dr = Drawable.createFromXmlInner(r, parser, attrs, theme);
                            } else {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(parser.getPositionDescription());
                                stringBuilder.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
                                throw new XmlPullParserException(stringBuilder.toString());
                            }
                        }
                        state.addStateSet(states, dr);
                    }
                }
            } else {
                return;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public int[] extractStateSet(AttributeSet attrs) {
        int j = 0;
        int numAttrs = attrs.getAttributeCount();
        int[] states = new int[numAttrs];
        for (int i = 0; i < numAttrs; i++) {
            int stateResId = attrs.getAttributeNameResource(i);
            if (!(stateResId == 0 || stateResId == 16842960 || stateResId == 16843161)) {
                int j2 = j + 1;
                states[j] = attrs.getAttributeBooleanValue(i, false) ? stateResId : -stateResId;
                j = j2;
            }
        }
        return StateSet.trimStateSet(states, j);
    }

    /* Access modifiers changed, original: 0000 */
    public StateListState getStateListState() {
        return this.mStateListState;
    }

    public int getStateCount() {
        return this.mStateListState.getChildCount();
    }

    public int[] getStateSet(int index) {
        return this.mStateListState.mStateSets[index];
    }

    public Drawable getStateDrawable(int index) {
        return this.mStateListState.getChild(index);
    }

    public int findStateDrawableIndex(int[] stateSet) {
        return this.mStateListState.indexOfStateSet(stateSet);
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mStateListState.mutate();
            this.mMutated = true;
        }
        return this;
    }

    /* Access modifiers changed, original: 0000 */
    public StateListState cloneConstantState() {
        return new StateListState(this.mStateListState, this, null);
    }

    public void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    public void applyTheme(Theme theme) {
        super.applyTheme(theme);
        onStateChange(getState());
    }

    /* Access modifiers changed, original: protected */
    public void setConstantState(DrawableContainerState state) {
        super.setConstantState(state);
        if (state instanceof StateListState) {
            this.mStateListState = (StateListState) state;
        }
    }

    private StateListDrawable(StateListState state, Resources res) {
        setConstantState(new StateListState(state, this, res));
        onStateChange(getState());
    }

    StateListDrawable(StateListState state) {
        if (state != null) {
            setConstantState(state);
        }
    }
}
