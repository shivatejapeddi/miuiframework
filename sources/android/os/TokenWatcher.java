package android.os;

import android.os.IBinder.DeathRecipient;
import android.util.Log;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

public abstract class TokenWatcher {
    private volatile boolean mAcquired = false;
    private Handler mHandler;
    private int mNotificationQueue = -1;
    private Runnable mNotificationTask = new Runnable() {
        public void run() {
            int value;
            synchronized (TokenWatcher.this.mTokens) {
                value = TokenWatcher.this.mNotificationQueue;
                TokenWatcher.this.mNotificationQueue = -1;
            }
            if (value == 1) {
                TokenWatcher.this.acquired();
            } else if (value == 0) {
                TokenWatcher.this.released();
            }
        }
    };
    private String mTag;
    private WeakHashMap<IBinder, Death> mTokens = new WeakHashMap();

    private class Death implements DeathRecipient {
        String tag;
        IBinder token;

        Death(IBinder token, String tag) {
            this.token = token;
            this.tag = tag;
        }

        public void binderDied() {
            TokenWatcher.this.cleanup(this.token, false);
        }

        /* Access modifiers changed, original: protected */
        public void finalize() throws Throwable {
            try {
                if (this.token != null) {
                    String access$200 = TokenWatcher.this.mTag;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("cleaning up leaked reference: ");
                    stringBuilder.append(this.tag);
                    Log.w(access$200, stringBuilder.toString());
                    TokenWatcher.this.release(this.token);
                }
                super.finalize();
            } catch (Throwable th) {
                super.finalize();
            }
        }
    }

    public abstract void acquired();

    public abstract void released();

    public TokenWatcher(Handler h, String tag) {
        this.mHandler = h;
        this.mTag = tag != null ? tag : "TokenWatcher";
    }

    /* JADX WARNING: Missing block: B:18:0x002f, code skipped:
            return;
     */
    public void acquire(android.os.IBinder r5, java.lang.String r6) {
        /*
        r4 = this;
        r0 = r4.mTokens;
        monitor-enter(r0);
        r1 = r4.mTokens;	 Catch:{ all -> 0x0033 }
        r1 = r1.containsKey(r5);	 Catch:{ all -> 0x0033 }
        if (r1 == 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x0033 }
        return;
    L_0x000d:
        r1 = r4.mTokens;	 Catch:{ all -> 0x0033 }
        r1 = r1.size();	 Catch:{ all -> 0x0033 }
        r2 = new android.os.TokenWatcher$Death;	 Catch:{ all -> 0x0033 }
        r2.<init>(r5, r6);	 Catch:{ all -> 0x0033 }
        r3 = 0;
        r5.linkToDeath(r2, r3);	 Catch:{ RemoteException -> 0x0030 }
        r3 = r4.mTokens;	 Catch:{ all -> 0x0033 }
        r3.put(r5, r2);	 Catch:{ all -> 0x0033 }
        if (r1 != 0) goto L_0x002e;
    L_0x0024:
        r3 = r4.mAcquired;	 Catch:{ all -> 0x0033 }
        if (r3 != 0) goto L_0x002e;
    L_0x0028:
        r3 = 1;
        r4.sendNotificationLocked(r3);	 Catch:{ all -> 0x0033 }
        r4.mAcquired = r3;	 Catch:{ all -> 0x0033 }
    L_0x002e:
        monitor-exit(r0);	 Catch:{ all -> 0x0033 }
        return;
    L_0x0030:
        r3 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0033 }
        return;
    L_0x0033:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0033 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.TokenWatcher.acquire(android.os.IBinder, java.lang.String):void");
    }

    public void cleanup(IBinder token, boolean unlink) {
        synchronized (this.mTokens) {
            Death d = (Death) this.mTokens.remove(token);
            if (unlink && d != null) {
                d.token.unlinkToDeath(d, 0);
                d.token = null;
            }
            if (this.mTokens.size() == 0 && this.mAcquired) {
                sendNotificationLocked(false);
                this.mAcquired = false;
            }
        }
    }

    public void release(IBinder token) {
        cleanup(token, true);
    }

    public boolean isAcquired() {
        boolean z;
        synchronized (this.mTokens) {
            z = this.mAcquired;
        }
        return z;
    }

    public void dump() {
        Iterator it = dumpInternal().iterator();
        while (it.hasNext()) {
            Log.i(this.mTag, (String) it.next());
        }
    }

    public void dump(PrintWriter pw) {
        Iterator it = dumpInternal().iterator();
        while (it.hasNext()) {
            pw.println((String) it.next());
        }
    }

    private ArrayList<String> dumpInternal() {
        ArrayList<String> a = new ArrayList();
        synchronized (this.mTokens) {
            Set<IBinder> keys = this.mTokens.keySet();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Token count: ");
            stringBuilder.append(this.mTokens.size());
            a.add(stringBuilder.toString());
            int i = 0;
            for (IBinder b : keys) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("[");
                stringBuilder2.append(i);
                stringBuilder2.append("] ");
                stringBuilder2.append(((Death) this.mTokens.get(b)).tag);
                stringBuilder2.append(" - ");
                stringBuilder2.append(b);
                a.add(stringBuilder2.toString());
                i++;
            }
        }
        return a;
    }

    private void sendNotificationLocked(boolean on) {
        boolean value = on;
        boolean z = this.mNotificationQueue;
        if (z) {
            this.mNotificationQueue = value;
            this.mHandler.post(this.mNotificationTask);
        } else if (z != value) {
            this.mNotificationQueue = -1;
            this.mHandler.removeCallbacks(this.mNotificationTask);
        }
    }
}
