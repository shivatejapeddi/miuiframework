package android.media.audiofx;

import android.app.ActivityThread;
import android.util.Log;
import java.util.UUID;

public class SourceDefaultEffect extends DefaultEffect {
    private static final String TAG = "SourceDefaultEffect-JAVA";

    private final native void native_release(int i);

    private final native int native_setup(String str, String str2, int i, int i2, String str3, int[] iArr);

    static {
        System.loadLibrary("audioeffect_jni");
    }

    public SourceDefaultEffect(UUID type, UUID uuid, int priority, int source) {
        int[] id = new int[1];
        int initResult = native_setup(type.toString(), uuid.toString(), priority, source, ActivityThread.currentOpPackageName(), id);
        if (initResult != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error code ");
            stringBuilder.append(initResult);
            stringBuilder.append(" when initializing SourceDefaultEffect");
            Log.e(TAG, stringBuilder.toString());
            if (initResult == -5) {
                throw new UnsupportedOperationException("Effect library not loaded");
            } else if (initResult != -4) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Cannot initialize effect engine for type: ");
                stringBuilder2.append(type);
                stringBuilder2.append(" Error: ");
                stringBuilder2.append(initResult);
                throw new RuntimeException(stringBuilder2.toString());
            } else {
                throw new IllegalArgumentException("Source, type uuid, or implementation uuid not supported.");
            }
        }
        this.mId = id[0];
    }

    public void release() {
        native_release(this.mId);
    }

    /* Access modifiers changed, original: protected */
    public void finalize() {
        release();
    }
}
