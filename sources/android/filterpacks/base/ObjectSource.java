package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.format.ObjectFormat;

public class ObjectSource extends Filter {
    private Frame mFrame;
    @GenerateFieldPort(name = "object")
    private Object mObject;
    @GenerateFinalPort(hasDefault = true, name = "format")
    private FrameFormat mOutputFormat = FrameFormat.unspecified();
    @GenerateFieldPort(hasDefault = true, name = "repeatFrame")
    boolean mRepeatFrame = false;

    public ObjectSource(String name) {
        super(name);
    }

    public void setupPorts() {
        addOutputPort("frame", this.mOutputFormat);
    }

    public void process(FilterContext context) {
        if (this.mFrame == null) {
            FrameFormat outputFormat = this.mObject;
            if (outputFormat != null) {
                this.mFrame = context.getFrameManager().newFrame(ObjectFormat.fromObject(outputFormat, 1));
                this.mFrame.setObjectValue(this.mObject);
                this.mFrame.setTimestamp(-1);
            } else {
                throw new NullPointerException("ObjectSource producing frame with no object set!");
            }
        }
        String str = "frame";
        pushOutput(str, this.mFrame);
        if (!this.mRepeatFrame) {
            closeOutputPort(str);
        }
    }

    public void tearDown(FilterContext context) {
        this.mFrame.release();
    }

    public void fieldPortValueUpdated(String name, FilterContext context) {
        if (name.equals("object")) {
            Frame frame = this.mFrame;
            if (frame != null) {
                frame.release();
                this.mFrame = null;
            }
        }
    }
}
