package android.app.backup;

import android.annotation.UnsupportedAppUsage;
import android.app.WallpaperManager;
import android.content.Context;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import java.io.File;

public class WallpaperBackupHelper extends FileBackupHelperBase implements BackupHelper {
    private static final boolean DEBUG = false;
    private static final String STAGE_FILE = new File(Environment.getUserSystemDirectory(0), "wallpaper-tmp").getAbsolutePath();
    private static final String TAG = "WallpaperBackupHelper";
    public static final String WALLPAPER_IMAGE_KEY = "/data/data/com.android.settings/files/wallpaper";
    public static final String WALLPAPER_INFO_KEY = "/data/system/wallpaper_info.xml";
    private final String[] mKeys;
    private final WallpaperManager mWpm;

    @UnsupportedAppUsage
    public /* bridge */ /* synthetic */ void writeNewStateDescription(ParcelFileDescriptor parcelFileDescriptor) {
        super.writeNewStateDescription(parcelFileDescriptor);
    }

    public WallpaperBackupHelper(Context context, String[] keys) {
        super(context);
        this.mContext = context;
        this.mKeys = keys;
        this.mWpm = (WallpaperManager) context.getSystemService(Context.WALLPAPER_SERVICE);
    }

    public void performBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) {
    }

    /* JADX WARNING: Missing block: B:20:?, code skipped:
            r2.close();
     */
    public void restoreEntity(android.app.backup.BackupDataInputStream r8) {
        /*
        r7 = this;
        r0 = r8.getKey();
        r1 = r7.mKeys;
        r1 = r7.isKeyInList(r0, r1);
        if (r1 == 0) goto L_0x0066;
    L_0x000c:
        r1 = "/data/data/com.android.settings/files/wallpaper";
        r1 = r0.equals(r1);
        if (r1 == 0) goto L_0x0066;
    L_0x0014:
        r1 = new java.io.File;
        r2 = STAGE_FILE;
        r1.<init>(r2);
        r2 = r7.writeFile(r1, r8);	 Catch:{ all -> 0x0061 }
        r3 = "WallpaperBackupHelper";
        if (r2 == 0) goto L_0x0058;
    L_0x0023:
        r2 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x003d }
        r2.<init>(r1);	 Catch:{ IOException -> 0x003d }
        r4 = r7.mWpm;	 Catch:{ all -> 0x0031 }
        r4.setStream(r2);	 Catch:{ all -> 0x0031 }
        r2.close();	 Catch:{ IOException -> 0x003d }
        goto L_0x005d;
    L_0x0031:
        r4 = move-exception;
        throw r4;	 Catch:{ all -> 0x0033 }
    L_0x0033:
        r5 = move-exception;
        r2.close();	 Catch:{ all -> 0x0038 }
        goto L_0x003c;
    L_0x0038:
        r6 = move-exception;
        r4.addSuppressed(r6);	 Catch:{ IOException -> 0x003d }
    L_0x003c:
        throw r5;	 Catch:{ IOException -> 0x003d }
    L_0x003d:
        r2 = move-exception;
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0061 }
        r4.<init>();	 Catch:{ all -> 0x0061 }
        r5 = "Unable to set restored wallpaper: ";
        r4.append(r5);	 Catch:{ all -> 0x0061 }
        r5 = r2.getMessage();	 Catch:{ all -> 0x0061 }
        r4.append(r5);	 Catch:{ all -> 0x0061 }
        r4 = r4.toString();	 Catch:{ all -> 0x0061 }
        android.util.Slog.e(r3, r4);	 Catch:{ all -> 0x0061 }
        goto L_0x005d;
    L_0x0058:
        r2 = "Unable to save restored wallpaper";
        android.util.Slog.e(r3, r2);	 Catch:{ all -> 0x0061 }
    L_0x005d:
        r1.delete();
        goto L_0x0066;
    L_0x0061:
        r2 = move-exception;
        r1.delete();
        throw r2;
    L_0x0066:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.backup.WallpaperBackupHelper.restoreEntity(android.app.backup.BackupDataInputStream):void");
    }
}
