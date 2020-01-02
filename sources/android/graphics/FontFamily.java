package android.graphics;

import android.annotation.UnsupportedAppUsage;
import android.content.res.AssetManager;
import android.graphics.fonts.FontVariationAxis;
import android.text.TextUtils;
import java.nio.ByteBuffer;
import libcore.util.NativeAllocationRegistry;

@Deprecated
public class FontFamily {
    private static String TAG = "FontFamily";
    private static final NativeAllocationRegistry sBuilderRegistry = NativeAllocationRegistry.createMalloced(FontFamily.class.getClassLoader(), nGetBuilderReleaseFunc());
    private static final NativeAllocationRegistry sFamilyRegistry = NativeAllocationRegistry.createMalloced(FontFamily.class.getClassLoader(), nGetFamilyReleaseFunc());
    private long mBuilderPtr;
    private Runnable mNativeBuilderCleaner;
    @UnsupportedAppUsage(trackingBug = 123768928)
    public long mNativePtr;

    private static native void nAddAxisValue(long j, int i, float f);

    private static native boolean nAddFont(long j, ByteBuffer byteBuffer, int i, int i2, int i3);

    private static native boolean nAddFontFromAssetManager(long j, AssetManager assetManager, String str, int i, boolean z, int i2, int i3, int i4);

    private static native boolean nAddFontWeightStyle(long j, ByteBuffer byteBuffer, int i, int i2, int i3);

    private static native long nCreateFamily(long j);

    private static native long nGetBuilderReleaseFunc();

    private static native long nGetFamilyReleaseFunc();

    private static native long nInitBuilder(String str, int i);

    @UnsupportedAppUsage(trackingBug = 123768928)
    public FontFamily() {
        this.mBuilderPtr = nInitBuilder(null, 0);
        this.mNativeBuilderCleaner = sBuilderRegistry.registerNativeAllocation(this, this.mBuilderPtr);
    }

    @UnsupportedAppUsage(trackingBug = 123768928)
    public FontFamily(String[] langs, int variant) {
        String langsString;
        if (langs == null || langs.length == 0) {
            langsString = null;
        } else if (langs.length == 1) {
            langsString = langs[null];
        } else {
            langsString = TextUtils.join((CharSequence) ",", (Object[]) langs);
        }
        this.mBuilderPtr = nInitBuilder(langsString, variant);
        this.mNativeBuilderCleaner = sBuilderRegistry.registerNativeAllocation(this, this.mBuilderPtr);
    }

    @UnsupportedAppUsage(trackingBug = 123768928)
    public boolean freeze() {
        long j = this.mBuilderPtr;
        if (j != 0) {
            this.mNativePtr = nCreateFamily(j);
            this.mNativeBuilderCleaner.run();
            this.mBuilderPtr = 0;
            j = this.mNativePtr;
            if (j != 0) {
                sFamilyRegistry.registerNativeAllocation(this, j);
            }
            return this.mNativePtr != 0;
        } else {
            throw new IllegalStateException("This FontFamily is already frozen");
        }
    }

    @UnsupportedAppUsage(trackingBug = 123768928)
    public void abortCreation() {
        if (this.mBuilderPtr != 0) {
            this.mNativeBuilderCleaner.run();
            this.mBuilderPtr = 0;
            return;
        }
        throw new IllegalStateException("This FontFamily is already frozen or abandoned");
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x002b A:{LOOP_END, LOOP:1: B:10:0x0029->B:11:0x002b, Catch:{ all -> 0x004d, all -> 0x0050 }} */
    @android.annotation.UnsupportedAppUsage(trackingBug = 123768928)
    public boolean addFont(java.lang.String r19, int r20, android.graphics.fonts.FontVariationAxis[] r21, int r22, int r23) {
        /*
        r18 = this;
        r1 = r18;
        r2 = r19;
        r3 = r21;
        r4 = r1.mBuilderPtr;
        r6 = 0;
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 == 0) goto L_0x0074;
    L_0x000e:
        r4 = 0;
        r0 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x005c }
        r0.<init>(r2);	 Catch:{ IOException -> 0x005c }
        r5 = r0;
        r6 = r5.getChannel();	 Catch:{ all -> 0x004d }
        r10 = r6.size();	 Catch:{ all -> 0x004d }
        r7 = java.nio.channels.FileChannel.MapMode.READ_ONLY;	 Catch:{ all -> 0x004d }
        r8 = 0;
        r14 = r6.map(r7, r8, r10);	 Catch:{ all -> 0x004d }
        if (r3 == 0) goto L_0x003d;
    L_0x0027:
        r0 = r3.length;	 Catch:{ all -> 0x004d }
        r7 = r4;
    L_0x0029:
        if (r7 >= r0) goto L_0x003d;
    L_0x002b:
        r8 = r3[r7];	 Catch:{ all -> 0x004d }
        r12 = r1.mBuilderPtr;	 Catch:{ all -> 0x004d }
        r9 = r8.getOpenTypeTagValue();	 Catch:{ all -> 0x004d }
        r15 = r8.getStyleValue();	 Catch:{ all -> 0x004d }
        nAddAxisValue(r12, r9, r15);	 Catch:{ all -> 0x004d }
        r7 = r7 + 1;
        goto L_0x0029;
    L_0x003d:
        r12 = r1.mBuilderPtr;	 Catch:{ all -> 0x004d }
        r15 = r20;
        r16 = r22;
        r17 = r23;
        r0 = nAddFont(r12, r14, r15, r16, r17);	 Catch:{ all -> 0x004d }
        r5.close();	 Catch:{ IOException -> 0x005c }
        return r0;
    L_0x004d:
        r0 = move-exception;
        r6 = r0;
        throw r6;	 Catch:{ all -> 0x0050 }
    L_0x0050:
        r0 = move-exception;
        r7 = r0;
        r5.close();	 Catch:{ all -> 0x0056 }
        goto L_0x005b;
    L_0x0056:
        r0 = move-exception;
        r8 = r0;
        r6.addSuppressed(r8);	 Catch:{ IOException -> 0x005c }
    L_0x005b:
        throw r7;	 Catch:{ IOException -> 0x005c }
    L_0x005c:
        r0 = move-exception;
        r5 = TAG;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Error mapping font file ";
        r6.append(r7);
        r6.append(r2);
        r6 = r6.toString();
        android.util.Log.e(r5, r6);
        return r4;
    L_0x0074:
        r0 = new java.lang.IllegalStateException;
        r4 = "Unable to call addFont after freezing.";
        r0.<init>(r4);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.FontFamily.addFont(java.lang.String, int, android.graphics.fonts.FontVariationAxis[], int, int):boolean");
    }

    @UnsupportedAppUsage(trackingBug = 123768928)
    public boolean addFontFromBuffer(ByteBuffer font, int ttcIndex, FontVariationAxis[] axes, int weight, int italic) {
        if (this.mBuilderPtr != 0) {
            if (axes != null) {
                for (FontVariationAxis axis : axes) {
                    nAddAxisValue(this.mBuilderPtr, axis.getOpenTypeTagValue(), axis.getStyleValue());
                }
            }
            return nAddFontWeightStyle(this.mBuilderPtr, font, ttcIndex, weight, italic);
        }
        throw new IllegalStateException("Unable to call addFontWeightStyle after freezing.");
    }

    @UnsupportedAppUsage(trackingBug = 123768928)
    public boolean addFontFromAssetManager(AssetManager mgr, String path, int cookie, boolean isAsset, int ttcIndex, int weight, int isItalic, FontVariationAxis[] axes) {
        FontVariationAxis[] fontVariationAxisArr = axes;
        if (this.mBuilderPtr != 0) {
            if (fontVariationAxisArr != null) {
                for (FontVariationAxis axis : fontVariationAxisArr) {
                    nAddAxisValue(this.mBuilderPtr, axis.getOpenTypeTagValue(), axis.getStyleValue());
                }
            }
            return nAddFontFromAssetManager(this.mBuilderPtr, mgr, path, cookie, isAsset, ttcIndex, weight, isItalic);
        }
        throw new IllegalStateException("Unable to call addFontFromAsset after freezing.");
    }

    private static boolean nAddFont(long builderPtr, ByteBuffer font, int ttcIndex) {
        return nAddFont(builderPtr, font, ttcIndex, -1, -1);
    }
}
