package miui.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.miui.R;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.SubscriptionManager.OnSubscriptionsChangedListener;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import miui.util.AppConstants;

public class SubscriptionManagerEx extends SubscriptionManager {
    public static final String ACTION_DEFAULT_DATA_SLOT_CHANGED = "miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED";
    public static final String ACTION_DEFAULT_DATA_SLOT_READY = "miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY";
    private static final String ACTION_UICC_MANUAL_PROVISION_STATUS_CHANGED = "org.codeaurora.intent.action.ACTION_UICC_MANUAL_PROVISION_STATUS_CHANGED";
    static final String DEFAULT_DATA_SLOT_PROPERTY = "persist.radio.default.data";
    static final String DEFAULT_VOICE_SLOT_PROPERTY = "persist.radio.default.voice";
    public static final String KEY_OLD_DATA_SLOT = "old_data_slot";
    public static final String KEY_SIM_INSERT_STATE_ARRAY = "sim_insert_state_array";
    public static final int SIM_CHANGED = 4;
    public static final int SIM_NEW_CARD = 2;
    public static final int SIM_NO_CARD = 1;
    public static final int SIM_NO_CHANGE = 0;
    public static final int SIM_REMOVED = 3;
    private static final Comparator<SubscriptionInfo> SUBSCRIPTION_INFO_COMPARATOR = -$$Lambda$SubscriptionManagerEx$nWnxymqBl7xu3TtQYhcHLhZZdf0.INSTANCE;
    private AtomicBoolean mReceiverRegistered;
    private BroadcastReceiver mSubscriptionChangedReceiver;
    private OnSubscriptionsChangedListener mSubscriptionListener;

    static class ConstantsDefiner {
        private static final String PHONE_ID = "phone_id";
        private static final String SLOT_ID = "slot_id";
        private static final String SUBSCRIPTION_ID = "subscription_id";

        private ConstantsDefiner() {
        }

        static int getInvalidSubscriptionIdConstant() {
            return -1;
        }

        static int getInvalidPhoneIdConstant() {
            return -1;
        }

        static int getInvalidSlotIdConstant() {
            return -1;
        }

        static int getDefaultSubscriptionIdConstant() {
            return Integer.MAX_VALUE;
        }

        static int getDefaultPhoneIdConstant() {
            return Integer.MAX_VALUE;
        }

        static int getDefaultSlotIdConstant() {
            return Integer.MAX_VALUE;
        }

        static String getSubscriptionKeyConstant() {
            return "subscription_id";
        }

        static String getPhoneKeyConstant() {
            return PHONE_ID;
        }

        static String getSlotKeyConstant() {
            return SLOT_ID;
        }
    }

    static class Holder {
        static final Context CONTEXT = AppConstants.getCurrentApplication();
        static final SubscriptionManagerEx INSTANCE = new SubscriptionManagerEx();
        static final SubscriptionManager SUBSCRIPTION_MANAGER = SubscriptionManager.from(CONTEXT);

        private Holder() {
        }
    }

    static class SubscriptionInfoImpl extends SubscriptionInfo {
        int mSlotId;
        private final SubscriptionInfo mSubInfo;
        private final int mSubscriptionId;

        private SubscriptionInfoImpl(SubscriptionInfo si) {
            this.mSubscriptionId = SubscriptionManager.isValidSubscriptionId(si.getSubscriptionId()) ? si.getSubscriptionId() : SubscriptionManager.INVALID_SUBSCRIPTION_ID;
            this.mSlotId = SubscriptionManager.isValidSlotId(si.getSimSlotIndex()) ? si.getSimSlotIndex() : SubscriptionManager.INVALID_SLOT_ID;
            this.mSubInfo = si;
        }

        public static SubscriptionInfo from(SubscriptionInfo sir) {
            return sir == null ? null : new SubscriptionInfoImpl(sir);
        }

        public static List<SubscriptionInfo> from(List<SubscriptionInfo> sis) {
            if (sis == null) {
                return new ArrayList();
            }
            List<SubscriptionInfo> rets = new ArrayList();
            for (int i = 0; i < sis.size(); i++) {
                rets.add(i, from((SubscriptionInfo) sis.get(i)));
            }
            return rets;
        }

        public int getSubscriptionId() {
            return this.mSubscriptionId;
        }

        public String getIccId() {
            return this.mSubInfo.getIccId();
        }

        public int getSlotId() {
            return this.mSlotId;
        }

        public int getPhoneId() {
            return this.mSlotId;
        }

        public CharSequence getDisplayName() {
            return this.mSubInfo.getNameSource() == 2 ? this.mSubInfo.getDisplayName() : getDefaultDisplayName();
        }

        public String getDisplayNumber() {
            return this.mSubInfo.getNumber();
        }

        public boolean isActivated() {
            if (this.mSlotId == SubscriptionManager.INVALID_SLOT_ID || !TelephonyManager.getDefault().isMultiSimEnabled()) {
                return true;
            }
            try {
                return TelephonyManagerEx.getDefault().getMiuiTelephony().isIccCardActivate(this.mSlotId);
            } catch (Exception e) {
                return true;
            }
        }

        private String getDefaultDisplayName() {
            String displayName = "";
            if (this.mSlotId == SubscriptionManager.INVALID_SLOT_ID) {
                return displayName;
            }
            try {
                displayName = TelephonyManagerEx.getDefault().getMiuiTelephony().getSpn(TelephonyManager.getDefault().getSimOperatorForSlot(this.mSlotId), this.mSlotId, TelephonyManager.getDefault().getSimOperatorNameForSlot(this.mSlotId), true);
            } catch (Exception e) {
            }
            if (!TextUtils.isEmpty(displayName)) {
                return displayName;
            }
            return Holder.CONTEXT.getString(R.string.subinfo_default_name, Integer.valueOf(this.mSlotId + 1));
        }

        public int getMcc() {
            return this.mSubInfo.getMcc();
        }

        public int getMnc() {
            return this.mSubInfo.getMnc();
        }
    }

    /* synthetic */ SubscriptionManagerEx(AnonymousClass1 x0) {
        this();
    }

    static /* synthetic */ int lambda$static$0(SubscriptionInfo arg0, SubscriptionInfo arg1) {
        int flag = arg0.getSimSlotIndex() - arg1.getSimSlotIndex();
        if (flag == 0) {
            return arg0.getSubscriptionId() - arg1.getSubscriptionId();
        }
        return flag;
    }

    private SubscriptionManagerEx() {
        this.mReceiverRegistered = new AtomicBoolean(false);
        this.mSubscriptionChangedReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (SubscriptionManagerEx.ACTION_UICC_MANUAL_PROVISION_STATUS_CHANGED.equals(intent.getAction())) {
                    SubscriptionManagerEx.this.onSubscriptionInfoChanged();
                }
            }
        };
    }

    public static SubscriptionManagerEx getDefault() {
        return Holder.INSTANCE;
    }

    private void initSubscriptionListener(boolean checkLooper) {
        if (this.mSubscriptionListener == null) {
            if (!checkLooper || Looper.myLooper() == Looper.getMainLooper()) {
                this.mSubscriptionListener = new OnSubscriptionsChangedListener() {
                    public void onSubscriptionsChanged() {
                        SubscriptionManagerEx.this.onSubscriptionInfoChanged();
                    }
                };
                SubscriptionManager.from(Holder.CONTEXT).addOnSubscriptionsChangedListener(this.mSubscriptionListener);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("initSubscriptionListener failed for pkg=");
            stringBuilder.append(Holder.CONTEXT.getOpPackageName());
            stringBuilder.append(" threadName=");
            stringBuilder.append(Thread.currentThread().getName());
            Rlog.i("SubMgr", stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: protected */
    public void addOnSubscriptionsChangedListenerInternal() {
        if (this.mReceiverRegistered.compareAndSet(false, true)) {
            initSubscriptionListener(true);
            if (this.mSubscriptionListener == null) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("initSubscriptionListener in main Thread for pkg=");
                        stringBuilder.append(Holder.CONTEXT.getOpPackageName());
                        Rlog.i("SubMgr", stringBuilder.toString());
                        SubscriptionManagerEx.this.initSubscriptionListener(false);
                    }
                });
            }
            if ("qcom".equals(Build.HARDWARE)) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(ACTION_UICC_MANUAL_PROVISION_STATUS_CHANGED);
                Holder.CONTEXT.registerReceiver(this.mSubscriptionChangedReceiver, filter);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void removeOnSubscriptionsChangedListenerInternal() {
        this.mReceiverRegistered.set(false);
        if (this.mSubscriptionListener != null) {
            SubscriptionManager.from(Holder.CONTEXT).removeOnSubscriptionsChangedListener(this.mSubscriptionListener);
            this.mSubscriptionListener = null;
        }
        if (this.mSubscriptionChangedReceiver != null) {
            if ("qcom".equals(Build.HARDWARE)) {
                try {
                    Holder.CONTEXT.unregisterReceiver(this.mSubscriptionChangedReceiver);
                } catch (Exception e) {
                    Rlog.i("SubMgr", "unregister SubscriptionChangedReceiver error!!!");
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public List<SubscriptionInfo> getAllSubscriptionInfoListInternal() {
        return SubscriptionInfoImpl.from(Holder.SUBSCRIPTION_MANAGER.getAllSubscriptionInfoList());
    }

    /* Access modifiers changed, original: protected */
    public List<SubscriptionInfo> getSubscriptionInfoListInternal() {
        List infos = Holder.SUBSCRIPTION_MANAGER.getActiveSubscriptionInfoList();
        if (infos != null) {
            infos.sort(SUBSCRIPTION_INFO_COMPARATOR);
        }
        return SubscriptionInfoImpl.from(infos);
    }

    public int setDisplayNameForSubscription(String displayName, int subId) {
        if (!isValidSubscriptionId(subId)) {
            return 0;
        }
        if (subId == DEFAULT_SUBSCRIPTION_ID) {
            return setDisplayNameForSlot(displayName, getDefaultSlotId());
        }
        return Holder.SUBSCRIPTION_MANAGER.setDisplayName(displayName, subId, 2);
    }

    public int setDisplayNameForSlot(String displayName, int slotId) {
        if (!isValidSlotId(slotId)) {
            return 0;
        }
        if (slotId == DEFAULT_SLOT_ID) {
            return setDisplayNameForSubscription(displayName, getSubscriptionIdForSlot(getDefaultSlotId()));
        }
        return setDisplayNameForSubscription(displayName, getSubscriptionIdForSlot(slotId));
    }

    public int setDisplayNumberForSubscription(String number, int subId) {
        if (!isValidSubscriptionId(subId)) {
            return 0;
        }
        if (subId == DEFAULT_SUBSCRIPTION_ID) {
            return setDisplayNumberForSlot(number, getDefaultSlotId());
        }
        return Holder.SUBSCRIPTION_MANAGER.setDisplayNumber(number, subId);
    }

    public int setDisplayNumberForSlot(String number, int slotId) {
        if (!isValidSlotId(slotId)) {
            return 0;
        }
        if (slotId == DEFAULT_SLOT_ID) {
            return setDisplayNameForSubscription(number, getSubscriptionIdForSlot(getDefaultSlotId()));
        }
        return setDisplayNameForSubscription(number, getSubscriptionIdForSlot(slotId));
    }

    /* Access modifiers changed, original: protected */
    public int getDefaultSlotIdInternal() {
        try {
            return getMiuiTelephony().getSystemDefaultSlotId();
        } catch (Exception e) {
            return 0;
        }
    }

    /* Access modifiers changed, original: protected */
    public int getSlotId(int subId) {
        long identity = Binder.clearCallingIdentity();
        try {
            for (SubscriptionInfo subscriptionInfo : getSubscriptionInfoList()) {
                if (subscriptionInfo.getSubscriptionId() == subId) {
                    int slotId = subscriptionInfo.getSlotId();
                    return slotId;
                }
            }
            Binder.restoreCallingIdentity(identity);
            return SubscriptionManager.getPhoneId(subId);
        } finally {
            Binder.restoreCallingIdentity(identity);
        }
    }

    public int getSubscriptionIdForSlot(int slotId) {
        if (!isValidSlotId(slotId)) {
            return INVALID_SUBSCRIPTION_ID;
        }
        if (slotId == DEFAULT_SLOT_ID) {
            return DEFAULT_SUBSCRIPTION_ID;
        }
        long identity = Binder.clearCallingIdentity();
        try {
            for (SubscriptionInfo subscriptionInfo : getSubscriptionInfoList()) {
                if (subscriptionInfo.getSlotId() == slotId) {
                    int subscriptionId = subscriptionInfo.getSubscriptionId();
                    return subscriptionId;
                }
            }
            Binder.restoreCallingIdentity(identity);
            int[] subIds = SubscriptionManager.getSubId(slotId);
            int i = (subIds == null || subIds.length <= 0) ? INVALID_SUBSCRIPTION_ID : subIds[0];
            return i;
        } finally {
            Binder.restoreCallingIdentity(identity);
        }
    }

    public int getDefaultVoiceSubscriptionId() {
        int subId = SubscriptionManager.getDefaultVoiceSubscriptionId();
        return !isValidSubscriptionId(subId) ? INVALID_SUBSCRIPTION_ID : subId;
    }

    public SubscriptionInfo getDefaultVoiceSubscriptionInfo() {
        return SubscriptionInfoImpl.from(Holder.SUBSCRIPTION_MANAGER.getDefaultVoiceSubscriptionInfo());
    }

    public int getDefaultVoiceSlotId() {
        return SystemProperties.getInt(DEFAULT_VOICE_SLOT_PROPERTY, INVALID_SLOT_ID);
    }

    public void setDefaultVoiceSlotId(int slotId) {
        if (slotId != DEFAULT_SLOT_ID) {
            try {
                getMiuiTelephony().setDefaultVoiceSlotId(isValidSlotId(slotId) ? slotId : INVALID_SLOT_ID, Holder.CONTEXT.getOpPackageName());
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to set default voice slot id ");
                stringBuilder.append(slotId);
                stringBuilder.append(" - ");
                Rlog.e("SubMgr", stringBuilder.toString(), e);
            }
        }
    }

    public int getDefaultDataSubscriptionId() {
        int subId = SubscriptionManager.getDefaultDataSubscriptionId();
        if (subId == INVALID_SUBSCRIPTION_ID) {
            return getSubscriptionIdForSlot(getDefaultSlotIdInternal());
        }
        return subId;
    }

    public SubscriptionInfo getDefaultDataSubscriptionInfo() {
        return SubscriptionInfoImpl.from(Holder.SUBSCRIPTION_MANAGER.getDefaultDataSubscriptionInfo());
    }

    public int getDefaultDataSlotId() {
        int ret = SystemProperties.getInt(DEFAULT_DATA_SLOT_PROPERTY, INVALID_SLOT_ID);
        return ret == INVALID_SLOT_ID ? getDefaultSlotIdInternal() : ret;
    }

    public void setDefaultDataSlotId(int slotId) {
        if (isValidSlotId(slotId) && slotId != DEFAULT_SLOT_ID) {
            try {
                getMiuiTelephony().setDefaultDataSlotId(slotId, Holder.CONTEXT.getOpPackageName());
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to set default data slot id ");
                stringBuilder.append(slotId);
                stringBuilder.append(" - ");
                Rlog.e("SubMgr", stringBuilder.toString(), e);
            }
        }
    }

    public int getDefaultSmsSubscriptionId() {
        int subId = SubscriptionManager.getDefaultSmsSubscriptionId();
        return !isValidSubscriptionId(subId) ? INVALID_SUBSCRIPTION_ID : subId;
    }

    public SubscriptionInfo getDefaultSmsSubscriptionInfo() {
        return SubscriptionInfoImpl.from(Holder.SUBSCRIPTION_MANAGER.getDefaultSmsSubscriptionInfo());
    }

    public void setDefaultSmsSubscriptionId(int subId) {
        int settingSubId = isValidSubscriptionId(subId) ? subId : INVALID_SUBSCRIPTION_ID;
        if (settingSubId != DEFAULT_SUBSCRIPTION_ID && settingSubId != getDefaultSmsSubscriptionId()) {
            Holder.SUBSCRIPTION_MANAGER.setDefaultSmsSubId(settingSubId);
        }
    }

    private IMiuiTelephony getMiuiTelephony() {
        return TelephonyManagerEx.getDefault().getMiuiTelephony();
    }
}
