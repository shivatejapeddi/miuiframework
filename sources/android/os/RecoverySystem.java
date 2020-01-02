package android.os;

import android.Manifest.permission;
import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.IRecoverySystemProgressListener.Stub;
import android.provider.Settings.Global;
import android.telephony.euicc.EuiccManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.WindowManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import libcore.io.Streams;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;

public class RecoverySystem {
    private static final String ACTION_EUICC_FACTORY_RESET = "com.android.internal.action.EUICC_FACTORY_RESET";
    public static final File BLOCK_MAP_FILE = new File(RECOVERY_DIR, "block.map");
    private static final long DEFAULT_EUICC_FACTORY_RESET_TIMEOUT_MILLIS = 30000;
    private static final File DEFAULT_KEYSTORE = new File("/system/etc/security/otacerts.zip");
    private static final String LAST_INSTALL_PATH = "last_install";
    private static final String LAST_PREFIX = "last_";
    private static final File LOG_FILE = new File(RECOVERY_DIR, "log");
    private static final int LOG_FILE_MAX_LENGTH = 65536;
    private static final long MAX_EUICC_FACTORY_RESET_TIMEOUT_MILLIS = 60000;
    private static final long MIN_EUICC_FACTORY_RESET_TIMEOUT_MILLIS = 5000;
    private static final String PACKAGE_NAME_WIPING_EUICC_DATA_CALLBACK = "android";
    private static final long PUBLISH_PROGRESS_INTERVAL_MS = 500;
    private static final File RECOVERY_DIR = new File("/cache/recovery");
    private static final String TAG = "RecoverySystem";
    public static final File UNCRYPT_PACKAGE_FILE = new File(RECOVERY_DIR, "uncrypt_file");
    public static final File UNCRYPT_STATUS_FILE = new File(RECOVERY_DIR, "uncrypt_status");
    private static final Object sRequestLock = new Object();
    private final IRecoverySystem mService;

    public interface ProgressListener {
        void onProgress(int i);
    }

    private static HashSet<X509Certificate> getTrustedCerts(File keystore) throws IOException, GeneralSecurityException {
        HashSet<X509Certificate> trusted = new HashSet();
        if (keystore == null) {
            keystore = DEFAULT_KEYSTORE;
        }
        ZipFile zip = new ZipFile(keystore);
        InputStream is;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                is = zip.getInputStream((ZipEntry) entries.nextElement());
                trusted.add((X509Certificate) cf.generateCertificate(is));
                is.close();
            }
            zip.close();
            return trusted;
        } catch (Throwable th) {
            zip.close();
        }
    }

    public static void verifyPackage(File packageFile, ProgressListener listener, File deviceCertsZipFile) throws IOException, GeneralSecurityException {
        Throwable th;
        ProgressListener progressListener = listener;
        long fileLen = packageFile.length();
        RandomAccessFile raf = new RandomAccessFile(packageFile, "r");
        ProgressListener progressListener2;
        try {
            final long startTimeMillis = System.currentTimeMillis();
            if (progressListener != null) {
                progressListener.onProgress(0);
            }
            raf.seek(fileLen - 6);
            byte[] footer = new byte[6];
            raf.readFully(footer);
            if (footer[2] == (byte) -1) {
                int i = 3;
                if (footer[3] == (byte) -1) {
                    int commentSize = ((footer[5] & 255) << 8) | (footer[4] & 255);
                    int signatureStart = (footer[0] & 255) | ((footer[1] & 255) << 8);
                    byte[] eocd = new byte[(commentSize + 22)];
                    try {
                        raf.seek(fileLen - ((long) (commentSize + 22)));
                        raf.readFully(eocd);
                        byte[] bArr;
                        int i2;
                        if (eocd[0] == (byte) 80 && eocd[1] == (byte) 75 && eocd[2] == (byte) 5 && eocd[3] == (byte) 6) {
                            int i3 = 4;
                            while (i3 < eocd.length - i) {
                                if (eocd[i3] == (byte) 80 && eocd[i3 + 1] == (byte) 75 && eocd[i3 + 2] == (byte) 5) {
                                    if (eocd[i3 + 3] == (byte) 6) {
                                        throw new SignatureException("EOCD marker found after start of EOCD");
                                    }
                                }
                                i3++;
                                i = 3;
                            }
                            PKCS7 block = new PKCS7(new ByteArrayInputStream(eocd, (commentSize + 22) - signatureStart, signatureStart));
                            X509Certificate[] certificates = block.getCertificates();
                            X509Certificate[] x509CertificateArr;
                            if (certificates == null || certificates.length == 0) {
                                progressListener2 = listener;
                                x509CertificateArr = certificates;
                                bArr = eocd;
                                i2 = signatureStart;
                                throw new SignatureException("signature contains no certificates");
                            }
                            PublicKey signatureKey = certificates[0].getPublicKey();
                            SignerInfo[] signerInfos = block.getSignerInfos();
                            PublicKey publicKey;
                            if (signerInfos == null || signerInfos.length == 0) {
                                x509CertificateArr = certificates;
                                publicKey = signatureKey;
                                bArr = eocd;
                                i2 = signatureStart;
                                progressListener2 = listener;
                                throw new SignatureException("signature contains no signedData");
                            }
                            boolean verified;
                            SignerInfo signerInfo = signerInfos[0];
                            Iterator it = getTrustedCerts(deviceCertsZipFile == null ? DEFAULT_KEYSTORE : deviceCertsZipFile).iterator();
                            while (it.hasNext()) {
                                SignerInfo[] signerInfos2 = signerInfos;
                                if (((X509Certificate) it.next()).getPublicKey().equals(signatureKey) != null) {
                                    verified = true;
                                    break;
                                }
                                signerInfos = signerInfos2;
                            }
                            verified = false;
                            if (verified) {
                                raf.seek(null);
                                final ProgressListener listenerForInner = listener;
                                InputStream inputStream = signerInfos;
                                certificates = fileLen;
                                final int i4 = commentSize;
                                final RandomAccessFile randomAccessFile = raf;
                                signerInfos = new InputStream() {
                                    int lastPercent = 0;
                                    long lastPublishTime = startTimeMillis;
                                    long soFar = 0;
                                    long toRead = ((certificates - ((long) i4)) - 2);

                                    public int read() throws IOException {
                                        throw new UnsupportedOperationException();
                                    }

                                    public int read(byte[] b, int off, int len) throws IOException {
                                        if (this.soFar >= this.toRead || Thread.currentThread().isInterrupted()) {
                                            return -1;
                                        }
                                        int size = len;
                                        long j = this.soFar;
                                        long j2 = ((long) size) + j;
                                        long j3 = this.toRead;
                                        if (j2 > j3) {
                                            size = (int) (j3 - j);
                                        }
                                        int read = randomAccessFile.read(b, off, size);
                                        this.soFar += (long) read;
                                        if (listenerForInner != null) {
                                            long now = System.currentTimeMillis();
                                            int p = (int) ((this.soFar * 100) / this.toRead);
                                            if (p > this.lastPercent && now - this.lastPublishTime > 500) {
                                                this.lastPercent = p;
                                                this.lastPublishTime = now;
                                                listenerForInner.onProgress(this.lastPercent);
                                            }
                                        }
                                        return read;
                                    }
                                };
                                signerInfos = block.verify(signerInfo, inputStream);
                                certificates = Thread.interrupted();
                                progressListener2 = listener;
                                if (progressListener2 != null) {
                                    progressListener2.onProgress(100);
                                }
                                if (certificates != null) {
                                    throw new SignatureException("verification was interrupted");
                                } else if (signerInfos != null) {
                                    raf.close();
                                    if (!readAndVerifyPackageCompatibilityEntry(packageFile)) {
                                        throw new SignatureException("package compatibility verification failed");
                                    }
                                    return;
                                } else {
                                    throw new SignatureException("signature digest verification failed");
                                }
                            }
                            publicKey = signatureKey;
                            bArr = eocd;
                            i2 = signatureStart;
                            progressListener2 = listener;
                            throw new SignatureException("signature doesn't match any trusted key");
                        }
                        progressListener2 = listener;
                        bArr = eocd;
                        i2 = signatureStart;
                        throw new SignatureException("no signature in file (bad footer)");
                    } catch (Throwable th2) {
                        th = th2;
                        raf.close();
                        throw th;
                    }
                }
            }
            progressListener2 = progressListener;
            throw new SignatureException("no signature in file (no footer)");
        } catch (Throwable th3) {
            th = th3;
            progressListener2 = progressListener;
            raf.close();
            throw th;
        }
    }

    @UnsupportedAppUsage
    private static boolean verifyPackageCompatibility(InputStream inputStream) throws IOException {
        long entrySize;
        StringBuilder stringBuilder;
        ArrayList<String> list = new ArrayList();
        ZipInputStream zis = new ZipInputStream(inputStream);
        while (true) {
            ZipEntry nextEntry = zis.getNextEntry();
            ZipEntry entry = nextEntry;
            if (nextEntry != null) {
                entrySize = entry.getSize();
                if (entrySize > 2147483647L || entrySize < 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("invalid entry size (");
                    stringBuilder.append(entrySize);
                    stringBuilder.append(") in the compatibility file");
                } else {
                    byte[] bytes = new byte[((int) entrySize)];
                    Streams.readFully(zis, bytes);
                    list.add(new String(bytes, StandardCharsets.UTF_8));
                }
            } else if (!list.isEmpty()) {
                return VintfObject.verify((String[]) list.toArray(new String[list.size()])) == 0;
            } else {
                throw new IOException("no entries found in the compatibility file");
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("invalid entry size (");
        stringBuilder.append(entrySize);
        stringBuilder.append(") in the compatibility file");
        throw new IOException(stringBuilder.toString());
    }

    /* JADX WARNING: Missing block: B:15:0x0022, code skipped:
            $closeResource(r1, r0);
     */
    private static boolean readAndVerifyPackageCompatibilityEntry(java.io.File r5) throws java.io.IOException {
        /*
        r0 = new java.util.zip.ZipFile;
        r0.<init>(r5);
        r1 = "compatibility.zip";
        r1 = r0.getEntry(r1);	 Catch:{ all -> 0x001f }
        r2 = 0;
        if (r1 != 0) goto L_0x0013;
    L_0x000e:
        r3 = 1;
        $closeResource(r2, r0);
        return r3;
    L_0x0013:
        r3 = r0.getInputStream(r1);	 Catch:{ all -> 0x001f }
        r4 = verifyPackageCompatibility(r3);	 Catch:{ all -> 0x001f }
        $closeResource(r2, r0);
        return r4;
    L_0x001f:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0021 }
    L_0x0021:
        r2 = move-exception;
        $closeResource(r1, r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.RecoverySystem.readAndVerifyPackageCompatibilityEntry(java.io.File):boolean");
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 != null) {
            try {
                x1.close();
                return;
            } catch (Throwable th) {
                x0.addSuppressed(th);
                return;
            }
        }
        x1.close();
    }

    /* JADX WARNING: Missing block: B:9:0x0011, code skipped:
            $closeResource(r1, r0);
     */
    @android.annotation.SuppressLint({"Doclava125"})
    @android.annotation.SystemApi
    public static boolean verifyPackageCompatibility(java.io.File r3) throws java.io.IOException {
        /*
        r0 = new java.io.FileInputStream;
        r0.<init>(r3);
        r1 = verifyPackageCompatibility(r0);	 Catch:{ all -> 0x000e }
        r2 = 0;
        $closeResource(r2, r0);
        return r1;
    L_0x000e:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0010 }
    L_0x0010:
        r2 = move-exception;
        $closeResource(r1, r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.RecoverySystem.verifyPackageCompatibility(java.io.File):boolean");
    }

    @SystemApi
    public static void processPackage(Context context, File packageFile, final ProgressListener listener, Handler handler) throws IOException {
        String filename = packageFile.getCanonicalPath();
        if (filename.startsWith("/data/")) {
            RecoverySystem rs = (RecoverySystem) context.getSystemService("recovery");
            IRecoverySystemProgressListener progressListener = null;
            if (listener != null) {
                Handler progressHandler;
                if (handler != null) {
                    progressHandler = handler;
                } else {
                    progressHandler = new Handler(context.getMainLooper());
                }
                progressListener = new Stub() {
                    int lastProgress = 0;
                    long lastPublishTime = System.currentTimeMillis();

                    public void onProgress(final int progress) {
                        final long now = System.currentTimeMillis();
                        progressHandler.post(new Runnable() {
                            public void run() {
                                if (progress > AnonymousClass2.this.lastProgress && now - AnonymousClass2.this.lastPublishTime > 500) {
                                    AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                                    anonymousClass2.lastProgress = progress;
                                    anonymousClass2.lastPublishTime = now;
                                    listener.onProgress(progress);
                                }
                            }
                        });
                    }
                };
            }
            if (!rs.uncrypt(filename, progressListener)) {
                throw new IOException("process package failed");
            }
        }
    }

    @SystemApi
    public static void processPackage(Context context, File packageFile, ProgressListener listener) throws IOException {
        processPackage(context, packageFile, listener, null);
    }

    public static void installPackage(Context context, File packageFile) throws IOException {
        installPackage(context, packageFile, false);
    }

    @SystemApi
    public static void installPackage(Context context, File packageFile, boolean processed) throws IOException {
        synchronized (sRequestLock) {
            String str;
            LOG_FILE.delete();
            UNCRYPT_PACKAGE_FILE.delete();
            String filename = packageFile.getCanonicalPath();
            String str2 = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("!!! REBOOTING TO INSTALL ");
            stringBuilder.append(filename);
            stringBuilder.append(" !!!");
            Log.w(str2, stringBuilder.toString());
            boolean securityUpdate = filename.endsWith("_s.zip");
            if (filename.startsWith("/data/")) {
                if (!processed) {
                    FileWriter uncryptFile = new FileWriter(UNCRYPT_PACKAGE_FILE);
                    try {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(filename);
                        stringBuilder2.append("\n");
                        uncryptFile.write(stringBuilder2.toString());
                        if (!(UNCRYPT_PACKAGE_FILE.setReadable(true, false) && UNCRYPT_PACKAGE_FILE.setWritable(true, false))) {
                            str = TAG;
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("Error setting permission for ");
                            stringBuilder3.append(UNCRYPT_PACKAGE_FILE);
                            Log.e(str, stringBuilder3.toString());
                        }
                        BLOCK_MAP_FILE.delete();
                    } finally {
                        uncryptFile.close();
                    }
                } else if (!BLOCK_MAP_FILE.exists()) {
                    Log.e(TAG, "Package claimed to have been processed but failed to find the block map file.");
                    throw new IOException("Failed to find block map file");
                }
                filename = "@/cache/recovery/block.map";
            }
            String filenameArg = new StringBuilder();
            filenameArg.append("--update_package=");
            filenameArg.append(filename);
            filenameArg.append("\n");
            filenameArg = filenameArg.toString();
            str = new StringBuilder();
            str.append("--locale=");
            str.append(Locale.getDefault().toLanguageTag());
            str.append("\n");
            str = str.toString();
            String securityArg = "--security\n";
            String command = new StringBuilder();
            command.append(filenameArg);
            command.append(str);
            command = command.toString();
            if (securityUpdate) {
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append(command);
                stringBuilder4.append("--security\n");
                command = stringBuilder4.toString();
            }
            if (((RecoverySystem) context.getSystemService("recovery")).setupBcb(command)) {
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                String reason = PowerManager.REBOOT_RECOVERY_UPDATE;
                if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LEANBACK) && ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getState() != 2) {
                    StringBuilder stringBuilder5 = new StringBuilder();
                    stringBuilder5.append(reason);
                    stringBuilder5.append(",quiescent");
                    reason = stringBuilder5.toString();
                }
                pm.reboot(reason);
                throw new IOException("Reboot failed (no permissions?)");
            }
            throw new IOException("Setup BCB failed");
        }
    }

    public static void installPackage(Context context, File packageFile, boolean processed, String secrect, boolean isErase) throws IOException {
        Throwable th;
        Context context2 = context;
        synchronized (sRequestLock) {
            FileWriter uncryptFile;
            try {
                LOG_FILE.delete();
                UNCRYPT_PACKAGE_FILE.delete();
                String filename = packageFile.getCanonicalPath();
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("!!! REBOOTING TO INSTALL ");
                stringBuilder.append(filename);
                stringBuilder.append(" !!!");
                Log.w(str, stringBuilder.toString());
                boolean securityUpdate = filename.endsWith("_s.zip");
                if (filename.startsWith("/data/")) {
                    if (!processed) {
                        uncryptFile = new FileWriter(UNCRYPT_PACKAGE_FILE);
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(filename);
                        stringBuilder2.append("\n");
                        uncryptFile.write(stringBuilder2.toString());
                        uncryptFile.close();
                        if (!(UNCRYPT_PACKAGE_FILE.setReadable(true, false) && UNCRYPT_PACKAGE_FILE.setWritable(true, false))) {
                            str = TAG;
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("Error setting permission for ");
                            stringBuilder3.append(UNCRYPT_PACKAGE_FILE);
                            Log.e(str, stringBuilder3.toString());
                        }
                        BLOCK_MAP_FILE.delete();
                    } else if (!BLOCK_MAP_FILE.exists()) {
                        Log.e(TAG, "Package claimed to have been processed but failed to find the block map file.");
                        throw new IOException("Failed to find block map file");
                    }
                    filename = "@/cache/recovery/block.map";
                }
                str = new StringBuilder();
                str.append("--update_package=");
                str.append(filename);
                str.append("\n");
                str = str.toString();
                String localeArg = new StringBuilder();
                localeArg.append("--locale=");
                localeArg.append(Locale.getDefault().toString());
                localeArg.append("\n");
                localeArg = localeArg.toString();
                String securityArg = "--security\n";
                String validateArg = new StringBuilder();
                validateArg.append("--export_validate=");
                try {
                    StringBuilder stringBuilder4;
                    validateArg.append(secrect);
                    validateArg.append("\n");
                    validateArg = validateArg.toString();
                    String eraseArg = "--update_wipe\n";
                    String command = new StringBuilder();
                    command.append(str);
                    command.append(localeArg);
                    command.append(validateArg);
                    command = command.toString();
                    if (isErase) {
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append(command);
                        stringBuilder4.append("--update_wipe\n");
                        command = stringBuilder4.toString();
                    }
                    if (securityUpdate) {
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append(command);
                        stringBuilder4.append("--security\n");
                        command = stringBuilder4.toString();
                    }
                    if (((RecoverySystem) context.getSystemService("recovery")).setupBcb(command)) {
                        ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).reboot(PowerManager.REBOOT_RECOVERY_UPDATE);
                        throw new IOException("Reboot failed (no permissions?)");
                    }
                    throw new IOException("Setup BCB failed");
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                String str2 = secrect;
                throw th;
            }
        }
        throw th;
    }

    @SystemApi
    public static void scheduleUpdateOnBoot(Context context, File packageFile) throws IOException {
        String filename = packageFile.getCanonicalPath();
        boolean securityUpdate = filename.endsWith("_s.zip");
        if (filename.startsWith("/data/")) {
            filename = "@/cache/recovery/block.map";
        }
        String filenameArg = new StringBuilder();
        filenameArg.append("--update_package=");
        filenameArg.append(filename);
        String str = "\n";
        filenameArg.append(str);
        filenameArg = filenameArg.toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("--locale=");
        stringBuilder.append(Locale.getDefault().toLanguageTag());
        stringBuilder.append(str);
        str = stringBuilder.toString();
        String securityArg = "--security\n";
        String command = new StringBuilder();
        command.append(filenameArg);
        command.append(str);
        command = command.toString();
        if (securityUpdate) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(command);
            stringBuilder2.append("--security\n");
            command = stringBuilder2.toString();
        }
        if (!((RecoverySystem) context.getSystemService("recovery")).setupBcb(command)) {
            throw new IOException("schedule update on boot failed");
        }
    }

    @SystemApi
    public static void cancelScheduledUpdate(Context context) throws IOException {
        if (!((RecoverySystem) context.getSystemService("recovery")).clearBcb()) {
            throw new IOException("cancel scheduled update failed");
        }
    }

    public static void rebootWipeUserData(Context context) throws IOException {
        rebootWipeUserData(context, false, context.getPackageName(), false, false);
    }

    public static void rebootWipeUserData(Context context, String reason) throws IOException {
        rebootWipeUserData(context, false, reason, false, false);
    }

    public static void rebootWipeUserData(Context context, boolean shutdown) throws IOException {
        rebootWipeUserData(context, shutdown, context.getPackageName(), false, false);
    }

    public static void rebootWipeUserData(Context context, boolean shutdown, String reason, boolean force) throws IOException {
        rebootWipeUserData(context, shutdown, reason, force, false);
    }

    public static void rebootWipeUserData(Context context, boolean shutdown, String reason, boolean force, boolean wipeEuicc) throws IOException {
        Context context2 = context;
        UserManager um = (UserManager) context.getSystemService("user");
        if (force || !um.hasUserRestriction(UserManager.DISALLOW_FACTORY_RESET)) {
            String timeStamp;
            final ConditionVariable condition = new ConditionVariable();
            Intent intent = new Intent(Intent.ACTION_MASTER_CLEAR_NOTIFICATION);
            intent.addFlags(285212672);
            Context context3 = context;
            Intent intent2 = intent;
            context3.sendOrderedBroadcastAsUser(intent2, UserHandle.SYSTEM, permission.MASTER_CLEAR, new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    condition.open();
                }
            }, null, 0, null, null);
            condition.block();
            if (wipeEuicc) {
                wipeEuiccData(context, "android");
            }
            String shutdownArg = null;
            if (shutdown) {
                shutdownArg = "--shutdown_after";
            }
            String reasonArg = null;
            String str;
            if (TextUtils.isEmpty(reason)) {
                str = reason;
            } else {
                timeStamp = DateFormat.format((CharSequence) "yyyy-MM-ddTHH:mm:ssZ", System.currentTimeMillis()).toString();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("--reason=");
                StringBuilder stringBuilder2 = new StringBuilder();
                str = reason;
                stringBuilder2.append(reason);
                stringBuilder2.append(",");
                stringBuilder2.append(timeStamp);
                stringBuilder.append(sanitizeArg(stringBuilder2.toString()));
                reasonArg = stringBuilder.toString();
            }
            timeStamp = new StringBuilder();
            timeStamp.append("--locale=");
            timeStamp.append(Locale.getDefault().toLanguageTag());
            timeStamp = timeStamp.toString();
            bootCommand(context, shutdownArg, "--wipe_data", reasonArg, timeStamp);
            return;
        }
        throw new SecurityException("Wiping data is not allowed for this user.");
    }

    public static boolean wipeEuiccData(Context context, String packageName) {
        InterruptedException e;
        Throwable th;
        CountDownLatch countDownLatch;
        Context context2 = context;
        int i = Global.getInt(context.getContentResolver(), Global.EUICC_PROVISIONED, 0);
        String str = TAG;
        if (i == 0) {
            Log.d(str, "Skipping eUICC wipe/retain as it is not provisioned");
            return true;
        }
        EuiccManager euiccManager = (EuiccManager) context2.getSystemService(Context.EUICC_SERVICE);
        if (euiccManager == null || !euiccManager.isEnabled()) {
            String str2 = packageName;
            return false;
        }
        final CountDownLatch euiccFactoryResetLatch = new CountDownLatch(1);
        final AtomicBoolean wipingSucceeded = new AtomicBoolean(false);
        BroadcastReceiver euiccWipeFinishReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (RecoverySystem.ACTION_EUICC_FACTORY_RESET.equals(intent.getAction())) {
                    int resultCode = getResultCode();
                    String str = RecoverySystem.TAG;
                    if (resultCode != 0) {
                        resultCode = intent.getIntExtra(EuiccManager.EXTRA_EMBEDDED_SUBSCRIPTION_DETAILED_CODE, 0);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Error wiping euicc data, Detailed code = ");
                        stringBuilder.append(resultCode);
                        Log.e(str, stringBuilder.toString());
                    } else {
                        Log.d(str, "Successfully wiped euicc data.");
                        wipingSucceeded.set(true);
                    }
                    euiccFactoryResetLatch.countDown();
                }
            }
        };
        String str3 = ACTION_EUICC_FACTORY_RESET;
        Intent intent = new Intent(str3);
        intent.setPackage(packageName);
        PendingIntent callbackIntent = PendingIntent.getBroadcastAsUser(context2, 0, intent, 134217728, UserHandle.SYSTEM);
        IntentFilter filterConsent = new IntentFilter();
        filterConsent.addAction(str3);
        HandlerThread euiccHandlerThread = new HandlerThread("euiccWipeFinishReceiverThread");
        euiccHandlerThread.start();
        context.getApplicationContext().registerReceiver(euiccWipeFinishReceiver, filterConsent, null, new Handler(euiccHandlerThread.getLooper()));
        euiccManager.eraseSubscriptions(callbackIntent);
        try {
            CountDownLatch euiccFactoryResetLatch2 = euiccFactoryResetLatch;
            try {
                long waitingTimeMillis = Global.getLong(context.getContentResolver(), Global.EUICC_FACTORY_RESET_TIMEOUT_MILLIS, 30000);
                if (waitingTimeMillis < 5000) {
                    waitingTimeMillis = 5000;
                } else if (waitingTimeMillis > 60000) {
                    waitingTimeMillis = 60000;
                }
                try {
                    if (euiccFactoryResetLatch2.await(waitingTimeMillis, TimeUnit.MILLISECONDS)) {
                        context.getApplicationContext().unregisterReceiver(euiccWipeFinishReceiver);
                        return wipingSucceeded.get();
                    }
                    Log.e(str, "Timeout wiping eUICC data.");
                    context.getApplicationContext().unregisterReceiver(euiccWipeFinishReceiver);
                    return false;
                } catch (InterruptedException e2) {
                    e = e2;
                    try {
                        Thread.currentThread().interrupt();
                        Log.e(str, "Wiping eUICC data interrupted", e);
                        context.getApplicationContext().unregisterReceiver(euiccWipeFinishReceiver);
                        return false;
                    } catch (Throwable th2) {
                        th = th2;
                        context.getApplicationContext().unregisterReceiver(euiccWipeFinishReceiver);
                        throw th;
                    }
                }
            } catch (InterruptedException e3) {
                e = e3;
                countDownLatch = euiccFactoryResetLatch2;
                Thread.currentThread().interrupt();
                Log.e(str, "Wiping eUICC data interrupted", e);
                context.getApplicationContext().unregisterReceiver(euiccWipeFinishReceiver);
                return false;
            } catch (Throwable th3) {
                th = th3;
                countDownLatch = euiccFactoryResetLatch2;
                context.getApplicationContext().unregisterReceiver(euiccWipeFinishReceiver);
                throw th;
            }
        } catch (InterruptedException e4) {
            e = e4;
            countDownLatch = euiccFactoryResetLatch;
            Thread.currentThread().interrupt();
            Log.e(str, "Wiping eUICC data interrupted", e);
            context.getApplicationContext().unregisterReceiver(euiccWipeFinishReceiver);
            return false;
        } catch (Throwable th4) {
            th = th4;
            countDownLatch = euiccFactoryResetLatch;
            context.getApplicationContext().unregisterReceiver(euiccWipeFinishReceiver);
            throw th;
        }
    }

    public static void rebootPromptAndWipeUserData(Context context, String reason) throws IOException {
        String str = "rescueparty";
        String str2 = TAG;
        boolean checkpointing = false;
        IVold vold = null;
        try {
            vold = IVold.Stub.asInterface(ServiceManager.checkService("vold"));
            if (vold != null) {
                checkpointing = vold.needsCheckpoint();
            } else {
                Log.w(str2, "Failed to get vold");
            }
        } catch (Exception e) {
            Log.w(str2, "Failed to check for checkpointing");
        }
        if (checkpointing) {
            try {
                vold.abortChanges(str, false);
                Log.i(str2, "Rescue Party requested wipe. Aborting update");
            } catch (Exception e2) {
                Log.i(str2, "Rescue Party requested wipe. Rebooting instead.");
                ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).reboot(str);
            }
            return;
        }
        str = null;
        if (!TextUtils.isEmpty(reason)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("--reason=");
            stringBuilder.append(sanitizeArg(reason));
            str = stringBuilder.toString();
        }
        str2 = new StringBuilder();
        str2.append("--locale=");
        str2.append(Locale.getDefault().toString());
        str2 = str2.toString();
        bootCommand(context, null, "--prompt_and_wipe_data", str, str2);
    }

    public static void rebootWipeCache(Context context) throws IOException {
        rebootWipeCache(context, context.getPackageName());
    }

    public static void rebootWipeCache(Context context, String reason) throws IOException {
        String reasonArg = null;
        if (!TextUtils.isEmpty(reason)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("--reason=");
            stringBuilder.append(sanitizeArg(reason));
            reasonArg = stringBuilder.toString();
        }
        String localeArg = new StringBuilder();
        localeArg.append("--locale=");
        localeArg.append(Locale.getDefault().toLanguageTag());
        localeArg = localeArg.toString();
        bootCommand(context, "--wipe_cache", reasonArg, localeArg);
    }

    @SystemApi
    public static void rebootWipeAb(Context context, File packageFile, String reason) throws IOException {
        String reasonArg = null;
        if (!TextUtils.isEmpty(reason)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("--reason=");
            stringBuilder.append(sanitizeArg(reason));
            reasonArg = stringBuilder.toString();
        }
        String filename = packageFile.getCanonicalPath();
        String filenameArg = new StringBuilder();
        filenameArg.append("--wipe_package=");
        filenameArg.append(filename);
        filenameArg = filenameArg.toString();
        String localeArg = new StringBuilder();
        localeArg.append("--locale=");
        localeArg.append(Locale.getDefault().toLanguageTag());
        localeArg = localeArg.toString();
        bootCommand(context, "--wipe_ab", filenameArg, reasonArg, localeArg);
    }

    private static void bootCommand(Context context, String... args) throws IOException {
        LOG_FILE.delete();
        StringBuilder command = new StringBuilder();
        for (String arg : args) {
            if (!TextUtils.isEmpty(arg)) {
                command.append(arg);
                command.append("\n");
            }
        }
        ((RecoverySystem) context.getSystemService("recovery")).rebootRecoveryWithCommand(command.toString());
        throw new IOException("Reboot failed (no permissions?)");
    }

    public static String handleAftermath(Context context) {
        String str = TAG;
        String log = null;
        try {
            log = FileUtils.readTextFile(LOG_FILE, -65536, "...\n");
        } catch (FileNotFoundException e) {
            Log.i(str, "No recovery log file");
        } catch (IOException e2) {
            Log.e(str, "Error reading recovery log", e2);
        }
        boolean reservePackage = BLOCK_MAP_FILE.exists();
        if (!reservePackage && UNCRYPT_PACKAGE_FILE.exists()) {
            String filename = null;
            try {
                filename = FileUtils.readTextFile(UNCRYPT_PACKAGE_FILE, 0, null);
            } catch (IOException e3) {
                Log.e(str, "Error reading uncrypt file", e3);
            }
            if (filename != null && filename.startsWith("/data")) {
                StringBuilder stringBuilder;
                if (UNCRYPT_PACKAGE_FILE.delete()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Deleted: ");
                    stringBuilder.append(filename);
                    Log.i(str, stringBuilder.toString());
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Can't delete: ");
                    stringBuilder.append(filename);
                    Log.e(str, stringBuilder.toString());
                }
            }
        }
        String[] names = RECOVERY_DIR.list();
        int i = 0;
        while (names != null && i < names.length) {
            if (!(names[i].startsWith(LAST_PREFIX) || names[i].equals(LAST_INSTALL_PATH) || ((reservePackage && names[i].equals(BLOCK_MAP_FILE.getName())) || ((reservePackage && names[i].equals(UNCRYPT_PACKAGE_FILE.getName())) || names[i].equals("efs"))))) {
                recursiveDelete(new File(RECOVERY_DIR, names[i]));
            }
            i++;
        }
        return log;
    }

    private static void recursiveDelete(File name) {
        if (name.isDirectory()) {
            String[] files = name.list();
            int i = 0;
            while (files != null && i < files.length) {
                recursiveDelete(new File(name, files[i]));
                i++;
            }
        }
        boolean delete = name.delete();
        String str = TAG;
        StringBuilder stringBuilder;
        if (delete) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Deleted: ");
            stringBuilder.append(name);
            Log.i(str, stringBuilder.toString());
            return;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Can't delete: ");
        stringBuilder.append(name);
        Log.e(str, stringBuilder.toString());
    }

    private boolean uncrypt(String packageFile, IRecoverySystemProgressListener listener) {
        try {
            return this.mService.uncrypt(packageFile, listener);
        } catch (RemoteException e) {
            return false;
        }
    }

    private boolean setupBcb(String command) {
        try {
            return this.mService.setupBcb(command);
        } catch (RemoteException e) {
            return false;
        }
    }

    private boolean clearBcb() {
        try {
            return this.mService.clearBcb();
        } catch (RemoteException e) {
            return false;
        }
    }

    private void rebootRecoveryWithCommand(String command) {
        try {
            this.mService.rebootRecoveryWithCommand(command);
        } catch (RemoteException e) {
        }
    }

    private static String sanitizeArg(String arg) {
        return arg.replace(0, '?').replace(10, '?');
    }

    public RecoverySystem() {
        this.mService = null;
    }

    public RecoverySystem(IRecoverySystem service) {
        this.mService = service;
    }
}
