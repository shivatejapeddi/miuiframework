package android.mtp;

import android.provider.MediaStore.Audio.AudioColumns;
import android.util.Log;
import java.util.ArrayList;

class MtpPropertyGroup {
    private static final String PATH_WHERE = "_data=?";
    private static final String TAG = MtpPropertyGroup.class.getSimpleName();
    private String[] mColumns;
    private final Property[] mProperties;

    private class Property {
        int code;
        int column;
        int type;

        Property(int code, int type, int column) {
            this.code = code;
            this.type = type;
            this.column = column;
        }
    }

    private native String format_date_time(long j);

    public MtpPropertyGroup(int[] properties) {
        int i;
        int count = properties.length;
        ArrayList<String> columns = new ArrayList(count);
        columns.add("_id");
        this.mProperties = new Property[count];
        for (i = 0; i < count; i++) {
            this.mProperties[i] = createProperty(properties[i], columns);
        }
        count = columns.size();
        this.mColumns = new String[count];
        for (i = 0; i < count; i++) {
            this.mColumns[i] = (String) columns.get(i);
        }
    }

    private Property createProperty(int code, ArrayList<String> columns) {
        int type;
        String column = null;
        switch (code) {
            case MtpConstants.PROPERTY_STORAGE_ID /*56321*/:
                type = 6;
                break;
            case MtpConstants.PROPERTY_OBJECT_FORMAT /*56322*/:
                type = 4;
                break;
            case MtpConstants.PROPERTY_PROTECTION_STATUS /*56323*/:
                type = 4;
                break;
            case MtpConstants.PROPERTY_OBJECT_SIZE /*56324*/:
                type = 8;
                break;
            case MtpConstants.PROPERTY_OBJECT_FILE_NAME /*56327*/:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_DATE_MODIFIED /*56329*/:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_PARENT_OBJECT /*56331*/:
                type = 6;
                break;
            case MtpConstants.PROPERTY_PERSISTENT_UID /*56385*/:
                type = 10;
                break;
            case MtpConstants.PROPERTY_NAME /*56388*/:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_ARTIST /*56390*/:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_DESCRIPTION /*56392*/:
                column = "description";
                type = 65535;
                break;
            case MtpConstants.PROPERTY_DATE_ADDED /*56398*/:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_DURATION /*56457*/:
                column = "duration";
                type = 6;
                break;
            case MtpConstants.PROPERTY_TRACK /*56459*/:
                column = AudioColumns.TRACK;
                type = 4;
                break;
            case MtpConstants.PROPERTY_COMPOSER /*56470*/:
                column = AudioColumns.COMPOSER;
                type = 65535;
                break;
            case MtpConstants.PROPERTY_ORIGINAL_RELEASE_DATE /*56473*/:
                column = "year";
                type = 65535;
                break;
            case MtpConstants.PROPERTY_ALBUM_NAME /*56474*/:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_ALBUM_ARTIST /*56475*/:
                column = AudioColumns.ALBUM_ARTIST;
                type = 65535;
                break;
            case MtpConstants.PROPERTY_DISPLAY_NAME /*56544*/:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_BITRATE_TYPE /*56978*/:
            case MtpConstants.PROPERTY_NUMBER_OF_CHANNELS /*56980*/:
                type = 4;
                break;
            case MtpConstants.PROPERTY_SAMPLE_RATE /*56979*/:
            case MtpConstants.PROPERTY_AUDIO_WAVE_CODEC /*56985*/:
            case MtpConstants.PROPERTY_AUDIO_BITRATE /*56986*/:
                type = 6;
                break;
            default:
                type = 0;
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unsupported property ");
                stringBuilder.append(code);
                Log.e(str, stringBuilder.toString());
                break;
        }
        if (column == null) {
            return new Property(code, type, -1);
        }
        columns.add(column);
        return new Property(code, type, columns.size() - 1);
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0071  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0164  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0154  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0146  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0136  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x012b  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00f9  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00d6  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00b6  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0091  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0084  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0071  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0164  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0154  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0146  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0136  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x012b  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00f9  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00d6  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00b6  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0091  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0084  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0075  */
    public int getPropertyList(android.content.ContentProviderClient r24, java.lang.String r25, android.mtp.MtpStorageManager.MtpObject r26, android.mtp.MtpPropertyList r27) {
        /*
        r23 = this;
        r1 = r23;
        r8 = r27;
        r0 = 0;
        r9 = r26.getId();
        r2 = r26.getPath();
        r10 = r2.toString();
        r11 = r1.mProperties;
        r12 = r11.length;
        r13 = 0;
        r2 = r0;
        r14 = r13;
    L_0x0017:
        if (r14 >= r12) goto L_0x01b9;
    L_0x0019:
        r15 = r11[r14];
        r0 = r15.column;
        r3 = -1;
        if (r0 == r3) goto L_0x0069;
    L_0x0020:
        if (r2 != 0) goto L_0x0069;
    L_0x0022:
        r0 = r26.getFormat();	 Catch:{ IllegalArgumentException -> 0x0062, RemoteException -> 0x0056 }
        r6 = r25;
        r17 = android.mtp.MtpDatabase.getObjectPropertiesUri(r0, r6);	 Catch:{ IllegalArgumentException -> 0x0054, RemoteException -> 0x0052 }
        r0 = r1.mColumns;	 Catch:{ IllegalArgumentException -> 0x0054, RemoteException -> 0x0052 }
        r19 = "_data=?";
        r3 = 1;
        r3 = new java.lang.String[r3];	 Catch:{ IllegalArgumentException -> 0x0054, RemoteException -> 0x0052 }
        r3[r13] = r10;	 Catch:{ IllegalArgumentException -> 0x0054, RemoteException -> 0x0052 }
        r21 = 0;
        r22 = 0;
        r16 = r24;
        r18 = r0;
        r20 = r3;
        r0 = r16.query(r17, r18, r19, r20, r21, r22);	 Catch:{ IllegalArgumentException -> 0x0054, RemoteException -> 0x0052 }
        r2 = r0;
        if (r2 == 0) goto L_0x0050;
    L_0x0046:
        r0 = r2.moveToNext();	 Catch:{ IllegalArgumentException -> 0x0054, RemoteException -> 0x0052 }
        if (r0 != 0) goto L_0x0050;
    L_0x004c:
        r2.close();	 Catch:{ IllegalArgumentException -> 0x0054, RemoteException -> 0x0052 }
        r2 = 0;
    L_0x0050:
        r0 = r2;
        goto L_0x006c;
    L_0x0052:
        r0 = move-exception;
        goto L_0x0059;
    L_0x0054:
        r0 = move-exception;
        goto L_0x0065;
    L_0x0056:
        r0 = move-exception;
        r6 = r25;
    L_0x0059:
        r3 = TAG;
        r4 = "Mediaprovider lookup failed";
        android.util.Log.e(r3, r4);
        r0 = r2;
        goto L_0x006c;
    L_0x0062:
        r0 = move-exception;
        r6 = r25;
    L_0x0065:
        r3 = 43009; // 0xa801 float:6.0268E-41 double:2.12493E-319;
        return r3;
    L_0x0069:
        r6 = r25;
        r0 = r2;
    L_0x006c:
        r2 = r15.code;
        switch(r2) {
            case 56321: goto L_0x0164;
            case 56322: goto L_0x0154;
            case 56323: goto L_0x0146;
            case 56324: goto L_0x0136;
            case 56327: goto L_0x012b;
            case 56329: goto L_0x011c;
            case 56331: goto L_0x00f9;
            case 56385: goto L_0x00d6;
            case 56388: goto L_0x012b;
            case 56398: goto L_0x011c;
            case 56459: goto L_0x00b6;
            case 56473: goto L_0x0091;
            case 56544: goto L_0x012b;
            case 56978: goto L_0x0084;
            case 56979: goto L_0x0075;
            case 56980: goto L_0x0084;
            case 56985: goto L_0x0075;
            case 56986: goto L_0x0075;
            default: goto L_0x0071;
        };
    L_0x0071:
        r2 = r15.type;
        goto L_0x0174;
    L_0x0075:
        r4 = r15.code;
        r5 = 6;
        r16 = 0;
        r2 = r27;
        r3 = r9;
        r6 = r16;
        r2.append(r3, r4, r5, r6);
        goto L_0x01b4;
    L_0x0084:
        r4 = r15.code;
        r5 = 4;
        r6 = 0;
        r2 = r27;
        r3 = r9;
        r2.append(r3, r4, r5, r6);
        goto L_0x01b4;
    L_0x0091:
        r2 = 0;
        if (r0 == 0) goto L_0x009a;
    L_0x0094:
        r3 = r15.column;
        r2 = r0.getInt(r3);
    L_0x009a:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = java.lang.Integer.toString(r2);
        r3.append(r4);
        r4 = "0101T000000";
        r3.append(r4);
        r3 = r3.toString();
        r4 = r15.code;
        r8.append(r9, r4, r3);
        goto L_0x01b4;
    L_0x00b6:
        r2 = 0;
        if (r0 == 0) goto L_0x00c1;
    L_0x00b9:
        r3 = r15.column;
        r2 = r0.getInt(r3);
        r6 = r2;
        goto L_0x00c2;
    L_0x00c1:
        r6 = r2;
    L_0x00c2:
        r4 = r15.code;
        r5 = 4;
        r2 = r6 % 1000;
        r2 = (long) r2;
        r16 = r2;
        r2 = r27;
        r3 = r9;
        r18 = r6;
        r6 = r16;
        r2.append(r3, r4, r5, r6);
        goto L_0x01b4;
    L_0x00d6:
        r2 = r26.getPath();
        r2 = r2.toString();
        r2 = r2.hashCode();
        r2 = r2 << 32;
        r2 = (long) r2;
        r4 = r26.getModifiedTime();
        r16 = r2 + r4;
        r4 = r15.code;
        r5 = r15.type;
        r2 = r27;
        r3 = r9;
        r6 = r16;
        r2.append(r3, r4, r5, r6);
        goto L_0x01b4;
    L_0x00f9:
        r4 = r15.code;
        r5 = r15.type;
        r2 = r26.getParent();
        r2 = r2.isRoot();
        if (r2 == 0) goto L_0x010a;
    L_0x0107:
        r2 = 0;
        goto L_0x0113;
    L_0x010a:
        r2 = r26.getParent();
        r2 = r2.getId();
        r2 = (long) r2;
    L_0x0113:
        r6 = r2;
        r2 = r27;
        r3 = r9;
        r2.append(r3, r4, r5, r6);
        goto L_0x01b4;
    L_0x011c:
        r2 = r15.code;
        r3 = r26.getModifiedTime();
        r3 = r1.format_date_time(r3);
        r8.append(r9, r2, r3);
        goto L_0x01b4;
    L_0x012b:
        r2 = r15.code;
        r3 = r26.getName();
        r8.append(r9, r2, r3);
        goto L_0x01b4;
    L_0x0136:
        r4 = r15.code;
        r5 = r15.type;
        r6 = r26.getSize();
        r2 = r27;
        r3 = r9;
        r2.append(r3, r4, r5, r6);
        goto L_0x01b4;
    L_0x0146:
        r4 = r15.code;
        r5 = r15.type;
        r6 = 0;
        r2 = r27;
        r3 = r9;
        r2.append(r3, r4, r5, r6);
        goto L_0x01b4;
    L_0x0154:
        r4 = r15.code;
        r5 = r15.type;
        r2 = r26.getFormat();
        r6 = (long) r2;
        r2 = r27;
        r3 = r9;
        r2.append(r3, r4, r5, r6);
        goto L_0x01b4;
    L_0x0164:
        r4 = r15.code;
        r5 = r15.type;
        r2 = r26.getStorageId();
        r6 = (long) r2;
        r2 = r27;
        r3 = r9;
        r2.append(r3, r4, r5, r6);
        goto L_0x01b4;
    L_0x0174:
        if (r2 == 0) goto L_0x01a7;
    L_0x0176:
        r3 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        if (r2 == r3) goto L_0x0197;
    L_0x017b:
        r2 = 0;
        if (r0 == 0) goto L_0x0188;
    L_0x017f:
        r4 = r15.column;
        r2 = r0.getLong(r4);
        r16 = r2;
        goto L_0x018a;
    L_0x0188:
        r16 = r2;
    L_0x018a:
        r4 = r15.code;
        r5 = r15.type;
        r2 = r27;
        r3 = r9;
        r6 = r16;
        r2.append(r3, r4, r5, r6);
        goto L_0x01b4;
    L_0x0197:
        r2 = "";
        if (r0 == 0) goto L_0x01a1;
    L_0x019b:
        r3 = r15.column;
        r2 = r0.getString(r3);
    L_0x01a1:
        r3 = r15.code;
        r8.append(r9, r3, r2);
        goto L_0x01b4;
    L_0x01a7:
        r4 = r15.code;
        r5 = r15.type;
        r6 = 0;
        r2 = r27;
        r3 = r9;
        r2.append(r3, r4, r5, r6);
    L_0x01b4:
        r14 = r14 + 1;
        r2 = r0;
        goto L_0x0017;
    L_0x01b9:
        if (r2 == 0) goto L_0x01be;
    L_0x01bb:
        r2.close();
    L_0x01be:
        r0 = 8193; // 0x2001 float:1.1481E-41 double:4.048E-320;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpPropertyGroup.getPropertyList(android.content.ContentProviderClient, java.lang.String, android.mtp.MtpStorageManager$MtpObject, android.mtp.MtpPropertyList):int");
    }
}
