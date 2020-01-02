package android.filterfw.core;

import android.annotation.UnsupportedAppUsage;

public abstract class FrameManager {
    private FilterContext mContext;

    @UnsupportedAppUsage
    public abstract Frame newBoundFrame(FrameFormat frameFormat, int i, long j);

    @UnsupportedAppUsage
    public abstract Frame newFrame(FrameFormat frameFormat);

    public abstract Frame releaseFrame(Frame frame);

    public abstract Frame retainFrame(Frame frame);

    @UnsupportedAppUsage
    public Frame duplicateFrame(Frame frame) {
        Frame result = newFrame(frame.getFormat());
        result.setDataFromFrame(frame);
        return result;
    }

    public Frame duplicateFrameToTarget(Frame frame, int newTarget) {
        MutableFrameFormat newFormat = frame.getFormat().mutableCopy();
        newFormat.setTarget(newTarget);
        Frame result = newFrame(newFormat);
        result.setDataFromFrame(frame);
        return result;
    }

    public FilterContext getContext() {
        return this.mContext;
    }

    public GLEnvironment getGLEnvironment() {
        FilterContext filterContext = this.mContext;
        return filterContext != null ? filterContext.getGLEnvironment() : null;
    }

    public void tearDown() {
    }

    /* Access modifiers changed, original: 0000 */
    public void setContext(FilterContext context) {
        this.mContext = context;
    }
}
