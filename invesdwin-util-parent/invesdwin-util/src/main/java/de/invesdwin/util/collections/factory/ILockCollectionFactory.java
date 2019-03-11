package de.invesdwin.util.collections.factory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

import de.invesdwin.util.collections.fast.IFastIterableList;
import de.invesdwin.util.collections.fast.IFastIterableMap;
import de.invesdwin.util.collections.fast.IFastIterableSet;
import de.invesdwin.util.collections.loadingcache.ALoadingCache;
import de.invesdwin.util.collections.loadingcache.ALoadingCacheConfig;
import de.invesdwin.util.concurrent.lock.ILock;
import de.invesdwin.util.concurrent.nested.INestedExecutor;

public interface ILockCollectionFactory {

    ILock newLock(String name);

    <T> List<T> newArrayList();

    <T> IFastIterableList<T> newFastIterableArrayList();

    <T> Set<T> newSet();

    <T> IFastIterableSet<T> newFastIterableSet();

    <T> Set<T> newLinkedSet();

    <T> IFastIterableSet<T> newFastIterableLinkedSet();

    <K, V> Map<K, V> newMap();

    <K, V> IFastIterableMap<K, V> newFastIterableMap();

    <K, V> Map<K, V> newLinkedMap();

    <K, V> Map<K, V> newConcurrentMap();

    <T> Set<T> newConcurrentSet();

    <K, V> IFastIterableMap<K, V> newFastIterableLinkedMap();

    <K, V> ALoadingCache<K, V> newLoadingCache(ALoadingCacheConfig<K, V> config);

    <K, V> NavigableMap<K, V> newTreeMap();

    <K, V> NavigableMap<K, V> newTreeMap(Comparator<? extends K> comparator);

    INestedExecutor newNestedExecutor(String name);

    /**
     * returns a lock factory with disabled synchronization for thread unsafe usage (can be externally synchronized
     * maybe)
     */
    ILockCollectionFactory disabled();

    static ILockCollectionFactory getInstance(final boolean threadSafe) {
        if (threadSafe) {
            return SynchronizedLockCollectionFactory.INSTANCE;
        } else {
            return DisabledLockCollectionFactory.INSTANCE;
        }
    }

}