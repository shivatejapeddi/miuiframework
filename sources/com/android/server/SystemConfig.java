package com.android.server;

import android.content.ComponentName;
import android.content.pm.FeatureInfo;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Process;
import android.os.SystemProperties;
import android.permission.PermissionManager.SplitPermissionInfo;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.XmlUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SystemConfig {
    private static final int ALLOW_ALL = -1;
    private static final int ALLOW_APP_CONFIGS = 8;
    private static final int ALLOW_ASSOCIATIONS = 128;
    private static final int ALLOW_FEATURES = 1;
    private static final int ALLOW_HIDDENAPI_WHITELISTING = 64;
    private static final int ALLOW_LIBS = 2;
    private static final int ALLOW_OEM_PERMISSIONS = 32;
    private static final int ALLOW_PERMISSIONS = 4;
    private static final int ALLOW_PRIVAPP_PERMISSIONS = 16;
    private static final String SKU_PROPERTY = "ro.boot.product.hardware.sku";
    static final String TAG = "SystemConfig";
    static SystemConfig sInstance;
    final ArraySet<String> mAllowIgnoreLocationSettings = new ArraySet();
    final ArraySet<String> mAllowImplicitBroadcasts = new ArraySet();
    final ArraySet<String> mAllowInDataUsageSave = new ArraySet();
    final ArraySet<String> mAllowInPowerSave = new ArraySet();
    final ArraySet<String> mAllowInPowerSaveExceptIdle = new ArraySet();
    final ArraySet<String> mAllowUnthrottledLocation = new ArraySet();
    final ArrayMap<String, ArraySet<String>> mAllowedAssociations = new ArrayMap();
    final ArrayMap<String, FeatureInfo> mAvailableFeatures = new ArrayMap();
    final ArraySet<ComponentName> mBackupTransportWhitelist = new ArraySet();
    private final ArraySet<String> mBugreportWhitelistedPackages = new ArraySet();
    final ArraySet<ComponentName> mDefaultVrComponents = new ArraySet();
    final ArraySet<String> mDisabledUntilUsedPreinstalledCarrierApps = new ArraySet();
    final ArrayMap<String, List<String>> mDisabledUntilUsedPreinstalledCarrierAssociatedApps = new ArrayMap();
    int[] mGlobalGids;
    final ArraySet<String> mHiddenApiPackageWhitelist = new ArraySet();
    final ArraySet<String> mLinkedApps = new ArraySet();
    final ArrayMap<String, ArrayMap<String, Boolean>> mOemPermissions = new ArrayMap();
    final ArrayMap<String, PermissionEntry> mPermissions = new ArrayMap();
    final ArrayMap<String, ArraySet<String>> mPrivAppDenyPermissions = new ArrayMap();
    final ArrayMap<String, ArraySet<String>> mPrivAppPermissions = new ArrayMap();
    final ArrayMap<String, ArraySet<String>> mProductPrivAppDenyPermissions = new ArrayMap();
    final ArrayMap<String, ArraySet<String>> mProductPrivAppPermissions = new ArrayMap();
    final ArrayMap<String, ArraySet<String>> mProductServicesPrivAppDenyPermissions = new ArrayMap();
    final ArrayMap<String, ArraySet<String>> mProductServicesPrivAppPermissions = new ArrayMap();
    final ArrayMap<String, SharedLibraryEntry> mSharedLibraries = new ArrayMap();
    final ArrayList<SplitPermissionInfo> mSplitPermissions = new ArrayList();
    final SparseArray<ArraySet<String>> mSystemPermissions = new SparseArray();
    final ArraySet<String> mSystemUserBlacklistedApps = new ArraySet();
    final ArraySet<String> mSystemUserWhitelistedApps = new ArraySet();
    final ArraySet<String> mUnavailableFeatures = new ArraySet();
    final ArrayMap<String, ArraySet<String>> mVendorPrivAppDenyPermissions = new ArrayMap();
    final ArrayMap<String, ArraySet<String>> mVendorPrivAppPermissions = new ArrayMap();

    public static final class PermissionEntry {
        public int[] gids;
        public final String name;
        public boolean perUser;

        PermissionEntry(String name, boolean perUser) {
            this.name = name;
            this.perUser = perUser;
        }
    }

    public static final class SharedLibraryEntry {
        public final String[] dependencies;
        public final String filename;
        public final String name;

        SharedLibraryEntry(String name, String filename, String[] dependencies) {
            this.name = name;
            this.filename = filename;
            this.dependencies = dependencies;
        }
    }

    public static SystemConfig getInstance() {
        SystemConfig systemConfig;
        synchronized (SystemConfig.class) {
            if (sInstance == null) {
                sInstance = new SystemConfig();
            }
            systemConfig = sInstance;
        }
        return systemConfig;
    }

    public int[] getGlobalGids() {
        return this.mGlobalGids;
    }

    public SparseArray<ArraySet<String>> getSystemPermissions() {
        return this.mSystemPermissions;
    }

    public ArrayList<SplitPermissionInfo> getSplitPermissions() {
        return this.mSplitPermissions;
    }

    public ArrayMap<String, SharedLibraryEntry> getSharedLibraries() {
        return this.mSharedLibraries;
    }

    public ArrayMap<String, FeatureInfo> getAvailableFeatures() {
        return this.mAvailableFeatures;
    }

    public ArrayMap<String, PermissionEntry> getPermissions() {
        return this.mPermissions;
    }

    public ArraySet<String> getAllowImplicitBroadcasts() {
        return this.mAllowImplicitBroadcasts;
    }

    public ArraySet<String> getAllowInPowerSaveExceptIdle() {
        return this.mAllowInPowerSaveExceptIdle;
    }

    public ArraySet<String> getAllowInPowerSave() {
        return this.mAllowInPowerSave;
    }

    public ArraySet<String> getAllowInDataUsageSave() {
        return this.mAllowInDataUsageSave;
    }

    public ArraySet<String> getAllowUnthrottledLocation() {
        return this.mAllowUnthrottledLocation;
    }

    public ArraySet<String> getAllowIgnoreLocationSettings() {
        return this.mAllowIgnoreLocationSettings;
    }

    public ArraySet<String> getLinkedApps() {
        return this.mLinkedApps;
    }

    public ArraySet<String> getSystemUserWhitelistedApps() {
        return this.mSystemUserWhitelistedApps;
    }

    public ArraySet<String> getSystemUserBlacklistedApps() {
        return this.mSystemUserBlacklistedApps;
    }

    public ArraySet<String> getHiddenApiWhitelistedApps() {
        return this.mHiddenApiPackageWhitelist;
    }

    public ArraySet<ComponentName> getDefaultVrComponents() {
        return this.mDefaultVrComponents;
    }

    public ArraySet<ComponentName> getBackupTransportWhitelist() {
        return this.mBackupTransportWhitelist;
    }

    public ArraySet<String> getDisabledUntilUsedPreinstalledCarrierApps() {
        return this.mDisabledUntilUsedPreinstalledCarrierApps;
    }

    public ArrayMap<String, List<String>> getDisabledUntilUsedPreinstalledCarrierAssociatedApps() {
        return this.mDisabledUntilUsedPreinstalledCarrierAssociatedApps;
    }

    public ArraySet<String> getPrivAppPermissions(String packageName) {
        return (ArraySet) this.mPrivAppPermissions.get(packageName);
    }

    public ArraySet<String> getPrivAppDenyPermissions(String packageName) {
        return (ArraySet) this.mPrivAppDenyPermissions.get(packageName);
    }

    public ArraySet<String> getVendorPrivAppPermissions(String packageName) {
        return (ArraySet) this.mVendorPrivAppPermissions.get(packageName);
    }

    public ArraySet<String> getVendorPrivAppDenyPermissions(String packageName) {
        return (ArraySet) this.mVendorPrivAppDenyPermissions.get(packageName);
    }

    public ArraySet<String> getProductPrivAppPermissions(String packageName) {
        return (ArraySet) this.mProductPrivAppPermissions.get(packageName);
    }

    public ArraySet<String> getProductPrivAppDenyPermissions(String packageName) {
        return (ArraySet) this.mProductPrivAppDenyPermissions.get(packageName);
    }

    public ArraySet<String> getProductServicesPrivAppPermissions(String packageName) {
        return (ArraySet) this.mProductServicesPrivAppPermissions.get(packageName);
    }

    public ArraySet<String> getProductServicesPrivAppDenyPermissions(String packageName) {
        return (ArraySet) this.mProductServicesPrivAppDenyPermissions.get(packageName);
    }

    public Map<String, Boolean> getOemPermissions(String packageName) {
        Map<String, Boolean> oemPermissions = (Map) this.mOemPermissions.get(packageName);
        if (oemPermissions != null) {
            return oemPermissions;
        }
        return Collections.emptyMap();
    }

    public ArrayMap<String, ArraySet<String>> getAllowedAssociations() {
        return this.mAllowedAssociations;
    }

    public ArraySet<String> getBugreportWhitelistedPackages() {
        return this.mBugreportWhitelistedPackages;
    }

    SystemConfig() {
        String str = "sysconfig";
        String str2 = "etc";
        readPermissions(Environment.buildPath(Environment.getRootDirectory(), str2, str), -1);
        String str3 = "permissions";
        readPermissions(Environment.buildPath(Environment.getRootDirectory(), str2, str3), -1);
        int vendorPermissionFlag = 147;
        if (VERSION.FIRST_SDK_INT <= 27) {
            vendorPermissionFlag = 147 | 12;
        }
        readPermissions(Environment.buildPath(Environment.getVendorDirectory(), str2, str), vendorPermissionFlag);
        readPermissions(Environment.buildPath(Environment.getVendorDirectory(), str2, str3), vendorPermissionFlag);
        int odmPermissionFlag = vendorPermissionFlag;
        readPermissions(Environment.buildPath(Environment.getOdmDirectory(), str2, str), odmPermissionFlag);
        readPermissions(Environment.buildPath(Environment.getOdmDirectory(), str2, str3), odmPermissionFlag);
        String skuProperty = SystemProperties.get(SKU_PROPERTY, "");
        if (!skuProperty.isEmpty()) {
            String skuDir = new StringBuilder();
            skuDir.append("sku_");
            skuDir.append(skuProperty);
            skuDir = skuDir.toString();
            readPermissions(Environment.buildPath(Environment.getOdmDirectory(), str2, str, skuDir), odmPermissionFlag);
            readPermissions(Environment.buildPath(Environment.getOdmDirectory(), str2, str3, skuDir), odmPermissionFlag);
        }
        readPermissions(Environment.buildPath(Environment.getOemDirectory(), str2, str), 161);
        readPermissions(Environment.buildPath(Environment.getOemDirectory(), str2, str3), 161);
        readPermissions(Environment.buildPath(Environment.getProductDirectory(), str2, str), -1);
        readPermissions(Environment.buildPath(Environment.getProductDirectory(), str2, str3), -1);
        readPermissions(Environment.buildPath(Environment.getProductServicesDirectory(), str2, str), -1);
        readPermissions(Environment.buildPath(Environment.getProductServicesDirectory(), str2, str3), -1);
    }

    /* Access modifiers changed, original: 0000 */
    public void readPermissions(File libraryDir, int permissionFlag) {
        boolean exists = libraryDir.exists();
        String str = TAG;
        StringBuilder stringBuilder;
        if (exists && libraryDir.isDirectory()) {
            String str2 = " cannot be read";
            if (libraryDir.canRead()) {
                File platformFile = null;
                for (File f : libraryDir.listFiles()) {
                    if (f.isFile()) {
                        StringBuilder stringBuilder2;
                        if (f.getPath().endsWith("etc/permissions/platform.xml")) {
                            platformFile = f;
                        } else if (!f.getPath().endsWith(".xml")) {
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("Non-xml file ");
                            stringBuilder2.append(f);
                            stringBuilder2.append(" in ");
                            stringBuilder2.append(libraryDir);
                            stringBuilder2.append(" directory, ignoring");
                            Slog.i(str, stringBuilder2.toString());
                        } else if (f.canRead()) {
                            readPermissionsFromXml(f, permissionFlag);
                        } else {
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("Permissions library file ");
                            stringBuilder2.append(f);
                            stringBuilder2.append(str2);
                            Slog.w(str, stringBuilder2.toString());
                        }
                    }
                }
                if (platformFile != null) {
                    readPermissionsFromXml(platformFile, permissionFlag);
                }
                return;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Directory ");
            stringBuilder.append(libraryDir);
            stringBuilder.append(str2);
            Slog.w(str, stringBuilder.toString());
            return;
        }
        if (permissionFlag == -1) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("No directory ");
            stringBuilder.append(libraryDir);
            stringBuilder.append(", skipping");
            Slog.w(str, stringBuilder.toString());
        }
    }

    private void logNotAllowedInPartition(String name, File permFile, XmlPullParser parser) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<");
        stringBuilder.append(name);
        stringBuilder.append("> not allowed in partition of ");
        stringBuilder.append(permFile);
        stringBuilder.append(" at ");
        stringBuilder.append(parser.getPositionDescription());
        Slog.w(TAG, stringBuilder.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:391:0x0a68 A:{Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }} */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x002d A:{Catch:{ XmlPullParserException -> 0x0a97, IOException -> 0x0a88, all -> 0x0a80, all -> 0x0aeb }} */
    /* JADX WARNING: Removed duplicated region for block: B:411:0x0ab7  */
    /* JADX WARNING: Removed duplicated region for block: B:410:0x0aab  */
    /* JADX WARNING: Removed duplicated region for block: B:414:0x0abe  */
    /* JADX WARNING: Removed duplicated region for block: B:418:0x0acf  */
    /* JADX WARNING: Removed duplicated region for block: B:417:0x0ac9  */
    /* JADX WARNING: Removed duplicated region for block: B:422:0x0ae0 A:{LOOP_END, LOOP:2: B:420:0x0ada->B:422:0x0ae0} */
    /* JADX WARNING: Removed duplicated region for block: B:410:0x0aab  */
    /* JADX WARNING: Removed duplicated region for block: B:411:0x0ab7  */
    /* JADX WARNING: Removed duplicated region for block: B:414:0x0abe  */
    /* JADX WARNING: Removed duplicated region for block: B:417:0x0ac9  */
    /* JADX WARNING: Removed duplicated region for block: B:418:0x0acf  */
    /* JADX WARNING: Removed duplicated region for block: B:422:0x0ae0 A:{LOOP_END, LOOP:2: B:420:0x0ada->B:422:0x0ae0} */
    /* JADX WARNING: Removed duplicated region for block: B:411:0x0ab7  */
    /* JADX WARNING: Removed duplicated region for block: B:410:0x0aab  */
    /* JADX WARNING: Removed duplicated region for block: B:414:0x0abe  */
    /* JADX WARNING: Removed duplicated region for block: B:418:0x0acf  */
    /* JADX WARNING: Removed duplicated region for block: B:417:0x0ac9  */
    /* JADX WARNING: Removed duplicated region for block: B:422:0x0ae0 A:{LOOP_END, LOOP:2: B:420:0x0ada->B:422:0x0ae0} */
    /* JADX WARNING: Removed duplicated region for block: B:410:0x0aab  */
    /* JADX WARNING: Removed duplicated region for block: B:411:0x0ab7  */
    /* JADX WARNING: Removed duplicated region for block: B:414:0x0abe  */
    /* JADX WARNING: Removed duplicated region for block: B:417:0x0ac9  */
    /* JADX WARNING: Removed duplicated region for block: B:418:0x0acf  */
    /* JADX WARNING: Removed duplicated region for block: B:422:0x0ae0 A:{LOOP_END, LOOP:2: B:420:0x0ada->B:422:0x0ae0} */
    /* JADX WARNING: Removed duplicated region for block: B:411:0x0ab7  */
    /* JADX WARNING: Removed duplicated region for block: B:410:0x0aab  */
    /* JADX WARNING: Removed duplicated region for block: B:414:0x0abe  */
    /* JADX WARNING: Removed duplicated region for block: B:418:0x0acf  */
    /* JADX WARNING: Removed duplicated region for block: B:417:0x0ac9  */
    /* JADX WARNING: Removed duplicated region for block: B:422:0x0ae0 A:{LOOP_END, LOOP:2: B:420:0x0ada->B:422:0x0ae0} */
    /* JADX WARNING: Removed duplicated region for block: B:410:0x0aab  */
    /* JADX WARNING: Removed duplicated region for block: B:411:0x0ab7  */
    /* JADX WARNING: Removed duplicated region for block: B:414:0x0abe  */
    /* JADX WARNING: Removed duplicated region for block: B:417:0x0ac9  */
    /* JADX WARNING: Removed duplicated region for block: B:418:0x0acf  */
    /* JADX WARNING: Removed duplicated region for block: B:422:0x0ae0 A:{LOOP_END, LOOP:2: B:420:0x0ada->B:422:0x0ae0} */
    /* JADX WARNING: Missing block: B:151:0x021e, code skipped:
            r3 = "name";
            r24 = r12;
            r12 = "> without package in ";
            r25 = r7;
            r7 = "package";
            r26 = r4;
            r4 = "<";
            r27 = r8;
            r8 = " at ";
            r28 = "> without name in ";
     */
    /* JADX WARNING: Missing block: B:152:0x0237, code skipped:
            switch(r14) {
                case 0: goto L_0x09eb;
                case 1: goto L_0x09a7;
                case 2: goto L_0x08e4;
                case 3: goto L_0x08d3;
                case 4: goto L_0x0852;
                case 5: goto L_0x07f4;
                case 6: goto L_0x07b7;
                case 7: goto L_0x077c;
                case 8: goto L_0x0741;
                case 9: goto L_0x0706;
                case 10: goto L_0x06cb;
                case 11: goto L_0x0690;
                case 12: goto L_0x0651;
                case 13: goto L_0x0616;
                case 14: goto L_0x05db;
                case 15: goto L_0x05a0;
                case 16: goto L_0x0533;
                case 17: goto L_0x04c0;
                case 18: goto L_0x0467;
                case 19: goto L_0x042c;
                case 20: goto L_0x0373;
                case 21: goto L_0x0360;
                case 22: goto L_0x0325;
                case 23: goto L_0x0273;
                case 24: goto L_0x023e;
                default: goto L_0x023a;
            };
     */
    /* JADX WARNING: Missing block: B:153:0x023a, code skipped:
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:155:?, code skipped:
            r3 = r10.getAttributeValue(null, r7);
     */
    /* JADX WARNING: Missing block: B:156:0x0242, code skipped:
            if (r3 != null) goto L_0x0267;
     */
    /* JADX WARNING: Missing block: B:157:0x0244, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append(r12);
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
     */
    /* JADX WARNING: Missing block: B:158:0x0267, code skipped:
            r1.mBugreportWhitelistedPackages.add(r3);
     */
    /* JADX WARNING: Missing block: B:159:0x026c, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:160:0x0273, code skipped:
            if (r11 == false) goto L_0x031b;
     */
    /* JADX WARNING: Missing block: B:161:0x0275, code skipped:
            r3 = r10.getAttributeValue(null, "target");
     */
    /* JADX WARNING: Missing block: B:162:0x027c, code skipped:
            if (r3 != null) goto L_0x02a9;
     */
    /* JADX WARNING: Missing block: B:163:0x027e, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append("> without target in ");
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:164:0x02a9, code skipped:
            r7 = r10.getAttributeValue(null, "allowed");
     */
    /* JADX WARNING: Missing block: B:165:0x02af, code skipped:
            if (r7 != null) goto L_0x02dc;
     */
    /* JADX WARNING: Missing block: B:166:0x02b1, code skipped:
            r9 = new java.lang.StringBuilder();
            r9.append(r4);
            r9.append(r13);
            r9.append("> without allowed in ");
            r9.append(r2);
            r9.append(r8);
            r9.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r9.toString());
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:167:0x02dc, code skipped:
            r3 = r3.intern();
            r4 = r7.intern();
            r7 = (android.util.ArraySet) r1.mAllowedAssociations.get(r3);
     */
    /* JADX WARNING: Missing block: B:168:0x02ed, code skipped:
            if (r7 != null) goto L_0x02fa;
     */
    /* JADX WARNING: Missing block: B:169:0x02ef, code skipped:
            r7 = new android.util.ArraySet();
            r1.mAllowedAssociations.put(r3, r7);
     */
    /* JADX WARNING: Missing block: B:170:0x02fa, code skipped:
            r8 = new java.lang.StringBuilder();
            r8.append("Adding association: ");
            r8.append(r3);
            r8.append(" <- ");
            r8.append(r4);
            android.util.Slog.i(r6, r8.toString());
            r7.add(r4);
     */
    /* JADX WARNING: Missing block: B:171:0x031b, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:172:0x031e, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:173:0x0325, code skipped:
            if (r22 == false) goto L_0x0356;
     */
    /* JADX WARNING: Missing block: B:174:0x0327, code skipped:
            r3 = r10.getAttributeValue(null, r7);
     */
    /* JADX WARNING: Missing block: B:175:0x032b, code skipped:
            if (r3 != null) goto L_0x0350;
     */
    /* JADX WARNING: Missing block: B:176:0x032d, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append(r12);
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
     */
    /* JADX WARNING: Missing block: B:177:0x0350, code skipped:
            r1.mHiddenApiPackageWhitelist.add(r3);
     */
    /* JADX WARNING: Missing block: B:179:0x0356, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:180:0x0359, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:181:0x0360, code skipped:
            if (r21 == false) goto L_0x0369;
     */
    /* JADX WARNING: Missing block: B:182:0x0362, code skipped:
            readOemPermissions(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:183:0x0369, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:184:0x0373, code skipped:
            if (r20 == false) goto L_0x0422;
     */
    /* JADX WARNING: Missing block: B:185:0x0375, code skipped:
            r3 = r30.toPath();
            r4 = new java.lang.StringBuilder();
            r4.append(android.os.Environment.getVendorDirectory().toPath());
            r4.append(r5);
     */
    /* JADX WARNING: Missing block: B:186:0x0394, code skipped:
            if (r3.startsWith(r4.toString()) != false) goto L_0x03ba;
     */
    /* JADX WARNING: Missing block: B:187:0x0396, code skipped:
            r3 = r30.toPath();
            r4 = new java.lang.StringBuilder();
            r4.append(android.os.Environment.getOdmDirectory().toPath());
            r4.append(r5);
     */
    /* JADX WARNING: Missing block: B:188:0x03b5, code skipped:
            if (r3.startsWith(r4.toString()) == false) goto L_0x03b8;
     */
    /* JADX WARNING: Missing block: B:190:0x03b8, code skipped:
            r3 = false;
     */
    /* JADX WARNING: Missing block: B:191:0x03ba, code skipped:
            r3 = true;
     */
    /* JADX WARNING: Missing block: B:192:0x03bb, code skipped:
            r4 = r30.toPath();
            r7 = new java.lang.StringBuilder();
            r7.append(android.os.Environment.getProductDirectory().toPath());
            r7.append(r5);
            r4 = r4.startsWith(r7.toString());
            r7 = r30.toPath();
            r8 = new java.lang.StringBuilder();
            r8.append(android.os.Environment.getProductServicesDirectory().toPath());
            r8.append(r5);
            r7 = r7.startsWith(r8.toString());
     */
    /* JADX WARNING: Missing block: B:193:0x03f9, code skipped:
            if (r3 == false) goto L_0x0403;
     */
    /* JADX WARNING: Missing block: B:194:0x03fb, code skipped:
            readPrivAppPermissions(r10, r1.mVendorPrivAppPermissions, r1.mVendorPrivAppDenyPermissions);
     */
    /* JADX WARNING: Missing block: B:195:0x0403, code skipped:
            if (r4 == false) goto L_0x040d;
     */
    /* JADX WARNING: Missing block: B:196:0x0405, code skipped:
            readPrivAppPermissions(r10, r1.mProductPrivAppPermissions, r1.mProductPrivAppDenyPermissions);
     */
    /* JADX WARNING: Missing block: B:197:0x040d, code skipped:
            if (r7 == false) goto L_0x0417;
     */
    /* JADX WARNING: Missing block: B:198:0x040f, code skipped:
            readPrivAppPermissions(r10, r1.mProductServicesPrivAppPermissions, r1.mProductServicesPrivAppDenyPermissions);
     */
    /* JADX WARNING: Missing block: B:199:0x0417, code skipped:
            readPrivAppPermissions(r10, r1.mPrivAppPermissions, r1.mPrivAppDenyPermissions);
     */
    /* JADX WARNING: Missing block: B:200:0x041e, code skipped:
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:201:0x0422, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:202:0x042c, code skipped:
            if (r19 == false) goto L_0x045d;
     */
    /* JADX WARNING: Missing block: B:203:0x042e, code skipped:
            r3 = r10.getAttributeValue(null, r7);
     */
    /* JADX WARNING: Missing block: B:204:0x0432, code skipped:
            if (r3 != null) goto L_0x0457;
     */
    /* JADX WARNING: Missing block: B:205:0x0434, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append(r12);
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
     */
    /* JADX WARNING: Missing block: B:206:0x0457, code skipped:
            r1.mDisabledUntilUsedPreinstalledCarrierApps.add(r3);
     */
    /* JADX WARNING: Missing block: B:208:0x045d, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:209:0x0460, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:210:0x0467, code skipped:
            if (r19 == false) goto L_0x04b6;
     */
    /* JADX WARNING: Missing block: B:211:0x0469, code skipped:
            r3 = r10.getAttributeValue(null, r7);
            r7 = r10.getAttributeValue(null, "carrierAppPackage");
     */
    /* JADX WARNING: Missing block: B:212:0x0473, code skipped:
            if (r3 == null) goto L_0x0491;
     */
    /* JADX WARNING: Missing block: B:213:0x0475, code skipped:
            if (r7 != null) goto L_0x0478;
     */
    /* JADX WARNING: Missing block: B:215:0x0478, code skipped:
            r4 = (java.util.List) r1.mDisabledUntilUsedPreinstalledCarrierAssociatedApps.get(r7);
     */
    /* JADX WARNING: Missing block: B:216:0x0480, code skipped:
            if (r4 != null) goto L_0x048d;
     */
    /* JADX WARNING: Missing block: B:217:0x0482, code skipped:
            r4 = new java.util.ArrayList();
            r1.mDisabledUntilUsedPreinstalledCarrierAssociatedApps.put(r7, r4);
     */
    /* JADX WARNING: Missing block: B:218:0x048d, code skipped:
            r4.add(r3);
     */
    /* JADX WARNING: Missing block: B:219:0x0491, code skipped:
            r9 = new java.lang.StringBuilder();
            r9.append(r4);
            r9.append(r13);
            r9.append("> without package or carrierAppPackage in ");
            r9.append(r2);
            r9.append(r8);
            r9.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r9.toString());
     */
    /* JADX WARNING: Missing block: B:221:0x04b6, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:222:0x04b9, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:223:0x04c0, code skipped:
            if (r17 == false) goto L_0x0529;
     */
    /* JADX WARNING: Missing block: B:224:0x04c2, code skipped:
            r3 = r10.getAttributeValue(null, "service");
     */
    /* JADX WARNING: Missing block: B:225:0x04c9, code skipped:
            if (r3 != null) goto L_0x04f0;
     */
    /* JADX WARNING: Missing block: B:226:0x04cb, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append("> without service in ");
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
     */
    /* JADX WARNING: Missing block: B:227:0x04f0, code skipped:
            r7 = android.content.ComponentName.unflattenFromString(r3);
     */
    /* JADX WARNING: Missing block: B:228:0x04f4, code skipped:
            if (r7 != null) goto L_0x0523;
     */
    /* JADX WARNING: Missing block: B:229:0x04f6, code skipped:
            r9 = new java.lang.StringBuilder();
            r9.append(r4);
            r9.append(r13);
            r9.append("> with invalid service name ");
            r9.append(r3);
            r9.append(" in ");
            r9.append(r2);
            r9.append(r8);
            r9.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r9.toString());
     */
    /* JADX WARNING: Missing block: B:230:0x0523, code skipped:
            r1.mBackupTransportWhitelist.add(r7);
     */
    /* JADX WARNING: Missing block: B:232:0x0529, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:233:0x052c, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:234:0x0533, code skipped:
            if (r19 == false) goto L_0x0596;
     */
    /* JADX WARNING: Missing block: B:235:0x0535, code skipped:
            r3 = r10.getAttributeValue(null, r7);
            r7 = r10.getAttributeValue(null, com.miui.internal.search.Function.CLASS);
     */
    /* JADX WARNING: Missing block: B:236:0x053f, code skipped:
            if (r3 != null) goto L_0x0564;
     */
    /* JADX WARNING: Missing block: B:237:0x0541, code skipped:
            r9 = new java.lang.StringBuilder();
            r9.append(r4);
            r9.append(r13);
            r9.append(r12);
            r9.append(r2);
            r9.append(r8);
            r9.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r9.toString());
     */
    /* JADX WARNING: Missing block: B:238:0x0564, code skipped:
            if (r7 != null) goto L_0x058b;
     */
    /* JADX WARNING: Missing block: B:239:0x0566, code skipped:
            r9 = new java.lang.StringBuilder();
            r9.append(r4);
            r9.append(r13);
            r9.append("> without class in ");
            r9.append(r2);
            r9.append(r8);
            r9.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r9.toString());
     */
    /* JADX WARNING: Missing block: B:240:0x058b, code skipped:
            r1.mDefaultVrComponents.add(new android.content.ComponentName(r3, r7));
     */
    /* JADX WARNING: Missing block: B:242:0x0596, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:243:0x0599, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:244:0x05a0, code skipped:
            if (r19 == false) goto L_0x05d1;
     */
    /* JADX WARNING: Missing block: B:245:0x05a2, code skipped:
            r3 = r10.getAttributeValue(null, r7);
     */
    /* JADX WARNING: Missing block: B:246:0x05a6, code skipped:
            if (r3 != null) goto L_0x05cb;
     */
    /* JADX WARNING: Missing block: B:247:0x05a8, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append(r12);
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
     */
    /* JADX WARNING: Missing block: B:248:0x05cb, code skipped:
            r1.mSystemUserBlacklistedApps.add(r3);
     */
    /* JADX WARNING: Missing block: B:250:0x05d1, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:251:0x05d4, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:252:0x05db, code skipped:
            if (r19 == false) goto L_0x060c;
     */
    /* JADX WARNING: Missing block: B:253:0x05dd, code skipped:
            r3 = r10.getAttributeValue(null, r7);
     */
    /* JADX WARNING: Missing block: B:254:0x05e1, code skipped:
            if (r3 != null) goto L_0x0606;
     */
    /* JADX WARNING: Missing block: B:255:0x05e3, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append(r12);
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
     */
    /* JADX WARNING: Missing block: B:256:0x0606, code skipped:
            r1.mSystemUserWhitelistedApps.add(r3);
     */
    /* JADX WARNING: Missing block: B:258:0x060c, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:259:0x060f, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:260:0x0616, code skipped:
            if (r19 == false) goto L_0x0647;
     */
    /* JADX WARNING: Missing block: B:261:0x0618, code skipped:
            r3 = r10.getAttributeValue(null, r7);
     */
    /* JADX WARNING: Missing block: B:262:0x061c, code skipped:
            if (r3 != null) goto L_0x0641;
     */
    /* JADX WARNING: Missing block: B:263:0x061e, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append(r12);
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
     */
    /* JADX WARNING: Missing block: B:264:0x0641, code skipped:
            r1.mLinkedApps.add(r3);
     */
    /* JADX WARNING: Missing block: B:266:0x0647, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:267:0x064a, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:268:0x0651, code skipped:
            if (r15 == false) goto L_0x0686;
     */
    /* JADX WARNING: Missing block: B:269:0x0653, code skipped:
            r3 = r10.getAttributeValue(null, "action");
     */
    /* JADX WARNING: Missing block: B:270:0x0659, code skipped:
            if (r3 != null) goto L_0x0680;
     */
    /* JADX WARNING: Missing block: B:271:0x065b, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append("> without action in ");
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
     */
    /* JADX WARNING: Missing block: B:272:0x0680, code skipped:
            r1.mAllowImplicitBroadcasts.add(r3);
     */
    /* JADX WARNING: Missing block: B:274:0x0686, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:275:0x0689, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:276:0x0690, code skipped:
            if (r15 == false) goto L_0x06c1;
     */
    /* JADX WARNING: Missing block: B:277:0x0692, code skipped:
            r3 = r10.getAttributeValue(null, r7);
     */
    /* JADX WARNING: Missing block: B:278:0x0696, code skipped:
            if (r3 != null) goto L_0x06bb;
     */
    /* JADX WARNING: Missing block: B:279:0x0698, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append(r12);
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
     */
    /* JADX WARNING: Missing block: B:280:0x06bb, code skipped:
            r1.mAllowIgnoreLocationSettings.add(r3);
     */
    /* JADX WARNING: Missing block: B:282:0x06c1, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:283:0x06c4, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:284:0x06cb, code skipped:
            if (r15 == false) goto L_0x06fc;
     */
    /* JADX WARNING: Missing block: B:285:0x06cd, code skipped:
            r3 = r10.getAttributeValue(null, r7);
     */
    /* JADX WARNING: Missing block: B:286:0x06d1, code skipped:
            if (r3 != null) goto L_0x06f6;
     */
    /* JADX WARNING: Missing block: B:287:0x06d3, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append(r12);
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
     */
    /* JADX WARNING: Missing block: B:288:0x06f6, code skipped:
            r1.mAllowUnthrottledLocation.add(r3);
     */
    /* JADX WARNING: Missing block: B:290:0x06fc, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:291:0x06ff, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:292:0x0706, code skipped:
            if (r15 == false) goto L_0x0737;
     */
    /* JADX WARNING: Missing block: B:293:0x0708, code skipped:
            r3 = r10.getAttributeValue(null, r7);
     */
    /* JADX WARNING: Missing block: B:294:0x070c, code skipped:
            if (r3 != null) goto L_0x0731;
     */
    /* JADX WARNING: Missing block: B:295:0x070e, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append(r12);
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
     */
    /* JADX WARNING: Missing block: B:296:0x0731, code skipped:
            r1.mAllowInDataUsageSave.add(r3);
     */
    /* JADX WARNING: Missing block: B:298:0x0737, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:299:0x073a, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:300:0x0741, code skipped:
            if (r15 == false) goto L_0x0772;
     */
    /* JADX WARNING: Missing block: B:301:0x0743, code skipped:
            r3 = r10.getAttributeValue(null, r7);
     */
    /* JADX WARNING: Missing block: B:302:0x0747, code skipped:
            if (r3 != null) goto L_0x076c;
     */
    /* JADX WARNING: Missing block: B:303:0x0749, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append(r12);
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
     */
    /* JADX WARNING: Missing block: B:304:0x076c, code skipped:
            r1.mAllowInPowerSave.add(r3);
     */
    /* JADX WARNING: Missing block: B:306:0x0772, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:307:0x0775, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:308:0x077c, code skipped:
            if (r15 == false) goto L_0x07ad;
     */
    /* JADX WARNING: Missing block: B:309:0x077e, code skipped:
            r3 = r10.getAttributeValue(null, r7);
     */
    /* JADX WARNING: Missing block: B:310:0x0782, code skipped:
            if (r3 != null) goto L_0x07a7;
     */
    /* JADX WARNING: Missing block: B:311:0x0784, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append(r12);
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
     */
    /* JADX WARNING: Missing block: B:312:0x07a7, code skipped:
            r1.mAllowInPowerSaveExceptIdle.add(r3);
     */
    /* JADX WARNING: Missing block: B:314:0x07ad, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:315:0x07b0, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:316:0x07b7, code skipped:
            if (r17 == false) goto L_0x07ea;
     */
    /* JADX WARNING: Missing block: B:317:0x07b9, code skipped:
            r3 = r10.getAttributeValue(null, r3);
     */
    /* JADX WARNING: Missing block: B:318:0x07bd, code skipped:
            if (r3 != null) goto L_0x07e4;
     */
    /* JADX WARNING: Missing block: B:319:0x07bf, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append(r28);
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
     */
    /* JADX WARNING: Missing block: B:320:0x07e4, code skipped:
            r1.mUnavailableFeatures.add(r3);
     */
    /* JADX WARNING: Missing block: B:322:0x07ea, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:323:0x07ed, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:324:0x07f4, code skipped:
            r12 = r28;
     */
    /* JADX WARNING: Missing block: B:325:0x07f6, code skipped:
            if (r17 == false) goto L_0x0848;
     */
    /* JADX WARNING: Missing block: B:326:0x07f8, code skipped:
            r3 = r10.getAttributeValue(null, r3);
            r7 = com.android.internal.util.XmlUtils.readIntAttribute(r10, "version", 0);
     */
    /* JADX WARNING: Missing block: B:327:0x0804, code skipped:
            if (r27 != false) goto L_0x080a;
     */
    /* JADX WARNING: Missing block: B:328:0x0806, code skipped:
            r9 = true;
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:329:0x080a, code skipped:
            r23 = 1;
            r9 = "true".equals(r10.getAttributeValue(null, "notLowRam")) ^ 1;
     */
    /* JADX WARNING: Missing block: B:330:0x081d, code skipped:
            if (r3 != null) goto L_0x0842;
     */
    /* JADX WARNING: Missing block: B:331:0x081f, code skipped:
            r14 = new java.lang.StringBuilder();
            r14.append(r4);
            r14.append(r13);
            r14.append(r12);
            r14.append(r2);
            r14.append(r8);
            r14.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r14.toString());
     */
    /* JADX WARNING: Missing block: B:332:0x0842, code skipped:
            if (r9 == false) goto L_0x0847;
     */
    /* JADX WARNING: Missing block: B:333:0x0844, code skipped:
            addFeature(r3, r7);
     */
    /* JADX WARNING: Missing block: B:335:0x0848, code skipped:
            r23 = 1;
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:336:0x084d, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARNING: Missing block: B:337:0x0852, code skipped:
            r12 = r28;
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:338:0x0856, code skipped:
            if (r16 == false) goto L_0x08cb;
     */
    /* JADX WARNING: Missing block: B:339:0x0858, code skipped:
            r3 = r10.getAttributeValue(null, r3);
            r7 = r10.getAttributeValue(null, android.content.ContentResolver.SCHEME_FILE);
            r9 = r10.getAttributeValue(null, "dependency");
     */
    /* JADX WARNING: Missing block: B:340:0x0868, code skipped:
            if (r3 != null) goto L_0x088d;
     */
    /* JADX WARNING: Missing block: B:341:0x086a, code skipped:
            r14 = new java.lang.StringBuilder();
            r14.append(r4);
            r14.append(r13);
            r14.append(r12);
            r14.append(r2);
            r14.append(r8);
            r14.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r14.toString());
     */
    /* JADX WARNING: Missing block: B:342:0x088d, code skipped:
            if (r7 != null) goto L_0x08b4;
     */
    /* JADX WARNING: Missing block: B:343:0x088f, code skipped:
            r12 = new java.lang.StringBuilder();
            r12.append(r4);
            r12.append(r13);
            r12.append("> without file in ");
            r12.append(r2);
            r12.append(r8);
            r12.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r12.toString());
     */
    /* JADX WARNING: Missing block: B:345:0x08b6, code skipped:
            if (r9 != null) goto L_0x08bc;
     */
    /* JADX WARNING: Missing block: B:346:0x08b8, code skipped:
            r12 = new java.lang.String[0];
     */
    /* JADX WARNING: Missing block: B:347:0x08bc, code skipped:
            r12 = r9.split(":");
     */
    /* JADX WARNING: Missing block: B:348:0x08c2, code skipped:
            r1.mSharedLibraries.put(r3, new com.android.server.SystemConfig.SharedLibraryEntry(r3, r7, r12));
     */
    /* JADX WARNING: Missing block: B:350:0x08cb, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:351:0x08ce, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARNING: Missing block: B:352:0x08d3, code skipped:
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:353:0x08d5, code skipped:
            if (r18 == false) goto L_0x08dc;
     */
    /* JADX WARNING: Missing block: B:354:0x08d7, code skipped:
            readSplitPermission(r10, r2);
     */
    /* JADX WARNING: Missing block: B:355:0x08dc, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARNING: Missing block: B:356:0x08e4, code skipped:
            r12 = r28;
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:357:0x08e8, code skipped:
            if (r18 == false) goto L_0x099f;
     */
    /* JADX WARNING: Missing block: B:358:0x08ea, code skipped:
            r3 = r10.getAttributeValue(null, r3);
     */
    /* JADX WARNING: Missing block: B:359:0x08ee, code skipped:
            if (r3 != null) goto L_0x0917;
     */
    /* JADX WARNING: Missing block: B:360:0x08f0, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append(r12);
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARNING: Missing block: B:361:0x0917, code skipped:
            r7 = r10.getAttributeValue(null, android.accounts.GrantCredentialsPermissionActivity.EXTRAS_REQUESTING_UID);
     */
    /* JADX WARNING: Missing block: B:362:0x091e, code skipped:
            if (r7 != null) goto L_0x0949;
     */
    /* JADX WARNING: Missing block: B:363:0x0920, code skipped:
            r9 = new java.lang.StringBuilder();
            r9.append(r4);
            r9.append(r13);
            r9.append("> without uid in ");
            r9.append(r2);
            r9.append(r8);
            r9.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r9.toString());
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARNING: Missing block: B:364:0x0949, code skipped:
            r9 = android.os.Process.getUidForName(r7);
     */
    /* JADX WARNING: Missing block: B:365:0x094d, code skipped:
            if (r9 >= 0) goto L_0x0980;
     */
    /* JADX WARNING: Missing block: B:366:0x094f, code skipped:
            r12 = new java.lang.StringBuilder();
            r12.append(r4);
            r12.append(r13);
            r12.append("> with unknown uid \"");
            r12.append(r7);
            r12.append("  in ");
            r12.append(r2);
            r12.append(r8);
            r12.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r12.toString());
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARNING: Missing block: B:367:0x0980, code skipped:
            r3 = r3.intern();
            r4 = (android.util.ArraySet) r1.mSystemPermissions.get(r9);
     */
    /* JADX WARNING: Missing block: B:368:0x098d, code skipped:
            if (r4 != null) goto L_0x099a;
     */
    /* JADX WARNING: Missing block: B:369:0x098f, code skipped:
            r4 = new android.util.ArraySet();
            r1.mSystemPermissions.put(r9, r4);
     */
    /* JADX WARNING: Missing block: B:370:0x099a, code skipped:
            r4.add(r3);
     */
    /* JADX WARNING: Missing block: B:371:0x099f, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:372:0x09a2, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARNING: Missing block: B:373:0x09a7, code skipped:
            r12 = r28;
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:374:0x09ab, code skipped:
            if (r18 == false) goto L_0x09e4;
     */
    /* JADX WARNING: Missing block: B:375:0x09ad, code skipped:
            r3 = r10.getAttributeValue(null, r3);
     */
    /* JADX WARNING: Missing block: B:376:0x09b1, code skipped:
            if (r3 != null) goto L_0x09da;
     */
    /* JADX WARNING: Missing block: B:377:0x09b3, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append(r12);
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARNING: Missing block: B:378:0x09da, code skipped:
            readPermission(r10, r3.intern());
     */
    /* JADX WARNING: Missing block: B:379:0x09e4, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARNING: Missing block: B:380:0x09eb, code skipped:
            r23 = 1;
     */
    /* JADX WARNING: Missing block: B:381:0x09ed, code skipped:
            if (r15 == false) goto L_0x0a29;
     */
    /* JADX WARNING: Missing block: B:382:0x09ef, code skipped:
            r3 = r10.getAttributeValue(null, "gid");
     */
    /* JADX WARNING: Missing block: B:383:0x09f5, code skipped:
            if (r3 == null) goto L_0x0a04;
     */
    /* JADX WARNING: Missing block: B:384:0x09f7, code skipped:
            r1.mGlobalGids = com.android.internal.util.ArrayUtils.appendInt(r1.mGlobalGids, android.os.Process.getGidForName(r3));
     */
    /* JADX WARNING: Missing block: B:385:0x0a04, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append(r4);
            r7.append(r13);
            r7.append("> without gid in ");
            r7.append(r2);
            r7.append(r8);
            r7.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r7.toString());
     */
    /* JADX WARNING: Missing block: B:387:0x0a29, code skipped:
            logNotAllowedInPartition(r13, r2, r10);
     */
    /* JADX WARNING: Missing block: B:388:0x0a2c, code skipped:
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARNING: Missing block: B:389:0x0a30, code skipped:
            r3 = new java.lang.StringBuilder();
            r3.append("Tag ");
            r3.append(r13);
            r3.append(" is unknown in ");
            r3.append(r2);
            r3.append(r8);
            r3.append(r10.getPositionDescription());
            android.util.Slog.w(r6, r3.toString());
            com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARNING: Missing block: B:390:0x0a59, code skipped:
            r3 = r31;
            r14 = r23;
            r12 = r24;
            r7 = r25;
            r4 = r26;
            r8 = r27;
     */
    /* JADX WARNING: Missing block: B:393:0x0a78, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:394:0x0a79, code skipped:
            r3 = r0;
     */
    /* JADX WARNING: Missing block: B:395:0x0a7b, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:396:0x0a7c, code skipped:
            r3 = r0;
            r4 = r26;
     */
    private void readPermissionsFromXml(java.io.File r30, int r31) {
        /*
        r29 = this;
        r1 = r29;
        r2 = r30;
        r3 = r31;
        r4 = "Got exception parsing permissions.";
        r5 = "/";
        r6 = "SystemConfig";
        r7 = 0;
        r8 = new java.io.FileReader;	 Catch:{ FileNotFoundException -> 0x0af1 }
        r8.<init>(r2);	 Catch:{ FileNotFoundException -> 0x0af1 }
        r7 = r8;
        r8 = android.app.ActivityManager.isLowRamDeviceStatic();
        r10 = android.util.Xml.newPullParser();	 Catch:{ XmlPullParserException -> 0x0a97, IOException -> 0x0a88, all -> 0x0a80 }
        r10.setInput(r7);	 Catch:{ XmlPullParserException -> 0x0a97, IOException -> 0x0a88, all -> 0x0a80 }
    L_0x001f:
        r11 = r10.next();	 Catch:{ XmlPullParserException -> 0x0a97, IOException -> 0x0a88, all -> 0x0a80 }
        r12 = r11;
        r13 = 2;
        r14 = 1;
        if (r11 == r13) goto L_0x002b;
    L_0x0028:
        if (r12 == r14) goto L_0x002b;
    L_0x002a:
        goto L_0x001f;
    L_0x002b:
        if (r12 != r13) goto L_0x0a68;
    L_0x002d:
        r11 = r10.getName();	 Catch:{ XmlPullParserException -> 0x0a97, IOException -> 0x0a88, all -> 0x0a80 }
        r15 = "permissions";
        r11 = r11.equals(r15);	 Catch:{ XmlPullParserException -> 0x0a97, IOException -> 0x0a88, all -> 0x0a80 }
        if (r11 != 0) goto L_0x0089;
    L_0x003a:
        r11 = r10.getName();	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        r15 = "config";
        r11 = r11.equals(r15);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r11 == 0) goto L_0x0047;
    L_0x0046:
        goto L_0x0089;
    L_0x0047:
        r5 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        r11 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        r11.<init>();	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        r13 = "Unexpected start tag in ";
        r11.append(r13);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        r11.append(r2);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        r13 = ": found ";
        r11.append(r13);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        r13 = r10.getName();	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        r11.append(r13);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        r13 = ", expected 'permissions' or 'config'";
        r11.append(r13);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        r11 = r11.toString();	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        r5.<init>(r11);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        throw r5;	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
    L_0x006f:
        r0 = move-exception;
        r3 = r0;
        r25 = r7;
        r27 = r8;
        goto L_0x0aed;
    L_0x0077:
        r0 = move-exception;
        r3 = r0;
        r26 = r4;
        r25 = r7;
        r27 = r8;
        goto L_0x0a90;
    L_0x0081:
        r0 = move-exception;
        r3 = r0;
        r25 = r7;
        r27 = r8;
        goto L_0x0a9d;
    L_0x0089:
        r11 = -1;
        if (r3 != r11) goto L_0x008e;
    L_0x008c:
        r15 = r14;
        goto L_0x008f;
    L_0x008e:
        r15 = 0;
    L_0x008f:
        r16 = r3 & 2;
        if (r16 == 0) goto L_0x0096;
    L_0x0093:
        r16 = r14;
        goto L_0x0098;
    L_0x0096:
        r16 = 0;
    L_0x0098:
        r17 = r3 & 1;
        if (r17 == 0) goto L_0x009f;
    L_0x009c:
        r17 = r14;
        goto L_0x00a1;
    L_0x009f:
        r17 = 0;
    L_0x00a1:
        r18 = r3 & 4;
        if (r18 == 0) goto L_0x00a8;
    L_0x00a5:
        r18 = r14;
        goto L_0x00aa;
    L_0x00a8:
        r18 = 0;
    L_0x00aa:
        r19 = r3 & 8;
        if (r19 == 0) goto L_0x00b1;
    L_0x00ae:
        r19 = r14;
        goto L_0x00b3;
    L_0x00b1:
        r19 = 0;
    L_0x00b3:
        r20 = r3 & 16;
        if (r20 == 0) goto L_0x00ba;
    L_0x00b7:
        r20 = r14;
        goto L_0x00bc;
    L_0x00ba:
        r20 = 0;
    L_0x00bc:
        r21 = r3 & 32;
        if (r21 == 0) goto L_0x00c3;
    L_0x00c0:
        r21 = r14;
        goto L_0x00c5;
    L_0x00c3:
        r21 = 0;
    L_0x00c5:
        r22 = r3 & 64;
        if (r22 == 0) goto L_0x00cc;
    L_0x00c9:
        r22 = r14;
        goto L_0x00ce;
    L_0x00cc:
        r22 = 0;
    L_0x00ce:
        r11 = r3 & 128;
        if (r11 == 0) goto L_0x00d4;
    L_0x00d2:
        r11 = r14;
        goto L_0x00d5;
    L_0x00d4:
        r11 = 0;
    L_0x00d5:
        com.android.internal.util.XmlUtils.nextElement(r10);	 Catch:{ XmlPullParserException -> 0x0a97, IOException -> 0x0a88, all -> 0x0a80 }
        r13 = r10.getEventType();	 Catch:{ XmlPullParserException -> 0x0a97, IOException -> 0x0a88, all -> 0x0a80 }
        if (r13 != r14) goto L_0x00e8;
        libcore.io.IoUtils.closeQuietly(r7);
        r25 = r7;
        r27 = r8;
        goto L_0x0aa5;
    L_0x00e8:
        r13 = r10.getName();	 Catch:{ XmlPullParserException -> 0x0a97, IOException -> 0x0a88, all -> 0x0a80 }
        if (r13 != 0) goto L_0x00f3;
    L_0x00ee:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        r13 = 2;
        goto L_0x00d5;
    L_0x00f3:
        r23 = r13.hashCode();	 Catch:{ XmlPullParserException -> 0x0a97, IOException -> 0x0a88, all -> 0x0a80 }
        switch(r23) {
            case -2040330235: goto L_0x0212;
            case -1882490007: goto L_0x0207;
            case -1005864890: goto L_0x01fc;
            case -980620291: goto L_0x01f1;
            case -979207434: goto L_0x01e7;
            case -851582420: goto L_0x01db;
            case -828905863: goto L_0x01d0;
            case -642819164: goto L_0x01c6;
            case -560717308: goto L_0x01bb;
            case -517618225: goto L_0x01b0;
            case 98629247: goto L_0x01a5;
            case 166208699: goto L_0x019a;
            case 180165796: goto L_0x018e;
            case 347247519: goto L_0x0182;
            case 508457430: goto L_0x0175;
            case 802332808: goto L_0x0169;
            case 953292141: goto L_0x015e;
            case 1044015374: goto L_0x0151;
            case 1121420326: goto L_0x0145;
            case 1269564002: goto L_0x0139;
            case 1567330472: goto L_0x012d;
            case 1633270165: goto L_0x0121;
            case 1723146313: goto L_0x0114;
            case 1723586945: goto L_0x0108;
            case 1954925533: goto L_0x00fc;
            default: goto L_0x00fa;
        };
    L_0x00fa:
        goto L_0x021d;
    L_0x00fc:
        r14 = "allow-implicit-broadcast";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x0104:
        r14 = 12;
        goto L_0x021e;
    L_0x0108:
        r14 = "bugreport-whitelisted";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x0110:
        r14 = 24;
        goto L_0x021e;
    L_0x0114:
        r14 = "privapp-permissions";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x011d:
        r14 = 20;
        goto L_0x021e;
    L_0x0121:
        r14 = "disabled-until-used-preinstalled-carrier-associated-app";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x0129:
        r14 = 18;
        goto L_0x021e;
    L_0x012d:
        r14 = "default-enabled-vr-app";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x0135:
        r14 = 16;
        goto L_0x021e;
    L_0x0139:
        r14 = "split-permission";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x0142:
        r14 = 3;
        goto L_0x021e;
    L_0x0145:
        r14 = "app-link";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x014d:
        r14 = 13;
        goto L_0x021e;
    L_0x0151:
        r14 = "oem-permissions";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x015a:
        r14 = 21;
        goto L_0x021e;
    L_0x015e:
        r14 = "assign-permission";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x0166:
        r14 = 2;
        goto L_0x021e;
    L_0x0169:
        r14 = "allow-in-data-usage-save";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x0171:
        r14 = 9;
        goto L_0x021e;
    L_0x0175:
        r14 = "system-user-whitelisted-app";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x017e:
        r14 = 14;
        goto L_0x021e;
    L_0x0182:
        r14 = "backup-transport-whitelisted-service";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x018a:
        r14 = 17;
        goto L_0x021e;
    L_0x018e:
        r14 = "hidden-api-whitelisted-app";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x0196:
        r14 = 22;
        goto L_0x021e;
    L_0x019a:
        r14 = "library";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x01a2:
        r14 = 4;
        goto L_0x021e;
    L_0x01a5:
        r14 = "group";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x01ad:
        r14 = 0;
        goto L_0x021e;
    L_0x01b0:
        r14 = "permission";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x01b9:
        r14 = 1;
        goto L_0x021e;
    L_0x01bb:
        r14 = "allow-ignore-location-settings";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x01c3:
        r14 = 11;
        goto L_0x021e;
    L_0x01c6:
        r14 = "allow-in-power-save-except-idle";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x01ce:
        r14 = 7;
        goto L_0x021e;
    L_0x01d0:
        r14 = "unavailable-feature";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x01d9:
        r14 = 6;
        goto L_0x021e;
    L_0x01db:
        r14 = "system-user-blacklisted-app";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x01e4:
        r14 = 15;
        goto L_0x021e;
    L_0x01e7:
        r14 = "feature";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x01ef:
        r14 = 5;
        goto L_0x021e;
    L_0x01f1:
        r14 = "allow-association";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x01f9:
        r14 = 23;
        goto L_0x021e;
    L_0x01fc:
        r14 = "disabled-until-used-preinstalled-carrier-app";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x0204:
        r14 = 19;
        goto L_0x021e;
    L_0x0207:
        r14 = "allow-in-power-save";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x020f:
        r14 = 8;
        goto L_0x021e;
    L_0x0212:
        r14 = "allow-unthrottled-location";
        r14 = r13.equals(r14);	 Catch:{ XmlPullParserException -> 0x0081, IOException -> 0x0077, all -> 0x006f }
        if (r14 == 0) goto L_0x00fa;
    L_0x021a:
        r14 = 10;
        goto L_0x021e;
    L_0x021d:
        r14 = -1;
    L_0x021e:
        r9 = "> without name in ";
        r3 = "name";
        r24 = r12;
        r12 = "> without package in ";
        r25 = r7;
        r7 = "package";
        r26 = r4;
        r4 = "<";
        r27 = r8;
        r8 = " at ";
        r28 = r9;
        r9 = 0;
        switch(r14) {
            case 0: goto L_0x09eb;
            case 1: goto L_0x09a7;
            case 2: goto L_0x08e4;
            case 3: goto L_0x08d3;
            case 4: goto L_0x0852;
            case 5: goto L_0x07f4;
            case 6: goto L_0x07b7;
            case 7: goto L_0x077c;
            case 8: goto L_0x0741;
            case 9: goto L_0x0706;
            case 10: goto L_0x06cb;
            case 11: goto L_0x0690;
            case 12: goto L_0x0651;
            case 13: goto L_0x0616;
            case 14: goto L_0x05db;
            case 15: goto L_0x05a0;
            case 16: goto L_0x0533;
            case 17: goto L_0x04c0;
            case 18: goto L_0x0467;
            case 19: goto L_0x042c;
            case 20: goto L_0x0373;
            case 21: goto L_0x0360;
            case 22: goto L_0x0325;
            case 23: goto L_0x0273;
            case 24: goto L_0x023e;
            default: goto L_0x023a;
        };
    L_0x023a:
        r23 = 1;
        goto L_0x0a30;
    L_0x023e:
        r3 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x0267;
    L_0x0244:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x026c;
    L_0x0267:
        r4 = r1.mBugreportWhitelistedPackages;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.add(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x026c:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x0273:
        if (r11 == 0) goto L_0x031b;
    L_0x0275:
        r3 = "target";
        r3 = r10.getAttributeValue(r9, r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x02a9;
    L_0x027e:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = "> without target in ";
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x02a9:
        r7 = "allowed";
        r7 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r7 != 0) goto L_0x02dc;
    L_0x02b1:
        r9 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = "> without allowed in ";
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r9.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x02dc:
        r4 = r3.intern();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r3 = r4;
        r4 = r7.intern();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = r1.mAllowedAssociations;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = r7.get(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = (android.util.ArraySet) r7;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r7 != 0) goto L_0x02fa;
    L_0x02ef:
        r8 = new android.util.ArraySet;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = r8;
        r8 = r1.mAllowedAssociations;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8.put(r3, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x02fa:
        r8 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9 = "Adding association: ";
        r8.append(r9);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8.append(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9 = " <- ";
        r8.append(r9);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8 = r8.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.i(r6, r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.add(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x031e;
    L_0x031b:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x031e:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x0325:
        if (r22 == 0) goto L_0x0356;
    L_0x0327:
        r3 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x0350;
    L_0x032d:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0355;
    L_0x0350:
        r4 = r1.mHiddenApiPackageWhitelist;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.add(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0355:
        goto L_0x0359;
    L_0x0356:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0359:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x0360:
        if (r21 == 0) goto L_0x0369;
    L_0x0362:
        r1.readOemPermissions(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x0369:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x0373:
        if (r20 == 0) goto L_0x0422;
    L_0x0375:
        r3 = r30.toPath();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = android.os.Environment.getVendorDirectory();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = r7.toPath();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.append(r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.append(r5);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r4.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r3 = r3.startsWith(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x03ba;
    L_0x0396:
        r3 = r30.toPath();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = android.os.Environment.getOdmDirectory();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = r7.toPath();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.append(r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.append(r5);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r4.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r3 = r3.startsWith(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 == 0) goto L_0x03b8;
    L_0x03b7:
        goto L_0x03ba;
    L_0x03b8:
        r3 = 0;
        goto L_0x03bb;
    L_0x03ba:
        r3 = 1;
    L_0x03bb:
        r4 = r30.toPath();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8 = android.os.Environment.getProductDirectory();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8 = r8.toPath();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r5);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r4.startsWith(r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = r30.toPath();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9 = android.os.Environment.getProductServicesDirectory();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9 = r9.toPath();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8.append(r9);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8.append(r5);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8 = r8.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = r7.startsWith(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 == 0) goto L_0x0403;
    L_0x03fb:
        r8 = r1.mVendorPrivAppPermissions;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9 = r1.mVendorPrivAppDenyPermissions;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r1.readPrivAppPermissions(r10, r8, r9);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x041e;
    L_0x0403:
        if (r4 == 0) goto L_0x040d;
    L_0x0405:
        r8 = r1.mProductPrivAppPermissions;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9 = r1.mProductPrivAppDenyPermissions;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r1.readPrivAppPermissions(r10, r8, r9);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x041e;
    L_0x040d:
        if (r7 == 0) goto L_0x0417;
    L_0x040f:
        r8 = r1.mProductServicesPrivAppPermissions;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9 = r1.mProductServicesPrivAppDenyPermissions;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r1.readPrivAppPermissions(r10, r8, r9);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x041e;
    L_0x0417:
        r8 = r1.mPrivAppPermissions;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9 = r1.mPrivAppDenyPermissions;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r1.readPrivAppPermissions(r10, r8, r9);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x041e:
        r23 = 1;
        goto L_0x0a59;
    L_0x0422:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x042c:
        if (r19 == 0) goto L_0x045d;
    L_0x042e:
        r3 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x0457;
    L_0x0434:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x045c;
    L_0x0457:
        r4 = r1.mDisabledUntilUsedPreinstalledCarrierApps;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.add(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x045c:
        goto L_0x0460;
    L_0x045d:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0460:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x0467:
        if (r19 == 0) goto L_0x04b6;
    L_0x0469:
        r3 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = "carrierAppPackage";
        r7 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 == 0) goto L_0x0491;
    L_0x0475:
        if (r7 != 0) goto L_0x0478;
    L_0x0477:
        goto L_0x0491;
    L_0x0478:
        r4 = r1.mDisabledUntilUsedPreinstalledCarrierAssociatedApps;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r4.get(r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = (java.util.List) r4;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r4 != 0) goto L_0x048d;
    L_0x0482:
        r8 = new java.util.ArrayList;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r8;
        r8 = r1.mDisabledUntilUsedPreinstalledCarrierAssociatedApps;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8.put(r7, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x048d:
        r4.add(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x04b5;
    L_0x0491:
        r9 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = "> without package or carrierAppPackage in ";
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r9.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x04b5:
        goto L_0x04b9;
    L_0x04b6:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x04b9:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x04c0:
        if (r17 == 0) goto L_0x0529;
    L_0x04c2:
        r3 = "service";
        r3 = r10.getAttributeValue(r9, r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x04f0;
    L_0x04cb:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = "> without service in ";
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0528;
    L_0x04f0:
        r7 = android.content.ComponentName.unflattenFromString(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r7 != 0) goto L_0x0523;
    L_0x04f6:
        r9 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = "> with invalid service name ";
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = " in ";
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r9.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0528;
    L_0x0523:
        r4 = r1.mBackupTransportWhitelist;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.add(r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0528:
        goto L_0x052c;
    L_0x0529:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x052c:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x0533:
        if (r19 == 0) goto L_0x0596;
    L_0x0535:
        r3 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = "class";
        r7 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x0564;
    L_0x0541:
        r9 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r9.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0595;
    L_0x0564:
        if (r7 != 0) goto L_0x058b;
    L_0x0566:
        r9 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = "> without class in ";
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r9.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0595;
    L_0x058b:
        r4 = r1.mDefaultVrComponents;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8 = new android.content.ComponentName;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8.<init>(r3, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.add(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0595:
        goto L_0x0599;
    L_0x0596:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0599:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x05a0:
        if (r19 == 0) goto L_0x05d1;
    L_0x05a2:
        r3 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x05cb;
    L_0x05a8:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x05d0;
    L_0x05cb:
        r4 = r1.mSystemUserBlacklistedApps;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.add(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x05d0:
        goto L_0x05d4;
    L_0x05d1:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x05d4:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x05db:
        if (r19 == 0) goto L_0x060c;
    L_0x05dd:
        r3 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x0606;
    L_0x05e3:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x060b;
    L_0x0606:
        r4 = r1.mSystemUserWhitelistedApps;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.add(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x060b:
        goto L_0x060f;
    L_0x060c:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x060f:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x0616:
        if (r19 == 0) goto L_0x0647;
    L_0x0618:
        r3 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x0641;
    L_0x061e:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0646;
    L_0x0641:
        r4 = r1.mLinkedApps;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.add(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0646:
        goto L_0x064a;
    L_0x0647:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x064a:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x0651:
        if (r15 == 0) goto L_0x0686;
    L_0x0653:
        r3 = "action";
        r3 = r10.getAttributeValue(r9, r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x0680;
    L_0x065b:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = "> without action in ";
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0685;
    L_0x0680:
        r4 = r1.mAllowImplicitBroadcasts;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.add(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0685:
        goto L_0x0689;
    L_0x0686:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0689:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x0690:
        if (r15 == 0) goto L_0x06c1;
    L_0x0692:
        r3 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x06bb;
    L_0x0698:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x06c0;
    L_0x06bb:
        r4 = r1.mAllowIgnoreLocationSettings;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.add(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x06c0:
        goto L_0x06c4;
    L_0x06c1:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x06c4:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x06cb:
        if (r15 == 0) goto L_0x06fc;
    L_0x06cd:
        r3 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x06f6;
    L_0x06d3:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x06fb;
    L_0x06f6:
        r4 = r1.mAllowUnthrottledLocation;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.add(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x06fb:
        goto L_0x06ff;
    L_0x06fc:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x06ff:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x0706:
        if (r15 == 0) goto L_0x0737;
    L_0x0708:
        r3 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x0731;
    L_0x070e:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0736;
    L_0x0731:
        r4 = r1.mAllowInDataUsageSave;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.add(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0736:
        goto L_0x073a;
    L_0x0737:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x073a:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x0741:
        if (r15 == 0) goto L_0x0772;
    L_0x0743:
        r3 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x076c;
    L_0x0749:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0771;
    L_0x076c:
        r4 = r1.mAllowInPowerSave;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.add(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0771:
        goto L_0x0775;
    L_0x0772:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0775:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x077c:
        if (r15 == 0) goto L_0x07ad;
    L_0x077e:
        r3 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x07a7;
    L_0x0784:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x07ac;
    L_0x07a7:
        r4 = r1.mAllowInPowerSaveExceptIdle;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.add(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x07ac:
        goto L_0x07b0;
    L_0x07ad:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x07b0:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x07b7:
        if (r17 == 0) goto L_0x07ea;
    L_0x07b9:
        r3 = r10.getAttributeValue(r9, r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x07e4;
    L_0x07bf:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r12 = r28;
        r7.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x07e9;
    L_0x07e4:
        r4 = r1.mUnavailableFeatures;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4.add(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x07e9:
        goto L_0x07ed;
    L_0x07ea:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x07ed:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        goto L_0x0a59;
    L_0x07f4:
        r12 = r28;
        if (r17 == 0) goto L_0x0848;
    L_0x07f8:
        r3 = r10.getAttributeValue(r9, r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = "version";
        r14 = 0;
        r7 = com.android.internal.util.XmlUtils.readIntAttribute(r10, r7, r14);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r27 != 0) goto L_0x080a;
    L_0x0806:
        r9 = 1;
        r23 = 1;
        goto L_0x081d;
    L_0x080a:
        r14 = "notLowRam";
        r9 = r10.getAttributeValue(r9, r14);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r14 = "true";
        r14 = r14.equals(r9);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r23 = 1;
        r14 = r14 ^ 1;
        r9 = r14;
    L_0x081d:
        if (r3 != 0) goto L_0x0842;
    L_0x081f:
        r14 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r14.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r14.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r14.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r14.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r14.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r14.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r14.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r14.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0847;
    L_0x0842:
        if (r9 == 0) goto L_0x0847;
    L_0x0844:
        r1.addFeature(r3, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0847:
        goto L_0x084d;
    L_0x0848:
        r23 = 1;
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x084d:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0a59;
    L_0x0852:
        r12 = r28;
        r23 = 1;
        if (r16 == 0) goto L_0x08cb;
    L_0x0858:
        r3 = r10.getAttributeValue(r9, r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = "file";
        r7 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r14 = "dependency";
        r9 = r10.getAttributeValue(r9, r14);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x088d;
    L_0x086a:
        r14 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r14.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r14.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r14.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r14.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r14.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r14.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r14.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r14.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x08ca;
    L_0x088d:
        if (r7 != 0) goto L_0x08b4;
    L_0x088f:
        r12 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r12.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r12.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r12.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = "> without file in ";
        r12.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r12.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r12.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r12.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r12.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x08ca;
    L_0x08b4:
        r4 = new com.android.server.SystemConfig$SharedLibraryEntry;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r9 != 0) goto L_0x08bc;
    L_0x08b8:
        r8 = 0;
        r12 = new java.lang.String[r8];	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x08c2;
    L_0x08bc:
        r8 = ":";
        r12 = r9.split(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x08c2:
        r4.<init>(r3, r7, r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8 = r1.mSharedLibraries;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8.put(r3, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x08ca:
        goto L_0x08ce;
    L_0x08cb:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x08ce:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0a59;
    L_0x08d3:
        r23 = 1;
        if (r18 == 0) goto L_0x08dc;
    L_0x08d7:
        r1.readSplitPermission(r10, r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0a59;
    L_0x08dc:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0a59;
    L_0x08e4:
        r12 = r28;
        r23 = 1;
        if (r18 == 0) goto L_0x099f;
    L_0x08ea:
        r3 = r10.getAttributeValue(r9, r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x0917;
    L_0x08f0:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0a59;
    L_0x0917:
        r7 = "uid";
        r7 = r10.getAttributeValue(r9, r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r7 != 0) goto L_0x0949;
    L_0x0920:
        r9 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = "> without uid in ";
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r9.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r9.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0a59;
    L_0x0949:
        r9 = android.os.Process.getUidForName(r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r9 >= 0) goto L_0x0980;
    L_0x094f:
        r12 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r12.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r12.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r12.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = "> with unknown uid \"";
        r12.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r12.append(r7);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = "  in ";
        r12.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r12.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r12.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r12.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r12.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0a59;
    L_0x0980:
        r4 = r3.intern();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r3 = r4;
        r4 = r1.mSystemPermissions;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r4.get(r9);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = (android.util.ArraySet) r4;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r4 != 0) goto L_0x099a;
    L_0x098f:
        r8 = new android.util.ArraySet;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r8;
        r8 = r1.mSystemPermissions;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r8.put(r9, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x099a:
        r4.add(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x09a2;
    L_0x099f:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x09a2:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0a59;
    L_0x09a7:
        r12 = r28;
        r23 = 1;
        if (r18 == 0) goto L_0x09e4;
    L_0x09ad:
        r3 = r10.getAttributeValue(r9, r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 != 0) goto L_0x09da;
    L_0x09b3:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r12);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0a59;
    L_0x09da:
        r4 = r3.intern();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r3 = r4;
        r1.readPermission(r10, r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0a59;
    L_0x09e4:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0a59;
    L_0x09eb:
        r23 = 1;
        if (r15 == 0) goto L_0x0a29;
    L_0x09ef:
        r3 = "gid";
        r3 = r10.getAttributeValue(r9, r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        if (r3 == 0) goto L_0x0a04;
    L_0x09f7:
        r4 = android.os.Process.getGidForName(r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = r1.mGlobalGids;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7 = com.android.internal.util.ArrayUtils.appendInt(r7, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r1.mGlobalGids = r7;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0a28;
    L_0x0a04:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = "> without gid in ";
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r7.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r7.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0a28:
        goto L_0x0a2c;
    L_0x0a29:
        r1.logNotAllowedInPartition(r13, r2, r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0a2c:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        goto L_0x0a59;
    L_0x0a30:
        r3 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r3.<init>();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = "Tag ";
        r3.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r3.append(r13);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = " is unknown in ";
        r3.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r3.append(r2);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r3.append(r8);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = r10.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r3.append(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r3 = r3.toString();	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        android.util.Slog.w(r6, r3);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0a59:
        r3 = r31;
        r14 = r23;
        r12 = r24;
        r7 = r25;
        r4 = r26;
        r8 = r27;
        r13 = 2;
        goto L_0x00d5;
    L_0x0a68:
        r26 = r4;
        r25 = r7;
        r27 = r8;
        r24 = r12;
        r3 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        r4 = "No start tag found";
        r3.<init>(r4);	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
        throw r3;	 Catch:{ XmlPullParserException -> 0x0a7b, IOException -> 0x0a78 }
    L_0x0a78:
        r0 = move-exception;
        r3 = r0;
        goto L_0x0a90;
    L_0x0a7b:
        r0 = move-exception;
        r3 = r0;
        r4 = r26;
        goto L_0x0a9d;
    L_0x0a80:
        r0 = move-exception;
        r25 = r7;
        r27 = r8;
        r3 = r0;
        goto L_0x0aed;
    L_0x0a88:
        r0 = move-exception;
        r26 = r4;
        r25 = r7;
        r27 = r8;
        r3 = r0;
    L_0x0a90:
        r4 = r26;
        android.util.Slog.w(r6, r4, r3);	 Catch:{ all -> 0x0aeb }
        goto L_0x0aa1;
    L_0x0a97:
        r0 = move-exception;
        r25 = r7;
        r27 = r8;
        r3 = r0;
    L_0x0a9d:
        android.util.Slog.w(r6, r4, r3);	 Catch:{ all -> 0x0aeb }
    L_0x0aa1:
        libcore.io.IoUtils.closeQuietly(r25);
    L_0x0aa5:
        r3 = android.os.storage.StorageManager.isFileEncryptedNativeOnly();
        if (r3 == 0) goto L_0x0ab7;
    L_0x0aab:
        r3 = "android.software.file_based_encryption";
        r4 = 0;
        r1.addFeature(r3, r4);
        r3 = "android.software.securely_removes_users";
        r1.addFeature(r3, r4);
        goto L_0x0ab8;
    L_0x0ab7:
        r4 = 0;
    L_0x0ab8:
        r3 = android.os.storage.StorageManager.hasAdoptable();
        if (r3 == 0) goto L_0x0ac3;
    L_0x0abe:
        r3 = "android.software.adoptable_storage";
        r1.addFeature(r3, r4);
    L_0x0ac3:
        r3 = android.app.ActivityManager.isLowRamDeviceStatic();
        if (r3 == 0) goto L_0x0acf;
    L_0x0ac9:
        r3 = "android.hardware.ram.low";
        r1.addFeature(r3, r4);
        goto L_0x0ad4;
    L_0x0acf:
        r3 = "android.hardware.ram.normal";
        r1.addFeature(r3, r4);
    L_0x0ad4:
        r3 = r1.mUnavailableFeatures;
        r3 = r3.iterator();
    L_0x0ada:
        r4 = r3.hasNext();
        if (r4 == 0) goto L_0x0aea;
    L_0x0ae0:
        r4 = r3.next();
        r4 = (java.lang.String) r4;
        r1.removeFeature(r4);
        goto L_0x0ada;
    L_0x0aea:
        return;
    L_0x0aeb:
        r0 = move-exception;
        r3 = r0;
    L_0x0aed:
        libcore.io.IoUtils.closeQuietly(r25);
        throw r3;
    L_0x0af1:
        r0 = move-exception;
        r3 = r0;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Couldn't find or open permissions file ";
        r4.append(r5);
        r4.append(r2);
        r4 = r4.toString();
        android.util.Slog.w(r6, r4);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.SystemConfig.readPermissionsFromXml(java.io.File, int):void");
    }

    private void addFeature(String name, int version) {
        FeatureInfo fi = (FeatureInfo) this.mAvailableFeatures.get(name);
        if (fi == null) {
            fi = new FeatureInfo();
            fi.name = name;
            fi.version = version;
            this.mAvailableFeatures.put(name, fi);
            return;
        }
        fi.version = Math.max(fi.version, version);
    }

    private void removeFeature(String name) {
        if (this.mAvailableFeatures.remove(name) != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Removed unavailable feature ");
            stringBuilder.append(name);
            Slog.d(TAG, stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void readPermission(XmlPullParser parser, String name) throws IOException, XmlPullParserException {
        if (this.mPermissions.containsKey(name)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Duplicate permission definition for ");
            stringBuilder.append(name);
            throw new IllegalStateException(stringBuilder.toString());
        }
        PermissionEntry perm = new PermissionEntry(name, XmlUtils.readBooleanAttribute(parser, "perUser", false));
        this.mPermissions.put(name, perm);
        int outerDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == 1) {
                return;
            }
            if (type == 3 && parser.getDepth() <= outerDepth) {
                return;
            }
            if (type != 3) {
                if (type != 4) {
                    if ("group".equals(parser.getName())) {
                        String gidStr = parser.getAttributeValue(null, "gid");
                        if (gidStr != null) {
                            perm.gids = ArrayUtils.appendInt(perm.gids, Process.getGidForName(gidStr));
                        } else {
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("<group> without gid at ");
                            stringBuilder2.append(parser.getPositionDescription());
                            Slog.w(TAG, stringBuilder2.toString());
                        }
                    }
                    XmlUtils.skipCurrentTag(parser);
                }
            }
        }
    }

    private void readPrivAppPermissions(XmlPullParser parser, ArrayMap<String, ArraySet<String>> grantMap, ArrayMap<String, ArraySet<String>> denyMap) throws IOException, XmlPullParserException {
        String packageName = parser.getAttributeValue(null, "package");
        boolean isEmpty = TextUtils.isEmpty(packageName);
        String str = TAG;
        if (isEmpty) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("package is required for <privapp-permissions> in ");
            stringBuilder.append(parser.getPositionDescription());
            Slog.w(str, stringBuilder.toString());
            return;
        }
        ArraySet<String> permissions = (ArraySet) grantMap.get(packageName);
        if (permissions == null) {
            permissions = new ArraySet();
        }
        ArraySet<String> denyPermissions = (ArraySet) denyMap.get(packageName);
        int depth = parser.getDepth();
        while (XmlUtils.nextElementWithin(parser, depth)) {
            String name = parser.getName();
            String str2 = "name";
            String permName;
            StringBuilder stringBuilder2;
            if ("permission".equals(name)) {
                permName = parser.getAttributeValue(null, str2);
                if (TextUtils.isEmpty(permName)) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("name is required for <permission> in ");
                    stringBuilder2.append(parser.getPositionDescription());
                    Slog.w(str, stringBuilder2.toString());
                } else {
                    permissions.add(permName);
                }
            } else if ("deny-permission".equals(name)) {
                permName = parser.getAttributeValue(null, str2);
                if (TextUtils.isEmpty(permName)) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("name is required for <deny-permission> in ");
                    stringBuilder2.append(parser.getPositionDescription());
                    Slog.w(str, stringBuilder2.toString());
                } else {
                    if (denyPermissions == null) {
                        denyPermissions = new ArraySet();
                    }
                    denyPermissions.add(permName);
                }
            }
        }
        grantMap.put(packageName, permissions);
        if (denyPermissions != null) {
            denyMap.put(packageName, denyPermissions);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void readOemPermissions(XmlPullParser parser) throws IOException, XmlPullParserException {
        String packageName = parser.getAttributeValue(null, "package");
        boolean isEmpty = TextUtils.isEmpty(packageName);
        String str = TAG;
        if (isEmpty) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("package is required for <oem-permissions> in ");
            stringBuilder.append(parser.getPositionDescription());
            Slog.w(str, stringBuilder.toString());
            return;
        }
        ArrayMap<String, Boolean> permissions = (ArrayMap) this.mOemPermissions.get(packageName);
        if (permissions == null) {
            permissions = new ArrayMap();
        }
        int depth = parser.getDepth();
        while (XmlUtils.nextElementWithin(parser, depth)) {
            String name = parser.getName();
            String str2 = "name";
            String permName;
            StringBuilder stringBuilder2;
            if ("permission".equals(name)) {
                permName = parser.getAttributeValue(null, str2);
                if (TextUtils.isEmpty(permName)) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("name is required for <permission> in ");
                    stringBuilder2.append(parser.getPositionDescription());
                    Slog.w(str, stringBuilder2.toString());
                } else {
                    permissions.put(permName, Boolean.TRUE);
                }
            } else if ("deny-permission".equals(name)) {
                permName = parser.getAttributeValue(null, str2);
                if (TextUtils.isEmpty(permName)) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("name is required for <deny-permission> in ");
                    stringBuilder2.append(parser.getPositionDescription());
                    Slog.w(str, stringBuilder2.toString());
                } else {
                    permissions.put(permName, Boolean.FALSE);
                }
            }
        }
        this.mOemPermissions.put(packageName, permissions);
    }

    private void readSplitPermission(XmlPullParser parser, File permFile) throws IOException, XmlPullParserException {
        String str = "name";
        String splitPerm = parser.getAttributeValue(null, str);
        String str2 = " at ";
        String str3 = TAG;
        if (splitPerm == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<split-permission> without name in ");
            stringBuilder.append(permFile);
            stringBuilder.append(str2);
            stringBuilder.append(parser.getPositionDescription());
            Slog.w(str3, stringBuilder.toString());
            XmlUtils.skipCurrentTag(parser);
            return;
        }
        String targetSdkStr = parser.getAttributeValue(null, "targetSdk");
        int targetSdk = 10001;
        if (!TextUtils.isEmpty(targetSdkStr)) {
            try {
                str2 = Integer.parseInt(targetSdkStr);
                targetSdk = str2;
            } catch (NumberFormatException e) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("<split-permission> targetSdk not an integer in ");
                stringBuilder2.append(permFile);
                stringBuilder2.append(str2);
                stringBuilder2.append(parser.getPositionDescription());
                Slog.w(str3, stringBuilder2.toString());
                XmlUtils.skipCurrentTag(parser);
                return;
            }
        }
        int depth = parser.getDepth();
        List<String> newPermissions = new ArrayList();
        while (XmlUtils.nextElementWithin(parser, depth)) {
            if ("new-permission".equals(parser.getName())) {
                String newName = parser.getAttributeValue(null, str);
                if (TextUtils.isEmpty(newName)) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("name is required for <new-permission> in ");
                    stringBuilder3.append(parser.getPositionDescription());
                    Slog.w(str3, stringBuilder3.toString());
                } else {
                    newPermissions.add(newName);
                }
            } else {
                XmlUtils.skipCurrentTag(parser);
            }
        }
        if (!newPermissions.isEmpty()) {
            this.mSplitPermissions.add(new SplitPermissionInfo(splitPerm, newPermissions, targetSdk));
        }
    }
}
