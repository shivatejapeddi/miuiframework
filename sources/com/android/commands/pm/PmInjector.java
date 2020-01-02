package com.android.commands.pm;

import android.app.ActivityManager;
import android.app.AppGlobals;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.UserInfo;
import android.miui.AppOpsUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IMessenger.Stub;
import android.os.IUserManager;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.MiuiSettings.AntiSpam;
import android.util.Log;
import java.io.File;
import miui.securityspace.XSpaceUserHandle;

public final class PmInjector {
    private static final String PM = "Pm";
    public static final int STATUS_INVALID_APK = 3;
    public static final int STATUS_REJECT = -1;
    public static final int STATUS_SUCESS = 2;

    public static class InstallObserver extends Stub {
        boolean finished;
        String msg;
        int result;

        public void send(Message message) throws RemoteException {
            synchronized (this) {
                this.finished = true;
                this.result = message.what;
                Bundle data = message.getData();
                if (data != null) {
                    this.msg = data.getString(Notification.CATEGORY_MESSAGE);
                }
                notifyAll();
            }
        }
    }

    public static String statusToString(int status) {
        String msg = "";
        if (status == -1) {
            return "Install canceled by user";
        }
        if (status == 2) {
            return "Sucess";
        }
        if (status != 3) {
            return msg;
        }
        return "Invalid apk";
    }

    public static int installVerify(String apkFilePath) {
        Uri apkURI = Uri.fromFile(new File(apkFilePath));
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setClassName(AntiSpam.ANTISPAM_PKG, "com.miui.permcenter.install.AdbInstallActivity");
        intent.setDataAndType(apkURI, "application/vnd.android.package-archive");
        IBinder activityObs = new InstallObserver();
        intent.putExtra("observer", activityObs);
        intent.addFlags(402653184);
        try {
            int res = IActivityManagerCompat.startActivity(intent);
            String str;
            StringBuilder stringBuilder;
            if (res != 0) {
                str = PM;
                stringBuilder = new StringBuilder();
                stringBuilder.append("start PackageInstallerActivity failed [");
                stringBuilder.append(res);
                stringBuilder.append("]");
                Log.e(str, stringBuilder.toString());
                return !isSecurityCenterExist() ? 2 : -1;
            } else {
                synchronized (activityObs) {
                    while (!activityObs.finished) {
                        try {
                            activityObs.wait(60000);
                            activityObs.finished = true;
                        } catch (InterruptedException e) {
                        }
                    }
                    if (activityObs.result == -1) {
                        return 2;
                    }
                    String msg = activityObs.msg;
                    if (msg == null) {
                        msg = "Failure [INSTALL_CANCELED_BY_USER]";
                    }
                    str = PM;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("install msg : ");
                    stringBuilder.append(msg);
                    Log.e(str, stringBuilder.toString());
                    if (msg.contains("Invalid apk")) {
                        if (isSecurityCenterExist()) {
                            return 3;
                        }
                        return 2;
                    } else if (isSecurityCenterExist()) {
                        return -1;
                    } else {
                        return 2;
                    }
                }
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
            Log.e(PM, "start PackageInstallerActivity RemoteException");
            return !isSecurityCenterExist() ? 2 : -1;
        }
    }

    public static int getDefaultUserId() {
        try {
            for (UserInfo userInfo : IUserManager.Stub.asInterface(ServiceManager.getService("user")).getUsers(false)) {
                if (XSpaceUserHandle.isXSpaceUser(userInfo)) {
                    return ActivityManager.getCurrentUser();
                }
            }
        } catch (RemoteException e) {
        }
        return -1;
    }

    public static boolean isSecurityCenterExist() {
        String str = PM;
        if (!AppOpsUtils.isXOptMode()) {
            return true;
        }
        try {
            PackageInfo pi = AppGlobals.getPackageManager().getPackageInfo(AntiSpam.ANTISPAM_PKG, 1, UserHandle.myUserId());
            Log.d(str, "checkSecurityCenterInstalled:getPackageInfo:true");
            if (pi != null) {
                return true;
            }
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getPackageInfo error:");
            stringBuilder.append(e.toString());
            Log.d(str, stringBuilder.toString());
        }
        Log.d(str, "checkSecurityCenterInstalled:Exception:false");
        return false;
    }
}
