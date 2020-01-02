package android.app.backup;

import android.system.OsConstants;

class BackupAgentInjector {
    BackupAgentInjector() {
    }

    static boolean shouldSkip(int mode) {
        return (OsConstants.S_ISREG(mode) || OsConstants.S_ISDIR(mode)) ? false : true;
    }
}
