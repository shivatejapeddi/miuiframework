package miui.maml;

import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import miui.maml.ResourceManager.BitmapInfo;

public class LifecycleResourceManager extends ResourceManager {
    private static final String LOG_TAG = "LifecycleResourceManager";
    public static final int TIME_DAY = 86400000;
    public static final int TIME_HOUR = 3600000;
    private static long mLastCheckCacheTime;
    private long mCheckTime;
    private long mInactiveTime;

    public LifecycleResourceManager(ResourceLoader resourceLoader, long inactiveTime, long checkTime) {
        super(resourceLoader);
        this.mInactiveTime = inactiveTime;
        this.mCheckTime = checkTime;
    }

    public void checkCache() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - mLastCheckCacheTime >= this.mCheckTime) {
            Log.d(LOG_TAG, "begin check cache... ");
            ArrayList<String> mToBeRemoved = new ArrayList();
            synchronized (this.mBitmapsCache) {
                String key;
                for (String key2 : this.mBitmapsCache.snapshot().keySet()) {
                    BitmapInfo bi = (BitmapInfo) this.mBitmapsCache.get(key2);
                    if (bi != null && currentTimeMillis - bi.mLastVisitTime > this.mInactiveTime) {
                        mToBeRemoved.add(key2);
                    }
                }
                Iterator it = mToBeRemoved.iterator();
                while (it.hasNext()) {
                    key2 = (String) it.next();
                    String str = LOG_TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("remove cache: ");
                    stringBuilder.append(key2);
                    Log.d(str, stringBuilder.toString());
                    this.mBitmapsCache.remove(key2);
                }
            }
            mLastCheckCacheTime = currentTimeMillis;
        }
    }

    public void pause() {
        checkCache();
    }

    public void finish(boolean keepResource) {
        if (keepResource) {
            checkCache();
        }
        super.finish(keepResource);
    }
}
