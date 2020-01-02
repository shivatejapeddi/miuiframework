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

public class ImageStitcher extends Filter {
    private int mImageHeight;
    private int mImageWidth;
    private int mInputHeight;
    private int mInputWidth;
    private Frame mOutputFrame;
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

    public ImageStitcher(String name) {
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

    private FrameFormat calcOutputFormatForInput(FrameFormat format) {
        MutableFrameFormat outputFormat = format.mutableCopy();
        this.mInputWidth = format.getWidth();
        this.mInputHeight = format.getHeight();
        int i = this.mInputWidth;
        int i2 = this.mPadSize;
        this.mSliceWidth = i - (i2 * 2);
        this.mSliceHeight = this.mInputHeight - (i2 * 2);
        this.mImageWidth = this.mSliceWidth * this.mXSlices;
        this.mImageHeight = this.mSliceHeight * this.mYSlices;
        outputFormat.setDimensions(this.mImageWidth, this.mImageHeight);
        return outputFormat;
    }

    public void process(FilterContext context) {
        String str = SliceItem.FORMAT_IMAGE;
        Frame input = pullInput(str);
        FrameFormat format = input.getFormat();
        if (this.mSliceIndex == 0) {
            this.mOutputFrame = context.getFrameManager().newFrame(calcOutputFormatForInput(format));
        } else if (!(format.getWidth() == this.mInputWidth && format.getHeight() == this.mInputHeight)) {
            throw new RuntimeException("Image size should not change.");
        }
        if (this.mProgram == null) {
            this.mProgram = ShaderProgram.createIdentity(context);
        }
        int i = this.mPadSize;
        float x0 = ((float) i) / ((float) this.mInputWidth);
        float y0 = ((float) i) / ((float) this.mInputHeight);
        int i2 = this.mSliceIndex;
        int i3 = this.mXSlices;
        int outputOffsetX = i2 % i3;
        int i4 = this.mSliceWidth;
        outputOffsetX *= i4;
        i2 = (i2 / i3) * this.mSliceHeight;
        float outputWidth = (float) Math.min(i4, this.mImageWidth - outputOffsetX);
        float outputHeight = (float) Math.min(this.mSliceHeight, this.mImageHeight - i2);
        ((ShaderProgram) this.mProgram).setSourceRect(x0, y0, outputWidth / ((float) this.mInputWidth), outputHeight / ((float) this.mInputHeight));
        ShaderProgram shaderProgram = (ShaderProgram) this.mProgram;
        float f = (float) outputOffsetX;
        int i5 = this.mImageWidth;
        f /= (float) i5;
        float f2 = (float) i2;
        int i6 = this.mImageHeight;
        shaderProgram.setTargetRect(f, f2 / ((float) i6), outputWidth / ((float) i5), outputHeight / ((float) i6));
        this.mProgram.process(input, this.mOutputFrame);
        this.mSliceIndex++;
        if (this.mSliceIndex == this.mXSlices * this.mYSlices) {
            pushOutput(str, this.mOutputFrame);
            this.mOutputFrame.release();
            this.mSliceIndex = 0;
        }
    }
}
