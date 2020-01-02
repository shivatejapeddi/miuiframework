package android.media;

import android.annotation.UnsupportedAppUsage;
import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.UserInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.IAudioService.Stub;
import android.media.VolumeShaper.Configuration;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor.AutoCloseInputStream;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Audio.Media;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.util.Log;
import com.android.internal.database.SortCursor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RingtoneManager {
    public static final String ACTION_RINGTONE_PICKER = "android.intent.action.RINGTONE_PICKER";
    public static final String EXTRA_RINGTONE_AUDIO_ATTRIBUTES_FLAGS = "android.intent.extra.ringtone.AUDIO_ATTRIBUTES_FLAGS";
    public static final String EXTRA_RINGTONE_DEFAULT_URI = "android.intent.extra.ringtone.DEFAULT_URI";
    public static final String EXTRA_RINGTONE_EXISTING_URI = "android.intent.extra.ringtone.EXISTING_URI";
    @Deprecated
    public static final String EXTRA_RINGTONE_INCLUDE_DRM = "android.intent.extra.ringtone.INCLUDE_DRM";
    public static final String EXTRA_RINGTONE_PICKED_URI = "android.intent.extra.ringtone.PICKED_URI";
    public static final String EXTRA_RINGTONE_SHOW_DEFAULT = "android.intent.extra.ringtone.SHOW_DEFAULT";
    public static final String EXTRA_RINGTONE_SHOW_SILENT = "android.intent.extra.ringtone.SHOW_SILENT";
    public static final String EXTRA_RINGTONE_TITLE = "android.intent.extra.ringtone.TITLE";
    public static final String EXTRA_RINGTONE_TYPE = "android.intent.extra.ringtone.TYPE";
    public static final int ID_COLUMN_INDEX = 0;
    private static final String[] INTERNAL_COLUMNS;
    private static final String[] MEDIA_COLUMNS;
    private static final String TAG = "RingtoneManager";
    public static final int TITLE_COLUMN_INDEX = 1;
    public static final int TYPE_ALARM = 4;
    public static final int TYPE_ALL = 7;
    public static final int TYPE_NOTIFICATION = 2;
    public static final int TYPE_RINGTONE = 1;
    public static final int URI_COLUMN_INDEX = 2;
    private final Activity mActivity;
    private final Context mContext;
    @UnsupportedAppUsage
    private Cursor mCursor;
    private final List<String> mFilterColumns;
    private boolean mIncludeParentRingtones;
    private Ringtone mPreviousRingtone;
    private boolean mStopPreviousRingtone;
    private int mType;

    static {
        String str = "title_key";
        String str2 = "_id";
        String str3 = "title";
        INTERNAL_COLUMNS = new String[]{str2, str3, str3, str};
        MEDIA_COLUMNS = new String[]{str2, str3, str3, str};
    }

    public RingtoneManager(Activity activity) {
        this(activity, false);
    }

    public RingtoneManager(Activity activity, boolean includeParentRingtones) {
        this.mType = 1;
        this.mFilterColumns = new ArrayList();
        this.mStopPreviousRingtone = true;
        this.mActivity = activity;
        this.mContext = activity;
        setType(this.mType);
        this.mIncludeParentRingtones = includeParentRingtones;
    }

    public RingtoneManager(Context context) {
        this(context, false);
    }

    public RingtoneManager(Context context, boolean includeParentRingtones) {
        this.mType = 1;
        this.mFilterColumns = new ArrayList();
        this.mStopPreviousRingtone = true;
        this.mActivity = null;
        this.mContext = context;
        setType(this.mType);
        this.mIncludeParentRingtones = includeParentRingtones;
    }

    public void setType(int type) {
        if (this.mCursor == null) {
            this.mType = type;
            setFilterColumnsList(type);
            return;
        }
        throw new IllegalStateException("Setting filter columns should be done before querying for ringtones.");
    }

    public int inferStreamType() {
        int i = this.mType;
        if (i != 2) {
            return i != 4 ? 2 : 4;
        } else {
            return 5;
        }
    }

    public void setStopPreviousRingtone(boolean stopPreviousRingtone) {
        this.mStopPreviousRingtone = stopPreviousRingtone;
    }

    public boolean getStopPreviousRingtone() {
        return this.mStopPreviousRingtone;
    }

    public void stopPreviousRingtone() {
        Ringtone ringtone = this.mPreviousRingtone;
        if (ringtone != null) {
            ringtone.stop();
        }
    }

    @Deprecated
    public boolean getIncludeDrm() {
        return false;
    }

    @Deprecated
    public void setIncludeDrm(boolean includeDrm) {
        if (includeDrm) {
            Log.w(TAG, "setIncludeDrm no longer supported");
        }
    }

    public Cursor getCursor() {
        Cursor cursor = this.mCursor;
        if (cursor != null && cursor.requery()) {
            return this.mCursor;
        }
        ArrayList<Cursor> ringtoneCursors = new ArrayList();
        ringtoneCursors.add(getInternalRingtones());
        ringtoneCursors.add(getMediaRingtones());
        if (this.mIncludeParentRingtones) {
            Cursor parentRingtonesCursor = getParentProfileRingtones();
            if (parentRingtonesCursor != null) {
                ringtoneCursors.add(parentRingtonesCursor);
            }
        }
        SortCursor sortCursor = new SortCursor((Cursor[]) ringtoneCursors.toArray(new Cursor[ringtoneCursors.size()]), "title_key");
        this.mCursor = sortCursor;
        return sortCursor;
    }

    private Cursor getParentProfileRingtones() {
        UserInfo parentInfo = UserManager.get(this.mContext).getProfileParent(this.mContext.getUserId());
        if (!(parentInfo == null || parentInfo.id == this.mContext.getUserId())) {
            Context parentContext = createPackageContextAsUser(this.mContext, parentInfo.id);
            if (parentContext != null) {
                return new ExternalRingtonesCursorWrapper(getMediaRingtones(parentContext), ContentProvider.maybeAddUserId(Media.EXTERNAL_CONTENT_URI, parentInfo.id));
            }
        }
        return null;
    }

    public Ringtone getRingtone(int position) {
        if (this.mStopPreviousRingtone) {
            Ringtone ringtone = this.mPreviousRingtone;
            if (ringtone != null) {
                ringtone.stop();
            }
        }
        this.mPreviousRingtone = getRingtone(this.mContext, getRingtoneUri(position), inferStreamType());
        return this.mPreviousRingtone;
    }

    public Uri getRingtoneUri(int position) {
        Cursor cursor = this.mCursor;
        if (cursor == null || !cursor.moveToPosition(position)) {
            return null;
        }
        return getUriFromCursor(this.mContext, this.mCursor);
    }

    private static Uri getUriFromCursor(Context context, Cursor cursor) {
        return context.getContentResolver().canonicalizeOrElse(ContentUris.withAppendedId(Uri.parse(cursor.getString(2)), cursor.getLong(0)));
    }

    public int getRingtonePosition(Uri ringtoneUri) {
        if (ringtoneUri == null) {
            return -1;
        }
        long ringtoneId = ContentUris.parseId(ringtoneUri);
        Cursor cursor = getCursor();
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            if (ringtoneId == cursor.getLong(0)) {
                return cursor.getPosition();
            }
        }
        return -1;
    }

    public static Uri getValidRingtoneUri(Context context) {
        RingtoneManager rm = new RingtoneManager(context);
        Uri uri = getValidRingtoneUriFromCursorAndClose(context, rm.getInternalRingtones());
        if (uri == null) {
            return getValidRingtoneUriFromCursorAndClose(context, rm.getMediaRingtones());
        }
        return uri;
    }

    private static Uri getValidRingtoneUriFromCursorAndClose(Context context, Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        Uri uri = null;
        if (cursor.moveToFirst()) {
            uri = getUriFromCursor(context, cursor);
        }
        cursor.close();
        return uri;
    }

    @UnsupportedAppUsage
    private Cursor getInternalRingtones() {
        return new ExternalRingtonesCursorWrapper(query(Media.INTERNAL_CONTENT_URI, INTERNAL_COLUMNS, constructBooleanTrueWhereClause(this.mFilterColumns), null, "title_key"), Media.INTERNAL_CONTENT_URI);
    }

    private Cursor getMediaRingtones() {
        return new ExternalRingtonesCursorWrapper(getMediaRingtones(this.mContext), Media.EXTERNAL_CONTENT_URI);
    }

    @UnsupportedAppUsage
    private Cursor getMediaRingtones(Context context) {
        return query(Media.EXTERNAL_CONTENT_URI, MEDIA_COLUMNS, constructBooleanTrueWhereClause(this.mFilterColumns), null, "title_key", context);
    }

    private void setFilterColumnsList(int type) {
        List<String> columns = this.mFilterColumns;
        columns.clear();
        if ((type & 1) != 0) {
            columns.add(AudioColumns.IS_RINGTONE);
        }
        if ((type & 2) != 0) {
            columns.add(AudioColumns.IS_NOTIFICATION);
        }
        if ((type & 4) != 0) {
            columns.add(AudioColumns.IS_ALARM);
        }
    }

    private static String constructBooleanTrueWhereClause(List<String> columns) {
        if (columns == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = columns.size() - 1; i >= 0; i--) {
            sb.append((String) columns.get(i));
            sb.append("=1 or ");
        }
        if (columns.size() > 0) {
            sb.setLength(sb.length() - 4);
        }
        sb.append(")");
        return sb.toString();
    }

    private Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return query(uri, projection, selection, selectionArgs, sortOrder, this.mContext);
    }

    private Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, Context context) {
        Activity activity = this.mActivity;
        if (activity != null) {
            return activity.managedQuery(uri, projection, selection, selectionArgs, sortOrder);
        }
        return context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    public static Ringtone getRingtone(Context context, Uri ringtoneUri) {
        return getRingtone(context, ringtoneUri, -1);
    }

    public static Ringtone getRingtone(Context context, Uri ringtoneUri, Configuration volumeShaperConfig) {
        return getRingtone(context, ringtoneUri, -1, volumeShaperConfig);
    }

    @UnsupportedAppUsage
    private static Ringtone getRingtone(Context context, Uri ringtoneUri, int streamType) {
        return getRingtone(context, ringtoneUri, streamType, null);
    }

    @UnsupportedAppUsage
    private static Ringtone getRingtone(Context context, Uri ringtoneUri, int streamType, Configuration volumeShaperConfig) {
        try {
            Ringtone r = new Ringtone(context, true);
            if (streamType >= 0) {
                r.setStreamType(streamType);
            }
            r.setUri(ringtoneUri, volumeShaperConfig);
            return r;
        } catch (Exception ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to open ringtone ");
            stringBuilder.append(ringtoneUri);
            stringBuilder.append(": ");
            stringBuilder.append(ex);
            Log.e(TAG, stringBuilder.toString());
            return null;
        }
    }

    public static void disableSyncFromParent(Context userContext) {
        try {
            Stub.asInterface(ServiceManager.getService("audio")).disableRingtoneSync(userContext.getUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to disable ringtone sync.");
        }
    }

    public static void enableSyncFromParent(Context userContext) {
        Secure.putIntForUser(userContext.getContentResolver(), Secure.SYNC_PARENT_SOUNDS, 1, userContext.getUserId());
    }

    public static Uri getActualDefaultRingtoneUri(Context context, int type) {
        String setting = getSettingForType(type);
        Uri ringtoneUri = null;
        if (setting == null) {
            return null;
        }
        String uriString = System.getStringForUser(context.getContentResolver(), setting, context.getUserId());
        if (uriString != null) {
            ringtoneUri = Uri.parse(uriString);
        }
        if (ringtoneUri != null && ContentProvider.getUserIdFromUri(ringtoneUri) == context.getUserId()) {
            ringtoneUri = ContentProvider.getUriWithoutUserId(ringtoneUri);
        }
        return ringtoneUri;
    }

    /* JADX WARNING: Missing block: B:32:0x0060, code skipped:
            if (r5 != null) goto L_0x0062;
     */
    /* JADX WARNING: Missing block: B:34:?, code skipped:
            $closeResource(r2, r5);
     */
    /* JADX WARNING: Missing block: B:40:0x0069, code skipped:
            if (r4 != null) goto L_0x006b;
     */
    /* JADX WARNING: Missing block: B:42:?, code skipped:
            $closeResource(r2, r4);
     */
    public static void setActualDefaultRingtoneUri(android.content.Context r7, int r8, android.net.Uri r9) {
        /*
        r0 = getSettingForType(r8);
        if (r0 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r1 = r7.getContentResolver();
        r2 = 0;
        r3 = r7.getUserId();
        r4 = "sync_parent_sounds";
        r2 = android.provider.Settings.Secure.getIntForUser(r1, r4, r2, r3);
        r3 = 1;
        if (r2 != r3) goto L_0x001d;
    L_0x001a:
        disableSyncFromParent(r7);
    L_0x001d:
        r2 = isInternalRingtoneUri(r9);
        if (r2 != 0) goto L_0x002b;
    L_0x0023:
        r2 = r7.getUserId();
        r9 = android.content.ContentProvider.maybeAddUserId(r9, r2);
        r2 = 0;
        if (r9 == 0) goto L_0x0034;
    L_0x002f:
        r3 = r9.toString();
        goto L_0x0035;
    L_0x0034:
        r3 = r2;
    L_0x0035:
        r4 = r7.getUserId();
        android.provider.Settings.System.putStringForUser(r1, r0, r3, r4);
        if (r9 == 0) goto L_0x0086;
    L_0x003e:
        r3 = r7.getUserId();
        r3 = getCacheForType(r8, r3);
        r4 = openRingtone(r7, r9);	 Catch:{ IOException -> 0x006f }
        r5 = r1.openOutputStream(r3);	 Catch:{ all -> 0x0066 }
        android.os.FileUtils.copy(r4, r5);	 Catch:{ all -> 0x005d }
        if (r5 == 0) goto L_0x0057;
    L_0x0054:
        $closeResource(r2, r5);	 Catch:{ all -> 0x0066 }
    L_0x0057:
        if (r4 == 0) goto L_0x005c;
    L_0x0059:
        $closeResource(r2, r4);	 Catch:{ IOException -> 0x006f }
    L_0x005c:
        goto L_0x0086;
    L_0x005d:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x005f }
    L_0x005f:
        r6 = move-exception;
        if (r5 == 0) goto L_0x0065;
    L_0x0062:
        $closeResource(r2, r5);	 Catch:{ all -> 0x0066 }
    L_0x0065:
        throw r6;	 Catch:{ all -> 0x0066 }
    L_0x0066:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x0068 }
    L_0x0068:
        r5 = move-exception;
        if (r4 == 0) goto L_0x006e;
    L_0x006b:
        $closeResource(r2, r4);	 Catch:{ IOException -> 0x006f }
    L_0x006e:
        throw r5;	 Catch:{ IOException -> 0x006f }
    L_0x006f:
        r2 = move-exception;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Failed to cache ringtone: ";
        r4.append(r5);
        r4.append(r2);
        r4 = r4.toString();
        r5 = "RingtoneManager";
        android.util.Log.w(r5, r4);
    L_0x0086:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.RingtoneManager.setActualDefaultRingtoneUri(android.content.Context, int, android.net.Uri):void");
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 != null) {
            try {
                x1.close();
                return;
            } catch (Throwable th) {
                x0.addSuppressed(th);
                return;
            }
        }
        x1.close();
    }

    private static boolean isInternalRingtoneUri(Uri uri) {
        return isRingtoneUriInStorage(uri, Media.INTERNAL_CONTENT_URI);
    }

    private static boolean isExternalRingtoneUri(Uri uri) {
        return isRingtoneUriInStorage(uri, Media.EXTERNAL_CONTENT_URI);
    }

    private static boolean isRingtoneUriInStorage(Uri ringtone, Uri storage) {
        Uri uriWithoutUserId = ContentProvider.getUriWithoutUserId(ringtone);
        if (uriWithoutUserId == null) {
            return false;
        }
        return uriWithoutUserId.toString().startsWith(storage.toString());
    }

    /* JADX WARNING: Missing block: B:25:?, code skipped:
            $closeResource(r5, r4);
     */
    /* JADX WARNING: Missing block: B:31:0x0068, code skipped:
            if (r3 != null) goto L_0x006a;
     */
    /* JADX WARNING: Missing block: B:32:0x006a, code skipped:
            $closeResource(r4, r3);
     */
    public android.net.Uri addCustomExternalRingtone(android.net.Uri r8, int r9) throws java.io.FileNotFoundException, java.lang.IllegalArgumentException, java.io.IOException {
        /*
        r7 = this;
        r0 = android.os.Environment.getExternalStorageState();
        r1 = "mounted";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x008a;
    L_0x000d:
        r0 = r7.mContext;
        r0 = r0.getContentResolver();
        r0 = r0.getType(r8);
        if (r0 == 0) goto L_0x006e;
    L_0x0019:
        r1 = "audio/";
        r1 = r0.startsWith(r1);
        if (r1 != 0) goto L_0x0029;
    L_0x0021:
        r1 = "application/ogg";
        r1 = r0.equals(r1);
        if (r1 == 0) goto L_0x006e;
    L_0x0029:
        r1 = getExternalDirectoryForType(r9);
        r2 = r7.mContext;
        r3 = android.media.Utils.getFileDisplayNameFromUri(r2, r8);
        r3 = android.os.FileUtils.buildValidFatFilename(r3);
        r2 = android.media.Utils.getUniqueExternalFile(r2, r1, r3, r0);
        r3 = r7.mContext;
        r3 = r3.getContentResolver();
        r3 = r3.openInputStream(r8);
        r4 = new java.io.FileOutputStream;	 Catch:{ all -> 0x0065 }
        r4.<init>(r2);	 Catch:{ all -> 0x0065 }
        android.os.FileUtils.copy(r3, r4);	 Catch:{ all -> 0x005e }
        r5 = 0;
        $closeResource(r5, r4);	 Catch:{ all -> 0x0065 }
        if (r3 == 0) goto L_0x0057;
    L_0x0054:
        $closeResource(r5, r3);
    L_0x0057:
        r3 = r7.mContext;
        r3 = android.provider.MediaStore.scanFile(r3, r2);
        return r3;
    L_0x005e:
        r5 = move-exception;
        throw r5;	 Catch:{ all -> 0x0060 }
    L_0x0060:
        r6 = move-exception;
        $closeResource(r5, r4);	 Catch:{ all -> 0x0065 }
        throw r6;	 Catch:{ all -> 0x0065 }
    L_0x0065:
        r4 = move-exception;
        throw r4;	 Catch:{ all -> 0x0067 }
    L_0x0067:
        r5 = move-exception;
        if (r3 == 0) goto L_0x006d;
    L_0x006a:
        $closeResource(r4, r3);
    L_0x006d:
        throw r5;
    L_0x006e:
        r1 = new java.lang.IllegalArgumentException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Ringtone file must have MIME type \"audio/*\". Given file has MIME type \"";
        r2.append(r3);
        r2.append(r0);
        r3 = "\"";
        r2.append(r3);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
    L_0x008a:
        r0 = new java.io.IOException;
        r1 = "External storage is not mounted. Unable to install ringtones.";
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.RingtoneManager.addCustomExternalRingtone(android.net.Uri, int):android.net.Uri");
    }

    private static final String getExternalDirectoryForType(int type) {
        if (type == 1) {
            return Environment.DIRECTORY_RINGTONES;
        }
        if (type == 2) {
            return Environment.DIRECTORY_NOTIFICATIONS;
        }
        if (type == 4) {
            return Environment.DIRECTORY_ALARMS;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported ringtone type: ");
        stringBuilder.append(type);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private static InputStream openRingtone(Context context, Uri uri) throws IOException {
        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (IOException | SecurityException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to open directly; attempting failover: ");
            stringBuilder.append(e);
            Log.w(TAG, stringBuilder.toString());
            try {
                return new AutoCloseInputStream(((AudioManager) context.getSystemService(AudioManager.class)).getRingtonePlayer().openRingtone(uri));
            } catch (Exception e2) {
                throw new IOException(e2);
            }
        }
    }

    private static String getSettingForType(int type) {
        if ((type & 1) != 0) {
            return "ringtone";
        }
        if ((type & 2) != 0) {
            return System.NOTIFICATION_SOUND;
        }
        if ((type & 4) != 0) {
            return System.ALARM_ALERT;
        }
        return null;
    }

    public static Uri getCacheForType(int type) {
        return getCacheForType(type, UserHandle.getCallingUserId());
    }

    public static Uri getCacheForType(int type, int userId) {
        if ((type & 1) != 0) {
            return ContentProvider.maybeAddUserId(System.RINGTONE_CACHE_URI, userId);
        }
        if ((type & 2) != 0) {
            return ContentProvider.maybeAddUserId(System.NOTIFICATION_SOUND_CACHE_URI, userId);
        }
        if ((type & 4) != 0) {
            return ContentProvider.maybeAddUserId(System.ALARM_ALERT_CACHE_URI, userId);
        }
        return null;
    }

    public static boolean isDefault(Uri ringtoneUri) {
        return getDefaultType(ringtoneUri) != -1;
    }

    public static int getDefaultType(Uri defaultRingtoneUri) {
        defaultRingtoneUri = ContentProvider.getUriWithoutUserId(defaultRingtoneUri);
        if (defaultRingtoneUri == null) {
            return -1;
        }
        if (defaultRingtoneUri.equals(System.DEFAULT_RINGTONE_URI)) {
            return 1;
        }
        if (defaultRingtoneUri.equals(System.DEFAULT_NOTIFICATION_URI)) {
            return 2;
        }
        if (defaultRingtoneUri.equals(System.DEFAULT_ALARM_ALERT_URI)) {
            return 4;
        }
        return -1;
    }

    public static Uri getDefaultUri(int type) {
        if ((type & 1) != 0) {
            return System.DEFAULT_RINGTONE_URI;
        }
        if ((type & 2) != 0) {
            return System.DEFAULT_NOTIFICATION_URI;
        }
        if ((type & 4) != 0) {
            return System.DEFAULT_ALARM_ALERT_URI;
        }
        return null;
    }

    public static AssetFileDescriptor openDefaultRingtoneUri(Context context, Uri uri) throws FileNotFoundException {
        int type = getDefaultType(uri);
        Uri cacheUri = getCacheForType(type, context.getUserId());
        Uri actualUri = getActualDefaultRingtoneUri(context, type);
        ContentResolver resolver = context.getContentResolver();
        AssetFileDescriptor afd = null;
        String str = "r";
        if (cacheUri != null) {
            afd = resolver.openAssetFileDescriptor(cacheUri, str);
            if (afd != null) {
                return afd;
            }
        }
        if (actualUri != null) {
            afd = resolver.openAssetFileDescriptor(actualUri, str);
        }
        return afd;
    }

    public boolean hasHapticChannels(int position) {
        return hasHapticChannels(getRingtoneUri(position));
    }

    public static boolean hasHapticChannels(Uri ringtoneUri) {
        return AudioManager.hasHapticChannels(ringtoneUri);
    }

    private static Context createPackageContextAsUser(Context context, int userId) {
        try {
            return context.createPackageContextAsUser(context.getPackageName(), 0, UserHandle.of(userId));
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Unable to create package context", e);
            return null;
        }
    }
}
