package android.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseConfiguration;
import android.util.Log;
import java.io.File;

public final class DefaultDatabaseErrorHandler implements DatabaseErrorHandler {
    private static final String TAG = "DefaultDatabaseErrorHandler";

    /* JADX WARNING: Removed duplicated region for block: B:8:0x0038 A:{Splitter:B:5:0x0032, PHI: r0 , ExcHandler: all (th java.lang.Throwable)} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:12:0x003f, code skipped:
            if (r0 != null) goto L_0x0041;
     */
    /* JADX WARNING: Missing block: B:13:0x0041, code skipped:
            r2 = r0.iterator();
     */
    /* JADX WARNING: Missing block: B:15:0x0049, code skipped:
            if (r2.hasNext() != false) goto L_0x004b;
     */
    /* JADX WARNING: Missing block: B:16:0x004b, code skipped:
            deleteDatabaseFile((java.lang.String) ((android.util.Pair) r2.next()).second);
     */
    /* JADX WARNING: Missing block: B:17:0x005a, code skipped:
            deleteDatabaseFile(r6.getPath());
     */
    public void onCorruption(android.database.sqlite.SQLiteDatabase r6) {
        /*
        r5 = this;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Corruption reported by sqlite on database: ";
        r0.append(r1);
        r1 = r6.getPath();
        r0.append(r1);
        r0 = r0.toString();
        r1 = "DefaultDatabaseErrorHandler";
        android.util.Log.e(r1, r0);
        r0 = r6.getPath();
        r1 = "corruption";
        android.database.sqlite.SQLiteDatabase.wipeDetected(r0, r1);
        r0 = r6.isOpen();
        if (r0 != 0) goto L_0x0031;
    L_0x0029:
        r0 = r6.getPath();
        r5.deleteDatabaseFile(r0);
        return;
    L_0x0031:
        r0 = 0;
        r1 = r6.getAttachedDbs();	 Catch:{ SQLiteException -> 0x003a, all -> 0x0038 }
        r0 = r1;
        goto L_0x003b;
    L_0x0038:
        r1 = move-exception;
        goto L_0x003f;
    L_0x003a:
        r1 = move-exception;
    L_0x003b:
        r6.close();	 Catch:{ SQLiteException -> 0x0062, all -> 0x0038 }
        goto L_0x0063;
    L_0x003f:
        if (r0 == 0) goto L_0x005a;
    L_0x0041:
        r2 = r0.iterator();
    L_0x0045:
        r3 = r2.hasNext();
        if (r3 == 0) goto L_0x0059;
    L_0x004b:
        r3 = r2.next();
        r3 = (android.util.Pair) r3;
        r4 = r3.second;
        r4 = (java.lang.String) r4;
        r5.deleteDatabaseFile(r4);
        goto L_0x0045;
    L_0x0059:
        goto L_0x0061;
    L_0x005a:
        r2 = r6.getPath();
        r5.deleteDatabaseFile(r2);
    L_0x0061:
        throw r1;
    L_0x0062:
        r1 = move-exception;
    L_0x0063:
        if (r0 == 0) goto L_0x007e;
    L_0x0065:
        r1 = r0.iterator();
    L_0x0069:
        r2 = r1.hasNext();
        if (r2 == 0) goto L_0x007d;
    L_0x006f:
        r2 = r1.next();
        r2 = (android.util.Pair) r2;
        r3 = r2.second;
        r3 = (java.lang.String) r3;
        r5.deleteDatabaseFile(r3);
        goto L_0x0069;
    L_0x007d:
        goto L_0x0086;
    L_0x007e:
        r1 = r6.getPath();
        r5.deleteDatabaseFile(r1);
    L_0x0086:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.database.DefaultDatabaseErrorHandler.onCorruption(android.database.sqlite.SQLiteDatabase):void");
    }

    private void deleteDatabaseFile(String fileName) {
        if (!fileName.equalsIgnoreCase(SQLiteDatabaseConfiguration.MEMORY_DB_PATH) && fileName.trim().length() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("deleting the database file: ");
            stringBuilder.append(fileName);
            String stringBuilder2 = stringBuilder.toString();
            String str = TAG;
            Log.e(str, stringBuilder2);
            try {
                SQLiteDatabase.deleteDatabase(new File(fileName), false);
            } catch (Exception e) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("delete failed: ");
                stringBuilder3.append(e.getMessage());
                Log.w(str, stringBuilder3.toString());
            }
        }
    }
}
