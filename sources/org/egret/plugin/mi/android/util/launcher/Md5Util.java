package org.egret.plugin.mi.android.util.launcher;

import android.security.keystore.KeyProperties;
import android.text.format.DateFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

public class Md5Util {
    public static boolean checkMd5(File file, String md5String) {
        boolean z = false;
        if (file == null || md5String == null) {
            return false;
        }
        String hash = md5(file);
        if (hash != null && hash.equals(md5String)) {
            z = true;
        }
        return z;
    }

    public static String md5(File file) {
        if (!file.exists()) {
            return null;
        }
        FileInputStream in = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance(KeyProperties.DIGEST_MD5);
            in = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            while (true) {
                int read = in.read(buffer);
                int b = read;
                if (read <= 0) {
                    break;
                }
                md5.update(buffer, 0, b);
            }
            String md5String = getMd5String(md5.digest());
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return md5String;
        } catch (Exception e2) {
            e2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            return null;
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
    }

    private static String getMd5String(byte[] md) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', DateFormat.AM_PM, 'b', 'c', DateFormat.DATE, 'e', 'f'};
        int j = md.length;
        char[] str = new char[(j * 2)];
        for (int i = 0; i < j; i++) {
            byte hex = md[i];
            str[i * 2] = hexDigits[(hex >>> 4) & 15];
            str[(i * 2) + 1] = hexDigits[hex & 15];
        }
        return new String(str);
    }
}
