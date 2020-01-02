package miui.maml.util;

import miui.maml.data.IndexedVariable;
import miui.maml.data.Variables;

@Deprecated
public class IndexedStringVariable extends IndexedVariable {
    public IndexedStringVariable(String name, Variables vars) {
        super(name, vars, false);
    }
}
