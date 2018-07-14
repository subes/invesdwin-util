package de.invesdwin.util.collections.loadingcache.cache2k;

import java.util.Map;

import javax.annotation.concurrent.ThreadSafe;

import de.invesdwin.util.collections.delegate.ADelegateMap;
import de.invesdwin.util.error.Throwables;

@ThreadSafe
public abstract class ACache2kLoadingCacheMap<K, V> extends ADelegateMap<K, V> {

    @Override
    protected final Map<K, V> newDelegate() {
        return getConfig().newMap(this);
    }

    /**
     * May be overwritten to reconfigure the cache.
     * 
     * It is unknown if the calculations should only be kept temporarily. Such an assumption here would cause problems
     * elsewhere, thus we don't do eviction per default.
     * 
     * Null may be returned by this.
     */
    protected Cache2kLoadingCacheMapConfig getConfig() {
        return new Cache2kLoadingCacheMapConfig();
    }

    protected abstract V loadValue(K key);

    @Override
    public V get(final Object key) {
        try {
            return super.get(key);
        } catch (final Throwable e) {
            throw Throwables.propagate(e);
        }
    }

}
