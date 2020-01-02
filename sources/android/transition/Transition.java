package android.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseLongArray;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowId;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public abstract class Transition implements Cloneable {
    static final boolean DBG = false;
    private static final int[] DEFAULT_MATCH_ORDER = new int[]{2, 1, 3, 4};
    private static final String LOG_TAG = "Transition";
    private static final int MATCH_FIRST = 1;
    public static final int MATCH_ID = 3;
    private static final String MATCH_ID_STR = "id";
    public static final int MATCH_INSTANCE = 1;
    private static final String MATCH_INSTANCE_STR = "instance";
    public static final int MATCH_ITEM_ID = 4;
    private static final String MATCH_ITEM_ID_STR = "itemId";
    private static final int MATCH_LAST = 4;
    public static final int MATCH_NAME = 2;
    private static final String MATCH_NAME_STR = "name";
    private static final String MATCH_VIEW_NAME_STR = "viewName";
    private static final PathMotion STRAIGHT_PATH_MOTION = new PathMotion() {
        public Path getPath(float startX, float startY, float endX, float endY) {
            Path path = new Path();
            path.moveTo(startX, startY);
            path.lineTo(endX, endY);
            return path;
        }
    };
    private static ThreadLocal<ArrayMap<Animator, AnimationInfo>> sRunningAnimators = new ThreadLocal();
    ArrayList<Animator> mAnimators = new ArrayList();
    boolean mCanRemoveViews = false;
    private ArrayList<Animator> mCurrentAnimators = new ArrayList();
    long mDuration = -1;
    private TransitionValuesMaps mEndValues = new TransitionValuesMaps();
    ArrayList<TransitionValues> mEndValuesList;
    private boolean mEnded = false;
    EpicenterCallback mEpicenterCallback;
    TimeInterpolator mInterpolator = null;
    ArrayList<TransitionListener> mListeners = null;
    int[] mMatchOrder = DEFAULT_MATCH_ORDER;
    private String mName = getClass().getName();
    ArrayMap<String, String> mNameOverrides;
    int mNumInstances = 0;
    TransitionSet mParent = null;
    PathMotion mPathMotion = STRAIGHT_PATH_MOTION;
    boolean mPaused = false;
    TransitionPropagation mPropagation;
    ViewGroup mSceneRoot = null;
    long mStartDelay = -1;
    private TransitionValuesMaps mStartValues = new TransitionValuesMaps();
    ArrayList<TransitionValues> mStartValuesList;
    ArrayList<View> mTargetChildExcludes = null;
    ArrayList<View> mTargetExcludes = null;
    ArrayList<Integer> mTargetIdChildExcludes = null;
    ArrayList<Integer> mTargetIdExcludes = null;
    ArrayList<Integer> mTargetIds = new ArrayList();
    ArrayList<String> mTargetNameExcludes = null;
    ArrayList<String> mTargetNames = null;
    ArrayList<Class> mTargetTypeChildExcludes = null;
    ArrayList<Class> mTargetTypeExcludes = null;
    ArrayList<Class> mTargetTypes = null;
    ArrayList<View> mTargets = new ArrayList();

    public interface TransitionListener {
        void onTransitionCancel(Transition transition);

        void onTransitionEnd(Transition transition);

        void onTransitionPause(Transition transition);

        void onTransitionResume(Transition transition);

        void onTransitionStart(Transition transition);
    }

    public static class AnimationInfo {
        String name;
        Transition transition;
        TransitionValues values;
        public View view;
        WindowId windowId;

        AnimationInfo(View view, String name, Transition transition, WindowId windowId, TransitionValues values) {
            this.view = view;
            this.name = name;
            this.values = values;
            this.windowId = windowId;
            this.transition = transition;
        }
    }

    private static class ArrayListManager {
        private ArrayListManager() {
        }

        static <T> ArrayList<T> add(ArrayList<T> list, T item) {
            if (list == null) {
                list = new ArrayList();
            }
            if (!list.contains(item)) {
                list.add(item);
            }
            return list;
        }

        static <T> ArrayList<T> remove(ArrayList<T> list, T item) {
            if (list == null) {
                return list;
            }
            list.remove(item);
            if (list.isEmpty()) {
                return null;
            }
            return list;
        }
    }

    public static abstract class EpicenterCallback {
        public abstract Rect onGetEpicenter(Transition transition);
    }

    public abstract void captureEndValues(TransitionValues transitionValues);

    public abstract void captureStartValues(TransitionValues transitionValues);

    public Transition(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Transition);
        long duration = (long) a.getInt(1, -1);
        if (duration >= 0) {
            setDuration(duration);
        }
        long startDelay = (long) a.getInt(2, -1);
        if (startDelay > 0) {
            setStartDelay(startDelay);
        }
        int resID = a.getResourceId(0, 0);
        if (resID > 0) {
            setInterpolator(AnimationUtils.loadInterpolator(context, resID));
        }
        String matchOrder = a.getString(3);
        if (matchOrder != null) {
            setMatchOrder(parseMatchOrder(matchOrder));
        }
        a.recycle();
    }

    private static int[] parseMatchOrder(String matchOrderString) {
        StringTokenizer st = new StringTokenizer(matchOrderString, ",");
        int[] matches = new int[st.countTokens()];
        int index = 0;
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            if ("id".equalsIgnoreCase(token)) {
                matches[index] = 3;
            } else if (MATCH_INSTANCE_STR.equalsIgnoreCase(token)) {
                matches[index] = 1;
            } else if ("name".equalsIgnoreCase(token)) {
                matches[index] = 2;
            } else if (MATCH_VIEW_NAME_STR.equalsIgnoreCase(token)) {
                matches[index] = 2;
            } else if (MATCH_ITEM_ID_STR.equalsIgnoreCase(token)) {
                matches[index] = 4;
            } else if (token.isEmpty()) {
                int[] smallerMatches = new int[(matches.length - 1)];
                System.arraycopy(matches, 0, smallerMatches, 0, index);
                matches = smallerMatches;
                index--;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown match type in matchOrder: '");
                stringBuilder.append(token);
                stringBuilder.append("'");
                throw new InflateException(stringBuilder.toString());
            }
            index++;
        }
        return matches;
    }

    public Transition setDuration(long duration) {
        this.mDuration = duration;
        return this;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public Transition setStartDelay(long startDelay) {
        this.mStartDelay = startDelay;
        return this;
    }

    public long getStartDelay() {
        return this.mStartDelay;
    }

    public Transition setInterpolator(TimeInterpolator interpolator) {
        this.mInterpolator = interpolator;
        return this;
    }

    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    public String[] getTransitionProperties() {
        return null;
    }

    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        return null;
    }

    public void setMatchOrder(int... matches) {
        if (matches == null || matches.length == 0) {
            this.mMatchOrder = DEFAULT_MATCH_ORDER;
            return;
        }
        int i = 0;
        while (i < matches.length) {
            if (!isValidMatch(matches[i])) {
                throw new IllegalArgumentException("matches contains invalid value");
            } else if (alreadyContains(matches, i)) {
                throw new IllegalArgumentException("matches contains a duplicate value");
            } else {
                i++;
            }
        }
        this.mMatchOrder = (int[]) matches.clone();
    }

    private static boolean isValidMatch(int match) {
        return match >= 1 && match <= 4;
    }

    private static boolean alreadyContains(int[] array, int searchIndex) {
        int value = array[searchIndex];
        for (int i = 0; i < searchIndex; i++) {
            if (array[i] == value) {
                return true;
            }
        }
        return false;
    }

    private void matchInstances(ArrayMap<View, TransitionValues> unmatchedStart, ArrayMap<View, TransitionValues> unmatchedEnd) {
        for (int i = unmatchedStart.size() - 1; i >= 0; i--) {
            View view = (View) unmatchedStart.keyAt(i);
            if (view != null && isValidTarget(view)) {
                TransitionValues end = (TransitionValues) unmatchedEnd.remove(view);
                if (end != null && isValidTarget(end.view)) {
                    this.mStartValuesList.add((TransitionValues) unmatchedStart.removeAt(i));
                    this.mEndValuesList.add(end);
                }
            }
        }
    }

    private void matchItemIds(ArrayMap<View, TransitionValues> unmatchedStart, ArrayMap<View, TransitionValues> unmatchedEnd, LongSparseArray<View> startItemIds, LongSparseArray<View> endItemIds) {
        int numStartIds = startItemIds.size();
        for (int i = 0; i < numStartIds; i++) {
            View startView = (View) startItemIds.valueAt(i);
            if (startView != null && isValidTarget(startView)) {
                View endView = (View) endItemIds.get(startItemIds.keyAt(i));
                if (endView != null && isValidTarget(endView)) {
                    TransitionValues startValues = (TransitionValues) unmatchedStart.get(startView);
                    TransitionValues endValues = (TransitionValues) unmatchedEnd.get(endView);
                    if (!(startValues == null || endValues == null)) {
                        this.mStartValuesList.add(startValues);
                        this.mEndValuesList.add(endValues);
                        unmatchedStart.remove(startView);
                        unmatchedEnd.remove(endView);
                    }
                }
            }
        }
    }

    private void matchIds(ArrayMap<View, TransitionValues> unmatchedStart, ArrayMap<View, TransitionValues> unmatchedEnd, SparseArray<View> startIds, SparseArray<View> endIds) {
        int numStartIds = startIds.size();
        for (int i = 0; i < numStartIds; i++) {
            View startView = (View) startIds.valueAt(i);
            if (startView != null && isValidTarget(startView)) {
                View endView = (View) endIds.get(startIds.keyAt(i));
                if (endView != null && isValidTarget(endView)) {
                    TransitionValues startValues = (TransitionValues) unmatchedStart.get(startView);
                    TransitionValues endValues = (TransitionValues) unmatchedEnd.get(endView);
                    if (!(startValues == null || endValues == null)) {
                        this.mStartValuesList.add(startValues);
                        this.mEndValuesList.add(endValues);
                        unmatchedStart.remove(startView);
                        unmatchedEnd.remove(endView);
                    }
                }
            }
        }
    }

    private void matchNames(ArrayMap<View, TransitionValues> unmatchedStart, ArrayMap<View, TransitionValues> unmatchedEnd, ArrayMap<String, View> startNames, ArrayMap<String, View> endNames) {
        int numStartNames = startNames.size();
        for (int i = 0; i < numStartNames; i++) {
            View startView = (View) startNames.valueAt(i);
            if (startView != null && isValidTarget(startView)) {
                View endView = (View) endNames.get(startNames.keyAt(i));
                if (endView != null && isValidTarget(endView)) {
                    TransitionValues startValues = (TransitionValues) unmatchedStart.get(startView);
                    TransitionValues endValues = (TransitionValues) unmatchedEnd.get(endView);
                    if (!(startValues == null || endValues == null)) {
                        this.mStartValuesList.add(startValues);
                        this.mEndValuesList.add(endValues);
                        unmatchedStart.remove(startView);
                        unmatchedEnd.remove(endView);
                    }
                }
            }
        }
    }

    private void addUnmatched(ArrayMap<View, TransitionValues> unmatchedStart, ArrayMap<View, TransitionValues> unmatchedEnd) {
        int i;
        TransitionValues start;
        for (i = 0; i < unmatchedStart.size(); i++) {
            start = (TransitionValues) unmatchedStart.valueAt(i);
            if (isValidTarget(start.view)) {
                this.mStartValuesList.add(start);
                this.mEndValuesList.add(null);
            }
        }
        for (i = 0; i < unmatchedEnd.size(); i++) {
            start = (TransitionValues) unmatchedEnd.valueAt(i);
            if (isValidTarget(start.view)) {
                this.mEndValuesList.add(start);
                this.mStartValuesList.add(null);
            }
        }
    }

    private void matchStartAndEnd(TransitionValuesMaps startValues, TransitionValuesMaps endValues) {
        ArrayMap<View, TransitionValues> unmatchedStart = new ArrayMap(startValues.viewValues);
        ArrayMap<View, TransitionValues> unmatchedEnd = new ArrayMap(endValues.viewValues);
        int i = 0;
        while (true) {
            int[] iArr = this.mMatchOrder;
            if (i < iArr.length) {
                int i2 = iArr[i];
                if (i2 == 1) {
                    matchInstances(unmatchedStart, unmatchedEnd);
                } else if (i2 == 2) {
                    matchNames(unmatchedStart, unmatchedEnd, startValues.nameValues, endValues.nameValues);
                } else if (i2 == 3) {
                    matchIds(unmatchedStart, unmatchedEnd, startValues.idValues, endValues.idValues);
                } else if (i2 == 4) {
                    matchItemIds(unmatchedStart, unmatchedEnd, startValues.itemIdValues, endValues.itemIdValues);
                }
                i++;
            } else {
                addUnmatched(unmatchedStart, unmatchedEnd);
                return;
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void createAnimators(ViewGroup sceneRoot, TransitionValuesMaps startValues, TransitionValuesMaps endValues, ArrayList<TransitionValues> startValuesList, ArrayList<TransitionValues> endValuesList) {
        int startValuesListCount;
        int i;
        ViewGroup viewGroup = sceneRoot;
        ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        long minStartDelay = Long.MAX_VALUE;
        int minAnimator = this.mAnimators.size();
        SparseLongArray startDelays = new SparseLongArray();
        int startValuesListCount2 = startValuesList.size();
        int i2 = 0;
        while (i2 < startValuesListCount2) {
            TransitionValues start;
            TransitionValues end;
            int minAnimator2;
            TransitionValues start2 = (TransitionValues) startValuesList.get(i2);
            TransitionValues end2 = (TransitionValues) endValuesList.get(i2);
            if (start2 == null || start2.targetedTransitions.contains(this)) {
                start = start2;
            } else {
                start = null;
            }
            if (end2 == null || end2.targetedTransitions.contains(this)) {
                end = end2;
            } else {
                end = null;
            }
            if (start == null && end == null) {
                minAnimator2 = minAnimator;
                startValuesListCount = startValuesListCount2;
                i = i2;
            } else {
                boolean z = start == null || end == null || isTransitionRequired(start, end);
                if (z) {
                    Animator animator = createAnimator(viewGroup, start, end);
                    Animator animator2;
                    if (animator != null) {
                        TransitionValues infoValues;
                        if (end != null) {
                            TransitionValues infoValues2;
                            View view = end.view;
                            animator2 = animator;
                            animator = getTransitionProperties();
                            String[] properties;
                            if (animator != null) {
                                infoValues = null;
                                if (animator.length > 0) {
                                    infoValues2 = new TransitionValues(view);
                                    minAnimator2 = minAnimator;
                                    startValuesListCount = startValuesListCount2;
                                    TransitionValues newValues = (TransitionValues) endValues.viewValues.get(view);
                                    if (newValues != null) {
                                        minAnimator = 0;
                                        while (minAnimator < animator.length) {
                                            i = i2;
                                            infoValues = newValues;
                                            infoValues2.values.put(animator[minAnimator], newValues.values.get(animator[minAnimator]));
                                            minAnimator++;
                                            ArrayList<TransitionValues> arrayList = startValuesList;
                                            ArrayList<TransitionValues> arrayList2 = endValuesList;
                                            newValues = infoValues;
                                            i2 = i;
                                        }
                                        i = i2;
                                    } else {
                                        infoValues = newValues;
                                        i = i2;
                                    }
                                    minAnimator = runningAnimators.size();
                                    startValuesListCount2 = 0;
                                    while (startValuesListCount2 < minAnimator) {
                                        AnimationInfo info = (AnimationInfo) runningAnimators.get((Animator) runningAnimators.keyAt(startValuesListCount2));
                                        if (info.values == null || info.view != view) {
                                            properties = animator;
                                        } else {
                                            if (info.name == null && getName() == null) {
                                                properties = animator;
                                            } else {
                                                properties = animator;
                                                if (info.name.equals(getName()) == null) {
                                                    continue;
                                                }
                                            }
                                            if (info.values.equals(infoValues2) != null) {
                                                animator = null;
                                                break;
                                            }
                                        }
                                        startValuesListCount2++;
                                        animator = properties;
                                    }
                                    properties = animator;
                                    animator = animator2;
                                    minAnimator = animator;
                                    startValuesListCount2 = view;
                                    infoValues = infoValues2;
                                } else {
                                    properties = animator;
                                    minAnimator2 = minAnimator;
                                    startValuesListCount = startValuesListCount2;
                                    i = i2;
                                }
                            } else {
                                properties = animator;
                                infoValues = null;
                                minAnimator2 = minAnimator;
                                startValuesListCount = startValuesListCount2;
                                i = i2;
                            }
                            animator = animator2;
                            infoValues2 = infoValues;
                            minAnimator = animator;
                            startValuesListCount2 = view;
                            infoValues = infoValues2;
                        } else {
                            infoValues = null;
                            minAnimator2 = minAnimator;
                            startValuesListCount = startValuesListCount2;
                            i = i2;
                            startValuesListCount2 = start != null ? start.view : null;
                            minAnimator = animator;
                        }
                        if (minAnimator != 0) {
                            animator = this.mPropagation;
                            if (animator != null) {
                                animator = animator.getStartDelay(viewGroup, this, start, end);
                                startDelays.put(this.mAnimators.size(), animator);
                                i2 = Math.min(animator, minStartDelay);
                            } else {
                                i2 = minStartDelay;
                            }
                            runningAnimators.put(minAnimator, new AnimationInfo(startValuesListCount2, getName(), this, sceneRoot.getWindowId(), infoValues));
                            this.mAnimators.add(minAnimator);
                            minStartDelay = i2;
                        }
                    } else {
                        animator2 = animator;
                        TransitionValues transitionValues = end;
                        minAnimator2 = minAnimator;
                        startValuesListCount = startValuesListCount2;
                        i = i2;
                    }
                } else {
                    minAnimator2 = minAnimator;
                    startValuesListCount = startValuesListCount2;
                    i = i2;
                }
            }
            i2 = i + 1;
            minAnimator = minAnimator2;
            startValuesListCount2 = startValuesListCount;
        }
        startValuesListCount = startValuesListCount2;
        i = i2;
        if (startDelays.size() != 0) {
            for (int i3 = 0; i3 < startDelays.size(); i3++) {
                Animator animator3 = (Animator) this.mAnimators.get(startDelays.keyAt(i3));
                animator3.setStartDelay((startDelays.valueAt(i3) - minStartDelay) + animator3.getStartDelay());
            }
        }
    }

    public boolean isValidTarget(View target) {
        if (target == null) {
            return false;
        }
        int targetId = target.getId();
        ArrayList arrayList = this.mTargetIdExcludes;
        if (arrayList != null && arrayList.contains(Integer.valueOf(targetId))) {
            return false;
        }
        arrayList = this.mTargetExcludes;
        if (arrayList != null && arrayList.contains(target)) {
            return false;
        }
        int numTypes = this.mTargetTypeExcludes;
        if (numTypes != 0) {
            numTypes = numTypes.size();
            for (int i = 0; i < numTypes; i++) {
                if (((Class) this.mTargetTypeExcludes.get(i)).isInstance(target)) {
                    return false;
                }
            }
        }
        if (this.mTargetNameExcludes != null && target.getTransitionName() != null && this.mTargetNameExcludes.contains(target.getTransitionName())) {
            return false;
        }
        if (this.mTargetIds.size() == 0 && this.mTargets.size() == 0) {
            arrayList = this.mTargetTypes;
            if (arrayList == null || arrayList.isEmpty()) {
                arrayList = this.mTargetNames;
                if (arrayList == null || arrayList.isEmpty()) {
                    return true;
                }
            }
        }
        if (this.mTargetIds.contains(Integer.valueOf(targetId)) || this.mTargets.contains(target)) {
            return true;
        }
        arrayList = this.mTargetNames;
        if (arrayList != null && arrayList.contains(target.getTransitionName())) {
            return true;
        }
        if (this.mTargetTypes != null) {
            for (numTypes = 0; numTypes < this.mTargetTypes.size(); numTypes++) {
                if (((Class) this.mTargetTypes.get(numTypes)).isInstance(target)) {
                    return true;
                }
            }
        }
        return false;
    }

    @UnsupportedAppUsage
    private static ArrayMap<Animator, AnimationInfo> getRunningAnimators() {
        ArrayMap<Animator, AnimationInfo> runningAnimators = (ArrayMap) sRunningAnimators.get();
        if (runningAnimators != null) {
            return runningAnimators;
        }
        runningAnimators = new ArrayMap();
        sRunningAnimators.set(runningAnimators);
        return runningAnimators;
    }

    /* Access modifiers changed, original: protected */
    public void runAnimators() {
        start();
        ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        Iterator it = this.mAnimators.iterator();
        while (it.hasNext()) {
            Animator anim = (Animator) it.next();
            if (runningAnimators.containsKey(anim)) {
                start();
                runAnimator(anim, runningAnimators);
            }
        }
        this.mAnimators.clear();
        end();
    }

    private void runAnimator(Animator animator, final ArrayMap<Animator, AnimationInfo> runningAnimators) {
        if (animator != null) {
            animator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationStart(Animator animation) {
                    Transition.this.mCurrentAnimators.add(animation);
                }

                public void onAnimationEnd(Animator animation) {
                    runningAnimators.remove(animation);
                    Transition.this.mCurrentAnimators.remove(animation);
                }
            });
            animate(animator);
        }
    }

    public Transition addTarget(int targetId) {
        if (targetId > 0) {
            this.mTargetIds.add(Integer.valueOf(targetId));
        }
        return this;
    }

    public Transition addTarget(String targetName) {
        if (targetName != null) {
            if (this.mTargetNames == null) {
                this.mTargetNames = new ArrayList();
            }
            this.mTargetNames.add(targetName);
        }
        return this;
    }

    public Transition addTarget(Class targetType) {
        if (targetType != null) {
            if (this.mTargetTypes == null) {
                this.mTargetTypes = new ArrayList();
            }
            this.mTargetTypes.add(targetType);
        }
        return this;
    }

    public Transition removeTarget(int targetId) {
        if (targetId > 0) {
            this.mTargetIds.remove(Integer.valueOf(targetId));
        }
        return this;
    }

    public Transition removeTarget(String targetName) {
        if (targetName != null) {
            ArrayList arrayList = this.mTargetNames;
            if (arrayList != null) {
                arrayList.remove(targetName);
            }
        }
        return this;
    }

    public Transition excludeTarget(int targetId, boolean exclude) {
        if (targetId >= 0) {
            this.mTargetIdExcludes = excludeObject(this.mTargetIdExcludes, Integer.valueOf(targetId), exclude);
        }
        return this;
    }

    public Transition excludeTarget(String targetName, boolean exclude) {
        this.mTargetNameExcludes = excludeObject(this.mTargetNameExcludes, targetName, exclude);
        return this;
    }

    public Transition excludeChildren(int targetId, boolean exclude) {
        if (targetId >= 0) {
            this.mTargetIdChildExcludes = excludeObject(this.mTargetIdChildExcludes, Integer.valueOf(targetId), exclude);
        }
        return this;
    }

    public Transition excludeTarget(View target, boolean exclude) {
        this.mTargetExcludes = excludeObject(this.mTargetExcludes, target, exclude);
        return this;
    }

    public Transition excludeChildren(View target, boolean exclude) {
        this.mTargetChildExcludes = excludeObject(this.mTargetChildExcludes, target, exclude);
        return this;
    }

    private static <T> ArrayList<T> excludeObject(ArrayList<T> list, T target, boolean exclude) {
        if (target == null) {
            return list;
        }
        if (exclude) {
            return ArrayListManager.add(list, target);
        }
        return ArrayListManager.remove(list, target);
    }

    public Transition excludeTarget(Class type, boolean exclude) {
        this.mTargetTypeExcludes = excludeObject(this.mTargetTypeExcludes, type, exclude);
        return this;
    }

    public Transition excludeChildren(Class type, boolean exclude) {
        this.mTargetTypeChildExcludes = excludeObject(this.mTargetTypeChildExcludes, type, exclude);
        return this;
    }

    public Transition addTarget(View target) {
        this.mTargets.add(target);
        return this;
    }

    public Transition removeTarget(View target) {
        if (target != null) {
            this.mTargets.remove(target);
        }
        return this;
    }

    public Transition removeTarget(Class target) {
        if (target != null) {
            this.mTargetTypes.remove(target);
        }
        return this;
    }

    public List<Integer> getTargetIds() {
        return this.mTargetIds;
    }

    public List<View> getTargets() {
        return this.mTargets;
    }

    public List<String> getTargetNames() {
        return this.mTargetNames;
    }

    public List<String> getTargetViewNames() {
        return this.mTargetNames;
    }

    public List<Class> getTargetTypes() {
        return this.mTargetTypes;
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:62:? A:{SYNTHETIC, RETURN, ORIG_RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a7  */
    public void captureValues(android.view.ViewGroup r7, boolean r8) {
        /*
        r6 = this;
        r6.clearValues(r8);
        r0 = r6.mTargetIds;
        r0 = r0.size();
        if (r0 > 0) goto L_0x0013;
    L_0x000b:
        r0 = r6.mTargets;
        r0 = r0.size();
        if (r0 <= 0) goto L_0x0028;
    L_0x0013:
        r0 = r6.mTargetNames;
        if (r0 == 0) goto L_0x001d;
    L_0x0017:
        r0 = r0.isEmpty();
        if (r0 == 0) goto L_0x0028;
    L_0x001d:
        r0 = r6.mTargetTypes;
        if (r0 == 0) goto L_0x002d;
    L_0x0021:
        r0 = r0.isEmpty();
        if (r0 == 0) goto L_0x0028;
    L_0x0027:
        goto L_0x002d;
    L_0x0028:
        r6.captureHierarchy(r7, r8);
        goto L_0x00a5;
    L_0x002d:
        r0 = 0;
    L_0x002e:
        r1 = r6.mTargetIds;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x006e;
    L_0x0036:
        r1 = r6.mTargetIds;
        r1 = r1.get(r0);
        r1 = (java.lang.Integer) r1;
        r1 = r1.intValue();
        r2 = r7.findViewById(r1);
        if (r2 == 0) goto L_0x006b;
    L_0x0048:
        r3 = new android.transition.TransitionValues;
        r3.<init>(r2);
        if (r8 == 0) goto L_0x0053;
    L_0x004f:
        r6.captureStartValues(r3);
        goto L_0x0056;
    L_0x0053:
        r6.captureEndValues(r3);
    L_0x0056:
        r4 = r3.targetedTransitions;
        r4.add(r6);
        r6.capturePropagationValues(r3);
        if (r8 == 0) goto L_0x0066;
    L_0x0060:
        r4 = r6.mStartValues;
        addViewValues(r4, r2, r3);
        goto L_0x006b;
    L_0x0066:
        r4 = r6.mEndValues;
        addViewValues(r4, r2, r3);
    L_0x006b:
        r0 = r0 + 1;
        goto L_0x002e;
    L_0x006e:
        r0 = 0;
    L_0x006f:
        r1 = r6.mTargets;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x00a5;
    L_0x0077:
        r1 = r6.mTargets;
        r1 = r1.get(r0);
        r1 = (android.view.View) r1;
        r2 = new android.transition.TransitionValues;
        r2.<init>(r1);
        if (r8 == 0) goto L_0x008a;
    L_0x0086:
        r6.captureStartValues(r2);
        goto L_0x008d;
    L_0x008a:
        r6.captureEndValues(r2);
    L_0x008d:
        r3 = r2.targetedTransitions;
        r3.add(r6);
        r6.capturePropagationValues(r2);
        if (r8 == 0) goto L_0x009d;
    L_0x0097:
        r3 = r6.mStartValues;
        addViewValues(r3, r1, r2);
        goto L_0x00a2;
    L_0x009d:
        r3 = r6.mEndValues;
        addViewValues(r3, r1, r2);
    L_0x00a2:
        r0 = r0 + 1;
        goto L_0x006f;
    L_0x00a5:
        if (r8 != 0) goto L_0x00ec;
    L_0x00a7:
        r0 = r6.mNameOverrides;
        if (r0 == 0) goto L_0x00ec;
    L_0x00ab:
        r0 = r0.size();
        r1 = new java.util.ArrayList;
        r1.<init>(r0);
        r2 = 0;
    L_0x00b5:
        if (r2 >= r0) goto L_0x00cf;
    L_0x00b7:
        r3 = r6.mNameOverrides;
        r3 = r3.keyAt(r2);
        r3 = (java.lang.String) r3;
        r4 = r6.mStartValues;
        r4 = r4.nameValues;
        r4 = r4.remove(r3);
        r4 = (android.view.View) r4;
        r1.add(r4);
        r2 = r2 + 1;
        goto L_0x00b5;
    L_0x00cf:
        r2 = 0;
    L_0x00d0:
        if (r2 >= r0) goto L_0x00ec;
    L_0x00d2:
        r3 = r1.get(r2);
        r3 = (android.view.View) r3;
        if (r3 == 0) goto L_0x00e9;
    L_0x00da:
        r4 = r6.mNameOverrides;
        r4 = r4.valueAt(r2);
        r4 = (java.lang.String) r4;
        r5 = r6.mStartValues;
        r5 = r5.nameValues;
        r5.put(r4, r3);
    L_0x00e9:
        r2 = r2 + 1;
        goto L_0x00d0;
    L_0x00ec:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.transition.Transition.captureValues(android.view.ViewGroup, boolean):void");
    }

    static void addViewValues(TransitionValuesMaps transitionValuesMaps, View view, TransitionValues transitionValues) {
        transitionValuesMaps.viewValues.put(view, transitionValues);
        int id = view.getId();
        if (id >= 0) {
            if (transitionValuesMaps.idValues.indexOfKey(id) >= 0) {
                transitionValuesMaps.idValues.put(id, null);
            } else {
                transitionValuesMaps.idValues.put(id, view);
            }
        }
        String name = view.getTransitionName();
        if (name != null) {
            if (transitionValuesMaps.nameValues.containsKey(name)) {
                transitionValuesMaps.nameValues.put(name, null);
            } else {
                transitionValuesMaps.nameValues.put(name, view);
            }
        }
        if (view.getParent() instanceof ListView) {
            ListView listview = (ListView) view.getParent();
            if (listview.getAdapter().hasStableIds()) {
                long itemId = listview.getItemIdAtPosition(listview.getPositionForView(view));
                if (transitionValuesMaps.itemIdValues.indexOfKey(itemId) >= 0) {
                    View alreadyMatched = (View) transitionValuesMaps.itemIdValues.get(itemId);
                    if (alreadyMatched != null) {
                        alreadyMatched.setHasTransientState(false);
                        transitionValuesMaps.itemIdValues.put(itemId, null);
                        return;
                    }
                    return;
                }
                view.setHasTransientState(true);
                transitionValuesMaps.itemIdValues.put(itemId, view);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void clearValues(boolean start) {
        if (start) {
            this.mStartValues.viewValues.clear();
            this.mStartValues.idValues.clear();
            this.mStartValues.itemIdValues.clear();
            this.mStartValues.nameValues.clear();
            this.mStartValuesList = null;
            return;
        }
        this.mEndValues.viewValues.clear();
        this.mEndValues.idValues.clear();
        this.mEndValues.itemIdValues.clear();
        this.mEndValues.nameValues.clear();
        this.mEndValuesList = null;
    }

    private void captureHierarchy(View view, boolean start) {
        if (view != null) {
            int id = view.getId();
            ArrayList arrayList = this.mTargetIdExcludes;
            if (arrayList == null || !arrayList.contains(Integer.valueOf(id))) {
                arrayList = this.mTargetExcludes;
                if (arrayList == null || !arrayList.contains(view)) {
                    int i;
                    int numTypes = this.mTargetTypeExcludes;
                    if (numTypes != 0) {
                        numTypes = numTypes.size();
                        i = 0;
                        while (i < numTypes) {
                            if (!((Class) this.mTargetTypeExcludes.get(i)).isInstance(view)) {
                                i++;
                            } else {
                                return;
                            }
                        }
                    }
                    if (view.getParent() instanceof ViewGroup) {
                        TransitionValues values = new TransitionValues(view);
                        if (start) {
                            captureStartValues(values);
                        } else {
                            captureEndValues(values);
                        }
                        values.targetedTransitions.add(this);
                        capturePropagationValues(values);
                        if (start) {
                            addViewValues(this.mStartValues, view, values);
                        } else {
                            addViewValues(this.mEndValues, view, values);
                        }
                    }
                    if (view instanceof ViewGroup) {
                        arrayList = this.mTargetIdChildExcludes;
                        if (arrayList == null || !arrayList.contains(Integer.valueOf(id))) {
                            arrayList = this.mTargetChildExcludes;
                            if (arrayList == null || !arrayList.contains(view)) {
                                numTypes = this.mTargetTypeChildExcludes;
                                if (numTypes != 0) {
                                    numTypes = numTypes.size();
                                    i = 0;
                                    while (i < numTypes) {
                                        if (!((Class) this.mTargetTypeChildExcludes.get(i)).isInstance(view)) {
                                            i++;
                                        } else {
                                            return;
                                        }
                                    }
                                }
                                ViewGroup parent = (ViewGroup) view;
                                for (i = 0; i < parent.getChildCount(); i++) {
                                    captureHierarchy(parent.getChildAt(i), start);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public TransitionValues getTransitionValues(View view, boolean start) {
        TransitionSet transitionSet = this.mParent;
        if (transitionSet != null) {
            return transitionSet.getTransitionValues(view, start);
        }
        return (TransitionValues) (start ? this.mStartValues : this.mEndValues).viewValues.get(view);
    }

    /* Access modifiers changed, original: 0000 */
    public TransitionValues getMatchedTransitionValues(View view, boolean viewInStart) {
        TransitionSet transitionSet = this.mParent;
        if (transitionSet != null) {
            return transitionSet.getMatchedTransitionValues(view, viewInStart);
        }
        ArrayList<TransitionValues> lookIn = viewInStart ? this.mStartValuesList : this.mEndValuesList;
        if (lookIn == null) {
            return null;
        }
        int count = lookIn.size();
        int index = -1;
        for (int i = 0; i < count; i++) {
            TransitionValues values = (TransitionValues) lookIn.get(i);
            if (values == null) {
                return null;
            }
            if (values.view == view) {
                index = i;
                break;
            }
        }
        TransitionValues values2 = null;
        if (index >= 0) {
            values2 = (TransitionValues) (viewInStart ? this.mEndValuesList : this.mStartValuesList).get(index);
        }
        return values2;
    }

    public void pause(View sceneRoot) {
        if (!this.mEnded) {
            int i;
            ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
            int numOldAnims = runningAnimators.size();
            if (sceneRoot != null) {
                WindowId windowId = sceneRoot.getWindowId();
                for (i = numOldAnims - 1; i >= 0; i--) {
                    AnimationInfo info = (AnimationInfo) runningAnimators.valueAt(i);
                    if (!(info.view == null || windowId == null || !windowId.equals(info.windowId))) {
                        ((Animator) runningAnimators.keyAt(i)).pause();
                    }
                }
            }
            ArrayList arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                ArrayList<TransitionListener> tmpListeners = (ArrayList) this.mListeners.clone();
                i = tmpListeners.size();
                for (int i2 = 0; i2 < i; i2++) {
                    ((TransitionListener) tmpListeners.get(i2)).onTransitionPause(this);
                }
            }
            this.mPaused = true;
        }
    }

    public void resume(View sceneRoot) {
        if (this.mPaused) {
            if (!this.mEnded) {
                ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
                int numOldAnims = runningAnimators.size();
                WindowId windowId = sceneRoot.getWindowId();
                for (int i = numOldAnims - 1; i >= 0; i--) {
                    AnimationInfo info = (AnimationInfo) runningAnimators.valueAt(i);
                    if (!(info.view == null || windowId == null || !windowId.equals(info.windowId))) {
                        ((Animator) runningAnimators.keyAt(i)).resume();
                    }
                }
                ArrayList arrayList = this.mListeners;
                if (arrayList != null && arrayList.size() > 0) {
                    ArrayList<TransitionListener> tmpListeners = (ArrayList) this.mListeners.clone();
                    int numListeners = tmpListeners.size();
                    for (int i2 = 0; i2 < numListeners; i2++) {
                        ((TransitionListener) tmpListeners.get(i2)).onTransitionResume(this);
                    }
                }
            }
            this.mPaused = false;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void playTransition(ViewGroup sceneRoot) {
        this.mStartValuesList = new ArrayList();
        this.mEndValuesList = new ArrayList();
        matchStartAndEnd(this.mStartValues, this.mEndValues);
        ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        int numOldAnims = runningAnimators.size();
        WindowId windowId = sceneRoot.getWindowId();
        for (int i = numOldAnims - 1; i >= 0; i--) {
            Animator anim = (Animator) runningAnimators.keyAt(i);
            if (anim != null) {
                AnimationInfo oldInfo = (AnimationInfo) runningAnimators.get(anim);
                if (!(oldInfo == null || oldInfo.view == null || oldInfo.windowId != windowId)) {
                    TransitionValues oldValues = oldInfo.values;
                    View oldView = oldInfo.view;
                    boolean cancel = true;
                    TransitionValues startValues = getTransitionValues(oldView, true);
                    TransitionValues endValues = getMatchedTransitionValues(oldView, true);
                    if (startValues == null && endValues == null) {
                        endValues = (TransitionValues) this.mEndValues.viewValues.get(oldView);
                    }
                    if ((startValues == null && endValues == null) || !oldInfo.transition.isTransitionRequired(oldValues, endValues)) {
                        cancel = false;
                    }
                    if (cancel) {
                        if (anim.isRunning() || anim.isStarted()) {
                            anim.cancel();
                        } else {
                            runningAnimators.remove(anim);
                        }
                    }
                }
            }
        }
        createAnimators(sceneRoot, this.mStartValues, this.mEndValues, this.mStartValuesList, this.mEndValuesList);
        runAnimators();
    }

    public boolean isTransitionRequired(TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return false;
        }
        String[] properties = getTransitionProperties();
        if (properties != null) {
            for (String isValueChanged : properties) {
                if (isValueChanged(startValues, endValues, isValueChanged)) {
                    return true;
                }
            }
            return false;
        }
        for (String key : startValues.values.keySet()) {
            if (isValueChanged(startValues, endValues, key)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValueChanged(TransitionValues oldValues, TransitionValues newValues, String key) {
        if (oldValues.values.containsKey(key) != newValues.values.containsKey(key)) {
            return false;
        }
        boolean changed;
        Object oldValue = oldValues.values.get(key);
        Object newValue = newValues.values.get(key);
        if (oldValue == null && newValue == null) {
            changed = false;
        } else if (oldValue == null || newValue == null) {
            changed = true;
        } else {
            changed = oldValue.equals(newValue) ^ 1;
        }
        return changed;
    }

    /* Access modifiers changed, original: protected */
    public void animate(Animator animator) {
        if (animator == null) {
            end();
            return;
        }
        if (getDuration() >= 0) {
            animator.setDuration(getDuration());
        }
        if (getStartDelay() >= 0) {
            animator.setStartDelay(getStartDelay() + animator.getStartDelay());
        }
        if (getInterpolator() != null) {
            animator.setInterpolator(getInterpolator());
        }
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                Transition.this.end();
                animation.removeListener(this);
            }
        });
        animator.start();
    }

    /* Access modifiers changed, original: protected */
    public void start() {
        if (this.mNumInstances == 0) {
            ArrayList arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                ArrayList<TransitionListener> tmpListeners = (ArrayList) this.mListeners.clone();
                int numListeners = tmpListeners.size();
                for (int i = 0; i < numListeners; i++) {
                    ((TransitionListener) tmpListeners.get(i)).onTransitionStart(this);
                }
            }
            this.mEnded = false;
        }
        this.mNumInstances++;
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void end() {
        this.mNumInstances--;
        if (this.mNumInstances == 0) {
            int i;
            View view;
            ArrayList arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                ArrayList<TransitionListener> tmpListeners = (ArrayList) this.mListeners.clone();
                int numListeners = tmpListeners.size();
                for (int i2 = 0; i2 < numListeners; i2++) {
                    ((TransitionListener) tmpListeners.get(i2)).onTransitionEnd(this);
                }
            }
            for (i = 0; i < this.mStartValues.itemIdValues.size(); i++) {
                view = (View) this.mStartValues.itemIdValues.valueAt(i);
                if (view != null) {
                    view.setHasTransientState(false);
                }
            }
            for (i = 0; i < this.mEndValues.itemIdValues.size(); i++) {
                view = (View) this.mEndValues.itemIdValues.valueAt(i);
                if (view != null) {
                    view.setHasTransientState(false);
                }
            }
            this.mEnded = true;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void forceToEnd(ViewGroup sceneRoot) {
        ArrayMap runningAnimators = getRunningAnimators();
        int numOldAnims = runningAnimators.size();
        if (sceneRoot != null && numOldAnims != 0) {
            WindowId windowId = sceneRoot.getWindowId();
            ArrayMap<Animator, AnimationInfo> oldAnimators = new ArrayMap(runningAnimators);
            runningAnimators.clear();
            for (int i = numOldAnims - 1; i >= 0; i--) {
                AnimationInfo info = (AnimationInfo) oldAnimators.valueAt(i);
                if (!(info.view == null || windowId == null || !windowId.equals(info.windowId))) {
                    ((Animator) oldAnimators.keyAt(i)).end();
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void cancel() {
        for (int i = this.mCurrentAnimators.size() - 1; i >= 0; i--) {
            ((Animator) this.mCurrentAnimators.get(i)).cancel();
        }
        ArrayList arrayList = this.mListeners;
        if (arrayList != null && arrayList.size() > 0) {
            ArrayList<TransitionListener> tmpListeners = (ArrayList) this.mListeners.clone();
            int numListeners = tmpListeners.size();
            for (int i2 = 0; i2 < numListeners; i2++) {
                ((TransitionListener) tmpListeners.get(i2)).onTransitionCancel(this);
            }
        }
    }

    public Transition addListener(TransitionListener listener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(listener);
        return this;
    }

    public Transition removeListener(TransitionListener listener) {
        ArrayList arrayList = this.mListeners;
        if (arrayList == null) {
            return this;
        }
        arrayList.remove(listener);
        if (this.mListeners.size() == 0) {
            this.mListeners = null;
        }
        return this;
    }

    public void setEpicenterCallback(EpicenterCallback epicenterCallback) {
        this.mEpicenterCallback = epicenterCallback;
    }

    public EpicenterCallback getEpicenterCallback() {
        return this.mEpicenterCallback;
    }

    public Rect getEpicenter() {
        EpicenterCallback epicenterCallback = this.mEpicenterCallback;
        if (epicenterCallback == null) {
            return null;
        }
        return epicenterCallback.onGetEpicenter(this);
    }

    public void setPathMotion(PathMotion pathMotion) {
        if (pathMotion == null) {
            this.mPathMotion = STRAIGHT_PATH_MOTION;
        } else {
            this.mPathMotion = pathMotion;
        }
    }

    public PathMotion getPathMotion() {
        return this.mPathMotion;
    }

    public void setPropagation(TransitionPropagation transitionPropagation) {
        this.mPropagation = transitionPropagation;
    }

    public TransitionPropagation getPropagation() {
        return this.mPropagation;
    }

    /* Access modifiers changed, original: 0000 */
    public void capturePropagationValues(TransitionValues transitionValues) {
        if (!(this.mPropagation == null || transitionValues.values.isEmpty())) {
            String[] propertyNames = this.mPropagation.getPropagationProperties();
            if (propertyNames != null) {
                boolean containsAll = true;
                for (Object containsKey : propertyNames) {
                    if (!transitionValues.values.containsKey(containsKey)) {
                        containsAll = false;
                        break;
                    }
                }
                if (!containsAll) {
                    this.mPropagation.captureValues(transitionValues);
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Transition setSceneRoot(ViewGroup sceneRoot) {
        this.mSceneRoot = sceneRoot;
        return this;
    }

    /* Access modifiers changed, original: 0000 */
    public void setCanRemoveViews(boolean canRemoveViews) {
        this.mCanRemoveViews = canRemoveViews;
    }

    public boolean canRemoveViews() {
        return this.mCanRemoveViews;
    }

    public void setNameOverrides(ArrayMap<String, String> overrides) {
        this.mNameOverrides = overrides;
    }

    public ArrayMap<String, String> getNameOverrides() {
        return this.mNameOverrides;
    }

    public String toString() {
        return toString("");
    }

    public Transition clone() {
        Transition clone = null;
        try {
            clone = (Transition) super.clone();
            clone.mAnimators = new ArrayList();
            clone.mStartValues = new TransitionValuesMaps();
            clone.mEndValues = new TransitionValuesMaps();
            clone.mStartValuesList = null;
            clone.mEndValuesList = null;
            return clone;
        } catch (CloneNotSupportedException e) {
            return clone;
        }
    }

    public String getName() {
        return this.mName;
    }

    /* Access modifiers changed, original: 0000 */
    public String toString(String indent) {
        StringBuilder stringBuilder;
        String result = new StringBuilder();
        result.append(indent);
        result.append(getClass().getSimpleName());
        result.append("@");
        result.append(Integer.toHexString(hashCode()));
        result.append(": ");
        result = result.toString();
        String str = ") ";
        if (this.mDuration != -1) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(result);
            stringBuilder.append("dur(");
            stringBuilder.append(this.mDuration);
            stringBuilder.append(str);
            result = stringBuilder.toString();
        }
        if (this.mStartDelay != -1) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(result);
            stringBuilder.append("dly(");
            stringBuilder.append(this.mStartDelay);
            stringBuilder.append(str);
            result = stringBuilder.toString();
        }
        if (this.mInterpolator != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(result);
            stringBuilder.append("interp(");
            stringBuilder.append(this.mInterpolator);
            stringBuilder.append(str);
            result = stringBuilder.toString();
        }
        if (this.mTargetIds.size() <= 0 && this.mTargets.size() <= 0) {
            return result;
        }
        int i;
        StringBuilder stringBuilder2;
        stringBuilder = new StringBuilder();
        stringBuilder.append(result);
        stringBuilder.append("tgts(");
        result = stringBuilder.toString();
        str = ", ";
        if (this.mTargetIds.size() > 0) {
            for (i = 0; i < this.mTargetIds.size(); i++) {
                if (i > 0) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(result);
                    stringBuilder2.append(str);
                    result = stringBuilder2.toString();
                }
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(result);
                stringBuilder2.append(this.mTargetIds.get(i));
                result = stringBuilder2.toString();
            }
        }
        if (this.mTargets.size() > 0) {
            for (i = 0; i < this.mTargets.size(); i++) {
                if (i > 0) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(result);
                    stringBuilder2.append(str);
                    result = stringBuilder2.toString();
                }
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(result);
                stringBuilder2.append(this.mTargets.get(i));
                result = stringBuilder2.toString();
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(result);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
