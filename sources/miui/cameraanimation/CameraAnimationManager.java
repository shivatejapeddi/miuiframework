package miui.cameraanimation;

import android.content.Context;
import android.database.ContentObserver;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraManager.AvailabilityCallback;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings.Global;
import android.util.Slog;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import java.util.ArrayList;
import java.util.List;
import miui.process.ForegroundInfo;
import miui.process.IForegroundInfoListener.Stub;
import miui.process.ProcessManager;

public class CameraAnimationManager {
    private static final String FORCE_BLACK_V2 = "force_black_v2";
    public static final int FRONT_CAMERA_CLOSE = 1;
    public static final int FRONT_CAMERA_OPEN = 0;
    public static final String TAG = "CameraAnimationManager";
    private static final int VIRTUAL_CAMERA_BOUNDARY = 100;
    public static final ArrayList<String> sMonitorList = new ArrayList();
    private CameraAnimationView mAnimationView;
    private AvailabilityCallback mAvailabilityCallback = new AvailabilityCallback() {
        public void onCameraAvailable(String cameraId) {
            super.onCameraAvailable(cameraId);
            if (CameraAnimationManager.this.mFrontCameraID.contains(Integer.valueOf(Integer.valueOf(cameraId).intValue()))) {
                CameraAnimationManager.this.stopCameraAnimation();
            }
        }

        public void onCameraUnavailable(String cameraId) {
            super.onCameraUnavailable(cameraId);
            int id = Integer.valueOf(cameraId).intValue();
            if (CameraAnimationManager.this.mFrontCameraID.contains(Integer.valueOf(id)) && CameraAnimationManager.this.mLastUnavailableCameraId != id) {
                CameraAnimationManager.this.startCameraAnimation();
            }
            CameraAnimationManager.this.mLastUnavailableCameraId = id;
        }
    };
    private List<Integer> mBackCameraID = new ArrayList();
    private CameraManager mCameraManager;
    private Context mContext;
    private Stub mForegroundInfoChangeListener = new Stub() {
        public void onForegroundInfoChanged(ForegroundInfo foregroundInfo) {
            CameraAnimationManager.this.mForegroundPackage = foregroundInfo.mForegroundPackageName;
            CameraAnimationManager.this.mLastUnavailableCameraId = -1;
        }
    };
    private String mForegroundPackage;
    private List<Integer> mFrontCameraID = new ArrayList();
    private Handler mHandler;
    private boolean mHideNotch;
    private ContentObserver mHideNotchObserver = new ContentObserver(this.mHandler) {
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            CameraAnimationManager cameraAnimationManager = CameraAnimationManager.this;
            boolean z = false;
            if (Global.getInt(cameraAnimationManager.mContext.getContentResolver(), CameraAnimationManager.FORCE_BLACK_V2, 0) != 0) {
                z = true;
            }
            cameraAnimationManager.mHideNotch = z;
        }
    };
    private boolean mIsAddedView;
    private int mLastUnavailableCameraId;
    private WindowManager mWindowManager;

    private class H extends Handler {
        public H(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            LayoutParams layoutParams = CameraAnimationManager.this.getWindowParam();
            int i = msg.what;
            if (i != 0) {
                if (i == 1 && CameraAnimationManager.this.mAnimationView != null && CameraAnimationManager.this.mIsAddedView) {
                    CameraAnimationManager.this.mAnimationView.stopAnimation();
                    CameraAnimationManager.this.mWindowManager.removeView(CameraAnimationManager.this.mAnimationView);
                    CameraAnimationManager.this.mIsAddedView = false;
                }
            } else if (CameraAnimationManager.this.mAnimationView != null && !CameraAnimationManager.this.mIsAddedView) {
                CameraAnimationManager.this.mWindowManager.addView(CameraAnimationManager.this.mAnimationView, layoutParams);
                CameraAnimationManager.this.mIsAddedView = true;
                CameraAnimationManager.this.mAnimationView.startAnimation();
            }
        }
    }

    static {
        sMonitorList.add("com.android.camera");
        sMonitorList.add("com.android.systemui");
        sMonitorList.add("com.mlab.cam");
    }

    public CameraAnimationManager(Context context) {
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        HandlerThread t = new HandlerThread("camera_animation", -2);
        t.start();
        this.mHandler = new H(t.getLooper());
        this.mAnimationView = new CameraAnimationView(context, t.getLooper());
        this.mIsAddedView = false;
        this.mContext.getContentResolver().registerContentObserver(Global.getUriFor(FORCE_BLACK_V2), false, this.mHideNotchObserver, -1);
        this.mHideNotchObserver.onChange(false);
    }

    public void systemReady() {
        this.mCameraManager = (CameraManager) this.mContext.getSystemService(Context.CAMERA_SERVICE);
        this.mCameraManager.registerAvailabilityCallback(this.mAvailabilityCallback, this.mHandler);
        registerForegroundInfoChangeListener();
        initCameraId();
    }

    private void startCameraAnimation() {
        if (canShowAnimation()) {
            this.mHandler.sendEmptyMessage(0);
        }
    }

    private void stopCameraAnimation() {
        this.mHandler.sendEmptyMessage(1);
    }

    private boolean canShowAnimation() {
        return !this.mHideNotch && sMonitorList.contains(this.mForegroundPackage);
    }

    public void registerForegroundInfoChangeListener() {
        ProcessManager.unregisterForegroundInfoListener(this.mForegroundInfoChangeListener);
        ProcessManager.registerForegroundInfoListener(this.mForegroundInfoChangeListener);
    }

    private LayoutParams getWindowParam() {
        LayoutParams lp = new LayoutParams(-1, 85, 2015, 1336, -3);
        lp.privateFlags |= 64;
        lp.privateFlags |= 16;
        lp.layoutInDisplayCutoutMode = 1;
        lp.gravity = 51;
        lp.setTitle("cameraAnimation");
        return lp;
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
}
