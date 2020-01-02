package android.content.pm;

import android.annotation.UnsupportedAppUsage;
import android.os.PersistableBundle;
import android.util.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import java.util.Arrays;

public class PackageUserState {
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "PackageUserState";
    public int appLinkGeneration;
    public int categoryHint;
    public long ceDataInode;
    public SuspendDialogInfo dialogInfo;
    public ArraySet<String> disabledComponents;
    public int distractionFlags;
    public int domainVerificationStatus;
    public int enabled;
    public ArraySet<String> enabledComponents;
    public String harmfulAppWarning;
    public boolean hidden;
    public int installReason;
    public boolean installed;
    public boolean instantApp;
    public String lastDisableAppCaller;
    public boolean notLaunched;
    public String[] overlayPaths;
    public boolean stopped;
    public boolean suspended;
    public PersistableBundle suspendedAppExtras;
    public PersistableBundle suspendedLauncherExtras;
    public String suspendingPackage;
    public boolean virtualPreload;

    @UnsupportedAppUsage
    public PackageUserState() {
        this.categoryHint = -1;
        this.installed = true;
        this.hidden = false;
        this.suspended = false;
        this.enabled = 0;
        this.domainVerificationStatus = 0;
        this.installReason = 0;
    }

    @VisibleForTesting
    public PackageUserState(PackageUserState o) {
        this.categoryHint = -1;
        this.ceDataInode = o.ceDataInode;
        this.installed = o.installed;
        this.stopped = o.stopped;
        this.notLaunched = o.notLaunched;
        this.hidden = o.hidden;
        this.distractionFlags = o.distractionFlags;
        this.suspended = o.suspended;
        this.suspendingPackage = o.suspendingPackage;
        this.dialogInfo = o.dialogInfo;
        this.suspendedAppExtras = o.suspendedAppExtras;
        this.suspendedLauncherExtras = o.suspendedLauncherExtras;
        this.instantApp = o.instantApp;
        this.virtualPreload = o.virtualPreload;
        this.enabled = o.enabled;
        this.lastDisableAppCaller = o.lastDisableAppCaller;
        this.domainVerificationStatus = o.domainVerificationStatus;
        this.appLinkGeneration = o.appLinkGeneration;
        this.categoryHint = o.categoryHint;
        this.installReason = o.installReason;
        this.disabledComponents = ArrayUtils.cloneOrNull(o.disabledComponents);
        this.enabledComponents = ArrayUtils.cloneOrNull(o.enabledComponents);
        String[] strArr = o.overlayPaths;
        this.overlayPaths = strArr == null ? null : (String[]) Arrays.copyOf(strArr, strArr.length);
        this.harmfulAppWarning = o.harmfulAppWarning;
    }

    public boolean isAvailable(int flags) {
        boolean matchAnyUser = (4194304 & flags) != 0;
        boolean matchUninstalled = (flags & 8192) != 0;
        if (matchAnyUser) {
            return true;
        }
        if (!this.installed || (this.hidden && !matchUninstalled)) {
            return false;
        }
        return true;
    }

    public boolean isMatch(ComponentInfo componentInfo, int flags) {
        boolean isSystemApp = componentInfo.applicationInfo.isSystemApp();
        boolean z = true;
        boolean matchUninstalled = (PackageManager.MATCH_KNOWN_PACKAGES & flags) != 0;
        if (!isAvailable(flags) && (!isSystemApp || !matchUninstalled)) {
            return reportIfDebug(false, flags);
        }
        if (!isEnabled(componentInfo, flags)) {
            return reportIfDebug(false, flags);
        }
        if ((1048576 & flags) != 0 && !isSystemApp) {
            return reportIfDebug(false, flags);
        }
        boolean matchesUnaware = ((262144 & flags) == 0 || componentInfo.directBootAware) ? false : true;
        boolean matchesAware = (524288 & flags) != 0 && componentInfo.directBootAware;
        if (!(matchesUnaware || matchesAware)) {
            z = false;
        }
        return reportIfDebug(z, flags);
    }

    private boolean reportIfDebug(boolean result, int flags) {
        return result;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x002f  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x002e A:{RETURN} */
    public boolean isEnabled(android.content.pm.ComponentInfo r5, int r6) {
        /*
        r4 = this;
        r0 = r6 & 512;
        r1 = 1;
        if (r0 == 0) goto L_0x0006;
    L_0x0005:
        return r1;
    L_0x0006:
        r0 = r4.enabled;
        r2 = 0;
        if (r0 == 0) goto L_0x001d;
    L_0x000b:
        r3 = 2;
        if (r0 == r3) goto L_0x001c;
    L_0x000e:
        r3 = 3;
        if (r0 == r3) goto L_0x001c;
    L_0x0011:
        r3 = 4;
        if (r0 == r3) goto L_0x0015;
    L_0x0014:
        goto L_0x0024;
    L_0x0015:
        r0 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r0 = r0 & r6;
        if (r0 != 0) goto L_0x001d;
    L_0x001b:
        return r2;
    L_0x001c:
        return r2;
    L_0x001d:
        r0 = r5.applicationInfo;
        r0 = r0.enabled;
        if (r0 != 0) goto L_0x0024;
    L_0x0023:
        return r2;
    L_0x0024:
        r0 = r4.enabledComponents;
        r3 = r5.name;
        r0 = com.android.internal.util.ArrayUtils.contains(r0, r3);
        if (r0 == 0) goto L_0x002f;
    L_0x002e:
        return r1;
    L_0x002f:
        r0 = r4.disabledComponents;
        r1 = r5.name;
        r0 = com.android.internal.util.ArrayUtils.contains(r0, r1);
        if (r0 == 0) goto L_0x003a;
    L_0x0039:
        return r2;
    L_0x003a:
        r0 = r5.enabled;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageUserState.isEnabled(android.content.pm.ComponentInfo, int):boolean");
    }

    /* JADX WARNING: Missing block: B:38:0x006c, code skipped:
            return false;
     */
    public final boolean equals(java.lang.Object r7) {
        /*
        r6 = this;
        r0 = r7 instanceof android.content.pm.PackageUserState;
        r1 = 0;
        if (r0 != 0) goto L_0x0006;
    L_0x0005:
        return r1;
    L_0x0006:
        r0 = r7;
        r0 = (android.content.pm.PackageUserState) r0;
        r2 = r6.ceDataInode;
        r4 = r0.ceDataInode;
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 == 0) goto L_0x0012;
    L_0x0011:
        return r1;
    L_0x0012:
        r2 = r6.installed;
        r3 = r0.installed;
        if (r2 == r3) goto L_0x0019;
    L_0x0018:
        return r1;
    L_0x0019:
        r2 = r6.stopped;
        r3 = r0.stopped;
        if (r2 == r3) goto L_0x0020;
    L_0x001f:
        return r1;
    L_0x0020:
        r2 = r6.notLaunched;
        r3 = r0.notLaunched;
        if (r2 == r3) goto L_0x0027;
    L_0x0026:
        return r1;
    L_0x0027:
        r2 = r6.hidden;
        r3 = r0.hidden;
        if (r2 == r3) goto L_0x002e;
    L_0x002d:
        return r1;
    L_0x002e:
        r2 = r6.distractionFlags;
        r3 = r0.distractionFlags;
        if (r2 == r3) goto L_0x0035;
    L_0x0034:
        return r1;
    L_0x0035:
        r2 = r6.suspended;
        r3 = r0.suspended;
        if (r2 == r3) goto L_0x003c;
    L_0x003b:
        return r1;
    L_0x003c:
        if (r2 == 0) goto L_0x006d;
    L_0x003e:
        r2 = r6.suspendingPackage;
        if (r2 == 0) goto L_0x006c;
    L_0x0042:
        r3 = r0.suspendingPackage;
        r2 = r2.equals(r3);
        if (r2 != 0) goto L_0x004b;
    L_0x004a:
        goto L_0x006c;
    L_0x004b:
        r2 = r6.dialogInfo;
        r3 = r0.dialogInfo;
        r2 = java.util.Objects.equals(r2, r3);
        if (r2 != 0) goto L_0x0056;
    L_0x0055:
        return r1;
    L_0x0056:
        r2 = r6.suspendedAppExtras;
        r3 = r0.suspendedAppExtras;
        r2 = android.os.BaseBundle.kindofEquals(r2, r3);
        if (r2 != 0) goto L_0x0061;
    L_0x0060:
        return r1;
    L_0x0061:
        r2 = r6.suspendedLauncherExtras;
        r3 = r0.suspendedLauncherExtras;
        r2 = android.os.BaseBundle.kindofEquals(r2, r3);
        if (r2 != 0) goto L_0x006d;
    L_0x006b:
        return r1;
    L_0x006c:
        return r1;
    L_0x006d:
        r2 = r6.instantApp;
        r3 = r0.instantApp;
        if (r2 == r3) goto L_0x0074;
    L_0x0073:
        return r1;
    L_0x0074:
        r2 = r6.virtualPreload;
        r3 = r0.virtualPreload;
        if (r2 == r3) goto L_0x007b;
    L_0x007a:
        return r1;
    L_0x007b:
        r2 = r6.enabled;
        r3 = r0.enabled;
        if (r2 == r3) goto L_0x0082;
    L_0x0081:
        return r1;
    L_0x0082:
        r2 = r6.lastDisableAppCaller;
        if (r2 != 0) goto L_0x008a;
    L_0x0086:
        r2 = r0.lastDisableAppCaller;
        if (r2 != 0) goto L_0x0096;
    L_0x008a:
        r2 = r6.lastDisableAppCaller;
        if (r2 == 0) goto L_0x0097;
    L_0x008e:
        r3 = r0.lastDisableAppCaller;
        r2 = r2.equals(r3);
        if (r2 != 0) goto L_0x0097;
    L_0x0096:
        return r1;
    L_0x0097:
        r2 = r6.domainVerificationStatus;
        r3 = r0.domainVerificationStatus;
        if (r2 == r3) goto L_0x009e;
    L_0x009d:
        return r1;
    L_0x009e:
        r2 = r6.appLinkGeneration;
        r3 = r0.appLinkGeneration;
        if (r2 == r3) goto L_0x00a5;
    L_0x00a4:
        return r1;
    L_0x00a5:
        r2 = r6.categoryHint;
        r3 = r0.categoryHint;
        if (r2 == r3) goto L_0x00ac;
    L_0x00ab:
        return r1;
    L_0x00ac:
        r2 = r6.installReason;
        r3 = r0.installReason;
        if (r2 == r3) goto L_0x00b3;
    L_0x00b2:
        return r1;
    L_0x00b3:
        r2 = r6.disabledComponents;
        if (r2 != 0) goto L_0x00bb;
    L_0x00b7:
        r2 = r0.disabledComponents;
        if (r2 != 0) goto L_0x00c3;
    L_0x00bb:
        r2 = r6.disabledComponents;
        if (r2 == 0) goto L_0x00c4;
    L_0x00bf:
        r2 = r0.disabledComponents;
        if (r2 != 0) goto L_0x00c4;
    L_0x00c3:
        return r1;
    L_0x00c4:
        r2 = r6.disabledComponents;
        r3 = 1;
        if (r2 == 0) goto L_0x00f1;
    L_0x00c9:
        r2 = r2.size();
        r4 = r0.disabledComponents;
        r4 = r4.size();
        if (r2 == r4) goto L_0x00d6;
    L_0x00d5:
        return r1;
    L_0x00d6:
        r2 = r6.disabledComponents;
        r2 = r2.size();
        r2 = r2 - r3;
    L_0x00dd:
        if (r2 < 0) goto L_0x00f1;
    L_0x00df:
        r4 = r0.disabledComponents;
        r5 = r6.disabledComponents;
        r5 = r5.valueAt(r2);
        r4 = r4.contains(r5);
        if (r4 != 0) goto L_0x00ee;
    L_0x00ed:
        return r1;
    L_0x00ee:
        r2 = r2 + -1;
        goto L_0x00dd;
    L_0x00f1:
        r2 = r6.enabledComponents;
        if (r2 != 0) goto L_0x00f9;
    L_0x00f5:
        r2 = r0.enabledComponents;
        if (r2 != 0) goto L_0x0101;
    L_0x00f9:
        r2 = r6.enabledComponents;
        if (r2 == 0) goto L_0x0102;
    L_0x00fd:
        r2 = r0.enabledComponents;
        if (r2 != 0) goto L_0x0102;
    L_0x0101:
        return r1;
    L_0x0102:
        r2 = r6.enabledComponents;
        if (r2 == 0) goto L_0x012e;
    L_0x0106:
        r2 = r2.size();
        r4 = r0.enabledComponents;
        r4 = r4.size();
        if (r2 == r4) goto L_0x0113;
    L_0x0112:
        return r1;
    L_0x0113:
        r2 = r6.enabledComponents;
        r2 = r2.size();
        r2 = r2 - r3;
    L_0x011a:
        if (r2 < 0) goto L_0x012e;
    L_0x011c:
        r4 = r0.enabledComponents;
        r5 = r6.enabledComponents;
        r5 = r5.valueAt(r2);
        r4 = r4.contains(r5);
        if (r4 != 0) goto L_0x012b;
    L_0x012a:
        return r1;
    L_0x012b:
        r2 = r2 + -1;
        goto L_0x011a;
    L_0x012e:
        r2 = r6.harmfulAppWarning;
        if (r2 != 0) goto L_0x0136;
    L_0x0132:
        r2 = r0.harmfulAppWarning;
        if (r2 != 0) goto L_0x0142;
    L_0x0136:
        r2 = r6.harmfulAppWarning;
        if (r2 == 0) goto L_0x0143;
    L_0x013a:
        r4 = r0.harmfulAppWarning;
        r2 = r2.equals(r4);
        if (r2 != 0) goto L_0x0143;
    L_0x0142:
        return r1;
    L_0x0143:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageUserState.equals(java.lang.Object):boolean");
    }
}
