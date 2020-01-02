package android.view;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.style.AccessibilityClickableSpan;
import android.text.style.ClickableSpan;
import android.util.LongSparseArray;
import android.util.Slog;
import android.view.accessibility.AccessibilityInteractionClient;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeIdManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.accessibility.AccessibilityRequestPreparer;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.os.SomeArgs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.function.Predicate;

@VisibleForTesting(visibility = Visibility.PACKAGE)
public final class AccessibilityInteractionController {
    private static final boolean CONSIDER_REQUEST_PREPARERS = false;
    private static final boolean ENFORCE_NODE_TREE_CONSISTENT = false;
    private static final boolean IGNORE_REQUEST_PREPARERS = true;
    private static final String LOG_TAG = "AccessibilityInteractionController";
    private static final long REQUEST_PREPARER_TIMEOUT_MS = 500;
    private final AccessibilityManager mA11yManager;
    @GuardedBy({"mLock"})
    private int mActiveRequestPreparerId;
    private AddNodeInfosForViewId mAddNodeInfosForViewId;
    private final PrivateHandler mHandler;
    private final Object mLock = new Object();
    @GuardedBy({"mLock"})
    private List<MessageHolder> mMessagesWaitingForRequestPreparer;
    private final long mMyLooperThreadId;
    private final int mMyProcessId;
    @GuardedBy({"mLock"})
    private int mNumActiveRequestPreparers;
    private final AccessibilityNodePrefetcher mPrefetcher;
    private final ArrayList<AccessibilityNodeInfo> mTempAccessibilityNodeInfoList = new ArrayList();
    private final ArrayList<View> mTempArrayList = new ArrayList();
    private final Point mTempPoint = new Point();
    private final Rect mTempRect = new Rect();
    private final Rect mTempRect1 = new Rect();
    private final Rect mTempRect2 = new Rect();
    private final ViewRootImpl mViewRootImpl;

    private class AccessibilityNodePrefetcher {
        private static final int MAX_ACCESSIBILITY_NODE_INFO_BATCH_SIZE = 50;
        private final ArrayList<View> mTempViewList;

        private AccessibilityNodePrefetcher() {
            this.mTempViewList = new ArrayList();
        }

        public void prefetchAccessibilityNodeInfos(View view, int virtualViewId, int fetchFlags, List<AccessibilityNodeInfo> outInfos, Bundle arguments) {
            String extraDataRequested;
            AccessibilityNodeProvider provider = view.getAccessibilityNodeProvider();
            if (arguments == null) {
                extraDataRequested = null;
            } else {
                extraDataRequested = arguments.getString(AccessibilityNodeInfo.EXTRA_DATA_REQUESTED_KEY);
            }
            AccessibilityNodeInfo root;
            if (provider == null) {
                root = view.createAccessibilityNodeInfo();
                if (root != null) {
                    if (extraDataRequested != null) {
                        view.addExtraDataToAccessibilityNodeInfo(root, extraDataRequested, arguments);
                    }
                    outInfos.add(root);
                    if ((fetchFlags & 1) != 0) {
                        prefetchPredecessorsOfRealNode(view, outInfos);
                    }
                    if ((fetchFlags & 2) != 0) {
                        prefetchSiblingsOfRealNode(view, outInfos);
                    }
                    if ((fetchFlags & 4) != 0) {
                        prefetchDescendantsOfRealNode(view, outInfos);
                        return;
                    }
                    return;
                }
                return;
            }
            root = provider.createAccessibilityNodeInfo(virtualViewId);
            if (root != null) {
                if (extraDataRequested != null) {
                    provider.addExtraDataToAccessibilityNodeInfo(virtualViewId, root, extraDataRequested, arguments);
                }
                outInfos.add(root);
                if ((fetchFlags & 1) != 0) {
                    prefetchPredecessorsOfVirtualNode(root, view, provider, outInfos);
                }
                if ((fetchFlags & 2) != 0) {
                    prefetchSiblingsOfVirtualNode(root, view, provider, outInfos);
                }
                if ((fetchFlags & 4) != 0) {
                    prefetchDescendantsOfVirtualNode(root, provider, outInfos);
                }
            }
        }

        private void enforceNodeTreeConsistent(List<AccessibilityNodeInfo> nodes) {
            AccessibilityNodeInfo node;
            StringBuilder stringBuilder;
            LongSparseArray<AccessibilityNodeInfo> nodeMap = new LongSparseArray();
            int nodeCount = nodes.size();
            for (int i = 0; i < nodeCount; i++) {
                node = (AccessibilityNodeInfo) nodes.get(i);
                nodeMap.put(node.getSourceNodeId(), node);
            }
            AccessibilityNodeInfo root = (AccessibilityNodeInfo) nodeMap.valueAt(0);
            for (node = root; node != null; node = (AccessibilityNodeInfo) nodeMap.get(node.getParentNodeId())) {
                root = node;
            }
            AccessibilityNodeInfo accessFocus = null;
            AccessibilityNodeInfo inputFocus = null;
            HashSet<AccessibilityNodeInfo> seen = new HashSet();
            Queue<AccessibilityNodeInfo> fringe = new LinkedList();
            fringe.add(root);
            while (!fringe.isEmpty()) {
                AccessibilityNodeInfo current = (AccessibilityNodeInfo) fringe.poll();
                String str = " in window:";
                if (seen.add(current)) {
                    if (current.isAccessibilityFocused()) {
                        if (accessFocus == null) {
                            accessFocus = current;
                        } else {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Duplicate accessibility focus:");
                            stringBuilder.append(current);
                            stringBuilder.append(str);
                            stringBuilder.append(AccessibilityInteractionController.this.mViewRootImpl.mAttachInfo.mAccessibilityWindowId);
                            throw new IllegalStateException(stringBuilder.toString());
                        }
                    }
                    if (current.isFocused()) {
                        if (inputFocus == null) {
                            inputFocus = current;
                        } else {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Duplicate input focus: ");
                            stringBuilder.append(current);
                            stringBuilder.append(str);
                            stringBuilder.append(AccessibilityInteractionController.this.mViewRootImpl.mAttachInfo.mAccessibilityWindowId);
                            throw new IllegalStateException(stringBuilder.toString());
                        }
                    }
                    int childCount = current.getChildCount();
                    for (int j = 0; j < childCount; j++) {
                        AccessibilityNodeInfo child = (AccessibilityNodeInfo) nodeMap.get(current.getChildId(j));
                        if (child != null) {
                            fringe.add(child);
                        }
                    }
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Duplicate node: ");
                    stringBuilder.append(current);
                    stringBuilder.append(str);
                    stringBuilder.append(AccessibilityInteractionController.this.mViewRootImpl.mAttachInfo.mAccessibilityWindowId);
                    throw new IllegalStateException(stringBuilder.toString());
                }
            }
            int j2 = nodeMap.size() - 1;
            while (j2 >= 0) {
                AccessibilityNodeInfo info = (AccessibilityNodeInfo) nodeMap.valueAt(j2);
                if (seen.contains(info)) {
                    j2--;
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Disconnected node: ");
                    stringBuilder.append(info);
                    throw new IllegalStateException(stringBuilder.toString());
                }
            }
        }

        private void prefetchPredecessorsOfRealNode(View view, List<AccessibilityNodeInfo> outInfos) {
            for (ViewParent parent = view.getParentForAccessibility(); (parent instanceof View) && outInfos.size() < 50; parent = parent.getParentForAccessibility()) {
                AccessibilityNodeInfo info = ((View) parent).createAccessibilityNodeInfo();
                if (info != null) {
                    outInfos.add(info);
                }
            }
        }

        private void prefetchSiblingsOfRealNode(View current, List<AccessibilityNodeInfo> outInfos) {
            ViewParent parent = current.getParentForAccessibility();
            if (parent instanceof ViewGroup) {
                ViewGroup parentGroup = (ViewGroup) parent;
                ArrayList<View> children = this.mTempViewList;
                children.clear();
                try {
                    parentGroup.addChildrenForAccessibility(children);
                    int childCount = children.size();
                    int i = 0;
                    while (i < childCount) {
                        if (outInfos.size() < 50) {
                            View child = (View) children.get(i);
                            if (child.getAccessibilityViewId() != current.getAccessibilityViewId() && AccessibilityInteractionController.this.isShown(child)) {
                                AccessibilityNodeInfo info;
                                AccessibilityNodeProvider provider = child.getAccessibilityNodeProvider();
                                if (provider == null) {
                                    info = child.createAccessibilityNodeInfo();
                                } else {
                                    info = provider.createAccessibilityNodeInfo(-1);
                                }
                                if (info != null) {
                                    outInfos.add(info);
                                }
                            }
                            i++;
                        } else {
                            return;
                        }
                    }
                    children.clear();
                } finally {
                    children.clear();
                }
            }
        }

        private void prefetchDescendantsOfRealNode(View root, List<AccessibilityNodeInfo> outInfos) {
            if (root instanceof ViewGroup) {
                HashMap<View, AccessibilityNodeInfo> addedChildren = new HashMap();
                ArrayList<View> children = this.mTempViewList;
                children.clear();
                try {
                    View child;
                    root.addChildrenForAccessibility(children);
                    int childCount = children.size();
                    int i = 0;
                    while (i < childCount) {
                        if (outInfos.size() < 50) {
                            child = (View) children.get(i);
                            if (AccessibilityInteractionController.this.isShown(child)) {
                                AccessibilityNodeProvider provider = child.getAccessibilityNodeProvider();
                                AccessibilityNodeInfo info;
                                if (provider == null) {
                                    info = child.createAccessibilityNodeInfo();
                                    if (info != null) {
                                        outInfos.add(info);
                                        addedChildren.put(child, null);
                                    }
                                } else {
                                    info = provider.createAccessibilityNodeInfo(-1);
                                    if (info != null) {
                                        outInfos.add(info);
                                        addedChildren.put(child, info);
                                    }
                                }
                            }
                            i++;
                        } else {
                            return;
                        }
                    }
                    children.clear();
                    if (outInfos.size() < 50) {
                        for (Entry<View, AccessibilityNodeInfo> entry : addedChildren.entrySet()) {
                            child = (View) entry.getKey();
                            AccessibilityNodeInfo virtualRoot = (AccessibilityNodeInfo) entry.getValue();
                            if (virtualRoot == null) {
                                prefetchDescendantsOfRealNode(child, outInfos);
                            } else {
                                prefetchDescendantsOfVirtualNode(virtualRoot, child.getAccessibilityNodeProvider(), outInfos);
                            }
                        }
                    }
                } finally {
                    children.clear();
                }
            }
        }

        private void prefetchPredecessorsOfVirtualNode(AccessibilityNodeInfo root, View providerHost, AccessibilityNodeProvider provider, List<AccessibilityNodeInfo> outInfos) {
            int initialResultSize = outInfos.size();
            long parentNodeId = root.getParentNodeId();
            int accessibilityViewId = AccessibilityNodeInfo.getAccessibilityViewId(parentNodeId);
            while (accessibilityViewId != Integer.MAX_VALUE && outInfos.size() < 50) {
                int virtualDescendantId = AccessibilityNodeInfo.getVirtualDescendantId(parentNodeId);
                if (virtualDescendantId != -1 || accessibilityViewId == providerHost.getAccessibilityViewId()) {
                    AccessibilityNodeInfo parent = provider.createAccessibilityNodeInfo(virtualDescendantId);
                    if (parent == null) {
                        for (int i = outInfos.size() - 1; i >= initialResultSize; i--) {
                            outInfos.remove(i);
                        }
                        return;
                    }
                    outInfos.add(parent);
                    parentNodeId = parent.getParentNodeId();
                    accessibilityViewId = AccessibilityNodeInfo.getAccessibilityViewId(parentNodeId);
                } else {
                    prefetchPredecessorsOfRealNode(providerHost, outInfos);
                    return;
                }
            }
        }

        private void prefetchSiblingsOfVirtualNode(AccessibilityNodeInfo current, View providerHost, AccessibilityNodeProvider provider, List<AccessibilityNodeInfo> outInfos) {
            long parentNodeId = current.getParentNodeId();
            int parentAccessibilityViewId = AccessibilityNodeInfo.getAccessibilityViewId(parentNodeId);
            int parentVirtualDescendantId = AccessibilityNodeInfo.getVirtualDescendantId(parentNodeId);
            if (parentVirtualDescendantId != -1 || parentAccessibilityViewId == providerHost.getAccessibilityViewId()) {
                AccessibilityNodeInfo parent = provider.createAccessibilityNodeInfo(parentVirtualDescendantId);
                if (parent != null) {
                    int childCount = parent.getChildCount();
                    for (int i = 0; i < childCount && outInfos.size() < 50; i++) {
                        long childNodeId = parent.getChildId(i);
                        if (childNodeId != current.getSourceNodeId()) {
                            AccessibilityNodeInfo child = provider.createAccessibilityNodeInfo(AccessibilityNodeInfo.getVirtualDescendantId(childNodeId));
                            if (child != null) {
                                outInfos.add(child);
                            }
                        }
                    }
                    return;
                }
            }
            prefetchSiblingsOfRealNode(providerHost, outInfos);
        }

        private void prefetchDescendantsOfVirtualNode(AccessibilityNodeInfo root, AccessibilityNodeProvider provider, List<AccessibilityNodeInfo> outInfos) {
            int initialOutInfosSize = outInfos.size();
            int childCount = root.getChildCount();
            int i = 0;
            while (i < childCount) {
                if (outInfos.size() < 50) {
                    AccessibilityNodeInfo child = provider.createAccessibilityNodeInfo(AccessibilityNodeInfo.getVirtualDescendantId(root.getChildId(i)));
                    if (child != null) {
                        outInfos.add(child);
                    }
                    i++;
                } else {
                    return;
                }
            }
            if (outInfos.size() < 50) {
                i = outInfos.size() - initialOutInfosSize;
                for (int i2 = 0; i2 < i; i2++) {
                    prefetchDescendantsOfVirtualNode((AccessibilityNodeInfo) outInfos.get(initialOutInfosSize + i2), provider, outInfos);
                }
            }
        }
    }

    private final class AddNodeInfosForViewId implements Predicate<View> {
        private List<AccessibilityNodeInfo> mInfos;
        private int mViewId;

        private AddNodeInfosForViewId() {
            this.mViewId = -1;
        }

        public void init(int viewId, List<AccessibilityNodeInfo> infos) {
            this.mViewId = viewId;
            this.mInfos = infos;
        }

        public void reset() {
            this.mViewId = -1;
            this.mInfos = null;
        }

        public boolean test(View view) {
            if (view.getId() == this.mViewId && AccessibilityInteractionController.this.isShown(view)) {
                this.mInfos.add(view.createAccessibilityNodeInfo());
            }
            return false;
        }
    }

    private static final class MessageHolder {
        final int mInterrogatingPid;
        final long mInterrogatingTid;
        final Message mMessage;

        MessageHolder(Message message, int interrogatingPid, long interrogatingTid) {
            this.mMessage = message;
            this.mInterrogatingPid = interrogatingPid;
            this.mInterrogatingTid = interrogatingTid;
        }
    }

    private class PrivateHandler extends Handler {
        private static final int FIRST_NO_ACCESSIBILITY_CALLBACK_MSG = 100;
        private static final int MSG_APP_PREPARATION_FINISHED = 8;
        private static final int MSG_APP_PREPARATION_TIMEOUT = 9;
        private static final int MSG_CLEAR_ACCESSIBILITY_FOCUS = 101;
        private static final int MSG_FIND_ACCESSIBILITY_NODE_INFOS_BY_VIEW_ID = 3;
        private static final int MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_ACCESSIBILITY_ID = 2;
        private static final int MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_TEXT = 4;
        private static final int MSG_FIND_FOCUS = 5;
        private static final int MSG_FOCUS_SEARCH = 6;
        private static final int MSG_NOTIFY_OUTSIDE_TOUCH = 102;
        private static final int MSG_PERFORM_ACCESSIBILITY_ACTION = 1;
        private static final int MSG_PREPARE_FOR_EXTRA_DATA_REQUEST = 7;

        public PrivateHandler(Looper looper) {
            super(looper);
        }

        public String getMessageName(Message message) {
            int type = message.what;
            if (type == 101) {
                return "MSG_CLEAR_ACCESSIBILITY_FOCUS";
            }
            if (type == 102) {
                return "MSG_NOTIFY_OUTSIDE_TOUCH";
            }
            switch (type) {
                case 1:
                    return "MSG_PERFORM_ACCESSIBILITY_ACTION";
                case 2:
                    return "MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_ACCESSIBILITY_ID";
                case 3:
                    return "MSG_FIND_ACCESSIBILITY_NODE_INFOS_BY_VIEW_ID";
                case 4:
                    return "MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_TEXT";
                case 5:
                    return "MSG_FIND_FOCUS";
                case 6:
                    return "MSG_FOCUS_SEARCH";
                case 7:
                    return "MSG_PREPARE_FOR_EXTRA_DATA_REQUEST";
                case 8:
                    return "MSG_APP_PREPARATION_FINISHED";
                case 9:
                    return "MSG_APP_PREPARATION_TIMEOUT";
                default:
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown message type: ");
                    stringBuilder.append(type);
                    throw new IllegalArgumentException(stringBuilder.toString());
            }
        }

        public void handleMessage(Message message) {
            int type = message.what;
            if (type == 101) {
                AccessibilityInteractionController.this.clearAccessibilityFocusUiThread();
            } else if (type != 102) {
                switch (type) {
                    case 1:
                        AccessibilityInteractionController.this.performAccessibilityActionUiThread(message);
                        return;
                    case 2:
                        AccessibilityInteractionController.this.findAccessibilityNodeInfoByAccessibilityIdUiThread(message);
                        return;
                    case 3:
                        AccessibilityInteractionController.this.findAccessibilityNodeInfosByViewIdUiThread(message);
                        return;
                    case 4:
                        AccessibilityInteractionController.this.findAccessibilityNodeInfosByTextUiThread(message);
                        return;
                    case 5:
                        AccessibilityInteractionController.this.findFocusUiThread(message);
                        return;
                    case 6:
                        AccessibilityInteractionController.this.focusSearchUiThread(message);
                        return;
                    case 7:
                        AccessibilityInteractionController.this.prepareForExtraDataRequestUiThread(message);
                        return;
                    case 8:
                        AccessibilityInteractionController.this.requestPreparerDoneUiThread(message);
                        return;
                    case 9:
                        AccessibilityInteractionController.this.requestPreparerTimeoutUiThread();
                        return;
                    default:
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown message type: ");
                        stringBuilder.append(type);
                        throw new IllegalArgumentException(stringBuilder.toString());
                }
            } else {
                AccessibilityInteractionController.this.notifyOutsideTouchUiThread();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public boolean hasAccessibilityCallback(Message message) {
            return message.what < 100;
        }
    }

    public AccessibilityInteractionController(ViewRootImpl viewRootImpl) {
        Looper looper = viewRootImpl.mHandler.getLooper();
        this.mMyLooperThreadId = looper.getThread().getId();
        this.mMyProcessId = Process.myPid();
        this.mHandler = new PrivateHandler(looper);
        this.mViewRootImpl = viewRootImpl;
        this.mPrefetcher = new AccessibilityNodePrefetcher();
        this.mA11yManager = (AccessibilityManager) this.mViewRootImpl.mContext.getSystemService(AccessibilityManager.class);
    }

    private void scheduleMessage(Message message, int interrogatingPid, long interrogatingTid, boolean ignoreRequestPreparers) {
        if (!ignoreRequestPreparers && holdOffMessageIfNeeded(message, interrogatingPid, interrogatingTid)) {
            return;
        }
        if (interrogatingPid == this.mMyProcessId && interrogatingTid == this.mMyLooperThreadId && this.mHandler.hasAccessibilityCallback(message)) {
            AccessibilityInteractionClient.getInstanceForThread(interrogatingTid).setSameThreadMessage(message);
        } else if (this.mHandler.hasAccessibilityCallback(message) || Thread.currentThread().getId() != this.mMyLooperThreadId) {
            this.mHandler.sendMessage(message);
        } else {
            this.mHandler.handleMessage(message);
        }
    }

    private boolean isShown(View view) {
        return view != null && view.getWindowVisibility() == 0 && view.isShown();
    }

    public void findAccessibilityNodeInfoByAccessibilityIdClientThread(long accessibilityNodeId, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec, Bundle arguments) {
        Message message = this.mHandler.obtainMessage();
        message.what = 2;
        message.arg1 = flags;
        SomeArgs args = SomeArgs.obtain();
        args.argi1 = AccessibilityNodeInfo.getAccessibilityViewId(accessibilityNodeId);
        args.argi2 = AccessibilityNodeInfo.getVirtualDescendantId(accessibilityNodeId);
        args.argi3 = interactionId;
        args.arg1 = callback;
        args.arg2 = spec;
        args.arg3 = interactiveRegion;
        args.arg4 = arguments;
        message.obj = args;
        scheduleMessage(message, interrogatingPid, interrogatingTid, false);
    }

    private boolean holdOffMessageIfNeeded(Message originalMessage, int callingPid, long callingTid) {
        Message message = originalMessage;
        synchronized (this.mLock) {
            if (this.mNumActiveRequestPreparers != 0) {
                queueMessageToHandleOncePrepared(originalMessage, callingPid, callingTid);
                return true;
            } else if (message.what != 2) {
                return false;
            } else {
                SomeArgs originalMessageArgs = message.obj;
                Bundle requestArguments = (Bundle) originalMessageArgs.arg4;
                if (requestArguments == null) {
                    return false;
                }
                List<AccessibilityRequestPreparer> preparers = this.mA11yManager.getRequestPreparersForAccessibilityId(originalMessageArgs.argi1);
                if (preparers == null) {
                    return false;
                }
                String extraDataKey = requestArguments.getString(AccessibilityNodeInfo.EXTRA_DATA_REQUESTED_KEY);
                if (extraDataKey == null) {
                    return false;
                }
                this.mNumActiveRequestPreparers = preparers.size();
                for (int i = false; i < preparers.size(); i++) {
                    Message requestPreparerMessage = this.mHandler.obtainMessage(7);
                    SomeArgs requestPreparerArgs = SomeArgs.obtain();
                    requestPreparerArgs.argi1 = originalMessageArgs.argi2 == Integer.MAX_VALUE ? -1 : originalMessageArgs.argi2;
                    requestPreparerArgs.arg1 = preparers.get(i);
                    requestPreparerArgs.arg2 = extraDataKey;
                    requestPreparerArgs.arg3 = requestArguments;
                    Message preparationFinishedMessage = this.mHandler.obtainMessage(8);
                    int i2 = this.mActiveRequestPreparerId + 1;
                    this.mActiveRequestPreparerId = i2;
                    preparationFinishedMessage.arg1 = i2;
                    requestPreparerArgs.arg4 = preparationFinishedMessage;
                    requestPreparerMessage.obj = requestPreparerArgs;
                    scheduleMessage(requestPreparerMessage, callingPid, callingTid, true);
                    this.mHandler.obtainMessage(9);
                    this.mHandler.sendEmptyMessageDelayed(9, 500);
                }
                queueMessageToHandleOncePrepared(originalMessage, callingPid, callingTid);
                return true;
            }
        }
    }

    private void prepareForExtraDataRequestUiThread(Message message) {
        SomeArgs args = message.obj;
        args.arg1.onPrepareExtraData(args.argi1, args.arg2, args.arg3, args.arg4);
    }

    private void queueMessageToHandleOncePrepared(Message message, int interrogatingPid, long interrogatingTid) {
        if (this.mMessagesWaitingForRequestPreparer == null) {
            this.mMessagesWaitingForRequestPreparer = new ArrayList(1);
        }
        this.mMessagesWaitingForRequestPreparer.add(new MessageHolder(message, interrogatingPid, interrogatingTid));
    }

    /* JADX WARNING: Missing block: B:12:0x0027, code skipped:
            return;
     */
    private void requestPreparerDoneUiThread(android.os.Message r4) {
        /*
        r3 = this;
        r0 = r3.mLock;
        monitor-enter(r0);
        r1 = r4.arg1;	 Catch:{ all -> 0x0028 }
        r2 = r3.mActiveRequestPreparerId;	 Catch:{ all -> 0x0028 }
        if (r1 == r2) goto L_0x0012;
    L_0x0009:
        r1 = "AccessibilityInteractionController";
        r2 = "Surprising AccessibilityRequestPreparer callback (likely late)";
        android.util.Slog.e(r1, r2);	 Catch:{ all -> 0x0028 }
        monitor-exit(r0);	 Catch:{ all -> 0x0028 }
        return;
    L_0x0012:
        r1 = r3.mNumActiveRequestPreparers;	 Catch:{ all -> 0x0028 }
        r1 = r1 + -1;
        r3.mNumActiveRequestPreparers = r1;	 Catch:{ all -> 0x0028 }
        r1 = r3.mNumActiveRequestPreparers;	 Catch:{ all -> 0x0028 }
        if (r1 > 0) goto L_0x0026;
    L_0x001c:
        r1 = r3.mHandler;	 Catch:{ all -> 0x0028 }
        r2 = 9;
        r1.removeMessages(r2);	 Catch:{ all -> 0x0028 }
        r3.scheduleAllMessagesWaitingForRequestPreparerLocked();	 Catch:{ all -> 0x0028 }
    L_0x0026:
        monitor-exit(r0);	 Catch:{ all -> 0x0028 }
        return;
    L_0x0028:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0028 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.AccessibilityInteractionController.requestPreparerDoneUiThread(android.os.Message):void");
    }

    private void requestPreparerTimeoutUiThread() {
        synchronized (this.mLock) {
            Slog.e(LOG_TAG, "AccessibilityRequestPreparer timed out");
            scheduleAllMessagesWaitingForRequestPreparerLocked();
        }
    }

    @GuardedBy({"mLock"})
    private void scheduleAllMessagesWaitingForRequestPreparerLocked() {
        int numMessages = this.mMessagesWaitingForRequestPreparer.size();
        int i = 0;
        while (true) {
            boolean z = false;
            if (i < numMessages) {
                MessageHolder request = (MessageHolder) this.mMessagesWaitingForRequestPreparer.get(i);
                Message message = request.mMessage;
                int i2 = request.mInterrogatingPid;
                long j = request.mInterrogatingTid;
                if (i == 0) {
                    z = true;
                }
                scheduleMessage(message, i2, j, z);
                i++;
            } else {
                this.mMessagesWaitingForRequestPreparer.clear();
                this.mNumActiveRequestPreparers = 0;
                this.mActiveRequestPreparerId = -1;
                return;
            }
        }
    }

    private void findAccessibilityNodeInfoByAccessibilityIdUiThread(Message message) {
        Throwable th;
        Message message2 = message;
        int flags = message2.arg1;
        SomeArgs args = message2.obj;
        int accessibilityViewId = args.argi1;
        int virtualDescendantId = args.argi2;
        int interactionId = args.argi3;
        IAccessibilityInteractionConnectionCallback callback = args.arg1;
        MagnificationSpec spec = args.arg2;
        Region interactiveRegion = args.arg3;
        Bundle arguments = args.arg4;
        args.recycle();
        List<AccessibilityNodeInfo> infos = this.mTempAccessibilityNodeInfoList;
        infos.clear();
        List<AccessibilityNodeInfo> infos2;
        try {
            if (this.mViewRootImpl.mView == null) {
                infos2 = infos;
            } else if (this.mViewRootImpl.mAttachInfo == null) {
                infos2 = infos;
            } else {
                this.mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = flags;
                View root = findViewByAccessibilityId(accessibilityViewId);
                if (root == null || !isShown(root)) {
                    infos2 = infos;
                } else {
                    infos2 = infos;
                    try {
                        this.mPrefetcher.prefetchAccessibilityNodeInfos(root, virtualDescendantId, flags, infos, arguments);
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
                updateInfosForViewportAndReturnFindNodeResult(infos2, callback, interactionId, spec, interactiveRegion);
                return;
            }
            updateInfosForViewportAndReturnFindNodeResult(infos2, callback, interactionId, spec, interactiveRegion);
        } catch (Throwable th3) {
            th = th3;
            infos2 = infos;
            updateInfosForViewportAndReturnFindNodeResult(infos2, callback, interactionId, spec, interactiveRegion);
            throw th;
        }
    }

    public void findAccessibilityNodeInfosByViewIdClientThread(long accessibilityNodeId, String viewId, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
        Message message = this.mHandler.obtainMessage();
        message.what = 3;
        message.arg1 = flags;
        message.arg2 = AccessibilityNodeInfo.getAccessibilityViewId(accessibilityNodeId);
        SomeArgs args = SomeArgs.obtain();
        args.argi1 = interactionId;
        args.arg1 = callback;
        args.arg2 = spec;
        args.arg3 = viewId;
        args.arg4 = interactiveRegion;
        message.obj = args;
        scheduleMessage(message, interrogatingPid, interrogatingTid, false);
    }

    private void findAccessibilityNodeInfosByViewIdUiThread(Message message) {
        Throwable th;
        Message message2 = message;
        int flags = message2.arg1;
        int accessibilityViewId = message2.arg2;
        SomeArgs args = message2.obj;
        int interactionId = args.argi1;
        IAccessibilityInteractionConnectionCallback callback = args.arg1;
        MagnificationSpec spec = args.arg2;
        String viewId = args.arg3;
        Region interactiveRegion = args.arg4;
        args.recycle();
        List<AccessibilityNodeInfo> infos = this.mTempAccessibilityNodeInfoList;
        infos.clear();
        int i;
        List<AccessibilityNodeInfo> flags2;
        try {
            if (this.mViewRootImpl.mView == null) {
                flags = infos;
            } else if (this.mViewRootImpl.mAttachInfo == null) {
                i = flags;
                flags = infos;
            } else {
                this.mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = flags;
                View root = findViewByAccessibilityId(accessibilityViewId);
                if (root != null) {
                    int resolvedViewId = root.getContext().getResources().getIdentifier(viewId, null, null);
                    if (resolvedViewId <= 0) {
                        flags = infos;
                        updateInfosForViewportAndReturnFindNodeResult(infos, callback, interactionId, spec, interactiveRegion);
                        return;
                    }
                    int resolvedViewId2 = resolvedViewId;
                    i = flags;
                    flags2 = infos;
                    try {
                        if (this.mAddNodeInfosForViewId == null) {
                            this.mAddNodeInfosForViewId = new AddNodeInfosForViewId();
                        }
                        this.mAddNodeInfosForViewId.init(resolvedViewId2, flags2);
                        root.findViewByPredicate(this.mAddNodeInfosForViewId);
                        this.mAddNodeInfosForViewId.reset();
                    } catch (Throwable th2) {
                        th = th2;
                        updateInfosForViewportAndReturnFindNodeResult(flags2, callback, interactionId, spec, interactiveRegion);
                        throw th;
                    }
                }
                flags = infos;
                updateInfosForViewportAndReturnFindNodeResult(flags, callback, interactionId, spec, interactiveRegion);
                return;
            }
            updateInfosForViewportAndReturnFindNodeResult(flags, callback, interactionId, spec, interactiveRegion);
        } catch (Throwable th3) {
            th = th3;
            i = flags;
            flags2 = infos;
            updateInfosForViewportAndReturnFindNodeResult(flags2, callback, interactionId, spec, interactiveRegion);
            throw th;
        }
    }

    public void findAccessibilityNodeInfosByTextClientThread(long accessibilityNodeId, String text, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
        Message message = this.mHandler.obtainMessage();
        message.what = 4;
        message.arg1 = flags;
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = text;
        args.arg2 = callback;
        args.arg3 = spec;
        args.argi1 = AccessibilityNodeInfo.getAccessibilityViewId(accessibilityNodeId);
        args.argi2 = AccessibilityNodeInfo.getVirtualDescendantId(accessibilityNodeId);
        args.argi3 = interactionId;
        args.arg4 = interactiveRegion;
        message.obj = args;
        scheduleMessage(message, interrogatingPid, interrogatingTid, false);
    }

    private void findAccessibilityNodeInfosByTextUiThread(Message message) {
        int interactionId;
        Throwable th;
        Message message2 = message;
        int flags = message2.arg1;
        SomeArgs args = message2.obj;
        String text = args.arg1;
        IAccessibilityInteractionConnectionCallback callback = args.arg2;
        MagnificationSpec spec = args.arg3;
        int accessibilityViewId = args.argi1;
        int virtualDescendantId = args.argi2;
        int interactionId2 = args.argi3;
        Region interactiveRegion = args.arg4;
        args.recycle();
        List<AccessibilityNodeInfo> infos = null;
        try {
            if (this.mViewRootImpl.mView == null) {
                interactionId = interactionId2;
            } else if (this.mViewRootImpl.mAttachInfo == null) {
                interactionId = interactionId2;
            } else {
                View view;
                this.mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = flags;
                View root = findViewByAccessibilityId(accessibilityViewId);
                if (root != null) {
                    try {
                        if (isShown(root)) {
                            AccessibilityNodeProvider provider = root.getAccessibilityNodeProvider();
                            if (provider != null) {
                                infos = provider.findAccessibilityNodeInfosByText(text, virtualDescendantId);
                            } else if (virtualDescendantId == -1) {
                                ArrayList<View> foundViews = this.mTempArrayList;
                                foundViews.clear();
                                root.findViewsWithText(foundViews, text, 7);
                                ArrayList<View> arrayList;
                                if (foundViews.isEmpty()) {
                                    view = root;
                                    arrayList = foundViews;
                                } else {
                                    List<AccessibilityNodeInfo> infos2 = this.mTempAccessibilityNodeInfoList;
                                    try {
                                        infos2.clear();
                                        int viewCount = foundViews.size();
                                        provider = null;
                                        while (provider < viewCount) {
                                            View foundView = (View) foundViews.get(provider);
                                            if (isShown(foundView)) {
                                                view = root;
                                                AccessibilityNodeProvider root2 = foundView.getAccessibilityNodeProvider();
                                                if (root2 != null) {
                                                    arrayList = foundViews;
                                                    List<AccessibilityNodeInfo> foundViews2 = root2.findAccessibilityNodeInfosByText(text, -1);
                                                    if (foundViews2 != null) {
                                                        infos2.addAll(foundViews2);
                                                    }
                                                } else {
                                                    arrayList = foundViews;
                                                    infos2.add(foundView.createAccessibilityNodeInfo());
                                                }
                                                AccessibilityNodeProvider accessibilityNodeProvider = root2;
                                            } else {
                                                view = root;
                                                arrayList = foundViews;
                                            }
                                            provider++;
                                            root = view;
                                            foundViews = arrayList;
                                        }
                                        arrayList = foundViews;
                                        infos = infos2;
                                    } catch (Throwable th2) {
                                        th = th2;
                                        infos = infos2;
                                        interactionId = interactionId2;
                                        updateInfosForViewportAndReturnFindNodeResult(infos, callback, interactionId, spec, interactiveRegion);
                                        throw th;
                                    }
                                }
                            }
                            updateInfosForViewportAndReturnFindNodeResult(infos, callback, interactionId2, spec, interactiveRegion);
                            return;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        interactionId = interactionId2;
                        updateInfosForViewportAndReturnFindNodeResult(infos, callback, interactionId, spec, interactiveRegion);
                        throw th;
                    }
                }
                view = root;
                updateInfosForViewportAndReturnFindNodeResult(infos, callback, interactionId2, spec, interactiveRegion);
                return;
            }
            updateInfosForViewportAndReturnFindNodeResult(null, callback, interactionId, spec, interactiveRegion);
        } catch (Throwable th4) {
            th = th4;
            interactionId = interactionId2;
            updateInfosForViewportAndReturnFindNodeResult(infos, callback, interactionId, spec, interactiveRegion);
            throw th;
        }
    }

    public void findFocusClientThread(long accessibilityNodeId, int focusType, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
        Message message = this.mHandler.obtainMessage();
        message.what = 5;
        message.arg1 = flags;
        message.arg2 = focusType;
        SomeArgs args = SomeArgs.obtain();
        args.argi1 = interactionId;
        args.argi2 = AccessibilityNodeInfo.getAccessibilityViewId(accessibilityNodeId);
        args.argi3 = AccessibilityNodeInfo.getVirtualDescendantId(accessibilityNodeId);
        args.arg1 = callback;
        args.arg2 = spec;
        args.arg3 = interactiveRegion;
        message.obj = args;
        scheduleMessage(message, interrogatingPid, interrogatingTid, false);
    }

    private void findFocusUiThread(Message message) {
        Message message2 = message;
        int flags = message2.arg1;
        int focusType = message2.arg2;
        SomeArgs args = message2.obj;
        int interactionId = args.argi1;
        int accessibilityViewId = args.argi2;
        int virtualDescendantId = args.argi3;
        IAccessibilityInteractionConnectionCallback callback = args.arg1;
        MagnificationSpec spec = args.arg2;
        Region interactiveRegion = args.arg3;
        args.recycle();
        AccessibilityNodeInfo focused = null;
        try {
            if (this.mViewRootImpl.mView != null) {
                if (this.mViewRootImpl.mAttachInfo != null) {
                    this.mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = flags;
                    View root = findViewByAccessibilityId(accessibilityViewId);
                    if (root != null && isShown(root)) {
                        View target;
                        if (focusType == 1) {
                            target = root.findFocus();
                            if (isShown(target)) {
                                AccessibilityNodeProvider provider = target.getAccessibilityNodeProvider();
                                if (provider != null) {
                                    focused = provider.findFocus(focusType);
                                }
                                if (focused == null) {
                                    focused = target.createAccessibilityNodeInfo();
                                }
                            }
                        } else if (focusType == 2) {
                            target = this.mViewRootImpl.mAccessibilityFocusedHost;
                            if (target != null) {
                                if (ViewRootImpl.isViewDescendantOf(target, root)) {
                                    if (isShown(target)) {
                                        if (target.getAccessibilityNodeProvider() != null) {
                                            if (this.mViewRootImpl.mAccessibilityFocusedVirtualView != null) {
                                                focused = AccessibilityNodeInfo.obtain(this.mViewRootImpl.mAccessibilityFocusedVirtualView);
                                            }
                                        } else if (virtualDescendantId == -1) {
                                            focused = target.createAccessibilityNodeInfo();
                                        }
                                    }
                                }
                            }
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Unknown focus type: ");
                            stringBuilder.append(focusType);
                            throw new IllegalArgumentException(stringBuilder.toString());
                        }
                    }
                    updateInfoForViewportAndReturnFindNodeResult(focused, callback, interactionId, spec, interactiveRegion);
                    return;
                }
            }
            updateInfoForViewportAndReturnFindNodeResult(null, callback, interactionId, spec, interactiveRegion);
        } catch (Throwable th) {
            updateInfoForViewportAndReturnFindNodeResult(null, callback, interactionId, spec, interactiveRegion);
        }
    }

    public void focusSearchClientThread(long accessibilityNodeId, int direction, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
        Message message = this.mHandler.obtainMessage();
        message.what = 6;
        message.arg1 = flags;
        message.arg2 = AccessibilityNodeInfo.getAccessibilityViewId(accessibilityNodeId);
        SomeArgs args = SomeArgs.obtain();
        args.argi2 = direction;
        args.argi3 = interactionId;
        args.arg1 = callback;
        args.arg2 = spec;
        args.arg3 = interactiveRegion;
        message.obj = args;
        scheduleMessage(message, interrogatingPid, interrogatingTid, false);
    }

    private void focusSearchUiThread(Message message) {
        Message message2 = message;
        int flags = message2.arg1;
        int accessibilityViewId = message2.arg2;
        SomeArgs args = message2.obj;
        int direction = args.argi2;
        int interactionId = args.argi3;
        IAccessibilityInteractionConnectionCallback callback = args.arg1;
        MagnificationSpec spec = args.arg2;
        Region interactiveRegion = args.arg3;
        args.recycle();
        AccessibilityNodeInfo next = null;
        try {
            if (this.mViewRootImpl.mView != null) {
                if (this.mViewRootImpl.mAttachInfo != null) {
                    this.mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = flags;
                    View root = findViewByAccessibilityId(accessibilityViewId);
                    if (root != null && isShown(root)) {
                        View nextView = root.focusSearch(direction);
                        if (nextView != null) {
                            next = nextView.createAccessibilityNodeInfo();
                        }
                    }
                    updateInfoForViewportAndReturnFindNodeResult(next, callback, interactionId, spec, interactiveRegion);
                    return;
                }
            }
            updateInfoForViewportAndReturnFindNodeResult(null, callback, interactionId, spec, interactiveRegion);
        } catch (Throwable th) {
            updateInfoForViewportAndReturnFindNodeResult(null, callback, interactionId, spec, interactiveRegion);
        }
    }

    public void performAccessibilityActionClientThread(long accessibilityNodeId, int action, Bundle arguments, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) {
        Message message = this.mHandler.obtainMessage();
        message.what = 1;
        message.arg1 = flags;
        message.arg2 = AccessibilityNodeInfo.getAccessibilityViewId(accessibilityNodeId);
        SomeArgs args = SomeArgs.obtain();
        args.argi1 = AccessibilityNodeInfo.getVirtualDescendantId(accessibilityNodeId);
        args.argi2 = action;
        args.argi3 = interactionId;
        args.arg1 = callback;
        args.arg2 = arguments;
        message.obj = args;
        scheduleMessage(message, interrogatingPid, interrogatingTid, false);
    }

    private void performAccessibilityActionUiThread(Message message) {
        int flags = message.arg1;
        int accessibilityViewId = message.arg2;
        SomeArgs args = message.obj;
        int virtualDescendantId = args.argi1;
        int action = args.argi2;
        int interactionId = args.argi3;
        IAccessibilityInteractionConnectionCallback callback = args.arg1;
        Bundle arguments = args.arg2;
        args.recycle();
        boolean succeeded = false;
        try {
            if (!(this.mViewRootImpl.mView == null || this.mViewRootImpl.mAttachInfo == null || this.mViewRootImpl.mStopped)) {
                if (!this.mViewRootImpl.mPausedForTransition) {
                    this.mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = flags;
                    View target = findViewByAccessibilityId(accessibilityViewId);
                    if (target != null && isShown(target)) {
                        if (action == R.id.accessibilityActionClickOnClickableSpan) {
                            succeeded = handleClickableSpanActionUiThread(target, virtualDescendantId, arguments);
                        } else {
                            AccessibilityNodeProvider provider = target.getAccessibilityNodeProvider();
                            if (provider != null) {
                                succeeded = provider.performAction(virtualDescendantId, action, arguments);
                            } else if (virtualDescendantId == -1) {
                                succeeded = target.performAccessibilityAction(action, arguments);
                            }
                        }
                    }
                    try {
                        this.mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = 0;
                        callback.setPerformAccessibilityActionResult(succeeded, interactionId);
                    } catch (RemoteException e) {
                    }
                }
            }
        } finally {
            try {
                this.mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = 0;
                callback.setPerformAccessibilityActionResult(false, interactionId);
            } catch (RemoteException e2) {
            }
        }
    }

    public void clearAccessibilityFocusClientThread() {
        Message message = this.mHandler.obtainMessage();
        message.what = 101;
        scheduleMessage(message, 0, 0, false);
    }

    private void clearAccessibilityFocusUiThread() {
        if (this.mViewRootImpl.mView != null && this.mViewRootImpl.mAttachInfo != null) {
            try {
                this.mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = 8;
                View root = this.mViewRootImpl.mView;
                if (root != null && isShown(root)) {
                    View host = this.mViewRootImpl.mAccessibilityFocusedHost;
                    if (host != null) {
                        if (ViewRootImpl.isViewDescendantOf(host, root)) {
                            AccessibilityNodeProvider provider = host.getAccessibilityNodeProvider();
                            AccessibilityNodeInfo focusNode = this.mViewRootImpl.mAccessibilityFocusedVirtualView;
                            if (provider == null || focusNode == null) {
                                host.performAccessibilityAction(AccessibilityAction.ACTION_CLEAR_ACCESSIBILITY_FOCUS.getId(), null);
                            } else {
                                provider.performAction(AccessibilityNodeInfo.getVirtualDescendantId(focusNode.getSourceNodeId()), AccessibilityAction.ACTION_CLEAR_ACCESSIBILITY_FOCUS.getId(), null);
                            }
                        }
                    }
                    this.mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = 0;
                    return;
                }
                this.mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = 0;
            } catch (Throwable th) {
                this.mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = 0;
            }
        }
    }

    public void notifyOutsideTouchClientThread() {
        Message message = this.mHandler.obtainMessage();
        message.what = 102;
        scheduleMessage(message, 0, 0, false);
    }

    private void notifyOutsideTouchUiThread() {
        if (this.mViewRootImpl.mView != null && this.mViewRootImpl.mAttachInfo != null && !this.mViewRootImpl.mStopped && !this.mViewRootImpl.mPausedForTransition) {
            View root = this.mViewRootImpl.mView;
            if (root != null && isShown(root)) {
                long now = SystemClock.uptimeMillis();
                MotionEvent event = MotionEvent.obtain(now, now, 4, 0.0f, 0.0f, 0);
                event.setSource(4098);
                this.mViewRootImpl.dispatchInputEvent(event);
            }
        }
    }

    private View findViewByAccessibilityId(int accessibilityId) {
        if (accessibilityId == 2147483646) {
            return this.mViewRootImpl.mView;
        }
        return AccessibilityNodeIdManager.getInstance().findView(accessibilityId);
    }

    private void applyAppScaleAndMagnificationSpecIfNeeded(List<AccessibilityNodeInfo> infos, MagnificationSpec spec) {
        if (infos != null && shouldApplyAppScaleAndMagnificationSpec(this.mViewRootImpl.mAttachInfo.mApplicationScale, spec)) {
            int infoCount = infos.size();
            for (int i = 0; i < infoCount; i++) {
                applyAppScaleAndMagnificationSpecIfNeeded((AccessibilityNodeInfo) infos.get(i), spec);
            }
        }
    }

    private void adjustIsVisibleToUserIfNeeded(List<AccessibilityNodeInfo> infos, Region interactiveRegion) {
        if (interactiveRegion != null && infos != null) {
            int infoCount = infos.size();
            for (int i = 0; i < infoCount; i++) {
                adjustIsVisibleToUserIfNeeded((AccessibilityNodeInfo) infos.get(i), interactiveRegion);
            }
        }
    }

    private void adjustIsVisibleToUserIfNeeded(AccessibilityNodeInfo info, Region interactiveRegion) {
        if (interactiveRegion != null && info != null) {
            Rect boundsInScreen = this.mTempRect;
            info.getBoundsInScreen(boundsInScreen);
            if (interactiveRegion.quickReject(boundsInScreen) && !shouldBypassAdjustIsVisible()) {
                info.setVisibleToUser(false);
            }
        }
    }

    private boolean shouldBypassAdjustIsVisible() {
        if (this.mViewRootImpl.mOrigWindowType == 2011) {
            return true;
        }
        return false;
    }

    private void applyAppScaleAndMagnificationSpecIfNeeded(AccessibilityNodeInfo info, MagnificationSpec spec) {
        AccessibilityNodeInfo accessibilityNodeInfo = info;
        MagnificationSpec magnificationSpec = spec;
        if (accessibilityNodeInfo != null) {
            float applicationScale = this.mViewRootImpl.mAttachInfo.mApplicationScale;
            if (shouldApplyAppScaleAndMagnificationSpec(applicationScale, magnificationSpec)) {
                Rect boundsInParent = this.mTempRect;
                Rect boundsInScreen = this.mTempRect1;
                accessibilityNodeInfo.getBoundsInParent(boundsInParent);
                accessibilityNodeInfo.getBoundsInScreen(boundsInScreen);
                if (applicationScale != 1.0f) {
                    boundsInParent.scale(applicationScale);
                    boundsInScreen.scale(applicationScale);
                }
                if (magnificationSpec != null) {
                    boundsInParent.scale(magnificationSpec.scale);
                    boundsInScreen.scale(magnificationSpec.scale);
                    boundsInScreen.offset((int) magnificationSpec.offsetX, (int) magnificationSpec.offsetY);
                }
                accessibilityNodeInfo.setBoundsInParent(boundsInParent);
                accessibilityNodeInfo.setBoundsInScreen(boundsInScreen);
                if (info.hasExtras()) {
                    Parcelable[] textLocations = info.getExtras().getParcelableArray(AccessibilityNodeInfo.EXTRA_DATA_TEXT_CHARACTER_LOCATION_KEY);
                    if (textLocations != null) {
                        for (RectF textLocation : textLocations) {
                            textLocation.scale(applicationScale);
                            if (magnificationSpec != null) {
                                textLocation.scale(magnificationSpec.scale);
                                textLocation.offset(magnificationSpec.offsetX, magnificationSpec.offsetY);
                            }
                        }
                    }
                }
                if (magnificationSpec != null) {
                    AttachInfo attachInfo = this.mViewRootImpl.mAttachInfo;
                    if (attachInfo.mDisplay != null) {
                        float scale = attachInfo.mApplicationScale * magnificationSpec.scale;
                        Rect visibleWinFrame = this.mTempRect1;
                        visibleWinFrame.left = (int) ((((float) attachInfo.mWindowLeft) * scale) + magnificationSpec.offsetX);
                        visibleWinFrame.top = (int) ((((float) attachInfo.mWindowTop) * scale) + magnificationSpec.offsetY);
                        visibleWinFrame.right = (int) (((float) visibleWinFrame.left) + (((float) this.mViewRootImpl.mWidth) * scale));
                        visibleWinFrame.bottom = (int) (((float) visibleWinFrame.top) + (((float) this.mViewRootImpl.mHeight) * scale));
                        attachInfo.mDisplay.getRealSize(this.mTempPoint);
                        int displayWidth = this.mTempPoint.x;
                        int displayHeight = this.mTempPoint.y;
                        Rect visibleDisplayFrame = this.mTempRect2;
                        visibleDisplayFrame.set(0, 0, displayWidth, displayHeight);
                        if (!visibleWinFrame.intersect(visibleDisplayFrame)) {
                            visibleDisplayFrame.setEmpty();
                        }
                        if (!visibleWinFrame.intersects(boundsInScreen.left, boundsInScreen.top, boundsInScreen.right, boundsInScreen.bottom)) {
                            accessibilityNodeInfo.setVisibleToUser(false);
                        }
                    }
                }
            }
        }
    }

    private boolean shouldApplyAppScaleAndMagnificationSpec(float appScale, MagnificationSpec spec) {
        return (appScale == 1.0f && (spec == null || spec.isNop())) ? false : true;
    }

    private void updateInfosForViewportAndReturnFindNodeResult(List<AccessibilityNodeInfo> infos, IAccessibilityInteractionConnectionCallback callback, int interactionId, MagnificationSpec spec, Region interactiveRegion) {
        try {
            this.mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = 0;
            applyAppScaleAndMagnificationSpecIfNeeded((List) infos, spec);
            adjustIsVisibleToUserIfNeeded((List) infos, interactiveRegion);
            callback.setFindAccessibilityNodeInfosResult(infos, interactionId);
            if (infos != null) {
                infos.clear();
            }
        } catch (RemoteException e) {
        } catch (Throwable th) {
            recycleMagnificationSpecAndRegionIfNeeded(spec, interactiveRegion);
        }
        recycleMagnificationSpecAndRegionIfNeeded(spec, interactiveRegion);
    }

    private void updateInfoForViewportAndReturnFindNodeResult(AccessibilityNodeInfo info, IAccessibilityInteractionConnectionCallback callback, int interactionId, MagnificationSpec spec, Region interactiveRegion) {
        try {
            this.mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = 0;
            applyAppScaleAndMagnificationSpecIfNeeded(info, spec);
            adjustIsVisibleToUserIfNeeded(info, interactiveRegion);
            callback.setFindAccessibilityNodeInfoResult(info, interactionId);
        } catch (RemoteException e) {
        } catch (Throwable th) {
            recycleMagnificationSpecAndRegionIfNeeded(spec, interactiveRegion);
        }
        recycleMagnificationSpecAndRegionIfNeeded(spec, interactiveRegion);
    }

    private void recycleMagnificationSpecAndRegionIfNeeded(MagnificationSpec spec, Region region) {
        if (Process.myPid() != Binder.getCallingPid()) {
            if (spec != null) {
                spec.recycle();
            }
        } else if (region != null) {
            region.recycle();
        }
    }

    private boolean handleClickableSpanActionUiThread(View view, int virtualDescendantId, Bundle arguments) {
        Parcelable span = arguments.getParcelable(AccessibilityNodeInfo.ACTION_ARGUMENT_ACCESSIBLE_CLICKABLE_SPAN);
        if (!(span instanceof AccessibilityClickableSpan)) {
            return false;
        }
        AccessibilityNodeInfo infoWithSpan = null;
        AccessibilityNodeProvider provider = view.getAccessibilityNodeProvider();
        if (provider != null) {
            infoWithSpan = provider.createAccessibilityNodeInfo(virtualDescendantId);
        } else if (virtualDescendantId == -1) {
            infoWithSpan = view.createAccessibilityNodeInfo();
        }
        if (infoWithSpan == null) {
            return false;
        }
        ClickableSpan clickableSpan = ((AccessibilityClickableSpan) span).findClickableSpan(infoWithSpan.getOriginalText());
        if (clickableSpan == null) {
            return false;
        }
        clickableSpan.onClick(view);
        return true;
    }
}
