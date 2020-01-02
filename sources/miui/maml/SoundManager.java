package miui.maml;

import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Handler;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SoundManager implements OnLoadCompleteListener {
    private static final String ADVANCE = "advance/";
    private static final String LOCKSCREEN_AUDIO = "lockscreen_audio/";
    private static final String LOG_TAG = "MamlSoundManager";
    private static final int MAX_FILE_SIZE = 524288;
    private static final int MAX_STREAMS = 8;
    private Handler mHandler;
    private Object mInitSignal = new Object();
    private boolean mInitialized;
    private HashMap<Integer, SoundOptions> mPendingSoundMap = new HashMap();
    private ArrayList<Integer> mPlayingSoundMap = new ArrayList();
    private ResourceManager mResourceManager;
    private SoundPool mSoundPool;
    private HashMap<String, Integer> mSoundPoolMap = new HashMap();

    /* renamed from: miui.maml.SoundManager$2 */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$miui$maml$SoundManager$Command = new int[Command.values().length];

        static {
            try {
                $SwitchMap$miui$maml$SoundManager$Command[Command.Play.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$miui$maml$SoundManager$Command[Command.Pause.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$miui$maml$SoundManager$Command[Command.Resume.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$miui$maml$SoundManager$Command[Command.Stop.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public enum Command {
        Play,
        Pause,
        Resume,
        Stop;

        public static Command parse(String c) {
            if ("pause".equals(c)) {
                return Pause;
            }
            if ("resume".equals(c)) {
                return Resume;
            }
            if ("stop".equals(c)) {
                return Stop;
            }
            return Play;
        }
    }

    public static class SoundOptions {
        public boolean mKeepCur;
        public boolean mLoop;
        public float mVolume;

        public SoundOptions(boolean keepCur, boolean loop, float volume) {
            this.mKeepCur = keepCur;
            this.mLoop = loop;
            if (volume < 0.0f) {
                this.mVolume = 0.0f;
            } else if (volume > 1.0f) {
                this.mVolume = 1.0f;
            } else {
                this.mVolume = volume;
            }
        }
    }

    public SoundManager(ScreenContext context) {
        this.mResourceManager = context.mResourceManager;
        this.mHandler = context.getHandler();
    }

    private void init() {
        if (!this.mInitialized) {
            if (Thread.currentThread().getId() == this.mHandler.getLooper().getThread().getId()) {
                this.mSoundPool = new SoundPool(8, 3, 100);
                this.mSoundPool.setOnLoadCompleteListener(this);
                this.mInitialized = true;
            } else {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        SoundManager.this.mSoundPool = new SoundPool(8, 3, 100);
                        SoundManager.this.mSoundPool.setOnLoadCompleteListener(SoundManager.this);
                        synchronized (SoundManager.this.mInitSignal) {
                            SoundManager.this.mInitialized = true;
                            SoundManager.this.mInitSignal.notify();
                        }
                    }
                });
                synchronized (this.mInitSignal) {
                    while (!this.mInitialized) {
                        try {
                            this.mInitSignal.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }
    }

    /* JADX WARNING: Missing block: B:31:0x0048, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:33:0x004a, code skipped:
            return;
     */
    public synchronized void playSound(int r4, miui.maml.SoundManager.Command r5) {
        /*
        r3 = this;
        monitor-enter(r3);
        r0 = r3.mInitialized;	 Catch:{ all -> 0x004b }
        if (r0 != 0) goto L_0x0008;
    L_0x0005:
        r3.init();	 Catch:{ all -> 0x004b }
    L_0x0008:
        r0 = r3.mSoundPool;	 Catch:{ all -> 0x004b }
        if (r0 == 0) goto L_0x0049;
    L_0x000c:
        if (r4 > 0) goto L_0x000f;
    L_0x000e:
        goto L_0x0049;
    L_0x000f:
        r0 = miui.maml.SoundManager.AnonymousClass2.$SwitchMap$miui$maml$SoundManager$Command;	 Catch:{ all -> 0x004b }
        r1 = r5.ordinal();	 Catch:{ all -> 0x004b }
        r0 = r0[r1];	 Catch:{ all -> 0x004b }
        r1 = 1;
        if (r0 == r1) goto L_0x0046;
    L_0x001a:
        r1 = 2;
        if (r0 == r1) goto L_0x0040;
    L_0x001d:
        r1 = 3;
        if (r0 == r1) goto L_0x003a;
    L_0x0020:
        r1 = 4;
        if (r0 == r1) goto L_0x0024;
    L_0x0023:
        goto L_0x0047;
    L_0x0024:
        r0 = r3.mSoundPool;	 Catch:{ all -> 0x004b }
        r0.stop(r4);	 Catch:{ all -> 0x004b }
        r0 = r3.mPlayingSoundMap;	 Catch:{ all -> 0x004b }
        monitor-enter(r0);	 Catch:{ all -> 0x004b }
        r1 = r3.mPlayingSoundMap;	 Catch:{ all -> 0x0037 }
        r2 = java.lang.Integer.valueOf(r4);	 Catch:{ all -> 0x0037 }
        r1.remove(r2);	 Catch:{ all -> 0x0037 }
        monitor-exit(r0);	 Catch:{ all -> 0x0037 }
        goto L_0x0047;
    L_0x0037:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0037 }
        throw r1;	 Catch:{ all -> 0x004b }
    L_0x003a:
        r0 = r3.mSoundPool;	 Catch:{ all -> 0x004b }
        r0.resume(r4);	 Catch:{ all -> 0x004b }
        goto L_0x0047;
    L_0x0040:
        r0 = r3.mSoundPool;	 Catch:{ all -> 0x004b }
        r0.pause(r4);	 Catch:{ all -> 0x004b }
        goto L_0x0047;
    L_0x0047:
        monitor-exit(r3);
        return;
    L_0x0049:
        monitor-exit(r3);
        return;
    L_0x004b:
        r4 = move-exception;
        monitor-exit(r3);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.maml.SoundManager.playSound(int, miui.maml.SoundManager$Command):void");
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:26:0x0064, B:52:0x012f] */
    /* JADX WARNING: Missing block: B:28:0x0084, code skipped:
            r3 = move-exception;
     */
    /* JADX WARNING: Missing block: B:30:?, code skipped:
            r4 = LOG_TAG;
            r5 = new java.lang.StringBuilder();
            r5.append("fail to load sound. ");
            r5.append(r3.toString());
            android.util.Log.e(r4, r5.toString());
     */
    /* JADX WARNING: Missing block: B:60:?, code skipped:
            miui.util.IOUtils.closeQuietly(null);
     */
    public synchronized int playSound(java.lang.String r12, miui.maml.SoundManager.SoundOptions r13) {
        /*
        r11 = this;
        monitor-enter(r11);
        r0 = r11.mInitialized;	 Catch:{ all -> 0x0150 }
        if (r0 != 0) goto L_0x0008;
    L_0x0005:
        r11.init();	 Catch:{ all -> 0x0150 }
    L_0x0008:
        r0 = r11.mSoundPool;	 Catch:{ all -> 0x0150 }
        r1 = 0;
        if (r0 != 0) goto L_0x000f;
    L_0x000d:
        monitor-exit(r11);
        return r1;
    L_0x000f:
        r0 = r11.mSoundPoolMap;	 Catch:{ all -> 0x0150 }
        r0 = r0.get(r12);	 Catch:{ all -> 0x0150 }
        r0 = (java.lang.Integer) r0;	 Catch:{ all -> 0x0150 }
        if (r0 != 0) goto L_0x0146;
    L_0x0019:
        r2 = android.os.Build.VERSION.SDK_INT;	 Catch:{ all -> 0x0150 }
        r3 = 26;
        r4 = 1;
        r5 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        r6 = 2;
        if (r2 >= r3) goto L_0x00a1;
    L_0x0023:
        r2 = r11.mResourceManager;	 Catch:{ all -> 0x0150 }
        r2 = r2.getFile(r12);	 Catch:{ all -> 0x0150 }
        if (r2 != 0) goto L_0x0044;
    L_0x002b:
        r3 = "MamlSoundManager";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0150 }
        r4.<init>();	 Catch:{ all -> 0x0150 }
        r5 = "the sound does not exist: ";
        r4.append(r5);	 Catch:{ all -> 0x0150 }
        r4.append(r12);	 Catch:{ all -> 0x0150 }
        r4 = r4.toString();	 Catch:{ all -> 0x0150 }
        android.util.Log.e(r3, r4);	 Catch:{ all -> 0x0150 }
        monitor-exit(r11);
        return r1;
    L_0x0044:
        r3 = r2.length();	 Catch:{ all -> 0x0150 }
        r7 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
        if (r3 <= r7) goto L_0x0064;
    L_0x004c:
        r3 = "MamlSoundManager";
        r7 = "the sound file is larger than %d KB: %s";
        r6 = new java.lang.Object[r6];	 Catch:{ all -> 0x0150 }
        r5 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x0150 }
        r6[r1] = r5;	 Catch:{ all -> 0x0150 }
        r6[r4] = r12;	 Catch:{ all -> 0x0150 }
        r4 = java.lang.String.format(r7, r6);	 Catch:{ all -> 0x0150 }
        android.util.Log.w(r3, r4);	 Catch:{ all -> 0x0150 }
        monitor-exit(r11);
        return r1;
    L_0x0064:
        r3 = r11.mSoundPool;	 Catch:{ IOException -> 0x0084 }
        r4 = r2.getFileDescriptor();	 Catch:{ IOException -> 0x0084 }
        r5 = 0;
        r7 = r2.length();	 Catch:{ IOException -> 0x0084 }
        r7 = (long) r7;	 Catch:{ IOException -> 0x0084 }
        r9 = 1;
        r3 = r3.load(r4, r5, r7, r9);	 Catch:{ IOException -> 0x0084 }
        r3 = java.lang.Integer.valueOf(r3);	 Catch:{ IOException -> 0x0084 }
        r0 = r3;
        r3 = r11.mSoundPoolMap;	 Catch:{ IOException -> 0x0084 }
        r3.put(r12, r0);	 Catch:{ IOException -> 0x0084 }
        r2.close();	 Catch:{ IOException -> 0x0084 }
        goto L_0x009f;
    L_0x0084:
        r3 = move-exception;
        r4 = "MamlSoundManager";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0150 }
        r5.<init>();	 Catch:{ all -> 0x0150 }
        r6 = "fail to load sound. ";
        r5.append(r6);	 Catch:{ all -> 0x0150 }
        r6 = r3.toString();	 Catch:{ all -> 0x0150 }
        r5.append(r6);	 Catch:{ all -> 0x0150 }
        r5 = r5.toString();	 Catch:{ all -> 0x0150 }
        android.util.Log.e(r4, r5);	 Catch:{ all -> 0x0150 }
    L_0x009f:
        goto L_0x013b;
    L_0x00a1:
        r2 = new java.io.File;	 Catch:{ all -> 0x0150 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0150 }
        r3.<init>();	 Catch:{ all -> 0x0150 }
        r7 = miui.content.res.ThemeResources.THEME_MAGIC_PATH;	 Catch:{ all -> 0x0150 }
        r3.append(r7);	 Catch:{ all -> 0x0150 }
        r7 = "lockscreen_audio/";
        r3.append(r7);	 Catch:{ all -> 0x0150 }
        r7 = "advance/";
        r3.append(r7);	 Catch:{ all -> 0x0150 }
        r3.append(r12);	 Catch:{ all -> 0x0150 }
        r3 = r3.toString();	 Catch:{ all -> 0x0150 }
        r2.<init>(r3);	 Catch:{ all -> 0x0150 }
        r3 = r2.exists();	 Catch:{ all -> 0x0150 }
        if (r3 != 0) goto L_0x00e0;
    L_0x00c7:
        r3 = "MamlSoundManager";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0150 }
        r4.<init>();	 Catch:{ all -> 0x0150 }
        r5 = "the sound does not exist: ";
        r4.append(r5);	 Catch:{ all -> 0x0150 }
        r4.append(r12);	 Catch:{ all -> 0x0150 }
        r4 = r4.toString();	 Catch:{ all -> 0x0150 }
        android.util.Log.e(r3, r4);	 Catch:{ all -> 0x0150 }
        monitor-exit(r11);
        return r1;
    L_0x00e0:
        r7 = r2.length();	 Catch:{ all -> 0x0150 }
        r9 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
        r3 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r3 <= 0) goto L_0x0103;
    L_0x00eb:
        r3 = "MamlSoundManager";
        r7 = "the sound file is larger than %d KB: %s";
        r6 = new java.lang.Object[r6];	 Catch:{ all -> 0x0150 }
        r5 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x0150 }
        r6[r1] = r5;	 Catch:{ all -> 0x0150 }
        r6[r4] = r12;	 Catch:{ all -> 0x0150 }
        r4 = java.lang.String.format(r7, r6);	 Catch:{ all -> 0x0150 }
        android.util.Log.w(r3, r4);	 Catch:{ all -> 0x0150 }
        monitor-exit(r11);
        return r1;
    L_0x0103:
        r3 = 0;
        r4 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r4 = android.os.ParcelFileDescriptor.open(r2, r4);	 Catch:{ IOException -> 0x012e }
        r3 = r4;
        if (r3 == 0) goto L_0x0128;
    L_0x010d:
        r4 = r11.mSoundPool;	 Catch:{ IOException -> 0x012e }
        r5 = r3.getFileDescriptor();	 Catch:{ IOException -> 0x012e }
        r6 = 0;
        r8 = r2.length();	 Catch:{ IOException -> 0x012e }
        r10 = 1;
        r4 = r4.load(r5, r6, r8, r10);	 Catch:{ IOException -> 0x012e }
        r4 = java.lang.Integer.valueOf(r4);	 Catch:{ IOException -> 0x012e }
        r0 = r4;
        r4 = r11.mSoundPoolMap;	 Catch:{ IOException -> 0x012e }
        r4.put(r12, r0);	 Catch:{ IOException -> 0x012e }
    L_0x0128:
        miui.util.IOUtils.closeQuietly(r3);	 Catch:{ all -> 0x0150 }
    L_0x012b:
        goto L_0x013b;
    L_0x012c:
        r1 = move-exception;
        goto L_0x0142;
    L_0x012e:
        r4 = move-exception;
        r5 = "MamlSoundManager";
        r6 = "fail to load sound. ";
        android.util.Log.e(r5, r6, r4);	 Catch:{ all -> 0x012c }
        miui.util.IOUtils.closeQuietly(r3);	 Catch:{ all -> 0x0150 }
        goto L_0x012b;
    L_0x013b:
        r2 = r11.mPendingSoundMap;	 Catch:{ all -> 0x0150 }
        r2.put(r0, r13);	 Catch:{ all -> 0x0150 }
        monitor-exit(r11);
        return r1;
    L_0x0142:
        miui.util.IOUtils.closeQuietly(r3);	 Catch:{ all -> 0x0150 }
        throw r1;	 Catch:{ all -> 0x0150 }
    L_0x0146:
        r1 = r0.intValue();	 Catch:{ all -> 0x0150 }
        r1 = r11.playSoundImp(r1, r13);	 Catch:{ all -> 0x0150 }
        monitor-exit(r11);
        return r1;
    L_0x0150:
        r12 = move-exception;
        monitor-exit(r11);
        throw r12;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.maml.SoundManager.playSound(java.lang.String, miui.maml.SoundManager$SoundOptions):int");
    }

    public synchronized void release() {
        if (this.mInitialized) {
            stopAllPlaying();
            if (this.mSoundPool != null) {
                this.mSoundPoolMap.clear();
                this.mSoundPool.setOnLoadCompleteListener(null);
                this.mSoundPool.release();
                this.mSoundPool = null;
            }
            this.mInitialized = false;
        }
    }

    private synchronized int playSoundImp(int sound, SoundOptions options) {
        if (this.mSoundPool == null) {
            return 0;
        }
        if (!options.mKeepCur) {
            stopAllPlaying();
        }
        try {
            int cur;
            synchronized (this.mPlayingSoundMap) {
                cur = this.mSoundPool.play(sound, options.mVolume, options.mVolume, 1, options.mLoop ? -1 : 0, 1.0f);
                this.mPlayingSoundMap.add(Integer.valueOf(cur));
            }
            return cur;
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
            return 0;
        }
        while (true) {
        }
    }

    /* Access modifiers changed, original: protected */
    public void stopAllPlaying() {
        if (!this.mPlayingSoundMap.isEmpty()) {
            synchronized (this.mPlayingSoundMap) {
                if (this.mSoundPool != null) {
                    Iterator it = this.mPlayingSoundMap.iterator();
                    while (it.hasNext()) {
                        this.mSoundPool.stop(((Integer) it.next()).intValue());
                    }
                }
                this.mPlayingSoundMap.clear();
            }
        }
    }

    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        if (status == 0) {
            playSoundImp(sampleId, (SoundOptions) this.mPendingSoundMap.get(Integer.valueOf(sampleId)));
        }
        this.mPendingSoundMap.remove(Integer.valueOf(sampleId));
    }

    public void pause() {
        stopAllPlaying();
    }
}
