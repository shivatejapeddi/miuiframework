package com.android.internal.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.IntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeProvider;

public abstract class ExploreByTouchHelper extends AccessibilityDelegate {
    private static final String DEFAULT_CLASS_NAME = View.class.getName();
    public static final int HOST_ID = -1;
    public static final int INVALID_ID = Integer.MIN_VALUE;
    private static final Rect INVALID_PARENT_BOUNDS = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    private final Context mContext;
    private int mFocusedVirtualViewId = Integer.MIN_VALUE;
    private int mHoveredVirtualViewId = Integer.MIN_VALUE;
    private final AccessibilityManager mManager;
    private ExploreByTouchNodeProvider mNodeProvider;
    private IntArray mTempArray;
    private int[] mTempGlobalRect;
    private Rect mTempParentRect;
    private Rect mTempScreenRect;
    private Rect mTempVisibleRect;
    private final View mView;

    private class ExploreByTouchNodeProvider extends AccessibilityNodeProvider {
        private ExploreByTouchNodeProvider() {
        }

        public AccessibilityNodeInfo createAccessibilityNodeInfo(int virtualViewId) {
            return ExploreByTouchHelper.this.createNode(virtualViewId);
        }

        public boolean performAction(int virtualViewId, int action, Bundle arguments) {
            return ExploreByTouchHelper.this.performAction(virtualViewId, action, arguments);
        }
    }

    public abstract int getVirtualViewAt(float f, float f2);

    public abstract void getVisibleVirtualViews(IntArray intArray);

    public abstract boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle);

    public abstract void onPopulateEventForVirtualView(int i, AccessibilityEvent accessibilityEvent);

    public abstract void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfo accessibilityNodeInfo);

    public ExploreByTouchHelper(View forView) {
        if (forView != null) {
            this.mView = forView;
            this.mContext = forView.getContext();
            this.mManager = (AccessibilityManager) this.mContext.getSystemService(Context.ACCESSIBILITY_SERVICE);
            return;
        }
        throw new IllegalArgumentException("View may not be null");
    }

    public AccessibilityNodeProvider getAccessibilityNodeProvider(View host) {
        if (this.mNodeProvider == null) {
            this.mNodeProvider = new ExploreByTouchNodeProvider();
        }
        return this.mNodeProvider;
    }

    public boolean dispatchHoverEvent(MotionEvent event) {
        boolean z = false;
        if (!this.mManager.isEnabled() || !this.mManager.isTouchExplorationEnabled()) {
            return false;
        }
        int action = event.getAction();
        if (action == 7 || action == 9) {
            action = getVirtualViewAt(event.getX(), event.getY());
            updateHoveredVirtualView(action);
            if (action != Integer.MIN_VALUE) {
                z = true;
            }
            return z;
        } else if (action != 10 || this.mHoveredVirtualViewId == Integer.MIN_VALUE) {
            return false;
        } else {
            updateHoveredVirtualView(Integer.MIN_VALUE);
            return true;
        }
    }

    public boolean sendEventForVirtualView(int virtualViewId, int eventType) {
        if (virtualViewId == Integer.MIN_VALUE || !this.mManager.isEnabled()) {
            return false;
        }
        ViewParent parent = this.mView.getParent();
        if (parent == null) {
            return false;
        }
        return parent.requestSendAccessibilityEvent(this.mView, createEvent(virtualViewId, eventType));
    }

    public void invalidateRoot() {
        invalidateVirtualView(-1, 1);
    }

    public void invalidateVirtualView(int virtualViewId) {
        invalidateVirtualView(virtualViewId, 0);
    }

    public void invalidateVirtualView(int virtualViewId, int changeTypes) {
        if (virtualViewId != Integer.MIN_VALUE && this.mManager.isEnabled()) {
            ViewParent parent = this.mView.getParent();
            if (parent != null) {
                AccessibilityEvent event = createEvent(virtualViewId, 2048);
                event.setContentChangeTypes(changeTypes);
                parent.requestSendAccessibilityEvent(this.mView, event);
            }
        }
    }

    public int getFocusedVirtualView() {
        return this.mFocusedVirtualViewId;
    }

    private void updateHoveredVirtualView(int virtualViewId) {
        if (this.mHoveredVirtualViewId != virtualViewId) {
            int previousVirtualViewId = this.mHoveredVirtualViewId;
            this.mHoveredVirtualViewId = virtualViewId;
            sendEventForVirtualView(virtualViewId, 128);
            sendEventForVirtualView(previousVirtualViewId, 256);
        }
    }

    private AccessibilityEvent createEvent(int virtualViewId, int eventType) {
        if (virtualViewId != -1) {
            return createEventForChild(virtualViewId, eventType);
        }
        return createEventForHost(eventType);
    }

    private AccessibilityEvent createEventForHost(int eventType) {
        AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
        this.mView.onInitializeAccessibilityEvent(event);
        onPopulateEventForHost(event);
        return event;
    }

    private AccessibilityEvent createEventForChild(int virtualViewId, int eventType) {
        AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
        event.setEnabled(true);
        event.setClassName(DEFAULT_CLASS_NAME);
        onPopulateEventForVirtualView(virtualViewId, event);
        if (event.getText().isEmpty() && event.getContentDescription() == null) {
            throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
        }
        event.setPackageName(this.mView.getContext().getPackageName());
        event.setSource(this.mView, virtualViewId);
        return event;
    }

    private AccessibilityNodeInfo createNode(int virtualViewId) {
        if (virtualViewId != -1) {
            return createNodeForChild(virtualViewId);
        }
        return createNodeForHost();
    }

    private AccessibilityNodeInfo createNodeForHost() {
        AccessibilityNodeInfo node = AccessibilityNodeInfo.obtain(this.mView);
        this.mView.onInitializeAccessibilityNodeInfo(node);
        int realNodeCount = node.getChildCount();
        onPopulateNodeForHost(node);
        IntArray intArray = this.mTempArray;
        if (intArray == null) {
            this.mTempArray = new IntArray();
        } else {
            intArray.clear();
        }
        intArray = this.mTempArray;
        getVisibleVirtualViews(intArray);
        if (realNodeCount <= 0 || intArray.size() <= 0) {
            int N = intArray.size();
            for (int i = 0; i < N; i++) {
                node.addChild(this.mView, intArray.get(i));
            }
            return node;
        }
        throw new RuntimeException("Views cannot have both real and virtual children");
    }

    private AccessibilityNodeInfo createNodeForChild(int virtualViewId) {
        ensureTempRects();
        Rect tempParentRect = this.mTempParentRect;
        int[] tempGlobalRect = this.mTempGlobalRect;
        Rect tempScreenRect = this.mTempScreenRect;
        AccessibilityNodeInfo node = AccessibilityNodeInfo.obtain();
        node.setEnabled(true);
        node.setClassName(DEFAULT_CLASS_NAME);
        node.setBoundsInParent(INVALID_PARENT_BOUNDS);
        onPopulateNodeForVirtualView(virtualViewId, node);
        if (node.getText() == null && node.getContentDescription() == null) {
            throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
        }
        node.getBoundsInParent(tempParentRect);
        if (tempParentRect.equals(INVALID_PARENT_BOUNDS)) {
            throw new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
        }
        int actions = node.getActions();
        if ((actions & 64) != 0) {
            throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
        } else if ((actions & 128) == 0) {
            node.setPackageName(this.mView.getContext().getPackageName());
            node.setSource(this.mView, virtualViewId);
            node.setParent(this.mView);
            if (this.mFocusedVirtualViewId == virtualViewId) {
                node.setAccessibilityFocused(true);
                node.addAction(AccessibilityAction.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            } else {
                node.setAccessibilityFocused(false);
                node.addAction(AccessibilityAction.ACTION_ACCESSIBILITY_FOCUS);
            }
            if (intersectVisibleToUser(tempParentRect)) {
                node.setVisibleToUser(true);
                node.setBoundsInParent(tempParentRect);
            }
            this.mView.getLocationOnScreen(tempGlobalRect);
            int offsetX = tempGlobalRect[0];
            int offsetY = tempGlobalRect[1];
            tempScreenRect.set(tempParentRect);
            tempScreenRect.offset(offsetX, offsetY);
            node.setBoundsInScreen(tempScreenRect);
            return node;
        } else {
            throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
        }
    }

    private void ensureTempRects() {
        this.mTempGlobalRect = new int[2];
        this.mTempParentRect = new Rect();
        this.mTempScreenRect = new Rect();
    }

    private boolean performAction(int virtualViewId, int action, Bundle arguments) {
        if (virtualViewId != -1) {
            return performActionForChild(virtualViewId, action, arguments);
        }
        return performActionForHost(action, arguments);
    }

    private boolean performActionForHost(int action, Bundle arguments) {
        return this.mView.performAccessibilityAction(action, arguments);
    }

    private boolean performActionForChild(int virtualViewId, int action, Bundle arguments) {
        if (action == 64 || action == 128) {
            return manageFocusForChild(virtualViewId, action);
        }
        return onPerformActionForVirtualView(virtualViewId, action, arguments);
    }

    private boolean manageFocusForChild(int virtualViewId, int action) {
        if (action == 64) {
            return requestAccessibilityFocus(virtualViewId);
        }
        if (action != 128) {
            return false;
        }
        return clearAccessibilityFocus(virtualViewId);
    }

    /* JADX WARNING: Missing block: B:26:0x0054, code skipped:
            return false;
     */
    private boolean intersectVisibleToUser(android.graphics.Rect r6) {
        /*
        r5 = this;
        r0 = 0;
        if (r6 == 0) goto L_0x0054;
    L_0x0003:
        r1 = r6.isEmpty();
        if (r1 == 0) goto L_0x000a;
    L_0x0009:
        goto L_0x0054;
    L_0x000a:
        r1 = r5.mView;
        r1 = r1.getWindowVisibility();
        if (r1 == 0) goto L_0x0013;
    L_0x0012:
        return r0;
    L_0x0013:
        r1 = r5.mView;
        r1 = r1.getParent();
    L_0x0019:
        r2 = r1 instanceof android.view.View;
        if (r2 == 0) goto L_0x0036;
    L_0x001d:
        r2 = r1;
        r2 = (android.view.View) r2;
        r3 = r2.getAlpha();
        r4 = 0;
        r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r3 <= 0) goto L_0x0035;
    L_0x0029:
        r3 = r2.getVisibility();
        if (r3 == 0) goto L_0x0030;
    L_0x002f:
        goto L_0x0035;
    L_0x0030:
        r1 = r2.getParent();
        goto L_0x0019;
    L_0x0035:
        return r0;
    L_0x0036:
        if (r1 != 0) goto L_0x0039;
    L_0x0038:
        return r0;
    L_0x0039:
        r2 = r5.mTempVisibleRect;
        if (r2 != 0) goto L_0x0044;
    L_0x003d:
        r2 = new android.graphics.Rect;
        r2.<init>();
        r5.mTempVisibleRect = r2;
    L_0x0044:
        r2 = r5.mTempVisibleRect;
        r3 = r5.mView;
        r3 = r3.getLocalVisibleRect(r2);
        if (r3 != 0) goto L_0x004f;
    L_0x004e:
        return r0;
    L_0x004f:
        r0 = r6.intersect(r2);
        return r0;
    L_0x0054:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.ExploreByTouchHelper.intersectVisibleToUser(android.graphics.Rect):boolean");
    }

    private boolean isAccessibilityFocused(int virtualViewId) {
        return this.mFocusedVirtualViewId == virtualViewId;
    }

    /* JADX WARNING: Missing block: B:12:0x003b, code skipped:
            return false;
     */
    private boolean requestAccessibilityFocus(int r4) {
        /*
        r3 = this;
        r0 = r3.mContext;
        r1 = "accessibility";
        r0 = r0.getSystemService(r1);
        r0 = (android.view.accessibility.AccessibilityManager) r0;
        r1 = r3.mManager;
        r1 = r1.isEnabled();
        r2 = 0;
        if (r1 == 0) goto L_0x003b;
    L_0x0013:
        r1 = r0.isTouchExplorationEnabled();
        if (r1 != 0) goto L_0x001a;
    L_0x0019:
        goto L_0x003b;
    L_0x001a:
        r1 = r3.isAccessibilityFocused(r4);
        if (r1 != 0) goto L_0x003a;
    L_0x0020:
        r1 = r3.mFocusedVirtualViewId;
        r2 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        if (r1 == r2) goto L_0x002b;
    L_0x0026:
        r2 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r3.sendEventForVirtualView(r1, r2);
    L_0x002b:
        r3.mFocusedVirtualViewId = r4;
        r1 = r3.mView;
        r1.invalidate();
        r1 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r3.sendEventForVirtualView(r4, r1);
        r1 = 1;
        return r1;
    L_0x003a:
        return r2;
    L_0x003b:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.ExploreByTouchHelper.requestAccessibilityFocus(int):boolean");
    }

    private boolean clearAccessibilityFocus(int virtualViewId) {
        if (!isAccessibilityFocused(virtualViewId)) {
            return false;
        }
        this.mFocusedVirtualViewId = Integer.MIN_VALUE;
        this.mView.invalidate();
        sendEventForVirtualView(virtualViewId, 65536);
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void onPopulateEventForHost(AccessibilityEvent event) {
    }

    /* Access modifiers changed, original: protected */
    public void onPopulateNodeForHost(AccessibilityNodeInfo node) {
    }
}
