package android.os.statistics;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.statistics.MicroscopicEvent.BlockedEventFields;
import android.os.statistics.MicroscopicEvent.RootEventFields;
import android.os.statistics.PerfEvent.DetailFields;
import org.json.JSONException;
import org.json.JSONObject;

public class BinderSuperviser {

    public static class BinderCallFields extends BlockedEventFields {
        private static final String FIELD_CODE = "code";
        private static final String FIELD_INTERFACE_DESCRIPTOR = "interface";
        private static final String FIELD_STACK = "stack";
        private static Class<?> sBinderProxyClass;
        public int code;
        public String interfaceDescriptor;
        public final String packageName;
        public final String processName;
        public String[] stackTrace;

        public BinderCallFields() {
            super(true);
            String str = "";
            this.processName = str;
            this.packageName = str;
        }

        public void fillIn(JniParcel dataParcel) {
            super.fillIn(dataParcel);
            Object binder = dataParcel.readObject();
            this.interfaceDescriptor = dataParcel.readString();
            this.code = dataParcel.readInt();
            if (binder != null) {
                this.interfaceDescriptor = BinderSuperviser.getInterfaceDescriptor(binder);
            }
            if (this.interfaceDescriptor == null) {
                this.interfaceDescriptor = "";
            }
        }

        public void fillInStackTrace(Class[] javaStackTraceClasses, StackTraceElement[] javaStackTraceElements, NativeBackTrace nativeBackTrace) {
            initClasses();
            if (isJavaBinderCall(javaStackTraceElements, javaStackTraceClasses)) {
                for (int i = 0; i < javaStackTraceClasses.length; i++) {
                    if (javaStackTraceClasses[i] == sBinderProxyClass) {
                        javaStackTraceClasses[i] = null;
                        javaStackTraceElements[i] = null;
                    }
                }
                this.stackTrace = StackUtils.getStackTrace(javaStackTraceElements, javaStackTraceClasses, null);
            } else {
                String[] nativeStackTrace = NativeBackTrace.resolve(nativeBackTrace);
                if (nativeStackTrace != null) {
                    for (int i2 = 0; i2 < nativeStackTrace.length; i2++) {
                        String line = nativeStackTrace[i2];
                        if (line == null || (!line.contains("BinderSuperviser") && !line.contains("libbinder.so"))) {
                            break;
                        }
                        nativeStackTrace[i2] = null;
                    }
                }
                this.stackTrace = StackUtils.getStackTrace(javaStackTraceElements, javaStackTraceClasses, nativeStackTrace);
            }
            if (nativeBackTrace != null) {
                nativeBackTrace.dispose();
            }
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.interfaceDescriptor);
            dest.writeInt(this.code);
            ParcelUtils.writeStringArray(dest, this.stackTrace);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            this.interfaceDescriptor = source.readString();
            if (this.interfaceDescriptor == null) {
                this.interfaceDescriptor = "";
            }
            this.code = source.readInt();
            this.stackTrace = ParcelUtils.readStringArray(source);
            if (this.stackTrace == null) {
                this.stackTrace = StackUtils.emptyStack;
            }
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(FIELD_INTERFACE_DESCRIPTOR, this.interfaceDescriptor);
                json.put(FIELD_CODE, this.code);
                json.put(FIELD_STACK, JSONObject.wrap(this.stackTrace));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private static void initClasses() {
            if (sBinderProxyClass == null) {
                try {
                    sBinderProxyClass = Class.forName("android.os.BinderProxy");
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private static boolean isJavaBinderCall(StackTraceElement[] javaStackTraceElements, Class[] javaStackTraceClasses) {
            boolean z = false;
            if (javaStackTraceElements == null || javaStackTraceElements.length == 0 || javaStackTraceClasses == null || javaStackTraceClasses.length == 0) {
                return false;
            }
            if (javaStackTraceClasses[0] == sBinderProxyClass) {
                z = true;
            }
            return z;
        }
    }

    public static class BinderExecutionFields extends RootEventFields {
        private static final String FIELD_CALLING_PID = "callingPid";
        private static final String FIELD_CODE = "code";
        private static final String FIELD_INTERFACE_DESCRIPTOR = "interface";
        public int callingPid;
        public int code;
        public String interfaceDescriptor;

        public BinderExecutionFields() {
            super(false);
        }

        public void fillIn(JniParcel dataParcel) {
            super.fillIn(dataParcel);
            Object binder = dataParcel.readObject();
            this.interfaceDescriptor = dataParcel.readString();
            if (binder != null) {
                this.interfaceDescriptor = BinderSuperviser.getInterfaceDescriptor(binder);
            }
            if (this.interfaceDescriptor == null) {
                this.interfaceDescriptor = "";
            }
            this.code = dataParcel.readInt();
            this.callingPid = dataParcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.interfaceDescriptor);
            dest.writeInt(this.code);
            dest.writeInt(this.callingPid);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            this.interfaceDescriptor = source.readString();
            if (this.interfaceDescriptor == null) {
                this.interfaceDescriptor = "";
            }
            this.code = source.readInt();
            this.callingPid = source.readInt();
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(FIELD_INTERFACE_DESCRIPTOR, this.interfaceDescriptor);
                json.put(FIELD_CODE, this.code);
                json.put(FIELD_CALLING_PID, this.callingPid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class BinderStarvation extends MacroscopicEvent<BinderStarvationDetails> {
        public static final Creator<BinderStarvation> CREATOR = new Creator<BinderStarvation>() {
            public BinderStarvation createFromParcel(Parcel source) {
                BinderStarvation object = new BinderStarvation();
                object.readFromParcel(source);
                return object;
            }

            public BinderStarvation[] newArray(int size) {
                return new BinderStarvation[size];
            }
        };

        public BinderStarvation() {
            super(65540, new BinderStarvationDetails());
        }
    }

    public static class BinderStarvationDetails extends DetailFields {
        private static final String FIELD_MAX_THREADS = "maxThreads";
        public int maxThreads;

        public BinderStarvationDetails() {
            super(false);
        }

        public void fillIn(JniParcel dataParcel) {
            super.fillIn(dataParcel);
            this.maxThreads = dataParcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.maxThreads);
        }

        public void readFromParcel(Parcel source) {
            super.readFromParcel(source);
            this.maxThreads = source.readInt();
        }

        public void writeToJson(JSONObject json) {
            super.writeToJson(json);
            try {
                json.put(FIELD_MAX_THREADS, this.maxThreads);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static final class BinderWrapper {
        private long mObject;

        private final native void destroy();

        public native String getInterfaceDescriptor() throws RemoteException;

        private BinderWrapper() {
        }

        /* Access modifiers changed, original: protected */
        public void finalize() throws Throwable {
            try {
                destroy();
            } finally {
                super.finalize();
            }
        }
    }

    public static class SingleBinderCall extends MicroscopicEvent<BinderCallFields> {
        public static final Creator<SingleBinderCall> CREATOR = new Creator<SingleBinderCall>() {
            public SingleBinderCall createFromParcel(Parcel source) {
                SingleBinderCall object = new SingleBinderCall();
                object.readFromParcel(source);
                return object;
            }

            public SingleBinderCall[] newArray(int size) {
                return new SingleBinderCall[size];
            }
        };

        public SingleBinderCall() {
            super(4, new BinderCallFields());
        }
    }

    public static class SingleBinderExecution extends MicroscopicEvent<BinderExecutionFields> {
        public static final Creator<SingleBinderExecution> CREATOR = new Creator<SingleBinderExecution>() {
            public SingleBinderExecution createFromParcel(Parcel source) {
                SingleBinderExecution object = new SingleBinderExecution();
                object.readFromParcel(source);
                return object;
            }

            public SingleBinderExecution[] newArray(int size) {
                return new SingleBinderExecution[size];
            }
        };

        public SingleBinderExecution() {
            super(5, new BinderExecutionFields());
        }
    }

    private static String getInterfaceDescriptor(Object binder) {
        String interfaceDescriptor = null;
        try {
            if (binder instanceof BinderWrapper) {
                interfaceDescriptor = ((BinderWrapper) binder).getInterfaceDescriptor();
            } else if (binder instanceof IBinder) {
                interfaceDescriptor = ((IBinder) binder).getInterfaceDescriptor();
            }
        } catch (RemoteException e) {
        }
        return interfaceDescriptor == null ? "" : interfaceDescriptor;
    }
}
