package miui.app.backup;

import android.app.backup.FullBackup;
import android.app.backup.FullBackupDataOutput;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class BackupMeta {
    private static final String METAFILE_NAME = "_tmp_meta";
    public static final int META_VERSION = 1;
    private static final String TAG = "Backup:BackupMeta";
    public int appVersionCode;
    public String appVersionName;
    public long createTime;
    public String deviceName;
    public int feature;
    public int metaVersion = 1;
    public String miuiVersion;
    public String packageName;
    public int version;

    public void writeToTar(Context context, FullBackupDataOutput data) throws IOException {
        File tmpMeta = new File(context.getExternalCacheDir(), METAFILE_NAME);
        StringBuilder buf = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(1));
        String str = "\n";
        stringBuilder.append(str);
        buf.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.packageName);
        stringBuilder.append(str);
        buf.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(this.appVersionCode));
        stringBuilder.append(str);
        buf.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.appVersionName);
        stringBuilder.append(str);
        buf.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(this.createTime));
        stringBuilder.append(str);
        buf.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(this.version));
        stringBuilder.append(str);
        buf.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(this.feature));
        stringBuilder.append(str);
        buf.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.deviceName);
        stringBuilder.append(str);
        buf.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.miuiVersion);
        stringBuilder.append(str);
        buf.append(stringBuilder.toString());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tmpMeta);
            fos.write(buf.toString().getBytes());
            FullBackupProxy.backupToTar(this.packageName, BackupManager.DOMAIN_META, null, tmpMeta.getParentFile().getParent(), tmpMeta.getAbsolutePath(), data);
            fos.close();
            tmpMeta.delete();
        } catch (Throwable th) {
            if (fos != null) {
                fos.close();
            }
            tmpMeta.delete();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void buildFrom(Context context, ParcelFileDescriptor data, long size, int type, long mode, long mtime) throws IOException {
        File tmpMeta = new File(context.getExternalCacheDir(), METAFILE_NAME);
        BufferedReader reader = null;
        try {
            FullBackup.restoreFile(data, size, type, mode, mtime, tmpMeta);
            reader = new BufferedReader(new FileReader(tmpMeta));
            this.metaVersion = Integer.parseInt(reader.readLine());
            int i = this.metaVersion;
            String str = TAG;
            if (i != 1) {
                Log.e(str, "version error");
            } else {
                this.packageName = reader.readLine();
                this.appVersionCode = Integer.parseInt(reader.readLine());
                this.appVersionName = reader.readLine();
                this.createTime = Long.parseLong(reader.readLine());
                this.version = Integer.parseInt(reader.readLine());
                this.feature = Integer.parseInt(reader.readLine());
                this.deviceName = reader.readLine();
                this.miuiVersion = reader.readLine();
                if (reader.readLine() != null) {
                    Log.e(str, "Something wrong in meta file");
                }
            }
            reader.close();
        } catch (Throwable th) {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public int getMetaVersion() {
        return 1;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public int getAppVersionCode() {
        return this.appVersionCode;
    }

    public String getAppVersionName() {
        return this.appVersionName;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public int getVersion() {
        return this.version;
    }

    public int getFeature() {
        return this.feature;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getMiuiVersion() {
        return this.miuiVersion;
    }
}
