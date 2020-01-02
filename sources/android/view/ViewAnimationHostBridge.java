package android.view;

import android.graphics.RenderNode;
import android.graphics.RenderNode.AnimationHost;

public class ViewAnimationHostBridge implements AnimationHost {
    private final View mView;

    public ViewAnimationHostBridge(View view) {
        this.mView = view;
    }

    public void registerAnimatingRenderNode(RenderNode animator) {
        this.mView.mAttachInfo.mViewRootImpl.registerAnimatingRenderNode(animator);
    }

    public void registerVectorDrawableAnimator(NativeVectorDrawableAnimator animator) {
        this.mView.mAttachInfo.mViewRootImpl.registerVectorDrawableAnimator(animator);
    }

    public boolean isAttached() {
        return this.mView.mAttachInfo != null;
    }
}
