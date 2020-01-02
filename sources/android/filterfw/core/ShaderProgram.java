package android.filterfw.core;

import android.annotation.UnsupportedAppUsage;
import android.filterfw.geometry.Quad;
import android.opengl.GLES20;

public class ShaderProgram extends Program {
    private GLEnvironment mGLEnvironment;
    private int mMaxTileSize = 0;
    private StopWatchMap mTimer = null;
    private int shaderProgramId;

    private native boolean allocate(GLEnvironment gLEnvironment, String str, String str2);

    private native boolean beginShaderDrawing();

    private native boolean compileAndLink();

    private native boolean deallocate();

    private native Object getUniformValue(String str);

    private static native ShaderProgram nativeCreateIdentity(GLEnvironment gLEnvironment);

    private native boolean setShaderAttributeValues(String str, float[] fArr, int i);

    private native boolean setShaderAttributeVertexFrame(String str, VertexFrame vertexFrame, int i, int i2, int i3, int i4, boolean z);

    private native boolean setShaderBlendEnabled(boolean z);

    private native boolean setShaderBlendFunc(int i, int i2);

    private native boolean setShaderClearColor(float f, float f2, float f3);

    private native boolean setShaderClearsOutput(boolean z);

    private native boolean setShaderDrawMode(int i);

    private native boolean setShaderTileCounts(int i, int i2);

    private native boolean setShaderVertexCount(int i);

    private native boolean setTargetRegion(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8);

    private native boolean setUniformValue(String str, Object obj);

    private native boolean shaderProcess(GLFrame[] gLFrameArr, GLFrame gLFrame);

    public native boolean setSourceRegion(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8);

    private void setTimer() {
        this.mTimer = new StopWatchMap();
    }

    private ShaderProgram() {
    }

    private ShaderProgram(NativeAllocatorTag tag) {
    }

    @UnsupportedAppUsage
    public ShaderProgram(FilterContext context, String fragmentShader) {
        this.mGLEnvironment = getGLEnvironment(context);
        allocate(this.mGLEnvironment, null, fragmentShader);
        if (compileAndLink()) {
            setTimer();
            return;
        }
        throw new RuntimeException("Could not compile and link shader!");
    }

    public ShaderProgram(FilterContext context, String vertexShader, String fragmentShader) {
        this.mGLEnvironment = getGLEnvironment(context);
        allocate(this.mGLEnvironment, vertexShader, fragmentShader);
        if (compileAndLink()) {
            setTimer();
            return;
        }
        throw new RuntimeException("Could not compile and link shader!");
    }

    @UnsupportedAppUsage
    public static ShaderProgram createIdentity(FilterContext context) {
        ShaderProgram program = nativeCreateIdentity(getGLEnvironment(context));
        program.setTimer();
        return program;
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        deallocate();
    }

    public GLEnvironment getGLEnvironment() {
        return this.mGLEnvironment;
    }

    @UnsupportedAppUsage
    public void process(Frame[] inputs, Frame output) {
        if (this.mTimer.LOG_MFF_RUNNING_TIMES) {
            String str = "glFinish";
            this.mTimer.start(str);
            GLES20.glFinish();
            this.mTimer.stop(str);
        }
        GLFrame[] glInputs = new GLFrame[inputs.length];
        int i = 0;
        while (i < inputs.length) {
            if (inputs[i] instanceof GLFrame) {
                glInputs[i] = (GLFrame) inputs[i];
                i++;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("ShaderProgram got non-GL frame as input ");
                stringBuilder.append(i);
                stringBuilder.append("!");
                throw new RuntimeException(stringBuilder.toString());
            }
        }
        if (output instanceof GLFrame) {
            GLFrame glOutput = (GLFrame) output;
            if (this.mMaxTileSize > 0) {
                int width = output.getFormat().getWidth();
                int i2 = this.mMaxTileSize;
                width = ((width + i2) - 1) / i2;
                i2 = output.getFormat().getHeight();
                int i3 = this.mMaxTileSize;
                setShaderTileCounts(width, ((i2 + i3) - 1) / i3);
            }
            if (!shaderProcess(glInputs, glOutput)) {
                throw new RuntimeException("Error executing ShaderProgram!");
            } else if (this.mTimer.LOG_MFF_RUNNING_TIMES) {
                GLES20.glFinish();
                return;
            } else {
                return;
            }
        }
        throw new RuntimeException("ShaderProgram got non-GL output frame!");
    }

    @UnsupportedAppUsage
    public void setHostValue(String variableName, Object value) {
        if (!setUniformValue(variableName, value)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error setting uniform value for variable '");
            stringBuilder.append(variableName);
            stringBuilder.append("'!");
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    public Object getHostValue(String variableName) {
        return getUniformValue(variableName);
    }

    public void setAttributeValues(String attributeName, float[] data, int componentCount) {
        if (!setShaderAttributeValues(attributeName, data, componentCount)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error setting attribute value for attribute '");
            stringBuilder.append(attributeName);
            stringBuilder.append("'!");
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    public void setAttributeValues(String attributeName, VertexFrame vertexData, int type, int componentCount, int strideInBytes, int offsetInBytes, boolean normalize) {
        if (!setShaderAttributeVertexFrame(attributeName, vertexData, type, componentCount, strideInBytes, offsetInBytes, normalize)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error setting attribute value for attribute '");
            stringBuilder.append(attributeName);
            stringBuilder.append("'!");
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    @UnsupportedAppUsage
    public void setSourceRegion(Quad region) {
        setSourceRegion(region.p0.x, region.p0.y, region.p1.x, region.p1.y, region.p2.x, region.p2.y, region.p3.x, region.p3.y);
    }

    public void setTargetRegion(Quad region) {
        setTargetRegion(region.p0.x, region.p0.y, region.p1.x, region.p1.y, region.p2.x, region.p2.y, region.p3.x, region.p3.y);
    }

    @UnsupportedAppUsage
    public void setSourceRect(float x, float y, float width, float height) {
        setSourceRegion(x, y, x + width, y, x, y + height, x + width, y + height);
    }

    public void setTargetRect(float x, float y, float width, float height) {
        setTargetRegion(x, y, x + width, y, x, y + height, x + width, y + height);
    }

    public void setClearsOutput(boolean clears) {
        if (!setShaderClearsOutput(clears)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not set clears-output flag to ");
            stringBuilder.append(clears);
            stringBuilder.append("!");
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    public void setClearColor(float r, float g, float b) {
        if (!setShaderClearColor(r, g, b)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not set clear color to ");
            stringBuilder.append(r);
            String str = ",";
            stringBuilder.append(str);
            stringBuilder.append(g);
            stringBuilder.append(str);
            stringBuilder.append(b);
            stringBuilder.append("!");
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    public void setBlendEnabled(boolean enable) {
        if (!setShaderBlendEnabled(enable)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not set Blending ");
            stringBuilder.append(enable);
            stringBuilder.append("!");
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    public void setBlendFunc(int sfactor, int dfactor) {
        if (!setShaderBlendFunc(sfactor, dfactor)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not set BlendFunc ");
            stringBuilder.append(sfactor);
            stringBuilder.append(",");
            stringBuilder.append(dfactor);
            stringBuilder.append("!");
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    public void setDrawMode(int drawMode) {
        if (!setShaderDrawMode(drawMode)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not set GL draw-mode to ");
            stringBuilder.append(drawMode);
            stringBuilder.append("!");
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    public void setVertexCount(int count) {
        if (!setShaderVertexCount(count)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not set GL vertex count to ");
            stringBuilder.append(count);
            stringBuilder.append("!");
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    @UnsupportedAppUsage
    public void setMaximumTileSize(int size) {
        this.mMaxTileSize = size;
    }

    public void beginDrawing() {
        if (!beginShaderDrawing()) {
            throw new RuntimeException("Could not prepare shader-program for drawing!");
        }
    }

    private static GLEnvironment getGLEnvironment(FilterContext context) {
        GLEnvironment result = context != null ? context.getGLEnvironment() : null;
        if (result != null) {
            return result;
        }
        throw new NullPointerException("Attempting to create ShaderProgram with no GL environment in place!");
    }

    static {
        System.loadLibrary("filterfw");
    }
}
