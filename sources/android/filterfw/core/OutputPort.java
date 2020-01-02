package android.filterfw.core;

public class OutputPort extends FilterPort {
    protected InputPort mBasePort;
    protected InputPort mTargetPort;

    public OutputPort(Filter filter, String name) {
        super(filter, name);
    }

    public void connectTo(InputPort target) {
        if (this.mTargetPort == null) {
            this.mTargetPort = target;
            this.mTargetPort.setSourcePort(this);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this);
        stringBuilder.append(" already connected to ");
        stringBuilder.append(this.mTargetPort);
        stringBuilder.append("!");
        throw new RuntimeException(stringBuilder.toString());
    }

    public boolean isConnected() {
        return this.mTargetPort != null;
    }

    public void open() {
        super.open();
        InputPort inputPort = this.mTargetPort;
        if (inputPort != null && !inputPort.isOpen()) {
            this.mTargetPort.open();
        }
    }

    public void close() {
        super.close();
        InputPort inputPort = this.mTargetPort;
        if (inputPort != null && inputPort.isOpen()) {
            this.mTargetPort.close();
        }
    }

    public InputPort getTargetPort() {
        return this.mTargetPort;
    }

    public Filter getTargetFilter() {
        InputPort inputPort = this.mTargetPort;
        return inputPort == null ? null : inputPort.getFilter();
    }

    public void setBasePort(InputPort basePort) {
        this.mBasePort = basePort;
    }

    public InputPort getBasePort() {
        return this.mBasePort;
    }

    public boolean filterMustClose() {
        return !isOpen() && isBlocking();
    }

    public boolean isReady() {
        return (isOpen() && this.mTargetPort.acceptsFrame()) || !isBlocking();
    }

    public void clear() {
        InputPort inputPort = this.mTargetPort;
        if (inputPort != null) {
            inputPort.clear();
        }
    }

    public void pushFrame(Frame frame) {
        InputPort inputPort = this.mTargetPort;
        if (inputPort != null) {
            inputPort.pushFrame(frame);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Attempting to push frame on unconnected port: ");
        stringBuilder.append(this);
        stringBuilder.append("!");
        throw new RuntimeException(stringBuilder.toString());
    }

    public void setFrame(Frame frame) {
        assertPortIsOpen();
        InputPort inputPort = this.mTargetPort;
        if (inputPort != null) {
            inputPort.setFrame(frame);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Attempting to set frame on unconnected port: ");
        stringBuilder.append(this);
        stringBuilder.append("!");
        throw new RuntimeException(stringBuilder.toString());
    }

    public Frame pullFrame() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot pull frame on ");
        stringBuilder.append(this);
        stringBuilder.append("!");
        throw new RuntimeException(stringBuilder.toString());
    }

    public boolean hasFrame() {
        InputPort inputPort = this.mTargetPort;
        return inputPort == null ? false : inputPort.hasFrame();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("output ");
        stringBuilder.append(super.toString());
        return stringBuilder.toString();
    }
}
