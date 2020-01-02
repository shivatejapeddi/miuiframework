package android.webkit;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class PluginList {
    private ArrayList<Plugin> mPlugins = new ArrayList();

    @Deprecated
    public synchronized List getList() {
        return this.mPlugins;
    }

    @Deprecated
    public synchronized void addPlugin(Plugin plugin) {
        if (!this.mPlugins.contains(plugin)) {
            this.mPlugins.add(plugin);
        }
    }

    @Deprecated
    public synchronized void removePlugin(Plugin plugin) {
        int location = this.mPlugins.indexOf(plugin);
        if (location != -1) {
            this.mPlugins.remove(location);
        }
    }

    @Deprecated
    public synchronized void clear() {
        this.mPlugins.clear();
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x000f */
    /* JADX WARNING: Failed to process nested try/catch */
    @java.lang.Deprecated
    public synchronized void pluginClicked(android.content.Context r2, int r3) {
        /*
        r1 = this;
        monitor-enter(r1);
        r0 = r1.mPlugins;	 Catch:{ IndexOutOfBoundsException -> 0x0012, all -> 0x000f }
        r0 = r0.get(r3);	 Catch:{ IndexOutOfBoundsException -> 0x000d, all -> 0x000f }
        r0 = (android.webkit.Plugin) r0;	 Catch:{ IndexOutOfBoundsException -> 0x000d, all -> 0x000f }
        r0.dispatchClickEvent(r2);	 Catch:{ IndexOutOfBoundsException -> 0x000d, all -> 0x000f }
        goto L_0x0013;
    L_0x000d:
        r0 = move-exception;
        goto L_0x0013;
    L_0x000f:
        r2 = move-exception;
        monitor-exit(r1);
        throw r2;
    L_0x0012:
        r0 = move-exception;
    L_0x0013:
        monitor-exit(r1);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.webkit.PluginList.pluginClicked(android.content.Context, int):void");
    }
}
