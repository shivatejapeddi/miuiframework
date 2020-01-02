package android.util;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.SntpClient;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings.Global;
import android.text.TextUtils;
import com.android.internal.R;

public class NtpTrustedTime implements TrustedTime {
    private static final String BACKUP_SERVER = "persist.backup.ntpServer";
    private static final boolean LOGD = true;
    private static final String TAG = "NtpTrustedTime";
    private static String mBackupServer = "";
    private static int mNtpRetries = 0;
    private static int mNtpRetriesMax = 0;
    private static Context sContext;
    private static NtpTrustedTime sSingleton;
    private boolean mBackupmode = false;
    private ConnectivityManager mCM;
    private long mCachedNtpCertainty;
    private long mCachedNtpElapsedRealtime;
    private long mCachedNtpTime;
    private boolean mHasCache;
    private final String mServer;
    private final long mTimeout;

    private NtpTrustedTime(String server, long timeout) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("creating NtpTrustedTime using ");
        stringBuilder.append(server);
        Log.d(TAG, stringBuilder.toString());
        this.mServer = server;
        this.mTimeout = timeout;
    }

    @UnsupportedAppUsage
    public static synchronized NtpTrustedTime getInstance(Context context) {
        NtpTrustedTime ntpTrustedTime;
        synchronized (NtpTrustedTime.class) {
            if (sSingleton == null) {
                Resources res = context.getResources();
                ContentResolver resolver = context.getContentResolver();
                String defaultServer = res.getString(R.string.config_ntpServer);
                long defaultTimeout = (long) res.getInteger(R.integer.config_ntpTimeout);
                String secureServer = Global.getString(resolver, Global.NTP_SERVER);
                sSingleton = new NtpTrustedTime(secureServer != null ? secureServer : defaultServer, Global.getLong(resolver, Global.NTP_TIMEOUT, defaultTimeout));
                sContext = context;
                String sserver_prop = Global.getString(resolver, Global.NTP_SERVER_2);
                String secondServer_prop = (sserver_prop == null || sserver_prop.length() <= 0) ? BACKUP_SERVER : sserver_prop;
                String backupServer = SystemProperties.get(secondServer_prop);
                if (backupServer == null || backupServer.length() <= 0) {
                } else {
                    int retryMax = res.getInteger(R.integer.config_ntpRetry);
                    if (retryMax > 0) {
                        NtpTrustedTime ntpTrustedTime2 = sSingleton;
                        mNtpRetriesMax = retryMax;
                        ntpTrustedTime2 = sSingleton;
                        mBackupServer = backupServer.trim().replace("\"", "");
                    }
                }
            }
            ntpTrustedTime = sSingleton;
        }
        return ntpTrustedTime;
    }

    @UnsupportedAppUsage
    public boolean forceRefresh() {
        return hasCache() ? forceSync() : false;
    }

    public boolean forceSync() {
        synchronized (this) {
            if (this.mCM == null) {
                this.mCM = (ConnectivityManager) sContext.getSystemService(ConnectivityManager.class);
            }
        }
        ConnectivityManager connectivityManager = this.mCM;
        return forceRefresh(connectivityManager == null ? null : connectivityManager.getActiveNetwork());
    }

    public boolean forceRefresh(Network network) {
        if (TextUtils.isEmpty(this.mServer)) {
            return false;
        }
        synchronized (this) {
            if (this.mCM == null) {
                this.mCM = (ConnectivityManager) sContext.getSystemService(ConnectivityManager.class);
            }
        }
        ConnectivityManager connectivityManager = this.mCM;
        NetworkInfo ni = connectivityManager == null ? null : connectivityManager.getNetworkInfo(network);
        if (ni == null || !ni.isConnected()) {
            Log.d(TAG, "forceRefresh: no connectivity");
            return false;
        }
        Log.d(TAG, "forceRefresh() from cache miss");
        SntpClient client = new SntpClient();
        String targetServer = this.mServer;
        if (getBackupmode()) {
            setBackupmode(false);
            targetServer = mBackupServer;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Ntp Server to access at:");
        stringBuilder.append(targetServer);
        Log.d(TAG, stringBuilder.toString());
        if (NtpTrustedTimeInjector.requestTime(client, targetServer, (int) this.mTimeout, network)) {
            this.mHasCache = true;
            this.mCachedNtpTime = client.getNtpTime();
            this.mCachedNtpElapsedRealtime = client.getNtpTimeReference();
            this.mCachedNtpCertainty = client.getRoundTripTime() / 2;
            return true;
        }
        countInBackupmode();
        return false;
    }

    @UnsupportedAppUsage
    public boolean hasCache() {
        return this.mHasCache;
    }

    public long getCacheAge() {
        if (this.mHasCache) {
            return SystemClock.elapsedRealtime() - this.mCachedNtpElapsedRealtime;
        }
        return Long.MAX_VALUE;
    }

    public long getCacheCertainty() {
        if (this.mHasCache) {
            return this.mCachedNtpCertainty;
        }
        return Long.MAX_VALUE;
    }

    @UnsupportedAppUsage
    public long currentTimeMillis() {
        if (this.mHasCache) {
            Log.d(TAG, "currentTimeMillis() cache hit");
            return this.mCachedNtpTime + getCacheAge();
        }
        throw new IllegalStateException("Missing authoritative time source");
    }

    @UnsupportedAppUsage
    public long getCachedNtpTime() {
        Log.d(TAG, "getCachedNtpTime() cache hit");
        return this.mCachedNtpTime;
    }

    @UnsupportedAppUsage
    public long getCachedNtpTimeReference() {
        return this.mCachedNtpElapsedRealtime;
    }

    public void setBackupmode(boolean mode) {
        if (isBackupSupported()) {
            this.mBackupmode = mode;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setBackupmode() set the backup mode to be:");
        stringBuilder.append(this.mBackupmode);
        Log.d(TAG, stringBuilder.toString());
    }

    private boolean getBackupmode() {
        return this.mBackupmode;
    }

    private boolean isBackupSupported() {
        if (mNtpRetriesMax > 0) {
            String str = mBackupServer;
            if (!(str == null || str.length() == 0)) {
                return true;
            }
        }
        return false;
    }

    private void countInBackupmode() {
        if (isBackupSupported()) {
            mNtpRetries++;
            if (mNtpRetries >= mNtpRetriesMax) {
                mNtpRetries = 0;
                setBackupmode(true);
            }
        }
        Log.d(TAG, "countInBackupmode() func");
    }
}
