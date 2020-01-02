package android.media;

import android.app.job.JobInfo;
import android.util.TimedRemoteCaller;
import java.nio.ByteBuffer;

class MiniThumbFileInjector {
    private static final int MAX_MINI_THUMB_COUNT = 5000;

    MiniThumbFileInjector() {
    }

    public static long getPosition(long id) {
        return (id % TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS) * JobInfo.MIN_BACKOFF_MILLIS;
    }

    public static boolean isMatch(ByteBuffer buffer, long id) {
        return buffer.get() == (byte) 1 && id == buffer.getLong();
    }
}
