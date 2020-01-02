package miui.maml.animation;

import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import miui.maml.CommandTriggers;
import miui.maml.animation.interpolater.InterpolatorHelper;
import miui.maml.data.Expression;
import miui.maml.data.IndexedVariable;
import miui.maml.data.Variables;
import miui.maml.elements.ScreenElement;
import miui.maml.util.Utils;
import miui.maml.util.Utils.XmlTraverseListener;
import org.w3c.dom.Element;

public abstract class BaseAnimation {
    private static final long INFINITE_TIME = 1000000000000L;
    private static final String LOG_TAG = "BaseAnimation";
    public static final int PLAY_TO_END = -1;
    private static final String VAR_CURRENT_FRAME = "current_frame";
    private long mAnimEndTime;
    private long mAnimStartTime;
    protected String[] mAttrs;
    private double[] mCurValues;
    private IndexedVariable mCurrentFrame;
    private Expression mDelay;
    private boolean mDisable;
    private long mEndTime;
    private boolean mHasName;
    private boolean mInitPaused;
    private boolean mIsDelay;
    private boolean mIsFirstFrame;
    private boolean mIsFirstReset;
    private boolean mIsLastFrame;
    private boolean mIsLoop;
    private boolean mIsPaused;
    private boolean mIsReverse;
    private boolean mIsTimeInfinite;
    protected ArrayList<AnimationItem> mItems;
    private boolean mLoop;
    private String mName;
    private long mPauseTime;
    private long mPlayTimeRange;
    private long mRealTimeRange;
    private long mResetTime;
    protected ScreenElement mScreenElement;
    private long mStartTime;
    private String mTag;
    private CommandTriggers mTriggers;

    public static class AnimationItem {
        private BaseAnimation mAni;
        private double[] mAttrsValue;
        public Expression mDeltaTimeExp;
        public Expression[] mExps;
        public long mInitTime;
        public InterpolatorHelper mInterpolator;
        private boolean mNeedEvaluate = true;
        public long mTime;

        public AnimationItem(BaseAnimation ani, Element node) {
            this.mAni = ani;
            load(node);
        }

        public double get(int i) {
            double[] dArr = this.mAttrsValue;
            if (dArr == null || i < 0 || i >= dArr.length) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("fail to get number in AnimationItem:");
                stringBuilder.append(i);
                Log.e(BaseAnimation.LOG_TAG, stringBuilder.toString());
                return 0.0d;
            }
            if (this.mNeedEvaluate) {
                reevaluate();
                this.mNeedEvaluate = false;
            }
            return this.mAttrsValue[i];
        }

        public boolean attrExists(int i) {
            Expression[] expressionArr = this.mExps;
            boolean z = false;
            if (expressionArr == null || i < 0 || i >= expressionArr.length) {
                return false;
            }
            if (expressionArr[i] != null) {
                z = true;
            }
            return z;
        }

        private void load(Element node) {
            Variables vars = this.mAni.getVariables();
            this.mInterpolator = InterpolatorHelper.create(vars, node);
            try {
                this.mTime = Long.parseLong(node.getAttribute("time"));
            } catch (NumberFormatException e) {
            }
            this.mDeltaTimeExp = Expression.build(vars, node.getAttribute("dtime"));
            String[] attrs = this.mAni.getAttrs();
            if (attrs != null) {
                this.mAttrsValue = new double[attrs.length];
                this.mExps = new Expression[attrs.length];
                int i = 0;
                int length = attrs.length;
                int i2 = 0;
                while (i2 < length) {
                    String s = attrs[i2];
                    Expression exp = Expression.build(vars, node.getAttribute(s));
                    if (exp == null && i == 0) {
                        String str = "value";
                        if (!str.equals(s)) {
                            exp = Expression.build(vars, node.getAttribute(str));
                        }
                    }
                    int i3 = i + 1;
                    this.mExps[i] = exp;
                    i2++;
                    i = i3;
                }
            }
            this.mInitTime = this.mTime;
        }

        private void reevaluate() {
            Expression[] expressionArr = this.mExps;
            if (expressionArr != null) {
                int i = 0;
                int length = expressionArr.length;
                int i2 = 0;
                while (i2 < length) {
                    Expression e = expressionArr[i2];
                    int i3 = i + 1;
                    this.mAttrsValue[i] = e == null ? 0.0d : e.evaluate();
                    i2++;
                    i = i3;
                }
            }
        }

        public void reset() {
            this.mNeedEvaluate = true;
            this.mTime = this.mInitTime;
        }
    }

    public BaseAnimation(Element node, String tag, String[] attrs, ScreenElement ele) {
        this.mItems = new ArrayList();
        this.mLoop = true;
        this.mScreenElement = ele;
        this.mAttrs = attrs;
        this.mCurValues = new double[this.mAttrs.length];
        load(node, tag);
    }

    public BaseAnimation(Element node, String tag, String attr, ScreenElement ele) {
        this(node, tag, new String[]{attr}, ele);
    }

    public BaseAnimation(Element node, String tag, ScreenElement ele) {
        this(node, tag, "value", ele);
    }

    public BaseAnimation(Element node, ScreenElement ele) {
        this(node, null, "value", ele);
    }

    public String[] getAttrs() {
        return this.mAttrs;
    }

    private void load(Element node, String tag) {
        this.mName = node.getAttribute("name");
        this.mHasName = TextUtils.isEmpty(this.mName) ^ 1;
        Variables vars = getVariables();
        if (this.mHasName) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.mName);
            stringBuilder.append(".");
            stringBuilder.append(VAR_CURRENT_FRAME);
            this.mCurrentFrame = new IndexedVariable(stringBuilder.toString(), vars, true);
        }
        this.mDelay = Expression.build(vars, node.getAttribute("delay"));
        this.mInitPaused = Boolean.parseBoolean(node.getAttribute("initPause"));
        this.mLoop = "false".equalsIgnoreCase(node.getAttribute("loop")) ^ 1;
        this.mTag = node.getAttribute("tag");
        r3 = new String[2];
        boolean z = false;
        r3[0] = tag;
        r3[1] = ListItemElement.TAG_NAME;
        Utils.traverseXmlElementChildrenTags(node, r3, new XmlTraverseListener() {
            public void onChild(Element child) {
                ArrayList arrayList = BaseAnimation.this.mItems;
                BaseAnimation baseAnimation = BaseAnimation.this;
                arrayList.add(baseAnimation.onCreateItem(baseAnimation, child));
            }
        });
        if (this.mItems.size() <= 0) {
            Log.e(LOG_TAG, "empty items");
            return;
        }
        ArrayList arrayList = this.mItems;
        if (((AnimationItem) arrayList.get(arrayList.size() - 1)).mTime >= INFINITE_TIME) {
            z = true;
        }
        this.mIsTimeInfinite = z;
        if (this.mItems.size() <= 1 || !this.mIsTimeInfinite) {
            ArrayList arrayList2 = this.mItems;
            this.mRealTimeRange = ((AnimationItem) arrayList2.get(arrayList2.size() - 1)).mTime;
        } else {
            ArrayList arrayList3 = this.mItems;
            this.mRealTimeRange = ((AnimationItem) arrayList3.get(arrayList3.size() - 2)).mTime;
        }
        Element triggers = Utils.getChild(node, CommandTriggers.TAG_NAME);
        if (triggers != null) {
            this.mTriggers = new CommandTriggers(triggers, this.mScreenElement);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final Variables getVariables() {
        return this.mScreenElement.getVariables();
    }

    /* Access modifiers changed, original: protected */
    public AnimationItem getItem(int index) {
        if (index < 0 || index >= this.mItems.size()) {
            return null;
        }
        return (AnimationItem) this.mItems.get(index);
    }

    /* Access modifiers changed, original: protected */
    public AnimationItem onCreateItem(BaseAnimation ani, Element ele) {
        return new AnimationItem(ani, ele);
    }

    public void init() {
        CommandTriggers commandTriggers = this.mTriggers;
        if (commandTriggers != null) {
            commandTriggers.init();
        }
    }

    public void finish() {
        int i;
        CommandTriggers commandTriggers = this.mTriggers;
        if (commandTriggers != null) {
            commandTriggers.finish();
        }
        int M = this.mItems.size();
        for (i = 0; i < M; i++) {
            ((AnimationItem) this.mItems.get(i)).reset();
        }
        i = this.mCurValues.length;
        for (int i2 = 0; i2 < i; i2++) {
            this.mCurValues[i2] = 0.0d;
        }
    }

    public void pause() {
        CommandTriggers commandTriggers = this.mTriggers;
        if (commandTriggers != null) {
            commandTriggers.pause();
        }
    }

    public void resume() {
        CommandTriggers commandTriggers = this.mTriggers;
        if (commandTriggers != null) {
            commandTriggers.resume();
        }
    }

    public void onAction(String action) {
        CommandTriggers commandTriggers = this.mTriggers;
        if (commandTriggers != null) {
            commandTriggers.onAction(action);
        }
    }

    public void reset(long time) {
        if (!this.mDisable) {
            int N = this.mAttrs.length;
            for (int i = 0; i < N; i++) {
                this.mCurValues[i] = getDelayValue(i);
            }
            if (this.mInitPaused) {
                playAnim(time, 0, 0, false, false);
            } else {
                playAnim(time, 0, -1, true, true);
            }
            if (this.mHasName) {
                this.mCurrentFrame.set(0.0d);
            }
            onAction("init");
        }
    }

    /* Access modifiers changed, original: protected */
    public double getDelayValue(int i) {
        AnimationItem ai = getItem(null);
        return ai != null ? ai.get(i) : 0.0d;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0057  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x005d  */
    public void playAnim(long r15, long r17, long r19, boolean r21, boolean r22) {
        /*
        r14 = this;
        r0 = r14;
        r1 = r0.mDisable;
        if (r1 == 0) goto L_0x0006;
    L_0x0005:
        return;
    L_0x0006:
        r1 = r15;
        r0.mResetTime = r1;
        r3 = 0;
        r5 = (r17 > r3 ? 1 : (r17 == r3 ? 0 : -1));
        r6 = -1;
        if (r5 >= 0) goto L_0x0018;
    L_0x0011:
        r5 = (r17 > r6 ? 1 : (r17 == r6 ? 0 : -1));
        if (r5 != 0) goto L_0x0016;
    L_0x0015:
        goto L_0x0018;
    L_0x0016:
        r8 = r3;
        goto L_0x001a;
    L_0x0018:
        r8 = r17;
    L_0x001a:
        r0.mStartTime = r8;
        r0.mAnimStartTime = r8;
        r5 = (r19 > r3 ? 1 : (r19 == r3 ? 0 : -1));
        if (r5 >= 0) goto L_0x0029;
    L_0x0022:
        r5 = (r19 > r6 ? 1 : (r19 == r6 ? 0 : -1));
        if (r5 != 0) goto L_0x0027;
    L_0x0026:
        goto L_0x0029;
    L_0x0027:
        r8 = r3;
        goto L_0x002b;
    L_0x0029:
        r8 = r19;
    L_0x002b:
        r0.mEndTime = r8;
        r0.mAnimEndTime = r8;
        r5 = r21;
        r0.mIsLoop = r5;
        r8 = r22;
        r0.mIsDelay = r8;
        r9 = r0.mStartTime;
        r6 = (r9 > r6 ? 1 : (r9 == r6 ? 0 : -1));
        r7 = 1;
        r11 = 0;
        if (r6 == 0) goto L_0x004c;
    L_0x003f:
        r12 = r0.mEndTime;
        r6 = (r9 > r12 ? 1 : (r9 == r12 ? 0 : -1));
        if (r6 < 0) goto L_0x004a;
    L_0x0045:
        r6 = (r12 > r3 ? 1 : (r12 == r3 ? 0 : -1));
        if (r6 < 0) goto L_0x004a;
    L_0x0049:
        goto L_0x004c;
    L_0x004a:
        r6 = r11;
        goto L_0x004d;
    L_0x004c:
        r6 = r7;
    L_0x004d:
        r0.mIsReverse = r6;
        r9 = r0.mStartTime;
        r12 = r0.mEndTime;
        r6 = (r9 > r12 ? 1 : (r9 == r12 ? 0 : -1));
        if (r6 != 0) goto L_0x0059;
    L_0x0057:
        r0.mIsLoop = r11;
    L_0x0059:
        r6 = r0.mIsDelay;
        if (r6 == 0) goto L_0x006c;
    L_0x005d:
        r6 = r0.mDelay;
        if (r6 == 0) goto L_0x006c;
    L_0x0061:
        r9 = r0.mResetTime;
        r9 = (double) r9;
        r12 = r6.evaluate();
        r9 = r9 + r12;
        r9 = (long) r9;
        r0.mResetTime = r9;
    L_0x006c:
        r0.mIsFirstFrame = r7;
        r0.mIsLastFrame = r11;
        r0.mIsPaused = r11;
        r0.mIsFirstReset = r7;
        r0.mPlayTimeRange = r3;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.maml.animation.BaseAnimation.playAnim(long, long, long, boolean, boolean):void");
    }

    public final void tick(long currentTime) {
        if (!this.mIsPaused && !this.mDisable) {
            long elapsedTime = currentTime - this.mResetTime;
            if (elapsedTime < 0) {
                if (this.mIsFirstFrame) {
                    this.mIsFirstFrame = false;
                    elapsedTime = 0;
                } else {
                    onTick(null, null, 0.0f);
                    return;
                }
            }
            if (this.mIsFirstReset || (this.mIsLastFrame && !this.mIsTimeInfinite && this.mLoop && this.mIsLoop)) {
                resetTime();
            }
            if (!(!this.mIsTimeInfinite && this.mLoop && this.mIsLoop) && this.mIsLastFrame) {
                this.mIsPaused = true;
                this.mPauseTime = this.mResetTime + this.mPlayTimeRange;
                if (this.mHasName) {
                    this.mCurrentFrame.set((double) this.mEndTime);
                }
                onAction("end");
                return;
            }
            long elapsedTime2;
            long j = this.mPlayTimeRange;
            if (elapsedTime >= j) {
                this.mResetTime = currentTime - (elapsedTime % (j + 1));
                elapsedTime = this.mPlayTimeRange;
                this.mIsLastFrame = true;
            }
            if (this.mIsReverse) {
                elapsedTime2 = this.mAnimStartTime - elapsedTime;
            } else {
                elapsedTime2 = this.mAnimStartTime + elapsedTime;
            }
            long time = elapsedTime2 % (this.mRealTimeRange + 1);
            AnimationItem item2 = null;
            int N = this.mItems.size();
            int i = 0;
            while (i < N) {
                AnimationItem item22 = (AnimationItem) this.mItems.get(i);
                if (time < item22.mTime) {
                    AnimationItem item1;
                    long base;
                    long range;
                    if (i == 0) {
                        item1 = null;
                        base = 0;
                        range = item22.mTime;
                    } else {
                        AnimationItem item12 = (AnimationItem) this.mItems.get(i - 1);
                        long range2 = item22.mTime - item12.mTime;
                        item1 = item12;
                        base = item12.mTime;
                        range = range2;
                    }
                    onTick(item1, item22, getRatio(item1, time, base, range));
                    return;
                }
                i++;
                item2 = item22;
            }
            onTick(null, item2, 1.0f);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onTick(AnimationItem item1, AnimationItem item2, float ratio) {
        if (item1 != null || item2 != null) {
            double defaultValue = getDefaultValue();
            int N = this.mAttrs.length;
            int i = 0;
            while (i < N) {
                double v = item1 == null ? defaultValue : item1.get(i);
                this.mCurValues[i] = ((item2.get(i) - v) * ((double) ratio)) + v;
                i++;
            }
        }
    }

    public double getCurValue(int i) {
        return this.mCurValues[i];
    }

    public void setCurValue(int i, double v) {
        this.mCurValues[i] = v;
    }

    /* Access modifiers changed, original: protected */
    public double getDefaultValue() {
        return 0.0d;
    }

    public void pauseAnim(long time) {
        if (!(this.mDisable || this.mIsPaused)) {
            this.mIsPaused = true;
            this.mPauseTime = time;
        }
    }

    public void resumeAnim(long time) {
        if (!this.mDisable && this.mIsPaused) {
            this.mIsPaused = false;
            this.mResetTime += time - this.mPauseTime;
        }
    }

    private float getRatio(AnimationItem item1, long time, long base, long range) {
        float ratio = range == 0 ? 1.0f : ((float) (time - base)) / ((float) range);
        if (item1 == null || item1.mInterpolator == null) {
            return ratio;
        }
        return item1.mInterpolator.get(ratio);
    }

    private void resetTime() {
        if (this.mIsFirstReset) {
            this.mIsFirstReset = false;
        }
        this.mIsLastFrame = false;
        int N = this.mItems.size();
        for (int i = 0; i < N; i++) {
            ((AnimationItem) this.mItems.get(i)).reset();
        }
        reevaluate();
        this.mAnimStartTime = transToAnimTime(this.mStartTime);
        this.mAnimEndTime = transToAnimTime(this.mEndTime);
        this.mPlayTimeRange = Math.abs(this.mAnimEndTime - this.mAnimStartTime);
    }

    private long transToAnimTime(long time) {
        if (time == -1 || time > this.mRealTimeRange) {
            return this.mRealTimeRange;
        }
        return time;
    }

    private void reevaluate() {
        long timeRange = 0;
        int N = this.mItems.size();
        for (int i = 0; i < N; i++) {
            AnimationItem pos = (AnimationItem) this.mItems.get(i);
            if (pos.mDeltaTimeExp != null) {
                long tmpTimeRange = (long) pos.mDeltaTimeExp.evaluate();
                if (tmpTimeRange < 0) {
                    tmpTimeRange = 0;
                }
                timeRange += tmpTimeRange;
                pos.mTime = timeRange;
            } else if (pos.mTime >= timeRange) {
                timeRange = pos.mTime;
            }
        }
        this.mIsTimeInfinite = timeRange >= INFINITE_TIME;
        if (N <= 1 || !this.mIsTimeInfinite) {
            this.mRealTimeRange = timeRange;
        } else {
            this.mRealTimeRange = ((AnimationItem) this.mItems.get(N - 2)).mTime;
        }
    }

    public String getTag() {
        return this.mTag;
    }

    public void setDisable(boolean b) {
        this.mDisable = b;
    }
}
