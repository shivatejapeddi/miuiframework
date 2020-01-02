package android.media;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources.NotFoundException;
import android.media.AudioAttributes.Builder;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.VolumeShaper.Configuration;
import android.media.VolumeShaper.Operation;
import android.net.Uri;
import android.os.Binder;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.R;
import java.io.IOException;
import java.util.ArrayList;

public class Ringtone {
    private static final boolean LOGD = true;
    private static final String[] MEDIA_COLUMNS = new String[]{"_id", "title"};
    private static final String MEDIA_SELECTION = "mime_type LIKE 'audio/%' OR mime_type IN ('application/ogg', 'application/x-flac')";
    private static final String TAG = "Ringtone";
    private static final ArrayList<Ringtone> sActiveRingtones = new ArrayList();
    private final boolean mAllowRemote;
    private AudioAttributes mAudioAttributes = new Builder().setUsage(6).setContentType(4).build();
    private final AudioManager mAudioManager;
    private final MyOnCompletionListener mCompletionListener = new MyOnCompletionListener();
    private final Context mContext;
    private boolean mIsLooping = false;
    @UnsupportedAppUsage
    private MediaPlayer mLocalPlayer;
    private final Object mPlaybackSettingsLock = new Object();
    private final IRingtonePlayer mRemotePlayer;
    private final Binder mRemoteToken;
    private String mTitle;
    @UnsupportedAppUsage
    private Uri mUri;
    private float mVolume = 1.0f;
    private VolumeShaper mVolumeShaper;
    private Configuration mVolumeShaperConfig;

    class MyOnCompletionListener implements OnCompletionListener {
        MyOnCompletionListener() {
        }

        public void onCompletion(MediaPlayer mp) {
            synchronized (Ringtone.sActiveRingtones) {
                Ringtone.sActiveRingtones.remove(Ringtone.this);
            }
            mp.setOnCompletionListener(null);
        }
    }

    @UnsupportedAppUsage
    public Ringtone(Context context, boolean allowRemote) {
        this.mContext = context;
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.mAllowRemote = allowRemote;
        Binder binder = null;
        this.mRemotePlayer = allowRemote ? this.mAudioManager.getRingtonePlayer() : null;
        if (allowRemote) {
            binder = new Binder();
        }
        this.mRemoteToken = binder;
    }

    @Deprecated
    public void setStreamType(int streamType) {
        PlayerBase.deprecateStreamTypeForPlayback(streamType, TAG, "setStreamType()");
        setAudioAttributes(new Builder().setInternalLegacyStreamType(streamType).build());
    }

    @Deprecated
    public int getStreamType() {
        return AudioAttributes.toLegacyStreamType(this.mAudioAttributes);
    }

    public void setAudioAttributes(AudioAttributes attributes) throws IllegalArgumentException {
        if (attributes != null) {
            this.mAudioAttributes = attributes;
            setUri(this.mUri, this.mVolumeShaperConfig);
            return;
        }
        throw new IllegalArgumentException("Invalid null AudioAttributes for Ringtone");
    }

    public AudioAttributes getAudioAttributes() {
        return this.mAudioAttributes;
    }

    public void setLooping(boolean looping) {
        synchronized (this.mPlaybackSettingsLock) {
            this.mIsLooping = looping;
            applyPlaybackProperties_sync();
        }
    }

    public boolean isLooping() {
        boolean z;
        synchronized (this.mPlaybackSettingsLock) {
            z = this.mIsLooping;
        }
        return z;
    }

    public void setVolume(float volume) {
        synchronized (this.mPlaybackSettingsLock) {
            if (volume < 0.0f) {
                volume = 0.0f;
            }
            if (volume > 1.0f) {
                volume = 1.0f;
            }
            this.mVolume = volume;
            applyPlaybackProperties_sync();
        }
    }

    public float getVolume() {
        float f;
        synchronized (this.mPlaybackSettingsLock) {
            f = this.mVolume;
        }
        return f;
    }

    private void applyPlaybackProperties_sync() {
        MediaPlayer mediaPlayer = this.mLocalPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(this.mVolume);
            this.mLocalPlayer.setLooping(this.mIsLooping);
            return;
        }
        boolean z = this.mAllowRemote;
        String str = TAG;
        if (z) {
            IRingtonePlayer iRingtonePlayer = this.mRemotePlayer;
            if (iRingtonePlayer != null) {
                try {
                    iRingtonePlayer.setPlaybackProperties(this.mRemoteToken, this.mVolume, this.mIsLooping);
                    return;
                } catch (RemoteException e) {
                    Log.w(str, "Problem setting playback properties: ", e);
                    return;
                }
            }
        }
        Log.w(str, "Neither local nor remote player available when applying playback properties");
    }

    public String getTitle(Context context) {
        String str = this.mTitle;
        if (str != null) {
            return str;
        }
        str = getTitle(context, this.mUri, true, this.mAllowRemote);
        this.mTitle = str;
        return str;
    }

    /* JADX WARNING: Missing block: B:20:0x0068, code skipped:
            if (r10 != null) goto L_0x006a;
     */
    /* JADX WARNING: Missing block: B:21:0x006a, code skipped:
            r10.close();
     */
    /* JADX WARNING: Missing block: B:39:0x0092, code skipped:
            if (r10 == null) goto L_0x006d;
     */
    /* JADX WARNING: Missing block: B:40:0x0095, code skipped:
            if (r7 != null) goto L_0x00a3;
     */
    /* JADX WARNING: Missing block: B:41:0x0097, code skipped:
            r7 = r12.getLastPathSegment();
     */
    public static java.lang.String getTitle(android.content.Context r11, android.net.Uri r12, boolean r13, boolean r14) {
        /*
        r6 = r11.getContentResolver();
        r7 = 0;
        if (r12 == 0) goto L_0x009c;
    L_0x0007:
        r0 = r12.getAuthority();
        r8 = android.content.ContentProvider.getAuthorityWithoutUserId(r0);
        r0 = "settings";
        r0 = r0.equals(r8);
        r9 = 1;
        if (r0 == 0) goto L_0x0036;
    L_0x0019:
        if (r13 == 0) goto L_0x009b;
        r0 = android.media.RingtoneManager.getDefaultType(r12);
        r0 = android.media.RingtoneManager.getActualDefaultRingtoneUri(r11, r0);
        r1 = 0;
        r2 = getTitle(r11, r0, r1, r14);
        r3 = 17041031; // 0x1040687 float:2.4249254E-38 double:8.419388E-317;
        r4 = new java.lang.Object[r9];
        r4[r1] = r2;
        r7 = r11.getString(r3, r4);
        goto L_0x009b;
    L_0x0036:
        r10 = 0;
        r0 = "media";
        r0 = r0.equals(r8);	 Catch:{ SecurityException -> 0x0071 }
        if (r0 == 0) goto L_0x0068;
    L_0x0040:
        if (r14 == 0) goto L_0x0044;
    L_0x0042:
        r0 = 0;
        goto L_0x0047;
    L_0x0044:
        r0 = "mime_type LIKE 'audio/%' OR mime_type IN ('application/ogg', 'application/x-flac')";
    L_0x0047:
        r3 = r0;
        r2 = MEDIA_COLUMNS;	 Catch:{ SecurityException -> 0x0071 }
        r4 = 0;
        r5 = 0;
        r0 = r6;
        r1 = r12;
        r0 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ SecurityException -> 0x0071 }
        r10 = r0;
        if (r10 == 0) goto L_0x0068;
    L_0x0055:
        r0 = r10.getCount();	 Catch:{ SecurityException -> 0x0071 }
        if (r0 != r9) goto L_0x0068;
    L_0x005b:
        r10.moveToFirst();	 Catch:{ SecurityException -> 0x0071 }
        r0 = r10.getString(r9);	 Catch:{ SecurityException -> 0x0071 }
        r10.close();
        r1 = 0;
        return r0;
    L_0x0068:
        if (r10 == 0) goto L_0x006d;
    L_0x006a:
        r10.close();
    L_0x006d:
        r0 = 0;
        goto L_0x0095;
    L_0x006f:
        r0 = move-exception;
        goto L_0x008a;
    L_0x0071:
        r0 = move-exception;
        r1 = 0;
        if (r14 == 0) goto L_0x0082;
    L_0x0075:
        r2 = "audio";
        r2 = r11.getSystemService(r2);	 Catch:{ all -> 0x006f }
        r2 = (android.media.AudioManager) r2;	 Catch:{ all -> 0x006f }
        r3 = r2.getRingtonePlayer();	 Catch:{ all -> 0x006f }
        r1 = r3;
    L_0x0082:
        if (r1 == 0) goto L_0x0092;
    L_0x0084:
        r2 = r1.getTitle(r12);	 Catch:{ RemoteException -> 0x0091 }
        r7 = r2;
        goto L_0x0092;
    L_0x008a:
        if (r10 == 0) goto L_0x008f;
    L_0x008c:
        r10.close();
    L_0x008f:
        r1 = 0;
        throw r0;
    L_0x0091:
        r2 = move-exception;
    L_0x0092:
        if (r10 == 0) goto L_0x006d;
    L_0x0094:
        goto L_0x006a;
    L_0x0095:
        if (r7 != 0) goto L_0x009b;
    L_0x0097:
        r7 = r12.getLastPathSegment();
    L_0x009b:
        goto L_0x00a3;
    L_0x009c:
        r0 = 17041035; // 0x104068b float:2.4249265E-38 double:8.41939E-317;
        r7 = r11.getString(r0);
    L_0x00a3:
        if (r7 != 0) goto L_0x00b0;
    L_0x00a5:
        r0 = 17041036; // 0x104068c float:2.4249268E-38 double:8.4193905E-317;
        r7 = r11.getString(r0);
        if (r7 != 0) goto L_0x00b0;
    L_0x00ae:
        r7 = "";
    L_0x00b0:
        return r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.Ringtone.getTitle(android.content.Context, android.net.Uri, boolean, boolean):java.lang.String");
    }

    @UnsupportedAppUsage
    public void setUri(Uri uri) {
        setUri(uri, null);
    }

    public void setUri(Uri uri, Configuration volumeShaperConfig) {
        this.mVolumeShaperConfig = volumeShaperConfig;
        destroyLocalPlayer();
        this.mUri = uri;
        if (this.mUri != null) {
            this.mLocalPlayer = new MediaPlayer();
            try {
                this.mLocalPlayer.setDataSource(this.mContext, this.mUri);
                this.mLocalPlayer.setAudioAttributes(this.mAudioAttributes);
                synchronized (this.mPlaybackSettingsLock) {
                    applyPlaybackProperties_sync();
                }
                if (this.mVolumeShaperConfig != null) {
                    this.mVolumeShaper = this.mLocalPlayer.createVolumeShaper(this.mVolumeShaperConfig);
                }
                this.mLocalPlayer.prepare();
            } catch (IOException | SecurityException e) {
                destroyLocalPlayer();
                if (!this.mAllowRemote) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Remote playback not allowed: ");
                    stringBuilder.append(e);
                    Log.w(TAG, stringBuilder.toString());
                }
            }
            if (this.mLocalPlayer != null) {
                Log.d(TAG, "Successfully created local player");
            } else {
                Log.d(TAG, "Problem opening; delegating to remote player");
            }
        }
    }

    @UnsupportedAppUsage
    public Uri getUri() {
        return this.mUri;
    }

    public void play() {
        if (this.mLocalPlayer != null) {
            if (this.mAudioManager.getStreamVolume(AudioAttributes.toLegacyStreamType(this.mAudioAttributes)) != 0) {
                startLocalPlayer();
            }
        } else if (this.mAllowRemote && this.mRemotePlayer != null) {
            boolean looping;
            float volume;
            Uri canonicalUri = this.mUri.getCanonicalUri();
            synchronized (this.mPlaybackSettingsLock) {
                looping = this.mIsLooping;
                volume = this.mVolume;
            }
            try {
                this.mRemotePlayer.playWithVolumeShaping(this.mRemoteToken, canonicalUri, this.mAudioAttributes, volume, looping, this.mVolumeShaperConfig);
            } catch (RemoteException e) {
                if (!playFallbackRingtone()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Problem playing ringtone: ");
                    stringBuilder.append(e);
                    Log.w(TAG, stringBuilder.toString());
                }
            }
        } else if (!playFallbackRingtone()) {
            Log.w(TAG, "Neither local nor remote playback available");
        }
    }

    public void stop() {
        if (this.mLocalPlayer != null) {
            destroyLocalPlayer();
        } else if (this.mAllowRemote) {
            IRingtonePlayer iRingtonePlayer = this.mRemotePlayer;
            if (iRingtonePlayer != null) {
                try {
                    iRingtonePlayer.stop(this.mRemoteToken);
                } catch (RemoteException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Problem stopping ringtone: ");
                    stringBuilder.append(e);
                    Log.w(TAG, stringBuilder.toString());
                }
            }
        }
    }

    private void destroyLocalPlayer() {
        MediaPlayer mediaPlayer = this.mLocalPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(null);
            this.mLocalPlayer.reset();
            this.mLocalPlayer.release();
            this.mLocalPlayer = null;
            this.mVolumeShaper = null;
            synchronized (sActiveRingtones) {
                sActiveRingtones.remove(this);
            }
        }
    }

    private void startLocalPlayer() {
        if (this.mLocalPlayer != null) {
            synchronized (sActiveRingtones) {
                sActiveRingtones.add(this);
            }
            this.mLocalPlayer.setOnCompletionListener(this.mCompletionListener);
            this.mLocalPlayer.start();
            VolumeShaper volumeShaper = this.mVolumeShaper;
            if (volumeShaper != null) {
                volumeShaper.apply(Operation.PLAY);
            }
        }
    }

    public boolean isPlaying() {
        MediaPlayer mediaPlayer = this.mLocalPlayer;
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        boolean z = this.mAllowRemote;
        String str = TAG;
        if (z) {
            IRingtonePlayer iRingtonePlayer = this.mRemotePlayer;
            if (iRingtonePlayer != null) {
                try {
                    return iRingtonePlayer.isPlaying(this.mRemoteToken);
                } catch (RemoteException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Problem checking ringtone: ");
                    stringBuilder.append(e);
                    Log.w(str, stringBuilder.toString());
                    return false;
                }
            }
        }
        Log.w(str, "Neither local nor remote playback available");
        return false;
    }

    private boolean playFallbackRingtone() {
        if (this.mAudioManager.getStreamVolume(AudioAttributes.toLegacyStreamType(this.mAudioAttributes)) != 0) {
            int ringtoneType = RingtoneManager.getDefaultType(this.mUri);
            if (ringtoneType == -1 || RingtoneManager.getActualDefaultRingtoneUri(this.mContext, ringtoneType) != null) {
                try {
                    AssetFileDescriptor afd = this.mContext.getResources().openRawResourceFd(R.raw.fallbackring);
                    if (afd != null) {
                        this.mLocalPlayer = new MediaPlayer();
                        if (afd.getDeclaredLength() < 0) {
                            this.mLocalPlayer.setDataSource(afd.getFileDescriptor());
                        } else {
                            this.mLocalPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
                        }
                        this.mLocalPlayer.setAudioAttributes(this.mAudioAttributes);
                        synchronized (this.mPlaybackSettingsLock) {
                            applyPlaybackProperties_sync();
                        }
                        if (this.mVolumeShaperConfig != null) {
                            this.mVolumeShaper = this.mLocalPlayer.createVolumeShaper(this.mVolumeShaperConfig);
                        }
                        this.mLocalPlayer.prepare();
                        startLocalPlayer();
                        afd.close();
                        return true;
                    }
                    Log.e(TAG, "Could not load fallback ringtone");
                } catch (IOException e) {
                    destroyLocalPlayer();
                    Log.e(TAG, "Failed to open fallback ringtone");
                } catch (NotFoundException e2) {
                    Log.e(TAG, "Fallback ringtone does not exist");
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("not playing fallback for ");
                stringBuilder.append(this.mUri);
                Log.w(TAG, stringBuilder.toString());
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public void setTitle(String title) {
        this.mTitle = title;
    }

    /* Access modifiers changed, original: protected */
    public void finalize() {
        MediaPlayer mediaPlayer = this.mLocalPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
