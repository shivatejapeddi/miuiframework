package android.nfc;

import android.annotation.UnsupportedAppUsage;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.nfc.IAppCallback.Stub;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcAdapter.ReaderCallback;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class NfcActivityManager extends Stub implements ActivityLifecycleCallbacks {
    static final Boolean DBG = Boolean.valueOf(false);
    static final String TAG = "NFC";
    final List<NfcActivityState> mActivities = new LinkedList();
    @UnsupportedAppUsage
    final NfcAdapter mAdapter;
    final List<NfcApplicationState> mApps = new ArrayList(1);

    class NfcActivityState {
        Activity activity;
        int flags = 0;
        NdefMessage ndefMessage = null;
        CreateNdefMessageCallback ndefMessageCallback = null;
        OnNdefPushCompleteCallback onNdefPushCompleteCallback = null;
        ReaderCallback readerCallback = null;
        Bundle readerModeExtras = null;
        int readerModeFlags = 0;
        boolean resumed = false;
        Binder token;
        CreateBeamUrisCallback uriCallback = null;
        Uri[] uris = null;

        public NfcActivityState(Activity activity) {
            if (activity.getWindow().isDestroyed()) {
                throw new IllegalStateException("activity is already destroyed");
            }
            this.resumed = activity.isResumed();
            this.activity = activity;
            this.token = new Binder();
            NfcActivityManager.this.registerApplication(activity.getApplication());
        }

        public void destroy() {
            NfcActivityManager.this.unregisterApplication(this.activity.getApplication());
            this.resumed = false;
            this.activity = null;
            this.ndefMessage = null;
            this.ndefMessageCallback = null;
            this.onNdefPushCompleteCallback = null;
            this.uriCallback = null;
            this.uris = null;
            this.readerModeFlags = 0;
            this.token = null;
        }

        public String toString() {
            StringBuilder s = new StringBuilder("[");
            String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
            s = s.append(str);
            s.append(this.ndefMessage);
            s.append(str);
            s.append(this.ndefMessageCallback);
            s.append(str);
            s.append(this.uriCallback);
            s.append(str);
            Uri[] uriArr = this.uris;
            if (uriArr != null) {
                for (Uri uri : uriArr) {
                    s.append(this.onNdefPushCompleteCallback);
                    s.append(str);
                    s.append(uri);
                    s.append("]");
                }
            }
            return s.toString();
        }
    }

    class NfcApplicationState {
        final Application app;
        int refCount = 0;

        public NfcApplicationState(Application app) {
            this.app = app;
        }

        public void register() {
            this.refCount++;
            if (this.refCount == 1) {
                this.app.registerActivityLifecycleCallbacks(NfcActivityManager.this);
            }
        }

        public void unregister() {
            this.refCount--;
            int i = this.refCount;
            if (i == 0) {
                this.app.unregisterActivityLifecycleCallbacks(NfcActivityManager.this);
            } else if (i < 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("-ve refcount for ");
                stringBuilder.append(this.app);
                Log.e(NfcActivityManager.TAG, stringBuilder.toString());
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public NfcApplicationState findAppState(Application app) {
        for (NfcApplicationState appState : this.mApps) {
            if (appState.app == app) {
                return appState;
            }
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public void registerApplication(Application app) {
        NfcApplicationState appState = findAppState(app);
        if (appState == null) {
            appState = new NfcApplicationState(app);
            this.mApps.add(appState);
        }
        appState.register();
    }

    /* Access modifiers changed, original: 0000 */
    public void unregisterApplication(Application app) {
        NfcApplicationState appState = findAppState(app);
        if (appState == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("app was not registered ");
            stringBuilder.append(app);
            Log.e(TAG, stringBuilder.toString());
            return;
        }
        appState.unregister();
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized NfcActivityState findActivityState(Activity activity) {
        for (NfcActivityState state : this.mActivities) {
            if (state.activity == activity) {
                return state;
            }
        }
        return null;
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized NfcActivityState getActivityState(Activity activity) {
        NfcActivityState state;
        state = findActivityState(activity);
        if (state == null) {
            state = new NfcActivityState(activity);
            this.mActivities.add(state);
        }
        return state;
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized NfcActivityState findResumedActivityState() {
        for (NfcActivityState state : this.mActivities) {
            if (state.resumed) {
                return state;
            }
        }
        return null;
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void destroyActivityState(Activity activity) {
        NfcActivityState activityState = findActivityState(activity);
        if (activityState != null) {
            activityState.destroy();
            this.mActivities.remove(activityState);
        }
    }

    public NfcActivityManager(NfcAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void enableReaderMode(Activity activity, ReaderCallback callback, int flags, Bundle extras) {
        Binder token;
        boolean isResumed;
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            state.readerCallback = callback;
            state.readerModeFlags = flags;
            state.readerModeExtras = extras;
            token = state.token;
            isResumed = state.resumed;
        }
        if (isResumed) {
            setReaderMode(token, flags, extras);
        }
    }

    public void disableReaderMode(Activity activity) {
        Binder token;
        boolean isResumed;
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            state.readerCallback = null;
            state.readerModeFlags = 0;
            state.readerModeExtras = null;
            token = state.token;
            isResumed = state.resumed;
        }
        if (isResumed) {
            setReaderMode(token, 0, null);
        }
    }

    public void setReaderMode(Binder token, int flags, Bundle extras) {
        if (DBG.booleanValue()) {
            Log.d(TAG, "Setting reader mode");
        }
        try {
            NfcAdapter.sService.setReaderMode(token, this, flags, extras);
        } catch (RemoteException e) {
            this.mAdapter.attemptDeadServiceRecovery(e);
        }
    }

    public void setNdefPushContentUri(Activity activity, Uri[] uris) {
        boolean isResumed;
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            state.uris = uris;
            isResumed = state.resumed;
        }
        if (isResumed) {
            requestNfcServiceCallback();
        } else {
            verifyNfcPermission();
        }
    }

    public void setNdefPushContentUriCallback(Activity activity, CreateBeamUrisCallback callback) {
        boolean isResumed;
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            state.uriCallback = callback;
            isResumed = state.resumed;
        }
        if (isResumed) {
            requestNfcServiceCallback();
        } else {
            verifyNfcPermission();
        }
    }

    public void setNdefPushMessage(Activity activity, NdefMessage message, int flags) {
        boolean isResumed;
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            state.ndefMessage = message;
            state.flags = flags;
            isResumed = state.resumed;
        }
        if (isResumed) {
            requestNfcServiceCallback();
        } else {
            verifyNfcPermission();
        }
    }

    public void setNdefPushMessageCallback(Activity activity, CreateNdefMessageCallback callback, int flags) {
        boolean isResumed;
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            state.ndefMessageCallback = callback;
            state.flags = flags;
            isResumed = state.resumed;
        }
        if (isResumed) {
            requestNfcServiceCallback();
        } else {
            verifyNfcPermission();
        }
    }

    public void setOnNdefPushCompleteCallback(Activity activity, OnNdefPushCompleteCallback callback) {
        boolean isResumed;
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            state.onNdefPushCompleteCallback = callback;
            isResumed = state.resumed;
        }
        if (isResumed) {
            requestNfcServiceCallback();
        } else {
            verifyNfcPermission();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void requestNfcServiceCallback() {
        try {
            NfcAdapter.sService.setAppCallback(this);
        } catch (RemoteException e) {
            this.mAdapter.attemptDeadServiceRecovery(e);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void verifyNfcPermission() {
        try {
            NfcAdapter.sService.verifyNfcPermission();
        } catch (RemoteException e) {
            this.mAdapter.attemptDeadServiceRecovery(e);
        }
    }

    /* JADX WARNING: Missing block: B:14:0x0028, code skipped:
            r10 = android.os.Binder.clearCallingIdentity();
     */
    /* JADX WARNING: Missing block: B:15:0x002c, code skipped:
            if (r4 == null) goto L_0x0039;
     */
    /* JADX WARNING: Missing block: B:18:0x0032, code skipped:
            r6 = r4.createNdefMessage(r2);
     */
    /* JADX WARNING: Missing block: B:19:0x0034, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:20:0x0035, code skipped:
            r16 = r2;
     */
    /* JADX WARNING: Missing block: B:21:0x0039, code skipped:
            if (r5 == null) goto L_0x00a4;
     */
    /* JADX WARNING: Missing block: B:23:?, code skipped:
            r7 = r5.createBeamUris(r2);
     */
    /* JADX WARNING: Missing block: B:24:0x0040, code skipped:
            if (r7 == null) goto L_0x009d;
     */
    /* JADX WARNING: Missing block: B:25:0x0042, code skipped:
            r12 = new java.util.ArrayList();
            r13 = r7.length;
            r14 = 0;
     */
    /* JADX WARNING: Missing block: B:26:0x0049, code skipped:
            if (r14 >= r13) goto L_0x008d;
     */
    /* JADX WARNING: Missing block: B:27:0x004b, code skipped:
            r15 = r7[r14];
     */
    /* JADX WARNING: Missing block: B:28:0x004d, code skipped:
            if (r15 != null) goto L_0x0059;
     */
    /* JADX WARNING: Missing block: B:30:0x0051, code skipped:
            r16 = r2;
     */
    /* JADX WARNING: Missing block: B:32:?, code skipped:
            android.util.Log.e(TAG, "Uri not allowed to be null.");
     */
    /* JADX WARNING: Missing block: B:33:0x0059, code skipped:
            r16 = r2;
            r0 = r15.getScheme();
     */
    /* JADX WARNING: Missing block: B:34:0x005f, code skipped:
            if (r0 == null) goto L_0x007e;
     */
    /* JADX WARNING: Missing block: B:36:0x0067, code skipped:
            if (r0.equalsIgnoreCase(android.content.ContentResolver.SCHEME_FILE) != null) goto L_0x0072;
     */
    /* JADX WARNING: Missing block: B:38:0x006f, code skipped:
            if (r0.equalsIgnoreCase("content") != null) goto L_0x0072;
     */
    /* JADX WARNING: Missing block: B:40:0x0072, code skipped:
            r12.add(android.content.ContentProvider.maybeAddUserId(r15, r9.getUserId()));
     */
    /* JADX WARNING: Missing block: B:41:0x007e, code skipped:
            r17 = r0;
            android.util.Log.e(TAG, "Uri needs to have either scheme file or scheme content");
     */
    /* JADX WARNING: Missing block: B:42:0x0088, code skipped:
            r14 = r14 + 1;
            r2 = r16;
     */
    /* JADX WARNING: Missing block: B:43:0x008d, code skipped:
            r16 = r2;
            r7 = (android.net.Uri[]) r12.toArray(new android.net.Uri[r12.size()]);
     */
    /* JADX WARNING: Missing block: B:44:0x009d, code skipped:
            r16 = r2;
     */
    /* JADX WARNING: Missing block: B:45:0x00a0, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:46:0x00a1, code skipped:
            r16 = r2;
     */
    /* JADX WARNING: Missing block: B:47:0x00a4, code skipped:
            r16 = r2;
     */
    /* JADX WARNING: Missing block: B:48:0x00a6, code skipped:
            if (r7 == null) goto L_0x00bf;
     */
    /* JADX WARNING: Missing block: B:50:0x00a9, code skipped:
            if (r7.length <= 0) goto L_0x00bf;
     */
    /* JADX WARNING: Missing block: B:51:0x00ab, code skipped:
            r0 = r7.length;
            r2 = 0;
     */
    /* JADX WARNING: Missing block: B:52:0x00ad, code skipped:
            if (r2 >= r0) goto L_0x00bf;
     */
    /* JADX WARNING: Missing block: B:53:0x00af, code skipped:
            r9.grantUriPermission("com.android.nfc", r7[r2], 1);
     */
    /* JADX WARNING: Missing block: B:54:0x00b7, code skipped:
            r2 = r2 + 1;
     */
    /* JADX WARNING: Missing block: B:55:0x00ba, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:56:0x00bb, code skipped:
            android.os.Binder.restoreCallingIdentity(r10);
     */
    /* JADX WARNING: Missing block: B:57:0x00be, code skipped:
            throw r0;
     */
    /* JADX WARNING: Missing block: B:58:0x00bf, code skipped:
            android.os.Binder.restoreCallingIdentity(r10);
     */
    /* JADX WARNING: Missing block: B:59:0x00cc, code skipped:
            return new android.nfc.BeamShareData(r6, r7, r9.getUser(), r8);
     */
    public android.nfc.BeamShareData createBeamShareData(byte r19) {
        /*
        r18 = this;
        r1 = r18;
        r0 = new android.nfc.NfcEvent;
        r2 = r1.mAdapter;
        r3 = r19;
        r0.<init>(r2, r3);
        r2 = r0;
        monitor-enter(r18);
        r0 = r18.findResumedActivityState();	 Catch:{ all -> 0x00cd }
        if (r0 != 0) goto L_0x001b;
    L_0x0013:
        r4 = 0;
        monitor-exit(r18);	 Catch:{ all -> 0x0016 }
        return r4;
    L_0x0016:
        r0 = move-exception;
        r16 = r2;
        goto L_0x00d0;
    L_0x001b:
        r4 = r0.ndefMessageCallback;	 Catch:{ all -> 0x00cd }
        r5 = r0.uriCallback;	 Catch:{ all -> 0x00cd }
        r6 = r0.ndefMessage;	 Catch:{ all -> 0x00cd }
        r7 = r0.uris;	 Catch:{ all -> 0x00cd }
        r8 = r0.flags;	 Catch:{ all -> 0x00cd }
        r9 = r0.activity;	 Catch:{ all -> 0x00cd }
        monitor-exit(r18);	 Catch:{ all -> 0x00cd }
        r10 = android.os.Binder.clearCallingIdentity();
        if (r4 == 0) goto L_0x0039;
    L_0x002e:
        r0 = r4.createNdefMessage(r2);	 Catch:{ all -> 0x0034 }
        r6 = r0;
        goto L_0x0039;
    L_0x0034:
        r0 = move-exception;
        r16 = r2;
        goto L_0x00bb;
    L_0x0039:
        if (r5 == 0) goto L_0x00a4;
    L_0x003b:
        r12 = r5.createBeamUris(r2);	 Catch:{ all -> 0x00a0 }
        r7 = r12;
        if (r7 == 0) goto L_0x009d;
    L_0x0042:
        r12 = new java.util.ArrayList;	 Catch:{ all -> 0x00a0 }
        r12.<init>();	 Catch:{ all -> 0x00a0 }
        r13 = r7.length;	 Catch:{ all -> 0x00a0 }
        r14 = 0;
    L_0x0049:
        if (r14 >= r13) goto L_0x008d;
    L_0x004b:
        r15 = r7[r14];	 Catch:{ all -> 0x00a0 }
        if (r15 != 0) goto L_0x0059;
    L_0x004f:
        r0 = "NFC";
        r16 = r2;
        r2 = "Uri not allowed to be null.";
        android.util.Log.e(r0, r2);	 Catch:{ all -> 0x00ba }
        goto L_0x0088;
    L_0x0059:
        r16 = r2;
        r0 = r15.getScheme();	 Catch:{ all -> 0x00ba }
        if (r0 == 0) goto L_0x007e;
    L_0x0061:
        r2 = "file";
        r2 = r0.equalsIgnoreCase(r2);	 Catch:{ all -> 0x00ba }
        if (r2 != 0) goto L_0x0072;
    L_0x0069:
        r2 = "content";
        r2 = r0.equalsIgnoreCase(r2);	 Catch:{ all -> 0x00ba }
        if (r2 != 0) goto L_0x0072;
    L_0x0071:
        goto L_0x007e;
    L_0x0072:
        r2 = r9.getUserId();	 Catch:{ all -> 0x00ba }
        r2 = android.content.ContentProvider.maybeAddUserId(r15, r2);	 Catch:{ all -> 0x00ba }
        r12.add(r2);	 Catch:{ all -> 0x00ba }
        goto L_0x0088;
    L_0x007e:
        r2 = "NFC";
        r17 = r0;
        r0 = "Uri needs to have either scheme file or scheme content";
        android.util.Log.e(r2, r0);	 Catch:{ all -> 0x00ba }
    L_0x0088:
        r14 = r14 + 1;
        r2 = r16;
        goto L_0x0049;
    L_0x008d:
        r16 = r2;
        r0 = r12.size();	 Catch:{ all -> 0x00ba }
        r0 = new android.net.Uri[r0];	 Catch:{ all -> 0x00ba }
        r0 = r12.toArray(r0);	 Catch:{ all -> 0x00ba }
        r0 = (android.net.Uri[]) r0;	 Catch:{ all -> 0x00ba }
        r7 = r0;
        goto L_0x00a6;
    L_0x009d:
        r16 = r2;
        goto L_0x00a6;
    L_0x00a0:
        r0 = move-exception;
        r16 = r2;
        goto L_0x00bb;
    L_0x00a4:
        r16 = r2;
    L_0x00a6:
        if (r7 == 0) goto L_0x00bf;
    L_0x00a8:
        r0 = r7.length;	 Catch:{ all -> 0x00ba }
        if (r0 <= 0) goto L_0x00bf;
    L_0x00ab:
        r0 = r7.length;	 Catch:{ all -> 0x00ba }
        r2 = 0;
    L_0x00ad:
        if (r2 >= r0) goto L_0x00bf;
    L_0x00af:
        r12 = r7[r2];	 Catch:{ all -> 0x00ba }
        r13 = "com.android.nfc";
        r14 = 1;
        r9.grantUriPermission(r13, r12, r14);	 Catch:{ all -> 0x00ba }
        r2 = r2 + 1;
        goto L_0x00ad;
    L_0x00ba:
        r0 = move-exception;
    L_0x00bb:
        android.os.Binder.restoreCallingIdentity(r10);
        throw r0;
    L_0x00bf:
        android.os.Binder.restoreCallingIdentity(r10);
        r0 = new android.nfc.BeamShareData;
        r2 = r9.getUser();
        r0.<init>(r6, r7, r2, r8);
        return r0;
    L_0x00cd:
        r0 = move-exception;
        r16 = r2;
    L_0x00d0:
        monitor-exit(r18);	 Catch:{ all -> 0x00d2 }
        throw r0;
    L_0x00d2:
        r0 = move-exception;
        goto L_0x00d0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcActivityManager.createBeamShareData(byte):android.nfc.BeamShareData");
    }

    /* JADX WARNING: Missing block: B:8:0x000d, code skipped:
            r1 = new android.nfc.NfcEvent(r3.mAdapter, r4);
     */
    /* JADX WARNING: Missing block: B:9:0x0014, code skipped:
            if (r0 == null) goto L_0x0019;
     */
    /* JADX WARNING: Missing block: B:10:0x0016, code skipped:
            r0.onNdefPushComplete(r1);
     */
    /* JADX WARNING: Missing block: B:11:0x0019, code skipped:
            return;
     */
    public void onNdefPushComplete(byte r4) {
        /*
        r3 = this;
        monitor-enter(r3);
        r0 = r3.findResumedActivityState();	 Catch:{ all -> 0x001a }
        if (r0 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r3);	 Catch:{ all -> 0x001a }
        return;
    L_0x0009:
        r1 = r0.onNdefPushCompleteCallback;	 Catch:{ all -> 0x001a }
        r0 = r1;
        monitor-exit(r3);	 Catch:{ all -> 0x001a }
        r1 = new android.nfc.NfcEvent;
        r2 = r3.mAdapter;
        r1.<init>(r2, r4);
        if (r0 == 0) goto L_0x0019;
    L_0x0016:
        r0.onNdefPushComplete(r1);
    L_0x0019:
        return;
    L_0x001a:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x001a }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcActivityManager.onNdefPushComplete(byte):void");
    }

    /* JADX WARNING: Missing block: B:8:0x000d, code skipped:
            if (r0 == null) goto L_0x0012;
     */
    /* JADX WARNING: Missing block: B:9:0x000f, code skipped:
            r0.onTagDiscovered(r3);
     */
    /* JADX WARNING: Missing block: B:10:0x0012, code skipped:
            return;
     */
    public void onTagDiscovered(android.nfc.Tag r3) throws android.os.RemoteException {
        /*
        r2 = this;
        monitor-enter(r2);
        r0 = r2.findResumedActivityState();	 Catch:{ all -> 0x0013 }
        if (r0 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r2);	 Catch:{ all -> 0x0013 }
        return;
    L_0x0009:
        r1 = r0.readerCallback;	 Catch:{ all -> 0x0013 }
        r0 = r1;
        monitor-exit(r2);	 Catch:{ all -> 0x0013 }
        if (r0 == 0) goto L_0x0012;
    L_0x000f:
        r0.onTagDiscovered(r3);
    L_0x0012:
        return;
    L_0x0013:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0013 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcActivityManager.onTagDiscovered(android.nfc.Tag):void");
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    /* JADX WARNING: Missing block: B:11:0x003e, code skipped:
            if (r0 == 0) goto L_0x0043;
     */
    /* JADX WARNING: Missing block: B:12:0x0040, code skipped:
            setReaderMode(r3, r0, r1);
     */
    /* JADX WARNING: Missing block: B:13:0x0043, code skipped:
            requestNfcServiceCallback();
     */
    /* JADX WARNING: Missing block: B:14:0x0046, code skipped:
            return;
     */
    public void onActivityResumed(android.app.Activity r7) {
        /*
        r6 = this;
        r0 = 0;
        r1 = 0;
        monitor-enter(r6);
        r2 = r6.findActivityState(r7);	 Catch:{ all -> 0x0047 }
        r3 = DBG;	 Catch:{ all -> 0x0047 }
        r3 = r3.booleanValue();	 Catch:{ all -> 0x0047 }
        if (r3 == 0) goto L_0x002e;
    L_0x000f:
        r3 = "NFC";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0047 }
        r4.<init>();	 Catch:{ all -> 0x0047 }
        r5 = "onResume() for ";
        r4.append(r5);	 Catch:{ all -> 0x0047 }
        r4.append(r7);	 Catch:{ all -> 0x0047 }
        r5 = " ";
        r4.append(r5);	 Catch:{ all -> 0x0047 }
        r4.append(r2);	 Catch:{ all -> 0x0047 }
        r4 = r4.toString();	 Catch:{ all -> 0x0047 }
        android.util.Log.d(r3, r4);	 Catch:{ all -> 0x0047 }
    L_0x002e:
        if (r2 != 0) goto L_0x0032;
    L_0x0030:
        monitor-exit(r6);	 Catch:{ all -> 0x0047 }
        return;
    L_0x0032:
        r3 = 1;
        r2.resumed = r3;	 Catch:{ all -> 0x0047 }
        r3 = r2.token;	 Catch:{ all -> 0x0047 }
        r4 = r2.readerModeFlags;	 Catch:{ all -> 0x0047 }
        r0 = r4;
        r4 = r2.readerModeExtras;	 Catch:{ all -> 0x0047 }
        r1 = r4;
        monitor-exit(r6);	 Catch:{ all -> 0x0047 }
        if (r0 == 0) goto L_0x0043;
    L_0x0040:
        r6.setReaderMode(r3, r0, r1);
    L_0x0043:
        r6.requestNfcServiceCallback();
        return;
    L_0x0047:
        r2 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x0047 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcActivityManager.onActivityResumed(android.app.Activity):void");
    }

    /* JADX WARNING: Missing block: B:14:0x003e, code skipped:
            if (r0 == false) goto L_0x0044;
     */
    /* JADX WARNING: Missing block: B:15:0x0040, code skipped:
            setReaderMode(r2, 0, null);
     */
    /* JADX WARNING: Missing block: B:16:0x0044, code skipped:
            return;
     */
    public void onActivityPaused(android.app.Activity r5) {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = r4.findActivityState(r5);	 Catch:{ all -> 0x0045 }
        r1 = DBG;	 Catch:{ all -> 0x0045 }
        r1 = r1.booleanValue();	 Catch:{ all -> 0x0045 }
        if (r1 == 0) goto L_0x002c;
    L_0x000d:
        r1 = "NFC";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0045 }
        r2.<init>();	 Catch:{ all -> 0x0045 }
        r3 = "onPause() for ";
        r2.append(r3);	 Catch:{ all -> 0x0045 }
        r2.append(r5);	 Catch:{ all -> 0x0045 }
        r3 = " ";
        r2.append(r3);	 Catch:{ all -> 0x0045 }
        r2.append(r0);	 Catch:{ all -> 0x0045 }
        r2 = r2.toString();	 Catch:{ all -> 0x0045 }
        android.util.Log.d(r1, r2);	 Catch:{ all -> 0x0045 }
    L_0x002c:
        if (r0 != 0) goto L_0x0030;
    L_0x002e:
        monitor-exit(r4);	 Catch:{ all -> 0x0045 }
        return;
    L_0x0030:
        r1 = 0;
        r0.resumed = r1;	 Catch:{ all -> 0x0045 }
        r2 = r0.token;	 Catch:{ all -> 0x0045 }
        r3 = r0.readerModeFlags;	 Catch:{ all -> 0x0045 }
        if (r3 == 0) goto L_0x003b;
    L_0x0039:
        r3 = 1;
        goto L_0x003c;
    L_0x003b:
        r3 = r1;
    L_0x003c:
        r0 = r3;
        monitor-exit(r4);	 Catch:{ all -> 0x0045 }
        if (r0 == 0) goto L_0x0044;
    L_0x0040:
        r3 = 0;
        r4.setReaderMode(r2, r1, r3);
    L_0x0044:
        return;
    L_0x0045:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0045 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcActivityManager.onActivityPaused(android.app.Activity):void");
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityDestroyed(Activity activity) {
        synchronized (this) {
            NfcActivityState state = findActivityState(activity);
            if (DBG.booleanValue()) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onDestroy() for ");
                stringBuilder.append(activity);
                stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                stringBuilder.append(state);
                Log.d(str, stringBuilder.toString());
            }
            if (state != null) {
                destroyActivityState(activity);
            }
        }
    }
}
