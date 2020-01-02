package android.service.autofill.augmented;

import android.content.ComponentName;
import android.metrics.LogMaker;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import java.io.PrintStream;

public final class Helper {
    private static final MetricsLogger sMetricsLogger = new MetricsLogger();

    public static void logResponse(int type, String servicePackageName, ComponentName componentName, int mSessionId, long durationMs) {
        LogMaker log = new LogMaker((int) MetricsEvent.AUTOFILL_AUGMENTED_RESPONSE).setType(type).setComponentName(componentName).addTaggedData(MetricsEvent.FIELD_AUTOFILL_SESSION_ID, Integer.valueOf(mSessionId)).addTaggedData(MetricsEvent.FIELD_AUTOFILL_SERVICE, servicePackageName).addTaggedData(MetricsEvent.FIELD_AUTOFILL_DURATION, Long.valueOf(durationMs));
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LOGGGO: ");
        stringBuilder.append(log.getEntries());
        printStream.println(stringBuilder.toString());
        sMetricsLogger.write(log);
    }

    private Helper() {
        throw new UnsupportedOperationException("contains only static methods");
    }
}
