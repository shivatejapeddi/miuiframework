package android.view.textclassifier.logging;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.metrics.LogMaker;
import android.util.Log;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextClassifier;
import android.view.textclassifier.TextSelection;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;
import java.util.UUID;

public final class SmartSelectionEventTracker {
    private static final String CUSTOM_EDITTEXT = "customedit";
    private static final String CUSTOM_TEXTVIEW = "customview";
    private static final String CUSTOM_UNSELECTABLE_TEXTVIEW = "nosel-customview";
    private static final boolean DEBUG_LOG_ENABLED = true;
    private static final String EDITTEXT = "edittext";
    private static final String EDIT_WEBVIEW = "edit-webview";
    private static final int ENTITY_TYPE = 1254;
    private static final int EVENT_END = 1251;
    private static final int EVENT_START = 1250;
    private static final int INDEX = 1120;
    private static final String LOG_TAG = "SmartSelectEventTracker";
    private static final int MODEL_NAME = 1256;
    private static final int PREV_EVENT_DELTA = 1118;
    private static final int SESSION_ID = 1119;
    private static final int SMART_END = 1253;
    private static final int SMART_START = 1252;
    private static final int START_EVENT_DELTA = 1117;
    private static final String TEXTVIEW = "textview";
    private static final String UNKNOWN = "unknown";
    private static final String UNSELECTABLE_TEXTVIEW = "nosel-textview";
    private static final String WEBVIEW = "webview";
    private static final int WIDGET_TYPE = 1255;
    private static final int WIDGET_VERSION = 1262;
    private static final String ZERO = "0";
    private final Context mContext;
    private int mIndex;
    private long mLastEventTime;
    private final MetricsLogger mMetricsLogger;
    private String mModelName;
    private int mOrigStart;
    private final int[] mPrevIndices;
    private String mSessionId;
    private long mSessionStartTime;
    private final int[] mSmartIndices;
    private boolean mSmartSelectionTriggered;
    private final int mWidgetType;
    private final String mWidgetVersion;

    public static final class SelectionEvent {
        private static final String NO_VERSION_TAG = "";
        public static final int OUT_OF_BOUNDS = Integer.MAX_VALUE;
        public static final int OUT_OF_BOUNDS_NEGATIVE = Integer.MIN_VALUE;
        private final int mEnd;
        private final String mEntityType;
        private int mEventType;
        private final int mStart;
        private final String mVersionTag;

        @Retention(RetentionPolicy.SOURCE)
        public @interface ActionType {
            public static final int ABANDON = 107;
            public static final int COPY = 101;
            public static final int CUT = 103;
            public static final int DRAG = 106;
            public static final int OTHER = 108;
            public static final int OVERTYPE = 100;
            public static final int PASTE = 102;
            public static final int RESET = 201;
            public static final int SELECT_ALL = 200;
            public static final int SHARE = 104;
            public static final int SMART_SHARE = 105;
        }

        @Retention(RetentionPolicy.SOURCE)
        private @interface EventType {
            public static final int AUTO_SELECTION = 5;
            public static final int SELECTION_MODIFIED = 2;
            public static final int SELECTION_STARTED = 1;
            public static final int SMART_SELECTION_MULTI = 4;
            public static final int SMART_SELECTION_SINGLE = 3;
        }

        private SelectionEvent(int start, int end, int eventType, String entityType, String versionTag) {
            Preconditions.checkArgument(end >= start, "end cannot be less than start");
            this.mStart = start;
            this.mEnd = end;
            this.mEventType = eventType;
            this.mEntityType = (String) Preconditions.checkNotNull(entityType);
            this.mVersionTag = (String) Preconditions.checkNotNull(versionTag);
        }

        @UnsupportedAppUsage
        public static SelectionEvent selectionStarted(int start) {
            return new SelectionEvent(start, start + 1, 1, "", "");
        }

        @UnsupportedAppUsage
        public static SelectionEvent selectionModified(int start, int end) {
            return new SelectionEvent(start, end, 2, "", "");
        }

        @UnsupportedAppUsage
        public static SelectionEvent selectionModified(int start, int end, TextClassification classification) {
            String entityType;
            if (classification.getEntityCount() > 0) {
                entityType = classification.getEntity(0);
            } else {
                entityType = "";
            }
            return new SelectionEvent(start, end, 2, entityType, getVersionInfo(classification.getId()));
        }

        @UnsupportedAppUsage
        public static SelectionEvent selectionModified(int start, int end, TextSelection selection) {
            int eventType;
            String entityType;
            if (!getSourceClassifier(selection.getId()).equals(TextClassifier.DEFAULT_LOG_TAG)) {
                eventType = 5;
            } else if (end - start > 1) {
                eventType = 4;
            } else {
                eventType = 3;
            }
            if (selection.getEntityCount() > 0) {
                entityType = selection.getEntity(0);
            } else {
                entityType = "";
            }
            return new SelectionEvent(start, end, eventType, entityType, getVersionInfo(selection.getId()));
        }

        @UnsupportedAppUsage
        public static SelectionEvent selectionAction(int start, int end, int actionType) {
            return new SelectionEvent(start, end, actionType, "", "");
        }

        @UnsupportedAppUsage
        public static SelectionEvent selectionAction(int start, int end, int actionType, TextClassification classification) {
            String entityType;
            if (classification.getEntityCount() > 0) {
                entityType = classification.getEntity(0);
            } else {
                entityType = "";
            }
            return new SelectionEvent(start, end, actionType, entityType, getVersionInfo(classification.getId()));
        }

        private static String getVersionInfo(String signature) {
            int end = "|";
            int start = signature.indexOf(end);
            end = signature.indexOf(end, start);
            if (start < 0 || end < start) {
                return "";
            }
            return signature.substring(start, end);
        }

        private static String getSourceClassifier(String signature) {
            int end = signature.indexOf("|");
            if (end >= 0) {
                return signature.substring(0, end);
            }
            return "";
        }

        private boolean isTerminal() {
            switch (this.mEventType) {
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107:
                case 108:
                    return true;
                default:
                    return false;
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WidgetType {
        public static final int CUSTOM_EDITTEXT = 7;
        public static final int CUSTOM_TEXTVIEW = 6;
        public static final int CUSTOM_UNSELECTABLE_TEXTVIEW = 8;
        public static final int EDITTEXT = 3;
        public static final int EDIT_WEBVIEW = 4;
        public static final int TEXTVIEW = 1;
        public static final int UNSELECTABLE_TEXTVIEW = 5;
        public static final int UNSPECIFIED = 0;
        public static final int WEBVIEW = 2;
    }

    @UnsupportedAppUsage
    public SmartSelectionEventTracker(Context context, int widgetType) {
        this.mMetricsLogger = new MetricsLogger();
        this.mSmartIndices = new int[2];
        this.mPrevIndices = new int[2];
        this.mWidgetType = widgetType;
        this.mWidgetVersion = null;
        this.mContext = (Context) Preconditions.checkNotNull(context);
    }

    public SmartSelectionEventTracker(Context context, int widgetType, String widgetVersion) {
        this.mMetricsLogger = new MetricsLogger();
        this.mSmartIndices = new int[2];
        this.mPrevIndices = new int[2];
        this.mWidgetType = widgetType;
        this.mWidgetVersion = widgetVersion;
        this.mContext = (Context) Preconditions.checkNotNull(context);
    }

    /* JADX WARNING: Missing block: B:15:0x002b, code skipped:
            if (r0 != 5) goto L_0x007b;
     */
    @android.annotation.UnsupportedAppUsage
    public void logEvent(android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent r7) {
        /*
        r6 = this;
        com.android.internal.util.Preconditions.checkNotNull(r7);
        r0 = r7.mEventType;
        r1 = 1;
        if (r0 == r1) goto L_0x0016;
    L_0x000a:
        r0 = r6.mSessionId;
        if (r0 != 0) goto L_0x0016;
    L_0x000e:
        r0 = "SmartSelectEventTracker";
        r1 = "Selection session not yet started. Ignoring event";
        android.util.Log.d(r0, r1);
        return;
    L_0x0016:
        r2 = java.lang.System.currentTimeMillis();
        r0 = r7.mEventType;
        r4 = 0;
        if (r0 == r1) goto L_0x005c;
    L_0x0021:
        r5 = 2;
        if (r0 == r5) goto L_0x0047;
    L_0x0024:
        r5 = 3;
        if (r0 == r5) goto L_0x002e;
    L_0x0027:
        r5 = 4;
        if (r0 == r5) goto L_0x002e;
    L_0x002a:
        r5 = 5;
        if (r0 == r5) goto L_0x0047;
    L_0x002d:
        goto L_0x007b;
    L_0x002e:
        r6.mSmartSelectionTriggered = r1;
        r0 = r6.getModelName(r7);
        r6.mModelName = r0;
        r0 = r6.mSmartIndices;
        r5 = r7.mStart;
        r0[r4] = r5;
        r0 = r6.mSmartIndices;
        r4 = r7.mEnd;
        r0[r1] = r4;
        goto L_0x007b;
    L_0x0047:
        r0 = r6.mPrevIndices;
        r0 = r0[r4];
        r4 = r7.mStart;
        if (r0 != r4) goto L_0x007b;
    L_0x0051:
        r0 = r6.mPrevIndices;
        r0 = r0[r1];
        r1 = r7.mEnd;
        if (r0 != r1) goto L_0x007b;
    L_0x005b:
        return;
    L_0x005c:
        r0 = r6.startNewSession();
        r6.mSessionId = r0;
        r0 = r7.mEnd;
        r5 = r7.mStart;
        r5 = r5 + r1;
        if (r0 != r5) goto L_0x006e;
    L_0x006d:
        goto L_0x006f;
    L_0x006e:
        r1 = r4;
    L_0x006f:
        com.android.internal.util.Preconditions.checkArgument(r1);
        r0 = r7.mStart;
        r6.mOrigStart = r0;
        r6.mSessionStartTime = r2;
    L_0x007b:
        r6.writeEvent(r7, r2);
        r0 = r7.isTerminal();
        if (r0 == 0) goto L_0x0087;
    L_0x0084:
        r6.endSession();
    L_0x0087:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.textclassifier.logging.SmartSelectionEventTracker.logEvent(android.view.textclassifier.logging.SmartSelectionEventTracker$SelectionEvent):void");
    }

    private void writeEvent(SelectionEvent event, long now) {
        long j = this.mLastEventTime;
        long j2 = 0;
        if (j != 0) {
            j2 = now - j;
        }
        LogMaker log = new LogMaker(1100).setType(getLogType(event)).setSubtype(1).setPackageName(this.mContext.getPackageName()).addTaggedData(1117, Long.valueOf(now - this.mSessionStartTime)).addTaggedData(1118, Long.valueOf(j2)).addTaggedData(1120, Integer.valueOf(this.mIndex)).addTaggedData(1255, getWidgetTypeName()).addTaggedData(1262, this.mWidgetVersion).addTaggedData(1256, this.mModelName).addTaggedData(1254, event.mEntityType).addTaggedData(1252, Integer.valueOf(getSmartRangeDelta(this.mSmartIndices[0]))).addTaggedData(1253, Integer.valueOf(getSmartRangeDelta(this.mSmartIndices[1]))).addTaggedData(1250, Integer.valueOf(getRangeDelta(event.mStart))).addTaggedData(1251, Integer.valueOf(getRangeDelta(event.mEnd))).addTaggedData(1119, this.mSessionId);
        this.mMetricsLogger.write(log);
        debugLog(log);
        this.mLastEventTime = now;
        this.mPrevIndices[0] = event.mStart;
        this.mPrevIndices[1] = event.mEnd;
        this.mIndex++;
    }

    private String startNewSession() {
        endSession();
        this.mSessionId = createSessionId();
        return this.mSessionId;
    }

    private void endSession() {
        this.mOrigStart = 0;
        int[] iArr = this.mSmartIndices;
        iArr[1] = 0;
        iArr[0] = 0;
        iArr = this.mPrevIndices;
        iArr[1] = 0;
        iArr[0] = 0;
        this.mIndex = 0;
        this.mSessionStartTime = 0;
        this.mLastEventTime = 0;
        this.mSmartSelectionTriggered = false;
        this.mModelName = getModelName(null);
        this.mSessionId = null;
    }

    private static int getLogType(SelectionEvent event) {
        int access$000 = event.mEventType;
        if (access$000 == 1) {
            return 1101;
        }
        if (access$000 == 2) {
            return 1102;
        }
        if (access$000 == 3) {
            return MetricsEvent.ACTION_TEXT_SELECTION_SMART_SINGLE;
        }
        if (access$000 == 4) {
            return MetricsEvent.ACTION_TEXT_SELECTION_SMART_MULTI;
        }
        if (access$000 == 5) {
            return MetricsEvent.ACTION_TEXT_SELECTION_AUTO;
        }
        if (access$000 == 200) {
            return MetricsEvent.ACTION_TEXT_SELECTION_SELECT_ALL;
        }
        if (access$000 == 201) {
            return MetricsEvent.ACTION_TEXT_SELECTION_RESET;
        }
        switch (access$000) {
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

    private int getRangeDelta(int offset) {
        return offset - this.mOrigStart;
    }

    private int getSmartRangeDelta(int offset) {
        return this.mSmartSelectionTriggered ? getRangeDelta(offset) : 0;
    }

    private String getWidgetTypeName() {
        switch (this.mWidgetType) {
            case 1:
                return "textview";
            case 2:
                return "webview";
            case 3:
                return "edittext";
            case 4:
                return "edit-webview";
            case 5:
                return "nosel-textview";
            case 6:
                return "customview";
            case 7:
                return "customedit";
            case 8:
                return "nosel-customview";
            default:
                return "unknown";
        }
    }

    private String getModelName(SelectionEvent event) {
        String str = "";
        if (event == null) {
            return str;
        }
        return Objects.toString(event.mVersionTag, str);
    }

    private static String createSessionId() {
        return UUID.randomUUID().toString();
    }

    private static void debugLog(LogMaker log) {
        String widget;
        LogMaker logMaker = log;
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
        type = Integer.parseInt(Objects.toString(logMaker.getTaggedData(1252), str2));
        int smartEnd = Integer.parseInt(Objects.toString(logMaker.getTaggedData(1253), str2));
        int eventStart = Integer.parseInt(Objects.toString(logMaker.getTaggedData(1250), str2));
        int eventEnd = Integer.parseInt(Objects.toString(logMaker.getTaggedData(1251), str2));
        Log.d(str3, String.format("%2d: %s/%s, range=%d,%d - smart_range=%d,%d (%s/%s)", new Object[]{Integer.valueOf(index), str, entity, Integer.valueOf(eventStart), Integer.valueOf(eventEnd), Integer.valueOf(type), Integer.valueOf(smartEnd), widget, sessionId}));
    }
}
