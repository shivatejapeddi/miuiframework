package miui.securityspace;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import miui.app.Activity;

public class CrossUserPickerActivity extends Activity {
    private static final String TAG = "CrossUserPickerActivity";
    public static final int USER_ID_INVALID = -1;
    private volatile ContentResolver mCrossUserContentResolver;
    private volatile ContextWrapper mCrossUserContextWrapper;
    private final Object mLockObject = new Object();

    class CrossUserContextWrapper extends ContextWrapper {
        Context mBase;
        UserHandle mCrossUser;

        public CrossUserContextWrapper(Context base, UserHandle userHandle) {
            super(base);
            this.mBase = base;
            this.mCrossUser = userHandle;
        }

        public ContentResolver getContentResolver() {
            return this.mBase.getContentResolverForUser(this.mCrossUser);
        }
    }

    public ContentResolver getContentResolver() {
        if (isCrossUserPick()) {
            if (this.mCrossUserContentResolver == null) {
                synchronized (this.mLockObject) {
                    if (this.mCrossUserContentResolver == null) {
                        this.mCrossUserContentResolver = getContentResolverForUser(new UserHandle(validateCrossUser()));
                    }
                }
            }
            Log.d(TAG, "getContentResolver: CrossUserContentResolver");
            return this.mCrossUserContentResolver;
        }
        Log.d(TAG, "getContentResolver: NormalContentResolver");
        return super.getContentResolver();
    }

    public Context getApplicationContext() {
        if (isCrossUserPick()) {
            if (this.mCrossUserContextWrapper == null) {
                synchronized (this.mLockObject) {
                    if (this.mCrossUserContextWrapper == null) {
                        this.mCrossUserContextWrapper = new CrossUserContextWrapper(super.getApplicationContext(), new UserHandle(validateCrossUser()));
                    }
                }
            }
            Log.d(TAG, "getApplicationContext: WrapperedApplication");
            return this.mCrossUserContextWrapper;
        }
        Log.d(TAG, "getApplicationContext: NormalApplication");
        return super.getApplicationContext();
    }

    public void startActivity(Intent intent) {
        if (isCrossUserPick()) {
            intent.putExtra(CrossUserUtils.EXTRA_PICKED_USER_ID, validateCrossUser());
        }
        super.startActivity(intent);
    }

    public void startActivity(Intent intent, Bundle options) {
        if (isCrossUserPick()) {
            intent.putExtra(CrossUserUtils.EXTRA_PICKED_USER_ID, validateCrossUser());
        }
        super.startActivity(intent, options);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        if (isCrossUserPick()) {
            intent.putExtra(CrossUserUtils.EXTRA_PICKED_USER_ID, validateCrossUser());
        }
        super.startActivityForResult(intent, requestCode);
    }

    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (isCrossUserPick()) {
            intent.putExtra(CrossUserUtils.EXTRA_PICKED_USER_ID, validateCrossUser());
        }
        super.startActivityForResult(intent, requestCode, options);
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options) {
        if (isCrossUserPick()) {
            intent.putExtra(CrossUserUtils.EXTRA_PICKED_USER_ID, validateCrossUser());
        }
        super.startActivityFromFragment(fragment, intent, requestCode, options);
    }

    private int validateCrossUser() {
        if (getIntent() == null) {
            return -1;
        }
        int userId = getIntent().getIntExtra(CrossUserUtils.EXTRA_PICKED_USER_ID, -1);
        if (validateCallingPackage()) {
            return userId;
        }
        return -1;
    }

    private boolean validateCallingPackage() {
        if (getPackageName().equals(getCallingPackage()) || CrossUserUtils.checkUidPermission(this, getCallingPackage())) {
            return true;
        }
        return false;
    }

    public boolean isCrossUserPick() {
        return validateCrossUser() != -1;
    }
}
