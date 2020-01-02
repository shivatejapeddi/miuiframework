package android.app;

import android.annotation.SystemApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Environment;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.provider.BaseColumns;
import android.provider.Downloads.Impl;
import android.provider.Downloads.Impl.RequestHeaders;
import android.provider.MediaStore.Images.Media;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.util.Pair;
import com.android.internal.widget.MessagingMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DownloadManager {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String ACTION_DOWNLOAD_COMPLETE = "android.intent.action.DOWNLOAD_COMPLETE";
    @SystemApi
    public static final String ACTION_DOWNLOAD_COMPLETED = "android.intent.action.DOWNLOAD_COMPLETED";
    public static final String ACTION_DOWNLOAD_DELETED = "android.intent.action.DOWNLOAD_DELETED";
    public static final String ACTION_DOWNLOAD_UPDATED = "android.intent.action.DOWNLOAD_UPDATED";
    public static final String ACTION_DOWNLOAD_UPDATE_PROGRESS_REGISTRATION = "android.intent.action.DOWNLOAD_UPDATE_PROGRESS_REGISTRATION";
    public static final String ACTION_NOTIFICATION_CLICKED = "android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED";
    public static final String ACTION_VIEW_DOWNLOADS = "android.intent.action.VIEW_DOWNLOADS";
    public static final String COLUMN_ALLOW_WRITE = "allow_write";
    public static final String COLUMN_BYTES_DOWNLOADED_SO_FAR = "bytes_so_far";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LAST_MODIFIED_TIMESTAMP = "last_modified_timestamp";
    @Deprecated
    public static final String COLUMN_LOCAL_FILENAME = "local_filename";
    public static final String COLUMN_LOCAL_URI = "local_uri";
    public static final String COLUMN_MEDIAPROVIDER_URI = "mediaprovider_uri";
    public static final String COLUMN_MEDIASTORE_URI = "mediastore_uri";
    public static final String COLUMN_MEDIA_TYPE = "media_type";
    public static final String COLUMN_REASON = "reason";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TOTAL_SIZE_BYTES = "total_size";
    public static final String COLUMN_URI = "uri";
    public static final int ERROR_BLOCKED = 1010;
    public static final int ERROR_CANNOT_RESUME = 1008;
    public static final int ERROR_DEVICE_NOT_FOUND = 1007;
    public static final int ERROR_FILE_ALREADY_EXISTS = 1009;
    public static final int ERROR_FILE_ERROR = 1001;
    public static final int ERROR_HTTP_DATA_ERROR = 1004;
    public static final int ERROR_INSUFFICIENT_SPACE = 1006;
    public static final int ERROR_TOO_MANY_REDIRECTS = 1005;
    public static final int ERROR_UNHANDLED_HTTP_CODE = 1002;
    public static final int ERROR_UNKNOWN = 1000;
    public static final String EXTRA_DOWNLOAD_CURRENT_BYTES = "extra_download_current_bytes";
    public static final String EXTRA_DOWNLOAD_ID = "extra_download_id";
    public static final String EXTRA_DOWNLOAD_STATUS = "extra_download_status";
    public static final String EXTRA_DOWNLOAD_TOTAL_BYTES = "extra_download_total_bytes";
    public static final String EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS = "extra_click_download_ids";
    public static final String INTENT_EXTRAS_SORT_BY_SIZE = "android.app.DownloadManager.extra_sortBySize";
    public static final String INTENT_EXTRA_APPLICATION_PACKAGENAME = "intent_extra_application_packagename";
    public static final String INTENT_EXTRA_REGISTER_DOWNLOADS_UPDATE_PROGRESS = "intent_extra_register_downloads_update_progress";
    public static final String INTENT_EXTRA_UNREGISTER_DOWNLOADS_UPDATE_PROGRESS = "intent_extra_unregister_downloads_update_progress";
    public static final String[] MIUI_UNDERLYING_COLUMNS = ((String[]) concatArrays(UNDERLYING_COLUMNS, new String[]{"entity", ExtraDownloads.COLUMN_FILE_CREATE_TIME, ExtraDownloads.COLUMN_DOWNLOADING_CURRENT_SPEED, ExtraDownloads.COLUMN_DOWNLOAD_SURPLUS_TIME, ExtraDownloads.COLUMN_XL_ACCELERATE_SPEED, ExtraDownloads.COLUMN_DOWNLOADED_TIME, ExtraDownloads.COLUMN_XL_VIP_STATUS, ExtraDownloads.COLUMN_XL_VIP_CDN_URL, ExtraDownloads.COLUMN_XL_TASK_OPEN_MARK, ExtraDownloads.COLUMN_TASK_FOR_THUMBNAIL, ExtraDownloads.COLUMN_APK_PACKGENAME, ExtraDownloads.COLUMN_FILE_HASH, ExtraDownloads.COLUMN_APK_INSTALL_WAY, ExtraDownloads.COLUMN_EXTRA}, String.class));
    private static final String NON_DOWNLOADMANAGER_DOWNLOAD = "non-dwnldmngr-download-dont-retry2download";
    public static final int PAUSED_BY_APP = 5;
    public static final int PAUSED_QUEUED_FOR_WIFI = 3;
    public static final int PAUSED_UNKNOWN = 4;
    public static final int PAUSED_WAITING_FOR_NETWORK = 2;
    public static final int PAUSED_WAITING_TO_RETRY = 1;
    public static final int PAUSE_INSUFFICIENT_SPACE = 6;
    public static final String PERMISSION_SILENCE_INSTALL = "android.permission.XL_SILENCE_INSTALL";
    public static final int STATUS_FAILED = 16;
    public static final int STATUS_PAUSED = 4;
    public static final int STATUS_PENDING = 1;
    public static final int STATUS_RUNNING = 2;
    public static final int STATUS_SUCCESSFUL = 8;
    public static final String[] UNDERLYING_COLUMNS = new String[]{"_id", "_data AS local_filename", "mediaprovider_uri", Impl.COLUMN_DESTINATION, "title", "description", "uri", "status", Impl.COLUMN_FILE_NAME_HINT, "mimetype AS media_type", "total_bytes AS total_size", "lastmod AS last_modified_timestamp", "current_bytes AS bytes_so_far", "allow_write", Impl.COLUMN_ERROR_MSG, Impl.COLUMN_NOTIFICATION_PACKAGE, "'placeholder' AS local_uri", "'placeholder' AS reason"};
    private boolean mAccessFilename;
    private Uri mBaseUri = Impl.CONTENT_URI;
    private final String mPackageName;
    private final ContentResolver mResolver;

    private static class CursorTranslator extends CursorWrapper {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final boolean mAccessFilename;
        private final Uri mBaseUri;

        static {
            Class cls = DownloadManager.class;
        }

        public CursorTranslator(Cursor cursor, Uri baseUri, boolean accessFilename) {
            super(cursor);
            this.mBaseUri = baseUri;
            this.mAccessFilename = accessFilename;
        }

        public int getInt(int columnIndex) {
            return (int) getLong(columnIndex);
        }

        public long getLong(int columnIndex) {
            String str = "status";
            if (getColumnName(columnIndex).equals("reason")) {
                return getReason(super.getInt(getColumnIndex(str)));
            }
            if (getColumnName(columnIndex).equals(str)) {
                return (long) translateStatus(super.getInt(getColumnIndex(str)));
            }
            return super.getLong(columnIndex);
        }

        /* JADX WARNING: Removed duplicated region for block: B:21:0x0043  */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x002b  */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x002b  */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x0043  */
        public java.lang.String getString(int r5) {
            /*
            r4 = this;
            r0 = r4.getColumnName(r5);
            r1 = r0.hashCode();
            r2 = -1204869480; // 0xffffffffb82f2698 float:-4.1759195E-5 double:NaN;
            r3 = 1;
            if (r1 == r2) goto L_0x001e;
        L_0x000e:
            r2 = 22072411; // 0x150cc5b float:3.8350184E-38 double:1.090522E-316;
            if (r1 == r2) goto L_0x0014;
        L_0x0013:
            goto L_0x0028;
        L_0x0014:
            r1 = "local_filename";
            r1 = r0.equals(r1);
            if (r1 == 0) goto L_0x0013;
        L_0x001c:
            r1 = r3;
            goto L_0x0029;
        L_0x001e:
            r1 = "local_uri";
            r1 = r0.equals(r1);
            if (r1 == 0) goto L_0x0013;
        L_0x0026:
            r1 = 0;
            goto L_0x0029;
        L_0x0028:
            r1 = -1;
        L_0x0029:
            if (r1 == 0) goto L_0x0043;
        L_0x002b:
            if (r1 == r3) goto L_0x0032;
        L_0x002d:
            r1 = super.getString(r5);
            return r1;
        L_0x0032:
            r1 = r4.mAccessFilename;
            if (r1 == 0) goto L_0x003b;
        L_0x0036:
            r1 = super.getString(r5);
            return r1;
        L_0x003b:
            r1 = new java.lang.SecurityException;
            r2 = "COLUMN_LOCAL_FILENAME is deprecated; use ContentResolver.openFileDescriptor() instead";
            r1.<init>(r2);
            throw r1;
        L_0x0043:
            r1 = r4.getLocalUri();
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.DownloadManager$CursorTranslator.getString(int):java.lang.String");
        }

        private String getLocalUri() {
            long destinationType = getLong(getColumnIndex(Impl.COLUMN_DESTINATION));
            if (destinationType == 4 || destinationType == 0 || destinationType == 6) {
                String localPath = super.getString(getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                if (localPath == null) {
                    return null;
                }
                return Uri.fromFile(new File(localPath)).toString();
            }
            return ContentUris.withAppendedId(Impl.ALL_DOWNLOADS_CONTENT_URI, getLong(getColumnIndex("_id"))).toString();
        }

        private long getReason(int status) {
            int translateStatus = translateStatus(status);
            if (translateStatus == 4) {
                return getPausedReason(status);
            }
            if (translateStatus != 16) {
                return 0;
            }
            return getErrorCode(status);
        }

        private long getPausedReason(int status) {
            switch (status) {
                case 193:
                    return 5;
                case 194:
                    return 1;
                case 195:
                    return 2;
                case 196:
                    return 3;
                case 198:
                    return 6;
                default:
                    return 4;
            }
        }

        private long getErrorCode(int status) {
            if ((400 <= status && status < 488) || (500 <= status && status < 600)) {
                return (long) status;
            }
            if (status == 198) {
                return 1006;
            }
            if (status == 199) {
                return 1007;
            }
            if (status == 488) {
                return 1009;
            }
            if (status == 489) {
                return 1008;
            }
            if (status == Impl.STATUS_TOO_MANY_REDIRECTS) {
                return 1005;
            }
            switch (status) {
                case 492:
                    return 1001;
                case 493:
                case 494:
                    return 1002;
                case 495:
                    return 1004;
                default:
                    return 1000;
            }
        }

        static int translateStatus(int status) {
            switch (status) {
                case 190:
                    return 1;
                case 192:
                    return 2;
                case 193:
                case 194:
                case 195:
                case 196:
                case 198:
                    return 4;
                case 200:
                    return 8;
                default:
                    return 16;
            }
        }
    }

    public static final class ExtraDownloads implements BaseColumns {
        public static final String COLUMN_APK_INSTALL_WAY = "download_apk_install_way";
        public static final String COLUMN_APK_PACKGENAME = "apk_package_name";
        public static final String COLUMN_APPOINT_NAME = "appointname";
        public static final String COLUMN_DOWNLOADED_TIME = "downloaded_time";
        public static final String COLUMN_DOWNLOADING_CURRENT_SPEED = "downloading_current_speed";
        public static final String COLUMN_DOWNLOAD_SURPLUS_TIME = "download_surplus_time";
        public static final String COLUMN_EXTRA = "download_extra";
        public static final String COLUMN_EXTRA2 = "download_extra2";
        public static final String COLUMN_FILE_CREATE_TIME = "file_create_time";
        public static final String COLUMN_FILE_HASH = "download_file_hash";
        public static final String COLUMN_IF_RANGE_ID = "if_range_id";
        public static final String COLUMN_SUB_DIRECTORY = "subdirectory";
        public static final String COLUMN_TASK_FOR_THUMBNAIL = "download_task_thumbnail";
        public static final String COLUMN_XL_ACCELERATE_SPEED = "xl_accelerate_speed";
        public static final String COLUMN_XL_TASK_OPEN_MARK = "xl_task_open_mark";
        public static final String COLUMN_XL_VIP_CDN_URL = "xl_vip_cdn_url";
        public static final String COLUMN_XL_VIP_STATUS = "xl_vip_status";
        public static final int CONTROL_PAUSED_WITHOUT_WIFI = 2;
    }

    public static class Query {
        public static final int ORDER_ASCENDING = 1;
        public static final int ORDER_DESCENDING = 2;
        private String mAppendedClause;
        private String mColumnAppData;
        private String mColumnNotificationPackage;
        private String mFilterString = null;
        private long[] mIds = null;
        private boolean mOnlyIncludeVisibleInDownloadsUi = false;
        private String mOrderByColumn = Impl.COLUMN_LAST_MODIFICATION;
        private int mOrderDirection = 2;
        private Integer mStatusFlags = null;

        public Query setFilterById(long... ids) {
            this.mIds = ids;
            return this;
        }

        public Query setFilterByString(String filter) {
            this.mFilterString = filter;
            return this;
        }

        public Query setFilterByStatus(int flags) {
            this.mStatusFlags = Integer.valueOf(flags);
            return this;
        }

        public Query setFilterByNotificationPackage(String notificationPackage) {
            this.mColumnNotificationPackage = notificationPackage;
            return this;
        }

        public Query setOnlyIncludeVisibleInDownloadsUi(boolean value) {
            this.mOnlyIncludeVisibleInDownloadsUi = value;
            return this;
        }

        public Query orderBy(String column, int direction) {
            StringBuilder stringBuilder;
            if (direction == 1 || direction == 2) {
                String str = "_id";
                if (column.equals(str)) {
                    this.mOrderByColumn = str;
                    this.mOrderDirection = direction;
                    return this;
                }
                if (column.equals(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)) {
                    this.mOrderByColumn = Impl.COLUMN_LAST_MODIFICATION;
                } else if (column.equals(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)) {
                    this.mOrderByColumn = Impl.COLUMN_TOTAL_BYTES;
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Cannot order by ");
                    stringBuilder.append(column);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
                this.mOrderDirection = direction;
                return this;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid direction: ");
            stringBuilder.append(direction);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        /* Access modifiers changed, original: 0000 */
        public Cursor runQuery(ContentResolver resolver, String[] projection, Uri baseUri) {
            Uri uri = baseUri;
            ArrayList selectionParts = new ArrayList();
            int whereArgsCount = this.mIds;
            whereArgsCount = whereArgsCount == 0 ? 0 : whereArgsCount.length;
            int whereArgsCount2 = this.mFilterString == null ? whereArgsCount : whereArgsCount + 1;
            String[] selectionArgs = new String[whereArgsCount2];
            if (whereArgsCount2 > 0) {
                long[] jArr = this.mIds;
                if (jArr != null) {
                    selectionParts.add(DownloadManager.getWhereClauseForIds(jArr));
                    DownloadManager.getWhereArgsForIds(this.mIds, selectionArgs);
                }
                if (this.mFilterString != null) {
                    selectionParts.add("title LIKE ?");
                    whereArgsCount = selectionArgs.length - 1;
                    StringBuilder stringBuilder = new StringBuilder();
                    String str = "%";
                    stringBuilder.append(str);
                    stringBuilder.append(this.mFilterString);
                    stringBuilder.append(str);
                    selectionArgs[whereArgsCount] = stringBuilder.toString();
                }
            }
            String str2 = " AND ";
            if (this.mStatusFlags != null) {
                List<String> parts = new ArrayList();
                String str3 = "=";
                if ((this.mStatusFlags.intValue() & 1) != 0) {
                    parts.add(statusClause(str3, 190));
                }
                if ((this.mStatusFlags.intValue() & 2) != 0) {
                    parts.add(statusClause(str3, 192));
                }
                if ((this.mStatusFlags.intValue() & 4) != 0) {
                    parts.add(statusClause(str3, 193));
                    parts.add(statusClause(str3, 194));
                    parts.add(statusClause(str3, 195));
                    parts.add(statusClause(str3, 196));
                    parts.add(statusClause(str3, 198));
                }
                if ((this.mStatusFlags.intValue() & 8) != 0) {
                    parts.add(statusClause(str3, 200));
                }
                if ((this.mStatusFlags.intValue() & 16) != 0) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("(");
                    stringBuilder2.append(statusClause(">=", 400));
                    stringBuilder2.append(str2);
                    stringBuilder2.append(statusClause("<", 600));
                    stringBuilder2.append(")");
                    parts.add(stringBuilder2.toString());
                }
                selectionParts.add(joinStrings(" OR ", parts));
            }
            if (this.mOnlyIncludeVisibleInDownloadsUi) {
                selectionParts.add("is_visible_in_downloads_ui != '0'");
            }
            addExtraSelectionParts(selectionParts);
            selectionParts.add("deleted != '1'");
            String selection = joinStrings(str2, selectionParts);
            String orderDirection = this.mOrderDirection == 1 ? "ASC" : "DESC";
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(this.mOrderByColumn);
            stringBuilder3.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder3.append(orderDirection);
            return resolver.query(uri, projection, selection, selectionArgs, stringBuilder3.toString());
        }

        private String joinStrings(String joiner, Iterable<String> parts) {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (String part : parts) {
                if (!first) {
                    builder.append(joiner);
                }
                builder.append(part);
                first = false;
            }
            return builder.toString();
        }

        private String statusClause(String operator, int value) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("status");
            stringBuilder.append(operator);
            String str = "'";
            stringBuilder.append(str);
            stringBuilder.append(value);
            stringBuilder.append(str);
            return stringBuilder.toString();
        }

        public Query setFilterByAppData(String appData) {
            this.mColumnAppData = appData;
            return this;
        }

        public Query setFilterByAppendedClause(String appendedClause) {
            this.mAppendedClause = appendedClause;
            return this;
        }

        /* Access modifiers changed, original: 0000 */
        public void addExtraSelectionParts(List<String> selectionParts) {
            String str = "%s='%s'";
            if (!TextUtils.isEmpty(this.mColumnAppData)) {
                selectionParts.add(String.format(str, new Object[]{"entity", this.mColumnAppData}));
            }
            if (!TextUtils.isEmpty(this.mColumnNotificationPackage)) {
                selectionParts.add(String.format(str, new Object[]{Impl.COLUMN_NOTIFICATION_PACKAGE, this.mColumnNotificationPackage}));
            }
            if (!TextUtils.isEmpty(this.mAppendedClause)) {
                selectionParts.add(this.mAppendedClause);
            }
        }
    }

    public static class Request {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        public static final int INSTALL_WAY_Manual = 2;
        public static final int INSTALL_WAY_NONE = 0;
        public static final int INSTALL_WAY_SILENCE = 1;
        @Deprecated
        public static final int NETWORK_BLUETOOTH = 4;
        public static final int NETWORK_MOBILE = 1;
        public static final int NETWORK_WIFI = 2;
        private static final int SCANNABLE_VALUE_NO = 2;
        private static final int SCANNABLE_VALUE_YES = 0;
        public static final int VISIBILITY_HIDDEN = 2;
        public static final int VISIBILITY_VISIBLE = 0;
        public static final int VISIBILITY_VISIBLE_NOTIFY_COMPLETED = 1;
        public static final int VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION = 3;
        private int mAllowedNetworkTypes = -1;
        private String mApkPackageName;
        private String mAppointName;
        private boolean mBypassRecommendedSizeLimit;
        private String mColumnAppData;
        private CharSequence mDescription;
        private Uri mDestinationUri;
        private String mExtra;
        private String mExtra2;
        private String mFileHash;
        private Uri mFileIconUri;
        private long mFileSize = -1;
        private int mFlags = 0;
        private int mInstallWay = 0;
        private boolean mIsVisibleInDownloadsUi = true;
        private boolean mMeteredAllowed = true;
        private String mMimeType;
        private String mNotificationClass;
        private int mNotificationVisibility = 0;
        private List<Pair<String, String>> mRequestHeaders = new ArrayList();
        private boolean mRoamingAllowed = true;
        private boolean mScannable = false;
        private CharSequence mTitle;
        private Uri mUri;
        private boolean mUseSystemCache = false;
        private String mUserAgent;
        private int mXlVipStatus = 0;

        static {
            Class cls = DownloadManager.class;
        }

        public Request(Uri uri) {
            checkUri(uri);
            this.mUri = uri;
        }

        private void checkUri(Uri uri) throws IllegalArgumentException {
            if (uri != null) {
                String newScheme = uri.getScheme();
                if (newScheme != null) {
                    String scheme = newScheme.toLowerCase();
                    if (!scheme.equals(IntentFilter.SCHEME_HTTP) && !scheme.equals(IntentFilter.SCHEME_HTTPS) && !scheme.equals("ftp") && !scheme.equals("ed2k") && !scheme.equals("magnet")) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Can not handle uri:: ");
                        stringBuilder.append(uri);
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                    return;
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Can not handle uri: ");
                stringBuilder2.append(uri);
                throw new IllegalArgumentException(stringBuilder2.toString());
            }
            throw new IllegalArgumentException("uri is null");
        }

        Request(String uriString) {
            this.mUri = Uri.parse(uriString);
        }

        public Request setDestinationUri(Uri uri) {
            this.mDestinationUri = uri;
            return this;
        }

        public Request setDestinationToSystemCache() {
            this.mUseSystemCache = true;
            return this;
        }

        public Request setDestinationInExternalFilesDir(Context context, String dirType, String subPath) {
            File file = context.getExternalFilesDir(dirType);
            if (file != null) {
                StringBuilder stringBuilder;
                if (file.exists()) {
                    if (!file.isDirectory()) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(file.getAbsolutePath());
                        stringBuilder.append(" already exists and is not a directory");
                        throw new IllegalStateException(stringBuilder.toString());
                    }
                } else if (!file.mkdirs()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to create directory: ");
                    stringBuilder.append(file.getAbsolutePath());
                    throw new IllegalStateException(stringBuilder.toString());
                }
                setDestinationFromBase(file, subPath);
                return this;
            }
            throw new IllegalStateException("Failed to get external storage files directory");
        }

        /* JADX WARNING: Missing block: B:28:0x008c, code skipped:
            if (r2 != null) goto L_0x008e;
     */
        /* JADX WARNING: Missing block: B:30:?, code skipped:
            r2.close();
     */
        public android.app.DownloadManager.Request setDestinationInExternalPublicDir(java.lang.String r8, java.lang.String r9) {
            /*
            r7 = this;
            r0 = android.os.Environment.getExternalStoragePublicDirectory(r8);
            if (r0 == 0) goto L_0x00b1;
        L_0x0006:
            r1 = android.app.AppGlobals.getInitialApplication();
            r2 = r1.getApplicationInfo();
            r2 = r2.targetSdkVersion;
            r3 = 29;
            r4 = "Unable to create directory: ";
            if (r2 >= r3) goto L_0x0065;
        L_0x0016:
            r2 = android.os.Environment.isExternalStorageLegacy();
            if (r2 != 0) goto L_0x001d;
        L_0x001c:
            goto L_0x0065;
        L_0x001d:
            r2 = r0.exists();
            if (r2 == 0) goto L_0x0045;
        L_0x0023:
            r2 = r0.isDirectory();
            if (r2 == 0) goto L_0x002a;
        L_0x0029:
            goto L_0x0085;
        L_0x002a:
            r2 = new java.lang.IllegalStateException;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = r0.getAbsolutePath();
            r3.append(r4);
            r4 = " already exists and is not a directory";
            r3.append(r4);
            r3 = r3.toString();
            r2.<init>(r3);
            throw r2;
        L_0x0045:
            r2 = r0.mkdirs();
            if (r2 == 0) goto L_0x004c;
        L_0x004b:
            goto L_0x0085;
        L_0x004c:
            r2 = new java.lang.IllegalStateException;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r3.append(r4);
            r4 = r0.getAbsolutePath();
            r3.append(r4);
            r3 = r3.toString();
            r2.<init>(r3);
            throw r2;
        L_0x0065:
            r2 = r1.getContentResolver();	 Catch:{ RemoteException -> 0x0097 }
            r3 = "downloads";
            r2 = r2.acquireContentProviderClient(r3);	 Catch:{ RemoteException -> 0x0097 }
            r3 = new android.os.Bundle;	 Catch:{ all -> 0x0089 }
            r3.<init>();	 Catch:{ all -> 0x0089 }
            r5 = "dir_type";
            r3.putString(r5, r8);	 Catch:{ all -> 0x0089 }
            r5 = "create_external_public_dir";
            r6 = 0;
            r2.call(r5, r6, r3);	 Catch:{ all -> 0x0089 }
            r2.close();	 Catch:{ RemoteException -> 0x0097 }
        L_0x0085:
            r7.setDestinationFromBase(r0, r9);
            return r7;
        L_0x0089:
            r3 = move-exception;
            throw r3;	 Catch:{ all -> 0x008b }
        L_0x008b:
            r5 = move-exception;
            if (r2 == 0) goto L_0x0096;
        L_0x008e:
            r2.close();	 Catch:{ all -> 0x0092 }
            goto L_0x0096;
        L_0x0092:
            r6 = move-exception;
            r3.addSuppressed(r6);	 Catch:{ RemoteException -> 0x0097 }
        L_0x0096:
            throw r5;	 Catch:{ RemoteException -> 0x0097 }
        L_0x0097:
            r2 = move-exception;
            r3 = new java.lang.IllegalStateException;
            r5 = new java.lang.StringBuilder;
            r5.<init>();
            r5.append(r4);
            r4 = r0.getAbsolutePath();
            r5.append(r4);
            r4 = r5.toString();
            r3.<init>(r4);
            throw r3;
        L_0x00b1:
            r1 = new java.lang.IllegalStateException;
            r2 = "Failed to get external storage public directory";
            r1.<init>(r2);
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.DownloadManager$Request.setDestinationInExternalPublicDir(java.lang.String, java.lang.String):android.app.DownloadManager$Request");
        }

        private void setDestinationFromBase(File base, String subPath) {
            if (subPath != null) {
                this.mDestinationUri = Uri.withAppendedPath(Uri.fromFile(base), subPath);
                return;
            }
            throw new NullPointerException("subPath cannot be null");
        }

        public void allowScanningByMediaScanner() {
            this.mScannable = true;
        }

        public Request addRequestHeader(String header, String value) {
            if (header == null) {
                throw new NullPointerException("header cannot be null");
            } else if (header.contains(":")) {
                throw new IllegalArgumentException("header may not contain ':'");
            } else {
                Object value2;
                if (value2 == null) {
                    value2 = "";
                }
                this.mRequestHeaders.add(Pair.create(header, value2));
                return this;
            }
        }

        public Request setTitle(CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public Request setDescription(CharSequence description) {
            this.mDescription = description;
            return this;
        }

        public Request setMimeType(String mimeType) {
            this.mMimeType = mimeType;
            return this;
        }

        @Deprecated
        public Request setShowRunningNotification(boolean show) {
            if (show) {
                return setNotificationVisibility(0);
            }
            return setNotificationVisibility(2);
        }

        public Request setNotificationVisibility(int visibility) {
            this.mNotificationVisibility = visibility;
            return this;
        }

        public Request setAllowedNetworkTypes(int flags) {
            this.mAllowedNetworkTypes = flags;
            return this;
        }

        public Request setAllowedOverRoaming(boolean allowed) {
            this.mRoamingAllowed = allowed;
            return this;
        }

        public Request setAllowedOverMetered(boolean allow) {
            this.mMeteredAllowed = allow;
            return this;
        }

        public Request setRequiresCharging(boolean requiresCharging) {
            if (requiresCharging) {
                this.mFlags |= 1;
            } else {
                this.mFlags &= -2;
            }
            return this;
        }

        public Request setRequiresDeviceIdle(boolean requiresDeviceIdle) {
            if (requiresDeviceIdle) {
                this.mFlags |= 2;
            } else {
                this.mFlags &= -3;
            }
            return this;
        }

        public Request setVisibleInDownloadsUi(boolean isVisible) {
            this.mIsVisibleInDownloadsUi = isVisible;
            return this;
        }

        /* Access modifiers changed, original: 0000 */
        public ContentValues toContentValues(String packageName) {
            ContentValues values = new ContentValues();
            values.put("uri", this.mUri.toString());
            values.put(Impl.COLUMN_IS_PUBLIC_API, Boolean.valueOf(true));
            values.put(Impl.COLUMN_NOTIFICATION_PACKAGE, packageName);
            Uri uri = this.mDestinationUri;
            int i = 2;
            String str = Impl.COLUMN_DESTINATION;
            if (uri != null) {
                values.put(str, Integer.valueOf(4));
                values.put(Impl.COLUMN_FILE_NAME_HINT, this.mDestinationUri.toString());
            } else {
                int i2;
                if (this.mUseSystemCache) {
                    i2 = 5;
                } else {
                    i2 = 2;
                }
                values.put(str, Integer.valueOf(i2));
            }
            if (this.mScannable) {
                i = 0;
            }
            values.put(Impl.COLUMN_MEDIA_SCANNED, Integer.valueOf(i));
            if (!this.mRequestHeaders.isEmpty()) {
                encodeHttpHeaders(values);
            }
            putIfNonNull(values, "title", this.mTitle);
            putIfNonNull(values, "description", this.mDescription);
            putIfNonNull(values, "mimetype", this.mMimeType);
            values.put("visibility", Integer.valueOf(this.mNotificationVisibility));
            values.put(Impl.COLUMN_ALLOWED_NETWORK_TYPES, Integer.valueOf(this.mAllowedNetworkTypes));
            values.put(Impl.COLUMN_ALLOW_ROAMING, Boolean.valueOf(this.mRoamingAllowed));
            values.put("allow_metered", Boolean.valueOf(this.mMeteredAllowed));
            values.put("flags", Integer.valueOf(this.mFlags));
            values.put(Impl.COLUMN_IS_VISIBLE_IN_DOWNLOADS_UI, Boolean.valueOf(this.mIsVisibleInDownloadsUi));
            putIfNonNull(values, "entity", this.mColumnAppData);
            putIfNonNull(values, ExtraDownloads.COLUMN_APPOINT_NAME, this.mAppointName);
            putIfNonNull(values, Impl.COLUMN_NOTIFICATION_CLASS, this.mNotificationClass);
            putIfNonNull(values, Impl.COLUMN_USER_AGENT, this.mUserAgent);
            values.put(Impl.COLUMN_TOTAL_BYTES, Long.valueOf(this.mFileSize));
            putIfNonNull(values, ExtraDownloads.COLUMN_TASK_FOR_THUMBNAIL, this.mFileIconUri);
            putIfNonNull(values, ExtraDownloads.COLUMN_EXTRA2, this.mExtra2);
            putIfNonNull(values, ExtraDownloads.COLUMN_APK_PACKGENAME, this.mApkPackageName);
            putIfNonNull(values, ExtraDownloads.COLUMN_FILE_HASH, this.mFileHash);
            values.put(ExtraDownloads.COLUMN_APK_INSTALL_WAY, Integer.valueOf(this.mInstallWay));
            putIfNonNull(values, ExtraDownloads.COLUMN_EXTRA, this.mExtra);
            return values;
        }

        private void encodeHttpHeaders(ContentValues values) {
            int index = 0;
            for (Pair<String, String> header : this.mRequestHeaders) {
                String headerString = new StringBuilder();
                headerString.append((String) header.first);
                headerString.append(": ");
                headerString.append((String) header.second);
                headerString = headerString.toString();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(RequestHeaders.INSERT_KEY_PREFIX);
                stringBuilder.append(index);
                values.put(stringBuilder.toString(), headerString);
                index++;
            }
        }

        private void putIfNonNull(ContentValues contentValues, String key, Object value) {
            if (value != null) {
                contentValues.put(key, value.toString());
            }
        }

        public Request setAppointName(String appointName) {
            this.mAppointName = appointName;
            return this;
        }

        public Request setAppData(String appData) {
            this.mColumnAppData = appData;
            return this;
        }

        public Request setNotificationClass(String notificationClass) {
            this.mNotificationClass = notificationClass;
            return this;
        }

        public void setUserAgent(String agent) {
            this.mUserAgent = agent;
        }

        public void setBypassRecommendedSizeLimit(boolean bypass) {
            this.mBypassRecommendedSizeLimit = bypass;
        }

        public void setXlVipStatus(int vipstatus) {
            this.mXlVipStatus = vipstatus;
        }

        public void setFileHash(String fileHash) {
            this.mFileHash = fileHash;
        }

        public void setInstallWay(int installWay) {
            this.mInstallWay = installWay;
        }

        public void setExtra(String extra) {
            this.mExtra = extra;
        }

        public Request setFileSize(long fileSize) {
            this.mFileSize = fileSize;
            return this;
        }

        public Request setFileIconUri(Uri uri) {
            this.mFileIconUri = uri;
            return this;
        }

        public Request setExtra2(String jsonStr) {
            this.mExtra2 = jsonStr;
            return this;
        }

        public Request setApkPackageName(String packageName) {
            this.mApkPackageName = packageName;
            return this;
        }
    }

    public DownloadManager(Context context) {
        this.mResolver = context.getContentResolver();
        this.mPackageName = context.getPackageName();
        this.mAccessFilename = context.getApplicationInfo().targetSdkVersion < 24;
    }

    public void setAccessAllDownloads(boolean accessAllDownloads) {
        if (accessAllDownloads) {
            this.mBaseUri = Impl.ALL_DOWNLOADS_CONTENT_URI;
        } else {
            this.mBaseUri = Impl.CONTENT_URI;
        }
    }

    public void setAccessFilename(boolean accessFilename) {
        this.mAccessFilename = accessFilename;
    }

    public long enqueue(Request request) {
        return Long.parseLong(this.mResolver.insert(Impl.CONTENT_URI, request.toContentValues(this.mPackageName)).getLastPathSegment());
    }

    public int markRowDeleted(long... ids) {
        if (ids == null || ids.length == 0) {
            throw new IllegalArgumentException("input param 'ids' can't be null");
        }
        ContentValues values = new ContentValues();
        values.put("deleted", Integer.valueOf(1));
        if (ids.length == 1) {
            return this.mResolver.update(ContentUris.withAppendedId(this.mBaseUri, ids[0]), values, null, null);
        }
        return this.mResolver.update(this.mBaseUri, values, getWhereClauseForIds(ids), getWhereArgsForIds(ids));
    }

    public int remove(long... ids) {
        return markRowDeleted(ids);
    }

    public Cursor query(Query query) {
        Cursor underlyingCursor = query.runQuery(this.mResolver, MIUI_UNDERLYING_COLUMNS, this.mBaseUri);
        if (underlyingCursor == null) {
            return null;
        }
        return new CursorTranslator(underlyingCursor, this.mBaseUri, this.mAccessFilename);
    }

    public ParcelFileDescriptor openDownloadedFile(long id) throws FileNotFoundException {
        return this.mResolver.openFileDescriptor(getDownloadUri(id), "r");
    }

    public Uri getUriForDownloadedFile(long id) {
        Cursor cursor = null;
        try {
            cursor = query(new Query().setFilterById(id));
            if (cursor == null) {
                return null;
            }
            if (cursor.moveToFirst() && 8 == cursor.getInt(cursor.getColumnIndexOrThrow("status"))) {
                Uri withAppendedId = ContentUris.withAppendedId(Impl.ALL_DOWNLOADS_CONTENT_URI, id);
                cursor.close();
                return withAppendedId;
            }
            cursor.close();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getMimeTypeForDownloadedFile(long id) {
        Cursor cursor = null;
        try {
            cursor = query(new Query().setFilterById(id));
            if (cursor == null) {
                return null;
            }
            if (cursor.moveToFirst()) {
                String string = cursor.getString(cursor.getColumnIndexOrThrow("media_type"));
                cursor.close();
                return string;
            }
            cursor.close();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void restartDownload(long... ids) {
        Cursor cursor = query(new Query().setFilterById(ids));
        try {
            String str;
            cursor.moveToFirst();
            while (true) {
                str = "status";
                if (cursor.isAfterLast()) {
                    break;
                }
                int status = cursor.getInt(cursor.getColumnIndex(str));
                if (status != 8) {
                    if (status != 16) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Cannot restart incomplete download: ");
                        stringBuilder.append(cursor.getLong(cursor.getColumnIndex("_id")));
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                }
                cursor.moveToNext();
            }
            ContentValues values = new ContentValues();
            values.put(Impl.COLUMN_CURRENT_BYTES, Integer.valueOf(0));
            values.put(Impl.COLUMN_TOTAL_BYTES, Integer.valueOf(-1));
            values.putNull("_data");
            values.put(str, Integer.valueOf(190));
            values.put(Impl.COLUMN_FAILED_CONNECTIONS, Integer.valueOf(0));
            this.mResolver.update(this.mBaseUri, values, getWhereClauseForIds(ids), getWhereArgsForIds(ids));
        } finally {
            cursor.close();
        }
    }

    public void forceDownload(long... ids) {
        ContentValues values = new ContentValues();
        values.put("status", Integer.valueOf(190));
        values.put(Impl.COLUMN_CONTROL, Integer.valueOf(0));
        values.put(Impl.COLUMN_BYPASS_RECOMMENDED_SIZE_LIMIT, Integer.valueOf(1));
        this.mResolver.update(this.mBaseUri, values, getWhereClauseForIds(ids), getWhereArgsForIds(ids));
    }

    public static Long getMaxBytesOverMobile(Context context) {
        try {
            return Long.valueOf(Global.getLong(context.getContentResolver(), Global.DOWNLOAD_MAX_BYTES_OVER_MOBILE));
        } catch (SettingNotFoundException e) {
            return null;
        }
    }

    public boolean rename(Context context, long id, String displayName) {
        long j = id;
        String str = displayName;
        Context context2;
        StringBuilder stringBuilder;
        if (FileUtils.isValidFatFilename(displayName)) {
            Cursor cursor = null;
            String oldDisplayName = null;
            String mimeType = null;
            try {
                cursor = query(new Query().setFilterById(new long[]{j}));
                if (cursor == null) {
                    if (cursor != null) {
                        cursor.close();
                    }
                    return false;
                }
                String str2 = "title";
                if (cursor.moveToFirst()) {
                    if (8 != cursor.getInt(cursor.getColumnIndexOrThrow("status"))) {
                        cursor.close();
                        return false;
                    }
                    oldDisplayName = cursor.getString(cursor.getColumnIndexOrThrow(str2));
                    mimeType = cursor.getString(cursor.getColumnIndexOrThrow("media_type"));
                }
                cursor.close();
                if (oldDisplayName == null || mimeType == null) {
                    context2 = context;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Document with id ");
                    stringBuilder.append(j);
                    stringBuilder.append(" does not exist");
                    throw new IllegalStateException(stringBuilder.toString());
                }
                File parent = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File before = new File(parent, oldDisplayName);
                File after = new File(parent, str);
                if (after.exists()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Already exists ");
                    stringBuilder.append(after);
                    throw new IllegalStateException(stringBuilder.toString());
                } else if (before.renameTo(after)) {
                    if (mimeType.startsWith(MessagingMessage.IMAGE_MIME_TYPE_PREFIX)) {
                        context.getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, "_data=?", new String[]{before.getAbsolutePath()});
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(after));
                        context.sendBroadcast(intent);
                    } else {
                        context2 = context;
                    }
                    ContentValues values = new ContentValues();
                    values.put(str2, str);
                    values.put("_data", after.toString());
                    values.putNull("mediaprovider_uri");
                    long[] ids = new long[1];
                    boolean z = false;
                    ids[0] = j;
                    if (this.mResolver.update(this.mBaseUri, values, getWhereClauseForIds(ids), getWhereArgsForIds(ids)) == 1) {
                        z = true;
                    }
                    return z;
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to rename to ");
                    stringBuilder.append(after);
                    throw new IllegalStateException(stringBuilder.toString());
                }
            } catch (Throwable th) {
                context2 = context;
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else {
            context2 = context;
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(" is not a valid filename");
            throw new SecurityException(stringBuilder.toString());
        }
    }

    public static Long getRecommendedMaxBytesOverMobile(Context context) {
        try {
            return Long.valueOf(Global.getLong(context.getContentResolver(), Global.DOWNLOAD_RECOMMENDED_MAX_BYTES_OVER_MOBILE));
        } catch (SettingNotFoundException e) {
            return null;
        }
    }

    public static boolean isActiveNetworkExpensive(Context context) {
        return false;
    }

    public static long getActiveNetworkWarningBytes(Context context) {
        return -1;
    }

    public long addCompletedDownload(String title, String description, boolean isMediaScannerScannable, String mimeType, String path, long length, boolean showNotification) {
        return addCompletedDownload(title, description, isMediaScannerScannable, mimeType, path, length, showNotification, false, null, null);
    }

    public long addCompletedDownload(String title, String description, boolean isMediaScannerScannable, String mimeType, String path, long length, boolean showNotification, Uri uri, Uri referer) {
        return addCompletedDownload(title, description, isMediaScannerScannable, mimeType, path, length, showNotification, false, uri, referer);
    }

    public long addCompletedDownload(String title, String description, boolean isMediaScannerScannable, String mimeType, String path, long length, boolean showNotification, boolean allowWrite) {
        return addCompletedDownload(title, description, isMediaScannerScannable, mimeType, path, length, showNotification, allowWrite, null, null);
    }

    public long addCompletedDownload(String title, String description, boolean isMediaScannerScannable, String mimeType, String path, long length, boolean showNotification, boolean allowWrite, Uri uri, Uri referer) {
        String str = title;
        String str2 = description;
        String str3 = mimeType;
        String str4 = path;
        Uri uri2 = uri;
        validateArgumentIsNonEmpty("title", title);
        validateArgumentIsNonEmpty("description", description);
        validateArgumentIsNonEmpty("path", str4);
        validateArgumentIsNonEmpty("mimeType", mimeType);
        if (length >= 0) {
            Request request;
            int i;
            if (uri2 != null) {
                request = new Request(uri2);
            } else {
                request = new Request(NON_DOWNLOADMANAGER_DOWNLOAD);
            }
            request.setTitle(title).setDescription(description).setMimeType(mimeType);
            if (referer != null) {
                request.addRequestHeader("Referer", referer.toString());
            }
            ContentValues values = request.toContentValues(null);
            values.put(Impl.COLUMN_DESTINATION, Integer.valueOf(6));
            values.put("_data", str4);
            values.put("status", Integer.valueOf(200));
            values.put(Impl.COLUMN_TOTAL_BYTES, Long.valueOf(length));
            int i2 = 2;
            if (isMediaScannerScannable) {
                i = 0;
            } else {
                i = 2;
            }
            values.put(Impl.COLUMN_MEDIA_SCANNED, Integer.valueOf(i));
            if (showNotification) {
                i2 = 3;
            }
            values.put("visibility", Integer.valueOf(i2));
            values.put("allow_write", Integer.valueOf(allowWrite));
            Uri downloadUri = this.mResolver.insert(Impl.CONTENT_URI, values);
            if (downloadUri == null) {
                return -1;
            }
            return Long.parseLong(downloadUri.getLastPathSegment());
        }
        throw new IllegalArgumentException(" invalid value for param: totalBytes");
    }

    private static void validateArgumentIsNonEmpty(String paramName, String val) {
        if (TextUtils.isEmpty(val)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(paramName);
            stringBuilder.append(" can't be null");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public Uri getDownloadUri(long id) {
        return ContentUris.withAppendedId(Impl.ALL_DOWNLOADS_CONTENT_URI, id);
    }

    static String getWhereClauseForIds(long[] ids) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("(");
        for (int i = 0; i < ids.length; i++) {
            if (i > 0) {
                whereClause.append("OR ");
            }
            whereClause.append("_id");
            whereClause.append(" = ? ");
        }
        whereClause.append(")");
        return whereClause.toString();
    }

    static String[] getWhereArgsForIds(long[] ids) {
        return getWhereArgsForIds(ids, new String[ids.length]);
    }

    static String[] getWhereArgsForIds(long[] ids, String[] args) {
        for (int i = 0; i < ids.length; i++) {
            args[i] = Long.toString(ids[i]);
        }
        return args;
    }

    public int removeRecordOnly(long... ids) {
        if (ids != null && ids.length != 0) {
            return this.mResolver.delete(this.mBaseUri, getWhereClauseForIds(ids), getWhereArgsForIds(ids));
        }
        throw new IllegalArgumentException("input param 'ids' can't be null");
    }

    public void pauseDownload(long... ids) {
        ContentValues values = new ContentValues();
        values.put("status", Integer.valueOf(193));
        values.put(Impl.COLUMN_CONTROL, Integer.valueOf(1));
        String whereClause = new StringBuilder();
        whereClause.append("( ");
        whereClause.append(getWhereClauseForIds(ids));
        whereClause.append(" AND ");
        String str = "=";
        whereClause.append(getWhereClauseForStatuses(new String[]{str, str}, new String[]{"OR"}));
        whereClause.append(")");
        this.mResolver.update(this.mBaseUri, values, whereClause.toString(), (String[]) concatArrays(getWhereArgsForIds(ids), getWhereArgsForStatuses(new int[]{190, 192}), String.class));
    }

    public void resumeDownload(long... ids) {
        ContentValues values = new ContentValues();
        addRunningStatusAndControlRun(values);
        String whereClause = new StringBuilder();
        whereClause.append("( ");
        whereClause.append(getWhereClauseForIds(ids));
        whereClause.append(" AND ");
        String str = "=";
        String str2 = "OR";
        whereClause.append(getWhereClauseForStatuses(new String[]{str, str, str, str}, new String[]{str2, str2, str2}));
        whereClause.append(")");
        this.mResolver.update(this.mBaseUri, values, whereClause.toString(), (String[]) concatArrays(getWhereArgsForIds(ids), getWhereArgsForStatuses(new int[]{193, 194, 195, 196}), String.class));
    }

    private static <T> T[] concatArrays(T[] src1, T[] src2, Class<T> type) {
        Object[] dst = (Object[]) Array.newInstance(type, src1.length + src2.length);
        System.arraycopy(src1, 0, dst, 0, src1.length);
        System.arraycopy(src2, 0, dst, src1.length, src2.length);
        return dst;
    }

    public static void setDownloadUpdateProgressRegistration(Context context, long[] registerIds, long[] unregisterIds) {
        Intent intent = new Intent(ACTION_DOWNLOAD_UPDATE_PROGRESS_REGISTRATION);
        intent.putExtra(INTENT_EXTRA_REGISTER_DOWNLOADS_UPDATE_PROGRESS, registerIds);
        intent.putExtra(INTENT_EXTRA_UNREGISTER_DOWNLOADS_UPDATE_PROGRESS, unregisterIds);
        context.sendBroadcast(intent);
    }

    private static String getWhereClauseForStatuses(String[] operators, String[] jointConditions) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("(");
        for (int i = 0; i < operators.length; i++) {
            StringBuilder stringBuilder;
            String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
            if (i > 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(jointConditions[i - 1]);
                stringBuilder.append(str);
                whereClause.append(stringBuilder.toString());
            }
            whereClause.append("status");
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(operators[i]);
            stringBuilder.append(" ? ");
            whereClause.append(stringBuilder.toString());
        }
        whereClause.append(")");
        return whereClause.toString();
    }

    private static String[] getWhereArgsForStatuses(int[] statuses) {
        String[] whereArgs = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            whereArgs[i] = Integer.toString(statuses[i]);
        }
        return whereArgs;
    }

    public static void addRunningStatusAndControlRun(ContentValues values) {
        if (values != null) {
            values.put("status", Integer.valueOf(192));
            values.put(Impl.COLUMN_CONTROL, Integer.valueOf(0));
        }
    }

    public static boolean isDownloading(Cursor cursor) {
        int status = cursor.getInt(cursor.getColumnIndex("status"));
        if (status == 1 || status == 2) {
            return true;
        }
        return false;
    }

    public static int getDownloadStatus(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex("status"));
    }

    public static boolean isDownloadSuccess(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex("status")) == 8;
    }

    public static int translateStatus(int downloadsStatus) {
        return CursorTranslator.translateStatus(downloadsStatus);
    }

    public static boolean setRecommendedMaxBytesOverMobile(Context context, long size) {
        return Global.putLong(context.getContentResolver(), Global.DOWNLOAD_RECOMMENDED_MAX_BYTES_OVER_MOBILE, size);
    }
}
