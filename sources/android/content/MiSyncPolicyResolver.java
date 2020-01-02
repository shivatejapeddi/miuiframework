package android.content;

import android.accounts.Account;
import android.os.RemoteException;
import android.os.UserHandle;

public class MiSyncPolicyResolver {
    public static final int MI_SYNC_STRATEGY_MI_OPTIMIZED = 1;
    public static final int MI_SYNC_STRATEGY_OFFICIAL = 0;
    public static final String SYNC_ERROR_MI_CANCELED = "mi_canceled";
    public static final String SYNC_EXTRAS_MICLOUD_FORCE = "micloud_force";

    public static void setMiSyncPauseToTime(Account account, long pauseTimeMillis) {
        try {
            ContentResolver.getContentService().setMiSyncPauseToTime(account, pauseTimeMillis, UserHandle.myUserId());
        } catch (RemoteException e) {
        }
    }

    public static void setMiSyncResume(Account account) {
        setMiSyncPauseToTime(account, 0);
    }

    public static long getMiSyncPauseToTime(Account account) {
        try {
            return ContentResolver.getContentService().getMiSyncPauseToTime(account, UserHandle.myUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static void setMiSyncStrategy(Account account, int strategy) {
        try {
            ContentResolver.getContentService().setMiSyncStrategy(account, strategy, UserHandle.myUserId());
        } catch (RemoteException e) {
        }
    }

    public static int getMiSyncStrategy(Account account) {
        try {
            return ContentResolver.getContentService().getMiSyncStrategy(account, UserHandle.myUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }
}
