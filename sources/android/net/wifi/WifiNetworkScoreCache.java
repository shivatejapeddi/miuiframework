package android.net.wifi;

import android.Manifest.permission;
import android.content.Context;
import android.net.INetworkScoreCache.Stub;
import android.net.NetworkKey;
import android.net.ScoredNetwork;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import android.util.LruCache;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public class WifiNetworkScoreCache extends Stub {
    private static final boolean DBG = Log.isLoggable(TAG, 3);
    private static final int DEFAULT_MAX_CACHE_SIZE = 100;
    public static final int INVALID_NETWORK_SCORE = -128;
    private static final String TAG = "WifiNetworkScoreCache";
    @GuardedBy({"mLock"})
    private final LruCache<String, ScoredNetwork> mCache;
    private final Context mContext;
    @GuardedBy({"mLock"})
    private CacheListener mListener;
    private final Object mLock;

    public static abstract class CacheListener {
        private Handler mHandler;

        public abstract void networkCacheUpdated(List<ScoredNetwork> list);

        public CacheListener(Handler handler) {
            Preconditions.checkNotNull(handler);
            this.mHandler = handler;
        }

        /* Access modifiers changed, original: 0000 */
        public void post(final List<ScoredNetwork> updatedNetworks) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    CacheListener.this.networkCacheUpdated(updatedNetworks);
                }
            });
        }
    }

    public WifiNetworkScoreCache(Context context) {
        this(context, null);
    }

    public WifiNetworkScoreCache(Context context, CacheListener listener) {
        this(context, listener, 100);
    }

    public WifiNetworkScoreCache(Context context, CacheListener listener, int maxCacheSize) {
        this.mLock = new Object();
        this.mContext = context.getApplicationContext();
        this.mListener = listener;
        this.mCache = new LruCache(maxCacheSize);
    }

    public final void updateScores(List<ScoredNetwork> networks) {
        if (networks != null && !networks.isEmpty()) {
            if (DBG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("updateScores list size=");
                stringBuilder.append(networks.size());
                Log.d(TAG, stringBuilder.toString());
            }
            boolean changed = false;
            synchronized (this.mLock) {
                for (ScoredNetwork network : networks) {
                    String networkKey = buildNetworkKey(network);
                    if (networkKey != null) {
                        this.mCache.put(networkKey, network);
                        changed = true;
                    } else if (DBG) {
                        String str = TAG;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Failed to build network key for ScoredNetwork");
                        stringBuilder2.append(network);
                        Log.d(str, stringBuilder2.toString());
                    }
                }
                if (this.mListener != null && changed) {
                    this.mListener.post(networks);
                }
            }
        }
    }

    public final void clearScores() {
        synchronized (this.mLock) {
            this.mCache.evictAll();
        }
    }

    public boolean isScoredNetwork(ScanResult result) {
        return getScoredNetwork(result) != null;
    }

    public boolean hasScoreCurve(ScanResult result) {
        ScoredNetwork network = getScoredNetwork(result);
        return (network == null || network.rssiCurve == null) ? false : true;
    }

    public int getNetworkScore(ScanResult result) {
        int score = -128;
        ScoredNetwork network = getScoredNetwork(result);
        if (!(network == null || network.rssiCurve == null)) {
            score = network.rssiCurve.lookupScore(result.level);
            if (DBG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("getNetworkScore found scored network ");
                stringBuilder.append(network.networkKey);
                stringBuilder.append(" score ");
                stringBuilder.append(Integer.toString(score));
                stringBuilder.append(" RSSI ");
                stringBuilder.append(result.level);
                Log.d(TAG, stringBuilder.toString());
            }
        }
        return score;
    }

    public boolean getMeteredHint(ScanResult result) {
        ScoredNetwork network = getScoredNetwork(result);
        return network != null && network.meteredHint;
    }

    public int getNetworkScore(ScanResult result, boolean isActiveNetwork) {
        int score = -128;
        ScoredNetwork network = getScoredNetwork(result);
        if (!(network == null || network.rssiCurve == null)) {
            score = network.rssiCurve.lookupScore(result.level, isActiveNetwork);
            if (DBG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("getNetworkScore found scored network ");
                stringBuilder.append(network.networkKey);
                stringBuilder.append(" score ");
                stringBuilder.append(Integer.toString(score));
                stringBuilder.append(" RSSI ");
                stringBuilder.append(result.level);
                stringBuilder.append(" isActiveNetwork ");
                stringBuilder.append(isActiveNetwork);
                Log.d(TAG, stringBuilder.toString());
            }
        }
        return score;
    }

    public ScoredNetwork getScoredNetwork(ScanResult result) {
        String key = buildNetworkKey(result);
        if (key == null) {
            return null;
        }
        ScoredNetwork network;
        synchronized (this.mLock) {
            network = (ScoredNetwork) this.mCache.get(key);
        }
        return network;
    }

    public ScoredNetwork getScoredNetwork(NetworkKey networkKey) {
        String key = buildNetworkKey(networkKey);
        if (key == null) {
            if (DBG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Could not build key string for Network Key: ");
                stringBuilder.append(networkKey);
                Log.d(TAG, stringBuilder.toString());
            }
            return null;
        }
        ScoredNetwork scoredNetwork;
        synchronized (this.mLock) {
            scoredNetwork = (ScoredNetwork) this.mCache.get(key);
        }
        return scoredNetwork;
    }

    private String buildNetworkKey(ScoredNetwork network) {
        if (network == null) {
            return null;
        }
        return buildNetworkKey(network.networkKey);
    }

    private String buildNetworkKey(NetworkKey networkKey) {
        if (networkKey == null || networkKey.wifiKey == null || networkKey.type != 1) {
            return null;
        }
        String key = networkKey.wifiKey.ssid;
        if (key == null) {
            return null;
        }
        if (networkKey.wifiKey.bssid != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(key);
            stringBuilder.append(networkKey.wifiKey.bssid);
            key = stringBuilder.toString();
        }
        return key;
    }

    private String buildNetworkKey(ScanResult result) {
        if (result == null || result.SSID == null) {
            return null;
        }
        String str = "\"";
        StringBuilder key = new StringBuilder(str);
        key.append(result.SSID);
        key.append(str);
        if (result.BSSID != null) {
            key.append(result.BSSID);
        }
        return key.toString();
    }

    /* Access modifiers changed, original: protected|final */
    public final void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        this.mContext.enforceCallingOrSelfPermission(permission.DUMP, TAG);
        writer.println(String.format("WifiNetworkScoreCache (%s/%d)", new Object[]{this.mContext.getPackageName(), Integer.valueOf(Process.myUid())}));
        writer.println("  All score curves:");
        synchronized (this.mLock) {
            for (ScoredNetwork score : this.mCache.snapshot().values()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("    ");
                stringBuilder.append(score);
                writer.println(stringBuilder.toString());
            }
            writer.println("  Network scores for latest ScanResults:");
            for (ScanResult scanResult : ((WifiManager) this.mContext.getSystemService("wifi")).getScanResults()) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("    ");
                stringBuilder2.append(buildNetworkKey(scanResult));
                stringBuilder2.append(": ");
                stringBuilder2.append(getNetworkScore(scanResult));
                writer.println(stringBuilder2.toString());
            }
        }
    }

    public void registerListener(CacheListener listener) {
        synchronized (this.mLock) {
            this.mListener = listener;
        }
    }

    public void unregisterListener() {
        synchronized (this.mLock) {
            this.mListener = null;
        }
    }
}
