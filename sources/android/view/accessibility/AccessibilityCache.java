package android.view.accessibility;

import android.os.Build;
import android.util.ArraySet;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.List;

public class AccessibilityCache {
    public static final int CACHE_CRITICAL_EVENTS_MASK = 4307005;
    private static final boolean CHECK_INTEGRITY = Build.IS_ENG;
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "AccessibilityCache";
    private long mAccessibilityFocus = 2147483647L;
    private final AccessibilityNodeRefresher mAccessibilityNodeRefresher;
    private long mInputFocus = 2147483647L;
    private boolean mIsAllWindowsCached;
    private final Object mLock = new Object();
    private final SparseArray<LongSparseArray<AccessibilityNodeInfo>> mNodeCache = new SparseArray();
    private final SparseArray<AccessibilityWindowInfo> mTempWindowArray = new SparseArray();
    private final SparseArray<AccessibilityWindowInfo> mWindowCache = new SparseArray();

    public static class AccessibilityNodeRefresher {
        public boolean refreshNode(AccessibilityNodeInfo info, boolean bypassCache) {
            return info.refresh(null, bypassCache);
        }
    }

    public AccessibilityCache(AccessibilityNodeRefresher nodeRefresher) {
        this.mAccessibilityNodeRefresher = nodeRefresher;
    }

    public void setWindows(List<AccessibilityWindowInfo> windows) {
        synchronized (this.mLock) {
            clearWindowCache();
            if (windows == null) {
                return;
            }
            int windowCount = windows.size();
            for (int i = 0; i < windowCount; i++) {
                addWindow((AccessibilityWindowInfo) windows.get(i));
            }
            this.mIsAllWindowsCached = true;
        }
    }

    public void addWindow(AccessibilityWindowInfo window) {
        synchronized (this.mLock) {
            this.mWindowCache.put(window.getId(), new AccessibilityWindowInfo(window));
        }
    }

    public void onAccessibilityEvent(AccessibilityEvent event) {
        synchronized (this.mLock) {
            switch (event.getEventType()) {
                case 1:
                case 4:
                case 16:
                case 8192:
                    refreshCachedNodeLocked(event.getWindowId(), event.getSourceNodeId());
                    break;
                case 8:
                    if (this.mInputFocus != 2147483647L) {
                        refreshCachedNodeLocked(event.getWindowId(), this.mInputFocus);
                    }
                    this.mInputFocus = event.getSourceNodeId();
                    refreshCachedNodeLocked(event.getWindowId(), this.mInputFocus);
                    break;
                case 32:
                case 4194304:
                    clear();
                    break;
                case 2048:
                    synchronized (this.mLock) {
                        int windowId = event.getWindowId();
                        long sourceId = event.getSourceNodeId();
                        if ((event.getContentChangeTypes() & 1) != 0) {
                            clearSubTreeLocked(windowId, sourceId);
                        } else {
                            refreshCachedNodeLocked(windowId, sourceId);
                        }
                    }
                    break;
                case 4096:
                    clearSubTreeLocked(event.getWindowId(), event.getSourceNodeId());
                    break;
                case 32768:
                    if (this.mAccessibilityFocus != 2147483647L) {
                        refreshCachedNodeLocked(event.getWindowId(), this.mAccessibilityFocus);
                    }
                    this.mAccessibilityFocus = event.getSourceNodeId();
                    refreshCachedNodeLocked(event.getWindowId(), this.mAccessibilityFocus);
                    break;
                case 65536:
                    if (this.mAccessibilityFocus == event.getSourceNodeId()) {
                        refreshCachedNodeLocked(event.getWindowId(), this.mAccessibilityFocus);
                        this.mAccessibilityFocus = 2147483647L;
                        break;
                    }
                    break;
                default:
                    break;
            }
        }
        if (CHECK_INTEGRITY) {
            checkIntegrity();
        }
    }

    private void refreshCachedNodeLocked(int windowId, long sourceId) {
        LongSparseArray<AccessibilityNodeInfo> nodes = (LongSparseArray) this.mNodeCache.get(windowId);
        if (nodes != null) {
            AccessibilityNodeInfo cachedInfo = (AccessibilityNodeInfo) nodes.get(sourceId);
            if (cachedInfo != null && !this.mAccessibilityNodeRefresher.refreshNode(cachedInfo, true)) {
                clearSubTreeLocked(windowId, sourceId);
            }
        }
    }

    /* JADX WARNING: Missing block: B:12:0x001f, code skipped:
            return r2;
     */
    public android.view.accessibility.AccessibilityNodeInfo getNode(int r5, long r6) {
        /*
        r4 = this;
        r0 = r4.mLock;
        monitor-enter(r0);
        r1 = r4.mNodeCache;	 Catch:{ all -> 0x0020 }
        r1 = r1.get(r5);	 Catch:{ all -> 0x0020 }
        r1 = (android.util.LongSparseArray) r1;	 Catch:{ all -> 0x0020 }
        if (r1 != 0) goto L_0x0010;
    L_0x000d:
        r2 = 0;
        monitor-exit(r0);	 Catch:{ all -> 0x0020 }
        return r2;
    L_0x0010:
        r2 = r1.get(r6);	 Catch:{ all -> 0x0020 }
        r2 = (android.view.accessibility.AccessibilityNodeInfo) r2;	 Catch:{ all -> 0x0020 }
        if (r2 == 0) goto L_0x001e;
    L_0x0018:
        r3 = new android.view.accessibility.AccessibilityNodeInfo;	 Catch:{ all -> 0x0020 }
        r3.<init>(r2);	 Catch:{ all -> 0x0020 }
        r2 = r3;
    L_0x001e:
        monitor-exit(r0);	 Catch:{ all -> 0x0020 }
        return r2;
    L_0x0020:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0020 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityCache.getNode(int, long):android.view.accessibility.AccessibilityNodeInfo");
    }

    public List<AccessibilityWindowInfo> getWindows() {
        synchronized (this.mLock) {
            if (this.mIsAllWindowsCached) {
                int windowCount = this.mWindowCache.size();
                if (windowCount > 0) {
                    int i;
                    SparseArray<AccessibilityWindowInfo> sortedWindows = this.mTempWindowArray;
                    sortedWindows.clear();
                    for (i = 0; i < windowCount; i++) {
                        AccessibilityWindowInfo window = (AccessibilityWindowInfo) this.mWindowCache.valueAt(i);
                        sortedWindows.put(window.getLayer(), window);
                    }
                    i = sortedWindows.size();
                    List<AccessibilityWindowInfo> windows = new ArrayList(i);
                    for (int i2 = i - 1; i2 >= 0; i2--) {
                        windows.add(new AccessibilityWindowInfo((AccessibilityWindowInfo) sortedWindows.valueAt(i2)));
                        sortedWindows.removeAt(i2);
                    }
                    return windows;
                }
                return null;
            }
            return null;
        }
    }

    public AccessibilityWindowInfo getWindow(int windowId) {
        synchronized (this.mLock) {
            AccessibilityWindowInfo window = (AccessibilityWindowInfo) this.mWindowCache.get(windowId);
            if (window != null) {
                AccessibilityWindowInfo accessibilityWindowInfo = new AccessibilityWindowInfo(window);
                return accessibilityWindowInfo;
            }
            return null;
        }
    }

    /* JADX WARNING: Missing block: B:39:0x0095, code skipped:
            return;
     */
    public void add(android.view.accessibility.AccessibilityNodeInfo r13) {
        /*
        r12 = this;
        r0 = r12.mLock;
        monitor-enter(r0);
        r1 = r13.getWindowId();	 Catch:{ all -> 0x0096 }
        r2 = r12.mNodeCache;	 Catch:{ all -> 0x0096 }
        r2 = r2.get(r1);	 Catch:{ all -> 0x0096 }
        r2 = (android.util.LongSparseArray) r2;	 Catch:{ all -> 0x0096 }
        if (r2 != 0) goto L_0x001c;
    L_0x0011:
        r3 = new android.util.LongSparseArray;	 Catch:{ all -> 0x0096 }
        r3.<init>();	 Catch:{ all -> 0x0096 }
        r2 = r3;
        r3 = r12.mNodeCache;	 Catch:{ all -> 0x0096 }
        r3.put(r1, r2);	 Catch:{ all -> 0x0096 }
    L_0x001c:
        r3 = r13.getSourceNodeId();	 Catch:{ all -> 0x0096 }
        r5 = r2.get(r3);	 Catch:{ all -> 0x0096 }
        r5 = (android.view.accessibility.AccessibilityNodeInfo) r5;	 Catch:{ all -> 0x0096 }
        if (r5 == 0) goto L_0x005f;
    L_0x0028:
        r6 = r13.getChildNodeIds();	 Catch:{ all -> 0x0096 }
        r7 = r5.getChildCount();	 Catch:{ all -> 0x0096 }
        r8 = 0;
    L_0x0031:
        if (r8 >= r7) goto L_0x0050;
    L_0x0033:
        r9 = r5.getChildId(r8);	 Catch:{ all -> 0x0096 }
        if (r6 == 0) goto L_0x003f;
    L_0x0039:
        r11 = r6.indexOf(r9);	 Catch:{ all -> 0x0096 }
        if (r11 >= 0) goto L_0x0042;
    L_0x003f:
        r12.clearSubTreeLocked(r1, r9);	 Catch:{ all -> 0x0096 }
    L_0x0042:
        r11 = r2.get(r3);	 Catch:{ all -> 0x0096 }
        if (r11 != 0) goto L_0x004d;
    L_0x0048:
        r12.clearNodesForWindowLocked(r1);	 Catch:{ all -> 0x0096 }
        monitor-exit(r0);	 Catch:{ all -> 0x0096 }
        return;
    L_0x004d:
        r8 = r8 + 1;
        goto L_0x0031;
    L_0x0050:
        r8 = r5.getParentNodeId();	 Catch:{ all -> 0x0096 }
        r10 = r13.getParentNodeId();	 Catch:{ all -> 0x0096 }
        r10 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1));
        if (r10 == 0) goto L_0x005f;
    L_0x005c:
        r12.clearSubTreeLocked(r1, r8);	 Catch:{ all -> 0x0096 }
    L_0x005f:
        r6 = new android.view.accessibility.AccessibilityNodeInfo;	 Catch:{ all -> 0x0096 }
        r6.<init>(r13);	 Catch:{ all -> 0x0096 }
        r2.put(r3, r6);	 Catch:{ all -> 0x0096 }
        r7 = r6.isAccessibilityFocused();	 Catch:{ all -> 0x0096 }
        r8 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        if (r7 == 0) goto L_0x0084;
    L_0x0070:
        r10 = r12.mAccessibilityFocus;	 Catch:{ all -> 0x0096 }
        r7 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1));
        if (r7 == 0) goto L_0x0081;
    L_0x0076:
        r7 = r12.mAccessibilityFocus;	 Catch:{ all -> 0x0096 }
        r7 = (r7 > r3 ? 1 : (r7 == r3 ? 0 : -1));
        if (r7 == 0) goto L_0x0081;
    L_0x007c:
        r7 = r12.mAccessibilityFocus;	 Catch:{ all -> 0x0096 }
        r12.refreshCachedNodeLocked(r1, r7);	 Catch:{ all -> 0x0096 }
    L_0x0081:
        r12.mAccessibilityFocus = r3;	 Catch:{ all -> 0x0096 }
        goto L_0x008c;
    L_0x0084:
        r10 = r12.mAccessibilityFocus;	 Catch:{ all -> 0x0096 }
        r7 = (r10 > r3 ? 1 : (r10 == r3 ? 0 : -1));
        if (r7 != 0) goto L_0x008c;
    L_0x008a:
        r12.mAccessibilityFocus = r8;	 Catch:{ all -> 0x0096 }
    L_0x008c:
        r7 = r6.isFocused();	 Catch:{ all -> 0x0096 }
        if (r7 == 0) goto L_0x0094;
    L_0x0092:
        r12.mInputFocus = r3;	 Catch:{ all -> 0x0096 }
    L_0x0094:
        monitor-exit(r0);	 Catch:{ all -> 0x0096 }
        return;
    L_0x0096:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0096 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityCache.add(android.view.accessibility.AccessibilityNodeInfo):void");
    }

    public void clear() {
        synchronized (this.mLock) {
            clearWindowCache();
            for (int i = this.mNodeCache.size() - 1; i >= 0; i--) {
                clearNodesForWindowLocked(this.mNodeCache.keyAt(i));
            }
            this.mAccessibilityFocus = 2147483647L;
            this.mInputFocus = 2147483647L;
        }
    }

    private void clearWindowCache() {
        this.mWindowCache.clear();
        this.mIsAllWindowsCached = false;
    }

    private void clearNodesForWindowLocked(int windowId) {
        if (((LongSparseArray) this.mNodeCache.get(windowId)) != null) {
            this.mNodeCache.remove(windowId);
        }
    }

    private void clearSubTreeLocked(int windowId, long rootNodeId) {
        LongSparseArray<AccessibilityNodeInfo> nodes = (LongSparseArray) this.mNodeCache.get(windowId);
        if (nodes != null) {
            clearSubTreeRecursiveLocked(nodes, rootNodeId);
        }
    }

    private boolean clearSubTreeRecursiveLocked(LongSparseArray<AccessibilityNodeInfo> nodes, long rootNodeId) {
        AccessibilityNodeInfo current = (AccessibilityNodeInfo) nodes.get(rootNodeId);
        if (current == null) {
            clear();
            return true;
        }
        nodes.remove(rootNodeId);
        int childCount = current.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (clearSubTreeRecursiveLocked(nodes, current.getChildId(i))) {
                return true;
            }
        }
        return false;
    }

    public void checkIntegrity() {
        AccessibilityCache accessibilityCache = this;
        synchronized (accessibilityCache.mLock) {
            if (accessibilityCache.mWindowCache.size() > 0 || accessibilityCache.mNodeCache.size() != 0) {
                AccessibilityWindowInfo activeWindow;
                int windowCount;
                AccessibilityWindowInfo focusedWindow = null;
                AccessibilityWindowInfo activeWindow2 = null;
                int windowCount2 = accessibilityCache.mWindowCache.size();
                for (int i = 0; i < windowCount2; i++) {
                    String str;
                    StringBuilder stringBuilder;
                    AccessibilityWindowInfo window = (AccessibilityWindowInfo) accessibilityCache.mWindowCache.valueAt(i);
                    if (window.isActive()) {
                        if (activeWindow2 != null) {
                            str = LOG_TAG;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Duplicate active window:");
                            stringBuilder.append(window);
                            Log.e(str, stringBuilder.toString());
                        } else {
                            activeWindow2 = window;
                        }
                    }
                    if (window.isFocused()) {
                        if (focusedWindow != null) {
                            str = LOG_TAG;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Duplicate focused window:");
                            stringBuilder.append(window);
                            Log.e(str, stringBuilder.toString());
                        } else {
                            focusedWindow = window;
                        }
                    }
                }
                AccessibilityNodeInfo accessFocus = null;
                AccessibilityNodeInfo inputFocus = null;
                int nodesForWindowCount = accessibilityCache.mNodeCache.size();
                int i2 = 0;
                while (i2 < nodesForWindowCount) {
                    AccessibilityWindowInfo focusedWindow2;
                    LongSparseArray<AccessibilityNodeInfo> nodes = (LongSparseArray) accessibilityCache.mNodeCache.valueAt(i2);
                    if (nodes.size() <= 0) {
                        focusedWindow2 = focusedWindow;
                        activeWindow = activeWindow2;
                        windowCount = windowCount2;
                    } else {
                        ArraySet<AccessibilityNodeInfo> seen = new ArraySet();
                        int windowId = accessibilityCache.mNodeCache.keyAt(i2);
                        int nodeCount = nodes.size();
                        int j = 0;
                        while (j < nodeCount) {
                            AccessibilityNodeInfo node = (AccessibilityNodeInfo) nodes.valueAt(j);
                            if (seen.add(node)) {
                                StringBuilder stringBuilder2;
                                int k;
                                AccessibilityNodeInfo accessFocus2;
                                AccessibilityNodeInfo inputFocus2;
                                focusedWindow2 = focusedWindow;
                                if (node.isAccessibilityFocused() != null) {
                                    if (accessFocus != null) {
                                        focusedWindow = LOG_TAG;
                                        stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append("Duplicate accessibility focus:");
                                        stringBuilder2.append(node);
                                        stringBuilder2.append(" in window:");
                                        stringBuilder2.append(windowId);
                                        Log.e(focusedWindow, stringBuilder2.toString());
                                    } else {
                                        accessFocus = node;
                                    }
                                }
                                if (node.isFocused() != null) {
                                    if (inputFocus != null) {
                                        focusedWindow = LOG_TAG;
                                        stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append("Duplicate input focus: ");
                                        stringBuilder2.append(node);
                                        stringBuilder2.append(" in window:");
                                        stringBuilder2.append(windowId);
                                        Log.e(focusedWindow, stringBuilder2.toString());
                                    } else {
                                        inputFocus = node;
                                    }
                                }
                                AccessibilityNodeInfo focusedWindow3 = (AccessibilityNodeInfo) nodes.get(node.getParentNodeId());
                                if (focusedWindow3 != null) {
                                    int childCount = focusedWindow3.getChildCount();
                                    boolean childOfItsParent = false;
                                    k = 0;
                                    while (k < childCount) {
                                        activeWindow = activeWindow2;
                                        windowCount = windowCount2;
                                        if (((AccessibilityNodeInfo) nodes.get(focusedWindow3.getChildId(k))) == node) {
                                            childOfItsParent = true;
                                            break;
                                        }
                                        k++;
                                        windowCount2 = windowCount;
                                        activeWindow2 = activeWindow;
                                    }
                                    activeWindow = activeWindow2;
                                    windowCount = windowCount2;
                                    if (!childOfItsParent) {
                                        String str2 = LOG_TAG;
                                        StringBuilder stringBuilder3 = new StringBuilder();
                                        stringBuilder3.append("Invalid parent-child relation between parent: ");
                                        stringBuilder3.append(focusedWindow3);
                                        stringBuilder3.append(" and child: ");
                                        stringBuilder3.append(node);
                                        Log.e(str2, stringBuilder3.toString());
                                    }
                                } else {
                                    activeWindow = activeWindow2;
                                    windowCount = windowCount2;
                                }
                                k = node.getChildCount();
                                int k2 = 0;
                                while (k2 < k) {
                                    int childCount2;
                                    accessFocus2 = accessFocus;
                                    AccessibilityNodeInfo child = (AccessibilityNodeInfo) nodes.get(node.getChildId(k2));
                                    AccessibilityNodeInfo accessibilityNodeInfo;
                                    if (child != null) {
                                        inputFocus2 = inputFocus;
                                        if (((AccessibilityNodeInfo) nodes.get(child.getParentNodeId())) != node) {
                                            String str3 = LOG_TAG;
                                            childCount2 = k;
                                            stringBuilder2 = new StringBuilder();
                                            stringBuilder2.append("Invalid child-parent relation between child: ");
                                            stringBuilder2.append(node);
                                            stringBuilder2.append(" and parent: ");
                                            stringBuilder2.append(focusedWindow3);
                                            Log.e(str3, stringBuilder2.toString());
                                        } else {
                                            childCount2 = k;
                                            accessibilityNodeInfo = child;
                                        }
                                    } else {
                                        childCount2 = k;
                                        accessibilityNodeInfo = child;
                                        inputFocus2 = inputFocus;
                                    }
                                    k2++;
                                    accessFocus = accessFocus2;
                                    inputFocus = inputFocus2;
                                    k = childCount2;
                                }
                                accessFocus2 = accessFocus;
                                inputFocus2 = inputFocus;
                            } else {
                                String str4 = LOG_TAG;
                                focusedWindow2 = focusedWindow;
                                focusedWindow = new StringBuilder();
                                focusedWindow.append("Duplicate node: ");
                                focusedWindow.append(node);
                                focusedWindow.append(" in window:");
                                focusedWindow.append(windowId);
                                Log.e(str4, focusedWindow.toString());
                                activeWindow = activeWindow2;
                                windowCount = windowCount2;
                            }
                            j++;
                            focusedWindow = focusedWindow2;
                            windowCount2 = windowCount;
                            activeWindow2 = activeWindow;
                        }
                        focusedWindow2 = focusedWindow;
                        activeWindow = activeWindow2;
                        windowCount = windowCount2;
                    }
                    i2++;
                    accessibilityCache = this;
                    focusedWindow = focusedWindow2;
                    windowCount2 = windowCount;
                    activeWindow2 = activeWindow;
                }
                activeWindow = activeWindow2;
                windowCount = windowCount2;
                return;
            }
        }
    }
}
