package android.security;

import android.Manifest.permission;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.widget.ILockSettings;
import com.android.internal.widget.ILockSettings.Stub;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockPatternView.Cell;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import miui.security.SecurityManager;

public class MiuiLockPatternUtils extends LockPatternUtils {
    public static final int[] FAILED_ATTEMPT_TIMEOUT_SECONDS_ARRAY = new int[]{30, 60, 300};
    public static final String LOCKOUT_ATTEMPT_DEADLINE = "lockscreen.lockoutattemptdeadline";
    private static final String MATCHED_BLUETOOTH_DEVICE_ADDRESS_TO_UNLOCK = "bluetooth_address_to_unlock";
    private static final String MATCHED_BLUETOOTH_DEVICE_NAME_TO_UNLOCK = "bluetooth_name_to_unlock";
    private static final String MATCHED_BLUETOOTH_KEY_TO_UNLOCK = "bluetooth_key_to_unlock";
    private static final String MATCHED_BLUETOOTH_UNLOCK_STATUS = "bluetooth_unlock_status";
    @Deprecated
    public static final int MIUI_LOCK_PATTERN_DATA_TYPE_AC = 0;
    public static final int MIUI_LOCK_PATTERN_DATA_TYPE_GALLERY = 2;
    public static final int MIUI_LOCK_PATTERN_DATA_TYPE_PRIVACY_PASSWORD = 3;
    public static final int MIUI_LOCK_PATTERN_DATA_TYPE_SMS = 1;
    private static final String SYSTEM_DIRECTORY = "/system/";
    private static final String TAG = "MiuiLockPatternUtils";
    private static MiuiLockPatternData[] mMiuiLockPatternDatas = new MiuiLockPatternData[]{new MiuiLockPatternData("access_control", "access_control.key"), new MiuiLockPatternData("sms", "sms_private.key"), new MiuiLockPatternData("gallery", "gallery_private.key"), new MiuiLockPatternData("privacy_password_setting", "privacy_password_setting.key")};
    private Context mContext;
    private ILockSettings mLockSettingsService;
    private final boolean mMultiUserMode;
    private SecurityManager mSecurityManager;
    private int mType;

    public static class MiuiLockPatternData {
        public final AtomicBoolean mHaveNonZeroFile = new AtomicBoolean(false);
        public String mLockFile;
        public String mLockFilename;
        public FileObserver mPasswordObserver;
        public String mTag;

        public MiuiLockPatternData(String tag, String lockFile) {
            this.mTag = tag;
            this.mLockFile = lockFile;
        }
    }

    private static class PasswordFileObserver extends FileObserver {
        public PasswordFileObserver(String path, int mask) {
            super(path, mask);
        }

        public void onEvent(int event, String path) {
            for (MiuiLockPatternData data : MiuiLockPatternUtils.mMiuiLockPatternDatas) {
                if (data.mLockFile.equals(path) && !TextUtils.isEmpty(data.mLockFilename)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(data.mTag);
                    stringBuilder.append("password file changed");
                    Log.d(MiuiLockPatternUtils.TAG, stringBuilder.toString());
                    data.mHaveNonZeroFile.set(new File(data.mLockFilename).length() > 0);
                }
            }
        }
    }

    public MiuiLockPatternUtils(Context context) {
        this(context, 0);
    }

    public MiuiLockPatternUtils(Context context, int type) {
        super(context);
        this.mContext = context;
        this.mType = type;
        MiuiLockPatternData data = mMiuiLockPatternDatas[type];
        boolean z = true;
        if (data.mLockFilename == null) {
            String dataSystemDirectory = new StringBuilder();
            dataSystemDirectory.append(Environment.getDataDirectory().getAbsolutePath());
            dataSystemDirectory.append(SYSTEM_DIRECTORY);
            dataSystemDirectory = dataSystemDirectory.toString();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(dataSystemDirectory);
            stringBuilder.append(data.mLockFile);
            data.mLockFilename = stringBuilder.toString();
            data.mHaveNonZeroFile.set(new File(data.mLockFilename).length() > 0);
            data.mPasswordObserver = new PasswordFileObserver(dataSystemDirectory, MetricsEvent.APP_TRANSITION_CALLING_PACKAGE_NAME);
            data.mPasswordObserver.startWatching();
        }
        if (context.checkCallingOrSelfPermission(permission.INTERACT_ACROSS_USERS_FULL) != 0) {
            z = false;
        }
        this.mMultiUserMode = z;
        this.mSecurityManager = (SecurityManager) this.mContext.getSystemService(Context.SECURITY_SERVICE);
    }

    public boolean checkMiuiLockPattern(List<Cell> pattern) {
        if (VERSION.SDK_INT >= 24) {
            return PrivacyLockPatternUtils.checkPrivacyPasswordPattern(pattern, mMiuiLockPatternDatas[this.mType].mLockFilename, 0);
        }
        try {
            RandomAccessFile raf = new RandomAccessFile(mMiuiLockPatternDatas[this.mType].mLockFilename, "r");
            byte[] stored = new byte[((int) raf.length())];
            int got = raf.read(stored, 0, stored.length);
            raf.close();
            if (got <= 0) {
                return true;
            }
            return Arrays.equals(stored, LockPatternUtils.patternToHash(pattern));
        } catch (FileNotFoundException e) {
            return true;
        } catch (IOException e2) {
            return true;
        }
    }

    public boolean checkMiuiLockPatternAsUser(List<Cell> pattern, int userId) {
        if (userId != 0) {
            return TextUtils.equals(this.mSecurityManager.readSystemDataStringFile(getFileNameAsUser(userId)), new String(LockPatternUtils.patternToHash(pattern)));
        } else if (VERSION.SDK_INT >= 24) {
            return PrivacyLockPatternUtils.checkPrivacyPasswordPattern(pattern, mMiuiLockPatternDatas[this.mType].mLockFilename, userId);
        } else {
            return checkMiuiLockPattern(pattern);
        }
    }

    public boolean savedMiuiLockPatternExists() {
        return savedMiuiLockPatternExistsAsUser(UserHandle.myUserId());
    }

    public boolean savedMiuiLockPatternExistsAsUser(int userId) {
        if (userId == 0) {
            return mMiuiLockPatternDatas[this.mType].mHaveNonZeroFile.get();
        }
        return true;
    }

    public void saveMiuiLockPattern(List<Cell> pattern) {
        StringBuilder stringBuilder;
        String str = "Unable to save lock pattern to ";
        String str2 = TAG;
        if (VERSION.SDK_INT >= 24) {
            PrivacyLockPatternUtils.savePrivacyPasswordPattern(pattern, mMiuiLockPatternDatas[this.mType].mLockFilename, 0);
            return;
        }
        byte[] hash = LockPatternUtils.patternToHash(pattern);
        String filename = mMiuiLockPatternDatas[this.mType].mLockFilename;
        try {
            RandomAccessFile raf = new RandomAccessFile(filename, "rw");
            if (pattern == null) {
                raf.setLength(0);
            } else {
                raf.write(hash, 0, hash.length);
            }
            raf.close();
        } catch (FileNotFoundException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(filename);
            Log.e(str2, stringBuilder.toString());
        } catch (IOException e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(filename);
            Log.e(str2, stringBuilder.toString());
        }
    }

    public void saveMiuiLockPatternAsUser(List<Cell> pattern, int userId) {
        if (userId != 0) {
            byte[] hash = LockPatternUtils.patternToHash(pattern);
            String filename = getFileNameAsUser(userId);
            if (hash != null) {
                try {
                    this.mSecurityManager.putSystemDataStringFile(filename, new String(hash, "UTF-8"));
                    return;
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "save pattern as user failed");
                    return;
                }
            }
            this.mSecurityManager.putSystemDataStringFile(filename, "");
        } else if (VERSION.SDK_INT >= 24) {
            PrivacyLockPatternUtils.savePrivacyPasswordPattern(pattern, mMiuiLockPatternDatas[this.mType].mLockFilename, userId);
        } else {
            saveMiuiLockPattern(pattern);
        }
    }

    public void clearLockoutAttemptDeadline() {
        setLong(LOCKOUT_ATTEMPT_DEADLINE, 0);
    }

    public long setKeyguardLockoutAttemptDeadline(int failedAttempts) {
        long deadline = SystemClock.elapsedRealtime() + ((long) getTimeoutInMsByFailedAttempts(failedAttempts));
        setLong(LOCKOUT_ATTEMPT_DEADLINE, deadline);
        return deadline;
    }

    /* Access modifiers changed, original: protected */
    public long getLong(String secureSettingKey, long defaultValue) {
        try {
            return getLockSettingsService().getLong(secureSettingKey, defaultValue, getCurrentOrCallingUserId());
        } catch (RemoteException e) {
            return defaultValue;
        }
    }

    /* Access modifiers changed, original: protected */
    public void setLong(String secureSettingKey, long value) {
        setLong(secureSettingKey, value, getCurrentOrCallingUserId());
    }

    private void setLong(String secureSettingKey, long value, int userHandle) {
        try {
            getLockSettingsService().setLong(secureSettingKey, value, getCurrentOrCallingUserId());
        } catch (RemoteException re) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Couldn't write long ");
            stringBuilder.append(secureSettingKey);
            stringBuilder.append(re);
            Log.e(TAG, stringBuilder.toString());
        }
    }

    private ILockSettings getLockSettingsService() {
        if (this.mLockSettingsService == null) {
            this.mLockSettingsService = Stub.asInterface(ServiceManager.getService("lock_settings"));
        }
        return this.mLockSettingsService;
    }

    /* Access modifiers changed, original: 0000 */
    public int getCurrentOrCallingUserId() {
        if (this.mMultiUserMode) {
            return ActivityManager.getCurrentUser();
        }
        return UserHandle.getCallingUserId();
    }

    public void reportSuccessfulPasswordAttempt() {
        reportSuccessfulPasswordAttempt(UserHandle.getCallingUserId());
    }

    public void reportSuccessfulPasswordAttempt(int userId) {
        setLong(LOCKOUT_ATTEMPT_DEADLINE, 0);
        super.reportSuccessfulPasswordAttempt(userId);
    }

    public long getKeyguardLockoutAttemptDeadline(int failedAttempts) {
        String str = LOCKOUT_ATTEMPT_DEADLINE;
        long deadline = getLong(str, 0);
        long now = SystemClock.elapsedRealtime();
        if (deadline < now) {
            return 0;
        }
        if (deadline <= ((long) getTimeoutInMsByFailedAttempts(failedAttempts)) + now) {
            return deadline;
        }
        setLong(str, ((long) getTimeoutInMsByFailedAttempts(failedAttempts)) + now);
        return ((long) getTimeoutInMsByFailedAttempts(failedAttempts)) + now;
    }

    private int getTimeoutInMsByFailedAttempts(int failedAttempts) {
        return FAILED_ATTEMPT_TIMEOUT_SECONDS_ARRAY[Math.min(Math.max(failedAttempts - 5, 0), FAILED_ATTEMPT_TIMEOUT_SECONDS_ARRAY.length - 1)] * 1000;
    }

    public String getOwnerInfo() {
        return Secure.getString(this.mContext.getContentResolver(), Secure.LOCK_SCREEN_OWNER_INFO);
    }

    public void setBluetoothAddressToUnlock(String address) {
        if (checkAccessKeyguardStoragePermission()) {
            Secure.putString(this.mContext.getContentResolver(), MATCHED_BLUETOOTH_DEVICE_ADDRESS_TO_UNLOCK, address);
            return;
        }
        throw new SecurityException("Need android.permission.ACCESS_KEYGUARD_SECURE_STORAGE permission to access");
    }

    public String getBluetoothAddressToUnlock() {
        if (checkAccessKeyguardStoragePermission()) {
            return Secure.getString(this.mContext.getContentResolver(), MATCHED_BLUETOOTH_DEVICE_ADDRESS_TO_UNLOCK);
        }
        throw new SecurityException("Need android.permission.ACCESS_KEYGUARD_SECURE_STORAGE permission to access");
    }

    public void setBluetoothNameToUnlock(String name) {
        if (checkAccessKeyguardStoragePermission()) {
            Secure.putString(this.mContext.getContentResolver(), MATCHED_BLUETOOTH_DEVICE_NAME_TO_UNLOCK, name);
            return;
        }
        throw new SecurityException("Need android.permission.ACCESS_KEYGUARD_SECURE_STORAGE permission to access");
    }

    public String getBluetoothNameToUnlock() {
        if (checkAccessKeyguardStoragePermission()) {
            return Secure.getString(this.mContext.getContentResolver(), MATCHED_BLUETOOTH_DEVICE_NAME_TO_UNLOCK);
        }
        throw new SecurityException("Need android.permission.ACCESS_KEYGUARD_SECURE_STORAGE permission to access");
    }

    public void setBluetoothKeyToUnlock(String key) {
        if (checkAccessKeyguardStoragePermission()) {
            Secure.putString(this.mContext.getContentResolver(), MATCHED_BLUETOOTH_KEY_TO_UNLOCK, key);
            return;
        }
        throw new SecurityException("Need android.permission.ACCESS_KEYGUARD_SECURE_STORAGE permission to access");
    }

    public String getBluetoothKeyToUnlock() {
        if (checkAccessKeyguardStoragePermission()) {
            return Secure.getString(this.mContext.getContentResolver(), MATCHED_BLUETOOTH_KEY_TO_UNLOCK);
        }
        throw new SecurityException("Need android.permission.ACCESS_KEYGUARD_SECURE_STORAGE permission to access");
    }

    public void setBluetoothUnlockEnabled(boolean enable) {
        if (checkAccessKeyguardStoragePermission()) {
            Secure.putInt(this.mContext.getContentResolver(), MATCHED_BLUETOOTH_UNLOCK_STATUS, enable);
            return;
        }
        throw new SecurityException("Need android.permission.ACCESS_KEYGUARD_SECURE_STORAGE permission to access");
    }

    public boolean getBluetoothUnlockEnabled() {
        if (checkAccessKeyguardStoragePermission()) {
            return Secure.getInt(this.mContext.getContentResolver(), MATCHED_BLUETOOTH_UNLOCK_STATUS, 0) != 0;
        } else {
            throw new SecurityException("Need android.permission.ACCESS_KEYGUARD_SECURE_STORAGE permission to access");
        }
    }

    private boolean checkAccessKeyguardStoragePermission() {
        String permissionString = permission.ACCESS_KEYGUARD_SECURE_STORAGE;
        return this.mContext.checkPermission(permission.ACCESS_KEYGUARD_SECURE_STORAGE, Process.myPid(), Process.myUid()) == 0;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean savedPatternExists() {
        try {
            return getLockSettingsService().havePattern(getCurrentOrCallingUserId());
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setKeyguardPasswordQuality(int quality) {
        String permissionString = "miui.permission.USE_INTERNAL_GENERAL_API";
        if (this.mContext.checkPermission("miui.permission.USE_INTERNAL_GENERAL_API", Process.myPid(), Process.myUid()) == 0) {
            setLong(LockPatternUtils.PASSWORD_TYPE_KEY, (long) quality);
            return;
        }
        throw new SecurityException("Need miui.permission.USE_INTERNAL_GENERAL_API permission to access");
    }

    private String getFileNameAsUser(int userId) {
        String filename = mMiuiLockPatternDatas[this.mType].mLockFilename;
        if (userId != 0) {
            return new File(Environment.getUserSystemDirectory(userId), mMiuiLockPatternDatas[this.mType].mLockFile).getAbsolutePath();
        }
        return filename;
    }

    public static void savePrivacyPasswordPattern(String pattern, String filename, int userId) {
        if (pattern != null) {
            RandomAccessFile raf = null;
            try {
                byte[] hash = LockPatternUtils.patternToHash(LockPatternUtils.stringToPattern(pattern));
                raf = new RandomAccessFile(filename, "rw");
                if (hash != null) {
                    if (hash.length != 0) {
                        raf.write(hash, 0, hash.length);
                        raf.close();
                    }
                }
                raf.setLength(0);
                try {
                    raf.close();
                } catch (IOException e) {
                }
            } catch (Exception e2) {
                Log.e(TAG, "savePrivacyPasswordPattern error", e2);
                throw new RuntimeException(e2);
            } catch (Throwable th) {
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e3) {
                    }
                }
            }
        } else {
            throw new RuntimeException("pattern is null");
        }
    }

    public static boolean checkPrivacyPasswordPattern(String pattern, String filename, int userId) {
        if (pattern != null) {
            RandomAccessFile raf = null;
            boolean z = false;
            try {
                List<Cell> stringToPattern = LockPatternUtils.stringToPattern(pattern);
                raf = new RandomAccessFile(filename, "r");
                byte[] stored = new byte[((int) raf.length())];
                raf.readFully(stored, 0, stored.length);
                z = Arrays.equals(stored, LockPatternUtils.patternToHash(stringToPattern));
                try {
                    raf.close();
                } catch (IOException e) {
                }
                return z;
            } catch (Exception e2) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("checkPrivacyPasswordPattern error ");
                stringBuilder.append(filename);
                Log.e(str, stringBuilder.toString(), e2);
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e3) {
                    }
                }
                return z;
            } catch (Throwable th) {
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e4) {
                    }
                }
            }
        } else {
            throw new RuntimeException("pattern is null");
        }
    }

    public long getLockPasswordLength(int userId) {
        long j = 4;
        try {
            j = getLockSettings().getLong(LockPatternUtils.PASSWORD_LENGTH_KEY, 4, userId);
            return j;
        } catch (RemoteException re) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getLockPasswordLength error ");
            stringBuilder.append(userId);
            Log.e(TAG, stringBuilder.toString(), re);
            return j;
        }
    }
}
