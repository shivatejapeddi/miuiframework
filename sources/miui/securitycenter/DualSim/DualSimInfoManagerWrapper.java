package miui.securitycenter.DualSim;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.SubscriptionManager.OnSubscriptionsChangedListener;

public class DualSimInfoManagerWrapper {
    private static ArrayList<ISimInfoChangeWrapperListener> mListeners = new ArrayList();
    private static SimInfoChangeImpl mSimInfoChangeImpl = new SimInfoChangeImpl();

    public interface ISimInfoChangeWrapperListener {
        void onSubscriptionsChanged();
    }

    private static class SimInfoChangeImpl implements OnSubscriptionsChangedListener {
        private SimInfoChangeImpl() {
        }

        public void onSubscriptionsChanged() {
            DualSimInfoManagerWrapper.broadcastSubscriptionsChanged();
        }
    }

    private static final class SubscriptionInfoComparable implements Comparator<SubscriptionInfo> {
        private SubscriptionInfoComparable() {
        }

        public int compare(SubscriptionInfo sim1, SubscriptionInfo sim2) {
            return sim1.getSlotId() - sim2.getSlotId();
        }
    }

    public static void registerSimInfoChangeListener(Context context, ISimInfoChangeWrapperListener listener) {
        synchronized (mListeners) {
            if (listener != null) {
                if (!mListeners.contains(listener)) {
                    mListeners.add(listener);
                    registerChangeListener(mSimInfoChangeImpl);
                }
            }
        }
    }

    public static void unRegisterSimInfoChangeListener(Context context, ISimInfoChangeWrapperListener listener) {
        synchronized (mListeners) {
            if (listener != null) {
                if (mListeners.contains(listener)) {
                    mListeners.remove(listener);
                    unRegisterChangeListener(mSimInfoChangeImpl);
                }
            }
        }
    }

    private static void broadcastSubscriptionsChanged() {
        synchronized (mListeners) {
            Iterator it = mListeners.iterator();
            while (it.hasNext()) {
                ((ISimInfoChangeWrapperListener) it.next()).onSubscriptionsChanged();
            }
        }
    }

    private DualSimInfoManagerWrapper() {
    }

    private static void registerChangeListener(SimInfoChangeImpl listener) {
        SubscriptionManager.getDefault().addOnSubscriptionsChangedListener(listener);
    }

    private static void unRegisterChangeListener(SimInfoChangeImpl listener) {
        SubscriptionManager.getDefault().removeOnSubscriptionsChangedListener(listener);
    }

    public static List<Map<String, String>> getSimInfoList(Context context) {
        try {
            List<SubscriptionInfo> siminfoList = SubscriptionManager.getDefault().getSubscriptionInfoList();
            if (siminfoList != null) {
                if (siminfoList.size() != 0) {
                    if (siminfoList.size() > 0) {
                        Collections.sort(siminfoList, new SubscriptionInfoComparable());
                    }
                    List<Map<String, String>> simInfos = new ArrayList();
                    for (SubscriptionInfo info : siminfoList) {
                        if (info.isActivated()) {
                            Map<String, String> simInfo = new HashMap();
                            simInfo.put("simId", String.valueOf(info.getSubscriptionId()));
                            simInfo.put("slotNum", String.valueOf(info.getSlotId()));
                            simInfo.put("simName", info.getDisplayName().toString());
                            simInfo.put("iccId", info.getIccId());
                            simInfos.add(simInfo);
                        }
                    }
                    return simInfos;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
