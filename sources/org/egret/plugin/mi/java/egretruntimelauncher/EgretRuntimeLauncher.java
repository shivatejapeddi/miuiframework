package org.egret.plugin.mi.java.egretruntimelauncher;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.egret.plugin.mi.android.util.launcher.ExecutorLab;
import org.egret.plugin.mi.android.util.launcher.FileUtil;
import org.egret.plugin.mi.android.util.launcher.Md5Util;
import org.egret.plugin.mi.android.util.launcher.NetClass;
import org.egret.plugin.mi.android.util.launcher.NetClass.OnNetListener;
import org.egret.plugin.mi.android.util.launcher.ZipClass;
import org.egret.plugin.mi.java.egretruntimelauncher.EgretRuntimeLibrary.OnDownloadListener;

public class EgretRuntimeLauncher {
    public static int DEBUG_RUNTIME_DOWNLOAD = 0;
    public static final String EGRET_JSON = "egret.json";
    public static final String EGRET_ROOT = "egret";
    private static final String EGRET_RUNTIME_CACHE_ROOT = "update";
    public static final String EGRET_RUNTIME_SD_ROOT = "egret/runtime";
    private static final String EGRET_RUNTIME_VERSION_URL = "http://runtime.egret-labs.org/runtime.php";
    private static final String TAG = "EgretRuntimeLauncher";
    private File cacheRoot;
    private int downLoadSum;
    private EgretRuntimeDownloadListener downloadListener;
    private ArrayList<EgretRuntimeLibrary> downloaderList;
    private int fileSizeSum;
    private File libraryRoot;
    private Handler mainHandler;
    private ConcurrentHashMap<String, Integer> mapFileSize;
    private EgretRuntimeVersion runtimeVersion;
    private String runtimeVersionUrl;
    private File sdRoot;
    protected int updatedNumber;
    private String urlData;

    public interface EgretRuntimeDownloadListener {
        void onError(String str);

        void onProgress(String str, int i, int i2);

        void onProgressTotal(int i, int i2);

        void onSuccess(Class<?> cls);
    }

    public class GameEngineMethod {
        public static final String CALL_EGRET_INTERFACE = "callEgretInterface";
        public static final String ENABLE_EGRET_RUNTIME_INTERFACE = "enableEgretRuntimeInterface";
        public static final String GAME_ENGINE_GET_VIEW = "game_engine_get_view";
        public static final String GAME_ENGINE_INIT = "game_engine_init";
        public static final String GAME_ENGINE_ON_PAUSE = "game_engine_onPause";
        public static final String GAME_ENGINE_ON_RESUME = "game_engine_onResume";
        public static final String GAME_ENGINE_ON_STOP = "game_engine_onStop";
        public static final String GAME_ENGINE_SET_LOADING_VIEW = "game_engine_set_loading_view";
        public static final String GAME_ENGINE_SET_OPTIONS = "game_engine_set_options";
        public static final String SET_RUNTIME_INTERFACE_SET = "setRuntimeInterfaceSet";
    }

    public EgretRuntimeLauncher(Context context, String libraryRoot) {
        this.mapFileSize = new ConcurrentHashMap();
        this.runtimeVersion = new EgretRuntimeVersion();
        this.downloaderList = new ArrayList();
        this.mainHandler = new Handler(context.getMainLooper());
        this.runtimeVersionUrl = EGRET_RUNTIME_VERSION_URL;
        this.libraryRoot = libraryRoot != null ? new File(libraryRoot) : null;
        this.cacheRoot = new File(libraryRoot, EGRET_RUNTIME_CACHE_ROOT);
        this.sdRoot = getSdRoot();
        this.cacheRoot.mkdirs();
    }

    public EgretRuntimeLauncher(Context context, String libraryRoot, String appId, String appKey, int devVersion) {
        this(context, libraryRoot);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?appId=");
        stringBuilder.append(appId);
        stringBuilder.append("&appKey=");
        stringBuilder.append(appKey);
        this.urlData = stringBuilder.toString();
        if (devVersion > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.urlData);
            stringBuilder.append("&dev=");
            stringBuilder.append(devVersion);
            this.urlData = stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.runtimeVersionUrl);
        stringBuilder.append(this.urlData);
        this.runtimeVersionUrl = stringBuilder.toString();
    }

    private File getSdRoot() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File runtimeRootInExternalStorage = new File(Environment.getExternalStorageDirectory(), EGRET_RUNTIME_SD_ROOT);
            if (runtimeRootInExternalStorage.exists() || runtimeRootInExternalStorage.mkdirs()) {
                return runtimeRootInExternalStorage;
            }
        }
        return null;
    }

    public void setRuntimeVersionUrl(String url) {
        if (this.urlData == null) {
            this.runtimeVersionUrl = url;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url);
        stringBuilder.append(this.urlData);
        this.runtimeVersionUrl = stringBuilder.toString();
    }

    public void run(EgretRuntimeDownloadListener listener) {
        String str = this.runtimeVersionUrl;
        String str2 = TAG;
        if (str == null || this.libraryRoot == null || listener == null) {
            str = "library root, url or listener may be null";
            Log.e(str2, str);
            listener.onError(str);
            ExecutorLab.releaseInstance();
            return;
        }
        Log.d(str2, "run");
        this.downloadListener = listener;
        fetchRemoteVersion();
    }

    private void fetchRemoteVersion() {
        ExecutorLab.getInstance().addTask(new Thread(new Runnable() {
            public void run() {
                new NetClass().getRequest(EgretRuntimeLauncher.this.runtimeVersionUrl, new OnNetListener() {
                    public void onSuccess(String content) {
                        if (content == null) {
                            EgretRuntimeLauncher.this.handleError("response content is null");
                        } else {
                            EgretRuntimeLauncher.this.parseRuntimeVersion(content);
                        }
                    }

                    public void onProgress(int progress, int length) {
                    }

                    public void onError(String message) {
                        EgretRuntimeLauncher.this.handleError(message);
                    }
                });
            }
        }));
    }

    private void parseRuntimeVersion(String content) {
        this.runtimeVersion.fromString(content);
        FileUtil.writeFile(new File(this.libraryRoot, EGRET_JSON), content);
        updateLibrary();
    }

    private synchronized void updateDownLoadSum() {
        this.downLoadSum = 0;
        for (Entry<String, Integer> entry : this.mapFileSize.entrySet()) {
            String key = (String) entry.getKey();
            Integer value = (Integer) entry.getValue();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("rt zipUrl progress key = ");
            stringBuilder.append(key);
            stringBuilder.append(" value = ");
            stringBuilder.append(value);
            Log.d("", stringBuilder.toString());
            this.downLoadSum += value.intValue();
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("rt zipUrl progress downLoadSum = ");
        stringBuilder2.append(this.downLoadSum);
        Log.d("", stringBuilder2.toString());
    }

    private int getFileSize(String fileUrl) {
        int fileSize = 0;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(fileUrl).openConnection();
            fileSize = conn.getContentLength();
            conn.disconnect();
            return fileSize;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return fileSize;
        } catch (IOException e2) {
            e2.printStackTrace();
            return fileSize;
        }
    }

    private void updateLibrary() {
        this.updatedNumber = 0;
        ArrayList<Library> libraryList = getNeedUpdateLibraryList();
        if (libraryList.size() == 0) {
            updated();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("rt libraryList size: ");
        stringBuilder.append(String.valueOf(libraryList.size()));
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        int fileSizeTemp = 0;
        Iterator it = libraryList.iterator();
        while (it.hasNext()) {
            fileSizeTemp += getFileSize(((Library) it.next()).getUrl());
        }
        this.fileSizeSum = fileSizeTemp;
        it = libraryList.iterator();
        while (it.hasNext()) {
            final Library library = (Library) it.next();
            EgretRuntimeLibrary downloader = new EgretRuntimeLibrary(library, this.libraryRoot, this.cacheRoot, this.sdRoot);
            downloader.download(new OnDownloadListener() {
                public void onSuccess() {
                    EgretRuntimeLauncher egretRuntimeLauncher = EgretRuntimeLauncher.this;
                    egretRuntimeLauncher.updatedNumber++;
                    EgretRuntimeLauncher.this.updated();
                }

                public void onProgress(int progress, int length) {
                    EgretRuntimeLauncher.this.mapFileSize.put(library.getZipName(), Integer.valueOf(progress));
                    EgretRuntimeLauncher.this.updateDownLoadSum();
                    EgretRuntimeLauncher.this.downloadListener.onProgressTotal(EgretRuntimeLauncher.this.downLoadSum, EgretRuntimeLauncher.this.fileSizeSum);
                }

                public void onError(String message) {
                    EgretRuntimeLauncher egretRuntimeLauncher = EgretRuntimeLauncher.this;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Fail to download file: ");
                    stringBuilder.append(library.getZipName());
                    stringBuilder.append(" detail: ");
                    stringBuilder.append(message);
                    egretRuntimeLauncher.handleError(stringBuilder.toString());
                    ExecutorLab.releaseInstance();
                }
            });
            this.downloaderList.add(downloader);
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("addTask: ");
            stringBuilder3.append(library.getZipName());
            Log.d(str, stringBuilder3.toString());
            ExecutorLab.getInstance().addTask(downloader);
        }
    }

    private ArrayList<Library> getNeedUpdateLibraryList() {
        ArrayList<Library> result = new ArrayList();
        Iterator it = this.runtimeVersion.getLibraryList().iterator();
        while (it.hasNext()) {
            Library library = (Library) it.next();
            if (!(checkLocal(library) || checkCache(library))) {
                if (!checkSd(library)) {
                    result.add(library);
                }
            }
        }
        return result;
    }

    private boolean checkLocal(Library library) {
        return isLatest(new File(this.libraryRoot, library.getLibraryName()), library.getLibraryCheckSum());
    }

    private boolean isLatest(File file, String checkSum) {
        if (DEBUG_RUNTIME_DOWNLOAD > 0 || !file.exists()) {
            return false;
        }
        if (Md5Util.checkMd5(file, checkSum)) {
            return true;
        }
        if (!file.delete()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fail to delete file: ");
            stringBuilder.append(file.getAbsolutePath());
            handleError(stringBuilder.toString());
            ExecutorLab.releaseInstance();
        }
        return false;
    }

    private boolean checkZipInRoot(Library library, File root) {
        return isLatest(new File(root, library.getZipName()), library.getZipCheckSum());
    }

    private boolean checkCache(Library library) {
        if (!checkZipInRoot(library, this.cacheRoot)) {
            return false;
        }
        File cacheZip = new File(this.cacheRoot, library.getZipName());
        boolean unzip = new ZipClass().unzip(cacheZip, this.libraryRoot);
        String str = TAG;
        StringBuilder stringBuilder;
        if (!unzip) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("fail to unzip ");
            stringBuilder.append(cacheZip.getAbsolutePath());
            Log.e(str, stringBuilder.toString());
            return false;
        } else if (cacheZip.delete()) {
            return true;
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("fail to delete ");
            stringBuilder.append(cacheZip.getAbsolutePath());
            Log.e(str, stringBuilder.toString());
            return false;
        }
    }

    private boolean checkSd(Library library) {
        if (!checkZipInRoot(library, this.sdRoot)) {
            return false;
        }
        if (FileUtil.Copy(new File(this.sdRoot, library.getZipName()), new File(this.cacheRoot, library.getZipName()))) {
            return checkCache(library);
        }
        return false;
    }

    private void updated() {
        if (this.downloaderList.size() <= 0 || this.updatedNumber == this.downloaderList.size()) {
            loadLibrary();
        }
    }

    private void loadLibrary() {
        if (!EgretRuntimeLoader.get().isLoaded()) {
            Iterator it = this.runtimeVersion.getLibraryList().iterator();
            while (it.hasNext()) {
                EgretRuntimeLoader.get().load(new File(this.libraryRoot, ((Library) it.next()).getLibraryName()).getAbsolutePath());
            }
        }
        notifyLoadHandler();
    }

    private void handleError(String message) {
        String content = FileUtil.readFile(new File(this.libraryRoot, EGRET_JSON));
        if (content == null) {
            this.downloadListener.onError(message);
            ExecutorLab.releaseInstance();
            return;
        }
        this.runtimeVersion.fromString(content);
        ArrayList<Library> libraries = this.runtimeVersion.getLibraryList();
        if (libraries != null) {
            Iterator it = libraries.iterator();
            while (it.hasNext()) {
                Library library = (Library) it.next();
                if (!checkLocal(library)) {
                    this.downloadListener.onError(message);
                    ExecutorLab.releaseInstance();
                    return;
                } else if (!EgretRuntimeLoader.get().isLoaded()) {
                    EgretRuntimeLoader.get().load(new File(this.libraryRoot, library.getLibraryName()).getAbsolutePath());
                }
            }
            notifyLoadHandler();
        }
    }

    private void notifyLoadHandler() {
        this.mainHandler.post(new Runnable() {
            public void run() {
                Class<?> gameEngineClass = EgretRuntimeLoader.get().getEgretGameEngineClass();
                if (gameEngineClass == null) {
                    EgretRuntimeLauncher.this.downloadListener.onError("fails to new game engine");
                    ExecutorLab.releaseInstance();
                    return;
                }
                EgretRuntimeLauncher.this.downloadListener.onSuccess(gameEngineClass);
            }
        });
    }

    public void stop() {
        for (int i = 0; i < this.downloaderList.size(); i++) {
            ((EgretRuntimeLibrary) this.downloaderList.get(i)).stop();
        }
        ExecutorLab.releaseInstance();
    }
}
