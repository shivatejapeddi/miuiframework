package miui.maml;

import android.app.MobileDataUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public final class NotifierManager {
    private static boolean DBG = true;
    private static final String LOG_TAG = "NotifierManager";
    public static String TYPE_MOBILE_DATA = "MobileData";
    public static String TYPE_TIME_CHANGED = "TimeChanged";
    public static String TYPE_WIFI_STATE = "WifiState";
    private static NotifierManager sInstance;
    private Context mContext;
    private HashMap<String, BaseNotifier> mNotifiers = new HashMap();

    public interface OnNotifyListener {
        void onNotify(Context context, Intent intent, Object obj);
    }

    public static abstract class BaseNotifier {
        private int mActiveCount;
        protected Context mContext;
        private ArrayList<Listener> mListeners = new ArrayList();
        private int mRefCount;
        private boolean mRegistered;

        private static class Listener {
            public Context context;
            public Intent intent;
            public Object obj;
            private boolean paused;
            private boolean pendingNotify;
            public WeakReference<OnNotifyListener> ref;

            public Listener(OnNotifyListener l) {
                this.ref = new WeakReference(l);
            }

            public void onNotify(Context c, Intent inte, Object o) {
                if (this.paused) {
                    this.pendingNotify = true;
                    this.context = c;
                    this.intent = inte;
                    this.obj = o;
                    return;
                }
                OnNotifyListener l = (OnNotifyListener) this.ref.get();
                if (l != null) {
                    l.onNotify(c, inte, o);
                }
            }

            public void pause() {
                this.paused = true;
            }

            public void resume() {
                this.paused = false;
                if (this.pendingNotify) {
                    OnNotifyListener l = (OnNotifyListener) this.ref.get();
                    if (l != null) {
                        l.onNotify(this.context, this.intent, this.obj);
                        this.pendingNotify = false;
                        this.context = null;
                        this.intent = null;
                        this.obj = null;
                    }
                }
            }
        }

        public abstract void onRegister();

        public abstract void onUnregister();

        public BaseNotifier(Context c) {
            this.mContext = c;
        }

        public final void addListener(OnNotifyListener l) {
            synchronized (this.mListeners) {
                if (findListenerLocked(l) == null) {
                    this.mListeners.add(new Listener(l));
                    checkListenersLocked();
                }
            }
        }

        public final void removeListener(OnNotifyListener l) {
            synchronized (this.mListeners) {
                Listener li = findListenerLocked(l);
                if (li != null) {
                    this.mListeners.remove(li);
                    checkListenersLocked();
                }
            }
        }

        private final Listener findListenerLocked(OnNotifyListener l) {
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                Listener li = (Listener) it.next();
                if (li.ref.get() == l) {
                    return li;
                }
            }
            return null;
        }

        private final void checkListeners() {
            synchronized (this.mListeners) {
                checkListenersLocked();
            }
        }

        private final void checkListenersLocked() {
            this.mActiveCount = 0;
            for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                Listener li = (Listener) this.mListeners.get(i);
                if (li.ref.get() == null) {
                    this.mListeners.remove(i);
                } else if (!li.paused) {
                    this.mActiveCount++;
                }
            }
            this.mRefCount = this.mListeners.size();
        }

        public final int resumeListener(OnNotifyListener l) {
            synchronized (this.mListeners) {
                Listener li = findListenerLocked(l);
                int i;
                if (li == null) {
                    Log.w(NotifierManager.LOG_TAG, "resumeListener, listener not exist");
                    i = this.mActiveCount;
                    return i;
                }
                li.resume();
                checkListenersLocked();
                i = this.mActiveCount;
                return i;
            }
        }

        public final int pauseListener(OnNotifyListener l) {
            synchronized (this.mListeners) {
                Listener li = findListenerLocked(l);
                int i;
                if (li == null) {
                    Log.w(NotifierManager.LOG_TAG, "pauseListener, listener not exist");
                    i = this.mActiveCount;
                    return i;
                }
                li.pause();
                checkListenersLocked();
                i = this.mActiveCount;
                return i;
            }
        }

        public final int getActiveCount() {
            checkListeners();
            return this.mActiveCount;
        }

        public final int getRef() {
            checkListeners();
            return this.mRefCount;
        }

        /* Access modifiers changed, original: protected */
        public void onNotify(Context context, Intent intent, Object o) {
            checkListeners();
            synchronized (this.mListeners) {
                Iterator it = this.mListeners.iterator();
                while (it.hasNext()) {
                    ((Listener) it.next()).onNotify(context, intent, o);
                }
            }
        }

        public void init() {
            register();
        }

        public void resume() {
            register();
        }

        public void pause() {
            unregister();
        }

        public void finish() {
            unregister();
        }

        /* Access modifiers changed, original: protected */
        public void register() {
            if (!this.mRegistered) {
                onRegister();
                this.mRegistered = true;
                if (NotifierManager.DBG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("onRegister: ");
                    stringBuilder.append(toString());
                    Log.i(NotifierManager.LOG_TAG, stringBuilder.toString());
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public void unregister() {
            String str = NotifierManager.LOG_TAG;
            if (this.mRegistered) {
                try {
                    onUnregister();
                } catch (IllegalArgumentException e) {
                    Log.w(str, e.toString());
                }
                this.mRegistered = false;
                if (NotifierManager.DBG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("onUnregister: ");
                    stringBuilder.append(toString());
                    Log.i(str, stringBuilder.toString());
                }
            }
        }
    }

    public static class BroadcastNotifier extends BaseNotifier {
        private String mAction;
        private IntentFilter mIntentFilter;
        private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (NotifierManager.DBG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("onNotify: ");
                    stringBuilder.append(BroadcastNotifier.this.toString());
                    Log.i(NotifierManager.LOG_TAG, stringBuilder.toString());
                }
                BroadcastNotifier.this.onNotify(context, intent, null);
            }
        };

        public BroadcastNotifier(Context c) {
            super(c);
        }

        public BroadcastNotifier(Context c, String action) {
            super(c);
            this.mAction = action;
        }

        /* Access modifiers changed, original: protected */
        public void onRegister() {
            if (this.mIntentFilter == null) {
                this.mIntentFilter = createIntentFilter();
            }
            if (this.mIntentFilter == null) {
                Log.e(NotifierManager.LOG_TAG, "onRegister: mIntentFilter is null");
                return;
            }
            Intent intent = this.mContext.registerReceiver(this.mIntentReceiver, this.mIntentFilter);
            if (intent != null) {
                onNotify(this.mContext, intent, null);
            }
        }

        /* Access modifiers changed, original: protected */
        public void onUnregister() {
            try {
                this.mContext.unregisterReceiver(this.mIntentReceiver);
            } catch (IllegalArgumentException e) {
            }
        }

        /* Access modifiers changed, original: protected */
        public IntentFilter createIntentFilter() {
            String action = getIntentAction();
            return action == null ? null : new IntentFilter(action);
        }

        /* Access modifiers changed, original: protected */
        public String getIntentAction() {
            return this.mAction;
        }
    }

    public static class ContentChangeNotifier extends BaseNotifier {
        protected final ContentObserver mObserver = new ContentObserver(null) {
            public void onChange(boolean selfChange) {
                if (NotifierManager.DBG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("onNotify: ");
                    stringBuilder.append(ContentChangeNotifier.this.toString());
                    Log.i(NotifierManager.LOG_TAG, stringBuilder.toString());
                }
                ContentChangeNotifier.this.onNotify(null, null, Boolean.valueOf(selfChange));
            }
        };
        private Uri mUri;

        public ContentChangeNotifier(Context c, Uri uri) {
            super(c);
            this.mUri = uri;
        }

        /* Access modifiers changed, original: protected */
        public void onRegister() {
            this.mContext.getContentResolver().registerContentObserver(this.mUri, false, this.mObserver);
            onNotify(null, null, Boolean.valueOf(true));
        }

        /* Access modifiers changed, original: protected */
        public void onUnregister() {
            this.mContext.getContentResolver().unregisterContentObserver(this.mObserver);
        }
    }

    public static class MobileDataNotifier extends ContentChangeNotifier {
        private MobileDataUtils mMobileDataUtils = MobileDataUtils.getInstance();

        public MobileDataNotifier(Context c) {
            super(c, null);
        }

        /* Access modifiers changed, original: protected */
        public void onRegister() {
            this.mMobileDataUtils.registerContentObserver(this.mContext, this.mObserver);
            onNotify(null, null, Boolean.valueOf(true));
        }
    }

    public static class MultiBroadcastNotifier extends BroadcastNotifier {
        private String[] mIntents;

        public MultiBroadcastNotifier(Context c, String... intents) {
            super(c);
            this.mIntents = intents;
        }

        /* Access modifiers changed, original: protected */
        public IntentFilter createIntentFilter() {
            IntentFilter filter = new IntentFilter();
            for (String s : this.mIntents) {
                filter.addAction(s);
            }
            return filter;
        }
    }

    private NotifierManager(Context c) {
        this.mContext = c;
    }

    private static BaseNotifier createNotifier(String t, Context c) {
        if (DBG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("createNotifier:");
            stringBuilder.append(t);
            Log.i(LOG_TAG, stringBuilder.toString());
        }
        if (TYPE_MOBILE_DATA.equals(t)) {
            return new MobileDataNotifier(c);
        }
        if (TYPE_WIFI_STATE.equals(t)) {
            return new MultiBroadcastNotifier(c, WifiManager.WIFI_STATE_CHANGED_ACTION, WifiManager.SCAN_RESULTS_AVAILABLE_ACTION, WifiManager.NETWORK_STATE_CHANGED_ACTION);
        }
        if (TYPE_TIME_CHANGED.equals(t)) {
            return new MultiBroadcastNotifier(c, Intent.ACTION_TIMEZONE_CHANGED, Intent.ACTION_TIME_CHANGED);
        }
        return new BroadcastNotifier(c, t);
    }

    public static synchronized NotifierManager getInstance(Context c) {
        NotifierManager notifierManager;
        synchronized (NotifierManager.class) {
            if (sInstance == null) {
                sInstance = new NotifierManager(c);
            }
            notifierManager = sInstance;
        }
        return notifierManager;
    }

    /* JADX WARNING: Missing block: B:14:0x0049, code skipped:
            r0.addListener(r5);
     */
    /* JADX WARNING: Missing block: B:15:0x004c, code skipped:
            return;
     */
    public void acquireNotifier(java.lang.String r4, miui.maml.NotifierManager.OnNotifyListener r5) {
        /*
        r3 = this;
        r0 = DBG;
        if (r0 == 0) goto L_0x0026;
    L_0x0004:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "acquireNotifier:";
        r0.append(r1);
        r0.append(r4);
        r1 = "  ";
        r0.append(r1);
        r1 = r5.toString();
        r0.append(r1);
        r0 = r0.toString();
        r1 = "NotifierManager";
        android.util.Log.i(r1, r0);
    L_0x0026:
        r0 = 0;
        r1 = r3.mNotifiers;
        monitor-enter(r1);
        r2 = r3.mNotifiers;	 Catch:{ all -> 0x004d }
        r2 = r2.get(r4);	 Catch:{ all -> 0x004d }
        r2 = (miui.maml.NotifierManager.BaseNotifier) r2;	 Catch:{ all -> 0x004d }
        r0 = r2;
        if (r0 != 0) goto L_0x0048;
    L_0x0035:
        r2 = r3.mContext;	 Catch:{ all -> 0x004d }
        r2 = createNotifier(r4, r2);	 Catch:{ all -> 0x004d }
        r0 = r2;
        if (r0 != 0) goto L_0x0040;
    L_0x003e:
        monitor-exit(r1);	 Catch:{ all -> 0x004d }
        return;
    L_0x0040:
        r0.init();	 Catch:{ all -> 0x004d }
        r2 = r3.mNotifiers;	 Catch:{ all -> 0x004d }
        r2.put(r4, r0);	 Catch:{ all -> 0x004d }
    L_0x0048:
        monitor-exit(r1);	 Catch:{ all -> 0x004d }
        r0.addListener(r5);
        return;
    L_0x004d:
        r2 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x004d }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.maml.NotifierManager.acquireNotifier(java.lang.String, miui.maml.NotifierManager$OnNotifyListener):void");
    }

    /* JADX WARNING: Missing block: B:14:0x0048, code skipped:
            return;
     */
    public void releaseNotifier(java.lang.String r4, miui.maml.NotifierManager.OnNotifyListener r5) {
        /*
        r3 = this;
        r0 = DBG;
        if (r0 == 0) goto L_0x0027;
    L_0x0004:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "releaseNotifier:";
        r0.append(r1);
        r0.append(r4);
        r1 = "  ";
        r0.append(r1);
        r1 = r5.toString();
        r0.append(r1);
        r0 = r0.toString();
        r1 = "NotifierManager";
        android.util.Log.i(r1, r0);
    L_0x0027:
        r0 = r3.mNotifiers;
        monitor-enter(r0);
        r1 = r3.mNotifiers;	 Catch:{ all -> 0x0049 }
        r1 = r1.get(r4);	 Catch:{ all -> 0x0049 }
        r1 = (miui.maml.NotifierManager.BaseNotifier) r1;	 Catch:{ all -> 0x0049 }
        if (r1 != 0) goto L_0x0036;
    L_0x0034:
        monitor-exit(r0);	 Catch:{ all -> 0x0049 }
        return;
    L_0x0036:
        r1.removeListener(r5);	 Catch:{ all -> 0x0049 }
        r2 = r1.getRef();	 Catch:{ all -> 0x0049 }
        if (r2 != 0) goto L_0x0047;
    L_0x003f:
        r1.finish();	 Catch:{ all -> 0x0049 }
        r2 = r3.mNotifiers;	 Catch:{ all -> 0x0049 }
        r2.remove(r4);	 Catch:{ all -> 0x0049 }
    L_0x0047:
        monitor-exit(r0);	 Catch:{ all -> 0x0049 }
        return;
    L_0x0049:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0049 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.maml.NotifierManager.releaseNotifier(java.lang.String, miui.maml.NotifierManager$OnNotifyListener):void");
    }

    public void pause(String t, OnNotifyListener l) {
        BaseNotifier notifier = safeGet(t);
        if (notifier != null && notifier.pauseListener(l) == 0) {
            notifier.pause();
        }
    }

    /* JADX WARNING: Missing block: B:11:0x0014, code skipped:
            return;
     */
    public synchronized void resume(java.lang.String r4, miui.maml.NotifierManager.OnNotifyListener r5) {
        /*
        r3 = this;
        monitor-enter(r3);
        r0 = r3.safeGet(r4);	 Catch:{ all -> 0x0015 }
        if (r0 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r3);
        return;
    L_0x0009:
        r1 = r0.resumeListener(r5);	 Catch:{ all -> 0x0015 }
        r2 = 1;
        if (r1 != r2) goto L_0x0013;
    L_0x0010:
        r0.resume();	 Catch:{ all -> 0x0015 }
    L_0x0013:
        monitor-exit(r3);
        return;
    L_0x0015:
        r4 = move-exception;
        monitor-exit(r3);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.maml.NotifierManager.resume(java.lang.String, miui.maml.NotifierManager$OnNotifyListener):void");
    }

    private BaseNotifier safeGet(String type) {
        BaseNotifier baseNotifier;
        synchronized (this.mNotifiers) {
            baseNotifier = (BaseNotifier) this.mNotifiers.get(type);
        }
        return baseNotifier;
    }
}
