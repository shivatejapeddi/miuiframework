package miui.maml;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.ContextThemeWrapper;
import java.util.HashMap;
import miui.maml.data.ContextVariables;
import miui.maml.data.Variables;
import miui.maml.elements.ScreenElementFactory;

public class ScreenContext {
    public final Context mContext;
    public final ContextVariables mContextVariables;
    public final ScreenElementFactory mFactory;
    private final Handler mHandler;
    private HashMap<String, ObjectFactory> mObjectFactories;
    public final ResourceManager mResourceManager;
    public final Variables mVariables;

    public ScreenContext(Context context, ResourceManager resourceMgr) {
        this(context, resourceMgr, new ScreenElementFactory());
    }

    public ScreenContext(Context context, ResourceLoader loader) {
        this(context, loader, new ScreenElementFactory());
    }

    public ScreenContext(Context context, ResourceLoader loader, ScreenElementFactory factory) {
        this(context, new ResourceManager(loader), factory);
    }

    public ScreenContext(Context context, ResourceManager resourceMgr, ScreenElementFactory factory) {
        this(context, resourceMgr, factory, new Variables());
    }

    public ScreenContext(Context context, ResourceManager resourceMgr, ScreenElementFactory factory, Variables v) {
        Context rawContext = context.getApplicationContext();
        rawContext = rawContext != null ? rawContext : context;
        int themeResId = context.getThemeResId();
        if (themeResId != 0) {
            this.mContext = new ContextThemeWrapper(rawContext, themeResId);
        } else {
            this.mContext = rawContext;
        }
        this.mResourceManager = resourceMgr;
        this.mFactory = factory;
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mVariables = v;
        this.mContextVariables = new ContextVariables();
    }

    public boolean postDelayed(Runnable r, long delayMillis) {
        return this.mHandler.postDelayed(r, delayMillis);
    }

    public void removeCallbacks(Runnable r) {
        this.mHandler.removeCallbacks(r);
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    /* JADX WARNING: Missing block: B:7:0x000d, code skipped:
            return;
     */
    public final synchronized void registerObjectFactory(java.lang.String r4, miui.maml.ObjectFactory r5) {
        /*
        r3 = this;
        monitor-enter(r3);
        if (r5 != 0) goto L_0x000e;
    L_0x0003:
        r0 = r3.mObjectFactories;	 Catch:{ all -> 0x0059 }
        if (r0 == 0) goto L_0x000c;
    L_0x0007:
        r0 = r3.mObjectFactories;	 Catch:{ all -> 0x0059 }
        r0.remove(r4);	 Catch:{ all -> 0x0059 }
    L_0x000c:
        monitor-exit(r3);
        return;
    L_0x000e:
        r0 = r5.getName();	 Catch:{ all -> 0x0059 }
        r0 = r4.equals(r0);	 Catch:{ all -> 0x0059 }
        if (r0 == 0) goto L_0x0042;
    L_0x0018:
        r0 = r3.mObjectFactories;	 Catch:{ all -> 0x0059 }
        if (r0 != 0) goto L_0x0023;
    L_0x001c:
        r0 = new java.util.HashMap;	 Catch:{ all -> 0x0059 }
        r0.<init>();	 Catch:{ all -> 0x0059 }
        r3.mObjectFactories = r0;	 Catch:{ all -> 0x0059 }
    L_0x0023:
        r0 = r3.mObjectFactories;	 Catch:{ all -> 0x0059 }
        r0 = r0.get(r4);	 Catch:{ all -> 0x0059 }
        r0 = (miui.maml.ObjectFactory) r0;	 Catch:{ all -> 0x0059 }
        r1 = r0;
    L_0x002c:
        if (r1 == 0) goto L_0x0038;
    L_0x002e:
        if (r1 != r5) goto L_0x0032;
    L_0x0030:
        monitor-exit(r3);
        return;
    L_0x0032:
        r2 = r1.getOld();	 Catch:{ all -> 0x0059 }
        r1 = r2;
        goto L_0x002c;
    L_0x0038:
        r5.setOld(r0);	 Catch:{ all -> 0x0059 }
        r2 = r3.mObjectFactories;	 Catch:{ all -> 0x0059 }
        r2.put(r4, r5);	 Catch:{ all -> 0x0059 }
        monitor-exit(r3);
        return;
    L_0x0042:
        r0 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x0059 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0059 }
        r1.<init>();	 Catch:{ all -> 0x0059 }
        r2 = "ObjectFactory name mismatchs ";
        r1.append(r2);	 Catch:{ all -> 0x0059 }
        r1.append(r4);	 Catch:{ all -> 0x0059 }
        r1 = r1.toString();	 Catch:{ all -> 0x0059 }
        r0.<init>(r1);	 Catch:{ all -> 0x0059 }
        throw r0;	 Catch:{ all -> 0x0059 }
    L_0x0059:
        r4 = move-exception;
        monitor-exit(r3);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.maml.ScreenContext.registerObjectFactory(java.lang.String, miui.maml.ObjectFactory):void");
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0014 */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:9:0x0011, code skipped:
            return r0;
     */
    public final synchronized <T extends miui.maml.ObjectFactory> T getObjectFactory(java.lang.String r3) {
        /*
        r2 = this;
        monitor-enter(r2);
        r0 = 0;
        r1 = r2.mObjectFactories;	 Catch:{ ClassCastException -> 0x0017, all -> 0x0014 }
        if (r1 != 0) goto L_0x0007;
    L_0x0006:
        goto L_0x0010;
    L_0x0007:
        r1 = r2.mObjectFactories;	 Catch:{ ClassCastException -> 0x0012, all -> 0x0014 }
        r1 = r1.get(r3);	 Catch:{ ClassCastException -> 0x0012, all -> 0x0014 }
        r1 = (miui.maml.ObjectFactory) r1;	 Catch:{ ClassCastException -> 0x0012, all -> 0x0014 }
        r0 = r1;
    L_0x0010:
        monitor-exit(r2);
        return r0;
    L_0x0012:
        r1 = move-exception;
        goto L_0x0018;
    L_0x0014:
        r3 = move-exception;
        monitor-exit(r2);
        throw r3;
    L_0x0017:
        r1 = move-exception;
    L_0x0018:
        monitor-exit(r2);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.maml.ScreenContext.getObjectFactory(java.lang.String):miui.maml.ObjectFactory");
    }
}
