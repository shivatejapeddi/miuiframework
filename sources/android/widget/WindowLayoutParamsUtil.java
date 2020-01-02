package android.widget;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;

class WindowLayoutParamsUtil {
    private WindowLayoutParamsUtil() {
    }

    static boolean isInSystemWindow(View view) {
        LayoutParams wlp = getWindowLayoutParams(view);
        return wlp != null && wlp.type >= 2000;
    }

    static LayoutParams getWindowLayoutParams(View view) {
        for (View curView = view; curView != null; curView = (View) curView.getParent()) {
            ViewGroup.LayoutParams lp = curView.getLayoutParams();
            if (lp instanceof LayoutParams) {
                return (LayoutParams) lp;
            }
        }
        return null;
    }
}
