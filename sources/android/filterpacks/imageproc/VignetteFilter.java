package android.filterpacks.imageproc;

import android.app.slice.Slice;
import android.app.slice.SliceItem;
import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.Program;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.hardware.Camera.Parameters;
import android.os.BatteryManager;

public class VignetteFilter extends Filter {
    private int mHeight = 0;
    private Program mProgram;
    @GenerateFieldPort(hasDefault = true, name = "scale")
    private float mScale = 0.0f;
    private final float mShade = 0.85f;
    private final float mSlope = 20.0f;
    private int mTarget = 0;
    @GenerateFieldPort(hasDefault = true, name = "tile_size")
    private int mTileSize = 640;
    private final String mVignetteShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform float range;\nuniform float inv_max_dist;\nuniform float shade;\nuniform vec2 scale;\nvarying vec2 v_texcoord;\nvoid main() {\n  const float slope = 20.0;\n  vec2 coord = v_texcoord - vec2(0.5, 0.5);\n  float dist = length(coord * scale);\n  float lumen = shade / (1.0 + exp((dist * inv_max_dist - range) * slope)) + (1.0 - shade);\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  gl_FragColor = vec4(color.rgb * lumen, color.a);\n}\n";
    private int mWidth = 0;

    public VignetteFilter(String name) {
        super(name);
    }

    public void setupPorts() {
        MutableFrameFormat create = ImageFormat.create(3);
        String str = SliceItem.FORMAT_IMAGE;
        addMaskedInputPort(str, create);
        addOutputBasedOnInput(str, str);
    }

    public FrameFormat getOutputFormat(String portName, FrameFormat inputFormat) {
        return inputFormat;
    }

    public void initProgram(FilterContext context, int target) {
        if (target == 3) {
            ShaderProgram shaderProgram = new ShaderProgram(context, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform float range;\nuniform float inv_max_dist;\nuniform float shade;\nuniform vec2 scale;\nvarying vec2 v_texcoord;\nvoid main() {\n  const float slope = 20.0;\n  vec2 coord = v_texcoord - vec2(0.5, 0.5);\n  float dist = length(coord * scale);\n  float lumen = shade / (1.0 + exp((dist * inv_max_dist - range) * slope)) + (1.0 - shade);\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  gl_FragColor = vec4(color.rgb * lumen, color.a);\n}\n");
            shaderProgram.setMaximumTileSize(this.mTileSize);
            this.mProgram = shaderProgram;
            this.mTarget = target;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Filter Sharpen does not support frames of target ");
        stringBuilder.append(target);
        stringBuilder.append("!");
        throw new RuntimeException(stringBuilder.toString());
    }

    private void initParameters() {
        if (this.mProgram != null) {
            float[] scale = new float[2];
            int i = this.mWidth;
            int i2 = this.mHeight;
            if (i > i2) {
                scale[0] = 1.0f;
                scale[1] = ((float) i2) / ((float) i);
            } else {
                scale[0] = ((float) i) / ((float) i2);
                scale[1] = 1.0f;
            }
            float max_dist = ((float) Math.sqrt((double) ((scale[0] * scale[0]) + (scale[1] * scale[1])))) * 0.5f;
            this.mProgram.setHostValue(BatteryManager.EXTRA_SCALE, scale);
            this.mProgram.setHostValue("inv_max_dist", Float.valueOf(1.0f / max_dist));
            this.mProgram.setHostValue(Parameters.WHITE_BALANCE_SHADE, Float.valueOf(0.85f));
            updateParameters();
        }
    }

    private void updateParameters() {
        this.mProgram.setHostValue(Slice.SUBTYPE_RANGE, Float.valueOf(1.3f - (((float) Math.sqrt((double) this.mScale)) * 0.7f)));
    }

    public void fieldPortValueUpdated(String name, FilterContext context) {
        if (this.mProgram != null) {
            updateParameters();
        }
    }

    public void process(FilterContext context) {
        String str = SliceItem.FORMAT_IMAGE;
        Frame input = pullInput(str);
        FrameFormat inputFormat = input.getFormat();
        if (this.mProgram == null || inputFormat.getTarget() != this.mTarget) {
            initProgram(context, inputFormat.getTarget());
        }
        if (!(inputFormat.getWidth() == this.mWidth && inputFormat.getHeight() == this.mHeight)) {
            this.mWidth = inputFormat.getWidth();
            this.mHeight = inputFormat.getHeight();
            initParameters();
        }
        Frame output = context.getFrameManager().newFrame(inputFormat);
        this.mProgram.process(input, output);
        pushOutput(str, output);
        output.release();
    }
}
