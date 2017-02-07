package de.invesdwin.util.collections.loadingcache.historical.query.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.base.Optional;

import de.invesdwin.util.collections.iterable.ICloseableIterable;
import de.invesdwin.util.collections.iterable.ICloseableIterator;
import de.invesdwin.util.collections.iterable.WrapperCloseableIterator;
import de.invesdwin.util.collections.loadingcache.historical.query.IHistoricalCacheQuery;
import de.invesdwin.util.collections.loadingcache.historical.query.IHistoricalCacheQueryElementFilter;
import de.invesdwin.util.collections.loadingcache.historical.query.IHistoricalCacheQueryWithFuture;
import de.invesdwin.util.collections.loadingcache.historical.query.internal.core.IHistoricalCacheQueryCore;
import de.invesdwin.util.collections.loadingcache.historical.query.internal.core.IHistoricalCacheQueryInternalMethods;
import de.invesdwin.util.time.fdate.FDate;

@NotThreadSafe
public class HistoricalCacheQuery<V> implements IHistoricalCacheQuery<V> {

    private static final HistoricalCacheAssertValue DEFAULT_ASSERT_VALUE = HistoricalCacheAssertValue.ASSERT_VALUE_WITHOUT_FUTURE;
    protected HistoricalCacheAssertValue assertValue = DEFAULT_ASSERT_VALUE;
    protected final IHistoricalCacheQueryCore<V> core;
    protected final IHistoricalCacheQueryInternalMethods<V> internalMethods = new IHistoricalCacheQueryInternalMethods<V>() {

        @Override
        public boolean isRememberNullValue() {
            return rememberNullValue;
        }

        @Override
        public HistoricalCacheAssertValue getAssertValue() {
            return assertValue;
        }

        @Override
        public IHistoricalCacheQueryElementFilter<V> getElementFilter() {
            return elementFilter;
        }

        @Override
        public List<Entry<FDate, V>> newEntriesList(final int size) {
            return HistoricalCacheQuery.this.newEntriesList(size);
        }

        @Override
        public boolean isFilterDuplicateKeys() {
            return filterDuplicateKeys;
        }

    };
    private boolean filterDuplicateKeys = true;
    private boolean rememberNullValue = false;
    private IHistoricalCacheQueryElementFilter<V> elementFilter;

    public HistoricalCacheQuery(final IHistoricalCacheQueryCore<V> core) {
        this.core = core;
    }

    @Override
    public IHistoricalCacheQuery<V> withElementFilter(final IHistoricalCacheQueryElementFilter<V> elementFilter) {
        if (elementFilter == null) {
            this.elementFilter = null;
        } else {
            this.elementFilter = elementFilter;
        }
        return this;
    }

    @Override
    public IHistoricalCacheQuery<V> withFilterDuplicateKeys(final boolean filterDuplicateKeys) {
        this.filterDuplicateKeys = filterDuplicateKeys;
        return this;
    }

    @Override
    public IHistoricalCacheQuery<V> withRememberNullValue(final boolean rememberNullValue) {
        this.rememberNullValue = rememberNullValue;
        return this;
    }

    /**
     * Prevents exeption on future value and instead returns null.
     */
    @Override
    public IHistoricalCacheQuery<V> withFutureNull() {
        checkAssertValueUnchangedAndSet(HistoricalCacheAssertValue.ASSERT_VALUE_WITH_FUTURE_NULL);
        return this;
    }

    /**
     * Allows values from future.
     */
    @Override
    public final IHistoricalCacheQueryWithFuture<V> withFuture() {
        checkAssertValueUnchangedAndSet(HistoricalCacheAssertValue.ASSERT_VALUE_WITH_FUTURE);
        return newFutureQuery();
    }

    @Override
    public final Entry<FDate, V> getEntry(final FDate key) {
        return core.getEntry(internalMethods, key, assertValue);
    }

    @Override
    public V getValue(final FDate key) {
        return core.getValue(internalMethods, key, assertValue);
    }

    @Override
    public final ICloseableIterable<Entry<FDate, V>> getEntries(final Iterable<FDate> keys) {
        return getEntries(keys, assertValue);
    }

    @Override
    public final ICloseableIterable<V> getValues(final Iterable<FDate> keys) {
        return getValues(keys, assertValue);
    }

    /**
     * If a key returns null, it will get skipped.
     */
    protected final ICloseableIterable<Entry<FDate, V>> getEntries(final Iterable<FDate> keys,
            final HistoricalCacheAssertValue assertValue) {
        return new ICloseableIterable<Entry<FDate, V>>() {
            @Override
            public ICloseableIterator<Entry<FDate, V>> iterator() {
                return new ICloseableIterator<Entry<FDate, V>>() {
                    private final ICloseableIterator<FDate> keysIterator = WrapperCloseableIterator
                            .maybeWrap(keys.iterator());

                    @Override
                    public boolean hasNext() {
                        return keysIterator.hasNext();
                    }

                    @Override
                    public Entry<FDate, V> next() {
                        return core.getEntry(internalMethods, keysIterator.next(), assertValue);
                    }

                    @Override
                    public void close() {
                        keysIterator.close();
                    }
                };
            }
        };
    }

    protected final ICloseableIterable<V> getValues(final Iterable<FDate> keys,
            final HistoricalCacheAssertValue assertValue) {
        return new ICloseableIterable<V>() {
            @Override
            public ICloseableIterator<V> iterator() {
                return new ICloseableIterator<V>() {
                    private final ICloseableIterator<Entry<FDate, V>> entriesIterator = getEntries(keys, assertValue)
                            .iterator();

                    @Override
                    public boolean hasNext() {
                        return entriesIterator.hasNext();
                    }

                    @Override
                    public V next() {
                        return HistoricalCacheAssertValue.unwrapEntryValue(entriesIterator.next());
                    }

                    @Override
                    public void close() {
                        entriesIterator.close();
                    }
                };
            }
        };
    }

    @Override
    public FDate getKey(final FDate key) {
        final IHistoricalCacheQuery<?> interceptor = newKeysQueryInterceptor();
        if (interceptor != null) {
            return interceptor.getKey(key);
        }
        if (key == null) {
            return null;
        }
        return HistoricalCacheAssertValue.unwrapEntryKey(core.getParent(),
                core.getEntry(internalMethods, key, assertValue));
    }

    @Override
    public final FDate getPreviousKey(final FDate key, final int shiftBackUnits) {
        final IHistoricalCacheQuery<?> interceptor = newKeysQueryInterceptor();
        if (interceptor != null) {
            return interceptor.getPreviousKey(key, shiftBackUnits);
        }
        assertShiftUnitsPositive(shiftBackUnits);
        return HistoricalCacheAssertValue.unwrapEntryKey(core.getParent(), getPreviousEntry(key, shiftBackUnits));
    }

    /**
     * Skips null values for keys.
     * 
     * Fills the list with keys from the past.
     */
    @Override
    public final ICloseableIterable<FDate> getPreviousKeys(final FDate key, final int shiftBackUnits) {
        final IHistoricalCacheQuery<?> interceptor = newKeysQueryInterceptor();
        if (interceptor != null) {
            return interceptor.getPreviousKeys(key, shiftBackUnits);
        }
        assertShiftUnitsPositiveNonZero(shiftBackUnits);
        return new ICloseableIterable<FDate>() {
            @Override
            public ICloseableIterator<FDate> iterator() {
                return new ICloseableIterator<FDate>() {
                    private final ICloseableIterator<Entry<FDate, V>> previousEntries = getPreviousEntries(key,
                            shiftBackUnits).iterator();

                    @Override
                    public boolean hasNext() {
                        return previousEntries.hasNext();
                    }

                    @Override
                    public FDate next() {
                        return HistoricalCacheAssertValue.unwrapEntryKey(core.getParent(), previousEntries.next());
                    }

                    @Override
                    public void close() {
                        previousEntries.close();
                    }
                };
            }

        };
    }

    @Override
    public final V getPreviousValue(final FDate key, final int shiftBackUnits) {
        assertShiftUnitsPositive(shiftBackUnits);
        return HistoricalCacheAssertValue.unwrapEntryValue(getPreviousEntry(key, shiftBackUnits));
    }

    @Override
    public final ICloseableIterable<V> getPreviousValues(final FDate key, final int shiftBackUnits) {
        assertShiftUnitsPositiveNonZero(shiftBackUnits);
        return new ICloseableIterable<V>() {
            @Override
            public ICloseableIterator<V> iterator() {
                return new ICloseableIterator<V>() {
                    private final ICloseableIterator<Entry<FDate, V>> previousEntries = getPreviousEntries(key,
                            shiftBackUnits).iterator();

                    @Override
                    public boolean hasNext() {
                        return previousEntries.hasNext();
                    }

                    @Override
                    public V next() {
                        return HistoricalCacheAssertValue.unwrapEntryValue(previousEntries.next());
                    }

                    @Override
                    public void close() {
                        previousEntries.close();
                    }
                };
            }

        };
    }

    protected void assertShiftUnitsPositiveNonZero(final int shiftUnits) {
        if (shiftUnits <= 0) {
            throw new IllegalArgumentException("shiftUnits needs to be a positive non zero value: " + shiftUnits);
        }
    }

    protected void assertShiftUnitsPositive(final int shiftUnits) {
        if (shiftUnits < 0) {
            throw new IllegalArgumentException("shiftUnits needs to be a positive value: " + shiftUnits);
        }
    }

    /**
     * Returns all keys in the given time range.
     */
    @Override
    public ICloseableIterable<FDate> getKeys(final FDate from, final FDate to) {
        final IHistoricalCacheQuery<?> interceptor = newKeysQueryInterceptor();
        if (interceptor != null) {
            return interceptor.getKeys(from, to);
        }
        final ICloseableIterable<FDate> iterableInterceptor = core.getParent().getRangeQueryInterceptor().getKeys(from, to);
        if (iterableInterceptor != null) {
            return iterableInterceptor;
        } else {
            return new ICloseableIterable<FDate>() {
                @Override
                public ICloseableIterator<FDate> iterator() {
                    return new ICloseableIterator<FDate>() {

                        private final ICloseableIterator<Entry<FDate, V>> entriesIterator = getEntries(from, to)
                                .iterator();

                        @Override
                        public boolean hasNext() {
                            return entriesIterator.hasNext();
                        }

                        @Override
                        public FDate next() {
                            return HistoricalCacheAssertValue.unwrapEntryKey(core.getParent(), entriesIterator.next());
                        }

                        @Override
                        public void close() {
                            entriesIterator.close();
                        }

                    };
                }
            };
        }
    }

    /**
     * Returns all values in the given time range.
     */
    @Override
    public ICloseableIterable<Entry<FDate, V>> getEntries(final FDate from, final FDate to) {
        final ICloseableIterable<Entry<FDate, V>> iterableInterceptor = core.getParent().getRangeQueryInterceptor().getEntries(from,
                to);
        if (iterableInterceptor != null) {
            return iterableInterceptor;
        } else {
            return new ICloseableIterable<Entry<FDate, V>>() {
                @Override
                public ICloseableIterator<Entry<FDate, V>> iterator() {
                    return new ICloseableIterator<Entry<FDate, V>>() {
                        private final IHistoricalCacheQueryWithFuture<V> future = withFuture();
                        private Entry<FDate, V> nextEntry = future.getNextEntry(from, 0);
                        private FDate nextEntryKey = extractKey(nextEntry);

                        @Override
                        public boolean hasNext() {
                            return nextEntryKey != null && !nextEntryKey.isAfter(to);
                        }

                        private FDate extractKey(final Entry<FDate, V> e) {
                            return HistoricalCacheAssertValue.unwrapEntryKey(core.getParent(), e);
                        }

                        @Override
                        public Entry<FDate, V> next() {
                            if (hasNext()) {
                                //always returning current and reading ahead once
                                final Entry<FDate, V> currentEntry = nextEntry;
                                final FDate currentEntryKey = extractKey(currentEntry);
                                nextEntry = future.getNextEntry(currentEntryKey, 1);
                                nextEntryKey = extractKey(nextEntry);
                                if (nextEntry != null && !nextEntryKey.isAfter(currentEntryKey)) {
                                    nextEntry = null;
                                    nextEntryKey = null;
                                }
                                return currentEntry;
                            } else {
                                throw new NoSuchElementException();
                            }
                        }

                        @Override
                        public void close() {
                            nextEntry = null;
                            nextEntryKey = null;
                        }

                    };
                }
            };
        }
    }

    @Override
    public ICloseableIterable<V> getValues(final FDate from, final FDate to) {
        return new ICloseableIterable<V>() {
            @Override
            public ICloseableIterator<V> iterator() {
                return new ICloseableIterator<V>() {

                    private final ICloseableIterator<Entry<FDate, V>> entriesIterator;

                    {
                        final ICloseableIterable<Entry<FDate, V>> entries = getEntries(from, to);
                        entriesIterator = entries.iterator();
                    }

                    @Override
                    public boolean hasNext() {
                        return entriesIterator.hasNext();
                    }

                    @Override
                    public V next() {
                        return HistoricalCacheAssertValue.unwrapEntryValue(entriesIterator.next());
                    }

                    @Override
                    public void close() {
                        entriesIterator.close();
                    }

                };
            }
        };
    }

    @Override
    public FDate getPreviousValueKeyBetween(final FDate from, final FDate to, final V value) {
        FDate curKey = to;
        final Optional<V> optionalValue = Optional.fromNullable(value);
        boolean firstTry = true;
        while (true) {
            final int shiftBackUnits;
            if (firstTry) {
                shiftBackUnits = 0;
            } else {
                shiftBackUnits = 1;
            }
            final Entry<FDate, V> previousEntry = getPreviousEntry(curKey, shiftBackUnits);
            if (previousEntry == null) {
                return null;
            }
            final FDate previousKey = core.getParent().extractKey(previousEntry.getKey(), previousEntry.getValue());
            if (!firstTry && !previousKey.isBefore(curKey)) {
                return null;
            }
            curKey = previousKey;
            if (curKey.isBefore(from)) {
                return null;
            }
            final Optional<V> optionalCurValue = Optional.fromNullable(previousEntry.getValue());
            if (optionalValue.equals(optionalCurValue)) {
                return curKey;
            }
            firstTry = false;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void copyQuerySettings(final HistoricalCacheQuery query) {
        query.assertValue = assertValue;
        query.filterDuplicateKeys = filterDuplicateKeys;
        query.elementFilter = elementFilter;
        query.rememberNullValue = rememberNullValue;
    }

    private HistoricalCacheQueryWithFuture<V> newFutureQuery() {
        final HistoricalCacheQueryWithFuture<V> query = new HistoricalCacheQueryWithFuture<V>(core);
        copyQuerySettings(query);
        return query;
    }

    protected IHistoricalCacheQuery<?> newKeysQueryInterceptor() {
        if (elementFilter == null) {
            final HistoricalCacheQuery<?> interceptor = core.getParent().newKeysQueryInterceptor();
            if (interceptor != null) {
                copyQuerySettings(interceptor);
                return interceptor;
            }
        }
        return null;
    }

    protected final List<Entry<FDate, V>> newEntriesList(final int size) {
        if (filterDuplicateKeys) {
            /*
             * duplicates will only occur on the edged, never in the middle, so we can use this fast implementation
             */
            return new FilterDuplicateKeysList<V>(size);
        } else {
            return new ArrayList<Entry<FDate, V>>(size);
        }
    }

    private void checkAssertValueUnchangedAndSet(final HistoricalCacheAssertValue newAssertValue) {
        if (newAssertValue != assertValue) {
            if (assertValue != DEFAULT_ASSERT_VALUE) {
                throw new IllegalStateException("Either withFuture() or withFutureNull() can be used, but not both!");
            }
            this.assertValue = newAssertValue;
        }
    }

    @Override
    public final Entry<FDate, V> getPreviousEntry(final FDate key, final int shiftBackUnits) {
        assertShiftUnitsPositive(shiftBackUnits);
        return core.getPreviousEntry(internalMethods, key, shiftBackUnits);
    }

    /**
     * Skips null values for keys.
     * 
     * Fills the list with values from the past.
     */
    @Override
    public final ICloseableIterable<Entry<FDate, V>> getPreviousEntries(final FDate key, final int shiftBackUnits) {
        assertShiftUnitsPositiveNonZero(shiftBackUnits);
        return core.getPreviousEntries(internalMethods, key, shiftBackUnits);
    }

}
