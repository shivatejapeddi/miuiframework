package android.content.res;

import android.annotation.UnsupportedAppUsage;
import android.miui.BiometricConnect;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.io.FileDescriptor;
import java.io.IOException;

public final class ApkAssets {
    @GuardedBy({"this"})
    private final long mNativePtr;
    @GuardedBy({"this"})
    private boolean mOpen = true;
    @GuardedBy({"this"})
    private final StringBlock mStringBlock;

    private static native void nativeDestroy(long j);

    private static native String nativeGetAssetPath(long j);

    private static native long nativeGetStringBlock(long j);

    private static native boolean nativeIsUpToDate(long j);

    private static native long nativeLoad(String str, boolean z, boolean z2, boolean z3) throws IOException;

    private static native long nativeLoadFromFd(FileDescriptor fileDescriptor, String str, boolean z, boolean z2) throws IOException;

    private static native long nativeOpenXml(long j, String str) throws IOException;

    public static ApkAssets loadFromPath(String path) throws IOException {
        return new ApkAssets(path, false, false, false);
    }

    public static ApkAssets loadFromPath(String path, boolean system) throws IOException {
        return new ApkAssets(path, system, false, false);
    }

    public static ApkAssets loadFromPath(String path, boolean system, boolean forceSharedLibrary) throws IOException {
        return new ApkAssets(path, system, forceSharedLibrary, false);
    }

    public static ApkAssets loadFromFd(FileDescriptor fd, String friendlyName, boolean system, boolean forceSharedLibrary) throws IOException {
        return new ApkAssets(fd, friendlyName, system, forceSharedLibrary);
    }

    public static ApkAssets loadOverlayFromPath(String idmapPath, boolean system) throws IOException {
        return new ApkAssets(idmapPath, system, false, true);
    }

    private ApkAssets(String path, boolean system, boolean forceSharedLib, boolean overlay) throws IOException {
        Preconditions.checkNotNull(path, "path");
        this.mNativePtr = nativeLoad(path, system, forceSharedLib, overlay);
        this.mStringBlock = new StringBlock(nativeGetStringBlock(this.mNativePtr), true);
    }

    private ApkAssets(FileDescriptor fd, String friendlyName, boolean system, boolean forceSharedLib) throws IOException {
        Preconditions.checkNotNull(fd, BiometricConnect.MSG_CB_BUNDLE_SHAREMEM_FD);
        Preconditions.checkNotNull(friendlyName, "friendlyName");
        this.mNativePtr = nativeLoadFromFd(fd, friendlyName, system, forceSharedLib);
        this.mStringBlock = new StringBlock(nativeGetStringBlock(this.mNativePtr), true);
    }

    @UnsupportedAppUsage
    public String getAssetPath() {
        String nativeGetAssetPath;
        synchronized (this) {
            nativeGetAssetPath = nativeGetAssetPath(this.mNativePtr);
        }
        return nativeGetAssetPath;
    }

    /* Access modifiers changed, original: 0000 */
    public CharSequence getStringFromPool(int idx) {
        CharSequence charSequence;
        synchronized (this) {
            charSequence = this.mStringBlock.get(idx);
        }
        return charSequence;
    }

    /* JADX WARNING: Missing block: B:19:?, code skipped:
            r2.close();
     */
    /* JADX WARNING: Missing block: B:20:0x002d, code skipped:
            r5 = move-exception;
     */
    /* JADX WARNING: Missing block: B:22:?, code skipped:
            r3.addSuppressed(r5);
     */
    public android.content.res.XmlResourceParser openXml(java.lang.String r7) throws java.io.IOException {
        /*
        r6 = this;
        r0 = "fileName";
        com.android.internal.util.Preconditions.checkNotNull(r7, r0);
        monitor-enter(r6);
        r0 = r6.mNativePtr;	 Catch:{ all -> 0x0032 }
        r0 = nativeOpenXml(r0, r7);	 Catch:{ all -> 0x0032 }
        r2 = new android.content.res.XmlBlock;	 Catch:{ all -> 0x0032 }
        r3 = 0;
        r2.<init>(r3, r0);	 Catch:{ all -> 0x0032 }
        r3 = r2.newParser();	 Catch:{ all -> 0x0026 }
        if (r3 == 0) goto L_0x001e;
        r2.close();	 Catch:{ all -> 0x0032 }
        monitor-exit(r6);	 Catch:{ all -> 0x0032 }
        return r3;
    L_0x001e:
        r4 = new java.lang.AssertionError;	 Catch:{ all -> 0x0026 }
        r5 = "block.newParser() returned a null parser";
        r4.<init>(r5);	 Catch:{ all -> 0x0026 }
        throw r4;	 Catch:{ all -> 0x0026 }
    L_0x0026:
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x0028 }
    L_0x0028:
        r4 = move-exception;
        r2.close();	 Catch:{ all -> 0x002d }
        goto L_0x0031;
    L_0x002d:
        r5 = move-exception;
        r3.addSuppressed(r5);	 Catch:{ all -> 0x0032 }
    L_0x0031:
        throw r4;	 Catch:{ all -> 0x0032 }
    L_0x0032:
        r0 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x0032 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.res.ApkAssets.openXml(java.lang.String):android.content.res.XmlResourceParser");
    }

    public boolean isUpToDate() {
        boolean nativeIsUpToDate;
        synchronized (this) {
            nativeIsUpToDate = nativeIsUpToDate(this.mNativePtr);
        }
        return nativeIsUpToDate;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ApkAssets{path=");
        stringBuilder.append(getAssetPath());
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        close();
    }

    public void close() throws Throwable {
        synchronized (this) {
            if (this.mOpen) {
                this.mOpen = false;
                this.mStringBlock.close();
                nativeDestroy(this.mNativePtr);
            }
        }
    }
}
