package android.filterpacks.imageproc;

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
import android.filterfw.geometry.Point;
import android.filterfw.geometry.Quad;

public class RotateFilter extends Filter {
    @GenerateFieldPort(name = "angle")
    private int mAngle;
    private int mHeight = 0;
    private int mOutputHeight;
    private int mOutputWidth;
    private Program mProgram;
    private int mTarget = 0;
    @GenerateFieldPort(hasDefault = true, name = "tile_size")
    private int mTileSize = 640;
    private int mWidth = 0;

    public RotateFilter(String name) {
        super(name);
    }

    public void setupPorts() {
        MutableFrameFormat create = ImageFormat.create(3);
        String str = SliceItem.FORMAT_IMAGE;
        addMaskedInputPort(str, create);
        addOutputBasedOnInput(str, str);
    }

    public void initProgram(FilterContext context, int target) {
        if (target == 3) {
            ShaderProgram shaderProgram = ShaderProgram.createIdentity(context);
            shaderProgram.setMaximumTileSize(this.mTileSize);
            shaderProgram.setClearsOutput(true);
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
            this.mOutputWidth = this.mWidth;
            this.mOutputHeight = this.mHeight;
            updateParameters();
        }
        Frame output = context.getFrameManager().newFrame(ImageFormat.create(this.mOutputWidth, this.mOutputHeight, 3, 3));
        this.mProgram.process(input, output);
        pushOutput(str, output);
        output.release();
    }

    private void updateParameters() {
        int i = this.mAngle;
        if (i % 90 == 0) {
            float sinTheta;
            float cosTheta;
            float f = -1.0f;
            if (i % 180 == 0) {
                sinTheta = 0.0f;
                if (i % 360 == 0) {
                    f = 1.0f;
                }
                cosTheta = f;
            } else {
                if ((i + 90) % 360 != 0) {
                    f = 1.0f;
                }
                cosTheta = f;
                this.mOutputWidth = this.mHeight;
                this.mOutputHeight = this.mWidth;
                sinTheta = cosTheta;
                cosTheta = 0.0f;
            }
            ((ShaderProgram) this.mProgram).setTargetRegion(new Quad(new Point((((-cosTheta) + sinTheta) + 1.0f) * 0.5f, (((-sinTheta) - cosTheta) + 1.0f) * 0.5f), new Point(((cosTheta + sinTheta) + 1.0f) * 0.5f, ((sinTheta - cosTheta) + 1.0f) * 0.5f), new Point((((-cosTheta) - sinTheta) + 1.0f) * 0.5f, (((-sinTheta) + cosTheta) + 1.0f) * 0.5f), new Point(((cosTheta - sinTheta) + 1.0f) * 0.5f, ((sinTheta + cosTheta) + 1.0f) * 0.5f)));
            return;
        }
        throw new RuntimeException("degree has to be multiply of 90.");
    }
}
