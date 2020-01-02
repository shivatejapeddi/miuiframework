package android.printservice;

import android.content.pm.ParceledListSlice;
import android.os.CancellationSignal;
import android.os.RemoteException;
import android.print.PrinterId;
import android.print.PrinterInfo;
import android.util.ArrayMap;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PrinterDiscoverySession {
    private static final String LOG_TAG = "PrinterDiscoverySession";
    private static int sIdCounter = 0;
    private final int mId;
    private boolean mIsDestroyed;
    private boolean mIsDiscoveryStarted;
    private ArrayMap<PrinterId, PrinterInfo> mLastSentPrinters;
    private IPrintServiceClient mObserver;
    private final ArrayMap<PrinterId, PrinterInfo> mPrinters = new ArrayMap();
    private final List<PrinterId> mTrackedPrinters = new ArrayList();

    public abstract void onDestroy();

    public abstract void onStartPrinterDiscovery(List<PrinterId> list);

    public abstract void onStartPrinterStateTracking(PrinterId printerId);

    public abstract void onStopPrinterDiscovery();

    public abstract void onStopPrinterStateTracking(PrinterId printerId);

    public abstract void onValidatePrinters(List<PrinterId> list);

    public PrinterDiscoverySession() {
        int i = sIdCounter;
        sIdCounter = i + 1;
        this.mId = i;
    }

    /* Access modifiers changed, original: 0000 */
    public void setObserver(IPrintServiceClient observer) {
        this.mObserver = observer;
        if (!this.mPrinters.isEmpty()) {
            try {
                this.mObserver.onPrintersAdded(new ParceledListSlice(getPrinters()));
            } catch (RemoteException re) {
                Log.e(LOG_TAG, "Error sending added printers", re);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int getId() {
        return this.mId;
    }

    public final List<PrinterInfo> getPrinters() {
        PrintService.throwIfNotCalledOnMainThread();
        if (this.mIsDestroyed) {
            return Collections.emptyList();
        }
        return new ArrayList(this.mPrinters.values());
    }

    public final void addPrinters(List<PrinterInfo> printers) {
        PrintService.throwIfNotCalledOnMainThread();
        boolean z = this.mIsDestroyed;
        String str = LOG_TAG;
        if (z) {
            Log.w(str, "Not adding printers - session destroyed.");
            return;
        }
        if (this.mIsDiscoveryStarted) {
            List<PrinterInfo> addedPrinters = null;
            int addedPrinterCount = printers.size();
            for (int i = 0; i < addedPrinterCount; i++) {
                PrinterInfo addedPrinter = (PrinterInfo) printers.get(i);
                PrinterInfo oldPrinter = (PrinterInfo) this.mPrinters.put(addedPrinter.getId(), addedPrinter);
                if (oldPrinter == null || !oldPrinter.equals(addedPrinter)) {
                    if (addedPrinters == null) {
                        addedPrinters = new ArrayList();
                    }
                    addedPrinters.add(addedPrinter);
                }
            }
            if (addedPrinters != null) {
                try {
                    this.mObserver.onPrintersAdded(new ParceledListSlice(addedPrinters));
                } catch (RemoteException re) {
                    Log.e(str, "Error sending added printers", re);
                }
            }
        } else {
            if (this.mLastSentPrinters == null) {
                this.mLastSentPrinters = new ArrayMap(this.mPrinters);
            }
            int addedPrinterCount2 = printers.size();
            for (int i2 = 0; i2 < addedPrinterCount2; i2++) {
                PrinterInfo addedPrinter2 = (PrinterInfo) printers.get(i2);
                if (this.mPrinters.get(addedPrinter2.getId()) == null) {
                    this.mPrinters.put(addedPrinter2.getId(), addedPrinter2);
                }
            }
        }
    }

    public final void removePrinters(List<PrinterId> printerIds) {
        PrintService.throwIfNotCalledOnMainThread();
        boolean z = this.mIsDestroyed;
        String str = LOG_TAG;
        if (z) {
            Log.w(str, "Not removing printers - session destroyed.");
            return;
        }
        if (this.mIsDiscoveryStarted) {
            List<PrinterId> removedPrinterIds = new ArrayList();
            int removedPrinterIdCount = printerIds.size();
            for (int i = 0; i < removedPrinterIdCount; i++) {
                PrinterId removedPrinterId = (PrinterId) printerIds.get(i);
                if (this.mPrinters.remove(removedPrinterId) != null) {
                    removedPrinterIds.add(removedPrinterId);
                }
            }
            if (!removedPrinterIds.isEmpty()) {
                try {
                    this.mObserver.onPrintersRemoved(new ParceledListSlice(removedPrinterIds));
                } catch (RemoteException re) {
                    Log.e(str, "Error sending removed printers", re);
                }
            }
        } else {
            if (this.mLastSentPrinters == null) {
                this.mLastSentPrinters = new ArrayMap(this.mPrinters);
            }
            int removedPrinterIdCount2 = printerIds.size();
            for (int i2 = 0; i2 < removedPrinterIdCount2; i2++) {
                this.mPrinters.remove((PrinterId) printerIds.get(i2));
            }
        }
    }

    private void sendOutOfDiscoveryPeriodPrinterChanges() {
        ArrayMap arrayMap = this.mLastSentPrinters;
        if (arrayMap == null || arrayMap.isEmpty()) {
            this.mLastSentPrinters = null;
            return;
        }
        List<PrinterInfo> addedPrinters = null;
        for (PrinterInfo printer : this.mPrinters.values()) {
            PrinterInfo sentPrinter = (PrinterInfo) this.mLastSentPrinters.get(printer.getId());
            if (sentPrinter == null || !sentPrinter.equals(printer)) {
                if (addedPrinters == null) {
                    addedPrinters = new ArrayList();
                }
                addedPrinters.add(printer);
            }
        }
        String str = LOG_TAG;
        if (addedPrinters != null) {
            try {
                this.mObserver.onPrintersAdded(new ParceledListSlice(addedPrinters));
            } catch (RemoteException re) {
                Log.e(str, "Error sending added printers", re);
            }
        }
        List<PrinterId> removedPrinterIds = null;
        for (PrinterInfo sentPrinter2 : this.mLastSentPrinters.values()) {
            if (!this.mPrinters.containsKey(sentPrinter2.getId())) {
                if (removedPrinterIds == null) {
                    removedPrinterIds = new ArrayList();
                }
                removedPrinterIds.add(sentPrinter2.getId());
            }
        }
        if (removedPrinterIds != null) {
            try {
                this.mObserver.onPrintersRemoved(new ParceledListSlice(removedPrinterIds));
            } catch (RemoteException re2) {
                Log.e(str, "Error sending removed printers", re2);
            }
        }
        this.mLastSentPrinters = null;
    }

    public void onRequestCustomPrinterIcon(PrinterId printerId, CancellationSignal cancellationSignal, CustomPrinterIconCallback callback) {
    }

    public final List<PrinterId> getTrackedPrinters() {
        PrintService.throwIfNotCalledOnMainThread();
        if (this.mIsDestroyed) {
            return Collections.emptyList();
        }
        return new ArrayList(this.mTrackedPrinters);
    }

    public final boolean isDestroyed() {
        PrintService.throwIfNotCalledOnMainThread();
        return this.mIsDestroyed;
    }

    public final boolean isPrinterDiscoveryStarted() {
        PrintService.throwIfNotCalledOnMainThread();
        return this.mIsDiscoveryStarted;
    }

    /* Access modifiers changed, original: 0000 */
    public void startPrinterDiscovery(List<PrinterId> priorityList) {
        if (!this.mIsDestroyed) {
            this.mIsDiscoveryStarted = true;
            sendOutOfDiscoveryPeriodPrinterChanges();
            if (priorityList == null) {
                priorityList = Collections.emptyList();
            }
            onStartPrinterDiscovery(priorityList);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void stopPrinterDiscovery() {
        if (!this.mIsDestroyed) {
            this.mIsDiscoveryStarted = false;
            onStopPrinterDiscovery();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void validatePrinters(List<PrinterId> printerIds) {
        if (!this.mIsDestroyed && this.mObserver != null) {
            onValidatePrinters(printerIds);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void startPrinterStateTracking(PrinterId printerId) {
        if (!this.mIsDestroyed && this.mObserver != null && !this.mTrackedPrinters.contains(printerId)) {
            this.mTrackedPrinters.add(printerId);
            onStartPrinterStateTracking(printerId);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void requestCustomPrinterIcon(PrinterId printerId) {
        if (!this.mIsDestroyed) {
            IPrintServiceClient iPrintServiceClient = this.mObserver;
            if (iPrintServiceClient != null) {
                onRequestCustomPrinterIcon(printerId, new CancellationSignal(), new CustomPrinterIconCallback(printerId, iPrintServiceClient));
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void stopPrinterStateTracking(PrinterId printerId) {
        if (!this.mIsDestroyed && this.mObserver != null && this.mTrackedPrinters.remove(printerId)) {
            onStopPrinterStateTracking(printerId);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void destroy() {
        if (!this.mIsDestroyed) {
            this.mIsDestroyed = true;
            this.mIsDiscoveryStarted = false;
            this.mPrinters.clear();
            this.mLastSentPrinters = null;
            this.mObserver = null;
            onDestroy();
        }
    }
}
