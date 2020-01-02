package miui.log;

import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

final class Utils {
    private static final String TAG = "MiuiLogUtils";

    Utils() {
    }

    static void createLogSwitchesFileIfNotExisted(String logSwitchesFolder, String logSwitchesFilePath) {
        File configFolder = new File(logSwitchesFolder);
        boolean newCreated = false;
        if (!configFolder.exists()) {
            newCreated = true;
        } else if (configFolder.isFile()) {
            configFolder.delete();
            newCreated = true;
        }
        if (newCreated) {
            createConfigFolder(configFolder);
        }
        File configFile = new File(logSwitchesFilePath);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                configFile.setWritable(true, true);
                configFile.setReadable(true, false);
                configFile.setExecutable(false);
            } catch (IOException ex) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("failed to create file ");
                stringBuilder.append(logSwitchesFilePath);
                Log.e(TAG, stringBuilder.toString(), ex);
            }
        }
    }

    private static void createConfigFolder(File configFolder) {
        ArrayList<File> absentFolders = new ArrayList();
        File folder = configFolder;
        while (folder != null && !folder.exists()) {
            absentFolders.add(0, folder);
            folder = folder.getParentFile();
        }
        Iterator it = absentFolders.iterator();
        while (it.hasNext()) {
            File absentFolder = (File) it.next();
            absentFolder.mkdir();
            absentFolder.setReadable(true, false);
            absentFolder.setWritable(true, true);
            absentFolder.setExecutable(true, false);
        }
    }
}
