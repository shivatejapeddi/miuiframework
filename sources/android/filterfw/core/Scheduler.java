package android.filterfw.core;

public abstract class Scheduler {
    private FilterGraph mGraph;

    public abstract void reset();

    public abstract Filter scheduleNextNode();

    Scheduler(FilterGraph graph) {
        this.mGraph = graph;
    }

    /* Access modifiers changed, original: 0000 */
    public FilterGraph getGraph() {
        return this.mGraph;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean finished() {
        return true;
    }
}
