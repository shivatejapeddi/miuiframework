package android.print;

import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.print.IPrinterDiscoveryObserver.Stub;
import android.util.ArrayMap;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public final class PrinterDiscoverySession {
    private static final String LOG_TAG = "PrinterDiscoverySession";
    private static final int MSG_PRINTERS_ADDED = 1;
    private static final int MSG_PRINTERS_REMOVED = 2;
    private final Handler mHandler;
    private boolean mIsPrinterDiscoveryStarted;
    private OnPrintersChangeListener mListener;
    private IPrinterDiscoveryObserver mObserver;
    private final IPrintManager mPrintManager;
    private final LinkedHashMap<PrinterId, PrinterInfo> mPrinters = new LinkedHashMap();
    private final int mUserId;

    public interface OnPrintersChangeListener {
        void onPrintersChanged();
    }

    public static final class PrinterDiscoveryObserver extends Stub {
        private final WeakReference<PrinterDiscoverySession> mWeakSession;

        public PrinterDiscoveryObserver(PrinterDiscoverySession session) {
            this.mWeakSession = new WeakReference(session);
        }

        public void onPrintersAdded(ParceledListSlice printers) {
            PrinterDiscoverySession session = (PrinterDiscoverySession) this.mWeakSession.get();
            if (session != null) {
                session.mHandler.obtainMessage(1, printers.getList()).sendToTarget();
            }
        }

        public void onPrintersRemoved(ParceledListSlice printerIds) {
            PrinterDiscoverySession session = (PrinterDiscoverySession) this.mWeakSession.get();
            if (session != null) {
                session.mHandler.obtainMessage(2, printerIds.getList()).sendToTarget();
            }
        }
    }

    private final class SessionHandler extends Handler {
        public SessionHandler(Looper looper) {
            super(looper, null, false);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                PrinterDiscoverySession.this.handlePrintersAdded(message.obj);
            } else if (i == 2) {
                PrinterDiscoverySession.this.handlePrintersRemoved(message.obj);
            }
        }
    }

    PrinterDiscoverySession(IPrintManager printManager, Context context, int userId) {
        this.mPrintManager = printManager;
        this.mUserId = userId;
        this.mHandler = new SessionHandler(context.getMainLooper());
        this.mObserver = new PrinterDiscoveryObserver(this);
        try {
            this.mPrintManager.createPrinterDiscoverySession(this.mObserver, this.mUserId);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error creating printer discovery session", re);
        }
    }

    public final void startPrinterDiscovery(List<PrinterId> priorityList) {
        boolean isDestroyed = isDestroyed();
        String str = LOG_TAG;
        if (isDestroyed) {
            Log.w(str, "Ignoring start printers discovery - session destroyed");
            return;
        }
        if (!this.mIsPrinterDiscoveryStarted) {
            this.mIsPrinterDiscoveryStarted = true;
            try {
                this.mPrintManager.startPrinterDiscovery(this.mObserver, priorityList, this.mUserId);
            } catch (RemoteException re) {
                Log.e(str, "Error starting printer discovery", re);
            }
        }
    }

    public final void stopPrinterDiscovery() {
        boolean isDestroyed = isDestroyed();
        String str = LOG_TAG;
        if (isDestroyed) {
            Log.w(str, "Ignoring stop printers discovery - session destroyed");
            return;
        }
        if (this.mIsPrinterDiscoveryStarted) {
            this.mIsPrinterDiscoveryStarted = false;
            try {
                this.mPrintManager.stopPrinterDiscovery(this.mObserver, this.mUserId);
            } catch (RemoteException re) {
                Log.e(str, "Error stopping printer discovery", re);
            }
        }
    }

    public final void startPrinterStateTracking(PrinterId printerId) {
        boolean isDestroyed = isDestroyed();
        String str = LOG_TAG;
        if (isDestroyed) {
            Log.w(str, "Ignoring start printer state tracking - session destroyed");
            return;
        }
        try {
            this.mPrintManager.startPrinterStateTracking(printerId, this.mUserId);
        } catch (RemoteException re) {
            Log.e(str, "Error starting printer state tracking", re);
        }
    }

    public final void stopPrinterStateTracking(PrinterId printerId) {
        boolean isDestroyed = isDestroyed();
        String str = LOG_TAG;
        if (isDestroyed) {
            Log.w(str, "Ignoring stop printer state tracking - session destroyed");
            return;
        }
        try {
            this.mPrintManager.stopPrinterStateTracking(printerId, this.mUserId);
        } catch (RemoteException re) {
            Log.e(str, "Error stopping printer state tracking", re);
        }
    }

    public final void validatePrinters(List<PrinterId> printerIds) {
        boolean isDestroyed = isDestroyed();
        String str = LOG_TAG;
        if (isDestroyed) {
            Log.w(str, "Ignoring validate printers - session destroyed");
            return;
        }
        try {
            this.mPrintManager.validatePrinters(printerIds, this.mUserId);
        } catch (RemoteException re) {
            Log.e(str, "Error validating printers", re);
        }
    }

    public final void destroy() {
        if (isDestroyed()) {
            Log.w(LOG_TAG, "Ignoring destroy - session destroyed");
        }
        destroyNoCheck();
    }

    public final List<PrinterInfo> getPrinters() {
        if (!isDestroyed()) {
            return new ArrayList(this.mPrinters.values());
        }
        Log.w(LOG_TAG, "Ignoring get printers - session destroyed");
        return Collections.emptyList();
    }

    public final boolean isDestroyed() {
        throwIfNotCalledOnMainThread();
        return isDestroyedNoCheck();
    }

    public final boolean isPrinterDiscoveryStarted() {
        throwIfNotCalledOnMainThread();
        return this.mIsPrinterDiscoveryStarted;
    }

    public final void setOnPrintersChangeListener(OnPrintersChangeListener listener) {
        throwIfNotCalledOnMainThread();
        this.mListener = listener;
    }

    /* Access modifiers changed, original: protected|final */
    public final void finalize() throws Throwable {
        if (!isDestroyedNoCheck()) {
            Log.e(LOG_TAG, "Destroying leaked printer discovery session");
            destroyNoCheck();
        }
        super.finalize();
    }

    private boolean isDestroyedNoCheck() {
        return this.mObserver == null;
    }

    private void destroyNoCheck() {
        stopPrinterDiscovery();
        try {
            this.mPrintManager.destroyPrinterDiscoverySession(this.mObserver, this.mUserId);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error destroying printer discovery session", re);
        } catch (Throwable th) {
            this.mObserver = null;
            this.mPrinters.clear();
        }
        this.mObserver = null;
        this.mPrinters.clear();
    }

    private void handlePrintersAdded(List<PrinterInfo> addedPrinters) {
        if (!isDestroyed()) {
            int i;
            if (this.mPrinters.isEmpty()) {
                int printerCount = addedPrinters.size();
                for (i = 0; i < printerCount; i++) {
                    PrinterInfo printer = (PrinterInfo) addedPrinters.get(i);
                    this.mPrinters.put(printer.getId(), printer);
                }
                notifyOnPrintersChanged();
                return;
            }
            ArrayMap<PrinterId, PrinterInfo> addedPrintersMap = new ArrayMap();
            i = addedPrinters.size();
            for (int i2 = 0; i2 < i; i2++) {
                PrinterInfo printer2 = (PrinterInfo) addedPrinters.get(i2);
                addedPrintersMap.put(printer2.getId(), printer2);
            }
            for (PrinterId oldPrinterId : this.mPrinters.keySet()) {
                PrinterInfo updatedPrinter = (PrinterInfo) addedPrintersMap.remove(oldPrinterId);
                if (updatedPrinter != null) {
                    this.mPrinters.put(oldPrinterId, updatedPrinter);
                }
            }
            this.mPrinters.putAll(addedPrintersMap);
            notifyOnPrintersChanged();
        }
    }

    private void handlePrintersRemoved(List<PrinterId> printerIds) {
        if (!isDestroyed()) {
            boolean printersChanged = false;
            int removedPrinterIdCount = printerIds.size();
            for (int i = 0; i < removedPrinterIdCount; i++) {
                if (this.mPrinters.remove((PrinterId) printerIds.get(i)) != null) {
                    printersChanged = true;
                }
            }
            if (printersChanged) {
                notifyOnPrintersChanged();
            }
        }
    }

    private void notifyOnPrintersChanged() {
        OnPrintersChangeListener onPrintersChangeListener = this.mListener;
        if (onPrintersChangeListener != null) {
            onPrintersChangeListener.onPrintersChanged();
        }
    }

    private static void throwIfNotCalledOnMainThread() {
        if (!Looper.getMainLooper().isCurrentThread()) {
            throw new IllegalAccessError("must be called from the main thread");
        }
    }
}
