package android.transition;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import android.view.ViewGroup;
import com.android.internal.R;
import com.miui.internal.search.Function;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class TransitionInflater {
    private static final Class<?>[] sConstructorSignature = new Class[]{Context.class, AttributeSet.class};
    private static final ArrayMap<String, Constructor> sConstructors = new ArrayMap();
    private Context mContext;

    private TransitionInflater(Context context) {
        this.mContext = context;
    }

    public static TransitionInflater from(Context context) {
        return new TransitionInflater(context);
    }

    public Transition inflateTransition(int resource) {
        InflateException ex;
        XmlResourceParser parser = this.mContext.getResources().getXml(resource);
        try {
            Transition createTransitionFromXml = createTransitionFromXml(parser, Xml.asAttributeSet(parser), null);
            parser.close();
            return createTransitionFromXml;
        } catch (XmlPullParserException e) {
            ex = new InflateException(e.getMessage());
            ex.initCause(e);
            throw ex;
        } catch (IOException e2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(parser.getPositionDescription());
            stringBuilder.append(": ");
            stringBuilder.append(e2.getMessage());
            ex = new InflateException(stringBuilder.toString());
            ex.initCause(e2);
            throw ex;
        } catch (Throwable th) {
            parser.close();
        }
    }

    public TransitionManager inflateTransitionManager(int resource, ViewGroup sceneRoot) {
        InflateException ex;
        XmlResourceParser parser = this.mContext.getResources().getXml(resource);
        try {
            TransitionManager createTransitionManagerFromXml = createTransitionManagerFromXml(parser, Xml.asAttributeSet(parser), sceneRoot);
            parser.close();
            return createTransitionManagerFromXml;
        } catch (XmlPullParserException e) {
            ex = new InflateException(e.getMessage());
            ex.initCause(e);
            throw ex;
        } catch (IOException e2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(parser.getPositionDescription());
            stringBuilder.append(": ");
            stringBuilder.append(e2.getMessage());
            ex = new InflateException(stringBuilder.toString());
            ex.initCause(e2);
            throw ex;
        } catch (Throwable th) {
            parser.close();
        }
    }

    private Transition createTransitionFromXml(XmlPullParser parser, AttributeSet attrs, Transition parent) throws XmlPullParserException, IOException {
        Transition transition = null;
        int depth = parser.getDepth();
        TransitionSet transitionSet = parent instanceof TransitionSet ? (TransitionSet) parent : null;
        while (true) {
            int next = parser.next();
            int type = next;
            if ((next != 3 || parser.getDepth() > depth) && type != 1) {
                if (type == 2) {
                    String name = parser.getName();
                    if ("fade".equals(name)) {
                        transition = new Fade(this.mContext, attrs);
                    } else if ("changeBounds".equals(name)) {
                        transition = new ChangeBounds(this.mContext, attrs);
                    } else if ("slide".equals(name)) {
                        transition = new Slide(this.mContext, attrs);
                    } else if ("explode".equals(name)) {
                        transition = new Explode(this.mContext, attrs);
                    } else if ("changeImageTransform".equals(name)) {
                        transition = new ChangeImageTransform(this.mContext, attrs);
                    } else if ("changeTransform".equals(name)) {
                        transition = new ChangeTransform(this.mContext, attrs);
                    } else if ("changeClipBounds".equals(name)) {
                        transition = new ChangeClipBounds(this.mContext, attrs);
                    } else if ("autoTransition".equals(name)) {
                        transition = new AutoTransition(this.mContext, attrs);
                    } else if ("recolor".equals(name)) {
                        transition = new Recolor(this.mContext, attrs);
                    } else if ("changeScroll".equals(name)) {
                        transition = new ChangeScroll(this.mContext, attrs);
                    } else if ("transitionSet".equals(name)) {
                        transition = new TransitionSet(this.mContext, attrs);
                    } else {
                        String str = "transition";
                        if (str.equals(name)) {
                            transition = (Transition) createCustom(attrs, Transition.class, str);
                        } else if ("targets".equals(name)) {
                            getTargetIds(parser, attrs, parent);
                        } else if ("arcMotion".equals(name)) {
                            parent.setPathMotion(new ArcMotion(this.mContext, attrs));
                        } else {
                            str = "pathMotion";
                            if (str.equals(name)) {
                                parent.setPathMotion((PathMotion) createCustom(attrs, PathMotion.class, str));
                            } else if ("patternPathMotion".equals(name)) {
                                parent.setPathMotion(new PatternPathMotion(this.mContext, attrs));
                            } else {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Unknown scene name: ");
                                stringBuilder.append(parser.getName());
                                throw new RuntimeException(stringBuilder.toString());
                            }
                        }
                    }
                    if (transition != null) {
                        if (!parser.isEmptyElementTag()) {
                            createTransitionFromXml(parser, attrs, transition);
                        }
                        if (transitionSet != null) {
                            transitionSet.addTransition(transition);
                            transition = null;
                        } else if (parent != null) {
                            throw new InflateException("Could not add transition to another transition.");
                        }
                    }
                }
            }
        }
        return transition;
    }

    private Object createCustom(AttributeSet attrs, Class expectedType, String tag) {
        StringBuilder stringBuilder;
        String className = attrs.getAttributeValue(null, Function.CLASS);
        if (className != null) {
            try {
                Object newInstance;
                synchronized (sConstructors) {
                    Constructor constructor = (Constructor) sConstructors.get(className);
                    if (constructor == null) {
                        Class c = this.mContext.getClassLoader().loadClass(className).asSubclass(expectedType);
                        if (c != null) {
                            constructor = c.getConstructor(sConstructorSignature);
                            constructor.setAccessible(true);
                            sConstructors.put(className, constructor);
                        }
                    }
                    newInstance = constructor.newInstance(new Object[]{this.mContext, attrs});
                }
                return newInstance;
            } catch (InstantiationException e) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Could not instantiate ");
                stringBuilder.append(expectedType);
                stringBuilder.append(" class ");
                stringBuilder.append(className);
                throw new InflateException(stringBuilder.toString(), e);
            } catch (ClassNotFoundException e2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Could not instantiate ");
                stringBuilder.append(expectedType);
                stringBuilder.append(" class ");
                stringBuilder.append(className);
                throw new InflateException(stringBuilder.toString(), e2);
            } catch (InvocationTargetException e3) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Could not instantiate ");
                stringBuilder.append(expectedType);
                stringBuilder.append(" class ");
                stringBuilder.append(className);
                throw new InflateException(stringBuilder.toString(), e3);
            } catch (NoSuchMethodException e4) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Could not instantiate ");
                stringBuilder.append(expectedType);
                stringBuilder.append(" class ");
                stringBuilder.append(className);
                throw new InflateException(stringBuilder.toString(), e4);
            } catch (IllegalAccessException e5) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Could not instantiate ");
                stringBuilder.append(expectedType);
                stringBuilder.append(" class ");
                stringBuilder.append(className);
                throw new InflateException(stringBuilder.toString(), e5);
            }
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(tag);
        stringBuilder2.append(" tag must have a 'class' attribute");
        throw new InflateException(stringBuilder2.toString());
    }

    private void getTargetIds(XmlPullParser parser, AttributeSet attrs, Transition transition) throws XmlPullParserException, IOException {
        int depth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if ((next == 3 && parser.getDepth() <= depth) || type == 1) {
                return;
            }
            if (type == 2) {
                if (parser.getName().equals("target")) {
                    TypedArray a = this.mContext.obtainStyledAttributes(attrs, R.styleable.TransitionTarget);
                    int id = a.getResourceId(1, 0);
                    if (id != 0) {
                        transition.addTarget(id);
                    } else {
                        int resourceId = a.getResourceId(2, 0);
                        id = resourceId;
                        if (resourceId != 0) {
                            transition.excludeTarget(id, true);
                        } else {
                            String string = a.getString(4);
                            String transitionName = string;
                            if (string != null) {
                                transition.addTarget(transitionName);
                            } else {
                                string = a.getString(5);
                                transitionName = string;
                                if (string != null) {
                                    transition.excludeTarget(transitionName, true);
                                } else {
                                    String className = a.getString(3);
                                    if (className != null) {
                                        try {
                                            transition.excludeTarget(Class.forName(className), true);
                                        } catch (ClassNotFoundException e) {
                                            a.recycle();
                                            StringBuilder stringBuilder = new StringBuilder();
                                            stringBuilder.append("Could not create ");
                                            stringBuilder.append(className);
                                            throw new RuntimeException(stringBuilder.toString(), e);
                                        }
                                    }
                                    String string2 = a.getString(0);
                                    className = string2;
                                    if (string2 != null) {
                                        transition.addTarget(Class.forName(className));
                                    }
                                }
                            }
                        }
                    }
                    a.recycle();
                } else {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Unknown scene name: ");
                    stringBuilder2.append(parser.getName());
                    throw new RuntimeException(stringBuilder2.toString());
                }
            }
        }
    }

    /* JADX WARNING: Missing block: B:20:0x0058, code skipped:
            return r1;
     */
    private android.transition.TransitionManager createTransitionManagerFromXml(org.xmlpull.v1.XmlPullParser r8, android.util.AttributeSet r9, android.view.ViewGroup r10) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r7 = this;
        r0 = r8.getDepth();
        r1 = 0;
    L_0x0005:
        r2 = r8.next();
        r3 = r2;
        r4 = 3;
        if (r2 != r4) goto L_0x0013;
    L_0x000d:
        r2 = r8.getDepth();
        if (r2 <= r0) goto L_0x0058;
    L_0x0013:
        r2 = 1;
        if (r3 == r2) goto L_0x0058;
    L_0x0016:
        r2 = 2;
        if (r3 == r2) goto L_0x001a;
    L_0x0019:
        goto L_0x0005;
    L_0x001a:
        r2 = r8.getName();
        r4 = "transitionManager";
        r4 = r2.equals(r4);
        if (r4 == 0) goto L_0x002e;
    L_0x0027:
        r4 = new android.transition.TransitionManager;
        r4.<init>();
        r1 = r4;
        goto L_0x003c;
    L_0x002e:
        r4 = "transition";
        r4 = r2.equals(r4);
        if (r4 == 0) goto L_0x003d;
    L_0x0037:
        if (r1 == 0) goto L_0x003d;
    L_0x0039:
        r7.loadTransition(r9, r10, r1);
    L_0x003c:
        goto L_0x0005;
    L_0x003d:
        r4 = new java.lang.RuntimeException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Unknown scene name: ";
        r5.append(r6);
        r6 = r8.getName();
        r5.append(r6);
        r5 = r5.toString();
        r4.<init>(r5);
        throw r4;
    L_0x0058:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.transition.TransitionInflater.createTransitionManagerFromXml(org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.view.ViewGroup):android.transition.TransitionManager");
    }

    private void loadTransition(AttributeSet attrs, ViewGroup sceneRoot, TransitionManager transitionManager) throws NotFoundException {
        TypedArray a = this.mContext.obtainStyledAttributes(attrs, R.styleable.TransitionManager);
        int transitionId = a.getResourceId(2, -1);
        int fromId = a.getResourceId(0, -1);
        Scene toScene = null;
        Scene fromScene = fromId < 0 ? null : Scene.getSceneForLayout(sceneRoot, fromId, this.mContext);
        int toId = a.getResourceId(1, -1);
        if (toId >= 0) {
            toScene = Scene.getSceneForLayout(sceneRoot, toId, this.mContext);
        }
        if (transitionId >= 0) {
            Transition transition = inflateTransition(transitionId);
            if (transition != null) {
                if (toScene == null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("No toScene for transition ID ");
                    stringBuilder.append(transitionId);
                    throw new RuntimeException(stringBuilder.toString());
                } else if (fromScene == null) {
                    transitionManager.setTransition(toScene, transition);
                } else {
                    transitionManager.setTransition(fromScene, toScene, transition);
                }
            }
        }
        a.recycle();
    }
}
