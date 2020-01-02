package com.android.internal.app;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import com.android.internal.R;

public class UnlaunchableAppActivity extends Activity implements OnDismissListener, OnClickListener {
    private static final String EXTRA_UNLAUNCHABLE_REASON = "unlaunchable_reason";
    private static final String TAG = "UnlaunchableAppActivity";
    private static final int UNLAUNCHABLE_REASON_QUIET_MODE = 1;
    private int mReason;
    private IntentSender mTarget;
    private int mUserId;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        Intent intent = getIntent();
        this.mReason = intent.getIntExtra(EXTRA_UNLAUNCHABLE_REASON, -1);
        this.mUserId = intent.getIntExtra(Intent.EXTRA_USER_HANDLE, -10000);
        this.mTarget = (IntentSender) intent.getParcelableExtra(Intent.EXTRA_INTENT);
        int i = this.mUserId;
        String str = TAG;
        StringBuilder stringBuilder;
        if (i == -10000) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid user id: ");
            stringBuilder.append(this.mUserId);
            stringBuilder.append(". Stopping.");
            Log.wtf(str, stringBuilder.toString());
            finish();
        } else if (this.mReason == 1) {
            CharSequence dialogTitle = getResources().getString(R.string.work_mode_off_title);
            Builder builder = new Builder(this).setTitle(dialogTitle).setMessage(getResources().getString(R.string.work_mode_off_message)).setOnDismissListener(this);
            if (this.mReason == 1) {
                builder.setPositiveButton((int) R.string.work_mode_turn_on, (OnClickListener) this).setNegativeButton(17039360, null);
            } else {
                builder.setPositiveButton(17039370, null);
            }
            builder.show();
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid unlaunchable type: ");
            stringBuilder.append(this.mReason);
            Log.wtf(str, stringBuilder.toString());
            finish();
        }
    }

    public void onDismiss(DialogInterface dialog) {
        finish();
    }

    public void onClick(DialogInterface dialog, int which) {
        if (this.mReason == 1 && which == -1) {
            UserManager.get(this).requestQuietModeEnabled(false, UserHandle.of(this.mUserId), this.mTarget);
        }
    }

    private static final Intent createBaseIntent() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("android", UnlaunchableAppActivity.class.getName()));
        intent.setFlags(276824064);
        return intent;
    }

    public static Intent createInQuietModeDialogIntent(int userId) {
        Intent intent = createBaseIntent();
        intent.putExtra(EXTRA_UNLAUNCHABLE_REASON, 1);
        intent.putExtra(Intent.EXTRA_USER_HANDLE, userId);
        return intent;
    }

    public static Intent createInQuietModeDialogIntent(int userId, IntentSender target) {
        Intent intent = createInQuietModeDialogIntent(userId);
        intent.putExtra(Intent.EXTRA_INTENT, (Parcelable) target);
        return intent;
    }
}
