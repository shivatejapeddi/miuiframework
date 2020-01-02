package com.android.internal.util;

import android.os.FileUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import libcore.io.IoUtils;
import org.apache.miui.commons.lang3.ClassUtils;

public class FileRotator {
    private static final boolean LOGD = false;
    private static final String SUFFIX_BACKUP = ".backup";
    private static final String SUFFIX_NO_BACKUP = ".no_backup";
    private static final String TAG = "FileRotator";
    private final File mBasePath;
    private final long mDeleteAgeMillis;
    private final String mPrefix;
    private final long mRotateAgeMillis;

    public interface Reader {
        void read(InputStream inputStream) throws IOException;
    }

    public interface Writer {
        void write(OutputStream outputStream) throws IOException;
    }

    public interface Rewriter extends Reader, Writer {
        void reset();

        boolean shouldWrite();
    }

    private static class FileInfo {
        public long endMillis;
        public final String prefix;
        public long startMillis;

        public FileInfo(String prefix) {
            this.prefix = (String) Preconditions.checkNotNull(prefix);
        }

        /* JADX WARNING: Missing block: B:15:0x0051, code skipped:
            return false;
     */
        public boolean parse(java.lang.String r8) {
            /*
            r7 = this;
            r0 = -1;
            r7.endMillis = r0;
            r7.startMillis = r0;
            r0 = 46;
            r0 = r8.lastIndexOf(r0);
            r1 = 45;
            r1 = r8.lastIndexOf(r1);
            r2 = -1;
            r3 = 0;
            if (r0 == r2) goto L_0x0051;
        L_0x0016:
            if (r1 != r2) goto L_0x0019;
        L_0x0018:
            goto L_0x0051;
        L_0x0019:
            r2 = r7.prefix;
            r4 = r8.substring(r3, r0);
            r2 = r2.equals(r4);
            if (r2 != 0) goto L_0x0026;
        L_0x0025:
            return r3;
        L_0x0026:
            r2 = r0 + 1;
            r2 = r8.substring(r2, r1);	 Catch:{ NumberFormatException -> 0x004f }
            r4 = java.lang.Long.parseLong(r2);	 Catch:{ NumberFormatException -> 0x004f }
            r7.startMillis = r4;	 Catch:{ NumberFormatException -> 0x004f }
            r2 = r8.length();	 Catch:{ NumberFormatException -> 0x004f }
            r2 = r2 - r1;
            r4 = 1;
            if (r2 != r4) goto L_0x0042;
        L_0x003a:
            r5 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r7.endMillis = r5;	 Catch:{ NumberFormatException -> 0x004f }
            goto L_0x004e;
        L_0x0042:
            r2 = r1 + 1;
            r2 = r8.substring(r2);	 Catch:{ NumberFormatException -> 0x004f }
            r5 = java.lang.Long.parseLong(r2);	 Catch:{ NumberFormatException -> 0x004f }
            r7.endMillis = r5;	 Catch:{ NumberFormatException -> 0x004f }
        L_0x004e:
            return r4;
        L_0x004f:
            r2 = move-exception;
            return r3;
        L_0x0051:
            return r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.util.FileRotator$FileInfo.parse(java.lang.String):boolean");
        }

        public String build() {
            StringBuilder name = new StringBuilder();
            name.append(this.prefix);
            name.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            name.append(this.startMillis);
            name.append('-');
            long j = this.endMillis;
            if (j != Long.MAX_VALUE) {
                name.append(j);
            }
            return name.toString();
        }

        public boolean isActive() {
            return this.endMillis == Long.MAX_VALUE;
        }
    }

    public FileRotator(File basePath, String prefix, long rotateAgeMillis, long deleteAgeMillis) {
        this.mBasePath = (File) Preconditions.checkNotNull(basePath);
        this.mPrefix = (String) Preconditions.checkNotNull(prefix);
        this.mRotateAgeMillis = rotateAgeMillis;
        this.mDeleteAgeMillis = deleteAgeMillis;
        this.mBasePath.mkdirs();
        for (String name : this.mBasePath.list()) {
            if (name.startsWith(this.mPrefix)) {
                String str = SUFFIX_BACKUP;
                if (name.endsWith(str)) {
                    new File(this.mBasePath, name).renameTo(new File(this.mBasePath, name.substring(0, name.length() - str.length())));
                } else {
                    str = SUFFIX_NO_BACKUP;
                    if (name.endsWith(str)) {
                        File noBackupFile = new File(this.mBasePath, name);
                        File file = new File(this.mBasePath, name.substring(0, name.length() - str.length()));
                        noBackupFile.delete();
                        file.delete();
                    }
                }
            }
        }
    }

    public void deleteAll() {
        FileInfo info = new FileInfo(this.mPrefix);
        for (String name : this.mBasePath.list()) {
            if (info.parse(name)) {
                new File(this.mBasePath, name).delete();
            }
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: EliminatePhiNodes
        jadx.core.utils.exceptions.JadxRuntimeException: Assign predecessor not found for B:6:0x001e from B:24:?
        	at jadx.core.dex.visitors.ssa.EliminatePhiNodes.replaceMerge(EliminatePhiNodes.java:102)
        	at jadx.core.dex.visitors.ssa.EliminatePhiNodes.replaceMergeInstructions(EliminatePhiNodes.java:68)
        	at jadx.core.dex.visitors.ssa.EliminatePhiNodes.visit(EliminatePhiNodes.java:31)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        */
    public void dumpAll(java.io.OutputStream r10) throws java.io.IOException {
        /*
        r9 = this;
        r0 = new java.util.zip.ZipOutputStream;
        r0.<init>(r10);
        r1 = new com.android.internal.util.FileRotator$FileInfo;	 Catch:{ all -> 0x004a }
        r2 = r9.mPrefix;	 Catch:{ all -> 0x004a }
        r1.<init>(r2);	 Catch:{ all -> 0x004a }
        r2 = r9.mBasePath;	 Catch:{ all -> 0x004a }
        r2 = r2.list();	 Catch:{ all -> 0x004a }
        r3 = r2.length;	 Catch:{ all -> 0x004a }
        r4 = 0;	 Catch:{ all -> 0x004a }
    L_0x0014:
        if (r4 >= r3) goto L_0x0045;	 Catch:{ all -> 0x004a }
    L_0x0016:
        r5 = r2[r4];	 Catch:{ all -> 0x004a }
        r6 = r1.parse(r5);	 Catch:{ all -> 0x004a }
        if (r6 == 0) goto L_0x0042;	 Catch:{ all -> 0x004a }
    L_0x001e:
        r6 = new java.util.zip.ZipEntry;	 Catch:{ all -> 0x004a }
        r6.<init>(r5);	 Catch:{ all -> 0x004a }
        r0.putNextEntry(r6);	 Catch:{ all -> 0x004a }
        r7 = new java.io.File;	 Catch:{ all -> 0x004a }
        r8 = r9.mBasePath;	 Catch:{ all -> 0x004a }
        r7.<init>(r8, r5);	 Catch:{ all -> 0x004a }
        r8 = new java.io.FileInputStream;	 Catch:{ all -> 0x004a }
        r8.<init>(r7);	 Catch:{ all -> 0x004a }
        android.os.FileUtils.copy(r8, r0);	 Catch:{ all -> 0x003d }
        libcore.io.IoUtils.closeQuietly(r8);	 Catch:{ all -> 0x004a }
        r0.closeEntry();	 Catch:{ all -> 0x004a }
        goto L_0x0042;	 Catch:{ all -> 0x004a }
    L_0x003d:
        r2 = move-exception;	 Catch:{ all -> 0x004a }
        libcore.io.IoUtils.closeQuietly(r8);	 Catch:{ all -> 0x004a }
        throw r2;	 Catch:{ all -> 0x004a }
    L_0x0042:
        r4 = r4 + 1;
        goto L_0x0014;
    L_0x0045:
        libcore.io.IoUtils.closeQuietly(r0);
        return;
    L_0x004a:
        r1 = move-exception;
        libcore.io.IoUtils.closeQuietly(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.util.FileRotator.dumpAll(java.io.OutputStream):void");
    }

    public void rewriteActive(Rewriter rewriter, long currentTimeMillis) throws IOException {
        rewriteSingle(rewriter, getActiveName(currentTimeMillis));
    }

    @Deprecated
    public void combineActive(final Reader reader, final Writer writer, long currentTimeMillis) throws IOException {
        rewriteActive(new Rewriter() {
            public void reset() {
            }

            public void read(InputStream in) throws IOException {
                reader.read(in);
            }

            public boolean shouldWrite() {
                return true;
            }

            public void write(OutputStream out) throws IOException {
                writer.write(out);
            }
        }, currentTimeMillis);
    }

    public void rewriteAll(Rewriter rewriter) throws IOException {
        FileInfo info = new FileInfo(this.mPrefix);
        for (String name : this.mBasePath.list()) {
            if (info.parse(name)) {
                rewriteSingle(rewriter, name);
            }
        }
    }

    private void rewriteSingle(Rewriter rewriter, String name) throws IOException {
        IOException rethrowAsIoException;
        File file = new File(this.mBasePath, name);
        rewriter.reset();
        File file2;
        StringBuilder stringBuilder;
        File backupFile;
        if (file.exists()) {
            readFile(file, rewriter);
            if (rewriter.shouldWrite()) {
                file2 = this.mBasePath;
                stringBuilder = new StringBuilder();
                stringBuilder.append(name);
                stringBuilder.append(SUFFIX_BACKUP);
                backupFile = new File(file2, stringBuilder.toString());
                file.renameTo(backupFile);
                try {
                    writeFile(file, rewriter);
                    backupFile.delete();
                } catch (Throwable t) {
                    file.delete();
                    backupFile.renameTo(file);
                    rethrowAsIoException = rethrowAsIoException(t);
                }
            } else {
                return;
            }
        }
        file2 = this.mBasePath;
        stringBuilder = new StringBuilder();
        stringBuilder.append(name);
        stringBuilder.append(SUFFIX_NO_BACKUP);
        backupFile = new File(file2, stringBuilder.toString());
        backupFile.createNewFile();
        try {
            writeFile(file, rewriter);
            backupFile.delete();
        } catch (Throwable t2) {
            file.delete();
            backupFile.delete();
            rethrowAsIoException = rethrowAsIoException(t2);
        }
    }

    public void readMatching(Reader reader, long matchStartMillis, long matchEndMillis) throws IOException {
        FileInfo info = new FileInfo(this.mPrefix);
        for (String name : this.mBasePath.list()) {
            if (info.parse(name) && info.startMillis <= matchEndMillis && matchStartMillis <= info.endMillis) {
                readFile(new File(this.mBasePath, name), reader);
            }
        }
    }

    private String getActiveName(long currentTimeMillis) {
        String oldestActiveName = null;
        long oldestActiveStart = Long.MAX_VALUE;
        FileInfo info = new FileInfo(this.mPrefix);
        String[] baseFiles = this.mBasePath.list();
        if (baseFiles == null) {
            info.startMillis = currentTimeMillis;
            info.endMillis = Long.MAX_VALUE;
            return info.build();
        }
        for (String name : baseFiles) {
            if (info.parse(name) && info.isActive() && info.startMillis < currentTimeMillis && info.startMillis < oldestActiveStart) {
                oldestActiveName = name;
                oldestActiveStart = info.startMillis;
            }
        }
        if (oldestActiveName != null) {
            return oldestActiveName;
        }
        info.startMillis = currentTimeMillis;
        info.endMillis = Long.MAX_VALUE;
        return info.build();
    }

    public void maybeRotate(long currentTimeMillis) {
        long rotateBefore = currentTimeMillis - this.mRotateAgeMillis;
        long deleteBefore = currentTimeMillis - this.mDeleteAgeMillis;
        FileInfo info = new FileInfo(this.mPrefix);
        String[] baseFiles = this.mBasePath.list();
        if (baseFiles != null) {
            for (String name : baseFiles) {
                if (info.parse(name)) {
                    if (info.isActive()) {
                        if (info.startMillis <= rotateBefore) {
                            info.endMillis = currentTimeMillis;
                            new File(this.mBasePath, name).renameTo(new File(this.mBasePath, info.build()));
                        }
                    } else if (info.endMillis <= deleteBefore) {
                        new File(this.mBasePath, name).delete();
                    }
                }
            }
        }
    }

    private static void readFile(File file, Reader reader) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        try {
            reader.read(bis);
        } finally {
            IoUtils.closeQuietly(bis);
        }
    }

    private static void writeFile(File file, Writer writer) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        try {
            writer.write(bos);
            bos.flush();
        } finally {
            FileUtils.sync(fos);
            IoUtils.closeQuietly(bos);
        }
    }

    private static IOException rethrowAsIoException(Throwable t) throws IOException {
        if (t instanceof IOException) {
            throw ((IOException) t);
        }
        throw new IOException(t.getMessage(), t);
    }
}
