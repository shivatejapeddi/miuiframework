package miui.maml;

import android.graphics.Canvas;
import android.util.Log;
import java.lang.ref.WeakReference;
import miui.maml.RendererController.IRenderable;

public class RendererCore {
    private static final String LOG_TAG = "RendererCore";
    private boolean mCleaned;
    private MultipleRenderable mMultipleRenderable;
    private WeakReference<OnReleaseListener> mOnReleaseListener;
    private boolean mReleased;
    private ScreenElementRoot mRoot;
    private RenderThread mThread;

    public interface OnReleaseListener {
        boolean OnRendererCoreReleased(RendererCore rendererCore);
    }

    public RendererCore(ScreenElementRoot root, RenderThread t) {
        this(root, t, true);
    }

    public RendererCore(ScreenElementRoot root, RenderThread t, boolean attach) {
        this.mMultipleRenderable = new MultipleRenderable();
        this.mThread = t;
        this.mRoot = root;
        this.mRoot.setRenderControllerRenderable(this.mMultipleRenderable);
        this.mRoot.selfInit();
        if (attach) {
            attach(t);
        }
    }

    public void setOnReleaseListener(OnReleaseListener l) {
        this.mOnReleaseListener = new WeakReference(l);
    }

    public synchronized void addRenderable(IRenderable r) {
        if (!this.mCleaned) {
            this.mMultipleRenderable.add(r);
            String str = LOG_TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("add: ");
            stringBuilder.append(r);
            stringBuilder.append(" size:");
            stringBuilder.append(this.mMultipleRenderable.size());
            Log.d(str, stringBuilder.toString());
            this.mRoot.selfResume();
            this.mReleased = false;
        }
    }

    /* JADX WARNING: Missing block: B:20:0x0063, code skipped:
            return;
     */
    public synchronized void removeRenderable(miui.maml.RendererController.IRenderable r4) {
        /*
        r3 = this;
        monitor-enter(r3);
        r0 = r3.mCleaned;	 Catch:{ all -> 0x0064 }
        if (r0 == 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r3);
        return;
    L_0x0007:
        r0 = r3.mMultipleRenderable;	 Catch:{ all -> 0x0064 }
        r0.remove(r4);	 Catch:{ all -> 0x0064 }
        r0 = "RendererCore";
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0064 }
        r1.<init>();	 Catch:{ all -> 0x0064 }
        r2 = "remove: ";
        r1.append(r2);	 Catch:{ all -> 0x0064 }
        r1.append(r4);	 Catch:{ all -> 0x0064 }
        r2 = " size:";
        r1.append(r2);	 Catch:{ all -> 0x0064 }
        r2 = r3.mMultipleRenderable;	 Catch:{ all -> 0x0064 }
        r2 = r2.size();	 Catch:{ all -> 0x0064 }
        r1.append(r2);	 Catch:{ all -> 0x0064 }
        r1 = r1.toString();	 Catch:{ all -> 0x0064 }
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x0064 }
        r0 = r3.mMultipleRenderable;	 Catch:{ all -> 0x0064 }
        r0 = r0.size();	 Catch:{ all -> 0x0064 }
        if (r0 != 0) goto L_0x0062;
    L_0x0039:
        r0 = r3.mRoot;	 Catch:{ all -> 0x0064 }
        r0.selfPause();	 Catch:{ all -> 0x0064 }
        r0 = r3.mReleased;	 Catch:{ all -> 0x0064 }
        if (r0 != 0) goto L_0x005f;
    L_0x0042:
        r0 = r3.mOnReleaseListener;	 Catch:{ all -> 0x0064 }
        if (r0 == 0) goto L_0x005f;
    L_0x0046:
        r0 = r3.mOnReleaseListener;	 Catch:{ all -> 0x0064 }
        r0 = r0.get();	 Catch:{ all -> 0x0064 }
        if (r0 == 0) goto L_0x005f;
    L_0x004e:
        r0 = r3.mOnReleaseListener;	 Catch:{ all -> 0x0064 }
        r0 = r0.get();	 Catch:{ all -> 0x0064 }
        r0 = (miui.maml.RendererCore.OnReleaseListener) r0;	 Catch:{ all -> 0x0064 }
        r0 = r0.OnRendererCoreReleased(r3);	 Catch:{ all -> 0x0064 }
        if (r0 == 0) goto L_0x005f;
    L_0x005c:
        r3.cleanUp();	 Catch:{ all -> 0x0064 }
    L_0x005f:
        r0 = 1;
        r3.mReleased = r0;	 Catch:{ all -> 0x0064 }
    L_0x0062:
        monitor-exit(r3);
        return;
    L_0x0064:
        r4 = move-exception;
        monitor-exit(r3);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.maml.RendererCore.removeRenderable(miui.maml.RendererController$IRenderable):void");
    }

    /* JADX WARNING: Missing block: B:11:0x0030, code skipped:
            return;
     */
    public synchronized void pauseRenderable(miui.maml.RendererController.IRenderable r5) {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = r4.mCleaned;	 Catch:{ all -> 0x0031 }
        if (r0 == 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r4);
        return;
    L_0x0007:
        r0 = r4.mMultipleRenderable;	 Catch:{ all -> 0x0031 }
        r0 = r0.pause(r5);	 Catch:{ all -> 0x0031 }
        if (r0 != 0) goto L_0x002f;
    L_0x000f:
        r1 = "RendererCore";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0031 }
        r2.<init>();	 Catch:{ all -> 0x0031 }
        r3 = "self pause: ";
        r2.append(r3);	 Catch:{ all -> 0x0031 }
        r3 = r4.toString();	 Catch:{ all -> 0x0031 }
        r2.append(r3);	 Catch:{ all -> 0x0031 }
        r2 = r2.toString();	 Catch:{ all -> 0x0031 }
        android.util.Log.d(r1, r2);	 Catch:{ all -> 0x0031 }
        r1 = r4.mRoot;	 Catch:{ all -> 0x0031 }
        r1.selfPause();	 Catch:{ all -> 0x0031 }
    L_0x002f:
        monitor-exit(r4);
        return;
    L_0x0031:
        r5 = move-exception;
        monitor-exit(r4);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.maml.RendererCore.pauseRenderable(miui.maml.RendererController$IRenderable):void");
    }

    public synchronized void resumeRenderable(IRenderable r) {
        if (!this.mCleaned) {
            this.mMultipleRenderable.resume(r);
            String str = LOG_TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("self resume: ");
            stringBuilder.append(toString());
            Log.d(str, stringBuilder.toString());
            this.mRoot.selfResume();
        }
    }

    public ScreenElementRoot getRoot() {
        return this.mRoot;
    }

    public void render(Canvas c) {
        if (!this.mCleaned && this.mThread.isStarted()) {
            this.mRoot.render(c);
        }
    }

    public void attach(RenderThread t) {
        this.mThread = t;
        ScreenElementRoot screenElementRoot = this.mRoot;
        if (screenElementRoot != null) {
            screenElementRoot.attachToRenderThread(this.mThread);
            this.mRoot.requestUpdate();
        }
    }

    public void cleanUp() {
        this.mCleaned = true;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("cleanUp: ");
        stringBuilder.append(toString());
        Log.d(LOG_TAG, stringBuilder.toString());
        ScreenElementRoot screenElementRoot = this.mRoot;
        if (screenElementRoot != null) {
            screenElementRoot.detachFromRenderThread(this.mThread);
            this.mRoot.selfFinish();
            this.mRoot = null;
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        cleanUp();
        super.finalize();
    }
}
