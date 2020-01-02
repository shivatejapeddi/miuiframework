package miui.provider;

import android.app.backup.FullBackup;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import miui.os.Environment;
import miui.os.FileUtils;

public class Recordings {
    public static final String AUTHORITY = "records";
    public static final String CALL_RECORD_DIR;
    public static final String FM_RECORD_DIR;
    private static final String[] HEXDIGITS = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", FullBackup.APK_TREE_TOKEN, "b", FullBackup.CACHE_TREE_TOKEN, "d", "e", FullBackup.FILES_TREE_TOKEN};
    private static final String MEDIA_SCANNER_CLASS = "com.android.providers.media.MediaScannerReceiver";
    private static final String MEDIA_SCANNER_CLASS_Q = "com.android.providers.media.MediaReceiver";
    private static final String MEDIA_SCANNER_PACKAGE = "com.android.providers.media";
    public static final String RECORDER_ROOT_PATH;
    public static final String SAMPLE_DEFAULT_DIR = "/sound_recorder";
    private static final String TAG = "SoundRecorder:SoundRecorder";

    public static class CachedAccount {
        public static final Uri CONTENT_URI = Uri.parse("content://records/cached_account");
        public static final String TABLE_NAME = "cached_account";
        public static final String URI_PATH = "cached_account";

        public static final class Columns implements BaseColumns {
            public static final String ACCOUNT_NAME = "account_name";
        }
    }

    public static class CallRecords {
        public static final Uri CONTENT_URI = Uri.parse("content://records/call_records");
        public static final String TABLE_NAME = "call_records";
        public static final String URI_PATH = "call_records";

        public static final class Columns implements BaseColumns {
            public static final String NUMBER = "number";
            public static final String RECORD_ID = "record_id";
        }
    }

    public static class CallRecordsView {
        public static final Uri CONTENT_URI = Uri.parse("content://records/call_records_view");
        public static final String URI_PATH = "call_records_view";
        public static final String VIEW_NAME = "call_records_view";
    }

    public static class Downloads {
        public static final Uri CONTENT_URI = Uri.parse("content://records/downloads");
        public static final String TABLE_NAME = "downloads";
        public static final String URI_PATH = "downloads";

        public static final class Columns implements BaseColumns {
            public static final String FILE_ID = "file_id";
            public static final String FILE_NAME = "file_name";
            public static final String FILE_PATH = "file_path";
            public static final String FILE_SIZE = "file_size";
            public static final String PROGRESS = "progress";
            public static final String REC_ID = "rec_id";
            public static final String STATUS = "status";
        }

        public static final class Status {
            public static final int Downloading = 1;
            public static final int Failed = 4;
            public static final int Paused = 3;
            public static final int Pendding = 2;
            public static final int PenddingByNetwork = 5;
            public static final int Success = 0;
        }
    }

    public static class MarkPoints {
        public static final Uri CONTENT_URI = Uri.parse("content://records/mark_points");
        public static final String TABLE_NAME = "mark_points";
        public static final String URI_PATH = "mark_points";

        public static final class Columns implements BaseColumns {
            public static final String DESCRIPTION = "desp";
            public static final String E_TAG = "e_tag";
            public static final String FILE_SHA1 = "file_sha1";
            public static final String PATH = "path";
            public static final String RECORD_ID = "record_id";
            public static final String SYNC_DIRTY = "sync_dirty";
            public static final String TIME_POINT = "time_point";
            public static final String TYPE = "type";
        }

        public static final class SyncDirty {
            public static final int DIRTY = 1;
            public static final int SYNCED = 0;
            public static final int SYNC_ERROR = 2;
        }
    }

    public static class MarkpointsOperations {
        public static final Uri CONTENT_URI = Uri.parse("content://records/markpoint_operations");
        public static final String TABLE_NAME = "markpoint_operations";
        public static final String URI_PATH = "markpoint_operations";

        public static final class Columns implements BaseColumns {
            public static final String CLOUD_RECORD_ID = "cloud_record_id";
            public static final String E_TAG = "e_tag";
            public static final String OPER = "oper";
            public static final String REC_ID = "rec_id";
        }

        public static final class Opers {
            public static final int ADD = 103;
            public static final int DELETE = 101;
            public static final int UPDATE = 102;
        }
    }

    public static class Operations {
        public static final Uri CONTENT_URI = Uri.parse("content://records/operations");
        public static final String TABLE_NAME = "operations";
        public static final String URI_PATH = "operations";

        public static final class Columns implements BaseColumns {
            public static final String DESC = "decs";
            public static final String FILE_ID = "file_id";
            public static final String OPER = "oper";
            public static final String REC_ID = "rec_id";
        }

        public static final class Opers {
            public static final int DELETE = 0;
            public static final int RENAME = 1;
        }
    }

    public static class RecordingNotifications {
        public static final Uri CONTENT_URI = Uri.parse("content://records/recordingnotifications");
        public static final String EXTRA_DIRPATH = "extra_dirpath";
        public static final String EXTRA_RECTYPE = "extra_rectype";
        public static final String TABLE_NAME = "recordingnotifications";
        public static final String URI_PATH = "recordingnotifications";

        public static final class Columns implements BaseColumns {
            public static final String CNT_UNREAD = "cnt_unread";
            public static final String NOTIF_DESC = "NOTIF_DESC";
            public static final String REC_TYPE = "rec_type";
        }
    }

    public static class Records {
        public static final Uri CONTENT_URI = Uri.parse("content://records/records");
        public static final String TABLE_NAME = "records";
        public static final String URI_PATH = "records";

        public static final class Columns implements BaseColumns {
            public static final String CLOUD_SYNC_TIME = "cloud_sync_time";
            public static final String CONTENT = "content";
            public static final String CREATE_TIME = "create_time";
            public static final String DB_SYNC_TIME = "db_sync_time";
            public static final String DURATION = "duration";
            public static final String FILE_ID = "file_id";
            public static final String FILE_NAME = "file_name";
            public static final String FILE_PATH = "file_path";
            public static final String FILE_SIZE = "file_size";
            public static final String IN_CLOUD = "in_cloud";
            public static final String IN_LOCAL = "in_local";
            public static final String REC_DESC = "rec_desc";
            public static final String REC_TYPE = "rec_type";
            public static final String SHA1 = "sha1";
            public static final String SYNC_DIRTY = "sync_dirty";
        }

        public static final class InCloud {
            public static final int IN_CLOUD = 1;
            public static final int NOT_IN_CLOUD = 0;
        }

        public static final class InLocal {
            public static final int IN_LOCAL = 1;
            public static final int NOT_IN_LOCAL = 0;
        }

        public static final class Order {
            public static final String BY_CTREAT_TIME_DESC = "cloud_sync_time DESC";
        }

        public static final class RecType {
            public static final int CALL = 1;
            public static final int FM = 2;
            public static final int NORMAL = 0;
        }

        public static final class SyncDirty {
            public static final int DIRTY = 1;
            public static final int SYNCED = 0;
        }
    }

    public static class SyncTokens {
        public static final Uri CONTENT_URI = Uri.parse("content://records/synctokens");
        public static final String OLD_TABLE_NAME = "markpoint_synctoken";
        public static final String TABLE_NAME = "synctokens";
        public static final String URI_PATH = "synctokens";

        public static final class Columns implements BaseColumns {
            public static final String SYNC_EXTRA_INFO = "sync_extra_info";
            public static final String SYNC_TOKEN = "sync_token";
            public static final String SYNC_TOKEN_TYPE = "sync_token_type";
            public static final String WATER_MARK = "water_mark";
        }

        public static final class TokenType {
            public static final int TYPE_FILE_LIST = 1;
            public static final int TYPE_MARK_POINT = 0;
        }
    }

    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageMiuiDirectory().getAbsolutePath());
        stringBuilder.append(SAMPLE_DEFAULT_DIR);
        RECORDER_ROOT_PATH = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(RECORDER_ROOT_PATH);
        stringBuilder.append("/call_rec");
        CALL_RECORD_DIR = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(RECORDER_ROOT_PATH);
        stringBuilder.append("/fm_rec");
        FM_RECORD_DIR = stringBuilder.toString();
    }

    public static void notifyRecording(Context context, String path, long duration) {
        if (path != null) {
            File f = new File(path);
            int type = -1;
            if (path.startsWith(CALL_RECORD_DIR)) {
                type = 1;
            } else if (path.startsWith(FM_RECORD_DIR)) {
                type = 2;
            } else if (path.startsWith(RECORDER_ROOT_PATH)) {
                type = 0;
            }
            if (f.exists() && type != -1) {
                boolean isAndroidQ = VERSION.SDK_INT >= 29;
                if (isAndroidQ) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(RECORDER_ROOT_PATH);
                    stringBuilder.append("/.nomedia");
                    try {
                        new File(stringBuilder.toString()).delete();
                    } catch (Exception e) {
                    }
                } else {
                    FileUtils.addNoMedia(RECORDER_ROOT_PATH);
                }
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setClassName(MEDIA_SCANNER_PACKAGE, isAndroidQ ? MEDIA_SCANNER_CLASS_Q : MEDIA_SCANNER_CLASS);
                intent.setData(Uri.fromFile(f));
                context.sendBroadcast(intent);
                String sha1 = getSha1(f);
                if (!TextUtils.isEmpty(sha1)) {
                    ContentValues values = new ContentValues();
                    values.put("file_path", path);
                    values.put("file_name", f.getName());
                    values.put(Columns.CREATE_TIME, Long.valueOf(f.lastModified()));
                    values.put("rec_type", Integer.valueOf(type));
                    values.put(Columns.DB_SYNC_TIME, Long.valueOf(System.currentTimeMillis()));
                    values.put("duration", Long.valueOf(duration / 1000));
                    values.put("sync_dirty", Integer.valueOf(1));
                    values.put(Columns.IN_LOCAL, Integer.valueOf(1));
                    values.put(Columns.IN_CLOUD, Integer.valueOf(0));
                    values.put(Columns.SHA1, sha1);
                    context.getContentResolver().insert(Records.CONTENT_URI, values);
                }
            }
        }
    }

    public static int getNotificationUnreadCount(Context context, String recType) {
        Cursor c = null;
        int cnt = 0;
        try {
            c = context.getContentResolver().query(RecordingNotifications.CONTENT_URI, new String[]{Columns.CNT_UNREAD}, "rec_type=?", new String[]{recType}, null, null);
            if (c != null && c.getCount() == 1) {
                c.moveToFirst();
                cnt = c.getInt(0);
            }
            if (c != null) {
                c.close();
            }
            return cnt;
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
    }

    public static void setNotificationUnreadCount(Context context, String recType, int cntUnread, String desc) {
        String str = recType;
        String str2 = desc;
        ContentResolver resolver = context.getContentResolver();
        String str3 = Columns.CNT_UNREAD;
        String selection = "rec_type=?";
        boolean z = true;
        boolean update = new String[]{str};
        String[] selectionArgs = update;
        Cursor c = null;
        try {
            c = resolver.query(RecordingNotifications.CONTENT_URI, new String[]{str3}, selection, selectionArgs, null, null);
            if (c == null || c.getCount() == 0) {
                z = false;
            }
            update = z;
            ContentValues v = new ContentValues();
            v.put(str3, Integer.valueOf(cntUnread));
            if (str2 != null) {
                v.put(Columns.NOTIF_DESC, str2);
            }
            if (update) {
                resolver.update(RecordingNotifications.CONTENT_URI, v, selection, selectionArgs);
                return;
            }
            v.put("rec_type", str);
            resolver.insert(RecordingNotifications.CONTENT_URI, v);
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public static String getSha1(File file) {
        IOException e;
        String str = "Exception when close inputstream";
        String str2 = TAG;
        InputStream in = null;
        String sha1 = null;
        try {
            in = new FileInputStream(file);
            MessageDigest fileSha1 = MessageDigest.getInstance("SHA1");
            byte[] buf = new byte[8192];
            while (true) {
                int read = in.read(buf);
                int len = read;
                if (read < 0) {
                    break;
                }
                fileSha1.update(buf, 0, len);
            }
            sha1 = byteArrayToHexString(fileSha1.digest());
            try {
                in.close();
            } catch (IOException e2) {
                e = e2;
            }
        } catch (Exception e3) {
            Log.e(str2, "Exception when getSha1", e3);
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e4) {
                    e = e4;
                }
            }
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e5) {
                    Log.e(str2, str, e5);
                }
            }
        }
        return sha1;
        Log.e(str2, str, e);
        return sha1;
    }

    private static String byteArrayToHexString(byte[] b) {
        if (b == null) {
            return null;
        }
        StringBuffer resultSB = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            resultSB.append(HEXDIGITS[(b[i] >>> 4) & 15]);
            resultSB.append(HEXDIGITS[b[i] & 15]);
        }
        return resultSB.toString();
    }
}
