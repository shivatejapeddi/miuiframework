package miui.maml;

import android.net.wifi.WifiEnterpriseConfig;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import miui.maml.RendererController.IRenderable;

public class MultipleRenderable implements IRenderable {
    private static final String LOG_TAG = "MultipleRenderable";
    private int mActiveCount;
    private ArrayList<RenderableInfo> mList = new ArrayList();

    private static class RenderableInfo {
        public boolean paused;
        public WeakReference<IRenderable> r;

        public RenderableInfo(IRenderable re) {
            this.r = new WeakReference(re);
        }
    }

    public synchronized void add(IRenderable r) {
        if (find(r) == null) {
            String str = LOG_TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("add: ");
            stringBuilder.append(r);
            Log.d(str, stringBuilder.toString());
            this.mList.add(new RenderableInfo(r));
            this.mActiveCount++;
        }
    }

    public synchronized void remove(IRenderable r) {
        int N = this.mList.size();
        if (N != 0) {
            for (int i = N - 1; i >= 0; i--) {
                RenderableInfo ri = (RenderableInfo) this.mList.get(i);
                IRenderable ren = (IRenderable) ri.r.get();
                if (ren == null || ren == r) {
                    if (!ri.paused) {
                        this.mActiveCount--;
                    }
                    this.mList.remove(i);
                    String str = LOG_TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("remove: ");
                    stringBuilder.append(ren);
                    Log.d(str, stringBuilder.toString());
                }
            }
        }
    }

    public synchronized int pause(IRenderable r) {
        return setPause(r, true);
    }

    public synchronized int resume(IRenderable r) {
        return setPause(r, false);
    }

    private int setPause(IRenderable r, boolean pause) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setPause: ");
        stringBuilder.append(pause);
        stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        stringBuilder.append(r);
        Log.d(LOG_TAG, stringBuilder.toString());
        RenderableInfo ri = find(r);
        if (ri == null) {
            return this.mActiveCount;
        }
        if (ri.paused != pause) {
            ri.paused = pause;
            int i = this.mActiveCount;
            this.mActiveCount = pause ? i - 1 : i + 1;
        }
        return this.mActiveCount;
    }

    private RenderableInfo find(IRenderable r) {
        int N = this.mList.size();
        for (int i = 0; i < N; i++) {
            RenderableInfo ri = (RenderableInfo) this.mList.get(i);
            if (ri.r.get() == r) {
                return ri;
            }
        }
        return null;
    }

    public synchronized void doRender() {
        this.mActiveCount = 0;
        for (int i = this.mList.size() - 1; i >= 0; i--) {
            RenderableInfo ri = (RenderableInfo) this.mList.get(i);
            if (!ri.paused) {
                IRenderable r = (IRenderable) ri.r.get();
                if (r != null) {
                    r.doRender();
                    this.mActiveCount++;
                } else {
                    this.mList.remove(i);
                }
            }
        }
    }

    public synchronized int size() {
        return this.mList.size();
    }
}
