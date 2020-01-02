package miui.media;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import java.io.File;
import miui.os.Environment;

public class MiuiMediaScannerUtil {
    private static final String MEDIA_SCANNER_CLASS = "com.android.providers.media.MediaScannerReceiver";
    private static final String MEDIA_SCANNER_PACKAGE = "com.android.providers.media";
    private static final String TAG = "MiuiMediaScannerUtil";

    public static void scanFolder(Context context, String folderPath) {
        if (folderPath == null || !new File(folderPath).isDirectory()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The folder path to scan is invalid: ");
            stringBuilder.append(folderPath);
            Log.e(TAG, stringBuilder.toString());
            return;
        }
        Intent intent = new Intent("miui.intent.action.MEDIA_SCANNER_SCAN_FOLDER");
        intent.setClassName(MEDIA_SCANNER_PACKAGE, MEDIA_SCANNER_CLASS);
        intent.setData(Uri.fromFile(new File(folderPath)));
        context.sendBroadcast(intent);
    }

    public static void scanSingleFile(Context context, String filePath) {
        if (filePath == null || new File(filePath).isDirectory()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The path must be a file path: ");
            stringBuilder.append(filePath);
            Log.e(TAG, stringBuilder.toString());
            return;
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setClassName(MEDIA_SCANNER_PACKAGE, MEDIA_SCANNER_CLASS);
        intent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(intent);
    }

    public static void scanWholeExternalStorage(Context context) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
        intent.setClassName(MEDIA_SCANNER_PACKAGE, MEDIA_SCANNER_CLASS);
        intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
        context.sendBroadcast(intent);
    }
}
