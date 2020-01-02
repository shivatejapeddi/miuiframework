package android.security.net.config;

import java.io.File;
import java.security.cert.X509Certificate;
import java.util.Set;

public final class WfaCertificateSource extends DirectoryCertificateSource {

    private static class NoPreloadHolder {
        private static final WfaCertificateSource INSTANCE = new WfaCertificateSource();

        private NoPreloadHolder() {
        }
    }

    public /* bridge */ /* synthetic */ Set findAllByIssuerAndSignature(X509Certificate x509Certificate) {
        return super.findAllByIssuerAndSignature(x509Certificate);
    }

    public /* bridge */ /* synthetic */ X509Certificate findByIssuerAndSignature(X509Certificate x509Certificate) {
        return super.findByIssuerAndSignature(x509Certificate);
    }

    public /* bridge */ /* synthetic */ X509Certificate findBySubjectAndPublicKey(X509Certificate x509Certificate) {
        return super.findBySubjectAndPublicKey(x509Certificate);
    }

    public /* bridge */ /* synthetic */ Set getCertificates() {
        return super.getCertificates();
    }

    public /* bridge */ /* synthetic */ void handleTrustStorageUpdate() {
        super.handleTrustStorageUpdate();
    }

    private WfaCertificateSource() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.getenv("ANDROID_ROOT"));
        stringBuilder.append("/etc/security/cacerts_wfa");
        super(new File(stringBuilder.toString()));
    }

    public static WfaCertificateSource getInstance() {
        return NoPreloadHolder.INSTANCE;
    }

    /* Access modifiers changed, original: protected */
    public boolean isCertMarkedAsRemoved(String caFile) {
        return false;
    }
}
