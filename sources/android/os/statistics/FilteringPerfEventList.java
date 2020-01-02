package android.os.statistics;

public final class FilteringPerfEventList extends FilteringPerfEventListNode {
    public int size;

    public FilteringPerfEventList() {
        this.next = this;
        this.previous = this;
        this.size = 0;
    }

    public void detachElements() {
        this.next = this;
        this.previous = this;
        this.size = 0;
    }

    public final void addLast(FilteringPerfEvent item) {
        FilteringPerfEventListNode last = this.previous;
        this.size++;
        item.next = this;
        item.previous = last;
        this.previous = item;
        last.next = item;
    }

    public final void addAfter(FilteringPerfEventListNode pos, FilteringPerfEvent item) {
        this.size++;
        FilteringPerfEventListNode next = pos.next;
        item.next = next;
        item.previous = pos;
        pos.next = item;
        next.previous = item;
    }

    public final void remove(FilteringPerfEvent item) {
        this.size--;
        FilteringPerfEventListNode previous = item.previous;
        FilteringPerfEventListNode next = item.next;
        previous.next = next;
        next.previous = previous;
    }

    public final FilteringPerfEvent pollFirst() {
        int i = this.size;
        if (i <= 0) {
            return null;
        }
        this.size = i - 1;
        FilteringPerfEventListNode item = this.next;
        FilteringPerfEventListNode next = item.next;
        this.next = next;
        next.previous = this;
        return item.value;
    }

    /* Access modifiers changed, original: final */
    public final FilteringPerfEvent pollFirstUnchecked() {
        this.size--;
        FilteringPerfEventListNode item = this.next;
        FilteringPerfEventListNode next = item.next;
        this.next = next;
        next.previous = this;
        return item.value;
    }

    public final void moveAllFrom(FilteringPerfEventList srcList) {
        int srcSize = srcList.size;
        if (srcSize > 0) {
            FilteringPerfEventListNode srcFirst = srcList.next;
            FilteringPerfEventListNode srcLast = srcList.previous;
            srcList.detachElements();
            this.size += srcSize;
            FilteringPerfEventListNode curLast = this.previous;
            curLast.next = srcFirst;
            srcFirst.previous = curLast;
            this.previous = srcLast;
            srcLast.next = this;
        }
    }
}
