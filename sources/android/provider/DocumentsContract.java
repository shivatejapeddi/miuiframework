package android.provider;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.MimeTypeFilter;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.OperationCanceledException;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.ParcelableException;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.util.Preconditions;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class DocumentsContract {
    @SystemApi
    public static final String ACTION_DOCUMENT_ROOT_SETTINGS = "android.provider.action.DOCUMENT_ROOT_SETTINGS";
    public static final String ACTION_DOCUMENT_SETTINGS = "android.provider.action.DOCUMENT_SETTINGS";
    @SystemApi
    public static final String ACTION_MANAGE_DOCUMENT = "android.provider.action.MANAGE_DOCUMENT";
    public static final String EXTERNAL_STORAGE_PRIMARY_EMULATED_ROOT_ID = "primary";
    public static final String EXTERNAL_STORAGE_PROVIDER_AUTHORITY = "com.android.externalstorage.documents";
    public static final String EXTRA_ERROR = "error";
    public static final String EXTRA_EXCLUDE_SELF = "android.provider.extra.EXCLUDE_SELF";
    public static final String EXTRA_INFO = "info";
    public static final String EXTRA_INITIAL_URI = "android.provider.extra.INITIAL_URI";
    public static final String EXTRA_LOADING = "loading";
    public static final String EXTRA_OPTIONS = "options";
    public static final String EXTRA_ORIENTATION = "android.provider.extra.ORIENTATION";
    @Deprecated
    public static final String EXTRA_PACKAGE_NAME = "android.intent.extra.PACKAGE_NAME";
    public static final String EXTRA_PARENT_URI = "parentUri";
    public static final String EXTRA_PROMPT = "android.provider.extra.PROMPT";
    public static final String EXTRA_RESULT = "result";
    @SystemApi
    public static final String EXTRA_SHOW_ADVANCED = "android.provider.extra.SHOW_ADVANCED";
    public static final String EXTRA_TARGET_URI = "android.content.extra.TARGET_URI";
    public static final String EXTRA_URI = "uri";
    public static final String EXTRA_URI_PERMISSIONS = "uriPermissions";
    public static final String METADATA_EXIF = "android:documentExif";
    public static final String METADATA_TREE_COUNT = "android:metadataTreeCount";
    public static final String METADATA_TREE_SIZE = "android:metadataTreeSize";
    public static final String METADATA_TYPES = "android:documentMetadataTypes";
    public static final String METHOD_COPY_DOCUMENT = "android:copyDocument";
    @UnsupportedAppUsage
    public static final String METHOD_CREATE_DOCUMENT = "android:createDocument";
    public static final String METHOD_CREATE_WEB_LINK_INTENT = "android:createWebLinkIntent";
    public static final String METHOD_DELETE_DOCUMENT = "android:deleteDocument";
    public static final String METHOD_EJECT_ROOT = "android:ejectRoot";
    public static final String METHOD_FIND_DOCUMENT_PATH = "android:findDocumentPath";
    public static final String METHOD_GET_DOCUMENT_METADATA = "android:getDocumentMetadata";
    public static final String METHOD_IS_CHILD_DOCUMENT = "android:isChildDocument";
    public static final String METHOD_MOVE_DOCUMENT = "android:moveDocument";
    public static final String METHOD_REMOVE_DOCUMENT = "android:removeDocument";
    public static final String METHOD_RENAME_DOCUMENT = "android:renameDocument";
    public static final String PACKAGE_DOCUMENTS_UI = "com.android.documentsui";
    private static final String PARAM_MANAGE = "manage";
    private static final String PARAM_QUERY = "query";
    private static final String PATH_CHILDREN = "children";
    @UnsupportedAppUsage
    private static final String PATH_DOCUMENT = "document";
    private static final String PATH_RECENT = "recent";
    private static final String PATH_ROOT = "root";
    private static final String PATH_SEARCH = "search";
    @UnsupportedAppUsage
    private static final String PATH_TREE = "tree";
    public static final String PROVIDER_INTERFACE = "android.content.action.DOCUMENTS_PROVIDER";
    public static final String QUERY_ARG_DISPLAY_NAME = "android:query-arg-display-name";
    public static final String QUERY_ARG_EXCLUDE_MEDIA = "android:query-arg-exclude-media";
    public static final String QUERY_ARG_FILE_SIZE_OVER = "android:query-arg-file-size-over";
    public static final String QUERY_ARG_LAST_MODIFIED_AFTER = "android:query-arg-last-modified-after";
    public static final String QUERY_ARG_MIME_TYPES = "android:query-arg-mime-types";
    private static final String TAG = "DocumentsContract";

    public static final class Document {
        public static final String COLUMN_DISPLAY_NAME = "_display_name";
        public static final String COLUMN_DOCUMENT_ID = "document_id";
        public static final String COLUMN_FLAGS = "flags";
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_LAST_MODIFIED = "last_modified";
        public static final String COLUMN_MIME_TYPE = "mime_type";
        public static final String COLUMN_SIZE = "_size";
        public static final String COLUMN_SUMMARY = "summary";
        public static final int FLAG_DIR_PREFERS_GRID = 16;
        public static final int FLAG_DIR_PREFERS_LAST_MODIFIED = 32;
        public static final int FLAG_DIR_SUPPORTS_CREATE = 8;
        public static final int FLAG_PARTIAL = 8192;
        public static final int FLAG_SUPPORTS_COPY = 128;
        public static final int FLAG_SUPPORTS_DELETE = 4;
        public static final int FLAG_SUPPORTS_METADATA = 16384;
        public static final int FLAG_SUPPORTS_MOVE = 256;
        public static final int FLAG_SUPPORTS_REMOVE = 1024;
        public static final int FLAG_SUPPORTS_RENAME = 64;
        public static final int FLAG_SUPPORTS_SETTINGS = 2048;
        public static final int FLAG_SUPPORTS_THUMBNAIL = 1;
        public static final int FLAG_SUPPORTS_WRITE = 2;
        public static final int FLAG_VIRTUAL_DOCUMENT = 512;
        public static final int FLAG_WEB_LINKABLE = 4096;
        public static final String MIME_TYPE_DIR = "vnd.android.document/directory";

        private Document() {
        }
    }

    public static final class Path implements Parcelable {
        public static final Creator<Path> CREATOR = new Creator<Path>() {
            public Path createFromParcel(Parcel in) {
                return new Path(in.readString(), in.createStringArrayList());
            }

            public Path[] newArray(int size) {
                return new Path[size];
            }
        };
        private final List<String> mPath;
        private final String mRootId;

        public Path(String rootId, List<String> path) {
            String str = "path";
            Preconditions.checkCollectionNotEmpty(path, str);
            Preconditions.checkCollectionElementsNotNull(path, str);
            this.mRootId = rootId;
            this.mPath = path;
        }

        public String getRootId() {
            return this.mRootId;
        }

        public List<String> getPath() {
            return this.mPath;
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (o == null || !(o instanceof Path)) {
                return false;
            }
            Path path = (Path) o;
            if (!(Objects.equals(this.mRootId, path.mRootId) && Objects.equals(this.mPath, path.mPath))) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mRootId, this.mPath});
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DocumentsContract.Path{");
            stringBuilder.append("rootId=");
            stringBuilder.append(this.mRootId);
            stringBuilder.append(", path=");
            stringBuilder.append(this.mPath);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mRootId);
            dest.writeStringList(this.mPath);
        }

        public int describeContents() {
            return 0;
        }
    }

    public static final class Root {
        public static final String COLUMN_AVAILABLE_BYTES = "available_bytes";
        public static final String COLUMN_CAPACITY_BYTES = "capacity_bytes";
        public static final String COLUMN_DOCUMENT_ID = "document_id";
        public static final String COLUMN_FLAGS = "flags";
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_MIME_TYPES = "mime_types";
        public static final String COLUMN_QUERY_ARGS = "query_args";
        public static final String COLUMN_ROOT_ID = "root_id";
        public static final String COLUMN_SUMMARY = "summary";
        public static final String COLUMN_TITLE = "title";
        @SystemApi
        public static final int FLAG_ADVANCED = 65536;
        public static final int FLAG_EMPTY = 64;
        @SystemApi
        public static final int FLAG_HAS_SETTINGS = 131072;
        public static final int FLAG_LOCAL_ONLY = 2;
        @SystemApi
        public static final int FLAG_REMOVABLE_SD = 262144;
        @SystemApi
        public static final int FLAG_REMOVABLE_USB = 524288;
        public static final int FLAG_SUPPORTS_CREATE = 1;
        public static final int FLAG_SUPPORTS_EJECT = 32;
        public static final int FLAG_SUPPORTS_IS_CHILD = 16;
        public static final int FLAG_SUPPORTS_RECENTS = 4;
        public static final int FLAG_SUPPORTS_SEARCH = 8;
        public static final String MIME_TYPE_ITEM = "vnd.android.document/root";

        private Root() {
        }
    }

    private DocumentsContract() {
    }

    public static Uri buildRootsUri(String authority) {
        return new Builder().scheme("content").authority(authority).appendPath(PATH_ROOT).build();
    }

    public static Uri buildRootUri(String authority, String rootId) {
        return new Builder().scheme("content").authority(authority).appendPath(PATH_ROOT).appendPath(rootId).build();
    }

    public static Uri buildRecentDocumentsUri(String authority, String rootId) {
        return new Builder().scheme("content").authority(authority).appendPath(PATH_ROOT).appendPath(rootId).appendPath(PATH_RECENT).build();
    }

    public static Uri buildTreeDocumentUri(String authority, String documentId) {
        return new Builder().scheme("content").authority(authority).appendPath(PATH_TREE).appendPath(documentId).build();
    }

    public static Uri buildDocumentUri(String authority, String documentId) {
        return getBaseDocumentUriBuilder(authority).appendPath(documentId).build();
    }

    public static Uri buildBaseDocumentUri(String authority) {
        return getBaseDocumentUriBuilder(authority).build();
    }

    private static Builder getBaseDocumentUriBuilder(String authority) {
        return new Builder().scheme("content").authority(authority).appendPath(PATH_DOCUMENT);
    }

    public static Uri buildDocumentUriUsingTree(Uri treeUri, String documentId) {
        return new Builder().scheme("content").authority(treeUri.getAuthority()).appendPath(PATH_TREE).appendPath(getTreeDocumentId(treeUri)).appendPath(PATH_DOCUMENT).appendPath(documentId).build();
    }

    public static Uri buildDocumentUriMaybeUsingTree(Uri baseUri, String documentId) {
        if (isTreeUri(baseUri)) {
            return buildDocumentUriUsingTree(baseUri, documentId);
        }
        return buildDocumentUri(baseUri.getAuthority(), documentId);
    }

    public static Uri buildChildDocumentsUri(String authority, String parentDocumentId) {
        return new Builder().scheme("content").authority(authority).appendPath(PATH_DOCUMENT).appendPath(parentDocumentId).appendPath("children").build();
    }

    public static Uri buildChildDocumentsUriUsingTree(Uri treeUri, String parentDocumentId) {
        return new Builder().scheme("content").authority(treeUri.getAuthority()).appendPath(PATH_TREE).appendPath(getTreeDocumentId(treeUri)).appendPath(PATH_DOCUMENT).appendPath(parentDocumentId).appendPath("children").build();
    }

    public static Uri buildSearchDocumentsUri(String authority, String rootId, String query) {
        return new Builder().scheme("content").authority(authority).appendPath(PATH_ROOT).appendPath(rootId).appendPath("search").appendQueryParameter("query", query).build();
    }

    public static boolean matchSearchQueryArguments(Bundle queryArgs, String displayName, String mimeType, long lastModified, long size) {
        if (queryArgs == null) {
            return true;
        }
        String argDisplayName = queryArgs.getString(QUERY_ARG_DISPLAY_NAME, "");
        if (!argDisplayName.isEmpty() && !displayName.toLowerCase().contains(argDisplayName.toLowerCase())) {
            return false;
        }
        long argFileSize = queryArgs.getLong(QUERY_ARG_FILE_SIZE_OVER, -1);
        if (argFileSize != -1 && size < argFileSize) {
            return false;
        }
        long argLastModified = queryArgs.getLong(QUERY_ARG_LAST_MODIFIED_AFTER, -1);
        if (argLastModified != -1 && lastModified < argLastModified) {
            return false;
        }
        String[] argMimeTypes = queryArgs.getStringArray(QUERY_ARG_MIME_TYPES);
        if (argMimeTypes == null || argMimeTypes.length <= 0) {
            return true;
        }
        String mimeType2 = Intent.normalizeMimeType(mimeType);
        for (String type : argMimeTypes) {
            if (MimeTypeFilter.matches(mimeType2, Intent.normalizeMimeType(type))) {
                return true;
            }
        }
        return false;
    }

    public static String[] getHandledQueryArguments(Bundle queryArgs) {
        if (queryArgs == null) {
            return new String[0];
        }
        ArrayList<String> args = new ArrayList();
        Set keySet = queryArgs.keySet();
        String str = QUERY_ARG_EXCLUDE_MEDIA;
        if (keySet.contains(str)) {
            args.add(str);
        }
        keySet = queryArgs.keySet();
        str = QUERY_ARG_DISPLAY_NAME;
        if (keySet.contains(str)) {
            args.add(str);
        }
        keySet = queryArgs.keySet();
        str = QUERY_ARG_FILE_SIZE_OVER;
        if (keySet.contains(str)) {
            args.add(str);
        }
        keySet = queryArgs.keySet();
        str = QUERY_ARG_LAST_MODIFIED_AFTER;
        if (keySet.contains(str)) {
            args.add(str);
        }
        keySet = queryArgs.keySet();
        str = QUERY_ARG_MIME_TYPES;
        if (keySet.contains(str)) {
            args.add(str);
        }
        return (String[]) args.toArray(new String[0]);
    }

    public static boolean isDocumentUri(Context context, Uri uri) {
        boolean z = false;
        if (isContentUri(uri) && isDocumentsProvider(context, uri.getAuthority())) {
            List<String> paths = uri.getPathSegments();
            int size = paths.size();
            String str = PATH_DOCUMENT;
            if (size == 2) {
                return str.equals(paths.get(0));
            }
            if (paths.size() == 4) {
                if (PATH_TREE.equals(paths.get(0)) && str.equals(paths.get(2))) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }

    public static boolean isRootsUri(Context context, Uri uri) {
        Preconditions.checkNotNull(context, "context can not be null");
        return isRootUri(context, uri, 1);
    }

    public static boolean isRootUri(Context context, Uri uri) {
        Preconditions.checkNotNull(context, "context can not be null");
        return isRootUri(context, uri, 2);
    }

    public static boolean isContentUri(Uri uri) {
        if (uri != null) {
            if ("content".equals(uri.getScheme())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTreeUri(Uri uri) {
        List<String> paths = uri.getPathSegments();
        if (paths.size() < 2) {
            return false;
        }
        return PATH_TREE.equals(paths.get(0));
    }

    private static boolean isRootUri(Context context, Uri uri, int pathSize) {
        boolean z = false;
        if (!isContentUri(uri) || !isDocumentsProvider(context, uri.getAuthority())) {
            return false;
        }
        List<String> paths = uri.getPathSegments();
        if (paths.size() == pathSize) {
            if (PATH_ROOT.equals(paths.get(0))) {
                z = true;
            }
        }
        return z;
    }

    private static boolean isDocumentsProvider(Context context, String authority) {
        for (ResolveInfo info : context.getPackageManager().queryIntentContentProviders(new Intent(PROVIDER_INTERFACE), 0)) {
            if (authority.equals(info.providerInfo.authority)) {
                return true;
            }
        }
        return false;
    }

    public static String getRootId(Uri rootUri) {
        List<String> paths = rootUri.getPathSegments();
        if (paths.size() >= 2) {
            if (PATH_ROOT.equals(paths.get(0))) {
                return (String) paths.get(1);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid URI: ");
        stringBuilder.append(rootUri);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static String getDocumentId(Uri documentUri) {
        List<String> paths = documentUri.getPathSegments();
        int size = paths.size();
        String str = PATH_DOCUMENT;
        if (size >= 2 && str.equals(paths.get(0))) {
            return (String) paths.get(1);
        }
        if (paths.size() >= 4) {
            if (PATH_TREE.equals(paths.get(0)) && str.equals(paths.get(2))) {
                return (String) paths.get(3);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid URI: ");
        stringBuilder.append(documentUri);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static String getTreeDocumentId(Uri documentUri) {
        List<String> paths = documentUri.getPathSegments();
        if (paths.size() >= 2) {
            if (PATH_TREE.equals(paths.get(0))) {
                return (String) paths.get(1);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid URI: ");
        stringBuilder.append(documentUri);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static String getSearchDocumentsQuery(Uri searchDocumentsUri) {
        return searchDocumentsUri.getQueryParameter("query");
    }

    public static String getSearchDocumentsQuery(Bundle bundle) {
        Preconditions.checkNotNull(bundle, "bundle can not be null");
        return bundle.getString(QUERY_ARG_DISPLAY_NAME, "");
    }

    @SystemApi
    public static Uri setManageMode(Uri uri) {
        Preconditions.checkNotNull(uri, "uri can not be null");
        return uri.buildUpon().appendQueryParameter(PARAM_MANAGE, "true").build();
    }

    @SystemApi
    public static boolean isManageMode(Uri uri) {
        Preconditions.checkNotNull(uri, "uri can not be null");
        return uri.getBooleanQueryParameter(PARAM_MANAGE, false);
    }

    public static Bitmap getDocumentThumbnail(ContentResolver content, Uri documentUri, Point size, CancellationSignal signal) throws FileNotFoundException {
        try {
            return ContentResolver.loadThumbnail(content, documentUri, Point.convert(size), signal, 1);
        } catch (Exception e) {
            if (!(e instanceof OperationCanceledException)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to load thumbnail for ");
                stringBuilder.append(documentUri);
                stringBuilder.append(": ");
                stringBuilder.append(e);
                Log.w(TAG, stringBuilder.toString());
            }
            rethrowIfNecessary(e);
            return null;
        }
    }

    public static Uri createDocument(ContentResolver content, Uri parentDocumentUri, String mimeType, String displayName) throws FileNotFoundException {
        String str = "uri";
        try {
            Bundle in = new Bundle();
            in.putParcelable(str, parentDocumentUri);
            in.putString("mime_type", mimeType);
            in.putString("_display_name", displayName);
            return (Uri) content.call(parentDocumentUri.getAuthority(), METHOD_CREATE_DOCUMENT, null, in).getParcelable(str);
        } catch (Exception e) {
            Log.w(TAG, "Failed to create document", e);
            rethrowIfNecessary(e);
            return null;
        }
    }

    public static boolean isChildDocument(ContentResolver content, Uri parentDocumentUri, Uri childDocumentUri) throws FileNotFoundException {
        String str = "result";
        Preconditions.checkNotNull(content, "content can not be null");
        Preconditions.checkNotNull(parentDocumentUri, "parentDocumentUri can not be null");
        Preconditions.checkNotNull(childDocumentUri, "childDocumentUri can not be null");
        try {
            Bundle in = new Bundle();
            in.putParcelable("uri", parentDocumentUri);
            in.putParcelable(EXTRA_TARGET_URI, childDocumentUri);
            Bundle out = content.call(parentDocumentUri.getAuthority(), METHOD_IS_CHILD_DOCUMENT, null, in);
            if (out == null) {
                throw new RemoteException("Failed to get a response from isChildDocument query.");
            } else if (out.containsKey(str)) {
                return out.getBoolean(str);
            } else {
                throw new RemoteException("Response did not include result field..");
            }
        } catch (Exception e) {
            Log.w(TAG, "Failed to create document", e);
            rethrowIfNecessary(e);
            return false;
        }
    }

    public static Uri renameDocument(ContentResolver content, Uri documentUri, String displayName) throws FileNotFoundException {
        String str = "uri";
        try {
            Bundle in = new Bundle();
            in.putParcelable(str, documentUri);
            in.putString("_display_name", displayName);
            Uri outUri = (Uri) content.call(documentUri.getAuthority(), METHOD_RENAME_DOCUMENT, null, in).getParcelable(str);
            return outUri != null ? outUri : documentUri;
        } catch (Exception e) {
            Log.w(TAG, "Failed to rename document", e);
            rethrowIfNecessary(e);
            return null;
        }
    }

    public static boolean deleteDocument(ContentResolver content, Uri documentUri) throws FileNotFoundException {
        try {
            Bundle in = new Bundle();
            in.putParcelable("uri", documentUri);
            content.call(documentUri.getAuthority(), METHOD_DELETE_DOCUMENT, null, in);
            return true;
        } catch (Exception e) {
            Log.w(TAG, "Failed to delete document", e);
            rethrowIfNecessary(e);
            return false;
        }
    }

    public static Uri copyDocument(ContentResolver content, Uri sourceDocumentUri, Uri targetParentDocumentUri) throws FileNotFoundException {
        String str = "uri";
        try {
            Bundle in = new Bundle();
            in.putParcelable(str, sourceDocumentUri);
            in.putParcelable(EXTRA_TARGET_URI, targetParentDocumentUri);
            return (Uri) content.call(sourceDocumentUri.getAuthority(), METHOD_COPY_DOCUMENT, null, in).getParcelable(str);
        } catch (Exception e) {
            Log.w(TAG, "Failed to copy document", e);
            rethrowIfNecessary(e);
            return null;
        }
    }

    public static Uri moveDocument(ContentResolver content, Uri sourceDocumentUri, Uri sourceParentDocumentUri, Uri targetParentDocumentUri) throws FileNotFoundException {
        String str = "uri";
        try {
            Bundle in = new Bundle();
            in.putParcelable(str, sourceDocumentUri);
            in.putParcelable(EXTRA_PARENT_URI, sourceParentDocumentUri);
            in.putParcelable(EXTRA_TARGET_URI, targetParentDocumentUri);
            return (Uri) content.call(sourceDocumentUri.getAuthority(), METHOD_MOVE_DOCUMENT, null, in).getParcelable(str);
        } catch (Exception e) {
            Log.w(TAG, "Failed to move document", e);
            rethrowIfNecessary(e);
            return null;
        }
    }

    public static boolean removeDocument(ContentResolver content, Uri documentUri, Uri parentDocumentUri) throws FileNotFoundException {
        try {
            Bundle in = new Bundle();
            in.putParcelable("uri", documentUri);
            in.putParcelable(EXTRA_PARENT_URI, parentDocumentUri);
            content.call(documentUri.getAuthority(), METHOD_REMOVE_DOCUMENT, null, in);
            return true;
        } catch (Exception e) {
            Log.w(TAG, "Failed to remove document", e);
            rethrowIfNecessary(e);
            return false;
        }
    }

    public static void ejectRoot(ContentResolver content, Uri rootUri) {
        try {
            Bundle in = new Bundle();
            in.putParcelable("uri", rootUri);
            content.call(rootUri.getAuthority(), METHOD_EJECT_ROOT, null, in);
        } catch (Exception e) {
            Log.w(TAG, "Failed to eject", e);
        }
    }

    public static Bundle getDocumentMetadata(ContentResolver content, Uri documentUri) throws FileNotFoundException {
        Preconditions.checkNotNull(content, "content can not be null");
        Preconditions.checkNotNull(documentUri, "documentUri can not be null");
        Bundle bundle = null;
        try {
            Bundle in = new Bundle();
            in.putParcelable("uri", documentUri);
            bundle = content.call(documentUri.getAuthority(), METHOD_GET_DOCUMENT_METADATA, null, in);
            return bundle;
        } catch (Exception e) {
            Log.w(TAG, "Failed to get document metadata");
            rethrowIfNecessary(e);
            return bundle;
        }
    }

    public static Path findDocumentPath(ContentResolver content, Uri treeUri) throws FileNotFoundException {
        try {
            Bundle in = new Bundle();
            in.putParcelable("uri", treeUri);
            return (Path) content.call(treeUri.getAuthority(), METHOD_FIND_DOCUMENT_PATH, null, in).getParcelable("result");
        } catch (Exception e) {
            Log.w(TAG, "Failed to find path", e);
            rethrowIfNecessary(e);
            return null;
        }
    }

    public static IntentSender createWebLinkIntent(ContentResolver content, Uri uri, Bundle options) throws FileNotFoundException {
        try {
            Bundle in = new Bundle();
            in.putParcelable("uri", uri);
            if (options != null) {
                in.putBundle(EXTRA_OPTIONS, options);
            }
            return (IntentSender) content.call(uri.getAuthority(), METHOD_CREATE_WEB_LINK_INTENT, null, in).getParcelable("result");
        } catch (Exception e) {
            Log.w(TAG, "Failed to create a web link intent", e);
            rethrowIfNecessary(e);
            return null;
        }
    }

    public static AssetFileDescriptor openImageThumbnail(File file) throws FileNotFoundException {
        ParcelFileDescriptor pfd = ParcelFileDescriptor.open(file, 268435456);
        try {
            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            long[] thumb = exif.getThumbnailRange();
            if (thumb != null) {
                Bundle extras;
                int attributeInt = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
                String str = EXTRA_ORIENTATION;
                Bundle extras2;
                if (attributeInt == 3) {
                    extras2 = new Bundle(1);
                    extras2.putInt(str, 180);
                    extras = extras2;
                } else if (attributeInt == 6) {
                    extras2 = new Bundle(1);
                    extras2.putInt(str, 90);
                    extras = extras2;
                } else if (attributeInt != 8) {
                    extras = null;
                } else {
                    extras2 = new Bundle(1);
                    extras2.putInt(str, 270);
                    extras = extras2;
                }
                return new AssetFileDescriptor(pfd, thumb[0], thumb[1], extras);
            }
        } catch (IOException e) {
        }
        return new AssetFileDescriptor(pfd, 0, -1, null);
    }

    private static void rethrowIfNecessary(Exception e) throws FileNotFoundException {
        if (VMRuntime.getRuntime().getTargetSdkVersion() < 26) {
            return;
        }
        if (e instanceof ParcelableException) {
            ((ParcelableException) e).maybeRethrow(FileNotFoundException.class);
        } else if (e instanceof RemoteException) {
            ((RemoteException) e).rethrowAsRuntimeException();
        } else if (e instanceof RuntimeException) {
            throw ((RuntimeException) e);
        }
    }
}
