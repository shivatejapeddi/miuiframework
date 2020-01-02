package miui.app.backup;

import android.app.backup.BackupAgent;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FullBackupDataOutput;
import android.net.Uri;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.util.Pair;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class FullBackupAgent extends BackupAgent {
    private static final String TAG = "Backup:FullBackupAgent";
    private static final String TMP_BAK_NAME = "_tmp_bak";
    private ArrayList<Object> mAttachedFiles;
    private BackupManager mBackupManager;
    private BackupMeta mBackupMeta;

    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
    }

    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0121  */
    public final void onFullBackup(android.app.backup.FullBackupDataOutput r21) throws java.io.IOException {
        /*
        r20 = this;
        r1 = r20;
        r8 = r21;
        r9 = "Backup:FullBackupAgent";
        r0 = r1.mBackupManager;
        if (r0 != 0) goto L_0x0010;
    L_0x000a:
        r0 = miui.app.backup.BackupManager.getBackupManager(r20);
        r1.mBackupManager = r0;
    L_0x0010:
        r10 = r20.getPackageManager();
        r2 = 0;
        r3 = "";
        r4 = miui.os.Build.DEVICE;
        r11 = r20.getPackageName();
        r5 = android.os.Build.VERSION.INCREMENTAL;
        r0 = r1.mBackupManager;
        r12 = r0.getCurrentWorkingFeature();
        r13 = r1.getVersion(r12);
        r0 = r20.getPackageName();	 Catch:{ NameNotFoundException -> 0x003a }
        r6 = 0;
        r0 = r10.getPackageInfo(r0, r6);	 Catch:{ NameNotFoundException -> 0x003a }
        r6 = r0.versionCode;	 Catch:{ NameNotFoundException -> 0x003a }
        r2 = r6;
        r6 = r0.versionName;	 Catch:{ NameNotFoundException -> 0x003a }
        r3 = r6;
        r14 = r2;
        goto L_0x0041;
    L_0x003a:
        r0 = move-exception;
        r6 = "NameNotFoundException";
        android.util.Log.e(r9, r6, r0);
        r14 = r2;
    L_0x0041:
        r0 = "\r";
        r2 = "";
        r3 = r3.replaceAll(r0, r2);
        r6 = "\n";
        r15 = r3.replaceAll(r6, r2);
        r3 = r4.replaceAll(r0, r2);
        r7 = r3.replaceAll(r6, r2);
        r0 = r5.replaceAll(r0, r2);
        r6 = r0.replaceAll(r6, r2);
        r0 = new miui.app.backup.BackupMeta;
        r0.<init>();
        r1.mBackupMeta = r0;
        r0 = r1.mBackupMeta;
        r0.appVersionCode = r14;
        r0.appVersionName = r15;
        r0.deviceName = r7;
        r2 = r20.getPackageName();
        r0.packageName = r2;
        r0 = r1.mBackupMeta;
        r0.miuiVersion = r6;
        r0.feature = r12;
        r2 = java.lang.System.currentTimeMillis();
        r0.createTime = r2;
        r0 = r1.mBackupMeta;
        r0.version = r13;
        r0.writeToTar(r1, r8);
        if (r13 != 0) goto L_0x0092;
    L_0x0089:
        super.onFullBackup(r21);
        r16 = r6;
        r17 = r7;
        goto L_0x0109;
    L_0x0092:
        r0 = new java.io.File;
        r2 = r20.getExternalCacheDir();
        r3 = "_tmp_bak";
        r0.<init>(r2, r3);
        r5 = r0;
        r2 = 0;
        r5.createNewFile();	 Catch:{ all -> 0x0116 }
        r0 = 536870912; // 0x20000000 float:1.0842022E-19 double:2.652494739E-315;
        r0 = android.os.ParcelFileDescriptor.open(r5, r0);	 Catch:{ all -> 0x0116 }
        r4 = r0;
        r1.onFullBackup(r4, r12);	 Catch:{ all -> 0x010c }
        r3 = miui.app.backup.BackupManager.DOMAIN_BAK;	 Catch:{ all -> 0x010c }
        r0 = 0;
        r16 = r5.getParent();	 Catch:{ all -> 0x010c }
        r17 = r5.getAbsolutePath();	 Catch:{ all -> 0x010c }
        r2 = r11;
        r18 = r4;
        r4 = r0;
        r19 = r5;
        r5 = r16;
        r16 = r6;
        r6 = r17;
        r17 = r7;
        r7 = r21;
        r0 = miui.app.backup.FullBackupProxy.backupToTar(r2, r3, r4, r5, r6, r7);	 Catch:{ all -> 0x010a }
        if (r0 == 0) goto L_0x00ef;
    L_0x00cd:
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x010a }
        r2.<init>();	 Catch:{ all -> 0x010a }
        r3 = "err when data backup err = ";
        r2.append(r3);	 Catch:{ all -> 0x010a }
        r2.append(r0);	 Catch:{ all -> 0x010a }
        r2 = r2.toString();	 Catch:{ all -> 0x010a }
        android.util.Log.w(r9, r2);	 Catch:{ all -> 0x010a }
        r2 = r1.mBackupManager;	 Catch:{ all -> 0x010a }
        r2.setWorkingError(r0);	 Catch:{ all -> 0x010a }
        if (r18 == 0) goto L_0x00eb;
    L_0x00e8:
        r18.close();
    L_0x00eb:
        r19.delete();
        return;
    L_0x00ef:
        if (r18 == 0) goto L_0x00f4;
    L_0x00f1:
        r18.close();
    L_0x00f4:
        r19.delete();
        r0 = r1.tarAttaches(r11, r8, r12);
        if (r0 == 0) goto L_0x0109;
    L_0x00fe:
        r2 = "err when tar attaches";
        android.util.Log.w(r9, r2);
        r2 = r1.mBackupManager;
        r2.setWorkingError(r0);
        return;
    L_0x0109:
        return;
    L_0x010a:
        r0 = move-exception;
        goto L_0x011f;
    L_0x010c:
        r0 = move-exception;
        r18 = r4;
        r19 = r5;
        r16 = r6;
        r17 = r7;
        goto L_0x011f;
    L_0x0116:
        r0 = move-exception;
        r19 = r5;
        r16 = r6;
        r17 = r7;
        r18 = r2;
    L_0x011f:
        if (r18 == 0) goto L_0x0124;
    L_0x0121:
        r18.close();
    L_0x0124:
        r19.delete();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.app.backup.FullBackupAgent.onFullBackup(android.app.backup.FullBackupDataOutput):void");
    }

    /* Access modifiers changed, original: protected */
    public int tarAttaches(String pkg, FullBackupDataOutput out, int feature) throws IOException {
        ArrayList arrayList = this.mAttachedFiles;
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Pair<Uri, String> ob = it.next();
                if (ob instanceof String) {
                    File f = new File((String) ob);
                    if (f.exists()) {
                        FullBackupProxy.backupToTar(pkg, BackupManager.DOMAIN_ATTACH, null, null, f.getAbsolutePath(), out);
                    }
                } else if (ob instanceof Pair) {
                    Pair<Uri, String> pair = ob;
                    InputStream is = null;
                    File f2 = new File(getExternalCacheDir(), (String) pair.second);
                    try {
                        is = getContentResolver().openInputStream((Uri) pair.first);
                        FileUtils.copyToFile(is, f2);
                        if (f2.exists()) {
                            FullBackupProxy.backupToTar(pkg, BackupManager.DOMAIN_ATTACH, null, f2.getParent(), f2.getAbsolutePath(), out);
                        }
                        f2.delete();
                        if (is == null) {
                        }
                    } catch (Exception e) {
                        String str = TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Exception when tar attaches for uri ");
                        stringBuilder.append(pair.first);
                        stringBuilder.append(" name ");
                        stringBuilder.append((String) pair.second);
                        stringBuilder.append(" skip!");
                        Log.w(str, stringBuilder.toString(), e);
                        f2.delete();
                        if (is == null) {
                        }
                    } catch (Throwable th) {
                        f2.delete();
                        if (is != null) {
                            is.close();
                        }
                    }
                    is.close();
                }
            }
        }
        return 0;
    }

    /* Access modifiers changed, original: protected|final */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00d3 A:{Catch:{ IOException -> 0x01af }} */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0155 A:{Catch:{ IOException -> 0x01af }} */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00d3 A:{Catch:{ IOException -> 0x01af }} */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0155 A:{Catch:{ IOException -> 0x01af }} */
    public final void onRestoreFile(android.os.ParcelFileDescriptor r21, long r22, int r24, java.lang.String r25, java.lang.String r26, long r27, long r29) throws java.io.IOException {
        /*
        r20 = this;
        r13 = r20;
        r14 = r25;
        r15 = r26;
        r11 = "Backup:FullBackupAgent";
        r0 = r13.mBackupManager;
        if (r0 != 0) goto L_0x0012;
    L_0x000c:
        r0 = miui.app.backup.BackupManager.getBackupManager(r20);
        r13.mBackupManager = r0;
    L_0x0012:
        r0 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01af }
        r0.<init>();	 Catch:{ IOException -> 0x01af }
        r1 = "onRestoreFile type = ";
        r0.append(r1);	 Catch:{ IOException -> 0x01af }
        r12 = r24;
        r0.append(r12);	 Catch:{ IOException -> 0x01af }
        r1 = " domain = ";
        r0.append(r1);	 Catch:{ IOException -> 0x01af }
        r0.append(r14);	 Catch:{ IOException -> 0x01af }
        r1 = " path = ";
        r0.append(r1);	 Catch:{ IOException -> 0x01af }
        r0.append(r15);	 Catch:{ IOException -> 0x01af }
        r0 = r0.toString();	 Catch:{ IOException -> 0x01af }
        android.util.Log.v(r11, r0);	 Catch:{ IOException -> 0x01af }
        r0 = miui.app.backup.BackupManager.DOMAIN_META;	 Catch:{ IOException -> 0x01af }
        r0 = r0.equals(r14);	 Catch:{ IOException -> 0x01af }
        if (r0 == 0) goto L_0x005b;
    L_0x0041:
        r0 = new miui.app.backup.BackupMeta;	 Catch:{ IOException -> 0x01af }
        r0.<init>();	 Catch:{ IOException -> 0x01af }
        r13.mBackupMeta = r0;	 Catch:{ IOException -> 0x01af }
        r1 = r13.mBackupMeta;	 Catch:{ IOException -> 0x01af }
        r2 = r20;
        r3 = r21;
        r4 = r22;
        r6 = r24;
        r7 = r27;
        r9 = r29;
        r1.buildFrom(r2, r3, r4, r6, r7, r9);	 Catch:{ IOException -> 0x01af }
        goto L_0x01ab;
    L_0x005b:
        r0 = miui.app.backup.BackupManager.DOMAIN_BAK;	 Catch:{ IOException -> 0x01af }
        r0 = r0.equals(r14);	 Catch:{ IOException -> 0x01af }
        r1 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r16 = 5;
        if (r0 == 0) goto L_0x00da;
    L_0x0067:
        r0 = new java.io.File;	 Catch:{ IOException -> 0x01af }
        r2 = r20.getExternalCacheDir();	 Catch:{ IOException -> 0x01af }
        r3 = "_tmp_bak";
        r0.<init>(r2, r3);	 Catch:{ IOException -> 0x01af }
        r10 = r0;
        r17 = 0;
        r2 = r21;
        r3 = r22;
        r5 = r24;
        r6 = r27;
        r8 = r29;
        r18 = r10;
        android.app.backup.FullBackup.restoreFile(r2, r3, r5, r6, r8, r10);	 Catch:{ all -> 0x00cc }
        r2 = r18;
        r0 = android.os.ParcelFileDescriptor.open(r2, r1);	 Catch:{ all -> 0x00c8 }
        r1 = r0;
        r0 = r13.mBackupMeta;	 Catch:{ all -> 0x00c6 }
        r0 = r0.feature;	 Catch:{ all -> 0x00c6 }
        r0 = r13.checkVersion(r0);	 Catch:{ all -> 0x00c6 }
        if (r0 == 0) goto L_0x009c;
    L_0x0095:
        r0 = r13.mBackupMeta;	 Catch:{ all -> 0x00c6 }
        r16 = r13.onDataRestore(r0, r1);	 Catch:{ all -> 0x00c6 }
        goto L_0x009e;
    L_0x009c:
        r0 = r13.mBackupManager;	 Catch:{ all -> 0x00c6 }
    L_0x009e:
        r0 = r16;
        if (r0 == 0) goto L_0x00bb;
    L_0x00a2:
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c6 }
        r3.<init>();	 Catch:{ all -> 0x00c6 }
        r4 = "err when data restoring err = ";
        r3.append(r4);	 Catch:{ all -> 0x00c6 }
        r3.append(r0);	 Catch:{ all -> 0x00c6 }
        r3 = r3.toString();	 Catch:{ all -> 0x00c6 }
        android.util.Log.w(r11, r3);	 Catch:{ all -> 0x00c6 }
        r3 = r13.mBackupManager;	 Catch:{ all -> 0x00c6 }
        r3.setWorkingError(r0);	 Catch:{ all -> 0x00c6 }
    L_0x00bb:
        if (r1 == 0) goto L_0x00c0;
    L_0x00bd:
        r1.close();	 Catch:{ IOException -> 0x01af }
    L_0x00c0:
        r2.delete();	 Catch:{ IOException -> 0x01af }
        goto L_0x01ab;
    L_0x00c6:
        r0 = move-exception;
        goto L_0x00d1;
    L_0x00c8:
        r0 = move-exception;
        r1 = r17;
        goto L_0x00d1;
    L_0x00cc:
        r0 = move-exception;
        r2 = r18;
        r1 = r17;
    L_0x00d1:
        if (r1 == 0) goto L_0x00d6;
    L_0x00d3:
        r1.close();	 Catch:{ IOException -> 0x01af }
    L_0x00d6:
        r2.delete();	 Catch:{ IOException -> 0x01af }
        throw r0;	 Catch:{ IOException -> 0x01af }
    L_0x00da:
        r0 = miui.app.backup.BackupManager.DOMAIN_ATTACH;	 Catch:{ IOException -> 0x01af }
        r0 = r0.equals(r14);	 Catch:{ IOException -> 0x01af }
        if (r0 == 0) goto L_0x015c;
    L_0x00e2:
        r0 = r13.getFileName(r15);	 Catch:{ IOException -> 0x01af }
        r10 = r0;
        r0 = new java.io.File;	 Catch:{ IOException -> 0x01af }
        r2 = r20.getExternalCacheDir();	 Catch:{ IOException -> 0x01af }
        r0.<init>(r2, r10);	 Catch:{ IOException -> 0x01af }
        r8 = r0;
        r17 = 0;
        r2 = r21;
        r3 = r22;
        r5 = r24;
        r6 = r27;
        r18 = r8;
        r8 = r29;
        r19 = r10;
        r10 = r18;
        android.app.backup.FullBackup.restoreFile(r2, r3, r5, r6, r8, r10);	 Catch:{ all -> 0x014e }
        r2 = r18;
        r0 = android.os.ParcelFileDescriptor.open(r2, r1);	 Catch:{ all -> 0x014a }
        r1 = r0;
        r0 = r13.mBackupMeta;	 Catch:{ all -> 0x0148 }
        r0 = r0.feature;	 Catch:{ all -> 0x0148 }
        r0 = r13.checkVersion(r0);	 Catch:{ all -> 0x0148 }
        if (r0 == 0) goto L_0x011e;
    L_0x0117:
        r0 = r13.mBackupMeta;	 Catch:{ all -> 0x0148 }
        r16 = r13.onAttachRestore(r0, r1, r15);	 Catch:{ all -> 0x0148 }
        goto L_0x0120;
    L_0x011e:
        r0 = r13.mBackupManager;	 Catch:{ all -> 0x0148 }
    L_0x0120:
        r0 = r16;
        if (r0 == 0) goto L_0x013d;
    L_0x0124:
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0148 }
        r3.<init>();	 Catch:{ all -> 0x0148 }
        r4 = "err when attach restoring err = ";
        r3.append(r4);	 Catch:{ all -> 0x0148 }
        r3.append(r0);	 Catch:{ all -> 0x0148 }
        r3 = r3.toString();	 Catch:{ all -> 0x0148 }
        android.util.Log.w(r11, r3);	 Catch:{ all -> 0x0148 }
        r3 = r13.mBackupManager;	 Catch:{ all -> 0x0148 }
        r3.setWorkingError(r0);	 Catch:{ all -> 0x0148 }
    L_0x013d:
        if (r1 == 0) goto L_0x0142;
    L_0x013f:
        r1.close();	 Catch:{ IOException -> 0x01af }
    L_0x0142:
        r2.delete();	 Catch:{ IOException -> 0x01af }
        goto L_0x01ab;
    L_0x0148:
        r0 = move-exception;
        goto L_0x0153;
    L_0x014a:
        r0 = move-exception;
        r1 = r17;
        goto L_0x0153;
    L_0x014e:
        r0 = move-exception;
        r2 = r18;
        r1 = r17;
    L_0x0153:
        if (r1 == 0) goto L_0x0158;
    L_0x0155:
        r1.close();	 Catch:{ IOException -> 0x01af }
    L_0x0158:
        r2.delete();	 Catch:{ IOException -> 0x01af }
        throw r0;	 Catch:{ IOException -> 0x01af }
    L_0x015c:
        r0 = miui.app.backup.BackupManager.DOMAIN_END;	 Catch:{ IOException -> 0x01af }
        r0 = r0.equals(r14);	 Catch:{ IOException -> 0x01af }
        if (r0 == 0) goto L_0x0195;
    L_0x0164:
        r0 = r13.mBackupMeta;	 Catch:{ IOException -> 0x01af }
        r0 = r0.feature;	 Catch:{ IOException -> 0x01af }
        r0 = r13.checkVersion(r0);	 Catch:{ IOException -> 0x01af }
        if (r0 == 0) goto L_0x0175;
    L_0x016e:
        r0 = r13.mBackupMeta;	 Catch:{ IOException -> 0x01af }
        r16 = r13.onRestoreEnd(r0);	 Catch:{ IOException -> 0x01af }
        goto L_0x0177;
    L_0x0175:
        r0 = r13.mBackupManager;	 Catch:{ IOException -> 0x01af }
    L_0x0177:
        r0 = r16;
        if (r0 == 0) goto L_0x0194;
    L_0x017b:
        r1 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01af }
        r1.<init>();	 Catch:{ IOException -> 0x01af }
        r2 = "err when restore ending err = ";
        r1.append(r2);	 Catch:{ IOException -> 0x01af }
        r1.append(r0);	 Catch:{ IOException -> 0x01af }
        r1 = r1.toString();	 Catch:{ IOException -> 0x01af }
        android.util.Log.w(r11, r1);	 Catch:{ IOException -> 0x01af }
        r1 = r13.mBackupManager;	 Catch:{ IOException -> 0x01af }
        r1.setWorkingError(r0);	 Catch:{ IOException -> 0x01af }
    L_0x0194:
        goto L_0x01ab;
    L_0x0195:
        r2 = r13.mBackupMeta;	 Catch:{ IOException -> 0x01af }
        r1 = r20;
        r3 = r21;
        r4 = r22;
        r6 = r24;
        r7 = r25;
        r8 = r26;
        r9 = r27;
        r13 = r11;
        r11 = r29;
        r1.onOriginalAttachesRestore(r2, r3, r4, r6, r7, r8, r9, r11);	 Catch:{ IOException -> 0x01ad }
        return;
    L_0x01ad:
        r0 = move-exception;
        goto L_0x01b1;
    L_0x01af:
        r0 = move-exception;
        r13 = r11;
    L_0x01b1:
        r1 = "Exception when restore file";
        android.util.Log.e(r13, r1, r0);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.app.backup.FullBackupAgent.onRestoreFile(android.os.ParcelFileDescriptor, long, int, java.lang.String, java.lang.String, long, long):void");
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf(File.separator);
        return separatorIndex < 0 ? path : path.substring(separatorIndex + 1, path.length());
    }

    public final void onRestoreFile(ParcelFileDescriptor data, long size, File destination, int type, long mode, long mtime) throws IOException {
        super.onRestoreFile(data, size, destination, type, mode, mtime);
    }

    public void addAttachedFile(String path) {
        if (this.mAttachedFiles == null) {
            this.mAttachedFiles = new ArrayList();
        }
        this.mAttachedFiles.add(path);
    }

    public void addAttachedFile(Uri uri, String fileName) {
        if (this.mAttachedFiles == null) {
            this.mAttachedFiles = new ArrayList();
        }
        this.mAttachedFiles.add(new Pair(uri, fileName));
    }

    /* Access modifiers changed, original: protected */
    public void onOriginalAttachesRestore(BackupMeta meta, ParcelFileDescriptor data, long size, int type, String domain, String path, long mode, long mtime) throws IOException {
        super.onRestoreFile(data, size, type, domain, path, mode, mtime);
    }

    /* Access modifiers changed, original: protected */
    public int getVersion(int feature) {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public boolean checkVersion(int feature) {
        return this.mBackupMeta.version <= getVersion(feature);
    }

    /* Access modifiers changed, original: protected */
    public int onDataRestore(BackupMeta meta, ParcelFileDescriptor data) throws IOException {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public int onAttachRestore(BackupMeta meta, ParcelFileDescriptor fd, String path) throws IOException {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public int onRestoreEnd(BackupMeta meta) throws IOException {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public int onFullBackup(ParcelFileDescriptor data, int feature) throws IOException {
        return 0;
    }
}
