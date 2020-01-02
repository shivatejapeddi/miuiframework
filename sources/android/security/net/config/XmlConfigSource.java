package android.security.net.config;

import android.app.slice.SliceProvider;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.XmlResourceParser;
import android.security.net.config.NetworkSecurityConfig.Builder;
import android.util.ArraySet;
import android.util.Base64;
import android.util.Pair;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlConfigSource implements ConfigSource {
    private static final int CONFIG_BASE = 0;
    private static final int CONFIG_DEBUG = 2;
    private static final int CONFIG_DOMAIN = 1;
    private final ApplicationInfo mApplicationInfo;
    private Context mContext;
    private final boolean mDebugBuild;
    private NetworkSecurityConfig mDefaultConfig;
    private Set<Pair<Domain, NetworkSecurityConfig>> mDomainMap;
    private boolean mInitialized;
    private final Object mLock = new Object();
    private final int mResourceId;

    public static class ParserException extends Exception {
        public ParserException(XmlPullParser parser, String message, Throwable cause) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(message);
            stringBuilder.append(" at: ");
            stringBuilder.append(parser.getPositionDescription());
            super(stringBuilder.toString(), cause);
        }

        public ParserException(XmlPullParser parser, String message) {
            this(parser, message, null);
        }
    }

    public XmlConfigSource(Context context, int resourceId, ApplicationInfo info) {
        this.mContext = context;
        this.mResourceId = resourceId;
        this.mApplicationInfo = new ApplicationInfo(info);
        this.mDebugBuild = (this.mApplicationInfo.flags & 2) != 0;
    }

    public Set<Pair<Domain, NetworkSecurityConfig>> getPerDomainConfigs() {
        ensureInitialized();
        return this.mDomainMap;
    }

    public NetworkSecurityConfig getDefaultConfig() {
        ensureInitialized();
        return this.mDefaultConfig;
    }

    private static final String getConfigString(int configType) {
        if (configType == 0) {
            return "base-config";
        }
        if (configType == 1) {
            return "domain-config";
        }
        if (configType == 2) {
            return "debug-overrides";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown config type: ");
        stringBuilder.append(configType);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    /* JADX WARNING: Missing block: B:21:0x0029, code skipped:
            if (r1 != null) goto L_0x002b;
     */
    /* JADX WARNING: Missing block: B:23:?, code skipped:
            $closeResource(r2, r1);
     */
    private void ensureInitialized() {
        /*
        r6 = this;
        r0 = r6.mLock;
        monitor-enter(r0);
        r1 = r6.mInitialized;	 Catch:{ all -> 0x0053 }
        if (r1 == 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r0);	 Catch:{ all -> 0x0053 }
        return;
    L_0x0009:
        r1 = r6.mContext;	 Catch:{ NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f }
        r1 = r1.getResources();	 Catch:{ NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f }
        r2 = r6.mResourceId;	 Catch:{ NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f }
        r1 = r1.getXml(r2);	 Catch:{ NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f }
        r6.parseNetworkSecurityConfig(r1);	 Catch:{ all -> 0x0026 }
        r2 = 0;
        r6.mContext = r2;	 Catch:{ all -> 0x0026 }
        r3 = 1;
        r6.mInitialized = r3;	 Catch:{ all -> 0x0026 }
        if (r1 == 0) goto L_0x0023;
    L_0x0020:
        $closeResource(r2, r1);	 Catch:{ NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f }
        monitor-exit(r0);	 Catch:{ all -> 0x0053 }
        return;
    L_0x0026:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x0028 }
    L_0x0028:
        r3 = move-exception;
        if (r1 == 0) goto L_0x002e;
    L_0x002b:
        $closeResource(r2, r1);	 Catch:{ NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f }
    L_0x002e:
        throw r3;	 Catch:{ NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f, NotFoundException | ParserException | IOException | XmlPullParserException -> 0x002f }
    L_0x002f:
        r1 = move-exception;
        r2 = new java.lang.RuntimeException;	 Catch:{ all -> 0x0053 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0053 }
        r3.<init>();	 Catch:{ all -> 0x0053 }
        r4 = "Failed to parse XML configuration from ";
        r3.append(r4);	 Catch:{ all -> 0x0053 }
        r4 = r6.mContext;	 Catch:{ all -> 0x0053 }
        r4 = r4.getResources();	 Catch:{ all -> 0x0053 }
        r5 = r6.mResourceId;	 Catch:{ all -> 0x0053 }
        r4 = r4.getResourceEntryName(r5);	 Catch:{ all -> 0x0053 }
        r3.append(r4);	 Catch:{ all -> 0x0053 }
        r3 = r3.toString();	 Catch:{ all -> 0x0053 }
        r2.<init>(r3, r1);	 Catch:{ all -> 0x0053 }
        throw r2;	 Catch:{ all -> 0x0053 }
    L_0x0053:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0053 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.security.net.config.XmlConfigSource.ensureInitialized():void");
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

    private Pin parsePin(XmlResourceParser parser) throws IOException, XmlPullParserException, ParserException {
        String digestAlgorithm = parser.getAttributeValue(null, "digest");
        if (!Pin.isSupportedDigestAlgorithm(digestAlgorithm)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported pin digest algorithm: ");
            stringBuilder.append(digestAlgorithm);
            throw new ParserException(parser, stringBuilder.toString());
        } else if (parser.next() == 4) {
            try {
                byte[] decodedDigest = Base64.decode(parser.getText().trim(), 0);
                int expectedLength = Pin.getDigestLength(digestAlgorithm);
                if (decodedDigest.length != expectedLength) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("digest length ");
                    stringBuilder2.append(decodedDigest.length);
                    stringBuilder2.append(" does not match expected length for ");
                    stringBuilder2.append(digestAlgorithm);
                    stringBuilder2.append(" of ");
                    stringBuilder2.append(expectedLength);
                    throw new ParserException(parser, stringBuilder2.toString());
                } else if (parser.next() == 3) {
                    return new Pin(digestAlgorithm, decodedDigest);
                } else {
                    throw new ParserException(parser, "pin contains additional elements");
                }
            } catch (IllegalArgumentException e) {
                throw new ParserException(parser, "Invalid pin digest", e);
            }
        } else {
            throw new ParserException(parser, "Missing pin digest");
        }
    }

    private PinSet parsePinSet(XmlResourceParser parser) throws IOException, XmlPullParserException, ParserException {
        String str = "Invalid expiration date in pin-set";
        String expirationDate = parser.getAttributeValue(null, "expiration");
        long expirationTimestampMilis = Long.MAX_VALUE;
        if (expirationDate != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                Date date = sdf.parse(expirationDate);
                if (date != null) {
                    expirationTimestampMilis = date.getTime();
                } else {
                    throw new ParserException(parser, str);
                }
            } catch (ParseException e) {
                throw new ParserException(parser, str, e);
            }
        }
        int outerDepth = parser.getDepth();
        Set<Pin> pins = new ArraySet();
        while (XmlUtils.nextElementWithin(parser, outerDepth)) {
            if (parser.getName().equals(SliceProvider.METHOD_PIN)) {
                pins.add(parsePin(parser));
            } else {
                XmlUtils.skipCurrentTag(parser);
            }
        }
        return new PinSet(pins, expirationTimestampMilis);
    }

    private Domain parseDomain(XmlResourceParser parser, Set<String> seenDomains) throws IOException, XmlPullParserException, ParserException {
        boolean includeSubdomains = parser.getAttributeBooleanValue(false, "includeSubdomains", false);
        if (parser.next() == 4) {
            String domain = parser.getText().trim().toLowerCase(Locale.US);
            if (parser.next() != 3) {
                throw new ParserException(parser, "domain contains additional elements");
            } else if (seenDomains.add(domain)) {
                return new Domain(domain, includeSubdomains);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(domain);
                stringBuilder.append(" has already been specified");
                throw new ParserException(parser, stringBuilder.toString());
            }
        }
        throw new ParserException(parser, "Domain name missing");
    }

    private CertificatesEntryRef parseCertificatesEntry(XmlResourceParser parser, boolean defaultOverridePins) throws IOException, XmlPullParserException, ParserException {
        boolean overridePins = parser.getAttributeBooleanValue(null, "overridePins", defaultOverridePins);
        String str = "src";
        int sourceId = parser.getAttributeResourceValue(null, str, -1);
        String sourceString = parser.getAttributeValue(null, str);
        if (sourceString != null) {
            CertificateSource source;
            if (sourceId != -1) {
                source = new ResourceCertificateSource(sourceId, this.mContext);
            } else if ("system".equals(sourceString)) {
                source = SystemCertificateSource.getInstance();
            } else if ("user".equals(sourceString)) {
                source = UserCertificateSource.getInstance();
            } else if ("wfa".equals(sourceString)) {
                source = WfaCertificateSource.getInstance();
            } else {
                throw new ParserException(parser, "Unknown certificates src. Should be one of system|user|@resourceVal");
            }
            XmlUtils.skipCurrentTag(parser);
            return new CertificatesEntryRef(source, overridePins);
        }
        throw new ParserException(parser, "certificates element missing src attribute");
    }

    private Collection<CertificatesEntryRef> parseTrustAnchors(XmlResourceParser parser, boolean defaultOverridePins) throws IOException, XmlPullParserException, ParserException {
        int outerDepth = parser.getDepth();
        List<CertificatesEntryRef> anchors = new ArrayList();
        while (XmlUtils.nextElementWithin(parser, outerDepth)) {
            if (parser.getName().equals("certificates")) {
                anchors.add(parseCertificatesEntry(parser, defaultOverridePins));
            } else {
                XmlUtils.skipCurrentTag(parser);
            }
        }
        return anchors;
    }

    private List<Pair<Builder, Set<Domain>>> parseConfigEntry(XmlResourceParser parser, Set<String> seenDomains, Builder parentBuilder, int configType) throws IOException, XmlPullParserException, ParserException {
        Set<String> domain;
        XmlConfigSource xmlConfigSource = this;
        XmlResourceParser xmlResourceParser = parser;
        int i = configType;
        List<Pair<Builder, Set<Domain>>> builders = new ArrayList();
        Builder builder = new Builder();
        builder.setParent(parentBuilder);
        Set<Domain> domains = new ArraySet();
        boolean seenPinSet = false;
        boolean seenTrustAnchors = false;
        boolean z = false;
        boolean defaultOverridePins = i == 2;
        String configName = parser.getName();
        int outerDepth = parser.getDepth();
        builders.add(new Pair(builder, domains));
        int i2 = 0;
        while (i2 < parser.getAttributeCount()) {
            String name = xmlResourceParser.getAttributeName(i2);
            if ("hstsEnforced".equals(name)) {
                builder.setHstsEnforced(xmlResourceParser.getAttributeBooleanValue(i2, z));
            } else if ("cleartextTrafficPermitted".equals(name)) {
                builder.setCleartextTrafficPermitted(xmlResourceParser.getAttributeBooleanValue(i2, true));
            }
            i2++;
            z = false;
        }
        while (XmlUtils.nextElementWithin(xmlResourceParser, outerDepth)) {
            String tagName = parser.getName();
            StringBuilder stringBuilder;
            if ("domain".equals(tagName)) {
                if (i == 1) {
                    domains.add(parseDomain(parser, seenDomains));
                    domain = seenDomains;
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("domain element not allowed in ");
                    stringBuilder.append(getConfigString(configType));
                    throw new ParserException(xmlResourceParser, stringBuilder.toString());
                }
            } else if ("trust-anchors".equals(tagName)) {
                if (seenTrustAnchors) {
                    throw new ParserException(xmlResourceParser, "Multiple trust-anchor elements not allowed");
                }
                builder.addCertificatesEntryRefs(xmlConfigSource.parseTrustAnchors(xmlResourceParser, defaultOverridePins));
                seenTrustAnchors = true;
                domain = seenDomains;
            } else if ("pin-set".equals(tagName)) {
                if (i != 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("pin-set element not allowed in ");
                    stringBuilder.append(getConfigString(configType));
                    throw new ParserException(xmlResourceParser, stringBuilder.toString());
                } else if (seenPinSet) {
                    throw new ParserException(xmlResourceParser, "Multiple pin-set elements not allowed");
                } else {
                    builder.setPinSet(parsePinSet(parser));
                    seenPinSet = true;
                    domain = seenDomains;
                }
            } else if (!"domain-config".equals(tagName)) {
                domain = seenDomains;
                XmlUtils.skipCurrentTag(parser);
            } else if (i == 1) {
                builders.addAll(xmlConfigSource.parseConfigEntry(xmlResourceParser, seenDomains, builder, i));
            } else {
                domain = seenDomains;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Nested domain-config not allowed in ");
                stringBuilder2.append(getConfigString(configType));
                throw new ParserException(xmlResourceParser, stringBuilder2.toString());
            }
            xmlConfigSource = this;
        }
        domain = seenDomains;
        if (i != 1 || !domains.isEmpty()) {
            return builders;
        }
        throw new ParserException(xmlResourceParser, "No domain elements in domain-config");
    }

    /* JADX WARNING: Missing block: B:8:0x0018, code skipped:
            return;
     */
    private void addDebugAnchorsIfNeeded(android.security.net.config.NetworkSecurityConfig.Builder r2, android.security.net.config.NetworkSecurityConfig.Builder r3) {
        /*
        r1 = this;
        if (r2 == 0) goto L_0x0018;
    L_0x0002:
        r0 = r2.hasCertificatesEntryRefs();
        if (r0 != 0) goto L_0x0009;
    L_0x0008:
        goto L_0x0018;
    L_0x0009:
        r0 = r3.hasCertificatesEntryRefs();
        if (r0 != 0) goto L_0x0010;
    L_0x000f:
        return;
    L_0x0010:
        r0 = r2.getCertificatesEntryRefs();
        r3.addCertificatesEntryRefs(r0);
        return;
    L_0x0018:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.security.net.config.XmlConfigSource.addDebugAnchorsIfNeeded(android.security.net.config.NetworkSecurityConfig$Builder, android.security.net.config.NetworkSecurityConfig$Builder):void");
    }

    private void parseNetworkSecurityConfig(XmlResourceParser parser) throws IOException, XmlPullParserException, ParserException {
        XmlResourceParser xmlResourceParser = parser;
        Set<String> seenDomains = new ArraySet();
        List<Pair<Builder, Set<Domain>>> builders = new ArrayList();
        Builder baseConfigBuilder = null;
        Builder debugConfigBuilder = null;
        boolean seenDebugOverrides = false;
        boolean seenBaseConfig = false;
        XmlUtils.beginDocument(xmlResourceParser, "network-security-config");
        int outerDepth = parser.getDepth();
        while (XmlUtils.nextElementWithin(xmlResourceParser, outerDepth)) {
            if (!"base-config".equals(parser.getName())) {
                if ("domain-config".equals(parser.getName())) {
                    builders.addAll(parseConfigEntry(xmlResourceParser, seenDomains, baseConfigBuilder, 1));
                } else {
                    if (!"debug-overrides".equals(parser.getName())) {
                        XmlUtils.skipCurrentTag(parser);
                    } else if (seenDebugOverrides) {
                        throw new ParserException(xmlResourceParser, "Only one debug-overrides allowed");
                    } else {
                        if (this.mDebugBuild) {
                            debugConfigBuilder = ((Pair) parseConfigEntry(xmlResourceParser, null, null, 2).get(0)).first;
                        } else {
                            XmlUtils.skipCurrentTag(parser);
                        }
                        seenDebugOverrides = true;
                    }
                }
            } else if (seenBaseConfig) {
                throw new ParserException(xmlResourceParser, "Only one base-config allowed");
            } else {
                seenBaseConfig = true;
                baseConfigBuilder = ((Pair) parseConfigEntry(xmlResourceParser, seenDomains, null, 0).get(0)).first;
            }
        }
        if (this.mDebugBuild && debugConfigBuilder == null) {
            debugConfigBuilder = parseDebugOverridesResource();
        }
        Builder platformDefaultBuilder = NetworkSecurityConfig.getDefaultBuilder(this.mApplicationInfo);
        addDebugAnchorsIfNeeded(debugConfigBuilder, platformDefaultBuilder);
        if (baseConfigBuilder != null) {
            baseConfigBuilder.setParent(platformDefaultBuilder);
            addDebugAnchorsIfNeeded(debugConfigBuilder, baseConfigBuilder);
        } else {
            baseConfigBuilder = platformDefaultBuilder;
        }
        Set<Pair<Domain, NetworkSecurityConfig>> configs = new ArraySet();
        for (Pair<Builder, Set<Domain>> entry : builders) {
            Builder builder = entry.first;
            Set<Domain> domains = entry.second;
            if (builder.getParent() == null) {
                builder.setParent(baseConfigBuilder);
            }
            addDebugAnchorsIfNeeded(debugConfigBuilder, builder);
            NetworkSecurityConfig config = builder.build();
            for (Domain domain : domains) {
                Set<String> seenDomains2 = seenDomains;
                configs.add(new Pair(domain, config));
                xmlResourceParser = parser;
                seenDomains = seenDomains2;
            }
            xmlResourceParser = parser;
        }
        this.mDefaultConfig = baseConfigBuilder.build();
        this.mDomainMap = configs;
    }

    /* JADX WARNING: Missing block: B:26:0x0080, code skipped:
            if (r6 != null) goto L_0x0082;
     */
    /* JADX WARNING: Missing block: B:27:0x0082, code skipped:
            $closeResource(r4, r6);
     */
    private android.security.net.config.NetworkSecurityConfig.Builder parseDebugOverridesResource() throws java.io.IOException, org.xmlpull.v1.XmlPullParserException, android.security.net.config.XmlConfigSource.ParserException {
        /*
        r12 = this;
        r0 = r12.mContext;
        r0 = r0.getResources();
        r1 = r12.mResourceId;
        r1 = r0.getResourcePackageName(r1);
        r2 = r12.mResourceId;
        r2 = r0.getResourceEntryName(r2);
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r2);
        r4 = "_debug";
        r3.append(r4);
        r3 = r3.toString();
        r4 = "xml";
        r3 = r0.getIdentifier(r3, r4, r1);
        r4 = 0;
        if (r3 != 0) goto L_0x002e;
    L_0x002d:
        return r4;
    L_0x002e:
        r5 = 0;
        r6 = r0.getXml(r3);
        r7 = "network-security-config";
        com.android.internal.util.XmlUtils.beginDocument(r6, r7);	 Catch:{ all -> 0x007d }
        r7 = r6.getDepth();	 Catch:{ all -> 0x007d }
        r8 = 0;
        r9 = r8;
    L_0x003f:
        r10 = com.android.internal.util.XmlUtils.nextElementWithin(r6, r7);	 Catch:{ all -> 0x007d }
        if (r10 == 0) goto L_0x0079;
    L_0x0045:
        r10 = "debug-overrides";
        r11 = r6.getName();	 Catch:{ all -> 0x007d }
        r10 = r10.equals(r11);	 Catch:{ all -> 0x007d }
        if (r10 == 0) goto L_0x0075;
    L_0x0051:
        if (r9 != 0) goto L_0x006d;
    L_0x0053:
        r10 = r12.mDebugBuild;	 Catch:{ all -> 0x007d }
        if (r10 == 0) goto L_0x0068;
    L_0x0057:
        r10 = 2;
        r10 = r12.parseConfigEntry(r6, r4, r4, r10);	 Catch:{ all -> 0x007d }
        r10 = r10.get(r8);	 Catch:{ all -> 0x007d }
        r10 = (android.util.Pair) r10;	 Catch:{ all -> 0x007d }
        r10 = r10.first;	 Catch:{ all -> 0x007d }
        r10 = (android.security.net.config.NetworkSecurityConfig.Builder) r10;	 Catch:{ all -> 0x007d }
        r5 = r10;
        goto L_0x006b;
    L_0x0068:
        com.android.internal.util.XmlUtils.skipCurrentTag(r6);	 Catch:{ all -> 0x007d }
    L_0x006b:
        r9 = 1;
        goto L_0x003f;
    L_0x006d:
        r4 = new android.security.net.config.XmlConfigSource$ParserException;	 Catch:{ all -> 0x007d }
        r8 = "Only one debug-overrides allowed";
        r4.<init>(r6, r8);	 Catch:{ all -> 0x007d }
        throw r4;	 Catch:{ all -> 0x007d }
    L_0x0075:
        com.android.internal.util.XmlUtils.skipCurrentTag(r6);	 Catch:{ all -> 0x007d }
        goto L_0x003f;
    L_0x0079:
        $closeResource(r4, r6);
        return r5;
    L_0x007d:
        r4 = move-exception;
        throw r4;	 Catch:{ all -> 0x007f }
    L_0x007f:
        r7 = move-exception;
        if (r6 == 0) goto L_0x0085;
    L_0x0082:
        $closeResource(r4, r6);
    L_0x0085:
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.security.net.config.XmlConfigSource.parseDebugOverridesResource():android.security.net.config.NetworkSecurityConfig$Builder");
    }
}
