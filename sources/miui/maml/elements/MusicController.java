package miui.maml.elements;

import android.content.Context;
import android.media.MediaMetadata;
import android.media.Rating;
import android.media.session.MediaController;
import android.media.session.MediaController.Callback;
import android.media.session.MediaController.PlaybackInfo;
import android.media.session.MediaSession.QueueItem;
import android.media.session.MediaSessionManager;
import android.media.session.MediaSessionManager.OnActiveSessionsChangedListener;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import java.util.List;

public class MusicController {
    private static final String TAG = "MAML_MusicController";
    private Context mContext;
    private Handler mHandler;
    private Callback mMediaCallback = new Callback() {
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
            Log.d(MusicController.TAG, "onSessionDestroyed");
            if (MusicController.this.mUpdateListener != null) {
                MusicController.this.mUpdateListener.onSessionDestroyed();
            }
        }

        public void onSessionEvent(String event, Bundle extras) {
            super.onSessionEvent(event, extras);
            Log.d(MusicController.TAG, "onSessionEvent");
        }

        public void onPlaybackStateChanged(PlaybackState state) {
            super.onPlaybackStateChanged(state);
            Log.d(MusicController.TAG, "onPlaybackStateChanged");
            if (MusicController.this.mUpdateListener == null) {
                return;
            }
            if (state != null) {
                MusicController.this.mUpdateListener.onClientPlaybackStateUpdate(state.getState());
                MusicController.this.mUpdateListener.onClientPlaybackActionUpdate(state.getActions());
                return;
            }
            MusicController.this.mUpdateListener.onClientPlaybackStateUpdate(0);
        }

        public void onMetadataChanged(MediaMetadata metadata) {
            super.onMetadataChanged(metadata);
            Log.d(MusicController.TAG, "onMetadataChanged");
            if (MusicController.this.mUpdateListener != null) {
                MusicController.this.mUpdateListener.onClientMetadataUpdate(metadata);
            }
        }

        public void onQueueChanged(List<QueueItem> queue) {
            super.onQueueChanged(queue);
        }

        public void onQueueTitleChanged(CharSequence title) {
            super.onQueueTitleChanged(title);
        }

        public void onExtrasChanged(Bundle extras) {
            super.onExtrasChanged(extras);
        }

        public void onAudioInfoChanged(PlaybackInfo info) {
            super.onAudioInfoChanged(info);
        }
    };
    private MediaController mMediaController;
    private MediaSessionManager mSessionManager;
    private OnActiveSessionsChangedListener mSessionsChangedListener = new OnActiveSessionsChangedListener() {
        public void onActiveSessionsChanged(List<MediaController> controllers) {
            MusicController.this.resetMediaController(controllers);
            Log.d(MusicController.TAG, "onActiveSessionsChanged");
        }
    };
    private OnClientUpdateListener mUpdateListener;

    public interface OnClientUpdateListener {
        void onClientChange();

        void onClientMetadataUpdate(MediaMetadata mediaMetadata);

        void onClientPlaybackActionUpdate(long j);

        void onClientPlaybackStateUpdate(int i);

        void onSessionDestroyed();
    }

    public MusicController(Context context, Handler handler) {
        this.mContext = context.getApplicationContext();
        this.mHandler = handler;
        this.mSessionManager = (MediaSessionManager) this.mContext.getSystemService(Context.MEDIA_SESSION_SERVICE);
        init();
    }

    public void init() {
        Log.d(TAG, "init");
        resetMediaController(this.mSessionManager.getActiveSessions(null));
        this.mSessionManager.addOnActiveSessionsChangedListener(this.mSessionsChangedListener, null, this.mHandler);
    }

    public void reset() {
        resetMediaController(this.mSessionManager.getActiveSessions(null));
    }

    private void resetMediaController(List<MediaController> controllers) {
        Log.d(TAG, "resetMediaController");
        clearMediaController();
        if (controllers != null) {
            if (controllers.size() > 0) {
                this.mMediaController = (MediaController) controllers.get(0);
            }
            initMediaController();
            updateInfoToListener();
        }
    }

    private void clearMediaController() {
        String str = TAG;
        Log.d(str, "clearMediaController");
        if (this.mMediaController != null) {
            OnClientUpdateListener onClientUpdateListener = this.mUpdateListener;
            if (onClientUpdateListener != null) {
                onClientUpdateListener.onClientChange();
            }
            try {
                this.mMediaController.unregisterCallback(this.mMediaCallback);
            } catch (Exception e) {
                Log.e(str, "unregister MediaController.Callback failed");
            }
            this.mMediaController = null;
        }
    }

    private void initMediaController() {
        String str = TAG;
        Log.d(str, "initMediaController");
        MediaController mediaController = this.mMediaController;
        if (mediaController != null) {
            try {
                mediaController.registerCallback(this.mMediaCallback, this.mHandler);
            } catch (Exception e) {
                Log.e(str, "register MediaController.Callback failed");
            }
        }
    }

    private void updateInfoToListener() {
        Log.d(TAG, "updateInfoToListener");
        if (this.mMediaController != null) {
            OnClientUpdateListener onClientUpdateListener = this.mUpdateListener;
            if (onClientUpdateListener != null) {
                onClientUpdateListener.onClientChange();
                PlaybackState playbackState = this.mMediaController.getPlaybackState();
                if (playbackState != null) {
                    this.mUpdateListener.onClientPlaybackStateUpdate(playbackState.getState());
                }
                this.mUpdateListener.onClientMetadataUpdate(this.mMediaController.getMetadata());
            }
        }
    }

    public long getEstimatedMediaPosition() {
        PlaybackState playbackState = this.mMediaController;
        if (playbackState != null) {
            playbackState = playbackState.getPlaybackState();
            if (playbackState != null) {
                return playbackState.getPosition();
            }
        }
        return 0;
    }

    public String getClientPackageName() {
        MediaController mediaController = this.mMediaController;
        return mediaController != null ? mediaController.getPackageName() : null;
    }

    public boolean seekTo(long ms) {
        try {
            if (this.mMediaController != null) {
                this.mMediaController.getTransportControls().seekTo(ms);
                return true;
            }
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" seekTo failed: ");
            stringBuilder.append(e);
            Log.w(TAG, stringBuilder.toString());
        }
        return false;
    }

    public void rating(Rating rating) {
        try {
            if (this.mMediaController != null) {
                this.mMediaController.getTransportControls().setRating(rating);
            }
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("RATING_KEY_BY_USER: failed: ");
            stringBuilder.append(e);
            Log.w(TAG, stringBuilder.toString());
        }
    }

    public boolean sendMediaKeyEvent(int action, int code) {
        try {
            if (this.mMediaController != null) {
                KeyEvent keyEvent = new KeyEvent(action, code);
                keyEvent.setSource(4098);
                return this.mMediaController.dispatchMediaButtonEvent(keyEvent);
            }
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Send media key event failed: ");
            stringBuilder.append(e);
            Log.w(TAG, stringBuilder.toString());
        }
        return false;
    }

    public void registerListener(OnClientUpdateListener listener) {
        this.mUpdateListener = listener;
        updateInfoToListener();
    }

    public void unregisterListener() {
        this.mUpdateListener = null;
    }

    public void finish() {
        Log.d(TAG, "finish");
        this.mSessionManager.removeOnActiveSessionsChangedListener(this.mSessionsChangedListener);
        clearMediaController();
    }

    public boolean isMusicActive() {
        PlaybackState playbackState = this.mMediaController;
        boolean z = false;
        if (playbackState != null) {
            playbackState = playbackState.getPlaybackState();
            if (playbackState != null) {
                int state = playbackState.getState();
                if (state == 3 || state == 6) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }
}
