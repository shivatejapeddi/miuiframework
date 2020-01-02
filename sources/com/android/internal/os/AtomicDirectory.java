package com.android.internal.os;

import android.os.FileUtils;
import android.util.ArrayMap;
import com.android.internal.util.Preconditions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public final class AtomicDirectory {
    private final File mBackupDirectory;
    private int mBackupDirectoryFd = -1;
    private final File mBaseDirectory;
    private int mBaseDirectoryFd = -1;
    private final ArrayMap<File, FileOutputStream> mOpenFiles = new ArrayMap();

    private static native void fsyncDirectoryFd(int i);

    private static native int getDirectoryFd(String str);

    public AtomicDirectory(File baseDirectory) {
        Preconditions.checkNotNull(baseDirectory, "baseDirectory cannot be null");
        this.mBaseDirectory = baseDirectory;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(baseDirectory.getPath());
        stringBuilder.append("_bak");
        this.mBackupDirectory = new File(stringBuilder.toString());
    }

    public File getBackupDirectory() {
        return this.mBackupDirectory;
    }

    public File startRead() throws IOException {
        restore();
        return getOrCreateBaseDirectory();
    }

    public void finishRead() {
        this.mBaseDirectoryFd = -1;
        this.mBackupDirectoryFd = -1;
    }

    public File startWrite() throws IOException {
        backup();
        return getOrCreateBaseDirectory();
    }

    public FileOutputStream openWrite(File file) throws IOException {
        if (file.isDirectory() || !file.getParentFile().equals(getOrCreateBaseDirectory())) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Must be a file in ");
            stringBuilder.append(getOrCreateBaseDirectory());
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        FileOutputStream destination = new FileOutputStream(file);
        if (this.mOpenFiles.put(file, destination) == null) {
            return destination;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Already open file");
        stringBuilder2.append(file.getCanonicalPath());
        throw new IllegalArgumentException(stringBuilder2.toString());
    }

    public void closeWrite(FileOutputStream destination) {
        if (this.mOpenFiles.removeAt(this.mOpenFiles.indexOfValue(destination)) != null) {
            FileUtils.sync(destination);
            try {
                destination.close();
                return;
            } catch (IOException e) {
                return;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown file stream ");
        stringBuilder.append(destination);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void failWrite(FileOutputStream destination) {
        int indexOfValue = this.mOpenFiles.indexOfValue(destination);
        if (indexOfValue >= 0) {
            this.mOpenFiles.removeAt(indexOfValue);
        }
    }

    public void finishWrite() {
        throwIfSomeFilesOpen();
        fsyncDirectoryFd(this.mBaseDirectoryFd);
        deleteDirectory(this.mBackupDirectory);
        fsyncDirectoryFd(this.mBackupDirectoryFd);
        this.mBaseDirectoryFd = -1;
        this.mBackupDirectoryFd = -1;
    }

    public void failWrite() {
        throwIfSomeFilesOpen();
        try {
            restore();
        } catch (IOException e) {
        }
        this.mBaseDirectoryFd = -1;
        this.mBackupDirectoryFd = -1;
    }

    public boolean exists() {
        return this.mBaseDirectory.exists() || this.mBackupDirectory.exists();
    }

    public void delete() {
        if (this.mBaseDirectory.exists()) {
            deleteDirectory(this.mBaseDirectory);
            fsyncDirectoryFd(this.mBaseDirectoryFd);
        }
        if (this.mBackupDirectory.exists()) {
            deleteDirectory(this.mBackupDirectory);
            fsyncDirectoryFd(this.mBackupDirectoryFd);
        }
    }

    private File getOrCreateBaseDirectory() throws IOException {
        if (!this.mBaseDirectory.exists()) {
            if (this.mBaseDirectory.mkdirs()) {
                FileUtils.setPermissions(this.mBaseDirectory.getPath(), 505, -1, -1);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Couldn't create directory ");
                stringBuilder.append(this.mBaseDirectory);
                throw new IOException(stringBuilder.toString());
            }
        }
        if (this.mBaseDirectoryFd < 0) {
            this.mBaseDirectoryFd = getDirectoryFd(this.mBaseDirectory.getCanonicalPath());
        }
        return this.mBaseDirectory;
    }

    private void throwIfSomeFilesOpen() {
        if (!this.mOpenFiles.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unclosed files: ");
            stringBuilder.append(Arrays.toString(this.mOpenFiles.keySet().toArray()));
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    private void backup() throws IOException {
        if (this.mBaseDirectory.exists()) {
            if (this.mBaseDirectoryFd < 0) {
                this.mBaseDirectoryFd = getDirectoryFd(this.mBaseDirectory.getCanonicalPath());
            }
            if (this.mBackupDirectory.exists()) {
                deleteDirectory(this.mBackupDirectory);
            }
            if (this.mBaseDirectory.renameTo(this.mBackupDirectory)) {
                this.mBackupDirectoryFd = this.mBaseDirectoryFd;
                this.mBaseDirectoryFd = -1;
                fsyncDirectoryFd(this.mBackupDirectoryFd);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Couldn't backup ");
            stringBuilder.append(this.mBaseDirectory);
            stringBuilder.append(" to ");
            stringBuilder.append(this.mBackupDirectory);
            throw new IOException(stringBuilder.toString());
        }
    }

    private void restore() throws IOException {
        if (this.mBackupDirectory.exists()) {
            if (this.mBackupDirectoryFd == -1) {
                this.mBackupDirectoryFd = getDirectoryFd(this.mBackupDirectory.getCanonicalPath());
            }
            if (this.mBaseDirectory.exists()) {
                deleteDirectory(this.mBaseDirectory);
            }
            if (this.mBackupDirectory.renameTo(this.mBaseDirectory)) {
                this.mBaseDirectoryFd = this.mBackupDirectoryFd;
                this.mBackupDirectoryFd = -1;
                fsyncDirectoryFd(this.mBaseDirectoryFd);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Couldn't restore ");
            stringBuilder.append(this.mBackupDirectory);
            stringBuilder.append(" to ");
            stringBuilder.append(this.mBaseDirectory);
            throw new IOException(stringBuilder.toString());
        }
    }

    private static void deleteDirectory(File file) {
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                deleteDirectory(child);
            }
        }
        file.delete();
    }
}
