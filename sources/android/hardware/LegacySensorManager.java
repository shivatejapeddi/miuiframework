package android.hardware;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.view.IRotationWatcher;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import java.util.HashMap;

final class LegacySensorManager {
    private static boolean sInitialized;
    private static int sRotation = 0;
    private static IWindowManager sWindowManager;
    private final HashMap<SensorListener, LegacyListener> mLegacyListenersMap = new HashMap();
    private final SensorManager mSensorManager;

    private static final class LegacyListener implements SensorEventListener {
        private int mSensors;
        private SensorListener mTarget;
        private float[] mValues = new float[6];
        private final LmsFilter mYawfilter = new LmsFilter();

        LegacyListener(SensorListener target) {
            this.mTarget = target;
            this.mSensors = 0;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean registerSensor(int legacyType) {
            boolean alreadyHasOrientationSensor = this.mSensors;
            if ((alreadyHasOrientationSensor & legacyType) != 0) {
                return false;
            }
            alreadyHasOrientationSensor = hasOrientationSensor(alreadyHasOrientationSensor);
            this.mSensors |= legacyType;
            if (alreadyHasOrientationSensor && hasOrientationSensor(legacyType)) {
                return false;
            }
            return true;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean unregisterSensor(int legacyType) {
            int i = this.mSensors;
            if ((i & legacyType) == 0) {
                return false;
            }
            this.mSensors = i & (~legacyType);
            if (hasOrientationSensor(legacyType) && hasOrientationSensor(this.mSensors)) {
                return false;
            }
            return true;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean hasSensors() {
            return this.mSensors != 0;
        }

        private static boolean hasOrientationSensor(int sensors) {
            return (sensors & 129) != 0;
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            try {
                this.mTarget.onAccuracyChanged(getLegacySensorType(sensor.getType()), accuracy);
            } catch (AbstractMethodError e) {
            }
        }

        public void onSensorChanged(SensorEvent event) {
            float[] v = this.mValues;
            v[0] = event.values[0];
            v[1] = event.values[1];
            v[2] = event.values[2];
            int type = event.sensor.getType();
            int legacyType = getLegacySensorType(type);
            mapSensorDataToWindow(legacyType, v, LegacySensorManager.getRotation());
            if (type == 3) {
                if ((this.mSensors & 128) != 0) {
                    this.mTarget.onSensorChanged(128, v);
                }
                if ((this.mSensors & 1) != 0) {
                    v[0] = this.mYawfilter.filter(event.timestamp, v[0]);
                    this.mTarget.onSensorChanged(1, v);
                    return;
                }
                return;
            }
            this.mTarget.onSensorChanged(legacyType, v);
        }

        /* JADX WARNING: Removed duplicated region for block: B:10:0x0032  */
        /* JADX WARNING: Removed duplicated region for block: B:34:? A:{SYNTHETIC, RETURN} */
        /* JADX WARNING: Removed duplicated region for block: B:22:0x005a  */
        /* JADX WARNING: Missing block: B:4:0x0013, code skipped:
            if (r10 != 128) goto L_0x001f;
     */
        /* JADX WARNING: Missing block: B:13:0x0038, code skipped:
            if (r10 != 128) goto L_0x0056;
     */
        private void mapSensorDataToWindow(int r10, float[] r11, int r12) {
            /*
            r9 = this;
            r0 = 0;
            r1 = r11[r0];
            r2 = 1;
            r3 = r11[r2];
            r4 = 2;
            r5 = r11[r4];
            r6 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
            r7 = 8;
            if (r10 == r2) goto L_0x001d;
        L_0x000f:
            if (r10 == r4) goto L_0x0019;
        L_0x0011:
            if (r10 == r7) goto L_0x0016;
        L_0x0013:
            if (r10 == r6) goto L_0x001d;
        L_0x0015:
            goto L_0x001f;
        L_0x0016:
            r1 = -r1;
            r3 = -r3;
            goto L_0x001f;
        L_0x0019:
            r1 = -r1;
            r3 = -r3;
            r5 = -r5;
            goto L_0x001f;
        L_0x001d:
            r5 = -r5;
        L_0x001f:
            r11[r0] = r1;
            r11[r2] = r3;
            r11[r4] = r5;
            r8 = 3;
            r11[r8] = r1;
            r8 = 4;
            r11[r8] = r3;
            r8 = 5;
            r11[r8] = r5;
            r8 = r12 & 1;
            if (r8 == 0) goto L_0x0056;
        L_0x0032:
            if (r10 == r2) goto L_0x0043;
        L_0x0034:
            if (r10 == r4) goto L_0x003b;
        L_0x0036:
            if (r10 == r7) goto L_0x003b;
        L_0x0038:
            if (r10 == r6) goto L_0x0043;
        L_0x003a:
            goto L_0x0056;
        L_0x003b:
            r8 = -r3;
            r11[r0] = r8;
            r11[r2] = r1;
            r11[r4] = r5;
            goto L_0x0056;
        L_0x0043:
            r8 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
            r8 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1));
            if (r8 >= 0) goto L_0x004c;
        L_0x0049:
            r8 = 90;
            goto L_0x004e;
        L_0x004c:
            r8 = -270; // 0xfffffffffffffef2 float:NaN double:NaN;
        L_0x004e:
            r8 = (float) r8;
            r8 = r8 + r1;
            r11[r0] = r8;
            r11[r2] = r5;
            r11[r4] = r3;
        L_0x0056:
            r8 = r12 & 2;
            if (r8 == 0) goto L_0x0084;
        L_0x005a:
            r1 = r11[r0];
            r3 = r11[r2];
            r5 = r11[r4];
            if (r10 == r2) goto L_0x0072;
        L_0x0062:
            if (r10 == r4) goto L_0x0069;
        L_0x0064:
            if (r10 == r7) goto L_0x0069;
        L_0x0066:
            if (r10 == r6) goto L_0x0072;
        L_0x0068:
            goto L_0x0084;
        L_0x0069:
            r6 = -r1;
            r11[r0] = r6;
            r0 = -r3;
            r11[r2] = r0;
            r11[r4] = r5;
            goto L_0x0084;
        L_0x0072:
            r6 = 1127481344; // 0x43340000 float:180.0 double:5.570497984E-315;
            r7 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1));
            if (r7 < 0) goto L_0x007b;
        L_0x0078:
            r6 = r1 - r6;
            goto L_0x007c;
        L_0x007b:
            r6 = r6 + r1;
        L_0x007c:
            r11[r0] = r6;
            r0 = -r3;
            r11[r2] = r0;
            r0 = -r5;
            r11[r4] = r0;
        L_0x0084:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.LegacySensorManager$LegacyListener.mapSensorDataToWindow(int, float[], int):void");
        }

        private static int getLegacySensorType(int type) {
            if (type == 1) {
                return 2;
            }
            if (type == 2) {
                return 8;
            }
            if (type == 3) {
                return 128;
            }
            if (type != 7) {
                return 0;
            }
            return 4;
        }
    }

    private static final class LmsFilter {
        private static final int COUNT = 12;
        private static final float PREDICTION_RATIO = 0.33333334f;
        private static final float PREDICTION_TIME = 0.08f;
        private static final int SENSORS_RATE_MS = 20;
        private int mIndex = 12;
        private long[] mT = new long[24];
        private float[] mV = new float[24];

        public float filter(long time, float in) {
            float T;
            float v = in;
            float v1 = this.mV[this.mIndex];
            if (v - v1 > 180.0f) {
                v -= 360.0f;
            } else if (v1 - v > 180.0f) {
                v += 360.0f;
            }
            this.mIndex++;
            if (this.mIndex >= 24) {
                this.mIndex = 12;
            }
            float[] fArr = this.mV;
            int i = this.mIndex;
            fArr[i] = v;
            long[] jArr = this.mT;
            jArr[i] = time;
            fArr[i - 12] = v;
            jArr[i - 12] = time;
            float E = 0.0f;
            float D = 0.0f;
            float C = 0.0f;
            float B = 0.0f;
            float A = 0.0f;
            for (int i2 = 0; i2 < 11; i2++) {
                int j = (this.mIndex - 1) - i2;
                float Z = this.mV[j];
                long[] jArr2 = this.mT;
                T = ((float) (((jArr2[j] / 2) + (jArr2[j + 1] / 2)) - time)) * 1.0E-9f;
                float dT = ((float) (jArr2[j] - jArr2[j + 1])) * 1.0E-9f;
                dT *= dT;
                A += Z * dT;
                B += (T * dT) * T;
                C += T * dT;
                D += (T * dT) * Z;
                E += dT;
            }
            float E2 = E;
            T = ((A * B) + (C * D)) / ((E2 * B) + (C * C));
            float f = ((PREDICTION_TIME * (((E2 * T) - A) / C)) + T) * 0.0027777778f;
            if ((f >= 0.0f ? f : -f) >= 0.5f) {
                f = (f - ((float) Math.ceil((double) (0.5f + f)))) + 1.0f;
            }
            if (f < 0.0f) {
                f += 1.0f;
            }
            return f * 360.0f;
        }
    }

    public LegacySensorManager(SensorManager sensorManager) {
        this.mSensorManager = sensorManager;
        synchronized (SensorManager.class) {
            if (!sInitialized) {
                sWindowManager = Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE));
                if (sWindowManager != null) {
                    try {
                        sRotation = sWindowManager.watchRotation(new IRotationWatcher.Stub() {
                            public void onRotationChanged(int rotation) {
                                LegacySensorManager.onRotationChanged(rotation);
                            }
                        }, 0);
                    } catch (RemoteException e) {
                    }
                }
            }
        }
    }

    public int getSensors() {
        int result = 0;
        for (Sensor i : this.mSensorManager.getFullSensorList()) {
            int type = i.getType();
            if (type == 1) {
                result |= 2;
            } else if (type == 2) {
                result |= 8;
            } else if (type == 3) {
                result |= 129;
            }
        }
        return result;
    }

    public boolean registerListener(SensorListener listener, int sensors, int rate) {
        boolean result = false;
        if (listener == null) {
            return false;
        }
        boolean z = registerLegacyListener(2, 1, listener, sensors, rate) || false;
        z = registerLegacyListener(8, 2, listener, sensors, rate) || z;
        z = registerLegacyListener(128, 3, listener, sensors, rate) || z;
        z = registerLegacyListener(1, 3, listener, sensors, rate) || z;
        boolean result2 = z;
        if (registerLegacyListener(4, 7, listener, sensors, rate) || result2) {
            result = true;
        }
        return result;
    }

    private boolean registerLegacyListener(int legacyType, int type, SensorListener listener, int sensors, int rate) {
        boolean result = false;
        if ((sensors & legacyType) != 0) {
            Sensor sensor = this.mSensorManager.getDefaultSensor(type);
            if (sensor != null) {
                synchronized (this.mLegacyListenersMap) {
                    SensorEventListener legacyListener = (LegacyListener) this.mLegacyListenersMap.get(listener);
                    if (legacyListener == null) {
                        legacyListener = new LegacyListener(listener);
                        this.mLegacyListenersMap.put(listener, legacyListener);
                    }
                    if (legacyListener.registerSensor(legacyType)) {
                        result = this.mSensorManager.registerListener(legacyListener, sensor, rate);
                    } else {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    public void unregisterListener(SensorListener listener, int sensors) {
        if (listener != null) {
            unregisterLegacyListener(2, 1, listener, sensors);
            unregisterLegacyListener(8, 2, listener, sensors);
            unregisterLegacyListener(128, 3, listener, sensors);
            unregisterLegacyListener(1, 3, listener, sensors);
            unregisterLegacyListener(4, 7, listener, sensors);
        }
    }

    private void unregisterLegacyListener(int legacyType, int type, SensorListener listener, int sensors) {
        if ((sensors & legacyType) != 0) {
            Sensor sensor = this.mSensorManager.getDefaultSensor(type);
            if (sensor != null) {
                synchronized (this.mLegacyListenersMap) {
                    SensorEventListener legacyListener = (LegacyListener) this.mLegacyListenersMap.get(listener);
                    if (legacyListener != null && legacyListener.unregisterSensor(legacyType)) {
                        this.mSensorManager.unregisterListener(legacyListener, sensor);
                        if (!legacyListener.hasSensors()) {
                            this.mLegacyListenersMap.remove(listener);
                        }
                    }
                }
            }
        }
    }

    static void onRotationChanged(int rotation) {
        synchronized (SensorManager.class) {
            sRotation = rotation;
        }
    }

    static int getRotation() {
        int i;
        synchronized (SensorManager.class) {
            i = sRotation;
        }
        return i;
    }
}
