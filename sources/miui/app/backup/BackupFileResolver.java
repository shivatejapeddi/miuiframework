package miui.app.backup;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiEnterpriseConfig;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class BackupFileResolver {
    private static final String TAG = "Backup:BackupFileResolver";

    public static class BackupFileMiuiHeader {
        public String apkName;
        public String encryptedPwd;
        public int featureId;
        public boolean isEncrypted;
        public String packageName;
        public int version;
    }

    public static BackupFileMiuiHeader getMiuiHeader(File bakFile) {
        String str = "IOException";
        String str2 = TAG;
        BackupFileMiuiHeader header = null;
        InputStream in = null;
        try {
            in = new FileInputStream(bakFile);
            header = readMiuiHeader(in);
            try {
                in.close();
            } catch (IOException e) {
                Log.e(str2, str, e);
            }
        } catch (FileNotFoundException e2) {
            Log.e(str2, str, e2);
            if (in != null) {
                in.close();
            }
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                    Log.e(str2, str, e3);
                }
            }
        }
        return header;
    }

    public static BackupFileMiuiHeader readMiuiHeader(InputStream in) {
        try {
            String s = new StringBuilder();
            s.append(readLine(in));
            s.append("\n");
            if (!BackupManager.BACKUP_FILE_HEADER_MAGIC.equals(s.toString())) {
                return null;
            }
            BackupFileMiuiHeader tmp = new BackupFileMiuiHeader();
            tmp.version = Integer.parseInt(readLine(in));
            if (tmp.version != 2) {
                return null;
            }
            String[] ss = readLine(in).split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, 2);
            tmp.packageName = ss[0];
            if (ss.length > 1) {
                tmp.apkName = ss[1];
            }
            tmp.featureId = Integer.valueOf(readLine(in)).intValue();
            tmp.isEncrypted = "1".equals(readLine(in));
            if (tmp.isEncrypted) {
                tmp.encryptedPwd = readLine(in);
            }
            return tmp;
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }

    public static void writeMiuiHeader(OutputStream out, Context context, String pkg, int featrueId, String encyptedPwd) throws IOException {
        boolean isEncrypted = TextUtils.isEmpty(encyptedPwd) ^ true;
        StringBuilder buf = new StringBuilder();
        PackageManager pm = context.getPackageManager();
        String appName = null;
        try {
            appName = pm.getPackageInfo(pkg, 0).applicationInfo.loadLabel(pm).toString().replace(10, ' ');
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        buf.append(BackupManager.BACKUP_FILE_HEADER_MAGIC);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(2));
        String str = "\n";
        stringBuilder.append(str);
        buf.append(stringBuilder.toString());
        if (TextUtils.isEmpty(appName)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(pkg);
            stringBuilder.append(str);
            buf.append(stringBuilder.toString());
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(pkg);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(appName);
            stringBuilder.append(str);
            buf.append(stringBuilder.toString());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(featrueId));
        stringBuilder.append(str);
        buf.append(stringBuilder.toString());
        StringBuilder stringBuilder2;
        if (isEncrypted) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(String.valueOf(1));
            stringBuilder3.append(str);
            buf.append(stringBuilder3.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(encyptedPwd);
            stringBuilder2.append(str);
            buf.append(stringBuilder2.toString());
        } else {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(String.valueOf(0));
            stringBuilder2.append(str);
            buf.append(stringBuilder2.toString());
        }
        out.write(buf.toString().getBytes());
    }

    private static String readLine(InputStream in) throws IOException {
        byte[] sequence;
        ArrayList<Byte> buffer = new ArrayList();
        while (true) {
            int read = in.read();
            int c = read;
            if (read < 0 || c == 10) {
                sequence = new byte[buffer.size()];
            } else {
                buffer.add(Byte.valueOf((byte) c));
            }
        }
        sequence = new byte[buffer.size()];
        for (int i = 0; i < buffer.size(); i++) {
            sequence[i] = ((Byte) buffer.get(i)).byteValue();
        }
        return new String(sequence, Charset.forName("UTF-8"));
    }
}
