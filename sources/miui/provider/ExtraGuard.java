package miui.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import miui.util.IOUtils;

public final class ExtraGuard {
    public static final String DEFAULT_PACKAGE_NAME = "com.miui.guardprovider";
    public static final int VIRUS_BLACK = 2;
    public static final int VIRUS_GRAY = 3;
    public static final int VIRUS_WHITE = 1;

    public static ExtraGuardVirusInfoEntity checkApkForVirusInfo(Context context, Uri uri, boolean cloud) {
        if (uri != null) {
            ContentResolver resolver = context.getContentResolver();
            try {
                Uri antiUri = Uri.parse("content://guard/sync_scan");
                if (antiUri != null) {
                    ExtraGuardVirusInfoEntity result = new ExtraGuardVirusInfoEntity();
                    Cursor cursor = resolver.query(antiUri, null, null, new String[]{uri.toString(), String.valueOf(cloud)}, null);
                    if (cursor != null && cursor.moveToNext()) {
                        result.setType(cursor.getInt(cursor.getColumnIndex("Type")));
                        result.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                        result.setVirusName(cursor.getString(cursor.getColumnIndex("VirusName")));
                        result.setEngineName(cursor.getString(cursor.getColumnIndex("EngineName")));
                    }
                    IOUtils.closeQuietly(cursor);
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable th) {
                IOUtils.closeQuietly(null);
            }
            IOUtils.closeQuietly(null);
            return null;
        }
        throw new NullPointerException("uri is null");
    }
}
