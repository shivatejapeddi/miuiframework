package miui.slide;

import android.Manifest.permission;
import android.content.Context;
import android.os.Binder;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import miui.slide.ISlideManagerService.Stub;

public class SlideManagerService extends Stub {
    public static final String SERVICE_NAME = "miui.slide.SlideManagerService";
    private static final String TAG = "SlideManager";
    private Context mContext;

    public SlideManagerService(Context context) {
        this.mContext = context;
        SlideCloudConfigHelper.getInstance().initConfig(this.mContext);
    }

    public int getCameraStatus() {
        long token = Binder.clearCallingIdentity();
        int cameraStatus = 0;
        if (SlideCameraMonitor.getInstance().isFrontCameraOpening()) {
            cameraStatus = 0 | 1;
        }
        if (SlideCameraMonitor.getInstance().isBackCameraOpening()) {
            cameraStatus |= 2;
        }
        if (SlideCameraMonitor.getInstance().isCameraRecording()) {
            cameraStatus |= 4;
        }
        Binder.restoreCallingIdentity(token);
        return cameraStatus;
    }

    public AppSlideConfig getAppSlideConfig(String packageName, int versionCode) {
        AppSlideConfig slideConfig = SlideCloudConfigHelper.getInstance().getAppSlideConfigs(packageName);
        return slideConfig != null ? slideConfig : new AppSlideConfig();
    }

    public void registerSlideChangeListener(String identity, ISlideChangeListener listener) {
        if (identity != null && identity.length() != 0 && listener != null) {
            SlideCoverEventManager.getInstance().registerSlideChangeListener(identity, listener);
        }
    }

    public void unregisterSlideChangeListener(ISlideChangeListener listener) {
        if (listener != null) {
            SlideCoverEventManager.getInstance().unregisterSlideChangeListener(listener);
        }
    }

    /* Access modifiers changed, original: protected */
    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        if (this.mContext.checkCallingOrSelfPermission(permission.DUMP) != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Permission Denial: can't dump miui.slide.SlideManagerService from pid=");
            stringBuilder.append(Binder.getCallingPid());
            stringBuilder.append(", uid=");
            stringBuilder.append(Binder.getCallingUid());
            stringBuilder.append(" due to missing android.permission.DUMP permission");
            pw.println(stringBuilder.toString());
            return;
        }
        SlideCoverEventManager.getInstance().dump("    ", pw, args);
    }
}
