package android.view.textclassifier;

import android.content.Context;
import android.metrics.LogMaker;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.util.Preconditions;
import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

public final class SelectionSessionLogger {
    static final String CLASSIFIER_ID = "androidtc";
    private static final int ENTITY_TYPE = 1254;
    private static final int EVENT_END = 1251;
    private static final int EVENT_START = 1250;
    private static final int INDEX = 1120;
    private static final String LOG_TAG = "SelectionSessionLogger";
    private static final int MODEL_NAME = 1256;
    private static final int PREV_EVENT_DELTA = 1118;
    private static final int SESSION_ID = 1119;
    private static final int SMART_END = 1253;
    private static final int SMART_START = 1252;
    private static final int START_EVENT_DELTA = 1117;
    private static final String UNKNOWN = "unknown";
    private static final int WIDGET_TYPE = 1255;
    private static final int WIDGET_VERSION = 1262;
    private static final String ZERO = "0";
    private final MetricsLogger mMetricsLogger;

    @VisibleForTesting
    public static final class SignatureParser {
        static String createSignature(String classifierId, String modelName, int hash) {
            return String.format(Locale.US, "%s|%s|%d", new Object[]{classifierId, modelName, Integer.valueOf(hash)});
        }

        static String getClassifierId(String signature) {
            String str = "";
            if (signature == null) {
                return str;
            }
            int end = signature.indexOf("|");
            if (end >= 0) {
                return signature.substring(0, end);
            }
            return str;
        }

        static String getModelName(String signature) {
            String str = "";
            if (signature == null) {
                return str;
            }
            int end = "|";
            int start = signature.indexOf(end) + 1;
            end = signature.indexOf(end, start);
            if (start < 1 || end < start) {
                return str;
            }
            return signature.substring(start, end);
        }

        static int getHash(String signature) {
            if (signature == null) {
                return 0;
            }
            int index2 = "|";
            index2 = signature.indexOf(index2, signature.indexOf(index2));
            if (index2 > 0) {
                return Integer.parseInt(signature.substring(index2));
            }
            return 0;
        }
    }

    public SelectionSessionLogger() {
        this.mMetricsLogger = new MetricsLogger();
    }

    @VisibleForTesting
    public SelectionSessionLogger(MetricsLogger metricsLogger) {
        this.mMetricsLogger = (MetricsLogger) Preconditions.checkNotNull(metricsLogger);
    }

    public void writeEvent(SelectionEvent event) {
        Preconditions.checkNotNull(event);
        LogMaker log = new LogMaker(1100).setType(getLogType(event)).setSubtype(getLogSubType(event)).setPackageName(event.getPackageName()).addTaggedData(1117, Long.valueOf(event.getDurationSinceSessionStart())).addTaggedData(1118, Long.valueOf(event.getDurationSincePreviousEvent())).addTaggedData(1120, Integer.valueOf(event.getEventIndex())).addTaggedData(1255, event.getWidgetType()).addTaggedData(1262, event.getWidgetVersion()).addTaggedData(1254, event.getEntityType()).addTaggedData(1250, Integer.valueOf(event.getStart())).addTaggedData(1251, Integer.valueOf(event.getEnd()));
        if (isPlatformLocalTextClassifierSmartSelection(event.getResultId())) {
            log.addTaggedData(1256, SignatureParser.getModelName(event.getResultId())).addTaggedData(1252, Integer.valueOf(event.getSmartStart())).addTaggedData(1253, Integer.valueOf(event.getSmartEnd()));
        }
        if (event.getSessionId() != null) {
            log.addTaggedData(1119, event.getSessionId().flattenToString());
        }
        this.mMetricsLogger.write(log);
        debugLog(log);
    }

    private static int getLogType(SelectionEvent event) {
        int eventType = event.getEventType();
        if (eventType == 1) {
            return 1101;
        }
        if (eventType == 2) {
            return 1102;
        }
        if (eventType == 3) {
            return MetricsEvent.ACTION_TEXT_SELECTION_SMART_SINGLE;
        }
        if (eventType == 4) {
            return MetricsEvent.ACTION_TEXT_SELECTION_SMART_MULTI;
        }
        if (eventType == 5) {
            return MetricsEvent.ACTION_TEXT_SELECTION_AUTO;
        }
        if (eventType == 200) {
            return MetricsEvent.ACTION_TEXT_SELECTION_SELECT_ALL;
        }
        if (eventType == 201) {
            return MetricsEvent.ACTION_TEXT_SELECTION_RESET;
        }
        switch (eventType) {
            case 100:
                return MetricsEvent.ACTION_TEXT_SELECTION_OVERTYPE;
            case 101:
                return MetricsEvent.ACTION_TEXT_SELECTION_COPY;
            case 102:
                return MetricsEvent.ACTION_TEXT_SELECTION_PASTE;
            case 103:
                return MetricsEvent.ACTION_TEXT_SELECTION_CUT;
            case 104:
                return MetricsEvent.ACTION_TEXT_SELECTION_SHARE;
            case 105:
                return MetricsEvent.ACTION_TEXT_SELECTION_SMART_SHARE;
            case 106:
                return MetricsEvent.ACTION_TEXT_SELECTION_DRAG;
            case 107:
                return MetricsEvent.ACTION_TEXT_SELECTION_ABANDON;
            case 108:
                return MetricsEvent.ACTION_TEXT_SELECTION_OTHER;
            default:
                return 0;
        }
    }

    private static int getLogSubType(SelectionEvent event) {
        int invocationMethod = event.getInvocationMethod();
        if (invocationMethod == 1) {
            return 1;
        }
        if (invocationMethod != 2) {
            return 0;
        }
        return 2;
    }

    private static String getLogTypeString(int logType) {
        switch (logType) {
            case 1101:
                return "SELECTION_STARTED";
            case 1102:
                return "SELECTION_MODIFIED";
            case MetricsEvent.ACTION_TEXT_SELECTION_SELECT_ALL /*1103*/:
                return "SELECT_ALL";
            case MetricsEvent.ACTION_TEXT_SELECTION_RESET /*1104*/:
                return "RESET";
            case MetricsEvent.ACTION_TEXT_SELECTION_SMART_SINGLE /*1105*/:
                return "SMART_SELECTION_SINGLE";
            case MetricsEvent.ACTION_TEXT_SELECTION_SMART_MULTI /*1106*/:
                return "SMART_SELECTION_MULTI";
            case MetricsEvent.ACTION_TEXT_SELECTION_AUTO /*1107*/:
                return "AUTO_SELECTION";
            case MetricsEvent.ACTION_TEXT_SELECTION_OVERTYPE /*1108*/:
                return "OVERTYPE";
            case MetricsEvent.ACTION_TEXT_SELECTION_COPY /*1109*/:
                return "COPY";
            case MetricsEvent.ACTION_TEXT_SELECTION_PASTE /*1110*/:
                return "PASTE";
            case MetricsEvent.ACTION_TEXT_SELECTION_CUT /*1111*/:
                return "CUT";
            case MetricsEvent.ACTION_TEXT_SELECTION_SHARE /*1112*/:
                return "SHARE";
            case MetricsEvent.ACTION_TEXT_SELECTION_SMART_SHARE /*1113*/:
                return "SMART_SHARE";
            case MetricsEvent.ACTION_TEXT_SELECTION_DRAG /*1114*/:
                return "DRAG";
            case MetricsEvent.ACTION_TEXT_SELECTION_ABANDON /*1115*/:
                return "ABANDON";
            case MetricsEvent.ACTION_TEXT_SELECTION_OTHER /*1116*/:
                return "OTHER";
            default:
                return "unknown";
        }
    }

    private static String getLogSubTypeString(int logSubType) {
        if (logSubType == 1) {
            return "MANUAL";
        }
        if (logSubType != 2) {
            return "unknown";
        }
        return "LINK";
    }

    static boolean isPlatformLocalTextClassifierSmartSelection(String signature) {
        return "androidtc".equals(SignatureParser.getClassifierId(signature));
    }

    private static void debugLog(LogMaker log) {
        LogMaker logMaker = log;
        if (Log.ENABLE_FULL_LOGGING) {
            String widget;
            String entity = "unknown";
            String widgetType = Objects.toString(logMaker.getTaggedData(1255), entity);
            String sessionId = "";
            String widgetVersion = Objects.toString(logMaker.getTaggedData(1262), sessionId);
            String str = "-";
            if (widgetVersion.isEmpty()) {
                widget = widgetType;
            } else {
                widget = new StringBuilder();
                widget.append(widgetType);
                widget.append(str);
                widget.append(widgetVersion);
                widget = widget.toString();
            }
            String str2 = "0";
            int index = Integer.parseInt(Objects.toString(logMaker.getTaggedData(1120), str2));
            int type = log.getType();
            String str3 = LOG_TAG;
            if (type == 1101) {
                sessionId = Objects.toString(logMaker.getTaggedData(1119), sessionId);
                sessionId = sessionId.substring(sessionId.lastIndexOf(str) + 1);
                Log.d(str3, String.format("New selection session: %s (%s)", new Object[]{widget, sessionId}));
            }
            sessionId = Objects.toString(logMaker.getTaggedData(1256), entity);
            entity = Objects.toString(logMaker.getTaggedData(1254), entity);
            str = getLogTypeString(log.getType());
            String subType = getLogSubTypeString(log.getSubtype());
            int smartStart = Integer.parseInt(Objects.toString(logMaker.getTaggedData(1252), str2));
            int smartEnd = Integer.parseInt(Objects.toString(logMaker.getTaggedData(1253), str2));
            int eventStart = Integer.parseInt(Objects.toString(logMaker.getTaggedData(1250), str2));
            int eventEnd = Integer.parseInt(Objects.toString(logMaker.getTaggedData(1251), str2));
            Log.v(str3, String.format(Locale.US, "%2d: %s/%s/%s, range=%d,%d - smart_range=%d,%d (%s/%s)", new Object[]{Integer.valueOf(index), str, subType, entity, Integer.valueOf(eventStart), Integer.valueOf(eventEnd), Integer.valueOf(smartStart), Integer.valueOf(smartEnd), widget, sessionId}));
        }
    }

    public static BreakIterator getTokenIterator(Locale locale) {
        return BreakIterator.getWordInstance((Locale) Preconditions.checkNotNull(locale));
    }

    public static String createId(String text, int start, int end, Context context, int modelVersion, List<Locale> locales) {
        Preconditions.checkNotNull(text);
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(locales);
        StringJoiner localesJoiner = new StringJoiner(",");
        for (Locale locale : locales) {
            localesJoiner.add(locale.toLanguageTag());
        }
        return SignatureParser.createSignature("androidtc", String.format(Locale.US, "%s_v%d", new Object[]{localesJoiner.toString(), Integer.valueOf(modelVersion)}), Objects.hash(new Object[]{text, Integer.valueOf(start), Integer.valueOf(end), context.getPackageName()}));
    }
}
