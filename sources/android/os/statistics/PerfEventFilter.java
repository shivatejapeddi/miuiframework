package android.os.statistics;

import java.util.Comparator;

public final class PerfEventFilter {
    public static final int APP_MAX_FILTER_INTERVAL_MILLIS = Math.max(2000, (PerfSupervisionSettings.sMinPerfEventDurationMillis << 5) + ((PerfSupervisionSettings.sMinPerfEventDurationMillis << 5) / 4));
    public static final int APP_ONE_BATCH_MAX_EVENT_COUNT = 200;
    public static final int DAEMON_MAX_FILTER_INTERVAL_MILLIS = Math.max(APP_MAX_FILTER_INTERVAL_MILLIS, 2000);
    public static final int DAEMON_ONE_BATCH_MAX_EVENT_COUNT = 2000;
    public static final int MAX_TIME_SPAN_MILLIS = 65536;
    public static final int SYSTEM_SERVER_ONE_BATCH_MAX_EVENT_COUNT = 600;
    private LeveledCheckingEventList effectivePerfEvents;
    private LeveledCheckingEventList effectivePerfEventsWaitingPeer;
    private final boolean isAppSideFilter;
    private long latestBatchBeginUptimeMillis;
    private long latestBatchEndUptimeMillis;
    private final int maxFilterIntervalMillis;
    private final int minWaitTimeMillis;
    private final LeveledCheckingEventList suspectedPerfEvents;
    private final long[] tempBatchTimestampMillis = new long[2];

    private static final class LeveledCheckingEventList {
        public final int longLevelCount;
        public final int longLevelDeltaMillis;
        public final int longLevelDeltaMillisPower;
        public final int longLevelFloorMillis = Math.max(1024, this.shortLevelDeltaMillis);
        public final TimeslicedCheckingEventList[] longPerfEventsByLevel;
        public int perfEventCount;
        public final int shortLevelCount = (this.longLevelFloorMillis / this.shortLevelDeltaMillis);
        public final int shortLevelDeltaMillis = (1 << this.shortLevelDeltaMillisPower);
        public final int shortLevelDeltaMillisPower;
        public final TimeslicedCheckingEventList[] shortPerfEventsByLevel;

        public LeveledCheckingEventList(boolean _isEffectiveEvents, int _shortLevelDeltaMillis, int _longLevelDeltaMillis, int _minWaitTimeMillis, long _earliestUnexpiredUptimeMillis) {
            int exclusiveMaxPerfEventDurationMsInLevel;
            int timespanInMillis;
            int i;
            this.shortLevelDeltaMillisPower = PerfEventFilter.getCeilingOfPowerByTwo(_shortLevelDeltaMillis);
            this.longLevelDeltaMillisPower = PerfEventFilter.getCeilingOfPowerByTwo(_longLevelDeltaMillis);
            this.longLevelDeltaMillis = 1 << this.longLevelDeltaMillisPower;
            this.longLevelCount = 65536 / this.longLevelDeltaMillis;
            this.shortPerfEventsByLevel = new TimeslicedCheckingEventList[this.shortLevelCount];
            int i2 = 0;
            while (i2 < this.shortLevelCount) {
                int inclusiveMinPerfEventDurationMsInLevel = i2 == 0 ? Math.min(PerfSupervisionSettings.sMinPerfEventDurationMillis, this.shortLevelDeltaMillis) : this.shortLevelDeltaMillis * i2;
                exclusiveMaxPerfEventDurationMsInLevel = (i2 + 1) * this.shortLevelDeltaMillis;
                timespanInMillis = _minWaitTimeMillis + exclusiveMaxPerfEventDurationMsInLevel;
                if (_isEffectiveEvents) {
                    i = exclusiveMaxPerfEventDurationMsInLevel << 2;
                } else {
                    i = exclusiveMaxPerfEventDurationMsInLevel << 5;
                }
                timespanInMillis += i;
                if (timespanInMillis > 65536) {
                    timespanInMillis = 65536;
                }
                this.shortPerfEventsByLevel[i2] = new TimeslicedCheckingEventList(exclusiveMaxPerfEventDurationMsInLevel, timespanInMillis, _earliestUnexpiredUptimeMillis, inclusiveMinPerfEventDurationMsInLevel, exclusiveMaxPerfEventDurationMsInLevel);
                i2++;
            }
            this.longPerfEventsByLevel = new TimeslicedCheckingEventList[this.longLevelCount];
            for (i2 = 0; i2 < this.longLevelCount; i2++) {
                int i3;
                exclusiveMaxPerfEventDurationMsInLevel = this.longLevelDeltaMillis;
                timespanInMillis = (i2 * exclusiveMaxPerfEventDurationMsInLevel) + this.longLevelFloorMillis;
                exclusiveMaxPerfEventDurationMsInLevel += timespanInMillis;
                if (exclusiveMaxPerfEventDurationMsInLevel > 65536) {
                    exclusiveMaxPerfEventDurationMsInLevel = 65536;
                }
                i = _minWaitTimeMillis + exclusiveMaxPerfEventDurationMsInLevel;
                if (_isEffectiveEvents) {
                    i3 = exclusiveMaxPerfEventDurationMsInLevel << 2;
                } else {
                    i3 = exclusiveMaxPerfEventDurationMsInLevel << 5;
                }
                i += i3;
                if (i > 65536) {
                    i = 65536;
                }
                this.longPerfEventsByLevel[i2] = new TimeslicedCheckingEventList(1 << PerfEventFilter.getCeilingOfPowerByTwo(exclusiveMaxPerfEventDurationMsInLevel), i, _earliestUnexpiredUptimeMillis, timespanInMillis, exclusiveMaxPerfEventDurationMsInLevel);
            }
        }

        public final void add(FilteringPerfEvent perfEvent) {
            int shortLevel;
            if (perfEvent.durationMillis < this.longLevelFloorMillis) {
                shortLevel = perfEvent.durationMillis >> this.shortLevelDeltaMillisPower;
                this.perfEventCount++;
                this.shortPerfEventsByLevel[shortLevel].add(perfEvent);
                return;
            }
            shortLevel = (perfEvent.durationMillis - this.longLevelFloorMillis) >> this.longLevelDeltaMillisPower;
            if (shortLevel < this.longLevelCount) {
                this.perfEventCount++;
                this.longPerfEventsByLevel[shortLevel].add(perfEvent);
            }
        }

        public final void remove(TimeslicedCheckingEventList levelPerfEvents, FilteringPerfEventList slice, FilteringPerfEvent perfEvent) {
            levelPerfEvents.remove(slice, perfEvent);
            this.perfEventCount--;
        }
    }

    public static abstract class TimeslicedFilteringPerfEventList {
        public final int exclusiveMaxPerfEventDurationMillis;
        public int firstSliceIndex;
        public long firstSliceUptimeMillis;
        public final int inclusiveMinPerfEventDurationMillis;
        public final FilteringPerfEventList outDatedPerfEvents = new FilteringPerfEventList();
        public int perfEventCount;
        public final Comparator<FilteringPerfEvent> secondaryComparator;
        public final int timesliceCount;
        public final int timesliceMillis;
        public final int timesliceMillisPower;
        public final FilteringPerfEventList[] timeslicedPerfEvents;

        public TimeslicedFilteringPerfEventList(int _suggestTimesliceInMillis, int _timespanInMillis, long _firstSliceUptimeMillis, int _inclusiveMinPerfEventDurationMillis, int _exclusiveMaxPerfEventDurationMillis, Comparator<FilteringPerfEvent> _secondaryComparator) {
            this.timesliceMillisPower = PerfEventFilter.getCeilingOfPowerByTwo(_suggestTimesliceInMillis);
            this.timesliceMillis = 1 << this.timesliceMillisPower;
            int i = this.timesliceMillis;
            int tempTimesliceCount = _timespanInMillis / i;
            if (i * tempTimesliceCount != _timespanInMillis) {
                tempTimesliceCount++;
            }
            this.timesliceCount = tempTimesliceCount;
            this.firstSliceIndex = 0;
            long j = 0;
            if (_firstSliceUptimeMillis >= 0) {
                j = _firstSliceUptimeMillis;
            }
            i = this.timesliceMillis;
            this.firstSliceUptimeMillis = (j / ((long) i)) * ((long) i);
            this.timeslicedPerfEvents = new FilteringPerfEventList[this.timesliceCount];
            for (i = 0; i < this.timesliceCount; i++) {
                this.timeslicedPerfEvents[i] = new FilteringPerfEventList();
            }
            this.inclusiveMinPerfEventDurationMillis = _inclusiveMinPerfEventDurationMillis;
            this.exclusiveMaxPerfEventDurationMillis = _exclusiveMaxPerfEventDurationMillis;
            this.secondaryComparator = _secondaryComparator;
        }

        public final void add(FilteringPerfEvent perfEvent) {
            FilteringPerfEventListNode targetSlice;
            long sortingUptimeMillis = perfEvent.sortingUptimeMillis;
            int millisAfterFirstSlice = (int) (sortingUptimeMillis - this.firstSliceUptimeMillis);
            if (millisAfterFirstSlice >= 0) {
                int sliceNo = millisAfterFirstSlice >> this.timesliceMillisPower;
                int i = this.timesliceCount;
                if (sliceNo < i) {
                    targetSlice = this.timeslicedPerfEvents[(this.firstSliceIndex + sliceNo) % i];
                } else {
                    int i2;
                    FilteringPerfEventList outDated = this.outDatedPerfEvents;
                    do {
                        FilteringPerfEventList newOutDated = this.timeslicedPerfEvents[this.firstSliceIndex];
                        if (newOutDated.size > 0) {
                            outDated.moveAllFrom(newOutDated);
                        }
                        int i3 = this.firstSliceIndex + 1;
                        i2 = this.timesliceCount;
                        this.firstSliceIndex = i3 % i2;
                        this.firstSliceUptimeMillis += (long) this.timesliceMillis;
                        sliceNo--;
                    } while (sliceNo >= i2);
                    targetSlice = this.timeslicedPerfEvents[(this.firstSliceIndex + sliceNo) % i2];
                }
            } else {
                targetSlice = this.outDatedPerfEvents;
            }
            FilteringPerfEventListNode head = targetSlice;
            FilteringPerfEventListNode pos = head.previous;
            while (pos != head) {
                int delta = (int) (pos.value.sortingUptimeMillis - sortingUptimeMillis);
                if (delta >= 0) {
                    if (delta <= 0) {
                        Comparator comparator = this.secondaryComparator;
                        if (comparator == null || comparator.compare(pos.value, perfEvent) <= 0) {
                            break;
                        }
                        pos = pos.previous;
                    } else {
                        pos = pos.previous;
                    }
                } else {
                    break;
                }
            }
            targetSlice.addAfter(pos, perfEvent);
            this.perfEventCount++;
        }

        public final void remove(FilteringPerfEventList fromSlice, FilteringPerfEvent perfEvent) {
            fromSlice.remove(perfEvent);
            this.perfEventCount--;
        }
    }

    private static final class TimeslicedCheckingEventList extends TimeslicedFilteringPerfEventList {
        public TimeslicedCheckingEventList(int _timesliceInMillis, int _timespanInMillis, long _firstSliceUptimeMillis, int _inclusiveMinPerfEventDurationMillis, int _exclusiveMaxPerfEventDurationMillis) {
            super(_timesliceInMillis, _timespanInMillis, _firstSliceUptimeMillis, _inclusiveMinPerfEventDurationMillis, _exclusiveMaxPerfEventDurationMillis, null);
        }
    }

    public static final class TimeslicedCompletedEventList extends TimeslicedFilteringPerfEventList {
        public TimeslicedCompletedEventList(int _timesliceInMillis, int _timespanInMillis, long _firstSliceUptimeMillis, int _inclusiveMinPerfEventDurationMillis, int _exclusiveMaxPerfEventDurationMillis) {
            super(_timesliceInMillis, _timespanInMillis, _firstSliceUptimeMillis, _inclusiveMinPerfEventDurationMillis, _exclusiveMaxPerfEventDurationMillis, new Comparator<FilteringPerfEvent>() {
                public int compare(FilteringPerfEvent perfEvent1, FilteringPerfEvent perfEvent2) {
                    long endUptimeMillis1 = perfEvent1.endUptimeMillis;
                    long endUptimeMillis2 = perfEvent2.endUptimeMillis;
                    if (endUptimeMillis1 > endUptimeMillis2) {
                        return 1;
                    }
                    if (endUptimeMillis1 < endUptimeMillis2) {
                        return -1;
                    }
                    long beginUptimeMillis1 = perfEvent1.beginUptimeMillis;
                    long beginUptimeMillis2 = perfEvent2.beginUptimeMillis;
                    if (beginUptimeMillis1 > beginUptimeMillis2) {
                        return -1;
                    }
                    if (beginUptimeMillis1 < beginUptimeMillis2) {
                        return 1;
                    }
                    int eventFlags1 = perfEvent1.eventFlags;
                    int eventFlags2 = perfEvent2.eventFlags;
                    int result = Integer.compare(eventFlags1 & 1, eventFlags2 & 1);
                    if (result != 0) {
                        return result;
                    }
                    result = Integer.compare(eventFlags1 & 262144, 262144 & eventFlags2);
                    if (result != 0) {
                        return result;
                    }
                    return Integer.compare(eventFlags1 & 6, eventFlags2 & 6);
                }
            });
        }
    }

    private static int getCeilingOfPowerByTwo(int num) {
        int power = 0;
        int numByPower = 1;
        while (numByPower < num) {
            power++;
            numByPower = 1 << power;
        }
        return power;
    }

    public PerfEventFilter(boolean _isAppSideFilter, int _maxFilterIntervalMillis) {
        this.isAppSideFilter = _isAppSideFilter;
        this.maxFilterIntervalMillis = _maxFilterIntervalMillis;
        this.minWaitTimeMillis = this.isAppSideFilter ? 0 : APP_MAX_FILTER_INTERVAL_MILLIS + this.maxFilterIntervalMillis;
        long curUptimeMillis = OsUtils.getCoarseUptimeMillisFast();
        long j = curUptimeMillis;
        this.effectivePerfEvents = new LeveledCheckingEventList(true, 256, 65536, this.minWaitTimeMillis, j);
        this.effectivePerfEventsWaitingPeer = new LeveledCheckingEventList(true, 256, 65536, this.minWaitTimeMillis, j);
        this.suspectedPerfEvents = new LeveledCheckingEventList(false, PerfSupervisionSettings.sMinPerfEventDurationMillis, 2048, this.minWaitTimeMillis, j);
        this.latestBatchBeginUptimeMillis = curUptimeMillis;
        this.latestBatchEndUptimeMillis = curUptimeMillis;
    }

    public boolean hasPendingPerfEvents() {
        return this.effectivePerfEvents.perfEventCount > 0 || this.suspectedPerfEvents.perfEventCount > 0;
    }

    public void send(FilteringPerfEventList newEffectiveEvents, FilteringPerfEventList newSuspectedEvents) {
        long[] jArr;
        long batchBeginUptimeMillis = 0;
        long batchEndUptimeMillis = 0;
        if (newEffectiveEvents.size > 0) {
            jArr = this.tempBatchTimestampMillis;
            jArr[0] = 0;
            jArr[1] = 0;
            moveNewArrivedPerfEvents(newEffectiveEvents, this.effectivePerfEvents);
            jArr = this.tempBatchTimestampMillis;
            batchBeginUptimeMillis = jArr[0];
            batchEndUptimeMillis = jArr[1];
        }
        if (newSuspectedEvents.size > 0) {
            jArr = this.tempBatchTimestampMillis;
            jArr[0] = 0;
            jArr[1] = 0;
            moveNewArrivedPerfEvents(newSuspectedEvents, this.suspectedPerfEvents);
            jArr = this.tempBatchTimestampMillis;
            if (batchBeginUptimeMillis > jArr[0]) {
                batchBeginUptimeMillis = jArr[0];
            }
            jArr = this.tempBatchTimestampMillis;
            if (batchEndUptimeMillis < jArr[1]) {
                batchEndUptimeMillis = jArr[1];
            }
        }
        if (batchBeginUptimeMillis == 0) {
            batchBeginUptimeMillis = OsUtils.getCoarseUptimeMillisFast();
        }
        if (batchEndUptimeMillis == 0) {
            batchEndUptimeMillis = OsUtils.getCoarseUptimeMillisFast();
        }
        this.latestBatchBeginUptimeMillis = Math.max(this.latestBatchBeginUptimeMillis, batchBeginUptimeMillis);
        this.latestBatchEndUptimeMillis = Math.max(this.latestBatchEndUptimeMillis, batchEndUptimeMillis);
    }

    private void moveNewArrivedPerfEvents(FilteringPerfEventList newEvents, LeveledCheckingEventList targetEventLists) {
        long earliest = 0;
        long latest = 0;
        int eventCount = newEvents.size;
        FilteringPerfEventListNode pos = newEvents.next;
        newEvents.detachElements();
        while (eventCount > 0) {
            FilteringPerfEvent filteringPerfEvent = pos.value;
            pos = pos.next;
            eventCount--;
            filteringPerfEvent.enoughPeerWaitDuration = ((filteringPerfEvent.durationMillis >> 1) + (filteringPerfEvent.durationMillis >> 2)) + (filteringPerfEvent.durationMillis >> 3);
            filteringPerfEvent.matchedPeerWaitDuration = 0;
            filteringPerfEvent.sortingUptimeMillis = filteringPerfEvent.beginUptimeMillis;
            targetEventLists.add(filteringPerfEvent);
            if (earliest == 0 || earliest > filteringPerfEvent.beginUptimeMillis) {
                earliest = filteringPerfEvent.beginUptimeMillis;
            }
            if (latest < filteringPerfEvent.endUptimeMillis) {
                latest = filteringPerfEvent.endUptimeMillis;
            }
        }
        long[] jArr = this.tempBatchTimestampMillis;
        jArr[0] = earliest;
        jArr[1] = latest;
    }

    public void filter(TimeslicedCompletedEventList completedEvents, long earliestExecutingRootEventBeginUptimeMillis) {
        while (this.effectivePerfEvents.perfEventCount > 0) {
            checkEffectivePerfEvents(completedEvents);
        }
        LeveledCheckingEventList temp = this.effectivePerfEvents;
        this.effectivePerfEvents = this.effectivePerfEventsWaitingPeer;
        this.effectivePerfEventsWaitingPeer = temp;
        if (this.suspectedPerfEvents.perfEventCount > 0) {
            checkSuspectedPerfEvents(Math.min(earliestExecutingRootEventBeginUptimeMillis, this.latestBatchBeginUptimeMillis));
        }
    }

    private void checkEffectivePerfEvents(TimeslicedCompletedEventList completedEvents) {
        int i;
        TimeslicedCheckingEventList checkingEffectiveLevel;
        for (i = this.effectivePerfEvents.longLevelCount - 1; i >= 0; i--) {
            checkingEffectiveLevel = this.effectivePerfEvents.longPerfEventsByLevel[i];
            if (checkingEffectiveLevel.perfEventCount > 0) {
                checkEffectiveLevel(checkingEffectiveLevel, completedEvents);
            }
        }
        for (i = this.effectivePerfEvents.shortLevelCount - 1; i >= 0; i--) {
            checkingEffectiveLevel = this.effectivePerfEvents.shortPerfEventsByLevel[i];
            if (checkingEffectiveLevel.perfEventCount > 0) {
                checkEffectiveLevel(checkingEffectiveLevel, completedEvents);
            }
        }
    }

    private void checkEffectiveLevel(TimeslicedCheckingEventList checkingEffectiveLevel, TimeslicedCompletedEventList completedEvents) {
        FilteringPerfEventList checkingEffectiveSlice = checkingEffectiveLevel.outDatedPerfEvents;
        if (checkingEffectiveSlice.size > 0) {
            checkEffectiveSlice(checkingEffectiveLevel, checkingEffectiveSlice, completedEvents);
            completeEffectiveSlice(checkingEffectiveLevel, checkingEffectiveSlice, completedEvents);
        }
        int timesliceCount = checkingEffectiveLevel.timesliceCount;
        int firstSliceIndex = checkingEffectiveLevel.firstSliceIndex;
        FilteringPerfEventList[] timeslicedPerfEvents = checkingEffectiveLevel.timeslicedPerfEvents;
        for (int i = 0; i < timesliceCount; i++) {
            checkingEffectiveSlice = timeslicedPerfEvents[(firstSliceIndex + i) % timesliceCount];
            if (checkingEffectiveSlice.size > 0) {
                checkEffectiveSlice(checkingEffectiveLevel, checkingEffectiveSlice, completedEvents);
            }
        }
    }

    private void completeEffectiveSlice(TimeslicedCheckingEventList checkingEffectiveLevel, FilteringPerfEventList checkingEffectiveSlice, TimeslicedCompletedEventList completedEvents) {
        int eventsSize = checkingEffectiveSlice.size;
        FilteringPerfEventListNode pos = checkingEffectiveSlice.next;
        checkingEffectiveSlice.detachElements();
        checkingEffectiveLevel.perfEventCount -= eventsSize;
        LeveledCheckingEventList leveledCheckingEventList = this.effectivePerfEvents;
        leveledCheckingEventList.perfEventCount -= eventsSize;
        while (eventsSize > 0) {
            FilteringPerfEvent checkingEffectiveEvent = pos.value;
            pos = pos.next;
            eventsSize--;
            if (checkingEffectiveEvent.event == null && checkingEffectiveEvent.detailsPtr == 0) {
                FilteringPerfEventCache.recycleUnchecked(checkingEffectiveEvent);
            } else {
                checkingEffectiveEvent.sortingUptimeMillis = checkingEffectiveEvent.endUptimeMillis;
                completedEvents.add(checkingEffectiveEvent);
            }
        }
    }

    private void checkEffectiveSlice(TimeslicedCheckingEventList checkingEffectiveLevel, FilteringPerfEventList checkingEffectiveSlice, TimeslicedCompletedEventList completedEvents) {
        int eventsSize = checkingEffectiveSlice.size;
        FilteringPerfEventListNode pos = checkingEffectiveSlice.next;
        checkingEffectiveSlice.detachElements();
        checkingEffectiveLevel.perfEventCount -= eventsSize;
        LeveledCheckingEventList leveledCheckingEventList = this.effectivePerfEvents;
        leveledCheckingEventList.perfEventCount -= eventsSize;
        while (eventsSize > 0) {
            FilteringPerfEvent checkingEffectiveEvent = pos.value;
            pos = pos.next;
            eventsSize--;
            if ((checkingEffectiveEvent.eventFlags & 268435456) == 0) {
                checkEffectiveEvent(checkingEffectiveEvent, completedEvents);
            } else if (checkingEffectiveEvent.event == null && checkingEffectiveEvent.detailsPtr == 0) {
                FilteringPerfEventCache.recycleUnchecked(checkingEffectiveEvent);
            } else {
                checkingEffectiveEvent.sortingUptimeMillis = checkingEffectiveEvent.endUptimeMillis;
                completedEvents.add(checkingEffectiveEvent);
            }
        }
    }

    private void checkEffectiveEvent(FilteringPerfEvent checkingEffectiveEvent, TimeslicedCompletedEventList completedEvents) {
        boolean needWaitForPeer;
        if ((checkingEffectiveEvent.eventFlags & 256) != 0) {
            needWaitForPeer = checkBlockedEffectiveMicroEvent(checkingEffectiveEvent);
        } else if (checkingEffectiveEvent.eventType < 65536) {
            needWaitForPeer = checkNonBlockedEffectiveMicroEvent(checkingEffectiveEvent);
        } else {
            needWaitForPeer = checkEffectiveMacroEvent(checkingEffectiveEvent);
        }
        if (needWaitForPeer) {
            this.effectivePerfEventsWaitingPeer.add(checkingEffectiveEvent);
            if (checkingEffectiveEvent.event != null || checkingEffectiveEvent.detailsPtr != 0) {
                FilteringPerfEvent completedEvent = FilteringPerfEventCache.obtain();
                completedEvent.eventType = checkingEffectiveEvent.eventType;
                completedEvent.eventFlags = checkingEffectiveEvent.eventFlags;
                completedEvent.beginUptimeMillis = checkingEffectiveEvent.beginUptimeMillis;
                completedEvent.endUptimeMillis = checkingEffectiveEvent.endUptimeMillis;
                completedEvent.inclusionId = checkingEffectiveEvent.inclusionId;
                completedEvent.synchronizationId = checkingEffectiveEvent.synchronizationId;
                completedEvent.eventSeq = checkingEffectiveEvent.eventSeq;
                completedEvent.detailsPtr = checkingEffectiveEvent.detailsPtr;
                completedEvent.event = checkingEffectiveEvent.event;
                completedEvent.durationMillis = checkingEffectiveEvent.durationMillis;
                checkingEffectiveEvent.event = null;
                checkingEffectiveEvent.detailsPtr = 0;
                completedEvent.sortingUptimeMillis = checkingEffectiveEvent.endUptimeMillis;
                completedEvents.add(completedEvent);
            }
        } else if (checkingEffectiveEvent.event == null && checkingEffectiveEvent.detailsPtr == 0) {
            FilteringPerfEventCache.recycleUnchecked(checkingEffectiveEvent);
        } else {
            checkingEffectiveEvent.sortingUptimeMillis = checkingEffectiveEvent.endUptimeMillis;
            completedEvents.add(checkingEffectiveEvent);
        }
    }

    private boolean checkBlockedEffectiveMicroEvent(FilteringPerfEvent checkingEffectiveEvent) {
        int minPeerEventDurationMs;
        int maxPeerEventDurationMs;
        int beginCheckLevel;
        int matchLevel;
        int endCheckLevel;
        TimeslicedCheckingEventList[] perfEventsByLevel;
        int i;
        TimeslicedCheckingEventList checkingSuspectedLevel;
        int eventFlags = checkingEffectiveEvent.eventFlags;
        int durationMillis = checkingEffectiveEvent.durationMillis;
        if ((eventFlags & 16384) != 0) {
            minPeerEventDurationMs = durationMillis >> 5;
            maxPeerEventDurationMs = durationMillis << 5;
        } else if ((eventFlags & 4096) != 0) {
            minPeerEventDurationMs = durationMillis;
            maxPeerEventDurationMs = durationMillis;
        } else if ((eventFlags & 8192) != 0) {
            minPeerEventDurationMs = durationMillis >> 2;
            maxPeerEventDurationMs = durationMillis << 5;
        } else {
            minPeerEventDurationMs = durationMillis >> 2;
            maxPeerEventDurationMs = durationMillis;
        }
        boolean completed = false;
        if (minPeerEventDurationMs < this.suspectedPerfEvents.longLevelFloorMillis) {
            beginCheckLevel = minPeerEventDurationMs >> this.suspectedPerfEvents.shortLevelDeltaMillisPower;
            matchLevel = durationMillis >> this.suspectedPerfEvents.shortLevelDeltaMillisPower;
            endCheckLevel = maxPeerEventDurationMs >> this.suspectedPerfEvents.shortLevelDeltaMillisPower;
            if (matchLevel >= this.suspectedPerfEvents.shortLevelCount) {
                matchLevel = this.suspectedPerfEvents.shortLevelCount - 1;
            }
            if (endCheckLevel >= this.suspectedPerfEvents.shortLevelCount) {
                endCheckLevel = this.suspectedPerfEvents.shortLevelCount - 1;
            }
            perfEventsByLevel = this.suspectedPerfEvents.shortPerfEventsByLevel;
            for (i = matchLevel; i >= beginCheckLevel; i--) {
                checkingSuspectedLevel = perfEventsByLevel[i];
                if (checkingSuspectedLevel.perfEventCount > 0) {
                    completed = checkBlockedEffectiveMicroEventWithSuspectedLevel(checkingEffectiveEvent, checkingSuspectedLevel);
                    if (completed) {
                        break;
                    }
                }
            }
            if (!completed) {
                for (i = matchLevel + 1; i <= endCheckLevel; i++) {
                    checkingSuspectedLevel = perfEventsByLevel[i];
                    if (checkingSuspectedLevel.perfEventCount > 0) {
                        completed = checkBlockedEffectiveMicroEventWithSuspectedLevel(checkingEffectiveEvent, checkingSuspectedLevel);
                        if (completed) {
                            break;
                        }
                    }
                }
            }
        }
        if (!completed && maxPeerEventDurationMs >= this.suspectedPerfEvents.longLevelFloorMillis) {
            beginCheckLevel = (minPeerEventDurationMs - this.suspectedPerfEvents.longLevelFloorMillis) >> this.suspectedPerfEvents.longLevelDeltaMillisPower;
            matchLevel = (durationMillis - this.suspectedPerfEvents.longLevelFloorMillis) >> this.suspectedPerfEvents.longLevelDeltaMillisPower;
            endCheckLevel = (maxPeerEventDurationMs - this.suspectedPerfEvents.longLevelFloorMillis) >> this.suspectedPerfEvents.longLevelDeltaMillisPower;
            if (beginCheckLevel < 0) {
                beginCheckLevel = 0;
            }
            if (matchLevel < 0) {
                matchLevel = 0;
            }
            if (matchLevel > this.suspectedPerfEvents.longLevelCount) {
                matchLevel = this.suspectedPerfEvents.longLevelCount - 1;
            }
            if (endCheckLevel >= this.suspectedPerfEvents.longLevelCount) {
                endCheckLevel = this.suspectedPerfEvents.longLevelCount - 1;
            }
            perfEventsByLevel = this.suspectedPerfEvents.longPerfEventsByLevel;
            for (i = matchLevel; i >= beginCheckLevel; i--) {
                checkingSuspectedLevel = perfEventsByLevel[i];
                if (checkingSuspectedLevel.perfEventCount > 0) {
                    completed = checkBlockedEffectiveMicroEventWithSuspectedLevel(checkingEffectiveEvent, checkingSuspectedLevel);
                    if (completed) {
                        break;
                    }
                }
            }
            if (!completed) {
                for (i = matchLevel + 1; i <= endCheckLevel; i++) {
                    checkingSuspectedLevel = perfEventsByLevel[i];
                    if (checkingSuspectedLevel.perfEventCount > 0) {
                        completed = checkBlockedEffectiveMicroEventWithSuspectedLevel(checkingEffectiveEvent, checkingSuspectedLevel);
                        if (completed) {
                            break;
                        }
                    }
                }
            }
        }
        boolean z = false;
        if (completed) {
            return false;
        }
        matchLevel = this.minWaitTimeMillis + (durationMillis << 1);
        if (matchLevel > 65536) {
            matchLevel = 65536;
        }
        if (checkingEffectiveEvent.endUptimeMillis + ((long) matchLevel) > this.latestBatchEndUptimeMillis) {
            z = true;
        }
        return z;
    }

    private boolean checkBlockedEffectiveMicroEventWithSuspectedLevel(FilteringPerfEvent checkingEffectiveEvent, TimeslicedCheckingEventList checkingSuspectedLevel) {
        long firstSliceUptimeMillis = checkingSuspectedLevel.firstSliceUptimeMillis;
        int timesliceCount = checkingSuspectedLevel.timesliceCount;
        int firstSliceIndex = checkingSuspectedLevel.firstSliceIndex;
        int timesliceMillisPower = checkingSuspectedLevel.timesliceMillisPower;
        int beginSliceNo = (((int) (checkingEffectiveEvent.beginUptimeMillis - firstSliceUptimeMillis)) - checkingSuspectedLevel.exclusiveMaxPerfEventDurationMillis) >> timesliceMillisPower;
        int endSliceNo = ((int) (checkingEffectiveEvent.endUptimeMillis - firstSliceUptimeMillis)) >> timesliceMillisPower;
        if (beginSliceNo < 0) {
            beginSliceNo = 0;
        }
        if (endSliceNo >= timesliceCount) {
            endSliceNo = timesliceCount - 1;
        }
        if (beginSliceNo == 0) {
            FilteringPerfEventList checkingSuspectedSlice = checkingSuspectedLevel.outDatedPerfEvents;
            if (checkingSuspectedSlice.size > 0 && checkBlockedEffectiveMicroEventWithSuspectedSlice(checkingEffectiveEvent, checkingSuspectedLevel, checkingSuspectedSlice)) {
                return true;
            }
        }
        FilteringPerfEventList[] timeslicedPerfEvents = checkingSuspectedLevel.timeslicedPerfEvents;
        for (int i = beginSliceNo; i <= endSliceNo; i++) {
            FilteringPerfEventList checkingSuspectedSlice2 = timeslicedPerfEvents[(firstSliceIndex + i) % timesliceCount];
            if (checkingSuspectedSlice2.size > 0 && checkBlockedEffectiveMicroEventWithSuspectedSlice(checkingEffectiveEvent, checkingSuspectedLevel, checkingSuspectedSlice2)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkBlockedEffectiveMicroEventWithSuspectedSlice(FilteringPerfEvent checkingEffectiveEvent, TimeslicedCheckingEventList checkingSuspectedLevel, FilteringPerfEventList checkingSuspectedSlice) {
        TimeslicedCheckingEventList timeslicedCheckingEventList;
        FilteringPerfEventList filteringPerfEventList;
        long j;
        FilteringPerfEvent filteringPerfEvent = checkingEffectiveEvent;
        long beginUptimeMillis = filteringPerfEvent.beginUptimeMillis;
        long endUptimeMillis = filteringPerfEvent.endUptimeMillis;
        int enoughPeerWaitDuration = filteringPerfEvent.enoughPeerWaitDuration;
        FilteringPerfEventListNode head = checkingSuspectedSlice;
        FilteringPerfEventListNode pos = head.next;
        while (pos != head) {
            FilteringPerfEvent checkingSuspectedEvent = pos.value;
            pos = pos.next;
            if (checkingSuspectedEvent.beginUptimeMillis >= endUptimeMillis) {
                timeslicedCheckingEventList = checkingSuspectedLevel;
                filteringPerfEventList = checkingSuspectedSlice;
                j = beginUptimeMillis;
                return false;
            }
            if ((checkingSuspectedEvent.eventFlags & 65536) == 0 || checkingSuspectedEvent.synchronizationId != filteringPerfEvent.synchronizationId || checkingSuspectedEvent.endUptimeMillis <= beginUptimeMillis || checkingSuspectedEvent.endUptimeMillis > filteringPerfEvent.endUptimeMillis) {
                timeslicedCheckingEventList = checkingSuspectedLevel;
                filteringPerfEventList = checkingSuspectedSlice;
                j = beginUptimeMillis;
            } else if (PerfEventConstants.isBlockedBy(filteringPerfEvent.eventType, checkingSuspectedEvent.eventType)) {
                j = beginUptimeMillis;
                beginUptimeMillis = (int) (checkingSuspectedEvent.endUptimeMillis - (filteringPerfEvent.beginUptimeMillis < checkingSuspectedEvent.beginUptimeMillis ? checkingSuspectedEvent.beginUptimeMillis : filteringPerfEvent.beginUptimeMillis));
                if (beginUptimeMillis != checkingSuspectedEvent.durationMillis) {
                    if (beginUptimeMillis < PerfSupervisionSettings.sMinOverlappedDurationMillis) {
                        timeslicedCheckingEventList = checkingSuspectedLevel;
                        filteringPerfEventList = checkingSuspectedSlice;
                    } else if ((beginUptimeMillis << 5) < filteringPerfEvent.durationMillis) {
                        timeslicedCheckingEventList = checkingSuspectedLevel;
                        filteringPerfEventList = checkingSuspectedSlice;
                    }
                }
                this.suspectedPerfEvents.remove(checkingSuspectedLevel, checkingSuspectedSlice, checkingSuspectedEvent);
                this.effectivePerfEvents.add(checkingSuspectedEvent);
                if ((filteringPerfEvent.eventFlags & 16384) != 0) {
                    filteringPerfEvent.matchedPeerWaitDuration += beginUptimeMillis;
                } else if (filteringPerfEvent.matchedPeerWaitDuration < beginUptimeMillis) {
                    filteringPerfEvent.matchedPeerWaitDuration = beginUptimeMillis;
                }
                if (filteringPerfEvent.matchedPeerWaitDuration >= enoughPeerWaitDuration) {
                    return true;
                }
            } else {
                timeslicedCheckingEventList = checkingSuspectedLevel;
                filteringPerfEventList = checkingSuspectedSlice;
                j = beginUptimeMillis;
            }
            beginUptimeMillis = j;
        }
        timeslicedCheckingEventList = checkingSuspectedLevel;
        filteringPerfEventList = checkingSuspectedSlice;
        j = beginUptimeMillis;
        return false;
    }

    private boolean checkNonBlockedEffectiveMicroEvent(FilteringPerfEvent checkingEffectiveEvent) {
        int beginCheckLevel;
        int endCheckLevel;
        TimeslicedCheckingEventList[] perfEventsByLevel;
        int i;
        TimeslicedCheckingEventList checkingSuspectedLevel;
        int eventFlags = checkingEffectiveEvent.eventFlags;
        int durationMillis = checkingEffectiveEvent.durationMillis;
        int minPeerEventDurationMs = durationMillis >> 5;
        int maxPeerEventDurationMs = durationMillis;
        if ((65537 & eventFlags) == 65536) {
            maxPeerEventDurationMs = durationMillis << 3;
        }
        if (minPeerEventDurationMs < this.suspectedPerfEvents.longLevelFloorMillis) {
            beginCheckLevel = minPeerEventDurationMs >> this.suspectedPerfEvents.shortLevelDeltaMillisPower;
            endCheckLevel = maxPeerEventDurationMs >> this.suspectedPerfEvents.shortLevelDeltaMillisPower;
            if (endCheckLevel >= this.suspectedPerfEvents.shortLevelCount) {
                endCheckLevel = this.suspectedPerfEvents.shortLevelCount - 1;
            }
            perfEventsByLevel = this.suspectedPerfEvents.shortPerfEventsByLevel;
            for (i = beginCheckLevel; i <= endCheckLevel; i++) {
                checkingSuspectedLevel = perfEventsByLevel[i];
                if (checkingSuspectedLevel.perfEventCount > 0) {
                    checkNonBlockedEffectiveMicroEventWithSuspectedLevel(checkingEffectiveEvent, checkingSuspectedLevel);
                }
            }
        }
        if (maxPeerEventDurationMs >= this.suspectedPerfEvents.longLevelFloorMillis) {
            beginCheckLevel = (minPeerEventDurationMs - this.suspectedPerfEvents.longLevelFloorMillis) >> this.suspectedPerfEvents.longLevelDeltaMillisPower;
            endCheckLevel = (maxPeerEventDurationMs - this.suspectedPerfEvents.longLevelFloorMillis) >> this.suspectedPerfEvents.longLevelDeltaMillisPower;
            if (beginCheckLevel < 0) {
                beginCheckLevel = 0;
            }
            if (endCheckLevel >= this.suspectedPerfEvents.longLevelCount) {
                endCheckLevel = this.suspectedPerfEvents.longLevelCount - 1;
            }
            perfEventsByLevel = this.suspectedPerfEvents.longPerfEventsByLevel;
            for (i = beginCheckLevel; i <= endCheckLevel; i++) {
                checkingSuspectedLevel = perfEventsByLevel[i];
                if (checkingSuspectedLevel.perfEventCount > 0) {
                    checkNonBlockedEffectiveMicroEventWithSuspectedLevel(checkingEffectiveEvent, checkingSuspectedLevel);
                }
            }
        }
        return false;
    }

    private void checkNonBlockedEffectiveMicroEventWithSuspectedLevel(FilteringPerfEvent checkingEffectiveEvent, TimeslicedCheckingEventList checkingSuspectedLevel) {
        long firstSliceUptimeMillis = checkingSuspectedLevel.firstSliceUptimeMillis;
        int timesliceCount = checkingSuspectedLevel.timesliceCount;
        int firstSliceIndex = checkingSuspectedLevel.firstSliceIndex;
        int timesliceMillisPower = checkingSuspectedLevel.timesliceMillisPower;
        int beginSliceNo = (((int) (checkingEffectiveEvent.beginUptimeMillis - firstSliceUptimeMillis)) - checkingSuspectedLevel.exclusiveMaxPerfEventDurationMillis) >> timesliceMillisPower;
        int endSliceNo = ((int) (checkingEffectiveEvent.endUptimeMillis - firstSliceUptimeMillis)) >> timesliceMillisPower;
        if (beginSliceNo < 0) {
            beginSliceNo = 0;
        }
        if (endSliceNo >= timesliceCount) {
            endSliceNo = timesliceCount - 1;
        }
        if (beginSliceNo == 0) {
            FilteringPerfEventList checkingSuspectedSlice = checkingSuspectedLevel.outDatedPerfEvents;
            if (checkingSuspectedSlice.size > 0) {
                checkNonBlockedEffectiveMicroEventWithSuspectedSlice(checkingEffectiveEvent, checkingSuspectedLevel, checkingSuspectedSlice);
            }
        }
        FilteringPerfEventList[] timeslicedPerfEvents = checkingSuspectedLevel.timeslicedPerfEvents;
        for (int i = beginSliceNo; i <= endSliceNo; i++) {
            FilteringPerfEventList checkingSuspectedSlice2 = timeslicedPerfEvents[(firstSliceIndex + i) % timesliceCount];
            if (checkingSuspectedSlice2.size > 0) {
                checkNonBlockedEffectiveMicroEventWithSuspectedSlice(checkingEffectiveEvent, checkingSuspectedLevel, checkingSuspectedSlice2);
            }
        }
    }

    private void checkNonBlockedEffectiveMicroEventWithSuspectedSlice(FilteringPerfEvent checkingEffectiveEvent, TimeslicedCheckingEventList checkingSuspectedLevel, FilteringPerfEventList checkingSuspectedSlice) {
        TimeslicedCheckingEventList timeslicedCheckingEventList;
        FilteringPerfEventList filteringPerfEventList;
        FilteringPerfEvent filteringPerfEvent = checkingEffectiveEvent;
        long beginUptimeMillis = filteringPerfEvent.beginUptimeMillis;
        long endUptimeMillis = filteringPerfEvent.endUptimeMillis;
        FilteringPerfEventListNode head = checkingSuspectedSlice;
        FilteringPerfEventListNode pos = head.next;
        while (pos != head) {
            FilteringPerfEvent checkingSuspectedEvent = pos.value;
            pos = pos.next;
            if (checkingSuspectedEvent.beginUptimeMillis >= endUptimeMillis) {
                timeslicedCheckingEventList = checkingSuspectedLevel;
                filteringPerfEventList = checkingSuspectedSlice;
                return;
            } else if ((checkingSuspectedEvent.eventFlags & 65536) != 0 || checkingSuspectedEvent.inclusionId != filteringPerfEvent.inclusionId || checkingSuspectedEvent.endUptimeMillis <= beginUptimeMillis || checkingSuspectedEvent.endUptimeMillis > filteringPerfEvent.endUptimeMillis) {
                timeslicedCheckingEventList = checkingSuspectedLevel;
                filteringPerfEventList = checkingSuspectedSlice;
            } else if (checkingSuspectedEvent.eventType >= 65536) {
                timeslicedCheckingEventList = checkingSuspectedLevel;
                filteringPerfEventList = checkingSuspectedSlice;
            } else {
                int overlappedDurationMillis = (int) (checkingSuspectedEvent.endUptimeMillis - (filteringPerfEvent.beginUptimeMillis < checkingSuspectedEvent.beginUptimeMillis ? checkingSuspectedEvent.beginUptimeMillis : filteringPerfEvent.beginUptimeMillis));
                if (overlappedDurationMillis != checkingSuspectedEvent.durationMillis) {
                    if ((filteringPerfEvent.eventFlags & 65537) != 65536 || overlappedDurationMillis < PerfSupervisionSettings.sMinOverlappedDurationMillis) {
                        timeslicedCheckingEventList = checkingSuspectedLevel;
                        filteringPerfEventList = checkingSuspectedSlice;
                    } else if ((overlappedDurationMillis << 5) < filteringPerfEvent.durationMillis) {
                        timeslicedCheckingEventList = checkingSuspectedLevel;
                        filteringPerfEventList = checkingSuspectedSlice;
                    }
                }
                this.suspectedPerfEvents.remove(checkingSuspectedLevel, checkingSuspectedSlice, checkingSuspectedEvent);
                this.effectivePerfEvents.add(checkingSuspectedEvent);
                checkingSuspectedEvent.eventFlags |= filteringPerfEvent.eventFlags & 4;
            }
        }
        timeslicedCheckingEventList = checkingSuspectedLevel;
        filteringPerfEventList = checkingSuspectedSlice;
    }

    private boolean checkEffectiveMacroEvent(FilteringPerfEvent checkingEffectiveEvent) {
        int beginCheckLevel;
        TimeslicedCheckingEventList[] perfEventsByLevel;
        int i;
        TimeslicedCheckingEventList checkingSuspectedLevel;
        int durationMillis = checkingEffectiveEvent.durationMillis;
        int minPeerEventDurationMs = durationMillis >> 5;
        if (minPeerEventDurationMs < this.suspectedPerfEvents.longLevelFloorMillis) {
            beginCheckLevel = minPeerEventDurationMs >> this.suspectedPerfEvents.shortLevelDeltaMillisPower;
            perfEventsByLevel = this.suspectedPerfEvents.shortPerfEventsByLevel;
            for (i = beginCheckLevel; i < this.suspectedPerfEvents.shortLevelCount; i++) {
                checkingSuspectedLevel = perfEventsByLevel[i];
                if (checkingSuspectedLevel.perfEventCount > 0) {
                    checkEffectiveMacroEventWithSuspectedLevel(checkingEffectiveEvent, checkingSuspectedLevel);
                }
            }
        }
        beginCheckLevel = (minPeerEventDurationMs - this.suspectedPerfEvents.longLevelFloorMillis) >> this.suspectedPerfEvents.longLevelDeltaMillisPower;
        if (beginCheckLevel < 0) {
            beginCheckLevel = 0;
        }
        perfEventsByLevel = this.suspectedPerfEvents.longPerfEventsByLevel;
        for (i = beginCheckLevel; i < this.suspectedPerfEvents.longLevelCount; i++) {
            checkingSuspectedLevel = perfEventsByLevel[i];
            if (checkingSuspectedLevel.perfEventCount > 0) {
                checkEffectiveMacroEventWithSuspectedLevel(checkingEffectiveEvent, checkingSuspectedLevel);
            }
        }
        i = this.minWaitTimeMillis + (durationMillis << 2);
        if (i > 65536) {
            i = 65536;
        }
        return checkingEffectiveEvent.endUptimeMillis + ((long) i) > this.latestBatchEndUptimeMillis;
    }

    private void checkEffectiveMacroEventWithSuspectedLevel(FilteringPerfEvent checkingEffectiveEvent, TimeslicedCheckingEventList checkingSuspectedLevel) {
        long firstSliceUptimeMillis = checkingSuspectedLevel.firstSliceUptimeMillis;
        int timesliceCount = checkingSuspectedLevel.timesliceCount;
        int firstSliceIndex = checkingSuspectedLevel.firstSliceIndex;
        int timesliceMillisPower = checkingSuspectedLevel.timesliceMillisPower;
        int beginSliceNo = (((int) (checkingEffectiveEvent.beginUptimeMillis - firstSliceUptimeMillis)) - checkingSuspectedLevel.exclusiveMaxPerfEventDurationMillis) >> timesliceMillisPower;
        int endSliceNo = ((int) (checkingEffectiveEvent.endUptimeMillis - firstSliceUptimeMillis)) >> timesliceMillisPower;
        if (beginSliceNo < 0) {
            beginSliceNo = 0;
        }
        if (endSliceNo >= timesliceCount) {
            endSliceNo = timesliceCount - 1;
        }
        if (beginSliceNo == 0) {
            FilteringPerfEventList checkingSuspectedSlice = checkingSuspectedLevel.outDatedPerfEvents;
            if (checkingSuspectedSlice.size > 0) {
                checkEffectiveMacroEventWithSuspectedSlice(checkingEffectiveEvent, checkingSuspectedLevel, checkingSuspectedSlice);
            }
        }
        FilteringPerfEventList[] timeslicedPerfEvents = checkingSuspectedLevel.timeslicedPerfEvents;
        for (int i = beginSliceNo; i <= endSliceNo; i++) {
            FilteringPerfEventList checkingSuspectedSlice2 = timeslicedPerfEvents[(firstSliceIndex + i) % timesliceCount];
            if (checkingSuspectedSlice2.size > 0) {
                checkEffectiveMacroEventWithSuspectedSlice(checkingEffectiveEvent, checkingSuspectedLevel, checkingSuspectedSlice2);
            }
        }
    }

    private void checkEffectiveMacroEventWithSuspectedSlice(FilteringPerfEvent checkingEffectiveEvent, TimeslicedCheckingEventList checkingSuspectedLevel, FilteringPerfEventList checkingSuspectedSlice) {
        TimeslicedCheckingEventList timeslicedCheckingEventList;
        FilteringPerfEventList filteringPerfEventList;
        FilteringPerfEvent filteringPerfEvent = checkingEffectiveEvent;
        long beginUptimeMillis = filteringPerfEvent.beginUptimeMillis;
        long endUptimeMillis = filteringPerfEvent.endUptimeMillis;
        FilteringPerfEventListNode head = checkingSuspectedSlice;
        FilteringPerfEventListNode pos = head.next;
        while (pos != head) {
            FilteringPerfEvent checkingSuspectedEvent = pos.value;
            pos = pos.next;
            if (checkingSuspectedEvent.beginUptimeMillis >= endUptimeMillis) {
                timeslicedCheckingEventList = checkingSuspectedLevel;
                filteringPerfEventList = checkingSuspectedSlice;
                return;
            } else if ((checkingSuspectedEvent.eventFlags & 4) == 0 || checkingSuspectedEvent.endUptimeMillis <= beginUptimeMillis || (checkingSuspectedEvent.eventFlags & 65536) != 0) {
                timeslicedCheckingEventList = checkingSuspectedLevel;
                filteringPerfEventList = checkingSuspectedSlice;
            } else if ((filteringPerfEvent.inclusionId & checkingSuspectedEvent.inclusionId) != filteringPerfEvent.inclusionId) {
                timeslicedCheckingEventList = checkingSuspectedLevel;
                filteringPerfEventList = checkingSuspectedSlice;
            } else {
                int overlappedDurationMillis = (int) (Math.min(filteringPerfEvent.endUptimeMillis, checkingSuspectedEvent.endUptimeMillis) - Math.max(filteringPerfEvent.beginUptimeMillis, checkingSuspectedEvent.beginUptimeMillis));
                if (overlappedDurationMillis != checkingSuspectedEvent.durationMillis) {
                    if (overlappedDurationMillis < PerfSupervisionSettings.sMinOverlappedDurationMillis) {
                        timeslicedCheckingEventList = checkingSuspectedLevel;
                        filteringPerfEventList = checkingSuspectedSlice;
                    } else if ((overlappedDurationMillis << 5) < filteringPerfEvent.durationMillis) {
                        timeslicedCheckingEventList = checkingSuspectedLevel;
                        filteringPerfEventList = checkingSuspectedSlice;
                    }
                }
                this.suspectedPerfEvents.remove(checkingSuspectedLevel, checkingSuspectedSlice, checkingSuspectedEvent);
                this.effectivePerfEvents.add(checkingSuspectedEvent);
            }
        }
        timeslicedCheckingEventList = checkingSuspectedLevel;
        filteringPerfEventList = checkingSuspectedSlice;
    }

    private void checkSuspectedPerfEvents(long earliestExecutingRootEventBeginUptimeMillis) {
        int i;
        TimeslicedCheckingEventList checkingSuspectedLevel;
        for (i = 0; i < this.suspectedPerfEvents.shortLevelCount; i++) {
            checkingSuspectedLevel = this.suspectedPerfEvents.shortPerfEventsByLevel[i];
            if (checkingSuspectedLevel.perfEventCount > 0) {
                checkLevelSuspectedPerfEvents(checkingSuspectedLevel, earliestExecutingRootEventBeginUptimeMillis);
            }
        }
        for (i = 0; i < this.suspectedPerfEvents.longLevelCount; i++) {
            checkingSuspectedLevel = this.suspectedPerfEvents.longPerfEventsByLevel[i];
            if (checkingSuspectedLevel.perfEventCount > 0) {
                checkLevelSuspectedPerfEvents(checkingSuspectedLevel, earliestExecutingRootEventBeginUptimeMillis);
            }
        }
    }

    private void checkLevelSuspectedPerfEvents(TimeslicedCheckingEventList checkingSuspectedLevel, long earliestExecutingRootEventBeginUptimeMillis) {
        int minPeerWaitTimeMillis;
        TimeslicedCheckingEventList timeslicedCheckingEventList = checkingSuspectedLevel;
        long j = earliestExecutingRootEventBeginUptimeMillis;
        FilteringPerfEventList checkingSuspectedSlice = timeslicedCheckingEventList.outDatedPerfEvents;
        if (checkingSuspectedSlice.size > 0) {
            recycleSuspectedSlice(timeslicedCheckingEventList, checkingSuspectedSlice);
        }
        int timesliceCount = timeslicedCheckingEventList.timesliceCount;
        int firstSliceIndex = timeslicedCheckingEventList.firstSliceIndex;
        int maxPeerWaitTimeMillis = this.minWaitTimeMillis + (timeslicedCheckingEventList.exclusiveMaxPerfEventDurationMillis << 5);
        if (maxPeerWaitTimeMillis > 65536) {
            maxPeerWaitTimeMillis = 65536;
        }
        int minPeerWaitTimeMillis2 = this.minWaitTimeMillis + (timeslicedCheckingEventList.inclusiveMinPerfEventDurationMillis << 5);
        if (minPeerWaitTimeMillis2 > 65536) {
            minPeerWaitTimeMillis2 = 65536;
        }
        int firstMaybeUnexpiredSliceNo = ((int) ((Math.max(this.latestBatchEndUptimeMillis - ((long) maxPeerWaitTimeMillis), j - ((long) this.minWaitTimeMillis)) - ((long) timeslicedCheckingEventList.exclusiveMaxPerfEventDurationMillis)) - timeslicedCheckingEventList.firstSliceUptimeMillis)) >> timeslicedCheckingEventList.timesliceMillisPower;
        int lastMaybeExpiredSliceNo = ((int) ((Math.max(this.latestBatchEndUptimeMillis - ((long) minPeerWaitTimeMillis2), j - ((long) this.minWaitTimeMillis)) - ((long) timeslicedCheckingEventList.inclusiveMinPerfEventDurationMillis)) - timeslicedCheckingEventList.firstSliceUptimeMillis)) >> timeslicedCheckingEventList.timesliceMillisPower;
        if (firstMaybeUnexpiredSliceNo < 0) {
            firstMaybeUnexpiredSliceNo = 0;
        }
        if (firstMaybeUnexpiredSliceNo >= timesliceCount) {
            firstMaybeUnexpiredSliceNo = timesliceCount - 1;
        }
        if (lastMaybeExpiredSliceNo < 0) {
            lastMaybeExpiredSliceNo = 0;
        }
        if (lastMaybeExpiredSliceNo >= timesliceCount) {
            lastMaybeExpiredSliceNo = timesliceCount - 1;
        }
        FilteringPerfEventList[] timeslicedPerfEvents = timeslicedCheckingEventList.timeslicedPerfEvents;
        int i = 0;
        while (i < firstMaybeUnexpiredSliceNo) {
            int maxPeerWaitTimeMillis2 = maxPeerWaitTimeMillis;
            maxPeerWaitTimeMillis = timeslicedPerfEvents[(firstSliceIndex + i) % timesliceCount];
            minPeerWaitTimeMillis = minPeerWaitTimeMillis2;
            if (maxPeerWaitTimeMillis.size > 0) {
                recycleSuspectedSlice(timeslicedCheckingEventList, maxPeerWaitTimeMillis);
            }
            i++;
            Object obj = maxPeerWaitTimeMillis;
            maxPeerWaitTimeMillis = maxPeerWaitTimeMillis2;
            minPeerWaitTimeMillis2 = minPeerWaitTimeMillis;
        }
        minPeerWaitTimeMillis = minPeerWaitTimeMillis2;
        i = firstMaybeUnexpiredSliceNo;
        while (i <= lastMaybeExpiredSliceNo) {
            maxPeerWaitTimeMillis = (firstSliceIndex + i) % timesliceCount;
            FilteringPerfEventList checkingSuspectedSlice2 = timeslicedPerfEvents[maxPeerWaitTimeMillis];
            int timesliceCount2 = timesliceCount;
            if (checkingSuspectedSlice2.size > 0) {
                checkSuspectedSlice(timeslicedCheckingEventList, timeslicedPerfEvents[maxPeerWaitTimeMillis], j);
            }
            i++;
            FilteringPerfEventList filteringPerfEventList = checkingSuspectedSlice2;
            timesliceCount = timesliceCount2;
        }
    }

    private void recycleSuspectedSlice(TimeslicedCheckingEventList checkingSuspectedLevel, FilteringPerfEventList checkingSuspectedSlice) {
        int eventsSize = checkingSuspectedSlice.size;
        FilteringPerfEventCache.recycleAllUnchecked(checkingSuspectedSlice);
        checkingSuspectedLevel.perfEventCount -= eventsSize;
        LeveledCheckingEventList leveledCheckingEventList = this.suspectedPerfEvents;
        leveledCheckingEventList.perfEventCount -= eventsSize;
    }

    private void checkSuspectedSlice(TimeslicedCheckingEventList checkingSuspectedLevel, FilteringPerfEventList checkingSuspectedSlice, long earliestExecutingRootEventBeginUptimeMillis) {
        FilteringPerfEventListNode head = checkingSuspectedSlice;
        FilteringPerfEventListNode pos = head.next;
        while (pos != head) {
            boolean expired;
            FilteringPerfEvent checkingSuspectedEvent = pos.value;
            pos = pos.next;
            long j = checkingSuspectedEvent.endUptimeMillis;
            int maxWaitTimeInMillis = this.minWaitTimeMillis;
            if (j < earliestExecutingRootEventBeginUptimeMillis - ((long) maxWaitTimeInMillis)) {
                expired = true;
            } else {
                maxWaitTimeInMillis += checkingSuspectedEvent.durationMillis << 5;
                if (maxWaitTimeInMillis > 65536) {
                    maxWaitTimeInMillis = 65536;
                }
                expired = checkingSuspectedEvent.endUptimeMillis + ((long) maxWaitTimeInMillis) <= this.latestBatchEndUptimeMillis;
            }
            if (expired) {
                checkingSuspectedSlice.remove(checkingSuspectedEvent);
                checkingSuspectedLevel.perfEventCount--;
                LeveledCheckingEventList leveledCheckingEventList = this.suspectedPerfEvents;
                leveledCheckingEventList.perfEventCount--;
                FilteringPerfEventCache.recycleUnchecked(checkingSuspectedEvent);
            }
        }
    }
}
