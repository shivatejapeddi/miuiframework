package miui.maml.elements;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewConfiguration;
import miui.maml.ScreenElementRoot;
import org.w3c.dom.Element;

public class ButtonScreenElement extends ElementGroup {
    private static final String LOG_TAG = "ButtonScreenElement";
    public static final String TAG_NAME = "Button";
    private boolean mIsAlignChildren;
    private ButtonActionListener mListener;
    private String mListenerName;
    private ElementGroup mNormalElements;
    private ElementGroup mPressedElements;
    private float mPreviousTapPositionX;
    private float mPreviousTapPositionY;
    private long mPreviousTapUpTime;

    public interface ButtonActionListener {
        boolean onButtonDoubleClick(String str);

        boolean onButtonDown(String str);

        boolean onButtonLongClick(String str);

        boolean onButtonUp(String str);
    }

    public ButtonScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        load(node);
    }

    private void load(Element node) {
        if (node != null) {
            this.mIsAlignChildren = Boolean.parseBoolean(getAttr(node, "alignChildren"));
            this.mListenerName = getAttr(node, "listener");
            this.mTouchable = true;
        }
    }

    public void setListener(ButtonActionListener listener) {
        this.mListener = listener;
    }

    /* Access modifiers changed, original: protected */
    public ScreenElement onCreateChild(Element ele) {
        String tag = ele.getTagName();
        ElementGroup elementGroup;
        if (tag.equalsIgnoreCase("Normal")) {
            elementGroup = new ElementGroup(ele, this.mRoot);
            this.mNormalElements = elementGroup;
            return elementGroup;
        } else if (!tag.equalsIgnoreCase("Pressed")) {
            return super.onCreateChild(ele);
        } else {
            elementGroup = new ElementGroup(ele, this.mRoot);
            this.mPressedElements = elementGroup;
            return elementGroup;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onActionDown(float x, float y) {
        super.onActionDown(x, y);
        ButtonActionListener buttonActionListener = this.mListener;
        if (buttonActionListener != null) {
            buttonActionListener.onButtonDown(this.mName);
        }
        if (SystemClock.uptimeMillis() - this.mPreviousTapUpTime <= ((long) ViewConfiguration.getDoubleTapTimeout())) {
            float deltaX = x - this.mPreviousTapPositionX;
            float deltaY = y - this.mPreviousTapPositionY;
            float distanceSquared = (deltaX * deltaX) + (deltaY * deltaY);
            int doubleTapSlop = ViewConfiguration.get(getContext().mContext).getScaledDoubleTapSlop();
            if (distanceSquared < ((float) (doubleTapSlop * doubleTapSlop))) {
                ButtonActionListener buttonActionListener2 = this.mListener;
                if (buttonActionListener2 != null) {
                    buttonActionListener2.onButtonDoubleClick(this.mName);
                }
                performAction("double");
            }
        }
        this.mPreviousTapPositionX = x;
        this.mPreviousTapPositionY = y;
        showPressedElements();
        ElementGroup elementGroup = this.mPressedElements;
        if (elementGroup != null) {
            elementGroup.reset();
        }
    }

    public void onActionUp() {
        super.onActionUp();
        ButtonActionListener buttonActionListener = this.mListener;
        if (buttonActionListener != null) {
            buttonActionListener.onButtonUp(this.mName);
        }
        this.mPreviousTapUpTime = SystemClock.uptimeMillis();
        resetState();
    }

    /* Access modifiers changed, original: protected */
    public void onActionCancel() {
        super.onActionCancel();
        resetState();
    }

    /* Access modifiers changed, original: protected */
    public void resetState() {
        showNormalElements();
        ElementGroup elementGroup = this.mNormalElements;
        if (elementGroup != null) {
            elementGroup.reset();
        }
    }

    private void showNormalElements() {
        ElementGroup elementGroup = this.mNormalElements;
        if (elementGroup != null) {
            elementGroup.show(true);
        }
        elementGroup = this.mPressedElements;
        if (elementGroup != null) {
            elementGroup.show(false);
        }
    }

    private void showPressedElements() {
        ElementGroup elementGroup = this.mPressedElements;
        if (elementGroup != null) {
            elementGroup.show(true);
            elementGroup = this.mNormalElements;
            if (elementGroup != null) {
                elementGroup.show(false);
                return;
            }
            return;
        }
        elementGroup = this.mNormalElements;
        if (elementGroup != null) {
            elementGroup.show(true);
        }
    }

    public void init() {
        super.init();
        if (this.mListener == null && !TextUtils.isEmpty(this.mListenerName)) {
            try {
                this.mListener = (ButtonActionListener) this.mRoot.findElement(this.mListenerName);
            } catch (ClassCastException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("button listener designated by the name is not actually a listener: ");
                stringBuilder.append(this.mListenerName);
                Log.e(LOG_TAG, stringBuilder.toString());
            }
        }
        showNormalElements();
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

    /* Access modifiers changed, original: protected */
    public void doRender(Canvas c) {
        c.save();
        if (!this.mIsAlignChildren) {
            c.translate(-getLeft(), -getTop());
        }
        super.doRender(c);
        c.restore();
    }
}
