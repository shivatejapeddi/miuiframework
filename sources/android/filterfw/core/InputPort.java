package android.filterfw.core;

public abstract class InputPort extends FilterPort {
    protected OutputPort mSourcePort;

    public abstract void transfer(FilterContext filterContext);

    public InputPort(Filter filter, String name) {
        super(filter, name);
    }

    public void setSourcePort(OutputPort source) {
        if (this.mSourcePort == null) {
            this.mSourcePort = source;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this);
        stringBuilder.append(" already connected to ");
        stringBuilder.append(this.mSourcePort);
        stringBuilder.append("!");
        throw new RuntimeException(stringBuilder.toString());
    }

    public boolean isConnected() {
        return this.mSourcePort != null;
    }

    public void open() {
        super.open();
        OutputPort outputPort = this.mSourcePort;
        if (outputPort != null && !outputPort.isOpen()) {
            this.mSourcePort.open();
        }
    }

    public void close() {
        OutputPort outputPort = this.mSourcePort;
        if (outputPort != null && outputPort.isOpen()) {
            this.mSourcePort.close();
        }
        super.close();
    }

    public OutputPort getSourcePort() {
        return this.mSourcePort;
    }

    public Filter getSourceFilter() {
        OutputPort outputPort = this.mSourcePort;
        return outputPort == null ? null : outputPort.getFilter();
    }

    public FrameFormat getSourceFormat() {
        OutputPort outputPort = this.mSourcePort;
        return outputPort != null ? outputPort.getPortFormat() : getPortFormat();
    }

    public Object getTarget() {
        return null;
    }

    public boolean filterMustClose() {
        return (isOpen() || !isBlocking() || hasFrame()) ? false : true;
    }

    public boolean isReady() {
        return hasFrame() || !isBlocking();
    }

    public boolean acceptsFrame() {
        return hasFrame() ^ 1;
    }
}
