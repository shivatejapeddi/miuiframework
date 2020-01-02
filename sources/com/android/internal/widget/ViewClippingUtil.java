package com.android.internal.widget;

import android.util.ArraySet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class ViewClippingUtil {
    private static final int CLIP_CHILDREN_TAG = 16908821;
    private static final int CLIP_CLIPPING_SET = 16908820;
    private static final int CLIP_TO_PADDING = 16908823;

    public interface ClippingParameters {
        boolean shouldFinish(View view);

        boolean isClippingEnablingAllowed(View view) {
            return MessagingPropertyAnimator.isAnimatingTranslation(view) ^ 1;
        }

        void onClippingStateChanged(View view, boolean isClipping) {
        }
    }

    public static void setClippingDeactivated(View transformedView, boolean deactivated, ClippingParameters clippingParameters) {
        if ((deactivated || clippingParameters.isClippingEnablingAllowed(transformedView)) && (transformedView.getParent() instanceof ViewGroup)) {
            ViewGroup parent = (ViewGroup) transformedView.getParent();
            while (true) {
                if (deactivated || clippingParameters.isClippingEnablingAllowed(transformedView)) {
                    ArraySet<View> clipSet = (ArraySet) parent.getTag(16908820);
                    if (clipSet == null) {
                        clipSet = new ArraySet();
                        parent.setTagInternal(16908820, clipSet);
                    }
                    Boolean clipChildren = (Boolean) parent.getTag(16908821);
                    if (clipChildren == null) {
                        clipChildren = Boolean.valueOf(parent.getClipChildren());
                        parent.setTagInternal(16908821, clipChildren);
                    }
                    Boolean clipToPadding = (Boolean) parent.getTag(16908823);
                    if (clipToPadding == null) {
                        clipToPadding = Boolean.valueOf(parent.getClipToPadding());
                        parent.setTagInternal(16908823, clipToPadding);
                    }
                    if (deactivated) {
                        clipSet.add(transformedView);
                        parent.setClipChildren(false);
                        parent.setClipToPadding(false);
                        clippingParameters.onClippingStateChanged(parent, false);
                    } else {
                        clipSet.remove(transformedView);
                        if (clipSet.isEmpty()) {
                            parent.setClipChildren(clipChildren.booleanValue());
                            parent.setClipToPadding(clipToPadding.booleanValue());
                            parent.setTagInternal(16908820, null);
                            clippingParameters.onClippingStateChanged(parent, true);
                        }
                    }
                    if (!clippingParameters.shouldFinish(parent)) {
                        ViewParent viewParent = parent.getParent();
                        if (viewParent instanceof ViewGroup) {
                            parent = (ViewGroup) viewParent;
                        } else {
                            return;
                        }
                    }
                    return;
                }
                return;
            }
        }
    }
}
