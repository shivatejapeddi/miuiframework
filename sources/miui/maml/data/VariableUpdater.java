package miui.maml.data;

import miui.maml.ScreenContext;
import miui.maml.ScreenElementRoot;

public class VariableUpdater {
    private VariableUpdaterManager mVariableUpdaterManager;

    public VariableUpdater(VariableUpdaterManager m) {
        this.mVariableUpdaterManager = m;
    }

    /* Access modifiers changed, original: protected|final */
    public final ScreenElementRoot getRoot() {
        return this.mVariableUpdaterManager.getRoot();
    }

    /* Access modifiers changed, original: protected|final */
    public final ScreenContext getContext() {
        return getRoot().getContext();
    }

    public void init() {
    }

    public void tick(long currentTime) {
    }

    public void resume() {
    }

    public void pause() {
    }

    public void finish() {
    }
}
