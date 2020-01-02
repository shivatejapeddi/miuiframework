package android.view.autofill;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SystemApi;
import android.content.AutofillOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.graphics.Rect;
import android.metrics.LogMaker;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.service.autofill.FillEventHistory;
import android.service.autofill.UserData;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.view.Choreographer;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityManager.AccessibilityPolicy;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.autofill.IAugmentedAutofillManagerClient.Stub;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.os.IResultReceiver;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import com.android.internal.util.SyncResultReceiver;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;
import sun.misc.Cleaner;

public final class AutofillManager {
    public static final int ACTION_START_SESSION = 1;
    public static final int ACTION_VALUE_CHANGED = 4;
    public static final int ACTION_VIEW_ENTERED = 2;
    public static final int ACTION_VIEW_EXITED = 3;
    private static final int AUTHENTICATION_ID_DATASET_ID_MASK = 65535;
    private static final int AUTHENTICATION_ID_DATASET_ID_SHIFT = 16;
    public static final int AUTHENTICATION_ID_DATASET_ID_UNDEFINED = 65535;
    public static final int DEFAULT_LOGGING_LEVEL;
    public static final int DEFAULT_MAX_PARTITIONS_SIZE = 10;
    public static final String DEVICE_CONFIG_AUGMENTED_SERVICE_IDLE_UNBIND_TIMEOUT = "augmented_service_idle_unbind_timeout";
    public static final String DEVICE_CONFIG_AUGMENTED_SERVICE_REQUEST_TIMEOUT = "augmented_service_request_timeout";
    public static final String DEVICE_CONFIG_AUTOFILL_SMART_SUGGESTION_SUPPORTED_MODES = "smart_suggestion_supported_modes";
    public static final String EXTRA_ASSIST_STRUCTURE = "android.view.autofill.extra.ASSIST_STRUCTURE";
    public static final String EXTRA_AUGMENTED_AUTOFILL_CLIENT = "android.view.autofill.extra.AUGMENTED_AUTOFILL_CLIENT";
    public static final String EXTRA_AUTHENTICATION_RESULT = "android.view.autofill.extra.AUTHENTICATION_RESULT";
    public static final String EXTRA_CLIENT_STATE = "android.view.autofill.extra.CLIENT_STATE";
    public static final String EXTRA_RESTORE_SESSION_TOKEN = "android.view.autofill.extra.RESTORE_SESSION_TOKEN";
    public static final int FC_SERVICE_TIMEOUT = 5000;
    public static final int FLAG_ADD_CLIENT_DEBUG = 2;
    public static final int FLAG_ADD_CLIENT_ENABLED = 1;
    public static final int FLAG_ADD_CLIENT_ENABLED_FOR_AUGMENTED_AUTOFILL_ONLY = 8;
    public static final int FLAG_ADD_CLIENT_VERBOSE = 4;
    public static final int FLAG_SMART_SUGGESTION_OFF = 0;
    public static final int FLAG_SMART_SUGGESTION_SYSTEM = 1;
    private static final String LAST_AUTOFILLED_DATA_TAG = "android:lastAutoFilledData";
    public static final int MAX_TEMP_AUGMENTED_SERVICE_DURATION_MS = 120000;
    public static final int NO_LOGGING = 0;
    public static final int NO_SESSION = Integer.MAX_VALUE;
    public static final int PENDING_UI_OPERATION_CANCEL = 1;
    public static final int PENDING_UI_OPERATION_RESTORE = 2;
    public static final int RECEIVER_FLAG_SESSION_FOR_AUGMENTED_AUTOFILL_ONLY = 1;
    public static final int RESULT_CODE_NOT_SERVICE = -1;
    public static final int RESULT_OK = 0;
    private static final String SESSION_ID_TAG = "android:sessionId";
    public static final int SET_STATE_FLAG_DEBUG = 8;
    public static final int SET_STATE_FLAG_ENABLED = 1;
    public static final int SET_STATE_FLAG_FOR_AUTOFILL_ONLY = 32;
    public static final int SET_STATE_FLAG_RESET_CLIENT = 4;
    public static final int SET_STATE_FLAG_RESET_SESSION = 2;
    public static final int SET_STATE_FLAG_VERBOSE = 16;
    public static final int STATE_ACTIVE = 1;
    public static final int STATE_DISABLED_BY_SERVICE = 4;
    public static final int STATE_FINISHED = 2;
    public static final int STATE_SHOWING_SAVE_UI = 3;
    private static final String STATE_TAG = "android:state";
    public static final int STATE_UNKNOWN = 0;
    public static final int STATE_UNKNOWN_COMPAT_MODE = 5;
    public static final int STATE_UNKNOWN_FAILED = 6;
    private static final int SYNC_CALLS_TIMEOUT_MS = 5000;
    private static final String TAG = "AutofillManager";
    @GuardedBy({"mLock"})
    private IAugmentedAutofillManagerClient mAugmentedAutofillServiceClient;
    @GuardedBy({"mLock"})
    private AutofillCallback mCallback;
    @GuardedBy({"mLock"})
    private CompatibilityBridge mCompatibilityBridge;
    private final Context mContext;
    @GuardedBy({"mLock"})
    private boolean mEnabled;
    @GuardedBy({"mLock"})
    private boolean mEnabledForAugmentedAutofillOnly;
    @GuardedBy({"mLock"})
    private Set<AutofillId> mEnteredForAugmentedAutofillIds;
    @GuardedBy({"mLock"})
    private ArraySet<AutofillId> mEnteredIds;
    @GuardedBy({"mLock"})
    private ArraySet<AutofillId> mFillableIds;
    @GuardedBy({"mLock"})
    private boolean mForAugmentedAutofillOnly;
    private AutofillId mIdShownFillUi;
    @GuardedBy({"mLock"})
    private ParcelableMap mLastAutofilledData;
    private final Object mLock = new Object();
    private final MetricsLogger mMetricsLogger = new MetricsLogger();
    @GuardedBy({"mLock"})
    private boolean mOnInvisibleCalled;
    private final AutofillOptions mOptions;
    @GuardedBy({"mLock"})
    private boolean mSaveOnFinish;
    @GuardedBy({"mLock"})
    private AutofillId mSaveTriggerId;
    private final IAutoFillManager mService;
    @GuardedBy({"mLock"})
    private IAutoFillManagerClient mServiceClient;
    @GuardedBy({"mLock"})
    private Cleaner mServiceClientCleaner;
    @GuardedBy({"mLock"})
    private int mSessionId = Integer.MAX_VALUE;
    @GuardedBy({"mLock"})
    private int mState;
    @GuardedBy({"mLock"})
    private TrackedViews mTrackedViews;

    private static final class AugmentedAutofillManagerClient extends Stub {
        private final WeakReference<AutofillManager> mAfm;

        private AugmentedAutofillManagerClient(AutofillManager autofillManager) {
            this.mAfm = new WeakReference(autofillManager);
        }

        public Rect getViewCoordinates(AutofillId id) {
            AutofillManager afm = (AutofillManager) this.mAfm.get();
            if (afm == null) {
                return null;
            }
            AutofillClient client = afm.getClient();
            String str = "getViewCoordinates(";
            String str2 = AutofillManager.TAG;
            if (client == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(id);
                stringBuilder.append("): no autofill client");
                Log.w(str2, stringBuilder.toString());
                return null;
            }
            View view = client.autofillClientFindViewByAutofillIdTraversal(id);
            if (view == null) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(id);
                stringBuilder2.append("): could not find view");
                Log.w(str2, stringBuilder2.toString());
                return null;
            }
            Rect windowVisibleDisplayFrame = new Rect();
            view.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            Rect rect = new Rect(location[0], location[1] - windowVisibleDisplayFrame.top, location[0] + view.getWidth(), (location[1] - windowVisibleDisplayFrame.top) + view.getHeight());
            if (Helper.sVerbose) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Coordinates for ");
                stringBuilder3.append(id);
                stringBuilder3.append(": ");
                stringBuilder3.append(rect);
                Log.v(str2, stringBuilder3.toString());
            }
            return rect;
        }

        public void autofill(int sessionId, List<AutofillId> ids, List<AutofillValue> values) {
            AutofillManager afm = (AutofillManager) this.mAfm.get();
            if (afm != null) {
                afm.post(new -$$Lambda$AutofillManager$AugmentedAutofillManagerClient$k-qssZkEBwVEPdSmrHGsi2QT-3Y(afm, sessionId, ids, values));
            }
        }

        public void requestShowFillUi(int sessionId, AutofillId id, int width, int height, Rect anchorBounds, IAutofillWindowPresenter presenter) {
            AutofillManager afm = (AutofillManager) this.mAfm.get();
            if (afm != null) {
                afm.post(new -$$Lambda$AutofillManager$AugmentedAutofillManagerClient$OrAY5q15e0VwuCSYnsGgs6GcY1U(afm, sessionId, id, width, height, anchorBounds, presenter));
            }
        }

        public void requestHideFillUi(int sessionId, AutofillId id) {
            AutofillManager afm = (AutofillManager) this.mAfm.get();
            if (afm != null) {
                afm.post(new -$$Lambda$AutofillManager$AugmentedAutofillManagerClient$tbNtqpHgXnRdc3JO5HaBlxclFg0(afm, id));
            }
        }
    }

    public static abstract class AutofillCallback {
        public static final int EVENT_INPUT_HIDDEN = 2;
        public static final int EVENT_INPUT_SHOWN = 1;
        public static final int EVENT_INPUT_UNAVAILABLE = 3;

        @Retention(RetentionPolicy.SOURCE)
        public @interface AutofillEventType {
        }

        public void onAutofillEvent(View view, int event) {
        }

        public void onAutofillEvent(View view, int virtualId, int event) {
        }
    }

    public interface AutofillClient {
        void autofillClientAuthenticate(int i, IntentSender intentSender, Intent intent);

        void autofillClientDispatchUnhandledKey(View view, KeyEvent keyEvent);

        View autofillClientFindViewByAccessibilityIdTraversal(int i, int i2);

        View autofillClientFindViewByAutofillIdTraversal(AutofillId autofillId);

        View[] autofillClientFindViewsByAutofillIdTraversal(AutofillId[] autofillIdArr);

        IBinder autofillClientGetActivityToken();

        ComponentName autofillClientGetComponentName();

        AutofillId autofillClientGetNextAutofillId();

        boolean[] autofillClientGetViewVisibility(AutofillId[] autofillIdArr);

        boolean autofillClientIsCompatibilityModeEnabled();

        boolean autofillClientIsFillUiShowing();

        boolean autofillClientIsVisibleForAutofill();

        boolean autofillClientRequestHideFillUi();

        boolean autofillClientRequestShowFillUi(View view, int i, int i2, Rect rect, IAutofillWindowPresenter iAutofillWindowPresenter);

        void autofillClientResetableStateAvailable();

        void autofillClientRunOnUiThread(Runnable runnable);

        boolean isDisablingEnterExitEventForAutofill();
    }

    private static final class AutofillManagerClient extends IAutoFillManagerClient.Stub {
        private final WeakReference<AutofillManager> mAfm;

        private AutofillManagerClient(AutofillManager autofillManager) {
            this.mAfm = new WeakReference(autofillManager);
        }

        public void setState(int flags) {
            AutofillManager afm = (AutofillManager) this.mAfm.get();
            if (afm != null) {
                afm.post(new -$$Lambda$AutofillManager$AutofillManagerClient$qH36EJk2Hkdja9ZZmTxqYPyr0YA(afm, flags));
            }
        }

        public void autofill(int sessionId, List<AutofillId> ids, List<AutofillValue> values) {
            AutofillManager afm = (AutofillManager) this.mAfm.get();
            if (afm != null) {
                afm.post(new -$$Lambda$AutofillManager$AutofillManagerClient$1jAzMluMSJksx55SMUQn4BKB2Ng(afm, sessionId, ids, values));
            }
        }

        public void authenticate(int sessionId, int authenticationId, IntentSender intent, Intent fillInIntent) {
            AutofillManager afm = (AutofillManager) this.mAfm.get();
            if (afm != null) {
                afm.post(new -$$Lambda$AutofillManager$AutofillManagerClient$qyxZ4PACUgHFGSvMBHzgwjJ3yns(afm, sessionId, authenticationId, intent, fillInIntent));
            }
        }

        public void requestShowFillUi(int sessionId, AutofillId id, int width, int height, Rect anchorBounds, IAutofillWindowPresenter presenter) {
            AutofillManager afm = (AutofillManager) this.mAfm.get();
            if (afm != null) {
                afm.post(new -$$Lambda$AutofillManager$AutofillManagerClient$kRL9XILLc2XNr90gxVDACLzcyqc(afm, sessionId, id, width, height, anchorBounds, presenter));
            }
        }

        public void requestHideFillUi(int sessionId, AutofillId id) {
            AutofillManager afm = (AutofillManager) this.mAfm.get();
            if (afm != null) {
                afm.post(new -$$Lambda$AutofillManager$AutofillManagerClient$dCTetwfU0gT1ZrSzZGZiGStXlOY(afm, id));
            }
        }

        public void notifyNoFillUi(int sessionId, AutofillId id, int sessionFinishedState) {
            AutofillManager afm = (AutofillManager) this.mAfm.get();
            if (afm != null) {
                afm.post(new -$$Lambda$AutofillManager$AutofillManagerClient$K79QnIPRaZuikYDQdsLcIUBhqiI(afm, sessionId, id, sessionFinishedState));
            }
        }

        public void dispatchUnhandledKey(int sessionId, AutofillId id, KeyEvent fullScreen) {
            AutofillManager afm = (AutofillManager) this.mAfm.get();
            if (afm != null) {
                afm.post(new -$$Lambda$AutofillManager$AutofillManagerClient$xqXjXW0fvc8JdYR5fgGKw9lJc3I(afm, sessionId, id, fullScreen));
            }
        }

        public void startIntentSender(IntentSender intentSender, Intent intent) {
            AutofillManager afm = (AutofillManager) this.mAfm.get();
            if (afm != null) {
                afm.post(new -$$Lambda$AutofillManager$AutofillManagerClient$pM5e3ez5KTBdZt4d8qLEERBUSiU(afm, intentSender, intent));
            }
        }

        static /* synthetic */ void lambda$startIntentSender$7(AutofillManager afm, IntentSender intentSender, Intent intent) {
            try {
                afm.mContext.startIntentSender(intentSender, intent, 0, 0, 0);
            } catch (SendIntentException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("startIntentSender() failed for intent:");
                stringBuilder.append(intentSender);
                Log.e(AutofillManager.TAG, stringBuilder.toString(), e);
            }
        }

        public void setTrackedViews(int sessionId, AutofillId[] ids, boolean saveOnAllViewsInvisible, boolean saveOnFinish, AutofillId[] fillableIds, AutofillId saveTriggerId) {
            AutofillManager afm = (AutofillManager) this.mAfm.get();
            if (afm != null) {
                afm.post(new -$$Lambda$AutofillManager$AutofillManagerClient$BPlC2x7GLNHFS92rPUSzbcpFhUc(afm, sessionId, ids, saveOnAllViewsInvisible, saveOnFinish, fillableIds, saveTriggerId));
            }
        }

        public void setSaveUiState(int sessionId, boolean shown) {
            AutofillManager afm = (AutofillManager) this.mAfm.get();
            if (afm != null) {
                afm.post(new -$$Lambda$AutofillManager$AutofillManagerClient$QIW-100CKwHzdHffwaus9KOEHCA(afm, sessionId, shown));
            }
        }

        public void setSessionFinished(int newState, List<AutofillId> autofillableIds) {
            AutofillManager afm = (AutofillManager) this.mAfm.get();
            if (afm != null) {
                afm.post(new -$$Lambda$AutofillManager$AutofillManagerClient$-IhPS_W7AwZ4M9TKIIFigmQd5SE(afm, newState, autofillableIds));
            }
        }

        public void getAugmentedAutofillClient(IResultReceiver result) {
            AutofillManager afm = (AutofillManager) this.mAfm.get();
            if (afm != null) {
                afm.post(new -$$Lambda$AutofillManager$AutofillManagerClient$eeFWMGoPtaXdpslR3NLvhgXvMMs(afm, result));
            }
        }
    }

    private final class CompatibilityBridge implements AccessibilityPolicy {
        @GuardedBy({"mLock"})
        AccessibilityServiceInfo mCompatServiceInfo;
        @GuardedBy({"mLock"})
        private final Rect mFocusedBounds = new Rect();
        @GuardedBy({"mLock"})
        private long mFocusedNodeId = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
        @GuardedBy({"mLock"})
        private int mFocusedWindowId = -1;
        @GuardedBy({"mLock"})
        private final Rect mTempBounds = new Rect();

        CompatibilityBridge() {
            AccessibilityManager.getInstance(AutofillManager.this.mContext).setAccessibilityPolicy(this);
        }

        private AccessibilityServiceInfo getCompatServiceInfo() {
            synchronized (AutofillManager.this.mLock) {
                if (this.mCompatServiceInfo != null) {
                    AccessibilityServiceInfo accessibilityServiceInfo = this.mCompatServiceInfo;
                    return accessibilityServiceInfo;
                }
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("android", "com.android.server.autofill.AutofillCompatAccessibilityService"));
                try {
                    this.mCompatServiceInfo = new AccessibilityServiceInfo(AutofillManager.this.mContext.getPackageManager().resolveService(intent, 1048704), AutofillManager.this.mContext);
                    AccessibilityServiceInfo accessibilityServiceInfo2 = this.mCompatServiceInfo;
                    return accessibilityServiceInfo2;
                } catch (IOException | XmlPullParserException e) {
                    String str = AutofillManager.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Cannot find compat autofill service:");
                    stringBuilder.append(intent);
                    Log.e(str, stringBuilder.toString());
                    throw new IllegalStateException("Cannot find compat autofill service");
                }
            }
        }

        public boolean isEnabled(boolean accessibilityEnabled) {
            return true;
        }

        public int getRelevantEventTypes(int relevantEventTypes) {
            return (((relevantEventTypes | 8) | 16) | 1) | 2048;
        }

        public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(List<AccessibilityServiceInfo> installedServices) {
            if (installedServices == null) {
                installedServices = new ArrayList();
            }
            installedServices.add(getCompatServiceInfo());
            return installedServices;
        }

        public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int feedbackTypeFlags, List<AccessibilityServiceInfo> enabledService) {
            if (enabledService == null) {
                enabledService = new ArrayList();
            }
            enabledService.add(getCompatServiceInfo());
            return enabledService;
        }

        public AccessibilityEvent onAccessibilityEvent(AccessibilityEvent event, boolean accessibilityEnabled, int relevantEventTypes) {
            int type = event.getEventType();
            if (Helper.sVerbose) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onAccessibilityEvent(");
                stringBuilder.append(AccessibilityEvent.eventTypeToString(type));
                stringBuilder.append("): virtualId=");
                stringBuilder.append(AccessibilityNodeInfo.getVirtualDescendantId(event.getSourceNodeId()));
                stringBuilder.append(", client=");
                stringBuilder.append(AutofillManager.this.getClient());
                Log.v(AutofillManager.TAG, stringBuilder.toString());
            }
            if (type == 1) {
                synchronized (AutofillManager.this.mLock) {
                    notifyViewClicked(event.getWindowId(), event.getSourceNodeId());
                }
            } else if (type == 8) {
                synchronized (AutofillManager.this.mLock) {
                    if (this.mFocusedWindowId == event.getWindowId() && this.mFocusedNodeId == event.getSourceNodeId()) {
                        return event;
                    }
                    if (!(this.mFocusedWindowId == -1 || this.mFocusedNodeId == AccessibilityNodeInfo.UNDEFINED_NODE_ID)) {
                        notifyViewExited(this.mFocusedWindowId, this.mFocusedNodeId);
                        this.mFocusedWindowId = -1;
                        this.mFocusedNodeId = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
                        this.mFocusedBounds.set(0, 0, 0, 0);
                    }
                    int windowId = event.getWindowId();
                    long nodeId = event.getSourceNodeId();
                    if (notifyViewEntered(windowId, nodeId, this.mFocusedBounds)) {
                        this.mFocusedWindowId = windowId;
                        this.mFocusedNodeId = nodeId;
                    }
                }
            } else if (type == 16) {
                synchronized (AutofillManager.this.mLock) {
                    if (this.mFocusedWindowId == event.getWindowId() && this.mFocusedNodeId == event.getSourceNodeId()) {
                        notifyValueChanged(event.getWindowId(), event.getSourceNodeId());
                    }
                }
            } else if (type == 2048) {
                AutofillClient client = AutofillManager.this.getClient();
                if (client != null) {
                    synchronized (AutofillManager.this.mLock) {
                        if (client.autofillClientIsFillUiShowing()) {
                            notifyViewEntered(this.mFocusedWindowId, this.mFocusedNodeId, this.mFocusedBounds);
                        }
                        updateTrackedViewsLocked();
                    }
                }
            }
            return accessibilityEnabled ? event : null;
        }

        private boolean notifyViewEntered(int windowId, long nodeId, Rect focusedBounds) {
            int virtualId = AccessibilityNodeInfo.getVirtualDescendantId(nodeId);
            if (!isVirtualNode(virtualId)) {
                return false;
            }
            View view = findViewByAccessibilityId(windowId, nodeId);
            if (view == null) {
                return false;
            }
            AccessibilityNodeInfo node = findVirtualNodeByAccessibilityId(view, virtualId);
            if (node == null || !node.isEditable()) {
                return false;
            }
            Rect newBounds = this.mTempBounds;
            node.getBoundsInScreen(newBounds);
            if (newBounds.equals(focusedBounds)) {
                return false;
            }
            focusedBounds.set(newBounds);
            AutofillManager.this.notifyViewEntered(view, virtualId, newBounds);
            return true;
        }

        private void notifyViewExited(int windowId, long nodeId) {
            int virtualId = AccessibilityNodeInfo.getVirtualDescendantId(nodeId);
            if (isVirtualNode(virtualId)) {
                View view = findViewByAccessibilityId(windowId, nodeId);
                if (view != null) {
                    AutofillManager.this.notifyViewExited(view, virtualId);
                }
            }
        }

        private void notifyValueChanged(int windowId, long nodeId) {
            int virtualId = AccessibilityNodeInfo.getVirtualDescendantId(nodeId);
            if (isVirtualNode(virtualId)) {
                View view = findViewByAccessibilityId(windowId, nodeId);
                if (view != null) {
                    AccessibilityNodeInfo node = findVirtualNodeByAccessibilityId(view, virtualId);
                    if (node != null) {
                        AutofillManager.this.notifyValueChanged(view, virtualId, AutofillValue.forText(node.getText()));
                    }
                }
            }
        }

        private void notifyViewClicked(int windowId, long nodeId) {
            int virtualId = AccessibilityNodeInfo.getVirtualDescendantId(nodeId);
            if (isVirtualNode(virtualId)) {
                View view = findViewByAccessibilityId(windowId, nodeId);
                if (view != null && findVirtualNodeByAccessibilityId(view, virtualId) != null) {
                    AutofillManager.this.notifyViewClicked(view, virtualId);
                }
            }
        }

        @GuardedBy({"mLock"})
        private void updateTrackedViewsLocked() {
            if (AutofillManager.this.mTrackedViews != null) {
                AutofillManager.this.mTrackedViews.onVisibleForAutofillChangedLocked();
            }
        }

        private View findViewByAccessibilityId(int windowId, long nodeId) {
            AutofillClient client = AutofillManager.this.getClient();
            if (client == null) {
                return null;
            }
            return client.autofillClientFindViewByAccessibilityIdTraversal(AccessibilityNodeInfo.getAccessibilityViewId(nodeId), windowId);
        }

        private AccessibilityNodeInfo findVirtualNodeByAccessibilityId(View view, int virtualId) {
            AccessibilityNodeProvider provider = view.getAccessibilityNodeProvider();
            if (provider == null) {
                return null;
            }
            return provider.createAccessibilityNodeInfo(virtualId);
        }

        private boolean isVirtualNode(int nodeId) {
            return (nodeId == -1 || nodeId == Integer.MAX_VALUE) ? false : true;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SmartSuggestionMode {
    }

    private class TrackedViews {
        private ArraySet<AutofillId> mInvisibleTrackedIds;
        private ArraySet<AutofillId> mVisibleTrackedIds;

        private <T> boolean isInSet(ArraySet<T> set, T value) {
            return set != null && set.contains(value);
        }

        private <T> ArraySet<T> addToSet(ArraySet<T> set, T valueToAdd) {
            if (set == null) {
                set = new ArraySet(1);
            }
            set.add(valueToAdd);
            return set;
        }

        private <T> ArraySet<T> removeFromSet(ArraySet<T> set, T valueToRemove) {
            if (set == null) {
                return null;
            }
            set.remove(valueToRemove);
            if (set.isEmpty()) {
                return null;
            }
            return set;
        }

        TrackedViews(AutofillId[] trackedIds) {
            AutofillClient client = AutofillManager.this.getClient();
            boolean isEmpty = ArrayUtils.isEmpty((Object[]) trackedIds);
            String str = AutofillManager.TAG;
            if (!(isEmpty || client == null)) {
                boolean[] isVisible;
                if (client.autofillClientIsVisibleForAutofill()) {
                    if (Helper.sVerbose) {
                        Log.v(str, "client is visible, check tracked ids");
                    }
                    isVisible = client.autofillClientGetViewVisibility(trackedIds);
                } else {
                    isVisible = new boolean[trackedIds.length];
                }
                int numIds = trackedIds.length;
                for (int i = 0; i < numIds; i++) {
                    AutofillId id = trackedIds[i];
                    id.resetSessionId();
                    if (isVisible[i]) {
                        this.mVisibleTrackedIds = addToSet(this.mVisibleTrackedIds, id);
                    } else {
                        this.mInvisibleTrackedIds = addToSet(this.mInvisibleTrackedIds, id);
                    }
                }
            }
            if (Helper.sVerbose) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("TrackedViews(trackedIds=");
                stringBuilder.append(Arrays.toString(trackedIds));
                stringBuilder.append("):  mVisibleTrackedIds=");
                stringBuilder.append(this.mVisibleTrackedIds);
                stringBuilder.append(" mInvisibleTrackedIds=");
                stringBuilder.append(this.mInvisibleTrackedIds);
                Log.v(str, stringBuilder.toString());
            }
            if (this.mVisibleTrackedIds == null) {
                AutofillManager.this.finishSessionLocked();
            }
        }

        /* Access modifiers changed, original: 0000 */
        @GuardedBy({"mLock"})
        public void notifyViewVisibilityChangedLocked(AutofillId id, boolean isVisible) {
            StringBuilder stringBuilder;
            boolean z = Helper.sDebug;
            String str = AutofillManager.TAG;
            if (z) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("notifyViewVisibilityChangedLocked(): id=");
                stringBuilder.append(id);
                stringBuilder.append(" isVisible=");
                stringBuilder.append(isVisible);
                Log.d(str, stringBuilder.toString());
            }
            if (AutofillManager.this.isClientVisibleForAutofillLocked()) {
                if (isVisible) {
                    if (isInSet(this.mInvisibleTrackedIds, id)) {
                        this.mInvisibleTrackedIds = removeFromSet(this.mInvisibleTrackedIds, id);
                        this.mVisibleTrackedIds = addToSet(this.mVisibleTrackedIds, id);
                    }
                } else if (isInSet(this.mVisibleTrackedIds, id)) {
                    this.mVisibleTrackedIds = removeFromSet(this.mVisibleTrackedIds, id);
                    this.mInvisibleTrackedIds = addToSet(this.mInvisibleTrackedIds, id);
                }
            }
            if (this.mVisibleTrackedIds == null) {
                if (Helper.sVerbose) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("No more visible ids. Invisibile = ");
                    stringBuilder.append(this.mInvisibleTrackedIds);
                    Log.v(str, stringBuilder.toString());
                }
                AutofillManager.this.finishSessionLocked();
            }
        }

        /* Access modifiers changed, original: 0000 */
        @GuardedBy({"mLock"})
        public void onVisibleForAutofillChangedLocked() {
            AutofillClient client = AutofillManager.this.getClient();
            ArraySet<AutofillId> updatedVisibleTrackedIds = null;
            ArraySet<AutofillId> updatedInvisibleTrackedIds = null;
            String str = AutofillManager.TAG;
            if (client != null) {
                ArrayList<AutofillId> orderedInvisibleIds;
                boolean[] isVisible;
                int numInvisibleTrackedIds;
                int i;
                AutofillId id;
                StringBuilder stringBuilder;
                if (Helper.sVerbose) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("onVisibleForAutofillChangedLocked(): inv= ");
                    stringBuilder2.append(this.mInvisibleTrackedIds);
                    stringBuilder2.append(" vis=");
                    stringBuilder2.append(this.mVisibleTrackedIds);
                    Log.v(str, stringBuilder2.toString());
                }
                ArraySet arraySet = this.mInvisibleTrackedIds;
                String str2 = "onVisibleForAutofill() ";
                if (arraySet != null) {
                    orderedInvisibleIds = new ArrayList(arraySet);
                    isVisible = client.autofillClientGetViewVisibility(Helper.toArray(orderedInvisibleIds));
                    numInvisibleTrackedIds = orderedInvisibleIds.size();
                    for (i = 0; i < numInvisibleTrackedIds; i++) {
                        id = (AutofillId) orderedInvisibleIds.get(i);
                        if (isVisible[i]) {
                            updatedVisibleTrackedIds = addToSet(updatedVisibleTrackedIds, id);
                            if (Helper.sDebug) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(str2);
                                stringBuilder.append(id);
                                stringBuilder.append(" became visible");
                                Log.d(str, stringBuilder.toString());
                            }
                        } else {
                            updatedInvisibleTrackedIds = addToSet(updatedInvisibleTrackedIds, id);
                        }
                    }
                }
                arraySet = this.mVisibleTrackedIds;
                if (arraySet != null) {
                    orderedInvisibleIds = new ArrayList(arraySet);
                    isVisible = client.autofillClientGetViewVisibility(Helper.toArray(orderedInvisibleIds));
                    numInvisibleTrackedIds = orderedInvisibleIds.size();
                    for (i = 0; i < numInvisibleTrackedIds; i++) {
                        id = (AutofillId) orderedInvisibleIds.get(i);
                        if (isVisible[i]) {
                            updatedVisibleTrackedIds = addToSet(updatedVisibleTrackedIds, id);
                        } else {
                            updatedInvisibleTrackedIds = addToSet(updatedInvisibleTrackedIds, id);
                            if (Helper.sDebug) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(str2);
                                stringBuilder.append(id);
                                stringBuilder.append(" became invisible");
                                Log.d(str, stringBuilder.toString());
                            }
                        }
                    }
                }
                this.mInvisibleTrackedIds = updatedInvisibleTrackedIds;
                this.mVisibleTrackedIds = updatedVisibleTrackedIds;
            }
            if (this.mVisibleTrackedIds == null) {
                if (Helper.sVerbose) {
                    Log.v(str, "onVisibleForAutofillChangedLocked(): no more visible ids");
                }
                AutofillManager.this.finishSessionLocked();
            }
        }
    }

    static {
        int i;
        if (Build.IS_DEBUGGABLE) {
            i = 2;
        } else {
            i = 0;
        }
        DEFAULT_LOGGING_LEVEL = i;
    }

    public static int makeAuthenticationId(int requestId, int datasetId) {
        return (requestId << 16) | (65535 & datasetId);
    }

    public static int getRequestIdFromAuthenticationId(int authRequestId) {
        return authRequestId >> 16;
    }

    public static int getDatasetIdFromAuthenticationId(int authRequestId) {
        return 65535 & authRequestId;
    }

    public AutofillManager(Context context, IAutoFillManager service) {
        boolean z = false;
        this.mState = 0;
        this.mContext = (Context) Preconditions.checkNotNull(context, "context cannot be null");
        this.mService = service;
        this.mOptions = context.getAutofillOptions();
        AutofillOptions autofillOptions = this.mOptions;
        if (autofillOptions != null) {
            Helper.sDebug = (autofillOptions.loggingLevel & 2) != 0;
            if ((this.mOptions.loggingLevel & 4) != 0) {
                z = true;
            }
            Helper.sVerbose = z;
        }
    }

    public void enableCompatibilityMode() {
        synchronized (this.mLock) {
            if (Helper.sDebug) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("creating CompatibilityBridge for ");
                stringBuilder.append(this.mContext);
                Slog.d(str, stringBuilder.toString());
            }
            this.mCompatibilityBridge = new CompatibilityBridge();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        if (hasAutofillFeature()) {
            synchronized (this.mLock) {
                this.mLastAutofilledData = (ParcelableMap) savedInstanceState.getParcelable(LAST_AUTOFILLED_DATA_TAG);
                if (isActiveLocked()) {
                    Log.w(TAG, "New session was started before onCreate()");
                    return;
                }
                this.mSessionId = savedInstanceState.getInt(SESSION_ID_TAG, Integer.MAX_VALUE);
                this.mState = savedInstanceState.getInt(STATE_TAG, 0);
                if (this.mSessionId != Integer.MAX_VALUE) {
                    ensureServiceClientAddedIfNeededLocked();
                    AutofillClient client = getClient();
                    if (client != null) {
                        SyncResultReceiver receiver = new SyncResultReceiver(5000);
                        try {
                            this.mService.restoreSession(this.mSessionId, client.autofillClientGetActivityToken(), this.mServiceClient.asBinder(), receiver);
                            boolean z = true;
                            if (receiver.getIntResult() != 1) {
                                z = false;
                            }
                            if (z) {
                                if (Helper.sDebug) {
                                    String str = TAG;
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("session ");
                                    stringBuilder.append(this.mSessionId);
                                    stringBuilder.append(" was restored");
                                    Log.d(str, stringBuilder.toString());
                                }
                                client.autofillClientResetableStateAvailable();
                            } else {
                                String str2 = TAG;
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("Session ");
                                stringBuilder2.append(this.mSessionId);
                                stringBuilder2.append(" could not be restored");
                                Log.w(str2, stringBuilder2.toString());
                                this.mSessionId = Integer.MAX_VALUE;
                                this.mState = 0;
                            }
                        } catch (RemoteException e) {
                            Log.e(TAG, "Could not figure out if there was an autofill session", e);
                        }
                    }
                }
            }
        } else {
            return;
        }
    }

    public void onVisibleForAutofill() {
        Choreographer.getInstance().postCallback(4, new -$$Lambda$AutofillManager$YfpJNFodEuj5lbXfPlc77fsEvC8(this), null);
    }

    public /* synthetic */ void lambda$onVisibleForAutofill$0$AutofillManager() {
        synchronized (this.mLock) {
            if (this.mEnabled && isActiveLocked() && this.mTrackedViews != null) {
                this.mTrackedViews.onVisibleForAutofillChangedLocked();
            }
        }
    }

    public void onInvisibleForAutofill() {
        synchronized (this.mLock) {
            this.mOnInvisibleCalled = true;
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        if (hasAutofillFeature()) {
            synchronized (this.mLock) {
                if (this.mSessionId != Integer.MAX_VALUE) {
                    outState.putInt(SESSION_ID_TAG, this.mSessionId);
                }
                if (this.mState != 0) {
                    outState.putInt(STATE_TAG, this.mState);
                }
                if (this.mLastAutofilledData != null) {
                    outState.putParcelable(LAST_AUTOFILLED_DATA_TAG, this.mLastAutofilledData);
                }
            }
        }
    }

    @GuardedBy({"mLock"})
    public boolean isCompatibilityModeEnabledLocked() {
        return this.mCompatibilityBridge != null;
    }

    public boolean isEnabled() {
        if (!hasAutofillFeature()) {
            return false;
        }
        synchronized (this.mLock) {
            if (isDisabledByServiceLocked()) {
                return false;
            }
            ensureServiceClientAddedIfNeededLocked();
            boolean z = this.mEnabled;
            return z;
        }
    }

    public FillEventHistory getFillEventHistory() {
        try {
            SyncResultReceiver receiver = new SyncResultReceiver(5000);
            this.mService.getFillEventHistory(receiver);
            return (FillEventHistory) receiver.getParcelableResult();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public void requestAutofill(View view) {
        notifyViewEntered(view, 1);
    }

    public void requestAutofill(View view, int virtualId, Rect absBounds) {
        notifyViewEntered(view, virtualId, absBounds, 1);
    }

    public void notifyViewEntered(View view) {
        notifyViewEntered(view, 0);
    }

    @GuardedBy({"mLock"})
    private boolean shouldIgnoreViewEnteredLocked(AutofillId id, int flags) {
        boolean isDisabledByServiceLocked = isDisabledByServiceLocked();
        String str = ") on state ";
        String str2 = ", view=";
        String str3 = "ignoring notifyViewEntered(flags=";
        String str4 = TAG;
        StringBuilder stringBuilder;
        if (isDisabledByServiceLocked) {
            if (Helper.sVerbose) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str3);
                stringBuilder.append(flags);
                stringBuilder.append(str2);
                stringBuilder.append(id);
                stringBuilder.append(str);
                stringBuilder.append(getStateAsStringLocked());
                stringBuilder.append(" because disabled by svc");
                Log.v(str4, stringBuilder.toString());
            }
            return true;
        }
        if (isFinishedLocked() && (flags & 1) == 0) {
            ArraySet arraySet = this.mEnteredIds;
            if (arraySet != null && arraySet.contains(id)) {
                if (Helper.sVerbose) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str3);
                    stringBuilder.append(flags);
                    stringBuilder.append(str2);
                    stringBuilder.append(id);
                    stringBuilder.append(str);
                    stringBuilder.append(getStateAsStringLocked());
                    stringBuilder.append(" because view was already entered: ");
                    stringBuilder.append(this.mEnteredIds);
                    Log.v(str4, stringBuilder.toString());
                }
                return true;
            }
        }
        return false;
    }

    private boolean isClientVisibleForAutofillLocked() {
        AutofillClient client = getClient();
        return client != null && client.autofillClientIsVisibleForAutofill();
    }

    private boolean isClientDisablingEnterExitEvent() {
        AutofillClient client = getClient();
        return client != null && client.isDisablingEnterExitEventForAutofill();
    }

    private void notifyViewEntered(View view, int flags) {
        if (hasAutofillFeature()) {
            AutofillCallback callback;
            synchronized (this.mLock) {
                callback = notifyViewEnteredLocked(view, flags);
            }
            if (callback != null) {
                this.mCallback.onAutofillEvent(view, 3);
            }
        }
    }

    @GuardedBy({"mLock"})
    private AutofillCallback notifyViewEnteredLocked(View view, int flags) {
        AutofillId id = view.getAutofillId();
        if (shouldIgnoreViewEnteredLocked(id, flags)) {
            return null;
        }
        AutofillCallback callback = null;
        ensureServiceClientAddedIfNeededLocked();
        boolean z = this.mEnabled;
        String str = TAG;
        StringBuilder stringBuilder;
        if (!z && !this.mEnabledForAugmentedAutofillOnly) {
            if (Helper.sVerbose) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("ignoring notifyViewEntered(");
                stringBuilder.append(id);
                stringBuilder.append("): disabled");
                Log.v(str, stringBuilder.toString());
            }
            if (this.mCallback != null) {
                callback = this.mCallback;
            }
        } else if (!isClientDisablingEnterExitEvent()) {
            AutofillValue value = view.getAutofillValue();
            if (isActiveLocked()) {
                if (this.mForAugmentedAutofillOnly && (flags & 1) != 0) {
                    if (Helper.sDebug) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("notifyViewEntered(");
                        stringBuilder.append(id);
                        stringBuilder.append("): resetting mForAugmentedAutofillOnly on manual request");
                        Log.d(str, stringBuilder.toString());
                    }
                    this.mForAugmentedAutofillOnly = false;
                }
                updateSessionLocked(id, null, value, 2, flags);
            } else {
                startSessionLocked(id, null, value, flags);
            }
            addEnteredIdLocked(id);
        }
        return callback;
    }

    public void notifyViewExited(View view) {
        if (hasAutofillFeature()) {
            synchronized (this.mLock) {
                notifyViewExitedLocked(view);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @GuardedBy({"mLock"})
    public void notifyViewExitedLocked(View view) {
        ensureServiceClientAddedIfNeededLocked();
        if ((this.mEnabled || this.mEnabledForAugmentedAutofillOnly) && isActiveLocked() && !isClientDisablingEnterExitEvent()) {
            updateSessionLocked(view.getAutofillId(), null, null, 3, 0);
        }
    }

    public void notifyViewVisibilityChanged(View view, boolean isVisible) {
        notifyViewVisibilityChangedInternal(view, 0, isVisible, false);
    }

    public void notifyViewVisibilityChanged(View view, int virtualId, boolean isVisible) {
        notifyViewVisibilityChangedInternal(view, virtualId, isVisible, true);
    }

    /* JADX WARNING: Missing block: B:9:0x0014, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:36:0x00a8, code skipped:
            return;
     */
    private void notifyViewVisibilityChangedInternal(android.view.View r6, int r7, boolean r8, boolean r9) {
        /*
        r5 = this;
        r0 = r5.mLock;
        monitor-enter(r0);
        r1 = r5.mForAugmentedAutofillOnly;	 Catch:{ all -> 0x00a9 }
        if (r1 == 0) goto L_0x0015;
    L_0x0007:
        r1 = android.view.autofill.Helper.sVerbose;	 Catch:{ all -> 0x00a9 }
        if (r1 == 0) goto L_0x0013;
    L_0x000b:
        r1 = "AutofillManager";
        r2 = "notifyViewVisibilityChanged(): ignoring on augmented only mode";
        android.util.Log.v(r1, r2);	 Catch:{ all -> 0x00a9 }
    L_0x0013:
        monitor-exit(r0);	 Catch:{ all -> 0x00a9 }
        return;
    L_0x0015:
        r1 = r5.mEnabled;	 Catch:{ all -> 0x00a9 }
        if (r1 == 0) goto L_0x00a7;
    L_0x0019:
        r1 = r5.isActiveLocked();	 Catch:{ all -> 0x00a9 }
        if (r1 == 0) goto L_0x00a7;
    L_0x001f:
        if (r9 == 0) goto L_0x0026;
    L_0x0021:
        r1 = getAutofillId(r6, r7);	 Catch:{ all -> 0x00a9 }
        goto L_0x002a;
    L_0x0026:
        r1 = r6.getAutofillId();	 Catch:{ all -> 0x00a9 }
        r2 = android.view.autofill.Helper.sVerbose;	 Catch:{ all -> 0x00a9 }
        if (r2 == 0) goto L_0x004e;
    L_0x002f:
        r2 = "AutofillManager";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a9 }
        r3.<init>();	 Catch:{ all -> 0x00a9 }
        r4 = "visibility changed for ";
        r3.append(r4);	 Catch:{ all -> 0x00a9 }
        r3.append(r1);	 Catch:{ all -> 0x00a9 }
        r4 = ": ";
        r3.append(r4);	 Catch:{ all -> 0x00a9 }
        r3.append(r8);	 Catch:{ all -> 0x00a9 }
        r3 = r3.toString();	 Catch:{ all -> 0x00a9 }
        android.util.Log.v(r2, r3);	 Catch:{ all -> 0x00a9 }
    L_0x004e:
        if (r8 != 0) goto L_0x007e;
    L_0x0050:
        r2 = r5.mFillableIds;	 Catch:{ all -> 0x00a9 }
        if (r2 == 0) goto L_0x007e;
    L_0x0054:
        r2 = r5.mFillableIds;	 Catch:{ all -> 0x00a9 }
        r2 = r2.contains(r1);	 Catch:{ all -> 0x00a9 }
        if (r2 == 0) goto L_0x007e;
    L_0x005c:
        r2 = android.view.autofill.Helper.sDebug;	 Catch:{ all -> 0x00a9 }
        if (r2 == 0) goto L_0x007b;
    L_0x0060:
        r2 = "AutofillManager";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a9 }
        r3.<init>();	 Catch:{ all -> 0x00a9 }
        r4 = "Hidding UI when view ";
        r3.append(r4);	 Catch:{ all -> 0x00a9 }
        r3.append(r1);	 Catch:{ all -> 0x00a9 }
        r4 = " became invisible";
        r3.append(r4);	 Catch:{ all -> 0x00a9 }
        r3 = r3.toString();	 Catch:{ all -> 0x00a9 }
        android.util.Log.d(r2, r3);	 Catch:{ all -> 0x00a9 }
    L_0x007b:
        r5.requestHideFillUi(r1, r6);	 Catch:{ all -> 0x00a9 }
    L_0x007e:
        r2 = r5.mTrackedViews;	 Catch:{ all -> 0x00a9 }
        if (r2 == 0) goto L_0x0088;
    L_0x0082:
        r2 = r5.mTrackedViews;	 Catch:{ all -> 0x00a9 }
        r2.notifyViewVisibilityChangedLocked(r1, r8);	 Catch:{ all -> 0x00a9 }
        goto L_0x00a7;
    L_0x0088:
        r2 = android.view.autofill.Helper.sVerbose;	 Catch:{ all -> 0x00a9 }
        if (r2 == 0) goto L_0x00a7;
    L_0x008c:
        r2 = "AutofillManager";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a9 }
        r3.<init>();	 Catch:{ all -> 0x00a9 }
        r4 = "Ignoring visibility change on ";
        r3.append(r4);	 Catch:{ all -> 0x00a9 }
        r3.append(r1);	 Catch:{ all -> 0x00a9 }
        r4 = ": no tracked views";
        r3.append(r4);	 Catch:{ all -> 0x00a9 }
        r3 = r3.toString();	 Catch:{ all -> 0x00a9 }
        android.util.Log.v(r2, r3);	 Catch:{ all -> 0x00a9 }
    L_0x00a7:
        monitor-exit(r0);	 Catch:{ all -> 0x00a9 }
        return;
    L_0x00a9:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x00a9 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.autofill.AutofillManager.notifyViewVisibilityChangedInternal(android.view.View, int, boolean, boolean):void");
    }

    public void notifyViewEntered(View view, int virtualId, Rect absBounds) {
        notifyViewEntered(view, virtualId, absBounds, 0);
    }

    private void notifyViewEntered(View view, int virtualId, Rect bounds, int flags) {
        if (hasAutofillFeature()) {
            AutofillCallback callback;
            synchronized (this.mLock) {
                callback = notifyViewEnteredLocked(view, virtualId, bounds, flags);
            }
            if (callback != null) {
                callback.onAutofillEvent(view, virtualId, 3);
            }
        }
    }

    @GuardedBy({"mLock"})
    private AutofillCallback notifyViewEnteredLocked(View view, int virtualId, Rect bounds, int flags) {
        AutofillId id = getAutofillId(view, virtualId);
        AutofillCallback callback = null;
        if (shouldIgnoreViewEnteredLocked(id, flags)) {
            return null;
        }
        ensureServiceClientAddedIfNeededLocked();
        boolean z = this.mEnabled;
        String str = TAG;
        StringBuilder stringBuilder;
        if (!z && !this.mEnabledForAugmentedAutofillOnly) {
            if (Helper.sVerbose) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("ignoring notifyViewEntered(");
                stringBuilder.append(id);
                stringBuilder.append("): disabled");
                Log.v(str, stringBuilder.toString());
            }
            if (this.mCallback != null) {
                callback = this.mCallback;
            }
        } else if (!isClientDisablingEnterExitEvent()) {
            if (isActiveLocked()) {
                if (this.mForAugmentedAutofillOnly && (flags & 1) != 0) {
                    if (Helper.sDebug) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("notifyViewEntered(");
                        stringBuilder.append(id);
                        stringBuilder.append("): resetting mForAugmentedAutofillOnly on manual request");
                        Log.d(str, stringBuilder.toString());
                    }
                    this.mForAugmentedAutofillOnly = false;
                }
                updateSessionLocked(id, bounds, null, 2, flags);
            } else {
                startSessionLocked(id, bounds, null, flags);
            }
            addEnteredIdLocked(id);
        }
        return callback;
    }

    @GuardedBy({"mLock"})
    private void addEnteredIdLocked(AutofillId id) {
        if (this.mEnteredIds == null) {
            this.mEnteredIds = new ArraySet(1);
        }
        id.resetSessionId();
        this.mEnteredIds.add(id);
    }

    public void notifyViewExited(View view, int virtualId) {
        if (Helper.sVerbose) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("notifyViewExited(");
            stringBuilder.append(view.getAutofillId());
            stringBuilder.append(", ");
            stringBuilder.append(virtualId);
            Log.v(TAG, stringBuilder.toString());
        }
        if (hasAutofillFeature()) {
            synchronized (this.mLock) {
                notifyViewExitedLocked(view, virtualId);
            }
        }
    }

    @GuardedBy({"mLock"})
    private void notifyViewExitedLocked(View view, int virtualId) {
        ensureServiceClientAddedIfNeededLocked();
        if ((this.mEnabled || this.mEnabledForAugmentedAutofillOnly) && isActiveLocked() && !isClientDisablingEnterExitEvent()) {
            updateSessionLocked(getAutofillId(view, virtualId), null, null, 3, 0);
        }
    }

    /* JADX WARNING: Missing block: B:22:0x0057, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:39:0x00a8, code skipped:
            return;
     */
    public void notifyValueChanged(android.view.View r12) {
        /*
        r11 = this;
        r0 = r11.hasAutofillFeature();
        if (r0 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r0 = 0;
        r1 = 0;
        r2 = 0;
        r3 = r11.mLock;
        monitor-enter(r3);
        r4 = r11.mLastAutofilledData;	 Catch:{ all -> 0x00a9 }
        r5 = 0;
        if (r4 != 0) goto L_0x0016;
    L_0x0012:
        r12.setAutofilled(r5);	 Catch:{ all -> 0x00a9 }
        goto L_0x0046;
    L_0x0016:
        r4 = r12.getAutofillId();	 Catch:{ all -> 0x00a9 }
        r0 = r4;
        r4 = r11.mLastAutofilledData;	 Catch:{ all -> 0x00a9 }
        r4 = r4.containsKey(r0);	 Catch:{ all -> 0x00a9 }
        if (r4 == 0) goto L_0x0043;
    L_0x0023:
        r4 = r12.getAutofillValue();	 Catch:{ all -> 0x00a9 }
        r2 = r4;
        r1 = 1;
        r4 = r11.mLastAutofilledData;	 Catch:{ all -> 0x00a9 }
        r4 = r4.get(r0);	 Catch:{ all -> 0x00a9 }
        r4 = java.util.Objects.equals(r4, r2);	 Catch:{ all -> 0x00a9 }
        if (r4 == 0) goto L_0x003a;
    L_0x0035:
        r4 = 1;
        r12.setAutofilled(r4);	 Catch:{ all -> 0x00a9 }
        goto L_0x0046;
    L_0x003a:
        r12.setAutofilled(r5);	 Catch:{ all -> 0x00a9 }
        r4 = r11.mLastAutofilledData;	 Catch:{ all -> 0x00a9 }
        r4.remove(r0);	 Catch:{ all -> 0x00a9 }
        goto L_0x0046;
    L_0x0043:
        r12.setAutofilled(r5);	 Catch:{ all -> 0x00a9 }
    L_0x0046:
        r4 = r11.mForAugmentedAutofillOnly;	 Catch:{ all -> 0x00a9 }
        if (r4 == 0) goto L_0x0058;
    L_0x004a:
        r4 = android.view.autofill.Helper.sVerbose;	 Catch:{ all -> 0x00a9 }
        if (r4 == 0) goto L_0x0056;
    L_0x004e:
        r4 = "AutofillManager";
        r5 = "notifyValueChanged(): not notifying system server on augmented-only mode";
        android.util.Log.v(r4, r5);	 Catch:{ all -> 0x00a9 }
    L_0x0056:
        monitor-exit(r3);	 Catch:{ all -> 0x00a9 }
        return;
    L_0x0058:
        r4 = r11.mEnabled;	 Catch:{ all -> 0x00a9 }
        if (r4 == 0) goto L_0x007c;
    L_0x005c:
        r4 = r11.isActiveLocked();	 Catch:{ all -> 0x00a9 }
        if (r4 != 0) goto L_0x0063;
    L_0x0062:
        goto L_0x007c;
    L_0x0063:
        if (r0 != 0) goto L_0x006a;
    L_0x0065:
        r4 = r12.getAutofillId();	 Catch:{ all -> 0x00a9 }
        r0 = r4;
    L_0x006a:
        if (r1 != 0) goto L_0x0071;
    L_0x006c:
        r4 = r12.getAutofillValue();	 Catch:{ all -> 0x00a9 }
        r2 = r4;
    L_0x0071:
        r7 = 0;
        r9 = 4;
        r10 = 0;
        r5 = r11;
        r6 = r0;
        r8 = r2;
        r5.updateSessionLocked(r6, r7, r8, r9, r10);	 Catch:{ all -> 0x00a9 }
        monitor-exit(r3);	 Catch:{ all -> 0x00a9 }
        return;
    L_0x007c:
        r4 = android.view.autofill.Helper.sVerbose;	 Catch:{ all -> 0x00a9 }
        if (r4 == 0) goto L_0x00a7;
    L_0x0080:
        r4 = "AutofillManager";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a9 }
        r5.<init>();	 Catch:{ all -> 0x00a9 }
        r6 = "notifyValueChanged(";
        r5.append(r6);	 Catch:{ all -> 0x00a9 }
        r6 = r12.getAutofillId();	 Catch:{ all -> 0x00a9 }
        r5.append(r6);	 Catch:{ all -> 0x00a9 }
        r6 = "): ignoring on state ";
        r5.append(r6);	 Catch:{ all -> 0x00a9 }
        r6 = r11.getStateAsStringLocked();	 Catch:{ all -> 0x00a9 }
        r5.append(r6);	 Catch:{ all -> 0x00a9 }
        r5 = r5.toString();	 Catch:{ all -> 0x00a9 }
        android.util.Log.v(r4, r5);	 Catch:{ all -> 0x00a9 }
    L_0x00a7:
        monitor-exit(r3);	 Catch:{ all -> 0x00a9 }
        return;
    L_0x00a9:
        r4 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x00a9 }
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.autofill.AutofillManager.notifyValueChanged(android.view.View):void");
    }

    /* JADX WARNING: Missing block: B:12:0x001b, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:25:0x0069, code skipped:
            return;
     */
    public void notifyValueChanged(android.view.View r9, int r10, android.view.autofill.AutofillValue r11) {
        /*
        r8 = this;
        r0 = r8.hasAutofillFeature();
        if (r0 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r0 = r8.mLock;
        monitor-enter(r0);
        r1 = r8.mForAugmentedAutofillOnly;	 Catch:{ all -> 0x006a }
        if (r1 == 0) goto L_0x001c;
    L_0x000e:
        r1 = android.view.autofill.Helper.sVerbose;	 Catch:{ all -> 0x006a }
        if (r1 == 0) goto L_0x001a;
    L_0x0012:
        r1 = "AutofillManager";
        r2 = "notifyValueChanged(): ignoring on augmented only mode";
        android.util.Log.v(r1, r2);	 Catch:{ all -> 0x006a }
    L_0x001a:
        monitor-exit(r0);	 Catch:{ all -> 0x006a }
        return;
    L_0x001c:
        r1 = r8.mEnabled;	 Catch:{ all -> 0x006a }
        if (r1 == 0) goto L_0x0035;
    L_0x0020:
        r1 = r8.isActiveLocked();	 Catch:{ all -> 0x006a }
        if (r1 != 0) goto L_0x0027;
    L_0x0026:
        goto L_0x0035;
    L_0x0027:
        r3 = getAutofillId(r9, r10);	 Catch:{ all -> 0x006a }
        r4 = 0;
        r6 = 4;
        r7 = 0;
        r2 = r8;
        r5 = r11;
        r2.updateSessionLocked(r3, r4, r5, r6, r7);	 Catch:{ all -> 0x006a }
        monitor-exit(r0);	 Catch:{ all -> 0x006a }
        return;
    L_0x0035:
        r1 = android.view.autofill.Helper.sVerbose;	 Catch:{ all -> 0x006a }
        if (r1 == 0) goto L_0x0068;
    L_0x0039:
        r1 = "AutofillManager";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x006a }
        r2.<init>();	 Catch:{ all -> 0x006a }
        r3 = "notifyValueChanged(";
        r2.append(r3);	 Catch:{ all -> 0x006a }
        r3 = r9.getAutofillId();	 Catch:{ all -> 0x006a }
        r2.append(r3);	 Catch:{ all -> 0x006a }
        r3 = ":";
        r2.append(r3);	 Catch:{ all -> 0x006a }
        r2.append(r10);	 Catch:{ all -> 0x006a }
        r3 = "): ignoring on state ";
        r2.append(r3);	 Catch:{ all -> 0x006a }
        r3 = r8.getStateAsStringLocked();	 Catch:{ all -> 0x006a }
        r2.append(r3);	 Catch:{ all -> 0x006a }
        r2 = r2.toString();	 Catch:{ all -> 0x006a }
        android.util.Log.v(r1, r2);	 Catch:{ all -> 0x006a }
    L_0x0068:
        monitor-exit(r0);	 Catch:{ all -> 0x006a }
        return;
    L_0x006a:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x006a }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.autofill.AutofillManager.notifyValueChanged(android.view.View, int, android.view.autofill.AutofillValue):void");
    }

    public void notifyViewClicked(View view) {
        notifyViewClicked(view.getAutofillId());
    }

    public void notifyViewClicked(View view, int virtualId) {
        notifyViewClicked(getAutofillId(view, virtualId));
    }

    /* JADX WARNING: Missing block: B:23:0x0070, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:25:0x0072, code skipped:
            return;
     */
    private void notifyViewClicked(android.view.autofill.AutofillId r5) {
        /*
        r4 = this;
        r0 = r4.hasAutofillFeature();
        if (r0 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r0 = android.view.autofill.Helper.sVerbose;
        if (r0 == 0) goto L_0x002c;
    L_0x000b:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "notifyViewClicked(): id=";
        r0.append(r1);
        r0.append(r5);
        r1 = ", trigger=";
        r0.append(r1);
        r1 = r4.mSaveTriggerId;
        r0.append(r1);
        r0 = r0.toString();
        r1 = "AutofillManager";
        android.util.Log.v(r1, r0);
    L_0x002c:
        r0 = r4.mLock;
        monitor-enter(r0);
        r1 = r4.mEnabled;	 Catch:{ all -> 0x0073 }
        if (r1 == 0) goto L_0x0071;
    L_0x0033:
        r1 = r4.isActiveLocked();	 Catch:{ all -> 0x0073 }
        if (r1 != 0) goto L_0x003a;
    L_0x0039:
        goto L_0x0071;
    L_0x003a:
        r1 = r4.mSaveTriggerId;	 Catch:{ all -> 0x0073 }
        if (r1 == 0) goto L_0x006f;
    L_0x003e:
        r1 = r4.mSaveTriggerId;	 Catch:{ all -> 0x0073 }
        r1 = r1.equals(r5);	 Catch:{ all -> 0x0073 }
        if (r1 == 0) goto L_0x006f;
    L_0x0046:
        r1 = android.view.autofill.Helper.sDebug;	 Catch:{ all -> 0x0073 }
        if (r1 == 0) goto L_0x0061;
    L_0x004a:
        r1 = "AutofillManager";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0073 }
        r2.<init>();	 Catch:{ all -> 0x0073 }
        r3 = "triggering commit by click of ";
        r2.append(r3);	 Catch:{ all -> 0x0073 }
        r2.append(r5);	 Catch:{ all -> 0x0073 }
        r2 = r2.toString();	 Catch:{ all -> 0x0073 }
        android.util.Log.d(r1, r2);	 Catch:{ all -> 0x0073 }
    L_0x0061:
        r4.commitLocked();	 Catch:{ all -> 0x0073 }
        r1 = r4.mMetricsLogger;	 Catch:{ all -> 0x0073 }
        r2 = 1229; // 0x4cd float:1.722E-42 double:6.07E-321;
        r2 = r4.newLog(r2);	 Catch:{ all -> 0x0073 }
        r1.write(r2);	 Catch:{ all -> 0x0073 }
    L_0x006f:
        monitor-exit(r0);	 Catch:{ all -> 0x0073 }
        return;
    L_0x0071:
        monitor-exit(r0);	 Catch:{ all -> 0x0073 }
        return;
    L_0x0073:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0073 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.autofill.AutofillManager.notifyViewClicked(android.view.autofill.AutofillId):void");
    }

    public void onActivityFinishing() {
        if (hasAutofillFeature()) {
            synchronized (this.mLock) {
                if (this.mSaveOnFinish) {
                    if (Helper.sDebug) {
                        Log.d(TAG, "onActivityFinishing(): calling commitLocked()");
                    }
                    commitLocked();
                } else {
                    if (Helper.sDebug) {
                        Log.d(TAG, "onActivityFinishing(): calling cancelLocked()");
                    }
                    cancelLocked();
                }
            }
        }
    }

    public void commit() {
        if (hasAutofillFeature()) {
            if (Helper.sVerbose) {
                Log.v(TAG, "commit() called by app");
            }
            synchronized (this.mLock) {
                commitLocked();
            }
        }
    }

    @GuardedBy({"mLock"})
    private void commitLocked() {
        if (this.mEnabled || isActiveLocked()) {
            finishSessionLocked();
        }
    }

    public void cancel() {
        if (Helper.sVerbose) {
            Log.v(TAG, "cancel() called by app");
        }
        if (hasAutofillFeature()) {
            synchronized (this.mLock) {
                cancelLocked();
            }
        }
    }

    @GuardedBy({"mLock"})
    private void cancelLocked() {
        if (this.mEnabled || isActiveLocked()) {
            cancelSessionLocked();
        }
    }

    public void disableOwnedAutofillServices() {
        disableAutofillServices();
    }

    public void disableAutofillServices() {
        if (hasAutofillFeature()) {
            try {
                this.mService.disableOwnedAutofillServices(this.mContext.getUserId());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public boolean hasEnabledAutofillServices() {
        boolean z = false;
        if (this.mService == null) {
            return false;
        }
        SyncResultReceiver receiver = new SyncResultReceiver(5000);
        try {
            this.mService.isServiceEnabled(this.mContext.getUserId(), this.mContext.getPackageName(), receiver);
            if (receiver.getIntResult() == 1) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ComponentName getAutofillServiceComponentName() {
        if (this.mService == null) {
            return null;
        }
        SyncResultReceiver receiver = new SyncResultReceiver(5000);
        try {
            this.mService.getAutofillServiceComponentName(receiver);
            return (ComponentName) receiver.getParcelableResult();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getUserDataId() {
        try {
            SyncResultReceiver receiver = new SyncResultReceiver(5000);
            this.mService.getUserDataId(receiver);
            return receiver.getStringResult();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public UserData getUserData() {
        try {
            SyncResultReceiver receiver = new SyncResultReceiver(5000);
            this.mService.getUserData(receiver);
            return (UserData) receiver.getParcelableResult();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public void setUserData(UserData userData) {
        try {
            this.mService.setUserData(userData);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public boolean isFieldClassificationEnabled() {
        SyncResultReceiver receiver = new SyncResultReceiver(5000);
        boolean z = false;
        try {
            this.mService.isFieldClassificationEnabled(receiver);
            if (receiver.getIntResult() == 1) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public String getDefaultFieldClassificationAlgorithm() {
        SyncResultReceiver receiver = new SyncResultReceiver(5000);
        try {
            this.mService.getDefaultFieldClassificationAlgorithm(receiver);
            return receiver.getStringResult();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public List<String> getAvailableFieldClassificationAlgorithms() {
        SyncResultReceiver receiver = new SyncResultReceiver(5000);
        try {
            this.mService.getAvailableFieldClassificationAlgorithms(receiver);
            String[] algorithms = receiver.getStringArrayResult();
            return algorithms != null ? Arrays.asList(algorithms) : Collections.emptyList();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public boolean isAutofillSupported() {
        boolean z = false;
        if (this.mService == null) {
            return false;
        }
        SyncResultReceiver receiver = new SyncResultReceiver(5000);
        try {
            this.mService.isServiceSupported(this.mContext.getUserId(), receiver);
            if (receiver.getIntResult() == 1) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private AutofillClient getClient() {
        AutofillClient client = this.mContext.getAutofillClient();
        if (client == null && Helper.sVerbose) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("No AutofillClient for ");
            stringBuilder.append(this.mContext.getPackageName());
            stringBuilder.append(" on context ");
            stringBuilder.append(this.mContext);
            Log.v(TAG, stringBuilder.toString());
        }
        return client;
    }

    public boolean isAutofillUiShowing() {
        AutofillClient client = this.mContext.getAutofillClient();
        return client != null && client.autofillClientIsFillUiShowing();
    }

    public void onAuthenticationResult(int authenticationId, Intent data, View focusView) {
        if (hasAutofillFeature()) {
            if (Helper.sDebug) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onAuthenticationResult(): id= ");
                stringBuilder.append(authenticationId);
                stringBuilder.append(", data=");
                stringBuilder.append(data);
                Log.d(TAG, stringBuilder.toString());
            }
            synchronized (this.mLock) {
                if (isActiveLocked()) {
                    if (!(this.mOnInvisibleCalled || focusView == null || !focusView.canNotifyAutofillEnterExitEvent())) {
                        notifyViewExitedLocked(focusView);
                        notifyViewEnteredLocked(focusView, 0);
                    }
                    if (data == null) {
                        return;
                    }
                    Parcelable result = data.getParcelableExtra(EXTRA_AUTHENTICATION_RESULT);
                    Bundle responseData = new Bundle();
                    responseData.putParcelable(EXTRA_AUTHENTICATION_RESULT, result);
                    Bundle newClientState = data.getBundleExtra(EXTRA_CLIENT_STATE);
                    if (newClientState != null) {
                        responseData.putBundle(EXTRA_CLIENT_STATE, newClientState);
                    }
                    try {
                        this.mService.setAuthenticationResult(responseData, this.mSessionId, authenticationId, this.mContext.getUserId());
                    } catch (RemoteException e) {
                        Log.e(TAG, "Error delivering authentication result", e);
                    }
                } else {
                    return;
                }
            }
        }
        return;
    }

    public AutofillId getNextAutofillId() {
        AutofillClient client = getClient();
        if (client == null) {
            return null;
        }
        AutofillId id = client.autofillClientGetNextAutofillId();
        if (id == null && Helper.sDebug) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getNextAutofillId(): client ");
            stringBuilder.append(client);
            stringBuilder.append(" returned null");
            Log.d(TAG, stringBuilder.toString());
        }
        return id;
    }

    private static AutofillId getAutofillId(View parent, int virtualId) {
        return new AutofillId(parent.getAutofillViewId(), virtualId);
    }

    @GuardedBy({"mLock"})
    private void startSessionLocked(AutofillId id, Rect bounds, AutofillValue value, int flags) {
        int flags2;
        StringBuilder stringBuilder;
        RemoteException e;
        AutofillId autofillId = id;
        Set set = this.mEnteredForAugmentedAutofillIds;
        String str = TAG;
        if ((set == null || !set.contains(autofillId)) && !this.mEnabledForAugmentedAutofillOnly) {
            flags2 = flags;
        } else {
            if (Helper.sVerbose) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Starting session for augmented autofill on ");
                stringBuilder.append(autofillId);
                Log.v(str, stringBuilder.toString());
            }
            flags2 = flags | 8;
        }
        if (Helper.sVerbose) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("startSessionLocked(): id=");
            stringBuilder.append(autofillId);
            stringBuilder.append(", bounds=");
            stringBuilder.append(bounds);
            stringBuilder.append(", value=");
            stringBuilder.append(value);
            stringBuilder.append(", flags=");
            stringBuilder.append(flags2);
            stringBuilder.append(", state=");
            stringBuilder.append(getStateAsStringLocked());
            stringBuilder.append(", compatMode=");
            stringBuilder.append(isCompatibilityModeEnabledLocked());
            stringBuilder.append(", augmentedOnly=");
            stringBuilder.append(this.mForAugmentedAutofillOnly);
            stringBuilder.append(", enabledAugmentedOnly=");
            stringBuilder.append(this.mEnabledForAugmentedAutofillOnly);
            stringBuilder.append(", enteredIds=");
            stringBuilder.append(this.mEnteredIds);
            Log.v(str, stringBuilder.toString());
        } else {
            Rect rect = bounds;
            AutofillValue autofillValue = value;
        }
        if (!(!this.mForAugmentedAutofillOnly || this.mEnabledForAugmentedAutofillOnly || (flags2 & 1) == 0)) {
            if (Helper.sVerbose) {
                Log.v(str, "resetting mForAugmentedAutofillOnly on manual autofill request");
            }
            this.mForAugmentedAutofillOnly = false;
        }
        if (this.mState == 0 || isFinishedLocked() || (flags2 & 1) != 0) {
            try {
                AutofillClient client = getClient();
                if (client != null) {
                    SyncResultReceiver receiver = new SyncResultReceiver(5000);
                    ComponentName componentName = client.autofillClientGetComponentName();
                    int i = 1;
                    ComponentName componentName2 = componentName;
                    SyncResultReceiver receiver2 = receiver;
                    try {
                        this.mService.startSession(client.autofillClientGetActivityToken(), this.mServiceClient.asBinder(), id, bounds, value, this.mContext.getUserId(), this.mCallback != null, flags2, componentName2, isCompatibilityModeEnabledLocked(), receiver2);
                        this.mSessionId = receiver2.getIntResult();
                        if (this.mSessionId != Integer.MAX_VALUE) {
                            this.mState = i;
                        }
                        if ((receiver2.getOptionalExtraIntResult(0) & 1) != 0) {
                            if (Helper.sDebug) {
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("startSession(");
                                stringBuilder2.append(componentName2);
                                stringBuilder2.append("): for augmented only");
                                Log.d(str, stringBuilder2.toString());
                            }
                            this.mForAugmentedAutofillOnly = i;
                        }
                        client.autofillClientResetableStateAvailable();
                        return;
                    } catch (RemoteException e2) {
                        e = e2;
                        throw e.rethrowFromSystemServer();
                    }
                }
                return;
            } catch (RemoteException e3) {
                e = e3;
                int i2 = flags2;
                throw e.rethrowFromSystemServer();
            }
        }
        if (Helper.sVerbose) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("not automatically starting session for ");
            stringBuilder.append(autofillId);
            stringBuilder.append(" on state ");
            stringBuilder.append(getStateAsStringLocked());
            stringBuilder.append(" and flags ");
            stringBuilder.append(flags2);
            Log.v(str, stringBuilder.toString());
        }
    }

    @GuardedBy({"mLock"})
    private void finishSessionLocked() {
        if (Helper.sVerbose) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("finishSessionLocked(): ");
            stringBuilder.append(getStateAsStringLocked());
            Log.v(TAG, stringBuilder.toString());
        }
        if (isActiveLocked()) {
            try {
                this.mService.finishSession(this.mSessionId, this.mContext.getUserId());
                resetSessionLocked(true);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @GuardedBy({"mLock"})
    private void cancelSessionLocked() {
        if (Helper.sVerbose) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("cancelSessionLocked(): ");
            stringBuilder.append(getStateAsStringLocked());
            Log.v(TAG, stringBuilder.toString());
        }
        if (isActiveLocked()) {
            try {
                this.mService.cancelSession(this.mSessionId, this.mContext.getUserId());
                resetSessionLocked(true);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @GuardedBy({"mLock"})
    private void resetSessionLocked(boolean resetEnteredIds) {
        this.mSessionId = Integer.MAX_VALUE;
        this.mState = 0;
        this.mTrackedViews = null;
        this.mFillableIds = null;
        this.mSaveTriggerId = null;
        this.mIdShownFillUi = null;
        if (resetEnteredIds) {
            this.mEnteredIds = null;
        }
    }

    @GuardedBy({"mLock"})
    private void updateSessionLocked(AutofillId id, Rect bounds, AutofillValue value, int action, int flags) {
        if (Helper.sVerbose) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("updateSessionLocked(): id=");
            stringBuilder.append(id);
            stringBuilder.append(", bounds=");
            stringBuilder.append(bounds);
            stringBuilder.append(", value=");
            stringBuilder.append(value);
            stringBuilder.append(", action=");
            stringBuilder.append(action);
            stringBuilder.append(", flags=");
            stringBuilder.append(flags);
            Log.v(TAG, stringBuilder.toString());
        }
        try {
            this.mService.updateSession(this.mSessionId, id, bounds, value, action, flags, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @GuardedBy({"mLock"})
    private void ensureServiceClientAddedIfNeededLocked() {
        AutofillClient client = getClient();
        if (client != null && this.mServiceClient == null) {
            this.mServiceClient = new AutofillManagerClient();
            try {
                int userId = this.mContext.getUserId();
                SyncResultReceiver receiver = new SyncResultReceiver(5000);
                this.mService.addClient(this.mServiceClient, client.autofillClientGetComponentName(), userId, receiver);
                int flags = receiver.getIntResult();
                boolean z = false;
                this.mEnabled = (flags & 1) != 0;
                Helper.sDebug = (flags & 2) != 0;
                Helper.sVerbose = (flags & 4) != 0;
                if ((flags & 8) != 0) {
                    z = true;
                }
                this.mEnabledForAugmentedAutofillOnly = z;
                if (Helper.sVerbose) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("receiver results: flags=");
                    stringBuilder.append(flags);
                    stringBuilder.append(" enabled=");
                    stringBuilder.append(this.mEnabled);
                    stringBuilder.append(", enabledForAugmentedOnly: ");
                    stringBuilder.append(this.mEnabledForAugmentedAutofillOnly);
                    Log.v(str, stringBuilder.toString());
                }
                this.mServiceClientCleaner = Cleaner.create(this, new -$$Lambda$AutofillManager$V76JiQu509LCUz3-ckpb-nB3JhA(this.mService, this.mServiceClient, userId));
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    static /* synthetic */ void lambda$ensureServiceClientAddedIfNeededLocked$1(IAutoFillManager service, IAutoFillManagerClient serviceClient, int userId) {
        try {
            service.removeClient(serviceClient, userId);
        } catch (RemoteException e) {
        }
    }

    /* JADX WARNING: Missing block: B:22:0x002f, code skipped:
            return;
     */
    public void registerCallback(android.view.autofill.AutofillManager.AutofillCallback r7) {
        /*
        r6 = this;
        r0 = r6.hasAutofillFeature();
        if (r0 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r0 = r6.mLock;
        monitor-enter(r0);
        if (r7 != 0) goto L_0x000e;
    L_0x000c:
        monitor-exit(r0);	 Catch:{ all -> 0x0030 }
        return;
    L_0x000e:
        r1 = r6.mCallback;	 Catch:{ all -> 0x0030 }
        r2 = 1;
        if (r1 == 0) goto L_0x0015;
    L_0x0013:
        r1 = r2;
        goto L_0x0016;
    L_0x0015:
        r1 = 0;
    L_0x0016:
        r6.mCallback = r7;	 Catch:{ all -> 0x0030 }
        if (r1 != 0) goto L_0x002e;
    L_0x001a:
        r3 = r6.mService;	 Catch:{ RemoteException -> 0x0028 }
        r4 = r6.mSessionId;	 Catch:{ RemoteException -> 0x0028 }
        r5 = r6.mContext;	 Catch:{ RemoteException -> 0x0028 }
        r5 = r5.getUserId();	 Catch:{ RemoteException -> 0x0028 }
        r3.setHasCallback(r4, r5, r2);	 Catch:{ RemoteException -> 0x0028 }
        goto L_0x002e;
    L_0x0028:
        r2 = move-exception;
        r3 = r2.rethrowFromSystemServer();	 Catch:{ all -> 0x0030 }
        throw r3;	 Catch:{ all -> 0x0030 }
    L_0x002e:
        monitor-exit(r0);	 Catch:{ all -> 0x0030 }
        return;
    L_0x0030:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0030 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.autofill.AutofillManager.registerCallback(android.view.autofill.AutofillManager$AutofillCallback):void");
    }

    /* JADX WARNING: Missing block: B:22:0x0030, code skipped:
            return;
     */
    public void unregisterCallback(android.view.autofill.AutofillManager.AutofillCallback r6) {
        /*
        r5 = this;
        r0 = r5.hasAutofillFeature();
        if (r0 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r0 = r5.mLock;
        monitor-enter(r0);
        if (r6 == 0) goto L_0x002f;
    L_0x000c:
        r1 = r5.mCallback;	 Catch:{ all -> 0x0031 }
        if (r1 == 0) goto L_0x002f;
    L_0x0010:
        r1 = r5.mCallback;	 Catch:{ all -> 0x0031 }
        if (r6 == r1) goto L_0x0015;
    L_0x0014:
        goto L_0x002f;
    L_0x0015:
        r1 = 0;
        r5.mCallback = r1;	 Catch:{ all -> 0x0031 }
        r1 = r5.mService;	 Catch:{ RemoteException -> 0x0029 }
        r2 = r5.mSessionId;	 Catch:{ RemoteException -> 0x0029 }
        r3 = r5.mContext;	 Catch:{ RemoteException -> 0x0029 }
        r3 = r3.getUserId();	 Catch:{ RemoteException -> 0x0029 }
        r4 = 0;
        r1.setHasCallback(r2, r3, r4);	 Catch:{ RemoteException -> 0x0029 }
        monitor-exit(r0);	 Catch:{ all -> 0x0031 }
        return;
    L_0x0029:
        r1 = move-exception;
        r2 = r1.rethrowFromSystemServer();	 Catch:{ all -> 0x0031 }
        throw r2;	 Catch:{ all -> 0x0031 }
    L_0x002f:
        monitor-exit(r0);	 Catch:{ all -> 0x0031 }
        return;
    L_0x0031:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0031 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.autofill.AutofillManager.unregisterCallback(android.view.autofill.AutofillManager$AutofillCallback):void");
    }

    @SystemApi
    public void setAugmentedAutofillWhitelist(Set<String> packages, Set<ComponentName> activities) {
        if (hasAutofillFeature()) {
            SyncResultReceiver resultReceiver = new SyncResultReceiver(5000);
            try {
                this.mService.setAugmentedAutofillWhitelist(Helper.toList(packages), Helper.toList(activities), resultReceiver);
                int resultCode = resultReceiver.getIntResult();
                if (resultCode == -1) {
                    throw new SecurityException("caller is not user's Augmented Autofill Service");
                } else if (resultCode != 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("setAugmentedAutofillWhitelist(): received invalid result: ");
                    stringBuilder.append(resultCode);
                    Log.wtf(TAG, stringBuilder.toString());
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void notifyViewEnteredForAugmentedAutofill(View view) {
        AutofillId id = view.getAutofillId();
        synchronized (this.mLock) {
            if (this.mEnteredForAugmentedAutofillIds == null) {
                this.mEnteredForAugmentedAutofillIds = new ArraySet(1);
            }
            this.mEnteredForAugmentedAutofillIds.add(id);
        }
    }

    /* JADX WARNING: Missing block: B:16:0x002e, code skipped:
            if (r10 == null) goto L_0x0042;
     */
    /* JADX WARNING: Missing block: B:18:0x0035, code skipped:
            if (r15.isVirtualInt() == false) goto L_0x003f;
     */
    /* JADX WARNING: Missing block: B:19:0x0037, code skipped:
            r10.onAutofillEvent(r9, r15.getVirtualChildIntId(), 1);
     */
    /* JADX WARNING: Missing block: B:20:0x003f, code skipped:
            r10.onAutofillEvent(r9, 1);
     */
    /* JADX WARNING: Missing block: B:21:0x0042, code skipped:
            return;
     */
    private void requestShowFillUi(int r14, android.view.autofill.AutofillId r15, int r16, int r17, android.graphics.Rect r18, android.view.autofill.IAutofillWindowPresenter r19) {
        /*
        r13 = this;
        r1 = r13;
        r2 = r15;
        r9 = r13.findView(r15);
        if (r9 != 0) goto L_0x0009;
    L_0x0008:
        return;
    L_0x0009:
        r10 = 0;
        r11 = r1.mLock;
        monitor-enter(r11);
        r0 = r1.mSessionId;	 Catch:{ all -> 0x0043 }
        r12 = r14;
        if (r0 != r12) goto L_0x002d;
    L_0x0012:
        r0 = r13.getClient();	 Catch:{ all -> 0x0047 }
        if (r0 == 0) goto L_0x002d;
    L_0x0018:
        r3 = r0;
        r4 = r9;
        r5 = r16;
        r6 = r17;
        r7 = r18;
        r8 = r19;
        r3 = r3.autofillClientRequestShowFillUi(r4, r5, r6, r7, r8);	 Catch:{ all -> 0x0047 }
        if (r3 == 0) goto L_0x002d;
    L_0x0028:
        r3 = r1.mCallback;	 Catch:{ all -> 0x0047 }
        r10 = r3;
        r1.mIdShownFillUi = r2;	 Catch:{ all -> 0x0047 }
    L_0x002d:
        monitor-exit(r11);	 Catch:{ all -> 0x0047 }
        if (r10 == 0) goto L_0x0042;
    L_0x0030:
        r0 = r15.isVirtualInt();
        r3 = 1;
        if (r0 == 0) goto L_0x003f;
    L_0x0037:
        r0 = r15.getVirtualChildIntId();
        r10.onAutofillEvent(r9, r0, r3);
        goto L_0x0042;
    L_0x003f:
        r10.onAutofillEvent(r9, r3);
    L_0x0042:
        return;
    L_0x0043:
        r0 = move-exception;
        r12 = r14;
    L_0x0045:
        monitor-exit(r11);	 Catch:{ all -> 0x0047 }
        throw r0;
    L_0x0047:
        r0 = move-exception;
        goto L_0x0045;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.autofill.AutofillManager.requestShowFillUi(int, android.view.autofill.AutofillId, int, int, android.graphics.Rect, android.view.autofill.IAutofillWindowPresenter):void");
    }

    private void authenticate(int sessionId, int authenticationId, IntentSender intent, Intent fillInIntent) {
        synchronized (this.mLock) {
            if (sessionId == this.mSessionId) {
                AutofillClient client = getClient();
                if (client != null) {
                    this.mOnInvisibleCalled = false;
                    client.autofillClientAuthenticate(authenticationId, intent, fillInIntent);
                }
            }
        }
    }

    private void dispatchUnhandledKey(int sessionId, AutofillId id, KeyEvent keyEvent) {
        View anchor = findView(id);
        if (anchor != null) {
            synchronized (this.mLock) {
                if (this.mSessionId == sessionId) {
                    AutofillClient client = getClient();
                    if (client != null) {
                        client.autofillClientDispatchUnhandledKey(anchor, keyEvent);
                    }
                }
            }
        }
    }

    /* JADX WARNING: Missing block: B:29:0x006a, code skipped:
            if ((r6 & 8) == 0) goto L_0x006e;
     */
    /* JADX WARNING: Missing block: B:30:0x006c, code skipped:
            r0 = true;
     */
    /* JADX WARNING: Missing block: B:31:0x006e, code skipped:
            r0 = false;
     */
    /* JADX WARNING: Missing block: B:32:0x006f, code skipped:
            android.view.autofill.Helper.sDebug = r0;
     */
    /* JADX WARNING: Missing block: B:33:0x0073, code skipped:
            if ((r6 & 16) == 0) goto L_0x0076;
     */
    /* JADX WARNING: Missing block: B:34:0x0076, code skipped:
            r2 = false;
     */
    /* JADX WARNING: Missing block: B:35:0x0077, code skipped:
            android.view.autofill.Helper.sVerbose = r2;
     */
    /* JADX WARNING: Missing block: B:36:0x0079, code skipped:
            return;
     */
    private void setState(int r6) {
        /*
        r5 = this;
        r0 = android.view.autofill.Helper.sVerbose;
        if (r0 == 0) goto L_0x0030;
    L_0x0004:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "setState(";
        r0.append(r1);
        r0.append(r6);
        r1 = ": ";
        r0.append(r1);
        r1 = android.view.autofill.AutofillManager.class;
        r2 = "SET_STATE_FLAG_";
        r1 = android.util.DebugUtils.flagsToString(r1, r2, r6);
        r0.append(r1);
        r1 = ")";
        r0.append(r1);
        r0 = r0.toString();
        r1 = "AutofillManager";
        android.util.Log.v(r1, r0);
    L_0x0030:
        r0 = r5.mLock;
        monitor-enter(r0);
        r1 = r6 & 32;
        r2 = 1;
        if (r1 == 0) goto L_0x003e;
    L_0x0038:
        r5.mForAugmentedAutofillOnly = r2;	 Catch:{ all -> 0x003c }
        monitor-exit(r0);	 Catch:{ all -> 0x003c }
        return;
    L_0x003c:
        r1 = move-exception;
        goto L_0x007a;
    L_0x003e:
        r1 = r6 & 1;
        r3 = 0;
        if (r1 == 0) goto L_0x0045;
    L_0x0043:
        r1 = r2;
        goto L_0x0046;
    L_0x0045:
        r1 = r3;
    L_0x0046:
        r5.mEnabled = r1;	 Catch:{ all -> 0x003c }
        r1 = r5.mEnabled;	 Catch:{ all -> 0x003c }
        if (r1 == 0) goto L_0x0050;
    L_0x004c:
        r1 = r6 & 2;
        if (r1 == 0) goto L_0x0053;
    L_0x0050:
        r5.resetSessionLocked(r2);	 Catch:{ all -> 0x003c }
    L_0x0053:
        r1 = r6 & 4;
        if (r1 == 0) goto L_0x0067;
    L_0x0057:
        r1 = 0;
        r5.mServiceClient = r1;	 Catch:{ all -> 0x003c }
        r5.mAugmentedAutofillServiceClient = r1;	 Catch:{ all -> 0x003c }
        r4 = r5.mServiceClientCleaner;	 Catch:{ all -> 0x003c }
        if (r4 == 0) goto L_0x0067;
    L_0x0060:
        r4 = r5.mServiceClientCleaner;	 Catch:{ all -> 0x003c }
        r4.clean();	 Catch:{ all -> 0x003c }
        r5.mServiceClientCleaner = r1;	 Catch:{ all -> 0x003c }
    L_0x0067:
        monitor-exit(r0);	 Catch:{ all -> 0x003c }
        r0 = r6 & 8;
        if (r0 == 0) goto L_0x006e;
    L_0x006c:
        r0 = r2;
        goto L_0x006f;
    L_0x006e:
        r0 = r3;
    L_0x006f:
        android.view.autofill.Helper.sDebug = r0;
        r0 = r6 & 16;
        if (r0 == 0) goto L_0x0076;
    L_0x0075:
        goto L_0x0077;
    L_0x0076:
        r2 = r3;
    L_0x0077:
        android.view.autofill.Helper.sVerbose = r2;
        return;
    L_0x007a:
        monitor-exit(r0);	 Catch:{ all -> 0x003c }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.autofill.AutofillManager.setState(int):void");
    }

    private void setAutofilledIfValuesIs(View view, AutofillValue targetValue) {
        if (Objects.equals(view.getAutofillValue(), targetValue)) {
            synchronized (this.mLock) {
                if (this.mLastAutofilledData == null) {
                    this.mLastAutofilledData = new ParcelableMap(1);
                }
                this.mLastAutofilledData.put(view.getAutofillId(), targetValue);
            }
            view.setAutofilled(true);
        }
    }

    private void autofill(int sessionId, List<AutofillId> ids, List<AutofillValue> values) {
        Throwable th;
        List<AutofillValue> list;
        synchronized (this.mLock) {
            List<AutofillId> list2;
            try {
                if (sessionId != this.mSessionId) {
                    return;
                }
                AutofillClient client = getClient();
                if (client == null) {
                    return;
                }
                StringBuilder stringBuilder;
                AutofillClient client2;
                int itemCount = ids.size();
                ArrayMap<View, SparseArray<AutofillValue>> virtualValues = null;
                View[] views = client.autofillClientFindViewsByAutofillIdTraversal(Helper.toArray(ids));
                ArrayList<AutofillId> failedIds = null;
                int i = 0;
                int numApplied = 0;
                while (i < itemCount) {
                    try {
                        AutofillId id = (AutofillId) ids.get(i);
                        AutofillValue value = (AutofillValue) values.get(i);
                        View view = views[i];
                        if (view == null) {
                            String str = TAG;
                            stringBuilder = new StringBuilder();
                            client2 = client;
                            stringBuilder.append("autofill(): no View with id ");
                            stringBuilder.append(id);
                            Log.d(str, stringBuilder.toString());
                            if (failedIds == null) {
                                failedIds = new ArrayList();
                            }
                            failedIds.add(id);
                        } else {
                            client2 = client;
                            if (id.isVirtualInt()) {
                                if (virtualValues == null) {
                                    virtualValues = new ArrayMap(1);
                                }
                                SparseArray<AutofillValue> valuesByParent = (SparseArray) virtualValues.get(view);
                                if (valuesByParent == null) {
                                    valuesByParent = new SparseArray(5);
                                    virtualValues.put(view, valuesByParent);
                                }
                                valuesByParent.put(id.getVirtualChildIntId(), value);
                            } else {
                                if (this.mLastAutofilledData == null) {
                                    this.mLastAutofilledData = new ParcelableMap(itemCount - i);
                                }
                                this.mLastAutofilledData.put(id, value);
                                view.autofill(value);
                                setAutofilledIfValuesIs(view, value);
                                numApplied++;
                            }
                        }
                        i++;
                        int i2 = sessionId;
                        client = client2;
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
                list2 = ids;
                list = values;
                client2 = client;
                if (failedIds != null) {
                    if (Helper.sVerbose) {
                        String str2 = TAG;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("autofill(): total failed views: ");
                        stringBuilder.append(failedIds);
                        Log.v(str2, stringBuilder.toString());
                    }
                    try {
                        this.mService.setAutofillFailure(this.mSessionId, failedIds, this.mContext.getUserId());
                    } catch (RemoteException e) {
                        e.rethrowFromSystemServer();
                    }
                }
                if (virtualValues != null) {
                    for (int i3 = 0; i3 < virtualValues.size(); i3++) {
                        SparseArray childrenValues = (SparseArray) virtualValues.valueAt(i3);
                        ((View) virtualValues.keyAt(i3)).autofill(childrenValues);
                        numApplied += childrenValues.size();
                    }
                }
                this.mMetricsLogger.write(newLog(MetricsEvent.AUTOFILL_DATASET_APPLIED).addTaggedData(MetricsEvent.FIELD_AUTOFILL_NUM_VALUES, Integer.valueOf(itemCount)).addTaggedData(MetricsEvent.FIELD_AUTOFILL_NUM_VIEWS_FILLED, Integer.valueOf(numApplied)));
            } catch (Throwable th3) {
                th = th3;
                list2 = ids;
                list = values;
                throw th;
            }
        }
    }

    private LogMaker newLog(int category) {
        LogMaker log = new LogMaker(category).addTaggedData(MetricsEvent.FIELD_AUTOFILL_SESSION_ID, Integer.valueOf(this.mSessionId));
        if (isCompatibilityModeEnabledLocked()) {
            log.addTaggedData(MetricsEvent.FIELD_AUTOFILL_COMPAT_MODE, Integer.valueOf(1));
        }
        AutofillClient client = getClient();
        if (client == null) {
            log.setPackageName(this.mContext.getPackageName());
        } else {
            log.setComponentName(client.autofillClientGetComponentName());
        }
        return log;
    }

    private void setTrackedViews(int sessionId, AutofillId[] trackedIds, boolean saveOnAllViewsInvisible, boolean saveOnFinish, AutofillId[] fillableIds, AutofillId saveTriggerId) {
        if (saveTriggerId != null) {
            saveTriggerId.resetSessionId();
        }
        synchronized (this.mLock) {
            if (Helper.sVerbose) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("setTrackedViews(): sessionId=");
                stringBuilder.append(sessionId);
                stringBuilder.append(", trackedIds=");
                stringBuilder.append(Arrays.toString(trackedIds));
                stringBuilder.append(", saveOnAllViewsInvisible=");
                stringBuilder.append(saveOnAllViewsInvisible);
                stringBuilder.append(", saveOnFinish=");
                stringBuilder.append(saveOnFinish);
                stringBuilder.append(", fillableIds=");
                stringBuilder.append(Arrays.toString(fillableIds));
                stringBuilder.append(", saveTrigerId=");
                stringBuilder.append(saveTriggerId);
                stringBuilder.append(", mFillableIds=");
                stringBuilder.append(this.mFillableIds);
                stringBuilder.append(", mEnabled=");
                stringBuilder.append(this.mEnabled);
                stringBuilder.append(", mSessionId=");
                stringBuilder.append(this.mSessionId);
                Log.v(str, stringBuilder.toString());
            }
            if (this.mEnabled && this.mSessionId == sessionId) {
                if (saveOnAllViewsInvisible) {
                    this.mTrackedViews = new TrackedViews(trackedIds);
                } else {
                    this.mTrackedViews = null;
                }
                this.mSaveOnFinish = saveOnFinish;
                if (fillableIds != null) {
                    if (this.mFillableIds == null) {
                        this.mFillableIds = new ArraySet(fillableIds.length);
                    }
                    for (AutofillId id : fillableIds) {
                        id.resetSessionId();
                        this.mFillableIds.add(id);
                    }
                }
                if (!(this.mSaveTriggerId == null || this.mSaveTriggerId.equals(saveTriggerId))) {
                    setNotifyOnClickLocked(this.mSaveTriggerId, false);
                }
                if (!(saveTriggerId == null || saveTriggerId.equals(this.mSaveTriggerId))) {
                    this.mSaveTriggerId = saveTriggerId;
                    setNotifyOnClickLocked(this.mSaveTriggerId, true);
                }
            }
        }
    }

    private void setNotifyOnClickLocked(AutofillId id, boolean notify) {
        View view = findView(id);
        if (view == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("setNotifyOnClick(): invalid id: ");
            stringBuilder.append(id);
            Log.w(TAG, stringBuilder.toString());
            return;
        }
        view.setNotifyAutofillManagerOnClick(notify);
    }

    private void setSaveUiState(int sessionId, boolean shown) {
        if (Helper.sDebug) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("setSaveUiState(");
            stringBuilder.append(sessionId);
            stringBuilder.append("): ");
            stringBuilder.append(shown);
            Log.d(TAG, stringBuilder.toString());
        }
        synchronized (this.mLock) {
            if (this.mSessionId != Integer.MAX_VALUE) {
                String str = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("setSaveUiState(");
                stringBuilder2.append(sessionId);
                stringBuilder2.append(", ");
                stringBuilder2.append(shown);
                stringBuilder2.append(") called on existing session ");
                stringBuilder2.append(this.mSessionId);
                stringBuilder2.append("; cancelling it");
                Log.w(str, stringBuilder2.toString());
                cancelSessionLocked();
            }
            if (shown) {
                this.mSessionId = sessionId;
                this.mState = 3;
            } else {
                this.mSessionId = Integer.MAX_VALUE;
                this.mState = 0;
            }
        }
    }

    private void setSessionFinished(int newState, List<AutofillId> autofillableIds) {
        if (autofillableIds != null) {
            for (int i = 0; i < autofillableIds.size(); i++) {
                ((AutofillId) autofillableIds.get(i)).resetSessionId();
            }
        }
        synchronized (this.mLock) {
            if (Helper.sVerbose) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("setSessionFinished(): from ");
                stringBuilder.append(getStateAsStringLocked());
                stringBuilder.append(" to ");
                stringBuilder.append(getStateAsString(newState));
                stringBuilder.append("; autofillableIds=");
                stringBuilder.append(autofillableIds);
                Log.v(str, stringBuilder.toString());
            }
            if (autofillableIds != null) {
                this.mEnteredIds = new ArraySet((Collection) autofillableIds);
            }
            if (newState != 5) {
                if (newState != 6) {
                    resetSessionLocked(false);
                    this.mState = newState;
                }
            }
            resetSessionLocked(true);
            this.mState = 0;
        }
    }

    private void getAugmentedAutofillClient(IResultReceiver result) {
        synchronized (this.mLock) {
            if (this.mAugmentedAutofillServiceClient == null) {
                this.mAugmentedAutofillServiceClient = new AugmentedAutofillManagerClient();
            }
            Bundle resultData = new Bundle();
            resultData.putBinder(EXTRA_AUGMENTED_AUTOFILL_CLIENT, this.mAugmentedAutofillServiceClient.asBinder());
            try {
                result.send(0, resultData);
            } catch (RemoteException e) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Could not send AugmentedAutofillClient back: ");
                stringBuilder.append(e);
                Log.w(str, stringBuilder.toString());
            }
        }
    }

    public void requestHideFillUi() {
        requestHideFillUi(this.mIdShownFillUi, true);
    }

    private void requestHideFillUi(AutofillId id, boolean force) {
        View anchor = id == null ? null : findView(id);
        if (Helper.sVerbose) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("requestHideFillUi(");
            stringBuilder.append(id);
            stringBuilder.append("): anchor = ");
            stringBuilder.append(anchor);
            Log.v(TAG, stringBuilder.toString());
        }
        if (anchor == null) {
            if (force) {
                AutofillClient client = getClient();
                if (client != null) {
                    client.autofillClientRequestHideFillUi();
                }
            }
            return;
        }
        requestHideFillUi(id, anchor);
    }

    private void requestHideFillUi(AutofillId id, View anchor) {
        AutofillCallback callback = null;
        synchronized (this.mLock) {
            AutofillClient client = getClient();
            if (client != null && client.autofillClientRequestHideFillUi()) {
                this.mIdShownFillUi = null;
                callback = this.mCallback;
            }
        }
        if (callback == null) {
            return;
        }
        if (id.isVirtualInt()) {
            callback.onAutofillEvent(anchor, id.getVirtualChildIntId(), 2);
        } else {
            callback.onAutofillEvent(anchor, 2);
        }
    }

    private void notifyNoFillUi(int sessionId, AutofillId id, int sessionFinishedState) {
        if (Helper.sVerbose) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("notifyNoFillUi(): sessionId=");
            stringBuilder.append(sessionId);
            stringBuilder.append(", autofillId=");
            stringBuilder.append(id);
            stringBuilder.append(", sessionFinishedState=");
            stringBuilder.append(sessionFinishedState);
            Log.v(TAG, stringBuilder.toString());
        }
        View anchor = findView(id);
        if (anchor != null) {
            AutofillCallback callback = null;
            synchronized (this.mLock) {
                if (this.mSessionId == sessionId && getClient() != null) {
                    callback = this.mCallback;
                }
            }
            if (callback != null) {
                if (id.isVirtualInt()) {
                    callback.onAutofillEvent(anchor, id.getVirtualChildIntId(), 3);
                } else {
                    callback.onAutofillEvent(anchor, 3);
                }
            }
            if (sessionFinishedState != 0) {
                setSessionFinished(sessionFinishedState, null);
            }
        }
    }

    private View findView(AutofillId autofillId) {
        AutofillClient client = getClient();
        if (client != null) {
            return client.autofillClientFindViewByAutofillIdTraversal(autofillId);
        }
        return null;
    }

    public boolean hasAutofillFeature() {
        return this.mService != null;
    }

    public void onPendingSaveUi(int operation, IBinder token) {
        if (Helper.sVerbose) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onPendingSaveUi(");
            stringBuilder.append(operation);
            stringBuilder.append("): ");
            stringBuilder.append(token);
            Log.v(TAG, stringBuilder.toString());
        }
        synchronized (this.mLock) {
            try {
                this.mService.onPendingSaveUi(operation, token);
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
            }
        }
    }

    public void dump(String outerPrefix, PrintWriter pw) {
        pw.print(outerPrefix);
        pw.println("AutofillManager:");
        String pfx = new StringBuilder();
        pfx.append(outerPrefix);
        pfx.append("  ");
        pfx = pfx.toString();
        pw.print(pfx);
        pw.print("sessionId: ");
        pw.println(this.mSessionId);
        pw.print(pfx);
        pw.print("state: ");
        pw.println(getStateAsStringLocked());
        pw.print(pfx);
        pw.print("context: ");
        pw.println(this.mContext);
        AutofillClient client = getClient();
        if (client != null) {
            pw.print(pfx);
            pw.print("client: ");
            pw.print(client);
            pw.print(" (");
            pw.print(client.autofillClientGetActivityToken());
            pw.println(')');
        }
        pw.print(pfx);
        pw.print("enabled: ");
        pw.println(this.mEnabled);
        pw.print(pfx);
        pw.print("enabledAugmentedOnly: ");
        pw.println(this.mForAugmentedAutofillOnly);
        pw.print(pfx);
        pw.print("hasService: ");
        boolean z = true;
        pw.println(this.mService != null);
        pw.print(pfx);
        pw.print("hasCallback: ");
        if (this.mCallback == null) {
            z = false;
        }
        pw.println(z);
        pw.print(pfx);
        pw.print("onInvisibleCalled ");
        pw.println(this.mOnInvisibleCalled);
        pw.print(pfx);
        pw.print("last autofilled data: ");
        pw.println(this.mLastAutofilledData);
        pw.print(pfx);
        pw.print("id of last fill UI shown: ");
        pw.println(this.mIdShownFillUi);
        pw.print(pfx);
        pw.print("tracked views: ");
        if (this.mTrackedViews == null) {
            pw.println("null");
        } else {
            String pfx2 = new StringBuilder();
            pfx2.append(pfx);
            pfx2.append("  ");
            pfx2 = pfx2.toString();
            pw.println();
            pw.print(pfx2);
            pw.print("visible:");
            pw.println(this.mTrackedViews.mVisibleTrackedIds);
            pw.print(pfx2);
            pw.print("invisible:");
            pw.println(this.mTrackedViews.mInvisibleTrackedIds);
        }
        pw.print(pfx);
        pw.print("fillable ids: ");
        pw.println(this.mFillableIds);
        pw.print(pfx);
        pw.print("entered ids: ");
        pw.println(this.mEnteredIds);
        if (this.mEnteredForAugmentedAutofillIds != null) {
            pw.print(pfx);
            pw.print("entered ids for augmented autofill: ");
            pw.println(this.mEnteredForAugmentedAutofillIds);
        }
        if (this.mForAugmentedAutofillOnly) {
            pw.print(pfx);
            pw.println("For Augmented Autofill Only");
        }
        pw.print(pfx);
        pw.print("save trigger id: ");
        pw.println(this.mSaveTriggerId);
        pw.print(pfx);
        pw.print("save on finish(): ");
        pw.println(this.mSaveOnFinish);
        if (this.mOptions != null) {
            pw.print(pfx);
            pw.print("options: ");
            this.mOptions.dumpShort(pw);
            pw.println();
        }
        pw.print(pfx);
        pw.print("compat mode enabled: ");
        synchronized (this.mLock) {
            if (this.mCompatibilityBridge != null) {
                String pfx22 = new StringBuilder();
                pfx22.append(pfx);
                pfx22.append("  ");
                pfx22 = pfx22.toString();
                pw.println("true");
                pw.print(pfx22);
                pw.print("windowId: ");
                pw.println(this.mCompatibilityBridge.mFocusedWindowId);
                pw.print(pfx22);
                pw.print("nodeId: ");
                pw.println(this.mCompatibilityBridge.mFocusedNodeId);
                pw.print(pfx22);
                pw.print("virtualId: ");
                pw.println(AccessibilityNodeInfo.getVirtualDescendantId(this.mCompatibilityBridge.mFocusedNodeId));
                pw.print(pfx22);
                pw.print("focusedBounds: ");
                pw.println(this.mCompatibilityBridge.mFocusedBounds);
            } else {
                pw.println("false");
            }
        }
        pw.print(pfx);
        pw.print("debug: ");
        pw.print(Helper.sDebug);
        pw.print(" verbose: ");
        pw.println(Helper.sVerbose);
    }

    @GuardedBy({"mLock"})
    private String getStateAsStringLocked() {
        return getStateAsString(this.mState);
    }

    private static String getStateAsString(int state) {
        switch (state) {
            case 0:
                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
            case 1:
                return "ACTIVE";
            case 2:
                return "FINISHED";
            case 3:
                return "SHOWING_SAVE_UI";
            case 4:
                return "DISABLED_BY_SERVICE";
            case 5:
                return "UNKNOWN_COMPAT_MODE";
            case 6:
                return "UNKNOWN_FAILED";
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("INVALID:");
                stringBuilder.append(state);
                return stringBuilder.toString();
        }
    }

    public static String getSmartSuggestionModeToString(int flags) {
        if (flags == 0) {
            return "OFF";
        }
        if (flags == 1) {
            return "SYSTEM";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INVALID:");
        stringBuilder.append(flags);
        return stringBuilder.toString();
    }

    @GuardedBy({"mLock"})
    private boolean isActiveLocked() {
        return this.mState == 1;
    }

    @GuardedBy({"mLock"})
    private boolean isDisabledByServiceLocked() {
        return this.mState == 4;
    }

    @GuardedBy({"mLock"})
    private boolean isFinishedLocked() {
        return this.mState == 2;
    }

    private void post(Runnable runnable) {
        AutofillClient client = getClient();
        if (client == null) {
            if (Helper.sVerbose) {
                Log.v(TAG, "ignoring post() because client is null");
            }
            return;
        }
        client.autofillClientRunOnUiThread(runnable);
    }
}
