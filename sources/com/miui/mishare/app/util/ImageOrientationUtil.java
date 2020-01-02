package com.miui.mishare.app.util;

import android.app.slice.SliceItem;
import android.content.ContentResolver;
import android.content.Context;
import android.media.ExifInterface;
import android.net.Uri;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageOrientationUtil {
    private static final String SCHEME_CONTENT = "content";
    private static final String SCHEME_FILE = "file";

    public static void closeSilently(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Throwable th) {
            }
        }
    }

    public static int getExifRotation(File imageFile) {
        if (imageFile == null) {
            return 0;
        }
        try {
            int attributeInt = new ExifInterface(imageFile.getAbsolutePath()).getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            if (attributeInt == 3) {
                return 180;
            }
            if (attributeInt == 6) {
                return 90;
            }
            if (attributeInt != 8) {
                return 0;
            }
            return 270;
        } catch (IOException e) {
            return 0;
        }
    }

    /* JADX WARNING: Missing block: B:26:0x006d, code skipped:
            if (r9 != null) goto L_0x0075;
     */
    /* JADX WARNING: Missing block: B:29:0x0073, code skipped:
            if (r9 == null) goto L_0x008a;
     */
    /* JADX WARNING: Missing block: B:30:0x0075, code skipped:
            r9.close();
     */
    public static java.io.File getFromMediaUri(android.content.Context r10, android.content.ContentResolver r11, android.net.Uri r12) {
        /*
        r0 = 0;
        if (r12 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = r12.getScheme();
        r2 = "file";
        r1 = r2.equals(r1);
        if (r1 == 0) goto L_0x001a;
    L_0x0010:
        r0 = new java.io.File;
        r1 = r12.getPath();
        r0.<init>(r1);
        return r0;
    L_0x001a:
        r1 = r12.getScheme();
        r2 = "content";
        r1 = r2.equals(r1);
        if (r1 == 0) goto L_0x008a;
    L_0x0026:
        r1 = "_display_name";
        r2 = "_data";
        r5 = new java.lang.String[]{r2, r1};
        r9 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r3 = r11;
        r4 = r12;
        r3 = r3.query(r4, r5, r6, r7, r8);	 Catch:{ IllegalArgumentException -> 0x0079, SecurityException -> 0x0072 }
        r9 = r3;
        if (r9 == 0) goto L_0x006d;
    L_0x003b:
        r3 = r9.moveToFirst();	 Catch:{ IllegalArgumentException -> 0x0079, SecurityException -> 0x0072 }
        if (r3 == 0) goto L_0x006d;
    L_0x0041:
        r3 = r12.toString();	 Catch:{ IllegalArgumentException -> 0x0079, SecurityException -> 0x0072 }
        r4 = "content://com.google.android.gallery3d";
        r3 = r3.startsWith(r4);	 Catch:{ IllegalArgumentException -> 0x0079, SecurityException -> 0x0072 }
        if (r3 == 0) goto L_0x0052;
    L_0x004d:
        r1 = r9.getColumnIndex(r1);	 Catch:{ IllegalArgumentException -> 0x0079, SecurityException -> 0x0072 }
        goto L_0x0056;
    L_0x0052:
        r1 = r9.getColumnIndex(r2);	 Catch:{ IllegalArgumentException -> 0x0079, SecurityException -> 0x0072 }
        r2 = -1;
        if (r1 == r2) goto L_0x006d;
    L_0x005a:
        r2 = r9.getString(r1);	 Catch:{ IllegalArgumentException -> 0x0079, SecurityException -> 0x0072 }
        r3 = android.text.TextUtils.isEmpty(r2);	 Catch:{ IllegalArgumentException -> 0x0079, SecurityException -> 0x0072 }
        if (r3 != 0) goto L_0x006d;
    L_0x0064:
        r3 = new java.io.File;	 Catch:{ IllegalArgumentException -> 0x0079, SecurityException -> 0x0072 }
        r3.<init>(r2);	 Catch:{ IllegalArgumentException -> 0x0079, SecurityException -> 0x0072 }
        r9.close();
        return r3;
    L_0x006d:
        if (r9 == 0) goto L_0x008a;
    L_0x006f:
        goto L_0x0075;
    L_0x0070:
        r0 = move-exception;
        goto L_0x0084;
    L_0x0072:
        r1 = move-exception;
        if (r9 == 0) goto L_0x008a;
    L_0x0075:
        r9.close();
        goto L_0x008a;
    L_0x0079:
        r0 = move-exception;
        r1 = getFromMediaUriPfd(r10, r11, r12);	 Catch:{ all -> 0x0070 }
        if (r9 == 0) goto L_0x0083;
    L_0x0080:
        r9.close();
    L_0x0083:
        return r1;
    L_0x0084:
        if (r9 == 0) goto L_0x0089;
    L_0x0086:
        r9.close();
    L_0x0089:
        throw r0;
    L_0x008a:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.mishare.app.util.ImageOrientationUtil.getFromMediaUri(android.content.Context, android.content.ContentResolver, android.net.Uri):java.io.File");
    }

    private static String getTempFilename(Context context) throws IOException {
        return File.createTempFile(SliceItem.FORMAT_IMAGE, "tmp", context.getCacheDir()).getAbsolutePath();
    }

    private static File getFromMediaUriPfd(Context context, ContentResolver resolver, Uri uri) {
        if (uri == null) {
            return null;
        }
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new FileInputStream(resolver.openFileDescriptor(uri, "r").getFileDescriptor());
            String tempFilename = getTempFilename(context);
            output = new FileOutputStream(tempFilename);
            byte[] bytes = new byte[4096];
            while (true) {
                int read = input.read(bytes);
                int read2 = read;
                if (read == -1) {
                    break;
                }
                output.write(bytes, 0, read2);
            }
            File file = new File(tempFilename);
            return file;
        } catch (IOException e) {
            return null;
        } finally {
            closeSilently(input);
            closeSilently(output);
        }
    }
}
