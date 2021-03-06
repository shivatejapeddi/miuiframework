package android.speech.tts;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.ConditionVariable;
import android.util.Log;

class AudioPlaybackQueueItem extends PlaybackQueueItem {
    private static final String TAG = "TTS.AudioQueueItem";
    private final AudioOutputParams mAudioParams;
    private final Context mContext;
    private final ConditionVariable mDone = new ConditionVariable();
    private volatile boolean mFinished = false;
    private MediaPlayer mPlayer = null;
    private final Uri mUri;

    AudioPlaybackQueueItem(UtteranceProgressDispatcher dispatcher, Object callerIdentity, Context context, Uri uri, AudioOutputParams audioParams) {
        super(dispatcher, callerIdentity);
        this.mContext = context;
        this.mUri = uri;
        this.mAudioParams = audioParams;
    }

    public void run() {
        UtteranceProgressDispatcher dispatcher = getDispatcher();
        dispatcher.dispatchOnStart();
        int sessionId = this.mAudioParams.mSessionId;
        this.mPlayer = MediaPlayer.create(this.mContext, this.mUri, null, this.mAudioParams.mAudioAttributes, sessionId > 0 ? sessionId : 0);
        MediaPlayer mediaPlayer = this.mPlayer;
        if (mediaPlayer == null) {
            dispatcher.dispatchOnError(-5);
            return;
        }
        try {
            mediaPlayer.setOnErrorListener(new OnErrorListener() {
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Audio playback error: ");
                    stringBuilder.append(what);
                    stringBuilder.append(", ");
                    stringBuilder.append(extra);
                    Log.w(AudioPlaybackQueueItem.TAG, stringBuilder.toString());
                    AudioPlaybackQueueItem.this.mDone.open();
                    return true;
                }
            });
            this.mPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    AudioPlaybackQueueItem.this.mFinished = true;
                    AudioPlaybackQueueItem.this.mDone.open();
                }
            });
            setupVolume(this.mPlayer, this.mAudioParams.mVolume, this.mAudioParams.mPan);
            this.mPlayer.start();
            this.mDone.block();
            finish();
        } catch (IllegalArgumentException ex) {
            Log.w(TAG, "MediaPlayer failed", ex);
            this.mDone.open();
        }
        if (this.mFinished) {
            dispatcher.dispatchOnSuccess();
        } else {
            dispatcher.dispatchOnStop();
        }
    }

    private static void setupVolume(MediaPlayer player, float volume, float pan) {
        float vol = clip(volume, 0.0f, 1.0f);
        float panning = clip(pan, -1.0f, 1.0f);
        float volLeft = vol;
        float volRight = vol;
        if (panning > 0.0f) {
            volLeft *= 1.0f - panning;
        } else if (panning < 0.0f) {
            volRight *= 1.0f + panning;
        }
        player.setVolume(volLeft, volRight);
    }

    private static final float clip(float value, float min, float max) {
        if (value < min) {
            return min;
        }
        return value < max ? value : max;
    }

    private void finish() {
        try {
            this.mPlayer.stop();
        } catch (IllegalStateException e) {
        }
        this.mPlayer.release();
    }

    /* Access modifiers changed, original: 0000 */
    public void stop(int errorCode) {
        this.mDone.open();
    }
}
