package miui.maml.data;

import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import miui.maml.ScreenElementRoot;
import miui.maml.data.ContentProviderBinder.Builder;
import miui.maml.data.ContentProviderBinder.QueryCompleteListener;
import miui.maml.util.TextFormatter;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class VariableBinderManager implements QueryCompleteListener {
    private static final String LOG_TAG = "VariableBinderManager";
    public static final String TAG_NAME = "VariableBinders";
    private ScreenElementRoot mRoot;
    private ArrayList<VariableBinder> mVariableBinders = new ArrayList();

    public VariableBinderManager(Element node, ScreenElementRoot root) {
        this.mRoot = root;
        if (node != null) {
            load(node, root);
        }
    }

    public void init() {
        Iterator it = this.mVariableBinders.iterator();
        while (it.hasNext()) {
            ((VariableBinder) it.next()).init();
        }
    }

    public void tick() {
        Iterator it = this.mVariableBinders.iterator();
        while (it.hasNext()) {
            ((VariableBinder) it.next()).tick();
        }
    }

    public void finish() {
        Iterator it = this.mVariableBinders.iterator();
        while (it.hasNext()) {
            ((VariableBinder) it.next()).finish();
        }
    }

    public void pause() {
        Iterator it = this.mVariableBinders.iterator();
        while (it.hasNext()) {
            ((VariableBinder) it.next()).pause();
        }
    }

    public void resume() {
        Iterator it = this.mVariableBinders.iterator();
        while (it.hasNext()) {
            ((VariableBinder) it.next()).resume();
        }
    }

    public VariableBinder findBinder(String name) {
        Iterator it = this.mVariableBinders.iterator();
        while (it.hasNext()) {
            VariableBinder binder = (VariableBinder) it.next();
            if (TextUtils.equals(name, binder.getName())) {
                return binder;
            }
        }
        return null;
    }

    public final void acceptVisitor(VariableBinderVisitor v) {
        Iterator it = this.mVariableBinders.iterator();
        while (it.hasNext()) {
            ((VariableBinder) it.next()).accept(v);
        }
    }

    private void load(Element node, ScreenElementRoot root) {
        if (node != null) {
            loadBinders(node, root);
            return;
        }
        String str = "node is null";
        Log.e(LOG_TAG, str);
        throw new NullPointerException(str);
    }

    private static VariableBinder createBinder(Element ele, ScreenElementRoot root, VariableBinderManager m) {
        String tag = ele.getTagName();
        VariableBinder ret = null;
        if (tag.equalsIgnoreCase(ContentProviderBinder.TAG_NAME)) {
            ret = new ContentProviderBinder(ele, root);
        } else if (tag.equalsIgnoreCase(WebServiceBinder.TAG_NAME)) {
            ret = new WebServiceBinder(ele, root);
        } else if (tag.equalsIgnoreCase(SensorBinder.TAG_NAME)) {
            ret = new SensorBinder(ele, root);
        } else if (tag.equalsIgnoreCase(LocationBinder.TAG_NAME)) {
            ret = new LocationBinder(ele, root);
        } else if (tag.equalsIgnoreCase(BroadcastBinder.TAG_NAME)) {
            ret = new BroadcastBinder(ele, root);
        } else if (tag.equalsIgnoreCase(FileBinder.TAG_NAME)) {
            ret = new FileBinder(ele, root);
        } else if (tag.equalsIgnoreCase(SettingsBinder.TAG_NAME)) {
            ret = new SettingsBinder(ele, root);
        }
        if (ret != null) {
            ret.setQueryCompleteListener(m);
        }
        return ret;
    }

    private void loadBinders(Element node, ScreenElementRoot root) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == (short) 1) {
                VariableBinder vb = createBinder((Element) children.item(i), root, this);
                if (vb != null) {
                    this.mVariableBinders.add(vb);
                }
            }
        }
    }

    public Builder addContentProviderBinder(String uri) {
        return addContentProviderBinder(new TextFormatter(this.mRoot.getVariables(), uri));
    }

    public Builder addContentProviderBinder(String uriFormat, String uriParas) {
        return addContentProviderBinder(new TextFormatter(this.mRoot.getVariables(), uriFormat, uriParas));
    }

    public Builder addContentProviderBinder(TextFormatter uri) {
        ContentProviderBinder binder = new ContentProviderBinder(this.mRoot);
        binder.setQueryCompleteListener(this);
        binder.mUriFormatter = uri;
        this.mVariableBinders.add(binder);
        return new Builder(binder);
    }

    public void onQueryCompleted(String name) {
        if (!TextUtils.isEmpty(name)) {
            Iterator it = this.mVariableBinders.iterator();
            while (it.hasNext()) {
                VariableBinder binder = (VariableBinder) it.next();
                String dependency = binder.getDependency();
                if (!TextUtils.isEmpty(dependency) && dependency.equals(name)) {
                    binder.startQuery();
                }
            }
        }
    }
}
