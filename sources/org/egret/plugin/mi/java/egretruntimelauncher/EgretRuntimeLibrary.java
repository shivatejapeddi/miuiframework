package org.egret.plugin.mi.java.egretruntimelauncher;

import android.util.Log;
import java.io.File;
import org.egret.plugin.mi.android.util.launcher.FileUtil;
import org.egret.plugin.mi.android.util.launcher.Md5Util;
import org.egret.plugin.mi.android.util.launcher.NetClass;
import org.egret.plugin.mi.android.util.launcher.NetClass.OnNetListener;
import org.egret.plugin.mi.android.util.launcher.ZipClass;
import org.egret.plugin.mi.android.util.launcher.ZipClass.OnZipListener;

public class EgretRuntimeLibrary implements Runnable {
    protected static final String TAG = "EgretRuntimeLibrary";
    private File cacheRoot;
    private OnDownloadListener downloadListener;
    private volatile boolean isCancelling;
    private Library library;
    private File root;
    private File sdRoot;

    public interface OnDownloadListener {
        void onError(String str);

        void onProgress(int i, int i2);

        void onSuccess();
    }

    public EgretRuntimeLibrary(Library library, File root, File cacheRoot, File sdRoot) {
        this.library = library;
        this.root = root;
        this.cacheRoot = cacheRoot;
        this.sdRoot = sdRoot;
    }

    public void download(OnDownloadListener listener) {
        if (this.library == null || this.root == null || this.cacheRoot == null || listener == null) {
            listener.onError("libray, root, cacheRoot, listener may be null");
        } else {
            this.downloadListener = listener;
        }
    }

    public void run() {
        this.isCancelling = false;
        doDownload();
    }

    private void doDownload() {
        File targetRoot = this.sdRoot;
        if (targetRoot == null) {
            targetRoot = this.cacheRoot;
        }
        new NetClass().writeResponseToFile(this.library.getUrl(), new File(targetRoot, this.library.getZipName()), new OnNetListener() {
            public void onSuccess(String arg0) {
                if (!EgretRuntimeLibrary.this.isCancelling && EgretRuntimeLibrary.this.doMove()) {
                    EgretRuntimeLibrary.this.doUnzip();
                }
            }

            public void onProgress(int progress, int length) {
                EgretRuntimeLibrary.this.downloadListener.onProgress(progress, length);
            }

            public void onError(String message) {
                EgretRuntimeLibrary.this.downloadListener.onError(message);
            }
        });
    }

    private boolean doMove() {
        if (this.isCancelling) {
            this.downloadListener.onError("thread is cancelling");
            return false;
        }
        File file = this.sdRoot;
        if (file == null || FileUtil.Copy(new File(file, this.library.getZipName()), new File(this.cacheRoot, this.library.getZipName()))) {
            return true;
        }
        this.downloadListener.onError("copy file error");
        return false;
    }

    private void doUnzip() {
        if (this.isCancelling) {
            this.downloadListener.onError("thread is cancelling");
        }
        final File cache = new File(this.cacheRoot, this.library.getZipName());
        final File target = new File(this.root, this.library.getLibraryName());
        if (!Md5Util.checkMd5(cache, this.library.getZipCheckSum())) {
            this.downloadListener.onError("cache file md5 error");
        }
        new ZipClass().unzip(cache, this.root, new OnZipListener() {
            public void onSuccess() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Success to unzip file: ");
                stringBuilder.append(cache.getAbsolutePath());
                String stringBuilder2 = stringBuilder.toString();
                String str = EgretRuntimeLibrary.TAG;
                Log.i(str, stringBuilder2);
                if (!cache.delete()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Fail to delete file: ");
                    stringBuilder.append(cache.getAbsolutePath());
                    Log.e(str, stringBuilder.toString());
                }
                if (Md5Util.checkMd5(target, EgretRuntimeLibrary.this.library.getLibraryCheckSum())) {
                    EgretRuntimeLibrary.this.downloadListener.onSuccess();
                } else {
                    EgretRuntimeLibrary.this.downloadListener.onError("target file md5 error");
                }
            }

            public void onProgress(int arg0, int arg1) {
            }

            public void onFileProgress(int arg0, int arg1) {
            }

            public void onError(String arg0) {
                OnDownloadListener access$300 = EgretRuntimeLibrary.this.downloadListener;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("fail to unzip file: ");
                stringBuilder.append(cache.getAbsolutePath());
                access$300.onError(stringBuilder.toString());
            }
        });
    }

    public void stop() {
        this.isCancelling = true;
    }
}
