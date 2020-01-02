package android.filterpacks.videosrc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.GLFrame;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.filterfw.geometry.Point;
import android.filterfw.geometry.Quad;
import android.graphics.SurfaceTexture;
import android.util.Log;

public class SurfaceTextureTarget extends Filter {
    private static final String TAG = "SurfaceTextureTarget";
    private final int RENDERMODE_CUSTOMIZE = 3;
    private final int RENDERMODE_FILL_CROP = 2;
    private final int RENDERMODE_FIT = 1;
    private final int RENDERMODE_STRETCH = 0;
    private float mAspectRatio = 1.0f;
    private boolean mLogVerbose = Log.isLoggable(TAG, 2);
    private ShaderProgram mProgram;
    private int mRenderMode = 1;
    @GenerateFieldPort(hasDefault = true, name = "renderMode")
    private String mRenderModeString;
    private GLFrame mScreen;
    @GenerateFinalPort(name = "height")
    private int mScreenHeight;
    @GenerateFinalPort(name = "width")
    private int mScreenWidth;
    @GenerateFieldPort(hasDefault = true, name = "sourceQuad")
    private Quad mSourceQuad = new Quad(new Point(0.0f, 1.0f), new Point(1.0f, 1.0f), new Point(0.0f, 0.0f), new Point(1.0f, 0.0f));
    private int mSurfaceId;
    @GenerateFinalPort(name = "surfaceTexture")
    private SurfaceTexture mSurfaceTexture;
    @GenerateFieldPort(hasDefault = true, name = "targetQuad")
    private Quad mTargetQuad = new Quad(new Point(0.0f, 0.0f), new Point(1.0f, 0.0f), new Point(0.0f, 1.0f), new Point(1.0f, 1.0f));

    public SurfaceTextureTarget(String name) {
        super(name);
    }

    public synchronized void setupPorts() {
        if (this.mSurfaceTexture != null) {
            addMaskedInputPort("frame", ImageFormat.create(3));
        } else {
            throw new RuntimeException("Null SurfaceTexture passed to SurfaceTextureTarget");
        }
    }

    public void updateRenderMode() {
        if (this.mLogVerbose) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("updateRenderMode. Thread: ");
            stringBuilder.append(Thread.currentThread());
            Log.v(TAG, stringBuilder.toString());
        }
        String str = this.mRenderModeString;
        if (str != null) {
            if (str.equals("stretch")) {
                this.mRenderMode = 0;
            } else if (this.mRenderModeString.equals("fit")) {
                this.mRenderMode = 1;
            } else if (this.mRenderModeString.equals("fill_crop")) {
                this.mRenderMode = 2;
            } else if (this.mRenderModeString.equals("customize")) {
                this.mRenderMode = 3;
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Unknown render mode '");
                stringBuilder2.append(this.mRenderModeString);
                stringBuilder2.append("'!");
                throw new RuntimeException(stringBuilder2.toString());
            }
        }
        updateTargetRect();
    }

    public void prepare(FilterContext context) {
        if (this.mLogVerbose) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Prepare. Thread: ");
            stringBuilder.append(Thread.currentThread());
            Log.v(TAG, stringBuilder.toString());
        }
        this.mProgram = ShaderProgram.createIdentity(context);
        this.mProgram.setSourceRect(0.0f, 1.0f, 1.0f, -1.0f);
        this.mProgram.setClearColor(0.0f, 0.0f, 0.0f);
        updateRenderMode();
        MutableFrameFormat screenFormat = new MutableFrameFormat(2, 3);
        screenFormat.setBytesPerSample(4);
        screenFormat.setDimensions(this.mScreenWidth, this.mScreenHeight);
        this.mScreen = (GLFrame) context.getFrameManager().newBoundFrame(screenFormat, 101, 0);
    }

    public synchronized void open(FilterContext context) {
        StringBuilder stringBuilder;
        if (this.mSurfaceTexture != null) {
            this.mSurfaceId = context.getGLEnvironment().registerSurfaceTexture(this.mSurfaceTexture, this.mScreenWidth, this.mScreenHeight);
            if (this.mSurfaceId <= 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Could not register SurfaceTexture: ");
                stringBuilder.append(this.mSurfaceTexture);
                throw new RuntimeException(stringBuilder.toString());
            }
        } else {
            Log.e(TAG, "SurfaceTexture is null!!");
            stringBuilder = new StringBuilder();
            stringBuilder.append("Could not register SurfaceTexture: ");
            stringBuilder.append(this.mSurfaceTexture);
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    public synchronized void close(FilterContext context) {
        if (this.mSurfaceId > 0) {
            context.getGLEnvironment().unregisterSurfaceId(this.mSurfaceId);
            this.mSurfaceId = -1;
        }
    }

    /* JADX WARNING: Missing block: B:16:0x002d, code skipped:
            return;
     */
    public synchronized void disconnect(android.filterfw.core.FilterContext r3) {
        /*
        r2 = this;
        monitor-enter(r2);
        r0 = r2.mLogVerbose;	 Catch:{ all -> 0x002e }
        if (r0 == 0) goto L_0x000c;
    L_0x0005:
        r0 = "SurfaceTextureTarget";
        r1 = "disconnect";
        android.util.Log.v(r0, r1);	 Catch:{ all -> 0x002e }
    L_0x000c:
        r0 = r2.mSurfaceTexture;	 Catch:{ all -> 0x002e }
        if (r0 != 0) goto L_0x0019;
    L_0x0010:
        r0 = "SurfaceTextureTarget";
        r1 = "SurfaceTexture is already null. Nothing to disconnect.";
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x002e }
        monitor-exit(r2);
        return;
    L_0x0019:
        r0 = 0;
        r2.mSurfaceTexture = r0;	 Catch:{ all -> 0x002e }
        r0 = r2.mSurfaceId;	 Catch:{ all -> 0x002e }
        if (r0 <= 0) goto L_0x002c;
    L_0x0020:
        r0 = r3.getGLEnvironment();	 Catch:{ all -> 0x002e }
        r1 = r2.mSurfaceId;	 Catch:{ all -> 0x002e }
        r0.unregisterSurfaceId(r1);	 Catch:{ all -> 0x002e }
        r0 = -1;
        r2.mSurfaceId = r0;	 Catch:{ all -> 0x002e }
    L_0x002c:
        monitor-exit(r2);
        return;
    L_0x002e:
        r3 = move-exception;
        monitor-exit(r2);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.filterpacks.videosrc.SurfaceTextureTarget.disconnect(android.filterfw.core.FilterContext):void");
    }

    /* JADX WARNING: Missing block: B:21:0x0095, code skipped:
            return;
     */
    public synchronized void process(android.filterfw.core.FilterContext r9) {
        /*
        r8 = this;
        monitor-enter(r8);
        r0 = r8.mSurfaceId;	 Catch:{ all -> 0x0096 }
        if (r0 > 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r8);
        return;
    L_0x0007:
        r0 = r9.getGLEnvironment();	 Catch:{ all -> 0x0096 }
        r1 = "frame";
        r1 = r8.pullInput(r1);	 Catch:{ all -> 0x0096 }
        r2 = 0;
        r3 = r1.getFormat();	 Catch:{ all -> 0x0096 }
        r3 = r3.getWidth();	 Catch:{ all -> 0x0096 }
        r3 = (float) r3;	 Catch:{ all -> 0x0096 }
        r4 = r1.getFormat();	 Catch:{ all -> 0x0096 }
        r4 = r4.getHeight();	 Catch:{ all -> 0x0096 }
        r4 = (float) r4;	 Catch:{ all -> 0x0096 }
        r3 = r3 / r4;
        r4 = r8.mAspectRatio;	 Catch:{ all -> 0x0096 }
        r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r4 == 0) goto L_0x0061;
    L_0x002c:
        r4 = r8.mLogVerbose;	 Catch:{ all -> 0x0096 }
        if (r4 == 0) goto L_0x005c;
    L_0x0030:
        r4 = "SurfaceTextureTarget";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0096 }
        r5.<init>();	 Catch:{ all -> 0x0096 }
        r6 = "Process. New aspect ratio: ";
        r5.append(r6);	 Catch:{ all -> 0x0096 }
        r5.append(r3);	 Catch:{ all -> 0x0096 }
        r6 = ", previously: ";
        r5.append(r6);	 Catch:{ all -> 0x0096 }
        r6 = r8.mAspectRatio;	 Catch:{ all -> 0x0096 }
        r5.append(r6);	 Catch:{ all -> 0x0096 }
        r6 = ". Thread: ";
        r5.append(r6);	 Catch:{ all -> 0x0096 }
        r6 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0096 }
        r5.append(r6);	 Catch:{ all -> 0x0096 }
        r5 = r5.toString();	 Catch:{ all -> 0x0096 }
        android.util.Log.v(r4, r5);	 Catch:{ all -> 0x0096 }
    L_0x005c:
        r8.mAspectRatio = r3;	 Catch:{ all -> 0x0096 }
        r8.updateTargetRect();	 Catch:{ all -> 0x0096 }
    L_0x0061:
        r4 = 0;
        r5 = r1.getFormat();	 Catch:{ all -> 0x0096 }
        r5 = r5.getTarget();	 Catch:{ all -> 0x0096 }
        r6 = 3;
        if (r5 == r6) goto L_0x0078;
    L_0x006d:
        r7 = r9.getFrameManager();	 Catch:{ all -> 0x0096 }
        r6 = r7.duplicateFrameToTarget(r1, r6);	 Catch:{ all -> 0x0096 }
        r4 = r6;
        r2 = 1;
        goto L_0x0079;
    L_0x0078:
        r4 = r1;
    L_0x0079:
        r6 = r8.mSurfaceId;	 Catch:{ all -> 0x0096 }
        r0.activateSurfaceWithId(r6);	 Catch:{ all -> 0x0096 }
        r6 = r8.mProgram;	 Catch:{ all -> 0x0096 }
        r7 = r8.mScreen;	 Catch:{ all -> 0x0096 }
        r6.process(r4, r7);	 Catch:{ all -> 0x0096 }
        r6 = r1.getTimestamp();	 Catch:{ all -> 0x0096 }
        r0.setSurfaceTimestamp(r6);	 Catch:{ all -> 0x0096 }
        r0.swapBuffers();	 Catch:{ all -> 0x0096 }
        if (r2 == 0) goto L_0x0094;
    L_0x0091:
        r4.release();	 Catch:{ all -> 0x0096 }
    L_0x0094:
        monitor-exit(r8);
        return;
    L_0x0096:
        r9 = move-exception;
        monitor-exit(r8);
        throw r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.filterpacks.videosrc.SurfaceTextureTarget.process(android.filterfw.core.FilterContext):void");
    }

    public void fieldPortValueUpdated(String name, FilterContext context) {
        if (this.mLogVerbose) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("FPVU. Thread: ");
            stringBuilder.append(Thread.currentThread());
            Log.v(TAG, stringBuilder.toString());
        }
        updateRenderMode();
    }

    public void tearDown(FilterContext context) {
        GLFrame gLFrame = this.mScreen;
        if (gLFrame != null) {
            gLFrame.release();
        }
    }

    private void updateTargetRect() {
        boolean z = this.mLogVerbose;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("updateTargetRect. Thread: ");
            stringBuilder.append(Thread.currentThread());
            Log.v(str, stringBuilder.toString());
        }
        int i = this.mScreenWidth;
        if (i > 0) {
            int i2 = this.mScreenHeight;
            if (i2 > 0 && this.mProgram != null) {
                StringBuilder stringBuilder2;
                float screenAspectRatio = ((float) i) / ((float) i2);
                float relativeAspectRatio = screenAspectRatio / this.mAspectRatio;
                if (this.mLogVerbose) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("UTR. screen w = ");
                    stringBuilder2.append((float) this.mScreenWidth);
                    stringBuilder2.append(" x screen h = ");
                    stringBuilder2.append((float) this.mScreenHeight);
                    stringBuilder2.append(" Screen AR: ");
                    stringBuilder2.append(screenAspectRatio);
                    stringBuilder2.append(", frame AR: ");
                    stringBuilder2.append(this.mAspectRatio);
                    stringBuilder2.append(", relative AR: ");
                    stringBuilder2.append(relativeAspectRatio);
                    Log.v(str, stringBuilder2.toString());
                }
                if (relativeAspectRatio != 1.0f || this.mRenderMode == 3) {
                    int i3 = this.mRenderMode;
                    if (i3 == 0) {
                        this.mTargetQuad.p0.set(0.0f, 0.0f);
                        this.mTargetQuad.p1.set(1.0f, 0.0f);
                        this.mTargetQuad.p2.set(0.0f, 1.0f);
                        this.mTargetQuad.p3.set(1.0f, 1.0f);
                        this.mProgram.setClearsOutput(false);
                    } else if (i3 == 1) {
                        if (relativeAspectRatio > 1.0f) {
                            this.mTargetQuad.p0.set(0.5f - (0.5f / relativeAspectRatio), 0.0f);
                            this.mTargetQuad.p1.set((0.5f / relativeAspectRatio) + 0.5f, 0.0f);
                            this.mTargetQuad.p2.set(0.5f - (0.5f / relativeAspectRatio), 1.0f);
                            this.mTargetQuad.p3.set((0.5f / relativeAspectRatio) + 0.5f, 1.0f);
                        } else {
                            this.mTargetQuad.p0.set(0.0f, 0.5f - (relativeAspectRatio * 0.5f));
                            this.mTargetQuad.p1.set(1.0f, 0.5f - (relativeAspectRatio * 0.5f));
                            this.mTargetQuad.p2.set(0.0f, (relativeAspectRatio * 0.5f) + 0.5f);
                            this.mTargetQuad.p3.set(1.0f, (relativeAspectRatio * 0.5f) + 0.5f);
                        }
                        this.mProgram.setClearsOutput(true);
                    } else if (i3 == 2) {
                        if (relativeAspectRatio > 1.0f) {
                            this.mTargetQuad.p0.set(0.0f, 0.5f - (relativeAspectRatio * 0.5f));
                            this.mTargetQuad.p1.set(1.0f, 0.5f - (relativeAspectRatio * 0.5f));
                            this.mTargetQuad.p2.set(0.0f, (relativeAspectRatio * 0.5f) + 0.5f);
                            this.mTargetQuad.p3.set(1.0f, (relativeAspectRatio * 0.5f) + 0.5f);
                        } else {
                            this.mTargetQuad.p0.set(0.5f - (0.5f / relativeAspectRatio), 0.0f);
                            this.mTargetQuad.p1.set((0.5f / relativeAspectRatio) + 0.5f, 0.0f);
                            this.mTargetQuad.p2.set(0.5f - (0.5f / relativeAspectRatio), 1.0f);
                            this.mTargetQuad.p3.set((0.5f / relativeAspectRatio) + 0.5f, 1.0f);
                        }
                        this.mProgram.setClearsOutput(true);
                    } else if (i3 == 3) {
                        this.mProgram.setSourceRegion(this.mSourceQuad);
                    }
                    if (this.mLogVerbose) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("UTR. quad: ");
                        stringBuilder2.append(this.mTargetQuad);
                        Log.v(str, stringBuilder2.toString());
                    }
                    this.mProgram.setTargetRegion(this.mTargetQuad);
                    return;
                }
                this.mProgram.setTargetRect(0.0f, 0.0f, 1.0f, 1.0f);
                this.mProgram.setClearsOutput(false);
            }
        }
    }
}
