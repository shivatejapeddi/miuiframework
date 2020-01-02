package android.app;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.UnsupportedAppUsage;
import android.app.Fragment.SavedState;
import android.app.FragmentManager.BackStackEntry;
import android.app.FragmentManager.FragmentLifecycleCallbacks;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.DebugUtils;
import android.util.Log;
import android.util.LogWriter;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater.Factory2;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.R;
import com.android.internal.util.FastPrintWriter;
import com.miui.internal.search.Function;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: FragmentManager */
final class FragmentManagerImpl extends FragmentManager implements Factory2 {
    static boolean DEBUG = false;
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    @UnsupportedAppUsage
    SparseArray<Fragment> mActive;
    @UnsupportedAppUsage
    final ArrayList<Fragment> mAdded = new ArrayList();
    boolean mAllowOldReentrantBehavior;
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState = 0;
    boolean mDestroyed;
    Runnable mExecCommit = new Runnable() {
        public void run() {
            FragmentManagerImpl.this.execPendingActions();
        }
    };
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback<?> mHost;
    final CopyOnWriteArrayList<Pair<FragmentLifecycleCallbacks, Boolean>> mLifecycleCallbacks = new CopyOnWriteArrayList();
    boolean mNeedMenuInvalidate;
    int mNextFragmentIndex = 0;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<OpGenerator> mPendingActions;
    ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    Fragment mPrimaryNav;
    FragmentManagerNonConfig mSavedNonConfig;
    SparseArray<Parcelable> mStateArray = null;
    Bundle mStateBundle = null;
    @UnsupportedAppUsage
    boolean mStateSaved;
    ArrayList<Fragment> mTmpAddedFragments;
    ArrayList<Boolean> mTmpIsPop;
    ArrayList<BackStackRecord> mTmpRecords;

    /* compiled from: FragmentManager */
    interface OpGenerator {
        boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2);
    }

    /* compiled from: FragmentManager */
    static class AnimateOnHWLayerIfNeededListener implements AnimatorListener {
        private boolean mShouldRunOnHWLayer = false;
        private View mView;

        public AnimateOnHWLayerIfNeededListener(View v) {
            if (v != null) {
                this.mView = v;
            }
        }

        public void onAnimationStart(Animator animation) {
            this.mShouldRunOnHWLayer = FragmentManagerImpl.shouldRunOnHWLayer(this.mView, animation);
            if (this.mShouldRunOnHWLayer) {
                this.mView.setLayerType(2, null);
            }
        }

        public void onAnimationEnd(Animator animation) {
            if (this.mShouldRunOnHWLayer) {
                this.mView.setLayerType(0, null);
            }
            this.mView = null;
            animation.removeListener(this);
        }

        public void onAnimationCancel(Animator animation) {
        }

        public void onAnimationRepeat(Animator animation) {
        }
    }

    /* compiled from: FragmentManager */
    private class PopBackStackState implements OpGenerator {
        final int mFlags;
        final int mId;
        final String mName;

        public PopBackStackState(String name, int id, int flags) {
            this.mName = name;
            this.mId = id;
            this.mFlags = flags;
        }

        public boolean generateOps(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
            if (FragmentManagerImpl.this.mPrimaryNav != null && this.mId < 0 && this.mName == null) {
                FragmentManager childManager = FragmentManagerImpl.this.mPrimaryNav.mChildFragmentManager;
                if (childManager != null && childManager.popBackStackImmediate()) {
                    return false;
                }
            }
            return FragmentManagerImpl.this.popBackStackState(records, isRecordPop, this.mName, this.mId, this.mFlags);
        }
    }

    /* compiled from: FragmentManager */
    static class StartEnterTransitionListener implements OnStartEnterTransitionListener {
        private final boolean mIsBack;
        private int mNumPostponed;
        private final BackStackRecord mRecord;

        public StartEnterTransitionListener(BackStackRecord record, boolean isBack) {
            this.mIsBack = isBack;
            this.mRecord = record;
        }

        public void onStartEnterTransition() {
            this.mNumPostponed--;
            if (this.mNumPostponed == 0) {
                this.mRecord.mManager.scheduleCommit();
            }
        }

        public void startListening() {
            this.mNumPostponed++;
        }

        public boolean isReady() {
            return this.mNumPostponed == 0;
        }

        public void completeTransaction() {
            boolean z = false;
            boolean canceled = this.mNumPostponed > 0;
            FragmentManagerImpl manager = this.mRecord.mManager;
            int numAdded = manager.mAdded.size();
            for (int i = 0; i < numAdded; i++) {
                Fragment fragment = (Fragment) manager.mAdded.get(i);
                fragment.setOnStartEnterTransitionListener(null);
                if (canceled && fragment.isPostponed()) {
                    fragment.startPostponedEnterTransition();
                }
            }
            FragmentManagerImpl fragmentManagerImpl = this.mRecord.mManager;
            BackStackRecord backStackRecord = this.mRecord;
            boolean z2 = this.mIsBack;
            if (!canceled) {
                z = true;
            }
            fragmentManagerImpl.completeExecute(backStackRecord, z2, z, true);
        }

        public void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
        }
    }

    FragmentManagerImpl() {
    }

    private void throwException(RuntimeException ex) {
        String message = ex.getMessage();
        String str = TAG;
        Log.e(str, message);
        PrintWriter pw = new FastPrintWriter(new LogWriter(6, str), false, 1024);
        String str2 = "Failed dumping state";
        String str3 = "  ";
        if (this.mHost != null) {
            Log.e(str, "Activity state:");
            try {
                this.mHost.onDump(str3, null, pw, new String[0]);
            } catch (Exception e) {
                pw.flush();
                Log.e(str, str2, e);
            }
        } else {
            Log.e(str, "Fragment manager state:");
            try {
                dump(str3, null, pw, new String[0]);
            } catch (Exception e2) {
                pw.flush();
                Log.e(str, str2, e2);
            }
        }
        pw.flush();
        throw ex;
    }

    static boolean modifiesAlpha(Animator anim) {
        if (anim == null) {
            return false;
        }
        if (anim instanceof ValueAnimator) {
            PropertyValuesHolder[] values = ((ValueAnimator) anim).getValues();
            for (PropertyValuesHolder propertyName : values) {
                if ("alpha".equals(propertyName.getPropertyName())) {
                    return true;
                }
            }
        } else if (anim instanceof AnimatorSet) {
            List<Animator> animList = ((AnimatorSet) anim).getChildAnimations();
            for (int i = 0; i < animList.size(); i++) {
                if (modifiesAlpha((Animator) animList.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean shouldRunOnHWLayer(View v, Animator anim) {
        boolean z = false;
        if (v == null || anim == null) {
            return false;
        }
        if (v.getLayerType() == 0 && v.hasOverlappingRendering() && modifiesAlpha(anim)) {
            z = true;
        }
        return z;
    }

    /* JADX WARNING: Missing block: B:6:0x0014, code skipped:
            return;
     */
    private void setHWLayerAnimListenerIfAlpha(android.view.View r2, android.animation.Animator r3) {
        /*
        r1 = this;
        if (r2 == 0) goto L_0x0014;
    L_0x0002:
        if (r3 != 0) goto L_0x0005;
    L_0x0004:
        goto L_0x0014;
    L_0x0005:
        r0 = shouldRunOnHWLayer(r2, r3);
        if (r0 == 0) goto L_0x0013;
    L_0x000b:
        r0 = new android.app.FragmentManagerImpl$AnimateOnHWLayerIfNeededListener;
        r0.<init>(r2);
        r3.addListener(r0);
    L_0x0013:
        return;
    L_0x0014:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.FragmentManagerImpl.setHWLayerAnimListenerIfAlpha(android.view.View, android.animation.Animator):void");
    }

    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    public boolean executePendingTransactions() {
        boolean updates = execPendingActions();
        forcePostponedTransactions();
        return updates;
    }

    public void popBackStack() {
        enqueueAction(new PopBackStackState(null, -1, 0), false);
    }

    public boolean popBackStackImmediate() {
        checkStateLoss();
        return popBackStackImmediate(null, -1, 0);
    }

    public void popBackStack(String name, int flags) {
        enqueueAction(new PopBackStackState(name, -1, flags), false);
    }

    public boolean popBackStackImmediate(String name, int flags) {
        checkStateLoss();
        return popBackStackImmediate(name, -1, flags);
    }

    public void popBackStack(int id, int flags) {
        if (id >= 0) {
            enqueueAction(new PopBackStackState(null, id, flags), false);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bad id: ");
        stringBuilder.append(id);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public boolean popBackStackImmediate(int id, int flags) {
        checkStateLoss();
        if (id >= 0) {
            return popBackStackImmediate(null, id, flags);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bad id: ");
        stringBuilder.append(id);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private boolean popBackStackImmediate(String name, int id, int flags) {
        execPendingActions();
        ensureExecReady(true);
        FragmentManager childManager = this.mPrimaryNav;
        if (childManager != null && id < 0 && name == null) {
            childManager = childManager.mChildFragmentManager;
            if (childManager != null && childManager.popBackStackImmediate()) {
                return true;
            }
        }
        boolean executePop = popBackStackState(this.mTmpRecords, this.mTmpIsPop, name, id, flags);
        if (executePop) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            } finally {
                cleanupExec();
            }
        }
        doPendingDeferredStart();
        burpActive();
        return executePop;
    }

    public int getBackStackEntryCount() {
        ArrayList arrayList = this.mBackStack;
        return arrayList != null ? arrayList.size() : 0;
    }

    public BackStackEntry getBackStackEntryAt(int index) {
        return (BackStackEntry) this.mBackStack.get(index);
    }

    public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList();
        }
        this.mBackStackChangeListeners.add(listener);
    }

    public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {
        ArrayList arrayList = this.mBackStackChangeListeners;
        if (arrayList != null) {
            arrayList.remove(listener);
        }
    }

    public void putFragment(Bundle bundle, String key, Fragment fragment) {
        if (fragment.mIndex < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(fragment);
            stringBuilder.append(" is not currently in the FragmentManager");
            throwException(new IllegalStateException(stringBuilder.toString()));
        }
        bundle.putInt(key, fragment.mIndex);
    }

    public Fragment getFragment(Bundle bundle, String key) {
        int index = bundle.getInt(key, -1);
        if (index == -1) {
            return null;
        }
        Fragment f = (Fragment) this.mActive.get(index);
        if (f == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment no longer exists for key ");
            stringBuilder.append(key);
            stringBuilder.append(": index ");
            stringBuilder.append(index);
            throwException(new IllegalStateException(stringBuilder.toString()));
        }
        return f;
    }

    public List<Fragment> getFragments() {
        if (this.mAdded.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List list;
        synchronized (this.mAdded) {
            list = (List) this.mAdded.clone();
        }
        return list;
    }

    public SavedState saveFragmentInstanceState(Fragment fragment) {
        if (fragment.mIndex < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(fragment);
            stringBuilder.append(" is not currently in the FragmentManager");
            throwException(new IllegalStateException(stringBuilder.toString()));
        }
        SavedState savedState = null;
        if (fragment.mState <= 0) {
            return null;
        }
        Bundle result = saveFragmentBasicState(fragment);
        if (result != null) {
            savedState = new SavedState(result);
        }
        return savedState;
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        Fragment fragment = this.mParent;
        if (fragment != null) {
            DebugUtils.buildShortClassTag(fragment, sb);
        } else {
            DebugUtils.buildShortClassTag(this.mHost, sb);
        }
        sb.append("}}");
        return sb.toString();
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        int i;
        Fragment f;
        BackStackRecord bs;
        String innerPrefix = new StringBuilder();
        innerPrefix.append(prefix);
        innerPrefix.append("    ");
        innerPrefix = innerPrefix.toString();
        int N = this.mActive;
        if (N != 0) {
            N = N.size();
            if (N > 0) {
                writer.print(prefix);
                writer.print("Active Fragments in ");
                writer.print(Integer.toHexString(System.identityHashCode(this)));
                writer.println(":");
                for (i = 0; i < N; i++) {
                    f = (Fragment) this.mActive.valueAt(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(f);
                    if (f != null) {
                        f.dump(innerPrefix, fd, writer, args);
                    }
                }
            }
        }
        N = this.mAdded.size();
        if (N > 0) {
            writer.print(prefix);
            writer.println("Added Fragments:");
            for (i = 0; i < N; i++) {
                f = (Fragment) this.mAdded.get(i);
                writer.print(prefix);
                writer.print("  #");
                writer.print(i);
                writer.print(": ");
                writer.println(f.toString());
            }
        }
        ArrayList arrayList = this.mCreatedMenus;
        if (arrayList != null) {
            N = arrayList.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Fragments Created Menus:");
                for (i = 0; i < N; i++) {
                    f = (Fragment) this.mCreatedMenus.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(f.toString());
                }
            }
        }
        arrayList = this.mBackStack;
        if (arrayList != null) {
            N = arrayList.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Back Stack:");
                for (i = 0; i < N; i++) {
                    bs = (BackStackRecord) this.mBackStack.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(bs.toString());
                    bs.dump(innerPrefix, fd, writer, args);
                }
            }
        }
        synchronized (this) {
            if (this.mBackStackIndices != null) {
                N = this.mBackStackIndices.size();
                if (N > 0) {
                    writer.print(prefix);
                    writer.println("Back Stack Indices:");
                    for (i = 0; i < N; i++) {
                        bs = (BackStackRecord) this.mBackStackIndices.get(i);
                        writer.print(prefix);
                        writer.print("  #");
                        writer.print(i);
                        writer.print(": ");
                        writer.println(bs);
                    }
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                writer.print(prefix);
                writer.print("mAvailBackStackIndices: ");
                writer.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
        }
        arrayList = this.mPendingActions;
        if (arrayList != null) {
            N = arrayList.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Pending Actions:");
                for (i = 0; i < N; i++) {
                    OpGenerator r = (OpGenerator) this.mPendingActions.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(r);
                }
            }
        }
        writer.print(prefix);
        writer.println("FragmentManager misc state:");
        writer.print(prefix);
        writer.print("  mHost=");
        writer.println(this.mHost);
        writer.print(prefix);
        writer.print("  mContainer=");
        writer.println(this.mContainer);
        if (this.mParent != null) {
            writer.print(prefix);
            writer.print("  mParent=");
            writer.println(this.mParent);
        }
        writer.print(prefix);
        writer.print("  mCurState=");
        writer.print(this.mCurState);
        writer.print(" mStateSaved=");
        writer.print(this.mStateSaved);
        writer.print(" mDestroyed=");
        writer.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            writer.print(prefix);
            writer.print("  mNeedMenuInvalidate=");
            writer.println(this.mNeedMenuInvalidate);
        }
        if (this.mNoTransactionsBecause != null) {
            writer.print(prefix);
            writer.print("  mNoTransactionsBecause=");
            writer.println(this.mNoTransactionsBecause);
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public Animator loadAnimator(Fragment fragment, int transit, boolean enter, int transitionStyle) {
        Animator animObj = fragment.onCreateAnimator(transit, enter, fragment.getNextAnim());
        if (animObj != null) {
            return animObj;
        }
        if (fragment.getNextAnim() != 0) {
            Animator anim = AnimatorInflater.loadAnimator(this.mHost.getContext(), fragment.getNextAnim());
            if (anim != null) {
                return anim;
            }
        }
        if (transit == 0) {
            return null;
        }
        int styleIndex = transitToStyleIndex(transit, enter);
        if (styleIndex < 0) {
            return null;
        }
        if (transitionStyle == 0 && this.mHost.onHasWindowAnimations()) {
            transitionStyle = this.mHost.onGetWindowAnimations();
        }
        if (transitionStyle == 0) {
            return null;
        }
        TypedArray attrs = this.mHost.getContext().obtainStyledAttributes(transitionStyle, R.styleable.FragmentAnimation);
        int anim2 = attrs.getResourceId(styleIndex, 0);
        attrs.recycle();
        if (anim2 == 0) {
            return null;
        }
        return AnimatorInflater.loadAnimator(this.mHost.getContext(), anim2);
    }

    public void performPendingDeferredStart(Fragment f) {
        if (f.mDeferStart) {
            if (this.mExecutingActions) {
                this.mHavePendingDeferredStart = true;
                return;
            }
            f.mDeferStart = false;
            moveToState(f, this.mCurState, 0, 0, false);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isStateAtLeast(int state) {
        return this.mCurState >= state;
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x0291  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x02b2  */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x028d  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x0291  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x02b2  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0199  */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x028d  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x0291  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x02b2  */
    /* JADX WARNING: Removed duplicated region for block: B:193:0x03c0  */
    /* JADX WARNING: Missing block: B:39:0x006e, code skipped:
            if (r1 != 4) goto L_0x02d4;
     */
    public void moveToState(android.app.Fragment r20, int r21, int r22, int r23, boolean r24) {
        /*
        r19 = this;
        r7 = r19;
        r8 = r20;
        r0 = DEBUG;
        r0 = r8.mAdded;
        r9 = 1;
        if (r0 == 0) goto L_0x0013;
    L_0x000b:
        r0 = r8.mDetached;
        if (r0 == 0) goto L_0x0010;
    L_0x000f:
        goto L_0x0013;
    L_0x0010:
        r0 = r21;
        goto L_0x0018;
    L_0x0013:
        r0 = r21;
        if (r0 <= r9) goto L_0x0018;
    L_0x0017:
        r0 = 1;
    L_0x0018:
        r1 = r8.mRemoving;
        if (r1 == 0) goto L_0x002e;
    L_0x001c:
        r1 = r8.mState;
        if (r0 <= r1) goto L_0x002e;
    L_0x0020:
        r1 = r8.mState;
        if (r1 != 0) goto L_0x002c;
    L_0x0024:
        r1 = r20.isInBackStack();
        if (r1 == 0) goto L_0x002c;
    L_0x002a:
        r0 = 1;
        goto L_0x002e;
    L_0x002c:
        r0 = r8.mState;
    L_0x002e:
        r1 = r8.mDeferStart;
        r10 = 4;
        r11 = 3;
        if (r1 == 0) goto L_0x003b;
    L_0x0034:
        r1 = r8.mState;
        if (r1 >= r10) goto L_0x003b;
    L_0x0038:
        if (r0 <= r11) goto L_0x003b;
    L_0x003a:
        r0 = 3;
    L_0x003b:
        r1 = r8.mState;
        r12 = 2;
        r13 = "FragmentManager";
        r14 = 0;
        r15 = 0;
        if (r1 > r0) goto L_0x02da;
    L_0x0044:
        r1 = r8.mFromLayout;
        if (r1 == 0) goto L_0x004d;
    L_0x0048:
        r1 = r8.mInLayout;
        if (r1 != 0) goto L_0x004d;
    L_0x004c:
        return;
    L_0x004d:
        r1 = r20.getAnimatingAway();
        if (r1 == 0) goto L_0x0064;
    L_0x0053:
        r8.setAnimatingAway(r14);
        r3 = r20.getStateAfterAnimating();
        r4 = 0;
        r5 = 0;
        r6 = 1;
        r1 = r19;
        r2 = r20;
        r1.moveToState(r2, r3, r4, r5, r6);
    L_0x0064:
        r1 = r8.mState;
        if (r1 == 0) goto L_0x0072;
    L_0x0068:
        if (r1 == r9) goto L_0x0193;
    L_0x006a:
        if (r1 == r12) goto L_0x028b;
    L_0x006c:
        if (r1 == r11) goto L_0x028f;
    L_0x006e:
        if (r1 == r10) goto L_0x02af;
    L_0x0070:
        goto L_0x02d4;
    L_0x0072:
        if (r0 <= 0) goto L_0x0193;
    L_0x0074:
        r1 = DEBUG;
        if (r1 == 0) goto L_0x008c;
    L_0x0078:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "moveto CREATED: ";
        r1.append(r2);
        r1.append(r8);
        r1 = r1.toString();
        android.util.Log.v(r13, r1);
    L_0x008c:
        r1 = r8.mSavedFragmentState;
        if (r1 == 0) goto L_0x00c5;
    L_0x0090:
        r1 = r8.mSavedFragmentState;
        r2 = "android:view_state";
        r1 = r1.getSparseParcelableArray(r2);
        r8.mSavedViewState = r1;
        r1 = r8.mSavedFragmentState;
        r2 = "android:target_state";
        r1 = r7.getFragment(r1, r2);
        r8.mTarget = r1;
        r1 = r8.mTarget;
        if (r1 == 0) goto L_0x00b2;
    L_0x00a8:
        r1 = r8.mSavedFragmentState;
        r2 = "android:target_req_state";
        r1 = r1.getInt(r2, r15);
        r8.mTargetRequestCode = r1;
    L_0x00b2:
        r1 = r8.mSavedFragmentState;
        r2 = "android:user_visible_hint";
        r1 = r1.getBoolean(r2, r9);
        r8.mUserVisibleHint = r1;
        r1 = r8.mUserVisibleHint;
        if (r1 != 0) goto L_0x00c5;
    L_0x00c0:
        r8.mDeferStart = r9;
        if (r0 <= r11) goto L_0x00c5;
    L_0x00c4:
        r0 = 3;
    L_0x00c5:
        r1 = r7.mHost;
        r8.mHost = r1;
        r2 = r7.mParent;
        r8.mParentFragment = r2;
        if (r2 == 0) goto L_0x00d2;
    L_0x00cf:
        r1 = r2.mChildFragmentManager;
        goto L_0x00d6;
    L_0x00d2:
        r1 = r1.getFragmentManagerImpl();
    L_0x00d6:
        r8.mFragmentManager = r1;
        r1 = r8.mTarget;
        r6 = "Fragment ";
        if (r1 == 0) goto L_0x0129;
    L_0x00de:
        r1 = r7.mActive;
        r2 = r8.mTarget;
        r2 = r2.mIndex;
        r1 = r1.get(r2);
        r2 = r8.mTarget;
        if (r1 != r2) goto L_0x0104;
    L_0x00ec:
        r1 = r8.mTarget;
        r1 = r1.mState;
        if (r1 >= r9) goto L_0x0102;
    L_0x00f2:
        r2 = r8.mTarget;
        r3 = 1;
        r4 = 0;
        r5 = 0;
        r16 = 1;
        r1 = r19;
        r10 = r6;
        r6 = r16;
        r1.moveToState(r2, r3, r4, r5, r6);
        goto L_0x012a;
    L_0x0102:
        r10 = r6;
        goto L_0x012a;
    L_0x0104:
        r10 = r6;
        r1 = new java.lang.IllegalStateException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r2.append(r10);
        r2.append(r8);
        r3 = " declared target fragment ";
        r2.append(r3);
        r3 = r8.mTarget;
        r2.append(r3);
        r3 = " that does not belong to this FragmentManager!";
        r2.append(r3);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
    L_0x0129:
        r10 = r6;
    L_0x012a:
        r1 = r7.mHost;
        r1 = r1.getContext();
        r7.dispatchOnFragmentPreAttached(r8, r1, r15);
        r8.mCalled = r15;
        r1 = r7.mHost;
        r1 = r1.getContext();
        r8.onAttach(r1);
        r1 = r8.mCalled;
        if (r1 == 0) goto L_0x0179;
    L_0x0142:
        r1 = r8.mParentFragment;
        if (r1 != 0) goto L_0x014c;
    L_0x0146:
        r1 = r7.mHost;
        r1.onAttachFragment(r8);
        goto L_0x0151;
    L_0x014c:
        r1 = r8.mParentFragment;
        r1.onAttachFragment(r8);
    L_0x0151:
        r1 = r7.mHost;
        r1 = r1.getContext();
        r7.dispatchOnFragmentAttached(r8, r1, r15);
        r1 = r8.mIsCreated;
        if (r1 != 0) goto L_0x016e;
    L_0x015e:
        r1 = r8.mSavedFragmentState;
        r7.dispatchOnFragmentPreCreated(r8, r1, r15);
        r1 = r8.mSavedFragmentState;
        r8.performCreate(r1);
        r1 = r8.mSavedFragmentState;
        r7.dispatchOnFragmentCreated(r8, r1, r15);
        goto L_0x0175;
    L_0x016e:
        r1 = r8.mSavedFragmentState;
        r8.restoreChildFragmentState(r1, r9);
        r8.mState = r9;
    L_0x0175:
        r8.mRetaining = r15;
        r1 = r0;
        goto L_0x0194;
    L_0x0179:
        r1 = new android.util.SuperNotCalledException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r2.append(r10);
        r2.append(r8);
        r3 = " did not call through to super.onAttach()";
        r2.append(r3);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
    L_0x0193:
        r1 = r0;
    L_0x0194:
        r19.ensureInflatedFragmentView(r20);
        if (r1 <= r9) goto L_0x028a;
    L_0x0199:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x01b1;
    L_0x019d:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r2 = "moveto ACTIVITY_CREATED: ";
        r0.append(r2);
        r0.append(r8);
        r0 = r0.toString();
        android.util.Log.v(r13, r0);
    L_0x01b1:
        r0 = r8.mFromLayout;
        if (r0 != 0) goto L_0x0275;
    L_0x01b5:
        r0 = 0;
        r2 = r8.mContainerId;
        if (r2 == 0) goto L_0x022c;
    L_0x01ba:
        r2 = r8.mContainerId;
        r3 = -1;
        if (r2 != r3) goto L_0x01dd;
    L_0x01bf:
        r2 = new java.lang.IllegalArgumentException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Cannot create fragment ";
        r3.append(r4);
        r3.append(r8);
        r4 = " for a container view with no id";
        r3.append(r4);
        r3 = r3.toString();
        r2.<init>(r3);
        r7.throwException(r2);
    L_0x01dd:
        r2 = r7.mContainer;
        r3 = r8.mContainerId;
        r2 = r2.onFindViewById(r3);
        r2 = (android.view.ViewGroup) r2;
        if (r2 != 0) goto L_0x022b;
    L_0x01e9:
        r0 = r8.mRestored;
        if (r0 != 0) goto L_0x022b;
    L_0x01ed:
        r0 = r20.getResources();	 Catch:{ NotFoundException -> 0x01f8 }
        r3 = r8.mContainerId;	 Catch:{ NotFoundException -> 0x01f8 }
        r0 = r0.getResourceName(r3);	 Catch:{ NotFoundException -> 0x01f8 }
        goto L_0x01fc;
    L_0x01f8:
        r0 = move-exception;
        r0 = "unknown";
    L_0x01fc:
        r3 = new java.lang.IllegalArgumentException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "No view found for id 0x";
        r4.append(r5);
        r5 = r8.mContainerId;
        r5 = java.lang.Integer.toHexString(r5);
        r4.append(r5);
        r5 = " (";
        r4.append(r5);
        r4.append(r0);
        r5 = ") for fragment ";
        r4.append(r5);
        r4.append(r8);
        r4 = r4.toString();
        r3.<init>(r4);
        r7.throwException(r3);
    L_0x022b:
        r0 = r2;
    L_0x022c:
        r8.mContainer = r0;
        r2 = r8.mSavedFragmentState;
        r2 = r8.performGetLayoutInflater(r2);
        r3 = r8.mSavedFragmentState;
        r2 = r8.performCreateView(r2, r0, r3);
        r8.mView = r2;
        r2 = r8.mView;
        if (r2 == 0) goto L_0x0275;
    L_0x0240:
        r2 = r8.mView;
        r2.setSaveFromParentEnabled(r15);
        if (r0 == 0) goto L_0x024c;
    L_0x0247:
        r2 = r8.mView;
        r0.addView(r2);
    L_0x024c:
        r2 = r8.mHidden;
        if (r2 == 0) goto L_0x0257;
    L_0x0250:
        r2 = r8.mView;
        r3 = 8;
        r2.setVisibility(r3);
    L_0x0257:
        r2 = r8.mView;
        r3 = r8.mSavedFragmentState;
        r8.onViewCreated(r2, r3);
        r2 = r8.mView;
        r3 = r8.mSavedFragmentState;
        r7.dispatchOnFragmentViewCreated(r8, r2, r3, r15);
        r2 = r8.mView;
        r2 = r2.getVisibility();
        if (r2 != 0) goto L_0x0272;
    L_0x026d:
        r2 = r8.mContainer;
        if (r2 == 0) goto L_0x0272;
    L_0x0271:
        goto L_0x0273;
    L_0x0272:
        r9 = r15;
    L_0x0273:
        r8.mIsNewlyAdded = r9;
    L_0x0275:
        r0 = r8.mSavedFragmentState;
        r8.performActivityCreated(r0);
        r0 = r8.mSavedFragmentState;
        r7.dispatchOnFragmentActivityCreated(r8, r0, r15);
        r0 = r8.mView;
        if (r0 == 0) goto L_0x0288;
    L_0x0283:
        r0 = r8.mSavedFragmentState;
        r8.restoreViewState(r0);
    L_0x0288:
        r8.mSavedFragmentState = r14;
    L_0x028a:
        r0 = r1;
    L_0x028b:
        if (r0 <= r12) goto L_0x028f;
    L_0x028d:
        r8.mState = r11;
    L_0x028f:
        if (r0 <= r11) goto L_0x02af;
    L_0x0291:
        r1 = DEBUG;
        if (r1 == 0) goto L_0x02a9;
    L_0x0295:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "moveto STARTED: ";
        r1.append(r2);
        r1.append(r8);
        r1 = r1.toString();
        android.util.Log.v(r13, r1);
    L_0x02a9:
        r20.performStart();
        r7.dispatchOnFragmentStarted(r8, r15);
    L_0x02af:
        r1 = 4;
        if (r0 <= r1) goto L_0x02d4;
    L_0x02b2:
        r1 = DEBUG;
        if (r1 == 0) goto L_0x02ca;
    L_0x02b6:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "moveto RESUMED: ";
        r1.append(r2);
        r1.append(r8);
        r1 = r1.toString();
        android.util.Log.v(r13, r1);
    L_0x02ca:
        r20.performResume();
        r7.dispatchOnFragmentResumed(r8, r15);
        r8.mSavedFragmentState = r14;
        r8.mSavedViewState = r14;
    L_0x02d4:
        r10 = r22;
        r11 = r23;
        goto L_0x046d;
    L_0x02da:
        r1 = r8.mState;
        if (r1 <= r0) goto L_0x0469;
    L_0x02de:
        r1 = r8.mState;
        if (r1 == r9) goto L_0x0407;
    L_0x02e2:
        if (r1 == r12) goto L_0x0333;
    L_0x02e4:
        if (r1 == r11) goto L_0x0333;
    L_0x02e6:
        r2 = 4;
        if (r1 == r2) goto L_0x0312;
    L_0x02e9:
        r2 = 5;
        if (r1 == r2) goto L_0x02f2;
    L_0x02ec:
        r10 = r22;
        r11 = r23;
        goto L_0x046d;
    L_0x02f2:
        if (r0 >= r2) goto L_0x0312;
    L_0x02f4:
        r1 = DEBUG;
        if (r1 == 0) goto L_0x030c;
    L_0x02f8:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "movefrom RESUMED: ";
        r1.append(r2);
        r1.append(r8);
        r1 = r1.toString();
        android.util.Log.v(r13, r1);
    L_0x030c:
        r20.performPause();
        r7.dispatchOnFragmentPaused(r8, r15);
    L_0x0312:
        r1 = 4;
        if (r0 >= r1) goto L_0x0333;
    L_0x0315:
        r1 = DEBUG;
        if (r1 == 0) goto L_0x032d;
    L_0x0319:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "movefrom STARTED: ";
        r1.append(r2);
        r1.append(r8);
        r1 = r1.toString();
        android.util.Log.v(r13, r1);
    L_0x032d:
        r20.performStop();
        r7.dispatchOnFragmentStopped(r8, r15);
    L_0x0333:
        if (r0 >= r12) goto L_0x0402;
    L_0x0335:
        r1 = DEBUG;
        if (r1 == 0) goto L_0x034d;
    L_0x0339:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "movefrom ACTIVITY_CREATED: ";
        r1.append(r2);
        r1.append(r8);
        r1 = r1.toString();
        android.util.Log.v(r13, r1);
    L_0x034d:
        r1 = r8.mView;
        if (r1 == 0) goto L_0x0360;
    L_0x0351:
        r1 = r7.mHost;
        r1 = r1.onShouldSaveFragmentState(r8);
        if (r1 == 0) goto L_0x0360;
    L_0x0359:
        r1 = r8.mSavedViewState;
        if (r1 != 0) goto L_0x0360;
    L_0x035d:
        r19.saveFragmentViewState(r20);
    L_0x0360:
        r20.performDestroyView();
        r7.dispatchOnFragmentViewDestroyed(r8, r15);
        r1 = r8.mView;
        if (r1 == 0) goto L_0x03f7;
    L_0x036a:
        r1 = r8.mContainer;
        if (r1 == 0) goto L_0x03f7;
    L_0x036e:
        r1 = r19.getTargetSdk();
        r2 = 26;
        if (r1 < r2) goto L_0x0382;
    L_0x0376:
        r1 = r8.mView;
        r1.clearAnimation();
        r1 = r8.mContainer;
        r2 = r8.mView;
        r1.endViewTransition(r2);
    L_0x0382:
        r1 = 0;
        r2 = r7.mCurState;
        if (r2 <= 0) goto L_0x03b2;
    L_0x0387:
        r2 = r7.mDestroyed;
        if (r2 != 0) goto L_0x03b2;
    L_0x038b:
        r2 = r8.mView;
        r2 = r2.getVisibility();
        if (r2 != 0) goto L_0x03ad;
    L_0x0393:
        r2 = r8.mView;
        r2 = r2.getTransitionAlpha();
        r3 = 0;
        r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r2 <= 0) goto L_0x03a8;
    L_0x039e:
        r10 = r22;
        r11 = r23;
        r1 = r7.loadAnimator(r8, r10, r15, r11);
        r12 = r1;
        goto L_0x03b7;
    L_0x03a8:
        r10 = r22;
        r11 = r23;
        goto L_0x03b6;
    L_0x03ad:
        r10 = r22;
        r11 = r23;
        goto L_0x03b6;
    L_0x03b2:
        r10 = r22;
        r11 = r23;
    L_0x03b6:
        r12 = r1;
    L_0x03b7:
        r1 = r8.mView;
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r1.setTransitionAlpha(r2);
        if (r12 == 0) goto L_0x03ef;
    L_0x03c0:
        r5 = r8.mContainer;
        r4 = r8.mView;
        r6 = r20;
        r5.startViewTransition(r4);
        r8.setAnimatingAway(r12);
        r8.setStateAfterAnimating(r0);
        r3 = new android.app.FragmentManagerImpl$2;
        r1 = r3;
        r2 = r19;
        r9 = r3;
        r3 = r5;
        r17 = r4;
        r18 = r5;
        r5 = r20;
        r1.<init>(r3, r4, r5, r6);
        r12.addListener(r9);
        r1 = r8.mView;
        r12.setTarget(r1);
        r1 = r8.mView;
        r7.setHWLayerAnimListenerIfAlpha(r1, r12);
        r12.start();
    L_0x03ef:
        r1 = r8.mContainer;
        r2 = r8.mView;
        r1.removeView(r2);
        goto L_0x03fb;
    L_0x03f7:
        r10 = r22;
        r11 = r23;
    L_0x03fb:
        r8.mContainer = r14;
        r8.mView = r14;
        r8.mInLayout = r15;
        goto L_0x040b;
    L_0x0402:
        r10 = r22;
        r11 = r23;
        goto L_0x040b;
    L_0x0407:
        r10 = r22;
        r11 = r23;
    L_0x040b:
        r1 = 1;
        if (r0 >= r1) goto L_0x046d;
    L_0x040e:
        r1 = r7.mDestroyed;
        if (r1 == 0) goto L_0x0422;
    L_0x0412:
        r1 = r20.getAnimatingAway();
        if (r1 == 0) goto L_0x0422;
    L_0x0418:
        r1 = r20.getAnimatingAway();
        r8.setAnimatingAway(r14);
        r1.cancel();
    L_0x0422:
        r1 = r20.getAnimatingAway();
        if (r1 == 0) goto L_0x042d;
    L_0x0428:
        r8.setStateAfterAnimating(r0);
        r0 = 1;
        goto L_0x046d;
    L_0x042d:
        r1 = DEBUG;
        if (r1 == 0) goto L_0x0445;
    L_0x0431:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "movefrom CREATED: ";
        r1.append(r2);
        r1.append(r8);
        r1 = r1.toString();
        android.util.Log.v(r13, r1);
    L_0x0445:
        r1 = r8.mRetaining;
        if (r1 != 0) goto L_0x0450;
    L_0x0449:
        r20.performDestroy();
        r7.dispatchOnFragmentDestroyed(r8, r15);
        goto L_0x0452;
    L_0x0450:
        r8.mState = r15;
    L_0x0452:
        r20.performDetach();
        r7.dispatchOnFragmentDetached(r8, r15);
        if (r24 != 0) goto L_0x046d;
    L_0x045a:
        r1 = r8.mRetaining;
        if (r1 != 0) goto L_0x0462;
    L_0x045e:
        r19.makeInactive(r20);
        goto L_0x046d;
    L_0x0462:
        r8.mHost = r14;
        r8.mParentFragment = r14;
        r8.mFragmentManager = r14;
        goto L_0x046d;
    L_0x0469:
        r10 = r22;
        r11 = r23;
    L_0x046d:
        r1 = r8.mState;
        if (r1 == r0) goto L_0x0499;
    L_0x0471:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "moveToState: Fragment state for ";
        r1.append(r2);
        r1.append(r8);
        r2 = " not updated inline; expected state ";
        r1.append(r2);
        r1.append(r0);
        r2 = " found ";
        r1.append(r2);
        r2 = r8.mState;
        r1.append(r2);
        r1 = r1.toString();
        android.util.Log.w(r13, r1);
        r8.mState = r0;
    L_0x0499:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.FragmentManagerImpl.moveToState(android.app.Fragment, int, int, int, boolean):void");
    }

    /* Access modifiers changed, original: 0000 */
    public void moveToState(Fragment f) {
        moveToState(f, this.mCurState, 0, 0, false);
    }

    /* Access modifiers changed, original: 0000 */
    public void ensureInflatedFragmentView(Fragment f) {
        if (f.mFromLayout && !f.mPerformedCreateView) {
            f.mView = f.performCreateView(f.performGetLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
            if (f.mView != null) {
                f.mView.setSaveFromParentEnabled(false);
                if (f.mHidden) {
                    f.mView.setVisibility(8);
                }
                f.onViewCreated(f.mView, f.mSavedFragmentState);
                dispatchOnFragmentViewCreated(f, f.mView, f.mSavedFragmentState, false);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void completeShowHideFragment(final Fragment fragment) {
        if (fragment.mView != null) {
            Animator anim = loadAnimator(fragment, fragment.getNextTransition(), fragment.mHidden ^ 1, fragment.getNextTransitionStyle());
            if (anim != null) {
                anim.setTarget(fragment.mView);
                if (!fragment.mHidden) {
                    fragment.mView.setVisibility(0);
                } else if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                } else {
                    final ViewGroup container = fragment.mContainer;
                    final View animatingView = fragment.mView;
                    if (container != null) {
                        container.startViewTransition(animatingView);
                    }
                    anim.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            ViewGroup viewGroup = container;
                            if (viewGroup != null) {
                                viewGroup.endViewTransition(animatingView);
                            }
                            animation.removeListener(this);
                            if (fragment.isHidden()) {
                                animatingView.setVisibility(8);
                            }
                        }
                    });
                }
                setHWLayerAnimListenerIfAlpha(fragment.mView, anim);
                anim.start();
            } else {
                int visibility;
                if (!fragment.mHidden || fragment.isHideReplaced()) {
                    visibility = 0;
                } else {
                    visibility = 8;
                }
                fragment.mView.setVisibility(visibility);
                if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                }
            }
        }
        if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }

    /* Access modifiers changed, original: 0000 */
    public void moveFragmentToExpectedState(Fragment f) {
        if (f != null) {
            int nextState = this.mCurState;
            if (f.mRemoving) {
                if (f.isInBackStack()) {
                    nextState = Math.min(nextState, 1);
                } else {
                    nextState = Math.min(nextState, 0);
                }
            }
            moveToState(f, nextState, f.getNextTransition(), f.getNextTransitionStyle(), false);
            if (f.mView != null) {
                Fragment underFragment = findFragmentUnder(f);
                if (underFragment != null) {
                    View underView = underFragment.mView;
                    ViewGroup container = f.mContainer;
                    int underIndex = container.indexOfChild(underView);
                    int viewIndex = container.indexOfChild(f.mView);
                    if (viewIndex < underIndex) {
                        container.removeViewAt(viewIndex);
                        container.addView(f.mView, underIndex);
                    }
                }
                if (f.mIsNewlyAdded && f.mContainer != null) {
                    f.mView.setTransitionAlpha(1.0f);
                    f.mIsNewlyAdded = false;
                    Animator anim = loadAnimator(f, f.getNextTransition(), true, f.getNextTransitionStyle());
                    if (anim != null) {
                        anim.setTarget(f.mView);
                        setHWLayerAnimListenerIfAlpha(f.mView, anim);
                        anim.start();
                    }
                }
            }
            if (f.mHiddenChanged) {
                completeShowHideFragment(f);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void moveToState(int newState, boolean always) {
        if (this.mHost == null && newState != 0) {
            throw new IllegalStateException("No activity");
        } else if (always || this.mCurState != newState) {
            this.mCurState = newState;
            if (this.mActive != null) {
                int i;
                boolean loadersRunning = false;
                int numAdded = this.mAdded.size();
                for (i = 0; i < numAdded; i++) {
                    Fragment f = (Fragment) this.mAdded.get(i);
                    moveFragmentToExpectedState(f);
                    if (f.mLoaderManager != null) {
                        loadersRunning |= f.mLoaderManager.hasRunningLoaders();
                    }
                }
                i = this.mActive.size();
                for (int i2 = 0; i2 < i; i2++) {
                    Fragment f2 = (Fragment) this.mActive.valueAt(i2);
                    if (f2 != null && ((f2.mRemoving || f2.mDetached) && !f2.mIsNewlyAdded)) {
                        moveFragmentToExpectedState(f2);
                        if (f2.mLoaderManager != null) {
                            loadersRunning |= f2.mLoaderManager.hasRunningLoaders();
                        }
                    }
                }
                if (!loadersRunning) {
                    startPendingDeferredFragments();
                }
                if (this.mNeedMenuInvalidate) {
                    FragmentHostCallback fragmentHostCallback = this.mHost;
                    if (fragmentHostCallback != null && this.mCurState == 5) {
                        fragmentHostCallback.onInvalidateOptionsMenu();
                        this.mNeedMenuInvalidate = false;
                    }
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void startPendingDeferredFragments() {
        if (this.mActive != null) {
            for (int i = 0; i < this.mActive.size(); i++) {
                Fragment f = (Fragment) this.mActive.valueAt(i);
                if (f != null) {
                    performPendingDeferredStart(f);
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void makeActive(Fragment f) {
        if (f.mIndex < 0) {
            int i = this.mNextFragmentIndex;
            this.mNextFragmentIndex = i + 1;
            f.setIndex(i, this.mParent);
            if (this.mActive == null) {
                this.mActive = new SparseArray();
            }
            this.mActive.put(f.mIndex, f);
            if (DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Allocated fragment index ");
                stringBuilder.append(f);
                Log.v(TAG, stringBuilder.toString());
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void makeInactive(Fragment f) {
        if (f.mIndex >= 0) {
            if (DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Freeing fragment index ");
                stringBuilder.append(f);
                Log.v(TAG, stringBuilder.toString());
            }
            this.mActive.put(f.mIndex, null);
            this.mHost.inactivateFragment(f.mWho);
            f.initState();
        }
    }

    public void addFragment(Fragment fragment, boolean moveToStateNow) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("add: ");
            stringBuilder.append(fragment);
            Log.v(TAG, stringBuilder.toString());
        }
        makeActive(fragment);
        if (!fragment.mDetached) {
            if (this.mAdded.contains(fragment)) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Fragment already added: ");
                stringBuilder2.append(fragment);
                throw new IllegalStateException(stringBuilder2.toString());
            }
            synchronized (this.mAdded) {
                this.mAdded.add(fragment);
            }
            fragment.mAdded = true;
            fragment.mRemoving = false;
            if (fragment.mView == null) {
                fragment.mHiddenChanged = false;
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            if (moveToStateNow) {
                moveToState(fragment);
            }
        }
    }

    public void removeFragment(Fragment fragment) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("remove: ");
            stringBuilder.append(fragment);
            stringBuilder.append(" nesting=");
            stringBuilder.append(fragment.mBackStackNesting);
            Log.v(TAG, stringBuilder.toString());
        }
        boolean inactive = fragment.isInBackStack() ^ true;
        if (!fragment.mDetached || inactive) {
            synchronized (this.mAdded) {
                this.mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            fragment.mRemoving = true;
        }
    }

    public void hideFragment(Fragment fragment) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("hide: ");
            stringBuilder.append(fragment);
            Log.v(TAG, stringBuilder.toString());
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            fragment.mHiddenChanged = 1 ^ fragment.mHiddenChanged;
        }
    }

    public void showFragment(Fragment fragment) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("show: ");
            stringBuilder.append(fragment);
            Log.v(TAG, stringBuilder.toString());
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged ^= 1;
        }
    }

    public void detachFragment(Fragment fragment) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("detach: ");
            stringBuilder.append(fragment);
            Log.v(TAG, stringBuilder.toString());
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (DEBUG) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("remove from detach: ");
                    stringBuilder2.append(fragment);
                    Log.v(TAG, stringBuilder2.toString());
                }
                synchronized (this.mAdded) {
                    this.mAdded.remove(fragment);
                }
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                fragment.mAdded = false;
            }
        }
    }

    public void attachFragment(Fragment fragment) {
        StringBuilder stringBuilder;
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("attach: ");
            stringBuilder.append(fragment);
            Log.v(TAG, stringBuilder.toString());
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (!fragment.mAdded) {
                if (this.mAdded.contains(fragment)) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Fragment already added: ");
                    stringBuilder2.append(fragment);
                    throw new IllegalStateException(stringBuilder2.toString());
                }
                if (DEBUG) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("add from attach: ");
                    stringBuilder.append(fragment);
                    Log.v(TAG, stringBuilder.toString());
                }
                synchronized (this.mAdded) {
                    this.mAdded.add(fragment);
                }
                fragment.mAdded = true;
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
            }
        }
    }

    public Fragment findFragmentById(int id) {
        int i;
        Fragment f;
        for (i = this.mAdded.size() - 1; i >= 0; i--) {
            f = (Fragment) this.mAdded.get(i);
            if (f != null && f.mFragmentId == id) {
                return f;
            }
        }
        SparseArray sparseArray = this.mActive;
        if (sparseArray != null) {
            for (i = sparseArray.size() - 1; i >= 0; i--) {
                f = (Fragment) this.mActive.valueAt(i);
                if (f != null && f.mFragmentId == id) {
                    return f;
                }
            }
        }
        return null;
    }

    public Fragment findFragmentByTag(String tag) {
        int i;
        Fragment f;
        if (tag != null) {
            for (i = this.mAdded.size() - 1; i >= 0; i--) {
                f = (Fragment) this.mAdded.get(i);
                if (f != null && tag.equals(f.mTag)) {
                    return f;
                }
            }
        }
        SparseArray sparseArray = this.mActive;
        if (!(sparseArray == null || tag == null)) {
            for (i = sparseArray.size() - 1; i >= 0; i--) {
                f = (Fragment) this.mActive.valueAt(i);
                if (f != null && tag.equals(f.mTag)) {
                    return f;
                }
            }
        }
        return null;
    }

    public Fragment findFragmentByWho(String who) {
        SparseArray sparseArray = this.mActive;
        if (!(sparseArray == null || who == null)) {
            for (int i = sparseArray.size() - 1; i >= 0; i--) {
                Fragment f = (Fragment) this.mActive.valueAt(i);
                if (f != null) {
                    Fragment findFragmentByWho = f.findFragmentByWho(who);
                    f = findFragmentByWho;
                    if (findFragmentByWho != null) {
                        return f;
                    }
                }
            }
        }
        return null;
    }

    private void checkStateLoss() {
        if (this.mStateSaved) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        } else if (this.mNoTransactionsBecause != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can not perform this action inside of ");
            stringBuilder.append(this.mNoTransactionsBecause);
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    public boolean isStateSaved() {
        return this.mStateSaved;
    }

    public void enqueueAction(OpGenerator action, boolean allowStateLoss) {
        if (!allowStateLoss) {
            checkStateLoss();
        }
        synchronized (this) {
            if (!this.mDestroyed) {
                if (this.mHost != null) {
                    if (this.mPendingActions == null) {
                        this.mPendingActions = new ArrayList();
                    }
                    this.mPendingActions.add(action);
                    scheduleCommit();
                    return;
                }
            }
            if (allowStateLoss) {
                return;
            }
            throw new IllegalStateException("Activity has been destroyed");
        }
    }

    private void scheduleCommit() {
        synchronized (this) {
            boolean pendingReady = false;
            boolean postponeReady = (this.mPostponedTransactions == null || this.mPostponedTransactions.isEmpty()) ? false : true;
            if (this.mPendingActions != null && this.mPendingActions.size() == 1) {
                pendingReady = true;
            }
            if (postponeReady || pendingReady) {
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
            }
        }
    }

    public int allocBackStackIndex(BackStackRecord bse) {
        synchronized (this) {
            int index;
            String str;
            StringBuilder stringBuilder;
            if (this.mAvailBackStackIndices != null) {
                if (this.mAvailBackStackIndices.size() > 0) {
                    index = ((Integer) this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1)).intValue();
                    if (DEBUG) {
                        str = TAG;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Adding back stack index ");
                        stringBuilder.append(index);
                        stringBuilder.append(" with ");
                        stringBuilder.append(bse);
                        Log.v(str, stringBuilder.toString());
                    }
                    this.mBackStackIndices.set(index, bse);
                    return index;
                }
            }
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList();
            }
            index = this.mBackStackIndices.size();
            if (DEBUG) {
                str = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Setting back stack index ");
                stringBuilder.append(index);
                stringBuilder.append(" to ");
                stringBuilder.append(bse);
                Log.v(str, stringBuilder.toString());
            }
            this.mBackStackIndices.add(bse);
            return index;
        }
    }

    public void setBackStackIndex(int index, BackStackRecord bse) {
        synchronized (this) {
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList();
            }
            int N = this.mBackStackIndices.size();
            String str;
            StringBuilder stringBuilder;
            if (index < N) {
                if (DEBUG) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Setting back stack index ");
                    stringBuilder.append(index);
                    stringBuilder.append(" to ");
                    stringBuilder.append(bse);
                    Log.v(str, stringBuilder.toString());
                }
                this.mBackStackIndices.set(index, bse);
            } else {
                while (N < index) {
                    this.mBackStackIndices.add(null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList();
                    }
                    if (DEBUG) {
                        str = TAG;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Adding available back stack index ");
                        stringBuilder.append(N);
                        Log.v(str, stringBuilder.toString());
                    }
                    this.mAvailBackStackIndices.add(Integer.valueOf(N));
                    N++;
                }
                if (DEBUG) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Adding back stack index ");
                    stringBuilder.append(index);
                    stringBuilder.append(" with ");
                    stringBuilder.append(bse);
                    Log.v(str, stringBuilder.toString());
                }
                this.mBackStackIndices.add(bse);
            }
        }
    }

    public void freeBackStackIndex(int index) {
        synchronized (this) {
            this.mBackStackIndices.set(index, null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList();
            }
            if (DEBUG) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Freeing back stack index ");
                stringBuilder.append(index);
                Log.v(str, stringBuilder.toString());
            }
            this.mAvailBackStackIndices.add(Integer.valueOf(index));
        }
    }

    private void ensureExecReady(boolean allowStateLoss) {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        } else if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
            if (!allowStateLoss) {
                checkStateLoss();
            }
            if (this.mTmpRecords == null) {
                this.mTmpRecords = new ArrayList();
                this.mTmpIsPop = new ArrayList();
            }
            this.mExecutingActions = true;
            try {
                executePostponedTransaction(null, null);
            } finally {
                this.mExecutingActions = false;
            }
        } else {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        }
    }

    public void execSingleAction(OpGenerator action, boolean allowStateLoss) {
        if (!allowStateLoss || (this.mHost != null && !this.mDestroyed)) {
            ensureExecReady(allowStateLoss);
            if (action.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
                this.mExecutingActions = true;
                try {
                    removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                } finally {
                    cleanupExec();
                }
            }
            doPendingDeferredStart();
            burpActive();
        }
    }

    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }

    public boolean execPendingActions() {
        ensureExecReady(true);
        boolean didSomething = false;
        while (generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                cleanupExec();
                didSomething = true;
            } catch (Throwable th) {
                cleanupExec();
                throw th;
            }
        }
        doPendingDeferredStart();
        burpActive();
        return didSomething;
    }

    private void executePostponedTransaction(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
        int numPostponed = this.mPostponedTransactions;
        numPostponed = numPostponed == 0 ? 0 : numPostponed.size();
        int i = 0;
        while (i < numPostponed) {
            int index;
            StartEnterTransitionListener listener = (StartEnterTransitionListener) this.mPostponedTransactions.get(i);
            if (!(records == null || listener.mIsBack)) {
                index = records.indexOf(listener.mRecord);
                if (index != -1 && ((Boolean) isRecordPop.get(index)).booleanValue()) {
                    listener.cancelTransaction();
                    i++;
                }
            }
            if (listener.isReady() || (records != null && listener.mRecord.interactsWith(records, 0, records.size()))) {
                this.mPostponedTransactions.remove(i);
                i--;
                numPostponed--;
                if (!(records == null || listener.mIsBack)) {
                    index = records.indexOf(listener.mRecord);
                    int index2 = index;
                    if (index != -1 && ((Boolean) isRecordPop.get(index2)).booleanValue()) {
                        listener.cancelTransaction();
                    }
                }
                listener.completeTransaction();
            }
            i++;
        }
    }

    private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
        if (records != null && !records.isEmpty()) {
            if (isRecordPop == null || records.size() != isRecordPop.size()) {
                throw new IllegalStateException("Internal error with the back stack records");
            }
            executePostponedTransaction(records, isRecordPop);
            int numRecords = records.size();
            int startIndex = 0;
            int recordNum = 0;
            while (recordNum < numRecords) {
                if (!((BackStackRecord) records.get(recordNum)).mReorderingAllowed) {
                    if (startIndex != recordNum) {
                        executeOpsTogether(records, isRecordPop, startIndex, recordNum);
                    }
                    int reorderingEnd = recordNum + 1;
                    if (((Boolean) isRecordPop.get(recordNum)).booleanValue()) {
                        while (reorderingEnd < numRecords && ((Boolean) isRecordPop.get(reorderingEnd)).booleanValue() && !((BackStackRecord) records.get(reorderingEnd)).mReorderingAllowed) {
                            reorderingEnd++;
                        }
                    }
                    executeOpsTogether(records, isRecordPop, recordNum, reorderingEnd);
                    startIndex = reorderingEnd;
                    recordNum = reorderingEnd - 1;
                }
                recordNum++;
            }
            if (startIndex != numRecords) {
                executeOpsTogether(records, isRecordPop, startIndex, numRecords);
            }
        }
    }

    private void executeOpsTogether(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex) {
        int postponeIndex;
        ArrayList<BackStackRecord> arrayList = records;
        ArrayList<Boolean> arrayList2 = isRecordPop;
        int i = startIndex;
        int i2 = endIndex;
        boolean allowReordering = ((BackStackRecord) arrayList.get(i)).mReorderingAllowed;
        ArrayList arrayList3 = this.mTmpAddedFragments;
        if (arrayList3 == null) {
            this.mTmpAddedFragments = new ArrayList();
        } else {
            arrayList3.clear();
        }
        this.mTmpAddedFragments.addAll(this.mAdded);
        int recordNum = startIndex;
        boolean addToBackStack = false;
        Fragment oldPrimaryNav = getPrimaryNavigationFragment();
        while (true) {
            boolean z = true;
            if (recordNum >= i2) {
                break;
            }
            BackStackRecord record = (BackStackRecord) arrayList.get(recordNum);
            if (((Boolean) arrayList2.get(recordNum)).booleanValue()) {
                record.trackAddedFragmentsInPop(this.mTmpAddedFragments);
            } else {
                oldPrimaryNav = record.expandOps(this.mTmpAddedFragments, oldPrimaryNav);
            }
            if (!(addToBackStack || record.mAddToBackStack)) {
                z = false;
            }
            addToBackStack = z;
            recordNum++;
        }
        this.mTmpAddedFragments.clear();
        if (!allowReordering) {
            FragmentTransition.startTransitions(this, records, isRecordPop, startIndex, endIndex, false);
        }
        executeOps(records, isRecordPop, startIndex, endIndex);
        int postponeIndex2 = endIndex;
        if (allowReordering) {
            ArraySet<Fragment> addedFragments = new ArraySet();
            addAddedFragments(addedFragments);
            ArraySet<Fragment> addedFragments2 = addedFragments;
            postponeIndex = postponePostponableTransactions(records, isRecordPop, startIndex, endIndex, addedFragments);
            makeRemovedFragmentsInvisible(addedFragments2);
            postponeIndex2 = postponeIndex;
        }
        if (postponeIndex2 != i && allowReordering) {
            FragmentTransition.startTransitions(this, records, isRecordPop, startIndex, postponeIndex2, true);
            moveToState(this.mCurState, true);
        }
        for (postponeIndex = startIndex; postponeIndex < i2; postponeIndex++) {
            BackStackRecord record2 = (BackStackRecord) arrayList.get(postponeIndex);
            if (((Boolean) arrayList2.get(postponeIndex)).booleanValue() && record2.mIndex >= 0) {
                freeBackStackIndex(record2.mIndex);
                record2.mIndex = -1;
            }
            record2.runOnCommitRunnables();
        }
        if (addToBackStack) {
            reportBackStackChanged();
        }
    }

    private void makeRemovedFragmentsInvisible(ArraySet<Fragment> fragments) {
        int numAdded = fragments.size();
        for (int i = 0; i < numAdded; i++) {
            Fragment fragment = (Fragment) fragments.valueAt(i);
            if (!fragment.mAdded) {
                fragment.getView().setTransitionAlpha(0.0f);
            }
        }
    }

    private int postponePostponableTransactions(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex, ArraySet<Fragment> added) {
        int postponeIndex = endIndex;
        for (int i = endIndex - 1; i >= startIndex; i--) {
            BackStackRecord record = (BackStackRecord) records.get(i);
            boolean isPop = ((Boolean) isRecordPop.get(i)).booleanValue();
            boolean isPostponed = record.isPostponed() && !record.interactsWith(records, i + 1, endIndex);
            if (isPostponed) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList();
                }
                StartEnterTransitionListener listener = new StartEnterTransitionListener(record, isPop);
                this.mPostponedTransactions.add(listener);
                record.setOnStartPostponedListener(listener);
                if (isPop) {
                    record.executeOps();
                } else {
                    record.executePopOps(false);
                }
                postponeIndex--;
                if (i != postponeIndex) {
                    records.remove(i);
                    records.add(postponeIndex, record);
                }
                addAddedFragments(added);
            }
        }
        return postponeIndex;
    }

    private void completeExecute(BackStackRecord record, boolean isPop, boolean runTransitions, boolean moveToState) {
        if (isPop) {
            record.executePopOps(moveToState);
        } else {
            record.executeOps();
        }
        ArrayList<BackStackRecord> records = new ArrayList(1);
        ArrayList<Boolean> isRecordPop = new ArrayList(1);
        records.add(record);
        isRecordPop.add(Boolean.valueOf(isPop));
        if (runTransitions) {
            FragmentTransition.startTransitions(this, records, isRecordPop, 0, 1, true);
        }
        if (moveToState) {
            moveToState(this.mCurState, true);
        }
        int numActive = this.mActive;
        if (numActive != 0) {
            numActive = numActive.size();
            for (int i = 0; i < numActive; i++) {
                Fragment fragment = (Fragment) this.mActive.valueAt(i);
                if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded && record.interactsWith(fragment.mContainerId)) {
                    fragment.mIsNewlyAdded = false;
                }
            }
        }
    }

    private Fragment findFragmentUnder(Fragment f) {
        ViewGroup container = f.mContainer;
        View view = f.mView;
        if (container == null || view == null) {
            return null;
        }
        for (int i = this.mAdded.indexOf(f) - 1; i >= 0; i--) {
            Fragment underFragment = (Fragment) this.mAdded.get(i);
            if (underFragment.mContainer == container && underFragment.mView != null) {
                return underFragment;
            }
        }
        return null;
    }

    private static void executeOps(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            BackStackRecord record = (BackStackRecord) records.get(i);
            boolean moveToState = true;
            if (((Boolean) isRecordPop.get(i)).booleanValue()) {
                record.bumpBackStackNesting(-1);
                if (i != endIndex - 1) {
                    moveToState = false;
                }
                record.executePopOps(moveToState);
            } else {
                record.bumpBackStackNesting(1);
                record.executeOps();
            }
        }
    }

    private void addAddedFragments(ArraySet<Fragment> added) {
        int state = this.mCurState;
        if (state >= 1) {
            state = Math.min(state, 4);
            int numAdded = this.mAdded.size();
            for (int i = 0; i < numAdded; i++) {
                Fragment fragment = (Fragment) this.mAdded.get(i);
                if (fragment.mState < state) {
                    moveToState(fragment, state, fragment.getNextAnim(), fragment.getNextTransition(), false);
                    if (!(fragment.mView == null || fragment.mHidden || !fragment.mIsNewlyAdded)) {
                        added.add(fragment);
                    }
                }
            }
        }
    }

    private void forcePostponedTransactions() {
        if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                ((StartEnterTransitionListener) this.mPostponedTransactions.remove(0)).completeTransaction();
            }
        }
    }

    private void endAnimatingAwayFragments() {
        int numFragments = this.mActive;
        numFragments = numFragments == 0 ? 0 : numFragments.size();
        for (int i = 0; i < numFragments; i++) {
            Fragment fragment = (Fragment) this.mActive.valueAt(i);
            if (!(fragment == null || fragment.getAnimatingAway() == null)) {
                fragment.getAnimatingAway().end();
            }
        }
    }

    private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> records, ArrayList<Boolean> isPop) {
        boolean didSomething = false;
        synchronized (this) {
            if (this.mPendingActions != null) {
                if (this.mPendingActions.size() != 0) {
                    for (int i = 0; i < this.mPendingActions.size(); i++) {
                        didSomething |= ((OpGenerator) this.mPendingActions.get(i)).generateOps(records, isPop);
                    }
                    this.mPendingActions.clear();
                    this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                    return didSomething;
                }
            }
            return false;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            boolean loadersRunning = false;
            for (int i = 0; i < this.mActive.size(); i++) {
                Fragment f = (Fragment) this.mActive.valueAt(i);
                if (!(f == null || f.mLoaderManager == null)) {
                    loadersRunning |= f.mLoaderManager.hasRunningLoaders();
                }
            }
            if (!loadersRunning) {
                this.mHavePendingDeferredStart = false;
                startPendingDeferredFragments();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); i++) {
                ((OnBackStackChangedListener) this.mBackStackChangeListeners.get(i)).onBackStackChanged();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void addBackStackState(BackStackRecord state) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList();
        }
        this.mBackStack.add(state);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean popBackStackState(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, String name, int id, int flags) {
        ArrayList arrayList = this.mBackStack;
        if (arrayList == null) {
            return false;
        }
        int last;
        if (name == null && id < 0 && (flags & 1) == 0) {
            last = arrayList.size() - 1;
            if (last < 0) {
                return false;
            }
            records.add((BackStackRecord) this.mBackStack.remove(last));
            isRecordPop.add(Boolean.valueOf(true));
        } else {
            last = -1;
            if (name != null || id >= 0) {
                BackStackRecord bss;
                last = this.mBackStack.size() - 1;
                while (last >= 0) {
                    bss = (BackStackRecord) this.mBackStack.get(last);
                    if ((name != null && name.equals(bss.getName())) || (id >= 0 && id == bss.mIndex)) {
                        break;
                    }
                    last--;
                }
                if (last < 0) {
                    return false;
                }
                if ((flags & 1) != 0) {
                    last--;
                    while (last >= 0) {
                        bss = (BackStackRecord) this.mBackStack.get(last);
                        if ((name == null || !name.equals(bss.getName())) && (id < 0 || id != bss.mIndex)) {
                            break;
                        }
                        last--;
                    }
                }
            }
            if (last == this.mBackStack.size() - 1) {
                return false;
            }
            for (int i = this.mBackStack.size() - 1; i > last; i--) {
                records.add((BackStackRecord) this.mBackStack.remove(i));
                isRecordPop.add(Boolean.valueOf(true));
            }
        }
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public FragmentManagerNonConfig retainNonConfig() {
        setRetaining(this.mSavedNonConfig);
        return this.mSavedNonConfig;
    }

    private static void setRetaining(FragmentManagerNonConfig nonConfig) {
        if (nonConfig != null) {
            List<Fragment> fragments = nonConfig.getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    fragment.mRetaining = true;
                }
            }
            List<FragmentManagerNonConfig> children = nonConfig.getChildNonConfigs();
            if (children != null) {
                for (FragmentManagerNonConfig child : children) {
                    setRetaining(child);
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void saveNonConfig() {
        ArrayList<Fragment> fragments = null;
        ArrayList<FragmentManagerNonConfig> childFragments = null;
        if (this.mActive != null) {
            for (int i = 0; i < this.mActive.size(); i++) {
                Fragment f = (Fragment) this.mActive.valueAt(i);
                if (f != null) {
                    FragmentManagerNonConfig child;
                    if (f.mRetainInstance) {
                        if (fragments == null) {
                            fragments = new ArrayList();
                        }
                        fragments.add(f);
                        f.mTargetIndex = f.mTarget != null ? f.mTarget.mIndex : -1;
                        if (DEBUG) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("retainNonConfig: keeping retained ");
                            stringBuilder.append(f);
                            Log.v(TAG, stringBuilder.toString());
                        }
                    }
                    if (f.mChildFragmentManager != null) {
                        f.mChildFragmentManager.saveNonConfig();
                        child = f.mChildFragmentManager.mSavedNonConfig;
                    } else {
                        child = f.mChildNonConfig;
                    }
                    if (childFragments == null && child != null) {
                        childFragments = new ArrayList(this.mActive.size());
                        for (int j = 0; j < i; j++) {
                            childFragments.add(null);
                        }
                    }
                    if (childFragments != null) {
                        childFragments.add(child);
                    }
                }
            }
        }
        if (fragments == null && childFragments == null) {
            this.mSavedNonConfig = null;
        } else {
            this.mSavedNonConfig = new FragmentManagerNonConfig(fragments, childFragments);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void saveFragmentViewState(Fragment f) {
        if (f.mView != null) {
            SparseArray sparseArray = this.mStateArray;
            if (sparseArray == null) {
                this.mStateArray = new SparseArray();
            } else {
                sparseArray.clear();
            }
            f.mView.saveHierarchyState(this.mStateArray);
            if (this.mStateArray.size() > 0) {
                f.mSavedViewState = this.mStateArray;
                this.mStateArray = null;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Bundle saveFragmentBasicState(Fragment f) {
        Bundle result = null;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        f.performSaveInstanceState(this.mStateBundle);
        dispatchOnFragmentSaveInstanceState(f, this.mStateBundle, false);
        if (!this.mStateBundle.isEmpty()) {
            result = this.mStateBundle;
            this.mStateBundle = null;
        }
        if (f.mView != null) {
            saveFragmentViewState(f);
        }
        if (f.mSavedViewState != null) {
            if (result == null) {
                result = new Bundle();
            }
            result.putSparseParcelableArray(VIEW_STATE_TAG, f.mSavedViewState);
        }
        if (!f.mUserVisibleHint) {
            if (result == null) {
                result = new Bundle();
            }
            result.putBoolean(USER_VISIBLE_HINT_TAG, f.mUserVisibleHint);
        }
        return result;
    }

    /* Access modifiers changed, original: 0000 */
    public Parcelable saveAllState() {
        forcePostponedTransactions();
        endAnimatingAwayFragments();
        execPendingActions();
        this.mStateSaved = true;
        this.mSavedNonConfig = null;
        SparseArray sparseArray = this.mActive;
        if (sparseArray == null || sparseArray.size() <= 0) {
            return null;
        }
        String str;
        String str2;
        String str3;
        String str4;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        StringBuilder stringBuilder3;
        int N = this.mActive.size();
        FragmentState[] active = new FragmentState[N];
        boolean haveFragments = false;
        int i = 0;
        while (true) {
            str = " has cleared index: ";
            str2 = "Failure saving state: active ";
            str3 = ": ";
            str4 = TAG;
            if (i >= N) {
                break;
            }
            Fragment f = (Fragment) this.mActive.valueAt(i);
            if (f != null) {
                if (f.mIndex < 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str2);
                    stringBuilder.append(f);
                    stringBuilder.append(str);
                    stringBuilder.append(f.mIndex);
                    throwException(new IllegalStateException(stringBuilder.toString()));
                }
                haveFragments = true;
                FragmentState fs = new FragmentState(f);
                active[i] = fs;
                if (f.mState <= 0 || fs.mSavedFragmentState != null) {
                    fs.mSavedFragmentState = f.mSavedFragmentState;
                } else {
                    fs.mSavedFragmentState = saveFragmentBasicState(f);
                    if (f.mTarget != null) {
                        if (f.mTarget.mIndex < 0) {
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("Failure saving state: ");
                            stringBuilder2.append(f);
                            stringBuilder2.append(" has target not in fragment manager: ");
                            stringBuilder2.append(f.mTarget);
                            throwException(new IllegalStateException(stringBuilder2.toString()));
                        }
                        if (fs.mSavedFragmentState == null) {
                            fs.mSavedFragmentState = new Bundle();
                        }
                        putFragment(fs.mSavedFragmentState, TARGET_STATE_TAG, f.mTarget);
                        if (f.mTargetRequestCode != 0) {
                            fs.mSavedFragmentState.putInt(TARGET_REQUEST_CODE_STATE_TAG, f.mTargetRequestCode);
                        }
                    }
                }
                if (DEBUG) {
                    stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Saved state of ");
                    stringBuilder3.append(f);
                    stringBuilder3.append(str3);
                    stringBuilder3.append(fs.mSavedFragmentState);
                    Log.v(str4, stringBuilder3.toString());
                }
            }
            i++;
        }
        if (haveFragments) {
            int[] added = null;
            BackStackState[] backStack = null;
            N = this.mAdded.size();
            if (N > 0) {
                added = new int[N];
                for (int i2 = 0; i2 < N; i2++) {
                    added[i2] = ((Fragment) this.mAdded.get(i2)).mIndex;
                    if (added[i2] < 0) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str2);
                        stringBuilder.append(this.mAdded.get(i2));
                        stringBuilder.append(str);
                        stringBuilder.append(added[i2]);
                        throwException(new IllegalStateException(stringBuilder.toString()));
                    }
                    if (DEBUG) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("saveAllState: adding fragment #");
                        stringBuilder2.append(i2);
                        stringBuilder2.append(str3);
                        stringBuilder2.append(this.mAdded.get(i2));
                        Log.v(str4, stringBuilder2.toString());
                    }
                }
            }
            ArrayList arrayList = this.mBackStack;
            if (arrayList != null) {
                N = arrayList.size();
                if (N > 0) {
                    backStack = new BackStackState[N];
                    for (int i3 = 0; i3 < N; i3++) {
                        backStack[i3] = new BackStackState(this, (BackStackRecord) this.mBackStack.get(i3));
                        if (DEBUG) {
                            stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("saveAllState: adding back stack #");
                            stringBuilder3.append(i3);
                            stringBuilder3.append(str3);
                            stringBuilder3.append(this.mBackStack.get(i3));
                            Log.v(str4, stringBuilder3.toString());
                        }
                    }
                }
            }
            FragmentManagerState fms = new FragmentManagerState();
            fms.mActive = active;
            fms.mAdded = added;
            fms.mBackStack = backStack;
            fms.mNextFragmentIndex = this.mNextFragmentIndex;
            Fragment fragment = this.mPrimaryNav;
            if (fragment != null) {
                fms.mPrimaryNavActiveIndex = fragment.mIndex;
            }
            saveNonConfig();
            return fms;
        }
        if (DEBUG) {
            Log.v(str4, "saveAllState: no fragments!");
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public void restoreAllState(Parcelable state, FragmentManagerNonConfig nonConfig) {
        if (state != null) {
            FragmentManagerState fms = (FragmentManagerState) state;
            if (fms.mActive != null) {
                List<Fragment> nonConfigFragments;
                int count;
                int i;
                Fragment f;
                StringBuilder stringBuilder;
                List<FragmentManagerNonConfig> childNonConfigs = null;
                if (nonConfig != null) {
                    nonConfigFragments = nonConfig.getFragments();
                    childNonConfigs = nonConfig.getChildNonConfigs();
                    count = nonConfigFragments != null ? nonConfigFragments.size() : 0;
                    for (i = 0; i < count; i++) {
                        f = (Fragment) nonConfigFragments.get(i);
                        if (DEBUG) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("restoreAllState: re-attaching retained ");
                            stringBuilder.append(f);
                            Log.v(TAG, stringBuilder.toString());
                        }
                        int index = 0;
                        while (index < fms.mActive.length && fms.mActive[index].mIndex != f.mIndex) {
                            index++;
                        }
                        if (index == fms.mActive.length) {
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("Could not find active fragment with index ");
                            stringBuilder2.append(f.mIndex);
                            throwException(new IllegalStateException(stringBuilder2.toString()));
                        }
                        FragmentState fs = fms.mActive[index];
                        fs.mInstance = f;
                        f.mSavedViewState = null;
                        f.mBackStackNesting = 0;
                        f.mInLayout = false;
                        f.mAdded = false;
                        f.mTarget = null;
                        if (fs.mSavedFragmentState != null) {
                            fs.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                            f.mSavedViewState = fs.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
                            f.mSavedFragmentState = fs.mSavedFragmentState;
                        }
                    }
                }
                this.mActive = new SparseArray(fms.mActive.length);
                int i2 = 0;
                while (i2 < fms.mActive.length) {
                    FragmentState fs2 = fms.mActive[i2];
                    if (fs2 != null) {
                        FragmentManagerNonConfig childNonConfig = null;
                        if (childNonConfigs != null && i2 < childNonConfigs.size()) {
                            childNonConfig = (FragmentManagerNonConfig) childNonConfigs.get(i2);
                        }
                        f = fs2.instantiate(this.mHost, this.mContainer, this.mParent, childNonConfig);
                        if (DEBUG) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("restoreAllState: active #");
                            stringBuilder.append(i2);
                            stringBuilder.append(": ");
                            stringBuilder.append(f);
                            Log.v(TAG, stringBuilder.toString());
                        }
                        this.mActive.put(f.mIndex, f);
                        fs2.mInstance = null;
                    }
                    i2++;
                }
                if (nonConfig != null) {
                    nonConfigFragments = nonConfig.getFragments();
                    count = nonConfigFragments != null ? nonConfigFragments.size() : 0;
                    for (i = 0; i < count; i++) {
                        f = (Fragment) nonConfigFragments.get(i);
                        if (f.mTargetIndex >= 0) {
                            f.mTarget = (Fragment) this.mActive.get(f.mTargetIndex);
                            if (f.mTarget == null) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Re-attaching retained fragment ");
                                stringBuilder.append(f);
                                stringBuilder.append(" target no longer exists: ");
                                stringBuilder.append(f.mTargetIndex);
                                Log.w(TAG, stringBuilder.toString());
                                f.mTarget = null;
                            }
                        }
                    }
                }
                this.mAdded.clear();
                if (fms.mAdded != null) {
                    for (i2 = 0; i2 < fms.mAdded.length; i2++) {
                        Fragment f2 = (Fragment) this.mActive.get(fms.mAdded[i2]);
                        if (f2 == null) {
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("No instantiated fragment for index #");
                            stringBuilder3.append(fms.mAdded[i2]);
                            throwException(new IllegalStateException(stringBuilder3.toString()));
                        }
                        f2.mAdded = true;
                        if (DEBUG) {
                            StringBuilder stringBuilder4 = new StringBuilder();
                            stringBuilder4.append("restoreAllState: added #");
                            stringBuilder4.append(i2);
                            stringBuilder4.append(": ");
                            stringBuilder4.append(f2);
                            Log.v(TAG, stringBuilder4.toString());
                        }
                        if (this.mAdded.contains(f2)) {
                            throw new IllegalStateException("Already added!");
                        }
                        synchronized (this.mAdded) {
                            this.mAdded.add(f2);
                        }
                    }
                }
                if (fms.mBackStack != null) {
                    this.mBackStack = new ArrayList(fms.mBackStack.length);
                    for (int i3 = 0; i3 < fms.mBackStack.length; i3++) {
                        BackStackRecord bse = fms.mBackStack[i3].instantiate(this);
                        if (DEBUG) {
                            StringBuilder stringBuilder5 = new StringBuilder();
                            stringBuilder5.append("restoreAllState: back stack #");
                            stringBuilder5.append(i3);
                            stringBuilder5.append(" (index ");
                            stringBuilder5.append(bse.mIndex);
                            stringBuilder5.append("): ");
                            stringBuilder5.append(bse);
                            Log.v(TAG, stringBuilder5.toString());
                            PrintWriter pw = new FastPrintWriter(new LogWriter(2, TAG), false, 1024);
                            bse.dump("  ", pw, false);
                            pw.flush();
                        }
                        this.mBackStack.add(bse);
                        if (bse.mIndex >= 0) {
                            setBackStackIndex(bse.mIndex, bse);
                        }
                    }
                } else {
                    this.mBackStack = null;
                }
                if (fms.mPrimaryNavActiveIndex >= 0) {
                    this.mPrimaryNav = (Fragment) this.mActive.get(fms.mPrimaryNavActiveIndex);
                }
                this.mNextFragmentIndex = fms.mNextFragmentIndex;
            }
        }
    }

    private void burpActive() {
        SparseArray sparseArray = this.mActive;
        if (sparseArray != null) {
            for (int i = sparseArray.size() - 1; i >= 0; i--) {
                if (this.mActive.valueAt(i) == null) {
                    SparseArray sparseArray2 = this.mActive;
                    sparseArray2.delete(sparseArray2.keyAt(i));
                }
            }
        }
    }

    public void attachController(FragmentHostCallback<?> host, FragmentContainer container, Fragment parent) {
        if (this.mHost == null) {
            this.mHost = host;
            this.mContainer = container;
            this.mParent = parent;
            this.mAllowOldReentrantBehavior = getTargetSdk() <= 25;
            return;
        }
        throw new IllegalStateException("Already attached");
    }

    /* Access modifiers changed, original: 0000 */
    public int getTargetSdk() {
        Context context = this.mHost;
        if (context != null) {
            context = context.getContext();
            if (context != null) {
                ApplicationInfo info = context.getApplicationInfo();
                if (info != null) {
                    return info.targetSdkVersion;
                }
            }
        }
        return 0;
    }

    @UnsupportedAppUsage
    public void noteStateNotSaved() {
        this.mSavedNonConfig = null;
        this.mStateSaved = false;
        int addedCount = this.mAdded.size();
        for (int i = 0; i < addedCount; i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null) {
                fragment.noteStateNotSaved();
            }
        }
    }

    public void dispatchCreate() {
        this.mStateSaved = false;
        dispatchMoveToState(1);
    }

    public void dispatchActivityCreated() {
        this.mStateSaved = false;
        dispatchMoveToState(2);
    }

    public void dispatchStart() {
        this.mStateSaved = false;
        dispatchMoveToState(4);
    }

    public void dispatchResume() {
        this.mStateSaved = false;
        dispatchMoveToState(5);
    }

    public void dispatchPause() {
        dispatchMoveToState(4);
    }

    public void dispatchStop() {
        dispatchMoveToState(3);
    }

    public void dispatchDestroyView() {
        dispatchMoveToState(1);
    }

    public void dispatchDestroy() {
        this.mDestroyed = true;
        execPendingActions();
        dispatchMoveToState(0);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
    }

    private void dispatchMoveToState(int state) {
        if (this.mAllowOldReentrantBehavior) {
            moveToState(state, false);
        } else {
            try {
                this.mExecutingActions = true;
                moveToState(state, false);
            } finally {
                this.mExecutingActions = false;
            }
        }
        execPendingActions();
    }

    @Deprecated
    public void dispatchMultiWindowModeChanged(boolean isInMultiWindowMode) {
        for (int i = this.mAdded.size() - 1; i >= 0; i--) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null) {
                f.performMultiWindowModeChanged(isInMultiWindowMode);
            }
        }
    }

    public void dispatchMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        for (int i = this.mAdded.size() - 1; i >= 0; i--) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null) {
                f.performMultiWindowModeChanged(isInMultiWindowMode, newConfig);
            }
        }
    }

    @Deprecated
    public void dispatchPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        for (int i = this.mAdded.size() - 1; i >= 0; i--) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null) {
                f.performPictureInPictureModeChanged(isInPictureInPictureMode);
            }
        }
    }

    public void dispatchPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        for (int i = this.mAdded.size() - 1; i >= 0; i--) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null) {
                f.performPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
            }
        }
    }

    public void dispatchConfigurationChanged(Configuration newConfig) {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null) {
                f.performConfigurationChanged(newConfig);
            }
        }
    }

    public void dispatchLowMemory() {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null) {
                f.performLowMemory();
            }
        }
    }

    public void dispatchTrimMemory(int level) {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null) {
                f.performTrimMemory(level);
            }
        }
    }

    public boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (this.mCurState < 1) {
            return false;
        }
        int i;
        Fragment f;
        boolean show = false;
        ArrayList<Fragment> newMenus = null;
        for (i = 0; i < this.mAdded.size(); i++) {
            f = (Fragment) this.mAdded.get(i);
            if (f != null && f.performCreateOptionsMenu(menu, inflater)) {
                show = true;
                if (newMenus == null) {
                    newMenus = new ArrayList();
                }
                newMenus.add(f);
            }
        }
        if (this.mCreatedMenus != null) {
            for (i = 0; i < this.mCreatedMenus.size(); i++) {
                f = (Fragment) this.mCreatedMenus.get(i);
                if (newMenus == null || !newMenus.contains(f)) {
                    f.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = newMenus;
        return show;
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu) {
        if (this.mCurState < 1) {
            return false;
        }
        boolean show = false;
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null && f.performPrepareOptionsMenu(menu)) {
                show = true;
            }
        }
        return show;
    }

    public boolean dispatchOptionsItemSelected(MenuItem item) {
        if (this.mCurState < 1) {
            return false;
        }
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null && f.performOptionsItemSelected(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean dispatchContextItemSelected(MenuItem item) {
        if (this.mCurState < 1) {
            return false;
        }
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f != null && f.performContextItemSelected(item)) {
                return true;
            }
        }
        return false;
    }

    public void dispatchOptionsMenuClosed(Menu menu) {
        if (this.mCurState >= 1) {
            for (int i = 0; i < this.mAdded.size(); i++) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null) {
                    f.performOptionsMenuClosed(menu);
                }
            }
        }
    }

    public void setPrimaryNavigationFragment(Fragment f) {
        if (f == null || (this.mActive.get(f.mIndex) == f && (f.mHost == null || f.getFragmentManager() == this))) {
            this.mPrimaryNav = f;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment ");
        stringBuilder.append(f);
        stringBuilder.append(" is not an active fragment of FragmentManager ");
        stringBuilder.append(this);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }

    public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb, boolean recursive) {
        this.mLifecycleCallbacks.add(new Pair(cb, Boolean.valueOf(recursive)));
    }

    public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb) {
        synchronized (this.mLifecycleCallbacks) {
            int N = this.mLifecycleCallbacks.size();
            for (int i = 0; i < N; i++) {
                if (((Pair) this.mLifecycleCallbacks.get(i)).first == cb) {
                    this.mLifecycleCallbacks.remove(i);
                    break;
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnFragmentPreAttached(Fragment f, Context context, boolean onlyRecursive) {
        FragmentManager parentManager = this.mParent;
        if (parentManager != null) {
            parentManager = parentManager.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentPreAttached(f, context, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentPreAttached(this, f, context);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnFragmentAttached(Fragment f, Context context, boolean onlyRecursive) {
        FragmentManager parentManager = this.mParent;
        if (parentManager != null) {
            parentManager = parentManager.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentAttached(f, context, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentAttached(this, f, context);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnFragmentPreCreated(Fragment f, Bundle savedInstanceState, boolean onlyRecursive) {
        FragmentManager parentManager = this.mParent;
        if (parentManager != null) {
            parentManager = parentManager.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentPreCreated(f, savedInstanceState, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentPreCreated(this, f, savedInstanceState);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnFragmentCreated(Fragment f, Bundle savedInstanceState, boolean onlyRecursive) {
        FragmentManager parentManager = this.mParent;
        if (parentManager != null) {
            parentManager = parentManager.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentCreated(f, savedInstanceState, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentCreated(this, f, savedInstanceState);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnFragmentActivityCreated(Fragment f, Bundle savedInstanceState, boolean onlyRecursive) {
        FragmentManager parentManager = this.mParent;
        if (parentManager != null) {
            parentManager = parentManager.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentActivityCreated(f, savedInstanceState, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentActivityCreated(this, f, savedInstanceState);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnFragmentViewCreated(Fragment f, View v, Bundle savedInstanceState, boolean onlyRecursive) {
        FragmentManager parentManager = this.mParent;
        if (parentManager != null) {
            parentManager = parentManager.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentViewCreated(f, v, savedInstanceState, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentViewCreated(this, f, v, savedInstanceState);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnFragmentStarted(Fragment f, boolean onlyRecursive) {
        FragmentManager parentManager = this.mParent;
        if (parentManager != null) {
            parentManager = parentManager.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentStarted(f, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentStarted(this, f);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnFragmentResumed(Fragment f, boolean onlyRecursive) {
        FragmentManager parentManager = this.mParent;
        if (parentManager != null) {
            parentManager = parentManager.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentResumed(f, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentResumed(this, f);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnFragmentPaused(Fragment f, boolean onlyRecursive) {
        FragmentManager parentManager = this.mParent;
        if (parentManager != null) {
            parentManager = parentManager.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentPaused(f, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentPaused(this, f);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnFragmentStopped(Fragment f, boolean onlyRecursive) {
        FragmentManager parentManager = this.mParent;
        if (parentManager != null) {
            parentManager = parentManager.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentStopped(f, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentStopped(this, f);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnFragmentSaveInstanceState(Fragment f, Bundle outState, boolean onlyRecursive) {
        FragmentManager parentManager = this.mParent;
        if (parentManager != null) {
            parentManager = parentManager.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentSaveInstanceState(f, outState, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentSaveInstanceState(this, f, outState);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnFragmentViewDestroyed(Fragment f, boolean onlyRecursive) {
        FragmentManager parentManager = this.mParent;
        if (parentManager != null) {
            parentManager = parentManager.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentViewDestroyed(f, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentViewDestroyed(this, f);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnFragmentDestroyed(Fragment f, boolean onlyRecursive) {
        FragmentManager parentManager = this.mParent;
        if (parentManager != null) {
            parentManager = parentManager.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentDestroyed(f, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentDestroyed(this, f);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnFragmentDetached(Fragment f, boolean onlyRecursive) {
        FragmentManager parentManager = this.mParent;
        if (parentManager != null) {
            parentManager = parentManager.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentDetached(f, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
            if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) p.first).onFragmentDetached(this, f);
            }
        }
    }

    public void invalidateOptionsMenu() {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback == null || this.mCurState != 5) {
            this.mNeedMenuInvalidate = true;
        } else {
            fragmentHostCallback.onInvalidateOptionsMenu();
        }
    }

    public static int reverseTransit(int transit) {
        if (transit == 4097) {
            return 8194;
        }
        if (transit == 4099) {
            return 4099;
        }
        if (transit != 8194) {
            return 0;
        }
        return 4097;
    }

    public static int transitToStyleIndex(int transit, boolean enter) {
        int i;
        if (transit == 4097) {
            if (enter) {
                i = 0;
            } else {
                i = 1;
            }
            return i;
        } else if (transit == 4099) {
            if (enter) {
                i = 4;
            } else {
                i = 5;
            }
            return i;
        } else if (transit != 8194) {
            return -1;
        } else {
            if (enter) {
                i = 2;
            } else {
                i = 3;
            }
            return i;
        }
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        Context context2 = context;
        AttributeSet attributeSet = attrs;
        if (!Function.FRAGMENT.equals(name)) {
            return null;
        }
        String fname;
        String fname2 = attributeSet.getAttributeValue(null, Function.CLASS);
        TypedArray a = context2.obtainStyledAttributes(attributeSet, R.styleable.Fragment);
        int i = 0;
        if (fname2 == null) {
            fname = a.getString(0);
        } else {
            fname = fname2;
        }
        int id = a.getResourceId(1, -1);
        String tag = a.getString(2);
        a.recycle();
        if (parent != null) {
            i = parent.getId();
        }
        int containerId = i;
        StringBuilder stringBuilder;
        if (containerId == -1 && id == -1 && tag == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(attrs.getPositionDescription());
            stringBuilder.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
            stringBuilder.append(fname);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        Fragment fragment;
        Fragment fragment2 = id != -1 ? findFragmentById(id) : null;
        if (fragment2 == null && tag != null) {
            fragment2 = findFragmentByTag(tag);
        }
        if (fragment2 == null && containerId != -1) {
            fragment2 = findFragmentById(containerId);
        }
        if (DEBUG) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("onCreateView: id=0x");
            stringBuilder2.append(Integer.toHexString(id));
            stringBuilder2.append(" fname=");
            stringBuilder2.append(fname);
            stringBuilder2.append(" existing=");
            stringBuilder2.append(fragment2);
            Log.v(TAG, stringBuilder2.toString());
        }
        if (fragment2 == null) {
            Fragment fragment3 = this.mContainer.instantiate(context2, fname, null);
            fragment3.mFromLayout = true;
            fragment3.mFragmentId = id != 0 ? id : containerId;
            fragment3.mContainerId = containerId;
            fragment3.mTag = tag;
            fragment3.mInLayout = true;
            fragment3.mFragmentManager = this;
            FragmentHostCallback fragmentHostCallback = this.mHost;
            fragment3.mHost = fragmentHostCallback;
            fragment3.onInflate(fragmentHostCallback.getContext(), attributeSet, fragment3.mSavedFragmentState);
            addFragment(fragment3, true);
            fragment = fragment3;
        } else if (fragment2.mInLayout) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(attrs.getPositionDescription());
            stringBuilder.append(": Duplicate id 0x");
            stringBuilder.append(Integer.toHexString(id));
            stringBuilder.append(", tag ");
            stringBuilder.append(tag);
            stringBuilder.append(", or parent id 0x");
            stringBuilder.append(Integer.toHexString(containerId));
            stringBuilder.append(" with another fragment for ");
            stringBuilder.append(fname);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else {
            fragment2.mInLayout = true;
            fragment2.mHost = this.mHost;
            if (!fragment2.mRetaining) {
                fragment2.onInflate(this.mHost.getContext(), attributeSet, fragment2.mSavedFragmentState);
            }
            fragment = fragment2;
        }
        if (this.mCurState >= 1 || !fragment.mFromLayout) {
            moveToState(fragment);
        } else {
            moveToState(fragment, 1, 0, 0, false);
        }
        if (fragment.mView != null) {
            if (id != 0) {
                fragment.mView.setId(id);
            }
            if (fragment.mView.getTag() == null) {
                fragment.mView.setTag(tag);
            }
            return fragment.mView;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment ");
        stringBuilder.append(fname);
        stringBuilder.append(" did not create a view.");
        throw new IllegalStateException(stringBuilder.toString());
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public Factory2 getLayoutInflaterFactory() {
        return this;
    }
}
