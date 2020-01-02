package android.filterfw.core;

public class SimpleFrameManager extends FrameManager {
    public Frame newFrame(FrameFormat format) {
        return createNewFrame(format);
    }

    public Frame newBoundFrame(FrameFormat format, int bindingType, long bindingId) {
        if (format.getTarget() == 3) {
            Frame gLFrame = new GLFrame(format, this, bindingType, bindingId);
            gLFrame.init(getGLEnvironment());
            return gLFrame;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Attached frames are not supported for target type: ");
        stringBuilder.append(FrameFormat.targetToString(format.getTarget()));
        stringBuilder.append("!");
        throw new RuntimeException(stringBuilder.toString());
    }

    private Frame createNewFrame(FrameFormat format) {
        int target = format.getTarget();
        if (target == 1) {
            return new SimpleFrame(format, this);
        }
        if (target == 2) {
            return new NativeFrame(format, this);
        }
        if (target == 3) {
            Frame glFrame = new GLFrame(format, this);
            glFrame.init(getGLEnvironment());
            return glFrame;
        } else if (target == 4) {
            return new VertexFrame(format, this);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported frame target type: ");
            stringBuilder.append(FrameFormat.targetToString(format.getTarget()));
            stringBuilder.append("!");
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    public Frame retainFrame(Frame frame) {
        frame.incRefCount();
        return frame;
    }

    public Frame releaseFrame(Frame frame) {
        int refCount = frame.decRefCount();
        if (refCount == 0 && frame.hasNativeAllocation()) {
            frame.releaseNativeAllocation();
            return null;
        } else if (refCount >= 0) {
            return frame;
        } else {
            throw new RuntimeException("Frame reference count dropped below 0!");
        }
    }
}
