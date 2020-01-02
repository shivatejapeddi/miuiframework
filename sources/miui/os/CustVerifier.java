package miui.os;

import android.content.pm.PackageParser;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Slog;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Scanner;

public class CustVerifier {
    private static final String CUST_PATH;
    private static final String CUST_SIGNATURE_FILE;
    private static final boolean DEBUG = Build.IS_DEBUGGABLE;
    private static final int INT_SIZE = 4;
    public static final int MODE_DELETE = 1;
    public static final int MODE_NORMAL = 0;
    private static final int RSANUMBYTES = 256;
    private static final String TAG = "CustVerifier";
    private static final String VERIFY_KEY_FILE = "/verity_key";
    private static CustVerifier sInstance;
    private PublicKey mPubKey;
    private HashMap<String, String> mSignatures;

    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/cust/cust/");
        stringBuilder.append(Build.getCustVariant());
        stringBuilder.append("/");
        CUST_PATH = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(CUST_PATH);
        stringBuilder.append("sign_customized_applist");
        CUST_SIGNATURE_FILE = stringBuilder.toString();
    }

    public static synchronized CustVerifier getInstance() {
        CustVerifier instance;
        synchronized (CustVerifier.class) {
            if (sInstance == null) {
                instance = new CustVerifier();
                if (instance.init()) {
                    sInstance = instance;
                }
            }
            instance = sInstance;
        }
        return instance;
    }

    private String getSignPath(String path) {
        String[] pathSplite = path.split("\\/");
        StringBuffer sb = new StringBuffer(CUST_PATH);
        sb.append(pathSplite[pathSplite.length - 1]);
        return sb.toString().replace(PackageParser.APK_FILE_EXTENSION, ".sig");
    }

    public boolean verifyApkSignatue(String path, int mode) {
        String sigPath = getSignPath(path);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" Sig path ");
        stringBuilder.append(sigPath);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Slog.e(str, stringBuilder2);
        File sigFile = new File(sigPath);
        boolean hasSigFile = sigFile.exists();
        if (mode == 0) {
            String signature;
            StringBuilder stringBuilder3;
            if (hasSigFile) {
                signature = loadSignatureFromFile(sigFile);
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("has sig File1 : ");
                stringBuilder3.append(signature);
                Slog.e(str, stringBuilder3.toString());
            } else {
                signature = (String) this.mSignatures.get(path);
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("has sig File2 : ");
                stringBuilder3.append(signature);
                Slog.e(str, stringBuilder3.toString());
            }
            if (signature != null) {
                return verifyFileSignature(path, hexStringToBytes(signature));
            }
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append("no signature found for ");
            stringBuilder3.append(path);
            Slog.e(str, stringBuilder3.toString());
            return false;
        } else if (mode != 1) {
            throw new RuntimeException("not supported mode");
        } else if (hasSigFile) {
            return verifyPathSignature(path, loadSignatureFromFile(sigFile));
        } else {
            return false;
        }
    }

    private CustVerifier() {
    }

    private boolean init() {
        this.mPubKey = loadVerifyKey(VERIFY_KEY_FILE);
        boolean z = false;
        if (this.mPubKey == null) {
            Slog.e(TAG, "failed to load verify key");
            return false;
        }
        this.mSignatures = getCustApkSignatures(CUST_SIGNATURE_FILE);
        if (this.mSignatures != null) {
            z = true;
        }
        return z;
    }

    private boolean verifyPathSignature(String path, String signature) {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(this.mPubKey);
            sig.update(path.getBytes());
            return sig.verify(hexStringToBytes(signature));
        } catch (Exception e) {
            Slog.e(TAG, e.toString());
            return false;
        }
    }

    private String loadSignatureFromFile(File sigFile) {
        byte[] buf = new byte[512];
        FileInputStream fis = new FileInputStream(sigFile);
        int total = 0;
        do {
            try {
                int read = fis.read(buf, total, buf.length - total);
                int count = read;
                if (read != -1) {
                    total += count;
                }
                break;
            } catch (IOException e) {
                Slog.e(TAG, e.toString());
                return null;
            } catch (Throwable th) {
                fis.close();
            }
        } while (total < buf.length);
        fis.close();
        return new String(buf);
    }

    private PublicKey loadVerifyKey(String keyPath) {
        Exception e;
        StringBuilder stringBuilder;
        String str = TAG;
        try {
            RandomAccessFile keyFile;
            try {
                keyFile = new RandomAccessFile(keyPath, "r");
                byte[] buf = new byte[4];
                if (keyFile.read(buf) != buf.length) {
                    Slog.e(str, "EOF when read len bytes");
                    keyFile.close();
                    return null;
                }
                int len = (((buf[0] | (buf[1] << 8)) | (buf[2] << 16)) | (buf[3] << 24)) * 4;
                if (len > 0) {
                    if (len <= 256) {
                        if (keyFile.skipBytes(4) != 4) {
                            Slog.e(str, "can't skip n0inv bytes");
                            keyFile.close();
                            return null;
                        }
                        byte[] modBytes = new byte[len];
                        if (keyFile.read(modBytes) != modBytes.length) {
                            Slog.e(str, "EOF when read mod bytes");
                            keyFile.close();
                            return null;
                        }
                        int j;
                        int i = 0;
                        for (j = len - 1; i < j; j--) {
                            byte b = modBytes[i];
                            modBytes[i] = modBytes[j];
                            modBytes[j] = b;
                            i++;
                        }
                        BigInteger mod = new BigInteger(1, modBytes);
                        j = (256 - len) + 256;
                        if (keyFile.skipBytes(j) != j) {
                            Slog.e(str, "can't skip rr bytes");
                            keyFile.close();
                            return null;
                        } else if (keyFile.read(buf) != buf.length) {
                            Slog.e(str, "EOF when read exp bytes");
                            keyFile.close();
                            return null;
                        } else {
                            int expInt = ((buf[0] | (buf[1] << 8)) | (buf[2] << 16)) | (buf[3] << 24);
                            PublicKey pk = KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_RSA).generatePublic(new RSAPublicKeySpec(mod, BigInteger.valueOf((long) expInt)));
                            if (DEBUG) {
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("loadVerifyKey: \n\tlen: ");
                                stringBuilder2.append(len);
                                stringBuilder2.append("\n\tmodulus: ");
                                stringBuilder2.append(mod.toString(16));
                                stringBuilder2.append("\n\tpublic exponent: ");
                                stringBuilder2.append(expInt);
                                Slog.d(str, stringBuilder2.toString());
                            }
                            keyFile.close();
                            return pk;
                        }
                    }
                }
                Slog.e(str, "invalid len bytes");
                keyFile.close();
                return null;
            } catch (Exception e2) {
                e = e2;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Exception occur when load verify key: ");
                stringBuilder.append(e.toString());
                Slog.e(str, stringBuilder.toString());
                return null;
            } catch (Throwable th) {
                keyFile.close();
            }
        } catch (Exception e3) {
            e = e3;
            String str2 = keyPath;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Exception occur when load verify key: ");
            stringBuilder.append(e.toString());
            Slog.e(str, stringBuilder.toString());
            return null;
        }
    }

    private boolean verifyFileSignature(String filename, byte[] signature) {
        if (TextUtils.isEmpty(filename)) {
            Slog.e(TAG, "verifyFileSignature get null file");
            return false;
        }
        File f = new File(filename);
        return verifyFileBlockSignature(f, (int) f.length(), signature);
    }

    private boolean verifyFileBlockSignature(File file, int size, byte[] signature) {
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        String str2 = TAG;
        if (size == 0) {
            Slog.e(str2, "verifyFileBlockSignature get 0-sized block");
            return false;
        }
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(this.mPubKey);
        byte[] buf = new byte[4096];
        FileInputStream fis = new FileInputStream(file);
        int total = 0;
        do {
            try {
                int read = fis.read(buf);
                int count = read;
                if (read == -1) {
                    break;
                }
                if (count + total > size) {
                    count = size - total;
                }
                sig.update(buf, 0, count);
                total += count;
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Exception occurs when verify file block: ");
                stringBuilder.append(e.toString());
                Slog.e(str2, stringBuilder.toString());
                return false;
            } catch (Throwable th) {
                fis.close();
            }
        } while (total != size);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("total: ");
        stringBuilder2.append(total);
        Slog.e(str2, stringBuilder2.toString());
        boolean result = sig.verify(signature);
        if (DEBUG) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("verify ");
            stringBuilder3.append(file.getAbsolutePath());
            stringBuilder3.append(str);
            stringBuilder3.append(size);
            stringBuilder3.append(str);
            stringBuilder3.append(result);
            Slog.d(str2, stringBuilder3.toString());
        }
        fis.close();
        return result;
    }

    private HashMap<String, String> getCustApkSignatures(String sigFn) {
        String str = TAG;
        HashMap<String, String> signatures = new HashMap();
        String failedMsg = null;
        Scanner scanner;
        String line;
        try {
            File signFile = new File(sigFn);
            scanner = new Scanner(signFile);
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String[] tokens = line.split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (tokens.length == 2) {
                    signatures.put(tokens[0], tokens[1]);
                } else {
                    int fileSize = (int) signFile.length();
                    if (verifyFileBlockSignature(signFile, fileSize - tokens[0].length(), hexStringToBytes(tokens[0]))) {
                        if (DEBUG) {
                            Slog.d(str, "cust apk and signatures:");
                            for (Entry<String, String> entry : signatures.entrySet()) {
                                StringBuilder stringBuilder = new StringBuilder();
                                String line2 = line;
                                stringBuilder.append("\t");
                                stringBuilder.append((String) entry.getKey());
                                stringBuilder.append(":");
                                stringBuilder.append(((String) entry.getValue()).substring(0, 20));
                                Slog.d(str, stringBuilder.toString());
                                int i = 0;
                                line = line2;
                                String str2 = sigFn;
                            }
                        }
                        scanner.close();
                        if (null == null) {
                            failedMsg = "Cust signature read finish.";
                        }
                        Slog.e(str, failedMsg);
                        return signatures;
                    }
                    failedMsg = "cust signature file is cracked";
                }
            }
            scanner.close();
            if (failedMsg == null) {
                failedMsg = "cust signature file is not signed";
            }
        } catch (NullPointerException ne) {
            Slog.e(str, ne.toString());
            failedMsg = "cust signature file not found";
        } catch (IOException ie) {
            Slog.e(str, ie.toString());
            failedMsg = "failed to read cust signature file: IOException";
        } catch (Exception e) {
            try {
                Slog.e(str, e.toString());
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("failed to read cust signature file: ");
                stringBuilder2.append(e.toString());
                line = stringBuilder2.toString();
                if (line == null) {
                    failedMsg = "Cust signature read finish.";
                } else {
                    failedMsg = line;
                }
            } catch (Throwable th) {
                if (failedMsg == null) {
                    failedMsg = "Cust signature read finish.";
                }
                Slog.e(str, failedMsg);
            }
        } catch (Throwable th2) {
            scanner.close();
        }
        Slog.e(str, failedMsg);
        return null;
    }

    private void checkCustSignature() {
        String str = TAG;
        PublicKey pubKey = loadVerifyKey(VERIFY_KEY_FILE);
        LinkedList<File> queue = new LinkedList();
        try {
            queue.add(new File(CUST_PATH));
            while (!queue.isEmpty()) {
                File dir = (File) queue.remove();
                String[] files = dir.list();
                if (DEBUG) {
                    Slog.d(str, dir.getAbsolutePath());
                }
                for (String fn : files) {
                    File f = new File(dir, fn);
                    if (DEBUG) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("\t");
                        stringBuilder.append(f.getAbsolutePath());
                        Slog.d(str, stringBuilder.toString());
                    }
                    if (f.isDirectory()) {
                        queue.add(f);
                    } else if (f.isFile()) {
                        fn.endsWith(PackageParser.APK_FILE_EXTENSION);
                    }
                }
            }
        } catch (Exception e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("checkCustSignature occurs: ");
            stringBuilder2.append(e.toString());
            Slog.e(str, stringBuilder2.toString());
        }
    }

    private byte a2b(byte a) {
        if (a >= (byte) 48 && a <= (byte) 57) {
            return (byte) (a - 48);
        }
        if (a >= (byte) 97 && a <= (byte) 102) {
            return (byte) ((a - 97) + 10);
        }
        if (a < (byte) 65 || a > (byte) 70) {
            return (byte) 0;
        }
        return (byte) ((a - 65) + 10);
    }

    private byte b2a(int b) {
        if (b < 10) {
            return (byte) (b + 48);
        }
        return (byte) ((b - 10) + 97);
    }

    private byte[] hexStringToBytes(String hex) {
        if (hex.length() % 2 == 1) {
            return null;
        }
        byte[] buf = new byte[(hex.length() / 2)];
        byte[] str = hex.getBytes();
        int j = 0;
        int i = 0;
        while (i < str.length - 1) {
            int j2 = j + 1;
            buf[j] = (byte) ((a2b(str[i]) << 4) | a2b(str[i + 1]));
            i += 2;
            j = j2;
        }
        return buf;
    }

    private String bytesToHexString(byte[] data) {
        byte[] res = new byte[(data.length * 2)];
        int j = 0;
        for (int i = 0; i < data.length; i++) {
            int i2 = j + 1;
            res[j] = b2a((data[i] >> 4) & 15);
            j = i2 + 1;
            res[i2] = b2a(data[i] & 15);
        }
        return new String(res);
    }
}
