package com.miui.internal.search;

public interface Function {
    public static final String CATEGORY = "category";
    public static final String CATEGORY_ORIGIN = "category_origin";
    public static final String CLASS = "class";
    public static final String FEATURE = "feature";
    public static final String FRAGMENT = "fragment";
    public static final String[] FUNCTIONS = new String[]{"package", RESOURCE, "title", "category", "path", "keywords", "summary", "icon", IS_CHECKBOX, "intent", "status", "level"};
    public static final String ICON = "icon";
    public static final String INTENT = "intent";
    public static final String IS_CHECKBOX = "is_checkbox";
    public static final String IS_OLDMAN = "is_oldman";
    public static final String KEYWORDS = "keywords";
    public static final String LEVEL = "level";
    public static final String PACKAGE = "package";
    public static final String PARENT = "parent";
    public static final String PATH = "path";
    public static final String RESOURCE = "resource";
    public static final String SECOND_SPACE = "second_space";
    public static final String SON = "son";
    public static final String STATUS = "status";
    public static final String SUMMARY = "summary";
    public static final String TEMPORARY = "temporary";
    public static final String TITLE = "title";

    public interface Intent {
        public static final String ACTION = "intent_action";
        public static final String CLASS = "intent_class";
        public static final String DATA = "intent_data";
        public static final String EXTRA = "intent_extra";
        public static final String PACKAGE = "intent_package";
    }
}
