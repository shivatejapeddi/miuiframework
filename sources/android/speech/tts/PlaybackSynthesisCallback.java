package android.speech.tts;

import android.util.Log;

class PlaybackSynthesisCallback extends AbstractSynthesisCallback {
    private static final boolean DBG = false;
    private static final int MIN_AUDIO_BUFFER_SIZE = 8192;
    private static final String TAG = "PlaybackSynthesisRequest";
    private final AudioOutputParams mAudioParams;
    private final AudioPlaybackHandler mAudioTrackHandler;
    private final Object mCallerIdentity;
    private final UtteranceProgressDispatcher mDispatcher;
    private volatile boolean mDone = false;
    private SynthesisPlaybackQueueItem mItem = null;
    private final AbstractEventLogger mLogger;
    private final Object mStateLock = new Object();
    protected int mStatusCode;

    PlaybackSynthesisCallback(AudioOutputParams audioParams, AudioPlaybackHandler audioTrackHandler, UtteranceProgressDispatcher dispatcher, Object callerIdentity, AbstractEventLogger logger, boolean clientIsUsingV2) {
        super(clientIsUsingV2);
        this.mAudioParams = audioParams;
        this.mAudioTrackHandler = audioTrackHandler;
        this.mDispatcher = dispatcher;
        this.mCallerIdentity = callerIdentity;
        this.mLogger = logger;
        this.mStatusCode = 0;
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:14:0x001d, code skipped:
            if (r1 == null) goto L_0x0023;
     */
    /* JADX WARNING: Missing block: B:15:0x001f, code skipped:
            r1.stop(-2);
     */
    /* JADX WARNING: Missing block: B:16:0x0023, code skipped:
            r3.mLogger.onCompleted(-2);
            r3.mDispatcher.dispatchOnStop();
     */
    /* JADX WARNING: Missing block: B:17:0x002d, code skipped:
            return;
     */
    public void stop() {
        /*
        r3 = this;
        r0 = r3.mStateLock;
        monitor-enter(r0);
        r1 = r3.mDone;	 Catch:{ all -> 0x002e }
        if (r1 == 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r0);	 Catch:{ all -> 0x002e }
        return;
    L_0x0009:
        r1 = r3.mStatusCode;	 Catch:{ all -> 0x002e }
        r2 = -2;
        if (r1 != r2) goto L_0x0018;
    L_0x000e:
        r1 = "PlaybackSynthesisRequest";
        r2 = "stop() called twice";
        android.util.Log.w(r1, r2);	 Catch:{ all -> 0x002e }
        monitor-exit(r0);	 Catch:{ all -> 0x002e }
        return;
    L_0x0018:
        r1 = r3.mItem;	 Catch:{ all -> 0x002e }
        r3.mStatusCode = r2;	 Catch:{ all -> 0x002e }
        monitor-exit(r0);	 Catch:{ all -> 0x002e }
        if (r1 == 0) goto L_0x0023;
    L_0x001f:
        r1.stop(r2);
        goto L_0x002d;
    L_0x0023:
        r0 = r3.mLogger;
        r0.onCompleted(r2);
        r0 = r3.mDispatcher;
        r0.dispatchOnStop();
    L_0x002d:
        return;
    L_0x002e:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x002e }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.speech.tts.PlaybackSynthesisCallback.stop():void");
    }

    public int getMaxBufferSize() {
        return 8192;
    }

    public boolean hasStarted() {
        boolean z;
        synchronized (this.mStateLock) {
            z = this.mItem != null;
        }
        return z;
    }

    public boolean hasFinished() {
        boolean z;
        synchronized (this.mStateLock) {
            z = this.mDone;
        }
        return z;
    }

    public int start(int sampleRateInHz, int audioFormat, int channelCount) {
        if (!(audioFormat == 3 || audioFormat == 2 || audioFormat == 4)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Audio format encoding ");
            stringBuilder.append(audioFormat);
            stringBuilder.append(" not supported. Please use one of AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT or AudioFormat.ENCODING_PCM_FLOAT");
            Log.w(TAG, stringBuilder.toString());
        }
        this.mDispatcher.dispatchOnBeginSynthesis(sampleRateInHz, audioFormat, channelCount);
        int channelConfig = BlockingAudioTrack.getChannelConfig(channelCount);
        synchronized (this.mStateLock) {
            if (channelConfig == 0) {
                String str = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Unsupported number of channels :");
                stringBuilder2.append(channelCount);
                Log.e(str, stringBuilder2.toString());
                this.mStatusCode = -5;
                return -1;
            } else if (this.mStatusCode == -2) {
                int errorCodeOnStop = errorCodeOnStop();
                return errorCodeOnStop;
            } else if (this.mStatusCode != 0) {
                return -1;
            } else if (this.mItem != null) {
                Log.e(TAG, "Start called twice");
                return -1;
            } else {
                SynthesisPlaybackQueueItem synthesisPlaybackQueueItem = new SynthesisPlaybackQueueItem(this.mAudioParams, sampleRateInHz, audioFormat, channelCount, this.mDispatcher, this.mCallerIdentity, this.mLogger);
                this.mAudioTrackHandler.enqueue(synthesisPlaybackQueueItem);
                this.mItem = synthesisPlaybackQueueItem;
                return 0;
            }
        }
    }

    public int audioAvailable(byte[] buffer, int offset, int length) {
        if (length > getMaxBufferSize() || length <= 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("buffer is too large or of zero length (");
            stringBuilder.append(length);
            stringBuilder.append(" bytes)");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        synchronized (this.mStateLock) {
            if (this.mItem == null) {
                this.mStatusCode = -5;
                return -1;
            } else if (this.mStatusCode != 0) {
                return -1;
            } else if (this.mStatusCode == -2) {
                int errorCodeOnStop = errorCodeOnStop();
                return errorCodeOnStop;
            } else {
                SynthesisPlaybackQueueItem item = this.mItem;
                byte[] bufferCopy = new byte[length];
                System.arraycopy(buffer, offset, bufferCopy, 0, length);
                this.mDispatcher.dispatchOnAudioAvailable(bufferCopy);
                try {
                    item.put(bufferCopy);
                    this.mLogger.onEngineDataReceived();
                    return 0;
                } catch (InterruptedException e) {
                    InterruptedException ie = e;
                    synchronized (this.mStateLock) {
                        this.mStatusCode = -5;
                        return -1;
                    }
                }
            }
        }
    }

    /* JADX WARNING: Missing block: B:24:0x004b, code skipped:
            if (r0 != 0) goto L_0x0051;
     */
    /* JADX WARNING: Missing block: B:25:0x004d, code skipped:
            r1.done();
     */
    /* JADX WARNING: Missing block: B:26:0x0051, code skipped:
            r1.stop(r0);
     */
    /* JADX WARNING: Missing block: B:27:0x0054, code skipped:
            r6.mLogger.onEngineComplete();
     */
    /* JADX WARNING: Missing block: B:28:0x005a, code skipped:
            return 0;
     */
    public int done() {
        /*
        r6 = this;
        r0 = 0;
        r1 = 0;
        r2 = r6.mStateLock;
        monitor-enter(r2);
        r3 = r6.mDone;	 Catch:{ all -> 0x005b }
        r4 = -1;
        if (r3 == 0) goto L_0x0013;
    L_0x000a:
        r3 = "PlaybackSynthesisRequest";
        r5 = "Duplicate call to done()";
        android.util.Log.w(r3, r5);	 Catch:{ all -> 0x005b }
        monitor-exit(r2);	 Catch:{ all -> 0x005b }
        return r4;
    L_0x0013:
        r3 = r6.mStatusCode;	 Catch:{ all -> 0x005b }
        r5 = -2;
        if (r3 != r5) goto L_0x001e;
    L_0x0018:
        r3 = r6.errorCodeOnStop();	 Catch:{ all -> 0x005b }
        monitor-exit(r2);	 Catch:{ all -> 0x005b }
        return r3;
    L_0x001e:
        r3 = 1;
        r6.mDone = r3;	 Catch:{ all -> 0x005b }
        r3 = r6.mItem;	 Catch:{ all -> 0x005b }
        if (r3 != 0) goto L_0x0044;
    L_0x0025:
        r3 = "PlaybackSynthesisRequest";
        r5 = "done() was called before start() call";
        android.util.Log.w(r3, r5);	 Catch:{ all -> 0x005b }
        r3 = r6.mStatusCode;	 Catch:{ all -> 0x005b }
        if (r3 != 0) goto L_0x0036;
    L_0x0030:
        r3 = r6.mDispatcher;	 Catch:{ all -> 0x005b }
        r3.dispatchOnSuccess();	 Catch:{ all -> 0x005b }
        goto L_0x003d;
    L_0x0036:
        r3 = r6.mDispatcher;	 Catch:{ all -> 0x005b }
        r5 = r6.mStatusCode;	 Catch:{ all -> 0x005b }
        r3.dispatchOnError(r5);	 Catch:{ all -> 0x005b }
    L_0x003d:
        r3 = r6.mLogger;	 Catch:{ all -> 0x005b }
        r3.onEngineComplete();	 Catch:{ all -> 0x005b }
        monitor-exit(r2);	 Catch:{ all -> 0x005b }
        return r4;
    L_0x0044:
        r3 = r6.mItem;	 Catch:{ all -> 0x005b }
        r1 = r3;
        r3 = r6.mStatusCode;	 Catch:{ all -> 0x005b }
        r0 = r3;
        monitor-exit(r2);	 Catch:{ all -> 0x005b }
        if (r0 != 0) goto L_0x0051;
    L_0x004d:
        r1.done();
        goto L_0x0054;
    L_0x0051:
        r1.stop(r0);
    L_0x0054:
        r2 = r6.mLogger;
        r2.onEngineComplete();
        r2 = 0;
        return r2;
    L_0x005b:
        r3 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x005b }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.speech.tts.PlaybackSynthesisCallback.done():int");
    }

    public void error() {
        error(-3);
    }

    public void error(int errorCode) {
        synchronized (this.mStateLock) {
            if (this.mDone) {
                return;
            }
            this.mStatusCode = errorCode;
        }
    }

    public void rangeStart(int markerInFrames, int start, int end) {
        SynthesisPlaybackQueueItem synthesisPlaybackQueueItem = this.mItem;
        if (synthesisPlaybackQueueItem == null) {
            Log.e(TAG, "mItem is null");
        } else {
            synthesisPlaybackQueueItem.rangeStart(markerInFrames, start, end);
        }
    }
}
