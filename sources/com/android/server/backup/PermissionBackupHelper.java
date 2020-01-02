package com.android.server.backup;

import android.app.backup.BlobBackupHelper;
import android.os.UserHandle;
import android.permission.PermissionManagerInternal;
import android.util.Slog;
import com.android.server.LocalServices;

public class PermissionBackupHelper extends BlobBackupHelper {
    private static final boolean DEBUG = false;
    private static final String KEY_PERMISSIONS = "permissions";
    private static final int STATE_VERSION = 1;
    private static final String TAG = "PermissionBackup";
    private final PermissionManagerInternal mPermissionManager = ((PermissionManagerInternal) LocalServices.getService(PermissionManagerInternal.class));
    private final UserHandle mUser;

    public PermissionBackupHelper(int userId) {
        super(1, KEY_PERMISSIONS);
        this.mUser = UserHandle.of(userId);
    }

    /* Access modifiers changed, original: protected */
    public byte[] getBackupPayload(String key) {
        String str = TAG;
        Object obj = -1;
        try {
            if (key.hashCode() == 1133704324 && key.equals(KEY_PERMISSIONS)) {
                obj = null;
            }
            if (obj == null) {
                return this.mPermissionManager.backupRuntimePermissions(this.mUser);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected backup key ");
            stringBuilder.append(key);
            Slog.w(str, stringBuilder.toString());
            return null;
        } catch (Exception e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unable to store payload ");
            stringBuilder2.append(key);
            Slog.e(str, stringBuilder2.toString());
        }
    }

    /* Access modifiers changed, original: protected */
    public void applyRestoredPayload(String key, byte[] payload) {
        String str = TAG;
        Object obj = -1;
        try {
            if (key.hashCode() == 1133704324 && key.equals(KEY_PERMISSIONS)) {
                obj = null;
            }
            if (obj != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected restore key ");
                stringBuilder.append(key);
                Slog.w(str, stringBuilder.toString());
                return;
            }
            this.mPermissionManager.restoreRuntimePermissions(payload, this.mUser);
        } catch (Exception e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unable to restore key ");
            stringBuilder2.append(key);
            Slog.w(str, stringBuilder2.toString());
        }
    }
}
