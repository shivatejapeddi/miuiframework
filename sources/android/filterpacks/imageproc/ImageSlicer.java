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

public class ImageSlicer extends Filter {
    private int mInputHeight;
    private int mInputWidth;
    private Frame mOriginalFrame;
    private int mOutputHeight;
    private int mOutputWidth;
    @GenerateFieldPort(name = "padSize")
    private int mPadSize;
    private Program mProgram;
    private int mSliceHeight;
    private int mSliceIndex = 0;
    private int mSliceWidth;
    @GenerateFieldPort(name = "xSlices")
    private int mXSlices;
    @GenerateFieldPort(name = "ySlices")
    private int mYSlices;

    public ImageSlicer(String name) {
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

    private void calcOutputFormatForInput(Frame frame) {
        this.mInputWidth = frame.getFormat().getWidth();
        this.mInputHeight = frame.getFormat().getHeight();
        int i = this.mInputWidth;
        int i2 = this.mXSlices;
        this.mSliceWidth = ((i + i2) - 1) / i2;
        i = this.mInputHeight;
        i2 = this.mYSlices;
        this.mSliceHeight = ((i + i2) - 1) / i2;
        i = this.mSliceWidth;
        i2 = this.mPadSize;
        this.mOutputWidth = i + (i2 * 2);
        this.mOutputHeight = this.mSliceHeight + (i2 * 2);
    }

    public void process(FilterContext context) {
        int i = this.mSliceIndex;
        String str = SliceItem.FORMAT_IMAGE;
        if (i == 0) {
            this.mOriginalFrame = pullInput(str);
            calcOutputFormatForInput(this.mOriginalFrame);
        }
        MutableFrameFormat outputFormat = this.mOriginalFrame.getFormat().mutableCopy();
        outputFormat.setDimensions(this.mOutputWidth, this.mOutputHeight);
        Frame output = context.getFrameManager().newFrame(outputFormat);
        if (this.mProgram == null) {
            this.mProgram = ShaderProgram.createIdentity(context);
        }
        int ySliceIndex = this.mSliceIndex;
        int i2 = this.mXSlices;
        int xSliceIndex = ySliceIndex % i2;
        ySliceIndex /= i2;
        i2 = this.mSliceWidth * xSliceIndex;
        int i3 = this.mPadSize;
        float x0 = (float) (i2 - i3);
        int i4 = this.mInputWidth;
        x0 /= (float) i4;
        float y0 = (float) ((this.mSliceHeight * ySliceIndex) - i3);
        int i5 = this.mInputHeight;
        ((ShaderProgram) this.mProgram).setSourceRect(x0, y0 / ((float) i5), ((float) this.mOutputWidth) / ((float) i4), ((float) this.mOutputHeight) / ((float) i5));
        this.mProgram.process(this.mOriginalFrame, output);
        this.mSliceIndex++;
        if (this.mSliceIndex == this.mXSlices * this.mYSlices) {
            this.mSliceIndex = 0;
            this.mOriginalFrame.release();
            setWaitsOnInputPort(str, true);
        } else {
            this.mOriginalFrame.retain();
            setWaitsOnInputPort(str, false);
        }
        pushOutput(str, output);
        output.release();
    }
}
