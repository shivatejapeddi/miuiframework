package android.content;

import android.app.ActivityThread;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArraySet;
import android.util.Log;
import android.view.contentcapture.ContentCaptureManager.ContentCaptureClient;
import com.android.internal.annotations.VisibleForTesting;
import java.io.PrintWriter;

public final class ContentCaptureOptions implements Parcelable {
    public static final Creator<ContentCaptureOptions> CREATOR = new Creator<ContentCaptureOptions>() {
        public ContentCaptureOptions createFromParcel(Parcel parcel) {
            boolean lite = parcel.readBoolean();
            int loggingLevel = parcel.readInt();
            if (lite) {
                return new ContentCaptureOptions(loggingLevel);
            }
            return new ContentCaptureOptions(loggingLevel, parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readArraySet(null));
        }

        public ContentCaptureOptions[] newArray(int size) {
            return new ContentCaptureOptions[size];
        }
    };
    private static final String TAG = ContentCaptureOptions.class.getSimpleName();
    public final int idleFlushingFrequencyMs;
    public final boolean lite;
    public final int logHistorySize;
    public final int loggingLevel;
    public final int maxBufferSize;
    public final int textChangeFlushingFrequencyMs;
    public final ArraySet<ComponentName> whitelistedComponents;

    public ContentCaptureOptions(int loggingLevel) {
        this(true, loggingLevel, 0, 0, 0, 0, null);
    }

    public ContentCaptureOptions(int loggingLevel, int maxBufferSize, int idleFlushingFrequencyMs, int textChangeFlushingFrequencyMs, int logHistorySize, ArraySet<ComponentName> whitelistedComponents) {
        this(false, loggingLevel, maxBufferSize, idleFlushingFrequencyMs, textChangeFlushingFrequencyMs, logHistorySize, whitelistedComponents);
    }

    @VisibleForTesting
    public ContentCaptureOptions(ArraySet<ComponentName> whitelistedComponents) {
        this(2, 100, 5000, 1000, 10, whitelistedComponents);
    }

    private ContentCaptureOptions(boolean lite, int loggingLevel, int maxBufferSize, int idleFlushingFrequencyMs, int textChangeFlushingFrequencyMs, int logHistorySize, ArraySet<ComponentName> whitelistedComponents) {
        this.lite = lite;
        this.loggingLevel = loggingLevel;
        this.maxBufferSize = maxBufferSize;
        this.idleFlushingFrequencyMs = idleFlushingFrequencyMs;
        this.textChangeFlushingFrequencyMs = textChangeFlushingFrequencyMs;
        this.logHistorySize = logHistorySize;
        this.whitelistedComponents = whitelistedComponents;
    }

    public static ContentCaptureOptions forWhitelistingItself() {
        ActivityThread at = ActivityThread.currentActivityThread();
        if (at != null) {
            String packageName = at.getApplication().getPackageName();
            if ("android.contentcaptureservice.cts".equals(packageName)) {
                ContentCaptureOptions options = new ContentCaptureOptions(null);
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("forWhitelistingItself(");
                stringBuilder.append(packageName);
                stringBuilder.append("): ");
                stringBuilder.append(options);
                Log.i(str, stringBuilder.toString());
                return options;
            }
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("forWhitelistingItself(): called by ");
            stringBuilder2.append(packageName);
            Log.e(str2, stringBuilder2.toString());
            throw new SecurityException("Thou shall not pass!");
        }
        throw new IllegalStateException("No ActivityThread");
    }

    @VisibleForTesting
    public boolean isWhitelisted(Context context) {
        if (this.whitelistedComponents == null) {
            return true;
        }
        ContentCaptureClient client = context.getContentCaptureClient();
        if (client != null) {
            return this.whitelistedComponents.contains(client.contentCaptureClientGetComponentName());
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("isWhitelisted(): no ContentCaptureClient on ");
        stringBuilder.append(context);
        Log.w(str, stringBuilder.toString());
        return false;
    }

    public String toString() {
        StringBuilder stringBuilder;
        if (this.lite) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("ContentCaptureOptions [loggingLevel=");
            stringBuilder.append(this.loggingLevel);
            stringBuilder.append(" (lite)]");
            return stringBuilder.toString();
        }
        stringBuilder = new StringBuilder("ContentCaptureOptions [");
        stringBuilder.append("loggingLevel=");
        stringBuilder.append(this.loggingLevel);
        stringBuilder.append(", maxBufferSize=");
        stringBuilder.append(this.maxBufferSize);
        stringBuilder.append(", idleFlushingFrequencyMs=");
        stringBuilder.append(this.idleFlushingFrequencyMs);
        stringBuilder.append(", textChangeFlushingFrequencyMs=");
        stringBuilder.append(this.textChangeFlushingFrequencyMs);
        stringBuilder.append(", logHistorySize=");
        stringBuilder.append(this.logHistorySize);
        if (this.whitelistedComponents != null) {
            stringBuilder.append(", whitelisted=");
            stringBuilder.append(this.whitelistedComponents);
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public void dumpShort(PrintWriter pw) {
        pw.print("logLvl=");
        pw.print(this.loggingLevel);
        if (this.lite) {
            pw.print(", lite");
            return;
        }
        pw.print(", bufferSize=");
        pw.print(this.maxBufferSize);
        pw.print(", idle=");
        pw.print(this.idleFlushingFrequencyMs);
        pw.print(", textIdle=");
        pw.print(this.textChangeFlushingFrequencyMs);
        pw.print(", logSize=");
        pw.print(this.logHistorySize);
        if (this.whitelistedComponents != null) {
            pw.print(", whitelisted=");
            pw.print(this.whitelistedComponents);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeBoolean(this.lite);
        parcel.writeInt(this.loggingLevel);
        if (!this.lite) {
            parcel.writeInt(this.maxBufferSize);
            parcel.writeInt(this.idleFlushingFrequencyMs);
            parcel.writeInt(this.textChangeFlushingFrequencyMs);
            parcel.writeInt(this.logHistorySize);
            parcel.writeArraySet(this.whitelistedComponents);
        }
    }
}
