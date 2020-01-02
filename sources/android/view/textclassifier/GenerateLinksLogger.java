package android.view.textclassifier;

import android.metrics.LogMaker;
import android.util.ArrayMap;
import android.view.textclassifier.TextLinks.TextLink;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.Preconditions;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@VisibleForTesting(visibility = Visibility.PACKAGE)
public final class GenerateLinksLogger {
    private static final String LOG_TAG = "GenerateLinksLogger";
    private static final String ZERO = "0";
    private final MetricsLogger mMetricsLogger;
    private final Random mRng;
    private final int mSampleRate;

    private static final class LinkifyStats {
        int mNumLinks;
        int mNumLinksTextLength;

        private LinkifyStats() {
        }

        /* Access modifiers changed, original: 0000 */
        public void countLink(TextLink link) {
            this.mNumLinks++;
            this.mNumLinksTextLength += link.getEnd() - link.getStart();
        }
    }

    public GenerateLinksLogger(int sampleRate) {
        this.mSampleRate = sampleRate;
        this.mRng = new Random(System.nanoTime());
        this.mMetricsLogger = new MetricsLogger();
    }

    @VisibleForTesting
    public GenerateLinksLogger(int sampleRate, MetricsLogger metricsLogger) {
        this.mSampleRate = sampleRate;
        this.mRng = new Random(System.nanoTime());
        this.mMetricsLogger = metricsLogger;
    }

    public void logGenerateLinks(CharSequence text, TextLinks links, String callingPackageName, long latencyMs) {
        Preconditions.checkNotNull(text);
        Preconditions.checkNotNull(links);
        Preconditions.checkNotNull(callingPackageName);
        if (shouldLog()) {
            LinkifyStats totalStats = new LinkifyStats();
            Map<String, LinkifyStats> perEntityTypeStats = new ArrayMap();
            for (TextLink link : links.getLinks()) {
                if (link.getEntityCount() != 0) {
                    String entityType = link.getEntity(null);
                    if (!(entityType == null || "other".equals(entityType))) {
                        if (!"".equals(entityType)) {
                            totalStats.countLink(link);
                            ((LinkifyStats) perEntityTypeStats.computeIfAbsent(entityType, -$$Lambda$GenerateLinksLogger$vmbT_h7MLlbrIm0lJJwA-eHQhXk.INSTANCE)).countLink(link);
                        }
                    }
                }
            }
            String callId = UUID.randomUUID().toString();
            writeStats(callId, callingPackageName, null, totalStats, text, latencyMs);
            for (Entry<String, LinkifyStats> entry : perEntityTypeStats.entrySet()) {
                writeStats(callId, callingPackageName, (String) entry.getKey(), (LinkifyStats) entry.getValue(), text, latencyMs);
            }
        }
    }

    private boolean shouldLog() {
        int i = this.mSampleRate;
        boolean z = true;
        if (i <= 1) {
            return true;
        }
        if (this.mRng.nextInt(i) != 0) {
            z = false;
        }
        return z;
    }

    private void writeStats(String callId, String callingPackageName, String entityType, LinkifyStats stats, CharSequence text, long latencyMs) {
        LogMaker log = new LogMaker(1313).setPackageName(callingPackageName).addTaggedData(1319, callId).addTaggedData(1316, Integer.valueOf(stats.mNumLinks)).addTaggedData(1317, Integer.valueOf(stats.mNumLinksTextLength)).addTaggedData(1315, Integer.valueOf(text.length())).addTaggedData(1314, Long.valueOf(latencyMs));
        if (entityType != null) {
            log.addTaggedData(1318, entityType);
        }
        this.mMetricsLogger.write(log);
        debugLog(log);
    }

    private static void debugLog(LogMaker log) {
        if (Log.ENABLE_FULL_LOGGING) {
            String callId = Objects.toString(log.getTaggedData(1319), "");
            String entityType = Objects.toString(log.getTaggedData(1318), "ANY_ENTITY");
            String str = "0";
            int numLinks = Integer.parseInt(Objects.toString(log.getTaggedData(1316), str));
            int linkLength = Integer.parseInt(Objects.toString(log.getTaggedData(1317), str));
            int textLength = Integer.parseInt(Objects.toString(log.getTaggedData(1315), str));
            int latencyMs = Integer.parseInt(Objects.toString(log.getTaggedData(1314), str));
            Log.v(LOG_TAG, String.format(Locale.US, "%s:%s %d links (%d/%d chars) %dms %s", new Object[]{callId, entityType, Integer.valueOf(numLinks), Integer.valueOf(linkLength), Integer.valueOf(textLength), Integer.valueOf(latencyMs), log.getPackageName()}));
        }
    }
}
