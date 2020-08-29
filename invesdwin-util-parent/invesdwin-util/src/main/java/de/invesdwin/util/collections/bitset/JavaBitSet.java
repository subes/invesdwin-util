package de.invesdwin.util.collections.bitset;

import java.util.BitSet;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class JavaBitSet implements IBitSet {

    private final BitSet bitSet;
    private int trueCount = 0;

    public JavaBitSet() {
        this.bitSet = new BitSet();
        this.trueCount = -1;
    }

    public JavaBitSet(final BitSet bitSet) {
        this.bitSet = bitSet;
    }

    public JavaBitSet(final int expectedSize) {
        this.bitSet = new BitSet(expectedSize);
    }

    @Override
    public void add(final int index) {
        bitSet.set(index);
        trueCount++;
    }

    @Override
    public void remove(final int index) {
        bitSet.clear(index);
        trueCount = -1;
    }

    @Override
    public boolean contains(final int index) {
        return bitSet.get(index);
    }

    @Override
    public IBitSet optimize() {
        if (isEmpty()) {
            return EmptyBitSet.INSTANCE;
        } else {
            return this;
        }
    }

    @Override
    public IBitSet and(final IBitSet... others) {
        final BitSet combined = (BitSet) bitSet.clone();
        for (int i = 0; i < others.length; i++) {
            final IBitSet other = others[i];
            if (other.isEmpty()) {
                return EmptyBitSet.INSTANCE;
            }
            final JavaBitSet cOther = (JavaBitSet) other;
            combined.and(cOther.bitSet);
        }
        return new JavaBitSet(combined);
    }

    @Override
    public int getTrueCount() {
        if (trueCount == -1) {
            trueCount = bitSet.cardinality();
        }
        return trueCount;
    }

    @Override
    public boolean isEmpty() {
        return bitSet.isEmpty();
    }

    @Override
    public ISkippingIndexProvider newSkippingIndexProvider() {
        return cur -> {
            final int next = bitSet.nextSetBit(cur + 1);
            if (next == -1) {
                return ISkippingIndexProvider.END;
            } else {
                return next;
            }
        };
    }

}
