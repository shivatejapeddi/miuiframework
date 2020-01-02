package android.graphics;

import android.annotation.UnsupportedAppUsage;
import android.content.res.AssetManager.AssetInputStream;
import android.content.res.Resources;
import android.graphics.Bitmap.Config;
import android.graphics.ColorSpace.Rgb;
import android.os.Trace;
import android.util.Log;
import android.util.TypedValue;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapFactory {
    private static final int DECODE_BUFFER_SIZE = 16384;

    public static class Options {
        public Bitmap inBitmap;
        public int inDensity;
        public boolean inDither;
        @Deprecated
        public boolean inInputShareable;
        public boolean inJustDecodeBounds;
        public boolean inMutable;
        @Deprecated
        public boolean inPreferQualityOverSpeed;
        public ColorSpace inPreferredColorSpace = null;
        public Config inPreferredConfig = Config.ARGB_8888;
        public boolean inPremultiplied = true;
        @Deprecated
        public boolean inPurgeable;
        public int inSampleSize;
        public boolean inScaled = true;
        public int inScreenDensity;
        public int inTargetDensity;
        public byte[] inTempStorage;
        @Deprecated
        public boolean mCancel;
        public ColorSpace outColorSpace;
        public Config outConfig;
        public int outHeight;
        public String outMimeType;
        public int outWidth;

        @Deprecated
        public void requestCancelDecode() {
            this.mCancel = true;
        }

        static void validate(Options opts) {
            if (opts != null) {
                Bitmap bitmap = opts.inBitmap;
                if (bitmap != null) {
                    if (bitmap.getConfig() == Config.HARDWARE) {
                        throw new IllegalArgumentException("Bitmaps with Config.HARDWARE are always immutable");
                    } else if (opts.inBitmap.isRecycled()) {
                        throw new IllegalArgumentException("Cannot reuse a recycled Bitmap");
                    }
                }
                if (opts.inMutable && opts.inPreferredConfig == Config.HARDWARE) {
                    throw new IllegalArgumentException("Bitmaps with Config.HARDWARE cannot be decoded into - they are immutable");
                }
                ColorSpace colorSpace = opts.inPreferredColorSpace;
                if (colorSpace != null) {
                    if (!(colorSpace instanceof Rgb)) {
                        throw new IllegalArgumentException("The destination color space must use the RGB color model");
                    } else if (((Rgb) colorSpace).getTransferParameters() == null) {
                        throw new IllegalArgumentException("The destination color space must use an ICC parametric transfer function");
                    }
                }
            }
        }

        static long nativeInBitmap(Options opts) {
            if (opts != null) {
                Bitmap bitmap = opts.inBitmap;
                if (bitmap != null) {
                    return bitmap.getNativeInstance();
                }
            }
            return 0;
        }

        static long nativeColorSpace(Options opts) {
            if (opts != null) {
                ColorSpace colorSpace = opts.inPreferredColorSpace;
                if (colorSpace != null) {
                    return colorSpace.getNativeInstance();
                }
            }
            return 0;
        }
    }

    @UnsupportedAppUsage
    private static native Bitmap nativeDecodeAsset(long j, Rect rect, Options options, long j2, long j3);

    @UnsupportedAppUsage
    private static native Bitmap nativeDecodeByteArray(byte[] bArr, int i, int i2, Options options, long j, long j2);

    @UnsupportedAppUsage
    private static native Bitmap nativeDecodeFileDescriptor(FileDescriptor fileDescriptor, Rect rect, Options options, long j, long j2);

    @UnsupportedAppUsage
    private static native Bitmap nativeDecodeStream(InputStream inputStream, byte[] bArr, Rect rect, Options options, long j, long j2);

    private static native boolean nativeIsSeekable(FileDescriptor fileDescriptor);

    public static Bitmap decodeFile(String pathName, Options opts) {
        Options.validate(opts);
        Bitmap bm = null;
        InputStream stream = null;
        try {
            stream = new FileInputStream(pathName);
            bm = decodeStream(stream, null, opts);
            try {
                stream.close();
            } catch (IOException e) {
            }
        } catch (Exception e2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to decode stream: ");
            stringBuilder.append(e2);
            Log.e("BitmapFactory", stringBuilder.toString());
            if (stream != null) {
                stream.close();
            }
        } catch (Throwable th) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e3) {
                }
            }
        }
        return bm;
    }

    public static Bitmap decodeFile(String pathName) {
        return decodeFile(pathName, null);
    }

    public static Bitmap decodeResourceStream(Resources res, TypedValue value, InputStream is, Rect pad, Options opts) {
        Options.validate(opts);
        if (opts == null) {
            opts = new Options();
        }
        if (opts.inDensity == 0 && value != null) {
            int density = value.density;
            if (density == 0) {
                opts.inDensity = 160;
            } else if (density != 65535) {
                opts.inDensity = density;
            }
        }
        if (opts.inTargetDensity == 0 && res != null) {
            opts.inTargetDensity = res.getDisplayMetrics().densityDpi;
        }
        return decodeStream(is, pad, opts);
    }

    public static Bitmap decodeResource(Resources res, int id, Options opts) {
        Options.validate(opts);
        Bitmap bm = null;
        InputStream is = null;
        try {
            TypedValue value = new TypedValue();
            is = res.openRawResource(id, value);
            bm = decodeResourceStream(res, value, is, null, opts);
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        } catch (Exception e2) {
            if (is != null) {
                is.close();
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e3) {
                }
            }
        }
        if (bm != null || opts == null || opts.inBitmap == null) {
            return bm;
        }
        throw new IllegalArgumentException("Problem decoding into existing bitmap");
    }

    public static Bitmap decodeResource(Resources res, int id) {
        return decodeResource(res, id, null);
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length, Options opts) {
        if ((offset | length) < 0 || data.length < offset + length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        Options.validate(opts);
        Trace.traceBegin(2, "decodeBitmap");
        try {
            Bitmap bm = nativeDecodeByteArray(data, offset, length, opts, Options.nativeInBitmap(opts), Options.nativeColorSpace(opts));
            if (bm == null && opts != null) {
                if (opts.inBitmap != null) {
                    throw new IllegalArgumentException("Problem decoding into existing bitmap");
                }
            }
            setDensityFromOptions(bm, opts);
            return bm;
        } finally {
            Trace.traceEnd(2);
        }
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length) {
        return decodeByteArray(data, offset, length, null);
    }

    private static void setDensityFromOptions(Bitmap outputBitmap, Options opts) {
        if (outputBitmap != null && opts != null) {
            int density = opts.inDensity;
            if (density != 0) {
                outputBitmap.setDensity(density);
                int targetDensity = opts.inTargetDensity;
                if (targetDensity != 0 && density != targetDensity && density != opts.inScreenDensity) {
                    byte[] np = outputBitmap.getNinePatchChunk();
                    boolean isNinePatch = np != null && NinePatch.isNinePatchChunk(np);
                    if (opts.inScaled || isNinePatch) {
                        outputBitmap.setDensity(targetDensity);
                    }
                }
            } else if (opts.inBitmap != null) {
                outputBitmap.setDensity(Bitmap.getDefaultDensity());
            }
        }
    }

    public static Bitmap decodeStream(InputStream is, Rect outPadding, Options opts) {
        if (is == null) {
            return null;
        }
        Options.validate(opts);
        Bitmap bm = null;
        Trace.traceBegin(2, "decodeBitmap");
        try {
            if (is instanceof AssetInputStream) {
                bm = nativeDecodeAsset(((AssetInputStream) is).getNativeAsset(), outPadding, opts, Options.nativeInBitmap(opts), Options.nativeColorSpace(opts));
            } else {
                bm = decodeStreamInternal(is, outPadding, opts);
            }
            if (bm == null && opts != null) {
                if (opts.inBitmap != null) {
                    throw new IllegalArgumentException("Problem decoding into existing bitmap");
                }
            }
            setDensityFromOptions(bm, opts);
            return bm;
        } finally {
            Trace.traceEnd(2);
        }
    }

    private static Bitmap decodeStreamInternal(InputStream is, Rect outPadding, Options opts) {
        byte[] tempStorage = null;
        if (opts != null) {
            tempStorage = opts.inTempStorage;
        }
        if (tempStorage == null) {
            tempStorage = new byte[16384];
        }
        return nativeDecodeStream(is, tempStorage, outPadding, opts, Options.nativeInBitmap(opts), Options.nativeColorSpace(opts));
    }

    public static Bitmap decodeStream(InputStream is) {
        return decodeStream(is, null, null);
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor fd, Rect outPadding, Options opts) {
        Options.validate(opts);
        Trace.traceBegin(2, "decodeFileDescriptor");
        FileInputStream fis;
        try {
            if (nativeIsSeekable(fd)) {
                fis = nativeDecodeFileDescriptor(fd, outPadding, opts, Options.nativeInBitmap(opts), Options.nativeColorSpace(opts));
            } else {
                fis = new FileInputStream(fd);
                Bitmap bm = decodeStreamInternal(fis, outPadding, opts);
                try {
                    fis.close();
                } catch (Throwable th) {
                }
                fis = bm;
            }
            if (fis == null && opts != null) {
                if (opts.inBitmap != null) {
                    throw new IllegalArgumentException("Problem decoding into existing bitmap");
                }
            }
            setDensityFromOptions(fis, opts);
            Trace.traceEnd(2);
            return fis;
        } catch (Throwable th2) {
            Trace.traceEnd(2);
        }
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor fd) {
        return decodeFileDescriptor(fd, null, null);
    }
}
