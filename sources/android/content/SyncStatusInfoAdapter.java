package android.content;

public class SyncStatusInfoAdapter {
    private SyncStatusInfoAdapter() {
    }

    public static int getNumSyncs(SyncStatusInfo info) {
        return info.totalStats.numSyncs;
    }
}
