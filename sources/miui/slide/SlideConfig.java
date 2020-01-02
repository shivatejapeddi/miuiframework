package miui.slide;

import android.app.Activity;
import android.app.AppGlobals;
import android.media.AudioManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.provider.MiuiSettings.Global;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import miui.slide.ISlideManagerService.Stub;

public class SlideConfig implements Parcelable {
    public static final boolean BOOLEAN_CONDITION_TRUE_FALSE = true;
    public static final Creator<SlideConfig> CREATOR = new Creator<SlideConfig>() {
        public SlideConfig createFromParcel(Parcel in) {
            return new SlideConfig(in, null);
        }

        public SlideConfig[] newArray(int size) {
            return new SlideConfig[size];
        }
    };
    public static final int FLAG_ACTION_BACK = 8;
    public static final int FLAG_ACTION_CLICK_CLASSNAME = 2;
    public static final int FLAG_ACTION_CLICK_VIEW = 16;
    public static final int FLAG_ACTION_CLICK_VIEWID = 1;
    public static final int FLAG_ACTION_TOUCH_POSITION = 4;
    public static final int FLAG_CONDITION_AUDIO_COMMUNICATION = 4;
    public static final int FLAG_CONDITION_AUDIO_NOT_RECORDING = 5;
    public static final int FLAG_CONDITION_BACK_CAMERA_OPEN = 3;
    public static final int FLAG_CONDITION_CAMERA_OPEN = 1;
    public static final int FLAG_CONDITION_FRONT_CAMERA_OPEN = 2;
    public static final int FLAG_RESULT_GOTO_ACTIVITY = 1;
    public static final int FLAG_RESULT_OPEN_AUDIO = 4;
    public static final int FLAG_RESULT_OPEN_CAMERA = 2;
    public static final String TAG = "SlideConfig";
    final int DEFAULT_EDGE_FLAGS;
    final int DEFAULT_META_STATE;
    final float DEFAULT_PRECISION_X;
    final float DEFAULT_PRECISION_Y;
    final float DEFAULT_SIZE;
    public boolean mConditionTrueFalse;
    public int mFlagAction;
    public int mFlagCondition;
    public int mFlagResult;
    public int mKeyCode;
    public String mStartingActivity;
    public String mTargetActivity;
    public List<TouchEventConfig> mTouchEventConfigList;
    public int mVersionCode;
    public String mViewClassName;
    public String mViewID;

    public static class TouchEventConfig implements Parcelable {
        public static final Creator<TouchEventConfig> CREATOR = new Creator<TouchEventConfig>() {
            public TouchEventConfig createFromParcel(Parcel in) {
                return new TouchEventConfig(in, null);
            }

            public TouchEventConfig[] newArray(int size) {
                return new TouchEventConfig[size];
            }
        };
        public int mPositionBetaX;
        public int mPositionBetaY;
        public int mPositionX;
        public int mPositionY;
        public int mWaitingTime;

        /* synthetic */ TouchEventConfig(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        public TouchEventConfig(int positionX, int positionY, int positionBetaX, int positionBetaY, int waitingTime) {
            this.mPositionX = positionX;
            this.mPositionY = positionY;
            this.mPositionBetaX = positionBetaX;
            this.mPositionBetaY = positionBetaY;
            this.mWaitingTime = waitingTime;
        }

        public TouchEventConfig(TouchEventConfig origin) {
            this.mPositionX = origin.mPositionX;
            this.mPositionY = origin.mPositionY;
            this.mPositionBetaX = origin.mPositionBetaX;
            this.mPositionBetaY = origin.mPositionBetaY;
            this.mWaitingTime = origin.mWaitingTime;
        }

        private TouchEventConfig(Parcel in) {
            this.mPositionX = in.readInt();
            this.mPositionY = in.readInt();
            this.mPositionBetaX = in.readInt();
            this.mPositionBetaY = in.readInt();
            this.mWaitingTime = in.readInt();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mPositionX);
            dest.writeInt(this.mPositionY);
            dest.writeInt(this.mPositionBetaX);
            dest.writeInt(this.mPositionBetaY);
            dest.writeInt(this.mWaitingTime);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("TouchEventConfig{, mPositionX=");
            stringBuilder.append(this.mPositionX);
            stringBuilder.append(", mPositionY=");
            stringBuilder.append(this.mPositionY);
            stringBuilder.append(", mPositionBetaX=");
            stringBuilder.append(this.mPositionBetaX);
            stringBuilder.append(", mPositionBetaY=");
            stringBuilder.append(this.mPositionBetaY);
            stringBuilder.append(", mWaitingTime=");
            stringBuilder.append(this.mWaitingTime);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    /* synthetic */ SlideConfig(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public SlideConfig() {
        this.mTouchEventConfigList = new ArrayList();
        this.DEFAULT_SIZE = 1.0f;
        this.DEFAULT_META_STATE = 0;
        this.DEFAULT_PRECISION_X = 1.0f;
        this.DEFAULT_PRECISION_Y = 1.0f;
        this.DEFAULT_EDGE_FLAGS = 0;
    }

    public SlideConfig(int keyCode, int versionCode, String startingActivity, int flagAction, int flagResult, int flagCondition, boolean conditionTrueFalse, String viewID, String viewClassName, List<TouchEventConfig> touchEventConfigList, String targetActivity) {
        this.mTouchEventConfigList = new ArrayList();
        this.DEFAULT_SIZE = 1.0f;
        this.DEFAULT_META_STATE = 0;
        this.DEFAULT_PRECISION_X = 1.0f;
        this.DEFAULT_PRECISION_Y = 1.0f;
        this.DEFAULT_EDGE_FLAGS = 0;
        this.mKeyCode = keyCode;
        this.mVersionCode = versionCode;
        this.mStartingActivity = startingActivity;
        this.mFlagAction = flagAction;
        this.mFlagResult = flagResult;
        this.mFlagCondition = flagCondition;
        this.mConditionTrueFalse = conditionTrueFalse;
        this.mViewID = viewID;
        this.mViewClassName = viewClassName;
        this.mTouchEventConfigList = touchEventConfigList;
        this.mTargetActivity = targetActivity;
    }

    private SlideConfig(Parcel in) {
        this.mTouchEventConfigList = new ArrayList();
        this.DEFAULT_SIZE = 1.0f;
        boolean z = false;
        this.DEFAULT_META_STATE = 0;
        this.DEFAULT_PRECISION_X = 1.0f;
        this.DEFAULT_PRECISION_Y = 1.0f;
        this.DEFAULT_EDGE_FLAGS = 0;
        this.mKeyCode = in.readInt();
        this.mVersionCode = in.readInt();
        this.mStartingActivity = in.readString();
        this.mFlagAction = in.readInt();
        this.mFlagResult = in.readInt();
        this.mFlagCondition = in.readInt();
        if (in.readInt() == 1) {
            z = true;
        }
        this.mConditionTrueFalse = z;
        this.mViewID = in.readString();
        this.mViewClassName = in.readString();
        in.readTypedList(this.mTouchEventConfigList, TouchEventConfig.CREATOR);
        this.mTargetActivity = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mKeyCode);
        dest.writeInt(this.mVersionCode);
        dest.writeString(this.mStartingActivity);
        dest.writeInt(this.mFlagAction);
        dest.writeInt(this.mFlagResult);
        dest.writeInt(this.mFlagCondition);
        dest.writeInt(this.mConditionTrueFalse);
        dest.writeString(this.mViewID);
        dest.writeString(this.mViewClassName);
        dest.writeTypedList(this.mTouchEventConfigList);
        dest.writeString(this.mTargetActivity);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SlideConfig{, mKeyCode=");
        stringBuilder.append(this.mKeyCode);
        stringBuilder.append(", mVersionCode=");
        stringBuilder.append(this.mVersionCode);
        stringBuilder.append(", mStartingActivity=");
        stringBuilder.append(this.mStartingActivity);
        stringBuilder.append(", mFlagAction=");
        stringBuilder.append(this.mFlagAction);
        stringBuilder.append(", mFlagResult=");
        stringBuilder.append(this.mFlagResult);
        stringBuilder.append(", mFlagCondition=");
        stringBuilder.append(this.mFlagCondition);
        stringBuilder.append(", mConditionTrueFalse=");
        stringBuilder.append(this.mConditionTrueFalse);
        stringBuilder.append(", mViewID=");
        stringBuilder.append(this.mViewID);
        stringBuilder.append(", mViewClassName=");
        stringBuilder.append(this.mViewClassName);
        stringBuilder.append(", mTargetActivity=");
        stringBuilder.append(this.mTargetActivity);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public boolean hasActionFlag(int flag) {
        return (this.mFlagAction & flag) != 0;
    }

    public int tryGotoTarget(Activity activity, View decorView) {
        if ((this.mConditionTrueFalse ^ checkCondition()) != 0) {
            return 0;
        }
        boolean hasActionFlag = hasActionFlag(8);
        String str = TAG;
        if (hasActionFlag) {
            Log.d(str, "FLAG_ACTION_BACK");
            if (decorView != null) {
                injectBackKey(decorView);
                return 8;
            }
        }
        if (hasActionFlag(1)) {
            Log.d(str, "FLAG_ACTION_CLICK_VIEWID");
            if (!(TextUtils.isEmpty(this.mViewID) || activity.getResources() == null)) {
                int resId = activity.getResources().getIdentifier(this.mViewID, "id", activity.getBasePackageName());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("resId: ");
                stringBuilder.append(resId);
                Log.d(str, stringBuilder.toString());
                View targetView = activity.findViewById(resId);
                if (targetView != null && targetView.isVisibleToUser()) {
                    Log.d(str, "Target found by resId");
                    targetView.performClick();
                    return 1;
                }
            }
        }
        if (hasActionFlag(4)) {
            Log.d(str, "FLAG_ACTION_TOUCH_POSITION");
            if (decorView != null) {
                boolean hasNavBar = Global.getBoolean(activity.getContentResolver(), Global.FORCE_FSG_NAV_BAR);
                List list = this.mTouchEventConfigList;
                if (list != null && list.size() > 0) {
                    TouchEventConfig config = (TouchEventConfig) this.mTouchEventConfigList.get(0);
                    injectMotionEvent(decorView, hasNavBar ? config.mPositionBetaX : config.mPositionX, hasNavBar ? config.mPositionBetaY : config.mPositionY);
                    int waitingTime = config.mWaitingTime;
                    if (this.mTouchEventConfigList.size() > 1 && waitingTime > 0) {
                        activity.getMainThreadHandler().postDelayed(new -$$Lambda$SlideConfig$OWrUjwHDwalS9A4Tr0PKIikWk3I(this, decorView, hasNavBar, (TouchEventConfig) this.mTouchEventConfigList.get(1)), (long) waitingTime);
                    }
                }
                return 4;
            }
        }
        return 0;
    }

    public /* synthetic */ void lambda$tryGotoTarget$0$SlideConfig(View decorView, boolean hasNavBar, TouchEventConfig config2) {
        injectMotionEvent(decorView, hasNavBar ? config2.mPositionBetaX : config2.mPositionX, hasNavBar ? config2.mPositionBetaY : config2.mPositionY);
    }

    private void injectMotionEvent(View decorView, int positionX, int positionY) {
        int i = positionX;
        int i2 = positionY;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TouchEvent: ");
        stringBuilder.append(i);
        stringBuilder.append("-");
        stringBuilder.append(i2);
        Log.d(TAG, stringBuilder.toString());
        long now = SystemClock.uptimeMillis();
        long j = now;
        MotionEvent downEvent = MotionEvent.obtain(now, j, 0, (float) i, (float) i2, 1.0f, 1.0f, 0, 1065353216, 1.0f, getInputDeviceId(4098), 0);
        downEvent.setSource(4098);
        decorView.getViewRootImpl().dispatchInputEvent(downEvent);
        MotionEvent upEvent = MotionEvent.obtain(now, j, 1, (float) i, (float) i2, 0.0f, 1.0f, 0, 1.0f, 1.0f, getInputDeviceId(4098), 0);
        upEvent.setSource(4098);
        decorView.getViewRootImpl().dispatchInputEvent(upEvent);
    }

    private void injectBackKey(View decorView) {
        long now = SystemClock.uptimeMillis();
        long j = now;
        long j2 = now;
        decorView.getViewRootImpl().dispatchInputEvent(new KeyEvent(j, j2, 0, 4, 0, 0, -1, 0, 0, 257));
        decorView.getViewRootImpl().dispatchInputEvent(new KeyEvent(j, j2, 1, 4, 0, 0, -1, 0, 0, 257));
    }

    private int getInputDeviceId(int inputSource) {
        for (int devId : InputDevice.getDeviceIds()) {
            if (InputDevice.getDevice(devId).supportsSource(inputSource)) {
                return devId;
            }
        }
        return 0;
    }

    public boolean checkCondition() {
        boolean z = false;
        try {
            ISlideManagerService slideManager = Stub.asInterface(ServiceManager.getService(SlideManagerService.SERVICE_NAME));
            int i = this.mFlagCondition;
            if (i == 1) {
                if (slideManager.getCameraStatus() != 0) {
                    z = true;
                }
                return z;
            } else if (i == 2) {
                if ((slideManager.getCameraStatus() & 1) != 0) {
                    z = true;
                }
                return z;
            } else if (i == 3) {
                if ((slideManager.getCameraStatus() & 2) != 0) {
                    z = true;
                }
                return z;
            } else if (i == 4) {
                if (((AudioManager) AppGlobals.getInitialApplication().getSystemService("audio")).getMode() == 3) {
                    z = true;
                }
                return z;
            } else if (i != 5) {
                return true;
            } else {
                i = slideManager.getCameraStatus();
                if (i != 0 && (i & 4) == 0) {
                    z = true;
                }
                return z;
            }
        } catch (Exception e) {
            Slog.d(TAG, e.getMessage());
            return false;
        }
    }
}
