package android.hardware;

import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemProperties;
import android.text.TextUtils.SimpleStringSplitter;
import android.text.TextUtils.StringSplitter;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.WindowManager;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;
import miui.process.ProcessManager;
import miui.util.FeatureParser;

public class CameraInjector {
    private static final String ACTION_CAMERA_INIT_TO_NFC = "com.android.nfc.action.DISABLE_POLLING";
    private static final String ACTION_CAMERA_RELEASE_TO_NFC = "com.android.nfc.action.ENABLE_POLLING";
    private static final String ACTION_SAVE_CAMERA_INFO = "action_save_camera_info";
    private static final String CAMERA_ACTIVE_NFC_DISABLE_DEVICES_LIST = "cepheus,crux";
    private static final String CAMERA_CONFIG_KEY = "camera";
    private static final String ENDTTIME = "endtime";
    private static final String EXTRA_BEAUTIFY_VALUE = "extra_still_beautify_value";
    private static final String EXTRA_MIN_PREVIEW_SIZE = "extra_min_preview_size";
    private static final String FRONTORBACK = "frontorback";
    private static final String KEY_BEAUTIFY = "xiaomi-still-beautify-values";
    private static final String KEY_CAMERA_ID = "camera-id";
    private static final String KEY_PICTURE_SIZE = "picture-size";
    private static final String KEY_PREVIEW_SIZE = "preview-size";
    private static final String SAVE_CAMERA_INFO_PACKAGE_NAME = "com.miui.klo.bugreport";
    private static final String SAVE_CAMERA_INFO_SERVICE_NAME = "com.miui.klo.bugreport.service.ReceiveCameraInfoService";
    private static final String STARTTIME = "starttime";
    private static final String SUPPORTED_VALUES_SUFFIX = "-values";
    private static final String TAG = "CameraInjector";
    private static WeakHashMap<Camera, CameraExInfo> sCameraInfoMap = new WeakHashMap();

    private static class CameraExInfo {
        private CameraOrientationEventListener mCameraOrientationEventListener;
        private HashMap<String, String> mCameraParameterInfo;
        private long mEndTime;
        private boolean mReverseFrameData;
        private long mStartTime;

        private CameraExInfo(int cameraId) {
            this.mCameraParameterInfo = new HashMap();
            this.mCameraParameterInfo.put(CameraInjector.KEY_CAMERA_ID, String.valueOf(cameraId));
            this.mStartTime = System.currentTimeMillis();
        }
    }

    private static class CameraOrientationEventListener extends OrientationEventListener {
        private Camera mCamera;
        private Context mContext;
        private Display mDisplay;
        private int mOrientation = -1;

        public CameraOrientationEventListener(Camera camera, Context context) {
            super(context);
            this.mCamera = camera;
            this.mContext = context;
            this.mDisplay = ((WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        }

        public void onOrientationChanged(int orientation) {
            if (orientation != -1) {
                this.mOrientation = CameraInjector.roundOrientation(orientation, this.mOrientation);
                if (this.mOrientation % 180 == 0) {
                    int degrees = this.mDisplay.getRotation();
                    boolean newReverse = degrees == 2;
                    if (((CameraExInfo) CameraInjector.sCameraInfoMap.get(this.mCamera)).mReverseFrameData != newReverse) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("mReverseFrameData change to ");
                        stringBuilder.append(newReverse);
                        stringBuilder.append(" current client orientation ");
                        stringBuilder.append(degrees);
                        Log.v(CameraInjector.TAG, stringBuilder.toString());
                        ((CameraExInfo) CameraInjector.sCameraInfoMap.get(this.mCamera)).mReverseFrameData = newReverse;
                    }
                }
            }
        }
    }

    static class UploadInfoThread extends Thread {
        CameraExInfo mCameraExInfo;

        private UploadInfoThread(CameraExInfo info) {
            this.mCameraExInfo = info;
        }

        public void run() {
            if (this.mCameraExInfo != null) {
                Intent intent = new Intent(CameraInjector.ACTION_SAVE_CAMERA_INFO);
                intent.setComponent(new ComponentName(CameraInjector.SAVE_CAMERA_INFO_PACKAGE_NAME, CameraInjector.SAVE_CAMERA_INFO_SERVICE_NAME));
                Bundle bundle = new Bundle();
                bundle.putLong(CameraInjector.STARTTIME, this.mCameraExInfo.mStartTime);
                if (this.mCameraExInfo.mEndTime != 0) {
                    bundle.putLong(CameraInjector.ENDTTIME, this.mCameraExInfo.mEndTime);
                }
                bundle.putString(CameraInjector.FRONTORBACK, (String) this.mCameraExInfo.mCameraParameterInfo.get(CameraInjector.KEY_CAMERA_ID));
                intent.putExtras(bundle);
                ActivityThread.currentApplication().getBaseContext().startService(intent);
            }
        }
    }

    private static boolean isInWhiteList(String key) {
        String processName = ActivityThread.currentPackageName();
        if (processName != null && processName.length() > 0) {
            String[] whitelist = FeatureParser.getStringArray(key);
            if (whitelist != null && whitelist.length > 0) {
                for (String str : whitelist) {
                    if (processName.equals(str)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void initCamera(Camera camera, int cameraId) {
        sCameraInfoMap.put(camera, new CameraExInfo(cameraId));
        onCameraStateChange(camera, true);
        uploadCameraUseInfo(camera, false);
    }

    public static void releaseCamera(Camera camera) {
        if (sCameraInfoMap.containsKey(camera)) {
            onCameraStateChange(camera, false);
            uploadCameraUseInfo(camera, true);
            sCameraInfoMap.remove(camera);
        }
    }

    private static void uploadCameraUseInfo(Camera camera, boolean endFlag) {
        CameraExInfo cameraExInfo = (CameraExInfo) sCameraInfoMap.get(camera);
        if (cameraExInfo != null) {
            if (endFlag) {
                cameraExInfo.mEndTime = System.currentTimeMillis();
            }
            new UploadInfoThread(cameraExInfo).start();
        }
    }

    public static void getParametersEx(Parameters params) {
        if (isInWhiteList("remove_lower_perview_size_list")) {
            List<Size> previewSizes = params.getSupportedPreviewSizes();
            List<Size> newSizeList = new ArrayList(previewSizes.size());
            int minSize = FeatureParser.getInteger(EXTRA_MIN_PREVIEW_SIZE, 921600);
            for (Size size : previewSizes) {
                if (minSize <= size.width * size.height) {
                    newSizeList.add(size);
                }
            }
            if (newSizeList.size() != previewSizes.size()) {
                params.set("preview-size-values", getValueString(newSizeList));
            }
        }
    }

    protected static String convertSizeToString(Size size) {
        if (size == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(size.width);
        stringBuilder.append("x");
        stringBuilder.append(size.height);
        return stringBuilder.toString();
    }

    public static void setParametersEx(Camera camera, Parameters params) {
        if (isInWhiteList("add_still_beautify_list")) {
            String str = KEY_BEAUTIFY;
            if (params.get(str) == null) {
                Size size = params.getPreviewSize();
                if (size != null && (size.height <= MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH || size.width <= MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH)) {
                    params.set(str, FeatureParser.getString(EXTRA_BEAUTIFY_VALUE));
                }
            }
        }
        HashMap<String, String> info = getCameraParameterInfo(camera);
        if (info != null) {
            info.put(KEY_PREVIEW_SIZE, convertSizeToString(params.getPreviewSize()));
            info.put(KEY_PICTURE_SIZE, convertSizeToString(params.getPictureSize()));
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setParametersEx: Lost camera info ");
        stringBuilder.append(camera);
        Log.w(TAG, stringBuilder.toString());
    }

    public static void startPreview(Camera camera) {
        HashMap<String, String> info = getCameraParameterInfo(camera);
        if (info != null) {
            String cameraId = (String) info.get(KEY_CAMERA_ID);
            String str = (String) info.get(KEY_PREVIEW_SIZE);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("startPreview: Lost camera info ");
        stringBuilder.append(camera);
        Log.w(TAG, stringBuilder.toString());
    }

    public static void takePicture(Camera camera) {
        HashMap<String, String> info = getCameraParameterInfo(camera);
        if (info != null) {
            String cameraId = (String) info.get(KEY_CAMERA_ID);
            String str = (String) info.get(KEY_PICTURE_SIZE);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("takePicture: Lost camera info ");
        stringBuilder.append(camera);
        Log.w(TAG, stringBuilder.toString());
    }

    public static void processPreviewFrame(Camera camera, byte[] data) {
        CameraExInfo cameraExInfo = (CameraExInfo) sCameraInfoMap.get(camera);
        if (cameraExInfo != null && cameraExInfo.mReverseFrameData) {
            reversePreviewFrame(data);
        }
    }

    private static String getValueString(List<Size> sizes) {
        if (sizes == null) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            Size size = (Size) sizes.get(i);
            buffer.append(size.width);
            buffer.append(StateProperty.TARGET_X);
            buffer.append(size.height);
            if (i != sizes.size() - 1) {
                buffer.append(',');
            }
        }
        return buffer.toString();
    }

    public static int getNumberOfCameras(int numberOfCameras) {
        if (!isExposeAuxCamera() && numberOfCameras > 2) {
            numberOfCameras = 2;
        }
        if (!limitCamera() || numberOfCameras <= 1) {
            return numberOfCameras;
        }
        return 1;
    }

    public static void setTorchMode(String cameraId) {
        if (limitByCameraId(cameraId)) {
            throw new IllegalArgumentException("invalid cameraId");
        }
    }

    public static boolean limitByCameraId(String cameraId) {
        boolean limit = false;
        if (cameraId == null) {
            limit = true;
        }
        if (!isExposeAuxCamera() && Integer.parseInt(cameraId) >= 2) {
            limit = true;
        }
        if (!limitCamera() || Integer.parseInt(cameraId) < 1) {
            return limit;
        }
        return true;
    }

    private static boolean limitByPackageName(String packageList) {
        String packageName = ActivityThread.currentPackageName();
        if (packageList == null || packageList.length() <= 0 || packageName == null || packageName.length() <= 0) {
            return false;
        }
        StringSplitter<String> splitter = new SimpleStringSplitter(',');
        splitter.setString(packageList);
        for (String str : splitter) {
            if (packageName.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExposeAuxCamera() {
        if (isInWhiteList("camera_aux_package_list") || ActivityThread.isSystem()) {
            return true;
        }
        return limitByPackageName(SystemProperties.get("camera.aux.packagelist"));
    }

    public static boolean limitCamera() {
        return limitByPackageName(SystemProperties.get("camera.limit.packagelist"));
    }

    private static int roundOrientation(int orientation, int orientationHistory) {
        boolean changeOrientation;
        if (orientationHistory == -1) {
            changeOrientation = true;
        } else {
            int dist = Math.abs(orientation - orientationHistory);
            changeOrientation = Math.min(dist, 360 - dist) >= 50;
        }
        if (changeOrientation) {
            return (((orientation + 45) / 90) * 90) % 360;
        }
        return orientationHistory;
    }

    private static void reversePreviewFrame(byte[] array) {
        if (array != null) {
            int totalYNum = (array.length * 2) / 3;
            reverse(array, 0, totalYNum);
            reverseUV(array, totalYNum, array.length - 1);
        }
    }

    private static void reverse(byte[] array, int start, int end) {
        if (array != null && start <= array.length - 1 && end <= array.length - 1 && start >= 0 && end >= 0) {
            while (end > start) {
                byte tmp = array[end];
                array[end] = array[start];
                array[start] = tmp;
                end--;
                start++;
            }
        }
    }

    private static void reverseUV(byte[] array, int start, int end) {
        if (array != null && start <= array.length - 1 && end <= array.length - 1 && start >= 0 && end >= 0) {
            while (end > start) {
                byte tmpU = array[end - 1];
                byte tmpV = array[end];
                array[end - 1] = array[start];
                array[end] = array[start + 1];
                array[start] = tmpU;
                array[start + 1] = tmpV;
                end -= 2;
                start += 2;
            }
        }
    }

    private static boolean isInRotateWhiteList() {
        if (isInWhiteList("camera_rotate_packagelist")) {
            return true;
        }
        String processName = ActivityThread.currentPackageName();
        String packageList = SystemProperties.get("camera.rotate.packagelist");
        if (packageList == null || packageList.length() == 0) {
            packageList = SystemProperties.get("vendor.camera.rotate.packagelist");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("isInRotateWhiteList whiteList=");
        stringBuilder.append(packageList);
        stringBuilder.append(" processName=");
        stringBuilder.append(processName);
        Log.v(TAG, stringBuilder.toString());
        if (packageList.length() > 0) {
            StringSplitter<String> splitter = new SimpleStringSplitter(',');
            splitter.setString(packageList);
            for (String str : splitter) {
                if (processName.equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void onCameraStateChange(Camera camera, boolean active) {
        if (active) {
            ProcessManager.boostCameraIfNeed();
        }
        notifyCameraStateChange(active);
        if (!"chiron".equals(Build.DEVICE)) {
            if (!"lithium".equals(Build.DEVICE)) {
                if (!"polaris".equals(Build.DEVICE)) {
                    return;
                }
            }
        }
        if (active) {
            createOrientationListener(camera);
        } else {
            destoryOrientationListener(camera);
        }
    }

    public static void notifyCameraStateChange(boolean active) {
        if (!CAMERA_ACTIVE_NFC_DISABLE_DEVICES_LIST.contains(Build.DEVICE)) {
            return;
        }
        if (active) {
            ActivityThread.currentApplication().getBaseContext().sendBroadcast(new Intent(ACTION_CAMERA_INIT_TO_NFC));
        } else {
            ActivityThread.currentApplication().getBaseContext().sendBroadcast(new Intent(ACTION_CAMERA_RELEASE_TO_NFC));
        }
    }

    private static void createOrientationListener(Camera camera) {
        CameraExInfo cameraExInfo = (CameraExInfo) sCameraInfoMap.get(camera);
        if (isInRotateWhiteList() && cameraExInfo != null) {
            Log.v(TAG, "Listener orientation");
            CameraOrientationEventListener cameraOrientationEventListener = new CameraOrientationEventListener(camera, ActivityThread.currentApplication().getBaseContext());
            cameraOrientationEventListener.enable();
            cameraExInfo.mCameraOrientationEventListener = cameraOrientationEventListener;
        }
    }

    private static void destoryOrientationListener(Camera camera) {
        CameraExInfo cameraExInfo = (CameraExInfo) sCameraInfoMap.get(camera);
        if (cameraExInfo != null) {
            CameraOrientationEventListener listener = cameraExInfo.mCameraOrientationEventListener;
            if (listener != null) {
                Log.v(TAG, "release orientation listener");
                listener.disable();
                cameraExInfo.mCameraOrientationEventListener = null;
            }
        }
    }

    private static HashMap<String, String> getCameraParameterInfo(Camera camera) {
        CameraExInfo cameraExInfo = (CameraExInfo) sCameraInfoMap.get(camera);
        if (cameraExInfo != null) {
            return cameraExInfo.mCameraParameterInfo;
        }
        return null;
    }
}
