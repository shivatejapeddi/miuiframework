package miui.securitycenter.net;

import android.content.Context;
import android.net.INetworkStatsService;
import android.net.INetworkStatsService.Stub;
import android.net.INetworkStatsSession;
import android.net.NetworkStats;
import android.net.NetworkStatsHistory;
import android.net.NetworkStatsHistory.Entry;
import android.net.NetworkTemplate;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.SparseArray;
import java.util.HashMap;
import java.util.Map;

public class MiuiNetworkSessionStats {
    private INetworkStatsService mStatsService = Stub.asInterface(ServiceManager.getService(Context.NETWORK_STATS_SERVICE));
    private INetworkStatsSession mStatsSession;

    public void openSession() {
        try {
            this.mStatsSession = this.mStatsService.openSession();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeSession() {
        INetworkStatsSession iNetworkStatsSession = this.mStatsSession;
        if (iNetworkStatsSession != null) {
            try {
                iNetworkStatsSession.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception e) {
            }
        }
    }

    public void forceUpdate() {
        try {
            this.mStatsService.forceUpdate();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public long[] getWifiHistoryForUid(int uid, long beginTime, long endTime) {
        long[] total = new long[2];
        try {
            NetworkTemplate wifiTemplate = buildTemplateWifiWildcard();
            if (wifiTemplate == null) {
                return total;
            }
            total = getHistoryStats(getHistoryForUid(wifiTemplate, uid, -1, 0, 10), beginTime, endTime);
            return total;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public long[] getMobileHistoryForUid(String imsi, int uid, long beginTime, long endTime) {
        long[] total = new long[2];
        try {
            NetworkTemplate mobileTemplate = buildTemplateMobileAll(imsi);
            if (mobileTemplate == null) {
                return total;
            }
            total = getHistoryStats(getHistoryForUid(mobileTemplate, uid, -1, 0, 10), beginTime, endTime);
            return total;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public long getNetworkWifiTotalBytes(long beginTime, long endTime) {
        try {
            NetworkTemplate wifiTemplate = buildTemplateWifiWildcard();
            if (wifiTemplate != null) {
                return getNetworkTotalBytes(wifiTemplate, beginTime, endTime);
            }
            return 0;
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long getNetworkMobileTotalBytes(String imsi, long beginTime, long endTime) {
        try {
            NetworkTemplate mobileTemplate = buildTemplateMobileAll(imsi);
            if (mobileTemplate != null) {
                return getNetworkTotalBytes(mobileTemplate, beginTime, endTime);
            }
            return 0;
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private long[] getHistoryStats(NetworkStatsHistory networkHistory, long beginTime, long endTime) {
        long[] total = new long[2];
        Entry statsEntry = new Entry();
        if (networkHistory != null) {
            Entry entry = networkHistory.getValues(beginTime, endTime, statsEntry);
            if (entry.rxBytes >= 0) {
                total[0] = entry.rxBytes;
            } else {
                total[0] = 0;
            }
            if (entry.txBytes >= 0) {
                total[1] = entry.txBytes;
            } else {
                total[1] = 0;
            }
        }
        return total;
    }

    public SparseArray<Map<String, Long>> getMobileSummaryForAllUid(String imsi, long beginTime, long endTime) {
        return buildNetworkStatsMap(buildTemplateMobileAll(imsi), beginTime, endTime);
    }

    public SparseArray<Map<String, Long>> getWifiSummaryForAllUid(long beginTime, long endTime) {
        return buildNetworkStatsMap(buildTemplateWifiWildcard(), beginTime, endTime);
    }

    private SparseArray<Map<String, Long>> buildNetworkStatsMap(NetworkTemplate networkTemplate, long beginTime, long endTime) {
        NetworkStats networkStats = null;
        try {
            if (this.mStatsSession != null) {
                if (networkTemplate != null) {
                    networkStats = getSummaryForAllUid(networkTemplate, beginTime, endTime, false);
                    NetworkStats networkStats2;
                    if (networkStats == null) {
                        networkStats2 = networkStats;
                    } else if (networkStats.size() == 0) {
                        networkStats2 = networkStats;
                    } else {
                        SparseArray<Map<String, Long>> networkStatsMap = new SparseArray(255);
                        NetworkStats.Entry statsEntry = new NetworkStats.Entry();
                        int size = networkStats.size();
                        int i = 0;
                        while (i < size) {
                            NetworkStats.Entry statsEntry2;
                            int size2;
                            NetworkStats.Entry entry = networkStats.getValues(i, statsEntry);
                            if (entry == null) {
                                networkStats2 = networkStats;
                                statsEntry2 = statsEntry;
                                size2 = size;
                            } else {
                                int uid = entry.uid;
                                Map<String, Long> statsInfo = (Map) networkStatsMap.get(uid);
                                if (statsInfo == null) {
                                    statsInfo = buildStatsMap();
                                    networkStatsMap.put(uid, statsInfo);
                                }
                                if (entry.set == 1) {
                                    String str = "txForegroundBytes";
                                    statsInfo.put(str, Long.valueOf(((Long) statsInfo.get(str)).longValue() + entry.txBytes));
                                    str = "rxForegroundBytes";
                                    networkStats2 = networkStats;
                                    statsEntry2 = statsEntry;
                                    statsInfo.put(str, Long.valueOf(((Long) statsInfo.get(str)).longValue() + entry.rxBytes));
                                } else {
                                    networkStats2 = networkStats;
                                    statsEntry2 = statsEntry;
                                }
                                String str2 = "txBytes";
                                statsInfo.put(str2, Long.valueOf(((Long) statsInfo.get(str2)).longValue() + entry.txBytes));
                                str2 = "rxBytes";
                                size2 = size;
                                statsInfo.put(str2, Long.valueOf(((Long) statsInfo.get(str2)).longValue() + entry.rxBytes));
                            }
                            i++;
                            size = size2;
                            networkStats = networkStats2;
                            statsEntry = statsEntry2;
                        }
                        return networkStatsMap;
                    }
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Long> buildStatsMap() {
        Map<String, Long> statsMap = new HashMap();
        Long valueOf = Long.valueOf(0);
        statsMap.put("rxForegroundBytes", valueOf);
        statsMap.put("txForegroundBytes", valueOf);
        statsMap.put("txBytes", valueOf);
        statsMap.put("rxBytes", valueOf);
        return statsMap;
    }

    private NetworkStats getSummaryForAllUid(NetworkTemplate template, long start, long end, boolean includeTags) throws RemoteException {
        return this.mStatsSession.getSummaryForAllUid(template, start, end, includeTags);
    }

    private NetworkStatsHistory getHistoryForUid(NetworkTemplate template, int uid, int set, int tag, int fields) throws RemoteException {
        return this.mStatsSession.getHistoryForUid(template, uid, set, tag, fields);
    }

    private long getNetworkTotalBytes(NetworkTemplate networkTemplate, long beginTime, long endTime) throws RemoteException {
        return this.mStatsSession.getSummaryForNetwork(networkTemplate, beginTime, endTime).getTotalBytes();
    }

    private NetworkTemplate buildTemplateMobileAll(String imsi) {
        if (TextUtils.isEmpty(imsi)) {
            return null;
        }
        return NetworkTemplate.buildTemplateMobileAll(imsi);
    }

    private NetworkTemplate buildTemplateWifiWildcard() {
        return NetworkTemplate.buildTemplateWifiWildcard();
    }
}
