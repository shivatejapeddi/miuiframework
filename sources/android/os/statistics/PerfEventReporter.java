package android.os.statistics;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.os.statistics.PerfEventFilter.TimeslicedCompletedEventList;
import android.system.OsConstants;
import com.android.internal.app.IPerfShielder;
import com.miui.daemon.performance.PerfShielderManager;
import java.io.IOException;
import miui.util.ReflectionUtils;

public class PerfEventReporter {
    private static final boolean DEBUGGING = false;
    private static final int MAX_RETRY_WRITE_COUNT = 6;

    private static class ProcPerfEventFilterThread extends Thread {
        private static final int MACRO_EVENT_TYPE_INDEX_OFFSET = -65520;
        private static final ProcPerfEventFilterThread sInstance = new ProcPerfEventFilterThread();
        private long mEarliestBeginUptimeMillisOfExecutingRootEvents;
        private final Object mLock = new Object();
        private boolean mNeedFilter = false;
        private final PerfEvent[] mPerfEventObjectCache = new PerfEvent[23];
        private ParcelFileDescriptor mPerfEventSocketFd;
        private int mPerfEventSocketFdNo = -1;
        private FilteringPerfEventList mReceivedEffectiveEvents = new FilteringPerfEventList();
        private FilteringPerfEventList mReceivedSuspectedEvents = new FilteringPerfEventList();
        private Parcel mSendingParcel;
        private long mSendingParcelNativePtr;
        private int myTid;
        private final PerfEventFilter perfEventFilter = new PerfEventFilter(true, PerfEventFilter.APP_MAX_FILTER_INTERVAL_MILLIS);
        private final TimeslicedCompletedEventList tempCompletedEvents = new TimeslicedCompletedEventList(PerfSupervisionSettings.sPerfSupervisionSoftThreshold * 2, PerfEventFilter.APP_MAX_FILTER_INTERVAL_MILLIS * 5, OsUtils.getCoarseUptimeMillisFast(), 0, 65536);

        public static ProcPerfEventFilterThread getInstance() {
            return sInstance;
        }

        private ProcPerfEventFilterThread() {
            super("Binder:filter-perf-event");
        }

        public void send(FilteringPerfEventList effectiveEvents, FilteringPerfEventList suspectedEvents, long earliestBeginUptimeMillisOfExecutingRootEvents) {
            synchronized (this.mLock) {
                if (this.mReceivedEffectiveEvents.size + this.mReceivedSuspectedEvents.size <= 5000) {
                    if (effectiveEvents.size > 0) {
                        this.mReceivedEffectiveEvents.moveAllFrom(effectiveEvents);
                    }
                    if (suspectedEvents.size > 0) {
                        this.mReceivedSuspectedEvents.moveAllFrom(suspectedEvents);
                    }
                    this.mEarliestBeginUptimeMillisOfExecutingRootEvents = earliestBeginUptimeMillisOfExecutingRootEvents;
                    this.mNeedFilter = true;
                    this.mLock.notifyAll();
                } else {
                    FilteringPerfEventCache.recycleAllUnchecked(effectiveEvents);
                    FilteringPerfEventCache.recycleAllUnchecked(suspectedEvents);
                }
            }
        }

        public void run() {
            PerfSuperviser.setThreadPerfSupervisionOn(false);
            this.myTid = Process.myTid();
            this.mSendingParcel = Parcel.obtain();
            this.mSendingParcel.setDataCapacity(4096);
            try {
                this.mSendingParcelNativePtr = ((Long) ReflectionUtils.getObjectField(this.mSendingParcel, "mNativePtr", Long.class)).longValue();
            } catch (Exception e) {
                this.mSendingParcelNativePtr = 0;
            }
            OsUtils.setThreadPriorityUnconditonally(this.myTid, -10);
            while (true) {
                loopOnce();
            }
        }

        private void loopOnce() {
            long earliestBeginUptimeMillisOfExecutingRootEvents = 0;
            FilteringPerfEventList effectiveEvents = null;
            FilteringPerfEventList suspectedEvents = null;
            synchronized (this.mLock) {
                if (!this.mNeedFilter) {
                    try {
                        if (this.perfEventFilter.hasPendingPerfEvents()) {
                            this.mLock.wait((long) PerfEventFilter.APP_MAX_FILTER_INTERVAL_MILLIS);
                        } else {
                            this.mLock.wait();
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
            boolean filterable = this.mNeedFilter || !ProcPerfEventReaderThread.getInstance().hasPendingPerfEvents();
            if (filterable) {
                synchronized (this.mLock) {
                    earliestBeginUptimeMillisOfExecutingRootEvents = this.mEarliestBeginUptimeMillisOfExecutingRootEvents;
                    effectiveEvents = this.mReceivedEffectiveEvents;
                    suspectedEvents = this.mReceivedSuspectedEvents;
                    this.mReceivedEffectiveEvents = new FilteringPerfEventList();
                    this.mReceivedSuspectedEvents = new FilteringPerfEventList();
                    this.mNeedFilter = false;
                }
            }
            if (filterable) {
                OsUtils.setThreadPriorityUnconditonally(this.myTid, -4);
                this.perfEventFilter.send(effectiveEvents, suspectedEvents);
                this.perfEventFilter.filter(this.tempCompletedEvents, earliestBeginUptimeMillisOfExecutingRootEvents);
                if (this.tempCompletedEvents.perfEventCount > 0) {
                    obtainPerfEventSocketFd();
                    NativeBackTrace.reset();
                    sendPerfEvents(this.tempCompletedEvents);
                }
                OsUtils.setThreadPriorityUnconditonally(this.myTid, -10);
                FilteringPerfEventCache.compact();
            }
        }

        private void obtainPerfEventSocketFd() {
            if (this.mPerfEventSocketFd == null) {
                IPerfShielder perfShielder = PerfShielderManager.getService();
                if (perfShielder != null) {
                    try {
                        this.mPerfEventSocketFd = perfShielder.getPerfEventSocketFd();
                    } catch (RemoteException e) {
                    }
                    ParcelFileDescriptor parcelFileDescriptor = this.mPerfEventSocketFd;
                    if (parcelFileDescriptor == null || parcelFileDescriptor.getFileDescriptor() == null || !this.mPerfEventSocketFd.getFileDescriptor().valid()) {
                        this.mPerfEventSocketFd = null;
                        this.mPerfEventSocketFdNo = -1;
                    } else {
                        this.mPerfEventSocketFdNo = this.mPerfEventSocketFd.getFileDescriptor().getInt$();
                    }
                }
            }
        }

        private void sendPerfEvents(TimeslicedCompletedEventList completedEvents) {
            FilteringPerfEventList slice = completedEvents.outDatedPerfEvents;
            if (slice.size > 0) {
                sendPerfEvents(completedEvents, slice);
            }
            int timesliceCount = completedEvents.timesliceCount;
            int firstSliceIndex = completedEvents.firstSliceIndex;
            FilteringPerfEventList[] timeslicedPerfEvents = completedEvents.timeslicedPerfEvents;
            for (int i = 0; i < timesliceCount; i++) {
                slice = timeslicedPerfEvents[(firstSliceIndex + i) % timesliceCount];
                if (slice.size > 0) {
                    sendPerfEvents(completedEvents, slice);
                }
            }
        }

        private void sendPerfEvents(TimeslicedCompletedEventList completedEvents, FilteringPerfEventList slice) {
            FilteringPerfEventListNode head = slice;
            FilteringPerfEventListNode pos = head.next;
            while (pos != head) {
                FilteringPerfEvent item = pos.value;
                pos = pos.next;
                completedEvents.remove(slice, item);
                if (!(item.eventType == 11 || item.eventType == 12)) {
                    sendPerfEvent(item);
                }
                FilteringPerfEventCache.recycleUnchecked(item);
            }
        }

        private void sendPerfEvent(FilteringPerfEvent item) {
            if (item.event == null && (item.eventFlags & 16) == 0) {
                this.mSendingParcel.setDataPosition(0);
                this.mSendingParcel.setDataSize(0);
                item.writeToParcel(this.mSendingParcel, this.mSendingParcelNativePtr);
                sendPerfEventParcel(this.mSendingParcel, this.mSendingParcelNativePtr);
                return;
            }
            PerfEvent event;
            if (item.event == null) {
                event = obtainPerfEvent(item.eventType);
                item.resolveTo(event, this.mSendingParcel, this.mSendingParcelNativePtr);
            } else {
                event = item.event;
                event.eventFlags = item.eventFlags;
            }
            event.resolveLazyInfo();
            event.eventFlags |= 32;
            if (event.isMeaningful()) {
                this.mSendingParcel.setDataPosition(0);
                this.mSendingParcel.setDataSize(0);
                event.writeToParcel(this.mSendingParcel, 0);
                sendPerfEventParcel(this.mSendingParcel, this.mSendingParcelNativePtr);
            }
        }

        private PerfEvent obtainPerfEvent(int eventType) {
            int eventTypeIndex = (eventType < 65536 ? eventType : MACRO_EVENT_TYPE_INDEX_OFFSET + eventType) + 0;
            PerfEvent event = this.mPerfEventObjectCache[eventTypeIndex];
            if (event != null) {
                return event;
            }
            event = PerfEventFactory.createPerfEvent(eventType);
            this.mPerfEventObjectCache[eventTypeIndex] = event;
            return event;
        }

        private boolean sendPerfEventParcel(Parcel parcel, long parcelNativePtr) {
            if (this.mPerfEventSocketFd != null) {
                int i = 0;
                while (i < 6) {
                    int result = PerfEventSocket.sendPerfEvent(this.mPerfEventSocketFdNo, parcel, parcelNativePtr, 4096);
                    if (result >= 0) {
                        return true;
                    }
                    int errno = -result;
                    if (errno == OsConstants.EAGAIN || errno == OsConstants.EINTR) {
                        try {
                            Thread.sleep((long) ((i / 2) + 1));
                        } catch (InterruptedException e) {
                        }
                        i++;
                    } else if (!(errno == OsConstants.EMSGSIZE || errno == OsConstants.ENOMEM)) {
                        try {
                            this.mPerfEventSocketFd.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        this.mPerfEventSocketFd = null;
                        this.mPerfEventSocketFdNo = -1;
                    }
                }
            }
            return false;
        }
    }

    private static class ProcPerfEventReaderThread extends Thread {
        private static final ProcPerfEventReaderThread sInstance = new ProcPerfEventReaderThread();
        private long latestSendUptimeMillis;
        private FilteringPerfEventList mEffectivePerfEvents = new FilteringPerfEventList();
        private FilteringPerfEvent[] mPerfEventFetchingBuffer;
        private FilteringPerfEventList mSuspectedPerfEvents = new FilteringPerfEventList();
        private int maxEventCountOfOneBatch;
        private int myTid;

        public static ProcPerfEventReaderThread getInstance() {
            return sInstance;
        }

        private ProcPerfEventReaderThread() {
            super("Binder:read-perf-event");
        }

        public boolean hasPendingPerfEvents() {
            FilteringPerfEventList effectiveEvents = this.mEffectivePerfEvents;
            FilteringPerfEventList suspectedEvents = this.mSuspectedPerfEvents;
            return (effectiveEvents != null && effectiveEvents.size > 0) || (suspectedEvents != null && suspectedEvents.size > 0);
        }

        public void run() {
            PerfSuperviser.setThreadPerfSupervisionOn(false);
            this.myTid = Process.myTid();
            this.latestSendUptimeMillis = OsUtils.getCoarseUptimeMillisFast();
            this.maxEventCountOfOneBatch = PerfSupervisionSettings.isSystemServer() ? 600 : 200;
            this.mPerfEventFetchingBuffer = new FilteringPerfEvent[this.maxEventCountOfOneBatch];
            OsUtils.setThreadPriorityUnconditonally(this.myTid, -10);
            while (true) {
                loopOnce();
            }
        }

        private void loopOnce() {
            long earliestBeginUptimeMillisOfExecutingRootEvents;
            long now;
            FilteringPerfEventList effectiveEvents;
            FilteringPerfEventList suspectedEvents;
            while (true) {
                int timeoutMillis;
                if (this.mEffectivePerfEvents.size + this.mSuspectedPerfEvents.size > 0) {
                    timeoutMillis = PerfEventFilter.APP_MAX_FILTER_INTERVAL_MILLIS >> 2;
                } else {
                    timeoutMillis = -1;
                }
                PerfEventReporter.waitForPerfEventArrived(timeoutMillis);
                earliestBeginUptimeMillisOfExecutingRootEvents = PerfEventReporter.getEarliestBeginUptimeMillisOfExecutingRootEvents();
                fetchPerfEventsFromBuffer(this.mEffectivePerfEvents, this.mSuspectedPerfEvents);
                now = OsUtils.getCoarseUptimeMillisFast();
                if (now - this.latestSendUptimeMillis >= ((long) PerfEventFilter.APP_MAX_FILTER_INTERVAL_MILLIS) || now - this.latestSendUptimeMillis < 0 || this.mEffectivePerfEvents.size + this.mSuspectedPerfEvents.size >= this.maxEventCountOfOneBatch) {
                    this.latestSendUptimeMillis = now;
                    effectiveEvents = this.mEffectivePerfEvents;
                    suspectedEvents = this.mSuspectedPerfEvents;
                    this.mEffectivePerfEvents = new FilteringPerfEventList();
                    this.mSuspectedPerfEvents = new FilteringPerfEventList();
                    ProcPerfEventFilterThread.getInstance().send(effectiveEvents, suspectedEvents, earliestBeginUptimeMillisOfExecutingRootEvents);
                }
            }
            this.latestSendUptimeMillis = now;
            effectiveEvents = this.mEffectivePerfEvents;
            suspectedEvents = this.mSuspectedPerfEvents;
            this.mEffectivePerfEvents = new FilteringPerfEventList();
            this.mSuspectedPerfEvents = new FilteringPerfEventList();
            ProcPerfEventFilterThread.getInstance().send(effectiveEvents, suspectedEvents, earliestBeginUptimeMillisOfExecutingRootEvents);
        }

        private void fetchPerfEventsFromBuffer(FilteringPerfEventList effectiveEvents, FilteringPerfEventList suspectedEvents) {
            int count = PerfEventReporter.fetchProcUserspacePerfEvents(this.mPerfEventFetchingBuffer);
            if (count != 0) {
                dividePerfEventsFromBuffer(count, effectiveEvents, suspectedEvents);
            }
            count = PerfEventReporter.fetchProcKernelPerfEvents(this.mPerfEventFetchingBuffer);
            if (count != 0) {
                dividePerfEventsFromBuffer(count, effectiveEvents, suspectedEvents);
            }
        }

        private void dividePerfEventsFromBuffer(int count, FilteringPerfEventList effectiveEvents, FilteringPerfEventList suspectedEvents) {
            for (int i = 0; i < count; i++) {
                FilteringPerfEvent[] filteringPerfEventArr = this.mPerfEventFetchingBuffer;
                FilteringPerfEvent filteringPerfEvent = filteringPerfEventArr[i];
                filteringPerfEventArr[i] = null;
                if (filteringPerfEvent.beginUptimeMillis <= 0 || filteringPerfEvent.durationMillis < 0 || filteringPerfEvent.durationMillis >= 65536) {
                    FilteringPerfEventCache.recycle(filteringPerfEvent);
                } else {
                    boolean isEffectiveEvent;
                    if (filteringPerfEvent.eventType >= 65536) {
                        isEffectiveEvent = true;
                        filteringPerfEvent.eventFlags |= 268435456;
                    } else {
                        isEffectiveEvent = (filteringPerfEvent.eventFlags & 262145) != 0;
                        if ((filteringPerfEvent.eventFlags & 768) == 256) {
                            filteringPerfEvent.eventFlags |= 268435456;
                        }
                    }
                    if (isEffectiveEvent) {
                        effectiveEvents.addLast(filteringPerfEvent);
                    } else {
                        suspectedEvents.addLast(filteringPerfEvent);
                    }
                }
            }
        }
    }

    private static native int fetchProcKernelPerfEvents(FilteringPerfEvent[] filteringPerfEventArr);

    private static native int fetchProcUserspacePerfEvents(FilteringPerfEvent[] filteringPerfEventArr);

    public static native long getEarliestBeginUptimeMillisOfExecutingRootEvents();

    private static native void nativeReport(int i, PerfEvent perfEvent);

    private static native void waitForPerfEventArrived(int i);

    static void start() {
        if (PerfSupervisionSettings.isPerfEventReportable()) {
            FilteringPerfEventCache.setCapacity(PerfSupervisionSettings.isSystemServer() ? 1800 : 600);
            ProcPerfEventFilterThread.getInstance().start();
            ProcPerfEventReaderThread.getInstance().start();
        }
    }

    public static void report(PerfEvent event) {
        if (PerfSupervisionSettings.isPerfEventReportable()) {
            nativeReport(event.eventType, event);
        }
    }
}
