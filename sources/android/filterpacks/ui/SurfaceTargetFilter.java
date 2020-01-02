package android.filterpacks.ui;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.GLEnvironment;
import android.filterfw.core.GLFrame;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.util.Log;
import android.view.Surface;

public class SurfaceTargetFilter extends Filter {
    private static final String TAG = "SurfaceRenderFilter";
    private final int RENDERMODE_FILL_CROP = 2;
    private final int RENDERMODE_FIT = 1;
    private final int RENDERMODE_STRETCH = 0;
    private float mAspectRatio = 1.0f;
    private GLEnvironment mGlEnv;
    private boolean mLogVerbose = Log.isLoggable(TAG, 2);
    private ShaderProgram mProgram;
    private int mRenderMode = 1;
    @GenerateFieldPort(hasDefault = true, name = "renderMode")
    private String mRenderModeString;
    private GLFrame mScreen;
    @GenerateFieldPort(name = "oheight")
    private int mScreenHeight;
    @GenerateFieldPort(name = "owidth")
    private int mScreenWidth;
    @GenerateFinalPort(name = "surface")
    private Surface mSurface;
    private int mSurfaceId = -1;

    public SurfaceTargetFilter(String name) {
        super(name);
    }

    public void setupPorts() {
        if (this.mSurface != null) {
            addMaskedInputPort("frame", ImageFormat.create(3));
            return;
        }
        throw new RuntimeException("NULL Surface passed to SurfaceTargetFilter");
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
        this.mGlEnv = context.getGLEnvironment();
        this.mProgram = ShaderProgram.createIdentity(context);
        this.mProgram.setSourceRect(0.0f, 1.0f, 1.0f, -1.0f);
        this.mProgram.setClearsOutput(true);
        this.mProgram.setClearColor(0.0f, 0.0f, 0.0f);
        this.mScreen = (GLFrame) context.getFrameManager().newBoundFrame(ImageFormat.create(this.mScreenWidth, this.mScreenHeight, 3, 3), 101, 0);
        updateRenderMode();
    }

    public void open(FilterContext context) {
        registerSurface();
    }

    public void process(FilterContext context) {
        Frame gpuFrame;
        boolean z = this.mLogVerbose;
        String str = TAG;
        if (z) {
            Log.v(str, "Starting frame processing");
        }
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
        this.mGlEnv.activateSurfaceWithId(this.mSurfaceId);
        this.mProgram.process(gpuFrame, (Frame) this.mScreen);
        this.mGlEnv.swapBuffers();
        if (createdFrame) {
            gpuFrame.release();
        }
    }

    public void fieldPortValueUpdated(String name, FilterContext context) {
        this.mScreen.setViewport(0, 0, this.mScreenWidth, this.mScreenHeight);
        updateTargetRect();
    }

    public void close(FilterContext context) {
        unregisterSurface();
    }

    public void tearDown(FilterContext context) {
        GLFrame gLFrame = this.mScreen;
        if (gLFrame != null) {
            gLFrame.release();
        }
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

    private void registerSurface() {
        this.mSurfaceId = this.mGlEnv.registerSurface(this.mSurface);
        if (this.mSurfaceId < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not register Surface: ");
            stringBuilder.append(this.mSurface);
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    private void unregisterSurface() {
        int i = this.mSurfaceId;
        if (i > 0) {
            this.mGlEnv.unregisterSurfaceId(i);
        }
    }
}
