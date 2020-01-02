package android.media;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.drm.DrmManagerClient;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Audio.Playlists;
import android.provider.MediaStore.Audio.Playlists.Members;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Video.VideoColumns;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.sax.ElementListener;
import android.sax.RootElement;
import android.telecom.Logging.Session;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.Xml;
import dalvik.system.CloseGuard;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

@Deprecated
public class MediaScanner implements AutoCloseable {
    private static final String ALARMS_DIR = "/alarms/";
    private static final String AUDIOBOOKS_DIR = "/audiobooks/";
    private static final int DATE_MODIFIED_PLAYLISTS_COLUMN_INDEX = 2;
    private static final String DEFAULT_RINGTONE_PROPERTY_PREFIX = "ro.config.";
    private static final boolean ENABLE_BULK_INSERTS = true;
    private static final int FILES_PRESCAN_DATE_MODIFIED_COLUMN_INDEX = 3;
    private static final int FILES_PRESCAN_FORMAT_COLUMN_INDEX = 2;
    private static final int FILES_PRESCAN_ID_COLUMN_INDEX = 0;
    private static final int FILES_PRESCAN_MEDIA_TYPE_COLUMN_INDEX = 4;
    private static final int FILES_PRESCAN_PATH_COLUMN_INDEX = 1;
    @UnsupportedAppUsage
    private static final String[] FILES_PRESCAN_PROJECTION;
    private static final String[] ID3_GENRES = new String[]{"Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "A capella", "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie", "Britpop", null, "Polsk Punk", "Beat", "Christian Gangsta", "Heavy Metal", "Black Metal", "Crossover", "Contemporary Christian", "Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime", "JPop", "Synthpop"};
    private static final int ID_PLAYLISTS_COLUMN_INDEX = 0;
    private static final String[] ID_PROJECTION;
    public static final String LAST_INTERNAL_SCAN_FINGERPRINT = "lastScanFingerprint";
    private static final String MUSIC_DIR = "/music/";
    private static final String NOTIFICATIONS_DIR = "/notifications/";
    private static final String OEM_SOUNDS_DIR;
    private static final int PATH_PLAYLISTS_COLUMN_INDEX = 1;
    private static final String[] PLAYLIST_MEMBERS_PROJECTION = new String[]{Members.PLAYLIST_ID};
    private static final String PODCASTS_DIR = "/podcasts/";
    private static final String PRODUCT_SOUNDS_DIR;
    private static final String RINGTONES_DIR = "/ringtones/";
    public static final String SCANNED_BUILD_PREFS_NAME = "MediaScanBuild";
    private static final String SYSTEM_SOUNDS_DIR;
    private static final String TAG = "MediaScanner";
    private static HashMap<String, String> mMediaPaths = new HashMap();
    private static HashMap<String, String> mNoMediaPaths = new HashMap();
    private static String sLastInternalScanFingerprint;
    @UnsupportedAppUsage
    private final Uri mAudioUri;
    private final Options mBitmapOptions = new Options();
    @UnsupportedAppUsage
    private final MyMediaScannerClient mClient = new MyMediaScannerClient();
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final AtomicBoolean mClosed = new AtomicBoolean();
    @UnsupportedAppUsage
    private final Context mContext;
    @UnsupportedAppUsage
    private String mDefaultAlarmAlertFilename;
    private boolean mDefaultAlarmSet;
    @UnsupportedAppUsage
    private String mDefaultNotificationFilename;
    private boolean mDefaultNotificationSet;
    @UnsupportedAppUsage
    private String mDefaultRingtoneFilename;
    private boolean mDefaultRingtoneSet;
    private DrmManagerClient mDrmManagerClient = null;
    private final Uri mFilesFullUri;
    @UnsupportedAppUsage
    private final Uri mFilesUri;
    private final Uri mImagesUri;
    @UnsupportedAppUsage
    private MediaInserter mMediaInserter;
    private final ContentProviderClient mMediaProvider;
    private int mMtpObjectHandle;
    private long mNativeContext;
    private int mOriginalCount;
    @UnsupportedAppUsage
    private final String mPackageName;
    private final ArrayList<FileEntry> mPlayLists = new ArrayList();
    private final ArrayList<PlaylistEntry> mPlaylistEntries = new ArrayList();
    private final Uri mPlaylistsUri;
    private final boolean mProcessGenres;
    private final boolean mProcessPlaylists;
    private final Uri mVideoUri;
    private final String mVolumeName;

    private static class FileEntry {
        int mFormat;
        long mLastModified;
        @UnsupportedAppUsage
        boolean mLastModifiedChanged;
        int mMediaType;
        String mPath;
        @UnsupportedAppUsage
        long mRowId;

        @Deprecated
        @UnsupportedAppUsage
        FileEntry(long rowId, String path, long lastModified, int format) {
            this(rowId, path, lastModified, format, 0);
        }

        FileEntry(long rowId, String path, long lastModified, int format, int mediaType) {
            this.mRowId = rowId;
            this.mPath = path;
            this.mLastModified = lastModified;
            this.mFormat = format;
            this.mMediaType = mediaType;
            this.mLastModifiedChanged = false;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.mPath);
            stringBuilder.append(" mRowId: ");
            stringBuilder.append(this.mRowId);
            return stringBuilder.toString();
        }
    }

    static class MediaBulkDeleter {
        final Uri mBaseUri;
        final ContentProviderClient mProvider;
        ArrayList<String> whereArgs = new ArrayList(100);
        StringBuilder whereClause = new StringBuilder();

        public MediaBulkDeleter(ContentProviderClient provider, Uri baseUri) {
            this.mProvider = provider;
            this.mBaseUri = baseUri;
        }

        public void delete(long id) throws RemoteException {
            if (this.whereClause.length() != 0) {
                this.whereClause.append(",");
            }
            this.whereClause.append("?");
            ArrayList arrayList = this.whereArgs;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(id);
            arrayList.add(stringBuilder.toString());
            if (this.whereArgs.size() > 100) {
                flush();
            }
        }

        public void flush() throws RemoteException {
            int size = this.whereArgs.size();
            if (size > 0) {
                String[] foo = (String[]) this.whereArgs.toArray(new String[size]);
                int numrows = this.mProvider;
                Uri uri = this.mBaseUri;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("_id IN (");
                stringBuilder.append(this.whereClause.toString());
                stringBuilder.append(")");
                numrows = numrows.delete(uri, stringBuilder.toString(), foo);
                this.whereClause.setLength(0);
                this.whereArgs.clear();
            }
        }
    }

    private class MyMediaScannerClient implements MediaScannerClient {
        private String mAlbum;
        private String mAlbumArtist;
        private String mArtist;
        private int mColorRange;
        private int mColorStandard;
        private int mColorTransfer;
        private int mCompilation;
        private String mComposer;
        private long mDate;
        private final SimpleDateFormat mDateFormatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        private int mDuration;
        private long mFileSize;
        @Deprecated
        @UnsupportedAppUsage
        private int mFileType;
        private String mGenre;
        private int mHeight;
        @UnsupportedAppUsage
        private boolean mIsDrm;
        private long mLastModified;
        @UnsupportedAppUsage
        private String mMimeType;
        @UnsupportedAppUsage
        private boolean mNoMedia;
        @UnsupportedAppUsage
        private String mPath;
        private boolean mScanSuccess;
        private String mTitle;
        private int mTrack;
        private int mWidth;
        private String mWriter;
        private int mYear;

        public MyMediaScannerClient() {
            this.mDateFormatter.setTimeZone(TimeZone.getTimeZone(Time.TIMEZONE_UTC));
        }

        @UnsupportedAppUsage
        public FileEntry beginFile(String path, String mimeType, long lastModified, long fileSize, boolean isDirectory, boolean noMedia) {
            boolean noMedia2;
            String str = path;
            long j = lastModified;
            this.mMimeType = mimeType;
            this.mFileSize = fileSize;
            this.mIsDrm = false;
            this.mScanSuccess = true;
            if (isDirectory) {
            } else {
                if (noMedia || !MediaScanner.isNoMediaFile(path)) {
                    noMedia2 = noMedia;
                } else {
                    noMedia2 = true;
                }
                this.mNoMedia = noMedia2;
                if (this.mMimeType == null) {
                    this.mMimeType = MediaFile.getMimeTypeForFile(path);
                }
                if (MediaScanner.this.isDrmEnabled() && MediaFile.isDrmMimeType(this.mMimeType)) {
                    getMimeTypeFromDrm(path);
                }
                boolean z = noMedia2;
            }
            FileEntry entry = MediaScanner.this.makeEntryFor(str);
            long delta = entry != null ? j - entry.mLastModified : 0;
            noMedia2 = delta > 1 || delta < -1;
            boolean wasModified = noMedia2;
            if (entry == null || wasModified) {
                boolean z2;
                if (wasModified) {
                    entry.mLastModified = j;
                    z2 = true;
                } else {
                    z2 = true;
                    entry = new FileEntry(0, path, lastModified, isDirectory ? true : false, 0);
                }
                entry.mLastModifiedChanged = z2;
            }
            if (MediaScanner.this.mProcessPlaylists && MediaFile.isPlayListMimeType(this.mMimeType)) {
                MediaScanner.this.mPlayLists.add(entry);
                return null;
            }
            this.mArtist = null;
            this.mAlbumArtist = null;
            this.mAlbum = null;
            this.mTitle = null;
            this.mComposer = null;
            this.mGenre = null;
            this.mTrack = 0;
            this.mYear = 0;
            this.mDuration = 0;
            this.mPath = str;
            this.mDate = 0;
            this.mLastModified = j;
            this.mWriter = null;
            this.mCompilation = 0;
            this.mWidth = 0;
            this.mHeight = 0;
            this.mColorStandard = -1;
            this.mColorTransfer = -1;
            this.mColorRange = -1;
            return entry;
        }

        @UnsupportedAppUsage
        public void scanFile(String path, long lastModified, long fileSize, boolean isDirectory, boolean noMedia) {
            doScanFile(path, null, lastModified, fileSize, isDirectory, false, noMedia);
        }

        /* JADX WARNING: Removed duplicated region for block: B:122:0x01a7 A:{SYNTHETIC, Splitter:B:122:0x01a7} */
        /* JADX WARNING: Removed duplicated region for block: B:113:0x0196 A:{SYNTHETIC, Splitter:B:113:0x0196} */
        /* JADX WARNING: Removed duplicated region for block: B:122:0x01a7 A:{SYNTHETIC, Splitter:B:122:0x01a7} */
        /* JADX WARNING: Removed duplicated region for block: B:104:0x0185 A:{SYNTHETIC, Splitter:B:104:0x0185} */
        /* JADX WARNING: Removed duplicated region for block: B:113:0x0196 A:{SYNTHETIC, Splitter:B:113:0x0196} */
        /* JADX WARNING: Removed duplicated region for block: B:122:0x01a7 A:{SYNTHETIC, Splitter:B:122:0x01a7} */
        /* JADX WARNING: Removed duplicated region for block: B:95:0x0174 A:{SYNTHETIC, Splitter:B:95:0x0174} */
        /* JADX WARNING: Removed duplicated region for block: B:104:0x0185 A:{SYNTHETIC, Splitter:B:104:0x0185} */
        /* JADX WARNING: Removed duplicated region for block: B:113:0x0196 A:{SYNTHETIC, Splitter:B:113:0x0196} */
        /* JADX WARNING: Removed duplicated region for block: B:122:0x01a7 A:{SYNTHETIC, Splitter:B:122:0x01a7} */
        /* JADX WARNING: Removed duplicated region for block: B:86:0x0163 A:{SYNTHETIC, Splitter:B:86:0x0163} */
        /* JADX WARNING: Removed duplicated region for block: B:95:0x0174 A:{SYNTHETIC, Splitter:B:95:0x0174} */
        /* JADX WARNING: Removed duplicated region for block: B:104:0x0185 A:{SYNTHETIC, Splitter:B:104:0x0185} */
        /* JADX WARNING: Removed duplicated region for block: B:113:0x0196 A:{SYNTHETIC, Splitter:B:113:0x0196} */
        /* JADX WARNING: Removed duplicated region for block: B:122:0x01a7 A:{SYNTHETIC, Splitter:B:122:0x01a7} */
        /* JADX WARNING: Removed duplicated region for block: B:69:0x0139 A:{SYNTHETIC, Splitter:B:69:0x0139} */
        /* JADX WARNING: Removed duplicated region for block: B:77:0x0152 A:{SYNTHETIC, Splitter:B:77:0x0152} */
        /* JADX WARNING: Removed duplicated region for block: B:86:0x0163 A:{SYNTHETIC, Splitter:B:86:0x0163} */
        /* JADX WARNING: Removed duplicated region for block: B:95:0x0174 A:{SYNTHETIC, Splitter:B:95:0x0174} */
        /* JADX WARNING: Removed duplicated region for block: B:104:0x0185 A:{SYNTHETIC, Splitter:B:104:0x0185} */
        /* JADX WARNING: Removed duplicated region for block: B:113:0x0196 A:{SYNTHETIC, Splitter:B:113:0x0196} */
        /* JADX WARNING: Removed duplicated region for block: B:122:0x01a7 A:{SYNTHETIC, Splitter:B:122:0x01a7} */
        /* JADX WARNING: Removed duplicated region for block: B:51:0x00ea A:{SYNTHETIC, Splitter:B:51:0x00ea} */
        /* JADX WARNING: Removed duplicated region for block: B:45:0x00d3  */
        /* JADX WARNING: Removed duplicated region for block: B:42:0x00ca A:{SKIP} */
        /* JADX WARNING: Removed duplicated region for block: B:45:0x00d3  */
        /* JADX WARNING: Removed duplicated region for block: B:51:0x00ea A:{SYNTHETIC, Splitter:B:51:0x00ea} */
        /* JADX WARNING: Missing block: B:22:0x0049, code skipped:
            if (doesPathHaveFilename(r0.mPath, android.media.MediaScanner.access$600(r10.this$0)) == false) goto L_0x004b;
     */
        @android.annotation.UnsupportedAppUsage
        public android.net.Uri doScanFile(java.lang.String r24, java.lang.String r25, long r26, long r28, boolean r30, boolean r31, boolean r32) {
            /*
            r23 = this;
            r10 = r23;
            r11 = "MediaScanner";
            r12 = 0;
            r1 = r23;
            r2 = r24;
            r3 = r25;
            r4 = r26;
            r6 = r28;
            r8 = r30;
            r9 = r32;
            r0 = r1.beginFile(r2, r3, r4, r6, r8, r9);	 Catch:{ RemoteException -> 0x01e3 }
            if (r0 != 0) goto L_0x001b;
        L_0x0019:
            r1 = 0;
            return r1;
        L_0x001b:
            r1 = android.media.MediaScanner.this;	 Catch:{ RemoteException -> 0x01e3 }
            r1 = r1.mMtpObjectHandle;	 Catch:{ RemoteException -> 0x01e3 }
            if (r1 == 0) goto L_0x002f;
        L_0x0023:
            r1 = 0;
            r0.mRowId = r1;	 Catch:{ RemoteException -> 0x0028 }
            goto L_0x002f;
        L_0x0028:
            r0 = move-exception;
            r22 = r24;
            r9 = r31;
            goto L_0x01ea;
        L_0x002f:
            r1 = r0.mPath;	 Catch:{ RemoteException -> 0x01e3 }
            if (r1 == 0) goto L_0x00c4;
        L_0x0033:
            r1 = android.media.MediaScanner.this;	 Catch:{ RemoteException -> 0x0028 }
            r1 = r1.mDefaultNotificationSet;	 Catch:{ RemoteException -> 0x0028 }
            r2 = "forcing rescan of ";
            if (r1 != 0) goto L_0x004b;
        L_0x003d:
            r1 = r0.mPath;	 Catch:{ RemoteException -> 0x0028 }
            r3 = android.media.MediaScanner.this;	 Catch:{ RemoteException -> 0x0028 }
            r3 = r3.mDefaultNotificationFilename;	 Catch:{ RemoteException -> 0x0028 }
            r1 = r10.doesPathHaveFilename(r1, r3);	 Catch:{ RemoteException -> 0x0028 }
            if (r1 != 0) goto L_0x0077;
        L_0x004b:
            r1 = android.media.MediaScanner.this;	 Catch:{ RemoteException -> 0x0028 }
            r1 = r1.mDefaultRingtoneSet;	 Catch:{ RemoteException -> 0x0028 }
            if (r1 != 0) goto L_0x0061;
        L_0x0053:
            r1 = r0.mPath;	 Catch:{ RemoteException -> 0x0028 }
            r3 = android.media.MediaScanner.this;	 Catch:{ RemoteException -> 0x0028 }
            r3 = r3.mDefaultRingtoneFilename;	 Catch:{ RemoteException -> 0x0028 }
            r1 = r10.doesPathHaveFilename(r1, r3);	 Catch:{ RemoteException -> 0x0028 }
            if (r1 != 0) goto L_0x0077;
        L_0x0061:
            r1 = android.media.MediaScanner.this;	 Catch:{ RemoteException -> 0x0028 }
            r1 = r1.mDefaultAlarmSet;	 Catch:{ RemoteException -> 0x0028 }
            if (r1 != 0) goto L_0x0094;
        L_0x0069:
            r1 = r0.mPath;	 Catch:{ RemoteException -> 0x0028 }
            r3 = android.media.MediaScanner.this;	 Catch:{ RemoteException -> 0x0028 }
            r3 = r3.mDefaultAlarmAlertFilename;	 Catch:{ RemoteException -> 0x0028 }
            r1 = r10.doesPathHaveFilename(r1, r3);	 Catch:{ RemoteException -> 0x0028 }
            if (r1 == 0) goto L_0x0094;
        L_0x0077:
            r1 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0028 }
            r1.<init>();	 Catch:{ RemoteException -> 0x0028 }
            r1.append(r2);	 Catch:{ RemoteException -> 0x0028 }
            r2 = r0.mPath;	 Catch:{ RemoteException -> 0x0028 }
            r1.append(r2);	 Catch:{ RemoteException -> 0x0028 }
            r2 = "since ringtone setting didn't finish";
            r1.append(r2);	 Catch:{ RemoteException -> 0x0028 }
            r1 = r1.toString();	 Catch:{ RemoteException -> 0x0028 }
            android.util.Log.w(r11, r1);	 Catch:{ RemoteException -> 0x0028 }
            r1 = 1;
            r9 = r1;
            goto L_0x00c6;
        L_0x0094:
            r1 = r0.mPath;	 Catch:{ RemoteException -> 0x0028 }
            r1 = android.media.MediaScanner.isSystemSoundWithMetadata(r1);	 Catch:{ RemoteException -> 0x0028 }
            if (r1 == 0) goto L_0x00c4;
        L_0x009c:
            r1 = android.os.Build.FINGERPRINT;	 Catch:{ RemoteException -> 0x0028 }
            r3 = android.media.MediaScanner.sLastInternalScanFingerprint;	 Catch:{ RemoteException -> 0x0028 }
            r1 = r1.equals(r3);	 Catch:{ RemoteException -> 0x0028 }
            if (r1 != 0) goto L_0x00c4;
        L_0x00a8:
            r1 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0028 }
            r1.<init>();	 Catch:{ RemoteException -> 0x0028 }
            r1.append(r2);	 Catch:{ RemoteException -> 0x0028 }
            r2 = r0.mPath;	 Catch:{ RemoteException -> 0x0028 }
            r1.append(r2);	 Catch:{ RemoteException -> 0x0028 }
            r2 = " since build fingerprint changed";
            r1.append(r2);	 Catch:{ RemoteException -> 0x0028 }
            r1 = r1.toString();	 Catch:{ RemoteException -> 0x0028 }
            android.util.Log.i(r11, r1);	 Catch:{ RemoteException -> 0x0028 }
            r1 = 1;
            r9 = r1;
            goto L_0x00c6;
        L_0x00c4:
            r9 = r31;
        L_0x00c6:
            r1 = r0.mLastModifiedChanged;	 Catch:{ RemoteException -> 0x01dd }
            if (r1 != 0) goto L_0x00d1;
        L_0x00ca:
            if (r9 == 0) goto L_0x00cd;
        L_0x00cc:
            goto L_0x00d1;
        L_0x00cd:
            r22 = r24;
            goto L_0x01d2;
        L_0x00d1:
            if (r32 == 0) goto L_0x00ea;
        L_0x00d3:
            r3 = 0;
            r4 = 0;
            r5 = 0;
            r6 = 0;
            r7 = 0;
            r8 = 0;
            r1 = r23;
            r2 = r0;
            r1 = r1.endFile(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ RemoteException -> 0x00e5 }
            r12 = r1;
            r22 = r24;
            goto L_0x01d2;
        L_0x00e5:
            r0 = move-exception;
            r22 = r24;
            goto L_0x01ea;
        L_0x00ea:
            r1 = r10.mMimeType;	 Catch:{ RemoteException -> 0x01dd }
            r1 = android.media.MediaFile.isAudioMimeType(r1);	 Catch:{ RemoteException -> 0x01dd }
            r13 = r1;
            r1 = r10.mMimeType;	 Catch:{ RemoteException -> 0x01dd }
            r1 = android.media.MediaFile.isVideoMimeType(r1);	 Catch:{ RemoteException -> 0x01dd }
            r14 = r1;
            r1 = r10.mMimeType;	 Catch:{ RemoteException -> 0x01dd }
            r1 = android.media.MediaFile.isImageMimeType(r1);	 Catch:{ RemoteException -> 0x01dd }
            r15 = r1;
            if (r13 != 0) goto L_0x0109;
        L_0x0101:
            if (r14 != 0) goto L_0x0109;
        L_0x0103:
            if (r15 == 0) goto L_0x0106;
        L_0x0105:
            goto L_0x0109;
        L_0x0106:
            r7 = r24;
            goto L_0x0119;
        L_0x0109:
            r1 = new java.io.File;	 Catch:{ RemoteException -> 0x01dd }
            r2 = r24;
            r1.<init>(r2);	 Catch:{ RemoteException -> 0x01d9 }
            r1 = android.os.Environment.maybeTranslateEmulatedPathToInternal(r1);	 Catch:{ RemoteException -> 0x01d9 }
            r1 = r1.getAbsolutePath();	 Catch:{ RemoteException -> 0x01d9 }
            r7 = r1;
        L_0x0119:
            if (r13 != 0) goto L_0x0121;
        L_0x011b:
            if (r14 == 0) goto L_0x011e;
        L_0x011d:
            goto L_0x0121;
        L_0x011e:
            r6 = r25;
            goto L_0x0137;
        L_0x0121:
            r1 = android.media.MediaScanner.this;	 Catch:{ RemoteException -> 0x01d5 }
            r1 = r1.mContext;	 Catch:{ RemoteException -> 0x01d5 }
            android.media.MediaScannerInjector.processFileBegin(r7, r1);	 Catch:{ RemoteException -> 0x01d5 }
            r1 = android.media.MediaScanner.this;	 Catch:{ RemoteException -> 0x01d5 }
            r6 = r25;
            r1 = r1.processFile(r7, r6, r10);	 Catch:{ RemoteException -> 0x01d5 }
            r10.mScanSuccess = r1;	 Catch:{ RemoteException -> 0x01d5 }
            android.media.MediaScannerInjector.processFileEnd();	 Catch:{ RemoteException -> 0x01d5 }
        L_0x0137:
            if (r15 == 0) goto L_0x0145;
        L_0x0139:
            r1 = r10.processImageFile(r7);	 Catch:{ RemoteException -> 0x0140 }
            r10.mScanSuccess = r1;	 Catch:{ RemoteException -> 0x0140 }
            goto L_0x0145;
        L_0x0140:
            r0 = move-exception;
            r22 = r7;
            goto L_0x01ea;
        L_0x0145:
            r1 = java.util.Locale.ROOT;	 Catch:{ RemoteException -> 0x01d5 }
            r1 = r7.toLowerCase(r1);	 Catch:{ RemoteException -> 0x01d5 }
            r5 = r1;
            r1 = r10.mScanSuccess;	 Catch:{ RemoteException -> 0x01d5 }
            r2 = 1;
            r3 = 0;
            if (r1 == 0) goto L_0x015c;
        L_0x0152:
            r1 = "/ringtones/";
            r1 = r5.indexOf(r1);	 Catch:{ RemoteException -> 0x0140 }
            if (r1 <= 0) goto L_0x015c;
        L_0x015a:
            r1 = r2;
            goto L_0x015d;
        L_0x015c:
            r1 = r3;
        L_0x015d:
            r16 = r1;
            r1 = r10.mScanSuccess;	 Catch:{ RemoteException -> 0x01d5 }
            if (r1 == 0) goto L_0x016d;
        L_0x0163:
            r1 = "/notifications/";
            r1 = r5.indexOf(r1);	 Catch:{ RemoteException -> 0x0140 }
            if (r1 <= 0) goto L_0x016d;
        L_0x016b:
            r1 = r2;
            goto L_0x016e;
        L_0x016d:
            r1 = r3;
        L_0x016e:
            r17 = r1;
            r1 = r10.mScanSuccess;	 Catch:{ RemoteException -> 0x01d5 }
            if (r1 == 0) goto L_0x017e;
        L_0x0174:
            r1 = "/alarms/";
            r1 = r5.indexOf(r1);	 Catch:{ RemoteException -> 0x0140 }
            if (r1 <= 0) goto L_0x017e;
        L_0x017c:
            r1 = r2;
            goto L_0x017f;
        L_0x017e:
            r1 = r3;
        L_0x017f:
            r18 = r1;
            r1 = r10.mScanSuccess;	 Catch:{ RemoteException -> 0x01d5 }
            if (r1 == 0) goto L_0x018f;
        L_0x0185:
            r1 = "/podcasts/";
            r1 = r5.indexOf(r1);	 Catch:{ RemoteException -> 0x0140 }
            if (r1 <= 0) goto L_0x018f;
        L_0x018d:
            r1 = r2;
            goto L_0x0190;
        L_0x018f:
            r1 = r3;
        L_0x0190:
            r19 = r1;
            r1 = r10.mScanSuccess;	 Catch:{ RemoteException -> 0x01d5 }
            if (r1 == 0) goto L_0x01a0;
        L_0x0196:
            r1 = "/audiobooks/";
            r1 = r5.indexOf(r1);	 Catch:{ RemoteException -> 0x0140 }
            if (r1 <= 0) goto L_0x01a0;
        L_0x019e:
            r1 = r2;
            goto L_0x01a1;
        L_0x01a0:
            r1 = r3;
        L_0x01a1:
            r20 = r1;
            r1 = r10.mScanSuccess;	 Catch:{ RemoteException -> 0x01d5 }
            if (r1 == 0) goto L_0x01bb;
        L_0x01a7:
            r1 = "/music/";
            r1 = r5.indexOf(r1);	 Catch:{ RemoteException -> 0x0140 }
            if (r1 > 0) goto L_0x01b9;
        L_0x01af:
            if (r16 != 0) goto L_0x01bb;
        L_0x01b1:
            if (r17 != 0) goto L_0x01bb;
        L_0x01b3:
            if (r18 != 0) goto L_0x01bb;
        L_0x01b5:
            if (r19 != 0) goto L_0x01bb;
        L_0x01b7:
            if (r20 != 0) goto L_0x01bb;
        L_0x01b9:
            r8 = r2;
            goto L_0x01bc;
        L_0x01bb:
            r8 = r3;
        L_0x01bc:
            r1 = r23;
            r2 = r0;
            r3 = r16;
            r4 = r17;
            r21 = r5;
            r5 = r18;
            r6 = r19;
            r22 = r7;
            r7 = r20;
            r1 = r1.endFile(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ RemoteException -> 0x01d3 }
            r12 = r1;
        L_0x01d2:
            goto L_0x01ef;
        L_0x01d3:
            r0 = move-exception;
            goto L_0x01ea;
        L_0x01d5:
            r0 = move-exception;
            r22 = r7;
            goto L_0x01ea;
        L_0x01d9:
            r0 = move-exception;
            r22 = r2;
            goto L_0x01ea;
        L_0x01dd:
            r0 = move-exception;
            r2 = r24;
            r22 = r2;
            goto L_0x01ea;
        L_0x01e3:
            r0 = move-exception;
            r2 = r24;
            r9 = r31;
            r22 = r2;
        L_0x01ea:
            r1 = "RemoteException in MediaScanner.scanFile()";
            android.util.Log.e(r11, r1, r0);
        L_0x01ef:
            return r12;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner$MyMediaScannerClient.doScanFile(java.lang.String, java.lang.String, long, long, boolean, boolean, boolean):android.net.Uri");
        }

        private long parseDate(String date) {
            try {
                return this.mDateFormatter.parse(date).getTime();
            } catch (ParseException e) {
                return 0;
            }
        }

        private int parseSubstring(String s, int ch, int defaultValue) {
            int length = s.length();
            if (ch == length) {
                return defaultValue;
            }
            int start = ch + 1;
            char ch2 = s.charAt(ch);
            if (ch2 < '0' || ch2 > '9') {
                return defaultValue;
            }
            int result = ch2 - 48;
            while (start < length) {
                int start2 = start + 1;
                ch2 = s.charAt(start);
                if (ch2 < '0' || ch2 > '9') {
                    return result;
                }
                result = (result * 10) + (ch2 - 48);
                start = start2;
            }
            return result;
        }

        @UnsupportedAppUsage
        public void handleStringTag(String name, String value) {
            if (name.equalsIgnoreCase("title") || name.startsWith("title;")) {
                this.mTitle = value;
            } else if (name.equalsIgnoreCase("artist") || name.startsWith("artist;")) {
                this.mArtist = value.trim();
            } else if (name.equalsIgnoreCase("albumartist") || name.startsWith("albumartist;") || name.equalsIgnoreCase("band") || name.startsWith("band;")) {
                this.mAlbumArtist = value.trim();
            } else if (name.equalsIgnoreCase("album") || name.startsWith("album;")) {
                this.mAlbum = value.trim();
            } else if (name.equalsIgnoreCase(AudioColumns.COMPOSER) || name.startsWith("composer;")) {
                this.mComposer = value.trim();
            } else if (MediaScanner.this.mProcessGenres && (name.equalsIgnoreCase(AudioColumns.GENRE) || name.startsWith("genre;"))) {
                this.mGenre = getGenreName(value);
            } else {
                boolean z = false;
                if (name.equalsIgnoreCase("year") || name.startsWith("year;")) {
                    this.mYear = parseSubstring(value, 0, 0);
                } else if (name.equalsIgnoreCase("tracknumber") || name.startsWith("tracknumber;")) {
                    this.mTrack = ((this.mTrack / 1000) * 1000) + parseSubstring(value, 0, 0);
                } else if (name.equalsIgnoreCase("discnumber") || name.equals("set") || name.startsWith("set;")) {
                    this.mTrack = (parseSubstring(value, 0, 0) * 1000) + (this.mTrack % 1000);
                } else if (name.equalsIgnoreCase("duration")) {
                    this.mDuration = parseSubstring(value, 0, 0);
                } else if (name.equalsIgnoreCase("writer") || name.startsWith("writer;")) {
                    this.mWriter = value.trim();
                } else if (name.equalsIgnoreCase(AudioColumns.COMPILATION)) {
                    this.mCompilation = parseSubstring(value, 0, 0);
                } else if (name.equalsIgnoreCase("isdrm")) {
                    if (parseSubstring(value, 0, 0) == 1) {
                        z = true;
                    }
                    this.mIsDrm = z;
                } else if (name.equalsIgnoreCase("date")) {
                    this.mDate = parseDate(value);
                } else if (name.equalsIgnoreCase("width")) {
                    this.mWidth = parseSubstring(value, 0, 0);
                } else if (name.equalsIgnoreCase("height")) {
                    this.mHeight = parseSubstring(value, 0, 0);
                } else if (name.equalsIgnoreCase("colorstandard")) {
                    this.mColorStandard = parseSubstring(value, 0, -1);
                } else if (name.equalsIgnoreCase("colortransfer")) {
                    this.mColorTransfer = parseSubstring(value, 0, -1);
                } else if (name.equalsIgnoreCase("colorrange")) {
                    this.mColorRange = parseSubstring(value, 0, -1);
                }
            }
        }

        private boolean convertGenreCode(String input, String expected) {
            String output = getGenreName(input);
            if (output.equals(expected)) {
                return true;
            }
            StringBuilder stringBuilder = new StringBuilder();
            String str = "'";
            stringBuilder.append(str);
            stringBuilder.append(input);
            stringBuilder.append("' -> '");
            stringBuilder.append(output);
            stringBuilder.append("', expected '");
            stringBuilder.append(expected);
            stringBuilder.append(str);
            Log.d(MediaScanner.TAG, stringBuilder.toString());
            return false;
        }

        private void testGenreNameConverter() {
            String str = "Country";
            convertGenreCode("2", str);
            convertGenreCode("(2)", str);
            String str2 = "(2";
            convertGenreCode(str2, str2);
            convertGenreCode("2 Foo", str);
            convertGenreCode("(2) Foo", str);
            str2 = "(2 Foo";
            convertGenreCode(str2, str2);
            str2 = "2Foo";
            convertGenreCode(str2, str2);
            convertGenreCode("(2)Foo", str);
            str = "Foo";
            convertGenreCode("200 Foo", str);
            convertGenreCode("(200) Foo", str);
            str2 = "200Foo";
            convertGenreCode(str2, str2);
            convertGenreCode("(200)Foo", str);
            str = "200)Foo";
            convertGenreCode(str, str);
            str = "200) Foo";
            convertGenreCode(str, str);
        }

        public String getGenreName(String genreTagValue) {
            if (genreTagValue == null) {
                return null;
            }
            int length = genreTagValue.length();
            if (length > 0) {
                char c;
                boolean parenthesized = false;
                StringBuffer number = new StringBuffer();
                int i = 0;
                while (i < length) {
                    c = genreTagValue.charAt(i);
                    if (i != 0 || c != '(') {
                        if (!Character.isDigit(c)) {
                            break;
                        }
                        number.append(c);
                    } else {
                        parenthesized = true;
                    }
                    i++;
                }
                c = i < length ? genreTagValue.charAt(i) : ' ';
                if ((parenthesized && c == ')') || (!parenthesized && Character.isWhitespace(c))) {
                    try {
                        short genreIndex = Short.parseShort(number.toString());
                        if (genreIndex >= (short) 0) {
                            if (genreIndex < MediaScanner.ID3_GENRES.length && MediaScanner.ID3_GENRES[genreIndex] != null) {
                                return MediaScanner.ID3_GENRES[genreIndex];
                            }
                            if (genreIndex == (short) 255) {
                                return null;
                            }
                            if (genreIndex >= (short) 255 || i + 1 >= length) {
                                return number.toString();
                            }
                            if (parenthesized && c == ')') {
                                i++;
                            }
                            String ret = genreTagValue.substring(i).trim();
                            if (ret.length() != 0) {
                                return ret;
                            }
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
            return genreTagValue;
        }

        private boolean processImageFile(String path) {
            boolean z = false;
            try {
                MediaScanner.this.mBitmapOptions.outWidth = 0;
                MediaScanner.this.mBitmapOptions.outHeight = 0;
                BitmapFactory.decodeFile(path, MediaScanner.this.mBitmapOptions);
                this.mWidth = MediaScanner.this.mBitmapOptions.outWidth;
                this.mHeight = MediaScanner.this.mBitmapOptions.outHeight;
                if (this.mWidth > 0 && this.mHeight > 0) {
                    z = true;
                }
                return z;
            } catch (Throwable th) {
                return false;
            }
        }

        @UnsupportedAppUsage
        public void setMimeType(String mimeType) {
            if ((!"audio/mp4".equals(this.mMimeType) || !mimeType.startsWith("video")) && !MediaScannerInjector.keepMimeType(this.mMimeType, mimeType)) {
                this.mMimeType = mimeType;
            }
        }

        @UnsupportedAppUsage
        private ContentValues toValues() {
            ContentValues map = new ContentValues();
            map.put("_data", this.mPath);
            map.put("title", this.mTitle);
            map.put("date_modified", Long.valueOf(this.mLastModified));
            map.put("_size", Long.valueOf(this.mFileSize));
            map.put("mime_type", this.mMimeType);
            map.put(MediaColumns.IS_DRM, Boolean.valueOf(this.mIsDrm));
            map.putNull(MediaColumns.HASH);
            String resolution = null;
            int i = this.mWidth;
            if (i > 0 && this.mHeight > 0) {
                map.put("width", Integer.valueOf(i));
                map.put("height", Integer.valueOf(this.mHeight));
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mWidth);
                stringBuilder.append("x");
                stringBuilder.append(this.mHeight);
                resolution = stringBuilder.toString();
            }
            if (!this.mNoMedia) {
                boolean isVideoMimeType = MediaFile.isVideoMimeType(this.mMimeType);
                String str = "duration";
                String str2 = "album";
                String str3 = "artist";
                String str4 = MediaStore.UNKNOWN_STRING;
                String str5;
                if (isVideoMimeType) {
                    str5 = this.mArtist;
                    str5 = (str5 == null || str5.length() <= 0) ? str4 : this.mArtist;
                    map.put(str3, str5);
                    str5 = this.mAlbum;
                    if (str5 != null && str5.length() > 0) {
                        str4 = this.mAlbum;
                    }
                    map.put(str2, str4);
                    map.put(str, Integer.valueOf(this.mDuration));
                    if (resolution != null) {
                        map.put(VideoColumns.RESOLUTION, resolution);
                    }
                    i = this.mColorStandard;
                    if (i >= 0) {
                        map.put(VideoColumns.COLOR_STANDARD, Integer.valueOf(i));
                    }
                    i = this.mColorTransfer;
                    if (i >= 0) {
                        map.put(VideoColumns.COLOR_TRANSFER, Integer.valueOf(i));
                    }
                    i = this.mColorRange;
                    if (i >= 0) {
                        map.put(VideoColumns.COLOR_RANGE, Integer.valueOf(i));
                    }
                    long j = this.mDate;
                    if (j > 0) {
                        map.put("datetaken", Long.valueOf(j));
                    }
                } else if (!MediaFile.isImageMimeType(this.mMimeType) && MediaFile.isAudioMimeType(this.mMimeType)) {
                    str5 = this.mArtist;
                    str5 = (str5 == null || str5.length() <= 0) ? str4 : this.mArtist;
                    map.put(str3, str5);
                    str5 = this.mAlbumArtist;
                    str5 = (str5 == null || str5.length() <= 0) ? null : this.mAlbumArtist;
                    map.put(AudioColumns.ALBUM_ARTIST, str5);
                    str5 = this.mAlbum;
                    if (str5 != null && str5.length() > 0) {
                        str4 = this.mAlbum;
                    }
                    map.put(str2, str4);
                    map.put(AudioColumns.COMPOSER, this.mComposer);
                    map.put(AudioColumns.GENRE, this.mGenre);
                    i = this.mYear;
                    if (i != 0) {
                        map.put("year", Integer.valueOf(i));
                    }
                    map.put(AudioColumns.TRACK, Integer.valueOf(this.mTrack));
                    map.put(str, Integer.valueOf(this.mDuration));
                    map.put(AudioColumns.COMPILATION, Integer.valueOf(this.mCompilation));
                }
            }
            return map;
        }

        /* JADX WARNING: Removed duplicated region for block: B:20:0x0060  */
        @android.annotation.UnsupportedAppUsage
        private android.net.Uri endFile(android.media.MediaScanner.FileEntry r19, boolean r20, boolean r21, boolean r22, boolean r23, boolean r24, boolean r25) throws android.os.RemoteException {
            /*
            r18 = this;
            r1 = r18;
            r2 = r19;
            r0 = r1.mArtist;
            if (r0 == 0) goto L_0x000e;
        L_0x0008:
            r0 = r0.length();
            if (r0 != 0) goto L_0x0012;
        L_0x000e:
            r0 = r1.mAlbumArtist;
            r1.mArtist = r0;
        L_0x0012:
            r3 = r18.toValues();
            r0 = "title";
            r4 = r3.getAsString(r0);
            r5 = "_data";
            if (r4 == 0) goto L_0x002b;
        L_0x0021:
            r6 = r4.trim();
            r6 = android.text.TextUtils.isEmpty(r6);
            if (r6 == 0) goto L_0x0036;
        L_0x002b:
            r6 = r3.getAsString(r5);
            r4 = android.media.MediaFile.getFileTitle(r6);
            r3.put(r0, r4);
        L_0x0036:
            r0 = "album";
            r6 = r3.getAsString(r0);
            r7 = "<unknown>";
            r7 = r7.equals(r6);
            if (r7 == 0) goto L_0x0069;
        L_0x0044:
            r6 = r3.getAsString(r5);
            r7 = 47;
            r8 = r6.lastIndexOf(r7);
            if (r8 < 0) goto L_0x0069;
        L_0x0050:
            r9 = 0;
        L_0x0051:
            r10 = r9 + 1;
            r10 = r6.indexOf(r7, r10);
            if (r10 < 0) goto L_0x005e;
        L_0x0059:
            if (r10 < r8) goto L_0x005c;
        L_0x005b:
            goto L_0x005e;
        L_0x005c:
            r9 = r10;
            goto L_0x0051;
        L_0x005e:
            if (r9 == 0) goto L_0x0069;
        L_0x0060:
            r7 = r9 + 1;
            r6 = r6.substring(r7, r8);
            r3.put(r0, r6);
        L_0x0069:
            r7 = r2.mRowId;
            r0 = r1.mMimeType;
            r0 = android.media.MediaFile.isAudioMimeType(r0);
            r9 = 0;
            if (r0 == 0) goto L_0x00bf;
        L_0x0075:
            r0 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
            if (r0 == 0) goto L_0x0081;
        L_0x0079:
            r0 = android.media.MediaScanner.this;
            r0 = r0.mMtpObjectHandle;
            if (r0 == 0) goto L_0x00bf;
        L_0x0081:
            r0 = java.lang.Boolean.valueOf(r20);
            r11 = "is_ringtone";
            r3.put(r11, r0);
            r0 = java.lang.Boolean.valueOf(r21);
            r11 = "is_notification";
            r3.put(r11, r0);
            r0 = java.lang.Boolean.valueOf(r22);
            r11 = "is_alarm";
            r3.put(r11, r0);
            r0 = java.lang.Boolean.valueOf(r25);
            r11 = "is_music";
            r3.put(r11, r0);
            r0 = java.lang.Boolean.valueOf(r23);
            r11 = "is_podcast";
            r3.put(r11, r0);
            r0 = java.lang.Boolean.valueOf(r24);
            r11 = "is_audiobook";
            r3.put(r11, r0);
            goto L_0x0135;
        L_0x00bf:
            r0 = r1.mMimeType;
            r0 = android.media.MediaFile.isExifMimeType(r0);
            if (r0 == 0) goto L_0x0135;
        L_0x00c7:
            r0 = r1.mNoMedia;
            if (r0 != 0) goto L_0x0135;
        L_0x00cb:
            r11 = 0;
            r0 = new android.media.ExifInterface;	 Catch:{ Exception -> 0x00d5 }
            r12 = r2.mPath;	 Catch:{ Exception -> 0x00d5 }
            r0.<init>(r12);	 Catch:{ Exception -> 0x00d5 }
            r11 = r0;
            goto L_0x00d6;
        L_0x00d5:
            r0 = move-exception;
        L_0x00d6:
            if (r11 == 0) goto L_0x0135;
        L_0x00d8:
            r12 = r11.getGpsDateTime();
            r14 = -1;
            r0 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1));
            r9 = "datetaken";
            if (r0 == 0) goto L_0x00ec;
        L_0x00e4:
            r0 = java.lang.Long.valueOf(r12);
            r3.put(r9, r0);
            goto L_0x010d;
        L_0x00ec:
            r12 = r11.getDateTime();
            r0 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1));
            if (r0 == 0) goto L_0x010d;
        L_0x00f4:
            r14 = r1.mLastModified;
            r16 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
            r14 = r14 * r16;
            r14 = r14 - r12;
            r14 = java.lang.Math.abs(r14);
            r16 = 86400000; // 0x5265c00 float:7.82218E-36 double:4.2687272E-316;
            r0 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1));
            if (r0 < 0) goto L_0x010d;
        L_0x0106:
            r0 = java.lang.Long.valueOf(r12);
            r3.put(r9, r0);
        L_0x010d:
            r0 = -1;
            r9 = "Orientation";
            r9 = r11.getAttributeInt(r9, r0);
            if (r9 == r0) goto L_0x0135;
        L_0x0116:
            r0 = 3;
            if (r9 == r0) goto L_0x0128;
        L_0x0119:
            r0 = 6;
            if (r9 == r0) goto L_0x0125;
        L_0x011c:
            r0 = 8;
            if (r9 == r0) goto L_0x0122;
        L_0x0120:
            r0 = 0;
            goto L_0x012b;
        L_0x0122:
            r0 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
            goto L_0x012b;
        L_0x0125:
            r0 = 90;
            goto L_0x012b;
        L_0x0128:
            r0 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        L_0x012b:
            r10 = java.lang.Integer.valueOf(r0);
            r14 = "orientation";
            r3.put(r14, r10);
        L_0x0135:
            r0 = android.media.MediaScanner.this;
            r0 = r0.mFilesUri;
            r9 = 0;
            r10 = android.media.MediaScanner.this;
            r10 = r10.mMediaInserter;
            r11 = r1.mNoMedia;
            if (r11 != 0) goto L_0x0185;
        L_0x0146:
            r11 = r1.mMimeType;
            r11 = android.media.MediaFile.isVideoMimeType(r11);
            if (r11 == 0) goto L_0x0156;
        L_0x014e:
            r11 = android.media.MediaScanner.this;
            r0 = r11.mVideoUri;
            r9 = 3;
            goto L_0x0185;
        L_0x0156:
            r11 = r1.mMimeType;
            r11 = android.media.MediaFile.isImageMimeType(r11);
            if (r11 == 0) goto L_0x0166;
        L_0x015e:
            r11 = android.media.MediaScanner.this;
            r0 = r11.mImagesUri;
            r9 = 1;
            goto L_0x0185;
        L_0x0166:
            r11 = r1.mMimeType;
            r11 = android.media.MediaFile.isAudioMimeType(r11);
            if (r11 == 0) goto L_0x0176;
        L_0x016e:
            r11 = android.media.MediaScanner.this;
            r0 = r11.mAudioUri;
            r9 = 2;
            goto L_0x0185;
        L_0x0176:
            r11 = r1.mMimeType;
            r11 = android.media.MediaFile.isPlayListMimeType(r11);
            if (r11 == 0) goto L_0x0185;
        L_0x017e:
            r11 = android.media.MediaScanner.this;
            r0 = r11.mPlaylistsUri;
            r9 = 4;
        L_0x0185:
            r11 = 0;
            r12 = 0;
            if (r21 == 0) goto L_0x01ad;
        L_0x0189:
            r13 = android.media.MediaScanner.this;
            r13 = r13.mDefaultNotificationSet;
            if (r13 != 0) goto L_0x01ad;
        L_0x0191:
            r13 = android.media.MediaScanner.this;
            r13 = r13.mDefaultNotificationFilename;
            r13 = android.text.TextUtils.isEmpty(r13);
            if (r13 != 0) goto L_0x01ab;
        L_0x019d:
            r13 = r2.mPath;
            r14 = android.media.MediaScanner.this;
            r14 = r14.mDefaultNotificationFilename;
            r13 = r1.doesPathHaveFilename(r13, r14);
            if (r13 == 0) goto L_0x01f8;
        L_0x01ab:
            r12 = 1;
            goto L_0x01f8;
        L_0x01ad:
            if (r20 == 0) goto L_0x01d3;
        L_0x01af:
            r13 = android.media.MediaScanner.this;
            r13 = r13.mDefaultRingtoneSet;
            if (r13 != 0) goto L_0x01d3;
        L_0x01b7:
            r13 = android.media.MediaScanner.this;
            r13 = r13.mDefaultRingtoneFilename;
            r13 = android.text.TextUtils.isEmpty(r13);
            if (r13 != 0) goto L_0x01d1;
        L_0x01c3:
            r13 = r2.mPath;
            r14 = android.media.MediaScanner.this;
            r14 = r14.mDefaultRingtoneFilename;
            r13 = r1.doesPathHaveFilename(r13, r14);
            if (r13 == 0) goto L_0x01f8;
        L_0x01d1:
            r12 = 1;
            goto L_0x01f8;
        L_0x01d3:
            if (r22 == 0) goto L_0x01f8;
        L_0x01d5:
            r13 = android.media.MediaScanner.this;
            r13 = r13.mDefaultAlarmSet;
            if (r13 != 0) goto L_0x01f8;
        L_0x01dd:
            r13 = android.media.MediaScanner.this;
            r13 = r13.mDefaultAlarmAlertFilename;
            r13 = android.text.TextUtils.isEmpty(r13);
            if (r13 != 0) goto L_0x01f7;
        L_0x01e9:
            r13 = r2.mPath;
            r14 = android.media.MediaScanner.this;
            r14 = r14.mDefaultAlarmAlertFilename;
            r13 = r1.doesPathHaveFilename(r13, r14);
            if (r13 == 0) goto L_0x01f8;
        L_0x01f7:
            r12 = 1;
        L_0x01f8:
            r13 = 0;
            r13 = (r7 > r13 ? 1 : (r7 == r13 ? 0 : -1));
            if (r13 != 0) goto L_0x025e;
        L_0x01fe:
            r5 = android.media.MediaScanner.this;
            r5 = r5.mMtpObjectHandle;
            if (r5 == 0) goto L_0x0216;
        L_0x0206:
            r5 = android.media.MediaScanner.this;
            r5 = r5.mMtpObjectHandle;
            r5 = java.lang.Integer.valueOf(r5);
            r13 = "media_scanner_new_object_id";
            r3.put(r13, r5);
        L_0x0216:
            r5 = android.media.MediaScanner.this;
            r5 = r5.mFilesUri;
            if (r0 != r5) goto L_0x0233;
        L_0x021e:
            r5 = r2.mFormat;
            if (r5 != 0) goto L_0x022a;
        L_0x0222:
            r13 = r2.mPath;
            r14 = r1.mMimeType;
            r5 = android.media.MediaFile.getFormatCode(r13, r14);
        L_0x022a:
            r13 = java.lang.Integer.valueOf(r5);
            r14 = "format";
            r3.put(r14, r13);
        L_0x0233:
            if (r10 == 0) goto L_0x0246;
        L_0x0235:
            if (r12 == 0) goto L_0x0238;
        L_0x0237:
            goto L_0x0246;
        L_0x0238:
            r5 = r2.mFormat;
            r13 = 12289; // 0x3001 float:1.722E-41 double:6.0716E-320;
            if (r5 != r13) goto L_0x0242;
        L_0x023e:
            r10.insertwithPriority(r0, r3);
            goto L_0x0255;
        L_0x0242:
            r10.insert(r0, r3);
            goto L_0x0255;
        L_0x0246:
            if (r10 == 0) goto L_0x024b;
        L_0x0248:
            r10.flushAll();
        L_0x024b:
            r5 = android.media.MediaScanner.this;
            r5 = r5.mMediaProvider;
            r11 = r5.insert(r0, r3);
        L_0x0255:
            if (r11 == 0) goto L_0x0299;
        L_0x0257:
            r7 = android.content.ContentUris.parseId(r11);
            r2.mRowId = r7;
            goto L_0x0299;
        L_0x025e:
            r11 = android.content.ContentUris.withAppendedId(r0, r7);
            r3.remove(r5);
            r5 = r1.mNoMedia;
            r13 = 0;
            if (r5 != 0) goto L_0x0290;
        L_0x026a:
            r5 = r2.mMediaType;
            if (r9 == r5) goto L_0x0290;
        L_0x026e:
            r5 = new android.content.ContentValues;
            r5.<init>();
            r14 = java.lang.Integer.valueOf(r9);
            r15 = "media_type";
            r5.put(r15, r14);
            r14 = android.media.MediaScanner.this;
            r14 = r14.mMediaProvider;
            r15 = android.media.MediaScanner.this;
            r15 = r15.mFilesUri;
            r15 = android.content.ContentUris.withAppendedId(r15, r7);
            r14.update(r15, r5, r13, r13);
        L_0x0290:
            r5 = android.media.MediaScanner.this;
            r5 = r5.mMediaProvider;
            r5.update(r11, r3, r13, r13);
        L_0x0299:
            if (r12 == 0) goto L_0x02c4;
        L_0x029b:
            r5 = 1;
            if (r21 == 0) goto L_0x02aa;
        L_0x029e:
            r13 = "notification_sound";
            r1.setRingtoneIfNotSet(r13, r0, r7);
            r13 = android.media.MediaScanner.this;
            r13.mDefaultNotificationSet = r5;
            goto L_0x02c4;
        L_0x02aa:
            if (r20 == 0) goto L_0x02b8;
        L_0x02ac:
            r13 = "ringtone";
            r1.setRingtoneIfNotSet(r13, r0, r7);
            r13 = android.media.MediaScanner.this;
            r13.mDefaultRingtoneSet = r5;
            goto L_0x02c4;
        L_0x02b8:
            if (r22 == 0) goto L_0x02c4;
        L_0x02ba:
            r13 = "alarm_alert";
            r1.setRingtoneIfNotSet(r13, r0, r7);
            r13 = android.media.MediaScanner.this;
            r13.mDefaultAlarmSet = r5;
        L_0x02c4:
            return r11;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner$MyMediaScannerClient.endFile(android.media.MediaScanner$FileEntry, boolean, boolean, boolean, boolean, boolean, boolean):android.net.Uri");
        }

        private boolean doesPathHaveFilename(String path, String filename) {
            int pathFilenameStart = path.lastIndexOf(File.separatorChar) + 1;
            int filenameLength = filename.length();
            if (path.regionMatches(pathFilenameStart, filename, 0, filenameLength) && pathFilenameStart + filenameLength == path.length()) {
                return true;
            }
            return false;
        }

        private void setRingtoneIfNotSet(String settingName, Uri uri, long rowId) {
            if (!MediaScanner.this.wasRingtoneAlreadySet(settingName)) {
                ContentResolver cr = MediaScanner.this.mContext.getContentResolver();
                if (TextUtils.isEmpty(System.getString(cr, settingName))) {
                    Uri settingUri = System.getUriFor(settingName);
                    RingtoneManager.setActualDefaultRingtoneUri(MediaScanner.this.mContext, RingtoneManager.getDefaultType(settingUri), ContentUris.withAppendedId(uri, rowId));
                }
                System.putInt(cr, MediaScanner.this.settingSetIndicatorName(settingName), 1);
            }
        }

        @Deprecated
        @UnsupportedAppUsage
        private int getFileTypeFromDrm(String path) {
            return 0;
        }

        private void getMimeTypeFromDrm(String path) {
            this.mMimeType = null;
            if (MediaScanner.this.mDrmManagerClient == null) {
                MediaScanner mediaScanner = MediaScanner.this;
                mediaScanner.mDrmManagerClient = new DrmManagerClient(mediaScanner.mContext);
            }
            if (MediaScanner.this.mDrmManagerClient.canHandle(path, null)) {
                this.mIsDrm = true;
                this.mMimeType = MediaScanner.this.mDrmManagerClient.getOriginalMimeType(path);
            }
            if (this.mMimeType == null) {
                this.mMimeType = ContentResolver.MIME_TYPE_DEFAULT;
            }
        }
    }

    private static class PlaylistEntry {
        long bestmatchid;
        int bestmatchlevel;
        String path;

        private PlaylistEntry() {
        }
    }

    class WplHandler implements ElementListener {
        final ContentHandler handler;
        String playListDirectory;

        public WplHandler(String playListDirectory, Uri uri, Cursor fileList) {
            this.playListDirectory = playListDirectory;
            RootElement root = new RootElement("smil");
            root.getChild("body").getChild("seq").getChild(MediaStore.AUTHORITY).setElementListener(this);
            this.handler = root.getContentHandler();
        }

        public void start(Attributes attributes) {
            String path = attributes.getValue("", "src");
            if (path != null) {
                MediaScanner.this.cachePlaylistEntry(path, this.playListDirectory);
            }
        }

        public void end() {
        }

        /* Access modifiers changed, original: 0000 */
        public ContentHandler getContentHandler() {
            return this.handler;
        }
    }

    private final native void native_finalize();

    private static final native void native_init();

    private final native void native_setup();

    private native void processDirectory(String str, MediaScannerClient mediaScannerClient);

    private native boolean processFile(String str, String str2, MediaScannerClient mediaScannerClient);

    @UnsupportedAppUsage
    private native void setLocale(String str);

    public native byte[] extractAlbumArt(FileDescriptor fileDescriptor);

    static {
        System.loadLibrary("media_jni");
        native_init();
        String str = "_id";
        FILES_PRESCAN_PROJECTION = new String[]{str, "_data", "format", "date_modified", "media_type"};
        ID_PROJECTION = new String[]{str};
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getRootDirectory());
        String str2 = "/media/audio";
        stringBuilder.append(str2);
        SYSTEM_SOUNDS_DIR = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getOemDirectory());
        stringBuilder.append(str2);
        OEM_SOUNDS_DIR = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getProductDirectory());
        stringBuilder.append(str2);
        PRODUCT_SOUNDS_DIR = stringBuilder.toString();
    }

    @UnsupportedAppUsage
    public MediaScanner(Context c, String volumeName) {
        native_setup();
        this.mContext = c;
        this.mPackageName = c.getPackageName();
        this.mVolumeName = volumeName;
        Options options = this.mBitmapOptions;
        options.inSampleSize = 1;
        options.inJustDecodeBounds = true;
        setDefaultRingtoneFileNames();
        this.mMediaProvider = this.mContext.getContentResolver().acquireContentProviderClient(MediaStore.AUTHORITY);
        if (sLastInternalScanFingerprint == null) {
            sLastInternalScanFingerprint = this.mContext.getSharedPreferences(SCANNED_BUILD_PREFS_NAME, 0).getString(LAST_INTERNAL_SCAN_FINGERPRINT, new String());
        }
        this.mAudioUri = Media.getContentUri(volumeName);
        this.mVideoUri = Video.Media.getContentUri(volumeName);
        this.mImagesUri = Images.Media.getContentUri(volumeName);
        this.mFilesUri = Files.getContentUri(volumeName);
        this.mFilesFullUri = MediaStore.setIncludeTrashed(MediaStore.setIncludePending(this.mFilesUri.buildUpon().appendQueryParameter("nonotify", "1").build()));
        if (volumeName.equals(MediaStore.VOLUME_INTERNAL)) {
            this.mProcessPlaylists = false;
            this.mProcessGenres = false;
            this.mPlaylistsUri = null;
        } else {
            this.mProcessPlaylists = true;
            this.mProcessGenres = true;
            this.mPlaylistsUri = Playlists.getContentUri(volumeName);
        }
        Locale locale = this.mContext.getResources().getConfiguration().locale;
        if (locale != null) {
            String language = locale.getLanguage();
            String country = locale.getCountry();
            if (language != null) {
                if (country != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(language);
                    stringBuilder.append(Session.SESSION_SEPARATION_CHAR_CHILD);
                    stringBuilder.append(country);
                    setLocale(stringBuilder.toString());
                } else {
                    setLocale(language);
                }
            }
        }
        this.mCloseGuard.open("close");
        MediaScannerInjector.initMediaFileCapture("media.extractor");
    }

    private void setDefaultRingtoneFileNames() {
        this.mDefaultRingtoneFilename = SystemProperties.get("ro.config.ringtone");
        this.mDefaultNotificationFilename = SystemProperties.get("ro.config.notification_sound");
        this.mDefaultAlarmAlertFilename = SystemProperties.get("ro.config.alarm_alert");
    }

    @UnsupportedAppUsage
    private boolean isDrmEnabled() {
        String prop = SystemProperties.get("drm.service.enabled");
        return prop != null && prop.equals("true");
    }

    private static boolean isSystemSoundWithMetadata(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SYSTEM_SOUNDS_DIR);
        String str = ALARMS_DIR;
        stringBuilder.append(str);
        if (!path.startsWith(stringBuilder.toString())) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(SYSTEM_SOUNDS_DIR);
            String str2 = RINGTONES_DIR;
            stringBuilder.append(str2);
            if (!path.startsWith(stringBuilder.toString())) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(SYSTEM_SOUNDS_DIR);
                String str3 = NOTIFICATIONS_DIR;
                stringBuilder.append(str3);
                if (!path.startsWith(stringBuilder.toString())) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(OEM_SOUNDS_DIR);
                    stringBuilder.append(str);
                    if (!path.startsWith(stringBuilder.toString())) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(OEM_SOUNDS_DIR);
                        stringBuilder.append(str2);
                        if (!path.startsWith(stringBuilder.toString())) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(OEM_SOUNDS_DIR);
                            stringBuilder.append(str3);
                            if (!path.startsWith(stringBuilder.toString())) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(PRODUCT_SOUNDS_DIR);
                                stringBuilder.append(str);
                                if (!path.startsWith(stringBuilder.toString())) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(PRODUCT_SOUNDS_DIR);
                                    stringBuilder.append(str2);
                                    if (!path.startsWith(stringBuilder.toString())) {
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append(PRODUCT_SOUNDS_DIR);
                                        stringBuilder.append(str3);
                                        if (!path.startsWith(stringBuilder.toString())) {
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private String settingSetIndicatorName(String base) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(base);
        stringBuilder.append("_set");
        return stringBuilder.toString();
    }

    private boolean wasRingtoneAlreadySet(String name) {
        boolean z = false;
        try {
            if (System.getInt(this.mContext.getContentResolver(), settingSetIndicatorName(name)) != 0) {
                z = true;
            }
            return z;
        } catch (SettingNotFoundException e) {
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:72:0x0176  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0176  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0176  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0176  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0176  */
    @android.annotation.UnsupportedAppUsage
    private void prescan(java.lang.String r26, boolean r27) throws android.os.RemoteException {
        /*
        r25 = this;
        r1 = r25;
        r2 = 0;
        r0 = 0;
        r3 = 0;
        r4 = r1.mPlayLists;
        r4.clear();
        r4 = 1;
        r5 = 2;
        r6 = "";
        r7 = 0;
        if (r26 == 0) goto L_0x001d;
    L_0x0011:
        r0 = "_id>? AND _data=?";
        r8 = new java.lang.String[r5];
        r8[r7] = r6;
        r8[r4] = r26;
        r3 = r8;
        r15 = r3;
        r3 = r0;
        goto L_0x0026;
    L_0x001d:
        r0 = "_id>?";
        r8 = new java.lang.String[]{r6};
        r3 = r8;
        r15 = r3;
        r3 = r0;
    L_0x0026:
        r0 = "ringtone";
        r0 = r1.wasRingtoneAlreadySet(r0);
        r1.mDefaultRingtoneSet = r0;
        r0 = "notification_sound";
        r0 = r1.wasRingtoneAlreadySet(r0);
        r1.mDefaultNotificationSet = r0;
        r0 = "alarm_alert";
        r0 = r1.wasRingtoneAlreadySet(r0);
        r1.mDefaultAlarmSet = r0;
        r0 = r1.mFilesUri;
        r14 = r0.buildUpon();
        r0 = "deletedata";
        r8 = "false";
        r14.appendQueryParameter(r0, r8);
        r0 = new android.media.MediaScanner$MediaBulkDeleter;
        r8 = r1.mMediaProvider;
        r9 = r14.build();
        r0.<init>(r8, r9);
        r13 = r0;
        if (r27 == 0) goto L_0x017d;
    L_0x005b:
        r10 = -9223372036854775808;
        r0 = r1.mFilesUri;	 Catch:{ all -> 0x016e }
        r0 = r0.buildUpon();	 Catch:{ all -> 0x016e }
        r8 = "limit";
        r9 = "1000";
        r0 = r0.appendQueryParameter(r8, r9);	 Catch:{ all -> 0x016e }
        r9 = r0.build();	 Catch:{ all -> 0x016e }
        r11 = r10;
    L_0x0071:
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x016e }
        r0.<init>();	 Catch:{ all -> 0x016e }
        r0.append(r6);	 Catch:{ all -> 0x016e }
        r0.append(r11);	 Catch:{ all -> 0x016e }
        r0 = r0.toString();	 Catch:{ all -> 0x016e }
        r15[r7] = r0;	 Catch:{ all -> 0x016e }
        if (r2 == 0) goto L_0x0092;
    L_0x0084:
        r2.close();	 Catch:{ all -> 0x008a }
        r0 = 0;
        r2 = r0;
        goto L_0x0092;
    L_0x008a:
        r0 = move-exception;
        r24 = r2;
        r4 = r13;
        r20 = r14;
        goto L_0x0174;
    L_0x0092:
        r8 = r1.mMediaProvider;	 Catch:{ all -> 0x016e }
        r10 = FILES_PRESCAN_PROJECTION;	 Catch:{ all -> 0x016e }
        r0 = "_id";
        r16 = 0;
        r17 = r11;
        r11 = r3;
        r12 = r15;
        r19 = r13;
        r13 = r0;
        r20 = r14;
        r14 = r16;
        r0 = r8.query(r9, r10, r11, r12, r13, r14);	 Catch:{ all -> 0x0168 }
        r2 = r0;
        if (r2 != 0) goto L_0x00ad;
    L_0x00ac:
        goto L_0x00b5;
    L_0x00ad:
        r0 = r2.getCount();	 Catch:{ all -> 0x0162 }
        r8 = r0;
        if (r8 != 0) goto L_0x00b9;
    L_0x00b5:
        r4 = r19;
        goto L_0x0180;
    L_0x00b9:
        r11 = r17;
    L_0x00bb:
        r0 = r2.moveToNext();	 Catch:{ all -> 0x0162 }
        if (r0 == 0) goto L_0x0156;
    L_0x00c1:
        r13 = r2.getLong(r7);	 Catch:{ all -> 0x0162 }
        r0 = r2.getString(r4);	 Catch:{ all -> 0x0162 }
        r10 = r0;
        r0 = r2.getInt(r5);	 Catch:{ all -> 0x0162 }
        r16 = r0;
        r0 = 3;
        r17 = r2.getLong(r0);	 Catch:{ all -> 0x0162 }
        r11 = r13;
        if (r10 == 0) goto L_0x0149;
    L_0x00d8:
        r0 = "/";
        r0 = r10.startsWith(r0);	 Catch:{ all -> 0x0162 }
        if (r0 == 0) goto L_0x0149;
    L_0x00e0:
        r21 = 0;
        r0 = android.system.OsConstants.F_OK;	 Catch:{ ErrnoException -> 0x00f2, all -> 0x00eb }
        r0 = android.system.Os.access(r10, r0);	 Catch:{ ErrnoException -> 0x00f2, all -> 0x00eb }
        r21 = r0;
        goto L_0x00f3;
    L_0x00eb:
        r0 = move-exception;
        r24 = r2;
        r4 = r19;
        goto L_0x0174;
    L_0x00f2:
        r0 = move-exception;
    L_0x00f3:
        if (r21 != 0) goto L_0x0144;
    L_0x00f5:
        r0 = android.mtp.MtpConstants.isAbstractObject(r16);	 Catch:{ all -> 0x0162 }
        if (r0 != 0) goto L_0x0144;
    L_0x00fb:
        r0 = android.media.MediaFile.getMimeTypeForFile(r10);	 Catch:{ all -> 0x0162 }
        r22 = android.media.MediaFile.isPlayListMimeType(r0);	 Catch:{ all -> 0x0162 }
        if (r22 != 0) goto L_0x013d;
    L_0x0105:
        r4 = r19;
        r4.delete(r13);	 Catch:{ all -> 0x0139 }
        r5 = java.util.Locale.US;	 Catch:{ all -> 0x0139 }
        r5 = r10.toLowerCase(r5);	 Catch:{ all -> 0x0139 }
        r7 = "/.nomedia";
        r5 = r5.endsWith(r7);	 Catch:{ all -> 0x0139 }
        if (r5 == 0) goto L_0x0134;
    L_0x0118:
        r4.flush();	 Catch:{ all -> 0x0139 }
        r5 = new java.io.File;	 Catch:{ all -> 0x0139 }
        r5.<init>(r10);	 Catch:{ all -> 0x0139 }
        r5 = r5.getParent();	 Catch:{ all -> 0x0139 }
        r7 = r1.mMediaProvider;	 Catch:{ all -> 0x0139 }
        r23 = r0;
        r0 = "unhide";
        r24 = r2;
        r2 = 0;
        r7.call(r0, r5, r2);	 Catch:{ all -> 0x0132 }
        goto L_0x014d;
    L_0x0132:
        r0 = move-exception;
        goto L_0x0174;
    L_0x0134:
        r23 = r0;
        r24 = r2;
        goto L_0x014d;
    L_0x0139:
        r0 = move-exception;
        r24 = r2;
        goto L_0x0174;
    L_0x013d:
        r23 = r0;
        r24 = r2;
        r4 = r19;
        goto L_0x014d;
    L_0x0144:
        r24 = r2;
        r4 = r19;
        goto L_0x014d;
    L_0x0149:
        r24 = r2;
        r4 = r19;
    L_0x014d:
        r19 = r4;
        r2 = r24;
        r4 = 1;
        r5 = 2;
        r7 = 0;
        goto L_0x00bb;
    L_0x0156:
        r24 = r2;
        r4 = r19;
        r13 = r4;
        r14 = r20;
        r4 = 1;
        r5 = 2;
        r7 = 0;
        goto L_0x0071;
    L_0x0162:
        r0 = move-exception;
        r24 = r2;
        r4 = r19;
        goto L_0x0174;
    L_0x0168:
        r0 = move-exception;
        r4 = r19;
        r24 = r2;
        goto L_0x0174;
    L_0x016e:
        r0 = move-exception;
        r4 = r13;
        r20 = r14;
        r24 = r2;
    L_0x0174:
        if (r24 == 0) goto L_0x0179;
    L_0x0176:
        r24.close();
    L_0x0179:
        r4.flush();
        throw r0;
    L_0x017d:
        r4 = r13;
        r20 = r14;
    L_0x0180:
        if (r2 == 0) goto L_0x0185;
    L_0x0182:
        r2.close();
    L_0x0185:
        r4.flush();
        r5 = 0;
        r1.mOriginalCount = r5;
        r6 = r1.mMediaProvider;
        r7 = r1.mImagesUri;
        r8 = ID_PROJECTION;
        r9 = 0;
        r10 = 0;
        r11 = 0;
        r12 = 0;
        r0 = r6.query(r7, r8, r9, r10, r11, r12);
        if (r0 == 0) goto L_0x01a5;
    L_0x019c:
        r2 = r0.getCount();
        r1.mOriginalCount = r2;
        r0.close();
    L_0x01a5:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.prescan(java.lang.String, boolean):void");
    }

    @UnsupportedAppUsage
    private void postscan(String[] directories) throws RemoteException {
        if (this.mProcessPlaylists) {
            processPlayLists();
        }
        this.mPlayLists.clear();
    }

    private void releaseResources() {
        DrmManagerClient drmManagerClient = this.mDrmManagerClient;
        if (drmManagerClient != null) {
            drmManagerClient.close();
            this.mDrmManagerClient = null;
        }
    }

    public void scanDirectories(String[] directories) {
        String str = TAG;
        try {
            long start = System.currentTimeMillis();
            prescan(null, true);
            long prescan = System.currentTimeMillis();
            this.mMediaInserter = new MediaInserter(this.mMediaProvider, 500);
            for (String processDirectory : directories) {
                processDirectory(processDirectory, this.mClient);
            }
            this.mMediaInserter.flushAll();
            this.mMediaInserter = null;
            long scan = System.currentTimeMillis();
            postscan(directories);
            System.currentTimeMillis();
        } catch (SQLException e) {
            Log.e(str, "SQLException in MediaScanner.scan()", e);
        } catch (UnsupportedOperationException e2) {
            Log.e(str, "UnsupportedOperationException in MediaScanner.scan()", e2);
        } catch (RemoteException e3) {
            Log.e(str, "RemoteException in MediaScanner.scan()", e3);
        } catch (Throwable th) {
            releaseResources();
        }
        releaseResources();
    }

    @UnsupportedAppUsage
    public Uri scanSingleFile(String path, String mimeType) {
        String str = path;
        try {
            prescan(path, true);
            File file = new File(path);
            if (file.exists()) {
                if (file.canRead()) {
                    String str2 = path;
                    String str3 = mimeType;
                    Uri doScanFile = this.mClient.doScanFile(str2, str3, file.lastModified() / 1000, file.length(), false, true, isNoMediaPath(path));
                    releaseResources();
                    return doScanFile;
                }
            }
            releaseResources();
            return null;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in MediaScanner.scanFile()", e);
            releaseResources();
            return null;
        } catch (Throwable th) {
            releaseResources();
            throw th;
        }
    }

    /* JADX WARNING: Missing block: B:19:0x0072, code skipped:
            if (r12.regionMatches(true, r1 + 1, "AlbumArtSmall", 0, 13) == false) goto L_0x0074;
     */
    private static boolean isNoMediaFile(java.lang.String r12) {
        /*
        r0 = new java.io.File;
        r0.<init>(r12);
        r1 = r0.isDirectory();
        r2 = 0;
        if (r1 == 0) goto L_0x000d;
    L_0x000c:
        return r2;
    L_0x000d:
        r1 = 47;
        r1 = r12.lastIndexOf(r1);
        if (r1 < 0) goto L_0x0088;
    L_0x0015:
        r3 = r1 + 2;
        r4 = r12.length();
        if (r3 >= r4) goto L_0x0088;
    L_0x001d:
        r3 = r1 + 1;
        r4 = 2;
        r5 = "._";
        r3 = r12.regionMatches(r3, r5, r2, r4);
        r4 = 1;
        if (r3 == 0) goto L_0x002a;
    L_0x0029:
        return r4;
    L_0x002a:
        r6 = 1;
        r3 = r12.length();
        r7 = r3 + -4;
        r9 = 0;
        r10 = 4;
        r8 = ".jpg";
        r5 = r12;
        r3 = r5.regionMatches(r6, r7, r8, r9, r10);
        if (r3 == 0) goto L_0x0088;
    L_0x003c:
        r6 = 1;
        r7 = r1 + 1;
        r9 = 0;
        r10 = 10;
        r8 = "AlbumArt_{";
        r5 = r12;
        r3 = r5.regionMatches(r6, r7, r8, r9, r10);
        if (r3 != 0) goto L_0x0087;
    L_0x004b:
        r6 = 1;
        r7 = r1 + 1;
        r9 = 0;
        r10 = 9;
        r8 = "AlbumArt.";
        r5 = r12;
        r3 = r5.regionMatches(r6, r7, r8, r9, r10);
        if (r3 == 0) goto L_0x005b;
    L_0x005a:
        goto L_0x0087;
    L_0x005b:
        r3 = r12.length();
        r3 = r3 - r1;
        r3 = r3 - r4;
        r5 = 17;
        if (r3 != r5) goto L_0x0074;
    L_0x0065:
        r7 = 1;
        r8 = r1 + 1;
        r10 = 0;
        r11 = 13;
        r9 = "AlbumArtSmall";
        r6 = r12;
        r5 = r6.regionMatches(r7, r8, r9, r10, r11);
        if (r5 != 0) goto L_0x0086;
    L_0x0074:
        r5 = 10;
        if (r3 != r5) goto L_0x0088;
    L_0x0078:
        r7 = 1;
        r8 = r1 + 1;
        r10 = 0;
        r11 = 6;
        r9 = "Folder";
        r6 = r12;
        r5 = r6.regionMatches(r7, r8, r9, r10, r11);
        if (r5 == 0) goto L_0x0088;
    L_0x0086:
        return r4;
    L_0x0087:
        return r4;
    L_0x0088:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.isNoMediaFile(java.lang.String):boolean");
    }

    public static void clearMediaPathCache(boolean clearMediaPaths, boolean clearNoMediaPaths) {
        synchronized (MediaScanner.class) {
            if (clearMediaPaths) {
                try {
                    mMediaPaths.clear();
                } catch (Throwable th) {
                }
            }
            if (clearNoMediaPaths) {
                mNoMediaPaths.clear();
            }
        }
    }

    /* JADX WARNING: Missing block: B:31:0x0072, code skipped:
            return isNoMediaFile(r11);
     */
    @android.annotation.UnsupportedAppUsage
    public static boolean isNoMediaPath(java.lang.String r11) {
        /*
        r0 = 0;
        if (r11 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = "/.";
        r1 = r11.indexOf(r1);
        r2 = 1;
        if (r1 < 0) goto L_0x000e;
    L_0x000d:
        return r2;
    L_0x000e:
        r1 = 47;
        r3 = r11.lastIndexOf(r1);
        if (r3 > 0) goto L_0x0017;
    L_0x0016:
        return r0;
    L_0x0017:
        r4 = r11.substring(r0, r3);
        r5 = android.media.MediaScanner.class;
        monitor-enter(r5);
        r6 = mNoMediaPaths;	 Catch:{ all -> 0x0073 }
        r6 = r6.containsKey(r4);	 Catch:{ all -> 0x0073 }
        if (r6 == 0) goto L_0x0028;
    L_0x0026:
        monitor-exit(r5);	 Catch:{ all -> 0x0073 }
        return r2;
    L_0x0028:
        r6 = mMediaPaths;	 Catch:{ all -> 0x0073 }
        r6 = r6.containsKey(r4);	 Catch:{ all -> 0x0073 }
        if (r6 != 0) goto L_0x006d;
    L_0x0030:
        r6 = 1;
    L_0x0031:
        if (r6 < 0) goto L_0x0066;
    L_0x0033:
        r7 = r11.indexOf(r1, r6);	 Catch:{ all -> 0x0073 }
        if (r7 <= r6) goto L_0x0064;
    L_0x0039:
        r7 = r7 + 1;
        r8 = new java.io.File;	 Catch:{ all -> 0x0073 }
        r9 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0073 }
        r9.<init>();	 Catch:{ all -> 0x0073 }
        r10 = r11.substring(r0, r7);	 Catch:{ all -> 0x0073 }
        r9.append(r10);	 Catch:{ all -> 0x0073 }
        r10 = ".nomedia";
        r9.append(r10);	 Catch:{ all -> 0x0073 }
        r9 = r9.toString();	 Catch:{ all -> 0x0073 }
        r8.<init>(r9);	 Catch:{ all -> 0x0073 }
        r9 = r8.exists();	 Catch:{ all -> 0x0073 }
        if (r9 == 0) goto L_0x0064;
    L_0x005b:
        r0 = mNoMediaPaths;	 Catch:{ all -> 0x0073 }
        r1 = "";
        r0.put(r4, r1);	 Catch:{ all -> 0x0073 }
        monitor-exit(r5);	 Catch:{ all -> 0x0073 }
        return r2;
    L_0x0064:
        r6 = r7;
        goto L_0x0031;
    L_0x0066:
        r0 = mMediaPaths;	 Catch:{ all -> 0x0073 }
        r1 = "";
        r0.put(r4, r1);	 Catch:{ all -> 0x0073 }
    L_0x006d:
        monitor-exit(r5);	 Catch:{ all -> 0x0073 }
        r0 = isNoMediaFile(r11);
        return r0;
    L_0x0073:
        r0 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0073 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.isNoMediaPath(java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:57:0x00fe  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00fe  */
    /* JADX WARNING: Missing block: B:41:0x00d9, code skipped:
            if (r18 != null) goto L_0x00f1;
     */
    /* JADX WARNING: Missing block: B:50:0x00ef, code skipped:
            if (r18 == null) goto L_0x00f4;
     */
    /* JADX WARNING: Missing block: B:51:0x00f1, code skipped:
            r18.close();
     */
    /* JADX WARNING: Missing block: B:52:0x00f4, code skipped:
            releaseResources();
     */
    /* JADX WARNING: Missing block: B:53:0x00f8, code skipped:
            return;
     */
    public void scanMtpFile(java.lang.String r24, int r25, int r26) {
        /*
        r23 = this;
        r1 = r23;
        r12 = r24;
        r13 = r26;
        r14 = android.media.MediaFile.getMimeType(r12, r13);
        r0 = new java.io.File;
        r0.<init>(r12);
        r15 = r0;
        r2 = r15.lastModified();
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r16 = r2 / r4;
        r0 = android.media.MediaFile.isAudioMimeType(r14);
        r11 = "MediaScanner";
        r2 = 1;
        r10 = 0;
        if (r0 != 0) goto L_0x0074;
    L_0x0022:
        r0 = android.media.MediaFile.isVideoMimeType(r14);
        if (r0 != 0) goto L_0x0074;
    L_0x0028:
        r0 = android.media.MediaFile.isImageMimeType(r14);
        if (r0 != 0) goto L_0x0074;
    L_0x002e:
        r0 = android.media.MediaFile.isPlayListMimeType(r14);
        if (r0 != 0) goto L_0x0074;
    L_0x0034:
        r0 = android.media.MediaFile.isDrmMimeType(r14);
        if (r0 != 0) goto L_0x0074;
    L_0x003a:
        r0 = new android.content.ContentValues;
        r0.<init>();
        r3 = r0;
        r4 = r15.length();
        r0 = java.lang.Long.valueOf(r4);
        r4 = "_size";
        r3.put(r4, r0);
        r0 = java.lang.Long.valueOf(r16);
        r4 = "date_modified";
        r3.put(r4, r0);
        r0 = new java.lang.String[r2];	 Catch:{ RemoteException -> 0x006d }
        r2 = java.lang.Integer.toString(r25);	 Catch:{ RemoteException -> 0x006d }
        r0[r10] = r2;	 Catch:{ RemoteException -> 0x006d }
        r2 = r1.mMediaProvider;	 Catch:{ RemoteException -> 0x006d }
        r4 = r1.mVolumeName;	 Catch:{ RemoteException -> 0x006d }
        r4 = android.provider.MediaStore.Files.getMtpObjectsUri(r4);	 Catch:{ RemoteException -> 0x006d }
        r5 = "_id=?";
        r2.update(r4, r3, r5, r0);	 Catch:{ RemoteException -> 0x006d }
        goto L_0x0073;
    L_0x006d:
        r0 = move-exception;
        r2 = "RemoteException in scanMtpFile";
        android.util.Log.e(r11, r2, r0);
    L_0x0073:
        return;
    L_0x0074:
        r9 = r25;
        r1.mMtpObjectHandle = r9;
        r18 = 0;
        r0 = android.media.MediaFile.isPlayListMimeType(r14);	 Catch:{ RemoteException -> 0x00e1, all -> 0x00de }
        if (r0 == 0) goto L_0x00ad;
    L_0x0080:
        r0 = 0;
        r1.prescan(r0, r2);	 Catch:{ RemoteException -> 0x00e1, all -> 0x00de }
        r0 = r23.makeEntryFor(r24);	 Catch:{ RemoteException -> 0x00e1, all -> 0x00de }
        if (r0 == 0) goto L_0x00ab;
    L_0x008a:
        r2 = r1.mMediaProvider;	 Catch:{ RemoteException -> 0x00e1, all -> 0x00de }
        r3 = r1.mFilesUri;	 Catch:{ RemoteException -> 0x00e1, all -> 0x00de }
        r4 = FILES_PRESCAN_PROJECTION;	 Catch:{ RemoteException -> 0x00e1, all -> 0x00de }
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r2 = r2.query(r3, r4, r5, r6, r7, r8);	 Catch:{ RemoteException -> 0x00e1, all -> 0x00de }
        r1.processPlayList(r0, r2);	 Catch:{ RemoteException -> 0x00a4, all -> 0x009e }
        r18 = r2;
        goto L_0x00ab;
    L_0x009e:
        r0 = move-exception;
        r18 = r2;
        r12 = r10;
        goto L_0x00fa;
    L_0x00a4:
        r0 = move-exception;
        r18 = r2;
        r12 = r10;
        r22 = r11;
        goto L_0x00e5;
    L_0x00ab:
        r12 = r10;
        goto L_0x00d7;
    L_0x00ad:
        r1.prescan(r12, r10);	 Catch:{ RemoteException -> 0x00e1, all -> 0x00de }
        r0 = r1.mClient;	 Catch:{ RemoteException -> 0x00e1, all -> 0x00de }
        r7 = r15.length();	 Catch:{ RemoteException -> 0x00e1, all -> 0x00de }
        r3 = 12289; // 0x3001 float:1.722E-41 double:6.0716E-320;
        if (r13 != r3) goto L_0x00bd;
    L_0x00ba:
        r19 = r2;
        goto L_0x00bf;
    L_0x00bd:
        r19 = r10;
    L_0x00bf:
        r20 = 1;
        r21 = isNoMediaPath(r24);	 Catch:{ RemoteException -> 0x00e1, all -> 0x00de }
        r2 = r0;
        r3 = r24;
        r4 = r14;
        r5 = r16;
        r9 = r19;
        r12 = r10;
        r10 = r20;
        r22 = r11;
        r11 = r21;
        r2.doScanFile(r3, r4, r5, r7, r9, r10, r11);	 Catch:{ RemoteException -> 0x00dc }
    L_0x00d7:
        r1.mMtpObjectHandle = r12;
        if (r18 == 0) goto L_0x00f4;
    L_0x00db:
        goto L_0x00f1;
    L_0x00dc:
        r0 = move-exception;
        goto L_0x00e5;
    L_0x00de:
        r0 = move-exception;
        r12 = r10;
        goto L_0x00fa;
    L_0x00e1:
        r0 = move-exception;
        r12 = r10;
        r22 = r11;
    L_0x00e5:
        r2 = "RemoteException in MediaScanner.scanFile()";
        r3 = r22;
        android.util.Log.e(r3, r2, r0);	 Catch:{ all -> 0x00f9 }
        r1.mMtpObjectHandle = r12;
        if (r18 == 0) goto L_0x00f4;
    L_0x00f1:
        r18.close();
    L_0x00f4:
        r23.releaseResources();
        return;
    L_0x00f9:
        r0 = move-exception;
    L_0x00fa:
        r1.mMtpObjectHandle = r12;
        if (r18 == 0) goto L_0x0101;
    L_0x00fe:
        r18.close();
    L_0x0101:
        r23.releaseResources();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.scanMtpFile(java.lang.String, int, int):void");
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public FileEntry makeEntryFor(String path) {
        Cursor c = null;
        try {
            String[] selectionArgs = new String[]{path};
            c = this.mMediaProvider.query(this.mFilesFullUri, FILES_PRESCAN_PROJECTION, "_data=?", selectionArgs, null, null);
            if (c == null || !c.moveToFirst()) {
                if (c != null) {
                    c.close();
                }
                return null;
            }
            FileEntry fileEntry = new FileEntry(c.getLong(0), path, c.getLong(3), c.getInt(2), c.getInt(4));
            c.close();
            return fileEntry;
        } catch (RemoteException e) {
            if (c != null) {
                c.close();
            }
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
            throw th;
        }
    }

    private int matchPaths(String path1, String path2) {
        String str = path1;
        String str2 = path2;
        int result = 0;
        int end1 = path1.length();
        int end2 = path2.length();
        while (end1 > 0 && end2 > 0) {
            int slash1 = str.lastIndexOf(47, end1 - 1);
            int slash2 = str2.lastIndexOf(47, end2 - 1);
            int backSlash1 = str.lastIndexOf(92, end1 - 1);
            int backSlash2 = str2.lastIndexOf(92, end2 - 1);
            int start1 = slash1 > backSlash1 ? slash1 : backSlash1;
            int start2 = slash2 > backSlash2 ? slash2 : backSlash2;
            int start12 = start1 < 0 ? 0 : start1 + 1;
            int start22 = start2 < 0 ? 0 : start2 + 1;
            int length = end1 - start12;
            if (end2 - start22 == length) {
                if (!path1.regionMatches(true, start12, path2, start22, length)) {
                    break;
                }
                result++;
                end1 = start12 - 1;
                end2 = start22 - 1;
            } else {
                break;
            }
        }
        return result;
    }

    private boolean matchEntries(long rowId, String data) {
        int len = this.mPlaylistEntries.size();
        boolean done = true;
        for (int i = 0; i < len; i++) {
            PlaylistEntry entry = (PlaylistEntry) this.mPlaylistEntries.get(i);
            if (entry.bestmatchlevel != Integer.MAX_VALUE) {
                done = false;
                if (data.equalsIgnoreCase(entry.path)) {
                    entry.bestmatchid = rowId;
                    entry.bestmatchlevel = Integer.MAX_VALUE;
                } else {
                    int matchLength = matchPaths(data, entry.path);
                    if (matchLength > entry.bestmatchlevel) {
                        entry.bestmatchid = rowId;
                        entry.bestmatchlevel = matchLength;
                    }
                }
            }
        }
        return done;
    }

    private void cachePlaylistEntry(String line, String playListDirectory) {
        PlaylistEntry entry = new PlaylistEntry();
        int entryLength = line.length();
        while (entryLength > 0 && Character.isWhitespace(line.charAt(entryLength - 1))) {
            entryLength--;
        }
        if (entryLength >= 3) {
            boolean fullPath = false;
            if (entryLength < line.length()) {
                line = line.substring(0, entryLength);
            }
            char ch1 = line.charAt(0);
            if (ch1 == '/' || (Character.isLetter(ch1) && line.charAt(1) == ':' && line.charAt(2) == '\\')) {
                fullPath = true;
            }
            if (!fullPath) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(playListDirectory);
                stringBuilder.append(line);
                line = stringBuilder.toString();
            }
            entry.path = line;
            this.mPlaylistEntries.add(entry);
        }
    }

    private void processCachedPlaylist(Cursor fileList, ContentValues values, Uri playlistUri) {
        fileList.moveToPosition(-1);
        while (fileList.moveToNext() && !matchEntries(fileList.getLong(0), fileList.getString(1))) {
        }
        int len = this.mPlaylistEntries.size();
        int index = 0;
        for (int i = 0; i < len; i++) {
            PlaylistEntry entry = (PlaylistEntry) this.mPlaylistEntries.get(i);
            if (entry.bestmatchlevel > 0) {
                try {
                    values.clear();
                    values.put("play_order", Integer.valueOf(index));
                    values.put("audio_id", Long.valueOf(entry.bestmatchid));
                    this.mMediaProvider.insert(playlistUri, values);
                    index++;
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in MediaScanner.processCachedPlaylist()", e);
                    return;
                }
            }
        }
        this.mPlaylistEntries.clear();
    }

    private void processM3uPlayList(String path, String playListDirectory, Uri uri, ContentValues values, Cursor fileList) {
        String str = "IOException in MediaScanner.processM3uPlayList()";
        String str2 = TAG;
        BufferedReader reader = null;
        try {
            File f = new File(path);
            if (f.exists()) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)), 8192);
                String line = reader.readLine();
                this.mPlaylistEntries.clear();
                while (line != null) {
                    if (line.length() > 0 && line.charAt(0) != '#') {
                        cachePlaylistEntry(line, playListDirectory);
                    }
                    line = reader.readLine();
                }
                processCachedPlaylist(fileList, values, uri);
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(str2, str, e);
                }
            }
        } catch (IOException e2) {
            Log.e(str2, str, e2);
            if (reader != null) {
                reader.close();
            }
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e3) {
                    Log.e(str2, str, e3);
                }
            }
        }
    }

    private void processPlsPlayList(String path, String playListDirectory, Uri uri, ContentValues values, Cursor fileList) {
        String str = "IOException in MediaScanner.processPlsPlayList()";
        String str2 = TAG;
        BufferedReader reader = null;
        try {
            File f = new File(path);
            if (f.exists()) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)), 8192);
                this.mPlaylistEntries.clear();
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    if (line.startsWith("File")) {
                        int equals = line.indexOf(61);
                        if (equals > 0) {
                            cachePlaylistEntry(line.substring(equals + 1), playListDirectory);
                        }
                    }
                }
                processCachedPlaylist(fileList, values, uri);
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(str2, str, e);
                }
            }
        } catch (IOException e2) {
            Log.e(str2, str, e2);
            if (reader != null) {
                reader.close();
            }
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e3) {
                    Log.e(str2, str, e3);
                }
            }
        }
    }

    private void processWplPlayList(String path, String playListDirectory, Uri uri, ContentValues values, Cursor fileList) {
        String str = "IOException in MediaScanner.processWplPlayList()";
        String str2 = TAG;
        FileInputStream fis = null;
        try {
            File f = new File(path);
            if (f.exists()) {
                fis = new FileInputStream(f);
                this.mPlaylistEntries.clear();
                Xml.parse(fis, Xml.findEncodingByName("UTF-8"), new WplHandler(playListDirectory, uri, fileList).getContentHandler());
                processCachedPlaylist(fileList, values, uri);
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.e(str2, str, e);
                }
            }
        } catch (SAXException e2) {
            e2.printStackTrace();
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            if (fis != null) {
                fis.close();
            }
        } catch (Throwable th) {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e4) {
                    Log.e(str2, str, e4);
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x00f1  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00f1  */
    /* JADX WARNING: Missing block: B:24:0x00b2, code skipped:
            if (r5.equals("application/vnd.ms-wpl") == false) goto L_0x00c9;
     */
    private void processPlayList(android.media.MediaScanner.FileEntry r19, android.database.Cursor r20) throws android.os.RemoteException {
        /*
        r18 = this;
        r6 = r18;
        r7 = r19;
        r8 = r7.mPath;
        r0 = new android.content.ContentValues;
        r0.<init>();
        r9 = r0;
        r0 = 47;
        r10 = r8.lastIndexOf(r0);
        if (r10 < 0) goto L_0x0101;
    L_0x0014:
        r0 = r7.mRowId;
        r2 = "name";
        r3 = r9.getAsString(r2);
        if (r3 != 0) goto L_0x0042;
    L_0x001f:
        r4 = "title";
        r3 = r9.getAsString(r4);
        if (r3 != 0) goto L_0x0040;
    L_0x0028:
        r4 = 46;
        r4 = r8.lastIndexOf(r4);
        if (r4 >= 0) goto L_0x0037;
    L_0x0030:
        r5 = r10 + 1;
        r5 = r8.substring(r5);
        goto L_0x003d;
    L_0x0037:
        r5 = r10 + 1;
        r5 = r8.substring(r5, r4);
    L_0x003d:
        r3 = r5;
        r11 = r3;
        goto L_0x0043;
    L_0x0040:
        r11 = r3;
        goto L_0x0043;
    L_0x0042:
        r11 = r3;
    L_0x0043:
        r9.put(r2, r11);
        r2 = r7.mLastModified;
        r2 = java.lang.Long.valueOf(r2);
        r3 = "date_modified";
        r9.put(r3, r2);
        r2 = 0;
        r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        r3 = "members";
        if (r2 != 0) goto L_0x0073;
    L_0x005a:
        r2 = "_data";
        r9.put(r2, r8);
        r2 = r6.mMediaProvider;
        r4 = r6.mPlaylistsUri;
        r2 = r2.insert(r4, r9);
        r0 = android.content.ContentUris.parseId(r2);
        r3 = android.net.Uri.withAppendedPath(r2, r3);
        r12 = r0;
        r14 = r2;
        r15 = r3;
        goto L_0x008b;
    L_0x0073:
        r2 = r6.mPlaylistsUri;
        r2 = android.content.ContentUris.withAppendedId(r2, r0);
        r4 = r6.mMediaProvider;
        r5 = 0;
        r4.update(r2, r9, r5, r5);
        r3 = android.net.Uri.withAppendedPath(r2, r3);
        r4 = r6.mMediaProvider;
        r4.delete(r3, r5, r5);
        r12 = r0;
        r14 = r2;
        r15 = r3;
    L_0x008b:
        r0 = r10 + 1;
        r1 = 0;
        r16 = r8.substring(r1, r0);
        r5 = android.media.MediaFile.getMimeTypeForFile(r8);
        r2 = r5.hashCode();
        r3 = -1165508903; // 0xffffffffba87bed9 float:-0.001035656 double:NaN;
        r4 = 2;
        r0 = 1;
        if (r2 == r3) goto L_0x00bf;
    L_0x00a1:
        r3 = 264230524; // 0xfbfd67c float:1.891667E-29 double:1.305472245E-315;
        if (r2 == r3) goto L_0x00b5;
    L_0x00a6:
        r3 = 1872259501; // 0x6f9869ad float:9.433895E28 double:9.250190995E-315;
        if (r2 == r3) goto L_0x00ac;
    L_0x00ab:
        goto L_0x00c9;
    L_0x00ac:
        r2 = "application/vnd.ms-wpl";
        r2 = r5.equals(r2);
        if (r2 == 0) goto L_0x00ab;
    L_0x00b4:
        goto L_0x00ca;
    L_0x00b5:
        r1 = "audio/x-mpegurl";
        r1 = r5.equals(r1);
        if (r1 == 0) goto L_0x00ab;
    L_0x00bd:
        r1 = r0;
        goto L_0x00ca;
    L_0x00bf:
        r1 = "audio/x-scpls";
        r1 = r5.equals(r1);
        if (r1 == 0) goto L_0x00ab;
    L_0x00c7:
        r1 = r4;
        goto L_0x00ca;
    L_0x00c9:
        r1 = -1;
    L_0x00ca:
        if (r1 == 0) goto L_0x00f1;
    L_0x00cc:
        if (r1 == r0) goto L_0x00e2;
    L_0x00ce:
        if (r1 == r4) goto L_0x00d3;
    L_0x00d0:
        r17 = r5;
        goto L_0x0100;
    L_0x00d3:
        r0 = r18;
        r1 = r8;
        r2 = r16;
        r3 = r15;
        r4 = r9;
        r17 = r5;
        r5 = r20;
        r0.processPlsPlayList(r1, r2, r3, r4, r5);
        goto L_0x0100;
    L_0x00e2:
        r17 = r5;
        r0 = r18;
        r1 = r8;
        r2 = r16;
        r3 = r15;
        r4 = r9;
        r5 = r20;
        r0.processM3uPlayList(r1, r2, r3, r4, r5);
        goto L_0x0100;
    L_0x00f1:
        r17 = r5;
        r0 = r18;
        r1 = r8;
        r2 = r16;
        r3 = r15;
        r4 = r9;
        r5 = r20;
        r0.processWplPlayList(r1, r2, r3, r4, r5);
    L_0x0100:
        return;
    L_0x0101:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "bad path ";
        r1.append(r2);
        r1.append(r8);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.processPlayList(android.media.MediaScanner$FileEntry, android.database.Cursor):void");
    }

    /* JADX WARNING: Missing block: B:21:?, code skipped:
            return;
     */
    private void processPlayLists() throws android.os.RemoteException {
        /*
        r9 = this;
        r0 = r9.mPlayLists;
        r0 = r0.iterator();
        r1 = 0;
        r2 = r9.mMediaProvider;	 Catch:{ RemoteException -> 0x0039, all -> 0x0032 }
        r3 = r9.mFilesUri;	 Catch:{ RemoteException -> 0x0039, all -> 0x0032 }
        r4 = FILES_PRESCAN_PROJECTION;	 Catch:{ RemoteException -> 0x0039, all -> 0x0032 }
        r5 = "media_type=2";
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r2 = r2.query(r3, r4, r5, r6, r7, r8);	 Catch:{ RemoteException -> 0x0039, all -> 0x0032 }
        r1 = r2;
    L_0x0018:
        r2 = r0.hasNext();	 Catch:{ RemoteException -> 0x0039, all -> 0x0032 }
        if (r2 == 0) goto L_0x002c;
    L_0x001e:
        r2 = r0.next();	 Catch:{ RemoteException -> 0x0039, all -> 0x0032 }
        r2 = (android.media.MediaScanner.FileEntry) r2;	 Catch:{ RemoteException -> 0x0039, all -> 0x0032 }
        r3 = r2.mLastModifiedChanged;	 Catch:{ RemoteException -> 0x0039, all -> 0x0032 }
        if (r3 == 0) goto L_0x002b;
    L_0x0028:
        r9.processPlayList(r2, r1);	 Catch:{ RemoteException -> 0x0039, all -> 0x0032 }
    L_0x002b:
        goto L_0x0018;
    L_0x002c:
        if (r1 == 0) goto L_0x003d;
    L_0x002e:
        r1.close();
        goto L_0x003d;
    L_0x0032:
        r2 = move-exception;
        if (r1 == 0) goto L_0x0038;
    L_0x0035:
        r1.close();
    L_0x0038:
        throw r2;
    L_0x0039:
        r2 = move-exception;
        if (r1 == 0) goto L_0x003d;
    L_0x003c:
        goto L_0x002e;
    L_0x003d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.processPlayLists():void");
    }

    public void close() {
        this.mCloseGuard.close();
        if (this.mClosed.compareAndSet(false, true)) {
            this.mMediaProvider.close();
            native_finalize();
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            close();
        } finally {
            super.finalize();
        }
    }
}
