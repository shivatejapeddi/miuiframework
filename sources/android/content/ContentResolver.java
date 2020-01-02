package android.content;

import android.accounts.Account;
import android.annotation.RequiresPermission.Read;
import android.annotation.RequiresPermission.Write;
import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.UriGrantsManager;
import android.content.ISyncStatusObserver.Stub;
import android.content.SyncRequest.Builder;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.ContentObserver;
import android.database.CrossProcessCursorWrapper;
import android.database.Cursor;
import android.database.IContentObserver;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.ImageInfo;
import android.graphics.ImageDecoder.Source;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.miui.BiometricConnect;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.DeadObjectException;
import android.os.ICancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.provider.ContactsContract.Directory;
import android.provider.DocumentsContract;
import android.system.Int32Ref;
import android.text.TextUtils;
import android.util.Log;
import android.util.SeempLog;
import android.util.Size;
import com.android.internal.util.MimeIconUtils;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ContentResolver implements ContentInterface {
    public static final Intent ACTION_SYNC_CONN_STATUS_CHANGED = new Intent("com.android.sync.SYNC_CONN_STATUS_CHANGED");
    public static final String ANY_CURSOR_ITEM_TYPE = "vnd.android.cursor.item/*";
    public static final String CONTENT_SERVICE_NAME = "content";
    public static final String CURSOR_DIR_BASE_TYPE = "vnd.android.cursor.dir";
    public static final String CURSOR_ITEM_BASE_TYPE = "vnd.android.cursor.item";
    public static final boolean DEPRECATE_DATA_COLUMNS = StorageManager.hasIsolatedStorage();
    public static final String DEPRECATE_DATA_PREFIX = "/mnt/content/";
    private static final boolean ENABLE_CONTENT_SAMPLE = false;
    public static final String EXTRA_HONORED_ARGS = "android.content.extra.HONORED_ARGS";
    public static final String EXTRA_REFRESH_SUPPORTED = "android.content.extra.REFRESH_SUPPORTED";
    public static final String EXTRA_SIZE = "android.content.extra.SIZE";
    public static final String EXTRA_TOTAL_COUNT = "android.content.extra.TOTAL_COUNT";
    public static final String MIME_TYPE_DEFAULT = "application/octet-stream";
    public static final int NOTIFY_SKIP_NOTIFY_FOR_DESCENDANTS = 2;
    public static final int NOTIFY_SYNC_TO_NETWORK = 1;
    public static final String QUERY_ARG_LIMIT = "android:query-arg-limit";
    public static final String QUERY_ARG_OFFSET = "android:query-arg-offset";
    public static final String QUERY_ARG_SORT_COLLATION = "android:query-arg-sort-collation";
    public static final String QUERY_ARG_SORT_COLUMNS = "android:query-arg-sort-columns";
    public static final String QUERY_ARG_SORT_DIRECTION = "android:query-arg-sort-direction";
    public static final String QUERY_ARG_SQL_GROUP_BY = "android:query-arg-sql-group-by";
    public static final String QUERY_ARG_SQL_HAVING = "android:query-arg-sql-having";
    public static final String QUERY_ARG_SQL_LIMIT = "android:query-arg-sql-limit";
    public static final String QUERY_ARG_SQL_SELECTION = "android:query-arg-sql-selection";
    public static final String QUERY_ARG_SQL_SELECTION_ARGS = "android:query-arg-sql-selection-args";
    public static final String QUERY_ARG_SQL_SORT_ORDER = "android:query-arg-sql-sort-order";
    public static final int QUERY_SORT_DIRECTION_ASCENDING = 0;
    public static final int QUERY_SORT_DIRECTION_DESCENDING = 1;
    public static final String SCHEME_ANDROID_RESOURCE = "android.resource";
    public static final String SCHEME_CONTENT = "content";
    public static final String SCHEME_FILE = "file";
    private static final int SLOW_THRESHOLD_MILLIS = 500;
    public static final int SYNC_ERROR_AUTHENTICATION = 2;
    public static final int SYNC_ERROR_CONFLICT = 5;
    public static final int SYNC_ERROR_INTERNAL = 8;
    public static final int SYNC_ERROR_IO = 3;
    private static final String[] SYNC_ERROR_NAMES = new String[]{"already-in-progress", "authentication-error", "io-error", "parse-error", "conflict", "too-many-deletions", "too-many-retries", "internal-error"};
    public static final int SYNC_ERROR_PARSE = 4;
    @UnsupportedAppUsage
    public static final int SYNC_ERROR_SYNC_ALREADY_IN_PROGRESS = 1;
    public static final int SYNC_ERROR_TOO_MANY_DELETIONS = 6;
    public static final int SYNC_ERROR_TOO_MANY_RETRIES = 7;
    public static final int SYNC_EXEMPTION_NONE = 0;
    public static final int SYNC_EXEMPTION_PROMOTE_BUCKET = 1;
    public static final int SYNC_EXEMPTION_PROMOTE_BUCKET_WITH_TEMP = 2;
    @Deprecated
    public static final String SYNC_EXTRAS_ACCOUNT = "account";
    public static final String SYNC_EXTRAS_DISALLOW_METERED = "allow_metered";
    public static final String SYNC_EXTRAS_DISCARD_LOCAL_DELETIONS = "discard_deletions";
    public static final String SYNC_EXTRAS_DO_NOT_RETRY = "do_not_retry";
    public static final String SYNC_EXTRAS_EXPECTED_DOWNLOAD = "expected_download";
    public static final String SYNC_EXTRAS_EXPECTED_UPLOAD = "expected_upload";
    public static final String SYNC_EXTRAS_EXPEDITED = "expedited";
    @Deprecated
    public static final String SYNC_EXTRAS_FORCE = "force";
    public static final String SYNC_EXTRAS_IGNORE_BACKOFF = "ignore_backoff";
    public static final String SYNC_EXTRAS_IGNORE_SETTINGS = "ignore_settings";
    public static final String SYNC_EXTRAS_INITIALIZE = "initialize";
    public static final String SYNC_EXTRAS_MANUAL = "force";
    public static final String SYNC_EXTRAS_OVERRIDE_TOO_MANY_DELETIONS = "deletions_override";
    public static final String SYNC_EXTRAS_PRIORITY = "sync_priority";
    public static final String SYNC_EXTRAS_REQUIRE_CHARGING = "require_charging";
    public static final String SYNC_EXTRAS_UPLOAD = "upload";
    public static final int SYNC_OBSERVER_TYPE_ACTIVE = 4;
    public static final int SYNC_OBSERVER_TYPE_ALL = Integer.MAX_VALUE;
    public static final int SYNC_OBSERVER_TYPE_PENDING = 2;
    public static final int SYNC_OBSERVER_TYPE_SETTINGS = 1;
    @UnsupportedAppUsage
    public static final int SYNC_OBSERVER_TYPE_STATUS = 8;
    public static final String SYNC_VIRTUAL_EXTRAS_EXEMPTION_FLAG = "v_exemption";
    private static final String TAG = "ContentResolver";
    @UnsupportedAppUsage
    private static volatile IContentService sContentService;
    @UnsupportedAppUsage
    private final Context mContext;
    @UnsupportedAppUsage
    final String mPackageName;
    private final Random mRandom;
    final int mTargetSdkVersion;
    final ContentInterface mWrapped;

    private final class CursorWrapperInner extends CrossProcessCursorWrapper {
        private final CloseGuard mCloseGuard = CloseGuard.get();
        private final IContentProvider mContentProvider;
        private final AtomicBoolean mProviderReleased = new AtomicBoolean();

        CursorWrapperInner(Cursor cursor, IContentProvider contentProvider) {
            super(cursor);
            this.mContentProvider = contentProvider;
            this.mCloseGuard.open("close");
        }

        public void close() {
            this.mCloseGuard.close();
            super.close();
            if (this.mProviderReleased.compareAndSet(false, true)) {
                ContentResolver.this.releaseProvider(this.mContentProvider);
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

    public static final class MimeTypeInfo {
        private final CharSequence mContentDescription;
        private final Icon mIcon;
        private final CharSequence mLabel;

        public MimeTypeInfo(Icon icon, CharSequence label, CharSequence contentDescription) {
            this.mIcon = (Icon) Objects.requireNonNull(icon);
            this.mLabel = (CharSequence) Objects.requireNonNull(label);
            this.mContentDescription = (CharSequence) Objects.requireNonNull(contentDescription);
        }

        public Icon getIcon() {
            return this.mIcon;
        }

        public CharSequence getLabel() {
            return this.mLabel;
        }

        public CharSequence getContentDescription() {
            return this.mContentDescription;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NotifyFlags {
    }

    public class OpenResourceIdResult {
        @UnsupportedAppUsage
        public int id;
        @UnsupportedAppUsage
        public Resources r;
    }

    private final class ParcelFileDescriptorInner extends ParcelFileDescriptor {
        private final IContentProvider mContentProvider;
        private final AtomicBoolean mProviderReleased = new AtomicBoolean();

        ParcelFileDescriptorInner(ParcelFileDescriptor pfd, IContentProvider icp) {
            super(pfd);
            this.mContentProvider = icp;
        }

        public void releaseResources() {
            if (this.mProviderReleased.compareAndSet(false, true)) {
                ContentResolver.this.releaseProvider(this.mContentProvider);
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface QueryCollator {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SortDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SyncExemption {
    }

    @UnsupportedAppUsage
    public abstract IContentProvider acquireProvider(Context context, String str);

    @UnsupportedAppUsage
    public abstract IContentProvider acquireUnstableProvider(Context context, String str);

    @UnsupportedAppUsage
    public abstract boolean releaseProvider(IContentProvider iContentProvider);

    @UnsupportedAppUsage
    public abstract boolean releaseUnstableProvider(IContentProvider iContentProvider);

    @UnsupportedAppUsage
    public abstract void unstableProviderDied(IContentProvider iContentProvider);

    public static String syncErrorToString(int error) {
        if (error >= 1) {
            String[] strArr = SYNC_ERROR_NAMES;
            if (error <= strArr.length) {
                return strArr[error - 1];
            }
        }
        return String.valueOf(error);
    }

    public static int syncErrorStringToInt(String error) {
        int n = SYNC_ERROR_NAMES.length;
        for (int i = 0; i < n; i++) {
            if (SYNC_ERROR_NAMES[i].equals(error)) {
                return i + 1;
            }
        }
        if (error != null) {
            try {
                return Integer.parseInt(error);
            } catch (NumberFormatException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("error parsing sync error: ");
                stringBuilder.append(error);
                Log.d(TAG, stringBuilder.toString());
            }
        }
        return 0;
    }

    public ContentResolver(Context context) {
        this(context, null);
    }

    public ContentResolver(Context context, ContentInterface wrapped) {
        this.mRandom = new Random();
        this.mContext = context != null ? context : ActivityThread.currentApplication();
        this.mPackageName = this.mContext.getOpPackageName();
        this.mTargetSdkVersion = this.mContext.getApplicationInfo().targetSdkVersion;
        this.mWrapped = wrapped;
    }

    public static ContentResolver wrap(ContentInterface wrapped) {
        Preconditions.checkNotNull(wrapped);
        return new ContentResolver(null, wrapped) {
            public void unstableProviderDied(IContentProvider icp) {
                throw new UnsupportedOperationException();
            }

            public boolean releaseUnstableProvider(IContentProvider icp) {
                throw new UnsupportedOperationException();
            }

            public boolean releaseProvider(IContentProvider icp) {
                throw new UnsupportedOperationException();
            }

            /* Access modifiers changed, original: protected */
            public IContentProvider acquireUnstableProvider(Context c, String name) {
                throw new UnsupportedOperationException();
            }

            /* Access modifiers changed, original: protected */
            public IContentProvider acquireProvider(Context c, String name) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static ContentResolver wrap(ContentProvider wrapped) {
        return wrap((ContentInterface) wrapped);
    }

    public static ContentResolver wrap(ContentProviderClient wrapped) {
        return wrap((ContentInterface) wrapped);
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public IContentProvider acquireExistingProvider(Context c, String name) {
        return acquireProvider(c, name);
    }

    public void appNotRespondingViaProvider(IContentProvider icp) {
        throw new UnsupportedOperationException("appNotRespondingViaProvider");
    }

    public final String getType(Uri url) {
        StringBuilder stringBuilder;
        Preconditions.checkNotNull(url, "url");
        String type = null;
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.getType(url);
            }
            IContentProvider provider = acquireExistingProvider(url);
            String str = ")";
            String str2 = " (";
            String str3 = "Failed to get type for: ";
            String str4 = TAG;
            if (provider != null) {
                try {
                    type = provider.getType(url);
                    return type;
                } catch (RemoteException e) {
                    return type;
                } catch (Exception e2) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str3);
                    stringBuilder.append(url);
                    stringBuilder.append(str2);
                    stringBuilder.append(e2.getMessage());
                    stringBuilder.append(str);
                    Log.w(str4, stringBuilder.toString());
                    return type;
                } finally {
                    releaseProvider(provider);
                }
            } else {
                if (!"content".equals(url.getScheme())) {
                    return null;
                }
                try {
                    type = ActivityManager.getService().getProviderMimeType(ContentProvider.getUriWithoutUserId(url), resolveUserId(url));
                    return type;
                } catch (RemoteException e3) {
                    throw e3.rethrowFromSystemServer();
                } catch (Exception e22) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str3);
                    stringBuilder.append(url);
                    stringBuilder.append(str2);
                    stringBuilder.append(e22.getMessage());
                    stringBuilder.append(str);
                    Log.w(str4, stringBuilder.toString());
                    return type;
                }
            }
        } catch (RemoteException e4) {
            return null;
        }
    }

    public String[] getStreamTypes(Uri url, String mimeTypeFilter) {
        Preconditions.checkNotNull(url, "url");
        Preconditions.checkNotNull(mimeTypeFilter, "mimeTypeFilter");
        String[] strArr = null;
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.getStreamTypes(url, mimeTypeFilter);
            }
            IContentProvider provider = acquireProvider(url);
            if (provider == null) {
                return null;
            }
            try {
                strArr = provider.getStreamTypes(url, mimeTypeFilter);
                return strArr;
            } catch (RemoteException e) {
                return strArr;
            } finally {
                releaseProvider(provider);
            }
        } catch (RemoteException e2) {
            return null;
        }
    }

    public final Cursor query(@Read Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SeempLog.record_uri(13, uri);
        return query(uri, projection, selection, selectionArgs, sortOrder, null);
    }

    public final Cursor query(@Read Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) {
        return query(uri, projection, createSqlQueryBundle(selection, selectionArgs, sortOrder), cancellationSignal);
    }

    /* JADX WARNING: Removed duplicated region for block: B:52:0x00b2 A:{SYNTHETIC, Splitter:B:52:0x00b2} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x009e  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x006d A:{Catch:{ RemoteException -> 0x0102, all -> 0x00ed }} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0063 A:{Catch:{ RemoteException -> 0x0102, all -> 0x00ed }} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x009e  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00b2 A:{SYNTHETIC, Splitter:B:52:0x00b2} */
    public final android.database.Cursor query(@android.annotation.RequiresPermission.Read android.net.Uri r22, java.lang.String[] r23, android.os.Bundle r24, android.os.CancellationSignal r25) {
        /*
        r21 = this;
        r7 = r21;
        r8 = r22;
        r9 = r23;
        r10 = r24;
        r11 = r25;
        r0 = 13;
        android.util.SeempLog.record_uri(r0, r8);
        r0 = "uri";
        com.android.internal.util.Preconditions.checkNotNull(r8, r0);
        r12 = 0;
        r0 = r7.mWrapped;	 Catch:{ RemoteException -> 0x0118 }
        if (r0 == 0) goto L_0x0021;
    L_0x001a:
        r0 = r7.mWrapped;	 Catch:{ RemoteException -> 0x0118 }
        r0 = r0.query(r8, r9, r10, r11);	 Catch:{ RemoteException -> 0x0118 }
        return r0;
        r13 = r21.acquireUnstableProvider(r22);
        if (r13 != 0) goto L_0x0029;
    L_0x0028:
        return r12;
    L_0x0029:
        r14 = 0;
        r15 = 0;
        r0 = android.os.SystemClock.uptimeMillis();	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        r16 = r0;
        r0 = 0;
        if (r11 == 0) goto L_0x0041;
    L_0x0034:
        r25.throwIfCanceled();	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        r1 = r13.createCancellationSignal();	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        r0 = r1;
        r11.setRemote(r0);	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        r6 = r0;
        goto L_0x0042;
    L_0x0041:
        r6 = r0;
    L_0x0042:
        r2 = r7.mPackageName;	 Catch:{ DeadObjectException -> 0x0055 }
        r1 = r13;
        r3 = r22;
        r4 = r23;
        r5 = r24;
        r18 = r6;
        r0 = r1.query(r2, r3, r4, r5, r6);	 Catch:{ DeadObjectException -> 0x0053 }
        r15 = r0;
        goto L_0x009b;
    L_0x0053:
        r0 = move-exception;
        goto L_0x0058;
    L_0x0055:
        r0 = move-exception;
        r18 = r6;
    L_0x0058:
        r7.unstableProviderDied(r13);	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        r1 = r7.mPackageName;	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        r1 = android.content.ContentResolverInjector.isForceAcquireUnstableProvider(r1, r8);	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        if (r1 == 0) goto L_0x006d;
    L_0x0063:
        r6 = r18;
        r1 = android.content.ContentResolverInjector.unstableQuery(r7, r8, r9, r10, r6);	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        r15 = r1;
        r18 = r6;
        goto L_0x009b;
    L_0x006d:
        r6 = r18;
        r1 = r21.acquireProvider(r22);	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        r14 = r1;
        if (r14 != 0) goto L_0x008b;
        if (r15 == 0) goto L_0x007c;
    L_0x0079:
        r15.close();
    L_0x007c:
        if (r11 == 0) goto L_0x0081;
    L_0x007e:
        r11.setRemote(r12);
        r7.releaseUnstableProvider(r13);
        if (r14 == 0) goto L_0x008a;
    L_0x0087:
        r7.releaseProvider(r14);
    L_0x008a:
        return r12;
    L_0x008b:
        r2 = r7.mPackageName;	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        r1 = r14;
        r3 = r22;
        r4 = r23;
        r5 = r24;
        r18 = r6;
        r1 = r1.query(r2, r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        r15 = r1;
    L_0x009b:
        if (r15 != 0) goto L_0x00b2;
        if (r15 == 0) goto L_0x00a3;
    L_0x00a0:
        r15.close();
    L_0x00a3:
        if (r11 == 0) goto L_0x00a8;
    L_0x00a5:
        r11.setRemote(r12);
        r7.releaseUnstableProvider(r13);
        if (r14 == 0) goto L_0x00b1;
    L_0x00ae:
        r7.releaseProvider(r14);
    L_0x00b1:
        return r12;
    L_0x00b2:
        r15.getCount();	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        r0 = android.os.SystemClock.uptimeMillis();	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        r19 = r0 - r16;
        r1 = r21;
        r2 = r19;
        r4 = r22;
        r5 = r23;
        r6 = r24;
        r1.maybeLogQueryToEventLog(r2, r4, r5, r6);	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        if (r14 == 0) goto L_0x00cc;
    L_0x00ca:
        r0 = r14;
        goto L_0x00d0;
    L_0x00cc:
        r0 = r21.acquireProvider(r22);	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        r1 = new android.content.ContentResolver$CursorWrapperInner;	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        r1.<init>(r15, r0);	 Catch:{ RemoteException -> 0x0102, all -> 0x00ed }
        r2 = 0;
        r3 = 0;
        if (r3 == 0) goto L_0x00de;
    L_0x00db:
        r3.close();
    L_0x00de:
        if (r11 == 0) goto L_0x00e3;
    L_0x00e0:
        r11.setRemote(r12);
        r7.releaseUnstableProvider(r13);
        if (r2 == 0) goto L_0x00ec;
    L_0x00e9:
        r7.releaseProvider(r2);
    L_0x00ec:
        return r1;
    L_0x00ed:
        r0 = move-exception;
        if (r15 == 0) goto L_0x00f3;
    L_0x00f0:
        r15.close();
    L_0x00f3:
        if (r11 == 0) goto L_0x00f8;
    L_0x00f5:
        r11.setRemote(r12);
        r7.releaseUnstableProvider(r13);
        if (r14 == 0) goto L_0x0101;
    L_0x00fe:
        r7.releaseProvider(r14);
    L_0x0101:
        throw r0;
    L_0x0102:
        r0 = move-exception;
        if (r15 == 0) goto L_0x0109;
    L_0x0106:
        r15.close();
    L_0x0109:
        if (r11 == 0) goto L_0x010e;
    L_0x010b:
        r11.setRemote(r12);
        r7.releaseUnstableProvider(r13);
        if (r14 == 0) goto L_0x0117;
    L_0x0114:
        r7.releaseProvider(r14);
    L_0x0117:
        return r12;
    L_0x0118:
        r0 = move-exception;
        return r12;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.ContentResolver.query(android.net.Uri, java.lang.String[], android.os.Bundle, android.os.CancellationSignal):android.database.Cursor");
    }

    public final Uri canonicalizeOrElse(Uri uri) {
        Uri res = canonicalize(uri);
        return res != null ? res : uri;
    }

    public final Uri canonicalize(Uri url) {
        Preconditions.checkNotNull(url, "url");
        Uri uri = null;
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.canonicalize(url);
            }
            IContentProvider provider = acquireProvider(url);
            if (provider == null) {
                return null;
            }
            try {
                uri = provider.canonicalize(this.mPackageName, url);
                return uri;
            } catch (RemoteException e) {
                return uri;
            } finally {
                releaseProvider(provider);
            }
        } catch (RemoteException e2) {
            return null;
        }
    }

    public final Uri uncanonicalize(Uri url) {
        Preconditions.checkNotNull(url, "url");
        Uri uri = null;
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.uncanonicalize(url);
            }
            IContentProvider provider = acquireProvider(url);
            if (provider == null) {
                return null;
            }
            try {
                uri = provider.uncanonicalize(this.mPackageName, url);
                return uri;
            } catch (RemoteException e) {
                return uri;
            } finally {
                releaseProvider(provider);
            }
        } catch (RemoteException e2) {
            return null;
        }
    }

    public final boolean refresh(Uri url, Bundle args, CancellationSignal cancellationSignal) {
        Preconditions.checkNotNull(url, "url");
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.refresh(url, args, cancellationSignal);
            }
            IContentProvider provider = acquireProvider(url);
            if (provider == null) {
                return false;
            }
            ICancellationSignal remoteCancellationSignal = null;
            if (cancellationSignal != null) {
                try {
                    cancellationSignal.throwIfCanceled();
                    remoteCancellationSignal = provider.createCancellationSignal();
                    cancellationSignal.setRemote(remoteCancellationSignal);
                } catch (RemoteException e) {
                    releaseProvider(provider);
                    return false;
                } catch (Throwable th) {
                    releaseProvider(provider);
                }
            }
            boolean refresh = provider.refresh(this.mPackageName, url, args, remoteCancellationSignal);
            releaseProvider(provider);
            return refresh;
        } catch (RemoteException e2) {
            return false;
        }
    }

    public final InputStream openInputStream(Uri uri) throws FileNotFoundException {
        Preconditions.checkNotNull(uri, "uri");
        String scheme = uri.getScheme();
        if (SCHEME_ANDROID_RESOURCE.equals(scheme)) {
            OpenResourceIdResult r = getResourceId(uri);
            try {
                return r.r.openRawResource(r.id);
            } catch (NotFoundException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Resource does not exist: ");
                stringBuilder.append(uri);
                throw new FileNotFoundException(stringBuilder.toString());
            }
        } else if (SCHEME_FILE.equals(scheme)) {
            return new FileInputStream(uri.getPath());
        } else {
            IOException e2 = null;
            AssetFileDescriptor fd = openAssetFileDescriptor(uri, "r", null);
            if (fd != null) {
                try {
                    e2 = fd.createInputStream();
                } catch (IOException e3) {
                    throw new FileNotFoundException("Unable to create stream");
                }
            }
            return e2;
        }
    }

    public final OutputStream openOutputStream(Uri uri) throws FileNotFoundException {
        return openOutputStream(uri, BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_W);
    }

    public final OutputStream openOutputStream(Uri uri, String mode) throws FileNotFoundException {
        AssetFileDescriptor fd = openAssetFileDescriptor(uri, mode, null);
        if (fd == null) {
            return null;
        }
        try {
            return fd.createOutputStream();
        } catch (IOException e) {
            throw new FileNotFoundException("Unable to create stream");
        }
    }

    public final ParcelFileDescriptor openFile(Uri uri, String mode, CancellationSignal signal) throws FileNotFoundException {
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.openFile(uri, mode, signal);
            }
            return openFileDescriptor(uri, mode, signal);
        } catch (RemoteException e) {
            return null;
        }
    }

    public final ParcelFileDescriptor openFileDescriptor(Uri uri, String mode) throws FileNotFoundException {
        return openFileDescriptor(uri, mode, null);
    }

    public final ParcelFileDescriptor openFileDescriptor(Uri uri, String mode, CancellationSignal cancellationSignal) throws FileNotFoundException {
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.openFile(uri, mode, cancellationSignal);
            }
            AssetFileDescriptor afd = openAssetFileDescriptor(uri, mode, cancellationSignal);
            if (afd == null) {
                return null;
            }
            if (afd.getDeclaredLength() < 0) {
                return afd.getParcelFileDescriptor();
            }
            try {
                afd.close();
            } catch (IOException e) {
            }
            throw new FileNotFoundException("Not a whole file");
        } catch (RemoteException e2) {
            return null;
        }
    }

    public final AssetFileDescriptor openAssetFile(Uri uri, String mode, CancellationSignal signal) throws FileNotFoundException {
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.openAssetFile(uri, mode, signal);
            }
            return openAssetFileDescriptor(uri, mode, signal);
        } catch (RemoteException e) {
            return null;
        }
    }

    public final AssetFileDescriptor openAssetFileDescriptor(Uri uri, String mode) throws FileNotFoundException {
        return openAssetFileDescriptor(uri, mode, null);
    }

    public final AssetFileDescriptor openAssetFileDescriptor(Uri uri, String mode, CancellationSignal cancellationSignal) throws FileNotFoundException {
        ICancellationSignal remoteCancellationSignal;
        Uri uri2 = uri;
        String str = mode;
        CancellationSignal cancellationSignal2 = cancellationSignal;
        Preconditions.checkNotNull(uri2, "uri");
        Preconditions.checkNotNull(str, "mode");
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.openAssetFile(uri2, str, cancellationSignal2);
            }
            String scheme = uri.getScheme();
            String str2 = "r";
            StringBuilder stringBuilder;
            if (SCHEME_ANDROID_RESOURCE.equals(scheme)) {
                if (str2.equals(str)) {
                    OpenResourceIdResult r = getResourceId(uri);
                    try {
                        return r.r.openRawResourceFd(r.id);
                    } catch (NotFoundException e) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Resource does not exist: ");
                        stringBuilder2.append(uri2);
                        throw new FileNotFoundException(stringBuilder2.toString());
                    }
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Can't write resources: ");
                stringBuilder.append(uri2);
                throw new FileNotFoundException(stringBuilder.toString());
            } else if (SCHEME_FILE.equals(scheme)) {
                return new AssetFileDescriptor(ParcelFileDescriptor.open(new File(uri.getPath()), ParcelFileDescriptor.parseMode(mode)), 0, -1);
            } else {
                if (str2.equals(str)) {
                    return openTypedAssetFileDescriptor(uri2, "*/*", null, cancellationSignal2);
                }
                IContentProvider unstableProvider = acquireUnstableProvider(uri);
                String str3 = "No content provider: ";
                if (unstableProvider != null) {
                    AssetFileDescriptor fd;
                    IContentProvider stableProvider = null;
                    if (cancellationSignal2 != null) {
                        try {
                            cancellationSignal.throwIfCanceled();
                            ICancellationSignal remoteCancellationSignal2 = unstableProvider.createCancellationSignal();
                            cancellationSignal2.setRemote(remoteCancellationSignal2);
                            remoteCancellationSignal = remoteCancellationSignal2;
                        } catch (DeadObjectException e2) {
                            unstableProviderDied(unstableProvider);
                            stableProvider = acquireProvider(uri);
                            if (stableProvider != null) {
                                AssetFileDescriptor fd2 = stableProvider.openAssetFile(this.mPackageName, uri2, str, remoteCancellationSignal);
                                if (fd2 == null) {
                                    if (cancellationSignal2 != null) {
                                        cancellationSignal2.setRemote(null);
                                    }
                                    releaseProvider(stableProvider);
                                    releaseUnstableProvider(unstableProvider);
                                    return null;
                                }
                                fd = fd2;
                            } else {
                                StringBuilder stringBuilder3 = new StringBuilder();
                                stringBuilder3.append(str3);
                                stringBuilder3.append(uri2);
                                throw new FileNotFoundException(stringBuilder3.toString());
                            }
                        } catch (RemoteException e3) {
                            StringBuilder stringBuilder4 = new StringBuilder();
                            stringBuilder4.append("Failed opening content provider: ");
                            stringBuilder4.append(uri2);
                            throw new FileNotFoundException(stringBuilder4.toString());
                        } catch (FileNotFoundException e4) {
                            throw e4;
                        } catch (Throwable th) {
                            if (cancellationSignal2 != null) {
                                cancellationSignal2.setRemote(null);
                            }
                            if (stableProvider != null) {
                                releaseProvider(stableProvider);
                            }
                            if (unstableProvider != null) {
                                releaseUnstableProvider(unstableProvider);
                            }
                        }
                    } else {
                        remoteCancellationSignal = null;
                    }
                    AssetFileDescriptor fd3 = unstableProvider.openAssetFile(this.mPackageName, uri2, str, remoteCancellationSignal);
                    if (fd3 == null) {
                        if (cancellationSignal2 != null) {
                            cancellationSignal2.setRemote(null);
                        }
                        if (null != null) {
                            releaseProvider(null);
                        }
                        releaseUnstableProvider(unstableProvider);
                        return null;
                    }
                    fd = fd3;
                    if (stableProvider == null) {
                        stableProvider = acquireProvider(uri);
                    }
                    releaseUnstableProvider(unstableProvider);
                    RemoteException assetFileDescriptor = new AssetFileDescriptor(new ParcelFileDescriptorInner(fd.getParcelFileDescriptor(), stableProvider), fd.getStartOffset(), fd.getDeclaredLength());
                    if (cancellationSignal2 != null) {
                        cancellationSignal2.setRemote(null);
                    }
                    if (null != null) {
                        releaseProvider(null);
                    }
                    if (null != null) {
                        releaseUnstableProvider(null);
                    }
                    return assetFileDescriptor;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append(str3);
                stringBuilder.append(uri2);
                throw new FileNotFoundException(stringBuilder.toString());
            }
        } catch (RemoteException e5) {
            return null;
        }
    }

    public final AssetFileDescriptor openTypedAssetFile(Uri uri, String mimeTypeFilter, Bundle opts, CancellationSignal signal) throws FileNotFoundException {
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.openTypedAssetFile(uri, mimeTypeFilter, opts, signal);
            }
            return openTypedAssetFileDescriptor(uri, mimeTypeFilter, opts, signal);
        } catch (RemoteException e) {
            return null;
        }
    }

    public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri uri, String mimeType, Bundle opts) throws FileNotFoundException {
        return openTypedAssetFileDescriptor(uri, mimeType, opts, null);
    }

    public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri uri, String mimeType, Bundle opts, CancellationSignal cancellationSignal) throws FileNotFoundException {
        ICancellationSignal remoteCancellationSignal;
        StringBuilder stringBuilder;
        Uri uri2 = uri;
        String str = mimeType;
        CancellationSignal cancellationSignal2 = cancellationSignal;
        Preconditions.checkNotNull(uri2, "uri");
        Preconditions.checkNotNull(str, "mimeType");
        Bundle bundle;
        try {
            if (this.mWrapped != null) {
                try {
                    return this.mWrapped.openTypedAssetFile(uri2, str, opts, cancellationSignal2);
                } catch (RemoteException e) {
                    return null;
                }
            }
            bundle = opts;
            IContentProvider unstableProvider = acquireUnstableProvider(uri);
            String str2 = "No content provider: ";
            if (unstableProvider != null) {
                AssetFileDescriptor fd;
                IContentProvider stableProvider = null;
                if (cancellationSignal2 != null) {
                    try {
                        cancellationSignal.throwIfCanceled();
                        ICancellationSignal remoteCancellationSignal2 = unstableProvider.createCancellationSignal();
                        cancellationSignal2.setRemote(remoteCancellationSignal2);
                        remoteCancellationSignal = remoteCancellationSignal2;
                    } catch (DeadObjectException e2) {
                        unstableProviderDied(unstableProvider);
                        stableProvider = acquireProvider(uri);
                        if (stableProvider != null) {
                            AssetFileDescriptor fd2 = stableProvider.openTypedAssetFile(this.mPackageName, uri, mimeType, opts, remoteCancellationSignal);
                            if (fd2 == null) {
                                if (cancellationSignal2 != null) {
                                    cancellationSignal2.setRemote(null);
                                }
                                releaseProvider(stableProvider);
                                releaseUnstableProvider(unstableProvider);
                                return null;
                            }
                            fd = fd2;
                        } else {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(str2);
                            stringBuilder.append(uri2);
                            throw new FileNotFoundException(stringBuilder.toString());
                        }
                    } catch (RemoteException e3) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed opening content provider: ");
                        stringBuilder.append(uri2);
                        throw new FileNotFoundException(stringBuilder.toString());
                    } catch (FileNotFoundException e4) {
                        throw e4;
                    } catch (Throwable th) {
                        if (cancellationSignal2 != null) {
                            cancellationSignal2.setRemote(null);
                        }
                        if (stableProvider != null) {
                            releaseProvider(stableProvider);
                        }
                        if (unstableProvider != null) {
                            releaseUnstableProvider(unstableProvider);
                        }
                    }
                } else {
                    remoteCancellationSignal = null;
                }
                AssetFileDescriptor fd3 = unstableProvider.openTypedAssetFile(this.mPackageName, uri, mimeType, opts, remoteCancellationSignal);
                if (fd3 == null) {
                    if (cancellationSignal2 != null) {
                        cancellationSignal2.setRemote(null);
                    }
                    if (null != null) {
                        releaseProvider(null);
                    }
                    releaseUnstableProvider(unstableProvider);
                    return null;
                }
                fd = fd3;
                if (stableProvider == null) {
                    stableProvider = acquireProvider(uri);
                }
                releaseUnstableProvider(unstableProvider);
                RemoteException assetFileDescriptor = new AssetFileDescriptor(new ParcelFileDescriptorInner(fd.getParcelFileDescriptor(), stableProvider), fd.getStartOffset(), fd.getDeclaredLength());
                if (cancellationSignal2 != null) {
                    cancellationSignal2.setRemote(null);
                }
                if (null != null) {
                    releaseProvider(null);
                }
                if (null != null) {
                    releaseUnstableProvider(null);
                }
                return assetFileDescriptor;
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(uri2);
            throw new FileNotFoundException(stringBuilder2.toString());
        } catch (RemoteException e5) {
            bundle = opts;
            return null;
        }
    }

    @UnsupportedAppUsage
    public OpenResourceIdResult getResourceId(Uri uri) throws FileNotFoundException {
        StringBuilder stringBuilder;
        String authority = uri.getAuthority();
        if (TextUtils.isEmpty(authority)) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("No authority: ");
            stringBuilder2.append(uri);
            throw new FileNotFoundException(stringBuilder2.toString());
        }
        try {
            Resources r = this.mContext.getPackageManager().getResourcesForApplication(authority);
            List<String> path = uri.getPathSegments();
            if (path != null) {
                int id;
                int len = path.size();
                if (len == 1) {
                    try {
                        id = Integer.parseInt((String) path.get(0));
                    } catch (NumberFormatException e) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Single path segment is not a resource ID: ");
                        stringBuilder.append(uri);
                        throw new FileNotFoundException(stringBuilder.toString());
                    }
                } else if (len == 2) {
                    id = r.getIdentifier((String) path.get(1), (String) path.get(0), authority);
                } else {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("More than two path segments: ");
                    stringBuilder3.append(uri);
                    throw new FileNotFoundException(stringBuilder3.toString());
                }
                if (id != 0) {
                    OpenResourceIdResult res = new OpenResourceIdResult();
                    res.r = r;
                    res.id = id;
                    return res;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("No resource found for: ");
                stringBuilder.append(uri);
                throw new FileNotFoundException(stringBuilder.toString());
            }
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append("No path: ");
            stringBuilder4.append(uri);
            throw new FileNotFoundException(stringBuilder4.toString());
        } catch (NameNotFoundException e2) {
            StringBuilder stringBuilder5 = new StringBuilder();
            stringBuilder5.append("No package found for authority: ");
            stringBuilder5.append(uri);
            throw new FileNotFoundException(stringBuilder5.toString());
        }
    }

    public final Uri insert(@Write Uri url, ContentValues values) {
        SeempLog.record_uri(37, url);
        Preconditions.checkNotNull(url, "url");
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.insert(url, values);
            }
            IContentProvider provider = acquireProvider(url);
            if (provider != null) {
                try {
                    long startTime = SystemClock.uptimeMillis();
                    Uri createdRow = provider.insert(this.mPackageName, url, values);
                    maybeLogUpdateToEventLog(SystemClock.uptimeMillis() - startTime, url, "insert", null);
                    return createdRow;
                } catch (RemoteException e) {
                    return null;
                } finally {
                    releaseProvider(provider);
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown URL ");
                stringBuilder.append(url);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        } catch (RemoteException e2) {
            return null;
        }
    }

    public ContentProviderResult[] applyBatch(String authority, ArrayList<ContentProviderOperation> operations) throws RemoteException, OperationApplicationException {
        Preconditions.checkNotNull(authority, Directory.DIRECTORY_AUTHORITY);
        Preconditions.checkNotNull(operations, "operations");
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.applyBatch(authority, operations);
            }
            ContentProviderClient provider = acquireContentProviderClient(authority);
            if (provider != null) {
                try {
                    ContentProviderResult[] applyBatch = provider.applyBatch(operations);
                    return applyBatch;
                } finally {
                    provider.release();
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown authority ");
                stringBuilder.append(authority);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        } catch (RemoteException e) {
            return null;
        }
    }

    public final int bulkInsert(@Write Uri url, ContentValues[] values) {
        Preconditions.checkNotNull(url, "url");
        Preconditions.checkNotNull(values, "values");
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.bulkInsert(url, values);
            }
            IContentProvider provider = acquireProvider(url);
            if (provider != null) {
                try {
                    long startTime = SystemClock.uptimeMillis();
                    int rowsCreated = provider.bulkInsert(this.mPackageName, url, values);
                    maybeLogUpdateToEventLog(SystemClock.uptimeMillis() - startTime, url, "bulkinsert", null);
                    return rowsCreated;
                } catch (RemoteException e) {
                    return 0;
                } finally {
                    releaseProvider(provider);
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown URL ");
                stringBuilder.append(url);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        } catch (RemoteException e2) {
            return 0;
        }
    }

    public final int delete(@Write Uri url, String where, String[] selectionArgs) {
        Preconditions.checkNotNull(url, "url");
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.delete(url, where, selectionArgs);
            }
            IContentProvider provider = acquireProvider(url);
            if (provider != null) {
                try {
                    long startTime = SystemClock.uptimeMillis();
                    int rowsDeleted = provider.delete(this.mPackageName, url, where, selectionArgs);
                    maybeLogUpdateToEventLog(SystemClock.uptimeMillis() - startTime, url, "delete", where);
                    return rowsDeleted;
                } catch (RemoteException e) {
                    return -1;
                } finally {
                    releaseProvider(provider);
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown URL ");
                stringBuilder.append(url);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        } catch (RemoteException e2) {
            return 0;
        }
    }

    public final int update(@Write Uri uri, ContentValues values, String where, String[] selectionArgs) {
        Uri uri2 = uri;
        Preconditions.checkNotNull(uri2, "uri");
        ContentValues contentValues;
        String str;
        String[] strArr;
        try {
            if (this.mWrapped != null) {
                try {
                    return this.mWrapped.update(uri2, values, where, selectionArgs);
                } catch (RemoteException e) {
                    return 0;
                }
            }
            contentValues = values;
            str = where;
            strArr = selectionArgs;
            IContentProvider provider = acquireProvider(uri);
            if (provider != null) {
                int rowsUpdated;
                int i;
                try {
                    long startTime = SystemClock.uptimeMillis();
                    rowsUpdated = provider.update(this.mPackageName, uri, values, where, selectionArgs);
                    i = this;
                    i.maybeLogUpdateToEventLog(SystemClock.uptimeMillis() - startTime, uri, "update", where);
                    return rowsUpdated;
                } catch (RemoteException e2) {
                    rowsUpdated = e2;
                    i = -1;
                    return i;
                } finally {
                    releaseProvider(provider);
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown URI ");
                stringBuilder.append(uri2);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        } catch (RemoteException e3) {
            contentValues = values;
            str = where;
            strArr = selectionArgs;
            return 0;
        }
    }

    public final Bundle call(Uri uri, String method, String arg, Bundle extras) {
        return call(uri.getAuthority(), method, arg, extras);
    }

    public final Bundle call(String authority, String method, String arg, Bundle extras) {
        Preconditions.checkNotNull(authority, Directory.DIRECTORY_AUTHORITY);
        Preconditions.checkNotNull(method, RemindersColumns.METHOD);
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.call(authority, method, arg, extras);
            }
            IContentProvider provider = acquireProvider(authority);
            if (provider != null) {
                Bundle res;
                try {
                    res = provider.call(this.mPackageName, authority, method, arg, extras);
                    Bundle.setDefusable(res, true);
                    return res;
                } catch (RemoteException e) {
                    res = e;
                    return null;
                } finally {
                    releaseProvider(provider);
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown authority ");
                stringBuilder.append(authority);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        } catch (RemoteException e2) {
            return null;
        }
    }

    @UnsupportedAppUsage
    public final IContentProvider acquireProvider(Uri uri) {
        if (!"content".equals(uri.getScheme())) {
            return null;
        }
        String auth = uri.getAuthority();
        if (auth != null) {
            return acquireProvider(this.mContext, auth);
        }
        return null;
    }

    @UnsupportedAppUsage
    public final IContentProvider acquireExistingProvider(Uri uri) {
        if (!"content".equals(uri.getScheme())) {
            return null;
        }
        String auth = uri.getAuthority();
        if (auth != null) {
            return acquireExistingProvider(this.mContext, auth);
        }
        return null;
    }

    @UnsupportedAppUsage
    public final IContentProvider acquireProvider(String name) {
        if (name == null) {
            return null;
        }
        return acquireProvider(this.mContext, name);
    }

    public final IContentProvider acquireUnstableProvider(Uri uri) {
        if ("content".equals(uri.getScheme()) && uri.getAuthority() != null) {
            return acquireUnstableProvider(this.mContext, uri.getAuthority());
        }
        return null;
    }

    @UnsupportedAppUsage
    public final IContentProvider acquireUnstableProvider(String name) {
        if (name == null) {
            return null;
        }
        return acquireUnstableProvider(this.mContext, name);
    }

    public final ContentProviderClient acquireContentProviderClient(Uri uri) {
        Preconditions.checkNotNull(uri, "uri");
        IContentProvider provider = acquireProvider(uri);
        if (provider != null) {
            return new ContentProviderClient(this, provider, uri.getAuthority(), true);
        }
        return null;
    }

    public final ContentProviderClient acquireContentProviderClient(String name) {
        Preconditions.checkNotNull(name, "name");
        IContentProvider provider = acquireProvider(name);
        if (provider != null) {
            return new ContentProviderClient(this, provider, name, true);
        }
        return null;
    }

    public final ContentProviderClient acquireUnstableContentProviderClient(Uri uri) {
        Preconditions.checkNotNull(uri, "uri");
        IContentProvider provider = acquireUnstableProvider(uri);
        if (provider != null) {
            return new ContentProviderClient(this, provider, uri.getAuthority(), false);
        }
        return null;
    }

    public final ContentProviderClient acquireUnstableContentProviderClient(String name) {
        Preconditions.checkNotNull(name, "name");
        IContentProvider provider = acquireUnstableProvider(name);
        if (provider != null) {
            return new ContentProviderClient(this, provider, name, false);
        }
        return null;
    }

    public final void registerContentObserver(Uri uri, boolean notifyForDescendants, ContentObserver observer) {
        Preconditions.checkNotNull(uri, "uri");
        Preconditions.checkNotNull(observer, "observer");
        registerContentObserver(ContentProvider.getUriWithoutUserId(uri), notifyForDescendants, observer, ContentProvider.getUserIdFromUri(uri, this.mContext.getUserId()));
    }

    @UnsupportedAppUsage
    public final void registerContentObserver(Uri uri, boolean notifyForDescendents, ContentObserver observer, int userHandle) {
        try {
            getContentService().registerContentObserver(uri, notifyForDescendents, observer.getContentObserver(), userHandle, this.mTargetSdkVersion);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public final void unregisterContentObserver(ContentObserver observer) {
        Preconditions.checkNotNull(observer, "observer");
        try {
            IContentObserver contentObserver = observer.releaseContentObserver();
            if (contentObserver != null) {
                getContentService().unregisterContentObserver(contentObserver);
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void notifyChange(Uri uri, ContentObserver observer) {
        notifyChange(uri, observer, true);
    }

    public void notifyChange(Uri uri, ContentObserver observer, boolean syncToNetwork) {
        Preconditions.checkNotNull(uri, "uri");
        notifyChange(ContentProvider.getUriWithoutUserId(uri), observer, syncToNetwork, ContentProvider.getUserIdFromUri(uri, this.mContext.getUserId()));
    }

    public void notifyChange(Uri uri, ContentObserver observer, int flags) {
        Preconditions.checkNotNull(uri, "uri");
        notifyChange(ContentProvider.getUriWithoutUserId(uri), observer, flags, ContentProvider.getUserIdFromUri(uri, this.mContext.getUserId()));
    }

    public void notifyChange(Uri uri, ContentObserver observer, boolean syncToNetwork, int userHandle) {
        try {
            IContentService contentService = getContentService();
            IContentObserver contentObserver = observer == null ? null : observer.getContentObserver();
            boolean z = observer != null && observer.deliverSelfNotifications();
            contentService.notifyChange(uri, contentObserver, z, syncToNetwork ? 1 : 0, userHandle, this.mTargetSdkVersion, this.mContext.getPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void notifyChange(Uri uri, ContentObserver observer, int flags, int userHandle) {
        try {
            IContentService contentService = getContentService();
            IContentObserver contentObserver = observer == null ? null : observer.getContentObserver();
            boolean z = observer != null && observer.deliverSelfNotifications();
            contentService.notifyChange(uri, contentObserver, z, flags, userHandle, this.mTargetSdkVersion, this.mContext.getPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void takePersistableUriPermission(Uri uri, int modeFlags) {
        Preconditions.checkNotNull(uri, "uri");
        try {
            UriGrantsManager.getService().takePersistableUriPermission(ContentProvider.getUriWithoutUserId(uri), modeFlags, null, resolveUserId(uri));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public void takePersistableUriPermission(String toPackage, Uri uri, int modeFlags) {
        Preconditions.checkNotNull(toPackage, "toPackage");
        Preconditions.checkNotNull(uri, "uri");
        try {
            UriGrantsManager.getService().takePersistableUriPermission(ContentProvider.getUriWithoutUserId(uri), modeFlags, toPackage, resolveUserId(uri));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void releasePersistableUriPermission(Uri uri, int modeFlags) {
        Preconditions.checkNotNull(uri, "uri");
        try {
            UriGrantsManager.getService().releasePersistableUriPermission(ContentProvider.getUriWithoutUserId(uri), modeFlags, null, resolveUserId(uri));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<UriPermission> getPersistedUriPermissions() {
        try {
            return UriGrantsManager.getService().getUriPermissions(this.mPackageName, true, true).getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<UriPermission> getOutgoingPersistedUriPermissions() {
        try {
            return UriGrantsManager.getService().getUriPermissions(this.mPackageName, false, true).getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<UriPermission> getOutgoingUriPermissions() {
        try {
            return UriGrantsManager.getService().getUriPermissions(this.mPackageName, false, false).getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void startSync(Uri uri, Bundle extras) {
        Account account = null;
        if (extras != null) {
            String str = "account";
            String accountName = extras.getString(str);
            if (!TextUtils.isEmpty(accountName)) {
                account = new Account(accountName, "com.google");
            }
            extras.remove(str);
        }
        requestSync(account, uri != null ? uri.getAuthority() : null, extras);
    }

    public static void requestSync(Account account, String authority, Bundle extras) {
        requestSyncAsUser(account, authority, UserHandle.myUserId(), extras);
    }

    public static void requestSyncAsUser(Account account, String authority, int userId, Bundle extras) {
        if (extras != null) {
            try {
                getContentService().syncAsUser(new Builder().setSyncAdapter(account, authority).setExtras(extras).syncOnce().build(), userId, ActivityThread.currentPackageName());
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("Must specify extras.");
    }

    public static void requestSync(SyncRequest request) {
        try {
            getContentService().sync(request, ActivityThread.currentPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void validateSyncExtrasBundle(Bundle extras) {
        try {
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                if (value != null) {
                    if (!(value instanceof Long)) {
                        if (!(value instanceof Integer)) {
                            if (!(value instanceof Boolean)) {
                                if (!(value instanceof Float)) {
                                    if (!(value instanceof Double)) {
                                        if (!(value instanceof String)) {
                                            if (!(value instanceof Account)) {
                                                StringBuilder stringBuilder = new StringBuilder();
                                                stringBuilder.append("unexpected value type: ");
                                                stringBuilder.append(value.getClass().getName());
                                                throw new IllegalArgumentException(stringBuilder.toString());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (RuntimeException exc) {
            throw new IllegalArgumentException("error unparceling Bundle", exc);
        }
    }

    @Deprecated
    public void cancelSync(Uri uri) {
        cancelSync(null, uri != null ? uri.getAuthority() : null);
    }

    public static void cancelSync(Account account, String authority) {
        try {
            getContentService().cancelSync(account, authority, null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void cancelSyncAsUser(Account account, String authority, int userId) {
        try {
            getContentService().cancelSyncAsUser(account, authority, null, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static SyncAdapterType[] getSyncAdapterTypes() {
        try {
            return getContentService().getSyncAdapterTypes();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static SyncAdapterType[] getSyncAdapterTypesAsUser(int userId) {
        try {
            return getContentService().getSyncAdapterTypesAsUser(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static String[] getSyncAdapterPackagesForAuthorityAsUser(String authority, int userId) {
        try {
            return getContentService().getSyncAdapterPackagesForAuthorityAsUser(authority, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean getSyncAutomatically(Account account, String authority) {
        try {
            return getContentService().getSyncAutomatically(account, authority);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean getSyncAutomaticallyAsUser(Account account, String authority, int userId) {
        try {
            return getContentService().getSyncAutomaticallyAsUser(account, authority, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void setSyncAutomatically(Account account, String authority, boolean sync) {
        setSyncAutomaticallyAsUser(account, authority, sync, UserHandle.myUserId());
    }

    public static void setSyncAutomaticallyAsUser(Account account, String authority, boolean sync, int userId) {
        try {
            getContentService().setSyncAutomaticallyAsUser(account, authority, sync, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void addPeriodicSync(Account account, String authority, Bundle extras, long pollFrequency) {
        validateSyncExtrasBundle(extras);
        if (invalidPeriodicExtras(extras)) {
            throw new IllegalArgumentException("illegal extras were set");
        }
        try {
            getContentService().addPeriodicSync(account, authority, extras, pollFrequency);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean invalidPeriodicExtras(Bundle extras) {
        String str = "force";
        if (extras.getBoolean(str, false) || extras.getBoolean(SYNC_EXTRAS_DO_NOT_RETRY, false) || extras.getBoolean(SYNC_EXTRAS_IGNORE_BACKOFF, false) || extras.getBoolean(SYNC_EXTRAS_IGNORE_SETTINGS, false) || extras.getBoolean(SYNC_EXTRAS_INITIALIZE, false) || extras.getBoolean(str, false) || extras.getBoolean(SYNC_EXTRAS_EXPEDITED, false)) {
            return true;
        }
        return false;
    }

    public static void removePeriodicSync(Account account, String authority, Bundle extras) {
        validateSyncExtrasBundle(extras);
        try {
            getContentService().removePeriodicSync(account, authority, extras);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void cancelSync(SyncRequest request) {
        if (request != null) {
            try {
                getContentService().cancelRequest(request);
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("request cannot be null");
    }

    public static List<PeriodicSync> getPeriodicSyncs(Account account, String authority) {
        try {
            return getContentService().getPeriodicSyncs(account, authority, null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static int getIsSyncable(Account account, String authority) {
        try {
            return getContentService().getIsSyncable(account, authority);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static int getIsSyncableAsUser(Account account, String authority, int userId) {
        try {
            return getContentService().getIsSyncableAsUser(account, authority, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void setIsSyncable(Account account, String authority, int syncable) {
        try {
            getContentService().setIsSyncable(account, authority, syncable);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void setIsSyncableAsUser(Account account, String authority, int syncable, int userId) {
        try {
            getContentService().setIsSyncableAsUser(account, authority, syncable, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean getMasterSyncAutomatically() {
        try {
            return getContentService().getMasterSyncAutomatically();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean getMasterSyncAutomaticallyAsUser(int userId) {
        try {
            return getContentService().getMasterSyncAutomaticallyAsUser(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void setMasterSyncAutomatically(boolean sync) {
        setMasterSyncAutomaticallyAsUser(sync, UserHandle.myUserId());
    }

    public static void setMasterSyncAutomaticallyAsUser(boolean sync, int userId) {
        try {
            getContentService().setMasterSyncAutomaticallyAsUser(sync, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean isSyncActive(Account account, String authority) {
        if (account == null) {
            throw new IllegalArgumentException("account must not be null");
        } else if (authority != null) {
            try {
                return getContentService().isSyncActive(account, authority, null);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("authority must not be null");
        }
    }

    @Deprecated
    public static SyncInfo getCurrentSync() {
        try {
            List<SyncInfo> syncs = getContentService().getCurrentSyncs();
            if (syncs.isEmpty()) {
                return null;
            }
            return (SyncInfo) syncs.get(0);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static List<SyncInfo> getCurrentSyncs() {
        try {
            return getContentService().getCurrentSyncs();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static List<SyncInfo> getCurrentSyncsAsUser(int userId) {
        try {
            return getContentService().getCurrentSyncsAsUser(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public static SyncStatusInfo getSyncStatus(Account account, String authority) {
        try {
            return getContentService().getSyncStatus(account, authority, null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public static SyncStatusInfo getSyncStatusAsUser(Account account, String authority, int userId) {
        try {
            return getContentService().getSyncStatusAsUser(account, authority, null, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean isSyncPending(Account account, String authority) {
        return isSyncPendingAsUser(account, authority, UserHandle.myUserId());
    }

    public static boolean isSyncPendingAsUser(Account account, String authority, int userId) {
        try {
            return getContentService().isSyncPendingAsUser(account, authority, null, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static Object addStatusChangeListener(int mask, final SyncStatusObserver callback) {
        if (callback != null) {
            try {
                Stub observer = new Stub() {
                    public void onStatusChanged(int which) throws RemoteException {
                        callback.onStatusChanged(which);
                    }
                };
                getContentService().addStatusChangeListener(mask, observer);
                return observer;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("you passed in a null callback");
    }

    public static void removeStatusChangeListener(Object handle) {
        if (handle != null) {
            try {
                getContentService().removeStatusChangeListener((Stub) handle);
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("you passed in a null handle");
    }

    @SystemApi
    public void putCache(Uri key, Bundle value) {
        try {
            getContentService().putCache(this.mContext.getPackageName(), key, value, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public Bundle getCache(Uri key) {
        try {
            Bundle bundle = getContentService().getCache(this.mContext.getPackageName(), key, this.mContext.getUserId());
            if (bundle != null) {
                bundle.setClassLoader(this.mContext.getClassLoader());
            }
            return bundle;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getTargetSdkVersion() {
        return this.mTargetSdkVersion;
    }

    private int samplePercentForDuration(long durationMillis) {
        if (durationMillis >= 500) {
            return 100;
        }
        return ((int) ((100 * durationMillis) / 500)) + 1;
    }

    private void maybeLogQueryToEventLog(long durationMillis, Uri uri, String[] projection, Bundle queryArgs) {
    }

    private void maybeLogUpdateToEventLog(long durationMillis, Uri uri, String operation, String selection) {
    }

    @UnsupportedAppUsage
    public static IContentService getContentService() {
        if (sContentService != null) {
            return sContentService;
        }
        sContentService = IContentService.Stub.asInterface(ServiceManager.getService("content"));
        return sContentService;
    }

    @UnsupportedAppUsage
    public String getPackageName() {
        return this.mPackageName;
    }

    public int resolveUserId(Uri uri) {
        return ContentProvider.getUserIdFromUri(uri, this.mContext.getUserId());
    }

    public int getUserId() {
        return this.mContext.getUserId();
    }

    @Deprecated
    public Drawable getTypeDrawable(String mimeType) {
        return getTypeInfo(mimeType).getIcon().loadDrawable(this.mContext);
    }

    public final MimeTypeInfo getTypeInfo(String mimeType) {
        Objects.requireNonNull(mimeType);
        return MimeIconUtils.getTypeInfo(mimeType);
    }

    public static Bundle createSqlQueryBundle(String selection, String[] selectionArgs, String sortOrder) {
        if (selection == null && selectionArgs == null && sortOrder == null) {
            return null;
        }
        Bundle queryArgs = new Bundle();
        if (selection != null) {
            queryArgs.putString(QUERY_ARG_SQL_SELECTION, selection);
        }
        if (selectionArgs != null) {
            queryArgs.putStringArray(QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs);
        }
        if (sortOrder != null) {
            queryArgs.putString(QUERY_ARG_SQL_SORT_ORDER, sortOrder);
        }
        return queryArgs;
    }

    public static String createSqlSortClause(Bundle queryArgs) {
        Object[] columns = queryArgs.getStringArray(QUERY_ARG_SORT_COLUMNS);
        if (columns == null || columns.length == 0) {
            throw new IllegalArgumentException("Can't create sort clause without columns.");
        }
        String query = TextUtils.join((CharSequence) ", ", columns);
        int collation = queryArgs.getInt(QUERY_ARG_SORT_COLLATION, 3);
        if (collation == 0 || collation == 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(query);
            stringBuilder.append(" COLLATE NOCASE");
            query = stringBuilder.toString();
        }
        int sortDir = queryArgs.getInt(QUERY_ARG_SORT_DIRECTION, Integer.MIN_VALUE);
        if (sortDir == Integer.MIN_VALUE) {
            return query;
        }
        StringBuilder stringBuilder2;
        if (sortDir == 0) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(query);
            stringBuilder2.append(" ASC");
            return stringBuilder2.toString();
        } else if (sortDir == 1) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(query);
            stringBuilder2.append(" DESC");
            return stringBuilder2.toString();
        } else {
            throw new IllegalArgumentException("Unsupported sort direction value. See ContentResolver documentation for details.");
        }
    }

    public Bitmap loadThumbnail(Uri uri, Size size, CancellationSignal signal) throws IOException {
        return loadThumbnail(this, uri, size, signal, 1);
    }

    public static Bitmap loadThumbnail(ContentInterface content, Uri uri, Size size, CancellationSignal signal, int allocator) throws IOException {
        Objects.requireNonNull(content);
        Objects.requireNonNull(uri);
        Objects.requireNonNull(size);
        Bundle opts = new Bundle();
        opts.putParcelable(EXTRA_SIZE, Point.convert(size));
        Int32Ref orientation = new Int32Ref(0);
        int i = allocator;
        Bitmap bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(new -$$Lambda$ContentResolver$7ILY1SWNxC2xhk-fQUG6tAXW9Ik(content, uri, opts, signal, orientation)), new -$$Lambda$ContentResolver$RVw7W0M7r0cGmbYi8rAG5GKxq4M(i, signal, size));
        if (orientation.value == 0) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix m = new Matrix();
        m.setRotate((float) orientation.value, (float) (width / 2), (float) (height / 2));
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, m, false);
    }

    static /* synthetic */ AssetFileDescriptor lambda$loadThumbnail$0(ContentInterface content, Uri uri, Bundle opts, CancellationSignal signal, Int32Ref orientation) throws Exception {
        AssetFileDescriptor afd = content.openTypedAssetFile(uri, "image/*", opts, signal);
        Bundle extras = afd.getExtras();
        int i = 0;
        if (extras != null) {
            i = extras.getInt(DocumentsContract.EXTRA_ORIENTATION, 0);
        }
        orientation.value = i;
        return afd;
    }

    static /* synthetic */ void lambda$loadThumbnail$1(int allocator, CancellationSignal signal, Size size, ImageDecoder decoder, ImageInfo info, Source source) {
        decoder.setAllocator(allocator);
        if (signal != null) {
            signal.throwIfCanceled();
        }
        int sample = Math.min(info.getSize().getWidth() / size.getWidth(), info.getSize().getHeight() / size.getHeight());
        if (sample > 1) {
            decoder.setTargetSampleSize(sample);
        }
    }

    public static void onDbCorruption(String tag, String message, Throwable stacktrace) {
        try {
            getContentService().onDbCorruption(tag, message, Log.getStackTraceString(stacktrace));
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public static Uri translateDeprecatedDataPath(String path) {
        String ssp = new StringBuilder();
        ssp.append("//");
        ssp.append(path.substring(DEPRECATE_DATA_PREFIX.length()));
        return Uri.parse(new Uri.Builder().scheme("content").encodedOpaquePart(ssp.toString()).build().toString());
    }

    public static String translateDeprecatedDataPath(Uri uri) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DEPRECATE_DATA_PREFIX);
        stringBuilder.append(uri.getEncodedSchemeSpecificPart().substring(2));
        return stringBuilder.toString();
    }
}
