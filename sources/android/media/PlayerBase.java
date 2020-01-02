package android.media;

import android.media.VolumeShaper.Configuration;
import android.media.VolumeShaper.Operation;
import android.media.VolumeShaper.State;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.IAppOpsCallback;
import com.android.internal.app.IAppOpsCallback.Stub;
import com.android.internal.app.IAppOpsService;
import java.lang.ref.WeakReference;
import java.util.Objects;

public abstract class PlayerBase {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_APP_OPS = false;
    private static final String TAG = "PlayerBase";
    private static final boolean USE_AUDIOFLINGER_MUTING_FOR_OP = true;
    private static IAudioService sService;
    private IAppOpsService mAppOps;
    private IAppOpsCallback mAppOpsCallback;
    protected AudioAttributes mAttributes;
    protected float mAuxEffectSendLevel = 0.0f;
    @GuardedBy({"mLock"})
    private boolean mHasAppOpsPlayAudio = true;
    private final int mImplType;
    protected float mLeftVolume = 1.0f;
    private final Object mLock = new Object();
    @GuardedBy({"mLock"})
    private float mPanMultiplierL = 1.0f;
    @GuardedBy({"mLock"})
    private float mPanMultiplierR = 1.0f;
    private int mPlayerIId = -1;
    protected float mRightVolume = 1.0f;
    @GuardedBy({"mLock"})
    private int mStartDelayMs = 0;
    @GuardedBy({"mLock"})
    private int mState;
    @GuardedBy({"mLock"})
    private float mVolMultiplier = 1.0f;

    private static class IAppOpsCallbackWrapper extends Stub {
        private final WeakReference<PlayerBase> mWeakPB;

        public IAppOpsCallbackWrapper(PlayerBase pb) {
            this.mWeakPB = new WeakReference(pb);
        }

        public void opChanged(int op, int uid, String packageName) {
            if (op == 28) {
                PlayerBase pb = (PlayerBase) this.mWeakPB.get();
                if (pb != null) {
                    pb.updateAppOpsPlayAudio();
                }
            }
        }
    }

    private static class IPlayerWrapper extends IPlayer.Stub {
        private final WeakReference<PlayerBase> mWeakPB;

        public IPlayerWrapper(PlayerBase pb) {
            this.mWeakPB = new WeakReference(pb);
        }

        public void start() {
            PlayerBase pb = (PlayerBase) this.mWeakPB.get();
            if (pb != null) {
                pb.playerStart();
            }
        }

        public void pause() {
            PlayerBase pb = (PlayerBase) this.mWeakPB.get();
            if (pb != null) {
                pb.playerPause();
            }
        }

        public void stop() {
            PlayerBase pb = (PlayerBase) this.mWeakPB.get();
            if (pb != null) {
                pb.playerStop();
            }
        }

        public void setVolume(float vol) {
            PlayerBase pb = (PlayerBase) this.mWeakPB.get();
            if (pb != null) {
                pb.setVolumeMultiplier(vol);
            }
        }

        public void setPan(float pan) {
            PlayerBase pb = (PlayerBase) this.mWeakPB.get();
            if (pb != null) {
                pb.baseSetPan(pan);
            }
        }

        public void setStartDelayMs(int delayMs) {
            PlayerBase pb = (PlayerBase) this.mWeakPB.get();
            if (pb != null) {
                pb.baseSetStartDelayMs(delayMs);
            }
        }

        public void applyVolumeShaper(Configuration configuration, Operation operation) {
            PlayerBase pb = (PlayerBase) this.mWeakPB.get();
            if (pb != null) {
                pb.playerApplyVolumeShaper(configuration, operation);
            }
        }
    }

    public static class PlayerIdCard implements Parcelable {
        public static final int AUDIO_ATTRIBUTES_DEFINED = 1;
        public static final int AUDIO_ATTRIBUTES_NONE = 0;
        public static final Creator<PlayerIdCard> CREATOR = new Creator<PlayerIdCard>() {
            public PlayerIdCard createFromParcel(Parcel p) {
                return new PlayerIdCard(p);
            }

            public PlayerIdCard[] newArray(int size) {
                return new PlayerIdCard[size];
            }
        };
        public final AudioAttributes mAttributes;
        public final IPlayer mIPlayer;
        public final int mPlayerType;

        PlayerIdCard(int type, AudioAttributes attr, IPlayer iplayer) {
            this.mPlayerType = type;
            this.mAttributes = attr;
            this.mIPlayer = iplayer;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{Integer.valueOf(this.mPlayerType)});
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mPlayerType);
            this.mAttributes.writeToParcel(dest, 0);
            IPlayer iPlayer = this.mIPlayer;
            dest.writeStrongBinder(iPlayer == null ? null : iPlayer.asBinder());
        }

        private PlayerIdCard(Parcel in) {
            this.mPlayerType = in.readInt();
            this.mAttributes = (AudioAttributes) AudioAttributes.CREATOR.createFromParcel(in);
            IBinder b = in.readStrongBinder();
            this.mIPlayer = b == null ? null : IPlayer.Stub.asInterface(b);
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (o == null || !(o instanceof PlayerIdCard)) {
                return false;
            }
            PlayerIdCard that = (PlayerIdCard) o;
            if (!(this.mPlayerType == that.mPlayerType && this.mAttributes.equals(that.mAttributes))) {
                z = false;
            }
            return z;
        }
    }

    public abstract int playerApplyVolumeShaper(Configuration configuration, Operation operation);

    public abstract State playerGetVolumeShaperState(int i);

    public abstract void playerPause();

    public abstract int playerSetAuxEffectSendLevel(boolean z, float f);

    public abstract void playerSetVolume(boolean z, float f, float f2);

    public abstract void playerStart();

    public abstract void playerStop();

    PlayerBase(AudioAttributes attr, int implType) {
        if (attr != null) {
            this.mAttributes = attr;
            this.mImplType = implType;
            this.mState = 1;
            return;
        }
        throw new IllegalArgumentException("Illegal null AudioAttributes");
    }

    /* Access modifiers changed, original: protected */
    public void baseRegisterPlayer() {
        try {
            this.mPlayerIId = getService().trackPlayer(new PlayerIdCard(this.mImplType, this.mAttributes, new IPlayerWrapper(this)));
        } catch (RemoteException e) {
            Log.e(TAG, "Error talking to audio service, player will not be tracked", e);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void baseUpdateAudioAttributes(AudioAttributes attr) {
        if (attr != null) {
            try {
                getService().playerAttributes(this.mPlayerIId, attr);
            } catch (RemoteException e) {
                Log.e(TAG, "Error talking to audio service, STARTED state will not be tracked", e);
            }
            synchronized (this.mLock) {
                boolean attributesChanged = this.mAttributes != attr;
                this.mAttributes = attr;
                updateAppOpsPlayAudio_sync(attributesChanged);
            }
            return;
        }
        throw new IllegalArgumentException("Illegal null AudioAttributes");
    }

    private void updateState(int state) {
        int piid;
        synchronized (this.mLock) {
            this.mState = state;
            piid = this.mPlayerIId;
        }
        try {
            getService().playerEvent(piid, state);
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error talking to audio service, ");
            stringBuilder.append(AudioPlaybackConfiguration.toLogFriendlyPlayerState(state));
            stringBuilder.append(" state will not be tracked for piid=");
            stringBuilder.append(piid);
            Log.e(TAG, stringBuilder.toString(), e);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void baseStart() {
        updateState(2);
        synchronized (this.mLock) {
            if (isRestricted_sync()) {
                playerSetVolume(true, 0.0f, 0.0f);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void baseSetStartDelayMs(int delayMs) {
        synchronized (this.mLock) {
            this.mStartDelayMs = Math.max(delayMs, 0);
        }
    }

    /* Access modifiers changed, original: protected */
    public int getStartDelayMs() {
        int i;
        synchronized (this.mLock) {
            i = this.mStartDelayMs;
        }
        return i;
    }

    /* Access modifiers changed, original: 0000 */
    public void basePause() {
        updateState(3);
    }

    /* Access modifiers changed, original: 0000 */
    public void baseStop() {
        updateState(4);
    }

    /* Access modifiers changed, original: 0000 */
    public void baseSetPan(float pan) {
        float p = Math.min(Math.max(-1.0f, pan), 1.0f);
        synchronized (this.mLock) {
            if (p >= 0.0f) {
                this.mPanMultiplierL = 1.0f - p;
                this.mPanMultiplierR = 1.0f;
            } else {
                this.mPanMultiplierL = 1.0f;
                this.mPanMultiplierR = 1.0f + p;
            }
        }
        updatePlayerVolume();
    }

    private void updatePlayerVolume() {
        float finalLeftVol;
        float finalRightVol;
        boolean isRestricted;
        synchronized (this.mLock) {
            finalLeftVol = (this.mVolMultiplier * this.mLeftVolume) * this.mPanMultiplierL;
            finalRightVol = (this.mVolMultiplier * this.mRightVolume) * this.mPanMultiplierR;
            isRestricted = isRestricted_sync();
        }
        playerSetVolume(isRestricted, finalLeftVol, finalRightVol);
    }

    /* Access modifiers changed, original: 0000 */
    public void setVolumeMultiplier(float vol) {
        synchronized (this.mLock) {
            this.mVolMultiplier = vol;
        }
        updatePlayerVolume();
    }

    /* Access modifiers changed, original: 0000 */
    public void baseSetVolume(float leftVolume, float rightVolume) {
        synchronized (this.mLock) {
            this.mLeftVolume = leftVolume;
            this.mRightVolume = rightVolume;
        }
        updatePlayerVolume();
    }

    /* Access modifiers changed, original: 0000 */
    public int baseSetAuxEffectSendLevel(float level) {
        synchronized (this.mLock) {
            this.mAuxEffectSendLevel = level;
            if (isRestricted_sync()) {
                return 0;
            }
            return playerSetAuxEffectSendLevel(false, level);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void baseRelease() {
        boolean releasePlayer = false;
        synchronized (this.mLock) {
            if (this.mState != 0) {
                releasePlayer = true;
                this.mState = 0;
            }
        }
        if (releasePlayer) {
            try {
                getService().releasePlayer(this.mPlayerIId);
            } catch (RemoteException e) {
                Log.e(TAG, "Error talking to audio service, the player will still be tracked", e);
            }
        }
        try {
            if (this.mAppOps != null) {
                this.mAppOps.stopWatchingMode(this.mAppOpsCallback);
            }
        } catch (Exception e2) {
        }
    }

    private void updateAppOpsPlayAudio() {
        synchronized (this.mLock) {
            updateAppOpsPlayAudio_sync(false);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void updateAppOpsPlayAudio_sync(boolean attributesChanged) {
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isRestricted_sync() {
        return false;
    }

    private static IAudioService getService() {
        IAudioService iAudioService = sService;
        if (iAudioService != null) {
            return iAudioService;
        }
        sService = IAudioService.Stub.asInterface(ServiceManager.getService("audio"));
        return sService;
    }

    public void setStartDelayMs(int delayMs) {
        baseSetStartDelayMs(delayMs);
    }

    public static void deprecateStreamTypeForPlayback(int streamType, String className, String opName) throws IllegalArgumentException {
        if (streamType != 10) {
            Log.w(className, "Use of stream types is deprecated for operations other than volume control");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("See the documentation of ");
            stringBuilder.append(opName);
            stringBuilder.append(" for what to use instead with android.media.AudioAttributes to qualify your playback use case");
            Log.w(className, stringBuilder.toString());
            return;
        }
        throw new IllegalArgumentException("Use of STREAM_ACCESSIBILITY is reserved for volume control");
    }
}
