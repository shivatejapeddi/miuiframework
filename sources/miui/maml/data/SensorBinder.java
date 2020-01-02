package miui.maml.data;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.speech.tts.TextToSpeech.Engine;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import miui.maml.ScreenElementRoot;
import miui.maml.util.Utils;
import org.w3c.dom.Element;

public class SensorBinder extends VariableBinder {
    private static final String LOG_TAG = "SensorBinder";
    private static final HashMap<String, Integer> SENSOR_TYPES = new HashMap();
    public static final String TAG_NAME = "SensorBinder";
    private static SensorManager mSensorManager;
    private boolean mEnable;
    private Expression mEnableExp;
    private int mRate;
    private boolean mRegistered;
    private Sensor mSensor;
    private SensorEventListener mSensorEventListener;
    private String mType;

    private static class Variable extends miui.maml.data.VariableBinder.Variable {
        public int mIndex;

        public Variable(Element node, Variables var) {
            super(node, var);
            this.mIndex = Utils.getAttrAsInt(node, "index", 0);
        }
    }

    static {
        SENSOR_TYPES.put("orientation", Integer.valueOf(3));
        SENSOR_TYPES.put("gravity", Integer.valueOf(9));
        SENSOR_TYPES.put("accelerometer", Integer.valueOf(1));
        SENSOR_TYPES.put("linear_acceleration", Integer.valueOf(10));
        SENSOR_TYPES.put("pressure", Integer.valueOf(6));
        SENSOR_TYPES.put("proximity", Integer.valueOf(8));
        SENSOR_TYPES.put("light", Integer.valueOf(5));
        SENSOR_TYPES.put("gyroscope", Integer.valueOf(4));
    }

    public SensorBinder(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mType = node.getAttribute("type");
        this.mRate = Utils.getAttrAsInt(node, Engine.KEY_PARAM_RATE, 3);
        this.mEnableExp = Expression.build(getVariables(), node.getAttribute("enable"));
        if (mSensorManager == null) {
            mSensorManager = (SensorManager) getContext().mContext.getSystemService(Context.SENSOR_SERVICE);
        }
        this.mSensor = mSensorManager.getDefaultSensor(getSensorType(this.mType));
        if (this.mSensor == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fail to get sensor! TYPE: ");
            stringBuilder.append(this.mType);
            Log.e("SensorBinder", stringBuilder.toString());
            return;
        }
        this.mSensorEventListener = new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                int size = event.values.length;
                Iterator it = SensorBinder.this.mVariables.iterator();
                while (it.hasNext()) {
                    Variable var = (Variable) ((miui.maml.data.VariableBinder.Variable) it.next());
                    if (var.mIndex >= 0 && var.mIndex < size) {
                        var.set((double) event.values[var.mIndex]);
                    }
                }
                SensorBinder.this.onUpdateComplete();
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        loadVariables(node);
    }

    public void finish() {
        unregisterListener();
        super.finish();
    }

    public void init() {
        super.init();
        Expression expression = this.mEnableExp;
        boolean z = true;
        if (expression != null && expression.evaluate() <= 0.0d) {
            z = false;
        }
        this.mEnable = z;
        registerListener();
    }

    public void pause() {
        super.pause();
        unregisterListener();
    }

    public void resume() {
        super.resume();
        registerListener();
    }

    private void registerListener() {
        if (!this.mRegistered) {
            Sensor sensor = this.mSensor;
            if (sensor != null && this.mEnable) {
                mSensorManager.registerListener(this.mSensorEventListener, sensor, this.mRate);
                this.mRegistered = true;
            }
        }
    }

    private void unregisterListener() {
        if (this.mRegistered) {
            Sensor sensor = this.mSensor;
            if (sensor != null) {
                mSensorManager.unregisterListener(this.mSensorEventListener, sensor);
                this.mRegistered = false;
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public Variable onLoadVariable(Element child) {
        return new Variable(child, getContext().mVariables);
    }

    private int getSensorType(String name) {
        Integer type = (Integer) SENSOR_TYPES.get(name);
        if (type != null) {
            return type.intValue();
        }
        try {
            return Integer.parseInt(name);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
