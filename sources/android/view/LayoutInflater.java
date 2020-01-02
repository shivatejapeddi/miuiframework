package android.view;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.Trace;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import com.android.internal.R;
import com.miui.internal.search.Function;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class LayoutInflater {
    @UnsupportedAppUsage
    private static final int[] ATTRS_THEME = new int[]{16842752};
    private static final String ATTR_LAYOUT = "layout";
    private static final ClassLoader BOOT_CLASS_LOADER = LayoutInflater.class.getClassLoader();
    private static final String COMPILED_VIEW_DEX_FILE_NAME = "/compiled_view.dex";
    private static final boolean DEBUG = false;
    private static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
    private static final String TAG = LayoutInflater.class.getSimpleName();
    private static final String TAG_1995 = "blink";
    private static final String TAG_INCLUDE = "include";
    private static final String TAG_MERGE = "merge";
    private static final String TAG_REQUEST_FOCUS = "requestFocus";
    private static final String TAG_TAG = "tag";
    private static final String USE_PRECOMPILED_LAYOUT = "view.precompiled_layout_enabled";
    @UnsupportedAppUsage
    static final Class<?>[] mConstructorSignature = new Class[]{Context.class, AttributeSet.class};
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769490)
    private static final HashMap<String, Constructor<? extends View>> sConstructorMap = new HashMap();
    @UnsupportedAppUsage(maxTargetSdk = 28)
    final Object[] mConstructorArgs;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    protected final Context mContext;
    @UnsupportedAppUsage
    private Factory mFactory;
    @UnsupportedAppUsage
    private Factory2 mFactory2;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private boolean mFactorySet;
    private Filter mFilter;
    private HashMap<String, Boolean> mFilterMap;
    private ClassLoader mPrecompiledClassLoader;
    @UnsupportedAppUsage
    private Factory2 mPrivateFactory;
    private TypedValue mTempValue;
    private boolean mUseCompiledView;

    private static class BlinkLayout extends FrameLayout {
        private static final int BLINK_DELAY = 500;
        private static final int MESSAGE_BLINK = 66;
        private boolean mBlink;
        private boolean mBlinkState;
        private final Handler mHandler = new Handler(new Callback() {
            public boolean handleMessage(Message msg) {
                if (msg.what != 66) {
                    return false;
                }
                if (BlinkLayout.this.mBlink) {
                    BlinkLayout blinkLayout = BlinkLayout.this;
                    blinkLayout.mBlinkState = blinkLayout.mBlinkState ^ 1;
                    BlinkLayout.this.makeBlink();
                }
                BlinkLayout.this.invalidate();
                return true;
            }
        });

        public BlinkLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        private void makeBlink() {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(66), 500);
        }

        /* Access modifiers changed, original: protected */
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.mBlink = true;
            this.mBlinkState = true;
            makeBlink();
        }

        /* Access modifiers changed, original: protected */
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.mBlink = false;
            this.mBlinkState = true;
            this.mHandler.removeMessages(66);
        }

        /* Access modifiers changed, original: protected */
        public void dispatchDraw(Canvas canvas) {
            if (this.mBlinkState) {
                super.dispatchDraw(canvas);
            }
        }
    }

    public interface Factory {
        View onCreateView(String str, Context context, AttributeSet attributeSet);
    }

    public interface Factory2 extends Factory {
        View onCreateView(View view, String str, Context context, AttributeSet attributeSet);
    }

    private static class FactoryMerger implements Factory2 {
        private final Factory mF1;
        private final Factory2 mF12;
        private final Factory mF2;
        private final Factory2 mF22;

        FactoryMerger(Factory f1, Factory2 f12, Factory f2, Factory2 f22) {
            this.mF1 = f1;
            this.mF2 = f2;
            this.mF12 = f12;
            this.mF22 = f22;
        }

        public View onCreateView(String name, Context context, AttributeSet attrs) {
            View v = this.mF1.onCreateView(name, context, attrs);
            if (v != null) {
                return v;
            }
            return this.mF2.onCreateView(name, context, attrs);
        }

        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
            View v;
            Factory2 factory2 = this.mF12;
            if (factory2 != null) {
                v = factory2.onCreateView(parent, name, context, attrs);
            } else {
                v = this.mF1.onCreateView(name, context, attrs);
            }
            if (v != null) {
                return v;
            }
            View onCreateView;
            Factory2 factory22 = this.mF22;
            if (factory22 != null) {
                onCreateView = factory22.onCreateView(parent, name, context, attrs);
            } else {
                onCreateView = this.mF2.onCreateView(name, context, attrs);
            }
            return onCreateView;
        }
    }

    public interface Filter {
        boolean onLoadClass(Class cls);
    }

    public abstract LayoutInflater cloneInContext(Context context);

    protected LayoutInflater(Context context) {
        this.mConstructorArgs = new Object[2];
        this.mContext = context;
        initPrecompiledViews();
    }

    protected LayoutInflater(LayoutInflater original, Context newContext) {
        this.mConstructorArgs = new Object[2];
        this.mContext = newContext;
        this.mFactory = original.mFactory;
        this.mFactory2 = original.mFactory2;
        this.mPrivateFactory = original.mPrivateFactory;
        setFilter(original.mFilter);
        initPrecompiledViews();
    }

    public static LayoutInflater from(Context context) {
        LayoutInflater LayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (LayoutInflater != null) {
            return LayoutInflater;
        }
        throw new AssertionError("LayoutInflater not found.");
    }

    public Context getContext() {
        return this.mContext;
    }

    public final Factory getFactory() {
        return this.mFactory;
    }

    public final Factory2 getFactory2() {
        return this.mFactory2;
    }

    public void setFactory(Factory factory) {
        if (this.mFactorySet) {
            throw new IllegalStateException("A factory has already been set on this LayoutInflater");
        } else if (factory != null) {
            this.mFactorySet = true;
            Factory factory2 = this.mFactory;
            if (factory2 == null) {
                this.mFactory = factory;
            } else {
                this.mFactory = new FactoryMerger(factory, null, factory2, this.mFactory2);
            }
        } else {
            throw new NullPointerException("Given factory can not be null");
        }
    }

    public void setFactory2(Factory2 factory) {
        if (this.mFactorySet) {
            throw new IllegalStateException("A factory has already been set on this LayoutInflater");
        } else if (factory != null) {
            this.mFactorySet = true;
            Factory factory2 = this.mFactory;
            if (factory2 == null) {
                this.mFactory2 = factory;
                this.mFactory = factory;
                return;
            }
            FactoryMerger factoryMerger = new FactoryMerger(factory, factory, factory2, this.mFactory2);
            this.mFactory2 = factoryMerger;
            this.mFactory = factoryMerger;
        } else {
            throw new NullPointerException("Given factory can not be null");
        }
    }

    @UnsupportedAppUsage
    public void setPrivateFactory(Factory2 factory) {
        Factory2 factory2 = this.mPrivateFactory;
        if (factory2 == null) {
            this.mPrivateFactory = factory;
        } else {
            this.mPrivateFactory = new FactoryMerger(factory, factory, factory2, factory2);
        }
    }

    public Filter getFilter() {
        return this.mFilter;
    }

    public void setFilter(Filter filter) {
        this.mFilter = filter;
        if (filter != null) {
            this.mFilterMap = new HashMap();
        }
    }

    private void initPrecompiledViews() {
        initPrecompiledViews(false);
    }

    private void initPrecompiledViews(boolean enablePrecompiledViews) {
        this.mUseCompiledView = enablePrecompiledViews;
        if (this.mUseCompiledView) {
            ApplicationInfo appInfo = this.mContext.getApplicationInfo();
            if (appInfo.isEmbeddedDexUsed() || appInfo.isPrivilegedApp()) {
                this.mUseCompiledView = false;
                return;
            }
            try {
                this.mPrecompiledClassLoader = this.mContext.getClassLoader();
                String dexFile = new StringBuilder();
                dexFile.append(this.mContext.getCodeCacheDir());
                dexFile.append(COMPILED_VIEW_DEX_FILE_NAME);
                dexFile = dexFile.toString();
                if (new File(dexFile).exists()) {
                    this.mPrecompiledClassLoader = new PathClassLoader(dexFile, this.mPrecompiledClassLoader);
                } else {
                    this.mUseCompiledView = false;
                }
            } catch (Throwable th) {
                this.mUseCompiledView = false;
            }
            if (!this.mUseCompiledView) {
                this.mPrecompiledClassLoader = null;
            }
            return;
        }
        this.mPrecompiledClassLoader = null;
    }

    public void setPrecompiledLayoutsEnabledForTesting(boolean enablePrecompiledLayouts) {
        initPrecompiledViews(enablePrecompiledLayouts);
    }

    public View inflate(int resource, ViewGroup root) {
        return inflate(resource, root, root != null);
    }

    public View inflate(XmlPullParser parser, ViewGroup root) {
        return inflate(parser, root, root != null);
    }

    public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
        Resources res = getContext().getResources();
        resource = LayoutInflaterMap.getResourceId(getContext(), resource);
        View view = tryInflatePrecompiled(resource, res, root, attachToRoot);
        if (view != null) {
            return view;
        }
        XmlPullParser parser = res.getLayout(resource);
        try {
            View inflate = inflate(parser, root, attachToRoot);
            return inflate;
        } finally {
            parser.close();
        }
    }

    private View tryInflatePrecompiled(int resource, Resources res, ViewGroup root, boolean attachToRoot) {
        if (!this.mUseCompiledView) {
            return null;
        }
        Trace.traceBegin(8, "inflate (precompiled)");
        String pkg = res.getResourcePackageName(resource);
        String layout = res.getResourceEntryName(resource);
        XmlResourceParser parser;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(pkg);
            stringBuilder.append(".CompiledView");
            View view = (View) Class.forName(stringBuilder.toString(), false, this.mPrecompiledClassLoader).getMethod(layout, new Class[]{Context.class, Integer.TYPE}).invoke(null, new Object[]{this.mContext, Integer.valueOf(resource)});
            if (!(view == null || root == null)) {
                parser = res.getLayout(resource);
                AttributeSet attrs = Xml.asAttributeSet(parser);
                advanceToRootNode(parser);
                LayoutParams params = root.generateLayoutParams(attrs);
                if (attachToRoot) {
                    root.addView(view, params);
                } else {
                    view.setLayoutParams(params);
                }
                parser.close();
            }
            Trace.traceEnd(8);
            return view;
        } catch (Throwable th) {
            Trace.traceEnd(8);
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x000f  */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x000e A:{RETURN} */
    private void advanceToRootNode(org.xmlpull.v1.XmlPullParser r5) throws android.view.InflateException, java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        /*
        r4 = this;
    L_0x0000:
        r0 = r5.next();
        r1 = r0;
        r2 = 2;
        if (r0 == r2) goto L_0x000c;
    L_0x0008:
        r0 = 1;
        if (r1 == r0) goto L_0x000c;
    L_0x000b:
        goto L_0x0000;
    L_0x000c:
        if (r1 != r2) goto L_0x000f;
    L_0x000e:
        return;
    L_0x000f:
        r0 = new android.view.InflateException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = r5.getPositionDescription();
        r2.append(r3);
        r3 = ": No start tag found!";
        r2.append(r3);
        r2 = r2.toString();
        r0.<init>(r2);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.LayoutInflater.advanceToRootNode(org.xmlpull.v1.XmlPullParser):void");
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:53:0x00e1=Splitter:B:53:0x00e1, B:29:0x007b=Splitter:B:29:0x007b} */
    public android.view.View inflate(org.xmlpull.v1.XmlPullParser r20, android.view.ViewGroup r21, boolean r22) {
        /*
        r19 = this;
        r7 = r19;
        r8 = r21;
        r9 = r7.mConstructorArgs;
        monitor-enter(r9);
        r0 = "inflate";
        r10 = 8;
        android.os.Trace.traceBegin(r10, r0);	 Catch:{ all -> 0x00ef }
        r0 = r7.mContext;	 Catch:{ all -> 0x00ef }
        r12 = r0;
        r0 = android.util.Xml.asAttributeSet(r20);	 Catch:{ all -> 0x00ef }
        r13 = r0;
        r0 = r7.mConstructorArgs;	 Catch:{ all -> 0x00ef }
        r14 = 0;
        r0 = r0[r14];	 Catch:{ all -> 0x00ef }
        r0 = (android.content.Context) r0;	 Catch:{ all -> 0x00ef }
        r15 = r0;
        r0 = r7.mConstructorArgs;	 Catch:{ all -> 0x00ef }
        r0[r14] = r12;	 Catch:{ all -> 0x00ef }
        r16 = r21;
        r17 = 0;
        r6 = 1;
        r19.advanceToRootNode(r20);	 Catch:{ XmlPullParserException -> 0x00cc, Exception -> 0x00a0, all -> 0x009b }
        r0 = r20.getName();	 Catch:{ XmlPullParserException -> 0x00cc, Exception -> 0x00a0, all -> 0x009b }
        r1 = "merge";
        r1 = r1.equals(r0);	 Catch:{ XmlPullParserException -> 0x00cc, Exception -> 0x00a0, all -> 0x009b }
        if (r1 == 0) goto L_0x0057;
    L_0x0037:
        if (r8 == 0) goto L_0x004e;
    L_0x0039:
        if (r22 == 0) goto L_0x004e;
    L_0x003b:
        r18 = 0;
        r1 = r19;
        r2 = r20;
        r3 = r21;
        r4 = r12;
        r5 = r13;
        r10 = r6;
        r6 = r18;
        r1.rInflate(r2, r3, r4, r5, r6);	 Catch:{ XmlPullParserException -> 0x0097, Exception -> 0x0093, all -> 0x008f }
        r3 = r20;
        goto L_0x007b;
    L_0x004e:
        r10 = r6;
        r1 = new android.view.InflateException;	 Catch:{ XmlPullParserException -> 0x0097, Exception -> 0x0093, all -> 0x008f }
        r2 = "<merge /> can be used only with a valid ViewGroup root and attachToRoot=true";
        r1.<init>(r2);	 Catch:{ XmlPullParserException -> 0x0097, Exception -> 0x0093, all -> 0x008f }
        throw r1;	 Catch:{ XmlPullParserException -> 0x0097, Exception -> 0x0093, all -> 0x008f }
    L_0x0057:
        r10 = r6;
        r1 = r7.createViewFromTag(r8, r0, r12, r13);	 Catch:{ XmlPullParserException -> 0x0097, Exception -> 0x0093, all -> 0x008f }
        r2 = 0;
        if (r8 == 0) goto L_0x0069;
    L_0x005f:
        r3 = r8.generateLayoutParams(r13);	 Catch:{ XmlPullParserException -> 0x0097, Exception -> 0x0093, all -> 0x008f }
        r2 = r3;
        if (r22 != 0) goto L_0x0069;
    L_0x0066:
        r1.setLayoutParams(r2);	 Catch:{ XmlPullParserException -> 0x0097, Exception -> 0x0093, all -> 0x008f }
    L_0x0069:
        r3 = r20;
        r7.rInflateChildren(r3, r1, r13, r10);	 Catch:{ XmlPullParserException -> 0x008d, Exception -> 0x008b }
        if (r8 == 0) goto L_0x0075;
    L_0x0070:
        if (r22 == 0) goto L_0x0075;
    L_0x0072:
        r8.addView(r1, r2);	 Catch:{ XmlPullParserException -> 0x008d, Exception -> 0x008b }
    L_0x0075:
        if (r8 == 0) goto L_0x0079;
    L_0x0077:
        if (r22 != 0) goto L_0x007b;
    L_0x0079:
        r16 = r1;
    L_0x007b:
        r0 = r7.mConstructorArgs;	 Catch:{ all -> 0x00f4 }
        r0[r14] = r15;	 Catch:{ all -> 0x00f4 }
        r0 = r7.mConstructorArgs;	 Catch:{ all -> 0x00f4 }
        r0[r10] = r17;	 Catch:{ all -> 0x00f4 }
        r1 = 8;
        android.os.Trace.traceEnd(r1);	 Catch:{ all -> 0x00f4 }
        monitor-exit(r9);	 Catch:{ all -> 0x00f4 }
        return r16;
    L_0x008b:
        r0 = move-exception;
        goto L_0x00a4;
    L_0x008d:
        r0 = move-exception;
        goto L_0x00d0;
    L_0x008f:
        r0 = move-exception;
        r3 = r20;
        goto L_0x00e1;
    L_0x0093:
        r0 = move-exception;
        r3 = r20;
        goto L_0x00a4;
    L_0x0097:
        r0 = move-exception;
        r3 = r20;
        goto L_0x00d0;
    L_0x009b:
        r0 = move-exception;
        r3 = r20;
        r10 = r6;
        goto L_0x00e1;
    L_0x00a0:
        r0 = move-exception;
        r3 = r20;
        r10 = r6;
    L_0x00a4:
        r1 = new android.view.InflateException;	 Catch:{ all -> 0x00e0 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00e0 }
        r2.<init>();	 Catch:{ all -> 0x00e0 }
        r4 = getParserStateDescription(r12, r13);	 Catch:{ all -> 0x00e0 }
        r2.append(r4);	 Catch:{ all -> 0x00e0 }
        r4 = ": ";
        r2.append(r4);	 Catch:{ all -> 0x00e0 }
        r4 = r0.getMessage();	 Catch:{ all -> 0x00e0 }
        r2.append(r4);	 Catch:{ all -> 0x00e0 }
        r2 = r2.toString();	 Catch:{ all -> 0x00e0 }
        r1.<init>(r2, r0);	 Catch:{ all -> 0x00e0 }
        r2 = EMPTY_STACK_TRACE;	 Catch:{ all -> 0x00e0 }
        r1.setStackTrace(r2);	 Catch:{ all -> 0x00e0 }
        throw r1;	 Catch:{ all -> 0x00e0 }
    L_0x00cc:
        r0 = move-exception;
        r3 = r20;
        r10 = r6;
    L_0x00d0:
        r1 = new android.view.InflateException;	 Catch:{ all -> 0x00e0 }
        r2 = r0.getMessage();	 Catch:{ all -> 0x00e0 }
        r1.<init>(r2, r0);	 Catch:{ all -> 0x00e0 }
        r2 = EMPTY_STACK_TRACE;	 Catch:{ all -> 0x00e0 }
        r1.setStackTrace(r2);	 Catch:{ all -> 0x00e0 }
        throw r1;	 Catch:{ all -> 0x00e0 }
    L_0x00e0:
        r0 = move-exception;
    L_0x00e1:
        r1 = r7.mConstructorArgs;	 Catch:{ all -> 0x00f4 }
        r1[r14] = r15;	 Catch:{ all -> 0x00f4 }
        r1 = r7.mConstructorArgs;	 Catch:{ all -> 0x00f4 }
        r1[r10] = r17;	 Catch:{ all -> 0x00f4 }
        r1 = 8;
        android.os.Trace.traceEnd(r1);	 Catch:{ all -> 0x00f4 }
        throw r0;	 Catch:{ all -> 0x00f4 }
    L_0x00ef:
        r0 = move-exception;
        r3 = r20;
    L_0x00f2:
        monitor-exit(r9);	 Catch:{ all -> 0x00f4 }
        throw r0;
    L_0x00f4:
        r0 = move-exception;
        goto L_0x00f2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.LayoutInflater.inflate(org.xmlpull.v1.XmlPullParser, android.view.ViewGroup, boolean):android.view.View");
    }

    private static String getParserStateDescription(Context context, AttributeSet attrs) {
        int sourceResId = Resources.getAttributeSetSourceResId(attrs);
        if (sourceResId == 0) {
            return attrs.getPositionDescription();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(attrs.getPositionDescription());
        stringBuilder.append(" in ");
        stringBuilder.append(context.getResources().getResourceName(sourceResId));
        return stringBuilder.toString();
    }

    private final boolean verifyClassLoader(Constructor<? extends View> constructor) {
        ClassLoader constructorLoader = constructor.getDeclaringClass().getClassLoader();
        if (constructorLoader == BOOT_CLASS_LOADER) {
            return true;
        }
        ClassLoader cl = this.mContext.getClassLoader();
        while (constructorLoader != cl) {
            cl = cl.getParent();
            if (cl == null) {
                return false;
            }
        }
        return true;
    }

    public final View createView(String name, String prefix, AttributeSet attrs) throws ClassNotFoundException, InflateException {
        Context context = this.mConstructorArgs[0];
        if (context == null) {
            context = this.mContext;
        }
        return createView(context, name, prefix, attrs);
    }

    public final View createView(Context viewContext, String name, String prefix, AttributeSet attrs) throws ClassNotFoundException, InflateException {
        InflateException ie;
        String str = ": Error inflating class ";
        Objects.requireNonNull(viewContext);
        Objects.requireNonNull(name);
        Constructor<? extends View> constructor = (Constructor) sConstructorMap.get(name);
        if (!(constructor == null || verifyClassLoader(constructor))) {
            constructor = null;
            sConstructorMap.remove(name);
        }
        Class<? extends View> clazz = null;
        StringBuilder stringBuilder;
        String stringBuilder2;
        Object lastContext;
        try {
            Trace.traceBegin(8, name);
            if (constructor == null) {
                if (prefix != null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(prefix);
                    stringBuilder.append(name);
                    stringBuilder2 = stringBuilder.toString();
                } else {
                    stringBuilder2 = name;
                }
                clazz = Class.forName(stringBuilder2, false, this.mContext.getClassLoader()).asSubclass(View.class);
                if (!(this.mFilter == null || clazz == null || this.mFilter.onLoadClass(clazz))) {
                    failNotAllowed(name, prefix, viewContext, attrs);
                }
                constructor = clazz.getConstructor(mConstructorSignature);
                constructor.setAccessible(true);
                sConstructorMap.put(name, constructor);
            } else if (this.mFilter != null) {
                Boolean allowedState = (Boolean) this.mFilterMap.get(name);
                if (allowedState == null) {
                    String stringBuilder3;
                    if (prefix != null) {
                        StringBuilder stringBuilder4 = new StringBuilder();
                        stringBuilder4.append(prefix);
                        stringBuilder4.append(name);
                        stringBuilder3 = stringBuilder4.toString();
                    } else {
                        stringBuilder3 = name;
                    }
                    clazz = Class.forName(stringBuilder3, false, this.mContext.getClassLoader()).asSubclass(View.class);
                    boolean allowed = clazz != null && this.mFilter.onLoadClass(clazz);
                    this.mFilterMap.put(name, Boolean.valueOf(allowed));
                    if (!allowed) {
                        failNotAllowed(name, prefix, viewContext, attrs);
                    }
                } else if (allowedState.equals(Boolean.FALSE)) {
                    failNotAllowed(name, prefix, viewContext, attrs);
                }
            }
            lastContext = this.mConstructorArgs[0];
            this.mConstructorArgs[0] = viewContext;
            Object[] args = this.mConstructorArgs;
            args[1] = attrs;
            View view = (View) constructor.newInstance(args);
            if (view instanceof ViewStub) {
                ((ViewStub) view).setLayoutInflater(cloneInContext((Context) args[0]));
            }
            this.mConstructorArgs[0] = lastContext;
            Trace.traceEnd(8);
            return view;
        } catch (NoSuchMethodException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(getParserStateDescription(viewContext, attrs));
            stringBuilder.append(str);
            if (prefix != null) {
                StringBuilder stringBuilder5 = new StringBuilder();
                stringBuilder5.append(prefix);
                stringBuilder5.append(name);
                str = stringBuilder5.toString();
            } else {
                str = name;
            }
            stringBuilder.append(str);
            ie = new InflateException(stringBuilder.toString(), e);
            ie.setStackTrace(EMPTY_STACK_TRACE);
            throw ie;
        } catch (ClassCastException e2) {
            StringBuilder stringBuilder6 = new StringBuilder();
            stringBuilder6.append(getParserStateDescription(viewContext, attrs));
            stringBuilder6.append(": Class is not a View ");
            if (prefix != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(prefix);
                stringBuilder.append(name);
                stringBuilder2 = stringBuilder.toString();
            } else {
                stringBuilder2 = name;
            }
            stringBuilder6.append(stringBuilder2);
            InflateException ie2 = new InflateException(stringBuilder6.toString(), e2);
            ie2.setStackTrace(EMPTY_STACK_TRACE);
            throw ie2;
        } catch (ClassNotFoundException e3) {
            throw e3;
        } catch (Exception e4) {
            try {
                stringBuilder = new StringBuilder();
                stringBuilder.append(getParserStateDescription(viewContext, attrs));
                stringBuilder.append(str);
                stringBuilder.append(clazz == null ? MediaStore.UNKNOWN_STRING : clazz.getName());
                ie = new InflateException(stringBuilder.toString(), e4);
                ie.setStackTrace(EMPTY_STACK_TRACE);
                throw ie;
            } catch (Throwable th) {
                Trace.traceEnd(8);
            }
        } catch (Throwable th2) {
            this.mConstructorArgs[0] = lastContext;
        }
    }

    private void failNotAllowed(String name, String prefix, Context context, AttributeSet attrs) {
        String stringBuilder;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(getParserStateDescription(context, attrs));
        stringBuilder2.append(": Class not allowed to be inflated ");
        if (prefix != null) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(prefix);
            stringBuilder3.append(name);
            stringBuilder = stringBuilder3.toString();
        } else {
            stringBuilder = name;
        }
        stringBuilder2.append(stringBuilder);
        throw new InflateException(stringBuilder2.toString());
    }

    /* Access modifiers changed, original: protected */
    public View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
        return createView(name, "android.view.", attrs);
    }

    /* Access modifiers changed, original: protected */
    public View onCreateView(View parent, String name, AttributeSet attrs) throws ClassNotFoundException {
        return onCreateView(name, attrs);
    }

    public View onCreateView(Context viewContext, View parent, String name, AttributeSet attrs) throws ClassNotFoundException {
        return onCreateView(parent, name, attrs);
    }

    @UnsupportedAppUsage
    private View createViewFromTag(View parent, String name, Context context, AttributeSet attrs) {
        return createViewFromTag(parent, name, context, attrs, false);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public View createViewFromTag(View parent, String name, Context context, AttributeSet attrs, boolean ignoreThemeAttr) {
        InflateException e;
        StringBuilder stringBuilder;
        String str = ": Error inflating class ";
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, Function.CLASS);
        }
        if (!ignoreThemeAttr) {
            TypedArray ta = context.obtainStyledAttributes(attrs, ATTRS_THEME);
            int themeResId = ta.getResourceId(0, 0);
            if (themeResId != 0) {
                context = new ContextThemeWrapper(context, themeResId);
            }
            ta.recycle();
        }
        Object lastContext;
        try {
            View view = tryCreateView(parent, name, context, attrs);
            if (view == null) {
                lastContext = this.mConstructorArgs[0];
                this.mConstructorArgs[0] = context;
                if (-1 == name.indexOf(46)) {
                    view = onCreateView(context, parent, name, attrs);
                } else {
                    view = createView(context, name, null, attrs);
                }
                this.mConstructorArgs[0] = lastContext;
            }
            return view;
        } catch (InflateException e2) {
            throw e2;
        } catch (ClassNotFoundException e3) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(getParserStateDescription(context, attrs));
            stringBuilder.append(str);
            stringBuilder.append(name);
            e2 = new InflateException(stringBuilder.toString(), e3);
            e2.setStackTrace(EMPTY_STACK_TRACE);
            throw e2;
        } catch (Exception e4) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(getParserStateDescription(context, attrs));
            stringBuilder.append(str);
            stringBuilder.append(name);
            e2 = new InflateException(stringBuilder.toString(), e4);
            e2.setStackTrace(EMPTY_STACK_TRACE);
            throw e2;
        } catch (Throwable th) {
            this.mConstructorArgs[0] = lastContext;
        }
    }

    @UnsupportedAppUsage(trackingBug = 122360734)
    public final View tryCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (name.equals(TAG_1995)) {
            return new BlinkLayout(context, attrs);
        }
        View view = this.mFactory2;
        if (view != null) {
            view = view.onCreateView(parent, name, context, attrs);
        } else {
            Factory factory = this.mFactory;
            if (factory != null) {
                view = factory.onCreateView(name, context, attrs);
            } else {
                view = null;
            }
        }
        if (view == null) {
            Factory2 factory2 = this.mPrivateFactory;
            if (factory2 != null) {
                view = factory2.onCreateView(parent, name, context, attrs);
            }
        }
        return view;
    }

    /* Access modifiers changed, original: final */
    public final void rInflateChildren(XmlPullParser parser, View parent, AttributeSet attrs, boolean finishInflate) throws XmlPullParserException, IOException {
        rInflate(parser, parent, parent.getContext(), attrs, finishInflate);
    }

    /* Access modifiers changed, original: 0000 */
    public void rInflate(XmlPullParser parser, View parent, Context context, AttributeSet attrs, boolean finishInflate) throws XmlPullParserException, IOException {
        int depth = parser.getDepth();
        boolean pendingRequestFocus = false;
        while (true) {
            int next = parser.next();
            int type = next;
            if ((next != 3 || parser.getDepth() > depth) && type != 1) {
                if (type == 2) {
                    String name = parser.getName();
                    if (TAG_REQUEST_FOCUS.equals(name)) {
                        pendingRequestFocus = true;
                        consumeChildElements(parser);
                    } else if ("tag".equals(name)) {
                        parseViewTag(parser, parent, attrs);
                    } else if (TAG_INCLUDE.equals(name)) {
                        if (parser.getDepth() != 0) {
                            parseInclude(parser, context, parent, attrs);
                        } else {
                            throw new InflateException("<include /> cannot be the root element");
                        }
                    } else if (TAG_MERGE.equals(name)) {
                        throw new InflateException("<merge /> must be the root element");
                    } else {
                        View view = createViewFromTag(parent, name, context, attrs);
                        ViewGroup viewGroup = (ViewGroup) parent;
                        LayoutParams params = viewGroup.generateLayoutParams(attrs);
                        rInflateChildren(parser, view, attrs, true);
                        viewGroup.addView(view, params);
                    }
                }
            }
        }
        if (pendingRequestFocus) {
            parent.restoreDefaultFocus();
        }
        if (finishInflate) {
            parent.onFinishInflate();
        }
    }

    private void parseViewTag(XmlPullParser parser, View view, AttributeSet attrs) throws XmlPullParserException, IOException {
        TypedArray ta = view.getContext().obtainStyledAttributes(attrs, R.styleable.ViewTag);
        view.setTag(ta.getResourceId(1, 0), ta.getText(0));
        ta.recycle();
        consumeChildElements(parser);
    }

    /* JADX WARNING: Removed duplicated region for block: B:82:0x0151  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a5 A:{Catch:{ all -> 0x0174 }} */
    @android.annotation.UnsupportedAppUsage
    private void parseInclude(org.xmlpull.v1.XmlPullParser r23, android.content.Context r24, android.view.View r25, android.util.AttributeSet r26) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r22 = this;
        r7 = r22;
        r0 = r24;
        r8 = r25;
        r9 = r26;
        r1 = r8 instanceof android.view.ViewGroup;
        if (r1 == 0) goto L_0x01a3;
    L_0x000c:
        r1 = ATTRS_THEME;
        r10 = r0.obtainStyledAttributes(r9, r1);
        r11 = 0;
        r12 = r10.getResourceId(r11, r11);
        r13 = 1;
        if (r12 == 0) goto L_0x001c;
    L_0x001a:
        r1 = r13;
        goto L_0x001d;
    L_0x001c:
        r1 = r11;
    L_0x001d:
        r14 = r1;
        if (r14 == 0) goto L_0x0028;
    L_0x0020:
        r1 = new android.view.ContextThemeWrapper;
        r1.<init>(r0, r12);
        r0 = r1;
        r15 = r0;
        goto L_0x0029;
    L_0x0028:
        r15 = r0;
    L_0x0029:
        r10.recycle();
        r0 = "layout";
        r1 = 0;
        r2 = r9.getAttributeResourceValue(r1, r0, r11);
        if (r2 != 0) goto L_0x005c;
    L_0x0035:
        r3 = r9.getAttributeValue(r1, r0);
        if (r3 == 0) goto L_0x0054;
    L_0x003b:
        r4 = r3.length();
        if (r4 <= 0) goto L_0x0054;
    L_0x0041:
        r4 = r15.getResources();
        r5 = r3.substring(r13);
        r6 = r15.getPackageName();
        r1 = "attr";
        r2 = r4.getIdentifier(r5, r1, r6);
        goto L_0x005c;
    L_0x0054:
        r0 = new android.view.InflateException;
        r1 = "You must specify a layout in the include tag: <include layout=\"@layout/layoutID\" />";
        r0.<init>(r1);
        throw r0;
    L_0x005c:
        r1 = r7.mTempValue;
        if (r1 != 0) goto L_0x0067;
    L_0x0060:
        r1 = new android.util.TypedValue;
        r1.<init>();
        r7.mTempValue = r1;
    L_0x0067:
        if (r2 == 0) goto L_0x007b;
    L_0x0069:
        r1 = r15.getTheme();
        r3 = r7.mTempValue;
        r1 = r1.resolveAttribute(r2, r3, r13);
        if (r1 == 0) goto L_0x007b;
    L_0x0075:
        r1 = r7.mTempValue;
        r2 = r1.resourceId;
        r6 = r2;
        goto L_0x007c;
    L_0x007b:
        r6 = r2;
    L_0x007c:
        if (r6 == 0) goto L_0x0182;
    L_0x007e:
        r0 = r15.getResources();
        r1 = r8;
        r1 = (android.view.ViewGroup) r1;
        r16 = r7.tryInflatePrecompiled(r6, r0, r1, r13);
        if (r16 != 0) goto L_0x017c;
    L_0x008b:
        r0 = r15.getResources();
        r5 = r0.getLayout(r6);
        r0 = android.util.Xml.asAttributeSet(r5);	 Catch:{ all -> 0x0174 }
        r4 = r0;
    L_0x0098:
        r0 = r5.next();	 Catch:{ all -> 0x0174 }
        r3 = r0;
        r2 = 2;
        if (r0 == r2) goto L_0x00a3;
    L_0x00a0:
        if (r3 == r13) goto L_0x00a3;
    L_0x00a2:
        goto L_0x0098;
    L_0x00a3:
        if (r3 != r2) goto L_0x0151;
    L_0x00a5:
        r0 = r5.getName();	 Catch:{ all -> 0x0174 }
        r1 = r0;
        r0 = "merge";
        r0 = r0.equals(r1);	 Catch:{ all -> 0x0174 }
        if (r0 == 0) goto L_0x00ce;
    L_0x00b3:
        r0 = 0;
        r17 = r1;
        r1 = r22;
        r2 = r5;
        r18 = r3;
        r3 = r25;
        r11 = r4;
        r4 = r15;
        r13 = r5;
        r5 = r11;
        r19 = r6;
        r6 = r0;
        r1.rInflate(r2, r3, r4, r5, r6);	 Catch:{ all -> 0x00ca }
        r4 = r13;
        goto L_0x0149;
    L_0x00ca:
        r0 = move-exception;
        r4 = r13;
        goto L_0x0178;
    L_0x00ce:
        r17 = r1;
        r18 = r3;
        r19 = r6;
        r6 = r5;
        r5 = r4;
        r1 = r22;
        r4 = r2;
        r2 = r25;
        r3 = r17;
        r4 = r15;
        r24 = r5;
        r20 = r6;
        r6 = r14;
        r0 = r1.createViewFromTag(r2, r3, r4, r5, r6);	 Catch:{ all -> 0x014d }
        r1 = r0;
        r0 = r8;
        r0 = (android.view.ViewGroup) r0;	 Catch:{ all -> 0x014d }
        r2 = r0;
        r0 = com.android.internal.R.styleable.Include;	 Catch:{ all -> 0x014d }
        r0 = r15.obtainStyledAttributes(r9, r0);	 Catch:{ all -> 0x014d }
        r3 = r0;
        r4 = -1;
        r0 = r3.getResourceId(r11, r4);	 Catch:{ all -> 0x014d }
        r5 = r0;
        r0 = r3.getInt(r13, r4);	 Catch:{ all -> 0x014d }
        r6 = r0;
        r3.recycle();	 Catch:{ all -> 0x014d }
        r21 = 0;
        r0 = r2.generateLayoutParams(r9);	 Catch:{ RuntimeException -> 0x010f }
        r21 = r0;
        goto L_0x0110;
    L_0x010a:
        r0 = move-exception;
        r4 = r20;
        goto L_0x0178;
    L_0x010f:
        r0 = move-exception;
    L_0x0110:
        if (r21 != 0) goto L_0x011b;
    L_0x0112:
        r11 = r24;
        r0 = r2.generateLayoutParams(r11);	 Catch:{ all -> 0x010a }
        r21 = r0;
        goto L_0x011f;
    L_0x011b:
        r11 = r24;
        r0 = r21;
    L_0x011f:
        r1.setLayoutParams(r0);	 Catch:{ all -> 0x014d }
        r4 = r20;
        r7.rInflateChildren(r4, r1, r11, r13);	 Catch:{ all -> 0x0172 }
        r13 = -1;
        if (r5 == r13) goto L_0x012d;
    L_0x012a:
        r1.setId(r5);	 Catch:{ all -> 0x0172 }
    L_0x012d:
        if (r6 == 0) goto L_0x0141;
    L_0x012f:
        r13 = 1;
        if (r6 == r13) goto L_0x013c;
    L_0x0132:
        r13 = 2;
        if (r6 == r13) goto L_0x0136;
    L_0x0135:
        goto L_0x0146;
    L_0x0136:
        r13 = 8;
        r1.setVisibility(r13);	 Catch:{ all -> 0x0172 }
        goto L_0x0146;
    L_0x013c:
        r13 = 4;
        r1.setVisibility(r13);	 Catch:{ all -> 0x0172 }
        goto L_0x0146;
    L_0x0141:
        r13 = 0;
        r1.setVisibility(r13);	 Catch:{ all -> 0x0172 }
    L_0x0146:
        r2.addView(r1);	 Catch:{ all -> 0x0172 }
    L_0x0149:
        r4.close();
        goto L_0x017e;
    L_0x014d:
        r0 = move-exception;
        r4 = r20;
        goto L_0x0178;
    L_0x0151:
        r18 = r3;
        r11 = r4;
        r4 = r5;
        r19 = r6;
        r0 = new android.view.InflateException;	 Catch:{ all -> 0x0172 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0172 }
        r1.<init>();	 Catch:{ all -> 0x0172 }
        r2 = getParserStateDescription(r15, r11);	 Catch:{ all -> 0x0172 }
        r1.append(r2);	 Catch:{ all -> 0x0172 }
        r2 = ": No start tag found!";
        r1.append(r2);	 Catch:{ all -> 0x0172 }
        r1 = r1.toString();	 Catch:{ all -> 0x0172 }
        r0.<init>(r1);	 Catch:{ all -> 0x0172 }
        throw r0;	 Catch:{ all -> 0x0172 }
    L_0x0172:
        r0 = move-exception;
        goto L_0x0178;
    L_0x0174:
        r0 = move-exception;
        r4 = r5;
        r19 = r6;
    L_0x0178:
        r4.close();
        throw r0;
    L_0x017c:
        r19 = r6;
    L_0x017e:
        consumeChildElements(r23);
        return;
    L_0x0182:
        r1 = 0;
        r0 = r9.getAttributeValue(r1, r0);
        r1 = new android.view.InflateException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "You must specify a valid layout reference. The layout ID ";
        r2.append(r3);
        r2.append(r0);
        r3 = " is not valid.";
        r2.append(r3);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
    L_0x01a3:
        r1 = new android.view.InflateException;
        r2 = "<include /> can only be used inside of a ViewGroup";
        r1.<init>(r2);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.LayoutInflater.parseInclude(org.xmlpull.v1.XmlPullParser, android.content.Context, android.view.View, android.util.AttributeSet):void");
    }

    static final void consumeChildElements(XmlPullParser parser) throws XmlPullParserException, IOException {
        int currentDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if ((next == 3 && parser.getDepth() <= currentDepth) || type == 1) {
                return;
            }
        }
    }
}
