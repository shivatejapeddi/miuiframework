package android.media;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Rect;
import android.media.Image.Plane;
import android.os.Bundle;
import android.os.Handler;
import android.os.IHwBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.Surface;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.NioUtils;
import java.nio.ReadOnlyBufferException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class MediaCodec {
    public static final int BUFFER_FLAG_CODEC_CONFIG = 2;
    public static final int BUFFER_FLAG_END_OF_STREAM = 4;
    public static final int BUFFER_FLAG_KEY_FRAME = 1;
    public static final int BUFFER_FLAG_MUXER_DATA = 16;
    public static final int BUFFER_FLAG_PARTIAL_FRAME = 8;
    public static final int BUFFER_FLAG_SYNC_FRAME = 1;
    private static final int CB_ERROR = 3;
    private static final int CB_INPUT_AVAILABLE = 1;
    private static final int CB_OUTPUT_AVAILABLE = 2;
    private static final int CB_OUTPUT_FORMAT_CHANGE = 4;
    public static final int CONFIGURE_FLAG_ENCODE = 1;
    public static final int CRYPTO_MODE_AES_CBC = 2;
    public static final int CRYPTO_MODE_AES_CTR = 1;
    public static final int CRYPTO_MODE_UNENCRYPTED = 0;
    private static final int EVENT_CALLBACK = 1;
    private static final int EVENT_FRAME_RENDERED = 3;
    private static final int EVENT_SET_CALLBACK = 2;
    public static final int INFO_OUTPUT_BUFFERS_CHANGED = -3;
    public static final int INFO_OUTPUT_FORMAT_CHANGED = -2;
    public static final int INFO_TRY_AGAIN_LATER = -1;
    public static final String PARAMETER_KEY_HDR10_PLUS_INFO = "hdr10-plus-info";
    public static final String PARAMETER_KEY_OFFSET_TIME = "time-offset-us";
    public static final String PARAMETER_KEY_REQUEST_SYNC_FRAME = "request-sync";
    public static final String PARAMETER_KEY_SUSPEND = "drop-input-frames";
    public static final String PARAMETER_KEY_SUSPEND_TIME = "drop-start-time-us";
    public static final String PARAMETER_KEY_VIDEO_BITRATE = "video-bitrate";
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT = 1;
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING = 2;
    private final Object mBufferLock;
    private ByteBuffer[] mCachedInputBuffers;
    private ByteBuffer[] mCachedOutputBuffers;
    private Callback mCallback;
    private EventHandler mCallbackHandler;
    private MediaCodecInfo mCodecInfo;
    private final Object mCodecInfoLock = new Object();
    private MediaCrypto mCrypto;
    private final BufferMap mDequeuedInputBuffers;
    private final BufferMap mDequeuedOutputBuffers;
    private final Map<Integer, BufferInfo> mDequeuedOutputInfos;
    private EventHandler mEventHandler;
    private boolean mHasSurface = false;
    private final Object mListenerLock = new Object();
    private String mNameAtCreation;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private long mNativeContext;
    private final Lock mNativeContextLock;
    private EventHandler mOnFrameRenderedHandler;
    private OnFrameRenderedListener mOnFrameRenderedListener;

    @Retention(RetentionPolicy.SOURCE)
    public @interface BufferFlag {
    }

    public static final class BufferInfo {
        public int flags;
        public int offset;
        public long presentationTimeUs;
        public int size;

        public void set(int newOffset, int newSize, long newTimeUs, int newFlags) {
            this.offset = newOffset;
            this.size = newSize;
            this.presentationTimeUs = newTimeUs;
            this.flags = newFlags;
        }

        public BufferInfo dup() {
            BufferInfo copy = new BufferInfo();
            copy.set(this.offset, this.size, this.presentationTimeUs, this.flags);
            return copy;
        }
    }

    private static class BufferMap {
        private final Map<Integer, CodecBuffer> mMap;

        private static class CodecBuffer {
            private ByteBuffer mByteBuffer;
            private Image mImage;

            private CodecBuffer() {
            }

            public void free() {
                ByteBuffer byteBuffer = this.mByteBuffer;
                if (byteBuffer != null) {
                    NioUtils.freeDirectBuffer(byteBuffer);
                    this.mByteBuffer = null;
                }
                Image image = this.mImage;
                if (image != null) {
                    image.close();
                    this.mImage = null;
                }
            }

            public void setImage(Image image) {
                free();
                this.mImage = image;
            }

            public void setByteBuffer(ByteBuffer buffer) {
                free();
                this.mByteBuffer = buffer;
            }
        }

        private BufferMap() {
            this.mMap = new HashMap();
        }

        public void remove(int index) {
            CodecBuffer buffer = (CodecBuffer) this.mMap.get(Integer.valueOf(index));
            if (buffer != null) {
                buffer.free();
                this.mMap.remove(Integer.valueOf(index));
            }
        }

        public void put(int index, ByteBuffer newBuffer) {
            CodecBuffer buffer = (CodecBuffer) this.mMap.get(Integer.valueOf(index));
            if (buffer == null) {
                buffer = new CodecBuffer();
                this.mMap.put(Integer.valueOf(index), buffer);
            }
            buffer.setByteBuffer(newBuffer);
        }

        public void put(int index, Image newImage) {
            CodecBuffer buffer = (CodecBuffer) this.mMap.get(Integer.valueOf(index));
            if (buffer == null) {
                buffer = new CodecBuffer();
                this.mMap.put(Integer.valueOf(index), buffer);
            }
            buffer.setImage(newImage);
        }

        public void clear() {
            for (CodecBuffer buffer : this.mMap.values()) {
                buffer.free();
            }
            this.mMap.clear();
        }
    }

    public static abstract class Callback {
        public abstract void onError(MediaCodec mediaCodec, CodecException codecException);

        public abstract void onInputBufferAvailable(MediaCodec mediaCodec, int i);

        public abstract void onOutputBufferAvailable(MediaCodec mediaCodec, int i, BufferInfo bufferInfo);

        public abstract void onOutputFormatChanged(MediaCodec mediaCodec, MediaFormat mediaFormat);
    }

    public static final class CodecException extends IllegalStateException {
        private static final int ACTION_RECOVERABLE = 2;
        private static final int ACTION_TRANSIENT = 1;
        public static final int ERROR_INSUFFICIENT_RESOURCE = 1100;
        public static final int ERROR_RECLAIMED = 1101;
        private final int mActionCode;
        private final String mDiagnosticInfo;
        private final int mErrorCode;

        @Retention(RetentionPolicy.SOURCE)
        public @interface ReasonCode {
        }

        @UnsupportedAppUsage
        CodecException(int errorCode, int actionCode, String detailMessage) {
            super(detailMessage);
            this.mErrorCode = errorCode;
            this.mActionCode = actionCode;
            String sign = errorCode < 0 ? "neg_" : "";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("android.media.MediaCodec.error_");
            stringBuilder.append(sign);
            stringBuilder.append(Math.abs(errorCode));
            this.mDiagnosticInfo = stringBuilder.toString();
        }

        public boolean isTransient() {
            return this.mActionCode == 1;
        }

        public boolean isRecoverable() {
            return this.mActionCode == 2;
        }

        public int getErrorCode() {
            return this.mErrorCode;
        }

        public String getDiagnosticInfo() {
            return this.mDiagnosticInfo;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ConfigureFlag {
    }

    public static final class CryptoException extends RuntimeException {
        public static final int ERROR_FRAME_TOO_LARGE = 8;
        public static final int ERROR_INSUFFICIENT_OUTPUT_PROTECTION = 4;
        public static final int ERROR_INSUFFICIENT_SECURITY = 7;
        public static final int ERROR_KEY_EXPIRED = 2;
        public static final int ERROR_LOST_STATE = 9;
        public static final int ERROR_NO_KEY = 1;
        public static final int ERROR_RESOURCE_BUSY = 3;
        public static final int ERROR_SESSION_NOT_OPENED = 5;
        public static final int ERROR_UNSUPPORTED_OPERATION = 6;
        private int mErrorCode;

        @Retention(RetentionPolicy.SOURCE)
        public @interface CryptoErrorCode {
        }

        public CryptoException(int errorCode, String detailMessage) {
            super(detailMessage);
            this.mErrorCode = errorCode;
        }

        public int getErrorCode() {
            return this.mErrorCode;
        }
    }

    public static final class CryptoInfo {
        public byte[] iv;
        public byte[] key;
        public int mode;
        public int[] numBytesOfClearData;
        public int[] numBytesOfEncryptedData;
        public int numSubSamples;
        private Pattern pattern;
        private final Pattern zeroPattern = new Pattern(0, 0);

        public static final class Pattern {
            private int mEncryptBlocks;
            private int mSkipBlocks;

            public Pattern(int blocksToEncrypt, int blocksToSkip) {
                set(blocksToEncrypt, blocksToSkip);
            }

            public void set(int blocksToEncrypt, int blocksToSkip) {
                this.mEncryptBlocks = blocksToEncrypt;
                this.mSkipBlocks = blocksToSkip;
            }

            public int getSkipBlocks() {
                return this.mSkipBlocks;
            }

            public int getEncryptBlocks() {
                return this.mEncryptBlocks;
            }
        }

        public void set(int newNumSubSamples, int[] newNumBytesOfClearData, int[] newNumBytesOfEncryptedData, byte[] newKey, byte[] newIV, int newMode) {
            this.numSubSamples = newNumSubSamples;
            this.numBytesOfClearData = newNumBytesOfClearData;
            this.numBytesOfEncryptedData = newNumBytesOfEncryptedData;
            this.key = newKey;
            this.iv = newIV;
            this.mode = newMode;
            this.pattern = this.zeroPattern;
        }

        public void setPattern(Pattern newPattern) {
            this.pattern = newPattern;
        }

        private void setPattern(int blocksToEncrypt, int blocksToSkip) {
            this.pattern = new Pattern(blocksToEncrypt, blocksToSkip);
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.numSubSamples);
            stringBuilder.append(" subsamples, key [");
            builder.append(stringBuilder.toString());
            String hexdigits = "0123456789abcdef";
            int i = 0;
            while (true) {
                byte[] bArr = this.key;
                if (i >= bArr.length) {
                    break;
                }
                builder.append(hexdigits.charAt((bArr[i] & 240) >> 4));
                builder.append(hexdigits.charAt(this.key[i] & 15));
                i++;
            }
            builder.append("], iv [");
            for (i = 0; i < this.key.length; i++) {
                builder.append(hexdigits.charAt((this.iv[i] & 240) >> 4));
                builder.append(hexdigits.charAt(this.iv[i] & 15));
            }
            builder.append("], clear ");
            builder.append(Arrays.toString(this.numBytesOfClearData));
            builder.append(", encrypted ");
            builder.append(Arrays.toString(this.numBytesOfEncryptedData));
            return builder.toString();
        }
    }

    private class EventHandler extends Handler {
        private MediaCodec mCodec;

        public EventHandler(MediaCodec codec, Looper looper) {
            super(looper);
            this.mCodec = codec;
        }

        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 1) {
                handleCallback(msg);
            } else if (i == 2) {
                MediaCodec.this.mCallback = (Callback) msg.obj;
            } else if (i == 3) {
                synchronized (MediaCodec.this.mListenerLock) {
                    Map<String, Object> map = msg.obj;
                    int i2 = 0;
                    while (true) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(i2);
                        stringBuilder.append("-media-time-us");
                        Object mediaTimeUs = map.get(stringBuilder.toString());
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(i2);
                        stringBuilder2.append("-system-nano");
                        Object systemNano = map.get(stringBuilder2.toString());
                        if (mediaTimeUs == null || systemNano == null) {
                            break;
                        } else if (MediaCodec.this.mOnFrameRenderedListener == null) {
                            break;
                        } else {
                            MediaCodec.this.mOnFrameRenderedListener.onFrameRendered(this.mCodec, ((Long) mediaTimeUs).longValue(), ((Long) systemNano).longValue());
                            i2++;
                        }
                    }
                }
            }
        }

        private void handleCallback(Message msg) {
            if (MediaCodec.this.mCallback != null) {
                int i = msg.arg1;
                if (i == 1) {
                    i = msg.arg2;
                    synchronized (MediaCodec.this.mBufferLock) {
                        MediaCodec.this.validateInputByteBuffer(MediaCodec.this.mCachedInputBuffers, i);
                    }
                    MediaCodec.this.mCallback.onInputBufferAvailable(this.mCodec, i);
                } else if (i == 2) {
                    i = msg.arg2;
                    BufferInfo info = msg.obj;
                    synchronized (MediaCodec.this.mBufferLock) {
                        MediaCodec.this.validateOutputByteBuffer(MediaCodec.this.mCachedOutputBuffers, i, info);
                    }
                    MediaCodec.this.mCallback.onOutputBufferAvailable(this.mCodec, i, info);
                } else if (i == 3) {
                    MediaCodec.this.mCallback.onError(this.mCodec, (CodecException) msg.obj);
                } else if (i == 4) {
                    MediaCodec.this.mCallback.onOutputFormatChanged(this.mCodec, new MediaFormat((Map) msg.obj));
                }
            }
        }
    }

    public static class MediaImage extends Image {
        private static final int TYPE_YUV = 1;
        private final ByteBuffer mBuffer;
        private final int mFormat = 35;
        private final int mHeight;
        private final ByteBuffer mInfo;
        private final boolean mIsReadOnly;
        private final Plane[] mPlanes;
        private final int mScalingMode = 0;
        private long mTimestamp;
        private final int mTransform = 0;
        private final int mWidth;
        private final int mXOffset;
        private final int mYOffset;

        private class MediaPlane extends Plane {
            private final int mColInc;
            private final ByteBuffer mData;
            private final int mRowInc;

            public MediaPlane(ByteBuffer buffer, int rowInc, int colInc) {
                this.mData = buffer;
                this.mRowInc = rowInc;
                this.mColInc = colInc;
            }

            public int getRowStride() {
                MediaImage.this.throwISEIfImageIsInvalid();
                return this.mRowInc;
            }

            public int getPixelStride() {
                MediaImage.this.throwISEIfImageIsInvalid();
                return this.mColInc;
            }

            public ByteBuffer getBuffer() {
                MediaImage.this.throwISEIfImageIsInvalid();
                return this.mData;
            }
        }

        public int getFormat() {
            throwISEIfImageIsInvalid();
            return this.mFormat;
        }

        public int getHeight() {
            throwISEIfImageIsInvalid();
            return this.mHeight;
        }

        public int getWidth() {
            throwISEIfImageIsInvalid();
            return this.mWidth;
        }

        public int getTransform() {
            throwISEIfImageIsInvalid();
            return 0;
        }

        public int getScalingMode() {
            throwISEIfImageIsInvalid();
            return 0;
        }

        public long getTimestamp() {
            throwISEIfImageIsInvalid();
            return this.mTimestamp;
        }

        public Plane[] getPlanes() {
            throwISEIfImageIsInvalid();
            Plane[] planeArr = this.mPlanes;
            return (Plane[]) Arrays.copyOf(planeArr, planeArr.length);
        }

        public void close() {
            if (this.mIsImageValid) {
                NioUtils.freeDirectBuffer(this.mBuffer);
                this.mIsImageValid = false;
            }
        }

        public void setCropRect(Rect cropRect) {
            if (this.mIsReadOnly) {
                throw new ReadOnlyBufferException();
            }
            super.setCropRect(cropRect);
        }

        public MediaImage(ByteBuffer buffer, ByteBuffer info, boolean readOnly, long timestamp, int xOffset, int yOffset, Rect cropRect) {
            ByteBuffer byteBuffer = buffer;
            int i = xOffset;
            int i2 = yOffset;
            this.mTimestamp = timestamp;
            this.mIsImageValid = true;
            this.mIsReadOnly = buffer.isReadOnly();
            this.mBuffer = buffer.duplicate();
            this.mXOffset = i;
            this.mYOffset = i2;
            this.mInfo = info;
            StringBuilder stringBuilder;
            if (info.remaining() == 104) {
                int type = info.getInt();
                if (type == 1) {
                    int numPlanes = info.getInt();
                    if (numPlanes == 3) {
                        this.mWidth = info.getInt();
                        this.mHeight = info.getInt();
                        String str = "x";
                        if (this.mWidth < 1 || this.mHeight < 1) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("unsupported size: ");
                            stringBuilder.append(this.mWidth);
                            stringBuilder.append(str);
                            stringBuilder.append(this.mHeight);
                            throw new UnsupportedOperationException(stringBuilder.toString());
                        }
                        int bitDepth = info.getInt();
                        if (bitDepth == 8) {
                            int bitDepthAllocated = info.getInt();
                            if (bitDepthAllocated == 8) {
                                Rect cropRect2;
                                this.mPlanes = new MediaPlane[numPlanes];
                                int ix = 0;
                                while (ix < numPlanes) {
                                    StringBuilder stringBuilder2;
                                    int planeOffset = info.getInt();
                                    int colInc = info.getInt();
                                    int rowInc = info.getInt();
                                    int horiz = info.getInt();
                                    int vert = info.getInt();
                                    if (horiz == vert) {
                                        if (horiz == (ix == 0 ? 1 : 2)) {
                                            if (colInc < 1 || rowInc < 1) {
                                                stringBuilder2 = new StringBuilder();
                                                stringBuilder2.append("unexpected strides: ");
                                                stringBuilder2.append(colInc);
                                                stringBuilder2.append(" pixel, ");
                                                stringBuilder2.append(rowInc);
                                                stringBuilder2.append(" row on plane ");
                                                stringBuilder2.append(ix);
                                                throw new UnsupportedOperationException(stringBuilder2.toString());
                                            }
                                            buffer.clear();
                                            byteBuffer.position(((this.mBuffer.position() + planeOffset) + ((i / horiz) * colInc)) + ((i2 / vert) * rowInc));
                                            byteBuffer.limit(((buffer.position() + Utils.divUp(bitDepth, 8)) + (((this.mHeight / vert) - 1) * rowInc)) + (((this.mWidth / horiz) - 1) * colInc));
                                            this.mPlanes[ix] = new MediaPlane(buffer.slice(), rowInc, colInc);
                                            ix++;
                                            byteBuffer = buffer;
                                            ByteBuffer byteBuffer2 = info;
                                            long j = timestamp;
                                            horiz = 1;
                                        }
                                    }
                                    stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append("unexpected subsampling: ");
                                    stringBuilder2.append(horiz);
                                    stringBuilder2.append(str);
                                    stringBuilder2.append(vert);
                                    stringBuilder2.append(" on plane ");
                                    stringBuilder2.append(ix);
                                    throw new UnsupportedOperationException(stringBuilder2.toString());
                                }
                                if (cropRect == null) {
                                    cropRect2 = new Rect(0, 0, this.mWidth, this.mHeight);
                                } else {
                                    cropRect2 = cropRect;
                                }
                                cropRect2.offset(-i, -i2);
                                super.setCropRect(cropRect2);
                                return;
                            }
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("unsupported allocated bit depth: ");
                            stringBuilder.append(bitDepthAllocated);
                            throw new UnsupportedOperationException(stringBuilder.toString());
                        }
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("unsupported bit depth: ");
                        stringBuilder.append(bitDepth);
                        throw new UnsupportedOperationException(stringBuilder.toString());
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("unexpected number of planes: ");
                    stringBuilder.append(numPlanes);
                    throw new RuntimeException(stringBuilder.toString());
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("unsupported type: ");
                stringBuilder.append(type);
                throw new UnsupportedOperationException(stringBuilder.toString());
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("unsupported info length: ");
            stringBuilder.append(info.remaining());
            throw new UnsupportedOperationException(stringBuilder.toString());
        }
    }

    public static final class MetricsConstants {
        public static final String CODEC = "android.media.mediacodec.codec";
        public static final String ENCODER = "android.media.mediacodec.encoder";
        public static final String HEIGHT = "android.media.mediacodec.height";
        public static final String MIME_TYPE = "android.media.mediacodec.mime";
        public static final String MODE = "android.media.mediacodec.mode";
        public static final String MODE_AUDIO = "audio";
        public static final String MODE_VIDEO = "video";
        public static final String ROTATION = "android.media.mediacodec.rotation";
        public static final String SECURE = "android.media.mediacodec.secure";
        public static final String WIDTH = "android.media.mediacodec.width";

        private MetricsConstants() {
        }
    }

    public interface OnFrameRenderedListener {
        void onFrameRendered(MediaCodec mediaCodec, long j, long j2);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OutputBufferInfo {
    }

    static class PersistentSurface extends Surface {
        private long mPersistentObject;

        PersistentSurface() {
        }

        public void release() {
            MediaCodec.native_releasePersistentInputSurface(this);
            super.release();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface VideoScalingMode {
    }

    private final native ByteBuffer getBuffer(boolean z, int i);

    @UnsupportedAppUsage
    private final native ByteBuffer[] getBuffers(boolean z);

    private final native Map<String, Object> getFormatNative(boolean z);

    private final native Image getImage(boolean z, int i);

    private final native Map<String, Object> getOutputFormatNative(int i);

    private final native MediaCodecInfo getOwnCodecInfo();

    private final native void native_configure(String[] strArr, Object[] objArr, Surface surface, MediaCrypto mediaCrypto, IHwBinder iHwBinder, int i);

    private static final native PersistentSurface native_createPersistentInputSurface();

    private final native int native_dequeueInputBuffer(long j);

    private final native int native_dequeueOutputBuffer(BufferInfo bufferInfo, long j);

    private native void native_enableOnFrameRenderedListener(boolean z);

    private final native void native_finalize();

    private final native void native_flush();

    private native PersistableBundle native_getMetrics();

    private static final native void native_init();

    private final native void native_queueInputBuffer(int i, int i2, int i3, long j, int i4) throws CryptoException;

    private final native void native_queueSecureInputBuffer(int i, int i2, CryptoInfo cryptoInfo, long j, int i3) throws CryptoException;

    private final native void native_release();

    private static final native void native_releasePersistentInputSurface(Surface surface);

    private final native void native_reset();

    private native void native_setAudioPresentation(int i, int i2);

    private final native void native_setCallback(Callback callback);

    private final native void native_setInputSurface(Surface surface);

    private native void native_setSurface(Surface surface);

    private final native void native_setup(String str, boolean z, boolean z2);

    private final native void native_start();

    private final native void native_stop();

    @UnsupportedAppUsage
    private final native void releaseOutputBuffer(int i, boolean z, boolean z2, long j);

    @UnsupportedAppUsage
    private final native void setParameters(String[] strArr, Object[] objArr);

    public final native Surface createInputSurface();

    public final native String getCanonicalName();

    public final native void setVideoScalingMode(int i);

    public final native void signalEndOfInputStream();

    public static MediaCodec createDecoderByType(String type) throws IOException {
        return new MediaCodec(type, true, false);
    }

    public static MediaCodec createEncoderByType(String type) throws IOException {
        return new MediaCodec(type, true, true);
    }

    public static MediaCodec createByCodecName(String name) throws IOException {
        return new MediaCodec(name, false, false);
    }

    private MediaCodec(String name, boolean nameIsType, boolean encoder) {
        String str = null;
        this.mDequeuedInputBuffers = new BufferMap();
        this.mDequeuedOutputBuffers = new BufferMap();
        this.mDequeuedOutputInfos = new HashMap();
        this.mNativeContext = 0;
        this.mNativeContextLock = new ReentrantLock();
        Looper myLooper = Looper.myLooper();
        Looper looper = myLooper;
        if (myLooper != null) {
            this.mEventHandler = new EventHandler(this, looper);
        } else {
            myLooper = Looper.getMainLooper();
            looper = myLooper;
            if (myLooper != null) {
                this.mEventHandler = new EventHandler(this, looper);
            } else {
                this.mEventHandler = null;
            }
        }
        EventHandler eventHandler = this.mEventHandler;
        this.mCallbackHandler = eventHandler;
        this.mOnFrameRenderedHandler = eventHandler;
        this.mBufferLock = new Object();
        if (!nameIsType) {
            str = name;
        }
        this.mNameAtCreation = str;
        native_setup(name, nameIsType, encoder);
    }

    /* Access modifiers changed, original: protected */
    public void finalize() {
        native_finalize();
        this.mCrypto = null;
    }

    public final void reset() {
        freeAllTrackedBuffers();
        native_reset();
        this.mCrypto = null;
    }

    public final void release() {
        freeAllTrackedBuffers();
        native_release();
        this.mCrypto = null;
    }

    public void configure(MediaFormat format, Surface surface, MediaCrypto crypto, int flags) {
        configure(format, surface, crypto, null, flags);
    }

    public void configure(MediaFormat format, Surface surface, int flags, MediaDescrambler descrambler) {
        configure(format, surface, null, descrambler != null ? descrambler.getBinder() : null, flags);
    }

    private void configure(MediaFormat format, Surface surface, MediaCrypto crypto, IHwBinder descramblerBinder, int flags) {
        MediaCrypto mediaCrypto = crypto;
        if (mediaCrypto == null || descramblerBinder == null) {
            Object[] values;
            String[] keys = null;
            if (format != null) {
                Map<String, Object> formatMap = format.getMap();
                String[] keys2 = new String[formatMap.size()];
                Object[] values2 = new Object[formatMap.size()];
                int i = 0;
                for (Entry<String, Object> entry : formatMap.entrySet()) {
                    if (((String) entry.getKey()).equals(MediaFormat.KEY_AUDIO_SESSION_ID)) {
                        try {
                            int sessionId = ((Integer) entry.getValue()).intValue();
                            keys2[i] = "audio-hw-sync";
                            values2[i] = Integer.valueOf(AudioSystem.getAudioHwSyncForSession(sessionId));
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Wrong Session ID Parameter!");
                        }
                    }
                    keys2[i] = (String) entry.getKey();
                    values2[i] = entry.getValue();
                    i++;
                }
                values = values2;
                keys = keys2;
            } else {
                values = null;
            }
            this.mHasSurface = surface != null;
            this.mCrypto = mediaCrypto;
            native_configure(keys, values, surface, crypto, descramblerBinder, flags);
            return;
        }
        throw new IllegalArgumentException("Can't use crypto and descrambler together!");
    }

    public void setOutputSurface(Surface surface) {
        if (this.mHasSurface) {
            native_setSurface(surface);
            return;
        }
        throw new IllegalStateException("codec was not configured for an output surface");
    }

    public static Surface createPersistentInputSurface() {
        return native_createPersistentInputSurface();
    }

    public void setInputSurface(Surface surface) {
        if (surface instanceof PersistentSurface) {
            native_setInputSurface(surface);
            return;
        }
        throw new IllegalArgumentException("not a PersistentSurface");
    }

    public final void start() {
        native_start();
        synchronized (this.mBufferLock) {
            cacheBuffers(true);
            cacheBuffers(false);
        }
    }

    public final void stop() {
        native_stop();
        freeAllTrackedBuffers();
        synchronized (this.mListenerLock) {
            if (this.mCallbackHandler != null) {
                this.mCallbackHandler.removeMessages(2);
                this.mCallbackHandler.removeMessages(1);
            }
            if (this.mOnFrameRenderedHandler != null) {
                this.mOnFrameRenderedHandler.removeMessages(3);
            }
        }
    }

    public final void flush() {
        synchronized (this.mBufferLock) {
            invalidateByteBuffers(this.mCachedInputBuffers);
            invalidateByteBuffers(this.mCachedOutputBuffers);
            this.mDequeuedInputBuffers.clear();
            this.mDequeuedOutputBuffers.clear();
        }
        native_flush();
    }

    public final void queueInputBuffer(int index, int offset, int size, long presentationTimeUs, int flags) throws CryptoException {
        synchronized (this.mBufferLock) {
            invalidateByteBuffer(this.mCachedInputBuffers, index);
            this.mDequeuedInputBuffers.remove(index);
        }
        try {
            native_queueInputBuffer(index, offset, size, presentationTimeUs, flags);
        } catch (CryptoException | IllegalStateException e) {
            revalidateByteBuffer(this.mCachedInputBuffers, index);
            throw e;
        }
    }

    public final void queueSecureInputBuffer(int index, int offset, CryptoInfo info, long presentationTimeUs, int flags) throws CryptoException {
        synchronized (this.mBufferLock) {
            invalidateByteBuffer(this.mCachedInputBuffers, index);
            this.mDequeuedInputBuffers.remove(index);
        }
        try {
            native_queueSecureInputBuffer(index, offset, info, presentationTimeUs, flags);
        } catch (CryptoException | IllegalStateException e) {
            revalidateByteBuffer(this.mCachedInputBuffers, index);
            throw e;
        }
    }

    public final int dequeueInputBuffer(long timeoutUs) {
        int res = native_dequeueInputBuffer(timeoutUs);
        if (res >= 0) {
            synchronized (this.mBufferLock) {
                validateInputByteBuffer(this.mCachedInputBuffers, res);
            }
        }
        return res;
    }

    public final int dequeueOutputBuffer(BufferInfo info, long timeoutUs) {
        int res = native_dequeueOutputBuffer(info, timeoutUs);
        synchronized (this.mBufferLock) {
            if (res == -3) {
                try {
                    cacheBuffers(false);
                } catch (Throwable th) {
                }
            } else if (res >= 0) {
                validateOutputByteBuffer(this.mCachedOutputBuffers, res, info);
                if (this.mHasSurface) {
                    this.mDequeuedOutputInfos.put(Integer.valueOf(res), info.dup());
                }
            }
        }
        return res;
    }

    public final void releaseOutputBuffer(int index, boolean render) {
        synchronized (this.mBufferLock) {
            invalidateByteBuffer(this.mCachedOutputBuffers, index);
            this.mDequeuedOutputBuffers.remove(index);
            if (this.mHasSurface) {
                BufferInfo info = (BufferInfo) this.mDequeuedOutputInfos.remove(Integer.valueOf(index));
            }
        }
        releaseOutputBuffer(index, render, false, 0);
    }

    public final void releaseOutputBuffer(int index, long renderTimestampNs) {
        synchronized (this.mBufferLock) {
            invalidateByteBuffer(this.mCachedOutputBuffers, index);
            this.mDequeuedOutputBuffers.remove(index);
            if (this.mHasSurface) {
                BufferInfo info = (BufferInfo) this.mDequeuedOutputInfos.remove(Integer.valueOf(index));
            }
        }
        releaseOutputBuffer(index, true, true, renderTimestampNs);
    }

    public final MediaFormat getOutputFormat() {
        return new MediaFormat(getFormatNative(false));
    }

    public final MediaFormat getInputFormat() {
        return new MediaFormat(getFormatNative(true));
    }

    public final MediaFormat getOutputFormat(int index) {
        return new MediaFormat(getOutputFormatNative(index));
    }

    private final void invalidateByteBuffer(ByteBuffer[] buffers, int index) {
        if (buffers != null && index >= 0 && index < buffers.length) {
            ByteBuffer buffer = buffers[index];
            if (buffer != null) {
                buffer.setAccessible(false);
            }
        }
    }

    private final void validateInputByteBuffer(ByteBuffer[] buffers, int index) {
        if (buffers != null && index >= 0 && index < buffers.length) {
            ByteBuffer buffer = buffers[index];
            if (buffer != null) {
                buffer.setAccessible(true);
                buffer.clear();
            }
        }
    }

    private final void revalidateByteBuffer(ByteBuffer[] buffers, int index) {
        synchronized (this.mBufferLock) {
            if (buffers != null && index >= 0) {
                if (index < buffers.length) {
                    ByteBuffer buffer = buffers[index];
                    if (buffer != null) {
                        buffer.setAccessible(true);
                    }
                }
            }
        }
    }

    private final void validateOutputByteBuffer(ByteBuffer[] buffers, int index, BufferInfo info) {
        if (buffers != null && index >= 0 && index < buffers.length) {
            ByteBuffer buffer = buffers[index];
            if (buffer != null) {
                buffer.setAccessible(true);
                buffer.limit(info.offset + info.size).position(info.offset);
            }
        }
    }

    private final void invalidateByteBuffers(ByteBuffer[] buffers) {
        if (buffers != null) {
            for (ByteBuffer buffer : buffers) {
                if (buffer != null) {
                    buffer.setAccessible(false);
                }
            }
        }
    }

    private final void freeByteBuffer(ByteBuffer buffer) {
        if (buffer != null) {
            NioUtils.freeDirectBuffer(buffer);
        }
    }

    private final void freeByteBuffers(ByteBuffer[] buffers) {
        if (buffers != null) {
            for (ByteBuffer buffer : buffers) {
                freeByteBuffer(buffer);
            }
        }
    }

    private final void freeAllTrackedBuffers() {
        synchronized (this.mBufferLock) {
            freeByteBuffers(this.mCachedInputBuffers);
            freeByteBuffers(this.mCachedOutputBuffers);
            this.mCachedInputBuffers = null;
            this.mCachedOutputBuffers = null;
            this.mDequeuedInputBuffers.clear();
            this.mDequeuedOutputBuffers.clear();
        }
    }

    private final void cacheBuffers(boolean input) {
        ByteBuffer[] buffers = null;
        try {
            buffers = getBuffers(input);
            invalidateByteBuffers(buffers);
        } catch (IllegalStateException e) {
        }
        if (input) {
            this.mCachedInputBuffers = buffers;
        } else {
            this.mCachedOutputBuffers = buffers;
        }
    }

    public ByteBuffer[] getInputBuffers() {
        ByteBuffer[] byteBufferArr = this.mCachedInputBuffers;
        if (byteBufferArr != null) {
            return byteBufferArr;
        }
        throw new IllegalStateException();
    }

    public ByteBuffer[] getOutputBuffers() {
        ByteBuffer[] byteBufferArr = this.mCachedOutputBuffers;
        if (byteBufferArr != null) {
            return byteBufferArr;
        }
        throw new IllegalStateException();
    }

    public ByteBuffer getInputBuffer(int index) {
        ByteBuffer newBuffer = getBuffer(true, index);
        synchronized (this.mBufferLock) {
            invalidateByteBuffer(this.mCachedInputBuffers, index);
            this.mDequeuedInputBuffers.put(index, newBuffer);
        }
        return newBuffer;
    }

    public Image getInputImage(int index) {
        Image newImage = getImage(true, index);
        synchronized (this.mBufferLock) {
            invalidateByteBuffer(this.mCachedInputBuffers, index);
            this.mDequeuedInputBuffers.put(index, newImage);
        }
        return newImage;
    }

    public ByteBuffer getOutputBuffer(int index) {
        ByteBuffer newBuffer = getBuffer(null, index);
        synchronized (this.mBufferLock) {
            invalidateByteBuffer(this.mCachedOutputBuffers, index);
            this.mDequeuedOutputBuffers.put(index, newBuffer);
        }
        return newBuffer;
    }

    public Image getOutputImage(int index) {
        Image newImage = getImage(null, index);
        synchronized (this.mBufferLock) {
            invalidateByteBuffer(this.mCachedOutputBuffers, index);
            this.mDequeuedOutputBuffers.put(index, newImage);
        }
        return newImage;
    }

    public void setAudioPresentation(AudioPresentation presentation) {
        if (presentation != null) {
            native_setAudioPresentation(presentation.getPresentationId(), presentation.getProgramId());
            return;
        }
        throw new NullPointerException("audio presentation is null");
    }

    public final String getName() {
        String canonicalName = getCanonicalName();
        String str = this.mNameAtCreation;
        return str != null ? str : canonicalName;
    }

    public PersistableBundle getMetrics() {
        return native_getMetrics();
    }

    public final void setParameters(Bundle params) {
        if (params != null) {
            String[] keys = new String[params.size()];
            Object[] values = new Object[params.size()];
            int i = 0;
            for (String key : params.keySet()) {
                keys[i] = key;
                Object value = params.get(key);
                if (value instanceof byte[]) {
                    values[i] = ByteBuffer.wrap((byte[]) value);
                } else {
                    values[i] = value;
                }
                i++;
            }
            setParameters(keys, values);
        }
    }

    public void setCallback(Callback cb, Handler handler) {
        if (cb != null) {
            synchronized (this.mListenerLock) {
                EventHandler newHandler = getEventHandlerOn(handler, this.mCallbackHandler);
                if (newHandler != this.mCallbackHandler) {
                    this.mCallbackHandler.removeMessages(2);
                    this.mCallbackHandler.removeMessages(1);
                    this.mCallbackHandler = newHandler;
                }
            }
        } else {
            EventHandler eventHandler = this.mCallbackHandler;
            if (eventHandler != null) {
                eventHandler.removeMessages(2);
                this.mCallbackHandler.removeMessages(1);
            }
        }
        Message msg = this.mCallbackHandler;
        if (msg != null) {
            this.mCallbackHandler.sendMessage(msg.obtainMessage(2, 0, 0, cb));
            native_setCallback(cb);
        }
    }

    public void setCallback(Callback cb) {
        setCallback(cb, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0029  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0027  */
    public void setOnFrameRenderedListener(android.media.MediaCodec.OnFrameRenderedListener r5, android.os.Handler r6) {
        /*
        r4 = this;
        r0 = r4.mListenerLock;
        monitor-enter(r0);
        r4.mOnFrameRenderedListener = r5;	 Catch:{ all -> 0x002f }
        r1 = 3;
        if (r5 == 0) goto L_0x001a;
    L_0x0008:
        r2 = r4.mOnFrameRenderedHandler;	 Catch:{ all -> 0x002f }
        r2 = r4.getEventHandlerOn(r6, r2);	 Catch:{ all -> 0x002f }
        r3 = r4.mOnFrameRenderedHandler;	 Catch:{ all -> 0x002f }
        if (r2 == r3) goto L_0x0017;
    L_0x0012:
        r3 = r4.mOnFrameRenderedHandler;	 Catch:{ all -> 0x002f }
        r3.removeMessages(r1);	 Catch:{ all -> 0x002f }
    L_0x0017:
        r4.mOnFrameRenderedHandler = r2;	 Catch:{ all -> 0x002f }
        goto L_0x0024;
    L_0x001a:
        r2 = r4.mOnFrameRenderedHandler;	 Catch:{ all -> 0x002f }
        if (r2 == 0) goto L_0x0024;
    L_0x001e:
        r2 = r4.mOnFrameRenderedHandler;	 Catch:{ all -> 0x002f }
        r2.removeMessages(r1);	 Catch:{ all -> 0x002f }
        goto L_0x0025;
    L_0x0025:
        if (r5 == 0) goto L_0x0029;
    L_0x0027:
        r1 = 1;
        goto L_0x002a;
    L_0x0029:
        r1 = 0;
    L_0x002a:
        r4.native_enableOnFrameRenderedListener(r1);	 Catch:{ all -> 0x002f }
        monitor-exit(r0);	 Catch:{ all -> 0x002f }
        return;
    L_0x002f:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x002f }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaCodec.setOnFrameRenderedListener(android.media.MediaCodec$OnFrameRenderedListener, android.os.Handler):void");
    }

    private EventHandler getEventHandlerOn(Handler handler, EventHandler lastHandler) {
        if (handler == null) {
            return this.mEventHandler;
        }
        Looper looper = handler.getLooper();
        if (lastHandler.getLooper() == looper) {
            return lastHandler;
        }
        return new EventHandler(this, looper);
    }

    private void postEventFromNative(int what, int arg1, int arg2, Object obj) {
        synchronized (this.mListenerLock) {
            EventHandler handler = this.mEventHandler;
            if (what == 1) {
                handler = this.mCallbackHandler;
            } else if (what == 3) {
                handler = this.mOnFrameRenderedHandler;
            }
            if (handler != null) {
                handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
            }
        }
    }

    public MediaCodecInfo getCodecInfo() {
        MediaCodecInfo mediaCodecInfo;
        String name = getName();
        synchronized (this.mCodecInfoLock) {
            if (this.mCodecInfo == null) {
                this.mCodecInfo = getOwnCodecInfo();
                if (this.mCodecInfo == null) {
                    this.mCodecInfo = MediaCodecList.getInfoFor(name);
                }
            }
            mediaCodecInfo = this.mCodecInfo;
        }
        return mediaCodecInfo;
    }

    static {
        System.loadLibrary("media_jni");
        native_init();
    }

    private final long lockAndGetContext() {
        this.mNativeContextLock.lock();
        return this.mNativeContext;
    }

    private final void setAndUnlockContext(long context) {
        this.mNativeContext = context;
        this.mNativeContextLock.unlock();
    }
}
