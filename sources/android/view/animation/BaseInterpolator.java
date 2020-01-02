package android.view.animation;

public abstract class BaseInterpolator implements Interpolator {
    private int mChangingConfiguration;

    public int getChangingConfiguration() {
        return this.mChangingConfiguration;
    }

    /* Access modifiers changed, original: 0000 */
    public void setChangingConfiguration(int changingConfiguration) {
        this.mChangingConfiguration = changingConfiguration;
    }
}
