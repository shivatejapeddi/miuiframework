package android.animation;

import android.animation.Animator.AnimatorListener;
import android.app.ActivityThread;
import android.app.Application;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Looper;
import android.provider.Telephony.BaseMmsColumns;
import android.util.AndroidRuntimeException;
import android.util.ArrayMap;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public final class AnimatorSet extends Animator implements AnimationFrameCallback {
    private static final String TAG = "AnimatorSet";
    private boolean mChildrenInitialized;
    private ValueAnimator mDelayAnim;
    private boolean mDependencyDirty;
    private AnimatorListenerAdapter mDummyListener;
    private long mDuration;
    private final boolean mEndCanBeCalled;
    private ArrayList<AnimationEvent> mEvents = new ArrayList();
    private long mFirstFrame;
    private TimeInterpolator mInterpolator;
    private int mLastEventId;
    private long mLastFrameTime;
    private ArrayMap<Animator, Node> mNodeMap = new ArrayMap();
    private ArrayList<Node> mNodes = new ArrayList();
    private long mPauseTime;
    private ArrayList<Node> mPlayingSet = new ArrayList();
    private boolean mReversing;
    private Node mRootNode;
    private SeekState mSeekState;
    private boolean mSelfPulse;
    private final boolean mShouldIgnoreEndWithoutStart;
    private final boolean mShouldResetValuesAtStart;
    private long mStartDelay;
    private boolean mStarted;
    private long mTotalDuration;

    private static class AnimationEvent {
        static final int ANIMATION_DELAY_ENDED = 1;
        static final int ANIMATION_END = 2;
        static final int ANIMATION_START = 0;
        final int mEvent;
        final Node mNode;

        AnimationEvent(Node node, int event) {
            this.mNode = node;
            this.mEvent = event;
        }

        /* Access modifiers changed, original: 0000 */
        public long getTime() {
            int i = this.mEvent;
            if (i == 0) {
                return this.mNode.mStartTime;
            }
            if (i != 1) {
                return this.mNode.mEndTime;
            }
            long j = -1;
            if (this.mNode.mStartTime != -1) {
                j = this.mNode.mAnimation.getStartDelay() + this.mNode.mStartTime;
            }
            return j;
        }

        public String toString() {
            int i = this.mEvent;
            String eventStr = i == 0 ? BaseMmsColumns.START : i == 1 ? "delay ended" : "end";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(eventStr);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(this.mNode.mAnimation.toString());
            return stringBuilder.toString();
        }
    }

    public class Builder {
        private Node mCurrentNode;

        Builder(Animator anim) {
            AnimatorSet.this.mDependencyDirty = true;
            this.mCurrentNode = AnimatorSet.this.getNodeForAnimation(anim);
        }

        public Builder with(Animator anim) {
            this.mCurrentNode.addSibling(AnimatorSet.this.getNodeForAnimation(anim));
            return this;
        }

        public Builder before(Animator anim) {
            this.mCurrentNode.addChild(AnimatorSet.this.getNodeForAnimation(anim));
            return this;
        }

        public Builder after(Animator anim) {
            this.mCurrentNode.addParent(AnimatorSet.this.getNodeForAnimation(anim));
            return this;
        }

        /* JADX WARNING: Incorrect type for fill-array insn 0x0003, element type: float, insn element type: null */
        public android.animation.AnimatorSet.Builder after(long r2) {
            /*
            r1 = this;
            r0 = 2;
            r0 = new float[r0];
            r0 = {0, 1065353216};
            r0 = android.animation.ValueAnimator.ofFloat(r0);
            r0.setDuration(r2);
            r1.after(r0);
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.animation.AnimatorSet$Builder.after(long):android.animation.AnimatorSet$Builder");
        }
    }

    private static class Node implements Cloneable {
        Animator mAnimation;
        ArrayList<Node> mChildNodes = null;
        long mEndTime = 0;
        boolean mEnded = false;
        Node mLatestParent = null;
        ArrayList<Node> mParents;
        boolean mParentsAdded = false;
        ArrayList<Node> mSiblings;
        long mStartTime = 0;
        long mTotalDuration = 0;

        public Node(Animator animation) {
            this.mAnimation = animation;
        }

        public Node clone() {
            try {
                Node node = (Node) super.clone();
                node.mAnimation = this.mAnimation.clone();
                if (this.mChildNodes != null) {
                    node.mChildNodes = new ArrayList(this.mChildNodes);
                }
                if (this.mSiblings != null) {
                    node.mSiblings = new ArrayList(this.mSiblings);
                }
                if (this.mParents != null) {
                    node.mParents = new ArrayList(this.mParents);
                }
                node.mEnded = false;
                return node;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void addChild(Node node) {
            if (this.mChildNodes == null) {
                this.mChildNodes = new ArrayList();
            }
            if (!this.mChildNodes.contains(node)) {
                this.mChildNodes.add(node);
                node.addParent(this);
            }
        }

        public void addSibling(Node node) {
            if (this.mSiblings == null) {
                this.mSiblings = new ArrayList();
            }
            if (!this.mSiblings.contains(node)) {
                this.mSiblings.add(node);
                node.addSibling(this);
            }
        }

        public void addParent(Node node) {
            if (this.mParents == null) {
                this.mParents = new ArrayList();
            }
            if (!this.mParents.contains(node)) {
                this.mParents.add(node);
                node.addChild(this);
            }
        }

        public void addParents(ArrayList<Node> parents) {
            if (parents != null) {
                int size = parents.size();
                for (int i = 0; i < size; i++) {
                    addParent((Node) parents.get(i));
                }
            }
        }
    }

    private class SeekState {
        private long mPlayTime;
        private boolean mSeekingInReverse;

        private SeekState() {
            this.mPlayTime = -1;
            this.mSeekingInReverse = false;
        }

        /* synthetic */ SeekState(AnimatorSet x0, AnonymousClass1 x1) {
            this();
        }

        /* Access modifiers changed, original: 0000 */
        public void reset() {
            this.mPlayTime = -1;
            this.mSeekingInReverse = false;
        }

        /* Access modifiers changed, original: 0000 */
        public void setPlayTime(long playTime, boolean inReverse) {
            if (AnimatorSet.this.getTotalDuration() != -1) {
                this.mPlayTime = Math.min(playTime, AnimatorSet.this.getTotalDuration() - AnimatorSet.this.mStartDelay);
            }
            this.mPlayTime = Math.max(0, this.mPlayTime);
            this.mSeekingInReverse = inReverse;
        }

        /* Access modifiers changed, original: 0000 */
        public void updateSeekDirection(boolean inReverse) {
            if (inReverse && AnimatorSet.this.getTotalDuration() == -1) {
                throw new UnsupportedOperationException("Error: Cannot reverse infinite animator set");
            } else if (this.mPlayTime >= 0 && inReverse != this.mSeekingInReverse) {
                this.mPlayTime = (AnimatorSet.this.getTotalDuration() - AnimatorSet.this.mStartDelay) - this.mPlayTime;
                this.mSeekingInReverse = inReverse;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public long getPlayTime() {
            return this.mPlayTime;
        }

        /* Access modifiers changed, original: 0000 */
        public long getPlayTimeNormalized() {
            if (AnimatorSet.this.mReversing) {
                return (AnimatorSet.this.getTotalDuration() - AnimatorSet.this.mStartDelay) - this.mPlayTime;
            }
            return this.mPlayTime;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isActive() {
            return this.mPlayTime != -1;
        }
    }

    public AnimatorSet() {
        boolean isPreO;
        boolean z = false;
        this.mDependencyDirty = false;
        this.mStarted = false;
        this.mStartDelay = 0;
        this.mDelayAnim = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(0);
        this.mRootNode = new Node(this.mDelayAnim);
        this.mDuration = -1;
        this.mInterpolator = null;
        this.mTotalDuration = 0;
        this.mLastFrameTime = -1;
        this.mFirstFrame = -1;
        this.mLastEventId = -1;
        this.mReversing = false;
        this.mSelfPulse = true;
        this.mSeekState = new SeekState(this, null);
        this.mChildrenInitialized = false;
        this.mPauseTime = -1;
        this.mDummyListener = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (AnimatorSet.this.mNodeMap.get(animation) != null) {
                    ((Node) AnimatorSet.this.mNodeMap.get(animation)).mEnded = true;
                    return;
                }
                throw new AndroidRuntimeException("Error: animation ended is not in the node map");
            }
        };
        this.mNodeMap.put(this.mDelayAnim, this.mRootNode);
        this.mNodes.add(this.mRootNode);
        Application app = ActivityThread.currentApplication();
        if (app == null || app.getApplicationInfo() == null) {
            this.mShouldIgnoreEndWithoutStart = true;
            isPreO = true;
        } else {
            if (app.getApplicationInfo().targetSdkVersion < 24) {
                this.mShouldIgnoreEndWithoutStart = true;
            } else {
                this.mShouldIgnoreEndWithoutStart = false;
            }
            isPreO = app.getApplicationInfo().targetSdkVersion < 26;
        }
        this.mShouldResetValuesAtStart = !isPreO;
        if (!isPreO) {
            z = true;
        }
        this.mEndCanBeCalled = z;
    }

    public void playTogether(Animator... items) {
        if (items != null) {
            Builder builder = play(items[0]);
            for (int i = 1; i < items.length; i++) {
                builder.with(items[i]);
            }
        }
    }

    public void playTogether(Collection<Animator> items) {
        if (items != null && items.size() > 0) {
            Builder builder = null;
            for (Animator anim : items) {
                if (builder == null) {
                    builder = play(anim);
                } else {
                    builder.with(anim);
                }
            }
        }
    }

    public void playSequentially(Animator... items) {
        if (items == null) {
            return;
        }
        if (items.length == 1) {
            play(items[0]);
            return;
        }
        for (int i = 0; i < items.length - 1; i++) {
            play(items[i]).before(items[i + 1]);
        }
    }

    public void playSequentially(List<Animator> items) {
        if (items != null && items.size() > 0) {
            if (items.size() == 1) {
                play((Animator) items.get(0));
                return;
            }
            for (int i = 0; i < items.size() - 1; i++) {
                play((Animator) items.get(i)).before((Animator) items.get(i + 1));
            }
        }
    }

    public ArrayList<Animator> getChildAnimations() {
        ArrayList<Animator> childList = new ArrayList();
        int size = this.mNodes.size();
        for (int i = 0; i < size; i++) {
            Node node = (Node) this.mNodes.get(i);
            if (node != this.mRootNode) {
                childList.add(node.mAnimation);
            }
        }
        return childList;
    }

    public void setTarget(Object target) {
        int size = this.mNodes.size();
        for (int i = 0; i < size; i++) {
            Animator animation = ((Node) this.mNodes.get(i)).mAnimation;
            if (animation instanceof AnimatorSet) {
                ((AnimatorSet) animation).setTarget(target);
            } else if (animation instanceof ObjectAnimator) {
                ((ObjectAnimator) animation).setTarget(target);
            }
        }
    }

    public int getChangingConfigurations() {
        int conf = super.getChangingConfigurations();
        for (int i = 0; i < this.mNodes.size(); i++) {
            conf |= ((Node) this.mNodes.get(i)).mAnimation.getChangingConfigurations();
        }
        return conf;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    public Builder play(Animator anim) {
        if (anim != null) {
            return new Builder(anim);
        }
        return null;
    }

    public void cancel() {
        if (Looper.myLooper() == null) {
            throw new AndroidRuntimeException("Animators may only be run on Looper threads");
        } else if (isStarted()) {
            int i;
            if (this.mListeners != null) {
                ArrayList<AnimatorListener> tmpListeners = (ArrayList) this.mListeners.clone();
                int size = tmpListeners.size();
                for (i = 0; i < size; i++) {
                    ((AnimatorListener) tmpListeners.get(i)).onAnimationCancel(this);
                }
            }
            ArrayList<Node> playingSet = new ArrayList(this.mPlayingSet);
            i = playingSet.size();
            for (int i2 = 0; i2 < i; i2++) {
                ((Node) playingSet.get(i2)).mAnimation.cancel();
            }
            this.mPlayingSet.clear();
            endAnimation();
        }
    }

    private void forceToEnd() {
        if (this.mEndCanBeCalled) {
            end();
            return;
        }
        if (this.mReversing) {
            handleAnimationEvents(this.mLastEventId, 0, getTotalDuration());
        } else {
            long zeroScalePlayTime = getTotalDuration();
            if (zeroScalePlayTime == -1) {
                zeroScalePlayTime = 2147483647L;
            }
            handleAnimationEvents(this.mLastEventId, this.mEvents.size() - 1, zeroScalePlayTime);
        }
        this.mPlayingSet.clear();
        endAnimation();
    }

    public void end() {
        if (Looper.myLooper() == null) {
            throw new AndroidRuntimeException("Animators may only be run on Looper threads");
        } else if (!this.mShouldIgnoreEndWithoutStart || isStarted()) {
            if (isStarted()) {
                AnimationEvent event;
                Animator anim;
                if (this.mReversing) {
                    int i = this.mLastEventId;
                    if (i == -1) {
                        i = this.mEvents.size();
                    }
                    this.mLastEventId = i;
                    while (true) {
                        i = this.mLastEventId;
                        if (i <= 0) {
                            break;
                        }
                        this.mLastEventId = i - 1;
                        event = (AnimationEvent) this.mEvents.get(this.mLastEventId);
                        anim = event.mNode.mAnimation;
                        if (!((Node) this.mNodeMap.get(anim)).mEnded) {
                            if (event.mEvent == 2) {
                                anim.reverse();
                            } else if (event.mEvent == 1 && anim.isStarted()) {
                                anim.end();
                            }
                        }
                    }
                } else {
                    while (this.mLastEventId < this.mEvents.size() - 1) {
                        this.mLastEventId++;
                        event = (AnimationEvent) this.mEvents.get(this.mLastEventId);
                        anim = event.mNode.mAnimation;
                        if (!((Node) this.mNodeMap.get(anim)).mEnded) {
                            if (event.mEvent == 0) {
                                anim.start();
                            } else if (event.mEvent == 2 && anim.isStarted()) {
                                anim.end();
                            }
                        }
                    }
                }
                this.mPlayingSet.clear();
            }
            endAnimation();
        }
    }

    public boolean isRunning() {
        if (this.mStartDelay == 0) {
            return this.mStarted;
        }
        return this.mLastFrameTime > 0;
    }

    public boolean isStarted() {
        return this.mStarted;
    }

    public long getStartDelay() {
        return this.mStartDelay;
    }

    public void setStartDelay(long startDelay) {
        if (startDelay < 0) {
            Log.w(TAG, "Start delay should always be non-negative");
            startDelay = 0;
        }
        long delta = startDelay - this.mStartDelay;
        if (delta != 0) {
            this.mStartDelay = startDelay;
            if (!this.mDependencyDirty) {
                int size = this.mNodes.size();
                int i = 0;
                while (true) {
                    long j = -1;
                    if (i >= size) {
                        break;
                    }
                    Node node = (Node) this.mNodes.get(i);
                    if (node == this.mRootNode) {
                        node.mEndTime = this.mStartDelay;
                    } else {
                        node.mStartTime = node.mStartTime == -1 ? -1 : node.mStartTime + delta;
                        if (node.mEndTime != -1) {
                            j = node.mEndTime + delta;
                        }
                        node.mEndTime = j;
                    }
                    i++;
                }
                long j2 = this.mTotalDuration;
                if (j2 != -1) {
                    this.mTotalDuration = j2 + delta;
                }
            }
        }
    }

    public long getDuration() {
        return this.mDuration;
    }

    public AnimatorSet setDuration(long duration) {
        if (duration >= 0) {
            this.mDependencyDirty = true;
            this.mDuration = duration;
            return this;
        }
        throw new IllegalArgumentException("duration must be a value of zero or greater");
    }

    public void setupStartValues() {
        int size = this.mNodes.size();
        for (int i = 0; i < size; i++) {
            Node node = (Node) this.mNodes.get(i);
            if (node != this.mRootNode) {
                node.mAnimation.setupStartValues();
            }
        }
    }

    public void setupEndValues() {
        int size = this.mNodes.size();
        for (int i = 0; i < size; i++) {
            Node node = (Node) this.mNodes.get(i);
            if (node != this.mRootNode) {
                node.mAnimation.setupEndValues();
            }
        }
    }

    public void pause() {
        if (Looper.myLooper() != null) {
            boolean previouslyPaused = this.mPaused;
            super.pause();
            if (!previouslyPaused && this.mPaused) {
                this.mPauseTime = -1;
                return;
            }
            return;
        }
        throw new AndroidRuntimeException("Animators may only be run on Looper threads");
    }

    public void resume() {
        if (Looper.myLooper() != null) {
            boolean previouslyPaused = this.mPaused;
            super.resume();
            if (previouslyPaused && !this.mPaused && this.mPauseTime >= 0) {
                addAnimationCallback(0);
                return;
            }
            return;
        }
        throw new AndroidRuntimeException("Animators may only be run on Looper threads");
    }

    public void start() {
        start(false, true);
    }

    /* Access modifiers changed, original: 0000 */
    public void startWithoutPulsing(boolean inReverse) {
        start(inReverse, false);
    }

    private void initAnimation() {
        if (this.mInterpolator != null) {
            for (int i = 0; i < this.mNodes.size(); i++) {
                ((Node) this.mNodes.get(i)).mAnimation.setInterpolator(this.mInterpolator);
            }
        }
        updateAnimatorsDuration();
        createDependencyGraph();
    }

    private void start(boolean inReverse, boolean selfPulse) {
        if (Looper.myLooper() != null) {
            this.mStarted = true;
            this.mSelfPulse = selfPulse;
            this.mPaused = false;
            this.mPauseTime = -1;
            int size = this.mNodes.size();
            for (int i = 0; i < size; i++) {
                Node node = (Node) this.mNodes.get(i);
                node.mEnded = false;
                node.mAnimation.setAllowRunningAsynchronously(false);
            }
            initAnimation();
            if (!inReverse || canReverse()) {
                this.mReversing = inReverse;
                boolean isEmptySet = isEmptySet(this);
                if (!isEmptySet) {
                    startAnimation();
                }
                if (this.mListeners != null) {
                    ArrayList<AnimatorListener> tmpListeners = (ArrayList) this.mListeners.clone();
                    int numListeners = tmpListeners.size();
                    for (int i2 = 0; i2 < numListeners; i2++) {
                        ((AnimatorListener) tmpListeners.get(i2)).onAnimationStart(this, inReverse);
                    }
                }
                if (isEmptySet) {
                    end();
                    return;
                }
                return;
            }
            throw new UnsupportedOperationException("Cannot reverse infinite AnimatorSet");
        }
        throw new AndroidRuntimeException("Animators may only be run on Looper threads");
    }

    private static boolean isEmptySet(AnimatorSet set) {
        if (set.getStartDelay() > 0) {
            return false;
        }
        for (int i = 0; i < set.getChildAnimations().size(); i++) {
            Animator anim = (Animator) set.getChildAnimations().get(i);
            if (!(anim instanceof AnimatorSet) || !isEmptySet((AnimatorSet) anim)) {
                return false;
            }
        }
        return true;
    }

    private void updateAnimatorsDuration() {
        if (this.mDuration >= 0) {
            int size = this.mNodes.size();
            for (int i = 0; i < size; i++) {
                ((Node) this.mNodes.get(i)).mAnimation.setDuration(this.mDuration);
            }
        }
        this.mDelayAnim.setDuration(this.mStartDelay);
    }

    /* Access modifiers changed, original: 0000 */
    public void skipToEndValue(boolean inReverse) {
        if (isInitialized()) {
            initAnimation();
            int i;
            if (inReverse) {
                for (i = this.mEvents.size() - 1; i >= 0; i--) {
                    if (((AnimationEvent) this.mEvents.get(i)).mEvent == 1) {
                        ((AnimationEvent) this.mEvents.get(i)).mNode.mAnimation.skipToEndValue(true);
                    }
                }
                return;
            }
            for (i = 0; i < this.mEvents.size(); i++) {
                if (((AnimationEvent) this.mEvents.get(i)).mEvent == 2) {
                    ((AnimationEvent) this.mEvents.get(i)).mNode.mAnimation.skipToEndValue(false);
                }
            }
            return;
        }
        throw new UnsupportedOperationException("Children must be initialized.");
    }

    /* Access modifiers changed, original: 0000 */
    public void animateBasedOnPlayTime(long currentPlayTime, long lastPlayTime, boolean inReverse) {
        long j = currentPlayTime;
        if (j < 0 || lastPlayTime < 0) {
            throw new UnsupportedOperationException("Error: Play time should never be negative.");
        }
        long lastPlayTime2;
        long currentPlayTime2;
        boolean inReverse2;
        long duration;
        int i;
        if (!inReverse) {
            lastPlayTime2 = lastPlayTime;
            currentPlayTime2 = j;
            inReverse2 = inReverse;
        } else if (getTotalDuration() != -1) {
            duration = getTotalDuration() - this.mStartDelay;
            currentPlayTime2 = duration - Math.min(j, duration);
            lastPlayTime2 = duration - lastPlayTime;
            inReverse2 = false;
        } else {
            throw new UnsupportedOperationException("Cannot reverse AnimatorSet with infinite duration");
        }
        ArrayList<Node> unfinishedNodes = new ArrayList();
        for (int i2 = 0; i2 < this.mEvents.size(); i2++) {
            AnimationEvent event = (AnimationEvent) this.mEvents.get(i2);
            if (event.getTime() > currentPlayTime2 || event.getTime() == -1) {
                break;
            }
            if (event.mEvent == 1 && (event.mNode.mEndTime == -1 || event.mNode.mEndTime > currentPlayTime2)) {
                unfinishedNodes.add(event.mNode);
            }
            if (event.mEvent == 2) {
                event.mNode.mAnimation.skipToEndValue(false);
            }
        }
        for (i = 0; i < unfinishedNodes.size(); i++) {
            Node node = (Node) unfinishedNodes.get(i);
            duration = getPlayTimeForNode(currentPlayTime2, node, inReverse2);
            if (!inReverse2) {
                duration -= node.mAnimation.getStartDelay();
            }
            node.mAnimation.animateBasedOnPlayTime(duration, lastPlayTime2, inReverse2);
        }
        for (i = 0; i < this.mEvents.size(); i++) {
            AnimationEvent event2 = (AnimationEvent) this.mEvents.get(i);
            if (event2.getTime() > currentPlayTime2 && event2.mEvent == 1) {
                event2.mNode.mAnimation.skipToEndValue(true);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isInitialized() {
        if (this.mChildrenInitialized) {
            return true;
        }
        boolean allInitialized = true;
        for (int i = 0; i < this.mNodes.size(); i++) {
            if (!((Node) this.mNodes.get(i)).mAnimation.isInitialized()) {
                allInitialized = false;
                break;
            }
        }
        this.mChildrenInitialized = allInitialized;
        return this.mChildrenInitialized;
    }

    private void skipToStartValue(boolean inReverse) {
        skipToEndValue(inReverse ^ 1);
    }

    public void setCurrentPlayTime(long playTime) {
        if (this.mReversing && getTotalDuration() == -1) {
            throw new UnsupportedOperationException("Error: Cannot seek in reverse in an infinite AnimatorSet");
        } else if ((getTotalDuration() == -1 || playTime <= getTotalDuration() - this.mStartDelay) && playTime >= 0) {
            initAnimation();
            if (isStarted() && !isPaused()) {
                this.mSeekState.setPlayTime(playTime, this.mReversing);
            } else if (this.mReversing) {
                throw new UnsupportedOperationException("Error: Something went wrong. mReversing should not be set when AnimatorSet is not started.");
            } else {
                if (!this.mSeekState.isActive()) {
                    findLatestEventIdForTime(0);
                    initChildren();
                    this.mSeekState.setPlayTime(0, this.mReversing);
                }
                animateBasedOnPlayTime(playTime, 0, this.mReversing);
                this.mSeekState.setPlayTime(playTime, this.mReversing);
            }
        } else {
            throw new UnsupportedOperationException("Error: Play time should always be in between0 and duration.");
        }
    }

    public long getCurrentPlayTime() {
        if (this.mSeekState.isActive()) {
            return this.mSeekState.getPlayTime();
        }
        if (this.mLastFrameTime == -1) {
            return 0;
        }
        float durationScale = ValueAnimator.getDurationScale();
        durationScale = durationScale == 0.0f ? 1.0f : durationScale;
        if (this.mReversing) {
            return (long) (((float) (this.mLastFrameTime - this.mFirstFrame)) / durationScale);
        }
        return (long) (((float) ((this.mLastFrameTime - this.mFirstFrame) - this.mStartDelay)) / durationScale);
    }

    private void initChildren() {
        if (!isInitialized()) {
            this.mChildrenInitialized = true;
            skipToEndValue(false);
        }
    }

    public boolean doAnimationFrame(long frameTime) {
        float durationScale = ValueAnimator.getDurationScale();
        if (durationScale == 0.0f) {
            forceToEnd();
            return true;
        }
        if (this.mFirstFrame < 0) {
            this.mFirstFrame = frameTime;
        }
        if (this.mPaused) {
            this.mPauseTime = frameTime;
            removeAnimationCallback();
            return false;
        }
        long j = this.mPauseTime;
        if (j > 0) {
            this.mFirstFrame += frameTime - j;
            this.mPauseTime = -1;
        }
        if (this.mSeekState.isActive()) {
            this.mSeekState.updateSeekDirection(this.mReversing);
            if (this.mReversing) {
                this.mFirstFrame = (long) (((float) frameTime) - (((float) this.mSeekState.getPlayTime()) * durationScale));
            } else {
                this.mFirstFrame = (long) (((float) frameTime) - (((float) (this.mSeekState.getPlayTime() + this.mStartDelay)) * durationScale));
            }
            this.mSeekState.reset();
        }
        if (!this.mReversing && ((float) frameTime) < ((float) this.mFirstFrame) + (((float) this.mStartDelay) * durationScale)) {
            return false;
        }
        int i;
        long unscaledPlayTime = (long) (((float) (frameTime - this.mFirstFrame)) / durationScale);
        this.mLastFrameTime = frameTime;
        int latestId = findLatestEventIdForTime(unscaledPlayTime);
        handleAnimationEvents(this.mLastEventId, latestId, unscaledPlayTime);
        this.mLastEventId = latestId;
        for (i = 0; i < this.mPlayingSet.size(); i++) {
            Node node = (Node) this.mPlayingSet.get(i);
            if (!node.mEnded) {
                pulseFrame(node, getPlayTimeForNode(unscaledPlayTime, node));
            }
        }
        for (i = this.mPlayingSet.size() - 1; i >= 0; i--) {
            if (((Node) this.mPlayingSet.get(i)).mEnded) {
                this.mPlayingSet.remove(i);
            }
        }
        boolean finished = false;
        if (!this.mReversing) {
            boolean z = this.mPlayingSet.isEmpty() && this.mLastEventId == this.mEvents.size() - 1;
            finished = z;
        } else if (this.mPlayingSet.size() == 1 && this.mPlayingSet.get(0) == this.mRootNode) {
            finished = true;
        } else if (this.mPlayingSet.isEmpty() && this.mLastEventId < 3) {
            finished = true;
        }
        if (!finished) {
            return false;
        }
        endAnimation();
        return true;
    }

    public void commitAnimationFrame(long frameTime) {
    }

    /* Access modifiers changed, original: 0000 */
    public boolean pulseAnimationFrame(long frameTime) {
        return doAnimationFrame(frameTime);
    }

    private void handleAnimationEvents(int startId, int latestId, long playTime) {
        int i;
        AnimationEvent event;
        Node node;
        if (this.mReversing) {
            for (i = (startId == -1 ? this.mEvents.size() : startId) - 1; i >= latestId; i--) {
                event = (AnimationEvent) this.mEvents.get(i);
                node = event.mNode;
                if (event.mEvent == 2) {
                    if (node.mAnimation.isStarted()) {
                        node.mAnimation.cancel();
                    }
                    node.mEnded = false;
                    this.mPlayingSet.add(event.mNode);
                    node.mAnimation.startWithoutPulsing(true);
                    pulseFrame(node, 0);
                } else if (event.mEvent == 1 && !node.mEnded) {
                    pulseFrame(node, getPlayTimeForNode(playTime, node));
                }
            }
            return;
        }
        for (i = startId + 1; i <= latestId; i++) {
            event = (AnimationEvent) this.mEvents.get(i);
            node = event.mNode;
            if (event.mEvent == 0) {
                this.mPlayingSet.add(event.mNode);
                if (node.mAnimation.isStarted()) {
                    node.mAnimation.cancel();
                }
                node.mEnded = false;
                node.mAnimation.startWithoutPulsing(false);
                pulseFrame(node, 0);
            } else if (event.mEvent == 2 && !node.mEnded) {
                pulseFrame(node, getPlayTimeForNode(playTime, node));
            }
        }
    }

    private void pulseFrame(Node node, long animPlayTime) {
        if (!node.mEnded) {
            float durationScale = ValueAnimator.getDurationScale();
            node.mEnded = node.mAnimation.pulseAnimationFrame((long) (((float) animPlayTime) * (durationScale == 0.0f ? 1.0f : durationScale)));
        }
    }

    private long getPlayTimeForNode(long overallPlayTime, Node node) {
        return getPlayTimeForNode(overallPlayTime, node, this.mReversing);
    }

    private long getPlayTimeForNode(long overallPlayTime, Node node, boolean inReverse) {
        if (!inReverse) {
            return overallPlayTime - node.mStartTime;
        }
        return node.mEndTime - (getTotalDuration() - overallPlayTime);
    }

    private void startAnimation() {
        int i;
        addDummyListener();
        addAnimationCallback(0);
        if (this.mSeekState.getPlayTimeNormalized() == 0 && this.mReversing) {
            this.mSeekState.reset();
        }
        if (this.mShouldResetValuesAtStart) {
            if (isInitialized()) {
                skipToEndValue(this.mReversing ^ 1);
            } else if (this.mReversing) {
                initChildren();
                skipToEndValue(this.mReversing ^ 1);
            } else {
                for (i = this.mEvents.size() - 1; i >= 0; i--) {
                    if (((AnimationEvent) this.mEvents.get(i)).mEvent == 1) {
                        Animator anim = ((AnimationEvent) this.mEvents.get(i)).mNode.mAnimation;
                        if (anim.isInitialized()) {
                            anim.skipToEndValue(true);
                        }
                    }
                }
            }
        }
        if (this.mReversing || this.mStartDelay == 0 || this.mSeekState.isActive()) {
            long playTime;
            if (this.mSeekState.isActive()) {
                this.mSeekState.updateSeekDirection(this.mReversing);
                playTime = this.mSeekState.getPlayTime();
            } else {
                playTime = 0;
            }
            i = findLatestEventIdForTime(playTime);
            handleAnimationEvents(-1, i, playTime);
            for (int i2 = this.mPlayingSet.size() - 1; i2 >= 0; i2--) {
                if (((Node) this.mPlayingSet.get(i2)).mEnded) {
                    this.mPlayingSet.remove(i2);
                }
            }
            this.mLastEventId = i;
        }
    }

    private void addDummyListener() {
        for (int i = 1; i < this.mNodes.size(); i++) {
            ((Node) this.mNodes.get(i)).mAnimation.addListener(this.mDummyListener);
        }
    }

    private void removeDummyListener() {
        for (int i = 1; i < this.mNodes.size(); i++) {
            ((Node) this.mNodes.get(i)).mAnimation.removeListener(this.mDummyListener);
        }
    }

    private int findLatestEventIdForTime(long currentPlayTime) {
        int size = this.mEvents.size();
        int latestId = this.mLastEventId;
        int i;
        if (this.mReversing) {
            currentPlayTime = getTotalDuration() - currentPlayTime;
            i = this.mLastEventId;
            if (i == -1) {
                i = size;
            }
            this.mLastEventId = i;
            for (i = this.mLastEventId - 1; i >= 0; i--) {
                if (((AnimationEvent) this.mEvents.get(i)).getTime() >= currentPlayTime) {
                    latestId = i;
                }
            }
        } else {
            for (i = this.mLastEventId + 1; i < size; i++) {
                AnimationEvent event = (AnimationEvent) this.mEvents.get(i);
                if (event.getTime() != -1 && event.getTime() <= currentPlayTime) {
                    latestId = i;
                }
            }
        }
        return latestId;
    }

    private void endAnimation() {
        this.mStarted = false;
        this.mLastFrameTime = -1;
        this.mFirstFrame = -1;
        this.mLastEventId = -1;
        this.mPaused = false;
        this.mPauseTime = -1;
        this.mSeekState.reset();
        this.mPlayingSet.clear();
        removeAnimationCallback();
        if (this.mListeners != null) {
            ArrayList<AnimatorListener> tmpListeners = (ArrayList) this.mListeners.clone();
            int numListeners = tmpListeners.size();
            for (int i = 0; i < numListeners; i++) {
                ((AnimatorListener) tmpListeners.get(i)).onAnimationEnd(this, this.mReversing);
            }
        }
        removeDummyListener();
        this.mSelfPulse = true;
        this.mReversing = false;
    }

    private void removeAnimationCallback() {
        if (this.mSelfPulse) {
            AnimationHandler.getInstance().removeCallback(this);
        }
    }

    private void addAnimationCallback(long delay) {
        if (this.mSelfPulse) {
            AnimationHandler.getInstance().addAnimationFrameCallback(this, delay);
        }
    }

    public AnimatorSet clone() {
        int n;
        Node node;
        Node nodeClone;
        final AnimatorSet anim = (AnimatorSet) super.clone();
        int nodeCount = this.mNodes.size();
        anim.mStarted = false;
        anim.mLastFrameTime = -1;
        anim.mFirstFrame = -1;
        anim.mLastEventId = -1;
        anim.mPaused = false;
        anim.mPauseTime = -1;
        anim.mSeekState = new SeekState(this, null);
        anim.mSelfPulse = true;
        anim.mPlayingSet = new ArrayList();
        anim.mNodeMap = new ArrayMap();
        anim.mNodes = new ArrayList(nodeCount);
        anim.mEvents = new ArrayList();
        anim.mDummyListener = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (anim.mNodeMap.get(animation) != null) {
                    ((Node) anim.mNodeMap.get(animation)).mEnded = true;
                    return;
                }
                throw new AndroidRuntimeException("Error: animation ended is not in the node map");
            }
        };
        anim.mReversing = false;
        anim.mDependencyDirty = true;
        HashMap<Node, Node> clonesMap = new HashMap(nodeCount);
        for (n = 0; n < nodeCount; n++) {
            node = (Node) this.mNodes.get(n);
            nodeClone = node.clone();
            nodeClone.mAnimation.removeListener(this.mDummyListener);
            clonesMap.put(node, nodeClone);
            anim.mNodes.add(nodeClone);
            anim.mNodeMap.put(nodeClone.mAnimation, nodeClone);
        }
        anim.mRootNode = (Node) clonesMap.get(this.mRootNode);
        anim.mDelayAnim = (ValueAnimator) anim.mRootNode.mAnimation;
        for (n = 0; n < nodeCount; n++) {
            int j;
            node = (Node) this.mNodes.get(n);
            nodeClone = (Node) clonesMap.get(node);
            nodeClone.mLatestParent = node.mLatestParent == null ? null : (Node) clonesMap.get(node.mLatestParent);
            int size = node.mChildNodes == null ? 0 : node.mChildNodes.size();
            for (j = 0; j < size; j++) {
                nodeClone.mChildNodes.set(j, (Node) clonesMap.get(node.mChildNodes.get(j)));
            }
            size = node.mSiblings == null ? 0 : node.mSiblings.size();
            for (j = 0; j < size; j++) {
                nodeClone.mSiblings.set(j, (Node) clonesMap.get(node.mSiblings.get(j)));
            }
            size = node.mParents == null ? 0 : node.mParents.size();
            for (j = 0; j < size; j++) {
                nodeClone.mParents.set(j, (Node) clonesMap.get(node.mParents.get(j)));
            }
        }
        return anim;
    }

    public boolean canReverse() {
        return getTotalDuration() != -1;
    }

    public void reverse() {
        start(true, true);
    }

    public String toString() {
        String returnVal = new StringBuilder();
        returnVal.append("AnimatorSet@");
        returnVal.append(Integer.toHexString(hashCode()));
        returnVal.append("{");
        returnVal = returnVal.toString();
        int size = this.mNodes.size();
        for (int i = 0; i < size; i++) {
            Node node = (Node) this.mNodes.get(i);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(returnVal);
            stringBuilder.append("\n    ");
            stringBuilder.append(node.mAnimation.toString());
            returnVal = stringBuilder.toString();
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(returnVal);
        stringBuilder2.append("\n}");
        return stringBuilder2.toString();
    }

    private void printChildCount() {
        ArrayList<Node> list = new ArrayList(this.mNodes.size());
        list.add(this.mRootNode);
        String str = TAG;
        Log.d(str, "Current tree: ");
        int index = 0;
        while (index < list.size()) {
            int listSize = list.size();
            StringBuilder builder = new StringBuilder();
            while (index < listSize) {
                Node node = (Node) list.get(index);
                int num = 0;
                if (node.mChildNodes != null) {
                    for (int i = 0; i < node.mChildNodes.size(); i++) {
                        Node child = (Node) node.mChildNodes.get(i);
                        if (child.mLatestParent == node) {
                            num++;
                            list.add(child);
                        }
                    }
                }
                builder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                builder.append(num);
                index++;
            }
            Log.d(str, builder.toString());
        }
    }

    private void createDependencyGraph() {
        int i;
        int i2;
        Node node;
        if (!this.mDependencyDirty) {
            boolean durationChanged = false;
            for (i = 0; i < this.mNodes.size(); i++) {
                if (((Node) this.mNodes.get(i)).mTotalDuration != ((Node) this.mNodes.get(i)).mAnimation.getTotalDuration()) {
                    durationChanged = true;
                    break;
                }
            }
            if (!durationChanged) {
                return;
            }
        }
        this.mDependencyDirty = false;
        i = this.mNodes.size();
        for (int i3 = 0; i3 < i; i3++) {
            ((Node) this.mNodes.get(i3)).mParentsAdded = false;
        }
        for (i2 = 0; i2 < i; i2++) {
            node = (Node) this.mNodes.get(i2);
            if (!node.mParentsAdded) {
                node.mParentsAdded = true;
                if (node.mSiblings != null) {
                    int j;
                    findSiblings(node, node.mSiblings);
                    node.mSiblings.remove(node);
                    int siblingSize = node.mSiblings.size();
                    for (j = 0; j < siblingSize; j++) {
                        node.addParents(((Node) node.mSiblings.get(j)).mParents);
                    }
                    for (j = 0; j < siblingSize; j++) {
                        Node sibling = (Node) node.mSiblings.get(j);
                        sibling.addParents(node.mParents);
                        sibling.mParentsAdded = true;
                    }
                }
            }
        }
        for (i2 = 0; i2 < i; i2++) {
            node = (Node) this.mNodes.get(i2);
            if (node != this.mRootNode && node.mParents == null) {
                node.addParent(this.mRootNode);
            }
        }
        ArrayList<Node> visited = new ArrayList(this.mNodes.size());
        node = this.mRootNode;
        node.mStartTime = 0;
        node.mEndTime = this.mDelayAnim.getDuration();
        updatePlayTime(this.mRootNode, visited);
        sortAnimationEvents();
        ArrayList arrayList = this.mEvents;
        this.mTotalDuration = ((AnimationEvent) arrayList.get(arrayList.size() - 1)).getTime();
    }

    private void sortAnimationEvents() {
        int i;
        this.mEvents.clear();
        for (i = 1; i < this.mNodes.size(); i++) {
            Node node = (Node) this.mNodes.get(i);
            this.mEvents.add(new AnimationEvent(node, 0));
            this.mEvents.add(new AnimationEvent(node, 1));
            this.mEvents.add(new AnimationEvent(node, 2));
        }
        this.mEvents.sort(new Comparator<AnimationEvent>() {
            public int compare(AnimationEvent e1, AnimationEvent e2) {
                long t1 = e1.getTime();
                long t2 = e2.getTime();
                if (t1 == t2) {
                    if (e2.mEvent + e1.mEvent == 1) {
                        return e1.mEvent - e2.mEvent;
                    }
                    return e2.mEvent - e1.mEvent;
                } else if (t2 == -1) {
                    return -1;
                } else {
                    if (t1 == -1) {
                        return 1;
                    }
                    return (int) (t1 - t2);
                }
            }
        });
        i = this.mEvents.size();
        int i2 = 0;
        while (i2 < i) {
            AnimationEvent event = (AnimationEvent) this.mEvents.get(i2);
            if (event.mEvent == 2) {
                boolean needToSwapStart;
                if (event.mNode.mStartTime == event.mNode.mEndTime) {
                    needToSwapStart = true;
                } else if (event.mNode.mEndTime == event.mNode.mStartTime + event.mNode.mAnimation.getStartDelay()) {
                    needToSwapStart = false;
                } else {
                    i2++;
                }
                int startEventId = i;
                int startDelayEndId = i;
                for (int j = i2 + 1; j < i && (startEventId >= i || startDelayEndId >= i); j++) {
                    if (((AnimationEvent) this.mEvents.get(j)).mNode == event.mNode) {
                        if (((AnimationEvent) this.mEvents.get(j)).mEvent == 0) {
                            startEventId = j;
                        } else if (((AnimationEvent) this.mEvents.get(j)).mEvent == 1) {
                            startDelayEndId = j;
                        }
                    }
                }
                if (needToSwapStart && startEventId == this.mEvents.size()) {
                    throw new UnsupportedOperationException("Something went wrong, no start isfound after stop for an animation that has the same start and endtime.");
                } else if (startDelayEndId != this.mEvents.size()) {
                    if (needToSwapStart) {
                        this.mEvents.add(i2, (AnimationEvent) this.mEvents.remove(startEventId));
                        i2++;
                    }
                    this.mEvents.add(i2, (AnimationEvent) this.mEvents.remove(startDelayEndId));
                    i2 += 2;
                } else {
                    throw new UnsupportedOperationException("Something went wrong, no startdelay end is found after stop for an animation");
                }
            }
            i2++;
        }
        if (this.mEvents.isEmpty() || ((AnimationEvent) this.mEvents.get(0)).mEvent == 0) {
            this.mEvents.add(0, new AnimationEvent(this.mRootNode, 0));
            this.mEvents.add(1, new AnimationEvent(this.mRootNode, 1));
            this.mEvents.add(2, new AnimationEvent(this.mRootNode, 2));
            ArrayList arrayList = this.mEvents;
            if (((AnimationEvent) arrayList.get(arrayList.size() - 1)).mEvent != 0) {
                arrayList = this.mEvents;
                if (((AnimationEvent) arrayList.get(arrayList.size() - 1)).mEvent != 1) {
                    return;
                }
            }
            throw new UnsupportedOperationException("Something went wrong, the last event is not an end event");
        }
        throw new UnsupportedOperationException("Sorting went bad, the start event should always be at index 0");
    }

    private void updatePlayTime(Node parent, ArrayList<Node> visited) {
        int i;
        if (parent.mChildNodes == null) {
            if (parent == this.mRootNode) {
                for (i = 0; i < this.mNodes.size(); i++) {
                    Node node = (Node) this.mNodes.get(i);
                    if (node != this.mRootNode) {
                        node.mStartTime = -1;
                        node.mEndTime = -1;
                    }
                }
            }
            return;
        }
        visited.add(parent);
        i = parent.mChildNodes.size();
        for (int i2 = 0; i2 < i; i2++) {
            Node child = (Node) parent.mChildNodes.get(i2);
            child.mTotalDuration = child.mAnimation.getTotalDuration();
            int index = visited.indexOf(child);
            if (index >= 0) {
                for (int j = index; j < visited.size(); j++) {
                    ((Node) visited.get(j)).mLatestParent = null;
                    ((Node) visited.get(j)).mStartTime = -1;
                    ((Node) visited.get(j)).mEndTime = -1;
                }
                child.mStartTime = -1;
                child.mEndTime = -1;
                child.mLatestParent = null;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Cycle found in AnimatorSet: ");
                stringBuilder.append(this);
                Log.w(TAG, stringBuilder.toString());
            } else {
                if (child.mStartTime != -1) {
                    if (parent.mEndTime == -1) {
                        child.mLatestParent = parent;
                        child.mStartTime = -1;
                        child.mEndTime = -1;
                    } else {
                        if (parent.mEndTime >= child.mStartTime) {
                            child.mLatestParent = parent;
                            child.mStartTime = parent.mEndTime;
                        }
                        child.mEndTime = child.mTotalDuration == -1 ? -1 : child.mStartTime + child.mTotalDuration;
                    }
                }
                updatePlayTime(child, visited);
            }
        }
        visited.remove(parent);
    }

    private void findSiblings(Node node, ArrayList<Node> siblings) {
        if (!siblings.contains(node)) {
            siblings.add(node);
            if (node.mSiblings != null) {
                for (int i = 0; i < node.mSiblings.size(); i++) {
                    findSiblings((Node) node.mSiblings.get(i), siblings);
                }
            }
        }
    }

    public boolean shouldPlayTogether() {
        updateAnimatorsDuration();
        createDependencyGraph();
        return this.mRootNode.mChildNodes == null || this.mRootNode.mChildNodes.size() == this.mNodes.size() - 1;
    }

    public long getTotalDuration() {
        updateAnimatorsDuration();
        createDependencyGraph();
        return this.mTotalDuration;
    }

    private Node getNodeForAnimation(Animator anim) {
        Node node = (Node) this.mNodeMap.get(anim);
        if (node != null) {
            return node;
        }
        node = new Node(anim);
        this.mNodeMap.put(anim, node);
        this.mNodes.add(node);
        return node;
    }
}
