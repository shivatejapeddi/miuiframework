package android.media;

import android.media.IMiuiAudioRecord.Stub;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.app.DumpHeapActivity;
import java.io.FileDescriptor;

public class MiuiAudioRecord extends Stub {
    private static String TAG = "MiuiAudioRecord";
    private AudioRecordClient mAudioRecordClient = null;
    private long mNativeAudioRecordInJavaObj;

    public final class AudioInfo {
        public long size;
        public long timeUs;
    }

    private class AudioRecordClient implements DeathRecipient {
        private IBinder mCb;
        final /* synthetic */ MiuiAudioRecord this$0;

        AudioRecordClient(MiuiAudioRecord miuiAudioRecord, IBinder cb) {
            String str = " binder death";
            this.this$0 = miuiAudioRecord;
            if (cb != null) {
                try {
                    String access$000 = MiuiAudioRecord.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("AudioRecordClient link to ");
                    stringBuilder.append(cb);
                    stringBuilder.append(str);
                    Log.i(access$000, stringBuilder.toString());
                    cb.linkToDeath(this, 0);
                } catch (RemoteException e) {
                    String access$0002 = MiuiAudioRecord.TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("AudioRecordClient could not link to ");
                    stringBuilder2.append(cb);
                    stringBuilder2.append(str);
                    Log.w(access$0002, stringBuilder2.toString());
                    cb = null;
                }
            }
            this.mCb = cb;
        }

        public void binderDied() {
            Log.i(MiuiAudioRecord.TAG, "detect AudioRecordClient binderDied");
            this.this$0.stop();
        }

        public void release() {
            if (this.mCb != null) {
                Log.i(MiuiAudioRecord.TAG, "AudioRecordClient unlink to binder death");
                this.mCb.unlinkToDeath(this, 0);
                this.mCb = null;
            }
        }
    }

    public final class MetaData {
        public int channelCount;
        public int sampleRate;
    }

    private final native boolean native_fillBuffer(AudioInfo audioInfo, int i, int i2);

    private final native boolean native_getMetadata(MetaData metaData);

    private final native boolean native_setup(FileDescriptor fileDescriptor, long j);

    private final native boolean native_start(long j);

    private final native boolean native_stop();

    static {
        System.loadLibrary("exmedia");
    }

    public MiuiAudioRecord(FileDescriptor sharedMem, long size) {
        if (!native_setup(sharedMem, size)) {
            Log.e(TAG, "init MiuiAudioRecord fail");
        }
    }

    public MiuiAudioRecord(FileDescriptor sharedMem, long size, IBinder cb) {
        if (!native_setup(sharedMem, size)) {
            Log.e(TAG, "init MiuiAudioRecord fail");
        }
        this.mAudioRecordClient = new AudioRecordClient(this, cb);
    }

    public Bundle fillBuffer(int offset, int size) {
        AudioInfo audioInfo = new AudioInfo();
        if (native_fillBuffer(audioInfo, offset, size)) {
            Bundle ret = new Bundle();
            ret.putLong("presentationTimeUs", audioInfo.timeUs);
            ret.putLong(DumpHeapActivity.KEY_SIZE, audioInfo.size);
            return ret;
        }
        Log.d(TAG, "fillBuffer fail!");
        return null;
    }

    public Bundle getMetaData() {
        MetaData meta = new MetaData();
        if (native_getMetadata(meta)) {
            Bundle ret = new Bundle();
            ret.putInt(MediaFormat.KEY_SAMPLE_RATE, meta.sampleRate);
            ret.putInt(MediaFormat.KEY_CHANNEL_COUNT, meta.channelCount);
            return ret;
        }
        Log.d(TAG, "getMetaData fail!");
        return null;
    }

    public boolean start(long startTimeUs) {
        return native_start(startTimeUs);
    }

    public boolean stop() {
        AudioRecordClient audioRecordClient = this.mAudioRecordClient;
        if (audioRecordClient != null) {
            audioRecordClient.release();
            this.mAudioRecordClient = null;
        }
        return native_stop();
    }
}
