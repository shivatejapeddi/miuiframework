package com.android.framework.protobuf;

import com.android.framework.protobuf.Internal.EnumLite;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class MapFieldLite<K, V> implements MutabilityOracle {
    private static final MapFieldLite EMPTY_MAP_FIELD = new MapFieldLite(Collections.emptyMap());
    private boolean isMutable = true;
    private MutatabilityAwareMap<K, V> mapData;

    private static class MutatabilityAwareCollection<E> implements Collection<E> {
        private final Collection<E> delegate;
        private final MutabilityOracle mutabilityOracle;

        MutatabilityAwareCollection(MutabilityOracle mutabilityOracle, Collection<E> delegate) {
            this.mutabilityOracle = mutabilityOracle;
            this.delegate = delegate;
        }

        public int size() {
            return this.delegate.size();
        }

        public boolean isEmpty() {
            return this.delegate.isEmpty();
        }

        public boolean contains(Object o) {
            return this.delegate.contains(o);
        }

        public Iterator<E> iterator() {
            return new MutatabilityAwareIterator(this.mutabilityOracle, this.delegate.iterator());
        }

        public Object[] toArray() {
            return this.delegate.toArray();
        }

        public <T> T[] toArray(T[] a) {
            return this.delegate.toArray(a);
        }

        public boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object o) {
            this.mutabilityOracle.ensureMutable();
            return this.delegate.remove(o);
        }

        public boolean containsAll(Collection<?> c) {
            return this.delegate.containsAll(c);
        }

        public boolean addAll(Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> c) {
            this.mutabilityOracle.ensureMutable();
            return this.delegate.removeAll(c);
        }

        public boolean retainAll(Collection<?> c) {
            this.mutabilityOracle.ensureMutable();
            return this.delegate.retainAll(c);
        }

        public void clear() {
            this.mutabilityOracle.ensureMutable();
            this.delegate.clear();
        }

        public boolean equals(Object o) {
            return this.delegate.equals(o);
        }

        public int hashCode() {
            return this.delegate.hashCode();
        }

        public String toString() {
            return this.delegate.toString();
        }
    }

    private static class MutatabilityAwareIterator<E> implements Iterator<E> {
        private final Iterator<E> delegate;
        private final MutabilityOracle mutabilityOracle;

        MutatabilityAwareIterator(MutabilityOracle mutabilityOracle, Iterator<E> delegate) {
            this.mutabilityOracle = mutabilityOracle;
            this.delegate = delegate;
        }

        public boolean hasNext() {
            return this.delegate.hasNext();
        }

        public E next() {
            return this.delegate.next();
        }

        public void remove() {
            this.mutabilityOracle.ensureMutable();
            this.delegate.remove();
        }

        public boolean equals(Object obj) {
            return this.delegate.equals(obj);
        }

        public int hashCode() {
            return this.delegate.hashCode();
        }

        public String toString() {
            return this.delegate.toString();
        }
    }

    static class MutatabilityAwareMap<K, V> implements Map<K, V> {
        private final Map<K, V> delegate;
        private final MutabilityOracle mutabilityOracle;

        MutatabilityAwareMap(MutabilityOracle mutabilityOracle, Map<K, V> delegate) {
            this.mutabilityOracle = mutabilityOracle;
            this.delegate = delegate;
        }

        public int size() {
            return this.delegate.size();
        }

        public boolean isEmpty() {
            return this.delegate.isEmpty();
        }

        public boolean containsKey(Object key) {
            return this.delegate.containsKey(key);
        }

        public boolean containsValue(Object value) {
            return this.delegate.containsValue(value);
        }

        public V get(Object key) {
            return this.delegate.get(key);
        }

        public V put(K key, V value) {
            this.mutabilityOracle.ensureMutable();
            return this.delegate.put(key, value);
        }

        public V remove(Object key) {
            this.mutabilityOracle.ensureMutable();
            return this.delegate.remove(key);
        }

        public void putAll(Map<? extends K, ? extends V> m) {
            this.mutabilityOracle.ensureMutable();
            this.delegate.putAll(m);
        }

        public void clear() {
            this.mutabilityOracle.ensureMutable();
            this.delegate.clear();
        }

        public Set<K> keySet() {
            return new MutatabilityAwareSet(this.mutabilityOracle, this.delegate.keySet());
        }

        public Collection<V> values() {
            return new MutatabilityAwareCollection(this.mutabilityOracle, this.delegate.values());
        }

        public Set<Entry<K, V>> entrySet() {
            return new MutatabilityAwareSet(this.mutabilityOracle, this.delegate.entrySet());
        }

        public boolean equals(Object o) {
            return this.delegate.equals(o);
        }

        public int hashCode() {
            return this.delegate.hashCode();
        }

        public String toString() {
            return this.delegate.toString();
        }
    }

    private static class MutatabilityAwareSet<E> implements Set<E> {
        private final Set<E> delegate;
        private final MutabilityOracle mutabilityOracle;

        MutatabilityAwareSet(MutabilityOracle mutabilityOracle, Set<E> delegate) {
            this.mutabilityOracle = mutabilityOracle;
            this.delegate = delegate;
        }

        public int size() {
            return this.delegate.size();
        }

        public boolean isEmpty() {
            return this.delegate.isEmpty();
        }

        public boolean contains(Object o) {
            return this.delegate.contains(o);
        }

        public Iterator<E> iterator() {
            return new MutatabilityAwareIterator(this.mutabilityOracle, this.delegate.iterator());
        }

        public Object[] toArray() {
            return this.delegate.toArray();
        }

        public <T> T[] toArray(T[] a) {
            return this.delegate.toArray(a);
        }

        public boolean add(E e) {
            this.mutabilityOracle.ensureMutable();
            return this.delegate.add(e);
        }

        public boolean remove(Object o) {
            this.mutabilityOracle.ensureMutable();
            return this.delegate.remove(o);
        }

        public boolean containsAll(Collection<?> c) {
            return this.delegate.containsAll(c);
        }

        public boolean addAll(Collection<? extends E> c) {
            this.mutabilityOracle.ensureMutable();
            return this.delegate.addAll(c);
        }

        public boolean retainAll(Collection<?> c) {
            this.mutabilityOracle.ensureMutable();
            return this.delegate.retainAll(c);
        }

        public boolean removeAll(Collection<?> c) {
            this.mutabilityOracle.ensureMutable();
            return this.delegate.removeAll(c);
        }

        public void clear() {
            this.mutabilityOracle.ensureMutable();
            this.delegate.clear();
        }

        public boolean equals(Object o) {
            return this.delegate.equals(o);
        }

        public int hashCode() {
            return this.delegate.hashCode();
        }

        public String toString() {
            return this.delegate.toString();
        }
    }

    private MapFieldLite(Map<K, V> mapData) {
        this.mapData = new MutatabilityAwareMap(this, mapData);
    }

    static {
        EMPTY_MAP_FIELD.makeImmutable();
    }

    public static <K, V> MapFieldLite<K, V> emptyMapField() {
        return EMPTY_MAP_FIELD;
    }

    public static <K, V> MapFieldLite<K, V> newMapField() {
        return new MapFieldLite(new LinkedHashMap());
    }

    public Map<K, V> getMap() {
        return Collections.unmodifiableMap(this.mapData);
    }

    public Map<K, V> getMutableMap() {
        return this.mapData;
    }

    public void mergeFrom(MapFieldLite<K, V> other) {
        this.mapData.putAll(copy(other.mapData));
    }

    public void clear() {
        this.mapData.clear();
    }

    private static boolean equals(Object a, Object b) {
        if ((a instanceof byte[]) && (b instanceof byte[])) {
            return Arrays.equals((byte[]) a, (byte[]) b);
        }
        return a.equals(b);
    }

    static <K, V> boolean equals(Map<K, V> a, Map<K, V> b) {
        if (a == b) {
            return true;
        }
        if (a.size() != b.size()) {
            return false;
        }
        for (Entry<K, V> entry : a.entrySet()) {
            if (!b.containsKey(entry.getKey()) || !equals(entry.getValue(), b.get(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        if (!(object instanceof MapFieldLite)) {
            return false;
        }
        return equals(this.mapData, ((MapFieldLite) object).mapData);
    }

    private static int calculateHashCodeForObject(Object a) {
        if (a instanceof byte[]) {
            return Internal.hashCode((byte[]) a);
        }
        if (!(a instanceof EnumLite)) {
            return a.hashCode();
        }
        throw new UnsupportedOperationException();
    }

    static <K, V> int calculateHashCodeForMap(Map<K, V> a) {
        int result = 0;
        for (Entry<K, V> entry : a.entrySet()) {
            result += calculateHashCodeForObject(entry.getKey()) ^ calculateHashCodeForObject(entry.getValue());
        }
        return result;
    }

    public int hashCode() {
        return calculateHashCodeForMap(this.mapData);
    }

    private static Object copy(Object object) {
        if (!(object instanceof byte[])) {
            return object;
        }
        byte[] data = (byte[]) object;
        return Arrays.copyOf(data, data.length);
    }

    static <K, V> Map<K, V> copy(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap();
        for (Entry<K, V> entry : map.entrySet()) {
            result.put(entry.getKey(), copy(entry.getValue()));
        }
        return result;
    }

    public MapFieldLite<K, V> copy() {
        return new MapFieldLite(copy(this.mapData));
    }

    public void makeImmutable() {
        this.isMutable = false;
    }

    public boolean isMutable() {
        return this.isMutable;
    }

    public void ensureMutable() {
        if (!isMutable()) {
            throw new UnsupportedOperationException();
        }
    }
}
