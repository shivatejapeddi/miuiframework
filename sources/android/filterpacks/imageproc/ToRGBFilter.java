package android.filterpacks.imageproc;

import android.app.slice.SliceItem;
import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.NativeProgram;
import android.filterfw.core.Program;
import android.filterfw.format.ImageFormat;

public class ToRGBFilter extends Filter {
    private int mInputBPP;
    private FrameFormat mLastFormat = null;
    private Program mProgram;

    public ToRGBFilter(String name) {
        super(name);
    }

    public void setupPorts() {
        MutableFrameFormat mask = new MutableFrameFormat(2, 2);
        mask.setDimensionCount(2);
        String str = SliceItem.FORMAT_IMAGE;
        addMaskedInputPort(str, mask);
        addOutputBasedOnInput(str, str);
    }

    public FrameFormat getOutputFormat(String portName, FrameFormat inputFormat) {
        return getConvertedFormat(inputFormat);
    }

    public FrameFormat getConvertedFormat(FrameFormat format) {
        MutableFrameFormat result = format.mutableCopy();
        result.setMetaValue(ImageFormat.COLORSPACE_KEY, Integer.valueOf(2));
        result.setBytesPerSample(3);
        return result;
    }

    public void createProgram(FilterContext context, FrameFormat format) {
        this.mInputBPP = format.getBytesPerSample();
        FrameFormat frameFormat = this.mLastFormat;
        if (frameFormat == null || frameFormat.getBytesPerSample() != this.mInputBPP) {
            this.mLastFormat = format;
            int i = this.mInputBPP;
            String str = "filterpack_imageproc";
            if (i == 1) {
                this.mProgram = new NativeProgram(str, "gray_to_rgb");
            } else if (i == 4) {
                this.mProgram = new NativeProgram(str, "rgba_to_rgb");
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported BytesPerPixel: ");
                stringBuilder.append(this.mInputBPP);
                stringBuilder.append("!");
                throw new RuntimeException(stringBuilder.toString());
            }
        }
    }

    public void process(FilterContext context) {
        String str = SliceItem.FORMAT_IMAGE;
        Frame input = pullInput(str);
        createProgram(context, input.getFormat());
        Frame output = context.getFrameManager().newFrame(getConvertedFormat(input.getFormat()));
        this.mProgram.process(input, output);
        pushOutput(str, output);
        output.release();
    }
}
