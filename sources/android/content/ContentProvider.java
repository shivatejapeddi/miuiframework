package android.content;

import android.Manifest.permission;
import android.annotation.UnsupportedAppUsage;
import android.app.AppOpsManager;
import android.content.pm.PathPermission;
import android.content.pm.ProviderInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.os.Trace;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import miui.securityspace.XSpaceUserHandle;

public abstract class ContentProvider implements ContentInterface, ComponentCallbacks2 {
    private static final String TAG = "ContentProvider";
    @UnsupportedAppUsage
    private String[] mAuthorities;
    @UnsupportedAppUsage
    private String mAuthority;
    private ThreadLocal<String> mCallingPackage;
    @UnsupportedAppUsage
    private Context mContext = null;
    private boolean mExported;
    private int mMyUid;
    private boolean mNoPerms;
    @UnsupportedAppUsage
    private PathPermission[] mPathPermissions;
    @UnsupportedAppUsage
    private String mReadPermission;
    private boolean mSingleUser;
    private Transport mTransport = new Transport();
    @UnsupportedAppUsage
    private String mWritePermission;

    public final class CallingIdentity {
        public final long binderToken;
        public final String callingPackage;

        public CallingIdentity(long binderToken, String callingPackage) {
            this.binderToken = binderToken;
            this.callingPackage = callingPackage;
        }
    }

    public interface PipeDataWriter<T> {
        void writeDataToPipe(ParcelFileDescriptor parcelFileDescriptor, Uri uri, String str, Bundle bundle, T t);
    }

    class Transport extends ContentProviderNative {
        volatile AppOpsManager mAppOpsManager = null;
        volatile ContentInterface mInterface = ContentProvider.this;
        volatile int mReadOp = -1;
        volatile int mWriteOp = -1;

        Transport() {
        }

        /* Access modifiers changed, original: 0000 */
        public ContentProvider getContentProvider() {
            return ContentProvider.this;
        }

        public String getProviderName() {
            return getContentProvider().getClass().getName();
        }

        public Cursor query(String callingPkg, Uri uri, String[] projection, Bundle queryArgs, ICancellationSignal cancellationSignal) {
            uri = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            String original;
            Cursor query;
            if (enforceReadPermission(callingPkg, uri, null) == 0) {
                Trace.traceBegin(1048576, "query");
                original = ContentProvider.this.setCallingPackage(callingPkg);
                try {
                    query = this.mInterface.query(uri, projection, queryArgs, CancellationSignal.fromTransport(cancellationSignal));
                    ContentProvider.this.setCallingPackage(original);
                    Trace.traceEnd(1048576);
                    return query;
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                } catch (Throwable th) {
                    ContentProvider.this.setCallingPackage(original);
                    Trace.traceEnd(1048576);
                }
            } else if (projection != null) {
                return new MatrixCursor(projection, 0);
            } else {
                original = ContentProvider.this.setCallingPackage(callingPkg);
                try {
                    query = this.mInterface.query(uri, projection, queryArgs, CancellationSignal.fromTransport(cancellationSignal));
                    ContentProvider.this.setCallingPackage(original);
                    if (query == null) {
                        return null;
                    }
                    return new MatrixCursor(query.getColumnNames(), 0);
                } catch (RemoteException e2) {
                    throw e2.rethrowAsRuntimeException();
                } catch (Throwable th2) {
                    ContentProvider.this.setCallingPackage(original);
                }
            }
        }

        public String getType(Uri uri) {
            uri = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            Trace.traceBegin(1048576, "getType");
            try {
                String type = this.mInterface.getType(uri);
                Trace.traceEnd(1048576);
                return type;
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            } catch (Throwable th) {
                Trace.traceEnd(1048576);
            }
        }

        public Uri insert(String callingPkg, Uri uri, ContentValues initialValues) {
            uri = ContentProvider.this.validateIncomingUri(uri);
            int userId = ContentProvider.getUserIdFromUri(uri);
            uri = ContentProvider.this.maybeGetUriWithoutUserId(uri);
            if (enforceWritePermission(callingPkg, uri, null) != 0) {
                String original = ContentProvider.this.setCallingPackage(callingPkg);
                try {
                    Uri rejectInsert = ContentProvider.this.rejectInsert(uri, initialValues);
                    return rejectInsert;
                } finally {
                    ContentProvider.this.setCallingPackage(original);
                }
            } else {
                Trace.traceBegin(1048576, "insert");
                String original2 = ContentProvider.this.setCallingPackage(callingPkg);
                try {
                    Uri maybeAddUserId = ContentProvider.maybeAddUserId(this.mInterface.insert(uri, initialValues), userId);
                    ContentProvider.this.setCallingPackage(original2);
                    Trace.traceEnd(1048576);
                    return maybeAddUserId;
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                } catch (Throwable th) {
                    ContentProvider.this.setCallingPackage(original2);
                    Trace.traceEnd(1048576);
                }
            }
        }

        public int bulkInsert(String callingPkg, Uri uri, ContentValues[] initialValues) {
            uri = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            if (enforceWritePermission(callingPkg, uri, null) != 0) {
                return 0;
            }
            Trace.traceBegin(1048576, "bulkInsert");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                int bulkInsert = this.mInterface.bulkInsert(uri, initialValues);
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
                return bulkInsert;
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            } catch (Throwable th) {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
            }
        }

        public ContentProviderResult[] applyBatch(String callingPkg, String authority, ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
            ContentProvider.this.validateIncomingAuthority(authority);
            int numOperations = operations.size();
            int[] userIds = new int[numOperations];
            int i = 0;
            while (true) {
                int i2 = 0;
                if (i < numOperations) {
                    ContentProviderOperation operation = (ContentProviderOperation) operations.get(i);
                    Uri uri = operation.getUri();
                    userIds[i] = ContentProvider.getUserIdFromUri(uri);
                    uri = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
                    if (!Objects.equals(operation.getUri(), uri)) {
                        operation = new ContentProviderOperation(operation, uri);
                        operations.set(i, operation);
                    }
                    String str = "App op not allowed";
                    if (operation.isReadOperation() && enforceReadPermission(callingPkg, uri, null) != 0) {
                        throw new OperationApplicationException(str, 0);
                    } else if (!operation.isWriteOperation() || enforceWritePermission(callingPkg, uri, null) == 0) {
                        i++;
                    } else {
                        throw new OperationApplicationException(str, 0);
                    }
                }
                Trace.traceBegin(1048576, "applyBatch");
                String original = ContentProvider.this.setCallingPackage(callingPkg);
                try {
                    ContentProviderResult[] results = this.mInterface.applyBatch(authority, operations);
                    if (results != null) {
                        while (i2 < results.length) {
                            if (userIds[i2] != -2) {
                                results[i2] = new ContentProviderResult(results[i2], userIds[i2]);
                            }
                            i2++;
                        }
                    }
                    ContentProvider.this.setCallingPackage(original);
                    Trace.traceEnd(1048576);
                    return results;
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                } catch (Throwable th) {
                    ContentProvider.this.setCallingPackage(original);
                    Trace.traceEnd(1048576);
                }
            }
        }

        public int delete(String callingPkg, Uri uri, String selection, String[] selectionArgs) {
            uri = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            if (enforceWritePermission(callingPkg, uri, null) != 0) {
                return 0;
            }
            Trace.traceBegin(1048576, "delete");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                int delete = this.mInterface.delete(uri, selection, selectionArgs);
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
                return delete;
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            } catch (Throwable th) {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
            }
        }

        public int update(String callingPkg, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            uri = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            if (enforceWritePermission(callingPkg, uri, null) != 0) {
                return 0;
            }
            Trace.traceBegin(1048576, "update");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                int update = this.mInterface.update(uri, values, selection, selectionArgs);
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
                return update;
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            } catch (Throwable th) {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
            }
        }

        public ParcelFileDescriptor openFile(String callingPkg, Uri uri, String mode, ICancellationSignal cancellationSignal, IBinder callerToken) throws FileNotFoundException {
            uri = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            enforceFilePermission(callingPkg, uri, mode, callerToken);
            Trace.traceBegin(1048576, "openFile");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                ParcelFileDescriptor openFile = this.mInterface.openFile(uri, mode, CancellationSignal.fromTransport(cancellationSignal));
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
                return openFile;
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            } catch (Throwable th) {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
            }
        }

        public AssetFileDescriptor openAssetFile(String callingPkg, Uri uri, String mode, ICancellationSignal cancellationSignal) throws FileNotFoundException {
            uri = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            enforceFilePermission(callingPkg, uri, mode, null);
            Trace.traceBegin(1048576, "openAssetFile");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                AssetFileDescriptor openAssetFile = this.mInterface.openAssetFile(uri, mode, CancellationSignal.fromTransport(cancellationSignal));
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
                return openAssetFile;
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            } catch (Throwable th) {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
            }
        }

        public Bundle call(String callingPkg, String authority, String method, String arg, Bundle extras) {
            ContentProvider.this.validateIncomingAuthority(authority);
            Bundle.setDefusable(extras, true);
            Trace.traceBegin(1048576, "call");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                Bundle call = this.mInterface.call(authority, method, arg, extras);
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
                return call;
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            } catch (Throwable th) {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
            }
        }

        public String[] getStreamTypes(Uri uri, String mimeTypeFilter) {
            uri = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            Trace.traceBegin(1048576, "getStreamTypes");
            try {
                String[] streamTypes = this.mInterface.getStreamTypes(uri, mimeTypeFilter);
                Trace.traceEnd(1048576);
                return streamTypes;
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            } catch (Throwable th) {
                Trace.traceEnd(1048576);
            }
        }

        public AssetFileDescriptor openTypedAssetFile(String callingPkg, Uri uri, String mimeType, Bundle opts, ICancellationSignal cancellationSignal) throws FileNotFoundException {
            Bundle.setDefusable(opts, true);
            uri = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            enforceFilePermission(callingPkg, uri, "r", null);
            Trace.traceBegin(1048576, "openTypedAssetFile");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                AssetFileDescriptor openTypedAssetFile = this.mInterface.openTypedAssetFile(uri, mimeType, opts, CancellationSignal.fromTransport(cancellationSignal));
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
                return openTypedAssetFile;
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            } catch (Throwable th) {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
            }
        }

        public ICancellationSignal createCancellationSignal() {
            return CancellationSignal.createTransport();
        }

        public Uri canonicalize(String callingPkg, Uri uri) {
            uri = ContentProvider.this.validateIncomingUri(uri);
            int userId = ContentProvider.getUserIdFromUri(uri);
            uri = ContentProvider.getUriWithoutUserId(uri);
            if (enforceReadPermission(callingPkg, uri, null) != 0) {
                return null;
            }
            Trace.traceBegin(1048576, "canonicalize");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                Uri maybeAddUserId = ContentProvider.maybeAddUserId(this.mInterface.canonicalize(uri), userId);
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
                return maybeAddUserId;
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            } catch (Throwable th) {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
            }
        }

        public Uri uncanonicalize(String callingPkg, Uri uri) {
            uri = ContentProvider.this.validateIncomingUri(uri);
            int userId = ContentProvider.getUserIdFromUri(uri);
            uri = ContentProvider.getUriWithoutUserId(uri);
            if (enforceReadPermission(callingPkg, uri, null) != 0) {
                return null;
            }
            Trace.traceBegin(1048576, "uncanonicalize");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                Uri maybeAddUserId = ContentProvider.maybeAddUserId(this.mInterface.uncanonicalize(uri), userId);
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
                return maybeAddUserId;
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            } catch (Throwable th) {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
            }
        }

        public boolean refresh(String callingPkg, Uri uri, Bundle args, ICancellationSignal cancellationSignal) throws RemoteException {
            uri = ContentProvider.getUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            if (enforceReadPermission(callingPkg, uri, null) != 0) {
                return false;
            }
            Trace.traceBegin(1048576, "refresh");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                boolean refresh = this.mInterface.refresh(uri, args, CancellationSignal.fromTransport(cancellationSignal));
                return refresh;
            } finally {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576);
            }
        }

        private void enforceFilePermission(String callingPkg, Uri uri, String mode, IBinder callerToken) throws FileNotFoundException, SecurityException {
            String str = "App op not allowed";
            if (mode == null || mode.indexOf(119) == -1) {
                if (enforceReadPermission(callingPkg, uri, callerToken) != 0) {
                    throw new FileNotFoundException(str);
                }
            } else if (enforceWritePermission(callingPkg, uri, callerToken) != 0) {
                throw new FileNotFoundException(str);
            }
        }

        private int enforceReadPermission(String callingPkg, Uri uri, IBinder callerToken) throws SecurityException {
            int mode = ContentProvider.this.enforceReadPermissionInner(uri, callingPkg, callerToken);
            if (mode != 0) {
                return mode;
            }
            return noteProxyOp(callingPkg, this.mReadOp);
        }

        private int enforceWritePermission(String callingPkg, Uri uri, IBinder callerToken) throws SecurityException {
            int mode = ContentProvider.this.enforceWritePermissionInner(uri, callingPkg, callerToken);
            if (mode != 0) {
                return mode;
            }
            return noteProxyOp(callingPkg, this.mWriteOp);
        }

        private int noteProxyOp(String callingPkg, int op) {
            if (op == -1) {
                return 0;
            }
            int mode = this.mAppOpsManager.noteProxyOp(op, callingPkg);
            return mode == 3 ? 1 : mode;
        }
    }

    public abstract int delete(Uri uri, String str, String[] strArr);

    public abstract String getType(Uri uri);

    public abstract Uri insert(Uri uri, ContentValues contentValues);

    public abstract boolean onCreate();

    public abstract Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2);

    public abstract int update(Uri uri, ContentValues contentValues, String str, String[] strArr);

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public ContentProvider(Context context, String readPermission, String writePermission, PathPermission[] pathPermissions) {
        this.mContext = context;
        this.mReadPermission = readPermission;
        this.mWritePermission = writePermission;
        this.mPathPermissions = pathPermissions;
    }

    @UnsupportedAppUsage
    public static ContentProvider coerceToLocalContentProvider(IContentProvider abstractInterface) {
        if (abstractInterface instanceof Transport) {
            return ((Transport) abstractInterface).getContentProvider();
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean checkUser(int pid, int uid, Context context) {
        return UserHandle.getUserId(uid) == context.getUserId() || ((XSpaceUserHandle.isUidBelongtoXSpace(uid) && context.getUserId() == 0) || this.mSingleUser || context.checkPermission(permission.INTERACT_ACROSS_USERS, pid, uid) == 0);
    }

    private int checkPermissionAndAppOp(String permission, String callingPkg, IBinder callerToken) {
        if (getContext().checkPermission(permission, Binder.getCallingPid(), Binder.getCallingUid(), callerToken) != 0) {
            return 2;
        }
        int permOp = AppOpsManager.permissionToOpCode(permission);
        if (ContentProviderInjector.isMmsProviderClass(getClass().getName()) && permOp == 14) {
            permOp = 10005;
        }
        return this.mTransport.noteProxyOp(callingPkg, permOp);
    }

    /* Access modifiers changed, original: protected */
    public int enforceReadPermissionInner(Uri uri, String callingPkg, IBinder callerToken) throws SecurityException {
        Uri uri2 = uri;
        String str = callingPkg;
        IBinder iBinder = callerToken;
        Context context = getContext();
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        String missingPerm = null;
        int strongestMode = 0;
        if (UserHandle.isSameApp(uid, this.mMyUid)) {
            return 0;
        }
        String missingPerm2;
        int strongestMode2;
        if (this.mExported && checkUser(pid, uid, context)) {
            boolean allowDefaultRead;
            String componentPerm = getReadPermission();
            if (componentPerm != null) {
                int mode = checkPermissionAndAppOp(componentPerm, str, iBinder);
                if (mode == 0) {
                    return 0;
                }
                missingPerm = componentPerm;
                strongestMode = Math.max(0, mode);
            }
            boolean allowDefaultRead2 = componentPerm == null;
            PathPermission[] pps = getPathPermissions();
            if (pps != null) {
                String missingPerm3;
                String path = uri.getPath();
                allowDefaultRead = allowDefaultRead2;
                allowDefaultRead2 = strongestMode;
                String missingPerm4 = missingPerm;
                for (PathPermission pp : pps) {
                    String pathPerm = pp.getReadPermission();
                    if (pathPerm == null || !pp.match(path)) {
                        missingPerm3 = missingPerm4;
                    } else {
                        missingPerm4 = checkPermissionAndAppOp(pathPerm, str, iBinder);
                        if (missingPerm4 == null) {
                            return 0;
                        }
                        allowDefaultRead = false;
                        missingPerm3 = pathPerm;
                        allowDefaultRead2 = Math.max(allowDefaultRead2, missingPerm4);
                    }
                    missingPerm4 = missingPerm3;
                }
                missingPerm3 = missingPerm4;
                strongestMode = allowDefaultRead2;
                missingPerm = missingPerm3;
            } else {
                allowDefaultRead = allowDefaultRead2;
            }
            if (allowDefaultRead) {
                return 0;
            }
            missingPerm2 = missingPerm;
            strongestMode2 = strongestMode;
        } else {
            missingPerm2 = null;
            strongestMode2 = 0;
        }
        Uri userUri = (!this.mSingleUser || UserHandle.isSameUser(this.mMyUid, uid)) ? uri2 : maybeAddUserId(uri2, UserHandle.getUserId(uid));
        if (context.checkUriPermission(userUri, pid, uid, 1, callerToken) == 0) {
            return 0;
        }
        if (strongestMode2 == 1) {
            return 1;
        }
        if (permission.MANAGE_DOCUMENTS.equals(this.mReadPermission)) {
            missingPerm = " requires that you obtain access using ACTION_OPEN_DOCUMENT or related APIs";
        } else if (this.mExported) {
            missingPerm = new StringBuilder();
            missingPerm.append(" requires ");
            missingPerm.append(missingPerm2);
            missingPerm.append(", or grantUriPermission()");
            missingPerm = missingPerm.toString();
        } else {
            missingPerm = " requires the provider be exported, or grantUriPermission()";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Permission Denial: reading ");
        stringBuilder.append(getClass().getName());
        stringBuilder.append(" uri ");
        stringBuilder.append(uri2);
        stringBuilder.append(" from pid=");
        stringBuilder.append(pid);
        stringBuilder.append(", uid=");
        stringBuilder.append(uid);
        stringBuilder.append(missingPerm);
        throw new SecurityException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: protected */
    public int enforceWritePermissionInner(Uri uri, String callingPkg, IBinder callerToken) throws SecurityException {
        String str = callingPkg;
        IBinder iBinder = callerToken;
        Context context = getContext();
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        String missingPerm = null;
        int strongestMode = 0;
        if (UserHandle.isSameApp(uid, this.mMyUid)) {
            return 0;
        }
        String missingPerm2;
        int strongestMode2;
        if (this.mExported && checkUser(pid, uid, context)) {
            boolean allowDefaultWrite;
            String componentPerm = getWritePermission();
            if (componentPerm != null) {
                int mode = checkPermissionAndAppOp(componentPerm, str, iBinder);
                if (mode == 0) {
                    return 0;
                }
                missingPerm = componentPerm;
                strongestMode = Math.max(0, mode);
            }
            boolean allowDefaultWrite2 = componentPerm == null;
            PathPermission[] pps = getPathPermissions();
            if (pps != null) {
                String missingPerm3;
                String path = uri.getPath();
                allowDefaultWrite = allowDefaultWrite2;
                allowDefaultWrite2 = strongestMode;
                String missingPerm4 = missingPerm;
                for (PathPermission pp : pps) {
                    String pathPerm = pp.getWritePermission();
                    if (pathPerm == null || !pp.match(path)) {
                        missingPerm3 = missingPerm4;
                    } else {
                        missingPerm4 = checkPermissionAndAppOp(pathPerm, str, iBinder);
                        if (missingPerm4 == null) {
                            return 0;
                        }
                        allowDefaultWrite = false;
                        missingPerm3 = pathPerm;
                        allowDefaultWrite2 = Math.max(allowDefaultWrite2, missingPerm4);
                    }
                    missingPerm4 = missingPerm3;
                }
                missingPerm3 = missingPerm4;
                strongestMode = allowDefaultWrite2;
                missingPerm = missingPerm3;
            } else {
                allowDefaultWrite = allowDefaultWrite2;
            }
            if (allowDefaultWrite) {
                return 0;
            }
            missingPerm2 = missingPerm;
            strongestMode2 = strongestMode;
        } else {
            missingPerm2 = null;
            strongestMode2 = 0;
        }
        if (context.checkUriPermission(uri, pid, uid, 2, callerToken) == 0) {
            return 0;
        }
        if (strongestMode2 == 1) {
            return 1;
        }
        if (this.mExported) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" requires ");
            stringBuilder.append(missingPerm2);
            stringBuilder.append(", or grantUriPermission()");
            missingPerm = stringBuilder.toString();
        } else {
            missingPerm = " requires the provider be exported, or grantUriPermission()";
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Permission Denial: writing ");
        stringBuilder2.append(getClass().getName());
        stringBuilder2.append(" uri ");
        stringBuilder2.append(uri);
        stringBuilder2.append(" from pid=");
        stringBuilder2.append(pid);
        stringBuilder2.append(", uid=");
        stringBuilder2.append(uid);
        stringBuilder2.append(missingPerm);
        throw new SecurityException(stringBuilder2.toString());
    }

    public final Context getContext() {
        return this.mContext;
    }

    private String setCallingPackage(String callingPackage) {
        String original = (String) this.mCallingPackage.get();
        this.mCallingPackage.set(callingPackage);
        onCallingPackageChanged();
        return original;
    }

    public final String getCallingPackage() {
        String pkg = (String) this.mCallingPackage.get();
        if (pkg != null) {
            this.mTransport.mAppOpsManager.checkPackage(Binder.getCallingUid(), pkg);
        }
        return pkg;
    }

    public final String getCallingPackageUnchecked() {
        return (String) this.mCallingPackage.get();
    }

    public void onCallingPackageChanged() {
    }

    public final CallingIdentity clearCallingIdentity() {
        return new CallingIdentity(Binder.clearCallingIdentity(), setCallingPackage(null));
    }

    public final void restoreCallingIdentity(CallingIdentity identity) {
        Binder.restoreCallingIdentity(identity.binderToken);
        this.mCallingPackage.set(identity.callingPackage);
    }

    /* Access modifiers changed, original: protected|final */
    public final void setAuthorities(String authorities) {
        if (authorities == null) {
            return;
        }
        if (authorities.indexOf(59) == -1) {
            this.mAuthority = authorities;
            this.mAuthorities = null;
            return;
        }
        this.mAuthority = null;
        this.mAuthorities = authorities.split(";");
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean matchesOurAuthorities(String authority) {
        String str = this.mAuthority;
        if (str != null) {
            return str.equals(authority);
        }
        int length = this.mAuthorities;
        if (length != 0) {
            length = length.length;
            for (int i = 0; i < length; i++) {
                if (this.mAuthorities[i].equals(authority)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* Access modifiers changed, original: protected|final */
    public final void setReadPermission(String permission) {
        this.mReadPermission = permission;
    }

    public final String getReadPermission() {
        return this.mReadPermission;
    }

    /* Access modifiers changed, original: protected|final */
    public final void setWritePermission(String permission) {
        this.mWritePermission = permission;
    }

    public final String getWritePermission() {
        return this.mWritePermission;
    }

    /* Access modifiers changed, original: protected|final */
    public final void setPathPermissions(PathPermission[] permissions) {
        this.mPathPermissions = permissions;
    }

    public final PathPermission[] getPathPermissions() {
        return this.mPathPermissions;
    }

    @UnsupportedAppUsage
    public final void setAppOps(int readOp, int writeOp) {
        if (!this.mNoPerms) {
            Transport transport = this.mTransport;
            transport.mReadOp = readOp;
            transport.mWriteOp = writeOp;
        }
    }

    public AppOpsManager getAppOpsManager() {
        return this.mTransport.mAppOpsManager;
    }

    public final void setTransportLoggingEnabled(boolean enabled) {
        if (enabled) {
            this.mTransport.mInterface = new LoggingContentInterface(getClass().getSimpleName(), this);
            return;
        }
        this.mTransport.mInterface = this;
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }

    public void onLowMemory() {
    }

    public void onTrimMemory(int level) {
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) {
        return query(uri, projection, selection, selectionArgs, sortOrder);
    }

    public Cursor query(Uri uri, String[] projection, Bundle queryArgs, CancellationSignal cancellationSignal) {
        queryArgs = queryArgs != null ? queryArgs : Bundle.EMPTY;
        String sortClause = queryArgs.getString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER);
        if (sortClause == null && queryArgs.containsKey(ContentResolver.QUERY_ARG_SORT_COLUMNS)) {
            sortClause = ContentResolver.createSqlSortClause(queryArgs);
        }
        return query(uri, projection, queryArgs.getString(ContentResolver.QUERY_ARG_SQL_SELECTION), queryArgs.getStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS), sortClause, cancellationSignal);
    }

    public Uri canonicalize(Uri url) {
        return null;
    }

    public Uri uncanonicalize(Uri url) {
        return url;
    }

    public boolean refresh(Uri uri, Bundle args, CancellationSignal cancellationSignal) {
        return false;
    }

    public Uri rejectInsert(Uri uri, ContentValues values) {
        return uri.buildUpon().appendPath("0").build();
    }

    public int bulkInsert(Uri uri, ContentValues[] values) {
        for (ContentValues insert : values) {
            insert(uri, insert);
        }
        return numValues;
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No files supported by provider at ");
        stringBuilder.append(uri);
        throw new FileNotFoundException(stringBuilder.toString());
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode, CancellationSignal signal) throws FileNotFoundException {
        return openFile(uri, mode);
    }

    public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
        ParcelFileDescriptor fd = openFile(uri, mode);
        return fd != null ? new AssetFileDescriptor(fd, 0, -1) : null;
    }

    public AssetFileDescriptor openAssetFile(Uri uri, String mode, CancellationSignal signal) throws FileNotFoundException {
        return openAssetFile(uri, mode);
    }

    /* Access modifiers changed, original: protected|final */
    public final ParcelFileDescriptor openFileHelper(Uri uri, String mode) throws FileNotFoundException {
        int i = "_data";
        Cursor c = query(uri, new String[]{i}, null, null, null);
        int count = c != null ? c.getCount() : 0;
        if (count != 1) {
            if (c != null) {
                c.close();
            }
            StringBuilder stringBuilder;
            if (count == 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("No entry for ");
                stringBuilder.append(uri);
                throw new FileNotFoundException(stringBuilder.toString());
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Multiple items at ");
            stringBuilder.append(uri);
            throw new FileNotFoundException(stringBuilder.toString());
        }
        c.moveToFirst();
        i = c.getColumnIndex(i);
        String path = i >= 0 ? c.getString(i) : null;
        c.close();
        if (path != null) {
            return ParcelFileDescriptor.open(new File(path), ParcelFileDescriptor.parseMode(mode));
        }
        throw new FileNotFoundException("Column _data not found.");
    }

    public String[] getStreamTypes(Uri uri, String mimeTypeFilter) {
        return null;
    }

    public AssetFileDescriptor openTypedAssetFile(Uri uri, String mimeTypeFilter, Bundle opts) throws FileNotFoundException {
        String str = "r";
        if ("*/*".equals(mimeTypeFilter)) {
            return openAssetFile(uri, str);
        }
        String baseType = getType(uri);
        if (baseType != null && ClipDescription.compareMimeTypes(baseType, mimeTypeFilter)) {
            return openAssetFile(uri, str);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Can't open ");
        stringBuilder.append(uri);
        stringBuilder.append(" as type ");
        stringBuilder.append(mimeTypeFilter);
        throw new FileNotFoundException(stringBuilder.toString());
    }

    public AssetFileDescriptor openTypedAssetFile(Uri uri, String mimeTypeFilter, Bundle opts, CancellationSignal signal) throws FileNotFoundException {
        return openTypedAssetFile(uri, mimeTypeFilter, opts);
    }

    public <T> ParcelFileDescriptor openPipeHelper(Uri uri, String mimeType, Bundle opts, T args, PipeDataWriter<T> func) throws FileNotFoundException {
        try {
            ParcelFileDescriptor[] fds = ParcelFileDescriptor.createPipe();
            final PipeDataWriter<T> pipeDataWriter = func;
            final ParcelFileDescriptor[] parcelFileDescriptorArr = fds;
            final Uri uri2 = uri;
            final String str = mimeType;
            final Bundle bundle = opts;
            final T t = args;
            new AsyncTask<Object, Object, Object>() {
                /* Access modifiers changed, original: protected|varargs */
                public Object doInBackground(Object... params) {
                    pipeDataWriter.writeDataToPipe(parcelFileDescriptorArr[1], uri2, str, bundle, t);
                    try {
                        parcelFileDescriptorArr[1].close();
                    } catch (IOException e) {
                        Log.w(ContentProvider.TAG, "Failure closing pipe", e);
                    }
                    return null;
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[]) null);
            return fds[0];
        } catch (IOException e) {
            throw new FileNotFoundException("failure making pipe");
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean isTemporary() {
        return false;
    }

    @UnsupportedAppUsage
    public IContentProvider getIContentProvider() {
        return this.mTransport;
    }

    @UnsupportedAppUsage
    public void attachInfoForTesting(Context context, ProviderInfo info) {
        attachInfo(context, info, true);
    }

    public void attachInfo(Context context, ProviderInfo info) {
        attachInfo(context, info, false);
    }

    private void attachInfo(Context context, ProviderInfo info, boolean testing) {
        this.mNoPerms = testing;
        this.mCallingPackage = new ThreadLocal();
        if (this.mContext == null) {
            this.mContext = context;
            if (context != null) {
                Transport transport = this.mTransport;
                if (transport != null) {
                    transport.mAppOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                }
            }
            this.mMyUid = Process.myUid();
            if (info != null) {
                setReadPermission(info.readPermission);
                setWritePermission(info.writePermission);
                setPathPermissions(info.pathPermissions);
                this.mExported = info.exported;
                this.mSingleUser = (info.flags & 1073741824) != 0;
                setAuthorities(info.authority);
            }
            onCreate();
        }
    }

    public ContentProviderResult[] applyBatch(String authority, ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        return applyBatch(operations);
    }

    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        int numOperations = operations.size();
        ContentProviderResult[] results = new ContentProviderResult[numOperations];
        for (int i = 0; i < numOperations; i++) {
            results[i] = ((ContentProviderOperation) operations.get(i)).apply(this, results, i);
        }
        return results;
    }

    public Bundle call(String authority, String method, String arg, Bundle extras) {
        return call(method, arg, extras);
    }

    public Bundle call(String method, String arg, Bundle extras) {
        return null;
    }

    public void shutdown() {
        Log.w(TAG, "implement ContentProvider shutdown() to make sure all database connections are gracefully shutdown");
    }

    public void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        writer.println("nothing to dump");
    }

    private void validateIncomingAuthority(String authority) throws SecurityException {
        if (!matchesOurAuthorities(getAuthorityWithoutUserId(authority))) {
            String message = new StringBuilder();
            message.append("The authority ");
            message.append(authority);
            message.append(" does not match the one of the contentProvider: ");
            message = message.toString();
            StringBuilder stringBuilder;
            if (this.mAuthority != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(message);
                stringBuilder.append(this.mAuthority);
                message = stringBuilder.toString();
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(message);
                stringBuilder.append(Arrays.toString(this.mAuthorities));
                message = stringBuilder.toString();
            }
            throw new SecurityException(message);
        }
    }

    @VisibleForTesting
    public Uri validateIncomingUri(Uri uri) throws SecurityException {
        StringBuilder stringBuilder;
        String auth = uri.getAuthority();
        if (!this.mSingleUser) {
            int userId = getUserIdFromAuthority(auth, -2);
            if (!(userId == -2 || userId == this.mContext.getUserId() || ContentProviderInjector.isCrossUserIncomingUri(this.mContext, userId))) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("trying to query a ContentProvider in user ");
                stringBuilder.append(this.mContext.getUserId());
                stringBuilder.append(" with a uri belonging to user ");
                stringBuilder.append(userId);
                throw new SecurityException(stringBuilder.toString());
            }
        }
        validateIncomingAuthority(auth);
        String encodedPath = uri.getEncodedPath();
        if (encodedPath == null || encodedPath.indexOf("//") == -1) {
            return uri;
        }
        Uri normalized = uri.buildUpon().encodedPath(encodedPath.replaceAll("//+", "/")).build();
        stringBuilder = new StringBuilder();
        stringBuilder.append("Normalized ");
        stringBuilder.append(uri);
        stringBuilder.append(" to ");
        stringBuilder.append(normalized);
        stringBuilder.append(" to avoid possible security issues");
        Log.w(TAG, stringBuilder.toString());
        return normalized;
    }

    private Uri maybeGetUriWithoutUserId(Uri uri) {
        if (this.mSingleUser) {
            return uri;
        }
        return getUriWithoutUserId(uri);
    }

    public static int getUserIdFromAuthority(String auth, int defaultUserId) {
        if (auth == null) {
            return defaultUserId;
        }
        int end = auth.lastIndexOf(64);
        if (end == -1) {
            return defaultUserId;
        }
        try {
            return Integer.parseInt(auth.substring(null, end));
        } catch (NumberFormatException e) {
            Log.w(TAG, "Error parsing userId.", e);
            return -10000;
        }
    }

    public static int getUserIdFromAuthority(String auth) {
        return getUserIdFromAuthority(auth, -2);
    }

    public static int getUserIdFromUri(Uri uri, int defaultUserId) {
        if (uri == null) {
            return defaultUserId;
        }
        return getUserIdFromAuthority(uri.getAuthority(), defaultUserId);
    }

    public static int getUserIdFromUri(Uri uri) {
        return getUserIdFromUri(uri, -2);
    }

    public static String getAuthorityWithoutUserId(String auth) {
        if (auth == null) {
            return null;
        }
        return auth.substring(auth.lastIndexOf(64) + 1);
    }

    public static Uri getUriWithoutUserId(Uri uri) {
        if (uri == null) {
            return null;
        }
        Builder builder = uri.buildUpon();
        builder.authority(getAuthorityWithoutUserId(uri.getAuthority()));
        return builder.build();
    }

    public static boolean uriHasUserId(Uri uri) {
        if (uri == null) {
            return false;
        }
        return TextUtils.isEmpty(uri.getUserInfo()) ^ 1;
    }

    @UnsupportedAppUsage
    public static Uri maybeAddUserId(Uri uri, int userId) {
        if (uri == null) {
            return null;
        }
        if (userId != -2) {
            if ("content".equals(uri.getScheme()) && !uriHasUserId(uri)) {
                Builder builder = uri.buildUpon();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("");
                stringBuilder.append(userId);
                stringBuilder.append("@");
                stringBuilder.append(uri.getEncodedAuthority());
                builder.encodedAuthority(stringBuilder.toString());
                return builder.build();
            }
        }
        return uri;
    }
}
