package com.android.framework.protobuf;

import com.android.framework.protobuf.FieldSet.FieldDescriptorLite;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

class SmallSortedMap<K extends Comparable<K>, V> extends AbstractMap<K, V> {
    private List<Entry> entryList;
    private boolean isImmutable;
    private volatile EntrySet lazyEntrySet;
    private final int maxArraySize;
    private Map<K, V> overflowEntries;

    private static class EmptySet {
        private static final Iterable<Object> ITERABLE = new Iterable<Object>() {
            public Iterator<Object> iterator() {
                return EmptySet.ITERATOR;
            }
        };
        private static final Iterator<Object> ITERATOR = new Iterator<Object>() {
            public boolean hasNext() {
                return false;
            }

            public Object next() {
                throw new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };

        private EmptySet() {
        }

        static <T> Iterable<T> iterable() {
            return ITERABLE;
        }
    }

    private class Entry implements java.util.Map.Entry<K, V>, Comparable<Entry> {
        private final K key;
        private V value;

        Entry(SmallSortedMap smallSortedMap, java.util.Map.Entry<K, V> copy) {
            this((Comparable) copy.getKey(), copy.getValue());
        }

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public int compareTo(Entry other) {
            return getKey().compareTo(other.getKey());
        }

        public V setValue(V newValue) {
            SmallSortedMap.this.checkMutable();
            V oldValue = this.value;
            this.value = newValue;
            return oldValue;
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (o == this) {
                return true;
            }
            if (!(o instanceof java.util.Map.Entry)) {
                return false;
            }
            java.util.Map.Entry<?, ?> other = (java.util.Map.Entry) o;
            if (!(equals(this.key, other.getKey()) && equals(this.value, other.getValue()))) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            Comparable comparable = this.key;
            int i = 0;
            int hashCode = comparable == null ? 0 : comparable.hashCode();
            Object obj = this.value;
            if (obj != null) {
                i = obj.hashCode();
            }
            return hashCode ^ i;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.key);
            stringBuilder.append("=");
            stringBuilder.append(this.value);
            return stringBuilder.toString();
        }

        private boolean equals(Object o1, Object o2) {
            if (o1 == null) {
                return o2 == null;
            } else {
                return o1.equals(o2);
            }
        }
    }

    private class EntryIterator implements Iterator<java.util.Map.Entry<K, V>> {
        private Iterator<java.util.Map.Entry<K, V>> lazyOverflowIterator;
        private boolean nextCalledBeforeRemove;
        private int pos;

        private EntryIterator() {
            this.pos = -1;
        }

        /* synthetic */ EntryIterator(SmallSortedMap x0, AnonymousClass1 x1) {
            this();
        }

        public boolean hasNext() {
            if (this.pos + 1 < SmallSortedMap.this.entryList.size() || getOverflowIterator().hasNext()) {
                return true;
            }
            return false;
        }

        public java.util.Map.Entry<K, V> next() {
            this.nextCalledBeforeRemove = true;
            int i = this.pos + 1;
            this.pos = i;
            if (i < SmallSortedMap.this.entryList.size()) {
                return (java.util.Map.Entry) SmallSortedMap.this.entryList.get(this.pos);
            }
            return (java.util.Map.Entry) getOverflowIterator().next();
        }

        public void remove() {
            if (this.nextCalledBeforeRemove) {
                this.nextCalledBeforeRemove = false;
                SmallSortedMap.this.checkMutable();
                if (this.pos < SmallSortedMap.this.entryList.size()) {
                    SmallSortedMap smallSortedMap = SmallSortedMap.this;
                    int i = this.pos;
                    this.pos = i - 1;
                    smallSortedMap.removeArrayEntryAt(i);
                    return;
                }
                getOverflowIterator().remove();
                return;
            }
            throw new IllegalStateException("remove() was called before next()");
        }

        private Iterator<java.util.Map.Entry<K, V>> getOverflowIterator() {
            if (this.lazyOverflowIterator == null) {
                this.lazyOverflowIterator = SmallSortedMap.this.overflowEntries.entrySet().iterator();
            }
            return this.lazyOverflowIterator;
        }
    }

    private class EntrySet extends AbstractSet<java.util.Map.Entry<K, V>> {
        private EntrySet() {
        }

        /* synthetic */ EntrySet(SmallSortedMap x0, AnonymousClass1 x1) {
            this();
        }

        public Iterator<java.util.Map.Entry<K, V>> iterator() {
            return new EntryIterator(SmallSortedMap.this, null);
        }

        public int size() {
            return SmallSortedMap.this.size();
        }

        public boolean contains(Object o) {
            java.util.Map.Entry<K, V> entry = (java.util.Map.Entry) o;
            V existing = SmallSortedMap.this.get(entry.getKey());
            V value = entry.getValue();
            return existing == value || (existing != null && existing.equals(value));
        }

        public boolean add(java.util.Map.Entry<K, V> entry) {
            if (contains(entry)) {
                return false;
            }
            SmallSortedMap.this.put((Comparable) entry.getKey(), entry.getValue());
            return true;
        }

        public boolean remove(Object o) {
            java.util.Map.Entry<K, V> entry = (java.util.Map.Entry) o;
            if (!contains(entry)) {
                return false;
            }
            SmallSortedMap.this.remove(entry.getKey());
            return true;
        }

        public void clear() {
            SmallSortedMap.this.clear();
        }
    }

    /* synthetic */ SmallSortedMap(int x0, AnonymousClass1 x1) {
        this(x0);
    }

    static <FieldDescriptorType extends FieldDescriptorLite<FieldDescriptorType>> SmallSortedMap<FieldDescriptorType, Object> newFieldMap(int arraySize) {
        return new SmallSortedMap<FieldDescriptorType, Object>(arraySize) {
            public void makeImmutable() {
                if (!isImmutable()) {
                    java.util.Map.Entry<FieldDescriptorType, Object> entry;
                    for (int i = 0; i < getNumArrayEntries(); i++) {
                        entry = getArrayEntryAt(i);
                        if (((FieldDescriptorLite) entry.getKey()).isRepeated()) {
                            entry.setValue(Collections.unmodifiableList((List) entry.getValue()));
                        }
                    }
                    for (java.util.Map.Entry<FieldDescriptorType, Object> entry2 : getOverflowEntries()) {
                        if (((FieldDescriptorLite) entry2.getKey()).isRepeated()) {
                            entry2.setValue(Collections.unmodifiableList((List) entry2.getValue()));
                        }
                    }
                }
                super.makeImmutable();
            }
        };
    }

    static <K extends Comparable<K>, V> SmallSortedMap<K, V> newInstanceForTest(int arraySize) {
        return new SmallSortedMap(arraySize);
    }

    private SmallSortedMap(int arraySize) {
        this.maxArraySize = arraySize;
        this.entryList = Collections.emptyList();
        this.overflowEntries = Collections.emptyMap();
    }

    public void makeImmutable() {
        if (!this.isImmutable) {
            Map emptyMap;
            if (this.overflowEntries.isEmpty()) {
                emptyMap = Collections.emptyMap();
            } else {
                emptyMap = Collections.unmodifiableMap(this.overflowEntries);
            }
            this.overflowEntries = emptyMap;
            this.isImmutable = true;
        }
    }

    public boolean isImmutable() {
        return this.isImmutable;
    }

    public int getNumArrayEntries() {
        return this.entryList.size();
    }

    public java.util.Map.Entry<K, V> getArrayEntryAt(int index) {
        return (java.util.Map.Entry) this.entryList.get(index);
    }

    public int getNumOverflowEntries() {
        return this.overflowEntries.size();
    }

    public Iterable<java.util.Map.Entry<K, V>> getOverflowEntries() {
        if (this.overflowEntries.isEmpty()) {
            return EmptySet.iterable();
        }
        return this.overflowEntries.entrySet();
    }

    public int size() {
        return this.entryList.size() + this.overflowEntries.size();
    }

    public boolean containsKey(Object o) {
        Comparable key = (Comparable) o;
        return binarySearchInArray(key) >= 0 || this.overflowEntries.containsKey(key);
    }

    public V get(Object o) {
        Comparable key = (Comparable) o;
        int index = binarySearchInArray(key);
        if (index >= 0) {
            return ((Entry) this.entryList.get(index)).getValue();
        }
        return this.overflowEntries.get(key);
    }

    public V put(K key, V value) {
        checkMutable();
        int index = binarySearchInArray(key);
        if (index >= 0) {
            return ((Entry) this.entryList.get(index)).setValue(value);
        }
        ensureEntryArrayMutable();
        int insertionPoint = -(index + 1);
        if (insertionPoint >= this.maxArraySize) {
            return getOverflowEntriesMutable().put(key, value);
        }
        int size = this.entryList.size();
        int i = this.maxArraySize;
        if (size == i) {
            Entry lastEntryInArray = (Entry) this.entryList.remove(i - 1);
            getOverflowEntriesMutable().put(lastEntryInArray.getKey(), lastEntryInArray.getValue());
        }
        this.entryList.add(insertionPoint, new Entry(key, value));
        return null;
    }

    public void clear() {
        checkMutable();
        if (!this.entryList.isEmpty()) {
            this.entryList.clear();
        }
        if (!this.overflowEntries.isEmpty()) {
            this.overflowEntries.clear();
        }
    }

    public V remove(Object o) {
        checkMutable();
        Comparable key = (Comparable) o;
        int index = binarySearchInArray(key);
        if (index >= 0) {
            return removeArrayEntryAt(index);
        }
        if (this.overflowEntries.isEmpty()) {
            return null;
        }
        return this.overflowEntries.remove(key);
    }

    private V removeArrayEntryAt(int index) {
        checkMutable();
        V removed = ((Entry) this.entryList.remove(index)).getValue();
        if (!this.overflowEntries.isEmpty()) {
            Iterator<java.util.Map.Entry<K, V>> iterator = getOverflowEntriesMutable().entrySet().iterator();
            this.entryList.add(new Entry(this, (java.util.Map.Entry) iterator.next()));
            iterator.remove();
        }
        return removed;
    }

    private int binarySearchInArray(K key) {
        int cmp;
        int left = 0;
        int right = this.entryList.size() - 1;
        if (right >= 0) {
            cmp = key.compareTo(((Entry) this.entryList.get(right)).getKey());
            if (cmp > 0) {
                return -(right + 2);
            }
            if (cmp == 0) {
                return right;
            }
        }
        while (left <= right) {
            cmp = (left + right) / 2;
            int cmp2 = key.compareTo(((Entry) this.entryList.get(cmp)).getKey());
            if (cmp2 < 0) {
                right = cmp - 1;
            } else if (cmp2 <= 0) {
                return cmp;
            } else {
                left = cmp + 1;
            }
        }
        return -(left + 1);
    }

    public Set<java.util.Map.Entry<K, V>> entrySet() {
        if (this.lazyEntrySet == null) {
            this.lazyEntrySet = new EntrySet(this, null);
        }
        return this.lazyEntrySet;
    }

    private void checkMutable() {
        if (this.isImmutable) {
            throw new UnsupportedOperationException();
        }
    }

    private SortedMap<K, V> getOverflowEntriesMutable() {
        checkMutable();
        if (this.overflowEntries.isEmpty() && !(this.overflowEntries instanceof TreeMap)) {
            this.overflowEntries = new TreeMap();
        }
        return (SortedMap) this.overflowEntries;
    }

    private void ensureEntryArrayMutable() {
        checkMutable();
        if (this.entryList.isEmpty() && !(this.entryList instanceof ArrayList)) {
            this.entryList = new ArrayList(this.maxArraySize);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmallSortedMap)) {
            return super.equals(o);
        }
        SmallSortedMap<?, ?> other = (SmallSortedMap) o;
        int size = size();
        if (size != other.size()) {
            return false;
        }
        int numArrayEntries = getNumArrayEntries();
        if (numArrayEntries != other.getNumArrayEntries()) {
            return entrySet().equals(other.entrySet());
        }
        for (int i = 0; i < numArrayEntries; i++) {
            if (!getArrayEntryAt(i).equals(other.getArrayEntryAt(i))) {
                return false;
            }
        }
        if (numArrayEntries != size) {
            return this.overflowEntries.equals(other.overflowEntries);
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        for (int i = 0; i < getNumArrayEntries(); i++) {
            h += ((Entry) this.entryList.get(i)).hashCode();
        }
        if (getNumOverflowEntries() > 0) {
            return h + this.overflowEntries.hashCode();
        }
        return h;
    }
}
