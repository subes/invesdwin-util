package de.invesdwin.util.collections.fast;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.concurrent.NotThreadSafe;

import de.invesdwin.util.bean.tuple.ImmutableEntry;
import de.invesdwin.util.collections.iterable.buffer.BufferingIterator;

/**
 * Boosts the iteration speed over the values by keeping a fast iterator instance that only gets modified when changes
 * to the map occur.
 * 
 * The iterator returned from this map is also suitable for concurrent modification during iteration.
 */
@NotThreadSafe
public abstract class AFastIterableDelegateMap<K, V> implements IFastIterableMap<K, V> {

    private transient BufferingIterator<Entry<K, V>> fastIterable;

    private transient Entry<K, V>[] entryArray;
    private transient K[] keyArray;
    private transient V[] valueArray;

    private final Map<K, V> delegate;

    private final Set<Entry<K, V>> entrySet = new EntrySet();

    private final Set<K> keySet = new KeySet();

    private final Collection<V> values = new ValuesCollection();

    protected AFastIterableDelegateMap(final Map<K, V> delegate) {
        this.delegate = delegate;
        refreshFastIterable();
    }

    public AFastIterableDelegateMap() {
        this.delegate = newDelegate();
        refreshFastIterable();
    }

    protected abstract Map<K, V> newDelegate();

    @Override
    public V put(final K key, final V value) {
        final V prev = delegate.put(key, value);
        if (prev == null) {
            addToFastIterable(key, value);
        } else if (prev != value) {
            refreshFastIterable();
        }
        return prev;
    }

    @Override
    public V putIfAbsent(final K key, final V value) {
        final V prev = delegate.putIfAbsent(key, value);
        if (prev == null) {
            addToFastIterable(key, value);
        } else if (prev != value) {
            refreshFastIterable();
        }
        return prev;
    }

    protected void addToFastIterable(final K key, final V value) {
        if (fastIterable != null) {
            fastIterable.add(ImmutableEntry.of(key, value));
        }
        entryArray = null;
        keyArray = null;
        valueArray = null;
    }

    @Override
    public void clear() {
        delegate.clear();
        fastIterable = new BufferingIterator<Entry<K, V>>();
        entryArray = null;
        keyArray = null;
        valueArray = null;
    }

    @Override
    public V remove(final Object key) {
        final V removed = delegate.remove(key);
        if (removed != null) {
            refreshFastIterable();
        }
        return removed;
    }

    /**
     * protected so it can be used inside addToFastIterable to refresh instead if desired by overriding
     */
    protected void refreshFastIterable() {
        fastIterable = null;
        entryArray = null;
        keyArray = null;
        valueArray = null;
    }

    @Override
    public boolean remove(final Object key, final Object value) {
        final boolean removed = delegate.remove(key, value);
        if (removed) {
            refreshFastIterable();
        }
        return removed;
    }

    @Override
    public Collection<V> values() {
        return values;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V[] asValueArray(final Class<V> valueType) {
        if (valueArray == null) {
            final V[] empty = (V[]) Array.newInstance(valueType, delegate.size());
            valueArray = values.toArray(empty);
        }
        return valueArray;
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    @SuppressWarnings("unchecked")
    public K[] asKeyArray(final Class<K> keyType) {
        if (keyArray == null) {
            final K[] empty = (K[]) Array.newInstance(keyType, delegate.size());
            keyArray = keySet.toArray(empty);
        }
        return keyArray;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return entrySet;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Entry<K, V>[] asEntryArray() {
        if (entryArray == null) {
            final Entry<K, V>[] empty = (Entry<K, V>[]) Array.newInstance(Entry.class, delegate.size());
            entryArray = entrySet.toArray(empty);
        }
        return entryArray;
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public int size() {
        return delegate.size();
    }

    private UnsupportedOperationException newUnmodifiableException() {
        return new UnsupportedOperationException(
                "Unmodifiable, only size/isEmpty/contains/containsAll/iterator/toArray methods supported");
    }

    @Override
    public boolean containsKey(final Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public V get(final Object key) {
        return delegate.get(key);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        delegate.putAll(m);
        refreshFastIterable();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    private final class ValuesCollection implements Collection<V> {
        @Override
        public int size() {
            return AFastIterableDelegateMap.this.size();
        }

        @Override
        public boolean isEmpty() {
            return AFastIterableDelegateMap.this.isEmpty();
        }

        @Override
        public boolean contains(final Object o) {
            return delegate.containsValue(o);
        }

        @Override
        public Iterator<V> iterator() {
            final Iterator<Entry<K, V>> iterator = entrySet.iterator();
            return new Iterator<V>() {
                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public V next() {
                    return iterator.next().getValue();
                }

            };
        }

        @Override
        public Object[] toArray() {
            return delegate.values().toArray();
        }

        @Override
        public <T> T[] toArray(final T[] a) {
            return delegate.values().toArray(a);
        }

        @Override
        public boolean add(final V e) {
            throw newUnmodifiableException();
        }

        @Override
        public boolean remove(final Object o) {
            throw newUnmodifiableException();
        }

        @Override
        public boolean containsAll(final Collection<?> c) {
            return delegate.values().containsAll(c);
        }

        @Override
        public boolean addAll(final Collection<? extends V> c) {
            throw newUnmodifiableException();
        }

        @Override
        public boolean retainAll(final Collection<?> c) {
            throw newUnmodifiableException();
        }

        @Override
        public boolean removeAll(final Collection<?> c) {
            throw newUnmodifiableException();
        }

        @Override
        public void clear() {
            throw newUnmodifiableException();
        }
    }

    private final class KeySet implements Set<K> {
        @Override
        public int size() {
            return AFastIterableDelegateMap.this.size();
        }

        @Override
        public boolean isEmpty() {
            return AFastIterableDelegateMap.this.isEmpty();
        }

        @Override
        public boolean contains(final Object o) {
            return delegate.containsKey(o);
        }

        @Override
        public Iterator<K> iterator() {
            final Iterator<Entry<K, V>> iterator = entrySet.iterator();
            return new Iterator<K>() {
                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public K next() {
                    return iterator.next().getKey();
                }
            };
        }

        @Override
        public Object[] toArray() {
            return delegate.keySet().toArray();
        }

        @Override
        public <T> T[] toArray(final T[] a) {
            return delegate.keySet().toArray(a);
        }

        @Override
        public boolean add(final K e) {
            throw newUnmodifiableException();
        }

        @Override
        public boolean remove(final Object o) {
            throw newUnmodifiableException();
        }

        @Override
        public boolean containsAll(final Collection<?> c) {
            return delegate.keySet().containsAll(c);
        }

        @Override
        public boolean addAll(final Collection<? extends K> c) {
            throw newUnmodifiableException();
        }

        @Override
        public boolean retainAll(final Collection<?> c) {
            throw newUnmodifiableException();
        }

        @Override
        public boolean removeAll(final Collection<?> c) {
            throw newUnmodifiableException();
        }

        @Override
        public void clear() {
            throw newUnmodifiableException();
        }
    }

    private final class EntrySet implements Set<Entry<K, V>> {
        @Override
        public int size() {
            return AFastIterableDelegateMap.this.size();
        }

        @Override
        public boolean isEmpty() {
            return AFastIterableDelegateMap.this.isEmpty();
        }

        @Override
        public boolean contains(final Object o) {
            return delegate.entrySet().contains(o);
        }

        @Override
        public Iterator<Entry<K, V>> iterator() {
            if (fastIterable == null) {
                fastIterable = new BufferingIterator<Entry<K, V>>();
                for (final Entry<K, V> e : delegate.entrySet()) {
                    //koloboke reuses/resets its entries, thus we have to make a safe copy
                    fastIterable.add(ImmutableEntry.of(e.getKey(), e.getValue()));
                }
            }
            return fastIterable.iterator();
        }

        @Override
        public Object[] toArray() {
            return delegate.entrySet().toArray();
        }

        @Override
        public <T> T[] toArray(final T[] a) {
            return delegate.entrySet().toArray(a);
        }

        @Override
        public boolean add(final Entry<K, V> e) {
            throw newUnmodifiableException();
        }

        @Override
        public boolean remove(final Object o) {
            throw newUnmodifiableException();
        }

        @Override
        public boolean containsAll(final Collection<?> c) {
            return delegate.entrySet().containsAll(c);
        }

        @Override
        public boolean addAll(final Collection<? extends Entry<K, V>> c) {
            throw newUnmodifiableException();
        }

        @Override
        public boolean retainAll(final Collection<?> c) {
            throw newUnmodifiableException();
        }

        @Override
        public boolean removeAll(final Collection<?> c) {
            throw newUnmodifiableException();
        }

        @Override
        public void clear() {
            throw newUnmodifiableException();
        }
    }

}
