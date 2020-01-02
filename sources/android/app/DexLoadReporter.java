package android.app;

import android.os.FileUtils;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import dalvik.system.BaseDexClassLoader.Reporter;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class DexLoadReporter implements Reporter {
    private static final boolean DEBUG = false;
    private static final DexLoadReporter INSTANCE = new DexLoadReporter();
    private static final String TAG = "DexLoadReporter";
    @GuardedBy({"mDataDirs"})
    private final Set<String> mDataDirs = new HashSet();

    private DexLoadReporter() {
    }

    static DexLoadReporter getInstance() {
        return INSTANCE;
    }

    /* Access modifiers changed, original: 0000 */
    public void registerAppDataDir(String packageName, String dataDir) {
        if (dataDir != null) {
            synchronized (this.mDataDirs) {
                this.mDataDirs.add(dataDir);
            }
        }
    }

    public void report(List<ClassLoader> classLoadersChain, List<String> classPaths) {
        int size = classLoadersChain.size();
        int size2 = classPaths.size();
        String str = TAG;
        if (size != size2) {
            Slog.wtf(str, "Bad call to DexLoadReporter: argument size mismatch");
        } else if (classPaths.isEmpty()) {
            Slog.wtf(str, "Bad call to DexLoadReporter: empty dex paths");
        } else {
            String[] dexPathsForRegistration = ((String) classPaths.get(0)).split(File.pathSeparator);
            if (dexPathsForRegistration.length != 0) {
                notifyPackageManager(classLoadersChain, classPaths);
                registerSecondaryDexForProfiling(dexPathsForRegistration);
            }
        }
    }

    private void notifyPackageManager(List<ClassLoader> classLoadersChain, List<String> classPaths) {
        List<String> classLoadersNames = new ArrayList(classPaths.size());
        for (ClassLoader classLoader : classLoadersChain) {
            classLoadersNames.add(classLoader.getClass().getName());
        }
        String packageName = ActivityThread.currentPackageName();
        try {
            ActivityThread.getPackageManager().notifyDexLoad(packageName, classLoadersNames, classPaths, VMRuntime.getRuntime().vmInstructionSet());
        } catch (RemoteException re) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to notify PM about dex load for package ");
            stringBuilder.append(packageName);
            Slog.e(TAG, stringBuilder.toString(), re);
        }
    }

    private void registerSecondaryDexForProfiling(String[] dexPaths) {
        int i = 0;
        if (SystemProperties.getBoolean("dalvik.vm.dexopt.secondary", false)) {
            String[] dataDirs;
            synchronized (this.mDataDirs) {
                dataDirs = (String[]) this.mDataDirs.toArray(new String[0]);
            }
            int length = dexPaths.length;
            while (i < length) {
                registerSecondaryDexForProfiling(dexPaths[i], dataDirs);
                i++;
            }
        }
    }

    private void registerSecondaryDexForProfiling(String dexPath, String[] dataDirs) {
        if (isSecondaryDexFile(dexPath, dataDirs)) {
            File dexPathFile = new File(dexPath);
            File secondaryProfileDir = new File(dexPathFile.getParent(), "oat");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(dexPathFile.getName());
            stringBuilder.append(".cur.prof");
            File secondaryProfile = new File(secondaryProfileDir, stringBuilder.toString());
            boolean exists = secondaryProfileDir.exists();
            String str = TAG;
            if (exists || secondaryProfileDir.mkdir()) {
                try {
                    secondaryProfile.createNewFile();
                    VMRuntime.registerAppInfo(secondaryProfile.getPath(), new String[]{dexPath});
                    return;
                } catch (IOException ex) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Failed to create profile for secondary dex ");
                    stringBuilder2.append(dexPath);
                    stringBuilder2.append(":");
                    stringBuilder2.append(ex.getMessage());
                    Slog.e(str, stringBuilder2.toString());
                    return;
                }
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Could not create the profile directory: ");
            stringBuilder.append(secondaryProfile);
            Slog.e(str, stringBuilder.toString());
        }
    }

    private boolean isSecondaryDexFile(String dexPath, String[] dataDirs) {
        for (String dataDir : dataDirs) {
            if (FileUtils.contains(dataDir, dexPath)) {
                return true;
            }
        }
        return false;
    }
}
