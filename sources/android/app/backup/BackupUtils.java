package android.app.backup;

import android.app.backup.FullBackup.BackupScheme.PathWithRequiredFlags;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class BackupUtils {
    private BackupUtils() {
    }

    public static boolean isFileSpecifiedInPathList(File file, Collection<PathWithRequiredFlags> canonicalPathList) throws IOException {
        for (PathWithRequiredFlags canonical : canonicalPathList) {
            String canonicalPath = canonical.getPath();
            File fileFromList = new File(canonicalPath);
            if (fileFromList.isDirectory()) {
                if (file.isDirectory()) {
                    if (file.equals(fileFromList)) {
                        return true;
                    }
                } else if (file.toPath().startsWith(canonicalPath)) {
                    return true;
                }
            } else if (file.equals(fileFromList)) {
                return true;
            }
        }
        return false;
    }
}
