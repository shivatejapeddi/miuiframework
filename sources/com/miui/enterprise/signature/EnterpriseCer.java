package com.miui.enterprise.signature;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import miui.maml.elements.MusicLyricParser;

public class EnterpriseCer implements Parcelable {
    public static final String ACTION_ENTERPRISE_CERT_UPDATE = "com.miui.enterprise.ACTION_CERT_UPDATE";
    public static final String CERT_FILE = "/data/system/entcert";
    public static final Creator<EnterpriseCer> CREATOR = new Creator<EnterpriseCer>() {
        public EnterpriseCer createFromParcel(Parcel source) {
            return new EnterpriseCer(source);
        }

        public EnterpriseCer[] newArray(int size) {
            return new EnterpriseCer[size];
        }
    };
    public static final String EXTRA_CERT = "extra_ent_cert";
    public static final String PERMISSION_ACTIVATE_ENTERPRISE_MODE = "com.miui.enterprise.permission.ACTIVE_ENTERPRISE_MODE";
    static final DateFormat sDateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public String agencyCode;
    public String apkHash;
    public String apkNewHash;
    public String[] deviceIds;
    public String licenceCode;
    public String[] permissions;
    String signature;
    public long validFrom;
    public long validTo;

    private String printArray(String[] array) {
        if (array == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String str : array) {
            sb.append(str);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public Date getValidFrom() {
        return new Date(this.validFrom);
    }

    public Date getValidTo() {
        return new Date(this.validTo);
    }

    public EnterpriseCer() {
        String str = "";
        this.agencyCode = str;
        this.licenceCode = str;
        this.apkHash = str;
        this.apkNewHash = str;
        this.signature = str;
    }

    public EnterpriseCer(Parcel in) {
        String str = "";
        this.agencyCode = str;
        this.licenceCode = str;
        this.apkHash = str;
        this.apkNewHash = str;
        this.signature = str;
        this.agencyCode = in.readString();
        this.licenceCode = in.readString();
        this.permissions = in.createStringArray();
        this.deviceIds = in.createStringArray();
        this.validFrom = in.readLong();
        this.validTo = in.readLong();
        this.apkHash = in.readString();
        this.apkNewHash = in.readString();
        this.signature = in.readString();
    }

    public EnterpriseCer(InputStream input) throws IOException {
        String str = "";
        this.agencyCode = str;
        this.licenceCode = str;
        this.apkHash = str;
        this.apkNewHash = str;
        this.signature = str;
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String readLine = reader.readLine();
            String line = readLine;
            if (readLine != null) {
                if (line.startsWith("AgencyCode")) {
                    this.agencyCode = line.replace("AgencyCode:", str);
                }
                if (line.startsWith("LicenceCode")) {
                    this.licenceCode = line.replace("LicenceCode:", str);
                }
                String str2 = ",";
                String str3 = "*";
                if (line.startsWith("Permissions")) {
                    readLine = line.replace("Permissions:", str);
                    if (readLine.equals(str) || readLine.equals(str3)) {
                        this.permissions = null;
                    } else {
                        this.permissions = readLine.split(str2);
                    }
                }
                if (line.startsWith("DeviceIds")) {
                    readLine = line.replace("DeviceIds:", str);
                    if (readLine.equals(str3)) {
                        this.deviceIds = null;
                    } else {
                        this.deviceIds = readLine.split(str2);
                    }
                }
                if (line.startsWith("ValidFrom")) {
                    try {
                        this.validFrom = sDateformat.parse(line.replace("ValidFrom:", str)).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (line.startsWith("ValidTo")) {
                    try {
                        this.validTo = sDateformat.parse(line.replace("ValidTo:", str)).getTime();
                    } catch (ParseException e2) {
                        e2.printStackTrace();
                    }
                }
                if (line.startsWith("ApkHash")) {
                    this.apkHash = line.replace("ApkHash:", str);
                }
                if (line.startsWith("ApkNewHash")) {
                    this.apkNewHash = line.replace("ApkNewHash:", str);
                }
                if (line.startsWith("Signature")) {
                    this.signature = line.replace("Signature:", str);
                }
            } else {
                return;
            }
        }
    }

    public String getUnSignedRaw() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AgencyCode:");
        stringBuilder.append(this.agencyCode);
        stringBuilder.append("\r\nLicenceCode:");
        stringBuilder.append(this.licenceCode);
        stringBuilder.append("\r\nPermissions:");
        String[] strArr = this.permissions;
        String str = "*";
        stringBuilder.append(strArr == null ? str : printArray(strArr));
        stringBuilder.append("\r\nDeviceIds:");
        strArr = this.deviceIds;
        if (strArr != null) {
            str = printArray(strArr);
        }
        stringBuilder.append(str);
        stringBuilder.append("\r\nValidFrom:");
        stringBuilder.append(sDateformat.format(new Date(this.validFrom)));
        stringBuilder.append("\r\nValidTo:");
        stringBuilder.append(sDateformat.format(new Date(this.validTo)));
        stringBuilder.append("\r\nApkHash:");
        stringBuilder.append(this.apkHash);
        stringBuilder.append("\r\nApkNewHash:");
        stringBuilder.append(this.apkNewHash);
        stringBuilder.append(MusicLyricParser.CRLF);
        return stringBuilder.toString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AgencyCode:");
        stringBuilder.append(this.agencyCode);
        stringBuilder.append("\r\nLicenceCode:");
        stringBuilder.append(this.licenceCode);
        stringBuilder.append("\r\nPermissions:");
        String[] strArr = this.permissions;
        String str = "*";
        stringBuilder.append(strArr == null ? str : printArray(strArr));
        stringBuilder.append("\r\nDeviceIds:");
        strArr = this.deviceIds;
        if (strArr != null) {
            str = printArray(strArr);
        }
        stringBuilder.append(str);
        stringBuilder.append("\r\nValidFrom:");
        stringBuilder.append(sDateformat.format(new Date(this.validFrom)));
        stringBuilder.append("\r\nValidTo:");
        stringBuilder.append(sDateformat.format(new Date(this.validTo)));
        stringBuilder.append("\r\nApkHash:");
        stringBuilder.append(this.apkHash);
        stringBuilder.append("\r\nApkNewHash:");
        stringBuilder.append(this.apkNewHash);
        stringBuilder.append("\r\nSignature:");
        stringBuilder.append(this.signature);
        stringBuilder.append(MusicLyricParser.CRLF);
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.agencyCode);
        dest.writeString(this.licenceCode);
        dest.writeStringArray(this.permissions);
        dest.writeStringArray(this.deviceIds);
        dest.writeLong(this.validFrom);
        dest.writeLong(this.validTo);
        dest.writeString(this.apkHash);
        dest.writeString(this.apkNewHash);
        dest.writeString(this.signature);
    }
}
