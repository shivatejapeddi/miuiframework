package miui.maml.elements;

import android.content.Intent;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import miui.maml.ActionCommand;
import miui.maml.CommandTrigger;
import miui.maml.ScreenElementRoot;
import miui.maml.animation.interpolater.InterpolatorHelper;
import miui.maml.data.Expression;
import miui.maml.data.IndexedVariable;
import miui.maml.data.VariableNames;
import miui.maml.data.Variables;
import miui.maml.util.IntentInfo;
import miui.maml.util.Task;
import miui.maml.util.Utils;
import miui.maml.util.Utils.Point;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AdvancedSlider extends ElementGroup {
    private static final boolean DEBUG = false;
    private static final int DEFAULT_DRAG_TOLERANCE = 150;
    private static final float FREE_ENDPOINT_DIST = 1.7014117E38f;
    private static final String LOG_TAG = "LockScreen_AdvancedSlider";
    public static final String MOVE_DIST = "move_dist";
    public static final String MOVE_X = "move_x";
    public static final String MOVE_Y = "move_y";
    private static final float NONE_ENDPOINT_DIST = Float.MAX_VALUE;
    public static final int SLIDER_STATE_NORMAL = 0;
    public static final int SLIDER_STATE_PRESSED = 1;
    public static final int SLIDER_STATE_REACHED = 2;
    public static final String STATE = "state";
    public static final String TAG_NAME = "Slider";
    private EndPoint mCurrentEndPoint;
    private ArrayList<EndPoint> mEndPoints;
    protected boolean mIsHaptic;
    private boolean mIsKeepStatusAfterLaunch;
    private IndexedVariable mMoveDistVar;
    private IndexedVariable mMoveXVar;
    private IndexedVariable mMoveYVar;
    private boolean mMoving;
    private OnLaunchListener mOnLaunchListener;
    private ReboundAnimationController mReboundAnimationController;
    private StartPoint mStartPoint;
    private boolean mStartPointPressed;
    private IndexedVariable mStateVar;
    private float mTouchOffsetX;
    private float mTouchOffsetY;

    /* renamed from: miui.maml.elements.AdvancedSlider$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$miui$maml$elements$AdvancedSlider$State = new int[State.values().length];

        static {
            try {
                $SwitchMap$miui$maml$elements$AdvancedSlider$State[State.Normal.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$miui$maml$elements$AdvancedSlider$State[State.Pressed.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$miui$maml$elements$AdvancedSlider$State[State.Reached.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private class CheckTouchResult {
        public EndPoint endPoint;
        public boolean reached;

        private CheckTouchResult() {
        }

        /* synthetic */ CheckTouchResult(AdvancedSlider x0, AnonymousClass1 x1) {
            this();
        }
    }

    private class SliderPoint extends ElementGroup {
        private ScreenElement mCurrentStateElements;
        protected boolean mIsAlignChildren;
        protected String mName;
        protected String mNormalSound;
        protected ElementGroup mNormalStateElements;
        @Deprecated
        private CommandTrigger mNormalStateTrigger;
        private IndexedVariable mPointStateVar;
        protected String mPressedSound;
        protected ElementGroup mPressedStateElements;
        @Deprecated
        private CommandTrigger mPressedStateTrigger;
        protected String mReachedSound;
        private ElementGroup mReachedStateElements;
        @Deprecated
        private CommandTrigger mReachedStateTrigger;
        private State mState = State.Invalid;

        public SliderPoint(Element node, ScreenElementRoot root, String tag) {
            super(node, root);
            load(node, tag);
        }

        /* Access modifiers changed, original: protected */
        public ScreenElement onCreateChild(Element ele) {
            String tag = ele.getTagName();
            ElementGroup elementGroup;
            if (tag.equalsIgnoreCase("NormalState")) {
                elementGroup = new ElementGroup(ele, this.mRoot);
                this.mNormalStateElements = elementGroup;
                return elementGroup;
            } else if (tag.equalsIgnoreCase("PressedState")) {
                elementGroup = new ElementGroup(ele, this.mRoot);
                this.mPressedStateElements = elementGroup;
                return elementGroup;
            } else if (!tag.equalsIgnoreCase("ReachedState")) {
                return super.onCreateChild(ele);
            } else {
                elementGroup = new ElementGroup(ele, this.mRoot);
                this.mReachedStateElements = elementGroup;
                return elementGroup;
            }
        }

        private void load(Element node, String tag) {
            this.mName = getAttr(node, "name");
            this.mNormalSound = getAttr(node, "normalSound");
            this.mPressedSound = getAttr(node, "pressedSound");
            this.mReachedSound = getAttr(node, "reachedSound");
            this.mNormalStateTrigger = loadTrigger(node, "NormalState");
            this.mPressedStateTrigger = loadTrigger(node, "PressedState");
            this.mReachedStateTrigger = loadTrigger(node, "ReachedState");
            if (!TextUtils.isEmpty(this.mName)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(".");
                stringBuilder.append("state");
                this.mPointStateVar = new IndexedVariable(stringBuilder.toString(), getVariables(), true);
            }
            this.mIsAlignChildren = Boolean.parseBoolean(getAttr(node, "alignChildren"));
        }

        private CommandTrigger loadTrigger(Element node, String tag) {
            Element ele = Utils.getChild(node, tag);
            if (ele != null) {
                return CommandTrigger.fromParentElement(ele, this.mRoot);
            }
            return null;
        }

        public void init() {
            super.init();
            ElementGroup elementGroup = this.mNormalStateElements;
            if (elementGroup != null) {
                elementGroup.show(true);
            }
            elementGroup = this.mPressedStateElements;
            if (elementGroup != null) {
                elementGroup.show(false);
            }
            elementGroup = this.mReachedStateElements;
            if (elementGroup != null) {
                elementGroup.show(false);
            }
            setState(State.Normal);
            CommandTrigger commandTrigger = this.mNormalStateTrigger;
            if (commandTrigger != null) {
                commandTrigger.init();
            }
            commandTrigger = this.mPressedStateTrigger;
            if (commandTrigger != null) {
                commandTrigger.init();
            }
            commandTrigger = this.mReachedStateTrigger;
            if (commandTrigger != null) {
                commandTrigger.init();
            }
        }

        /* Access modifiers changed, original: protected */
        public void doRender(Canvas c) {
            c.save();
            if (!this.mIsAlignChildren) {
                c.translate(-getLeft(), -getTop());
            }
            super.doRender(c);
            c.restore();
        }

        /* Access modifiers changed, original: protected */
        public float getParentLeft() {
            float f = 0.0f;
            float left = this.mIsAlignChildren ? getLeft() : 0.0f;
            if (this.mParent != null) {
                f = this.mParent.getParentLeft();
            }
            return left + f;
        }

        /* Access modifiers changed, original: protected */
        public float getParentTop() {
            float f = 0.0f;
            float top = this.mIsAlignChildren ? getTop() : 0.0f;
            if (this.mParent != null) {
                f = this.mParent.getParentTop();
            }
            return top + f;
        }

        public void setState(State s) {
            if (this.mState != s) {
                ScreenElement screenElement;
                State preState = this.mState;
                this.mState = s;
                ScreenElement ele = null;
                boolean reset = false;
                int state = 0;
                int i = AnonymousClass1.$SwitchMap$miui$maml$elements$AdvancedSlider$State[s.ordinal()];
                if (i == 1) {
                    ele = this.mNormalStateElements;
                    reset = this.mPressedStateElements != null;
                } else if (i == 2) {
                    screenElement = this.mPressedStateElements;
                    if (screenElement == null) {
                        screenElement = this.mNormalStateElements;
                    }
                    ele = screenElement;
                    boolean z = (this.mPressedStateElements == null || AdvancedSlider.this.mStartPointPressed) ? false : true;
                    reset = z;
                    state = 1;
                } else if (i == 3) {
                    screenElement = this.mReachedStateElements;
                    if (screenElement == null) {
                        screenElement = this.mPressedStateElements;
                        if (screenElement == null) {
                            screenElement = this.mNormalStateElements;
                        }
                    }
                    ele = screenElement;
                    reset = this.mReachedStateElements != null;
                    state = 2;
                }
                screenElement = this.mCurrentStateElements;
                if (screenElement != ele) {
                    if (screenElement != null) {
                        screenElement.show(false);
                    }
                    if (ele != null) {
                        ele.show(true);
                    }
                    this.mCurrentStateElements = ele;
                }
                if (ele != null && reset) {
                    ele.reset();
                }
                IndexedVariable indexedVariable = this.mPointStateVar;
                if (indexedVariable != null) {
                    indexedVariable.set((double) state);
                }
                onStateChange(preState, this.mState);
            }
        }

        public State getState() {
            return this.mState;
        }

        /* Access modifiers changed, original: protected */
        public void onStateChange(State pre, State s) {
            int i = AnonymousClass1.$SwitchMap$miui$maml$elements$AdvancedSlider$State[s.ordinal()];
            CommandTrigger commandTrigger;
            if (i == 1) {
                commandTrigger = this.mNormalStateTrigger;
                if (commandTrigger != null) {
                    commandTrigger.perform();
                }
                performAction("normal");
            } else if (i == 2) {
                commandTrigger = this.mPressedStateTrigger;
                if (commandTrigger != null) {
                    commandTrigger.perform();
                }
                performAction("pressed");
                i = AnonymousClass1.$SwitchMap$miui$maml$elements$AdvancedSlider$State[pre.ordinal()];
                if (i == 1) {
                    performAction("pressed_normal");
                } else if (i == 3) {
                    performAction("pressed_reached");
                }
            } else if (i == 3) {
                commandTrigger = this.mReachedStateTrigger;
                if (commandTrigger != null) {
                    commandTrigger.perform();
                }
                performAction("reached");
            }
        }
    }

    private class EndPoint extends SliderPoint {
        public static final String TAG_NAME = "EndPoint";
        public LaunchAction mAction;
        private ArrayList<Position> mPath;
        private Expression mPathX;
        private Expression mPathY;
        private int mRawTolerance = 150;
        private float mTolerance;

        public EndPoint(Element node, ScreenElementRoot root) {
            super(node, root, TAG_NAME);
            load(node);
        }

        private void load(Element node) {
            loadTask(node);
            loadPath(node);
        }

        public void init() {
            super.init();
            LaunchAction launchAction = this.mAction;
            if (launchAction != null) {
                launchAction.init();
            }
            this.mTolerance = scale((double) this.mRawTolerance);
        }

        public void finish() {
            super.finish();
            LaunchAction launchAction = this.mAction;
            if (launchAction != null) {
                launchAction.finish();
            }
        }

        public void pause() {
            super.pause();
            LaunchAction launchAction = this.mAction;
            if (launchAction != null) {
                launchAction.pause();
            }
        }

        public void resume() {
            super.resume();
            LaunchAction launchAction = this.mAction;
            if (launchAction != null) {
                launchAction.resume();
            }
        }

        private Point getNearestPoint(float x, float y) {
            if (this.mPath == null) {
                return new Point((double) (x - AdvancedSlider.this.mTouchOffsetX), (double) (y - AdvancedSlider.this.mTouchOffsetY));
            }
            Point pos = null;
            double dist = Double.MAX_VALUE;
            for (int i = 1; i < this.mPath.size(); i++) {
                float x0 = x - AdvancedSlider.this.mTouchOffsetX;
                float y0 = y - AdvancedSlider.this.mTouchOffsetY;
                Position pt1 = (Position) this.mPath.get(i - 1);
                Position pt2 = (Position) this.mPath.get(i);
                Point p1 = new Point((double) pt1.getX(), (double) pt1.getY());
                Point p2 = new Point((double) pt2.getX(), (double) pt2.getY());
                Point p0 = new Point((double) x0, (double) y0);
                Point pt = Utils.pointProjectionOnSegment(p1, p2, p0, true);
                double d = Utils.Dist(pt, p0, 0.0d);
                if (d < dist) {
                    dist = d;
                    pos = pt;
                }
            }
            return pos;
        }

        public float getTransformedDist(Point pt, float x, float y) {
            if (this.mPath == null) {
                return AdvancedSlider.FREE_ENDPOINT_DIST;
            }
            if (pt == null) {
                return Float.MAX_VALUE;
            }
            float dist = (float) Utils.Dist(pt, new Point((double) (x - AdvancedSlider.this.mTouchOffsetX), (double) (y - AdvancedSlider.this.mTouchOffsetY)), true);
            if (dist < this.mTolerance) {
                return dist;
            }
            return Float.MAX_VALUE;
        }

        private void loadPath(Element node) {
            Element ele = Utils.getChild(node, "Path");
            if (ele == null) {
                this.mPath = null;
                return;
            }
            this.mRawTolerance = getAttrAsInt(ele, "tolerance", 150);
            this.mPath = new ArrayList();
            Variables vars = getVariables();
            this.mPathX = Expression.build(vars, ele.getAttribute("x"));
            this.mPathY = Expression.build(vars, ele.getAttribute("y"));
            NodeList nodeList = ele.getElementsByTagName("Position");
            for (int i = 0; i < nodeList.getLength(); i++) {
                this.mPath.add(new Position(vars, (Element) nodeList.item(i), this.mPathX, this.mPathY));
            }
        }

        private void loadTask(Element node) {
            Element intentEle = Utils.getChild(node, "Intent");
            Element commandEle = Utils.getChild(node, ActionCommand.TAG_NAME);
            Element triggerEle = Utils.getChild(node, CommandTrigger.TAG_NAME);
            if (intentEle != null || commandEle != null || triggerEle != null) {
                this.mAction = new LaunchAction(AdvancedSlider.this, null);
                if (intentEle != null) {
                    this.mAction.mIntentInfo = new IntentInfo(intentEle, getVariables());
                } else if (commandEle != null) {
                    this.mAction.mCommand = ActionCommand.create(commandEle, this.mRoot);
                    if (this.mAction.mCommand == null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("invalid Command element: ");
                        stringBuilder.append(commandEle.toString());
                        Log.w(AdvancedSlider.LOG_TAG, stringBuilder.toString());
                    }
                } else if (triggerEle != null) {
                    this.mAction.mTrigger = new CommandTrigger(triggerEle, this.mRoot);
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public void onStateChange(State pre, State s) {
            if (pre != State.Invalid) {
                if (AnonymousClass1.$SwitchMap$miui$maml$elements$AdvancedSlider$State[s.ordinal()] == 3) {
                    this.mRoot.playSound(this.mReachedSound);
                }
                super.onStateChange(pre, s);
            }
        }
    }

    private abstract class ReboundAnimationController implements ITicker {
        private int mBounceStartPointIndex;
        private EndPoint mEndPoint;
        private long mPreDistance;
        protected long mStartTime;
        private float mStartX;
        private float mStartY;
        protected double mTotalDistance;

        public abstract long getDistance(long j);

        private ReboundAnimationController() {
            this.mStartTime = -1;
        }

        /* synthetic */ ReboundAnimationController(AdvancedSlider x0, AnonymousClass1 x1) {
            this();
        }

        public void init() {
            this.mStartTime = -1;
        }

        public void start(EndPoint ep) {
            this.mStartTime = 0;
            this.mEndPoint = ep;
            this.mStartX = AdvancedSlider.this.mStartPoint.getOffsetX() + AdvancedSlider.this.mStartPoint.getAnchorX();
            this.mStartY = AdvancedSlider.this.mStartPoint.getOffsetY() + AdvancedSlider.this.mStartPoint.getAnchorY();
            this.mBounceStartPointIndex = -1;
            this.mTotalDistance = 0.0d;
            Point p0 = new Point((double) this.mStartX, (double) this.mStartY);
            if (ep == null || ep.mPath == null) {
                this.mTotalDistance = Utils.Dist(new Point((double) AdvancedSlider.this.mStartPoint.getAnchorX(), (double) AdvancedSlider.this.mStartPoint.getAnchorY()), p0, true);
            } else {
                for (int i = 1; i < ep.mPath.size(); i++) {
                    Position pt1 = (Position) ep.mPath.get(i - 1);
                    Position pt2 = (Position) ep.mPath.get(i);
                    Point p1 = new Point((double) pt1.getX(), (double) pt1.getY());
                    Point p2 = new Point((double) pt2.getX(), (double) pt2.getY());
                    Point pt = Utils.pointProjectionOnSegment(p1, p2, p0, null);
                    if (pt != null) {
                        this.mBounceStartPointIndex = i - 1;
                        this.mTotalDistance += Utils.Dist(p1, pt, true);
                        break;
                    }
                    this.mTotalDistance += Utils.Dist(p1, p2, true);
                }
            }
            if (this.mTotalDistance < 3.0d) {
                onStop();
                return;
            }
            onStart();
            AdvancedSlider.this.requestUpdate();
        }

        /* Access modifiers changed, original: protected */
        public void onStart() {
        }

        /* Access modifiers changed, original: protected */
        public void onStop() {
            this.mStartTime = -1;
            AdvancedSlider.this.cancelMoving();
        }

        public void tick(long currentTime) {
            long j = currentTime;
            long j2 = this.mStartTime;
            if (j2 >= 0) {
                if (j2 == 0) {
                    this.mStartTime = j;
                    this.mPreDistance = 0;
                } else {
                    long dist = getDistance(j - j2);
                    if (this.mStartTime >= 0) {
                        EndPoint endPoint = this.mEndPoint;
                        Point pt;
                        if (endPoint == null || endPoint.mPath == null) {
                            pt = getPoint(AdvancedSlider.this.mStartPoint.getAnchorX(), AdvancedSlider.this.mStartPoint.getAnchorY(), this.mStartX, this.mStartY, dist);
                            if (pt == null) {
                                onStop();
                            } else {
                                onMove((float) pt.x, (float) pt.y);
                            }
                        } else {
                            long d = dist - this.mPreDistance;
                            float x2 = AdvancedSlider.this.mStartPoint.getOffsetX() + AdvancedSlider.this.mStartPoint.getAnchorX();
                            float y2 = AdvancedSlider.this.mStartPoint.getOffsetY() + AdvancedSlider.this.mStartPoint.getAnchorY();
                            int i = this.mBounceStartPointIndex;
                            long d2 = d;
                            while (i >= 0) {
                                Position pt1 = (Position) this.mEndPoint.mPath.get(i);
                                long d3 = d2;
                                float y22 = y2;
                                pt = getPoint(pt1.getX(), pt1.getY(), x2, y2, d3);
                                if (pt != null) {
                                    this.mBounceStartPointIndex = i;
                                    onMove((float) pt.x, (float) pt.y);
                                    break;
                                } else if (i == 0) {
                                    onStop();
                                    break;
                                } else {
                                    d2 = (long) (((double) d3) - Utils.Dist(new Point((double) pt1.getX(), (double) pt1.getY()), new Point((double) x2, (double) y22), true));
                                    x2 = pt1.getX();
                                    y2 = pt1.getY();
                                    i--;
                                    j = currentTime;
                                }
                            }
                        }
                        this.mPreDistance = dist;
                    } else {
                        return;
                    }
                }
                AdvancedSlider.this.requestUpdate();
            }
        }

        /* Access modifiers changed, original: protected */
        public void onMove(float x, float y) {
            AdvancedSlider.this.moveStartPoint(x, y);
        }

        private Point getPoint(float x1, float y1, float x2, float y2, long distance) {
            long j = distance;
            Point pt1 = new Point((double) x1, (double) y1);
            Point pt2 = new Point((double) x2, (double) y2);
            double total = Utils.Dist(pt1, pt2, Double.MIN_VALUE);
            if (((double) j) >= total) {
                return null;
            }
            double ratio = (total - ((double) j)) / total;
            return new Point(pt1.x + ((pt2.x - pt1.x) * ratio), pt1.y + ((pt2.y - pt1.y) * ratio));
        }

        public boolean isRunning() {
            return this.mStartTime >= 0;
        }

        public void stopRunning() {
            this.mStartTime = -1;
        }
    }

    private class InterpolatorController extends ReboundAnimationController {
        private InterpolatorHelper mInterpolator;
        private long mReboundTime;
        private Expression mReboundTimeExp;

        public InterpolatorController(InterpolatorHelper iph, Expression time) {
            super(AdvancedSlider.this, null);
            this.mInterpolator = iph;
            this.mReboundTimeExp = time;
        }

        /* Access modifiers changed, original: protected */
        public void onStart() {
            this.mReboundTime = (long) this.mReboundTimeExp.evaluate();
        }

        /* Access modifiers changed, original: protected */
        public long getDistance(long time) {
            long j = this.mReboundTime;
            if (time >= j) {
                onStop();
                return (long) this.mTotalDistance;
            }
            return (long) (this.mTotalDistance * ((double) this.mInterpolator.get(((float) time) / ((float) j))));
        }
    }

    private class LaunchAction {
        public ActionCommand mCommand;
        public boolean mConfigTaskLoaded;
        public IntentInfo mIntentInfo;
        public CommandTrigger mTrigger;

        private LaunchAction() {
        }

        /* synthetic */ LaunchAction(AdvancedSlider x0, AnonymousClass1 x1) {
            this();
        }

        public Intent perform() {
            if (this.mIntentInfo != null) {
                return performTask();
            }
            ActionCommand actionCommand = this.mCommand;
            if (actionCommand != null) {
                actionCommand.perform();
            } else {
                CommandTrigger commandTrigger = this.mTrigger;
                if (commandTrigger != null) {
                    commandTrigger.perform();
                }
            }
            return null;
        }

        private Intent performTask() {
            if (this.mIntentInfo == null) {
                return null;
            }
            if (!this.mConfigTaskLoaded) {
                Task configTask = AdvancedSlider.this.mRoot.findTask(this.mIntentInfo.getId());
                if (!(configTask == null || TextUtils.isEmpty(configTask.action))) {
                    this.mIntentInfo.set(configTask);
                }
                this.mConfigTaskLoaded = true;
            }
            if (Utils.isProtectedIntent(this.mIntentInfo.getAction())) {
                return null;
            }
            Intent intent = new Intent();
            this.mIntentInfo.update(intent);
            intent.setFlags(872415232);
            return intent;
        }

        public void finish() {
            ActionCommand actionCommand = this.mCommand;
            if (actionCommand != null) {
                actionCommand.finish();
            }
            CommandTrigger commandTrigger = this.mTrigger;
            if (commandTrigger != null) {
                commandTrigger.finish();
            }
            this.mConfigTaskLoaded = false;
        }

        public void init() {
            ActionCommand actionCommand = this.mCommand;
            if (actionCommand != null) {
                actionCommand.init();
            }
            CommandTrigger commandTrigger = this.mTrigger;
            if (commandTrigger != null) {
                commandTrigger.init();
            }
        }

        public void pause() {
            ActionCommand actionCommand = this.mCommand;
            if (actionCommand != null) {
                actionCommand.pause();
            }
            CommandTrigger commandTrigger = this.mTrigger;
            if (commandTrigger != null) {
                commandTrigger.pause();
            }
        }

        public void resume() {
            ActionCommand actionCommand = this.mCommand;
            if (actionCommand != null) {
                actionCommand.resume();
            }
            CommandTrigger commandTrigger = this.mTrigger;
            if (commandTrigger != null) {
                commandTrigger.resume();
            }
        }
    }

    public interface OnLaunchListener {
        boolean onLaunch(String str);
    }

    private class Position {
        public static final String TAG_NAME = "Position";
        private Expression mBaseX;
        private Expression mBaseY;
        private Expression mX;
        private Expression mY;

        public Position(Variables vars, Element node, Expression baseX, Expression baseY) {
            this.mBaseX = baseX;
            this.mBaseY = baseY;
            this.mX = Expression.build(vars, AdvancedSlider.this.getAttr(node, "x"));
            this.mY = Expression.build(vars, AdvancedSlider.this.getAttr(node, "y"));
        }

        public float getX() {
            AdvancedSlider advancedSlider = AdvancedSlider.this;
            Expression expression = this.mX;
            double d = 0.0d;
            double evaluate = expression == null ? 0.0d : expression.evaluate();
            expression = this.mBaseX;
            if (expression != null) {
                d = expression.evaluate();
            }
            return advancedSlider.scale(evaluate + d);
        }

        public float getY() {
            AdvancedSlider advancedSlider = AdvancedSlider.this;
            Expression expression = this.mY;
            double d = 0.0d;
            double evaluate = expression == null ? 0.0d : expression.evaluate();
            expression = this.mBaseY;
            if (expression != null) {
                d = expression.evaluate();
            }
            return advancedSlider.scale(evaluate + d);
        }
    }

    private class SpeedAccController extends ReboundAnimationController implements ITicker {
        private int mBounceAccelation;
        private Expression mBounceAccelationExp;
        private int mBounceInitSpeed;
        private Expression mBounceInitSpeedExp;
        private IndexedVariable mBounceProgress;

        public SpeedAccController(Element node) {
            super(AdvancedSlider.this, null);
            this.mBounceInitSpeedExp = Expression.build(AdvancedSlider.this.getVariables(), AdvancedSlider.this.getAttr(node, "bounceInitSpeed"));
            this.mBounceAccelationExp = Expression.build(AdvancedSlider.this.getVariables(), AdvancedSlider.this.getAttr(node, "bounceAcceleration"));
            if (AdvancedSlider.this.mHasName) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(AdvancedSlider.this.mName);
                stringBuilder.append(".");
                stringBuilder.append(VariableNames.BOUNCE_PROGRESS);
                this.mBounceProgress = new IndexedVariable(stringBuilder.toString(), AdvancedSlider.this.getVariables(), true);
            }
        }

        public void init() {
            super.init();
            IndexedVariable indexedVariable = this.mBounceProgress;
            if (indexedVariable != null) {
                indexedVariable.set(1.0d);
            }
        }

        public void start(EndPoint ep) {
            if (this.mBounceInitSpeedExp == null) {
                onStop();
            } else {
                super.start(ep);
            }
        }

        /* Access modifiers changed, original: protected */
        public void onStart() {
            Expression expression = this.mBounceInitSpeedExp;
            if (expression != null) {
                this.mBounceInitSpeed = (int) AdvancedSlider.this.evaluate(expression);
            }
            expression = this.mBounceAccelationExp;
            if (expression != null) {
                this.mBounceAccelation = (int) AdvancedSlider.this.evaluate(expression);
            }
            IndexedVariable indexedVariable = this.mBounceProgress;
            if (indexedVariable != null) {
                indexedVariable.set(0.0d);
            }
        }

        /* Access modifiers changed, original: protected */
        public long getDistance(long time) {
            IndexedVariable indexedVariable;
            int i = this.mBounceInitSpeed;
            long dist = (((long) i) * time) / 1000;
            int i2 = this.mBounceAccelation;
            dist += ((((long) i2) * time) * time) / 2000000;
            double d = 1.0d;
            if (((long) i) + ((((long) i2) * time) / 1000) <= 0) {
                onStop();
                indexedVariable = this.mBounceProgress;
                if (indexedVariable != null) {
                    indexedVariable.set(1.0d);
                }
            }
            if (this.mTotalDistance > 0.0d) {
                double progress = ((double) dist) / this.mTotalDistance;
                indexedVariable = this.mBounceProgress;
                if (indexedVariable != null) {
                    if (progress <= 1.0d) {
                        d = progress;
                    }
                    indexedVariable.set(d);
                }
            }
            return dist;
        }
    }

    private class StartPoint extends SliderPoint {
        public static final String TAG_NAME = "StartPoint";
        private float mAnchorX;
        private float mAnchorY;
        protected float mOffsetX;
        protected float mOffsetY;
        public InterpolatorController mReboundController;

        public StartPoint(Element node, ScreenElementRoot root) {
            super(node, root, TAG_NAME);
            this.mAnchorX = Utils.getAttrAsFloat(node, "anchorX", 0.0f);
            this.mAnchorY = Utils.getAttrAsFloat(node, "anchorY", 0.0f);
            InterpolatorHelper interpolator = InterpolatorHelper.create(getVariables(), node);
            Expression reboundTimeExp = Expression.build(getVariables(), node.getAttribute("easeTime"));
            if (interpolator != null && reboundTimeExp != null) {
                this.mReboundController = new InterpolatorController(interpolator, reboundTimeExp);
            }
        }

        /* Access modifiers changed, original: protected */
        public void onStateChange(State pre, State s) {
            if (pre != State.Invalid) {
                int i = AnonymousClass1.$SwitchMap$miui$maml$elements$AdvancedSlider$State[s.ordinal()];
                if (i == 1) {
                    this.mRoot.playSound(this.mNormalSound);
                } else if (i == 2 && !this.mPressed) {
                    this.mRoot.playSound(this.mPressedSound);
                }
                super.onStateChange(pre, s);
            }
        }

        public void init() {
            super.init();
            InterpolatorController interpolatorController = this.mReboundController;
            if (interpolatorController != null) {
                interpolatorController.init();
            }
        }

        /* Access modifiers changed, original: protected */
        public void doTick(long currentTime) {
            super.doTick(currentTime);
            InterpolatorController interpolatorController = this.mReboundController;
            if (interpolatorController != null) {
                interpolatorController.tick(currentTime);
            }
        }

        public void doRender(Canvas c) {
            int rs = c.save();
            c.translate(this.mOffsetX, this.mOffsetY);
            super.doRender(c);
            c.restoreToCount(rs);
        }

        public void moveTo(float x, float y) {
            this.mOffsetX = x;
            this.mOffsetY = y;
        }

        public float getOffsetX() {
            return this.mOffsetX;
        }

        public float getOffsetY() {
            return this.mOffsetY;
        }

        public float getAnchorX() {
            return getLeft() + this.mAnchorX;
        }

        public float getAnchorY() {
            return getTop() + this.mAnchorY;
        }
    }

    private enum State {
        Normal,
        Pressed,
        Reached,
        Invalid
    }

    public AdvancedSlider(Element node, ScreenElementRoot root) {
        super(node, root);
        load(node);
    }

    private void load(Element node) {
        if (node != null) {
            if (this.mHasName) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                String str = ".";
                stringBuilder.append(str);
                stringBuilder.append("state");
                this.mStateVar = new IndexedVariable(stringBuilder.toString(), getVariables(), true);
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(str);
                stringBuilder.append(MOVE_X);
                this.mMoveXVar = new IndexedVariable(stringBuilder.toString(), getVariables(), true);
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(str);
                stringBuilder.append(MOVE_Y);
                this.mMoveYVar = new IndexedVariable(stringBuilder.toString(), getVariables(), true);
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(str);
                stringBuilder.append(MOVE_DIST);
                this.mMoveDistVar = new IndexedVariable(stringBuilder.toString(), getVariables(), true);
            }
            StartPoint startPoint = this.mStartPoint;
            if (startPoint == null || startPoint.mReboundController == null) {
                this.mReboundAnimationController = new SpeedAccController(node);
                this.mRoot.addPreTicker(this.mReboundAnimationController);
            } else {
                this.mReboundAnimationController = this.mStartPoint.mReboundController;
            }
            this.mIsHaptic = Boolean.parseBoolean(getAttr(node, "haptic"));
            this.mIsKeepStatusAfterLaunch = Boolean.parseBoolean(getAttr(node, "keepStatusAfterLaunch"));
        }
    }

    /* Access modifiers changed, original: protected */
    public ScreenElement onCreateChild(Element ele) {
        String tag = ele.getTagName();
        if (tag.equalsIgnoreCase(StartPoint.TAG_NAME)) {
            StartPoint startPoint = new StartPoint(ele, this.mRoot);
            this.mStartPoint = startPoint;
            return startPoint;
        } else if (!tag.equalsIgnoreCase(EndPoint.TAG_NAME)) {
            return super.onCreateChild(ele);
        } else {
            EndPoint endPoint = new EndPoint(ele, this.mRoot);
            if (this.mEndPoints == null) {
                this.mEndPoints = new ArrayList();
            }
            this.mEndPoints.add(endPoint);
            return endPoint;
        }
    }

    public void setOnLaunchListener(OnLaunchListener l) {
        this.mOnLaunchListener = l;
    }

    public void init() {
        super.init();
        this.mReboundAnimationController.init();
        resetInner();
    }

    public void finish() {
        super.finish();
        resetInner();
    }

    public void pause() {
        super.pause();
        resetInner();
    }

    public boolean onTouch(MotionEvent event) {
        boolean z = false;
        if (!isVisible()) {
            return false;
        }
        float x = event.getX();
        boolean ret = false;
        x -= getAbsoluteLeft();
        float y = event.getY() - getAbsoluteTop();
        int actionMasked = event.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                boolean keepStatus = false;
                if (this.mMoving) {
                    Log.i(LOG_TAG, "unlock touch up");
                    CheckTouchResult result = checkTouch(x, y);
                    if (result != null) {
                        if (result.reached) {
                            keepStatus = doLaunch(result.endPoint);
                        } else {
                            String str = "release";
                            this.mStartPoint.performAction(str);
                            if (result.endPoint != null) {
                                result.endPoint.performAction(str);
                            }
                        }
                        this.mCurrentEndPoint = result.endPoint;
                    }
                    this.mMoving = false;
                    if (!keepStatus) {
                        this.mReboundAnimationController.start(this.mCurrentEndPoint);
                    }
                    onRelease();
                    ret = true;
                }
            } else if (actionMasked != 2) {
                if (actionMasked == 3 && this.mMoving) {
                    this.mReboundAnimationController.start(null);
                    this.mCurrentEndPoint = null;
                    this.mMoving = false;
                    onRelease();
                    this.mStartPoint.performAction("cancel");
                    ret = true;
                }
            } else if (this.mMoving) {
                CheckTouchResult result2 = checkTouch(x, y);
                if (result2 != null) {
                    this.mCurrentEndPoint = result2.endPoint;
                    onMove(x, y);
                } else {
                    this.mReboundAnimationController.start(this.mCurrentEndPoint);
                    this.mMoving = false;
                    onRelease();
                }
                ret = true;
            }
        } else if (this.mStartPoint.touched(x, y, false)) {
            this.mMoving = true;
            this.mTouchOffsetX = x - this.mStartPoint.getAnchorX();
            this.mTouchOffsetY = y - this.mStartPoint.getAnchorY();
            if (this.mReboundAnimationController.isRunning()) {
                this.mReboundAnimationController.stopRunning();
                this.mTouchOffsetX -= this.mStartPoint.getOffsetX();
                this.mTouchOffsetY -= this.mStartPoint.getOffsetY();
            }
            this.mStartPoint.setState(State.Pressed);
            Iterator it = this.mEndPoints.iterator();
            while (it.hasNext()) {
                ((EndPoint) it.next()).setState(State.Pressed);
            }
            this.mStartPointPressed = true;
            if (this.mHasName) {
                this.mStateVar.set(1.0d);
            }
            this.mReboundAnimationController.init();
            onStart();
            ret = true;
        }
        if (super.onTouch(event) || (ret && this.mInterceptTouch)) {
            z = true;
        }
        return z;
    }

    /* Access modifiers changed, original: protected */
    public void onMove(float x, float y) {
    }

    private CheckTouchResult checkTouch(float x, float y) {
        EndPoint ep;
        float minDist = Float.MAX_VALUE;
        Point point = null;
        CheckTouchResult result = new CheckTouchResult(this, null);
        Iterator it = this.mEndPoints.iterator();
        while (it.hasNext()) {
            ep = (EndPoint) it.next();
            Point pt = ep.getNearestPoint(x, y);
            float di = ep.getTransformedDist(pt, x, y);
            if (di < minDist) {
                minDist = di;
                point = pt;
                result.endPoint = ep;
            }
        }
        boolean reached = false;
        if (minDist < Float.MAX_VALUE) {
            boolean reached2;
            moveStartPoint((float) point.x, (float) point.y);
            if (minDist < FREE_ENDPOINT_DIST) {
                reached2 = checkEndPoint(point, result.endPoint);
            } else {
                Iterator it2 = this.mEndPoints.iterator();
                while (it2.hasNext()) {
                    ep = (EndPoint) it2.next();
                    if (ep.mPath == null) {
                        boolean checkEndPoint = checkEndPoint(point, ep);
                        reached = checkEndPoint;
                        if (checkEndPoint) {
                            result.endPoint = ep;
                            reached2 = reached;
                            break;
                        }
                    }
                }
                reached2 = reached;
            }
            this.mStartPoint.setState(reached2 ? State.Reached : State.Pressed);
            if (this.mHasName) {
                this.mStateVar.set(reached2 ? 2.0d : 1.0d);
            }
            result.reached = reached2;
            return result;
        }
        Log.i(LOG_TAG, "unlock touch canceled due to exceeding tollerance");
        this.mStartPoint.performAction("cancel");
        return null;
    }

    private void moveStartPoint(float x, float y) {
        x -= this.mStartPoint.getAnchorX();
        y -= this.mStartPoint.getAnchorY();
        this.mStartPoint.moveTo(x, y);
        if (this.mHasName) {
            double move_x = descale((double) x);
            double move_y = descale((double) y);
            double move_dist = Math.sqrt((move_x * move_x) + (move_y * move_y));
            this.mMoveXVar.set(move_x);
            this.mMoveYVar.set(move_y);
            this.mMoveDistVar.set(move_dist);
        }
    }

    private boolean checkEndPoint(Point point, EndPoint endPoint) {
        if (endPoint.touched((float) point.x, (float) point.y, false)) {
            if (endPoint.getState() != State.Reached) {
                endPoint.setState(State.Reached);
                Iterator it = this.mEndPoints.iterator();
                while (it.hasNext()) {
                    EndPoint ep = (EndPoint) it.next();
                    if (ep != endPoint) {
                        ep.setState(State.Pressed);
                    }
                }
                onReach(endPoint.mName);
            }
            return true;
        }
        endPoint.setState(State.Pressed);
        return false;
    }

    private boolean doLaunch(EndPoint endPoint) {
        String str = "launch";
        this.mStartPoint.performAction(str);
        endPoint.performAction(str);
        Intent intent = null;
        if (endPoint.mAction != null) {
            intent = endPoint.mAction.perform();
        }
        return onLaunch(endPoint.mName, intent);
    }

    /* Access modifiers changed, original: protected */
    public void onStart() {
        if (this.mIsHaptic) {
            this.mRoot.haptic(1);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCancel() {
    }

    /* Access modifiers changed, original: protected */
    public void onRelease() {
        if (this.mIsHaptic) {
            this.mRoot.haptic(1);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onReach(String name) {
        if (this.mIsHaptic) {
            this.mRoot.haptic(0);
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean onLaunch(String name, Intent intent) {
        OnLaunchListener onLaunchListener = this.mOnLaunchListener;
        if (onLaunchListener != null) {
            return onLaunchListener.onLaunch(name);
        }
        return this.mIsKeepStatusAfterLaunch;
    }

    private void cancelMoving() {
        resetInner();
        onCancel();
    }

    /* Access modifiers changed, original: protected */
    public void resetInner() {
        if (this.mStartPointPressed) {
            this.mStartPointPressed = false;
            this.mStartPoint.moveTo(0.0f, 0.0f);
            this.mStartPoint.setState(State.Normal);
            Iterator it = this.mEndPoints.iterator();
            while (it.hasNext()) {
                ((EndPoint) it.next()).setState(State.Normal);
            }
            if (this.mHasName) {
                this.mMoveXVar.set(0.0d);
                this.mMoveYVar.set(0.0d);
                this.mMoveDistVar.set(0.0d);
                this.mStateVar.set(0.0d);
            }
            this.mMoving = false;
            requestUpdate();
        }
    }

    public void reset(long time) {
        super.reset(time);
        resetInner();
    }
}
