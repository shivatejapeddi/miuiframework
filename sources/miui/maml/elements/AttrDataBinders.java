package miui.maml.elements;

import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import miui.maml.data.ContextVariables;
import miui.maml.util.Utils;
import miui.maml.util.Utils.XmlTraverseListener;
import org.w3c.dom.Element;

public class AttrDataBinders {
    private static final String ATTR_BITMAP = "bitmap";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PARAMS = "params";
    private static final String ATTR_PARAS = "paras";
    private static final String ATTR_SRC = "src";
    private static final String ATTR_SRCID = "srcid";
    private static final String ATTR_TEXT = "text";
    private static final String LOG_TAG = "AttrDataBinders";
    public static final String TAG = "AttrDataBinders";
    private ArrayList<AttrDataBinder> mBinders = new ArrayList();
    protected ContextVariables mVars;

    public static class AttrDataBinder {
        protected String mAttr;
        private Binder mBinder = createBinder(this.mAttr);
        protected String mData;
        protected String mTarget;
        protected ContextVariables mVars;

        private abstract class Binder {
            public abstract void bind(ScreenElement screenElement);

            private Binder() {
            }

            /* synthetic */ Binder(AttrDataBinder x0, AnonymousClass1 x1) {
                this();
            }
        }

        private class BitmapBinder extends Binder {
            private BitmapBinder() {
                super(AttrDataBinder.this, null);
            }

            /* synthetic */ BitmapBinder(AttrDataBinder x0, AnonymousClass1 x1) {
                this();
            }

            public void bind(ScreenElement e) {
                ((ImageScreenElement) e).setBitmap(AttrDataBinder.this.mVars.getBmp(AttrDataBinder.this.mData));
            }
        }

        private class NameBinder extends Binder {
            private NameBinder() {
                super(AttrDataBinder.this, null);
            }

            /* synthetic */ NameBinder(AttrDataBinder x0, AnonymousClass1 x1) {
                this();
            }

            public void bind(ScreenElement e) {
                e.setName(AttrDataBinder.this.mVars.getString(AttrDataBinder.this.mData));
            }
        }

        private class ParamsBinder extends Binder {
            private ParamsBinder() {
                super(AttrDataBinder.this, null);
            }

            /* synthetic */ ParamsBinder(AttrDataBinder x0, AnonymousClass1 x1) {
                this();
            }

            public void bind(ScreenElement e) {
                ((TextScreenElement) e).setParams(AttrDataBinder.this.mVars.getVar(AttrDataBinder.this.mData));
            }
        }

        private class SrcBinder extends Binder {
            private SrcBinder() {
                super(AttrDataBinder.this, null);
            }

            /* synthetic */ SrcBinder(AttrDataBinder x0, AnonymousClass1 x1) {
                this();
            }

            public void bind(ScreenElement e) {
                ((ImageScreenElement) e).setSrc(AttrDataBinder.this.mVars.getString(AttrDataBinder.this.mData));
            }
        }

        private class SrcIdBinder extends Binder {
            private SrcIdBinder() {
                super(AttrDataBinder.this, null);
            }

            /* synthetic */ SrcIdBinder(AttrDataBinder x0, AnonymousClass1 x1) {
                this();
            }

            public void bind(ScreenElement e) {
                Double d = AttrDataBinder.this.mVars.getDouble(AttrDataBinder.this.mData);
                ((ImageScreenElement) e).setSrcId(d == null ? 0.0d : d.doubleValue());
            }
        }

        private class TextBinder extends Binder {
            private TextBinder() {
                super(AttrDataBinder.this, null);
            }

            /* synthetic */ TextBinder(AttrDataBinder x0, AnonymousClass1 x1) {
                this();
            }

            public void bind(ScreenElement e) {
                ((TextScreenElement) e).setText(AttrDataBinder.this.mVars.getString(AttrDataBinder.this.mData));
            }
        }

        public AttrDataBinder(Element node, ContextVariables v) {
            this.mTarget = node.getAttribute("target");
            this.mAttr = node.getAttribute("attr");
            this.mData = node.getAttribute("data");
            this.mVars = v;
            if (TextUtils.isEmpty(this.mTarget) || TextUtils.isEmpty(this.mAttr) || TextUtils.isEmpty(this.mData) || this.mBinder == null) {
                throw new IllegalArgumentException("invalid AttrDataBinder");
            }
        }

        private Binder createBinder(String attr) {
            if (TextUtils.isEmpty(attr)) {
                return null;
            }
            if ("text".equals(attr)) {
                return new TextBinder(this, null);
            }
            if (AttrDataBinders.ATTR_PARAS.equals(attr) || AttrDataBinders.ATTR_PARAMS.equals(attr)) {
                return new ParamsBinder(this, null);
            }
            if ("name".equals(attr)) {
                return new NameBinder(this, null);
            }
            if (AttrDataBinders.ATTR_BITMAP.equals(attr)) {
                return new BitmapBinder(this, null);
            }
            if (AttrDataBinders.ATTR_SRC.equals(attr)) {
                return new SrcBinder(this, null);
            }
            if (AttrDataBinders.ATTR_SRCID.equals(attr)) {
                return new SrcIdBinder(this, null);
            }
            return null;
        }

        public boolean bind(ElementGroup g) {
            try {
                ScreenElement se = g.findElement(this.mTarget);
                if (se != null) {
                    this.mBinder.bind(se);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public AttrDataBinders(Element node, ContextVariables v) {
        this.mVars = v;
        Utils.traverseXmlElementChildren(node, "AttrDataBinder", new XmlTraverseListener() {
            public void onChild(Element child) {
                try {
                    AttrDataBinders.this.mBinders.add(new AttrDataBinder(child, AttrDataBinders.this.mVars));
                } catch (IllegalArgumentException e) {
                    Log.e("AttrDataBinders", e.toString());
                }
            }
        });
    }

    public void bind(ElementGroup s) {
        Iterator it = this.mBinders.iterator();
        while (it.hasNext()) {
            ((AttrDataBinder) it.next()).bind(s);
        }
    }
}
