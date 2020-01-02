package miui.provider;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import java.io.File;
import miui.graphics.BitmapFactory;
import miui.telephony.PhoneNumberUtils.PhoneNumber;

public class MiProfileResult {
    private static final String MIPROFILE_DIR = "/data/data/com.miui.cloudservice/files/";
    private static final String TAG = "MiProfileResult";
    public String mJson;
    public String mName;
    public Bitmap mPhoto;
    public String mSid;
    public Bitmap mThumbnail;
    public String mType;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("mSid = ");
        sb.append(this.mSid);
        sb.append(";");
        sb.append("mType = ");
        sb.append(this.mType);
        String str = "; ";
        sb.append(str);
        if (!TextUtils.isEmpty(this.mName)) {
            sb.append("mName = ");
            sb.append(this.mName);
            sb.append(str);
        }
        if (this.mPhoto != null) {
            sb.append("mPhoto exists; ");
        }
        if (this.mThumbnail != null) {
            sb.append("mThumbnail exists; ");
        }
        if (!TextUtils.isEmpty(this.mJson)) {
            sb.append("mJson = ");
            sb.append(this.mJson);
            sb.append(str);
        }
        return sb.toString();
    }

    /* JADX WARNING: Missing block: B:9:0x0086, code skipped:
            if (r1 != null) goto L_0x0088;
     */
    /* JADX WARNING: Missing block: B:10:0x0088, code skipped:
            r1.close();
     */
    /* JADX WARNING: Missing block: B:16:0x0094, code skipped:
            if (r1 == null) goto L_0x0097;
     */
    /* JADX WARNING: Missing block: B:18:0x0098, code skipped:
            return null;
     */
    public static miui.provider.MiProfileResult queryContactMiProfile(android.content.Context r13, java.lang.String r14) {
        /*
        r0 = normalizeNumber(r14);
        r1 = 0;
        r2 = r13.getContentResolver();	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r3 = miui.provider.MiProfile.MIPROFILE_NUMBERS_URI;	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r4 = "sid";
        r5 = "type";
        r6 = "name";
        r7 = "json";
        r4 = new java.lang.String[]{r4, r5, r6, r7};	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r5 = "number=? AND type=? AND (expireTime>=? OR expireTime<0)";
        r8 = 3;
        r6 = new java.lang.String[r8];	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r9 = 0;
        r6[r9] = r0;	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r7 = "SendCard";
        r10 = 1;
        r6[r10] = r7;	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r11 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r7 = java.lang.String.valueOf(r11);	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r11 = 2;
        r6[r11] = r7;	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r7 = 0;
        r2 = r2.query(r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r1 = r2;
        if (r1 == 0) goto L_0x0086;
    L_0x0039:
        r2 = r1.moveToFirst();	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        if (r2 == 0) goto L_0x0086;
    L_0x003f:
        r2 = new miui.provider.MiProfileResult;	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r2.<init>();	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r3 = r1.getString(r9);	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r2.mSid = r3;	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r3 = r1.getString(r10);	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r2.mType = r3;	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r3 = r1.getString(r11);	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r2.mName = r3;	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r3 = r1.getString(r8);	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r2.mJson = r3;	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r3 = r2.mSid;	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r4 = "thumbnail";
        r3 = queryMiProfilePhoto(r13, r3, r4, r10);	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r2.mThumbnail = r3;	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r3 = "MiProfileResult";
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r4.<init>();	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r5 = "queryContactMiProfile(): sid =";
        r4.append(r5);	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r5 = r2.mSid;	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r4.append(r5);	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        android.util.Log.d(r3, r4);	 Catch:{ Exception -> 0x0093, all -> 0x008c }
        r1.close();
        return r2;
    L_0x0086:
        if (r1 == 0) goto L_0x0097;
    L_0x0088:
        r1.close();
        goto L_0x0097;
    L_0x008c:
        r2 = move-exception;
        if (r1 == 0) goto L_0x0092;
    L_0x008f:
        r1.close();
    L_0x0092:
        throw r2;
    L_0x0093:
        r2 = move-exception;
        if (r1 == 0) goto L_0x0097;
    L_0x0096:
        goto L_0x0088;
    L_0x0097:
        r2 = 0;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.provider.MiProfileResult.queryContactMiProfile(android.content.Context, java.lang.String):miui.provider.MiProfileResult");
    }

    /* JADX WARNING: Missing block: B:13:0x0080, code skipped:
            if (r2 != null) goto L_0x0082;
     */
    /* JADX WARNING: Missing block: B:14:0x0082, code skipped:
            r2.close();
     */
    /* JADX WARNING: Missing block: B:20:0x008e, code skipped:
            if (r2 == null) goto L_0x0091;
     */
    /* JADX WARNING: Missing block: B:21:0x0091, code skipped:
            return null;
     */
    public static miui.provider.MiProfileResult queryPhoneMiProfile(android.content.Context r13, java.lang.String r14) {
        /*
        r0 = 0;
        if (r13 == 0) goto L_0x0092;
    L_0x0003:
        r1 = android.text.TextUtils.isEmpty(r14);
        if (r1 == 0) goto L_0x000b;
    L_0x0009:
        goto L_0x0092;
    L_0x000b:
        r1 = normalizeNumber(r14);
        r2 = 0;
        r3 = r13.getContentResolver();	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r4 = miui.provider.MiProfile.MIPROFILE_NUMBERS_URI;	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r5 = "sid";
        r6 = "name";
        r5 = new java.lang.String[]{r5, r6};	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r6 = "number=? AND type=? AND (expireTime>=? OR expireTime<0)";
        r7 = 3;
        r7 = new java.lang.String[r7];	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r9 = 0;
        r7[r9] = r1;	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r8 = "SendCard";
        r10 = 1;
        r7[r10] = r8;	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r8 = 2;
        r11 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r11 = java.lang.String.valueOf(r11);	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r7[r8] = r11;	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r8 = 0;
        r3 = r3.query(r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r2 = r3;
        if (r2 == 0) goto L_0x0080;
    L_0x003f:
        r3 = r2.moveToFirst();	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        if (r3 == 0) goto L_0x0080;
    L_0x0045:
        r3 = new miui.provider.MiProfileResult;	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r3.<init>();	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r4 = r2.getString(r9);	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r3.mSid = r4;	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r4 = r2.getString(r10);	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r3.mName = r4;	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r4 = r3.mSid;	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r5 = "thumbnail";
        r4 = queryMiProfilePhoto(r13, r4, r5, r9);	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r3.mThumbnail = r4;	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r4 = "MiProfileResult";
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r5.<init>();	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r6 = "queryPhoneMiProfile(): sid =";
        r5.append(r6);	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r6 = r3.mSid;	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r5.append(r6);	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        android.util.Log.d(r4, r5);	 Catch:{ Exception -> 0x008d, all -> 0x0086 }
        r2.close();
        return r3;
    L_0x0080:
        if (r2 == 0) goto L_0x0091;
    L_0x0082:
        r2.close();
        goto L_0x0091;
    L_0x0086:
        r0 = move-exception;
        if (r2 == 0) goto L_0x008c;
    L_0x0089:
        r2.close();
    L_0x008c:
        throw r0;
    L_0x008d:
        r3 = move-exception;
        if (r2 == 0) goto L_0x0091;
    L_0x0090:
        goto L_0x0082;
    L_0x0091:
        return r0;
    L_0x0092:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.provider.MiProfileResult.queryPhoneMiProfile(android.content.Context, java.lang.String):miui.provider.MiProfileResult");
    }

    public static String getMiProfileFileName(String sourceId, String fileType) {
        return String.format("%s-%s", new Object[]{Integer.valueOf(sourceId.hashCode()), fileType});
    }

    private static Bitmap queryMiProfilePhoto(Context context, String sid, String fileType, boolean rounded) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MIPROFILE_DIR);
        stringBuilder.append(getMiProfileFileName(sid, fileType));
        File photoFile = new File(stringBuilder.toString());
        try {
            if (photoFile.exists()) {
                Bitmap bmp = BitmapFactory.decodeBitmap(context, Uri.fromFile(photoFile), true);
                if (bmp != null) {
                    return rounded ? BitmapFactory.createPhoto(context, bmp) : bmp;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static String normalizeNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }
        PhoneNumber pn = PhoneNumber.parse(PhoneNumberUtils.normalizeNumber(number));
        String normalizedNumber = pn != null ? pn.getNormalizedNumber(true, false) : number;
        if (!TextUtils.isEmpty(normalizedNumber)) {
            if (normalizedNumber.startsWith("+86")) {
                return normalizedNumber.substring(3);
            }
            if (normalizedNumber.startsWith("0086")) {
                return normalizedNumber.substring(4);
            }
        }
        return normalizedNumber;
    }
}
