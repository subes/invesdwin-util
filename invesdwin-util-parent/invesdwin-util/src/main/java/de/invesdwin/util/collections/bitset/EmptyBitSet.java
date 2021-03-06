package de.invesdwin.util.collections.bitset;

import javax.annotation.concurrent.Immutable;

@Immutable
public class EmptyBitSet implements IBitSet {

    public static final EmptyBitSet INSTANCE = new EmptyBitSet();

    @Override
    public void add(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(final int index) {
        return false;
    }

    @Override
    public IBitSet optimize() {
        return this;
    }

    /**
     * We explicitly don't return an always true instance here because it does not add any knowledge and should instead
     * be skipped. A negated empty array is still empty, that is the rationale.
     */
    @Override
    public IBitSet negate() {
        return INSTANCE;
    }

    /**
     * We explicitly don't return an always true instance here because it does not add any knowledge and should instead
     * be skipped. A negated empty array is still empty, that is the rationale.
     */
    @Override
    public IBitSet negateShallow() {
        return INSTANCE;
    }

    @Override
    public IBitSet and(final IBitSet... others) {
        return INSTANCE;
    }

    @Override
    public IBitSet andRange(final int fromInclusive, final int toExclusive, final IBitSet[] others) {
        return INSTANCE;
    }

    @Override
    public int getTrueCount() {
        return 0;
    }

    @Override
    public int getExpectedSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ISkippingIndexProvider newSkippingIndexProvider() {
        return nextCandidate -> ISkippingIndexProvider.END;
    }

    @Override
    public IBitSet unwrap() {
        return this;
    }

}
