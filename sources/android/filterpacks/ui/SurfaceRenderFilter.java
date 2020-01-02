package android.filterpacks.ui;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.FilterSurfaceView;
import android.filterfw.core.Frame;
import android.filterfw.core.GLEnvironment;
import android.filterfw.core.GLFrame;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;

public class SurfaceRenderFilter extends Filter implements Callback {
    private static final String TAG = "SurfaceRenderFilter";
    private final int RENDERMODE_FILL_CROP = 2;
    private final int RENDERMODE_FIT = 1;
    private final int RENDERMODE_STRETCH = 0;
    private float mAspectRatio = 1.0f;
    private boolean mIsBound = false;
    private boolean mLogVerbose = Log.isLoggable(TAG, 2);
    private ShaderProgram mProgram;
    private int mRenderMode = 1;
    @GenerateFieldPort(hasDefault = true, name = "renderMode")
    private String mRenderModeString;
    private GLFrame mScreen;
    private int mScreenHeight;
    private int mScreenWidth;
    @GenerateFinalPort(name = "surfaceView")
    private FilterSurfaceView mSurfaceView;

    public SurfaceRenderFilter(String name) {
        super(name);
    }

    public void setupPorts() {
        if (this.mSurfaceView != null) {
            addMaskedInputPort("frame", ImageFormat.create(3));
            return;
        }
        throw new RuntimeException("NULL SurfaceView passed to SurfaceRenderFilter");
    }

    public void updateRenderMode() {
        String str = this.mRenderModeString;
        if (str != null) {
            if (str.equals("stretch")) {
                this.mRenderMode = 0;
            } else if (this.mRenderModeString.equals("fit")) {
                this.mRenderMode = 1;
            } else if (this.mRenderModeString.equals("fill_crop")) {
                this.mRenderMode = 2;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown render mode '");
                stringBuilder.append(this.mRenderModeString);
                stringBuilder.append("'!");
                throw new RuntimeException(stringBuilder.toString());
            }
        }
        updateTargetRect();
    }

    public void prepare(FilterContext context) {
        this.mProgram = ShaderProgram.createIdentity(context);
        this.mProgram.setSourceRect(0.0f, 1.0f, 1.0f, -1.0f);
        this.mProgram.setClearsOutput(true);
        this.mProgram.setClearColor(0.0f, 0.0f, 0.0f);
        updateRenderMode();
        this.mScreen = (GLFrame) context.getFrameManager().newBoundFrame(ImageFormat.create(this.mSurfaceView.getWidth(), this.mSurfaceView.getHeight(), 3, 3), 101, 0);
    }

    public void open(FilterContext context) {
        this.mSurfaceView.unbind();
        this.mSurfaceView.bindToListener(this, context.getGLEnvironment());
    }

    public void process(FilterContext context) {
        boolean z = this.mIsBound;
        String str = TAG;
        if (z) {
            if (this.mLogVerbose) {
                Log.v(str, "Starting frame processing");
            }
            GLEnvironment glEnv = this.mSurfaceView.getGLEnv();
            if (glEnv == context.getGLEnvironment()) {
                Frame gpuFrame;
                Frame input = pullInput("frame");
                boolean createdFrame = false;
                float currentAspectRatio = ((float) input.getFormat().getWidth()) / ((float) input.getFormat().getHeight());
                if (currentAspectRatio != this.mAspectRatio) {
                    if (this.mLogVerbose) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("New aspect ratio: ");
                        stringBuilder.append(currentAspectRatio);
                        stringBuilder.append(", previously: ");
                        stringBuilder.append(this.mAspectRatio);
                        Log.v(str, stringBuilder.toString());
                    }
                    this.mAspectRatio = currentAspectRatio;
                    updateTargetRect();
                }
                if (this.mLogVerbose) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Got input format: ");
                    stringBuilder2.append(input.getFormat());
                    Log.v(str, stringBuilder2.toString());
                }
                if (input.getFormat().getTarget() != 3) {
                    gpuFrame = context.getFrameManager().duplicateFrameToTarget(input, 3);
                    createdFrame = true;
                } else {
                    gpuFrame = input;
                }
                glEnv.activateSurfaceWithId(this.mSurfaceView.getSurfaceId());
                this.mProgram.process(gpuFrame, (Frame) this.mScreen);
                glEnv.swapBuffers();
                if (createdFrame) {
                    gpuFrame.release();
                }
                return;
            }
            throw new RuntimeException("Surface created under different GLEnvironment!");
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(this);
        stringBuilder3.append(": Ignoring frame as there is no surface to render to!");
        Log.w(str, stringBuilder3.toString());
    }

    public void fieldPortValueUpdated(String name, FilterContext context) {
        updateTargetRect();
    }

    public void close(FilterContext context) {
        this.mSurfaceView.unbind();
    }

    public void tearDown(FilterContext context) {
        GLFrame gLFrame = this.mScreen;
        if (gLFrame != null) {
            gLFrame.release();
        }
    }

    public synchronized void surfaceCreated(SurfaceHolder holder) {
        this.mIsBound = true;
    }

    public synchronized void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (this.mScreen != null) {
            this.mScreenWidth = width;
            this.mScreenHeight = height;
            this.mScreen.setViewport(0, 0, this.mScreenWidth, this.mScreenHeight);
            updateTargetRect();
        }
    }

    public synchronized void surfaceDestroyed(SurfaceHolder holder) {
        this.mIsBound = false;
    }

    private void updateTargetRect() {
        int i = this.mScreenWidth;
        if (i > 0) {
            int i2 = this.mScreenHeight;
            if (i2 > 0) {
                ShaderProgram shaderProgram = this.mProgram;
                if (shaderProgram != null) {
                    float relativeAspectRatio = (((float) i) / ((float) i2)) / this.mAspectRatio;
                    int i3 = this.mRenderMode;
                    if (i3 == 0) {
                        shaderProgram.setTargetRect(0.0f, 0.0f, 1.0f, 1.0f);
                    } else if (i3 != 1) {
                        if (i3 == 2) {
                            if (relativeAspectRatio > 1.0f) {
                                shaderProgram.setTargetRect(0.0f, 0.5f - (relativeAspectRatio * 0.5f), 1.0f, relativeAspectRatio);
                            } else {
                                shaderProgram.setTargetRect(0.5f - (0.5f / relativeAspectRatio), 0.0f, 1.0f / relativeAspectRatio, 1.0f);
                            }
                        }
                    } else if (relativeAspectRatio > 1.0f) {
                        shaderProgram.setTargetRect(0.5f - (0.5f / relativeAspectRatio), 0.0f, 1.0f / relativeAspectRatio, 1.0f);
                    } else {
                        shaderProgram.setTargetRect(0.0f, 0.5f - (relativeAspectRatio * 0.5f), 1.0f, relativeAspectRatio);
                    }
                }
            }
        }
    }
}
