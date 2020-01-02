package android.nfc;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.Activity;
import android.app.ActivityThread;
import android.app.OnActivityPausedListener;
import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.INfcAdapter.Stub;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class NfcAdapter {
    public static final String ACTION_ADAPTER_STATE_CHANGED = "android.nfc.action.ADAPTER_STATE_CHANGED";
    public static final String ACTION_HANDOVER_TRANSFER_DONE = "android.nfc.action.HANDOVER_TRANSFER_DONE";
    public static final String ACTION_HANDOVER_TRANSFER_STARTED = "android.nfc.action.HANDOVER_TRANSFER_STARTED";
    public static final String ACTION_NDEF_DISCOVERED = "android.nfc.action.NDEF_DISCOVERED";
    public static final String ACTION_TAG_DISCOVERED = "android.nfc.action.TAG_DISCOVERED";
    public static final String ACTION_TAG_LEFT_FIELD = "android.nfc.action.TAG_LOST";
    public static final String ACTION_TECH_DISCOVERED = "android.nfc.action.TECH_DISCOVERED";
    public static final String ACTION_TRANSACTION_DETECTED = "android.nfc.action.TRANSACTION_DETECTED";
    public static final String EXTRA_ADAPTER_STATE = "android.nfc.extra.ADAPTER_STATE";
    public static final String EXTRA_AID = "android.nfc.extra.AID";
    public static final String EXTRA_DATA = "android.nfc.extra.DATA";
    public static final String EXTRA_HANDOVER_TRANSFER_STATUS = "android.nfc.extra.HANDOVER_TRANSFER_STATUS";
    public static final String EXTRA_HANDOVER_TRANSFER_URI = "android.nfc.extra.HANDOVER_TRANSFER_URI";
    public static final String EXTRA_ID = "android.nfc.extra.ID";
    public static final String EXTRA_NDEF_MESSAGES = "android.nfc.extra.NDEF_MESSAGES";
    public static final String EXTRA_READER_PRESENCE_CHECK_DELAY = "presence";
    public static final String EXTRA_SECURE_ELEMENT_NAME = "android.nfc.extra.SECURE_ELEMENT_NAME";
    public static final String EXTRA_TAG = "android.nfc.extra.TAG";
    @SystemApi
    public static final int FLAG_NDEF_PUSH_NO_CONFIRM = 1;
    public static final int FLAG_READER_NFC_A = 1;
    public static final int FLAG_READER_NFC_B = 2;
    public static final int FLAG_READER_NFC_BARCODE = 16;
    public static final int FLAG_READER_NFC_F = 4;
    public static final int FLAG_READER_NFC_V = 8;
    public static final int FLAG_READER_NO_PLATFORM_SOUNDS = 256;
    public static final int FLAG_READER_SKIP_NDEF_CHECK = 128;
    public static final int HANDOVER_TRANSFER_STATUS_FAILURE = 1;
    public static final int HANDOVER_TRANSFER_STATUS_SUCCESS = 0;
    public static final int STATE_OFF = 1;
    public static final int STATE_ON = 3;
    public static final int STATE_TURNING_OFF = 4;
    public static final int STATE_TURNING_ON = 2;
    static final String TAG = "NFC";
    static INfcCardEmulation sCardEmulationService;
    static boolean sHasBeamFeature;
    static boolean sHasNfcFeature;
    static boolean sIsInitialized = false;
    static HashMap<Context, NfcAdapter> sNfcAdapters = new HashMap();
    static INfcFCardEmulation sNfcFCardEmulationService;
    static NfcAdapter sNullContextNfcAdapter;
    @UnsupportedAppUsage
    static INfcAdapter sService;
    static INfcTag sTagService;
    final Context mContext;
    OnActivityPausedListener mForegroundDispatchListener = new OnActivityPausedListener() {
        public void onPaused(Activity activity) {
            NfcAdapter.this.disableForegroundDispatchInternal(activity, true);
        }
    };
    final Object mLock;
    final NfcActivityManager mNfcActivityManager;
    final HashMap<NfcUnlockHandler, INfcUnlockHandler> mNfcUnlockHandlers;
    ITagRemovedCallback mTagRemovedListener;

    @Deprecated
    public interface CreateBeamUrisCallback {
        Uri[] createBeamUris(NfcEvent nfcEvent);
    }

    @Deprecated
    public interface CreateNdefMessageCallback {
        NdefMessage createNdefMessage(NfcEvent nfcEvent);
    }

    @SystemApi
    public interface NfcUnlockHandler {
        boolean onUnlockAttempted(Tag tag);
    }

    @Deprecated
    public interface OnNdefPushCompleteCallback {
        void onNdefPushComplete(NfcEvent nfcEvent);
    }

    public interface OnTagRemovedListener {
        void onTagRemoved();
    }

    public interface ReaderCallback {
        void onTagDiscovered(Tag tag);
    }

    private static boolean hasBeamFeature() {
        IPackageManager pm = ActivityThread.getPackageManager();
        String str = TAG;
        if (pm == null) {
            Log.e(str, "Cannot get package manager, assuming no Android Beam feature");
            return false;
        }
        try {
            str = pm.hasSystemFeature(PackageManager.FEATURE_NFC_BEAM, 0);
            return str;
        } catch (RemoteException e) {
            Log.e(str, "Package manager query failed, assuming no Android Beam feature", e);
            return false;
        }
    }

    private static boolean hasNfcFeature() {
        IPackageManager pm = ActivityThread.getPackageManager();
        String str = TAG;
        if (pm == null) {
            Log.e(str, "Cannot get package manager, assuming no NFC feature");
            return false;
        }
        try {
            str = pm.hasSystemFeature(PackageManager.FEATURE_NFC, 0);
            return str;
        } catch (RemoteException e) {
            Log.e(str, "Package manager query failed, assuming no NFC feature", e);
            return false;
        }
    }

    private static boolean hasNfcHceFeature() {
        IPackageManager pm = ActivityThread.getPackageManager();
        String str = TAG;
        boolean z = false;
        if (pm == null) {
            Log.e(str, "Cannot get package manager, assuming no NFC feature");
            return false;
        }
        try {
            if (pm.hasSystemFeature("android.hardware.nfc.hce", 0) || pm.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION_NFCF, 0)) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            Log.e(str, "Package manager query failed, assuming no NFC feature", e);
            return false;
        }
    }

    public List<String> getSupportedOffHostSecureElements() {
        List<String> offHostSE = new ArrayList();
        IPackageManager pm = ActivityThread.getPackageManager();
        String str = TAG;
        if (pm == null) {
            Log.e(str, "Cannot get package manager, assuming no off-host CE feature");
            return offHostSE;
        }
        try {
            if (pm.hasSystemFeature(PackageManager.FEATURE_NFC_OFF_HOST_CARD_EMULATION_UICC, 0)) {
                offHostSE.add("SIM");
            }
            if (pm.hasSystemFeature(PackageManager.FEATURE_NFC_OFF_HOST_CARD_EMULATION_ESE, 0)) {
                offHostSE.add("eSE");
            }
            return offHostSE;
        } catch (RemoteException e) {
            Log.e(str, "Package manager query failed, assuming no off-host CE feature", e);
            offHostSE.clear();
            return offHostSE;
        }
    }

    /* JADX WARNING: Missing block: B:48:0x00ba, code skipped:
            return r1;
     */
    @android.annotation.UnsupportedAppUsage
    public static synchronized android.nfc.NfcAdapter getNfcAdapter(android.content.Context r5) {
        /*
        r0 = android.nfc.NfcAdapter.class;
        monitor-enter(r0);
        r1 = sIsInitialized;	 Catch:{ all -> 0x00bb }
        if (r1 != 0) goto L_0x0092;
    L_0x0007:
        r1 = hasNfcFeature();	 Catch:{ all -> 0x00bb }
        sHasNfcFeature = r1;	 Catch:{ all -> 0x00bb }
        r1 = hasBeamFeature();	 Catch:{ all -> 0x00bb }
        sHasBeamFeature = r1;	 Catch:{ all -> 0x00bb }
        r1 = hasNfcHceFeature();	 Catch:{ all -> 0x00bb }
        r2 = sHasNfcFeature;	 Catch:{ all -> 0x00bb }
        if (r2 != 0) goto L_0x002c;
    L_0x001b:
        if (r1 == 0) goto L_0x001e;
    L_0x001d:
        goto L_0x002c;
    L_0x001e:
        r2 = "NFC";
        r3 = "this device does not have NFC support";
        android.util.Log.v(r2, r3);	 Catch:{ all -> 0x00bb }
        r2 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x00bb }
        r2.<init>();	 Catch:{ all -> 0x00bb }
        throw r2;	 Catch:{ all -> 0x00bb }
    L_0x002c:
        r2 = getServiceInterface();	 Catch:{ all -> 0x00bb }
        sService = r2;	 Catch:{ all -> 0x00bb }
        r2 = sService;	 Catch:{ all -> 0x00bb }
        if (r2 == 0) goto L_0x0085;
    L_0x0036:
        r2 = sHasNfcFeature;	 Catch:{ all -> 0x00bb }
        if (r2 == 0) goto L_0x0051;
    L_0x003a:
        r2 = sService;	 Catch:{ RemoteException -> 0x0043 }
        r2 = r2.getNfcTagInterface();	 Catch:{ RemoteException -> 0x0043 }
        sTagService = r2;	 Catch:{ RemoteException -> 0x0043 }
        goto L_0x0051;
    L_0x0043:
        r2 = move-exception;
        r3 = "NFC";
        r4 = "could not retrieve NFC Tag service";
        android.util.Log.e(r3, r4);	 Catch:{ all -> 0x00bb }
        r3 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x00bb }
        r3.<init>();	 Catch:{ all -> 0x00bb }
        throw r3;	 Catch:{ all -> 0x00bb }
    L_0x0051:
        if (r1 == 0) goto L_0x0081;
    L_0x0053:
        r2 = sService;	 Catch:{ RemoteException -> 0x0073 }
        r2 = r2.getNfcFCardEmulationInterface();	 Catch:{ RemoteException -> 0x0073 }
        sNfcFCardEmulationService = r2;	 Catch:{ RemoteException -> 0x0073 }
        r2 = sService;	 Catch:{ RemoteException -> 0x0065 }
        r2 = r2.getNfcCardEmulationInterface();	 Catch:{ RemoteException -> 0x0065 }
        sCardEmulationService = r2;	 Catch:{ RemoteException -> 0x0065 }
        goto L_0x0081;
    L_0x0065:
        r2 = move-exception;
        r3 = "NFC";
        r4 = "could not retrieve card emulation service";
        android.util.Log.e(r3, r4);	 Catch:{ all -> 0x00bb }
        r3 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x00bb }
        r3.<init>();	 Catch:{ all -> 0x00bb }
        throw r3;	 Catch:{ all -> 0x00bb }
    L_0x0073:
        r2 = move-exception;
        r3 = "NFC";
        r4 = "could not retrieve NFC-F card emulation service";
        android.util.Log.e(r3, r4);	 Catch:{ all -> 0x00bb }
        r3 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x00bb }
        r3.<init>();	 Catch:{ all -> 0x00bb }
        throw r3;	 Catch:{ all -> 0x00bb }
    L_0x0081:
        r2 = 1;
        sIsInitialized = r2;	 Catch:{ all -> 0x00bb }
        goto L_0x0092;
    L_0x0085:
        r2 = "NFC";
        r3 = "could not retrieve NFC service";
        android.util.Log.e(r2, r3);	 Catch:{ all -> 0x00bb }
        r2 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x00bb }
        r2.<init>();	 Catch:{ all -> 0x00bb }
        throw r2;	 Catch:{ all -> 0x00bb }
    L_0x0092:
        if (r5 != 0) goto L_0x00a4;
    L_0x0094:
        r1 = sNullContextNfcAdapter;	 Catch:{ all -> 0x00bb }
        if (r1 != 0) goto L_0x00a0;
    L_0x0098:
        r1 = new android.nfc.NfcAdapter;	 Catch:{ all -> 0x00bb }
        r2 = 0;
        r1.<init>(r2);	 Catch:{ all -> 0x00bb }
        sNullContextNfcAdapter = r1;	 Catch:{ all -> 0x00bb }
    L_0x00a0:
        r1 = sNullContextNfcAdapter;	 Catch:{ all -> 0x00bb }
        monitor-exit(r0);
        return r1;
    L_0x00a4:
        r1 = sNfcAdapters;	 Catch:{ all -> 0x00bb }
        r1 = r1.get(r5);	 Catch:{ all -> 0x00bb }
        r1 = (android.nfc.NfcAdapter) r1;	 Catch:{ all -> 0x00bb }
        if (r1 != 0) goto L_0x00b9;
    L_0x00ae:
        r2 = new android.nfc.NfcAdapter;	 Catch:{ all -> 0x00bb }
        r2.<init>(r5);	 Catch:{ all -> 0x00bb }
        r1 = r2;
        r2 = sNfcAdapters;	 Catch:{ all -> 0x00bb }
        r2.put(r5, r1);	 Catch:{ all -> 0x00bb }
    L_0x00b9:
        monitor-exit(r0);
        return r1;
    L_0x00bb:
        r5 = move-exception;
        monitor-exit(r0);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcAdapter.getNfcAdapter(android.content.Context):android.nfc.NfcAdapter");
    }

    public void setListenTechMask(int flags_ListenMask) throws IOException {
        String str = TAG;
        try {
            Log.e(str, "setListenTechMask");
            sService.setListenTechMask(flags_ListenMask);
        } catch (RemoteException e) {
            String str2 = "setListenTechMask failed";
            Log.e(str, str2, e);
            throw new IOException(str2);
        }
    }

    public int getListenTechMask() throws IOException {
        String result = TAG;
        try {
            Log.e(result, "getListenTechMask");
            result = sService.getListenTechMask();
            return result;
        } catch (RemoteException e) {
            String str = "getListenTechMask failed";
            Log.e(result, str, e);
            throw new IOException(str);
        }
    }

    private static INfcAdapter getServiceInterface() {
        IBinder b = ServiceManager.getService("nfc");
        if (b == null) {
            return null;
        }
        return Stub.asInterface(b);
    }

    public static NfcAdapter getDefaultAdapter(Context context) {
        if (context != null) {
            context = context.getApplicationContext();
            if (context != null) {
                NfcManager manager = (NfcManager) context.getSystemService("nfc");
                if (manager == null) {
                    return null;
                }
                return manager.getDefaultAdapter();
            }
            throw new IllegalArgumentException("context not associated with any application (using a mock context?)");
        }
        throw new IllegalArgumentException("context cannot be null");
    }

    @Deprecated
    @UnsupportedAppUsage
    public static NfcAdapter getDefaultAdapter() {
        Log.w(TAG, "WARNING: NfcAdapter.getDefaultAdapter() is deprecated, use NfcAdapter.getDefaultAdapter(Context) instead", new Exception());
        return getNfcAdapter(null);
    }

    NfcAdapter(Context context) {
        this.mContext = context;
        this.mNfcActivityManager = new NfcActivityManager(this);
        this.mNfcUnlockHandlers = new HashMap();
        this.mTagRemovedListener = null;
        this.mLock = new Object();
    }

    @UnsupportedAppUsage
    public Context getContext() {
        return this.mContext;
    }

    @UnsupportedAppUsage
    public INfcAdapter getService() {
        isEnabled();
        return sService;
    }

    public INfcTag getTagService() {
        isEnabled();
        return sTagService;
    }

    public INfcCardEmulation getCardEmulationService() {
        isEnabled();
        return sCardEmulationService;
    }

    public INfcFCardEmulation getNfcFCardEmulationService() {
        isEnabled();
        return sNfcFCardEmulationService;
    }

    public INfcDta getNfcDtaInterface() {
        Context context = this.mContext;
        if (context != null) {
            try {
                return sService.getNfcDtaInterface(context.getPackageName());
            } catch (RemoteException e) {
                attemptDeadServiceRecovery(e);
                return null;
            }
        }
        throw new UnsupportedOperationException("You need a context on NfcAdapter to use the  NFC extras APIs");
    }

    @UnsupportedAppUsage
    public void attemptDeadServiceRecovery(Exception e) {
        String str = TAG;
        Log.e(str, "NFC service dead - attempting to recover", e);
        INfcAdapter service = getServiceInterface();
        if (service == null) {
            Log.e(str, "could not retrieve NFC service during service recovery");
            return;
        }
        sService = service;
        try {
            sTagService = service.getNfcTagInterface();
            try {
                sCardEmulationService = service.getNfcCardEmulationInterface();
            } catch (RemoteException e2) {
                Log.e(str, "could not retrieve NFC card emulation service during service recovery");
            }
            try {
                sNfcFCardEmulationService = service.getNfcFCardEmulationInterface();
            } catch (RemoteException e3) {
                Log.e(str, "could not retrieve NFC-F card emulation service during service recovery");
            }
        } catch (RemoteException e4) {
            Log.e(str, "could not retrieve NFC tag service during service recovery");
        }
    }

    public boolean isEnabled() {
        boolean z = false;
        try {
            if (sService.getState() == 3) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    @UnsupportedAppUsage
    public int getAdapterState() {
        try {
            return sService.getState();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return 1;
        }
    }

    @SystemApi
    public boolean enable() {
        try {
            return sService.enable();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    @SystemApi
    public boolean disable() {
        try {
            return sService.disable(true);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    @SystemApi
    public boolean disable(boolean persist) {
        try {
            return sService.disable(persist);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    public void pausePolling(int timeoutInMs) {
        try {
            sService.pausePolling(timeoutInMs);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
        }
    }

    public void resumePolling() {
        try {
            sService.resumePolling();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
        }
    }

    /* JADX WARNING: Missing block: B:10:0x000e, code skipped:
            if (r7 == null) goto L_0x0049;
     */
    /* JADX WARNING: Missing block: B:11:0x0010, code skipped:
            if (r6 == null) goto L_0x0043;
     */
    /* JADX WARNING: Missing block: B:12:0x0012, code skipped:
            r0 = r6.length;
            r1 = 0;
     */
    /* JADX WARNING: Missing block: B:13:0x0014, code skipped:
            if (r1 >= r0) goto L_0x0043;
     */
    /* JADX WARNING: Missing block: B:14:0x0016, code skipped:
            r2 = r6[r1];
     */
    /* JADX WARNING: Missing block: B:15:0x0018, code skipped:
            if (r2 == null) goto L_0x003b;
     */
    /* JADX WARNING: Missing block: B:16:0x001a, code skipped:
            r3 = r2.getScheme();
     */
    /* JADX WARNING: Missing block: B:17:0x001e, code skipped:
            if (r3 == null) goto L_0x0033;
     */
    /* JADX WARNING: Missing block: B:19:0x0026, code skipped:
            if (r3.equalsIgnoreCase(android.content.ContentResolver.SCHEME_FILE) != false) goto L_0x0030;
     */
    /* JADX WARNING: Missing block: B:21:0x002e, code skipped:
            if (r3.equalsIgnoreCase("content") == false) goto L_0x0033;
     */
    /* JADX WARNING: Missing block: B:22:0x0030, code skipped:
            r1 = r1 + 1;
     */
    /* JADX WARNING: Missing block: B:24:0x003a, code skipped:
            throw new java.lang.IllegalArgumentException("URI needs to have either scheme file or scheme content");
     */
    /* JADX WARNING: Missing block: B:26:0x0042, code skipped:
            throw new java.lang.NullPointerException("Uri not allowed to be null");
     */
    /* JADX WARNING: Missing block: B:27:0x0043, code skipped:
            r5.mNfcActivityManager.setNdefPushContentUri(r7, r6);
     */
    /* JADX WARNING: Missing block: B:28:0x0048, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:30:0x0050, code skipped:
            throw new java.lang.NullPointerException("activity cannot be null");
     */
    @java.lang.Deprecated
    public void setBeamPushUris(android.net.Uri[] r6, android.app.Activity r7) {
        /*
        r5 = this;
        r0 = android.nfc.NfcAdapter.class;
        monitor-enter(r0);
        r1 = sHasNfcFeature;	 Catch:{ all -> 0x0057 }
        if (r1 == 0) goto L_0x0051;
    L_0x0007:
        r1 = sHasBeamFeature;	 Catch:{ all -> 0x0057 }
        if (r1 != 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x0057 }
        return;
    L_0x000d:
        monitor-exit(r0);	 Catch:{ all -> 0x0057 }
        if (r7 == 0) goto L_0x0049;
    L_0x0010:
        if (r6 == 0) goto L_0x0043;
    L_0x0012:
        r0 = r6.length;
        r1 = 0;
    L_0x0014:
        if (r1 >= r0) goto L_0x0043;
    L_0x0016:
        r2 = r6[r1];
        if (r2 == 0) goto L_0x003b;
    L_0x001a:
        r3 = r2.getScheme();
        if (r3 == 0) goto L_0x0033;
    L_0x0020:
        r4 = "file";
        r4 = r3.equalsIgnoreCase(r4);
        if (r4 != 0) goto L_0x0030;
    L_0x0028:
        r4 = "content";
        r4 = r3.equalsIgnoreCase(r4);
        if (r4 == 0) goto L_0x0033;
    L_0x0030:
        r1 = r1 + 1;
        goto L_0x0014;
    L_0x0033:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "URI needs to have either scheme file or scheme content";
        r0.<init>(r1);
        throw r0;
    L_0x003b:
        r0 = new java.lang.NullPointerException;
        r1 = "Uri not allowed to be null";
        r0.<init>(r1);
        throw r0;
    L_0x0043:
        r0 = r5.mNfcActivityManager;
        r0.setNdefPushContentUri(r7, r6);
        return;
    L_0x0049:
        r0 = new java.lang.NullPointerException;
        r1 = "activity cannot be null";
        r0.<init>(r1);
        throw r0;
    L_0x0051:
        r1 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x0057 }
        r1.<init>();	 Catch:{ all -> 0x0057 }
        throw r1;	 Catch:{ all -> 0x0057 }
    L_0x0057:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0057 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcAdapter.setBeamPushUris(android.net.Uri[], android.app.Activity):void");
    }

    /* JADX WARNING: Missing block: B:10:0x000e, code skipped:
            if (r4 == null) goto L_0x0016;
     */
    /* JADX WARNING: Missing block: B:11:0x0010, code skipped:
            r2.mNfcActivityManager.setNdefPushContentUriCallback(r4, r3);
     */
    /* JADX WARNING: Missing block: B:12:0x0015, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:14:0x001d, code skipped:
            throw new java.lang.NullPointerException("activity cannot be null");
     */
    @java.lang.Deprecated
    public void setBeamPushUrisCallback(android.nfc.NfcAdapter.CreateBeamUrisCallback r3, android.app.Activity r4) {
        /*
        r2 = this;
        r0 = android.nfc.NfcAdapter.class;
        monitor-enter(r0);
        r1 = sHasNfcFeature;	 Catch:{ all -> 0x0024 }
        if (r1 == 0) goto L_0x001e;
    L_0x0007:
        r1 = sHasBeamFeature;	 Catch:{ all -> 0x0024 }
        if (r1 != 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x0024 }
        return;
    L_0x000d:
        monitor-exit(r0);	 Catch:{ all -> 0x0024 }
        if (r4 == 0) goto L_0x0016;
    L_0x0010:
        r0 = r2.mNfcActivityManager;
        r0.setNdefPushContentUriCallback(r4, r3);
        return;
    L_0x0016:
        r0 = new java.lang.NullPointerException;
        r1 = "activity cannot be null";
        r0.<init>(r1);
        throw r0;
    L_0x001e:
        r1 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x0024 }
        r1.<init>();	 Catch:{ all -> 0x0024 }
        throw r1;	 Catch:{ all -> 0x0024 }
    L_0x0024:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0024 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcAdapter.setBeamPushUrisCallback(android.nfc.NfcAdapter$CreateBeamUrisCallback, android.app.Activity):void");
    }

    /* JADX WARNING: Missing block: B:10:0x000e, code skipped:
            r0 = getSdkVersion();
     */
    /* JADX WARNING: Missing block: B:11:0x0012, code skipped:
            if (r8 == null) goto L_0x0035;
     */
    /* JADX WARNING: Missing block: B:13:?, code skipped:
            r6.mNfcActivityManager.setNdefPushMessage(r8, r7, 0);
            r1 = r9.length;
            r3 = 0;
     */
    /* JADX WARNING: Missing block: B:14:0x001c, code skipped:
            if (r3 >= r1) goto L_0x0032;
     */
    /* JADX WARNING: Missing block: B:15:0x001e, code skipped:
            r4 = r9[r3];
     */
    /* JADX WARNING: Missing block: B:16:0x0020, code skipped:
            if (r4 == null) goto L_0x002a;
     */
    /* JADX WARNING: Missing block: B:17:0x0022, code skipped:
            r6.mNfcActivityManager.setNdefPushMessage(r4, r7, 0);
            r3 = r3 + 1;
     */
    /* JADX WARNING: Missing block: B:19:0x0031, code skipped:
            throw new java.lang.NullPointerException("activities cannot contain null");
     */
    /* JADX WARNING: Missing block: B:21:0x0033, code skipped:
            r1 = move-exception;
     */
    /* JADX WARNING: Missing block: B:24:0x003c, code skipped:
            throw new java.lang.NullPointerException("activity cannot be null");
     */
    /* JADX WARNING: Missing block: B:26:0x003f, code skipped:
            if (r0 < 16) goto L_0x0041;
     */
    /* JADX WARNING: Missing block: B:27:0x0041, code skipped:
            android.util.Log.e(TAG, "Cannot call API with Activity that has already been destroyed", r1);
     */
    /* JADX WARNING: Missing block: B:28:0x0048, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:29:0x0049, code skipped:
            throw r1;
     */
    @java.lang.Deprecated
    public void setNdefPushMessage(android.nfc.NdefMessage r7, android.app.Activity r8, android.app.Activity... r9) {
        /*
        r6 = this;
        r0 = android.nfc.NfcAdapter.class;
        monitor-enter(r0);
        r1 = sHasNfcFeature;	 Catch:{ all -> 0x0050 }
        if (r1 == 0) goto L_0x004a;
    L_0x0007:
        r1 = sHasBeamFeature;	 Catch:{ all -> 0x0050 }
        if (r1 != 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x0050 }
        return;
    L_0x000d:
        monitor-exit(r0);	 Catch:{ all -> 0x0050 }
        r0 = r6.getSdkVersion();
        if (r8 == 0) goto L_0x0035;
    L_0x0014:
        r1 = r6.mNfcActivityManager;	 Catch:{ IllegalStateException -> 0x0033 }
        r2 = 0;
        r1.setNdefPushMessage(r8, r7, r2);	 Catch:{ IllegalStateException -> 0x0033 }
        r1 = r9.length;	 Catch:{ IllegalStateException -> 0x0033 }
        r3 = r2;
    L_0x001c:
        if (r3 >= r1) goto L_0x0032;
    L_0x001e:
        r4 = r9[r3];	 Catch:{ IllegalStateException -> 0x0033 }
        if (r4 == 0) goto L_0x002a;
    L_0x0022:
        r5 = r6.mNfcActivityManager;	 Catch:{ IllegalStateException -> 0x0033 }
        r5.setNdefPushMessage(r4, r7, r2);	 Catch:{ IllegalStateException -> 0x0033 }
        r3 = r3 + 1;
        goto L_0x001c;
    L_0x002a:
        r1 = new java.lang.NullPointerException;	 Catch:{ IllegalStateException -> 0x0033 }
        r2 = "activities cannot contain null";
        r1.<init>(r2);	 Catch:{ IllegalStateException -> 0x0033 }
        throw r1;	 Catch:{ IllegalStateException -> 0x0033 }
    L_0x0032:
        goto L_0x0048;
    L_0x0033:
        r1 = move-exception;
        goto L_0x003d;
    L_0x0035:
        r1 = new java.lang.NullPointerException;	 Catch:{ IllegalStateException -> 0x0033 }
        r2 = "activity cannot be null";
        r1.<init>(r2);	 Catch:{ IllegalStateException -> 0x0033 }
        throw r1;	 Catch:{ IllegalStateException -> 0x0033 }
    L_0x003d:
        r2 = 16;
        if (r0 >= r2) goto L_0x0049;
    L_0x0041:
        r2 = "NFC";
        r3 = "Cannot call API with Activity that has already been destroyed";
        android.util.Log.e(r2, r3, r1);
    L_0x0048:
        return;
    L_0x0049:
        throw r1;
    L_0x004a:
        r1 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x0050 }
        r1.<init>();	 Catch:{ all -> 0x0050 }
        throw r1;	 Catch:{ all -> 0x0050 }
    L_0x0050:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0050 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcAdapter.setNdefPushMessage(android.nfc.NdefMessage, android.app.Activity, android.app.Activity[]):void");
    }

    @SystemApi
    public void setNdefPushMessage(NdefMessage message, Activity activity, int flags) {
        synchronized (NfcAdapter.class) {
            if (sHasNfcFeature) {
            } else {
                throw new UnsupportedOperationException();
            }
        }
        if (activity != null) {
            this.mNfcActivityManager.setNdefPushMessage(activity, message, flags);
            return;
        }
        throw new NullPointerException("activity cannot be null");
    }

    /* JADX WARNING: Missing block: B:10:0x000e, code skipped:
            r0 = getSdkVersion();
     */
    /* JADX WARNING: Missing block: B:11:0x0012, code skipped:
            if (r8 == null) goto L_0x0035;
     */
    /* JADX WARNING: Missing block: B:13:?, code skipped:
            r6.mNfcActivityManager.setNdefPushMessageCallback(r8, r7, 0);
            r1 = r9.length;
            r3 = 0;
     */
    /* JADX WARNING: Missing block: B:14:0x001c, code skipped:
            if (r3 >= r1) goto L_0x0032;
     */
    /* JADX WARNING: Missing block: B:15:0x001e, code skipped:
            r4 = r9[r3];
     */
    /* JADX WARNING: Missing block: B:16:0x0020, code skipped:
            if (r4 == null) goto L_0x002a;
     */
    /* JADX WARNING: Missing block: B:17:0x0022, code skipped:
            r6.mNfcActivityManager.setNdefPushMessageCallback(r4, r7, 0);
            r3 = r3 + 1;
     */
    /* JADX WARNING: Missing block: B:19:0x0031, code skipped:
            throw new java.lang.NullPointerException("activities cannot contain null");
     */
    /* JADX WARNING: Missing block: B:21:0x0033, code skipped:
            r1 = move-exception;
     */
    /* JADX WARNING: Missing block: B:24:0x003c, code skipped:
            throw new java.lang.NullPointerException("activity cannot be null");
     */
    /* JADX WARNING: Missing block: B:26:0x003f, code skipped:
            if (r0 < 16) goto L_0x0041;
     */
    /* JADX WARNING: Missing block: B:27:0x0041, code skipped:
            android.util.Log.e(TAG, "Cannot call API with Activity that has already been destroyed", r1);
     */
    /* JADX WARNING: Missing block: B:28:0x0048, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:29:0x0049, code skipped:
            throw r1;
     */
    @java.lang.Deprecated
    public void setNdefPushMessageCallback(android.nfc.NfcAdapter.CreateNdefMessageCallback r7, android.app.Activity r8, android.app.Activity... r9) {
        /*
        r6 = this;
        r0 = android.nfc.NfcAdapter.class;
        monitor-enter(r0);
        r1 = sHasNfcFeature;	 Catch:{ all -> 0x0050 }
        if (r1 == 0) goto L_0x004a;
    L_0x0007:
        r1 = sHasBeamFeature;	 Catch:{ all -> 0x0050 }
        if (r1 != 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x0050 }
        return;
    L_0x000d:
        monitor-exit(r0);	 Catch:{ all -> 0x0050 }
        r0 = r6.getSdkVersion();
        if (r8 == 0) goto L_0x0035;
    L_0x0014:
        r1 = r6.mNfcActivityManager;	 Catch:{ IllegalStateException -> 0x0033 }
        r2 = 0;
        r1.setNdefPushMessageCallback(r8, r7, r2);	 Catch:{ IllegalStateException -> 0x0033 }
        r1 = r9.length;	 Catch:{ IllegalStateException -> 0x0033 }
        r3 = r2;
    L_0x001c:
        if (r3 >= r1) goto L_0x0032;
    L_0x001e:
        r4 = r9[r3];	 Catch:{ IllegalStateException -> 0x0033 }
        if (r4 == 0) goto L_0x002a;
    L_0x0022:
        r5 = r6.mNfcActivityManager;	 Catch:{ IllegalStateException -> 0x0033 }
        r5.setNdefPushMessageCallback(r4, r7, r2);	 Catch:{ IllegalStateException -> 0x0033 }
        r3 = r3 + 1;
        goto L_0x001c;
    L_0x002a:
        r1 = new java.lang.NullPointerException;	 Catch:{ IllegalStateException -> 0x0033 }
        r2 = "activities cannot contain null";
        r1.<init>(r2);	 Catch:{ IllegalStateException -> 0x0033 }
        throw r1;	 Catch:{ IllegalStateException -> 0x0033 }
    L_0x0032:
        goto L_0x0048;
    L_0x0033:
        r1 = move-exception;
        goto L_0x003d;
    L_0x0035:
        r1 = new java.lang.NullPointerException;	 Catch:{ IllegalStateException -> 0x0033 }
        r2 = "activity cannot be null";
        r1.<init>(r2);	 Catch:{ IllegalStateException -> 0x0033 }
        throw r1;	 Catch:{ IllegalStateException -> 0x0033 }
    L_0x003d:
        r2 = 16;
        if (r0 >= r2) goto L_0x0049;
    L_0x0041:
        r2 = "NFC";
        r3 = "Cannot call API with Activity that has already been destroyed";
        android.util.Log.e(r2, r3, r1);
    L_0x0048:
        return;
    L_0x0049:
        throw r1;
    L_0x004a:
        r1 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x0050 }
        r1.<init>();	 Catch:{ all -> 0x0050 }
        throw r1;	 Catch:{ all -> 0x0050 }
    L_0x0050:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0050 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcAdapter.setNdefPushMessageCallback(android.nfc.NfcAdapter$CreateNdefMessageCallback, android.app.Activity, android.app.Activity[]):void");
    }

    @UnsupportedAppUsage
    public void setNdefPushMessageCallback(CreateNdefMessageCallback callback, Activity activity, int flags) {
        if (activity != null) {
            this.mNfcActivityManager.setNdefPushMessageCallback(activity, callback, flags);
            return;
        }
        throw new NullPointerException("activity cannot be null");
    }

    /* JADX WARNING: Missing block: B:10:0x000e, code skipped:
            r0 = getSdkVersion();
     */
    /* JADX WARNING: Missing block: B:11:0x0012, code skipped:
            if (r7 == null) goto L_0x0034;
     */
    /* JADX WARNING: Missing block: B:13:?, code skipped:
            r5.mNfcActivityManager.setOnNdefPushCompleteCallback(r7, r6);
            r1 = r8.length;
            r2 = 0;
     */
    /* JADX WARNING: Missing block: B:14:0x001b, code skipped:
            if (r2 >= r1) goto L_0x0031;
     */
    /* JADX WARNING: Missing block: B:15:0x001d, code skipped:
            r3 = r8[r2];
     */
    /* JADX WARNING: Missing block: B:16:0x001f, code skipped:
            if (r3 == null) goto L_0x0029;
     */
    /* JADX WARNING: Missing block: B:17:0x0021, code skipped:
            r5.mNfcActivityManager.setOnNdefPushCompleteCallback(r3, r6);
            r2 = r2 + 1;
     */
    /* JADX WARNING: Missing block: B:19:0x0030, code skipped:
            throw new java.lang.NullPointerException("activities cannot contain null");
     */
    /* JADX WARNING: Missing block: B:21:0x0032, code skipped:
            r1 = move-exception;
     */
    /* JADX WARNING: Missing block: B:24:0x003b, code skipped:
            throw new java.lang.NullPointerException("activity cannot be null");
     */
    /* JADX WARNING: Missing block: B:26:0x003e, code skipped:
            if (r0 < 16) goto L_0x0040;
     */
    /* JADX WARNING: Missing block: B:27:0x0040, code skipped:
            android.util.Log.e(TAG, "Cannot call API with Activity that has already been destroyed", r1);
     */
    /* JADX WARNING: Missing block: B:28:0x0047, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:29:0x0048, code skipped:
            throw r1;
     */
    @java.lang.Deprecated
    public void setOnNdefPushCompleteCallback(android.nfc.NfcAdapter.OnNdefPushCompleteCallback r6, android.app.Activity r7, android.app.Activity... r8) {
        /*
        r5 = this;
        r0 = android.nfc.NfcAdapter.class;
        monitor-enter(r0);
        r1 = sHasNfcFeature;	 Catch:{ all -> 0x004f }
        if (r1 == 0) goto L_0x0049;
    L_0x0007:
        r1 = sHasBeamFeature;	 Catch:{ all -> 0x004f }
        if (r1 != 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x004f }
        return;
    L_0x000d:
        monitor-exit(r0);	 Catch:{ all -> 0x004f }
        r0 = r5.getSdkVersion();
        if (r7 == 0) goto L_0x0034;
    L_0x0014:
        r1 = r5.mNfcActivityManager;	 Catch:{ IllegalStateException -> 0x0032 }
        r1.setOnNdefPushCompleteCallback(r7, r6);	 Catch:{ IllegalStateException -> 0x0032 }
        r1 = r8.length;	 Catch:{ IllegalStateException -> 0x0032 }
        r2 = 0;
    L_0x001b:
        if (r2 >= r1) goto L_0x0031;
    L_0x001d:
        r3 = r8[r2];	 Catch:{ IllegalStateException -> 0x0032 }
        if (r3 == 0) goto L_0x0029;
    L_0x0021:
        r4 = r5.mNfcActivityManager;	 Catch:{ IllegalStateException -> 0x0032 }
        r4.setOnNdefPushCompleteCallback(r3, r6);	 Catch:{ IllegalStateException -> 0x0032 }
        r2 = r2 + 1;
        goto L_0x001b;
    L_0x0029:
        r1 = new java.lang.NullPointerException;	 Catch:{ IllegalStateException -> 0x0032 }
        r2 = "activities cannot contain null";
        r1.<init>(r2);	 Catch:{ IllegalStateException -> 0x0032 }
        throw r1;	 Catch:{ IllegalStateException -> 0x0032 }
    L_0x0031:
        goto L_0x0047;
    L_0x0032:
        r1 = move-exception;
        goto L_0x003c;
    L_0x0034:
        r1 = new java.lang.NullPointerException;	 Catch:{ IllegalStateException -> 0x0032 }
        r2 = "activity cannot be null";
        r1.<init>(r2);	 Catch:{ IllegalStateException -> 0x0032 }
        throw r1;	 Catch:{ IllegalStateException -> 0x0032 }
    L_0x003c:
        r2 = 16;
        if (r0 >= r2) goto L_0x0048;
    L_0x0040:
        r2 = "NFC";
        r3 = "Cannot call API with Activity that has already been destroyed";
        android.util.Log.e(r2, r3, r1);
    L_0x0047:
        return;
    L_0x0048:
        throw r1;
    L_0x0049:
        r1 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x004f }
        r1.<init>();	 Catch:{ all -> 0x004f }
        throw r1;	 Catch:{ all -> 0x004f }
    L_0x004f:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x004f }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcAdapter.setOnNdefPushCompleteCallback(android.nfc.NfcAdapter$OnNdefPushCompleteCallback, android.app.Activity, android.app.Activity[]):void");
    }

    public void enableForegroundDispatch(Activity activity, PendingIntent intent, IntentFilter[] filters, String[][] techLists) {
        synchronized (NfcAdapter.class) {
            if (sHasNfcFeature) {
            } else {
                throw new UnsupportedOperationException();
            }
        }
        if (activity == null || intent == null) {
            throw new NullPointerException();
        } else if (activity.isResumed()) {
            TechListParcel parcel = null;
            if (techLists != null) {
                try {
                    if (techLists.length > 0) {
                        parcel = new TechListParcel(techLists);
                    }
                } catch (RemoteException e) {
                    attemptDeadServiceRecovery(e);
                    return;
                }
            }
            ActivityThread.currentActivityThread().registerOnActivityPausedListener(activity, this.mForegroundDispatchListener);
            sService.setForegroundDispatch(intent, filters, parcel);
        } else {
            throw new IllegalStateException("Foreground dispatch can only be enabled when your activity is resumed");
        }
    }

    public void disableForegroundDispatch(Activity activity) {
        synchronized (NfcAdapter.class) {
            if (sHasNfcFeature) {
            } else {
                throw new UnsupportedOperationException();
            }
        }
        ActivityThread.currentActivityThread().unregisterOnActivityPausedListener(activity, this.mForegroundDispatchListener);
        disableForegroundDispatchInternal(activity, false);
    }

    /* Access modifiers changed, original: 0000 */
    public void disableForegroundDispatchInternal(Activity activity, boolean force) {
        try {
            sService.setForegroundDispatch(null, null, null);
            if (!force) {
                if (!activity.isResumed()) {
                    throw new IllegalStateException("You must disable foreground dispatching while your activity is still resumed");
                }
            }
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
        }
    }

    public void enableReaderMode(Activity activity, ReaderCallback callback, int flags, Bundle extras) {
        synchronized (NfcAdapter.class) {
            if (sHasNfcFeature) {
            } else {
                throw new UnsupportedOperationException();
            }
        }
        this.mNfcActivityManager.enableReaderMode(activity, callback, flags, extras);
    }

    public void disableReaderMode(Activity activity) {
        synchronized (NfcAdapter.class) {
            if (sHasNfcFeature) {
            } else {
                throw new UnsupportedOperationException();
            }
        }
        this.mNfcActivityManager.disableReaderMode(activity);
    }

    /* JADX WARNING: Missing block: B:10:0x000f, code skipped:
            if (r5 == null) goto L_0x0028;
     */
    /* JADX WARNING: Missing block: B:11:0x0011, code skipped:
            enforceResumed(r5);
     */
    /* JADX WARNING: Missing block: B:13:?, code skipped:
            sService.invokeBeam();
     */
    /* JADX WARNING: Missing block: B:15:0x001a, code skipped:
            return true;
     */
    /* JADX WARNING: Missing block: B:16:0x001b, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:17:0x001c, code skipped:
            android.util.Log.e(TAG, "invokeBeam: NFC process has died.");
            attemptDeadServiceRecovery(r0);
     */
    /* JADX WARNING: Missing block: B:18:0x0027, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:20:0x002f, code skipped:
            throw new java.lang.NullPointerException("activity may not be null.");
     */
    @java.lang.Deprecated
    public boolean invokeBeam(android.app.Activity r5) {
        /*
        r4 = this;
        r0 = android.nfc.NfcAdapter.class;
        monitor-enter(r0);
        r1 = sHasNfcFeature;	 Catch:{ all -> 0x0036 }
        if (r1 == 0) goto L_0x0030;
    L_0x0007:
        r1 = sHasBeamFeature;	 Catch:{ all -> 0x0036 }
        r2 = 0;
        if (r1 != 0) goto L_0x000e;
    L_0x000c:
        monitor-exit(r0);	 Catch:{ all -> 0x0036 }
        return r2;
    L_0x000e:
        monitor-exit(r0);	 Catch:{ all -> 0x0036 }
        if (r5 == 0) goto L_0x0028;
    L_0x0011:
        r4.enforceResumed(r5);
        r0 = sService;	 Catch:{ RemoteException -> 0x001b }
        r0.invokeBeam();	 Catch:{ RemoteException -> 0x001b }
        r0 = 1;
        return r0;
    L_0x001b:
        r0 = move-exception;
        r1 = "NFC";
        r3 = "invokeBeam: NFC process has died.";
        android.util.Log.e(r1, r3);
        r4.attemptDeadServiceRecovery(r0);
        return r2;
    L_0x0028:
        r0 = new java.lang.NullPointerException;
        r1 = "activity may not be null.";
        r0.<init>(r1);
        throw r0;
    L_0x0030:
        r1 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x0036 }
        r1.<init>();	 Catch:{ all -> 0x0036 }
        throw r1;	 Catch:{ all -> 0x0036 }
    L_0x0036:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0036 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcAdapter.invokeBeam(android.app.Activity):boolean");
    }

    public boolean invokeBeam(BeamShareData shareData) {
        String str = TAG;
        try {
            Log.e(str, "invokeBeamInternal()");
            sService.invokeBeamInternal(shareData);
            return true;
        } catch (RemoteException e) {
            Log.e(str, "invokeBeam: NFC process has died.");
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    /* JADX WARNING: Missing block: B:10:0x000e, code skipped:
            if (r3 == null) goto L_0x001c;
     */
    /* JADX WARNING: Missing block: B:11:0x0010, code skipped:
            if (r4 == null) goto L_0x001c;
     */
    /* JADX WARNING: Missing block: B:12:0x0012, code skipped:
            enforceResumed(r3);
            r2.mNfcActivityManager.setNdefPushMessage(r3, r4, 0);
     */
    /* JADX WARNING: Missing block: B:13:0x001b, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:15:0x0021, code skipped:
            throw new java.lang.NullPointerException();
     */
    @java.lang.Deprecated
    public void enableForegroundNdefPush(android.app.Activity r3, android.nfc.NdefMessage r4) {
        /*
        r2 = this;
        r0 = android.nfc.NfcAdapter.class;
        monitor-enter(r0);
        r1 = sHasNfcFeature;	 Catch:{ all -> 0x0028 }
        if (r1 == 0) goto L_0x0022;
    L_0x0007:
        r1 = sHasBeamFeature;	 Catch:{ all -> 0x0028 }
        if (r1 != 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x0028 }
        return;
    L_0x000d:
        monitor-exit(r0);	 Catch:{ all -> 0x0028 }
        if (r3 == 0) goto L_0x001c;
    L_0x0010:
        if (r4 == 0) goto L_0x001c;
    L_0x0012:
        r2.enforceResumed(r3);
        r0 = r2.mNfcActivityManager;
        r1 = 0;
        r0.setNdefPushMessage(r3, r4, r1);
        return;
    L_0x001c:
        r0 = new java.lang.NullPointerException;
        r0.<init>();
        throw r0;
    L_0x0022:
        r1 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x0028 }
        r1.<init>();	 Catch:{ all -> 0x0028 }
        throw r1;	 Catch:{ all -> 0x0028 }
    L_0x0028:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0028 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcAdapter.enableForegroundNdefPush(android.app.Activity, android.nfc.NdefMessage):void");
    }

    /* JADX WARNING: Missing block: B:10:0x000e, code skipped:
            if (r4 == null) goto L_0x0025;
     */
    /* JADX WARNING: Missing block: B:11:0x0010, code skipped:
            enforceResumed(r4);
            r3.mNfcActivityManager.setNdefPushMessage(r4, null, 0);
            r3.mNfcActivityManager.setNdefPushMessageCallback(r4, null, 0);
            r3.mNfcActivityManager.setOnNdefPushCompleteCallback(r4, null);
     */
    /* JADX WARNING: Missing block: B:12:0x0024, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:14:0x002a, code skipped:
            throw new java.lang.NullPointerException();
     */
    @java.lang.Deprecated
    public void disableForegroundNdefPush(android.app.Activity r4) {
        /*
        r3 = this;
        r0 = android.nfc.NfcAdapter.class;
        monitor-enter(r0);
        r1 = sHasNfcFeature;	 Catch:{ all -> 0x0031 }
        if (r1 == 0) goto L_0x002b;
    L_0x0007:
        r1 = sHasBeamFeature;	 Catch:{ all -> 0x0031 }
        if (r1 != 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x0031 }
        return;
    L_0x000d:
        monitor-exit(r0);	 Catch:{ all -> 0x0031 }
        if (r4 == 0) goto L_0x0025;
    L_0x0010:
        r3.enforceResumed(r4);
        r0 = r3.mNfcActivityManager;
        r1 = 0;
        r2 = 0;
        r0.setNdefPushMessage(r4, r2, r1);
        r0 = r3.mNfcActivityManager;
        r0.setNdefPushMessageCallback(r4, r2, r1);
        r0 = r3.mNfcActivityManager;
        r0.setOnNdefPushCompleteCallback(r4, r2);
        return;
    L_0x0025:
        r0 = new java.lang.NullPointerException;
        r0.<init>();
        throw r0;
    L_0x002b:
        r1 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x0031 }
        r1.<init>();	 Catch:{ all -> 0x0031 }
        throw r1;	 Catch:{ all -> 0x0031 }
    L_0x0031:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0031 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcAdapter.disableForegroundNdefPush(android.app.Activity):void");
    }

    @SystemApi
    public boolean enableSecureNfc(boolean enable) {
        if (sHasNfcFeature) {
            try {
                return sService.setNfcSecure(enable);
            } catch (RemoteException e) {
                attemptDeadServiceRecovery(e);
                return false;
            }
        }
        throw new UnsupportedOperationException();
    }

    public boolean isSecureNfcSupported() {
        if (sHasNfcFeature) {
            try {
                return sService.deviceSupportsNfcSecure();
            } catch (RemoteException e) {
                attemptDeadServiceRecovery(e);
                return false;
            }
        }
        throw new UnsupportedOperationException();
    }

    public boolean isSecureNfcEnabled() {
        if (sHasNfcFeature) {
            try {
                return sService.isNfcSecureEnabled();
            } catch (RemoteException e) {
                attemptDeadServiceRecovery(e);
                return false;
            }
        }
        throw new UnsupportedOperationException();
    }

    @SystemApi
    public boolean enableNdefPush() {
        if (sHasNfcFeature) {
            try {
                return sService.enableNdefPush();
            } catch (RemoteException e) {
                attemptDeadServiceRecovery(e);
                return false;
            }
        }
        throw new UnsupportedOperationException();
    }

    @SystemApi
    public boolean disableNdefPush() {
        synchronized (NfcAdapter.class) {
            if (sHasNfcFeature) {
            } else {
                throw new UnsupportedOperationException();
            }
        }
        try {
            return sService.disableNdefPush();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    @Deprecated
    public boolean isNdefPushEnabled() {
        synchronized (NfcAdapter.class) {
            if (!sHasNfcFeature) {
                throw new UnsupportedOperationException();
            } else if (sHasBeamFeature) {
                try {
                    return sService.isNdefPushEnabled();
                } catch (RemoteException e) {
                    attemptDeadServiceRecovery(e);
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public boolean ignore(Tag tag, int debounceMs, final OnTagRemovedListener tagRemovedListener, final Handler handler) {
        ITagRemovedCallback.Stub iListener = null;
        if (tagRemovedListener != null) {
            iListener = new ITagRemovedCallback.Stub() {
                public void onTagRemoved() throws RemoteException {
                    Handler handler = handler;
                    if (handler != null) {
                        handler.post(new Runnable() {
                            public void run() {
                                tagRemovedListener.onTagRemoved();
                            }
                        });
                    } else {
                        tagRemovedListener.onTagRemoved();
                    }
                    synchronized (NfcAdapter.this.mLock) {
                        NfcAdapter.this.mTagRemovedListener = null;
                    }
                }
            };
        }
        synchronized (this.mLock) {
            this.mTagRemovedListener = iListener;
        }
        try {
            return sService.ignore(tag.getServiceHandle(), debounceMs, iListener);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void dispatch(Tag tag) {
        if (tag != null) {
            try {
                sService.dispatch(tag);
                return;
            } catch (RemoteException e) {
                attemptDeadServiceRecovery(e);
                return;
            }
        }
        throw new NullPointerException("tag cannot be null");
    }

    public void setP2pModes(int initiatorModes, int targetModes) {
        try {
            sService.setP2pModes(initiatorModes, targetModes);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
        }
    }

    @SystemApi
    public boolean addNfcUnlockHandler(final NfcUnlockHandler unlockHandler, String[] tagTechnologies) {
        synchronized (NfcAdapter.class) {
            if (sHasNfcFeature) {
            } else {
                throw new UnsupportedOperationException();
            }
        }
        if (tagTechnologies.length == 0) {
            return false;
        }
        try {
            synchronized (this.mLock) {
                if (this.mNfcUnlockHandlers.containsKey(unlockHandler)) {
                    sService.removeNfcUnlockHandler((INfcUnlockHandler) this.mNfcUnlockHandlers.get(unlockHandler));
                    this.mNfcUnlockHandlers.remove(unlockHandler);
                }
                INfcUnlockHandler.Stub iHandler = new INfcUnlockHandler.Stub() {
                    public boolean onUnlockAttempted(Tag tag) throws RemoteException {
                        return unlockHandler.onUnlockAttempted(tag);
                    }
                };
                sService.addNfcUnlockHandler(iHandler, Tag.getTechCodesFromStrings(tagTechnologies));
                this.mNfcUnlockHandlers.put(unlockHandler, iHandler);
            }
            return true;
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        } catch (IllegalArgumentException e2) {
            Log.e(TAG, "Unable to register LockscreenDispatch", e2);
            return false;
        }
    }

    @SystemApi
    public boolean removeNfcUnlockHandler(NfcUnlockHandler unlockHandler) {
        synchronized (NfcAdapter.class) {
            if (sHasNfcFeature) {
            } else {
                throw new UnsupportedOperationException();
            }
        }
        try {
            synchronized (this.mLock) {
                if (this.mNfcUnlockHandlers.containsKey(unlockHandler)) {
                    sService.removeNfcUnlockHandler((INfcUnlockHandler) this.mNfcUnlockHandlers.remove(unlockHandler));
                }
            }
            return true;
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    @UnsupportedAppUsage
    public INfcAdapterExtras getNfcAdapterExtrasInterface() {
        Context context = this.mContext;
        if (context != null) {
            try {
                return sService.getNfcAdapterExtrasInterface(context.getPackageName());
            } catch (RemoteException e) {
                attemptDeadServiceRecovery(e);
                return null;
            }
        }
        throw new UnsupportedOperationException("You need a context on NfcAdapter to use the  NFC extras APIs");
    }

    /* Access modifiers changed, original: 0000 */
    public void enforceResumed(Activity activity) {
        if (!activity.isResumed()) {
            throw new IllegalStateException("API cannot be called while activity is paused");
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int getSdkVersion() {
        Context context = this.mContext;
        if (context == null) {
            return 9;
        }
        return context.getApplicationInfo().targetSdkVersion;
    }

    public int setConfig(String aid) {
        try {
            return sService.setConfig(aid);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return 255;
        }
    }

    public byte[] getNfccDieid() {
        try {
            return sService.getNfccDieid();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return null;
        }
    }

    public boolean setSeRouting(int seID) {
        try {
            return sService.setSeRouting(seID);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    public int getSeRouting() {
        try {
            return sService.getSeRouting();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return 0;
        }
    }
}
