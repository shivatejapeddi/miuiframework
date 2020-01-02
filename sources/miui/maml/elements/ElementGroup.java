package miui.maml.elements;

import android.graphics.Canvas;
import android.miui.BiometricConnect;
import android.provider.Telephony.BaseMmsColumns;
import android.util.Log;
import android.view.MotionEvent;
import java.util.ArrayList;
import miui.maml.ScreenElementRoot;
import miui.maml.data.IndexedVariable;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ElementGroup extends AnimatedScreenElement {
    private static final String LOG_TAG = "MAML_ElementGroup";
    public static final String TAG_NAME = "ElementGroup";
    public static final String TAG_NAME1 = "Group";
    protected boolean mClip;
    protected ArrayList<ScreenElement> mElements = new ArrayList();
    private boolean mHovered;
    private IndexedVariable mIndexVar;
    private boolean mLayered;
    private LinearDirection mLinearDirection = LinearDirection.None;
    private boolean mTouched;

    /* renamed from: miui.maml.elements.ElementGroup$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$miui$maml$elements$ElementGroup$LinearDirection = new int[LinearDirection.values().length];

        static {
            try {
                $SwitchMap$miui$maml$elements$ElementGroup$LinearDirection[LinearDirection.Horizontal.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$miui$maml$elements$ElementGroup$LinearDirection[LinearDirection.Vertical.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private enum LinearDirection {
        None,
        Horizontal,
        Vertical
    }

    public ElementGroup(Element node, ScreenElementRoot root) {
        super(node, root);
        load(node);
    }

    private ElementGroup(ScreenElementRoot root, IndexedVariable indexVar) {
        super(null, root);
        this.mIndexVar = indexVar;
    }

    public static ElementGroup createArrayGroup(ScreenElementRoot root, IndexedVariable indexVar) {
        return new ElementGroup(root, indexVar);
    }

    public static boolean isArrayGroup(ScreenElement se) {
        return (se instanceof ElementGroup) && ((ElementGroup) se).isArray();
    }

    public boolean isArray() {
        return this.mIndexVar != null;
    }

    private void load(Element node) {
        if (node != null) {
            this.mClip = Boolean.parseBoolean(getAttr(node, "clip"));
            this.mLayered = Boolean.parseBoolean(getAttr(node, "layered"));
            String linear = getAttr(node, "linear");
            if (BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_H.equalsIgnoreCase(linear)) {
                this.mLinearDirection = LinearDirection.Horizontal;
            } else if (BaseMmsColumns.MMS_VERSION.equalsIgnoreCase(linear)) {
                this.mLinearDirection = LinearDirection.Vertical;
            }
            NodeList children = node.getChildNodes();
            int N = children.getLength();
            for (int i = 0; i < N; i++) {
                if (children.item(i).getNodeType() == (short) 1) {
                    addElement(onCreateChild((Element) children.item(i)));
                }
            }
        }
    }

    public void addElement(ScreenElement newElement) {
        if (newElement != null) {
            newElement.setParent(this);
            this.mElements.add(newElement);
        }
    }

    public void removeElement(ScreenElement element) {
        this.mElements.remove(element);
        requestUpdate();
    }

    public void removeAllElements() {
        this.mElements.clear();
        requestUpdate();
    }

    /* Access modifiers changed, original: protected */
    public ScreenElement onCreateChild(Element ele) {
        return getContext().mFactory.createInstance(ele, this.mRoot);
    }

    public void init() {
        super.init();
        int N = this.mElements.size();
        for (int i = 0; i < N; i++) {
            IndexedVariable indexedVariable = this.mIndexVar;
            if (indexedVariable != null) {
                indexedVariable.set((double) i);
            }
            ((ScreenElement) this.mElements.get(i)).init();
        }
    }

    public void setAnim(String[] tags) {
        super.setAnim(tags);
        int N = this.mElements.size();
        for (int i = 0; i < N; i++) {
            IndexedVariable indexedVariable = this.mIndexVar;
            if (indexedVariable != null) {
                indexedVariable.set((double) i);
            }
            ((ScreenElement) this.mElements.get(i)).setAnim(tags);
        }
    }

    public void playAnim(long time, long startTime, long endTime, boolean isLoop, boolean isDelay) {
        super.playAnim(time, startTime, endTime, isLoop, isDelay);
        int N = this.mElements.size();
        for (int i = 0; i < N; i++) {
            IndexedVariable indexedVariable = this.mIndexVar;
            if (indexedVariable != null) {
                indexedVariable.set((double) i);
            }
            ((ScreenElement) this.mElements.get(i)).playAnim(time, startTime, endTime, isLoop, isDelay);
        }
    }

    public void pauseAnim(long time) {
        super.pauseAnim(time);
        int N = this.mElements.size();
        for (int i = 0; i < N; i++) {
            ((ScreenElement) this.mElements.get(i)).pauseAnim(time);
        }
    }

    public void resumeAnim(long time) {
        super.resumeAnim(time);
        int N = this.mElements.size();
        for (int i = 0; i < N; i++) {
            ((ScreenElement) this.mElements.get(i)).resumeAnim(time);
        }
    }

    public void reset(long time) {
        super.reset(time);
        int N = this.mElements.size();
        for (int i = 0; i < N; i++) {
            ((ScreenElement) this.mElements.get(i)).reset(time);
        }
    }

    public void pause() {
        super.pause();
        int N = this.mElements.size();
        for (int i = 0; i < N; i++) {
            ((ScreenElement) this.mElements.get(i)).pause();
        }
    }

    public void resume() {
        super.resume();
        int N = this.mElements.size();
        for (int i = 0; i < N; i++) {
            ((ScreenElement) this.mElements.get(i)).resume();
        }
    }

    public ScreenElement getChild(int index) {
        if (index < 0 || index >= this.mElements.size()) {
            return null;
        }
        return (ScreenElement) this.mElements.get(index);
    }

    public int getSize() {
        return this.mElements.size();
    }

    public boolean isLayered() {
        return this.mLayered;
    }

    /* Access modifiers changed, original: protected */
    public void doRender(Canvas c) {
        int rs;
        float w = getWidth();
        float h = getHeight();
        float x = getLeft(0.0f, w);
        float y = getTop(0.0f, h);
        if (!this.mLayered || w <= 0.0f || h <= 0.0f) {
            rs = c.save();
        } else {
            rs = c.saveLayerAlpha(x, y, x + w, y + h, getAlpha(), 31);
        }
        c.translate(x, y);
        if (w > 0.0f && h > 0.0f && this.mClip) {
            c.clipRect(0.0f, 0.0f, w, h);
        }
        doRenderChildren(c);
        c.restoreToCount(rs);
    }

    /* Access modifiers changed, original: protected */
    public void doRenderChildren(Canvas c) {
        int N = this.mElements.size();
        for (int i = 0; i < N; i++) {
            ScreenElement element = (ScreenElement) this.mElements.get(i);
            IndexedVariable indexedVariable = this.mIndexVar;
            if (indexedVariable != null) {
                indexedVariable.set((double) i);
            }
            element.render(c);
        }
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        super.doTick(currentTime);
        doTickChildren(currentTime);
    }

    /* Access modifiers changed, original: protected */
    public void doTickChildren(long currentTime) {
        int i;
        int N = this.mElements.size();
        float currentPos = 0.0f;
        float maxSize = 0.0f;
        for (i = 0; i < N; i++) {
            ScreenElement element = (ScreenElement) this.mElements.get(i);
            IndexedVariable indexedVariable = this.mIndexVar;
            if (indexedVariable != null) {
                indexedVariable.set((double) i);
            }
            element.tick(currentTime);
            if (this.mLinearDirection != LinearDirection.None && (element instanceof AnimatedScreenElement) && element.isVisible()) {
                AnimatedScreenElement ae = (AnimatedScreenElement) element;
                int i2 = AnonymousClass1.$SwitchMap$miui$maml$elements$ElementGroup$LinearDirection[this.mLinearDirection.ordinal()];
                float h;
                if (i2 == 1) {
                    currentPos += ae.getMarginLeft();
                    ae.setX((double) currentPos);
                    currentPos += ae.getWidth() + ae.getMarginRight();
                    h = ae.getHeight();
                    if (maxSize < h) {
                        maxSize = h;
                    }
                } else if (i2 == 2) {
                    currentPos += ae.getMarginTop();
                    ae.setY((double) currentPos);
                    currentPos += ae.getHeight() + ae.getMarginBottom();
                    h = ae.getWidth();
                    if (maxSize < h) {
                        maxSize = h;
                    }
                }
            }
        }
        i = AnonymousClass1.$SwitchMap$miui$maml$elements$ElementGroup$LinearDirection[this.mLinearDirection.ordinal()];
        if (i == 1) {
            setW((double) currentPos);
            setH((double) maxSize);
            setActualWidth(descale((double) currentPos));
            setActualHeight(descale((double) maxSize));
        } else if (i == 2) {
            setH((double) currentPos);
            setW((double) maxSize);
            setActualHeight(descale((double) currentPos));
            setActualWidth(descale((double) maxSize));
        }
    }

    public boolean onTouch(MotionEvent event) {
        boolean reverse = false;
        if (!isVisible()) {
            return false;
        }
        int action = event.getAction();
        boolean touched = touched(event.getX(), event.getY());
        if (this.mClip && !touched) {
            if (!this.mTouched) {
                return false;
            }
            event.setAction(3);
        }
        boolean ret = false;
        int N = this.mElements.size();
        boolean z = true;
        if (this.mRoot.version() >= 2) {
            reverse = true;
        }
        int i;
        ScreenElement element;
        IndexedVariable indexedVariable;
        if (reverse) {
            for (i = N - 1; i >= 0; i--) {
                element = (ScreenElement) this.mElements.get(i);
                indexedVariable = this.mIndexVar;
                if (indexedVariable != null) {
                    indexedVariable.set((double) i);
                }
                if (element.onTouch(event)) {
                    ret = true;
                    break;
                }
            }
        } else {
            for (i = 0; i < N; i++) {
                element = (ScreenElement) this.mElements.get(i);
                indexedVariable = this.mIndexVar;
                if (indexedVariable != null) {
                    indexedVariable.set((double) i);
                }
                if (element.onTouch(event)) {
                    ret = true;
                    break;
                }
            }
        }
        event.setAction(action);
        if (!ret) {
            z = super.onTouch(event);
        }
        this.mTouched = z;
        return this.mTouched;
    }

    public boolean onHover(MotionEvent event) {
        if (!isVisible()) {
            return false;
        }
        boolean touched = touched(event.getX(), event.getY());
        if (this.mClip && !touched) {
            if (!this.mHovered) {
                return false;
            }
            event.setAction(10);
        }
        boolean ret = false;
        for (int i = this.mElements.size() - 1; i >= 0; i--) {
            ScreenElement element = (ScreenElement) this.mElements.get(i);
            IndexedVariable indexedVariable = this.mIndexVar;
            if (indexedVariable != null) {
                indexedVariable.set((double) i);
            }
            if (element.onHover(event)) {
                ret = true;
                break;
            }
        }
        this.mHovered = ret ? true : super.onHover(event);
        return this.mHovered;
    }

    /* Access modifiers changed, original: protected */
    public float getParentLeft() {
        return getLeft() + (this.mParent == null ? 0.0f : this.mParent.getParentLeft());
    }

    /* Access modifiers changed, original: protected */
    public float getParentTop() {
        return getTop() + (this.mParent == null ? 0.0f : this.mParent.getParentTop());
    }

    public void finish() {
        super.finish();
        int N = this.mElements.size();
        for (int i = 0; i < N; i++) {
            try {
                ((ScreenElement) this.mElements.get(i)).finish();
            } catch (Exception e) {
                Log.e(LOG_TAG, e.toString());
                e.printStackTrace();
            }
        }
    }

    public void showCategory(String category, boolean show) {
        super.showCategory(category, show);
        int N = this.mElements.size();
        for (int i = 0; i < N; i++) {
            ((ScreenElement) this.mElements.get(i)).showCategory(category, show);
        }
    }

    public ScreenElement findElement(String name) {
        ScreenElement ele = super.findElement(name);
        if (ele != null) {
            return ele;
        }
        int N = this.mElements.size();
        for (int i = 0; i < N; i++) {
            ele = ((ScreenElement) this.mElements.get(i)).findElement(name);
            if (ele != null) {
                return ele;
            }
        }
        return null;
    }

    public ArrayList<ScreenElement> getElements() {
        return this.mElements;
    }

    public void acceptVisitor(ScreenElementVisitor v) {
        super.acceptVisitor(v);
        int N = this.mElements.size();
        for (int i = 0; i < N; i++) {
            ((ScreenElement) this.mElements.get(i)).acceptVisitor(v);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onVisibilityChange(boolean visible) {
        super.onVisibilityChange(visible);
        int N = this.mElements.size();
        for (int i = 0; i < N; i++) {
            ((ScreenElement) this.mElements.get(i)).updateVisibility();
        }
    }

    public void setClip(boolean clip) {
        this.mClip = clip;
    }
}
