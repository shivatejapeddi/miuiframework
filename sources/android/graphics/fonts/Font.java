package android.graphics.fonts;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.LocaleList;
import android.os.ParcelFileDescriptor;
import android.util.TypedValue;
import com.android.internal.util.Preconditions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Arrays;
import java.util.Objects;
import libcore.util.NativeAllocationRegistry;

public final class Font {
    private static final int NOT_SPECIFIED = -1;
    private static final int STYLE_ITALIC = 1;
    private static final int STYLE_NORMAL = 0;
    private static final String TAG = "Font";
    private final FontVariationAxis[] mAxes;
    private final ByteBuffer mBuffer;
    private final File mFile;
    private final FontStyle mFontStyle;
    private final String mLocaleList;
    private final long mNativePtr;
    private final int mTtcIndex;

    public static final class Builder {
        private static final NativeAllocationRegistry sAssetByteBufferRegistry = NativeAllocationRegistry.createMalloced(ByteBuffer.class.getClassLoader(), nGetReleaseNativeAssetFunc());
        private static final NativeAllocationRegistry sFontRegistry = NativeAllocationRegistry.createMalloced(Font.class.getClassLoader(), nGetReleaseNativeFont());
        private FontVariationAxis[] mAxes;
        private ByteBuffer mBuffer;
        private IOException mException;
        private File mFile;
        private int mItalic;
        private String mLocaleList;
        private int mTtcIndex;
        private int mWeight;

        private static native void nAddAxis(long j, int i, float f);

        private static native long nBuild(long j, ByteBuffer byteBuffer, String str, int i, boolean z, int i2);

        private static native ByteBuffer nGetAssetBuffer(long j);

        private static native long nGetNativeAsset(AssetManager assetManager, String str, boolean z, int i);

        private static native long nGetReleaseNativeAssetFunc();

        private static native long nGetReleaseNativeFont();

        private static native long nInitBuilder();

        public Builder(ByteBuffer buffer) {
            this.mLocaleList = "";
            this.mWeight = -1;
            this.mItalic = -1;
            this.mTtcIndex = 0;
            this.mAxes = null;
            Preconditions.checkNotNull(buffer, "buffer can not be null");
            if (buffer.isDirect()) {
                this.mBuffer = buffer;
                return;
            }
            throw new IllegalArgumentException("Only direct buffer can be used as the source of font data.");
        }

        public Builder(ByteBuffer buffer, File path, String localeList) {
            this(buffer);
            this.mFile = path;
            this.mLocaleList = localeList;
        }

        /* JADX WARNING: Missing block: B:12:?, code skipped:
            $closeResource(r0, r1);
     */
        public Builder(java.io.File r9) {
            /*
            r8 = this;
            r8.<init>();
            r0 = "";
            r8.mLocaleList = r0;
            r0 = -1;
            r8.mWeight = r0;
            r8.mItalic = r0;
            r0 = 0;
            r8.mTtcIndex = r0;
            r0 = 0;
            r8.mAxes = r0;
            r1 = "path can not be null";
            com.android.internal.util.Preconditions.checkNotNull(r9, r1);
            r1 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x003a }
            r1.<init>(r9);	 Catch:{ IOException -> 0x003a }
            r2 = r1.getChannel();	 Catch:{ all -> 0x0033 }
            r3 = java.nio.channels.FileChannel.MapMode.READ_ONLY;	 Catch:{ all -> 0x0033 }
            r4 = 0;
            r6 = r2.size();	 Catch:{ all -> 0x0033 }
            r3 = r2.map(r3, r4, r6);	 Catch:{ all -> 0x0033 }
            r8.mBuffer = r3;	 Catch:{ all -> 0x0033 }
            $closeResource(r0, r1);	 Catch:{ IOException -> 0x003a }
            goto L_0x003d;
        L_0x0033:
            r0 = move-exception;
            throw r0;	 Catch:{ all -> 0x0035 }
        L_0x0035:
            r2 = move-exception;
            $closeResource(r0, r1);	 Catch:{ IOException -> 0x003a }
            throw r2;	 Catch:{ IOException -> 0x003a }
        L_0x003a:
            r0 = move-exception;
            r8.mException = r0;
        L_0x003d:
            r8.mFile = r9;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.graphics.fonts.Font$Builder.<init>(java.io.File):void");
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

        public Builder(ParcelFileDescriptor fd) {
            this(fd, 0, -1);
        }

        public Builder(ParcelFileDescriptor fd, long offset, long size) {
            IOException e;
            Throwable th;
            this.mLocaleList = "";
            this.mWeight = -1;
            this.mItalic = -1;
            this.mTtcIndex = 0;
            this.mAxes = null;
            FileInputStream fis;
            try {
                fis = new FileInputStream(fd.getFileDescriptor());
                try {
                    FileChannel fc = fis.getChannel();
                    long size2 = size == -1 ? fc.size() - offset : size;
                    try {
                        this.mBuffer = fc.map(MapMode.READ_ONLY, offset, size2);
                        try {
                            $closeResource(null, fis);
                            size = size2;
                        } catch (IOException e2) {
                            e = e2;
                            size = size2;
                            this.mException = e;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        size = size2;
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    throw th;
                }
            } catch (IOException e3) {
                e = e3;
                this.mException = e;
            } catch (Throwable th4) {
                $closeResource(th, fis);
            }
        }

        public Builder(AssetManager am, String path) {
            this(am, path, true, 0);
        }

        public Builder(AssetManager am, String path, boolean isAsset, int cookie) {
            this.mLocaleList = "";
            this.mWeight = -1;
            this.mItalic = -1;
            this.mTtcIndex = 0;
            this.mAxes = null;
            long nativeAsset = nGetNativeAsset(am, path, isAsset, cookie);
            if (nativeAsset == 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to open ");
                stringBuilder.append(path);
                this.mException = new FileNotFoundException(stringBuilder.toString());
                return;
            }
            ByteBuffer b = nGetAssetBuffer(nativeAsset);
            sAssetByteBufferRegistry.registerNativeAllocation(b, nativeAsset);
            if (b == null) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(path);
                stringBuilder2.append(" not found");
                this.mException = new FileNotFoundException(stringBuilder2.toString());
                return;
            }
            this.mBuffer = b;
        }

        public Builder(Resources res, int resId) {
            this.mLocaleList = "";
            this.mWeight = -1;
            this.mItalic = -1;
            this.mTtcIndex = 0;
            this.mAxes = null;
            TypedValue value = new TypedValue();
            res.getValue(resId, value, true);
            String str = " not found";
            if (value.string == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(resId);
                stringBuilder.append(str);
                this.mException = new FileNotFoundException(stringBuilder.toString());
                return;
            }
            String str2 = value.string.toString();
            StringBuilder stringBuilder2;
            if (str2.toLowerCase().endsWith(".xml")) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(resId);
                stringBuilder2.append(" must be font file.");
                this.mException = new FileNotFoundException(stringBuilder2.toString());
                return;
            }
            long nativeAsset = nGetNativeAsset(res.getAssets(), str2, false, value.assetCookie);
            if (nativeAsset == 0) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Unable to open ");
                stringBuilder2.append(str2);
                this.mException = new FileNotFoundException(stringBuilder2.toString());
                return;
            }
            ByteBuffer b = nGetAssetBuffer(nativeAsset);
            sAssetByteBufferRegistry.registerNativeAllocation(b, nativeAsset);
            if (b == null) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str2);
                stringBuilder3.append(str);
                this.mException = new FileNotFoundException(stringBuilder3.toString());
                return;
            }
            this.mBuffer = b;
        }

        public Builder setWeight(int weight) {
            boolean z = true;
            if (1 > weight || weight > 1000) {
                z = false;
            }
            Preconditions.checkArgument(z);
            this.mWeight = weight;
            return this;
        }

        public Builder setSlant(int slant) {
            this.mItalic = slant == 0 ? 0 : 1;
            return this;
        }

        public Builder setTtcIndex(int ttcIndex) {
            this.mTtcIndex = ttcIndex;
            return this;
        }

        public Builder setFontVariationSettings(String variationSettings) {
            this.mAxes = FontVariationAxis.fromFontVariationSettings(variationSettings);
            return this;
        }

        public Builder setFontVariationSettings(FontVariationAxis[] axes) {
            this.mAxes = axes == null ? null : (FontVariationAxis[]) axes.clone();
            return this;
        }

        public Font build() throws IOException {
            IOException iOException = this.mException;
            if (iOException == null) {
                int packed;
                int i = 0;
                if (this.mWeight == -1 || this.mItalic == -1) {
                    packed = FontFileUtil.analyzeStyle(this.mBuffer, this.mTtcIndex, this.mAxes);
                    if (FontFileUtil.isSuccess(packed)) {
                        if (this.mWeight == -1) {
                            this.mWeight = FontFileUtil.unpackWeight(packed);
                        }
                        if (this.mItalic == -1) {
                            this.mItalic = FontFileUtil.unpackItalic(packed);
                        }
                    } else {
                        this.mWeight = 400;
                        this.mItalic = 0;
                    }
                }
                int i2 = 1;
                this.mWeight = Math.max(1, Math.min(1000, this.mWeight));
                boolean italic = this.mItalic == 1;
                if (this.mItalic != 1) {
                    i2 = 0;
                }
                packed = i2;
                long builderPtr = nInitBuilder();
                FontVariationAxis[] fontVariationAxisArr = this.mAxes;
                if (fontVariationAxisArr != null) {
                    int length = fontVariationAxisArr.length;
                    while (i < length) {
                        FontVariationAxis axis = fontVariationAxisArr[i];
                        nAddAxis(builderPtr, axis.getOpenTypeTagValue(), axis.getStyleValue());
                        i++;
                    }
                }
                ByteBuffer readonlyBuffer = this.mBuffer.asReadOnlyBuffer();
                File file = this.mFile;
                long ptr = nBuild(builderPtr, readonlyBuffer, file == null ? "" : file.getAbsolutePath(), this.mWeight, italic, this.mTtcIndex);
                Font font = new Font(ptr, readonlyBuffer, this.mFile, new FontStyle(this.mWeight, packed), this.mTtcIndex, this.mAxes, this.mLocaleList);
                sFontRegistry.registerNativeAllocation(font, ptr);
                return font;
            }
            throw new IOException("Failed to read font contents", iOException);
        }
    }

    private Font(long nativePtr, ByteBuffer buffer, File file, FontStyle fontStyle, int ttcIndex, FontVariationAxis[] axes, String localeList) {
        this.mBuffer = buffer;
        this.mFile = file;
        this.mFontStyle = fontStyle;
        this.mNativePtr = nativePtr;
        this.mTtcIndex = ttcIndex;
        this.mAxes = axes;
        this.mLocaleList = localeList;
    }

    public ByteBuffer getBuffer() {
        return this.mBuffer;
    }

    public File getFile() {
        return this.mFile;
    }

    public FontStyle getStyle() {
        return this.mFontStyle;
    }

    public int getTtcIndex() {
        return this.mTtcIndex;
    }

    public FontVariationAxis[] getAxes() {
        FontVariationAxis[] fontVariationAxisArr = this.mAxes;
        return fontVariationAxisArr == null ? null : (FontVariationAxis[]) fontVariationAxisArr.clone();
    }

    public LocaleList getLocaleList() {
        return LocaleList.forLanguageTags(this.mLocaleList);
    }

    public long getNativePtr() {
        return this.mNativePtr;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof Font)) {
            return false;
        }
        Font f = (Font) o;
        if (!(this.mFontStyle.equals(f.mFontStyle) && f.mTtcIndex == this.mTtcIndex && Arrays.equals(f.mAxes, this.mAxes) && f.mBuffer.equals(this.mBuffer))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mFontStyle, Integer.valueOf(this.mTtcIndex), Integer.valueOf(Arrays.hashCode(this.mAxes)), this.mBuffer});
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Font {path=");
        stringBuilder.append(this.mFile);
        stringBuilder.append(", style=");
        stringBuilder.append(this.mFontStyle);
        stringBuilder.append(", ttcIndex=");
        stringBuilder.append(this.mTtcIndex);
        stringBuilder.append(", axes=");
        stringBuilder.append(FontVariationAxis.toFontVariationSettings(this.mAxes));
        stringBuilder.append(", localeList=");
        stringBuilder.append(this.mLocaleList);
        stringBuilder.append(", buffer=");
        stringBuilder.append(this.mBuffer);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
