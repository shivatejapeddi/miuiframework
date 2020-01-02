package android.media.effect;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterGraph;
import android.filterfw.core.GraphRunner;
import android.filterfw.core.SyncRunner;
import android.filterfw.io.GraphIOException;
import android.filterfw.io.TextGraphReader;

public class FilterGraphEffect extends FilterEffect {
    private static final String TAG = "FilterGraphEffect";
    protected FilterGraph mGraph;
    protected String mInputName;
    protected String mOutputName;
    protected GraphRunner mRunner;
    protected Class mSchedulerClass;

    public FilterGraphEffect(EffectContext context, String name, String graphString, String inputName, String outputName, Class scheduler) {
        super(context, name);
        this.mInputName = inputName;
        this.mOutputName = outputName;
        this.mSchedulerClass = scheduler;
        createGraph(graphString);
    }

    private void createGraph(String graphString) {
        String str = "Could not setup effect";
        try {
            this.mGraph = new TextGraphReader().readGraphString(graphString);
            if (this.mGraph != null) {
                this.mRunner = new SyncRunner(getFilterContext(), this.mGraph, this.mSchedulerClass);
                return;
            }
            throw new RuntimeException(str);
        } catch (GraphIOException e) {
            throw new RuntimeException(str, e);
        }
    }

    public void apply(int inputTexId, int width, int height, int outputTexId) {
        beginGLEffect();
        Filter src = this.mGraph.getFilter(this.mInputName);
        String str = "Internal error applying effect";
        if (src != null) {
            String str2 = "texId";
            src.setInputValue(str2, Integer.valueOf(inputTexId));
            src.setInputValue("width", Integer.valueOf(width));
            src.setInputValue("height", Integer.valueOf(height));
            Filter dest = this.mGraph.getFilter(this.mOutputName);
            if (dest != null) {
                dest.setInputValue(str2, Integer.valueOf(outputTexId));
                try {
                    this.mRunner.run();
                    endGLEffect();
                    return;
                } catch (RuntimeException e) {
                    throw new RuntimeException("Internal error applying effect: ", e);
                }
            }
            throw new RuntimeException(str);
        }
        throw new RuntimeException(str);
    }

    public void setParameter(String parameterKey, Object value) {
    }

    public void release() {
        this.mGraph.tearDown(getFilterContext());
        this.mGraph = null;
    }
}
