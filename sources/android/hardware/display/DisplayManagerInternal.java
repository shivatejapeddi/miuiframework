package android.hardware.display;

import android.hardware.SensorManager;
import android.os.Handler;
import android.util.IntArray;
import android.util.SparseArray;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.SurfaceControl.ScreenshotGraphicBuffer;
import android.view.SurfaceControl.Transaction;

public abstract class DisplayManagerInternal {

    public interface DisplayPowerCallbacks {
        void acquireSuspendBlocker();

        void onDisplayStateChange(int i);

        void onProximityNegative();

        void onProximityPositive();

        void onStateChanged();

        void releaseSuspendBlocker();
    }

    public static final class DisplayPowerRequest {
        public static final int POLICY_BRIGHT = 3;
        public static final int POLICY_DIM = 2;
        public static final int POLICY_DOZE = 1;
        public static final int POLICY_OFF = 0;
        public static final int POLICY_VR = 4;
        public boolean blockScreenOn;
        public boolean boostScreenBrightness;
        public int dozeScreenBrightness;
        public int dozeScreenState;
        public boolean lowPowerMode;
        public int policy;
        public float screenAutoBrightnessAdjustmentOverride;
        public int screenBrightnessOverride;
        public float screenLowPowerBrightnessFactor;
        public boolean useAutoBrightness;
        public boolean useProximitySensor;

        public DisplayPowerRequest() {
            this.policy = 3;
            this.useProximitySensor = false;
            this.screenBrightnessOverride = -1;
            this.useAutoBrightness = false;
            this.screenAutoBrightnessAdjustmentOverride = Float.NaN;
            this.screenLowPowerBrightnessFactor = 0.5f;
            this.blockScreenOn = false;
            this.dozeScreenBrightness = -1;
            this.dozeScreenState = 0;
        }

        public DisplayPowerRequest(DisplayPowerRequest other) {
            copyFrom(other);
        }

        public boolean isBrightOrDim() {
            int i = this.policy;
            return i == 3 || i == 2;
        }

        public boolean isVr() {
            return this.policy == 4;
        }

        public void copyFrom(DisplayPowerRequest other) {
            this.policy = other.policy;
            this.useProximitySensor = other.useProximitySensor;
            this.screenBrightnessOverride = other.screenBrightnessOverride;
            this.useAutoBrightness = other.useAutoBrightness;
            this.screenAutoBrightnessAdjustmentOverride = other.screenAutoBrightnessAdjustmentOverride;
            this.screenLowPowerBrightnessFactor = other.screenLowPowerBrightnessFactor;
            this.blockScreenOn = other.blockScreenOn;
            this.lowPowerMode = other.lowPowerMode;
            this.boostScreenBrightness = other.boostScreenBrightness;
            this.dozeScreenBrightness = other.dozeScreenBrightness;
            this.dozeScreenState = other.dozeScreenState;
        }

        public boolean equals(Object o) {
            return (o instanceof DisplayPowerRequest) && equals((DisplayPowerRequest) o);
        }

        public boolean equals(DisplayPowerRequest other) {
            return other != null && this.policy == other.policy && this.useProximitySensor == other.useProximitySensor && this.screenBrightnessOverride == other.screenBrightnessOverride && this.useAutoBrightness == other.useAutoBrightness && floatEquals(this.screenAutoBrightnessAdjustmentOverride, other.screenAutoBrightnessAdjustmentOverride) && this.screenLowPowerBrightnessFactor == other.screenLowPowerBrightnessFactor && this.blockScreenOn == other.blockScreenOn && this.lowPowerMode == other.lowPowerMode && this.boostScreenBrightness == other.boostScreenBrightness && this.dozeScreenBrightness == other.dozeScreenBrightness && this.dozeScreenState == other.dozeScreenState;
        }

        private boolean floatEquals(float f1, float f2) {
            return f1 == f2 || (Float.isNaN(f1) && Float.isNaN(f2));
        }

        public int hashCode() {
            return 0;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("policy=");
            stringBuilder.append(policyToString(this.policy));
            stringBuilder.append(", useProximitySensor=");
            stringBuilder.append(this.useProximitySensor);
            stringBuilder.append(", screenBrightnessOverride=");
            stringBuilder.append(this.screenBrightnessOverride);
            stringBuilder.append(", useAutoBrightness=");
            stringBuilder.append(this.useAutoBrightness);
            stringBuilder.append(", screenAutoBrightnessAdjustmentOverride=");
            stringBuilder.append(this.screenAutoBrightnessAdjustmentOverride);
            stringBuilder.append(", screenLowPowerBrightnessFactor=");
            stringBuilder.append(this.screenLowPowerBrightnessFactor);
            stringBuilder.append(", blockScreenOn=");
            stringBuilder.append(this.blockScreenOn);
            stringBuilder.append(", lowPowerMode=");
            stringBuilder.append(this.lowPowerMode);
            stringBuilder.append(", boostScreenBrightness=");
            stringBuilder.append(this.boostScreenBrightness);
            stringBuilder.append(", dozeScreenBrightness=");
            stringBuilder.append(this.dozeScreenBrightness);
            stringBuilder.append(", dozeScreenState=");
            stringBuilder.append(Display.stateToString(this.dozeScreenState));
            return stringBuilder.toString();
        }

        public static String policyToString(int policy) {
            if (policy == 0) {
                return "OFF";
            }
            if (policy == 1) {
                return "DOZE";
            }
            if (policy == 2) {
                return "DIM";
            }
            if (policy == 3) {
                return "BRIGHT";
            }
            if (policy != 4) {
                return Integer.toString(policy);
            }
            return "VR";
        }
    }

    public interface DisplayTransactionListener {
        void onDisplayTransaction(Transaction transaction);
    }

    public abstract DisplayInfo getDisplayInfo(int i);

    public abstract DisplayedContentSample getDisplayedContentSample(int i, long j, long j2);

    public abstract DisplayedContentSamplingAttributes getDisplayedContentSamplingAttributes(int i);

    public abstract void getNonOverrideDisplayInfo(int i, DisplayInfo displayInfo);

    public abstract void initPowerManagement(DisplayPowerCallbacks displayPowerCallbacks, Handler handler, SensorManager sensorManager);

    public abstract boolean isProximitySensorAvailable();

    public abstract void onOverlayChanged();

    public abstract void performTraversal(Transaction transaction);

    public abstract void persistBrightnessTrackerState();

    public abstract void registerDisplayTransactionListener(DisplayTransactionListener displayTransactionListener);

    public abstract boolean requestPowerState(DisplayPowerRequest displayPowerRequest, boolean z);

    public abstract ScreenshotGraphicBuffer screenshot(int i);

    public abstract void setDisplayAccessUIDs(SparseArray<IntArray> sparseArray);

    public abstract void setDisplayInfoOverrideFromWindowManager(int i, DisplayInfo displayInfo);

    public abstract void setDisplayOffsets(int i, int i2, int i3);

    public abstract void setDisplayProperties(int i, boolean z, float f, int i2, boolean z2);

    public abstract void setDisplayScalingDisabled(int i, boolean z);

    public abstract boolean setDisplayedContentSamplingEnabled(int i, boolean z, int i2, int i3);

    public abstract void unregisterDisplayTransactionListener(DisplayTransactionListener displayTransactionListener);

    public abstract void updateColorFadeOffAnimatorDuration(boolean z);
}
