package android.security;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.os.Process;
import android.os.UserHandle;
import android.security.IKeyChainAliasCallback.Stub;
import android.security.keystore.KeyProperties;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.Serializable;
import java.security.KeyPair;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.security.auth.x500.X500Principal;

public final class KeyChain {
    public static final String ACCOUNT_TYPE = "com.android.keychain";
    private static final String ACTION_CHOOSER = "com.android.keychain.CHOOSER";
    private static final String ACTION_INSTALL = "android.credentials.INSTALL";
    public static final String ACTION_KEYCHAIN_CHANGED = "android.security.action.KEYCHAIN_CHANGED";
    public static final String ACTION_KEY_ACCESS_CHANGED = "android.security.action.KEY_ACCESS_CHANGED";
    public static final String ACTION_STORAGE_CHANGED = "android.security.STORAGE_CHANGED";
    public static final String ACTION_TRUST_STORE_CHANGED = "android.security.action.TRUST_STORE_CHANGED";
    private static final String CERT_INSTALLER_PACKAGE = "com.android.certinstaller";
    public static final String EXTRA_ALIAS = "alias";
    public static final String EXTRA_CERTIFICATE = "CERT";
    public static final String EXTRA_ISSUERS = "issuers";
    public static final String EXTRA_KEY_ACCESSIBLE = "android.security.extra.KEY_ACCESSIBLE";
    public static final String EXTRA_KEY_ALIAS = "android.security.extra.KEY_ALIAS";
    public static final String EXTRA_KEY_TYPES = "key_types";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_PKCS12 = "PKCS12";
    public static final String EXTRA_RESPONSE = "response";
    public static final String EXTRA_SENDER = "sender";
    public static final String EXTRA_URI = "uri";
    private static final String KEYCHAIN_PACKAGE = "com.android.keychain";
    public static final int KEY_ATTESTATION_CANNOT_ATTEST_IDS = 3;
    public static final int KEY_ATTESTATION_CANNOT_COLLECT_DATA = 2;
    public static final int KEY_ATTESTATION_FAILURE = 4;
    public static final int KEY_ATTESTATION_MISSING_CHALLENGE = 1;
    public static final int KEY_ATTESTATION_SUCCESS = 0;
    public static final int KEY_GEN_FAILURE = 7;
    public static final int KEY_GEN_INVALID_ALGORITHM_PARAMETERS = 4;
    public static final int KEY_GEN_MISSING_ALIAS = 1;
    public static final int KEY_GEN_NO_KEYSTORE_PROVIDER = 5;
    public static final int KEY_GEN_NO_SUCH_ALGORITHM = 3;
    public static final int KEY_GEN_STRONGBOX_UNAVAILABLE = 6;
    public static final int KEY_GEN_SUCCESS = 0;
    public static final int KEY_GEN_SUPERFLUOUS_ATTESTATION_CHALLENGE = 2;

    private static class AliasResponse extends Stub {
        private final KeyChainAliasCallback keyChainAliasResponse;

        /* synthetic */ AliasResponse(KeyChainAliasCallback x0, AnonymousClass1 x1) {
            this(x0);
        }

        private AliasResponse(KeyChainAliasCallback keyChainAliasResponse) {
            this.keyChainAliasResponse = keyChainAliasResponse;
        }

        public void alias(String alias) {
            this.keyChainAliasResponse.alias(alias);
        }
    }

    public static class KeyChainConnection implements Closeable {
        private final Context context;
        private final IKeyChainService service;
        private final ServiceConnection serviceConnection;

        protected KeyChainConnection(Context context, ServiceConnection serviceConnection, IKeyChainService service) {
            this.context = context;
            this.serviceConnection = serviceConnection;
            this.service = service;
        }

        public void close() {
            this.context.unbindService(this.serviceConnection);
        }

        public IKeyChainService getService() {
            return this.service;
        }
    }

    public static Intent createInstallIntent() {
        Intent intent = new Intent("android.credentials.INSTALL");
        intent.setClassName(CERT_INSTALLER_PACKAGE, "com.android.certinstaller.CertInstallerMain");
        return intent;
    }

    public static void choosePrivateKeyAlias(Activity activity, KeyChainAliasCallback response, String[] keyTypes, Principal[] issuers, String host, int port, String alias) {
        Uri uri = null;
        if (host != null) {
            String stringBuilder;
            Builder builder = new Builder();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(host);
            if (port != -1) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(":");
                stringBuilder3.append(port);
                stringBuilder = stringBuilder3.toString();
            } else {
                stringBuilder = "";
            }
            stringBuilder2.append(stringBuilder);
            uri = builder.authority(stringBuilder2.toString()).build();
        }
        choosePrivateKeyAlias(activity, response, keyTypes, issuers, uri, alias);
    }

    public static void choosePrivateKeyAlias(Activity activity, KeyChainAliasCallback response, String[] keyTypes, Principal[] issuers, Uri uri, String alias) {
        if (activity == null) {
            throw new NullPointerException("activity == null");
        } else if (response != null) {
            Intent intent = new Intent(ACTION_CHOOSER);
            intent.setPackage("com.android.keychain");
            intent.putExtra("response", new AliasResponse(response, null));
            intent.putExtra("uri", (Parcelable) uri);
            intent.putExtra(EXTRA_ALIAS, alias);
            intent.putExtra(EXTRA_KEY_TYPES, keyTypes);
            Serializable issuersList = new ArrayList();
            if (issuers != null) {
                int length = issuers.length;
                int i = 0;
                while (i < length) {
                    Principal issuer = issuers[i];
                    if (issuer instanceof X500Principal) {
                        issuersList.add(((X500Principal) issuer).getEncoded());
                        i++;
                    } else {
                        throw new IllegalArgumentException(String.format("Issuer %s is of type %s, not X500Principal", new Object[]{issuer.toString(), issuer.getClass()}));
                    }
                }
            }
            intent.putExtra(EXTRA_ISSUERS, issuersList);
            intent.putExtra(EXTRA_SENDER, PendingIntent.getActivity(activity, 0, new Intent(), 0));
            activity.startActivity(intent);
        } else {
            throw new NullPointerException("response == null");
        }
    }

    public static PrivateKey getPrivateKey(Context context, String alias) throws KeyChainException, InterruptedException {
        KeyPair keyPair = getKeyPair(context, alias);
        if (keyPair != null) {
            return keyPair.getPrivate();
        }
        return null;
    }

    /* JADX WARNING: Missing block: B:21:0x0030, code skipped:
            if (r0 != null) goto L_0x0032;
     */
    /* JADX WARNING: Missing block: B:23:?, code skipped:
            $closeResource(r1, r0);
     */
    public static java.security.KeyPair getKeyPair(android.content.Context r3, java.lang.String r4) throws android.security.KeyChainException, java.lang.InterruptedException {
        /*
        if (r4 == 0) goto L_0x004c;
    L_0x0002:
        if (r3 == 0) goto L_0x0044;
    L_0x0004:
        r0 = r3.getApplicationContext();	 Catch:{ RemoteException -> 0x003d, RuntimeException -> 0x0036 }
        r0 = bind(r0);	 Catch:{ RemoteException -> 0x003d, RuntimeException -> 0x0036 }
        r1 = r0.getService();	 Catch:{ all -> 0x002d }
        r1 = r1.requestPrivateKey(r4);	 Catch:{ all -> 0x002d }
        r2 = 0;
        $closeResource(r2, r0);	 Catch:{ RemoteException -> 0x003d, RuntimeException -> 0x0036 }
        if (r1 != 0) goto L_0x001c;
    L_0x001b:
        return r2;
    L_0x001c:
        r0 = android.security.KeyStore.getInstance();	 Catch:{ KeyPermanentlyInvalidatedException | RuntimeException | UnrecoverableKeyException -> 0x0026, KeyPermanentlyInvalidatedException | RuntimeException | UnrecoverableKeyException -> 0x0026, KeyPermanentlyInvalidatedException | RuntimeException | UnrecoverableKeyException -> 0x0026 }
        r2 = -1;
        r0 = android.security.keystore.AndroidKeyStoreProvider.loadAndroidKeyStoreKeyPairFromKeystore(r0, r1, r2);	 Catch:{ KeyPermanentlyInvalidatedException | RuntimeException | UnrecoverableKeyException -> 0x0026, KeyPermanentlyInvalidatedException | RuntimeException | UnrecoverableKeyException -> 0x0026, KeyPermanentlyInvalidatedException | RuntimeException | UnrecoverableKeyException -> 0x0026 }
        return r0;
    L_0x0026:
        r0 = move-exception;
        r2 = new android.security.KeyChainException;
        r2.<init>(r0);
        throw r2;
    L_0x002d:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x002f }
    L_0x002f:
        r2 = move-exception;
        if (r0 == 0) goto L_0x0035;
    L_0x0032:
        $closeResource(r1, r0);	 Catch:{ RemoteException -> 0x003d, RuntimeException -> 0x0036 }
    L_0x0035:
        throw r2;	 Catch:{ RemoteException -> 0x003d, RuntimeException -> 0x0036 }
    L_0x0036:
        r0 = move-exception;
        r1 = new android.security.KeyChainException;
        r1.<init>(r0);
        throw r1;
    L_0x003d:
        r0 = move-exception;
        r1 = new android.security.KeyChainException;
        r1.<init>(r0);
        throw r1;
    L_0x0044:
        r0 = new java.lang.NullPointerException;
        r1 = "context == null";
        r0.<init>(r1);
        throw r0;
    L_0x004c:
        r0 = new java.lang.NullPointerException;
        r1 = "alias == null";
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.security.KeyChain.getKeyPair(android.content.Context, java.lang.String):java.security.KeyPair");
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

    /* JADX WARNING: Missing block: B:31:0x006e, code skipped:
            if (r0 != null) goto L_0x0070;
     */
    /* JADX WARNING: Missing block: B:33:?, code skipped:
            $closeResource(r1, r0);
     */
    public static java.security.cert.X509Certificate[] getCertificateChain(android.content.Context r6, java.lang.String r7) throws android.security.KeyChainException, java.lang.InterruptedException {
        /*
        if (r7 == 0) goto L_0x0082;
    L_0x0002:
        r0 = r6.getApplicationContext();	 Catch:{ RemoteException -> 0x007b, RuntimeException -> 0x0074 }
        r0 = bind(r0);	 Catch:{ RemoteException -> 0x007b, RuntimeException -> 0x0074 }
        r1 = r0.getService();	 Catch:{ all -> 0x006b }
        r2 = r1.getCertificate(r7);	 Catch:{ all -> 0x006b }
        r3 = 0;
        if (r2 != 0) goto L_0x001a;
        $closeResource(r3, r0);	 Catch:{ RemoteException -> 0x007b, RuntimeException -> 0x0074 }
        return r3;
    L_0x001a:
        r4 = r1.getCaCertificates(r7);	 Catch:{ all -> 0x006b }
        r1 = r4;
        $closeResource(r3, r0);	 Catch:{ RemoteException -> 0x007b, RuntimeException -> 0x0074 }
        r0 = toCertificate(r2);	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        if (r1 == 0) goto L_0x004e;
    L_0x0029:
        r3 = r1.length;	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        if (r3 == 0) goto L_0x004e;
    L_0x002c:
        r3 = toCertificates(r1);	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        r4 = new java.util.ArrayList;	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        r5 = r3.size();	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        r5 = r5 + 1;
        r4.<init>(r5);	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        r4.add(r0);	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        r4.addAll(r3);	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        r5 = r4.size();	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        r5 = new java.security.cert.X509Certificate[r5];	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        r5 = r4.toArray(r5);	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        r5 = (java.security.cert.X509Certificate[]) r5;	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        return r5;
    L_0x004e:
        r3 = new com.android.org.conscrypt.TrustedCertificateStore;	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        r3.<init>();	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        r4 = r3.getCertificateChain(r0);	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        r5 = r4.size();	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        r5 = new java.security.cert.X509Certificate[r5];	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        r5 = r4.toArray(r5);	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        r5 = (java.security.cert.X509Certificate[]) r5;	 Catch:{ RuntimeException | CertificateException -> 0x0064, RuntimeException | CertificateException -> 0x0064 }
        return r5;
    L_0x0064:
        r0 = move-exception;
        r3 = new android.security.KeyChainException;
        r3.<init>(r0);
        throw r3;
    L_0x006b:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x006d }
    L_0x006d:
        r2 = move-exception;
        if (r0 == 0) goto L_0x0073;
    L_0x0070:
        $closeResource(r1, r0);	 Catch:{ RemoteException -> 0x007b, RuntimeException -> 0x0074 }
    L_0x0073:
        throw r2;	 Catch:{ RemoteException -> 0x007b, RuntimeException -> 0x0074 }
    L_0x0074:
        r0 = move-exception;
        r1 = new android.security.KeyChainException;
        r1.<init>(r0);
        throw r1;
    L_0x007b:
        r0 = move-exception;
        r1 = new android.security.KeyChainException;
        r1.<init>(r0);
        throw r1;
    L_0x0082:
        r0 = new java.lang.NullPointerException;
        r1 = "alias == null";
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.security.KeyChain.getCertificateChain(android.content.Context, java.lang.String):java.security.cert.X509Certificate[]");
    }

    public static boolean isKeyAlgorithmSupported(String algorithm) {
        String algUpper = algorithm.toUpperCase(Locale.US);
        return KeyProperties.KEY_ALGORITHM_EC.equals(algUpper) || KeyProperties.KEY_ALGORITHM_RSA.equals(algUpper);
    }

    @Deprecated
    public static boolean isBoundKeyAlgorithm(String algorithm) {
        if (isKeyAlgorithmSupported(algorithm)) {
            return KeyStore.getInstance().isHardwareBacked(algorithm);
        }
        return false;
    }

    public static X509Certificate toCertificate(byte[] bytes) {
        if (bytes != null) {
            try {
                return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(bytes));
            } catch (CertificateException e) {
                throw new AssertionError(e);
            }
        }
        throw new IllegalArgumentException("bytes == null");
    }

    public static Collection<X509Certificate> toCertificates(byte[] bytes) {
        if (bytes != null) {
            try {
                return CertificateFactory.getInstance("X.509").generateCertificates(new ByteArrayInputStream(bytes));
            } catch (CertificateException e) {
                throw new AssertionError(e);
            }
        }
        throw new IllegalArgumentException("bytes == null");
    }

    public static KeyChainConnection bind(Context context) throws InterruptedException {
        return bindAsUser(context, Process.myUserHandle());
    }

    public static KeyChainConnection bindAsUser(Context context, UserHandle user) throws InterruptedException {
        if (context != null) {
            ensureNotOnMainThread(context);
            final BlockingQueue<IKeyChainService> q = new LinkedBlockingQueue(1);
            ServiceConnection keyChainServiceConnection = new ServiceConnection() {
                volatile boolean mConnectedAtLeastOnce = false;

                public void onServiceConnected(ComponentName name, IBinder service) {
                    if (!this.mConnectedAtLeastOnce) {
                        this.mConnectedAtLeastOnce = true;
                        try {
                            q.put(IKeyChainService.Stub.asInterface(Binder.allowBlocking(service)));
                        } catch (InterruptedException e) {
                        }
                    }
                }

                public void onServiceDisconnected(ComponentName name) {
                }
            };
            Intent intent = new Intent(IKeyChainService.class.getName());
            ComponentName comp = intent.resolveSystemService(context.getPackageManager(), 0);
            intent.setComponent(comp);
            if (comp != null && context.bindServiceAsUser(intent, keyChainServiceConnection, 1, user)) {
                return new KeyChainConnection(context, keyChainServiceConnection, (IKeyChainService) q.take());
            }
            throw new AssertionError("could not bind to KeyChainService");
        }
        throw new NullPointerException("context == null");
    }

    private static void ensureNotOnMainThread(Context context) {
        Looper looper = Looper.myLooper();
        if (looper != null && looper == context.getMainLooper()) {
            throw new IllegalStateException("calling this from your main thread can lead to deadlock");
        }
    }
}
