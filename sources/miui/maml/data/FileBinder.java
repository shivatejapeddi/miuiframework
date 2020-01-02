package miui.maml.data;

import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import miui.maml.ScreenElementRoot;
import miui.maml.util.FilenameExtFilter;
import miui.maml.util.TextFormatter;
import org.w3c.dom.Element;

public class FileBinder extends VariableBinder {
    private static final boolean DBG = false;
    private static final String LOG_TAG = "FileBinder";
    public static final String TAG_NAME = "FileBinder";
    private IndexedVariable mCountVar;
    protected TextFormatter mDirFormatter;
    private String[] mFiles;
    private String[] mFilters;
    private ArrayList<Variable> mVariables = new ArrayList();

    private static class Variable extends miui.maml.data.VariableBinder.Variable {
        public Expression mIndex;

        public Variable(Element node, Variables vars) {
            super(node, vars);
            this.mIndex = Expression.build(vars, node.getAttribute("index"));
            if (this.mIndex == null) {
                Log.e(miui.maml.data.VariableBinder.Variable.TAG_NAME, "fail to load file index expression");
            }
        }

        /* Access modifiers changed, original: protected */
        public int parseType(String type) {
            return 2;
        }
    }

    public FileBinder(Element node, ScreenElementRoot root) {
        super(node, root);
        load(node);
    }

    public void init() {
        super.init();
        refresh();
    }

    public void tick() {
        super.tick();
        updateVariables();
    }

    public void refresh() {
        super.refresh();
        File dir = new File(this.mDirFormatter.getText());
        String[] strArr = this.mFilters;
        this.mFiles = strArr == null ? dir.list() : dir.list(new FilenameExtFilter(strArr));
        int count = this.mFiles;
        count = count == 0 ? 0 : count.length;
        IndexedVariable indexedVariable = this.mCountVar;
        if (indexedVariable != null) {
            indexedVariable.set((double) count);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("file count: ");
        stringBuilder.append(count);
        Log.i("FileBinder", stringBuilder.toString());
        updateVariables();
    }

    private void load(Element node) {
        if (node == null) {
            Log.e("FileBinder", "FileBinder node is null");
            return;
        }
        String filter = node.getAttribute("filter").trim();
        this.mFilters = TextUtils.isEmpty(filter) ? null : filter.split(",");
        this.mDirFormatter = new TextFormatter(getVariables(), node.getAttribute("dir"), Expression.build(getVariables(), node.getAttribute("dirExp")));
        if (!TextUtils.isEmpty(this.mName)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.mName);
            stringBuilder.append(".count");
            this.mCountVar = new IndexedVariable(stringBuilder.toString(), getContext().mVariables, true);
        }
        loadVariables(node);
    }

    /* Access modifiers changed, original: protected */
    public Variable onLoadVariable(Element child) {
        return new Variable(child, getVariables());
    }

    /* Access modifiers changed, original: protected */
    public void addVariable(Variable v) {
        this.mVariables.add(v);
    }

    private void updateVariables() {
        int count = this.mFiles;
        count = count == 0 ? 0 : count.length;
        Iterator it = this.mVariables.iterator();
        while (it.hasNext()) {
            Variable v = (Variable) it.next();
            if (v.mIndex != null) {
                v.set(count == 0 ? null : this.mFiles[((int) v.mIndex.evaluate()) % count]);
            }
        }
    }
}
