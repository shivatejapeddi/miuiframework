package com.android.internal.content;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MatrixCursor.RowBuilder;
import android.graphics.Point;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.FileObserver;
import android.os.FileUtils;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.DocumentsContract.Document;
import android.provider.DocumentsProvider;
import android.provider.MediaStore;
import android.provider.MetadataReader;
import android.system.Int64Ref;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.webkit.MimeTypeMap;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ArrayUtils;
import com.android.internal.widget.MessagingMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import libcore.io.IoUtils;

public abstract class FileSystemProvider extends DocumentsProvider {
    private static final boolean LOG_INOTIFY = false;
    protected static final String SUPPORTED_QUERY_ARGS = joinNewline(DocumentsContract.QUERY_ARG_DISPLAY_NAME, DocumentsContract.QUERY_ARG_FILE_SIZE_OVER, DocumentsContract.QUERY_ARG_LAST_MODIFIED_AFTER, DocumentsContract.QUERY_ARG_MIME_TYPES);
    private static final String TAG = "FileSystemProvider";
    private String[] mDefaultProjection;
    private Handler mHandler;
    @GuardedBy({"mObservers"})
    private final ArrayMap<File, DirectoryObserver> mObservers = new ArrayMap();

    private class DirectoryCursor extends MatrixCursor {
        private final File mFile;

        public DirectoryCursor(String[] columnNames, String docId, File file) {
            super(columnNames);
            Uri notifyUri = FileSystemProvider.this.buildNotificationUri(docId);
            setNotificationUris(FileSystemProvider.this.getContext().getContentResolver(), Arrays.asList(new Uri[]{notifyUri}), FileSystemProvider.this.getContext().getContentResolver().getUserId(), false);
            this.mFile = file;
            FileSystemProvider.this.startObserving(this.mFile, notifyUri, this);
        }

        public void notifyChanged() {
            onChange(false);
        }

        public void close() {
            super.close();
            FileSystemProvider.this.stopObserving(this.mFile, this);
        }
    }

    private static class DirectoryObserver extends FileObserver {
        private static final int NOTIFY_EVENTS = 4044;
        private final CopyOnWriteArrayList<DirectoryCursor> mCursors = new CopyOnWriteArrayList();
        private final File mFile;
        private final Uri mNotifyUri;
        private final ContentResolver mResolver;

        DirectoryObserver(File file, ContentResolver resolver, Uri notifyUri) {
            super(file.getAbsolutePath(), (int) NOTIFY_EVENTS);
            this.mFile = file;
            this.mResolver = resolver;
            this.mNotifyUri = notifyUri;
        }

        public void onEvent(int event, String path) {
            if ((event & NOTIFY_EVENTS) != 0) {
                Iterator it = this.mCursors.iterator();
                while (it.hasNext()) {
                    ((DirectoryCursor) it.next()).notifyChanged();
                }
                this.mResolver.notifyChange(this.mNotifyUri, null, false);
            }
        }

        public String toString() {
            String filePath = this.mFile.getAbsolutePath();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DirectoryObserver{file=");
            stringBuilder.append(filePath);
            stringBuilder.append(", ref=");
            stringBuilder.append(this.mCursors.size());
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public abstract Uri buildNotificationUri(String str);

    public abstract String getDocIdForFile(File file) throws FileNotFoundException;

    public abstract File getFileForDocId(String str, boolean z) throws FileNotFoundException;

    private static String joinNewline(String... args) {
        return TextUtils.join((CharSequence) "\n", (Object[]) args);
    }

    /* Access modifiers changed, original: protected */
    public void onDocIdChanged(String docId) {
    }

    public boolean onCreate() {
        throw new UnsupportedOperationException("Subclass should override this and call onCreate(defaultDocumentProjection)");
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(String[] defaultProjection) {
        this.mHandler = new Handler();
        this.mDefaultProjection = defaultProjection;
    }

    public boolean isChildDocument(String parentDocId, String docId) {
        try {
            return FileUtils.contains(getFileForDocId(parentDocId).getCanonicalFile(), getFileForDocId(docId).getCanonicalFile());
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to determine if ");
            stringBuilder.append(docId);
            stringBuilder.append(" is child of ");
            stringBuilder.append(parentDocId);
            stringBuilder.append(": ");
            stringBuilder.append(e);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public Bundle getDocumentMetadata(String documentId) throws FileNotFoundException {
        File file = getFileForDocId(documentId);
        StringBuilder stringBuilder;
        if (file.exists()) {
            String mimeType = getDocumentType(documentId);
            boolean equals = Document.MIME_TYPE_DIR.equals(mimeType);
            String str = "An error occurred retrieving the metadata";
            String str2 = TAG;
            if (equals) {
                final Int64Ref treeCount = new Int64Ref(0);
                final Int64Ref treeSize = new Int64Ref(0);
                try {
                    Files.walkFileTree(FileSystems.getDefault().getPath(file.getAbsolutePath(), new String[0]), new FileVisitor<Path>() {
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                            return FileVisitResult.CONTINUE;
                        }

                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                            Int64Ref int64Ref = treeCount;
                            int64Ref.value++;
                            int64Ref = treeSize;
                            int64Ref.value += attrs.size();
                            return FileVisitResult.CONTINUE;
                        }

                        public FileVisitResult visitFileFailed(Path file, IOException exc) {
                            return FileVisitResult.CONTINUE;
                        }

                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                            return FileVisitResult.CONTINUE;
                        }
                    });
                    Bundle res = new Bundle();
                    res.putLong(DocumentsContract.METADATA_TREE_COUNT, treeCount.value);
                    res.putLong(DocumentsContract.METADATA_TREE_SIZE, treeSize.value);
                    return res;
                } catch (IOException e) {
                    Log.e(str2, str, e);
                    return null;
                }
            } else if (!file.isFile()) {
                Log.w(str2, "Can't stream non-regular file. Returning empty metadata.");
                return null;
            } else if (!file.canRead()) {
                Log.w(str2, "Can't stream non-readable file. Returning empty metadata.");
                return null;
            } else if (MetadataReader.isSupportedMimeType(mimeType)) {
                InputStream stream = null;
                Bundle metadata;
                try {
                    metadata = new Bundle();
                    stream = new FileInputStream(file.getAbsolutePath());
                    MetadataReader.getMetadata(metadata, stream, mimeType, null);
                    return metadata;
                } catch (IOException e2) {
                    metadata = e2;
                    Log.e(str2, str, metadata);
                    return null;
                } finally {
                    IoUtils.closeQuietly(stream);
                }
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported type ");
                stringBuilder.append(mimeType);
                stringBuilder.append(". Returning empty metadata.");
                Log.w(str2, stringBuilder.toString());
                return null;
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Can't find the file for documentId: ");
        stringBuilder.append(documentId);
        throw new FileNotFoundException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: protected|final */
    public final List<String> findDocumentPath(File parent, File doc) throws FileNotFoundException {
        StringBuilder stringBuilder;
        if (!doc.exists()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(doc);
            stringBuilder.append(" is not found.");
            throw new FileNotFoundException(stringBuilder.toString());
        } else if (FileUtils.contains(parent, doc)) {
            LinkedList<String> path = new LinkedList();
            while (doc != null && FileUtils.contains(parent, doc)) {
                path.addFirst(getDocIdForFile(doc));
                doc = doc.getParentFile();
            }
            return path;
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(doc);
            stringBuilder.append(" is not found under ");
            stringBuilder.append(parent);
            throw new FileNotFoundException(stringBuilder.toString());
        }
    }

    public String createDocument(String docId, String mimeType, String displayName) throws FileNotFoundException {
        String str = "Failed to touch ";
        displayName = FileUtils.buildValidFatFilename(displayName);
        File parent = getFileForDocId(docId);
        if (parent.isDirectory()) {
            File file = FileUtils.buildUniqueFile(parent, mimeType, displayName);
            if (!Document.MIME_TYPE_DIR.equals(mimeType)) {
                try {
                    if (file.createNewFile()) {
                        String childId = getDocIdForFile(file);
                        onDocIdChanged(childId);
                        str = childId;
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(file);
                        throw new IllegalStateException(stringBuilder.toString());
                    }
                } catch (IOException e) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str);
                    stringBuilder2.append(file);
                    stringBuilder2.append(": ");
                    stringBuilder2.append(e);
                    throw new IllegalStateException(stringBuilder2.toString());
                }
            } else if (file.mkdir()) {
                str = getDocIdForFile(file);
                onDocIdChanged(str);
            } else {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Failed to mkdir ");
                stringBuilder3.append(file);
                throw new IllegalStateException(stringBuilder3.toString());
            }
            MediaStore.scanFile(getContext(), file);
            return str;
        }
        throw new IllegalArgumentException("Parent document isn't a directory");
    }

    public String renameDocument(String docId, String displayName) throws FileNotFoundException {
        displayName = FileUtils.buildValidFatFilename(displayName);
        File before = getFileForDocId(docId);
        File beforeVisibleFile = getFileForDocId(docId, true);
        File after = FileUtils.buildUniqueFile(before.getParentFile(), displayName);
        if (before.renameTo(after)) {
            String afterDocId = getDocIdForFile(after);
            onDocIdChanged(docId);
            onDocIdChanged(afterDocId);
            moveInMediaStore(beforeVisibleFile, getFileForDocId(afterDocId, true));
            if (TextUtils.equals(docId, afterDocId)) {
                return null;
            }
            return afterDocId;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to rename to ");
        stringBuilder.append(after);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public String moveDocument(String sourceDocumentId, String sourceParentDocumentId, String targetParentDocumentId) throws FileNotFoundException {
        File before = getFileForDocId(sourceDocumentId);
        File after = new File(getFileForDocId(targetParentDocumentId), before.getName());
        File visibleFileBefore = getFileForDocId(sourceDocumentId, true);
        StringBuilder stringBuilder;
        if (after.exists()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Already exists ");
            stringBuilder.append(after);
            throw new IllegalStateException(stringBuilder.toString());
        } else if (before.renameTo(after)) {
            String docId = getDocIdForFile(after);
            onDocIdChanged(sourceDocumentId);
            onDocIdChanged(docId);
            moveInMediaStore(visibleFileBefore, getFileForDocId(docId, true));
            return docId;
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to move to ");
            stringBuilder.append(after);
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    private void moveInMediaStore(File oldVisibleFile, File newVisibleFile) {
        if (oldVisibleFile != null) {
            MediaStore.scanFile(getContext(), oldVisibleFile);
        }
        if (newVisibleFile != null) {
            MediaStore.scanFile(getContext(), newVisibleFile);
        }
    }

    public void deleteDocument(String docId) throws FileNotFoundException {
        File file = getFileForDocId(docId);
        File visibleFile = getFileForDocId(docId, true);
        boolean isDirectory = file.isDirectory();
        if (isDirectory) {
            FileUtils.deleteContents(file);
        }
        if (file.delete()) {
            onDocIdChanged(docId);
            removeFromMediaStore(visibleFile, isDirectory);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to delete ");
        stringBuilder.append(file);
        throw new IllegalStateException(stringBuilder.toString());
    }

    private void removeFromMediaStore(File visibleFile, boolean isFolder) throws FileNotFoundException {
        if (visibleFile != null) {
            long token = Binder.clearCallingIdentity();
            try {
                String path;
                ContentResolver resolver = getContext().getContentResolver();
                Uri externalUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
                if (isFolder) {
                    path = new StringBuilder();
                    path.append(visibleFile.getAbsolutePath());
                    path.append("/");
                    path = path.toString();
                    String[] strArr = new String[3];
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(path);
                    stringBuilder.append("%");
                    strArr[0] = stringBuilder.toString();
                    strArr[1] = Integer.toString(path.length());
                    strArr[2] = path;
                    resolver.delete(externalUri, "_data LIKE ?1 AND lower(substr(_data,1,?2))=lower(?3)", strArr);
                }
                path = visibleFile.getAbsolutePath();
                resolver.delete(externalUri, "_data LIKE ?1 AND lower(_data)=lower(?2)", new String[]{path, path});
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }
    }

    public Cursor queryDocument(String documentId, String[] projection) throws FileNotFoundException {
        MatrixCursor result = new MatrixCursor(resolveProjection(projection));
        includeFile(result, documentId, null);
        return result;
    }

    public Cursor queryChildDocuments(String parentDocumentId, String[] projection, String sortOrder) throws FileNotFoundException {
        File parent = getFileForDocId(parentDocumentId);
        MatrixCursor result = new DirectoryCursor(resolveProjection(projection), parentDocumentId, parent);
        if (parent.isDirectory()) {
            for (File file : FileUtils.listFilesOrEmpty(parent)) {
                includeFile(result, null, file);
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("parentDocumentId '");
            stringBuilder.append(parentDocumentId);
            stringBuilder.append("' is not Directory");
            Log.w(TAG, stringBuilder.toString());
        }
        return result;
    }

    /* Access modifiers changed, original: protected|final */
    public final Cursor querySearchDocuments(File folder, String[] projection, Set<String> exclusion, Bundle queryArgs) throws FileNotFoundException {
        MatrixCursor result = new MatrixCursor(resolveProjection(projection));
        LinkedList<File> pending = new LinkedList();
        pending.add(folder);
        while (!pending.isEmpty() && result.getCount() < 24) {
            File file = (File) pending.removeFirst();
            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    pending.add(child);
                }
            }
            if (!exclusion.contains(file.getAbsolutePath()) && matchSearchQueryArguments(file, queryArgs)) {
                includeFile(result, null, file);
            }
        }
        String[] handledQueryArgs = DocumentsContract.getHandledQueryArguments(queryArgs);
        if (handledQueryArgs.length > 0) {
            Bundle extras = new Bundle();
            extras.putStringArray(ContentResolver.EXTRA_HONORED_ARGS, handledQueryArgs);
            result.setExtras(extras);
        }
        return result;
    }

    public String getDocumentType(String documentId) throws FileNotFoundException {
        return getDocumentType(documentId, getFileForDocId(documentId));
    }

    private String getDocumentType(String documentId, File file) throws FileNotFoundException {
        if (file.isDirectory()) {
            return Document.MIME_TYPE_DIR;
        }
        int lastDot = documentId.lastIndexOf(46);
        if (lastDot >= 0) {
            String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(documentId.substring(lastDot + 1).toLowerCase());
            if (mime != null) {
                return mime;
            }
        }
        return ContentResolver.MIME_TYPE_DEFAULT;
    }

    public ParcelFileDescriptor openDocument(String documentId, String mode, CancellationSignal signal) throws FileNotFoundException {
        File file = getFileForDocId(documentId);
        File visibleFile = getFileForDocId(documentId, true);
        int pfdMode = ParcelFileDescriptor.parseMode(mode);
        if (pfdMode == 268435456 || visibleFile == null) {
            return ParcelFileDescriptor.open(file, pfdMode);
        }
        try {
            return ParcelFileDescriptor.open(file, pfdMode, this.mHandler, new -$$Lambda$FileSystemProvider$y9rjeYFpkvVjwD2Whw-ujCM-C7Y(this, documentId, visibleFile));
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to open for writing: ");
            stringBuilder.append(e);
            throw new FileNotFoundException(stringBuilder.toString());
        }
    }

    public /* synthetic */ void lambda$openDocument$0$FileSystemProvider(String documentId, File visibleFile, IOException e) {
        onDocIdChanged(documentId);
        scanFile(visibleFile);
    }

    private boolean matchSearchQueryArguments(File file, Bundle queryArgs) {
        if (file == null) {
            return false;
        }
        String fileMimeType;
        String fileName = file.getName();
        if (file.isDirectory()) {
            fileMimeType = Document.MIME_TYPE_DIR;
        } else {
            int dotPos = fileName.lastIndexOf(46);
            if (dotPos < 0) {
                return false;
            }
            fileMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileName.substring(dotPos + 1));
        }
        return DocumentsContract.matchSearchQueryArguments(queryArgs, fileName, fileMimeType, file.lastModified(), file.length());
    }

    private void scanFile(File visibleFile) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(visibleFile));
        getContext().sendBroadcast(intent);
    }

    public AssetFileDescriptor openDocumentThumbnail(String documentId, Point sizeHint, CancellationSignal signal) throws FileNotFoundException {
        return DocumentsContract.openImageThumbnail(getFileForDocId(documentId));
    }

    /* Access modifiers changed, original: protected */
    public RowBuilder includeFile(MatrixCursor result, String docId, File file) throws FileNotFoundException {
        Object docId2;
        int flags;
        String[] columns = result.getColumnNames();
        RowBuilder row = result.newRow();
        if (docId2 == null) {
            docId2 = getDocIdForFile(file);
        } else {
            file = getFileForDocId(docId2);
        }
        Object mimeType = getDocumentType(docId2, file);
        row.add("document_id", docId2);
        row.add("mime_type", mimeType);
        int flagIndex = ArrayUtils.indexOf(columns, "flags");
        if (flagIndex != -1) {
            flags = 0;
            if (file.canWrite()) {
                if (mimeType.equals(Document.MIME_TYPE_DIR)) {
                    flags = (((0 | 8) | 4) | 64) | 256;
                } else {
                    flags = (((0 | 2) | 4) | 64) | 256;
                }
            }
            if (mimeType.startsWith(MessagingMessage.IMAGE_MIME_TYPE_PREFIX)) {
                flags |= 1;
            }
            if (typeSupportsMetadata(mimeType)) {
                flags |= 16384;
            }
            row.add(flagIndex, Integer.valueOf(flags));
        }
        flags = ArrayUtils.indexOf(columns, "_display_name");
        if (flags != -1) {
            row.add(flags, file.getName());
        }
        int lastModifiedIndex = ArrayUtils.indexOf(columns, "last_modified");
        if (lastModifiedIndex != -1) {
            long lastModified = file.lastModified();
            if (lastModified > 31536000000L) {
                row.add(lastModifiedIndex, Long.valueOf(lastModified));
            }
        }
        int sizeIndex = ArrayUtils.indexOf(columns, "_size");
        if (sizeIndex != -1) {
            row.add(sizeIndex, Long.valueOf(file.length()));
        }
        return row;
    }

    /* Access modifiers changed, original: protected */
    public boolean typeSupportsMetadata(String mimeType) {
        return MetadataReader.isSupportedMimeType(mimeType) || Document.MIME_TYPE_DIR.equals(mimeType);
    }

    /* Access modifiers changed, original: protected|final */
    public final File getFileForDocId(String docId) throws FileNotFoundException {
        return getFileForDocId(docId, false);
    }

    private String[] resolveProjection(String[] projection) {
        return projection == null ? this.mDefaultProjection : projection;
    }

    private void startObserving(File file, Uri notifyUri, DirectoryCursor cursor) {
        synchronized (this.mObservers) {
            DirectoryObserver observer = (DirectoryObserver) this.mObservers.get(file);
            if (observer == null) {
                observer = new DirectoryObserver(file, getContext().getContentResolver(), notifyUri);
                observer.startWatching();
                this.mObservers.put(file, observer);
            }
            observer.mCursors.add(cursor);
        }
    }

    /* JADX WARNING: Missing block: B:11:0x0029, code skipped:
            return;
     */
    private void stopObserving(java.io.File r4, com.android.internal.content.FileSystemProvider.DirectoryCursor r5) {
        /*
        r3 = this;
        r0 = r3.mObservers;
        monitor-enter(r0);
        r1 = r3.mObservers;	 Catch:{ all -> 0x002a }
        r1 = r1.get(r4);	 Catch:{ all -> 0x002a }
        r1 = (com.android.internal.content.FileSystemProvider.DirectoryObserver) r1;	 Catch:{ all -> 0x002a }
        if (r1 != 0) goto L_0x000f;
    L_0x000d:
        monitor-exit(r0);	 Catch:{ all -> 0x002a }
        return;
    L_0x000f:
        r2 = r1.mCursors;	 Catch:{ all -> 0x002a }
        r2.remove(r5);	 Catch:{ all -> 0x002a }
        r2 = r1.mCursors;	 Catch:{ all -> 0x002a }
        r2 = r2.size();	 Catch:{ all -> 0x002a }
        if (r2 != 0) goto L_0x0028;
    L_0x0020:
        r2 = r3.mObservers;	 Catch:{ all -> 0x002a }
        r2.remove(r4);	 Catch:{ all -> 0x002a }
        r1.stopWatching();	 Catch:{ all -> 0x002a }
    L_0x0028:
        monitor-exit(r0);	 Catch:{ all -> 0x002a }
        return;
    L_0x002a:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x002a }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.content.FileSystemProvider.stopObserving(java.io.File, com.android.internal.content.FileSystemProvider$DirectoryCursor):void");
    }
}
