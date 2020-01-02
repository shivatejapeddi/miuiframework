package android.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.InflateException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@Deprecated
abstract class GenericInflater<T, P extends Parent> {
    private static final Class[] mConstructorSignature = new Class[]{Context.class, AttributeSet.class};
    private static final HashMap sConstructorMap = new HashMap();
    private final boolean DEBUG;
    private final Object[] mConstructorArgs;
    protected final Context mContext;
    private String mDefaultPackage;
    private Factory<T> mFactory;
    private boolean mFactorySet;

    public interface Factory<T> {
        T onCreateItem(String str, Context context, AttributeSet attributeSet);
    }

    private static class FactoryMerger<T> implements Factory<T> {
        private final Factory<T> mF1;
        private final Factory<T> mF2;

        FactoryMerger(Factory<T> f1, Factory<T> f2) {
            this.mF1 = f1;
            this.mF2 = f2;
        }

        public T onCreateItem(String name, Context context, AttributeSet attrs) {
            T v = this.mF1.onCreateItem(name, context, attrs);
            if (v != null) {
                return v;
            }
            return this.mF2.onCreateItem(name, context, attrs);
        }
    }

    public interface Parent<T> {
        void addItemFromInflater(T t);
    }

    public abstract GenericInflater cloneInContext(Context context);

    protected GenericInflater(Context context) {
        this.DEBUG = false;
        this.mConstructorArgs = new Object[2];
        this.mContext = context;
    }

    protected GenericInflater(GenericInflater<T, P> original, Context newContext) {
        this.DEBUG = false;
        this.mConstructorArgs = new Object[2];
        this.mContext = newContext;
        this.mFactory = original.mFactory;
    }

    public void setDefaultPackage(String defaultPackage) {
        this.mDefaultPackage = defaultPackage;
    }

    public String getDefaultPackage() {
        return this.mDefaultPackage;
    }

    public Context getContext() {
        return this.mContext;
    }

    public final Factory<T> getFactory() {
        return this.mFactory;
    }

    public void setFactory(Factory<T> factory) {
        if (this.mFactorySet) {
            throw new IllegalStateException("A factory has already been set on this inflater");
        } else if (factory != null) {
            this.mFactorySet = true;
            Factory factory2 = this.mFactory;
            if (factory2 == null) {
                this.mFactory = factory;
            } else {
                this.mFactory = new FactoryMerger(factory, factory2);
            }
        } else {
            throw new NullPointerException("Given factory can not be null");
        }
    }

    public T inflate(int resource, P root) {
        return inflate(resource, (Parent) root, root != null);
    }

    public T inflate(XmlPullParser parser, P root) {
        return inflate(parser, (Parent) root, root != null);
    }

    public T inflate(int resource, P root, boolean attachToRoot) {
        XmlPullParser parser = getContext().getResources().getXml(resource);
        try {
            T inflate = inflate(parser, (Parent) root, attachToRoot);
            return inflate;
        } finally {
            parser.close();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0033 A:{SYNTHETIC, Splitter:B:16:0x0033} */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x001d A:{Catch:{ InflateException -> 0x0084, XmlPullParserException -> 0x0075, IOException -> 0x004e }} */
    public T inflate(org.xmlpull.v1.XmlPullParser r8, P r9, boolean r10) {
        /*
        r7 = this;
        r0 = r7.mConstructorArgs;
        monitor-enter(r0);
        r1 = android.util.Xml.asAttributeSet(r8);	 Catch:{ all -> 0x0087 }
        r2 = r7.mConstructorArgs;	 Catch:{ all -> 0x0087 }
        r3 = 0;
        r4 = r7.mContext;	 Catch:{ all -> 0x0087 }
        r2[r3] = r4;	 Catch:{ all -> 0x0087 }
        r2 = r9;
    L_0x000f:
        r3 = r8.next();	 Catch:{ InflateException -> 0x0084, XmlPullParserException -> 0x0075, IOException -> 0x004e }
        r4 = r3;
        r5 = 2;
        if (r3 == r5) goto L_0x001b;
    L_0x0017:
        r3 = 1;
        if (r4 == r3) goto L_0x001b;
    L_0x001a:
        goto L_0x000f;
    L_0x001b:
        if (r4 != r5) goto L_0x0033;
    L_0x001d:
        r3 = r8.getName();	 Catch:{ InflateException -> 0x0084, XmlPullParserException -> 0x0075, IOException -> 0x004e }
        r3 = r7.createItemFromTag(r8, r3, r1);	 Catch:{ InflateException -> 0x0084, XmlPullParserException -> 0x0075, IOException -> 0x004e }
        r5 = r3;
        r5 = (android.preference.GenericInflater.Parent) r5;	 Catch:{ InflateException -> 0x0084, XmlPullParserException -> 0x0075, IOException -> 0x004e }
        r5 = r7.onMergeRoots(r9, r10, r5);	 Catch:{ InflateException -> 0x0084, XmlPullParserException -> 0x0075, IOException -> 0x004e }
        r2 = r5;
        r7.rInflate(r8, r2, r1);	 Catch:{ InflateException -> 0x0084, XmlPullParserException -> 0x0075, IOException -> 0x004e }
        monitor-exit(r0);	 Catch:{ all -> 0x0087 }
        return r2;
    L_0x0033:
        r3 = new android.view.InflateException;	 Catch:{ InflateException -> 0x0084, XmlPullParserException -> 0x0075, IOException -> 0x004e }
        r5 = new java.lang.StringBuilder;	 Catch:{ InflateException -> 0x0084, XmlPullParserException -> 0x0075, IOException -> 0x004e }
        r5.<init>();	 Catch:{ InflateException -> 0x0084, XmlPullParserException -> 0x0075, IOException -> 0x004e }
        r6 = r8.getPositionDescription();	 Catch:{ InflateException -> 0x0084, XmlPullParserException -> 0x0075, IOException -> 0x004e }
        r5.append(r6);	 Catch:{ InflateException -> 0x0084, XmlPullParserException -> 0x0075, IOException -> 0x004e }
        r6 = ": No start tag found!";
        r5.append(r6);	 Catch:{ InflateException -> 0x0084, XmlPullParserException -> 0x0075, IOException -> 0x004e }
        r5 = r5.toString();	 Catch:{ InflateException -> 0x0084, XmlPullParserException -> 0x0075, IOException -> 0x004e }
        r3.<init>(r5);	 Catch:{ InflateException -> 0x0084, XmlPullParserException -> 0x0075, IOException -> 0x004e }
        throw r3;	 Catch:{ InflateException -> 0x0084, XmlPullParserException -> 0x0075, IOException -> 0x004e }
    L_0x004e:
        r3 = move-exception;
        r4 = new android.view.InflateException;	 Catch:{ all -> 0x0087 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0087 }
        r5.<init>();	 Catch:{ all -> 0x0087 }
        r6 = r8.getPositionDescription();	 Catch:{ all -> 0x0087 }
        r5.append(r6);	 Catch:{ all -> 0x0087 }
        r6 = ": ";
        r5.append(r6);	 Catch:{ all -> 0x0087 }
        r6 = r3.getMessage();	 Catch:{ all -> 0x0087 }
        r5.append(r6);	 Catch:{ all -> 0x0087 }
        r5 = r5.toString();	 Catch:{ all -> 0x0087 }
        r4.<init>(r5);	 Catch:{ all -> 0x0087 }
        r4.initCause(r3);	 Catch:{ all -> 0x0087 }
        throw r4;	 Catch:{ all -> 0x0087 }
    L_0x0075:
        r3 = move-exception;
        r4 = new android.view.InflateException;	 Catch:{ all -> 0x0087 }
        r5 = r3.getMessage();	 Catch:{ all -> 0x0087 }
        r4.<init>(r5);	 Catch:{ all -> 0x0087 }
        r4.initCause(r3);	 Catch:{ all -> 0x0087 }
        throw r4;	 Catch:{ all -> 0x0087 }
    L_0x0084:
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x0087 }
    L_0x0087:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0087 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.preference.GenericInflater.inflate(org.xmlpull.v1.XmlPullParser, android.preference.GenericInflater$Parent, boolean):java.lang.Object");
    }

    public final T createItem(String name, String prefix, AttributeSet attrs) throws ClassNotFoundException, InflateException {
        InflateException ie;
        Constructor constructor = (Constructor) sConstructorMap.get(name);
        String str = ": Error inflating class ";
        if (constructor == null) {
            StringBuilder stringBuilder;
            try {
                String stringBuilder2;
                Class clazz = this.mContext.getClassLoader();
                if (prefix != null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(prefix);
                    stringBuilder.append(name);
                    stringBuilder2 = stringBuilder.toString();
                } else {
                    stringBuilder2 = name;
                }
                constructor = clazz.loadClass(stringBuilder2).getConstructor(mConstructorSignature);
                constructor.setAccessible(true);
                sConstructorMap.put(name, constructor);
            } catch (NoSuchMethodException e) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(attrs.getPositionDescription());
                stringBuilder.append(str);
                if (prefix != null) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(prefix);
                    stringBuilder3.append(name);
                    str = stringBuilder3.toString();
                } else {
                    str = name;
                }
                stringBuilder.append(str);
                ie = new InflateException(stringBuilder.toString());
                ie.initCause(e);
                throw ie;
            } catch (ClassNotFoundException e2) {
                throw e2;
            } catch (Exception e3) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(attrs.getPositionDescription());
                stringBuilder.append(str);
                stringBuilder.append(constructor.getClass().getName());
                ie = new InflateException(stringBuilder.toString());
                ie.initCause(e3);
                throw ie;
            }
        }
        Object[] args = this.mConstructorArgs;
        args[1] = attrs;
        return constructor.newInstance(args);
    }

    /* Access modifiers changed, original: protected */
    public T onCreateItem(String name, AttributeSet attrs) throws ClassNotFoundException {
        return createItem(name, this.mDefaultPackage, attrs);
    }

    private final T createItemFromTag(XmlPullParser parser, String name, AttributeSet attrs) {
        InflateException e;
        StringBuilder stringBuilder;
        String str = ": Error inflating class ";
        try {
            T item = this.mFactory == null ? null : this.mFactory.onCreateItem(name, this.mContext, attrs);
            if (item != null) {
                return item;
            }
            if (-1 == name.indexOf(46)) {
                return onCreateItem(name, attrs);
            }
            return createItem(name, null, attrs);
        } catch (InflateException e2) {
            throw e2;
        } catch (ClassNotFoundException e3) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(attrs.getPositionDescription());
            stringBuilder.append(str);
            stringBuilder.append(name);
            e2 = new InflateException(stringBuilder.toString());
            e2.initCause(e3);
            throw e2;
        } catch (Exception e4) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(attrs.getPositionDescription());
            stringBuilder.append(str);
            stringBuilder.append(name);
            e2 = new InflateException(stringBuilder.toString());
            e2.initCause(e4);
            throw e2;
        }
    }

    private void rInflate(XmlPullParser parser, T parent, AttributeSet attrs) throws XmlPullParserException, IOException {
        int depth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if ((next == 3 && parser.getDepth() <= depth) || type == 1) {
                return;
            }
            if (type == 2) {
                if (!onCreateCustomFromTag(parser, parent, attrs)) {
                    T item = createItemFromTag(parser, parser.getName(), attrs);
                    ((Parent) parent).addItemFromInflater(item);
                    rInflate(parser, item, attrs);
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean onCreateCustomFromTag(XmlPullParser parser, T t, AttributeSet attrs) throws XmlPullParserException {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public P onMergeRoots(P p, boolean attachToGivenRoot, P xmlRoot) {
        return xmlRoot;
    }
}
