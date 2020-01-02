package miui.maml.util;

import miui.maml.data.IndexedVariable;
import miui.maml.data.Variables;

@Deprecated
public class IndexedNumberVariable extends IndexedVariable {
    public IndexedNumberVariable(String name, Variables vars) {
        super(name, vars, true);
    }
}
