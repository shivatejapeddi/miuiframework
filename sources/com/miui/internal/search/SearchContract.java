package com.miui.internal.search;

public class SearchContract {
    public static final int COLUMN_RAW_EXTRAS = 9;
    public static final int COLUMN_RAW_ICON_RESID = 4;
    public static final int COLUMN_RAW_INTENT_ACTION = 5;
    public static final int COLUMN_RAW_INTENT_TARGET_CLASS = 7;
    public static final int COLUMN_RAW_INTENT_TARGET_PACKAGE = 6;
    public static final int COLUMN_RAW_KEYWORDS = 3;
    public static final int COLUMN_RAW_OTHER = 10;
    public static final int COLUMN_RAW_SUMMARY_OFF = 2;
    public static final int COLUMN_RAW_SUMMARY_ON = 1;
    public static final int COLUMN_RAW_TITLE = 0;
    public static final int COLUMN_RAW_URI_STRING = 8;
    public static final String[] SEARCH_RESULT_COLUMNS = new String[]{"title", "summaryOn", "summaryOff", "keywords", "iconResId", "intentAction", "intentTargetPackage", "intentTargetClass", SearchResultColumn.COLUMN_URI_STRING, SearchResultColumn.COLUMN_EXTRAS, "other"};
    public static final String SETTINGS_SEARCH_PROVIDER_INTERFACE = "miui.intent.action.SETTINGS_SEARCH_PROVIDER";

    public static final class SearchResultColumn {
        public static final String COLUMN_EXTRAS = "extras";
        public static final String COLUMN_ICON_RESID = "iconResId";
        public static final String COLUMN_INTENT_ACTION = "intentAction";
        public static final String COLUMN_INTENT_TARGET_CLASS = "intentTargetClass";
        public static final String COLUMN_INTENT_TARGET_PACKAGE = "intentTargetPackage";
        public static final String COLUMN_KEYWORDS = "keywords";
        public static final String COLUMN_OTHER = "other";
        public static final String COLUMN_SUMMARY_OFF = "summaryOff";
        public static final String COLUMN_SUMMARY_ON = "summaryOn";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_URI_STRING = "uriString";

        private SearchResultColumn() {
        }
    }
}
