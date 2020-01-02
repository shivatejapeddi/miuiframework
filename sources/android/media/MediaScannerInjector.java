package android.media;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import miui.os.Environment;
import miui.os.FileUtils;

public class MediaScannerInjector {
    private static final String DEBUG_LOG_PATH;
    private static final int MEDIA_META_APPROXIMATION = 4194304;
    private static final int MSG_PROCESSFILE_TIMEOUT = 1;
    private static final String[] NOMEDIA_PATH_WHITE_LIST = new String[]{"", "/Alarms", "/DCIM", "/DCIM/Camera", "/DCIM/Screenshots", "/Download", "/Movies", "/Music", "/Pictures", "/Podcasts", "/Ringtones", "/Notifications"};
    private static final int PROCESSFILE_TIMEOUT = 60000;
    private static final String SECURITY_CENTER = "com.miui.securitycenter";
    private static final String TAG = "MediaScannerInjector";
    private static HashSet<String> mNoMediaWhiteList = new HashSet();
    private static Context sContext;
    private static Handler sHandler;
    private static HandlerThread sHandlerThread;
    private static String sProcessName;

    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageMiuiDirectory().getAbsolutePath());
        stringBuilder.append("/debug_log/common/android.process.media/");
        DEBUG_LOG_PATH = stringBuilder.toString();
    }

    static boolean keepMimeType(String oldMimeType, String newMimeType) {
        boolean newTypeIsAudio = newMimeType == null || newMimeType.startsWith("audio");
        if ("video/mp2p".equals(oldMimeType) && newTypeIsAudio) {
            return true;
        }
        if ("video/x-matroska".equals(oldMimeType) && newTypeIsAudio) {
            return true;
        }
        return "video/x-pn-realvideo".equals(oldMimeType) && (newMimeType == null || newMimeType.equals(ContentResolver.MIME_TYPE_DEFAULT));
    }

    public static synchronized void initMediaFileCapture(String process) {
        synchronized (MediaScannerInjector.class) {
            sProcessName = process;
            if (sHandlerThread == null && sHandler == null) {
                sHandlerThread = new HandlerThread(TAG) {
                    /* Access modifiers changed, original: protected */
                    public void onLooperPrepared() {
                        MediaScannerInjector.sHandler = new Handler(getLooper()) {
                            public void handleMessage(Message msg) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("handleMessage what: ");
                                stringBuilder.append(msg.what);
                                String stringBuilder2 = stringBuilder.toString();
                                String str = MediaScannerInjector.TAG;
                                Log.e(str, stringBuilder2);
                                if (msg.what == 1) {
                                    stringBuilder2 = msg.obj;
                                    StringBuilder stringBuilder3 = new StringBuilder();
                                    stringBuilder3.append("ProcessFile timeout, path: ");
                                    stringBuilder3.append(stringBuilder2);
                                    Log.e(str, stringBuilder3.toString());
                                    Intent intent = new Intent("miui.intent.ACTION_MEDIASCANNER_TIMEOUT");
                                    intent.putExtra("miui.intent.extra.EXTRA_FILE_PATH", stringBuilder2);
                                    intent.putExtra("miui.intent.extra.EXTRA_PROCESS_NAME", MediaScannerInjector.sProcessName);
                                    intent.setPackage("com.miui.securitycenter");
                                    MediaScannerInjector.sContext.sendBroadcast(intent);
                                }
                            }
                        };
                    }
                };
                sHandlerThread.start();
            }
        }
    }

    public static void processFileBegin(String path, Context c) {
        Message msg = sHandler;
        if (msg != null) {
            sHandler.sendMessageDelayed(msg.obtainMessage(1, path), 60000);
        }
        sContext = c;
    }

    public static void processFileEnd() {
        Handler handler = sHandler;
        if (handler != null) {
            handler.removeMessages(1);
        }
        sContext = null;
    }

    private static void initDebugDirectory() {
        File f = new File(DEBUG_LOG_PATH);
        if (!f.exists()) {
            if (FileUtils.mkdirs(f, -1, -1, -1)) {
                FileUtils.addNoMedia(DEBUG_LOG_PATH);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("mkdir ");
                stringBuilder.append(DEBUG_LOG_PATH);
                stringBuilder.append(" failed");
                Log.e(TAG, stringBuilder.toString());
            }
        }
    }

    private static void copyFile(String src, String dst, int length) {
        String str = "IOException";
        String str2 = TAG;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(src);
            fos = new FileOutputStream(dst);
            fos.write(170);
            int size = 0;
            byte[] b = new byte[4096];
            while (true) {
                int read = fis.read(b);
                int bytesRead = read;
                if (read > 0) {
                    size += bytesRead;
                    if (size >= length) {
                        fos.write(b, 0, bytesRead - (size - length));
                        break;
                    }
                    fos.write(b, 0, bytesRead);
                }
            }
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                Log.e(str2, str, e);
            }
        } catch (IOException e2) {
            Log.e(str2, "IOException ", e2);
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        } catch (Throwable th) {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e3) {
                    Log.e(str2, str, e3);
                }
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    private static void loadNoMediaWhiteListLocked() {
        if (mNoMediaWhiteList.size() <= 0) {
            String externalStoragePath = Environment.getExternalStorageDirectory().getPath();
            for (String path : NOMEDIA_PATH_WHITE_LIST) {
                String element = new StringBuilder();
                element.append(externalStoragePath);
                element.append(path);
                element = element.toString();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("add ");
                stringBuilder.append(element);
                stringBuilder.append("to white list");
                Log.d(TAG, stringBuilder.toString());
                mNoMediaWhiteList.add(element);
            }
        }
    }

    public static boolean isInNoMediaWhiteList(String path) {
        boolean contains;
        synchronized (mNoMediaWhiteList) {
            loadNoMediaWhiteListLocked();
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("isInNoMediaWhiteList: ");
            stringBuilder.append(path);
            Log.d(str, stringBuilder.toString());
            contains = mNoMediaWhiteList.contains(path);
        }
        return contains;
    }

    public static HashSet<String> getNoMediaWhiteList() {
        HashSet hashSet;
        synchronized (mNoMediaWhiteList) {
            loadNoMediaWhiteListLocked();
            hashSet = mNoMediaWhiteList;
        }
        return hashSet;
    }

    public static void forceRescanExternal(Context context) throws RemoteException {
        context.getContentResolver().acquireProvider(MediaStore.AUTHORITY).call(context.getPackageName(), MediaStore.UNHIDE_CALL, Environment.getExternalStorageDirectory().getPath(), null);
    }
}
