package miui.maml.util;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.IntArray;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.widget.ExploreByTouchHelper;
import java.util.List;
import miui.maml.ScreenElementRoot;
import miui.maml.elements.AnimatedScreenElement;
import miui.maml.elements.ButtonScreenElement;

public class MamlAccessHelper extends ExploreByTouchHelper {
    private static final String TAG = "MamlAccessHelper";
    ScreenElementRoot mRoot = null;

    public MamlAccessHelper(ScreenElementRoot root, View parentView) {
        super(parentView);
        this.mRoot = root;
    }

    /* Access modifiers changed, original: protected */
    public int getVirtualViewAt(float x, float y) {
        List<AnimatedScreenElement> es = this.mRoot.getAccessibleElements();
        for (int i = es.size() - 1; i >= 0; i--) {
            AnimatedScreenElement e = (AnimatedScreenElement) es.get(i);
            if (e.isVisible() && e.touched(x, y)) {
                return i;
            }
        }
        return Integer.MIN_VALUE;
    }

    /* Access modifiers changed, original: protected */
    public void getVisibleVirtualViews(IntArray virtualViewIds) {
        List<AnimatedScreenElement> es = this.mRoot.getAccessibleElements();
        for (int i = 0; i < es.size(); i++) {
            if (((AnimatedScreenElement) es.get(i)).isVisible()) {
                virtualViewIds.add(i);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onPopulateEventForVirtualView(int virtualViewId, AccessibilityEvent event) {
        List<AnimatedScreenElement> es = this.mRoot.getAccessibleElements();
        if (virtualViewId >= 0 && virtualViewId < es.size()) {
            event.setContentDescription(((AnimatedScreenElement) es.get(virtualViewId)).getContentDescription());
        }
    }

    /* Access modifiers changed, original: protected */
    public void onPopulateNodeForVirtualView(int virtualViewId, AccessibilityNodeInfo node) {
        List<AnimatedScreenElement> es = this.mRoot.getAccessibleElements();
        String str = TAG;
        if (virtualViewId < 0 || virtualViewId >= es.size()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("virtualViewId not found ");
            stringBuilder.append(virtualViewId);
            Log.e(str, stringBuilder.toString());
            node.setContentDescription("");
            node.setBoundsInParent(new Rect(0, 0, 0, 0));
            return;
        }
        AnimatedScreenElement e = (AnimatedScreenElement) es.get(virtualViewId);
        String content = e.getContentDescription();
        if (content == null) {
            content = "";
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("element.getContentDescription() == null ");
            stringBuilder2.append(e.getName());
            Log.e(str, stringBuilder2.toString());
        }
        node.setContentDescription(content);
        node.addAction(16);
        node.setBoundsInParent(new Rect((int) e.getAbsoluteLeft(), (int) e.getAbsoluteTop(), (int) (e.getAbsoluteLeft() + e.getWidth()), (int) (e.getAbsoluteTop() + e.getHeight())));
    }

    /* Access modifiers changed, original: protected */
    public boolean onPerformActionForVirtualView(int virtualViewId, int action, Bundle arguments) {
        if (action == 16) {
            List<AnimatedScreenElement> es = this.mRoot.getAccessibleElements();
            if (virtualViewId >= 0 && virtualViewId < es.size()) {
                AnimatedScreenElement e = (AnimatedScreenElement) es.get(virtualViewId);
                e.performAction("up");
                if (e instanceof ButtonScreenElement) {
                    ((ButtonScreenElement) e).onActionUp();
                }
                return true;
            }
        }
        return false;
    }
}
