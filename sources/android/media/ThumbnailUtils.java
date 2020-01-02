package android.media;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.ImageInfo;
import android.graphics.ImageDecoder.OnHeaderDecodedListener;
import android.graphics.ImageDecoder.Source;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever.BitmapParams;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore.ThumbnailConstants;
import android.util.Log;
import android.util.Size;
import java.io.File;
import java.io.IOException;
import libcore.io.IoUtils;

public class ThumbnailUtils {
    private static final int OPTIONS_NONE = 0;
    public static final int OPTIONS_RECYCLE_INPUT = 2;
    private static final int OPTIONS_SCALE_UP = 1;
    private static final String TAG = "ThumbnailUtils";
    @Deprecated
    @UnsupportedAppUsage
    public static final int TARGET_SIZE_MICRO_THUMBNAIL = 96;

    private static class Resizer implements OnHeaderDecodedListener {
        private final CancellationSignal signal;
        private final Size size;

        public Resizer(Size size, CancellationSignal signal) {
            this.size = size;
            this.signal = signal;
        }

        public void onHeaderDecoded(ImageDecoder decoder, ImageInfo info, Source source) {
            CancellationSignal cancellationSignal = this.signal;
            if (cancellationSignal != null) {
                cancellationSignal.throwIfCanceled();
            }
            decoder.setAllocator(1);
            int sample = Math.max(info.getSize().getWidth() / this.size.getWidth(), info.getSize().getHeight() / this.size.getHeight());
            if (sample > 1) {
                decoder.setTargetSampleSize(sample);
            }
        }
    }

    @Deprecated
    private static class SizedThumbnailBitmap {
        public Bitmap mBitmap;
        public byte[] mThumbnailData;
        public int mThumbnailHeight;
        public int mThumbnailWidth;

        private SizedThumbnailBitmap() {
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: SSATransform
        jadx.core.utils.exceptions.JadxRuntimeException: Not initialized variable reg: 12, insn: 0x0057: IF  (r4_7 ?[int, OBJECT, ARRAY, boolean, byte, short, char]) <= (r12 ?[int, OBJECT, ARRAY, boolean, byte, short, char])  -> B:24:0x0070, block:B:17:0x0057, method: android.media.ThumbnailUtils.createVideoThumbnail(java.io.File, android.util.Size, android.os.CancellationSignal):android.graphics.Bitmap
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:162)
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:184)
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:184)
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:184)
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:184)
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:184)
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:184)
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:184)
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:133)
        	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
        	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.lang.Iterable.forEach(Unknown Source)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        */
    public static android.graphics.Bitmap createVideoThumbnail(java.io.File r16, android.util.Size r17, android.os.CancellationSignal r18) throws java.io.IOException {
        /*
        r1 = r18;
        if (r1 == 0) goto L_0x0007;
        r18.throwIfCanceled();
        r0 = new android.media.ThumbnailUtils$Resizer;
        r2 = r17;
        r0.<init>(r2, r1);
        r3 = r0;
        r0 = new android.media.MediaMetadataRetriever;	 Catch:{ RuntimeException -> 0x0093 }
        r0.<init>();	 Catch:{ RuntimeException -> 0x0093 }
        r10 = r0;
        r0 = 0;
        r4 = r16.getAbsolutePath();	 Catch:{ all -> 0x008a, all -> 0x008d }
        r10.setDataSource(r4);	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = r10.getEmbeddedPicture();	 Catch:{ all -> 0x008a, all -> 0x008d }
        r11 = r4;	 Catch:{ all -> 0x008a, all -> 0x008d }
        if (r11 == 0) goto L_0x0030;	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = android.graphics.ImageDecoder.createSource(r11);	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = android.graphics.ImageDecoder.decodeBitmap(r4, r3);	 Catch:{ all -> 0x008a, all -> 0x008d }
        $closeResource(r0, r10);	 Catch:{ RuntimeException -> 0x0093 }
        return r4;
        r4 = 18;
        r4 = r10.extractMetadata(r4);	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = java.lang.Integer.parseInt(r4);	 Catch:{ all -> 0x008a, all -> 0x008d }
        r12 = r4;	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = 19;	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = r10.extractMetadata(r4);	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = java.lang.Integer.parseInt(r4);	 Catch:{ all -> 0x008a, all -> 0x008d }
        r13 = r4;	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = 9;	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = r10.extractMetadata(r4);	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = java.lang.Long.parseLong(r4);	 Catch:{ all -> 0x008a, all -> 0x008d }
        r14 = r4;	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = r17.getWidth();	 Catch:{ all -> 0x008a, all -> 0x008d }
        r5 = 2;	 Catch:{ all -> 0x008a, all -> 0x008d }
        if (r4 <= r12) goto L_0x0070;	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = r17.getHeight();	 Catch:{ all -> 0x008a, all -> 0x008d }
        if (r4 <= r13) goto L_0x0070;	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = r14 / r5;	 Catch:{ all -> 0x008a, all -> 0x008d }
        r6 = 2;	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = r10.getFrameAtTime(r4, r6);	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = java.util.Objects.requireNonNull(r4);	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = (android.graphics.Bitmap) r4;	 Catch:{ all -> 0x008a, all -> 0x008d }
        $closeResource(r0, r10);	 Catch:{ RuntimeException -> 0x0093 }
        return r4;
        r5 = r14 / r5;	 Catch:{ all -> 0x008a, all -> 0x008d }
        r7 = 2;	 Catch:{ all -> 0x008a, all -> 0x008d }
        r8 = r17.getWidth();	 Catch:{ all -> 0x008a, all -> 0x008d }
        r9 = r17.getHeight();	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = r10;	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = r4.getScaledFrameAtTime(r5, r7, r8, r9);	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = java.util.Objects.requireNonNull(r4);	 Catch:{ all -> 0x008a, all -> 0x008d }
        r4 = (android.graphics.Bitmap) r4;	 Catch:{ all -> 0x008a, all -> 0x008d }
        $closeResource(r0, r10);	 Catch:{ RuntimeException -> 0x0093 }
        return r4;
        r0 = move-exception;
        r4 = r0;
        throw r4;	 Catch:{ all -> 0x008a, all -> 0x008d }
        r0 = move-exception;
        r5 = r0;
        $closeResource(r4, r10);	 Catch:{ RuntimeException -> 0x0093 }
        throw r5;	 Catch:{ RuntimeException -> 0x0093 }
        r0 = move-exception;
        r4 = new java.io.IOException;
        r5 = "Failed to create thumbnail";
        r4.<init>(r5, r0);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.ThumbnailUtils.createVideoThumbnail(java.io.File, android.util.Size, android.os.CancellationSignal):android.graphics.Bitmap");
    }

    private static Size convertKind(int kind) {
        if (kind == 3) {
            return Point.convert(ThumbnailConstants.MICRO_SIZE);
        }
        if (kind == 2) {
            return Point.convert(ThumbnailConstants.FULL_SCREEN_SIZE);
        }
        if (kind == 1) {
            return Point.convert(ThumbnailConstants.MINI_SIZE);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported kind: ");
        stringBuilder.append(kind);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @Deprecated
    public static Bitmap createAudioThumbnail(String filePath, int kind) {
        Bitmap bitmap = null;
        try {
            bitmap = createAudioThumbnail(new File(filePath), convertKind(kind), null);
            return bitmap;
        } catch (IOException e) {
            Log.w(TAG, e);
            return bitmap;
        }
    }

    /* JADX WARNING: Missing block: B:44:?, code skipped:
            $closeResource(r2, r1);
     */
    public static android.graphics.Bitmap createAudioThumbnail(java.io.File r9, android.util.Size r10, android.os.CancellationSignal r11) throws java.io.IOException {
        /*
        if (r11 == 0) goto L_0x0005;
    L_0x0002:
        r11.throwIfCanceled();
    L_0x0005:
        r0 = new android.media.ThumbnailUtils$Resizer;
        r0.<init>(r10, r11);
        r1 = new android.media.MediaMetadataRetriever;	 Catch:{ RuntimeException -> 0x00c1 }
        r1.<init>();	 Catch:{ RuntimeException -> 0x00c1 }
        r2 = r9.getAbsolutePath();	 Catch:{ all -> 0x00ba }
        r1.setDataSource(r2);	 Catch:{ all -> 0x00ba }
        r2 = r1.getEmbeddedPicture();	 Catch:{ all -> 0x00ba }
        r3 = 0;
        if (r2 == 0) goto L_0x0029;
    L_0x001d:
        r4 = android.graphics.ImageDecoder.createSource(r2);	 Catch:{ all -> 0x00ba }
        r4 = android.graphics.ImageDecoder.decodeBitmap(r4, r0);	 Catch:{ all -> 0x00ba }
        $closeResource(r3, r1);	 Catch:{ RuntimeException -> 0x00c1 }
        return r4;
    L_0x0029:
        $closeResource(r3, r1);	 Catch:{ RuntimeException -> 0x00c1 }
        r1 = android.os.Environment.getExternalStorageState(r9);
        r2 = "unknown";
        r1 = r2.equals(r1);
        if (r1 != 0) goto L_0x00b2;
    L_0x003a:
        r1 = r9.getParentFile();
        if (r1 == 0) goto L_0x0045;
    L_0x0040:
        r4 = r1.getParentFile();
        goto L_0x0046;
    L_0x0045:
        r4 = r3;
    L_0x0046:
        if (r1 == 0) goto L_0x005d;
    L_0x0048:
        r5 = r1.getName();
        r6 = android.os.Environment.DIRECTORY_DOWNLOADS;
        r5 = r5.equals(r6);
        if (r5 != 0) goto L_0x0055;
    L_0x0054:
        goto L_0x005d;
    L_0x0055:
        r2 = new java.io.IOException;
        r3 = "No thumbnails in Downloads directories";
        r2.<init>(r3);
        throw r2;
    L_0x005d:
        if (r4 == 0) goto L_0x0072;
    L_0x005f:
        r5 = android.os.Environment.getExternalStorageState(r4);
        r2 = r2.equals(r5);
        if (r2 != 0) goto L_0x006a;
    L_0x0069:
        goto L_0x0072;
    L_0x006a:
        r2 = new java.io.IOException;
        r3 = "No thumbnails in top-level directories";
        r2.<init>(r3);
        throw r2;
        r2 = r9.getParentFile();
        r5 = android.media.-$$Lambda$ThumbnailUtils$P13h9YbyD69p6ss1gYpoef43_MU.INSTANCE;
        r2 = r2.listFiles(r5);
        r2 = com.android.internal.util.ArrayUtils.defeatNullable(r2);
        r5 = android.media.-$$Lambda$ThumbnailUtils$qOH5vebuTwPi2G92PTa6rgwKGoc.INSTANCE;
        r6 = new android.media.-$$Lambda$ThumbnailUtils$HhGKNQZck57eO__Paj6KyQm6lCk;
        r6.<init>(r5);
        r7 = java.util.Arrays.asList(r2);
        r7 = r7.stream();
        r7 = r7.max(r6);
        r3 = r7.orElse(r3);
        r3 = (java.io.File) r3;
        if (r3 == 0) goto L_0x00aa;
    L_0x009c:
        if (r11 == 0) goto L_0x00a1;
    L_0x009e:
        r11.throwIfCanceled();
    L_0x00a1:
        r7 = android.graphics.ImageDecoder.createSource(r3);
        r7 = android.graphics.ImageDecoder.decodeBitmap(r7, r0);
        return r7;
    L_0x00aa:
        r7 = new java.io.IOException;
        r8 = "No album art found";
        r7.<init>(r8);
        throw r7;
    L_0x00b2:
        r1 = new java.io.IOException;
        r2 = "No embedded album art found";
        r1.<init>(r2);
        throw r1;
    L_0x00ba:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x00bc }
    L_0x00bc:
        r3 = move-exception;
        $closeResource(r2, r1);	 Catch:{ RuntimeException -> 0x00c1 }
        throw r3;	 Catch:{ RuntimeException -> 0x00c1 }
    L_0x00c1:
        r1 = move-exception;
        r2 = new java.io.IOException;
        r3 = "Failed to create thumbnail";
        r2.<init>(r3, r1);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.ThumbnailUtils.createAudioThumbnail(java.io.File, android.util.Size, android.os.CancellationSignal):android.graphics.Bitmap");
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

    static /* synthetic */ boolean lambda$createAudioThumbnail$0(File dir, String name) {
        String lower = name.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".png");
    }

    static /* synthetic */ int lambda$createAudioThumbnail$1(File f) {
        String lower = f.getName().toLowerCase();
        if (lower.equals("albumart.jpg")) {
            return 4;
        }
        String str = "albumart";
        String str2 = ".jpg";
        if (lower.startsWith(str) && lower.endsWith(str2)) {
            return 3;
        }
        if (lower.contains(str) && lower.endsWith(str2)) {
            return 2;
        }
        if (lower.endsWith(str2)) {
            return 1;
        }
        return 0;
    }

    @Deprecated
    public static Bitmap createImageThumbnail(String filePath, int kind) {
        Bitmap bitmap = null;
        try {
            bitmap = createImageThumbnail(new File(filePath), convertKind(kind), null);
            return bitmap;
        } catch (IOException e) {
            Log.w(TAG, e);
            return bitmap;
        }
    }

    public static Bitmap createImageThumbnail(File file, Size size, CancellationSignal signal) throws IOException {
        ExifInterface exif;
        MediaMetadataRetriever retriever;
        Throwable th;
        Throwable thumbnailImageAtIndex;
        CancellationSignal cancellationSignal = signal;
        if (cancellationSignal != null) {
            signal.throwIfCanceled();
        }
        Resizer resizer = new Resizer(size, cancellationSignal);
        String mimeType = MediaFile.getMimeTypeForFile(file.getName());
        Bitmap bitmap = null;
        int orientation = 0;
        if (MediaFile.isExifMimeType(mimeType)) {
            ExifInterface exif2 = new ExifInterface(file);
            int attributeInt = exif2.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            if (attributeInt == 3) {
                orientation = 180;
                exif = exif2;
            } else if (attributeInt == 6) {
                orientation = 90;
                exif = exif2;
            } else if (attributeInt != 8) {
                exif = exif2;
            } else {
                orientation = 270;
                exif = exif2;
            }
        } else {
            File file2 = file;
            exif = null;
        }
        if (mimeType.equals("image/heif") || mimeType.equals("image/heif-sequence") || mimeType.equals("image/heic") || mimeType.equals("image/heic-sequence")) {
            try {
                retriever = new MediaMetadataRetriever();
                th = null;
                try {
                    retriever.setDataSource(file.getAbsolutePath());
                    thumbnailImageAtIndex = retriever.getThumbnailImageAtIndex(-1, new BitmapParams(), size.getWidth(), size.getWidth() * size.getHeight());
                } catch (Throwable th2) {
                    th = th2;
                    $closeResource(thumbnailImageAtIndex, retriever);
                } finally {
                    while (true) {
                        th = 
/*
Method generation error in method: android.media.ThumbnailUtils.createImageThumbnail(java.io.File, android.util.Size, android.os.CancellationSignal):android.graphics.Bitmap, dex: classes2.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: ?: MERGE  (r0_17 'th' java.lang.Throwable) = (r0_16 'th' java.lang.Throwable), (r10_4 'thumbnailImageAtIndex' java.lang.Throwable) in method: android.media.ThumbnailUtils.createImageThumbnail(java.io.File, android.util.Size, android.os.CancellationSignal):android.graphics.Bitmap, dex: classes2.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:228)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:205)
	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:102)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:52)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:95)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:175)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:95)
	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:300)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:95)
	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:280)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:95)
	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:120)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:183)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:321)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:259)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:221)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:111)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:77)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:10)
	at jadx.core.ProcessClass.process(ProcessClass.java:38)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
Caused by: jadx.core.utils.exceptions.CodegenException: MERGE can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:539)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:511)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:222)
	... 35 more

*/

    @Deprecated
    public static Bitmap createVideoThumbnail(String filePath, int kind) {
        Bitmap bitmap = null;
        try {
            bitmap = createVideoThumbnail(new File(filePath), convertKind(kind), null);
            return bitmap;
        } catch (IOException e) {
            Log.w(TAG, e);
            return bitmap;
        }
    }

    public static Bitmap extractThumbnail(Bitmap source, int width, int height) {
        return extractThumbnail(source, width, height, 0);
    }

    public static Bitmap extractThumbnail(Bitmap source, int width, int height, int options) {
        if (source == null) {
            return null;
        }
        float scale;
        if (source.getWidth() < source.getHeight()) {
            scale = ((float) width) / ((float) source.getWidth());
        } else {
            scale = ((float) height) / ((float) source.getHeight());
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        return transform(matrix, source, width, height, options | 1);
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private static int computeSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
        return 1;
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private static int computeInitialSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
        return 1;
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private static void closeSilently(ParcelFileDescriptor c) {
        IoUtils.closeQuietly(c);
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private static ParcelFileDescriptor makeInputStream(Uri uri, ContentResolver cr) {
        try {
            return cr.openFileDescriptor(uri, "r");
        } catch (IOException e) {
            return null;
        }
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int targetHeight, int options) {
        Matrix scaler2 = scaler;
        Bitmap bitmap = source;
        int i = targetWidth;
        int i2 = targetHeight;
        boolean z = true;
        boolean scaleUp = (options & 1) != 0;
        if ((options & 2) == 0) {
            z = false;
        }
        boolean recycle = z;
        int deltaX = source.getWidth() - i;
        int deltaY = source.getHeight() - i2;
        if (scaleUp || (deltaX >= 0 && deltaY >= 0)) {
            Matrix scaler3;
            Bitmap b1;
            float bitmapWidthF = (float) source.getWidth();
            float bitmapHeightF = (float) source.getHeight();
            float scale;
            if (bitmapWidthF / bitmapHeightF > ((float) i) / ((float) i2)) {
                scale = ((float) i2) / bitmapHeightF;
                if (scale < 0.9f || scale > 1.0f) {
                    scaler2.setScale(scale, scale);
                } else {
                    scaler2 = null;
                }
                scaler3 = scaler2;
            } else {
                scale = ((float) i) / bitmapWidthF;
                if (scale < 0.9f || scale > 1.0f) {
                    scaler2.setScale(scale, scale);
                    scaler3 = scaler2;
                } else {
                    scaler3 = null;
                }
            }
            if (scaler3 != null) {
                b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), scaler3, true);
            } else {
                b1 = source;
            }
            if (recycle && b1 != bitmap) {
                source.recycle();
            }
            Bitmap b2 = Bitmap.createBitmap(b1, Math.max(0, b1.getWidth() - i) / 2, Math.max(0, b1.getHeight() - i2) / 2, i, i2);
            if (b2 != b1 && (recycle || b1 != bitmap)) {
                b1.recycle();
            }
            return b2;
        }
        Bitmap b22 = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
        Canvas c = new Canvas(b22);
        int deltaXHalf = Math.max(0, deltaX / 2);
        int deltaYHalf = Math.max(0, deltaY / 2);
        Rect src = new Rect(deltaXHalf, deltaYHalf, Math.min(i, source.getWidth()) + deltaXHalf, Math.min(i2, source.getHeight()) + deltaYHalf);
        int dstX = (i - src.width()) / 2;
        int dstY = (i2 - src.height()) / 2;
        c.drawBitmap(bitmap, src, new Rect(dstX, dstY, i - dstX, i2 - dstY), null);
        if (recycle) {
            source.recycle();
        }
        c.setBitmap(null);
        return b22;
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private static void createThumbnailFromEXIF(String filePath, int targetSize, int maxPixels, SizedThumbnailBitmap sizedThumbBitmap) {
    }
}
