package com.miui.internal.search;

import android.util.ArrayMap;

public class CloudKeywordsHolder {
    private ArrayMap<String, String> keywordsMap;
    private String locale;
    private String version;

    public static final class Builder {
        private ArrayMap<String, String> keywordsMap = new ArrayMap();
        private String locale;
        private String version;

        public Builder setVersion(String val) {
            this.version = val;
            return this;
        }

        public Builder setLocale(String val) {
            this.locale = val;
            return this;
        }

        public Builder setKeywords(ArrayMap<String, String> val) {
            this.keywordsMap = val;
            return this;
        }

        public CloudKeywordsHolder build() {
            return new CloudKeywordsHolder(this);
        }
    }

    private CloudKeywordsHolder(Builder builder) {
        this.version = builder.version;
        this.locale = builder.locale;
        this.keywordsMap = builder.keywordsMap;
    }

    public String getVersion() {
        return this.version;
    }

    public String getLocale() {
        return this.locale;
    }

    public String getKeywords(String name) {
        ArrayMap arrayMap = this.keywordsMap;
        return arrayMap != null ? (String) arrayMap.get(name) : "";
    }

    public boolean isEmpty() {
        ArrayMap arrayMap = this.keywordsMap;
        return arrayMap == null || arrayMap.isEmpty();
    }
}
