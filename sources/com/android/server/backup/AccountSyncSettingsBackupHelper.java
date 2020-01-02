package com.android.server.backup;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.backup.BackupDataInputStream;
import android.app.backup.BackupDataOutput;
import android.app.backup.BackupHelper;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncAdapterType;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.security.keystore.KeyProperties;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountSyncSettingsBackupHelper implements BackupHelper {
    private static final boolean DEBUG = false;
    private static final String JSON_FORMAT_ENCODING = "UTF-8";
    private static final String JSON_FORMAT_HEADER_KEY = "account_data";
    private static final int JSON_FORMAT_VERSION = 1;
    private static final String KEY_ACCOUNTS = "accounts";
    private static final String KEY_ACCOUNT_AUTHORITIES = "authorities";
    private static final String KEY_ACCOUNT_NAME = "name";
    private static final String KEY_ACCOUNT_TYPE = "type";
    private static final String KEY_AUTHORITY_NAME = "name";
    private static final String KEY_AUTHORITY_SYNC_ENABLED = "syncEnabled";
    private static final String KEY_AUTHORITY_SYNC_STATE = "syncState";
    private static final String KEY_MASTER_SYNC_ENABLED = "masterSyncEnabled";
    private static final String KEY_VERSION = "version";
    private static final int MD5_BYTE_SIZE = 16;
    private static final String STASH_FILE = "/backup/unadded_account_syncsettings.json";
    private static final int STATE_VERSION = 1;
    private static final int SYNC_REQUEST_LATCH_TIMEOUT_SECONDS = 1;
    private static final String TAG = "AccountSyncSettingsBackupHelper";
    private AccountManager mAccountManager = AccountManager.get(this.mContext);
    private Context mContext;
    private final int mUserId;

    public AccountSyncSettingsBackupHelper(Context context, int userId) {
        this.mContext = context;
        this.mUserId = userId;
    }

    public void performBackup(ParcelFileDescriptor oldState, BackupDataOutput output, ParcelFileDescriptor newState) {
        String str = TAG;
        try {
            byte[] dataBytes = serializeAccountSyncSettingsToJSON(this.mUserId).toString().getBytes(JSON_FORMAT_ENCODING);
            byte[] oldMd5Checksum = readOldMd5Checksum(oldState);
            byte[] newMd5Checksum = generateMd5Checksum(dataBytes);
            if (Arrays.equals(oldMd5Checksum, newMd5Checksum)) {
                Log.i(str, "Old and new MD5 checksums match. Skipping backup.");
            } else {
                int dataSize = dataBytes.length;
                output.writeEntityHeader(JSON_FORMAT_HEADER_KEY, dataSize);
                output.writeEntityData(dataBytes, dataSize);
                Log.i(str, "Backup successful.");
            }
            writeNewMd5Checksum(newState, newMd5Checksum);
        } catch (IOException | NoSuchAlgorithmException | JSONException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Couldn't backup account sync settings\n");
            stringBuilder.append(e);
            Log.e(str, stringBuilder.toString());
        }
    }

    private JSONObject serializeAccountSyncSettingsToJSON(int userId) throws JSONException {
        int i = userId;
        Account[] accounts = this.mAccountManager.getAccountsAsUser(i);
        SyncAdapterType[] syncAdapters = ContentResolver.getSyncAdapterTypesAsUser(userId);
        HashMap<String, List<String>> accountTypeToAuthorities = new HashMap();
        int i2 = 0;
        for (SyncAdapterType syncAdapter : syncAdapters) {
            if (syncAdapter.isUserVisible()) {
                if (!accountTypeToAuthorities.containsKey(syncAdapter.accountType)) {
                    accountTypeToAuthorities.put(syncAdapter.accountType, new ArrayList());
                }
                ((List) accountTypeToAuthorities.get(syncAdapter.accountType)).add(syncAdapter.authority);
            }
        }
        JSONObject backupJSON = new JSONObject();
        backupJSON.put("version", 1);
        backupJSON.put(KEY_MASTER_SYNC_ENABLED, ContentResolver.getMasterSyncAutomaticallyAsUser(userId));
        JSONArray accountJSONArray = new JSONArray();
        int length = accounts.length;
        while (i2 < length) {
            Account[] accounts2;
            SyncAdapterType[] syncAdapters2;
            Account account = accounts[i2];
            List<String> authorities = (List) accountTypeToAuthorities.get(account.type);
            if (authorities == null) {
                accounts2 = accounts;
                syncAdapters2 = syncAdapters;
            } else if (authorities.isEmpty()) {
                accounts2 = accounts;
                syncAdapters2 = syncAdapters;
            } else {
                JSONObject accountJSON = new JSONObject();
                String str = "name";
                accountJSON.put(str, account.name);
                accountJSON.put("type", account.type);
                JSONArray authoritiesJSONArray = new JSONArray();
                for (String authority : authorities) {
                    int syncState = ContentResolver.getIsSyncableAsUser(account, authority, i);
                    accounts2 = accounts;
                    accounts = ContentResolver.getSyncAutomaticallyAsUser(account, authority, i);
                    JSONObject authorityJSON = new JSONObject();
                    authorityJSON.put(str, authority);
                    syncAdapters2 = syncAdapters;
                    authorityJSON.put(KEY_AUTHORITY_SYNC_STATE, syncState);
                    authorityJSON.put(KEY_AUTHORITY_SYNC_ENABLED, accounts);
                    authoritiesJSONArray.put(authorityJSON);
                    i = userId;
                    accounts = accounts2;
                    syncAdapters = syncAdapters2;
                }
                accounts2 = accounts;
                syncAdapters2 = syncAdapters;
                accountJSON.put("authorities", authoritiesJSONArray);
                accountJSONArray.put(accountJSON);
            }
            i2++;
            i = userId;
            accounts = accounts2;
            syncAdapters = syncAdapters2;
        }
        backupJSON.put("accounts", accountJSONArray);
        return backupJSON;
    }

    private byte[] readOldMd5Checksum(ParcelFileDescriptor oldState) throws IOException {
        DataInputStream dataInput = new DataInputStream(new FileInputStream(oldState.getFileDescriptor()));
        byte[] oldMd5Checksum = new byte[16];
        try {
            int stateVersion = dataInput.readInt();
            if (stateVersion <= 1) {
                for (int i = 0; i < 16; i++) {
                    oldMd5Checksum[i] = dataInput.readByte();
                }
            } else {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Backup state version is: ");
                stringBuilder.append(stateVersion);
                stringBuilder.append(" (support only up to version ");
                stringBuilder.append(1);
                stringBuilder.append(")");
                Log.i(str, stringBuilder.toString());
            }
        } catch (EOFException e) {
        }
        return oldMd5Checksum;
    }

    private void writeNewMd5Checksum(ParcelFileDescriptor newState, byte[] md5Checksum) throws IOException {
        DataOutputStream dataOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(newState.getFileDescriptor())));
        dataOutput.writeInt(1);
        dataOutput.write(md5Checksum);
    }

    private byte[] generateMd5Checksum(byte[] data) throws NoSuchAlgorithmException {
        if (data == null) {
            return null;
        }
        return MessageDigest.getInstance(KeyProperties.DIGEST_MD5).digest(data);
    }

    public void restoreEntity(BackupDataInputStream data) {
        String str = TAG;
        byte[] dataBytes = new byte[data.size()];
        boolean masterSyncEnabled;
        try {
            data.read(dataBytes);
            JSONObject dataJSON = new JSONObject(new String(dataBytes, JSON_FORMAT_ENCODING));
            masterSyncEnabled = dataJSON.getBoolean(KEY_MASTER_SYNC_ENABLED);
            JSONArray accountJSONArray = dataJSON.getJSONArray("accounts");
            if (ContentResolver.getMasterSyncAutomaticallyAsUser(this.mUserId)) {
                ContentResolver.setMasterSyncAutomaticallyAsUser(false, this.mUserId);
            }
            restoreFromJsonArray(accountJSONArray, this.mUserId);
            ContentResolver.setMasterSyncAutomaticallyAsUser(masterSyncEnabled, this.mUserId);
            Log.i(str, "Restore successful.");
        } catch (IOException | JSONException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Couldn't restore account sync settings\n");
            stringBuilder.append(e);
            Log.e(str, stringBuilder.toString());
        } catch (Throwable th) {
            ContentResolver.setMasterSyncAutomaticallyAsUser(masterSyncEnabled, this.mUserId);
        }
    }

    /* JADX WARNING: Missing block: B:26:?, code skipped:
            $closeResource(r3, r2);
     */
    private void restoreFromJsonArray(org.json.JSONArray r9, int r10) throws org.json.JSONException {
        /*
        r8 = this;
        r0 = r8.getAccounts(r10);
        r1 = new org.json.JSONArray;
        r1.<init>();
        r2 = 0;
    L_0x000a:
        r3 = r9.length();
        if (r2 >= r3) goto L_0x003f;
    L_0x0010:
        r3 = r9.get(r2);
        r3 = (org.json.JSONObject) r3;
        r4 = "name";
        r4 = r3.getString(r4);
        r5 = "type";
        r5 = r3.getString(r5);
        r6 = 0;
        r7 = new android.accounts.Account;	 Catch:{ IllegalArgumentException -> 0x003a }
        r7.<init>(r4, r5);	 Catch:{ IllegalArgumentException -> 0x003a }
        r6 = r7;
        r7 = r0.contains(r6);
        if (r7 == 0) goto L_0x0036;
    L_0x0032:
        r8.restoreExistingAccountSyncSettingsFromJSON(r3, r10);
        goto L_0x003c;
    L_0x0036:
        r1.put(r3);
        goto L_0x003c;
    L_0x003a:
        r7 = move-exception;
    L_0x003c:
        r2 = r2 + 1;
        goto L_0x000a;
    L_0x003f:
        r2 = r1.length();
        if (r2 <= 0) goto L_0x0070;
    L_0x0045:
        r2 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x0066 }
        r3 = getStashFile(r10);	 Catch:{ IOException -> 0x0066 }
        r2.<init>(r3);	 Catch:{ IOException -> 0x0066 }
        r3 = 0;
        r4 = r1.toString();	 Catch:{ all -> 0x005f }
        r5 = new java.io.DataOutputStream;	 Catch:{ all -> 0x005f }
        r5.<init>(r2);	 Catch:{ all -> 0x005f }
        r5.writeUTF(r4);	 Catch:{ all -> 0x005f }
        $closeResource(r3, r2);	 Catch:{ IOException -> 0x0066 }
        goto L_0x006f;
    L_0x005f:
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x0061 }
    L_0x0061:
        r4 = move-exception;
        $closeResource(r3, r2);	 Catch:{ IOException -> 0x0066 }
        throw r4;	 Catch:{ IOException -> 0x0066 }
    L_0x0066:
        r2 = move-exception;
        r3 = "AccountSyncSettingsBackupHelper";
        r4 = "unable to write the sync settings to the stash file";
        android.util.Log.e(r3, r4, r2);
    L_0x006f:
        goto L_0x007d;
    L_0x0070:
        r2 = getStashFile(r10);
        r3 = r2.exists();
        if (r3 == 0) goto L_0x007d;
    L_0x007a:
        r2.delete();
    L_0x007d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.AccountSyncSettingsBackupHelper.restoreFromJsonArray(org.json.JSONArray, int):void");
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 != null) {
            try {
                x1.close();
                return;
            } catch (Throwable th) {
                x0.addSuppressed(th);
                return;
            }
        }
        x1.close();
    }

    /* JADX WARNING: Missing block: B:18:?, code skipped:
            $closeResource(r1, r0);
     */
    private void accountAddedInternal(int r5) {
        /*
        r4 = this;
        r0 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x0034, IOException -> 0x0032 }
        r1 = getStashFile(r5);	 Catch:{ FileNotFoundException -> 0x0034, IOException -> 0x0032 }
        r0.<init>(r1);	 Catch:{ FileNotFoundException -> 0x0034, IOException -> 0x0032 }
        r1 = 0;
        r2 = new java.io.DataInputStream;	 Catch:{ all -> 0x002b }
        r2.<init>(r0);	 Catch:{ all -> 0x002b }
        r3 = r2.readUTF();	 Catch:{ all -> 0x002b }
        r2 = r3;
        $closeResource(r1, r0);	 Catch:{ FileNotFoundException -> 0x0034, IOException -> 0x0032 }
        r0 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x0021 }
        r0.<init>(r2);	 Catch:{ JSONException -> 0x0021 }
        r4.restoreFromJsonArray(r0, r5);	 Catch:{ JSONException -> 0x0021 }
        goto L_0x002a;
    L_0x0021:
        r0 = move-exception;
        r1 = "AccountSyncSettingsBackupHelper";
        r3 = "there was an error with the stashed sync settings";
        android.util.Log.e(r1, r3, r0);
    L_0x002a:
        return;
    L_0x002b:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x002d }
    L_0x002d:
        r2 = move-exception;
        $closeResource(r1, r0);	 Catch:{ FileNotFoundException -> 0x0034, IOException -> 0x0032 }
        throw r2;	 Catch:{ FileNotFoundException -> 0x0034, IOException -> 0x0032 }
    L_0x0032:
        r0 = move-exception;
        return;
    L_0x0034:
        r0 = move-exception;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.AccountSyncSettingsBackupHelper.accountAddedInternal(int):void");
    }

    public static void accountAdded(Context context, int userId) {
        new AccountSyncSettingsBackupHelper(context, userId).accountAddedInternal(userId);
    }

    private Set<Account> getAccounts(int userId) {
        Account[] accounts = this.mAccountManager.getAccountsAsUser(userId);
        Set<Account> accountHashSet = new HashSet();
        for (Account account : accounts) {
            accountHashSet.add(account);
        }
        return accountHashSet;
    }

    private void restoreExistingAccountSyncSettingsFromJSON(JSONObject accountJSON, int userId) throws JSONException {
        JSONArray authorities = accountJSON.getJSONArray("authorities");
        String str = "name";
        Account account = new Account(accountJSON.getString(str), accountJSON.getString("type"));
        for (int i = 0; i < authorities.length(); i++) {
            JSONObject authority = (JSONObject) authorities.get(i);
            String authorityName = authority.getString(str);
            boolean wasSyncEnabled = authority.getBoolean(KEY_AUTHORITY_SYNC_ENABLED);
            int wasSyncable = authority.getInt(KEY_AUTHORITY_SYNC_STATE);
            ContentResolver.setSyncAutomaticallyAsUser(account, authorityName, wasSyncEnabled, userId);
            if (!wasSyncEnabled) {
                ContentResolver.setIsSyncableAsUser(account, authorityName, wasSyncable == 0 ? 0 : 2, userId);
            }
        }
    }

    public void writeNewStateDescription(ParcelFileDescriptor newState) {
    }

    private static File getStashFile(int userId) {
        File baseDir;
        if (userId == 0) {
            baseDir = Environment.getDataDirectory();
        } else {
            baseDir = Environment.getDataSystemCeDirectory(userId);
        }
        return new File(baseDir, STASH_FILE);
    }
}
