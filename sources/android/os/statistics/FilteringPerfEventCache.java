package android.os.statistics;

public final class FilteringPerfEventCache {
    private static int curSize = 0;
    private static final FilteringPerfEventListNode head = new FilteringPerfEventListNode();
    private static final Object lock = new Object();
    private static int maxSize = 0;

    static {
        head.next = null;
    }

    private FilteringPerfEventCache() {
    }

    public static void setCapacity(int capacity) {
        maxSize = capacity;
    }

    public static FilteringPerfEvent obtain() {
        synchronized (lock) {
            if (curSize > 0) {
                FilteringPerfEventListNode item = head.next;
                head.next = item.next;
                curSize--;
                FilteringPerfEvent filteringPerfEvent = item.value;
                return filteringPerfEvent;
            }
            return new FilteringPerfEvent();
        }
    }

    public static void recycle(FilteringPerfEvent item) {
        item.dispose();
        synchronized (lock) {
            if (curSize < maxSize) {
                curSize++;
                item.next = head.next;
                head.next = item;
            }
        }
    }

    public static void recycleUnchecked(FilteringPerfEvent item) {
        item.dispose();
        synchronized (lock) {
            curSize++;
            item.next = head.next;
            head.next = item;
        }
    }

    public static void recycleAllUnchecked(FilteringPerfEventList srcList) {
        int srcSize = srcList.size;
        if (srcSize != 0) {
            for (FilteringPerfEventListNode pos = srcList.next; pos != srcList; pos = pos.next) {
                pos.value.dispose();
            }
            FilteringPerfEventListNode srcFirst = srcList.next;
            FilteringPerfEventListNode srcLast = srcList.previous;
            srcList.detachElements();
            synchronized (lock) {
                curSize += srcSize;
                FilteringPerfEventListNode curFirst = head.next;
                head.next = srcFirst;
                srcLast.next = curFirst;
            }
        }
    }

    public static void compact() {
        synchronized (lock) {
            int exceedCount = curSize - maxSize;
            if (exceedCount > 0) {
                for (int i = 0; i < exceedCount; i++) {
                    FilteringPerfEventListNode item = head.next;
                    head.next = item.next;
                }
                curSize = maxSize;
            }
        }
    }
}
