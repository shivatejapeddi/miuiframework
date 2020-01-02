package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;

public class FrameFetch extends Filter {
    @GenerateFinalPort(hasDefault = true, name = "format")
    private FrameFormat mFormat;
    @GenerateFieldPort(name = "key")
    private String mKey;
    @GenerateFieldPort(hasDefault = true, name = "repeatFrame")
    private boolean mRepeatFrame = false;

    public FrameFetch(String name) {
        super(name);
    }

    public void setupPorts() {
        FrameFormat frameFormat = this.mFormat;
        if (frameFormat == null) {
            frameFormat = FrameFormat.unspecified();
        }
        addOutputPort("frame", frameFormat);
    }

    public void process(FilterContext context) {
        Frame output = context.fetchFrame(this.mKey);
        if (output != null) {
            String str = "frame";
            pushOutput(str, output);
            if (!this.mRepeatFrame) {
                closeOutputPort(str);
                return;
            }
            return;
        }
        delayNextProcess(250);
    }
}
