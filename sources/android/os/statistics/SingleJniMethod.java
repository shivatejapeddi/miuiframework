package android.os.statistics;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.statistics.MicroscopicEvent.MicroEventFields;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public final class SingleJniMethod extends MicroscopicEvent<JniMethodFields> {
    public static final Creator<SingleJniMethod> CREATOR = new Creator<SingleJniMethod>() {
        public SingleJniMethod createFromParcel(Parcel source) {
            SingleJniMethod object = new SingleJniMethod();
            object.readFromParcel(source);
            return object;
        }

        public SingleJniMethod[] newArray(int size) {
            return new SingleJniMethod[size];
        }
    };

    public static class JniMethodFields extends MicroEventFields {
        private static final String FIELD_STACK = "stack";
        private static final HashMap<Class<?>, ArrayList<String>> exceptionalClassMethodSimpleNames = new HashMap();
        private static final String[] exceptionalJniMethodFullNames = new String[]{"java.lang.Object.wait", "java.lang.Object.notify", "java.lang.Object.notifyAll", "java.lang.Thread.sleep", "android.os.BinderProxy.transactNative"};
        public String[] stackTrace;

        public JniMethodFields() {
            super(true);
        }

        public void fillInStackTrace(Class[] javaStackTraceClasses, StackTraceElement[] javaStackTraceElements, NativeBackTrace nativeBackTrace) {
            this.stackTrace = buildStackTrace(javaStackTraceElements, javaStackTraceClasses);
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            ParcelUtils.writeStringArray(dest, this.stackTrace);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            this.stackTrace = ParcelUtils.readStringArray(source);
            if (this.stackTrace == null) {
                this.stackTrace = StackUtils.emptyStack;
            }
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(FIELD_STACK, JSONObject.wrap(this.stackTrace));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private static String[] buildStackTrace(StackTraceElement[] javaStackTraceElements, Class[] javaStackTraceClasses) {
            if (exceptionalClassMethodSimpleNames.isEmpty()) {
                synchronized (exceptionalClassMethodSimpleNames) {
                    for (String methodFullName : exceptionalJniMethodFullNames) {
                        try {
                            int dotPosition = methodFullName.lastIndexOf(".");
                            String methodSimpleName = methodFullName.substring(dotPosition + 1);
                            Class<?> clazz = Class.forName(methodFullName.substring(0, dotPosition));
                            ArrayList<String> exceptionalMethodSimpleNames = (ArrayList) exceptionalClassMethodSimpleNames.get(clazz);
                            if (exceptionalMethodSimpleNames == null) {
                                exceptionalMethodSimpleNames = new ArrayList();
                                exceptionalClassMethodSimpleNames.put(clazz, exceptionalMethodSimpleNames);
                            }
                            exceptionalMethodSimpleNames.add(methodSimpleName);
                        } catch (ClassNotFoundException e) {
                        }
                    }
                }
            }
            if (javaStackTraceElements == null || javaStackTraceElements.length == 0 || javaStackTraceClasses == null || javaStackTraceClasses.length == 0) {
                return StackUtils.emptyStack;
            }
            StackTraceElement topElement = javaStackTraceElements[0];
            Class<?> topClass = javaStackTraceClasses[0];
            if (topElement.getClassName().startsWith("android.os.statistics.")) {
                return StackUtils.emptyStack;
            }
            ArrayList<String> exceptionalMethodSimpleNames2 = (ArrayList) exceptionalClassMethodSimpleNames.get(topClass);
            if (exceptionalMethodSimpleNames2 == null || !exceptionalMethodSimpleNames2.contains(topElement.getMethodName())) {
                return StackUtils.getStackTrace(javaStackTraceElements, javaStackTraceClasses, null);
            }
            return StackUtils.emptyStack;
        }
    }

    public SingleJniMethod() {
        super(10, new JniMethodFields());
    }

    public boolean isMeaningful() {
        String[] stackTrace = null;
        if (getDetailsFields() != null) {
            stackTrace = ((JniMethodFields) getDetailsFields()).stackTrace;
        }
        return stackTrace != null && stackTrace.length > 0;
    }
}
