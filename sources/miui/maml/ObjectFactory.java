package miui.maml;

import miui.maml.elements.BitmapProvider;
import miui.maml.elements.ScreenElement;
import org.w3c.dom.Element;

public abstract class ObjectFactory {

    public static abstract class ObjectFactoryBase<T extends ObjectFactory> extends ObjectFactory {
        private String mName;
        protected T mOld;

        protected ObjectFactoryBase(String name) {
            this.mName = name;
        }

        public T getOld() {
            return this.mOld;
        }

        public void setOld(ObjectFactory f) {
            this.mOld = f;
        }

        public String getName() {
            return this.mName;
        }
    }

    public static abstract class ActionCommandFactory extends ObjectFactoryBase<ActionCommandFactory> {
        public static final String NAME = "ActionCommand";

        public abstract ActionCommand doCreate(ScreenElement screenElement, Element element);

        protected ActionCommandFactory() {
            super("ActionCommand");
        }

        public final ActionCommand create(ScreenElement screenElement, Element ele) {
            ActionCommand ret = doCreate(screenElement, ele);
            if (ret != null) {
                return ret;
            }
            return this.mOld == null ? null : ((ActionCommandFactory) this.mOld).create(screenElement, ele);
        }
    }

    public static abstract class BitmapProviderFactory extends ObjectFactoryBase<BitmapProviderFactory> {
        public static final String NAME = "BitmapProvider";

        public abstract BitmapProvider doCreate(ScreenElementRoot screenElementRoot, String str);

        protected BitmapProviderFactory() {
            super(NAME);
        }

        public final BitmapProvider create(ScreenElementRoot root, String type) {
            BitmapProvider ret = doCreate(root, type);
            if (ret != null) {
                return ret;
            }
            return this.mOld == null ? null : ((BitmapProviderFactory) this.mOld).create(root, type);
        }
    }

    public abstract String getName();

    public abstract ObjectFactory getOld();

    public abstract void setOld(ObjectFactory objectFactory);
}
