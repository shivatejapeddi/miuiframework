package android.media;

import android.media.SoundPool.Builder;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;

public class MediaActionSound {
    public static final int FOCUS_COMPLETE = 1;
    private static final int NUM_MEDIA_SOUND_STREAMS = 1;
    public static final int SHUTTER_CLICK = 0;
    private static final String[] SOUND_DIRS = new String[]{"/product/media/audio/ui/", "/system/media/audio/ui/"};
    private static final String[] SOUND_FILES = new String[]{"camera_click.ogg", "camera_focus.ogg", "VideoRecord.ogg", "VideoStop.ogg"};
    public static final int START_VIDEO_RECORDING = 2;
    private static final int STATE_LOADED = 3;
    private static final int STATE_LOADING = 1;
    private static final int STATE_LOADING_PLAY_REQUESTED = 2;
    private static final int STATE_NOT_LOADED = 0;
    public static final int STOP_VIDEO_RECORDING = 3;
    private static final String TAG = "MediaActionSound";
    private OnLoadCompleteListener mLoadCompleteListener = new OnLoadCompleteListener() {
        /* JADX WARNING: Missing block: B:20:0x0075, code skipped:
            if (r0 == 0) goto L_0x0088;
     */
        /* JADX WARNING: Missing block: B:21:0x0077, code skipped:
            r13.play(r0, 1.0f, 1.0f, 0, 0, 1.0f);
     */
        public void onLoadComplete(android.media.SoundPool r13, int r14, int r15) {
            /*
            r12 = this;
            r0 = android.media.MediaActionSound.this;
            r0 = r0.mSounds;
            r1 = r0.length;
            r2 = 0;
            r3 = r2;
        L_0x0009:
            if (r3 >= r1) goto L_0x0088;
        L_0x000b:
            r4 = r0[r3];
            r5 = r4.id;
            if (r5 == r14) goto L_0x0015;
            r3 = r3 + 1;
            goto L_0x0009;
        L_0x0015:
            r0 = 0;
            monitor-enter(r4);
            if (r15 == 0) goto L_0x003f;
        L_0x0019:
            r4.state = r2;	 Catch:{ all -> 0x0085 }
            r4.id = r2;	 Catch:{ all -> 0x0085 }
            r1 = "MediaActionSound";
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0085 }
            r2.<init>();	 Catch:{ all -> 0x0085 }
            r3 = "OnLoadCompleteListener() error: ";
            r2.append(r3);	 Catch:{ all -> 0x0085 }
            r2.append(r15);	 Catch:{ all -> 0x0085 }
            r3 = " loading sound: ";
            r2.append(r3);	 Catch:{ all -> 0x0085 }
            r3 = r4.name;	 Catch:{ all -> 0x0085 }
            r2.append(r3);	 Catch:{ all -> 0x0085 }
            r2 = r2.toString();	 Catch:{ all -> 0x0085 }
            android.util.Log.e(r1, r2);	 Catch:{ all -> 0x0085 }
            monitor-exit(r4);	 Catch:{ all -> 0x0085 }
            return;
        L_0x003f:
            r1 = r4.state;	 Catch:{ all -> 0x0085 }
            r2 = 3;
            r3 = 1;
            if (r1 == r3) goto L_0x0071;
        L_0x0045:
            r3 = 2;
            if (r1 == r3) goto L_0x006b;
        L_0x0048:
            r1 = "MediaActionSound";
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0085 }
            r2.<init>();	 Catch:{ all -> 0x0085 }
            r3 = "OnLoadCompleteListener() called in wrong state: ";
            r2.append(r3);	 Catch:{ all -> 0x0085 }
            r3 = r4.state;	 Catch:{ all -> 0x0085 }
            r2.append(r3);	 Catch:{ all -> 0x0085 }
            r3 = " for sound: ";
            r2.append(r3);	 Catch:{ all -> 0x0085 }
            r3 = r4.name;	 Catch:{ all -> 0x0085 }
            r2.append(r3);	 Catch:{ all -> 0x0085 }
            r2 = r2.toString();	 Catch:{ all -> 0x0085 }
            android.util.Log.e(r1, r2);	 Catch:{ all -> 0x0085 }
            goto L_0x0074;
        L_0x006b:
            r1 = r4.id;	 Catch:{ all -> 0x0085 }
            r0 = r1;
            r4.state = r2;	 Catch:{ all -> 0x0085 }
            goto L_0x0074;
        L_0x0071:
            r4.state = r2;	 Catch:{ all -> 0x0085 }
        L_0x0074:
            monitor-exit(r4);	 Catch:{ all -> 0x0085 }
            if (r0 == 0) goto L_0x0088;
        L_0x0077:
            r7 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r9 = 0;
            r10 = 0;
            r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r5 = r13;
            r6 = r0;
            r5.play(r6, r7, r8, r9, r10, r11);
            goto L_0x0088;
        L_0x0085:
            r1 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0085 }
            throw r1;
        L_0x0088:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.MediaActionSound$AnonymousClass1.onLoadComplete(android.media.SoundPool, int, int):void");
        }
    };
    private SoundPool mSoundPool = new Builder().setMaxStreams(1).setAudioAttributes(new AudioAttributes.Builder().setUsage(13).setFlags(1).setContentType(4).build()).build();
    private SoundState[] mSounds;

    private class SoundState {
        public int id = 0;
        public final int name;
        public int state = 0;

        public SoundState(int name) {
            this.name = name;
        }
    }

    public MediaActionSound() {
        this.mSoundPool.setOnLoadCompleteListener(this.mLoadCompleteListener);
        this.mSounds = new SoundState[SOUND_FILES.length];
        int i = 0;
        while (true) {
            SoundState[] soundStateArr = this.mSounds;
            if (i < soundStateArr.length) {
                soundStateArr[i] = new SoundState(i);
                i++;
            } else {
                return;
            }
        }
    }

    private int loadSound(SoundState sound) {
        String soundFileName = SOUND_FILES[sound.name];
        for (String soundDir : SOUND_DIRS) {
            int id = this.mSoundPool;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(soundDir);
            stringBuilder.append(soundFileName);
            id = id.load(stringBuilder.toString(), 1);
            if (id > 0) {
                sound.state = 1;
                sound.id = id;
                return id;
            }
        }
        return 0;
    }

    public void load(int soundName) {
        if (soundName < 0 || soundName >= SOUND_FILES.length) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown sound requested: ");
            stringBuilder.append(soundName);
            throw new RuntimeException(stringBuilder.toString());
        }
        SoundState sound = this.mSounds[soundName];
        synchronized (sound) {
            String str;
            StringBuilder stringBuilder2;
            if (sound.state != 0) {
                str = TAG;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("load() called in wrong state: ");
                stringBuilder2.append(sound);
                stringBuilder2.append(" for sound: ");
                stringBuilder2.append(soundName);
                Log.e(str, stringBuilder2.toString());
            } else if (loadSound(sound) <= 0) {
                str = TAG;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("load() error loading sound: ");
                stringBuilder2.append(soundName);
                Log.e(str, stringBuilder2.toString());
            }
        }
    }

    public void play(int soundName) {
        if (soundName < 0 || soundName >= SOUND_FILES.length) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown sound requested: ");
            stringBuilder.append(soundName);
            throw new RuntimeException(stringBuilder.toString());
        }
        SoundState sound = this.mSounds[soundName];
        synchronized (sound) {
            int i = sound.state;
            String str;
            StringBuilder stringBuilder2;
            if (i == 0) {
                loadSound(sound);
                if (loadSound(sound) <= 0) {
                    str = TAG;
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("play() error loading sound: ");
                    stringBuilder2.append(soundName);
                    Log.e(str, stringBuilder2.toString());
                }
            } else if (i != 1) {
                if (i != 3) {
                    str = TAG;
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("play() called in wrong state: ");
                    stringBuilder2.append(sound.state);
                    stringBuilder2.append(" for sound: ");
                    stringBuilder2.append(soundName);
                    Log.e(str, stringBuilder2.toString());
                } else {
                    this.mSoundPool.play(sound.id, 1.0f, 1.0f, 0, 0, 1.0f);
                }
            }
            sound.state = 2;
        }
    }

    public void release() {
        if (this.mSoundPool != null) {
            for (SoundState sound : this.mSounds) {
                synchronized (sound) {
                    sound.state = 0;
                    sound.id = 0;
                }
            }
            this.mSoundPool.release();
            this.mSoundPool = null;
        }
    }
}
