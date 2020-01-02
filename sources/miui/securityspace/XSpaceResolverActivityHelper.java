package miui.securityspace;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.miui.R;
import android.os.Binder;
import android.os.UserHandle;
import android.provider.MiuiSettings.XSpace;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.app.AlertController.AlertParams;

public class XSpaceResolverActivityHelper {
    private static final String PACKAGE_GOOGLE_VOICEASSIST = "com.google.android.googlequicksearchbox";
    private static final String PACKAGE_VOICEASSIST = "com.miui.voiceassist";
    private static final String TAG = "XSpaceResolverActivity";
    private static final String XSPACE_SERVICE_COMPONENT = "com.miui.securitycore/com.miui.xspace.service.XSpaceService";

    private static class ResolverActivityRunner implements Runnable {
        private Activity mActivity;
        private String mAimPackageName;
        private int mAimUserId = -1;
        private AlertParams mAlertParams;
        private CheckBox mAlwaysOption;
        private int mAskType = 0;
        private String mCallingPackage;
        private Context mContext;
        private int mIconSize;
        private Intent mIntent;
        private String mKeyType;
        private OnClickListener mOnClickListener = new OnClickListener() {
            public void onClick(View v) {
                int i;
                ResolverActivityRunner resolverActivityRunner = ResolverActivityRunner.this;
                if (v.getId() == R.id.app1) {
                    i = 0;
                } else {
                    i = 999;
                }
                resolverActivityRunner.mAimUserId = i;
                if (ResolverActivityRunner.this.mAlwaysOption != null && ResolverActivityRunner.this.mAlwaysOption.isChecked()) {
                    ResolverActivityRunner.this.mAskType = v.getId() == R.id.app1 ? 1 : 2;
                    XSpace.setAskType(ResolverActivityRunner.this.mContext, ResolverActivityRunner.this.mKeyType, ResolverActivityRunner.this.mAskType);
                }
                resolverActivityRunner = ResolverActivityRunner.this;
                resolverActivityRunner.forward(resolverActivityRunner.mAimUserId);
            }
        };
        private Intent mOriginalIntent;
        private View mRootView;

        public ResolverActivityRunner(Activity activity, Intent intent, AlertParams alertParams) {
            this.mActivity = activity;
            this.mContext = this.mActivity.getApplicationContext();
            this.mIntent = intent;
            this.mAlertParams = alertParams;
        }

        public void run() {
            this.mOriginalIntent = (Intent) this.mIntent.getParcelableExtra(CrossUserUtils.EXTRA_XSPACE_RESOLVER_ACTIVITY_ORIGINAL_INTENT);
            this.mAimPackageName = this.mIntent.getStringExtra(CrossUserUtils.EXTRA_XSPACE_RESOLVER_ACTIVITY_AIM_PACKAGE);
            Intent intent = this.mOriginalIntent;
            if (!(intent == null || intent.getComponent() == null || this.mOriginalIntent.getComponent().getClassName() == null)) {
                StringBuilder stringBuilder;
                this.mCallingPackage = this.mOriginalIntent.getStringExtra(CrossUserUtils.EXTRA_XSPACE_RESOLVER_ACTIVITY_CALLING_PACKAGE);
                if (this.mCallingPackage.equals(XSpaceResolverActivityHelper.PACKAGE_VOICEASSIST) || this.mCallingPackage.equals(XSpaceResolverActivityHelper.PACKAGE_GOOGLE_VOICEASSIST)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(this.mCallingPackage);
                    stringBuilder.append("-");
                    stringBuilder.append(this.mAimPackageName);
                    this.mKeyType = stringBuilder.toString();
                } else {
                    this.mKeyType = this.mOriginalIntent.getComponent().getClassName();
                }
                this.mAskType = XSpace.getAskType(this.mContext, this.mKeyType);
                if (this.mAskType != 0) {
                    int i;
                    XSpaceIntentCompat.prepareToLeaveUser(this.mOriginalIntent, Binder.getCallingUserHandle().getIdentifier());
                    if (this.mAskType == 1) {
                        i = 0;
                    } else {
                        i = 999;
                    }
                    this.mAimUserId = i;
                    if (needShowDefaultSettingGuide()) {
                        startXSpaceServiceAsUser(this.mContext, this.mAskType, 0);
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Direct to ");
                    stringBuilder.append(this.mAimUserId);
                    Log.i(XSpaceResolverActivityHelper.TAG, stringBuilder.toString());
                    forward(this.mAimUserId);
                }
            }
            AlertParams alertParams = this.mAlertParams;
            alertParams.mMessage = null;
            alertParams.mTitle = this.mActivity.getString(R.string.xspace_resolver_activity_title);
            this.mRootView = this.mActivity.getLayoutInflater().inflate((int) R.layout.resolver_screen_xspace, null);
            alertParams = this.mAlertParams;
            alertParams.mView = this.mRootView;
            alertParams.mNegativeButtonText = this.mActivity.getResources().getString(17039360);
            this.mAlertParams.mNegativeButtonListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ResolverActivityRunner.this.mActivity.finish();
                }
            };
            this.mIconSize = ((ActivityManager) this.mContext.getSystemService(Context.ACTIVITY_SERVICE)).getLauncherLargeIconSize();
            if (!(this.mOriginalIntent == null || this.mAimPackageName == null)) {
                Drawable iconDrawable = getAppIcon();
                CharSequence label = getAppLabel();
                loadItem(R.id.app1, iconDrawable, label);
                loadItem(R.id.app2, iconDrawable, label);
            }
            this.mAlwaysOption = (CheckBox) this.mRootView.findViewById(R.id.always_option);
            if (this.mAlwaysOption != null && XSpace.sSupportDefaultSettingApps.contains(this.mKeyType)) {
                this.mAlwaysOption.setVisibility(0);
                this.mAlwaysOption.setChecked(false);
            }
        }

        private void forward(int userId) {
            this.mOriginalIntent.putExtra(CrossUserUtils.EXTRA_PICKED_USER_ID, userId);
            CrossUserUtilsCompat.startActivityAsCaller(this.mActivity, this.mOriginalIntent, null, false, userId);
            this.mActivity.finish();
        }

        private void loadItem(int appId, Drawable iconDrawable, CharSequence label) {
            LinearLayout app = (LinearLayout) this.mRootView.findViewById(appId);
            ImageView icon = (ImageView) app.findViewById(16908294);
            LayoutParams lp = icon.getLayoutParams();
            int i = this.mIconSize;
            lp.height = i;
            lp.width = i;
            TextView text = (TextView) app.findViewById(16908308);
            text.setMinLines(1);
            String iconContentDescription = new StringBuilder();
            iconContentDescription.append(label);
            iconContentDescription.append("");
            iconContentDescription = iconContentDescription.toString();
            if (appId == R.id.app2) {
                iconDrawable = XSpaceUserHandle.getXSpaceIcon(this.mContext, iconDrawable);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mActivity.getString(R.string.xspace_head));
                stringBuilder.append(label);
                iconContentDescription = stringBuilder.toString();
            }
            app.findViewById(16908309).setVisibility(8);
            app.setOnClickListener(this.mOnClickListener);
            icon.setImageDrawable(iconDrawable);
            icon.setContentDescription(iconContentDescription);
            text.setText(label);
            text.setImportantForAccessibility(2);
        }

        private Drawable getAppIcon() {
            return CrossUserUtils.getOriginalAppIcon(this.mContext, this.mAimPackageName);
        }

        public CharSequence getAppLabel() {
            try {
                PackageManager pm = this.mContext.getPackageManager();
                PackageInfo pkgInfo = pm.getPackageInfo(this.mAimPackageName, 0);
                if (pkgInfo != null) {
                    ApplicationInfo appInfo = pkgInfo.applicationInfo;
                    if (appInfo != null) {
                        CharSequence label = appInfo.loadLabel(pm);
                        if (label != null) {
                            return label;
                        }
                    }
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            return this.mAimPackageName;
        }

        private static void startXSpaceServiceAsUser(Context context, int askType, int mAimUserId) {
            Intent intent = new Intent();
            intent.putExtra(XSpace.PARAM_INTENT_KEY_HAS_EXTRA, XSpace.PARAM_INTENT_VALUE_HAS_EXTRA);
            intent.putExtra(XSpace.PARAM_INTENT_KEY_DEFAULT_ASKTYPE, askType);
            intent.setComponent(ComponentName.unflattenFromString(XSpaceResolverActivityHelper.XSPACE_SERVICE_COMPONENT));
            context.startServiceAsUser(intent, new UserHandle(mAimUserId));
        }

        public boolean needShowDefaultSettingGuide() {
            return XSpace.getGuideNotificationTimes(this.mContext, XSpace.KEY_DEFAULT_GUIDE_TIMES) < 2;
        }
    }

    public static boolean checkAndResolve(Activity activity, Intent intent, AlertParams alertParams) {
        if (intent != null) {
            if (CrossUserUtils.ACTION_XSPACE_RESOLVER_ACTIVITY.equals(intent.getAction())) {
                new ResolverActivityRunner(activity, intent, alertParams).run();
                return true;
            }
        }
        return false;
    }
}
