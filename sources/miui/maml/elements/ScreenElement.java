package miui.maml.elements;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MotionEvent;
import java.util.ArrayList;
import miui.maml.CommandTriggers;
import miui.maml.FramerateTokenList.FramerateToken;
import miui.maml.NotifierManager;
import miui.maml.RendererController;
import miui.maml.ScreenContext;
import miui.maml.ScreenElementRoot;
import miui.maml.StylesManager.Style;
import miui.maml.animation.BaseAnimation;
import miui.maml.data.Expression;
import miui.maml.data.IndexedVariable;
import miui.maml.data.Variables;
import miui.maml.util.StyleHelper;
import miui.maml.util.Utils;
import miui.maml.util.Utils.XmlTraverseListener;
import org.w3c.dom.Element;

public abstract class ScreenElement {
    public static final String ACTUAL_H = "actual_h";
    public static final String ACTUAL_W = "actual_w";
    public static final String ACTUAL_X = "actual_x";
    public static final String ACTUAL_Y = "actual_y";
    private static final String LOG_TAG = "MAML ScreenElement";
    public static final String VISIBILITY = "visibility";
    public static final int VISIBILITY_FALSE = 0;
    public static final int VISIBILITY_TRUE = 1;
    private IndexedVariable mActualHeightVar;
    private IndexedVariable mActualWidthVar;
    protected Align mAlign;
    protected AlignV mAlignV;
    protected ArrayList<BaseAnimation> mAnimations;
    protected RendererController mAvailableController;
    protected String mCategory;
    private float mCurFramerate;
    private FramerateToken mFramerateToken;
    protected boolean mHasName;
    private boolean mInitShow = true;
    private boolean mIsVisible = true;
    protected String mName;
    protected ElementGroup mParent;
    protected ScreenElementRoot mRoot;
    private boolean mShow = true;
    protected Style mStyle;
    protected CommandTriggers mTriggers;
    private Expression mVisibilityExpression;
    private IndexedVariable mVisibilityVar;

    /* renamed from: miui.maml.elements.ScreenElement$2 */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$miui$maml$elements$ScreenElement$Align = new int[Align.values().length];
        static final /* synthetic */ int[] $SwitchMap$miui$maml$elements$ScreenElement$AlignV = new int[AlignV.values().length];

        static {
            try {
                $SwitchMap$miui$maml$elements$ScreenElement$Align[Align.CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$miui$maml$elements$ScreenElement$Align[Align.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$miui$maml$elements$ScreenElement$AlignV[AlignV.CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$miui$maml$elements$ScreenElement$AlignV[AlignV.BOTTOM.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    protected enum Align {
        LEFT,
        CENTER,
        RIGHT
    }

    protected enum AlignV {
        TOP,
        CENTER,
        BOTTOM
    }

    public abstract void doRender(Canvas canvas);

    public ScreenElement(Element node, ScreenElementRoot root) {
        this.mRoot = root;
        if (!(node == null || root == null)) {
            this.mStyle = root.getStyle(node.getAttribute(TtmlUtils.TAG_STYLE));
        }
        load(node);
    }

    private void load(Element node) {
        if (node != null) {
            this.mCategory = getAttr(node, "category");
            this.mName = getAttr(node, "name");
            this.mHasName = TextUtils.isEmpty(this.mName) ^ 1;
            String vis = getAttr(node, "visibility");
            if (!TextUtils.isEmpty(vis)) {
                if (vis.equalsIgnoreCase("false")) {
                    this.mInitShow = false;
                } else if (vis.equalsIgnoreCase("true")) {
                    this.mInitShow = true;
                } else {
                    this.mVisibilityExpression = Expression.build(getVariables(), vis);
                }
            }
            this.mAlign = Align.LEFT;
            String align = getAttr(node, "align");
            if (TextUtils.isEmpty(align)) {
                align = getAttr(node, "alignH");
            }
            String str = "center";
            if (align.equalsIgnoreCase("right")) {
                this.mAlign = Align.RIGHT;
            } else if (align.equalsIgnoreCase("left")) {
                this.mAlign = Align.LEFT;
            } else if (align.equalsIgnoreCase(str)) {
                this.mAlign = Align.CENTER;
            }
            this.mAlignV = AlignV.TOP;
            align = getAttr(node, "alignV");
            if (align.equalsIgnoreCase("bottom")) {
                this.mAlignV = AlignV.BOTTOM;
            } else if (align.equalsIgnoreCase("top")) {
                this.mAlignV = AlignV.TOP;
            } else if (align.equalsIgnoreCase(str)) {
                this.mAlignV = AlignV.CENTER;
            }
            loadTriggers(node);
            loadAnimations(node);
        }
    }

    /* Access modifiers changed, original: protected */
    public void loadTriggers(Element node) {
        Element triggers = Utils.getChild(node, CommandTriggers.TAG_NAME);
        if (triggers != null) {
            this.mTriggers = new CommandTriggers(triggers, this);
        }
    }

    private void loadAnimations(Element node) {
        Utils.traverseXmlElementChildren(node, null, new XmlTraverseListener() {
            public void onChild(Element child) {
                String tag = child.getNodeName();
                if (tag.endsWith("Animation")) {
                    BaseAnimation ani = ScreenElement.this.onCreateAnimation(tag, child);
                    if (ani != null) {
                        if (ScreenElement.this.mAnimations == null) {
                            ScreenElement.this.mAnimations = new ArrayList();
                        }
                        ScreenElement.this.mAnimations.add(ani);
                    }
                }
            }
        });
    }

    /* Access modifiers changed, original: protected */
    public BaseAnimation onCreateAnimation(String tag, Element ele) {
        return null;
    }

    /* Access modifiers changed, original: protected */
    public String getAttr(Element node, String attrName) {
        return StyleHelper.getAttr(node, attrName, this.mStyle);
    }

    /* Access modifiers changed, original: protected */
    public int getAttrAsInt(Element node, String attrName, int def) {
        try {
            return Integer.parseInt(getAttr(node, attrName));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /* Access modifiers changed, original: protected */
    public long getAttrAsLong(Element node, String attrName, long def) {
        try {
            return Long.parseLong(getAttr(node, attrName));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /* Access modifiers changed, original: protected */
    public float getAttrAsFloat(Element node, String attrName, float def) {
        try {
            return Float.parseFloat(getAttr(node, attrName));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public void show(boolean show) {
        this.mShow = show;
        updateVisibility();
        requestUpdate();
    }

    public boolean isVisible() {
        return this.mIsVisible;
    }

    public void showCategory(String category, boolean show) {
        String str = this.mCategory;
        if (str != null && str.equals(category)) {
            show(show);
        }
    }

    public ScreenElement findElement(String name) {
        String str = this.mName;
        return (str == null || !str.equals(name)) ? null : this;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getName() {
        return this.mName;
    }

    public void setParent(ElementGroup parent) {
        this.mParent = parent;
    }

    public ElementGroup getParent() {
        return this.mParent;
    }

    public void init() {
        this.mShow = this.mInitShow;
        this.mFramerateToken = null;
        CommandTriggers commandTriggers = this.mTriggers;
        if (commandTriggers != null) {
            commandTriggers.init();
        }
        setAnim(null);
        int N = this.mAnimations;
        if (N != 0) {
            N = N.size();
            for (int i = 0; i < N; i++) {
                ((BaseAnimation) this.mAnimations.get(i)).init();
            }
        }
        updateVisibility();
        setVisibilityVar(isVisible());
        performAction("init");
    }

    /* Access modifiers changed, original: protected */
    public void updateVisibility() {
        boolean v = isVisibleInner();
        if (this.mIsVisible != v) {
            this.mIsVisible = v;
            onVisibilityChange(this.mIsVisible);
            if (v) {
                doTick(SystemClock.elapsedRealtime());
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onVisibilityChange(boolean visible) {
        setVisibilityVar(visible);
        if (visible) {
            requestFramerate(this.mCurFramerate);
            return;
        }
        this.mCurFramerate = getFramerate();
        requestFramerate(0.0f);
    }

    private void setVisibilityVar(boolean visible) {
        if (this.mHasName) {
            if (this.mVisibilityVar == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(".");
                stringBuilder.append("visibility");
                this.mVisibilityVar = new IndexedVariable(stringBuilder.toString(), getContext().mVariables, true);
            }
            this.mVisibilityVar.set(visible ? 1.0d : 0.0d);
        }
    }

    public void pause() {
        CommandTriggers commandTriggers = this.mTriggers;
        if (commandTriggers != null) {
            commandTriggers.pause();
        }
        int N = this.mAnimations;
        if (N != 0) {
            N = N.size();
            for (int i = 0; i < N; i++) {
                ((BaseAnimation) this.mAnimations.get(i)).pause();
            }
        }
    }

    public void resume() {
        CommandTriggers commandTriggers = this.mTriggers;
        if (commandTriggers != null) {
            commandTriggers.resume();
        }
        int N = this.mAnimations;
        if (N != 0) {
            N = N.size();
            for (int i = 0; i < N; i++) {
                ((BaseAnimation) this.mAnimations.get(i)).resume();
            }
        }
    }

    public void finish() {
        CommandTriggers commandTriggers = this.mTriggers;
        if (commandTriggers != null) {
            commandTriggers.finish();
        }
        int N = this.mAnimations;
        if (N != 0) {
            N = N.size();
            for (int i = 0; i < N; i++) {
                ((BaseAnimation) this.mAnimations.get(i)).finish();
            }
        }
    }

    public final void playAnim() {
        playAnim(0, -1, true, true);
    }

    public final void playAnim(long startTime, long endTime, boolean isLoop, boolean isDelay) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        playAnim(elapsedRealtime, startTime, endTime, isLoop, isDelay);
        doTick(elapsedRealtime);
    }

    /* Access modifiers changed, original: protected */
    public void playAnim(long time, long startTime, long endTime, boolean isLoop, boolean isDelay) {
        int N = this.mAnimations;
        if (N != 0) {
            N = N.size();
            for (int i = 0; i < N; i++) {
                ((BaseAnimation) this.mAnimations.get(i)).playAnim(time, startTime, endTime, isLoop, isDelay);
            }
        }
    }

    public final void setAnimations(String tags) {
        String[] split = (TextUtils.isEmpty(tags) || ".".equals(tags)) ? null : tags.split(",");
        setAnim(split);
    }

    public void setAnim(String[] tags) {
        if (this.mAnimations != null) {
            onSetAnimBefore();
            int N = this.mAnimations.size();
            for (int i = 0; i < N; i++) {
                BaseAnimation ani = (BaseAnimation) this.mAnimations.get(i);
                boolean enable = isTagEnable(tags, ani.getTag());
                ani.setDisable(enable ^ 1);
                if (enable) {
                    onSetAnimEnable(ani);
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onSetAnimBefore() {
    }

    /* Access modifiers changed, original: protected */
    public void onSetAnimEnable(BaseAnimation ani) {
    }

    protected static boolean isTagEnable(String[] tags, String tag) {
        if (tags == null) {
            if (TextUtils.isEmpty(tag)) {
                return true;
            }
        } else if (Utils.arrayContains(tags, tag)) {
            return true;
        }
        return false;
    }

    public final void pauseAnim() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        pauseAnim(elapsedRealtime);
        doTick(elapsedRealtime);
    }

    /* Access modifiers changed, original: protected */
    public void pauseAnim(long time) {
        int N = this.mAnimations;
        if (N != 0) {
            N = N.size();
            for (int i = 0; i < N; i++) {
                ((BaseAnimation) this.mAnimations.get(i)).pauseAnim(time);
            }
        }
    }

    public final void resumeAnim() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        resumeAnim(elapsedRealtime);
        doTick(elapsedRealtime);
    }

    /* Access modifiers changed, original: protected */
    public void resumeAnim(long time) {
        int N = this.mAnimations;
        if (N != 0) {
            N = N.size();
            for (int i = 0; i < N; i++) {
                ((BaseAnimation) this.mAnimations.get(i)).resumeAnim(time);
            }
        }
    }

    public final void reset() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        reset(elapsedRealtime);
        doTick(elapsedRealtime);
    }

    public void reset(long time) {
        int N = this.mAnimations;
        if (N != 0) {
            N = N.size();
            for (int i = 0; i < N; i++) {
                ((BaseAnimation) this.mAnimations.get(i)).reset(time);
            }
        }
    }

    public void tick(long currentTime) {
        updateVisibility();
        if (isVisible()) {
            doTick(currentTime);
        }
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        int N = this.mAnimations;
        if (N != 0) {
            N = N.size();
            for (int i = 0; i < N; i++) {
                ((BaseAnimation) this.mAnimations.get(i)).tick(currentTime);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean isVisibleInner() {
        if (this.mShow) {
            Expression expression = this.mVisibilityExpression;
            if (expression == null || expression.evaluate() > 0.0d) {
                ElementGroup elementGroup = this.mParent;
                if (elementGroup == null || elementGroup.isVisible()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void render(Canvas c) {
        updateVisibility();
        if (isVisible()) {
            doRender(c);
        }
    }

    public boolean onTouch(MotionEvent event) {
        return false;
    }

    public boolean onHover(MotionEvent event) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void setActualWidth(double value) {
        if (this.mHasName) {
            if (this.mActualWidthVar == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(".");
                stringBuilder.append(ACTUAL_W);
                this.mActualWidthVar = new IndexedVariable(stringBuilder.toString(), getVariables(), true);
            }
            this.mActualWidthVar.set(value);
        }
    }

    /* Access modifiers changed, original: protected */
    public void setActualHeight(double value) {
        if (this.mHasName) {
            if (this.mActualHeightVar == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(".");
                stringBuilder.append(ACTUAL_H);
                this.mActualHeightVar = new IndexedVariable(stringBuilder.toString(), getVariables(), true);
            }
            this.mActualHeightVar.set(value);
        }
    }

    /* Access modifiers changed, original: protected */
    public float getTop(float pos, float height) {
        if (height <= 0.0f) {
            return pos;
        }
        float y = pos;
        int i = AnonymousClass2.$SwitchMap$miui$maml$elements$ScreenElement$AlignV[this.mAlignV.ordinal()];
        if (i == 1) {
            y -= height / 2.0f;
        } else if (i == 2) {
            y -= height;
        }
        return y;
    }

    /* Access modifiers changed, original: protected */
    public float getLeft(float pos, float width) {
        if (width <= 0.0f) {
            return pos;
        }
        float x = pos;
        int i = AnonymousClass2.$SwitchMap$miui$maml$elements$ScreenElement$Align[this.mAlign.ordinal()];
        if (i == 1) {
            x -= width / 2.0f;
        } else if (i == 2) {
            x -= width;
        }
        return x;
    }

    public void requestUpdate() {
        RendererController c = getRendererController();
        if (c != null) {
            c.requestUpdate();
        }
    }

    public FramerateToken createToken(String name) {
        RendererController c = getRendererController();
        if (c == null) {
            return null;
        }
        return c.createToken(name);
    }

    public ScreenContext getContext() {
        return this.mRoot.getContext();
    }

    public final Variables getVariables() {
        return getContext().mVariables;
    }

    public ScreenElementRoot getRoot() {
        return this.mRoot;
    }

    /* Access modifiers changed, original: protected|final */
    public final float scale(double v) {
        return (float) (((double) this.mRoot.getScale()) * v);
    }

    /* Access modifiers changed, original: protected|final */
    public final double descale(double v) {
        return v / ((double) this.mRoot.getScale());
    }

    /* Access modifiers changed, original: protected|final */
    public final float getFramerate() {
        FramerateToken framerateToken = this.mFramerateToken;
        return framerateToken == null ? 0.0f : framerateToken.getFramerate();
    }

    public RendererController getRendererController() {
        ElementGroup elementGroup = this.mParent;
        return elementGroup != null ? elementGroup.getRendererController() : null;
    }

    /* Access modifiers changed, original: protected|final */
    public final void requestFramerate(float f) {
        if (f >= 0.0f) {
            if (this.mFramerateToken == null) {
                if (f != 0.0f) {
                    this.mFramerateToken = createToken(toString());
                } else {
                    return;
                }
            }
            FramerateToken framerateToken = this.mFramerateToken;
            if (framerateToken != null) {
                framerateToken.requestFramerate(f);
            }
        }
    }

    public void acceptVisitor(ScreenElementVisitor v) {
        v.visit(this);
    }

    public void postRunnable(Runnable r) {
        RendererController c = this.mRoot.getRendererController();
        if (c != null) {
            c.postRunnable(r);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final String evaluateStr(Expression exp) {
        return exp == null ? null : exp.evaluateStr();
    }

    /* Access modifiers changed, original: protected|final */
    public final double evaluate(Expression exp) {
        return exp == null ? 0.0d : exp.evaluate();
    }

    /* Access modifiers changed, original: protected|final */
    public final NotifierManager getNotifierManager() {
        return NotifierManager.getInstance(getContext().mContext);
    }

    /* Access modifiers changed, original: protected|final */
    public final void postInMainThread(Runnable r) {
        getContext().getHandler().post(r);
    }

    public void performAction(String action) {
        CommandTriggers commandTriggers = this.mTriggers;
        if (commandTriggers != null && action != null) {
            commandTriggers.onAction(action);
            requestUpdate();
        }
    }
}
