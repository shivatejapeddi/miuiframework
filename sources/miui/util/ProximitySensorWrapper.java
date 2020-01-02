package miui.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.Slog;
import java.util.ArrayList;
import java.util.List;

public class ProximitySensorWrapper {
    private static final int EVENT_FAR = 1;
    private static final int EVENT_TOO_CLOSE = 0;
    private static final float PROXIMITY_THRESHOLD = 4.0f;
    public static final int STATE_STABLE_DELAY = 300;
    private final Context mContext;
    private final Handler mHandler;
    private final List<ProximitySensorChangeListener> mProximitySensorChangeListeners = new ArrayList();
    private int mProximitySensorState;
    private final Sensor mSensor;
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            float distance = event.values[0];
            boolean isTooClose = ((double) distance) >= 0.0d && distance < ProximitySensorWrapper.PROXIMITY_THRESHOLD && distance < ProximitySensorWrapper.this.mSensor.getMaximumRange();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("proximity distance: ");
            stringBuilder.append(distance);
            Slog.d("ProximitySensorWrapper", stringBuilder.toString());
            if (isTooClose) {
                if (ProximitySensorWrapper.this.mProximitySensorState != 1) {
                    ProximitySensorWrapper.this.mProximitySensorState = 1;
                    ProximitySensorWrapper.this.mHandler.removeMessages(1);
                    ProximitySensorWrapper.this.mHandler.sendEmptyMessageDelayed(0, 300);
                }
            } else if (ProximitySensorWrapper.this.mProximitySensorState != 0) {
                ProximitySensorWrapper.this.mProximitySensorState = 0;
                ProximitySensorWrapper.this.mHandler.removeMessages(0);
                ProximitySensorWrapper.this.mHandler.sendEmptyMessageDelayed(1, 300);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private final SensorManager mSensorManager;

    public interface ProximitySensorChangeListener {
        void onSensorChanged(boolean z);
    }

    public ProximitySensorWrapper(Context context) {
        this.mContext = context;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                int i = msg.what;
                if (i == 0) {
                    ProximitySensorWrapper.this.notifyListeners(true);
                } else if (i == 1) {
                    ProximitySensorWrapper.this.notifyListeners(false);
                }
            }
        };
        this.mProximitySensorState = -1;
        this.mSensorManager = (SensorManager) this.mContext.getSystemService(Context.SENSOR_SERVICE);
        this.mSensor = this.mSensorManager.getDefaultSensor(8);
    }

    private void notifyListeners(boolean tooClose) {
        synchronized (this.mProximitySensorChangeListeners) {
            for (ProximitySensorChangeListener listener : this.mProximitySensorChangeListeners) {
                listener.onSensorChanged(tooClose);
            }
        }
    }

    public void registerListener(ProximitySensorChangeListener listener) {
        synchronized (this.mProximitySensorChangeListeners) {
            if (!this.mProximitySensorChangeListeners.contains(listener)) {
                if (this.mProximitySensorChangeListeners.size() == 0) {
                    this.mSensorManager.registerListener(this.mSensorListener, this.mSensor, 0);
                }
                this.mProximitySensorChangeListeners.add(listener);
            }
        }
    }

    public void unregisterListener(ProximitySensorChangeListener listener) {
        synchronized (this.mProximitySensorChangeListeners) {
            this.mProximitySensorChangeListeners.remove(listener);
            unregisterSensorEventListenerLocked();
        }
    }

    public void unregisterAllListeners() {
        synchronized (this.mProximitySensorChangeListeners) {
            this.mProximitySensorChangeListeners.clear();
            unregisterSensorEventListenerLocked();
        }
    }

    private void unregisterSensorEventListenerLocked() {
        if (this.mProximitySensorChangeListeners.size() == 0) {
            this.mSensorManager.unregisterListener(this.mSensorListener, this.mSensor);
        }
    }
}
