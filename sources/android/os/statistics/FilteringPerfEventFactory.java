package android.os.statistics;

public class FilteringPerfEventFactory {
    public static FilteringPerfEvent createFilteringPerfEvent(int eventType, int eventFlags, long beginUptimeMillis, long endUptimeMillis, long inclusionId, long synchronizationId, long eventSeq, long detailsPtr) {
        long j = beginUptimeMillis;
        long j2 = endUptimeMillis;
        FilteringPerfEvent filteringPerfEvent = FilteringPerfEventCache.obtain();
        filteringPerfEvent.eventType = eventType;
        filteringPerfEvent.eventFlags = eventFlags;
        filteringPerfEvent.beginUptimeMillis = j;
        filteringPerfEvent.endUptimeMillis = j2;
        filteringPerfEvent.inclusionId = inclusionId;
        filteringPerfEvent.synchronizationId = synchronizationId;
        filteringPerfEvent.eventSeq = eventSeq;
        filteringPerfEvent.detailsPtr = detailsPtr;
        filteringPerfEvent.event = null;
        filteringPerfEvent.durationMillis = (int) (j2 - j);
        return filteringPerfEvent;
    }

    public static FilteringPerfEvent createFilteringPerfEvent(PerfEvent event) {
        FilteringPerfEvent filteringPerfEvent = FilteringPerfEventCache.obtain();
        filteringPerfEvent.set(event);
        return filteringPerfEvent;
    }
}
