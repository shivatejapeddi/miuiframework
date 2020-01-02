package android.speech.tts;

import android.os.ConditionVariable;

class SilencePlaybackQueueItem extends PlaybackQueueItem {
    private final ConditionVariable mCondVar = new ConditionVariable();
    private final long mSilenceDurationMs;

    SilencePlaybackQueueItem(UtteranceProgressDispatcher dispatcher, Object callerIdentity, long silenceDurationMs) {
        super(dispatcher, callerIdentity);
        this.mSilenceDurationMs = silenceDurationMs;
    }

    public void run() {
        getDispatcher().dispatchOnStart();
        boolean wasStopped = false;
        long j = this.mSilenceDurationMs;
        if (j > 0) {
            wasStopped = this.mCondVar.block(j);
        }
        if (wasStopped) {
            getDispatcher().dispatchOnStop();
        } else {
            getDispatcher().dispatchOnSuccess();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void stop(int errorCode) {
        this.mCondVar.open();
    }
}
