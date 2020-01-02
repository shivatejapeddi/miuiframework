package com.android.server.backup;

import android.accounts.AccountManagerInternal;
import android.app.backup.BlobBackupHelper;
import android.util.Slog;
import com.android.server.LocalServices;

public class AccountManagerBackupHelper extends BlobBackupHelper {
    private static final boolean DEBUG = false;
    private static final String KEY_ACCOUNT_ACCESS_GRANTS = "account_access_grants";
    private static final int STATE_VERSION = 1;
    private static final String TAG = "AccountsBackup";

    public AccountManagerBackupHelper() {
        super(1, KEY_ACCOUNT_ACCESS_GRANTS);
    }

    /* Access modifiers changed, original: protected */
    public byte[] getBackupPayload(String key) {
        String str = TAG;
        AccountManagerInternal am = (AccountManagerInternal) LocalServices.getService(AccountManagerInternal.class);
        int i = -1;
        try {
            if (key.hashCode() == 1544100736 && key.equals(KEY_ACCOUNT_ACCESS_GRANTS)) {
                i = 0;
            }
            if (i == 0) {
                return am.backupAccountAccessPermissions(0);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected backup key ");
            stringBuilder.append(key);
            Slog.w(str, stringBuilder.toString());
            return new byte[0];
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
        AccountManagerInternal am = (AccountManagerInternal) LocalServices.getService(AccountManagerInternal.class);
        int i = -1;
        try {
            if (key.hashCode() == 1544100736 && key.equals(KEY_ACCOUNT_ACCESS_GRANTS)) {
                i = 0;
            }
            if (i != 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected restore key ");
                stringBuilder.append(key);
                Slog.w(str, stringBuilder.toString());
                return;
            }
            am.restoreAccountAccessPermissions(payload, 0);
        } catch (Exception e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unable to restore key ");
            stringBuilder2.append(key);
            Slog.w(str, stringBuilder2.toString());
        }
    }
}
