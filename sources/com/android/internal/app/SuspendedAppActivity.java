package com.android.internal.app;

import android.Manifest.permission;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.SuspendDialogInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.UserHandle;
import android.util.Slog;
import com.android.internal.R;
import com.android.internal.app.AlertController.AlertParams;

public class SuspendedAppActivity extends AlertActivity implements OnClickListener {
    public static final String EXTRA_DIALOG_INFO = "com.android.internal.app.extra.DIALOG_INFO";
    public static final String EXTRA_SUSPENDED_PACKAGE = "com.android.internal.app.extra.SUSPENDED_PACKAGE";
    public static final String EXTRA_SUSPENDING_PACKAGE = "com.android.internal.app.extra.SUSPENDING_PACKAGE";
    private static final String PACKAGE_NAME = "com.android.internal.app";
    private static final String TAG = SuspendedAppActivity.class.getSimpleName();
    private Intent mMoreDetailsIntent;
    private PackageManager mPm;
    private SuspendDialogInfo mSuppliedDialogInfo;
    private Resources mSuspendingAppResources;
    private int mUserId;

    private CharSequence getAppLabel(String packageName) {
        try {
            return this.mPm.getApplicationInfoAsUser(packageName, 0, this.mUserId).loadLabel(this.mPm);
        } catch (NameNotFoundException ne) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Package ");
            stringBuilder.append(packageName);
            stringBuilder.append(" not found");
            Slog.e(str, stringBuilder.toString(), ne);
            return packageName;
        }
    }

    private Intent getMoreDetailsActivity(String suspendingPackage, String suspendedPackage, int userId) {
        Intent moreDetailsIntent = new Intent(Intent.ACTION_SHOW_SUSPENDED_APP_DETAILS).setPackage(suspendingPackage);
        String requiredPermission = permission.SEND_SHOW_SUSPENDED_APP_DETAILS;
        ResolveInfo resolvedInfo = this.mPm.resolveActivityAsUser(moreDetailsIntent, 0, userId);
        if (!(resolvedInfo == null || resolvedInfo.activityInfo == null)) {
            if (permission.SEND_SHOW_SUSPENDED_APP_DETAILS.equals(resolvedInfo.activityInfo.permission)) {
                moreDetailsIntent.putExtra("android.intent.extra.PACKAGE_NAME", suspendedPackage).setFlags(335544320);
                return moreDetailsIntent;
            }
        }
        return null;
    }

    private Drawable resolveIcon() {
        int iconId;
        SuspendDialogInfo suspendDialogInfo = this.mSuppliedDialogInfo;
        if (suspendDialogInfo != null) {
            iconId = suspendDialogInfo.getIconResId();
        } else {
            iconId = 0;
        }
        if (iconId != 0) {
            Resources resources = this.mSuspendingAppResources;
            if (resources != null) {
                try {
                    return resources.getDrawable(iconId, getTheme());
                } catch (NotFoundException e) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Could not resolve drawable resource id ");
                    stringBuilder.append(iconId);
                    Slog.e(str, stringBuilder.toString());
                }
            }
        }
        return null;
    }

    private String resolveTitle() {
        int titleId;
        SuspendDialogInfo suspendDialogInfo = this.mSuppliedDialogInfo;
        if (suspendDialogInfo != null) {
            titleId = suspendDialogInfo.getTitleResId();
        } else {
            titleId = 0;
        }
        if (titleId != 0) {
            Resources resources = this.mSuspendingAppResources;
            if (resources != null) {
                try {
                    return resources.getString(titleId);
                } catch (NotFoundException e) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Could not resolve string resource id ");
                    stringBuilder.append(titleId);
                    Slog.e(str, stringBuilder.toString());
                }
            }
        }
        return getString(R.string.app_suspended_title);
    }

    private String resolveDialogMessage(String suspendingPkg, String suspendedPkg) {
        CharSequence suspendedAppLabel = getAppLabel(suspendedPkg);
        int messageId = this.mSuppliedDialogInfo;
        int i = 1;
        if (messageId != 0) {
            messageId = messageId.getDialogMessageResId();
            String message = this.mSuppliedDialogInfo.getDialogMessage();
            if (messageId != 0) {
                Resources resources = this.mSuspendingAppResources;
                if (resources != null) {
                    try {
                        i = resources.getString(messageId, suspendedAppLabel);
                        return i;
                    } catch (NotFoundException e) {
                        String str = TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Could not resolve string resource id ");
                        stringBuilder.append(messageId);
                        Slog.e(str, stringBuilder.toString());
                    }
                }
            }
            if (message != null) {
                return String.format(getResources().getConfiguration().getLocales().get(0), message, new Object[]{suspendedAppLabel});
            }
        }
        return getString(R.string.app_suspended_default_message, suspendedAppLabel, getAppLabel(suspendingPkg));
    }

    private String resolveNeutralButtonText() {
        SuspendDialogInfo suspendDialogInfo = this.mSuppliedDialogInfo;
        int buttonTextId = suspendDialogInfo != null ? suspendDialogInfo.getNeutralButtonTextResId() : 0;
        if (buttonTextId != 0) {
            Resources resources = this.mSuspendingAppResources;
            if (resources != null) {
                try {
                    return resources.getString(buttonTextId);
                } catch (NotFoundException e) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Could not resolve string resource id ");
                    stringBuilder.append(buttonTextId);
                    Slog.e(str, stringBuilder.toString());
                }
            }
        }
        return getString(R.string.app_suspended_more_details);
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.mPm = getPackageManager();
        getWindow().setType(2008);
        Intent intent = getIntent();
        this.mUserId = intent.getIntExtra(Intent.EXTRA_USER_ID, -1);
        String str;
        if (this.mUserId < 0) {
            str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid user: ");
            stringBuilder.append(this.mUserId);
            Slog.wtf(str, stringBuilder.toString());
            finish();
            return;
        }
        str = intent.getStringExtra(EXTRA_SUSPENDED_PACKAGE);
        String suspendingPackage = intent.getStringExtra(EXTRA_SUSPENDING_PACKAGE);
        this.mSuppliedDialogInfo = (SuspendDialogInfo) intent.getParcelableExtra(EXTRA_DIALOG_INFO);
        if (this.mSuppliedDialogInfo != null) {
            try {
                this.mSuspendingAppResources = this.mPm.getResourcesForApplicationAsUser(suspendingPackage, this.mUserId);
            } catch (NameNotFoundException ne) {
                String str2 = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Could not find resources for ");
                stringBuilder2.append(suspendingPackage);
                Slog.e(str2, stringBuilder2.toString(), ne);
            }
        }
        AlertParams ap = this.mAlertParams;
        ap.mIcon = resolveIcon();
        ap.mTitle = resolveTitle();
        ap.mMessage = resolveDialogMessage(suspendingPackage, str);
        ap.mPositiveButtonText = getString(17039370);
        this.mMoreDetailsIntent = getMoreDetailsActivity(suspendingPackage, str, this.mUserId);
        if (this.mMoreDetailsIntent != null) {
            ap.mNeutralButtonText = resolveNeutralButtonText();
        }
        ap.mNeutralButtonListener = this;
        ap.mPositiveButtonListener = this;
        setupAlert();
    }

    public void onClick(DialogInterface dialog, int which) {
        if (which == -3) {
            startActivityAsUser(this.mMoreDetailsIntent, UserHandle.of(this.mUserId));
            Slog.i(TAG, "Started more details activity");
        }
        finish();
    }

    public static Intent createSuspendedAppInterceptIntent(String suspendedPackage, String suspendingPackage, SuspendDialogInfo dialogInfo, int userId) {
        if (dialogInfo != null) {
            if ("!miui_Suspended!".equals(dialogInfo.getDialogMessage())) {
                Intent intent = new Intent();
                intent.putExtra("pkgName", suspendedPackage);
                intent.setFlags(276824064);
                intent.setAction("miui.intent.action.USAGE_STATS_TIMEOVER");
                return intent;
            }
        }
        return new Intent().setClassName("android", SuspendedAppActivity.class.getName()).putExtra(EXTRA_SUSPENDED_PACKAGE, suspendedPackage).putExtra(EXTRA_DIALOG_INFO, (Parcelable) dialogInfo).putExtra(EXTRA_SUSPENDING_PACKAGE, suspendingPackage).putExtra(Intent.EXTRA_USER_ID, userId).setFlags(276824064);
    }
}
