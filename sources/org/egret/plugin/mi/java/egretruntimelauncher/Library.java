package org.egret.plugin.mi.java.egretruntimelauncher;

import org.json.JSONObject;

public class Library {
    private static final String JSON_LIBRARY_CHECKSUM = "md5";
    private static final String JSON_ZIP_CHECKSUM = "zip";
    private static final String JSON_ZIP_NAME = "name";
    private String libraryCheckSum;
    private String libraryName;
    private String url;
    private String zipCheckSum;
    private String zipName;

    public Library(JSONObject json, String baseUrl) {
        try {
            this.zipName = json.getString("name");
            this.libraryCheckSum = json.getString(JSON_LIBRARY_CHECKSUM);
            this.zipCheckSum = json.getString(JSON_ZIP_CHECKSUM);
            if (this.zipName == null) {
                this.libraryName = null;
                return;
            }
            this.url = getUrlBy(baseUrl, this.zipName);
            int end = this.zipName.lastIndexOf(".zip");
            this.libraryName = end < 0 ? null : this.zipName.substring(0, end);
        } catch (Exception e) {
            e.printStackTrace();
            this.zipName = null;
            this.libraryName = null;
            this.libraryCheckSum = null;
            this.zipCheckSum = null;
        }
    }

    private String getUrlBy(String baseUrl, String name) {
        if (baseUrl == null) {
            return null;
        }
        String str = "/";
        if (baseUrl.endsWith(str)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(baseUrl);
            stringBuilder.append(name);
            return stringBuilder.toString();
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(baseUrl);
        stringBuilder2.append(str);
        stringBuilder2.append(name);
        return stringBuilder2.toString();
    }

    public String getZipName() {
        return this.zipName;
    }

    public String getLibraryCheckSum() {
        return this.libraryCheckSum;
    }

    public String getZipCheckSum() {
        return this.zipCheckSum;
    }

    public String getLibraryName() {
        return this.libraryName;
    }

    public String getUrl() {
        return this.url;
    }
}
