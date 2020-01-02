package miui.app.backup;

import android.app.backup.FullBackup;
import android.app.backup.FullBackupDataOutput;

public class FullBackupProxy {
    public static int backupToTar(String packageName, String domain, String linkdomain, String rootpath, String path, FullBackupDataOutput output) {
        return FullBackup.backupToTar(packageName, domain, linkdomain, rootpath, path, output);
    }
}
