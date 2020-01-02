package miui.maml.util;

import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import miui.maml.ResourceLoader;

public class ZipResourceLoader extends ResourceLoader {
    private static final String LOG_TAG = "ZipResourceLoader";
    private String mInnerPath;
    private Object mLock;
    private String mResourcePath;
    private ZipFile mZipFile;

    public ZipResourceLoader(String zipPath) {
        this(zipPath, null, null);
    }

    public ZipResourceLoader(String zipPath, String innerPath) {
        this(zipPath, innerPath, null);
    }

    public ZipResourceLoader(String zipPath, String innerPath, String manifestName) {
        this.mLock = new Object();
        if (TextUtils.isEmpty(zipPath)) {
            throw new IllegalArgumentException("empty zip path");
        }
        this.mResourcePath = zipPath;
        this.mInnerPath = innerPath == null ? "" : innerPath;
        if (manifestName != null) {
            this.mManifestName = manifestName;
        }
        init();
    }

    public boolean resourceExists(String path) {
        boolean z = false;
        if (this.mZipFile == null || path == null) {
            return false;
        }
        synchronized (this.mLock) {
            if (this.mZipFile != null) {
                ZipFile zipFile = this.mZipFile;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mInnerPath);
                stringBuilder.append(path);
                if (zipFile.getEntry(stringBuilder.toString()) != null) {
                    z = true;
                }
            }
        }
        return z;
    }

    public InputStream getInputStream(String path, long[] size) {
        if (this.mZipFile == null || path == null) {
            return null;
        }
        synchronized (this.mLock) {
            if (this.mZipFile != null) {
                ZipEntry entry = this.mZipFile;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mInnerPath);
                stringBuilder.append(path);
                entry = entry.getEntry(stringBuilder.toString());
                if (entry == null) {
                    return null;
                }
                if (size != null) {
                    try {
                        size[0] = entry.getSize();
                    } catch (IOException e) {
                        Log.d(LOG_TAG, e.toString());
                        return null;
                    }
                }
                InputStream inputStream = this.mZipFile.getInputStream(entry);
                return inputStream;
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        close();
        super.finalize();
    }

    private void close() {
        synchronized (this.mLock) {
            if (this.mZipFile != null) {
                try {
                    this.mZipFile.close();
                } catch (IOException e) {
                }
                this.mZipFile = null;
            }
        }
    }

    public void init() {
        super.init();
        synchronized (this.mLock) {
            if (this.mZipFile == null) {
                try {
                    this.mZipFile = new ZipFile(this.mResourcePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    String str = LOG_TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("fail to init zip file: ");
                    stringBuilder.append(this.mResourcePath);
                    Log.e(str, stringBuilder.toString());
                }
            }
        }
    }

    public void finish() {
        close();
        super.finish();
    }
}
