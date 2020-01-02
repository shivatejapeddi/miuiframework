package android.speech.tts;

abstract class PlaybackQueueItem implements Runnable {
    private final Object mCallerIdentity;
    private final UtteranceProgressDispatcher mDispatcher;

    public abstract void run();

    public abstract void stop(int i);

    PlaybackQueueItem(UtteranceProgressDispatcher dispatcher, Object callerIdentity) {
        this.mDispatcher = dispatcher;
        this.mCallerIdentity = callerIdentity;
    }

    /* Access modifiers changed, original: 0000 */
    public Object getCallerIdentity() {
        return this.mCallerIdentity;
    }

    /* Access modifiers changed, original: protected */
    public UtteranceProgressDispatcher getDispatcher() {
        return this.mDispatcher;
    }
}
