package android.filterpacks.imageproc;

import android.app.slice.SliceItem;
import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.filterfw.geometry.Point;
import android.filterfw.geometry.Quad;

public class FixedRotationFilter extends Filter {
    private ShaderProgram mProgram = null;
    @GenerateFieldPort(hasDefault = true, name = "rotation")
    private int mRotation = 0;

    public FixedRotationFilter(String name) {
        super(name);
    }

    public void setupPorts() {
        MutableFrameFormat create = ImageFormat.create(3, 3);
        String str = SliceItem.FORMAT_IMAGE;
        addMaskedInputPort(str, create);
        addOutputBasedOnInput(str, str);
    }

    public FrameFormat getOutputFormat(String portName, FrameFormat inputFormat) {
        return inputFormat;
    }

    public void process(FilterContext context) {
        String str = SliceItem.FORMAT_IMAGE;
        Frame input = pullInput(str);
        if (this.mRotation == 0) {
            pushOutput(str, input);
            return;
        }
        Quad sourceRegion;
        FrameFormat inputFormat = input.getFormat();
        if (this.mProgram == null) {
            this.mProgram = ShaderProgram.createIdentity(context);
        }
        MutableFrameFormat outputFormat = inputFormat.mutableCopy();
        int width = inputFormat.getWidth();
        int height = inputFormat.getHeight();
        Point p1 = new Point(0.0f, 0.0f);
        Point p2 = new Point(1.0f, 0.0f);
        Point p3 = new Point(0.0f, 1.0f);
        Point p4 = new Point(1.0f, 1.0f);
        int round = Math.round(((float) this.mRotation) / 90.0f) % 4;
        if (round == 1) {
            sourceRegion = new Quad(p3, p1, p4, p2);
            outputFormat.setDimensions(height, width);
        } else if (round == 2) {
            sourceRegion = new Quad(p4, p3, p2, p1);
        } else if (round != 3) {
            sourceRegion = new Quad(p1, p2, p3, p4);
        } else {
            sourceRegion = new Quad(p2, p4, p1, p3);
            outputFormat.setDimensions(height, width);
        }
        Frame output = context.getFrameManager().newFrame(outputFormat);
        this.mProgram.setSourceRegion(sourceRegion);
        this.mProgram.process(input, output);
        pushOutput(str, output);
        output.release();
    }
}
