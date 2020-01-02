package android.graphics.drawable;

import android.content.res.Resources;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;

public class ListDrawable extends DrawableContainer {
    private final ListState mListState;
    private Resources mResources;

    private static final class ListState extends DrawableContainerState {
        ListState(ListState orig, ListDrawable owner, Resources res) {
            super(orig, owner, res);
        }

        public Drawable newDrawable() {
            return new ListDrawable(this, null);
        }

        public Drawable newDrawable(Resources res) {
            return new ListDrawable(this, res);
        }
    }

    public ListDrawable(ListState orig, Resources res) {
        this.mListState = new ListState(orig, this, res);
        setConstantState(this.mListState);
        if (res != null) {
            this.mResources = res;
            enableFade(true);
        }
    }

    public void addDrawable(int id) {
        Resources resources = this.mResources;
        if (resources != null) {
            this.mListState.addChild(resources.getDrawable(id));
        }
    }

    public void enableFade(boolean enable) {
        if (enable) {
            setEnterFadeDuration(this.mResources.getInteger(17694720));
            setExitFadeDuration(this.mResources.getInteger(17694721));
            return;
        }
        setEnterFadeDuration(1);
        setExitFadeDuration(1);
    }

    /* Access modifiers changed, original: protected */
    public boolean onLevelChange(int level) {
        if (selectDrawable(level)) {
            return true;
        }
        return super.onLevelChange(level);
    }
}
