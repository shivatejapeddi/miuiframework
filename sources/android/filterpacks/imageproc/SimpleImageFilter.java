package android.filterpacks.imageproc;

import android.app.slice.SliceItem;
import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.Program;
import android.filterfw.format.ImageFormat;

public abstract class SimpleImageFilter extends Filter {
    protected int mCurrentTarget = 0;
    protected String mParameterName;
    protected Program mProgram;

    public abstract Program getNativeProgram(FilterContext filterContext);

    public abstract Program getShaderProgram(FilterContext filterContext);

    public SimpleImageFilter(String name, String parameterName) {
        super(name);
        this.mParameterName = parameterName;
    }

    public void setupPorts() {
        if (this.mParameterName != null) {
            try {
                addProgramPort(this.mParameterName, this.mParameterName, SimpleImageFilter.class.getDeclaredField("mProgram"), Float.TYPE, false);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Internal Error: mProgram field not found!");
            }
        }
        MutableFrameFormat create = ImageFormat.create(3);
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
        FrameFormat inputFormat = input.getFormat();
        Frame output = context.getFrameManager().newFrame(inputFormat);
        updateProgramWithTarget(inputFormat.getTarget(), context);
        this.mProgram.process(input, output);
        pushOutput(str, output);
        output.release();
    }

    /* Access modifiers changed, original: protected */
    public void updateProgramWithTarget(int target, FilterContext context) {
        if (target != this.mCurrentTarget) {
            if (target == 2) {
                this.mProgram = getNativeProgram(context);
            } else if (target != 3) {
                this.mProgram = null;
            } else {
                this.mProgram = getShaderProgram(context);
            }
            Program program = this.mProgram;
            if (program != null) {
                initProgramInputs(program, context);
                this.mCurrentTarget = target;
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not create a program for image filter ");
            stringBuilder.append(this);
            stringBuilder.append("!");
            throw new RuntimeException(stringBuilder.toString());
        }
    }
}
