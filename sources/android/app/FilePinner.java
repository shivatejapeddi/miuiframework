package android.app;

import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build.VERSION;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.Slog;
import android.util.TimedRemoteCaller;
import com.android.internal.R;
import com.android.internal.os.BackgroundThread;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexFile;
import dalvik.system.VMRuntime;
import java.util.ArrayList;

public class FilePinner {
    private static final int MAX_LOCK_PAGES = 12800;
    private static final int MAX_PROFILE_COUNT = 10;
    private static final int PROFILE_DELAY = 5000;
    private static String TAG = "FilePinner";
    private static boolean enablePinAppFile = SystemProperties.getBoolean("persist.sys.pinappfile", false);
    private static String[] mAppsToPin = new String[]{WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER};
    private static boolean mIsSelectedApp = true;
    private static int mPageProfileCount;
    private static boolean mPinFileDone = false;
    private static ArrayMap<String, byte[]> mfileCaheVecs = new ArrayMap();

    private static class PinTask implements Runnable {
        private ApplicationInfo mAppInfo;
        private boolean mGoodToLock = false;
        private LoadedApk mPkgInfo;

        public PinTask(ApplicationInfo appInfo, LoadedApk pkgInfo, boolean goodToLock) {
            this.mGoodToLock = goodToLock;
            this.mAppInfo = appInfo;
            this.mPkgInfo = pkgInfo;
        }

        public void run() {
            ArrayList<String> filesToPin = FilePinner.getFilesToPin(this.mAppInfo, this.mPkgInfo);
            if (!filesToPin.isEmpty()) {
                FilePinner.recordHotPages(filesToPin, this.mGoodToLock);
            }
        }
    }

    private static ArrayList<String> getFilesToPin(ApplicationInfo appInfo, LoadedApk pkgInfo) {
        String baseOdex;
        ArrayList<String> filesToPin = new ArrayList();
        String baseApk = appInfo.getBaseCodePath();
        if (baseApk != null) {
            String arch = "arm";
            filesToPin.add(baseApk);
            if (appInfo.primaryCpuAbi != null && VMRuntime.is64BitAbi(appInfo.primaryCpuAbi)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(arch);
                stringBuilder.append("64");
                arch = stringBuilder.toString();
            }
            baseOdex = null;
            if (VERSION.SDK_INT >= 25) {
                try {
                    baseOdex = (String) DexFile.class.getDeclaredMethod("getDexFileOutputPath", new Class[]{String.class, String.class}).invoke(null, new Object[]{baseApk, arch});
                } catch (Exception e) {
                    Slog.e(TAG, "failed get base odex path");
                }
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(appInfo.getCodePath());
                stringBuilder2.append("/oat/");
                stringBuilder2.append(arch);
                stringBuilder2.append("/base.odex");
                baseOdex = stringBuilder2.toString();
            }
            if (baseOdex != null) {
                filesToPin.add(baseOdex);
            }
        }
        try {
            ClassLoader cl = pkgInfo.getClassLoader();
            if (cl instanceof BaseDexClassLoader) {
                baseOdex = ((BaseDexClassLoader) cl).toString();
                int startIdx = 0;
                int endIdx = 0;
                while (startIdx != -1) {
                    startIdx = baseOdex.indexOf("dex file \"", endIdx);
                    endIdx = baseOdex.indexOf("\",", startIdx);
                    if (!(startIdx == -1 || endIdx == -1)) {
                        String dexPath = baseOdex.substring(startIdx + 10, endIdx);
                        int p = dexPath.indexOf("dex/");
                        if (p >= 0) {
                            StringBuffer odexPath = new StringBuffer(dexPath);
                            odexPath.insert(p, "o");
                            filesToPin.add(odexPath.toString());
                        }
                    }
                }
            }
        } catch (Exception e2) {
            Slog.e(TAG, "failed to find dex files");
        }
        return filesToPin;
    }

    /* JADX WARNING: Removed duplicated region for block: B:98:0x01d7 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x01b7 A:{SYNTHETIC, Splitter:B:81:0x01b7} */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x01e6 A:{SYNTHETIC, Splitter:B:90:0x01e6} */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x01b7 A:{SYNTHETIC, Splitter:B:81:0x01b7} */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x01d7 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x01e6 A:{SYNTHETIC, Splitter:B:90:0x01e6} */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x01d7 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x01b7 A:{SYNTHETIC, Splitter:B:81:0x01b7} */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x01e6 A:{SYNTHETIC, Splitter:B:90:0x01e6} */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x01e6 A:{SYNTHETIC, Splitter:B:90:0x01e6} */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x01b7 A:{SYNTHETIC, Splitter:B:81:0x01b7} */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x01d7 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x01e6 A:{SYNTHETIC, Splitter:B:90:0x01e6} */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x01d7 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x01b7 A:{SYNTHETIC, Splitter:B:81:0x01b7} */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x01e6 A:{SYNTHETIC, Splitter:B:90:0x01e6} */
    private static void recordHotPages(java.util.ArrayList<java.lang.String> r21, boolean r22) {
        /*
        r1 = "Failed to close fd, error = ";
        r0 = 0;
        r2 = 0;
        r3 = r0;
    L_0x0005:
        r0 = r21.size();
        if (r2 >= r0) goto L_0x0206;
    L_0x000b:
        r0 = new java.io.FileDescriptor;
        r0.<init>();
        r4 = r0;
        r5 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
        r6 = r21;
        r0 = r6.get(r2);
        r7 = r0;
        r7 = (java.lang.String) r7;
        r0 = android.system.OsConstants.O_RDONLY;	 Catch:{ ErrnoException -> 0x0191, all -> 0x0189 }
        r8 = android.system.OsConstants.O_NOFOLLOW;	 Catch:{ ErrnoException -> 0x0191, all -> 0x0189 }
        r0 = r0 | r8;
        r0 = r0 | r5;
        r8 = android.system.OsConstants.O_RDONLY;	 Catch:{ ErrnoException -> 0x0191, all -> 0x0189 }
        r15 = android.system.Os.open(r7, r0, r8);	 Catch:{ ErrnoException -> 0x0191, all -> 0x0189 }
        r0 = android.system.Os.fstat(r15);	 Catch:{ ErrnoException -> 0x0182, all -> 0x017a }
        r4 = r0;
        r9 = 0;
        r11 = r4.st_size;	 Catch:{ ErrnoException -> 0x0182, all -> 0x017a }
        r13 = android.system.OsConstants.PROT_READ;	 Catch:{ ErrnoException -> 0x0182, all -> 0x017a }
        r14 = android.system.OsConstants.MAP_PRIVATE;	 Catch:{ ErrnoException -> 0x0182, all -> 0x017a }
        r16 = 0;
        r8 = android.system.Os.mmap(r9, r11, r13, r14, r15, r16);	 Catch:{ ErrnoException -> 0x0182, all -> 0x017a }
        r10 = r4.st_size;	 Catch:{ ErrnoException -> 0x0182, all -> 0x017a }
        r0 = (int) r10;	 Catch:{ ErrnoException -> 0x0182, all -> 0x017a }
        r0 = r0 + 4096;
        r0 = r0 + -1;
        r0 = r0 / 4096;
        r10 = r0;
        r0 = new byte[r10];	 Catch:{ ErrnoException -> 0x0182, all -> 0x017a }
        r11 = r0;
        r12 = r4.st_size;	 Catch:{ ErrnoException -> 0x0182, all -> 0x017a }
        android.system.Os.mincore(r8, r12, r11);	 Catch:{ ErrnoException -> 0x0182, all -> 0x017a }
        r0 = mfileCaheVecs;	 Catch:{ ErrnoException -> 0x0182, all -> 0x017a }
        r0 = r0.get(r7);	 Catch:{ ErrnoException -> 0x0182, all -> 0x017a }
        r0 = (byte[]) r0;	 Catch:{ ErrnoException -> 0x0182, all -> 0x017a }
        r12 = r0;
        if (r12 == 0) goto L_0x007a;
    L_0x0058:
        r0 = 0;
    L_0x0059:
        if (r0 >= r10) goto L_0x0066;
    L_0x005b:
        r13 = r12[r0];	 Catch:{ ErrnoException -> 0x0074, all -> 0x006c }
        r14 = r11[r0];	 Catch:{ ErrnoException -> 0x0074, all -> 0x006c }
        r13 = r13 & r14;
        r13 = (byte) r13;	 Catch:{ ErrnoException -> 0x0074, all -> 0x006c }
        r12[r0] = r13;	 Catch:{ ErrnoException -> 0x0074, all -> 0x006c }
        r0 = r0 + 1;
        goto L_0x0059;
    L_0x0066:
        r0 = mfileCaheVecs;	 Catch:{ ErrnoException -> 0x0074, all -> 0x006c }
        r0.put(r7, r12);	 Catch:{ ErrnoException -> 0x0074, all -> 0x006c }
        goto L_0x007f;
    L_0x006c:
        r0 = move-exception;
        r16 = r3;
        r17 = r5;
        r3 = r0;
        goto L_0x01e0;
    L_0x0074:
        r0 = move-exception;
        r17 = r5;
        r4 = r15;
        goto L_0x0196;
    L_0x007a:
        r0 = mfileCaheVecs;	 Catch:{ ErrnoException -> 0x0182, all -> 0x017a }
        r0.put(r7, r11);	 Catch:{ ErrnoException -> 0x0182, all -> 0x017a }
    L_0x007f:
        if (r22 != 0) goto L_0x00c6;
    L_0x0081:
        r13 = r4.st_size;	 Catch:{ ErrnoException -> 0x00be, all -> 0x00b6 }
        android.system.Os.munmap(r8, r13);	 Catch:{ ErrnoException -> 0x00be, all -> 0x00b6 }
        r0 = r15.valid();
        if (r0 == 0) goto L_0x00b0;
    L_0x008c:
        android.system.Os.close(r15);	 Catch:{ ErrnoException -> 0x0092 }
        r16 = r3;
        goto L_0x00b2;
    L_0x0092:
        r0 = move-exception;
        r13 = r0;
        r0 = r13;
        r13 = TAG;
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r14.append(r1);
        r16 = r3;
        r3 = r0.getMessage();
        r14.append(r3);
        r3 = r14.toString();
        android.util.Slog.e(r13, r3);
        goto L_0x00b2;
    L_0x00b0:
        r16 = r3;
    L_0x00b2:
        r3 = r16;
        goto L_0x01d7;
    L_0x00b6:
        r0 = move-exception;
        r16 = r3;
        r3 = r0;
        r17 = r5;
        goto L_0x01e0;
    L_0x00be:
        r0 = move-exception;
        r16 = r3;
        r17 = r5;
        r4 = r15;
        goto L_0x0196;
    L_0x00c6:
        r16 = r3;
        r0 = 0;
        r3 = 0;
        r13 = 0;
    L_0x00cb:
        if (r0 >= r10) goto L_0x011b;
    L_0x00cd:
        r14 = r12[r0];	 Catch:{ ErrnoException -> 0x0113, all -> 0x010d }
        r14 = r14 & 1;
        if (r14 <= 0) goto L_0x00dc;
    L_0x00d3:
        r13 = r13 + 1;
        r18 = r4;
        r17 = r5;
        r19 = r8;
        goto L_0x0104;
    L_0x00dc:
        if (r13 <= 0) goto L_0x00fe;
    L_0x00de:
        r14 = r0 - r13;
        r14 = r14 * 4096;
        r18 = r4;
        r17 = r5;
        r4 = (long) r14;
        r4 = r4 + r8;
        r14 = r13 * 4096;
        r19 = r8;
        r8 = (long) r14;
        android.system.Os.mlock(r4, r8);	 Catch:{ ErrnoException -> 0x0175, all -> 0x0171 }
        r3 = r3 + r13;
        r4 = r16 + r13;
        r13 = 0;
        r5 = 12800; // 0x3200 float:1.7937E-41 double:6.324E-320;
        if (r4 <= r5) goto L_0x00fb;
    L_0x00f8:
        r16 = r4;
        goto L_0x0121;
    L_0x00fb:
        r16 = r4;
        goto L_0x0104;
    L_0x00fe:
        r18 = r4;
        r17 = r5;
        r19 = r8;
    L_0x0104:
        r0 = r0 + 1;
        r5 = r17;
        r4 = r18;
        r8 = r19;
        goto L_0x00cb;
    L_0x010d:
        r0 = move-exception;
        r17 = r5;
        r3 = r0;
        goto L_0x01e0;
    L_0x0113:
        r0 = move-exception;
        r17 = r5;
        r4 = r15;
        r3 = r16;
        goto L_0x0196;
    L_0x011b:
        r18 = r4;
        r17 = r5;
        r19 = r8;
    L_0x0121:
        r4 = r3 * 100;
        r4 = (double) r4;	 Catch:{ ErrnoException -> 0x0175, all -> 0x0171 }
        r8 = (double) r10;	 Catch:{ ErrnoException -> 0x0175, all -> 0x0171 }
        r4 = r4 / r8;
        r8 = TAG;	 Catch:{ ErrnoException -> 0x0175, all -> 0x0171 }
        r9 = new java.lang.StringBuilder;	 Catch:{ ErrnoException -> 0x0175, all -> 0x0171 }
        r9.<init>();	 Catch:{ ErrnoException -> 0x0175, all -> 0x0171 }
        r14 = "cached ";
        r9.append(r14);	 Catch:{ ErrnoException -> 0x0175, all -> 0x0171 }
        r9.append(r7);	 Catch:{ ErrnoException -> 0x0175, all -> 0x0171 }
        r14 = " with ";
        r9.append(r14);	 Catch:{ ErrnoException -> 0x0175, all -> 0x0171 }
        r9.append(r4);	 Catch:{ ErrnoException -> 0x0175, all -> 0x0171 }
        r14 = "%";
        r9.append(r14);	 Catch:{ ErrnoException -> 0x0175, all -> 0x0171 }
        r9 = r9.toString();	 Catch:{ ErrnoException -> 0x0175, all -> 0x0171 }
        android.util.Slog.i(r8, r9);	 Catch:{ ErrnoException -> 0x0175, all -> 0x0171 }
        r0 = r15.valid();
        if (r0 == 0) goto L_0x00b2;
    L_0x0150:
        android.system.Os.close(r15);	 Catch:{ ErrnoException -> 0x0155 }
    L_0x0153:
        goto L_0x00b2;
    L_0x0155:
        r0 = move-exception;
        r3 = r0;
        r0 = r3;
        r3 = TAG;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r1);
        r5 = r0.getMessage();
        r4.append(r5);
        r4 = r4.toString();
        android.util.Slog.e(r3, r4);
        goto L_0x0153;
    L_0x0171:
        r0 = move-exception;
        r3 = r0;
        goto L_0x01e0;
    L_0x0175:
        r0 = move-exception;
        r4 = r15;
        r3 = r16;
        goto L_0x0196;
    L_0x017a:
        r0 = move-exception;
        r16 = r3;
        r17 = r5;
        r3 = r0;
        goto L_0x01e0;
    L_0x0182:
        r0 = move-exception;
        r16 = r3;
        r17 = r5;
        r4 = r15;
        goto L_0x0196;
    L_0x0189:
        r0 = move-exception;
        r16 = r3;
        r17 = r5;
        r3 = r0;
        r15 = r4;
        goto L_0x01e0;
    L_0x0191:
        r0 = move-exception;
        r16 = r3;
        r17 = r5;
    L_0x0196:
        r5 = TAG;	 Catch:{ all -> 0x01db }
        r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01db }
        r8.<init>();	 Catch:{ all -> 0x01db }
        r9 = "Could not pin file with error ";
        r8.append(r9);	 Catch:{ all -> 0x01db }
        r9 = r0.getMessage();	 Catch:{ all -> 0x01db }
        r8.append(r9);	 Catch:{ all -> 0x01db }
        r8 = r8.toString();	 Catch:{ all -> 0x01db }
        android.util.Slog.e(r5, r8);	 Catch:{ all -> 0x01db }
        r0 = r4.valid();
        if (r0 == 0) goto L_0x01d7;
    L_0x01b7:
        android.system.Os.close(r4);	 Catch:{ ErrnoException -> 0x01bb }
    L_0x01ba:
        goto L_0x01d7;
    L_0x01bb:
        r0 = move-exception;
        r5 = r0;
        r0 = r5;
        r5 = TAG;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r8.append(r1);
        r9 = r0.getMessage();
        r8.append(r9);
        r8 = r8.toString();
        android.util.Slog.e(r5, r8);
        goto L_0x01ba;
    L_0x01d7:
        r2 = r2 + 1;
        goto L_0x0005;
    L_0x01db:
        r0 = move-exception;
        r16 = r3;
        r15 = r4;
        r3 = r0;
    L_0x01e0:
        r0 = r15.valid();
        if (r0 == 0) goto L_0x0205;
    L_0x01e6:
        android.system.Os.close(r15);	 Catch:{ ErrnoException -> 0x01ea }
        goto L_0x0205;
    L_0x01ea:
        r0 = move-exception;
        r4 = r0;
        r0 = r4;
        r4 = TAG;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r5.append(r1);
        r1 = r0.getMessage();
        r5.append(r1);
        r1 = r5.toString();
        android.util.Slog.e(r4, r1);
    L_0x0205:
        throw r3;
    L_0x0206:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.FilePinner.recordHotPages(java.util.ArrayList, boolean):void");
    }

    static void handlePinAppFile(Application app, LoadedApk pkgInfo, Resources resources) {
        if (enablePinAppFile && mIsSelectedApp && !mPinFileDone && app != null && pkgInfo != null) {
            ApplicationInfo appInfo = app.getApplicationInfo();
            if (appInfo != null) {
                if (resources != null) {
                    try {
                        mAppsToPin = resources.getStringArray(R.array.config_appsToPin);
                    } catch (Exception e) {
                        Slog.e(TAG, "failed get app to pin");
                    }
                }
                String[] strArr = mAppsToPin;
                if (strArr != null) {
                    int i = strArr.length;
                    while (true) {
                        i--;
                        if (i < 0) {
                            break;
                        }
                        String[] strArr2 = mAppsToPin;
                        if (strArr2[i] != null && strArr2[i].equals(appInfo.processName)) {
                            break;
                        }
                    }
                    if (i < 0) {
                        mIsSelectedApp = false;
                        return;
                    }
                    i = mPageProfileCount;
                    if (i < 10) {
                        mPageProfileCount = i + 1;
                        boolean goodToLock = false;
                        if (mPageProfileCount == 10) {
                            goodToLock = true;
                            mPinFileDone = true;
                        }
                        BackgroundThread.getHandler().postDelayed(new PinTask(appInfo, pkgInfo, goodToLock), TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
                    }
                    return;
                }
                mIsSelectedApp = false;
            }
        }
    }
}
