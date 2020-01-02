package android.app;

import java.util.List;

@Deprecated
public class FragmentManagerNonConfig {
    private final List<FragmentManagerNonConfig> mChildNonConfigs;
    private final List<Fragment> mFragments;

    FragmentManagerNonConfig(List<Fragment> fragments, List<FragmentManagerNonConfig> childNonConfigs) {
        this.mFragments = fragments;
        this.mChildNonConfigs = childNonConfigs;
    }

    /* Access modifiers changed, original: 0000 */
    public List<Fragment> getFragments() {
        return this.mFragments;
    }

    /* Access modifiers changed, original: 0000 */
    public List<FragmentManagerNonConfig> getChildNonConfigs() {
        return this.mChildNonConfigs;
    }
}
