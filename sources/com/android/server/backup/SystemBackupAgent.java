package com.android.server.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupHelper;
import android.app.backup.FullBackupDataOutput;
import android.app.backup.WallpaperBackupHelper;
import android.content.Context;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.UserHandle;
import com.google.android.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class SystemBackupAgent extends BackupAgentHelper {
    private static final String ACCOUNT_MANAGER_HELPER = "account_manager";
    private static final String NOTIFICATION_HELPER = "notifications";
    private static final String PERMISSION_HELPER = "permissions";
    private static final String PREFERRED_HELPER = "preferred_activities";
    private static final String SHORTCUT_MANAGER_HELPER = "shortcut_manager";
    private static final String SLICES_HELPER = "slices";
    private static final String SYNC_SETTINGS_HELPER = "account_sync_settings";
    private static final String TAG = "SystemBackupAgent";
    private static final String USAGE_STATS_HELPER = "usage_stats";
    private static final String WALLPAPER_HELPER = "wallpaper";
    public static final String WALLPAPER_IMAGE = new File(Environment.getUserSystemDirectory(0), Context.WALLPAPER_SERVICE).getAbsolutePath();
    private static final String WALLPAPER_IMAGE_DIR = Environment.getUserSystemDirectory(0).getAbsolutePath();
    private static final String WALLPAPER_IMAGE_FILENAME = "wallpaper";
    private static final String WALLPAPER_IMAGE_KEY = "/data/data/com.android.settings/files/wallpaper";
    public static final String WALLPAPER_INFO = new File(Environment.getUserSystemDirectory(0), WALLPAPER_INFO_FILENAME).getAbsolutePath();
    private static final String WALLPAPER_INFO_DIR = Environment.getUserSystemDirectory(0).getAbsolutePath();
    private static final String WALLPAPER_INFO_FILENAME = "wallpaper_info.xml";
    private static final Set<String> sEligibleForMultiUser = Sets.newArraySet(PERMISSION_HELPER, "notifications", SYNC_SETTINGS_HELPER);
    private int mUserId = 0;

    public void onCreate(UserHandle user) {
        super.onCreate(user);
        this.mUserId = user.getIdentifier();
        addHelper(SYNC_SETTINGS_HELPER, new AccountSyncSettingsBackupHelper(this, this.mUserId));
        addHelper(PREFERRED_HELPER, new PreferredActivityBackupHelper());
        addHelper("notifications", new NotificationBackupHelper(this.mUserId));
        addHelper(PERMISSION_HELPER, new PermissionBackupHelper(this.mUserId));
        addHelper(USAGE_STATS_HELPER, new UsageStatsBackupHelper(this));
        addHelper(SHORTCUT_MANAGER_HELPER, new ShortcutBackupHelper());
        addHelper(ACCOUNT_MANAGER_HELPER, new AccountManagerBackupHelper());
        addHelper(SLICES_HELPER, new SliceBackupHelper(this));
    }

    public void onFullBackup(FullBackupDataOutput data) throws IOException {
    }

    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
        String str = "/data/data/com.android.settings/files/wallpaper";
        addHelper(Context.WALLPAPER_SERVICE, new WallpaperBackupHelper(this, new String[]{str}));
        addHelper("system_files", new WallpaperBackupHelper(this, new String[]{str}));
        super.onRestore(data, appVersionCode, newState);
    }

    public void addHelper(String keyPrefix, BackupHelper helper) {
        if (this.mUserId == 0 || sEligibleForMultiUser.contains(keyPrefix)) {
            super.addHelper(keyPrefix, helper);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0058 A:{SYNTHETIC, Splitter:B:10:0x0058} */
    /* JADX WARNING: Removed duplicated region for block: B:29:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0089 A:{Catch:{ IOException -> 0x00af }} */
    public void onRestoreFile(android.os.ParcelFileDescriptor r17, long r18, int r20, java.lang.String r21, java.lang.String r22, long r23, long r25) throws java.io.IOException {
        /*
        r16 = this;
        r1 = r21;
        r2 = r22;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r3 = "Restoring file domain=";
        r0.append(r3);
        r0.append(r1);
        r3 = " path=";
        r0.append(r3);
        r0.append(r2);
        r0 = r0.toString();
        r3 = "SystemBackupAgent";
        android.util.Slog.i(r3, r0);
        r0 = 0;
        r4 = 0;
        r5 = "r";
        r5 = r1.equals(r5);
        r6 = "wallpaper";
        if (r5 == 0) goto L_0x0055;
    L_0x0030:
        r5 = "wallpaper_info.xml";
        r5 = r2.equals(r5);
        if (r5 == 0) goto L_0x0044;
    L_0x0039:
        r5 = new java.io.File;
        r7 = WALLPAPER_INFO;
        r5.<init>(r7);
        r4 = r5;
        r0 = 1;
        r5 = r0;
        goto L_0x0056;
    L_0x0044:
        r5 = r2.equals(r6);
        if (r5 == 0) goto L_0x0055;
    L_0x004a:
        r5 = new java.io.File;
        r7 = WALLPAPER_IMAGE;
        r5.<init>(r7);
        r4 = r5;
        r0 = 1;
        r5 = r0;
        goto L_0x0056;
    L_0x0055:
        r5 = r0;
    L_0x0056:
        if (r4 != 0) goto L_0x0079;
    L_0x0058:
        r0 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00af }
        r0.<init>();	 Catch:{ IOException -> 0x00af }
        r7 = "Skipping unrecognized system file: [ ";
        r0.append(r7);	 Catch:{ IOException -> 0x00af }
        r0.append(r1);	 Catch:{ IOException -> 0x00af }
        r7 = " : ";
        r0.append(r7);	 Catch:{ IOException -> 0x00af }
        r0.append(r2);	 Catch:{ IOException -> 0x00af }
        r7 = " ]";
        r0.append(r7);	 Catch:{ IOException -> 0x00af }
        r0 = r0.toString();	 Catch:{ IOException -> 0x00af }
        android.util.Slog.w(r3, r0);	 Catch:{ IOException -> 0x00af }
    L_0x0079:
        r7 = r17;
        r8 = r18;
        r10 = r20;
        r11 = r23;
        r13 = r25;
        r15 = r4;
        android.app.backup.FullBackup.restoreFile(r7, r8, r10, r11, r13, r15);	 Catch:{ IOException -> 0x00af }
        if (r5 == 0) goto L_0x00ae;
        r0 = android.os.ServiceManager.getService(r6);	 Catch:{ IOException -> 0x00af }
        r0 = (android.app.IWallpaperManager) r0;	 Catch:{ IOException -> 0x00af }
        r6 = r0;
        if (r6 == 0) goto L_0x00ae;
    L_0x0093:
        r6.settingsRestored();	 Catch:{ RemoteException -> 0x0097 }
        goto L_0x00ae;
    L_0x0097:
        r0 = move-exception;
        r7 = r0;
        r0 = r7;
        r7 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00af }
        r7.<init>();	 Catch:{ IOException -> 0x00af }
        r8 = "Couldn't restore settings\n";
        r7.append(r8);	 Catch:{ IOException -> 0x00af }
        r7.append(r0);	 Catch:{ IOException -> 0x00af }
        r7 = r7.toString();	 Catch:{ IOException -> 0x00af }
        android.util.Slog.e(r3, r7);	 Catch:{ IOException -> 0x00af }
    L_0x00ae:
        goto L_0x00c6;
    L_0x00af:
        r0 = move-exception;
        if (r5 == 0) goto L_0x00c6;
    L_0x00b2:
        r3 = new java.io.File;
        r6 = WALLPAPER_IMAGE;
        r3.<init>(r6);
        r3.delete();
        r3 = new java.io.File;
        r6 = WALLPAPER_INFO;
        r3.<init>(r6);
        r3.delete();
    L_0x00c6:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.SystemBackupAgent.onRestoreFile(android.os.ParcelFileDescriptor, long, int, java.lang.String, java.lang.String, long, long):void");
    }
}
