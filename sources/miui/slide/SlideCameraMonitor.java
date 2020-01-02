package miui.slide;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraManager.AvailabilityCallback;
import android.media.AudioManager;
import android.media.AudioRecordingConfiguration;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Slog;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SlideCameraMonitor {
    private static String TAG = "SlideCameraMonitor";
    private static final int VIRTUAL_CAMERA_BOUNDARY = 100;
    private static volatile SlideCameraMonitor sInstance;
    private AvailabilityCallback mAvailabilityCallback = new AvailabilityCallback() {
        public void onCameraAvailable(String cameraId) {
            super.onCameraAvailable(cameraId);
            int id = Integer.valueOf(cameraId).intValue();
            if (id < 100) {
                String access$000 = SlideCameraMonitor.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onCameraAvailable:");
                stringBuilder.append(cameraId);
                Slog.d(access$000, stringBuilder.toString());
                SlideCameraMonitor.this.mOpeningCameraID.remove(Integer.valueOf(id));
                if (SlideCameraMonitor.this.mCameraOpenListener != null) {
                    SlideCameraMonitor.this.mCameraOpenListener.onCameraClose(id);
                }
            }
        }

        public void onCameraUnavailable(String cameraId) {
            super.onCameraUnavailable(cameraId);
            int id = Integer.valueOf(cameraId).intValue();
            if (id < 100) {
                String access$000 = SlideCameraMonitor.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onCameraUnavailable:");
                stringBuilder.append(cameraId);
                Slog.d(access$000, stringBuilder.toString());
                SlideCameraMonitor.this.mOpeningCameraID.add(Integer.valueOf(id));
                if (SlideCameraMonitor.this.mCameraOpenListener != null) {
                    SlideCameraMonitor.this.mCameraOpenListener.onCameraOpen(id);
                }
            }
        }
    };
    private List<Integer> mBackCameraID = new ArrayList();
    private CameraManager mCameraManager;
    private CameraOpenListener mCameraOpenListener;
    private Context mContext;
    private List<Integer> mFrontCameraID = new ArrayList();
    private Handler mHandler;
    private Set mOpeningCameraID = new HashSet();

    public interface CameraOpenListener {
        void onCameraClose(int i);

        void onCameraOpen(int i);
    }

    private class H extends Handler {
        public H(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    public static SlideCameraMonitor getInstance() {
        if (sInstance == null) {
            synchronized (SlideCameraMonitor.class) {
                if (sInstance == null) {
                    sInstance = new SlideCameraMonitor();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context, Looper looper) {
        this.mContext = context;
        this.mHandler = new H(looper);
        this.mCameraManager = (CameraManager) this.mContext.getSystemService(Context.CAMERA_SERVICE);
        this.mCameraManager.registerAvailabilityCallback(this.mAvailabilityCallback, this.mHandler);
        initCameraId();
    }

    public boolean isCameraOpening() {
        return isFrontCameraOpening() || isBackCameraOpening();
    }

    public boolean isFrontCameraOpening() {
        List list = this.mFrontCameraID;
        if (!(list == null || list.size() == 0)) {
            for (Integer id : this.mFrontCameraID) {
                if (this.mOpeningCameraID.contains(Integer.valueOf(id.intValue()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isBackCameraOpening() {
        List list = this.mBackCameraID;
        if (!(list == null || list.size() == 0)) {
            for (Integer id : this.mBackCameraID) {
                if (this.mOpeningCameraID.contains(Integer.valueOf(id.intValue()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCameraRecording() {
        List<AudioRecordingConfiguration> configurations = ((AudioManager) this.mContext.getSystemService("audio")).getActiveRecordingConfigurations();
        if (configurations == null || configurations.size() <= 0) {
            return false;
        }
        Slog.d(TAG, "recording");
        return true;
    }

    private void initCameraId() {
        try {
            String[] ids = this.mCameraManager.getCameraIdList();
            if (ids != null && ids.length > 0) {
                for (String sid : ids) {
                    int id = Integer.valueOf(sid).intValue();
                    if (id < 100) {
                        int facing = ((Integer) this.mCameraManager.getCameraCharacteristics(sid).get(CameraCharacteristics.LENS_FACING)).intValue();
                        if (facing == 0) {
                            this.mFrontCameraID.add(Integer.valueOf(id));
                        } else if (facing == 1) {
                            this.mBackCameraID.add(Integer.valueOf(id));
                        }
                    }
                }
            }
        } catch (Exception e) {
            Slog.d(TAG, "Can't initCameraId");
        }
    }

    public void setCameraOpenListener(CameraOpenListener listener) {
        this.mCameraOpenListener = listener;
    }

    public List<Integer> getFrontCameraID() {
        return this.mFrontCameraID;
    }

    public List<Integer> getBackCameraID() {
        return this.mBackCameraID;
    }
}
