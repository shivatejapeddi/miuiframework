package android.security.net.config;

import android.security.keystore.KeyProperties;
import android.text.format.DateFormat;
import android.util.ArraySet;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import libcore.io.IoUtils;

abstract class DirectoryCertificateSource implements CertificateSource {
    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', DateFormat.AM_PM, 'b', 'c', DateFormat.DATE, 'e', 'f'};
    private static final String LOG_TAG = "DirectoryCertificateSrc";
    private final CertificateFactory mCertFactory;
    private Set<X509Certificate> mCertificates;
    private final File mDir;
    private final Object mLock = new Object();

    private interface CertSelector {
        boolean match(X509Certificate x509Certificate);
    }

    public abstract boolean isCertMarkedAsRemoved(String str);

    protected DirectoryCertificateSource(File caDir) {
        this.mDir = caDir;
        try {
            this.mCertFactory = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            throw new RuntimeException("Failed to obtain X.509 CertificateFactory", e);
        }
    }

    public Set<X509Certificate> getCertificates() {
        synchronized (this.mLock) {
            if (this.mCertificates != null) {
                return this.mCertificates;
            }
            Set<X509Certificate> certs = new ArraySet();
            if (this.mDir.isDirectory()) {
                for (String caFile : this.mDir.list()) {
                    if (!isCertMarkedAsRemoved(caFile)) {
                        X509Certificate cert = readCertificate(caFile);
                        if (cert != null) {
                            certs.add(cert);
                        }
                    }
                }
            }
            this.mCertificates = certs;
            return this.mCertificates;
        }
    }

    public X509Certificate findBySubjectAndPublicKey(final X509Certificate cert) {
        return findCert(cert.getSubjectX500Principal(), new CertSelector() {
            public boolean match(X509Certificate ca) {
                return ca.getPublicKey().equals(cert.getPublicKey());
            }
        });
    }

    public X509Certificate findByIssuerAndSignature(final X509Certificate cert) {
        return findCert(cert.getIssuerX500Principal(), new CertSelector() {
            public boolean match(X509Certificate ca) {
                try {
                    cert.verify(ca.getPublicKey());
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    public Set<X509Certificate> findAllByIssuerAndSignature(final X509Certificate cert) {
        return findCerts(cert.getIssuerX500Principal(), new CertSelector() {
            public boolean match(X509Certificate ca) {
                try {
                    cert.verify(ca.getPublicKey());
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    public void handleTrustStorageUpdate() {
        synchronized (this.mLock) {
            this.mCertificates = null;
        }
    }

    private Set<X509Certificate> findCerts(X500Principal subj, CertSelector selector) {
        String hash = getHash(subj);
        Set<X509Certificate> certs = null;
        for (int index = 0; index >= 0; index++) {
            String fileName = new StringBuilder();
            fileName.append(hash);
            fileName.append(".");
            fileName.append(index);
            fileName = fileName.toString();
            if (!new File(this.mDir, fileName).exists()) {
                break;
            }
            if (!isCertMarkedAsRemoved(fileName)) {
                X509Certificate cert = readCertificate(fileName);
                if (cert != null && subj.equals(cert.getSubjectX500Principal()) && selector.match(cert)) {
                    if (certs == null) {
                        certs = new ArraySet();
                    }
                    certs.add(cert);
                }
            }
        }
        return certs != null ? certs : Collections.emptySet();
    }

    private X509Certificate findCert(X500Principal subj, CertSelector selector) {
        String hash = getHash(subj);
        for (int index = 0; index >= 0; index++) {
            String fileName = new StringBuilder();
            fileName.append(hash);
            fileName.append(".");
            fileName.append(index);
            fileName = fileName.toString();
            if (!new File(this.mDir, fileName).exists()) {
                break;
            }
            if (!isCertMarkedAsRemoved(fileName)) {
                X509Certificate cert = readCertificate(fileName);
                if (cert != null && subj.equals(cert.getSubjectX500Principal()) && selector.match(cert)) {
                    return cert;
                }
            }
        }
        return null;
    }

    private String getHash(X500Principal name) {
        return intToHexString(hashName(name), 8);
    }

    private static String intToHexString(int i, int minWidth) {
        char[] buf = new char[8];
        int cursor = 8;
        while (true) {
            cursor--;
            buf[cursor] = DIGITS[i & 15];
            int i2 = i >>> 4;
            i = i2;
            if (i2 == 0 && 8 - cursor >= minWidth) {
                return new String(buf, cursor, 8 - cursor);
            }
        }
    }

    private static int hashName(X500Principal principal) {
        try {
            byte[] digest = MessageDigest.getInstance(KeyProperties.DIGEST_MD5).digest(principal.getEncoded());
            int offset = 0 + 1;
            int offset2 = offset + 1;
            int i = ((digest[0] & 255) << 0) | ((digest[offset] & 255) << 8);
            return (i | ((digest[offset2] & 255) << 16)) | ((digest[offset2 + 1] & 255) << 24);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    private X509Certificate readCertificate(String file) {
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(new File(this.mDir, file)));
            X509Certificate x509Certificate = (X509Certificate) this.mCertFactory.generateCertificate(is);
            IoUtils.closeQuietly(is);
            return x509Certificate;
        } catch (IOException | CertificateException e) {
            String str = LOG_TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to read certificate from ");
            stringBuilder.append(file);
            Log.e(str, stringBuilder.toString(), e);
            IoUtils.closeQuietly(is);
            return null;
        } catch (Throwable th) {
            IoUtils.closeQuietly(is);
            throw th;
        }
    }
}
